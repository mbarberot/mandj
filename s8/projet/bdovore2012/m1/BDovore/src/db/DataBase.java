package db;

import gui.FrameMain;

import java.io.Reader;
import java.io.StringReader;
import java.sql.*;

import db.update.ClassEmptyWS;
import db.update.Updater;

import java.util.ArrayList;

import main.Clavier;

import au.com.bytecode.opencsv.CSVReader;
import db.data.Album;
import db.data.Auteur;
import db.data.Edition;
import db.data.Serie;
import db.data.Statistics;
import db.data.StatisticsRepartition;

/**
 * Objet principale de la base.
 * Ouvre/ferme la base, et exÃ©cute des requÃªtes.
 * 
 * @author Thorisoka
 */
public class DataBase {
	
	private Connection conn;
	
	/** Page PHP (sans paramÃ¨tres) pour la mise Ã  jour utilisateurs */
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
	
	public Connection getConn(){
		return conn;
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
	 * Effectue une requÃªte select de base et l'affiche
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
	 * Requete de recherche (rÃ©cupÃ¨re tout sauf auteurs et Ã©ditions)
	 * @param sql La requete de recherche
	 * @param limit Nombre d'Ã©lÃ©ments max Ã  chercher
	 * @param offset Nombre d'entrÃ©es Ã  sauter (pour pagination)
	 * @return Liste des albums trouvÃ©s
	 * @throws SQLException
	 */
	public synchronized ArrayList<Album> search(String sql, int limit, int offset)
	throws SQLException {
		
		ArrayList<Album> results = new ArrayList<Album>();
		
		if (sql == null || sql.isEmpty()) return results;
		
		
		sql += " LIMIT " + limit + " OFFSET " + offset;
		
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		
		//dump(rs);
		
		while (rs.next()) {
			/*System.out.println("id= "+rs.getInt("ID_TOME")+", titre= "+rs.getString("TITRE")+"\nnum= "+
				rs.getInt("NUM_TOME")+", id serie= "+rs.getInt("ID_SERIE")+", nom serie= "+ rs.getString("NOM_SERIE")+"\nidgenre= "+
				rs.getInt("ID_GENRE")+", nomgenre= "+rs.getString("NOM_GENRE")+", isbn= "+rs.getString("ISBN")+"\n");
			*/
			Album a = new Album(rs.getInt("ID_TOME"), rs.getString("TITRE"),
				rs.getInt("NUM_TOME"), rs.getInt("ID_SERIE"), rs.getString("NOM_SERIE"),
				rs.getInt("ID_GENRE"), rs.getString("NOM_GENRE"), rs.getString("ISBN"));
		
			results.add(a);
		}
		//System.out.println("resultats:\n"+results.toString());
		
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
	 * ComplÃ¨te les donnÃ©es manquantes d'un album (auteurs, Ã©ditions)
	 */
	public synchronized void fillAlbum(Album album)	throws SQLException {
		
		Statement st = conn.createStatement();
		ResultSet rs = null;
		
		ArrayList<Edition> editions = new ArrayList<Edition>();
		ArrayList<Auteur> scenaristes = new ArrayList<Auteur>();
		ArrayList<Auteur> dessinateurs = new ArrayList<Auteur>();
		ArrayList<Auteur> coloristes = new ArrayList<Auteur>();
		
		int tupleExist = 0;
		
		/* RÃ©cupÃ©ration Editions */
		
		rs = st.executeQuery(InfoQuery.getEditions(album.getId()));
		
		while (rs.next()) {
			Edition e = new Edition(album, rs.getInt("ID_EDITION"),
				rs.getInt("ID_EDITEUR"), rs.getString("NOM_EDITEUR"),
				rs.getDate("DATE_PARUTION"), rs.getString("ISBN"),
				rs.getString("IMG_COUV"), rs.getBoolean("FLG_DEFAULT"));
			
			Statement st2 = conn.createStatement();
			ResultSet rs2 = st2.executeQuery(
					InfoQuery.getEditionsPossedees(rs.getInt("ID_EDITION"))
			);
			
			if (rs2.next()) {
				e.setPossede(true);
				
				e.setAAcheter(rs2.getBoolean("FLG_AACHETER"));
				
				e.setDedicace(rs2.getBoolean("FLG_DEDICACE"));
				
				e.setPret(rs2.getBoolean("FLG_PRET"));
				
			}
			
			editions.add(e);
		}
		album.setEditions(editions);
		
		
		// On regarde s'il existe un tuple dans la table tj_tome_auteur pour l'album
		rs = st.executeQuery(InfoQuery.getAlbumVisited(album.getId()));
		if (rs.next()){
			tupleExist = rs.getInt("NBR");
		}
		
		// Si c'est pas le cas, on recupere les infos du web service
		if(!(tupleExist >0)){
			System.out.println("Pas d'infos dispo pour les auteurs");
			String reponseWS = "";
			try {
				/** TODO: A décommenter pour recuperer les infos tome-auteur */
				//reponseWS = Updater.wS.getInfosManquantesAuteurTome(FrameMain.currentUser.getUsername(),FrameMain.currentUser.getPassword(),edition.getParentAlbum().getId());
				//FrameMain.up.update("TJ_TOME_AUTEUR", reponseWS);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// On regarde s'il existe un tuple dans la table tj_tome_auteur pour l'album
		rs = st.executeQuery(InfoQuery.getAlbumVisited(album.getId()));
		if (rs.next()){		
			
			/* Récupération Auteurs */
			
			rs = st.executeQuery(InfoQuery.getAuteurs(album.getId(),"Scenariste"));
			while (rs.next()) {
				Auteur a = new Auteur(album, rs.getInt("ID_AUTEUR"), rs.getString("PSEUDO"),
					rs.getString("PRENOM"), rs.getString("NOM"));
				scenaristes.add(a);
			}
			album.setScenaristes(scenaristes);
			
			rs = st.executeQuery(InfoQuery.getAuteurs(album.getId(),"Dessinateur"));
			while (rs.next()) {
				Auteur a = new Auteur(album, rs.getInt("ID_AUTEUR"), rs.getString("PSEUDO"),
					rs.getString("PRENOM"), rs.getString("NOM"));
				dessinateurs.add(a);
			}
			album.setDessinateurs(dessinateurs);
			
			rs = st.executeQuery(InfoQuery.getAuteurs(album.getId(),"Coloriste"));
			while (rs.next()) {
				Auteur a = new Auteur(album, rs.getInt("ID_AUTEUR"), rs.getString("PSEUDO"),
					rs.getString("PRENOM"), rs.getString("NOM"));
				coloristes.add(a);
			}
			album.setColoristes(coloristes);	
		}
		
		st.close();
	}
	
	
	/**
	 * RÃ©cupÃ¨re les infos d'une sÃ©rie
	 * @param idSerie
	 * @return
	 * @throws SQLException
	 */
	public synchronized Serie getSerie(int idSerie) throws SQLException {
		
		Serie result = null;
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(InfoQuery.getSerie(idSerie));
		
		
		if (rs.next()) {
			result = new Serie(rs.getInt("ID_SERIE"), rs.getString("NOM_SERIE"),
				rs.getString("NOM_GENRE"));
		}
		
		st.close();
		return result;
	}
	
	/**
	 * Purge les donnÃ©es utilisateur pour une importation
	 * (Penser Ã  vÃ©rifier la possibilitÃ© d'importer AVANT)
	 * @throws SQLException
	 */
	public void deleteUserData() throws SQLException {
		update("DELETE FROM BD_USER");
	}
	
	/**
	 * Met Ã  jour les donnÃ©es utilisateur concernant l'Edition d'un Album.
	 * Selon les donnÃ©es/instruction dans l'album, on peut :
	 * INSERT/DELETE, ou UPDATE (dÃ©jÃ  dans la base, mais status changÃ©)
	 * Appel églament de la mise à jour de la table transaction
	 * 
	 * @param edition L'Ã©dition de l'album
	 * @throws SQLException
	 */
	public void updateUserData(Edition edition) throws SQLException {
		
		String sql = "";
		
		Edition e = edition;
		Album a = edition.getParentAlbum();
		
		switch (edition.getUpdate()) {
			
			case Edition.INSERT :
				
				sql = "INSERT INTO BD_USER (ID_EDITION,FLG_AACHETER,FLG_PRET,FLG_DEDICACE,DATE_AJOUT)" +
						"VALUES (" +
						e.getId() + "," +
						e.isAAcheter()+ ","+
						e.isPret()+ ","+
						e.isDedicace()+ ","+ 
						"NOW()" +
						")";
				
				//System.out.println(sql + "\n");
				this.update(sql);
				
				// TODO : GÃ©rer les attributs ignorÃ©s (cadeau, nom/mail pretÃ©, etc.)
				
				// Telecharge les infos relatives à l'édition et son album parent (url image, image elle même et les auteurs associés)
				downloadDetailsForEdition(edition);
				
				break;
				
				
				
			case Edition.DELETE :
				
				sql = "DELETE FROM BD_USER " +
				"WHERE ID_EDITION = " + e.getId();
				
				//System.out.println(sql + "\n");
				this.update(sql);
				
				break;
				
				
				
			case Edition.UPDATE :
				
				
				sql = "UPDATE BD_USER SET" +
				" FLG_AACHETER = " + e.isAAcheter() + 
				", FLG_PRET = " + e.isPret() +
				", FLG_DEDICACE = " + e.isDedicace() +
				" WHERE ID_EDITION = " + e.getId();

				//System.out.println(sql);
				this.update(sql);
				break;
				
				
			default : // case Edition.DO_NOTHING
				break;
		}
		
		// S'il y a une modification effectuée sur la table user, on met à jour la table transaction
		if(edition.getUpdate()!=Edition.DO_NOTHING){
			try {
				updateTransaction(edition.getId(),edition.getUpdate());
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}
		}
		
	}
	
	/**
	 * RequÃªte de type update : CREATE, DROP, INSERT, UPDATE
	 * @param sql la requÃªte
	 * 
	 */
	public synchronized int update(String sql){
		
		Statement st = null;
		int i =0;
		
		try {
			st = conn.createStatement();
			
			i = st.executeUpdate(sql); // run the query
			
			if (i == -1) {
				System.err.println("db error : " + sql);
			}
			
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return i;
	}	
	
	
	/**
	 * RÃ©cupÃ¨re le dernier ID d'une table (pour update)
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
	 * For debugging purpose, dump les donnÃ©es d'un ResultSet
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
	
	/**
	 * Renvoie un caractere correspondant au champ TYPE_MODIF de la table TRANSACTION
	 * a pour ajout, s pour suppression, m pour modification, z sinon
	 * @param sql : requete de type SELECT
	 * @throws SQLException
	 * @return int correspondant a TYPE_MODIF dans la BD TRANSACTION, 0 si le resultat de la requete est vide
	 */
	public synchronized int getTypeTransaction(String sql) throws SQLException {

		int modifPresente = 0;
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql); // run the query
		
		if(rs.next()){
			modifPresente = (Integer)rs.getObject(1);
		}
		
		st.close();
		System.out.println("modifPresente: "+modifPresente+"\n");
		return modifPresente;
	}
	
	/**
	 * Insere une entrÃ©e dans la table TRANSACTION pour l'id, le type de modif en parametre et la date courante, si pour l'id donnÃ© il existe dÃ©ja une transaction 
	 * on modifie la transaction existante.
	 * 
	 * @param idEdition : correspond au champ ID_EDITION de la table TRANSACTION
	 * @param typeModif : le type de la transaction
	 * @throws SQLException
	 */ 
	public void updateTransaction(int idEdition, int typeModif) throws SQLException {
		
		String sql = 	"SELECT TYPE " +
						"FROM TRANSACTION T " +
						"WHERE T.ID_EDITION=" + idEdition;
		
		int typeModifOld = getTypeTransaction(sql);		// Type de modification prÃ©sente, 1(ajout),2(modif) ou 3(suppression), si pas de transaction : 0

		sql ="";
		
		switch(typeModifOld){		// Si pour l'edition donnee, la transaction est de type :
		
			case 0 : 	
				switch (typeModif){
	
				case Edition.INSERT:									// AJOUT + AJOUT => IMPOSSIBLE
					break;
				case Edition.UPDATE: 									// AJOUT + MODIFICATION => AJOUT
					sql = 	"UPDATE TRANSACTION " +
							"SET TYPE = 1, DATE = NOW() " +
							"WHERE ID_EDITION = "+ idEdition; 
					break;
				case Edition.DELETE: 									// AJOUT + SUPRESSION => PAS DE TRANSACTION (On supprime la transaction ajout prÃ©cÃ©dente)
					sql = 	"DELETE FROM TRANSACTION " +
							"WHERE ID_EDITION = " + idEdition;
					break;
				}
				break;
				
			case 1 : 	
				switch (typeModif){
	
				case Edition.INSERT: 									// MODIFICATION + AJOUT => IMPOSSIBLE
					break;
				case Edition.UPDATE: 									// MODIFICATION + MODIFICATION => MODIFICATION (On ne change que la date dans la transaction)
					sql = 	"UPDATE TRANSACTION " +
							"SET DATE = NOW() " +
							"WHERE ID_EDITION = "+ idEdition;
					break;
				case Edition.DELETE: 									// MODIFICATION + SUPRESSION => PAS DE TRANSACTION (On supprime la transaction modification prÃ©cÃ©dente)
					sql = 	"DELETE FROM TRANSACTION " +
							"WHERE ID_EDITION = " + idEdition;
					break;
				}
				break;
			
			case 2 : 	
				switch (typeModif){
	
				case Edition.INSERT: 									// SUPRESSION + AJOUT => AJOUT
					sql = 	"UPDATE TRANSACTION " +
							"SET TYPE = 1, DATE = NOW() " +
							"WHERE ID_EDITION = "+ idEdition; 
					break;
				case Edition.UPDATE: 									// SUPRESSION + MODIFICATION => IMPOSSIBLE
					break;
				case Edition.DELETE: 									// SUPRESSION + SUPRESSION => IMPOSSIBLE
					break;
				}
				break;
				
			case 3 :									//  Si pour l'edition donnee, il n'y a pas de transaction
				
				switch (typeModif){
	
					case Edition.INSERT: 									// RIEN + AJOUT => AJOUT
						sql = 	"INSERT INTO TRANSACTION (ID_EDITION,TYPE,DATE)" +
						"VALUES(" + idEdition + ",1, NOW())";
						break;
					case Edition.UPDATE: 									// RIEN + MODIFICATION => MODIFICATION
						sql = 	"INSERT INTO TRANSACTION (ID_EDITION,TYPE,DATE) " +
						"VALUES(" + idEdition + ",2, NOW())";
						break;
					case Edition.DELETE: 									// RIEN + SUPPRESSION => IMPOSSIBLE
						break;
					}
					break;
					
		}
		
		//System.out.println("requete sur la table transaction:\n" + sql + "\n");
		
		update(sql);
		
		
	}
	

	
	/** Fonction qui complete une série en fonction des infos supplémentaires
	 *  disponible soit dans la base, soit via le web service
	 *  
	 * @param serial
	 * @throws SQLException
	 */
	public void fillSerie(Serie serial) throws SQLException{
		
		String sql = "";
		
		// si il existe une instance de la serie dans la table Details_Serie, on reccupere les infos
		sql = "SELECT ds.NB_TOMES, ds.FLG_FINI, ds.HISTOIRE\n" +
				"FROM SERIE s NATURAL JOIN DETAILS_SERIE ds\n" +
				"WHERE s.ID_SERIE = " + serial.getId();
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		
		if(rs.next()){
			serial.completeSerie(rs.getInt("NB_TOMES"),rs.getInt("FLG_FINI"), rs.getString("HISTOIRE"));
			System.out.println("Serie complete par la base de données locale");
		}else{
			System.out.println("Serie pas dans la base");
			//On telecharge les details sur la série via le WS
			String reponseWS = "";
			
			/** TODO: A décommenter pour recuperer les infos Serie */
			//reponseWS = Updater.wS.getInfosManquantesSerie(FrameMain.currentUser.getUsername(),FrameMain.currentUser.getPassword(),serial.getId());
			
			try {
				//FrameMain.up.update("DETAILS_SERIE", reponseWS);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if(rs.next()){
				serial.completeSerie(rs.getInt("NB_TOMES"),rs.getInt("FLG_FINI"), rs.getString("HISTOIRE"));
			}
		}
		
	}
	
	/**Fonction qui complete une série en fonction des infos supplémentaires 
	 * disponible soit dans la base, soit via le web service
	 * 
	 * @param author
	 * @throws SQLException
	 */
	public void fillAuteur(Auteur author) throws SQLException{
		
		String sql = "";
		
		// si il existe une instance de l'auteur dans la table Details_Auteur, on reccupere les infos
		sql = "SELECT da.DATE_NAISSANCE , da.DATE_DECES, da.NATIONALITE\n" +
				"FROM AUTEUR a NATURAL JOIN DETAILS_AUTEUR da\n" +
				"WHERE a.ID_AUTEUR = "+ author.getId();
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		
		if(rs.next()){
			author.completeAuteur(rs.getDate("DATE_NAISSANCE"),rs.getDate("DATE_DECES"), rs.getString("NATIONALITE"));
			System.out.println("Auteur complete par la base de données locale");
		}else{
			System.out.println("Auteur pas dans la base");
			//On telecharge les details sur la série via le WS
			String reponseWS = "";
			/** TODO: A décommenter pour recuperer les infos auteur*/
			//reponseWS = Updater.wS.getInfosManquantesAuteur(FrameMain.currentUser.getUsername(),FrameMain.currentUser.getPassword(),author.getId());
			
			try {
				//FrameMain.up.update("DETAILS_AUTEUR", reponseWS);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if(rs.next()){
				author.completeAuteur(rs.getDate("DATE_NAISSANCE"),rs.getDate("DATE_DECES"), rs.getString("NATIONALITE"));
			}
		}
		
	}

	public void downloadDetailsForEdition(Edition edition){
	
		// Recuperation des infos sur l'album parent
		String reponseWS = "";
		try {
			/** TODO: A décommenter pour recuperer les infos edition auteur et url image */
			//reponseWS = Updater.wS.getInfosManquantesAuteurTome(FrameMain.currentUser.getUsername(),FrameMain.currentUser.getPassword(),edition.getParentAlbum().getId());
			//FrameMain.up.update("TJ_TOME_AUTEUR", reponseWS);
			
			//reponseWS = Updater.wS.getInfosManquantesEdition(FrameMain.currentUser.getUsername(),FrameMain.currentUser.getPassword(),edition.getId());
			//FrameMain.up.update("DETAILS_EDITION", reponseWS);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/** TODO: telecharger l'image relative à l'édition */
	
	}
	
	/** Fonction qui retourne le nombre de tomes possédés pour une série donnée
	 * @param idSerie, id de la série
	 * @return nombre de tomes possédés pour cette série
	 */
	public int getAlbumInSerieUser(int idSerie){
		String sql = "";
		int nb = 0;
		
		sql = "SELECT COUNT(*) AS NBR\n" +
				"FROM SERIE s NATURAL JOIN TOME t\n" +
				"WHERE s.ID_SERIE = "+ idSerie +" " +
						"AND t.ID_TOME IN " +
								"(SELECT t.ID_TOME FROM TOME t NATURAL JOIN EDITION e NATURAL JOIN BD_USER us)";
		
		try{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				nb = rs.getInt("NBR");
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return nb;
		
	}
	
	/** Fonction qui compacte les données de la base locale. 
	 *  Attention, cela ne supprime pas les images du dossier covers
	 * 
	 * @throws SQLException
	 */
	public void compactageBD()throws SQLException{
		
		String sql = "";
		
		sql = "DELETE FROM DETAILS_EDITION de\n" +
				"WHERE de.ID_EDITION NOT IN (SELECT ID_EDITION FROM BD_USER)";
		update(sql);
		
		sql = "DELETE FROM TJ_TOME_AUTEUR tj\n" +
				"WHERE tj.ID_TOME NOT IN (SELECT t.ID_TOME FROM TOME t NATURAL JOIN EDITION e NATURAL JOIN BD_USER us)";
		update(sql);
		
		sql = "DELETE FROM DETAILS_AUTEUR da\n" +
				"WHERE da.ID_TOME NOT IN (SELECT t.ID_TOME FROM TOME t NATURAL JOIN EDITION e NATURAL JOIN BD_USER us)";
		update(sql);
		
		sql = "DELETE FROM DETAILS_SERIE ds\n" +
			"WHERE ds.ID_SERIE NOT IN (SELECT s.ID_SERIE FROM SERIE s NATURAL JOIN TOME t NATURAL JOIN EDITION e NATURAL JOIN BD_USER us)";
		update(sql);
		
		System.out.println("Base de données compactée");
	}
}
