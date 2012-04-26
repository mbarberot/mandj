#include "menu.h"


/**
 * Affichage du menu. Demande saisie à l'utilisateur et effectue l'action demandée
 */
void afficheMenu()
{
    int choix = -1;
    int choixOk = 0;

    char fileLoaded[ENTREE_MAX_LENGTH];

    while(1)
    {
	printf("\n----- GRAPHES -----\n");
	printf("1 : chargement. Charge un fichier de commande et produit des fichiers résultats.\n");
	printf("2 : recharger. Recharge le fichier courant.\n");
	printf("3 : sauvegarder. Sauvegarde le fichier courant à un autre emplacement\n");
	printf("4 : nettoyer. Nettoie les fichiers résultats produits par le fichier courant\n");
	printf("5 : quitter.\n");

	printf("\nEntrez votre choix :\n");

	choixOk = scanf("%d", &choix);

	if(choixOk == 1 )
	{
	    switch(choix)
	    {
		case CHARGER_FICHIER:
		    actionChargerFichier(fileLoaded); 
		    break;
		case RECHARGER:
		    actionRecharger(fileLoaded);
		    break;
		case SAUVEGARDER:
		    actionSauvegarder(fileLoaded);
		    break;
		case NETTOYER:
		    actionNettoyer(fileLoaded);
		    break;
		case QUITTER:
		    actionQuitter();
		    break;
		default:
		    printf("\nAction non valide \n");
	    }
	}
	else
	{
	    printf("\nAction non valide \n");
	}

	// Nettoyage du flux
	int c;
	while ( ((c = getchar()) != '\n') && c != EOF);


    }

}

/**
 * Quitte le programme et appelle le nettoyage de la mémoire
 */
void actionQuitter(){
    printf("Bye ! \n");
    // Nettoyage des graphes
    suppressionGraphe(1);
    suppressionGraphe(2);
    // Quitter
    exit(0);  
}

/**
 * Propose a l'utilisateur de charger un fichier de commandes
 */
void actionChargerFichier(char *fileLoaded){

    parserError err;
    int saisie;

    printf("\nIndiquez le chemin du fichier à traiter : \n");
    saisie = scanf("%s", fileLoaded);

    if(saisie)
    {

	// Démarrer traitement
	err = chargerFichier(fileLoaded);

	// Récupération de l'erreur, et action en conséquent
	switch(err)
	{
	    case TRAITEMENT_FICHIER_OK:
		printf("Le fichier de commandes a été correctement traité ! \n");
		break;	
	    case FICHIER_COMMANDES_INEXISTANT:
		printf("Le fichier de commandes n'existe pas. \n");
		break;	
	    case CREATION_RESULTAT_IMPOSSIBLE:
		printf("Creation du fichier résultat impossible. \n");
		break;
	    default:
		printf("Erreur : %s\n", parserErrorToString(err));
	}
    }
    else
    {
	printf("\nErreur de saisie \n");
	fileLoaded = NULL;
    }
}

void actionRecharger(char *fileLoaded)
{
    if(fileLoaded != NULL)
    {
	// Démarrer traitement
	parserError err = chargerFichier(fileLoaded);
	
	// Récupération de l'erreur, et action en conséquent
	switch(err)
	{         
	    case TRAITEMENT_FICHIER_OK:
		printf("Le fichier de commandes a été correctement traité ! \n");
		break;
	    case FICHIER_COMMANDES_INEXISTANT:
		printf("Le fichier de commandes n'existe pas. \n");
		break;
	    case CREATION_RESULTAT_IMPOSSIBLE:
		printf("Creation du fichier résultat impossible. \n");
		break;
	    default: 
		printf("Erreur : %s\n", parserErrorToString(err));
	} 
    }
    else
    {
	actionChargerFichier(fileLoaded);
    }
}

void actionNettoyer(char *fileLoaded)
{
    char cmd[512] = "rm ";

    if(fileLoaded != NULL)
    {
	strcat(cmd,fileLoaded);
	strcat(cmd,"_*");
	int err = system(cmd);
	if(err < 0) 
	{ 
	    printf("Erreur : Nettoyage impossible\n"); 
	}
	else 
	{ 
	    printf("Le nettoyage a été effectué avec succès !\n"); 
	}
    }
}


void actionSauvegarder(char *fileLoaded)
{
    int saisie;
    char newFile[ENTREE_MAX_LENGTH];

    printf("Indiquez le chemin du fichier de sauvegarde\n");
    saisie = scanf("%s",newFile);

    if(saisie)
    {
	FILE *fold = fopen(fileLoaded,"r");
	FILE *fnew = fopen(newFile,"w");

	char buffer;
	if(fold != NULL && fnew != NULL)
	{
	    while((buffer = fgetc(fold)) != EOF)
	    {
		fputc(buffer,fnew);
	    }
	    fputc(EOF,fnew);
	    printf("Sauvegarde effectuée!\n");

	}
	else
	{
	    printf("Erreur lors de l'ouverture des fichiers\n");
	}

	if(fold != NULL) fclose(fold);
	if(fnew != NULL) fclose(fnew);

    }
    else
    {
	printf("Erreur de saisie!\n");
    }
}
