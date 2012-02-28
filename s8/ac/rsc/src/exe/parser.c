#include "parser.h"

char** lectureFichier(char* nom)
{
    // Le fichier de commande
    FILE * entree;
    char content[TAILLE_MAX];
    
    entree = fopen(nom, "rt");
    
    if(entree != NULL){
	
	while (fgets(content, TAILLE_MAX, entree) != NULL) // On lit le fichier tant qu'on ne reçoit pas d'erreur (NULL)
        {
            // Chercher jsuqu'à tomber sur entier_: et récupérer la commande jusqu'à ;
            // interpréter la commande
            // reprendre le parsage à la fin de la commande
            // utiliser STRTOK
        }
	
	fclose(entree);
    }
}

void interpreteCommande(char* commande)
{
}
