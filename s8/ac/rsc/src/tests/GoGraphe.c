#include <stdlib.h>
#include <stdio.h>


#include "graphe.h"

/**
 * Fichier de test des fonctions de manipulation de graphe
 * 
 */

main(int argc, char **argv){
    
  graphes[0] = NULL;
  graphes[1] = NULL;
    
  // Definir le graphe courant
  grapheCourant = 0;
  // Création de ce graphe
  creation(5);
  // initialise des sommets
  insertionSommet(1);
  insertionSommet(5);
  // Affichage du graphe
  afficheGraphe(grapheCourant);
  // Modification du nb max de sommets
  modifierNbMaxSommet(6);
  afficheGraphe(grapheCourant);
  
  suppressionGraphe(grapheCourant);


}