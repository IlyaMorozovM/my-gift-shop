CREATE TABLE IF NOT EXISTS certificate_details (
        tag_id int NOT NULL,
        certificate_id int NOT NULL,
        PRIMARY KEY (tag_id, certificate_id),
        CONSTRAINT FK_Certificate_Id FOREIGN KEY (certificate_id) REFERENCES gift_certificates (id),
        CONSTRAINT FK_Tag_Id FOREIGN KEY (tag_id) REFERENCES Tags (id)
);
