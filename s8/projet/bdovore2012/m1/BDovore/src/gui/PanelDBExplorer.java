package gui;

import db.SearchQuery;
import db.data.Album;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.TableColumn;

/**
 * Partie de l'IHM contenant le champ de recherche et l'affichage des résultats
 */
public class PanelDBExplorer extends JPanel {

    private static final long serialVersionUID = 1L;
    /**
     * Nombre de résultats à afficher
     */
    public static final int RECORDS_PER_PAGE = 40;
    // Composants du paneau :
    // - les boutons radio
    private ButtonGroup rbtGroup;
    private JRadioButton radAvailable, radOwned, radMissing;
    // - le menu déroulant
    private JComboBox lstCriterias;
    // - le champ de recherche
    private JTextField txtKeywords;
    // - affichage des pages
    private JLabel labPages;
    // - affichage des résultats
    private JTable table;
    // - les boutons (rechercher / changer de pages)
    private JButton btnSearch, btnFirstPage, btnPreviousPage, btnNextPage, btnLastPage;
    
    // Resultats des requêtes 
    private String getResultQuery = null;
    private String getCountQuery = null;
    
    // 
    private ArrayList<Album> result = null;
    
    // No de page courante et nombre de page total
    private int crtPage, numPages;

    /**
     * Constructeur du paneau <br/> - Initialise les composants <br/> - Affiche
     * le contenu de la bibliothèque
     */
    public PanelDBExplorer() {
        super(new BorderLayout());

        add(makeSearchPane(), BorderLayout.NORTH);
        add(makeTablePane(), BorderLayout.CENTER);
        add(makeNavigationPane(), BorderLayout.SOUTH);

        refreshTableContent();
        refreshNavigationButtons();
    }

    /**
     * Rafraichit le composant
     */
    public void refreshAll() {
        calculatePageNumbers();
        refreshNavigationButtons();
        refreshTableContent();
    }

