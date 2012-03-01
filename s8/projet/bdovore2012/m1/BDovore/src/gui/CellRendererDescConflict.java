package gui;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

/**
 * Case du tableau des conlits
 */
public class CellRendererDescConflict extends JTextArea implements TableCellRenderer {

    private static final long serialVersionUID = 1L;

    /**
     * Constructeur
     */
    public CellRendererDescConflict() {
        super();
        setEditable(false);
        setLineWrap(true);
        setWrapStyleWord(true);
    }

   
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        setText(String.valueOf(value));
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getBackground());
        }
        return this;
    }
}
