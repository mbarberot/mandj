package remote;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import main.Reso;

/**
 * Serveur Réseau Implémentation de l'interface IReseau
 * 
 * @author Mathieu Barberot et Joan Racenet
 */
public class ReseauImpl extends UnicastRemoteObject implements IReseau {
	/**
	 * Compteur des processus du réseau pour l'affectation de leur ID
	 */
	private int idProc;

	/**
	 * Liste des processus du réseau
	 */
	private HashMap<Integer, IProcessus> listeProc;

	/**
	 * File des demandes en attente
	 */
	private LinkedList<Message> listeMess;

	/**
	 * Constructeur simple
	 * 
	 * @throws RemoteException
	 */
	public ReseauImpl() throws RemoteException {
		super();
		this.listeProc = new HashMap<Integer, IProcessus>();
		this.idProc = 0;
		this.listeMess = new LinkedList<Message>();
	}

	/**
	 * Enregistre un processus sur le réseau
	 * 
	 * @return l'ID du processus
	 */
	public int register() {
		int id = this.idProc;
		this.idProc++;

		// TODO : println
		System.out.println("register -> ID = " + id);

		return id;
	}

	/**
	 * Lookup sur le nom du client pour l'ajouter aux processus du réseau
	 * 
	 * @param idProc
	 *            - ID du processus
	 * @throws RemoteException
	 */
	public void naming(int idProc, String host) throws RemoteException {
		try {

			IProcessus proc = (IProcessus) Naming.lookup("rmi://" + InetAddress.getByName(host).getHostAddress() + "/"
					+ Reso.CLIENT_NAME + idProc);
			listeProc.put(idProc, proc);

			//TODO println
			System.out.println("Ajout du proc " + idProc);
			
			// Signaler aux participants l'arrivée du nouveau processus
			broadcast(Message.CONNEXION_NOUVEAU_PROC, idProc, null);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * Départ d'un processus
	 * 
	 * @param idProc
	 *            - ID du processus
	 * @throws TimeOutException 
	 * @throws RemoteException 
	 */
	public void quit(int idProc) throws RemoteException {
		// TODO : println
		System.out.println("quit(id) -> ID = " + idProc);
		listeProc.remove(idProc);
	}

	/**
	 * Envoi d'un message de idFrom vers idTo
	 * @throws RemoteException, TimeOutException 
	 */
	public void sendTo(int idFrom, int idTo, int msg, Object data) throws RemoteException, TimeOutException {
		IProcessus dest = this.listeProc.get(idTo);
		
		// On attend un temps aléatoire de 0.5 à 3 secondes avant de traiter le message
		/*int desync_time = (int) (Math.random() * 3000 + 500);
		try {
			Thread.sleep(desync_time);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}*/
		
		// Gestion du timeout
		if(listeProc.get(new Integer(idTo)) == null)
		{
			throw new TimeOutException("Le client " + idTo + " n'est plus connecté");
		}
		
		switch (msg) {
		case Message.CONNEXION_NOUVEAU_PROC:
			try {
				//TODO à modifier
				dest.signaleNouveauVoisin(idFrom);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			break;
		case Message.DEMANDE_SC:			
			try {
				dest.demanderSectionCritique(idFrom);
			} catch (RemoteException e) {
				e.printStackTrace();
			}			
			break;
		case Message.AUTORISER_ACCES_SC:
			try {
				dest.autoriserSectionCritique();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			break;
		case Message.ENVOI_NOUVELLE_FORME:
			try {
				dest.receptionNouvelleForme((String)data);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			break;
		default:
			throw new UnsupportedOperationException("Not supported yet.");
		}

	}

	/**
	 * Retourne la liste des numéros de processus sous la forme d'arraylist
	 * 
	 */
	public ArrayList<Integer> getVoisins() throws RemoteException {
		ArrayList<Integer> numVoisins = new ArrayList<Integer>();

		Iterator<Integer> iter = this.listeProc.keySet().iterator();

		while (iter.hasNext()) {
			numVoisins.add(iter.next());
		}

		return numVoisins;
	}
	
	/**
	 * Recupère la liste des voisins la plus à jour auprès des processus participants
	 */
	public ArrayList<String> getEtatWB(int idFrom) throws RemoteException {
		ArrayList<String> res = new ArrayList<String>();
		ArrayList<String> tmp = null;
		for(Integer i : this.listeProc.keySet())
		{
			if(i != idFrom)
			{
				tmp = this.listeProc.get(i).getListeForme();
				if(tmp.size() > res.size())
				{
					res = tmp;
				}
			}
		}
		
		return res;
	}

	/**
	 * Envoi un message à tous les processus participants
	 * 
	 * @param msg
	 * @param data
	 * @throws TimeOutException 
	 * @throws RemoteException 
	 */

	public void broadcast(int msg, int from, Object data) throws RemoteException {
		for (int iProc : listeProc.keySet()) {
			try {
				sendTo(from, iProc, msg, data);
			} catch (TimeOutException e) {}
		}
	}

	/**
	 * Permet de récupérer l'identifiant du maître parmi les processus participants
	 * Si aucun maître n'est élu, la fonction renvoie -1
	 */
	public int whoIsMaster() throws RemoteException {
		for (int proc : this.listeProc.keySet()) {
			if (this.listeProc.get(proc).isMaster())
				return proc;
		}
		return -1;
	}

}
