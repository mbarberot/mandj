package gui.action;

import db.SearchQuery;
import gui.FrameMain;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.io.*;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;
import main.Main;

/**
 * Action déclenchée lors du clic sur le bouton "A propos"
 */
public class ActionImport extends AbstractAction {

    private static final long serialVersionUID = 1L;

    /**
     * Constructeur
     *
     * @param text Nom de l'action
     * @param icon Icone de l'action
     * @param accelerator Touche de raccourci pour l'action
     * @param desc Courte description de l'action
     */
    public ActionImport(String text, Icon icon, KeyStroke accelerator, String desc) {
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
    public void actionPerformed(ActionEvent e) 
    {
        String getResultQuery = null;
        
        int nbEntree = 0;
        
        BufferedReader reader = null;
        BufferedWriter writer = null;
        
        // Création d'un dialog de choix de fichier
        FileDialog fDial = new FileDialog(Main.appFrame, "Ouvrir", FileDialog.LOAD);
        fDial.setVisible(true);
        
        // Ouverture et lecture du fichier
        String nom = fDial.getFile();
        if (nom != null) {
            
            // Ouverture du fichier
            File csvFile = new File(fDial.getDirectory() + System.getProperty("file.separator") + nom);
            
            // Préparation à la lecture
            try {
                reader = new BufferedReader(new FileReader(csvFile));
            } catch (Exception fex) {
                System.out.println("Le fichier n'existe pas");
                fex.printStackTrace();
            }

            // Préparaton à l'écriture dans un journal d'erreur
            try {
                writer = new BufferedWriter(new FileWriter(fDial.getDirectory() + System.getProperty("file.separator") + "error.log"));
            } catch (IOException ioex) {
                System.out.println("Erreur ecriture log");
                ioex.printStackTrace();
            }
            
            // Lecture du fichier, ligne après ligne
            String line = null;
            String value = null;
            int cpt = 0;
            int cptOk = 0;
            try {
                while ((line = reader.readLine()) != null) {
                    // Recherche de l'ISBN
                    value = line.substring(0, line.indexOf(";"));
                    // Traitement de l'ISBN
                    value = db.CodeBarre.removeTiret(value);
                    
                    // ISBN valide ?
                    if (((value.length() == 10) && (db.CodeBarre.isCodeEAN10(value)))
                            || (db.CodeBarre.isCodeEAN13(value))) 
                    {
                        // Création d'une requête SQL
                        getResultQuery = SearchQuery.insertISBN(value, "N", "N", "N");
                        try {
                            // Execution de la requête
                            nbEntree = FrameMain.db.update(getResultQuery);
                            if (nbEntree != 0) {
                                cptOk++;
                            } else {
                                writer.write("Edition / ISBN num. non trouve : " + value + " (l." + cpt + ")\n");
                                System.out.println("Erreur Edition ISBN num. : " + value + " (l." + cpt + ")");
                            }
                        } catch (Exception fe) {
                            System.out.println("Code ISBN déjà present dans la base : " + value + " (l." + cpt + ")");
                        }
                    } else {
                        System.out.println("Erreur code ISBN num. : " + cpt);
                    }
                    cpt++;
                }
                writer.write("ISBN insérer : " + cptOk + " / " + cpt);
                writer.close();
                reader.close();
            } catch (Exception fe) {
                System.out.println("Erreur " + fe.toString());
            }
            System.out.println("ISBN ok : " + cptOk + " / " + cpt);
        }
    }
}
