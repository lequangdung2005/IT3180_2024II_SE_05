CREATE TABLE IF NOT EXISTS person (
    person_id int not null auto_increment,
    family_id int,
    fullname varchar(64) not null,
    date_of_birth date,
    citizen_identification_number varchar(64) not null,
    phone_number varchar(16) not null,
    sex varchar(8) not null,
    nationality varchar(64) not null,
    residence_status varchar(8) not null,
    PRIMARY KEY(person_id, family_id)
);

CREATE TABLE IF NOT EXISTS family (
    family_id int not null auto_increment,
    person_id int,
    house_number varchar(255) not null,
    PRIMARY KEY(family_id, person_id),
    FOREIGN KEY(person_id)
        REFERENCES person (person_id)
        ON UPDATE RESTRICT
);

CREATE TABLE IF NOT EXISTS account (
    account_id int not null auto_increment,
    family_id int,
    username varchar(32) not null,
    digest_password varchar(64) not null,
    account_type varchar(32) not null,
    PRIMARY KEY(account_id, family_id),
    FOREIGN KEY(family_id)
        REFERENCES family (family_id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS vehicle (
    vehicle_id int not null auto_increment,
    vehicle_type varchar(64) not null,
    family_id int,
    plate_id varchar(16) not null,
    PRIMARY KEY (vehicle_id, family_id),
    FOREIGN KEY (family_id)
        REFERENCES family(family_id)
        ON UPDATE RESTRICT
);

CREATE TABLE IF NOT EXISTS expense (
    expense_id int not null auto_increment,
    expense_title varchar(256) not null,
    expense_description varchar(256) not null,
    published_date date,
    total_cost int,
    expense_type varchar(32),
    required tinyint(1),
    PRIMARY KEY(expense_id)
);

CREATE TABLE IF NOT EXISTS payment_status (
    payment_status_id int not null auto_increment,
    expense_id int,
    family_id int,
    total_pay int,
    published_date date,
    PRIMARY KEY(payment_status_id, expense_id, family_id),
    FOREIGN KEY(expense_id)
        REFERENCES expense(expense_id)
        ON UPDATE RESTRICT,
    FOREIGN KEY(family_id)
        REFERENCES family(family_id)
        ON UPDATE RESTRICT
);

CREATE TABLE IF NOT EXISTS temporary_stay_absent_request (
    temporary_stay_absent_request_id int not null auto_increment,
    person_id int,
    family_id int,
    fullname varchar(64) not null,
    date_of_birth date,
    citizen_identification_number varchar(64) not null,
    phone_number varchar(16) not null,
    sex varchar(8) not null,
    nationality varchar(64) not null,
    residence_status varchar(8) not null,
    PRIMARY KEY(temporary_stay_absent_request_id)
);

CREATE TABLE IF NOT EXISTS vehicle (
    vehicle_id int not null auto_increment,
    vehicle_type varchar (64),
    family_id int,
    plate_id varchar (64),
    PRIMARY KEY(vehicle_id)
);
