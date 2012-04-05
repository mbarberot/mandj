package db.synch;

import db.DataBase;
import db.SynchQuery;
import java.net.Proxy;
import java.rmi.RemoteException;
import java.sql.SQLException;
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
        int     lastID, 
                len,
                j;
        String  res,
                genre,
                coloristes[],
                dessinateurs[],
                scenaristes[],
                sql = "",
                editions[];
        
        Update update = new Update(db);
        
        DetailsEdition dEdition;
        DetailsVolume dTome;
        DetailsSerie dSerie;
        DetailsAuteur dAuteur,dTj;
        DetailsEditeur dEditeur;
        
        try 
        {
            do {
                
                // Récupération de l'ID du dernier tome de la base
                lastID = db.getLastID("EDITION");

                // Récupération des editiones manquantes
                res = port.getEditionsManquantes(lastID);
                editions = res.split(";");

                
                len = 1; //editions.length;
                for (int i = 0; i < len; i++) {
                    
                    // Récupération des détails
                    dEdition = port.getDetailsEdition(Integer.parseInt(editions[i]));
                    dEditeur = port.getDetailsEditeur(dEdition.getIdEditeur());
                    dTome = port.getDetailsTome(dEdition.getIdTome());
                    dSerie = port.getDetailsSerie(dTome.getIdSerie());
                    dAuteur = port.getDetailsAuteur(dTome.getIdAuteur());
                    coloristes = (port.getColoristesTome(dTome.getIdTome())).split(";");
                    dessinateurs = (port.getDessinateursTome(dTome.getIdTome())).split(";");
                    scenaristes = (port.getScenaristesTome(dTome.getIdTome())).split(";");
                    genre = port.getGenre(dTome.getIdGenre());
                    
                    
                    
                    // Insertion des données
                    sql += update.genre(dTome.getIdGenre(), genre) + "\n";
                    sql += update.serie(dSerie) + "\n";
                    sql += update.auteur(dAuteur) + "\n";
                    sql += update.volume(dTome) + "\n";

                    if(coloristes.length > 1)
                    {
                        for(j = 1; j < coloristes.length; j++)
                        {
                            dTj = port.getDetailsAuteur(Integer.parseInt(coloristes[i]));
                            sql += SynchQuery.insertAuteur(dTj) + "\n";
                            sql += SynchQuery.insertTjTomeAuteur(dTome.getIdTome(), coloristes[i], "Coloristes") + "\n";
                        }
                    }
                    
                    if(dessinateurs.length > 1)
                    {
                        for(j = 1; j < dessinateurs.length; j++)
                        {
                            dTj = port.getDetailsAuteur(Integer.parseInt(dessinateurs[i]));
                            sql += SynchQuery.insertAuteur(dTj) + "\n";
                            sql += SynchQuery.insertTjTomeAuteur(dTome.getIdTome(), dessinateurs[i], "Dessinateur") + "\n";
                        }
                    }
                    
                    if(scenaristes.length > 1)
                    {
                        for(j = 1; j < scenaristes.length; j++)
                        {
                            dTj = port.getDetailsAuteur(Integer.parseInt(scenaristes[i]));
                            sql += SynchQuery.insertAuteur(dTj) + "\n";
                            sql += SynchQuery.insertTjTomeAuteur(dTome.getIdTome(), scenaristes[i], "Scenariste") + "\n";
                        }
                    }
                    
                    sql += update.editeur(dEditeur) + "\n";
                    sql += update.edition(dEdition) + "\n";
                    
                    
                    System.out.println(sql);
                    
                }
                
                //this.db.update(sql);
                
            } while(len == 1000);
            
            
        } 
        catch (RemoteException ex) 
        {
            ex.printStackTrace();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }


        
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
