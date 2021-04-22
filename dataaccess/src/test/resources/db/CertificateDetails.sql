CREATE TABLE IF NOT EXISTS CertificateDetails (
        TagID int NOT NULL,
        CertificateID int NOT NULL,
        PRIMARY KEY (TagID, CertificateID),
        CONSTRAINT FK_Certificate_Id FOREIGN KEY (CertificateID) REFERENCES GiftCertificates (ID),
        CONSTRAINT FK_Tag_Id FOREIGN KEY (TagID) REFERENCES Tags (ID)
);
