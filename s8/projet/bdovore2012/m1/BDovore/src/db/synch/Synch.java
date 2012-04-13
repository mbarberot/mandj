package db.synch;

import db.DataBase;
import db.data.User;
import java.net.Proxy;
import util.UpdateBaseListener;
import wsdl.server.*;

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

    /**
     * Objet permettant d'envoyer et de recevoir les requêtes du webservice
     */
    private BDovore_PortType port;
    /**
     * Objet permettant de manipuler la base de données
     */
    private DataBase db;
    /**
     * Proxy pour la connection à internet
     */
    private Proxy proxy;
    /**
     * Objet 'intelligent' de création des requêtes
     */
    private Update update;
    /**
     * Thread d'update des éditions de la base de recherche
     */
    private UpdateBase updateBase;
    /**
     * Thread d'update des editions de l'utilisateur
     */
    private UpdateUser updateUser;
    
    //
    // Constructeurs
    //
    /**
     * Constructeur sans proxy
     *
     * @param db Objet de manipulation de la base de données
     */
    public Synch(DataBase db)
    {
        this(db, null);
    }

    /**
     * Constructeur avec proxy
     *
     * @param db Objet de manipulation de la base de données
     * @param proxy Proxy pour se connecter à internet
     */
    public Synch(DataBase db, Proxy proxy)
    {
        this.db = db;
        this.proxy = proxy;
        this.update = null;
        try
        {
            this.port = new BDovoreLocator().getBDovore_Port();
            this.update = new Update(db);
        } catch (Exception ex)
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
    public void updateGlobal(UpdateBaseListener listener)
    {
        // Mise en place du thread
        updateBase = new UpdateBase(update, db, port, listener);
        updateBase.start();
    }
    
    /**
     * Arrete le thread de mise à jour globale
     */
    public void cancelGlobal()
    {
        updateBase.cancel();
    }

    /**
     * Met à jour la bdtheque de l'utilisateur
     */
    public void updateBDtheque(User user)
    {
        updateUser = new UpdateUser(db, user, update, port);
        updateUser.start();
    }
  
    /**
     * Arrete le thread de mise à jour globale
     */
    public void cancelUser()
    {
        updateUser.cancel();
    }

    //
    // Getters and Setters
    //
    public DataBase getDb()
    {
        return db;
    }

    public void setDb(DataBase db)
    {
        this.db = db;
    }

    public BDovore_PortType getPort()
    {
        return port;
    }

    public void setPort(BDovore_PortType port)
    {
        this.port = port;
    }

    public Proxy getProxy()
    {
        return proxy;
    }

    public void setProxy(Proxy proxy)
    {
        this.proxy = proxy;
    }

    
}
