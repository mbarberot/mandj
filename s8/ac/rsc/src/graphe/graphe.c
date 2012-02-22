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
	if(pcId < 0)
	{
		return NUMERO_GRAPHE_TROP_PETIT;
	}
	else if(pcId > 1)
	{
		return NUMERO_GRAPHE_INVALIDE;
	}
	else
	{
		grapheCourant = pcId;
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
				g->aretes = (TypVoisins**)malloc(g->nbMaxSommets * sizeof(TypVoisins*));
				
				// Initialisation de la liste des sommets
				for(int i = 0 ; i < g->nbMaxSommets ; i++){
				  initialiseListe(g->aretes[i]);
				}
				
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

/**
* Change le nombre de sommets max sur le graphe courant.
* Plusieurs cas :
* - Si maxSommet > maxSommet en cours : on rajoute une nouvelle entrée dans le tableau des arêtes.
* - Si maxSommet < maxSommet en cours : on doit supprimer des entrées dans le tableau des arêtes (et supprimer dans les autres listes les références aux sommets supprimés).
* @param maxSommet : le nouveau nombre de sommets
*/
erreur modifierNbMaxSommet(int maxSommet){
  TypGraphe* current = graphes[grapheCourant];
  TypGraphe* new;
  
  /* Le nouveau nombre max de sommet est supérieur */
  if(maxSommet > current->nbMaxSommets)
  {
    
    new = (TypGraphe*) malloc(sizeof(TypGraphe));
    
    if(new != NULL) // L'allocation a réussi ?
    {
      new->nbMaxSommets = maxSommet
      new->aretes = (TypVoisins**)malloc(g->nbMaxSommets * sizeof(TypVoisins*));
      
      // On effectue la recopie de l'ancien graphe dans le nouveau
      for(int i = 0 ; i < current -> nbMaxSommets ; i++){
	initialiseListe(new->aretes[i]);
	
	// Copie des éléments de la liste
	
      }
    }
  }
  else
  {
  }
  
}

