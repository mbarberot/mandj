package gui;

import db.data.Auteur;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import javax.swing.*;

/**
 * Fenêtre affichant les informations sur un artiste
 */
public class DialogArtistInfo extends JDialog {
    private static final long serialVersionUID = 1L;
    
    // Elements de l'IHM
    private JLabel labNom, labPrenom, labDteNaissance, labDteDeces, labNationalite;
    private JLabel txtNom, txtPrenom, txtDteNaissance, txtDteDeces, txtNationalite;
    private JButton btnClose;
    
    // Auteur
    private Auteur crtAuteur;

    /**
     * Constructeur
     * @param owner Fenêtre parente
     * @param modal
     * @param author Auteur
     */
    public DialogArtistInfo(Window owner, Dialog.ModalityType modal, Auteur author) {
        super(owner, modal);
        
        crtAuteur = author;
        
        // Récupération des informations sur l'auteur
        try {
            FrameMain.db.fillAuteur(crtAuteur);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Impossible de charger les infos de l'auteur",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        
        createGUI();
        pack();
        setResizable(false);
        setTitle(crtAuteur.getPseudo());
        setLocationRelativeTo(owner);

    }

    /**
     * Création de l'IHM
     */
    private void createGUI() 
    {
        String str = "inconnue";
        int right = SwingConstants.RIGHT;
        labNom = new JLabel("Nom:", right);
        labPrenom = new JLabel("Prénom:", right);
        labDteNaissance = new JLabel("Date de naissance:", right);
        labDteDeces = new JLabel("Date de décès:", right);
        labNationalite = new JLabel("Nationalité:", right);


        txtNom = new JLabel(crtAuteur.getNom());
        txtPrenom = new JLabel(crtAuteur.getPrenom());

        // Vérification des dates (pour ne pas avoir d'exceptions lors de l'execution
        if (crtAuteur.getDteNaissance() != null) {
            str = crtAuteur.getDteNaissance().toString();
        }
        txtDteNaissance = new JLabel(str);

        // - idem -
        if (crtAuteur.getDteDeces() != null) {
            str = crtAuteur.getDteDeces().toString();
        }
        txtDteDeces = new JLabel(str);

        
        txtNationalite = new JLabel(crtAuteur.getNationalite());


        /*
         * Créer le panneau d'info de l'auteur et définir le gestionnaire de
         * positionnement de composants. Ici on utilise le gestionnaire
         * GroupLayout, qui est un gestionnaire adaptable pour créer des
         * formulaires de saisie. Pour avoir plus d'informations, lisez le
         * tutoriel de Sun:
         * http://java.sun.com/docs/books/tutorial/uiswing/layout/group.html
         */
        JPanel infoPane = new JPanel();
        GroupLayout layout = new GroupLayout(infoPane);
        infoPane.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);


        GroupLayout.Alignment alignment = GroupLayout.Alignment.LEADING;
        layout.setHorizontalGroup(
                layout.createSequentialGroup().addGroup(layout.createParallelGroup(alignment).addGroup(layout.createSequentialGroup().addComponent(labNom).addComponent(txtNom)).addGroup(layout.createSequentialGroup().addComponent(labPrenom).addComponent(txtPrenom)).addGroup(layout.createSequentialGroup().addComponent(labDteNaissance).addComponent(txtDteNaissance)).addGroup(layout.createSequentialGroup().addComponent(labDteDeces).addComponent(txtDteDeces)).addGroup(layout.createSequentialGroup().addComponent(labNationalite).addComponent(txtNationalite))));

        alignment = GroupLayout.Alignment.BASELINE;
        layout.setVerticalGroup(
                layout.createSequentialGroup().addGroup(layout.createParallelGroup(alignment).addComponent(labNom).addComponent(txtNom)).addGroup(layout.createParallelGroup(alignment).addComponent(labPrenom).addComponent(txtPrenom)).addGroup(layout.createParallelGroup(alignment).addComponent(labDteNaissance).addComponent(txtDteNaissance)).addGroup(layout.createParallelGroup(alignment).addComponent(labDteDeces).addComponent(txtDteDeces)).addGroup(layout.createParallelGroup(alignment).addComponent(labNationalite).addComponent(txtNationalite)));

        layout.linkSize(labNom, labPrenom, labDteNaissance, labDteDeces, labNationalite);
        layout.linkSize(txtNom, txtPrenom, txtDteNaissance, txtDteDeces, txtNationalite);

        btnClose = new JButton("Fermer");
        btnClose.setMnemonic(KeyEvent.VK_F);
        btnClose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                dispose();
            }
        });
        JPanel controlPane = new JPanel(new FlowLayout(SwingConstants.RIGHT));
        controlPane.add(btnClose);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(infoPane, BorderLayout.CENTER);
        contentPane.add(controlPane, BorderLayout.SOUTH);
        setContentPane(contentPane);
    }
}
