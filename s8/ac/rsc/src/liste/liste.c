#include <stdlib.h>
#include <stdio.h>
#include "liste.h"

/* Structure de liste simplement chainée
typedef struct voisins {
  int voisin; 
  int poidsVoisin; 
  void* info;
  struct voisins* voisinSuivant; 
  }TypVoisins;
*/

// Initialise la liste avec ajout d'un marqueur de fin
void initialiseListe(TypVoisins* l){
  if(l == NULL){
    l = (TypVoisins*)malloc(sizeof(TypVoisins));
    l->voisin = -1;
    l->poidsVoisin = 0;
    l->info = NULL;
    l->voisinSuivant = NULL;
  }
}

// Ajoute dans la liste l un nouveau voisin avec les infos passées en paramètres
void ajouteVoisin(TypVoisins* l, int numVoisin, int poidsVoisin, void* info){
  TypVoisins* new;
  
  if(l != NULL){
    new = (TypVoisins*)malloc(sizeof(TypVoisins));
    new->voisin = numVoisin;
    new->poidsVoisin = poidsVoisin;
    new->info = info;
    new->voisinSuivant = l;
  }
}

// Supprime dans la liste le voisin désigné par numVoisin
void supprimeVoisin(TypVoisins* l, int numVoisin){
  TypVoisins* tmp = l;
  
  // On place le pointeur temporaire sur le voisin précédant celui qu'on veut supprimer
  while(tmp != NULL && tmp->voisinSuivant->voisin != numVoisin)
    tmp = tmp-> voisinSuivant;
  
  tmp->voisinSuivant = tmp->voisinSuivant->voisinSuivant;
  free(tmp->voisinSuivant);
}


// Affichage de la liste
void afficheVoisins(TypVoisins* l){
  TypVoisins* tmp = l;
  
  while(tmp != NULL){
    printf("[Voisin = %d | PoidsVoisin = %d | ]->", l->voisin, l->poidsVoisin);
    tmp = tmp -> voisinSuivant;
  }  
  printf("[= \n");  
}
