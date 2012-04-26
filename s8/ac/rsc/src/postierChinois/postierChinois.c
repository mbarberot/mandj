/**
 * Algorithmique Combinatoire
 * Mars - Avril - Mai 2012
 *
 * Barberot Mathieu & Joan Racenet
 *
 * Implémentation du fichier d'entete
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>

#include "postierChinois.h"


erreur initParcoursChinois(int idGraphe, char *res_path)
{
    erreur err;
    
    // idGraphe valide ?
    if(idGraphe < 0 || idGraphe > 2) { return NUMERO_GRAPHE_INVALIDE; }
    
    // Quels graphes ?
    if(idGraphe == 0)
    {
	// Les deux graphes
	
	// Existent-ils ?
	if(graphes[0] == NULL || graphes[1] == NULL) { return GRAPHE_INEXISTANT; }
	
	// Peuplement de nbAretes
	if((err = peupleTabAretes(1)) != RES_OK) { return err; }
	if((err = peupleTabAretes(2)) != RES_OK) { return err; }
    }
    else
    {
	// Un seul
	
	// Existe-t-il ?
	if(graphes[idGraphe-1] == NULL) { return GRAPHE_INEXISTANT; }
	
	// Peuplement de nbAretes
	if((err = peupleTabAretes(idGraphe)) != RES_OK) { return err; }
    }
    
    /* Préparation du fichier résultat */
    resPostier = NULL;
    
    char file_name[320] = "";    
    strcat(file_name, res_path);
    strcat(file_name, "_ResPostier.txt");
        
    resPostier = fopen(file_name, "w+");

    return RES_OK;
}

erreur peupleTabAretes(int idGraphe)
{
    int i,j,	    // Itérateurs
    nbSom,	    // Nombre de sommet du graphe
    **tab;
    
    // Récupération 
    //	    - du graphe
    //	    - du nombre de sommets
    TypGraphe *g = graphes[idGraphe - 1];
    nbSom = g->nbMaxSommets;
    
    // Allocation de la mémoire pour les tableaux
    nbAretes[idGraphe - 1] = (int**) malloc(nbSom*sizeof(int));
    
    // Utilisation du pointeur tab pour plus de clareté
    // et vérification de l'allocation
    tab = nbAretes[idGraphe - 1];
    if(tab == NULL) { return PROBLEME_MEMOIRE; }

    
    // Peuplement du tableau :
    //	    - parcours du tableau d'arêtes du graphe
    for(i = 0; i < nbSom; i++)
    {
	// Initialisation de la matrice d'adjacence
	tab[i] = (int*)malloc(nbSom*sizeof(int));
	
	if(tab[i] == NULL)
	    return PROBLEME_MEMOIRE;

	
	// Initialisation des valeurs : 1 si une arête existe entre i et j, 0 sinon
	    for(j = 0 ; j < nbSom ; j++)
	    {	      
		tab[i][j] = (rechercheVoisin(&g->aretes[i], j+1) != NULL)? 1 : 0;
	    }
    }    

    return RES_OK;
}

erreur freeParcoursChinois()
{
    int i,j,	    // Iterateurs
    nbSom;	    // Nombre de sommets
    
    TypGraphe *g;   // Les graphes
    
    
    // Pour chaque graphe
    for(i = 0; i < 2; i++)
    {
	// Si le graphe existe
	if(graphes[i] != NULL)
	{
	    // Récupération 
	    //	    - du graphe
	    //	    - du nombre de sommets
	    g = graphes[i];
	    nbSom = g->nbMaxSommets;
	    
	    // Pour chaque sommet
	    for(j = 0; j < nbSom; j++)
	    {		
		free(nbAretes[i][j]);
	    }
	    
	    // On libere le 'graphe' i
	    free(nbAretes[i]);
	    
	}
    }
    /* Fermeture du fichier */
    fclose(resPostier);
    return RES_OK;
}

