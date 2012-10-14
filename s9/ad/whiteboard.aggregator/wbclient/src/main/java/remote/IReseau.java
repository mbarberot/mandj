package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IReseau extends Remote
{
	public int register() throws RemoteException;
    public void naming(int idProc, String host) throws RemoteException;
	public void quit(int idProc) throws RemoteException;
	public void sendTo(int idFrom, int idTo, int msg, Object data) throws RemoteException;
	public ArrayList<Integer> getVoisins() throws RemoteException; 
}