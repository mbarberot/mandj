package db;

import wsdl.server.*;

/**
 * Cette classe contient toutes les requêtes nécessaires aux fonctions de synchronisation 
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
       return "INSERT INTO EDITION VALUES("
                + dEdition.getIdEdition() + ","
                + ((dEdition.getIdTome() < 0) ? "NULL" : dEdition.getIdTome()) +","
                + sql_string(dEdition.getIsbn()) + ","
                + ((dEdition.getDate_parution().length() == 0) ? "NULL" : sql_date(dEdition.getDate_parution())) +","
                + ((dEdition.getIdEditeur() < 0) ? "NULL" : dEdition.getIdEditeur()) + ","
                + dEdition.getFlag_default()
                + ")";
    }
    
    
    /**
     * Retourne la requête d'insertion d'un tome/volume dans la DB
     * 
     * @param dVolume - Informations sur le volume
     * @return La requête SQL
     */
    public static String insertVolume(DetailsVolume dVolume)
    {
        return "INSERT INTO TOME VALUES ("
                + dVolume.getIdTome()   + ","
                + sql_string(dVolume.getTitre())    + ","
                + dVolume.getIdSerie()  + ","
                + ((dVolume.getNumTome() < 0) ? "NULL" : dVolume.getNumTome()) +","
                + ((dVolume.getIdGenre() < 0) ? "NULL" : dVolume.getIdGenre())
                + ")" ;        
    }
    
    /**
     * Retourne la requête pour insérer un auteur dans la DB
     * 
     * @param dAuteur Informations sur l'auteur
     * @return La requête SQL
     */
    public static String insertAuteur(DetailsAuteur dAuteur)
    {
       return "INSERT INTO AUTEUR VALUES("
                + dAuteur.getIdAuteur() + ","
                + sql_string(dAuteur.getPseudo())   + ","
                + sql_string(dAuteur.getNom())      + ","
                + sql_string(dAuteur.getPrenom()) 
                + ")";
    }
    
    /**
     * Retourne la requête pour insérer les correspondances tomes <-> auteurs dans la DB
     * 
     * @param idTome ID du tome
     * @param coloristes IDs des coloristes
     * @param dessinateurs IDs des dessinateurs
     * @param scenaristes IDs des scenaristes
     * @return La requête SQL
     */
    public static String insertTjTomeAuteur(int idTome, String idAuteur, String role)
    {        
        return "INSERT INTO TJ_TOME_AUTEUR VALUES("
                + idTome    +","
                + idAuteur  +","
                + sql_string(role) 
                +")";
    }
    
    /**
     * Retourne la requête pour insérer un éditeur dans la DB
     * @param dEditeur Informations sur l'éditeur
     * @return La requête SQL
     */
    public static String insertEditeur (DetailsEditeur dEditeur)
    {
        return "INSERT INTO EDITEUR VALUES("
                + dEditeur.getIdEditeur() + ","
                + sql_string(dEditeur.getNomEditeur()) + ","
                + sql_string(dEditeur.getUrl())
                + ")";
    }
    
    
    
    /**
     * Retourne la requête d'insertions d'une série dans la DB
     * @param dSerie Objet contenant les informations d'une série
     * @return La requête SQL
     */
    public static String insertSerie(DetailsSerie dSerie)
    {
        return "INSERT INTO SERIE VALUES("
                + dSerie.getIdSerie() + ","
                + sql_string(dSerie.getNomSerie())
                + ")";
    }
    
    /**
     * Retourne la requête d'insertion d'un genre dans la DB
     * @param idGenre Champ ID_GENRE
     * @param nomGenre Champ NOM_GENRE 
     * @return La requête SQL
     */
    public static String insertGenre(int idGenre, String nomGenre)
    {
        return "INSERT INTO GENRE VALUES("
                + idGenre + ","
                + ((nomGenre != null) ? sql_string(nomGenre) : "NULL" )
                + ")";
    }
    
    /**
     * Place des guillemets simples (') autour d'une chaine
     * @param str La chaine
     * @return La chaine entourée de guillemets simples (')
     */
    public static String sql_string(String str)
    {
        return "'"+str+"'";
    }
    
    /**
     * Ajoute le mot clef "Date" et les guillemets simples (') autour d'une chaine
     * @param str La chaine
     * @return La chaine rretouchée
     */
    public static String sql_date(String str)
    {
        return "DATE '"+str+"'";
    }
    
    public static String getCount(String what, String table, int condition)
    {
        return "SELECT COUNT("+what+") \n"
                + "FROM "+table+" \n"
                + "WHERE "+what+" = "+condition;
    }
    
    public static String getCount(int idTome, String auteur, String role)
    {
        return "SELECT COUNT(*) \n"
                + "FROM TJ_TOME_AUTEUR \n"
                + "WHERE ID_TOME = "+idTome+" \n"
                + "AND ID_AUTEUR ="+sql_string(auteur)+" \n"
                + "AND ROLE ="+sql_string(role);
    }
    
}
