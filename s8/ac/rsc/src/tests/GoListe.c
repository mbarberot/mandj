
#include <stdlib.h>
#include <stdio.h>


#include "liste.h"
#include "graphe.h"

main(int argc, char **argv){
  
  
  TypVoisins* liste = NULL;
  ajouteVoisinNonTries(&liste, 8, 0, NULL);
  
  // Affichage
  afficheVoisins(&liste);

  // Suppression
  supprimeListe(&liste);
  
}
