package remote;

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
			Registry reg = LocateRegistry.getRegistry(1099); // A modifier pour
																// contact à
																// distance

			IProcessus proc = (IProcessus) reg.lookup(host + "/"
					+ Reso.CLIENT_NAME + idProc);
			listeProc.put(idProc, proc);

			//TODO println
			System.out.println("Ajout du proc " + idProc);
			
			// Signaler aux participants l'arrivée du nouveau processus
			broadcast(TypeMessage.CONNEXION_NOUVEAU_PROC, idProc, null);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * Départ d'un processus
	 * 
	 * @param idProc
	 *            - ID du processus
	 */
	public void quit(int idProc) {
		// TODO : println
		System.out.println("quit(id) -> ID = " + idProc);
		listeProc.remove(idProc);

		// Notifier aux processus restants qu'un processus vient de quitter
		broadcast(TypeMessage.DECONNEXION_PROC, idProc, null);
	}

	/**
	 * Envoi d'un message de idFrom vers idTo
	 */
	public void sendTo(int idFrom, int idTo, int msg, Object data) {
		IProcessus dest = this.listeProc.get(idTo);
		switch (msg) {
		case TypeMessage.CONNEXION_NOUVEAU_PROC:
		case TypeMessage.DECONNEXION_PROC:
			try {
				dest.signalUpdateVoisins();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			break;
		case TypeMessage.DEMANDE_SC:			
			try {
				dest.demanderSectionCritique(idFrom);
			} catch (RemoteException e) {
				e.printStackTrace();
			}			
			break;
		case TypeMessage.AUTORISER_ACCES_SC:
			try {
				dest.autoriserSectionCritique();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			break;
		case TypeMessage.ENVOI_NOUVELLE_FORME:
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
	 * Envoi un message à tous les processus participants
	 * 
	 * @param msg
	 * @param data
	 */

	public void broadcast(int msg, int from, Object data) {
		for (int iProc : listeProc.keySet()) {
			sendTo(from, iProc, msg, data);
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
