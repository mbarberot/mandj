package remote;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadTraitementMessages extends Thread
{

    /*
     * Liste des demandes (ordonnées) d'accès à la SC
     */
    private LinkedList<Message> listeMessages;

    /*
     * Stub du serveur pour les envois de message
     */
    private HashMap<Integer, IProcessus> listeProc;

    /*
     * ID du processus maitre
     */
    private int masterId;

    /*
     * Initialisation
     */
    public ThreadTraitementMessages()
    {
        this.listeProc = new HashMap<Integer, IProcessus>();
        this.listeMessages = new LinkedList<Message>();
    }

    /*
     * Ajout d'un nouveau message
     */
    public synchronized void ajoutNouveauMessage(Message m)
    {

        listeMessages.add(m);

        // TODO println
        System.out.println(m.toString() + " ajouté dans la file d'attente ");
        if (listeMessages.size() >= 1)
        {
            this.notifyAll();
        }
    }

    /*
     * Ajout d'un nouveau client et de son stub
     */
    public void ajoutNouveauClient(int pId, IProcessus proc)
    {
        if (!listeProc.containsKey(pId))
        {
            listeProc.put(new Integer(pId), proc);
        }
    }

    /*
     * Déconnexion d'un client
     */
    public void suppressionClient(int pId)
    {
        listeProc.remove(new Integer(pId));
    }

    /*
     * Récupère la liste des clients
     */
    public ArrayList<Integer> getListeClients()
    {
        ArrayList<Integer> lClient = new ArrayList<Integer>();

        Iterator<Integer> iter = this.listeProc.keySet().iterator();

        while (iter.hasNext())
        {
            lClient.add(iter.next());
        }

        return lClient;
    }

    /*
     * Renvoie l'id du client maître
     */
    public int getMaster()
    {
        for (int proc : this.listeProc.keySet())
        {
            try
            {
                if (this.listeProc.get(proc).isMaster())
                {
                    return proc;
                }
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
        return -1;
    }

    /*
     * Renvoie le tableau blanc à jour
     */
    public ArrayList<String> getWB(int idFrom) throws RemoteException
    {
        //
        // TODO
        // - récupérer la version du maître ?
        // - maj des commentaires (voisin = forme) ?
        // - pourquoi est-ce dans le serveur Réseau ?
        // le processus devrait effectuer cette démarche seul.
        //
        ArrayList<String> res = new ArrayList<String>();
        ArrayList<String> tmp = null;
        for (Integer i : this.listeProc.keySet())
        {
            if (i != idFrom)
            {
                tmp = this.listeProc.get(i).getListeForme();
                if (tmp.size() > res.size())
                {
                    res = tmp;
                }
            }
        }

        return res;
    }

    /*
     * Boucle de traitement principal
     */
    public void run()
    {
        while (true)
        {
            synchronized (this)
            {
                while (this.listeMessages.size() == 0)
                {
                    try
                    {
                        this.wait();
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            Message m;
            while ((m = this.listeMessages.poll()) != null)
            {
                try
                {
                    achemineMessage(m);
                }
                catch (TimeOutException e)
                {
                    Iterator mess = this.listeMessages.iterator();

                    while (mess.hasNext())
                    {
                        Message tmp = (Message) mess.next();
                        // Supprimer les messages destinés au client déconnecté
                        if (tmp.getIdTo() == e.getProc())
                        {
                            // Si des demandes de SC étaient dans la file, refuser la SC
                            if (tmp.getType() == TypeMessage.DEMANDE_SC)
                            {
                                ajoutNouveauMessage(new Message(tmp.getIdTo(), TypeMessage.REFUSER_ACCES_SC, tmp.getIdFrom(), null));
                            }

                            mess.remove();
                        }



                    }

                    e.printStackTrace();
                }
            }
        }
    }

    /*
     * Acheminement du message vers son destinataire après temps d'attente
     */
    public void achemineMessage(Message m) throws TimeOutException
    {

        IProcessus dest = null;

        //TODO println
        System.out.println(m.toString() + " en traitement ");
        /*
         * Attente avant traitement
         */
        int desync_time = (int) (Math.random() * 5000 + 3000);
        try
        {
            Thread.sleep(desync_time);
        }
        catch (InterruptedException e2)
        {
            e2.printStackTrace();
        }

        if (m.getType() != TypeMessage.CONNEXION_NOUVEAU_PROC)
        {
            dest = this.listeProc.get(m.getIdTo());

            // Gestion du timeout
            if (dest == null)
            {

                // Appeler méthode remote signalerTimeout
                try
                {
                    if (m.getIdFrom() != m.getIdTo())
                    {
                        System.out.println(m.getIdTo() + " signale timeout à " + m.getIdFrom());
                        this.listeProc.get(new Integer(m.getIdFrom())).signalerTimeout(m.getIdTo());
                    }
                }
                catch (RemoteException e1)
                {
                    e1.printStackTrace();
                }
                throw new TimeOutException("Le client " + m.getIdTo()
                        + " n'est plus connecté");

            }
        }

        switch (m.getType())
        {
            case CONNEXION_NOUVEAU_PROC:
                try
                {
                    // broadcast
                    for (Integer dests : this.listeProc.keySet())
                    {
                        this.listeProc.get(dests).signaleNouveauVoisin(
                                m.getIdFrom());

                    }
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
                break;
                
            case DEMANDE_SC:
                try
                {
                    dest.demanderSectionCritique(m.getIdFrom());
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
                break;
                
            case AUTORISER_ACCES_SC:
                try
                {
                    dest.autoriserSectionCritique(1);
                }
                catch (RemoteException e1)
                {
                    e1.printStackTrace();
                }
                break;
                
            case REFUSER_ACCES_SC:
                try
                {
                    dest.autoriserSectionCritique(2);
                }
                catch (RemoteException e1)
                {
                    e1.printStackTrace();
                }
                break;
                
            case ENVOI_NOUVELLE_FORME:
                try
                {
                    dest.receptionNouvelleForme((String) m.getData());
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
                break;
                
            case ELECTION:
                try
                {
                    dest.accepteMessageElection(m);
                }
                catch (RemoteException ex)
                {
                    ex.printStackTrace();
                }
                break;


            default:
                throw new UnsupportedOperationException("Not supported yet.");
        }

        System.out.println(m.toString() + " traité.");

    }
}
