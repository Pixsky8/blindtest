CREATE SCHEMA IF NOT EXISTS `accounts`;

CREATE TABLE IF NOT EXISTS `accounts`.`account`
(
    `login` VARCHAR(32) NOT NULL,
    `password` VARCHAR(256) NOT NULL,
    PRIMARY KEY (`login`)
);

CREATE TABLE IF NOT EXISTS `accounts`.`admin`
(
    `login` VARCHAR(32) NOT NULL,
    FOREIGN KEY(`login`) REFERENCES `accounts`.`account`(`login`)
);

INSERT INTO `accounts`.`account`
VALUES ('admin', '902b01b5c6a09c2b9bc01809e4d93019a1d8271d95067cc679a6bc28480b52e0');

IF NOT EXISTS (SELECT * FROM `accounts`.`admin`
               WHERE `login` = 'admin')
BEGIN
    INSERT INTO `accounts`.`admin`
    VALUES ('admin')
END;
