USE [eni_encheres]
GO
/****** Object:  StoredProcedure [dbo].[updaterEtatVentes]    Script Date: 05/01/2026 15:03:02 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- pour terminer les ventes et refaire les soldes de vendeurs et acquéreurs
CREATE PROCEDURE updaterEtatVentes
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
        @NouvelEtatVente VARCHAR(50),
        @PrixInitial INT,
        @Now DATETIME2 = GETUTCDATE();

    -- Déclaration du curseur en dehors des conditions (c'est ici que la portée du curseur est correcte)
    DECLARE vente_cursor CURSOR FOR
        -- Si @noArticle est NULL, on sélectionne toutes les ventes
        -- Sinon, on sélectionne la vente spécifiée par @noArticle
        SELECT no_article, date_fin_encheres, date_debut_encheres, etat_vente, no_utilisateur, prix_initial
        FROM ArticlesVendus
        WHERE
            (@noArticle IS NULL OR no_article = @noArticle)  -- Si @noArticle est NULL, il sélectionne toutes les ventes
          AND etat_vente not in ('Terminée', 'Annulée');  -- Seules les ventes non terminées
    --AND date_fin_encheres <= GETDATE();  -- Date de fin passée

    -- Ouvrir le curseur
    OPEN vente_cursor;

    -- Lire la première ligne
    FETCH NEXT FROM vente_cursor INTO @noArticleParam, @dateFinEnchere, @dateDebutEnchere, @etatVente, @VendeurId, @PrixInitial;

    -- Boucle sur chaque vente
    WHILE @@FETCH_STATUS = 0
        BEGIN
            -- Mise à jour de l'état de la vente
            SET @NouvelEtatVente =
                    CASE
                        WHEN @dateFinEnchere <= @Now THEN 'Terminée'
                        WHEN @dateDebutEnchere > @Now THEN 'Non commencée'
                        ELSE 'En cours'
                        END;

            BEGIN TRANSACTION;

            SELECT TOP 1
                @EnchereMaxMontant = montant_enchere,
                @EnchereMaxUID = no_utilisateur
            FROM Encheres
            WHERE no_article = @noArticleParam
            ORDER BY montant_enchere DESC;


            PRINT 'no_article: ' + CAST(@noArticleParam AS VARCHAR);
            PRINT 'EtatVente: ' + @etatVente;
            PRINT 'NouvelEtatVente: ' + @NouvelEtatVente;
            PRINT 'PrixInitial : ' + cast(@PrixInitial as varchar);
            PRINT 'EnchereMax : ' + CAST(@EnchereMaxMontant as varchar);

            UPDATE ArticlesVendus
            SET etat_vente = @NouvelEtatVente
            --, prix_vente = coalesce(@EnchereMaxMontant, @PrixInitial)
            WHERE no_article = @noArticleParam;



            IF @etatVente <> 'Terminée' AND @NouvelEtatVente = 'Terminée' and @EnchereMaxUID IS NOT NULL
                BEGIN
                    -- cloture de vente, on ajuste les credits acheteur / vendeur

                    PRINT 'VendeurId: ' + CAST(@VendeurId AS VARCHAR);
                    PRINT 'AcheteurId: ' + CAST(@EnchereMaxUID AS VARCHAR);

                    UPDATE Utilisateurs
                    SET credit = credit + ISNULL(@EnchereMaxMontant, 0)
                    WHERE no_utilisateur = @VendeurId;

                    PRINT 'Vendeur crédit : ' + CAST(@@ROWCOUNT AS VARCHAR);

                    UPDATE Utilisateurs
                    SET credit = credit - @EnchereMaxMontant
                    WHERE no_utilisateur = @EnchereMaxUID;

                    PRINT 'Acheteur crédit : ' + CAST(@@ROWCOUNT AS VARCHAR);
                END

            COMMIT TRANSACTION;

            PRINT 'Lignes affectées: ' + CAST(@@ROWCOUNT AS VARCHAR);

            set @NAffectedVente += 1;

            -- Lire la prochaine ligne
            FETCH NEXT FROM vente_cursor INTO @noArticleParam, @dateFinEnchere, @dateDebutEnchere, @etatVente, @VendeurId, @PrixInitial;
        END;

    -- Fermer et libérer les ressources du curseur
    CLOSE vente_cursor;
    DEALLOCATE vente_cursor;

    return @NAffectedVente;
END;
