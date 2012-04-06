package db.synch;

import db.DataBase;
import db.SynchQuery;
import java.net.Proxy;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
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
    public void updateGlobal()
    {
        updateBase();
        updateBDtheque();
    }

    /**
     * Met à jour la base de recherche
     */
    public void updateBase()
    {
        int lastID,         // Dernier id_edition de la base
                len,        // Nb éditions à ajouter durant l'itération courante
                cptBoucle,  // Nombre de boucle
                tailleLot;  // Nb édition reçues maximum par itération
        
        String res,                 // Chaine au format CVS pour la réception des id_edition manquants 
                genre,              // Nom du genre
                coloristes[],       // Id des coloristes
                dessinateurs[],     // Id des dessinateurs
                scenaristes[],      // Id des scénaristes
                sql,           // Requête SQL qui sera construite
                editions[];         // Editions manquantes passées du CSV en un tableau
        
        DetailsEdition dEdition;        // Objet edition du webservice
        DetailsVolume dTome;            // Objet tome du webservice
        DetailsSerie dSerie;            // Objet série du webservice
        DetailsAuteur dAuteur, dTj;     // Objet auteur du webservice (+ dTj pour la table TJ_TOME_AUTEUR)
        DetailsEditeur dEditeur;        // Objet editeur du webservice

        // Synchronisation
        try
        {
            // Initialisation
            cptBoucle = 0;
            tailleLot = 0;
            
            // Boucle de mise à jour
            do
            {
                sql = "";
                
                // Récupération de l'ID du dernier tome de la base
                lastID = update.getLastIdEdition();
                
                // Récupération des editiones manquantes
                res = port.getEditionsManquantes(lastID);
                // CSV -> String[]
                editions = res.split(";");
                
                // Nombre d'ID_EDITION reçus :
                len = editions.length;
                
                // Initialisation de la taille de chaque lot de données
                // + gestion de la taille = 0 (sinon : boucle infinie)
                if(cptBoucle == 0) { tailleLot = len; }
                else if(tailleLot == 0) { break; } 
                
                // Récupération des informations depuis le webservice
                // + création de la requête SQL avec l'objet Update
                for (int i = 0; i < len; i++)
                {
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
                    
                    // Création de la requête
                    sql += update.genre(dTome.getIdGenre(), genre) + "\n";
                    sql += update.serie(dSerie) + "\n";
                    sql += update.auteur(dAuteur) + "\n";
                    sql += update.volume(dTome) + "\n";
                    sql += tj_tome_auteur(dTome.getIdTome(), coloristes, dessinateurs, scenaristes) +"\n";
                    sql += update.editeur(dEditeur) + "\n";
                    sql += update.edition(dEdition) + "\n";
                }
                
                // Affichage temporaire
                System.out.println(sql);
                
                // Execution de la requête
                this.db.update(sql);
                
                // Incrémentation du compteur de boucle pour éviter 
                // les boucles infinies si aucune données à récupérer
                // len == 0 & tailleLot == 0
                cptBoucle++;

                // Break temporaire
                break;
                
                
                
            } while (len == tailleLot);
            
        } catch (RemoteException ex)
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

    /**
     * 
     * @param idTome
     * @param coloristes
     * @param dessinateurs
     * @param scenaristes
     * @return
     * @throws RemoteException 
     */
    private String tj_tome_auteur(int idTome, String[] coloristes, String[] dessinateurs, String[] scenaristes) throws RemoteException
    {
        int j;
        DetailsAuteur dTj;
        String sql = "";

        if (coloristes.length > 1)
        {
            for (j = 1; j < coloristes.length; j++)
            {
                dTj = port.getDetailsAuteur(Integer.parseInt(coloristes[j]));
                sql += update.auteur(dTj) + "\n";
                sql += update.tj(idTome, coloristes[j], "Coloristes") + "\n";
            }
        }

        if (dessinateurs.length > 1)
        {
            for (j = 1; j < dessinateurs.length; j++)
            {
                dTj = port.getDetailsAuteur(Integer.parseInt(dessinateurs[j]));
                sql += update.auteur(dTj) + "\n";
                sql += update.tj(idTome, dessinateurs[j], "Dessinateur") + "\n";
            }
        }

        if (scenaristes.length > 1)
        {
            for (j = 1; j < scenaristes.length; j++)
            {
                dTj = port.getDetailsAuteur(Integer.parseInt(scenaristes[j]));
                sql += update.auteur(dTj) + "\n";
                sql += update.tj(idTome, scenaristes[j], "Scenariste") + "\n";
            }
        }
        return sql;
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
