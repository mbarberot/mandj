/*
 * Algorithmique Combinatoire
 * Mars - Avril - Mai 2012
 * 
 * Barberot Mathieu & Joan Racenet
 *
 *
 * Surcouche de la bibliothèque
 *
 */
#ifndef __postierChinois__
#define __postierChinois__

// Librairie de manipulation des graphes
#include "graphe.h"

/** Nombre d'heuristique implantées */
#define NB_HEURISTIQUE 0

/** Matrices d'adjacence pour les deux graphes. C(x,y) représente le nombre d'arêtes entre le sommet x et le sommet y */
int **nbAretes[NB_GRAPHES];

/** Le fichier contenant le résultat du parcours chinois*/
FILE *resPostier;

/**
 * ----------------------------------------------------------
 * Fonctions permettant de travailler sur le tableau d'arêtes
 * ----------------------------------------------------------
 */


/**
 * Renvoie le nombre de voisins accessibles pour un sommet
 */
int getNbVoisinsAccessibles(int idGraphe, int sommet);

/**
 * Prépare le tableau de recensement des arêtes
 * en vue d'un parcours chinois.
 * 
 * @param idGraphe  Le graphe utilisé pour le parcours chinois (0 = les deux graphes)
 * @return NUMERO_GRAPHE_INVALIDE
 * @return GRAPHE_INEXISTANT
 * @return RES_OK
 */
erreur initParcoursChinois(
	int idGraphe	
	);

/**
 * Peuple le tableau 'nbAretes' avec le graphe n° 'idGraphe' 
 * et alloue la mémoire nécessaire
 *
 * @param idGraphe  L'id du graphe, 1 ou 2
 * @return 
 */
erreur peupleTabAretes(int idGraphe);

/**
 * Libère la mémoire allouée au tablean nbAretes
 *
 * @return
 */
erreur freeParcoursChinois();


/**
 * Duplique l'arête du graphe allant du sommet s1 vers le sommet s2
 *
 * @param s1			ID du sommet de départ 
 * @param s2			ID du sommet d'arrivée
 * @param idGraphe		ID du graphe dont on veut dupliquer une arête
 * @return GRAPHE_INEXISTANT	Le graphe courant n'existe pas
 * @return SOMMET_INVALIDE	L'un ou les deux sommets n'existents pas
 * @return ARETE_INEXISTANTE	Aucune arête ne relie ces deux sommets
 */
erreur dupliqueArete(
	int idGraphe,
	int s1,
	int s2
	);

/**
 * Supprime l'arête du graphe allant du sommet s1 vers le sommet s2
 * @param idGraphe		ID du graphe à traiter
 * @param s1			ID du sommet de départ
 * @param s2			ID du sommet d''arrivée
 * @return GRAPHE_INEXISTANT	Le graphe idGraphe n'existe pas
 * @return SOMMET_INVALIDE	Un (ou les deux) sommets n'existe(nt) pas
 * @return ARETE_INEXISTANTE	Aucune arête ne relie ces deux sommets
 */
erreur supprimeArete(
    int idGraphe, 
    int s1,
    int s2
       );

/**
 * Renvoie la liste des sommets de degrés impairs (ls) classés par numéro de sommet
 * @param ls : la liste des sommets de degrés impairs
 * @param res : la liste resultante
 */
void listeCouplage(int idGraphe, TypVoisins *ls, TypVoisins **res);

/**
 * Retourne la liste des degrés de sommets impairs du graphe
 * @param idGraphe : le graphe dont on veut tester les sommets
 * @return : la liste de tous les sommets impairs
 */
void sommetsImpairs(int idGraphe, TypVoisins **res);


/**
 * ----------------------------------
 * Fonctions traitant les algorithmes
 * ----------------------------------
 */


/**
 * Teste si le graphe est eulérien
 *
 * @param idGraphe  ID du graphe à tester
 * @param res	    1 = cycle eulérien possible, 0 = cycle chinois possible
 * @return  
 */
