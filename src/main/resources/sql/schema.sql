-- DROP SCHEMA dbo;

CREATE SCHEMA dbo;
-- eni_encheres.dbo.address definition

-- Drop table

-- DROP TABLE eni_encheres.dbo.address;

CREATE TABLE eni_encheres.dbo.address (
                                          id bigint NOT NULL,
                                          street varchar(255) COLLATE French_CI_AS NOT NULL,
                                          zipcode varchar(255) COLLATE French_CI_AS NOT NULL,
                                          city varchar(255) COLLATE French_CI_AS NOT NULL,
                                          CONSTRAINT address_id_primary PRIMARY KEY (id)
);


-- eni_encheres.dbo.category definition

-- Drop table

-- DROP TABLE eni_encheres.dbo.category;

CREATE TABLE eni_encheres.dbo.category (
                                           id bigint NOT NULL,
                                           label varchar(255) COLLATE French_CI_AS NOT NULL,
                                           CONSTRAINT category_id_primary PRIMARY KEY (id)
);


-- eni_encheres.dbo.article definition

-- Drop table

-- DROP TABLE eni_encheres.dbo.article;

CREATE TABLE eni_encheres.dbo.article (
                                          id bigint NOT NULL,
                                          name varchar(255) COLLATE French_CI_AS NOT NULL,
                                          description varchar(255) COLLATE French_CI_AS NOT NULL,
                                          idCategory bigint NOT NULL,
                                          CONSTRAINT article_id_primary PRIMARY KEY (id),
                                          CONSTRAINT article_idcategory_foreign FOREIGN KEY (idCategory) REFERENCES eni_encheres.dbo.category(id)
);


-- eni_encheres.dbo.person definition

-- Drop table

-- DROP TABLE eni_encheres.dbo.person;

CREATE TABLE eni_encheres.dbo.person (
                                         idUser bigint NOT NULL,
                                         firstname varchar(255) COLLATE French_CI_AS NOT NULL,
                                         lastname varchar(255) COLLATE French_CI_AS NOT NULL,
                                         phone varchar(255) COLLATE French_CI_AS NOT NULL,
                                         credit int NOT NULL,
                                         idAddress bigint NOT NULL,
                                         CONSTRAINT person_iduser_primary PRIMARY KEY (idUser),
                                         CONSTRAINT person_idaddress_foreign FOREIGN KEY (idAddress) REFERENCES eni_encheres.dbo.address(id)
);


-- eni_encheres.dbo.sale definition

-- Drop table

-- DROP TABLE eni_encheres.dbo.sale;

CREATE TABLE eni_encheres.dbo.sale (
                                       id bigint NOT NULL,
                                       idArticle bigint NOT NULL,
                                       idPersonSeller bigint NOT NULL,
                                       startDate datetime2 NOT NULL,
                                       endDate datetime2 NOT NULL,
                                       startPrice float NOT NULL,
                                       endPrice float NOT NULL,
                                       status bigint NOT NULL,
                                       idAddressWithdrawal bigint NULL,
                                       CONSTRAINT sale_id_primary PRIMARY KEY (id),
                                       CONSTRAINT sale_idaddresswithdrawal_foreign FOREIGN KEY (idAddressWithdrawal) REFERENCES eni_encheres.dbo.address(id),
                                       CONSTRAINT sale_idarticle_foreign FOREIGN KEY (idArticle) REFERENCES eni_encheres.dbo.article(id),
                                       CONSTRAINT sale_idpersonseller_foreign FOREIGN KEY (idPersonSeller) REFERENCES eni_encheres.dbo.person(idUser)
);


-- eni_encheres.dbo.[user] definition

-- Drop table

-- DROP TABLE eni_encheres.dbo.[user];

CREATE TABLE eni_encheres.dbo.[user] (
                                         id bigint NOT NULL,
                                         email varchar(255) COLLATE French_CI_AS NOT NULL,
    username varchar(255) COLLATE French_CI_AS NOT NULL,
    password varchar(255) COLLATE French_CI_AS NOT NULL,
    admin bit NOT NULL,
    active bit NOT NULL,
    CONSTRAINT user_id_primary PRIMARY KEY (id),
    CONSTRAINT user_id_foreign FOREIGN KEY (id) REFERENCES eni_encheres.dbo.person(idUser)
    );
CREATE UNIQUE NONCLUSTERED INDEX user_email_unique ON eni_encheres.dbo.user (  email ASC  )
	 WITH (  PAD_INDEX = OFF ,FILLFACTOR = 100  ,SORT_IN_TEMPDB = OFF , IGNORE_DUP_KEY = OFF , STATISTICS_NORECOMPUTE = OFF , ONLINE = OFF , ALLOW_ROW_LOCKS = ON , ALLOW_PAGE_LOCKS = ON  )
	 ON [PRIMARY ] ;
 CREATE UNIQUE NONCLUSTERED INDEX user_username_unique ON eni_encheres.dbo.user (  username ASC  )
	 WITH (  PAD_INDEX = OFF ,FILLFACTOR = 100  ,SORT_IN_TEMPDB = OFF , IGNORE_DUP_KEY = OFF , STATISTICS_NORECOMPUTE = OFF , ONLINE = OFF , ALLOW_ROW_LOCKS = ON , ALLOW_PAGE_LOCKS = ON  )
	 ON [PRIMARY ] ;


-- eni_encheres.dbo.auction definition

-- Drop table

-- DROP TABLE eni_encheres.dbo.auction;

CREATE TABLE eni_encheres.dbo.auction (
                                          idSale bigint NOT NULL,
                                          idBuyer bigint NOT NULL,
    [date] datetime2 NOT NULL,
                                          amount float NOT NULL,
                                          CONSTRAINT auction_pk PRIMARY KEY (idSale,idBuyer),
                                          CONSTRAINT auction_idbuyer_foreign FOREIGN KEY (idBuyer) REFERENCES eni_encheres.dbo.person(idUser),
                                          CONSTRAINT auction_idsale_foreign FOREIGN KEY (idSale) REFERENCES eni_encheres.dbo.sale(id)
);