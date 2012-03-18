/*
 * Projet MOIA - SCS
 * 2011 - 2012
 *
 * Fichier principal (contenant le main)
 * 
 * Barberot Mathieu &
 * Racenet Joan
 *
 */

#define DEBUG 1

#include <time.h>

#include "client.h"
#include "jeu.h"
#include "ia.h"

/**
 * Fonction principale
 *
 * @param argc	Nombre d'arguments passés en paramètres
 * @param argv	Arguments passés en paramètres
 * @return	
 */
int main (int argc, char **argv)
{
    int port,
	err;
    char *machine,
	 login[TAIL_CHAIN];

    srand(time(NULL));


    // Récupération des arguments passés en paramètres
    // ou affichage de la commande à utiliser
    if(argc == 3)
    {
	machine = argv[1];
	port = atoi(argv[2]);
	if(DEBUG) { printf("[DEBUG] %s:%d\n",machine,port); }
    }
    else
    {
	printf("Usage : %s MACHINE PORT\n",argv[0]);
	return 1;
    }

    // Récupération du login
    printf("Login : ");
    err = scanf("%s",login);
    if(DEBUG) { printf("[DEBUG] Login : %s\n",login); }


    int sockArbitre,
	joueur,
	adversaire,
	nbCoup;

    TypBooleen finTournoi,
	       finPartie,
	       premier;

    TypCoupReq *coup;

    jeu_err jeuErr;
    client_err clientErr;
    ia_err iaErr;

    jeuErr = jeu_init();
    clientErr = client_connexion(machine,port,&sockArbitre);
    clientErr = client_identification(sockArbitre,login,&joueur);


    do {
	clientErr = client_partie(sockArbitre,joueur,&finTournoi,&premier,&adversaire);

	if(!finTournoi)
	{
	    finPartie = VRAI;
	    nbCoup = 0;

	    while(!finPartie)
	    {
		coup = (TypCoupReq*) malloc(sizeof(TypCoupReq));

		if(!premier)
		{
		    clientErr = client_attendCoup(sockArbitre,coup);
		    if(clientErr != COUP_OK) break;
		}
		else
		{
		    coup->idRequest = COUP;
		    coup->numeroDuCoup = nbCoup;
		    iaErr = ia_calculeCoup(coup);
		    clientErr = client_envoieCoup(sockArbitre,coup);
		    if(clientErr != COUP_OK) break;
		}

		jeuErr = jeu_ajouterCoup(*coup,premier);
		jeuErr = jeu_afficherJeu();

		nbCoup++;

		free(coup);
	    }
	}
    } while(!finTournoi);


    return 0;
}
