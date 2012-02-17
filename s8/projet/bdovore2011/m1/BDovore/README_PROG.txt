********************
*      BDovore     *
********************


- Compilation
- Execution
- Structure globale
- Autres notes



**************************************************


COMPILATION :

La compilation s'effectue avec Apache ANT, à l'aide du fichier build.xml (et MANIFEST.MF)

Durant la compilation, sont inclus :
- Les sources (de src/) compilées (dans bin/)
- Les images du logiciel, splash screen de démarrage compris (img/)

Est produit un fichier BDovore.jar en sortie.



**************************************************


EXECUTION :

BDovore nécessite les éléments suivants pour s'éxecuter :
- BDovore.jar (logiciel compilé)
- les bibliothèques : lib/ (h2.jar, lucene-core-2.4.jar, et opencsv-1.8.jar)
- les images : img/
- la base de données : db/
- config.xml (Fichier de configuration)

La base de donnée pesant, décompressée, presque 100Mo, et étant modifiée à chaque lancement, elle n'est pas incluse dans le SVN du projet pour des raisons évidentes.
Elle est cependant téléchargeable sur http://bdovore.googlecode.com (base de données seule ou code source complet)





**************************************************


STRUCTURE GLOBALE :

La structure globale de BDovore est la suivante :

- db : Toutes les opérations de base de donnée (DataBase pour les accès, Updater pour les mises à jour, Query générant des requêtes, Tables contenant des informations pour les INSERT/UPDATE)
- db.data : Les classes contenant les informations récupérées de la base (Album, Série, etc.)
- gui : L'interface graphique
- gui.action : Les actions/écouteur de l'interface
- main : Le MAIN.

On trouve, hors du code source :
- lib : Les bibliothèques (H2, la base de données, un module de recherche Lucene, et un module de CSV)
- img : Quelques images de l'application
- db : La base de données (dont un exemplaire est en ligne)





**************************************************


NOTES SUR LES BIBLIOTHEQUES "H2" ET "LUCENE" :

H2 est le moteur de base de données.
Apache Lucene est le moteur "Full Text" utilisé par H2, il permet la recherche naturelle par mots clé dans la base.


Quelques modifications simples ont été apportées à leur source (entre autre pour gérer la langue et les accents).
En voici le contenu (utile pour H2, car étant en plein développement une dernière version est toujours la bienvenue) :


org.h2.fulltext.FullTextLucene.java :
- StandardAnalyzer, changé pour FrenchAnalyzer (import .fr.FrenchAnalyzer, et 2 new FrenchAnalyze())

org.apache.lucene.analysis :
- Ajout du dossier "fr" (dans "contrib/analyzers/.../analysis")
- FrenchAnalyzer.java :
 import org.apache.lucene.analysis.ISOLatin1AccentFilter;
 result = new ISOLatin1AccentFilter(result); // dans tokenStream, en 3eme position des "result = new..."
 Suppression ou Edulcoration du tableau FRENCH_STOP_WORDS (sorte de "ignore list")

Compilation :
- Lucene se recompile comme il faut, avec ajout du fr (compiler avec ANT)
- H2 nécessite d'adapter le nouveau Lucene. Compiler une première fois (ANT JAR). Il téléchargera les extensions, dont Lucene, dans le dossier ext. Remplacer alors le Lucene par le notre (si version différente, renomer le .jar, ou son appel dans le build.xml), et recompiler.
(Dans certains versions, H2 compile sous Java 1.4, dans ce cas remplacer "-auto" dans switchSourceAuto par "-version 1.6")
