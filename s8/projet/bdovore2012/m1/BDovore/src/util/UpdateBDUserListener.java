package util;

import java.util.EventListener;

/**
 * Interface pour une utilisation du design pattern "Observer"
 * 
 * @author Barberot Mathieu et Racenet Joan
 */
public interface UpdateBDUserListener extends EventListener
{
    public void addRow(Object[] row);
}
