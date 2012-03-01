package gui;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellEditor;

/**
 * 
 */
public class CellEditorDescConflict extends AbstractCellEditor implements TableCellEditor{
	private static final long serialVersionUID = 1L;

        // Elements de l'IHM
	private JTextArea editor = new JTextArea();
        private JScrollPane scrollPane = new JScrollPane(editor);
	
        /**
         * Constructeur
         */
	public CellEditorDescConflict(){
		editor.setEditable(false);
		editor.setLineWrap(true);
		editor.setWrapStyleWord(true);
	}
	
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		editor.setText(String.valueOf(value));
		return scrollPane;
	}

        /**
         * Retourne la valeur de la cellule
         * @return null par défaut
         */
	public Object getCellEditorValue() {
		return null;
	}
	
        /**
         * Retourne si la cellule est éditable.<br />
         * C'est le cas si l'utilisateur à cliqué deux fois sur la cellule.
         * @param ev - Objet généré lors d'évenements
         * @return true si la cellule est éditable, false sinon.
         */
	public boolean isCellEditable(EventObject ev){
		if(ev instanceof MouseEvent){
			MouseEvent me = (MouseEvent)ev;
			if(me.getID() == MouseEvent.MOUSE_PRESSED && me.getClickCount() == 2){
				return true;
			}
		}
		return false;
	}
}
