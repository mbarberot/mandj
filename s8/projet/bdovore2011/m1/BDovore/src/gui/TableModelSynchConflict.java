package gui;

import javax.swing.table.AbstractTableModel;

public class TableModelSynchConflict extends AbstractTableModel{
	
	private static final long serialVersionUID = 1L;
	
	String[] columnNames = {"S", "L", "R", "Titre", "Série", "No. Tome", "Etat Serveur", "Etat Local"};
	Object[][] datas = {
			{new Boolean(false), new Boolean(false), new Boolean(true), "T1", "S1", "T1", "Whether you are setting the editor for a single column of cells (using the TableColumn setCellEditor method) or for a specific type of data (using the JTable setDefaultEditor method), you specify the editor using an argument that adheres to the TableCellEditor interface. Fortunately, the DefaultCellEditor class implements this interface and provides constructors to let you specify an editing component that is a JTextField, JCheckBox, or JComboBox. Usually you do not have to explicitly specify a check box as an editor, since columns with Boolean data automatically use a check box renderer and editor.", "EL1"},
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
