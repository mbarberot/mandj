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
	else
	{
	    printf("Graphe %d inexistant \n", idGraphe);
	}
    }
}

/**
 * Calcule le degré d'un sommet, c'est à dire, le nombre d'arêtes reliées à ce sommet
 * @param idGraphe : le graphe dans lequel on travaille
 * @param sommet : le sommet dont on veut connaître le degré
 * @param degre : le degré calculé
 * @return RES_OK : l'opération s'est déroulée normalement
 * @return NUMERO_GRAPHE_TROP_PETIT : idGraphe <= 0
 * @return NUMERO_GRAPHE_INVALIDE : idGraphe > 2
 * @return GRAPHE_INEXISTANT : le graphe n'est pas initialisé
 * @return SOMMET_INVALIDE : le sommet invalide est hors bornes
 * @return SOMMET_INEXISTANT : le sommet n'est pas initialisé
 */
erreur calculerDegreSommet(int idGraphe, int sommet, int *degre)
{
    int res = 0;
    int i;
    
    // Vérifications sur le graphe
    if(idGraphe < 1)
	return NUMERO_GRAPHE_TROP_PETIT;
    
    if(idGraphe > 2)
	return NUMERO_GRAPHE_INVALIDE;    
    
    TypGraphe* current = graphes[idGraphe -1 ];
    
    if(current == NULL)
	return GRAPHE_INEXISTANT;
    
    // Les sommets sont bien compris dans les bornes ?
    if(sommet < 1 || sommet > current->nbMaxSommets)
	return SOMMET_INVALIDE;
    
    // Le sommet est initialisé ? (=> il existe ?)
    if(current->aretes[sommet-1] == NULL)
	return SOMMET_INEXISTANT;
	
    // On parcourt toutes les arêtes.
    for(i = 0 ; i < current -> nbMaxSommets ; i++)
    {
	if(current->aretes[i] != NULL)
	{
	    if( i == sommet - 1)
	    {
		res = res + (tailleListe(&current->aretes[i]) - 1); // -1 : on ne compte pas le sommet fictif
	    }
	    else
	    {
		// On incrémente le compteur si l'arête est orientée (si non-orientée => on la compte déjà dans le if précédent)
		if(isNonOrientee(idGraphe, i+1, sommet) == TEST_KO)
		{		   
		    res++;
		}
	    }
	    
	}	
    }
    
    *degre = res;
    return RES_OK;
}

/**
 * Vérifie si l'arête précisée par s1 et s2 est une arête non-orientée du graphe idGraphe
 * @param idGraphe : le graphe dans lequel on travaille
 * @param s1 : une extrémité de l'arête
 * @param s2 : une autre extrémité de l'arête
 * @return GRAPHE_INEXISTANT : le graphe n'existe pas en mémoire
 * @return SOMMET_INVALIDE : s1 ou s2 hors bornes
 * @return SOMMET_INEXISTANT : s1 ou s2 non initialisés
 * @return TEST_OK : l'arête s1 <-> s2 est non orientée (les deux arcs existent et ont la même pondération)
 * @return TEST_KO : l'arête s1 -> s2 est orientée
 * @return ARETE_INEXISTANTE : pas d'arête entre s1 et s2 ou s2 -> s1 existe
 */
