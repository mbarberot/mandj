package db;

/**
 * Génère une requête de statistique
 *
 * @author Thorisoka
 */
public class StatsQuery {

    public static String total() {
        return "SELECT COUNT(ID_EDITION) AS NBR FROM BD_USER";
    }

    public static String totalOwned() {
        return "SELECT COUNT(ID_EDITION) AS NBR FROM BD_USER"
                + " WHERE FLG_AACHETER IS NULL OR FLG_AACHETER = false";
    }

    public static String totalWanted() {
        return "SELECT COUNT(ID_EDITION) AS NBR FROM BD_USER WHERE FLG_AACHETER = true";
    }

    public static String genres() {
        String sql = "SELECT g.NOM_GENRE AS LIB, COUNT(us.ID_EDITION) AS NBR, \n"
                + "100*COUNT(us.ID_EDITION)/(SELECT COUNT(ID_EDITION) FROM BD_USER) AS PERCENT \n"
                + "FROM BD_USER us \n"
                + "INNER JOIN EDITION e ON us.ID_EDITION = e.ID_EDITION \n"
                + "INNER JOIN TOME t ON e.ID_TOME = t.ID_TOME \n"
                + "INNER JOIN SERIE s ON t.ID_SERIE = s.ID_SERIE \n"
                + "INNER JOIN GENRE g ON s.ID_GENRE = g.ID_GENRE \n"
                + "GROUP BY g.ID_GENRE \n"
                + "ORDER BY NBR DESC";
        return sql;
    }

    public static String editeurs() {
        String sql = "SELECT ed.NOM_EDITEUR AS LIB, COUNT(us.ID_EDITION) AS NBR, \n"
                + "100*COUNT(us.ID_EDITION)/(SELECT COUNT(ID_EDITION) FROM BD_USER) AS PERCENT \n"
                + "FROM BD_USER us \n"
                + "INNER JOIN EDITION e ON us.ID_EDITION = e.ID_EDITION \n"
                + "INNER JOIN EDITEUR ed ON e.ID_EDITEUR = ed.ID_EDITEUR \n"
                + "GROUP BY ed.ID_EDITEUR \n"
                + "ORDER BY NBR DESC";
        return sql;
    }

    public static String dessinateurs() {
        String sql = "SELECT a.PSEUDO AS LIB, COUNT(us.ID_EDITION) AS NBR, \n"
                + "100*COUNT(us.ID_EDITION)/(SELECT COUNT(ID_EDITION) FROM BD_USER) AS PERCENT \n"
                + "FROM BD_USER us \n"
                + "INNER JOIN EDITION e ON us.ID_EDITION = e.ID_EDITION \n"
                + "INNER JOIN TOME t ON e.ID_TOME = t.ID_TOME \n"
                + "INNER JOIN TJ_TOME_AUTEUR tj ON t.ID_TOME = tj.ID_TOME \n"
                + "INNER JOIN AUTEUR a ON tj.ID_AUTEUR = a.ID_AUTEUR \n"
                + "WHERE tj.ROLE = 'Dessinateur'"
                + "GROUP BY a.ID_AUTEUR\n"
                + "ORDER BY NBR DESC";

        return sql;
    }

    public static String scenaristes() {
        String sql = "SELECT a.PSEUDO AS LIB, COUNT(us.ID_EDITION) AS NBR, \n"
                + "100*COUNT(us.ID_EDITION)/(SELECT COUNT(ID_EDITION) FROM BD_USER) AS PERCENT \n"
                + "FROM BD_USER us \n"
                + "INNER JOIN EDITION e ON us.ID_EDITION = e.ID_EDITION \n"
                + "INNER JOIN TOME t ON e.ID_TOME = t.ID_TOME \n"
                + "INNER JOIN TJ_TOME_AUTEUR tj ON t.ID_TOME = tj.ID_TOME \n"
                + "INNER JOIN AUTEUR a ON tj.ID_AUTEUR = a.ID_AUTEUR \n"
                + "WHERE tj.ROLE = 'Scenariste'"
                + "GROUP BY a.ID_AUTEUR\n"
                + "ORDER BY NBR DESC";

        return sql;
    }

    public static String coloristes() {
        String sql = "SELECT a.PSEUDO AS LIB, COUNT(us.ID_EDITION) AS NBR, \n"
                + "100*COUNT(us.ID_EDITION)/(SELECT COUNT(ID_EDITION) FROM BD_USER) AS PERCENT \n"
                + "FROM BD_USER us \n"
                + "INNER JOIN EDITION e ON us.ID_EDITION = e.ID_EDITION \n"
                + "INNER JOIN TOME t ON e.ID_TOME = t.ID_TOME \n"
                + "INNER JOIN TJ_TOME_AUTEUR tj ON t.ID_TOME = tj.ID_TOME \n"
                + "INNER JOIN AUTEUR a ON tj.ID_AUTEUR = a.ID_AUTEUR \n"
                + "WHERE tj.ROLE = 'Coloriste'"
                + "GROUP BY a.ID_AUTEUR\n"
                + "ORDER BY NBR DESC";

        return sql;
    }
}
