#ifndef _LISTE_
#define _LISTE_

#include <stdlib.h>
#include <stdio.h>

/* Structure de liste simplement chainée */
typedef struct voisins {
  int voisin; /* Le numéro du voisin */
  int poidsVoisin; /* Pondération de l'arête entre le sommet en cours et le voisin */
  void* info;
  struct voisins* voisinSuivant; 
}TypVoisins;



int initialiseListe(TypVoisins** l);
int ajouteVoisin(TypVoisins** l, int numVoisin, int poidsVoisin, void* info);
int supprimeVoisin(TypVoisins** l, int numVoisin);
void afficheVoisins(TypVoisins** l);
int voisinExiste(TypVoisins** l, int numVoisin);

#endif

