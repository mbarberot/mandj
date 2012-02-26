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
#include <stdlib.h>
#include <string.h>

#include "liste.h"

#define NB_GRAPHES 2
/*
 * Les erreurs
 */
typedef enum 
{       
	SOMMET_INVALIDE = 1,
	POIDS_INVALIDE = 2,
	MAX_SOMMET_INVALIDE = 3,
	DEGRE_INVALIDE = 4,
	NUMERO_GRAPHE_INVALIDE = 5,
	NUMERO_GRAPHE_TROP_PETIT = 6,
	GRAPHE_INEXISTANT = 7,
	SOMMET_INEXISTANT = 8,
	ARETE_INEXISTANTE = 9,
	GRAPHE_DEJA_EXISTANT = 10,
	SOMMET_DEJA_EXISTANT = 11,
	ARETE_DEJA_EXISTANTE = 12,
	MAX_SOMMET_DIFFERENT = 13,
	SOMMET_DIFFERENT = 14,
	ARETE_DIFFERENTE = 15,
	DEGRE_DIFFERENT = 16,
	TEST_OK = 17,
	PROBLEME_MEMOIRE = 18,
	COMMANDE_INVALIDE = 19,
	RES_OK = 20,
	TEST_KO = 21
} erreur ;


/*
 * La structure de graphe :
 *      int nbSommets   - Nombre de sommets du graphe
 *      int[] aretes    - Ensemble des aretes
 *
 *
 */
typedef struct TypGraphe
{
	int nbMaxSommets;
	TypVoisins **aretes; 
} TypGraphe ;


/*
 * Le graphe courant : 1 ou 2
 */
int grapheCourant;

/*
 * Les deux graphes à manipuler
 */
TypGraphe *graphes[NB_GRAPHES]; 

/*
 * 	Fonctions utilitaires
 */	
char* errToString(erreur err);
void afficheGraphe(int idGraphe);

/*
 * Fonctions de manipulation
 */

erreur choisirGraphe(int idGraphe);
erreur creation(int maxSommet);
erreur modifierNbMaxSommet(int maxSommet);
erreur suppressionGraphe(int idGraphe);		
erreur insertionSommet(int nvSommet);		
erreur suppressionSommet(int sommet);		
erreur insertionArete(
		int sommetDep,
		int poids,
		int sommetArr,
		char oriente
		);				
erreur modifierPoids(
		int sommetDep,
		int nvPoids,
		int sommetArr,
		char oriente
		);				
erreur suppressionArete(
		int sommetDep,
		int sommetArr,
		char oriente
		);				
erreur viderGraphe();				
erreur viderAreteGraphe();			
erreur testerArete(
		int idGraphe,
		int sommetDep,
		int poids,
		int sommetArr,
		char oriente,
		int resAttendu
		);				
erreur testerSommet(
		int idGraphe,
		int sommet,
		int resAttendu
		);				// Todo
erreur testerDegreSommet(
		int idGraphe,
                int sommet,
		int value,
		int resAttendu
		);				// Todo
erreur compareGraphe(int resAttendu);		// Todo
erreur compareSommet(
		int sommet,
		int resAttendu
		);				// Todo


#endif
