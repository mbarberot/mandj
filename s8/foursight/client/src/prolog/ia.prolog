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
%	- le numero du joueur : 1 = l'IA, 2 = l'adversaire
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

nbAlignementsNPions(Couleur, [_Case | LC], Aligns, N):-
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

    
minimax(Plateau,Joueurs,Couleur,Profondeur, MeilleurCoup) :-
    construireArbre(Plateau, Joueurs, Couleur, Profondeur, Arbre),
    appliquerMiniMax(Arbre,Profondeur,NvlArbre),
    meilleurCoup(NvlArbre,MeilleurCoup).
    
    
%
% Partie de construction de l'arbre
%
construireArbre(Plateau, [J1, J2], Couleur, Profondeur, Arbre) :-
    genererArbre([[[-1, Plateau, J2, []]]], [J1, J2], Couleur, Profondeur, Arbre).
    
% <<<<<<<<<<<<<<<< ****************
tplateau([]).
tj1([1,8,4,4]).
tj2([2,8,4,4]).
t(R) :-
    tplateau(Plateau),
    tj1(J1),
    tj2(J2),
    nouveauCoup([0,0,0],C),
    nouvelleEtape(0,Plateau,J2,C,E),
    genererArbre([[E]],[J1,J2],1,1,R).
% <<<<<<<<<<<<<<<< ****************

% test ~ ok
genererArbre(Arbre,_Joueurs, _Couleur,0,Arbre).
genererArbre(Arbre,[J1, J2], Couleur,Profondeur,NvlArbre) :-
    Profondeur > 0 ,
    P1 is Profondeur - 1,
    nouvelEtage(Arbre, J1, Couleur, A1),
    nouvelEtage(A1, J2, Couleur, A2),
    genererArbre(A2, [J1, J2], Couleur, P1, NvlArbre).
    
% test ~ ok
nouvelEtage([], _Joueur, _Couleur, []).
nouvelEtage([Chemin | LChemin ], Joueur, Couleur, ArbreFinal) :-
    genererFils(Chemin, Joueur, Couleur, NvxChemin),
    append(NvxChemin, NvlArbre, ArbreFinal), 
    nouvelEtage(LChemin, Joueur, Couleur, NvlArbre).
  
% test ~ ok  
genererFils([Etape | LEtape], Joueur, Couleur, NvxChemin) :-
    listerCoups(Etape,Joueur,LCoup),
    creerChemins(LCoup, [Etape | LEtape], Joueur, Couleur, NvxChemin).


% test ok
listerCoups([_Hval, Plateau, _PrecJoueur, _PrecCoup], Joueur, LCoup) :-
    creerListe(0,0,Liste),
    corrigerListe(Liste,Plateau,Joueur,LCoup).

% test ok
creerListe(N, _, []) :- taillePlateau(NMax), N is NMax + 1. 
creerListe(Nl, Nc, ListeFinale) :-
    taillePlateau(NMax),
    Nl =< NMax,
    L1 is Nl + 1,
    creerLigne(Nl,Nc,Ligne),
    append(Ligne,Liste,ListeFinale),
    creerListe(L1,Nc,Liste).
    
% test ok
creerLigne(_,N,[]) :- taillePlateau(NMax), N is NMax +1.
creerLigne(Nl,Nc,[[0,Nc,Nl] | Ligne]) :-
    taillePlateau(NMax),
    Nc =< NMax,
    C1 is Nc + 1,
    creerLigne(Nl,C1,Ligne).

% test ok
corrigerListe(LCase,[],_,LCase) :- !.
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
caseAvant([_,_,L1],[_,_,L2]) :- L1 < L2.
caseAvant([_,C1,L],[_,C2,L]) :- C1 < C2.
    
% test ok
memeCase([_,C,L],[_,C,L]).

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
creerChemins([],_,_,_,[]).
creerChemins([Case | LCase], Chemin, Joueur, Couleur, [NvChemin | NvxChemin]) :-
    nouveauChemin(Case, Chemin, Joueur, Couleur, NvChemin),    
    creerChemins(LCase,Chemin,Joueur,Couleur,NvxChemin).
    
    
tt(P) :-
    tplateau(Plateau),
    nouveauChemin([1,2,3],[[0,Plateau,[2,7,4,4],[0,[0,0,0]]]],[1,8,4,4],1,P).
	

% test ok
nouveauChemin(Case, [Etape | LEtape], Joueur, Couleur, [NvEtape, Etape | LEtape]) :-
    nouveauPlateau(Case, Etape, NvPlateau),
    nouveauJoueur(Case,Joueur,NvJoueur),
    evaluer(Etape,Joueur,Couleur,HVal),
    nouveauCoup(Case,NvCoup),
    nouvelleEtape(HVal,NvPlateau,NvJoueur,NvCoup,NvEtape).
    
evaluer([_,Plateau,_,_],Joueur,Couleur,H) :-
    isJoueur(Joueur,Couleur),
    assignerCouleurs(Couleur,IdJoueur,IdAdv),
    eval(Plateau, IdJoueur, IdAdv, H).
    
assignerCouleurs(Couleur,Couleur,IdAdv) :-
    couleur(Couleur,IdAdv).

isJoueur([Couleur,_,_,_],Couleur).
couleur(Couleur,Adv) :- rouge(Couleur), jaune(Adv).
couleur(Couleur,Adv) :- jaune(Couleur), rouge(Adv).

    
    
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
% Fonction d'évaluation du plateau
% @param Plateau : l'état du plateau de jeu
% @param Joueur : le joueur de reference pour lequel on calcule l'heuristique
% @param Adv : le joueur adverse
% @result H : l'heuristique calculee

%
% Cas de fin de partie avec alignement de 4
%
eval(Plateau, Joueur, _Adv, H):-
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
