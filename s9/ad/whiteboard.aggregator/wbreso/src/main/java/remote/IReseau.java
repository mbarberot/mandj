package remote;

import java.rmi.Remote;

public interface IReseau extends Remote {
	public int register();
	public boolean quit();
	public void sendTo(int idProc, int msg);
	public void sendAll(int msg);
}