erreur dupliqueArete(int idGraphe, int s1, int s2)
{
    TypVoisins *p, *q;
    TypGraphe *g;
    
    // Récupérer le graphe courant
    g = graphes[idGraphe - 1];
    
    // Est-il valide ?
    if(g == NULL) { return GRAPHE_INEXISTANT; }
    
    // Les sommets s1 et s2 existent-ils ?
    if(g->aretes[s1-1] == NULL || g->aretes[s2-1] == NULL) { return SOMMET_INEXISTANT; }
    
    // Y-a-t-il un arêtes entre les deux
    p = rechercheVoisin(&(g->aretes[s1-1]),s2);
    q = rechercheVoisin(&(g->aretes[s2-1]),s1);
    if(p == NULL || q == NULL) { return ARETE_INEXISTANTE; }
    
    // Dupliquer l'arête
    nbAretes[idGraphe - 1][s1-1][s2-1]++;
    nbAretes[idGraphe - 1][s2-1][s1-1]++;
    
    return RES_OK;
}

erreur supprimeArete(int idGraphe, int s1, int s2)
{
    TypVoisins *p, *q;
    TypGraphe *g;
    
    
    // Récupérer le graphe courant
    g = graphes[idGraphe - 1];
    
    // Est-il valide ?
    if(g == NULL) { return GRAPHE_INEXISTANT; }
    
    // Les sommets s1 et s2 existent-ils ?
    if(g->aretes[s1-1] == NULL || g->aretes[s2-1] == NULL) { return SOMMET_INEXISTANT; }
    
    // Y-a-t-il un arêtes entre les deux
    p = rechercheVoisin(&(g->aretes[s1-1]),s2);
    q = rechercheVoisin(&(g->aretes[s2-1]),s1);
    
    if(p == NULL || q == NULL) { return ARETE_INEXISTANTE; }

    // Supprimer l'arête
    nbAretes[idGraphe - 1][s1-1][s2-1]--;
    nbAretes[idGraphe - 1][s2-1][s1-1]--;
    
    return RES_OK;
}


erreur isGrapheEulerien(int idGraphe, int* res)
{
    TypGraphe *g = graphes[idGraphe - 1];
    int pariteCurrent = 0;
    int sommetsImpairs = 0;
    int i;
    
    *res = -1;
    
    
    if(g == NULL)
	return GRAPHE_INEXISTANT;
    
    // Tester la parité des degrés de chaque sommet
	for(i = 0 ; i < g -> nbMaxSommets ; i++)
	{
	    calculerDegreSommet(idGraphe, i + 1, &pariteCurrent);
	    
	    // => l'implémentation oblige de figurer les arêtes non-orientées comme deux arêtes orientées
	    // on divise donc le degré par deux pour avoir une valeur correcte
	    pariteCurrent = pariteCurrent / 2;
	    
	    if(pariteCurrent % 2 == 1)
		sommetsImpairs++;
	}  
	// -> SI tous pairs => on peut effectuer un cycle eulérien
	if(sommetsImpairs == 0)
	{
	    *res = 1;
	    
	}  // -> SI on a un nombre pair de sommets impairs
	else if (sommetsImpairs % 2 == 0)
	{
	    *res = 0;
	} // SINON : le graphe n'est pas eulérien
	else
	{
	    return TEST_KO;
	}
	
	return TEST_OK;
	
}

