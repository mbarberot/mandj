package controleur;

import forme.*;
import java.awt.Color;
import java.awt.Point;
import modele.Modele;

/**
 * Le controleur dans le patron de conception MVC
 *
 * Il reçoit les informations de la vue, prépare la forme puis la soumet au
 * modele.
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public class Controleur
{
    //
    // Constantes
    //  > Les formes

    public static final int PIX = 0;
    public static final int DRT = 1;
    public static final int REC = 2;
    public static final int ELL = 3;
    //  > Les plans
    public static final int BG = 4;
    public static final int FG = 5;
    /**
     * Modele (pattern MVC)
     */
    private Modele modele;
    /**
     * La forme à dessiner
     */
    private int forme;
    /**
     * Avancement du dessin = nombre de points placés
     */
    private int avancement;
    /**
     * Nombre de points à placer pour que la forme soit terminée
     */
    private int nbEtapes;
    /**
     * Couleur d'arrière plan
     */
    private Color bg;
    /**
     * Couleur d'avant plan
     */
    private Color fg;
    /**
     * Epaisseur du trait de contour de la forme
     */
    private float epaisseur;
    /**
     * Les points de la forme
     */
    private Point[] points;

    /**
     * Constructeur de la classe
     *
     * @param modele Le modele (pattern MVC)
     */
    public Controleur(Modele modele)
    {
        this.modele = modele;
        this.forme = PIX;
        this.avancement = 0;
        this.nbEtapes = 1;
        this.bg = Color.WHITE;
        this.fg = Color.BLACK;
        this.epaisseur = 1.0f;
        this.points = new Point[2];
    }

    /**
     * Définit la forme dessinée
     *
     * @param forme La forme (utiliser les constantes de la classe Controleur)
     */
    public void setForme(int forme)
    {
        this.forme = forme;
        this.setNbEtapes(forme);
    }

    /**
     * Définit le nombre d'étapes (clics) nécessaires pour achever la forme
     *
     * @param forme La forme (utiliser les constantes de la classe Controleur)
     */
    public void setNbEtapes(int forme)
    {
        switch (forme)
        {
            case PIX:
                this.nbEtapes = 1;
                break;
            case DRT:
            case REC:
            case ELL:
                this.nbEtapes = 2;
                break;
        }
    }

    /**
     * Définit la couleur du dessin
     *
     * @param ground Arrière ou avant plan (utiliser les constantes de la classe
     * Controleur)
     * @param color La couleur
     */
    public void setColor(int ground, Color color)
    {
        switch (ground)
        {
            case BG:
                this.bg = color;
                break;
            case FG:
                this.fg = color;
                break;
        }
    }

    /**
     * Définit l'épaisseur du trait
     *
     * @param epaisseur L'épaisseur du trait
     */
    public void setEpaisseur(float epaisseur)
    {
        this.epaisseur = epaisseur;
    }

    /**
     * Enregistre un clic envoyé par le tableau blanc
     *
     * @param p Coordonnée du clic
     */
    public void clic(Point p)
    {
        if (avancement < nbEtapes)
        {
            points[avancement] = p;
            if (avancement == nbEtapes - 1)
            {
                try
                {
                    ajouterDessin();
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                avancement = 0;
            } else
            {
                avancement++;
            }
        } else
        {
            avancement = 0;
        }
    }

    /**
     * Ajoute un dessin au modele
     *
     * @throws Exception
     */
    private void ajouterDessin() throws Exception
    {
        Forme f;
        switch (forme)
        {
            case PIX:
                f = new Pixel(points[avancement], bg, fg, epaisseur);
                break;
            case DRT:
                f = new Droite(points, bg, fg, epaisseur);
                break;
            case REC:
                f = new Rectangle(points, bg, fg, epaisseur);
                break;
            case ELL:
                f = new Ellipse(points, bg, fg, epaisseur);
                break;
            default:
                throw new Exception("Forme géométrique non prise en charge !");

        }
        modele.ajouterDessin(f);
    }
    
    /**
     * Indiquer au modèle la fermeture de la fenêtre
     */
    public void quitterWB()
    {
    	modele.quitterServeur();
    }
}
