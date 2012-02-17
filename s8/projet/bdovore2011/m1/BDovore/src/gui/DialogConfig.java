package gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import main.Config;

public class DialogConfig extends JDialog{
	
	private static final long serialVersionUID = 1L;
	
	private BufferedInputStream istream;
	private Properties config;
	
	private JTabbedPane tabbedPane;
	private PanelConfigProxy configProxyPane;
	private PanelConfigUser configUserPane;
	private JButton btnClose;
	
	public DialogConfig(Window owner, Dialog.ModalityType modal){
		super(owner, modal);
		
		loadConfig();
		createGUI();
		affectEventListeners();
		
		setTitle("Configurations");
		pack();
		setLocationRelativeTo(null);
	}
	
	private void loadConfig(){
		try{
			config = new Properties();
			istream = new BufferedInputStream(new FileInputStream(Config.CONFIG_FILENAME));
			config.loadFromXML(istream);
			istream.close();
		}catch(IOException ex){
			JOptionPane.showMessageDialog(null, "IOException", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void createGUI(){
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		configProxyPane	= new PanelConfigProxy(config);
		configUserPane	= new PanelConfigUser(config);
		
		tabbedPane.addTab("Serveur Proxy", new ImageIcon("img/proxy.png"), configProxyPane);
		tabbedPane.addTab("Compte BDovore", new ImageIcon("img/user.png"), configUserPane);
		
		btnClose	= new JButton("Fermer", new ImageIcon("img/close.png"));
		btnClose.setMnemonic(KeyEvent.VK_F);
		
		JPanel ctrlPane = new JPanel(new FlowLayout(SwingConstants.RIGHT));
		ctrlPane.add(btnClose);
		
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		contentPane.add(ctrlPane, BorderLayout.SOUTH);
		setContentPane(contentPane);
	}
	
	private void affectEventListeners(){
		btnClose.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				dispose();
			}
		});
	}
}
