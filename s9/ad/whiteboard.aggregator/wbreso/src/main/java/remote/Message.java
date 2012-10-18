package remote;

public class Message {
	private int idFrom;
	private int idTo;
	private TypeMessage mess;
	
	public Message(int nIdFrom, int nIdTo, TypeMessage nMess)
	{
		this.idFrom = nIdFrom;
		this.idTo = nIdTo;
		this.mess = nMess;
	}

	public int getIdFrom() {
		return idFrom;
	}

	public int getIdTo() {
		return idTo;
	}


	public TypeMessage getMess() {
		return mess;
	}
	
	
	
	
}
