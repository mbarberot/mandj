INSERT INTO AUTEUR (ID_AUTEUR, PSEUDO, NOM, PRENOM)
VALUES (1, 'Goscinny', 'Goscinny', 'Rene');

INSERT INTO DETAILS_AUTEUR (ID_AUTEUR, DATE_NAISSANCE, DATE_DECES, NATIONALITE)
VALUES (1, '1926-08-14', '1977-09-05','fran�aise');

INSERT INTO AUTEUR (ID_AUTEUR, PSEUDO, NOM, PRENOM)
VALUES (2, 'Huderzo', 'Huderzo', 'Albert');

INSERT INTO AUTEUR (ID_AUTEUR, PSEUDO, NOM, PRENOM)
VALUES (3, 'Franquin', 'Franquin', 'Andr�');

INSERT INTO AUTEUR (ID_AUTEUR, PSEUDO, NOM, PRENOM)
VALUES (4, 'Jid�hem', 'Jid�hem', '');

INSERT INTO GENRE (ID_GENRE, NOM_GENRE)
VALUES (1, 'Aventures Historiques');

INSERT INTO GENRE (ID_GENRE, NOM_GENRE)
VALUES (2, 'Humour');

INSERT INTO SERIE (ID_SERIE, NOM_SERIE, ID_GENRE)
VALUES (1, 'Ast�rix (Albums de film)',1);

INSERT INTO SERIE (ID_SERIE, NOM_SERIE, ID_GENRE)
VALUES (2, 'Gaston Lagaffe (16 Tomes - Historique) ',2);

INSERT INTO TOME (ID_TOME, TITRE, ID_SERIE, NUM_TOME)
VALUES (1, 'Ast�rix et les Vikings ', 1, 14);

INSERT INTO TOME (ID_TOME, TITRE, ID_SERIE, NUM_TOME)
VALUES (2, 'Ast�rix aux jeux olympiques', 1, 5);

INSERT INTO TOME (ID_TOME, TITRE, ID_SERIE, NUM_TOME)
VALUES (3, 'Gare aux gaffes', 2, 1);

INSERT INTO TJ_TOME_AUTEUR (ID_TOME, ROLE, ID_AUTEUR)
VALUES (1, 'Scenariste', 2);
INSERT INTO TJ_TOME_AUTEUR (ID_TOME, ROLE, ID_AUTEUR)
VALUES (1, 'Dessinateur', 2);

INSERT INTO TJ_TOME_AUTEUR (ID_TOME, ROLE, ID_AUTEUR)
VALUES (2, 'Scenariste', 1);
INSERT INTO TJ_TOME_AUTEUR (ID_TOME, ROLE, ID_AUTEUR)
VALUES (2, 'Dessinateur', 2);

INSERT INTO TJ_TOME_AUTEUR (ID_TOME, ROLE, ID_AUTEUR)
VALUES (3, 'Scenariste', 3);
INSERT INTO TJ_TOME_AUTEUR (ID_TOME, ROLE, ID_AUTEUR)
VALUES (3, 'Dessinateur', 3);
INSERT INTO TJ_TOME_AUTEUR (ID_TOME, ROLE, ID_AUTEUR)
VALUES (3, 'Dessinateur', 4);

INSERT INTO EDITEUR (ID_EDITEUR, NOM_EDITEUR)
VALUES (1, 'Albert Ren�');

INSERT INTO EDITEUR (ID_EDITEUR, NOM_EDITEUR)
VALUES (2, 'Dupuis');

INSERT INTO EDITEUR (ID_EDITEUR, NOM_EDITEUR)
VALUES (3, 'Marsu Productions');

INSERT INTO EDITION (ID_EDITION, ID_TOME, ISBN, DATE_PARUTION, ID_EDITEUR, FLG_DEFAULT)
VALUES (1, 2, '9782864972266' , '2008-01-16', 1,TRUE);

INSERT INTO EDITION (ID_EDITION, ID_TOME, ISBN, DATE_PARUTION, ID_EDITEUR, FLG_DEFAULT)
VALUES (2, 1, '2864971895', '2006-04-05', 1,TRUE);

INSERT INTO EDITION (ID_EDITION, ID_TOME, ISBN, DATE_PARUTION, ID_EDITEUR, FLG_DEFAULT)
VALUES (3, 3, '' , '2007-02-09', 2,TRUE);

INSERT INTO EDITION (ID_EDITION, ID_TOME, ISBN, DATE_PARUTION, ID_EDITEUR, FLG_DEFAULT)
VALUES (4, 3, '', '2006-04-05', 3,FALSE);

INSERT INTO DETAILS_EDITION (ID_EDITION, IMG_COUV)
VALUES (2, 'CV-037717-035302.jpg');

INSERT INTO DETAILS_EDITION (ID_EDITION, IMG_COUV)
VALUES (3, 'CV-030077-039680.jpg');

INSERT INTO BD_USER (ID_EDITION, FLG_PRET, FLG_DEDICACE, FLG_AACHETER, DATE_AJOUT)
VALUES (1,false,false,false,NOW());

INSERT INTO BD_USER (ID_EDITION, FLG_PRET, FLG_DEDICACE, FLG_AACHETER, DATE_AJOUT)
VALUES (2,false,false,false,NOW());

INSERT INTO BD_USER (ID_EDITION, FLG_PRET, FLG_DEDICACE, FLG_AACHETER, DATE_AJOUT)
VALUES (3,false,false,false,NOW());

INSERT INTO DETAILS_SERIE(ID_SERIE,Nb_Tomes,Flg_Fini,Histoire)
VALUES (2,16,2, 'Gaston...')
