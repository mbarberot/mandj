package gui.action;

import gui.DialogUserSynchronization;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;
import main.Main;

/**
 * Action déclenchée lors du clic sur le bouton "Synchroniser"
 */
public class ActionSynch extends AbstractAction {

    private static final long serialVersionUID = 1L;

    /**
     * Constructeur
     *
     * @param text Nom de l'action
     * @param icon Icone de l'action
     * @param accelerator Touche de raccourci pour l'action
     * @param desc Courte description de l'action
     */
    public ActionSynch(String text, Icon icon, KeyStroke accelerator, String desc) {
        super();

        putValue(Action.SMALL_ICON, icon);
        putValue(Action.LARGE_ICON_KEY, icon);
        putValue(Action.NAME, text);
        putValue(Action.ACCELERATOR_KEY, accelerator);
        putValue(Action.SHORT_DESCRIPTION, desc);
    }

    /**
     * Traitement lors du déclenchement de l'action
     *
     * @param e Objet contenant les détails de l'evenement
     */
    public void actionPerformed(ActionEvent e) {
        //TODO: Faire l'appel de la synchronisation
        //Synchronisation avec le compte BDovore,
        //et si on détecte des conflits, on affichera le dialog de conflig pour les résoudre manuellement.
        DialogUserSynchronization dialog = new DialogUserSynchronization(Main.appFrame, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setVisible(true);
    }
}
