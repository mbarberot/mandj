#ifndef _GRAPHE_
#define _GRAPHE_


/*
 * Les erreurs
 */
typedef struct enum 
{       
        SOMMET_INVALIDE,
        POIDS_INVALIDE,
        MAX_SOMMET_INVALIDE,
        DEGRE_INVALIDE,
        NUMERO_GRAPHE_INVALIDE,
        NUMERO_GRAPHE_TROP_PETIT,
        GRAPHE_INEXISTANT,
        SOMMET_INEXISTANT,
        ARETE_INEXISTANTE,
        GRAPHE_DEJA_EXISTANT,
        SOMMET_DEJA_EXISTANT,
        ARETE_DEJA_EXISTANTE,
        MAX_SOMMET_DIFFERENT,
        SOMMET_DIFFERENT,
        ARETE_DIFFERENTE,
        DEGRE_DIFFERENT,
        TEST_OK,
        PROBLEME_MEMOIRE,
        COMMANDE_INVALIDE,
        RES_OK
};


/*
 * Fonctions
 */

choisirGraphe(int idGraphe);            // Todo
chargerGraph();                         // Todo, Arg?
creation(int maxSommet);                // Todo
modifierNbMaxSommet(int maxSommet);     // Todo
suppressionGraphe(int idGraphe);        // Todo
insertionSommet(int nvSommet);          // Todo
suppressionSommet(int sommet);          // Todo
insertionArete(int sommetDep,
                int poids,
                int sommetArr,
                char oriente);          // Todo
modifierPoids(int sommetDep,
                int nvPoids,
                int sommetArr,
                char oriente);          // Todo
suppressionArete(int sommetDep,
                int sommetArr,
                char oriente);          // Todo
viderGraphe()                           // Todo
viderAreteGraphe()                      // Todo
testerArete(int idGraphe,
            int sommetDep,
            int poids,
            int sommetArr,
            char oriente,
            int resAttendu);            // Todo
testerSommet(int idGraphe,
             int X,Res);                // Todo
testerDegreSommet(int idGraphe,
                  int sommet,
                  int value,
                  int resAttendu);      // Todo
compareGraphe(int resAttendu);          // Todo
compareSommet(int sommet,
              int resAttendu);          // Todo


#endif
