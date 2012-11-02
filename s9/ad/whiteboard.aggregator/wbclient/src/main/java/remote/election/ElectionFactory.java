package remote.election;

import java.rmi.RemoteException;
import remote.IProcessus;
import remote.IReseau;
import remote.Processus;
import remote.election.bully.Bully;
import remote.election.changroberts.ChangRoberts;
import remote.election.dolevklawerodeh.DolevKlaweRodeh;

/**
 * Factory d'instanciation d'un algo d'élection
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public class ElectionFactory
{

    /**
     * Instancie un objet de type IElection
     * 
     * @param algo Algorithme à instancier
     * @param reso Interface de discussion avec le serveur Reseau
     * @param parent Processus parent
     * @param id ID du processus
     * @return Interface de manipulation de l'algorithme
     * @throws RemoteException 
     */
    public static IElection createAlgoElection(String algo, IReseau reso, Processus parent, int id) throws RemoteException
    {
        if (algo.equalsIgnoreCase("bully"))
        {
            return new Bully(reso, parent, id);
        }
        else if (algo.equalsIgnoreCase("chang_roberts"))
        {
            return new ChangRoberts(parent, reso, id);
        }
        else if (algo.equalsIgnoreCase("dolev_klawe_rodeh"))
        {
            return new DolevKlaweRodeh(parent, reso, id);
        }

        System.out.println("Erreur : " + algo + "non implémenté");
        return null;

    }
}
