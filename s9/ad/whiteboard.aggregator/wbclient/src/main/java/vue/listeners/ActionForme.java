package vue.listeners;

import controleur.Controleur;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Listener des boutons de dessin
 *
 * @author Mathieu Barberot
 */
public class ActionForme implements ActionListener
{

    /**
     * Controleur à contacter
     */
    private Controleur ctrl;
    /**
     * Forme assignée au bouton
     */
    private int forme;

    /**
     * Constructeur
     *
     * @param ctrl Controleur à contacter
     * @param forme Forme assignée au bouton
     */
    public ActionForme(Controleur ctrl, int forme)
    {
        this.ctrl = ctrl;
        this.forme = forme;
    }

    /**
     * Implémentation de l'ActionListener : Notification au contrôleur de la
     * forme à dessiner
     *
     * @param ae
     */
    public void actionPerformed(ActionEvent ae)
    {
        ctrl.setForme(forme);
    }
}
