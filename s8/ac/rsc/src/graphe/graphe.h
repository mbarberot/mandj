/*
 * Librairie de gestion de graphes
 * Fichier d'entête 
 *
 *
 * AC 2011-2012
 * Barberot Mathieu et Joan Racenet
 * Master 1 Informatique
 */


#ifndef _GRAPHE_
#define _GRAPHE_

/*
 * Librairie de gestion des listes
 */
#include "liste.h"

/*
 * Les erreurs
 */
typedef enum 
{       
	SOMMET_INVALIDE,
	POIDS_INVALIDE,
	MAX_SOMMET_INVALIDE,
	DEGRE_INVALIDE,
	NUMERO_GRAPHE_INVALIDE,
	NUMERO_GRAPHE_TROP_PETIT,
	GRAPHE_INEXISTANT,
	SOMMET_INEXISTANT,
	ARETE_INEXISTANTE,
	GRAPHE_DEJA_EXISTANT,
	SOMMET_DEJA_EXISTANT,
	ARETE_DEJA_EXISTANTE,
	MAX_SOMMET_DIFFERENT,
	SOMMET_DIFFERENT,
	ARETE_DIFFERENTE,
	DEGRE_DIFFERENT,
	TEST_OK,
	PROBLEME_MEMOIRE,
	COMMANDE_INVALIDE,
	RES_OK
} error ;

/*
 * La structure de graphe :
 *
 *      int id          - ID du graphe (1 ou 2)
 *      int nbSommets   - Nombre de sommets du graphe
 *      int[] aretes    - Ensemble des aretes
 *
 *
 */
typedef struct 
{
	int nbMaxSommets;
	TypVoisins aretes[]; 
} TypGraphe ;



int grapheCourant;

/*
 * Fonctions
 */

void choisirGraphe(int idGraphe);	// Todo
void chargerGraph();			// Todo, Arg?
void creation(int maxSommet);		// Todo
void modifierNbMaxSommet(int maxSommet);// Todo
void suppressionGraphe(int idGraphe);	// Todo
void insertionSommet(int nvSommet);	// Todo
void suppressionSommet(int sommet);	// Todo
void insertionArete(
		int sommetDep,
		int poids,
		int sommetArr,
		char oriente
		);			// Todo
void modifierPoids(
		int sommetDep,
		int nvPoids,
		int sommetArr,
		char oriente
		);			// Todo
void suppressionArete(
		int sommetDep,
		int sommetArr,
		char oriente
		);			// Todo
void viderGraphe();			// Todo
void viderAreteGraphe();		// Todo
void testerArete(
		int idGraphe,
		int sommetDep,
		int poids,
		int sommetArr,
		char oriente,
		int resAttendu
		);			// Todo
void testerSommet(
		int idGraphe,
		int sommet,
		int resAttendu
		);			// Todo
void testerDegreSommet(
		int idGraphe,
                int sommet,
		int value,
		int resAttendu
		);			// Todo
void compareGraphe(int resAttendu);	// Todo
void compareSommet(
		int sommet,
		int resAttendu
		);			// Todo

#endif
