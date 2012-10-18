package remote;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import main.Reso;

/**
 * Serveur Réseau
 * Implémentation de l'interface IReseau
 * 
 * @author Mathieu Barberot et Joan Racenet
 */
public class ReseauImpl extends UnicastRemoteObject implements IReseau
{  
    /**
     * Compteur des processus du réseau pour l'affectation de leur ID 
     */
    private int idProc;
    
    
    /**
     * Liste des processus du réseau
     */
    private HashMap<Integer,IProcessus> listeProc ;
    
    /**
     * File des demandes en attente
     */
    private ArrayList<Message> listeMess; 
    /**
     * Constructeur simple
     * @throws RemoteException 
     */
    public ReseauImpl () throws RemoteException
    {
        super();
        this.listeProc = new HashMap<Integer,IProcessus>();
        this.idProc = 0;
        this.listeMess = new ArrayList<Message>();
    }

    /**
     * Enregistre un processus sur le réseau
     * @return l'ID du processus
     */
    public int register()
    {
        int id = this.idProc;
        this.idProc++;
        
        // TODO : println
        System.out.println("register -> ID = "+id);
        
        return id;
    }
    
    /**
     * Lookup sur le nom du client pour l'ajouter aux processus du réseau
     * 
     * @param idProc - ID du processus
     * @throws RemoteException 
     */
    public void naming(int idProc, String host) throws RemoteException
    {
        try
        {
        	Registry reg = LocateRegistry.getRegistry(1099); // A modifier pour contact à distance
        	
        	/*for(String bounded : reg.list())
        		System.out.println(bounded);*/
        	
            IProcessus proc = (IProcessus)reg.lookup(host + "/" + Reso.CLIENT_NAME + idProc);
            listeProc.put(idProc, proc);
            
            // Si c'est le premier processus a être connecté : il devient maître
            if(listeProc.size() == 1)
            	listeProc.get(idProc).becomeMaster();
            
            // Signaler aux participants l'arrivée du nouveau processus
            broadcast(TypeMessage.CONNEXION_NOUVEAU_PROC, idProc, null);
           
        } 
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        
    }
    
    /**
     * Départ d'un processus
     * @param idProc - ID du processus
    */
    public void quit(int idProc)
    {
        // TODO : println
        System.out.println("quit(id) -> ID = "+idProc);
        listeProc.remove(idProc);
        
        // Notifier aux processus restants qu'un processus vient de quitter
        broadcast(TypeMessage.DECONNEXION_PROC, idProc, null);
    }
    
    public void sendTo(int idFrom, int idTo, int msg, Object data)
    {
        switch(msg)
        {
        	case TypeMessage.ENVOI_NOUVELLE_FORME:
        		IProcessus ipro = listeProc.get(new Integer(idTo));		
				System.out.println("Envoi de la forme de " + idFrom + " à " + idTo);
			
			try {
				ipro.recv(msg, idFrom, data);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
        	break;
        		
        	default:
        		throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }

    /**
     * Retourne la liste des numéros de processus sous la forme d'arraylist
     * 
     */
	public ArrayList<Integer> getVoisins() throws RemoteException {
		ArrayList<Integer> numVoisins = new ArrayList<Integer>();
		
		Iterator<Integer> iter = this.listeProc.keySet().iterator();
		
		while(iter.hasNext())
		{
			numVoisins.add(iter.next());
		}
		
		return numVoisins;
	}

	/**
	 * Recherche qui est le maître parmis les processus enregistrés
	 */
	public int getMaster() throws RemoteException{
		for(int idProc : listeProc.keySet())
		{
			if(listeProc.get(idProc).isMaster()) return idProc;
		}
		return -1;
	}
	/**
	 * Envoi un message à tous les processus participants
	 * @param msg
	 * @param data
	 */
   
	public void broadcast(int msg, int from, Object data)
	{
		
        for(IProcessus iProc : listeProc.values())
        {
        	try {
				iProc.recv(msg, from, data);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
        }
	}
	
    

    
}
