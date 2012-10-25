package remote;

/**
 * Exception levée en cas de non réponse d'un processus
 * 
 * @author Mathieu Barberot et Joan Racenet
 */
public class TimeOutException extends Exception
{

	private int idProc;
    public TimeOutException(int proc)
    {
    	this.idProc = proc;
    }

    public TimeOutException(String msg)
    {
        super(msg);
    }
    
    public int getProc()
    {
    	return this.idProc;
    }
}
