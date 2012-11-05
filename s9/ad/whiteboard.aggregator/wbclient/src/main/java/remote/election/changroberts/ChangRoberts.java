package remote.election.changroberts;

import java.rmi.RemoteException;
import java.util.ArrayList;
import remote.IReseau;
import remote.Processus;
import remote.election.IElection;
import remote.messages.ElectionAlgorithm;
import remote.messages.ElectionMessage;
import remote.messages.ElectionTypeMessage;
import remote.messages.Message;

/**
 * Implémentation de l'interface IChangRoberts, définissant le déroulement de
 * l'algorithme et de l'interface IElection permettant la manipulation des
 * élections.
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public class ChangRoberts implements IChangRoberts, IElection
{
    /**
     * Les différents états du processus dans l'algorithme
     */
    private enum EtatElection
    {

        CAND,
        LEADER,
        PERDU,
        SLEEP
    }
    
    /**
     * Processus parent
     */
    private Processus parent;
    /**
     * Interface avec le serveur Reseau
     */
    private IReseau reso;
    /**
     * Etat du processus dans l'algorithme
     */
    private EtatElection etat;
    /**
     * ID du processus
     */
    private int id;
    /**
     * true si en election, false sinon
     */
    private boolean election;
    /**
     * Dernier message <tok,id> envoyé (pour un renvoi éventuel après timeout)
     */
    private ElectionMessage em;

    /**
     * Constructeur
     *
     * @param parent Conteneur de l'algorithme
     * @param reso Interface avec le serveur de Reseau
     * @param id ID du processus courant
     */
    public ChangRoberts(Processus parent, IReseau reso, int id)
    {
        this.parent = parent;
        this.reso = reso;
        this.id = id;
        this.etat = EtatElection.SLEEP;
        this.election = false;
    }

    

    /**
     * Démarrage de l'élection - le processus est placé dans l'état "candidat" -
     * on envoie le premier token
     */
    public void demarrerElection()
    {
        // TODO : Println
        System.out.println("[ELECTION] Démarrage de l'élection");

        this.etat = EtatElection.CAND;
        this.election = true;

        // TODO : Println
        System.out.println("[ELECTION] Envoi du premier token");
        send(id); 
    }

    /**
     * Accepte un message d'élection (token)
     *
     * @param m Le message
     */
    public void accepteMessage(Message m)
    {
        if (m instanceof ElectionMessage)
        {
            ElectionMessage newEM = (ElectionMessage) m;
            int val = ((Integer)newEM.getData()).intValue();

            if (newEM.getElectionMsg() == ElectionTypeMessage.CR_TOK)
            {
                this.accepteTOK(val);
            }
        }
    }
    
   
    
    
    
    
    
    /**
     * Accepte le token du processus j et execute l'algorithme.
     *
     * @param j ID du processus dans le token
     */
    public void accepteTOK(int j)
    {
        this.election = true;
        
        // TODO : Println
        System.out.println("[ELECTION] Accepte <tok," + j + ">");
        
        switch (this.etat)
        {
            case CAND:
                
                setMaster(j);
                
                if(this.id == j)
                {
                    // TODO println
                    System.out.println("[ELECTION] Victoire !");
                }
                else if(this.id < j)
                {
                    // TODO println
                    System.out.println("[ELECTION] Défaite ! Le nouveau maitre est " + j);
                
                    // TODO : Println
                    System.out.println("[ELECTION] Transfert du token du maitre au processus suivant");
                    send(j);
                }
                break;

            case SLEEP:
                
                // TODO : Println
                System.out.println("[ELECTION] Sleeping : Le nouveau maitre est " + j);
                setMaster(j);
                
                // TODO : Println
                System.out.println("[ELECTION] Sleeping : Transfert du token du maitre au processus suivant");
                send(j);
        }

    }

    /**
     * Gestion du timeout. Dans l'anneau, un timeout fais perdre le token. Le
     * processus met à jour sa liste des voisins puis notifie l'algorithme. On
     * envoie alors le token au processus suivant dans la nouvelle liste.
     *
     * @param idProc ID du processus déconnecté
     */
    public void timeout(int idProc)
    {
        if (idProc == em.getIdTo())
        {
            int j = ((Integer) em.getData()).intValue();
            // TODO : Println
            System.out.println("[ELECTION] Timeout : Ré-envoi du token");
            send(j);
        }
    }
    
    /**
     * Retourne si le processus est en cours d'élection ou non.
     * 
     * @return true si en élection, false sinon
     */
    public boolean isInElection()
    {
        return election;
    }
    
    /**
     * Attend qu'un nouveau maitre soit élu pour ne pas créer de problèmes !
     */
    public void waitNewMaster()
    {
        // Le processus est endormi. 
        // Il participe à l'élection mais sera forcément perdant
        this.etat = EtatElection.SLEEP;
    }
    
    
    
    
    
    
    
    
    
    
    /**
     * Génère l'ID du processus suivant
     *
     * @return ID du processus suivant
     */
    private Integer getNextProc()
    {
        ArrayList<Integer> voisins = parent.getVoisins();

        int index = voisins.indexOf(new Integer(this.id));
        int size = voisins.size();

        return voisins.get((index + 1) % size).intValue();
    }
    
    /**
     * Définit le nouveau maitre.
     * Si le maitre est le processus courant, on démarre le thread de la SC.
     * 
     * @param masterID ID du processus maitre
     */
    private void setMaster(int masterID)
    {
        this.parent.setMaster(masterID);
        
        if(this.id == masterID)
        {   
            this.etat = EtatElection.LEADER;    
            this.parent.startThreadSC();    
        }
        else if(this.id < masterID)
        {
            this.etat = EtatElection.PERDU;
        }
        
        this.election = false;
    }
    
    /**
     * Envoie un <tok,j> au processus suivant
     * 
     * @param j Valeur du token
     */
    private void send(int j)
    {   
        try
        {
            Integer q = new Integer(j);
            int next = getNextProc();
            
            // TODO : Println
            System.out.println("[ELECTION] Envoi de <tok," + j + "> à " + next);
            em = new ElectionMessage(this.id, next, j, ElectionAlgorithm.CHANG_ROBERTS, ElectionTypeMessage.CR_TOK);
            reso.sendTo(em);
        }
        catch (RemoteException ex)
        {
            ex.printStackTrace();
        }
    }
}
