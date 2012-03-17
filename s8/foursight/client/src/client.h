#ifndef __CLIENTSCSMOIA__
#define __CLIENTSCSMOIA__

/*
 * Projet MOIA - SCS
 * 2011 - 2012
 * 
 * Client de connection à l'arbitre
 * Fichier d'entête
 *
 * Barberot Mathieu &
 * Racenet Joan
 *
 */

// Pour la fonction "strcpy"
#include <string.h>

// Librairie de manipulation des sockets
#include "fctSock.h"

// Protocole de connexion au serveur
#include "protocole.h"

/*
 * Etat du client
 *
 * ST_KO 		Echec d'une étape
 * ST_IDENTIFICATION 	Identification auprès de l'arbitre
 * ST_PARTIE		Demande d'une partie auprès de l'arbitre
 * ST_COUP		Calcul et envoi de nos coups & attente des coups adverses
 * ST_DECONNEXION	Fin du tournoi, déconnexion du serveur
 *
 */
typedef enum {
    ST_KO,
    ST_IDENTIFICATION,
    ST_PARTIE,
    ST_COUP,
    ST_DECONNEXION
} State ;


/*
 * Fonctions
 */

State demandeIdentification(
	int sockArbitre,
	char login[],
	int *joueur
	);

State demandePartie(
	int sockArbitre,
	int joueur,
	TypBooleen *finTournoi,
	TypBooleen *premier,
	int *adversaire
	);

State calculCoup(
	int sockArbitre,
	TypBooleen premier
	);

#endif
