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
        this.update = new Update(db);
        try
        {
            this.port = new BDovoreLocator().getBDovore_Port();
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
        int lastID,
                len,
                tailleLot;
        String res,
                genre,
                coloristes[],
                dessinateurs[],
                scenaristes[],
                sql = "",
                editions[];
        DetailsEdition dEdition;
        DetailsVolume dTome;
        DetailsSerie dSerie;
        DetailsAuteur dAuteur, dTj;
        DetailsEditeur dEditeur;

        try
        {
            tailleLot = 0;
            // Boucle de mise à jour
            do
            {
                // Récupération de l'ID du dernier tome de la base
                lastID = db.getLastID("EDITION");
                // Récupération des editiones manquantes
                res = port.getEditionsManquantes(lastID);
                editions = res.split(";");
                len = editions.length;
                if(tailleLot == 0) { tailleLot = len; }
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
                    // Insertion des données
                    sql += update.genre(dTome.getIdGenre(), genre) + "\n";
                    sql += update.serie(dSerie) + "\n";
                    sql += update.auteur(dAuteur) + "\n";
                    sql += update.volume(dTome) + "\n";
                    sql += tj_tome_auteur(dTome.getIdTome(), coloristes, dessinateurs, scenaristes) +"\n";
                    sql += update.editeur(dEditeur) + "\n";
                    sql += update.edition(dEdition) + "\n";
                    
                }
                System.out.println(sql);
                this.db.update(sql);
                break;
            } while (len == tailleLot);
            
        } catch (RemoteException ex)
        {
            ex.printStackTrace();
        } catch (SQLException ex)
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
