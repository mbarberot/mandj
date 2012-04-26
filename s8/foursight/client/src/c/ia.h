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

// Librairies
// - standard
// - entrées/sorties
#include <stdio.h>
#include <stdlib.h>

// Fichier de gestion :
// - des threads POSIX
// - du jeu
#include "thread.h"
#include "jeu.h"

// Enumération des retours de fonctions
typedef enum { IA_OK, IA_ERR } ia_err ;

//
// Fonctions :
//

/**
 * Calcule un coup
 * Lancé dans un thread POSIX pour permettre l'écoute de l'arbitre en parallèle
 *
 * @param coup	    Le coup calculé
 * @return IA_OK    Le calcul s'est bien passé
 * @return IA_ERR   Une erreur s'est produite
 */
void* ia_calculeCoup(void *arg);

#endif
