CREATE SCHEMA IF NOT EXISTS `accounts`;

CREATE TABLE IF NOT EXISTS `accounts`.`account`
(
    `login` VARCHAR(128) NOT NULL,
    `password` VARCHAR(256) NOT NULL,
    PRIMARY KEY (`login`)
);

