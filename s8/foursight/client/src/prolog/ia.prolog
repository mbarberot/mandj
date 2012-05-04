% Outils de liste %
:-set_prolog_flag(toplevel_print_options, [max_depth(0)]).
:-use_module(library(lists)).

%
% Plateau
%	Liste des cases occupÃ©es du jeu.
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
%
% Pion
%	Valeur entiere telle que : 
%	0 = Blanc
%	1 = Rouge
%	2 = Jaune
%
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
	NbAlign is NbAlign + 1,
	nbCasesAlignees([Couleur, NX, PosY], ListeCase, NbAlign, d).

% A gauche de la case %
nbCasesAlignees([Couleur, PosX, PosY], ListeCase, NbAlign, g):-
	NX is PosX - 1,
	member([Couleur, NX, PosY], ListeCase),
	NbAlign is NbAlign + 1,
	nbCasesAlignees([Couleur, NX, PosY], ListeCase, NbAlign, g).

% En haut de la case %
nbCasesAlignees([Couleur, PosX, PosY], ListeCase, NbAlign, h):-
	NY is PosY + 1,
	member([Couleur, PosX, NY], ListeCase),
	NbAlign is NbAlign + 1,
	nbCasesAlignees([Couleur, PosX, NY], ListeCase, NbAlign, h).

% En bas de la case %
nbCasesAlignees([Couleur, PosX, PosY], ListeCase, NbAlign, b):-
	NY is PosY - 1,
	member([Couleur, PosX, NY], ListeCase),
	NbAlign is NbAlign + 1,
	nbCasesAlignees([Couleur, PosX, NY], ListeCase, NbAlign, b).

% Diagonale haut-gauche %
nbCasesAlignees([Couleur, PosX, PosY], ListeCase, NbAlign, hg):-
	NX is PosX - 1,
	NY is PosY + 1,
	member([Couleur, NX, NY], ListeCase),
	NbAlign is NbAlign + 1,
	nbCasesAlignees([Couleur, NX, NY], ListeCase, NbAlign, hg).

% Diagonale bas - gauche %
nbCasesAlignees([Couleur, PosX, PosY], ListeCase, NbAlign, bg):-
	NX is PosX - 1,
	NY is PosY - 1,
	member([Couleur, NX, NY], ListeCase),
	NbAlign is NbAlign + 1,
	nbCasesAlignees([Couleur, NX, NY], ListeCase, NbAlign, bg).

% Diagonale haut - droite %
nbCasesAlignees([Couleur, PosX, PosY], ListeCase, NbAlign, hd):-
	NX is PosX + 1,
	NY is PosY + 1,
	member([Couleur, NX, NY], ListeCase),
	NbAlign is NbAlign + 1,
	nbCasesAlignees([Couleur, NX, NY], ListeCase, NbAlign, hd).

% Diagonale bas - droite  %
nbCasesAlignees([Couleur, PosX, PosY], ListeCase, NbAlign, bd):-
	NX is PosX + 1,
	NY is PosY - 1,
	member([Couleur, NX, NY], ListeCase),
	NbAlign is NbAlign + 1,
	nbCasesAlignees([Couleur, NX, NY], ListeCase, NbAlign, bd).
%
% Pour une case donnee, calcule le nombre d'alignements dont elle fait partie, selon
% les directions passées en parametres
%

alignementTroisPions(_Case, _ListeCase, 1, []).

alignementTroisPions(Case, ListeCase, NbAlign, [DC|Dirs]):-
	nbCasesAlignees(Case, ListeCase, NbCases, DC),
	NbCases = 3,
	NbAlign is NbAlign + 1,
	alignementTroisPions(Case, ListeCase, NbAlign, Dirs).

alignementTroisPions(Case, ListeCase, NbAlign, [DC|Dirs]):-
	alignementTroisPions(Case, ListeCase, NbAlign, Dirs).

%
% Prédicat permettant de calculer le nombre d'alignements de 3 pions pour une couleur
%
nbAlignementsTroisPions(_Couleur, [], _NbAlign).

nbAlignementsTroisPions(Couleur, [Current|LC], NbAlign):-
	Dir = [d, g, h, b, hg, bg, hd, bd],
	alignementTroisPions(Current, LC, AlignCurrent, Dir),
	NbAlign is NbAlign + AlignCurrent ,
	nbAligementsTroisPions(Couleur, LC, NbAlign).

%
% Predicat principal
% @param Plateau : l'état actuel du plateau :  [[Pion, PosX, PosY], [...]]
% @param ReserveIA : l'état de la reserve de l'IA : [Couleur, NbPionsB, NbPionsR, NbPionsJ] 
% @param ReserveAlgo : l'état de la reserve de l'adversaire [Couleur, NbPionsB, NbPionsR, NbPionsJ] 
% @param Coup : le resultat (Type de coup / Case ) [Type, [Pion, PosX, PosY]]
%


calculCoup(Plateau,ReserveIA, ReserveAdv, Coup) :-
    Coup = [0,[1,0,0]].
    
    
    
    
    
minimax(Plateau,Joueurs,MeilleurCoup):-
    genererJ1([[-1,[],Plateau]],Joueurs,[],LLH),
    evaluer().
    min(),
    max().
    
    
genererJ1(_,_,R,R).
genererJ1([H|LH],[J1,J2],Acc,R):-
    genererCoup(H,J1,NH),
    genererJ2([NH,H|LH],J2,[],NLH),
    genererJ1([H|LH],[J1,J2],[NLH|Acc],R).
    
genererJ2(_,_,R,R).
genererJ2([H|LH],J2,Acc,R):-
    genererCoup(H,J2,NH),
    genererJ2([H|LH],J2,[[NH,H|LH]|Acc],R).


genererCoup([_,_,Plateau],Joueur,[-1,[Type,Case],NP]) :-
    trouverCase(Case,Joueur),
    trouverType(Type),
    typeValide(),
    nouveauPlateau(Plateau,Type,Case,NP).

trouverCase([Pion,Ligne,Colonne]) :-
    trouverLigne(Ligne),
    trouverColonne(Colonne),
    trouverPion(Pion)
    caseValide([Pion,Ligne,Colonne]).
    
trouverLigne(0).
trouverLigne(1).
trouverLigne(2).
trouverLigne(3).
trouverColonne(0).
trouverColonne(1).
trouverColonne(2).
trouverColonne(3).
trouverPion(0).
trouverPion(1).
trouverPion(2).

caseValide().
trouverType().
typeValide().
nouveauPlateau().

evaluer().
min().
max().

