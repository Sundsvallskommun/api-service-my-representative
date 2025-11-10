alter table signing_information
    add column external_transaction_id varchar(36) not null;

alter table signing_information
    modify column bank_id_issue_date date null,
    modify column mrtd boolean null,
    modify column name varchar(255) null,
    modify column ocsp_response longtext null,
    modify column risk varchar(20) null,
    modify column uhi text null;
