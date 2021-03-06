package remote.election;

import remote.messages.Message;

/**
 * Interface mère des algorithmes d'élection
 *
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
     *
     * @param m Message
     */
    public void accepteMessage(Message m);

    /**
     * Récupération des timeouts
     *
     * @param idProc Processus déconnecté
     */
    public void timeout(int idProc);
    
    /**
     * Retourne si l'élection est en cours
     * 
     * @return Statut de l'élection : true = en cours, false sinon
     */
    public boolean isInElection();
    
    /**
     * Attend qu'un nouveau maitre soit élu
     */
    public void waitNewMaster();
          
}
