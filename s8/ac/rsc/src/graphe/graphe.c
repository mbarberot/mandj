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
 * Renvoie une erreur sous forme de chaine de caractères
 * @param err : l'erreur à afficher
 * @return l'erreur sous la forme d'une chaine de caractères
 */
char* errToString(erreur err)
{
    switch(err)
    {
	case SOMMET_INVALIDE:
	    return "SOMMET_INVALIDE";	    
	case POIDS_INVALIDE:
	    return "POIDS_INVALIDE";
	case MAX_SOMMET_INVALIDE:
	    return "MAX_SOMMET_INVALIDE";	    
	case DEGRE_INVALIDE:
	    return "DEGRE_INVALIDE";
	case NUMERO_GRAPHE_INVALIDE:
	    return "NUMERO_GRAPHE_INVALIDE";	    
	case NUMERO_GRAPHE_TROP_PETIT:
	    return "NUMERO_GRAPHE_TROP_PETIT";
	case GRAPHE_INEXISTANT:
	    return "GRAPHE_INEXISTANT";
	case SOMMET_INEXISTANT:
	    return "SOMMET_INEXISTANT";
	case ARETE_INEXISTANTE:
	    return "ARETE_INEXISTANTE";
	case GRAPHE_DEJA_EXISTANT:
	    return "GRAPHE_DEJA_EXISTANT";
	case SOMMET_DEJA_EXISTANT:
	    return "SOMMET_DEJA_EXISTANT";
	case ARETE_DEJA_EXISTANTE:
	    return "ARETE_DEJA_EXISTANTE";
	case MAX_SOMMET_DIFFERENT:
	    return "MAX_SOMMET_DIFFERENT";
	case SOMMET_DIFFERENT:
	    return "SOMMET_DIFFERENT";
	case ARETE_DIFFERENTE:
	    return "ARETE_DIFFERENTE";
	case DEGRE_DIFFERENT:
	    return "DEGRE_DIFFERENT";
	case TEST_OK:
	    return "TEST_OK";
	case PROBLEME_MEMOIRE:
	    return "PROBLEME_MEMOIRE";
	case COMMANDE_INVALIDE:
	    return "COMMANDE_INVALIDE";
	case RES_OK:
	    return "RES_OK";
	case TEST_KO:
	    return "TEST_KO";
	default:
	    return "UNKNOWN_ERROR";
    }
}


/**
 * Affichage d'un graphe (désigné par idGraphe) en mode texte.
 * @param idGraphe : un entier désignant un des deux graphes
 */

void afficheGraphe(int idGraphe)
{
    int i;
    
    if(idGraphe > 0 && idGraphe <= NB_GRAPHES)
    {
	TypGraphe* current = graphes[idGraphe - 1];
	
	if(current != NULL){
	    
	    printf("Affichage du graphe %d \n", idGraphe);
	    
	    for(i = 0; i < current->nbMaxSommets ; i++){	
		printf("Sommet : %d : \n",i+1); 
		if(current->aretes[i] != NULL)
		{
		    afficheVoisins(&current->aretes[i]);
		}	
	    }
	    
	    printf("\n");
	}
    }
}


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
 * @return MAX_SOMMET_INVALIDE : le nb de sommets précisés est invalide
 * @return PROBLEME_MEMOIRE : l'allocation en mémoire du graphe a échoué
 * @return RES_OK : l'opération s'est correctement déroulée
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
		
		if(g->aretes == NULL)
		    return PROBLEME_MEMOIRE;
		
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
 * @return MAX_SOMMET_INVALIDE : le maxSommet entrée est incorrect
 * @return PROBLEME_MEMOIRE : le graphe n'a pu être alloué
 * @return GRAPHE_INEXISTANT : le graphe courant n'est pas initialisé
 * @return RES_OK : l'opération s'est correctement déroulée
 */
