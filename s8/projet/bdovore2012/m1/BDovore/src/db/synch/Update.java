package db.synch;

import db.DataBase;
import db.SynchQuery;
import db.Tables;
import java.sql.ResultSet;
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

    private DataBase db;

    public Update(DataBase db)
    {
        this.db = db;

    }

    public String edition(DetailsEdition dEdition)
    {
        String str = "";
        if (!isAlreadyInDataBase(SynchQuery.getCount(Tables.ids.get("EDITION"), "EDITION", dEdition.getIdEdition())))
        {
            str = SynchQuery.insertEdition(dEdition);
        }
        return str;
    }

    public String editeur(DetailsEditeur dEditeur)
    {
        String str = "";
        if (!isAlreadyInDataBase(SynchQuery.getCount(Tables.ids.get("EDITEUR"), "EDITEUR", dEditeur.getIdEditeur())))
        {
            str = SynchQuery.insertEditeur(dEditeur);
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
        if (!isAlreadyInDataBase(SynchQuery.getCount(Tables.ids.get("TOME"), "TOME", dVolume.getIdTome())))
        {
             str = SynchQuery.insertVolume(dVolume);
        }
        return str;
    }

    public String auteur(DetailsAuteur dAuteur)
    {
        String str = "";
        if (!isAlreadyInDataBase(SynchQuery.getCount(Tables.ids.get("AUTEUR"), "AUTEUR", dAuteur.getIdAuteur())))
        {
            str = SynchQuery.insertAuteur(dAuteur);
        }

        return str;
    }

    public String serie(DetailsSerie dSerie)
    {
        String str = "";
        if (!isAlreadyInDataBase(SynchQuery.getCount(Tables.ids.get("SERIE"), "SERIE", dSerie.getIdSerie())))
        {
            str = SynchQuery.insertSerie(dSerie);
        }
        return str;
    }

    public String genre(int idGenre, String nomGenre)
    {
        String str = "";

        if (!isAlreadyInDataBase(SynchQuery.getCount(Tables.ids.get("GENRE"), "GENRE", idGenre)))
        {
            str = SynchQuery.insertGenre(idGenre, nomGenre);
        }
        return str;
    }

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
}
