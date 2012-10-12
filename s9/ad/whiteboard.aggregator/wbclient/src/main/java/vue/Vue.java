package vue;

import controleur.Controleur;
import forme.Forme;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * La vue dans le patron de conception MVC
 * 
 * @author Mathieu Barberot et Joan Racenet
 */
public class Vue extends JFrame 
{
    private Controleur ctrl;
    
    private JButton pix;
    private JButton drt;
    private JButton rec;
    private JButton ell;
    
    private JButton bg;
    private JButton fg;
    
    private TableauBlanc canvas;
    
    
    public Vue ()
    {
        super("Tableau blanc collaboratif");
     
        this.setLayout(new BorderLayout());
        
        this.add(makeToolbar(), BorderLayout.WEST);
        
        this.canvas = new TableauBlanc(this);
        this.add(canvas, BorderLayout.EAST);
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        
        this.repaint();
    }
    
    private JPanel makeToolbar()
    {
        JPanel toolbar = new JPanel(new BorderLayout());
        
        JPanel panelFormes = new JPanel(new GridLayout(4,1));
        
        pix = new JButton("Pixel");
        drt = new JButton("Droite");
        rec = new JButton("Rectangle");
        ell = new JButton("Ellipse");
        
        panelFormes.add(pix);
        panelFormes.add(drt);
        panelFormes.add(rec);
        panelFormes.add(ell);
        
        
        JPanel panelColors = new JPanel(new GridLayout(2, 1));
        
        bg = new JButton("Arrière plan");
        fg = new JButton("Couleur du trait");
        
        panelColors.add(bg);
        panelColors.add(fg);
        
        
        
        toolbar.add(panelFormes, BorderLayout.NORTH);
        // TODO ajouter le selecteur d'epaisseur de ligne
        toolbar.add(panelColors, BorderLayout.SOUTH);
        
        return toolbar;
    }
    
    public ArrayList<Forme> getListeFormes()
    {
        return new ArrayList<Forme>();
    }
}
