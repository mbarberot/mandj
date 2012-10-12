package remote;

import forme.Forme;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;


public class ProcessusImpl implements IProcessus {

	/* Identifiant du processus */
	private int pId;
	
	/* La liste des autres processus participants */
	private ArrayList<Integer> voisins;
	
	/* Etat actuel du whiteboard*/
	private ArrayList<Forme> whiteBoard;
	
	/* Indique le processus maître */
	private int masterId;
	
	/* Stub pour appeler les fcts de réseau*/
	private IReseau reso;
	
	/* Mutex pour le whiteboard */
	private Semaphore semWB;
	
	public void send() {

	}

	public void recv() {

		
	}

}
