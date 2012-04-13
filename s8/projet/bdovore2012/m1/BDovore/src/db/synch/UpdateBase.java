package db.synch;

import db.DataBase;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.UpdateBaseListener;
import wsdl.server.*;

/**
 * Thread qui effectue la mise à jour de la base de données
 *
 * @author Mathieu Barberot & Joan Racenet
 */
public class UpdateBase extends Thread
{
    /** Etat du thread */
    private boolean canceled;
    /** Base de données locale */
    private DataBase db;
    /** Objet de mise à jour 'intelligent' */
    private Update update;
    /** Interface du WebService */
    private BDovore_PortType port;
    /** 
     * Elements d'interface qui observent le thread. (Pattern Observer)
     * Permet par exemple la mise à jour d'une barre de progression
     */
    private ArrayList<UpdateBaseListener> listener;
   

     /**
     * Constructeur
     * @param update Objet d'update intelligent
     * @param db Base de donnée à mettre à jour
     * @param port Objet d'interfaçage du webservice
     */
    public UpdateBase(Update update, DataBase db, BDovore_PortType port)
    {
        this.update = update;
        this.db = db;
        this.port = port;
        this.listener = new ArrayList<UpdateBaseListener>();
    }
    
    /**
     * Constructeur
     * @param update Objet d'update intelligent
     * @param db Base de donnée à mettre à jour
     * @param port Objet d'interfaçage du webservice
     * @param listener Observateur du thread
     */
    public UpdateBase(Update update, DataBase db, BDovore_PortType port, UpdateBaseListener listener)
    {
        this.update = update;
        this.db = db;
        this.port = port;
        this.listener = new ArrayList<UpdateBaseListener>();
        this.listener.add(listener);
    }

    /**
     * Ajoute un nouvel observateur au thread
     * @param listener Nouvel observateur du thread
     */
    public void addListener(UpdateBaseListener listener)
    {
        this.listener.add(listener);
    }
    
    /**
     * Supprime un observateur du thread
     * @param listener L'observateur à supprimer
     */
    public void removeListener(UpdateBaseListener listener)
    {
        this.listener.remove(listener);
    }
    
    private void majListener(int value)
    {
        for(UpdateBaseListener l : listener)
        {
            l.progression(value);
        }
    }
            
    
    /**
     * Stoppe le thread.
     */
    public void cancel()
    {
        canceled = true;
    }

    /**
     * Retourne l'état du thread
     * @return true si le thread a été arreté, false sinon.
     */
    public boolean isCanceled()
    {
        return canceled;
    }
 
    @Override
    public void run()
    {
        int lastID,         // Dernier id_edition de la base
                len,        // Nb éditions à ajouter durant l'itération courante
                cptBoucle,  // Nombre de boucle
                total,      // Nombre d'éditions manquantes
                tailleLot;  // Nb édition reçues maximum par itération

        String res,             // Chaine au format CVS pour la réception des id_edition manquants 
                genre,          // Nom du genre
                coloristes[],   // Id des coloristes
                dessinateurs[], // Id des dessinateurs
                scenaristes[],  // Id des scénaristes
                sql,            // Requête SQL qui sera construite
                editions[];     // Editions manquantes passées du CSV en un tableau

        DetailsEdition dEdition;    // Objet edition du webservice
        DetailsVolume dTome;        // Objet tome du webservice
        DetailsSerie dSerie;        // Objet série du webservice
        DetailsAuteur dAuteur;      // Objet auteur du webservice (+ dTj pour la table TJ_TOME_AUTEUR)
        DetailsEditeur dEditeur;    // Objet editeur du webservice

        sql = "";
        
        // Synchronisation
        try
        {
            // Initialisation
            cptBoucle = 0;
            tailleLot = 0;
            total = 0;
            
            

            // Boucle de mise à jour
            do
            {
                sql = "";

                if(canceled) { break; }
                
                // Récupération de l'ID du dernier tome de la base
                lastID = update.getLastIdEdition();
                
                // Dans la première itération on prend le nombre total
                // d'éditions manquantes
                if(cptBoucle == 0)
                {
                    total = port.getNbEditionsManquantes(lastID);
                }
                
                // Récupération des editiones manquantes
                res = port.getEditionsManquantes(lastID);
                // CSV -> String[]
                editions = res.split(";");

                // Nombre d'ID_EDITION reçus :
                len = editions.length;

                // Mise à jour du pourcentage effectué
                majListener(cptBoucle * tailleLot * 100 / total);
                
                // Initialisation de la taille de chaque lot de données
                // + gestion de la taille = 0 (sinon : boucle infinie)
                if (cptBoucle == 0)
                {
                    tailleLot = len;
                } 
                else if (tailleLot == 0)
                {
                    break;
                }

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
                    sql += tj_tome_auteur(dTome.getIdTome(), coloristes, dessinateurs, scenaristes) + "\n";
                    sql += update.editeur(dEditeur) + "\n";
                    sql += update.edition(dEdition) + "\n";
                }

                

                // Execution de la requête
                this.db.update(sql);

                // Incrémentation du compteur de boucle pour éviter 
                // les boucles infinies si aucune données à récupérer
                // len == 0 & tailleLot == 0
                cptBoucle++;

                
            } while (len == tailleLot);

        } catch (RemoteException ex)
        {
            // Affichage temporaire
            System.out.println(sql);
            ex.printStackTrace();
            canceled = true;
            
        } catch (SQLException ex)
        {
            // Affichage temporaire
            System.out.println(sql);
            ex.printStackTrace();
            canceled = true;
        }
    }

    /**
     * Gere les requêtes d'insertion dans la table TJ_TOME_AUTEUR
     * @param idTome ID_TOME
     * @param coloristes Les ID_AUTEUR ayant pour rôle 'Coloriste'
     * @param dessinateurs Les ID_AUTEUR ayant pour rôle 'Dessinateur'
     * @param scenaristes Les ID_AUTEUR ayant pour rôle 'Scenariste'
     * @return La requête SQL
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
                sql += update.tj(idTome, dTj.getIdAuteur(), "Coloristes") + "\n";
            }
        }

        if (dessinateurs.length > 1)
        {
            for (j = 1; j < dessinateurs.length; j++)
            {
                dTj = port.getDetailsAuteur(Integer.parseInt(dessinateurs[j]));
                sql += update.auteur(dTj) + "\n";
                sql += update.tj(idTome, dTj.getIdAuteur(), "Dessinateur") + "\n";
            }
        }

        if (scenaristes.length > 1)
        {
            for (j = 1; j < scenaristes.length; j++)
            {
                dTj = port.getDetailsAuteur(Integer.parseInt(scenaristes[j]));
                sql += update.auteur(dTj) + "\n";
                sql += update.tj(idTome, dTj.getIdAuteur(), "Scenariste") + "\n";
            }
        }
        return sql;
    }
}
