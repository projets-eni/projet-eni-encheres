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


-- 3. UTILISATEURS (5 utilisateurs)
DBCC CHECKIDENT('Utilisateurs', RESEED, 0);
INSERT INTO Utilisateurs (pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur) VALUES
-- Admin
('admin', 'Dupont', 'Paul', 'admin@eni.fr', '0123456789', '12 Rue Admin', '75001', 'Paris', '{bcrypt}$2a$10$6D4p4PrBW5h8h2RsmU1ZGeamWNC1REl3nHYbdLjiiLB40GNvFDu6S', 500, 1),

-- Vendeurs
('vendeur1', 'Martin', 'Sophie', 'sophie.martin@eni.fr', '0987654321', '45 Av. Vente', '69001', 'Lyon', '{bcrypt}$2a$10$6D4p4PrBW5h8h2RsmU1ZGeamWNC1REl3nHYbdLjiiLB40GNvFDu6S', 200, 0),
('vendeur2', 'Bernard', 'Lucas', 'lucas.bernard@eni.fr', '0112233445', '78 Rue Enchères', '33000', 'Bordeaux', '{bcrypt}$2a$10$6D4p4PrBW5h8h2RsmU1ZGeamWNC1REl3nHYbdLjiiLB40GNvFDu6S', 150, 0),

-- Acheteurs
('acheteur1', 'Dubois', 'Marie', 'marie.dubois@eni.fr', '0555666777', '23 Rue Client', '31000', 'Toulouse', '{bcrypt}$2a$10$6D4p4PrBW5h8h2RsmU1ZGeamWNC1REl3nHYbdLjiiLB40GNvFDu6S', 300, 0),
('acheteur2', 'Roux', 'Thomas', 'thomas.roux@eni.fr', '0888999000', '56 Bd Acheteur', '13001', 'Marseille', '{bcrypt}$2a$10$6D4p4PrBW5h8h2RsmU1ZGeamWNC1REl3nHYbdLjiiLB40GNvFDu6S', 250, 0),

-- NOUVEL UTILISATEUR : Hybride Vendeur/Acheteur
('hybrid1', 'Leroy', 'Julie', 'julie.leroy@eni.fr', '0666777888', '15 Rue Hybride', '35000', 'Rennes', 'hybrid123', 180, 0);
GO

ALTER TABLE ArticlesVendus
    NOCHECK CONSTRAINT CK_ArticlesVendus_DateDebutEnchere;
GO

-- 4. ARTICLES VENDUS (8 articles variés)
DBCC CHECKIDENT('ArticlesVendus', RESEED, 0);
INSERT INTO ArticlesVendus (nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, etat_vente, no_utilisateur, no_categorie, image_filename) VALUES
-- NON COMMENCÉES (futures)
('PC Gamer RTX 4090', 'Ordinateur gaming haut de gamme', '2025-12-25 10:00:00', '2025-12-28 20:00:00', 1500, NULL, 'Non commencée', 2, 1, 'pc_gamer.jpg'),

('Canapé cuir 3 places', 'Canapé cuir italien comme neuf', '2025-12-26 14:00:00', '2025-12-30 18:00:00', 800, NULL, 'Non commencée', 3, 2, 'canape.jpg'),

-- EN COURS
('iPhone 15 Pro Max', 'Smartphone Apple 256Go état neuf', '2025-12-20 09:00:00', '2025-12-24 22:00:00', 900, NULL, 'En cours', 2, 1, 'iphone.jpg'),
('Vélo électrique', 'VTT électrique 500W autonomie 80km', '2025-12-22 11:00:00', '2025-12-26 19:00:00', 1200, NULL, 'En cours', 3, 4, 'velo.jpg'),

-- TERMINÉES (avec prix de vente)
('Ordinateur portable', 'Dell XPS 13 i7 16Go RAM', '2025-12-01 10:00:00', '2025-12-05 20:00:00', 600, 850, 'Terminée', 2, 1, 'laptop.jpg'),
('Table basse design', 'Table basse verre et métal', '2025-12-03 15:00:00', '2025-12-07 21:00:00', 150, 220, 'Terminée', 3, 2, 'table.jpg'),
('Veste Patagonia', 'Veste imperméable homme taille M', '2025-12-10 12:00:00', '2025-12-14 18:00:00', 200, 280, 'Terminée', 2, 3, 'veste.jpg'),
('Raquettes tennis', 'Raquettes Wilson Pro Staff x2', '2025-12-15 16:00:00', '2025-12-19 20:00:00', 100, 175, 'Terminée', 3, 4, 'raquettes.jpg'),

-- 9. NOUVEAUX ARTICLES VENDUS PAR hybrid1 (no_utilisateur = 6)
-- Non commencée (vendue par hybrid1)
('Smartwatch Apple Watch', 'Apple Watch Series 9 état neuf', '2025-12-27 09:00:00', '2025-12-30 21:00:00', 350, NULL, 'Non commencée', 6, 1, 'watch.jpg'),
-- En cours (vendue par hybrid1)
('Console PS5 Slim', 'Playstation 5 Slim 1To + 2 manettes', '2025-12-21 14:00:00', '2025-12-25 20:00:00', 550, NULL, 'En cours', 6, 1, 'ps5.jpg');

GO

ALTER TABLE ArticlesVendus
    CHECK CONSTRAINT CK_ArticlesVendus_DateDebutEnchere;
