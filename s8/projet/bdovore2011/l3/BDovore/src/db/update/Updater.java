package db.update;

import gui.FrameMain;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.NoRouteToHostException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import main.Clavier;
import main.Config;
import db.DataBase;
import db.Tables;
import db.data.Edition;
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
	
	public static final ClassEmptyWS wS = new ClassEmptyWS();
	
	
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
	public void updateGlobal() throws Exception {
		
		String[] ordreUpdateTables = {"EDITEUR","AUTEUR","GENRE","SERIE","TOME","EDITION"};
		String reponseWS ="";
		for(int i=0; i<ordreUpdateTables.length; i++){
			//System.out.println("table mise � jour: " + ordreUpdateTables[i]);
			reponseWS = wS.getNewEntries(FrameMain.currentUser.getUsername(),FrameMain.currentUser.getPassword(),ordreUpdateTables[i],db.getLastID(ordreUpdateTables[i]));
			this.update(ordreUpdateTables[i], reponseWS);
		}
		
	}
	/**
	 * Execute la mise à jour d'une table : <br>
	 * - Récupère les entrées manquante <br>
	 * - Parse le CSV pour en produire des requêtes <br>
	 * - INSERT dans la base
	 * 
	 * @param table
	 * @param reponse 
	 * @throws SQLException
	 */
	public void update(String table, String reponse) throws Exception {
		
		String reponseWS = reponse;
		String insert = null;
		
		
		CSVReader csv = createCSVReader(reponseWS);

		while ((insert = this.makeBatchInsert(table, csv, INSERT_SIZE)) != null) {
			try {
				db.update(insert);
			} catch (Exception e) {
				System.out.println(insert);
				System.out.println(e);
				//db.shutdown();  Inutile apr�s (on exit pas f�rocement)
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
	
	/**
	 * Fonction qui, � partir d'un String, retourne un CSVReader
	 * @param s
	 * @return
	 */
	public static CSVReader createCSVReader(String s){
		
		// R�cup�ration dans un Buffer (! Attention au CHARSET !)
		InputStream istream =  new ByteArrayInputStream(s.getBytes());
		Charset cs = Charset.forName("ISO-8859-1");
		InputStreamReader isr = new InputStreamReader(istream, cs);
		//BufferedReader buffer = new BufferedReader(isr);
		
		
		return new CSVReader(isr, ',', '"', 1);
		
		
	}
	

	/** Fonction permettant de synchroniser le compte de l'utilisateur
	 * 
	 * - Traite le CSV transmis par le WS
	 * - parcourt toutes les editions contenu dans la table BD_USER ainsi que toutes les transactions ( meme requete)
	 * pour chaque �dition on regarde si elle existe dans le csv et on effectue le traitement appropri� selon la 
	 * valeur de la transaction associ�e (ou si pas de transaction pour cet id). 
	 * - Une fois le traitement effectu� on supprime la ligne correspondant � l'id trait� dans le CSV.
	 * - Une fois parcourue toutes les editions de BD_USER on regarde s'il reste des ligne dans le csv et 
	 * �ventuellement on traite les cas restant.
	 * 
	 * 
	 * @param 
	 * @throws Exception 
	 */
	public void synchronizeUserAccount() throws Exception {
		
		// Faire un mise � jour globale avant de synchroniser
		updateGlobal();
		
		String user = FrameMain.currentUser.getUsername();
		String passwd = FrameMain.currentUser.getPassword();
		
		String sql = 	"SELECT * \n" +
						"FROM BD_USER LEFT OUTER JOIN TRANSACTION \n" +
						"ON BD_USER.ID_EDITION=TRANSACTION.ID_EDITION";
		
		Statement st = null;
		ResultSet rs = null;
		st = FrameMain.db.getConn().createStatement();
		rs = st.executeQuery(sql); // run the query
		
		String lstEditions = "";
		
		// r�cupere la liste des �ditions poss�d�s sur le compte du site
		lstEditions = wS.getListEditionsUser(user, passwd);

		CSVReader csv = Updater.createCSVReader(lstEditions);
		
		int i;
		
		String[] lineEd;
		
		// Parcours des ouvrages + transactions en local
		while(rs.next()){			
			i = rs.getInt("ID_EDITION");
			// On r�cup�re l'�dition a la ligne i si elle existe dans le csv
			lineEd = getLine(i, csv);
			
			// Si pour l'idEdition i, edition pr�sente en local et sur le site
			if(lineEd != null){	
				
				switch(rs.getInt("TYPE")){
					case Edition.INSERT :
						compareLineCSV2Local(rs,lineEd, true);
						break;
					case Edition.UPDATE :
						compareLineCSV2Local(rs,lineEd, true);
						break;
					default :
						compareLineCSV2Local(rs,lineEd, false);
						break;
				}
				// Supprime la ligne i du csv 
				deleteLine(i, csv);
				
			}
			// si pour l'idEdition i, l'edition est pr�sente qu'en local
			else{			
				switch(rs.getInt("TYPE")){
					case Edition.INSERT :
						// On ajoute l'edition sur le site
						wS.ajouter(user, passwd, i,rs.getInt("FLG_PRET"),rs.getInt("FLG_DEDICACE"),rs.getInt("FLG_AACHETER"),rs.getDate("DATE_AJOUT"));
						break;
					case Edition.UPDATE :
						//Ouvrage modifi� en local et supprim� sur le site --> conflit 
						//On demande quoi faire a l'utilisateur
						int choice = 0;
						choice = demandeUserConflict(i);
						if(choice == 2){
							wS.ajouter(user, passwd, i,rs.getInt("FLG_PRET"),rs.getInt("FLG_DEDICACE"),rs.getInt("FLG_AACHETER"),rs.getDate("DATE_AJOUT"));
						}else{
							FrameMain.db.update("DELETE FROM BD_USER WHERE ID_EDITION=" + i + ";");
						}
						break;
					default :
						FrameMain.db.update("DELETE FROM BD_USER WHERE ID_EDITION=" + i + ";");
						break;
				}
			}
		}
		
		// Ici, le csv ne comporte plus que les �ditions qui ne sont pas enregistr�s en local
		sql = 	"SELECT * \n" +
				"FROM TRANSACTION";
		st = null;
		st = FrameMain.db.getConn().createStatement();
		rs = st.executeQuery(sql);
		
		
		boolean find = false;
		try{
			while((lineEd = csv.readNext()) != null){
				find = false;
				rs = st.executeQuery(sql);
				while(rs.next() && !find){
					if(Integer.parseInt(lineEd[0]) == rs.getInt("ID_EDITION")){ 
						//�dition pr�sente sur site, absente local, transaction pr�sente => une seule transaction possible(suppr) on supprime donc sur site
						wS.supprimer(user, passwd, Integer.parseInt(lineEd[0]));
						find = true;
					}
				}

				if(!find){			
					//Ouvrage pr�sent sur site et absent en local et pas de transaction: on ajoute l'�dition dans la base local
					FrameMain.db.update("INSERT INTO BD_USER VALUES("+Integer.parseInt(lineEd[0])+", "+Integer.parseInt(lineEd[1])+", "+Integer.parseInt(lineEd[2])+", "+Integer.parseInt(lineEd[3])+", "+lineEd[4]+");");
					
					//On telecharge les infos sur l'�dition
					/** TODO: telecharger les infos sur l'�dition mais ne pas telecharger si les infos sont d�ja pr�sentes...
					 */
				}
			}
		}catch(Exception e){
			e.getMessage();
			e.printStackTrace();
		}
		
		st.close();
		
		// A la fin de la synchronisation, On supprime les donn�es de la table transaction
		sql = 	"DELETE *\n " +
				"FROM TRANSACTION";
		FrameMain.db.update(sql);
		
	}
	
	/** Compare les param�tres (Les flags) d'une �dition entre la base locale et le site 
	 * 
	 * @param rs 	edition courante de la base locale
	 * @param lineEd 	ligne du csv courante (qui correspond � l'�dition courante mais sur le compte utilisateur du site)
	 * @param hasTrans 	s'il existe une transaction pour l'edition courante
	 * @throws SQLException
	 */
	public void compareLineCSV2Local(ResultSet rs, String[] lineEd, boolean hasTrans) throws SQLException{
		
		final int PRET = 0;
		final int DEDICACE = 1;
		final int AACHETER = 2;
		
		int[] local_flg = {rs.getInt("FLG_PRET"),rs.getInt("FLG_DEDICACE"),rs.getInt("FLG_AACHETER")};
		
		int[] site_flg = {Integer.parseInt(lineEd[1]),Integer.parseInt(lineEd[2]),Integer.parseInt(lineEd[3])};
		
		int choice = 0;
		
		// S'il n'y a pas de transaction, on modifie sur le local
		if(!hasTrans){
			FrameMain.db.update("UPDATE BD_USER SET FLG_PRET=" + site_flg[PRET] + ", FLG_DEDICACE=" + site_flg[DEDICACE] + ", FLG_AACHETER=" + site_flg[AACHETER] + " WHERE ID_EDITION=" + lineEd[0] + ";");	
		}else{
			// S'il y a une transaction (modifi� sur local) et que au moins une des infos n'est pas la m�me sur le local et sur le site
			if(local_flg[PRET]!=site_flg[PRET] || local_flg[DEDICACE]!=site_flg[DEDICACE] || local_flg[AACHETER]!=site_flg[AACHETER]){
			
				// on demande a l'utilisateur quelles infos il veut garder
				choice = demandeUserConflict(Integer.parseInt(lineEd[0]));
				
				// Si l'utilisateur � choisi de garder les infos locales, mettre � jour le site
				if(choice == 2){
					wS.modifier(FrameMain.currentUser.getUsername(), FrameMain.currentUser.getPassword(), Integer.parseInt(lineEd[0]), local_flg[PRET], local_flg[DEDICACE], local_flg[AACHETER]);
				// Sinon, mettre � jour le local
				}else{
					FrameMain.db.update("UPDATE BD_USER SET FLG_PRET=" + site_flg[PRET] + ", FLG_DEDICACE=" + site_flg[DEDICACE] + ", FLG_AACHETER=" + site_flg[AACHETER] + " WHERE ID_EDITION=" + lineEd[0] + ";");	
				}
			}
		}
	
	}
	
	/**
	 * Renvoie un String[] correspondant � l'�dition � la ligne pass�e en parametre
	 * sous la forme : (idEdition, FLG_P, FLG_D, FLG_A, Date)
	 * 
	 * @param ln : num�ro de la ligne a renvoyer
	 * @return String[] sous la forme (idEdition, FLG_P, FLG_D, FLG_A, Date)
	 */
	public String[] getLine(int iD, CSVReader csv){
		
		String[] curLine;
		try {
			
			while((curLine = csv.readNext()) != null){ 		// Parcours des lignes du csv
				if(Integer.parseInt(curLine[0]) == iD)
					return curLine;

				// TODO : Supprimer la ligne courante du CSV venu du WS
			}
				return null;
		} catch (Exception e){}
		return null;
	}
	/** Fonction qui supprime la ligne i du csv
	 * 
	 * @param i
	 * @param csv
	 */
	public void deleteLine(int i, CSVReader csv){
		
		/** TODO: faire la fonction qui supprime une ligne du csv apr�s l'avoir trait�e
		*/
		
	}
	
	/** Demande � l'utilisateur son choix lors de conflits
	 * 
	 * @param id
	 * @return
	 */
	public int demandeUserConflict(int id){
		
		/** TODO: demander le choix � l'utilisateur en mode graphique */
		
		System.out.println("Conflit sur l'edition avec pour id " + id + ". \n" +
				"Que faire ? \n" +
				"1 - Priorite au site \n" +
				"2 - Priorite au logiciel \n");
		return Clavier.saisirInt();
	}
}
