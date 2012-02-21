package db.update;

/**
 * "Exception" levée pour signaler la fin d'une mise à jour
 * Une MAJ est faite de la façon suivante :
 * - Ajouter des données par batch de X entrée
 * - Lorsque l'on arrive au bout (recu 0 entrées), une exception est levée
 * 
 * @author Thorisoka
 */
public class UpdateDoneException extends Exception {

	public UpdateDoneException() {
		super();
	}
	
}
