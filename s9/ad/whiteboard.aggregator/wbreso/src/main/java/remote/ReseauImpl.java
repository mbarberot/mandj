package remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Serveur Réseau
 * Implémentation de l'interface IReseau
 * 
 * @author Mathieu Barberot et Joan Racenet
 */
public class ReseauImpl extends UnicastRemoteObject implements IReseau
{   
    /**
     * Liste des processus du réseau
     */
    private ArrayList<IProcessus> listProc ;
    
    
    /**
     * Constructeur simple
     * @throws RemoteException 
     */
    public ReseauImpl () throws RemoteException
    {
        super();
        listProc = new ArrayList<IProcessus>();
    }

    /**
     * Enregistre un processus sur le réseau
     * @param proc - Le processus
     * @return l'ID du processus
     */
    public int register(IProcessus proc)
    {
        listProc.add(proc);
        return listProc.indexOf(proc);
    }
    
    /**
     * Départ d'un processus
     * @param idProc - ID du processus
    */
    public void quit(int idProc)
    {
        listProc.remove(idProc);
    }

    /**
     * Départ d'un processus
     * @param proc - Processus
     */
    public void quit(IProcessus proc)
    {
        listProc.remove(proc);
    }

    public void sendTo(int idFrom, int idTo, int msg, Object data)
    {
        // TODO
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}
