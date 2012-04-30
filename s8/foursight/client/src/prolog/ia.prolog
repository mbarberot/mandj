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
%	Une lise des 2 éléments suivants
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

calculCoup(Plateau,Coup) :-
    Coup = [0,[1,0,0]].
    