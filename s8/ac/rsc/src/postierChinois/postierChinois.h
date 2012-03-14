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


/**
 * Duplique l'arète du graphe courant allant du sommet s1 vers le sommet s2
 *
 * @param s1	    ID du sommet de départ 
 * @param s2	    ID du sommet d'arrivée
 * @return	    
 */
erreur dupliqueArete(
	int s1,
	int s2
	);			// Todo

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
