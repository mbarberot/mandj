package remote.election;

import remote.election.bully.Bully;
import java.rmi.RemoteException;
import java.util.ArrayList;
import remote.IProcessus;
import remote.IReseau;

/**
 * Factory d'instanciation d'un algo d'élection
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public class ElectionFactory
{

    public static IElection createAlgoElection(String algo, IReseau reso, IProcessus processus, ArrayList<Integer> voisins, int id) throws RemoteException
    {
        if (algo.equalsIgnoreCase("bully"))
        {
            return new Bully(reso, processus, voisins, id);
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
