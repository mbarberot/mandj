#ifndef _GRAPHE_
#define _GRAPHE_


/*
 * Les erreurs
 */
#define SOMMET_INVALIDE 		1
#define POIDS_INVALIDE 			2
#define MAX_SOMMET_INVALIDE		3
#define DEGRE_INVALIDE			4
#define NUMERO_GRAPHE_INVALIDE		5
#define NUMERO_GRAPHE_TROP_PETIT	6
#define GRAPHE_INEXISTANT		7
#define SOMMET_INEXISTANT		8
#define ARETE_INEXISTANTE		9
#define GRAPHE_DEJA_EXISTANT		10
#define SOMMET_DEJA_EXISTANT		11
#define ARETE_DEJA_EXISTANTE		12
#define MAX_SOMMET_DIFFERENT		13
#define SOMMET_DIFFERENT		14
#define ARETE_DIFFERENTE		15
#define DEGRE_DIFFERENT			16
#define TEST_OK				17
#define PROBLEME_MEMOIRE		18
#define COMMANDE_INVALIDE		19
#define RES_OK				20


/*
 * Fonctions
 */

choisirGraphe(int idGraphe);		// Todo
chargerGraph();				// Todo, Arg?
creation(int maxSommet);		// Todo
modifierNbMaxSommet(int maxSommet);	// Todo
suppressionGraphe(int idGraphe);	// Todo
insertionSommet(int nvSommet);		// Todo
suppressionSommet(int sommet);		// Todo
insertionArete(int sommetDep,
	       int poids,
	       int sommetArr,
	       char oriente);		// Todo
modifierPoids(int sommetDep,
	      int nvPoids,
	      int sommetArr,
	      char oriente);		// Todo
suppressionArete(int sommetDep,
		 int sommetArr,
		 char oriente);		// Todo
viderGraphe()				// Todo
viderAreteGraphe()			// Todo
testerArete(int idGraphe,
	    int sommetDep,
	    int poids,
	    int sommetArr,
	    char oriente,
	    int resAttendu);		// Todo
testerSommet(int idGraphe,
	     int X,Res);		// Todo
testerDegreSommet(int idGraphe,
		  int sommet,
		  int value,
		  int resAttendu);	// Todo
compareGraphe(int resAttendu);		// Todo
compareSommet(int sommet,
	      int resAttendu);		// Todo


#endif