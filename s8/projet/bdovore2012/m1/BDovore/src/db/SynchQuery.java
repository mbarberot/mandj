package db;

import wsdl.server.DetailsEditeur;
import wsdl.server.DetailsEdition;
import wsdl.server.DetailsSerie;
import wsdl.server.DetailsVolume;

/**
 * Cette classe contient toutes les requêtes nécessaires aux fonctions de synchronisation 
 * @author kawa
 */
public class SynchQuery {
 
    
    public static String getLastID()
    {
        return "SELECT MAX(T.ID_TOME) FROM TOME T WHERE 1";
    }
    
    public static String insertEdition(DetailsEdition dEdition)
    {
        String edition, details;
        
        edition = "INSERT INTO EDITION VALUES("
                + dEdition.getIdEdition() + ","
                + ((dEdition.getIdTome() < 0) ? "NULL" : dEdition.getIdTome()) +","
                + dEdition.getIsbn() + ","
                + ((dEdition.getDate_parution().length() == 0) ? "NULL" : dEdition.getDate_parution()) +","
                + ((dEdition.getIdEditeur() < 0) ? "NULL" : dEdition.getIdEditeur()) + ","
                + dEdition.getFlag_default()
                + ");";
        
        details = "INSERT INTO DETAILS VALUES("
                + dEdition.getIdEdition() + ","
                + ((dEdition.getImg_couv().length() == 0) ? "NULL" : dEdition.getImg_couv())
                + ");";
        
        return edition + "\n" + details;
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
                + dVolume.getTitre()    + ","
                + dVolume.getIdSerie()  + ","
                + ((dVolume.getNumTome() < 0) ? "NULL" : dVolume.getNumTome()) +","
                + ((dVolume.getIdGenre() < 0) ? "NULL" : dVolume.getIdGenre())
                + ");" ;
    }
    
    
    /**
     * Retourne la requête pour insérer un éditeur dans la DB
     * @param dEditeur Informations sur l'éditeur
     * @return La requête SQL
     */
    public static String insertEditeur (DetailsEditeur dEditeur)
    {
        return "INSERT INTO EDITEUR("
                + dEditeur.getIdEditeur() + ","
                + dEditeur.getNomEditeur() + ","
                + dEditeur.getUrl()
                + ");";
    }
    
    
    
    /**
     * Retourne la requête d'insertions d'une série dans la DB
     * @param dSerie Objet contenant les informations d'une série
     * @return La requête SQL
     */
    public static String insertSerie(DetailsSerie dSerie)
    {
        String serie, details;
        
        serie = "INSERT INTO SERIE VALUES("
                + dSerie.getIdSerie() + ","
                + dSerie.getNomSerie()
                + ");";
        
        details = "INSERT INTO DETAILS_SERIE VALUES("
                + dSerie.getIdSerie() + ","
                + dSerie.getNbTomes() + ","
                + dSerie.getFlgFini() + ","
                + dSerie.getHistoire()
                + ");";
                
        return serie + "\n" + details;
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
                + ");";
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
    
}
