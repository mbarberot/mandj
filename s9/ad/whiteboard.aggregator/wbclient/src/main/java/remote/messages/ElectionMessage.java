package remote.messages;

import java.io.Serializable;

/**
 * Classe contenant les informations 
 * 
 * @author Mathieu Barberot et Joan Racenet
 */
public class ElectionMessage extends Message implements Serializable
{
    public ElectionAlgorithm algo;
    public ElectionTypeMessage electionMsg;
    
    public ElectionMessage (int idFrom, int idTo, Object data, ElectionAlgorithm algo, ElectionTypeMessage electionMsg)
    {
        super(idFrom, TypeMessage.ELECTION, idTo, data);
        this.algo = algo;
        this.electionMsg = electionMsg;
        
    }
    
    public ElectionMessage (int idFrom, int idTo, ElectionAlgorithm algo, ElectionTypeMessage electionMsg)
    {
        this(idFrom, idTo, null, algo, electionMsg);
    }

    public ElectionAlgorithm getAlgo()
    {
        return algo;
    }

    public ElectionTypeMessage getElectionMsg()
    {
        return electionMsg;
    }
    
}
