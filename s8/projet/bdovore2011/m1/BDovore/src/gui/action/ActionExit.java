package gui.action;

import gui.FrameMain;

import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

public class ActionExit extends AbstractAction{

	private static final long serialVersionUID = 1L;

	public ActionExit(String text, Icon icon, KeyStroke accelerator, String desc){
		super();
		
		putValue(Action.SMALL_ICON, icon);
		putValue(Action.LARGE_ICON_KEY, icon);
		putValue(Action.NAME, text);
		putValue(Action.ACCELERATOR_KEY, accelerator);
		putValue(Action.SHORT_DESCRIPTION, desc);
	}
	
	public void actionPerformed(ActionEvent e){
		// Shutdown the database
		try{
			FrameMain.db.shutdown();
		}catch(SQLException ex){
			JOptionPane.showMessageDialog(null,	"Impossible de se déconnecter de la base de donnéess", "Erreur", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
		System.out.println("DB Shutdown");

		System.exit(0);
	}
}
