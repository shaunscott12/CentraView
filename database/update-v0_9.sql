-- uifieldmapping table for Global Replace
DROP TABLE IF EXISTS `uifieldmapping`;
CREATE TABLE `uifieldmapping` (
  `ID` int(11) NOT NULL auto_increment,
  `uifieldname` varchar(50) NOT NULL default '',
  `moduleid` int(11) NOT NULL default '0',
  `fieldid` int(11) NOT NULL default '0',
  PRIMARY KEY (`ID`)
) TYPE=InnoDB;

INSERT INTO uifieldmapping (ID, uifieldname, moduleid, fieldid) VALUES (1, "Entity Name", 14, 3);
INSERT INTO uifieldmapping (ID, uifieldname, moduleid, fieldid) VALUES (2, "Street1", 14, 29);
INSERT INTO uifieldmapping (ID, uifieldname, moduleid, fieldid) VALUES (4, "city", 14, 31);
INSERT INTO uifieldmapping (ID, uifieldname, moduleid, fieldid) VALUES (3, "Street2", 14, 30);
INSERT INTO uifieldmapping (ID, uifieldname, moduleid, fieldid) VALUES (5, "Title", 14, 17);
INSERT INTO uifieldmapping (ID, uifieldname, moduleid, fieldid) VALUES (6, "First Name", 14, 14);
INSERT INTO uifieldmapping (ID, uifieldname, moduleid, fieldid) VALUES (7, "Last Name", 14, 16);
INSERT INTO uifieldmapping (ID, uifieldname, moduleid, fieldid) VALUES (8, "Acct Mgr", 14, 649);
INSERT INTO uifieldmapping (ID, uifieldname, moduleid, fieldid) VALUES (9, "Acct Team", 14, 62);
INSERT INTO uifieldmapping (ID, uifieldname, moduleid, fieldid) VALUES (16, "Title", 15, 17);
INSERT INTO uifieldmapping (ID, uifieldname, moduleid, fieldid) VALUES (10, "MI", 14, 15);
INSERT INTO uifieldmapping (ID, uifieldname, moduleid, fieldid) VALUES (13, "First Name", 15, 14);
INSERT INTO uifieldmapping (ID, uifieldname, moduleid, fieldid) VALUES (15, "Entity", 15, 3);
INSERT INTO uifieldmapping (ID, uifieldname, moduleid, fieldid) VALUES (14, "Last Name", 15, 16);
INSERT INTO uifieldmapping (ID, uifieldname, moduleid, fieldid) VALUES (17, "Street", 15, 29);
INSERT INTO uifieldmapping (ID, uifieldname, moduleid, fieldid) VALUES (18, "City", 15, 31);
INSERT INTO uifieldmapping (ID, uifieldname, moduleid, fieldid) VALUES (19, "State", 15, 32);
INSERT INTO uifieldmapping (ID, uifieldname, moduleid, fieldid) VALUES (20, "Zip", 15, 33);
INSERT INTO uifieldmapping (ID, uifieldname, moduleid, fieldid) VALUES (21, "Country", 15, 34);


-- systememailsettings table for holding system email information
DROP TABLE IF EXISTS `systememailsettings`;
CREATE TABLE `systememailsettings` (
 `systemEmailID` int(11) NOT NULL auto_increment,
 `toAddress` varchar(255) NOT NULL,
 `fromAddress` varchar(255) NOT NULL,
 `subject` varchar(255) NOT NULL,
 `smtpServer` varchar(255) NOT NULL,
 `description` varchar(255),
 PRIMARY KEY (`systemEmailID`)
) TYPE=InnoDB;

INSERT INTO `systememailsettings` (`systemEmailID`, `toAddress`, `fromAddress`, `subject`, `smtpServer`, `description`) VALUES (1, ' ', ' ', 'CentraView Web Request: Customer Profile Update', ' ', 'Email which is sent to a CentraView employee user when a Customer requests that their Customer Profile data gets changed.');
INSERT INTO `systememailsettings` (`systemEmailID`, `toAddress`, `fromAddress`, `subject`, `smtpServer`, `description`) VALUES (2, ' ', ' ', 'CentraView Web Request: Forgot Password', ' ', 'Email which is sent to a CentraView Customer user when the Customer make a \'Forgot Password\' request.');

