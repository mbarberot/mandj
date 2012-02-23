package gui;

import db.data.Statistics;
import db.data.StatisticsRepartition;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Composant de l'interface pour l'affichage des statistiques.
 *
 */
public class PanelStatistics extends JPanel {

    private static final long serialVersionUID = 1L;
    
    // Composants à propos des albums (tous, possédés, à acheter) :
    // - Label
    private JLabel labNbCollect, labNbObtained, labNbToBuy;
    // - Quantité
    private JLabel txtNbCollect, txtNbObtained, txtNbToBuy;
    
    // Composants à propos des répartitions :
    // - Titres
    private JLabel labStatGenre, labStatEditor, labStatScenarist, labStatDrawer;
    // - Tableau
    private JTable tabStatGenre, tabStatEditor, tabStatScenarist, tabStatDrawer;

    /**
     * Constructeur.
     *
     */
    public PanelStatistics() {
        super(new BorderLayout());

        // Initialisation des composants
        int right = SwingConstants.RIGHT;
        labNbCollect = new JLabel("Albums dans ma collection :", right);
        labNbObtained = new JLabel("Albums possédés :", right);
        labNbToBuy = new JLabel("Albums à acheter :", right);

        txtNbCollect = new JLabel();
        txtNbObtained = new JLabel();
        txtNbToBuy = new JLabel();

        labStatGenre = new JLabel("Répartition par genres :");
        labStatEditor = new JLabel("Répartition par éditeurs :");
        labStatScenarist = new JLabel("Répartition par scénaristes :");
        labStatDrawer = new JLabel("Répartition par dessinateurs :");

        tabStatGenre = new JTable();
        tabStatEditor = new JTable();
        tabStatScenarist = new JTable();
        tabStatDrawer = new JTable();

        tabStatGenre.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabStatEditor.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabStatScenarist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabStatDrawer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tabStatGenre.setAutoCreateRowSorter(true);
        tabStatEditor.setAutoCreateRowSorter(true);
        tabStatScenarist.setAutoCreateRowSorter(true);
        tabStatDrawer.setAutoCreateRowSorter(true);

        // Rafraichissement des statistiques
        refreshStatResult();

        // Organisation de l'IHM : Les informations générales
        JPanel infoPane = new JPanel();
        GroupLayout lo = new GroupLayout(infoPane);
        infoPane.setLayout(lo);
        lo.setAutoCreateGaps(true);
        lo.setAutoCreateContainerGaps(true);
        // - alignement horizontal
        GroupLayout.Alignment align = GroupLayout.Alignment.LEADING;
        lo.setHorizontalGroup(lo.createSequentialGroup()
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(labNbCollect)
                    .addComponent(labNbObtained)
                    .addComponent(labNbToBuy)
                )
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(txtNbCollect)
                    .addComponent(txtNbObtained)
                    .addComponent(txtNbToBuy)
                )
        );
        // - alignement vertical
        align = GroupLayout.Alignment.BASELINE;
        lo.setVerticalGroup(lo.createSequentialGroup()
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(labNbCollect)
                    .addComponent(txtNbCollect)
                )
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(labNbObtained)
                    .addComponent(txtNbObtained)
                )
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(labNbToBuy)
                    .addComponent(txtNbToBuy)
                )
        );
        // - tailles de police identiques pour chaque composant
        lo.linkSize(labNbCollect, labNbObtained, labNbToBuy);
        lo.linkSize(txtNbCollect, txtNbObtained, txtNbToBuy);

        infoPane.setBorder(BorderFactory.createTitledBorder("Informations générales"));

        // Organisation de l'IHM : Les informations détaillées
        // - des scrollpane pour contenir les tableaux
        JScrollPane scrStatGenre = new JScrollPane(tabStatGenre);
        JScrollPane scrStatEditor = new JScrollPane(tabStatEditor);
        JScrollPane scrStatScenarist = new JScrollPane(tabStatScenarist);
        JScrollPane scrStatDrawer = new JScrollPane(tabStatDrawer);

        scrStatGenre.getVerticalScrollBar().setUnitIncrement(20);
        scrStatEditor.getVerticalScrollBar().setUnitIncrement(20);
        scrStatScenarist.getVerticalScrollBar().setUnitIncrement(20);
        scrStatDrawer.getVerticalScrollBar().setUnitIncrement(20);

        scrStatGenre.setPreferredSize(new Dimension(400, 100));
        scrStatEditor.setPreferredSize(new Dimension(400, 100));
        scrStatScenarist.setPreferredSize(new Dimension(400, 100));
        scrStatDrawer.setPreferredSize(new Dimension(400, 100));

        // - le paneau conteneur
        JPanel tabPane = new JPanel();
        lo = new GroupLayout(tabPane);
        tabPane.setLayout(lo);
        lo.setAutoCreateGaps(true);
        lo.setAutoCreateContainerGaps(true);
        // - alignement horizontal
        align = GroupLayout.Alignment.LEADING;
        lo.setHorizontalGroup(lo.createSequentialGroup()
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(labStatGenre)
                    .addComponent(scrStatGenre)
                    .addComponent(labStatScenarist)
                    .addComponent(scrStatScenarist)
                )
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(labStatEditor)
                    .addComponent(scrStatEditor)
                    .addComponent(labStatDrawer)
                    .addComponent(scrStatDrawer)
                )
        );
        // - alignement vertical
        align = GroupLayout.Alignment.BASELINE;
        lo.setVerticalGroup(lo.createSequentialGroup()
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(labStatGenre)
                    .addComponent(labStatEditor)
                )
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(scrStatGenre)
                    .addComponent(scrStatEditor)
                )
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(labStatScenarist)
                    .addComponent(labStatDrawer)
                )
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(scrStatScenarist)
                    .addComponent(scrStatDrawer)
                )
        );
        // - tailles de police identiques pour chaque composant
        lo.linkSize(labStatGenre, labStatEditor, labStatScenarist, labStatDrawer);
        lo.linkSize(scrStatGenre, scrStatEditor, scrStatScenarist, scrStatDrawer);
        // - titre
        tabPane.setBorder(BorderFactory.createTitledBorder("Informations détaillées"));

        
        JScrollPane tabScrollPane = new JScrollPane(tabPane);
        tabScrollPane.getVerticalScrollBar().setUnitIncrement(20);

        add(infoPane, BorderLayout.NORTH);
        add(tabScrollPane, BorderLayout.CENTER);
    }

    /**
     * Rafraichissement des statistiques
     */
    public final void refreshStatResult() {
        String[] columnsGenre = {"Genre", "Nb d'albums", "%"};
        String[] columnsEditor = {"Editeur", "Nb d'albums", "%"};
        String[] columnsScenarist = {"Scénariste", "Nb d'albums", "%"};
        String[] columnsDrawer = {"Dessinateur", "Nb d'albums", "%"};

        try {
            Statistics stats = FrameMain.db.statistics();

            txtNbCollect.setText(String.valueOf(stats.getTotal()));
            txtNbObtained.setText(String.valueOf(stats.getOwned()));
            txtNbToBuy.setText(String.valueOf(stats.getWanted()));

            ArrayList<StatisticsRepartition> statsGenre = stats.getGenres();
            ArrayList<StatisticsRepartition> statsEditor = stats.getEditeurs();
            ArrayList<StatisticsRepartition> statsScenarist = stats.getScenaristes();
            ArrayList<StatisticsRepartition> statsDrawer = stats.getDessinateurs();

            tabStatGenre.setModel(new TableModelStatistics(columnsGenre, statsGenre));
            tabStatEditor.setModel(new TableModelStatistics(columnsEditor, statsEditor));
            tabStatScenarist.setModel(new TableModelStatistics(columnsScenarist, statsScenarist));
            tabStatDrawer.setModel(new TableModelStatistics(columnsDrawer, statsDrawer));

            tabStatGenre.repaint();
            tabStatEditor.repaint();
            tabStatScenarist.repaint();
            tabStatDrawer.repaint();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null, 
                    "SQLException", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}