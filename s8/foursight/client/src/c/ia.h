#ifndef __IASCSMOIA__
#define __IASCSMOIA__

/*
 * Projet MOIA - SCS
 * 2011 - 2012
 *
 * Couche de gestion de l'IA
 * Fichier d'entête
 *
 * Barberot Mathieu &
 * Racenet Joan
 *
 */

// Librairies
// - standard
// - entrées/sorties
// - interfaçage avec Java
// - fonctions utilitaires sur les chaînes de caractères
#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include <string.h>

// Fichier de gestion :
// - des threads POSIX
// - du jeu
#include "thread.h"
#include "jeu.h"

#define PATH_SEPARATOR ';'
#define USER_CLASSPATH "bin/:/usr/local/sicstus4.2.0/lib/sicstus-4.2.0/bin/jasper.jar"

// Enumération des retours de fonctions
typedef enum { IA_OK, IA_ERR } ia_err ;

typedef struct ia_jvm
{
    JavaVM *jvm;
    JNIEnv *env;
    jobject foursightIA;
    
} ia_jvm;

//
// Globales
//
ia_jvm foursightJVM ;


//
// Fonctions :
//

/**
 * Calcule un coup
 * Lancé dans un thread POSIX pour permettre l'écoute de l'arbitre en parallèle
 *
 * @param coup		Le coup calculé
 * @return IA_OK	Le calcul s'est bien passé
 * @return IA_ERR	Une erreur s'est produite
 */
void* ia_calculeCoup(void *arg);

/**
 * Initialise la JVM pour les calculs de l'IA
 */
ia_err ia_initJVM();

/**
 * Arret de la JVM
 */
ia_err ia_closeJVM();

#endif
