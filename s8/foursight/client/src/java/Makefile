#
# Projet SCS - MOIA
# 2011 - 2012
#
# Sous-makefile.
# Compile la partie Java
#
# Barberot Mathieu et Racenet Joan
#

# Variables :
# - Chemins utilies
RACINE=../../
BIN=$(RACINE)bin/
LIBDIR=$(RACINE)lib/
INC=$(RACINE)include/
SRCDIR=./

# - Commandes de compilation
CC=javac
CFLAGS=
CPPFLAGS=
LDFLAGS=
EXEC=

# - Fichiers sources
SRC= $(wildcard $(SRCDIR)*.java)

# Règles :
# - Principale
all:$(EXEC)

# - Création de l'executable
$(EXEC): $(OBJ)
	$(CC) -o $(BIN)$@ $^ $(CFLAGS) $(CPPFLAGS) $(LDFLAGS) 

# - Compilations intermédiaires
%.o: %.c %.h
	$(CC) -o $@ -c $< $(CFLAGS) $(CPPFLAGS) $(LDFLAGS)

# - Nettoyage des fichiers temporaires
clean:
	rm $(SRCDIR)*.o

# - Nettoyage des fichiers executables
reset:
	rm $(BIN)$(EXEC) 

# - Nettoyage complet
veryclean: clean reset

