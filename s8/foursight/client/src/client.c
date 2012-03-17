/* 
 * Projet MOIA - SCS
 * 2011 - 2012
 * 
 * Client de connection à l'arbitre
 * 
 * Barberot Mathieu &
 * Racenet Joan
 * 
 */
#include <time.h>
#include "client.h"



/**
 * Calcul un coup aléatoirement.
 *
 * Détails :
 *	- 
 *  Pas de prise en compte des cases déjà occupées
 *  Pas de prise en compte des pions restants
 *	+
 *  Ne choisis pas de case en dehors du tableau
 *  Graine aléatoire générée via le timestamp time(NULL)
 *
 * Testée : OK
 *
 * @param coupReq   Un coup vide (la mémoire doit être allouée).
 *		    Sera remplit dans la fonction
 */
void IA(TypCoupReq *coupReq)
{
    // Les variables aléatoires
    int aleaPiece,
	aleaCoup,
	aleaRow,
	aleaCol;

    // La case
    TypPosition *pos = (TypPosition*) malloc(sizeof(TypPosition));

    // Des tableaux de correspondances nb aleatoire <-> valeur
    TypPiece	piece[3]    = { BLANC, ROUGE, JAUNE } ;
    TypPropCoup	coup[4]	    = { POSE, GAGNE, PERD, NULLE } ;
    TypLigne	col[4]	    = { CO_ZERO, CO_UN, CO_DEUX, CO_TROIS } ;
    TypColonne	row[4]	    = { LI_ZERO, LI_UN, LI_DEUX, LI_TROIS } ;

    // Graine
    srand(time(NULL));

    // Tirage aléatoire
    aleaPiece = rand()%3;
    aleaCoup = rand()%4;
    aleaRow = rand()%4;
    aleaCol = rand()%4;

    // Création de la case 
    pos->ligne = row[aleaRow];
    pos->colonne = col[aleaCol];

    // Remplissage du coup
    coupReq->propCoup = coup[aleaCoup] ;
    coupReq->typePiece = piece[aleaPiece] ;
    coupReq->caseArrivee = *pos;

    // Affichage Tmp
    printf("%d - %d - %d - %d\n",aleaPiece,aleaCoup,aleaRow,aleaCol);

}




int main (int argc, char **argv)
{
    int sockArbitre,		// Socket de communication avec l'arbitre
	port,			// Port de la machine executant l'arbitre
	joueur,			// Numéro du joueur
	adversaire,		// Numéro de l'adversaire
	state;			// Etat du client
    char *machine,		// Nom de la machine executant l'arbitre
	 login[TAIL_CHAIN];	// Login d'identifiaction auprès de l'arbitre

    TypBooleen finTournoi,	// VRAI si le tournoi est terminé, FAUX sinon
	       premier;		// VRAI si l'on commence à jouer en premier, FAUX sinon


    //
    // Vérification des arguments
    //
    switch(argc)
    {
	case 1 : printf("\nVous avez oublié le PORT, le nom de la MACHINE et le LOGIN\nUsage : %s PORT MACHINE LOGIN\n\n",argv[0]);
		 return 1;
	case 2 : printf("\nVous avez oublié le nom de la MACHINE et le LOGIN\nUsage : %s PORT MACHINE LOGIN\n\n",argv[0]);
		 return 1;
	case 3 : printf("\nVous avez oublié le LOGIN\nUsage : %s PORT MACHINE LOGIN\n\n",argv[0]);
		 return 1;
    }

    // Récupération du port et du nom de la machine
    port = atoi(argv[1]);
    machine = argv[2];
    strcpy(login,argv[3]);

    // Initialisations
    state = ST_KO;

    // Connexion à l'arbitre
    // + contrôle des erreurs
    sockArbitre = socketClient(machine,port);
    if(sockArbitre < 0) { perror("[Erreur]\nLa connexion à l'arbitre a échoué\n"); }
    else 
    { 
	printf("[Message]Connexion établie\n"); 
	state = ST_IDENTIFICATION;
    }

    if(state == ST_IDENTIFICATION)
    {
	state = demandeIdentification(sockArbitre,login,&joueur);
    }


    if(state == ST_PARTIE)
    {
	state = demandePartie(sockArbitre,joueur,&finTournoi,&premier,&adversaire);
    }

    printf("---------piou ---------\n");

    if(state == ST_COUP)
    {
	state = calculCoup(sockArbitre,premier);
    }


    //
    // Arrêt de la connexion avec l'arbitre
    //
    shutdown(sockArbitre,2);
    close(sockArbitre);


    return 0;
}

/**
 * Demande une identification auprès de l'arbitre.
 * L'authentification consiste à donner un login à l'arbitre,
 * qui, en retour, nous assigne un numéro de joueur.
 *
 * La fonction retourne un état 
 *
 * Attention à bien passer l'adresse de la variable joueur,
 * car elle sera initialisée dans cette fonction.
 * 
 *
 * @param sockArbitre	Socket de l'arbitre
 * @param login		Login passé en argument
 * @param joueur	Numéro de joueur
 * @return ST_KO	Echec de l'identification		
 * @return ST_PARTIE	Réussite de l'identification, on passe à l'état suivant
 */