TypVoisins* cycleEulerien(int idGraphe, int x)
{
    
    TypVoisins* res = NULL;
    int current, voisin;
    int nbVoisins;
    
    
    // Vérifier si s_current est isole
    if (getNbVoisinsAccessibles(idGraphe, x) == 0)
    {
	return res;
    }
    else
    {
	
	// Ajout du sommet courant dans la liste res
	ajouteVoisinNonTries(&res, x, 0, NULL);  
	
	current = x;
	while((nbVoisins = getNbVoisinsAccessibles(idGraphe, current)) != 0)
	{
	    voisin = -1;
	    // Choisir z tq z soit un voisin de current
	    // => on parcourt les arêtes, et on prend la première existante
	    
	    int i = 0;
	    while(i < graphes[idGraphe - 1] -> nbMaxSommets && voisin == -1)
	    {
		if(nbAretes[idGraphe - 1][current -1][i] > 0)
		{
		    voisin = i + 1;
		}
		i++;
	    }
	    //On supprime l'arête y <-> z
	    supprimeArete(idGraphe, current, voisin);
	    current = voisin;
	    
	    // Ajout de y dans le résultat
	    ajouteVoisinNonTries(&res, current, 0, NULL);
	    
	}
    }
    
    
    /* Appeler cycle eulérien sur tous les sommets de la liste résultat*/
    
    TypVoisins *tmp = res;
    TypVoisins *nRes = NULL;
    
    while(tmp != NULL)
    {
	TypVoisins *lTmp = NULL;
	lTmp = cycleEulerien(idGraphe, tmp -> voisin);
	
	// Concaténer la liste temporaire avec la liste résultat
	if(tailleListe(&lTmp) > 0) 
	{
	    concateneListe(&nRes, lTmp);
	    supprimeListe(&lTmp);
	}
	else
	{
	    ajouteVoisinNonTries(&nRes, tmp->voisin, tmp->poidsVoisin, tmp->info);
	}
	
	tmp = tmp -> voisinSuivant;
    }
     
     supprimeListe(&res);
     supprimeListe(&tmp);     
     
    /* Retourner la concaténation de tous les résultats*/
    return nRes;
}


erreur calculCycleEulerien(int idGraphe,int idHeuristique, char *res_path)
{
    erreur res = RES_OK;
    int *tparc = (int*)malloc(sizeof(int));
        
    /* On teste si le graphe est eulérien ou pas */
    if(tparc != NULL) {    
	res = isGrapheEulerien(idGraphe, tparc);
    } 
    else
    {
	return PROBLEME_MEMOIRE;
    }
    
    /* Un cycle chinois est possible */
    if(*tparc == 0)
    {
	initParcoursChinois(idGraphe, res_path);
	goCycleChinois(idGraphe, idHeuristique, res_path);
	freeParcoursChinois();
    }
    else
    {
	goCycleChinois(idGraphe, -1, res_path);
    }   
    
    
    /* Libération mémoire */
    free(tparc);
    
    return res;  
}


void goCycleChinois(int idGraphe, int idHeuristique, char* res_path)
{    
    /* Liste dans laquelle le parcours sera mémorisé */    
    TypVoisins *parc = NULL;
    
    /* Allocation mémoire du parcours chinois (si besoin est) */
    if(idHeuristique != -1)
    {	
    
	switch(idHeuristique)
	{
	    /* Toutes les heuristiques */
	    case 0:
		goCycleChinois(idGraphe, 0, res_path);		
		break;
	    /* Couplage optimal */    
	    case 1 :
		doCouplageOptimal(idGraphe);		
		break;
	    case 2 :
	    //doFirstCouplage(idGraphe);
	    break;
	    case 3 :
	    //doRandomCouplage(idGraphe);
	    break;
	    default:
		printf("Cette heuristique n'existe pas ! \n"); return;
		
	}
	
	/* Quoiqu'il arrive : on écrit le résultat du couplage */
	ecrireFichierDot(idGraphe,idHeuristique, res_path);	
    }
    /* On peut effectuer le cycle eulérien */
    parc = cycleEulerien(idGraphe, 1);

    
    /* Ecriture du resultat */
    ecrireFichierRes(parc,idGraphe, idHeuristique);

    /* Libération mémoire de la liste resultat */
    supprimeListe(&parc);	
    
}


int getNbVoisinsAccessibles(int idGraphe, int sommet)
{
    int nbVoisins = 0 ;
    int i, max;
    
    max = graphes[idGraphe - 1]->nbMaxSommets;
    
    for(i = 0; i < max ; i++)
    {
	if(nbAretes[idGraphe - 1][sommet - 1][i] > 0 )
	{
	    nbVoisins ++;
	}
    }
    
    return nbVoisins;
}

