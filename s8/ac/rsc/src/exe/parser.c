#include "parser.h"

/**
 * Affichage des erreurs de parser
 * @param err : le code erreur à afficher
 * @return : l'affichage de l'erreur
 */
char* parserErrorToString(parserError err)
{
    switch(err)
    {
	case TRAITEMENT_FICHIER_OK:
	    return "TRAITEMENT_FICHIER_OK";
	case FICHIER_COMMANDES_INEXISTANT:
	    return "FICHIER_COMMANDES_INEXISTANT";
	case ARGUMENTS_INCORRECTS:
	    return "ARGUMENTS_INCORRECTS";
	case INDICE_RETOUR_INEXISTANT:
	    return "INDICE_RETOUR_INEXISTANT";
	case TRAITEMENT_CMD_OK:
	    return "TRAITEMENT_CMD_OK";
	default:
	    return "";
    }
}

/**
 * Traite le fichier de commande dont le chemin est passé en paramètre
 * - Recupère le contenu du fichier 
 * - Traitement des commandes récupérées
 * 
 * @param path : le chemin du fichier de commande
 * @return TRAITEMENT_FICHIER_OK : fichier de commande correctement traité
 * @return FICHIER_COMMANDES_INEXISTANT : fichier de commande indiqué inexistant
 */
parserError chargerFichier(char* path)
{
    /* Préparation de l'ouverture du fichier de commandes */
    entree = NULL;
    entree = fopen(path, "r"); 
    
    /* Recupération des données du fichier */
    stat(path, &entree_infos);
    
    if(entree != NULL)
    {
	/* On recupère dans une chaine les commandes*/
	int size = entree_infos.st_size;
	char cmd[size];
	lectureFichier(cmd);
	
	/* On peut alors fermer le fichier d'entree et ouvrir celui de résultat pour l'écriture*/
	fclose(entree);
	
	/* Et on lance l'interpretation*/
	interpreteCommande(cmd);
	
    }
    else
    {
	return FICHIER_COMMANDES_INEXISTANT;
    }
    
    return TRAITEMENT_FICHIER_OK;
}

/**
 * Lit le fichier passé en paramètre, et renvoie son contenu sous forme de chaine de caractères
 * Pour plus de lisibilité, la fonction enlève les commentaires éventuellement présents dans le fichier de commande
 * @param nom: le nom du fichier
 * @return le contenu du fichier sous forme d'une chaine de caractères
 */
void lectureFichier(char* content)
{        
    // Le caractere en cours
    int caractereActuel = 0;
    
    // Indice de parcours
    int i = 0;
    
    if(entree != NULL){
	// Initialisation du parcours
	caractereActuel = fgetc(entree);
	
	//Tant qu'on n'a pas atteint la fin du fichier
	while (caractereActuel != EOF)
	{  	    
	    // Si on trouve un début de commentaire : on passe à la ligne suivante
	    if(caractereActuel == '#')
	    {
		while(caractereActuel != '\n' && caractereActuel != EOF)
		{
		    caractereActuel = fgetc(entree);
		}
	    }	    
	    else if(isgraph(caractereActuel)) // Supprime les espaces au passage
	    {
		content[i] = caractereActuel;
		i++;		
		caractereActuel = fgetc(entree);		
	    }
	    else
	    {
		caractereActuel = fgetc(entree);
	    }
	    
	}	
	content[i] = '\0';
    }  
}

/**
 * Interprete les commandes écrites dans une chaine de caractères
 * @param commandes : la chaine de caractères contenant les commandes à interpréter
 */
