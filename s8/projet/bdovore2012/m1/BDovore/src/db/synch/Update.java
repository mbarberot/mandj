package db.synch;

import db.DataBase;
import db.SynchQuery;
import db.Tables;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import wsdl.server.DetailsAuteur;
import wsdl.server.DetailsEditeur;
import wsdl.server.DetailsEdition;
import wsdl.server.DetailsSerie;
import wsdl.server.DetailsVolume;

/**
 * Fonctions d'update 'intelligentes'
 *
 * @author Barberot Mathieu & Racenet Joan
 */
public class Update
{
    /** La base de donnée à utiliser */
    private DataBase db;
    
    /** Dernier id_genre de la base locale */
    private int lastIdGenre;
    /** Dernier id_serie de la base locale */
    private int lastIdSerie;
    /** Dernier id_auteur de la base locale */
    private int lastIdAuteur;
    /** Dernier id_editeur de la base locale */
    private int lastIdEditeur;
    /** Dernier id_edition de la base locale */
    private int lastIdEdition;
    /** Dernier id_tome de la base locale */
    private int lastIdTome;

    

    public Update(DataBase db) throws SQLException
    {
        this.db = db;
        init();
    }
    
    private void init() throws SQLException
    {
        this.lastIdAuteur = db.getLastID("AUTEUR");
        this.lastIdSerie = db.getLastID("SERIE");
        this.lastIdGenre = db.getLastID("GENRE");
        this.lastIdEditeur = db.getLastID("EDITEUR");
        this.lastIdEdition = db.getLastID("EDITION"); 
        this.lastIdTome = db.getLastID("TOME"); 
    }

    public String edition(DetailsEdition dEdition)
    {
        String str = "";
        if(lastIdEdition < dEdition.getIdEdition())
        {
            str = SynchQuery.insertEdition(dEdition);
            this.lastIdEdition = dEdition.getIdEdition();
        }
        return str;
    }

    public String editeur(DetailsEditeur dEditeur)
    {
        String str = "";
        if (lastIdEditeur < dEditeur.getIdEditeur())
        {
            str = SynchQuery.insertEditeur(dEditeur);
            lastIdEditeur = dEditeur.getIdEditeur();
        }
        return str;
    }

    public String tj(int idTome, String auteur, String role)
    {
        String str = "";
        if (!isAlreadyInDataBase(SynchQuery.getCount(idTome, auteur, role) ))
        {
            str = SynchQuery.insertTjTomeAuteur(idTome, auteur, role);
        }       
        return str;
    }

    public String volume(DetailsVolume dVolume)
    {
        String str = "";
        if (lastIdTome < dVolume.getIdTome())
        {
             str = SynchQuery.insertVolume(dVolume);
             lastIdTome = dVolume.getIdTome(); 
       }
        return str;
    }

    public String auteur(DetailsAuteur dAuteur)
    {
        String str = "";
        if (lastIdAuteur < dAuteur.getIdAuteur())
        {
            str = SynchQuery.insertAuteur(dAuteur);
            lastIdAuteur = dAuteur.getIdAuteur();
        }

        return str;
    }

    public String serie(DetailsSerie dSerie)
    {
        String str = "";
        if (lastIdSerie < dSerie.getIdSerie())
        {
            str = SynchQuery.insertSerie(dSerie);
            lastIdSerie = dSerie.getIdSerie();
        }
        return str;
    }

    public String genre(int idGenre, String nomGenre)
    {
        String str = "";

        if (lastIdGenre < idGenre)
        {
            str = SynchQuery.insertGenre(idGenre, nomGenre);
            lastIdGenre = idGenre;
        }
        return str;
    }

    /**
     * Vérifie si une donnée particulière est dans la base de donnée. Utilisée en interne avec un count, elle retourne vrai si le résultat du count > 0
     * @param sql Une requête de type "SELECT COUNT(..."
     * @return true si le count > 0, faux sinon
     * @deprecated 
     */
    private boolean isAlreadyInDataBase(String sql)
    {
        int amount = 0;
        try
        {
            amount = db.getCount(sql);
            //db.query(sql);
        } catch (SQLException ex)
        {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        }

        return amount > 0;
    }
    
    public int getLastIdEdition()
    {
        return lastIdEdition;
    }
}
