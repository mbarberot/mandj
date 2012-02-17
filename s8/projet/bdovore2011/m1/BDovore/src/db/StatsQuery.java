package db;

/**
 * Génère une requête de statistique
 * 
 * @author Thorisoka
 */
public class StatsQuery {
	
	
	public static String total() {
		return "SELECT COUNT(ID_EDITION) AS NBR FROM USERS_ALBUM";
	}
	
	public static String totalOwned() {
		return "SELECT COUNT(ID_EDITION) AS NBR FROM USERS_ALBUM" +
				" WHERE FLG_ACHAT IS NULL OR FLG_ACHAT = 'N'";
	}
	
	public static String totalWanted() {
		return "SELECT COUNT(ID_EDITION) AS NBR FROM USERS_ALBUM WHERE FLG_ACHAT = 'O'";
	}
	
	
	
	public static String genres() {
		String sql = "SELECT g.LIBELLE AS LIB, COUNT(ua.ID_EDITION) AS NBR, \n" +
			"100*COUNT(ua.ID_EDITION)/(SELECT COUNT(ID_EDITION) FROM USERS_ALBUM) AS PERCENT \n" +
			"FROM USERS_ALBUM ua \n" +
			"INNER JOIN BD_GENRE g ON g.ID_GENRE = ua.ID_GENRE \n" +
			"GROUP BY ua.ID_GENRE \n" +
			"ORDER BY NBR DESC";
		return sql;
	}
	
	
	public static String editeurs() {
		String sql = "SELECT e.NOM AS LIB, COUNT(ua.ID_EDITION) AS NBR, \n" +
		"100*COUNT(ua.ID_EDITION)/(SELECT COUNT(ID_EDITION) FROM USERS_ALBUM) AS PERCENT \n" +
		"FROM USERS_ALBUM ua \n" +
		"INNER JOIN BD_EDITEUR e ON e.ID_EDITEUR = ua.ID_EDITEUR \n" +
		"GROUP BY ua.ID_EDITEUR \n" +
		"ORDER BY NBR DESC";
		return sql;
	}
	
	
	public static String dessinateurs() {
		String sql = "SELECT e.PSEUDO AS LIB, COUNT(ua.ID_EDITION) AS NBR, \n" +
		"100*COUNT(ua.ID_EDITION)/(SELECT COUNT(ID_EDITION) FROM USERS_ALBUM) AS PERCENT \n" +
		"FROM USERS_ALBUM ua \n" +
		"INNER JOIN BD_AUTEUR e ON e.ID_AUTEUR = ua.ID_DESSIN \n" +
		"GROUP BY ua.ID_DESSIN \n" +
		"ORDER BY NBR DESC";
		return sql;
	}
	
	
	public static String scenaristes() {
		String sql = "SELECT e.PSEUDO AS LIB, COUNT(ua.ID_EDITION) AS NBR, \n" +
		"100*COUNT(ua.ID_EDITION)/(SELECT COUNT(ID_EDITION) FROM USERS_ALBUM) AS PERCENT \n" +
		"FROM USERS_ALBUM ua \n" +
		"INNER JOIN BD_AUTEUR e ON e.ID_AUTEUR = ua.ID_SCENAR \n" +
		"GROUP BY ua.ID_SCENAR \n" +
		"ORDER BY NBR DESC";
		return sql;
	}
}
