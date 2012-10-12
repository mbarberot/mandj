package vue.listeners;

import controleur.Controleur;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Listener de spinner
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public class ChangeSpinner implements ChangeListener
{

    /**
     * Controleur à contacter
     */
    private Controleur ctrl;

    /**
     * Constructeur
     *
     * @param ctrl Le controleur à contacter
     */
    public ChangeSpinner(Controleur ctrl)
    {
        this.ctrl = ctrl;
    }

    /**
     * Implémentation du ChangeListener : Notification au controleur de la
     * nouvelle valeur du spinner lorsqu'elle change.
     *
     * @param ce Contexte de l'évènement
     */
    public void stateChanged(ChangeEvent ce)
    {
        JSpinner spinner = (JSpinner) ce.getSource();

        float trait = ((SpinnerNumberModel) spinner.getModel()).getNumber().floatValue();

        ctrl.setEpaisseur(trait);
    }
}
