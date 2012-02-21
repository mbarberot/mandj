package db;


/**
 * GÃ©nÃ¨re une requÃªte de rÃ©cupÃ©ration des infos (auteurs/editions d'un album).
 * 
 * @author Thorisoka
 */
public class InfoQuery {
	
	private static final String AUTHORS_FIELDS =
		"a.ID_AUTEUR, a.PSEUDO, a.PRENOM, a.NOM";
	
	private static final String EDITIONS_FIELDS =
		"e.ID_EDITION, e.ID_EDITEUR, " +
		"ed.NOM_EDITEUR, e.DATE_PARUTION, " +
		"e.ISBN, e.FLG_DEFAULT, de.IMG_COUV";
	
	private static final String SERIE_FIELDS = 
		"s.ID_SERIE, s.NOM_SERIE, g.NOM_GENRE";
	
	
	public static String getAuteurs(int idTome, String src) {
				
		String sql = "SELECT "+ AUTHORS_FIELDS + "\n" +
					 "FROM TOME t \n"+
					 "LEFT OUTER JOIN TJ_TOME_AUTEUR tj on (t.ID_TOME = tj.ID_TOME) \n" +
					 "LEFT OUTER JOIN AUTEUR a on (tj.ID_AUTEUR = a.ID_AUTEUR) \n" +
					 "WHERE (t.ID_TOME = " + idTome + ") \n" +
					 "AND (tj.ROLE = '" + src + "');";
		
		//System.out.println(sql+"\n");
		
		return sql;
	}
	
	

	public static String getEditions(int idTome) {
		
		String sql = "SELECT " + EDITIONS_FIELDS + "\n" +
					 "FROM EDITION e \n" +
					 "LEFT OUTER JOIN DETAILS_EDITION de on e.ID_EDITION = de.ID_EDITION \n" +
					 "LEFT OUTER JOIN EDITEUR ed on e.ID_EDITEUR = ed.ID_EDITEUR \n" +
					 "WHERE e.ID_TOME = " + idTome + "\n" +
					 "ORDER BY e.DATE_PARUTION ASC ;";
		
		//System.out.println(sql+"\n");
		
		return sql;
	}
	
	public static String getEditionsPossedees(int idEdition) {
		
		String sql = "SELECT FLG_AACHETER, FLG_DEDICACE, FLG_PRET " +
					 "FROM BD_USER " +
					 "WHERE ID_EDITION = " + idEdition;
		
		//System.out.println(sql+"\n");
		
		return sql;
	}
	
	public static String getSerie(int idSerie) {
		
		String sql = "SELECT " + SERIE_FIELDS + "\n" +
					 "FROM SERIE s \n" +
					 "LEFT OUTER JOIN GENRE g ON g.ID_GENRE = s.ID_GENRE \n" +
					 "WHERE s.ID_SERIE = " + idSerie + ";";
		
		//System.out.println(sql+"\n");
		
		return sql;
	}
	
	/* Fonction qui sert a savoir si un tome a des details sur les auteurs 
	 * (soit l'user le possède, soit il l'a deja visité et les infos sur les auteurs existent
	 */
	public static String getAlbumVisited(int idAlbum){
	
		String sql = "SELECT COUNT(*) AS NBR\n" +
		 "FROM TOME t\n" +
		 "INNER JOIN TJ_TOME_AUTEUR tj ON tj.ID_TOME = t.ID_TOME \n" +
		 "WHERE t.ID_TOME = " + idAlbum + ";";

		//System.out.println(sql+"\n");
		
		return sql;
		
	}
	
	/* Fonction qui sert a savoir si une serie a des details 
	 * (si il a deja visité la fiche de la série, les details exitent deja)
	 */
	public static String getSerieVisited(int idSerie){
	
		String sql = "SELECT COUNT(*) AS NBR\n" +
		 "FROM Serie s\n" +
		 "INNER JOIN DETAILS_SERIE ds ON s.ID_SERIE = ds.ID_SERIE  \n" +
		 "WHERE s.ID_SERIE = " + idSerie + ";";

		//System.out.println(sql+"\n");
		
		return sql;
		
	}
	
	/* Fonction qui sert a savoir si une edition a des details
	 * (soit l'user la possède, soit il l'a deja visité et les details existent
	 */
	public static String getEditionVisited(int idEdition){
	
		String sql = "SELECT COUNT(*) AS NBR\n" +
		 "FROM EDITION e\n" +
		 "INNER JOIN DETAILS_EDITION de ON e.ID_EDITION = de.ID_EDITION \n" +
		 "WHERE e.ID_EDITION = " + idEdition + ";";

		//System.out.println(sql+"\n");
		
		return sql;
		
	}
}
