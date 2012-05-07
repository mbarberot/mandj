% Outils de liste %
:-set_prolog_flag(toplevel_print_options, [max_depth(0)]).
:-use_module(library(lists)).

%
% Plateau
%	Liste des cases occupÃÂ©es du jeu.
%	[Case, Case, ...]
%
% Case 
%	Une liste des 3 elements suivants :
%	- type du pion sur la case (un considere un pion vide)
%	- coordonnee en x
%	- coordonnee en y
%	[Pion, PosX, PosY]
%
% Coup
%	Une liste des 2 elements suivants
%	- type du coup
%	- la case du coup
%	[Type,Case]
%
% Type
%	Valeur entiere telle que :
%	0 = COUP
%	1 = GAGNE
%	2 = PERD
%	3 = NULLE
%	4 = PASSE
coup(0).
gagne(1).
perd(2).
nulle(3).
passe(4).


% Pion
%	Valeur entiere telle que : 
%	0 = Blanc
%	1 = Rouge
%	2 = Jaune
%
blanc(0).
rouge(1).
jaune(2).

%
% Joueurs
%	Liste de deux joeuurs
%	[Joueur, Joueur]
%
% Joueur
%	Liste de quatres elements :
%	- le numero du joueur : 0 = l'IA, 1 = l'adversaire
%	- le nombre de pions blancs
%	- le nombre de pions rouges
%	- le nombre de pions jaunes
%	[NJoueur,NbBlanc,NbRouge,NbJaune]
%
% Heuristique
%	Une liste de deux elements :
%	- une valeur heuristique
%	- un plateau de jeu
%	- le coup joue
%


taillePlateau(3).


%
% Indique si les coord. sont valides
%
caseValide(X,Y):-
	X >= 0,
	X < 4,
	Y >= 0,
	Y < 4.

%
% Predicats permettant de calculer le nb de cases alignees
%

% A droite de la case %
nbCasesAlignees([Couleur, PosX, PosY], ListeCase, NbAlign, d):-
	NX is PosX + 1,
	member([Couleur, NX, PosY], ListeCase),	
	nbCasesAlignees([Couleur, NX, PosY], ListeCase, NnbAlign, d),
	NbAlign is NnbAlign + 1.

nbCasesAlignees([Couleur, PosX, PosY], ListeCase, NbAlign, d):-
	NX is PosX + 1,
	\+ member([Couleur, NX, PosY], ListeCase),
	NbAlign is 1.

% En bas de la case %
nbCasesAlignees([Couleur, PosX, PosY], ListeCase, NbAlign, b):-
	NY is PosY - 1,
	member([Couleur, PosX, NY], ListeCase),
	nbCasesAlignees([Couleur, PosX, NY], ListeCase, NnbAlign, b),
	NbAlign is NnbAlign + 1.

nbCasesAlignees([Couleur, PosX, PosY], ListeCase, NbAlign, b):-
	NY is PosY - 1,
	\+ member([Couleur, PosX, NY], ListeCase),
	NbAlign is 1.


% Diagonale bas - gauche %
nbCasesAlignees([Couleur, PosX, PosY], ListeCase, NbAlign, bg):-
	NX is PosX - 1,
	NY is PosY - 1,
	member([Couleur, NX, NY], ListeCase),
	nbCasesAlignees([Couleur, NX, NY], ListeCase, NnbAlign, bg),
	NbAlign is NnbAlign + 1.

nbCasesAlignees([Couleur, PosX, PosY], ListeCase, NbAlign, bg):-
	NX is PosX - 1,
	NY is PosY - 1,
	\+ member([Couleur, NX, NY], ListeCase),
	NbAlign is 1.


% Diagonale bas - droite  %
nbCasesAlignees([Couleur, PosX, PosY], ListeCase, NbAlign, bd):-
	NX is PosX + 1,
	NY is PosY - 1,
	member([Couleur, NX, NY], ListeCase),
	nbCasesAlignees([Couleur, NX, NY], ListeCase, NnbAlign, bd),	
	NbAlign is NnbAlign + 1.

nbCasesAlignees([Couleur, PosX, PosY], ListeCase, NbAlign, bd):-
	NX is PosX + 1,
	NY is PosY - 1,
	\+ member([Couleur, NX, NY], ListeCase),
	NbAlign is 1.


