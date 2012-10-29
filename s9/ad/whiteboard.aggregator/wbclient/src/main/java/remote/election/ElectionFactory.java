package remote.election;

import java.rmi.RemoteException;
import remote.IProcessus;
import remote.IReseau;
import remote.Processus;
import remote.election.bully.Bully;

/**
 * Factory d'instanciation d'un algo d'élection
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public class ElectionFactory
{

    public static IElection createAlgoElection(String algo, IReseau reso, IProcessus processus, Processus parent, int id) throws RemoteException
    {
        if (algo.equalsIgnoreCase("bully"))
        {
            return new Bully(reso, processus, parent, id);
        }
        else if (algo.equalsIgnoreCase("chan_roberts"))
        {
            // TODO : return new ChanRoberts
        }
        else if (algo.equalsIgnoreCase("dolev_klaw_rodeh"))
        {
            // TODO : return new DolevKlaweRodeh
        }

        System.out.println("Erreur : " + algo + "non implémenté");
        return null;

    }
}
