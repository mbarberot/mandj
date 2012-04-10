package db;

import db.data.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Objet principale de la base. Ouvre/ferme la base, et exécute des requêtes.
 *
 * @author Thorisoka
 */
public class DataBase {

    // Objet pour les connexions
    private Connection conn;
    /**
     * Page PHP (sans paramètres) pour la mise à jour utilisateurs
     */
    public static final String SRC = "http://www.thorisoka.dnsdojo.com/BD/";

    /**
     * Constructeur
     *
     * @param filename Nom de la base de données
     * @throws Exception
     */
    public DataBase(String filename) throws Exception {

        // Ouvre le drivers JDBC pour H2
        Class.forName("org.h2.Driver");

        // Ouvre la connexion
        conn = DriverManager.getConnection("jdbc:h2:" + filename, "bdovore", "bdovore");

    }

    /**
     * Retourne l'interface de connexion à la base de données
     *
     * @return L'interface de connexion à la base de donées
     */
    public Connection getConn() {
        return conn;
    }

    /**
     * Ferme la conexion et sauvegarde la base
     *
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
     *
     * @param expression
     * @throws SQLException
     */
    public synchronized void query(String expression) throws SQLException {

        Statement st = null;
        ResultSet rs = null;

        st = conn.createStatement();
        rs = st.executeQuery(expression);

        dump(rs);

        st.close();
    }
    
    /**
     * Retourne le nombre d'entrée de la requête.
     * A utiliser avec un SELECT COUNT(...
     *
     * @param sql La requête
     * @return Le résultat
     * @throws SQLException
     */
    public synchronized int getCount(String sql) throws SQLException 
    {
        int amount = 0;

        Statement st = null;
        ResultSet rs = null;

        st = conn.createStatement();
        rs = st.executeQuery(sql);
        
        if(rs.next()) 
        { 
            amount = Integer.parseInt(""+rs.getLong(1));
        }

        st.close();
        return amount;
    }

    /**
     * Requete de recherche (récupère tout sauf auteurs et éditions)
     *
     * @param sql La requete de recherche
     * @param limit Nombre d'éléments max à chercher
     * @param offset Nombre d'entrées à sauter (pour pagination)
     * @return Liste des albums trouvés
     * @throws SQLException
     */
    public synchronized ArrayList<Album> search(String sql, int limit, int offset)
            throws SQLException {

        ArrayList<Album> results = new ArrayList<Album>();

        // Préparation de la requête
        if (sql == null || sql.isEmpty()) {
            return results;
        }
        sql += " LIMIT " + limit + " OFFSET " + offset;

        // Execution de la requête
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        // Traitement des résultats de la requête
        while (rs.next()) {
            Album a = new Album(
                    rs.getInt("ID_TOME"), rs.getString("TITRE"),
                    rs.getInt("NUM_TOME"), rs.getInt("ID_SERIE"), rs.getString("NOM_SERIE"),
                    rs.getInt("ID_GENRE"), rs.getString("NOM_GENRE"), rs.getString("ISBN"));
            results.add(a);
        }

        st.close();
        return results;
    }

    /**
     * Calcule les statistiques de l'utilisateur et les retourne.
     *
     * @return Objet contenant les statistiques de l'utilisateur
     * @throws SQLException
     */
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


        result = new Statistics(total, owned, wanted, genres, editeurs, dessinateurs, scenaristes);

