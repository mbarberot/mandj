package remote;

import remote.messages.Message;
import remote.messages.TypeMessage;
import forme.Forme;
import forme.FormeFactory;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import main.Client;
import modele.Modele;
import remote.election.ElectionFactory;
import remote.election.IElection;

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
     * True => toutes les données d'initialisation sont là (voisins, maître et wb courant récupérés)
     */
    boolean initReady;
    
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

    public void setMaster(int masterId)
    {
        this.masterId = masterId;
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

    public ArrayList<Integer> getVoisins()
    {
        return voisins;
    }
    
    public int getId()
    {
    	return this.myRemote.getId();
    }
    
    

    /**
     * Connexion au serveur Reseau.
     * 
     * Le processus récupère un id via le serveur et déclare son stub à celui-ci
     * Il récupère au passage l'état actuel du WB
     */
    public synchronized void connexionReso()
    {
        try
        {
            int pId = reso.register();
            // Création de l'algo
            this.algo = ElectionFactory.createAlgoElection(Client.ALGO, reso, myRemote, this, pId);
            this.myRemote = new ProcessusRemoteImpl(this, pId, algo, Client.MACHINE_DISTANTE);
            this.reso.naming(pId);            
                        
            while(!this.initReady)
            {
            	try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
            
            
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Recupere la liste des voisins connus + récupère le processus maitre
     */
    public void recupereVoisins() //TODO : bloquer si élection en cours
    {
        try
        {
            this.voisins = this.reso.getVoisins();
            
            //
            // TODO : éviter cette partie si déjà maître
            //
            
            // Si un seul voisin => le processus local devient automatiquement le maitre
            if (this.voisins.size() == 1)
            {
                this.masterId = this.myRemote.getId();
                //TODO println
                System.out.println("Je suis le seul maître à bord");
                this.myRemote.devientMaster();
            } else
            {
                //TODO println
                System.out.println(this.voisins.size() + "voisins récupérés ");
                // Demander au serveur qui est le maître
                this.masterId = this.reso.whoIsMaster();
                //TODO println
                System.out.println("Le maître est " + this.masterId);
            }
            
         // Récupération du WB actuel
            this.reso.sendTo(new Message(this.myRemote.getId(), TypeMessage.DEMANDE_ETAT_WB, masterId, null));

        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Ajout d'un processus dans la liste des voisins
     * 
     * @param nId ID du nouveau processus
     */
    public void ajoutNouveauVoisin(int nId)
    {
        this.voisins.add(nId);
    }

    /**
     * Suppression d'un processus dans la liste des voisins
     *
     * @param vId ID du processus supprimé
     */
    public void suppressionVoisin(int vId)
    {
        this.voisins.remove(new Integer(vId));
    }

    /**
     * Récupère auprès du serveur l'état actuel du wb
     */
    public synchronized void recupereWB(ArrayList<String> wb)
    {

        // TODO println
        System.out.println("Récupération d'un WB à jour");

        this.wb.recoitWB(wb);

        // Le client est prêt à dessiner
        this.initReady = true;
        notifyAll();

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
            this.reso.sendTo(new Message(this.myRemote.getId(),  TypeMessage.DEMANDE_SC, this.masterId, null));
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
        
        if(this.accesWB == 1)
        {
        	synchronized(this.voisins)
        	{
		        Iterator iterVoisin = voisins.iterator();
		        while (iterVoisin.hasNext())
		        {
		            Integer idTo = (Integer) iterVoisin.next();
		            System.out.println("Envoi de la forme à " + idTo);
		                try
		                {
		                    reso.sendTo(new Message(myRemote.getId(),TypeMessage.ENVOI_NOUVELLE_FORME, idTo, nF.makeItSendable()));
		                }
		                catch (RemoteException e)
		                {
		                    e.printStackTrace();
		                }
		            
		        }
        	}        	
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
        	synchronized(wb)
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
     * @param idFrom
     */
    public void recoitTimeOut(int idFrom)
    {
    	System.out.println("Timeout : " + idFrom);
    	synchronized(this.voisins)
    	{
    		this.voisins.remove(new Integer(idFrom));
    	}
        if(idFrom == this.masterId)
        {
            System.out.println("Le maître ne répond plus : Démarrage de l'éléction !");
            this.algo.demarrerElection();
        }
    }
    
    /**
     * Autorisation d'accèder à la Section Critique.
     * Débloquage de l'attente.
     */
    public synchronized void recoitAccesSC(int autorisation)
    {
        //TODO println
        System.out.println("Accès à la SC autorisé");
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
