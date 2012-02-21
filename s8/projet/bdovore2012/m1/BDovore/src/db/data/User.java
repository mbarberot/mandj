package db.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import main.Config;

/**
 * Informations sur l'utilisateur
 * @author Thorisoka
 */
public class User {
	
	private int id;
	private String username;
	private String password; /* non-MD5 */
	
	/** Page PHP (sans paramètres) pour la mise à jour utilisateur (site->logiciel) */
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		this.id = getIdFromWebService(null);
	}
	
	public User(String username, String password, Proxy proxy) {
		this.username = username;
		this.password = password;
		this.id = getIdFromWebService(proxy);
	}
	
	public int getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	
	public boolean isValid() {
		return (id >= 0);
	}
	
	
	private int getIdFromWebService(Proxy proxy) {
		try {
			URL url = new URL(Config.USER_ID_SRC + "?username=" + username + "&password=" + password);
			//System.out.println(url);
			
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
			BufferedReader buffer = new BufferedReader(isr);
			
			String entry = buffer.readLine();
			
			return Integer.parseInt(entry);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
}
