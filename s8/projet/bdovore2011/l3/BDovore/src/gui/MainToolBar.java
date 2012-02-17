package gui;

import gui.action.ActionDBExplorer;
import gui.action.ActionExit;
import gui.action.ActionStatistic;
import gui.action.ActionSynch;
import gui.action.ActionUpdate;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

public class MainToolBar extends JToolBar{

	private static final long serialVersionUID = 1L;
	private JButton btnDBExplorer, btnStatistic, btnSynch, btnUpdate;
	private JButton btnQuit;
	
	public MainToolBar(){
		super("BDovore");
		
		btnDBExplorer = new JButton(new ActionDBExplorer(
				null, 
				new ImageIcon("img/explorer.png"), 
				null, 
				"Explorer/Rechercher des BDs"));
		btnStatistic = new JButton(new ActionStatistic(
				null,
				new ImageIcon("img/statistic.png"), 
				null,
				"Les statistiques de mes collections"));
		
		btnUpdate = new JButton(new ActionUpdate(
				null, 
				new ImageIcon("img/update.png"), 
				null, 
				"Mettre à jour la base de données"));
		btnSynch = new JButton(new ActionSynch(
				null,
				new ImageIcon("img/synch.png"),
				null,
				"Récupérer mon menu BDovore en ligne"));
		btnQuit = new JButton(new ActionExit(
				null,
				new ImageIcon("img/quit.png"), 
				null,
				"Quitter"));
		
		add(btnDBExplorer);
		add(btnStatistic);
		add(btnUpdate);
		add(btnSynch);
		add(btnQuit);
	}	
}
