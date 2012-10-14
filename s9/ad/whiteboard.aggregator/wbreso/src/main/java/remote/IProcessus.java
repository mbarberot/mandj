package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public interface IProcessus extends Remote
{
    public void recv(int msg, int idFrom, Object data) throws RemoteException ;
}
