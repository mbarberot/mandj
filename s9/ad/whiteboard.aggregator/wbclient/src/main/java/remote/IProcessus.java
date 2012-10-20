package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public interface IProcessus extends Remote
{
    public boolean isMaster() throws RemoteException;
    
    public void receptionNouvelleForme(String data) throws RemoteException;
    public void signalUpdateVoisins() throws RemoteException;
    public void autoriserSectionCritique() throws RemoteException;
    
    public void demanderSectionCritique(int idFrom) throws RemoteException;
}
