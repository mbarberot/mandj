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
	int size = entree_infos.st_size;
	char cmd[size];
	lectureFichier(cmd);
	
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
void lectureFichier(char* content)
{    
    int size = entree_infos.st_size;
    
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
	content[i] = '\0';
    }  
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
    
    // TODO : gérer le cas de commande inconnue
    
    while ( ptr != NULL ) {
	// On fait une copie de l'instruction pour pouvoir travailler dessus
	tmp = strdup(ptr);	
	
	// Commande creation
	if(strstr(tmp,"creation") != NULL)
	{
	   interpreteCreation(tmp);
	}
	
	// Commande choixGraphe
	if(strstr(tmp,"choisirGraphe") != NULL)
	{
	    interpreteChoixGraphe(tmp);
	}
	
	// Commande modifierNbMaxSommet
	if(strstr(tmp, "modifierNbMaxSommet") != NULL)
	{
	    interpreteModifierNbMaxSommet(tmp);
	}
	
	// Commande suppressionGraphe
	if(strstr(tmp, "suppressionGraphe") != NULL)
	{
	    interpreteSuppressionGraphe(tmp);
	}
	
	// Commande insertionSommet
	if(strstr(tmp, "insertionSommet") != NULL)
	{
	    interpreteInsertionSommet(tmp);
	}
	
	// Commande suppressionSommet
	if(strstr(tmp, "suppressionSommet") != NULL)
	{
	    interpreteSuppressionSommet(tmp);
	}
	
	// Commande viderGraphe
	if(strstr(tmp,"viderGraphe") != NULL)
	{
	    interpreteViderGraphe(tmp);
	}
	
	// Commande viderAreteGraphe
	if(strstr(tmp, "viderAreteGraphe") != NULL)
	{
	    interpreteViderAreteGraphe(tmp);
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
    int numCommande = -1;
    int arg = -1;
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
    // ecritureResultatCommande(numCommande, err);
    
    // Ecriture graphviz
}

/**
 * Interprete la commande choisirGraphe(idGraphe)
 * @param cmd : la chaine de caractere de la commande
 */
void interpreteChoixGraphe(char* cmd)
{
    char *sep = {",()choisirGraphe: "};
    char *ptr;
    int first = 1;
    int numCommande = -1;
    int arg = -1;
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

    err = choisirGraphe(arg);
}
/**
 * Interprete la commande modifierNbMaxSommet(int nvMax)
 * @param cmd : la chaine de caractere de la commande
 */
void interpreteModifierNbMaxSommet(char* cmd)
{
    char *sep = {",()modifierNbMaxSommet: "};
    char *ptr;
    int first = 1;
    int numCommande = -1;
    int arg = -1;
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
    
    err = modifierNbMaxSommet(arg);
}

/**
 * Interprete la commande suppressionGraphe(int idGraphe)
 * @param cmd : la chaine de caractere de la commande
 */
void interpreteSuppressionGraphe(char* cmd)
{
    char *sep = {",()suppressionGraphe: "};
    char *ptr;
    int first = 1;
    int numCommande = -1;
    int arg = -1;
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
    err = suppressionGraphe(arg);
}

/**
 * Interprete la commande insertionSommet(int nvSommet)
 * @param cmd : la chaine de caractere de la commande
 */
void interpreteInsertionSommet(char* cmd)
{
    char *sep = {",()insertionSommet: "};
    char *ptr;
    int first = 1;
    int numCommande = -1;
    int arg = -1;
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
    err = insertionSommet(arg);
}

/**
 * interprete la commande suppressionSommet(int sommet)
 * @param cmd : la chaine de caracteres de la commande
 */
void interpreteSuppressionSommet(char* cmd)
{
    char *sep = {",()suppressionSommet: "};
    char *ptr;
    int first = 1;
    int numCommande = -1;
    int arg = -1;
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
    
    err = suppressionSommet(arg);
}

/**
 * Interprete la commande viderGraphe()
 * @param cmd : la chaine de caracteres de la commande
 */
void interpreteViderGraphe(char* cmd)
{
    erreur err;
    
    /* TODO check sur le nb d'arguments */    
    err = viderGraphe();
}

/**
 * Interprete la commande viderAreteGraphe()
 * @param cmd : la chaine de caracteres de la commande
 */
void interpreteViderAreteGraphe(char* cmd)
{
    erreur err;
    
    err = viderAreteGraphe();
}