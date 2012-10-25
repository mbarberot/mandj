package main;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import remote.ReseauImpl;

/**
 * Classe de lancement du serveur
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public class Reso
{
    // Nom du serveur sur le registre RMI
    public static final String SERVER_NAME = "Reso";
    // Nom du client sur le registre RMI
    public static final String CLIENT_NAME = "Client";

    public static void main(String[] args)
    {
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
