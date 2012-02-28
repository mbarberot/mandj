/**
 * 	Fonctions utilitaires pour la lecture et l'interprétation
 * 	des fichiers de commandes.
 * 
 * 	@author Mathieu BARBEROT
 * 	@author Joan RACENET
 */

#ifndef _PARSER_
#define _PARSER_

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#define TAILLE_MAX 1000


char** lectureFichier(char* nom);
void interpreteCommande(char* commande);


#endif