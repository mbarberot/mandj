package remote;

/**
 * Exception levée en cas de non réponse d'un processus
 * 
 * @author Mathieu Barberot et Joan Racenet
 */
public class TimeOutException extends Exception
{

    public TimeOutException()
    {
    }

    public TimeOutException(String msg)
    {
        super(msg);
    }
}
