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
    int i,
	alea,
	found,
	tirage[15];

    char *debug; 

    TypCoupReq *coup;
    TypPosition *pos;

    Shared_vars *data;

    data = (Shared_vars*)arg;
    coup = data->coup;

    if(coup == NULL)
    {
	if(DEBUG)
	{
	    printf("[DEBUG] ia_calculeCoup\n");
	    printf("[DEBUG] Argument coup == NULL\n");
	    printf("-----------------------------\n");
	}
	pthread_exit(NULL);
    }

    pos = (TypPosition*) malloc(sizeof(TypPosition));
    if(pos == NULL)
    {	
	if(DEBUG)
	{
	    printf("[DEBUG] ia_calculeCoup\n");
	    printf("[DEBUG] Impossible d'allouer la mémoire\n");
	    printf("---------------------------------------\n");
	}
	pthread_exit(NULL);
    }


    for(i = 0; i < 15; i++) { tirage[i] = 0; }

    found = 0;

    while(!found) 
    {
	do {
	    alea = rand()%16;	   
	} while(tirage[alea] > 0);	
	
	system("sleep 1");
	
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
		else if(
		    ((joueur->rouge == 0 && joueur->jaune > 0) || (joueur->rouge > 0 && joueur->jaune == 0))
		    && adversaire->rouge == 0 && adversaire->jaune == 0)
		{
		    coup->typePiece = VIDE;
		    coup->propCoup = PASSE;
		    found = 1;
		    debug = "vide";
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

    pthread_mutex_lock(&data->mutex);
    
    coup->caseArrivee = *pos;
    if(coup->propCoup != PASSE) coup->propCoup = POSE;

    data->fini++;
    data->ia_first++;
    

    pthread_mutex_unlock(&data->mutex);

    return IA_OK;
}