State demandeIdentification(int sockArbitre, char login[], int *joueur)
{
    int err;			    // Variable pour les contrôles des erreurs
    State state;		    // Etat 
    TypIdentificationReq *identReq; // Requête d'identification
    TypIdentificationRep *identRep; // Réponse de l'identification

    // Initialisations
    state = ST_KO;
    identReq = (TypIdentificationReq*) malloc(sizeof(TypIdentificationReq));
    identRep = (TypIdentificationRep*) malloc(sizeof(TypIdentificationRep));

    // Préparation de la requête
    // + contrôle des erreurs
    if(identReq == NULL) { perror("[Erreur]\nImpossible de créer la requête d'identification\n"); }
    else
    {
	identReq->idRequest = IDENTIFICATION ;
	strcpy(identReq->nom,login);

	// Envoi de la requête
	// + contrôle des erreurs
	err = send(sockArbitre,identReq,sizeof(TypIdentificationReq),0);
	if(err < 0) { perror("[Erreur]\nEchec de l'envoi de la requête d'identification\n"); }
	else
	{
	    printf("[Message]Requête d'identification envoyée !\n");

	    // Reception de la réponse de l'arbitre
	    // + contrôle des erreurs
	    err = recv(sockArbitre,identRep,sizeof(TypIdentificationRep),0);
	    if(err < 0) { perror("[Erreur]\nEchec de la réception de la requête d'identification\n"); }
	    else
	    {
		printf("[Message]Requête d'identification reçue !\n");

		// Inspection de la réponse
		if(identRep->err == ERR_NOM) { printf("[Erreur]\nLogin inconnu"); }
		else
		{
		    // Récupération du numero de joueur
		    // & passage à l'étape de demande de partie
		    *joueur = identRep->joueur;
		    state = ST_PARTIE;
		}
	    }
	}
    }
    // Libération de la mémoire allouée
    free(identReq);
    free(identRep);

    return state;
}


/**
 * Demande une partie à l'arbitre.
 * Cette demande consiste à envoyer notre numéro de joueur à l'arbitre
 * pour pouvoir commencer à jouer. Celui-ci nous envoie alors un booleen
 * indiquant si le tournoi est achevé ou non.
 *
 * Cas où le tournoi est achevé :
 *  Les variables premier et adversaire ne sont pas renseignées (NULL)
 *  Le client peut alors se déconnecter.
 *
 * Cas où le tournoi n'est pas terminé :
 *  Les variables permier et adversaire sont renseignées
 *  On passe à l'étape de calcul des coups
 *
 * 
 * Attention à bien passer des pointeurs pour les paramètres finTournoi,
 * premier et adversaire car elle seront initialisées dans la fonction.
 * De plus les variables permier et adversaire auront comme valeur NULL
 * si la finTournoi == VRAI
 *
 * @param sockArbitre	    Socket de l'arbitre
 * @param joueur	    Numéro du joueur
 * @param finTournoi	    VRAI si le tournoi est terminé, FAUX sinon
 * @param premier	    VRAI si le client est le premier à jouer, FAUX si c'est l'adversaire
 * @param adversaire	    Numéro du joueur adverse
 * @return ST_KO	    Echec de la demande de partie
 * @return ST_DECONNEXION   Fin du tournoi, passage à l'état de déconnexion du client
 * @return ST_COUP	    Début d'une nouvelle partie, passage à l'état de calcul des coups
 */
State demandePartie(int sockArbitre, int joueur, TypBooleen *finTournoi, TypBooleen *premier, int *adversaire)
{
    int err;			// Variable pour le contrôle des erreurs
    State state;		// Etat
    TypPartieReq *partieReq;	// Requête de demande de partie
    TypPartieRep *partieRep;	// Réponse de la demande de partie

    // Initialisations
    state = ST_KO;
    partieReq = (TypPartieReq*) malloc(sizeof(TypPartieReq));
    partieRep = (TypPartieRep*) malloc(sizeof(TypPartieRep));

    // Préparation de la requête
    // + contrôle des erreurs
    if(partieReq == NULL) { perror("[Erreur]\nImpossible de créer la requête demande de partie\n"); }
    else
    {
	partieReq->idRequest = PARTIE;
	partieReq->joueur = joueur;

	// Envoi de la requête
	// + contrôle des erreurs
	err = send(sockArbitre,partieReq,sizeof(TypPartieReq),0);
	if(err < 0) { perror("[Erreur]\nEchec de l'envoi de la demande de partie"); }
	else
	{
	    printf("[Message]Demande de partie envoyée !\n");

	    // Réception de la réponse
	    // + contrôle des erreurs
	    err = recv(sockArbitre,partieRep,sizeof(TypPartieRep),0);
	    if(err < 0) { perror("[Erreur]\nEchec de la réception de la demande de partie"); }
	    else
	    {
		printf("[Message]Demande de partie reçue !\n");

		// Inspection de la réponse
		if(partieRep->err == ERR_JOUEUR) { printf("[Erreur]Numéro de joueur inconnu\n"); }
		else 
		{
		    *finTournoi = partieRep->finTournoi ;

		    if (partieRep->finTournoi == FAUX)
		    {
			*adversaire = partieRep->adversaire;
			*premier = partieRep->premier;
			state = ST_COUP;
		    }
		    else
		    {
			adversaire = NULL;
			premier = NULL;
			state = ST_DECONNEXION;
		    }
		}
	    }
	}
    }
    free(partieReq);
    free(partieRep);
    return state;
}

