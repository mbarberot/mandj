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

    /**
     * Etat du thread
     */
    private boolean canceled;
    /**
     * Base de données locale
     */
    private DataBase db;
    /**
     * Objet de mise à jour 'intelligent'
     */
    private Update update;
    /**
     * Interface du WebService
     */
    private BDovore_PortType port;
    /**
     * Elements d'interface qui observent le thread. (Pattern Observer) Permet
     * par exemple la mise à jour d'une barre de progression
     */
    private ArrayList<UpdateBaseListener> listener;

    /**
     * Constructeur
     *
     * @param update Objet d'update intelligent
     * @param db Base de donnée à mettre à jour
     * @param port Objet d'interfaçage du webservice
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
     * Constructeur
     *
     * @param update Objet d'update intelligent
     * @param db Base de donnée à mettre à jour
     * @param port Objet d'interfaçage du webservice
     * @param listener Observateur du thread
     * 
     * @deprecated 
     */
    public UpdateBase(DataBase db, BDovore_PortType port, UpdateBaseListener listener)
    {
        this.update = null;
        try
        {
            this.update = new Update(db, port);
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        this.db = db;
        this.port = port;
        this.listener = new ArrayList<UpdateBaseListener>();
        this.listener.add(listener);
    }

    /**
     * Ajoute un nouvel observateur au thread
     *
     * @param listener Nouvel observateur du thread
     */
    public void addListener(UpdateBaseListener listener)
    {
        this.listener.add(listener);
    }

    /**
     * Supprime un observateur du thread
     *
     * @param listener L'observateur à supprimer
     */
    public void removeListener(UpdateBaseListener listener)
    {
        this.listener.remove(listener);
    }

    private void majListener(int value)
    {
        for (UpdateBaseListener l : listener)
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
     *
     * @return true si le thread a été arreté, false sinon.
     */
    public boolean isCanceled()
    {
        return canceled;
    }

    @Override
    public void run()
    {
        long lastID, // Dernier id_edition de la base
                total;      // Nombre d'éditions manquantes


        int len, // Nb éditions à ajouter durant l'itération courante
                cptBoucle, // Nombre de boucle
                tailleLot, // Nb édition reçues maximum par itération
                currentProgress;    // Pourcentage d'avancement de la tâche



        String res, // Chaine au format CVS pour la réception des id_edition manquants 
                sql, // Requête SQL qui sera construite
                editions[];     // Editions manquantes passées du CSV en un tableau

        sql = "";

        // Synchronisation
        try
        {
            // Initialisation
            cptBoucle = 0;
            tailleLot = 1;
            total = 1;
            currentProgress = 0;


            // Boucle de mise à jour
            do
            {
                sql = "";

                if (currentProgress == 100)
                {
                    canceled = true;
                }
                
                if (canceled)
                {
                    break;
                }


                // Récupération de l'ID du dernier tome de la base
                lastID = update.getLastIdEdition();

                // Dans la première itération on prend le nombre total
                // d'éditions manquantes
                if (cptBoucle == 1)
                {
                    total = port.getNbEditionsManquantes(lastID);
                    total = (total > 0)? total : 1 ;
                }

                // Récupération des editiones manquantes
                res = port.getEditionsManquantes(lastID);
                // CSV -> String[]
                editions = res.split(";");

                // Nombre d'ID_EDITION reçus :
                len = editions.length;

                // Initialisation de la taille de chaque lot de données
                // + gestion de la taille = 0 (sinon : boucle infinie)
                if (cptBoucle == 0)
                {
                    tailleLot = len;
                }
                
                if(editions[0].length() > 0)
                {
                    // Récupération des informations depuis le webservice
                    // + création de la requête SQL avec l'objet Update
                    for (int i = 0; i < len; i++)
                    {
                        sql += update.updateEdition(Integer.parseInt(editions[i])) + "\n";
                    }
                }

                // Execution de la requête
                this.db.update(sql);

                // Mise à jour du pourcentage effectué
                currentProgress = (int) (cptBoucle * tailleLot * 100 / total);
                majListener(currentProgress);

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
}
