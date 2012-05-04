% Outils de liste %
:-set_prolog_flag(toplevel_print_options, [max_depth(0)]).
:-use_module(library(lists)).

%
% Plateau
%	Liste des cases occupées du jeu.
%	[Case, Case, ...]
%
% Case 
%	Une liste des 3 éléments suivants :
%	- type du pion sur la case (un considère un pion vide)
%	- coordonnée en x
%	- coordonnée en y
%	[Pion, PosX, PosY]
%
% Coup
%	Une liste des 2 éléments suivants
%	- type du coup
%	- la case du coup
%	[Type,Case]
%
% Type
%	Valeur entière telle que :
%	0 = COUP
%	1 = GAGNE
%	2 = PERD
%	3 = NULLE
%	4 = PASSE
%
% Pion
%	Valeur entière telle que : 
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
%	Liste de quatres éléments :
%	- le numéro du joueur : 0 = l'IA, 1 = l'adversaire
%	- le nombre de pions blancs
%	- le nombre de pions rouges
%	- le nombre de pions jaunes
%	[NJoueur,NbBlanc,NbRouge,NbJaune]
%
% Heuristique
%	Une liste de deux éléments :
%	- une valeur heuritstique
%	- un plateau de jeu
%	- le coup joué
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
% Pour une case donn�e, calcule le nombre d'alignements dont elle fait partie, selon
% les directions pass�es en param�tres
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
% Pr�dicat permettant de calculer le nombre d'alignements de 3 pions pour une couleur
%
nbAlignementsTroisPions(_Couleur, [], _NbAlign).

nbAlignementsTroisPions(Couleur, [Current|LC], NbAlign):-
	Dir = [d, g, h, b, hg, bg, hd, bd],
	alignementTroisPions(Current, LC, AlignCurrent, Dir),
	NbAlign is NbAlign + AlignCurrent ,
	nbAligementsTroisPions(Couleur, LC, NbAlign).

%
% Predicat principal
% @param Plateau : l'�tat actuel du plateau :  [[Pion, PosX, PosY], [...]]
% @param ReserveIA : l'�tat de la reserve de l'IA : [Couleur, NbPionsB, NbPionsR, NbPionsJ] 
% @param ReserveAlgo : l'�tat de la reserve de l'adversaire [Couleur, NbPionsB, NbPionsR, NbPionsJ] 
% @param Coup : le resultat (Type de coup / Case ) [Type, [Pion, PosX, PosY]]
%


calculCoup(Plateau,ReserveIA, ReserveAdv, Coup) :-
    Coup = [0,[1,0,0]].
    