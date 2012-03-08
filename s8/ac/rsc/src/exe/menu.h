#include <stdio.h>
#include <stdlib.h>

#include "graphe.h"
#include "liste.h"
#include "parser.h"

#define CHARGER_FICHIER 1
#define RECHARGER 2
#define SAUVEGARDER 3
#define NETTOYER 4
#define QUITTER 5


void afficheMenu();

void actionQuitter();
void actionChargerFichier();
void actionRecharger();
void actionNettoyer();
void actionSauvegarder();