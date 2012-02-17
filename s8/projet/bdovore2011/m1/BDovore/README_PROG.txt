********************
*      BDovore     *
********************


- Compilation
- Execution
- Structure globale
- Autres notes



**************************************************


COMPILATION :

La compilation s'effectue avec Apache ANT, � l'aide du fichier build.xml (et MANIFEST.MF)

Durant la compilation, sont inclus :
- Les sources (de src/) compil�es (dans bin/)
- Les images du logiciel, splash screen de d�marrage compris (img/)

Est produit un fichier BDovore.jar en sortie.



**************************************************


EXECUTION :

BDovore n�cessite les �l�ments suivants pour s'�xecuter :
- BDovore.jar (logiciel compil�)
- les biblioth�ques : lib/ (h2.jar, lucene-core-2.4.jar, et opencsv-1.8.jar)
- les images : img/
- la base de donn�es : db/
- config.xml (Fichier de configuration)

La base de donn�e pesant, d�compress�e, presque 100Mo, et �tant modifi�e � chaque lancement, elle n'est pas incluse dans le SVN du projet pour des raisons �videntes.
Elle est cependant t�l�chargeable sur http://bdovore.googlecode.com (base de donn�es seule ou code source complet)





**************************************************


STRUCTURE GLOBALE :

La structure globale de BDovore est la suivante :

- db : Toutes les op�rations de base de donn�e (DataBase pour les acc�s, Updater pour les mises � jour, Query g�n�rant des requ�tes, Tables contenant des informations pour les INSERT/UPDATE)
- db.data : Les classes contenant les informations r�cup�r�es de la base (Album, S�rie, etc.)
- gui : L'interface graphique
- gui.action : Les actions/�couteur de l'interface
- main : Le MAIN.

On trouve, hors du code source :
- lib : Les biblioth�ques (H2, la base de donn�es, un module de recherche Lucene, et un module de CSV)
- img : Quelques images de l'application
- db : La base de donn�es (dont un exemplaire est en ligne)





**************************************************


NOTES SUR LES BIBLIOTHEQUES "H2" ET "LUCENE" :

H2 est le moteur de base de donn�es.
Apache Lucene est le moteur "Full Text" utilis� par H2, il permet la recherche naturelle par mots cl� dans la base.


Quelques modifications simples ont �t� apport�es � leur source (entre autre pour g�rer la langue et les accents).
En voici le contenu (utile pour H2, car �tant en plein d�veloppement une derni�re version est toujours la bienvenue) :


org.h2.fulltext.FullTextLucene.java :
- StandardAnalyzer, chang� pour FrenchAnalyzer (import .fr.FrenchAnalyzer, et 2 new FrenchAnalyze())

org.apache.lucene.analysis :
- Ajout du dossier "fr" (dans "contrib/analyzers/.../analysis")
- FrenchAnalyzer.java :
 import org.apache.lucene.analysis.ISOLatin1AccentFilter;
 result = new ISOLatin1AccentFilter(result); // dans tokenStream, en 3eme position des "result = new..."
 Suppression ou Edulcoration du tableau FRENCH_STOP_WORDS (sorte de "ignore list")

Compilation :
- Lucene se recompile comme il faut, avec ajout du fr (compiler avec ANT)
- H2 n�cessite d'adapter le nouveau Lucene. Compiler une premi�re fois (ANT JAR). Il t�l�chargera les extensions, dont Lucene, dans le dossier ext. Remplacer alors le Lucene par le notre (si version diff�rente, renomer le .jar, ou son appel dans le build.xml), et recompiler.
(Dans certains versions, H2 compile sous Java 1.4, dans ce cas remplacer "-auto" dans switchSourceAuto par "-version 1.6")
