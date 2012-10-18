package modele;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import remote.Processus;
import forme.Forme;

/**
 * Classe modele dans le patron de conception MVC
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public class Modele
{

    /**
     * Listeners du modele (vues)
     */
    private ArrayList<ModeleListener> listeners;
    /**
     * Liste des formes dessinées
     */
    private ArrayList<Forme> formes;
    
    /**
     * On conserve le processus pour pouvoir notifier l'ajout de formes
     */
    private Processus proc;

    /**
     * Constructeur
     */
    public Modele()
    {
        this.listeners = new ArrayList<ModeleListener>();
        this.formes = new ArrayList<Forme>();
        
        // Création du processus
        try {
			String host = "rmi://" + InetAddress.getLocalHost().getHostAddress();
			this.proc = new Processus(host, this);
			proc.connexionReso();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    }

    /**
     * Ajoute un listener à la liste
     *
     * @param listener Le nouveau listener
     */
    public void addListener(ModeleListener listener)
    {
        this.listeners.add(listener);
        majVues();
    }

    /**
     * Supprime un listener de la liste
     *
     * @param listener Le listener à éliminer
     */
    public void remListener(ModeleListener listener)
    {
        this.listeners.remove(listener);
    }

    /**
     * Notifie les listeners d'un changement du modele. Les listeners reçoivent
     * alors la liste la plus à jour.
     */
    public void majVues()
    {
        for (ModeleListener l : listeners)
        {
            l.redessine(formes);
        }
    }

    /**
     * Ajout d'un dessin à la liste. Cette méthode est utilisée par le
     * contrôleur lorsqu'une forme est achevée sur l'IHM
     *
     * @param f La forme
     */
    public void ajouterDessin(Forme f)
    {
        this.formes.add(f);
        majVues();
        // Signaler au serveur l'ajout de forme
        if(this.proc != null)
        	this.proc.envoiNouveauDessin(f);
    }
    
    /**
     * Ajout d'un dessin envoyé par le serveur à la liste.
     * La fonction est appelée par le processus réseau lié
     * @param f
     */
    public void recoitDessin(Forme f)
    {
    	this.formes.add(f);
        majVues();
    }
    
    public void quitterServeur()
    {
    	proc.deconnexion();
    }
}