erreur modifierNbMaxSommet(int maxSommet)
{
    
    TypGraphe* current = graphes[grapheCourant];
    TypGraphe* new;
    int i;
    int lim;
    
    // Le graphe courant est-il bien défini en mémoire ?
    if(current != NULL){
	
	// On vérifie que le paramètre est correct
	if(maxSommet < 1 ) 
	    return MAX_SOMMET_INVALIDE;
	
	// Permet de fixer une limite pour la recopie des éléments du graphe
	lim = (maxSommet > current->nbMaxSommets)? current->nbMaxSommets : maxSommet;  
	
	new = (TypGraphe*) malloc(sizeof(TypGraphe));
	
	// L'allocation du graphe a réussi ?
	if(new != NULL) 
	{
	    new->nbMaxSommets = maxSommet;
	    new->aretes = (TypVoisins**)malloc(new->nbMaxSommets * sizeof(TypVoisins*));
	    
	    if(new->aretes == NULL)
		return PROBLEME_MEMOIRE;
	    
	    // On effectue la recopie de l'ancien graphe dans le nouveau
		for(i = 0 ; i < lim ; i++)
		{
		    new->aretes[i] = NULL;
		    
		    // Copie des éléments de la liste
		    TypVoisins* tmp = current->aretes[i];
		    
		    while(tmp != NULL){
			// tmp->voisin < lim : on évite de rajouter des sommets supprimés
			if(tmp->voisin < lim)
			{ 
			    ajouteVoisin(&new->aretes[i], tmp->voisin , tmp->poidsVoisin, tmp->info);		
			}
			tmp = tmp -> voisinSuivant;
		    }
		    
		}
		
		// Si le nouveau nombre max de sommets et supérieur : on initialise les sommets en plus
		if(lim == current -> nbMaxSommets){
		    for(i = lim ; i < maxSommet ; i++) new->aretes[i] = NULL;
		}
		// supprimer le graphe courant
		suppressionGraphe(grapheCourant);
		
		// On assigne le nouveau graphe
		graphes[grapheCourant] = new;
	}
	else
	{
	    return PROBLEME_MEMOIRE;
	}
    }
    else
    {
	return GRAPHE_INEXISTANT;
    }
    
    return RES_OK;
}



/**
 * Suppression d'un graphe (désigné par idGraphe). Si idGraphe vaut 0, on supprime les deux graphes
 * @param idGraphe : le(s) graphe(s) à supprimer 
 * @return RES_OK : l'opération s'est déroulée avec succès
 * @return GRAPHE_INEXISTANT : le graphe à supprimer n'existe pas en mémoire
 * @return NUMERO_GRAPHE_INVALIDE : l'id du graphe demandé est invalide
 * @return NUMERO_GRAPHE_TROP_PETIT : l'id du graphe demandé est négatif
 */
erreur suppressionGraphe(int idGraphe)
{
    if(idGraphe > 2)    
	return NUMERO_GRAPHE_INVALIDE;
    
    if (idGraphe < 0)
	return NUMERO_GRAPHE_TROP_PETIT;
    
    if(idGraphe == 0)
    {
	suppressionGraphe(1);
	suppressionGraphe(2);
	return RES_OK;
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
	else
	{
	    return GRAPHE_INEXISTANT;
	}
	
	return RES_OK;
    }
    
}

/**
 * Création d'un nouveau sommet dans le graphe courant. nvSommet doit être compris entre 0 et nbMaxSommets
 * En pratique, la fonction initialise la case du tableau correspondant au nouveau sommet
 * @param nvSommet : le nouveau sommet à insérer
 * @return RES_OK : l'opération s'est déroulée correctement
 * @return PROBLEME_MEMOIRE : probleme d'initialisation du nouveau sommet
 * @return GRAPHE_INEXISTANT : le graphe en cours n'est pas initialisé
 * @return SOMMET_DEJA_EXISTANT : le sommet demandé est déjà initialisé
 * @return SOMMET_INVALIDE : le sommet a initialiser n'est pas compris dans le graphe
 */
erreur insertionSommet(int nvSommet)
{
    TypGraphe* current = graphes[grapheCourant];    
    int toAdd = nvSommet - 1;
    
    if(current == NULL)
	return GRAPHE_INEXISTANT;
    
    if(toAdd >= 0 && toAdd < graphes[grapheCourant]->nbMaxSommets)
    {
	if(current->aretes[toAdd] == NULL)
	{
	    if((initialiseListe(&current->aretes[toAdd])) != 1)
		return PROBLEME_MEMOIRE;
	}
	else
	{
	    return SOMMET_DEJA_EXISTANT;
	}
    }
    else
    {
	return SOMMET_INVALIDE;
    }
    
    return RES_OK;
}

