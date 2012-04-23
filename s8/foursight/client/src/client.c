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
#define DEBUG 1

#include "client.h"


void client_perror(TypErreur err)
{
    if(DEBUG)
    {
	switch(err)
	{
	    case ERR_NOM    :   printf("[DEBUG] Mauvais Login.\n"); break;
	    case ERR_JOUEUR :   printf("[DEBUG] Numéro de joueur inconnu.\n"); break;
	    case ERR_COUP   :   printf("[DEBUG] Mauvais coup.\n"); break;
	    case ERR_TYP    :   printf("[DEBUG] Mauvais type de requete.\n"); break;
	    default : break;
	}
    }
}


client_err client_connexion(char *machine, int port, int *sockArbitre)
{
    int sock;

    printf("%s:%d\n",machine,port);

    // Connexion au serveur
    // + contrôle des erreurs
    sock = socketClient(machine,port);
    if(sock < 0)
    {
	if(DEBUG)
	{
	    printf("[DEBUG] client_connexion \n");
	    printf("[DEBUG] Impossible de se connecter à l'arbitre\n");
	    printf("----------------------------------------------\n");
	}
	return CONN_ERR;
    }
    else if(DEBUG)
    {	
	printf("[DEBUG] client_connexion \n");
	printf("[DEBUG] Connexion établie !\n");
	printf("---------------------------\n");
    }

    *sockArbitre = sock;
    return CONN_OK;
}



client_err client_identification(int sockArbitre, char login[], int *joueur)
{
    int err;

    TypIdentificationReq *identReq = (TypIdentificationReq*) malloc(sizeof(TypIdentificationReq)); 
    TypIdentificationRep *identRep = (TypIdentificationRep*) malloc(sizeof(TypIdentificationRep)); 

    if(identReq == NULL || identRep == NULL)
    {
	if(DEBUG)
	{
	    printf("[DEBUG] client_identification \n");
	    printf("[DEBUG] Impossible d'allouer la mémoire nécessaire\n");
	    printf("--------------------------------------------------\n");
	}
	return IDENT_ERR;
    }

    // Construction de la requête
    identReq->idRequest = IDENTIFICATION ;
    strcpy(identReq->nom,login);

    // Envoi de la requête
    err = send(sockArbitre,identReq,sizeof(TypIdentificationReq),0);
    if(err < 0) 
    {
	if(DEBUG)
	{
	    printf("[DEBUG] client_identification \n");
	    printf("[DEBUG] Erreur lors de l'envoi de la requête\n");
	    printf("--------------------------------------------\n");
	}
	return IDENT_ERR;
    }

    if(DEBUG) 
    { 
	printf("[DEBUG] client_identification \n");
	printf("[DEBUG] Requête d'identification envoyée à l'arbitre\n");
	printf("[DEBUG] En attente d'un réponse\n");
	printf("----------------------------------------------------\n");
    }

    // Reception de la réponse
    err = recv(sockArbitre,identRep,sizeof(TypIdentificationRep),0);
    if(err < 0)
    {
	if(DEBUG)
	{
	    printf("[DEBUG] client_identification \n");
	    printf("[DEBUG] Erreur lors de la réception de la requête\n");
	    printf("-------------------------------------------------\n");
	}
	return IDENT_ERR;
    }
    else if(identRep->err == ERR_NOM)
    {
	if(DEBUG)
	{
	    printf("[DEBUG] client_identification \n");
	    printf("[DEBUG] Réponse de l'arbitre reçue :\n");
	    printf("[DEBUG] Mauvais Login\n");
	    printf("------------------------------------\n");
	}
	return IDENT_LOGIN;
    }
    else if(identRep->err == ERR_OK)
    {
	if(DEBUG)
	{
	    printf("[DEBUG] client_identification \n");
	    printf("[DEBUG] Réponse de l'arbitre reçue :\n");
	    printf("[DEBUG] Identification réussie\n");
	    printf("------------------------------------\n");
	}

	*joueur = identRep->joueur;
    }
    else
    {
	if(DEBUG)
	{
	    printf("[DEBUG] client_identification \n");
	    printf("[DEBUG] Réponse de l'arbitre reçue :\n");
	    printf("[DEBUG] Une erreur s'est produite.\n");
	    client_perror(identRep->err);
	    printf("------------------------------------\n");
	}

    }

    free(identReq);
    free(identRep);

    return IDENT_OK;
}



