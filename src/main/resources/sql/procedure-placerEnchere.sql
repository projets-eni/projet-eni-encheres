CREATE PROCEDURE placerEnchere
    @montantEnchere INT,
    @noArticle INT,
    @noUtilisateur INT
AS
BEGIN
    -- Déclarations des variables
    DECLARE
    @EnchereInvalide INT = 0,
    @EnchereId INT,
    @NOW DATETIME2 = GETDATE(),
    @PrixInit INT,
    @EtatVente VARCHAR(50),
    @VendeurId INT,
    @DebutEnchere DATETIME2,
    @FinEnchere DATETIME2,
    @EncherisseurId INT,
    @EncherisseurCredit INT,
    @MaxEnchereUID INT,
    @MaxEnchereMontant INT;


    -- Vérification de la vente
SELECT
    @PrixInit = prix_initial,
    @EtatVente = etat_vente,
    @VendeurId = no_utilisateur,
    @DebutEnchere = date_debut_encheres,
    @FinEnchere = date_fin_encheres
FROM ArticlesVendus
WHERE no_article = @noArticle;

IF @VendeurId IS NULL
BEGIN
    -- Pas de vendeur, pas de vente
    SET @EnchereInvalide = -1;
END
IF @DebutEnchere > @NOW OR @FinEnchere <= @NOW
BEGIN
    -- Vente non active
    SET @EnchereInvalide = -2;
END
IF @VendeurId = @noUtilisateur
BEGIN
    -- Pas d'enchère possible pour le vendeur
    SET @EnchereInvalide = -3;
END

-- Vérification de l'enchérisseur
SELECT
    @EncherisseurId = no_utilisateur,
    @EncherisseurCredit = credit
FROM Utilisateurs
WHERE no_utilisateur = @noUtilisateur;

IF @EncherisseurId IS NULL
BEGIN
    -- Enchérisseur inconnu
    SET @EnchereInvalide = -4;
END

    -- Vérification de la meilleure enchère actuelle
SELECT TOP 1
        @MaxEnchereUID = no_utilisateur,
    @MaxEnchereMontant = montant_enchere
FROM Encheres
WHERE no_article = @noArticle
ORDER BY montant_enchere DESC;

IF @MaxEnchereUID = @noUtilisateur
BEGIN
    -- Déjà meilleur enchérisseur
    SET @EnchereInvalide = -5;
END

IF @EncherisseurCredit < @montantEnchere OR @montantEnchere <= @MaxEnchereMontant
BEGIN
    -- Pas assez de crédit ou enchère trop faible
    SET @EnchereInvalide = -6;
END

    -- Validation ou rollback de la transaction
IF @EnchereInvalide = 0
    BEGIN
    BEGIN TRANSACTION;

    -- Placer l'enchère
    INSERT INTO Encheres (date_enchere, montant_enchere, no_article, no_utilisateur)
    VALUES (@NOW, @montantEnchere, @noArticle, @noUtilisateur);

    -- maj prix de vente actuel
    UPDATE ArticlesVendus SET prix_vente = @montantEnchere WHERE no_article = @noArticle;

    SET @EnchereId = @@IDENTITY;

    COMMIT TRANSACTION;
    return @EnchereId;
    END
ELSE
    BEGIN
    ROLLBACK TRANSACTION;
    return @EnchereInvalide;
    END

END;
