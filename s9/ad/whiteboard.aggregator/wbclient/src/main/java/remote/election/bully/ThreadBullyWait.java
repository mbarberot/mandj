package remote.election.bully;

/**
 * Thread d'attente de Bully.
 * Permet l'attente d'un message ACK et COOR.
 * Le thread notifie l'objet Bully de la fin de son attente.
 * 
 * @author Mathieu Barberot et Joan Racenet
 */
public class ThreadBullyWait extends Thread
{
    private Bully bully;
    private boolean ack;
    private long time;
    
    public ThreadBullyWait(Bully bully)
    {
        this.bully = bully;
        this.ack = true;
        this.time = 120000; 
    }
    
    @Override
    public void run()
    {
        if (ack)
        {
            synchronized (this)
            {
                try
                {
                    // TODO : println
                    System.out.println("[ELECTION] Attente d'un message ACK");
                    this.wait(time);
                }
                catch (InterruptedException ex)
                {
                    ex.printStackTrace();
                }
            }
            bully.doneWaitingForACK();
        }
        else
        {
            synchronized (this)
            {
                try
                {
                    // TODO : println
                    System.out.println("[ELECTION] Attente du nouveau maitre");
                    this.wait();
                }
                catch (InterruptedException ex)
                {
                    ex.printStackTrace();
                }
            }
            bully.doneWaitingForCOOR();
        }
    }

    public synchronized void waitForACK(long time)
    {
        this.ack = true;
        this.time = time;
        this.start();
    }
    
    public synchronized void waitForCOOR()
    {
        this.ack = false;
        this.start();
    }
    
    public synchronized void notifyACK()
    {
        if(ack)
        {
            this.notifyAll();
        }
    }
    
    public synchronized void notifyCOOR()
    {
        if(!ack)
        {
            this.notifyAll();
        }
    }
    
}