%
% Pour une case donnee, calcule le nombre d'alignements de N pions dont elle fait partie, selon
% les directions passÃÂ©es en parametres
%

alignementNPions(_, _, 0, [], _).

alignementNPions(Case, ListeCase, NbAlign, [DC|Dirs], N):-
	nbCasesAlignees(Case, ListeCase, NbCases, DC),
	NbCases = N, 
	alignementNPions(Case, ListeCase, NnbAlign, Dirs, N),
	NbAlign is NnbAlign + 1,
	!.

alignementNPions(Case, ListeCase, NbAlign, [_DC|Dirs], N):-
	alignementNPions(Case, ListeCase, NbAlign, Dirs, N).

%
% PrÃ©dicat permettant de calculer le nombre d'alignements de N pions pour une couleur
%
nbAlignementsNPions(_, [], 0, _).

nbAlignementsNPions(Couleur, [[Couleur,X,Y] | LC], Aligns, N):-
	alignementNPions([Couleur,X,Y], LC, NbCasesAlign, [d,b,bg,bd], N),
	nbAlignementsNPions(Couleur, LC, NextAlign, N),
	Aligns is NbCasesAlign + NextAlign,
	!.

nbAlignementsNPions(Couleur, [Case | LC], Aligns, N):-
	nbAlignementsNPions(Couleur, LC, Aligns, N).

%
% Predicat principal
% @param Plateau : l'ÃÂ©tat actuel du plateau :  [[Pion, PosX, PosY], [...]]
% @param ReserveIA : l'ÃÂ©tat de la reserve de l'IA : [Couleur, NbPionsB, NbPionsR, NbPionsJ] 
% @param ReserveAlgo : l'ÃÂ©tat de la reserve de l'adversaire [Couleur, NbPionsB, NbPionsR, NbPionsJ] 
% @param Coup : le resultat (Type de coup / Case ) [Type, [Pion, PosX, PosY]]
%


calculCoup(Plateau,ReserveIA, ReserveAdv, Coup) :-
    Coup = [0,[1,0,0]].
    
    
    
%
% TODO
% appliqueMiniMax
% meilleurCoup
% c
%
% consult('ia.prolog').
%
evaluer(0).
appliquerMiniMax(_,_,_).
meilleurCoup(_,_).

    
minimax(Plateau,Joueurs, Profondeur, MeilleurCoup) :-
    construireArbre(Plateau, Joueurs, Profondeur, Arbre),
    appliquerMiniMax(Arbre,Profondeur,NvlArbre),
    meilleurCoup(NvlArbre,MeilleurCoup).
    
    
%
% Partie de construction de l'arbre
%
construireArbre(Plateau, [J1, J2], Profondeur, Arbre) :-
    genererArbre([[[-1, Plateau, J2, []]]], [J1, J2], Profondeur, Arbre).
    
% <<<<<<<<<<<<<<<< ****************
tplateau([ [1,0,1] , [2,0,3] , [0,1,0] , [2,1,1] , [1,1,2] , [0,2,1] , [0,2,3] , [2,3,0] , [0,3,2], [1,3,3] ]).
tj1([0,0,4,4]).
tj2([0,8,4,4]).
t(R) :-
    tplateau(Plateau),
    tj1(J1),
    tj2(J2),
    nouveauCoup([0,0,0],C),
    nouvelleEtape(0,Plateau,J2,C,E),
    genererArbre([[E]],[J1,J2],3,R).
% <<<<<<<<<<<<<<<< ****************

% test ~ ok
genererArbre(Arbre,_Joueurs,0,Arbre).
genererArbre(Arbre,[J1, J2],Profondeur,NvlArbre) :-
    Profondeur > 0 ,
    P1 is Profondeur - 1,
    nouvelEtage(Arbre, J1, A1),
    nouvelEtage(A1, J2, A2),
    genererArbre(A2, [J1, J2], P1, NvlArbre).
    
% test ~ ok
nouvelEtage([], _Joueur, []).
nouvelEtage([Chemin | LChemin ], Joueur, ArbreFinal) :-
    genererFils(Chemin, Joueur, NvxChemin),
    append(NvxChemin, NvlArbre, ArbreFinal), 
    nouvelEtage(LChemin, Joueur, NvlArbre).
  
