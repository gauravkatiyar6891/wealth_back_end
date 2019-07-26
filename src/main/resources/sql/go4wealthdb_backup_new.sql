/*
SQLyog Community v11.51 (32 bit)
MySQL - 5.7.17-log : Database - go4wealthdb
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`go4wealthdb` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `go4wealthdb`;

/*Table structure for table `address` */

DROP TABLE IF EXISTS `address`;

CREATE TABLE `address` (
  `ADDRESS_ID` int(50) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(50) NOT NULL,
  `COUNTRY` varchar(126) DEFAULT NULL,
  `STATE` varchar(126) DEFAULT NULL,
  `CITY` varchar(126) DEFAULT NULL,
  `PINCODE` varchar(126) DEFAULT NULL,
  `REGISTER_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `FIELD1` int(50) DEFAULT NULL,
  `FIELD2` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`ADDRESS_ID`),
  KEY `USER_ID` (`USER_ID`),
  CONSTRAINT `address_ibfk_1` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `address` */

/*Table structure for table `agent_profile` */

DROP TABLE IF EXISTS `agent_profile`;

CREATE TABLE `agent_profile` (
  `AGENT_PROFILE_ID` int(50) NOT NULL,
  `ARN_CODE` varchar(128) DEFAULT NULL,
  `AADHAAR_NUMBER` varchar(128) DEFAULT NULL,
  `PAN_NUMBER` varchar(128) DEFAULT NULL,
  `CREATED_TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATED_DATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `FIELD1` int(50) DEFAULT NULL,
  `FIELD2` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`AGENT_PROFILE_ID`),
  CONSTRAINT `agent_profile_ibfk_1` FOREIGN KEY (`AGENT_PROFILE_ID`) REFERENCES `user` (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `agent_profile` */

/*Table structure for table `authority` */

DROP TABLE IF EXISTS `authority`;

CREATE TABLE `authority` (
  `AUTHORITY_ID` int(50) NOT NULL AUTO_INCREMENT,
  `AUTHORITY_TYPE` varchar(126) DEFAULT NULL,
  PRIMARY KEY (`AUTHORITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `authority` */

/*Table structure for table `commission` */

DROP TABLE IF EXISTS `commission`;

CREATE TABLE `commission` (
  `COMMISSION_ID` int(50) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(50) NOT NULL,
  `COMMISSION_PRICE` bigint(50) NOT NULL,
  PRIMARY KEY (`COMMISSION_ID`),
  KEY `USER_ID` (`USER_ID`),
  CONSTRAINT `commission_ibfk_1` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `commission` */

/*Table structure for table `otp` */

DROP TABLE IF EXISTS `otp`;

CREATE TABLE `otp` (
  `OTP_ID` int(50) NOT NULL AUTO_INCREMENT,
  `OTP` varchar(126) DEFAULT NULL,
  `SENT_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `MOBILE_NUMBER` int(10) DEFAULT NULL,
  PRIMARY KEY (`OTP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `otp` */

/*Table structure for table `role` */

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `ROLE_ID` int(50) NOT NULL AUTO_INCREMENT,
  `ROLE_NAME` varchar(126) DEFAULT NULL,
  PRIMARY KEY (`ROLE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `role` */

insert  into `role`(`ROLE_ID`,`ROLE_NAME`) values (1,'ADMIN'),(2,'AGENT'),(3,'MANAGER'),(4,'USER');

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `USER_ID` int(50) NOT NULL AUTO_INCREMENT,
  `username` varchar(126) NOT NULL,
  `PASSWORD` varchar(126) NOT NULL,
  `MOBILE_NUMBER` varchar(50) DEFAULT NULL,
  `REGISTER_TYPE` varchar(126) NOT NULL,
  `OWNER` varchar(126) DEFAULT NULL,
  `FIRST_NAME` varchar(126) DEFAULT NULL,
  `LAST_NAME` varchar(126) DEFAULT NULL,
  `GENDER` varchar(50) DEFAULT NULL,
  `DATE_OF_BIRTH` date DEFAULT NULL,
  `CREATED_TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATED_DATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `FIELD1` int(50) DEFAULT NULL,
  `FIELD2` varchar(128) DEFAULT NULL,
  `FIELD3` varchar(512) DEFAULT NULL,
  `FIELD4` varchar(128) DEFAULT NULL,
  `STATUS` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`USER_ID`,`username`,`PASSWORD`,`MOBILE_NUMBER`,`REGISTER_TYPE`,`OWNER`,`FIRST_NAME`,`LAST_NAME`,`GENDER`,`DATE_OF_BIRTH`,`CREATED_TIMESTAMP`,`UPDATED_DATE_TIME`,`FIELD1`,`FIELD2`,`FIELD3`,`FIELD4`,`STATUS`) values (3,'8859788733','$2a$12$zPSs77I7DaCRV85ESP0ZwODEU1xXOIvcb4/xRiNk9Gj6PvX.fQazq','8859788733','MO',NULL,NULL,NULL,NULL,NULL,'2018-04-25 20:42:17','2018-04-25 20:42:59',NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `user_authority` */

DROP TABLE IF EXISTS `user_authority`;

CREATE TABLE `user_authority` (
  `USER_ID` int(50) NOT NULL,
  `AUTHORITY_ID` int(50) NOT NULL,
  PRIMARY KEY (`USER_ID`,`AUTHORITY_ID`),
  KEY `AUTHORITY_ID` (`AUTHORITY_ID`),
  CONSTRAINT `user_authority_ibfk_1` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`USER_ID`),
  CONSTRAINT `user_authority_ibfk_2` FOREIGN KEY (`AUTHORITY_ID`) REFERENCES `authority` (`AUTHORITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_authority` */

/*Table structure for table `user_profile` */

DROP TABLE IF EXISTS `user_profile`;

CREATE TABLE `user_profile` (
  `USER_PROFILE_ID` int(50) NOT NULL,
  `AADHAAR_NUMBER` varchar(128) DEFAULT NULL,
  `PAN_NUMBER` varchar(128) DEFAULT NULL,
  `CREATED_TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATED_DATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `FIELD1` int(50) DEFAULT NULL,
  `FIELD2` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`USER_PROFILE_ID`),
  CONSTRAINT `user_profile_ibfk_1` FOREIGN KEY (`USER_PROFILE_ID`) REFERENCES `user` (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_profile` */

/*Table structure for table `user_role` */

DROP TABLE IF EXISTS `user_role`;

CREATE TABLE `user_role` (
  `ROLE_ID` int(50) NOT NULL,
  `USER_ID` int(50) NOT NULL,
  PRIMARY KEY (`ROLE_ID`,`USER_ID`),
  UNIQUE KEY `USER_ID` (`USER_ID`),
  CONSTRAINT `user_role_ibfk_1` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`USER_ID`),
  CONSTRAINT `user_role_ibfk_2` FOREIGN KEY (`ROLE_ID`) REFERENCES `role` (`ROLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_role` */

insert  into `user_role`(`ROLE_ID`,`USER_ID`) values (4,3);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
