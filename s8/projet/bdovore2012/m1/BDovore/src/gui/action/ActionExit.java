package gui.action;

import gui.FrameMain;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import javax.swing.*;

/**
 * Action déclenchée lors du clic sur le bouton "Quitter"
 */
public class ActionExit extends AbstractAction {

    private static final long serialVersionUID = 1L;

    /**
     * Constructeur
     *
     * @param text Nom de l'action
     * @param icon Icone de l'action
     * @param accelerator Touche de raccourci pour l'action
     * @param desc Courte description de l'action
     */
    public ActionExit(String text, Icon icon, KeyStroke accelerator, String desc) {
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
        
        // Fermer l'accès à la base de données
        try {
            FrameMain.db.shutdown();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Impossible de se déconnecter de la base de données", "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        System.out.println("DB Shutdown");

        System.exit(0);
    }
}
