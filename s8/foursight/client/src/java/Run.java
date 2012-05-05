
import java.util.Calendar;
import java.util.Date;



public class Run
{
    private IA ia;
    private boolean running;
    
    public Run ()
    {      
	ia = new IA();
        running = false;
    }
    
    public void cancel()
    {
        if(ia.isRunning())
        {
            ia.cancel();
        }
    }
    
    public void close()
    {
        ia.end();
    }
    
    public int getCoup()
    {
        return ia.getCoup();
    }
    
    public void run()
    {
	if(ia == null) { ia = new IA(); }
        long stamp = Calendar.getInstance().getTimeInMillis();
        long max = stamp + 6000;
        running = true;
        ia.start();
        try
        {
            Thread.sleep(500);
        } catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }
        
        while((stamp < max) && isRunning())
        {
            stamp = Calendar.getInstance().getTimeInMillis();
        }
        
        if(running) 
        { 
            ia.interrupt();
        }
        ia = new IA();
    }
    
    public void setPlateau(int[] plateau)
    {
        ia.setPlateau(plateau);
    }
    
    public boolean isRunning()
    {
        running = ia.isRunning();
        return running;
    }
}
