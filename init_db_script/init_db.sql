create table gift_certificates
(
    id               int auto_increment,
    name             varchar(45) not null,
    description      varchar(45) not null,
    price            double      not null,
    create_date      datetime    not null,
    last_update_date datetime    not null,
    duration         int         not null,
    constraint id_UNIQUE
        unique (id),
    constraint name_UNIQUE
        unique (name)
);

alter table gift_certificates
    add primary key (id);

create table tags
(
    id   int auto_increment,
    name varchar(45) not null,
    constraint Id_UNIQUE
        unique (id),
    constraint Name_UNIQUE
        unique (name)
);

alter table tags
    add primary key (id);

create table certificate_details
(
    tag_id         int not null,
    certificate_id int not null,
    primary key (tag_id, certificate_id),
    constraint FK_Certificate_Id
        foreign key (certificate_id) references gift_certificates (id),
    constraint FK_Tag_Id
        foreign key (tag_id) references tags (id)
);

create index FK_Certificate_Id_idx
    on certificate_details (certificate_id);

