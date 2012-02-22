#include <stdlib.h>
#include <stdio.h>
#include "liste.h"

/* Structure de liste simplement chainée
 * typedef struct voisins {
 *  int voisin; 
 *  int poidsVoisin; 
 *  void* info;
 *  struct voisins* voisinSuivant; 
 * }TypVoisins;
 */

// Initialise la liste avec ajout d'un marqueur de fin
int initialiseListe(TypVoisins** l){
    
    TypVoisins* new;
    
    if((new = malloc(sizeof(TypVoisins))) != NULL) {
	new->voisin = -1;
	new->poidsVoisin = 0;
	new->info = NULL;
	new->voisinSuivant = NULL;    
	*l = new;
    }else{
	perror("Impossible d'initialiser la liste \n");
	return -1;
    }
    return 1;
}

// Ajoute dans la liste l un nouveau voisin avec les infos passées en paramètres
int ajouteVoisin(TypVoisins** l, int numVoisin, int poidsVoisin, void* info){
    TypVoisins* new;
    
    if(l != NULL ){
	if(voisinExiste(l,numVoisin) != 1){
	    if((new = (TypVoisins*)malloc(sizeof(TypVoisins))) != NULL){
		new->voisin = numVoisin;
		new->poidsVoisin = poidsVoisin;
		new->info = info;
		new->voisinSuivant = *l;
		*l = new;      
		return 1;
	    }else{
		perror("Erreur a l'initialisation du nouveau voisin");
	    }
	}else{
	    perror("Le voisin est deja present dans la liste");
	}
    }
    
    return -1;
}

// Supprime dans la liste le voisin désigné par numVoisin
int supprimeVoisin(TypVoisins** l, int numVoisin){
    TypVoisins* tmp = *l;
    TypVoisins* prec;
    
    if(l != NULL){
	if(voisinExiste(l,numVoisin) != 1 ){
	    perror("Le voisin a supprimer n'existe pas");
	    return -1;
	}else{
	    // Le maillon à supprimer est le premier 
	    if(tmp->voisin == numVoisin){
		*l = tmp->voisinSuivant;
		free(tmp);
	    }else{	    
		while(tmp != NULL && tmp->voisin != numVoisin){
		    prec = tmp;
		    tmp = tmp-> voisinSuivant;
		}	    
		prec->voisinSuivant = tmp -> voisinSuivant;
		free(tmp);    
	    }
	}
    }
    return 1;
}

// Affichage de la liste
void afficheVoisins(TypVoisins** l){
    TypVoisins* tmp = *l;
    
    while(tmp != NULL){
	printf("[Voisin = %d | PoidsVoisin = %d | ]->", tmp->voisin, tmp->poidsVoisin);
	tmp = tmp -> voisinSuivant;
    }  
    printf("[= \n");  
}

// Vérifie l'existence du voisin numVoisin dans la liste
int voisinExiste(TypVoisins** l , int numVoisin){
    TypVoisins* tmp = *l;
    
    while(tmp != NULL){
	if(tmp->voisin == numVoisin)
	    return 1;
	tmp = tmp->voisinSuivant;
    }
    
    return -1;
}

// Libération de la liste en mémoire
void supprimeListe(TypVoisins** l){
  TypVoisins* next;
  TypVoisins* tmp = *l;
  
  while(tmp != NULL){
    next = tmp->voisinSuivant;
    free(tmp);
    tmp = next;
  }
  
  free(*l);
}