parserError interpreteCommande(char* commandes)
{
    char *sep = {";"};
    
    // Imbrication de strtok impossible => utilisation de strtok_r, adaptée dans ce cas
    char *bck;
    char *ptr = strtok_r(commandes, sep, &bck);    
    char *tmp = NULL;
    
    while ( ptr != NULL ) {
	// On fait une copie de l'instruction pour pouvoir travailler dessus
	tmp = strdup(ptr);	
	
	// Permet de vérifier que tmp est une commande valide
	int cmdOk = 0;
	
	// Commande creation
	if(strstr(tmp,"creation") != NULL)
	{
	    interpreteCreation(tmp);
	    cmdOk = 1;
	}
	
	// Commande choixGraphe
	if(strstr(tmp,"choisirGraphe") != NULL)
	{
	    interpreteChoisirGraphe(tmp);
	    cmdOk = 1;
	}
	
	// Commande modifierNbMaxSommet
	if(strstr(tmp, "modifierNbMaxSommet") != NULL)
	{
	    interpreteModifierNbMaxSommet(tmp);
	    cmdOk = 1;
	}
	
	// Commande suppressionGraphe
	if(strstr(tmp, "suppressionGraphe") != NULL)
	{
	    interpreteSuppressionGraphe(tmp);
	    cmdOk = 1;
	}
	
	// Commande insertionSommet
	if(strstr(tmp, "insertionSommet") != NULL)
	{
	    interpreteInsertionSommet(tmp);
	    cmdOk = 1;
	}
	
	// Commande suppressionSommet
	if(strstr(tmp, "suppressionSommet") != NULL)
	{
	    interpreteSuppressionSommet(tmp);
	    cmdOk = 1;
	}
	
	// Commande insertionArete
	if(strstr(tmp, "insertionArete") != NULL)
	{
	    interpreteInsertionArete(tmp);
	    cmdOk = 1;
	}
	
	// Commande modifierPoids
	if(strstr(tmp, "modifierPoids") != NULL)
	{
	    interpreteModifierPoids(tmp);
	    cmdOk = 1;
	}
	
	// Commande suppressionArete
	if(strstr(tmp, "suppressionArete") != NULL)
	{
	    interpreteSuppressionArete(tmp);
	    cmdOk = 1;
	}
	// Commande viderGraphe
	if(strstr(tmp,"viderGraphe") != NULL)
	{
	    interpreteViderGraphe(tmp);
	    cmdOk = 1;
	}
	
	// Commande viderAreteGraphe
	if(strstr(tmp, "viderAreteGraphe") != NULL)
	{
	    interpreteViderAreteGraphe(tmp);
	    cmdOk = 1;
	}
	
	// Commande testerArete
	if(strstr(tmp,"testerArete") != NULL)
	{
	    interpreteTesterArete(tmp);
	    cmdOk = 1;
	}
	
	// Commande testerSommet
	if(strstr(tmp,"testerSommet") != NULL)
	{
	    interpreteTesterSommet(tmp);
	    cmdOk = 1;
	}
	
	// Commande testerDegreSommet
	if(strstr(tmp, "testerDegreSommet") != NULL)
	{
	    interpreteTesterDegreSommet(tmp);
	    cmdOk = 1;
	}
	
	// Commande CompareGraphe
	if(strstr(tmp, "compareGraphe") != NULL)
	{
	    interpreteCompareGraphe(tmp);
	    cmdOk = 1;
	}
	
	//Commande CompareSommet
	if(strstr(tmp, "compareSommet") != NULL)
	{
	    interpreteCompareSommet(tmp);
	    cmdOk = 1;
	}
	
	// Commande non interprétable
	if(!cmdOk)
	{
	    printf("%s : commande inconnue \n", tmp);
	}
	
	// Réinitialisation de la copie et passage à l'instruction suivante
	free(tmp);
	ptr = strtok_r(NULL,sep,&bck);
    }
}

/**
 * Interprete la commande "creation". Ecris le résultat obtenu dans le fichier res et génère le .dot
 * @param cmd : la chaine de caractere de la commande
 */
parserError interpreteCreation(char* cmd)
{
    int numCom;
    int arg1;
    erreur err;
    
    int nbArgs = sscanf(cmd, "%d:creation(%d)", &numCom, &arg1);
    
    if(nbArgs == 2)
    {
	err = creation(arg1);
    }
    else
    {
	return ARGUMENTS_INCORRECTS;
    }
    
    return TRAITEMENT_CMD_OK;
}

/**
 * Interprete la commande choisirGraphe(idGraphe)
 * @param cmd : la chaine de caractere de la commande
 */
parserError interpreteChoisirGraphe(char* cmd)
{
    int numCom;
    int arg1;
    erreur err;
    
    int nbArgs = sscanf(cmd, "%d:choisirGraphe(%d)", &numCom, &arg1);
    
    if(nbArgs == 2)
    {
	err = choisirGraphe(arg1);
    }
    else
    {
	return ARGUMENTS_INCORRECTS;
    }
    return TRAITEMENT_CMD_OK;
    
}
/**
 * Interprete la commande modifierNbMaxSommet(int nvMax)
 * @param cmd : la chaine de caractere de la commande
 */
parserError interpreteModifierNbMaxSommet(char* cmd)
{
    int numCom;
    int arg1;
    erreur err;
    
    int nbArgs = sscanf(cmd, "%d:modifierNbMaxSommet(%d)", &numCom, &arg1);
    
    if(nbArgs == 2)
    {
	err = modifierNbMaxSommet(arg1);
    }
    else
    {
	return ARGUMENTS_INCORRECTS;
    }
    return TRAITEMENT_CMD_OK;
    
}

/**
 * Interprete la commande suppressionGraphe(int idGraphe)
 * @param cmd : la chaine de caractere de la commande
 */
