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

/** Tableau indiquant le nombre d'arêtes */
int **nbAretes[NB_GRAPHES];


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
 * et alloue la mémoire nécessaire.
 *
 * ATTENTION !
 * Cette méthode est utilisée par l'initialisation. Elle ne vérifie pas l'idGraphe
 * et ne devrait pas être utilisée hors de l'initialisation.
 *
 * @param idGraphe  L'id du graphe, 1 ou 2
 * @return 
 */
erreur peupleTabAretes(int idGraphe);

/**
 * Libère la mémoire allouée au tablean nbAretes
 *
 * @return RES_OK
 */
erreur freeParcoursChinois();


/**
 * Duplique l'arète du graphe courant allant du sommet s1 vers le sommet s2
 *
 * @param s1			ID du sommet de départ 
 * @param s2			ID du sommet d'arrivée
 * @return GRAPHE_INEXISTANT	Le graphe courant n'existe pas
 * @return SOMMET_INVALIDE	L'un ou les deux sommets n'existents pas
 * @return ARETE_INEXISTANTE	Aucune arête ne relie ces deux sommets
 */
erreur dupliqueArete(
	int s1,
	int s2
	); // TODO

/**
 * Teste si le graphe est eulérien
 *
 * @param idGraphe  ID du graphe à tester
 * @param res	    1 = Eulérien, 0 = Non-eulérien
 * @return  
 */
erreur isGrapheEulerien(
	int idGraphe,
	int* res
	); // TODO

/**
 * Effectue le parcours en profondeur d'un graphe donné
 * @param g : le graphe à parcourir
 * @param l : liste des sommets déjà parcourus (marquage)
 * @param origine : le sommet d'où commence le parcours
 * @return : le nombre de sommets parcourus
 */
int parcoursProfondeur(TypGraphe *g, TypVoisins** l, int origine);


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
 * Affiche un cycle Eulérien.
 * Exporte l'affichage en un fichier .dot
 *
 * @param idGraphe	ID du graphe (1 ou 2)
 * @param idHeuristique ID de l'heuristique (1 à NB_HEURISTIQUE)
 * @return		
 */
erreur afficheCycleEulerien(
	int idGraphe,
	int idHeuristique
	);

#endif
