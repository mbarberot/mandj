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

// Librairies :
//  Standard
//  IO
//  Temps
#include <stdlib.h>
#include <stdio.h>
#include <time.h>

// Fichiers d'entetes
//  Protocole de communication avec l'arbitre  
//  Interface avec l'arbitre (implémentation du protocole)
//  Modélisation du jeu
//  Interface avec l'IA
#include "protocole.h"
#include "client.h"
#include "ia.h"
#include "jeu.h"




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
/*
    // Récupération du login
    printf("Login : ");
    err = scanf("%s",login);
    if(DEBUG) { printf("[DEBUG] Login : %s\n",login); }
*/

    int sockArbitre,
	joueur,
	adversaire,
	nbCoup,
	pid;

    TypBooleen finTournoi,
	       finPartie,
	       premier;



    jeu_err jeuErr;
    client_err clientErr;
    ia_err iaErr;


    static Data data; 
    static pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
    TypCoupReq* coup = (TypCoupReq*)malloc(sizeof(TypCoupReq));

    data.mutex = mutex;
    data.coup = coup;

    pthread_t thread_ia;


    printf("[] - Avant calcul coup\n");
    if(pthread_create(&thread_ia,NULL,ia_calculeCoup,NULL))
    {
	perror("[] - pthread_create");
	return EXIT_FAILURE;
    }

    //
    // Comment détecter la fin du thread pour stopper l'attente du timeout ? -> mutex ?
    //
    // sock non blo
    // while(!msg et !thread fini) { recv( -- ) }
    //
    // lancer l'attente dans un thread, détecter le premier qui fini
    //
    // 
    //
    
    // <<< Attente du timeout ici
    system("sleep 5"); 
    pthread_cancel(thread_ia);
    // <<< Attente du timeout ici
    
    printf("[] - Après calcul coup\n");


/*
    clientErr = client_connexion(machine,port,&sockArbitre);
    clientErr = client_identification(sockArbitre,login,&joueur);

    do {
	clientErr = client_partie(sockArbitre,joueur,&finTournoi,&premier,&adversaire);
	if(clientErr != PARTIE_OK) { return 1; }

	if(finTournoi == FAUX)
	{
	    finPartie = FAUX;
	    nbCoup = 0;

	    jeuErr = jeu_init();
	    if(jeuErr == JEU_ERR) { finPartie = VRAI ; }

	    while(finPartie == FAUX)
	    {		
		data.coup = (TypCoupReq*) malloc(sizeof(TypCoupReq));

		if(!premier)
		{
		    clientErr = client_attendCoup(sockArbitre,coup);
		    if(clientErr != COUP_OK) finPartie = VRAI;
		}
		else
		{
		    coup->idRequest = COUP;
		    coup->numeroDuCoup = nbCoup;

		    //
		    //  Utilisation de threads :
		    //  -->  calculCoup lance un thread de calcul sur data
		    // -->  utilisation de mutex pour gérer les sections critiques
		    //
		    // <--  attente du timeout ici
		    // -->  arrêt du calcul si timeout
		    // ---  reception du coup sinon (IA_OK)
		    //

		    iaErr = ia_calculeCoup(coup);
		    clientErr = client_envoieCoup(sockArbitre,coup);
		    if(clientErr != COUP_OK) finPartie = VRAI;

		}

		if(clientErr == COUP_OK) 
		{ 
		    jeuErr = jeu_ajouterCoup(*coup,premier);
		}
		jeuErr = jeu_afficherJeu();

		if(coup->propCoup != POSE) finPartie = VRAI;

		premier = ((premier)?FAUX:VRAI);

		nbCoup++;

		free(data.coup);
	    }

	    jeuErr = jeu_fin();
	}
    } while(!finTournoi);
*/

    return 0;
}
