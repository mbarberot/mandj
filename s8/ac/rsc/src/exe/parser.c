#include "parser.h"


/**
 * Traite le fichier de commande dont le chemin est passé en paramètre
 * - Recupère le contenu du fichier 
 * - Traitement des commandes récupérées
 * 
 * @param path : le chemin du fichier de commande
 * @return TRAITEMENT_FICHIER_OK : fichier de commande correctement traité
 * @return FICHIER_COMMANDES_INEXISTANT : fichier de commande indiqué inexistant
 */
parserError chargerFichier(char* path)
{
    /* Préparation de l'ouverture du fichier de commandes */
    entree = NULL;
    entree = fopen(path, "r"); 
    
    /* Recupération des données du fichier */
    stat(path, &entree_infos);
    
    if(entree != NULL)
    {
	/* On recupère dans une chaine les commandes*/
	char cmd[entree_infos.st_size];
	strcpy(cmd, lectureFichier());
	
	/* On peut alors fermer le fichier d'entree et ouvrir celui de résultat pour l'écriture*/
	fclose(entree);
	
	/* Et on lance l'interpretation*/
	interpreteCommande(cmd);
	
    }
    else
    {
	return FICHIER_COMMANDES_INEXISTANT;
    }
    
    return TRAITEMENT_FICHIER_OK;
}

/**
 * Lit le fichier passé en paramètre, et renvoie son contenu sous forme de chaine de caractères
 * Pour plus de lisibilité, la fonction enlève les commentaires éventuellement présents dans le fichier de commande
 * @param nom: le nom du fichier
 * @return le contenu du fichier sous forme d'une chaine de caractères
 */
char* lectureFichier()
{    
    int size = entree_infos.st_size;
    
    // Chaine resultat
    char content[size];
    
    // Le caractere en cours
    int caractereActuel = 0;
    
    // Indice de parcours
    int i = 0;
    
    if(entree != NULL){
	// Initialisation du parcours
	caractereActuel = fgetc(entree);
	
	//Tant qu'on n'a pas atteint la fin du fichier
	while (caractereActuel != EOF)
	{  	    
	    // Si on trouve un début de commentaire : on passe à la ligne suivante
	    if(caractereActuel == '#')
	    {
		while(caractereActuel != '\n' && caractereActuel != EOF)
		{
		    caractereActuel = fgetc(entree);
		}
	    }	    
	    else if(isgraph(caractereActuel)) // Supprime les espaces au passage
	    {
		content[i] = caractereActuel;
		i++;		
		caractereActuel = fgetc(entree);		
	    }
	    else
	    {
		caractereActuel = fgetc(entree);
	    }
	    
	}	
	
    }
    
    return content;
    
}
/**
 * Interprete les commandes écrites dans une chaine de caractères
 * @param commandes : la chaine de caractères contenant les commandes à interpréter
 */
void interpreteCommande(char* commandes)
{
    char *sep = {";"};
    
    // Imbrication de strtok impossible => utilisation de strtok_r, adaptée dans ce cas
    char *bck;
    char *ptr = strtok_r(commandes, sep, &bck);    
    char *tmp = NULL;
    
    while ( ptr != NULL ) {
	// On fait une copie de l'instruction pour pouvoir travailler dessus
	tmp = strdup(ptr);	
	
	// Commande creation
	if(strstr(tmp,"creation") != NULL)
	{
	   interpreteCreation(tmp);
	}
	
	// Commande choixGraphe
	if(strstr(tmp,"choixGraphe") != NULL)
	{
	    interpreteChoixGraphe(tmp);
	}
	
	// Réinitialisation de la copie et passage à l'instruction suivante
	free(tmp);
	ptr = strtok_r(NULL,sep,&bck);
    }
}

/**
 * Interprete la commande "creation". Ecris le résultat obtenu dans le fichier res et génère le .dot
 * @param cmd : la chaine de caractere de la commande
 */
void interpreteCreation(char* cmd)
{
    char *sep = {",()creation: "};
    char *ptr;
    int first = 1;
    int numCommande;
    int arg;
    erreur err;
    
    ptr = strtok(cmd, sep);

    while(ptr != NULL)
    {
	if(first)
	{	    
	    numCommande = atoi(ptr);
	    first = 0;
	}
	else // Premier argument
	{
	    arg = atoi(ptr);
	}
	ptr = strtok(NULL, sep);
    }
    
    err = creation(arg);
    
    // Ecriture fichier resultat
    // Ecriture graphviz
}

/**
 * Interprete la commande choisirGraphe(idGraphe)
 * @param cmd : la chaine de caractere de la commande
 */
void interpreteChoixGraphe(char* cmd)
{
    
}
