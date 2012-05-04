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
//  Gestion des threads POSIX
//  Interface avec l'arbitre (implémentation du protocole)
//  Modélisation du jeu
//  Interface avec l'IA
#include "protocole.h"
#include "thread.h"
#include "client.h"
#include "ia.h"
#include "jeu.h"


int main_traitementArgs(int argc, char **argv, char *machine, int *port, char login[]);
int main_partie(char *machine, int port, char login[]);

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

    char machine[TAIL_CHAIN],
	 login[TAIL_CHAIN];
	 
    ia_err iaErr;

    srand(time(NULL));

    // Récupération des arguments
    // + vérification des erreurs
    err = main_traitementArgs(argc,argv,machine,&port,login);
    if(err > 0) { return err; }
    
    iaErr = ia_initJVM();
    if(iaErr == IA_ERR) 
    {
	return -1 ; 
    }

    // Lancement du tournoi et déroulement des parties
    err = main_partie(machine,port,login);
    if(err > 0) { return err; }

    
    iaErr = ia_closeJVM();
    if(iaErr == IA_ERR) { return -1; }

    return 0;
}


/**
 * Gestion de la partie
 *
 * @param machine   Nom de la machine hébergeant l'arbitre
 * @param port	    Port de la machine hébergeant l'arbitre
 * @param login	    Login du joueur
 * @return 0	    Tout s'est bien passé
 * @return 1	    Erreur
 */
