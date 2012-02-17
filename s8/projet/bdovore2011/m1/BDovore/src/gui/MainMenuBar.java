package gui;

import gui.action.ActionAbout;
import gui.action.ActionConfig;
import gui.action.ActionDBExplorer;
import gui.action.ActionExit;
import gui.action.ActionHelp;
import gui.action.ActionStatistic;
import gui.action.ActionSynch;
import gui.action.ActionUpdate;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MainMenuBar extends JMenuBar{
	private static final long serialVersionUID = 1L;
	
	private JMenu menuBDovore, menuAbout;
	private JMenuItem itemDBExplorer, itemStatistic, itemUpdate, itemSynch, itemConfig, itemExit;
	private JMenuItem itemHelp, itemAbout;
	
	public MainMenuBar(){
		
		super();
		
		// Créer le menu "BDovore"
		menuBDovore = new JMenu("Mon BDovore");
		menuBDovore.setMnemonic(KeyEvent.VK_B);
		
		itemDBExplorer	= new JMenuItem(new ActionDBExplorer(
				"Explorer/Rechercher des BDs", 
				new ImageIcon("img/explorer.png"), 
				KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK),
				null));
		itemStatistic		= new JMenuItem(new ActionStatistic(
				"Statistiques",
				new ImageIcon("img/statistic.png"),
				KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK),
				null));
		itemUpdate		= new JMenuItem(new ActionUpdate(
				"Mettre à jour la base de données", 
				new ImageIcon("img/update.png"), 
				KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK),
				null));
		itemSynch		= new JMenuItem(new ActionSynch(
				"Récupérer mon menu BDovore en ligne",
				new ImageIcon("img/synch.png"),
				null,
				null));
		itemConfig		= new JMenuItem(new ActionConfig(
				"Configurations...",
				new ImageIcon("img/config.png"),
				KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK),
				null));
		itemExit		= new JMenuItem(new ActionExit(
				"Quitter",
				new ImageIcon("img/quit.png"),
				null,
				null));
		
		menuBDovore.add(itemDBExplorer);
		menuBDovore.add(itemStatistic);
		menuBDovore.addSeparator();
		menuBDovore.add(itemUpdate);
		menuBDovore.add(itemSynch);
		menuBDovore.addSeparator();
		menuBDovore.add(itemConfig);
		menuBDovore.addSeparator();
		menuBDovore.add(itemExit);
		
		// Créer le menu "Help"
		menuAbout = new JMenu("?");
		
		itemHelp	= new JMenuItem(new ActionHelp("Aide", new ImageIcon("img/help.png"), null, null));
		itemAbout	= new JMenuItem(new ActionAbout("À propos", new ImageIcon("img/about.png"), null, null));
		
		menuAbout.add(itemHelp);
		menuAbout.add(itemAbout);
		
		// Ajouter tous les menus à la barre de menu
		add(menuBDovore);
		add(menuAbout);
	}
}
