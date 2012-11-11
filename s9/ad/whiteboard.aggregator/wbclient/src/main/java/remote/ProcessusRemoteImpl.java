package remote;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import main.Client;
import remote.election.IElection;
import remote.messages.Message;

/**
 * Classe implémentant la couche réseau
 * 
 * @author Mathieu Barberot et Joan Racenet
 */
public class ProcessusRemoteImpl extends UnicastRemoteObject implements
		IProcessus {

	/**
	 * Identifiant du processus
	 */
	private int pId;
	/**
	 * Instance du processus local pour échanger les informations
	 */
	private Processus myLocal;
	/**
	 * Algorithme d'élection à contacter pour les messages d'élection
	 */
	private IElection algo;
	/**
	 * Thread pour le traitement de la liste des demandes d'accès à la SC (si
	 * maître)
	 */
	private ThreadTraitementSC traitementSC;

	protected ProcessusRemoteImpl(Processus myLocal, int nId, IElection algo)
			throws RemoteException {
		super();
		this.pId = nId;
		this.myLocal = myLocal;
		this.algo = algo;
		this.traitementSC = null;
	}

	/**
	 * Renseigne si le processus est le maitre
	 * 
	 * @return true si le processus est le maitre, false sinon.
	 * @throws RemoteException
	 */
	public boolean isMaster() throws RemoteException {
		return this.myLocal.getMaster() == this.pId;
	}

	/**
	 * Reception d'une nouvelle forme On remonte cette forme vers l'interfaçage
	 * WB / Reseau
	 * 
	 * @param data
	 *            La nouvelle forme
	 * @throws RemoteException
	 */
	public void receptionNouvelleForme(String data) throws RemoteException {
		this.myLocal.recoitDessin(data);
	}

	/**
	 * Notification de l'arrivée d'un nouveau processus
	 * 
	 * @param idNew
	 *            ID du nouveau processus
	 * @throws RemoteException
	 */
	public void signaleNouveauVoisin(int idNew) throws RemoteException {
		// => un nouveau voisin est arrivé
		if (idNew != this.getId()) {
			this.myLocal.ajoutNouveauVoisin(idNew);
		} else // => le processus local vient d'arriver (récupération du WB, des
				// voisins, du maître)
		{
			this.myLocal.recupereVoisins();
		}
	}

	/**
	 * Autoriser l'accès à la section critique au processus
	 */
	public void autoriserSectionCritique(int autorisation)
			throws RemoteException {
		this.myLocal.recoitAccesSC(autorisation);
	}

	/**
	 * Renvoie la liste des formes du processus
	 * 
	 * @return Liste des formes
	 * @throws RemoteException
	 */
	public ArrayList<String> getListeForme() throws RemoteException {
		return this.myLocal.getMyWB();
	}

	/**
	 * Implémentation des fonctions remote utilisables uniquement en tant que
	 * maître
	 */
	public void devientMaster() {
		this.traitementSC = new ThreadTraitementSC(this.myLocal.getReso(),
				this.pId);
		traitementSC.start();
	}

	/**
	 * Demande d'accès la section critique de la part d'un processus
	 * 
	 * @param idFrom
	 *            ID du processus demandeur
	 * @throws RemoteException
	 */
	public void demanderSectionCritique(int idFrom) throws RemoteException {
		if (this.isMaster()) {
			this.traitementSC.ajoutDemande(idFrom);
		}
	}

	/**
	 * Dérive la réception des messages liés aux elections sur l'objet dédié
	 * 
	 * @param m
	 *            Le message
	 * @throws RemoteException
	 */
	public void accepteMessageElection(Message m) throws RemoteException {
		this.algo.accepteMessage(m);
	}

	/**
	 * Retourne l'ID du processus
	 * 
	 * @return ID du processus
	 */
	public int getId() {
		return this.pId;
	}

	/**
	 * Reception d'un timeout
	 * 
	 * @param idFrom
	 *            ID du processus ne répondant pas
	 * @throws RemoteException
	 */
	public void signalerTimeout(int idFrom) throws RemoteException {
		this.myLocal.recoitTimeOut(idFrom);
	}

	public void signalerFinAccesSC(int idFrom) throws RemoteException {
		this.traitementSC.signalerFinAccesSC();

	}
}
