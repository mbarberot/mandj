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
# Variables pour la compilation
# CC : Le compilateur des sources
# CFLAGS : Les options de compilations
# 	-O4 -W -Wall : Compilation rigoureuse (tous les warnings sont affichés) 
#	-g : Permet d'utiliser le débogueur
# CPPFLAGS : ?
# LDFLGAD : Les librairies à utiliser lors de la compilation
#	- m : ?
# AR : La commande construisant les .a
#
CC=gcc
CFLAGS=-O4 -W -Wall -g
CPPFLAGS=-I.
LDFLAGS=-lm $(LIB)/liste.a
AR=ar -q

#
# EXE : Le fichier à créer
# SRC : Les fichiers .c
# HDR : Les fichiers .h
# OBJ : Les fichiers .o
#
EXE=graphe.a
SRC=$(wildcard *.c)
HDR=$(wildcard *.h)
OBJ=$(SRC:.c=.o)

#
# Dossiers utiles
# INC : dossier contenant les .h (généralement appelé "include")
# LIB : dossier contenant les .a/.so (généralement appelé "lib")
#
INC=../../include/
LIB=../../lib


#
# Règle principale
#
all : $(EXE)

#
# 1/ Construction de l'archive
# 2/ Copie des .h dans le dossier "include"
# 3/ Copie des .a dans le dossier "lib"
#
$(EXE) : $(OBJ)
	$(AR) $@ $^
	cp $(HDR) $(INC)
	cp $(EXE) $(LIB)

#
# Compilation de tous les .c et .h en .o
#
%.o : %.c %.h
	$(CC) $(CFLAGS) $(CPPFLAGS) -o $@ -c $< $(LDFLAGS)


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
	rm *.o

reset :
	rm $(EXE)

veryclean : clean reset
	rm $(INC)$(HDR)
	rm $(LIB)$(EXE)

