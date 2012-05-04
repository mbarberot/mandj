#
# Projet SCS - MOIA
# 2011 - 2012
#
# Makefile du Foursight.
# Compile le programme.
#
# Barberot Mathieu et Racenet Joan
#

# Variables :
# - Chemins vers les sources
SRCDIR=src/
CDIR=$(SRCDIR)c/
JAVADIR=$(SRCDIR)java/
PROLOGDIR=$(SRCDIR)prolog/
# - Chemin vers l'executable
BINDIR=bin/


# Règles :
# - Principale
all: c java prolog

# - Compilation de la partie C
c :
	make -C $(CDIR)
	
# - Compilation de la partie Java
java :
	make -C $(JAVADIR)

# - Copie des fichiers prolog dans le repertoire bin/
prolog :
	make -C $(PROLOGDIR)

# - Nettoyage des fichiers objets
clean:
	make -C $(CDIR) clean
	make -C $(JAVADIR) clean

# - Nettoyage des executables
reset:
	make -C $(CDIR) reset
	make -C $(JAVADIR) reset
	
# - Nettoyage complet
veryclean: clean reset


