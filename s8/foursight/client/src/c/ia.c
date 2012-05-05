/*
 * Projet MOIA -SCS
 * 2011 - 2012
 *
 * Fichier de gestion de l'IA
 *
 * Barberot Mathieu &
 * Racenet Joan
 *
 */

#define DEBUG 1


#include "ia.h"


ia_err ia_initJVM(JNIEnv* env)
{   
    jvm = NULL;
    foursightIA = NULL;
    
    //JNIEnv* env;
    jint res;
    jclass cls;
    jmethodID mid;
    
    //
    // Demarrage de la JVM
    //
    // Code trouvé sur le manuel JNI 
    // http://java.sun.com/docs/books/jni/html/titlepage.html
    //
#ifdef JNI_VERSION_1_2
    JavaVMInitArgs vm_args;
    JavaVMOption options[1];
    options[0].optionString = "-Djava.class.path=" USER_CLASSPATH;
    vm_args.version = 0x00010002;
    vm_args.options = options;
    vm_args.nOptions = 1;
    vm_args.ignoreUnrecognized = JNI_TRUE;
    /* Create the Java VM */
    res = JNI_CreateJavaVM(&jvm, (void**)&env, &vm_args);
    
#else
    JDK1_1InitArgs vm_args;
    char classpath[1024];
    vm_args.version = 0x00010001;
    JNI_GetDefaultJavaVMInitArgs(&vm_args);
    /* Append USER_CLASSPATH to the default system class path */
    sprintf(classpath, "%s%c%s",
	    vm_args.classpath, 
	    PATH_SEPARATOR, 
	    USER_CLASSPATH
	    );
    vm_args.classpath = classpath;
    /* Create the Java VM */
    res = JNI_CreateJavaVM(&jvm, &env, &vm_args);
#endif /* JNI_VERSION_1_2 */


    //
    // Vérification
    //
    if (res < 0) 
    {
	fprintf(stderr, "Can't create Java VM\n");
	if(DEBUG) { printf("[DEBUG] (ia_initJVM) Impossible de créer la JVM\n"); }
	exit(1);
    }
	    
    //
    // Recherche de la classe IA
    //
    cls = (*env)->FindClass(env, "Run");
    if (cls == NULL) 
    {
	if(DEBUG) { printf("[DEBUG] (ia_initJVM) Impossible de trouver la classe IA\n"); }
	goto destroy;
    }

    //
    // Recherche du constructeur
    //
    mid = (*env)->GetMethodID(env, cls, "<init>","()V");
    if (mid == NULL) 
    {
	if(DEBUG) { printf("[DEBUG] (ia_initJVM) Impossible de trouver le constructeur de la classe IA\n"); }	
	goto destroy;	
    }
	    
    //
    // Création de l'objet IA dans une variable globale
    //
    foursightIA = (*env)->NewObject(env,cls,mid,NULL);
    if(foursightIA == NULL)
    {
	if(DEBUG) { printf("[DEBUG] (ia_initJVM) Création de l'objet IA impossible\n"); }
	goto destroy;
    }	    
    
  
    return IA_OK;
	    
//
// Cas d'échec
//
destroy:
    // Affichage des exceptions
    if ((*env)->ExceptionOccurred(env)) 
    {
	(*env)->ExceptionDescribe(env);
    }
    // Destruction de la JVM
    (*jvm)->DestroyJavaVM(jvm);
    
    return IA_ERR;
}



