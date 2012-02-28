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

void interpreteCommande(char* commandes)
{
    char *sep = {";"};
    char *ptr;

    ptr = strtok(commandes, sep);
    while ( ptr != NULL ) {
      printf("token: %s\n", ptr);
      ptr = strtok(NULL, sep);
    }
    //printf("ligne après traitement: %s\n", commandes);
}
