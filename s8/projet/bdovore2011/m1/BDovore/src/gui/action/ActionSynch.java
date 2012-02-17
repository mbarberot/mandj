package gui.action;

import gui.DialogUserSynchronization;

import java.awt.Dialog;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import main.Main;

public class ActionSynch extends AbstractAction{
	
	private static final long serialVersionUID = 1L;
	
	public ActionSynch(String text, Icon icon, KeyStroke accelerator, String desc){
		super();
		
		putValue(Action.SMALL_ICON, icon);
		putValue(Action.LARGE_ICON_KEY, icon);
		putValue(Action.NAME, text);
		putValue(Action.ACCELERATOR_KEY, accelerator);
		putValue(Action.SHORT_DESCRIPTION, desc);
	}

	public void actionPerformed(ActionEvent e){
		//TODO:
		//Synchronisation avec le compte BDovore,
		//et si on détecte des conflits, on affichera le dialog de conflig pour les résoudre manuellement.
		DialogUserSynchronization dialog = new DialogUserSynchronization(Main.appFrame, Dialog.ModalityType.APPLICATION_MODAL);
		dialog.setVisible(true);
	}
}
