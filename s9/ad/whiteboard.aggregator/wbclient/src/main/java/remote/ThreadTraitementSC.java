package remote;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Thread de gestion de la section critique
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public class ThreadTraitementSC extends Thread
{

    /*
     * Liste des demandes (ordonnées) d'accès à la SC
     */
    private LinkedList<Integer> demandesAccesSC;

    /*
     * Stub du serveur pour les envois de message
     */
    private IReseau stub;
    
    /**
     * ID du processus maitre
     */
    private int masterId;

    /**
     * Constructeur de la classe 
     * 
     * @param nStub Interface du serveur Reseau
     * @param mId ID du processus maitre
     */
    public ThreadTraitementSC(IReseau nStub, int mId)
    {
        this.stub = nStub;
        this.masterId = mId;
        this.demandesAccesSC = new LinkedList<Integer>();
    }

    /**
     * Ajout d'une demande dans la liste
     * 
     * @param pid ID du processus demandeur
     */
    public synchronized void ajoutDemande(int pid)
    {
        demandesAccesSC.add(pid);

        // TODO trier la file par estampille
        Collections.sort(demandesAccesSC);

        if (demandesAccesSC.size() == 1)
        {
            notifyAll();
        }
    }

    /**
     * Traitement de la liste
     */
    @Override
    public void run()
    {
        // Boucle infinie
        while (true)
        {
            synchronized (this)
            {
                // Blocage du traitement tant que la liste est vide
                while (demandesAccesSC.size() == 0)
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
                
                // Traitement de chaque demande de SC
                Integer proc;
                while ((proc = demandesAccesSC.pollFirst()) != null)
                {
                    try
                    {
                        stub.sendTo(masterId, proc, Message.AUTORISER_ACCES_SC, null);
                    }
                    catch (RemoteException e)
                    {
                        e.printStackTrace();
                    }
                    catch (TimeOutException e)
                    {
                        // TODO Supprimer toutes les éventuelles demandes en SC du processus déconnecté
                        while (demandesAccesSC.remove(proc));
                    }
                }
            }
        }

    }
}
