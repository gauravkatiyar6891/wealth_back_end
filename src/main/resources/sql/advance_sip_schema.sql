CREATE TABLE `INFLATION` (
  `INFLATION_ID` INT(50) NOT NULL AUTO_INCREMENT,
  `INFLATION_TYPE` VARCHAR(126) NOT NULL,
  `LOCATION_OF_SPEND` VARCHAR(126) NOT NULL,
  `INFLATION` DECIMAL(10,5) DEFAULT NULL,
  PRIMARY KEY (`INFLATION_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `RISK_BEARING_CAPACITY` (
  `RISK_BEARING_CAPACITY_ID` INT(50) NOT NULL AUTO_INCREMENT,
  `RETURN` VARCHAR(126) NOT NULL,
  `RISK` VARCHAR(126) NOT NULL,
  `FUND_TYPE` VARCHAR(126) NOT NULL,
  `NOMINAL_RETURN` DECIMAL(10,5) DEFAULT NULL,
  `INFL_ADJUSTED_RETURN` DECIMAL(10,5) DEFAULT NULL,
  PRIMARY KEY (`RISK_BEARING_CAPACITY_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


CREATE TABLE `ASSET_CLASS` (
  `ASSET_CLASS_ID` INT(50) NOT NULL AUTO_INCREMENT,
  `ASSET_CLASS` VARCHAR(126) NOT NULL,
  `ROI_EXPTD` DECIMAL(10,5) DEFAULT NULL,
  `INFL_ADJUSTED_RETURN` DECIMAL(10,5) DEFAULT NULL,
  PRIMARY KEY (`ASSET_CLASS_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


CREATE TABLE `ASSET_CLASS_INTERNAL` (
  `ASSET_CLASS_INTERNAL_ID` INT(50) NOT NULL AUTO_INCREMENT,
  `FUND_TYPE` VARCHAR(126) NOT NULL,
  `FUND_TYPE_CODE` VARCHAR(10) NOT NULL,
  `ROI_EXPTD` DECIMAL(10,5) DEFAULT NULL,
  `INFL_ADJUSTED_RETURN` DECIMAL(10,5) DEFAULT NULL,
  PRIMARY KEY (`ASSET_CLASS_INTERNAL_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `RISK_TYPE` (
  `RISK_TYPE_ID` INT(50) NOT NULL AUTO_INCREMENT,
  `RISK_TYPE` VARCHAR(126) NOT NULL,
  PRIMARY KEY (`RISK_TYPE_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `RETURNS_TYPE` (
  `RETURNS_TYPE_ID` INT(50) NOT NULL AUTO_INCREMENT,
  `RETURNS_TYPE` VARCHAR(126) NOT NULL,
  PRIMARY KEY (`RETURNS_TYPE_ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


--new tables to implement new changes in sip goals
  
  
 CREATE TABLE `GOALS_ORDER` (
  `GOALS_ORDER_ID` INT(50) NOT NULL AUTO_INCREMENT,
  `USER_ID` INT(50) NOT NULL,
  `DESCRIPTION` VARCHAR(128) DEFAULT NULL,
  `RISK_PROFILE` VARCHAR(126) DEFAULT NULL,
  `FUND_TYPE` VARCHAR(126) DEFAULT NULL,
  `INFLATION` DECIMAL(10,5) NOT NULL,
  `ROI` DECIMAL(10,5) NOT NULL,
  `TOTAL_MONTHLY_SAVING` DECIMAL(20,5) NOT NULL,
  `TOTAL_LUMPSUM_SAVING` DECIMAL(20,5) NOT NULL,
  `TOTAL_MONTHLY_ADJUSTMENT` DECIMAL(20,5) NOT NULL,
  `TOTAL_LUMPSUM_ADJUSMENT` DECIMAL(20,5) NOT NULL,
  `TOTAL_FUTURE_VALUE` DECIMAL(20,5) NOT NULL,
  `TOTAL_ASSET_VALUE` DECIMAL(20,5) NOT NULL,
  `CREATED_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00',
  `UPDATED_DATE_TIME` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `FIELD1` INT(50) DEFAULT NULL,
  `FIELD2` VARCHAR(128) DEFAULT NULL,
  `FIELD4` VARCHAR(128) DEFAULT NULL,
   PRIMARY KEY (`GOALS_ORDER_ID`),
   FOREIGN KEY (`USER_ID`) REFERENCES `user` (`USER_ID`)
   ) ENGINE=INNODB DEFAULT CHARSET=utf8;
   
 
 CREATE TABLE GOAL_ORDER_ITEMS(
  `GOAL_ORDER_ITEM_ID` INT(50) NOT NULL AUTO_INCREMENT,
  `GOAL_ID` INT(50) NOT NULL ,
  `GOALS_ORDER_ID` INT(50) NOT NULL,
  `USER_ID` INT(50) NOT NULL,
  `DESCRIPTION` VARCHAR(254) DEFAULT NULL,
  `FUTURE_VALUE` DECIMAL(20,5) NOT NULL,
  `DURATION` INT(50) DEFAULT NULL ,
  `GOAL_DATE` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00',
  `MONTHLY_SAVING` DECIMAL(20,5) NOT NULL,
  `LUMPSUM_SAVING` DECIMAL(20,5) NOT NULL,
  `CREATED_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00',
  `UPDATED_DATE_TIME` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `FIELD1` INT(50) DEFAULT NULL,
  `FIELD2` VARCHAR(128) DEFAULT NULL,
  `FIELD4` VARCHAR(128) DEFAULT NULL,
  PRIMARY KEY(`GOAL_ORDER_ITEM_ID`),
  FOREIGN KEY (`GOALS_ORDER_ID`) REFERENCES GOALS_ORDER(`GOALS_ORDER_ID`),
  FOREIGN KEY (`GOAL_ID`) REFERENCES GOALS(`GOAL_ID`),
  FOREIGN KEY (`USER_ID`) REFERENCES `user` (`USER_ID`)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8;
 

 CREATE TABLE `user_asset_items` (
  `user_asset_item_id` int(50) NOT NULL AUTO_INCREMENT,
  `GOALS_ORDER_ID` int(50) NOT NULL,
  `ASSET_CLASS_ID` int(50) NOT NULL,
  `ASSET_VALUE` decimal(20,5) NOT NULL,
  `future_value` decimal(20,5) NOT NULL,
  PRIMARY KEY (`user_asset_item_id`),
  KEY `ASSET_CLASS_ID` (`ASSET_CLASS_ID`),
  KEY `GOALS_ORDER_ID` (`GOALS_ORDER_ID`),
  CONSTRAINT `user_asset_items_ibfk_1` FOREIGN KEY (`GOALS_ORDER_ID`) REFERENCES `goals_order` (`GOALS_ORDER_ID`),
  CONSTRAINT `user_asset_items_ibfk_2` FOREIGN KEY (`ASSET_CLASS_ID`) REFERENCES `asset_class` (`ASSET_CLASS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
