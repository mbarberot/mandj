import java.util.HashMap;
import se.sics.jasper.*;


/**
 * Classe permettant d'utiliser un programme Prolog avec Sicstus
 * @author Barberot Mathieu et Racenet Joan
 */
public class IA extends Thread
{
    public static String PATH_TO_SICSTUS = "/usr/local/sicstus4.2.0/"; 
    public static String PATH_TO_FILE = "src/prolog/ia.prolog";
    
    /**
     * Plateau de jeu
     */
    private int[] plateau;
    
    /** 
     * Objet SICStus pour l'execution du programme Prolog 
     */ 
    private SICStus sp;
    
    /**
     * Requête à effectuer avec le programme Prolog
     */
    private Query query;
    
    /**
     * Flag permettant de connaitre l'état du thread
     */
    private boolean running ;

    /**
     * Un coup : 
     */
    private String coup;

 
    /**
     * Constructeur
     */
    public IA()
    {
        sp = null;
        plateau = new int[16];
        running = false;
        
        try 
        {
            sp = new SICStus(PATH_TO_SICSTUS);
            sp.load(PATH_TO_FILE);
        }
        catch (SPException ex)
        {
            System.err.println("Exception SICStus Prolog : " + ex);
            ex.printStackTrace();
            System.exit(-2);
        }
        
        for(int i = 0; i < plateau.length; i++)
        {
            plateau[i] = 0;
        }
    }
    
    /**
     * Demande le calcul d'un coup à l'IA
     *
     * @param plateau Le plateau de jeu reçu du client en C
     * @return Le coup déterminé par l'IA
     */
    @Override
    public void run()
    {
        String requete = prepareRequete("calculCoup",plateauToString(plateau));
        HashMap resultats = new HashMap();
       
        running = true;
	
	try 
        {
	    query = sp.openQuery(requete,resultats);
	    query.nextSolution();
	    
	    if(!resultats.isEmpty())
	    {
                SPTerm res = (SPTerm)resultats.get("Sol");  
                
                coup = traitement(res.toString());
	    }
	    
	    query.close();
	}
	catch( SPException ex )
	{
	    System.err.println("Exception Prolog : " + ex);
	}
	catch( Exception ex )
	{
	    System.err.println("Autre exception : "+ ex);
	}
	
        running = false;	
    }

    /**
     * Traite la chaîne pour la mettre sous la forme 0-0-0-0
     * 
     * @param str La chaîne brute
     * @return La chaine transformée
     */
    private String traitement(String str)
    {
        String res = "";
        char c;
        for(int i = 0; i < str.length(); i++)
        {
            c = str.charAt(i);
            if(c >= '0' && c <= '9') { res += c + "-"; }
        }
        return res.substring(0, res.length()-1);
    }
    
    /**
     * Prépare une requete pour l'IA
     *
     * @param predicat Le prédicat a appeller
     * @param plateau Le plateau, formaté pour l'IA
     * @return La requête complète
     */
    private String prepareRequete(String predicat, String plateau)
    {
	return predicat + "(" + plateau + "," + "Sol" + ").";
    }
    
    
    /**
     * Convertit le plateau de jeu en une chaîne de caractéres
     *
     * @param tab Tableau de 16 cases. Chaque case contient un entier représentant un pion.
     * @return Une châine de caractères représentant une liste formatée pour le programme Prolog.
     */
    private String plateauToString(int[] tab)
    {
	String str = "[";
	for(int i = 0; i < tab.length; i++)
	{
	    str += "["
		+ tab[i] + ","
		+ i % 4 + ","
		+ i / 4 + "]"
		+ (i != tab.length-1 ? "," : ""); 
	}
	str += "]";
	return str;
    }
    
    /**
     * Stoppe le thread s'il est lancé.
     */
    public void cancel()
    {
        if(running)
        {
            try
            {
                query.close();
                running = false;
            } 
            catch (NoSuchMethodException ex)
            {
                ex.printStackTrace();
            } 
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            } 
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Retourne l'état du thread
     * 
     * @return vrai si le thread est lancé, faux sinon.
     */
    public boolean isRunning()
    {
        return running;
    }
    
    

    /**
     * Met à jour le plateau
     * 
     * @param plateau Tableau d'entier codant le plateau. Chaque case contient un entier qui représente un pion
     */
    public void setPlateau(int[] plateau)
    {
        System.arraycopy(plateau, 0, this.plateau, 0, this.plateau.length);
    }
    
    /**
     * Retourne le coup trouvé
     * @return 
     */
    public String getCoup()
    {
        return this.coup;
    }
    
    
    
}