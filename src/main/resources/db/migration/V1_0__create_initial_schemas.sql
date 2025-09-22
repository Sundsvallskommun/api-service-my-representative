create table mandate
(
    municipality_id    varchar(4)   not null,
    created            datetime(6)  not null,
    updated            datetime(6)  not null,
    valid_from         datetime(6)  not null,
    valid_to           datetime(6)  not null,
    grantor_party_id   varchar(36)  not null,
    id                 varchar(36)  not null primary key,
    signatory_party_id varchar(36)  not null,
    name               varchar(160) null,
    status             varchar(64)  not null
);

create table bankid_signature
(
    bank_id_issue_date date         not null,
    mrtd_step_up       bit          null,
    id                 bigint auto_increment primary key,
    personal_number    varchar(12)  not null,
    risk               varchar(20)  not null,
    status             varchar(20)  not null,
    mandate_id         varchar(36)  not null,
    order_ref          varchar(36)  not null,
    ip_address         varchar(45)  not null,
    given_name         varchar(255) not null,
    name               varchar(255) null,
    ocsp_response      longtext     not null,
    signature_data     longtext     not null,
    surname            varchar(255) not null,
    uhi                text         not null,
    constraint fk_bankid_signature_mandate
        foreign key (mandate_id) references mandate (id)
);

create table grantee
(
    id         varchar(36) not null primary key,
    mandate_id varchar(36) not null,
    party_id   varchar(36) not null,
    
    constraint fk_grantee_mandate
        foreign key (mandate_id) references mandate (id)
);

create index idx_mandate_id
    on grantee (mandate_id);

create index idx_party_id
    on grantee (party_id);

create index idx__municipality_id_signatory_party_id
    on mandate (municipality_id, signatory_party_id);

create index idx_municipality_id_grantor_party_id
    on mandate (municipality_id, grantor_party_id);

