package gui.action;

import gui.FrameMain;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;
import main.Main;

/**
 * Action déclenchée lors du clic sur le bouton "Explorer/Rechercher"
 */
public class ActionDBExplorer extends AbstractAction {

    private static final long serialVersionUID = 1L;

    /**
     * Constructeur
     *
     * @param text Nom de l'action
     * @param icon Icone de l'action
     * @param accelerator Touche de raccourci pour l'action
     * @param desc Courte description de l'action
     */
    public ActionDBExplorer(String text, Icon icon, KeyStroke accelerator, String desc) {
        super();

        putValue(Action.SMALL_ICON, icon);
        putValue(Action.LARGE_ICON_KEY, icon);
        putValue(Action.NAME, text);
        putValue(Action.SHORT_DESCRIPTION, desc);
        putValue(Action.ACCELERATOR_KEY, accelerator);
    }

    /**
     * Traitement lors du déclenchement de l'action
     *
     * @param e Objet contenant les détails de l'evenement
     */
    public void actionPerformed(ActionEvent e) {
        Main.appFrame.switchCard(FrameMain.cardExplorerName);
    }
}
