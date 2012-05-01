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

void *ia_calculeCoup(void *arg)
{
    int i,
	alea,
	found,
	tirage[15];

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


    for(i = 0; i < 15; i++) { tirage[i] = 0; }

    found = 0;

    while(!found) 
    {
	do {
	    alea = rand()%16;	   
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
	printf("[DEBUG] alea = %d, ligne = %d, colonne = %d, pion = %s\n",alea,alea/4,alea%4,debug);
	printf("-------------------------------------------\n");
    }

    pos->ligne = alea/4;
    pos->colonne = alea%4;

    pthread_mutex_lock(&data->mutex);

    coup->caseArrivee = *pos;
    if(coup->propCoup != PASSE) coup->propCoup = POSE;

    data->fini++;
    data->ia_first++;


    pthread_mutex_unlock(&data->mutex);

    return IA_OK;
}


ia_err ia_initJVM()
{
    JNIEnv *env;
    JavaVM *jvm;
    jint res;
    jclass cls;
    jmethodID mid;
    jobject foursightIA;
   
#ifdef JNI_VERSION_1_2
    JavaVMInitArgs vm_args;
    JavaVMOption options[1];
    options[0].optionString =
	"-Djava.class.path=" USER_CLASSPATH;
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
	    vm_args.classpath, PATH_SEPARATOR, USER_CLASSPATH);
    vm_args.classpath = classpath;
    /* Create the Java VM */
    res = JNI_CreateJavaVM(&jvm, &env, &vm_args);
#endif /* JNI_VERSION_1_2 */

    if (res < 0) 
    {
	fprintf(stderr, "Can't create Java VM\n");
	exit(1);
    }
    
    
    cls = (*env)->FindClass(env, "IA");
    if (cls == NULL) 
    {
	goto destroy;
    }
    printf("FindClass IA\n");

    mid = (*env)->GetMethodID(env, cls, "<init>","()V");
    if (mid == NULL) 
    {
	goto destroy;
    
    }
    printf("GetMethodID <init>\n");
    
    foursightIA = (*env)->NewObject(env,cls,mid,NULL);
    if(foursightIA == NULL)
    {
	goto destroy;
    }
    printf("Object IA !\n");
    return IA_OK;
    
destroy:
    if ((*env)->ExceptionOccurred(env)) {
	(*env)->ExceptionDescribe(env);
    }
    (*jvm)->DestroyJavaVM(jvm);
    return IA_ERR;
}

ia_err ia_closeJVM()
{
    JavaVM *jvm = foursightJVM.jvm;
    //JNIEnv *env = foursightJVM.env;
    //jobject foursightIA = foursightJVM.foursightIA
    
    //(*env)->DeleteLocalRef(env, foursightIA);
    (*jvm)->DestroyJavaVM(jvm);
    
    return IA_OK;
}


