/*
 * Projet MOIA - SCS
 * 2011 - 2012
 * 
 * Modélisation du jeu
 * 
 * Barberot Mathieu &
 * Racenet Joan
 *
 */

#define DEBUG 1

#include "jeu.h"


jeu_err jeu_init()
{
    int i,j;

    // Initialisation du plateau
    // Toutes les cases sont vides
    for(i = 0; i < 4; i++)
    {
	for(j = 0; j < 4; j++)
	{
	    plateau[(i*4)+j] = VIDE ;
	}
    }

    // Initialisation des joueurs
    joueur = (Joueur*) malloc(sizeof(Joueur));
    adversaire = (Joueur*) malloc(sizeof(Joueur));

    // Contrôle de l'allocation de la mémoire
    if(joueur == NULL || adversaire == NULL)
    {
	if(DEBUG) 
	{ 
	    printf("[DEBUG] jeu_init \n");
	    printf("[DEBUG] Allocation mémoire impossible\n");
	    printf("-------------------------------------\n");
	}
	return JEU_ERR;
    }
    else
    {
	joueur->blanc = 8;
	joueur->rouge = 4;
	joueur->jaune = 4;
    
	adversaire->blanc = 8;
	adversaire->rouge = 4;
	adversaire->jaune = 4;
    }
    
    // Affichage DEBUG
    if(DEBUG)
    {
	printf("[DEBUG] jeu_init\n");
	jeu_afficheJeu();
    }

    return JEU_OK;
}



jeu_err jeu_reset()
{
    jeu_err err;

    // Fin de la partie
    err = jeu_fin();
    if(err == JEU_OK)
    {
	// Nouvelle initialisation
	err = jeu_init();
    }

    return err;
}



jeu_err jeu_fin()
{
    free(joueur);
    free(adversaire);
    return JEU_OK;
}


jeu_err jeu_getCase(TypPosition position, TypPiece *piece)
{
    if(piece == NULL)
    {
	if(DEBUG) 
	{ 
	    printf("[DEBUG] jeu_getCase \n");
	    printf("[DEBUG] Argument *piece == NULL\n");
	    printf("----------------------------------------\n");
	}
	return JEU_ERR;
    }

    *piece = plateau[(position.ligne * 4) + position.colonne];
    
    return JEU_OK;
}



jeu_err jeu_setCase(TypPosition position, TypPiece piece)
{
    plateau[(position.ligne * 4) + position.colonne] = piece;

    return JEU_OK;
}




jeu_err jeu_ajouterCoup(TypCoupReq coup, TypBooleen adv)
{
    TypPosition pos = coup.caseArrivee;
    TypPiece piece= coup.typePiece;
    
    Joueur *j = (adv)?adversaire:joueur;

    switch(piece)
    {
	case BLANC :
	    j->blanc--;
	    break;
	case ROUGE : 
	    j->rouge--;
	    break;
	case JAUNE : 
	    j->jaune--;
	    break;
	default:
	    break;
    }

    return jeu_setCase(pos,piece);
}



jeu_err jeu_afficheJeu()
{
    jeu_err err ;

    printf("\n");
    err = jeu_afficheJoueur(*joueur,FAUX);
    if(err == JEU_OK) 
    {
	printf("\n");
	jeu_affichePlateau();
	printf("\n");
	err = jeu_afficheJoueur(*adversaire,VRAI);
	printf("\n");
    }

    return err;
}


jeu_err jeu_afficheJoueur(Joueur j, TypBooleen adv)
{
    int i;

    printf("%s\n",((adv)?"Adversaire":"Joueur"));
    
    for(i = 0; i < j.blanc; i++)
    {
	printf("B ");
    }
    printf("\n");

    for(i = 0; i < j.rouge; i++)
    {
	printf("R ");
    }
    printf("\n");

    for(i = 0; i < j.jaune; i++)
    {
	printf("J ");
    }
    printf("\n");
   
    return JEU_OK;
}


void jeu_affichePlateau()
{
    int i,j;

    char p[4] = { ' ', 'B', 'R', 'J' };

    printf("---------------------\n");

    for(i = -1; i < 4; i++)
    {
	for(j = -1; j < 4; j++)
	{
	    //Affichage légende
	    if(i < 0)
	    {
		// Case top-left
		if(j < 0)	{ printf("| X "); }
		else if(j == 3)	{ printf("| %d |",j+1); }
		else		{ printf("| %d ",j+1); }
	    }
	    else if(j < 0)  { printf("| %d ",i+1); }
	    else if(j == 3) { printf("| %c |",p[plateau[(i*4)+j]]); }
	    else	    { printf("| %c ",p[plateau[(i*4)+j]]); }
	}
	printf("\n");
	printf("---------------------\n");
    }    
}
