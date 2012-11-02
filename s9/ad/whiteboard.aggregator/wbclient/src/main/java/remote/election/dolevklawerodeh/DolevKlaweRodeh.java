package remote.election.dolevklawerodeh;

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
 * Implémentation de l'algorithme de Dolev Klawe et Rodeh
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public class DolevKlaweRodeh implements IElection, IDolevKlaweRodeh
{

    /**
     * Processus parent
     */
    private Processus parent;
    /**
     * Interface de discussion avec le serveur de Reseau
     */
    private IReseau reso;
    /**
     * ID du processus courant
     */
    private int id;
    /**
     * Numero courant
     */
    private int ci;
    /**
     * Numero du voisin actif
     */
    private int vac;
    /**
     * Numero du leader
     */
    private int lead;
    /**
     * true si un election est en cours, false sinon
     */
    private boolean election;
    private boolean receivedOne;

    /**
     * Etats dans lequel l'algorithme peut se trouver
     */
    private enum EtatDKR
    {

        actif, passif, leader, perdu
    };
    /**
     * Etat courant de l'algorithme
     */
    private EtatDKR etat;
    /**
     * Dernier message envoyé (pour la gestion du timeout)
     */
    private ElectionMessage em;

    /**
     * Constructeur
     *
     * @param parent Processus parent
     * @param reso Interface avec le serveur Reseau
     * @param id ID du processus
     */
    public DolevKlaweRodeh(Processus parent, IReseau reso, int id)
    {
        this.parent = parent;
        this.reso = reso;
        this.id = id;
        this.em = null;

        // INIT
        this.ci = id;
        this.vac = -1;
        this.lead = parent.getMaster();
        this.etat = EtatDKR.passif;
        this.election = false;
        this.receivedOne = false;
    }

    /**
     * Calcule l'ID du processus suivant
     *
     * @return ID du processus suivant dans l'anneau
     */
    private Integer getNextProc()
    {
        ArrayList<Integer> voisins = parent.getVoisins();

        int index = voisins.indexOf(new Integer(this.id));
        int size = voisins.size();

        return voisins.get((index + 1) % size).intValue();
    }

    /**
     * Démarrage de l'élection : - passage en mode actif - envoi de <one, id> au
     * processus suivant
     */
    public void demarrerElection()
    {
            // TODO : Println
            System.out.println("[ELECTION] Début de l'élection, Algorithme de Dolev Klawe et Rodeh");

            // INIT
            this.ci = id;
            this.vac = -1;
            this.lead = -1;
            this.election = false;
            this.receivedOne = false;
            this.etat = EtatDKR.actif;

            // TODO println
            System.out.println("[ELECTION] Envoi du premier token ONE");
            send(ElectionTypeMessage.DKR_ONE, new Integer(ci));
        
    }

    /**
     * Accepte un message d'élection puis mobilise la bonne méthode pour le
     * traiter.
     *
     * @param m Le message
     */
    public void accepteMessage(Message m)
    {
        ElectionMessage _em;
        if (m instanceof ElectionMessage)
        {
            _em = (ElectionMessage) m;
            int q = ((Integer) _em.getData()).intValue();

            switch (_em.getElectionMsg())
            {
                case DKR_ONE:
                    this.recoitOne(q);
                    break;
                case DKR_TWO:
                    this.recoitTwo(q);
                    break;
                case DKR_SMALL:
                    recoitSmall(q);
                    break;
            }
        }
    }

    /**
     * Renvoie le dernier message au processus suivant en tenant compte du
     * processus déconnecté.
     *
     * @param idProc ID du processus déconnecté
     */
    public void timeout(int idProc)
    {
        if (election && em != null)
        {
            if (idProc == em.getIdTo())
            {
                Integer j = (Integer) em.getData();
                ElectionTypeMessage typeMess = em.getElectionMsg();

                // TODO println
                System.out.println("[ELECTION] Timeout ! Envoi d'un nouveau message.");
                send(typeMess, j);
            }
        }
    }

    /**
     * Accepte le message <one,q>
     *
     * @param q Valeur du token ONE
     */
    public void recoitOne(int q)
    {
        // TODO println
        System.out.println("[ELECTION] Token reçu : <ONE," + q + ">");
        this.election = true;

        if (etat == EtatDKR.actif)
        {
            this.vac = q;
            if (vac == ci)
            {
                // TODO println
                System.out.println("[ELECTION] Victoire !");
                setMaster(vac);
                send(ElectionTypeMessage.DKR_SMALL, new Integer(vac));
            }
            else
            {
                // TODO println
                System.out.println("[ELECTION] Deuxième ronde");
                send(ElectionTypeMessage.DKR_TWO, vac);
            }
        }
        else
        {
            etat = EtatDKR.passif;
            // TODO println
            System.out.println("[ELECTION] Mode passif : transfert au prochain processus");
            send(ElectionTypeMessage.DKR_ONE, new Integer(q));
        }
    }

    /**
     * Accepte le message <two,q>
     *
     * @param q Valeur du token TWO
     */
    public void recoitTwo(int q)
    {
        // TODO println
        System.out.println("[ELECTION] Token reçu : <TWO," + q + ">");

        if (etat == EtatDKR.actif)
        {
            if (vac < ci && vac < q)
            {
                ci = vac;
                // TODO println
                System.out.println("[ELECTION] Fin du tour, relance d'un token ONE");
                send(ElectionTypeMessage.DKR_ONE, new Integer(ci));
            }
            else
            {
                // TODO println
                System.out.println("[ELECTION] Le processus est désormais PASSIF.");
                etat = EtatDKR.passif;
            }
        }
        else
        {
            // TODO println
            System.out.println("[ELECTION] Mode passif : transfert au prochain processus");
            send(ElectionTypeMessage.DKR_TWO, new Integer(q));
        }
    }

    /**
     * Accepte le message <small,q>
     *
     * @param q Valeur du token SMALL
     */
    public void recoitSmall(int q)
    {
        // TODO println
        System.out.println("[ELECTION] Token reçu : <SMALL," + q + ">");

        if (etat == EtatDKR.passif)
        {
            setMaster(q);
            // TODO println
            System.out.println("[ELECTION] Mode passif : Transfert au prochain processus.");
            send(ElectionTypeMessage.DKR_SMALL, new Integer(q));
        }
    }

    /**
     * Définit le nouveau maitre. Si le maitre est le processus courant, on
     * démarre le thread de traitement des demandes de section critiques.
     *
     * @param masterID ID du nouveau maitre
     */
    private void setMaster(int masterID)
    {
        // TODO println
        System.out.println("[ELECTION] Nouveau maitre : " + masterID);

        lead = masterID;
        parent.setMaster(masterID);

        if (masterID == id)
        {
            etat = EtatDKR.leader;
            parent.startThreadSC();
        }
        else
        {
            etat = EtatDKR.perdu;
        }

        this.election = false;
    }

    /**
     * Methode de simplification de l'envoi d'un message d'élection. On ne
     * stipule que les informations du token.
     *
     * @param typeMess Type de token (ONE, TWO ou SMALL)
     * @param value Valeur du token
     */
    private void send(ElectionTypeMessage typeMess, Integer value)
    {
        int next = getNextProc();
        try
        {
            em = new ElectionMessage(this.id, next, value, ElectionAlgorithm.DOLEV_KLAWE_RODEH, typeMess);
            // TODO println
            System.out.println("[ELECTION] Envoi de : " + em.toString() + " à " + next);
            reso.sendTo(em);
        }
        catch (RemoteException ex)
        {
            ex.printStackTrace();
        }
    }
    
    public boolean isInElection()
    {
        return election;
    }
}
