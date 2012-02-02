/**
*	PROJET IG - 2011/2012
*		ILLUSIONS D'OPTIQUE
*				BUMP MAPPING
*
*	Auteurs : M. BARBEROT & J. RACENET
*
*/

#include <stdio.h>
#include <math.h>
#include <stdlib.h>

#include <GL/glut.h>
#include <GL/gl.h>
#include <GL/glu.h>

/* Variables et constantes globales             */

// Angles de la scène
static float angX = 90.0;
static float angY = 0.0;
static float angZ = 0.0;

// Positions des lumières
static float posLum1[] = { 2.0f, 2.0f, -5.0f, 1.0f };
static float posLum2[] = { 0.0f, -1.0f, 1.0f, 0.0f };
static float posLum3[] = { 10.0f, 15.0f, 5.0f, 1.0f };

//Couleurs
static const float blanc[] = {1.0f, 1.0f, 1.0f};
static const float gris[] = { 0.5f, 0.5f, 0.5f };
static const float gris_clair[] = { 0.8f, 0.8f, 0.8f };
static const float noir[] = { 0.0F, 0.0F, 0.0F, 1.0F };
static const float bleuClair[] = { 0.95f, 1.0f , 1.0f };
static const float jaune[] = {1.0f, 0.95f, 0.9f};
static const float marron[] = { 0.5f, 0.25f, 0.1f };
static const float spec[] = {0.7f, 0.22f, 0.05f };
static const float ambient[] = { 0.8f, 0.9f, 1.0f };
// Cstes de modélisation
double c = 5.0 ;    // Taille d'un coté du carrelage
double r = 0.5 ;    // Rayon des arrondis
static int n = 5 ;         // Nombre de facettes = n*n
static const float subDiv = 2.0;	// Subdivision des faces

// Ratio influençant la vitesse de déplacement de la lumière
double ratio = 0.5 ;

// Cstes d'exécution
bool bumpMode = false;
bool fildefer = false;



/**
*	Fonctions de l'application
*/

// Gestion du clavier
void keyboard(unsigned char key,int x,int y){
	switch(key){
	case 0x20 : // espace
		bumpMode = !(bumpMode);
		glutPostRedisplay();
		break;
	case 0x66 : // f
        fildefer = !fildefer;
        glutPostRedisplay();
        break;
    case 0x7A : // z
        posLum3[2] -= ratio ;
        glutPostRedisplay();
        break;
    case 0x71 : // q
        posLum3[0] -= ratio ;
        glutPostRedisplay();
        break;
    case 0x73 : // s
        posLum3[2] += ratio ;
        glutPostRedisplay();
        break;
    case 0x64 : // d
        posLum3[0] += ratio ;
        glutPostRedisplay();
        break;
    case 0x61 : // a
        posLum3[1] += ratio ;
        glutPostRedisplay();
        break;
    case 0x65 : // e
        posLum3[1] -= ratio ;
        glutPostRedisplay();
        break;
    case 0x2B : // +
        n ++;
        glutPostRedisplay();
        break;
    case 0x2D : // -
        n = (n < 2 )?1:n - 1;
        glutPostRedisplay();
        break;
    }
}

void special(int key, int x, int y)
{
	switch(key)
	{
	case GLUT_KEY_UP :
		angX++;
		glutPostRedisplay();
		break;
	case GLUT_KEY_DOWN :
		angX--;
		glutPostRedisplay();
		break;
	case GLUT_KEY_LEFT :
		angY++;
		glutPostRedisplay();
		break;
	case GLUT_KEY_RIGHT :
		angY--;
		glutPostRedisplay();
		break;
	case GLUT_KEY_PAGE_UP :
		angZ++;
		glutPostRedisplay();
		break;
	case GLUT_KEY_PAGE_DOWN :
		angZ--;
		glutPostRedisplay();
		break;
	}

}

