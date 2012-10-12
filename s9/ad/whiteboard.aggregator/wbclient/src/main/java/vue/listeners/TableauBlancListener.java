package vue.listeners;

import controleur.Controleur;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Listener de la souris sur le canvas
 *
 * Seule la méthode mouseClicked possède une implémentation. Les autres méthodes
 * étant inutiles dans notre cas.
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public class TableauBlancListener implements MouseListener
{

    /**
     * Controleur (pattern MVC)
     */
    private Controleur ctrl;

    /**
     * Constructeur de la classe
     *
     * @param ctrl Le controleur à contacter
     */
    public TableauBlancListener(Controleur ctrl)
    {
        this.ctrl = ctrl;
    }

    /**
     * Implémentation de MouseListener :
     *
     * Transmet les coordonnées du clic au contrôleur
     *
     * @param me Informations sur l'évènement
     */
    public void mouseClicked(MouseEvent me)
    {
        ctrl.clic(me.getPoint());
    }

    //
    // Inutiles 
    //
    public void mousePressed(MouseEvent me)
    {
    }

    public void mouseReleased(MouseEvent me)
    {
    }

    public void mouseEntered(MouseEvent me)
    {
    }

    public void mouseExited(MouseEvent me)
    {
    }
}
