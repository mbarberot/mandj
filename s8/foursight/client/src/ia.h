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

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

// Fichier du protocole
// -> pour les structures et énumérations
#include "jeu.h"

typedef enum { IA_OK, IA_ERR } ia_err ;

// Mutex pour le calcul du coup
typedef struct data {
    TypCoupReq *coup;
    pthread_mutex_t mutex;
} Data ;

//
// Fonctions :
//

/**
 * Calcule un coup
 * Lance un thread POSIX pour permettre l'écoute de l'arbitre en parallèle
 *
 * @param coup	    Le coup calculé
 * @return IA_OK    Le calcul s'est bien passé
 * @return IA_ERR   Une erreur s'est produite
 */
void* ia_calculeCoup(void *arg);

/**
 * Arrête le calcul du coup (timeout) :
 * - arrêt du thread
 * - arrêt de l'ia (TODO)
 *
 */
ia_err ia_arretCalcul();

#endif
