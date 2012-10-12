package forme;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Classe permettant de dessiner une droite
 * 
 * @author Mathieu Barberot et Joan Racenet
 */
public class Droite extends Forme
{
    private Point p1;
    private Point p2;
    
    /**
     * Constructeur d'une droite
     * @param p Les points de la droite (deux points suffisent)
     * @param bg La couleur du fond
     * @param fg La couleur des bords
     * @param epaisseur L'épaisseur des bords
     */
    public Droite(Point[] points, Color bg, Color fg, float epaisseur)
    {
        super(bg, fg, epaisseur);
        this.p1 = new Point(points[0]);
        this.p2 = new Point(points[1]);   
    }
    
    @Override
    public void paintBackground(Graphics2D g)
    {
        g.drawLine(p1.x, p1.y, p2.x, p2.y);
    }

    @Override
    public void paintForeground(Graphics2D g)
    {
    }
    
}
