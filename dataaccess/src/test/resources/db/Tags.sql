CREATE TABLE IF NOT EXISTS tags (
        id int NOT NULL AUTO_INCREMENT UNIQUE,
        name varchar(45) NOT NULL UNIQUE,
        PRIMARY KEY (id)
);