        st.close();
        return result;
    }

    /**
     * Calcule la répartition
     *
     * @param sql Requête sur les genres, éditeurs, dessinateurs ou scénaristes
     * @return Liste contenant la réparition des oeuvres en fonction du critère
     * @throws SQLException
     */
    public synchronized ArrayList<StatisticsRepartition> statRepartition(String sql)
            throws SQLException {

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        ArrayList<StatisticsRepartition> results = new ArrayList<StatisticsRepartition>();

        while (rs.next()) {
            StatisticsRepartition sr = new StatisticsRepartition(
                    rs.getString("LIB"), rs.getInt("NBR"), rs.getInt("PERCENT"));
            results.add(sr);
        }

        st.close();
        return results;
    }

    /**
     * Calcule le nombre max d'album pour la recherche en cours
     *
     * @param sql La requete de recherche pour albums
     * @return Le nombre maximum d'albums
     * @throws SQLException
     */
    public synchronized int getNumAlbums(String sql)
            throws SQLException {


        int result = 0;

        if (sql == null || sql.isEmpty()) {
            return result;
        }

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
    public synchronized void fillAlbum(Album album) throws SQLException {

        Statement st = conn.createStatement();
        ResultSet rs = null;

        ArrayList<Edition> editions = new ArrayList<Edition>();
        ArrayList<Auteur> scenaristes = new ArrayList<Auteur>();
        ArrayList<Auteur> dessinateurs = new ArrayList<Auteur>();
        ArrayList<Auteur> coloristes = new ArrayList<Auteur>();

        int tupleExist = 0;

        //
        // Récupération des éditions
        //
        rs = st.executeQuery(InfoQuery.getEditions(album.getId()));

        // Pour chaque édition de l'album
        //  On crée une édition
        //  On regarde si l'on possède l'album dans cette edition
        while (rs.next()) {
            Edition e = new Edition(album, rs.getInt("ID_EDITION"),
                    rs.getInt("ID_EDITEUR"), rs.getString("NOM_EDITEUR"),
                    rs.getDate("DATE_PARUTION"), rs.getString("ISBN"),
                    rs.getString("IMG_COUV"), rs.getBoolean("FLG_DEFAULT"));

            Statement st2 = conn.createStatement();
            ResultSet rs2 = st2.executeQuery(InfoQuery.getEditionsPossedees(rs.getInt("ID_EDITION")));

            if (rs2.next()) {
                e.setPossede(true);
                e.setAAcheter(rs2.getBoolean("FLG_AACHETER"));
                e.setDedicace(rs2.getBoolean("FLG_DEDICACE"));
                e.setPret(rs2.getBoolean("FLG_PRET"));
            }
            editions.add(e);
        }
        album.setEditions(editions);


        //
        //  Récupération des scénaristes, dessinateurs, coloristes
        //

        // On regarde s'il existe un tuple dans la table tj_tome_auteur pour l'album
        rs = st.executeQuery(InfoQuery.getAlbumVisited(album.getId()));
        if (rs.next()) {
            tupleExist = rs.getInt("NBR");
        }

        // Si c'est pas le cas, on recupere les infos du web service
        if (!(tupleExist > 0)) {
            System.out.println("Pas d'infos dispo pour les auteurs");
            String reponseWS = "";
            try {
                /**
                 * TODO: WebService Récupérer les informations manquantes
                 */
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // On regarde s'il existe un tuple dans la table tj_tome_auteur pour l'album
        rs = st.executeQuery(InfoQuery.getAlbumVisited(album.getId()));
        if (rs.next()) {
            // Récupération des scénaristes
            rs = st.executeQuery(InfoQuery.getAuteurs(album.getId(), "Scenariste"));
            while (rs.next()) {
                Auteur a = new Auteur(album, rs.getInt("ID_AUTEUR"), rs.getString("PSEUDO"),
                        rs.getString("PRENOM"), rs.getString("NOM"));
                scenaristes.add(a);
            }
            album.setScenaristes(scenaristes);

            // Récupération des dessinateurs
            rs = st.executeQuery(InfoQuery.getAuteurs(album.getId(), "Dessinateur"));
            while (rs.next()) {
                Auteur a = new Auteur(album, rs.getInt("ID_AUTEUR"), rs.getString("PSEUDO"),
                        rs.getString("PRENOM"), rs.getString("NOM"));
                dessinateurs.add(a);
            }
            album.setDessinateurs(dessinateurs);

            // Récupération des coloristes
            rs = st.executeQuery(InfoQuery.getAuteurs(album.getId(), "Coloriste"));
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
     * Récupère les infos d'une série
     *
     * @param idSerie ID de la série
     * @return La série
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
     * Purge les données utilisateur pour une importation (Penser à vérifier la
     * possibilité d'importer AVANT)
     *
     * @throws SQLException
     */
    public void deleteUserData() throws SQLException {
        update("DELETE FROM BD_USER");
    }

    /**
     * Met à jour les données utilisateur concernant l'Edition d'un Album. Selon
     * les données/instruction dans l'album, on peut : INSERT/DELETE, ou UPDATE
     * (déjà dans la base, mais status changé). Appel également de la mise à
     * jour de la table transaction
     *
     * @param edition L'édition de l'album
     * @throws SQLException
     */
    public void updateUserData(Edition edition) throws SQLException {

        String sql = "";

        Edition e = edition;
        Album a = edition.getParentAlbum();

        switch (edition.getUpdate()) {
            case Edition.INSERT:
                sql = "INSERT INTO BD_USER (ID_EDITION,FLG_AACHETER,FLG_PRET,FLG_DEDICACE,DATE_AJOUT)"
                        + "VALUES ("
                        + e.getId() + ","
                        + e.isAAcheter() + ","
                        + e.isPret() + ","
                        + e.isDedicace() + ","
                        + "NOW()"
                        + ")";
                this.update(sql);
                // Télécharge les infos relatives à l'édition et son album parent 
                // (url image, image elle même et les auteurs associés)
                downloadDetailsForEdition(edition);
                break;

            case Edition.DELETE:
                sql = "DELETE FROM BD_USER "
                        + "WHERE ID_EDITION = " + e.getId();
                this.update(sql);
                break;

            case Edition.UPDATE:
                sql = "UPDATE BD_USER SET"
                        + " FLG_AACHETER = " + e.isAAcheter()
                        + ", FLG_PRET = " + e.isPret()
                        + ", FLG_DEDICACE = " + e.isDedicace()
                        + " WHERE ID_EDITION = " + e.getId();
                this.update(sql);
                break;

            case Edition.DO_NOTHING:
                break;
            default:
                break;
        }

        // S'il y a une modification effectuée sur la table user, on met à jour la table transaction
        if (edition.getUpdate() != Edition.DO_NOTHING) {
            try {
                updateTransaction(edition.getId(), edition.getUpdate());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }

    /**
     * Requête de type update : CREATE, DROP, INSERT, UPDATE
     *
     * @param sql la requête
     * @return -1 pour une erreur, code de retour de l'update sinon.
     */
    public synchronized int update(String sql) {

        Statement st = null;
        int i = 0;

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
     * Récupère le dernier ID d'une table (pour update)
     *
     * @param table La table à interroger
     * @return Le dernier id de cette table
     * @throws SQLException
     */
    public synchronized int getLastID(String table) throws SQLException {
        int id = 0;

        if (!Tables.ids.containsKey(table)) {
            return 0;
        }

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
     * Récupère les ID d'une table
     *
     * @param table La table à interroger
     * @return Les id d'une table
     * @throws SQLException
     */
    public synchronized TreeSet<Integer> getAllID(String table) throws SQLException {
        TreeSet<Integer> ids = new TreeSet<Integer>();

        if (!Tables.ids.containsKey(table)) {
            return null;
        }

        String sql = "SELECT " + Tables.ids.get(table) + " AS id FROM " + table;

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            ids.add( (rs.getObject(1) == null) ? 0 : (Integer) rs.getObject(1) );
        }
        
        System.out.println(ids);

        st.close();
        return ids;
    }
    
    /**
     * Récupère les ID d'une table
     *
     * @param table La table à interroger
     * @return Les id d'une table
     * @throws SQLException
     */
    public synchronized TreeSet<TJ> getAllTJ() throws SQLException {

        int idTome, idAuteur;
        String role;
        TreeSet<TJ> tjs = new TreeSet<TJ>();

        String sql = "SELECT * FROM TJ_TOME_AUTEUR";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) 
        {
            idTome = rs.getInt("ID_TOME");
            idAuteur = rs.getInt("ID_AUTEUR");
            role = rs.getString("ROLE");
            tjs.add(new TJ(idTome,idAuteur,role));
        }
        
        System.out.println(tjs);

        st.close();
        return tjs;
    }

    /**
     * For debugging purpose, dump les données d'un ResultSet
     *
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

                if (o != null) {
                    System.out.print("["+(i+1)+"] "+o.getClass().getName()
                            + " : " + o + " / ");
                } else {
                    System.out.print("NULL : " + o + " / ");
                }
            }

            System.out.println("");
        }

    }

    /**
     * Renvoie un caractere correspondant au champ TYPE_MODIF de la table
     * TRANSACTION a pour ajout, s pour suppression, m pour modification, z
     * sinon
     *
     * @param sql : requete de type SELECT
     * @throws SQLException
     * @return int correspondant a TYPE_MODIF dans la BD TRANSACTION, 0 si le
     * resultat de la requete est vide
     */
    public synchronized int getTypeTransaction(String sql) throws SQLException {

        int modifPresente = 0;

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        if (rs.next()) {
            modifPresente = (Integer) rs.getObject(1);
        }

        st.close();
        System.out.println("modifPresente: " + modifPresente + "\n");
        return modifPresente;
    }

    /**
     * Insere une entrée dans la table TRANSACTION pour l'id, le type de modif
     * en parametre et la date courante, si pour l'id donné il existe déja une
     * transaction on modifie la transaction existante.
     *
     * @param idEdition : correspond au champ ID_EDITION de la table TRANSACTION
     * @param typeModif : le type de la transaction
     * @throws SQLException
     */
    public void updateTransaction(int idEdition, int typeModif) throws SQLException {

        String sql = "SELECT TYPE "
                + "FROM TRANSACTION T "
                + "WHERE T.ID_EDITION=" + idEdition;
        // Type de modification présente :
        //  0(aucune modif)
        //  1(ajout)
        //  2(modif)
        //  3(suppression)
        int typeModifOld = getTypeTransaction(sql);

        sql = "";
        // On regarde le type de transaction dans notre base
        // Pour chaque type de transaction on regarde le type de modification passé
        //  en argument par la méthode
        //
        switch (typeModifOld) {
            case 0: // Ajout
                switch (typeModif) {
                    case Edition.INSERT: // AJOUT + AJOUT => IMPOSSIBLE
                        break;
                    case Edition.UPDATE: // AJOUT + MODIFICATION => AJOUT
                        sql = "UPDATE TRANSACTION "
                                + "SET TYPE = 1, DATE = NOW() "
                                + "WHERE ID_EDITION = " + idEdition;
                        break;
                    case Edition.DELETE: // AJOUT + SUPRESSION => PAS DE TRANSACTION (On supprime la transaction ajout précédente)
                        sql = "DELETE FROM TRANSACTION "
                                + "WHERE ID_EDITION = " + idEdition;
                        break;
                }
                break;

            case 1: // Modification
                switch (typeModif) {
                    case Edition.INSERT: // MODIFICATION + AJOUT => IMPOSSIBLE
                        break;
                    case Edition.UPDATE: // MODIFICATION + MODIFICATION => MODIFICATION (On ne change que la date dans la transaction)
                        sql = "UPDATE TRANSACTION "
                                + "SET DATE = NOW() "
                                + "WHERE ID_EDITION = " + idEdition;
                        break;
                    case Edition.DELETE: // MODIFICATION + SUPRESSION => PAS DE TRANSACTION (On supprime la transaction modification précédente)
                        sql = "DELETE FROM TRANSACTION "
                                + "WHERE ID_EDITION = " + idEdition;
                        break;
                }
                break;

            case 2: // Suppression
                switch (typeModif) {

                    case Edition.INSERT: // SUPRESSION + AJOUT => AJOUT
                        sql = "UPDATE TRANSACTION "
                                + "SET TYPE = 1, DATE = NOW() "
                                + "WHERE ID_EDITION = " + idEdition;
                        break;
                    case Edition.UPDATE: // SUPRESSION + MODIFICATION => IMPOSSIBLE
                        break;
                    case Edition.DELETE: // SUPRESSION + SUPRESSION => IMPOSSIBLE
                        break;
                }
                break;

            case 3: // Rien
                switch (typeModif) {
                    case Edition.INSERT: // RIEN + AJOUT => AJOUT
                        sql = "INSERT INTO TRANSACTION (ID_EDITION,TYPE,DATE)"
                                + "VALUES(" + idEdition + ",1, NOW())";
                        break;
                    case Edition.UPDATE: // RIEN + MODIFICATION => MODIFICATION
                        sql = "INSERT INTO TRANSACTION (ID_EDITION,TYPE,DATE) "
                                + "VALUES(" + idEdition + ",2, NOW())";
                        break;
                    case Edition.DELETE: // RIEN + SUPPRESSION => IMPOSSIBLE
                        break;
                }
                break;

        }
        
        update(sql);


    }

    /**
     * Fonction qui complete une série en fonction des infos supplémentaires
     * disponible soit dans la base, soit via le web service
     *
     * @param serial La série à compléter
     * @throws SQLException
     */
    public void fillSerie(Serie serial) throws SQLException {

        String sql = "";
        sql = "SELECT ds.NB_TOMES, ds.FLG_FINI, ds.HISTOIRE\n"
                + "FROM SERIE s NATURAL JOIN DETAILS_SERIE ds\n"
                + "WHERE s.ID_SERIE = " + serial.getId();

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        // Si les données sont présentes dans la base, on les récupères
        // Sinon on contatcte le webservice
        if (rs.next()) {
            serial.completeSerie(rs.getInt("NB_TOMES"), rs.getInt("FLG_FINI"), rs.getString("HISTOIRE"));
            System.out.println("Série completée par la base de données locale");
        } else {
            System.out.println("Série pas dans la base");
            //
            // TODO : Contacter le webservice
            // Récupérer les informations manquantes sur la série
            //
            try {
                // TODO : Update de la base de données locale
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Deuxième essai de récupération des informations dans le base de données locale
            // Celle ci est normalement ajour grâce au WebService
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                serial.completeSerie(rs.getInt("NB_TOMES"), rs.getInt("FLG_FINI"), rs.getString("HISTOIRE"));
            }
        }

    }

    /**
     * Fonction qui complete un auteur en fonction des infos supplémentaires
     * disponible soit dans la base, soit via le web service
     *
     * @param author L'auteur à compléter
     * @throws SQLException
     */
    public void fillAuteur(Auteur author) throws SQLException {

        String sql = "";
        sql = "SELECT da.DATE_NAISSANCE , da.DATE_DECES, da.NATIONALITE\n"
                + "FROM AUTEUR a NATURAL JOIN DETAILS_AUTEUR da\n"
                + "WHERE a.ID_AUTEUR = " + author.getId();

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        // Si les données sont présente dans la base de donnée locale, on les utilise
        // Sinon on contacte le webservice
        if (rs.next()) {
            author.completeAuteur(rs.getDate("DATE_NAISSANCE"), rs.getDate("DATE_DECES"), rs.getString("NATIONALITE"));
            System.out.println("Auteur complete par la base de données locale");
        } else {
            System.out.println("Auteur pas dans la base");
            //
            // TODO : Contacter le WebService
            // Récupérer les informations manquantes sur l'auteur
            //
            try {
                // TODO : Mise à jour de la base de données locale
                //FrameMain.up.update("DETAILS_AUTEUR", reponseWS);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Deuxième essai de récupération des données dans la base locale
            // Celle-ci devrait être complétée grâce au webservice
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                author.completeAuteur(rs.getDate("DATE_NAISSANCE"), rs.getDate("DATE_DECES"), rs.getString("NATIONALITE"));
            }
        }

    }

    /**
     * Télécharge les détails d'une édition
     * @param edition L'édition concernée
     */
    public void downloadDetailsForEdition(Edition edition) {

        // Recuperation des infos sur l'album parent
        try {
            //
            // TODO : Contacter le WebService
            // Récupérer les informations sur l'édition
            //
            
            
            //reponseWS = Updater.wS.getInfosManquantesAuteurTome(FrameMain.currentUser.getUsername(),FrameMain.currentUser.getPassword(),edition.getParentAlbum().getId());
            //FrameMain.up.update("TJ_TOME_AUTEUR", reponseWS);
            //reponseWS = Updater.wS.getInfosManquantesEdition(FrameMain.currentUser.getUsername(),FrameMain.currentUser.getPassword(),edition.getId());
            //FrameMain.up.update("DETAILS_EDITION", reponseWS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //
        // TODO : Récupérer l'image
        //
    }

    /**
     * Fonction qui retourne le nombre de tomes possédés pour une série donnée
     *
     * @param idSerie, id de la série
     * @return nombre de tomes possédés pour cette série
     */
    public int getAlbumInSerieUser(int idSerie) {
        String sql = "";
        int nb = 0;

        sql = "SELECT COUNT(*) AS NBR\n"
                + "FROM SERIE s NATURAL JOIN TOME t\n"
                + "WHERE s.ID_SERIE = " + idSerie + " "
                + "AND t.ID_TOME IN "
                + "(SELECT t.ID_TOME FROM TOME t NATURAL JOIN EDITION e NATURAL JOIN BD_USER us)";

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                nb = rs.getInt("NBR");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return nb;

    }

    /**
     * Fonction qui compacte les données de la base locale. Attention, cela ne
     * supprime pas les images du dossier covers
     *
     * @throws SQLException
     */
    public void compactageBD() throws SQLException {

        String sql = "";

        sql = "DELETE FROM DETAILS_EDITION de\n"
                + "WHERE de.ID_EDITION NOT IN (SELECT ID_EDITION FROM BD_USER)";
        update(sql);

        sql = "DELETE FROM TJ_TOME_AUTEUR tj\n"
                + "WHERE tj.ID_TOME NOT IN (SELECT t.ID_TOME FROM TOME t NATURAL JOIN EDITION e NATURAL JOIN BD_USER us)";
        update(sql);

        sql = "DELETE FROM DETAILS_AUTEUR da\n"
                + "WHERE da.ID_TOME NOT IN (SELECT t.ID_TOME FROM TOME t NATURAL JOIN EDITION e NATURAL JOIN BD_USER us)";
        update(sql);

        sql = "DELETE FROM DETAILS_SERIE ds\n"
                + "WHERE ds.ID_SERIE NOT IN (SELECT s.ID_SERIE FROM SERIE s NATURAL JOIN TOME t NATURAL JOIN EDITION e NATURAL JOIN BD_USER us)";
        update(sql);

        System.out.println("Base de données compactée");
    }
}
