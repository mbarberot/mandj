#include "menu.h"


/**
* Affichage du menu. Demande saisie à l'utilisateur et effectue l'action demandée
*/
void afficheMenu()
{
  int choix = -1;
  int choixOk = 0;
  
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
	  actionChargerFichier(); 
	  break;
	case RECHARGER:
	  actionRecharger();
	  break;
	case SAUVEGARDER:
	  actionSauvegarder();
	  break;
	case NETTOYER:
	  actionNettoyer();
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
void actionChargerFichier(){
  parserError err;
  char path[ENTREE_MAX_LENGTH];
  int saisie;
  
  printf("\nIndiquez le chemin du fichier à traiter : \n");
  saisie = scanf("%s", path);
  
  if(saisie)
  {
    // Démarrer traitement
    err = chargerFichier(path);
    
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
  } 
  
  afficheGraphe(0);
  afficheGraphe(1);
  
}

void actionRecharger(){}
void actionNettoyer(){}
void actionSauvegarder(){}