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

alter table giftcertificates
    add primary key (ID);

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
    add primary key (ID);

create table certificatedetails
(
    TagID         int not null,
    CertificateID int not null,
    primary key (TagID, CertificateID),
    constraint FK_Certificate_Id
        foreign key (CertificateID) references giftcertificates (ID),
    constraint FK_Tag_Id
        foreign key (TagID) references tags (ID)
);

create index FK_Certificate_Id_idx
    on certificatedetails (CertificateID);

