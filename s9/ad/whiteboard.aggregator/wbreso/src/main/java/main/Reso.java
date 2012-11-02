package main;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import remote.ReseauImpl;

/**
 * Classe de lancement du serveur
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public class Reso
{
    // Nom du serveur sur le registre RMI
    public static String SERVER_NAME;
    // Nom du client sur le registre RMI
    public static String CLIENT_NAME;
 
    public static void main(String[] args)
    {
        if(args.length < 2)
        {
            System.out.println("Usage : Reso <nom_rmi_reseau> <nom_rmi_client> <algo>");
            System.exit(1);
        }
        else
        {
            SERVER_NAME = args[0] ;
            CLIENT_NAME = args[1] ;
        }
        
        try
        {
            // Création du registre RMI
            LocateRegistry.createRegistry(1099);
            
            

            // Création du serveur Reso
            ReseauImpl reso = new ReseauImpl();

            // Rebind
            String url = "rmi://" + InetAddress.getLocalHost().getHostAddress() + "/" + SERVER_NAME;
            Naming.rebind(url, reso);

            // TODO : println
            System.out.println("Objet enregistre dans le registry a l'adresse : " + url);
            System.out.println("Serveur prêt ! ");

        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

    }
}
