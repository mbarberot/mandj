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
     * Retourne le code SQL permettant de faire une recherche sans critères pour
     * lister tous les albums.
     *
     * @param searchIn Domaine de recherche (Albums possédés, manquant ou tous
     * les albums)
     * @param type Type de la requete (éléments ou COUNT)
     * @param sortby Critère de tri
     * @param order Ordre des résultats (ASC ou DESC)
     * @return Le code SQL
     */
    public static String searchNothing(int searchIn, String type, String sortby, int order) {

        if (searchIn < 0 && searchIn > 2) {
            return "";
        }
        if (searchIn == SEARCH_IN_ALL) {
            return ""; // Pas de liste totale
        }

        String sql = "SELECT " + type + "\n"
                + "FROM TOME t \n"
                + "INNER JOIN SERIE s ON s.ID_SERIE = t.ID_SERIE \n"
                + "INNER JOIN GENRE g ON g.ID_GENRE = s.ID_GENRE \n"
                + "INNER JOIN EDITION e ON " + searchInJoin[searchIn] + "\n"
                + "WHERE 1" + searchInWhere[searchIn] + "\n"
                + (type.equals(GET_MAX) ? "" : orderBy(sortby, order)) + "\n";

        return sql;
    }

    /**
     * Retourne le code SQL permettant une recherche par titre.
     *
     * @param searchIn Domaine de recherche (Albums possédés, manquant ou tous
     * les albums)
     * @param search Mot(s)-clef(s) de la recherche
     * @param type Type de la requete (éléments ou COUNT)
     * @param sortby Critère de tri
     * @param order Ordre des résultats (ASC ou DESC)
     * @return Le code SQL
     */
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
        //  1/ récupérer les mots (caractère sép : espace, virgule)
        //  2/ faire une recherche : mot1 mot2 =>  like '%mot1%' or like '%mot2%'
        //  3/ Voir pour gérer le symbole '+' qui remplacerai le 'or' par 'and'
        //  4/ Voir pour gérer les accents (recherche avec et sans accents)
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

    /**
     * Retourne le code SQL permettant une recherche par série.
     *
     * @param searchIn Domaine de recherche (Albums possédés, manquant ou tous
     * les albums)
     * @param search Mot(s)-clef(s) de la recherche
     * @param type Type de la requete (éléments ou COUNT)
     * @param sortby Critère de tri
     * @param order Ordre des résultats (ASC ou DESC)
     * @return Le code SQL
     */
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

    /**
     * Retourne le code SQL permettant une recherche par auteur. Attention,
     * cette recherche prend en compte la casse!
     *
     * @param searchIn Domaine de recherche (Albums possédés, manquant ou tous
     * les albums)
     * @param search Mot(s)-clef(s) de la recherche
     * @param type Type de la requete (éléments ou COUNT)
     * @param sortby Critère de tri
     * @param order Ordre des résultats (ASC ou DESC)
     * @return
     * @return Le code SQL
     */
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

    /**
     * Retourne le code SQL permettant une recherche par ISBN.
     *
     * @param searchIn Domaine de recherche (Albums possédés, manquant ou tous
     * les albums)
     * @param search Mot(s)-clef(s) de la recherche
     * @param type Type de la requete (éléments ou COUNT)
     * @param sortby Critère de tri
     * @param order Ordre des résultats (ASC ou DESC)
     * @return Le code SQL
     */
    public static String searchISBN(int searchIn, String search, String type, String sortby, int order) {

        if (searchIn < 0 && searchIn > 2) {
            return "";
        }
        if (search.length() < 3) {
            return "";
        }
        
        String isbn = CodeBarre.toBDovoreISBN(escape(search));
        String ean = CodeBarre.toBDovoreEAN(escape(search));

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
     * Echape et transforme les caractères pour H2
     *
     * @param str Chaîne à traiter
     * @return La chaîne traitée
     */
    private static String escape(String str) {
        return str.replace("'", "''").toLowerCase();
    }

    /**
     * Encode la chaine avec l'algorithme MD5
     *
     * @param str La chaîne à traiter
     * @return La chaîne traitée
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
     *
     * @param field Champ pour l'ordre
     * @param order Ordre (utiliser les constantes ORDER.ASC et ORDER.DESC)
     * @return Le clause ORDER BY en SQL
     */
    private static String orderBy(String field, int order) {
        if (order != 0 && order != 1) {
            return "";
        }
        return "ORDER BY " + field + " " + ((order == ORDER_ASC) ? "ASC" : "DESC");
    }
}
