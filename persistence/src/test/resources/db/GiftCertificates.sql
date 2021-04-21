CREATE TABLE IF NOT EXISTS GiftCertificates (
        ID int NOT NULL AUTO_INCREMENT UNIQUE,
        Name varchar(45) NOT NULL,
        Description varchar(45) NOT NULL,
        Price DOUBLE NOT NULL,
        CreateDate datetime NOT NULL,
        LastUpdateDate datetime NOT NULL,
        Duration int NOT NULL,
        PRIMARY KEY (ID)
);