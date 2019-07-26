INSERT INTO `go4wealthdb`.`inflation` (`INFLATION_TYPE`,`LOCATION_OF_SPEND`,`INFLATION`) VALUES ('Education','India',10.0),('Education','Europe',2.5),('Education','USA/Canada',2.5),('Education','Australia',4.0),('Education','Others',7.0),('General','India',5.0),('General','Others',2.0);

INSERT INTO `go4wealthdb`.`risk_bearing_capacity` (`RETURN`,`RISK`,`FUND_TYPE`,`NOMINAL_RETURN`,`INFL_ADJUSTED_RETURN`) VALUES ('HIGH','HIGH','M+S',NULL,NULL),('HIGH','MODERATELY HIGH','L+M+S',NULL,NULL),('HIGH','MODERATE','L+M',NULL,NULL),('MODERATE','MODERATE','L+B',NULL,NULL),('LOW','LOW','L+D/Mny',NULL,NULL);

INSERT INTO `go4wealthdb`.`asset_class` (`ASSET_CLASS`,`ROI_EXPTD`,`INFL_ADJUSTED_RETURN`) VALUES ('Gold',5,NULL),('Property',9,NULL),('FD / Cash / Cash Equivalent',6,NULL),('PPF / EPF / Oth Govt Papers',8,NULL),('Equity - Stocks',15,NULL),('Mutual Fund',13,NULL);

INSERT INTO `go4wealthdb`.`asset_class_internal` (`FUND_TYPE`,`FUND_TYPE_CODE`,`ROI_EXPTD`,`INFL_ADJUSTED_RETURN`) VALUES('Large Cap MF','L',12,NULL),('Mid Cap MF','M',14,NULL),('Small Cap MF','S',16,NULL),('Balanced Fund MF','B',10,NULL),('Debt MF','D',6,NULL),('Money Mkt MF','Mny',5,NULL);

INSERT INTO `go4wealthdb`.`risk_type` (`RISK_TYPE`) VALUES ('HIGH'),('MODERATELY HIGH'),('MODERATE'),('LOW');

INSERT INTO `go4wealthdb`.`returns_type` (`RETURNS_TYPE`) VALUES ('HIGH'),('MODERATE'),('LOW');