package db;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Génère une requête de recherche en fonction des paramètres. On peut chercher
 * selon un certain champs (Titre, Série, ISBN, etc.) On cherche également selon
 * un filtre (tout album, possédés, manquant)
 *
 * @author Thorisoka
 */
public class SearchQuery {

    public static final String GET_FIELDS =
            "t.ID_TOME, t.TITRE, t.NUM_TOME, "
            + "s.ID_SERIE, s.NOM_SERIE, "
            + "g.ID_GENRE, g.NOM_GENRE, "
            + "e.ISBN";
    
    
    public static final String GET_MAX = "COUNT(t.ID_TOME) AS NBR";
    public static final int SEARCH_IN_ALL = 0;
    public static final int SEARCH_IN_OWNED = 1;
    public static final int SEARCH_IN_MISSING = 2;
    private static final String[] searchInJoin = {
        "(e.ID_TOME = t.ID_TOME AND e.FLG_DEFAULT = TRUE)",
        "e.ID_TOME = t.ID_TOME",
        "e.ID_TOME = t.ID_TOME"
    };
    private static final String[] searchInWhere = {
        "",
        " AND e.ID_EDITION IN (SELECT ID_EDITION FROM BD_USER)",
        " AND t.ID_SERIE IN "
        + "(SELECT ss.ID_SERIE "
        + "FROM BD_USER us, EDITION e, TOME t, SERIE ss \n"
        + "WHERE us.ID_EDITION = e.ID_EDITION \n"
        + "AND e.ID_TOME = t.ID_TOME \n"
        + "AND t.ID_SERIE = ss.ID_SERIE) \n"
        + "AND e.ID_EDITION NOT IN (SELECT ID_EDITION FROM BD_USER)"
    };
    public static final int ORDER_ASC = 0;
    public static final int ORDER_DESC = 1;

    
    
    /**
     * Recherche sans recherche : liste les albums a la volée
     * 
     * @param searchIn ALL,OWNED ou MISSING
     * @param type COUNT ou SELECT
     * @param sortby Colonne selon laquelle on trie
     * @param order DESC ou ASC
     * @return 
     */
    public static String searchNothing(int searchIn, String type, String sortby, int order) {

        if (searchIn < 0 && searchIn > 2) {
            return "";
        }
        if (searchIn == SEARCH_IN_ALL) {
            return ""; // Pas de liste totale
        }
        
        String sql = "SELECT " + type + "\n"
            + "FROM TOME t, SERIE s, GENRE g, EDITION e \n"
            + "WHERE s.ID_SERIE = t.ID_SERIE \n"
            + "AND g.ID_GENRE = s.ID_GENRE \n"
            + "AND " + searchInJoin[searchIn] + "\n"
            + searchInWhere[searchIn] + "\n"
            + (type.equals(GET_MAX) ? "" : orderBy(sortby, order)) + "\n";

//        String sql = "SELECT " + type + "\n"
//                + "FROM TOME t \n"
//                + "INNER JOIN SERIE s ON s.ID_SERIE = t.ID_SERIE \n"
//                + "INNER JOIN GENRE g ON g.ID_GENRE = s.ID_GENRE \n"
//                + "INNER JOIN EDITION e ON " + searchInJoin[searchIn] + "\n"
//                + "WHERE 1" + searchInWhere[searchIn] + "\n"
//                + (type.equals(GET_MAX) ? "" : orderBy(sortby, order)) + "\n";
        return sql;
    }

    public static String searchTitre(int searchIn, String search, String type, String sortby, int order) {

        if (searchIn < 0 && searchIn > 2) {
            return "";
        }

        if (search.length() < 3) {
            return "";
        }

        //
        // MB
        //
        // TODO : Faire de la recherche avec plusieurs mots clef
        // Première approche, naïve : 
        //  1/ récupérer les mots (caractère sép : espace, 
        //  2/ faire une recherche :
        //      mot1 mot2 =>  like '%mot1%' or like '%mot2%'
        //  3/ Voir pour gérer le symbole '+' qui remplacerai le 'or' par 'and'
        //  4/ Voir pour gérer les accents (recherche avec et sans accents)
        //
        
        
        
        //
        // MB
        //
        // TODO : Se documenter sur l'optimisation des requêtes :
        //  L'inner join est-il mieux que le from t,i where t.j = i.j ?
        //  Est-ce toujours le cas avec des index ?
        //
        // Une fois cette information trouvée :
        //  Faire une abstraction plus profonde en dédiant une fonction de génération
        //  d'une requête de recherche, gérant plusieurs mots-clefs, laquelle sera 
        //  ensuite utilisée dans les autres fonctions de recherche.
        //
        

        String sql = "SELECT " + type + "\n"
                + "FROM TOME t \n"
                + "INNER JOIN SERIE s ON s.ID_SERIE = t.ID_SERIE \n"
                + "INNER JOIN GENRE g ON g.ID_GENRE = s.ID_GENRE \n"
                + "INNER JOIN EDITION e ON " + searchInJoin[searchIn] + "\n"
                + "WHERE t.TITRE like '%" + search + "%' \n"
                + searchInWhere[searchIn] + "\n"
                + (type.equals(GET_MAX) ? "" : orderBy(sortby, order)) + "\n";

        return sql;
    }