/**
 * Cette fonction gère le déroulement des coups :
 *  - demande de calcul à l'IA, envoi d'un coup à l'arbitre, attente de sa réponse
 *  - attente de la validation du coup adverse et attente du coup adverse
 *
 * @param sockArbitre	Socket de l'arbitre
 * @param premier	VRAI si c'est le tour du joueur, FAUX si c'est celui de l'adversaire
 * @return ST_KO	Echec durant le déroulement de la partie
 * @return ST_PARTIE	La partie s'est terminée, on demande une nouvelle partie
 */
State calculCoup(int sockArbitre, TypBooleen premier)
{
    int err,		    // Variable pour le contrôle des erreurs
	partieFinie,	    // Flag de sortie de la boucle while
	noTour;		    // Numéro du tour/coup

    TypBooleen tourJoueur;  // VRAI = tour de l'adversaire, FAUX = tour du joueur

    State state;	    // Etat 

    TypCoupReq *coupReq;    // Un coup (du joueur ou de l'adversaire)
    TypCoupRep *coupRep;    // Réponse de l'arbitre au coup proposé

    // Initialisations
    partieFinie = 0;
    noTour = 0;
    tourJoueur = premier;
    state = ST_KO;

    while(partieFinie == 0)
    {
	// Allocation de la mémoire
	coupReq = (TypCoupReq*) malloc(sizeof(TypCoupReq));
	coupRep = (TypCoupRep*) malloc(sizeof(TypCoupRep));

	if(coupReq == NULL || coupRep == NULL)
	{
	    printf("[Erreur]Impossible d'allouer la mémoire pour les coups\n");
	    break;
	}

	// Tour du joueur
	if(tourJoueur == VRAI)
	{	    
	    // Calcul d'un coup
	    coupReq->idRequest = COUP;
	    coupReq->numeroDuCoup = noTour;
	    IA(coupReq);
	    // Soumission du coup à l'arbitre
	    err = send(sockArbitre,coupReq,sizeof(TypCoupReq),0);
	    if(err < 0) { perror("[Erreur]Echec de l'envoi du coup à l'arbitre"); break; }
	    else 
	    { 
		printf("Coup envoyé !\n"); 

		// Réception du message de l'arbitre
		err = recv(sockArbitre,coupRep,sizeof(TypCoupRep),0);
		if(err < 0) { perror("[Erreur]Echec de la réception de la réponse de l'arbitre au coup du joueur"); break; }
		else
		{
		    if(coupRep->err != ERR_OK) { printf("Erreur dans la requête d'envoi du coup\n"); break; }
		    else if(coupRep->validCoup != VALID) 
		    { 
			printf("[Erreur]Coup invalide !\n");
			break;
		    } 
		}
	    }

	    //
	    // TODO :
	    // Mise à jour du plateau du jeu avec le nouveau pion
	    //

	    // Mise à jour du tour
	    tourJoueur = FAUX;
	}
	else
	{
	    // Réception de la validation du coup de l'adversaire
	    err = recv(sockArbitre,coupRep,sizeof(TypCoupRep),0);
	    if(err < 0) { perror("[Erreur]Echec de la réception du coup de l'adversaire"); break; }
	    else
	    {
		// Reception du coup de l'adversaire
		// + contrôle des erreurs
		err = recv(sockArbitre,coupReq,sizeof(TypCoupReq),0);
		if(err < 0) { perror("[Erreur]Echec de la réception du coup de l'adversaire"); break; }
		else
		{
		    // Inspection du coup de l'adversaire
		    if(coupReq->propCoup != POSE)
		    {
			// Mise à jour du flag de sortie de boucle
			partieFinie = 1;
		    }

		    //
		    // TODO :
		    // Mise à jour du plateau du foursight
		    // pour ajouter le pion placé par l'adversaire
		    //

		}
	    }

	    // Mise à jour du tour
	    tourJoueur = VRAI;
	}
	//Incrémentation du compteur de tour
	noTour++;
	// Libération de la mémoire allouée
	free(coupReq);
	free(coupRep);

    }

    if(partieFinie == 1)
    {
	state = ST_PARTIE;
    }

    return state;
}
