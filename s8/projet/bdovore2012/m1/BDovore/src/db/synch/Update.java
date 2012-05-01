package db.synch;

import db.DataBase;
import db.SynchQuery;
import db.data.TJ;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.TreeSet;
import wsdl.server.*;

/**
 * Fonctions d'update 'intelligentes'.<br/> Cette classe implémente la
 * construction de requêtes SQL permettant l'insertion de données dans la base
 * de données en respectant les contraintes d'intégrités référentielles.<br/>
 * Les fonctions n'ajoutent pas d'éléments dajà présents dans la base et
 * ajoutent les informations manquante pour les clés étrangères.<br/> Enfin, les
 * informations manquantes sont téléchargée depuis le Webservice.<br/>
 *
 * @author Barberot Mathieu & Racenet Joan
 */
public class Update
{

    /**
     * La base de donnée à utiliser
     */
    private DataBase db;
    /**
     * Interface du webservice
     */
    private BDovore_PortType webservice;
    /**
     * Tous les id_edition de la bibliotheque
     */
    private TreeSet<Integer> allIdUser;
    /**
     * Tous id_genre de la base locale
     */
    private TreeSet<Integer> allIdGenre;
    /**
     * Tous id_serie de la base locale
     */
    private TreeSet<Integer> allIdSerie;
    /**
     * Tous id_auteur de la base locale
     */
    private TreeSet<Integer> allIdAuteur;
    /**
     * Tous id_editeur de la base locale
     */
    private TreeSet<Integer> allIdEditeur;
    /**
     * Tous id_edition de la base locale
     */
    private TreeSet<Integer> allIdEdition;
    /**
     * Tous id_tome de la base locale
     */
    private TreeSet<Integer> allIdTome;
    /**
     * Tous les couples de TJ_TOME_AUTEUR
     */
    private TreeSet<TJ> allTJ;
    /**
     * Tous les id_auteur de la table DETAILS_AUTEUR
     */
    private TreeSet<Integer> allIdDetailsAuteur;
    /**
     * Tous les id_edition de la table DETAILS_EDITION
     */
    private TreeSet<Integer> allIdDetailsEdition;
    /**
     * Tous les id_serie de la table DETAILS_SERIE
     */
    private TreeSet<Integer> allIdDetailsSerie;
    /**
     * Définit s'il faut, oui ou non, ajouter les informations relatives aux
     * détails (Tables DETAILS_AUTEUR, DETAILS_EDITION, DETAILS_EDITEUR)
     */
    private boolean withDetails;

    /**
     * Constructeur simple, pas d'ajout de données dans les tables DETAILS_FOO
     *
     * @param db Base de données locale
     * @param webservice Interface du webservice
     * @throws SQLException
     */
    public Update(DataBase db, BDovore_PortType webservice) throws SQLException
    {
        this(db, webservice, false);
    }

    /**
     * Constructeur Complet
     *
     * @param db Base de données locale
     * @param webservice Interface du webservice
     * @param withDetails Flag : true = ajout de données dans les tables
     * DETAILS_FOO
     * @throws SQLException
     */
    public Update(DataBase db, BDovore_PortType webservice, boolean withDetails) throws SQLException
    {
        this.db = db;
        this.webservice = webservice;
        this.withDetails = withDetails;
        init();
    }

    /**
     * Initialise les variable allFoo
     *
     * @throws SQLException
     */
    private void init() throws SQLException
    {
        this.allIdUser = db.getAllID("BD_USER");
        this.allIdAuteur = db.getAllID("AUTEUR");
        this.allIdSerie = db.getAllID("SERIE");
        this.allIdGenre = db.getAllID("GENRE");
        this.allIdEditeur = db.getAllID("EDITEUR");
        this.allIdEdition = db.getAllID("EDITION");
        this.allIdTome = db.getAllID("TOME");
        this.allTJ = db.getAllTJ();
        this.allIdDetailsAuteur = db.getAllID("DETAILS_AUTEUR");
        this.allIdDetailsEdition = db.getAllID("DETAILS_EDITION");
        this.allIdDetailsSerie = db.getAllID("DETAILS_SERIE");
    }

    public String updateBDUser(int idEdition, boolean ajouter, String nom, String pwd) throws RemoteException
    {
        String sql = "";
        DetailsEdition dEdition;
        Integer newId = new Integer(idEdition);

        if (allIdUser.contains(newId))
        {
            if (!ajouter)
            {
                sql += SynchQuery.delBDUser(idEdition);
            } 
            else
            {
                dEdition = webservice.getDetailsEditionUser(idEdition, nom, pwd);
                sql += SynchQuery.setBDUser(dEdition);
                sql += updateEdition(idEdition);
            }
        } else if (ajouter)
        {
            dEdition = webservice.getDetailsEditionUser(idEdition, nom, pwd);
            sql += SynchQuery.addBDUser(dEdition);
            sql += updateEdition(idEdition);
        }

        return sql;
    }

