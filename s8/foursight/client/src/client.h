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

typedef enum {
    CONN_OK,	CONN_ERR,
    IDENT_OK,	IDENT_ERR,  IDENT_LOGIN,
    PARTIE_OK,	PARTIE_ERR,  PARTIE_JOUEUR,
    COUP_OK,	COUP_ERR,   COUP_INVALIDE,  COUP_TIMEOUT
} client_err;


/*
 * Fonctions
 */


/**
 * Affiche un message d'erreur pour les erreurs définies dans protocole.h
 * Attention : Ne fonctionne qu'en mode DEBUG
 *
 * @param err	Le type d'erreur à afficher
 */
void client_perror(TypErreur err);

/**
 * Connexion à l'arbitre
 *
 * @param machine	Machine qui héberge le serveur
 * @param port		Port de la machine qui héberge le serveur
 * @param sockArbitre	Socket de communication créé lors de la connexion
 * @return CONN_OK	Tout s'est bien passé
 * @return CONN_ERR	Erreur lors de la connexion
 */
client_err client_connexion(
	char machine[],
	int port,
	int *sockArbitre
	);

/**
 * Identification auprès de l'arbitre
 *
 * @param sockArbitre	Socket de communication avec l'arbitre
 * @param login		Login du joueur
 * @param joueur	Numéro du joueur donné par l'arbitre
 * @return IDENT_OK	Tout s'est bien passé
 * @return IDENT_ERR	Erreur lors des transmissions
 * @return IDENT_LOGIN	Login incorrect
 */
client_err client_identification(
	int sockArbitre,
	char login[],
	int *joueur
	);

/**
 * Demande une partie à l'arbitre
 *
 * @param sockArbitre	    Socket de communication avec l'arbitre
 * @param joueur	    Numéro du joueur
 * @param finTournoi	    VRAI si le tournoi est terminé, FAUX sinon
 * @param premier	    VRAI si le joueur est le premier à jouer, FAUX sinon
 * @param adversaire	    Numéro de l'adversaire
 * @return PARTIE_OK	    Tout s'est bien passé
 * @return PARTIE_ERR	    Erreur lors des transmissions
 * @return PARTIE_JOUEUR    No Joueur incorrect
 */
client_err client_partie(
	int sockArbitre,
	int joueur,
	TypBooleen *finTournoi,
	TypBooleen *premier,
	int *adversaire
	);

/**
 * Envoie un coup à l'arbitre et attend la validation.
 *
 * @param sockArbitre	    Socket de communication avec l'arbitre
 * @param coup		    Coup du joueur
 * @return COUP_OK	    Le coup est valide et tout s'est bien passé
 * @return COUP_ERR	    Erreur lors des transmissions
 * @return COUP_INVALIDE    Le coup est invalide
 * @return COUP_TIMEOUT	    Le coup est en timeout
 */
client_err client_envoieCoup(
	int sockArbitre,
	TypCoupReq *coup
	);

/**
 * Attend la validation du coup de l'adversaire puis son coup.
 *
 * @param sockArbitre	    Socket de communication avec l'arbitre
 * @param coup		    Coup de l'adversaire
 * @return COUP_OK	    Le coup de l'adversaire est valide et tout s'est bien passé
 * @return COUP_ERR	    Erreur lors des transmissions
 * @return COUP_INVALIDE    Le coup de l'adversaire est invalide
 * @return COUP_TIMEOUT	    L'adversaire est en timeout
 */
client_err client_attendCoup(
	int sockArbitre,
	TypCoupReq *coup
	);

#endif
