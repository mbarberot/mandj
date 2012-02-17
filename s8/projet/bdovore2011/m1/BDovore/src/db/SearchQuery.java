package db;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Génère une requête de recherche en fonction des paramètres.
 * On peut chercher selon un certain champs (Titre, Série, ISBN, etc.)
 * On cherche également selon un filtre (tout album, possédés, manquant)
 * 
 * @author Thorisoka
 */
public class SearchQuery {
	
	public static final String GET_FIELDS =
		"t.ID_TOME, t.TITRE, t.NUM_TOME, " +
		"e.ID_EDITEUR, e.ID_COLLECTION, " +
		"s.ID_SERIE, s.NOM, " +
		"g.ID_GENRE, g.LIBELLE, " +
		"t.FLG_INT, t.FLG_TYPE, " +
		"e.ISBN, e.EAN";
	
	public static final String GET_MAX = "COUNT(t.ID_TOME) AS NBR";
	
	
	public static final int SEARCH_IN_ALL = 0;
	public static final int SEARCH_IN_OWNED = 1;
	public static final int SEARCH_IN_MISSING = 2;
	
	private static final String[] searchInJoin = {
		"(e.ID_TOME = t.ID_TOME AND e.FLG_DEFAULT = 'O')",
		"e.ID_TOME = t.ID_TOME",
		"e.ID_TOME = t.ID_TOME"
	};
	private static final String[] searchInWhere = {
		"",
		" AND e.ID_EDITION IN (SELECT ID_EDITION FROM USERS_ALBUM)",
		
		" AND t.ID_SERIE IN (SELECT ID_SERIE FROM USERS_ALBUM) \n" +
		"AND e.ID_EDITION NOT IN (SELECT ID_EDITION FROM USERS_ALBUM)"
	};
	
	
	public static final int ORDER_ASC = 0;
	public static final int ORDER_DESC = 1;
	
	/* Champs disponiblees pour le ORDER BY :
	 * t.TITRE (nom album)
	 * t.NUM_TOME (numéro tome)
	 * s.NOM (nom série)
	 * g.LIBELLE (genre)
	 * e.ISBN
	 * e.EAN
	 */
	
	
	
	
	/**
	 * Recherche sans recherche : liste les albums a la volée
	 * @param limit
	 * @param offset
	 * @return
	 */
	public static String searchNothing(
			int searchIn, String type, String sortby, int order) {
		
		if (searchIn < 0 && searchIn > 2) return "";
		if (searchIn == SEARCH_IN_ALL) return ""; // Pas de liste totale
		
		
		String sql = "SELECT " + type + "\n" +
		"FROM BD_TOME t \n" +
		
		"INNER JOIN BD_SERIE s ON s.ID_SERIE = t.ID_SERIE \n" +
		"INNER JOIN BD_GENRE g ON g.ID_GENRE = t.ID_GENRE \n" +
		"INNER JOIN BD_EDITION e ON " + searchInJoin[searchIn] + "\n" +
		
		"WHERE 1" + searchInWhere[searchIn] + "\n" +

		(type.equals(GET_MAX) ? "" : orderBy(sortby,order)) + "\n";
		
		if (searchIn == SEARCH_IN_MISSING) System.out.println(sql);
		
		
		return sql;
	}
	
	
	
	public static String searchTitre(
			int searchIn, String search, String type, String sortby, int order) {
		
		if (searchIn < 0 && searchIn > 2) return "";
		
		// TODO :  Etoffer pour gerer chaque mot d'une recherche ?
		// (auto avec des word, ou a mano en coupant par espace ?)
		// ou check l'effet sous H2 (avec 1 ou plusieurs mots)
		if (search.length() < 3) return "";
		
		
		String sql = "SELECT " + type + "\n" +
		"FROM BD_TOME t, FTL_SEARCH_DATA('" + escape(search) + "',0,0) FT \n" +
		
		"INNER JOIN BD_SERIE s ON s.ID_SERIE = t.ID_SERIE \n" +
		"INNER JOIN BD_GENRE g ON g.ID_GENRE = t.ID_GENRE \n" +
		"INNER JOIN BD_EDITION e ON " + searchInJoin[searchIn] + "\n" +
		
		"WHERE FT.TABLE='BD_TOME' AND t.ID_TOME = FT.KEYS[0] \n" +
		searchInWhere[searchIn] + "\n" +
		
		(type.equals(GET_MAX) ? "" : orderBy(sortby,order)) + "\n";
		
		return sql;
	}
	
	
	
