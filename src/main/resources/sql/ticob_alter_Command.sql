/*** New Query Date 25-03-2019 ***/
CREATE TABLE `transfer_in`(
`TRANSFER_IN_ID` INT(50) NOT NULL AUTO_INCREMENT,
`UNIQUE_ID` INT(50) DEFAULT NULL,
`FOLIO_NUMBER` VARCHAR(126) DEFAULT NULL,
`SCHEME_CODE` VARCHAR(512) DEFAULT NULL,
`INVESTER_NAME` VARCHAR(128) DEFAULT NULL,
`STARTED_ON` VARCHAR(126) DEFAULT NULL,
`PRICE` DECIMAL(20,5) NOT NULL DEFAULT '0.00000',
`UNIT` DECIMAL(20,5) NOT NULL DEFAULT '0.00000',
`AVAILABLE_UNIT` DECIMAL(20,5) DEFAULT '0.00000',
`AMOUNT` DECIMAL(20,5) NOT NULL DEFAULT '0.00000',
`PAN` VARCHAR(128) DEFAULT NULL,
`RTA_AGENT` VARCHAR(50) DEFAULT NULL,
`TRANSACTION_TYPE` VARCHAR(50) DEFAULT NULL,
`SIP_REG_NO` VARCHAR(126) DEFAULT NULL,
`INVESTMENT_TYPE` VARCHAR(50) DEFAULT NULL,
`TIME_CREATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
`LAST_UPDATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
`STATUS` VARCHAR(50) DEFAULT NULL,
`STATUS_CODE` INT(5) DEFAULT '0',
`FIELD1` DECIMAL(20,5) NOT NULL DEFAULT '0.00000',
`USER_ID` INT(50) NOT NULL DEFAULT '0',
`FIELD3` VARCHAR(50) DEFAULT NULL,
`FIELD4` VARCHAR(128) DEFAULT NULL,
PRIMARY KEY(`TRANSFER_IN_ID`)
)ENGINE=INNODB DEFAULT CHARSET=utf8;