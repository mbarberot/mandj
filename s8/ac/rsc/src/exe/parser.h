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
#include <ctype.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>

#include "graphe.h"
#include "liste.h"


char* lectureFichier(char* nom);
void interpreteCommande(char* commande);
void interpreteCreation(char* cmd);

#endif