/**
 * Suppression du sommet dans le graphe courant.
 * On supprime de même toutes les références à se sommet dans le reste des aretes
 * @param sommet : le sommet à retirer (compris entre 1 et nbMaxSommets)
 * @return GRAPHE_INEXISTANT : le graphe courant n'est pas initialisé
 * @return SOMMET_INVALIDE : le sommet à retirer n'est pas dans le graphe
 * @return SOMMET_INEXISTANT : le sommet à supprimer n'est pas initialisé
 * @return RES_OK : l'opération s'est déroulée avec succès
 */
erreur suppressionSommet(int sommet)
{
    int i;
    int toDel = sommet - 1 ;
    TypGraphe* current = graphes[grapheCourant];
    
    if(current == NULL)
	return GRAPHE_INEXISTANT;
    
    if(toDel < 0 || toDel >= current-> nbMaxSommets)
	return SOMMET_INVALIDE;
    
    if(current->aretes[toDel] != NULL)
    {
	for(i = 0 ; i < current->nbMaxSommets ; i++ )
	{	    
	    if( i == toDel)
	    {
		supprimeListe(&current->aretes[i]);
		current->aretes[i] = NULL;
	    }
	    else
	    {
		supprimeVoisin(&current->aretes[i], toDel);
	    }
	}
    }
    else
    {
	return SOMMET_INEXISTANT;
    }
    
    return RES_OK;
}

/**
 * Insertion d'une arête partant de sommetDep, allant à sommetArr en précisant sa pondération
 * Si oriente vaut 'o' => on ne rajoute que l'arete sommetdep -> sommetArr
 * Si oriente vaut 'n' => on rajoute aussi l'arete sommetArr -> sommetDep
 * @param sommetDep : le sommet de départ
 * @param poids : la pondération de l'arête
 * @param sommetArr : le sommet d'arrivée
 * @param oriente : un char précisant si l'arete est orientée ou non
 * @return RES_OK : l'opération s'est déroulée avec succès
 * @return GRAPHE_INEXISTANT : le graphe courant n'est pas initialisé
 * @return PROBLEME_MEMOIRE : l'initialisation du nouvel élément a échoué
 * @return SOMMET_INVALIDE : un des deux sommets (voire les deux) est invalide
 * @return SOMMET_INEXISTANT : un des deux sommets (voire les deux) n'est pas initialisé
 * @return POIDS_INVALIDE : le poids est négatif
 * @return COMMANDE_INVALIDE : oriente n'est ni 'o' ni 'n'
 * @return ARETE_DEJA_EXISTANTE l 'arête a créer existe déjà.
 */
erreur insertionArete(int sommetDep,int poids,int sommetArr,char oriente)
{
    TypGraphe *current = graphes[grapheCourant];
    
    
    // Le graphe courant existe
    if(current == NULL)
	return GRAPHE_INEXISTANT;
    
    // Les numéros des sommets sont valides
    if(sommetDep <= 0 || sommetDep > current->nbMaxSommets || sommetArr <= 0 || sommetArr >current->nbMaxSommets)
	return SOMMET_INVALIDE;
    
    // Le poids est valide?
    if(poids < 0)
	return POIDS_INVALIDE;
    
    // On vérifie si les sommets existent, et on effectue la création de l'arête
    if(&current->aretes[sommetDep-1] != NULL && &current->aretes[sommetArr -1] != NULL){
	if(oriente == 'o')
	{
	    int err;
	    err = ajouteVoisin(&current->aretes[sommetDep-1], sommetArr, poids, NULL);
	    
	    // Cas d'erreur
	    if(err == 0)
	    {
		return ARETE_DEJA_EXISTANTE;
	    }
	    else if (err == -1)
	    {
		return PROBLEME_MEMOIRE;
	    }
	    
	}	
	else if(oriente == 'n')
	{
	    int err1, err2;
	    
	    err1 = ajouteVoisin(&current->aretes[sommetDep-1], sommetArr, poids, NULL);
	    err2 = ajouteVoisin(&current->aretes[sommetArr-1], sommetDep, poids, NULL);
	    
	    // On vérifie si une arête existe déjà
	    if(err1 == 0 || err2 == 0)
	    {
		return ARETE_DEJA_EXISTANTE;
	    }
	    else if(err1 == -1 || err2 == -1)
	    {
		return PROBLEME_MEMOIRE;
	    }
		
	}
	else
	{
	    return COMMANDE_INVALIDE;
	}
    }
    else
    {
	return SOMMET_INEXISTANT;
    }
    
    return RES_OK;
}