parserError interpreteSuppressionGraphe(char* cmd)
{
    int numCom;
    int arg1;
    erreur err;
    
    int nbArgs = sscanf(cmd, "%d:suppressionGraphe(%d)", &numCom, &arg1);
    
    if(nbArgs == 2)
    {
	err = suppressionGraphe(arg1);
    }
    else
    {
	return ARGUMENTS_INCORRECTS;
    }
    return TRAITEMENT_CMD_OK;
    
}

/**
 * Interprete la commande insertionSommet(int nvSommet)
 * @param cmd : la chaine de caractere de la commande
 */
parserError interpreteInsertionSommet(char* cmd)
{
    int numCom;
    int arg1;
    erreur err;
    
    int nbArgs = sscanf(cmd, "%d:insertionSommet(%d)", &numCom, &arg1);
    
    if(nbArgs == 2)
    {
	err = insertionSommet(arg1);
    }
    else
    {
	return ARGUMENTS_INCORRECTS;
    }
    
    return TRAITEMENT_CMD_OK;
    
}

/**
 * interprete la commande suppressionSommet(int sommet)
 * @param cmd : la chaine de caracteres de la commande
 */
parserError interpreteSuppressionSommet(char* cmd)
{
    int numCom;
    int arg1;
    erreur err;
    
    int nbArgs = sscanf(cmd, "%d:suppressionSommet(%d)", &numCom, &arg1);
    
    if(nbArgs == 2)
    {
	err = suppressionSommet(arg1);
    }
    else
    {
	return ARGUMENTS_INCORRECTS;
    }
    
    return TRAITEMENT_CMD_OK;
    
}

/**
 * Interprete la commande insertionArete(
 *		int sommetDep,
 *		int poids,
 *		int sommetArr,
 *		char oriente
 *		);
 * @param cmd : la chaine de caracteres de la commande
 */
parserError interpreteInsertionArete(char* cmd)
{
    int numCom;
    int arg1, arg2, arg3;
    char arg4;
    erreur err;
    
    int nbArgs = sscanf(cmd, "%d:insertionArete(%d,%d,%d,%c)", &numCom, &arg1, &arg2, &arg3, &arg4);
    
    if(nbArgs == 5)
    {
	err = insertionArete(arg1,arg2,arg3,arg4);
    }
    else
    {
	return ARGUMENTS_INCORRECTS;
    }
    
    return TRAITEMENT_CMD_OK;
    
}

/**
 * Interprete la commande modifierPoids(int sommetDep,
 *		int nvPoids,
 *		int sommetArr,
 *		char oriente
 *		);
 * @param cmd : la chaine de caracteres de la commande
 */
parserError interpreteModifierPoids(char* cmd)
{
    int numCom;
    int arg1, arg2, arg3;
    char arg4;
    erreur err;
    
    int nbArgs = sscanf(cmd, "%d:modifierPoids(%d,%d,%d,%c)", &numCom, &arg1, &arg2, &arg3, &arg4);
    
    if(nbArgs == 5)
    {
	err = modifierPoids(arg1,arg2,arg3,arg4);
    }
    else
    {
	return ARGUMENTS_INCORRECTS;
    }
    
    return TRAITEMENT_CMD_OK;
    
}
/**
 * Interprete la commande suppressionArete(int sommetDep,
 *		int sommetArr,
 *		char oriente)
 * @param cmd : la chaine de caracteres de la commande		
 */
parserError interpreteSuppressionArete(char* cmd)
{
    int numCom;
    int arg1, arg2;
    char arg3;
    erreur err;
    
    int nbArgs = sscanf(cmd, "%d:suppressionArete(%d,%d,%c)", &numCom, &arg1, &arg2, &arg3);
    
    if(nbArgs == 4)
    {
	err = suppressionArete(arg1,arg2,arg3);
    }
    else
    {
	return ARGUMENTS_INCORRECTS;
    } 
    
    return TRAITEMENT_CMD_OK;
    
}

/**
 * Interprete la commande viderGraphe()
 * @param cmd : la chaine de caracteres de la commande
 */
parserError interpreteViderGraphe(char* cmd)
{
    erreur err;
    int numCom;
    
    int nbArgs = sscanf(cmd, "%d:viderGraphe()", &numCom);
    
    if(nbArgs == 1)
    {
	err = viderGraphe();
    }
    else
    {
	return ARGUMENTS_INCORRECTS;
    }
    
    return TRAITEMENT_CMD_OK;
    
    
}

/**
 * Interprete la commande viderAreteGraphe()
 * @param cmd : la chaine de caracteres de la commande
 */
parserError interpreteViderAreteGraphe(char* cmd)
{
    erreur err;
    
    int numCom;    
    int nbArgs = sscanf(cmd, "%d:viderAreteGraphe()", &numCom);
    
    if(nbArgs ==1)
    {
	err = viderAreteGraphe();
    }
    else
    {
	return ARGUMENTS_INCORRECTS;
    }
    
    return TRAITEMENT_CMD_OK;
    
}

