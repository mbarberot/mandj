package remote;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import modele.Modele;
import forme.Forme;
import forme.FormeFactory;

public class Processus
{

    /*
     * Objet contenant les méthodes appelables par le serveur
     */
    private ProcessusRemoteImpl myRemote;
    /*
     * La liste des autres processus participants
     */
    private ArrayList<Integer> voisins;
    /*
     * Indique le processus maître
     */
    private int masterId;
    /*
     * Stub pour appeler les fcts de réseau
     */
    private IReseau reso;
    /*
     * Mutex pour le whiteboard
     */
    private boolean accesWB;
    /*
     * Le processus a accès aux données du wb
     */
    private Modele wb;
    /*
     * Constantes
     */
    public static final String SERVER_NAME = "Reso";

    /*
     * Constructeur
     */
    public Processus(String host, Modele mod)
    {
        this.wb = mod;
        this.voisins = new ArrayList<Integer>();
        this.masterId = -1;
        this.accesWB = false;

        try
        {
            reso = (IReseau) Naming.lookup(host + "/" + SERVER_NAME);
        } catch (RemoteException e)
        {
            e.printStackTrace();
        } catch (NotBoundException e)
        {
            e.printStackTrace();
        } catch (MalformedURLException e) {
			e.printStackTrace();
		}

    }

    /**
     * GETTERS & SETTERS
     *
     * @return
     */
    public int getSizeVoisins()
    {
        return this.voisins.size();
    }
    
    public int getMaster()
    {
        return this.masterId;
    }
    
    public IReseau getReso()
    {
    	return this.reso;
    }

    /**
     * Le processus récupère un id via le serveur et déclare son stub à celui-ci
     * Il récupère au passage l'état actuel du WB
     */
    public void connexionReso()
    {
        try
        {
            int pId = reso.register();
            this.myRemote = new ProcessusRemoteImpl(this, pId);
            this.reso.naming(pId, myRemote.getHost());
            // Récupération du WB actuel
            recupereWB();
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Recupere la liste des voisins connus + récupère le processus maitre
     */
    public void recupereVoisins()
    {
        try
        {
            this.voisins = this.reso.getVoisins();
            //TODO éviter cette partie si déjà maître
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
           
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Récupère auprès du serveur l'état actuel du wb
     */
    public void recupereWB()
    {
    	try {
    		//TODO println
    		System.out.println("Récupération d'un WB à jour");
			this.wb.recoitWB(this.reso.getEtatWB(myRemote.getId()));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    }

    /**
     * Retourner l'état du WB sous forme de liste de String
     * @return
     */
    public ArrayList<String> getMyWB()
    {
    	ArrayList<Forme> myWB = this.wb.getFormes();
    	ArrayList<String> myStringWB = new ArrayList<String>();
    	
    	for(Forme f : myWB)
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
        // On vérifie d'abord si le maître est encore en ligne. Si ce n'est pas le cas, on déclenche
        // une élection.
       /* if (this.masterId == -1)
        {
            declencheBullyElection();
        }*/

        // Demander au maître l'accès au tableau
        // => sendTo (moi, maitre, ACCES_WB)
        // => le maître répond :
        // 			- Si OK => la forme est diffusée à tout le monde
        //			- Sinon => la demande d'accès au WB est placée en file d'attente
    	try {
			this.reso.sendTo(this.myRemote.getId(), this.masterId, Message.DEMANDE_SC, null);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
    	
    	// Attente de l'autorisation
    	synchronized(this)
    	{
    		while(!this.accesWB)
    		{
    			try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    		}
    	}
    	
        for (int idTo : voisins)
        {
            if (idTo != myRemote.getId())
            {
                try
                {
                    reso.sendTo(myRemote.getId(), idTo, Message.ENVOI_NOUVELLE_FORME, nF.makeItSendable());
                } catch (RemoteException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Délenchement de l'élection d'un nouveau maître par l'algo de Bully
     */
    private void declencheBullyElection()
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Ajout d'une forme reçue au modèle
     *
     * @param forme
     */
    public void recoitDessin(String forme)
    {
        try
        {
            wb.recoitDessin(FormeFactory.createForme(forme));
        } catch (Exception ex)
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
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Autoriser l'accès à la SC
     */
    public synchronized void recoitAccesSC()
    {
    	//TODO println
    	System.out.println("Accès à la SC autorisé");
    	this.accesWB = true;
    	notifyAll();
    }
    
}
