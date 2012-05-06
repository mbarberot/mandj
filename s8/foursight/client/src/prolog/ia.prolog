% Outils de liste %
:-set_prolog_flag(toplevel_print_options, [max_depth(0)]).
:-use_module(library(lists)).

%
% Plateau
%	Liste des cases occupées du jeu.
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

nbCasesAlignees(_,[],0,_).

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

% A gauche de la case %
nbCasesAlignees([Couleur, PosX, PosY], ListeCase, NbAlign, g):-
	NX is PosX - 1,
	member([Couleur, NX, PosY], ListeCase),	
	nbCasesAlignees([Couleur, NX, PosY], ListeCase, NnbAlign, g),
	NbAlign is NnbAlign + 1.

nbCasesAlignees([Couleur, PosX, PosY], ListeCase, NbAlign, g):-
	NX is PosX - 1,
	\+ member([Couleur, NX, PosY], ListeCase),
	NbAlign is 1.

% En haut de la case %
nbCasesAlignees([Couleur, PosX, PosY], ListeCase, NbAlign, h):-
	NY is PosY + 1,
	member([Couleur, PosX, NY], ListeCase),
	nbCasesAlignees([Couleur, PosX, NY], ListeCase, NnbAlign, h),
	NbAlign is NnbAlign + 1.

nbCasesAlignees([Couleur, PosX, PosY], ListeCase, NbAlign, h):-
	NY is PosY + 1,
	\+ member([Couleur, PosX, NY], ListeCase),
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

% Diagonale haut-gauche %
nbCasesAlignees([Couleur, PosX, PosY], ListeCase, NbAlign, hg):-
	NX is PosX - 1,
	NY is PosY + 1,
	member([Couleur, NX, NY], ListeCase),
	nbCasesAlignees([Couleur, NX, NY], ListeCase, NnbAlign, hg),
	NbAlign is NnbAlign + 1.
	
nbCasesAlignees([Couleur, PosX, PosY], ListeCase, NbAlign, hg):-
	NX is PosX - 1,
	NY is PosY + 1,
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
	\+ member([Couleur, PosX, NY], ListeCase),
	NbAlign is 1.

% Diagonale haut - droite %
nbCasesAlignees([Couleur, PosX, PosY], ListeCase, NbAlign, hd):-
	NX is PosX + 1,
	NY is PosY + 1,
	member([Couleur, NX, NY], ListeCase),
	nbCasesAlignees([Couleur, NX, NY], ListeCase, NnbAlign, hd),
	NbAlign is NnbAlign + 1.

nbCasesAlignees([Couleur, PosX, PosY], ListeCase, NbAlign, hd):-
	NX is PosX + 1,
	NY is PosY + 1,
	\+ member([Couleur, PosX, NY], ListeCase),
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
	\+ member([Couleur, PosX, NY], ListeCase),
	NbAlign is 1.
	
%
% Pour une case donnee, calcule le nombre d'alignements de N pions dont elle fait partie, selon
% les directions passées en parametres
%

alignementNPions(_Case, _ListeCase, 0, [], N).

alignementNPions(Case, ListeCase, NbAlign, [DC|Dirs], N):-
	nbCasesAlignees(Case, ListeCase, NbCases, DC),
	NbCases = N,	
	alignementNPions(Case, ListeCase, NnbAlign, Dirs, N),
	NbAlign is NnbAlign + 1.

alignementNPions(Case, ListeCase, NbAlign, [_DC|Dirs], N):-
	alignementNPions(Case, ListeCase, NbAlign, Dirs, N).

%
% Pr�dicat permettant de calculer le nombre d'alignements de N pions pour une couleur
%
nbAlignementsNPions(_, [], 0, _).

nbAlignementsNPions(Couleur, [Case | LC], Aligns, N):-
	alignementNPions(Case, LC, NbCasesAlign, [d,g,b,h,bg,bd,hg,hd], N),
	nbAlignementsNPions(Couleur, LC, NextAlign, N),
	Aligns is NbCasesAlign + NextAlign.

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
    genererJ1([[-1,[],Plateau]],Joueurs,[],LLH). %,
%    evaluer,
%    min,
%    max.
    
    
genererMax(_,_,R,R).
genererMax([H|LH],[J1,J2],Acc,R):-
    genererCoup(H,J1,NH),
    genererMin([NH,H|LH],J2,[],NLH),
    genererMax([H|LH],[J1,J2],[NLH|Acc],R).
    
genererMin(_,_,R,R).
genererMin([H|LH],J2,Acc,R):-
    genererCoup(H,J2,NH),
    genererMin([H|LH],J2,[[NH,H|LH]|Acc],R).


