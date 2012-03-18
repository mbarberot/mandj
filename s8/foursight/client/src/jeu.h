#ifndef __JEUSCSMOIA__
#define __JEUSCSMOIA__

/*
 * Projet MOIA - SCS
 * 2011 - 2012
 *
 * Modélisation du jeu
 * Fichier d'entete
 *
 * Barberot Mathieu &
 * Raceenet Joan
 *
 */

#include <stdlib.h>
#include <stdio.h>

// Fichier du protocole
//  -> pour les structures et énumérations
#include "protocole.h"


typedef enum {
    JEU_ERR,
    JEU_OK
} jeu_err;


/**
 * Structure d'un joueur :
 *
 * Nombre de pions blancs
 * Nombre de pions rouges
 * Nombre de pions jaunes
 *
 */
typedef struct {
    int blanc;
    int rouge;
    int jaune;
} Joueur ;


//
// Variables globales :
//


/**
 * Plateau de jeu.
 *
 * Le plateau est unidimensionnel.
 * Utiliser getCase(col,lig) pour atteindre les cases plus simplement
 *
 */
TypPiece plateau[16];


Joueur *joueur;
Joueur *adversaire;

//
// Fonctions :
//


/**
 * Initialise le plateau et crée les joueurs
 *
 * @return JEU_OK   L'intialisation s'est bien passée
 * @return JEU_ERR  Echec de l'initialisation
 */
jeu_err jeu_init();

/**
 * Réinitialise le jeu
 *
 * @return JEU_OK   L'intialisation s'est bien passée
 * @return JEU_ERR  Echec de l'initialisation
 */
jeu_err jeu_reset();

/**
 * Libère la mémoire allouée dans l'initialisation
 *
 * @return JEU_OK   L'intialisation s'est bien passée
 * @return JEU_ERR  Echec de l'initialisation
 */
jeu_err jeu_fin();

/**
 * Renvoie le contenu d'une case du tableau
 *
 * L'accès au case est effectué grâce à la fonction (ligne * nbColonne) + colonne
 *
 * @param position  Structure représentant une case
 * @param piece	    Piece sur la case
 * @return JEU_OK   L'intialisation s'est bien passée
 * @return JEU_ERR  Echec de l'initialisation
 */
jeu_err jeu_getCase(TypPosition position, TypPiece *piece);


/**
 * Renvoie le contenu d'une case du tableau
 *
 * L'accès au case est effectué grâce à la fonction (ligne * nbColonne) + colonne
 *
 * @param position  Structure représentant une case
 * @param piece	    Piece sur la case
 * @return JEU_OK   L'intialisation s'est bien passée
 * @return JEU_ERR  Echec de l'initialisation
 */
jeu_err jeu_setCase(TypPosition position, TypPiece piece);


/**
 * Ajoute le coup dans le plateau
 *
 * @param coup	    Le coup à ajouter
 * @param moi	    VRAI si c'est mon coup, FAUX si c'est celui de l'adversaire
 * @return JEU_OK   L'intialisation s'est bien passée
 * @return JEU_ERR  Echec de l'initialisation
 */
jeu_err jeu_ajouterCoup(TypCoupReq coup, TypBooleen moi);

/**
 * Affichage du jeu
 *
 * @return JEU_OK   L'intialisation s'est bien passée
 * @return JEU_ERR  Echec de l'initialisation
 */
jeu_err jeu_afficherJeu();

/**
 * Affiche le plateau
 *
 * @return JEU_OK   L'intialisation s'est bien passée
 * @return JEU_ERR  Echec de l'initialisation
 */
void jeu_afficherPlateau();

/**
 * Affiche un joueur
 *
 * @param j	    Le joueur
 * @param adv	    VRAI si 'j' est l'adversaire, FAUX sinon
 * @return JEU_OK   L'intialisation s'est bien passée
 * @return JEU_ERR  Echec de l'initialisation
 */
jeu_err jeu_afficherJoueur(Joueur j, TypBooleen adv);


#endif