/**
 * Interprete la commande testerArete(int idGraphe,
 *		int sommetDep,
 *		int poids,
 *		int sommetArr,
 *		char oriente,
 *		int resAttendu);
 *		
 * @param cmd : la chaine de caracteres de la commande
 */
parserError interpreteTesterArete(char* cmd)
{
    int numCom;
    int arg1, arg2, arg3,arg4;
    char arg5;
    char arg6[5];
    
    erreur err;
    
    int nbArgs = sscanf(cmd, "%d:testerArete(%d,%d,%d,%d,%c,%4s)", &numCom, &arg1, &arg2, &arg3, &arg4, &arg5, arg6);
    
    if(nbArgs == 7)
    {
	if(strcmp(arg6,"true") == 0)
	{
	    err = testerArete(arg1,arg2,arg3,arg4,arg5,1);
	}
	else if(strcmp(arg6,"fals") == 0)
	{
	    err = testerArete(arg1,arg2,arg3,arg4,arg5,0);
	}
	else
	{
	    return ARGUMENTS_INCORRECTS;
	}
    }
    else
    {
	// Retourner erreur
    }
    return TRAITEMENT_CMD_OK;
    
}

/**
 * Interprete la commande testerSommet(int idGraphe,
 *		int sommet,
 *		int resAttendu);
 * @param cmd : la chaine de caracteres de la commande
 */
parserError interpreteTesterSommet(char* cmd)
{
    int numCom;
    int arg1, arg2;
    char arg3[5];
    
    erreur err;
    
    int nbArgs = sscanf(cmd, "%d:testerSommet(%d,%d,%4s)", &numCom, &arg1, &arg2, arg3);
    
    if(nbArgs == 4)
    {
	if(strcmp(arg3,"true") == 0)
	{
	    err = testerSommet(arg1,arg2,1);
	}
	else if(strcmp(arg3,"fals") == 0)
	{
	    err = testerSommet(arg1,arg2,0);
	}
	else
	{
	    return ARGUMENTS_INCORRECTS;
	}
    }
    else
    {
	
	return ARGUMENTS_INCORRECTS;
    }
    
    return TRAITEMENT_CMD_OK;
    
    
}

/**
 * Interprete la commande testerDegreSommet(int idGraphe,
 *                int sommet,
 *		int value,
 *		int resAttendu)
 *		
 * @param cmd : la chaine de caracteres contenant la commande
 */
parserError interpreteTesterDegreSommet(char* cmd)
{
    int numCom;
    int arg1, arg2, arg3;
    char arg4[5];
    
    erreur err;
    
    int nbArgs = sscanf(cmd, "%d:testerDegreSommet(%d,%d,%d,%4s)", &numCom, &arg1, &arg2, &arg3, arg4);
    
    if(nbArgs == 5)
    {
	if(strcmp(arg4,"true") == 0)
	{
	    err = testerDegreSommet(arg1,arg2,arg3,1);
	}
	else if(strcmp(arg4,"fals") == 0)
	{
	    err = testerDegreSommet(arg1,arg2,arg3,0);
	}
	else
	{
	    return ARGUMENTS_INCORRECTS;
	}
    }
    else
    {
	return ARGUMENTS_INCORRECTS;
    }
    
    return TRAITEMENT_CMD_OK;
    
}

/**
 * Interprete la commande compareGraphe(int resAttendu)
 * @param cmd : la chaine de caracteres de la commande
 */
parserError interpreteCompareGraphe(char* cmd)
{
    int numCom;
    int arg1;
    
    erreur err;
    
    int nbArgs = sscanf(cmd, "%d:compareGraphe(%d)", &numCom, &arg1);
    
    if(nbArgs == 2)
    {
	err = compareGraphe(arg1);
    }
    else
    {
	return ARGUMENTS_INCORRECTS;
    }
    
    return TRAITEMENT_CMD_OK;
    
}

/**
 * Interprete la commande compareSommet(int sommet, int resAttendu)
 * @param cmd : la chaine de caracteres de la commande
 */
parserError interpreteCompareSommet(char* cmd)
{
    int numCom;
    int arg1;
    char arg2[5];
    erreur err;
    
    int nbArgs = sscanf(cmd, "%d:compareSommet(%d,%4s)", &numCom, &arg1, arg2);
    
    if(nbArgs == 3)
    {
	if(strcmp(arg2,"true")== 0)
	{
	    err = compareSommet(arg1,1);
	}
	else if(strcmp(arg2,"fals") == 0)
	{
	    err = compareSommet(arg1, 0);
	}
	else
	{
	    return ARGUMENTS_INCORRECTS;
	}
    }
    else
    {
	return ARGUMENTS_INCORRECTS;
    }
    
    return TRAITEMENT_CMD_OK;
    
}