client_err client_partie(int sockArbitre, int joueur, TypBooleen *finTournoi, TypBooleen *premier, int *adversaire)
{
    int err;

    TypPartieReq *partieReq = (TypPartieReq*) malloc(sizeof(TypPartieReq));  
    TypPartieRep *partieRep = (TypPartieRep*) malloc(sizeof(TypPartieRep));

    if(partieReq == NULL || partieRep == NULL)
    {
	if(DEBUG)
	{
	    printf("[DEBUG] client_partie \n");
	    printf("[DEBUG] Impossible d'allouer la mémoire nécessaire\n");
	    printf("--------------------------------------------------\n");
	}
	return PARTIE_ERR;
    }

    // Construction de la requête
    partieReq->idRequest = PARTIE ;
    partieReq->joueur = joueur ;

    // Envoi de la requête
    err = send(sockArbitre,partieReq,sizeof(TypPartieReq),0);
    if(err < 0)
    {
	if(DEBUG)
	{
	    printf("[DEBUG] client_partie \n");
	    printf("[DEBUG] Echec de l'envoi de la requête à l'arbitre\n");
	    printf("--------------------------------------------------\n");
	}
	return PARTIE_ERR;
    }

    if(DEBUG)
    {
	printf("[DEBUG] client_partie \n");
	printf("[DEBUG] Requête de demande de partie envoyée à l'arbitre\n");
	printf("[DEBUG] En attente d'une réponse\n");
	printf("------------------------------------\n");
    }

    // Réception de la requête
    err = recv(sockArbitre,partieRep,sizeof(TypPartieRep),0);
    if(err < 0)
    {
	if(DEBUG)
	{
	    printf("[DEBUG] client_partie \n");
	    printf("[DEBUG] Erreur lors de la réception de la requete\n");
	    printf("-------------------------------------------------\n");
	}
    }
    else if(partieRep->err == ERR_OK)
    {
	if(DEBUG)
	{
	    printf("[DEBUG] client_partie \n");
	    printf("[DEBUG] Réponse de l'arbitre reçue :\n");
	}

	*finTournoi = partieRep->finTournoi;

	if(*finTournoi == FAUX) 
	{
	    if(DEBUG) { printf("[DEBUG] Demande de partie acceptée\n"); }

	    *adversaire = partieRep->adversaire;
	    *premier	= partieRep->premier;
	}
	else
	{
	    if(DEBUG) { printf("[DEBUG] Tournoi achevé \n"); }

	    adversaire = NULL;
	    premier = NULL;
	}

	if(DEBUG) { printf("------------------------------------\n"); }
    }
    else if(partieRep->err == ERR_JOUEUR)
    {
	if(DEBUG)
	{
	    printf("[DEBUG] client_partie \n");
	    printf("[DEBUG] Réponse de l'arbitre reçue :\n");
	    printf("[DEBUG] Une erreur s'est produite.\n");
	    client_perror(ERR_JOUEUR);
	    printf("------------------------------------\n");
	    return PARTIE_JOUEUR;
	}
    }
    else
    {
	if(DEBUG)
	{
	    printf("[DEBUG] client_partie \n");
	    printf("[DEBUG] Réponse de l'arbitre reçue :\n");
	    printf("[DEBUG] Une erreur s'est produite.\n");
	    client_perror(partieRep->err);
	    printf("------------------------------------\n");
	}
	return PARTIE_ERR;
    }
    free(partieReq);
    free(partieRep);

    return PARTIE_OK;
}



