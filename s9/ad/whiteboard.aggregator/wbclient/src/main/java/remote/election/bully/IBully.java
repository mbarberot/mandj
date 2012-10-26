package remote.election.bully;

/**
 * Interface pour manipuler l'algorithme d'élection
 * @author Mathieu Barberot et Joan Racenet
 */
public interface IBully
{
    public void accepteElection(int j);
    public void accepteAck();
    public void accepteCoor(int j);
}
