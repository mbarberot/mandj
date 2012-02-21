package gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Proxy;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import db.update.Updater;

public class DialogGlobalUpdate extends JDialog{

	private static final long serialVersionUID = 1L;
	
	private JLabel updateMsg;
	private JButton btnStart;
	private Proxy proxy;
	private Updater updater;

	public DialogGlobalUpdate(Window owner, Dialog.ModalityType modal, Proxy p, Updater up){
		super(owner, modal);

		proxy = p;
		updater = up;
		updateMsg = new JLabel("Cliquer le bouton START pour commencer.");
		
		btnStart = new JButton("Start", new ImageIcon("img/restore.png"));
		btnStart.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				update();
			}
		});
		
		JPanel ctrlPane = new JPanel(new FlowLayout());
		ctrlPane.add(btnStart);
		
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(updateMsg, BorderLayout.CENTER);
		contentPane.add(ctrlPane, BorderLayout.SOUTH);
		setContentPane(contentPane);
		
		setTitle("Mise à jour globale");
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
	}
	
	private void update(){
		try{
			updater.updateGlobal();
			JOptionPane.showMessageDialog(null, "Mise à jour terminée avec succès", "Félicitation", JOptionPane.INFORMATION_MESSAGE);
			dispose();
		}catch(Exception ex){
			JOptionPane.showMessageDialog(
					null, 
					"Vérifier votre connexion réseau ou " +
					"le serveur est momentanément indisponible", 
					"Mise à jour impossible", 
					JOptionPane.ERROR_MESSAGE);
			dispose();
		}
	}
}
