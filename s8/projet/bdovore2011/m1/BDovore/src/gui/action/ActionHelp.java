package gui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;

public class ActionHelp extends AbstractAction{
	
	private static final long serialVersionUID = 1L;
	
	public ActionHelp(String text, Icon icon, KeyStroke accelerator, String desc){
		super();
		
		putValue(Action.SMALL_ICON, icon);
		putValue(Action.LARGE_ICON_KEY, icon);
		putValue(Action.NAME, text);
		putValue(Action.SHORT_DESCRIPTION, desc);
		putValue(Action.ACCELERATOR_KEY, accelerator);
	}
	public void actionPerformed(ActionEvent e){
		
	}
}
