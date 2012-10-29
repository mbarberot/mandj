package remote.election;

import remote.messages.Message;

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
    public void accepteMessage(Message m);
}
