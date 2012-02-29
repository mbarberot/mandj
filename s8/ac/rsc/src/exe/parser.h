/**
 * 	Fonctions utilitaires pour la lecture et l'interprétation
 * 	des fichiers de commandes.
 * 
 * 	@author Mathieu BARBEROT
 * 	@author Joan RACENET
 */

/**
 * @TODO : faire les fonctions d'interprétations de toutes les fonctions
 * @TODO : écriture dans le fichier résultat
 * @TODO : génération des .dot et des .jpg associés 
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

/*
 * Les erreurs
 * 
 */
typedef enum 
{       
	TRAITEMENT_FICHIER_OK, // => afficher que l'interprétation a été un succès
	FICHIER_COMMANDES_INEXISTANT, // => afficher que le fichier n'a pas été trouvé
	ARGUMENTS_INCORRECTS, // => nb d'arguments invalide. Afficher la commande concernée et l'ignorer (passer à la commande suivante)
	INDICE_RETOUR_INEXISTANT, // => pas d'indice précisé pour le retour de la fonction. Afficher la commande concernée et l'ignorer (passer à la commande suivante)
	TRAITEMENT_CMD_OK // => commande bien interprétée
} parserError ;

/*
 * Le fichier de commande lu
 */
FILE * entree;

/*
 * Infos fichier d'entrée
 */
struct stat entree_infos;

/*
 * Le fichier résultat (contient les retours des fonctions)
 */
FILE * res;

parserError chargerFichier(char* path);
char* lectureFichier();
void interpreteCommande(char* commande);
void interpreteCreation(char* cmd);
void interpreteChoixGraphe(char* cmd);

#endif