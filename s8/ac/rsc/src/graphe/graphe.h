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
	RES_OK,
	TEST_KO
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
 * Fonctions
 */

erreur choisirGraphe(int idGraphe);
erreur creation(int maxSommet);
erreur modifierNbMaxSommet(int maxSommet);
erreur suppressionGraphe(int idGraphe);		// Todo
erreur insertionSommet(int nvSommet);		// Todo
erreur suppressionSommet(int sommet);		// Todo
erreur insertionArete(
		int sommetDep,
		int poids,
		int sommetArr,
		char oriente
		);				// Todo
erreur modifierPoids(
		int sommetDep,
		int nvPoids,
		int sommetArr,
		char oriente
		);				// Todo
erreur suppressionArete(
		int sommetDep,
		int sommetArr,
		char oriente
		);				// Todo
erreur viderGraphe();				// Todo
erreur viderAreteGraphe();			// Todo
erreur testerArete(
		int idGraphe,
		int sommetDep,
		int poids,
		int sommetArr,
		char oriente,
		int resAttendu
		);				// Todo
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
void afficheGraphe(int idGraphe);

#endif
