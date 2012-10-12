package vue;

import forme.Forme;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * Classe de dessin du tableau blanc
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public class TableauBlanc extends Canvas
{
    
    private Dimension dim;
    private Color bg;
    private Vue parent;

    public TableauBlanc(Vue v)
    {
        super();
        this.dim = new Dimension(255,255);
        this.bg = Color.WHITE;
        this.parent = v;
        
        this.setSize(dim); 
    }
    
    public void paint(Graphics g)
    {
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.clipRect(0, 0, dim.width, dim.height);
        
        // sauvegarde de la couleur courante.
        // Color sauve = g2d.getColor();
        
        // Le fond
        g2d.setColor(bg);
        g2d.fillRect(0, 0, dim.width, dim.height);
        
        // restauration de la couleur courante.
        // g2d.setColor(sauve);

        // Dessin des formes dans l'ordre
        ArrayList<Forme> formes = parent.getListeFormes();
        for(Forme f : formes)
        {
            f.paint(g2d);
        }
    }
}
