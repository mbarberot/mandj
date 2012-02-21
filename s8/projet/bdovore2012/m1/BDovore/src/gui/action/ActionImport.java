package gui.action;

import gui.FrameMain;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import db.SearchQuery;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

import main.Main;

public class ActionImport extends AbstractAction{
	
	private static final long serialVersionUID = 1L;
	
	public ActionImport(String text, Icon icon, KeyStroke accelerator, String desc){
		super();
		
		putValue(Action.SMALL_ICON, icon);
		putValue(Action.LARGE_ICON_KEY, icon);
		putValue(Action.NAME, text);
		putValue(Action.SHORT_DESCRIPTION, desc);
		putValue(Action.ACCELERATOR_KEY, accelerator);
	}
	
	public void actionPerformed(ActionEvent e){
		String getResultQuery = null;
//		ArrayList<Album> result = null;
//		Album album = null;
//		Edition crtEdition = null;
		int nbEntree = 0;
		BufferedReader reader = null;
		BufferedWriter writer = null;
		FileDialog fDial=new FileDialog(Main.appFrame, "Ouvrir", FileDialog.LOAD);
		fDial.setVisible(true);
        String nom=fDial.getFile();
        if (nom!=null) {
        	File csvFile = new File(fDial.getDirectory()+ System.getProperty("file.separator")+nom); 
        	try {	
        		reader = new BufferedReader(new FileReader(csvFile)); 
        	} catch (Exception fe) {System.out.println("Le fichier n'existe pas"); }
        	
        	try {
				writer = new BufferedWriter(new FileWriter(fDial.getDirectory()+ System.getProperty("file.separator")+"error.log"));
			} catch (IOException e1) {
				System.out.println("Erreur ecriture log"); ;
			}
        	String line = null;
        	String value = null;
        	int cpt = 0;
        	int cptOk = 0;
        	try {
        		while ((line = reader.readLine()) != null) {
        			value = line.substring(0,line.indexOf(";"));
        			value = db.CodeBarre.removeTiret(value);
        			if (((value.length() == 10) && (db.CodeBarre.isCodeEAN10(value)))
        					|| (db.CodeBarre.isCodeEAN13(value))) {
        				getResultQuery = SearchQuery.insertISBN(value, "N", "N", "N");
        				try {
        				 nbEntree = FrameMain.db.update(getResultQuery);
        				 if (nbEntree != 0) cptOk++;
        				 else {
        					writer.write("Edition / ISBN num. non trouve : "+value+" (l."+cpt+")\n"); 
        					System.out.println("Erreur Edition ISBN num. : "+value+" (l."+cpt+")");
        				 }
//        				getResultQuery = SearchQuery.searchISBN(SearchQuery.SEARCH_IN_ALL, value, SearchQuery.GET_FIELDS, "t.TITRE", SearchQuery.ORDER_ASC);
//        				try {
//        					result = FrameMain.db.search(getResultQuery, 2, 0);
//       						album = result.get(0);
//       						FrameMain.db.fillAlbum(album);
//       						crtEdition = album.getEditions().get(0);
//       						if (crtEdition != null) {
//       							cptOk++;
//     							crtEdition.setUpdate(Edition.INSERT);
//     							crtEdition.setPossede(true);
//     							crtEdition.setAAcheter(false);
//     							crtEdition.setDedicace(false);
//     							crtEdition.setPret(false);
//     							FrameMain.db.updateUserData(crtEdition);
//       						} else System.out.println("Erreur Edition ISBN num. : "+cpt);	
        				}
        				catch (Exception fe) {System.out.println("code ISBN déja present dans la base : "+value+" (l."+cpt+")"); }
        			}
        			else System.out.println("Erreur code ISBN num. : "+cpt);
        			cpt++;
        		}
        		writer.write("ISBN insérer : "+cptOk+" / "+cpt);
        		writer.close();
        		reader.close();
        	}
        	catch (Exception fe) {System.out.println("Erreur "+fe.toString()); }
        	System.out.println("ISBN ok : "+cptOk+" / "+cpt);
        }
	}
}
