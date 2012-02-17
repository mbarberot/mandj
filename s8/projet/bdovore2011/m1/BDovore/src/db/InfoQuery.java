package db;


/**
 * Génère une requête de récupération des infos (auteurs/editions d'un album).
 * 
 * @author Thorisoka
 */
public class InfoQuery {
	
	private static final String AUTHORS_FIELDS =
		"a.ID_AUTEUR, a.PSEUDO, a.PRENOM, a.NOM";
	
	private static final String EDITIONS_FIELDS =
		"e.ID_EDITION, e.FLG_DEFAULT, e.ID_EDITEUR, e.ID_COLLECTION, " +
		"ed.NOM AS ED_NOM, e.DTE_PARUTION, c.NOM AS C_NOM, " +
		"e.ISBN, e.EAN, e.IMG_COUV, e.FLG_EO, e.FLG_TT, e.COMMENT";
	
	private static final String SERIE_FIELDS = 
		"s.ID_SERIE, s.NOM, g.LIBELLE, s.NB_TOME, s.FLG_FINI, s.HISTOIRE";
	
	
	
	
	
	public static String getAuteurs(int idTome, String src) {
		
		// Changer quand Auteurs en table de jointure.
		
		String sql = "SELECT " + AUTHORS_FIELDS + "\n" +
		"FROM BD_TOME t \n" +
		"INNER JOIN BD_AUTEUR a ON " +
		"(a.ID_AUTEUR = t.ID_"+src+" OR a.ID_AUTEUR = t.ID_"+src+"_ALT) \n" +
		"WHERE t.ID_TOME = " + idTome;
		
		return sql;
	}
	
	
	
	public static String getEditions(int idTome) {
		
		String sql = "SELECT " + EDITIONS_FIELDS + "\n" +
		"FROM BD_EDITION e \n" +
		"INNER JOIN BD_EDITEUR ed ON ed.ID_EDITEUR = e.ID_EDITEUR \n" +
		"INNER JOIN BD_COLLECTION c ON c.ID_COLLECTION = e.ID_COLLECTION \n" +
		"WHERE e.ID_TOME = " + idTome + "\n" +
		"ORDER BY e.DTE_PARUTION ASC";
		
		return sql;
	}
	
	
	
	public static String getEditionsPossedees(int idTome, int idEdition) {
		
		String sql = "SELECT FLG_ACHAT, FLG_DEDICACE, FLG_PRET FROM USERS_ALBUM " +
				"WHERE ID_TOME = " + idTome + " AND ID_EDITION = " + idEdition;
		
		return sql;
	}
	
	
	
	public static String getSerie(int idSerie) {
		
		String sql = "SELECT " + SERIE_FIELDS + "\n" +
		"FROM BD_SERIE s \n" +
		"INNER JOIN BD_GENRE g ON g.ID_GENRE = s.ID_GENRE \n" +
		"WHERE s.ID_SERIE = " + idSerie;
		
		return sql;
	}
}
