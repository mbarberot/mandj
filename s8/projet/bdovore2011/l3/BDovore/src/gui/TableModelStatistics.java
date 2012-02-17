package gui;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import db.data.StatisticsRepartition;

public class TableModelStatistics extends AbstractTableModel{
	
	private static final long serialVersionUID = 1L;
	
	private ArrayList<StatisticsRepartition> records;
	private String[] columnNames = null;
	private Object[][] data = null;
	
	public TableModelStatistics(String[] cn, ArrayList<StatisticsRepartition> rec){
		columnNames = cn;
		records = rec;
		
		data = new Object[records.size()][3];
		
		StatisticsRepartition s = null;
		for(int i = 0; i < records.size(); i ++){
			s = records.get(i);
			data[i][0] = s.getNom();
			data[i][1] = s.getNombre();
			data[i][2] = s.getRatio();
		}
	}
	
    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }
    
    public Class<?> getColumnClass(int column){
    	switch(column){
    	case 1: return Integer.class;
    	case 2: return Integer.class;
    	default: return String.class;
    	}
    }
}