erreur isGrapheEulerien(
	int idGraphe,
	int* res
	);


/**
 * Effectue un cycle eulérien
 * 
 * @param idGraphe ID du graphe à tester
 * @return 
 */
TypVoisins* cycleEulerien(int idGraphe, int x);


/**
 * Calcule un Cycle Eulérien.
 * Modifie le graphe si nécessaire et utilise l'heuristique demandée
 *
 * @param idGraphe	ID du graphe (1 ou 2)
 * @param idHeuristique ID de l'heuristique (1 à NB_HEURISTIQUE)
 * @param res_path Le chemin du fichier d'entrée (où l'on stockera les fichiers résultats)
 * @return		
 */
erreur calculCycleEulerien(
	int idGraphe,
	int idHeuristique,
	char *res_path
	);

/**
 * Effectue les couplages en fonction de l'heuristique ainsi que le cycle eulérien
 * @param idGraphe ID du graphe à traiter
 * @param idHeuristique ( 0 : toutes les heuristiques, 1 : chemin optimal, 2 : premier couplage, 3 : aléatoire)
 * @param res_path : le chemin du fichier d'entrée
 */
void goCycleChinois(
    int idGraphe,
    int idHeuristique,
    char *res_path
     );

/**
 * ----------------------------------------------------------------------------------
 * Fonctions des couplages avec recherche de chemin basés sur l'algorithme "gourmand"
 * ----------------------------------------------------------------------------------
 */

/**
 * Fait le couplage en appliquant l'algorithme gourmand pour la duplication des arêtes. Les couples sont choisis dans l'ordre croissant des sommets
 * @param idGraphe : le graphe de référence
 */
void doCouplageSommetsCroissants(int idGraphe);

/**
 * Fait le couplage en appliquant l'algorithme gourmand pour la duplication des arêtes. Les couples sont choisis aléatoirement
 * @param idGraphe : le graphe de référence
 */
void doCouplageSommetsRandom(int idGraphe);

/**
 * Trouve un chemin entre som1 et som2 dans le graphe idGraphe et stocke les sommets traversés dans path
 */
void trouveCheminGourmand(int idGraphe, int som1, int som2, TypVoisins **path);


/**
 * -----------------------------------
 * Fonctions liées au couplage optimal
 * -----------------------------------
 */


/**
 * Teste tous les couples de sommets impairs possibles, et calcule celui de poids optimal.
 * La fonction duplique les arêtes en conséquent.
 * @param idGraphe : le graphe de référence
 */
void doCouplageOptimal(int idGraphe);

/**
 * Implémentation de Floyd-Warshall de recherche du plus court chemin
 * Utilisé pour la résolution du postier chinois
 * @param idGraphe : le graphe de référence
 * @param m : matrice contenant la pondération du plus court chemin entre deux sommets
 * @param p : matrice contenant le sommet précédent du plus court chemin entre deux sommets
 */
void plusCourtChemin(int idGraphe, int *m[], int *p[]);

/**
 * Evalue la valeur de couplage de la liste passée en paramètre
 * @param couples : le couplage à évaluer
 * @param m : la matrice de pondération
 */
int evalueCouplage(TypVoisins *couples, int *m[]);


/**
 * Retourne le couplage de poids minimal
 */
TypVoisins* calculeCoupleOptimal(TypVoisins *couples, int* m[]);

/**
 * Effectue la duplication des différentes arêtes trouvées dans le couplage 
 */
void duplicationCouplage(int idGraphe, TypVoisins *couples, int* p[]);


/**
 * -------------------------------------------
 * Fonctions de gestion des fichiers résultats
 * -------------------------------------------
 */

void ouvertureFichierRes(char* res_path);

void ecrireFichierRes(TypVoisins* res, int idGraphe, int idHeuristique);

char* aretesToString(TypVoisins* aretes, int idGraphe);

void ecrireFichierDot(int idGraphe, int idHeuristique, char* res_path);
#endif
