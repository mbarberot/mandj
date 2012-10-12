package remote;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
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
     * Constructeur simple
     * @throws RemoteException 
     */
    public ReseauImpl () throws RemoteException
    {
        super();
        this.listeProc = new HashMap<Integer,IProcessus>();
        this.idProc = 0;
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
    public void naming(int idProc) throws RemoteException
    {
        try
        {
            IProcessus proc = (IProcessus)Naming.lookup(Reso.CLIENT_NAME+idProc);
            listeProc.put(idProc, proc);
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
    }


    public void sendTo(int idFrom, int idTo, int msg, Object data)
    {
        // TODO
        throw new UnsupportedOperationException("Not supported yet.");
    }

   

    

    
}
