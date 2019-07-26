CREATE TABLE `user` (
  `USER_ID` INT(50) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(126) NOT NULL UNIQUE,
  `PASSWORD` VARCHAR(126) NOT NULL,
  `MOBILE_NUMBER` INT(15) DEFAULT NULL,
  `REGISTER_TYPE` VARCHAR(126) NOT NULL,
  `EMAIL` VARCHAR(126) DEFAULT NULL,
  `FIRST_NAME` VARCHAR(126) DEFAULT NULL,
  `LAST_NAME` VARCHAR(126) DEFAULT NULL,
  `GENDER` VARCHAR(50) DEFAULT NULL,
   `STATUS` VARCHAR(10) DEFAULT NULL,
  `DATE_OF_BIRTH` DATE DEFAULT NULL,
  `CREATED_TIMESTAMP` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `UPDATED_DATE_TIME` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `FIELD1` INT(50) DEFAULT NULL,
  `FIELD2` VARCHAR(128) DEFAULT NULL,
  `FIELD3` VARCHAR(128) DEFAULT NULL,
  `FIELD4` VARCHAR(128) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


CREATE TABLE `authority` (
  `AUTHORITY_ID` INT(50) NOT NULL AUTO_INCREMENT,
  `AUTHORITY_TYPE` VARCHAR(126) DEFAULT NULL,
  PRIMARY KEY (`AUTHORITY_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `user_authority` (
  `USER_ID` INT(50) NOT NULL,
  `AUTHORITY_ID` INT(50) NOT NULL,
  PRIMARY KEY (`USER_ID`,`AUTHORITY_ID`),
  FOREIGN KEY (`USER_ID`) REFERENCES `user` (`USER_ID`),
  FOREIGN KEY (`AUTHORITY_ID`) REFERENCES `authority` (`AUTHORITY_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `user_profile` (
  `USER_PROFILE_ID` INT(50) NOT NULL,
  `AADHAAR_NUMBER` VARCHAR(128) DEFAULT NULL,
  `PAN_NUMBER` VARCHAR(128) DEFAULT NULL,
  `CREATED_TIMESTAMP` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `UPDATED_DATE_TIME` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `FIELD1` INT(50) DEFAULT NULL,
  `FIELD2` VARCHAR(128) DEFAULT NULL,
  PRIMARY KEY (`USER_PROFILE_ID`),
  FOREIGN KEY (`USER_PROFILE_ID`) REFERENCES `user` (`USER_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `agent_profile` (
  `AGENT_PROFILE_ID` INT(50) NOT NULL,
  `ARN_CODE` VARCHAR(128) DEFAULT NULL,
  `AADHAAR_NUMBER` VARCHAR(128) DEFAULT NULL,
  `PAN_NUMBER` VARCHAR(128) DEFAULT NULL,
  `CREATED_TIMESTAMP` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `UPDATED_DATE_TIME` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `FIELD1` INT(50) DEFAULT NULL,
  `FIELD2` VARCHAR(128) DEFAULT NULL,
   PRIMARY KEY (`AGENT_PROFILE_ID`),
   FOREIGN KEY (`AGENT_PROFILE_ID`) REFERENCES `user` (`USER_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `role` (
  `ROLE_ID` INT(50) NOT NULL AUTO_INCREMENT,
  `ROLE_NAME` VARCHAR(126) DEFAULT NULL,
  PRIMARY KEY (`ROLE_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;



CREATE TABLE `user_role` (
  `ROLE_ID` INT(50) NOT NULL,
  `USER_ID` INT(50) NOT NULL UNIQUE,
  PRIMARY KEY (`ROLE_ID`,`USER_ID`),
  FOREIGN KEY (`USER_ID`) REFERENCES `user` (`USER_ID`),
  FOREIGN KEY (`ROLE_ID`) REFERENCES `role` (`ROLE_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;




CREATE TABLE `address` (
  `ADDRESS_ID` INT(50) NOT NULL AUTO_INCREMENT,
  `USER_ID` INT(50) NOT NULL,
  `COUNTRY` VARCHAR(126) DEFAULT NULL,
  `STATE` VARCHAR(126) DEFAULT NULL,
  `CITY` VARCHAR(126) DEFAULT NULL,
  `PINCODE` VARCHAR(126) DEFAULT NULL,
  `REGISTER_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `FIELD1` INT(50) DEFAULT NULL,
  `FIELD2` VARCHAR(128) DEFAULT NULL,
  PRIMARY KEY (`ADDRESS_ID`),
  KEY `USER_ID` (`USER_ID`),
  FOREIGN KEY (`USER_ID`) REFERENCES `user` (`USER_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


CREATE TABLE `commission` (
  `COMMISSION_ID` INT(50) NOT NULL AUTO_INCREMENT,
  `USER_ID` INT(50) NOT NULL,
  `COMMISSION_PRICE` BIGINT(50) NOT NULL,
  PRIMARY KEY (`COMMISSION_ID`),
  FOREIGN KEY (`USER_ID`) REFERENCES `user` (`USER_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


CREATE TABLE `otp` (
   `OTP_ID` INT(50) NOT NULL AUTO_INCREMENT,
   `OTP` VARCHAR(126) DEFAULT NULL,
   `SENT_TIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   `MOBILE_NUMBER` varchar(10) DEFAULT NULL,
   PRIMARY KEY(`OTP_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


CREATE TABLE `user_owner_scheme_rel` ( 
	`USER_ID` INT(50) NOT NULL , 
	`OWNER_ID` INT(50) NOT NULL, 
	`SCHEME` VARCHAR(126) DEFAULT NULL, 
	`FOLIO_NO` VARCHAR(126) DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=utf8;






/** Modified database **/

CREATE TABLE `user` (
  `USER_ID` INT(50) NOT NULL AUTO_INCREMENT,
  `USERNAME` VARCHAR(126) NOT NULL UNIQUE,
  `PASSWORD` VARCHAR(126) NOT NULL,
  `MOBILE_NUMBER` INT(15) DEFAULT NULL,
  `REGISTER_TYPE` VARCHAR(126) NOT NULL,
  `EMAIL` VARCHAR(126) DEFAULT NULL,
  `STATUS` VARCHAR(10) DEFAULT NULL,
  `ROLE_ID` INT(50) DEFAULT NULL,
  `REGISTER_DATE` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `FIELD1` INT(50) DEFAULT NULL,
  `FIELD2` VARCHAR(128) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`),
  FOREIGN KEY (`ROLE_ID`) REFERENCES `role` (`ROLE_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


CREATE TABLE `authority` (
  `AUTHORITY_ID` INT(50) NOT NULL AUTO_INCREMENT,
  `AUTHORITY_TYPE` VARCHAR(126) DEFAULT NULL,
  PRIMARY KEY (`AUTHORITY_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


CREATE TABLE `user_authority` (
  `USER_ID` INT(50) NOT NULL,
  `AUTHORITY_ID` INT(50) NOT NULL,
  PRIMARY KEY (`USER_ID`,`AUTHORITY_ID`),
  FOREIGN KEY (`USER_ID`) REFERENCES `user` (`USER_ID`),
  FOREIGN KEY (`AUTHORITY_ID`) REFERENCES `authority` (`AUTHORITY_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


CREATE TABLE `user_profile` (
  `USER_PROFILE_ID` INT(50) NOT NULL,
  `NAME` VARCHAR(128) DEFAULT NULL,
  `GENDER` VARCHAR(128) DEFAULT NULL,
  `DOB` VARCHAR(128) DEFAULT NULL,
  `AADHAAR_NUMBER` VARCHAR(128) DEFAULT NULL,
  `PAN_NUMBER` VARCHAR(128) DEFAULT NULL,
  `ARN_CODE` VARCHAR(128) DEFAULT NULL,
  `UPDATED_DATE_TIME` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `FIELD1` INT(50) DEFAULT NULL,
  `FIELD2` VARCHAR(128) DEFAULT NULL,
  PRIMARY KEY (`USER_PROFILE_ID`),
  FOREIGN KEY (`USER_PROFILE_ID`) REFERENCES `user` (`USER_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


CREATE TABLE `role` (
  `ROLE_ID` INT(50) NOT NULL AUTO_INCREMENT,
  `ROLE_NAME` VARCHAR(126) DEFAULT NULL,
  PRIMARY KEY (`ROLE_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


CREATE TABLE `address` (
  `ADDRESS_ID` INT(50) NOT NULL AUTO_INCREMENT,
  `USER_ID` INT(50) NOT NULL,
  `COUNTRY` VARCHAR(126) DEFAULT NULL,
  `STATE` VARCHAR(126) DEFAULT NULL,
  `CITY` VARCHAR(126) DEFAULT NULL,
  `PINCODE` VARCHAR(126) DEFAULT NULL,
  `UPDATE_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `FIELD1` INT(50) DEFAULT NULL,
  `FIELD2` VARCHAR(128) DEFAULT NULL,
  PRIMARY KEY (`ADDRESS_ID`),
  KEY `USER_ID` (`USER_ID`),
  FOREIGN KEY (`USER_ID`) REFERENCES `user` (`USER_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


CREATE TABLE `commission` (
  `COMMISSION_ID` INT(50) NOT NULL AUTO_INCREMENT,
  `USER_ID` INT(50) NOT NULL,
  `COMMISSION_PRICE` BIGINT(50) NOT NULL,
  PRIMARY KEY (`COMMISSION_ID`),
  FOREIGN KEY (`USER_ID`) REFERENCES `user` (`USER_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


CREATE TABLE `otp` (
   `OTP_ID` INT(50) NOT NULL AUTO_INCREMENT,`go4wealthdb`
   `OTP` VARCHAR(126) DEFAULT NULL,
   `SENT_TIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   `USERNAME` VARCHAR(10) DEFAULT NULL,
   PRIMARY KEY(`OTP_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


CREATE TABLE `user_owner_rel` ( 
	`USER_OWNER_REL_ID` INT(50) NOT NULL AUTO_INCREMENT, 
	`OWNER_ID` INT(50) NOT NULL, 
	`USER_ID` INT(50) NOT NULL,
	PRIMARY KEY(`USER_OWNER_REL_ID`),
	FOREIGN KEY(`USER_ID`) REFERENCES `user` (`USER_ID`),
	FOREIGN KEY(`OWNER_ID`) REFERENCES `user` (`USER_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;