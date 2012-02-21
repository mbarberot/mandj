package db;

import java.util.HashMap;

/**
 * Informations statiques sur les tables
 * @author Thorisoka
 */
public class Tables{
	
	/** Liste des TABLE => ID_TABLE*/
	public static final HashMap<String,String> ids = listeTables();
	
	/**
	 * Liste des TABLE => QUOTES
	 * QUOTE est un tableau listant les champs d'un table,
	 * avec <code>true<code> si le champs doit √™tre quot√© (TEXT/VARCHAR)
	 */
	public static final HashMap<String,boolean[]> quotes = listeQuotes();
	
	/**
	 * Liste des TABLE => NULL
	 * NULL est un tableau listant les champs d'un table,
	 * avec <code>true<code> si une chaine vide du webservice correspond √† NULL.
	 */
	public static final HashMap<String,boolean[]> nullIfEmpty = listeNullIfEmpty();
	
	/**
	 * Liste des TABLE => TABLE 
	 * avec la "valeur TABLE" devant etre mise ‡ jour avant la "cle TABLE"
	 * -->Pour l'instant pas utilisÈ car nous utilisons un tableau.
	 * 		A voir s'il est nÈccessaire de l'utiliser et comment.
	 */
	public static final HashMap<String,String> lstTablesDependance = listeTablesDependance();
	
	
	private static HashMap<String, String> listeTables() {
		HashMap<String, String> tables = new HashMap<String, String>();
		tables.put("GENRE", "ID_GENRE");
		tables.put("SERIE", "ID_SERIE");
		tables.put("AUTEUR", "ID_AUTEUR");
		tables.put("EDITEUR", "ID_EDITEUR");
		tables.put("TOME", "ID_TOME");
		tables.put("EDITION", "ID_EDITION");
		
		// Inutile pour l'update
		
		//tables.put("TJ_TOME_AUTEUR", "ID_TOME, ID_AUTEUR, ROLE");
		//tables.put("DETAILS_EDITION, "ID_EDITION");
		//tables.put("DETAILS_SERIE, "ID_SERIE");
		//tables.put("DETAILS_AUTEUR, "ID_AUTEUR");
		
		//tables.put("BD_USER", "ID_EDITION");
		//tables.put("TRANSACTION", "ID_EDITION");
		
		return tables;
	}
	
	private static HashMap<String, String> listeTablesDependance() {
		
		HashMap<String, String> tables = new HashMap<String, String>();

		tables.put("AUTEUR", "");
		tables.put("EDITEUR", "AUTEUR"); // pour avoir un ordre clair de m‡j
		tables.put("GENRE", "EDITEUR"); // pour avoir un ordre clair de m‡j
		tables.put("SERIE", "GENRE");
		tables.put("TOME", "SERIE");
		tables.put("EDITION", "TOME");
		
		
		return tables;
	}
	
	/**
	 * Liste, pour chaque table, les champs ou on doit ajouter des quotes (VARCHAR)
	 */
	private static HashMap<String,boolean[]> listeQuotes() {
		
		HashMap<String,boolean[]> quotes = new HashMap<String,boolean[]>();
		
		boolean[] AUTEUR = {false,true,true,true};
		boolean[] EDITEUR = {false,true,true};
		boolean[] EDITION = {false,false,true,false,false};
		boolean[] GENRE = {false,true};
		boolean[] SERIE = {false,true,false};
		boolean[] TOME = {false,true,false,false};
		
		boolean[] TJ_TOME_AUTEUR = {false,false,true};
		boolean[] DETAILS_EDITION = {false,true};
		boolean[] DETAILS_SERIE = {false,false,false,true};
		boolean[] DETAILS_AUTEUR = {false,};		
		
		boolean[] BD_USER = {false,false,false,false,false};
		boolean[] TRANSACTION = {false,false,false};
		
		quotes.put("AUTEUR", AUTEUR);
		quotes.put("EDITEUR", EDITEUR);
		quotes.put("EDITION", EDITION);
		quotes.put("GENRE", GENRE);
		quotes.put("SERIE", SERIE);
		quotes.put("TOME", TOME);
		
		quotes.put("TJ_TOME_AUTEUR", TJ_TOME_AUTEUR);
		quotes.put("DETAILS_EDITION", DETAILS_EDITION);
		quotes.put("DETAILS_SERIE", DETAILS_SERIE);
		quotes.put("DETAILS_AUTEUR", DETAILS_AUTEUR);
		
		quotes.put("BD_USER", BD_USER);
		quotes.put("TRANSACTION", TRANSACTION);
		
		return quotes;
	}
	
	
	
	/**
	 * Liste, pour chaque table, les champs qui sont √† NULL en cas de chaine vide.
	 * En cas de chaine, NULL est ignor√© (pass√© en chaine vide)
	 * Pour le reste (en cas de Date par exemple), d√©j√† Null, sinon Quotes
	 */
	private static HashMap<String,boolean[]> listeNullIfEmpty() {
		
		HashMap<String,boolean[]> nullIfEmpty = new HashMap<String,boolean[]>();
		
		boolean[] AUTEUR = {false,false,false,false};
		boolean[] EDITEUR = {false,false,false};
		boolean[] EDITION = {false,false,false,true,false};
		boolean[] GENRE = {false,false};
		boolean[] SERIE = {false,false,false};
		boolean[] TOME = {false,false,false,false};
		
		boolean[] TJ_TOME_AUTEUR = {false,false,false};
		boolean[] DETAILS_EDITION = {false,false};
		boolean[] DETAILS_SERIE = {false,false,false,true};
		boolean[] DETAILS_AUTEUR = {false,true,true,false};
		
		boolean[] BD_USER = {false,false,false,false,true};
		boolean[] TRANSACTION = {false,false,true};
		
		nullIfEmpty.put("AUTEUR", AUTEUR);
		nullIfEmpty.put("EDITEUR", EDITEUR);
		nullIfEmpty.put("EDITION", EDITION);
		nullIfEmpty.put("GENRE", GENRE);
		nullIfEmpty.put("SERIE", SERIE);
		nullIfEmpty.put("TOME", TOME);
		
		nullIfEmpty.put("TJ_TOME_AUTEUR", TJ_TOME_AUTEUR);
		nullIfEmpty.put("DETAILS_EDITION", DETAILS_EDITION);
		nullIfEmpty.put("DETAILS_SERIE", DETAILS_SERIE);
		nullIfEmpty.put("DETAILS_AUTEUR", DETAILS_AUTEUR);
		
		nullIfEmpty.put("BD_USER", BD_USER);
		nullIfEmpty.put("TRANSACTION", TRANSACTION);
		
		return nullIfEmpty;
	}
}




