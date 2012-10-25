package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Interface de manipulation du serveur Reseau
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public interface IReseau extends Remote
{

    /**
     * Demande d'enregistrement sur le serveur
     * 
     * @return Un id unique pour le demandeur
     * @throws RemoteException 
     */
    public int register() throws RemoteException;

    /**
     * Notification au serveur qu'il peut effectuer un lookup
     * 
     * @param idProc ID du processus
     * @param host Hôte du processus
     * @throws RemoteException 
     */
    public void naming(int idProc, String host) throws RemoteException;

    /**
     * Notification au serveur du départ d'un processus
     * 
     * @param idProc ID du processus
     * @throws RemoteException 
     */
    public void quit(int idProc) throws RemoteException;

    /**
     * Envoi d'un message à un client
     * 
     * @param idFrom ID du processus emetteur
     * @param idTo ID du processus recepteur
     * @param msg Type de message envoyé
     * @param data Données du message
     * @throws RemoteException
     * @throws TimeOutException 
     */
    public void sendTo(int idFrom, int idTo, int msg, Object data) throws RemoteException, TimeOutException;

    /**
     * Méthode de récupération des processus voisins 
     * 
     * @return La liste des ID des processus voisins
     * @throws RemoteException 
     */
    public ArrayList<Integer> getVoisins() throws RemoteException;

    /**
     * Méthode de récupération des formes
     * 
     * @param idFrom ID du processus demandeur
     * @return Liste des formes à dessiner
     * @throws RemoteException 
     */
    public ArrayList<String> getEtatWB(int idFrom) throws RemoteException;

    /**
     * Retourne l'identifiant du processus maitre
     * 
     * @return ID du processus maitre
     * @throws RemoteException 
     */
    public int whoIsMaster() throws RemoteException;
}