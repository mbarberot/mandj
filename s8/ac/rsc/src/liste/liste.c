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
 * @return 1 si succès, -1 sinon, 0 si le voisin est deja present dans la liste
 */
int ajouteVoisin(TypVoisins** l, int numVoisin, int poidsVoisin, void* info){
    TypVoisins* new;
    
    if(l != NULL ){
	if(voisinExiste(l,numVoisin) == NULL){
	    if((new = (TypVoisins*)malloc(sizeof(TypVoisins))) != NULL){
		new->voisin = numVoisin;
		new->poidsVoisin = poidsVoisin;
		new->info = info;
		new->voisinSuivant = *l;
		*l = new;      
		return 1;
	    }else{
		return -1;
	    }
	}else{
	    return 0;
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
	if(voisinExiste(l,numVoisin) == NULL ){	    
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
 * Change le poids de l'arête l -> numVoisin
 * @param l :la liste contenant le voisin à modifier
 * @param numVoisin : le voisin à modifier
 * @param nPoids : le nouveau poids de l'arête

 * @return 1 si succès, -1 sinon
 */
int modifiePoidsVoisin(TypVoisins** l, int numVoisin, int nPoids)
{
    TypVoisins* toModify = voisinExiste(l,numVoisin);
    
    if(toModify != NULL)
    {
	toModify->poidsVoisin = nPoids;	
	return 1;
    }
    else
    {
	return -1;
    }
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
 * @return le voisin s'il est trouvé, NULL sinon
 */
TypVoisins* voisinExiste(TypVoisins** l , int numVoisin){
    TypVoisins* tmp = *l;
    
    while(tmp != NULL){
	if(tmp->voisin == numVoisin){
	    return tmp;
	}
	tmp = tmp->voisinSuivant;
    }
    
    return NULL;
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
  
  /**
   * Renvoie la taille de la liste (nb d'éléments)
   * @param l : la liste dont on veut connaître la taille
   * @return la taille calculée
   */
  int tailleListe(TypVoisins** l)
  {
      int res = 0;
      TypVoisins *tmp = *l;
      
      while(tmp != NULL)
      {
	  res++;
	  tmp = tmp->voisinSuivant;
      }
      return res;
  }

  /**
   * Compare deux listes (=> les deux listes ont les mêmes éléments)
   * @param l1 : une liste
   * @param l2 : une autre liste
   * @return 1 si égales, 0 sinon
   */
int compareListes(TypVoisins** l1, TypVoisins** l2)
{
    int res = 1;
    TypVoisins *tmp1 = *l1;
    TypVoisins *tmp2 = *l2;
    
    while(tmp1 != NULL && tmp2 != NULL)
    {
	if(tmp1->voisin != tmp2-> voisin || tmp1->poidsVoisin != tmp2->poidsVoisin)
	    return 0;
	
	tmp1 = tmp1->voisinSuivant;
	tmp2 = tmp2->voisinSuivant;
    }
    
    if(tmp1!= NULL || tmp2 != NULL)
	res = 0;
    
    return res;
}
  

