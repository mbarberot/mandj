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

    
    jeu_init();


    //
    // En travaux
    // 

    TypCoupReq *coup = (TypCoupReq*) malloc(sizeof(TypCoupReq));
    coup->idRequest = COUP;
    coup->numeroDuCoup = 0;

    ia_calculeCoup(coup); 
    jeu_ajouterCoup(*coup);

    jeu_affichePlateau();
    
    return 0;
}
