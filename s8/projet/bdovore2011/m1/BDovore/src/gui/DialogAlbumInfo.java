package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker; //import java.awt.MediaTracker;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import main.Config;
import main.Main;
import db.data.Album;
import db.data.Auteur;
import db.data.Edition;

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
	
	private JComboBox lstEdition;
	private JButton btnClose;
	
	// Les conteneurs des composants Swing
	private JPanel formPane, imagePane, ctrlPane;
	
	private Album crtAlbum;
	private Edition crtEdition;
	private ArrayList<Auteur> allScenarists, allDrawers, allColorists;
	private ArrayList<Edition> allEditions;
	
	public DialogAlbumInfo(Window owner, Dialog.ModalityType modal, Album album) {
		super(owner, modal);
		crtAlbum = album;
		try {
			FrameMain.db.fillAlbum(crtAlbum);
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
	
	private void createGUI() {
		initComponents();
		layoutComponents();
		affectEventListeners();
	}
	
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
		labState = new JLabel("État :", right);
		
		labCover = new JLabel();
		
		txtTitle = new JLabel(crtAlbum.getTitre());
		txtSerial = new JLabel(crtAlbum.getSerie());
		txtTome = new JLabel(crtAlbum.getNumTome().toString());
		txtGenre = new JLabel(crtAlbum.getGenre());
		txtISBN10 = new JLabel();
		txtISBN13 = new JLabel();
		
		// Afficher les artistes, pour l'instant, on n'affiche que 2 artistes
		// par catégorie,
		// ce n'est pas encore supporté par la base de données du système.
		// Dans la prochaine version, on pourra mettre un JComboBox à la place
		// de JLabel.
		txtScenarist1 = new JLabel();
		txtScenarist2 = new JLabel();
		txtDrawer1 = new JLabel();
		txtDrawer2 = new JLabel();
		txtColorist1 = new JLabel();
		txtColorist2 = new JLabel();
		
		// Les scénaristes
		if (allScenarists.size() >= 1)
			txtScenarist1.setText(allScenarists.get(0).getPrenom() + " "
					+ allScenarists.get(0).getNom());
		if (allScenarists.size() == 2)
			txtScenarist2.setText(allScenarists.get(1).getPrenom() + " "
					+ allScenarists.get(1).getNom());
		
		// Les dessinateurs
		if (allDrawers.size() >= 1)
			txtDrawer1.setText(allDrawers.get(0).getPrenom() + " " + allDrawers.get(0).getNom());
		if (allDrawers.size() == 2)
			txtDrawer2.setText(allDrawers.get(1).getPrenom() + " " + allDrawers.get(1).getNom());
		
		// Les coloristes
		if (allColorists.size() >= 1)
			txtColorist1.setText(allColorists.get(0).getPrenom() + " "
					+ allColorists.get(0).getNom());
		if (allColorists.size() == 2)
			txtColorist2.setText(allColorists.get(1).getPrenom() + " "
					+ allColorists.get(1).getNom());
		
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
		
		radObtained = new JRadioButton("Possédée");
		radToBuy = new JRadioButton("À acheter");
		radNone = new JRadioButton("Non possédée");
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
		lo.setHorizontalGroup(lo.createSequentialGroup().addGroup(
				lo.createParallelGroup(align).addComponent(labTitle).addComponent(labSerial)
						.addComponent(labTome).addComponent(labGenre).addComponent(labScenarist)
						.addComponent(labDrawer).addComponent(labColorist).addComponent(labEdition)
						.addComponent(labIdentifier).addComponent(labState)).addGroup(
				lo.createParallelGroup(align).addComponent(txtTitle).addComponent(txtSerial)
						.addComponent(txtTome).addComponent(txtGenre).addComponent(txtScenarist1)
						.addComponent(txtScenarist2).addComponent(txtDrawer1).addComponent(
								txtDrawer2).addComponent(txtColorist1).addComponent(txtColorist2)
						.addComponent(lstEdition).addGroup(
								lo.createSequentialGroup().addComponent(labISBN10).addComponent(
										txtISBN10)).addGroup(
								lo.createSequentialGroup().addComponent(labISBN13).addComponent(
										txtISBN13)).addGroup(
								lo.createSequentialGroup().addComponent(radObtained).addComponent(
										chkDedicated).addComponent(chkLentOut)).addComponent(
								radToBuy).addComponent(radNone)));
		
		// Positionnment vertical des composants
		align = GroupLayout.Alignment.BASELINE;
		lo
				.setVerticalGroup(lo.createSequentialGroup()
						.addGroup(
								lo.createParallelGroup(align).addComponent(labTitle).addComponent(
										txtTitle)).addGroup(
								lo.createParallelGroup(align).addComponent(labSerial).addComponent(
										txtSerial)).addGroup(
								lo.createParallelGroup(align).addComponent(labTome).addComponent(
										txtTome)).addGroup(
								lo.createParallelGroup(align).addComponent(labGenre).addComponent(
										txtGenre)).addGroup(
								lo.createParallelGroup(align).addComponent(labScenarist).addGroup(
										lo.createSequentialGroup().addComponent(txtScenarist1)
												.addComponent(txtScenarist2))).addGroup(
								lo.createParallelGroup(align).addComponent(labDrawer).addGroup(
										lo.createSequentialGroup().addComponent(txtDrawer1)
												.addComponent(txtDrawer2))).addGroup(
								lo.createParallelGroup(align).addComponent(labColorist).addGroup(
										lo.createSequentialGroup().addComponent(txtColorist1)
												.addComponent(txtColorist2))).addGroup(
								lo.createParallelGroup(align).addComponent(labEdition)
										.addComponent(lstEdition)).addGroup(
								lo.createParallelGroup(align).addComponent(labIdentifier)
										.addGroup(
												lo.createSequentialGroup().addGroup(
														lo.createParallelGroup(align).addComponent(
																labISBN10).addComponent(txtISBN10))
														.addGroup(
																lo.createParallelGroup(align)
																		.addComponent(labISBN13)
																		.addComponent(txtISBN13))))
						.addGroup(
								lo.createParallelGroup(align).addComponent(labState).addGroup(
										lo.createSequentialGroup().addGroup(
												lo.createParallelGroup(align).addComponent(
														radObtained).addComponent(chkDedicated)
														.addComponent(chkLentOut)).addComponent(
												radToBuy).addComponent(radNone))));
		

		// Fixer les tailles des composants
		lo.linkSize(labTitle, labSerial, labTome, labGenre, labEdition, labScenarist, labDrawer,
				labColorist, labIdentifier, labState);
		lo.linkSize(txtTitle, txtSerial, txtTome, txtGenre, lstEdition, txtScenarist1,
				txtScenarist2, txtDrawer1, txtDrawer2, txtColorist1, txtColorist2);
		// lo.linkSize(radToBuy, radDedicated, radLentOut);
		
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
		
		// Curseur de souris sur le nom de la série, mettre le nom en BLEU.
		// Cliquer sur le nom de série, afficher la fiche de série.
		txtSerial.addMouseListener(new HighLightMouseListener());
		txtSerial.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent ev) {
			// try{
			// Component c = (Component)ev.getSource();
			// Window owner = SwingUtilities.getWindowAncestor(c);
			// int serialID = crtAlbum.getIdSerie().intValue();
			// DialogSerialInfo dialogSerial = new DialogSerialInfo(owner,
			// Dialog.ModalityType.APPLICATION_MODAL,
			// FrameMain.db.getSerie(serialID));
			// dialogSerial.setVisible(true);
			// }catch(ClassCastException ex){
			//					
			// }catch(SQLException ex){
			//					
			// }
			}
		});
		
		// Curseur de souris sur les noms des artistes, mettre les noms en BLEU.
		// FICHE ARTISTE N'EST PAS ENCORE FAITE
		txtScenarist1.addMouseListener(new HighLightMouseListener());
		txtScenarist2.addMouseListener(new HighLightMouseListener());
		txtDrawer1.addMouseListener(new HighLightMouseListener());
		txtDrawer2.addMouseListener(new HighLightMouseListener());
		txtColorist1.addMouseListener(new HighLightMouseListener());
		txtColorist2.addMouseListener(new HighLightMouseListener());
		
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
	
	// Mettre à jour les états de l'édition courante
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
				&& (crtEdition.isDedicace() != chkDedicated.isSelected() || crtEdition.isPret() != chkLentOut
						.isSelected())) {
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
		
		// System.out.println("Update :" + crtEdition.getUpdate());
		// System.out.println("Possédé :" + crtEdition.isPossede());
		// System.out.println("Acheter :" + crtEdition.isAAcheter());
		// System.out.println("Dédicacé :" + crtEdition.isDedicace());
		// System.out.println("Prêt :" + crtEdition.isPret());
		
		try {
			FrameMain.db.updateUserData(crtEdition);
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Unable to update user data: edition", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// Si
	private void stateSelected() {
		if (radObtained.isSelected()) {
			chkDedicated.setEnabled(true);
			chkLentOut.setEnabled(true);
		}
		else {
			chkDedicated.setEnabled(false);
			chkLentOut.setEnabled(false);
		}
	}
	
	private void updateISBN() {
		String ISBN = null;
		String EAN = null;
		ISBN = crtEdition.getISBN();
		EAN = crtEdition.getEAN();
		txtISBN10.setText(ISBN);
		txtISBN13.setText(EAN);
	}
	
	private void updateStateCheckbox() {
		radObtained.setSelected(crtEdition.isPossede());
		radToBuy.setSelected(crtEdition.isAAcheter());
		radNone.setSelected(!crtEdition.isPossede() & !crtEdition.isAAcheter());
		
		chkDedicated.setSelected(crtEdition.isPossede() & crtEdition.isDedicace());
		chkLentOut.setSelected(crtEdition.isPossede() & crtEdition.isPret());
	}
	
	private void updateCoverImage() {
		String coverURL = null;
		ImageIcon image = null;
		coverURL = crtEdition.getCouvertureURL();
		MediaTracker tracker = new MediaTracker(this);
		
		File test = new File(Config.COUV_PATH);
		if (test.exists()) System.out.println("OK !");
		else System.out.println("Non.");
		
		// Vérification si image en cache
		File imageCache = new File(Config.COUV_PATH + crtEdition.getCouverture());
		
		if (imageCache.exists()) {
			//System.out.println("Image en cache : " + crtEdition.getCouverture());
			image = new ImageIcon(Config.COUV_PATH + crtEdition.getCouverture());
		}
		
		
		// Pas de cache : téléchargement
		else {
			//System.out.println("Pas de cache pour " + crtEdition.getCouverture());
			if (coverURL != null) {
				try {
					image = new ImageIcon(new URL(coverURL));
					tracker.addImage(image.getImage(), 0);
				} catch (MalformedURLException ex) {
					JOptionPane.showMessageDialog(this, "URL malformé : " + coverURL, "Erreur",
							JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
			
			try {
				tracker.waitForAll(5000); // Attend max 5 secondes
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
			// Mise en cache (à refaire sous certaines conditions ?)
			//if (image != null)
			//	saveImage(image, crtEdition.getCouverture());
		}
		
		
		
		
		if (image != null && image.getImageLoadStatus() == MediaTracker.COMPLETE) {
			ImageIcon scaledImage = new ImageIcon(image.getImage().getScaledInstance(
					Config.COUV_WIDTH, Config.COUV_HEIGHT, Image.SCALE_DEFAULT));
			labCover.setIcon(scaledImage);
		}
		
		else {
			image = new ImageIcon("img/noimg.jpg");
			if (image != null)
				labCover.setIcon(image);
			else System.err.println("noimg.jpg n'éxiste pas");
		}
	}
	
	public static class HighLightMouseListener extends MouseAdapter {
		
		public void mouseEntered(MouseEvent ev) {
			try {
				Component c = (Component) ev.getSource();
				c.setCursor(new Cursor(Cursor.HAND_CURSOR));
				c.setForeground(Color.BLUE);
			} catch (ClassCastException e) {}
		}
		
		public void mouseExited(MouseEvent ev) {
			try {
				Component c = (Component) ev.getSource();
				c.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				c.setForeground(Color.BLACK);
			} catch (ClassCastException e) {}
		}
	}
	
	

	private static void saveImage(ImageIcon imageIcon, String filename) {
		Image image = imageIcon.getImage();
		RenderedImage rendered = null;
		
		
		// Recréé "covers" si besoin
		File dir = new File(Config.COUV_PATH);
		if (!dir.exists()) dir.mkdir();
		
		
		if (image instanceof RenderedImage) {
			rendered = (RenderedImage) image;
		}
		
		else {
			BufferedImage buffered = new BufferedImage(imageIcon.getIconWidth(), imageIcon
					.getIconHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D g = buffered.createGraphics();
			g.drawImage(image, 0, 0, null);
			g.dispose();
			rendered = buffered;
		}
		
		String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
		
		try {
			ImageIO.write(rendered, ext, new File(Config.COUV_PATH + filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
