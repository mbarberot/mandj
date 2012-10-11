package main;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;
import remote.ReseauImpl;

/**
 * Classe de lancement du serveur
 * 
 * @author Mathieu Barberot et Joan Racenet
 */
public class Reso
{
    public static final String NAME = "Reso" ;
    
    public static void main(String[] args)
    {
        try
        {
            ReseauImpl reso = new ReseauImpl();
            Remote stub = UnicastRemoteObject.toStub(reso);
            Naming.rebind(NAME,stub);
            //Remote stub = UnicastRemoteObject.exportObject(reso, 0);
            //Naming.rebind(NAME, stub);
            
            // TODO : println
            System.out.println("Serveur prêt");
        } 
        catch (Exception ex)
        {
            // TODO : println
            System.out.println("Erreur lors du démarrage du serveur Reso");
            ex.printStackTrace();
        }
        
        
    }
    
}
