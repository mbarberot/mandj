package gui;

import db.data.Edition;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import util.UpdateBDUserListener;
import util.UpdateBaseListener;

/**
 * Element d'interface graphique permettant l'affichage des conflits lors de la
 * synchronisation du logiciel avec la base de données distante. Elle permet
 * également le choix de l'action à effectuer pour régler ces conflits.
 *
 */
public class TableModelSynchConflict extends AbstractTableModel implements UpdateBDUserListener
{
    private static final long serialVersionUID = 1L;
    
    String[] columnNames = { "Serveur", "Local", "Rien", "Titre", "Série", "No. Tome", "Etat Serveur", "Etat Local"};
    ArrayList<Object[]> datas;

    public TableModelSynchConflict()
    {
        datas = new ArrayList<Object[]>();
    }

    public int getColumnCount()
    {
        return columnNames.length;
    }

    public String getColumnName(int col)
    {
        return columnNames[col];
    }

    public int getRowCount()
    {
        return datas.size();
    }

    public Object getValueAt(int row, int col)
    {
        return datas.get(row)[col];
    }

    public Class<?> getColumnClass(int col)
    {
        if (col >= 0 && col <= 2)
        {
            return Boolean.class;
        }
        return String.class;
    }

    public Object[][] getDatas()
    {
        Object o[][] = new Object[datas.size()][columnNames.length];
        int i = 0;
        for(Object b[] : datas)
        {
            o[i] = b;
            i++;
        }
        return o;
    }
    
    public ArrayList<Object[]> getConflicts()
    {
        return datas;
    }

    public boolean isCellEditable(int row, int col)
    {
        if (col == 6 || col == 7)
        {
            return true;
        }
        return false;
    }

    public void addRow(Object[] row)
    {
        datas.add(row);
    }
}
