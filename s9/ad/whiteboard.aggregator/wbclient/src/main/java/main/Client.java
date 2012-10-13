package main;

import java.net.InetAddress;
import java.net.UnknownHostException;

import remote.Processus;
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
    public static void main (String[] args)
    {
        //
        // Mise en place du pattern MVC
        //
        Modele m = new Modele();
        Controleur c = new Controleur(m);
        Vue v = new Vue(c);
        m.addListener(v);
       
       
                
    }
    
}
