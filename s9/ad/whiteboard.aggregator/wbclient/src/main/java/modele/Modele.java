package modele;

import forme.Forme;
import java.util.ArrayList;

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
     * Constructeur
     */
    public Modele()
    {
        this.listeners = new ArrayList<ModeleListener>();
        this.formes = new ArrayList<Forme>();
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
    }
}
