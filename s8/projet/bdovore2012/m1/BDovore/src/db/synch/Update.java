package db.synch;

import db.DataBase;
import db.SynchQuery;
import db.data.TJ;
import java.sql.SQLException;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import wsdl.server.*;

/**
 * Fonctions d'update 'intelligentes'
 *
 * @author Barberot Mathieu & Racenet Joan
 */
public class Update
{
    /** La base de donnée à utiliser */
    private DataBase db;
    
    /** Tous id_genre de la base locale */
    private TreeSet<Integer> allIdGenre;
    /** Tous id_serie de la base locale */
    private TreeSet<Integer> allIdSerie;
    /** Tous id_auteur de la base locale */
    private TreeSet<Integer> allIdAuteur;
    /** Tous id_editeur de la base locale */
    private TreeSet<Integer> allIdEditeur;
    /** Tous id_edition de la base locale */
    private TreeSet<Integer> allIdEdition;
    /** Tous id_tome de la base locale */
    private TreeSet<Integer> allIdTome;
    /** Tous les couples de TJ_TOME_AUTEUR */
    private TreeSet<TJ> allTJ;

    
    /**
     * Constructeur
     * 
     * @param db - Base de données locale
     * @throws SQLException 
     */
    public Update(DataBase db) throws SQLException
    {
        this.db = db;
        init();
    }
    
    /**
     * Initialise les variable allFoo
     * 
     * @throws SQLException 
     */
    private void init() throws SQLException
    {
        this.allIdAuteur = db.getAllID("AUTEUR");
        this.allIdSerie = db.getAllID("SERIE");
        this.allIdGenre = db.getAllID("GENRE");
        this.allIdEditeur = db.getAllID("EDITEUR");
        this.allIdEdition = db.getAllID("EDITION"); 
        this.allIdTome = db.getAllID("TOME");
        this.allTJ = db.getAllTJ();
    }

    /**
     * Retourne la requête d'insertion de l'édition
     * 
     * @param dEdition L'édition
     * @return La requête sql
     */
    public String edition(DetailsEdition dEdition)
    {
        String str = "";
        Integer newId = new Integer(dEdition.getIdEdition());
        if(!allIdEdition.contains(newId))
        {
            str = SynchQuery.insertEdition(dEdition);
            allIdEdition.add(newId);
        }
        return str;
    }

    /**
     * Retourne la requête d'insertion de l'éditeur
     * 
     * @param dEditeur L'édition
     * @return La requête SQL
     */
    public String editeur(DetailsEditeur dEditeur)
    {
        String str = "";
        Integer newId = new Integer(dEditeur.getIdEditeur());
        if (!allIdEditeur.contains(newId))
        {
            str = SynchQuery.insertEditeur(dEditeur);
            allIdEditeur.add(newId);
        }
        return str;
    }

    /**
     * Retourne la requête d'insertion dans la table TJ_TOME_AUTEUR
     * 
     * @param idTome    ID_TOME
     * @param idAuteur  ID_AUTEUR
     * @param role      ROLE
     * @return          La requête SQL
     */
    public String tj(int idTome, int idAuteur, String role)
    {
        String str = "";
        TJ newTJ = new TJ(idTome, idAuteur, role);
        if (!allTJ.contains(newTJ))
        {
            str = SynchQuery.insertTjTomeAuteur(idTome, idAuteur, role);
            allTJ.add(newTJ);
        }       
        return str;
    }

    /**
     * Retourne la requête d'insertion du tome
     * 
     * @param dVolume   Le tome
     * @return          La requête SQL
     */
    public String volume(DetailsVolume dVolume)
    {
        String str = "";
        Integer newId = new Integer(dVolume.getIdTome());
        if (!allIdTome.contains(newId))
        {
             str = SynchQuery.insertVolume(dVolume);
             allIdTome.add(newId);
       }
        return str;
    }

    /**
     * Retourne la requête d'insertion de l'auteur
     * 
     * @param dAuteur   L'auteur
     * @return          La requête SQL
     */
    public String auteur(DetailsAuteur dAuteur)
    {
        String str = "";
        Integer newId = new Integer(dAuteur.getIdAuteur());
        if (!allIdAuteur.contains(newId))
        {
            str = SynchQuery.insertAuteur(dAuteur);
            allIdAuteur.add(newId);
        }

        return str;
    }

    /**
     * Retourne la requête d'insertion de la série
     * 
     * @param dSerie    La série
     * @return          La requête SQL
     */
    public String serie(DetailsSerie dSerie)
    {
        String str = "";
        Integer newId = new Integer(dSerie.getIdSerie());
        if (!allIdSerie.contains(newId))
        {
            str = SynchQuery.insertSerie(dSerie);
            allIdSerie.add(newId);
        }

        return str;
    }

    /**
     * Retourne la requête d'insertion du genre
     * 
     * @param idGenre   ID_GENRE
     * @param nomGenre  TITRE
     * @return La requête SQL
     */
    public String genre(int idGenre, String nomGenre)
    {
        String str = "";
        Integer newId = new Integer(idGenre);

        if (!allIdGenre.contains(newId))
        {
            str = SynchQuery.insertGenre(idGenre, nomGenre);
            allIdGenre.add(newId);
        }
        return str;
    }

    /**
     * Vérifie si une donnée particulière est dans la base de donnée. Utilisée en interne avec un count, elle retourne vrai si le résultat du count > 0
     * @param sql Une requête de type "SELECT COUNT(..."
     * @return true si le count > 0, faux sinon
     */
    private boolean isAlreadyInDataBase(String sql)
    {
        int amount = 0;
        try
        {
            amount = db.getCount(sql);
        } catch (SQLException ex)
        {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        }

        return amount > 0;
    }
    
    public int getLastIdEdition() throws SQLException
    {
        return db.getLastID("EDITION");
    }
}
