#include "parser.h"

/**
 * Lit le fichier passé en paramètre, et renvoie son contenu sous forme de chaine de caractères
 * Pour plus de lisibilité, la fonction enlève les commentaires éventuellement présents dans le fichier de commande
 * @param nom: le nom du fichier
 * @return le contenu du fichier sous forme d'une chaine de caractères
 */
char* lectureFichier(char* nom)
{
    // Le fichier de commande
    FILE * entree = NULL;
    
    // Recuperation de la taille du fichier
    struct stat st;
    stat(nom, &st);
    int size = st.st_size;
    
    // Chaine resultat
    char content[size];
    
    // Le caractere en cours
    int caractereActuel = 0;
    
    // Indice de parcours
    int i = 0;
    
    entree = fopen(nom, "rt");
    
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
	    else if(isgraph(caractereActuel))
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
	fclose(entree);
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
    char *ptr;

    ptr = strtok(commandes, sep);
    while ( ptr != NULL ) {
      
      // Vérifier si premiers char => nbres
      if(strstr(ptr, "creation") != NULL)
	  interpreteCreation(ptr);
      
      ptr = strtok(NULL, sep);
    }
    //printf("ligne après traitement: %s\n", commandes);
}

/**
 * Interprete la commande "creation". Ecris le résultat obtenu dans le fichier res et génère le .dot
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
	// Premier token rencontré : numéro de la commande
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