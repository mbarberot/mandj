package remote;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ProcessusRemoteImpl extends UnicastRemoteObject implements IProcessus {


	/* Identifiant du processus */
	private int pId;
	
	/* Instance du processus local pour échanger les informations */
	private Processus myLocal;
	
	/* Adresse de l'hôte local */
	private String host;
	
	/* Constante */
	public static final String CLIENT_NAME = "Client";
	
	protected ProcessusRemoteImpl(Processus myLocal, int nId) throws RemoteException {
		super();
		this.pId = nId;
		this.myLocal = myLocal;
		
		/* Enregistrement dans le rmiregistry */
		Registry reg = LocateRegistry.getRegistry();
		try {
			this.host = "rmi://" + InetAddress.getLocalHost().getHostAddress();
			System.out.println("Client enregistre a l'adresse " + host + "/" + CLIENT_NAME + this.pId);
			reg.rebind(host + "/" + CLIENT_NAME + this.pId, this);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		
	}

	public void recv(int msg, int idFrom, Object data) throws RemoteException{	
		switch(msg) {
			case (TypeMessage.ENVOI_NOUVELLE_FORME):
				myLocal.recoitDessin((byte[]) data);
				break;
			case (TypeMessage.CONNEXION_NOUVEAU_PROC):				
			case(TypeMessage.DECONNEXION_PROC):
				myLocal.recupereVoisins();
			break;			
		}
	}
	
	
	public int getId()
	{
		return this.pId;
	}

	public String getHost()
	{
		return this.host;
	}
	
	public void setMaster(int id)
	{
		
	}
	public void becomeMaster() throws RemoteException
	{
		this.myLocal.setMaster(this.pId);
		//TODO println
		System.out.println("Je suis le maitre ! mouahaha");
	}

	public boolean isMaster() throws RemoteException{
		return this.pId == this.myLocal.getMaster();
	}
}