void plusCourtChemin(int idGraphe, int *m[], int *p[])
{
    TypGraphe *g = graphes[idGraphe - 1];
    int nbSom = g -> nbMaxSommets;
    
    /* itérateurs */
    int x, y, z;
    
    /* Initialisation */
    for(x = 0 ; x < nbSom ; x++)
    {
	for(y = 0 ; y < nbSom ; y++)
	{
	    TypVoisins *tmp = rechercheVoisin(&g -> aretes[x], y + 1);
	    
	    if(x == y || tmp == NULL)
	    {
		m[x][y] = 0;
	    }
	    else
	    {
		m[x][y] = tmp -> poidsVoisin;
	    }
	    
	    p[x][y] = x + 1;
	}
    }
    
    /* Boucle de calcul */
    for(z = 0 ; z < nbSom ; z++)
    {
	for(x = 0; x < nbSom ; x++)
	{
	    for(y = 0 ; y < nbSom; y++)
	    {
		if(m[x][z] != 0 && m[z][y] != 0 && x != y) 
		{
		    if(m[x][z] + m[z][y] < m[x][y] || m[x][y] == 0)
		    {
			m[x][y] = m[x][z]+ m[z][y];
			p[x][y] = p[z][y];
		    }
		}
	    }	    
	}
    }
}


void sommetsImpairs(int idGraphe, TypVoisins **res)
{
    TypGraphe *g = graphes[idGraphe - 1];
    int i;
    int pariteCurrent = 0;
    
    for(i = 0 ; i < g ->nbMaxSommets ; i++)
    {
	calculerDegreSommet(idGraphe, i + 1, &pariteCurrent);
	if((pariteCurrent / 2) % 2 == 1)
	{
	    ajouteVoisin(res, i + 1, 0, NULL);

	}
    }
    
}


void listeCouplage(int idGraphe, TypVoisins *ls, TypVoisins **res)
{
    if(ls == NULL)
	return ;
        
    /* La liste étant triée, le minimum est le premier élément*/
    TypVoisins *x = ls;
    TypVoisins *y = x -> voisinSuivant;
    
    /* Pour tous les autres éléments de la liste dont le numéro de sommet est plus grand que min */
    while(y != NULL)
    {
	 /* Ajout de {x,y} dans la liste de résultat */
	ajouteVoisinNonTries(res, x -> voisin, x -> poidsVoisin, NULL);
	ajouteVoisinNonTries(res, y -> voisin, y -> poidsVoisin, NULL);
	    
	/* Appel de la fonction sur la liste privée de {x,y} */
	TypVoisins *nls = NULL;
	cloneListe(&ls, &nls);
	supprimeVoisin(&nls, x -> voisin);
	supprimeVoisin(&nls, y -> voisin);	
	listeCouplage(idGraphe, nls, res);
	
	supprimeListe(&nls);
	
	y = y -> voisinSuivant;
    }
    
    
}


void doCouplageOptimal(int idGraphe)
{
  
  // Récupérer la liste des sommets impairs
  TypVoisins *s_impairs = NULL; 
  sommetsImpairs(idGraphe, &s_impairs);
  
  // Liste utilisée pour stocker le couplage en cours
  TypVoisins *c_couple = NULL;
  
  // Liste utilisée pour stocker le meilleur couplage
  TypVoisins *b_couple = NULL;
  
  // Calcul des plus courts chemins
  int size = graphes[idGraphe - 1] -> nbMaxSommets;
  int** m;
  int** p;
  int i;
  
  /* Initialisation des matrices */
  m = (int**)malloc(size * sizeof(int*));
  p = (int**)malloc(size * sizeof(int*));
  
  for(i = 0 ; i < size ; i++)
  {
      m[i] = (int*)malloc(size * sizeof(int));
      p[i] = (int*)malloc(size * sizeof(int));
  }
  
  /* Constitution de la matrice des plus courts chemins*/
  plusCourtChemin(idGraphe, m, p);  
  
  /* On calcule la liste des couplages possibles */
  listeCouplage(idGraphe, s_impairs, &c_couple);
  
  /* Recherche du couplage de poids minimal */
  b_couple = calculeCoupleOptimal(c_couple, m);
  
  /* Duplication des aretes */
  duplicationCouplage(idGraphe, b_couple, p);
  
  /* Libération de la mémoire */
  for(i= 0 ; i < size ; i++)
  {
      free(m[i]); free(p[i]);
  }
  free(m); 
  free(p);
  supprimeListe(&s_impairs);
  supprimeListe(&c_couple);
  supprimeListe(&b_couple);
}


