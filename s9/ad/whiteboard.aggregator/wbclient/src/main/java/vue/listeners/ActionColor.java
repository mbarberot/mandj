package vue.listeners;

import controleur.Controleur;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JColorChooser;

/**
 * Listener des boutons de choix de couleur
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public class ActionColor implements ActionListener
{

    /**
     * Contrôleur à contacter
     */
    private Controleur ctrl;
    /**
     * Arrière ou Avant plan
     */
    private int ground;
    /**
     * Couleur assignée
     */
    private Color color;

    /**
     * Constructeur
     *
     * @param ctrl Contrôleur à contacter
     * @param ground Controleur.BG ou Controleur.FG
     * @param color Couleur par défaut
     */
    public ActionColor(Controleur ctrl, int ground, Color color)
    {
        this.ctrl = ctrl;
        this.ground = ground;
        this.color = color;
    }

    /**
     * Implémentation de l'ActionListener : Affichage d'un JColorChooser,
     * notification de la couleur au contrôleur et affichage de la couleur sur
     * le texte du bouton écouté.
     *
     * @param ae Contexte de l'évènement
     */
    public void actionPerformed(ActionEvent ae)
    {
        JButton source = (JButton) ae.getSource();
        this.color = JColorChooser.showDialog(
                source,
                "Nouvelle couleur d'"
                + (ground == Controleur.BG ? "arrière" : "avant")
                + "plan",
                color);
        source.setForeground(color);
        this.ctrl.setColor(ground, color);
    }
}
