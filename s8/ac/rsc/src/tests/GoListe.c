
#include <stdlib.h>
#include <stdio.h>


#include "liste.h"
#include "graphe.h"

main(int argc, char **argv){
  
  
  TypVoisins* liste;
  
  // Initialisation
  initialiseListe(&liste);
  
  // Affichage
  afficheVoisins(&liste);
  
}