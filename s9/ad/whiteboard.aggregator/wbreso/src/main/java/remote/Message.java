package remote;

/**
 * Classe définissant les types de messages
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public class Message
{	
    
	private int idFrom;
	private TypeMessage type;
	private int idTo;
	private Object data;
	
    public Message(int idFrom, TypeMessage typeMessage, int idTo, Object data)
    {
    	this.idFrom = idFrom;
    	this.type = typeMessage;
    	this.idTo = idTo;
    	this.data = data;
    }

	/**
	 * Getters
	 * @return
	 */
	public int getIdFrom() {
		return idFrom;
	}

	public TypeMessage getType() {
		return type;
	}

	public int getIdTo() {
		return idTo;
	}
	
	public Object getData() {
		return data;
	}
	
	public String toString()
	{
		return "Message FROM : " + idFrom + " TO : " + idTo + " De type " + type;
 	}

}
