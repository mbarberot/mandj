package remote.election;

import remote.Message;
import remote.election.message.ElectionMessage;

/**
 * Interface mère des algorithmes d'élection
 * @author Mathieu Barberot et Joan Racenet
 */
public interface IElection
{
    /**
     * Démarre l'algorithme d'éléction
     */
    public void demarrerElection();
    /**
     * Accepte le message
     * @param m Message
     */
    public void accepteMessage(ElectionMessage m);
}