// Configuration de l'éclairage
void lights(void)
{
	//LIGHT_1 : omni, à l'arrière, grise
	glLightfv(GL_LIGHT1, GL_POSITION, posLum1);
	glLightfv(GL_LIGHT1,GL_DIFFUSE,gris_clair);
	glLightfv(GL_LIGHT1,GL_SPECULAR,blanc);
	glLightfv(GL_LIGHT1,GL_AMBIENT,gris_clair);

    //LIGHT_2 : Directionnelle, à l'avant, crème (soleil)
	glLightfv(GL_LIGHT2, GL_POSITION, posLum2);
	glLightfv(GL_LIGHT2,GL_DIFFUSE,jaune);
	glLightfv(GL_LIGHT2,GL_SPECULAR,noir);
	glLightfv(GL_LIGHT2,GL_AMBIENT,jaune);

    //LIGHT_3 : Directionnelle, depuis la caméra, blanche
    glLightfv(GL_LIGHT3, GL_POSITION, posLum3);
	glLightfv(GL_LIGHT3,GL_DIFFUSE,blanc);
	glLightfv(GL_LIGHT3,GL_SPECULAR,gris);
	glLightfv(GL_LIGHT3,GL_AMBIENT,bleuClair);



    if(!glIsEnabled(GL_LIGHTING)) { glEnable(GL_LIGHTING); }
    if(!glIsEnabled(GL_LIGHT1))   { glEnable(GL_LIGHT1);   }
    if(!glIsEnabled(GL_LIGHT2))   { glEnable(GL_LIGHT2);   }
    if(!glIsEnabled(GL_LIGHT3))   { glEnable(GL_LIGHT3);   }

}

// Définit les matériaux
void materials ()
{
    glMaterialfv(GL_FRONT_AND_BACK,GL_DIFFUSE,marron);
    glMaterialfv(GL_FRONT_AND_BACK,GL_SPECULAR,blanc);
    glMaterialfv(GL_FRONT_AND_BACK,GL_AMBIENT,noir);
    glMaterialf(GL_FRONT_AND_BACK,GL_SHININESS,128.0f);
}

// Initialisation
void init(void){
	glDepthFunc(GL_LESS);
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_NORMALIZE);
	glEnable(GL_AUTO_NORMAL);
	glEnable(GL_CULL_FACE);
}

/**
 * Dessine un carré facettisé
 * @param n : nombre de lignes et de colonnes
 * @param c : coté du carré à facettiser
 * @param h : hauteur où placer les facettes
 */
void drawSquare (int n, float c, float h)
{
    // Locales
    float x1,x2;
    float y = h;
    float z1,z2;
    float cote_facette = 2 * c / n ;

    // Algorithme
    for( int j = 0; j < n; j++ )
    {
        z1 = (j * cote_facette) - c ;
        z2 = ((j+1) * cote_facette) - c;

        for( int i = 0; i < n; i++ )
        {
            x1 = (i * cote_facette) - c;
            x2 = ((i+1) * cote_facette) - c;

            glVertex3f(x1,y,z1);
            glVertex3f(x1,y,z2);
            glVertex3f(x2,y,z2);
            glVertex3f(x2,y,z1);
        }
    }
}


