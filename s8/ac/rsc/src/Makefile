#
# Makefile pour compiler la librairie de manipulation des graphes
# 
# Détails :
#	Ce fichier doit être placé dans un dossier contenant les sources (.c et .h)
#	Le dossier parent à celui des sources doit contenir un dossier "include" et "lib"
#	La règle principale effectue :
#		- la compilation des .c et .h en .o
#		- la construction du .a
#		- la copie des .h dans le dossier "include"
#		- la copie des .a dans le dossier "lib"
#
#
#
# Barberot Mathieu
# Cours d'Algorithmique Combinatoire (AC)
# Master 1 Informatique
# 19 janvier 2012
# 
# -----------------------------------------------------------------------



#
# Dossiers utiles
# LISTE : dossier des sources de la bibliothèque de manipulation de listes
# GRAPHE : pareil, mais pour les graphes
# TESTS : Fichiers sources permettant de tester les fonctions développées
# EXEC : Les fichiers du programme principal (parser, menu, main)
LISTE= ./liste
GRAPHE= ./graphe
TESTS= ./tests
EXEC= ./exe
POSTIER_CHINOIS= ./postierChinois

BIN= ../bin

#
# Règle principale
#
all : liste.a graphe.a tests postier.a exec 

liste.a:
	make -C $(LISTE)

graphe.a: liste.a
	make -C $(GRAPHE)

tests: graphe.a liste.a
	make -C $(TESTS)

exec: graphe.a liste.a postier.a
	make -C $(EXEC)

postier.a : graphe.a liste.a
	make -C $(POSTIER_CHINOIS)

# 
# Nettoyage
#	clean : supprime les .o
#	reset : supprime le fichier généré
#	veryclean : effectue les deux commandes précédentes
#	mrproper : 
#		1/ nettoye le dossier courant avec veryclean 
#		2/ supprime les fichiers .h du dossier "include" 
#		3/ supprime les fichier .a du dossier "lib"
#
clean : 
	make -C $(LISTE) clean
	make -C $(GRAPHE) clean
	make -C $(EXEC) clean
	make -C $(POSTIER_CHINOIS) clean

reset :
	make -C $(LISTE) reset
	make -C $(GRAPHE) reset
	make -C $(EXEC) reset
	make -C $(POSTIER_CHINOIS) reset

veryclean : clean reset
	rm $(BIN)/*
