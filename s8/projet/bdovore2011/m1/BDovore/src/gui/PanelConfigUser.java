package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import main.Config;

import db.data.User;

public class PanelConfigUser extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private JLabel labUsername, labUserpwd;
	private JTextField txtUsername;
	private JPasswordField txtUserpwd;
	private JButton btnApply, btnRestore;
	
	private Properties config;
	private BufferedOutputStream ostream;
	
	public PanelConfigUser(Properties config){
		super(new BorderLayout());
		
		this.config = config;
		
		createGUI();
		affectEventListeners();
	}
	
	private void createGUI(){
		labUsername	= new JLabel("Nom d'utilisateur BDovore :");
		labUserpwd	= new JLabel("Mot de passe :");
		
		txtUsername	= new JTextField(20);
		txtUserpwd	= new JPasswordField(20);
		
		txtUsername.setText(config.getProperty("username"));
		txtUserpwd.setText(config.getProperty("userpwd"));
		
		JPanel formPane = new JPanel();
		GroupLayout lo = new GroupLayout(formPane);
		formPane.setLayout(lo);
		
		lo.setAutoCreateGaps(true);
		lo.setAutoCreateContainerGaps(true);
		
		GroupLayout.Alignment align = GroupLayout.Alignment.LEADING;
		lo.setHorizontalGroup(lo.createSequentialGroup()
			.addGroup(lo.createParallelGroup(align)
				.addComponent(labUsername)
				.addComponent(labUserpwd))
			.addGroup(lo.createParallelGroup(align)
				.addComponent(txtUsername)
				.addComponent(txtUserpwd)));
		
		align = GroupLayout.Alignment.BASELINE;
		lo.setVerticalGroup(lo.createSequentialGroup()
			.addGroup(lo.createParallelGroup(align)
				.addComponent(labUsername)
				.addComponent(txtUsername))
			.addGroup(lo.createParallelGroup(align)
				.addComponent(labUserpwd)
				.addComponent(txtUserpwd)));
		
		JPanel ctrlPane = new JPanel(new FlowLayout());
		
		btnApply	= new JButton("Appliquer", new ImageIcon("img/apply.png"));
		btnRestore	= new JButton("Restaurer", new ImageIcon("img/restore.png"));
		btnApply.setMnemonic(KeyEvent.VK_A);
		btnRestore.setMnemonic(KeyEvent.VK_R);
		
		ctrlPane.add(btnApply);
		ctrlPane.add(btnRestore);
		
		add(formPane, BorderLayout.CENTER);
		add(ctrlPane, BorderLayout.SOUTH);
	}
	
	private void affectEventListeners(){
		btnApply.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				saveConfig();
			}
		});
		
		btnRestore.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				txtUsername.setText(config.getProperty("username"));
				txtUserpwd.setText(config.getProperty("userpwd"));
			}
		});
	}
	
	private void saveConfig(){
		User user = new User(txtUsername.getText(), new String(txtUserpwd.getPassword()));
		if(!user.isValid()){
			JOptionPane.showMessageDialog(
					null, 
					"Nom d'utilisateur ou le mot de passe incorrect\n" +
					"ou problème de connexion réseau", 
					"Utilisateur invalid", 
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		config.setProperty("username", txtUsername.getText());
		config.setProperty("userpwd", new String(txtUserpwd.getPassword()));
		
		try{
			ostream = new BufferedOutputStream(new FileOutputStream(Config.CONFIG_FILENAME));
			config.storeToXML(ostream, null);
			ostream.close();
			JOptionPane.showMessageDialog(null, "Compte utilisateur enregistré", null, JOptionPane.INFORMATION_MESSAGE);
		}catch(IOException ex){
			JOptionPane.showMessageDialog(null, "IOException", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