% test ~ ok  
genererFils([Etape | LEtape], Joueur, NvxChemin) :-
    listerCoups(Etape,Joueur,LCoup),
    creerChemins(LCoup, [Etape | LEtape], Joueur, NvxChemin).


% test ok
listerCoups([_Hval, Plateau, _PrecJoueur, _PrecCoup], Joueur, LCoup) :-
    taillePlateau(N),
    creerListe(N,N,L),
    reverse(L,Liste),
    corrigerListe(Liste,Plateau,Joueur,LCoup).

% test ok
creerListe(-1, _, []).
creerListe(Nl, Nc, ListeFinale) :-
    Nl >= 0,
    L1 is Nl - 1,
    creerLigne(Nl,Nc,Ligne),
    append(Ligne,Liste,ListeFinale),
    creerListe(L1,Nc,Liste).
    
% test ok
creerLigne(_,-1,[]).
creerLigne(Nl,Nc,[[0,Nl,Nc] | Ligne]) :-
    Nc >= 0,
    C1 is Nc - 1,
    creerLigne(Nl,C1,Ligne).

% test ok
corrigerListe([],[],_,[]).
% Meme case -> ajout de pion possible
corrigerListe([Case1 | LCase1], [Case2 | LCase2], Joueur, ListeFinale) :-
    memeCase(Case1,Case2),
    isPionBlanc(Case2),
    hasPionColore(Joueur),
    jouerPionColore(Case1, Joueur, LCase),
    append(LCase,ListeCorrigee,ListeFinale),
    !,
    corrigerListe(LCase1, LCase2, Joueur, ListeCorrigee).
