package db;

import java.util.HashMap;

/**
 * Informations statiques sur les tables
 * @author Thorisoka
 */
public class Tables {
	
	/** Liste des TABLE => ID_TABLE */
	public static final HashMap<String,String> ids = listeTables();
	
	/**
	 * Liste des TABLE => QUOTES
	 * QUOTE est un tableau listant les champs d'un table,
	 * avec <code>true<code> si le champs doit être quoté (TEXT/VARCHAR)
	 */
	public static final HashMap<String,boolean[]> quotes = listeQuotes();
	
	/**
	 * Liste des TABLE => NULL
	 * NULL est un tableau listant les champs d'un table,
	 * avec <code>true<code> si une chaine vide du webservice correspond à NULL.
	 */
	public static final HashMap<String,boolean[]> nullIfEmpty = listeNullIfEmpty();
	
	
	
	
	private static HashMap<String, String> listeTables() {
		HashMap<String, String> tables = new HashMap<String, String>();
		tables.put("BD_AUTEUR", "ID_AUTEUR");
		tables.put("BD_COLLECTION", "ID_COLLECTION");
		tables.put("BD_EDITEUR", "ID_EDITEUR");
		tables.put("BD_EDITION", "ID_EDITION");
		tables.put("BD_GENRE", "ID_GENRE");
		tables.put("BD_SERIE", "ID_SERIE");
		tables.put("BD_TOME", "ID_TOME");
		
		//tables.put("USERS_ALBUM", "USER_ID");
		//tables.put("USERS_EXCLUSIONS", "USER_ID");
		return tables;
	}
	
	
	
	/**
	 * Liste, pour chaque table, les champs ou on doit ajouter des quotes (VARCHAR)
	 */
	private static HashMap<String,boolean[]> listeQuotes() {
		
		HashMap<String,boolean[]> quotes = new HashMap<String,boolean[]>();
		
		boolean[] BD_AUTEUR = {false,true,true,true,false,false,false,true,
				true,true,true,true};
		boolean[] BD_COLLECTION = {false,true,false};
		boolean[] BD_EDITEUR = {false,true,true};
		boolean[] BD_EDITION = {false,false,true,false,false,true,true,true,true,true,
				true,true,false,true,false,false,true};
		boolean[] BD_GENRE = {false,true,true};
		boolean[] BD_SERIE = {false,true,false,false,false,false,false,true,true};
		boolean[] BD_TOME = {false,true,false,false,true,false,false,false,false,false,
				false,false,false,false,true,false,true,false,true,true,false,false,
				true,true};
		
		boolean[] USERS_ALBUM = {/*false,*/false,false,false,false,false,false,false,false,
				true,true,true,true,true,true,false,true,true,true,false,true};
		
		quotes.put("BD_AUTEUR", BD_AUTEUR);
		quotes.put("BD_COLLECTION", BD_COLLECTION);
		quotes.put("BD_EDITEUR", BD_EDITEUR);
		quotes.put("BD_EDITION", BD_EDITION);
		quotes.put("BD_GENRE", BD_GENRE);
		quotes.put("BD_SERIE", BD_SERIE);
		quotes.put("BD_TOME", BD_TOME);
		
		quotes.put("USERS_ALBUM", USERS_ALBUM);
		
		return quotes;
	}
	
	
	
	/**
	 * Liste, pour chaque table, les champs qui sont à NULL en cas de chaine vide.
	 * En cas de chaine, NULL est ignoré (passé en chaine vide)
	 * Pour le reste (en cas de Date par exemple), déjà Null, sinon Quotes
	 */
	private static HashMap<String,boolean[]> listeNullIfEmpty() {
		
		HashMap<String,boolean[]> nullIfEmpty = new HashMap<String,boolean[]>();
		
		boolean[] BD_AUTEUR = {false,false,false,false,true,true,true,false,
				true,true,false,false};
		boolean[] BD_COLLECTION = {false,false,false};
		boolean[] BD_EDITEUR = {false,false,false};
		boolean[] BD_EDITION = {false,false,false,false,false,true,false,false,
				false,false,false,false,true,true,false,true,true};
		boolean[] BD_GENRE = {false,false,false};
		boolean[] BD_SERIE = {false,false,false,false,true,true,false,false,false};
		boolean[] BD_TOME = {false,false,true,false,true,false,false,false,false,false,
				false,false,false,true,false,false,false,true,false,false,true,false,
				false,false};
		
		boolean[] USERS_ALBUM = {/*false,*/false,true,true,true,false,true,true,true,true,
			true,true,true,false,true,true,true,false,true,true,false};
		
		nullIfEmpty.put("BD_AUTEUR", BD_AUTEUR);
		nullIfEmpty.put("BD_COLLECTION", BD_COLLECTION);
		nullIfEmpty.put("BD_EDITEUR", BD_EDITEUR);
		nullIfEmpty.put("BD_EDITION", BD_EDITION);
		nullIfEmpty.put("BD_GENRE", BD_GENRE);
		nullIfEmpty.put("BD_SERIE", BD_SERIE);
		nullIfEmpty.put("BD_TOME", BD_TOME);
		
		nullIfEmpty.put("USERS_ALBUM", USERS_ALBUM);
		
		return nullIfEmpty;
	}
}




