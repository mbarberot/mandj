package main;

import controleur.Controleur;
import modele.Modele;
import vue.Vue;

/**
 * Classe principale du client
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public class Client
{

    public static String MACHINE_DISTANTE;
    public static String SERVER_NAME;
    public static String CLIENT_NAME;
    public static String ALGO;

    /**
     * Main du client
     *
     * Usage : client machine_distante
     *
     * @param args
     */
    public static void main(String[] args)
    {
        if (args.length < 4)
        {
            System.out.println("Usage : Reso <nmachine_distante> <nom_rmi_reseau> <nom_rmi_client> <algo>");
            System.out.println("algo = chang_roberts | bully | dolev_klawe_rodeh");
            System.exit(0);
        } else
        {
            MACHINE_DISTANTE = args[0];
            SERVER_NAME = args[1];
            CLIENT_NAME = args[2];
            ALGO = args[3];
        }



        //
        // Mise en place du pattern MVC
        //
        Modele m = new Modele();
        Controleur c = new Controleur(m);
        Vue v = new Vue(c);
        m.addListener(v);

    }
}
