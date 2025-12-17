-- Script de création de la base de données ENCHERES
--   type :      SQL Server 2012
/*
-- Désactiver toutes les contraintes de clé étrangère
EXEC sp_MSforeachtable 'ALTER TABLE ? NOCHECK CONSTRAINT ALL';*/
-- Supprimer une table spécifique
/*
DROP TABLE Encheres ;
DROP TABLE Retraits ;
DROP TABLE ArticlesVendus ;
DROP TABLE Categories ;
DROP TABLE Utilisateurs ;
*/
-- Réactiver toutes les contraintes de clé étrangère
/*
EXEC sp_MSforeachtable 'ALTER TABLE ? CHECK CONSTRAINT ALL';
*/


CREATE TABLE Categories
(
    no_categorie INTEGER IDENTITY (1,1) NOT NULL,
    libelle      NVARCHAR(30)           NOT NULL UNIQUE /* CONSTRAINT CK_Categories_libelle CHECK(libelle IN('Informatique','Ameublement','Vêtement','Sport & Loisirs')) */
);
GO

ALTER TABLE Categories
    ADD constraint PK_Categorie PRIMARY KEY (no_categorie);
GO

CREATE TABLE Utilisateurs
(
    no_utilisateur INTEGER IDENTITY (1,1) NOT NULL,
    pseudo         NVARCHAR(30)           NOT NULL
        CONSTRAINT UN_Utilisateurs_pseudo UNIQUE,
    nom            NVARCHAR(30)           NOT NULL,
    prenom         NVARCHAR(30)           NOT NULL,
    email          NVARCHAR(50)           NOT NULL
        CONSTRAINT UN_Utilisateurs_email UNIQUE,
    telephone      NVARCHAR(15)           NOT NULL,
    rue            NVARCHAR(30)           NOT NULL,
    code_postal    INTEGER                NOT NULL
        CONSTRAINT CK_Utilisateurs_codePostal CHECK (code_postal BETWEEN 1000 AND 99000),
    ville          NVARCHAR(50)           NOT NULL,
    mot_de_passe   NVARCHAR(100)          NOT NULL,
    credit         INTEGER                NOT NULL
        CONSTRAINT DF_Utilisateurs_credit DEFAULT 100
        CONSTRAINT CK_Utilisateurs_credit CHECK (credit >= 0),
    administrateur bit                    NOT NULL
        CONSTRAINT DF_Utilisateurs_admin DEFAULT 0
);
GO

ALTER TABLE Utilisateurs
    ADD constraint PK_Utilisateurs PRIMARY KEY (no_utilisateur);
GO


CREATE TABLE ArticlesVendus
(
    no_article          INTEGER IDENTITY (1,1) NOT NULL,
    nom_article         NVARCHAR(30)           NOT NULL,
    description         NVARCHAR(300)          NOT NULL,
    date_debut_encheres DATETIME2              NOT NULL
        CONSTRAINT DF_ArticlesVendus_DateDebutEnchere DEFAULT GETDATE()
        CONSTRAINT CK_ArticlesVendus_DateDebutEnchere CHECK (date_debut_encheres >= GETDATE()),
    date_fin_encheres   DATETIME2              NOT NULL,
    prix_initial        INTEGER                NOT NULL
        CONSTRAINT CK_ArticlesVendus_prixInitial CHECK (prix_initial > 0),
    prix_vente          INTEGER,
    etat_vente          NVARCHAR(30)           NOT NULL
        CONSTRAINT CK_ArticlensVendus_etatVente CHECK (etat_vente IN ('Non commencée', 'En cours', 'Terminée')),

    no_utilisateur      INTEGER                NOT NULL,
    no_categorie        INTEGER                NOT NULL,

    image_filename      VARCHAR(30),

    CONSTRAINT CK_ArticlesVendus_DateFinEnchere CHECK (date_fin_encheres >= date_debut_encheres),
    CONSTRAINT CK_ArticlesVendus_prixVente CHECK (prix_vente >= prix_initial)
);
GO