// Création d'une dalle de carrelage
void tablette (bool mode)
{
    double hc = c / 2 ;    // Moitié de la taille d'un coté du "grand carré" formant la base du carrelage
    double dc = (c - r) / 2 ; // Moitié de la taille d'un coté du "petit carré" formant le haut du carrelage

    GLboolean normalize = glIsEnabled(GL_NORMALIZE);
    if( !normalize ) { glEnable(GL_NORMALIZE); }

	for(int i = 0 ; i < 6; i ++) {
		for (int j = 0 ; j < 3 ; j++ ){

			glPushMatrix();
			glTranslatef( i * c, 0.0f , j * c );

			glBegin(GL_QUADS);
			// Face "devant"
			glNormal3f(0.0, 2 * dc * (hc - dc), 2 * r * dc );
            (mode)?glVertex3f(-dc,0,dc):glVertex3f(-dc,r,dc);
            glVertex3f(-hc,0.0,hc);
            glVertex3f(hc,0.0,hc);
            (mode)?glVertex3f(dc,0,dc):glVertex3f(dc,r,dc);

			// Face de droite

			glNormal3f(2 * r * dc , 2 * dc * (hc - dc),0.0);
			(mode)? glVertex3f(dc,0,dc):glVertex3f(dc,r,dc);
			glVertex3f(hc,0.0,hc);
			glVertex3f(hc,0.0,-hc);
			(mode)?glVertex3f(dc,0,-dc):glVertex3f(dc,r,-dc);

			// Face derriere
			glNormal3f(0.0, 2 * dc * (hc - dc),- 2 * r * dc );
			(mode)? glVertex3f(dc,0,-dc):glVertex3f(dc,r,-dc);
			glVertex3f(hc,0.0,-hc);
			glVertex3f(-hc,0.0,-hc);
			(mode)? glVertex3f(-dc,0,-dc):glVertex3f(-dc,r,-dc);


			// Face gauche
			glNormal3f( - 2 * r * dc , 2 * dc * (hc - dc),0.0);
			(mode)? glVertex3f(-dc,0,-dc) : glVertex3f(-dc,r,-dc);
			glVertex3f(-hc,0.0,-hc);
			glVertex3f(-hc,0.0,hc);
			(mode)? glVertex3f(-dc,0,dc):glVertex3f(-dc,r,dc);

			// Face bas
			if(!mode){
				glNormal3f(0.0,-1.0,0.0);
				glVertex3f(hc,0.0,-hc);
				glVertex3f(hc,0.0,hc);
				glVertex3f(-hc,0.0,hc);
				glVertex3f(-hc,0.0,-hc);
			}


			// Face haut
			glNormal3f(0.0,1.0,0.0);
			drawSquare(n,dc,(mode)?0.0:r);
			glEnd();
			glPopMatrix();
		}

	}

    if( !normalize ) { glDisable(GL_NORMALIZE); }


}


// Modelisation de la scène
void scene(void){
	glPushMatrix();
	tablette(bumpMode);
	glPopMatrix();
}

// Affichage
void display(void){
	glClearColor(0.0F,0.0F,0.0F,1.0F);
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glPolygonMode(GL_FRONT_AND_BACK, (fildefer) ? GL_LINE : GL_FILL);
	// Rotation de la scène
	glPushMatrix();
	glRotatef(angX, 1.0, 0.0, 0.0);
	glRotatef(angY, 0.0, 1.0, 0.0);
	glRotatef(angZ, 0.0, 0.0, 1.0);
	// Configurations des lumières
	lights();
    materials();
	// Dessin de la scène
	scene();
	glPopMatrix();
	glFlush();
	glutSwapBuffers();
	int error = glGetError();
	if ( error != GL_NO_ERROR )
		printf("Erreur OpenGL: %d\n",error);
}

// Réaffichage
void reshape(int x,int y) {
  glViewport(0,0,x,y);
  glMatrixMode(GL_PROJECTION);
  glLoadIdentity();
  gluPerspective(30.0F,(float) x/y,-30.0,30.0);
  glMatrixMode(GL_MODELVIEW);
  glLoadIdentity();
  gluLookAt(
        12.0,-5.0,50.0,
        12.0,-5.0,40.0,
        0.0,1.0,0.0
        );
}

/* Fonction principale                          */

int main(int argc,char **argv) {
  glutInit(&argc,argv);
  glutInitDisplayMode(GLUT_RGBA|GLUT_DEPTH|GLUT_DOUBLE);
  glutInitWindowSize(600,300);
  glutInitWindowPosition(50,50);
  glutCreateWindow("Bump Mapping");
  init();
  glutDisplayFunc(display);
  glutReshapeFunc(reshape);
  glutSpecialFunc(special);
  glutKeyboardFunc(keyboard);
  glutMainLoop();
  return(0);
}
