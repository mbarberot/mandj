
#include <stdlib.h>
#include <stdio.h>


#include "liste.h"
#include "graphe.h"

main(int argc, char **argv){
  
  
  TypVoisins* liste;
  
  // Initialisation
  initialiseListe(&liste);
  // Ajouts de voisins
  ajouteVoisin(&liste, 1, 0, NULL);
  ajouteVoisin(&liste, 2, 0, NULL);
  ajouteVoisin(&liste, 3, 0, NULL);


  // Affichage
  afficheVoisins(&liste);
  printf("Nb éléments : %d ", sizeof(liste) / sizeof(TypVoisins*));
  // Suppression
  supprimeListe(&liste);
  
}