    /**
     * Retourne la requête d'insertion de l'édition.
     *
     * @param idEdition ID_EDITION
     * @return La requête sql
     */
    public String updateEdition(int idEdition) throws RemoteException
    {
        String sql = "";
        DetailsEdition dEdition;
        Integer newId = new Integer(idEdition);
        if (!allIdEdition.contains(newId))
        {
            dEdition = webservice.getDetailsEdition(idEdition);
            sql += updateEditeur(dEdition.getIdEditeur());
            sql += updateTome(dEdition.getIdTome());
            sql += SynchQuery.insertEdition(dEdition);
            sql += (withDetails) ? updateDetailsEdition(dEdition) : "";
            allIdEdition.add(newId);
        }
        else if(withDetails)
        {
            sql += updateDetailsEdition(idEdition);
        }

        return sql;
    }
    
    public String updateDetailsEdition(int idEdition) throws RemoteException
    {
        String sql = "";
        DetailsEdition dEdition;
        Integer newId = new Integer(idEdition);
        if(!allIdDetailsEdition.contains(newId))
        {
            dEdition = webservice.getDetailsEdition(idEdition);
            sql += SynchQuery.insertDetailsEdition(dEdition);
            allIdDetailsEdition.add(newId);
        }
        return sql;
    }
    
    public String updateDetailsEdition(DetailsEdition dEdition) throws RemoteException
    {
        String sql = "";
        Integer newId = new Integer(dEdition.getIdEdition());
        if(!allIdDetailsEdition.contains(newId))
        {
            sql += SynchQuery.insertDetailsEdition(dEdition);
            allIdDetailsEdition.add(newId);
        }
        return sql;
    }
    

    /**
     * Retourne la requête d'insertion de l'éditeur
     *
     * @param idEditeur ID_EDITEUR
     * @return La requête SQL
     */
    public String updateEditeur(int idEditeur) throws RemoteException
    {
        String sql = "";
        DetailsEditeur dEditeur;
        Integer newId = new Integer(idEditeur);
        if (!allIdEditeur.contains(newId))
        {
            dEditeur = webservice.getDetailsEditeur(idEditeur);
            sql += SynchQuery.insertEditeur(dEditeur);
            allIdEditeur.add(newId);
        }
        return sql;
    }

    /**
     * Retourne la requête d'insertion dans la table TJ_TOME_AUTEUR
     *
     * @param idTome ID_TOME
     * @param idAuteur ID_AUTEUR
     * @param role ROLE
     * @return La requête SQL
     */
    public String updateTj(int idTome, int idAuteur, String role) throws RemoteException
    {
        String sql = "";
        TJ newTJ = new TJ(idTome, idAuteur, role);
        if (!allTJ.contains(newTJ))
        {
            sql += updateAuteur(idAuteur);
            sql += SynchQuery.insertTjTomeAuteur(idTome, idAuteur, role);
            allTJ.add(newTJ);
        }
        return sql;
    }

    /**
     * Retourne la requête d'insertion du tome
     *
     * @param dVolume Le tome
     * @return La requête SQL
     */
    public String updateTome(int idTome) throws RemoteException
    {
        String sql = "";
        DetailsVolume dTome;
        Integer newId = new Integer(idTome);
        if (!allIdTome.contains(newId))
        {
            dTome = webservice.getDetailsTome(idTome);
            sql += updateGenre(dTome.getIdGenre());
            sql += updateSerie(dTome.getIdSerie());
            sql += SynchQuery.insertVolume(dTome);
            sql += process_tj(idTome);
            allIdTome.add(newId);
        }
        return sql;
    }

    /**
     * Retourne la requête d'insertion de l'auteur
     *
     * @param dAuteur L'auteur
     * @return La requête SQL
     */
    public String updateAuteur(int idAuteur) throws RemoteException
    {
        String sql = "";
        DetailsAuteur dAuteur;
        Integer newId = new Integer(idAuteur);
        if (!allIdAuteur.contains(newId))
        {
            dAuteur = webservice.getDetailsAuteur(idAuteur);
            sql += SynchQuery.insertAuteur(dAuteur);
            sql += (withDetails) ? updateDetailsAuteur(dAuteur) : ""; 
            allIdAuteur.add(newId);
        }
        else if(withDetails)
        {
            sql += updateDetailsAuteur(idAuteur);
        }


        return sql;
    }
    