	public static String searchSerie(
			int searchIn, String search, String type, String sortby, int order) {
		
		if (searchIn < 0 && searchIn > 2) return "";
		if (search.length() < 3) return "";
		
		
		String sql = "SELECT " + type + "\n" +
		"FROM BD_TOME t, FTL_SEARCH_DATA('" + escape(search) + "', 0, 0) FT \n" +
		
		"INNER JOIN BD_SERIE s ON s.ID_SERIE = t.ID_SERIE \n" +
		"INNER JOIN BD_GENRE g ON g.ID_GENRE = t.ID_GENRE \n" +
		"INNER JOIN BD_EDITION e ON " + searchInJoin[searchIn] + "\n" +
		
		"WHERE FT.TABLE='BD_SERIE' AND s.ID_SERIE = FT.KEYS[0] \n" +
		searchInWhere[searchIn] + "\n" +
		
		(type.equals(GET_MAX) ? "" : orderBy(sortby,order)) + "\n";
		
		return sql;
	}
	
	
	
	public static String searchAuteur(
			int searchIn, String search, String type, String sortby, int order) {
		
		if (searchIn < 0 && searchIn > 2) return "";
		if (search.length() < 3) return "";
		
		
		String sql = "SELECT " + type + "\n" +
		
		"FROM BD_TOME t \n" +
		
		"INNER JOIN (SELECT ID_AUTEUR FROM BD_AUTEUR bda, " +
			"FTL_SEARCH_DATA('" + escape(search) + "', 0, 0) FT " +
			"WHERE FT.TABLE='BD_AUTEUR' AND bda.ID_AUTEUR = FT.KEYS[0]) a ON \n" +
		"(t.ID_SCENAR = a.ID_AUTEUR OR t.ID_SCENAR_ALT = a.ID_AUTEUR " +
		"OR t.ID_DESSIN = a.ID_AUTEUR OR t.ID_DESSIN_ALT = a.ID_AUTEUR " +
		"OR t.ID_COLOR = a.ID_AUTEUR OR t.ID_COLOR_ALT = a.ID_AUTEUR) \n" +
		
		"INNER JOIN BD_SERIE s ON s.ID_SERIE = t.ID_SERIE \n" +
		"INNER JOIN BD_GENRE g ON g.ID_GENRE = t.ID_GENRE \n" +
		"INNER JOIN BD_EDITION e ON " + searchInJoin[searchIn] + "\n" +
		
		"WHERE 1" + searchInWhere[searchIn] + "\n" +
		
		(type.equals(GET_MAX) ? "" : orderBy(sortby,order)) + "\n";
		
		return sql;
	}
	
	
	
	public static String searchISBN(
			int searchIn, String search, String type, String sortby, int order) {
		
		if (searchIn < 0 && searchIn > 2) return "";
		if (search.length() < 3) return "";
		
		
		String isbn = CodeBarre.toBDovoreISBN(escape(search));
		String ean = CodeBarre.toBDovoreEAN(escape(search));
		String eanisbn = isbn + " " + ean; // FullText prend tout
		
		String sql = "SELECT " + type + "\n" +
		"FROM BD_TOME t, FTL_SEARCH_DATA('" + eanisbn + "', 0, 0) FT \n" +
		
		"INNER JOIN BD_SERIE s ON s.ID_SERIE = t.ID_SERIE \n" +
		"INNER JOIN BD_GENRE g ON g.ID_GENRE = t.ID_GENRE \n" +
		"INNER JOIN BD_EDITION e ON " + searchInJoin[searchIn] + "\n" +
		
		"WHERE FT.TABLE='BD_EDITION' AND e.ID_EDITION = FT.KEYS[0] \n" +
		searchInWhere[searchIn] + "\n" +
		
		(type.equals(GET_MAX) ? "" : orderBy(sortby,order)) + "\n";
		
		return sql;
	}
	
	
	
	
	/**
	 * Echape et transforme les caractères pour H2
	 */
	private static String escape(String str) {
		return str.replace("'", "''").toLowerCase();
	}
	
	/**
	 * Encode la chaine avec l'algorithme MD5
	 * @param str
	 * @return
	 * TODO : Bogus
	 */
	public static String md5(String str) {
		
		String md5 = null;
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			
			md5 = new String(md.digest());
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return md5;
	}
	
	/**
	 * Génère la clause ORDER BY
	 */
	private static String orderBy(String field, int order) {
		if (order != 0 && order != 1) return "";
		return "ORDER BY " + field + " " + ((order == ORDER_ASC) ? "ASC" : "DESC");
	}
}
