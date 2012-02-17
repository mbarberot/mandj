package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.Config;

import db.DataBase;
import db.data.User;
import db.update.Updater;

public class FrameMain extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public static final String DATABASE_NAME = "db/bdovore";
	
	public static final DataBase db = getDBConnection(); 
	public static final Updater up = getUpdaterConstruct(); 
	public static final User currentUser = getCurrentUser();
	public static final Proxy currentProxy = getCurrentProxy();
	
	public static final String cardExplorerName = "PanelDBExplorer";
	public static final String cardStatName = "PanelStatistics";
	
	private JPanel bodyPane;
	private PanelDBExplorer cardExplorer;
	private PanelStatistics cardStat;
	
	public FrameMain(){
		bodyPane		= new JPanel(new CardLayout());
		cardExplorer	= new PanelDBExplorer();
		cardStat		= new PanelStatistics();
		
		bodyPane.add(cardExplorer, FrameMain.cardExplorerName);
		bodyPane.add(cardStat, FrameMain.cardStatName);
		
		// Ajout la barre d'outils.
		setJMenuBar(new MainMenuBar());
		
		// CrÃ©er le contenu de la fenÃªtre principale
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(new MainToolBar(), BorderLayout.NORTH);
		contentPane.add(bodyPane, BorderLayout.CENTER);
		setContentPane(contentPane);

		// DÃ©connexion de la base de donnÃ©es lors que la fermeture de fenÃªtre
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent ev){
				try{
					db.shutdown();
				}catch(SQLException e){
					JOptionPane.showMessageDialog(null,	"Impossible de se déconnecter de la base de donnés", "Erreur", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
				System.exit(0);
			}});
		
		//pack();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize((int) screenSize.getWidth()*3/4,(int) screenSize.getHeight()*3/4);
		
		setTitle("BDovore");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		getCurrentUser();
	}


	public void switchCard(String cardName){
		CardLayout layout = (CardLayout)bodyPane.getLayout();
		layout.show(bodyPane, cardName);
	}
	
	public void refreshExplorer(){
		cardExplorer.refreshAll();
	}
	
	public void refreshStatResult(){
		cardStat.refreshStatResult();
	}
	
	private static DataBase getDBConnection(){
		DataBase db = null;
		try{
			db = new DataBase(DATABASE_NAME);
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,	"Impossible de se connecter à la base de donnée", "Erreur",	JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return db;
	}
	
	private static User getCurrentUser(){
		User user = null;
		Properties config = new Properties();
		try{
			BufferedInputStream istream = new BufferedInputStream(new FileInputStream(Config.CONFIG_FILENAME));
			config.loadFromXML(istream);
			istream.close();
		}catch(IOException ex){
			return null;
		}
		
		String userName = config.getProperty("username");
		String userPwd = config.getProperty("userpwd");
		if(userName == null || userPwd == null)
			return null;
		
		user = new User(userName, userPwd);
		if(! user.isValid())
			return null;
		return user;
	}
	
	private static Proxy getCurrentProxy(){
		InetSocketAddress proxyAddress = null;
		
		

		//Lecture de la configuration de proxy
		Properties config = new Properties();
		try{
			BufferedInputStream istream = new BufferedInputStream(new FileInputStream(Config.CONFIG_FILENAME));
			config.loadFromXML(istream);
			istream.close();
		} catch (IOException ex) {
			System.getProperties().put("http.proxySet", "false");
			return null;
		}
		
		//Construction d'adresse du serveur proxy
		String proxyServer = config.getProperty("proxyserver");
		String proxyPort = config.getProperty("proxyport");
		
		
		if (proxyServer == null || proxyPort == null) {
			System.getProperties().put("http.proxySet", "false");
			return null;
		}
		
		try{
			proxyAddress = new InetSocketAddress(proxyServer, Integer.parseInt(proxyPort));
		} catch (Exception ex) {
			System.getProperties().put("http.proxySet", "false");
			return null;
		}
		
		/*if (proxyAddress.isUnresolved()) {
			System.getProperties().put("http.proxySet", "false");
			return null;
		}*/
		
		System.getProperties().put("http.proxyHost", proxyAddress.getHostName());
		System.getProperties().put("http.proxyPort", "" + proxyAddress.getPort());
		System.getProperties().put("http.proxySet", "true");
		
		System.out.println("Proxy : " + proxyAddress.getHostName());
		System.out.println("Proxy : " + proxyAddress.getPort());
		
		return new Proxy(Proxy.Type.HTTP, proxyAddress);
	}
	
	private static Updater getUpdaterConstruct() {
		
		Proxy proxy = currentProxy;
		if(proxy == null)
			return new Updater(FrameMain.db);
		else
			return  new Updater(FrameMain.db, proxy);
	}
	
}
