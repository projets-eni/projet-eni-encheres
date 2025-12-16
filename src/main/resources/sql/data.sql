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