create table mandate
(
    active_from        date         not null,
    inactive_after     date         not null,
    deleted            varchar(36)  default 'false' not null,
    municipality_id    varchar(4)   not null,
    created            datetime(6)  not null,
    updated            datetime(6)  not null,
    grantee_party_id   varchar(36)  not null,
    grantor_party_id   varchar(36)  not null,
    id                 varchar(36)  not null primary key,
    signatory_party_id varchar(36)  not null,
    namespace          varchar(64)  not null,
    name               varchar(160) null,

    period for valid_date_range(active_from, inactive_after),

    unique (municipality_id, namespace, grantor_party_id, grantee_party_id, deleted, valid_date_range without overlaps)
);

create table bankid_signature
(
    bank_id_issue_date date         not null,
    mrtd_step_up       bit          null,
    personal_number    varchar(12)  not null,
    risk               varchar(20)  not null,
    status             varchar(20)  not null,
    id                 varchar(36)  not null primary key,
    mandate_id         varchar(36)  not null,
    order_ref          varchar(36)  not null,
    ip_address         varchar(45)  not null,
    given_name         varchar(255) not null,
    name               varchar(255) not null,
    ocsp_response      longtext     not null,
    signature_data     longtext     not null,
    surname            varchar(255) not null,
    uhi                text         not null,
    constraint fk_bankid_signature_mandate
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
