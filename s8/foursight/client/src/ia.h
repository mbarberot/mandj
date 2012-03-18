#ifndef __IASCSMOIA__
#define __IASCSMOIA__

/*
 * Projet MOIA - SCS
 * 2011 - 2012
 *
 * Couche de gestion de l'IA
 * Fichier d'entête
 *
 * Barberot Mathieu &
 * Racenet Joan
 *
 */


// Fichier du protocole
// -> pour les structures et énumérations
#include "protocole.h"
#include "jeu.h"

typedef enum { IA_OK, IA_ERR } ia_err ;

//
// Fonctions :
//


/**
 * Calcule un coup
 *
 * @param plateau   Plateau de jeu
 * @param coup	    Le coup calculé
 */
ia_err ia_calculeCoup(
	TypCoupReq *coup
	);




#endif
