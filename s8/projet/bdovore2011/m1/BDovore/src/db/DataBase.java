package db;

import java.sql.*;
import java.util.ArrayList;
import db.data.Album;
import db.data.Auteur;
import db.data.Edition;
import db.data.Serie;
import db.data.Statistics;
import db.data.StatisticsRepartition;

/**
 * Objet principale de la base.
 * Ouvre/ferme la base, et exécute des requêtes.
 * 
 * @author Thorisoka
 */
public class DataBase {
	
	private Connection conn;
	
	/** Page PHP (sans paramètres) pour la mise à jour utilisateurs */
	public static final String SRC = "http://www.thorisoka.dnsdojo.com/BD/";
	
	
	public DataBase(String filename) throws Exception {
		/*
		Class.forName("org.hsqldb.jdbcDriver");
		conn = DriverManager.getConnection("jdbc:hsqldb:file:" + filename, "sa", "");
		*/
		
		// Ouvre le drivers JDBC pour H2
		Class.forName("org.h2.Driver");
		
		// Ouvre la connexion
		conn = DriverManager.getConnection("jdbc:h2:" + filename, "bdovore", "b64tgtc");
		
	}
	
	


	/**
	 * Ferme la conexion et sauvegarde la base
	 * @throws SQLException
	 */
	public void shutdown() throws SQLException {
		
		Statement st = conn.createStatement();
		
		// Ecriture en fichier et cleanup
		st.execute("SHUTDOWN");
		conn.close();
	}
	
	
	
	
	/**
	 * Effectue une requête select de base et l'affiche
	 * @param expression
	 * @throws SQLException
	 */
	public synchronized void query(String expression) throws SQLException {
		
		Statement st = null;
		ResultSet rs = null;
		
		st = conn.createStatement();
		rs = st.executeQuery(expression); // run the query
		
		dump(rs);
		
		st.close();
	}
	
	
	
	
	
	/**
	 * Requete de recherche (récupère tout sauf auteurs et éditions)
	 * @param sql La requete de recherche
	 * @param limit Nombre d'éléments max à chercher
	 * @param offset Nombre d'entrées à sauter (pour pagination)
	 * @return Liste des albums trouvés
	 * @throws SQLException
	 */
	public synchronized ArrayList<Album> search(String sql, int limit, int offset)
	throws SQLException {
		
		ArrayList<Album> results = new ArrayList<Album>();
		
		if (sql == null || sql.isEmpty()) return results;
		
		
		sql += " LIMIT " + limit + " OFFSET " + offset;
		
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		
		
		
		while (rs.next()) {
			
			Album a = new Album(rs.getInt("ID_TOME"), rs.getString("TITRE"),
				rs.getInt("NUM_TOME"), rs.getInt("ID_SERIE"), rs.getString("NOM"),
				rs.getInt("ID_GENRE"), rs.getString("LIBELLE"), rs.getString("FLG_INT"),
				rs.getInt("FLG_TYPE"), rs.getString("ISBN"), rs.getString("EAN"));
			
			results.add(a);
		}
		
		st.close();
		return results;
	}
	
	
	
	
	public synchronized Statistics statistics() throws SQLException {
		
		Statistics result;
		Statement st = conn.createStatement();
		ResultSet rs = null;
		
		int total = 0;
		rs = st.executeQuery(StatsQuery.total());
		if (rs.next()) {
			total = rs.getInt("NBR");
		}
		
		int owned = 0;
		rs = st.executeQuery(StatsQuery.totalOwned());
		if (rs.next()) {
			owned = rs.getInt("NBR");
		}
		
		int wanted = 0;
		rs = st.executeQuery(StatsQuery.totalWanted());
		if (rs.next()) {
			wanted = rs.getInt("NBR");
		}
		
		ArrayList<StatisticsRepartition> genres = statRepartition(StatsQuery.genres());
		ArrayList<StatisticsRepartition> editeurs = statRepartition(StatsQuery.editeurs());
		ArrayList<StatisticsRepartition> dessinateurs = statRepartition(StatsQuery.dessinateurs());
		ArrayList<StatisticsRepartition> scenaristes = statRepartition(StatsQuery.scenaristes());
		
		
		result = new Statistics(total,owned,wanted,genres,editeurs,dessinateurs,scenaristes);
		
		st.close();
		return result;
	}
	
	
	
