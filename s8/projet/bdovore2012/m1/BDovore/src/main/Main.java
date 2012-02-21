package main;
import gui.FrameMain;

public class Main {
	/*
	 * Le référence de la fenêtre principale est stocké 
	 * dans une constante public & statique.
	 * Elle est initialisée lors du chargement de l'application et
	 * accessible depuis les autres objets.
	 */
	public static final FrameMain appFrame = new FrameMain();
		
	public static void main(String[] args){
		Main.appFrame.setVisible(true);
	}
}

// C:/Users/Pierre/workspace/BDovore_2/db/bdovore

// C:/Users/Pierre/Desktop/Mes documents/Cours/S6/project/bdovore bak/db/bdovore