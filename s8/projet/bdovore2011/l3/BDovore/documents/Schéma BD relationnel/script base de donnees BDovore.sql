CREATE TABLE GENRE(
	Id_Genre INTEGER(100), PRIMARY KEY (Id_Genre),
	Nom_Genre VARCHAR(100)
);
CREATE TABLE EDITEUR(
	Id_Editeur Integer(10) NOT NULL, PRIMARY KEY (Id_Editeur),
	Nom_Editeur VARCHAR(100),
	Url varchar(100)
);

CREATE TABLE SERIE(
	Id_Serie Integer(10) NOT NULL, PRIMARY KEY (Id_Serie),
	Nom_Serie VARCHAR(100),
	Id_Genre INTEGER(100),
	FOREIGN KEY (Id_Genre) REFERENCES GENRE(Id_Genre)
);

CREATE TABLE DETAILS_SERIE(
	Id_Serie INTEGER(10) NOT NULL, PRIMARY KEY (Id_Serie),
	Nb_Tomes INTEGER(10),
	Flg_Fini INTEGER(10),
	Histoire VARCHAR(2147483647),
	FOREIGN KEY (Id_Serie) REFERENCES SERIE(Id_Serie)
);

CREATE TABLE AUTEUR(
	Id_Auteur INTEGER(10) NOT NULL, PRIMARY KEY (Id_Auteur),
	Pseudo VARCHAR(100),
	Nom VARCHAR(100),
	Prenom VARCHAR(100)
);

CREATE TABLE DETAILS_AUTEUR(
	Id_Auteur INTEGER(10) NOT NULL, PRIMARY KEY (Id_Auteur),
	Date_Naissance DATE,
	Date_Deces DATE,
	Nationalite VARCHAR(100),
	FOREIGN KEY (Id_Auteur) REFERENCES AUTEUR(Id_Auteur)
);

CREATE TABLE TOME(
	Id_Tome INTEGER(10) NOT NULL, PRIMARY KEY (Id_Tome),
	Titre VARCHAR(150),
	Id_Serie INTEGER(10),
	Num_Tome INTEGER(10),
	FOREIGN KEY (Id_Serie) REFERENCES SERIE(Id_Serie)
);

CREATE TABLE TJ_TOME_AUTEUR(
	Id_Tome INTEGER(10) NOT NULL,
	Id_Auteur INTEGER(10) NOT NULL, 
	Role VARCHAR(100) NOT NULL, PRIMARY KEY (Id_Tome, Id_Auteur, Role),
	FOREIGN KEY (Id_Auteur) REFERENCES AUTEUR(Id_Auteur),
	FOREIGN KEY (Id_Tome) REFERENCES TOME(Id_Tome)
);

CREATE TABLE EDITION(
	Id_Edition Integer(10) NOT NULL, PRIMARY KEY (Id_Edition),
	Id_Tome INTEGER(10),
	Isbn VARCHAR(13),
	Date_Parution DATE,
	Id_Editeur INTEGER(10),
	Flg_Default BOOLEAN,
	FOREIGN KEY (Id_Editeur) REFERENCES EDITEUR(Id_Editeur),
	FOREIGN KEY (Id_Tome) REFERENCES TOME(Id_Tome)
);

CREATE TABLE DETAILS_EDITION(
	Id_Edition Integer(10) NOT NULL, PRIMARY KEY (Id_Edition),
	Img_Couv VARCHAR(100),
	FOREIGN KEY(Id_Edition) REFERENCES EDITION(Id_Edition)
);

CREATE TABLE TRANSACTION(
	Id_Edition INTEGER(10) NOT NULL, PRIMARY KEY (Id_Edition),
	Type INTEGER(10),
	Date DATE,
	FOREIGN KEY(Id_Edition) REFERENCES EDITION(Id_Edition)
);

CREATE TABLE BD_USER(
	Id_Edition Integer(10) NOT NULL, PRIMARY KEY (Id_Edition),
	Flg_Pret BOOLEAN,
	Flg_Dedicace BOOLEAN,
	Flg_AAcheter BOOLEAN,
	Date_Ajout DATE,
	FOREIGN KEY(Id_Edition) REFERENCES EDITION(Id_Edition)
);
	
CREATE INDEX IDX_AUTEUR_PSEUDO ON AUTEUR (PSEUDO);
CREATE INDEX IDX_EDITION_ISBN ON EDITION (ISBN);
CREATE INDEX IDX_TOME_TITRE ON TOME (TITRE);
CREATE INDEX IDX_SERIE_NOM ON SERIE (NOM_SERIE);