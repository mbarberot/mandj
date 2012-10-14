package remote;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import modele.Modele;
import forme.Forme;


public class Processus {

	/* Objet contenant les méthodes appelables par le serveur */
	private ProcessusRemoteImpl myRemote;
	
	/* La liste des autres processus participants */
	private ArrayList<Integer> voisins;
	
	/* Indique le processus maître */
	private int masterId;
	
	/* Stub pour appeler les fcts de réseau*/
	private IReseau reso;
	
	/* Mutex pour le whiteboard */
	private Semaphore semWB;
	
	/* Le processus a accès aux données du wb */
	private Modele wb;
	
	/* Constantes */
	public static final String SERVER_NAME = "Reso";
	
	
	
	/* Constructeur */
	public Processus(String host, Modele mod)
	{
		this.wb = mod;
		this.voisins = new ArrayList<Integer>();
		this.masterId = 0;
		
		try {
			Registry reg = LocateRegistry.getRegistry(1099); // A modifier pour contact à distance
			reso = (IReseau)reg.lookup(SERVER_NAME);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public void connexionReso()
	{
		try {
			int pId = reso.register();
			this.myRemote = new ProcessusRemoteImpl(this,pId);
			this.reso.naming(pId, myRemote.getHost());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void recupereVoisins()
	{
		try {
			this.voisins = this.reso.getVoisins();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		//TODO println
		System.out.println("Voisins connus ");
		for(int v : voisins)
		{
			System.out.println(v);
		}
	}
	
	public void envoiNouveauDessin(Forme nF)
	{		
		

		/* Hail to the test !
		try {
			reso.sendTo(myRemote.getId(), -1, TypeMessage.ENVOI_NOUVELLE_FORME, nF.toByteArray());
		} catch (RemoteException e) {
			e.printStackTrace();
		}*/
		
		for(int idTo : voisins)
		{
			if(idTo != myRemote.getId())
			{
				try {
					reso.sendTo(myRemote.getId(), idTo, TypeMessage.ENVOI_NOUVELLE_FORME, nF.toByteArray());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void recoitDessin(byte[] forme)
	{
		wb.recoitDessin(Forme.getFromByteArray(forme));
	}

	
	public void deconnexion()
	{
		try {
			reso.quit(myRemote.getId());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
