package db.synch;

import db.DataBase;
import db.data.Edition;
import db.data.User;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.UpdateBDUserListener;
import wsdl.server.BDovore_PortType;
import wsdl.server.DetailsEdition;

/**
 * Thread de mise à jour de la BDtheque de l'utilisateur.
 *
 * @author Barberot Mathieu & Racenet Joan
 */
public class UpdateUser extends Thread
{

    private LinkedList<Integer> allIdLocal;
    private LinkedList<Integer> allIdServer;
    private DataBase db;
    private User user;
    private Update update;
    private BDovore_PortType webservice;
    private boolean canceled;
    private ArrayList<UpdateBDUserListener> listeners;

    public UpdateUser(DataBase db, User user, BDovore_PortType webservice)
    {
        this.allIdLocal = null;
        this.allIdServer = null;

        this.db = db;
        this.user = user;
        this.update = null;
        try
        {
            this.update = new Update(db, webservice, true);
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        this.webservice = webservice;

        this.canceled = false;
        this.listeners = new ArrayList<UpdateBDUserListener>();

    }

    public UpdateUser(DataBase db, User user, BDovore_PortType webservice, UpdateBDUserListener listener)
    {
        this(db, user, webservice);
        this.listeners.add(listener);

    }

    @Override
    public void run()
    {
        Integer id;
        
        int tmpi = 0;

        try
        {
            if (canceled)
            {
                return;
            }

            // Récupérations des listes d'éditions possédées
            allIdLocal = db.getBDtheque();
            allIdServer = convert(webservice.getBibliotheque(user.getUsername(), user.getPassword()));

            // Boucle sur les éditions locales 
            while (allIdLocal.size() > 0 && !canceled)
            {
                // équivalent à un "push()"
                id = allIdLocal.poll();

                // Deux cas :
                //  L'id est dans la base distante (éditions communes)
                //  L'id est seulement locale
                if (allIdServer.contains(id))
                {
                    //
                    // ID dans les deux bases( locale ET distante)
                    //

                    // On applique les règles du tableau
                    transaction(id, true, true);
                    // On ôte l'édition des editions distantes
                    allIdServer.remove(id);
                } else
                {
                    //
                    // ID dans la base locale seulement
                    //

                    // On applique les règles du tableau
                    transaction(id, true, false);
                }

                        
            }


            // Boucle sur les ID de la base distante.
            // Comme on a ôté toutes les éditions en commun, il ne reste que 
            // celles qui sont uniquement dans la base distante
            while (allIdServer.size() > 0 && !canceled)
            {
                // push()
                id = allIdServer.poll();
                // On applique les règles du tableau
                transaction(id, false, true);

            }

        } catch (SQLException ex)
        {
            Logger.getLogger(UpdateUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex)
        {
            Logger.getLogger(UpdateUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Consulte la table transaction et prend une décision pour la
     * synchronisation de l'édition
     *
     * @param id ID_EDITION de l'édition à traiter
     * @param inLocal true si l'édition est présente dans la base locale, false
     * sinon
     * @param inServer true si l'édition est présente dans la base distante,
     * false sinon
     * @throws SQLException
     * @throws RemoteException
     */
    private void transaction(Integer id, boolean inLocal, boolean inServer) throws SQLException, RemoteException
    {
        int idEd = id.intValue();
        int typeTransaction = db.getTypeTransaction(idEd);

        if (inLocal)
        {
            if (inServer)
            {
                // Récupération des flags
                DetailsEdition edLoc = db.getBDUser(id);
                DetailsEdition edDist = webservice.getDetailsEditionUser(id.intValue(), user.getUsername(), user.getPassword());

                if (sameFlags(edLoc, edDist))
                {
                    switch (typeTransaction)
                    {
                        case Edition.INSERT:
                            addRow(fillRow(idEd, inLocal, inServer));
                            break; // Demande utilisateur
                        case Edition.UPDATE:
                            addRow(fillRow(idEd, inLocal, inServer));
                            break; // Demande utilisateur
                        case Edition.DELETE:
                            break;
                        case Edition.DO_NOTHING:
                            break;
                    }
                } else
                {
                    addRow(fillRow(idEd, inLocal, inServer)); // Demande utilisateur
                }
            } else
            {
                switch (typeTransaction)
                {
                    case Edition.INSERT:
                        webservice.addUserBibliotheque(user.getUsername(), user.getPassword(), idEd);
                        break; // Ajouter site
                    case Edition.UPDATE:
                        addRow(fillRow(idEd, inLocal, inServer));
                        break; // Demande utilisateur
                    case Edition.DELETE:
                        break;
                    case Edition.DO_NOTHING:
                        db.update(update.updateBDUser(idEd,false,user.getUsername(), user.getPassword()));
                        break; // Supprimer local
                }
            }
        } else
        {
            switch (typeTransaction)
            {
                case Edition.INSERT:
                    break;
                case Edition.UPDATE:
                    break;
                case Edition.DELETE:
                    webservice.delUserBibliotheque(user.getUsername(),user.getPassword(),idEd); 
                    break; // Supprimer site
                case Edition.DO_NOTHING:
                    db.update(update.updateBDUser(idEd,true,user.getUsername(), user.getPassword()));
                    break; // Ajouter local
            }
        }
        
    }

    /**
     * Crée une ligne pour l'affichage des conflits dans l'interface.
     * 
     * @param id ID_EDITION
     * @param inLocal true si l'édition est présente dans la bdtheque locale, false sinon
     * @param inServer true si l'édition est présente dans la bdtheque distante, false sinon
     * @return Une ligne du tableau de conflits
     * @throws SQLException 
     */
    private Object[] fillRow(int id, boolean inLocal, boolean inServer) throws SQLException
    {
        // Récupérer le titre, la série et le numéro du tome
        Object[] o = db.getSynchInfo(id);

        return new Object[]
                {
                    new Boolean(false),
                    new Boolean(false),
                    new Boolean(true),
                    new String((String) o[0]),
                    new String((String) o[1]),
                    new String((String) o[2]),
                    new String((inLocal ? "Présent" : "Absent")),
                    new String((inServer ? "Présent" : "Absent"))
                };
    }

    /**
     * Comparer les flags des deux éditions
     *
     * @param edLoc Edition de la base locale
     * @param edDist Edition de la base distante
     * @return true si les deux éditions ont les mêmes flags, false sinon.
     */
    private boolean sameFlags(DetailsEdition edLoc, DetailsEdition edDist)
    {
        return edLoc.getFlag_pret() == edDist.getFlag_pret()
                && edLoc.getFlag_dedicace() == edDist.getFlag_dedicace()
                && edLoc.getFlag_aAcheter() == edDist.getFlag_aAcheter();

    }

    /**
     * Convertit des données provenant du Webservice (au format CSV) en une file
     * d'Integer.
     *
     * @param fromWS Liste d'entier au format CSV
     * @return Arbre trié contenant les entiers de la chaine
     */
    private LinkedList<Integer> convert(String fromWS)
    {
        LinkedList<Integer> liste = new LinkedList<Integer>();
        String tabstr[] = fromWS.split(";");
        int len = tabstr.length;

        for (int i = 0; i < len; i++)
        {
            liste.offer(new Integer(tabstr[i]));
        }

        return liste;
    }

    /**
     * Stoppe le thread.
     *
     * Remplace les fonctions d'arrêt des thread dépréciées comme stop()
     */
    public void cancel()
    {
        this.canceled = true;
    }

    /**
     * Ajoute un observateur
     * 
     * @param listener Nouvel observateur
     */
    public void addListener(UpdateBDUserListener listener)
    {
        this.listeners.add(listener);
    }

    /**
     * Supprime un observateur
     * 
     * @param listener Observateur à supprimer
     */
    public void removeListener(UpdateBDUserListener listener)
    {
        this.listeners.remove(listener);
    }

    /**
     * Ajoute une ligne à tous les observateurs
     * 
     * @param row Nouvelle ligne
     */
    public void addRow(Object[] row)
    {
        for (UpdateBDUserListener listener : listeners)
        {
            listener.addRow(row);
        }
    }
}
