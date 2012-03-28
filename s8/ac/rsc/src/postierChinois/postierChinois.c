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

    // ID valide ?
    if(idGraphe < 0 || idGraphe > 2) { return NUMERO_GRAPHE_INVALIDE; }

    if(idGraphe == 0)
    {
	if(graphes[1] == NULL || graphes[2] == NULL) { return GRAPHE_INEXISTANT; }
	if(err = peupleTabAretes(1) != RES_OK) { return err; }
	if(err = peupleTabAretes(2) != RES_OK) { return err; }
    }
    else
    {
	if(graphes[idGraphe-1] == NULL) { return GRAPHE_INEXISTANT; }
	if(err = peupleTabAretes(idGraphe-1) != RES_OK) { return err; }
    }
    return RES_OK;
}

erreur peupleTabAretes(int idGraphe)
{
    int i,
	nbSom,
	**tab;

    TypGraphe *g = graphes[idGraphe];


    nbSom = g->nbMaxSommets;

    nbAretes[idGraphe] = (int**) malloc(nbSom*sizeof(int));
    tab = nbAretes[idGraphe];
    if(tab == NULL) { return PROBLEME_MEMOIRE; }

    for(i = 0; i < nbSom; i++)
    {
	if(g->aretes[i] == NULL) { tab[i] = NULL; }
	else
	{
	}
    }



    // Malloc nbSom
    // pour chaque sommet s
    //	    s = NULL => tab[s] = NULL
    //	    s != NULL =>
    //		malloc taille liste
    //		parcours liste & maj compteur
}

erreur freeParcoursChinois()
{
    // pour chaque graphe
    //	pour chaque sommet
    //   pour chaque arete
    //	    free(arete)
    //   fait
    //   free(sommet)
    //  fait
    //  free(graphe)
    // fait
}
