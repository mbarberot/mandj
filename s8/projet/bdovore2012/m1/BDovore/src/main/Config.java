package main;

/**
 * Classe contenant des informations de configuration statiques et globales
 * @author Thorisoka
 */
public class Config {
	
	/** Page PHP (sans paramètres) pour la mise à jour */
	public static final String MAJ_SRC =
		"http://www.thorisoka.dnsdojo.com/BD/logiciel.maj.php";
	
	/** Page PHP (sans paramètres) pour la mise à jour utilisateur (site->logiciel) */
	public static final String USER_SRC =
		"http://www.thorisoka.dnsdojo.com/BD/logiciel.user.php";
	
	/** Page PHP (sans paramètres) pour les infos utilisateur */
	public static final String USER_ID_SRC =
		"http://www.thorisoka.dnsdojo.com/BD/logiciel.userid.php";
	
	/** Lien de base vers les couvertures */
	public static final String IMG_COUV_URL = "http://www.bdovore.com/images/couv/";
	
	/** Taille des couvertures */
	public static final int COUV_WIDTH = 180;
	public static final int COUV_HEIGHT = 250;
	
	public static final String COUV_PATH = "covers/";
	
	public static final String CONFIG_FILENAME = "config.xml";
}
