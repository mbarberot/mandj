package db.synch;

import db.DataBase;
import db.SynchQuery;
import java.net.Proxy;
import java.sql.SQLException;
import wsdl.server.BDovoreLocator;
import wsdl.server.BDovore_PortType;
import org.

/**
 * Objet permettant la synchronisation de la base avec le serveur BDovore
 * 
 * @author Barberot Mathieu et Racenet Joan
 */
public class Synch 
{
    //
    // Attributs
    //
    
    /** Objet permettant d'envoyer et de recevoir les requêtes du webservice */
    private BDovore_PortType port;
    
    /** Objet permettant de manipuler la base de données */
    private DataBase db;
    
    /** Proxy pour la connection à internet */
    private Proxy proxy;
    
    
    
    //
    // Constructeurs
    //
    
    /**
     * Constructeur sans proxy
     * @param db Objet de manipulation de la base de données
     */
    public Synch(DataBase db)
    {
        this(db,null);
    }
    
    /**
     * Constructeur avec proxy
     * @param db Objet de manipulation de la base de données
     * @param proxy Proxy pour se connecter à internet
     */
    public Synch(DataBase db, Proxy proxy)
    {
        this.db = db;
        this.proxy = proxy;
        
        try
        {
            this.port = new BDovoreLocator().getBDovore_Port();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
    }
    
    //
    // Méthodes
    //
    
    /**
     * Commande une mise à jour globale
     */
    public void updateGlobal()
    {
        updateBase();
        updateBDtheque();
    }
    
    /**
     * Met à jour la base de recherche
     */
    public void updateBase ()
    {
        
        // Récupération de l'ID du dernier tome de la base
        String sql = SynchQuery.getLastID();
        
        try 
        { 
            db.query(sql);
        }
        catch( SQLException ex )
        {
            ex.printStackTrace();
        }
        
        // Requête au webservice
        
        // Insertion des nouveaux tomes dans la base
        
    }
    
    /**
     * Met à jour la bdtheque de l'utilisateur
     */
    public void updateBDtheque()
    {
        
    }
    
    
    
    //
    // Getters and Setters
    //
    public DataBase getDb() {
        return db;
    }

    public void setDb(DataBase db) {
        this.db = db;
    }

    public BDovore_PortType getPort() {
        return port;
    }

    public void setPort(BDovore_PortType port) {
        this.port = port;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }
}
