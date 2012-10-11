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

    
    public int register(IProcessus proc)
    {
        listProc.add(proc);
    }

    public boolean quit()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public void sendTo(int idProc, int msg)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void sendAll(int msg)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