    public static String searchSerie(int searchIn, String search, String type, String sortby, int order) {

        if (searchIn < 0 && searchIn > 2) {
            return "";
        }
        if (search.length() < 3) {
            return "";
        }


        String sql = "SELECT " + type + "\n"
                + "FROM TOME t \n"
                + "INNER JOIN SERIE s ON s.ID_SERIE = t.ID_SERIE \n"
                + "INNER JOIN GENRE g ON g.ID_GENRE = s.ID_GENRE \n"
                + "INNER JOIN EDITION e ON " + searchInJoin[searchIn] + "\n"
                + "WHERE  s.NOM_SERIE LIKE '%" + search + "%' \n"
                + searchInWhere[searchIn] + "\n"
                + (type.equals(GET_MAX) ? "" : orderBy(sortby, order)) + "\n";

        return sql;
    }

    // Attention, cette recherche prend en compte la casse!
    public static String searchAuteur(int searchIn, String search, String type, String sortby, int order) {

        if (searchIn < 0 && searchIn > 2) {
            return "";
        }
        if (search.length() < 3) {
            return "";
        }


        String sql = "SELECT " + type + "\n"
                + "FROM TOME t \n"
                + "INNER JOIN SERIE s ON s.ID_SERIE = t.ID_SERIE \n"
                + "INNER JOIN GENRE g ON g.ID_GENRE = s.ID_GENRE \n"
                + "INNER JOIN EDITION e ON " + searchInJoin[searchIn] + "\n"
                + "INNER JOIN TJ_TOME_AUTEUR tj ON tj.ID_TOME = t.ID_TOME \n"
                + "INNER JOIN AUTEUR a ON a.ID_AUTEUR = tj.ID_AUTEUR \n"
                + "WHERE a.PSEUDO LIKE '%" + search + "%' \n"
                + searchInWhere[searchIn] + "\n"
                + (type.equals(GET_MAX) ? "" : orderBy(sortby, order)) + "\n";

        return sql;
    }

    // Attention cela ne marche pas si on recherche dans les manquants...
    public static String searchISBN(int searchIn, String search, String type, String sortby, int order) {

        if (searchIn < 0 && searchIn > 2) {
            return "";
        }
        if (search.length() < 3) {
            return "";
        }


        String isbn = CodeBarre.toBDovoreISBN(escape(search));
        String ean = CodeBarre.toBDovoreEAN(escape(search));

        //String eanisbn = isbn + " " + ean; // FullText prend tout

        String sql = "SELECT " + type + "\n"
                + "FROM TOME t\n"
                + "INNER JOIN SERIE s ON s.ID_SERIE = t.ID_SERIE \n"
                + "INNER JOIN GENRE g ON g.ID_GENRE = s.ID_GENRE \n"
                + "INNER JOIN EDITION e ON e.ID_TOME = t.ID_TOME \n"
                + "WHERE (e.ISBN like '%" + search + "%' "
                + "OR e.ISBN like '%" + isbn + "%' "
                + "OR e.ISBN like '%" + ean + "%')\n"
                + searchInWhere[searchIn] + "\n"
                + (type.equals(GET_MAX) ? "" : orderBy(sortby, order)) + "\n";
        return sql;
    }

    /**
     * TODO: refaire cette fonction afin de l'adapter � la nouvelle base si
     * n�ccessaire
     */
    public static String insertISBN(String search, String isPret, String isDedicace, String isAAcheter) {

        String isbn = CodeBarre.toBDovoreISBN(escape(search));
        String ean = CodeBarre.toBDovoreEAN(escape(search));
        String eanisbn = isbn + " " + ean; // FullText prend tout

        String sqlSearch = "SELECT t.ID_TOME, s.ID_SERIE, e.ID_EDITEUR, e.ID_COLLECTION, e.ID_EDITION, scenar.ID_AUTEUR, dess.ID_AUTEUR, g.ID_GENRE, '" + isPret + "', NULL, NULL, '" + isDedicace + "', e.FLG_TT, NULL, NULL, CURRENT_TIMESTAMP, '" + isAAcheter + "', NULL, NULL, 'N' \n"
                + "FROM BD_EDITION e, FTL_SEARCH_DATA('" + eanisbn + "', 0, 0) FT \n"
                + "INNER JOIN BD_TOME t ON t.ID_TOME = e.ID_TOME \n"
                + "INNER JOIN BD_SERIE s ON s.ID_SERIE = t.ID_SERIE \n"
                + "INNER JOIN BD_GENRE g ON g.ID_GENRE = t.ID_GENRE \n"
                + "INNER JOIN BD_AUTEUR scenar ON scenar.ID_AUTEUR = t.ID_SCENAR \n"
                + "INNER JOIN BD_AUTEUR dess ON dess.ID_AUTEUR = t.ID_DESSIN \n"
                + "WHERE FT.TABLE='BD_EDITION' AND e.ID_EDITION = FT.KEYS[0] limit 1";

        String sql = "INSERT INTO USERS_ALBUM \n" + sqlSearch;

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
     *
     * @param str
     * @return TODO : Bogus
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
        if (order != 0 && order != 1) {
            return "";
        }
        return "ORDER BY " + field + " " + ((order == ORDER_ASC) ? "ASC" : "DESC");
    }
}
