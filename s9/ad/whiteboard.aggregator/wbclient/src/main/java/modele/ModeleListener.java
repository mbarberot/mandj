package modele;

import forme.Forme;
import java.util.ArrayList;

/**
 * Interface pour les vues qui souhaitent observer le modele
 * 
 * @author Mathieu Barberot et Joan Racenet
 */
public interface ModeleListener
{
    public void redessine(ArrayList<Forme> f);
}
