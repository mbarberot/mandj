package db;

import wsdl.server.*;

/**
 * Cette classe contient toutes les requêtes nécessaires aux fonctions de
 * synchronisation
 *
 * @author kawa
 */
public class SynchQuery
{

    // TODO
    // Insertion des détails_auteur, détails_editeur, détails_serie
    // Présence d'un info(genre,série,auteur...) dans la table correspondante
    /**
     * Retourne la requête pour insérer une édition dans la DB
     *
     * @param dEdition Informations sur l'édition
     * @return La requête SQL
     */
    public static String insertEdition(DetailsEdition dEdition)
    {
        return insertValues("EDITION", "")
                + dEdition.getIdEdition() + ","
                + ((dEdition.getIdTome() < 0) ? "NULL" : dEdition.getIdTome()) + ","
                + sql_string(dEdition.getIsbn()) + ","
                + ((dEdition.getDate_parution().length() == 0) ? "NULL" : sql_date(dEdition.getDate_parution())) + ","
                + ((dEdition.getIdEditeur() < 0) ? "NULL" : dEdition.getIdEditeur()) + ","
                + dEdition.getFlag_default()
                + ");";
    }

    /**
     * Retourne la requête d'insertion d'un tome/volume dans la DB
     *
     * @param dVolume - Informations sur le volume
     * @return La requête SQL
     */
    public static String insertVolume(DetailsVolume dVolume)
    {
        return insertValues("TOME", "")
                + dVolume.getIdTome() + ","
                + sql_string(dVolume.getTitre()) + ","
                + dVolume.getIdSerie() + ","
                + ((dVolume.getNumTome() < 0) ? "NULL" : dVolume.getNumTome()) + ","
                + ((dVolume.getIdGenre() < 0) ? "NULL" : dVolume.getIdGenre())
                + ");";
    }

    /**
     * Retourne la requête pour insérer un auteur dans la DB
     *
     * @param dAuteur Informations sur l'auteur
     * @return La requête SQL
     */
    public static String insertAuteur(DetailsAuteur dAuteur)
    {
        return insertValues("AUTEUR", "")
                + dAuteur.getIdAuteur() + ","
                + sql_string(dAuteur.getPseudo()) + ","
                + sql_string(dAuteur.getNom()) + ","
                + sql_string(dAuteur.getPrenom())
                + ");";
    }

    /**
     * Retourne la requête pour insérer les correspondances tomes <-> auteurs
     * dans la DB
     *
     * @param idTome ID du tome
     * @param coloristes IDs des coloristes
     * @param dessinateurs IDs des dessinateurs
     * @param scenaristes IDs des scenaristes
     * @return La requête SQL
     */
    public static String insertTjTomeAuteur(int idTome, int idAuteur, String role)
    {
        return insertValues("TJ_TOME_AUTEUR", "")
                + idTome + ","
                + idAuteur + ","
                + sql_string(role)
                + ");";
    }

    /**
     * Retourne la requête pour insérer un éditeur dans la DB
     *
     * @param dEditeur Informations sur l'éditeur
     * @return La requête SQL
     */
    public static String insertEditeur(DetailsEditeur dEditeur)
    {
        return insertValues("EDITEUR", "")
                + dEditeur.getIdEditeur() + ","
                + sql_string(dEditeur.getNomEditeur()) + ","
                + sql_string(dEditeur.getUrl())
                + ");";
    }

    /**
     * Retourne la requête d'insertions d'une série dans la DB
     *
     * @param dSerie Objet contenant les informations d'une série
     * @return La requête SQL
     */
    public static String insertSerie(DetailsSerie dSerie)
    {
        return insertValues("SERIE", "")
                + dSerie.getIdSerie() + ","
                + sql_string(dSerie.getNomSerie())
                + ");";
    }

    /**
     * Retourne la requête d'insertion d'un genre dans la DB
     *
     * @param idGenre Champ ID_GENRE
     * @param nomGenre Champ NOM_GENRE
     * @return La requête SQL
     */
    public static String insertGenre(int idGenre, String nomGenre)
    {
        return insertValues("GENRE", "")
                + idGenre + ","
                + ((nomGenre != null) ? sql_string(nomGenre) : "NULL")
                + ");";
    }

    public static String insertValues(String table, String flag)
    {
        return "INSERT " + flag + " INTO " + table + " VALUES(";
    }

    /**
     * Place des guillemets simples (') autour d'une chaine
     *
     * @param str La chaine
     * @return La chaine entourée de guillemets simples (')
     */
    public static String sql_string(String str)
    {
        return "'" + str.replace("'", "''") + "'";
    }

    /**
     * Ajoute le mot clef "Date" et les guillemets simples (') autour d'une
     * chaine
     *
     * @param str La chaine
     * @return La chaine rretouchée
     */
    public static String sql_date(String str)
    {
        return "DATE '" + str + "'";
    }

    /**
     * Retourne une requête permettant de compter les entrées dans la table TJ_TOME_AUTEUR
     * @param idTome    ID_TOME
     * @param auteur    ID_AUTEUR
     * @param role      'Scenariste' ou 'Dessinateur' ou 'Coloristes'
     * @return          Le nombre d'entrées
     */
    public static String getCountTj(int idTome, int idAuteur, String role)
    {
        return "SELECT COUNT(*) \n"
                + "FROM TJ_TOME_AUTEUR \n"
                + "WHERE ID_TOME = " + idTome + " \n"
                + "AND ID_AUTEUR =" + idAuteur + " \n"
                + "AND ROLE =" + sql_string(role);
    }
}
