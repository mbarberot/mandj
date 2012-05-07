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




evaluer.
min.
max.
    
 
 
%
% TODO
% 
% Réflexions sur :
% - l'évalutation d'un plateau
% - déterminer le type d'un coup
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
%	NLigne		Numero de la dernière ligne
%	NColonne	Numero de la derniere colonne
%	Accumulateur	Liste de cases
%	Resultat	Liste de cases
% 	)
%
% Construit les cases du plateau dans l'ordre croissant*, en iterant sur un prédicat de construction d'une ligne.
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
%	Liste		Liste de cases -> Une liste de case générées
%	Joueur		Le joueur actuel
%	Accumulateur	Liste de cases 
%	Resultat	Liste de cases -> Liste des coups jouables pour le joueur 
%	)
%
% Corrige la liste générée en ôtant les cases contenant déjà des pions colorés
% et en choissisant un pion coloré pour les cases contenant un pion blanc.
%
corrigeListe(_,[],_,Acc,Acc).


% Même case -> pion blanc présent -> poser un pion coloré
corrigeListe([[P,L,C] | Plateau], [[_,L,C] | Liste], Joueur, Acc, R) :-
    \+ pionColore(P),
    choixRouge(L,C,Joueur,CaseRouge),
    choixJaune(L,C,Joueur,CaseJaune),
    corrigeListe(Plateau,Liste,Joueur,[CaseJaune,CaseRouge | Acc], R).
 
% Même case -> Autres cas (pion coloré présent / pas de pion coloré en réserve)
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
%	Joueur		Joueur concerné
%	Case		La future case
%	)
%
% Verification du nombre de pions du type concerné
% Envoi d'une case vide si le joueur n'a plus du pion demandé
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
% Réussi sinon
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
% Réussi si les les lignes 1 et 2 sont identiques
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
%	Case,		Case du coup à jouer
%	Joueur,		Joueur avant le coup (N pions)
%	NvJoueur	Joueur apres le coup (N - 1 pions)
%	)
%
% Ote le pion de la case au joueur concerné.
% Retourne le joueur modifié.
%
majJoueur([0,_,_],[Id,Bl,Ro,Ja],[Id,NBl,Ro,Ja]) :- Bl > 0, NBl is Bl - 1.
majJoueur([1,_,_],[Id,Bl,Ro,Ja],[Id,Bl,NRo,Ja]) :- Ro > 0, NRo is Ro - 1.
majJoueur([2,_,_],[Id,Bl,Ro,Ja],[Id,Bl,Ro,NJa]) :- Ja > 0, NJa is Ja - 1.
    
%
% creePlateau(
%	Case		Case à mettre à jour
%	Plateau		Plateau actuel
%	NvPlateau	Plateau avec la nouvelle case
%	)
%
% Place la case dans une copie du plateau.
% Retourne le plateau modifié.
% 

% Si la case 1 est après la case 2
% -> on avance à la prochaine case occupée du plateau
creePlateau([P1,L1,C1],[[P2,L2,C2]|LC],[[P2,L2,C2]|NvPlt]) :-
    caseApres(L1,C1,L2,C2),
    creePlateau([P1,L1,C1],LC,NvPlt).
% Si la case 1 est avant la case 2 (cas du pion blanc)
% -> on insère la case 1 avant la case 2
creePlateau([0,L1,C1],[[P2,L2,C2]|LC],[[0,L1,C1],[P2,L2,C2]|LC]) :-
    caseAvant(L1,C1,L2,C2).
% Si la case 1 est la même que la case 2 (cas du pion coloré)
% -> on met à jour la case avec le nouveau pion
creePlateau([P1,L,C],[[_,L,C]|LC],[[P1,L,C]|LC]) :- pionColore(P1).


