package remote;

import remote.messages.Message;

public class ThreadAjoutFile implements Runnable{

	Message toAdd;
	ThreadTraitementMessages file;
	public ThreadAjoutFile(Message m, ThreadTraitementMessages file)
	{
		this.toAdd = m;
		this.file = file;
	}
	public void run() {
		/*
         * Attente avant traitement
         */
		
        int desync_time = (int) (Math.random() * 7000 + 3000);
        System.out.println("Message de " + toAdd.getIdFrom() + " à " + toAdd.getIdTo() + " de type " + toAdd.getType() 
        		+ " en attente pour " + desync_time + " ms");
        try
        {
            Thread.sleep(desync_time);
        }
        catch (InterruptedException e2)
        {
            e2.printStackTrace();
        }
        
		this.file.ajoutNouveauMessage(this.toAdd);
	}

}
