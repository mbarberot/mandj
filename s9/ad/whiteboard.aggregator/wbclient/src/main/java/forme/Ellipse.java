package forme;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * Dessine une ellipse
 * 
 * @author Mathieu Barberot et Joan Racenet
 */
public class Ellipse extends Forme
{
    /**
     * Point 1
     */
    private Point p1;
    /**
     * Point 2
     */
    private Point p2;
    
    /**
     * Constructeur de l'ellipse
     * @param p Les points de l'ellipse (deux points suffisent)
     * @param bg La couleur du fond
     * @param fg La couleur des bords
     * @param epaisseur L'épaisseur des bords
     */
    public Ellipse(Point[] p, Color bg, Color fg, float epaisseur)
    {
        super(bg, fg, epaisseur);
        p1 = new Point(p[0]);
        p2 = new Point(p[1]);
    }

    @Override
    public void paintBackground(Graphics2D g)
    {
        int xa = Math.min(p1.x, p2.x);
        int ya = Math.min(p1.y, p2.y);
        
        int dx = Math.abs(p1.x - p2.x);
        int dy = Math.abs(p1.y - p2.y);
        
        g.fillOval(xa, ya, dx, dy);
    }

    @Override
    public void paintForeground(Graphics2D g)
    {
         int xa = Math.min(p1.x, p2.x);
        int ya = Math.min(p1.y, p2.y);
        
        int dx = Math.abs(p1.x - p2.x);
        int dy = Math.abs(p1.y - p2.y);
        
        g.drawOval(xa, ya, dx, dy);
    }
}
