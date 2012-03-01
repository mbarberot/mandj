package gui;

import javax.swing.table.AbstractTableModel;

/**
 * Element d'interface graphique permettant l'affichage des conflits lors de la synchronisation du logiciel avec la base de données distante.
 * Elle permet également le choix de l'action à effectuer pour régler ces conflits.
 * 
 */
public class TableModelSynchConflict extends AbstractTableModel{
	
	private static final long serialVersionUID = 1L;
	
	String[] columnNames = {"Serveur", "Local", "Rien", "Titre", "Série", "No. Tome", "Etat Serveur", "Etat Local"};
	Object[][] datas = {
			{new Boolean(false), new Boolean(false), new Boolean(true), "T1", "S1", "T1", "ES1", "EL1"},
			{new Boolean(false), new Boolean(false), new Boolean(true), "T2", "S2", "T2", "ES2", "EL2"},
			{new Boolean(false), new Boolean(false), new Boolean(true), "T3", "S3", "T3", "ES3", "EL3"}
	};
	
	public TableModelSynchConflict(){
		//TODO: Quand la méthode qui renvoie la liste de conflits est finie,
		//il faudra ajouter le résultat retourné en paramètre.
	}
	
	public int getColumnCount() {
		return columnNames.length;
	}
	
	public String getColumnName(int col){
		return columnNames[col];
	}

	public int getRowCount() {
		return datas.length;
	}

	public Object getValueAt(int row, int col) {
		return datas[row][col];
	}
	
	public Class<?> getColumnClass(int col){
		if(col >= 0 && col <= 2)
			return Boolean.class;
		return String.class;
	}
	
	public Object[][] getDatas(){
		return datas;
	}
	
	public boolean isCellEditable(int row, int col){
		if(col == 6 || col == 7)
			return true;
		return false;
	}
}
