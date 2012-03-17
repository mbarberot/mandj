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

#include "client.h"

//
// TODO : 
//	- Implémenter la gestion des coups
//	- Placer des mallocs/free dans la boucle while?
//	- Inclure une boucle pour les parties ?
//	- Stopper le moteur IA dans les libérations
//

State calculCoup(int sockArbitre, TypBooleen premier)
{
    int err,
	partieFinie;
    State state;
    TypCoupReq *coupReq;
    TypCoupRep *coupRep;

    partieFinie = 0;
    state = ST_KO;
    coupReq = (TypCoupReq*) malloc(sizeof(TypCoupReq));
    coupRep = (TypCoupRep*) malloc(sizeof(TypCoupRep));
    
    if(premier == FAUX)
    {
	// On commence par attendre le coup de l'adversaire
    }

    while(partieFinie == 0)
    {
	// On construit une requête
	// On met à jour 'partieFinie' (si notre coup est le dernier)
	// On l'envoie à l'arbitre
	// On attend la réponse de l'arbitre
	//  - KO : On a perdu
	//  - OK : On continue
	// On attend le coup de l'adversaire
	// On met à jour 'partieFinie' (si son coup est le dernier)
	// 
	// On recommence !
    }

    free(coupReq);
    free(coupRep);
    
    return state;
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

    if(state == ST_COUP)
    {
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

