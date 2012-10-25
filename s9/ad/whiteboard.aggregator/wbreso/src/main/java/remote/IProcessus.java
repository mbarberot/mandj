package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Interface de manipulation d'un processus via RMI
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public interface IProcessus extends Remote
{

    /**
     * Retourne si le processus est le maitre ou non
     * 
     * @return true si le processus est le maitre, false sinon
     * @throws RemoteException 
     */
    public boolean isMaster() throws RemoteException;

    /**
     * Transmission d'une nouvelle forme au processus
     * 
     * @param data La nouvelle forme
     * @throws RemoteException 
     */
    public void receptionNouvelleForme(String data) throws RemoteException;

    /**
     * Notification de l'arrivée d'un nouveau processus
     * 
     * @param idNew ID du nouveau processus
     * @throws RemoteException 
     */
    public void signaleNouveauVoisin(int idNew) throws RemoteException;

    /**
     * Autorisation de l'accès à la section critique pour ce processus.
     * Seul un processus maitre peut donner cet accès.
     * 
     * @throws RemoteException 
     */
    public void autoriserSectionCritique(int autorisation) throws RemoteException;

    /**
     * Acquisition de la liste des formes du processus
     * 
     * @return Liste des formes
     * @throws RemoteException 
     */
    public ArrayList<String> getListeForme() throws RemoteException;

    /**
     * Demande de section critique au processus.
     * Seul le maitre doit répondre.
     * 
     * @param idFrom ID du processus demandeur
     * @throws RemoteException 
     */
    public void demanderSectionCritique(int idFrom) throws RemoteException;
    
    /**
     * Signaler un timeout au client
     */
    public void signalerTimeout(int idFrom) throws RemoteException;
}
