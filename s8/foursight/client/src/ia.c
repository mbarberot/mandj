/*
 * Projet MOIA -SCS
 * 2011 - 2012
 *
 * Fichier de gestion de l'IA
 *
 * Barberot Mathieu &
 * Racenet Joan
 *
 */

#define DEBUG 1

#include "ia.h"


// Calcul un coup "débile"
ia_err ia_calculeCoup(TypCoupReq *coup)
{
    if(coup == NULL)
    {
	if(DEBUG)
	{
	    printf("[DEBUG] ia_calculeCoup\n");
	    printf("[DEBUG] Argument coup == NULL\n");
	    printf("-----------------------------\n");
	}
	return IA_ERR;
    }

    int i,
	alea,
	found,
	tirage[15];

    TypPosition *pos = (TypPosition*) malloc(sizeof(TypPosition));
    if(pos == NULL)
    {	
	if(DEBUG)
	{
	    printf("[DEBUG] ia_calculeCoup\n");
	    printf("[DEBUG] Impossible d'allouer la mémoire\n");
	    printf("---------------------------------------\n");
	}
	return IA_ERR;
    }

    for(i = 0; i < 15; i++) { tirage[i] = 0; }

    found = 0;

    while(!found) 
    {
	do {
	    alea = rand()%15;
	} while(tirage[alea] > 0);
	
	tirage[alea]++;

	switch(plateau[alea])
	{
	    case VIDE :
		if(joueur->blanc > 0) 
		{ 
		    coup->typePiece = BLANC; 
		    found = 1;
		}
		break;
	    case BLANC :
		if(joueur->rouge > 0)
		{
		    coup->typePiece = ROUGE;
		    found = 1;
		}
		else if(joueur->jaune > 0)
		{
		    coup->typePiece = JAUNE;
		    found = 1;
		}
		break;
	    default:
		break;
	}

    }

    if(DEBUG)
    {
	printf("[DEBUG] ia_calculeCoup \n");
	printf("[DEBUG] alea = %d, ligne = %d, colonne = %d\n",alea,alea/4,alea%4);
	printf("-------------------------------------------\n");
    }

    pos->ligne = alea/4;
    pos->colonne = alea%4;

    coup->caseArrivee = *pos;
    coup->propCoup = POSE;

    return IA_OK;
}
