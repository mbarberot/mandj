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
# - Chemin vers l'executable
BINDIR=bin/
# - Argument de l'executable
HOST=localhost
PORT=5555

# Règles :
# - Principale
all: c java

# - Compilation de la partie C
c :
	make -C $(CDIR)
	
# - Compilation de la partie Java
java :
	make -C $(JAVADIR)
	
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

# - Lancement
run : all
	bin/client $(HOST) $(PORT)

