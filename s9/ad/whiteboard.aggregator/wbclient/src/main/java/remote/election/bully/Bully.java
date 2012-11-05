package remote.election.bully;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class Bully implements IElection, IBully
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
     * Cas du processus qui arrive en cours d'élection.
     * Il doit perdre cette élection ! 
     * Donc loser = true sinon false
     */
    private boolean loser;

    /**
     * Constructeur
     *
     * @param reso Interface reseau
     * @param parent Processus parent
     * @param id ID du processus
     * @throws RemoteException
     */
    public Bully(IReseau reso, Processus parent, int id) throws RemoteException
    {
        super();

        this.reso = reso;
        this.parent = parent;
        this.id = id;
        this.election = false;
        this.loser = false;
        this.ok = false;
        this.newCoor = false;
        this.bullyWait = null;
    }

    /**
     * Début de l'élection : - envoi des messages de types ELECTION aux
     * processus j tels que : j > i
     */
    public void demarrerElection()
    {
        // TODO : println
        System.out.println("[ELECTION] Initialisation");

        this.election = true;
        this.ok = false;
        this.newCoor = false;
        this.idCoor = -1;

        this.voisins = this.parent.getVoisins();
        // Propagation de l'election aux voisins 
        for (Integer j : voisins)
        {
            if (this.id < j.intValue())
            {
                // TODO : println
                System.out.println("[ELECTION] Envoie ELECTION au processus " + j);
                send(j.intValue(), ElectionTypeMessage.BULLY_ELECTION);
            }
        }

        // Lancement d'un thread pour l'attente d'un message ACK
        bullyWait = new ThreadBullyWait(this);
        bullyWait.waitForACK(TEMPO * voisins.size() + 1 * 2);

    }
    
    /**
     * Accepte un message lié aux elections.
     *
     * @param m Le message
     */
    public void accepteMessage(Message m)
    {
        ElectionMessage em = (ElectionMessage) m;

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
     * Fin de l'attente du message ACK - soit un message ACK a été reçu et on
     * attend un signe du nouveau maitre - soit le temps limite imparti est
     * écoulé et on s'autoproclame maitre
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
            // Diffusion du nouveau coordinateur
            // TODO : println
            System.out.println("[ELECTION] Victoire !");
            this.idCoor = this.id;
            this.voisins = parent.getVoisins();
            for (Integer j : voisins)
            {
                if (j.intValue() != this.id)
                {
                    // TODO : println
                    System.out.println("[ELECTION] Envoi ID nouveau maitre(" + this.id + ") à " + j.intValue());
                    send(j.intValue(),ElectionTypeMessage.BULLY_COOR);
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
        setMaster(idCoor);
        this.election = false;
    }

    

    /**
     * Accepte un message d'élection
     *
     * @param j ID du processus initiateur
     */
    public void accepteElection(int j)
    {       
        // TODO : println
        System.out.println("[ELECTION] Message ELECTION reçu du processus " + j);
        
        if(!loser)
        {
            // TODO : println
            System.out.println("[ELECTION] Envoi d'un message ACK à " + j);
            send(j, ElectionTypeMessage.BULLY_ACK);
        }

        if (!election)
        {
            this.demarrerElection();
        }
    }

    /**
     * Accepte un accord d'un processus. Un ACK est reçu que lorsque on
     * l'attend.
     */
    public void accepteAck()
    {
        // TODO : println
        System.out.println("[ELECTION] ACK reçu");
        this.ok = true;
        this.bullyWait.notifyACK();
    }

    /**
     * Accepte le message de nouveau maitre Un COOR peut être envoyé alors que
     * l'on ne l'attend pas !
     *
     * @param j ID du vainqueur de l'élection = nouveau maitre
     */
    public void accepteCoor(int j)
    {
        // TODO : println
        System.out.println("[ELECTION] NEWCOOR reçu de " + j);
        this.newCoor = true;
        this.idCoor = j;
        if (this.bullyWait != null)
        {
            this.bullyWait.notifyCOOR();
        }
    }

    /**
     * Gère la déonnection d'un processus.
     * Inutile pour cet algorithme.
     * 
     * @param idProc ID du processus déconnecté
     */
    public void timeout(int idProc)
    {
        // Nothing to do here !
    }

    /**
     * Retourne si l'élection est en cours ou pas
     * 
     * @return true si l'élection est en cours 
     */
    public boolean isInElection()
    {
        return election;
    }

    /**
     * On veut attendre la fin de l'élection pour acquérir le nouveau maitre.
     */
    public void waitNewMaster()
    {
        this.election = true;
        this.loser = true;
        
        if (this.bullyWait == null)
        {
            this.bullyWait = new ThreadBullyWait(this);
        }
        this.bullyWait.waitForCOOR();
    }
    
    
    
    /**
     * Envoi un message d'élection.
     * 
     * @param idTo Destinataire
     * @param typeMess Type du message (ELECTION, ACK ou COOR)
     */
    private void send(int idTo, ElectionTypeMessage typeMess)
    {
        try
        {
            ElectionMessage em = new ElectionMessage(this.id, idTo, null, ElectionAlgorithm.BULLY, typeMess);
            reso.sendTo(em);
        }
        catch (RemoteException ex)
        {
            ex.printStackTrace();
        }
    }
    
    /**
     * Définit le nouveau maitre.
     * Notifie le Processus du nouveau maitre.
     * Lance le thread de traitement des demandes de SC si nécessaire
     * 
     * @param masterID ID du maitre élu
     */
    private void setMaster(int masterID)
    {
        // TODO Println
        System.out.println("[ELECTION] Le nouveau maitre est " + masterID);
        
        this.parent.setMaster(masterID);
        
        if(this.id == masterID)
        {
            this.parent.startThreadSC();
        }
    }
}
