package remote.election;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import remote.IReseau;
import remote.Processus;

/**
 * Implémentation de l'algorithme d'élection de Bully
 * @author Mathieu Barberot et Joan Racenet
 */
public class Bully extends UnicastRemoteObject implements IElection,IBully
{
    // en secondes
    private static final int TEMPO = 2 ;
    
    private IReseau reso;
    
    private int nbVoisins;
    private int id;
    private boolean election;
    private boolean ok;
    private boolean newCoor;
    private ArrayList<Integer> voisins;
    
    
    // Constructeur
    public Bully (IReseau reso, ArrayList<Integer> voisins, int id) throws RemoteException
    {
        super();
        
        this.reso = reso;
        
        this.nbVoisins = voisins.size();
        this.id = id;
        this.election = false;
        this.ok = false;
        this.newCoor = false;
        this.voisins = voisins;
        
    }
        
    public void demarrerElection()
    {
        this.election = true;
        this.ok = false;
        this.newCoor = false;
        
        for(Integer j : voisins)
        {
            if(id > j)
            {
                // Envoi ELECTION au Processus j
            }
        }
       
        
    }

    public void accepteElection(int j)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void accepteAck()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void accepteCoor(int j)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
}
