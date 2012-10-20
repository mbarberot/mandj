package remote;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

public class ProcessusRemoteImpl extends UnicastRemoteObject implements IProcessus {


	/* Identifiant du processus */
	private int pId;
	
	/* Instance du processus local pour échanger les informations */
	private Processus myLocal;
	
	/* Adresse de l'hôte local */
	private String host;
	
	/* Liste des demandes (ordonnées) d'accès à la SC */
	private LinkedList<Integer> demandesAccesSC;
	
	/* Constante */
	public static final String CLIENT_NAME = "Client";
	
	protected ProcessusRemoteImpl(Processus myLocal, int nId) throws RemoteException {
		super();
		this.pId = nId;
		this.myLocal = myLocal;
		this.demandesAccesSC = new LinkedList<Integer>();
		
		/* Enregistrement dans le rmiregistry */
		Registry reg = LocateRegistry.getRegistry();
		try {
			this.host = "rmi://" + InetAddress.getLocalHost().getHostAddress();
			System.out.println("Client enregistre a l'adresse " + host + "/" + CLIENT_NAME + this.pId);
			reg.rebind(host + "/" + CLIENT_NAME + this.pId, this);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		
	}

	/**
	 * Implémentation des fonctions remote (simple client)
	 * 
	 */
	/**
	 * Renvoie true si le processus en cours est le maître
	 */
	public boolean isMaster() throws RemoteException {
		return this.myLocal.getMaster() == this.pId;
	}
	
	/**
	 * Réception d'une nouvelle forme
	 */
	public void receptionNouvelleForme(String data) throws RemoteException {
		this.myLocal.recoitDessin(data);		
	}

	/**
	 * Signaler au processus que la liste des voisins connectés a été mise à jour
	 */
	public void signalUpdateVoisins() throws RemoteException {
		this.myLocal.recupereVoisins();
	}
	
	/**
	 * Autoriser l'accès à la section critique au processus
	 */
	public void autoriserSectionCritique() throws RemoteException
	{
		this.myLocal.recoitAccesSC();
	}
	
	/**
	 * Implémentation des fonctions remote utilisables uniquement en tant que maître
	 */	
	public void demanderSectionCritique(int idFrom) throws RemoteException
	{
		this.demandesAccesSC.add(idFrom);
		System.out.println(idFrom + " demande l'accès à la SC");
		if(this.demandesAccesSC.size() == 1)
			traiterFileSC();
	}
	
	public void traiterFileSC()
	{
		IReseau reso = myLocal.getReso();
		Integer idP;
		while((idP = this.demandesAccesSC.pollFirst()) != null)
		{
			//TODO println
			System.out.println("Traitement de la demande de " + idP);
			try {
				reso.sendTo(pId, idP, TypeMessage.AUTORISER_ACCES_SC, null);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
			//TODO println
			System.out.println("Traitement de la demande de " + idP + " terminée.");
		}
	}
	/**
	 * GETTERS & SETTERS
	 * @return
	 */
	public int getId()
	{
		return this.pId;
	}

	public String getHost()
	{
		return this.host;
	}

	




}
