#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "parser.h"
#include "graphe.h"
#include "menu.h"



int main(int argc, char **argv)
{
    if(argc == 1)
    {
	// Lancement avec IHM
	afficheMenu();
    }
    else if(argc == 2)
    {
	// Lancement avec fichier
	parserError err = chargerFichier(argv[1]);
	if(err != TRAITEMENT_CMD_OK)
	{
	    parserErrorToString(err);
	}
    }
    else 
    {
	// Erreur sur les arguments
	printf("\nusage : main [FILE]\n\tFILE is optional\n\n");
	return 1;
    }

    return 0;
}
