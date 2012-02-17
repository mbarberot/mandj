package gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;

import db.SearchQuery;
import db.data.Album;

public class PanelDBExplorer extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	public static final int RECORDS_PER_PAGE = 40;
	
	private ButtonGroup rbtGroup;
	private JRadioButton radAvailable, radOwned, radMissing;
	private JComboBox lstCriterias;
	private JTextField txtKeywords;
	private JLabel labPages;
	private JTable table;
	private JButton btnSearch, btnFirstPage, btnPreviousPage, btnNextPage, btnLastPage;
	
	private String getResultQuery = null;
	private String getCountQuery = null;
	private ArrayList<Album> result = null;
	private int crtPage, numPages;
		
	public PanelDBExplorer(){
		super(new BorderLayout());
		
		add(makeSearchPane(), BorderLayout.NORTH);
		add(makeTablePane(), BorderLayout.CENTER);
		add(makeNavigationPane(), BorderLayout.SOUTH);
		
		refreshTableContent();
		refreshNavigationButtons();
	}
	
	public void refreshAll(){
		calculatePageNumbers();
		refreshNavigationButtons();
		refreshTableContent();
	}
	
	private JPanel makeSearchPane(){
		
		// CrÃ©er les composants Swing du formulaire de recherche.
		radAvailable	= new JRadioButton("Tous les albums");
		radOwned		= new JRadioButton("Albums possédés ou à acheter", true);
		radMissing		= new JRadioButton("Albums manquant");
		
		radOwned.setMnemonic(KeyEvent.VK_P);
		radAvailable.setMnemonic(KeyEvent.VK_D);
		radMissing.setMnemonic(KeyEvent.VK_A);
		
		radOwned.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				printAllObtainedAlbums();
			}
		});
		radAvailable.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				printAllAvailableAlbums();
			}
		});
		radMissing.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				printAllDesiredAlbums();
			}
		});
		
		rbtGroup = new ButtonGroup();
		rbtGroup.add(radAvailable);
		rbtGroup.add(radOwned);
		rbtGroup.add(radMissing);
		
		
		// Dans cette version du logiciel, la recherche par auteur n'est pas pertinante
		// puisque l'on telecharge au fur et a mesure les infos sur les album
		// Les fonctions de recherche par auteur sont tout de même gardé.
		String[] criterias = {"par Titre", "par Série", /*"par Auteur",*/ "par Code barre / ISBN"};
		lstCriterias	= new JComboBox(criterias);
		
		txtKeywords = new JTextField();
		txtKeywords.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent ev){
				if(ev.getKeyCode() == KeyEvent.VK_ENTER)
					newSearch();
			}
		});
		
		btnSearch = new JButton("Rechercher", new ImageIcon("img/search.png"));
		btnSearch.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				newSearch();
			}
		});
		
		// CrÃ©er le formulaire et dÃ©finir le gestionnaire de postionnement (LayoutManager).
		JPanel searchPane = new JPanel();
		GroupLayout layout = new GroupLayout(searchPane);
		searchPane.setLayout(layout);
		
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		// Positionnement horizontal des composants.
		GroupLayout.Alignment align = GroupLayout.Alignment.LEADING;
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(align)
					.addComponent(radAvailable)
					.addComponent(lstCriterias))
				.addGroup(layout.createParallelGroup(align)
					.addGroup(layout.createSequentialGroup()
						.addComponent(radOwned)
						.addComponent(radMissing))
					.addComponent(txtKeywords))
				.addComponent(btnSearch)
		);
		
		// Positionnement vertical des composants.
		align = GroupLayout.Alignment.BASELINE;
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(align)
					.addComponent(radOwned)
					.addComponent(radAvailable)
					.addComponent(radMissing))
				.addGroup(layout.createParallelGroup(align)
					.addComponent(lstCriterias)
					.addComponent(txtKeywords)
					.addComponent(btnSearch))
		);
		layout.linkSize(SwingConstants.HORIZONTAL, lstCriterias, radOwned, radAvailable, radMissing);
		layout.linkSize(SwingConstants.VERTICAL, lstCriterias, txtKeywords);
		
		// Ajouter le formulaire de recherche
		searchPane.setBorder(BorderFactory.createTitledBorder("Recherche d'albums"));
		
		return searchPane;
	}
	
	private JScrollPane makeTablePane(){
		table = new JTable();
		table.setAutoCreateRowSorter(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent ev){
				if(ev.getClickCount() == 2){
					JTable source = (JTable)ev.getSource();
					RowSorter<?> sorter = source.getRowSorter();
					//System.out.println("row:" + sorter.convertRowIndexToModel(source.getSelectedRow()));
					showAlbumDetails(sorter.convertRowIndexToModel(source.getSelectedRow()));
				}
			}
		});
		
		getResultQuery = SearchQuery.searchNothing(SearchQuery.SEARCH_IN_OWNED, SearchQuery.GET_FIELDS, "t.TITRE", SearchQuery.ORDER_ASC);
		getCountQuery = SearchQuery.searchNothing(SearchQuery.SEARCH_IN_OWNED, SearchQuery.GET_MAX, "t.TITRE", SearchQuery.ORDER_ASC);			
		
		JScrollPane scrollPane = new JScrollPane(table);
		return scrollPane;
	}
	
	private JPanel makeNavigationPane(){
		// CrÃ©er des boutons de navigation
		btnFirstPage	= new JButton(new ImageIcon("img/first.png"));
		btnFirstPage.setToolTipText("Première page");
		btnFirstPage.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent ev){
						turnToFirstPage();
					}
				});
		btnPreviousPage	= new JButton(new ImageIcon("img/previous.png"));
		btnPreviousPage.setToolTipText("Page précédente");
		btnPreviousPage.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent ev){
						turnToPreviousPage();
					}
				});
		btnNextPage		= new JButton(new ImageIcon("img/next.png"));
		btnNextPage.setToolTipText("Page suivante");
		btnNextPage.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent ev){
						turnToNextPage();
					}
				});
		btnLastPage		= new JButton(new ImageIcon("img/last.png"));
		btnLastPage.setToolTipText("Dernière page");
		btnLastPage.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent ev){
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
	
	private void showAlbumDetails(int selectedRow){
		Window owner = SwingUtilities.getWindowAncestor(this);
		if(owner != null){
			DialogAlbumInfo dialog = new DialogAlbumInfo(
					owner, 
					Dialog.ModalityType.APPLICATION_MODAL,
					((TableModelDBExplorer)table.getModel()).getSelectedAlbum(selectedRow));
			dialog.setVisible(true);
		}
	}
	
	private void newSearch(){
		int searchIn = SearchQuery.SEARCH_IN_ALL;
		String keywords = txtKeywords.getText();
		
		if(radOwned.isSelected())
			searchIn = SearchQuery.SEARCH_IN_OWNED;
		if(radMissing.isSelected())
			searchIn = SearchQuery.SEARCH_IN_MISSING;
		
		//0-Titre, 1-SÃ©rie, 2-ISBN, 3-Auteur(n'existe plus)
		switch(lstCriterias.getSelectedIndex()){
		case 0:
			getResultQuery = SearchQuery.searchTitre(searchIn, keywords, SearchQuery.GET_FIELDS, "t.TITRE", SearchQuery.ORDER_ASC);
			getCountQuery = SearchQuery.searchTitre(searchIn, keywords, SearchQuery.GET_MAX, "t.TITRE", SearchQuery.ORDER_ASC);
			break;
		case 1:
			getResultQuery = SearchQuery.searchSerie(searchIn, keywords, SearchQuery.GET_FIELDS, "t.TITRE", SearchQuery.ORDER_ASC);
			getCountQuery = SearchQuery.searchSerie(searchIn, keywords, SearchQuery.GET_MAX, "t.TITRE", SearchQuery.ORDER_ASC);
			break;
		case 2:
			getResultQuery = SearchQuery.searchISBN(searchIn, keywords, SearchQuery.GET_FIELDS, "t.TITRE", SearchQuery.ORDER_ASC);
			getCountQuery = SearchQuery.searchISBN(searchIn, keywords, SearchQuery.GET_MAX, "t.TITRE", SearchQuery.ORDER_ASC);
			break;
		case 3:
			getResultQuery = SearchQuery.searchAuteur(searchIn, keywords, SearchQuery.GET_FIELDS, "t.TITRE", SearchQuery.ORDER_ASC);
			getCountQuery = SearchQuery.searchAuteur(searchIn, keywords, SearchQuery.GET_MAX, "t.TITRE", SearchQuery.ORDER_ASC);
			break;
		}
		
		refreshAll();
	}
	
	private void calculatePageNumbers(){
		try{
			int numAlbums = FrameMain.db.getNumAlbums(getCountQuery);
			if(numAlbums % RECORDS_PER_PAGE > 0)
				numPages = (numAlbums / RECORDS_PER_PAGE + 1);
			else
				numPages = (numAlbums / RECORDS_PER_PAGE);
			
			if(numPages > 0)
				crtPage = 1;
			else
				crtPage = 0;
		}catch(SQLException ex){
			JOptionPane.showMessageDialog(null, "SQLException while refreshing navigation pane", "Error", JOptionPane.ERROR_MESSAGE);
			
		}
	}
	
	private void refreshNavigationButtons(){
		if(crtPage <= 1){
			btnFirstPage.setEnabled(false);
			btnPreviousPage.setEnabled(false);
		}
		else if(crtPage > 1){
			btnFirstPage.setEnabled(true);
			btnPreviousPage.setEnabled(true);
		}
		if(crtPage == numPages){
			btnNextPage.setEnabled(false);
			btnLastPage.setEnabled(false);
		}
		else if(0 < crtPage && crtPage < numPages){
			btnNextPage.setEnabled(true);
			btnLastPage.setEnabled(true);
		}
	}
	
	private void refreshTableContent(){
		labPages.setText("Page " + crtPage + "/" + numPages);
		
		try{
			result = FrameMain.db.search(getResultQuery, RECORDS_PER_PAGE, (crtPage - 1)*RECORDS_PER_PAGE);
			table.setModel(new TableModelDBExplorer(result));
			//DÃ©finir les tailles des colonnes
			TableColumn column = null;
			for(int c = 0; c < table.getColumnCount(); c ++){
				column = table.getColumnModel().getColumn(c);
				switch(c){
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
			table.repaint();
		}catch(SQLException ex){
			JOptionPane.showMessageDialog(null, "SQLException while refreshing table content", "Error", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}	

	private void turnToFirstPage(){
		crtPage = 1;
		refreshNavigationButtons();
		refreshTableContent();
	}
	private void turnToPreviousPage(){
		if(crtPage > 0)
			crtPage = crtPage - 1;
		refreshNavigationButtons();
		refreshTableContent();
	}
	private void turnToNextPage(){
		crtPage = crtPage + 1;
		refreshNavigationButtons();
		refreshTableContent();
	}
	private void turnToLastPage(){
		crtPage = numPages;
		refreshNavigationButtons();
		refreshTableContent();
	}
	
	private void printAllObtainedAlbums(){
		
		getResultQuery = SearchQuery.searchNothing(SearchQuery.SEARCH_IN_OWNED, SearchQuery.GET_FIELDS, "t.TITRE", SearchQuery.ORDER_ASC);
		getCountQuery = SearchQuery.searchNothing(SearchQuery.SEARCH_IN_OWNED, SearchQuery.GET_MAX, "t.TITRE", SearchQuery.ORDER_ASC);
		
		refreshAll();
	}
	private void printAllAvailableAlbums(){
		
		getResultQuery = SearchQuery.searchNothing(SearchQuery.SEARCH_IN_ALL, SearchQuery.GET_FIELDS, "t.TITRE", SearchQuery.ORDER_ASC);
		getCountQuery = SearchQuery.searchNothing(SearchQuery.SEARCH_IN_ALL, SearchQuery.GET_MAX, "t.TITRE", SearchQuery.ORDER_ASC);
		
		refreshAll();
	}
	private void printAllDesiredAlbums(){
		
		getResultQuery = SearchQuery.searchNothing(SearchQuery.SEARCH_IN_MISSING, SearchQuery.GET_FIELDS, "t.TITRE", SearchQuery.ORDER_ASC);
		getCountQuery = SearchQuery.searchNothing(SearchQuery.SEARCH_IN_MISSING, SearchQuery.GET_MAX, "t.TITRE", SearchQuery.ORDER_ASC);
		
		refreshAll();
	}
}
