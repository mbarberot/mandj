package gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import db.data.Serie;

public class DialogSerialInfo extends JDialog{

	private static final long serialVersionUID = 1L;
	
	private JLabel labSerial, labGenre, labNbTomes, labAlbumsInDB, labProgress, labSynopsis;
	private JLabel txtSerial, txtGenre, txtNbTomes, txtAlbumsInDB, txtProgress;
	private JTextArea txtSynopsis;
	private JButton btnClose;
	
	public DialogSerialInfo(Window owner, Dialog.ModalityType modal, Serie serial){
		super(owner, modal);
		
		createGUI();
		pack();
		setResizable(false);
		setTitle("Le Dernier Templier");
		setLocationRelativeTo(owner);
	}
	
	private void createGUI(){
		int right = SwingConstants.RIGHT;
		labSerial		= new JLabel("Série:", right);
		labGenre		= new JLabel("Genre:", right);
		labNbTomes		= new JLabel("Nombre de tomes:", right);
		labAlbumsInDB	= new JLabel("Albums dans la base:", right);
		labProgress		= new JLabel("Avancement:", right);
		labSynopsis		= new JLabel("Synopsis:");
		
		// Demo data
		txtSerial		= new JLabel("Le dernier templier");
		txtGenre		= new JLabel("Aventure");
		txtNbTomes		= new JLabel("1");
		txtAlbumsInDB	= new JLabel("1");
		txtProgress		= new JLabel("En cours");
		
		txtSynopsis		= new JTextArea();
		txtSynopsis.setEnabled(false);
		txtSynopsis.setLineWrap(true);
		txtSynopsis.setWrapStyleWord(true);
		
		// Demo data, cette ligne sera supprimée quand la lecture de données sera implémentée.
		txtSynopsis.setText("New York, de nos jours. " +
				"Lors d’une exposition sur les trésors du Vatican, " +
				"quatre cavaliers habillés en Templier surgissent et dérobent un objet. " +
				"Cette disparition inquiète au plus haut point le Vatican qui comprend que " +
				"ce vol spectaculaire a un lien avec un secret qui remontre au XIIIème siècle... " +
				"Cet objet, récupéré par les Templiers, peut tout simplement remettre en question les fondements de la chrétienté ! " +
				"Un agent du FBI, associé à une jeune archéologue, part à la recherche de l’objet : une enquête de tous les dangers...");
		
		
		JScrollPane scrollPane = new JScrollPane(txtSynopsis);
		scrollPane.setPreferredSize(new Dimension(300, 100));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		/*
		 * Créer le panneau d'info de la série et 
		 * définir le gestionnaire de positionnement de composants.
		 * Ici on utilise le gestionnaire GroupLayout, 
		 * qui est un gestionnaire adaptable pour créer des formulaires de saisie.
		 * Pour avoir plus d'informations, lisez le tutoriel de Sun:
		 * http://java.sun.com/docs/books/tutorial/uiswing/layout/group.html
		 */
		JPanel infoPane = new JPanel();
		GroupLayout layout = new GroupLayout(infoPane);
		infoPane.setLayout(layout);
		
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		GroupLayout.Alignment alignment = GroupLayout.Alignment.LEADING;
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(alignment)
					.addGroup(layout.createSequentialGroup()
						.addComponent(labSerial)
						.addComponent(txtSerial))
					.addGroup(layout.createSequentialGroup()
						.addComponent(labGenre)
						.addComponent(txtGenre))
					.addGroup(layout.createSequentialGroup()
						.addComponent(labNbTomes)
						.addComponent(txtNbTomes))
					.addGroup(layout.createSequentialGroup()
						.addComponent(labAlbumsInDB)
						.addComponent(txtAlbumsInDB))
					.addGroup(layout.createSequentialGroup()
						.addComponent(labProgress)
						.addComponent(txtProgress))
					.addComponent(labSynopsis)
					.addComponent(scrollPane))
			);
		
		alignment = GroupLayout.Alignment.BASELINE;
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(alignment)
					.addComponent(labSerial)
					.addComponent(txtSerial))
				.addGroup(layout.createParallelGroup(alignment)
					.addComponent(labGenre)
					.addComponent(txtGenre))
				.addGroup(layout.createParallelGroup(alignment)
					.addComponent(labNbTomes)
					.addComponent(txtNbTomes))
				.addGroup(layout.createParallelGroup(alignment)
					.addComponent(labAlbumsInDB)
					.addComponent(txtAlbumsInDB))
				.addGroup(layout.createParallelGroup(alignment)
					.addComponent(labProgress)
					.addComponent(txtProgress))
				.addComponent(labSynopsis)
				.addComponent(scrollPane)
			);
		
		layout.linkSize(labSerial, labGenre, labNbTomes, labAlbumsInDB, labProgress, labSynopsis);
		layout.linkSize(txtSerial, txtGenre, txtNbTomes, txtAlbumsInDB, txtProgress);
		
		btnClose = new JButton("Fermer");
		btnClose.setMnemonic(KeyEvent.VK_F);
		btnClose.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
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
