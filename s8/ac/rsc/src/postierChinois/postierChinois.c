/**
 * Algorithmique Combinatoire
 * Mars - Avril - Mai 2012
 *
 * Barberot Mathieu & Joan Racenet
 *
 * Implémentation du fichier d'entete
 *
 */

#include <stdio.h>
#include <stdlib.h>

#include "postierChinois.h"


erreur initParcoursChinois(int idGraphe)
{
    erreur err;
    
    // idGraphe valide ?
    if(idGraphe < 0 || idGraphe > 2) { return NUMERO_GRAPHE_INVALIDE; }
    
    // Quels graphes ?
    if(idGraphe == 0)
    {
	// Les deux graphes
	
	// Existent-ils ?
	if(graphes[0] == NULL || graphes[1] == NULL) { return GRAPHE_INEXISTANT; }
	
	// Peuplement de nbAretes
	if((err = peupleTabAretes(1)) != RES_OK) { return err; }
	if((err = peupleTabAretes(2)) != RES_OK) { return err; }
    }
    else
    {
	// Un seul
	
	// Existe-t-il ?
	if(graphes[idGraphe-1] == NULL) { return GRAPHE_INEXISTANT; }
	
	// Peuplement de nbAretes
	if((err = peupleTabAretes(idGraphe)) != RES_OK) { return err; }
    }
    return RES_OK;
}

erreur peupleTabAretes(int idGraphe)
{
    int i,j,k,	    // Itérateurs
    nbSom,	    // Nombre de sommet du graphe
    **tab;
    
    TypVoisins*** pond;

    // Récupération 
    //	    - du graphe
    //	    - du nombre de sommets
    TypGraphe *g = graphes[idGraphe - 1];
    nbSom = g->nbMaxSommets;
    
    // Allocation de la mémoire pour les tableaux
    nbAretes[idGraphe - 1] = (int**) malloc(nbSom*sizeof(int));
    
    // Utilisation du pointeur tab pour plus de clareté
    // et vérification de l'allocation
    tab = nbAretes[idGraphe - 1];
    if(tab == NULL) { return PROBLEME_MEMOIRE; }

    
    // Peuplement du tableau :
    //	    - parcours du tableau d'arêtes du graphe
    for(i = 0; i < nbSom; i++)
    {
	// Initialisation de la matrice d'adjacence
	tab[i] = (int*)malloc(nbSom*sizeof(int));
	
	if(tab[i] == NULL)
	    return PROBLEME_MEMOIRE;

	
	// Initialisation des valeurs : 1 si une arête existe entre i et j, 0 sinon
	    for(j = 0 ; j < nbSom ; j++)
	    {		
		tab[i][j] = compteOccurences(g -> aretes[i], j + 1);

	    }
    }    

    return RES_OK;
}

erreur freeParcoursChinois()
{
    int i,j,k,	    // Iterateurs
    nbSom;	    // Nombre de sommets
    
    TypGraphe *g;   // Les graphes
    
    
    // Pour chaque graphe
    for(i = 0; i < 2; i++)
    {
	// Si le graphe existe
	if(graphes[i] != NULL)
	{
	    // Récupération 
	    //	    - du graphe
	    //	    - du nombre de sommets
	    g = graphes[i];
	    nbSom = g->nbMaxSommets;
	    
	    // Pour chaque sommet
	    for(j = 0; j < nbSom; j++)
	    {		
		for( k = 0 ; k < nbSom ; k++)
		{
		    free(nbAretes[i][j][k]);
		 }
		free(nbAretes[i][j]);

	    }
	    
	    // On libere le 'graphe' i
	    free(nbAretes[i]);
	    
	}
    }
    
    return RES_OK;
}

erreur dupliqueArete(int s1, int s2, int idGraphe)
{
    TypVoisins *p, *q;
    TypGraphe *g;
    
    // Récupérer le graphe courant
    g = graphes[idGraphe - 1];
    
    // Est-il valide ?
    if(g == NULL) { return GRAPHE_INEXISTANT; }
    
    // Les sommets s1 et s2 existent-ils ?
    if(g->aretes[s1-1] == NULL || g->aretes[s2-1] == NULL) { return SOMMET_INEXISTANT; }
    
    // Y-a-t-il un arêtes entre les deux
    p = rechercheVoisin(&(g->aretes[s1-1]),s2);
    q = rechercheVoisin(&(g->aretes[s2-1]),s1);
    if(p == NULL || q == NULL) { return ARETE_INEXISTANTE; }
    
    // Dupliquer l'arête
    nbAretes[idGraphe - 1][s1-1][s2-1]++;
    nbAretes[idGraphe - 1][s2-1][s1-1]++;
    
    return RES_OK;
}

