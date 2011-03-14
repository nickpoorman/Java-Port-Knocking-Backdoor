CREATE DATABASE IF NOT EXISTS tea_database;

USE tea_database;

CREATE TABLE IF NOT EXISTS companies(
company_id          INT NOT NULL AUTO_INCREMENT,
company_name        VARCHAR(80) NOT NULL,
contact_first_name  VARCHAR(30),
contact_last_name   VARCHAR(30),
contact_phone       VARCHAR(15),
contact_cell        VARCHAR(15),
contact_email       VARCHAR(80),
street              VARCHAR(80),
city                VARCHAR(80),
state               VARCHAR(20),
zip                 VARCHAR(10),
alt_mailing_address VARCHAR(80),
alt_city            VARCHAR(80),
alt_state           VARCHAR(20),
alt_zip             VARCHAR(10),
billing_address     VARCHAR(80),
billing_city        VARCHAR(80),
billing_state       VARCHAR(20),
billing_zip         VARCHAR(10),
billing_contact     VARCHAR(80),
billing_phone       VARCHAR(80),
billing_email       VARCHAR(80),
PRIMARY KEY(company_id)
);

/*
INSERT INTO companies (company_name, contact_first_name, contact_last_name)
VALUES ('TEA Inc', 'Kendall', 'Moore');
*/
/*
DROP TABLE rates;
*/

CREATE TABLE IF NOT EXISTS rates(
plan_code_id        INT NOT NULL AUTO_INCREMENT,
company_id          INT NOT NULL,
plan_code           VARCHAR(80),
s_field             VARCHAR(80),
two_p               VARCHAR(80),
e_ch                VARCHAR(80),
e_sp                VARCHAR(80),
f_field             VARCHAR(80),
contracts           VARCHAR(80),
renewal             DATE,
filing              DATE,
medical_or_dental   VARCHAR(80),
PRIMARY KEY(plan_code_id),
FOREIGN KEY(company_id) REFERENCES companies(company_id)
);

INSERT INTO rates (company_id, plan_code, s_field, two_p, e_ch, e_sp, f_field, contracts, renewal, filing, medical_or_dental)
VALUES (1, 'Test Plan Code', 'S', '2p', 'e/ch', 'e/sp', 'F', '2', sysdate(), sysdate(), 'medical');

INSERT INTO rates (company_id, plan_code, s_field, two_p, e_ch, e_sp, f_field, contracts, renewal, filing, medical_or_dental)
VALUES (22, 'Test Plan Code2', 'S', '2p', 'e/ch', 'e/sp', 'F', '2', '2019-08-30', '2019-08-30', 'other');

CREATE TABLE IF NOT EXISTS fields(
field_id            INT NOT NULL AUTO_INCREMENT,
field_name          VARCHAR(80),
PRIMARY KEY(field_id)
);

INSERT INTO fields(field_name) VALUES('OV');
INSERT INTO fields(field_name) VALUES('RX');
INSERT INTO fields(field_name) VALUES('I/P');
INSERT INTO fields(field_name) VALUES('Ded');
INSERT INTO fields(field_name) VALUES('Coins');
INSERT INTO fields(field_name) VALUES('Stu');
INSERT INTO fields(field_name) VALUES('Dep');
INSERT INTO fields(field_name) VALUES('Pre-Ex');
INSERT INTO fields(field_name) VALUES('T Law Buy Up');
INSERT INTO fields(field_name) VALUES('OOP Max');

CREATE TABLE IF NOT EXISTS field_values(
field_id            INT NOT NULL,
field_value         VARCHAR(80),
rates_id            INT NOT NULL,
FOREIGN KEY(field_id) REFERENCES fields(field_id),
FOREIGN KEY(rates_id) REFERENCES rates(plan_code_id)
);

/*
INSERT INTO field_values(field_id, field_value, rates_id) VALUES(1, 'None', 1);
INSERT INTO field_values(field_id, field_value, rates_id) VALUES(2, 'None', 1);
INSERT INTO field_values(field_id, field_value, rates_id) VALUES(3, 'None', 1);
INSERT INTO field_values(field_id, field_value, rates_id) VALUES(4, 'None', 1);
INSERT INTO field_values(field_id, field_value, rates_id) VALUES(5, 'None', 1);
INSERT INTO field_values(field_id, field_value, rates_id) VALUES(6, 'None', 1);
INSERT INTO field_values(field_id, field_value, rates_id) VALUES(7, 'None', 1);
INSERT INTO field_values(field_id, field_value, rates_id) VALUES(8, 'None', 1);
INSERT INTO field_values(field_id, field_value, rates_id) VALUES(9, 'None', 1);
INSERT INTO field_values(field_id, field_value, rates_id) VALUES(10, 'None', 1);
*/

CREATE TABLE IF NOT EXISTS drop_downs(
drop_down_id        INT NOT NULL AUTO_INCREMENT,
drop_down_name      VARCHAR(80),
PRIMARY KEY(drop_down_id)
);

INSERT INTO drop_downs(drop_down_name) VALUES('Status');
INSERT INTO drop_downs(drop_down_name) VALUES('Group Size');
INSERT INTO drop_downs(drop_down_name) VALUES('Rating');

DROP TABLE drop_down_options;
CREATE TABLE IF NOT EXISTS drop_down_options(
option_id           INT NOT NULL AUTO_INCREMENT,
drop_down_id        INT NOT NULL,
menu_option         VARCHAR(80),
PRIMARY KEY(option_id),
FOREIGN KEY(drop_down_id) REFERENCES drop_downs(drop_down_id)
);

