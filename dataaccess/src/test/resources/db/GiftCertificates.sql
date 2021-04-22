CREATE TABLE IF NOT EXISTS gift_certificates (
        id int NOT NULL AUTO_INCREMENT UNIQUE,
        name varchar(45) NOT NULL,
        description varchar(45) NOT NULL,
        price DOUBLE NOT NULL,
        create_date datetime NOT NULL,
        last_update_date datetime NOT NULL,
        duration int NOT NULL,
        PRIMARY KEY (id)
);