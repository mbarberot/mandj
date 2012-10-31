package remote.election.changroberts;

/**
 * Interface de l'algorithme d'élection de Chan et Roberts
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public interface IChangRoberts
{

    /**
     * Accepte un token comportant la valeur j
     *
     * @param j Valeur du token
     */
    public void accepteTOK(int j);
}
