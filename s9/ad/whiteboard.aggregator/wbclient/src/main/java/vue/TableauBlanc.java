package vue;

import forme.Forme;
import java.awt.*;
import java.util.ArrayList;

/**
 * Classe de dessin du tableau blanc
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public class TableauBlanc extends Canvas
{

    /**
     * Dimension du Tableau
     */
    private Dimension dim;
    /**
     * Couleur de fond
     */
    private Color bg;
    /**
     * La vue qui contient le tableau
     */
    private Vue parent;

    /**
     * Constructeur
     *
     * @param v Le vue qui contient le tableau
     */
    public TableauBlanc(Vue v)
    {
        super();
        this.dim = new Dimension(255, 255);
        this.bg = Color.WHITE;
        this.parent = v;

        this.setSize(dim);
    }

    @Override
    public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.clipRect(0, 0, dim.width, dim.height);

        Color c = g2d.getColor();

        //
        // Le fond
        g2d.setColor(bg);
        g2d.fillRect(0, 0, dim.width, dim.height);

        g2d.setColor(c);

        //
        // Dessin des formes dans l'ordre
        ArrayList<Forme> formes = parent.getListeFormes();
        for (Forme f : formes)
        {
            f.paint(g2d);
        }
    }
}
