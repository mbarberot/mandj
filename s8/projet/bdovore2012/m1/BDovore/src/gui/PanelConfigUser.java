package gui;

import db.data.User;
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
 * Panel de configuration de l'utilisateur
 */
public class PanelConfigUser extends JPanel {

    private static final long serialVersionUID = 1L;
    // Elements de l'interface
    private JLabel labUsername, labUserpwd;
    private JTextField txtUsername;
    private JPasswordField txtUserpwd;
    private JButton btnApply, btnRestore;
    // Stockage de la configuration
    private Properties config;
    // Pour l'ecriture ver le fichier de configuration
    private BufferedOutputStream ostream;

    /**
     * Constructeur
     *
     * @param config La configuration actuelle
     */
    public PanelConfigUser(Properties config) {
        super(new BorderLayout());

        this.config = config;

        createGUI();
        affectEventListeners();
    }

    /**
     * Création du panneau
     */
    private void createGUI() {
        // Initialisation des composants
        labUsername = new JLabel("Nom d'utilisateur BDovore :");
        labUserpwd = new JLabel("Mot de passe :");

        txtUsername = new JTextField(20);
        txtUserpwd = new JPasswordField(20);

        txtUsername.setText(config.getProperty("username"));
        txtUserpwd.setText(config.getProperty("userpwd"));

        JPanel formPane = new JPanel();
        GroupLayout lo = new GroupLayout(formPane);
        formPane.setLayout(lo);

        lo.setAutoCreateGaps(true);
        lo.setAutoCreateContainerGaps(true);

        GroupLayout.Alignment align = GroupLayout.Alignment.LEADING;
        lo.setHorizontalGroup(lo.createSequentialGroup()
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(labUsername).addComponent(labUserpwd))
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(txtUsername)
                .addComponent(txtUserpwd)));

        align = GroupLayout.Alignment.BASELINE;
        lo.setVerticalGroup(lo.createSequentialGroup()
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(labUsername)
                    .addComponent(txtUsername))
                .addGroup(lo.createParallelGroup(align)
                    .addComponent(labUserpwd)
                .addComponent(txtUserpwd)));

        JPanel ctrlPane = new JPanel(new FlowLayout());

        btnApply = new JButton("Appliquer", new ImageIcon("img/apply.png"));
        btnRestore = new JButton("Restaurer", new ImageIcon("img/restore.png"));
        btnApply.setMnemonic(KeyEvent.VK_A);
        btnRestore.setMnemonic(KeyEvent.VK_R);

        ctrlPane.add(btnApply);
        ctrlPane.add(btnRestore);

        add(formPane, BorderLayout.CENTER);
        add(ctrlPane, BorderLayout.SOUTH);
    }

    /**
     * Mise en place des listeners
     */
    private void affectEventListeners() {
        btnApply.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                saveConfig();
            }
        });

        btnRestore.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                txtUsername.setText(config.getProperty("username"));
                txtUserpwd.setText(config.getProperty("userpwd"));
            }
        });
    }

    /**
     * Sauvegarde de la configuration
     */
    private void saveConfig() {
        // Création d'un nouvel utilisateur
        // + vérification de la validité des données
        User user = new User(txtUsername.getText(), new String(txtUserpwd.getPassword()));
        if (!user.isValid()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Nom d'utilisateur ou le mot de passe incorrect\n"
                    + "ou problème de connexion réseau",
                    "Utilisateur invalid",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Mise à jour des informations
        config.setProperty("username", txtUsername.getText());
        config.setProperty("userpwd", new String(txtUserpwd.getPassword()));

        // Sauvegarde de la nouvelle configuration dans le fichier de configuration
        try {
            ostream = new BufferedOutputStream(new FileOutputStream(Config.CONFIG_FILENAME));
            config.storeToXML(ostream, null);
            ostream.close();
            JOptionPane.showMessageDialog(null, "Compte utilisateur enregistré", null, JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "IOException", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
