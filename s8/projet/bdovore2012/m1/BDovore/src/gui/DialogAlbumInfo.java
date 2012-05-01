package gui;

import db.data.Album;
import db.data.Auteur;
import db.data.Edition;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import main.Config;
import main.Main;

/**
 * Fenêtre affichant les informations sur un album
 */
public class DialogAlbumInfo extends JDialog {

    private static final long serialVersionUID = 1L;
    // Les libellés de formulaire
    private JLabel labTitle, labSerial, labTome, labGenre, labScenarist, labDrawer, labColorist;
    private JLabel labEdition, labIdentifier, labISBN10, labISBN13, labState;
    private JLabel labCover;
    // Les champs de texte
    private JLabel txtTitle, txtTome, txtGenre, txtSerial, txtISBN10, txtISBN13;
    private JLabel txtScenarist1, txtScenarist2, txtDrawer1, txtDrawer2, txtColorist1,
            txtColorist2;
    private JRadioButton radObtained, radToBuy, radNone;
    private JCheckBox chkDedicated, chkLentOut;
    private JList lstScenar, lstDessin, lstColor;
    private JComboBox lstEdition;
    private JButton btnClose;
    // Les conteneurs des composants Swing
    private JPanel formPane, imagePane, ctrlPane;
    private Album crtAlbum;
    private Edition crtEdition;
    private ArrayList<Auteur> allScenarists, allDrawers, allColorists;
    private ArrayList<Edition> allEditions;

    /**
     * Constructeur
     *
     * @param owner Fenêtre parente
     * @param modal
     * @param album Album à afficher
     */
    public DialogAlbumInfo(Window owner, Dialog.ModalityType modal, Album album) {
        super(owner, modal);

        crtAlbum = album;

        // Récupération des données dans la base locale
        try {
            FrameMain.db.fillAlbum(crtAlbum, FrameMain.synch);
            allScenarists = crtAlbum.getScenaristes();
            allDrawers = crtAlbum.getDessinateurs();
            allColorists = crtAlbum.getColoristes();
            allEditions = crtAlbum.getEditions();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Impossible de charger les auteurs et les édition",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        createGUI();

        pack();
        setResizable(false);
        setTitle(crtAlbum.getTitre());
        setLocationRelativeTo(owner);
    }

    /**
     * Création de l'interface
     */
    private void createGUI() {
        initComponents();
        layoutComponents();
        affectEventListeners();
    }

    /**
     * Initialisation des composants de l'interface
     */
    private void initComponents() {
        int right = SwingConstants.RIGHT;

        labTitle = new JLabel("Titre :", right);
        labSerial = new JLabel("Série :", right);
        labTome = new JLabel("Tome :", right);
        labGenre = new JLabel("Genre :", right);
        labScenarist = new JLabel("Scénariste :", right);
        labDrawer = new JLabel("Dessinateur :", right);
        labColorist = new JLabel("Coloriste :", right);
        labEdition = new JLabel("Edition :", right);
        labIdentifier = new JLabel("Identifiants :", right);
        labISBN10 = new JLabel("ISBN-10 :", right);
        labISBN13 = new JLabel("ISBN-13 :", right);
        labState = new JLabel("Etat :", right);

        labCover = new JLabel();

        txtTitle = new JLabel(crtAlbum.getTitre());
        txtSerial = new JLabel(crtAlbum.getSerie());
        txtTome = new JLabel(crtAlbum.getNumTome().toString());
        txtGenre = new JLabel(crtAlbum.getGenre());
        txtISBN10 = new JLabel(crtAlbum.getDefaultISBN());
        txtISBN13 = new JLabel();

        // Affichage des artistes (scénaristes, dessinateurs et coloristes)
        lstScenar = new JList(allScenarists.toArray());
        lstScenar.setBackground(null);
        lstDessin = new JList(allDrawers.toArray());
        lstDessin.setBackground(null);
        lstColor = new JList(allColorists.toArray());
        lstColor.setBackground(null);

        // Liste déroulante contenant toutes les éditions de l'album.
        String[] editorNames = new String[allEditions.size()];
        for (int i = 0; i < allEditions.size(); i++) {
            Date dte = allEditions.get(i).getParution();
            editorNames[i] = allEditions.get(i).getEditeur() + " ("
                    + ((dte == null) ? "date inconnue" : dte) + ")";
        }

        lstEdition = new JComboBox(editorNames);
        for (int i = 0; i < allEditions.size(); i++) {
            if (allEditions.get(i).isPossede()) {
                lstEdition.setSelectedIndex(i);
                break;
            }
        }

        Edition editionSelected = allEditions.get(lstEdition.getSelectedIndex());

        // Affichage des ISBN
        txtISBN10 = new JLabel(editionSelected.getISBN());
        txtISBN13 = new JLabel(editionSelected.getEAN());

        // Détails de l'album
        radObtained = new JRadioButton("Possédé");
        radToBuy = new JRadioButton("A acheter");
        radNone = new JRadioButton("Non possédé");
        ButtonGroup grp = new ButtonGroup();
        grp.add(radObtained);
        grp.add(radToBuy);
        grp.add(radNone);

        chkDedicated = new JCheckBox("Dédicacé");
        chkLentOut = new JCheckBox("Prêté");

        // Lire l'edition sélectionnée
        crtEdition = allEditions.get(lstEdition.getSelectedIndex());
        updateISBN();
        updateStateCheckbox();
        stateSelected();
        updateCoverImage();

        btnClose = new JButton("Fermer", new ImageIcon("img/close.png"));
    }

    /**
     * Mise en page des éléments
     */
    private void layoutComponents() {
        // Positionner les composants sur l'interface.
        // On utilise le GroupLayout pour le formulaire.
        formPane = new JPanel();
        formPane.setBorder(BorderFactory.createTitledBorder("Informations Générales"));
        GroupLayout lo = new GroupLayout(formPane);
        formPane.setLayout(lo);

        lo.setAutoCreateGaps(true);
        // lo.setAutoCreateContainerGaps(true);

        // Positionnement horizontal des composants
        GroupLayout.Alignment align = GroupLayout.Alignment.LEADING;
        lo.setHorizontalGroup(lo.createSequentialGroup()
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(labTitle)
                    .addComponent(labSerial)
                    .addComponent(labTome)
                    .addComponent(labGenre)
                    .addComponent(labScenarist)
                    .addComponent(labDrawer)
                    .addComponent(labColorist)
                    .addComponent(labEdition)
                    .addComponent(labIdentifier)
                    .addComponent(labState))
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(txtTitle)
                    .addComponent(txtSerial)
                    .addComponent(txtTome)
                    .addComponent(txtGenre)
                    .addComponent(lstScenar)
                    .addComponent(lstDessin)
                    .addComponent(lstColor)
                    .addComponent(lstEdition)
                .addGroup(lo.createSequentialGroup()
                    .addComponent(labISBN10)
                    .addComponent(txtISBN10))
                .addGroup(lo.createSequentialGroup()
                    .addComponent(labISBN13)
                    .addComponent(txtISBN13))
                .addGroup(lo.createSequentialGroup()
                    .addComponent(radObtained)
                    .addComponent(chkDedicated)
                    .addComponent(chkLentOut))
                    .addComponent(radToBuy)
                    .addComponent(radNone)));

