package util;

import java.util.EventListener;

/**
 * Interface pour une utilisation du design pattern "Observer".
 * 
 * @author Barberot Mathieu et Racenet Joan
 */
public interface UpdateBaseListener extends EventListener
{
    public void progression(int pourcentage);
}
