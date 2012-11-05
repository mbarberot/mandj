package main;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Enumeration;
import java.util.List;

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
            
      
          // Rendre l'adresse du rmiregistry disponible sur le réseau
            String myAddress="";

            // Recupere l'adresse adaptee
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); 

            while (en.hasMoreElements()){ 
               List<InterfaceAddress> i = en.nextElement().getInterfaceAddresses();

              for (InterfaceAddress l : i) {
                InetAddress addr = l.getAddress();

                if (addr.isSiteLocalAddress()){
                  myAddress=addr.getHostAddress();
                 }
               }
            }

            // Ensuite positionner l'adresse qui convient :

            System.setProperty("java.rmi.server.hostname", myAddress);



            // Création du serveur Reso
            ReseauImpl reso = new ReseauImpl();

            // Rebind
            String url = "rmi://" + myAddress + "/" + SERVER_NAME;
            Naming.rebind(url, reso);

            // TODO : println
            System.out.println("Objet enregistre dans le registry a l'adresse : " + url);
            System.out.println("Serveur prêt ! ");

        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        } catch (SocketException e) {
			e.printStackTrace();
		}

    }
}