genererCoup([_,_,Plateau],Joueur,[-1,[Type,Case],NP]) :-
    trouverCase(Plateau,Joueur,Case),
    trouverType(Type),
    typeValide,
    nouveauPlateau(Plateau,Type,Case,NP).
    

%
%
%
%
genererCase(Plateau, Joueur, Case) :-
    choixCase(Plateau,Case),
    choixPion(Joueur,Case).
    
    
%
% Une case est valide si :
%	- elle est dans le tableau => vrai par construction
%	- elle n'est pas dans la liste des cases occupées => 'plateau'
%	- c'est une case avec un pion blanc et que l'on veut y placer un pion jaune ou rouge.
%
caseValide([[0,L,C]|LC],[0,L,C]) :- fail.


%
% Un pion est valide si l'utilisateur en a encore
%
pionValide([_,Bl,_,_],[0,_,_]) :- Bl > 0.
pionValide([_,_,Ro,_],[1,_,_]) :- Ro > 0.
pionValide([_,_,_,Ja],[2,_,_]) :- Ja > 0.


%
% Trouve une case dans un damier de 4 x 4 et un pion
% parmis les 3 types de pions possibles
%
trouverCase([Pion,Ligne,Colonne]) :-
    trouverLigne(Ligne),
    trouverColonne(Colonne),
    trouverPion(Pion).    

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


trouverType.
typeValide.
nouveauPlateau.

evaluer.
min.
max.
    
 
 
%
% TODO
% 
% Ajouter une case à un plateau EN RESPECTANT L'ORDRE
% 
% Evaluer un plateau
%

% consult('ia.prolog').
 
 
%
% Génération de tous les coups jouables depuis un plateau
% Façon Backtrack
%
% Exemple d'un plateau
%
%	|    | 0 | 1 | 2 | 3 |
%	|    |   |   |   |   |
%	| 0  | x | x | 0 | x | 
%	| 1  | 2 | x | x | x |
%	| 2  | x | 1 | x | x |
%	| 3  | 2 | x | x | 1 |
%
% plateau = [ [0,0,2] , [2,1,0], [1,2,1], [2,3,0], [1,3,3] ]
%
%

construireListe(-1,_,Acc,Acc).
construireListe(L,C,Acc,R) :-
    construireLigne(L,C,[],R1),
    append(R1,Acc,Acc1),
    L >= 0,
    L1 is L - 1,
    construireListe(L1,C,Acc1,R).
    
construireLigne(L, 0, Acc, [[-1,L,0]|Acc]).
construireLigne(L, C, Acc, R) :-
    C > 0,
    C1 is C - 1,
    construireLigne(L, C1, [[-1,L,C]|Acc], R).


corrigeListe([],_,_,Acc,Acc).
corrigeListe(_,[],_,Acc,Acc).


% Même case -> pion blanc présent -> poser un pion coloré
corrigeListe([[0,L,C] | Plateau], [[_,L,C] | Liste], Joueur, Acc, R) :-
    choixPionColore(Joueur,Pion),
    corrigeListe(Plateau,Liste,Joueur,[[Pion,L,C] | Acc], R).
    
% Même case -> Autres cas (pion coloré présent / pas de pion coloré en réserve)
corrigeListe([[_,L,C] | Plateau], [[_,L,C] | Liste], Joueur, Acc, R) :-
    corrigeListe(Plateau,Liste,Joueur,Acc,R).
    
% Autre cas -> poser un pion blanc
corrigeListe(Plateau, [[_,L2,C2] | Liste], Joueur, Acc, R) :-   
    choixPionBlanc(Joueur,Pion),
    corrigeListe(Plateau, Liste, Joueur, [[Pion,L2,C2] | Acc], R).

% Autre cas -> autres cas (pas de pion blanc en réserve)
corrigeListe(Plateau, [_ | Liste], Joueur, Acc, R) :-
    corrigeListe(Plateau, Liste, Joueur, Acc, R).
    

test(N,R) :-
    construireListe(N,N,[],Liste),
    Plateau = [ [0,0,2] , [2,1,0], [1,2,1], [2,3,0], [1,3,3] ],
    Joueur = [0,8,4,4],
    corrigeListe(Plateau,Liste,Joueur,[],R1),
    reverse(R1,R).


%
% Choix d'un pion selon les reserves du joueur
%
choixPionBlanc([_,Bl,_,_],0) :- Bl > 0.
choixPionColore([_,_,Ro,_],1) :- Ro > 0.
choixPionColore([_,_,_,Ja],2) :- Ja > 0.