void* ia_calculeCoup(void *arg)
{   
    JNIEnv *env;
    jclass cls;
    jmethodID mid;
    jint err;
    int i;
    
    // Récupération des variables partagées
    Shared_vars *data;
    data = (Shared_vars*)arg;
    
    
    //
    // Attacher le thread à la JVM
    // et obtenir l'environnement
    //
    err = (*jvm)->AttachCurrentThread(jvm,(void**)&env,NULL);
    if(err != JNI_OK)
    {
	if(DEBUG) { printf("[DEBUG] (ia_calculeCoup) Echec de la récupération l'environnement\n"); }
	goto destroy;
    }
    else if(DEBUG)
    {
	printf("[DEBUG] (ia_calculeCoup) Récupération de l'environnement réussie !\n");
    }
    
    //
    // Vérification de l'obtention de l'environnement
    //
    if(env == NULL)
    {
	if(DEBUG) { printf("[DEBUG] (ia_calculeCoup) env == NULL\n"); }
	goto destroy;
    }
    else if(DEBUG) { printf("[DEBUG] (ia_calculeCoup) env != NULL\n"); }
    
    
    //
    // Verification de l'initialisation de l'objet IA
    //
    if(foursightIA == NULL)
    {
	if(DEBUG) { printf("[DEBUG] (ia_calculeCoup) foursightIA == NULL)\n"); }
	goto destroy;
    }
    else if(DEBUG)
    {
	printf("[DEBUG] (ia_calculeCoup) foursightIA != NULL\n");
    }
    
    
    
    //
    // Récupération de la classe
    //
    cls = (*env)->GetObjectClass(env,foursightIA);
    if(cls == NULL) 
    { 
	if(DEBUG) { printf("[DEBUG] (ia_calculeCoup) Impossible de trouver la classe IA"); }
	goto destroy;
    }
    else if(DEBUG)
    {
	printf("[DEBUG] (ia_calculeCoup) Classe IA trouvée\n");
    }

    
    
    
    //
    // Création du plateau
    //
    jintArray jplateau;
    jplateau = (*env)->NewIntArray(env,SIZE_PLATEAU);    
    jint jtmp[SIZE_PLATEAU] ;    
    for(i = 0; i < SIZE_PLATEAU; i++)
    {
	jtmp[i] = plateau[i];
    }    
    (*env)->SetIntArrayRegion(env,jplateau,0,SIZE_PLATEAU,jtmp);
    
    //
    // Trouver la méthode setPlateau
    //
    mid = (*env)->GetMethodID(env,cls,"setPlateau","([I)V");
    if(mid == NULL) 
    { 
	if(DEBUG) { printf("[DEBUG] (ia_calculeCoup) Impossible de trouver la methode setPlateau"); } 
	goto destroy;
    }
    else if(DEBUG)
    {
	printf("[DEBUG] (ia_calculeCoup) Méthode setPlateau trouvée\n");
    }
    
    //
    // Appel de la méthode setPlateau
    //
    (*env)->CallVoidMethod(env,foursightIA,mid,jplateau);   
    if ((*env)->ExceptionOccurred(env)) 
    {
	printf("[DEBUG] (ia_calculeCoup) La méthode setPlateau a échouée\n");
	(*env)->ExceptionDescribe(env);
	goto destroy;
    }    
    else if(DEBUG) { printf("[DEBUG] (ia_calculeCoup) setPlateau effectué\n"); }
    
    
    
    //
    // Trouver la méthode de calcul du coup
    //
    jmethodID runID;
    runID = (*env)->GetMethodID(env,cls,"run","()V");
    if(runID == NULL)
    {
	if(DEBUG) { printf("[DEBUG] (ia_calculeCoup) Impossible de trouver la méthode run\n"); }
	goto destroy;
    }
    else if(DEBUG) { printf("[DEBUG] (ia_calculeCoup) Méthode run trouvée\n"); }
    
    //
    // Lancement de la méthode de calcul du coup
    // 
    (*env)->CallVoidMethod(env,foursightIA,runID,NULL);
    if ((*env)->ExceptionOccurred(env)) 
    {
	printf("[DEBUG] (ia_calculeCoup) La méthode run a échoué\n");
	(*env)->ExceptionDescribe(env);
	goto destroy;
    }    
    else if(DEBUG) { printf("[DEBUG] (ia_calculeCoup) run effectué\n"); }
    
    //
    // Récupérer la méthode getCoup
    //
    jmethodID getID;
    getID = (*env)->GetMethodID(env,cls,"getCoup","()I");
    if(getID == NULL)
    {
	if(DEBUG) { printf("[DEBUG] (ia_calculeCoup) Impossible de trouver la méthode getCoup\n"); }
	goto destroy;
    }
    else if(DEBUG) { printf("[DEBUG] (ia_calculeCoup) Méthode getCoup trouvée\n"); }

    //
    // Appeller la méthode getCoup
    //
    jint res;
    res = (*env)->CallIntMethod(env,foursightIA,getID,NULL);
    if ((*env)->ExceptionOccurred(env)) 
    {
	printf("[DEBUG] (ia_calculeCoup) La méthode getCoup a échoué\n");
	(*env)->ExceptionDescribe(env);
	goto destroy;
    }    
    else if(DEBUG) { printf("[DEBUG] (ia_calculeCoup) getCoup effectué\n"); }


    //
    // Traitement du résultat
    //
    
    if(res != 0)
    {
	int typeCoup, typePion, typeLigne, typeCol;
	
	typeCoup = res / 1000 ;
	res %= 1000;
	typePion = res / 100 ;
	res %= 100;
	typeLigne = res / 10 ;
	res %= 10;
	typeCol = res;
    
    

	//
	// Mise à jour des données partagées
	//
	// Utilisation d'un mutex pour ne pas faire d'écritures simultanées
	//    
	pthread_mutex_lock(&data->mutex);
	
	data->fini++;
	data->ia_first++;
	
	data->coup->propCoup = typeCoup ;
	data->coup->typePiece = typePion ;
	data->coup->caseArrivee.ligne = typeLigne ;
	data->coup->caseArrivee.colonne = typeCol ;
	
	if(DEBUG) { printf("[DEBUG] (ia_calculeCoup) TypeCoup : %d - TypePiece : %d - Ligne : %d - Colonne : %d \n", typeCoup, typePion, typeLigne,typeCol); }
	
	pthread_mutex_unlock(&data->mutex);    
    }
    else 
    {
	system("sleep 5");
    }
    
    //
    // Affichage de fin de fonction
    //
    if(DEBUG) { printf("---------------------------------------------------------------------------------\n"); }
    
    
    // Libération du plateau
    (*env)->DeleteLocalRef(env,jplateau);
    // Détachement du thread à la jvm
    (*jvm)->DetachCurrentThread(jvm);
    
    // Fin du thread
    pthread_exit(NULL);
    
    
//
// Cas d'erreur :
//
destroy:

    //
    // Affichage des exceptions
    //
    if ((*env)->ExceptionOccurred(env)) 
    {
	(*env)->ExceptionDescribe(env);
    }    
    
    //
    // Fin du thread
    //
    pthread_mutex_lock(&data->mutex);
    data->fini++;
    pthread_mutex_unlock(&data->mutex);
    
    //
    // Libérations
    //
    (*env)->DeleteLocalRef(env,jplateau);
    
    // Détachement du thread à la jvm
    (*jvm)->DetachCurrentThread(jvm);
    pthread_exit(NULL);
}













