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
# - Chemins utiles
RACINE=../../
BINDIR=$(RACINE)bin/
SRCDIR=./

JASPER=/usr/local/sicstus4.2.0/lib/sicstus-4.2.0/bin/jasper.jar

CC=javac
CP=-cp $(JASPER):$(SRCDIR)
FILE=$(wildcard $(SRCDIR)*.java)
OUTDIR=-d $(BINDIR)
EXE=$(BIN)Run


# Règles :
# - Principale
compile :
	$(CC) $(CP) $(FILE) $(OUTDIR)

# - Nettoyage des fichiers temporaires
clean:
	rm $(SRCDIR)*.class

# - Nettoyage des fichiers executables
reset:
	rm $(BINDIR)$(EXEC) 

# - Nettoyage complet
veryclean: clean reset

