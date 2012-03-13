package db.data;

import java.net.Proxy;

/**
 * Informations sur l'utilisateur
 *
 * @author Thorisoka
 */
public class User {

    // User's ID
    private int id;
    // User's username
    private String username;
    // User's password
    // Attention : il n'est pas en MD5
    private String password;

    /**
     * Constructeur
     *
     * @param username Username
     * @param password Password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.id = getIdFromWebService(null);
    }

    /**
     * Constructeur
     *
     * @param username Username
     * @param password Password
     * @param proxy Proxy
     */
    public User(String username, String password, Proxy proxy) {
        this.username = username;
        this.password = password;
        this.id = getIdFromWebService(proxy);
    }

    /*
     * Getters
     */
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

    /**
     * Utilise le WebService pour trouver l'ID d'un utilisateur
     *
     * @param proxy Proxy
     * @return L'id de l'utilisateur ou -1 si l'utilisateur n'existe pas
     */
    private int getIdFromWebService(Proxy proxy) {
        /*
         * 
         *
         * TODO : Utiliser le WebService
         *
         *
         *
         * Ancienne méthode (une sorte d'Ajax) :
         * ---------------------------------------------------
         * 
         * try {
         *
         * URL url = new URL(Config.USER_ID_SRC + "?username=" + username +
         * "&password=" + password); //System.out.println(url);
         *
         * // Ouverture d'une connexion HttpURLConnection conn;
         *
         * if (proxy == null) { conn = (HttpURLConnection) url.openConnection();
         * } else { conn = (HttpURLConnection) url.openConnection(proxy); }
         *
         * conn.setRequestMethod("GET"); conn.connect(); //
         * conn.setDoInput(true); // conn.setUseCaches(false);
         *
         *
         * // Récupération dans un Buffer (! Attention au CHARSET !)
         *
         * InputStream istream = conn.getInputStream(); Charset cs =
         * Charset.forName("ISO-8859-1"); InputStreamReader isr = new
         * InputStreamReader(istream, cs); BufferedReader buffer = new
         * BufferedReader(isr);
         *
         * String entry = buffer.readLine();
         *
         * return Integer.parseInt(entry);
         *
         * } catch (MalformedURLException e) { e.printStackTrace(); } catch
         * (IOException e) { e.printStackTrace(); }
         *
         *
         */
        return -1;
    }
}