client_err client_envoieCoup(int sockArbitre, TypCoupReq *coupReq)
{
    int err;

    TypCoupRep *coupRep = (TypCoupRep*) malloc(sizeof(TypCoupRep));

    if(coupReq == NULL || coupRep == NULL)
    {
	if(DEBUG)
	{
	    printf("[DEBUG] client_envoieCoup\n");
	    printf("[DEBUG] Impossible d'allouer la mémoire nécessaire.\n");
	    printf("------------------------------------\n");
	}
	return COUP_ERR;
    }

    // Envoi du coup
    err = send(sockArbitre,coupReq,sizeof(TypCoupReq),0);
    if(err < 0)
    {
	if(DEBUG)
	{
	    printf("[DEBUG] client_envoieCoup\n");
	    printf("[DEBUG] Erreur lors de l'envoi de la requete.\n");
	    printf("------------------------------------\n");
	}
	return COUP_ERR;
    }

    if(DEBUG)
    {
	printf("[DEBUG] client_envoieCoup\n");
	printf("[DEBUG] Requete envoyée à l'arbitre.\n");
	printf("[DEBUG] En attente de la réponse de l'arbitre.\n");
	printf("------------------------------------\n");
    }

    // Réception de la réponse de l'arbitre
    err = recv(sockArbitre,coupRep,sizeof(TypCoupRep),0);
    if(err < 0)
    {
	if(DEBUG)
	{
	    printf("[DEBUG] client_envoieCoup\n");
	    printf("[DEBUG] Erreur lors de la réception de la réponse de l'arbitre.\n");
	    printf("------------------------------------\n");
	}
	return COUP_ERR;
    }
    else if(coupRep->err == ERR_OK)
    {
	if(DEBUG)
	{
	    printf("[DEBUG] client_envoieCoup\n");
	    printf("[DEBUG] Réponse reçue :\n");
	}

	switch(coupRep->validCoup)
	{
	    case VALID :
		if(DEBUG) { printf("[DEBUG] Coup valide\n"); } 
		break;	
	    case TIMEOUT :
		if(DEBUG) { printf("[DEBUG] Timeout\n"); } 
		return COUP_TIMEOUT; 
		break;
	    case TRICHE :
		if(DEBUG) { printf("[DEBUG] Tentative de tricherie\n"); } 
		return COUP_INVALIDE;
		break;	
	}

	if(DEBUG) { printf("------------------------------------\n"); }
    }
    else
    {
	if(DEBUG)
	{
	    printf("[DEBUG] client_envoieCoup \n");
	    printf("[DEBUG] Réponse de l'arbitre reçue :\n");
	    printf("[DEBUG] Une erreur s'est produite.\n");
	    client_perror(coupRep->err);
	    printf("------------------------------------\n");
	}
	return COUP_ERR;
    }

    return COUP_OK;
}




client_err client_attendCoup(int sockArbitre, TypCoupReq *coupReq)
{
    int err;

    TypCoupRep *coupRep = (TypCoupRep*) malloc(sizeof(TypCoupRep));

    if(coupReq == NULL || coupRep == NULL)
    {
	if(DEBUG)
	{
	    printf("[DEBUG] client_attendCoup\n");
	    printf("[DEBUG] Impossible d'allouer la mémoire nécessaire.\n");
	    printf("------------------------------------\n");
	}
	return COUP_ERR;
    }


    // Réception de la validation du coup adverse par l'arbitre
    err = recv(sockArbitre,coupRep,sizeof(TypCoupRep),0);
    if(err < 0)
    {
	if(DEBUG)
	{
	    printf("[DEBUG] client_attendCoup\n");
	    printf("[DEBUG] Erreur lors de la réception de la réponse de l'arbitre.\n");
	    printf("------------------------------------\n");
	}
	return COUP_ERR;
    }
    else if(coupRep->err == ERR_OK)
    {
	if(DEBUG)
	{
	    printf("[DEBUG] client_attendCoup\n");
	    printf("[DEBUG] Réponse reçue :\n");
	}

	switch(coupRep->validCoup)
	{
	    case VALID :
		if(DEBUG) { printf("[DEBUG] Coup valide\n"); } 
		break;	
	    case TIMEOUT :
		if(DEBUG) { printf("[DEBUG] Timeout\n"); } 
		return COUP_TIMEOUT; 
		break;
	    case TRICHE :
		if(DEBUG) { printf("[DEBUG] Tentative de tricherie\n"); } 
		return COUP_INVALIDE;
		break;	
	}

	if(DEBUG) { printf("------------------------------------\n"); }
    }
    else
    {
	if(DEBUG)
	{
	    printf("[DEBUG] client_attendCoup \n");
	    printf("[DEBUG] Réponse de l'arbitre reçue :\n");
	    printf("[DEBUG] Une erreur s'est produite.\n");
	    client_perror(coupRep->err);
	    printf("------------------------------------\n");
	}
	return COUP_ERR;
    }

    // Reception du coup de l'adversaire
    err = recv(sockArbitre,coupReq,sizeof(TypCoupReq),0);
    if(err < 0)
    {
	if(DEBUG)
	{
	    printf("[DEBUG] client_attendCoup\n");
	    printf("[DEBUG] Erreur lors de la réception du coup de l'adversaire\n");
	    printf("------------------------------------\n");
	}
	return COUP_ERR;
    }

    return COUP_OK;
}
