package gui;

import db.data.Serie;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import javax.swing.*;

/**
 * Affiche les informations d'une série
 */
public class DialogSerialInfo extends JDialog {

    private static final long serialVersionUID = 1L;
    
    // Les éléments de l'IHM
    private JLabel labSerial, labGenre, labNbTomes, labAlbumsInDB, labProgress, labSynopsis;
    private JLabel txtSerial, txtGenre, txtNbTomes, txtAlbumsInDB, txtProgress;
    private JTextArea txtSynopsis;
    private JButton btnClose;
    private Serie crtSerie;
    private String[] stateSerie = {"Non connu", "En cours", "Finie", "Interrompue"};

    /**
     * Constructeur
     * @param owner Fenêtre parente
     * @param modal
     * @param serial Série
     */
    public DialogSerialInfo(Window owner, Dialog.ModalityType modal, Serie serial) {
        super(owner, modal);
        crtSerie = serial;
        
        // Récupérations des informations sur la série
        try {
            FrameMain.db.fillSerie(crtSerie);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Impossible de charger la série",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        
        createGUI();
        pack();
        setResizable(false);
        setTitle(crtSerie.getNom());
        setLocationRelativeTo(owner);
    }

    /**
     * Création de l'IHM
     */
    private void createGUI() {
        
        int right = SwingConstants.RIGHT;
        labSerial = new JLabel("S�rie:", right);
        labGenre = new JLabel("Genre:", right);
        labNbTomes = new JLabel("Nombre de tomes:", right);
        labAlbumsInDB = new JLabel("Albums dans la base:", right);
        labProgress = new JLabel("Avancement:", right);
        labSynopsis = new JLabel("Synopsis:");


        txtSerial = new JLabel(crtSerie.getNom());
        txtGenre = new JLabel(crtSerie.getGenre());
        txtNbTomes = new JLabel("" + crtSerie.getNbTomes());
        txtAlbumsInDB = new JLabel("" + FrameMain.db.getAlbumInSerieUser(crtSerie.getId()));

        txtProgress = new JLabel(stateSerie[crtSerie.getFlg_fini()]);

        txtSynopsis = new JTextArea();
        txtSynopsis.setEnabled(false);
        txtSynopsis.setLineWrap(true);
        txtSynopsis.setWrapStyleWord(true);

        txtSynopsis.setText(crtSerie.getHistoire());


        JScrollPane scrollPane = new JScrollPane(txtSynopsis);
        scrollPane.setPreferredSize(new Dimension(300, 100));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        /*
         * Créer le panneau d'info de la série et définir le gestionnaire de
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
                layout.createSequentialGroup().addGroup(layout.createParallelGroup(alignment).addGroup(layout.createSequentialGroup().addComponent(labSerial).addComponent(txtSerial)).addGroup(layout.createSequentialGroup().addComponent(labGenre).addComponent(txtGenre)).addGroup(layout.createSequentialGroup().addComponent(labNbTomes).addComponent(txtNbTomes)).addGroup(layout.createSequentialGroup().addComponent(labAlbumsInDB).addComponent(txtAlbumsInDB)).addGroup(layout.createSequentialGroup().addComponent(labProgress).addComponent(txtProgress)).addComponent(labSynopsis).addComponent(scrollPane)));

        alignment = GroupLayout.Alignment.BASELINE;
        layout.setVerticalGroup(
                layout.createSequentialGroup().addGroup(layout.createParallelGroup(alignment).addComponent(labSerial).addComponent(txtSerial)).addGroup(layout.createParallelGroup(alignment).addComponent(labGenre).addComponent(txtGenre)).addGroup(layout.createParallelGroup(alignment).addComponent(labNbTomes).addComponent(txtNbTomes)).addGroup(layout.createParallelGroup(alignment).addComponent(labAlbumsInDB).addComponent(txtAlbumsInDB)).addGroup(layout.createParallelGroup(alignment).addComponent(labProgress).addComponent(txtProgress)).addComponent(labSynopsis).addComponent(scrollPane));

        layout.linkSize(labSerial, labGenre, labNbTomes, labAlbumsInDB, labProgress, labSynopsis);
        layout.linkSize(txtSerial, txtGenre, txtNbTomes, txtAlbumsInDB, txtProgress);

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