TypVoisins* calculeCoupleOptimal(TypVoisins *couples, int* m[])
{
    /* Couplage résultat */
    TypVoisins *res = NULL;
    
    /* Couplage en cours */
    TypVoisins *current;
    
    /* Récupération du numéro de sommet minimal */
    int min = couples-> voisin;
    
    /* Itérateur */
    TypVoisins* tmp = couples;
    
    /* Variables pour stocker les évaluations */
    int tmp_coupl = -1 ; int best_coupl = -1;
    
    while(tmp != NULL)
    {
	
	/* On tombe sur un début de couplage */
	if(tmp -> voisin == min)
	{
	    current = NULL;
	    ajouteVoisinNonTries(&current, tmp -> voisin, tmp -> poidsVoisin , NULL);
	    tmp = tmp -> voisinSuivant;
	}
	else
	{
	    /* On rajoute les autres sommets du couplage en cours */
	    while((tmp != NULL) && (tmp -> voisin != min))
	    {
		ajouteVoisinNonTries(&current, tmp -> voisin, tmp -> poidsVoisin, NULL);
		tmp = tmp -> voisinSuivant;		

	    }
	    
	    /* Evaluation du couplage */
	    if(best_coupl == -1)
	    {
		/* Evaluation du premier couplage */
		best_coupl = evalueCouplage(current, m);
		cloneListe(&current, &res);

	    }
	    else
	    {
		/* Evaluation du couplage en cours */
		tmp_coupl = evalueCouplage(current, m);
		
		/* Comparaison avec le dernier meilleur couplage */
		if(tmp_coupl < best_coupl)
		{
		    best_coupl = tmp_coupl;
		    supprimeListe(&res);
		    res = NULL;
		    cloneListe(&current, &res);
		}
	    }
	    supprimeListe(&current);
	}
	
    }    
    return res;
}


int evalueCouplage(TypVoisins *couples, int *m[])
{
    int res = 0;
    int som1, som2;
    
    TypVoisins *tmp = couples;

    while(tmp != NULL)
    {
	// Récupération du premier sommet
	som1 = tmp -> voisin;	
	tmp = tmp -> voisinSuivant;
	
	// Récupération du second sommet
	som2 = tmp -> voisin;
		
	// Evaluation
	res = res + m[som1 - 1][som2 - 1];
	
	tmp = tmp -> voisinSuivant;

    }
    return res;
}

void duplicationCouplage(int idGraphe, TypVoisins *couples, int* p[])
{
    TypVoisins *tmp = couples;
    int som1, som2;
    int last_p, next_p;

    
    while(tmp != NULL)
    {
	/* Récupération du premier sommet */
	som1 = tmp -> voisin;
	tmp = tmp -> voisinSuivant;
	
	/* Récupération du deuxième sommet */
	som2 = tmp -> voisin;
	
	/* on duplique toutes les arêtes du chemin entre som1 et som2 */
	last_p = som2;
	next_p = som1;
	
	while(p[next_p - 1][last_p - 1] != som1)
	{
	    dupliqueArete(idGraphe, p[next_p -1][last_p - 1], last_p );
	    last_p = p[next_p - 1][last_p - 1];
	    
	}
	
	dupliqueArete(idGraphe, p[next_p -1][last_p - 1], last_p );
	
	/* On passe au couple suivant */
	tmp = tmp -> voisinSuivant;
    }
}

/*
 * Fonctions de gestion des fichiers résultats
 * Ecriture res :
 *	H(idHeuristique) = S1 --(Distance)--> S2 --> ...
 * 
 */