%
% case<Where>(
%	Ligne 1,	Ligne de la case 1
%	Colonne 1,	Colonne de la case 1
%	Ligne 2,	Ligne de la case 2
%	Colonne 2	Colonne de la case 2
%	)
%
% Détermine la position d'une case par rapport à une autre case.
%
% <Where> = Apres
caseApres(L1,_,L2,_) :- L1 > L2.
caseApres(L,C1,L,C2) :- C1 > C2.
% <Where> = Avant
caseAvant(L1,C1,L2,C2) :- caseApres(L2,C2,L1,C1).
    


test2(N,R,P) :-
    construireListe(N,N,[],Liste),
    Plateau = [ [1,0,1] , [2,0,3] , [0,1,0] , [2,1,1] , [1,1,2] , [0,2,1] , [0,2,3] , [2,3,0] , [0,3,2], [1,3,3] ],
    Joueur = [0,1,0,0],
    corrigeListe(Plateau,Liste,Joueur,[],R1),
    nettoieListe(R1,R2),
    reverse(R2,R),
    creeListePlateau(R,Plateau,Joueur,P).
    
    
test1(P,N) :-
    Hval = -1,
    Plateau = [ [1,0,1] , [2,0,3] , [0,1,0] , [2,1,1] , [1,1,2] , [0,2,1] , [0,2,3] , [2,3,0] , [0,3,2], [1,3,3] ],
    Joueur = [0,6,5,5],
    Coup = [-1,[]],
    H = [[[Hval,Plateau,Joueur,Coup]]],
    profondeur(N,H,P).
    
    
profondeur(0,LChemin,LChemin).
profondeur(N,LChemin,R) :-
    N > 0,
    N1 is N - 1,
    nouvelEtage(LChemin,[],NLChemin),
    !,
    profondeur(N1,NLChemin,R).
    
    

    
%
% nouvelEtage(
%	LLH	Liste de tous les chemins -> la suite des plateaux/coups
%	LLH	Nouvelle liste de tous les chemins 
%	)
%
nouvelEtage([],Acc,Acc).
nouvelEtage([Chemin | LChemin],Acc,NvLChemin) :-
    genererCoups(Chemin,NvxChemins),
    append(NvxChemins,Acc,Acc1),
    nouvelEtage(LChemin,Acc1,NvLChemin).
    
    
genererCoups([[HVal,Plateau,[Id,0,0,0],Coup]|LEtape],[[[HVal,Plateau,[Id,0,0,0],Coup]|LEtape]]).
genererCoups([[HVal,Plateau,Joueur,Coup]|LEtape],NvxChemins) :-
    peutJouer(Joueur),
    taillePlateau(N),
    construireListe(N,N,[],Liste),
    corrigeListe(Plateau,Liste,Joueur,[],ListeCorr),
    nettoieListe(ListeCorr,ListeNet),
    reverse(ListeNet,ListeRev),
    creeListePlateau(ListeRev,[[HVal,Plateau,Joueur,Coup]|LEtape],NvxChemins).
    
peutJouer([_,Bl,_,_]) :- Bl > 0.
peutJouer([_,_,Ro,_]) :- Ro > 0.
peutJouer([_,_,_,Ja]) :- Ja > 0.


%
% creeListePlateau(
%	ListeCoups	Liste de cases -> les coups possibles sur le plateau actuel
%	Chemin		Liste des étapes -> les coups joués jusqu'à maintenant
%	ListePlateau	Liste de plateaux -> chaque plateau doit avoir un valeur
%	)
%
creeListePlateau([],_,[]).
creeListePlateau(
	[C|LC],
	[[HVal,Plateau,Joueur,Coup]|LH],
	[ [ [-1,Plt,NJ,[0,C]], [HVal,Plateau,Joueur,Coup] | LH ] | LP ]
	) :-
    creePlateau(C,Plateau,Plt),
    majJoueur(C,Joueur,NJ),
    creeListePlateau(LC,[[HVal,Plateau,Joueur,Coup]|LH],LP).





