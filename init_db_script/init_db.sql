create table giftcertificates
(
    ID             int auto_increment,
    Name           varchar(45) not null,
    Description    varchar(45) not null,
    Price          double      not null,
    CreateDate     datetime    not null,
    LastUpdateDate datetime    not null,
    Duration       int         not null,
    constraint id_UNIQUE
        unique (ID)
);

alter table gift_certificates
    add primary key (id);

create table tags
(
    ID   int auto_increment,
    Name varchar(45) not null,
    constraint Id_UNIQUE
        unique (ID),
    constraint Name_UNIQUE
        unique (Name)
);

alter table tags
    add primary key (id);

create table certificatedetails
(
    TagID         int not null,
    CertificateID int not null,
    primary key (TagID, CertificateID),
    constraint FK_Certificate_Id
        foreign key (CertificateID) references gift_certificates (id),
    constraint FK_Tag_Id
        foreign key (TagID) references tags (id)
);

create index FK_Certificate_Id_idx
    on certificate_details (certificate_id);

