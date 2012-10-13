package forme;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Classe définissant une forme.
 * 
 * Reprise de la classe fournie sur Moodle
 * 
 * @author Mathieu Barberot et Joan Racenet
 */
public abstract class Forme implements Serializable
{
    /**
     * Couleur d'arrière plan
     */
    protected Color bg;
    /**
     * Couleur d'avant plan
     */
    protected Color fg;
    /**
     * Epaisseur des bords
     */
    protected float epaisseur;
    
    /**
     * Constructeur de base
     * @param bg Couleur d'arrière plan
     * @param fg Couleur d'avant plan
     * @param epaisseur Epaisseur des bords
     */
    public Forme(Color bg, Color fg, float epaisseur)
    {
        this.bg = bg;
        this.fg = fg;
        this.epaisseur = epaisseur;
    }
    
  
    /**
     * Dessine la forme sur le Canvas
     * @param g Outil graphique
     */
    public void paint(Graphics2D g)
    {
        Stroke s = g.getStroke();
        Color c = g.getColor();
        
        g.setStroke(new BasicStroke(epaisseur));
        g.setColor(bg);
        paintBackground(g);
        
        g.setColor(fg);
        paintForeground(g);
        
        g.setStroke(s);
        g.setColor(c);
        
    }
    
    /**
     * Dessine l'arrière plan de la forme
     * @param g Outil de dessin
     */
    public abstract void paintBackground(Graphics2D g);
    
    /**
     * Dessine l'avant plan de la forme
     * @param g Outil de dessin
     */
    public abstract void paintForeground(Graphics2D g);
            
    public byte[] toByteArray()
    {
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		byte[] serializedForme = null;
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(this);
			serializedForme = bos.toByteArray();
			bos.close();
			oos.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return serializedForme;
    }
    
    /**
     * Construction d'une forme à partir d'un flux binaire
     * @param baF tableau de byte codant une forme
     */
    public static Forme getFromByteArray(byte[] baF)
    {
    	ByteArrayInputStream bis = new ByteArrayInputStream(baF);
    	ObjectInput in = null;
    	Forme f = null;
    	try {
    	  in = new ObjectInputStream(bis);
    	  f = (Forme)in.readObject(); 
    	  bis.close();
    	  in.close();
    	} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    	
    	return f;
    }
}
