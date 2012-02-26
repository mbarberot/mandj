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
  
  // Initialisation de quelques sommets 1,3,7 et 8
  insertionSommet(1);
  insertionSommet(3);
  insertionSommet(7);
  insertionSommet(8);
  
  
  // Quelques arêtes
  // arete 1->3
  err = insertionArete(1,10,3,'o');
  printf("Creation arete 1 3 %d : %s \n", err, errToString(err));
  // arete 3-> 1
  err = insertionArete(3,12,1,'o');
  printf("Creation arete 3 1 %d : %s \n", err, errToString(err));
  
  // arete 3->3
  err = insertionArete(3,0,3,'n');
  printf("Creation arete 3 3 %d : %s \n", err, errToString(err));
  // arete 3->7
  err = insertionArete(3,15,7,'o');
  printf("Creation arete 3 7 %d : %s \n", err, errToString(err));
  // arete 7<->8
  err = insertionArete(7,10,8,'n');
  printf("Creation arete 7 8 %d : %s \n", err, errToString(err));
  
  afficheGraphe(2);
  
  // Modification du poids des arêtes 3->3 et 7<->8
  err = modifierPoids(3, 42, 3,'n');
  printf("Modifier poids arete 3 3 %d : %s \n", err, errToString(err));
  err = modifierPoids(7,42,8,'n');
  printf("Modifier poids arete 7 8 %d : %s \n", err, errToString(err));
  
  afficheGraphe(2);
  
  // Tests sur les arêtes
  err = testerArete(2,7,42,8,'n',1);
  printf("Existence arete 7 8 %d : %s \n", err, errToString(err));
  
  // Tests sur les sommets
  err = testerSommet(2, 3, 0);
  printf("Existence sommet 3 %d : %s \n", err, errToString(err));
  
  // Suppression du graphe
  err = suppressionGraphe(2);
  printf("Suppression graphe %d : %s \n", err, errToString(err));

}

