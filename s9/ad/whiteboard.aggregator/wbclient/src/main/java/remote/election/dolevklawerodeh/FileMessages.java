package remote.election.dolevklawerodeh;

import java.util.LinkedList;
import java.util.Queue;
import remote.messages.ElectionMessage;

/**
 * Classe de traitement d'une file de messages reçus.
 *
 * @author Mathieu Barberot et Joan Racenet
 */
public class FileMessages extends Thread
{

    /**
     * Objet parent
     */
    private DolevKlaweRodeh dkr;
    /**
     * File des messages
     */
    private Queue<ElectionMessage> messagesRecu;

    /**
     * Constructeur
     *
     * @param dkr Objet parent
     */
    public FileMessages(DolevKlaweRodeh dkr)
    {
        this.dkr = dkr;
        this.messagesRecu = new LinkedList<ElectionMessage>();
    }

    @Override
    public void run()
    {
        while (messagesRecu.isEmpty())
        {

            try
            {
                synchronized (this)
                {
                    this.wait();
                }
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
        }

        while (!messagesRecu.isEmpty())
        {
            try
            {
                Thread.sleep(5000);
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
            dkr.accepteMessage(messagesRecu.poll());
        }
    }

    /**
     * Ajoute un message à la file
     *
     * @param m Le message
     */
    public synchronized void addMessage(ElectionMessage m)
    {
        this.messagesRecu.offer(m);
        notifyAll();
    }
}
