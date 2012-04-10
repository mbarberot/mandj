package util;

import java.util.EventListener;

/**
 *
 * @author kawa
 */
public interface UpdateBaseListener extends EventListener
{
    public void progression(int pourcentage);
}
