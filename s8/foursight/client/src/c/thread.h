#ifndef __SCSMOIATHREAD__
#define __SCSMOIATHREAD__

/*
 * Projet MOIA - SCS
 * 2011 - 2012
 *
 * Fichier d'entête pour les threads :
 *  fournis les structures de données pour gérer
 *  des threads posix.
 *
 * Barberot Mathieu &
 * Racenet Joan
 *
 */

// Librairies :
// - threads POSIX
#include <pthread.h>

// Fichiers
// - description du protocole
#include "protocole.h"

// Structure pour gérer le premier thread à terminer
typedef struct Shared_vars 
{
    // Mutex pour gérer l'accès aux données
    pthread_mutex_t mutex ; 

    // Boolean
    // 0 = aucun thread terminé
    // 1 = un thread terminé
    int fini ;		   

    // Boolean
    // 0 = le thread ia n'est pas terminé
    // 1 = le thread ia est terminé
    int ia_first;
    
    // Socket de communication avec l'arbitre
    int socket ;

    // Structure modélisant un coup
    // Initialisé dans le thread de l'ia
    TypCoupReq *coup ;
    
} Shared_vars ;

void thread_init(Shared_vars *shvr);

#endif
