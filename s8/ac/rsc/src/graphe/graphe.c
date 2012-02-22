/*
 * Librairie de gestion de graphes
 * Fichier source 
 *
 *
 * AC 2011-2012
 * Barberot Mathieu et Joan Racenet
 * Master 1 Informatique
 */

#include "liste.h"
#include "graphe.h"

/**
 * Choisi un des deux graphes que la librairie permet de gérer
 * @param idGraphe - L'id du graphe (0 ou 1)
 */
erreur choisirGraphe(int idGraphe)
{
	int pcId = idGraphe -1; // Conversion de "1 ou 2" en "0 ou 1"
	if(idGraphe < 0)
	{
		return NUMERO_GRAPHE_TROP_PETIT;
	}
	else if(idGraphe > 1)
	{
		return NUMERO_GRAPHE_INVALIDE;
	}
	else
	{
		grapheCourant = idGraphe;
		return RES_OK;
	}
}

/**
 * Crée un nouveau graphe à l'indice courant.
 * Si un graphe est dèjà présent, une erreur "GRAPHE_DEJA_EXISTANT" 
 *	sera retournée.
 * @param nbSommet - Le nombre de sommets du nouveau graphe
 */
erreur creation(int nbSommet)
{
	// Y a-t-il déjà un graphe ?
	if( graphes[grapheCourant] == NULL )
	{
		// Il n'y a pas de graphe.
		// Le nombre de sommet est-il valide ?
		if( nbSommet < 0 )
		{
			// Nombre de sommet négatif
			return MAX_SOMMET_INVALIDE;
		}
		else
		{
			// Nombre de sommets positif ou null (graphe vide)
			// Création du graphe
			TypGraphe *g = (TypGraphe*) malloc(sizeof(TypGraphe));
			// L'allocation a-t-elle réussie ?
			if( g != NULL )
			{
				// Allocation réussie
				// On instancie la structure
				g->nbMaxSommets = nbSommet;
				g->aretes = NULL;
				// On place la structure dans le tableau à
				//  l'indice courant
				graphes[grapheCourant] = g;
				// Tout s'est bien passé :
				return RES_OK;
			}
			else
			{
				// L'allocation a échoué
				return PROBLEME_MEMOIRE;
			}
		}
	}
	else
	{
		// Un graphe est déjà présent
		return GRAPHE_DEJA_EXISTANT;
	}
}