	public synchronized ArrayList<StatisticsRepartition> statRepartition(String sql)
		throws SQLException {
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		
		ArrayList<StatisticsRepartition> results = new ArrayList<StatisticsRepartition>();
		
		
		while (rs.next()) {
			
			StatisticsRepartition sr = new StatisticsRepartition(
				rs.getString("LIB"),rs.getInt("NBR"),rs.getInt("PERCENT")
			);
			
			results.add(sr);
		}
		
		st.close();
		return results;
	}
	
	
	
	
	/**
	 * Calcule le nombre max d'album pour la recherche en cours
	 * @param sql La requete de recherche pour albums
	 * @return
	 * @throws SQLException
	 */
	public synchronized int getNumAlbums(String sql)
	throws SQLException {
		
		
		int result = 0;
		
		if (sql == null || sql.isEmpty()) return result;
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		
		if (rs.next()) {
			result = rs.getInt("NBR");
		}
		
		st.close();
		return result;
	}
	
	
	
	
	
	
	/**
	 * Complète les données manquantes d'un album (auteurs, éditions)
	 */
	public synchronized void fillAlbum(Album album)	throws SQLException {
		
		Statement st = conn.createStatement();
		ResultSet rs = null;
		
		ArrayList<Edition> editions = new ArrayList<Edition>();
		ArrayList<Auteur> scenaristes = new ArrayList<Auteur>();
		ArrayList<Auteur> dessinateurs = new ArrayList<Auteur>();
		ArrayList<Auteur> coloristes = new ArrayList<Auteur>();
		
		
		/* Récupération Editions */
		
		rs = st.executeQuery(InfoQuery.getEditions(album.getId()));
		while (rs.next()) {
			Edition e = new Edition(album, rs.getInt("ID_EDITION"),
				rs.getString("FLG_DEFAULT"), rs.getInt("ID_EDITEUR"), rs.getString("ED_NOM"),
				rs.getDate("DTE_PARUTION"), rs.getInt("ID_COLLECTION"),
				rs.getString("C_NOM"), rs.getString("ISBN"), rs.getString("EAN"),
				rs.getString("IMG_COUV"), rs.getString("FLG_EO"), rs.getString("FLG_TT"),
				rs.getString("COMMENT"));
			
			Statement st2 = conn.createStatement();
			ResultSet rs2 = st2.executeQuery(
					InfoQuery.getEditionsPossedees(album.getId(), rs.getInt("ID_EDITION"))
			);
			
			if (rs2.next()) {
				e.setPossede(true);
				
				e.setAAcheter(rs2.getString("FLG_ACHAT") != null
					&& rs2.getString("FLG_ACHAT").equals("O"));
				
				e.setDedicace(rs2.getString("FLG_DEDICACE") != null
						&& rs2.getString("FLG_DEDICACE").equals("O"));
				
				e.setPret(rs2.getString("FLG_PRET") != null
						&& rs2.getString("FLG_PRET").equals("O"));
				
				// TODO : Pret à qui ?
			}
			
			editions.add(e);
		}
		album.setEditions(editions);
		
		
		
		/* Récupération Auteurs */
		
		rs = st.executeQuery(InfoQuery.getAuteurs(album.getId(),"SCENAR"));
		while (rs.next()) {
			Auteur a = new Auteur(album, rs.getInt("ID_AUTEUR"), rs.getString("PSEUDO"),
				rs.getString("PRENOM"), rs.getString("NOM"));
			scenaristes.add(a);
		}
		album.setScenaristes(scenaristes);
		
		rs = st.executeQuery(InfoQuery.getAuteurs(album.getId(),"DESSIN"));
		while (rs.next()) {
			Auteur a = new Auteur(album, rs.getInt("ID_AUTEUR"), rs.getString("PSEUDO"),
				rs.getString("PRENOM"), rs.getString("NOM"));
			dessinateurs.add(a);
		}
		album.setDessinateurs(dessinateurs);
		
		rs = st.executeQuery(InfoQuery.getAuteurs(album.getId(),"COLOR"));
		while (rs.next()) {
			Auteur a = new Auteur(album, rs.getInt("ID_AUTEUR"), rs.getString("PSEUDO"),
				rs.getString("PRENOM"), rs.getString("NOM"));
			coloristes.add(a);
		}
		album.setColoristes(coloristes);
		
		
		st.close();
	}
	
	
	
	
	
	
	/**
	 * Récupère les infos d'une série
	 * @param idSerie
	 * @return
	 * @throws SQLException
	 */
	public synchronized Serie getSerie(int idSerie) throws SQLException {
		
		Serie result = null;
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(InfoQuery.getSerie(idSerie));
		
		//"s.ID_SERIE, s.NOM, g.LIBELLE, s.NB_TOME, s.FLG_FINI, s.HISTOIRE";
		
		if (rs.next()) {
			result = new Serie(rs.getInt("ID_SERIE"), rs.getString("NOM"),
				rs.getString("LIBELLE"), rs.getInt("NB_TOME"), rs.getInt("FLG_FINI"),
				rs.getString("HISTOIRE"));
		}
		
		st.close();
		return result;
	}
	
	
	
	
	/**
	 * Purge les données utilisateur pour une importation
	 * (Penser à vérifier la possibilité d'importer AVANT)
	 * @throws SQLException
	 */
	public void deleteUserData() throws SQLException {
		update("DELETE FROM USERS_ALBUM");
		update("DELETE FROM USERS_EXCLUSIONS");
	}
	
	
	
	
	