    public String updateDetailsAuteur(int idAuteur) throws RemoteException
    {
        String sql = "";
        DetailsAuteur dAuteur;
        Integer newId = new Integer(idAuteur);
        if(!allIdDetailsAuteur.contains(newId))
        {
            dAuteur = webservice.getDetailsAuteur(idAuteur);
            sql += SynchQuery.insertDetailsAuteur(dAuteur);
            allIdDetailsAuteur.add(newId);
        }
        return sql;
    }
    
    public String updateDetailsAuteur(DetailsAuteur dAuteur) throws RemoteException
    {
        String sql = "";
        Integer newId = new Integer(dAuteur.getIdAuteur());
        if(!allIdDetailsAuteur.contains(newId))
        {
            sql += SynchQuery.insertDetailsAuteur(dAuteur);
            allIdDetailsAuteur.add(newId);
        }
        return sql;
    }

    /**
     * Retourne la requête d'insertion de la série
     *
     * @param dSerie La série
     * @return La requête SQL
     */
    public String updateSerie(int idSerie) throws RemoteException
    {
        String sql = "";
        DetailsSerie dSerie;
        Integer newId = new Integer(idSerie);
        if (!allIdSerie.contains(newId))
        {
            dSerie = webservice.getDetailsSerie(idSerie);
            sql += SynchQuery.insertSerie(dSerie);
            sql += (withDetails) ?  updateDetailsSerie(dSerie) : "";
            allIdSerie.add(newId);
        } else if (withDetails)
        {
            sql += updateDetailsSerie(idSerie);
        }

        return sql;
    }
    
    public String updateDetailsSerie(int idSerie) throws RemoteException
    {
        String sql = "";
        DetailsSerie dSerie;
        Integer newId = new Integer(idSerie);
        if(!allIdDetailsSerie.contains(newId))
        {
            dSerie = webservice.getDetailsSerie(idSerie);
            sql += SynchQuery.insertDetailsSerie(dSerie);
            allIdDetailsSerie.add(newId);
        }
        return sql;
    }

    public String updateDetailsSerie(DetailsSerie dSerie) throws RemoteException
    {
        String sql = "";
        Integer newId = new Integer(dSerie.getIdSerie());
        if(!allIdDetailsSerie.contains(newId))
        {
            sql += SynchQuery.insertDetailsSerie(dSerie);
            allIdDetailsSerie.add(newId);
        }
        return sql;
    }
    
    /**
     * Retourne la requête d'insertion du genre
     *
     * @param idGenre ID_GENRE
     * @param nomGenre TITRE
     * @return La requête SQL
     */
    public String updateGenre(int idGenre) throws RemoteException
    {
        String str = "";
        String nomGenre;
        Integer newId = new Integer(idGenre);

        if (!allIdGenre.contains(newId))
        {
            nomGenre = webservice.getGenre(idGenre);
            str = SynchQuery.insertGenre(idGenre, nomGenre);
            allIdGenre.add(newId);
        }
        return str;
    }

    public int getLastIdEdition() throws SQLException
    {
        return db.getLastID("EDITION");
    }

    private String process_tj(int idTome) throws RemoteException
    {
        int j, id;
        String sql = "";
        String[] coloristes, dessinateurs, scenaristes;

        coloristes = webservice.getColoristesTome(idTome).split(";");
        dessinateurs = webservice.getDessinateursTome(idTome).split(";");
        scenaristes = webservice.getScenaristesTome(idTome).split(";");

        if (coloristes.length > 0)
        {
            for (j = 1; j < coloristes.length; j++)
            {
                id = Integer.parseInt(coloristes[j]);
                sql += updateAuteur(id) + "\n";
                sql += updateTj(idTome, id, "Coloriste") + "\n";
            }
        }

        if (dessinateurs.length > 0)
        {
            for (j = 1; j < dessinateurs.length; j++)
            {
                id = Integer.parseInt(dessinateurs[j]);
                sql += updateAuteur(id) + "\n";
                sql += updateTj(idTome, id, "Dessinateur") + "\n";
            }
        }

        if (scenaristes.length > 0)
        {
            for (j = 1; j < scenaristes.length; j++)
            {
                id = Integer.parseInt(scenaristes[j]);
                sql += updateAuteur(id) + "\n";
                sql += updateTj(idTome, id, "Scenariste") + "\n";
            }
        }
        return sql;
    }

    public void setWithDetails(boolean withDetails)
    {
        this.withDetails = withDetails;
    }
}
