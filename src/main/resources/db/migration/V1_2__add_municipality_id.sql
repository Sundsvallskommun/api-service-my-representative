alter table if exists jwk
    add column municipality_id varchar(255);

alter table if exists mandate_template
    add column municipality_id varchar(255);