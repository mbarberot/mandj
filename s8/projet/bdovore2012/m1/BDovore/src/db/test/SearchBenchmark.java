package db.test;

import db.DataBase;


public class SearchBenchmark {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String sql_tome = "SELECT * FROM BD_TOME t INNER JOIN BD_SERIE s ON s.ID_SERIE = t.ID_SERIE INNER JOIN BD_GENRE g ON g.ID_GENRE = t.ID_GENRE WHERE TITRE LIKE '%missa%'";
		String sql_serie = "SELECT * FROM BD_TOME t INNER JOIN (SELECT ID_SERIE, NOM FROM BD_SERIE WHERE NOM LIKE '%slayers%') s ON s.ID_SERIE = t.ID_SERIE INNER JOIN BD_GENRE g ON g.ID_GENRE = t.ID_GENRE";
		String sql_isbn = "SELECT * FROM BD_TOME t INNER JOIN BD_SERIE s ON s.ID_SERIE = t.ID_SERIE INNER JOIN BD_GENRE g ON g.ID_GENRE = t.ID_GENRE WHERE ISBN = '2723428893' OR EAN = '2723428893'";
		String sql_artiste = "SELECT * FROM BD_TOME t INNER JOIN (SELECT ID_AUTEUR AS ida FROM BD_AUTEUR WHERE LCASE(PSEUDO) LIKE '%kanzaka%' OR LCASE(NOM) LIKE '%kanzaka%' OR LCASE(PRENOM) LIKE '%kanzaka%') sq ON (sq.ida = t.ID_SCENAR OR sq.ida = t.ID_SCENAR_ALT OR sq.ida = t.ID_DESSIN OR sq.ida = t.ID_DESSIN_ALT OR sq.ida = t.ID_COLOR OR sq.ida = t.ID_COLOR_ALT) INNER JOIN BD_SERIE s ON s.ID_SERIE = t.ID_SERIE INNER JOIN BD_GENRE g ON g.ID_GENRE = t.ID_GENRE";
		String sql1 = "SELECT * FROM BD_TOME t INNER JOIN BD_SERIE s ON s.ID_SERIE = t.ID_SERIE INNER JOIN BD_GENRE g ON g.ID_GENRE = t.ID_GENRE WHERE ID_TOME = 8";
		String sql2 = "SELECT * FROM BD_AUTEUR WHERE ID_AUTEUR = 500";
		
		long t1,t2;
		
		try {
			DataBase db = new DataBase("db/bdovore");
			
			
			System.out.println("Flynn = Guy");
			
			t1 = System.currentTimeMillis();
			db.query(sql_tome);
			t2 = System.currentTimeMillis();
			System.out.println("TOME : " + (t2-t1));
			
			t1 = System.currentTimeMillis();
			db.query(sql_tome + " LIMIT 50");
			t2 = System.currentTimeMillis();
			System.out.println("TOME + LIMIT : " + (t2-t1));
			
			t1 = System.currentTimeMillis();
			db.query(sql_serie);
			t2 = System.currentTimeMillis();
			System.out.println("SERIE : " + (t2-t1));
			
			t1 = System.currentTimeMillis();
			db.query(sql_serie + " LIMIT 50");
			t2 = System.currentTimeMillis();
			System.out.println("SERIE + LIMIT : " + (t2-t1));
			
			t1 = System.currentTimeMillis();
			db.query(sql_isbn);
			t2 = System.currentTimeMillis();
			System.out.println("ISBN : " + (t2-t1));
			
			t1 = System.currentTimeMillis();
			db.query(sql_isbn + " LIMIT 50");
			t2 = System.currentTimeMillis();
			System.out.println("ISBN + LIMIT : " + (t2-t1));
			
			t1 = System.currentTimeMillis();
			db.query(sql_artiste);
			t2 = System.currentTimeMillis();
			System.out.println("ARTISTE : " + (t2-t1));
			
			t1 = System.currentTimeMillis();
			db.query(sql_artiste + " LIMIT 50");
			t2 = System.currentTimeMillis();
			System.out.println("ARTISTE + LIMIT : " + (t2-t1));
			
			t1 = System.currentTimeMillis();
			db.query(sql1);
			db.query(sql2);db.query(sql2);db.query(sql2);
			db.query(sql2);db.query(sql2);db.query(sql2);
			t2 = System.currentTimeMillis();
			System.out.println("FICHE : " + (t2-t1));
			
			System.out.println("Merci ;; !");
			
			db.shutdown();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
