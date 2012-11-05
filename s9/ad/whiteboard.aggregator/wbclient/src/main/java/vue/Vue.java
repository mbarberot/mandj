package vue;

import controleur.Controleur;
import forme.Forme;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import javax.swing.*;
import modele.ModeleListener;
import vue.listeners.ActionColor;
import vue.listeners.ActionForme;
import vue.listeners.ChangeSpinner;
import vue.listeners.TableauBlancListener;

/**
 * La vue dans le patron de conception MVC
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public class Vue extends JFrame implements ModeleListener, WindowListener
{

    /**
     * Contrôleur à contacter lors d'évènements
     */
    private Controleur ctrl;
    /**
     * Liste des formes à dessiner
     */
    private ArrayList<Forme> listeFormes;
    //
    // Elements d'interfaces
    //  > Formes
    private JToggleButton pix;
    private JToggleButton drt;
    private JToggleButton rec;
    private JToggleButton ell;
    //  > Couleurs
    private JButton bg;
    private JButton fg;
    //  > Spinner
    private JLabel lab_trait;
    private JSpinner trait;
    //  > Canvas
    private TableauBlanc canvas;

    /**
     * Constructeur
     *
     * @param ctrl Le contrôleur à contacter
     */
    public Vue(Controleur ctrl)
    {
        super("Tableau blanc collaboratif");
        this.ctrl = ctrl;
        this.listeFormes = new ArrayList<Forme>();

        makeIHM();
    }

    /**
     * Construction de l'IHM
     */
    private void makeIHM()
    {
        JPanel mainPanel = new JPanel();

        GroupLayout layout = new GroupLayout(mainPanel);
        mainPanel.setLayout(layout);

        makeElements();

        JPanel formesPanel = new JPanel(new GridLayout(4, 1));
        
        ButtonGroup group = new ButtonGroup();
        group.add(pix);
        group.add(drt);
        group.add(rec);
        group.add(ell);
        
        formesPanel.add(pix);
        formesPanel.add(drt);
        formesPanel.add(rec);
        formesPanel.add(ell);

        JPanel colorPanel = new JPanel(new GridLayout(1, 2));
        colorPanel.add(fg);
        colorPanel.add(bg);

        // Alignement Horizontal
        GroupLayout.SequentialGroup hgroup = layout.createSequentialGroup();

        hgroup.addGroup(layout.createParallelGroup().addComponent(formesPanel).addComponent(lab_trait).addComponent(trait).addComponent(colorPanel)).addGroup(layout.createParallelGroup().addComponent(canvas));
        layout.setHorizontalGroup(hgroup);

        // Alignement vertical
        GroupLayout.ParallelGroup vgroup = layout.createParallelGroup();

        vgroup.addGroup(layout.createSequentialGroup().addComponent(formesPanel).addGroup(layout.createSequentialGroup().addComponent(lab_trait).addComponent(trait)).addComponent(colorPanel)).addComponent(canvas);
        layout.setVerticalGroup(vgroup);

        addWindowListener(this);
        this.add(mainPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        

    }

    /**
     * Initialisation de l'IHM
     */
    private void makeElements()
    {
        //
        // Les boutons des formes
        //
        this.pix = new JToggleButton("Pixel");
        this.drt = new JToggleButton("Droite");
        this.rec = new JToggleButton("Rectangle");
        this.ell = new JToggleButton("Ellipse");

        pix.addActionListener(new ActionForme(ctrl, Controleur.PIX));
        drt.addActionListener(new ActionForme(ctrl, Controleur.DRT));
        rec.addActionListener(new ActionForme(ctrl, Controleur.REC));
        ell.addActionListener(new ActionForme(ctrl, Controleur.ELL));

        //
        // Selection de l'épaisseur du trait
        //
        this.lab_trait = new JLabel("Épaisseur du trait :");
        this.trait = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));

        trait.addChangeListener(new ChangeSpinner(ctrl));

        //
        // Selection des couleurs
        //
        this.bg = new JButton("Arrière plan");
        this.fg = new JButton("Couleur du trait");

        bg.addActionListener(new ActionColor(ctrl, Controleur.BG, Color.WHITE));
        fg.addActionListener(new ActionColor(ctrl, Controleur.FG, Color.BLACK));

        //
        // Le tableau blanc
        //
        this.canvas = new TableauBlanc(this);

        canvas.addMouseListener(new TableauBlancListener(ctrl));
    }

    /**
     * Getter sur la liste des formes locales à la vue
     *
     * @return Liste des formes actuelles
     */
    public ArrayList<Forme> getListeFormes()
    {
        return listeFormes;
    }

    /**
     * Implémentation du ModeleListener : Mise à jour de la liste des formes et
     * actualisation du tableau blanc
     *
     * @param f
     */
    public void redessine(ArrayList<Forme> f)
    {
        this.listeFormes = f;
        this.canvas.repaint();
    }

	public void windowActivated(WindowEvent arg0) {
	}

	public void windowClosed(WindowEvent arg0) {
		ctrl.quitterWB();	
	}

	public void windowClosing(WindowEvent arg0) {
		ctrl.quitterWB();
	}

	public void windowDeactivated(WindowEvent arg0) {
	}

	public void windowDeiconified(WindowEvent arg0) {
	}

	public void windowIconified(WindowEvent arg0) {
	}

	public void windowOpened(WindowEvent arg0) {
	}
}
