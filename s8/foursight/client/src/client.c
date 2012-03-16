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

#include <string.h>

#include "fctSock.h"
#include "protocole.h"

typedef enum { ST_IDENTIFICATION, ST_PARTIE, ST_COUP, ST_KO } State ;

int main (int argc, char **argv)
{
    int sockArbitre,		// Socket de communication avec l'arbitre
	port,			// Port de la machine executant l'arbitre
	err,			// Variable d'erreur
	joueur,			// Numéro du joueur
	adversaire,		// Numéro de l'adversaire
	state;			// Etat du client
    char *machine,		// Nom de la machine executant l'arbitre
	 login[TAIL_CHAIN];	// Login d'identifiaction auprès de l'arbitre

    TypBooleen premier;		// VRAI si l'on commence à jouer en premier, FAUX sinon

    TypIdentificationReq *identReq;  // Requête d'identification
    TypIdentificationRep *identRep;  // Réponse de l'identification

    TypPartieReq *partieReq;	// Requête de demande de partie
    TypPartieRep *partieRep;	// Réponse de la demande de partie

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
    identReq = (TypIdentificationReq*) malloc(sizeof(TypIdentificationReq));
    identRep = (TypIdentificationRep*) malloc(sizeof(TypIdentificationRep));
    partieReq = (TypPartieReq*) malloc(sizeof(TypPartieReq));
    partieRep = (TypPartieRep*) malloc(sizeof(TypPartieRep));


    // Connexion à l'arbitre
    // + contrôle des erreurs
    sockArbitre = socketClient(machine,port);
    if(sockArbitre < 0) { perror("[Erreur]\nLa connexion à l'arbitre a échoué\n"); }
    else 
    { 
	printf("[Message]Connexion établie\n"); 
	state = ST_IDENTIFICATION;
    }


    //
    // Identification
    //

    if(state == ST_IDENTIFICATION)
    {
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
			joueur = identRep->joueur;
			state = ST_PARTIE;
		    }
		}
	    }
	}
    }

    
    //
    // Demande de partie
    //

    // Contrôle de l'étape :
    // Si state = PARTIE alors l'identification s'est bien passé 
    if(state == PARTIE)
    {
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
			if (partieRep->finTournoi == FAUX)
			{
			    adversaire = partieRep->adversaire;
			    premier = partieRep->premier;
			    state = ST_COUP;
			}
		    }
		}
	    }
	}
    }


    //
    // Gestion des coups
    //
    if(state == COUP)
    {

    }
    
    //
    // TODO : 
    //	- Implémenter la gestion des coups
    //	- Inclure une boucle pour les parties ?
    //	- Stopper le moteur IA dans les libérations
    //


    //
    // Arrêt de la connexion avec l'arbitre
    //
    shutdown(sockArbitre,2);
    close(sockArbitre);

    //
    // Libération de la mémoire 
    //
    free(identReq);
    free(identRep);
    free(partieReq);
    free(partieRep);


    return 0;
}
