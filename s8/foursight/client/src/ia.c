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


void *ia_calculeCoup(void *arg)
{
    printf("[] - Thread !\n");
    //while(1);
    (void)arg;
    pthread_exit(NULL);
}


/*
// Calcul un coup "débile"
ia_err ia_calculeCoup(Data data)
{
    TypCoupReq *coup = data.coup;

    system("sleep 10");

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

    char *debug; 

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
		    debug = "blanc";
		}
		break;
	    case BLANC :
		if(joueur->rouge > 0)
		{
		    coup->typePiece = ROUGE;
		    found = 1;
		    debug = "rouge";		
		}
		else if(joueur->jaune > 0)
		{
		    coup->typePiece = JAUNE;
		    found = 1;
		    debug = "jaune";
		}
		break;
	    default:
		break;
	}

    }

    if(DEBUG)
    {
	printf("[DEBUG] ia_calculeCoup \n");
	printf("[DEBUG] alea = %d, ligne = %d, colonne = %d, pion = %s\n",alea,alea/4,alea%4,debug);
	printf("-------------------------------------------\n");
    }

    pos->ligne = alea/4;
    pos->colonne = alea%4;

    coup->caseArrivee = *pos;
    coup->propCoup = POSE;

    return IA_OK;
}
*/