erreur supprimeArete(int idGraphe, int s1, int s2)
{
    TypVoisins *p, *q;
    TypGraphe *g;
    
    
    // Récupérer le graphe courant
    g = graphes[idGraphe - 1];
    
    // Est-il valide ?
    if(g == NULL) { return GRAPHE_INEXISTANT; }
    
    // Les sommets s1 et s2 existent-ils ?
    if(g->aretes[s1-1] == NULL || g->aretes[s2-1] == NULL) { return SOMMET_INEXISTANT; }
    
    // Y-a-t-il un arêtes entre les deux
    p = rechercheVoisin(&(g->aretes[s1-1]),s2);
    q = rechercheVoisin(&(g->aretes[s2-1]),s1);
    
    if(p == NULL || q == NULL) { return ARETE_INEXISTANTE; }

    // Supprimer l'arête
    nbAretes[idGraphe - 1][s1-1][s2-1]--;
    nbAretes[idGraphe - 1][s2-1][s1-1]--;
    
    return RES_OK;
}


erreur isGrapheEulerien(int idGraphe, int* res)
{
    TypGraphe *g = graphes[idGraphe - 1];
    int pariteCurrent = 0;
    int sommetsImpairs = 0;
    int i;
    
    *res = -1;
    
    
    if(g == NULL)
	return GRAPHE_INEXISTANT;
    
    // Tester la parité des degrés de chaque sommet
	for(i = 0 ; i < g -> nbMaxSommets ; i++)
	{
	    calculerDegreSommet(idGraphe, i + 1, &pariteCurrent);
	    
	    // => l'implémentation oblige de figurer les arêtes non-orientées comme deux arêtes orientées
	    // on divise donc le degré par deux pour avoir une valeur correcte
	    pariteCurrent = pariteCurrent / 2;
	    
	    if(pariteCurrent % 2 == 1)
		sommetsImpairs++;
	}  
	// -> SI tous pairs => on peut effectuer un cycle eulérien
	if(sommetsImpairs == 0)
	{
	    *res = 1;
	    
	}  // -> SI on a un nombre pair de sommets impairs
	else if (sommetsImpairs % 2 == 0)
	{
	    *res = 0;
	} // SINON : le graphe n'est pas eulérien
	else
	{
	    return TEST_KO;
	}
	
	return TEST_OK;
	
}

TypVoisins* cycleEulerien(int idGraphe, int x)
{
    
    TypVoisins* res = NULL;
    int current, voisin;
    int nbVoisins;
    
    
    // Vérifier si s_current est isole
    if (getNbVoisinsAccessibles(idGraphe, x) == 0)
    {
	return res;
    }
    else
    {
	
	// Ajout du sommet courant dans la liste res
	ajouteVoisinNonTries(&res, x, 0, NULL);  
	
	current = x;
	while((nbVoisins = getNbVoisinsAccessibles(idGraphe, current)) != 0)
	{
	    voisin = -1;
	    // Choisir z tq z soit un voisin de current
	    // => on parcourt les arêtes, et on prend la première existante
	    
	    int i = 0;
	    while(i < graphes[idGraphe - 1] -> nbMaxSommets && voisin == -1)
	    {
		if(nbAretes[idGraphe - 1][current -1][i] > 0)
		{
		    voisin = i + 1;
		}
		i++;
	    }
	    //On supprime l'arête y <-> z
	    supprimeArete(idGraphe, current, voisin);
	    current = voisin;
	    
	    // Ajout de y dans le résultat
	    ajouteVoisinNonTries(&res, current, 0, NULL);
	    
	}
    }
    
    
    // Appeler cycle eulérien sur tous les sommets de la liste résultat
    
    TypVoisins *tmp = res;
    TypVoisins *nRes = NULL;
    
    while(tmp != NULL)
    {
	TypVoisins *lTmp = NULL;
	lTmp = cycleEulerien(idGraphe, tmp -> voisin);
	
	// Concaténer la liste temporaire avec la liste résultat
	if(tailleListe(&lTmp) > 0) 
	{
	    concateneListe(&nRes, lTmp);
	    supprimeListe(&lTmp);
	}
	else
	{
	    ajouteVoisinNonTries(&nRes, tmp->voisin, tmp->poidsVoisin, tmp->info);
	}
	
	tmp = tmp -> voisinSuivant;
    }
     
     supprimeListe(&res);
     supprimeListe(&tmp);     
     
    // Retourner la concaténation de tous les résultats
    return nRes;
}

