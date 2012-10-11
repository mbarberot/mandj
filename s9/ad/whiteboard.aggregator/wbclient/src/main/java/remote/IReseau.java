package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IReseau extends Remote
{
	public int register(IProcessus proc) throws RemoteException;
	public void quit(int idProc) throws RemoteException;
        public void quit(IProcessus proc) throws RemoteException;
	public void sendTo(int idFrom, int idTo, int msg, Object data) throws RemoteException;
}