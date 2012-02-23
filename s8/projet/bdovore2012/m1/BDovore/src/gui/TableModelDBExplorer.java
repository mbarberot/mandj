package gui;

import db.data.Album;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * Tableau des résultats des recherches.
 *
 */
public class TableModelDBExplorer extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    // Les albums 
    private ArrayList<Album> records;
    // 
    private Object[][] data;
    // Les noms des colonnes
    private String[] columnNames = {"Titre", "Tome n°", "Série", "Genre", "ISBN-13", "ISBN-10"};

    /**
     * Constructeur
     *
     * @param rec Liste des albums à afficher
     */
    public TableModelDBExplorer(ArrayList<Album> rec) {
        
        records = rec;
        
        // Nombre de lignes
        int numOfRows = records.size();
        
        // Remplissage des lignes
        Album album = null;
        data = new Object[numOfRows][columnNames.length];
        for (int row = 0; row < numOfRows; row++) 
        {    
            album = records.get(row);
            data[row][0] = album.getTitre();
            data[row][1] = album.getNumTome();
            data[row][2] = album.getSerie();
            data[row][3] = album.getGenre();
            data[row][4] = album.getDefaultEAN();
            data[row][5] = album.getDefaultISBN();
        }
    }

    /**
     * Retourne l'album de la ligne spécifiée
     * @param selectedRow Le numero d'une ligne du tableau
     * @return L'album pour la ligne donnée
     */
    public Album getSelectedAlbum(int selectedRow) {
        return records.get(selectedRow);
    }

    /**
     * Retourne le nombre de colonnes du tableau
     * @return Le nombre de colonnes du tableau
     */
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Retourne le nombre de lignes du tableau
     * @return Le nombre de lignes du tableau
     */
    public int getRowCount() {
        return records.size();
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
     * Retourne la valeur de la cellule spécifiée
     * @param row Un numéro de ligne du tableau
     * @param col Un numéro de colonne du tableau
     * @return La valeur de cette case
     */
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    /**
     * Retourne la classe des valeur de la colonne spécifiée
     * @param column Un numéro de colonne du tableau
     * @return La classe des éléments de cette colonne
     */
    public Class<?> getColumnClass(int column) {
        if (column == 1) {
            return Integer.class;
        }
        return String.class;
    }
}
