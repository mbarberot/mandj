package remote;

import java.rmi.Remote;

public interface IReseau extends Remote 
{
	public int register(IProcessus proc);
	public void quit(int idProc);
        public void quit(IProcessus proc);
	public void sendTo(int idFrom, int idTo, int msg, Object data);
	public void sendAll(int idForm, int msg, Object data);
}