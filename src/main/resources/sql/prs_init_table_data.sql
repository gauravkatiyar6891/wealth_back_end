INSERT INTO `go4wealthdb`.`fund_houses` (`FUND_HOUSE_NAME`,`FUND_NAME`,`FUND_SPONSOR`,`FUND_RELEASE_DATE`,`AMC_ADDRESS`,`WEBSITE`)
	VALUES
	('Aditya Birla Capital Limited (ABCL)', 
	'Birla Sun Life Mutual Fund', 
	'FUND_SPONSOR', 
	 NOW(), 
	'AMC_ADDRESS', 
	'https://adityabirlacapital.com'
	);
	
INSERT INTO `go4wealthdb`.`scheme_category`(`CATEGORY`) VALUES ('DEBT');
INSERT INTO `go4wealthdb`.`scheme_category`(`CATEGORY`)VALUES('EQUITY');
INSERT INTO `go4wealthdb`.`scheme_category`(`CATEGORY`)VALUES('ELSS');
INSERT INTO `go4wealthdb`.`scheme_category`(`CATEGORY`)VALUES('GILT');
INSERT INTO `go4wealthdb`.`scheme_category`(`CATEGORY`)VALUES('FOF');

INSERT INTO `go4wealthdb`.`scheme_sub_category` ( `SUB_CATEGORY`) VALUES ('Income');

