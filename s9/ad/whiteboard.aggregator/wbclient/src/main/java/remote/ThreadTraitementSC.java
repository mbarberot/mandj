package remote;

import java.rmi.RemoteException;
import java.util.LinkedList;

public class ThreadTraitementSC extends Thread {

	/* Liste des demandes (ordonnées) d'accès à la SC */
	// TODO contiendra des Messages avec estampille
	private LinkedList<Integer> demandesAccesSC;

	/* Stub du serveur pour les envois de message */
	private IReseau stub;

	private int masterId;
		
	public ThreadTraitementSC(IReseau nStub, int mId) {
		this.stub = nStub;
		this.masterId = mId;
		this.demandesAccesSC = new LinkedList<Integer>();
	}

	/** Ajout d'une demande dans la liste */
	public synchronized void ajoutDemande(int pid) {
		demandesAccesSC.add(pid);
		// TODO trier la file par estampille
		if (demandesAccesSC.size() == 1) {
			notifyAll();
		}
	}

	/** Traitement de la liste */
	public void run() {
		while (true) {
			// On bloque le traitement si la file d'attente est vide
			synchronized(this)
			{
				while (demandesAccesSC.size() == 0) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				Integer proc;
				while((proc = demandesAccesSC.pollFirst()) != null)
				{
					try {
						stub.sendTo(masterId, proc, Message.AUTORISER_ACCES_SC, null);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

}
