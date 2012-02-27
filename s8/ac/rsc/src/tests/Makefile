# Chemins utilises a la compilation
BIN= ../../bin/
LIBDIR=../../lib/
INC=../../include/
 
# Commandes de compilation
CC=gcc
CFLAGS=-O4 -W -Wall -g
CPPFLAGS=-I $(INC)
LDFLAGS=-L $(LIBDIR) -lgraphe -lliste
EXEC = GoListe GoGraphe GoParser


# Regles
all: $(EXEC)


GoListe:
	$(CC) $@.c -o $(BIN)$@ $(CPPFLAGS) $(LDFLAGS)
GoGraphe:
	$(CC) $@.c -o $(BIN)$@ $(CPPFLAGS) $(LDFLAGS)
GoParser:
	$(CC) $@.c -o $(BIN)$@ $(CPPFLAGS) $(LDFLAGS)



