package forme;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * Dessine un rectangle
 * @author Mathieu Barberot et Joan Racenet
 */
public class Rectangle extends Forme
{
    /**
     * Premier point
     */
    private Point p1;
    
    /**
     * Second point
     */
    private Point p2;
    
    /**
     * Constructeur du rectangle
     * @param p Les points du rectangle (deux points opposés suffisent)
     * @param bg La couleur du fond
     * @param fg La couleur des bords
     * @param epaisseur L'épaisseur des bords
     */
    public Rectangle(Point[] p, Color bg, Color fg, float epaisseur)
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
        
        g.fillRect(xa, ya, dx, dy);
    }

    @Override
    public void paintForeground(Graphics2D g)
    {
        int xa = Math.min(p1.x, p2.x);
        int ya = Math.min(p1.y, p2.y);
        
        int dx = Math.abs(p1.x - p2.x);
        int dy = Math.abs(p1.y - p2.y);
        
        g.drawRect(xa, ya, dx, dy);
    }

    @Override
    public String makeItSendable()
    {
        return "REC:"
                + p1.toString() + ":"
                + p2.toString() + ":"
                + bg.toString() + ":"
                + fg.toString() + ":"
                + epaisseur
                ;
    }
    
    
}
