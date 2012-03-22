package db;

/**
 * Cette classe contient toutes les requêtes nécessaires aux fonctions de synchronisation 
 * @author kawa
 */
public class SynchQuery {
 
    
    public static String getLastID()
    {
        return "SELECT MAX(T.ID_TOME) FROM TOME T WHERE 1";
    }
    
    
}
