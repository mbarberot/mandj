#ifndef _LISTE_
#define _LISTE_

#include <stdlib.h>
#include <stdio.h>

/* Structure de liste simplement chainée */
typedef struct voisins {
  int voisin; /* Le numéro du voisin */
  int poidsVoisin; /* Pondération de l'arête entre le sommet en cours et le voisin */
  void* info; /* Autres informations */
  struct voisins* voisinSuivant; 
}TypVoisins;


int initialiseListe(TypVoisins** l);
int ajouteVoisin(TypVoisins** l, int numVoisin, int poidsVoisin, void* info);
int supprimeVoisin(TypVoisins** l, int numVoisin);
int modifiePoidsVoisin(TypVoisins** l, int numVoisin, int nPoids);
void afficheVoisins(TypVoisins** l);
TypVoisins* rechercheVoisin(TypVoisins** l, int numVoisin);
void supprimeListe(TypVoisins** l);
int tailleListe(TypVoisins**l);
int compareListes(TypVoisins** l1, TypVoisins** l2);
int ajouteVoisinNonTries(TypVoisins** l, int numVoisin, int poidsVoisin, void* info);
void concateneListe(TypVoisins** l1, TypVoisins* l2);
int minPoids(TypVoisins** l, int voisin);
TypVoisins* cloneListe(TypVoisins** l);

#endif

