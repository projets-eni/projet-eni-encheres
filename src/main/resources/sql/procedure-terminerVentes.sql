-- pour terminer les ventes et refaire les soldes de vendeurs et acquéreurs
CREATE PROCEDURE terminerVentes
@noArticle INT = NULL -- Paramètre nullable
AS
BEGIN
    -- Déclaration des variables
    DECLARE
        @noArticleParam INT,
        @dateFinEnchere DATETIME2,
        @dateDebutEnchere DATETIME2,
        @etatVente VARCHAR(50),
        @VendeurId INT,
        @EnchereMaxUID INT,
        @EnchereMaxMontant INT,
        @NAffectedVente INT = 0,
        @Now DATETIME2 = GETUTCDATE();

    -- Déclaration du curseur en dehors des conditions (c'est ici que la portée du curseur est correcte)
    DECLARE vente_cursor CURSOR FOR
        -- Si @noArticle est NULL, on sélectionne toutes les ventes
        -- Sinon, on sélectionne la vente spécifiée par @noArticle
        SELECT no_article, date_fin_encheres, date_debut_encheres, etat_vente, no_utilisateur
        FROM ArticlesVendus
        WHERE
            (@noArticle IS NULL OR no_article = @noArticle)  -- Si @noArticle est NULL, il sélectionne toutes les ventes
          AND etat_vente <> 'Terminée'  -- Seules les ventes non terminées
          AND date_fin_encheres <= GETDATE();  -- Date de fin passée

    -- Ouvrir le curseur
    OPEN vente_cursor;

    -- Lire la première ligne
    FETCH NEXT FROM vente_cursor INTO @noArticleParam, @dateFinEnchere, @dateDebutEnchere, @etatVente, @VendeurId;

    -- Boucle sur chaque vente
    WHILE @@FETCH_STATUS = 0
        BEGIN
            -- Mise à jour de l'état de la vente
            IF @etatVente <> 'Terminée'
                BEGIN

                    BEGIN TRANSACTION;

                    SELECT TOP 1 @EnchereMaxMontant = montant_enchere, @EnchereMaxUID = no_utilisateur
                    FROM Encheres WHERE no_article = @noArticleParam
                    ORDER BY montant_enchere DESC;

                    UPDATE ArticlesVendus
                    SET
                    etat_vente =
                        CASE
                            WHEN @dateFinEnchere < @Now THEN 'Terminée'
                            WHEN @dateDebutEnchere > @Now THEN 'Non commencée'
                            ELSE 'En cours'
                        END,
                        prix_vente = @EnchereMaxMontant
                    WHERE no_article = @noArticleParam;

                    UPDATE ArticlesVendus SET prix_vente = @EnchereMaxMontant WHERE no_article = @noArticle;

                    UPDATE Utilisateurs SET credit += @EnchereMaxMontant WHERE no_utilisateur = @VendeurId;

                    UPDATE Utilisateurs SET credit -= @EnchereMaxMontant WHERE no_utilisateur = @EnchereMaxUID;

                    COMMIT TRANSACTION;

                    set @NAffectedVente += 1;
                END

            -- Lire la prochaine ligne
            FETCH NEXT FROM vente_cursor INTO @noArticleParam, @dateFinEnchere, @etatVente;
        END;

    -- Fermer et libérer les ressources du curseur
    CLOSE vente_cursor;
    DEALLOCATE vente_cursor;

    return @NAffectedVente;
END;
