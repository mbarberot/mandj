package main;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import remote.ReseauImpl;

/**
 * Classe de lancement du serveur
 * 
 * @author Mathieu Barberot et Joan Racenet
 */
public class Reso {
	public static final String SERVER_NAME = "Reso";
	public static final String CLIENT_NAME = "Client";

	public static void main(String[] args) {
		try {
			LocateRegistry.createRegistry(1099);        	
			ReseauImpl reso = new ReseauImpl();
			String url = "rmi://" + InetAddress.getLocalHost().getHostAddress()
					+ "/" + SERVER_NAME;

			Naming.rebind(url, reso);
			System.out.println("Objet enregistre dans le registry a l'adresse : " + url);
			System.out.println("Serveur prêt ! ");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}

}
