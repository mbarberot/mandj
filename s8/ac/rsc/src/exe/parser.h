#ifndef _PARSER_
#define _PARSER_

/**
 * 	Fonctions utilitaires pour la lecture et l'interprétation
 * 	des fichiers de commandes.
 * 
 * 	@author Mathieu BARBEROT
 * 	@author Joan RACENET
 */

/**
 * @TODO : faire les fonctions d'interprétations de chargerGraphe
 * @TODO : écriture dans le fichier résultat
 */


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
	CREATION_RESULTAT_IMPOSSIBLE,
	ECRITURE_IMPOSSIBLE,
	TRAITEMENT_FICHIER_OK, // => afficher que l'interprétation a été un succès
	FICHIER_COMMANDES_INEXISTANT, // => afficher que le fichier n'a pas été trouvé
	ARGUMENTS_INCORRECTS, // => nb d'arguments invalide ou de mauvais type. Afficher la commande concernée et l'ignorer (passer à la commande suivante)
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
FILE * fileRes;

/*
 * Fonctions utilitaires
 */
char* parserErrorToString(parserError err);

/*
 * Fonctions de manipulation des fichiers
 */
parserError chargerFichier(char* path);
void lectureFichier(char* res);
void ecritureResultatCommande(int numCommande, erreur res);

/*
 * Fonctions d'interpretations
 */
void interpreteCommande(char* commande);
parserError interpreteCreation(char* cmd);
parserError interpreteChoisirGraphe(char* cmd);
parserError interpreteModifierNbMaxSommet(char* cmd);
parserError interpreteSuppressionGraphe(char* cmd);
parserError interpreteInsertionSommet(char* cmd);
parserError interpreteSuppressionSommet(char* cmd);
parserError interpreteInsertionArete(char* cmd);				
parserError interpreteModifierPoids(char* cmd); 			
parserError interpreteSuppressionArete(char* cmd);				
parserError interpreteViderGraphe(char* cmd);			
parserError interpreteViderAreteGraphe(char* cmd); 			
parserError interpreteTesterArete(char* cmd);		
parserError interpreteTesterSommet(char* cmd); 
parserError interpreteTesterDegreSommet(char* cmd);		
parserError interpreteCompareGraphe(char* cmd);
parserError interpreteCompareSommet(char* cmd);

parserError interpreteChargerGraphe(char* cmd);

#endif