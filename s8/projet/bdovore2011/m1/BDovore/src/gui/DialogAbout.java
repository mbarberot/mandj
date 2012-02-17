package gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class DialogAbout extends JDialog{

	private static final long serialVersionUID = 1L;
	
	private JLabel projectLogo;
	private JTextArea txtDevelopers;
	private JButton btnClose;
	
	public DialogAbout(Window owner, Dialog.ModalityType modal){
		super(owner, modal);
		
		projectLogo = new JLabel(new ImageIcon("img/logo.png"));
		JPanel logoPane = new JPanel(new FlowLayout());
		logoPane.add(projectLogo);
		
		txtDevelopers = new JTextArea();
		txtDevelopers.setEditable(false);
		JPanel devPane = new JPanel(new FlowLayout());
		devPane.add(txtDevelopers);
		
		txtDevelopers.setText("" +
				"= Projet BDovore =\n\n" +
				"http://www.bdovore.com & Université de Franche-Comté\n" +
				"Directeur de projet : Fabrice Bouquet\n" +
				"Base de données : Mathias Coqblin\n" +
				"Interface graphique : Yanbo Shou\n");
		
		btnClose = new JButton("Fermer", new ImageIcon("img/close.png"));
		btnClose.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				dispose();
			}
		});
		
		JPanel ctrlPane = new JPanel(new FlowLayout());
		ctrlPane.add(btnClose);
		
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(logoPane, BorderLayout.NORTH);
		contentPane.add(devPane, BorderLayout.CENTER);
		contentPane.add(ctrlPane, BorderLayout.SOUTH);
		setContentPane(contentPane);
		
		setTitle("À propos");
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
	}
}