erreur isNonOrientee(int idGraphe, int s1, int s2)
{
    
    // Vérifications sur le graphe
    if(idGraphe < 1)
	return NUMERO_GRAPHE_TROP_PETIT;
    
    if(idGraphe > 2)
	return NUMERO_GRAPHE_INVALIDE;    
    
    TypGraphe* current = graphes[idGraphe -1 ];
    
    if(current == NULL)
	return GRAPHE_INEXISTANT;
    
    // Les sommets sont bien compris dans les bornes ?
    if(s1 < 1 || s1 > current->nbMaxSommets || s2 < 1 || s2 > current->nbMaxSommets)
	return SOMMET_INVALIDE;
    
    // Les sommets sont bien initialisés ?
    if(current->aretes[s1 - 1] != NULL && current->aretes[s2 -1] != NULL)
    {
	TypVoisins* arr1 = voisinExiste(&current->aretes[s1 -1], s2);
	TypVoisins* arr2 = voisinExiste(&current->aretes[s2 -1], s1);
	
	if(arr1 == NULL && arr2 == NULL)
	    return ARETE_INEXISTANTE;
	
	if((arr1 != NULL && arr2 != NULL) 
	    && (arr1->poidsVoisin == arr2->poidsVoisin))
	{	    
	    return TEST_OK;
	}
	else
	{
	    if(arr1 == NULL)
		return ARETE_INEXISTANTE;
	    
	    return TEST_KO;
	}
    }
    else
    {
	return SOMMET_INEXISTANT;
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
    
    TypGraphe *current = graphes[grapheCourant];
    TypGraphe *new;
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
	int err1, err2;
	err1 = suppressionGraphe(1);
	err2 = suppressionGraphe(2);
	
	if(err1 != RES_OK)
	    return err1;
	if(err2 != RES_OK)
	    return err2;
	
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
	
	graphes[idGraphe -1] = NULL;
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
    TypGraphe *current = graphes[grapheCourant];    
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
    TypGraphe *current = graphes[grapheCourant];
    
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
	    
	    // On vérifie si l'arête n'existe pas déjà
	    if(voisinExiste(&current->aretes[sommetDep-1], sommetArr) == NULL
		&& voisinExiste(&current->aretes[sommetArr-1], sommetDep) == NULL
	    )
	    {		
		int err1 = 0, err2 = 0;
		err1 = ajouteVoisin(&current->aretes[sommetDep-1], sommetArr, poids, NULL);
		// On vérifie que le sommet de départ et d'arrivée sont différents pour éviter de rajouter deux fois la même arête
		// (et provoquer une erreur)
		if(sommetDep != sommetArr)
		    err2 = ajouteVoisin(&current->aretes[sommetArr-1], sommetDep, poids, NULL);
		
		// Pas de problèmes mémoire ?
		if(err1 == -1 || err2 == -1)
		    return PROBLEME_MEMOIRE;
	    }
	    else
	    {
		return ARETE_DEJA_EXISTANTE;
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
/**
 * Modifie le poids de l'arête précisée par sommetDep, sommetArr et oriente
 * Si oriente vaut 'o' => on modifie l'arête sommetDep -> sommetArr
 * Si oriente vaut 'n' => on modifie aussi l'arête sommetArr -> sommetDep
 * @param sommetDep : le sommet de départ (une extrémité dans le cas d'une arête non-orientée) de l'arête à modifier
 * @param nvPoids : le nouveau poids de l'arête
 * @param sommetArr : le sommet d'arrivée (une extrémité dans le cas d'une arête non-orientée) de l'arête à modifier
 * @param oriente : 'o' si on modifie une arête orientée, 'n' sinon
 * @return RES_OK : l'opération s'est déroulée correctement
 * @return GRAPHE_INEXISTANT : le graphe courant n'est pas initialisé
 * @return SOMMET_INVALIDE : sommetDep ou sommetArr ne sont pas compris dans le graphe
 * @return SOMMET_INEXISTANT : sommetDep ou sommetArr ne sont pas initialisés
 * @return ARETE_INEXISTANTE : l'arête (orientée ou non) entre sommetDep et sommetArr n'existe pas
 * @return POIDS_INVALIDE : nvPoids est incorrect (< 0)
 * @return COMMANDE_INVALIDE : oriente est différent de 'o' ou 'n'
 */
erreur modifierPoids(int sommetDep, int nvPoids,int sommetArr,char oriente)
{
    TypGraphe *current = graphes[grapheCourant];
    
    // Le graphe existe ?
    if(current == NULL)
	return GRAPHE_INEXISTANT;
    
    // Le poids est valide ?
    if(nvPoids < 0)
	return POIDS_INVALIDE;
    
    // Les sommets précisés sont bien dans les bornes ?
    if(sommetDep <= 0 || sommetDep > current->nbMaxSommets || sommetArr <= 0 || sommetArr > current->nbMaxSommets)
	return SOMMET_INVALIDE;
    
    // Les sommets sont-ils initialisés ?
    if(current->aretes[sommetDep - 1] != NULL && current->aretes[sommetArr - 1] != NULL)
    {
	// Action à effectuer en fonction de oriente
	if(oriente == 'o')
	{
	    int err = modifiePoidsVoisin(&current->aretes[sommetDep -1], sommetArr, nvPoids);
	    if(err == -1)
		return ARETE_INEXISTANTE;
	}
	else if(oriente == 'n')
	{
	    // On vérifie que l'arête existe avant de faire toute modification
	    if(voisinExiste(&current->aretes[sommetDep - 1] , sommetArr) != NULL
		&& voisinExiste(&current->aretes[sommetArr - 1], sommetDep) != NULL)	    
	    {
		modifiePoidsVoisin(&current->aretes[sommetDep-1], sommetArr, nvPoids);
		
		// Arête non-orientée + sommetDep == sommetArr => meme traitement qu'une arete orientée
		if(sommetDep != sommetArr)
		    modifiePoidsVoisin(&current->aretes[sommetArr-1], sommetDep, nvPoids);
	    }
	    else
	    {
		return ARETE_INEXISTANTE;
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
/**
 * Supprime l'arête entre sommetDep et sommetArr
 * @param sommetDep : le sommet de départ / une extrémité de l'arête (arête non-orientée)
 * @param sommetArr : le sommet d'arrivée / une extrémité de l'arête (arête non-orientée)
 * @param oriente : spécifie si l'arête à supprimer est orientée ou pas
 * @return RES_OK : l'opération s'est déroulée correctement
 * @return GRAPHE_INEXISTANT : le graphe courant n'est pas initialisé
 * @return SOMMET_INVALIDE : sommetDep ou sommetArr ne sont pas compris dans le graphe
 * @return SOMMET_INEXISTANT : sommetDep ou sommetArr ne sont pas initialisés
 * @return ARETE_INEXISTANTE : l'arête (orientée ou non) entre sommetDep et sommetArr n'existe pas
 * @return COMMANDE_INVALIDE : oriente est différent de 'o' ou 'n'
 */
erreur suppressionArete(int sommetDep,int sommetArr,char oriente)
{
    TypGraphe *current = graphes[grapheCourant];
    
    // Le graphe existe ?
    if(current == NULL)
	return GRAPHE_INEXISTANT;

    // Les sommets précisés sont bien dans les bornes ?
    if(sommetDep <= 0 || sommetDep > current->nbMaxSommets || sommetArr <= 0 || sommetArr > current->nbMaxSommets)
	return SOMMET_INVALIDE;
    
    // Les sommets sont ils bien initialisés ?
    if(current->aretes[sommetDep - 1] != NULL && current->aretes[sommetArr - 1] != NULL)
    {
	if(oriente == 'o')
	{
	    int err;
	    err = supprimeVoisin(&current->aretes[sommetDep - 1], sommetArr);
	    
	    if(err != 1)
		return ARETE_INEXISTANTE;
	}
	else if(oriente == 'n')
	{
	    // On vérifie que l'arête existe avant de faire toute modification
	    if(voisinExiste(&current->aretes[sommetDep - 1] , sommetArr) != NULL
		&& voisinExiste(&current->aretes[sommetArr - 1], sommetDep) != NULL)	    
	    {
		supprimeVoisin(&current->aretes[sommetDep-1], sommetArr);
		
		// Arête non-orientée + sommetDep == sommetArr => meme traitement qu'une arete orientée
		if(sommetDep != sommetArr)
		    supprimeVoisin(&current->aretes[sommetArr-1], sommetDep);
	    }
	    else
	    {
		return ARETE_INEXISTANTE;
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


/**
 * Supprime tous les sommets et toutes les arêtes du graphe
 * @return RES_OK : l'opération s'est correctement déroulée
 * @return GRAPHE_INEXISTANT : le graphe courant n'existe pas en mémoire
 */
erreur viderGraphe()
{
    TypGraphe *current;
    int i;
    
    current = graphes[grapheCourant];
    
    if(current == NULL)
	return GRAPHE_INEXISTANT;
    
    for(i = 0 ; i < current -> nbMaxSommets ; i++)
    {
	if(current -> aretes[i] != NULL)
	{
	    supprimeListe(&current->aretes[i]);
	    current->aretes[i] = NULL;
	}
    }
    
    return RES_OK;
}

/**
 * Supprime toutes les arêtes du graphe.
 * Cette fonction laisse les sommets initialisés (dans l'état d'un sommet isolé, avec seulement le voisin fictif -1)
 * @return RES_OK : l'opération s'est correctement déroulée
 * @return GRAPHE_INEXISTANT : le graphe courant n'existe pas en mémoire
 */
erreur viderAreteGraphe()
{
    TypGraphe *current = graphes[grapheCourant];
    int i;
    TypVoisins* next;
    TypVoisins* tmp;
    
    if(current == NULL)
	return GRAPHE_INEXISTANT;
    
    for(i = 0 ; i < current->nbMaxSommets ; i++)
    {
	
	if(current -> aretes[i] != NULL)
	{
	    tmp = current->aretes[i];
	    
	    while(tmp != NULL)
	    {
		next = tmp->voisinSuivant;
		
		if(tmp->voisin != -1)
		{
		    free(tmp);
		}
		else
		{
		    // Si on tombe sur le voisin factice : on change le pointeur de début de liste
		    current->aretes[i] = tmp;
		}
		tmp = next;
	    }

	}
	
    }
    
    return RES_OK;   
}

/**
 * Teste l'existence de l'arête sommetDep <-> sommetArr (orientée ou non) avec le poids correspondant
 * Mode orienté : l'arc sommetDep -> sommetArr existe et son poids est le même que précisé à l'appel de la fonction
 * Mode non-orienté : les deux arcs (sommetDep -> sommetArr et sommetArr -> sommetDep) existe avec le poids précisé
 * 
 * @param idGraphe : le graphe à tester (0 pour les deux graphes)
 * @param sommetDep : le sommet de départ / l'extrémité de l'arête à tester
 * @param poids : le poids supposé de l'arête / de l'un des deux arcs
 * @param sommetArr : le sommet d'arrivée / l'extrémité de l'arête à tester
 * @param oriente : 'o' pour un test en mode orienté, 'n' sinon
 * @param resAttendu : le resultat attendu du test. S'il est en adéquation avec le resultat réel, renvoyer TEST_OK, TEST_KO sinon
 * @return TEST_OK : le résultat attendu est égal au résultat obtenu
 * @return TEST_KO : le résultat attendu est différent du résultat obtenu
 * @return GRAPHE_INEXISTANT : le graphe demandé n'existe pas en mémoire
 * @return SOMMET_INVALIDE : sommetDep ou sommetArr ne sont pas compris dans le graphe
 * @return SOMMET_INEXISTANT : sommetDep ou sommetArr ne sont pas initialisés
 * @return ARETE_DIFFERENTE : l'arête existe, mais a un poids différent
 * @return COMMANDE_INVALIDE : oriente est différent de 'o' ou 'n'
 */
erreur testerArete(int idGraphe, int sommetDep,	int poids, 
		   int sommetArr, char oriente, int resAttendu)
{
    
    if(idGraphe == 0)
    {
	erreur gr1 = testerArete(1, sommetDep, poids, sommetArr, oriente, resAttendu);
	
	// On vérifie si on a un résultat valide pour le graphe 1. Si c'est le cas, on teste maintenant le graphe 2
	if(gr1 == TEST_OK || gr1 == TEST_KO)
	{
	    erreur gr2 = testerArete(2, sommetDep, poids, sommetArr, oriente, resAttendu);
	    
	    // On vérifie si on a un résultat valide pour le graphe 2. On peut alors faire "le bilan" des deux tests
	    if(gr2 == TEST_OK || gr2 == TEST_KO)
	    {
		if(gr1 == TEST_OK && gr2 == TEST_OK) // Si les deux tests sont OK => on renvoie OK
		{
		    return TEST_OK;
		}
		else // Si au moins un des deux tests est KO => on renvoie KO
		{
		    return TEST_KO;
		}
	    }
	    else // On renvoie l'erreur sur le second graphe
	    {
		return gr2;
	    }
	}
	else // Sinon, on renvoie l'erreur rencontrée sur le premier graphe
	{
	    return gr1;
	}
    }
    
    TypGraphe *current = graphes[idGraphe - 1];
    
    // On vérifie l'existence du graphe
    if(current== NULL)
	return GRAPHE_INEXISTANT;
    
    // Les sommets précisés sont bien dans les bornes ?
    if(sommetDep <= 0 || sommetDep > current->nbMaxSommets || sommetArr <= 0 || sommetArr > current->nbMaxSommets)
	return SOMMET_INVALIDE;
    
    // Les sommets précisés sont bien initialisés ?
    if(current->aretes[sommetDep - 1] != NULL && current->aretes[sommetArr - 1] != NULL)
    {
	if(oriente == 'o')
	{
	    TypVoisins *arete = voisinExiste(&current->aretes[sommetDep - 1], sommetArr);	    
	    // Si l'arête n'existe pas ... 
	    if(arete == NULL)
	    {
		if(resAttendu == 0) // ... et que le résultat attendu était faux : le test est OK
		{
		    return TEST_OK;
		}
		else // Si le résultat attendu était à vrai : le test est KO
		{
		    return TEST_KO;
		}
	    }
	    else // L'arête existe 
	    {
		// Mais le poids est différent que celui attendu
		if(arete->poidsVoisin != poids)
		{
		    return ARETE_DIFFERENTE;
		}
		else // On renvoie TEST_OK ou TEST_KO en fonction du résultat attendu
		{
		    if(resAttendu == 0)
		    {
			return TEST_KO;
		    }
		    else
		    {
			return TEST_OK;
		    }
		}
	    }
	    
	}
	else if(oriente == 'n') // Test d'existence de l'arête non-orientée
	{	 	    
	    TypVoisins *arc1 = voisinExiste(&current->aretes[sommetDep - 1], sommetArr);
	    TypVoisins *arc2 = voisinExiste(&current->aretes[sommetArr - 1], sommetDep);
	    
	    // Si les deux arcs existent => l'arete existe
	    if(arc1 != NULL || arc2 != NULL)
	    {
		if(arc1->poidsVoisin == poids && arc2->poidsVoisin == poids)
		{
		    if(resAttendu == 1)
		    {
			return TEST_OK;
		    }
		    else
		    {
			return TEST_KO;
		    }
		}
		else
		{
		    return ARETE_DIFFERENTE;
		}
	    }
	    else
	    {
		if(resAttendu == 0)
		{
		    return TEST_OK;
		}
		else
		{
		    return TEST_KO;
		}
	    }
	}
	else // Si oriente n'est ni 'o' ni 'n' => commande invalide
	{
	    return COMMANDE_INVALIDE;
	}
    }
    else
    {
	return SOMMET_INEXISTANT;
    }    
}

/**
 * Teste l'existence d'un sommet dans le graphe désigné par idGraphe (voire dans les deux, si idGraphe = 0)
 * @param idGraphe : le graphe dans lequel on cherche le sommet (0 pour les deux graphes)
 * @param sommet : le sommet dont on veut vérifier l'existence
 * @param resAttendu : 1 si le sommet est censé exister, 0 sinon
 * @return TEST_OK : le résultat obtenu est le même que le résultat attendu
 * @return TEST_KO : le résultat obtenu n'est pas le même que le résultat attendu
 * @return GRAPHE_INEXISTANT : le(s) graphe(s) n'existe(nt) pas en mémoire
 * @return SOMMET_INVALIDE : le sommet n'est pas compris dans les bornes
 */
erreur testerSommet(int idGraphe, int sommet, int resAttendu)
{
    
    // On vérifie si idGraphe est bien dans les bornes
    if(idGraphe < 0 || idGraphe > 2)
	return GRAPHE_INEXISTANT;
    
    // On teste les deux graphes
    if(idGraphe == 0)
    {
	erreur gr1 = testerSommet(1, sommet, resAttendu);
	
	// Si le premier test nous renvoie un résultat correct
	if(gr1 == TEST_OK || gr1 == TEST_KO)
	{
	    // On teste le deuxième graphe
	    erreur gr2 = testerSommet(2, sommet, resAttendu);
	    
	    // Le second test nous renvoie un résultat correct
	    if(gr2 == TEST_OK || gr2 == TEST_KO)
	    {
		// Si les deux sont ok => on renvoie ok. Si l'un des deux est ko => on renvoie ko
		if(gr1 == TEST_OK && gr2 == TEST_OK)
		{
		    return TEST_OK;
		}
		else
		{
		    return TEST_KO;
		}
	    }
	    else // Sinon, on renvoie l'erreur trouvée sur le graphe 2
	    {
		return gr2;
	    }
	}
	else // Sinon, on renvoie l'erreur trouvée sur le graphe 1
	{
	    return gr1;
	}
    }
    
    TypGraphe *current = graphes[idGraphe - 1];
    
    // On vérifie l'existence du graphe
    if(current== NULL)
	return GRAPHE_INEXISTANT;
    
    // Les sommets précisés sont bien dans les bornes ?
    if(sommet <= 0 || sommet > current->nbMaxSommets)
	return SOMMET_INVALIDE;
    
    // On teste l'existence du sommet
    if(current->aretes[sommet -1] != NULL)
    {
	if(resAttendu == 1)
	{
	    return TEST_OK;
	}
	else
	{
	    return TEST_KO;
	}
    }
    else
    {
	if(resAttendu == 0)
	{
	    return TEST_OK;
	}
	else
	{
	    return TEST_KO;
	}
    }
}
/**
 *  Teste le degré d'un sommet dans un graphe donné
 *  @param idGraphe : le(s) graphe(s) à tester
 *  @param sommet : le sommet dont on veut tester le degré
 *  @param value : le degré attendu
 *  @param resAttendu : vrai si on s'attend à ce que le degré trouvé soit le même que le degré attendu (value)
 *  @return TEST_OK : le résultat obtenu est conforme à ce qu'on attendait
 *  @return TEST_KO : le résultat obtenu n'est pas conforme à ce qu'on attendait
 *  @return GRAPHE_INEXISTANT : le(s) graphe(s) n'est (ne sont) pas présent(s) en mémoire
 *  @return SOMMET_INVALIDE : le sommet n'est pas compris dans les bornes [1..nbMaxSommets]
 *  @return SOMMET_INEXISTANT : le sommet n'est pas initialisé
 */
erreur testerDegreSommet(int idGraphe, int sommet, int value, int resAttendu )
{
    if(idGraphe == 0)
    {
	erreur gr1 = testerDegreSommet(1, sommet, value, resAttendu);
	
	// Si le premier test nous renvoie un résultat correct
	if(gr1 == TEST_OK || gr1 == TEST_KO)
	{
	    // On teste le deuxième graphe
	    erreur gr2 = testerDegreSommet(2, sommet, value, resAttendu);
	    
	    // Le second test nous renvoie un résultat correct
	    if(gr2 == TEST_OK || gr2 == TEST_KO)
	    {
		// Si les deux sont ok => on renvoie ok. Si l'un des deux est ko => on renvoie ko
		if(gr1 == TEST_OK && gr2 == TEST_OK)
		{
		    return TEST_OK;
		}
		else
		{
		    return TEST_KO;
		}
	    }
	    else // Sinon, on renvoie l'erreur trouvée sur le graphe 2
	    {
		return gr2;
	    }
	}
	else // Sinon, on renvoie l'erreur trouvée sur le graphe 1
	{
	    return gr1;
	}
    }
    
    // Permettra de stocker le renvoi de calculerDegreSommet
    erreur err;
    
    // Le degré que l'on calcule pour le sommet
    int degreCalc = 0;
    
    // On vérifie si idGraphe est bien dans les bornes
    if(idGraphe < 0 || idGraphe > 2)
	return GRAPHE_INEXISTANT;
    
    TypGraphe *current = graphes[idGraphe - 1];
    
    // On vérifie l'existence du graphe
    if(current== NULL)
	return GRAPHE_INEXISTANT;
    
    // Les sommets précisés sont bien dans les bornes ?
    if(sommet <= 0 || sommet > current->nbMaxSommets)
	return SOMMET_INVALIDE;    
    
    err = calculerDegreSommet(idGraphe, sommet, &degreCalc);
    
    if(err != RES_OK)
	return err;
        
    // On compare le résultat obtenu et le résultat attendu et on renvoie le résultat du test
    if((degreCalc == value && resAttendu == 1) 
	|| (degreCalc != value && resAttendu ==0))
    {
	return TEST_OK;
    }
    else
    {
	return TEST_KO;
    }
    
}

/**
 * Compare un sommet sur les deux graphes.
 * Ils sont égaux si:
 * 	- ils existent dans les deux graphes
 * 	- ils ont les mêmes voisins
 * @param sommet : le sommet à tester dans les deux graphes
 * @param resAttendu : le résultat attendu du test
 * @return TEST_OK : résultat obtenu = résultat attendu
 * @return TEST_KO : résultat obtenu != résultat attendu
 * @return GRAPHE_INEXISTANT : un des deux (ou les deux) graphes n'existe pas
 * @return SOMMET_INVALIDE : sommet hors borne
 * @return SOMMET_INEXISTANT : sommet non initialisé dans un des deux graphes (voire les deux)
 */
erreur compareSommet(int sommet, int resAttendu)
{
    int res;
    TypGraphe *gr1 = graphes[0];
    TypGraphe *gr2 = graphes[1];
    
    // Les deux graphes sont bien initialisés ?
    if(gr1 == NULL || gr2 == NULL)
	return GRAPHE_INEXISTANT;
    
    // Les deux sommets sont biens dans les bornes ?
    if(sommet < 0 || sommet > gr1->nbMaxSommets || sommet > gr2->nbMaxSommets)
	return SOMMET_INVALIDE;
    
    TypVoisins *voisinsS1 = gr1->aretes[sommet-1];
    TypVoisins *voisinsS2 = gr2->aretes[sommet-1];
    
    // Les deux sommets sont bien initialisés ?
    if(voisinsS1 == NULL || voisinsS2 == NULL)
	return SOMMET_INEXISTANT;
    
    // On compare les deux listes de sommets
    res = compareListes(&voisinsS1, &voisinsS2);
    
    // On traite le résultat à renvoyer en fonction de ce qui était attendu
    if(res == resAttendu)
    {
	return TEST_OK;
    }
    else
    {
	return TEST_KO;
    }
}
/**
 * Compare les deux graphes.
 * Ils sont égaux si tous leurs sommets sont égaux
 * @param resAttendu : le résultat attendu de la comparaison
 * @return TEST_OK : résultat obtenu = résultat attendu
 * @return TEST_KO : résultat obtenu != résultat attendu
 * @return GRAPHE_INEXISTANT : un des deux (ou les deux) graphes n'existe pas
 */
erreur compareGraphe(int resAttendu)
{
    erreur err;
    int i;
    
    TypGraphe *gr1 = graphes[0];
    TypGraphe *gr2 = graphes[1];
    
    //Test de l'existence des deux graphes
    if(gr1 == NULL || gr2 == NULL)
	return GRAPHE_INEXISTANT;
    
    // Avant d'aller plus loin : on vérifie que la taille des deux graphes est la même
    if(gr1->nbMaxSommets != gr2->nbMaxSommets)
	return TEST_KO;
    
    //On parcourt tous les sommets pour vérifier s'ils sont égaux dans les deux graphes
    for(i = 0 ; i < gr1->nbMaxSommets ; i++)
    {
	err = compareSommet(i+1, 1);	
	
	if(err != TEST_OK)
	{
	    if(err == TEST_KO) // si compareSommet a renvoyé un résultat exploitable
	    {
		if(resAttendu ==0)
		{
		    return TEST_OK;
		}
		else
		{
		    return TEST_KO;
		}
	    }	 
	}
    }
    
    // Arrivé à ce point du programme : err = TEST_OK
    
    if(resAttendu == 1)
    {
	return TEST_OK;
    }
    else
    {
	return TEST_KO;
    }    
    
}



/**
 * Exporte le graphe courant dans un fichier .dot
 *
 * @param idCommande - indice de la commande
 */
void graphe2dot(int idCommande) 
{
	// Le graphe à exporter en dot
	TypGraphe *g = graphes[grapheCourant];

	// Création du nom de fichier
	// - numéro du graphe
	char numGraphe = ((grapheCourant == 1)?'1':'2');
	// - numéro de la commande
	char c,d,u;
	int tmp = idCommande;
	c = (tmp / 100) + 48;
	tmp = tmp % 100;
	d = (tmp / 10) + 48;
	tmp = tmp % 10;
	u = tmp + 48;
	// - nom du fichier
	// Format du nom de fichier :
	// exemple_GX_YYY.dot
	// X = numéro du graphe (1 ou 2)
	// YYY = numéro de la commande (exemple : 001)
	char filename[18] = {
		'e','x','e','m','p','l','e',
		'_','G',numGraphe,'_',c,d,u,
		'.','d','o','t'
	};
	
	// Ouverture du fichier
	FILE *fp;	
	// Essai d'ouverture du fichier, en écriture
	// Grâce à l'argument "w" (write) :
	// Si le fichier existe déjà, son contenu sera écrasé
	// Si le fichier n'existe pas, il sera créé
	fp = fopen(filename,"w");
	// Cas d'erreur
	if(fp == NULL)
	{
		printf("Impossible d'effectuer l'exportation\n");
	}
	else
	{
		if(g == NULL)
		{
			printf("Impossible d'afficher le graphe");
		}
		else
		{
			// Début
			fprintf(fp,"digraph G {\n\tedge [ arrowtail=dot, arrowhead=open ];\n");

			// Les sommets
			int nbSom = g->nbMaxSommets;
			int i;	
			for(i = 0; i < nbSom; i++) 
			{
				if(g->aretes[i] != NULL)
				{
					fprintf(fp,"\tA%d\n",i);
				}
			}
			
			// Les arètes
			// Pour chaque liste du tableau
			//	Pour chaque element de la liste
			//		Ecrire AX -> AY [label="S"]
			//			avec X : l'indice du tableau
			//			     Y : la valeur de l'élément
			//			     S : le label de l'élément
			TypVoisins *l;
			for(i = 0; i < nbSom; i++)
			{
				l = g->aretes[i];
				while(l != NULL)
				{
					if(l->voisin >= 0)
					{
						fprintf(fp,"\tA%d -> A%d [label=\"%d\"]\n",i,l->voisin,l->poidsVoisin);
					}
					l = l->voisinSuivant;
				}
			}

			// Fin
			fprintf(fp,"}\n");
		}
	}
	fclose(fp);
}