GO

-- 5. RETRAITS (pour les articles terminés)
INSERT INTO Retraits (no_article, rue, code_postal, ville, est_retire) VALUES
(1, '23 Rue Client', '31000', 'Toulouse', 1),      -- Ordinateur portable
(2, '56 Bd Acheteur', '13001', 'Marseille', 0),    -- Table basse
(3, '45 Av. Vente', '69001', 'Lyon', 1),           -- Veste Patagonia
(4, '78 Rue Enchères', '33000', 'Bordeaux', 0),    -- Raquettes
(7, '15 Rue Hybride', '35000', 'Rennes', 1);  -- Veste Patagonia récupérée
GO

-- 6. ENCHÈRES (pour articles en cours et terminés)
DBCC CHECKIDENT('Encheres', RESEED, 0);
INSERT INTO Encheres (date_enchere, montant_enchere, no_article, no_utilisateur) VALUES
-- iPhone 15 (en cours) - enchères croissantes
('2025-12-20 10:15:00', 920, 3, 4),
('2025-12-20 14:30:00', 950, 3, 5),
('2025-12-21 09:45:00', 980, 3, 4),
('2025-12-22 16:20:00', 1010, 3, 5),
('2025-12-23 11:10:00', 1050, 3, 4),  -- Meilleure offre actuelle

-- Vélo électrique (en cours)
('2025-12-22 12:00:00', 1250, 4, 5),
('2025-12-23 09:30:00', 1280, 4, 4),

-- Ordinateur portable (terminé)
('2025-12-01 11:00:00', 620, 5, 4),
('2025-12-02 14:20:00', 680, 5, 5),
('2025-12-03 17:45:00', 720, 5, 4),
('2025-12-04 19:30:00', 850, 5, 5),  -- Vente finale

-- Table basse (terminé)
('2025-12-03 16:00:00', 160, 6, 5),
('2025-12-04 20:15:00', 190, 6, 4),
('2025-12-05 18:30:00', 220, 6, 5),  -- Vente finale

-- Veste Patagonia (terminé)
('2025-12-10 13:00:00', 210, 7, 4),
('2025-12-11 15:45:00', 245, 7, 5),
('2025-12-12 17:20:00', 260, 7, 4),
('2025-12-13 20:10:00', 280, 7, 4),  -- Vente finale (acheteur1)

-- 7. ENCHÈRES PAR hybrid1 (acheteur) sur d'autres articles
-- hybrid1 enchéris sur iPhone (article 3)
('2025-12-23 14:20:00', 1070, 3, 6),  -- Surenchérit acheteur1
-- hybrid1 enchéris sur Vélo électrique (article 4)
('2025-12-23 15:45:00', 1300, 4, 6),  -- Meilleure offre actuelle !
-- hybrid1 a gagné une vente terminée (Veste Patagonia - article 7)
('2025-12-13 21:00:00', 285, 7, 6);   -- Prix final 285€ (mais prix_vente=280 dans table)

GO

-- =====================================================
-- VÉRIFICATIONS
-- =====================================================
SELECT 'Categories' as TableName, COUNT(*) as NombreLignes FROM Categories UNION ALL
SELECT 'Utilisateurs', COUNT(*) FROM Utilisateurs UNION ALL
SELECT 'ArticlesVendus', COUNT(*) FROM ArticlesVendus UNION ALL
SELECT 'Retraits', COUNT(*) FROM Retraits UNION ALL
SELECT 'Encheres', COUNT(*) FROM Encheres;
GO

-- Articles par état
SELECT etat_vente, COUNT(*) as nombre
FROM ArticlesVendus
GROUP BY etat_vente;
GO

-- Meilleure offre par article en cours
SELECT TOP 5
    a.nom_article,
    MAX(e.montant_enchere) as meilleure_offre,
       u.pseudo as acheteur
FROM ArticlesVendus a
         JOIN Encheres e ON a.no_article = e.no_article
         JOIN Utilisateurs u ON e.no_utilisateur = u.no_utilisateur
WHERE a.etat_vente = 'En cours'
GROUP BY a.nom_article, u.pseudo
ORDER BY a.nom_article;
GO

/* =========================================================
   ENCHÈRES SUPPLÉMENTAIRES — ARTICLE n°3 (iPhone 15 Pro Max)
   ========================================================= */

INSERT INTO Encheres (date_enchere, montant_enchere, no_article, no_utilisateur)
VALUES
    ('2025-12-23 16:05:00', 2000, 4, 6),  -- hybrid1 relance : 1080 pts
    ('2025-12-23 18:20:00', 2100, 4, 5),  -- acheteur2 suit
    ('2025-12-23 21:00:00', 2200, 4, 4),  -- acheteur1 surenchérit
    ('2025-12-24 09:30:00', 1350, 4, 6),  -- hybrid1 nouvelle offre
    ('2025-12-24 13:50:00', 2300, 4, 5),  -- acheteur2 encore
    ('2025-12-24 18:35:00', 3000, 4, 6);  -- hybrid1 termine en tête (meilleure offre)
GO

/* Vérification du résultat */
SELECT e.no_enchere, e.date_enchere, e.montant_enchere, u.pseudo
FROM Encheres e JOIN Utilisateurs u ON e.no_utilisateur = u.no_utilisateur
WHERE e.no_article = 3
ORDER BY e.date_enchere;
GO