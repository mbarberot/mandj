#include <stdlib.h>
#include <stdio.h>

#include "liste.h"

/**
 * Initialise la liste avec ajout d'un marqueur de fin
 * @param l : la liste à initialiser
 * @return 1 si succès, -1 sinon
 * */
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

/**
 * Ajoute dans la liste l un nouveau voisin avec les infos passées en paramètres
 * @param l : la liste dans laquelle on veut rajouter un élément
 * @param numVoisin : l'identifiant du voisin
 * @param poidsVoisin : la pondération du voisin
 * @param info : des infos complémentaires
 * @return 1 si succès, -1 sinon
 */
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

/**
 * Supprime dans la liste le voisin désigné par numVoisin
 * @param l : la liste dans laquelle on veut supprimer un élément
 * @param numVoisin : l'identifiant du voisin que l'on veut supprimer
 * @return 1 si succès, -1 sinon.
 * */
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

/**
 * Affichage de la liste dans la console
 * @param l : la liste à afficher
 */
void afficheVoisins(TypVoisins** l){
    TypVoisins* tmp = *l;
    
    while(tmp != NULL){
	printf("[Voisin = %d | PoidsVoisin = %d | ]->", tmp->voisin, tmp->poidsVoisin);
	tmp = tmp -> voisinSuivant;
    }  
    printf("[= \n");  
}

/**
 * Vérifie l'existence du voisin numVoisin dans la liste
 * @param l : la liste a traiter
 * @param numVoisin : l'id du voisin dont on veut vérifier l'existence
 * @return 1 si le voisin est présent, -1 sinon
 */
int voisinExiste(TypVoisins** l , int numVoisin){
    TypVoisins* tmp = *l;
    
    while(tmp != NULL){
	if(tmp->voisin == numVoisin){
	    return 1;
	}
	tmp = tmp->voisinSuivant;
    }
    
    return -1;
}

/**
 *  Désallocation de la mémoire occupée par la liste
 * @param l : la liste à supprimer
 */
void supprimeListe(TypVoisins** l){
  TypVoisins* next;
  TypVoisins* tmp = *l;
  
  while(tmp != NULL){
    next = tmp->voisinSuivant;
    free(tmp);
    tmp = next;
  }
  
}
