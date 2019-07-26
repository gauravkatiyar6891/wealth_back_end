INSERT INTO `go4wealthdb`.`cities` (`CITY_NAME`, `CITY_CODE`) VALUES ( 'NOIDA','A+'),( 'NEW DELHI','A'),( 'MUMBAI','B+'),( 'CHENNAI','B');
INSERT INTO `go4wealthdb`.`income_slabs` (`INCOME_SLAB_NAME`,`INCOME_FROM`,`INCOME_TO`,`INCOME_SLAB_CODE`) VALUES ('Upto 3 LAKH',0,300000,'A'),('Between 3-5 Lakh',300000,500000,'B'),('Between 5-10 Lakh',500000,1000000,'C'),('Between 10-20 Lakh',1000000,2000000,'D'),('Between 20-50 Lakh',2000000,5000000,'E'),('>50 Lakh',5000000,10000001,'F');
INSERT INTO `go4wealthdb`.`age_slabs` (`AGE_SLAB_NAME`, `AGE_FROM`, `AGE_TO`,`AGE_SLAB_CODE`) VALUES ('20-25 Years',20,25,'A'),('25-30 Years',25,30,'B'),('30-35 Years',30,35,'C'),('35-40 Years',35,40,'D'),('40-45 Years',40,45,'E'),('45-50 Years',45,50,'F'),('50-55 Years',50,55,'G'),('>55 Years',55,-1,'H');
INSERT INTO `go4wealthdb`.`marital_status` (`MARITAL_STATUS_NAME`,`MARITAL_STATUS_VALUE`,`MARITAL_STATUS_CODE`) VALUES ('Married', 'MA','A'),('Not Married','NM','B'),('Other','OT','C');
INSERT INTO `go4wealthdb`.`kids_slabs` (`KIDS_SLAB_NAME`,`KIDS_SLAB_VALUE`,`KIDS_SLAB_CODE`)VALUES('1',1,'A'),('2',2,'B'),('>2',-1,'C'),('0',0,'D');

INSERT INTO `go4wealthdb`.`goal_bucket`(`GOAL_BUCKET_NAME`,`GOAL_BUCKET_CODE`) VALUES ('SHORT TERM','ST'),('MEDIUM TERM','MT'),('LONG TERM','LT');

INSERT INTO `go4wealthdb`.`goals` (`GOAL_NAME`,`GOAL_BUCKET_ID`,`SHOW_TO_PROFILE_TYPE`,`COST_OF_GOAL`) VALUES 
			('Car Purchase',1,NULL,1000000),
			('Fund in Need (Emergency Fund)',1,NULL,500000),
			('Pay Off Debt',1,NULL,0),
			('Self Marriage',1,NULL,1000000),
			('Vacation Planning-Domestic',1,NULL,250000),
			('Vacation Planning-International',1,NULL,500000),
			('Fund for Fun - TV/Appliance/Décor',1,NULL,250000),
			('Fund to Save Tax',1,NULL,100000),
			('Dream Home',2,NULL,5000000),
			('My 1st Crore',2,NULL,10000000),
			('Philanthrophy & Charity',2,NULL,250000),
			('Starting my own Business',2,NULL,2000000),
			('Child Higher Education-Abroad',3,NULL,4000000),
			('Child Higher Education-India',3,NULL,1500000),
			('Children Dream Marriage',3,NULL,2500000),
			('My Second Home',3,NULL,10000000),
			('Retirement Planning',3,NULL,5000000);