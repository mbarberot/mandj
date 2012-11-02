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
    
    @Override
    public String toString()
    {
        String algorithme = "Algorithme : ";
        
        switch(this.algo)
        {
            case BULLY : 
                algorithme += "BULLY";
                break;
                
            case CHANG_ROBERTS :
                algorithme += "CHANG et ROBERTS";
                break;
                
            case DOLEV_KLAWE_RODEH :
                algorithme += "DOLEV, KLAWE et RODEH";
                break;
        }
        
        String message = "Message : ";
        
        switch(this.electionMsg)
        {
            case BULLY_ACK : 
                message += "ACK";
                break;
            case BULLY_COOR :
                message += "COOR";
                break;
            case BULLY_ELECTION :
                message += "ELECTION";
                break;
                
            case CR_TOK :
                message += "<TOK," + ((Integer)getData()) + ">";
                break;
                
            case DKR_ONE :
                message += "<ONE," + ((Integer)getData()) + ">";
                break;
            case DKR_SMALL :
                message += "<SMALL," + ((Integer)getData()) + ">";
                break;
            case DKR_TWO :
                message += "<TWO," + ((Integer)getData()) + ">";
                break;
        }
        
        return message;
    } 
           
}
