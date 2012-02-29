#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "parser.h"
#include "graphe.h"

main(int argc, char **argv){
    
    chargerFichier("../exemple_accents.txt");
    afficheGraphe(1);
}
