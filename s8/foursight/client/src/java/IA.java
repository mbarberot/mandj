import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.sics.jasper.*;


/**
 * Classe permettant d'utiliser un programme Prolog avec Sicstus
 * @author Barberot Mathieu et Racenet Joan
 */
public class IA extends Thread
{
    public static String PATH_TO_SICSTUS = "/usr/local/sicstus4.2.0/"; 
    public static String PATH_TO_FILE = "bin/ia.sav";
    
    /**
     * Plateau de jeu
     */
    private int[] plateau;
    
    /** 
     * Objet SICStus pour l'execution du programme Prolog 
     */ 
    private SICStus sicstus;
    
    /**
     * Interface prolog
     */
    private Prolog sp;
    
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
    private int coup;

 
    /**
     * Constructeur
     */
    public IA()
    {
        plateau = new int[16];
        running = false;
        coup = 0;
   
        try
        {
            sp = Jasper.newProlog(null,PATH_TO_SICSTUS,PATH_TO_FILE);
            
            
        } catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }
        
        for(int i = 0; i < plateau.length; i++)
        {
            plateau[i] = 0;
        }	    
    }
    
    public void debug(String methode)
    {
        String res = "[DEBUG] (IA."+methode+")";
        
        for(int i = 0; i < plateau.length; i++)
        {
            res += (i%4 == 0 ? "\n| " : "| ") 
            + plateau[i]
            + ((i+1) % 4 == 0 ? " | " : " ")
            ;
        }
        
        System.out.println(res);
     
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
            query = sp.openPrologQuery(requete, resultats);
            query.nextSolution();

            if (!resultats.isEmpty())
            {
                Term res = (Term) resultats.get("Sol");

                coup = traitement(res.toString());
                
            }

            query.close();
        } catch (SPException ex)
        {
            ex.printStackTrace();
	}
	catch( Exception ex )
	{
	    ex.printStackTrace();
	}
        sicstus.stopServer();
        running = false;	
    }

    /**
     * Traite la chaîne pour la mettre sous la forme 0-0-0-0
     * 
     * @param str La chaîne brute
     * @return La chaine transformée
     */
    private int traitement(String str)
    {
        int res = 0;
        int pow = 1000;
        char c;
        
        for(int i = 0; i < str.length(); i++)
        {
            c = str.charAt(i);
            if(c >= '0' && c <= '9') 
            { 
                res += Character.getNumericValue(c) * pow;
                pow /= 10;
            }
        }
        return res;
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
		+ i / 4 + ","
		+ i % 4 + "]"
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
        this.plateau = plateau;
	debug("setPlateau");
    }
    
    /**
     * Retourne le coup trouvé
     * @return 
     */
    public int getCoup()
    {
        return this.coup;
    }
    
    /**
     * Arrete l'IA
     */
    public void end()
    {
        cancel();
        sicstus.stopServer();
    }
    
}