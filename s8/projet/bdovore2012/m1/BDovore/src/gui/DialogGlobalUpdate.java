package gui;

import db.synch.Synch;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.Proxy;
import javax.swing.*;
import util.UpdateBaseListener;

/**
 * Fenêtre effectuant la mise à jour globale du client
 */
public class DialogGlobalUpdate extends JDialog implements UpdateBaseListener
{

    private static final long serialVersionUID = 1L;
    // Elements d'interface
    private JLabel updateMsg;
    private JButton btnStart;
    private JProgressBar progbar;
    // Proxy à utiliser
    private Proxy proxy;
    // Objet utilisé pour la mise à jour (cf WebService & WSDL)
    private Synch synch;
    private boolean started;

    /**
     * Constructeur
     *
     * @param owner Fenêtre parente
     * @param modal
     * @param p Proxy pour la connexion au serveur
     * @param up Updater pour l'utilisation du webservice
     */
    public DialogGlobalUpdate(Window owner, Dialog.ModalityType modal, Proxy p, Synch synch)
    {
        super(owner);
        

        this.proxy = p;
        this.synch = synch;
        this.started = false;

        this.updateMsg = new JLabel("Cliquer le bouton START pour commencer.");

        btnStart = new JButton("Start", new ImageIcon("img/restore.png"));
        btnStart.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ev)
            {
                if(!started)
                {
                    update();
                }
                else
                {
                    cancel(false);
                }
            }
        });

        progbar = new JProgressBar(SwingConstants.HORIZONTAL, 0, 100);
        progbar.setStringPainted(true);

        JPanel ctrlPane = new JPanel(new BorderLayout());
        ctrlPane.add(btnStart, BorderLayout.NORTH);
        ctrlPane.add(progbar, BorderLayout.SOUTH);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(updateMsg, BorderLayout.CENTER);
        contentPane.add(ctrlPane, BorderLayout.SOUTH);
        setContentPane(contentPane);

        addWindowListener(new WindowListener()
        {

            public void windowClosing(WindowEvent e)
            {
                cancel(true);
            }
            public void windowOpened(WindowEvent e) {}
            public void windowClosed(WindowEvent e) {}
            public void windowIconified(WindowEvent e) {}
            public void windowDeiconified(WindowEvent e) {}
            public void windowActivated(WindowEvent e) {}
            public void windowDeactivated(WindowEvent e) {}
        });

        setTitle("Mise à jour globale");
        setResizable(false);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    /**
     * Lance la mise à jour de la base de données.
     */
    private void update()
    {
        try
        {
            synch.updateGlobal(this);
            started = true;
            btnStart.setText("Stop");
            btnStart.setIcon(new ImageIcon("img/close.png"));
        } catch (Exception ex)
        {
            started = false;
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Vérifiez votre connexion réseau ou "
                    + "le serveur est momentanément indisponible",
                    "Mise à jour impossible",
                    JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    /**
     * Implémentation de l'interface UpdateBaseListener. Cette méthode permet de
     * résupérer le pourcentage d'avancement de la mise à jour de la base de
     * donnée et d'utiliser cette information pour mettre à jour la barre de
     * progression.
     *
     * @param pourcentage Pourcentage d'avancement de la mise à jour de la base
     */
    public void progression(int pourcentage)
    {
        progbar.setValue(pourcentage);
        if (pourcentage == 100)
        {
            JOptionPane.showMessageDialog(null, "Mise à jour terminée avec succès", "Félicitation", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            started = false;
        }
    }

    /**
     * Stoppe la mise à jour de la base de données.
     */
    public void cancel(boolean quit)
    {
        if (started)
        {
            synch.cancelGlobal();
            started = false;
            btnStart.setText("Start");
            btnStart.setIcon(new ImageIcon("img/restore.png"));   
        }
        
        JOptionPane.showMessageDialog(null, "Mise à jour interrompue", "Pensez à finir votre mise à jour plus tard !", JOptionPane.INFORMATION_MESSAGE);
        
        if(quit) { dispose(); }

    }
}
