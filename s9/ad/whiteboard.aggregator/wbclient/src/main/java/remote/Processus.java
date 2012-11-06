package remote;

import forme.Forme;
import forme.FormeFactory;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import main.Client;
import modele.Modele;
import remote.election.ElectionFactory;
import remote.election.IElection;
import remote.messages.Message;
import remote.messages.TypeMessage;

/**
 * Classe permettant l'interfaçage tableau blanc / réseau
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public class Processus
{

    /**
     * Objet contenant les méthodes appelables par le serveur
     */
    private ProcessusRemoteImpl myRemote;
    /**
     * La liste des autres processus participants
     */
    private ArrayList<Integer> voisins;
    /**
     * Indique le processus maître
     */
    private int masterId;
    /**
     * Stub pour appeler les fcts de réseau
     */
    private IReseau reso;
    /**
     * Algorithme pour l'élection
     */
    private IElection algo;
    /**
     * Mutex pour le whiteboard
     */
    private int accesWB; // 0 : en attente , 1 : ok, 2 : refus
    /**
     * Le processus a accès aux données du wb
     */
    private Modele wb;
    /**
     * True => toutes les données d'initialisation sont là (voisins, maître et
     * wb courant récupérés)
     */
    boolean initReady;
    /**
     * Dans le cas d'un processus arrivant après le départ du maitre. True, si
     * le processus doit attendre un nouveau maitre, false sinon
     */
    private boolean waitingMaster;

    /**
     * Constructeur
     *
     * @param host Hote du processus
     * @param mod Modele du MVC
     */
    public Processus(String host, Modele mod)
    {
        this.wb = mod;
        this.voisins = new ArrayList<Integer>();
        this.masterId = -1;
        this.accesWB = 0;

        try
        {
            reso = (IReseau) Naming.lookup(host + "/" + Client.SERVER_NAME);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        catch (NotBoundException e)
        {
            e.printStackTrace();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Retourne le nombre de voisins
     *
     * @return Le nombre de voisins
     */
    public int getSizeVoisins()
    {
        return this.voisins.size();
    }

    /**
     * Retourne l'ID du processus maitre
     *
     * @return L'ID du processus maitre
     */
    public int getMaster()
    {
        return this.masterId;
    }

    /**
     * Définit l'id du processus maitre. Si on attendait un maitre, on continue
     * l'initialisation
     *
     * @param masterId ID du nouveau maitre
     */
    public void setMaster(int masterId)
    {
        this.masterId = masterId;

        //TODO println
        System.out.println("Le maître est " + masterId);

        if (waitingMaster)
        {
            waitingMaster = false;
            recupereWB();
        }
    }

    /**
     * Retourne l'interface du serveur Reseau
     *
     * @return L'interface du serveur Reseau
     */
    public IReseau getReso()
    {
        return this.reso;
    }

    /**
     * Retourne l'algorithme d'election
     *
     * @return L'interface de l'algorithme d'election
     */
    public IElection getAlgo()
    {
        return algo;
    }

    /**
     * Retourne la liste des voisins
     *
     * @return Liste des voisins
     */
    public synchronized ArrayList<Integer> getVoisins()
    {
        return voisins;
    }

    /**
     * Retourne l'ID du processus courant
     *
     * @return ID du processus courant
     */
    public int getId()
    {
        return this.myRemote.getId();
    }

    /**
     * Connexion au serveur Reseau. Le processus récupère un id via le serveur
     * et déclare son stub à celui-ci
     */
    public synchronized void connexionReso()
    {
        try
        {
            // Récupération de l'ID
            int pId = reso.register();

            // Création de l'algo
            this.algo = ElectionFactory.createAlgoElection(Client.ALGO, reso, this, pId);

            // Création de l'interface avec le Reseau
            this.myRemote = new ProcessusRemoteImpl(this, pId, algo);

            // Notification au serveur Reseau de l'enregistrement du processus
            String myHostName = InetAddress.getLocalHost().getHostName();
            this.reso.naming(pId, myHostName);

            // Attente de la fin de l'initialisation
            while (!this.initReady)
            {
                try
                {
                    wait();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        catch (UnknownHostException e1)
        {
            e1.printStackTrace();
        }
    }

    /**
     * Recupere la liste des voisins connus. Une fois ceux-ci acquis, on
     * determine le maitre.
     */
    public synchronized void recupereVoisins()
    {
        try
        {
            this.voisins = this.reso.getVoisins();
            defineMaster();
        }
        catch (RemoteException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Determine le maitre, s'il est en ligne. Dans le cas où le maitre est hors
     * ligne, on attend l'élection d'un nouveau maitre Une fois le maitre
     * acquis, on passe à l'étape de récupération des formes.
     *
     * @throws RemoteException
     */
    public synchronized void defineMaster() throws RemoteException
    {
        // Si un seul voisin => le processus local devient automatiquement le maitre

        //TODO println
        System.out.println(this.voisins.size() + " voisins récupérés ");
        // Demander au serveur qui est le maître
        int masterID = this.reso.whoIsMaster();

        if (masterID == -1)
        {
            int id = this.getId();
            int size = this.voisins.size();
            if(size > 1 && voisins.get(size -1).intValue() != this.getId())
            {
                // TODO println
                System.out.println("Pas de maitre, election d'un nouveau maitre");
                this.algo.demarrerElection();
                this.waitingMaster = true;
            }
            else
            {
                // Pas de maitre.
                // On attend la fin de la prochaine élection
                // (potentiellement en cours)
                // TODO println
                System.out.println("Pas de maitre, attente d'un nouveau maitre");

                this.waitingMaster = true;
                algo.waitNewMaster();
            }
        }
        else
        {
            this.waitingMaster = false;
            setMaster(masterID);
            recupereWB();
        }
    
    }


    /**
     * Recupere les formes du maitre. Une fois cette tâche terminée, on notifie
     * de la fin de la phase d'initialisation
     */
    public synchronized void recupereWB()
    {
        try
        {
            // Récupération du WB actuel
            ArrayList<String> wb = this.reso.getWB(getId());
            for (String f : wb)
            {
                recoitDessin(f);
            }

            // TODO println
            System.out.println("WB à jour");

            // Le client est prêt à dessiner
            this.initReady = true;
            notifyAll();
        }
        catch (RemoteException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Ajout d'un processus dans la liste des voisins
     *
     * @param nId ID du nouveau processus
     */
    public void ajoutNouveauVoisin(int nId)
    {
        synchronized (this.voisins)
        {
            this.voisins.add(nId);
        }
    }

    /**
     * Suppression d'un processus dans la liste des voisins
     *
     * @param vId ID du processus supprimé
     */
    public void suppressionVoisin(int vId)
    {
        synchronized (this.voisins)
        {
            this.voisins.remove(new Integer(vId));
        }
    }

    /**
     * Retourner l'état du WB sous forme de liste de String
     *
     * @return La liste des formes formatée pour un envoi
     */
    public ArrayList<String> getMyWB()
    {
        //
        // TODO : Renommer la méthode en qqch comme "serializeListeForme" ?
        //

        ArrayList<Forme> myWB = this.wb.getFormes();
        ArrayList<String> myStringWB = new ArrayList<String>();

        for (Forme f : myWB)
        {
            myStringWB.add(f.makeItSendable());
        }

        return myStringWB;
    }

    /**
     * Le client vient de dessiner une forme et veut la diffuser à tous
     *
     * @param nF
     */
    public void envoiNouveauDessin(Forme nF)
    {

        // Demander au maître l'accès au tableau
        // => sendTo (moi, maitre, ACCES_WB)
        // => le maître répond :
        // 			- Si OK => la forme est diffusée à tout le monde
        //			- Sinon => la demande d'accès au WB est placée en file d'attente
        try
        {
            this.reso.sendTo(new Message(this.myRemote.getId(), TypeMessage.DEMANDE_SC, this.masterId, null));
        }
        catch (RemoteException e1)
        {
            e1.printStackTrace();
        }

        // Attente de l'autorisation
        synchronized (this)
        {
            while (this.accesWB == 0)
            {
                try
                {
                    wait();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }

        // Broadcast du nouveau dessin

        if (this.accesWB == 1)
        {
            System.out.println("Accès la SC autorisé :)");
            synchronized (this.voisins)
            {
                Iterator iterVoisin = voisins.iterator();
                while (iterVoisin.hasNext())
                {
                    Integer idTo = (Integer) iterVoisin.next();
                    System.out.println("Envoi de la forme à " + idTo);
                    try
                    {
                        reso.sendTo(new Message(myRemote.getId(), TypeMessage.ENVOI_NOUVELLE_FORME, idTo, nF.makeItSendable()));
                    }
                    catch (RemoteException e)
                    {
                        e.printStackTrace();
                    }

                }
            }

            // Signaler la fin de la diffusion
            try
            {
                System.out.println("Attente avant d'envoyer le message de fin d'accès à la SC");
                Thread.sleep(10000);
            }
            catch (InterruptedException e1)
            {
                e1.printStackTrace();
            }
            try
            {
                reso.sendTo(new Message(myRemote.getId(), TypeMessage.FIN_ACCES_SC, this.masterId, null));
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("Accès à la SC refusé :(");
        }
        this.accesWB = 0;
    }

    /**
     * Ajout d'une forme reçue au modèle
     *
     * @param forme Nouveau dessin
     */
    public void recoitDessin(String forme)
    {
        try
        {
            synchronized (wb)
            {
                wb.recoitDessin(FormeFactory.createForme(forme));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Deconnexion du client
     */
    public void deconnexion()
    {
        try
        {
            reso.quit(myRemote.getId());
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Supprime un voisin qui a renvoyé un timeout
     *
     * @param idFrom
     */
    public void recoitTimeOut(int idFrom)
    {
        System.out.println("Timeout : " + idFrom);
        synchronized (this.voisins)
        {
            this.voisins.remove(new Integer(idFrom));
        }

        // Si le maitre est déconnecté, on lance une élection
        // Sinon, on signale la deconnexion à l'algorithme au cas où il en aurait besoin
        if (idFrom == this.masterId)
        {
            if (!this.algo.isInElection())
            {
                System.out.println("Le maître ne répond plus : Démarrage de l'éléction !");
                this.algo.demarrerElection();
            }
            else
            {
                this.algo.timeout(idFrom);
            }
        }
        else
        {
            if(this.algo.isInElection())
            {
                this.algo.timeout(idFrom);
            }
        }

    }

    /**
     * Autorisation d'accèder à la Section Critique. Débloquage de l'attente.
     */
    public synchronized void recoitAccesSC(int autorisation)
    {
        //TODO println
        System.out.println("Accès à la SC " + (autorisation == 1 ? "autorisé" : "refusé"));
        this.accesWB = autorisation;
        notifyAll();
    }

    /**
     * Si le client local devient maître => lancer le traitement de la SC
     */
    public void startThreadSC()
    {
        this.myRemote.devientMaster();
    }
}
