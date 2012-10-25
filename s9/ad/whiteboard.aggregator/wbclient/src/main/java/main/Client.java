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

    /**
     * Main du client
     *
     * Usage : client machine_distante
     *
     * @param args
     */
    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            System.out.println("Usage : client <machine_distante>");

        } else
        {
            MACHINE_DISTANTE = args[0];

            //
            // Mise en place du pattern MVC
            //
            Modele m = new Modele();
            Controleur c = new Controleur(m);
            Vue v = new Vue(c);
            m.addListener(v);
        }
    }
}
