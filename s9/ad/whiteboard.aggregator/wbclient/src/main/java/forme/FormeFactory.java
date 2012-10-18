package forme;

import java.awt.Color;
import java.awt.Point;

/**
 * Factory pour créer une forme depuis les données reçues
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public class FormeFactory
{
    public static Forme createForme(String data) throws Exception
    {
        String elt[] = data.split(":");
        int len = elt.length;

        String forme = elt[0];

        if (forme.startsWith("PIX"))
        {
            return createPixel(elt);
        } else if (forme.startsWith("DRT"))
        {
            return createDroite(elt);
        } else if (forme.startsWith("REC"))
        {
            return createRectangle(elt);
        } else if (forme.startsWith("ELL"))
        {
            return createEllipse(elt);
        } else 
        {
            throw new Exception("Forme introuvable");
        }
        
    }

    private static Forme createPixel(String[] elt)
    {
        int i, xa, ya, rgb1, rgb2;
        float ep;
        Point p;
        Color bg, fg;


        i = 0;

        xa = Integer.parseInt(elt[i]);
        i++;
        ya = Integer.parseInt(elt[i]);
        i++;

        rgb1 = Integer.parseInt(elt[i]);
        i++;
        rgb2 = Integer.parseInt(elt[i]);
        i++;

        ep = Float.parseFloat(elt[i]);

        p = new Point(xa, ya);
        bg = new Color(rgb1);
        fg = new Color(rgb2);

        return new Pixel(p, bg, fg, ep);
    }

    
    private static Forme createDroite(String[] elt)
    {
        int i, xa, ya, xb, yb, rgb1, rgb2;
        float ep;
        Point p[], p1, p2;
        Color bg, fg;


        i = 0;

        xa = Integer.parseInt(elt[i]);
        i++;
        ya = Integer.parseInt(elt[i]);
        i++;
        xb = Integer.parseInt(elt[i]);
        i++;
        yb = Integer.parseInt(elt[i]);
        i++;

        rgb1 = Integer.parseInt(elt[i]);
        i++;
        rgb2 = Integer.parseInt(elt[i]);
        i++;

        ep = Float.parseFloat(elt[i]);

        p1 = new Point(xa, ya);
        p2 = new Point(xb, yb);
        p = new Point[]{p1,p2};
        
        bg = new Color(rgb1);
        fg = new Color(rgb2);

        return new Droite(p, bg, fg, ep);
    }

    private static Forme createRectangle(String[] elt)
    {
        int i, xa, ya, xb, yb, rgb1, rgb2;
        float ep;
        Point p[], p1, p2;
        Color bg, fg;


        i = 0;

        xa = Integer.parseInt(elt[i]);
        i++;
        ya = Integer.parseInt(elt[i]);
        i++;
        xb = Integer.parseInt(elt[i]);
        i++;
        yb = Integer.parseInt(elt[i]);
        i++;

        rgb1 = Integer.parseInt(elt[i]);
        i++;
        rgb2 = Integer.parseInt(elt[i]);
        i++;

        ep = Float.parseFloat(elt[i]);

        p1 = new Point(xa, ya);
        p2 = new Point(xb, yb);
        p = new Point[]{p1,p2};
        
        bg = new Color(rgb1);
        fg = new Color(rgb2);

        return new Rectangle(p, bg, fg, ep);
    }

    private static Forme createEllipse(String[] elt)
    {
        int i, xa, ya, xb, yb, rgb1, rgb2;
        float ep;
        Point p[], p1, p2;
        Color bg, fg;


        i = 0;

        xa = Integer.parseInt(elt[i]);
        i++;
        ya = Integer.parseInt(elt[i]);
        i++;
        xb = Integer.parseInt(elt[i]);
        i++;
        yb = Integer.parseInt(elt[i]);
        i++;

        rgb1 = Integer.parseInt(elt[i]);
        i++;
        rgb2 = Integer.parseInt(elt[i]);
        i++;

        ep = Float.parseFloat(elt[i]);

        p1 = new Point(xa, ya);
        p2 = new Point(xb, yb);
        p = new Point[]{p1,p2};
        
        bg = new Color(rgb1);
        fg = new Color(rgb2);

        return new Ellipse(p, bg, fg, ep);
    }
}
