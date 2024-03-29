CREATE TABLE `seo` (
  `SEO_ID` int(50) NOT NULL AUTO_INCREMENT,
  `PAGE_NAME` varchar(50) NOT NULL,
  `SITE_URL` varchar(60) DEFAULT NULL,
  `META_DESCRIPTION` varchar(190) DEFAULT NULL,
  `META_KEYWORDS` varchar(190) DEFAULT NULL,
  `TITLE` varchar(190) DEFAULT NULL,
  PRIMARY KEY (`SEO_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8



--CREATE TABLE `BLOG`(
--  `BLOG_ID` INT(50) NOT NULL AUTO_INCREMENT,
--  `TITLE` VARCHAR(126) DEFAULT NULL,
--  `URL` VARCHAR(126) DEFAULT NULL,
--  `DESCRIPTION` VARCHAR(5000) DEFAULT NULL,
--  `POST_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
--   PRIMARY KEY (`BLOG_ID`)
--  )ENGINE=INNODB DEFAULT CHARSET=utf8;

DROP TABLE blog;

CREATE TABLE `BLOG`(
  `BLOG_ID` INT(50) NOT NULL AUTO_INCREMENT,
  `BLOG_CATEGORY_ID` INT(126) NOT NULL,
  `TITLE` VARCHAR(126) DEFAULT NULL,
  `URL` VARCHAR(126) DEFAULT NULL,
  `MEDIA_TYPE` VARCHAR(126) DEFAULT NULL,
  `FINAL_WORDS` VARCHAR(126) DEFAULT NULL,
  `POSTED_BY` VARCHAR(126) DEFAULT NULL,
  `KEY_WORDS` VARCHAR(126) DEFAULT NULL,
  `CATEGORY_NAME` VARCHAR(126) DEFAULT NULL,
  `SHORT_DESCRIPTION` VARCHAR(5000) DEFAULT NULL,
  `LONG_DESCRIPTION` VARCHAR(8000) DEFAULT NULL,
  `POST_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`BLOG_CATEGORY_ID`) REFERENCES `BLOG_CATEGORY` (`BLOG_CATEGORY_ID`),
  PRIMARY KEY (`BLOG_ID`)
  )ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `BLOG_CATEGORY`(
  `BLOG_CATEGORY_ID` INT(50) NOT NULL AUTO_INCREMENT,
  `BLOG_CATEGORY_NAME` VARCHAR(126) DEFAULT NULL,
   PRIMARY KEY (`BLOG_CATEGORY_ID`)
  )ENGINE=INNODB DEFAULT CHARSET=utf8;
 
 
INSERT INTO 
`go4wealthdb`.`user` (`USER_ID`, `username`, `PASSWORD`, `MOBILE_NUMBER`, `REGISTER_TYPE`, `EMAIL`, `FIRST_NAME`, `LAST_NAME`, `GENDER`, `DATE_OF_BIRTH`, `CREATED_TIMESTAMP`, `UPDATED_DATE_TIME`, `FIELD1`, `FIELD2`, `FIELD3`, `FIELD4`, `STATUS`, `EMAIL_VERIFIED`)
	VALUES(
		19, 'admin@go4wealth.com', '$2a$12$s62I8z3KFv7Qbz/mTRu3nuwPVnwkGS3B6hzHWZaGwmLwoKTVNvkZO', '', '', '', '', '', '', NULL, NULL, NULL, 0, '', '', '', '2', '1'
	);
	

INSERT INTO `go4wealthdb`.`user_role` (`ROLE_ID`, `USER_ID`) VALUES(1,19);



DROP TABLE IF EXISTS `indian_ifsc_codes`;

CREATE TABLE `indian_ifsc_codes` (
  `IFSC_CODE_ID` int(100) NOT NULL AUTO_INCREMENT,
  `BANK` varchar(150) DEFAULT NULL,
  `IFSC_CODE` varchar(100) DEFAULT NULL,
  `MICR_CODE` varchar(100) DEFAULT NULL,
  `BRANCH` varchar(150) DEFAULT NULL,
  `ADDRESS` varchar(250) DEFAULT NULL,
  `CONTACT` varchar(50) DEFAULT NULL,
  `CITY` varchar(150) DEFAULT NULL,
  `DISTRICT` varchar(200) DEFAULT NULL,
  `STATE` varchar(200) DEFAULT NULL,
  `FIELD_1` varchar(100) DEFAULT NULL,
  `FIELD_2` varchar(100) DEFAULT NULL,
  `FIELD_3` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`IFSC_CODE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=82574 DEFAULT CHARSET=utf8;



CREATE TABLE `top_schemes` (
  `TOP_SCHEME_ID` int(11) NOT NULL AUTO_INCREMENT,
  `SCHEME_ID` int(11) NOT NULL,
  `STATUS` varchar(50) NOT NULL,
  `FIELD2` int(11) DEFAULT NULL,
  `FIELD3` int(11) DEFAULT NULL,
  PRIMARY KEY (`TOP_SCHEME_ID`,`SCHEME_ID`)
) 

ALTER TABLE top_schemes CHANGE FIELD2 CREATED_DATE Date NULL
ALTER TABLE top_schemes CHANGE FIELD3 DEACTIVATED_DATE Date NULL