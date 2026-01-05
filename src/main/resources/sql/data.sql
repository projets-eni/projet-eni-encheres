-- --insertion utilisateurs
-- DELETE FROM Utilisateurs;
--
-- --mdp : azerty1234
-- INSERT INTO Utilisateurs (pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur)
-- VALUES ('clmartin', 'Martin', 'Claire', 'claire.martin@mail.com', '0645678901', '3 rue Pasteur', '44000', 'Nantes', '{bcrypt}$2a$10$6D4p4PrBW5h8h2RsmU1ZGeamWNC1REl3nHYbdLjiiLB40GNvFDu6S', 100, 0);
--
-- --mdp : poiuyt1234
-- INSERT INTO Utilisateurs (pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur)
-- VALUES ('thdurand', 'Durand', 'Thomas', 'thomas.durand@mail.com', '0656789012', '27 chemin des Vignes', '34000', 'Montpellier', '{bcrypt}$2a$10$zQAUCog26UVwKf6ZADTn4.tLE61BkwGTFMx0o27wBwwA8rR./3a/S', 100, 0);
--
-- --mdp : wxcvbn1234
-- INSERT INTO Utilisateurs (pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur)
-- VALUES ('asimon', 'Simon', 'Alice', 'alice.simon@mail.com', '0667890123', '14 impasse des Roses', '21000', 'Dijon', '{bcrypt}$2a$10$mKLaqjCYNT8yReWM2dPNhetWIA5arKLmRj9hBo44GAkz1avqKBmcq', 100, 0);
-- Autoriser l'insertion explicite dans la colonne IDENTIY
SET IDENTITY_INSERT Utilisateurs ON;
delete from Utilisateurs;
INSERT INTO Utilisateurs (no_utilisateur, pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur)
VALUES
    (1, 'alice_01', 'Alice', 'Durand', 'alice@mail.com', '0123456789', '10 Rue des Lilas', '75001', 'Paris', 'password123', 5000, 0),
    (2, 'bob_02', 'Bob', 'Martin', 'bob@mail.com', '0123456790', '20 Rue des Roses', '75002', 'Paris', 'password123', 8000, 0),
    (3, 'charlie_03', 'Charlie', 'Dupont', 'charlie@mail.com', '0123456791', '30 Rue de la Gare', '75003', 'Paris', 'password123', 3000, 0),
    (4, 'david_04', 'David', 'Lemoine', 'david@mail.com', '0123456792', '40 Rue de la Mer', '75004', 'Paris', 'password123', 1000, 0);
SET IDENTITY_INSERT Utilisateurs OFF;

-- Autoriser l'insertion explicite dans la colonne IDENTIY
SET IDENTITY_INSERT ArticlesVendus ON;
delete from ArticlesVendus;
INSERT INTO ArticlesVendus (no_article, nom_article, description, etat_vente, date_fin_encheres, prix_initial, no_utilisateur, no_categorie)
VALUES
    (1, 'Ordinateur Portable', 'Un ordinateur portable haut de gamme', 'Non commencée', '2026-12-15T10:00:00', 1000, 1, 1),
    (2, 'Canapé', 'Canapé 3 places en cuir', 'Non commencée', '2026-12-15T10:00:00', 500, 2, 2),
    (3, 'T-shirt Sport', 'T-shirt de sport en coton', 'Non commencée', '2026-12-10T10:00:00', 100, 3, 3);
SET IDENTITY_INSERT ArticlesVendus OFF;


-- Autoriser l'insertion explicite dans la colonne IDENTIY
SET IDENTITY_INSERT Encheres ON;
delete from Encheres;
INSERT INTO Encheres (no_enchere, no_article, no_utilisateur, montant_enchere)
VALUES
    (1, 1, 2, 1500),  -- Bob enchère 1500 sur l'ordinateur
    (2, 1, 3, 2000),  -- Charlie enchère 2000 sur l'ordinateur
    (3, 1, 4, 2500),  -- David enchère 2500 sur l'ordinateur
    (4, 2, 2, 800),   -- Bob enchère 800 sur le canapé
    (5, 2, 3, 1000),  -- Charlie enchère 1000 sur le canapé
    (6, 3, 2, 150),   -- Bob enchère 150 sur le t-shirt
    (7, 3, 3, 200);   -- Charlie enchère 200 sur le t-shirt
SET IDENTITY_INSERT Encheres OFF;


DELETE FROM ArticlesVendus;
DELETE FROM Categories;
DELETE FROM Encheres;
DELETE FROM Retraits;
DELETE FROM Utilisateurs;

/* ⚠️ RÉINITIALISER IDENTITY à 1 pour CHAQUE table avec IDENTITY */
DBCC CHECKIDENT('Categories', RESEED, 0);
DBCC CHECKIDENT('Utilisateurs', RESEED, 0);
DBCC CHECKIDENT('ArticlesVendus', RESEED, 0);
DBCC CHECKIDENT('Encheres', RESEED, 0);

/* Table Categorie */
/* active temporairement l'insertion manuelle de valeurs dans une colonne IDENTITY (auto-incrémentée) */
SET IDENTITY_INSERT Categories ON;
INSERT INTO Categories(no_categorie, libelle)
VALUES (1, 'Informatique'),
       (2, 'Ameublement'),
       (3, 'Vêtement'),
       (4, 'Sport&Loisirs');
SET IDENTITY_INSERT Categories OFF;

/* Table Utilisateurs : azerty1234 */

SET IDENTITY_INSERT Utilisateurs ON;
INSERT INTO Utilisateurs(no_utilisateur, pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur)
VALUES (1, 'mdupond', 'Dupond', 'Martin', 'martin.dupond@yahoo.fr', '0607080910',
        '2 rue de la Boétie', '33000', 'Bordeaux', '{bcrypt}$2a$10$6D4p4PrBW5h8h2RsmU1ZGeamWNC1REl3nHYbdLjiiLB40GNvFDu6S', 100, 1);
SET IDENTITY_INSERT Utilisateurs OFF;

    SET IDENTITY_INSERT ArticlesVendus ON;
INSERT INTO ArticlesVendus (no_article, nom_article, description, etat_vente, date_fin_encheres, prix_initial, no_utilisateur, no_categorie)
VALUES
    (1, 'Ordinateur Portable', 'Un ordinateur portable haut de gamme', 'Non commencée', '2026-12-15T10:00:00', 1000, 1, 1),
    (2, 'Canapé', 'Canapé 3 places en cuir', 'Non commencée', '2026-12-15T10:00:00', 500, 2, 2),
    (3, 'T-shirt Sport', 'T-shirt de sport en coton', 'Non commencée', '2026-12-10T10:00:00', 100, 3, 3);
SET IDENTITY_INSERT ArticlesVendus OFF;