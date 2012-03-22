package gui;

import db.DataBase;
import db.data.User;
import db.synch.Synch;
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

/**
 * Fenetre principale
 */
public class FrameMain extends JFrame {

    private static final long serialVersionUID = 1L;
    
    //Localisation de la base de donnée locale
    public static final String DATABASE_NAME = "db/bdovore";
    // Base de donnée embarquée
    public static final DataBase db = getDBConnection();
    // Objet pour les mises à jour
    public static final Synch synch = getUpdaterConstruct();
    // Utilisateur courant
    public static final User currentUser = getCurrentUser();
    // Proxy courant
    public static final Proxy currentProxy = getCurrentProxy();
    
    // Les "cartes" disponibles
    // Elles permettent de switcher de la recherche au statistiques et inversement
    public static final String cardExplorerName = "PanelDBExplorer";
    public static final String cardStatName = "PanelStatistics";
    
    // Principaux composants de l'interface graphique :
    //  - Panel "conteneur"
    //  - Panel de recherche/affichage des résultats
    //  - Panel des statistiques
    private JPanel bodyPane;
    private PanelDBExplorer cardExplorer;
    private PanelStatistics cardStat;

    /**
     * Constructeur
     */
    public FrameMain() {
        // Initialisation des composants
        bodyPane = new JPanel(new CardLayout());
        cardExplorer = new PanelDBExplorer();
        cardStat = new PanelStatistics();

        // Ajout des "cartes"
        bodyPane.add(cardExplorer, FrameMain.cardExplorerName);
        bodyPane.add(cardStat, FrameMain.cardStatName);

        // Ajout la barre d'outils.
        setJMenuBar(new MainMenuBar());

        // Créer le contenu de la fenêtre principale
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(new MainToolBar(), BorderLayout.NORTH);
        contentPane.add(bodyPane, BorderLayout.CENTER);
        setContentPane(contentPane);

        // Déconnexion de la base de données lors que la fermeture de fenêtre
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent ev) {
                try {
                    db.shutdown();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Impossible de se déconnecter de la base de données",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });

        // Mise à la dimension de l'écran
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) screenSize.getWidth() * 3 / 4, (int) screenSize.getHeight() * 3 / 4);

        setTitle("BDovore");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        getCurrentUser();
    }

    /**
     * Permet de passer à la carte mentionnée.
     * @param cardName Le nom de la carte à afficher
     */
    public void switchCard(String cardName) {
        CardLayout layout = (CardLayout) bodyPane.getLayout();
        layout.show(bodyPane, cardName);
    }

    /**
     * Rafraichissement des recherches
     */
    public void refreshExplorer() {
        cardExplorer.refreshAll();
    }

    /**
     * Rafraichissement des stats
     */
    public void refreshStatResult() {
        cardStat.refreshStatResult();
    }

    /**
     * Connection à la base de donnée
     * @return objet de manipulation de la base de donnée
     */
    private static DataBase getDBConnection() {
        DataBase db = null;
        try {
            db = new DataBase(DATABASE_NAME);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null, 
                    "Impossible de se connecter à la base de données", 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE
                    );
            e.printStackTrace();
        }
        return db;
    }

    /**
     * Retourne l'utilisateur courant
     * @return l'utilisateur courant
     */
    private static User getCurrentUser() {
        User user = null;
        Properties config = new Properties();
        
        // Lecture du fichier de config
        try {
            BufferedInputStream istream = new BufferedInputStream(new FileInputStream(Config.CONFIG_FILENAME));
            config.loadFromXML(istream);
            istream.close();
        } catch (IOException ex) {
            return null;
        }

        // Récupération des informations (login/pwd)
        String userName = config.getProperty("username");
        String userPwd = config.getProperty("userpwd");
        if (userName == null || userPwd == null) {
            return null;
        }

        // Création d'un objet User
        user = new User(userName, userPwd);
        if (!user.isValid()) {
            return null;
        }
        
        return user;
    }

    /**
     * Retourne le proxy
     * @return le proxy 
     */
    private static Proxy getCurrentProxy() {
        InetSocketAddress proxyAddress = null;

        //Lecture de la configuration de proxy
        Properties config = new Properties();

        // Lecture du fichier de config
        try {
            BufferedInputStream istream = new BufferedInputStream(new FileInputStream(Config.CONFIG_FILENAME));
            config.loadFromXML(istream);
            istream.close();
        } catch (IOException ex) {
            System.getProperties().put("http.proxySet", "false");
            return null;
        }

        // Récupération des informations
        String proxyServer = config.getProperty("proxyserver");
        String proxyPort = config.getProperty("proxyport");

        // Vérifications 
        if (proxyServer == null || proxyPort == null) {
            System.getProperties().put("http.proxySet", "false");
            return null;
        }

        // Création du proxy
        try {
            proxyAddress = new InetSocketAddress(proxyServer, Integer.parseInt(proxyPort));
        } catch (Exception ex) {
            System.getProperties().put("http.proxySet", "false");
            return null;
        }
        
        // Applique le proxy
        System.getProperties().put("http.proxyHost", proxyAddress.getHostName());
        System.getProperties().put("http.proxyPort", "" + proxyAddress.getPort());
        System.getProperties().put("http.proxySet", "true");

        // Affichage
        System.out.println("Proxy : " + proxyAddress.getHostName());
        System.out.println("Proxy : " + proxyAddress.getPort());

        return new Proxy(Proxy.Type.HTTP, proxyAddress);
    }

    /**
     * Retourne un objet pour les mises à jour
     * @return un objet pour les mises à jour
     */
    private static Synch getUpdaterConstruct() {
        Proxy proxy = currentProxy;
        if (proxy == null) {
            return new Synch(FrameMain.db);
        } else {
            return new Synch(FrameMain.db, proxy);
        }
    }
}
