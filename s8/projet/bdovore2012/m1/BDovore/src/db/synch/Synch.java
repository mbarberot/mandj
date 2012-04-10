package db.synch;

import db.DataBase;
import db.SynchQuery;
import java.net.Proxy;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
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
    /*
     * Objet 'intelligent' de création des requêtes
     */
    private Update update;
    
    /*
     * Thread d'update
     */
    private UpdateBase updateBase;
    
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
        //updateBase();
        updateBDtheque();
    }
    
    /**
     * Arrete le thread
     */
    public void cancel()
    {
        updateBase.cancel();
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
