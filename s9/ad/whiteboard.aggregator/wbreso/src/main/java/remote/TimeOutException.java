package remote;

import remote.messages.Message;

/**
 * Exception levée en cas de non réponse d'un processus
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public class TimeOutException extends Exception
{
    private Message mError;

    public TimeOutException(Message m)
    {
    	super("Erreur sur le message " + m.toString());
        this.mError = m;
    }

    public TimeOutException(String msg)
    {
        super(msg);
    }

    public Message getMError()
    {
        return this.mError;
    }
}
