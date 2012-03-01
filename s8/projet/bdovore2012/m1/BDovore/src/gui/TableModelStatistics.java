package gui;

import db.data.StatisticsRepartition;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * Affiche les des statistiques dans un tableau
 */
public class TableModelStatistics extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    // Les statistiques à afficher
    private ArrayList<StatisticsRepartition> records;
    // Le nom de chaque colonne du tableau
    private String[] columnNames = null;
    // Les données du tableau
    private Object[][] data = null;

    /**
     * Constructeur
     * 
     * @param cn Nom des colonnes
     * @param rec Données du tableau
     */
    public TableModelStatistics(String[] cn, ArrayList<StatisticsRepartition> rec) {
        
        columnNames = cn;
        records = rec;
        data = new Object[records.size()][3];

        // Affichage des statistiques
        StatisticsRepartition s = null;
        for (int i = 0; i < records.size(); i++) {
            s = records.get(i);
            data[i][0] = s.getNom();
            data[i][1] = s.getNombre();
            data[i][2] = s.getRatio();
        }
    }

    /**
     * Retourne le nombre de colonnes du tableau
     * @return Le nombre de colonnes du tableau
     */
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Retourne le nombre de ligne du tableau 
     * @return Le nombre de ligne du tableau
     */
    public int getRowCount() {
        return data.length;
    }

    /**
     * Retourne le nom de la colonne spécifiée
     * @param col Le numéro d'une colonne du tableau
     * @return Le nom de cette colonne
     */
    public String getColumnName(int col) {
        return columnNames[col];
    }

    /**
     * Retourne la valeur d'une case du tableau
     * @param row Numéro de la ligne
     * @param col Numéro de la colonne
     * @return La valeur de la case
     */
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    /**
     * Retourne la classe des objets d'une colonne donnée
     * @param column Le numéro d'une colonne du tableau
     * @return La classe des objets de cette colonne
     */
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 1:
                return Integer.class;
            case 2:
                return Integer.class;
            default:
                return String.class;
        }
    }
}