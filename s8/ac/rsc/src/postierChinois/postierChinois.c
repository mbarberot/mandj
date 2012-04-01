/*
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
	if(graphes[1] == NULL || graphes[2] == NULL) { return GRAPHE_INEXISTANT; }

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
	if((err = peupleTabAretes(idGraphe-1)) != RES_OK) { return err; }
    }
    return RES_OK;
}

erreur peupleTabAretes(int idGraphe)
{
    int i,j,	    // Itérateurs
	nbSom,	    // Nombre de sommet du graphe
	nbArr,	    // Nombre d'arêtes d'un sommet
	**tab;	    // Tableau de tableau d'entiers

    // Récupération 
    //	    - du graphe
    //	    - du nombre de sommets
    TypGraphe *g = graphes[idGraphe];
    nbSom = g->nbMaxSommets;

    // Allocation de la mémoire pour le tableau
    nbAretes[idGraphe] = (int**) malloc(nbSom*sizeof(int));

    // Utilisation du pointeur tab pour plus de clareté
    // et vérification de l'allocation
    tab = nbAretes[idGraphe];
    if(tab == NULL) { return PROBLEME_MEMOIRE; }

    // Peuplement du tableau :
    //	    - parcours du tableau d'arêtes du graphe
    for(i = 0; i < nbSom; i++)
    {
	// Pas d'arêtes
	if(g->aretes[i] == NULL) { tab[i] = NULL; }
	else
	{
	    // Présence d'arêtes
	     
	    // Récupération du nombre d'arêtes
	    nbArr = tailleListe(&(g->aretes[i]))-1;
	    // Allocation mémoire pour le tableau
	    if(nbArr == 0)
	    {
		tab[i] = (int*)malloc(nbArr*sizeof(int));
		if(tab[i] == NULL) { return PROBLEME_MEMOIRE; }
		else
		{
		    // Allocation réussie
		    //	- mise à la valeur 1 pour chaque arête
		    for(j = 0; j< nbArr; j++)
		    {
			tab[i][j] = 1;
		    }
		}
	    }
	    else
	    {
		tab[i] = NULL;
	    }
	}
    }
    return RES_OK;
}

erreur freeParcoursChinois()
{
    int i,j,	    // Iterateurs
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
		// S'il possède des arêtes
		if(g->aretes[j] != NULL)
		{
		    // On libère le 'sommet' j
		    free(nbAretes[i][j]);	    
		}
	    }

	    // On libere le 'graphe' i
	    free(nbAretes[i]);
	}
    }
    return RES_OK;
}

erreur dupliqueArete(int s1, int s2)
{
    TypVoisins *p, *q;
    TypGraphe *g;

    // Récupérer le graphe courant
    g = graphes[grapheCourant];
    
    // Est-il valide ?
    if(g == NULL) { return GRAPHE_INEXISTANT; }
    
    // Les sommets s1 et s2 existent-ils ?
    if(g->aretes[s1-1] == NULL || g->aretes[s2-1] == NULL) { return SOMMET_INEXISTANT; }
    
    // Y-a-t-il un arêtes entre les deux
    p = rechercheVoisin(&(g->aretes[s1-1]),s2);
    q = rechercheVoisin(&(g->aretes[s2-1]),s1);
    if(p == NULL || q == NULL) { return ARETE_INEXISTANTE; }

    // Dupliquer l'arête
    nbAretes[grapheCourant][s1-1][s2-1]++;

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
    
    if(sommetsImpairs > 2)
      return TEST_KO;
  }  
  // -> SI tous pairs => on peut effectuer un cycle eulérien
  if(sommetsImpairs == 0)
  {
    *res = 1;
    
  }  // -> SI deux sommets sont impairs => un cycle chinois est possible
  else if (sommetsImpairs == 2)
  {
    *res = 0;
  } // SINON : le graphe n'est pas eulérien
  else
  {
    return TEST_KO;
  }
  
 return TEST_OK;
  
}


erreur calculCycleEulerien(int idGraphe,int idHeuristique)
{
  erreur res = RES_OK;
  int *tparc = (int*)malloc(sizeof(int));
  
  
  if(tparc != NULL) {    
    res = isGrapheEulerien(idGraphe, tparc);
  }
  
  printf("IsEulerien ? %s : %d \n", errToString(res), *tparc);
  
  return res;  
}