ALTER TABLE ArticlesVendus
    ADD constraint PK_ArticlesVendus PRIMARY KEY (no_article);
GO


CREATE TABLE Retraits
(
    no_article  INTEGER      NOT NULL,
    rue         NVARCHAR(30) NOT NULL,
    code_postal NVARCHAR(15) NOT NULL
        CONSTRAINT CK_Retraits_codePostal CHECK (code_postal BETWEEN 1000 AND 99000),
    ville       NVARCHAR(30) NOT NULL,
    est_retire  BIT          NOT NULL
        CONSTRAINT DF_Retraits_estRetire DEFAULT 0
);
GO

ALTER TABLE Retraits
    ADD CONSTRAINT FK_Retraits_ArticlesVendus FOREIGN KEY (no_article) REFERENCES ArticlesVendus (no_article)
        ON DELETE NO ACTION
        ON UPDATE no action;
GO


CREATE TABLE Encheres
(
    no_enchere      INTEGER IDENTITY (1,1) NOT NULL,
    date_enchere    DATETIME2              NOT NULL
        CONSTRAINT DF_Encheres_dateEnchere DEFAULT GETDATE(),
    montant_enchere INTEGER                NOT NULL
        CONSTRAINT CK_Encheres_montantEnchere CHECK (montant_enchere > 0),
    no_article      INTEGER                NOT NULL,
    no_utilisateur  INTEGER                NOT NULL
);
GO

ALTER TABLE Encheres
    ADD constraint PK_Encheres PRIMARY KEY (no_enchere);
GO

ALTER TABLE Encheres
    ADD CONSTRAINT FK_Encheres_Utilisateurs FOREIGN KEY (no_utilisateur) REFERENCES Utilisateurs (no_utilisateur)
        ON DELETE NO ACTION
        ON UPDATE no action;
GO

ALTER TABLE Encheres
    ADD CONSTRAINT FK_Encheres_ArticlesVendus FOREIGN KEY (no_article) REFERENCES ArticlesVendus (no_article)
        ON DELETE NO ACTION
        ON UPDATE no action;
GO


ALTER TABLE ArticlesVendus
    ADD CONSTRAINT FK_ArticlesVendus_Categories FOREIGN KEY (no_categorie)
        REFERENCES Categories (no_categorie)
        ON DELETE NO ACTION
        ON UPDATE no action;
GO

ALTER TABLE ArticlesVendus
    ADD CONSTRAINT FK_ArticlesVendus_Utilisateurs FOREIGN KEY (no_utilisateur)
        REFERENCES Utilisateurs (no_utilisateur)
        ON DELETE NO ACTION
        ON UPDATE no action;
GO


CREATE INDEX FK_Retraits_ArticlesVendus ON Retraits (no_article);
CREATE INDEX FK_Encheres_Utilisateurs ON Encheres (no_utilisateur);
CREATE INDEX FK_Encheres_ArticlesVendus ON Encheres (no_article);
CREATE INDEX FK_ArticlesVendus_Categories ON ArticlesVendus (no_categorie);
CREATE INDEX FK_ArticlesVendus_Utilisateurs ON ArticlesVendus (no_utilisateur);

CREATE INDEX IN_ArticlesVendus_nomArticle ON ArticlesVendus (nom_article);
CREATE INDEX IN_ArticlesVendus_descriptionArticle ON ArticlesVendus (description);
CREATE INDEX IN_ArticlesVendus_etatVente ON ArticlesVendus (etat_vente);


-- format code postal pour FR
ALTER TABLE Utilisateurs
    DROP CONSTRAINT CK_Utilisateurs_codePostal;

alter table Utilisateurs
    alter column code_postal varchar(5);

ALTER TABLE Retraits
    DROP CONSTRAINT CK_Retraits_codePostal;

alter table Retraits
    alter column code_postal varchar(5);