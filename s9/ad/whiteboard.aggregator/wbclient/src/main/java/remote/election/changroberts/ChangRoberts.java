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
     * Démarrage de l'élection - le processus est placé dans l'état "candidat" -
     * on envoie le premier token
     */
    public void demarrerElection()
    {
        // TODO : Println
        System.out.println("[ELECTION] Démarrage de l'élection");

        this.etat = EtatElection.CAND;
        try
        {
            // TODO : Println
            System.out.println("[ELECTION] Envoi de <tok," + this.id + "> à " + getNextProc());
            em = new ElectionMessage(this.id, getNextProc(), new Integer(id), ElectionAlgorithm.CHANG_ROBERTS, ElectionTypeMessage.CR_TOK);
            reso.sendTo(em);
        }
        catch (RemoteException ex)
        {
            ex.printStackTrace();
        }
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
            ElectionMessage em = (ElectionMessage) m;

            if (em.getElectionMsg() == ElectionTypeMessage.CR_TOK)
            {
                this.accepteTOK(
                        ((Integer) em.getData()).intValue());
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
        // TODO : Println
        System.out.println("[ELECTION] Accepte <tok," + j + ">");
        switch (this.etat)
        {
            //
            // Etat CANDIDAT à l'élection
            //
            case CAND:

                // On reçoit son ID dans le token = Victoire
                if (j == this.id)
                {
                    // TODO : Println
                    System.out.println("[ELECTION] Victoire !");
                    this.etat = EtatElection.LEADER;
                    this.parent.setMaster(j);
                    this.parent.startThreadSC();

                }
                else if (this.id < j)
                {
                    // TODO : Println
                    System.out.println("[ELECTION] Défaite ! Le nouveau maitre est " + j);
                    this.etat = EtatElection.PERDU;
                    this.parent.setMaster(j);
                    try
                    {
                        // TODO : Println
                        System.out.println("[ELECTION] Transfert de <tok," + j + "> à " + getNextProc());
                        em = new ElectionMessage(this.id, getNextProc(), new Integer(j), ElectionAlgorithm.CHANG_ROBERTS, ElectionTypeMessage.CR_TOK);
                        reso.sendTo(em);

                    }
                    catch (RemoteException ex)
                    {
                        ex.printStackTrace();
                    }
                }
                break;

            case SLEEP:
                // TODO : Println
                System.out.println("[ELECTION] Sleeping : Le nouveau maitre est " + j);
                this.etat = EtatElection.PERDU;
                this.parent.setMaster(j);
                try
                {
                    // TODO : Println
                    System.out.println("[ELECTION] Transfert de <tok," + j + "> à " + getNextProc());
                    em = new ElectionMessage(this.id, getNextProc(), new Integer(j), ElectionAlgorithm.CHANG_ROBERTS, ElectionTypeMessage.CR_TOK);
                    reso.sendTo(em);

                }
                catch (RemoteException ex)
                {
                    ex.printStackTrace();
                }
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
            Integer j = (Integer) em.getData();

            try
            {
                // TODO : Println
                System.out.println("[ELECTION] Timeout : Nouveau transfert de <tok," + j.intValue() + "> à " + getNextProc());
                em = new ElectionMessage(this.id, getNextProc(), j, ElectionAlgorithm.CHANG_ROBERTS, ElectionTypeMessage.CR_TOK);
                reso.sendTo(em);
            }
            catch (RemoteException ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
