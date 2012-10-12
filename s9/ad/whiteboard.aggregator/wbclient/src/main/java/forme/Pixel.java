package forme;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * Classe pour le dessin d'un pixel
 * @author Mathieu Barberot et Joan Racenet
 */
public class Pixel extends Forme
{
    /**
     * L'unique point du pixel
     */
    private Point point;
    
    /**
     * Constructeur d'un pixel
     * @param point Le point du pixel
     * @param bg La couleur d'arrière plan
     * @param fg La couleur d'avant plan (ne sera pas utilisée)
     * @param epaisseur Epaisseur du pixel
     */
    public Pixel(Point point, Color bg, Color fg, float epaisseur)
    {
        super(bg, fg, epaisseur);
        this.point = new Point(point);
    }

    @Override
    public void paintBackground(Graphics2D g)
    {
        int xa = (int) (point.x - epaisseur) ;
        int ya = (int) (point.y - epaisseur) ;
        int xb = (int) (point.x + epaisseur) ;
        int yb = (int) (point.y + epaisseur) ;
        
        int dx = xb - xa;
        int dy = yb - ya;
        
        g.fillRect(xa,ya,dx,dy);
    }

    @Override
    public void paintForeground(Graphics2D g)
    {
        // Nothing to do
    }

    
    
}
