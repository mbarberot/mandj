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
    
    TypVoisins *p,
    *q,
    *new;
    
    
    if(l != NULL)
    {
	// pas de doublon
	/*
	if(rechercheVoisin(l,numVoisin) != NULL)
	{
	    return -1;
	}*/
	
	// cas du premier élément
	// --> il faut modifier l
	if(numVoisin < (*l)->voisin || (*l)->voisin == -1)
	{
	    new = (TypVoisins*) malloc(sizeof(TypVoisins));
	    if(new == NULL)
	    {
		return -1;
	    }
	    else
	    {
		new->voisin = numVoisin;
		new->poidsVoisin = poidsVoisin;
		new->info = info;
		new->voisinSuivant = *l;
		*l = new;
		return 1;
	    }
	}
	
	// cas général (milieu / fin de liste)
	// --> il faut modifier les éléments de la liste
	// p pointe l'élément 'courant'
	// q pointe le précédent
	// le nouvel élément sera placé entre p et q ( q -> new -> p )
	q = *l;
	p = (*l)->voisinSuivant;
	while(p != NULL) 
	{	
	    if(numVoisin < p->voisin || p->voisin == -1)
	    {
		new = (TypVoisins*) malloc(sizeof(TypVoisins));
		if(new != NULL)
		{
		    new->voisin = numVoisin;
		    new->poidsVoisin = poidsVoisin;
		    new->info = info;
		    new->voisinSuivant = p;
		    q->voisinSuivant = new;				
		    return 1;
		}
		else
		{
		    return -1;
		}
	    }
	    q = p;
	    p = p->voisinSuivant;
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
	if(rechercheVoisin(l,numVoisin) == NULL ){	    
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
    TypVoisins* toModify = rechercheVoisin(l,numVoisin);
    
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
 * Recherche un élément, dont l'id est spécifié, dans la liste donnée
 *
 * @param l		La liste
 * @param numVoisin	L'id du voisin à trouver
 * @return		Le voisin s'il est trouvé ou NULL sinon
 */
TypVoisins* rechercheVoisin(TypVoisins** l, int numVoisin)
{
    TypVoisins *tmp;
    
    if(l != NULL)
    {
	tmp = *l;
	while(tmp != NULL)
	{
	    if(tmp->voisin == numVoisin)
		return tmp;
	    tmp = tmp->voisinSuivant;
	}
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

/**
 * Ajout d'un élément dans une liste sans respecter le tri
 * @return 1 si succès, 0 sinon
 */
int ajouteVoisinNonTries(TypVoisins** l, int numVoisin, int poidsVoisin, void* info)
{
    // Le petit nouveau
    TypVoisins *toAdd = (TypVoisins*) malloc(sizeof(TypVoisins));
    
    if(toAdd != NULL)
    {
	toAdd -> voisin = numVoisin;
	toAdd -> poidsVoisin = poidsVoisin;
	toAdd -> info = info;
	toAdd->voisinSuivant = NULL;
	
	// Premier élément
	if(*l == NULL)
	{  
	    *l = toAdd;
	}
	else // On ajoute à la fin
	{
	    TypVoisins *tmp = *l;
	
	    while(tmp->voisinSuivant != NULL)
	    {
		tmp = tmp ->voisinSuivant;
	    }
	    
	    tmp -> voisinSuivant = toAdd;
	    
	    }
    }
    else
    {
	return 0;
    }
    
    return 1;
}

/**
 * Ajoute l2 à la fin de l1
 */
void concateneListe(TypVoisins** l1, TypVoisins* l2)
{
    TypVoisins *tmp = l2;
    while(tmp != NULL)
    {
	ajouteVoisinNonTries(l1, tmp -> voisin, tmp -> poidsVoisin, tmp -> info);
	tmp = tmp -> voisinSuivant;
    }
}

/**
 * Compte le nombre de fois où voisin apparaît dans l
 */
int compteOccurences(TypVoisins** l, int voisin)
{
    int res = 0;
    TypVoisins *tmp = l;
    
    while(tmp != NULL)
    {
	if(tmp->voisin == voisin)
	{
	    res ++;
	}
	tmp = tmp -> voisinSuivant;
    }
    
    return res;
}

/**
 * Renvoie une liste chainée de doublons
 */
TypVoisins* getDoublons(TypVoisins** l, int voisin)
{
    TypVoisins *res = NULL;
    TypVoisins *tmp = l;
    
    while(tmp != NULL)
    {
	if(tmp -> voisin == voisin)
	{
	    ajouteVoisinNonTries(&res, tmp -> voisin, tmp -> poidsVoisin, tmp -> info);
	}
	tmp = tmp -> voisinSuivant;
    }
    
    return res;
}

/**
 * Renvoie le sommet de poids minimal d'une liste
 */
int minPoids(TypVoisins** l, int voisin)
{
    TypVoisins *tmp = l;
    int min = tmp -> poidsVoisin;
    
    while(tmp != NULL)
    {
	if(tmp -> poidsVoisin < min)
	{
	    min = tmp -> poidsVoisin;
	}
	
	tmp = tmp -> voisinSuivant;
    }
    
    return min;
}

