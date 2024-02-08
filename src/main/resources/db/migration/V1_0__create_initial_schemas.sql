CREATE TABLE jwk (
    id BIGINT NOT NULL AUTO_INCREMENT,
    jwk_json LONGTEXT,
    created DATETIME(6) NOT NULL,
    valid_until DATETIME(6) NOT NULL,
    PRIMARY KEY (id)
) engine=InnoDB;

CREATE TABLE shedlock (
    name VARCHAR(64),
    lock_until TIMESTAMP(3) NULL,
    locked_at TIMESTAMP(3) NULL,
    locked_by VARCHAR(255),
    PRIMARY KEY (name)
) engine=InnoDB;