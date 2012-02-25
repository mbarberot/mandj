/*
 * Librairie de gestion de graphes
 * Fichier source 
 *
 *
 * AC 2011-2012
 * Barberot Mathieu et Joan Racenet
 * Master 1 Informatique
 */

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
    int i;
    // Y a-t-il déjà un graphe ?
    if( graphes[grapheCourant] == NULL )
    {
	// Il n'y a pas de graphe.
	// Le nombre de sommet est-il valide ?
	if( nbSommet <= 0 )
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
		for( i = 0 ; i < g->nbMaxSommets ; i++){
		    g->aretes[i] = NULL;
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
erreur modifierNbMaxSommet(int maxSommet)
{
    
    TypGraphe* current = graphes[grapheCourant];
    TypGraphe* new;
    int i;
    int lim;
    
    /* Permet de fixer une limite pour la recopie des éléments du graphe */
    lim = (maxSommet > current->nbMaxSommets)? current->nbMaxSommets : maxSommet;  
    
    new = (TypGraphe*) malloc(sizeof(TypGraphe));
    
    if(new != NULL) // L'allocation a réussi ?
    {
	new->nbMaxSommets = maxSommet;
	new->aretes = (TypVoisins**)malloc(new->nbMaxSommets * sizeof(TypVoisins*));
	
	// On effectue la recopie de l'ancien graphe dans le nouveau
	for(i = 0 ; i < lim ; i++)
	{
	    new->aretes[i] = NULL;
	    
	    /* Copie des éléments de la liste */
	    TypVoisins* tmp = current->aretes[i];
	    
	    while(tmp != NULL){
		if(tmp->voisin < lim)
		{ // tmp->voisin < lim : on évite de rajouter des sommets supprimés
		    ajouteVoisin(&new->aretes[i], tmp->voisin , tmp->poidsVoisin, tmp->info);		
		}
		tmp = tmp -> voisinSuivant;
	    }
	    
	}
	
	/* Si le nouveau nombre max de sommets et supérieur : on initialise les sommets en plus */
	if(lim == current -> nbMaxSommets){
	    for(i = lim ; i < maxSommet ; i++) new->aretes[i] = NULL;
	}
	// supprimer le graphe courant
	suppressionGraphe(grapheCourant);
	
	// On assigne le nouveau graphe
	graphes[grapheCourant] = new;
    }
    
}

/**
 * Affichage d'un graphe (désigné par idGraphe) en mode texte.
 * @param idGraphe : un entier désignant un des deux graphes
 */

void afficheGraphe(int idGraphe)
{
    int i;
    TypGraphe* current = graphes[idGraphe];
    
    for(i = 0; i < current->nbMaxSommets ; i++){	
	    printf("Sommet : %d : \n",i+1); 
	    if(current->aretes[i] != NULL)
	    {
		afficheVoisins(&current->aretes[i]);
	    }	
    }
    
    printf("\n");
}

/**
 * Suppression d'un graphe (désigné par idGraphe). Si idGraphe vaut 0, on supprime les deux graphes
 * @param idGraphe : le(s) graphe(s) à supprimer 
 */
erreur suppressionGraphe(int idGraphe)
{
    if(idGraphe == 0){ // On supprime les deux graphes
	suppressionGraphe(1);
	suppressionGraphe(2);
    }
    else
    {
	int i;
	TypGraphe *current = graphes[idGraphe - 1];
	if(current != NULL)
	{
	    for(i = 0 ; i < current->nbMaxSommets ; i++)
	    {
		supprimeListe(&current->aretes[i]);
	    }
	    free(current->aretes);
	    free(current);
	}
    }
}

/**
 * Création d'un nouveau sommet dans le graphe courant. nvSommet doit être compris entre 0 et nbMaxSommets
 * En pratique, la fonction initialise la case du tableau correspondant au nouveau sommet
 * @param nvSommet : le nouveau sommet à insérer
 */
erreur insertionSommet(int nvSommet)
{
    int som = nvSommet - 1;
    
    if(som >= 0 && som < graphes[grapheCourant]->nbMaxSommets)
    {
	if(graphes[grapheCourant] != NULL)
	{
	    initialiseListe(&graphes[grapheCourant]->aretes[som]);
	}
	else
	{
	    // Sommet déjà existant
	}
    }
    else
    {
	// Sommet invalide
    }
}

/**
 * Suppression du sommet dans le graphe. On supprime de même toutes les références à se sommet dans le reste des aretes
 * @param sommet : le sommet à retirer (compris entre 1 et nbMaxSommets)
 */
erreur suppressionSommet(int sommet)
{
}

