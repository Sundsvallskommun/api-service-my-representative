create table mandate_template
(
    code        varchar(64) primary key,
    title       varchar(128)  not null,
    description varchar(2000) not null
) engine = InnoDB;