	/**
	 * Met à jour les données utilisateur concernant l'Edition d'un Album.
	 * Selon les données/instruction dans l'album, on peut :
	 * INSERT/DELETE, ou UPDATE (déjà dans la base, mais status changé)
	 * 
	 * @param edition L'édition de l'album
	 * @throws SQLException
	 */
	public void updateUserData(Edition edition) throws SQLException {
		
		String sql = "";
		
		Edition e = edition;
		Album a = edition.getParentAlbum();
		
		
		switch (edition.getUpdate()) {
			
			case Edition.INSERT :
				
				sql = "INSERT INTO USERS_ALBUM VALUES (" +
					a.getId() + "," + a.getIdSerie() + "," +
					e.getIdEditeur() + "," + e.getIdCollection() + "," + e.getId() +
					"," + ((a.getScenaristes().size() > 0) ? a.getScenaristes().get(0).getId() : "NULL") +
					"," + ((a.getDessinateurs().size() > 0) ? a.getDessinateurs().get(0).getId() : "NULL") +
					"," + a.getIdGenre() + ",'" + (e.isPret() ? 'O' : 'N') + "', NULL, NULL, '" +
					(e.isDedicace() ? 'O' : 'N') + "','" + (e.isTirageTete() ? 'O' : 'N') +
					"', NULL, NULL, CURRENT_TIMESTAMP, '" + (e.isAAcheter() ? 'O' : 'N') +
					"', NULL, NULL, 'N')";
				this.update(sql);
				
				// TODO : Gérer les attributs ignorés (date ajout, cadeau, nom/mail preté, etc.)
				
				break;
				
				
				
			case Edition.DELETE :
				
				sql = "DELETE FROM USERS_ALBUM ua" +
				" WHERE ua.ID_TOME = " + a.getId() +
				" AND ua.ID_EDITION = " + e.getId();
				this.update(sql);
				
				// Ne gère pas les exclusion par série ici
				sql = "DELETE FROM USERS_EXCLUSIONS ue" +
				" WHERE ue.ID_TOME = " + a.getId();
				this.update(sql);
				
				break;
				
				
				
			case Edition.UPDATE :
				
				String flg_achat = e.isAAcheter() ? "'O'" : "'N'";
				String flg_dedicace = e.isDedicace() ? "'O'" : "'N'";
				String flg_pret = e.isPret() ? "'O'" : "'N'";
				
				sql = "UPDATE USERS_ALBUM ua SET" +
					" FLG_ACHAT = " + flg_achat + ", FLG_DEDICACE = " + flg_dedicace +
					", FLG_PRET = " + flg_pret +
					" WHERE ua.ID_TOME = " + a.getId() +
					" AND ua.ID_EDITION = " + e.getId();
				this.update(sql);
				break;
				
				
			default : // case Edition.DO_NOTHING
				break;
		}
		
	}
	
	
	
	
	/**
	 * Requête de type update : CREATE, DROP, INSERT, UPDATE
	 * @param sql la requête
	 * @throws SQLException
	 */
	public synchronized void update(String sql) throws SQLException {
		
		Statement st = null;
		
		st = conn.createStatement();
		int i = st.executeUpdate(sql); // run the query
		
		if (i == -1) {
			System.err.println("db error : " + sql);
		}
		
		st.close();
	}
	
	
	
	/**
	 * Récupère le dernier ID d'une table (pour update)
	 * @param table
	 * @return
	 * @throws SQLException
	 */
	public synchronized int getLastID(String table) throws SQLException {
		int id = 0;
		
		if (!Tables.ids.containsKey(table)) return 0;
		
		if (!table.startsWith("BD_")) return 0;
		
		
		String sql = "SELECT MAX(" + Tables.ids.get(table) + ") AS id FROM " + table;
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		

		if (rs.next()) {
			id = (rs.getObject(1) == null) ? 0 : (Integer) rs.getObject(1);
		}
		
		st.close();
		return id;
	}
	
	
	
	
	/**
	 * For debugging purpose, dump les données d'un ResultSet
	 * @param rs
	 * @throws SQLException
	 */
	public static void dump(ResultSet rs) throws SQLException {
		
		ResultSetMetaData meta = rs.getMetaData();
		int colmax = meta.getColumnCount();
		int i;
		Object o = null;
		
		while (rs.next()) {
			for (i = 0; i < colmax; ++i) {
				o = rs.getObject(i + 1); // Is SQL the first column is indexed
				
				// with 1 not 0
				
				if (o != null)
					System.out.print(o.getClass().getName() +
						" : " + o + " / ");
				else
					System.out.print("NULL : " + o + " / ");
			}
			
			System.out.println("");
		}
		
	}
	
	
}
