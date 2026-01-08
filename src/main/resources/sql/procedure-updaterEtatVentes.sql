USE [eni_encheres]
GO
/****** Object:  StoredProcedure [dbo].[updaterEtatVentes]    Script Date: 05/01/2026 15:03:02 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- pour terminer les ventes et refaire les soldes de vendeurs et acquéreurs
CREATE PROCEDURE updaterEtatVentes
    @noArticle INT = NULL
    AS
    BEGIN
        SET NOCOUNT ON;

        DECLARE @Now DATETIME2 = SYSDATETIME();

        -------------------------------------------------------------------
        -- 0️⃣ Non commencée → En cours
        -------------------------------------------------------------------
        UPDATE ArticlesVendus
        SET etat_vente = 'En cours'
        WHERE
            (@noArticle IS NULL OR no_article = @noArticle)
          AND etat_vente = 'Non commencée'
          AND date_debut_encheres <= @Now
          AND date_fin_encheres > @Now;

        -------------------------------------------------------------------
        -- 1️⃣ Ventes qui VIENNENT de passer à Terminée
        -------------------------------------------------------------------
        DECLARE @VentesTerminees TABLE (
                                           no_article INT PRIMARY KEY,
                                           vendeur_id INT
                                       );

        UPDATE ArticlesVendus
        SET etat_vente = 'Terminée'
        OUTPUT
            inserted.no_article,
            inserted.no_utilisateur
            INTO @VentesTerminees (no_article, vendeur_id)
        WHERE
            (@noArticle IS NULL OR no_article = @noArticle)
          AND etat_vente NOT IN ('Terminée', 'Annulée')
          AND date_fin_encheres <= @Now;

        IF NOT EXISTS (SELECT 1 FROM @VentesTerminees)
            RETURN 0;

            -------------------------------------------------------------------
            -- 2️⃣ Enchère max par vente
            -------------------------------------------------------------------
            ;WITH EnchereMax AS (
            SELECT
                e.no_article,
                e.no_utilisateur AS acheteur_id,
                e.montant_enchere,
                ROW_NUMBER() OVER (
                    PARTITION BY e.no_article
                    ORDER BY e.montant_enchere DESC, e.no_enchere DESC
                    ) AS rn
            FROM Encheres e
                     JOIN @VentesTerminees vt ON vt.no_article = e.no_article
        )
             SELECT
                 em.no_article,
                 vt.vendeur_id,
                 em.acheteur_id,
                 em.montant_enchere,
                 u.credit AS credit_acheteur
             INTO #VentesAnalyse
             FROM EnchereMax em
                      JOIN @VentesTerminees vt ON vt.no_article = em.no_article
                      LEFT JOIN Utilisateurs u ON u.no_utilisateur = em.acheteur_id
             WHERE em.rn = 1;

        -------------------------------------------------------------------
        -- 3️⃣ Séparation : soldables / non soldables
        -------------------------------------------------------------------
        SELECT *
        INTO #VentesASolder
        FROM #VentesAnalyse
        WHERE montant_enchere IS NOT NULL
          AND credit_acheteur >= montant_enchere;

        SELECT *
        INTO #VentesCreditInsuffisant
        FROM #VentesAnalyse
        WHERE montant_enchere IS NULL
           OR credit_acheteur < montant_enchere;

        -------------------------------------------------------------------
        -- 4️⃣ Solde (transaction)
        -------------------------------------------------------------------
        BEGIN TRAN;

        UPDATE u
        SET credit = credit + v.montant_enchere
        FROM Utilisateurs u
                 JOIN #VentesASolder v ON v.vendeur_id = u.no_utilisateur;

        UPDATE u
        SET credit = credit - v.montant_enchere
        FROM Utilisateurs u
                 JOIN #VentesASolder v ON v.acheteur_id = u.no_utilisateur;

        COMMIT;

        -------------------------------------------------------------------
        -- 5️⃣ Résultat
        -------------------------------------------------------------------
        DECLARE @NbSoldees INT = (SELECT COUNT(*) FROM #VentesASolder);

        SELECT
            no_article,
            acheteur_id,
            montant_enchere,
            credit_acheteur
        FROM #VentesCreditInsuffisant;

        RETURN @NbSoldees;
    END;