ia_err ia_closeJVM()
{
   
    // Arrêt de la JVM
    (*jvm)->DestroyJavaVM(jvm);
    if(DEBUG) { printf("[DEBUG] (ia_closeJVM) JVM detruite\n"); }
    return IA_OK;
}


/*
void *ia_calculeCoupOld(void *arg)
{
int i,
alea,
found,
tirage[SIZE_PLATEAU];

char *debug; 

TypCoupReq *coup;
TypPosition *pos;

Shared_vars *data;

data = (Shared_vars*)arg;
coup = data->coup;

if(coup == NULL)
{
if(DEBUG)
{
printf("[DEBUG] ia_calculeCoup\n");
printf("[DEBUG] Argument coup == NULL\n");
printf("-----------------------------\n");
}
pthread_exit(NULL);
}

pos = (TypPosition*) malloc(sizeof(TypPosition));
if(pos == NULL)
{	
if(DEBUG)
{
printf("[DEBUG] ia_calculeCoup\n");
printf("[DEBUG] Impossible d'allouer la mémoire\n");
printf("---------------------------------------\n");
}
pthread_exit(NULL);
}


for(i = 0; i < SIZE_PLATEAU; i++) { tirage[i] = 0; }

found = 0;

while(!found) 
{
do {
alea = rand()%SIZE_PLATEAU;	   
} while(tirage[alea] > 0);	

system("sleep 1");

tirage[alea]++;	

switch(plateau[alea])
{
case VIDE :		
if(joueur->blanc > 0) 
{
coup->typePiece = BLANC; 
found = 1;
debug = "blanc";
}
else if(
((joueur->rouge == 0 && joueur->jaune > 0) || (joueur->rouge > 0 && joueur->jaune == 0))
&& adversaire->rouge == 0 && adversaire->jaune == 0)
{
coup->typePiece = VIDE;
coup->propCoup = PASSE;
found = 1;
debug = "vide";
}
break;
case BLANC :
if(joueur->rouge > 0)
{
coup->typePiece = ROUGE;
found = 1;
debug = "rouge";		
}
else if(joueur->jaune > 0)
{
coup->typePiece = JAUNE;
found = 1;
debug = "jaune";
}
break;
default:
break;
}

}

if(DEBUG)
{
printf("[DEBUG] ia_calculeCoup \n");
printf("[DEBUG] alea = %d, ligne = %d, colonne = %d, pion = %s\n",alea,alea/NB_COL,alea%NB_COL,debug);
printf("-------------------------------------------\n");
}

pos->ligne = alea/NB_COL;
pos->colonne = alea%NB_COL;

pthread_mutex_lock(&data->mutex);

coup->caseArrivee = *pos;
if(coup->propCoup != PASSE) coup->propCoup = POSE;

data->fini++;
data->ia_first++;


pthread_mutex_unlock(&data->mutex);

return IA_OK;
}
*/