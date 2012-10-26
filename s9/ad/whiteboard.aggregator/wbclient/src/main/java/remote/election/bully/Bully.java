package remote.election.bully;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import remote.IProcessus;
import remote.IReseau;
import remote.Message;
import remote.TypeMessage;
import remote.election.IElection;
import remote.election.message.ElectionAlgorithm;
import remote.election.message.ElectionMessage;
import remote.election.message.ElectionTypeMessage;

/**
 * Implémentation de l'algorithme d'élection de Bully
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public class Bully extends UnicastRemoteObject implements IElection, IBully
{

    /**
     * Temps d'attente
     */
    private static final long TEMPO = 1000;
    /**
     * Interface reseau
     */
    private IReseau reso;
    /**
     * Interface processus
     */
    private IProcessus processus;
    /**
     * Nombre de voisins
     */
    private int nbVoisins;
    /**
     * ID du processus
     */
    private int id;
    /**
     * ID du processus coordinateur
     */
    private int idCoor;
    /**
     * Etat de l'élection
     */
    private boolean election;
    /**
     * Etat d'acquiescement
     */
    private boolean ok;
    /**
     * Etat nouveau maitre
     */
    private boolean newCoor;
    /**
     * Liste des voisins
     */
    private ArrayList<Integer> voisins;

    /**
     * Constructeur
     *
     * @param reso Interface reseau
     * @param voisins Liste des voisins
     * @param id ID du processus
     * @throws RemoteException
     */
    public Bully(IReseau reso, IProcessus processus, ArrayList<Integer> voisins, int id) throws RemoteException
    {
        super();

        // Couches de communication
        this.reso = reso;           // ~= Send
        this.processus = processus; // ~= Recv

        this.nbVoisins = voisins.size();
        this.id = id;
        this.election = false;
        this.ok = false;
        this.newCoor = false;
        this.voisins = voisins;
        
        // TODO : println
        System.out.println("Algo Bully - Prêt");

    }

    public void demarrerElection()
    {
        // TODO : println
        System.out.println("[ELECTION] Initialisation");
        
        this.election = true;
        this.ok = false;
        this.newCoor = false;

        ElectionMessage em;

        // Propagation de l'election aux voisins 
        for (Integer j : voisins)
        {
            if (this.id-1 < j.intValue())
            {
                try
                {
                    // TODO : println
                    System.out.println("Envoie ELECTION au processus "+j);
                    em = new ElectionMessage(this.id, j.intValue(), null, ElectionAlgorithm.BULLY, ElectionTypeMessage.BULLY_ELECTION);
                    reso.sendTo(this.id, j.intValue(), TypeMessage.ELECTION, null);
                }
                catch (RemoteException ex)
                {
                    ex.printStackTrace();
                }
            }
        }


        //
        // Attente de timeout ou d'acknowledge
        // = while(!ok || TEMPO)
        //
        try
        {
            // TODO : println
            System.out.println("[ELECTION] Attente d'un message ACK");
            this.wait(TEMPO);
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }


        if (ok)
        {
            try
            {
                // TODO : println
                System.out.println("[ELECTION] Attente du nouveau maitre");
                this.wait();
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            // Diffusion du nouveau coordinateur
            this.idCoor = this.id;
            for (Integer j : voisins)
            {
                try
                {
                    // TODO : println
                    System.out.println("[ELECTION] Victoire !");
                    // TODO : println
                    System.out.println("[ELECTION] Diffusion de mon ID aux autres processus");
                    
                    em = new ElectionMessage(this.id, j.intValue(), ElectionAlgorithm.BULLY, ElectionTypeMessage.BULLY_COOR);
                    reso.sendTo(this.id, j.intValue(), TypeMessage.ELECTION, em);
                }
                catch (RemoteException ex)
                {
                    ex.printStackTrace();
                }

            }
        }
        
        // TODO : println
        System.out.println("[ELECTION] Fin de l'élection");
        this.election = false;

    }

    /**
     * Accepte un message lié aux elections.
     *
     * @param m Le message
     */
    public void accepteMessage(ElectionMessage m)
    {
        switch (m.getElectionMsg())
        {
            case BULLY_ELECTION:
                this.accepteElection(m.getIdFrom());
                break;
            case BULLY_ACK:
                this.accepteAck();
                break;
            case BULLY_COOR:
                this.accepteCoor(m.getIdFrom());
                break;
        }
    }

    /**
     * Accepte un message d'élection
     *
     * @param j ID du processus initiateur
     */
    public void accepteElection(int j)
    {
        try
        {
            // TODO : println
            System.out.println("[ELECTION] Message ELECTION reçu du processus "+j);
            reso.sendTo(this.id, j, TypeMessage.ELECTION,
                    new ElectionMessage(this.id, j, ElectionAlgorithm.BULLY, ElectionTypeMessage.BULLY_ACK));
        }
        catch (RemoteException ex)
        {
            ex.printStackTrace();
        }


        if (!election)
        {
            this.demarrerElection();
        }
    }

    /**
     * Accepte un accord d'un processus
     */
    public void accepteAck()
    {
        // TODO : println
        System.out.println("[ELECTION] ACK reçu");
        this.ok = true;
        this.notifyAll();
    }

    /**
     * Accepte le message de nouveau maitre
     *
     * @param j ID du vainqueur de l'élection = nouveau maitre
     */
    public void accepteCoor(int j)
    {
        // TODO : println
        System.out.println("[ELECTION] NEWCOOR reçu, le nouveau maitre est "+j);
        this.idCoor = j;
        this.newCoor = true;
        this.notifyAll();
    }
}
