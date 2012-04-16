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
void listeCouplage(TypVoisins *ls, TypVoisins *res);

/**
 * Retourne la liste des degrés de sommets impairs du graphe
 * @param idGraphe : le graphe dont on veut tester les sommets
 * @return : la liste de tous les sommets impairs
 */
TypVoisins* sommetsImpairs(int idGraphe);


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
 * @return		
 */
erreur calculCycleEulerien(
	int idGraphe,
	int idHeuristique
	);			// Todo


/**
 * Implémentation de Floyd-Warshall de recherche du plus court chemin
 * Utilisé pour la résolution du postier chinois
 */
int plusCourtChemin(int idGraphe, int dep, int arr);

#endif
