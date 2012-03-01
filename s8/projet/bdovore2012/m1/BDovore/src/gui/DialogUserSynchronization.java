package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.TableColumn;

/**
 * Fenêtre de synchronisation entre la base de données locale et la base de données en ligne
 */
public class DialogUserSynchronization extends JDialog {
    private static final long serialVersionUID = 1L;
    
    private JLabel labInfo;
    private JTable tabConflict;
    private JButton btnChooseLocal, btnChooseServer, btnOk, btnCancel;

    /**
     * Constructeur
     * @param owner Fenetre parente
     * @param modal 
     */
    public DialogUserSynchronization(Window owner, Dialog.ModalityType modal) {
        super(owner, modal);

        createGUI();

        setTitle("Synchronisation avec mon compte BDovore");
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Création de l'IHM
     */
    private void createGUI() 
    {
        labInfo = new JLabel();
        labInfo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        labInfo.setText("Conflits détectés entre la base de données locale / serveur");

        tabConflict = new JTable(new TableModelSynchConflict());
        tabConflict.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tabConflict.getColumnModel().getColumn(6).setCellRenderer(new CellRendererDescConflict());
        tabConflict.getColumnModel().getColumn(7).setCellRenderer(new CellRendererDescConflict());

        tabConflict.getColumnModel().getColumn(6).setCellEditor(new CellEditorDescConflict());
        tabConflict.getColumnModel().getColumn(7).setCellEditor(new CellEditorDescConflict());

        //Définir les tailles des colonnes
        TableColumn column = null;
        for (int c = 0; c < tabConflict.getColumnCount(); c++) 
        {
            column = tabConflict.getColumnModel().getColumn(c);
            if (c >= 0 && c <= 2) 
            {
                column.setPreferredWidth(50);
                
            } 
            else if (c >= 3 && c <= 5) 
            {
                column.setPreferredWidth(100);
                
            } 
            else if (c >= 6 && c <= 8) 
            {
                column.setPreferredWidth(200);
            }
        }

        //Cliquer le tableau (les 3 première colonnes),
        //et mettre à jour la priorité de l'ouvrage.
        tabConflict.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent ev) {
                updateAlbumPriority(ev);
            }
        });
        tabConflict.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent ev) {
                enlargeSelectedRow(ev);
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabConflict);

        btnChooseLocal = new JButton("Choisir Local", new ImageIcon("img/local.png"));
        btnChooseServer = new JButton("Choisir Serveur", new ImageIcon("img/network.png"));
        btnOk = new JButton("Appliquer", new ImageIcon("img/apply.png"));
        btnCancel = new JButton("Annuler", new ImageIcon("img/close.png"));

        btnChooseLocal.setMnemonic(KeyEvent.VK_L);
        btnChooseServer.setMnemonic(KeyEvent.VK_S);
        btnChooseServer.setDisplayedMnemonicIndex(8);
        btnOk.setMnemonic(KeyEvent.VK_A);
        btnCancel.setMnemonic(KeyEvent.VK_N);

        btnChooseLocal.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                chooseLocalForAll();
            }
        });
        btnChooseServer.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                chooseServerForAll();
            }
        });

        JPanel ctrlPane = new JPanel(new FlowLayout());
        ctrlPane.add(btnChooseLocal);
        ctrlPane.add(btnChooseServer);
        ctrlPane.add(btnOk);
        ctrlPane.add(btnCancel);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(labInfo, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(ctrlPane, BorderLayout.SOUTH);
        setContentPane(contentPane);
    }

    /**
     * 
     * @param ev 
     */
    private void updateAlbumPriority(MouseEvent ev) {
        //TODO: Quand la méthode qui renvoie la liste de conflits est finie,
        //il faudra enlever la méthode getDatas() du modèle,
        //et on manipulera directement le résultat retourné par le moteur de bd.
        try {
            JTable source = (JTable) ev.getSource();
            int selectedRow = source.getSelectedRow();
            int selectedCol = source.getSelectedColumn();

            Object datas[][] = ((TableModelSynchConflict) source.getModel()).getDatas();
            //Les 3 premières colonnes
            if (selectedCol >= 0 && selectedCol <= 2) {
                datas[selectedRow][0] = new Boolean(false);
                datas[selectedRow][1] = new Boolean(false);
                datas[selectedRow][2] = new Boolean(false);
                datas[selectedRow][selectedCol] = new Boolean(true);
                source.repaint();
            }
        } catch (ClassCastException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Elargit une colonne
     * @param ev Détails de l'évenement
     */
    private void enlargeSelectedRow(MouseEvent ev) {
        try {
            JTable source = (JTable) ev.getSource();
            source.setRowHeight(source.getRowHeight());
            source.setRowHeight(source.getSelectedRow(), source.getRowHeight() * 5);

        } catch (ClassCastException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Choisit l'option qui garde toutes les modifications locales et les applique au compte BDovore en ligne
     */
    private void chooseLocalForAll() {
        //TODO: Quand la méthode qui renvoie la liste de conflits est finie,
        //il faudra enlever la méthode getDatas() du modèle,
        //et on manipulera directement le résultat retourné par le moteur de bd. 
        try {
            Object[][] datas = ((TableModelSynchConflict) tabConflict.getModel()).getDatas();
            for (int i = 0; i < datas.length; i++) {
                datas[i][0] = new Boolean(false);
                datas[i][1] = new Boolean(true);
                datas[i][2] = new Boolean(false);
            }
            tabConflict.repaint();
        } catch (ClassCastException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Choisit l'option qui garde toutes les modification sur le compte en ligne et les applique en local
     */
    private void chooseServerForAll() {
        //TODO: Quand la méthode qui renvoie la liste de conflits est finie,
        //il faudra enlever la méthode getDatas() du modèle,
        //et on manipulera directement le résultat retourné par le moteur de bd. 
        try {
            Object[][] datas = ((TableModelSynchConflict) tabConflict.getModel()).getDatas();
            for (int i = 0; i < datas.length; i++) {
                datas[i][0] = new Boolean(true);
                datas[i][1] = new Boolean(false);
                datas[i][2] = new Boolean(false);
            }
            tabConflict.repaint();
        } catch (ClassCastException ex) {
            ex.printStackTrace();
        }
    }
}