/* The Company Status Options */
INSERT INTO drop_down_options(drop_down_id, menu_option) VALUES(1, 'None');
INSERT INTO drop_down_options(drop_down_id, menu_option) VALUES(1, 'Active');
INSERT INTO drop_down_options(drop_down_id, menu_option) VALUES(1, 'Inactive');
INSERT INTO drop_down_options(drop_down_id, menu_option) VALUES(1, 'Prospect');
INSERT INTO drop_down_options(drop_down_id, menu_option) VALUES(1, 'Terminated');
/*The Company Group Size Options*/
INSERT INTO drop_down_options(drop_down_id, menu_option) VALUES(2, 'None');
INSERT INTO drop_down_options(drop_down_id, menu_option) VALUES(2, 'Under 20');
INSERT INTO drop_down_options(drop_down_id, menu_option) VALUES(2, 'Over 20');
INSERT INTO drop_down_options(drop_down_id, menu_option) VALUES(2, 'Sole Proprietor');
INSERT INTO drop_down_options(drop_down_id, menu_option) VALUES(2, 'Individual');
/*The Rating Options*/
INSERT INTO drop_down_options(drop_down_id, menu_option) VALUES(3, 'None');
INSERT INTO drop_down_options(drop_down_id, menu_option) VALUES(3, 'Comm');
INSERT INTO drop_down_options(drop_down_id, menu_option) VALUES(3, 'Exper');
INSERT INTO drop_down_options(drop_down_id, menu_option) VALUES(3, 'Exchange');
INSERT INTO drop_down_options(drop_down_id, menu_option) VALUES(3, 'Assoc');
/*The Classes Options*/
INSERT INTO drop_down_options(drop_down_id, menu_option) VALUES(4, 'Mgmt');
INSERT INTO drop_down_options(drop_down_id, menu_option) VALUES(4, 'Non-Mgmt');
INSERT INTO drop_down_options(drop_down_id, menu_option) VALUES(4, 'Salary');
INSERT INTO drop_down_options(drop_down_id, menu_option) VALUES(4, 'Hourly');


CREATE TABLE IF NOT EXISTS company_drop_downs(
drop_down_id        INT NOT NULL,
option_id           INT NOT NULL,
company_id          INT NOT NULL,
FOREIGN KEY(drop_down_id) REFERENCES drop_downs(drop_down_id),
FOREIGN KEY(option_id)  REFERENCES drop_down_options(option_id),
FOREIGN KEY(company_id) REFERENCES companies(company_id)
);

/*
INSERT INTO company_drop_downs(drop_down_id, option_id, company_id)
VALUES(1, 2, 1);
*/

/*New Stuff -->*/
CREATE TABLE IF NOT EXISTS plans(
company_id          INT NOT NULL,
union_yes_no        VARCHAR(20),
submitted           DATE DEFAULT NULL,
initials            VARCHAR(20),
approval_date       DATE DEFAULT NULL,
welcome_letter      DATE DEFAULT NULL,
effective_date      DATE DEFAULT NULL,
FOREIGN KEY(company_id) REFERENCES companies(company_id)
);

CREATE TABLE IF NOT EXISTS plans_class_info(
company_id          INT NOT NULL,
class_id            INT NOT NULL,
waiting_period      VARCHAR(50),
eligibles           VARCHAR(50),
FOREIGN KEY(company_id) REFERENCES companies(company_id),
FOREIGN KEY(class_id)   REFERENCES drop_down_options(option_id)
);

/*Changed this*/
CREATE TABLE IF NOT EXISTS company_owners(
company_id          INT NOT NULL,
owner_id            INT NOT NULL AUTO_INCREMENT,
owner_name          VARCHAR(50),
percent_ownership   VARCHAR(10),
PRIMARY KEY(owner_id),
FOREIGN KEY(company_id) REFERENCES companies(company_id)
);

CREATE TABLE IF NOT EXISTS group_numbers(
company_id          INT NOT NULL,
group_number_id     INT NOT NULL AUTO_INCREMENT,
group_number        VARCHAR(50),
PRIMARY KEY(group_number_id),
FOREIGN KEY(company_id) REFERENCES companies(company_id)
);

/*
CREATE TABLE IF NOT EXISTS files(
file_id             INT NOT NULL AUTO_INCREMENT,
company_id          INT NOT NULL,
PRIMARY KEY(file_id),
FOREIGN KEY(company_id) REFERENCES companies(company_id)
);

CREATE TABLE IF NOT EXISTS file_labels(
label_id            INT NOT NULL AUTO_INCREMENT,
file_id             INT NOT NULL,
file_label          VARCHAR(50),
PRIMARY KEY(label_id),
FOREIGN KEY(file_id) REFERENCES files(file_id)
);

CREATE TABLE IF NOT EXISTS file_names(
label_id            INT NOT NULL,
file_name           VARCHAR(200),
FOREIGN KEY(label_id)   REFERENCES file_labels(label_id)
);
*/

CREATE TABLE IF NOT EXISTS files(
company_id          INT NOT NULL,
file_id             INT NOT NULL AUTO_INCREMENT,
file_name           VARCHAR(200) NOT NULL,
file_label          VARCHAR(50) NOT NULL,
PRIMARY KEY(file_id),
FOREIGN KEY(company_id) REFERENCES companies(company_id)
);

/*
CREATE TABLE IF NOT EXISTS to_do_tasks(
task			    VARCHAR(200),
to_do_date		    DATE,
completed		    BOOLEAN DEFAULT FALSE
);
*/

CREATE TABLE IF NOT EXISTS calendar(
task_id                     INT NOT NULL AUTO_INCREMENT,
task                        VARCHAR(300),
company_name                VARCHAR(80),
start_date                  DATE NOT NULL,
completed_date              DATE,
PRIMARY KEY(task_id)
);

/*FOREIGN KEY(company_name) REFERENCES companies(company_name)*/