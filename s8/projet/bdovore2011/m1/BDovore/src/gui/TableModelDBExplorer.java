package gui;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import db.data.Album;

public class TableModelDBExplorer extends AbstractTableModel{
	
	private static final long serialVersionUID = 1L;
	private ArrayList<Album> records; 	
	private Object[][] data;
	private String[] columnNames = {"Titre", "Tome n°", "Série", "Genre", "ISBN-13", "ISBN-10"};
	
	public TableModelDBExplorer(ArrayList<Album> rec){
		records = rec;
		
		int numOfRows = records.size();
		Album album = null;
		data = new Object[numOfRows][columnNames.length];
		for(int row = 0; row < numOfRows; row ++){
			album = records.get(row);
			
			data[row][0] = album.getTitre();
			data[row][1] = album.getNumTome();
			data[row][2] = album.getSerie();
			data[row][3] = album.getGenre();
			data[row][4] = album.getDefaultEAN();
			data[row][5] = album.getDefaultISBN();
		}
	}
	
	public Album getSelectedAlbum(int selectedRow){
		return records.get(selectedRow);
	}
	
    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return records.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
   		return data[row][col];
    }
    
    public Class<?> getColumnClass(int column){
    	if(column == 1)
    		return Integer.class;
    	return String.class;
    }
}