% Meme case -> ajout de pion impossible ( \+ isPion blanc ou \+ hasPionColore
corrigerListe([Case1 | LCase1], [Case2 | LCase2], Joueur, ListeCorrigee) :-
    memeCase(Case1,Case2),
    !,
    corrigerListe(LCase1, LCase2, Joueur, ListeCorrigee).
% Case avant -> ajout de pion possible
corrigerListe([Case1 | LCase1], [Case2 | LCase2], Joueur, [Case1 | ListeCorrigee]) :-
    caseAvant(Case1, Case2),    
    hasPionBlanc(Joueur),
    !,
    corrigerListe(LCase1, [Case2 | LCase2], Joueur, ListeCorrigee).
% Case avant -> ajout de pion impossible (\+ hasPionBlanc)
corrigerListe([_ | LCase1], Plateau, Joueur, ListeCorrigee ) :-
    corrigerListe(LCase1, Plateau, Joueur, ListeCorrigee).
    
    
% test ok
caseAvant([_,L1,_],[_,L2,_]) :- L1 < L2.
caseAvant([_,L,C1],[_,L,C2]) :- C1 < C2.
    
% test ok
memeCase([_,L,C],[_,L,C]).

% test ok
isPionBlanc([P,_,_]) :- blanc(P).

% test ok
hasPionBlanc([_,Bl,_,_]) :- Bl > 0.

% test ok
hasPionColore([_,_,Ro,_]) :- Ro > 0.
hasPionColore([_,_,_,Ja]) :- Ja > 0.

% test ok
jouerPionColore([_,L,C],[_,_,Ro,0],[[R,L,C]]) :- 
    Ro > 0,
    rouge(R).
jouerPionColore([_,L,C],[_,_,0,Ja],[[J,L,C]]) :- 
    Ja > 0,
    jaune(J).
jouerPionColore([_,L,C],[_,_,Ro,Ja],[[R,L,C],[J,L,C]]):-
    Ro > 0,
    rouge(R),
    Ja > 0,
    jaune(J).


% test ~ ok
creerChemins([],_,_,[]).
creerChemins([Case | LCase], Chemin, Joueur, [NvChemin | NvxChemin]) :-
    nouveauChemin(Case,Chemin,Joueur,NvChemin),    
    creerChemins(LCase,Chemin,Joueur,NvxChemin).
    
% test ok
nouveauChemin(Case,[Etape | LEtape],Joueur,[NvEtape, Etape | LEtape]) :-
    nouveauPlateau(Case, Etape, NvPlateau),
    nouveauJoueur(Case,Joueur,NvJoueur),
    evaluer(HVal),
    nouveauCoup(Case,NvCoup),
    nouvelleEtape(HVal,NvPlateau,NvJoueur,NvCoup,NvEtape).
    
% test ok
nouveauPlateau(Case, [_,Plateau,_,_], NvPlateau) :-
    ajouterCase(Case,Plateau,NvPlateau).
    
% test ok
ajouterCase(Case1, [Case2 | LCase2], [Case2 | NvPlateau]) :- 
    caseApres(Case1,Case2),
    ajouterCase(Case1, LCase2, NvPlateau).
ajouterCase(Case1, [Case2 | LCase2], [Case1, Case2 | LCase2]) :-
    caseAvant(Case1, Case2),
    isPionBlanc(Case1).
ajouterCase(Case1, [Case2 | LCase2], [Case1 | LCase2]) :-
    memeCase(Case1,Case2),
    isPionColore(Case1).
    
% test ok
isPionColore([P,_,_]) :- rouge(P).
isPionColore([P,_,_]) :- jaune(P).

% test ok
nouveauJoueur([P,_,_],[Id,Bl,Ro,Ja],[Id,NBl,Ro,Ja]) :- blanc(P), NBl is Bl - 1.
nouveauJoueur([P,_,_],[Id,Bl,Ro,Ja],[Id,Bl,NRo,Ja]) :- rouge(P), NRo is Ro - 1.
nouveauJoueur([P,_,_],[Id,Bl,Ro,Ja],[Id,Bl,Ro,NJa]) :- jaune(P), NJa is Ja - 1.

% test ok
nouveauCoup(Case,[0,Case]).

% test ok
nouvelleEtape(HVal,Plateau,Joueur,Coup,[HVal,Plateau,Joueur,Coup]).    

% test ok
caseApres(Case1,Case2) :- caseAvant(Case2,Case1).
    




    
%
% Partie d'application du minimax
%

    
 
 
%
% TODO
% 
% RÃÂ©flexions sur :
% - l'ÃÂ©valutation d'un plateau
% - dÃÂ©terminer le type d'un coup
%

% consult('ia.prolog').
 
 
%
% GÃÂ©nÃÂ©ration de tous les coups jouables depuis un plateau
% FaÃÂ§on Backtrack
%
% Exemple d'un plateau
%
%	|    | 0 | 1 | 2 | 3 |
%	|    |   |   |   |   |
%	| 0  | x | 1 | x | 2 | 
%	| 1  | 0 | 2 | 1 | x |
%	| 2  | x | 0 | x | 0 |
%	| 3  | 2 | x | 0 | 1 |
%
% plateau = [ [1,0,1] , [2,0,3] , [0,1,0] , [2,1,1] , [1,1,2] , [0,2,1] , [0,2,3] , [2,3,0] , [0,3,2], [1,3,3] ]
%
%


%
% construireListe(
%	NLigne		Numero de la derniere ligne
%	NColonne	Numero de la derniere colonne
%	Accumulateur	Liste de cases
%	Resultat	Liste de cases
% 	)
%
% Construit les cases du plateau dans l'ordre croissant*, en iterant sur un prÃÂ©dicat de construction d'une ligne.
% *Ordre croissant : [[_,0,0] , [_,0,1] , [_,1,0]] 
%
construireListe(-1,_,Acc,Acc).
construireListe(L,C,Acc,R) :-
    construireLigne(L,C,[],R1),
    append(R1,Acc,Acc1),
    L >= 0,
    L1 is L - 1,
    construireListe(L1,C,Acc1,R).
 
%
% construireLigne(
%	NLigne		Numero de la ligne
%	NColonne	Numero de la colonne
%	Accumulateur	Liste de cases
%	Resultat	Liste de cases
%
% Construit une ligne du plateau, dans l'ordre croissant*.
% *Ordre croissant : [[_,0,0] , [_,0,1] , [_,1,0]] 
%
construireLigne(L, 0, Acc, [[-1,L,0]|Acc]).
construireLigne(L, C, Acc, R) :-
    C > 0,
    C1 is C - 1,
    construireLigne(L, C1, [[-1,L,C]|Acc], R).


%
% corrigeListe(
%	Plateau		Liste de cases -> Un plateau de jeu
%	Liste		Liste de cases -> Une liste de case gÃÂ©nÃÂ©rÃÂ©es
%	Joueur		Le joueur actuel
%	Accumulateur	Liste de cases 
%	Resultat	Liste de cases -> Liste des coups jouables pour le joueur 
%	)
%
% Corrige la liste generee en etant les cases contenant deja des pions colores
% et en choissisant un pion colore pour les cases contenant un pion blanc.
%
corrigeListe(_,[],_,Acc,Acc).


% Meme case -> pion blanc prÃÂ©sent -> poser un pion colorÃÂ©
corrigeListe([[P,L,C] | Plateau], [[_,L,C] | Liste], Joueur, Acc, R) :-
    \+ pionColore(P),
    choixRouge(L,C,Joueur,CaseRouge),
    choixJaune(L,C,Joueur,CaseJaune),
    corrigeListe(Plateau,Liste,Joueur,[CaseJaune,CaseRouge | Acc], R).
 
% Meme case -> Autres cas (pion colorÃÂ© prÃÂ©sent / pas de pion colorÃÂ© en rÃÂ©serve)
corrigeListe([[P,L,C] | Plateau], [[_,L,C] | Liste], Joueur, Acc, R) :-
    pionColore(P),
    corrigeListe(Plateau,Liste,Joueur,Acc,R).
    
% Autre cas -> poser un pion blanc
corrigeListe([[P,L1,C1] | Plateau], [[_,L2,C2] | Liste], Joueur, Acc, R) :-   
    \+ memeCase(L1,C1,L2,C2),
    choixBlanc(L2,C2,Joueur,CaseBlanche),
    corrigeListe([[P,L1,C1] | Plateau], Liste, Joueur, [CaseBlanche | Acc], R).





%
% choix<TypePion>(
%	Ligne		Ligne de la future case
%	Colonne		Colonne de la future case
%	Joueur		Joueur concernÃÂ©
%	Case		La future case
%	)
%
% Verification du nombre de pions du type concernÃÂ©
% Envoi d'une case vide si le joueur n'a plus du pion demandÃÂ©
% Envoi d'une case [<TypePion>,Ligne,Colonne] sinon
%

% <TypePion> = Blanc
choixBlanc(_,_,[_,0,_,_],[]).
choixBlanc(L,C,[_,Bl,_,_],[0,L,C]) :- Bl > 0.

% <TypePion> = Rouge
choixRouge(_,_,[_,_,0,_],[]).
choixRouge(L,C,[_,_,Ro,_],[1,L,C]) :- Ro > 0.
    
% <TypePion> = Jaune
choixJaune(_,_,[_,_,_,0],[]).
choixJaune(L,C,[_,_,_,Ja],[2,L,C]) :- Ja > 0.

%
% pionColore(
%	Pion	<TypePion> = 0 (Blanc), 1(Rouge) ou 2(Jaune)
%	)
%
% Fail si le pion est blanc
% RÃÂ©ussi sinon
%
pionColore(N) :- rouge(N). 
pionColore(N) :- jaune(N).


%
% memeCase(
%	Ligne1		Ligne de la case 1
%	Colonne1	Colonne de la case 1
%	Ligne1		Ligne de la case 2
%	Colonne1	Colonne de la case 2
%	)
%
% RÃÂ©ussi si les les lignes 1 et 2 sont identiques
% ET si les colonnes 1 et 2 sont identiques.
% Echoue sinon.
%
memeCase(L,C,L,C).


%
% nettoieListe(
%	ListeSale,	Liste de cases pouvant contenir des cases vides
%	ListePropre	Liste de cases sans cases vides
%	)
%
% Supprime les cases vides d'une liste
%
nettoieListe([],[]). % Arret
nettoieListe([[P,L,C]|LC], [[P,L,C]|R]) :- % Cas des cases non vides
    P >= 0,
    L >= 0,
    C >= 0,
    !,
    nettoieListe(LC,R).
nettoieListe([_|LC],R) :- % Cas des cases vides
    nettoieListe(LC,R).
    

    
%
% majJoueur(
%	Case,		Case du coup ÃÂ  jouer
%	Joueur,		Joueur avant le coup (N pions)
%	NvJoueur	Joueur apres le coup (N - 1 pions)
%	)
%
% Ote le pion de la case au joueur concernÃÂ©.
% Retourne le joueur modifiÃÂ©.
%
majJoueur([0,_,_],[Id,Bl,Ro,Ja],[Id,NBl,Ro,Ja]) :- Bl > 0, NBl is Bl - 1.
majJoueur([1,_,_],[Id,Bl,Ro,Ja],[Id,Bl,NRo,Ja]) :- Ro > 0, NRo is Ro - 1.
majJoueur([2,_,_],[Id,Bl,Ro,Ja],[Id,Bl,Ro,NJa]) :- Ja > 0, NJa is Ja - 1.
    
%
% creePlateau(
%	Case		Case ÃÂ  mettre ÃÂ  jour
%	Plateau		Plateau actuel
%	NvPlateau	Plateau avec la nouvelle case
%	)
%
% Place la case dans une copie du plateau.
% Retourne le plateau modifiÃÂ©.
% 

% Si la case 1 est aprÃÂ¨s la case 2
% -> on avance ÃÂ  la prochaine case occupÃÂ©e du plateau
creePlateau([P1,L1,C1],[[P2,L2,C2]|LC],[[P2,L2,C2]|NvPlt]) :-
    caseApres(L1,C1,L2,C2),
    creePlateau([P1,L1,C1],LC,NvPlt).
% Si la case 1 est avant la case 2 (cas du pion blanc)
% -> on insÃÂ¨re la case 1 avant la case 2
creePlateau([0,L1,C1],[[P2,L2,C2]|LC],[[0,L1,C1],[P2,L2,C2]|LC]) :-
    caseAvant(L1,C1,L2,C2).
% Si la case 1 est la mÃÂªme que la case 2 (cas du pion colorÃÂ©)
% -> on met ÃÂ  jour la case avec le nouveau pion
creePlateau([P1,L,C],[[_,L,C]|LC],[[P1,L,C]|LC]) :- pionColore(P1).


%
% case<Where>(
%	Ligne 1,	Ligne de la case 1
%	Colonne 1,	Colonne de la case 1
%	Ligne 2,	Ligne de la case 2
%	Colonne 2	Colonne de la case 2
%	)
%
% DÃÂ©termine la position d'une case par rapport ÃÂ  une autre case.
%
% <Where> = Apres
%caseApres(L1,_,L2,_) :- L1 > L2.
%caseApres(L,C1,L,C2) :- C1 > C2.
% <Where> = Avant
%caseAvant(L1,C1,L2,C2) :- caseApres(L2,C2,L1,C1).
    


test2(N,R,P) :-
    construireListe(N,N,[],Liste),
    Plateau = [ [1,0,1] , [2,0,3] , [0,1,0] , [2,1,1] , [1,1,2] , [0,2,1] , [0,2,3] , [2,3,0] , [0,3,2], [1,3,3] ],
    Joueur = [0,1,0,0],
    corrigeListe(Plateau,Liste,Joueur,[],R1),
    nettoieListe(R1,R2),
    reverse(R2,R),
    creeListePlateau(R,Plateau,Joueur,P).
    
    
test(P,N) :-
    Hval = -1,
    Plateau = [ [1,0,1] , [2,0,3] , [0,1,0] , [2,1,1] , [1,1,2] , [0,2,1] , [0,2,3] , [2,3,0] , [0,3,2], [1,3,3] ],
    Joueurs = [[0,2,3,6],[1,3,2,6]],
    Coup = [-1,[]],
    H = [[[Hval,Plateau,[1,3,2,6],Coup]]],
    profondeur(N,H,Joueurs,P).
    
    
profondeur(0,LChemin,_,LChemin).
profondeur(N,LChemin,[J1,J2],R) :-
    N > 0,
    N1 is N - 1,
    nouvelEtage(LChemin,J1,[],R1),
    nouvelEtage(R1,J2,[],NLChemin),
    !,
    profondeur(N1,NLChemin,[J1,J2],R).
    
    

    
%
% nouvelEtage(
%	LLH	Liste de tous les chemins -> la suite des plateaux/coups
%	LLH	Nouvelle liste de tous les chemins 
%	)
%
nouvelEtage([],_,Acc,Acc).
nouvelEtage([Chemin | LChemin],Joueur,Acc,NvLChemin) :-
    genererCoups(Chemin,Joueur,NvxChemins),
    append(NvxChemins,Acc,Acc1),
    nouvelEtage(LChemin,Joueur,Acc1,NvLChemin).
    
    
genererCoups([[HVal,Plateau,PrecJoueur,Coup]|LEtape],[_,0,0,0],[[[],[HVal,Plateau,PrecJoueur,Coup]|LEtape]]).
genererCoups([[HVal,Plateau,PrecJoueur,Coup]|LEtape],Joueur,NvxChemins) :-
    peutJouer(Joueur),
    taillePlateau(N),
    construireListe(N,N,[],Liste),
    corrigeListe(Plateau,Liste,Joueur,[],ListeCorr),
    nettoieListe(ListeCorr,ListeNet),
    reverse(ListeNet,ListeRev),
    creeListePlateau(ListeRev,[[HVal,Plateau,PrecJoueur,Coup]|LEtape],Joueur,NvxChemins).
    
peutJouer([_,Bl,_,_]) :- Bl > 0.
peutJouer([_,_,Ro,_]) :- Ro > 0.
peutJouer([_,_,_,Ja]) :- Ja > 0.


%
% creeListePlateau(
%	ListeCoups	Liste de cases -> les coups possibles sur le plateau actuel
%	Chemin		Liste des ÃÂ©tapes -> les coups jouÃÂ©s jusqu'ÃÂ  maintenant
%	ListePlateau	Liste de plateaux -> chaque plateau doit avoir un valeur
%	)
%
creeListePlateau([],_,[]).
creeListePlateau(
	[C|LC],
	[[HVal,Plateau,PrecJoueur,Coup]|LH],
	Joueur,
	[ [ [-1,Plt,NJ,[0,C]], [HVal,Plateau,PrecJoueur,Coup] | LH ] | LP ]
	) :-
    creePlateau(C,Plateau,Plt),
    majJoueur(C,Joueur,NJ),
    creeListePlateau(LC,[[HVal,Plateau,PrecJoueur,Coup]|LH],Joueur,LP).


%
% Fonction d'évaluation du plateau
% @param Plateau : l'état du plateau de jeu
% @param Joueur : le joueur de reference pour lequel on calcule l'heuristique
% @param Adv : le joueur adverse
% @result H : l'heuristique calculee

%
% Cas de fin de partie avec alignement de 4
%
eval(Plateau, Joueur, Adv, H):-
	nbAlignementsNPions(Joueur, Plateau, AlignJ, 4),
	AlignJ =\= 0,
	H is 1000,
	!.

% Cas de fin de partie sans alignements de 4 (les joueurs ne peuvent plus jouer)
% => On calcule le nombre d'alignements de 3 pions des deux joueurs et on retourne :
%  * H = 1000 si Joueur gagnant
%  * H = -1000 si Joueur perdant
%  * H = 0 si match nul
eval(Plateau, Joueur, Adv,  H):-
	isPartieFinie(Plateau),
	nbAlignementsNPions(Joueur, Plateau, AlignJ, 3),
	nbAlignementsNPions(Adv, Plateau, AlignAdv, 3),
	((AlignJ > AlignAdv, H is 1000);
	(AlignJ < AlignAdv, H is -1000);
	(AlignJ = AlignAdv, H is 0)),
	!.

% Cas en cours de partie 
% => Pour chaque alignement de 2 pions de la meme couleur : on ajoute (joueur de ref) / supprime (adversaire) 5
% => Pour chaque alignement de 3 pions de la meme couleur : on ajoute / supprime 10
% => Pour chaque pion blanc aligné à un pion d'un joueur : on ajoute / supprime 3
eval(Plateau, Joueur, Adv, H):-
	nbAlignementsNPions(Joueur, Plateau, AlignJD, 2),
	nbAlignementsNPions(Adv, Plateau, AlignAdvD, 2),
	nbAlignementsNPions(Joueur, Plateau, AlignJT, 3),
	nbAlignementsNPions(Adv, Plateau, AlignAdvT, 3),
	H is (AlignJD * 5 - AlignAdvD * 5) + (AlignJT * 10 - AlignAdvT * 10).


%
% Predicat permettant de vérifier si la partie est finie
% => Les 16 cases sont occupees par des pions non blancs
%
isPartieFinie(Plateau):-
	length(Plateau, Length),
	Length = 16,
	\+ member([0, _, _], Plateau).
