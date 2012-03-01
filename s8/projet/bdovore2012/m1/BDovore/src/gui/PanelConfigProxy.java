package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.swing.*;
import main.Config;

/**
 * Panneau de configuration du proxy
 */
public class PanelConfigProxy extends JPanel {

    private static final long serialVersionUID = 1L;
    // Eléments de l'IHM
    private JLabel labProxyServer;
    private JTextField txtProxyServer, txtProxyPort;
    private JButton btnRestore, btnApply;
    // Stockage des propriétés
    private Properties config;
    // Pour la sauvegarde dans le fichier de configuration
    private BufferedOutputStream ostream;

    /**
     * Constructeur
     *
     * @param config - Configuration actuelle
     */
    public PanelConfigProxy(Properties config) {
        super(new BorderLayout());

        this.config = config;

        createGUI();
        affectEventListeners();
    }

    /**
     * Initialisation de l'IHM
     */
    private void createGUI() {
        labProxyServer = new JLabel("Serveur Proxy :");

        txtProxyServer = new JTextField(20);
        txtProxyPort = new JTextField(5);

        txtProxyServer.setText(config.getProperty("proxyserver"));
        txtProxyPort.setText(config.getProperty("proxyport"));

        JPanel formPane = new JPanel(new FlowLayout());
        formPane.add(labProxyServer);
        formPane.add(txtProxyServer);
        formPane.add(txtProxyPort);

        btnRestore = new JButton("Restaurer", new ImageIcon("img/restore.png"));
        btnApply = new JButton("Appliquer", new ImageIcon("img/apply.png"));

        btnRestore.setMnemonic(KeyEvent.VK_R);
        btnApply.setMnemonic(KeyEvent.VK_A);

        JPanel ctrlPane = new JPanel();
        ctrlPane.add(btnApply);
        ctrlPane.add(btnRestore);

        add(formPane, BorderLayout.CENTER);
        add(ctrlPane, BorderLayout.SOUTH);
    }

    /**
     * Application des listeners
     */
    private void affectEventListeners() {
        btnApply.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                saveConfig();
            }
        });
        btnRestore.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                txtProxyServer.setText(config.getProperty("proxyserver"));
                txtProxyPort.setText(config.getProperty("proxyport"));
            }
        });
    }

    /** 
     * Sauvegarde de la configuration
     */
    private void saveConfig() {
        
        // Récupération des informations entrées par l'utilisateur
        config.setProperty("proxyserver", txtProxyServer.getText());
        config.setProperty("proxyport", txtProxyPort.getText());

        // Ecriture dans le fichier de configuration
        try {
            ostream = new BufferedOutputStream(new FileOutputStream(Config.CONFIG_FILENAME));
            config.storeToXML(ostream, null);
            ostream.close();
            JOptionPane.showMessageDialog(null, "Serveur proxy enregistré", null, JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "IOException", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
