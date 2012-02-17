package db.update;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.NoRouteToHostException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import main.Config;
import db.DataBase;
import db.Tables;
import db.data.User;
import au.com.bytecode.opencsv.CSVReader;


/**
 * Module de mise à jour de la base Utilise un Web Service fournissant, au
 * format CSV, les entrées manquante à partir d'une table et d'un identifiant
 * donné.
 * 
 * @author Thorisoka
 */
public class Updater {
	
	
	/** Taille des requêtes d'insertion */
	public static final int INSERT_SIZE = 1;
	

	DataBase db;
	Proxy proxy;
	
	
	/**
	 * Constructeur sans proxy
	 * 
	 * @param db
	 */
	public Updater(DataBase db) {
		this.db = db;
		this.proxy = null;
	}
	
	
	/**
	 * Constructeur avec proxy
	 * 
	 * @param db
	 * @param proxy
	 */
	public Updater(DataBase db, Proxy proxy) {
		this.db = db;
		this.proxy = proxy;
	}
	
	

	/**
	 * Mise à jour : pour chaque table, interroge le web service.
	 * 
	 * @throws SQLException
	 * @throws SocketTimeoutException
	 * @throws NoRouteToHostException
	 */
	public void update() throws Exception {
		
		for (Iterator<String> it = Tables.ids.keySet().iterator(); it.hasNext();) {
			String table = (String) it.next();
			this.update(table, Config.MAJ_SRC, db.getLastID(table));
		}
	}
	
	

	public boolean getUserData(String username, String password) throws Exception {
		return getUserData(new User(username, password));
	}
	
	public boolean getUserData(User user) throws Exception {
		
		if (!user.isValid()) return false;
		
		db.deleteUserData();
		
		// TODO : Retourner nombre d'entrées ajoutées ?
		this.update("USERS_ALBUM", Config.USER_SRC, user.getId());
		
		return true;
	}
	
	

	/**
	 * Execute la mise à jour d'une table : <br>
	 * - Récupère les entrées manquante <br>
	 * - Parse le CSV pour en produire des requêtes <br>
	 * - INSERT dans la base
	 * 
	 * @param table
	 * @throws SQLException
	 */
	private void update(String table, String src, int id) throws Exception {
		

		URL url = new URL(src + "?table=" + table + "&id=" + id);
		// System.out.println(url);
		
		// Ouverture d'une connexion
		HttpURLConnection conn;
		
		if (proxy == null)
			conn = (HttpURLConnection) url.openConnection();
		else conn = (HttpURLConnection) url.openConnection(proxy);
		
		conn.setRequestMethod("GET");
		conn.connect();
		// conn.setDoInput(true);
		// conn.setUseCaches(false);
		

		// Récupération dans un Buffer (! Attention au CHARSET !)
		
		InputStream istream = conn.getInputStream();
		Charset cs = Charset.forName("ISO-8859-1");
		InputStreamReader isr = new InputStreamReader(istream, cs);
		// BufferedReader buffer = new BufferedReader(isr);
		
		// String entry;
		

		CSVReader csv = new CSVReader(isr, ',', '"', 1);
		
		String insert;
		while ((insert = this.makeBatchInsert(table, csv, INSERT_SIZE)) != null) {
			try {
				db.update(insert);
			} catch (Exception e) {
				System.out.println(insert);
				System.out.println(e);
				db.shutdown(); // Inutile après (on exit pas férocement)
			}
		}
		

	}
	
	


	/**
	 * Génère un batch de <code>limit</code> insertions SQL depuis le buffer CSV
	 * 
	 * @param table
	 * @param buffer
	 * @param limit Nombre d'insertion maximal par requête
	 * @return
	 * @throws IOException
	 */
	private String makeBatchInsert(String table, CSVReader csv, int limit) throws IOException {
		
		String query;
		String[] entry;
		ArrayList<String> entries = new ArrayList<String>();
		int cur = 0;
		
		query = "INSERT INTO " + table + " VALUES ";
		
		// Entry de la forme tableau de champs
		while ((cur < limit) && ((entry = csv.readNext()) != null)) {
			
			// On "patche" les entrées (null et quotes)
			for (int i = 0; i < entry.length; i++) {
				if (entry[i].equals("") && Tables.nullIfEmpty.get(table)[i])
					entry[i] = "NULL";
				else {
					if (Tables.quotes.get(table)[i]) entry[i] = "'" + entry[i] + "'";
				}
			}
			
			entries.add("(" + implode(entry, ",") + ")");
			cur++;
		}
		
		query += implode(entries, ",");
		

		if (cur == 0) return null;
		
		return query;
	}
	
	



	/**
	 * Implose un tableau de String avec un séparateur
	 * 
	 * @param inputArray le tableau
	 * @param glue le caractère séparateur
	 * @return
	 */
	private String implode(String[] inputArray, String glue) {
		String AsImplodedString;
		
		if (inputArray.length == 0)
			AsImplodedString = "";
		
		else {
			StringBuffer sb = new StringBuffer();
			
			sb.append(inputArray[0]);
			for (int i = 1; i < inputArray.length; i++) {
				sb.append(glue);
				sb.append(inputArray[i]);
			}
			
			AsImplodedString = sb.toString();
		}
		
		return AsImplodedString;
	}
	
	
	/**
	 * Implosion, version ArrayList<String>
	 * 
	 * @param liste
	 * @param glue
	 * @return
	 */
	private String implode(ArrayList<String> liste, String glue) {
		return implode((String[]) liste.toArray(new String[1]), glue);
	}
}
