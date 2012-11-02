package remote;

import remote.messages.TypeMessage;
import remote.messages.Message;
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
	 * Traitement des messages
	 */
	private ThreadTraitementMessages messages;
	
	/**
	 * Constructeur simple
	 * 
	 * @throws RemoteException
	 */
	public ReseauImpl() throws RemoteException {
		super();
		this.idProc = 0;
		this.messages = new ThreadTraitementMessages();
		this.messages.start();
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
	 * Lookup sur le nom du client pour l'ajouter aux processus du réseau et
	 * notification aux autres processus de l'arrivée du nouveau.
	 * 
	 * @param idProc
	 *            - ID du processus
	 * @param clientHost
	 *            Hote du processus
	 * @throws RemoteException
	 */
	public void naming(int idProc, String clientHost) throws RemoteException {
		try 
		{
			String host = InetAddress.getByName(clientHost).getHostAddress();
			IProcessus proc = (IProcessus) Naming.lookup("//" + host + "/" + Reso.CLIENT_NAME + idProc);
			
			messages.ajoutNouveauClient(idProc, proc);

			// TODO println
			System.out.println("Ajout du proc " + idProc);

			/* Signaler aux participants l'arrivée du nouveau processus*/
			sendTo(new Message(idProc, TypeMessage.CONNEXION_NOUVEAU_PROC, -1, null));
			

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
		this.messages.suppressionClient(idProc);		
	}

	/**
	 * Envoi d'un message de idFrom vers idTo
	 * 
	 * @param idFrom
	 *            ID de l'emetteur
	 * @param idTo
	 *            ID du recepteur
	 * @param msg
	 *            Type du message
	 * @param data
	 *            Données du message
	 * @throws RemoteException
	 * @throws TimeOutException
	 */
	public void sendTo(Message m)
			throws RemoteException {
		

        Thread th = new Thread(new ThreadAjoutFile(m, this.messages));
        th.start();
	}

	/**
	 * Retourne la liste des numéros de processus sous la forme d'arraylist
	 * 
	 * @return Liste des voisins
	 * @throws RemoteException
	 */
	public ArrayList<Integer> getVoisins() throws RemoteException {
		return this.messages.getListeClients();
	}
	
	/**
	 * Retourne le WB le plus à jour parmis tous les clients déjà connectés
	 * @param idFrom
	 * @return
	 * @throws RemoteException
	 */
	public ArrayList<String> getWB(int idFrom) throws RemoteException{
		return this.messages.getWB(idFrom);
	}


	
	/**
	 * Permet de récupérer l'identifiant du maître parmi les processus
	 * participants
	 * 
	 * @return ID du processus ou -1 s'il n'y a pas de maitre
	 * @throws RemoteException
	 */
	public int whoIsMaster() throws RemoteException {
		return this.messages.getMaster();
	}
}
