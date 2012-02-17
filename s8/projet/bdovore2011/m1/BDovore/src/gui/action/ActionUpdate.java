package gui.action;

import gui.DialogGlobalUpdate;
import gui.FrameMain;

import java.awt.Dialog;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import main.Main;

public class ActionUpdate extends AbstractAction{
	
	private static final long serialVersionUID = 1L;

	public ActionUpdate(String text, Icon icon, KeyStroke accelerator, String desc){
		super();
		
		putValue(Action.SMALL_ICON, icon);
		putValue(Action.LARGE_ICON_KEY, icon);
		putValue(Action.NAME, text);
		putValue(Action.ACCELERATOR_KEY, accelerator);
		putValue(Action.SHORT_DESCRIPTION, desc);
	}
	
	public void actionPerformed(ActionEvent e){		
		DialogGlobalUpdate dialog = new DialogGlobalUpdate(Main.appFrame, Dialog.ModalityType.APPLICATION_MODAL, FrameMain.currentProxy);
		dialog.setVisible(true);
	}
}
