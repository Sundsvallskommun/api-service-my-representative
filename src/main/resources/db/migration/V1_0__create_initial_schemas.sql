create table mandate
(
    active_from        date                                             not null,
    created            datetime(6)                                      not null,
    deleted            datetime(6) default '1970-01-01 01:00:00.000000' not null,
    grantee_party_id   varchar(36)                                      not null,
    grantor_party_id   varchar(36)                                      not null,
    id                 varchar(36)                                      not null primary key,
    inactive_after     date                                             not null,
    municipality_id    varchar(4)                                       not null,
    name               varchar(160)                                     null,
    namespace          varchar(128)                                     not null,
    signatory_party_id varchar(36)                                      not null,
    updated            datetime(6)                                      not null,

    period for valid_date_range(active_from, inactive_after),

    constraint unique_mandate_per_period
        unique (municipality_id, namespace, grantor_party_id, grantee_party_id, deleted,
                valid_date_range without overlaps)
);

create table signing_information
(
    bank_id_issue_date date         not null,
    given_name         varchar(255) not null,
    id                 varchar(36)  not null primary key,
    ip_address         varchar(45)  not null,
    mandate_id         varchar(36)  not null,
    mrtd               bit          null,
    name               varchar(255) not null,
    ocsp_response      longtext     not null,
    order_ref          varchar(36)  not null,
    personal_number    varchar(12)  not null,
    risk               varchar(20)  not null,
    signature_data     longtext     not null,
    signed             datetime(6)  not null,
    status             varchar(20)  not null,
    surname            varchar(255) not null,
    uhi                text         not null,
    constraint fk_signing_information_mandate
        foreign key (mandate_id) references mandate (id)
);

create index idx_municipality_id_grantee
    on mandate (municipality_id, grantee_party_id);

create index idx_municipality_id_grantor_party_id
    on mandate (municipality_id, grantor_party_id);

create index idx_municipality_id_namespace
    on mandate (municipality_id, namespace);

create index idx_municipality_id_signatory_party_id
    on mandate (municipality_id, signatory_party_id);