        // Positionnment vertical des composants
        align = GroupLayout.Alignment.BASELINE;
        lo.setVerticalGroup(lo.createSequentialGroup()
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(labTitle)
                    .addComponent(txtTitle))
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(labSerial)
                    .addComponent(txtSerial))
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(labTome)
                    .addComponent(txtTome))
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(labGenre)
                    .addComponent(txtGenre))
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(labScenarist)
                .addGroup(lo.createSequentialGroup()
                    .addComponent(lstScenar)))
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(labDrawer)
                .addGroup(lo.createSequentialGroup()
                    .addComponent(lstDessin)))
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(labColorist)
                .addGroup(lo.createSequentialGroup()
                    .addComponent(lstColor)))
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(labEdition)
                    .addComponent(lstEdition))
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(labIdentifier)
                .addGroup(lo.createSequentialGroup()
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(labISBN10)
                    .addComponent(txtISBN10))
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(labISBN13)
                    .addComponent(txtISBN13))))
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(labState)
                .addGroup(lo.createSequentialGroup()
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(radObtained)
                    .addComponent(chkDedicated)
                    .addComponent(chkLentOut))
                    .addComponent(radToBuy)
                    .addComponent(radNone))));


        // Fixer les tailles des composants
        lo.linkSize(labTitle, labSerial, labTome, labGenre, labEdition, labScenarist, labDrawer,
                labColorist, labIdentifier, labState);
        lo.linkSize(txtTitle, txtSerial, txtTome, txtGenre, lstEdition, lstScenar, lstDessin, lstColor);
       
        // Positionner l'image de couverture
        imagePane = new JPanel(new FlowLayout(SwingConstants.TOP));
        imagePane.add(labCover);

        // Positonner le bouton de fermeture
        ctrlPane = new JPanel(new FlowLayout());
        ctrlPane.add(btnClose);

        // Positionner les conteneurs
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(formPane, BorderLayout.EAST);
        contentPane.add(imagePane, BorderLayout.WEST);
        contentPane.add(ctrlPane, BorderLayout.SOUTH);
        setContentPane(contentPane);
    }

    /**
     * Mise en place des Listeners
     */
    private void affectEventListeners() {
        // Lorqu'on sélectionne une autre édition dans la liste déroulante,
        // - Mettre à jour les états de l'édition précédente,
        // - Enregistrer le nouvelle édition dans la variable crtEdition,
        // - Actualiser les codes ISBN, les boutons radio d'état et l'image de
        // couverture.
        lstEdition.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                updateEditionStates();
                crtEdition = allEditions.get(lstEdition.getSelectedIndex());
                updateISBN();
                updateStateCheckbox();
                updateCoverImage();
                pack();
            }
        });

        // Mettre à jour les états de l'édition en cliquant le bouton de
        // fermeture.
        btnClose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateEditionStates();
                Main.appFrame.refreshExplorer();
                dispose();
            }
        });

        // Mettre à jour les états de l'édition en fermant le dialogue.
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                updateEditionStates();
                Main.appFrame.refreshExplorer();
            }
        });

        // Curseur de souris sur le nom de l'auteur, mettre le nom en BLEU.
        // Cliquer sur le nom de l'auteur, afficher la fiche de l'auteur.
        lstScenar.addMouseListener(new HighLightMouseListener());
        lstDessin.addMouseListener(new HighLightMouseListener());
        lstColor.addMouseListener(new HighLightMouseListener());

        lstScenar.addMouseListener(
                new MouseAdapter() {

                    public void mouseClicked(MouseEvent ev) {
                        try {
                            Component c = (Component) ev.getSource();
                            Window owner = SwingUtilities.getWindowAncestor(c);
                            if (lstScenar.isSelectedIndex(lstScenar.getSelectedIndex())) {
                                DialogArtistInfo dialogAuteur = new DialogArtistInfo(owner,
                                        Dialog.ModalityType.APPLICATION_MODAL,
                                        (Auteur) lstScenar.getSelectedValue());
                                dialogAuteur.setVisible(true);
                                lstScenar.clearSelection();
                            }
                        } catch (ClassCastException ex) {
                        }
                    }
                });

        lstDessin.addMouseListener(
                new MouseAdapter() {

                    public void mouseClicked(MouseEvent ev) {
                        try {
                            Component c = (Component) ev.getSource();
                            Window owner = SwingUtilities.getWindowAncestor(c);
                            if (lstDessin.isSelectedIndex(lstDessin.getSelectedIndex())) {
                                DialogArtistInfo dialogAuteur = new DialogArtistInfo(owner,
                                        Dialog.ModalityType.APPLICATION_MODAL,
                                        (Auteur) lstDessin.getSelectedValue());
                                dialogAuteur.setVisible(true);
                                lstDessin.clearSelection();
                            }
                        } catch (ClassCastException ex) {
                        }
                    }
                });

        lstColor.addMouseListener(
                new MouseAdapter() {

                    public void mouseClicked(MouseEvent ev) {
                        try {
                            Component c = (Component) ev.getSource();
                            Window owner = SwingUtilities.getWindowAncestor(c);
                            if (lstColor.isSelectedIndex(lstColor.getSelectedIndex())) {
                                DialogArtistInfo dialogAuteur = new DialogArtistInfo(owner,
                                        Dialog.ModalityType.APPLICATION_MODAL,
                                        (Auteur) lstColor.getSelectedValue());
                                dialogAuteur.setVisible(true);
                                lstColor.clearSelection();
                            }
                        } catch (ClassCastException ex) {
                        }
                    }
                });

        // Curseur de souris sur le nom de la série, mettre le nom en BLEU.
        // Cliquer sur le nom de série, afficher la fiche de série.
        txtSerial.addMouseListener(new HighLightMouseListener());
        txtSerial.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent ev) {
                try {
                    Component c = (Component) ev.getSource();
                    Window owner = SwingUtilities.getWindowAncestor(c);
                    int albumID = crtAlbum.getId();
                    DialogSerialInfo dialogSerial = new DialogSerialInfo(owner,
                            Dialog.ModalityType.APPLICATION_MODAL,
                            FrameMain.db.getSerie(albumID));
                    dialogSerial.setVisible(true);
                } catch (ClassCastException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Si l'état de l'édition est "possédée", activer les cases "Dedicacé"
        // et "Prêt",
        // sinon, désactiver les 2 cases.
        radObtained.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                stateSelected();
            }
        });
        radToBuy.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                stateSelected();
            }
        });
        radNone.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                stateSelected();
            }
        });
    }

    /**
     * Mise à jour des états de l'édition courante
     */
    private void updateEditionStates() {

        // Si l'édition était NON_POSSEDEE, et on a choisi POSSEDEE ou A
        // ACHETER,
        // le type de mise à jour de l'édition est INSERT.
        if ((!crtEdition.isPossede() && !crtEdition.isAAcheter())
                && (radObtained.isSelected() || radToBuy.isSelected())) {

            crtEdition.setUpdate(Edition.INSERT);
            crtEdition.setPossede(radObtained.isSelected());
            crtEdition.setAAcheter(radToBuy.isSelected());
            crtEdition.setDedicace(chkDedicated.isSelected());
            crtEdition.setPret(chkLentOut.isSelected());


        } 
        
        // Si l'édition était POSSEDEE ou A ACHETER, et on a choisi NON
        // POSSEDEE,
        // le type de mise à jour de l'édition est DELETE.
        else if ((crtEdition.isPossede() || crtEdition.isAAcheter()) && radNone.isSelected()) {

            crtEdition.setUpdate(Edition.DELETE);
            crtEdition.setPossede(false);
            crtEdition.setAAcheter(false);
            crtEdition.setDedicace(false);
            crtEdition.setPret(false);


        } 
        
        // Si l'édition était POSSEDEE et on a choisi A ACHETER,
        // le type de mise à jour de l'édition est UPDATE
        else if (crtEdition.isPossede() && radToBuy.isSelected()) {
            crtEdition.setUpdate(Edition.UPDATE);
            crtEdition.setPossede(false);
            crtEdition.setAAcheter(true);
            crtEdition.setDedicace(false);
            crtEdition.setPret(false);
        } 
        
        // Si l'édition était A ACHETER et on a choisi POSSEDEE,
        // le type de mise à jour de l'édition est UPDATE
        else if (crtEdition.isAAcheter() && radObtained.isSelected()) {
            crtEdition.setUpdate(Edition.UPDATE);
            crtEdition.setPossede(true);
            crtEdition.setAAcheter(false);
            crtEdition.setDedicace(chkDedicated.isSelected());
            crtEdition.setPret(chkLentOut.isSelected());
        } 
        
        // Si l'édition était POSSEDEE et on a modificé DEDICACE ou PRET,
        // le type de mise à jour de l'édition est UPDATE.
        else if ((crtEdition.isPossede() && radObtained.isSelected())
                && (crtEdition.isDedicace() != chkDedicated.isSelected() || crtEdition.isPret() != chkLentOut.isSelected())) {
            crtEdition.setUpdate(Edition.UPDATE);
            crtEdition.setPossede(true);
            crtEdition.setAAcheter(false);
            crtEdition.setDedicace(chkDedicated.isSelected());
            crtEdition.setPret(chkLentOut.isSelected());
        } 
        
        // Sinon, c'est DO_NOTHING.
        else {
            crtEdition.setUpdate(Edition.DO_NOTHING);
        }   

        try {
            FrameMain.db.updateUserData(crtEdition, FrameMain.synch);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Unable to update user data: edition", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }


    }

    /**
     * Activation des checkbox "Dédicacé" et "Prêté" en fonction de l'état "possédé" de l'album
     */
    private void stateSelected() {
        if (radObtained.isSelected()) {
            chkDedicated.setEnabled(true);
            chkLentOut.setEnabled(true);
        } else {
            chkDedicated.setEnabled(false);
            chkLentOut.setEnabled(false);
        }
    }

    /**
     * Mise à jour de l'ISBN
     */
    private void updateISBN() {
        String ISBN = null;
        String EAN = null;
        ISBN = crtEdition.getISBN();
        EAN = crtEdition.getEAN();
        txtISBN10.setText(ISBN);
        txtISBN13.setText(EAN);
    }

    /**
     * Mise à jour des boutons radio et des checkbox
     */
    private void updateStateCheckbox() {
        radObtained.setSelected(crtEdition.isPossede());
        radToBuy.setSelected(crtEdition.isAAcheter());
        radNone.setSelected(!crtEdition.isPossede() & !crtEdition.isAAcheter());

        chkDedicated.setSelected(crtEdition.isPossede() & crtEdition.isDedicace());
        chkLentOut.setSelected(crtEdition.isPossede() & crtEdition.isPret());
    }

    /**
     * Mise à jour de l'image de couverture de l'album
     */
    private void updateCoverImage() 
    {
        String cover = null;
        ImageIcon image = null;
        cover = crtEdition.getCouvertureURL();

        if (cover == null) 
        {
            try
            {
                FrameMain.synch.updateEdition(crtEdition.getId());
                FrameMain.db.fillAlbum(crtAlbum, FrameMain.synch);
                allEditions = crtAlbum.getEditions();
                crtEdition = allEditions.get(lstEdition.getSelectedIndex());
                cover = crtEdition.getCouverture();
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }
        

        // Utilisation d'un MediaTracker pour contrôler l'avancée du 
        // téléchargement de l'image (et gérer le timeout)
        MediaTracker tracker = new MediaTracker(this);

        // Vérification si image en cache
        File imageCache = new File(Config.COUV_PATH + cover);

        // Présence de l'image en cache ?
        if (imageCache.exists()) 
        {
            // Oui, on prend cette image
            System.out.println("Image en cache : " + cover);
            image = new ImageIcon(Config.COUV_PATH + cover);
        }
        else 
        {
            // Non, on la télécharge
            System.out.println("Pas de cache : " + Config.COUV_PATH + cover);
            if (cover != null)
            {
                try 
                {
                    image = new ImageIcon(new URL(Config.IMG_COUV_URL + cover));
                    tracker.addImage(image.getImage(), 0);
                } 
                catch (MalformedURLException ex) 
                {
                    JOptionPane.showMessageDialog(this, "URL malformé : " + cover, "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }

            try 
            {
                // Attent de 5 secondes
                tracker.waitForAll(5000); 
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }

            // Mise en cache
            if (image != null) 
            {
                System.out.println("Mise en cache");
                saveImage(image, cover);
            }
        }

        // Image chargée?
        if (image != null && image.getImageLoadStatus() == MediaTracker.COMPLETE) 
        {
            // Oui : Ajout dans l'IHM
            ImageIcon scaledImage = new ImageIcon(image.getImage().getScaledInstance(
                    Config.COUV_WIDTH, Config.COUV_HEIGHT, Image.SCALE_DEFAULT));
            labCover.setIcon(scaledImage);
        } 
        else 
        {
            // Non : Mise en place d'une image "défaut"
            image = new ImageIcon("img/noimg.jpg");
            if (image != null) 
            {
                labCover.setIcon(image);
            } 
            else 
            {
                System.err.println("noimg.jpg n'éxiste pas");
            }
        }
    }

    /**
     * Evenement agissant sur la couleur du composant "écouté" au survol de la souris
     */
    public static class HighLightMouseListener extends MouseAdapter {

        /**
         * Evenement lorsque la souris entre sur la zone du composant.
         * On effectue setForeground(Color.BLUE)
         * @param ev Détails de l'évenement
         */
        @Override
        public void mouseEntered(MouseEvent ev) {
            try {
                Component c = (Component) ev.getSource();
                c.setCursor(new Cursor(Cursor.HAND_CURSOR));
                c.setForeground(Color.BLUE);
            } catch (ClassCastException e) {
            }
        }

        /**
         * Evenement lorsque la souris sort de la zone du composant.
         * On effectue setForeGround(Color.Black)
         * @param ev Détails de l'évenement
         */
        @Override
        public void mouseExited(MouseEvent ev) {
            try {
                Component c = (Component) ev.getSource();
                c.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                c.setForeground(Color.BLACK);
            } catch (ClassCastException e) {
            }
        }
    }

    /**
     * Sauvegarde une image dans le dossier "covers"
     * @param imageIcon L'image
     * @param filename Le nom de l'image
     */
    private static void saveImage(ImageIcon imageIcon, String filename) {
        Image image = imageIcon.getImage();
        RenderedImage rendered = null;


        // Si le dossier "covers" n'existe pas, on le crée
        File dir = new File(Config.COUV_PATH);
        if (!dir.exists()) {
            dir.mkdir();
        }

        // Si l'image est "prête" on la stocke
        // Sinon on la prépare et on la stocke
        if (image instanceof RenderedImage) 
        {
            rendered = (RenderedImage) image;
        } 
        else {
            BufferedImage buffered = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = buffered.createGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
            rendered = buffered;
        }

        // On récupère l'extension
        String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

        // Création du fichier
        try {
            ImageIO.write(rendered, ext, new File(Config.COUV_PATH + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