int main_partie(char *machine, int port, char login[])
{
    int sockArbitre;
    int joueur;
    int adversaire;
    int nbCoup;
    int err;

    TypBooleen finTournoi;
    TypBooleen finPartie;
    TypBooleen premier;

    Shared_vars *data;

    jeu_err jeuErr;
    client_err clientErr;

    pthread_t thread_ia,
	      thread_client;

    // Initialisation des données
    data = (Shared_vars*) malloc(sizeof(Shared_vars));
    if(data == NULL)
    { 
	if(DEBUG)
	{
	    printf("[DEBUG] - main_partie() \n");
	    perror("[DEBUG] - Impossible d'allouer la mémoire\n");
	    printf("-----------------------------------------\n");
	}
	return 1;
    }
    else if(DEBUG)
    {
	printf("[DEBUG] - main_partie() \n");
	printf("[DEBUG] - Allocation des variables partagées réussie \n");
	printf("-----------------------------------------------------\n");
    }

    err = pthread_mutex_init(&data->mutex,NULL);
    if( err == 0)
    { 
	if(DEBUG)
	{
	    printf("[DEBUG] - main_partie() \n");
	    printf("[DEBUG] - Mutex initialisé\n");
	    printf("--------------------------\n");
	}
    }
    else if(DEBUG)
    {
	printf("[DEBUG] - main_partie() \n");
	perror("[DEBUG] - Erreur lors de l'initialisation du mutex\n");
	printf("--------------------------------------------------\n");
	return 1;
    }
/*
    // Connexion à l'arbitre 
    // + contrôle des erreurs
    clientErr = client_connexion(machine,port,&sockArbitre);
    if(clientErr == CONN_ERR) { return 1; }
    
    data->socket = sockArbitre ;

    // Identification
    // + controle des erreurs
    clientErr = client_identification(sockArbitre,login,&joueur);
    if(clientErr == IDENT_ERR || clientErr == IDENT_LOGIN) { return 1; }

    // Boucle du tournoi
    do {

	// Demande de partie
	// + contrôle des erreurs
	clientErr = client_partie(sockArbitre,joueur,&finTournoi,&premier,&adversaire);
	if(clientErr != PARTIE_OK) { finTournoi = VRAI; }

	if(finTournoi == FAUX)
	{
	    //
	    // Cas 1 : Le tournoi continue
	    //

	    // Initialisation des variables locales
	    finPartie = FAUX;
	    nbCoup = 0;

	    // Initialisation du plateau de jeu
	    // + traitement des erreurs
	    jeuErr = jeu_init();
	    if(jeuErr == JEU_ERR) { finPartie = VRAI; }

	    // Boucle d'une partie
	    while(finPartie == FAUX)
	    {		*/
		// Initialisation d'un coup
		data->coup = (TypCoupReq*) malloc(sizeof(TypCoupReq));
		if(data->coup == NULL)
		{
		    printf("[DEBUG] - main_partie() \n");
		    printf("[DEBUG] - Impossible d'allouer la mémoire\n");
		    printf("-----------------------------------------\n");	    
		    //break;
		}
/*

		// A qui le tour ?
		if(!premier)
		{
		    //
		    // Tour de l'adversaire
		    //

		    // Attente du coup de l'adversaire
		    // + traitement des erreurs
		    clientErr = client_attendCoup(sockArbitre,data->coup);
		    if(clientErr != COUP_OK) finPartie = VRAI;
		}
		else
		{
		    // 
		    // Tour du joueur
		    //

		    // Peuplement de la structure de coup
		    data->coup->idRequest = COUP;
		    data->coup->numeroDuCoup = nbCoup;
*/		    
		    // Lancement du thread de calcul du coup
		    thread_init(data);

		    if(pthread_create(&thread_ia,NULL,ia_calculeCoup,(void*)data))
		    {
			if(DEBUG) { perror("[DEBUG] - pthread_create ( thread_ia ) :"); }
			return EXIT_FAILURE;
		    }
/*
		    void* status;
		    pthread_join(thread_ia,&status);
		    
/*
		    // Lancement du thread d'attente d'un éventuel timeout
		    if(pthread_create(&thread_client,NULL,client_attendTimeout,(void*)data))
		    {
			if(DEBUG) { perror("[DEBUG] - pthread_create ( thread_client ) : "); }
			return EXIT_FAILURE;
		    }
*/
		    int finThread = 0;
		    int finIA = 0;

		    // Boucle d'attente de fin d'un thread
		    while(!finThread)
		    {			
			// Utilisation du mutex
			pthread_mutex_lock(&data->mutex);
			
			finThread = data->fini ;
			finIA = data->ia_first ;

			pthread_mutex_unlock(&data->mutex);
		    }
/*
		    if(finIA)
		    {
			// Le thread de calcul du coup de l'IA à terminé en premier.
			// Le coup est initialisé

			// On stoppe l'autre thread
			pthread_cancel(thread_client);

			if(DEBUG)
			{
			    printf("[DEBUG] - main_partie()\n");
			    printf("[DEBUG] - Fin naturelle du thread IA\n");
			    printf("----------------------------------\n");
			}
			
		    }
		    else
		    {
			// Le client à reçu un message de timeout

			// On stoppe le thread de l'IA
			pthread_cancel(thread_ia);

			// Inutile de continuer : la partie est perdue			
			if(DEBUG)
			{
			    printf("[DEBUG] - main_partie()\n");
			    printf("[DEBUG] - Fin naturelle du thread Timeout\n");
			    printf("----------------------------------\n");
			}
			
			break;
		    }

		    // A ce point du programme :
		    //	- les thread sont terminés (normalement ou stoppés) => plus besoin des sémaphores
		    //	- le client n'a pas reçu de timeout => on peut envoyer un coup	

		    clientErr = client_envoieCoup(sockArbitre,data->coup);
		    if(clientErr != COUP_OK) finPartie = VRAI;

		}

		// Coup valide ?
		if(clientErr == COUP_OK) 
		{ 
		    // Ajout du pion dans la modélisation du plateau
		    jeuErr = jeu_ajouterCoup(*(data->coup),premier);
		}

		// Affichage du plateau
		jeuErr = jeu_afficherJeu(premier);

		// Si le coup est "GAGNE", "PERD" ou "NULLE", la partie est finie
		if(data->coup->propCoup != POSE) finPartie = VRAI;

		// Changement de tour
		premier = ((premier)?FAUX:VRAI);

		// Mise à jour du nombre de coups joués
		nbCoup++;

		// Libération mémoire
		free(data->coup);
	    }

	    // Libération du plateau de jeu
	    jeuErr = jeu_fin();
	}
    } while(!finTournoi);
*/
    if(pthread_mutex_destroy(&data->mutex) == 0 && DEBUG)
    {
	printf("[DEBUG] - main_partie() \n");
	printf("[DEBUG] - Mutex détruit\n");
	printf("-----------------------\n");
    }
    else if(DEBUG)
    {
	printf("[DEBUG] - main_partie() \n");
	perror("[DEBUG] - Erreur lors de la destruction du mutex\n");
	printf("------------------------------------------------\n");
    }


    free(data);

    return 0;
}



/**
 * Traite les arguments passés en ligne de commande et la saisie du login
 *
 * @param argc	    Nombre d'arguments passés en ligne de commande
 * @param argv	    Tableau des arguments passés en ligne de commande
 * @param machine   Nom de la machine hébergeant l'arbitre
 * @param port	    Port de la machine hébergeant l'arbitre
 * @param login	    Login du joueur
 * @return 0	    Tout s'est bien passé
 * @return 1	    Erreur
 */
int main_traitementArgs(int argc, char **argv, char machine[], int *port, char login[])
{
    int err;

    // Récupération des arguments passés en paramètres
    // ou affichage de la commande à utiliser
    if(argc == 3)
    {
	strcpy(machine,argv[1]);
	*port = atoi(argv[2]);
	if(DEBUG) { printf("[DEBUG] - Machine >  %s:%d\n",machine,*port); }
    }
    else
    {
	printf("Usage : %s MACHINE PORT\n",argv[0]);
	return 1;
    }

    // Récupération du login
    printf("Login : ");
    err = scanf("%s",login);
    if(DEBUG) { printf("[DEBUG] - Login : %s\n",login); }


    return 0;
}
