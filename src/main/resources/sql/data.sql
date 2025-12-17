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

/* Table Utilisateurs */
SET IDENTITY_INSERT Utilisateurs ON;
INSERT INTO Utilisateurs(no_utilisateur, pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur)
VALUES (1, 'mdupond', 'Dupond', 'Martin', 'martin.dupond@yahoo.fr', '0607080910',
        '2 rue de la Boétie', '33000', 'Bordeaux', '$2a$10$QX0vCM8/EHw9tQ8vnJTzqujfxdPG574A1YAfdMXQ5jjuHWWVYZfka', 100, 1);
SET IDENTITY_INSERT Utilisateurs OFF;