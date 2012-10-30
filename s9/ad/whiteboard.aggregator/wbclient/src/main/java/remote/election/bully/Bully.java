package remote.election.bully;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import remote.IProcessus;
import remote.IReseau;
import remote.Processus;
import remote.election.IElection;
import remote.messages.ElectionAlgorithm;
import remote.messages.ElectionMessage;
import remote.messages.ElectionTypeMessage;
import remote.messages.Message;

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
    private static final long TEMPO = 10000;
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
    private ArrayList<Integer> voisins;
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
     * Processus contenant
     */
    private Processus parent;
    /**
     * Thread d'attente pour bully
     */
    private ThreadBullyWait bullyWait;

    /**
     * Constructeur
     *
     * @param reso Interface reseau
     * @param voisins Liste des voisins
     * @param id ID du processus
     * @throws RemoteException
     */
    public Bully(IReseau reso, IProcessus processus, Processus parent, int id) throws RemoteException
    {
        super();

        // Couches de communication
        this.reso = reso;           // ~= Send
        this.processus = processus; // ~= Recv
        this.parent = parent;
        this.id = id;
        this.election = false;
        this.ok = false;
        this.newCoor = false;
        this.bullyWait = null;
        
        
        // TODO : println
        System.out.println("Algo Bully - Prêt");

    }

    /**
     * Début de l'élection :
     * - envoi des messages de types ELECTION aux processus j tels que : j > i
     */
    public void demarrerElection()
    {
        // TODO : println
        System.out.println("[ELECTION] Initialisation");

        this.election = true;
        this.ok = false;
        this.newCoor = false;

        this.voisins = this.parent.getVoisins();
        // Propagation de l'election aux voisins 
        for (Integer j : voisins)
        {
            if (this.id < j.intValue())
            {
                try
                {
                    // TODO : println
                    System.out.println("[ELECTION] Envoie ELECTION au processus " + j);
                    reso.sendTo(new ElectionMessage(this.id, j.intValue(), null, ElectionAlgorithm.BULLY, ElectionTypeMessage.BULLY_ELECTION));
                }
                catch (RemoteException ex)
                {
                    ex.printStackTrace();
                }
            }
        }

        // Lancement d'un thread pour l'attente d'un message ACK
        bullyWait = new ThreadBullyWait(this);
        bullyWait.waitForACK(TEMPO*voisins.size()*3);
        
    }
    
    /**
     * Fin de l'attente du message ACK
     * - soit un message ACK a été reçu et on attend un signe du nouveau maitre
     * - soit le temps limite imparti est écoulé et on s'autoproclame maitre
     */
    public void doneWaitingForACK()
    {
        if (ok)
        {
            bullyWait = new ThreadBullyWait(this);
            bullyWait.waitForCOOR();
        }
        else
        {
            this.voisins = parent.getVoisins();
            
            // Diffusion du nouveau coordinateur
            // TODO : println
            System.out.println("[ELECTION] Victoire !");
            this.idCoor = this.id;
            this.parent.setMaster(idCoor);
            
            //TODO démarrer le thread de SC
            if(idCoor == this.parent.getId())
            {
            	this.parent.startThreadSC();
            }
            
            for (Integer j : voisins)
            {
                if(j.intValue() != this.id)
                {
                    try
                    {
                        // TODO : println
                        System.out.println("[ELECTION] Envoi ID nouveau maitre("+this.idCoor+") à " + j.intValue());
                        reso.sendTo(new ElectionMessage(this.id, j.intValue(), ElectionAlgorithm.BULLY, ElectionTypeMessage.BULLY_COOR));
                    }
                    catch (RemoteException ex)
                    {
                        ex.printStackTrace();
                    }
                }

            }
            endElection();
        }
    }
    
    /**
     * Fin de l'attente du nouveau maitre : on a reçu l'ID du nouveau maitre
     */
    public void doneWaitingForCOOR()
    {
        endElection();
    }

    /**
     * Fin de l'élection
     */
    public void endElection()
    {
        // TODO : println
        System.out.println("[ELECTION] Fin de l'élection");
        this.election = false;
    }

    /**
     * Accepte un message lié aux elections.
     *
     * @param m Le message
     */
    public void accepteMessage(Message m)
    {
        ElectionMessage em = (ElectionMessage)m;
        
        switch (em.getElectionMsg())
        {
            case BULLY_ELECTION:
                this.accepteElection(em.getIdFrom());
                break;
            case BULLY_ACK:
                this.accepteAck();
                break;
            case BULLY_COOR:
                this.accepteCoor(em.getIdFrom());
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
            System.out.println("[ELECTION] Message ELECTION reçu du processus " + j);
            reso.sendTo(new ElectionMessage(this.id, j, ElectionAlgorithm.BULLY, ElectionTypeMessage.BULLY_ACK));
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
            this.bullyWait.notifyACK();
    }

    /**
     * Accepte le message de nouveau maitre
     *
     * @param j ID du vainqueur de l'élection = nouveau maitre
     */
    public void accepteCoor(int j)
    {
            // TODO : println
            System.out.println("[ELECTION] NEWCOOR reçu, le nouveau maitre est " + j);
            this.idCoor = j;
            this.parent.setMaster(idCoor);
            this.newCoor = true;
            this.bullyWait.notifyCOOR();
    }
}
