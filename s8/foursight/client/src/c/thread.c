/**
 * Projet MOIA - SCS
 *
 * Implémentation du fichier d'entête thread.h
 *
 * Barberot Mathieu &
 * Racenet Joan
 *
 */

#include "thread.h"

/**
 * Initialise les données partagées des threads
 *
 * @param shvr	Structure partagée par les threads
 */
void thread_init(Shared_vars *shvr)
{
    shvr->fini = 0;
    shvr->ia_first = 0;
}