    /**
     * Crée le panneau contenant le formulaire de recherche
     *
     * @return Le pannel de recherche
     */
    private JPanel makeSearchPane() {

        // Création des boutons radio
        radAvailable = new JRadioButton("Tous les albums");
        radOwned = new JRadioButton("Albums possédés ou à acheter", true);
        radMissing = new JRadioButton("Albums manquant");

        // Raccourcis
        radOwned.setMnemonic(KeyEvent.VK_P);
        radAvailable.setMnemonic(KeyEvent.VK_D);
        radMissing.setMnemonic(KeyEvent.VK_A);

        // Listeners
        radOwned.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                printAllObtainedAlbums();
            }
        });
        radAvailable.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                printAllAvailableAlbums();
            }
        });
        radMissing.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                printAllDesiredAlbums();
            }
        });

        // Ajout au groupe
        rbtGroup = new ButtonGroup();
        rbtGroup.add(radAvailable);
        rbtGroup.add(radOwned);
        rbtGroup.add(radMissing);

        // Création de la liste dérouleur
        String[] criterias = {"par Titre", "par Auteur", "par Série", "par Code barre / ISBN"};
        lstCriterias = new JComboBox(criterias);

        // Création du champ de recherche
        txtKeywords = new JTextField();
        txtKeywords.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent ev) {
                if (ev.getKeyCode() == KeyEvent.VK_ENTER) {
                    newSearch();
                }
            }
        });
        
        String help = "<html><font size=\"2\" color=\"gray\" face=\"Arial\">"
                + "Aide : Vous pouvez utiliser *, ?, AND, OR ou des guillemets dans vos recherches.<br/>"
                + "Vos recherches doivent comporter 3 caractères minimum et ne peuvent pas commencer par *<br/>"
                + "Sauf si voulez afficher tous les albums en utilisant alors * comme unique caractère"
                + "</font></html>";
        
        JLabel keywordsHelp = new JLabel(help);

        // Bouton de validation de la recherche
        btnSearch = new JButton("Rechercher", new ImageIcon("img/search.png"));
        btnSearch.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                newSearch();
            }
        });

        // Création du paneau de recherche
        // Utilisation d'un GroupLayout
        JPanel searchPane = new JPanel();
        GroupLayout layout = new GroupLayout(searchPane);
        searchPane.setLayout(layout);
        // Création d'espace entre les composants du groupe
        layout.setAutoCreateGaps(true);
        // Création d'espace entre les composants du groupe et le bord du conteneur
        layout.setAutoCreateContainerGaps(true);


        // Positionnement horizontal des composants.
        GroupLayout.Alignment align = GroupLayout.Alignment.LEADING;
        layout.setHorizontalGroup(
                layout.createSequentialGroup().addGroup(layout.createParallelGroup(align).addComponent(radAvailable).addComponent(lstCriterias)).addGroup(layout.createParallelGroup(align).addGroup(layout.createSequentialGroup().addComponent(radOwned).addComponent(radMissing)).addComponent(txtKeywords).addComponent(keywordsHelp)).addComponent(btnSearch));

        // Positionnement vertical des composants.
        align = GroupLayout.Alignment.BASELINE;
        layout.setVerticalGroup(
                layout.createSequentialGroup().addGroup(layout.createParallelGroup(align).addComponent(radOwned).addComponent(radAvailable).addComponent(radMissing)).addGroup(layout.createParallelGroup(align).addComponent(lstCriterias).addComponent(txtKeywords).addComponent(btnSearch)).addComponent(keywordsHelp));
    
        layout.linkSize(SwingConstants.HORIZONTAL, lstCriterias, radOwned, radAvailable, radMissing);
        layout.linkSize(SwingConstants.VERTICAL, lstCriterias, txtKeywords);

        // Ajouter le formulaire de recherche
        searchPane.setBorder(BorderFactory.createTitledBorder("Recherche d'albums"));

        return searchPane;
    }

    /**
     * Crée le panneau d'affichage des résultats
     * 
     * @return Le panneau d'affichage des résultats vide
     */
    private JScrollPane makeTablePane() {

        // Création de la table contenant les résultats
        table = new JTable();
        // Crée automatiquement un objet RowSorter pour les tris par colonnes
        table.setAutoCreateRowSorter(true);
        // Autorise la selection d'un seul élément de la table
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent ev) {
                if (ev.getClickCount() == 2) {
                    JTable source = (JTable) ev.getSource();
                    RowSorter<?> sorter = source.getRowSorter();
                    showAlbumDetails(sorter.convertRowIndexToModel(source.getSelectedRow()));
                }
            }
        });

        // Requêtes pour le peuplement initial (premier rafraichissement)
        getResultQuery = SearchQuery.searchNothing(SearchQuery.SEARCH_IN_OWNED, SearchQuery.GET_FIELDS, "t.TITRE", SearchQuery.ORDER_ASC);
        getCountQuery = SearchQuery.searchNothing(SearchQuery.SEARCH_IN_OWNED, SearchQuery.GET_MAX, "t.TITRE", SearchQuery.ORDER_ASC);

        JScrollPane scrollPane = new JScrollPane(table);
        return scrollPane;
    }

    /**
     * Crée le panneau contenant les boutons de navigation entre les pages de résultats
     * 
     * @return Le panneau de navigation
     */
    private JPanel makeNavigationPane() {
        
        // Création des boutons de navigation
        // + info-bulle & listener
        btnFirstPage = new JButton(new ImageIcon("img/first.png"));
        btnFirstPage.setToolTipText("Première page");
        btnFirstPage.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        turnToFirstPage();
                    }
                });
        btnPreviousPage = new JButton(new ImageIcon("img/previous.png"));
        btnPreviousPage.setToolTipText("Page précédente");
        btnPreviousPage.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        turnToPreviousPage();
                    }
                });
        btnNextPage = new JButton(new ImageIcon("img/next.png"));
        btnNextPage.setToolTipText("Page suivante");
        btnNextPage.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        turnToNextPage();
                    }
                });
        btnLastPage = new JButton(new ImageIcon("img/last.png"));
        btnLastPage.setToolTipText("Dernière page");
        btnLastPage.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        turnToLastPage();
                    }
                });

        labPages = new JLabel();

        JPanel navigationPane = new JPanel(new FlowLayout(SwingConstants.RIGHT));
        navigationPane.add(labPages);
        navigationPane.add(btnFirstPage);
        navigationPane.add(btnPreviousPage);
        navigationPane.add(btnNextPage);
        navigationPane.add(btnLastPage);

        return navigationPane;
    }

    /**
     * Affiche les détails d'un album dans une nouvelle petite fenêtre
     * 
     * @param selectedRow La ligne de l'album selectionné dans le tableau de resultat 
     */
    private void showAlbumDetails(int selectedRow) {
        Window owner = SwingUtilities.getWindowAncestor(this);
        if (owner != null) {
            DialogAlbumInfo dialog = new DialogAlbumInfo(
                    owner,
                    Dialog.ModalityType.APPLICATION_MODAL,
                    ((TableModelDBExplorer) table.getModel()).getSelectedAlbum(selectedRow));
            dialog.setVisible(true);
        }
    }

    /**
     * Effectue une nouvelle recherche et met à jour l'affichage.
     */
    private void newSearch() {
        
        // Etendue de la recherche
        int searchIn = SearchQuery.SEARCH_IN_ALL;
        
        // Mots-clés de l'utilisateur
        String keywords = txtKeywords.getText();

        // Consultation des boutons radio
        // => mise à jour de l'étendue de la recherche si nécessaire
        if (radOwned.isSelected()) {
            searchIn = SearchQuery.SEARCH_IN_OWNED;
        }
        if (radMissing.isSelected()) {
            searchIn = SearchQuery.SEARCH_IN_MISSING;
        }

        // Consultation des critères du menu déroulant :
        // Par titre, par auteur, par série, par ISBN
        // + les requêtes
        switch (lstCriterias.getSelectedIndex()) {
            case 0:
                getResultQuery = SearchQuery.searchTitre(searchIn, keywords, SearchQuery.GET_FIELDS, "t.TITRE", SearchQuery.ORDER_ASC);
                getCountQuery = SearchQuery.searchTitre(searchIn, keywords, SearchQuery.GET_MAX, "t.TITRE", SearchQuery.ORDER_ASC);
                break;
            case 1:
                getResultQuery = SearchQuery.searchAuteur(searchIn, keywords, SearchQuery.GET_FIELDS, "t.TITRE", SearchQuery.ORDER_ASC);
                getCountQuery = SearchQuery.searchAuteur(searchIn, keywords, SearchQuery.GET_MAX, "t.TITRE", SearchQuery.ORDER_ASC);
                break;
            case 2:
                getResultQuery = SearchQuery.searchSerie(searchIn, keywords, SearchQuery.GET_FIELDS, "t.TITRE", SearchQuery.ORDER_ASC);
                getCountQuery = SearchQuery.searchSerie(searchIn, keywords, SearchQuery.GET_MAX, "t.TITRE", SearchQuery.ORDER_ASC);
                break;
            case 3:
                getResultQuery = SearchQuery.searchISBN(searchIn, keywords, SearchQuery.GET_FIELDS, "t.TITRE", SearchQuery.ORDER_ASC);
                getCountQuery = SearchQuery.searchISBN(searchIn, keywords, SearchQuery.GET_MAX, "t.TITRE", SearchQuery.ORDER_ASC);
                break;
        }        
        
        refreshAll();
    }

    /**
     * Calcul le nombre de pages et met à jour les données du panneau correspondant.
     */
    private void calculatePageNumbers() {
        try {
            int numAlbums = FrameMain.db.getNumAlbums(getCountQuery);
            if (numAlbums % RECORDS_PER_PAGE > 0) {
                numPages = (numAlbums / RECORDS_PER_PAGE + 1);
            } else {
                numPages = (numAlbums / RECORDS_PER_PAGE);
            }

            if (numPages > 0) {
                crtPage = 1;
            } else {
                crtPage = 0;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null, 
                    "SQLException while refreshing navigation pane", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Mise à jour des boutons de navigations.
     */
    private void refreshNavigationButtons() {
        if (crtPage <= 1) {
            btnFirstPage.setEnabled(false);
            btnPreviousPage.setEnabled(false);
        } else if (crtPage > 1) {
            btnFirstPage.setEnabled(true);
            btnPreviousPage.setEnabled(true);
        }
        if (crtPage == numPages) {
            btnNextPage.setEnabled(false);
            btnLastPage.setEnabled(false);
        } else if (0 < crtPage && crtPage < numPages) {
            btnNextPage.setEnabled(true);
            btnLastPage.setEnabled(true);
        }
    }

    /**
     * Met à jour et rafraichit le tableau de résultats.
     */
    private void refreshTableContent() {
        
        // Mise à jour du label des pages
        labPages.setText("Page " + crtPage + "/" + numPages);

        // Requête pour obtenir une liste d'album
        // Insertion des données dans le tableau
        try {
            result = FrameMain.db.search(getResultQuery, RECORDS_PER_PAGE, (crtPage - 1) * RECORDS_PER_PAGE);
            table.setModel(new TableModelDBExplorer(result));
            //Définition de la taille de chaque colonne
            TableColumn column = null;
            for (int c = 0; c < table.getColumnCount(); c++) {
                column = table.getColumnModel().getColumn(c);
                switch (c) {
                    case 0:
                        column.setPreferredWidth(200);
                        break;
                    case 1:
                        column.setPreferredWidth(50);
                        break;
                    case 2:
                    case 3:
                        column.setPreferredWidth(100);
                        break;
                    case 4:
                    case 5:
                        column.setPreferredWidth(150);
                }
            }
            
            // Rafraichissement
            table.repaint();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null, 
                    "SQLException while refreshing table content", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }

    /**
     * Aller à la première page.
     */
    private void turnToFirstPage() {
        crtPage = 1;
        refreshNavigationButtons();
        refreshTableContent();
    }

    /**
     * Aller à la page précédente.
     */
    private void turnToPreviousPage() {
        if (crtPage > 0) {
            crtPage = crtPage - 1;
        }
        refreshNavigationButtons();
        refreshTableContent();
    }

    /**
     * Aller à la page suivante.
     */
    private void turnToNextPage() {
        crtPage = crtPage + 1;
        refreshNavigationButtons();
        refreshTableContent();
    }

    /**
     * Aller à la dernière page.
     */
    private void turnToLastPage() {
        crtPage = numPages;
        refreshNavigationButtons();
        refreshTableContent();
    }

    /**
     * Affiche tous les albums possédés.
     */
    private void printAllObtainedAlbums() {

        getResultQuery = SearchQuery.searchNothing(SearchQuery.SEARCH_IN_OWNED, SearchQuery.GET_FIELDS, "t.TITRE", SearchQuery.ORDER_ASC);
        getCountQuery = SearchQuery.searchNothing(SearchQuery.SEARCH_IN_OWNED, SearchQuery.GET_MAX, "t.TITRE", SearchQuery.ORDER_ASC);

        refreshAll();
    }

    /**
     * Affiche tous les albums. 
     */
    private void printAllAvailableAlbums() {

        getResultQuery = SearchQuery.searchNothing(SearchQuery.SEARCH_IN_ALL, SearchQuery.GET_FIELDS, "t.TITRE", SearchQuery.ORDER_ASC);
        getCountQuery = SearchQuery.searchNothing(SearchQuery.SEARCH_IN_ALL, SearchQuery.GET_MAX, "t.TITRE", SearchQuery.ORDER_ASC);

        refreshAll();
    }

    /**
     * Affiche tous les albums marqués comme "manquants".
     */
    private void printAllDesiredAlbums() {

        getResultQuery = SearchQuery.searchNothing(SearchQuery.SEARCH_IN_MISSING, SearchQuery.GET_FIELDS, "t.TITRE", SearchQuery.ORDER_ASC);
        getCountQuery = SearchQuery.searchNothing(SearchQuery.SEARCH_IN_MISSING, SearchQuery.GET_MAX, "t.TITRE", SearchQuery.ORDER_ASC);

        refreshAll();
    }
}
