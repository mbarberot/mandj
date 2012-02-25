#include <stdlib.h>
#include <stdio.h>


#include "graphe.h"

/**
 * Fichier de test des fonctions de manipulation de graphe
 * 
 */

main(int argc, char **argv){
  erreur err;  
  graphes[0] = NULL;
  graphes[1] = NULL;
    
  // Definir le graphe courant
  err = choisirGraphe(2);
  printf("Choix d'un graphe %d : %s \n", err, errToString(err));
  
  // Création de ce graphe
  err = creation(10);
  printf("Creation du graphe %d : %s \n ",err, errToString(err) );
  

  // Suppression du graphe
  err = suppressionGraphe(2);
  printf("Suppression graphe %d : %s \n", err, errToString(err));


}