erreur calculCycleEulerien(int idGraphe,int idHeuristique)
{
    erreur res = RES_OK;
    int *tparc = (int*)malloc(sizeof(int));
    
    
    if(tparc != NULL) {    
	res = isGrapheEulerien(idGraphe, tparc);
    } 
    
    
    if(*tparc == 1)
    {
	// Cycle eulerien
	res = initParcoursChinois(idGraphe);
	
	if(res == RES_OK)
	{
	    TypVoisins *test = cycleEulerien(idGraphe, 1);
	    printf("CYCLE EULERIEN TROUVE POUR LE GRAPHE : \n\n ");
	    afficheVoisins(&test);
	    printf("\n\n");
	    supprimeListe(&test);
	    freeParcoursChinois();
	}
	
    }
    else if(*tparc == 0)
    {
	// Cycle chinois
	// res = cycleChinois(int idGraphe, int idHeuristique);
    }
    else
    {
	// Erreur
    }
    
    free(tparc);
    
    return res;  
}


int getNbVoisinsAccessibles(int idGraphe, int sommet)
{
    int nbVoisins = 0 ;
    int i, max;
    
    max = graphes[idGraphe - 1]->nbMaxSommets;
    
    for(i = 0; i < max ; i++)
    {
	if(nbAretes[idGraphe - 1][sommet - 1][i] > 0 )
	{
	    nbVoisins ++;
	}
    }
    
    return nbVoisins;
}

int plusCourtChemin(int idGraphe, int m[][], int p[][])
{
    TypGraphe *g = graphes[idGraphe - 1];
    int nbSom = g -> nbMaxSommets;
    
    /* itérateurs */
    int x, y, z;
    
    /* Initialisation */
    for(x = 0 ; x < nbSom ; x++)
    {
	for(y = 0 ; y < nbSom ; y++)
	{
	    TypVoisins *tmp = rechercheVoisin(g -> aretes[x], y + 1);
	    
	    if(x == y)
	    {
		m[x][y] = 0;
	    }
	    else
	    {
		m[x][y] = (tmp == NULL)? -1 : tmp -> poidsVoisin;
	    }
	    
	    p[x][y] = y + 1;
	}
    }
    
    /* Boucle de calcul */
    for(x = 0 ; x < nbSom ; x++)
    {
	for(y = 0; y < nbSom ; y++)
	{
	    for(z = 0 ; z < nbSom; z++)
	    {
		if(m[x][z] != -1 && m[y][z] != -1 && m[x][z] + m[z][y] < m[x][y])
		{
		    m[x][y] = m[x][z]+ m[z][y];
		    p[x][y] = p[z][y];
		}
	    }	    
	}
    }
}


TypVoisins* sommetsImpairs(int idGraphe)
{
    TypGraphe *res = NULL;
    TypGraphe *g = graphes[idGraphe - 1];
    int i;
    int pariteCurrent = 0;
    
    for(i = 0 ; i < g ->nbMaxSommets ; i++)
    {
	calculerDegreSommet(idGraphe, i + 1, &pariteCurrent)
	if((pariteCurrent / 2) % 2 == 1)
	{
	    ajouteVoisin(&res, i + 1, 0, NULL);
	}
    }
    
    return res;
}

void listeCouplage(TypVoisins *ls, TypVoisins *res)
{
    
    TypVoisins *current = ls;
    
    while(current != NULL)
    {
	TypVoisins *poss = current -> voisinSuivant;
	
	while(poss != NULL)
	{
	    ajouteVoisinNonTries(&res, current->voisin, 0, NULL);
	    ajouteVoisinNonTries(&res, poss->voisin, 0, NULL);
	    poss = poss -> voisinSuivant;
	}
	
	current = current -> voisinSuivant;
    }   
    
}'