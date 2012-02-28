#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "parser.h"
#include "graphe.h"

main(int argc, char **argv){
    
    // Recuperation de la taille du fichier
    struct stat st;
    stat("../exemple_accents.txt", &st);
    int size = st.st_size;    
    char str[size];
    
    strcpy(str, lectureFichier("../exemple_accents.txt"));
    interpreteCommande(str);
}
