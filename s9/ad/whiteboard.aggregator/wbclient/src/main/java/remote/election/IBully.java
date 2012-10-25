package remote.election;

import java.rmi.Remote;

/**
 * Interface pour manipuler l'algorithme d'élection
 * @author Mathieu Barberot et Joan Racenet
 */
public interface IBully extends Remote
{
    public void accepteElection(int j);
    public void accepteAck();
    public void accepteCoor(int j);
}
