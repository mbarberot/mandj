package remote;


import java.rmi.Remote;

public interface IWhiteBoard extends Remote {
	public void drawRect(int hg, int bd);
	public void anotherFunction();
}