void ecrireFichierRes(TypVoisins* res, int idGraphe, int idHeuristique)
{
    /* 
     * On calcule la taille de la chaine de resultat
     */
    char *aretes = NULL; aretes = aretesToString(res, idGraphe);
    char *res_str = malloc((strlen(aretes) + 10) * sizeof(char)); 
    
    sprintf(res_str, "H(%d) = ", idHeuristique);
    strcat(res_str, aretes);
    
    
    /*
     * Récupération du fichier et écriture
     */
    if(resPostier != NULL)
	fprintf(resPostier, "%s\n", res_str);
    
    /* Libération mémoire */
    free(res_str);
    free(aretes);
}


char* aretesToString(TypVoisins* aretes, int idGraphe)
{
    int nb_elts = tailleListe(&aretes);
    int size_res = 25 * nb_elts;
    char *res = malloc(size_res * sizeof(*res));
    char *buffer;
    
    TypGraphe *g = graphes[idGraphe - 1];
    TypVoisins *tmp = aretes;
    
    while(tmp != NULL)
    {
	
	if(tmp -> voisinSuivant != NULL)
	{
	    TypVoisins *current = rechercheVoisin(&g -> aretes[tmp -> voisin - 1], tmp -> voisinSuivant -> voisin);
	    buffer = malloc(18 * sizeof(char));
	    sprintf(buffer, "%d --(%d)--> ", tmp -> voisin, current -> poidsVoisin);
	}
	else
	{
	    buffer = malloc(5 * sizeof(char));
	    sprintf(buffer, "%d\n", tmp -> voisin);
	}
	
	strcat(res, buffer);
	free(buffer);
	
	tmp = tmp -> voisinSuivant;
    }
    
    return res;
    
}


void ecrireFichierDot(int idGraphe, int idHeuristique, char* res_path)
{
    // Le graphe à exporter en dot
    TypGraphe *g = graphes[grapheCourant];
    
    // Les arêtes dupliquées
    int **arr = nbAretes[idGraphe - 1];
 
    // Modèle nom fichier : Path_Graphe_Heuristique.dot
    char filename[320] = "";
    sprintf(filename, "%s_%d_%d.dot", res_path, idGraphe, idHeuristique);

    // Ouverture du fichier
    FILE *fp;
    fp = fopen(filename,"w");
    
    // Cas d'erreur
    if(fp == NULL)
    {
	printf("Impossible d'effectuer l'exportation\n");
    }
    else
    {
	if(g == NULL)
	{
	    printf("Impossible d'afficher le graphe");
	}
	else
	{
	    // Début
	    fprintf(fp,"digraph G {\n\tedge [ arrowtail=dot, arrowhead=open ];\n");

	    // Les sommets
	    int nbSom = g->nbMaxSommets;
	    int i;	
	    for(i = 0; i < nbSom; i++) 
	    {
		if(g->aretes[i] != NULL)
		{
		    fprintf(fp,"\tA%d\n",i+1);
		}
	    }

	    // Les arètes
	    // Pour chaque liste du tableau
	    //	Pour chaque element de la liste
	    //		Ecrire AX -> AY [label="S"]
	    //			avec X : l'indice du tableau
	    //			     Y : la valeur de l'élément
	    //			     S : le label de l'élément
	    TypVoisins *l;
	    int nb_arr;
	    
	    for(i = 0; i < nbSom; i++)
	    {
		l = g->aretes[i];
		while(l != NULL)
		{		    
		    if(l->voisin >= 0)		    
		    {
			
			for(nb_arr = 0; nb_arr < arr[i][l->voisin - 1] ; nb_arr++)
			{
			    fprintf(fp,"\tA%d -> A%d [label=\"%d\"]\n",i+1,l->voisin,l->poidsVoisin);
			}
		    }
		    l = l->voisinSuivant;
		}
	    }

	    // Fin
	    fprintf(fp,"}\n");
	}
    }
    fclose(fp);
}