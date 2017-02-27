SET FOREIGN_KEY_CHECKS = 0;
SET UNIQUE_CHECKS=0;
SET AUTOCOMMIT=0;
-- MySQL dump 9.10
--
-- Host: localhost    Database: 100
-- ------------------------------------------------------
-- Server version	4.0.18-standard-log

--
-- Table structure for table `accountingstatus`
--

DROP TABLE IF EXISTS `accountingstatus`;
CREATE TABLE `accountingstatus` (
  `statusid` int(10) unsigned NOT NULL auto_increment,
  `title` varchar(25) NOT NULL default '',
  `description` text NOT NULL,
  PRIMARY KEY  (`statusid`)
) TYPE=InnoDB;

--
-- Dumping data for table `accountingstatus`
--

INSERT INTO `accountingstatus` (`statusid`, `title`, `description`) VALUES (1,'Pending','We are in process of deciding whether to accept or reject this order'),(2,'Accepted','We are going to supply material according to this order'),(3,'Rejected','We are not going to fulfill this order');

--
-- Table structure for table `accountingterms`
--

DROP TABLE IF EXISTS `accountingterms`;
CREATE TABLE `accountingterms` (
  `termsid` int(11) unsigned NOT NULL auto_increment,
  `title` varchar(25) default NULL,
  `description` text,
  PRIMARY KEY  (`termsid`)
) TYPE=InnoDB;

--
-- Dumping data for table `accountingterms`
--

INSERT INTO `accountingterms` (`termsid`, `title`, `description`) VALUES (1,'AccountTerms1',NULL),(2,'AccountTerms2',NULL),(3,'AccountTerms3',NULL),(4,'AccountTerms4',NULL);

--
-- Table structure for table `action`
--

DROP TABLE IF EXISTS `action`;
CREATE TABLE `action` (
  `ActionID` int(11) unsigned NOT NULL auto_increment,
  `Type` enum('ALERT','EMAIL') NOT NULL default 'ALERT',
  `Message` text,
  `ActionTime` datetime default NULL,
  `Repeat` tinyint(4) default NULL,
  `ActionInterval` int(11) default NULL,
  PRIMARY KEY  (`ActionID`),
  UNIQUE KEY `ActionID` (`ActionID`)
) TYPE=InnoDB;

--
-- Dumping data for table `action`
--


--
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
  `AllDay` enum('YES','NO') default 'NO',
  `ActivityID` int(11) unsigned NOT NULL auto_increment,
  `Type` int(11) NOT NULL default '0',
  `Priority` int(11) NOT NULL default '0',
  `Status` int(11) NOT NULL default '0',
  `Title` varchar(255) default NULL,
  `DueDate` datetime default NULL,
  `CompletedDate` datetime default NULL,
  `Details` text,
  `Creator` int(11) NOT NULL default '0',
  `Owner` int(11) default NULL,
  `ModifiedBy` int(11) default NULL,
  `Modified` timestamp(14) NOT NULL,
  `Created` datetime NOT NULL default '0000-00-00 00:00:00',
  `Start` datetime default NULL,
  `End` datetime default NULL,
  `AttachmentType` enum('NONE','FILE','LINK') NOT NULL default 'NONE',
  `Location` varchar(25) default NULL,
  `visibility` enum('PRIVATE','PUBLIC') NOT NULL default 'PRIVATE',
  `Notes` varchar(255) default NULL,
  PRIMARY KEY  (`ActivityID`),
  UNIQUE KEY `ActivityID` (`ActivityID`),
  KEY `Type` (`Type`),
  KEY `Priority` (`Priority`),
  KEY `Status` (`Status`),
  KEY `Creator` (`Creator`),
  KEY `Owner` (`Owner`),
  KEY `ModifiedBy` (`ModifiedBy`),
  KEY `Location` (`Location`)
) TYPE=InnoDB;

--
-- Dumping data for table `activity`
--


--
-- Table structure for table `activityaction`
--

DROP TABLE IF EXISTS `activityaction`;
CREATE TABLE `activityaction` (
  `ActivityID` int(11) unsigned NOT NULL default '0',
  `ActionID` int(11) unsigned NOT NULL default '0',
  `recipient` int(11) unsigned default NULL,
  KEY `ActivityID` (`ActivityID`),
  KEY `ActionID` (`ActionID`),
  KEY `recipient` (`recipient`)
) TYPE=InnoDB;

--
-- Dumping data for table `activityaction`
--


--
-- Table structure for table `activitylink`
--

DROP TABLE IF EXISTS `activitylink`;
CREATE TABLE `activitylink` (
  `ActivityID` int(11) unsigned NOT NULL default '0',
  `RecordTypeID` int(11) unsigned NOT NULL default '0',
  `RecordID` int(11) unsigned NOT NULL default '0',
  KEY `ActivityID` (`ActivityID`),
  KEY `RecordTypeID` (`RecordTypeID`),
  KEY `RecordID` (`RecordID`)
) TYPE=InnoDB;

--
-- Dumping data for table `activitylink`
--


--
-- Table structure for table `activityportlet`
--

DROP TABLE IF EXISTS `activityportlet`;
CREATE TABLE `activityportlet` (
  `individualid` int(11) unsigned default NULL,
  `activitytype` int(11) unsigned default NULL,
  KEY `individualid` (`individualid`),
  KEY `activitytype` (`activitytype`)
) TYPE=InnoDB;

--
-- Dumping data for table `activityportlet`
--


--
-- Table structure for table `activitypriority`
--

DROP TABLE IF EXISTS `activitypriority`;
CREATE TABLE `activitypriority` (
  `PriorityID` int(11) NOT NULL auto_increment,
  `Name` varchar(25) NOT NULL default '',
  `PriorityOrder` int(11) NOT NULL default '0',
  PRIMARY KEY  (`PriorityID`),
  UNIQUE KEY `PriorityID` (`PriorityID`)
) TYPE=InnoDB;

--
-- Dumping data for table `activitypriority`
--

INSERT INTO `activitypriority` (`PriorityID`, `Name`, `PriorityOrder`) VALUES (1,'High',1),(2,'Medium',2),(3,'Low',3);

--
-- Table structure for table `activityresources`
--

DROP TABLE IF EXISTS `activityresources`;
CREATE TABLE `activityresources` (
  `ActivityResourceID` int(11) unsigned NOT NULL auto_increment,
  `Name` varchar(25) NOT NULL default '',
  `Detail` text,
  PRIMARY KEY  (`ActivityResourceID`),
  UNIQUE KEY `ActivityResourceID` (`ActivityResourceID`)
) TYPE=InnoDB;

--
-- Dumping data for table `activityresources`
--


--
-- Table structure for table `activitystatus`
--

DROP TABLE IF EXISTS `activitystatus`;
CREATE TABLE `activitystatus` (
  `StatusID` int(11) NOT NULL auto_increment,
  `Name` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`StatusID`),
  UNIQUE KEY `StatusID` (`StatusID`),
  UNIQUE KEY `Name` (`Name`)
) TYPE=InnoDB;

--
-- Dumping data for table `activitystatus`
--

INSERT INTO `activitystatus` (`StatusID`, `Name`) VALUES (3,'Assigned'),(2,'Completed'),(1,'Pending');

--
-- Table structure for table `activitytype`
--

DROP TABLE IF EXISTS `activitytype`;
CREATE TABLE `activitytype` (
  `TypeID` int(11) NOT NULL auto_increment,
  `Name` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`TypeID`),
  UNIQUE KEY `TypeID` (`TypeID`)
) TYPE=InnoDB;

--
-- Dumping data for table `activitytype`
--

INSERT INTO `activitytype` (`TypeID`, `Name`) VALUES (1,'Appointment'),(2,'Call'),(3,'Forecast Sale'),(4,'Literature Request'),(5,'Meeting'),(6,'To Do'),(7,'Next Action'),(8,'Task');

--
-- Table structure for table `additionalmenu`
--

DROP TABLE IF EXISTS `additionalmenu`;
CREATE TABLE `additionalmenu` (
  `menuitem_id` int(11) unsigned NOT NULL auto_increment,
  `menuitem_name` varchar(25) NOT NULL default '',
  `moduleid` int(11) unsigned NOT NULL default '0',
  `forward_res` varchar(25) NOT NULL default '',
  `new_win` int(1) unsigned default '0',
  `win_property` varchar(50) default NULL,
  `params` varchar(50) default NULL,
  `menuitem_order` int(2) unsigned NOT NULL default '0',
  PRIMARY KEY  (`menuitem_id`),
  KEY `menuitem_id` (`menuitem_id`)
) TYPE=InnoDB;

--
-- Dumping data for table `additionalmenu`
--

INSERT INTO `additionalmenu` (`menuitem_id`, `menuitem_name`, `moduleid`, `forward_res`, `new_win`, `win_property`, `params`, `menuitem_order`) VALUES (1,'Help',60,'help',1,' width=500,height=500,status=no','',1),(3,'Preferences',62,'preferences',0,'','',3),(4,'Reports',63,'reports',0,'','',4),(5,'Administrator',64,'administrator',0,'','',6);

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
  `AddressID` int(10) unsigned NOT NULL auto_increment,
  `AddressType` int(11) unsigned default NULL,
  `Street1` varchar(225) default NULL,
  `Street2` varchar(255) default NULL,
  `City` varchar(255) default NULL,
  `state` varchar(255) default '',
  `Zip` varchar(25) default NULL,
  `country` varchar(255) default '',
  `Website` varchar(255) default NULL,
  `jurisdictionID` int(11) unsigned default '0',
  PRIMARY KEY  (`AddressID`),
  KEY `AddressType` (`AddressType`)
) TYPE=InnoDB;

--
-- Dumping data for table `address`
--

INSERT INTO `address` (`AddressID`, `AddressType`, `Street1`, `Street2`, `City`, `state`, `Zip`, `country`, `Website`, `jurisdictionID`) VALUES (1,1,'650 Sentry Park Suite One','','Blue Bell','PA','19422','USA','http://www.centraview.com',0);

--
-- Table structure for table `addressrelate`
--

DROP TABLE IF EXISTS `addressrelate`;
CREATE TABLE `addressrelate` (
  `Address` int(11) unsigned NOT NULL default '0',
  `ContactType` int(11) unsigned NOT NULL default '0',
  `Contact` int(11) unsigned NOT NULL default '0',
  `AddressType` int(11) unsigned default NULL,
  `IsPrimary` enum('YES','NO') default NULL,
  PRIMARY KEY  (`Address`,`ContactType`,`Contact`),
  KEY `Address` (`Address`),
  KEY `ContactType` (`ContactType`),
  KEY `Contact` (`Contact`),
  KEY `AddressType` (`AddressType`)
) TYPE=InnoDB;

--
-- Dumping data for table `addressrelate`
--

INSERT INTO `addressrelate` (`Address`, `ContactType`, `Contact`, `AddressType`, `IsPrimary`) VALUES (1,1,2,1,'YES');

--
-- Table structure for table `addresstype`
--

DROP TABLE IF EXISTS `addresstype`;
CREATE TABLE `addresstype` (
  `TypeID` int(11) unsigned NOT NULL auto_increment,
  `Name` varchar(25) default NULL,
  PRIMARY KEY  (`TypeID`)
) TYPE=InnoDB;

--
-- Dumping data for table `addresstype`
--

INSERT INTO `addresstype` (`TypeID`, `Name`) VALUES (1,'Billing'),(2,'Shipping');

--
-- Table structure for table `alert`
--

DROP TABLE IF EXISTS `alert`;
CREATE TABLE `alert` (
  `alertid` int(11) unsigned NOT NULL auto_increment,
  `message` text,
  `owner` int(11) unsigned default NULL,
  `ack` enum('YES','NO') default NULL,
  `displayedtime` datetime default NULL,
  PRIMARY KEY  (`alertid`),
  KEY `owner` (`owner`)
) TYPE=InnoDB;

--
-- Dumping data for table `alert`
--


--
-- Table structure for table `alertpreference`
--

DROP TABLE IF EXISTS `alertpreference`;
CREATE TABLE `alertpreference` (
  `individualid` int(11) unsigned default NULL,
  `acknowledgedvisability` enum('BOTH','HOME','NONE') default NULL,
  `emailvisability` enum('BOTH','HOME','NONE') default NULL,
  `acknowledgeddays` int(11) default NULL,
  KEY `individualid` (`individualid`)
) TYPE=InnoDB;

--
-- Dumping data for table `alertpreference`
--


--
-- Table structure for table `applicationsetting`
--

DROP TABLE IF EXISTS `applicationsetting`;
CREATE TABLE `applicationsetting` (
  `modulesettingid` int(11) unsigned NOT NULL auto_increment,
  `msname` varchar(100) NOT NULL default '',
  `msvalue` varchar(250) NOT NULL default '',
  `starttime` time default NULL,
  `endtime` time default NULL,
  `workingdays` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`modulesettingid`),
  UNIQUE KEY `msname` (`msname`)
) TYPE=InnoDB COMMENT='InnoDB free: 68608 kB; InnoDB free: 75776 kB';

--
-- Dumping data for table `applicationsetting`
--

INSERT INTO `applicationsetting` (`modulesettingid`, `msname`, `msvalue`, `starttime`, `endtime`, `workingdays`) VALUES (1,'CUSTOMERLOGO','logo_customer.gif',NULL,NULL,''),(2,'DEFAULTOWNER','646',NULL,NULL,''),(3,'','','10:00:00','07:00:00','mon,tues,wed,thurs,fri,sat');

--
-- Table structure for table `applypayment`
--

DROP TABLE IF EXISTS `applypayment`;
CREATE TABLE `applypayment` (
  `LineID` int(10) unsigned NOT NULL auto_increment,
  `LineStatus` enum('New','Active','Deleted') NOT NULL default 'Active',
  `PaymentID` int(10) unsigned NOT NULL default '0',
  `InvoiceID` int(10) unsigned NOT NULL default '0',
  `Amount` float NOT NULL default '0',
  PRIMARY KEY  (`LineID`),
  KEY `PaymentID` (`PaymentID`),
  KEY `InvoiceID` (`InvoiceID`)
) TYPE=InnoDB;

--
-- Dumping data for table `applypayment`
--


--
-- Table structure for table `attachment`
--

DROP TABLE IF EXISTS `attachment`;
CREATE TABLE `attachment` (
  `AttachmentID` int(4) unsigned NOT NULL auto_increment,
  `MessageID` int(11) unsigned default NULL,
  `FileName` varchar(100) NOT NULL default '',
  `FileID` int(11) unsigned default NULL,
  PRIMARY KEY  (`AttachmentID`),
  KEY `MessageID` (`MessageID`),
  KEY `FileID` (`FileID`)
) TYPE=InnoDB;

--
-- Dumping data for table `attachment`
--


--
-- Table structure for table `attendee`
--

DROP TABLE IF EXISTS `attendee`;
CREATE TABLE `attendee` (
  `ActivityID` int(11) unsigned NOT NULL default '0',
  `IndividualID` int(11) unsigned NOT NULL default '0',
  `Type` enum('REQUIRED','OPTIONAL') NOT NULL default 'REQUIRED',
  `Status` int(11) unsigned default NULL,
  KEY `ActivityID` (`ActivityID`),
  KEY `IndividualID` (`IndividualID`),
  KEY `Status` (`Status`)
) TYPE=InnoDB;

--
-- Dumping data for table `attendee`
--


--
-- Table structure for table `attendeestatus`
--

DROP TABLE IF EXISTS `attendeestatus`;
CREATE TABLE `attendeestatus` (
  `StatusID` int(11) NOT NULL auto_increment,
  `Name` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`StatusID`),
  UNIQUE KEY `StatusID` (`StatusID`),
  UNIQUE KEY `Name` (`Name`)
) TYPE=InnoDB;

--
-- Dumping data for table `attendeestatus`
--

INSERT INTO `attendeestatus` (`StatusID`, `Name`) VALUES (1,'Accepted'),(2,'Declined'),(3,'Tentatively Accepted');

--
-- Table structure for table `attic`
--

DROP TABLE IF EXISTS `attic`;
CREATE TABLE `attic` (
  `atticid` int(10) unsigned NOT NULL auto_increment,
  `deletedby` int(10) unsigned NOT NULL default '0',
  `deleted` timestamp(14) NOT NULL,
  `dumptype` enum('CV_ATTIC','CV_GARBAGE') NOT NULL default 'CV_ATTIC',
  `recordtitle` varchar(250) default NULL,
  `owner` int(11) unsigned NOT NULL default '0',
  `moduleid` int(10) unsigned NOT NULL default '0',
  `record` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`atticid`),
  KEY `deletedby` (`deletedby`),
  KEY `owner` (`owner`),
  KEY `moduleid` (`moduleid`),
  KEY `record` (`record`)
) TYPE=InnoDB;

--
-- Dumping data for table `attic`
--


--
-- Table structure for table `atticdata`
--

DROP TABLE IF EXISTS `atticdata`;
CREATE TABLE `atticdata` (
  `atticid` int(10) unsigned NOT NULL default '0',
  `fieldid` int(10) unsigned NOT NULL default '0',
  `value` varchar(250) default NULL,
  `recordtypeid` int(10) unsigned NOT NULL default '0',
  KEY `atticid` (`atticid`),
  KEY `fieldid` (`fieldid`),
  KEY `recordtypeid` (`recordtypeid`)
) TYPE=InnoDB;

--
-- Dumping data for table `atticdata`
--


--
-- Table structure for table `authorizationsettings`
--

DROP TABLE IF EXISTS `authorizationsettings`;
CREATE TABLE `authorizationsettings` (
  `userAuthType` varchar(100) NOT NULL default '',
  `server` varchar(100) NOT NULL default '',
  `port` varchar(100) NOT NULL default '',
  `username` varchar(100) NOT NULL default '',
  `password` varchar(100) NOT NULL default '',
  `usernameField` varchar(100) NOT NULL default '',
  `passwordField` varchar(100) NOT NULL default '',
  `authField` varchar(100) NOT NULL default ''
) TYPE=InnoDB;

--
-- Dumping data for table `authorizationsettings`
--


--
-- Table structure for table `calendarportlet`
--

DROP TABLE IF EXISTS `calendarportlet`;
CREATE TABLE `calendarportlet` (
  `individualid` int(11) unsigned default NULL,
  `activitytype` int(11) unsigned default NULL,
  KEY `individualid` (`individualid`),
  KEY `activitytype` (`activitytype`)
) TYPE=InnoDB;

--
-- Dumping data for table `calendarportlet`
--


--
-- Table structure for table `call`
--

DROP TABLE IF EXISTS `call`;
CREATE TABLE `call` (
  `ActivityID` int(11) unsigned NOT NULL default '0',
  `CallType` int(11) unsigned default NULL,
  KEY `ActivityID` (`ActivityID`),
  KEY `CallType` (`CallType`)
) TYPE=InnoDB;

--
-- Dumping data for table `call`
--


--
-- Table structure for table `calltype`
--

DROP TABLE IF EXISTS `calltype`;
CREATE TABLE `calltype` (
  `CallTypeID` int(10) unsigned NOT NULL auto_increment,
  `Name` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`CallTypeID`),
  UNIQUE KEY `CallTypeID` (`CallTypeID`)
) TYPE=InnoDB;

--
-- Dumping data for table `calltype`
--

INSERT INTO `calltype` (`CallTypeID`, `Name`) VALUES (1,'Incoming'),(2,'Outgoing');

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `catid` int(10) unsigned NOT NULL auto_increment,
  `title` varchar(100) NOT NULL default '',
  `description` text,
  `parent` int(11) unsigned NOT NULL default '0',
  `createdby` int(10) unsigned NOT NULL default '0',
  `modifiedby` int(10) unsigned NOT NULL default '0',
  `created` datetime NOT NULL default '0000-00-00 00:00:00',
  `modified` timestamp(14) NOT NULL,
  `owner` int(10) unsigned NOT NULL default '0',
  `status` enum('DRAFT','PUBLISH') NOT NULL default 'DRAFT',
  `publishToCustomerView` enum('YES','NO') NOT NULL default 'NO',
  PRIMARY KEY  (`catid`),
  KEY `parent` (`parent`),
  KEY `createdby` (`createdby`),
  KEY `modifiedby` (`modifiedby`),
  KEY `owner` (`owner`)
) TYPE=InnoDB;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`catid`, `title`, `description`, `parent`, `createdby`, `modifiedby`, `created`, `modified`, `owner`, `status`, `publishToCustomerView`) VALUES (1,'KnowledgeBase','Root Category',0,1,1,'2003-12-14 23:50:16',20040927155044,1,'DRAFT','YES');

--
-- Table structure for table `companynews`
--

DROP TABLE IF EXISTS `companynews`;
CREATE TABLE `companynews` (
  `FileID` int(11) NOT NULL auto_increment,
  `DateFrom` datetime NOT NULL default '0000-00-00 00:00:00',
  `DateTo` datetime NOT NULL default '0000-00-00 00:00:00',
  KEY `FileID` (`FileID`)
) TYPE=InnoDB;

--
-- Dumping data for table `companynews`
--


--
-- Table structure for table `competition`
--

DROP TABLE IF EXISTS `competition`;
CREATE TABLE `competition` (
  `CompetitionID` int(11) NOT NULL default '0',
  `EntityID` int(11) NOT NULL default '0',
  `Strengths` varchar(250) NOT NULL default '',
  `Weaknesses` varchar(250) NOT NULL default '',
  `Notes` text,
  PRIMARY KEY  (`CompetitionID`),
  KEY `EntityID` (`EntityID`)
) TYPE=InnoDB;

--
-- Dumping data for table `competition`
--


--
-- Table structure for table `contacttype`
--

DROP TABLE IF EXISTS `contacttype`;
CREATE TABLE `contacttype` (
  `ContactTypeID` int(11) unsigned NOT NULL auto_increment,
  `Name` varchar(25) default NULL,
  PRIMARY KEY  (`ContactTypeID`)
) TYPE=InnoDB;

--
-- Dumping data for table `contacttype`
--

INSERT INTO `contacttype` (`ContactTypeID`, `Name`) VALUES (1,'Entity'),(2,'Individual');

--
-- Table structure for table `contentstatus`
--

DROP TABLE IF EXISTS `contentstatus`;
CREATE TABLE `contentstatus` (
  `statusid` int(10) unsigned NOT NULL default '0',
  `name` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`statusid`)
) TYPE=InnoDB;

--
-- Dumping data for table `contentstatus`
--


--
-- Table structure for table `country`
--

DROP TABLE IF EXISTS `country`;
CREATE TABLE `country` (
  `CountryID` int(11) unsigned NOT NULL auto_increment,
  `Name` varchar(50) default NULL,
  PRIMARY KEY  (`CountryID`)
) TYPE=InnoDB;

--
-- Dumping data for table `country`
--

INSERT INTO `country` (`CountryID`, `Name`) VALUES (1,'United States'),(2,'Canada'),(3,'United Kingdom'),(4,'Afghanistan'),(5,'Albania'),(6,'Algeria'),(7,'American Samoa'),(8,'Andorra'),(9,'Angola'),(10,'Anguilla'),(11,'Antigua and Barbuda'),(12,'Argentina'),(13,'Armenia'),(14,'Aruba'),(15,'Australia'),(16,'Austria'),(17,'Azerbaijan Republic'),(18,'Bahamas'),(19,'Bahrain'),(20,'Bangladesh'),(21,'Barbados'),(22,'Belarus'),(23,'Belgium'),(24,'Belize'),(25,'Benin'),(26,'Bermuda'),(27,'Bhutan'),(28,'Bolivia'),(29,'Bosnia and Herzegovina'),(30,'Botswana'),(31,'Brazil'),(32,'British Virgin Islands'),(33,'Brunei Darussalam'),(34,'Bulgaria'),(35,'Burkina Faso'),(36,'Burma'),(37,'Burundi'),(38,'Cambodia'),(39,'Cameroon'),(40,'Canada'),(41,'Cape Verde Islands'),(42,'Cayman Islands'),(43,'Central African Republic'),(44,'Chad'),(45,'Chile'),(46,'China'),(47,'Colombia'),(48,'Comoros'),(49,'Congo, Democratic Republic of the'),(50,'Congo, Republic of the'),(51,'Cook Islands'),(52,'Costa Rica'),(53,'Cote d Ivoire (Ivory Coast)'),(54,'Croatia, Republic of'),(55,'Cyprus'),(56,'Czech Republic'),(57,'Denmark'),(58,'Djibouti'),(59,'Dominica'),(60,'Dominican Republic'),(61,'Ecuador'),(62,'Egypt'),(63,'El Salvador'),(64,'Equatorial Guinea'),(65,'Eritrea'),(66,'Estonia'),(67,'Ethiopia'),(68,'Falkland Islands (Islas Malvinas)'),(69,'Fiji'),(70,'Finland'),(71,'France'),(72,'French Guiana'),(73,'French Polynesia'),(74,'Gabon Republic'),(75,'Gambia'),(76,'Georgia'),(77,'Germany'),(78,'Ghana'),(79,'Gibraltar'),(80,'Greece'),(81,'Greenland'),(82,'Grenada'),(83,'Guadeloupe'),(84,'Guam'),(85,'Guatemala'),(86,'Guernsey'),(87,'Guinea'),(88,'Guinea-Bissau'),(89,'Guyana'),(90,'Haiti'),(91,'Honduras'),(92,'Hong Kong'),(93,'Hungary'),(94,'Iceland'),(95,'India'),(96,'Indonesia'),(97,'Ireland'),(98,'Israel'),(99,'Italy'),(100,'Jamaica'),(101,'Jan Mayen'),(102,'Japan'),(103,'Jersey'),(104,'Jordan'),(105,'Kazakhstan'),(106,'Kenya Coast Republic'),(107,'Kiribati'),(108,'Korea, South'),(109,'Kuwait'),(110,'Kyrgyzstan'),(111,'Laos'),(112,'Latvia'),(113,'Lebanon'),(114,'Liechtenstein'),(115,'Lithuania'),(116,'Luxembourg'),(117,'Macau'),(118,'Macedonia'),(119,'Madagascar'),(120,'Malawi'),(121,'Malaysia'),(122,'Maldives'),(123,'Mali'),(124,'Malta'),(125,'Marshall Islands'),(126,'Martinique'),(127,'Mauritania'),(128,'Mauritius'),(129,'Mayotte'),(130,'Mexico'),(131,'Micronesia'),(132,'Moldova'),(133,'Monaco'),(134,'Mongolia'),(135,'Montserrat'),(136,'Morocco'),(137,'Mozambique'),(138,'Namibia'),(139,'Nauru'),(140,'Nepal'),(141,'Netherlands'),(142,'Netherlands Antilles'),(143,'New Caledonia'),(144,'New Zealand'),(145,'Nicaragua'),(146,'Niger'),(147,'Nigeria'),(148,'Niue'),(149,'Norway'),(150,'Oman'),(151,'Pakistan'),(152,'Palau'),(153,'Panama'),(154,'Papua New Guinea'),(155,'Paraguay'),(156,'Peru'),(157,'Philippines'),(158,'Poland'),(159,'Portugal'),(160,'Puerto Rico'),(161,'Qatar'),(162,'Romania'),(163,'Russian Federation'),(164,'Rwanda'),(165,'Saint Helena'),(166,'Saint Kitts-Nevis'),(167,'Saint Lucia'),(168,'Saint Pierre and Miquelon'),(169,'Saint Vincent and the Grenadines'),(170,'San Marino'),(171,'Saudi Arabia'),(172,'Senegal'),(173,'Seychelles'),(174,'Sierra Leone'),(175,'Singapore'),(176,'Slovakia'),(177,'Slovenia'),(178,'Solomon Islands'),(179,'Somalia'),(180,'South Africa'),(181,'Spain'),(182,'Sri Lanka'),(183,'Suriname'),(184,'Svalbard'),(185,'Swaziland'),(186,'Sweden'),(187,'Switzerland'),(188,'Syria'),(189,'Tahiti'),(190,'Taiwan'),(191,'Tajikistan'),(192,'Tanzania'),(193,'Thailand'),(194,'Togo'),(195,'Tonga'),(196,'Trinidad and Tobago'),(197,'Tunisia'),(198,'Turkey'),(199,'Turkmenistan'),(200,'Turks and Caicos Islands'),(201,'Tuvalu'),(202,'Uganda'),(203,'Ukraine'),(204,'United Arab Emirates'),(205,'United Kingdom'),(206,'United States'),(207,'Uruguay'),(208,'Uzbekistan'),(209,'Vanuatu'),(210,'Vatican City State'),(211,'Venezuela'),(212,'Vietnam'),(213,'Virgin Islands (U.S.)'),(214,'Wallis and Futuna'),(215,'Western Sahara'),(216,'Western Samoa'),(217,'Yemen'),(218,'Yugoslavia'),(219,'Zambia'),(220,'Zimbabwe');

--
-- Table structure for table `createfieldauthorisation`
--

DROP TABLE IF EXISTS `createfieldauthorisation`;
CREATE TABLE `createfieldauthorisation` (
  `individualid` int(11) unsigned default NULL,
  `groupid` int(11) unsigned default NULL,
  `fieldid` int(11) unsigned default NULL,
  `privilegelevel` char(1) default NULL,
  `owner` int(11) unsigned default NULL,
  KEY `individualid` (`individualid`),
  KEY `groupid` (`groupid`),
  KEY `fieldid` (`fieldid`),
  KEY `owner` (`owner`)
) TYPE=InnoDB;

--
-- Dumping data for table `createfieldauthorisation`
--


--
-- Table structure for table `createrecordauthorisation`
--

DROP TABLE IF EXISTS `createrecordauthorisation`;
CREATE TABLE `createrecordauthorisation` (
  `individualid` int(11) unsigned default NULL,
  `groupid` int(11) unsigned default NULL,
  `recordid` int(11) unsigned default NULL,
  `privilegelevel` char(1) default NULL,
  `owner` int(11) unsigned default NULL,
  KEY `individualid` (`individualid`),
  KEY `groupid` (`groupid`),
  KEY `recordid` (`recordid`),
  KEY `owner` (`owner`)
) TYPE=InnoDB;

--
-- Dumping data for table `createrecordauthorisation`
--


--
-- Table structure for table `customfield`
--

DROP TABLE IF EXISTS `customfield`;
CREATE TABLE `customfield` (
  `CustomFieldID` int(10) unsigned NOT NULL auto_increment,
  `Name` varchar(25) default NULL,
  `CustomFieldTypeID` tinyint(4) unsigned default NULL,
  `FieldType` enum('SCALAR','MULTIPLE') default NULL,
  `RecordType` int(11) unsigned default NULL,
  UNIQUE KEY `CustomFieldID` (`CustomFieldID`),
  KEY `CustomFieldTypeID` (`CustomFieldTypeID`),
  KEY `RecordType` (`RecordType`)
) TYPE=InnoDB;

--
-- Dumping data for table `customfield`
--

INSERT INTO `customfield` (`CustomFieldID`, `Name`, `CustomFieldTypeID`, `FieldType`, `RecordType`) VALUES (1,'Type',NULL,'MULTIPLE',1),(2,'Industry',NULL,'SCALAR',1),(3,'Status',NULL,'MULTIPLE',1),(4,'Assistant\'s Name',NULL,'SCALAR',2),(5,'Department',NULL,'MULTIPLE',2);

--
-- Table structure for table `customfieldmultiple`
--

DROP TABLE IF EXISTS `customfieldmultiple`;
CREATE TABLE `customfieldmultiple` (
  `CustomFieldID` int(4) unsigned default NULL,
  `ValueID` int(11) unsigned default NULL,
  `RecordID` int(11) unsigned default NULL,
  KEY `CustomFieldID` (`CustomFieldID`),
  KEY `ValueID` (`ValueID`),
  KEY `RecordID` (`RecordID`)
) TYPE=InnoDB;

--
-- Dumping data for table `customfieldmultiple`
--

INSERT INTO `customfieldmultiple` (`CustomFieldID`, `ValueID`, `RecordID`) VALUES (1,0,1),(3,0,1),(5,0,1),(1,0,2),(3,0,2);

--
-- Table structure for table `customfieldscalar`
--

DROP TABLE IF EXISTS `customfieldscalar`;
CREATE TABLE `customfieldscalar` (
  `CustomFieldID` int(11) unsigned default NULL,
  `RecordID` int(11) unsigned default NULL,
  `Value` varchar(25) default NULL,
  KEY `CustomFieldID` (`CustomFieldID`),
  KEY `RecordID` (`RecordID`)
) TYPE=InnoDB;

--
-- Dumping data for table `customfieldscalar`
--

INSERT INTO `customfieldscalar` (`CustomFieldID`, `RecordID`, `Value`) VALUES (2,1,''),(4,1,''),(2,2,'');

--
-- Table structure for table `customfieldvalue`
--

DROP TABLE IF EXISTS `customfieldvalue`;
CREATE TABLE `customfieldvalue` (
  `ValueID` int(10) unsigned NOT NULL auto_increment,
  `CustomFieldID` int(11) unsigned default NULL,
  `Value` varchar(25) default NULL,
  PRIMARY KEY  (`ValueID`),
  UNIQUE KEY `ValueID` (`ValueID`),
  KEY `CustomFieldID` (`CustomFieldID`)
) TYPE=InnoDB;

--
-- Dumping data for table `customfieldvalue`
--

INSERT INTO `customfieldvalue` (`ValueID`, `CustomFieldID`, `Value`) VALUES (1,1,'Customer'),(2,1,'Vendor'),(3,1,'Reseller'),(4,3,'Active'),(5,3,'Inactive'),(6,5,'Information Technology'),(7,5,'Project Management'),(8,5,'Support'),(9,5,'Sales'),(10,5,'Accounting');

--
-- Table structure for table `cvfile`
--

DROP TABLE IF EXISTS `cvfile`;
CREATE TABLE `cvfile` (
  `FileID` int(11) unsigned NOT NULL auto_increment,
  `Title` varchar(250) default NULL,
  `Description` text,
  `Name` varchar(250) default NULL,
  `Owner` int(11) unsigned default NULL,
  `Creator` int(10) unsigned default NULL,
  `UpdatedBy` int(11) unsigned default NULL,
  `Created` datetime default NULL,
  `Updated` timestamp(14) NOT NULL,
  `FileSize` float(10,4) default NULL,
  `Version` varchar(15) default NULL,
  `Status` enum('DRAFT','PUBLISHED') default NULL,
  `visibility` enum('PUBLIC','PRIVATE') default NULL,
  `Author` int(11) unsigned default NULL,
  `CustomerView` enum('YES','NO') NOT NULL default 'NO',
  `RelateEntity` int(11) unsigned default NULL,
  `RelateIndividual` int(11) unsigned default NULL,
  `IsTemporary` enum('YES','NO') NOT NULL default 'NO',
  PRIMARY KEY  (`FileID`),
  KEY `Owner` (`Owner`),
  KEY `Creator` (`Creator`),
  KEY `UpdatedBy` (`UpdatedBy`),
  KEY `Author` (`Author`),
  KEY `RelateEntity` (`RelateEntity`),
  KEY `RelateIndividual` (`RelateIndividual`)
) TYPE=InnoDB;

--
-- Dumping data for table `cvfile`
--


--
-- Table structure for table `cvfilefolder`
--

DROP TABLE IF EXISTS `cvfilefolder`;
CREATE TABLE `cvfilefolder` (
  `fileid` int(11) unsigned default NULL,
  `folderid` int(11) unsigned default NULL,
  `referencetype` enum('PHYSICAL','VIRTUAL') default NULL,
  KEY `fileid` (`fileid`),
  KEY `folderid` (`folderid`)
) TYPE=InnoDB;

--
-- Dumping data for table `cvfilefolder`
--


--
-- Table structure for table `cvfilelink`
--

DROP TABLE IF EXISTS `cvfilelink`;
CREATE TABLE `cvfilelink` (
  `FileID` int(11) unsigned NOT NULL default '0',
  `RecordTypeID` int(11) unsigned NOT NULL default '0',
  `RecordID` int(11) unsigned NOT NULL default '0',
  KEY `FileID` (`FileID`),
  KEY `RecordTypeID` (`RecordTypeID`),
  KEY `RecordID` (`RecordID`)
) TYPE=InnoDB;

--
-- Dumping data for table `cvfilelink`
--


--
-- Table structure for table `cvfolder`
--

DROP TABLE IF EXISTS `cvfolder`;
CREATE TABLE `cvfolder` (
  `FolderID` int(11) unsigned NOT NULL auto_increment,
  `Name` varchar(250) default NULL,
  `Parent` int(11) unsigned default NULL,
  `CreatedBy` int(11) unsigned default NULL,
  `ModifiedBy` int(11) unsigned default NULL,
  `CreatedOn` datetime default NULL,
  `ModifiedOn` timestamp(14) NOT NULL,
  `Description` text,
  `FullPath` tinytext,
  `LocationID` int(11) unsigned NOT NULL default '0',
  `owner` int(11) unsigned default NULL,
  `visibility` enum('PRIVATE','PUBLIC') default 'PRIVATE',
  `IsSystem` enum('TRUE','FALSE') default 'FALSE',
  `leftNav` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`FolderID`),
  KEY `Parent` (`Parent`),
  KEY `CreatedBy` (`CreatedBy`),
  KEY `ModifiedBy` (`ModifiedBy`),
  KEY `LocationID` (`LocationID`),
  KEY `owner` (`owner`)
) TYPE=InnoDB;

--
-- Dumping data for table `cvfolder`
--

INSERT INTO `cvfolder` (`FolderID`, `Name`, `Parent`, `CreatedBy`, `ModifiedBy`, `CreatedOn`, `ModifiedOn`, `Description`, `FullPath`, `LocationID`, `owner`, `visibility`, `IsSystem`, `leftNav`) VALUES (1,'CV_ROOT',0,2,2,'2003-12-14 23:51:25',20031214235125,'',NULL,1,1,'PUBLIC','TRUE',0),(2,'CVFS_ROOT',1,2,2,'2003-12-14 23:51:25',20031214235125,'',NULL,1,1,'PUBLIC','TRUE',0),(3,'CVFS_SYSTEM',1,2,2,'2003-12-14 23:51:25',20031214235125,'',NULL,1,1,'PUBLIC','TRUE',0),(4,'CVFS_USER',2,2,2,'2003-12-14 23:51:26',20031214235126,'',NULL,1,1,'PUBLIC','FALSE',0),(5,'ATTIC',3,2,2,'2003-12-14 23:51:26',20031214235126,'',NULL,1,1,'PUBLIC','TRUE',0),(6,'CV_EMAIL_DEFAULT_FOLDER',2,2,2,'2003-12-14 23:51:26',20031214235126,'',NULL,1,1,'PUBLIC','TRUE',0),(7,'CV_EMPLOYEEHANDBOOK_DEFAULT_FOLDER',2,2,2,'2003-12-14 23:51:26',20031214235126,'',NULL,1,1,'PUBLIC','TRUE',0),(8,'admin',4,2,2,'2003-12-14 23:51:27',20031214235127,'',NULL,1,1,'PUBLIC','FALSE',0),(9,'CV_FILE_DEFAULT_FOLDER',3,2,2,'2003-12-14 23:51:26',20031214235126,'',NULL,1,1,'PUBLIC','TRUE',0),(10,'Public Folders',2,1,1,NULL,20050713001306,NULL,NULL,1,1,'PUBLIC','FALSE',1);

--
-- Table structure for table `cvfolderlocation`
--

DROP TABLE IF EXISTS `cvfolderlocation`;
CREATE TABLE `cvfolderlocation` (
  `LocationID` int(11) unsigned NOT NULL auto_increment,
  `Detail` varchar(250) default NULL,
  PRIMARY KEY  (`LocationID`)
) TYPE=InnoDB;

--
-- Dumping data for table `cvfolderlocation`
--

INSERT INTO `cvfolderlocation` (`LocationID`, `Detail`) VALUES (1,'/var/centraview/fs');

--
-- Table structure for table `cvjoin`
--

DROP TABLE IF EXISTS `cvjoin`;
CREATE TABLE `cvjoin` (
  `tableid1` int(10) unsigned NOT NULL default '0',
  `tableid2` int(10) unsigned NOT NULL default '0',
  `fieldid1` int(10) unsigned NOT NULL default '0',
  `fieldid2` int(10) unsigned NOT NULL default '0',
  `cvjoin` enum('INNER','LEFT','RIGHT') default NULL,
  `othertablename` varchar(100) NOT NULL default '',
  `clause` varchar(150) NOT NULL default '',
  KEY `tableid1` (`tableid1`),
  KEY `tableid2` (`tableid2`),
  KEY `fieldid1` (`fieldid1`),
  KEY `fieldid2` (`fieldid2`)
) TYPE=InnoDB;

--
-- Dumping data for table `cvjoin`
--

INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (1,2,1,13,'LEFT','','AND entity.EntityID = individual.Entity'),(1,2,61,12,'INNER','','AND entity.AccountManagerID = individual.IndividualID'),(1,3,62,3,'INNER','','AND entity.AccountTeamID = GroupTBL.GroupID'),(1,11,1,51,'LEFT','contacttype','AND entity.EntityID = MOCRelate.ContactID AND MOCRelate.ContactType = ContactType.ContactTypeID AND ContactType.Name = \'Entity\''),(1,12,1,55,'LEFT','contacttype','AND entity.EntityID = addressrelate.Contact AND addressrelate.ContactType = contacttype.ContactTypeID AND contacttype.Name = \'Entity\''),(1,14,9,59,'INNER','','AND entity.DBase = dbase.DBaseID'),(2,1,13,1,'RIGHT','','AND entity.EntityID = individual.Entity'),(2,11,12,51,'LEFT','contacttype','AND individual.IndividualID = mocrelate.ContactID AND mocrelate.ContactType = contacttype.ContactTypeID AND contacttype.Name = \'Individual\''),(2,12,12,55,'LEFT','contacttype','AND individual.IndividualID = addressrelate.Contact AND addressrelate.ContactType = contacttype.ContactTypeID AND contacttype.Name = \'Individual\''),(2,13,12,58,'INNER','','AND individual.IndividualID = member.ChildID'),(4,5,32,35,'INNER','','AND address.State = state.StateID'),(4,6,34,37,'INNER','','And address.Country = country.CountryID'),(12,4,12,27,'INNER','','AND addressrelate.Address = address.AddressID'),(13,3,57,24,'INNER','','AND member.GroupID = groupTbl.GroupID'),(15,20,71,106,'LEFT','','AND emailmessage.messageid = attachment.messageid'),(15,18,63,85,'INNER','','AND emailmessage.accountid = emailaccount.accountid'),(15,17,71,82,'INNER','','AND emailmessage.messageid = emailstore.messageid'),(15,19,71,99,'INNER','','AND emailmessage.messageid = emailrecipient.messageid'),(15,2,66,12,'INNER','','AND emailmessage.fromindividual = emailrecipient.individualid'),(15,2,72,19,'LEFT','','AND emailmessage.owner = individual.owner'),(15,2,65,20,'INNER','','AND emailmessage.createdby = individual.createdby'),(15,16,63,79,'INNER','','AND emailmessage.accountid = emailfolder.accountid'),(17,16,83,78,'INNER','','AND emailstore.folderid = emailfolder.folderid'),(2,19,12,100,'INNER','','AND individual.individualid = emailrecipient.recipientid AND emailrecipient.recipientlsgroup = \'NO\''),(19,3,100,24,'LEFT','','AND emailrecipient.recipientid = group.groupid AND emailrecipient.recipientlsgroup = \'YES\''),(20,33,104,162,'INNER','','AND attachment.fileid = file.fileid'),(2,18,19,92,'LEFT','','AND emailaccount.owner = individual.owner'),(23,107,129,480,'INNER','','AND calltype.calltypeid = call.calltype'),(2,22,12,126,'LEFT','','AND individual.individualid = attendee.individualid AND attendee.type=\'REQUIRED\''),(2,21,12,118,'LEFT','','AND individual.individualid = activity.owner'),(40,21,523,107,'LEFT','','AND literaturerequest.activityid = activity.activityid'),(96,21,426,107,'LEFT','','AND opportunity.activityid = activity.activityid'),(49,21,465,107,'LEFT','','AND task.activityid = activity.activityid'),(22,21,125,107,'LEFT','','AND attendee.activityid = activity.activityid'),(69,28,244,137,'LEFT','','AND record.recordid = activitylink.recordid'),(29,21,139,125,'LEFT','','AND recurrence.activityid = activity.activityid'),(31,21,149,139,'INNER','','AND activityaction.activityid = activity.activityid'),(106,21,477,123,'INNER','','AND activitytype.typeid = activity.type'),(110,21,486,121,'INNER','','AND activitystatus.statusid = status.status'),(109,21,483,119,'INNER','','AND activityprority.priorityid = activity.priority'),(108,22,481,127,'INNER','','AND attendeestatus.statusid = attendee.status'),(107,21,479,107,'LEFT','','AND call.activityid = activity.activityid'),(24,21,577,107,'LEFT','','AND appointment.activityid = activity.activityid'),(25,21,579,107,'LEFT','','AND meeting.activityid = activity.activityid'),(25,26,581,131,'LEFT','','AND meeting.resourceid = activityresources.activityresourceid'),(24,26,578,131,'LEFT','','AND appointment.resourceid = activityresources.activityresourceid'),(31,32,148,151,'INNER','','AND activityaction.actionid = action.actionid'),(29,30,141,147,'LEFT','','AND recurrence.recurrenceid = recurexcept.recurrenceid'),(111,2,492,19,'LEFT','','AND note.owner= individual.individualid'),(111,2,491,12,'LEFT','','AND note.creator= individual.individualid'),(111,2,495,12,'LEFT','','AND note.updatedby = individual.individualid'),(111,112,488,496,'LEFT','','AND note.noteid = notelink.noteid'),(112,69,498,244,'LEFT','','AND notelink.recordid = record.recordid'),(33,2,166,19,'LEFT','','AND file.owner = individual.owner'),(33,2,172,12,'LEFT','','AND file.updatedby = individual.individualid'),(33,2,159,12,'LEFT','','AND file.creator = individual.individualid'),(33,34,162,175,'LEFT','','AND file.fileid = filefolder.fileid'),(34,42,176,181,'INNER','','AND filefolder.folderid = folder.folderid'),(96,1,429,1,'LEFT','','AND opportunity.entityid = entity.entityid'),(96,2,430,12,'LEFT','','AND opportunity.individualid = individual.individualid'),(96,21,426,107,'LEFT','','AND opportunity.activityid = activity.activityid'),(96,100,425,446,'LEFT','','AND opportunity.opportunityid = opportunitylink.opportunityid'),(96,100,436,449,'INNER','','AND opportunity.probablity = salesprobablity.probablityid'),(96,97,431,440,'INNER','','AND opportunity.typeid = salestype.salestypeid'),(96,98,433,442,'INNER','','AND opportunity.stage = salesstage.salesstageid'),(96,99,432,444,'INNER','','AND opportunity.status = salesstatus.salesstatusid'),(69,100,242,448,'INNER','','AND opportunitylink.recordid = record.recordid'),(48,49,453,466,'LEFT','','AND project.projectid = task.projectid'),(48,105,453,474,'LEFT','','AND project.projectid = projectlink.projectid'),(48,2,460,19,'LEFT','','AND project.owner = individual.individualid'),(48,2,461,12,'LEFT','','AND project.createdby = individual.individualid'),(48,2,462,12,'LEFT','','AND project.modifiedby = individual.individualid'),(49,50,465,472,'INNER','','AND task.taskid = taskassigned.taskid'),(2,50,12,473,'INNER','','AND individual.individualid = taskassigned.individualid'),(69,105,244,476,'LEFT','','AND record.recordid = projectlink.recordid'),(37,1,564,10,'LEFT','','AND marketinglist.listid = entity.list'),(37,2,572,19,'LEFT','','AND marketinglist.owner = individual.owner'),(37,2,573,12,'LEFT','','AND marketinglist.creator = individual.individualid'),(37,2,574,12,'LEFT','','AND marketinglist.modifiedby = individual.individualid'),(2,43,19,541,'LEFT','','AND individual.owner = event.owner'),(2,43,12,542,'LEFT','','AND individual.individualid = event.creator'),(2,43,12,544,'LEFT','','AND individual.individualid = event.modifiedby'),(2,40,12,527,'LEFT','','AND individual.individualid = literaturerequest.requestedby'),(40,41,524,529,'LEFT','','AND literaturerequest.literatureid = literature.literatureid'),(40,42,528,533,'LEFT','','AND literaturerequest.deliverymethod = deliverymethod.deliveryid'),(43,44,535,546,'INNER','','AND event.eventid = eventregister.eventid'),(44,2,547,12,'INNER','','AND eventregister.individualid = individual.individualid'),(2,45,554,19,'LEFT','','AND individual.owner = promotion.owner'),(2,45,12,555,'LEFT','','AND individual.individualid = promotion.creator'),(2,45,12,556,'LEFT','','AND individual.individualid = promotion.modifiedby'),(45,46,548,559,'LEFT','','AND promotion.promotionid = promoitem.promotionid'),(46,78,560,297,'INNER','','AND promoitem.itemid = item.itemid'),(52,1,585,1,'LEFT','','AND ticket.entityid = entity.entityid'),(52,54,588,595,'LEFT','','AND ticket.status = supportstatus.statusid'),(52,56,589,607,'LEFT','','AND ticket.priority = supportpriority.priorityid'),(52,53,582,595,'LEFT','','AND ticket.ticketid = thread.ticketid'),(52,2,590,19,'LEFT','','AND ticket.owner = individual.owner'),(52,2,592,12,'LEFT','','AND ticket.createdby = individual.individualid'),(52,2,593,12,'LEFT','','AND ticket.modifiedby = individual.individualid'),(52,55,582,604,'LEFT','','AND ticket.ticketid = ticketlink.ticketid'),(53,2,602,12,'LEFT','','AND thread.creator = individual.individualid'),(55,69,606,244,'LEFT','','AND ticketlink.recordid = record.recordid'),(69,58,244,620,'LEFT','','AND record.recordid = knowledgebaselink.recordid'),(57,2,617,19,'LEFT','','AND FAQ.owner = individual.owner'),(57,2,612,12,'LEFT','','AND FAQ.createdby = individual.individualid'),(57,2,614,12,'LEFT','','AND FAQ.updatedby = individual.individualid'),(57,59,616,612,'LEFT','','AND FAQ.status = contentstatus.statusid'),(60,2,631,19,'LEFT','','AND knowledgebase.owner = individual.owner'),(60,2,626,12,'LEFT','','AND knowledgebase.createdby = individual.individualid'),(60,2,628,12,'LEFT','','AND knowledgebase.updatedby = individual.individualid'),(60,59,630,612,'LEFT','','AND knowledgebase.status = contentstatus.statusid'),(60,61,632,633,'LEFT','','AND knowledgebase.category = category.catid'),(93,94,403,415,'LEFT','','AND timeslip.timeslipid = timeentry.timeslipid'),(93,2,406,19,'LEFT','','AND timeslip.owner = individual.owner'),(93,2,407,12,'LEFT','','AND timeslip.creator = individual.individualid'),(93,2,408,12,'LEFT','','AND timeslip.modifiedby = individual.individualid'),(93,95,413,423,'LEFT','','AND timeslip.timestatus = status.statusid'),(94,52,417,582,'LEFT','','AND timeentry.ticketid = ticket.ticketid'),(94,48,416,453,'LEFT','','AND timeentry.projectid = project.projectid'),(73,74,258,280,'LEFT','','AND order.orderid = orderitem.orderid'),(73,77,264,294,'LEFT','','AND order.status = accountingstatus.statusid'),(73,79,265,309,'LEFT','','AND order.terms = accountingterms.termsid'),(73,84,258,324,'LEFT','','AND order.orderid = invoice.order'),(73,113,268,499,'LEFT','','AND order.proposal = proposal.proposalid'),(74,78,281,297,'LEFT','','AND orderitem.itemid = item.itemid'),(77,88,294,369,'LEFT','','AND accountingstatus.statusid = purchaseorder.status'),(77,84,294,327,'LEFT','','AND accountingstatus.statusid = invoice.status'),(79,84,309,328,'LEFT','','AND accountingterms.termsid = invoice.terms'),(75,76,290,291,'LEFT','','AND inventory.locationid = location.locationid'),(75,78,289,297,'LEFT','','AND inventory.item = item.itemid'),(78,81,302,318,'LEFT','','AND item.taxclass = taxclass.taxclassid'),(78,80,303,312,'LEFT','','AND item.type = itemtype.itemtypeid'),(78,90,297,388,'LEFT','','AND item.itemid = purchaseorderitem.itemid'),(81,82,315,318,'LEFT','','AND taxclass.taxclassid = taxmatrix.taxclassid'),(82,83,319,321,'LEFT','','AND taxmatrix.taxjurisdictionid = taxjurisdiction.taxjurisdictionid'),(88,90,354,387,'LEFT','','AND purchaseorder.purchaseorderid = purchaseorderitem.orderid'),(89,92,374,398,'LEFT','','AND expense.expenseid = expenseitem.expenseid'),(84,85,324,336,'LEFT','','AND invoice.invoiceid = applypayment.invoiceid'),(86,85,338,335,'LEFT','','AND payment.paymentid = applypayment.paymentid'),(86,87,342,352,'LEFT','','AND payment.paymentmethod = paymentmethod.methodid'),(48,116,645,643,'LEFT','','AND project.statusid=projectstatus.statusid'),(48,1,453,1,'LEFT','projectlink','AND project.projectid=projectlink.projectid and projectlink.recordid=entity.entityid');

--
-- Table structure for table `cvorder`
--

DROP TABLE IF EXISTS `cvorder`;
CREATE TABLE `cvorder` (
  `orderid` int(11) unsigned NOT NULL auto_increment,
  `title` varchar(25) default NULL,
  `description` text,
  `entityid` int(11) unsigned default NULL,
  `billindividual` int(11) unsigned default NULL,
  `shipindividual` int(11) unsigned default NULL,
  `billaddress` int(11) unsigned default NULL,
  `shipaddress` int(11) unsigned default NULL,
  `status` int(11) unsigned default NULL,
  `terms` int(11) unsigned default NULL,
  `accountmgr` int(11) unsigned default NULL,
  `project` int(11) unsigned default NULL,
  `proposal` int(11) unsigned default NULL,
  `subtotal` float default NULL,
  `tax` float default NULL,
  `shipping` float default NULL,
  `discount` float default NULL,
  `total` float default NULL,
  `creator` int(11) unsigned default NULL,
  `owner` int(11) unsigned default NULL,
  `modifiedby` int(11) unsigned default NULL,
  `created` datetime default NULL,
  `modified` timestamp(14) NOT NULL,
  `orderdate` date default '0000-00-00',
  `orderstatus` enum('Active','Deleted') NOT NULL default 'Active',
  `invoiceIsGenerated` enum('YES','NO') NOT NULL default 'NO',
  `ponumber` varchar(25) default NULL,
  PRIMARY KEY  (`orderid`),
  KEY `entityid` (`entityid`),
  KEY `billindividual` (`billindividual`),
  KEY `shipindividual` (`shipindividual`),
  KEY `billaddress` (`billaddress`),
  KEY `shipaddress` (`shipaddress`),
  KEY `status` (`status`),
  KEY `terms` (`terms`),
  KEY `accountmgr` (`accountmgr`),
  KEY `project` (`project`),
  KEY `proposal` (`proposal`),
  KEY `creator` (`creator`),
  KEY `owner` (`owner`),
  KEY `modifiedby` (`modifiedby`)
) TYPE=InnoDB;

--
-- Dumping data for table `cvorder`
--


--
-- Table structure for table `cvtable`
--

DROP TABLE IF EXISTS `cvtable`;
CREATE TABLE `cvtable` (
  `tableid` int(10) unsigned NOT NULL auto_increment,
  `moduleid` int(10) unsigned default NULL,
  `name` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`tableid`),
  KEY `moduleid` (`moduleid`)
) TYPE=InnoDB;

--
-- Dumping data for table `cvtable`
--

INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (1,14,'entity'),(2,15,'individual'),(3,16,'grouptbl'),(4,1,'address'),(5,1,'state'),(6,1,'country'),(7,1,'contacttype'),(8,1,'addresstype'),(9,1,'moctype'),(10,1,'methodofcontact'),(11,1,'mocrelate'),(12,1,'addressrelate'),(13,1,'member'),(14,1,'dbase'),(15,2,'emailmessage'),(16,2,'emailfolder'),(17,2,'emailstore'),(18,2,'emailaccount'),(19,2,'emailrecipient'),(20,2,'attachment'),(21,3,'activity'),(22,3,'attendee'),(23,3,'calltype'),(24,3,'appointment'),(25,3,'meeting'),(26,3,'activityresources'),(27,3,'resourcerelate'),(28,3,'activitylink'),(29,3,'recurrence'),(30,3,'recurexcept'),(31,3,'activityaction'),(32,3,'action'),(33,6,'cvfile'),(34,6,'cvfilefolder'),(35,6,'cvfolderlocation'),(36,6,'cvfolder'),(37,8,'marketinglist'),(40,8,'literaturerequest'),(41,8,'literature'),(42,8,'deliverymethod'),(43,8,'event'),(44,8,'eventregister'),(45,8,'promotion'),(46,8,'promoitem'),(48,9,'project'),(49,9,'task'),(50,9,'taskassigned'),(51,9,'projectlink'),(52,39,'ticket'),(53,10,'thread'),(54,10,'supportstatus'),(55,10,'ticketlink'),(56,10,'supportpriority'),(57,10,'faq'),(58,10,'knoledgebaselink'),(59,10,'contentstatus'),(60,10,'knowledgebase'),(61,10,'category'),(62,11,'role'),(63,11,'user'),(64,11,'alert'),(65,11,'moduleauthorization'),(66,11,'recordauthorization'),(67,11,'fieldauthorization'),(68,11,'module'),(69,11,'record'),(70,11,'field'),(71,11,'createrecordauthorization'),(72,11,'createfieldauthorization'),(73,12,'order'),(74,12,'orderitem'),(75,12,'p'),(76,12,'location'),(77,12,'accountingstatus'),(78,12,'item'),(79,12,'accountingterms'),(80,12,'itemtype'),(81,12,'taxclass'),(82,12,'taxmatrix'),(83,12,'taxjurisdiction'),(84,12,'invoice'),(85,12,'applypayment'),(86,12,'payment'),(87,12,'paymentmethod'),(88,12,'purchaseorder'),(89,12,'expense'),(90,12,'purchaseorderitem'),(91,12,'glaccount'),(92,12,'expenseitem'),(93,13,'timeslip'),(94,13,'timeentry'),(95,13,'timestatus'),(96,7,'opportunity'),(97,7,'salestype'),(98,7,'salesstage'),(99,7,'salesstatus'),(100,7,'opportunitylink'),(101,7,'salesprobablity'),(106,3,'activitytype'),(107,3,'call'),(108,3,'attendeestatus'),(109,3,'activitypriority'),(110,3,'activitystatus'),(111,5,'note'),(112,5,'notelink'),(113,7,'proposal'),(114,7,'proposallink'),(115,7,'proposalitem'),(116,9,'projectstatus');

--
-- Table structure for table `cvtableextra`
--

DROP TABLE IF EXISTS `cvtableextra`;
CREATE TABLE `cvtableextra` (
  `TableId` int(10) unsigned default NULL,
  `FullName` varchar(55) default NULL,
  `Appearance` enum('TOP','BOTTOM','BOTH') default 'BOTH',
  UNIQUE KEY `TableId` (`TableId`),
  CONSTRAINT `0_21439` FOREIGN KEY (`TableId`) REFERENCES `cvtable` (`tableid`)
) TYPE=InnoDB;

--
-- Dumping data for table `cvtableextra`
--

INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (1,'entity','BOTH'),(2,'individual','BOTH'),(3,'grouptbl','BOTH'),(4,'address','BOTH'),(5,'state','BOTH'),(6,'country','BOTH'),(7,'contacttype','BOTH'),(8,'addresstype','BOTH'),(9,'moctype','BOTH'),(10,'methodofcontact','BOTH'),(11,'mocrelate','BOTH'),(12,'addressrelate','BOTH'),(13,'member','BOTH'),(14,'dbase','BOTH'),(15,'emailmessage','BOTH'),(16,'emailfolder','BOTH'),(17,'emailstore','BOTH'),(18,'emailaccount','BOTH'),(19,'emailrecipient','BOTH'),(20,'attachment','BOTH'),(21,'activity','BOTH'),(22,'attendee','BOTH'),(23,'calltype','BOTH'),(24,'appointment','BOTH'),(25,'meeting','BOTH'),(26,'activityresources','BOTH'),(27,'resourcerelate','BOTH'),(28,'activitylink','BOTH'),(29,'recurrence','BOTH'),(30,'recurexcept','BOTH'),(31,'activityaction','BOTH'),(32,'action','BOTH'),(33,'cvfile','BOTH'),(34,'cvfilefolder','BOTH'),(35,'cvfolderlocation','BOTH'),(36,'cvfolder','BOTH'),(37,'marketinglist','BOTH'),(40,'literaturerequest','BOTH'),(41,'literature','BOTH'),(42,'deliverymethod','BOTH'),(43,'event','BOTH'),(44,'eventregister','BOTH'),(45,'promotion','BOTH'),(46,'promoitem','BOTH'),(48,'project','BOTH'),(49,'task','BOTH'),(50,'taskassigned','BOTH'),(51,'projectlink','BOTH'),(52,'ticket','BOTH'),(53,'thread','BOTH'),(54,'supportstatus','BOTH'),(55,'ticketlink','BOTH'),(56,'supportpriority','BOTH'),(57,'faq','BOTH'),(58,'knoledgebaselink','BOTH'),(59,'contentstatus','BOTH'),(60,'knowledgebase','BOTH'),(61,'category','BOTH'),(62,'role','BOTH'),(63,'user','BOTH'),(64,'alert','BOTH'),(65,'moduleauthorization','BOTH'),(66,'recordauthorization','BOTH'),(67,'fieldauthorization','BOTH'),(68,'module','BOTH'),(69,'record','BOTH'),(70,'field','BOTH'),(71,'createrecordauthorization','BOTH'),(72,'createfieldauthorization','BOTH'),(73,'order','BOTH'),(74,'orderitem','BOTH'),(75,'p','BOTH'),(76,'location','BOTH'),(77,'accountingstatus','BOTH'),(78,'item','BOTH'),(79,'accountingterms','BOTH'),(80,'itemtype','BOTH'),(81,'taxclass','BOTH'),(82,'taxmatrix','BOTH'),(83,'taxjurisdiction','BOTH'),(84,'invoice','BOTH'),(85,'applypayment','BOTH'),(86,'payment','BOTH'),(87,'paymentmethod','BOTH'),(88,'purchaseorder','BOTH'),(89,'expense','BOTH'),(90,'purchaseorderitem','BOTH'),(91,'glaccount','BOTH'),(92,'expenseitem','BOTH'),(93,'timeslip','BOTH'),(94,'timeentry','BOTH'),(95,'timestatus','BOTH'),(96,'opportunity','BOTH'),(97,'salestype','BOTH'),(98,'salesstage','BOTH'),(99,'salesstatus','BOTH'),(100,'opportunitylink','BOTH'),(101,'salesprobablity','BOTH'),(106,'activitytype','BOTH'),(107,'call','BOTH'),(108,'attendeestatus','BOTH'),(109,'activitypriority','BOTH'),(110,'activitystatus','BOTH'),(111,'note','BOTH'),(112,'notelink','BOTH'),(113,'proposal','BOTH'),(114,'proposallink','BOTH'),(115,'proposalitem','BOTH'),(116,'projectstatus','BOTH');

--
-- Table structure for table `defaultprivilege`
--

DROP TABLE IF EXISTS `defaultprivilege`;
CREATE TABLE `defaultprivilege` (
  `DefaultPrivilegeId` int(10) unsigned NOT NULL auto_increment,
  `OwnerId` int(10) unsigned NOT NULL default '0',
  `IndividualId` int(10) unsigned NOT NULL default '0',
  `PrivilegeLevel` tinyint(4) NOT NULL default '0',
  PRIMARY KEY  (`DefaultPrivilegeId`),
  KEY `OwnerId` (`OwnerId`),
  KEY `IndividualId` (`IndividualId`),
  CONSTRAINT `0_21441` FOREIGN KEY (`OwnerId`) REFERENCES `individual` (`IndividualID`) ON DELETE CASCADE,
  CONSTRAINT `0_21442` FOREIGN KEY (`IndividualId`) REFERENCES `individual` (`IndividualID`) ON DELETE CASCADE
) TYPE=InnoDB;

--
-- Dumping data for table `defaultprivilege`
--


--
-- Table structure for table `defaultrecordauthorisation`
--

DROP TABLE IF EXISTS `defaultrecordauthorisation`;
CREATE TABLE `defaultrecordauthorisation` (
  `recordid` int(11) unsigned default NULL,
  `recordtypeid` int(11) unsigned default NULL,
  `privilegelevel` tinyint(4) NOT NULL default '0',
  KEY `recordid` (`recordid`),
  KEY `recordtypeid` (`recordtypeid`)
) TYPE=InnoDB;

--
-- Dumping data for table `defaultrecordauthorisation`
--

INSERT INTO `defaultrecordauthorisation` (`recordid`, `recordtypeid`, `privilegelevel`) VALUES (1,76,30),(2,76,30),(3,76,30),(4,76,30),(5,76,10),(6,76,10),(7,76,30),(1,32,30),(9,76,20);

--
-- Table structure for table `defaultviews`
--

DROP TABLE IF EXISTS `defaultviews`;
CREATE TABLE `defaultviews` (
  `listtype` varchar(30) NOT NULL default '',
  `viewid` int(10) unsigned default NULL,
  PRIMARY KEY  (`listtype`),
  KEY `viewid` (`viewid`)
) TYPE=InnoDB;

--
-- Dumping data for table `defaultviews`
--

INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Entity',1),('Individual',3),('Group',4),('GroupMember',5),('AllActivity',6),('Address',7),('Appointment',8),('BottomIndividual',9),('Call',10),('CustomField',11),('Email',12),('File',13),('ForecastSales',14),('LiteratureRequests',17),('Meeting',18),('MOC',19),('MultiActivity',20),('NextAction',21),('Note',22),('Rule',23),('Tasks',24),('ToDo',25),('EmailLookup',30),('Ticket',66),('FAQ',70),('KnowledgeBase',71),('LiteratureFulfillment',72),('Event',73),('GLAccount',74),('Marketing',75),('Promotion',76),('Item',77),('Inventory',79),('Order',81),('Payment',82),('Expense',83),('PurchaseOrder',84),('VolumeDiscount',85),('Vendor',86),('InvoiceHistory',87),('BottomTask',88),('BottomTimeslip',89),('Task',90),('Project',91),('Timeslip',92),('Opportunity',93),('Proposal',94),('ActivityTask',100),('BottomMultiActivity',101),('BottomContacts',104),('BottomFiles',105),('BottomNotes',106),('CustomFields',110),('Employee',170),('TimeSheet',171),('Expenses',172),('EmployeeHandbook',173),('USER',174),('SavedSearch',175),('CustomView',177),('SecurityProfile',179),('CVAttic',180),('CVGarbage',181),('History',182),('EventAtendees',183),('StandardReport',185),('AdHocReport',186),('CustomReport',187),('ReportResult',188),('MarketingListMembers',189),('Literature',190),('EntityMerge',191),('BottomProposal',192),('Template',193),('IndividualMerge',194);

--
-- Table structure for table `delegation`
--

DROP TABLE IF EXISTS `delegation`;
CREATE TABLE `delegation` (
  `delegationid` int(10) unsigned NOT NULL auto_increment,
  `fromuser` int(10) unsigned NOT NULL default '0',
  `touser` int(10) unsigned NOT NULL default '0',
  `moduleid` int(10) unsigned NOT NULL default '0',
  `righttype` enum('VIEW','SEND','SCHEDULE') NOT NULL default 'VIEW',
  PRIMARY KEY  (`delegationid`),
  KEY `fromuser` (`fromuser`),
  KEY `touser` (`touser`),
  KEY `moduleid` (`moduleid`)
) TYPE=InnoDB;

--
-- Dumping data for table `delegation`
--


--
-- Table structure for table `deliverymethod`
--

DROP TABLE IF EXISTS `deliverymethod`;
CREATE TABLE `deliverymethod` (
  `DeliveryID` int(10) unsigned NOT NULL auto_increment,
  `Name` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`DeliveryID`)
) TYPE=InnoDB;

--
-- Dumping data for table `deliverymethod`
--

INSERT INTO `deliverymethod` (`DeliveryID`, `Name`) VALUES (1,'Email'),(2,'Fax'),(3,'Mail'),(4,'Postal');

--
-- Table structure for table `emailaccount`
--

DROP TABLE IF EXISTS `emailaccount`;
CREATE TABLE `emailaccount` (
  `AccountID` int(11) unsigned NOT NULL auto_increment,
  `Owner` int(11) unsigned default NULL,
  `Name` varchar(50) default NULL,
  `Login` varchar(50) default NULL,
  `Password` varchar(25) default NULL,
  `SMTPServer` varchar(50) default NULL,
  `ServerType` enum('imap','pop3') default NULL,
  `LeaveOnServer` enum('YES','NO') default NULL,
  `Address` varchar(100) default NULL,
  `ReplyTo` varchar(100) default NULL,
  `Signature` text,
  `Default` enum('YES','NO') default 'NO',
  `mailserver` varchar(50) default NULL,
  `lastfetchedcount` int(11) default NULL,
  `lastfetcheddate` datetime default NULL,
  `lastuid` varchar(20) default NULL,
  `Port` int(4) unsigned NOT NULL default '125',
  `SecureConnectionIfPossible` enum('NO','YES') NOT NULL default 'NO',
  `ForceSecureConnection` enum('NO','YES') NOT NULL default 'NO',
  `SMTPRequiresAuthentication` enum('NO','YES') NOT NULL default 'NO',
  `POPBeforeSMTP` enum('NO','YES') NOT NULL default 'NO',
  PRIMARY KEY  (`AccountID`),
  KEY `Owner` (`Owner`)
) TYPE=InnoDB;

--
-- Dumping data for table `emailaccount`
--


--
-- Table structure for table `emailaction`
--

DROP TABLE IF EXISTS `emailaction`;
CREATE TABLE `emailaction` (
  `RuleID` int(11) default NULL,
  `ActionName` enum('MOVE','DELETE','MARK_AS_READ') default NULL,
  `TargetID` int(11) default NULL,
  KEY `RuleID` (`RuleID`),
  KEY `TargetID` (`TargetID`)
) TYPE=InnoDB;

--
-- Dumping data for table `emailaction`
--


--
-- Table structure for table `emailcomposition`
--

DROP TABLE IF EXISTS `emailcomposition`;
CREATE TABLE `emailcomposition` (
  `individualid` int(10) unsigned default NULL,
  `composestyle` enum('TEXT','HTML') default NULL,
  KEY `individualid` (`individualid`)
) TYPE=InnoDB ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table `emailcomposition`
--


--
-- Table structure for table `emaildelegation`
--

DROP TABLE IF EXISTS `emaildelegation`;
CREATE TABLE `emaildelegation` (
  `individualID` int(11) unsigned NOT NULL default '0',
  `delegatorID` int(11) unsigned NOT NULL default '0',
  UNIQUE KEY `delegationID` (`individualID`,`delegatorID`)
) TYPE=InnoDB;

--
-- Dumping data for table `emaildelegation`
--


--
-- Table structure for table `emailfolder`
--

DROP TABLE IF EXISTS `emailfolder`;
CREATE TABLE `emailfolder` (
  `Parent` int(11) unsigned default NULL,
  `FolderID` int(11) unsigned NOT NULL auto_increment,
  `AccountID` int(11) unsigned default NULL,
  `Name` varchar(25) default NULL,
  `Ftype` enum('SYSTEM','USER') default NULL,
  `FullName` varchar(255) default NULL,
  PRIMARY KEY  (`FolderID`),
  KEY `Parent` (`Parent`),
  KEY `AccountID` (`AccountID`)
) TYPE=InnoDB;

--
-- Dumping data for table `emailfolder`
--

INSERT INTO `emailfolder` (`Parent`, `FolderID`, `AccountID`, `Name`, `Ftype`, `FullName`) VALUES (0,1,1,'root','SYSTEM',NULL);

--
-- Table structure for table `emailmessage`
--

DROP TABLE IF EXISTS `emailmessage`;
CREATE TABLE `emailmessage` (
  `MessageID` int(11) unsigned NOT NULL auto_increment,
  `MessageDate` datetime default NULL,
  `MailFrom` varchar(255) default NULL,
  `FromIndividual` int(11) unsigned default NULL,
  `ReplyTo` varchar(255) default NULL,
  `Subject` text,
  `Headers` text,
  `Owner` int(11) unsigned default NULL,
  `CreatedBy` int(11) unsigned default NULL,
  `size` int(11) default NULL,
  `Priority` enum('HIGH','MEDIUM','LOW') default NULL,
  `Importance` enum('YES','NO') default NULL,
  `Body` text,
  `AccountID` int(11) unsigned default NULL,
  `UID` varchar(22) default NULL,
  `LocallyDeleted` enum('NO','YES') NOT NULL default 'NO',
  `LocallyModified` enum('NO','YES') NOT NULL default 'NO',
  `MessageRead` enum('NO','YES') NOT NULL default 'NO',
  `MovedToFolder` int(11) unsigned default '0',
  `CopiedToFolder` int(11) unsigned default '0',
  `contentType` enum('text/plain','text/html') NOT NULL default 'text/plain',
  `private` enum('YES','NO') default 'NO',
  `ToList` varchar(255) default NULL,
  PRIMARY KEY  (`MessageID`),
  KEY `FromIndividual` (`FromIndividual`),
  KEY `Owner` (`Owner`),
  KEY `CreatedBy` (`CreatedBy`),
  KEY `AccountID` (`AccountID`)
) TYPE=InnoDB;

--
-- Dumping data for table `emailmessage`
--


--
-- Table structure for table `emailmessagefolder`
--

DROP TABLE IF EXISTS `emailmessagefolder`;
CREATE TABLE `emailmessagefolder` (
  `MessageID` int(11) unsigned default NULL,
  `FolderID` int(11) unsigned default NULL,
  UNIQUE KEY `SearchModuleID` (`MessageID`,`FolderID`),
  KEY `MessageID` (`MessageID`),
  KEY `FolderID` (`FolderID`),
  CONSTRAINT `0_21617` FOREIGN KEY (`MessageID`) REFERENCES `emailmessage` (`MessageID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `0_21618` FOREIGN KEY (`FolderID`) REFERENCES `emailfolder` (`FolderID`) ON DELETE CASCADE ON UPDATE CASCADE
) TYPE=InnoDB;

--
-- Dumping data for table `emailmessagefolder`
--


--
-- Table structure for table `emailpreference`
--

DROP TABLE IF EXISTS `emailpreference`;
CREATE TABLE `emailpreference` (
  `individualid` int(11) default NULL,
  `composestyle` enum('TEXT','HTML') default NULL,
  KEY `individualid` (`individualid`)
) TYPE=InnoDB;

--
-- Dumping data for table `emailpreference`
--


--
-- Table structure for table `emailrecipient`
--

DROP TABLE IF EXISTS `emailrecipient`;
CREATE TABLE `emailrecipient` (
  `MessageID` int(11) unsigned default NULL,
  `RecipientID` int(11) unsigned default NULL,
  `Address` varchar(255) default NULL,
  `RecipientType` enum('TO','CC','BCC','FROM') default NULL,
  `RecipientIsGroup` enum('YES','NO') default NULL,
  KEY `MessageID` (`MessageID`),
  KEY `RecipientID` (`RecipientID`)
) TYPE=InnoDB;

--
-- Dumping data for table `emailrecipient`
--


--
-- Table structure for table `emailrule`
--

DROP TABLE IF EXISTS `emailrule`;
CREATE TABLE `emailrule` (
  `ruleID` int(11) unsigned NOT NULL auto_increment,
  `accountID` int(11) default NULL,
  `name` varchar(50) default NULL,
  `description` varchar(200) default NULL,
  `enabled` enum('YES','NO') default NULL,
  PRIMARY KEY  (`ruleID`),
  KEY `accountID` (`accountID`)
) TYPE=InnoDB;

--
-- Dumping data for table `emailrule`
--


--
-- Table structure for table `emailruleaction`
--

DROP TABLE IF EXISTS `emailruleaction`;
CREATE TABLE `emailruleaction` (
  `ruleID` int(11) unsigned NOT NULL default '0',
  `actionType` enum('MOVE','MARK','DEL') NOT NULL default 'MOVE',
  `folderID` int(11) unsigned NOT NULL default '0',
  KEY `ruleID` (`ruleID`),
  KEY `folderID` (`folderID`),
  CONSTRAINT `0_21655` FOREIGN KEY (`ruleID`) REFERENCES `emailrule` (`ruleID`) ON DELETE CASCADE ON UPDATE CASCADE
) TYPE=InnoDB;

--
-- Dumping data for table `emailruleaction`
--


--
-- Table structure for table `emailrulecriteria`
--

DROP TABLE IF EXISTS `emailrulecriteria`;
CREATE TABLE `emailrulecriteria` (
  `ruleID` int(11) unsigned NOT NULL default '0',
  `orderID` int(11) unsigned NOT NULL default '0',
  `expressionType` enum('AND','OR') NOT NULL default 'OR',
  `fieldID` int(11) unsigned NOT NULL default '0',
  `conditionID` int(11) unsigned NOT NULL default '0',
  `value` varchar(255) NOT NULL default '',
  KEY `ruleID` (`ruleID`),
  KEY `fieldID` (`fieldID`),
  KEY `conditionID` (`conditionID`),
  CONSTRAINT `0_21653` FOREIGN KEY (`ruleID`) REFERENCES `emailrule` (`ruleID`) ON DELETE CASCADE ON UPDATE CASCADE
) TYPE=InnoDB;

--
-- Dumping data for table `emailrulecriteria`
--


--
-- Table structure for table `emailsettings`
--

DROP TABLE IF EXISTS `emailsettings`;
CREATE TABLE `emailsettings` (
  `username` varchar(50) default NULL,
  `password` varchar(25) default NULL,
  `authentication` enum('YES','NO') default 'NO',
  `smtpport` int(4) unsigned NOT NULL default '25',
  `smtpserver` varchar(255) default NULL
) TYPE=InnoDB;

--
-- Dumping data for table `emailsettings`
--

INSERT INTO `emailsettings` (`username`, `password`, `authentication`, `smtpport`, `smtpserver`) VALUES (NULL,NULL,'NO',25,NULL);

--
-- Table structure for table `emailtemplate`
--

DROP TABLE IF EXISTS `emailtemplate`;
CREATE TABLE `emailtemplate` (
  `templateID` int(11) unsigned NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `description` varchar(255) default NULL,
  `toAddress` varchar(50) default NULL,
  `fromAddress` varchar(50) default NULL,
  `replyTo` varchar(50) default NULL,
  `subject` text,
  `body` text,
  `requiredToAddress` enum('YES','NO') default 'NO',
  `requiredFromAddress` enum('YES','NO') default 'NO',
  `requiredReplyTo` enum('YES','NO') default 'NO',
  `requiredSubject` enum('YES','NO') default 'NO',
  `requiredBody` enum('YES','NO') default 'NO',
  PRIMARY KEY  (`templateID`)
) TYPE=InnoDB;

--
-- Dumping data for table `emailtemplate`
--

INSERT INTO `emailtemplate` (`templateID`, `name`, `description`, `toAddress`, `fromAddress`, `replyTo`, `subject`, `body`, `requiredToAddress`, `requiredFromAddress`, `requiredReplyTo`, `requiredSubject`, `requiredBody`) VALUES (1,'Support Ticket','Auto-response Template for New Tickets.','','','','',NULL,'NO','NO','NO','YES','YES'),(2,'Support Thread','Auto-response Template for Open Tickets (New Threads).','','','','',NULL,'NO','NO','NO','NO','YES'),(3,'Support Error','Auto-response Template for Errors (Invalid Ticket ID).','','','','',NULL,'NO','NO','NO','NO','YES'),(4,'Activities','Auto-response Template for reminding email to Attendee\'s of Activity.','','','','','','NO','NO','NO','YES','YES'),(5,'Task','Auto-response Template to notify that Task is completed.','','','','','','NO','NO','NO','YES','YES'),(6,'Forgot Password','Auto-response Template for Forgot Password.','','','','','','NO','YES','YES','YES','NO'),(7,'Suggestion Box','Auto-response Template for Suggestion Box.','','','','','','YES','YES','YES','YES','NO'),(8,'Change Request for Profile','Auto-response Template for Profile change request.','','','','','','YES','YES','YES','YES','YES'),(9,'Change Request for User','Auto-response Template for User Information change request.','','','','','','YES','YES','YES','YES','YES');

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `EmployeeID` int(10) unsigned NOT NULL auto_increment,
  `IndividualID` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`EmployeeID`),
  KEY `IndividualID` (`IndividualID`)
) TYPE=InnoDB;

--
-- Dumping data for table `employee`
--

INSERT INTO `employee` (`EmployeeID`, `IndividualID`) VALUES (1,1);

--
-- Table structure for table `entity`
--

DROP TABLE IF EXISTS `entity`;
CREATE TABLE `entity` (
  `EntityID` int(10) unsigned NOT NULL auto_increment,
  `ExternalID` varchar(25) default NULL,
  `name` varchar(255) default NULL,
  `Source` int(10) unsigned NOT NULL default '0',
  `Modified` timestamp(14) NOT NULL,
  `Created` timestamp(14) NOT NULL default '00000000000000',
  `Owner` int(10) unsigned default NULL,
  `ModifiedBy` int(10) unsigned default NULL,
  `DBase` int(10) unsigned default NULL,
  `List` int(10) unsigned default NULL,
  `Creator` int(10) unsigned default NULL,
  `AccountManagerID` int(10) unsigned default '0',
  `AccountTeamID` int(10) unsigned default '0',
  PRIMARY KEY  (`EntityID`),
  UNIQUE KEY `EntityID` (`EntityID`),
  KEY `DBase` (`DBase`),
  KEY `Owner` (`Owner`),
  KEY `ModifiedBy` (`ModifiedBy`),
  KEY `Source` (`Source`),
  KEY `List` (`List`),
  KEY `Creator` (`Creator`),
  KEY `AccountManagerID` (`AccountManagerID`),
  KEY `AccountTeamID` (`AccountTeamID`)
) TYPE=InnoDB;

--
-- Dumping data for table `entity`
--

INSERT INTO `entity` (`EntityID`, `ExternalID`, `name`, `Source`, `Modified`, `Created`, `Owner`, `ModifiedBy`, `DBase`, `List`, `Creator`, `AccountManagerID`, `AccountTeamID`) VALUES (1,'','Default Entity',0,20040308092111,20040308092111,1,1,0,1,1,NULL,NULL),(2,'','CentraView, LLC',0,20041116170152,20041116170152,1,NULL,0,1,1,1,0);

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
CREATE TABLE `event` (
  `EventID` int(10) unsigned NOT NULL auto_increment,
  `Title` varchar(100) NOT NULL default '',
  `Detail` varchar(100) NOT NULL default '',
  `Start` datetime NOT NULL default '0000-00-00 00:00:00',
  `End` datetime NOT NULL default '0000-00-00 00:00:00',
  `ForMember` varchar(100) default 'INTERNAL',
  `Owner` int(10) unsigned NOT NULL default '0',
  `Creator` int(10) unsigned NOT NULL default '0',
  `Created` datetime NOT NULL default '0000-00-00 00:00:00',
  `ModifiedBy` int(10) unsigned default '0',
  `Modified` timestamp(14) NOT NULL,
  `maxattendees` int(10) default '0',
  `moderator` int(10) unsigned default '0',
  PRIMARY KEY  (`EventID`),
  KEY `Owner` (`Owner`),
  KEY `Creator` (`Creator`),
  KEY `ModifiedBy` (`ModifiedBy`),
  KEY `moderator` (`moderator`)
) TYPE=InnoDB;

--
-- Dumping data for table `event`
--


--
-- Table structure for table `eventlink`
--

DROP TABLE IF EXISTS `eventlink`;
CREATE TABLE `eventlink` (
  `EventID` int(11) unsigned NOT NULL default '0',
  `RecordTypeID` int(11) unsigned NOT NULL default '0',
  `RecordID` int(11) unsigned NOT NULL default '0',
  KEY `EventID` (`EventID`),
  KEY `RecordTypeID` (`RecordTypeID`),
  KEY `RecordID` (`RecordID`)
) TYPE=InnoDB;

--
-- Dumping data for table `eventlink`
--


--
-- Table structure for table `eventregister`
--

DROP TABLE IF EXISTS `eventregister`;
CREATE TABLE `eventregister` (
  `EventID` int(10) unsigned NOT NULL default '0',
  `IndividualID` int(10) unsigned NOT NULL default '0',
  `accepted` enum('NO','YES') NOT NULL default 'NO',
  KEY `EventID` (`EventID`),
  KEY `IndividualID` (`IndividualID`)
) TYPE=InnoDB;

--
-- Dumping data for table `eventregister`
--


--
-- Table structure for table `expense`
--

DROP TABLE IF EXISTS `expense`;
CREATE TABLE `expense` (
  `ExpenseID` int(11) unsigned NOT NULL auto_increment,
  `ExternalID` varchar(100) default '0',
  `Title` varchar(50) NOT NULL default '',
  `Description` text NOT NULL,
  `EntityID` int(11) unsigned NOT NULL default '0',
  `Status` enum('Paid','Unpaid','Reimbursed') NOT NULL default 'Paid',
  `Project` int(11) unsigned NOT NULL default '0',
  `Ticket` int(11) unsigned NOT NULL default '0',
  `Opportunity` int(11) unsigned NOT NULL default '0',
  `Owner` int(11) unsigned NOT NULL default '0',
  `Creator` int(11) unsigned NOT NULL default '0',
  `ModifiedBy` int(11) unsigned NOT NULL default '0',
  `Created` timestamp(14) NOT NULL,
  `Modified` timestamp(14) NOT NULL default '00000000000000',
  `LineID` int(11) unsigned NOT NULL default '0',
  `LineStatus` enum('Active','Deleted','New') NOT NULL default 'Active',
  `Amount` float NOT NULL default '0',
  `GLAccountsID` int(10) unsigned NOT NULL default '0',
  `Notes` text,
  `ExpenseFormID` int(11) NOT NULL default '0',
  PRIMARY KEY  (`ExpenseID`),
  KEY `EntityID` (`EntityID`),
  KEY `Project` (`Project`),
  KEY `Ticket` (`Ticket`),
  KEY `Opportunity` (`Opportunity`),
  KEY `Owner` (`Owner`),
  KEY `Creator` (`Creator`),
  KEY `ModifiedBy` (`ModifiedBy`),
  KEY `LineID` (`LineID`),
  KEY `GLAccountsID` (`GLAccountsID`),
  KEY `ExpenseFormID` (`ExpenseFormID`)
) TYPE=InnoDB;

--
-- Dumping data for table `expense`
--


--
-- Table structure for table `expenseform`
--

DROP TABLE IF EXISTS `expenseform`;
CREATE TABLE `expenseform` (
  `ExpenseFormID` int(10) unsigned NOT NULL auto_increment,
  `FromDate` date NOT NULL default '0000-00-00',
  `ToDate` date NOT NULL default '0000-00-00',
  `Description` text,
  `Note` text,
  `ReportingTo` int(10) unsigned NOT NULL default '0',
  `Owner` int(10) unsigned NOT NULL default '0',
  `Creator` int(10) unsigned NOT NULL default '0',
  `ModifiedBy` int(10) unsigned NOT NULL default '0',
  `Created` datetime NOT NULL default '0000-00-00 00:00:00',
  `Modified` datetime default NULL,
  `Status` enum('Paid','Unpaid') NOT NULL default 'Unpaid',
  `employeeID` int(10) unsigned NOT NULL default '0',
  `lineStatus` enum('Deleted','Active') NOT NULL default 'Active',
  PRIMARY KEY  (`ExpenseFormID`),
  KEY `ReportingTo` (`ReportingTo`),
  KEY `Owner` (`Owner`),
  KEY `Creator` (`Creator`),
  KEY `ModifiedBy` (`ModifiedBy`)
) TYPE=InnoDB;

--
-- Dumping data for table `expenseform`
--


--
-- Table structure for table `expenseitem`
--

DROP TABLE IF EXISTS `expenseitem`;
CREATE TABLE `expenseitem` (
  `ExpenseID` int(10) unsigned NOT NULL default '0',
  `ExpenseItemID` int(10) unsigned NOT NULL default '0',
  `SKU` varchar(50) NOT NULL default '',
  `Description` text NOT NULL,
  `Price` float NOT NULL default '0',
  `Quantity` int(10) unsigned NOT NULL default '0',
  `LineID` int(11) unsigned NOT NULL auto_increment,
  `LineStatus` enum('Active','Deleted') NOT NULL default 'Active',
  PRIMARY KEY  (`LineID`),
  KEY `ExpenseID` (`ExpenseID`),
  KEY `ExpenseItemID` (`ExpenseItemID`)
) TYPE=InnoDB;

--
-- Dumping data for table `expenseitem`
--


--
-- Table structure for table `faq`
--

DROP TABLE IF EXISTS `faq`;
CREATE TABLE `faq` (
  `faqid` int(10) unsigned NOT NULL auto_increment,
  `title` varchar(150) default NULL,
  `detail` text,
  `createdby` int(10) unsigned NOT NULL default '0',
  `created` datetime NOT NULL default '0000-00-00 00:00:00',
  `updatedby` int(10) unsigned NOT NULL default '0',
  `updated` timestamp(14) NOT NULL,
  `status` enum('DRAFT','PUBLISH') NOT NULL default 'DRAFT',
  `owner` int(10) unsigned NOT NULL default '0',
  `publishToCustomerView` enum('YES','NO') NOT NULL default 'NO',
  PRIMARY KEY  (`faqid`),
  KEY `createdby` (`createdby`),
  KEY `updatedby` (`updatedby`),
  KEY `owner` (`owner`)
) TYPE=InnoDB;

--
-- Dumping data for table `faq`
--


--
-- Table structure for table `field`
--

DROP TABLE IF EXISTS `field`;
CREATE TABLE `field` (
  `fieldid` int(11) unsigned NOT NULL auto_increment,
  `tableid` int(10) default NULL,
  `name` varchar(25) default NULL,
  PRIMARY KEY  (`fieldid`),
  KEY `tableid` (`tableid`)
) TYPE=InnoDB;

--
-- Dumping data for table `field`
--

INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (1,1,'EntityID'),(2,1,'ExternalID'),(3,1,'Name'),(4,1,'Source'),(5,1,'Modified'),(6,1,'Created'),(7,1,'Owner'),(8,1,'ModifiedBy'),(9,1,'Dbase'),(10,1,'List'),(11,1,'Creator'),(12,2,'IndividualID'),(13,2,'Entity'),(14,2,'FirstName'),(15,2,'MiddleInitial'),(16,2,'LastName'),(17,2,'Title'),(18,2,'PrimaryContact'),(19,2,'Owner'),(20,2,'CreatedBy'),(21,2,'ModifiedBy'),(22,2,'Created'),(23,2,'Modified'),(24,3,'GroupID'),(25,3,'Name'),(26,3,'Description'),(27,4,'AddressID'),(28,4,'AddressType'),(29,4,'Street1'),(30,4,'Street2'),(31,4,'City'),(32,4,'State'),(33,4,'Zip'),(34,4,'Country'),(35,5,'StateID'),(36,5,'Name'),(37,6,'CountryID'),(38,6,'Name'),(39,7,'ContactTypeID'),(40,7,'Name'),(41,8,'TypeID'),(42,8,'Name'),(43,9,'MOCTypeID'),(44,9,'Name'),(45,10,'MOCID'),(46,10,'MOCType'),(47,10,'Content'),(48,10,'Note'),(49,11,'MOCID'),(50,11,'ContactType'),(51,11,'ContactID'),(52,11,'IsPrimary'),(53,12,'Address'),(54,12,'ContactType'),(55,12,'Contact'),(56,12,'AddressType'),(57,13,'GroupID'),(58,13,'ChildID'),(59,14,'DBaseID'),(60,14,'Name'),(61,1,'AccountManagerID'),(62,1,'AccountTeamID'),(63,15,'AccountID'),(64,15,'Body'),(65,15,'CreatedBy'),(66,15,'FromIndividual'),(67,15,'Headers'),(68,15,'Importance'),(69,15,'MailFrom'),(70,15,'MessageDate'),(71,15,'MessageID'),(72,15,'Owner'),(73,15,'Prority'),(74,15,'Reply To'),(75,15,'size'),(76,15,'subject'),(77,16,'Parent'),(78,16,'FolderID'),(79,16,'AccountID'),(80,16,'Name'),(81,16,'Ftype'),(82,17,'MessageID'),(83,17,'EmailFolder'),(84,17,'ReadStatus'),(85,18,'AccountID'),(86,18,'Address'),(87,18,'Default'),(88,18,'LeaveOnServer'),(89,18,'Login'),(90,18,'mailserver'),(91,18,'Name'),(92,18,'Owner'),(93,18,'Password'),(94,18,'Reply To'),(95,18,'ServerType'),(96,18,'Signature'),(97,18,'SMTPServer'),(98,19,'Address'),(99,19,'MessageID'),(100,19,'RecipientID'),(101,19,'RecipientlsGroup'),(102,19,'RecipientType'),(103,20,'AttachmentID'),(104,20,'FileID'),(105,20,'FileName'),(106,20,'MessageID'),(107,21,'ActivityID'),(108,21,'AllDay'),(109,21,'CompletedDate'),(110,21,'Created'),(111,21,'Creator'),(112,21,'Details'),(113,21,'DueDate'),(114,21,'End'),(115,21,'Location'),(116,21,'Modified'),(117,21,'ModifiedBy'),(118,21,'Owner'),(119,21,'Priority'),(120,21,'Start'),(121,21,'Status'),(122,21,'Title'),(123,21,'Type'),(124,21,'visibility'),(125,22,'ActivityID'),(126,22,'IndividualID'),(127,22,'Status'),(128,22,'Type'),(129,23,'CallTypeID'),(130,23,'Name'),(131,26,'ActivityResourceID'),(132,26,'Detail'),(133,26,'Name'),(134,27,'ActivityID'),(135,27,'ResourceID'),(136,28,'ActivityID'),(137,28,'RecordID'),(138,28,'RecordTypeID'),(139,29,'ActivityID'),(140,29,'Every'),(141,29,'RecurrenceID'),(142,29,'RecurrOn'),(143,29,'startdate'),(144,29,'TimePeriod'),(145,29,'Until'),(146,30,'Exception'),(147,30,'RecurrenceID'),(148,31,'ActionID'),(149,31,'ActivityID'),(150,31,'recipient'),(151,32,'ActionID'),(152,32,'ActionInterval'),(153,32,'Action Time'),(154,32,'Message'),(155,32,'Type'),(156,32,'Repeat'),(157,33,'Author'),(158,33,'Created'),(159,33,'Creator'),(160,33,'CustomerView'),(161,33,'Description'),(162,33,'FileID'),(163,33,'FileSize'),(164,33,'IsTemporary'),(165,33,'Name'),(166,33,'Owner'),(167,33,'RelateEntity'),(168,33,'RelateIndividual'),(169,33,'Status'),(170,33,'Title'),(171,33,'Updated'),(172,33,'UpdatedBy'),(173,33,'Version'),(174,33,'visibility'),(175,34,'fileid'),(176,34,'folderid'),(177,34,'referencetype'),(178,36,'CreatedBy'),(179,36,'CreatedOn'),(180,36,'Description'),(181,36,'FolderID'),(182,36,'FullPath'),(183,36,'isSystem'),(184,36,'LocationID'),(185,36,'ModifiedBy'),(186,36,'ModifiedOn'),(187,36,'Owner'),(188,36,'Name'),(189,36,'Parent'),(190,38,'AccountManagerID'),(191,38,'AccountTeamID'),(192,38,'Created'),(193,38,'Creator'),(194,38,'Dbase'),(195,38,'EntityID'),(196,38,'ExternalID'),(197,38,'List'),(198,38,'Modified'),(199,38,'ModifiedBy'),(200,38,'Name'),(201,38,'Owner'),(202,38,'Source'),(203,39,'Created'),(204,39,'CreatedBy'),(205,39,'Entity'),(206,39,'ExternalID'),(207,39,'FirstName'),(208,39,'IndividualID'),(209,39,'LastName'),(210,39,'MiddleInitial'),(211,39,'Modified'),(212,39,'ModifiedBy'),(213,39,'Owner'),(214,39,'PrimaryContact'),(215,39,'Source'),(216,39,'Title'),(217,62,'roleid'),(218,62,'rolename'),(219,63,'IndividualID'),(220,63,'Name'),(221,63,'Password'),(222,63,'UserID'),(223,64,'ack'),(224,64,'alertid'),(225,64,'displayedtime'),(226,64,'message'),(227,64,'Owner'),(228,65,'IndividualID'),(229,65,'moduleid'),(230,65,'privilegelevel'),(231,66,'IndividualID'),(232,66,'privilegelevel'),(233,66,'RecordID'),(234,66,'referenceid'),(235,67,'fieldid'),(236,67,'IndividualID'),(237,67,'privilegelevel'),(238,67,'referenceid'),(239,68,'moduleid'),(240,68,'name'),(241,68,'parentid'),(242,69,'name'),(243,69,'parentid'),(244,69,'recordid'),(245,70,'fieldid'),(246,70,'name'),(247,70,'tableid'),(248,71,'groupid'),(249,71,'IndividualID'),(250,71,'Owner'),(251,71,'privilegelevel'),(252,71,'RecordID'),(253,72,'fieldid'),(254,72,'groupid'),(255,72,'IndividualID'),(256,72,'Owner'),(257,72,'privilegelevel'),(258,73,'OrderID'),(259,73,'Title'),(260,73,'Description'),(261,73,'Entity'),(262,73,'BillIndividual'),(263,73,'ShipIndividual'),(264,73,'Status'),(265,73,'Terms'),(266,73,'AccountMgr'),(267,73,'Project'),(268,73,'Proposal'),(269,73,'SubTotal'),(270,73,'Tax'),(271,73,'Shipping'),(272,73,'Discount'),(273,73,'Total'),(274,73,'Creator'),(275,73,'Owner'),(276,73,'ModifiedBy'),(277,73,'Created'),(278,73,'Modified'),(279,73,'PONumber'),(280,74,'OrderID'),(281,74,'ItemID'),(282,74,'Quantity'),(283,74,'Price'),(284,74,'SKU'),(285,74,'Description'),(286,75,'InventoryID'),(287,75,'Title'),(288,75,'Description'),(289,75,'Item'),(290,75,'LocationID'),(291,76,'LocationID'),(292,76,'Title'),(293,76,'Parent'),(294,77,'StatusID'),(295,77,'Title'),(296,77,'Description'),(297,78,'ItemID'),(298,78,'Title'),(299,78,'Description'),(300,78,'ListPrice'),(301,78,'Cost'),(302,78,'TaxClass'),(303,78,'Type'),(304,78,'SKU'),(305,78,'Parent'),(306,78,'Manufacturer'),(307,78,'Vendor'),(308,78,'AlertLevel'),(309,79,'TermsID'),(310,79,'Title'),(311,79,'Description'),(312,80,'ItemTypeID'),(313,80,'Title'),(314,80,'Description'),(315,81,'TaxClassID'),(316,81,'Title'),(317,81,'Description'),(318,82,'TaxClassID'),(319,82,'TaxJurisdictionID'),(320,82,'TaxRate'),(321,83,'TaxJurisdictionID'),(322,83,'TaxJurisdictionName'),(323,83,'TaxJurisdictionCode'),(324,84,'InvoiceID'),(325,84,'Title'),(326,84,'Description'),(327,84,'Status'),(328,84,'Terms'),(329,84,'Order'),(330,84,'Owner'),(331,84,'Creator'),(332,84,'ModifiedBy'),(333,84,'Created'),(334,84,'Modified'),(335,85,'PaymentID'),(336,85,'InvoiceID'),(337,85,'Amount'),(338,86,'PaymentID'),(339,86,'Title'),(340,86,'Description'),(341,86,'EntityID'),(342,86,'PaymentMethod'),(343,86,'Amount'),(344,86,'CardType'),(345,86,'CardNumber'),(346,86,'Expiration'),(347,86,'Owner'),(348,86,'Creator'),(349,86,'ModifiedBy'),(350,86,'Created'),(351,86,'Modified'),(352,87,'MethodID'),(353,87,'Title'),(354,88,'PurchaseOrderID'),(355,88,'Title'),(356,88,'Description'),(357,88,'PONumber'),(358,88,'Entity'),(359,88,'SubTotal'),(360,88,'Shipping'),(361,88,'Tax'),(362,88,'Discount'),(363,88,'Total'),(364,88,'ShipIndividual'),(365,88,'ShipAddress'),(366,88,'BillIndividual'),(367,88,'BillAddress'),(368,88,'Modified'),(369,88,'Status'),(370,88,'Owner'),(371,88,'Creator'),(372,88,'ModifiedBy'),(373,88,'Created'),(374,89,'ExpenseID'),(375,89,'Title'),(376,89,'Description'),(377,89,'EntityID'),(378,89,'Status'),(379,89,'Project'),(380,89,'Ticket'),(381,89,'Opportunity'),(382,89,'Owner'),(383,89,'Creator'),(384,89,'ModifiedBy'),(385,89,'Created'),(386,89,'Modified'),(387,90,'OrderID'),(388,90,'ItemID'),(389,90,'Quantity'),(390,90,'price'),(391,90,'SKU'),(392,90,'Description'),(393,91,'GLAAccountsID'),(394,91,'Title'),(395,91,'Description'),(396,91,'Balance'),(397,91,'Parent'),(398,92,'ExpenseID'),(399,92,'ExpenseItemID'),(400,92,'Title'),(401,92,'Description'),(402,92,'Cost'),(403,93,'TimeSlipID'),(404,93,'TimeEntryID'),(405,93,'ProjectID'),(406,93,'ActivityID'),(407,93,'Title'),(408,93,'Description'),(409,93,'Hours'),(410,93,'CreatedBy'),(411,93,'Date'),(412,93,'Start'),(413,93,'End'),(414,94,'TimeEntryID'),(415,94,'TimeSlipID'),(416,94,'ProjectID'),(417,94,'TicketID'),(418,94,'Title'),(419,94,'Description'),(420,94,'Hours'),(421,94,'start'),(422,94,'End'),(423,95,'StatusID'),(424,95,'Title'),(425,96,'OpportunityID'),(426,96,'ActivityID'),(427,96,'Title'),(428,96,'Description'),(429,96,'EntityID'),(430,96,'IndividualID'),(431,96,'TypeID'),(432,96,'Status'),(433,96,'Stage'),(434,96,'ForecastAmmount'),(435,96,'ActualAmmount'),(436,96,'Probablity'),(437,96,'Source'),(438,96,'AccountManager'),(439,96,'AccountTeam'),(440,97,'SalesTypeID'),(441,97,'Name'),(442,98,'SalesStageID'),(443,98,'Name'),(444,99,'SalesStatusID'),(445,99,'Name'),(446,100,'OpportunityID'),(447,100,'RecordTypeID'),(448,100,'RecordID'),(449,101,'ProbablityID'),(450,101,'Title'),(451,101,'Description'),(452,101,'Probablity'),(453,48,'ProjectID'),(454,48,'ProjectTitle'),(455,48,'Description'),(456,48,'Start'),(457,48,'End'),(458,48,'BudgetedHours'),(459,48,'HoursUsed'),(460,48,'Owner'),(461,48,'Creator'),(462,48,'ModifiedBy'),(463,48,'Created'),(464,48,'Modified'),(465,49,'activityid'),(466,49,'ProjectID'),(467,49,'MileStone'),(470,49,'PercentComplete'),(471,49,'Parent'),(472,50,'TaskID'),(473,50,'IndividualID'),(474,51,'ProjectID'),(475,51,'RecordTypeID'),(476,51,'RecordID'),(477,106,'typeid'),(478,106,'name'),(479,107,'activityid'),(480,107,'calltype'),(481,108,'statusid'),(482,108,'name'),(483,109,'priorityid'),(484,109,'name'),(485,109,'priorityorder'),(486,110,'statusid'),(487,110,'name'),(488,111,'noteid'),(489,111,'title'),(490,111,'detail'),(491,111,'creator'),(492,111,'owner'),(493,111,'dateupdated'),(494,111,'datecreated'),(495,111,'updatedby'),(496,112,'noteid'),(497,112,'recordtypeid'),(498,112,'recordid'),(499,113,'proposalid'),(500,113,'title'),(501,113,'Description'),(502,113,'opportunityid'),(503,113,'status'),(504,113,'type'),(505,113,'stage'),(506,113,'estimatedclose'),(507,113,'probability'),(508,113,'amount'),(509,113,'owner'),(510,113,'creator'),(511,113,'modifiedby'),(512,113,'created'),(513,113,'modified'),(514,114,'proposalid'),(515,114,'recordtypeid'),(516,114,'recordid'),(517,115,'proposalid'),(518,115,'itemid'),(519,115,'Description'),(520,115,'Quantity'),(521,115,'price'),(522,115,'name'),(523,40,'ActivityID'),(524,40,'literatureid'),(525,40,'Title'),(526,40,'Description'),(527,40,'requestedby'),(528,40,'deliverymethod'),(529,41,'literatureid'),(530,41,'Title'),(531,41,'fileid'),(532,41,'Description'),(533,42,'deliveryid'),(534,42,'name'),(535,43,'eventid'),(536,43,'title'),(537,43,'detail'),(538,43,'start'),(539,43,'end'),(540,43,'for'),(541,43,'Owner'),(542,43,'creator'),(543,43,'created'),(544,43,'modifiedby'),(545,43,'modified'),(546,44,'eventid'),(547,44,'IndividualID'),(548,45,'promotionid'),(549,45,'title'),(550,45,'Description'),(551,45,'status'),(552,45,'startdate'),(553,45,'enddate'),(554,45,'Owner'),(555,45,'creator'),(556,45,'modifiedby'),(557,45,'created'),(558,45,'modified'),(559,46,'promotionid'),(560,46,'itemid'),(561,46,'Quantity'),(562,46,'rule'),(563,46,'price'),(564,37,'listid'),(565,37,'title'),(566,37,'Description'),(567,37,'numberofrecords'),(568,37,'proposals'),(569,37,'orders'),(570,37,'ordervalue'),(571,37,'status'),(572,37,'owner'),(573,37,'creator'),(574,37,'modifiedby'),(575,37,'created'),(576,37,'modified'),(577,24,'activityid'),(578,24,'resourceid'),(579,25,'activityid'),(580,25,'moderator'),(581,25,'resourceid'),(582,52,'ticketid'),(583,52,'subject'),(584,52,'Description'),(585,52,'entityid'),(586,52,'IndividualID'),(587,52,'assignedto'),(588,52,'status'),(589,52,'priority'),(590,52,'owner'),(591,52,'created'),(592,52,'createdby'),(593,52,'modifiedby'),(594,52,'modified'),(595,54,'statusid'),(596,54,'name'),(597,53,'threadid'),(598,53,'ticketid'),(599,53,'title'),(600,53,'detail'),(601,53,'created'),(602,53,'creator'),(603,53,'type'),(604,55,'ticketid'),(605,55,'recordtypeid'),(606,55,'recordid'),(607,56,'priorityid'),(608,56,'name'),(609,57,'faqid'),(610,57,'Title'),(611,57,'detail'),(612,57,'createdby'),(613,57,'created'),(614,57,'updatedby'),(615,57,'updated'),(616,57,'status'),(617,57,'Owner'),(618,58,'ticketid'),(619,58,'recordtypeid'),(620,58,'recordid'),(621,59,'statusid'),(622,59,'name'),(623,60,'kbid'),(624,60,'Title'),(625,60,'detail'),(626,60,'createdby'),(627,60,'created'),(628,60,'updatedby'),(629,60,'updated'),(630,60,'status'),(631,60,'Owner'),(632,60,'category'),(633,61,'catid'),(634,61,'title'),(635,61,'Description'),(636,61,'Parent'),(637,61,'Owner'),(638,61,'createdby'),(639,61,'modifiedby'),(640,61,'created'),(641,61,'modified'),(642,93,'BreakTime'),(643,116,'StatusID'),(644,116,'Title'),(645,48,'StatusID'),(646,35,'LocationID'),(647,35,'Detail'),(648,48,'Manager'),(649,2,'ExternalID'),(650,2,'Source'),(651,2,'list'),(652,12,'IsPrimary'),(653,10,'syncas'),(654,10,'MOCOrder'),(655,111,'priority'),(656,111,'RelateEntity'),(657,111,'RelateIndividual');

--
-- Table structure for table `fieldauthorisation`
--

DROP TABLE IF EXISTS `fieldauthorisation`;
CREATE TABLE `fieldauthorisation` (
  `individualid` int(11) unsigned default NULL,
  `fieldid` int(11) unsigned default NULL,
  `referenceid` int(11) default NULL,
  `profileid` int(11) NOT NULL default '0',
  `privilegelevel` tinyint(4) NOT NULL default '0',
  KEY `individualid` (`individualid`),
  KEY `fieldid` (`fieldid`),
  KEY `referenceid` (`referenceid`),
  KEY `profileid` (`profileid`)
) TYPE=InnoDB;

--
-- Dumping data for table `fieldauthorisation`
--

INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,3,NULL,1,10),(NULL,1,NULL,1,10),(NULL,5,NULL,1,10),(NULL,6,NULL,1,10),(NULL,29,NULL,1,10),(NULL,30,NULL,1,10),(NULL,31,NULL,1,10),(NULL,32,NULL,1,10),(NULL,33,NULL,1,10),(NULL,34,NULL,1,10),(NULL,35,NULL,1,10),(NULL,36,NULL,1,10),(NULL,37,NULL,1,10),(NULL,38,NULL,1,10),(NULL,39,NULL,1,10),(NULL,42,NULL,1,10),(NULL,43,NULL,1,10),(NULL,44,NULL,1,10),(NULL,45,NULL,1,10),(NULL,46,NULL,1,10),(NULL,47,NULL,1,10),(NULL,48,NULL,1,10),(NULL,49,NULL,1,10),(NULL,50,NULL,1,10),(NULL,51,NULL,1,10),(NULL,52,NULL,1,10),(NULL,53,NULL,1,10),(NULL,54,NULL,1,10),(NULL,55,NULL,1,10),(NULL,56,NULL,1,10),(NULL,57,NULL,1,10),(NULL,58,NULL,1,10),(NULL,59,NULL,1,10),(NULL,60,NULL,1,10),(NULL,61,NULL,1,10),(NULL,62,NULL,1,10),(NULL,63,NULL,1,10),(NULL,95,NULL,1,10),(NULL,99,NULL,1,10),(NULL,100,NULL,1,10),(NULL,101,NULL,1,10),(NULL,22,NULL,1,10),(NULL,106,NULL,1,10),(NULL,107,NULL,1,10),(NULL,108,NULL,1,10),(NULL,109,NULL,1,10),(NULL,110,NULL,1,10),(NULL,111,NULL,1,10),(NULL,113,NULL,1,10),(NULL,114,NULL,1,10),(NULL,115,NULL,1,10),(NULL,116,NULL,1,10),(NULL,117,NULL,1,10),(NULL,118,NULL,1,10),(NULL,119,NULL,1,10),(NULL,7,NULL,1,10),(NULL,8,NULL,1,10),(NULL,9,NULL,1,10),(NULL,10,NULL,1,10),(NULL,11,NULL,1,10),(NULL,12,NULL,1,10),(NULL,13,NULL,1,10),(NULL,14,NULL,1,10),(NULL,15,NULL,1,10),(NULL,120,NULL,1,10),(NULL,121,NULL,1,10),(NULL,122,NULL,1,10),(NULL,123,NULL,1,10),(NULL,124,NULL,1,10),(NULL,127,NULL,1,10),(NULL,128,NULL,1,10),(NULL,129,NULL,1,10),(NULL,133,NULL,1,10),(NULL,139,NULL,1,10),(NULL,140,NULL,1,10),(NULL,141,NULL,1,10),(NULL,142,NULL,1,10),(NULL,143,NULL,1,10),(NULL,144,NULL,1,10),(NULL,145,NULL,1,10),(NULL,146,NULL,1,10),(NULL,147,NULL,1,10),(NULL,148,NULL,1,10),(NULL,162,NULL,1,10),(NULL,163,NULL,1,10),(NULL,164,NULL,1,10),(NULL,165,NULL,1,10),(NULL,167,NULL,1,10),(NULL,168,NULL,1,10),(NULL,169,NULL,1,10),(NULL,170,NULL,1,10),(NULL,171,NULL,1,10),(NULL,172,NULL,1,10),(NULL,173,NULL,1,10),(NULL,174,NULL,1,10),(NULL,4,NULL,1,10),(NULL,16,NULL,1,10),(NULL,17,NULL,1,10),(NULL,18,NULL,1,10),(NULL,19,NULL,1,10),(NULL,20,NULL,1,10),(NULL,21,NULL,1,10),(NULL,187,NULL,1,10),(NULL,188,NULL,1,10),(NULL,193,NULL,1,10),(NULL,192,NULL,1,10),(NULL,191,NULL,1,10),(NULL,203,NULL,1,10),(NULL,204,NULL,1,10),(NULL,205,NULL,1,10),(NULL,206,NULL,1,10),(NULL,207,NULL,1,10),(NULL,208,NULL,1,10),(NULL,209,NULL,1,10),(NULL,210,NULL,1,10),(NULL,211,NULL,1,10),(NULL,212,NULL,1,10),(NULL,217,NULL,1,10),(NULL,218,NULL,1,10),(NULL,219,NULL,1,10),(NULL,220,NULL,1,10),(NULL,221,NULL,1,10),(NULL,222,NULL,1,10),(NULL,223,NULL,1,10),(NULL,224,NULL,1,10),(NULL,225,NULL,1,10),(NULL,226,NULL,1,10),(NULL,227,NULL,1,10),(NULL,228,NULL,1,10),(NULL,229,NULL,1,10),(NULL,230,NULL,1,10),(NULL,231,NULL,1,10),(NULL,232,NULL,1,10),(NULL,233,NULL,1,10),(NULL,234,NULL,1,10),(NULL,235,NULL,1,10),(NULL,236,NULL,1,10),(NULL,237,NULL,1,10),(NULL,238,NULL,1,10),(NULL,239,NULL,1,10),(NULL,240,NULL,1,10),(NULL,241,NULL,1,10),(NULL,242,NULL,1,10),(NULL,243,NULL,1,10),(NULL,244,NULL,1,10),(NULL,245,NULL,1,10),(NULL,246,NULL,1,10),(NULL,247,NULL,1,10),(NULL,248,NULL,1,10),(NULL,249,NULL,1,10),(NULL,250,NULL,1,10),(NULL,254,NULL,1,10),(NULL,255,NULL,1,10),(NULL,256,NULL,1,10),(NULL,257,NULL,1,10),(NULL,258,NULL,1,10),(NULL,259,NULL,1,10),(NULL,260,NULL,1,10),(NULL,261,NULL,1,10),(NULL,262,NULL,1,10),(NULL,263,NULL,1,10),(NULL,264,NULL,1,10),(NULL,265,NULL,1,10),(NULL,266,NULL,1,10),(NULL,267,NULL,1,10),(NULL,268,NULL,1,10),(NULL,269,NULL,1,10),(NULL,270,NULL,1,10),(NULL,271,NULL,1,10),(NULL,272,NULL,1,10),(NULL,273,NULL,1,10),(NULL,274,NULL,1,10),(NULL,275,NULL,1,10),(NULL,276,NULL,1,10),(NULL,277,NULL,1,10),(NULL,278,NULL,1,10),(NULL,279,NULL,1,10),(NULL,280,NULL,1,10),(NULL,281,NULL,1,10),(NULL,282,NULL,1,10),(NULL,285,NULL,1,10),(NULL,286,NULL,1,10),(NULL,287,NULL,1,10),(NULL,288,NULL,1,10),(NULL,289,NULL,1,10),(NULL,290,NULL,1,10),(NULL,291,NULL,1,10),(NULL,292,NULL,1,10),(NULL,293,NULL,1,10),(NULL,294,NULL,1,10),(NULL,295,NULL,1,10),(NULL,296,NULL,1,10),(NULL,297,NULL,1,10),(NULL,298,NULL,1,10),(NULL,299,NULL,1,10),(NULL,307,NULL,1,10),(NULL,308,NULL,1,10),(NULL,309,NULL,1,10),(NULL,310,NULL,1,10),(NULL,311,NULL,1,10),(NULL,312,NULL,1,10),(NULL,313,NULL,1,10),(NULL,314,NULL,1,10),(NULL,315,NULL,1,10),(NULL,316,NULL,1,10),(NULL,317,NULL,1,10),(NULL,318,NULL,1,10),(NULL,319,NULL,1,10),(NULL,320,NULL,1,10),(NULL,321,NULL,1,10),(NULL,126,NULL,1,10),(NULL,137,NULL,1,10),(NULL,138,NULL,1,10),(NULL,322,NULL,1,10),(NULL,323,NULL,1,10),(NULL,105,NULL,1,10),(NULL,149,NULL,1,10),(NULL,150,NULL,1,10),(NULL,151,NULL,1,10),(NULL,152,NULL,1,10),(NULL,153,NULL,1,10),(NULL,154,NULL,1,10),(NULL,156,NULL,1,10),(NULL,157,NULL,1,10),(NULL,159,NULL,1,10),(NULL,160,NULL,1,10),(NULL,161,NULL,1,10),(NULL,324,NULL,1,10);

--
-- Table structure for table `fieldextra`
--

DROP TABLE IF EXISTS `fieldextra`;
CREATE TABLE `fieldextra` (
  `FieldId` int(11) unsigned default NULL,
  `FullName` varchar(55) default NULL,
  `Appearance` enum('TOP','BOTTOM','BOTH') default 'BOTH',
  UNIQUE KEY `FieldId` (`FieldId`),
  CONSTRAINT `0_21468` FOREIGN KEY (`FieldId`) REFERENCES `field` (`fieldid`),
  CONSTRAINT `0_21469` FOREIGN KEY (`FieldId`) REFERENCES `field` (`fieldid`)
) TYPE=InnoDB;

--
-- Dumping data for table `fieldextra`
--

INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (1,'EntityID','BOTH'),(2,'ExternalID','BOTH'),(3,'Name','BOTH'),(4,'Source','BOTH'),(5,'Modified','BOTH'),(6,'Created','BOTH'),(7,'Owner','BOTH'),(8,'ModifiedBy','BOTH'),(9,'Dbase','BOTH'),(10,'List','BOTH'),(11,'Creator','BOTH'),(12,'IndividualID','BOTH'),(13,'EntityID','BOTH'),(14,'First','BOTH'),(15,'MiddleInitial','BOTH'),(16,'Last','BOTH'),(17,'Title','BOTH'),(18,'PrimaryContact','BOTH'),(19,'Owner','BOTH'),(20,'CreatedBy','BOTH'),(21,'ModifiedBy','BOTH'),(22,'Created','BOTH'),(23,'Modified','BOTH'),(24,'GroupID','BOTH'),(25,'Name','BOTH'),(26,'Description','BOTH'),(27,'AddressID','BOTH'),(28,'AddressType','BOTH'),(29,'Stret1','BOTH'),(30,'Street2','BOTH'),(31,'City','BOTH'),(32,'State','BOTH'),(33,'Zip','BOTH'),(34,'Country','BOTH'),(35,'StateID','BOTH'),(36,'Name','BOTH'),(37,'CountryID','BOTH'),(38,'Name','BOTH'),(39,'ContactTypeID','BOTH'),(40,'Name','BOTH'),(41,'TypeID','BOTH'),(42,'Name','BOTH'),(43,'MOCTypeID','BOTH'),(44,'Name','BOTH'),(45,'MOCID','BOTH'),(46,'MOCType','BOTH'),(47,'Content','BOTH'),(48,'Note','BOTH'),(49,'MOCID','BOTH'),(50,'ContactType','BOTH'),(51,'ContactID','BOTH'),(52,'Primary','BOTH'),(53,'Address','BOTH'),(54,'ContactType','BOTH'),(55,'Contact','BOTH'),(56,'AddressType','BOTH'),(57,'GroupID','BOTH'),(58,'ChildID','BOTH'),(59,'DBaseID','BOTH'),(60,'Name','BOTH'),(61,'AccountManager','BOTH'),(62,'AccountTeam','BOTH'),(63,'AccountID','BOTH'),(64,'Body','BOTH'),(65,'CreatedBy','BOTH'),(66,'FromIndividual','BOTH'),(67,'Headers','BOTH'),(68,'Importance','BOTH'),(69,'MailFrom','BOTH'),(70,'MessageDate','BOTH'),(71,'MessageID','BOTH'),(72,'Owner','BOTH'),(73,'Prority','BOTH'),(74,'Reply To','BOTH'),(75,'size','BOTH'),(76,'subject','BOTH'),(77,'Parent','BOTH'),(78,'FolderID','BOTH'),(79,'AccountID','BOTH'),(80,'Name','BOTH'),(81,'Ftype','BOTH'),(82,'MessageID','BOTH'),(83,'EmailFolder','BOTH'),(84,'ReadStatus','BOTH'),(85,'AccountID','BOTH'),(86,'Address','BOTH'),(87,'Default','BOTH'),(88,'LeaveOnServer','BOTH'),(89,'Login','BOTH'),(90,'mailserver','BOTH'),(91,'Name','BOTH'),(92,'Owner','BOTH'),(93,'Password','BOTH'),(94,'Reply To','BOTH'),(95,'ServerType','BOTH'),(96,'Signature','BOTH'),(97,'SMTPServer','BOTH'),(98,'Address','BOTH'),(99,'MessageID','BOTH'),(100,'RecipientID','BOTH'),(101,'RecipientlsGroup','BOTH'),(102,'RecipientType','BOTH'),(103,'AttachmentID','BOTH'),(104,'FileID','BOTH'),(105,'FileName','BOTH'),(106,'MessageID','BOTH'),(107,'ActivityID','BOTH'),(108,'AllDay','BOTH'),(109,'CompletedDate','BOTH'),(110,'Created','BOTH'),(111,'Creator','BOTH'),(112,'Details','BOTH'),(113,'DueDate','BOTH'),(114,'End','BOTH'),(115,'Location','BOTH'),(116,'Modified','BOTH'),(117,'ModifiedBy','BOTH'),(118,'Owner','BOTH'),(119,'Priority','BOTH'),(120,'Start','BOTH'),(121,'Status','BOTH'),(122,'Title','BOTH'),(123,'Type','BOTH'),(124,'visibility','BOTH'),(125,'ActivityID','BOTH'),(126,'IndividualID','BOTH'),(127,'Status','BOTH'),(128,'Type','BOTH'),(129,'CallTypeID','BOTH'),(130,'Name','BOTH'),(131,'ActivityResourceID','BOTH'),(132,'Detail','BOTH'),(133,'Name','BOTH'),(134,'ActivityID','BOTH'),(135,'ResourceID','BOTH'),(136,'ActivityID','BOTH'),(137,'RecordID','BOTH'),(138,'RecordTypeID','BOTH'),(139,'ActivityID','BOTH'),(140,'Every','BOTH'),(141,'RecurrenceID','BOTH'),(142,'RecurrOn','BOTH'),(143,'startdate','BOTH'),(144,'TimePeriod','BOTH'),(145,'Until','BOTH'),(146,'Exception','BOTH'),(147,'RecurrenceID','BOTH'),(148,'ActionID','BOTH'),(149,'ActivityID','BOTH'),(150,'recipient','BOTH'),(151,'ActionID','BOTH'),(152,'ActionInterval','BOTH'),(153,'Action Time','BOTH'),(154,'Message','BOTH'),(155,'Type','BOTH'),(156,'Repeat','BOTH'),(157,'Author','BOTH'),(158,'Created','BOTH'),(159,'Creator','BOTH'),(160,'CustomerView','BOTH'),(161,'Description','BOTH'),(162,'FileID','BOTH'),(163,'FileSize','BOTH'),(164,'IsTemporary','BOTH'),(165,'Name','BOTH'),(166,'Owner','BOTH'),(167,'RelateEntity','BOTH'),(168,'RelateIndividual','BOTH'),(169,'Status','BOTH'),(170,'Title','BOTH'),(171,'Updated','BOTH'),(172,'UpdatedBy','BOTH'),(173,'Version','BOTH'),(174,'visibility','BOTH'),(175,'fileid','BOTH'),(176,'folderid','BOTH'),(177,'referencetype','BOTH'),(178,'CreatedBy','BOTH'),(179,'CreatedOn','BOTH'),(180,'Description','BOTH'),(181,'FolderID','BOTH'),(182,'FullPath','BOTH'),(183,'isSystem','BOTH'),(184,'LocationID','BOTH'),(185,'ModifiedBy','BOTH'),(186,'ModifiedOn','BOTH'),(187,'Owner','BOTH'),(188,'Name','BOTH'),(189,'Parent','BOTH'),(190,'AccountManagerID','BOTH'),(191,'AccountTeamID','BOTH'),(192,'Created','BOTH'),(193,'Creator','BOTH'),(194,'Dbase','BOTH'),(195,'EntityID','BOTH'),(196,'ExternalID','BOTH'),(197,'List','BOTH'),(198,'Modified','BOTH'),(199,'ModifiedBy','BOTH'),(200,'Name','BOTH'),(201,'Owner','BOTH'),(202,'Source','BOTH'),(203,'Created','BOTH'),(204,'CreatedBy','BOTH'),(205,'Entity','BOTH'),(206,'ExternalID','BOTH'),(207,'FirstName','BOTH'),(208,'IndividualID','BOTH'),(209,'LastName','BOTH'),(210,'MiddleInitial','BOTH'),(211,'Modified','BOTH'),(212,'ModifiedBy','BOTH'),(213,'Owner','BOTH'),(214,'PrimaryContact','BOTH'),(215,'Source','BOTH'),(216,'Title','BOTH'),(217,'roleid','BOTH'),(218,'rolename','BOTH'),(219,'IndividualID','BOTH'),(220,'Name','BOTH'),(221,'Password','BOTH'),(222,'UserID','BOTH'),(223,'ack','BOTH'),(224,'alertid','BOTH'),(225,'displayedtime','BOTH'),(226,'message','BOTH'),(227,'Owner','BOTH'),(228,'IndividualID','BOTH'),(229,'moduleid','BOTH'),(230,'privilegelevel','BOTH'),(231,'IndividualID','BOTH'),(232,'privilegelevel','BOTH'),(233,'RecordID','BOTH'),(234,'referenceid','BOTH'),(235,'fieldid','BOTH'),(236,'IndividualID','BOTH'),(237,'privilegelevel','BOTH'),(238,'referenceid','BOTH'),(239,'moduleid','BOTH'),(240,'name','BOTH'),(241,'parentid','BOTH'),(242,'name','BOTH'),(243,'parentid','BOTH'),(244,'recordid','BOTH'),(245,'fieldid','BOTH'),(246,'name','BOTH'),(247,'tableid','BOTH'),(248,'groupid','BOTH'),(249,'IndividualID','BOTH'),(250,'Owner','BOTH'),(251,'privilegelevel','BOTH'),(252,'RecordID','BOTH'),(253,'fieldid','BOTH'),(254,'groupid','BOTH'),(255,'IndividualID','BOTH'),(256,'Owner','BOTH'),(257,'privilegelevel','BOTH'),(258,'OrderID','BOTH'),(259,'Title','BOTH'),(260,'Description','BOTH'),(261,'Entity','BOTH'),(262,'BillIndividual','BOTH'),(263,'ShipIndividual','BOTH'),(264,'Status','BOTH'),(265,'Terms','BOTH'),(266,'AccountMgr','BOTH'),(267,'Project','BOTH'),(268,'Proposal','BOTH'),(269,'SubTotal','BOTH'),(270,'Tax','BOTH'),(271,'Shipping','BOTH'),(272,'Discount','BOTH'),(273,'Total','BOTH'),(274,'Creator','BOTH'),(275,'Owner','BOTH'),(276,'ModifiedBy','BOTH'),(277,'Created','BOTH'),(278,'Modified','BOTH'),(279,'PONumber','BOTH'),(280,'OrderID','BOTH'),(281,'ItemID','BOTH'),(282,'Quantity','BOTH'),(283,'Price','BOTH'),(284,'SKU','BOTH'),(285,'Description','BOTH'),(286,'InventoryID','BOTH'),(287,'Title','BOTH'),(288,'Description','BOTH'),(289,'Item','BOTH'),(290,'LocationID','BOTH'),(291,'LocationID','BOTH'),(292,'Title','BOTH'),(293,'Parent','BOTH'),(294,'StatusID','BOTH'),(295,'Title','BOTH'),(296,'Description','BOTH'),(297,'ItemID','BOTH'),(298,'Title','BOTH'),(299,'Description','BOTH'),(300,'ListPrice','BOTH'),(301,'Cost','BOTH'),(302,'TaxClass','BOTH'),(303,'Type','BOTH'),(304,'SKU','BOTH'),(305,'Parent','BOTH'),(306,'Manufacturer','BOTH'),(307,'Vendor','BOTH'),(308,'AlertLevel','BOTH'),(309,'TermsID','BOTH'),(310,'Title','BOTH'),(311,'Description','BOTH'),(312,'ItemTypeID','BOTH'),(313,'Title','BOTH'),(314,'Description','BOTH'),(315,'TaxClassID','BOTH'),(316,'Title','BOTH'),(317,'Description','BOTH'),(318,'TaxClassID','BOTH'),(319,'TaxJurisdictionID','BOTH'),(320,'TaxRate','BOTH'),(321,'TaxJurisdictionID','BOTH'),(322,'TaxJurisdictionName','BOTH'),(323,'TaxJurisdictionCode','BOTH'),(324,'InvoiceID','BOTH'),(325,'Title','BOTH'),(326,'Description','BOTH'),(327,'Status','BOTH'),(328,'Terms','BOTH'),(329,'Order','BOTH'),(330,'Owner','BOTH'),(331,'Creator','BOTH'),(332,'ModifiedBy','BOTH'),(333,'Created','BOTH'),(334,'Modified','BOTH'),(335,'PaymentID','BOTH'),(336,'InvoiceID','BOTH'),(337,'Amount','BOTH'),(338,'PaymentID','BOTH'),(339,'Title','BOTH'),(340,'Description','BOTH'),(341,'EntityID','BOTH'),(342,'PaymentMethod','BOTH'),(343,'Amount','BOTH'),(344,'CardType','BOTH'),(345,'CardNumber','BOTH'),(346,'Expiration','BOTH'),(347,'Owner','BOTH'),(348,'Creator','BOTH'),(349,'ModifiedBy','BOTH'),(350,'Created','BOTH'),(351,'Modified','BOTH'),(352,'MethodID','BOTH'),(353,'Title','BOTH'),(354,'PurchaseOrderID','BOTH'),(355,'Title','BOTH'),(356,'Description','BOTH'),(357,'PONumber','BOTH'),(358,'Entity','BOTH'),(359,'SubTotal','BOTH'),(360,'Shipping','BOTH'),(361,'Tax','BOTH'),(362,'Discount','BOTH'),(363,'Total','BOTH'),(364,'ShipIndividual','BOTH'),(365,'ShipAddress','BOTH'),(366,'BillIndividual','BOTH'),(367,'BillAddress','BOTH'),(368,'Modified','BOTH'),(369,'Status','BOTH'),(370,'Owner','BOTH'),(371,'Creator','BOTH'),(372,'ModifiedBy','BOTH'),(373,'Created','BOTH'),(374,'ExpenseID','BOTH'),(375,'Title','BOTH'),(376,'Description','BOTH'),(377,'EntityID','BOTH'),(378,'Status','BOTH'),(379,'Project','BOTH'),(380,'Ticket','BOTH'),(381,'Opportunity','BOTH'),(382,'Owner','BOTH'),(383,'Creator','BOTH'),(384,'ModifiedBy','BOTH'),(385,'Created','BOTH'),(386,'Modified','BOTH'),(387,'OrderID','BOTH'),(388,'ItemID','BOTH'),(389,'Quantity','BOTH'),(390,'price','BOTH'),(391,'SKU','BOTH'),(392,'Description','BOTH'),(393,'GLAAccountsID','BOTH'),(394,'Title','BOTH'),(395,'Description','BOTH'),(396,'Balance','BOTH'),(397,'Parent','BOTH'),(398,'ExpenseID','BOTH'),(399,'ExpenseItemID','BOTH'),(400,'Title','BOTH'),(401,'Description','BOTH'),(402,'Cost','BOTH'),(403,'TimeSlipID','BOTH'),(404,'TimeEntryID','BOTH'),(405,'ProjectID','BOTH'),(406,'ActivityID','BOTH'),(407,'Title','BOTH'),(408,'Description','BOTH'),(409,'Hours','BOTH'),(410,'CreatedBy','BOTH'),(411,'Date','BOTH'),(412,'Start','BOTH'),(413,'End','BOTH'),(414,'TimeEntryID','BOTH'),(415,'TimeSlipID','BOTH'),(416,'ProjectID','BOTH'),(417,'TicketID','BOTH'),(418,'Title','BOTH'),(419,'Description','BOTH'),(420,'Hours','BOTH'),(421,'start','BOTH'),(422,'End','BOTH'),(423,'StatusID','BOTH'),(424,'Title','BOTH'),(425,'OpportunityID','BOTH'),(426,'ActivityID','BOTH'),(427,'Title','BOTH'),(428,'Description','BOTH'),(429,'EntityID','BOTH'),(430,'IndividualID','BOTH'),(431,'TypeID','BOTH'),(432,'Status','BOTH'),(433,'Stage','BOTH'),(434,'ForecastAmmount','BOTH'),(435,'ActualAmmount','BOTH'),(436,'Probablity','BOTH'),(437,'Source','BOTH'),(438,'AccountManager','BOTH'),(439,'AccountTeam','BOTH'),(440,'SalesTypeID','BOTH'),(441,'Name','BOTH'),(442,'SalesStageID','BOTH'),(443,'Name','BOTH'),(444,'SalesStatusID','BOTH'),(445,'Name','BOTH'),(446,'OpportunityID','BOTH'),(447,'RecordTypeID','BOTH'),(448,'RecordID','BOTH'),(449,'ProbablityID','BOTH'),(450,'Title','BOTH'),(451,'Description','BOTH'),(452,'Probablity','BOTH'),(453,'ProjectID','BOTH'),(454,'ProjectTitle','BOTH'),(455,'Description','BOTH'),(456,'Start','BOTH'),(457,'End','BOTH'),(458,'BudgetedHours','BOTH'),(459,'HoursUsed','BOTH'),(460,'Owner','BOTH'),(461,'CreatedBy','BOTH'),(462,'ModifiedBy','BOTH'),(463,'Created','BOTH'),(464,'Modified','BOTH'),(465,'activityid','BOTH'),(466,'ProjectID','BOTH'),(467,'MileStone','BOTH'),(470,'PercentComplete','BOTH'),(471,'Parent','BOTH'),(472,'TaskID','BOTH'),(473,'IndividualID','BOTH'),(474,'ProjectID','BOTH'),(475,'RecordTypeID','BOTH'),(476,'RecordID','BOTH'),(477,'typeid','BOTH'),(478,'name','BOTH'),(479,'activityid','BOTH'),(480,'calltype','BOTH'),(481,'statusid','BOTH'),(482,'name','BOTH'),(483,'priorityid','BOTH'),(484,'name','BOTH'),(485,'priorityorder','BOTH'),(486,'statusid','BOTH'),(487,'name','BOTH'),(488,'noteid','BOTH'),(489,'title','BOTH'),(490,'detail','BOTH'),(491,'creator','BOTH'),(492,'owner','BOTH'),(493,'dateupdated','BOTH'),(494,'datecreated','BOTH'),(495,'updatedby','BOTH'),(496,'noteid','BOTH'),(497,'recordtypeid','BOTH'),(498,'recordid','BOTH'),(499,'proposalid','BOTH'),(500,'title','BOTH'),(501,'Description','BOTH'),(502,'opportunityid','BOTH'),(503,'status','BOTH'),(504,'type','BOTH'),(505,'stage','BOTH'),(506,'estimatedclose','BOTH'),(507,'probability','BOTH'),(508,'amount','BOTH'),(509,'owner','BOTH'),(510,'creator','BOTH'),(511,'modifiedby','BOTH'),(512,'created','BOTH'),(513,'modified','BOTH'),(514,'proposalid','BOTH'),(515,'recordtypeid','BOTH'),(516,'recordid','BOTH'),(517,'proposalid','BOTH'),(518,'itemid','BOTH'),(519,'Description','BOTH'),(520,'Quantity','BOTH'),(521,'price','BOTH'),(522,'name','BOTH'),(523,'ActivityID','BOTH'),(524,'literatureid','BOTH'),(525,'Title','BOTH'),(526,'Description','BOTH'),(527,'requestedby','BOTH'),(528,'deliverymethod','BOTH'),(529,'literatureid','BOTH'),(530,'Title','BOTH'),(531,'fileid','BOTH'),(532,'Description','BOTH'),(533,'deliveryid','BOTH'),(534,'name','BOTH'),(535,'eventid','BOTH'),(536,'title','BOTH'),(537,'detail','BOTH'),(538,'start','BOTH'),(539,'end','BOTH'),(540,'for','BOTH'),(541,'Owner','BOTH'),(542,'creator','BOTH'),(543,'created','BOTH'),(544,'modifiedby','BOTH'),(545,'modified','BOTH'),(546,'eventid','BOTH'),(547,'IndividualID','BOTH'),(548,'promotionid','BOTH'),(549,'title','BOTH'),(550,'Description','BOTH'),(551,'status','BOTH'),(552,'startdate','BOTH'),(553,'enddate','BOTH'),(554,'Owner','BOTH'),(555,'creator','BOTH'),(556,'modifiedby','BOTH'),(557,'created','BOTH'),(558,'modified','BOTH'),(559,'promotionid','BOTH'),(560,'itemid','BOTH'),(561,'Quantity','BOTH'),(562,'rule','BOTH'),(563,'price','BOTH'),(564,'listid','BOTH'),(565,'title','BOTH'),(566,'Description','BOTH'),(567,'numberofrecords','BOTH'),(568,'proposals','BOTH'),(569,'orders','BOTH'),(570,'ordervalue','BOTH'),(571,'status','BOTH'),(572,'owner','BOTH'),(573,'creator','BOTH'),(574,'modifiedby','BOTH'),(575,'created','BOTH'),(576,'modified','BOTH'),(577,'activityid','BOTH'),(578,'resourceid','BOTH'),(579,'activityid','BOTH'),(580,'moderator','BOTH'),(581,'resourceid','BOTH'),(582,'ticketid','BOTH'),(583,'subject','BOTH'),(584,'Description','BOTH'),(585,'entityid','BOTH'),(586,'IndividualID','BOTH'),(587,'assignedto','BOTH'),(588,'status','BOTH'),(589,'priority','BOTH'),(590,'owner','BOTH'),(591,'created','BOTH'),(592,'createdby','BOTH'),(593,'modifiedby','BOTH'),(594,'modified','BOTH'),(595,'statusid','BOTH'),(596,'name','BOTH'),(597,'threadid','BOTH'),(598,'ticketid','BOTH'),(599,'title','BOTH'),(600,'detail','BOTH'),(601,'created','BOTH'),(602,'creator','BOTH'),(603,'type','BOTH'),(604,'ticketid','BOTH'),(605,'recordtypeid','BOTH'),(606,'recordid','BOTH'),(607,'priorityid','BOTH'),(608,'name','BOTH'),(609,'faqid','BOTH'),(610,'Title','BOTH'),(611,'detail','BOTH'),(612,'createdby','BOTH'),(613,'created','BOTH'),(614,'updatedby','BOTH'),(615,'updated','BOTH'),(616,'status','BOTH'),(617,'Owner','BOTH'),(618,'ticketid','BOTH'),(619,'recordtypeid','BOTH'),(620,'recordid','BOTH'),(621,'statusid','BOTH'),(622,'name','BOTH'),(623,'kbid','BOTH'),(624,'Title','BOTH'),(625,'detail','BOTH'),(626,'createdby','BOTH'),(627,'created','BOTH'),(628,'updatedby','BOTH'),(629,'updated','BOTH'),(630,'status','BOTH'),(631,'Owner','BOTH'),(632,'category','BOTH'),(633,'catid','BOTH'),(634,'title','BOTH'),(635,'Description','BOTH'),(636,'Parent','BOTH'),(637,'Owner','BOTH'),(638,'createdby','BOTH'),(639,'modifiedby','BOTH'),(640,'created','BOTH'),(641,'modified','BOTH'),(642,'BreakTime','BOTH'),(643,'StatusID','BOTH'),(644,'Title','BOTH'),(645,'StatusID','BOTH'),(646,'LocationID','BOTH'),(647,'Detail','BOTH'),(648,'Manager','BOTH');

--
-- Table structure for table `finalmappings`
--

DROP TABLE IF EXISTS `finalmappings`;
CREATE TABLE `finalmappings` (
  `mapid` int(11) NOT NULL auto_increment,
  `module` varchar(50) NOT NULL default '',
  `table` varchar(50) NOT NULL default '',
  `column` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`mapid`)
) TYPE=InnoDB;

--
-- Dumping data for table `finalmappings`
--

INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (1,'entity','individual','firstname'),(2,'entity','individual','middlename'),(3,'entity','individual','lastname'),(4,'entity','individual','title'),(5,'entity','individual','source'),(6,'entity','state','Name'),(7,'entity','country','Name'),(8,'entity','emailmessage','body'),(9,'entity','grouptbl','Name'),(10,'entity','address','street1'),(11,'entity','address','street2'),(12,'entity','address','city'),(13,'entity','address','zip'),(14,'individual','entity','Name'),(15,'individual','dbase','Name'),(16,'individual','address','street1'),(17,'individual','address','street2'),(18,'individual','address','city'),(19,'individual','address','zip'),(20,'individual','state','Name'),(21,'individual','country','Name'),(22,'individual','emailmessage','body'),(23,'individual','grouptbl','Name'),(24,'grouptbl','entity','Name'),(25,'grouptbl','individual','firstname'),(26,'grouptbl','individual','middlename'),(27,'grouptbl','individual','lastname'),(28,'grouptbl','individual','title'),(29,'grouptbl','address','street1'),(30,'grouptbl','address','street2'),(31,'grouptbl','address','city'),(32,'grouptbl','address','zip'),(33,'grouptbl','state','Name'),(34,'grouptbl','country','Name'),(35,'emailmessage','emailmessage','From'),(36,'emailmessage','emailmessage','ReplyTo'),(37,'emailmessage','emailmessage','Subject'),(38,'emailmessage','emailmessage','Body'),(39,'emailmessage','emailmessage','Headres'),(40,'emailmessage','emailfolder','Name'),(41,'emailmessage','emailaccount','Name'),(42,'emailmessage','emailaccount','Login'),(43,'emailmessage','emailaccount','SMTPServer'),(44,'emailmessage','emailaccount','MailServer'),(45,'emailmessage','emailaccount','Address'),(46,'emailmessage','emailaccount','ReplyTo'),(47,'emailmessage','emailaccount','Signature'),(48,'emailmessage','emailrecipient','Address'),(49,'emailmessage','individual','firstname'),(50,'emailmessage','individual','middlename'),(51,'emailmessage','individual','lastname'),(52,'emailmessage','individual','title'),(53,'emailmessage','address','street1'),(54,'emailmessage','address','street2'),(55,'emailmessage','address','city'),(56,'emailmessage','address','zip'),(57,'emailmessage','state','Name'),(58,'emailmessage','country','Name'),(59,'emailmessage','grouptbl','Name'),(60,'emailmessage','file','Title'),(61,'emailmessage','file','Description'),(62,'emailmessage','file','Name'),(63,'emailmessage','filelocation','Detail'),(64,'emailmessage','folder','Name'),(65,'activity','activity','title'),(66,'activity','calltype','name'),(67,'activity','activityresources','name'),(68,'activity','action','message'),(69,'activity','activitytype','name'),(70,'activity','attendeestatus','name'),(71,'activity','activitypriority','name'),(72,'activity','activitystatus','name'),(73,'activity','individual','firstname'),(74,'activity','individual','middlename'),(75,'activity','individual','lastname'),(76,'activity','individual','title'),(77,'activity','individual','source'),(78,'activity','literaturerequest','title'),(79,'activity','literaturerequest','description'),(80,'activity','opportunity','title'),(81,'activity','opportunity','description'),(82,'activity','opportunity','source'),(83,'activity','entity','Name'),(84,'activity','salestype','Name'),(85,'activity','salesstage','Name'),(86,'activity','salesstatus','Name'),(87,'activity','salesprobablity','description'),(88,'activity','salesprobablity','title'),(89,'activity','record','name'),(90,'activity','project','projecttitle'),(91,'activity','project','description'),(92,'activity','address','street1'),(93,'activity','address','street2'),(94,'activity','address','city'),(95,'activity','address','zip'),(96,'activity','address','website'),(97,'activity','country','Name'),(98,'activity','state','Name'),(99,'note','note','title'),(100,'note','note','detail'),(101,'note','entity','Name'),(102,'note','individual','firstname'),(103,'note','individual','middlename'),(104,'note','individual','lastname'),(105,'note','individual','title'),(106,'note','address','street1'),(107,'note','address','street2'),(108,'note','address','city'),(109,'note','address','zip'),(110,'note','state','Name'),(111,'note','country','Name'),(112,'note','record','name'),(113,'grouptbl','grouptbl','description'),(115,'cvfile','cvfile','Title'),(116,'cvfile','cvfile','Description'),(117,'cvfile','cvfile','Name'),(133,'ticket','ticket','Subject'),(134,'faq','faq','Title'),(135,'knowledgebase','knowledgebase','kbid'),(136,'inventory','inventory','Title'),(137,'glaccount','glaccount','Title'),(138,'project','project','projecttitle'),(139,'project','entity','Name'),(140,'project','individual','firstname'),(141,'project','individual','lastname'),(142,'project','task','title'),(143,'project','timeslip','description'),(145,'project','activity','Title'),(146,'task','activity','Title'),(147,'task','activity','Status'),(148,'task','activity','Priority'),(149,'task','project','ProjectTitle'),(150,'timeslip','project','ProjectTitle'),(151,'timeslip','task','Title'),(152,'knowledgebase','knowledgebase','Title'),(153,'project','projectstatus','Title'),(154,'opportunity','opportunity','Title'),(155,'proposal','proposal','Title'),(156,'proposal','proposal','Description'),(157,'opportunity','opportunity','Description'),(158,'marketinglist','marketinglist','Title'),(159,'marketinglist','marketinglist','Description'),(160,'promotion','promotion','Title'),(161,'promotion','promotion','Description'),(162,'event','event','Title'),(163,'event','event','Detail'),(164,'cvorder','cvorder','Title'),(165,'cvorder','cvorder','Description'),(166,'invoice','invoice','Title'),(167,'invoice','invoice','Description'),(168,'payment','payment','Title'),(169,'payment','payment','Description'),(170,'expense','expense','Title'),(171,'expense','expense','Description'),(172,'purchaseorder','purchaseorder','Title'),(173,'purchaseorder','purchaseorder','Description'),(174,'item','item','Title'),(175,'item','item','Description'),(176,'glaccount','glaccount','Title'),(177,'glaccount','glaccount','Description');

--
-- Table structure for table `folderlist`
--

DROP TABLE IF EXISTS `folderlist`;
CREATE TABLE `folderlist` (
  `AccountID` int(11) unsigned NOT NULL default '0',
  `AccountName` varchar(25) default NULL,
  `Address` varchar(25) default NULL,
  `Parent` int(11) unsigned default NULL,
  `FolderID` int(11) default NULL,
  `FolderName` varchar(25) default NULL,
  `Ftype` varchar(25) default NULL,
  `HasChild` int(1) default '0',
  KEY `AccountID` (`AccountID`),
  KEY `Parent` (`Parent`),
  KEY `FolderID` (`FolderID`)
) TYPE=InnoDB;

--
-- Dumping data for table `folderlist`
--


--
-- Table structure for table `glaccount`
--

DROP TABLE IF EXISTS `glaccount`;
CREATE TABLE `glaccount` (
  `GLAccountsID` int(11) unsigned NOT NULL auto_increment,
  `externalID` varchar(25) default '[NULL]',
  `Title` varchar(100) default '',
  `Description` text,
  `Balance` float NOT NULL default '0',
  `GLAccountType` varchar(100) NOT NULL default '',
  `Parent` int(11) NOT NULL default '0',
  PRIMARY KEY  (`GLAccountsID`),
  KEY `Parent` (`Parent`)
) TYPE=InnoDB;

--
-- Dumping data for table `glaccount`
--

INSERT INTO `glaccount` (`GLAccountsID`, `externalID`, `Title`, `Description`, `Balance`, `GLAccountType`, `Parent`) VALUES (1,'','Default Account','Default account',0,'A',0);

--
-- Table structure for table `glaccounttype`
--

DROP TABLE IF EXISTS `glaccounttype`;
CREATE TABLE `glaccounttype` (
  `accounttype` varchar(30) NOT NULL default '',
  PRIMARY KEY  (`accounttype`)
) TYPE=InnoDB;

--
-- Dumping data for table `glaccounttype`
--


--
-- Table structure for table `globalreplacerelate`
--

DROP TABLE IF EXISTS `globalreplacerelate`;
CREATE TABLE `globalreplacerelate` (
  `RelateTableID` int(11) unsigned NOT NULL default '0',
  `SearchTableID` int(11) unsigned NOT NULL default '0',
  `IsRelateTable` enum('N','Y') NOT NULL default 'Y',
  UNIQUE KEY `SearchRelationshipID` (`RelateTableID`,`SearchTableID`),
  KEY `RelateTableID` (`RelateTableID`),
  KEY `SearchTableID` (`SearchTableID`),
  CONSTRAINT `0_21721` FOREIGN KEY (`RelateTableID`) REFERENCES `searchtable` (`SearchTableID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `0_21722` FOREIGN KEY (`SearchTableID`) REFERENCES `searchtable` (`SearchTableID`) ON DELETE CASCADE ON UPDATE CASCADE
) TYPE=InnoDB;

--
-- Dumping data for table `globalreplacerelate`
--

INSERT INTO `globalreplacerelate` (`RelateTableID`, `SearchTableID`, `IsRelateTable`) VALUES (5,1,'N'),(5,2,'N');

--
-- Table structure for table `gobalreplacevalue`
--

DROP TABLE IF EXISTS `gobalreplacevalue`;
CREATE TABLE `gobalreplacevalue` (
  `tableID` int(11) unsigned NOT NULL default '0',
  `fieldID` int(11) unsigned NOT NULL default '0',
  `fieldName` varchar(255) default NULL,
  `tableName` varchar(255) default NULL,
  UNIQUE KEY `replaceValueID` (`tableID`,`fieldID`)
) TYPE=InnoDB;

--
-- Dumping data for table `gobalreplacevalue`
--

INSERT INTO `gobalreplacevalue` (`tableID`, `fieldID`, `fieldName`, `tableName`) VALUES (1,4,' ListID as ValueID , Title as Value','marketinglist'),(2,18,' ListID as ValueID , Title as Value','marketinglist');

--
-- Table structure for table `grouptbl`
--

DROP TABLE IF EXISTS `grouptbl`;
CREATE TABLE `grouptbl` (
  `owner` int(10) unsigned NOT NULL default '0',
  `createDate` datetime NOT NULL default '0000-00-00 00:00:00',
  `modifyDate` datetime NOT NULL default '0000-00-00 00:00:00',
  `GroupID` int(10) unsigned NOT NULL auto_increment,
  `Name` varchar(25) default NULL,
  `Description` text,
  PRIMARY KEY  (`GroupID`),
  UNIQUE KEY `GroupID` (`GroupID`),
  KEY `owner` (`owner`)
) TYPE=InnoDB;

--
-- Dumping data for table `grouptbl`
--


--
-- Table structure for table `history`
--

DROP TABLE IF EXISTS `history`;
CREATE TABLE `history` (
  `date` timestamp(14) NOT NULL,
  `recordName` varchar(255) NOT NULL default '',
  `recordtypeid` int(11) unsigned NOT NULL default '0',
  `recordid` int(11) unsigned NOT NULL default '0',
  `operation` int(11) unsigned NOT NULL default '0',
  `individualid` int(11) unsigned NOT NULL default '0',
  `referenceactivityid` int(11) unsigned NOT NULL default '0',
  `historyid` int(10) unsigned NOT NULL auto_increment,
  PRIMARY KEY  (`historyid`),
  KEY `recordtypeid` (`recordtypeid`),
  KEY `recordid` (`recordid`),
  KEY `operation` (`operation`),
  KEY `individualid` (`individualid`),
  KEY `referenceactivityid` (`referenceactivityid`)
) TYPE=InnoDB;

--
-- Dumping data for table `history`
--

INSERT INTO `history` (`date`, `recordName`, `recordtypeid`, `recordid`, `operation`, `individualid`, `referenceactivityid`, `historyid`) VALUES (20041116170153,'CentraView, LLC',14,2,2,1,0,1);

--
-- Table structure for table `historytype`
--

DROP TABLE IF EXISTS `historytype`;
CREATE TABLE `historytype` (
  `historytypeid` int(10) unsigned NOT NULL auto_increment,
  `historytype` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`historytypeid`)
) TYPE=InnoDB;

--
-- Dumping data for table `historytype`
--

INSERT INTO `historytype` (`historytypeid`, `historytype`) VALUES (1,'Deleted'),(2,'Created'),(3,'Restored'),(4,'Updated'),(5,'CompleteActivity');

--
-- Table structure for table `individual`
--

DROP TABLE IF EXISTS `individual`;
CREATE TABLE `individual` (
  `IndividualID` int(10) unsigned NOT NULL auto_increment,
  `Entity` int(10) unsigned NOT NULL default '0',
  `FirstName` varchar(25) default NULL,
  `MiddleInitial` char(1) default NULL,
  `LastName` varchar(25) default NULL,
  `Title` varchar(255) default NULL,
  `PrimaryContact` enum('YES','NO') default 'NO',
  `Owner` int(10) unsigned default NULL,
  `CreatedBy` int(10) unsigned default NULL,
  `ModifiedBy` int(10) unsigned default NULL,
  `Created` datetime default NULL,
  `Modified` timestamp(14) NOT NULL,
  `ExternalID` varchar(25) default NULL,
  `Source` int(10) unsigned NOT NULL default '0',
  `list` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`IndividualID`),
  KEY `Entity` (`Entity`),
  KEY `Owner` (`Owner`),
  KEY `CreatedBy` (`CreatedBy`),
  KEY `Modified` (`Modified`),
  KEY `ModifiedBy` (`ModifiedBy`),
  KEY `Source` (`Source`),
  KEY `list` (`list`)
) TYPE=InnoDB;

--
-- Dumping data for table `individual`
--

INSERT INTO `individual` (`IndividualID`, `Entity`, `FirstName`, `MiddleInitial`, `LastName`, `Title`, `PrimaryContact`, `Owner`, `CreatedBy`, `ModifiedBy`, `Created`, `Modified`, `ExternalID`, `Source`, `list`) VALUES (1,1,'Administrative','','User','','YES',1,1,1,'2004-03-08 09:21:19',20040308092119,'',0,1);

--
-- Table structure for table `inventory`
--

DROP TABLE IF EXISTS `inventory`;
CREATE TABLE `inventory` (
  `inventoryid` int(10) unsigned NOT NULL auto_increment,
  `title` varchar(25) NOT NULL default '',
  `description` text NOT NULL,
  `qty` int(10) unsigned NOT NULL default '0',
  `locationid` int(10) unsigned NOT NULL default '0',
  `item` int(10) unsigned NOT NULL default '0',
  `statusid` int(11) unsigned NOT NULL default '0',
  `customerid` int(11) unsigned NOT NULL default '0',
  `created` date NOT NULL default '0000-00-00',
  `modified` timestamp(14) NOT NULL,
  `vendorid` int(10) unsigned NOT NULL default '0',
  `LineID` int(11) NOT NULL default '0',
  `LineStatus` enum('Active','New','Deleted') NOT NULL default 'Active',
  `Owner` int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (`inventoryid`),
  KEY `locationid` (`locationid`),
  KEY `item` (`item`),
  KEY `statusid` (`statusid`),
  KEY `customerid` (`customerid`),
  KEY `vendorid` (`vendorid`),
  KEY `LineID` (`LineID`)
) TYPE=InnoDB;

--
-- Dumping data for table `inventory`
--


--
-- Table structure for table `inventorystatus`
--

DROP TABLE IF EXISTS `inventorystatus`;
CREATE TABLE `inventorystatus` (
  `StatusID` int(11) unsigned NOT NULL default '0',
  `StatusName` char(50) NOT NULL default '',
  KEY `StatusID` (`StatusID`)
) TYPE=InnoDB;

--
-- Dumping data for table `inventorystatus`
--

INSERT INTO `inventorystatus` (`StatusID`, `StatusName`) VALUES (1,'Avalible'),(2,'Back Ordered'),(1,'Not Avalible');

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
CREATE TABLE `invoice` (
  `InvoiceID` int(11) unsigned NOT NULL auto_increment,
  `ExternalID` varchar(25) default '[NULL]',
  `Title` varchar(50) default NULL,
  `Description` text,
  `Status` int(11) unsigned default '0',
  `Terms` int(11) unsigned default '0',
  `OrderID` int(11) unsigned NOT NULL default '0',
  `Owner` int(11) unsigned NOT NULL default '0',
  `Creator` int(11) unsigned NOT NULL default '0',
  `Modified By` int(11) unsigned NOT NULL default '0',
  `Created` timestamp(14) NOT NULL,
  `Modified` timestamp(14) NOT NULL default '00000000000000',
  `CustomerID` int(11) unsigned NOT NULL default '0',
  `SubTotal` float NOT NULL default '0',
  `Tax` int(11) unsigned NOT NULL default '0',
  `Shipping` varchar(50) default NULL,
  `Discount` float NOT NULL default '0',
  `Total` float NOT NULL default '0',
  `billaddress` int(11) unsigned NOT NULL default '0',
  `shipaddress` int(10) unsigned NOT NULL default '0',
  `ponumber` varchar(100) default '',
  `accountmgr` int(11) unsigned NOT NULL default '0',
  `invoicestatus` enum('Active','Deleted') NOT NULL default 'Active',
  `project` int(10) unsigned default '0',
  `InvoiceDate` date default '0000-00-00',
  PRIMARY KEY  (`InvoiceID`),
  KEY `Status` (`Status`),
  KEY `Terms` (`Terms`),
  KEY `OrderID` (`OrderID`),
  KEY `Owner` (`Owner`),
  KEY `Creator` (`Creator`),
  KEY `Modified By` (`Modified By`),
  KEY `CustomerID` (`CustomerID`),
  KEY `billaddress` (`billaddress`),
  KEY `shipaddress` (`shipaddress`),
  KEY `accountmgr` (`accountmgr`),
  KEY `project` (`project`)
) TYPE=InnoDB;

--
-- Dumping data for table `invoice`
--


--
-- Table structure for table `invoiceitems`
--

DROP TABLE IF EXISTS `invoiceitems`;
CREATE TABLE `invoiceitems` (
  `InvoiceID` int(11) NOT NULL default '0',
  `InvoiceLineID` int(11) unsigned NOT NULL auto_increment,
  `ItemID` int(11) unsigned NOT NULL default '0',
  `Quantity` int(11) unsigned NOT NULL default '0',
  `Price` float NOT NULL default '0',
  `SKU` varchar(50) NOT NULL default '',
  `Description` text NOT NULL,
  `status` enum('Active','Deleted') NOT NULL default 'Active',
  `TransactionID` int(11) NOT NULL default '0',
  `taxAmount` float NOT NULL default '0',
  PRIMARY KEY  (`InvoiceLineID`),
  KEY `InvoiceID` (`InvoiceID`),
  KEY `ItemID` (`ItemID`),
  KEY `TransactionID` (`TransactionID`)
) TYPE=InnoDB;

--
-- Dumping data for table `invoiceitems`
--


--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
CREATE TABLE `item` (
  `itemid` int(10) unsigned NOT NULL auto_increment,
  `externalid` varchar(25) default '',
  `title` varchar(255) default NULL,
  `description` text,
  `listprice` float unsigned default '0',
  `cost` float unsigned default '0',
  `taxclass` int(10) unsigned default '0',
  `type` int(10) unsigned default '0',
  `sku` varchar(255) default NULL,
  `parent` int(10) unsigned default '0',
  `manufacturerid` int(10) unsigned default '0',
  `vendorid` int(10) unsigned default '0',
  `itemcategory` int(11) unsigned default '0',
  `createdby` int(10) unsigned default NULL,
  `modifiedby` int(10) unsigned default NULL,
  `createddate` timestamp(14) NOT NULL,
  `modifieddate` timestamp(14) NOT NULL default '00000000000000',
  `glaccountid` int(10) unsigned default NULL,
  `deletestatus` enum('Active','Deleted') NOT NULL default 'Active',
  `LinkToInventory` enum('YES','NO') default 'NO',
  `qtyonbackorder` int(10) unsigned default '0',
  `qtyonorder` int(10) unsigned default '0',
  PRIMARY KEY  (`itemid`),
  KEY `taxclass` (`taxclass`),
  KEY `type` (`type`),
  KEY `parent` (`parent`),
  KEY `manufacturerid` (`manufacturerid`),
  KEY `vendorid` (`vendorid`),
  KEY `itemcategory` (`itemcategory`),
  KEY `createdby` (`createdby`),
  KEY `modifiedby` (`modifiedby`),
  KEY `glaccountid` (`glaccountid`)
) TYPE=InnoDB;

--
-- Dumping data for table `item`
--


--
-- Table structure for table `itemcategory`
--

DROP TABLE IF EXISTS `itemcategory`;
CREATE TABLE `itemcategory` (
  `categoryid` int(10) unsigned NOT NULL auto_increment,
  `categoryname` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`categoryid`)
) TYPE=InnoDB;

--
-- Dumping data for table `itemcategory`
--


--
-- Table structure for table `itemtype`
--

DROP TABLE IF EXISTS `itemtype`;
CREATE TABLE `itemtype` (
  `itemtypeid` int(10) unsigned NOT NULL auto_increment,
  `title` varchar(25) NOT NULL default '',
  `description` text NOT NULL,
  PRIMARY KEY  (`itemtypeid`)
) TYPE=InnoDB;

--
-- Dumping data for table `itemtype`
--

INSERT INTO `itemtype` (`itemtypeid`, `title`, `description`) VALUES (1,'Inventory Item',''),(2,'Non-inventory Item',''),(3,'Service',''),(4,'Other Charge',''),(5,'Discount',''),(6,'Expense',''),(7,'Support Instance',''),(8,'Support Contract','');

--
-- Table structure for table `knowledgebase`
--

DROP TABLE IF EXISTS `knowledgebase`;
CREATE TABLE `knowledgebase` (
  `kbid` int(10) unsigned NOT NULL auto_increment,
  `title` varchar(255) default NULL,
  `detail` text,
  `createdby` int(10) unsigned NOT NULL default '0',
  `created` datetime NOT NULL default '0000-00-00 00:00:00',
  `updatedby` int(10) unsigned NOT NULL default '0',
  `updated` timestamp(14) NOT NULL,
  `owner` int(10) unsigned NOT NULL default '0',
  `category` int(10) unsigned NOT NULL default '0',
  `status` enum('DRAFT','PUBLISH') NOT NULL default 'DRAFT',
  `publishToCustomerView` enum('YES','NO') NOT NULL default 'NO',
  PRIMARY KEY  (`kbid`),
  KEY `createdby` (`createdby`),
  KEY `updatedby` (`updatedby`),
  KEY `owner` (`owner`),
  KEY `category` (`category`)
) TYPE=InnoDB;

--
-- Dumping data for table `knowledgebase`
--


--
-- Table structure for table `knowledgebaselink`
--

DROP TABLE IF EXISTS `knowledgebaselink`;
CREATE TABLE `knowledgebaselink` (
  `ticketid` int(10) unsigned NOT NULL default '0',
  `recordtypeid` int(10) unsigned NOT NULL default '0',
  `recordid` int(10) unsigned NOT NULL default '0',
  KEY `ticketid` (`ticketid`),
  KEY `recordtypeid` (`recordtypeid`),
  KEY `recordid` (`recordid`)
) TYPE=InnoDB;

--
-- Dumping data for table `knowledgebaselink`
--


--
-- Table structure for table `license`
--

DROP TABLE IF EXISTS `license`;
CREATE TABLE `license` (
  `LicenseID` int(11) unsigned NOT NULL auto_increment,
  `LicenseKey` varchar(19) default NULL,
  `LastVerified` datetime default NULL,
  `LicenseVerification` varchar(32) default NULL,
  PRIMARY KEY  (`LicenseID`),
  UNIQUE KEY `LicenseID` (`LicenseID`)
) TYPE=InnoDB;

--
-- Dumping data for table `license`
--


--
-- Table structure for table `listcolumns`
--

DROP TABLE IF EXISTS `listcolumns`;
CREATE TABLE `listcolumns` (
  `listtype` varchar(30) NOT NULL default '',
  `columnname` varchar(100) NOT NULL default '',
  `columnorder` int(11) NOT NULL default '0',
  PRIMARY KEY  (`listtype`,`columnorder`)
) TYPE=InnoDB;

--
-- Dumping data for table `listcolumns`
--

INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ActivityTask','Title',1),('ActivityTask','CreatedBy',2),('ActivityTask','Created',3),('ActivityTask','DueDate',4),('ActivityTask','Priority',5),('ActivityTask','Status',6),('Address','Street1',1),('Address','Street2',2),('Address','City',3),('Address','State',4),('Address','ZipCode',5),('Address','Country',6),('AdHocReport','ReportID',1),('AdHocReport','Name',2),('AdHocReport','Description',3),('AdHocReport','View',4),('AllActivity','Type',1),('AllActivity','Title',2),('AllActivity','Created',3),('AllActivity','Due',4),('AllActivity','Priority',5),('AllActivity','CreatedBy',6),('AllActivity','Status',7),('Appointment','Title',1),('Appointment','Details',2),('Appointment','Entity',3),('Appointment','Individual',4),('Appointment','Start',5),('Appointment','End',6),('Appointment','Status',7),('Appointment','Priority',8),('Appointment','Owner',9),('Appointment','Created',10),('Appointment','CreatedBy',11),('Appointment','Notes',12),('BottomContacts','Name',1),('BottomContacts','Title',2),('BottomContacts','Entity',3),('BottomContacts','Phone',4),('BottomContacts','Fax',5),('BottomContacts','Email',6),('BottomEntityTicket','Subject',1),('BottomEntityTicket','DateOpened',2),('BottomEntityTicket','DateClosed',3),('BottomEntityTicket','Status',4),('BottomEntityTicket','AssignedTo',5),('BottomFiles','Name',1),('BottomFiles','Description',2),('BottomFiles','Created',3),('BottomFiles','Updated',4),('BottomFiles','CreatedBy',5),('BottomIndividual','Name',1),('BottomIndividual','Title',2),('BottomIndividual','Entity',3),('BottomIndividual','Phone',4),('BottomIndividual','Fax',5),('BottomIndividual','Email',6),('BottomIndividualTicket','Subject',1),('BottomIndividualTicket','DateOpened',2),('BottomIndividualTicket','DateClosed',3),('BottomIndividualTicket','Status',4),('BottomIndividualTicket','AssignedTo',5),('BottomMultiActivity','Type',1),('BottomMultiActivity','Title',2),('BottomMultiActivity','Created',3),('BottomMultiActivity','Due',4),('BottomMultiActivity','Priority',5),('BottomMultiActivity','CreatedBy',6),('BottomMultiActivity','Status',7),('BottomNotes','Title',1),('BottomNotes','Date',2),('BottomNotes','Priority',3),('BottomNotes','CreatedBy',4),('BottomProposal','ProposalID',1),('BottomProposal','Title',2),('BottomProposal','Description',3),('BottomProposal','Type',4),('BottomProposal','Stage',5),('BottomProposal','Probability',6),('BottomProposal','Status',7),('BottomProposal','EstimatedCloseDate',8),('BottomProposal','ForecastAmmount',9),('BottomTask','Task',1),('BottomTask','Milestone',2),('BottomTask','Owner',3),('BottomTask','StartDate',4),('BottomTask','DueDate',5),('BottomTask','Complete',6),('BottomTimeslip','Task',1),('BottomTimeslip','Description',2),('BottomTimeslip','Duration',3),('BottomTimeslip','CreatedBy',4),('BottomTimeslip','Date',5),('BottomTimeslip','StartTime',6),('BottomTimeslip','EndTime',7),('Call','Title',1),('Call','Details',2),('Call','Entity',3),('Call','Individual',4),('Call','Start',5),('Call','End',6),('Call','CallType',7),('Call','Status',8),('Call','Priority',9),('Call','Owner',10),('Call','Created',11),('Call','CreatedBy',12),('Call','Notes',13),('Competition','EntityID',1),('Competition','EntityName',2),('Competition','Strengths',3),('Competition','Weaknesses',4),('Competition','Notes',5),('CustomField','Field',1),('CustomField','Value',2),('CustomFields','Name',1),('CustomFields','Type',2),('CustomFields','Module',3),('CustomFields','Record',4),('CustomReport','ReportID',1),('CustomReport','Name',2),('CustomReport','Description',3),('CustomReport','View',4),('CustomView','ViewID',1),('CustomView','ViewName',2),('CustomView','Module',3),('CustomView','Record',4),('CVAttic','Title',1),('CVAttic','Module',2),('CVAttic','Record',3),('CVAttic','Owner',4),('CVAttic','Deleted',5),('CVGarbage','Title',1),('CVGarbage','Module',2),('CVGarbage','Record',3),('CVGarbage','Owner',4),('CVGarbage','Deleted',5),('Email','Subject',1),('Email','From',2),('Email','To',3),('Email','Received',4),('Email','Size',5),('EmailLookup','To',1),('EmailLookup','Cc',2),('EmailLookup','Bcc',3),('EmailLookup','Name',4),('EmailLookup','Address',5),('Employee','Name',1),('Employee','Title',2),('Employee','Entity',3),('Employee','Phone',4),('Employee','Fax',5),('Employee','Email',6),('EmployeeHandbook','Name',1),('EmployeeHandbook','Description',2),('EmployeeHandbook','Created',3),('EmployeeHandbook','Updated',4),('EmployeeHandbook','CreatedBy',5),('Entity','EntityID',1),('Entity','Name',2),('Entity','PrimaryContact',3),('Entity','Phone',4),('Entity','Email',5),('Entity','Address',6),('Entity','Street1',7),('Entity','Street2',8),('Entity','City',9),('Entity','State',10),('Entity','Zip',11),('Entity','Country',12),('Entity','ListID',13),('Entity','Website',14),('Entity','AccountManager',15),('Entity','Fax',16),('EntityMerge','Title',1),('EntityMerge','Owner',2),('Event','EventID',1),('Event','Title',2),('Event','Description',3),('Event','Start',4),('Event','End',5),('Event','RegisteredAttendees',6),('Event','OwnerName',7),('EventAtendees','individualname',1),('EventAtendees','email',2),('EventAtendees','accepted',3),('Expense','ExpenseID',1),('Expense','Amount',2),('Expense','Created',3),('Expense','EntityName',4),('Expense','Status',5),('Expense','Creator',6),('Expenses','ID',1),('Expenses','Employee',2),('Expenses','StartDate',3),('Expenses','EndDate',4),('Expenses','Amount',5),('Expenses','Status',6),('Expenses','CreatedBy',7),('FAQ','Title',1),('FAQ','Created',2),('FAQ','Updated',3),('File','Name',1),('File','Description',2),('File','Created',3),('File','Updated',4),('File','CreatedBy',5),('File','FolderName',6),('ForcastedSales','Title',1),('ForcastedSales','Entity',2),('ForcastedSales','Type',3),('ForcastedSales','Probability',4),('ForcastedSales','Status',5),('ForcastedSales','CloseDate',6),('ForcastedSales','Forecast Amnt.',7),('ForcastedSales','Sales Rep.',8),('ForecastSales','Title',1),('ForecastSales','Description',2),('ForecastSales','Entity',3),('ForecastSales','Type',4),('ForecastSales','Stage',5),('ForecastSales','Probability',6),('ForecastSales','Status',7),('ForecastSales','EstimatedCloseDate',8),('ForecastSales','ForecastAmount',9),('ForecastSales','ActualAmount',10),('ForecastSales','SalesPersonName',11),('GLAccount','Name',1),('GLAccount','Type',2),('GLAccount','Balance',3),('GLAccount','ParentAccount',4),('Group','Name',1),('Group','Description',2),('Group','NOOfMembers',3),('GroupMember','Name',1),('GroupMember','Title',2),('GroupMember','Email',3),('GroupMember','Phone',4),('History','Date',1),('History','User',2),('History','Action',3),('History','Type',4),('History','RecordName',5),('Individual','Name',1),('Individual','Title',2),('Individual','Entity',3),('Individual','Phone',4),('Individual','Fax',5),('Individual','Email',6),('IndividualMerge','Title',1),('IndividualMerge','entityName',2),('Inventory','InventoryID',1),('Inventory','ItemName',2),('Inventory','Identifier',3),('Inventory','Manufacturer',4),('Inventory','Vendor',5),('InvoiceHistory','InvoiceID',1),('InvoiceHistory','OrderID',2),('InvoiceHistory','Customer',3),('InvoiceHistory','Total',5),('InvoiceHistory','Paid',6),('InvoiceHistory','Creator',7),('Item','SKU',1),('Item','Name',2),('Item','Type',3),('Item','Price',4),('Item','OnHand',5),('Item','Tax',6),('Item','Cost',7),('Item','Vendor',8),('Item','Manufacturer',9),('Knowledgebase','Name',1),('Knowledgebase','DateCreated',2),('Knowledgebase','DateUpdated',3),('Literature','Name',1),('Literature','File',2),('LiteratureFulfillment','LiteratureRequested',1),('LiteratureFulfillment','WhoRequested',2),('LiteratureFulfillment','DateRequested',3),('LiteratureFulfillment','DeliveryMethod',4),('LiteratureFulfillment','Entity',5),('Marketing','Title',1),('Marketing','Description',2),('Marketing','OrderValue',3),('Marketing','NumberOfRecords',4),('Marketing','OpportunityCount',5),('Marketing','OpportunityValue',6),('Marketing','OrderCount',7),('MarketingListMembers','Entity',1),('MarketingListMembers','Individual',2),('MarketingListMembers','Email',3),('MarketingListMembers','PhoneNumber',4),('Meeting','Title',1),('Meeting','Details',2),('Meeting','Entity',3),('Meeting','Individual',4),('Meeting','Start',5),('Meeting','End',6),('Meeting','Status',7),('Meeting','Priority',8),('Meeting','Owner',9),('Meeting','Created',10),('Meeting','CreatedBy',11),('Meeting','Notes',12),('MOC','Type',1),('MOC','Content',2),('MOC','Notes',3),('MultiActivity','Title',1),('MultiActivity','Details',2),('MultiActivity','Entity',3),('MultiActivity','Individual',4),('MultiActivity','Start',5),('MultiActivity','End',6),('MultiActivity','Type',7),('MultiActivity','Status',8),('MultiActivity','Priority',9),('MultiActivity','Owner',10),('MultiActivity','Created',11),('MultiActivity','CreatedBy',12),('MultiActivity','Notes',13),('NextAction','Title',1),('NextAction','Details',2),('NextAction','Entity',3),('NextAction','Individual',4),('NextAction','Start',5),('NextAction','End',6),('NextAction','Status',7),('NextAction','Priority',8),('NextAction','Owner',9),('NextAction','Created',10),('NextAction','CreatedBy',11),('NextAction','Notes',12),('Note','Title',1),('Note','Date',2),('Note','CreatedBy',3),('Note','Detail',4),('Opportunity','OpportunityID',1),('Opportunity','Title',2),('Opportunity','Description',3),('Opportunity','EntityID',4),('Opportunity','Entity',5),('Opportunity','Type',6),('Opportunity','Stage',7),('Opportunity','Probability',8),('Opportunity','Status',9),('Opportunity','EstimatedCloseDate',10),('Opportunity','ForecastAmmount',11),('Opportunity','ActualAmount',12),('Opportunity','SalePersonName',13),('Order','OrderNO',1),('Order','Entity',2),('Order','Date',3),('Order','Total',4),('Order','SalesRep',5),('Payment','PaymentID',1),('Payment','Entity',2),('Payment','AmountPaid',3),('Payment','AppliedAmount',4),('Payment','UnAppliedAmount',5),('Payment','PaymentDate',6),('Payment','PaymentMethod',7),('Payment','Reference',8),('Payment','CreatedBy',9),('Project','Name',1),('Project','Entity',2),('Project','Status',3),('Project','DueDate',4),('Promotion','Title',1),('Promotion','Description',2),('Promotion','StartDate',3),('Promotion','EndDate',4),('Promotion','NoOfOrders',5),('Promotion','Status',6),('Proposal','ProposalID',1),('Proposal','Title',2),('Proposal','Description',3),('Proposal','Type',4),('Proposal','Stage',5),('Proposal','Probability',6),('Proposal','Status',7),('Proposal','EstimatedCloseDate',8),('Proposal','ForecastAmmount',9),('Proposal','SalePersonID',10),('Proposal','SalePersonName',11),('PurchaseOrder','PurchaseOrderID',1),('PurchaseOrder','Created',2),('PurchaseOrder','Creator',3),('PurchaseOrder','Entity',4),('PurchaseOrder','SubTotal',5),('PurchaseOrder','Tax',6),('PurchaseOrder','Total',7),('PurchaseOrder','Status',8),('Rule','Name',1),('Rule','Description',2),('Rule','Enabled',3),('SavedSearch','SearchID',1),('SavedSearch','SearchName',2),('SavedSearch','Module',3),('SavedSearch','Record',4),('StandardReport','ReportID',1),('StandardReport','Name',2),('StandardReport','Description',3),('Task','Title',1),('Task','Project',2),('Task','Milestone',3),('Task','Manager',4),('Task','StartDate',5),('Task','DueDate',6),('Task','Complete',7),('Task','Status',9),('Template','TemplateID',1),('Template','Name',2),('Template','Category',3),('Thread','Title',1),('Thread','Date',2),('Thread','Reference',3),('Thread','Priority',4),('Thread','CreatedBy',5),('Ticket','Subject',1),('Ticket','Entity',2),('Ticket','DateOpened',3),('Ticket','Status',4),('Ticket','DateClosed',5),('Ticket','AssignedTo',6),('Ticket','Number',8),('TimeSheet','ID',1),('TimeSheet','Employee',2),('TimeSheet','StartDate',3),('TimeSheet','EndDate',4),('TimeSheet','Duration',5),('TimeSheet','CreatedBy',6),('Timeslip','TimeSlipID',1),('Timeslip','Description',2),('Timeslip','Project',3),('Timeslip','Task',4),('Timeslip','Duration',5),('Timeslip','CreatedBy',6),('Timeslip','Date',7),('Timeslip','StartTime',8),('Timeslip','EndTime',9),('ToDo','Title',1),('ToDo','Details',2),('ToDo','Entity',3),('ToDo','Individual',4),('ToDo','Start',5),('ToDo','End',6),('ToDo','Status',7),('ToDo','Priority',8),('ToDo','Owner',9),('ToDo','Created',10),('ToDo','CreatedBy',11),('ToDo','Notes',12),('USER','UserID',1),('USER','IndividualID',2),('user','UserName',3),('user','Name',4),('user','Entity',5),('USER','Enabled',6),('USER','Email',7),('Vendor','Name',1),('Vendor','PrimaryContact',2),('Vendor','Phone',3),('Vendor','Email',5),('Vendor','Website',6),('Vendor','Address',7),('VolumeDiscount','DiscountID',1),('VolumeDiscount','Description',2),('VolumeDiscount','From',3),('VolumeDiscount','To',4),('VolumeDiscount','Discount',5),('VolumeDiscount','Status',6);

--
-- Table structure for table `listpreferences`
--

DROP TABLE IF EXISTS `listpreferences`;
CREATE TABLE `listpreferences` (
  `defaultviewid` int(11) unsigned NOT NULL default '0',
  `individualid` int(11) unsigned NOT NULL default '0',
  `sorttype` enum('A','D') NOT NULL default 'A',
  `sortelement` varchar(30) NOT NULL default '',
  `recordsperpage` int(11) unsigned NOT NULL default '10',
  `listtype` varchar(30) NOT NULL default '',
  PRIMARY KEY  (`individualid`,`listtype`),
  KEY `defaultviewid` (`defaultviewid`),
  KEY `individualid` (`individualid`)
) TYPE=InnoDB;

--
-- Dumping data for table `listpreferences`
--


--
-- Table structure for table `listtypes`
--

DROP TABLE IF EXISTS `listtypes`;
CREATE TABLE `listtypes` (
  `typename` varchar(30) NOT NULL default '',
  `moduleid` int(11) default NULL,
  PRIMARY KEY  (`typename`),
  KEY `moduleid` (`moduleid`)
) TYPE=InnoDB;

--
-- Dumping data for table `listtypes`
--

INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Address',1),('BottomEntityTicket',1),('BottomIndividual',1),('BottomIndividualTicket',1),('CustomField',1),('MOC',1),('Email',2),('Rule',2),('AllActivity',3),('MultiActivity',3),('Note',5),('File',6),('Competition',7),('Opportunity',7),('Proposal',7),('Event',8),('EventAtendees',8),('LiteratureFulfillment',8),('Marketing',8),('MarketingListMembers',8),('Promotion',8),('Entity',14),('Individual',15),('Group',16),('GroupMember',16),('Appointment',17),('Call',18),('ForecastSales',19),('Meeting',21),('NextAction',22),('ActivityTask',23),('ToDo',23),('BottomProposal',31),('BottomContacts',36),('BottomFiles',36),('BottomMultiActivity',36),('BottomNotes',36),('BottomTask',36),('BottomTimeslip',36),('Project',36),('FAQ',37),('Task',37),('Tasks',37),('Knowledgebase',38),('Timeslip',38),('Ticket',39),('Order',42),('Payment',43),('Expense',44),('PurchaseOrder',45),('Item',46),('GLAccount',47),('Inventory',48),('VolumeDiscount',49),('Vendor',50),('Employee',54),('InvoiceHistory',56),('CustomFields',58),('Template',59),('TimeSheet',60),('Expenses',61),('EmployeeHandbook',62),('AdHocReport',63),('CustomReport',63),('ReportResult',63),('StandardReport',63),('Merge',64),('USER',65),('SavedSearch',66),('CustomView',67),('SecurityProfile',68),('CVAttic',69),('CVGarbage',70),('History',71),('Literature',73),('EmailLookup',74);

--
-- Table structure for table `listview`
--

DROP TABLE IF EXISTS `listview`;
CREATE TABLE `listview` (
  `listViewId` int(11) unsigned NOT NULL auto_increment,
  `valueListId` int(11) unsigned NOT NULL default '0',
  `recordsPerPage` smallint(3) unsigned NOT NULL default '100',
  `sortColumn` int(11) unsigned NOT NULL default '0',
  `sortDirection` enum('ASC','DESC') NOT NULL default 'ASC',
  `owner` int(11) unsigned NOT NULL default '0',
  `modified` timestamp(14) NOT NULL,
  PRIMARY KEY  (`listViewId`),
  KEY `valueListId` (`valueListId`),
  KEY `owner` (`owner`),
  KEY `sortColumn` (`sortColumn`),
  CONSTRAINT `0_21729` FOREIGN KEY (`valueListId`) REFERENCES `valuelist` (`valueListId`) ON DELETE CASCADE,
  CONSTRAINT `0_21730` FOREIGN KEY (`sortColumn`) REFERENCES `valuelistfield` (`valueListFieldId`) ON DELETE CASCADE,
  CONSTRAINT `0_21731` FOREIGN KEY (`owner`) REFERENCES `individual` (`IndividualID`) ON DELETE CASCADE
) TYPE=InnoDB;

--
-- Dumping data for table `listview`
--


--
-- Table structure for table `listviews`
--

DROP TABLE IF EXISTS `listviews`;
CREATE TABLE `listviews` (
  `listtype` varchar(30) NOT NULL default '',
  `viewid` int(11) unsigned NOT NULL auto_increment,
  `viewname` varchar(100) NOT NULL default '',
  `ownerid` int(11) unsigned default NULL,
  `creatorid` int(11) unsigned default NULL,
  `sortmember` varchar(100) default NULL,
  `sorttype` char(1) default NULL,
  `searchid` int(11) unsigned default NULL,
  `noofrecords` int(3) unsigned default NULL,
  `searchtype` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`viewid`),
  KEY `ownerid` (`ownerid`),
  KEY `creatorid` (`creatorid`),
  KEY `searchid` (`searchid`)
) TYPE=InnoDB;

--
-- Dumping data for table `listviews`
--

INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Entity',1,'Default View',NULL,NULL,'Name','A',0,100,'A'),('Individual',3,'Default View',NULL,NULL,'Name','A',0,100,'A'),('Group',4,'Default View',NULL,NULL,'Name','D',0,100,'D'),('GroupMember',5,'Default View',NULL,NULL,'Name','D',0,100,'D'),('AllActivity',6,'Default View',NULL,NULL,'Title','D',0,100,'D'),('Address',7,'Default View',NULL,NULL,'Street1','D',0,100,'D'),('Appointment',8,'Default View',NULL,NULL,'Title','D',0,100,'D'),('BottomIndividual',9,'Default View',NULL,NULL,'Name','D',0,100,'D'),('Call',10,'Default View',NULL,NULL,'Title','D',0,100,'D'),('CustomField',11,'Default View',NULL,NULL,'Field','D',0,100,'D'),('Email',12,'Default View',NULL,NULL,'Received','D',0,100,'D'),('File',13,'Default View',NULL,NULL,'Name','D',0,100,'D'),('ForecastSales',14,'Default View',NULL,NULL,'Title','D',0,100,'D'),('Meeting',18,'Default View',NULL,NULL,'Title','D',0,100,'D'),('MOC',19,'Default View',NULL,NULL,'Type','D',0,100,'D'),('MultiActivity',20,'Default View',NULL,NULL,'Type','A',0,100,'D'),('NextAction',21,'Default View',NULL,NULL,'Title','D',0,100,'D'),('Note',22,'Default View',NULL,NULL,'Date','D',0,100,'D'),('Rule',23,'Default View',NULL,NULL,'Name','D',0,100,'D'),('Tasks',24,'Default View',NULL,NULL,'Name','D',0,100,'D'),('ToDo',25,'Default View',NULL,NULL,'Title','D',0,100,'D'),('EmailLookup',30,'Default View',NULL,NULL,'Name','A',0,100,'A'),('Ticket',66,'Default View',NULL,NULL,'DateOpened','A',0,100,'D'),('FAQ',70,'Default View',NULL,NULL,'Title','D',0,100,'D'),('Knowledgebase',71,'Default View',NULL,NULL,'Name','D',0,100,'D'),('LiteratureFulfillment',72,'Default View',NULL,NULL,'LiteratureRequested','A',0,100,'D'),('Event',73,'Default View',NULL,NULL,'Title','A',0,100,'A'),('GLAccount',74,'Default View',NULL,NULL,'Name','A',0,100,'D'),('Marketing',75,'Default View',NULL,NULL,'Title','A',0,100,'A'),('Promotion',76,'Default View',NULL,NULL,'Title','A',0,100,'A'),('Item',77,'Default View',NULL,NULL,'SKU','A',0,100,'A'),('Inventory',79,'Default View',NULL,NULL,'InventoryID','A',0,100,'A'),('Order',81,'Default View',NULL,NULL,'OrderNO','A',0,100,'A'),('Payment',82,'Default View',NULL,NULL,'PaymentID','A',0,100,'A'),('Expense',83,'Default View',NULL,NULL,'ExpenseID','A',0,100,'A'),('PurchaseOrder',84,'Default View',NULL,NULL,'PurchaseOrderID','A',0,100,'A'),('VolumeDiscount',85,'Default View',NULL,NULL,'DiscountID','A',0,100,'A'),('Vendor',86,'Default View',NULL,NULL,'Name','A',0,100,'A'),('InvoiceHistory',87,'Default View',NULL,NULL,'InvoiceID','A',0,100,'A'),('BottomTask',88,'Default View',NULL,NULL,'Task','A',0,100,'A'),('BottomTimeslip',89,'Default View',NULL,NULL,'Task','A',0,100,'A'),('Task',90,'Default View',NULL,NULL,'Title','A',0,100,'A'),('Project',91,'Default View',NULL,NULL,'Name','A',0,100,'A'),('Timeslip',92,'Default View',NULL,NULL,'TimeSlipID','A',0,100,'A'),('Opportunity',93,'Default View',NULL,NULL,'EstimatedCloseDate','A',0,100,'A'),('Proposal',94,'Default View',NULL,NULL,'EstimatedCloseDate','A',0,100,'D'),('ActivityTask',100,'Default View',NULL,NULL,'Title','A',0,100,'A'),('BottomMultiActivity',101,'Default View',NULL,NULL,'Type','A',0,100,'A'),('BottomContacts',104,'Default View',NULL,NULL,'Name','A',0,100,'A'),('BottomFiles',105,'Default View',NULL,NULL,'Name','A',0,100,'A'),('BottomNotes',106,'Default View',NULL,NULL,'Date','D',0,100,'A'),('CustomFields',110,'Default View',NULL,NULL,'Name','A',0,100,'A'),('Employee',170,'Default View',NULL,NULL,'Name','A',0,100,'A'),('TimeSheet',171,'Default View',NULL,NULL,'ID','A',0,100,'A'),('Expenses',172,'Default View',NULL,NULL,'ID','A',0,100,'A'),('EmployeeHandbook',173,'Default View',NULL,NULL,'Name','A',0,100,'A'),('USER',174,'Default User',NULL,NULL,'Name','A',0,100,'D'),('SavedSearch',175,'Default Saved Search',NULL,NULL,'SearchName','A',17,100,'A'),('Employee',176,'uma_testv1',546,546,'Name','A',0,100,'A'),('CustomView',177,'Default CustomView',NULL,NULL,'ViewName','A',17,100,'A'),('Order',178,'uma_v1',546,546,'OrderNO','A',0,100,'A'),('SecurityProfile',179,'Default Security Profile',NULL,NULL,'ProfileName','A',17,100,'A'),('CVAttic',180,'Default Attic',NULL,NULL,'Title','A',17,100,'A'),('CVGarbage',181,'Default Garbage',NULL,NULL,'Title','A',17,100,'A'),('History',182,'Default History',NULL,NULL,'Date','D',17,100,'D'),('EventAtendees',183,'Default View',NULL,NULL,'individualname','A',0,100,'A'),('MarketingListMembers',184,'Default View',NULL,NULL,'Entity','A',0,100,'A'),('StandardReport',185,'Default View',NULL,NULL,'ReportID','A',0,100,'A'),('AdHocReport',186,'Default View',NULL,NULL,'ReportID','A',0,100,'A'),('CustomReport',187,'Default View',NULL,NULL,'ReportID','A',0,100,'A'),('ReportResult',188,'Default View',NULL,NULL,'ReportID','A',0,100,'A'),('MarketingListMembers',189,'Default View',NULL,NULL,'Entity','A',0,100,'A'),('Literature',190,'Default View',NULL,NULL,'Name','A',0,10,'A'),('EntityMerge',191,'Merge Default',NULL,NULL,'Owner','A',0,101,'A'),('BottomProposal',192,'Default View',NULL,NULL,'Title','A',0,100,'D'),('Template',193,'Default View',NULL,NULL,'Name','A',0,100,'A'),('IndividualMerge',194,'Merge Default',NULL,NULL,'Title','A',0,101,'A');

--
-- Table structure for table `literature`
--

DROP TABLE IF EXISTS `literature`;
CREATE TABLE `literature` (
  `LiteratureID` int(10) unsigned NOT NULL auto_increment,
  `Title` varchar(25) NOT NULL default '',
  `FileID` int(10) unsigned NOT NULL default '0',
  `Description` text NOT NULL,
  KEY `LiteratureID` (`LiteratureID`),
  KEY `FileID` (`FileID`)
) TYPE=InnoDB;

--
-- Dumping data for table `literature`
--


--
-- Table structure for table `literaturerequest`
--

DROP TABLE IF EXISTS `literaturerequest`;
CREATE TABLE `literaturerequest` (
  `ActivityID` int(10) unsigned NOT NULL default '0',
  `RequestedBy` int(10) unsigned NOT NULL default '0',
  `DeliveryMethod` int(10) unsigned NOT NULL default '0',
  KEY `ActivityID` (`ActivityID`),
  KEY `RequestedBy` (`RequestedBy`)
) TYPE=InnoDB;

--
-- Dumping data for table `literaturerequest`
--


--
-- Table structure for table `literaturerequestlink`
--

DROP TABLE IF EXISTS `literaturerequestlink`;
CREATE TABLE `literaturerequestlink` (
  `ActivityID` int(11) unsigned default NULL,
  `LiteratureID` int(11) unsigned default NULL,
  KEY `ActivityID` (`ActivityID`),
  KEY `LiteratureID` (`LiteratureID`)
) TYPE=InnoDB;

--
-- Dumping data for table `literaturerequestlink`
--


--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
CREATE TABLE `location` (
  `locationid` int(10) unsigned NOT NULL auto_increment,
  `title` varchar(25) default NULL,
  `parent` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`locationid`),
  KEY `parent` (`parent`)
) TYPE=InnoDB;

--
-- Dumping data for table `location`
--


--
-- Table structure for table `mailimportfields`
--

DROP TABLE IF EXISTS `mailimportfields`;
CREATE TABLE `mailimportfields` (
  `fieldID` int(11) unsigned NOT NULL auto_increment,
  `typeID` int(11) unsigned NOT NULL default '0',
  `name` varchar(255) NOT NULL default '',
  `fieldName` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`fieldID`),
  KEY `typeID` (`typeID`)
) TYPE=InnoDB;

--
-- Dumping data for table `mailimportfields`
--

INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (1,1,'First Name','firstName'),(2,1,'MI','middleInitial'),(3,1,'Last Name','lastName'),(4,1,'Street1','street1'),(5,1,'Street2','street2'),(6,1,'City','city'),(7,1,'State','state'),(8,1,'Zip','zip'),(9,1,'Country','country'),(10,1,'Main Phone','mainPhone'),(11,1,'Fax Phone','faxPhone'),(12,1,'Mobile Phone','mobilePhone'),(13,1,'Home Phone','homePhone'),(14,1,'Other Phone','otherPhone'),(15,1,'Pager Phone','pagerPhone'),(16,1,'Work Phone','workPhone'),(17,1,'Email','email'),(18,1,'Source','source'),(19,1,'Title','title'),(20,1,'ID2','ID2'),(21,1,'Entity Name','entityName'),(22,1,'Entity Street1','entityStreet1'),(23,1,'Entity Street2','entityStreet2'),(24,1,'Entity City','entityCity'),(25,1,'Entity State','entityState'),(26,1,'Entity Zip','entityZip'),(27,1,'Entity Country','entityCountry'),(28,1,'Entity Main Phone','entityMainPhone'),(29,1,'Entity Fax Phone','entityFaxPhone'),(30,1,'Entity Mobile Phone','entityMobilePhone'),(31,1,'Entity Home Phone','entityHomePhone'),(32,1,'Entity Other Phone','entityOtherPhone'),(33,1,'Entity Pager Phone','entityPagerPhone'),(34,1,'Entity Work Phone','entityWorkPhone'),(35,1,'Entity Email','entityEmail'),(36,1,'Entity Source','entitySource'),(37,1,'Entity ID2','entityID2'),(38,1,'Main Phone Ext','mainPhoneExt'),(39,1,'Fax Phone Ext','faxPhoneExt'),(40,1,'Mobile Phone Ext','mobilePhoneExt'),(41,1,'Home Phone Ext','homePhoneExt'),(42,1,'Other Phone Ext','otherPhoneExt'),(43,1,'Pager Phone Ext','pagerPhoneExt'),(44,1,'Work Phone Ext','workPhoneExt'),(45,1,'Entity Main Phone Ext','entityMainPhoneExt'),(46,1,'Entity Fax Phone Ext','entityFaxPhoneExt'),(47,1,'Entity Mobile Phone Ext','entityMobilePhoneExt'),(48,1,'Entity Home Phone Ext','entityHomePhoneExt'),(49,1,'Entity Other Phone Ext','entityOtherPhoneExt'),(50,1,'Entity Pager Phone Ext','entityPagerPhoneExt'),(51,1,'Entity Work Phone Ext','entityWorkPhoneExt');

--
-- Table structure for table `mailimporttypes`
--

DROP TABLE IF EXISTS `mailimporttypes`;
CREATE TABLE `mailimporttypes` (
  `typeID` int(11) unsigned NOT NULL auto_increment,
  `name` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`typeID`)
) TYPE=InnoDB;

--
-- Dumping data for table `mailimporttypes`
--

INSERT INTO `mailimporttypes` (`typeID`, `name`) VALUES (1,'Contact');

--
-- Table structure for table `marketinglist`
--

DROP TABLE IF EXISTS `marketinglist`;
CREATE TABLE `marketinglist` (
  `ListID` int(10) unsigned NOT NULL auto_increment,
  `Title` varchar(25) NOT NULL default '',
  `Description` text NOT NULL,
  `NumberofRecords` int(11) NOT NULL default '0',
  `Proposals` int(11) NOT NULL default '0',
  `Orders` int(11) NOT NULL default '0',
  `Ordervalue` float NOT NULL default '0',
  `Status` enum('YES','NO') NOT NULL default 'NO',
  `Owner` int(10) unsigned NOT NULL default '0',
  `Creator` int(10) unsigned NOT NULL default '0',
  `ModifiedBy` int(10) unsigned NOT NULL default '0',
  `Created` datetime NOT NULL default '0000-00-00 00:00:00',
  `Modified` timestamp(14) NOT NULL,
  PRIMARY KEY  (`ListID`),
  KEY `Owner` (`Owner`),
  KEY `Creator` (`Creator`),
  KEY `ModifiedBy` (`ModifiedBy`)
) TYPE=InnoDB;

--
-- Dumping data for table `marketinglist`
--

INSERT INTO `marketinglist` (`ListID`, `Title`, `Description`, `NumberofRecords`, `Proposals`, `Orders`, `Ordervalue`, `Status`, `Owner`, `Creator`, `ModifiedBy`, `Created`, `Modified`) VALUES (1,'Default List','Do not delete this list.',0,0,0,0,'YES',1,1,1,'2004-03-08 09:21:31',20040308092131);

--
-- Table structure for table `mastertable`
--

DROP TABLE IF EXISTS `mastertable`;
CREATE TABLE `mastertable` (
  `Name` varchar(50) default NULL,
  `TableType` varchar(50) NOT NULL default ''
) TYPE=InnoDB;

--
-- Dumping data for table `mastertable`
--

INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('accountingstatus','M'),('accountingterms','M'),('action',''),('activity',''),('activityaction',''),('activitylink',''),('activityportlet','M'),('activitypriority','M'),('activityresources','M'),('activitystatus','M'),('activitytype','M'),('address','M'),('addressrelate','M'),('addresstype','M'),('alert',''),('alertpreference','M'),('applypayment',''),('attachment',''),('attendee',''),('attendeestatus',''),('attic',''),('atticdata',''),('calendarportlet','M'),('call',''),('calltype','M'),('category','M'),('competition',''),('contacttype','M'),('contentstatus',''),('country','M'),('createfieldauthorisation','M'),('createrecordauthorisation','M'),('customfield','M'),('customfieldmultiple',''),('customfieldscalar',''),('customfieldvalue','M'),('cvfile',''),('cvfilefolder',''),('cvfolder','M'),('cvfolderlocation','M'),('cvjoin',''),('cvorder',''),('cvtable','M'),('dbase','M'),('defaultviews','M'),('deliverymethod','M'),('emailaccount','M'),('emailaction',''),('emailcomposition','M'),('emailfolder','M'),('emailmessage',''),('emailportlet','M'),('emailpreference','M'),('emailrecipient',''),('emailrules','M'),('emailstore',''),('employee','M'),('entity','M'),('event',''),('eventregister',''),('expense',''),('expenseitem',''),('faq',''),('field','M'),('fieldauthorisation','M'),('finalmappings','M'),('folderlist',''),('glaccount','M'),('glaccounttype',''),('grouptbl','M'),('individual','M'),('inventory',''),('inventorystatus',''),('invoice',''),('invoiceitems',''),('item','M'),('itemcategory','M'),('itemtype','M'),('knowledgebase',''),('knowledgebaselink',''),('listcolumns','M'),('listpreferences','M'),('listtypes','M'),('listviews','M'),('literature','M'),('literaturerequest',''),('location','M'),('marketinglist','M'),('member','M'),('methodofcontact','M'),('mocrelate','M'),('moctype','M'),('module','M'),('moduleauthorisation','M'),('modulepreference','M'),('newsportlet','M'),('note',''),('opportunity',''),('opportunitylink',''),('opportunityportlet','M'),('orderitem',''),('otherpreferences','M'),('userpreferencedefault',''),('payment',''),('paymentmethod','M'),('project',''),('projectlink',''),('projectstatus','M'),('promoitem',''),('promotion',''),('proposal',''),('proposalitem',''),('proposallink',''),('purchaseorder',''),('purchaseorderitem',''),('question',''),('record','M'),('recordauthorisation',''),('recurexcept',''),('recurrence',''),('resourcerelate','M'),('role',''),('salesprobability','M'),('salesstage','M'),('salesstatus','M'),('salestype','M'),('search',''),('searchcriteria',''),('searchexpression',''),('source','M'),('state','M'),('supportportlet','M'),('supportpriority','M'),('supportstatus','M'),('syncmaster','M'),('task',''),('taskassigned',''),('taskportlet','M'),('taxclass','M'),('taxjurisdiction','M'),('license',''),('taxmatrix','M'),('thread',''),('ticket',''),('ticketlink',''),('timeentry',''),('timeslip',''),('user','M'),('vendor','M'),('viewcolumns','M'),('terms','M'),('userpreference','M'),('ptartifact','M'),('ptcategory',''),('ptdetail',''),('additionalmenu','M'),('timesheet',''),('expenseform',''),('suggestion','M'),('delegation','M'),('userrole',''),('securityprofile',''),('syncconfig',''),('syncproperties',''),('applicationsetting',''),('individuallist',''),('restoresequence','M'),('contacthistory',''),('historytype','M'),('authorizationsettings','M'),('mastertable','M'),('usersecurityprofile','');

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
CREATE TABLE `member` (
  `GroupID` int(10) unsigned NOT NULL default '0',
  `ChildID` int(10) unsigned default NULL,
  KEY `GroupID` (`GroupID`),
  KEY `ChildID` (`ChildID`)
) TYPE=InnoDB;

--
-- Dumping data for table `member`
--


--
-- Table structure for table `methodofcontact`
--

DROP TABLE IF EXISTS `methodofcontact`;
CREATE TABLE `methodofcontact` (
  `MOCID` int(10) unsigned NOT NULL auto_increment,
  `MOCType` int(11) unsigned default NULL,
  `Content` varchar(50) default NULL,
  `syncas` varchar(100) default '',
  `Note` text,
  `MOCOrder` varchar(11) default NULL,
  PRIMARY KEY  (`MOCID`),
  KEY `MOCType` (`MOCType`)
) TYPE=InnoDB;

--
-- Dumping data for table `methodofcontact`
--

INSERT INTO `methodofcontact` (`MOCID`, `MOCType`, `Content`, `syncas`, `Note`, `MOCOrder`) VALUES (1,4,'610-410-7457','',NULL,NULL),(2,2,'610-450-6771','Fax',NULL,NULL),(3,1,'info@centraview.com','Main',NULL,NULL);

--
-- Table structure for table `mocrelate`
--

DROP TABLE IF EXISTS `mocrelate`;
CREATE TABLE `mocrelate` (
  `MOCID` int(11) unsigned default NULL,
  `ContactType` int(11) unsigned default NULL,
  `ContactID` int(11) unsigned default NULL,
  `isPrimary` enum('YES','NO') default 'NO',
  KEY `MOCID` (`MOCID`),
  KEY `ContactType` (`ContactType`),
  KEY `ContactID` (`ContactID`)
) TYPE=InnoDB;

--
-- Dumping data for table `mocrelate`
--

INSERT INTO `mocrelate` (`MOCID`, `ContactType`, `ContactID`, `isPrimary`) VALUES (1,1,2,'YES'),(2,1,2,'YES'),(3,1,2,'YES');

--
-- Table structure for table `moctype`
--

DROP TABLE IF EXISTS `moctype`;
CREATE TABLE `moctype` (
  `MOCTypeID` int(10) unsigned NOT NULL auto_increment,
  `Name` varchar(25) default NULL,
  PRIMARY KEY  (`MOCTypeID`)
) TYPE=InnoDB;

--
-- Dumping data for table `moctype`
--

INSERT INTO `moctype` (`MOCTypeID`, `Name`) VALUES (1,'Email'),(2,'Fax'),(3,'Mobile'),(4,'Main'),(5,'Home'),(6,'Other'),(7,'Pager'),(8,'Work');

--
-- Table structure for table `module`
--

DROP TABLE IF EXISTS `module`;
CREATE TABLE `module` (
  `moduleid` int(10) unsigned NOT NULL auto_increment,
  `parentid` int(10) unsigned default NULL,
  `applyrights` tinyint(1) NOT NULL default '0',
  `hasfields` tinyint(1) NOT NULL default '0',
  `primarytable` varchar(50) NOT NULL default '',
  `ownerfield` varchar(50) NOT NULL default '',
  `primarykeyfield` varchar(255) NOT NULL default '',
  `otherownerfield` varchar(250) NOT NULL default '',
  `otherprimarytable` varchar(250) NOT NULL default '',
  `name` varchar(25) default NULL,
  PRIMARY KEY  (`moduleid`),
  KEY `parentid` (`parentid`)
) TYPE=InnoDB COMMENT='InnoDB free: 68608 kB; InnoDB free: 66560 kB';

--
-- Dumping data for table `module`
--

INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (1,NULL,0,0,'','','','','','Contacts'),(2,NULL,0,0,'emailmessage','Owner','','','','Email'),(3,NULL,0,0,'activity','owner','activityid','','','Activities'),(4,NULL,0,0,'','','','','','Calendar'),(5,NULL,0,0,'note','Owner','NoteID','','','Notes'),(6,NULL,0,0,'cvfile','Owner','FileID','','','File'),(7,NULL,0,0,'','','','','','Sales'),(8,NULL,0,0,'','','','','','Marketing'),(9,NULL,0,0,'','','','','','Projects'),(10,NULL,0,0,'','','','','','Support'),(11,NULL,0,0,'','','','','','Authentication'),(12,NULL,0,0,'','','','','','Accounting'),(13,NULL,0,0,'','','','','','HumanResources'),(14,1,0,0,'entity','Owner','EntityID','','','Entity'),(15,1,0,0,'individual','Owner','IndividualID','','','Individual'),(16,1,0,0,'grouptbl','owner','GroupID','','','Group'),(17,3,0,0,'activity','owner','activityid','','','Appointments'),(18,3,0,0,'activity','owner','activityid','','','Calls'),(19,3,0,0,'','','','','','ForecastSales'),(20,3,0,0,'','','','','','LiteratureRequests'),(21,3,0,0,'activity','owner','activityid','','','Meetings'),(22,3,0,0,'activity','owner','activityid','','','NextActions'),(23,3,0,0,'activity','owner','activityid','','','ToDos'),(25,4,0,0,'','','','','','DailyView'),(26,4,0,0,'','','','','','WeeklyView'),(27,4,0,0,'','','','','','WeeklyColumnar'),(28,4,0,0,'','','','','','MonthlyView'),(29,4,0,0,'','','','','','YearlyView'),(30,7,0,0,'opportunity o, activity a','owner','o.activityid = a.activityid and o.opportunityid','','','Opportunities'),(31,7,0,0,'proposal','Owner','ProposalID','','','Proposals'),(32,8,0,0,'marketinglist','owner','ListID','','','ListManager'),(33,8,0,0,'promotion','owner','PromotionID','','','Promotion'),(34,8,0,0,'literaturerequest','','','','','LiteratureFulfilment'),(35,8,0,0,'event','owner','EventId','','','Events'),(36,9,0,0,'project','Owner','ProjectID','','','Projects'),(37,9,0,0,' task, activity','owner','task.ActivityID=activity.ActivityID and task.ActivityID','','','Tasks'),(38,9,0,0,'timeslip','CreatedBy','TimeSlipID','','','Time Slips'),(39,10,0,0,'ticket','owner','ticketid','','','Ticket'),(40,10,0,0,'faq','owner','faqid','','','FAQ'),(41,10,0,0,'knowledgebase','owner','kbid','','','Knowledgebase'),(42,12,0,0,'cvorder','owner','orderid','','','OrderHistory'),(43,12,0,0,'payment','owner','paymentid','','','Payment'),(44,12,0,0,'expense','owner','expenseid','','','Expense'),(45,12,0,0,'purchaseorder','owner','purchaseorderid','','','PurchaseOrder'),(46,12,0,0,'item','createdby','itemid','','','Item'),(47,12,0,0,'glaccount','','glaccountsid','','','GLAccount'),(48,12,0,0,'inventory','Owner','inventoryid','','','Inventory'),(49,12,0,0,'','','','','','VolumeDiscount'),(50,12,0,0,'vendor','','','','','Vendor'),(51,13,0,0,'expenseform','Owner','ExpenseFormID','','','ExpenseForms'),(52,13,0,0,'timesheet','owner','TimeSheetID','','','Time Sheets'),(53,13,0,0,'','','','','','EmployeeHandbook'),(54,13,0,0,'employee','','','','','EmployeeList'),(55,13,0,0,'','','','','','SuggestionBox'),(56,12,0,0,'invoice','owner','InvoiceId','','','InvoiceHistory'),(57,39,0,0,'','','','','','Thread'),(58,NULL,0,0,'','','','','','CustomFields'),(59,NULL,0,0,'','','','','','Print Templates'),(60,NULL,0,0,'','','','','','Help'),(61,NULL,0,0,'','','','','','Synchronize'),(62,NULL,0,0,'','','','','','Preferences'),(63,NULL,0,0,'','','','','','Reports'),(64,NULL,0,0,'','','','','','Administrator'),(65,64,0,0,'','','','','','User'),(66,64,0,0,'','','','','','SavedSearch'),(67,64,0,0,'','','','','','CustomView'),(68,64,0,0,'','','','','','SecurityProfile'),(69,64,0,0,'','','','','','Attic'),(70,64,0,0,'','','','','','Search'),(71,64,0,0,'','','','','','History'),(72,NULL,0,0,'additionalmenu','','menuitem_id','','','Additional'),(73,64,0,0,'literature','','LiteratureID','','','Literature'),(76,6,0,0,'cvfolder','owner','FolderID','','','CVFolder'),(77,NULL,0,0,'event','','EventID','','','Event'),(78,NULL,0,0,'marketinglist','Owner','ListId','','','MarketingList'),(79,NULL,0,0,'emailrule','','ruleID','','','Email Rule');

--
-- Table structure for table `moduleauthorisation`
--

DROP TABLE IF EXISTS `moduleauthorisation`;
CREATE TABLE `moduleauthorisation` (
  `individualid` int(11) unsigned default NULL,
  `moduleid` int(11) unsigned default NULL,
  `profileid` int(11) unsigned NOT NULL default '0',
  `privilegelevel` tinyint(4) NOT NULL default '0',
  KEY `individualid` (`individualid`),
  KEY `moduleid` (`moduleid`),
  KEY `profileid` (`profileid`)
) TYPE=InnoDB;

--
-- Dumping data for table `moduleauthorisation`
--

INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,1,1,10),(NULL,2,1,10),(NULL,3,1,10),(NULL,4,1,10),(NULL,5,1,10),(NULL,6,1,10),(NULL,7,1,10),(NULL,8,1,10),(NULL,9,1,10),(NULL,10,1,10),(NULL,11,1,10),(NULL,12,1,10),(NULL,13,1,10),(NULL,14,1,10),(NULL,15,1,10),(NULL,16,1,10),(NULL,17,1,10),(NULL,18,1,10),(NULL,19,1,10),(NULL,20,1,10),(NULL,21,1,10),(NULL,22,1,10),(NULL,23,1,10),(NULL,24,1,10),(NULL,25,1,10),(NULL,26,1,10),(NULL,27,1,10),(NULL,28,1,10),(NULL,29,1,10),(NULL,30,1,10),(NULL,31,1,10),(NULL,32,1,10),(NULL,33,1,10),(NULL,34,1,10),(NULL,35,1,10),(NULL,37,1,10),(NULL,36,1,10),(NULL,38,1,10),(NULL,39,1,10),(NULL,40,1,10),(NULL,41,1,10),(NULL,42,1,10),(NULL,43,1,10),(NULL,44,1,10),(NULL,45,1,10),(NULL,46,1,10),(NULL,47,1,10),(NULL,48,1,10),(NULL,49,1,10),(NULL,50,1,10),(NULL,51,1,10),(NULL,52,1,10),(NULL,53,1,10),(NULL,54,1,10),(NULL,55,1,10),(NULL,56,1,10),(NULL,59,1,10),(NULL,60,1,10),(NULL,61,1,10),(NULL,62,1,10),(NULL,63,1,10),(NULL,64,1,10),(NULL,72,1,10);

--
-- Table structure for table `modulefieldmapping`
--

DROP TABLE IF EXISTS `modulefieldmapping`;
CREATE TABLE `modulefieldmapping` (
  `fieldid` int(11) unsigned NOT NULL default '0',
  `moduleid` int(11) unsigned NOT NULL default '0',
  `name` varchar(100) NOT NULL default '',
  `mapid` int(11) unsigned NOT NULL auto_increment,
  `listcolname` varchar(100) NOT NULL default '',
  `methodname` varchar(250) NOT NULL default '',
  `accesscontrollevel` tinyint(4) NOT NULL default '0',
  PRIMARY KEY  (`mapid`),
  KEY `fieldid` (`fieldid`),
  KEY `moduleid` (`moduleid`)
) TYPE=InnoDB;

--
-- Dumping data for table `modulefieldmapping`
--

INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (490,5,'detail',1,'','Detail',1),(3,14,'name',3,'','',1),(14,15,'title',4,'','',1),(0,5,'entity',5,'','RelateEntity',1),(0,5,'individual',6,'','RelateIndividual',1),(0,14,'address',7,'','',1),(0,14,'id1',8,'','',1),(0,14,'id2',9,'','',1),(0,14,'website',10,'','',1),(0,14,'source',11,'','',1),(0,14,'acctmanager',12,'','',1),(0,14,'acctteam',13,'','',1),(0,14,'contactmethod',14,'','',1),(0,14,'customfield',15,'','',1),(0,15,'id1',16,'','',1),(0,15,'id2',17,'','',1),(0,15,'address',18,'','',1),(0,15,'source',19,'','',1),(0,15,'contactmethod',20,'','',1),(0,15,'customfield',21,'','',1),(0,6,'description',22,'','Description',0),(0,6,'authorId',23,'','AuthorId',0),(0,6,'version',24,'','Version',0),(0,6,'entity',25,'','RelateEntity',0),(0,6,'individual',26,'','RelateIndividual',0),(0,36,'description',29,'0','Description',0),(0,36,'entity',30,'0','Entity',0),(0,36,'contact',31,'0','Contact',0),(0,36,'manager',32,'0','Manager',0),(0,36,'team',33,'0','Team',0),(0,36,'start',34,'0','Start',0),(0,36,'end',35,'0','End',0),(0,36,'status',36,'0','Status',0),(0,36,'budhr',37,'0','Budhr',0),(0,36,'usedHours',38,'0','UsedHours',0),(0,36,'available',39,'','Available',0),(0,37,'description',42,'0','ActivityDetails',0),(0,37,'milestone',43,'0','Milestone',0),(0,37,'sendAlert',44,'0','SendAlert',0),(0,37,'alertTypeAlert',45,'0','AlertTypeAlert',0),(0,37,'alertTypeEmail',46,'0','AlertTypeEmail',0),(0,37,'sendTo',47,'0','SendTo',0),(0,37,'manager',48,'0','Manager',0),(0,37,'status',49,'0','Status',0),(0,37,'percentComplete',50,'0','PercentComplete',0),(0,37,'assignedTo',51,'0','AssignedTo',0),(0,38,'project',52,'0','Reference',0),(0,38,'task',53,'0','TaskID',0),(0,38,'ticket',54,'0','lookupList',0),(0,39,'priority',55,'','Priority',0),(0,39,'detail',56,'','Detail',0),(0,39,'entityname',57,'','Entityname',0),(0,39,'contact',58,'','Contact',0),(0,39,'phone',59,'','Phone',0),(0,39,'email',60,'','Email',0),(0,39,'managername',61,'','Managername',0),(0,39,'assignedto',62,'','Assignedto',0),(0,39,'contact',63,'','Contact',0),(0,35,'attachfile',64,'','Attachfile',0),(0,35,'description',76,'','Description',0),(0,32,'listdescription',79,'','Listdescription',0),(0,35,'masterlistid',81,'','Masterlistid',0),(0,35,'moderatorname',82,'','Moderatorname',0),(0,35,'maxattendees',83,'','Maxattendees',0),(0,42,'statusIdValue',95,'','StatusIdValue',1),(0,42,'termsIdValue',99,'','TermsIdValue',1),(0,42,'acctMgrIdValue',100,'','AcctMgrIdValue',1),(0,42,'projectIdValue',101,'','ProjectIdValue',1),(0,31,'description',105,'','Description',0),(0,31,'status',106,'','Statusid',0),(0,31,'stage',107,'','Stage',0),(0,31,'proposaltype',108,'','Typeid',0),(0,31,'estimatedclosedate',109,'','Estimatedclose',0),(0,31,'probability',110,'','Probability',0),(0,30,'description',111,'','Description',0),(0,30,'source',112,'','SourceID',0),(0,30,'stage',113,'','StageID',0),(0,30,'status',114,'','StatusID',0),(0,30,'opportunitytype',115,'','OpportunityTypeID',0),(0,30,'probability',116,'','Probability',0),(0,30,'forcastedamount',117,'','ForcastedAmount',0),(0,30,'estimatedclosedate',118,'','EstimatedClose',0),(0,34,'individualname',119,'','Individualname',0),(0,56,'po',120,'','Po',0),(0,56,'statusId',121,'','StatusId',0),(0,56,'termId',122,'','TermId',0),(0,56,'accountManagerId',123,'','AccountManagerId',0),(0,56,'projectId',124,'','ProjectId',0),(0,30,'description',125,'','Description',0),(0,30,'sourcename',126,'','Sourcename',0),(0,44,'expenseDescription',127,'','ExpenseDescription',0),(0,44,'notes',128,'','Notes',0),(0,44,'statusIDValue',129,'','StatusIDValue',0),(0,30,'statusname',130,'','Statusname',0),(0,30,'stagename',131,'','Stagename',0),(0,30,'opportunitytypename',132,'','Opportunitytypename',0),(0,30,'forecastedamount',133,'','Forecastedamount',0),(0,30,'probabilityname',134,'','Probabilityname',0),(0,30,'estimatedclose',135,'','Estimatedclose',0),(0,30,'actualclose',136,'','Actualclose',0),(0,30,'acctmgrname',137,'','Acctmgrname',0),(0,30,'acctteamname',138,'','Acctteamname',0),(0,46,'itemDesc',139,'','ItemDesc',0),(0,46,'glAccountId',140,'','GlAccountId',0),(0,46,'taxClassId',141,'','TaxClassId',0),(0,46,'subItemOfId',142,'','SubItemOfId',0),(0,46,'price',143,'','price',0),(0,46,'cost',144,'','Cost',0),(0,46,'linkToInventory',145,'','LinkToInventory',0),(0,46,'qtyOnHand',146,'','QtyOnHand',0),(0,46,'qtyOnOrder',147,'','QtyOnOrder',0),(0,46,'qtyOnBackOrder',148,'','QtyOnBackOrder',0),(0,31,'prodescription',149,'','Prodescription',0),(0,31,'billingaddress',150,'','Billingaddress',0),(0,31,'shippingaddress',151,'','Shippingaddress',0),(0,31,'statuslist',152,'','Statuslist',0),(0,31,'stage',153,'','Stage',0),(0,31,'proposaltype',154,'','Proposaltype',0),(0,31,'probability',155,'','Probability',0),(0,31,'estimatedclose',156,'','Estimatedclose',0),(0,31,'actualclose',157,'','Actualclose',0),(0,31,'terms',158,'','Terms',0),(0,31,'item',159,'','Item',0),(0,31,'specialinstructions',160,'','Specialinstructions',0),(0,31,'attachFileValues',161,'','AttachFileValues',0),(0,45,'statusId',162,'','StatusId',0),(0,45,'purchaseorderDate',163,'','PurchaseorderDate',0),(0,45,'termId',164,'','TermId',0),(0,45,'accountManagerId',165,'','AccountManagerId',0),(0,48,'strIdentifier',167,'','StrIdentifier',0),(0,48,'intLocationID',168,'','IntLocationID',0),(0,48,'manufacturerVO',169,'','ManufacturerVO',0),(0,48,'vendorVO',170,'','VendorVO',0),(0,48,'strDescription',171,'','StrDescription',0),(0,48,'intStatusID',172,'','IntStatusID',0),(0,48,'soldToVo',173,'','soldToVo',0),(0,48,'customFieldsVec',174,'','CustomFieldsVec',0),(0,33,'description',175,'','Description',0),(0,33,'startdate',176,'','Startdate',0),(0,33,'status',177,'','Status',0),(0,33,'customfield',178,'','Customfield',0),(0,33,'notes',179,'','Notes',0),(0,33,'notes',180,'','Notes',0),(0,33,'itemlines',181,'','ItemLines',0),(0,34,'entityname',182,'','Entityname',0),(0,34,'assignedtoname',183,'','Assignedtoname',0),(0,34,'detail',184,'','Detail',0),(0,34,'deliverymethodname',185,'','Deliverymethodname',0),(0,34,'statusname',186,'','Statusname',0),(0,43,'reference',187,'','Reference',0),(0,43,'description',188,'','Description',0),(0,34,'duebyday',189,'','Duebyday',0),(0,34,'literaturename',190,'','Literaturename',0),(0,16,'description',191,'','Description',0),(0,16,'owner',192,'','Owner',0),(0,16,'memberlist',193,'','',0),(0,51,'formDescription',194,'','FormDescription',0),(0,51,'notes',195,'','Notes',0),(0,51,'linestatus',196,'','Linestatus',0),(0,51,'expenseId',197,'','ExpenseId',0),(0,52,'description',198,'','Description',0),(0,52,'notes',199,'','Notes',0),(0,52,'status',200,'','Status',0),(0,53,'description',201,'','Description',0),(0,17,'activityDetails',203,'','ActivityDetails',0),(0,17,'entityVO',204,'','EntityVO',0),(0,17,'individualVO',205,'','IndividualVO',0),(0,17,'opportunityID',206,'','OportunityID',0),(0,17,'projectID',207,'','ProjectID',0),(0,17,'status',208,'','Status',0),(0,17,'notes',209,'','Notes',0),(0,17,'activityStartDate',210,'','ActivityStartDate',0),(0,17,'activityEndDate',211,'','ActivityEndDate',0),(0,17,'owner',212,'','Owner',0),(0,53,'authorname',215,'','Authorname',0),(0,18,'activityDetails',217,'','ActivityDetails',0),(0,18,'entityVO',218,'','EntityVO',0),(0,18,'individualVO',219,'','IndividualVO',0),(0,18,'opportunityID',220,'','OpportunityID',0),(0,18,'projectID',221,'','ProjectID',0),(0,18,'owner',222,'','Owner',0),(0,18,'status',223,'','Status',0),(0,18,'notes',224,'','Notes',0),(0,18,'activityStartDate',225,'','ActivityStartDate',0),(0,18,'activityEndDate',226,'','ActivityEndDate',0),(0,21,'activityDetails',227,'','ActivityDetails',0),(0,21,'entityVO',228,'','EntityVO',0),(0,21,'individualVO',229,'','IndividualVO',0),(0,21,'opportunityID',230,'','OpportunityID',0),(0,21,'projectID',231,'','ProjectID',0),(0,21,'owner',232,'','Owner',0),(0,21,'status',233,'','Status',0),(0,21,'notes',234,'','Notes',0),(0,22,'activityDetails',235,'','ActivityDetails',0),(0,22,'entityVO',236,'','EntityVO',0),(0,22,'individualVO',237,'','IndividualVO',0),(0,22,'opportunityID',238,'','OpportunityID',0),(0,22,'projectID',239,'','ProjectID',0),(0,22,'owner',240,'','Owner',0),(0,22,'status',241,'','Status',0),(0,22,'notes',242,'','Notes',0),(0,23,'activityDetails',243,'','ActivityDetails',0),(0,23,'entityVO',244,'','EntityVO',0),(0,23,'individualVO',245,'','IndividualVO',0),(0,23,'opportunityID',246,'','OpportunityID',0),(0,23,'projectID',247,'','ProjectID',0),(0,23,'owner',248,'','Owner',0),(0,23,'status',249,'','Status',0),(0,23,'notes',250,'','Notes',0),(0,53,'fileversion',251,'','Fileversion',0),(0,53,'individualname',252,'','Individualname',0),(0,53,'entityname',253,'','Entityname',0),(0,14,'firstname',254,'','',1),(0,14,'middlename',255,'','',1),(0,14,'lastname',256,'','',1),(0,15,'entity',257,'','',1),(0,16,'groupname',258,'','',1),(0,16,'groupdescription',259,'','',1),(0,16,'owner',260,'','',1),(0,5,'title',261,'','Title',1),(0,15,'firstname',262,'','',1),(0,15,'lastname',263,'','',1),(0,3,'type',264,'','Type',1),(0,3,'priority',265,'','Priority',1),(0,3,'status',266,'','Status',1),(0,3,'title',267,'','Title',1),(0,3,'duedate',268,'','DueDate',1),(0,3,'completeddate',269,'','CompletedDate',1),(0,3,'details',270,'','Details',1),(0,3,'creator',271,'','Creator',1),(0,3,'owner',272,'','Owner',1),(0,3,'modifiedby',273,'','ModifiedBy',1),(0,3,'modified',274,'','Modified',1),(0,3,'created',275,'','Created',1),(0,3,'start',276,'','Start',1),(0,3,'end',277,'','End',1),(0,3,'location',278,'','Location',1),(0,3,'visibility',279,'','Visibility',1),(0,3,'notes',280,'','Notes',1),(0,3,'calltype',281,'','CallType',1),(0,15,'middlename',282,'','MiddleName',1),(0,46,'item',299,'','Item',1),(0,42,'acctMgr',307,'','AcctMgr',1),(0,42,'project',308,'','Project',1),(0,30,'entityid',309,'','Entityid',1),(0,30,'entityname',310,'','Entityname',1),(0,30,'opportunityid',311,'','Opportunityid',1),(0,30,'title',312,'','Title',1),(0,30,'activityid',313,'','Activityid',1),(0,30,'individualid',314,'','Individualid',1),(0,30,'individualname',315,'','Individualname',1),(0,30,'sourceid',316,'','Sourceid',1),(0,30,'statusid',317,'','Statusid',1),(0,30,'stageid',318,'','Stageid',1),(0,30,'opportunitytypeid',319,'','Opportunitytypeid',1),(0,30,'totalamount',320,'','Totalamount',1),(0,30,'probabilityid',321,'','Probabilityid',1),(0,30,'acctmgrid',322,'','Acctmgrid',1),(0,30,'acctteamid',323,'','Acctteamid',1),(0,14,'title',324,'','',1);

--
-- Table structure for table `modulepreference`
--

DROP TABLE IF EXISTS `modulepreference`;
CREATE TABLE `modulepreference` (
  `individualid` int(11) unsigned default NULL,
  `moduleid` int(11) unsigned default NULL,
  KEY `moduleid` (`moduleid`),
  KEY `individualid` (`individualid`)
) TYPE=InnoDB;

--
-- Dumping data for table `modulepreference`
--


--
-- Table structure for table `newsportlet`
--

DROP TABLE IF EXISTS `newsportlet`;
CREATE TABLE `newsportlet` (
  `individualid` int(11) unsigned default NULL,
  `visible` enum('YES','NO') default NULL,
  `days` int(11) default NULL,
  KEY `individualid` (`individualid`)
) TYPE=InnoDB;

--
-- Dumping data for table `newsportlet`
--


--
-- Table structure for table `note`
--

DROP TABLE IF EXISTS `note`;
CREATE TABLE `note` (
  `NoteID` int(4) unsigned NOT NULL auto_increment,
  `Title` varchar(255) default NULL,
  `Detail` text,
  `Creator` int(11) unsigned default NULL,
  `Owner` int(11) unsigned default NULL,
  `DateUpdated` timestamp(14) NOT NULL,
  `DateCreated` datetime default NULL,
  `UpdatedBy` int(11) unsigned default NULL,
  `priority` enum('HIGH','MEDIUM','LOW') default NULL,
  `RelateEntity` int(11) unsigned default NULL,
  `RelateIndividual` int(11) unsigned default NULL,
  PRIMARY KEY  (`NoteID`),
  KEY `Creator` (`Creator`),
  KEY `Owner` (`Owner`),
  KEY `UpdatedBy` (`UpdatedBy`),
  KEY `RelateEntity` (`RelateEntity`),
  KEY `RelateIndividual` (`RelateIndividual`)
) TYPE=InnoDB;

--
-- Dumping data for table `note`
--


--
-- Table structure for table `opportunity`
--

DROP TABLE IF EXISTS `opportunity`;
CREATE TABLE `opportunity` (
  `OpportunityID` int(11) unsigned NOT NULL auto_increment,
  `ActivityID` int(11) unsigned NOT NULL default '0',
  `title` varchar(255) NOT NULL default '',
  `Description` text NOT NULL,
  `EntityID` int(11) unsigned NOT NULL default '0',
  `IndividualID` int(11) unsigned NOT NULL default '0',
  `TypeID` int(11) unsigned NOT NULL default '0',
  `Status` int(11) unsigned NOT NULL default '0',
  `Stage` int(11) unsigned NOT NULL default '0',
  `ForecastAmmount` float(9,3) default NULL,
  `ActualAmount` float(9,3) default NULL,
  `Probability` int(11) unsigned NOT NULL default '0',
  `Source` int(11) unsigned NOT NULL default '0',
  `AccountManager` int(11) unsigned NOT NULL default '0',
  `AccountTeam` int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (`OpportunityID`),
  KEY `ActivityID` (`ActivityID`),
  KEY `EntityID` (`EntityID`),
  KEY `IndividualID` (`IndividualID`),
  KEY `TypeID` (`TypeID`),
  KEY `Status` (`Status`),
  KEY `Stage` (`Stage`),
  KEY `Probability` (`Probability`),
  KEY `Source` (`Source`),
  KEY `AccountManager` (`AccountManager`),
  KEY `AccountTeam` (`AccountTeam`)
) TYPE=InnoDB;

--
-- Dumping data for table `opportunity`
--


--
-- Table structure for table `opportunitylink`
--

DROP TABLE IF EXISTS `opportunitylink`;
CREATE TABLE `opportunitylink` (
  `OpportunityID` int(11) default NULL,
  `RecordTypeID` int(11) default NULL,
  `RecordID` int(11) default NULL,
  KEY `OpportunityID` (`OpportunityID`),
  KEY `RecordTypeID` (`RecordTypeID`),
  KEY `RecordID` (`RecordID`)
) TYPE=InnoDB;

--
-- Dumping data for table `opportunitylink`
--


--
-- Table structure for table `opportunityportlet`
--

DROP TABLE IF EXISTS `opportunityportlet`;
CREATE TABLE `opportunityportlet` (
  `individualid` int(11) unsigned default NULL,
  `visible` enum('YES','NO') default NULL,
  KEY `individualid` (`individualid`)
) TYPE=InnoDB;

--
-- Dumping data for table `opportunityportlet`
--


--
-- Table structure for table `orderitem`
--

DROP TABLE IF EXISTS `orderitem`;
CREATE TABLE `orderitem` (
  `lineid` int(11) unsigned NOT NULL auto_increment,
  `orderid` int(11) unsigned default NULL,
  `itemid` int(11) unsigned default NULL,
  `quantity` int(11) unsigned default NULL,
  `price` float default NULL,
  `sku` varchar(25) default NULL,
  `status` enum('Active','Deleted') NOT NULL default 'Active',
  `description` text,
  `taxAmount` float NOT NULL default '0',
  PRIMARY KEY  (`lineid`),
  KEY `orderid` (`orderid`),
  KEY `itemid` (`itemid`)
) TYPE=InnoDB;

--
-- Dumping data for table `orderitem`
--


--
-- Table structure for table `otherpreferences`
--

DROP TABLE IF EXISTS `otherpreferences`;
CREATE TABLE `otherpreferences` (
  `PreferenceName` varchar(100) NOT NULL default '',
  `PreferenceValue` varchar(100) NOT NULL default '',
  `IndividualID` int(11) unsigned NOT NULL default '0',
  KEY `IndividualID` (`IndividualID`)
) TYPE=InnoDB;

--
-- Dumping data for table `otherpreferences`
--

INSERT INTO `otherpreferences` (`PreferenceName`, `PreferenceValue`, `IndividualID`) VALUES ('DEFAULTFOLDERID','8',1);

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment` (
  `PaymentID` int(11) unsigned NOT NULL auto_increment,
  `ExternalID` varchar(25) default '[NULL]',
  `Title` varchar(100) NOT NULL default '',
  `Description` text NOT NULL,
  `EntityID` int(11) unsigned NOT NULL default '0',
  `PaymentMethod` int(11) unsigned NOT NULL default '0',
  `Reference` varchar(100) default '',
  `Amount` float NOT NULL default '0',
  `CardType` varchar(100) default '0',
  `CardNumber` varchar(100) default '0',
  `Expiration` date default '0000-00-00',
  `CheckNumber` varchar(100) default '',
  `Owner` int(11) unsigned NOT NULL default '0',
  `Creator` int(11) unsigned NOT NULL default '0',
  `ModifiedBy` int(11) unsigned NOT NULL default '0',
  `Created` timestamp(14) NOT NULL,
  `LineStatus` enum('New','Active','Deleted') NOT NULL default 'Active',
  `LineID` int(10) unsigned NOT NULL default '0',
  `Modified` timestamp(14) NOT NULL default '00000000000000',
  PRIMARY KEY  (`PaymentID`),
  KEY `EntityID` (`EntityID`),
  KEY `PaymentMethod` (`PaymentMethod`),
  KEY `Owner` (`Owner`),
  KEY `Creator` (`Creator`),
  KEY `ModifiedBy` (`ModifiedBy`),
  KEY `LineID` (`LineID`)
) TYPE=InnoDB;

--
-- Dumping data for table `payment`
--


--
-- Table structure for table `paymentmethod`
--

DROP TABLE IF EXISTS `paymentmethod`;
CREATE TABLE `paymentmethod` (
  `MethodID` int(10) unsigned NOT NULL auto_increment,
  `externalID` varchar(25) default '[NULL]',
  `Title` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`MethodID`)
) TYPE=InnoDB;

--
-- Dumping data for table `paymentmethod`
--

INSERT INTO `paymentmethod` (`MethodID`, `externalID`, `Title`) VALUES (1,'[NULL]','Credit Card'),(2,'[NULL]','Cash'),(3,'[NULL]','Check');

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `ProjectID` int(11) unsigned NOT NULL auto_increment,
  `ProjectTitle` varchar(255) default NULL,
  `Description` text,
  `StatusID` int(11) unsigned default NULL,
  `Start` date default NULL,
  `End` date default NULL,
  `BudgetedHours` int(10) unsigned default '0',
  `HoursUsed` int(10) unsigned default '0',
  `Manager` int(11) unsigned default NULL,
  `Owner` int(11) unsigned default NULL,
  `Creator` int(11) unsigned default NULL,
  `ModifiedBy` int(11) unsigned default NULL,
  `Modified` timestamp(14) NOT NULL,
  `Created` datetime default NULL,
  PRIMARY KEY  (`ProjectID`),
  KEY `Owner` (`Owner`),
  KEY `Creator` (`Creator`),
  KEY `ModifiedBy` (`ModifiedBy`)
) TYPE=InnoDB;

--
-- Dumping data for table `project`
--


--
-- Table structure for table `projectlink`
--

DROP TABLE IF EXISTS `projectlink`;
CREATE TABLE `projectlink` (
  `ProjectID` int(11) unsigned default NULL,
  `RecordTypeID` int(11) unsigned default NULL,
  `RecordID` int(11) unsigned default NULL,
  KEY `ProjectID` (`ProjectID`),
  KEY `RecordTypeID` (`RecordTypeID`),
  KEY `RecordID` (`RecordID`)
) TYPE=InnoDB;

--
-- Dumping data for table `projectlink`
--


--
-- Table structure for table `projectstatus`
--

DROP TABLE IF EXISTS `projectstatus`;
CREATE TABLE `projectstatus` (
  `StatusID` int(11) unsigned NOT NULL auto_increment,
  `Title` varchar(25) default NULL,
  `Description` text,
  PRIMARY KEY  (`StatusID`)
) TYPE=InnoDB;

--
-- Dumping data for table `projectstatus`
--

INSERT INTO `projectstatus` (`StatusID`, `Title`, `Description`) VALUES (1,'Open',NULL),(2,'Closed',NULL);

--
-- Table structure for table `promoitem`
--

DROP TABLE IF EXISTS `promoitem`;
CREATE TABLE `promoitem` (
  `PromotionID` int(10) unsigned NOT NULL default '0',
  `ItemID` int(10) unsigned NOT NULL default '0',
  `Quantity` int(10) unsigned NOT NULL default '0',
  `Rule` varchar(25) NOT NULL default '',
  `Price` float NOT NULL default '0',
  KEY `PromotionID` (`PromotionID`),
  KEY `ItemID` (`ItemID`)
) TYPE=InnoDB;

--
-- Dumping data for table `promoitem`
--


--
-- Table structure for table `promotion`
--

DROP TABLE IF EXISTS `promotion`;
CREATE TABLE `promotion` (
  `PromotionID` int(10) unsigned NOT NULL auto_increment,
  `Title` varchar(100) NOT NULL default '',
  `Description` text NOT NULL,
  `Status` enum('YES','NO') NOT NULL default 'YES',
  `Startdate` datetime NOT NULL default '0000-00-00 00:00:00',
  `Enddate` datetime NOT NULL default '0000-00-00 00:00:00',
  `Owner` int(10) unsigned NOT NULL default '0',
  `Creator` int(10) unsigned NOT NULL default '0',
  `ModifiedBy` datetime NOT NULL default '0000-00-00 00:00:00',
  `Created` datetime NOT NULL default '0000-00-00 00:00:00',
  `Notes` varchar(100) NOT NULL default '',
  `Modified` timestamp(14) NOT NULL,
  PRIMARY KEY  (`PromotionID`),
  KEY `Owner` (`Owner`),
  KEY `Creator` (`Creator`),
  KEY `ModifiedBy` (`ModifiedBy`)
) TYPE=InnoDB;

--
-- Dumping data for table `promotion`
--


--
-- Table structure for table `proposal`
--

DROP TABLE IF EXISTS `proposal`;
CREATE TABLE `proposal` (
  `ProposalID` int(11) NOT NULL auto_increment,
  `OpportunityID` int(11) unsigned default NULL,
  `Title` varchar(25) default NULL,
  `Description` text,
  `Owner` int(11) unsigned default NULL,
  `TypeID` int(11) unsigned default NULL,
  `Status` int(11) unsigned default NULL,
  `Stage` int(11) unsigned default NULL,
  `PreviousForecastAmount` float(20,2) unsigned default '0.00',
  `ForecastAmmount` float(9,2) default NULL,
  `ActualAmount` float(9,3) default NULL,
  `Probability` int(11) unsigned default NULL,
  `Source` int(11) unsigned default NULL,
  `AccountManager` int(11) unsigned default NULL,
  `AccountTeam` int(11) unsigned default NULL,
  `CreatedBy` int(25) unsigned default NULL,
  `Created` datetime default NULL,
  `ModifiedBy` int(25) unsigned default NULL,
  `Modified` datetime default NULL,
  `EstimatedCloseDate` datetime default NULL,
  `Shippingid` int(11) default '0',
  `Shipping` varchar(100) default '',
  `Billingid` int(11) default '0',
  `Billing` varchar(100) default '',
  `Instructions` varchar(100) default '',
  `ActualCloseDate` datetime default NULL,
  `IncludeForcastSale` enum('YES','NO') NOT NULL default 'NO',
  `OrderIsGenerated` enum('YES','NO') NOT NULL default 'NO',
  `termid` int(11) unsigned default NULL,
  `individualid` int(11) unsigned default NULL,
  `orderID` int(11) unsigned default '0',
  PRIMARY KEY  (`ProposalID`),
  KEY `OpportunityID` (`OpportunityID`),
  KEY `Owner` (`Owner`),
  KEY `TypeID` (`TypeID`),
  KEY `Status` (`Status`),
  KEY `Stage` (`Stage`),
  KEY `Probability` (`Probability`),
  KEY `Source` (`Source`),
  KEY `AccountManager` (`AccountManager`),
  KEY `AccountTeam` (`AccountTeam`),
  KEY `CreatedBy` (`CreatedBy`),
  KEY `ModifiedBy` (`ModifiedBy`),
  KEY `Shippingid` (`Shippingid`),
  KEY `Billingid` (`Billingid`)
) TYPE=InnoDB;

--
-- Dumping data for table `proposal`
--


--
-- Table structure for table `proposalitem`
--

DROP TABLE IF EXISTS `proposalitem`;
CREATE TABLE `proposalitem` (
  `Name` varchar(25) default NULL,
  `ProposalID` int(11) default NULL,
  `ProposalLineID` int(11) unsigned NOT NULL auto_increment,
  `ItemID` int(11) unsigned default NULL,
  `Description` text,
  `Quantity` int(11) unsigned default '0',
  `Price` float(9,3) default NULL,
  `SKU` varchar(50) NOT NULL default '',
  `status` enum('Active','Deleted') NOT NULL default 'Active',
  `TransactionID` int(11) NOT NULL default '0',
  `taxAmount` float NOT NULL default '0',
  PRIMARY KEY  (`ProposalLineID`),
  KEY `ProposalID` (`ProposalID`),
  KEY `ItemID` (`ItemID`)
) TYPE=InnoDB;

--
-- Dumping data for table `proposalitem`
--


--
-- Table structure for table `proposallink`
--

DROP TABLE IF EXISTS `proposallink`;
CREATE TABLE `proposallink` (
  `ProposalID` int(11) unsigned default NULL,
  `RecordTypeID` int(11) unsigned default NULL,
  `RecordID` int(11) unsigned default NULL,
  KEY `ProposalID` (`ProposalID`),
  KEY `RecordTypeID` (`RecordTypeID`),
  KEY `RecordID` (`RecordID`)
) TYPE=InnoDB;

--
-- Dumping data for table `proposallink`
--


--
-- Table structure for table `ptartifact`
--

DROP TABLE IF EXISTS `ptartifact`;
CREATE TABLE `ptartifact` (
  `artifactid` int(10) unsigned NOT NULL auto_increment,
  `artifactname` char(25) NOT NULL default '',
  PRIMARY KEY  (`artifactid`)
) TYPE=InnoDB;

--
-- Dumping data for table `ptartifact`
--

INSERT INTO `ptartifact` (`artifactid`, `artifactname`) VALUES (1,'file'),(2,'entity'),(3,'Individual');

--
-- Table structure for table `ptcategory`
--

DROP TABLE IF EXISTS `ptcategory`;
CREATE TABLE `ptcategory` (
  `ptcategoryid` int(11) unsigned NOT NULL auto_increment,
  `catname` varchar(250) NOT NULL default '',
  `artifactid` int(11) unsigned NOT NULL default '0',
  `parentid` int(11) unsigned NOT NULL default '0',
  `cattype` enum('PRINT','EMAIL','FAX') NOT NULL default 'PRINT',
  PRIMARY KEY  (`ptcategoryid`),
  KEY `artifactid` (`artifactid`),
  KEY `parentid` (`parentid`)
) TYPE=InnoDB;

--
-- Dumping data for table `ptcategory`
--

INSERT INTO `ptcategory` (`ptcategoryid`, `catname`, `artifactid`, `parentid`, `cattype`) VALUES (1,'Print',1,4,'PRINT'),(2,'Email',2,4,'EMAIL'),(3,'Proposal',2,4,'PRINT'),(4,'Ticket',2,4,'PRINT');

--
-- Table structure for table `ptdetail`
--

DROP TABLE IF EXISTS `ptdetail`;
CREATE TABLE `ptdetail` (
  `ptdetailid` int(11) unsigned NOT NULL auto_increment,
  `ptcategoryid` int(11) unsigned NOT NULL default '0',
  `isdefault` enum('YES','NO') NOT NULL default 'YES',
  `ptdata` text NOT NULL,
  `ptname` varchar(250) NOT NULL default '',
  `userid` int(11) unsigned NOT NULL default '0',
  `artifactid` int(11) unsigned NOT NULL default '0',
  `pttype` varchar(25) default NULL,
  `ptsubject` varchar(250) default NULL,
  PRIMARY KEY  (`ptdetailid`),
  KEY `ptcategoryid` (`ptcategoryid`),
  KEY `userid` (`userid`),
  KEY `artifactid` (`artifactid`)
) TYPE=InnoDB COMMENT='InnoDB free: 68608 kB; InnoDB free: 75776 kB';

--
-- Dumping data for table `ptdetail`
--

INSERT INTO `ptdetail` (`ptdetailid`, `ptcategoryid`, `isdefault`, `ptdata`, `ptname`, `userid`, `artifactid`, `pttype`, `ptsubject`) VALUES (1,1,'YES','<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&lt;EntityName/&gt;<?xml:namespace prefix = o ns = \"urn:schemas-microsoft-com:office:office\" /><o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&lt;Address/&gt;<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&nbsp;<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>date <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&nbsp;<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>Dear &lt;FirstName/&gt; <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><FONT face=\"Times New Roman\"><FONT size=3>&nbsp;<o:p></o:p></FONT></FONT></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt; TEXT-ALIGN: center\" align=center><FONT size=3><B style=\"mso-bidi-font-weight: normal\"><SPAN style=\"FONT-FAMILY: Tahoma\">attention grabbing heading</SPAN></B><SPAN style=\"FONT-FAMILY: Tahoma\"> (up to 10 words)<o:p></o:p></SPAN></FONT></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&nbsp;<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>(the heading must grab attention - something your prospect will relate to that your proposition will produce - for example, <B style=\"mso-bidi-font-weight: normal\">Cost-Effective Sales Enquiry Generation</B>, or <B style=\"mso-bidi-font-weight: normal\">Reduce Your Staffing Overheads</B>, or <B style=\"mso-bidi-font-weight: normal\">Fast-Track Management Training</B>) <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><FONT face=\"Times New Roman\"><FONT size=3>&nbsp;<o:p></o:p></FONT></FONT></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>It has come to my attention that you may be in the market for (product/service). I welcome the opportunity to gain a better understanding of your requirements, and to work together with you to determine if our product/service will help you achieve your goals.<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><FONT face=\"Times New Roman\"><FONT size=3>&nbsp;<o:p></o:p></FONT></FONT></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>(Optional) Our customers include (two or three examples, relevant and known to the prospect), who have found (state key benefit, % savings, strategic advantage) from working with us. <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&nbsp;<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>I will telephone you soon to agree a future contact time that suits you/your own review timescale. <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&nbsp;<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>Yours sincerely <o:p></o:p></FONT></SPAN></P>\r\n<P><SPAN style=\"FONT-SIZE: 12pt; FONT-FAMILY: Tahoma; mso-fareast-font-family: \'Arial Unicode MS\'; mso-ansi-language: EN-US; mso-bidi-language: AR-SA\">(Name)</SPAN></P>','Sample Template',1,2,'PRINT','hello'),(2,3,'YES','<html>\n<head>\n  <title>Untitled Document</title>\n</head>\n<body>\n<table width=\"100%%\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\">\n  <tr>\n    <td width=\"50%\" align=\"left\" valign=\"top\">\n      <table width=\"100%%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n        <tr>\n          <td width=\"50%\" align=\"left\" valign=\"top\">&lt;OwningEntity/&gt;</td>\n          <td width=\"50%\" valign=\"top\"><div align=\"right\"><font size=\"+2\" face=\"Arial, Helvetica, sans-serif\"><strong>PROPOSAL</strong></font></div></td>\n        </tr>\n      </table>\n      <br>\n      <hr>\n      <table width=\"100%%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n        <tr align=\"left\" valign=\"top\">\n          <td width=\"50%\"><p><font face=\"Arial, Helvetica, sans-serif\"><strong>Bill To:</strong></font></p>&lt;BillTo/&gt;</td>\n          <td width=\"50%\"><div align=\"left\"><p><font face=\"Arial, Helvetica, sans-serif\"><strong>Ship To:</strong></font></p>&lt;ShipTo/&gt;</div></td>\n        </tr>\n      </table>\n      <hr>\n      <br>\n      <table width=\"100%%\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\">\n        <tr align=\"left\" valign=\"top\" bgcolor=\"#CCCCCC\">\n          <td width=\"25%\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">Account Manager</font></td>\n          <td width=\"25%\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">Purchase Order</font></td>\n          <td width=\"25%\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">Payment Terms</font></td>\n          <td width=\"25%\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">Date</font></td>\n        </tr>\n        <tr align=\"left\" valign=\"top\">\n          <td width=\"25%\">&nbsp;&lt;AccountManager/&gt;</td>\n          <td width=\"25%\">&nbsp;&lt;PurchaseOrder/&gt;</td>\n          <td width=\"25%\">&nbsp;&lt;PaymentTerms/&gt;</td>\n          <td width=\"25%\">&nbsp;&lt;Date/&gt;</td>\n        </tr>\n      </table>\n      <br>\n      <table width=\"100%%\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\">\n        <tr>\n          <td width=\"15%\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">ITEM</font></td>\n          <td width=\"40%\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">Description</font></td>\n          <td width=\"15%\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">Quantity</font></td>\n          <td width=\"15%\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\" align=\"right\">Price</font></td>\n          <td width=\"15%\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\" align=\"right\">Extended Price</font></td>\n        </tr>\n      </table>\n      <table width=\"100%%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n        <tr>\n          <td>&lt;ProposalDetails/&gt;</td>\n        </tr>\n      </table>\n      <table width=\"100%%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n        <tr>\n          <td width=\"15%\" bordercolor=\"#FFFFFF\">&nbsp;</td>\n          <td width=\"40%\" bordercolor=\"#FFFFFF\">&nbsp;</td>\n          <td width=\"15%\" bordercolor=\"#FFFFFF\">&nbsp;</td>\n          <td>\n            <table width=\"100%%\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\">\n              <tr>\n                <td width=\"50%\" bgcolor=\"#CCCCCC\"><div align=\"right\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\"><strong>Sub Total</strong></font></div></td>\n                <td width=\"50%\"><div align=\"right\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">&lt;SubTotal/&gt;</font></div></td>\n              </tr>\n              <tr>\n                <td width=\"50%\" bgcolor=\"#CCCCCC\"><div align=\"right\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\"><strong>Tax</strong></font></div></td>\n                <td width=\"50%\"><div align=\"right\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">&lt;Tax/&gt;</font></div></td>\n              </tr>\n              <tr>\n                <td width=\"50%\" bgcolor=\"#CCCCCC\"><div align=\"right\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\"><strong>Total</strong></font></div></td>\n                <td width=\"50%\"><div align=\"right\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">&lt;Total/&gt;</font></div></td>\n              </tr>\n            </table>\n          </td>\n        </tr>\n      </table>\n      <p>Special Instructions:</p>\n      <table width=\"400\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\">\n        <tr>\n          <td bgcolor=\"#CCCCCC\"><p>&lt;Notes/&gt;</p><p>&nbsp;</p></td>\n        </tr>\n      </table>\n    </td>\n  </tr>\n</table>\n<p>&nbsp;</p>\n</body>\n</html>','ProposalTemplate',2,2,'PROPOSAL','Proposal'),(3,4,'YES','<HTML>\n<HEAD>\n<TITLE> Ticket Template </TITLE>\n</HEAD>\n<STYLE>\n.popupTableText\n{\n    FONT-SIZE: 65%;\n    COLOR: #334a60;\n    height: 3%;\n    font-family: verdana, arial, sans-serif\n}\n\n.popupTableHead\n{\n    PADDING-RIGHT: 2px;\n    PADDING-LEFT: 2px;\n    FONT-WEIGHT: bold;\n    FONT-SIZE: 65%;\n    height: 3%;\n    COLOR: #334a60;\n    font-family: verdana, arial, sans-serif;\n    BACKGROUND-COLOR: #9ebed1\n}\n\n.popupTableHeadText\n{\n    PADDING-RIGHT: 2px;\n    PADDING-LEFT: 2px;\n    FONT-WEIGHT: bold;\n    FONT-SIZE: 65%;\n    height: 3%;\n    COLOR: #334a60;\n    font-family: verdana, arial, sans-serif\n}\n\n.searchBuilder\n{\n    COLOR: #FFFFFF;\n    font-family: verdana, arial, sans-serif;\n    FONT-WEIGHT: bold;\n    FONT-SIZE: 65%;\n    PADDING-LEFT: 3px;\n    BACKGROUND-COLOR: #7c91b2;\n}\n\n.rowOdd\n{\n    FONT-WEIGHT: bold;\n    FONT-SIZE: 80%;\n    COLOR: #334a60;\n    font-family: verdana, arial, sans-serif;\n}\n\n.rowEven\n{\n    FONT-WEIGHT: bold;\n    FONT-SIZE: 80%;\n    COLOR: #334a60;\n    font-family: verdana, arial, sans-serif;\n}\n\n.contactTableHeadShadow\n{\n    BACKGROUND-COLOR: #7d93b2\n}\n.contactTableHeadShadowLt\n{\n    BACKGROUND-COLOR: #b1d0dc\n}\n.contactTableHeadBottom\n{\n    BACKGROUND-COLOR: #7d93b2\n}\n</STYLE>\n<BODY  marginheight=\"0\" marginwidth=\"0\" topmargin=\"0\" bottommargin=\"0\" leftmargin=\"0\" rightmargin=\"0\" bgcolor=\"#d8dfea\">\n<TABLE width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n<TR>\n                                    <TD colspan=\"2\" >&nbsp;</TD>\n</TR>\n<TR>\n                                    <TD class=\"popupTableText\" >\n                                    <FONT SIZE=\"5\" COLOR=\"#000066\">--Name--</FONT><br>\n                                    --Contact--<br>\n                                    --Address--<br>\n                                    --Email--<br>\n                                    --Phone--<br><br>\n                                    </TD>\n                                    <TD valign=\"top\" align=\"Right\" ><font Color=\"#000066\"><H1>Ticket</H1></font></TD>\n</TR>\n<TR>\n                                    <TD colspan=\"2\" >&nbsp;</TD>\n</TR>\n<TR>\n            <TD valign=\"top\" >\n                        <TABLE width=\"100%\"  cellspacing=\"0\" cellpadding=\"0\"  >\n                        <TR>\n                                    <TD class=\"popupTableHead\"  colspan=\"2\" >Ticket Details</TD>\n                        </TR>\n                        <TR>\n                                    <TD class=\"popupTableText\">Ticket Number:</TD>\n                                    <TD class=\"popupTableText\">--TicketNumber--</TD>\n                        </TR>\n                        <TR>\n                                    <TD class=\"popupTableText\">Status:</TD>\n                                    <TD class=\"popupTableText\">--Status--</TD>\n                        </TR>\n                        <TR>\n                                    <TD class=\"popupTableText\">Priority:</TD>\n                                    <TD class=\"popupTableText\">--Priority--</TD>\n                        </TR>\n                        <TR>\n                                    <TD class=\"popupTableText\">Subject:</TD>\n                                    <TD class=\"popupTableText\">--Subject--</TD>\n                        </TR>\n                        <TR>\n                                    <TD class=\"popupTableText\">Details:</TD>\n                                    <TD class=\"popupTableText\">--Details--</TD>\n                        </TR>\n                        <TR>\n                                    <TD class=\"popupTableHead\" colspan=\"2\">Responsibility</TD>\n                        </TR>\n                        <TR>\n                                    <TD class=\"popupTableText\">Manager:</TD>\n                                    <TD class=\"popupTableText\">--Manager--</TD>\n                        </TR>\n                        <TR>\n                                    <TD class=\"popupTableText\">Assigned To:</TD>\n                                    <TD class=\"popupTableText\">--AssignedTo--</TD>\n                        </TR>\n                        </TABLE>\n            </TD>\n            <TD valign=\"top\">\n                        <TABLE width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" >\n                        <TR>\n                                    <TD class=\"popupTableHead\" colspan=\"2\">Custom Fields</TD>\n                        </TR>\n                        --CustomFields--\n                        </TABLE>\n            </TD>\n</TR>\n<tr><td colspan=\"2\">&nbsp;</td>\n</tr>\n<TR>\n            <TD colspan=\"2\">\n            <table>\n                        <tr height=\"20\">\n                                    <td width=\"11\">&nbsp;</td>\n                                    <td class=\"searchBuilder\">\n                                      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n                                                <tr>\n                                                  <td class=\"searchBuilder\">Threads</td>\n                                                  <td class=\"searchBuilder\" style=\"padding:2px;padding-right:10px;\" align=\"right\">\n                                                  </td>\n                                                </tr>\n                                      </table>\n                                    </td>\n                                    <td width=\"11\"></td>\n                        </tr>\n                        <tr>\n                                    <td width=\"11\"></td>\n                                    <td width=\"100%\">\n                                                <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\">\n                                                            <tr height=\"1\">\n                                                                        <td class=\"contactTableHeadShadow\" colspan=\"5\"></td>\n                                                            </tr>\n                                                            <tr height=\"1\">\n                                                                        <td class=\"contactTableHeadShadowLt\" colspan=\"5\"></td>\n                                                            </tr>\n                                                            <tr>\n                                                                        <td class=\"popupTableHead\">Title</td>\n                                                                        <td class=\"popupTableHead\">Data</td>\n                                                                        <td class=\"popupTableHead\">Date</td>\n                                                                        <td class=\"popupTableHead\">Priority</td>\n                                                                        <td class=\"popupTableHead\">Created By</td>\n                                                            </tr>\n                                                            <tr height=\"1\">\n                                                                        <td class=\"contactTableHeadBottom\" colspan=\"5\"></td>\n                                                            </tr>\n                                                            --Values--\n                                    </tr>\n                                    <tr height=\"1\">\n                                      <td class=\"contactTableHeadBottom\" colspan=\"5\"></td>\n                                    </tr>\n                          </table>\n                        </td>\n                        <td width=\"11\"></td>\n              </tr>\n            </table>\n            </TD>\n</TR>\n</TABLE>\n</BODY>\n</HTML>\n','TicketTemplate',2,2,'TICKET','Ticket'),(98,2,'YES','<P>Dear &lt;FirstName/&gt; &lt;LastName/&gt;,</P>\r\n<P>Thank you for your interest in our products and services. As we discussed, I will be sending you our company brochure and some sample products for your review. Please note that I will be sending this information to the following address:</P>\r\n<P>&lt;Company/&gt;<BR>Attn: &lt;FirstName/&gt; &lt;MiddleInitial/&gt; &lt;LastName/&gt;<BR>&lt;Title/&gt;<BR>&lt;PrimaryAddress/&gt;<BR>&lt;Street1/&gt;<BR>&lt;Street2/&gt;<BR>&lt;City/&gt;, &lt;State/&gt; &lt;Zip/&gt;<BR>&lt;Country/&gt;<BR>Please also help us to make certain we have the correction information on file for your account. Our database currently reflects the following information for your account:</P>\r\n<P>&lt;IndividualID/&gt;<BR>&lt;EntityID/&gt;<BR>&lt;Website/&gt;<BR>&lt;ExternalID/&gt;<BR>&lt;Source/&gt;<BR>&lt;Fax/&gt;<BR>&lt;Home/&gt;<BR>&lt;Main/&gt;<BR>&lt;Mobile/&gt;<BR>&lt;Other/&gt;<BR>&lt;Pager/&gt;<BR>&lt;Work/&gt;<BR>&lt;Email/&gt;</P>\r\n<P>Thank you again for your interest, and I look forward to our follow-up meeting next week. If you have any questions in the mean time, please don\'t hesistate to call me at (215) 555-1212 or send me an email.</P>\r\n<P><BR>Regards,</P>\r\n<P>My Name<BR>Company, Inc<BR><A href=\"mailto:email@example.com\">email@example.com</A></P>\r\n<P>- - - - -</P>\r\n<P>The information transmitted is intended only for the person or entity to which it is addressed and may contain confidential and/or privileged material. Any review, retransmission,&nbsp; dissemination or other use of, or taking of any action in reliance upon, this information by persons or entities other than the intended recipient is prohibited. If you received this in error, please contact the sender and delete the material from any computer.</P>\r\n<P>&nbsp;</P>','Sample Email Template',1,2,'EMAIL',''),(99,1,'YES','<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>Dear Mr Smith <?xml:namespace prefix = o ns = \"urn:schemas-microsoft-com:office:office\" /><o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><FONT size=3><B style=\"mso-bidi-font-weight: normal\"><SPAN style=\"FONT-FAMILY: Tahoma\">Sales Development</SPAN></B><SPAN style=\"FONT-FAMILY: Tahoma\"> <o:p></o:p></SPAN></FONT></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>Companies like yours in the (industry type) sector have achieved significant business growth following the introduction of certain new specialised software technology, by increasing customer visibility, and sales personnel accountability and productivity. <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>Typically, for an investment of less than $7,500 it is possible to achieve sales growth of 20-30% in year one, and 25-50% subsequent years. All directly attributable to this new technology, which focuses on and integrates: <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 0pt 35.35pt; TEXT-INDENT: -14.15pt; mso-list: l0 level1 lfo1; tab-stops: list 35.35pt\"><SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: Symbol; mso-bidi-font-family: StarSymbol\">·<SPAN style=\"FONT: 7pt \'Times New Roman\'\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </SPAN></SPAN><FONT size=3><SPAN style=\"FONT-FAMILY: Tahoma\">New advanced contact management and salesforce automation</SPAN><FONT face=\"Times New Roman\"> </FONT></FONT></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 0pt 35.35pt; TEXT-INDENT: -14.15pt; mso-list: l0 level1 lfo1; tab-stops: list 35.35pt\"><SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: Symbol; mso-bidi-font-family: StarSymbol\">·<SPAN style=\"FONT: 7pt \'Times New Roman\'\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </SPAN></SPAN><FONT size=3><SPAN style=\"FONT-FAMILY: Tahoma\">Provides centralized management of all customer information, transactions and communications</SPAN><FONT face=\"Times New Roman\"> </FONT></FONT></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 0pt 35.35pt; TEXT-INDENT: -14.15pt; mso-list: l0 level1 lfo1; tab-stops: list 35.35pt\"><SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: Symbol; mso-bidi-font-family: StarSymbol\">·<SPAN style=\"FONT: 7pt \'Times New Roman\'\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </SPAN></SPAN><FONT size=3><SPAN style=\"FONT-FAMILY: Tahoma\">Refining and developing your propositions and market sector targeting</SPAN><FONT face=\"Times New Roman\"> </FONT></FONT></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt 35.35pt; TEXT-INDENT: -14.15pt; mso-list: l0 level1 lfo1; tab-stops: list 35.35pt\"><SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: Symbol; mso-bidi-font-family: StarSymbol\">·<SPAN style=\"FONT: 7pt \'Times New Roman\'\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </SPAN></SPAN><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>Time management improvements, especially selling-time optimization <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>If you are interested in assessing the potential and relevance of these ideas for your own business I\'m happy to talk on the phone first and will call you in the next few days. <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>Yours sincerely,<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&nbsp;<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>Your Name, Title<BR style=\"mso-special-character: line-break\"><BR style=\"mso-special-character: line-break\"><o:p></o:p></FONT></SPAN></P><SPAN style=\"FONT-SIZE: 12pt; FONT-FAMILY: Tahoma; mso-fareast-font-family: \'Arial Unicode MS\'; mso-ansi-language: EN-US; mso-bidi-language: AR-SA\">Company Name<BR>(215)555-1212<BR>Email@example.com</SPAN>','Sample Template 2',1,2,'PRINT',''),(100,1,'YES','<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&lt;EntityName/&gt;<?xml:namespace prefix = o ns = \"urn:schemas-microsoft-com:office:office\" /><o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&lt;Address/&gt;<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&nbsp;<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>date <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&nbsp;<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>Dear &lt;FirstName/&gt; <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><FONT face=\"Times New Roman\"><FONT size=3>&nbsp;<o:p></o:p></FONT></FONT></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt; TEXT-ALIGN: center\" align=center><FONT size=3><B style=\"mso-bidi-font-weight: normal\"><SPAN style=\"FONT-FAMILY: Tahoma\">attention grabbing heading</SPAN></B><SPAN style=\"FONT-FAMILY: Tahoma\"> (up to 10 words)<o:p></o:p></SPAN></FONT></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&nbsp;<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>(the heading must grab attention - something your prospect will relate to that your proposition will produce - for example, <B style=\"mso-bidi-font-weight: normal\">Cost-Effective Sales Enquiry Generation</B>, or <B style=\"mso-bidi-font-weight: normal\">Reduce Your Staffing Overheads</B>, or <B style=\"mso-bidi-font-weight: normal\">Fast-Track Management Training</B>) <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><FONT face=\"Times New Roman\"><FONT size=3>&nbsp;<o:p></o:p></FONT></FONT></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>It has come to my attention that you may be in the market for (product/service). I welcome the opportunity to gain a better understanding of your requirements, and to work together with you to determine if our product/service will help you achieve your goals.<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><FONT face=\"Times New Roman\"><FONT size=3>&nbsp;<o:p></o:p></FONT></FONT></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>(Optional) Our customers include (two or three examples, relevant and known to the prospect), who have found (state key benefit, % savings, strategic advantage) from working with us. <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&nbsp;<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>I will telephone you soon to agree a future contact time that suits you/your own review timescale. <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&nbsp;<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>Yours sincerely <o:p></o:p></FONT></SPAN></P><SPAN style=\"FONT-SIZE: 12pt; FONT-FAMILY: Tahoma; mso-fareast-font-family: \'Arial Unicode MS\'; mso-ansi-language: EN-US; mso-bidi-language: AR-SA\">(Name)</SPAN>','Sample Template 3',1,2,'PRINT',''),(101,2,'YES','\r\n<META content=\"OpenOffice.org 1.1.0  (Win32)\" name=GENERATOR>\r\n<META content=\"Alan Rihm\" name=AUTHOR>\r\n<META content=20040210;21092896 name=CREATED>\r\n<META content=\"Alan Rihm\" name=CHANGEDBY>\r\n<META content=20040210;21340454 name=CHANGED>\r\n<STYLE>\r\n	<!--\r\n		@page { size: 8.5in 11in; margin-left: 1.25in; margin-right: 1.25in; margin-top: 1in; margin-bottom: 1in }\r\n		P { margin-bottom: 0.08in }\r\n		TD P { margin-bottom: 0.08in }\r\n	-->\r\n	</STYLE>\r\n\r\n<P style=\"MARGIN-BOTTOM: 0in\"><BR></P>\r\n<CENTER>\r\n<TABLE borderColor=#336699 cellSpacing=2 cellPadding=15 width=600 border=2>\r\n<TBODY>\r\n<TR>\r\n<TD bgColor=#336699 colSpan=2>\r\n<P align=center><FONT color=#ffffff><FONT face=\"verdana, arial\"><B><FONT size=4>Newsletter Name Here </FONT></B><BR>Subtitle Here | Volume # Here | Date Here </FONT></FONT></P></TD></TR>\r\n<TR>\r\n<TD bgColor=#ffffff colSpan=2>\r\n<P><FONT face=\"verdana, arial\">Descriptive text here. Enter your descriptive text here. Descriptive text here. Enter your descriptive text here. Descriptive text here. Enter your descriptive text here. Descriptive text here. Enter your descriptive text here. Descriptive text here. Enter your descriptive text here. </FONT></P></TD></TR>\r\n<TR>\r\n<TD width=200>\r\n<P align=center><IMG height=134 src=\"http://www.lyris.com/img/template_sample_135x135.gif\" width=135 align=bottom border=0 name=Graphic1> </P></TD>\r\n<TD width=400>\r\n<P><FONT color=#336699><FONT face=\"verdana, arial\"><FONT size=4>Announcement! </FONT></FONT></FONT></P>\r\n<P><FONT face=\"verdana, arial\">Announcement text here. Enter your announcement text here. Announcement text here. Enter your announcement text here. Announcement text here. Enter your announcement text here. </FONT></P>\r\n<P><FONT face=\"verdana, arial\"><A href=\"http://www.example.com/\">Name Your Link Here</A> </FONT></P></TD></TR>\r\n<TR>\r\n<TD colSpan=2>\r\n<P><FONT face=\"verdana, arial\"><B><FONT color=#336699>Bold Text #1 </FONT></B><BR>Descriptive text #1 here. Enter your descriptive text #1 here. Descriptive text #1 here. Enter your descriptive text #1 here. Descriptive text #1 here. Enter your descriptive text #1 here. Descriptive text #1 here. Enter your descriptive text #1 here. </FONT></P>\r\n<P><FONT face=\"verdana, arial\"><B><FONT color=#336699>Bold Text #2 </FONT></B><BR>Descriptive text #2 here. Enter your descriptive text #2 here. Descriptive text #2 here. Enter your descriptive text #2 here. Descriptive text #2 here. Enter your descriptive text #2 here. Descriptive text #2 here. Enter your descriptive text #2 here. </FONT></P>\r\n<P><FONT face=\"verdana, arial\"><B><FONT color=#336699>Bold Text #3 </FONT></B><BR>Descriptive text #3 here. Enter your descriptive text #3 here. Descriptive text #3 here. Enter your descriptive text #3 here. Descriptive text #3 here. Enter your descriptive text #3 here. Descriptive text #3 here. Enter your descriptive text #3 here. </FONT></P></TD></TR>\r\n<TR>\r\n<TD colSpan=2>\r\n<P align=center><FONT color=#336699><FONT face=\"verdana, arial\"><FONT size=2>Small footer text here. Enter your small footer text here. Small footer text here. Enter your small footer text here. Small footer text here. </FONT></FONT></FONT></P></TD></TR></TBODY></TABLE></CENTER>\r\n<P style=\"MARGIN-BOTTOM: 0in\"><BR></P>','Sample HTML Email',1,2,'EMAIL',''),(102,2,'YES','\r\n<META content=\"OpenOffice.org 1.1.0  (Win32)\" name=GENERATOR>\r\n<META content=\"Alan Rihm\" name=AUTHOR>\r\n<META content=20040210;21385198 name=CREATED>\r\n<META content=\"Alan Rihm\" name=CHANGEDBY>\r\n<META content=20040210;21392016 name=CHANGED>\r\n<P><BR><BR></P>\r\n<CENTER>\r\n<TABLE borderColor=#336633 cellSpacing=0 cellPadding=10 width=600 bgColor=#99cc99 border=2>\r\n<TBODY>\r\n<TR vAlign=top>\r\n<TD width=\"25%\">\r\n<P><FONT face=arial><FONT size=2><B>Sidebar Title Here </B></FONT></FONT></P>\r\n<P><FONT size=2><FONT face=arial><A href=\"http://www.example.com/\">Name Your Link Here</A><BR><A href=\"http://www.example.com/\">Name Your Link Here</A><BR><A href=\"http://www.example.com/\">Name Your Link Here</A><BR><A href=\"http://www.example.com/\">Name Your Link Here</A><BR><A href=\"http://www.example.com/\">Name Your Link Here</A><BR><A href=\"http://www.example.com/\">Name Your Link Here</A><BR><A href=\"http://www.example.com/\">Name Your Link Here</A><BR><A href=\"http://www.example.com/\">Name Your Link Here</A><BR><A href=\"http://www.example.com/\">Name Your Link Here</A></FONT></FONT></P></TD>\r\n<TD width=\"75%\" bgColor=#ffffff>\r\n<P><FONT color=#336633><FONT face=arial><FONT size=4><B>Announcement Title Here! </B></FONT></FONT></FONT></P>\r\n<P><A href=\"http://www.example.com/\"><FONT color=#000080><IMG height=112 src=\"http://www.lyris.com/img/template_sample_97x112.gif\" width=97 align=right border=1 name=Graphic1></FONT></A><FONT face=arial>Descriptive text #1 here. Enter your descriptive text #1 here. Descriptive text #1 here. Enter your descriptive text #1 here. Descriptive text #1 here. Descriptive text #1 here. Enter your descriptive text #1 here. Descriptive text #1 here. Enter your descriptive text #1 here. Descriptive text #1 here. </FONT></P>\r\n<P><FONT color=#336633><FONT face=arial><B>Bold Text #1 Here </B></FONT></FONT></P>\r\n<UL>\r\n<LI>\r\n<P style=\"MARGIN-BOTTOM: 0in\"><FONT face=arial>Bullet text here, bullet text here </FONT></P>\r\n<LI>\r\n<P style=\"MARGIN-BOTTOM: 0in\"><FONT face=arial>Bullet text here, bullet text here </FONT></P>\r\n<LI>\r\n<P style=\"MARGIN-BOTTOM: 0in\"><FONT face=arial>Bullet text here, bullet text here </FONT></P>\r\n<LI>\r\n<P style=\"MARGIN-BOTTOM: 0in\"><FONT face=arial>Bullet text here, bullet text here </FONT></P>\r\n<LI>\r\n<P style=\"MARGIN-BOTTOM: 0in\"><FONT face=arial>Bullet text here, bullet text here </FONT></P>\r\n<LI>\r\n<P><FONT face=arial>Bullet text here, bullet text here </FONT></P></LI></UL>\r\n<P><FONT color=#336633><FONT face=arial><B>Bold Text #2 Here </B></FONT></FONT></P>\r\n<P><FONT face=arial>Descriptive text #2 here. Enter your descriptive text #2 here. Descriptive text #2 here. Enter your descriptive text #2 here. Descriptive text #2 here. Descriptive text #2 here. Enter your descriptive text #2 here. Descriptive text #2 here. Enter your descriptive text #2 here. Descriptive text #2 here. </FONT></P>\r\n<P align=center><FONT face=arial><A href=\"http://www.example.com/\"><B>Name Your Link Here</B></A> </FONT></P></TD></TR>\r\n<TR>\r\n<TD bgColor=#ffffff colSpan=2></TD></TR></TBODY></TABLE></CENTER>\r\n<P><BR><BR></P>','Sample HTML Email 2',1,2,'EMAIL','');

--
-- Table structure for table `publicrecords`
--

DROP TABLE IF EXISTS `publicrecords`;
CREATE TABLE `publicrecords` (
  `recordid` int(11) NOT NULL default '0',
  `moduleid` int(11) NOT NULL default '0',
  KEY `recordid` (`recordid`),
  KEY `moduleid` (`moduleid`)
) TYPE=InnoDB;

--
-- Dumping data for table `publicrecords`
--

INSERT INTO `publicrecords` (`recordid`, `moduleid`) VALUES (1,14),(1,15),(1,32),(1,78),(2,14),(10,76);

--
-- Table structure for table `purchaseorder`
--

DROP TABLE IF EXISTS `purchaseorder`;
CREATE TABLE `purchaseorder` (
  `PurchaseOrderID` int(10) unsigned NOT NULL auto_increment,
  `ExternalID` int(10) unsigned NOT NULL default '0',
  `Title` varchar(50) NOT NULL default '',
  `Description` text NOT NULL,
  `PONumber` varchar(50) NOT NULL default '',
  `Entity` int(10) unsigned NOT NULL default '0',
  `SubTotal` float NOT NULL default '0',
  `Shipping` float NOT NULL default '0',
  `Tax` float NOT NULL default '0',
  `Discount` float NOT NULL default '0',
  `Total` float NOT NULL default '0',
  `ShipIndividual` int(10) unsigned NOT NULL default '0',
  `ShipAddress` int(10) unsigned NOT NULL default '0',
  `BillIndividual` int(10) unsigned NOT NULL default '0',
  `BillAddress` int(10) unsigned NOT NULL default '0',
  `Modified` timestamp(14) NOT NULL,
  `Status` int(10) unsigned NOT NULL default '0',
  `Owner` int(10) unsigned NOT NULL default '0',
  `Creator` int(10) unsigned NOT NULL default '0',
  `ModifiedBy` int(10) unsigned NOT NULL default '0',
  `terms` int(10) unsigned NOT NULL default '0',
  `accountmgr` int(10) unsigned NOT NULL default '0',
  `purchaseorderdate` date default '0000-00-00',
  `purchaseorderstatus` enum('Active','Deleted') NOT NULL default 'Active',
  `Created` timestamp(14) NOT NULL default '00000000000000',
  PRIMARY KEY  (`PurchaseOrderID`),
  KEY `Entity` (`Entity`),
  KEY `ShipIndividual` (`ShipIndividual`),
  KEY `ShipAddress` (`ShipAddress`),
  KEY `BillIndividual` (`BillIndividual`),
  KEY `BillAddress` (`BillAddress`),
  KEY `Status` (`Status`),
  KEY `Owner` (`Owner`),
  KEY `Creator` (`Creator`),
  KEY `ModifiedBy` (`ModifiedBy`),
  KEY `terms` (`terms`),
  KEY `accountmgr` (`accountmgr`)
) TYPE=InnoDB;

--
-- Dumping data for table `purchaseorder`
--


--
-- Table structure for table `purchaseorderitem`
--

DROP TABLE IF EXISTS `purchaseorderitem`;
CREATE TABLE `purchaseorderitem` (
  `purchaseorderlineid` int(10) unsigned NOT NULL auto_increment,
  `purchaseorderid` int(10) unsigned NOT NULL default '0',
  `itemid` int(11) unsigned NOT NULL default '0',
  `quantity` int(11) unsigned NOT NULL default '0',
  `price` float unsigned NOT NULL default '0',
  `sku` varchar(25) NOT NULL default '',
  `description` varchar(100) NOT NULL default '',
  `status` enum('Active','Deleted') NOT NULL default 'Active',
  PRIMARY KEY  (`purchaseorderlineid`,`itemid`,`purchaseorderid`)
) TYPE=InnoDB;

--
-- Dumping data for table `purchaseorderitem`
--


--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
  `questionid` int(11) unsigned NOT NULL auto_increment,
  `faqid` int(11) unsigned NOT NULL default '0',
  `question` text NOT NULL,
  `answer` text,
  PRIMARY KEY  (`questionid`),
  KEY `faqid` (`faqid`)
) TYPE=InnoDB;

--
-- Dumping data for table `question`
--


--
-- Table structure for table `record`
--

DROP TABLE IF EXISTS `record`;
CREATE TABLE `record` (
  `RecordID` int(11) unsigned NOT NULL auto_increment,
  `ParentID` int(11) unsigned default NULL,
  `Name` varchar(25) default NULL,
  PRIMARY KEY  (`RecordID`),
  KEY `ParentID` (`ParentID`)
) TYPE=InnoDB;

--
-- Dumping data for table `record`
--

INSERT INTO `record` (`RecordID`, `ParentID`, `Name`) VALUES (289,1,'fff'),(298,1,'nnn'),(299,1,'hhh'),(440,2,'bbb'),(597,2,'kkk');

--
-- Table structure for table `recordauthorisation`
--

DROP TABLE IF EXISTS `recordauthorisation`;
CREATE TABLE `recordauthorisation` (
  `individualid` int(11) unsigned default NULL,
  `recordid` int(11) unsigned default NULL,
  `recordtypeid` int(11) unsigned default NULL,
  `privilegelevel` tinyint(4) NOT NULL default '0',
  KEY `individualid` (`individualid`),
  KEY `recordid` (`recordid`),
  KEY `recordtypeid` (`recordtypeid`)
) TYPE=InnoDB;

--
-- Dumping data for table `recordauthorisation`
--

INSERT INTO `recordauthorisation` (`individualid`, `recordid`, `recordtypeid`, `privilegelevel`) VALUES (1,9,76,20),(1,2,14,10);

--
-- Table structure for table `recurexcept`
--

DROP TABLE IF EXISTS `recurexcept`;
CREATE TABLE `recurexcept` (
  `RecurrenceID` int(11) unsigned NOT NULL default '0',
  `Exception` date NOT NULL default '0000-00-00',
  KEY `RecurrenceID` (`RecurrenceID`)
) TYPE=InnoDB;

--
-- Dumping data for table `recurexcept`
--


--
-- Table structure for table `recurrence`
--

DROP TABLE IF EXISTS `recurrence`;
CREATE TABLE `recurrence` (
  `RecurrenceID` int(10) unsigned NOT NULL auto_increment,
  `ActivityID` int(11) unsigned NOT NULL default '0',
  `Every` int(11) default NULL,
  `TimePeriod` enum('DAY','WEEK','MONTH','YEAR') default NULL,
  `Until` date default NULL,
  `startdate` date default NULL,
  `RecurrOn` tinyint(4) default NULL,
  PRIMARY KEY  (`RecurrenceID`),
  UNIQUE KEY `RecurrenceID` (`RecurrenceID`),
  KEY `ActivityID` (`ActivityID`)
) TYPE=InnoDB;

--
-- Dumping data for table `recurrence`
--


--
-- Table structure for table `report`
--

DROP TABLE IF EXISTS `report`;
CREATE TABLE `report` (
  `ReportId` int(10) unsigned NOT NULL auto_increment,
  `ModuleId` int(10) unsigned NOT NULL default '0',
  `Name` varchar(55) NOT NULL default '',
  `Description` text,
  `CreatedBy` int(10) NOT NULL default '0',
  `CreatedOn` datetime NOT NULL default '0000-00-00 00:00:00',
  `ModifiedBy` int(10) default NULL,
  `ModifiedOn` timestamp(14) NOT NULL,
  `ReportURL` varchar(100) default NULL,
  `ReportTypeId` int(4) NOT NULL default '0',
  `DateFrom` date default NULL,
  `DateTo` date default NULL,
  PRIMARY KEY  (`ReportId`),
  KEY `ModuleId` (`ModuleId`),
  KEY `ReportTypeId` (`ReportTypeId`),
  CONSTRAINT `0_21533` FOREIGN KEY (`ReportTypeId`) REFERENCES `reporttype` (`ReportTypeId`),
  CONSTRAINT `0_21534` FOREIGN KEY (`ModuleId`) REFERENCES `module` (`moduleid`)
) TYPE=InnoDB;

--
-- Dumping data for table `report`
--

INSERT INTO `report` (`ReportId`, `ModuleId`, `Name`, `Description`, `CreatedBy`, `CreatedOn`, `ModifiedBy`, `ModifiedOn`, `ReportURL`, `ReportTypeId`, `DateFrom`, `DateTo`) VALUES (100,14,'Contacts by state','Contacts by state, order by State, Zip Code, City',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL),(101,14,'Contacts with custom fields list','Contacts with custom fields list by account manager, order by Account Manager, Entity',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL),(102,52,'Expenses by user','Expenses by user, order by User, Expense Type',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL),(103,52,'Expenses by entity','Expenses by user, order by User, Expense Type',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL),(104,52,'Timeslips by user','Timeslips by user, order by User, Entity',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL),(105,52,'Timeslips by Entity','Timeslips by user, order by Entity, User',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL),(106,3,'Activities by user','Activities by user, order by User',1,'0000-00-00 00:00:00',NULL,20040308092154,NULL,1,NULL,NULL),(107,3,'Activities by entity','Activities by entity, order by Entity',1,'0000-00-00 00:00:00',NULL,20040308092154,NULL,1,NULL,NULL),(108,30,'Opportunitiess by user','Opportunities by user, order by Entity, Account Manager',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL),(109,30,'Proposal by user','Proposal by user, order by Entity, Account Manager',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL),(110,30,'Proposal detail by user','Proposal detail by user, order by Entity, Account Manager',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL),(111,30,'Sales order report','Sales order report, order by Entity, Account Manager',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL),(112,36,'Project summary','Project summary',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL),(114,39,'Support tickets per user','Support tickets per user',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL),(115,39,'Support tickets by entity','Support tickets by entity',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL),(116,39,'Support tickets list','Support tickets list',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL),(117,15,'Notes By Individual','Show all the notes for each individual',1,'0000-00-00 00:00:00',NULL,20050927102918,NULL,1,NULL,NULL);

--
-- Table structure for table `reportcontent`
--

DROP TABLE IF EXISTS `reportcontent`;
CREATE TABLE `reportcontent` (
  `ReportContentId` int(10) unsigned NOT NULL auto_increment,
  `ReportId` int(10) unsigned NOT NULL default '0',
  `FieldId` int(11) unsigned NOT NULL default '0',
  `SequenceNumber` tinyint(4) NOT NULL default '0',
  `SortOrder` enum('ASC','DESC') default NULL,
  `SortOrderSequence` tinyint(4) default NULL,
  `tableId` int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (`ReportContentId`),
  KEY `ReportId` (`ReportId`),
  KEY `FieldId` (`FieldId`),
  CONSTRAINT `0_21734` FOREIGN KEY (`FieldId`) REFERENCES `searchfield` (`SearchFieldID`),
  CONSTRAINT `0_21735` FOREIGN KEY (`ReportId`) REFERENCES `report` (`ReportId`) ON DELETE CASCADE
) TYPE=InnoDB;

--
-- Dumping data for table `reportcontent`
--


--
-- Table structure for table `reportmodule`
--

DROP TABLE IF EXISTS `reportmodule`;
CREATE TABLE `reportmodule` (
  `ModuleID` int(10) unsigned NOT NULL default '0',
  `SearchTableID` int(11) unsigned NOT NULL default '0',
  UNIQUE KEY `SearchModuleID` (`ModuleID`,`SearchTableID`),
  KEY `ModuleID` (`ModuleID`),
  KEY `SearchTableID` (`SearchTableID`),
  CONSTRAINT `0_21737` FOREIGN KEY (`ModuleID`) REFERENCES `module` (`moduleid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `0_21738` FOREIGN KEY (`SearchTableID`) REFERENCES `searchtable` (`SearchTableID`) ON DELETE CASCADE ON UPDATE CASCADE
) TYPE=InnoDB;

--
-- Dumping data for table `reportmodule`
--

INSERT INTO `reportmodule` (`ModuleID`, `SearchTableID`) VALUES (3,6),(14,1),(14,5),(15,2),(15,5),(30,10),(31,11),(33,21),(36,12),(37,13),(38,15),(39,5),(39,14),(42,18),(48,29),(52,36);

--
-- Table structure for table `reportsearch`
--

DROP TABLE IF EXISTS `reportsearch`;
CREATE TABLE `reportsearch` (
  `SearchID` int(11) unsigned NOT NULL default '0',
  `ReportID` int(11) unsigned NOT NULL default '0',
  UNIQUE KEY `ReportID` (`ReportID`),
  KEY `SearchID` (`SearchID`),
  CONSTRAINT `0_21645` FOREIGN KEY (`SearchID`) REFERENCES `search` (`SearchID`)
) TYPE=InnoDB;

--
-- Dumping data for table `reportsearch`
--


--
-- Table structure for table `reportsearchcriteria`
--

DROP TABLE IF EXISTS `reportsearchcriteria`;
CREATE TABLE `reportsearchcriteria` (
  `ReportSearchCriteriaId` int(10) unsigned NOT NULL auto_increment,
  `ReportId` int(10) unsigned NOT NULL default '0',
  `FieldId` int(11) unsigned NOT NULL default '0',
  `ExpressionId` int(4) NOT NULL default '0',
  `Value` varchar(25) default NULL,
  `AndOr` enum('AND','OR') NOT NULL default 'AND',
  `SequenceNumber` tinyint(4) NOT NULL default '0',
  PRIMARY KEY  (`ReportSearchCriteriaId`),
  KEY `ReportId` (`ReportId`),
  KEY `FieldId` (`FieldId`),
  KEY `ExpressionId` (`ExpressionId`),
  CONSTRAINT `0_21539` FOREIGN KEY (`ExpressionId`) REFERENCES `reportsearchexpression` (`ExpressionId`) ON DELETE CASCADE,
  CONSTRAINT `0_21540` FOREIGN KEY (`ReportId`) REFERENCES `report` (`ReportId`) ON DELETE CASCADE,
  CONSTRAINT `0_21541` FOREIGN KEY (`FieldId`) REFERENCES `field` (`fieldid`)
) TYPE=InnoDB;

--
-- Dumping data for table `reportsearchcriteria`
--


--
-- Table structure for table `reportsearchexpression`
--

DROP TABLE IF EXISTS `reportsearchexpression`;
CREATE TABLE `reportsearchexpression` (
  `ExpressionId` int(4) NOT NULL auto_increment,
  `Label` varchar(30) NOT NULL default '',
  `Clause` varchar(20) NOT NULL default '',
  `PreValue` varchar(20) NOT NULL default '',
  `PostValue` varchar(20) NOT NULL default '',
  PRIMARY KEY  (`ExpressionId`)
) TYPE=InnoDB;

--
-- Dumping data for table `reportsearchexpression`
--

INSERT INTO `reportsearchexpression` (`ExpressionId`, `Label`, `Clause`, `PreValue`, `PostValue`) VALUES (1,'begins with','like','\'','%\''),(2,'ends with','like','\'%','\''),(3,'contains','like','\'%','%\''),(4,'equals','=','',''),(5,'less than','<','',''),(6,'greater than','>','',''),(7,'less than equals','<=','',''),(8,'greater than equals','>=','','');

--
-- Table structure for table `reporttype`
--

DROP TABLE IF EXISTS `reporttype`;
CREATE TABLE `reporttype` (
  `ReportTypeId` int(4) NOT NULL auto_increment,
  `ReportTypeName` varchar(20) NOT NULL default '',
  PRIMARY KEY  (`ReportTypeId`),
  UNIQUE KEY `ReportTypeName` (`ReportTypeName`)
) TYPE=InnoDB;

--
-- Dumping data for table `reporttype`
--

INSERT INTO `reporttype` (`ReportTypeId`, `ReportTypeName`) VALUES (2,'Ad Hoc'),(3,'Custom'),(1,'Standard');

--
-- Table structure for table `resourcerelate`
--

DROP TABLE IF EXISTS `resourcerelate`;
CREATE TABLE `resourcerelate` (
  `ActivityID` int(11) unsigned default NULL,
  `ResourceID` int(11) unsigned default NULL,
  KEY `ActivityID` (`ActivityID`),
  KEY `ResourceID` (`ResourceID`)
) TYPE=InnoDB;

--
-- Dumping data for table `resourcerelate`
--


--
-- Table structure for table `restoresequence`
--

DROP TABLE IF EXISTS `restoresequence`;
CREATE TABLE `restoresequence` (
  `moduleid` int(10) unsigned NOT NULL default '0',
  `tableid` int(10) unsigned NOT NULL default '0',
  `sequence` int(10) unsigned NOT NULL default '0',
  `primaryTable` int(10) unsigned NOT NULL default '0',
  KEY `moduleid` (`moduleid`),
  KEY `tableid` (`tableid`),
  KEY `primaryTable` (`primaryTable`)
) TYPE=InnoDB;

--
-- Dumping data for table `restoresequence`
--

INSERT INTO `restoresequence` (`moduleid`, `tableid`, `sequence`, `primaryTable`) VALUES (5,111,1,111),(6,34,2,33),(6,33,1,33),(6,36,1,36),(6,34,2,36),(6,33,3,36),(1,10,3,1),(1,1,1,1),(1,4,4,1),(1,2,1,2),(1,11,2,2),(1,10,3,2),(1,4,4,2),(1,11,2,1),(1,12,5,2),(1,12,5,1),(10,52,2,52),(9,48,1,48);

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `roleid` int(10) NOT NULL auto_increment,
  `rolename` varchar(20) NOT NULL default '',
  PRIMARY KEY  (`roleid`)
) TYPE=InnoDB;

--
-- Dumping data for table `role`
--


--
-- Table structure for table `salesprobability`
--

DROP TABLE IF EXISTS `salesprobability`;
CREATE TABLE `salesprobability` (
  `Probability` tinyint(4) default NULL,
  `ProbabilityID` int(11) unsigned NOT NULL auto_increment,
  `Title` varchar(25) default NULL,
  `Description` text,
  PRIMARY KEY  (`ProbabilityID`)
) TYPE=InnoDB;

--
-- Dumping data for table `salesprobability`
--

INSERT INTO `salesprobability` (`Probability`, `ProbabilityID`, `Title`, `Description`) VALUES (10,1,' 10%',NULL),(20,2,' 20%',NULL),(25,3,' 25%',NULL),(30,4,' 30%',NULL),(40,5,' 40%',''),(50,6,' 50%',''),(60,7,' 60%',''),(70,8,' 70%',''),(75,9,' 75%',''),(80,10,' 80%',''),(90,11,' 90%',''),(100,12,'100%',''),(0,13,'0%','');

--
-- Table structure for table `salesstage`
--

DROP TABLE IF EXISTS `salesstage`;
CREATE TABLE `salesstage` (
  `SalesStageID` int(11) NOT NULL auto_increment,
  `Name` varchar(25) default NULL,
  PRIMARY KEY  (`SalesStageID`)
) TYPE=InnoDB;

--
-- Dumping data for table `salesstage`
--

INSERT INTO `salesstage` (`SalesStageID`, `Name`) VALUES (1,'Approach'),(2,'Needs Analysis'),(3,'Demo'),(4,'Proposal'),(5,'Trial Close'),(6,'Verbal Agreement'),(7,'Closed');

--
-- Table structure for table `salesstatus`
--

DROP TABLE IF EXISTS `salesstatus`;
CREATE TABLE `salesstatus` (
  `SalesStatusID` int(11) NOT NULL auto_increment,
  `Name` varchar(25) default NULL,
  `ActivityStatusID` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`SalesStatusID`),
  KEY `ActivityStatusID` (`ActivityStatusID`)
) TYPE=InnoDB;

--
-- Dumping data for table `salesstatus`
--

INSERT INTO `salesstatus` (`SalesStatusID`, `Name`, `ActivityStatusID`) VALUES (1,'Pending',1),(2,'Won',2),(3,'Lost',3),(4,'On Hold',4),(5,'Cancelled',5);

--
-- Table structure for table `salestype`
--

DROP TABLE IF EXISTS `salestype`;
CREATE TABLE `salestype` (
  `SalesTypeID` int(11) NOT NULL auto_increment,
  `Name` varchar(25) default NULL,
  PRIMARY KEY  (`SalesTypeID`)
) TYPE=InnoDB;

--
-- Dumping data for table `salestype`
--

INSERT INTO `salestype` (`SalesTypeID`, `Name`) VALUES (1,'Software'),(2,'Hardware'),(3,'Service');

--
-- Table structure for table `search`
--

DROP TABLE IF EXISTS `search`;
CREATE TABLE `search` (
  `SearchID` int(11) unsigned NOT NULL auto_increment,
  `ModuleID` int(10) unsigned NOT NULL default '0',
  `OwnerID` int(10) unsigned default NULL,
  `CreatedBy` int(10) unsigned default NULL,
  `CreationDate` datetime default NULL,
  `ModifiedBy` int(10) unsigned default NULL,
  `ModifiedDate` timestamp(14) NOT NULL,
  `SearchName` varchar(255) default NULL,
  PRIMARY KEY  (`SearchID`),
  UNIQUE KEY `SearchID` (`SearchID`),
  KEY `ModuleID` (`ModuleID`),
  KEY `OwnerID` (`OwnerID`),
  KEY `CreatedBy` (`CreatedBy`),
  KEY `ModifiedBy` (`ModifiedBy`),
  CONSTRAINT `0_21603` FOREIGN KEY (`ModuleID`) REFERENCES `module` (`moduleid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `0_21604` FOREIGN KEY (`OwnerID`) REFERENCES `individual` (`IndividualID`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `0_21605` FOREIGN KEY (`CreatedBy`) REFERENCES `individual` (`IndividualID`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `0_21606` FOREIGN KEY (`ModifiedBy`) REFERENCES `individual` (`IndividualID`) ON DELETE SET NULL ON UPDATE CASCADE
) TYPE=InnoDB;

--
-- Dumping data for table `search`
--


--
-- Table structure for table `searchcriteria`
--

DROP TABLE IF EXISTS `searchcriteria`;
CREATE TABLE `searchcriteria` (
  `SearchCriteriaID` int(11) unsigned NOT NULL auto_increment,
  `SearchID` int(11) unsigned NOT NULL default '0',
  `SearchTableID` int(11) unsigned NOT NULL default '0',
  `SearchFieldID` int(11) unsigned NOT NULL default '0',
  `ConditionID` int(11) unsigned NOT NULL default '0',
  `ExpressionType` enum('AND','OR') NOT NULL default 'AND',
  `Value` varchar(255) default NULL,
  `CriteriaGroup` int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (`SearchCriteriaID`),
  UNIQUE KEY `SearchCriteriaID` (`SearchCriteriaID`),
  KEY `SearchID` (`SearchID`),
  KEY `SearchTableID` (`SearchTableID`),
  KEY `SearchFieldID` (`SearchFieldID`),
  CONSTRAINT `0_21608` FOREIGN KEY (`SearchID`) REFERENCES `search` (`SearchID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `0_21609` FOREIGN KEY (`SearchTableID`) REFERENCES `searchtable` (`SearchTableID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `0_21610` FOREIGN KEY (`SearchFieldID`) REFERENCES `searchfield` (`SearchFieldID`) ON DELETE CASCADE ON UPDATE CASCADE
) TYPE=InnoDB;

--
-- Dumping data for table `searchcriteria`
--


--
-- Table structure for table `searchexpression`
--

DROP TABLE IF EXISTS `searchexpression`;
CREATE TABLE `searchexpression` (
  `expressionid` int(10) unsigned NOT NULL auto_increment,
  `label` varchar(25) default NULL,
  PRIMARY KEY  (`expressionid`)
) TYPE=InnoDB;

--
-- Dumping data for table `searchexpression`
--


--
-- Table structure for table `searchfield`
--

DROP TABLE IF EXISTS `searchfield`;
CREATE TABLE `searchfield` (
  `SearchFieldID` int(11) unsigned NOT NULL auto_increment,
  `SearchTableID` int(11) unsigned NOT NULL default '0',
  `DisplayName` varchar(255) default NULL,
  `FieldName` varchar(255) default NULL,
  `FieldPermissionQuery` varchar(255) default NULL,
  `IsOnTable` enum('N','Y') NOT NULL default 'Y',
  `RealTableName` varchar(255) default NULL,
  `RelationshipQuery` varchar(255) default NULL,
  `RealFieldName` varchar(255) default NULL,
  `FieldType` int(11) unsigned NOT NULL default '0',
  `IsOnGobalReplaceTable` enum('N','Y') NOT NULL default 'N',
  `SubRelationshipQuery` varchar(255) default NULL,
  `IsGobalReplaceField` enum('N','Y') NOT NULL default 'Y',
  PRIMARY KEY  (`SearchFieldID`),
  UNIQUE KEY `SearchFieldID` (`SearchFieldID`),
  KEY `SearchTableID` (`SearchTableID`),
  CONSTRAINT `0_21598` FOREIGN KEY (`SearchTableID`) REFERENCES `searchtable` (`SearchTableID`) ON DELETE CASCADE ON UPDATE CASCADE
) TYPE=InnoDB;

--
-- Dumping data for table `searchfield`
--

INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (1,1,'Entity ID','EntityID',NULL,'Y',NULL,NULL,'EntityID',0,'Y',NULL,'N'),(2,1,'ID 2','ExternalID',NULL,'Y',NULL,NULL,'ExternalID',0,'Y',NULL,'Y'),(3,1,'Entity Name','Name',NULL,'Y',NULL,NULL,'Name',0,'Y',NULL,'Y'),(4,1,'Marketing List','List',NULL,'Y',NULL,NULL,'List',8,'Y',NULL,'Y'),(5,1,'Created By','CONCAT(createdby1.FirstName, \" \", createdby1.LastName)',NULL,'N','individual createdby1','createdby1.IndividualID = entity.Creator','Creator',1,'N',NULL,'N'),(6,1,'Modified By','CONCAT(modifiedby1.FirstName, \" \", modifiedby1.LastName)',NULL,'N','individual modifiedby1','modifiedby1.IndividualID = entity.Modified','ModifiedBy',1,'N',NULL,'N'),(7,1,'Owned By','CONCAT(ownedby1.FirstName, \" \", ownedby1.LastName)',NULL,'N','individual ownedby1','ownedby1.IndividualID = entity.Owner','Owner',1,'Y',NULL,'Y'),(8,1,'Creation Date','Created',NULL,'Y',NULL,NULL,'Created',0,'Y',NULL,'N'),(9,1,'Modified Date','Modified',NULL,'Y',NULL,NULL,'Modified',0,'Y',NULL,'N'),(10,1,'Source','source1.Name',NULL,'N','source source1','source1.SourceID = entity.Source','Source',3,'Y',NULL,'Y'),(11,2,'Individual ID','IndividualID',NULL,'Y',NULL,NULL,'IndividualID',0,'Y',NULL,'N'),(12,2,'ID 2','ExternalID',NULL,'Y',NULL,NULL,'ExternalID',0,'Y',NULL,'Y'),(13,2,'Entity Name','entity2.Name',NULL,'N','entity entity2','individual.Entity = entity2.EntityID','Entity',7,'Y',NULL,'Y'),(14,2,'First Name','FirstName',NULL,'Y',NULL,NULL,'FirstName',0,'Y',NULL,'Y'),(15,2,'Last Name','LastName',NULL,'Y',NULL,NULL,'LastName',0,'Y',NULL,'Y'),(16,2,'Middle Initial','MiddleInitial',NULL,'Y',NULL,NULL,'MiddleInitial',0,'Y',NULL,'Y'),(17,2,'Title','Title',NULL,'Y',NULL,NULL,'Title',0,'Y',NULL,'Y'),(18,2,'Marketing List','list',NULL,'Y',NULL,NULL,'list',8,'Y',NULL,'Y'),(19,2,'Creation Date','Created',NULL,'Y',NULL,NULL,'Created',0,'Y',NULL,'N'),(20,2,'Modified Date','Modified',NULL,'Y',NULL,NULL,'Modified',0,'Y',NULL,'N'),(21,2,'Source','source2.Name',NULL,'N','source source2','source2.SourceID = individual.Source','Source',3,'Y',NULL,'Y'),(22,3,'Content','Content',NULL,'Y',NULL,NULL,'Content',0,'Y',NULL,'N'),(23,3,'Sync As','syncas',NULL,'Y',NULL,NULL,'syncas',0,'Y',NULL,'N'),(24,3,'Note','Note',NULL,'Y',NULL,NULL,'Note',0,'Y',NULL,'N'),(25,3,'Type','moctype3.Name',NULL,'N','moctype moctype3','moctype3.MOCTypeID = methodofcontact.MOCType','moctype3.Name',0,'N',NULL,'N'),(26,4,'Street 1','Street1',NULL,'Y',NULL,NULL,'Street1',0,'Y',NULL,'N'),(27,4,'Street 2','Street2',NULL,'Y',NULL,NULL,'Street2',0,'Y',NULL,'N'),(28,4,'State','state',NULL,'Y',NULL,NULL,'state',0,'Y',NULL,'N'),(29,4,'City','City',NULL,'Y',NULL,NULL,'City',0,'Y',NULL,'N'),(30,4,'Zip Code','Zip',NULL,'Y',NULL,NULL,'Zip',0,'Y',NULL,'N'),(31,4,'Country','country',NULL,'Y',NULL,NULL,'country',0,'Y',NULL,'N'),(32,4,'Website','Website',NULL,'Y',NULL,NULL,'Website',0,'Y',NULL,'N'),(33,5,'Name','Name',NULL,'Y',NULL,NULL,'Name',0,'Y',NULL,'N'),(34,5,'Custom Field Type','FieldType',NULL,'Y',NULL,NULL,'FieldType',0,'Y',NULL,'N'),(35,5,'Scalar Value','customfieldscalar5.Value',NULL,'N','customfieldscalar customfieldscalar5','customfieldscalar5.CustomFieldID = customfield.CustomFieldID','customfieldscalar5.Value',0,'N',NULL,'N'),(36,5,'Multiple Value','customfieldvalue5.Value',NULL,'N','customfieldvalue customfieldvalue5','customfieldvalue5.ValueID = customfieldmultiple.ValueID = AND customfieldmultiple.CustomFieldID = customfield.CustomFieldID','customfieldvalue5.Value',0,'N',NULL,'N'),(37,6,'Activity ID','ActivityID',NULL,'Y',NULL,NULL,'ActivityID',0,'Y',NULL,'Y'),(38,6,'Title','Title',NULL,'Y',NULL,NULL,'Title',0,'Y',NULL,'Y'),(39,6,'Details','Details',NULL,'Y',NULL,NULL,'Details',0,'Y',NULL,'Y'),(40,6,'Type','activitytype6.Name',NULL,'N','activitytype activitytype6','activitytype6.TypeID = activity.Type','activitytype6.Name',0,'N',NULL,'Y'),(41,6,'Status','activitystatus6.Name',NULL,'N','activitystatus activitystatus6','activitystatus6.StatusID = activity.Status','activitystatus6.Name',0,'N',NULL,'Y'),(42,6,'Priority','activitypriority6.Name',NULL,'N','activitypriority activitypriority6','activitypriority6.PriorityID = activity.Priority','activitypriority6.Name',0,'N',NULL,'Y'),(43,6,'Owned By','CONCAT(ownedby6.FirstName, \" \", ownedby6.LastName)',NULL,'N','individual ownedby6','ownedby6.IndividualID = activity.Owner','CONCAT(ownedby6.FirstName, \" \", ownedby6.LastName)',0,'N',NULL,'Y'),(44,6,'Created By','CONCAT(createdby6.FirstName, \" \", createdby6.LastName)',NULL,'N','individual createdby6','createdby6.IndividualID = activity.Creator','CONCAT(createdby6.FirstName, \" \", createdby6.LastName)',0,'N',NULL,'Y'),(45,6,'Modified By','CONCAT(modifiedby6.FirstName, \" \", modifiedby6.LastName)',NULL,'N','individual modifiedby6','modifiedby6.IndividualID = activity.ModifiedBy','CONCAT(modifiedby6.FirstName, \" \", modifiedby6.LastName)',0,'N',NULL,'Y'),(46,6,'Creation Date','Created',NULL,'Y',NULL,NULL,'Created',0,'Y',NULL,'Y'),(47,6,'Modified Date','Modified',NULL,'Y',NULL,NULL,'Modified',0,'Y',NULL,'Y'),(48,6,'Start Date','Start',NULL,'Y',NULL,NULL,'Start',0,'Y',NULL,'Y'),(49,6,'End Date','End',NULL,'Y',NULL,NULL,'End',0,'Y',NULL,'Y'),(50,6,'Notes','Notes',NULL,'Y',NULL,NULL,'Notes',0,'Y',NULL,'Y'),(51,6,'Visibility','visibility',NULL,'Y',NULL,NULL,'visibility',0,'Y',NULL,'Y'),(52,7,'Note ID','NoteID',NULL,'Y',NULL,NULL,'NoteID',0,'Y',NULL,'Y'),(53,7,'Title','Title',NULL,'Y',NULL,NULL,'Title',0,'Y',NULL,'Y'),(54,7,'Details','Detail',NULL,'Y',NULL,NULL,'Detail',0,'Y',NULL,'Y'),(55,7,'Created By','CONCAT(createdby7.FirstName, \" \", createdby7.LastName)',NULL,'N','individual createdby7','createdby7.IndividualID = note.Creator','CONCAT(createdby7.FirstName, \" \", createdby7.LastName)',0,'N',NULL,'Y'),(56,7,'Modified By','CONCAT(modifiedby7.FirstName, \" \", modifiedby7.LastName)',NULL,'N','individual modifiedby7','modifiedby7.IndividualID = note.UpdatedBy','CONCAT(modifiedby7.FirstName, \" \", modifiedby7.LastName)',0,'N',NULL,'Y'),(57,7,'Owned By','CONCAT(ownedby7.FirstName, \" \", ownedby7.LastName)',NULL,'N','individual ownedby7','ownedby7.IndividualID = note.Owner','CONCAT(ownedby7.FirstName, \" \", ownedby7.LastName)',0,'N',NULL,'Y'),(58,7,'Creation Date','DateCreated',NULL,'Y',NULL,NULL,'DateCreated',0,'Y',NULL,'Y'),(59,7,'Modified Date','DateUpdated',NULL,'Y',NULL,NULL,'DateUpdated',0,'Y',NULL,'Y'),(60,7,'Related Entity','entity7.name',NULL,'N','entity entity7','entity7.EntityID = note.RelateEntity','entity7.name',0,'N',NULL,'Y'),(61,7,'Related Individual','CONCAT(relatedind7.FirstName, \" \", relatedind7.LastName)',NULL,'N','individual relatedind7','relatedind7.IndividualID = note.RelateIndividual','CONCAT(relatedind7.FirstName, \" \", relatedind7.LastName)',0,'N',NULL,'Y'),(63,8,'Subject','Subject',NULL,'Y','emailmessage','','Subject',0,'Y',NULL,'Y'),(64,8,'Body','Body',NULL,'Y','emailmessage','','Body',0,'Y',NULL,'Y'),(66,8,'Sender','MailFrom',NULL,'Y','emailmessage','','MailFrom',0,'Y',NULL,'Y'),(67,8,'Recipient','Address',NULL,'N','emailrecipient','emailrecipient.MessageID=emailmessage.MessageID','Address',0,'N',NULL,'Y'),(68,8,'Message Date','MessageDate',NULL,'Y',NULL,NULL,'MessageDate',0,'Y',NULL,'Y'),(69,9,'File ID','FileID',NULL,'Y',NULL,NULL,'FileID',0,'Y',NULL,'Y'),(70,9,'Title','Title',NULL,'Y',NULL,NULL,'Title',0,'Y',NULL,'Y'),(71,9,'Description','Description',NULL,'Y',NULL,NULL,'Description',0,'Y',NULL,'Y'),(72,9,'Name','Name',NULL,'Y',NULL,NULL,'Name',0,'Y',NULL,'Y'),(73,9,'Owned By','CONCAT(ownedby9.FirstName, \" \", ownedby9.LastName)',NULL,'N','individual ownedby9','ownedby9.IndividualID = cvfile.Owner','CONCAT(ownedby9.FirstName, \" \", ownedby9.LastName)',0,'N',NULL,'Y'),(74,9,'Created By','CONCAT(createdby9.FirstName, \" \", createdby9.LastName)',NULL,'N','individual createdby9','createdby9.IndividualID = cvfile.Creator','CONCAT(createdby9.FirstName, \" \", createdby9.LastName)',0,'N',NULL,'Y'),(75,9,'Modified By','CONCAT(modifiedby9.FirstName, \" \", modifiedby9.LastName)',NULL,'N','individual modifiedby9','modifiedby9.IndividualID = cvfile.UpdatedBy','CONCAT(modifiedby9.FirstName, \" \", modifiedby9.LastName)',0,'N',NULL,'Y'),(76,9,'Author','CONCAT(author9.FirstName, \" \", author9.LastName)',NULL,'N','individual author9','author9.IndividualID = cvfile.Author','CONCAT(author9.FirstName, \" \", author9.LastName)',0,'N',NULL,'Y'),(77,9,'Creation Date','Created',NULL,'Y',NULL,NULL,'Created',0,'Y',NULL,'Y'),(78,9,'Modified Date','Updated',NULL,'Y',NULL,NULL,'Updated',0,'Y',NULL,'Y'),(79,9,'Version','Version',NULL,'Y',NULL,NULL,'Version',0,'Y',NULL,'Y'),(80,9,'Status','Status',NULL,'Y',NULL,NULL,'Status',0,'Y',NULL,'Y'),(81,9,'Visibility','visibility',NULL,'Y',NULL,NULL,'visibility',0,'Y',NULL,'Y'),(82,9,'Temporary File','IsTemporary',NULL,'Y',NULL,NULL,'IsTemporary',0,'Y',NULL,'Y'),(83,9,'Related Entity','entity9.name',NULL,'N','entity entity9','entity9.EntityID = cvfile.RelateEntity','entity9.name',0,'N',NULL,'Y'),(84,9,'Related Individual','0',NULL,'N','individual','individual.IndividualID = cvfile.RelateIndividual','0',0,'N',NULL,'Y'),(85,10,'Opportunity ID','OpportunityID',NULL,'Y',NULL,NULL,'OpportunityID',0,'Y',NULL,'Y'),(86,10,'Title','Title',NULL,'Y',NULL,NULL,'Title',0,'Y',NULL,'Y'),(87,10,'Description','Description',NULL,'Y',NULL,NULL,'Description',0,'Y',NULL,'Y'),(88,10,'Type','salestype10.Name',NULL,'N','salestype salestype10','salestype10.SalesTypeID = opportunity.TypeID','salestype10.Name',0,'N',NULL,'Y'),(89,10,'Status','salesstatus10.Name',NULL,'N','salesstatus salesstatus10','salesstatus10.SalesStatusID = opportunity.Status','salesstatus10.Name',0,'N',NULL,'Y'),(90,10,'Stage','salesstage10.Name',NULL,'N','salesstage salesstage10','salesstage10.SalesStageID = opportunity.Stage','salesstage10.Name',0,'N',NULL,'Y'),(91,10,'Probability','salesprobability10.Title',NULL,'N','salesprobability salesprobability10','salesprobability10.ProbabilityID = opportunity.Probability','salesprobability10.Title',0,'N',NULL,'Y'),(92,10,'Source','source10.Name',NULL,'N','source source10','source10.SourceID = opportunity.Source','source10.Name',0,'N',NULL,'Y'),(93,10,'Forecast Amount','ForecastAmmount',NULL,'Y',NULL,NULL,'ForecastAmmount',0,'Y',NULL,'Y'),(94,10,'Actual Amount','ActualAmount',NULL,'Y',NULL,NULL,'ActualAmount',0,'Y',NULL,'Y'),(95,10,'Owned By','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.IndividualID = activity.Owner AND activity.ActivityID = opportunity.ActivityID','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y'),(96,10,'Account Manager','CONCAT(accountmgr10.FirstName, \" \", accountmgr10.LastName)',NULL,'N','individual accountmgr10','accountmgr10.IndividualID = opportunity.AccountManager','CONCAT(accountmgr10.FirstName, \" \", accountmgr10.LastName)',0,'N',NULL,'Y'),(97,10,'Entity Name','entity.name',NULL,'N','entity','entity.EntityID = opportunity.EntityID','entity.name',0,'N',NULL,'Y'),(98,10,'Individual Name','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.IndividualID = opportunity.IndividualID','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y'),(99,10,'Account Team','grouptbl10.Name',NULL,'N','grouptbl grouptbl10','grouptbl10.GroupID = opportunity.AccountTeam','grouptbl10.Name',0,'N',NULL,'Y'),(100,10,'Estimated Close Date','estcldat10.Start',NULL,'N','activity estcldat10','estcldat10.ActivityID = opportunity.ActivityID','estcldat10.Start',0,'N',NULL,'Y'),(101,10,'Actual Close Date','actcldat10.CompletedDate',NULL,'N','activity actcldat10','actcldat10.ActivityID = opportunity.ActivityID','actcldat10.CompletedDate',0,'N',NULL,'Y'),(102,11,'Proposal ID','ProposalID',NULL,'Y',NULL,NULL,'ProposalID',0,'Y',NULL,'Y'),(103,11,'Title','Title',NULL,'Y',NULL,NULL,'Title',0,'Y',NULL,'Y'),(104,11,'Description','Description',NULL,'Y',NULL,NULL,'Description',0,'Y',NULL,'Y'),(105,11,'Type','salestype11.Name',NULL,'N','salestype salestype11','salestype11.SalesTypeID = proposal.TypeID','salestype11.Name',0,'N',NULL,'Y'),(106,11,'Status','salesstatus11.Name',NULL,'N','salesstatus salesstatus11','salesstatus11.SalesStatusID = proposal.Status','salesstatus11.Name',0,'N',NULL,'Y'),(107,11,'Stage','salesstage11.Name',NULL,'N','salesstage salesstage11','salesstage11.SalesStageID = proposal.Stage','salesstage11.Name',0,'N',NULL,'Y'),(108,11,'Probability','salesprobability11.Title',NULL,'N','salesprobability salesprobability11','salesprobability11.ProbabilityID = proposal.Probability','salesprobability11.Title',0,'N',NULL,'Y'),(109,11,'Source','source11.Name',NULL,'N','source source11','source11.SourceID = proposal.Source','source11.Name',0,'N',NULL,'Y'),(110,11,'Forecast Amount','ForecastAmmount',NULL,'Y',NULL,NULL,'ForecastAmmount',0,'Y',NULL,'Y'),(111,11,'Actual Amount','ActualAmount',NULL,'Y',NULL,NULL,'ActualAmount',0,'Y',NULL,'Y'),(112,11,'Owned By','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.IndividualID = proposal.Owner','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y'),(113,11,'Account Manager','CONCAT(acctmgr11.FirstName, \" \", acctmgr11.LastName)',NULL,'N','individual acctmgr11','acctmgr11.IndividualID = proposal.AccountManager','CONCAT(acctmgr11.FirstName, \" \", acctmgr11.LastName)',0,'N',NULL,'Y'),(114,11,'Account Team','grouptbl11.Name',NULL,'N','grouptbl grouptbl11','grouptbl11.GroupID = proposal.AccountTeam','grouptbl11.Name',0,'N',NULL,'Y'),(115,11,'Estimated Close Date','EstimatedCloseDate',NULL,'Y',NULL,NULL,'EstimatedCloseDate',0,'Y',NULL,'Y'),(116,11,'Actual Close Date','ActualCloseDate',NULL,'Y',NULL,NULL,'ActualCloseDate',0,'Y',NULL,'Y'),(117,12,'Project ID','ProjectID',NULL,'Y',NULL,NULL,'ProjectID',0,'Y',NULL,'Y'),(118,12,'Title','ProjectTitle',NULL,'Y',NULL,NULL,'ProjectTitle',0,'Y',NULL,'Y'),(119,12,'Description','Description',NULL,'Y',NULL,NULL,'Description',0,'Y',NULL,'Y'),(120,12,'Status','projectstatus12.Title',NULL,'N','projectstatus projectstatus12','projectstatus12.StatusID = project.StatusID','projectstatus12.Title',0,'N',NULL,'Y'),(121,12,'Start Date','Start',NULL,'Y',NULL,NULL,'Start',0,'Y',NULL,'Y'),(122,12,'End Date','End',NULL,'Y',NULL,NULL,'End',0,'Y',NULL,'Y'),(124,12,'Budgeted Hours','BudgetedHours',NULL,'Y',NULL,NULL,'BudgetedHours',0,'Y',NULL,'Y'),(125,12,'Hours Used','HoursUsed',NULL,'Y',NULL,NULL,'HoursUsed',0,'Y',NULL,'Y'),(126,12,'Manager','CONCAT(manager12.FirstName, \" \", manager12.LastName)',NULL,'N','individual manager12','manager12.IndividualID = project.Manager','CONCAT(manager12.FirstName, \" \", manager12.LastName)',0,'N',NULL,'Y'),(127,12,'Owner','CONCAT(owner12.FirstName, \" \", owner12.LastName)',NULL,'N','individual owner12','owner12.IndividualID = project.Owner','CONCAT(owner12.FirstName, \" \", owner12.LastName)',0,'N',NULL,'Y'),(128,12,'Created By','CONCAT(createdby12.FirstName, \" \", createdby12.LastName)',NULL,'N','individual createdby12','createdby12.IndividualID = project.Creator','CONCAT(createdby12.FirstName, \" \", createdby12.LastName)',0,'N',NULL,'Y'),(129,12,'Modified By','CONCAT(modifiedby12.FirstName, \" \", modifiedby12.LastName)',NULL,'N','individual modifiedby12','modifiedby12.IndividualID = project.ModifiedBy','CONCAT(modifiedby12.FirstName, \" \", modifiedby12.LastName)',0,'N',NULL,'Y'),(130,12,'Creation Date','Created',NULL,'Y',NULL,NULL,'Created',0,'Y',NULL,'Y'),(131,12,'Modified Date','Modified',NULL,'Y',NULL,NULL,'Modified',0,'Y',NULL,'Y'),(132,13,'Task ID','ActivityID',NULL,'Y',NULL,NULL,'ActivityID',0,'Y',NULL,'Y'),(133,13,'Milestone','Milestone',NULL,'Y',NULL,NULL,'Milestone',0,'Y',NULL,'Y'),(134,13,'Percent Complete','PercentComplete',NULL,'Y',NULL,NULL,'PercentComplete',0,'Y',NULL,'Y'),(135,13,'Start Date','std13.Start',NULL,'N','activity std13','std13.ActivityID = task.ActivityID','std13.Start',0,'N',NULL,'Y'),(136,13,'Due Date','dud13.DueDate',NULL,'N','activity dud13','dud13.ActivityID = task.ActivityID','dud13.DueDate',0,'N',NULL,'Y'),(137,6,'Status','activitystatus6.Name',NULL,'N','activitystatus activitystatus6','activitystatus6.StatusID = activity.Status','activitystatus6.Name',0,'N',NULL,'Y'),(138,13,'Assigned To','CONCAT(assto13.FirstName, \" \", assto13.LastName)',NULL,'N','individual assto13','assto13.IndividualID = taskassigned.AssignedTo AND taskassigned.TaskID = task.ActivityID','CONCAT(assto13.FirstName, \" \", assto13.LastName)',0,'N',NULL,'Y'),(139,14,'Ticket ID','ticketid',NULL,'Y',NULL,NULL,'ticketid',0,'Y',NULL,'Y'),(140,14,'Subject','subject',NULL,'Y',NULL,NULL,'subject',0,'Y',NULL,'Y'),(141,14,'Description','description',NULL,'Y',NULL,NULL,'description',0,'Y',NULL,'Y'),(142,14,'Assigned To','CONCAT(assto14.FirstName, \" \", assto14.LastName)',NULL,'N','individual assto14','assto14.IndividualID = ticket.assignedto','CONCAT(assto14.FirstName, \" \", assto14.LastName)',0,'N',NULL,'Y'),(143,14,'Manager','CONCAT(mgr14.FirstName, \" \", mgr14.LastName)',NULL,'N','individual mgr14','mgr14.IndividualID = ticket.manager','CONCAT(mgr14.FirstName, \" \", mgr14.LastName)',0,'N',NULL,'Y'),(144,14,'Created By','CONCAT(createdby14.FirstName, \" \", createdby14.LastName)',NULL,'N','individual createdby14','createdby14.IndividualID = ticket.createdby','CONCAT(createdby14.FirstName, \" \", createdby14.LastName)',0,'N',NULL,'Y'),(145,14,'Modified By','CONCAT(modifiedby14.FirstName, \" \", modifiedby14.LastName)',NULL,'N','individual modifiedby14','modifiedby14.IndividualID = ticket.modifiedby','CONCAT(modifiedby14.FirstName, \" \", modifiedby14.LastName)',0,'N',NULL,'Y'),(146,14,'Owned By','CONCAT(ownedby14.FirstName, \" \", ownedby14.LastName)',NULL,'N','individual ownedby14','ownedby14.IndividualID = ticket.owner','CONCAT(ownedby14.FirstName, \" \", ownedby14.LastName)',0,'N',NULL,'Y'),(147,14,'Status','supportstatus14.name',NULL,'N','supportstatus supportstatus14','supportstatus14.statusid = ticket.status','supportstatus14.name',0,'N',NULL,'Y'),(148,14,'Priority','supportpriority14.name',NULL,'N','supportpriority supportpriority14','supportpriority14.priorityid = ticket.priority','supportpriority14.name',0,'N',NULL,'Y'),(149,14,'Creation Date','created',NULL,'Y',NULL,NULL,'created',0,'Y',NULL,'Y'),(150,14,'Modification Date','modified',NULL,'Y',NULL,NULL,'modified',0,'Y',NULL,'Y'),(151,14,'Open/Closed Status','ocstatus',NULL,'Y',NULL,NULL,'ocstatus',0,'Y',NULL,'Y'),(152,15,'Timeslip ID','TimeSlipID',NULL,'Y',NULL,NULL,'TimeSlipID',0,'Y',NULL,'Y'),(153,15,'Description','Description',NULL,'Y',NULL,NULL,'Description',0,'Y',NULL,'Y'),(154,15,'Duration (in hours)','Hours',NULL,'Y',NULL,NULL,'Hours',0,'Y',NULL,'Y'),(155,15,'Break Time (in hours)','BreakTime',NULL,'Y',NULL,NULL,'BreakTime',0,'Y',NULL,'Y'),(156,15,'Date','Date',NULL,'Y',NULL,NULL,'Date',0,'Y',NULL,'Y'),(157,15,'Start Time','Start',NULL,'Y',NULL,NULL,'Start',0,'Y',NULL,'Y'),(158,15,'End Time','End',NULL,'Y',NULL,NULL,'End',0,'Y',NULL,'Y'),(159,15,'Created By','CONCAT(createdby15.FirstName, \" \", createdby15.LastName)',NULL,'N','individual createdby15','createdby15.IndividualID = timeslip.CreatedBy','CONCAT(createdby15.FirstName, \" \", createdby15.LastName)',0,'N',NULL,'Y'),(160,16,'FAQ ID','faqid',NULL,'Y',NULL,NULL,'faqid',0,'Y',NULL,'Y'),(161,16,'Title','title',NULL,'Y',NULL,NULL,'title',0,'Y',NULL,'Y'),(162,16,'Detail','detail',NULL,'Y',NULL,NULL,'detail',0,'Y',NULL,'Y'),(163,16,'Status','status',NULL,'Y',NULL,NULL,'status',0,'Y',NULL,'Y'),(164,16,'Created By','CONCAT(createdby16.FirstName, \" \", createdby16.LastName)',NULL,'N','individual createdby16','createdby16.IndividualID = faq.createdby','CONCAT(createdby16.FirstName, \" \", createdby16.LastName)',0,'N',NULL,'Y'),(165,16,'Modified By','CONCAT(modifiedby16.FirstName, \" \", modifiedby16.LastName)',NULL,'N','individual modifiedby16','modifiedby16.IndividualID = faq.updatedby','CONCAT(modifiedby16.FirstName, \" \", modifiedby16.LastName)',0,'N',NULL,'Y'),(166,16,'Owned By','CONCAT(ownedby16.FirstName, \" \", ownedby16.LastName)',NULL,'N','individual ownedby16','ownedby16.IndividualID = faq.owner','CONCAT(ownedby16.FirstName, \" \", ownedby16.LastName)',0,'N',NULL,'Y'),(167,16,'Creation Date','created',NULL,'Y',NULL,NULL,'created',0,'Y',NULL,'Y'),(168,16,'Modification Date','updated',NULL,'Y',NULL,NULL,'updated',0,'Y',NULL,'Y'),(169,17,'Knowledgebase ID','kbid',NULL,'Y',NULL,NULL,'kbid',0,'Y',NULL,'Y'),(170,17,'Title','title',NULL,'Y',NULL,NULL,'title',0,'Y',NULL,'Y'),(171,17,'Detail','detail',NULL,'Y',NULL,NULL,'detail',0,'Y',NULL,'Y'),(172,17,'Status','status',NULL,'Y',NULL,NULL,'status',0,'Y',NULL,'Y'),(173,17,'Category','category17.title',NULL,'N','category category17','category17.catid = knowledgebase.category','category17.title',0,'N',NULL,'Y'),(174,17,'Created By','CONCAT(createdby17.FirstName, \" \", createdby17.LastName)',NULL,'N','individual createdby17','createdby17.IndividualID = knowledgebase.createdby','CONCAT(createdby17.FirstName, \" \", createdby17.LastName)',0,'N',NULL,'Y'),(175,17,'Modified By','CONCAT(modifiedby17.FirstName, \" \", modifiedby17.LastName)',NULL,'N','individual createdby17','modifiedby17.IndividualID = knowledgebase.updatedby','CONCAT(modifiedby17.FirstName, \" \", modifiedby17.LastName)',0,'N',NULL,'Y'),(176,17,'Owned By','CONCAT(ownedby17.FirstName, \" \", ownedby17.LastName)',NULL,'N','individual ownedby17','ownedby17.IndividualID = knowledgebase.owner','CONCAT(ownedby17.FirstName, \" \", ownedby17.LastName)',0,'N',NULL,'Y'),(177,17,'Creation Date','created',NULL,'Y',NULL,NULL,'created',0,'Y',NULL,'Y'),(178,17,'Modification Date','updated',NULL,'Y',NULL,NULL,'updated',0,'Y',NULL,'Y'),(179,18,'Order ID','orderid',NULL,'Y',NULL,NULL,'orderid',0,'Y',NULL,'Y'),(181,18,'Notes','description',NULL,'Y',NULL,NULL,'description',0,'Y',NULL,'Y'),(182,18,'Status','accountingstatus18.title',NULL,'N','accountingstatus accountingstatus18','accountingstatus18.statusid = cvorder.status','accountingstatus18.title',0,'N',NULL,'Y'),(183,18,'Terms','accountingterms18.title',NULL,'N','accountingterms accountingterms18','accountingterms18.termsid = cvorder.terms','accountingterms18.title',0,'N',NULL,'Y'),(184,18,'Subtotal','subtotal',NULL,'Y',NULL,NULL,'subtotal',0,'Y',NULL,'Y'),(185,18,'Tax','tax',NULL,'Y',NULL,NULL,'tax',0,'Y',NULL,'Y'),(188,18,'Total','total',NULL,'Y',NULL,NULL,'total',0,'Y',NULL,'Y'),(189,18,'Owner','CONCAT(owner18.FirstName, \" \", owner18.LastName)',NULL,'N','individual owner18','owner18.IndividualID = cvorder.owner','CONCAT(owner18.FirstName, \" \", owner18.LastName)',0,'N',NULL,'Y'),(190,18,'Created By','CONCAT(cretedby18.FirstName, \" \", cretedby18.LastName)',NULL,'N','individual cretedby18','cretedby18.IndividualID = cvorder.creator','CONCAT(cretedby18.FirstName, \" \", cretedby18.LastName)',0,'N',NULL,'Y'),(191,18,'Modified By','CONCAT(modifiedby18.FirstName, \" \", modifiedby18.LastName)',NULL,'N','individual modifiedby18','modifiedby18.IndividualID = cvorder.modifiedby','CONCAT(modifiedby18.FirstName, \" \", modifiedby18.LastName)',0,'N',NULL,'Y'),(192,18,'Account Manager','CONCAT(acctmgr18.FirstName, \" \", acctmgr18.LastName)',NULL,'N','individual acctmgr18','acctmgr18.IndividualID = cvorder.accountmgr','CONCAT(acctmgr18.FirstName, \" \", acctmgr18.LastName)',0,'N',NULL,'Y'),(193,18,'Creation Date','created',NULL,'Y',NULL,NULL,'created',0,'Y',NULL,'Y'),(194,18,'Modified Date','modified',NULL,'Y',NULL,NULL,'modified',0,'Y',NULL,'Y'),(195,18,'Order Date','orderdate',NULL,'Y',NULL,NULL,'orderdate',0,'Y',NULL,'Y'),(196,18,'P. O. Number','ponumber',NULL,'Y',NULL,NULL,'ponumber',0,'Y',NULL,'Y'),(197,19,'Invoice ID','InvoiceID',NULL,'Y',NULL,NULL,'InvoiceID',0,'Y',NULL,'Y'),(198,19,'External ID','ExternalID',NULL,'Y',NULL,NULL,'ExternalID',0,'Y',NULL,'Y'),(200,19,'Notes','Description',NULL,'Y',NULL,NULL,'Description',0,'Y',NULL,'Y'),(201,19,'Status','accountingstatus19.title',NULL,'N','accountingstatus accountingstatus19','accountingstatus19.statusid = invoice.Status','accountingstatus19.title',0,'N',NULL,'Y'),(202,19,'Terms','accountingterms19.title',NULL,'N','accountingterms accountingterms19','accountingterms19.termsid = invoice.Terms','accountingterms19.title',0,'N',NULL,'Y'),(203,19,'Subtotal','SubTotal',NULL,'Y',NULL,NULL,'SubTotal',0,'Y',NULL,'Y'),(204,19,'Tax','Tax',NULL,'Y',NULL,NULL,'Tax',0,'Y',NULL,'Y'),(207,19,'Total','Total',NULL,'Y',NULL,NULL,'Total',0,'Y',NULL,'Y'),(208,19,'Owner','CONCAT(owner19.FirstName, \" \", owner19.LastName)',NULL,'N','individual owner19','owner19.IndividualID = invoice.Owner','CONCAT(owner19.FirstName, \" \", owner19.LastName)',0,'N',NULL,'Y'),(209,19,'Created By','CONCAT(createdby19.FirstName, \" \", createdby19.LastName)',NULL,'N','individual createdby19','createdby19.IndividualID = invoice.Creator','CONCAT(createdby19.FirstName, \" \", createdby19.LastName)',0,'N',NULL,'Y'),(210,19,'Account Manager','CONCAT(acctmgr19.FirstName, \" \", acctmgr19.LastName)',NULL,'N','individual acctmgr19','acctmgr19.IndividualID = invoice.accountmgr','CONCAT(acctmgr19.FirstName, \" \", acctmgr19.LastName)',0,'N',NULL,'Y'),(211,19,'Creation Date','created',NULL,'Y',NULL,NULL,'created',0,'Y',NULL,'Y'),(212,19,'Modified Date','modified',NULL,'Y',NULL,NULL,'modified',0,'Y',NULL,'Y'),(213,19,'P. O. Number','ponumber',NULL,'Y',NULL,NULL,'ponumber',0,'Y',NULL,'Y'),(214,6,'Attendee Name','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.IndividualID = attendee.individualid AND attendee.activityId = activity.activityId','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y'),(215,6,'Entity Name','entity.name',NULL,'N','entity','entity.entityId = activitylink.recordId AND activitylink.recordTypeId = 1 AND activitylink.activityId = activity.activityId','entity.name',0,'N',NULL,'Y'),(216,6,'Individual Name','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.individualId = activitylink.recordId AND activitylink.recordTypeId = 2 AND activitylink.activityId = activity.activityId','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y'),(217,1,'Account Manager','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.individualid = entity.accountmanagerid','AccountManagerID',4,'Y',NULL,'Y'),(218,1,'Account Team','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.individualid = entity.accountteamid','AccountTeamID',2,'Y',NULL,'Y'),(219,2,'Created By','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.individualid = individual.createdby','CreatedBy',1,'N',NULL,'N'),(220,2,'Modified By','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.individualid = individual.modifiedby','ModifiedBy',1,'N',NULL,'N'),(221,10,'Created By','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.individualid = activity.creator AND activity.activityid = opportunity.activityid','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y'),(222,10,'Modified By','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.individualid = activity.modifiedby AND activity.activityid = opportunity.activityid','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y'),(223,11,'Opportunity','opportunity.name',NULL,'N','opportunity','opportunity.opportunityid = proposal.opportunityid','opportunity.name',0,'N',NULL,'Y'),(224,11,'Individual','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.individualid = proposal.individualid','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y'),(225,11,'Billing Address','billing',NULL,'Y','','','billing',0,'Y',NULL,'Y'),(226,11,'Shipping Address','shipping',NULL,'Y','','','shipping',0,'Y',NULL,'Y'),(227,11,'Terms','terms.termname',NULL,'N','terms','terms.termid = proposal.termid','terms.termname',0,'N',NULL,'Y'),(228,11,'Item','proposalitem.name',NULL,'N','proposalitem','propsalitem.proposalid = proposal.proposalid','proposalitem.name',0,'N',NULL,'Y'),(229,11,'Created By','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.individualid = proposal.createdby','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y'),(230,11,'Modified By','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.individualid = proposal.createdby','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y'),(231,12,'Entity','entity.name',NULL,'N','entity','entity.entityid = projectlink.recordid AND projectlink.recordtypeid = 14 AND projectlink.projectid = project.projectid','entity.name',0,'N',NULL,'Y'),(232,12,'Individual','CONCAT(ind12.FirstName, \" \", ind12.LastName)',NULL,'N','individual ind12','ind12.individualid = projectlink.recordid AND projectlink.recordtypeid = 15 AND projectlink.projectid = project.projectid','CONCAT(ind12.FirstName, \" \", ind12.LastName)',0,'N',NULL,'Y'),(233,12,'Team','grouptbl.name',NULL,'N','grouptbl','grouptbl.groupid = projectlink.recordid AND projectlink.recordtypeid=16 AND projectlink.projectid = project.projectid','grouptbl.name',0,'N',NULL,'Y'),(234,12,'Available','project.budgetedhours - project.hoursused',NULL,'N','','','project.budgetedhours - project.hoursused',0,'N',NULL,'Y'),(235,13,'Created','created13.created',NULL,'N','activity created13','created13.activityid = task.activityid','created13.created',0,'N',NULL,'Y'),(236,13,'Modified','modified13.modified',NULL,'N','activity modified13','modified13.activityid = task.activityid','modified13.modified',0,'N',NULL,'Y'),(237,13,'Title','title13.title',NULL,'N','activity title13','title13.activityid = task.activityid','title13.title',0,'N',NULL,'Y'),(238,13,'Description','description13.description',NULL,'N','activity description13','description13.activityid = task.activityid','description13.description',0,'N',NULL,'Y'),(239,13,'Project','project.title',NULL,'N','project','project.projectid = task.projectid','project.title',0,'N',NULL,'Y'),(240,13,'Parent Task','activity.title',NULL,'N','activity','activity.activityid = task.parent','activity.title',0,'N',NULL,'Y'),(241,13,'Manager','CONCAT(ind13.FirstName, \" \", ind13.LastName)',NULL,'N','individual ind13','ind13.individualid = activitylink.recordid AND activitylink.recordtypeid = 2 AND activitylink.activityid = task.activityid','CONCAT(ind13.FirstName, \" \", ind13.LastName)',0,'N',NULL,'Y'),(242,13,'Status','activitystatus.name',NULL,'N','activitystatus','activitystatus.statusid = activity.status AND activity.activityid = task.activityid','activitystatus.name',0,'N',NULL,'Y'),(243,15,'Task','activity.title',NULL,'N','activity','activity.activityid = timeslip.activityid','activity.title',0,'N',NULL,'Y'),(244,14,'Entity','entity.name',NULL,'N','entity','entity.entityid = ticket.entityid','entity.name',0,'N',NULL,'Y'),(245,14,'Individual','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.individualid = ticket.individualid','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y'),(246,14,'Created','created',NULL,'Y','','individual.individualid = ticket.individualid','created',0,'Y',NULL,'Y'),(247,18,'Created','created',NULL,'Y','','','created',0,'Y',NULL,'Y'),(248,18,'modified','modified',NULL,'Y','','','modified',0,'Y',NULL,'Y'),(250,18,'Entity','en18.Name',NULL,'N','entity en18','en18.EntityID = cvorder.en18','en18.Name',0,'N',NULL,'Y'),(255,18,'Project','proj18.ProjectTitle',NULL,'N','project proj18','proj18.ProjectID = cvorder.project','proj18.ProjectTitle',0,'N',NULL,'Y'),(262,19,'OrderID','OrderID',NULL,'Y','','','OrderID',0,'Y',NULL,'Y'),(263,19,'Creator','CONCAT(indv19.FirstName, \" \", indv19.LastName)',NULL,'N','individual indv19','indv19.IndividualID = invoice.Creator','CONCAT(indv19.FirstName, \" \", indv19.LastName)',0,'N',NULL,'Y'),(264,19,'Modified By','CONCAT(indiv19.FirstName, \" \", indiv19.LastName)',NULL,'N','individual indiv19','indiv19.IndividualID = invoice.`Modified By`','CONCAT(indiv19.FirstName, \" \", indiv19.LastName)',0,'N',NULL,'Y'),(268,19,'Project','proj19.ProjectTitle',NULL,'N','project proj19','proj19.ProjectID = invoice.project','proj19.ProjectTitle',0,'N',NULL,'Y'),(269,19,'InvoiceDate','InvoiceDate',NULL,'Y','','','InvoiceDate',0,'Y',NULL,'Y'),(270,20,'ListID','listid',NULL,'Y','','','listid',0,'Y',NULL,'Y'),(271,20,'Title','title',NULL,'Y','','','title',0,'Y',NULL,'Y'),(272,20,'Description','description',NULL,'Y','','','description',0,'Y',NULL,'Y'),(277,20,'Status','status',NULL,'Y','','','status',0,'Y',NULL,'Y'),(278,20,'Owner','CONCAT(owner20.FirstName, \" \", owner20.LastName)',NULL,'N','individual owner20','owner20.individualid = marketinglist.owner','CONCAT(owner20.FirstName, \" \", owner20.LastName)',0,'N',NULL,'Y'),(279,20,'Creator','CONCAT(creator20.FirstName, \" \", creator20.LastName)',NULL,'N','individual creator20','creator20.individualid = marketinglist.creator','CONCAT(creator20.FirstName, \" \", creator20.LastName)',0,'N',NULL,'Y'),(280,20,'Modifier','CONCAT(modifier20.FirstName, \" \", modifier20.LastName)',NULL,'N','individual modifier20','modifier20.individualid = marketinglist.modifiedby','CONCAT(modifier20.FirstName, \" \", modifier20.LastName)',0,'N',NULL,'Y'),(281,21,'Title','title',NULL,'Y','','','title',0,'Y',NULL,'Y'),(282,21,'Description','description',NULL,'Y','','','description',0,'Y',NULL,'Y'),(283,21,'Start Date','satrtdate',NULL,'Y','','','satrtdate',0,'Y',NULL,'Y'),(284,21,'End Date','enddate',NULL,'Y','','','enddate',0,'Y',NULL,'Y'),(287,22,'Title','title',NULL,'Y','','','title',0,'Y',NULL,'Y'),(290,22,'Entity','entity22.name',NULL,'N','entity entity22','entity22.entityid = activitylink.recordid AND activitylink.recordtypeid = 1 AND activitylink.activityid = literaturerequest.activityid','entity22.name',0,'N',NULL,'Y'),(291,22,'Individual','CONCAT(ind22.FirstName, \" \", ind22.LastName)',NULL,'N','individual ind22','ind22.individualid = literaturerequest.requestedby','CONCAT(ind22.FirstName, \" \", ind22.LastName)',0,'N',NULL,'Y'),(293,22,'Delivery Method','deliverymethod22.name',NULL,'N','deliverymethod deliverymethod22','deliverymethod22.deliveryid = literaturerequest.deliverymethod','deliverymethod22.name',0,'N',NULL,'Y'),(298,23,'Start','start',NULL,'Y','','','start',0,'Y',NULL,'Y'),(299,23,'End','End',NULL,'Y','','','End',0,'Y',NULL,'Y'),(301,23,'Max attendees','maxattendees',NULL,'Y','','','maxattendees',0,'Y',NULL,'Y'),(302,23,'Moderator','CONCAT(moderator23.FirstName, \" \", moderator23.LastName)',NULL,'N','individual moderator23','moderator23.individualid = event.moderator','CONCAT(moderator23.FirstName, \" \", moderator23.LastName)',0,'N',NULL,'Y'),(303,24,'PaymentID','paymentid',NULL,'Y','','','paymentid',0,'Y',NULL,'Y'),(304,24,'ExternalID','externalid',NULL,'Y','','','externalid',0,'Y',NULL,'Y'),(306,24,'Description','description',NULL,'Y','','','description',0,'Y',NULL,'Y'),(307,24,'Entity','entity24.name',NULL,'N','entity entity24','entity24.EntityID = payment.EntityID','entity24.name',0,'N',NULL,'Y'),(308,24,'PaymentMethod','paymentmethod24.title',NULL,'N','paymentmethod paymentmethod24','Paymentmethod24.MethodID = Payment.PaymentMethod','paymentmethod24.title',0,'N',NULL,'Y'),(309,24,'Reference','reference',NULL,'Y','','','reference',0,'Y',NULL,'Y'),(310,24,'Amount','amount',NULL,'Y','','','amount',0,'Y',NULL,'Y'),(311,24,'Card Type','cardtype',NULL,'Y','','','cardtype',0,'Y',NULL,'Y'),(312,24,'Card Number','cardnumber',NULL,'Y','','','cardnumber',0,'Y',NULL,'Y'),(313,24,'Expiration','expiration',NULL,'Y','','','expiration',0,'Y',NULL,'Y'),(314,24,'Check Number','checknumber',NULL,'Y','','','checknumber',0,'Y',NULL,'Y'),(315,24,'Owner','CONCAT(owner24.FirstName, \" \", owner24.LastName)',NULL,'N','individual owner24','owner24.individualid = payment.owner','CONCAT(owner24.FirstName, \" \", owner24.LastName)',0,'N',NULL,'Y'),(316,24,'Modified By','CONCAT(individual24.FirstName, \" \", individual24.LastName)',NULL,'N','individual individual24','individual24.IndividualID = payment.modifiedby','CONCAT(individual24.FirstName, \" \", individual24.LastName)',0,'N',NULL,'Y'),(319,24,'Modified','modified',NULL,'Y','','','modified',0,'Y',NULL,'Y'),(320,25,'ExpenseID','expenseid',NULL,'Y','','','expenseid',0,'Y',NULL,'Y'),(321,25,'ExternalID','externalid',NULL,'Y','','','externalid',0,'Y',NULL,'Y'),(322,25,'Title','title',NULL,'Y','','','title',0,'Y',NULL,'Y'),(323,25,'Description','description',NULL,'Y','','','description',0,'Y',NULL,'Y'),(324,25,'Entity','entity25.name',NULL,'N','entity entity25','entity25.EntityID = expense.EntityID','entity25.name',0,'N',NULL,'Y'),(325,25,'Amount','amount',NULL,'Y','','','amount',0,'Y',NULL,'Y'),(326,25,'Owner','CONCAT(owner25.FirstName, \" \", owner25.LastName)',NULL,'N','individual owner25','owner25.individualid = expense.owner','CONCAT(owner25.FirstName, \" \", owner25.LastName)',0,'N',NULL,'Y'),(327,25,'Created','created',NULL,'Y','','','created',0,'Y',NULL,'Y'),(328,25,'Modified','modified',NULL,'Y','','','modified',0,'Y',NULL,'Y'),(329,25,'Status','status',NULL,'Y','','','status',0,'Y',NULL,'Y'),(330,25,'Project','project25.projecttitle',NULL,'N','project project25','expense.project = project25.projected','project25.projecttitle',0,'N',NULL,'Y'),(331,25,'Ticket','ticket25.subject',NULL,'N','ticket ticket25','expense.ticket = ticket25.ticketid','ticket25.subject',0,'N',NULL,'Y'),(332,25,'Opportunity','opportunity26.Title',NULL,'N','opportunity opportunity26','expense.opportunity = opportunity26.opportunityid','opportunity26.Title',0,'N',NULL,'Y'),(335,25,'GLAccountsID','glaccount25.title',NULL,'N','glaccount glaccount25','glaccount25.glaccountid=Expense.glaccountid','glaccount25.title',0,'N',NULL,'Y'),(336,25,'Notes','notes',NULL,'Y','','','notes',0,'Y',NULL,'Y'),(337,25,'Expense Form ID','expenseformid',NULL,'Y','','','expenseformid',0,'Y',NULL,'Y'),(338,26,'Purchase Order ID','purchaseorderid',NULL,'Y','','','purchaseorderid',0,'Y',NULL,'Y'),(339,26,'External ID','externalid',NULL,'Y','','','externalid',0,'Y',NULL,'Y'),(341,26,'Notes','description',NULL,'Y','','','description',0,'Y',NULL,'Y'),(343,26,'Entity','entity26.name',NULL,'N','entity entity26','purchaseorder.entity = entity26.entityid','entity26.name',0,'N',NULL,'Y'),(344,26,'SubTotal','subtotal',NULL,'Y','','','subtotal',0,'Y',NULL,'Y'),(346,26,'Tax','tax',NULL,'Y','','','tax',0,'Y',NULL,'Y'),(348,26,'Total','total',NULL,'Y','','','total',0,'Y',NULL,'Y'),(353,26,'Owner','CONCAT(owner26.FirstName, \" \", owner26.LastName)',NULL,'N','individual owner26','owner26.individualid = purchaseorder.owner','CONCAT(owner26.FirstName, \" \", owner26.LastName)',0,'N',NULL,'Y'),(354,26,'Creator','CONCAT(creator26.FirstName, \" \", creator26.LastName)',NULL,'N','individual creator26','creator26.individualid = purchaseorder.creator','CONCAT(creator26.FirstName, \" \", creator26.LastName)',0,'N',NULL,'Y'),(355,26,'Modified By','CONCAT(individual26.FirstName, \" \", individual26.LastName)',NULL,'N','individual individual26','individual26.IndividualID = purchaseorder.modifiedby','CONCAT(individual26.FirstName, \" \", individual26.LastName)',0,'N',NULL,'Y'),(356,26,'Modified','modified',NULL,'Y','','','modified',0,'Y',NULL,'Y'),(357,26,'Status','accountingstatus26.title',NULL,'N','accountingstatus accountingstatus26','purchaseorder.status = accountingstatus26.statusid','accountingstatus26.title',0,'N',NULL,'Y'),(358,26,'Terms','accountongterms26.title',NULL,'N','accountongterms accountongterms26','purchaseorder.terms  = accountingterms26.termsid','accountongterms26.title',0,'N',NULL,'Y'),(359,26,'Account Manager','CONCAT(acctmgr26.FirstName, \" \", acctmgr26.LastName)',NULL,'N','individual acctmgr26','acctmgr26.individualid = purchaseorder.accountmgr','CONCAT(acctmgr26.FirstName, \" \", acctmgr26.LastName)',0,'N',NULL,'Y'),(360,26,'Purchase Order Date','purchaseorderdate',NULL,'Y','','','purchaseorderdate',0,'Y',NULL,'Y'),(362,26,'Created','created',NULL,'Y','','','created',0,'Y',NULL,'Y'),(363,27,'Item ID','itemid',NULL,'Y','','','itemid',0,'Y',NULL,'Y'),(364,27,'External ID','externalid',NULL,'Y','','','externalid',0,'Y',NULL,'Y'),(365,27,'Title','title',NULL,'Y','','','title',0,'Y',NULL,'Y'),(366,27,'Description','description',NULL,'Y','','','description',0,'Y',NULL,'Y'),(367,27,'List Price','listprice',NULL,'Y','','','listprice',0,'Y',NULL,'Y'),(368,27,'Cost','cost',NULL,'Y','','','cost',0,'Y',NULL,'Y'),(369,27,'Tax Class','title',NULL,'Y','taxclass','taxclass. taxclass id=Item.taxclass','title',0,'Y',NULL,'Y'),(370,27,'Type','title',NULL,'Y','itemtipe','itemtype.Itemtypeid = Item.type','title',0,'Y',NULL,'Y'),(371,27,'Sku','sku',NULL,'Y','','','sku',0,'Y',NULL,'Y'),(372,27,'Parent','item27.Title',NULL,'N','item item27','item27.itemid = item.parent','item27.Title',0,'N',NULL,'Y'),(373,27,'Manufacturer ID','manentity27.name',NULL,'N','entity manentity27','manitem27.manufacturerid = entity.entityid','manentity27.name',0,'N',NULL,'Y'),(374,27,'Vendor ID','venentity27.name',NULL,'N','entity venentity27','item.vendorid = venentity27.entityid','venentity27.name',0,'N',NULL,'Y'),(375,27,'Created By','CONCAT(createdby27.FirstName, \" \", createdby27.LastName)',NULL,'N','individual createdby27','createdby27.individualid = item.createdby','CONCAT(createdby27.FirstName, \" \", createdby27.LastName)',0,'N',NULL,'Y'),(376,27,'Modified By','CONCAT(modifiedby27.FirstName, \" \", modifiedby27.LastName)',NULL,'N','individual modifiedby27','modifiedby27.individualid = item.modifiedby','CONCAT(modifiedby27.FirstName, \" \", modifiedby27.LastName)',0,'N',NULL,'Y'),(377,27,'Created Date','createddate',NULL,'Y','','','createddate',0,'Y',NULL,'Y'),(378,27,'Modified Date','modifieddate',NULL,'Y','','','modifieddate',0,'Y',NULL,'Y'),(379,27,'GLAccount ID','glaccountid27.title',NULL,'N','glaccount glaccountid27','glaccountid27.glaccountid=item.glaccountid','glaccountid27.title',0,'N',NULL,'Y'),(381,27,'Link To Inventory','linktoinventory',NULL,'Y','','','linktoinventory',0,'Y',NULL,'Y'),(382,28,'Title','title',NULL,'Y','','','title',0,'Y',NULL,'Y'),(383,28,'Type','type',NULL,'Y','','','type',0,'Y',NULL,'Y'),(384,28,'Balance','balance',NULL,'Y','','','balance',0,'Y',NULL,'Y'),(385,28,'Parent Account','glaccount28.title',NULL,'N','glaccount glaccount28','glaccount28.glaccountid=glaccount28.parent','glaccount28.title',0,'N',NULL,'Y'),(386,29,'Inventory ID','inventoryid',NULL,'Y','','','inventoryid',0,'Y',NULL,'Y'),(387,29,'Title','title',NULL,'Y','','','title',0,'Y',NULL,'Y'),(388,29,'Description','description',NULL,'Y','','','description',0,'Y',NULL,'Y'),(389,29,'Quantity','qty',NULL,'Y','','','qty',0,'Y',NULL,'Y'),(390,29,'Location ID','location29.title',NULL,'N','location location29','inventory.locationid=location29.locationid','location29.title',0,'N',NULL,'Y'),(391,29,'Item','title',NULL,'Y','','inventory.item=item.itemid','title',0,'Y',NULL,'Y'),(392,29,'Status ID','inventorystatus29.statusname',NULL,'N','inventorystatus inventorystatus29','inventorystatus29.statusid=inventory.statusid','inventorystatus29.statusname',0,'N',NULL,'Y'),(393,29,'Customer ID','entity29.name',NULL,'N','entity entity29','entity29.EntityID = inventory.customerid','entity29.name',0,'N',NULL,'Y'),(394,29,'Created','created',NULL,'Y','','','created',0,'Y',NULL,'Y'),(395,29,'Modified','modified',NULL,'Y','','','modified',0,'Y',NULL,'Y'),(396,29,'Vendor','entityv29.name',NULL,'N','entity entityv29','inventory.vendor = entityv29.entityid','entityv29.name',0,'N',NULL,'Y'),(399,30,'Entity','entityven30.name',NULL,'N','entity entityven30','vendor.entityid = entityven30.entityid','entityven30.name',0,'N',NULL,'Y'),(400,31,'Expense Form ID','expenseformid',NULL,'Y','','','expenseformid',0,'Y',NULL,'Y'),(401,31,'From Date','fromdate',NULL,'Y','','','fromdate',0,'Y',NULL,'Y'),(402,31,'To Date','todate',NULL,'Y','','','todate',0,'Y',NULL,'Y'),(403,31,'Description','description',NULL,'Y','','','description',0,'Y',NULL,'Y'),(404,31,'Note','note',NULL,'Y','','','note',0,'Y',NULL,'Y'),(405,31,'Reporting To','CONCAT(reportingto31.FirstName, \" \", reportingto31.LastName)',NULL,'N','individual reportingto31','reportingto31.individualid = Expenseform.ReportingTo','CONCAT(reportingto31.FirstName, \" \", reportingto31.LastName)',0,'N',NULL,'Y'),(406,31,'Owner','CONCAT(owner31.FirstName, \" \", owner31.LastName)',NULL,'N','individual owner31','owner31.individualid = Expenseform.owner','CONCAT(owner31.FirstName, \" \", owner31.LastName)',0,'N',NULL,'Y'),(407,31,'Creator','CONCAT(creator31.FirstName, \" \", creator31.LastName)',NULL,'N','individual creator31','creator31.individualid = Expenseform.Creator','CONCAT(creator31.FirstName, \" \", creator31.LastName)',0,'N',NULL,'Y'),(408,31,'Modified By','CONCAT(modifiedby31.FirstName, \" \", modifiedby31.LastName)',NULL,'N','individual modifiedby31','modifiedby31.individualid = Expenseform.modifiedby','CONCAT(modifiedby31.FirstName, \" \", modifiedby31.LastName)',0,'N',NULL,'Y'),(409,31,'Created','created',NULL,'Y','','','created',0,'Y',NULL,'Y'),(410,31,'Modified','modified',NULL,'Y','','','modified',0,'Y',NULL,'Y'),(411,31,'Status','status',NULL,'Y','','','status',0,'Y',NULL,'Y'),(412,32,'Name','CONCAT(name32.FirstName, \" \", name32.LastName)',NULL,'N','individual name32','name32.individualid = attendee.individualid AND attendee.activityid = activity.activityid','CONCAT(name32.FirstName, \" \", name32.LastName)',0,'N',NULL,'Y'),(413,32,'Status','attendeestatus32.name',NULL,'N','attendeestatus attendeestatus32','attendeestatus32.statusid = attendee.status AND attendee.activityid = activity.activityid','attendeestatus32.name',0,'N',NULL,'Y'),(414,32,'Type','type',NULL,'Y','','attendee.activityid = activity.activityid','type',0,'Y',NULL,'Y'),(415,33,'Body','Body',NULL,'Y',NULL,NULL,'Body',0,'Y',NULL,'Y'),(416,33,'Subject','Subject',NULL,'Y',NULL,NULL,'Subject',0,'Y',NULL,'Y'),(417,33,'Sender','Sender',NULL,'Y',NULL,NULL,'Sender',0,'Y',NULL,'Y'),(418,33,'Recipient','Recipient',NULL,'Y',NULL,NULL,'Recipient',0,'Y',NULL,'Y'),(419,33,'Message Date','MessageDate',NULL,'Y',NULL,NULL,'MessageDate',0,'Y',NULL,'Y'),(420,34,'Name','name',NULL,'Y',NULL,NULL,'name',0,'Y',NULL,'Y'),(421,34,'Description','description',NULL,'Y',NULL,NULL,'description',0,'Y',NULL,'Y'),(422,34,'Enabled','enabled',NULL,'Y',NULL,NULL,'enabled',0,'Y',NULL,'Y'),(423,8,'Owned By','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.IndividualID = emailmessage.Owner','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y'),(424,8,'From Individual','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.IndividualID = emailmessage.FromIndividual','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y'),(425,8,'Headers','Headers',NULL,'Y',NULL,NULL,'Headers',0,'Y',NULL,'Y'),(426,8,'Priority','Priority',NULL,'Y',NULL,NULL,'Priority',0,'Y',NULL,'Y'),(427,8,'Size','Size',NULL,'Y',NULL,NULL,'Size',0,'Y',NULL,'Y'),(428,8,'Importance','Importance',NULL,'Y',NULL,NULL,'Importance',0,'Y',NULL,'Y'),(429,22,'Description','Description',NULL,'Y',NULL,NULL,'Description',0,'Y',NULL,'Y'),(430,22,'Literature','literature.Title',NULL,'N','literature','literature.LiteratureID = literaturerequest.LiteratureID','literature.Title',0,'N',NULL,'Y'),(431,22,'Owner','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.IndividualID = activity.Owner AND activity.ActivityID = literaturerequest.ActivityID','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y'),(432,22,'Assigned To','CONCAT(ass22.FirstName, \" \", ass22.LastName)',NULL,'N','individual ass22','ass22.IndividualID = literaturerequest.RequestedBy','CONCAT(ass22.FirstName, \" \", ass22.LastName)',0,'N',NULL,'Y'),(433,21,'PromotionID','PromotionID',NULL,'Y',NULL,NULL,'PromotionID',0,'Y',NULL,'Y'),(434,21,'Notes','Notes',NULL,'Y',NULL,NULL,'Notes',0,'Y',NULL,'Y'),(435,21,'Owner','CONCAT(ind21.FirstName, \" \", ind21.LastName)',NULL,'N','individual ind21','ind21.IndividualID = promotion.Owner','CONCAT(ind21.FirstName, \" \", ind21.LastName)',0,'N',NULL,'Y'),(436,23,'EventID','EventID',NULL,'Y',NULL,NULL,'EventID',0,'Y',NULL,'Y'),(437,23,'Title','Title',NULL,'Y',NULL,NULL,'Title',0,'Y',NULL,'Y'),(438,23,'Description','Detail',NULL,'Y',NULL,NULL,'Detail',0,'Y',NULL,'Y'),(439,23,'Who Should Attend','ForMember',NULL,'Y',NULL,NULL,'ForMember',0,'Y',NULL,'Y'),(440,23,'Owner','CONCAT(own23.FirstName, \" \", own23.LastName)',NULL,'N','individual own23','own23.IndividualID = event.Owner','CONCAT(own23.FirstName, \" \", own23.LastName)',0,'N',NULL,'Y'),(441,23,'Modified By','CONCAT(mod23.FirstName, \" \", mod23.LastName)',NULL,'N','individual mod23','mod23.IndividualID = event.ModifiedBy','CONCAT(mod23.FirstName, \" \", mod23.LastName)',0,'N',NULL,'Y'),(443,36,'Time Sheet ID','TimeSheetID',NULL,'Y',NULL,NULL,'TimeSheetID',0,'Y',NULL,'Y'),(444,36,'Description','Description',NULL,'Y',NULL,NULL,'Description',0,'Y',NULL,'Y'),(445,36,'Owner','CONCAT(individual33.FirstName, \" \", individual33.LastName)',NULL,'N','individual individual33','individual33.IndividualID = timesheet.Owner','CONCAT(individual33.FirstName, \" \", individual33.LastName)',0,'N',NULL,'Y'),(446,36,'Creator','CONCAT(Creator33.FirstName, \" \", Creator33.LastName)',NULL,'N','individual Creator33','Creator33.IndividualID = timesheet.Creator','CONCAT(Creator33.FirstName, \" \", Creator33.LastName)',0,'N',NULL,'Y'),(447,36,'Modified By','CONCAT(ModifiedBy33.FirstName, \" \", ModifiedBy33.LastName)',NULL,'N','individual ModifiedBy33','ModifiedBy33.IndividualID = timesheet.ModifiedBy','CONCAT(ModifiedBy33.FirstName, \" \", ModifiedBy33.LastName)',0,'N',NULL,'Y'),(448,36,'Modified By','CONCAT(ReportingTo33.FirstName, \" \", ReportingTo33.LastName)',NULL,'N','individual ReportingTo33','ReportingTo33.IndividualID = timesheet.ReportingTo','CONCAT(ReportingTo33.FirstName, \" \", ReportingTo33.LastName)',0,'N',NULL,'Y'),(449,36,'Created','Created',NULL,'Y',NULL,NULL,'Created',0,'Y',NULL,'Y'),(450,36,'Modified','Modified',NULL,'Y',NULL,NULL,'Modified',0,'Y',NULL,'Y'),(451,36,'Start','Start',NULL,'Y',NULL,NULL,'Start',0,'Y',NULL,'Y'),(452,36,'End','End',NULL,'Y',NULL,NULL,'End',0,'Y',NULL,'Y'),(453,36,'Notes','Notes',NULL,'Y',NULL,NULL,'Notes',0,'Y',NULL,'Y'),(455,37,'Creation Date','createDate',NULL,'Y',NULL,NULL,'createDate',0,'Y',NULL,'Y'),(456,37,'Modified Date','modifyDate',NULL,'Y',NULL,NULL,'modifyDate',0,'Y',NULL,'Y'),(457,37,'Group ID','GroupID',NULL,'Y',NULL,NULL,'GroupID',0,'Y',NULL,'Y'),(458,37,'Name','Name',NULL,'Y',NULL,NULL,'Name',0,'Y',NULL,'Y'),(459,37,'Description','Description',NULL,'Y',NULL,NULL,'Description',0,'Y',NULL,'Y'),(460,37,'Owned By','CONCAT(Owned16.FirstName, \" \", Owned16.LastName)',NULL,'N','individual Owned16','Owned16.IndividualID = grouptbl.owner','CONCAT(Owned16.FirstName, \" \", Owned16.LastName)',0,'N',NULL,'Y'),(461,28,'GLAccounts ID','GLAccountsID',NULL,'Y',NULL,NULL,'GLAccountsID',0,'Y',NULL,'Y'),(462,28,'External ID','externalID',NULL,'Y',NULL,NULL,'externalID',0,'Y',NULL,'Y'),(463,28,'Name','Name',NULL,'Y',NULL,NULL,'Name',0,'Y',NULL,'Y'),(464,28,'Description','Description',NULL,'Y',NULL,NULL,'Description',0,'Y',NULL,'Y'),(465,35,'Individual','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.entity = 1','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y'),(466,1,'Email','mocEmail.Content',NULL,'N','mocrelate mocRelateEmail,methodofcontact mocEmail','entity.entityID = mocRelateEmail.ContactId AND mocRelateEmail.MOCID = mocEmail.MOCID AND mocRelateEmail.ContactType = 1 AND mocEmail.MOCType = 1','mocEmail.Content',0,'N',' AND mocRelateEmail.IsPrimary = \'YES\'','Y'),(467,1,'Fax','mocFax.Content',NULL,'N','mocrelate mocRelateFax,methodofcontact mocFax','entity.entityID = mocRelateFax.ContactId AND mocRelateFax.MOCID = mocFax.MOCID AND mocRelateFax.ContactType = 1 AND mocFax.MOCType = 2','mocFax.Content',6,'N',' AND mocRelateFax.IsPrimary = \'YES\'','Y'),(468,1,'Mobile','mocMobile.Content',NULL,'N','mocrelate mocRelateMobile,methodofcontact mocMobile','entity.entityID = mocRelateMobile.ContactId AND mocRelateMobile.MOCID = mocMobile.MOCID AND mocRelateMobile.ContactType = 1 AND mocMobile.MOCType = 3','mocMobile.Content',6,'N',' AND mocRelateMobile.IsPrimary = \'YES\'','Y'),(469,1,'Main','mocMain.Content',NULL,'N','mocrelate mocRelateMain,methodofcontact mocMain','entity.entityID = mocRelateMain.ContactId AND mocRelateMain.MOCID = mocMain.MOCID AND mocRelateMain.ContactType = 1 AND mocMain.MOCType = 4','mocMain.Content',6,'N',' AND mocRelateMain.IsPrimary = \'YES\'','Y'),(470,1,'Home','mocHome.Content',NULL,'N','mocrelate mocRelateHome,methodofcontact mocHome','entity.entityID = mocRelateHome.ContactId AND mocRelateHome.MOCID = mocHome.MOCID AND mocRelateHome.ContactType = 1 AND mocHome.MOCType = 5','mocHome.Content',6,'N',' AND mocRelateHome.IsPrimary = \'YES\'','Y'),(471,1,'Other','mocOther.Content',NULL,'N','mocrelate mocRelateOther,methodofcontact mocOther','entity.entityID = mocRelateOther.ContactId AND mocRelateOther.MOCID = mocOther.MOCID AND mocRelateOther.ContactType = 1 AND mocOther.MOCType = 6','mocOther.Content',6,'N',' AND mocRelateOther.IsPrimary = \'YES\'','Y'),(472,1,'Pager','mocPager.Content',NULL,'N','mocrelate mocRelatePager,methodofcontact mocPager','entity.entityID = mocRelatePager.ContactId AND mocRelatePager.MOCID = mocPager.MOCID AND mocRelatePager.ContactType = 1 AND mocPager.MOCType = 7','mocPager.Content',6,'N',' AND mocRelatePager.IsPrimary = \'YES\'','Y'),(473,1,'Work','mocWork.Content',NULL,'N','mocrelate mocRelateWork,methodofcontact mocWork','entity.entityID = mocRelateWork.ContactId AND mocRelateWork.MOCID = mocWork.MOCID AND mocRelateWork.ContactType = 1 AND mocWork.MOCType = 8','mocWork.Content',6,'N',' AND mocRelateWork.IsPrimary = \'YES\'','Y'),(474,2,'Email','mocEmail.Content',NULL,'N','mocrelate mocRelateEmail,methodofcontact mocEmail','individual.IndividualID = mocRelateEmail.ContactId AND mocRelateEmail.MOCID = mocEmail.MOCID AND mocRelateEmail.ContactType = 2 AND mocEmail.MOCType = 1','mocEmail.Content',0,'N',' AND mocRelateEmail.IsPrimary = \'YES\'','Y'),(475,2,'Fax','mocFax.Content',NULL,'N','mocrelate mocRelateFax,methodofcontact mocFax','individual.IndividualID = mocRelateFax.ContactId AND mocRelateFax.MOCID = mocFax.MOCID AND mocRelateFax.ContactType = 2 AND mocFax.MOCType = 2','mocFax.Content',6,'N',' AND mocRelateFax.IsPrimary = \'YES\'','Y'),(476,2,'Mobile','mocMobile.Content',NULL,'N','mocrelate mocRelateMobile,methodofcontact mocMobile','individual.IndividualID = mocRelateMobile.ContactId AND mocRelateMobile.MOCID = mocMobile.MOCID AND mocRelateMobile.ContactType = 2 AND mocMobile.MOCType = 3','mocMobile.Content',6,'N',' AND mocRelateMobile.IsPrimary = \'YES\'','Y'),(477,2,'Main','mocMain.Content',NULL,'N','mocrelate mocRelateMain,methodofcontact mocMain','individual.IndividualID = mocRelateMain.ContactId AND mocRelateMain.MOCID = mocMain.MOCID AND mocRelateMain.ContactType = 2 AND mocMain.MOCType = 4','mocMain.Content',6,'N',' AND mocRelateMain.IsPrimary = \'YES\'','Y'),(478,2,'Home','mocHome.Content',NULL,'N','mocrelate mocRelateHome,methodofcontact mocHome','individual.IndividualID = mocRelateHome.ContactId AND mocRelateHome.MOCID = mocHome.MOCID AND mocRelateHome.ContactType = 2 AND mocHome.MOCType = 5','mocHome.Content',6,'N',' AND mocRelateHome.IsPrimary = \'YES\'','Y'),(479,2,'Other','mocOther.Content',NULL,'N','mocrelate mocRelateOther,methodofcontact mocOther','individual.IndividualID = mocRelateOther.ContactId AND mocRelateOther.MOCID = mocOther.MOCID AND mocRelateOther.ContactType = 2 AND mocOther.MOCType = 6','mocOther.Content',6,'N',' AND mocRelateOther.IsPrimary = \'YES\'','Y'),(480,2,'Pager','mocPager.Content',NULL,'N','mocrelate mocRelatePager,methodofcontact mocPager','individual.IndividualID = mocRelatePager.ContactId AND mocRelatePager.MOCID = mocPager.MOCID AND mocRelatePager.ContactType = 2 AND mocPager.MOCType = 7','mocPager.Content',6,'N',' AND mocRelatePager.IsPrimary = \'YES\'','Y'),(481,2,'Work','mocWork.Content',NULL,'N','mocrelate mocRelateWork,methodofcontact mocWork','individual.IndividualID = mocRelateWork.ContactId AND mocRelateWork.MOCID = mocWork.MOCID AND mocRelateWork.ContactType = 2 AND mocWork.MOCType = 8','mocWork.Content',6,'N',' AND mocRelateWork.IsPrimary = \'YES\'','Y'),(482,1,'Street 1','addressStreet1.Street1',NULL,'N','addressrelate addressRelateStreet1,address addressStreet1','entity.entityID = addressRelateStreet1.Contact AND addressRelateStreet1.Address = addressStreet1.AddressID AND addressRelateStreet1.ContactType = 1','addressStreet1.Street1',0,'N',' AND addressRelateStreet1.IsPrimary = \'YES\'','Y'),(483,1,'Street 2','addressStreet2.Street2',NULL,'N','addressrelate addressRelateStreet2,address addressStreet2','entity.entityID = addressRelateStreet2.Contact AND addressRelateStreet2.Address = addressStreet2.AddressID AND addressRelateStreet2.ContactType = 1','addressStreet2.Street2',0,'N',' AND addressRelateStreet2.IsPrimary = \'YES\'','Y'),(484,1,'State','addressState.state',NULL,'N','addressrelate addressRelateState,address addressState','entity.entityID = addressRelateState.Contact AND addressRelateState.Address = addressState.AddressID AND addressRelateState.ContactType = 1','addressState.state',0,'N',' AND addressRelateState.IsPrimary = \'YES\'','Y'),(485,1,'Zip Code','addressZip.Zip',NULL,'N','addressrelate addressRelateZip,address addressZip','entity.entityID = addressRelateZip.Contact AND addressRelateZip.Address = addressZip.AddressID AND addressRelateZip.ContactType = 1','addressZip.Zip',0,'N',' AND addressRelateZip.IsPrimary = \'YES\'','Y'),(486,1,'Country','addressCountry.country',NULL,'N','addressrelate addressRelateCountry,address addressCountry','entity.entityID = addressRelateCountry.Contact AND addressRelateCountry.Address = addressCountry.AddressID AND addressRelateCountry.ContactType = 1','addressCountry.country',0,'N',' AND addressRelateCountry.IsPrimary = \'YES\'','Y'),(487,1,'Website','addressWebsite.Website',NULL,'N','addressrelate addressRelateWebsite,address addressWebsite','entity.entityID = addressRelateWebsite.Contact AND addressRelateWebsite.Address = addressWebsite.AddressID AND addressRelateWebsite.ContactType = 1','addressWebsite.Website',0,'N',' AND addressRelateWebsite.IsPrimary = \'YES\'','Y'),(488,2,'Street 1','addressStreet1.Street1',NULL,'N','addressrelate addressRelateStreet1,address addressStreet1','individual.IndividualID  = addressRelateStreet1.Contact AND addressRelateStreet1.Address = addressStreet1.AddressID AND addressRelateStreet1.ContactType = 2','addressStreet1.Street1',0,'N',' AND addressRelateStreet1.IsPrimary = \'YES\'','Y'),(489,2,'Street 2','addressStreet2.Street2',NULL,'N','addressrelate addressRelateStreet2,address addressStreet2','individual.IndividualID  = addressRelateStreet2.Contact AND addressRelateStreet2.Address = addressStreet2.AddressID AND addressRelateStreet2.ContactType = 2','addressStreet2.Street2',0,'N',' AND addressRelateStreet2.IsPrimary = \'YES\'','Y'),(490,2,'State','addressState.state',NULL,'N','addressrelate addressRelateState,address addressState','individual.IndividualID  = addressRelateState.Contact AND addressRelateState.Address = addressState.AddressID AND addressRelateState.ContactType = 2','addressState.state',0,'N',' AND addressRelateState.IsPrimary = \'YES\'','Y'),(491,2,'Zip Code','addressZip.Zip',NULL,'N','addressrelate addressRelateZip,address addressZip','individual.IndividualID  = addressRelateZip.Contact AND addressRelateZip.Address = addressZip.AddressID AND addressRelateZip.ContactType = 2','addressZip.Zip',0,'N',' AND addressRelateZip.IsPrimary = \'YES\'','Y'),(492,2,'Country','addressCountry.country',NULL,'N','addressrelate addressRelateCountry,address addressCountry','individual.IndividualID  = addressRelateCountry.Contact AND addressRelateCountry.Address = addressCountry.AddressID AND addressRelateCountry.ContactType = 2','addressCountry.country',0,'N',' AND addressRelateCountry.IsPrimary = \'YES\'','Y'),(493,2,'Website','addressWebsite.Website',NULL,'N','addressrelate addressRelateWebsite,address addressWebsite','individual.IndividualID  = addressRelateWebsite.Contact AND addressRelateWebsite.Address = addressWebsite.AddressID AND addressRelateWebsite.ContactType = 2','addressWebsite.Website',0,'N',' AND addressRelateWebsite.IsPrimary = \'YES\'','Y'),(494,18,'Street 1','addressStreet1.Street1',NULL,'N','address addressStreet1','(addressStreet1.AddressID = cvorder.BillAddress OR addressStreet1.AddressID = cvorder.ShipAddress)','addressStreet1.Street1',0,'N',' AND addressRelateStreet1.IsPrimary = \'YES\'','Y'),(495,18,'Street 2','addressStreet2.Street2',NULL,'N','address addressStreet2','(addressStreet2.AddressID = cvorder.BillAddress OR addressStreet2.AddressID = cvorder.ShipAddress)','addressStreet2.Street2',0,'N',' AND addressRelateStreet2.IsPrimary = \'YES\'','Y'),(496,18,'State','addressState.state',NULL,'N','address addressState','(addressState.AddressID = cvorder.BillAddress OR addressState.AddressID = cvorder.ShipAddress)','addressState.state',0,'N',' AND addressRelateState.IsPrimary = \'YES\'','Y'),(497,18,'Zip Code','addressZip.Zip',NULL,'N','address addressZip','(addressZip.AddressID = cvorder.BillAddress OR addressZip.AddressID = cvorder.ShipAddress)','addressZip.Zip',0,'N',' AND addressRelateZip.IsPrimary = \'YES\'','Y'),(498,18,'Country','addressCountry.country',NULL,'N','address addressCountry','(addressCountry.AddressID = cvorder.BillAddress OR addressCountry.AddressID = cvorder.ShipAddress)','addressCountry.country',0,'N',' AND addressRelateCountry.IsPrimary = \'YES\'','Y'),(499,18,'Website','addressWebsite.Website',NULL,'N','address addressWebsite','(addressWebsite.AddressID = cvorder.BillAddress OR addressWebsite.AddressID = cvorder.ShipAddress)','addressWebsite.Website',0,'N',' AND addressRelateWebsite.IsPrimary = \'YES\'','Y'),(500,19,'Street 1','addressStreet1.Street1',NULL,'N','address addressStreet1','(addressStreet1.AddressID = invoice.billaddress OR addressStreet1.AddressID = invoice.shipaddress)','addressStreet1.Street1',0,'N',' AND addressRelateStreet1.IsPrimary = \'YES\'','Y'),(501,19,'Street 2','addressStreet2.Street2',NULL,'N','address addressStreet2','(addressStreet2.AddressID = invoice.billaddress OR addressStreet2.AddressID = invoice.shipaddress)','addressStreet2.Street2',0,'N',' AND addressRelateStreet2.IsPrimary = \'YES\'','Y'),(502,19,'State','addressState.state',NULL,'N','address addressState','(addressState.AddressID = invoice.billaddress OR addressState.AddressID = invoice.shipaddress)','addressState.state',0,'N',' AND addressRelateState.IsPrimary = \'YES\'','Y'),(503,19,'Zip Code','addressZip.Zip',NULL,'N','address addressZip','(addressZip.AddressID = invoice.billaddress OR addressZip.AddressID = invoice.shipaddress)','addressZip.Zip',0,'N',' AND addressRelateZip.IsPrimary = \'YES\'','Y'),(504,19,'Country','addressCountry.country',NULL,'N','address addressCountry','(addressCountry.AddressID = invoice.billaddress OR addressCountry.AddressID = invoice.shipaddress)','addressCountry.country',0,'N',' AND addressRelateCountry.IsPrimary = \'YES\'','Y'),(505,19,'Website','addressWebsite.Website',NULL,'N','address addressWebsite','(addressWebsite.AddressID = invoice.billaddress OR addressWebsite.AddressID = invoice.shipaddress)','addressWebsite.Website',0,'N',' AND addressRelateWebsite.IsPrimary = \'YES\'','Y'),(506,26,'Street 1','addressStreet1.Street1',NULL,'N','address addressStreet1','(addressStreet1.AddressID = purchaseorder.BillAddress OR addressStreet1.AddressID = purchaseorder.ShipAddress)','addressStreet1.Street1',0,'N',' AND addressRelateStreet1.IsPrimary = \'YES\'','Y'),(507,26,'Street 2','addressStreet2.Street2',NULL,'N','address addressStreet2','(addressStreet2.AddressID = purchaseorder.BillAddress OR addressStreet2.AddressID = purchaseorder.ShipAddress)','addressStreet2.Street2',0,'N',' AND addressRelateStreet2.IsPrimary = \'YES\'','Y'),(508,26,'State','addressState.state',NULL,'N','address addressState','(addressState.AddressID = purchaseorder.BillAddress OR addressState.AddressID = purchaseorder.ShipAddress)','addressState.state',0,'N',' AND addressRelateState.IsPrimary = \'YES\'','Y'),(509,26,'Zip Code','addressZip.Zip',NULL,'N','address addressZip','(addressZip.AddressID = purchaseorder.BillAddress OR addressZip.AddressID = purchaseorder.ShipAddress)','addressZip.Zip',0,'N',' AND addressRelateZip.IsPrimary = \'YES\'','Y'),(510,26,'Country','addressCountry.country',NULL,'N','address addressCountry','(addressCountry.AddressID = purchaseorder.BillAddress OR addressCountry.AddressID = purchaseorder.ShipAddress)','addressCountry.country',0,'N',' AND addressRelateCountry.IsPrimary = \'YES\'','Y'),(511,26,'Website','addressWebsite.Website',NULL,'N','address addressWebsite','(addressWebsite.AddressID = purchaseorder.BillAddress OR addressWebsite.AddressID = purchaseorder.ShipAddress)','addressWebsite.Website',0,'N',' AND addressRelateWebsite.IsPrimary = \'YES\'','Y'),(512,11,'Street 1','addressStreet1.Street1',NULL,'N','address addressStreet1','(addressStreet1.AddressID = proposal.Billingid OR addressStreet1.AddressID = proposal.Shippingid)','addressStreet1.Street1',0,'N',' AND addressRelateStreet1.IsPrimary = \'YES\'','Y'),(513,11,'Street 2','addressStreet2.Street2',NULL,'N','address addressStreet2','(addressStreet2.AddressID = proposal.Billingid OR addressStreet2.AddressID = proposal.Shippingid)','addressStreet2.Street2',0,'N',' AND addressRelateStreet2.IsPrimary = \'YES\'','Y'),(514,11,'State','addressState.state',NULL,'N','address addressState','(addressState.AddressID = proposal.Billingid OR addressState.AddressID = proposal.Shippingid)','addressState.state',0,'N',' AND addressRelateState.IsPrimary = \'YES\'','Y'),(515,11,'Zip Code','addressZip.Zip',NULL,'N','address addressZip','(addressZip.AddressID = proposal.Billingid OR addressZip.AddressID = proposal.Shippingid)','addressZip.Zip',0,'N',' AND addressRelateZip.IsPrimary = \'YES\'','Y'),(516,11,'Country','addressCountry.country',NULL,'N','address addressCountry','(addressCountry.AddressID = proposal.Billingid OR addressCountry.AddressID = proposal.Shippingid)','addressCountry.country',0,'N',' AND addressRelateCountry.IsPrimary = \'YES\'','Y'),(517,11,'Website','addressWebsite.Website',NULL,'N','address addressWebsite','(addressWebsite.AddressID = proposal.Billingid OR addressWebsite.AddressID = proposal.Shippingid)','addressWebsite.Website',0,'N',' AND addressRelateWebsite.IsPrimary = \'YES\'','Y'),(518,2,'City','addressCity.city',NULL,'N','addressrelate addressRelateCity, address addressCity','individual.individualID = addressRelateCity.Contact AND addressRelateCity.Address = addressCity.AddressID AND addressRelateCity.contactType = 2','addressCity.city',0,'N','AND addressRelateCity.IsPrimary = \'YES\'','Y'),(519,1,'City','addressCity.City',NULL,'N','addressrelate addressRelateCity, address addressCity','entity.entityID = addressRelateCity.Contact AND addressRelateCity.Address = addressCity.AddressID AND addressRelateCity.ContactType = 1','addressCity.City',0,'N',' AND addressRelateCity.IsPrimary = \'YES\'','Y');

--
-- Table structure for table `searchmodule`
--

DROP TABLE IF EXISTS `searchmodule`;
CREATE TABLE `searchmodule` (
  `ModuleID` int(10) unsigned NOT NULL default '0',
  `SearchTableID` int(11) unsigned NOT NULL default '0',
  `IsPrimaryTable` enum('N','Y') NOT NULL default 'N',
  UNIQUE KEY `SearchModuleID` (`ModuleID`,`SearchTableID`),
  KEY `ModuleID` (`ModuleID`),
  KEY `SearchTableID` (`SearchTableID`),
  CONSTRAINT `0_21595` FOREIGN KEY (`ModuleID`) REFERENCES `module` (`moduleid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `0_21596` FOREIGN KEY (`SearchTableID`) REFERENCES `searchtable` (`SearchTableID`) ON DELETE CASCADE ON UPDATE CASCADE
) TYPE=InnoDB;

--
-- Dumping data for table `searchmodule`
--

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (2,8,'Y'),(3,6,'Y'),(5,1,'N'),(5,2,'N'),(5,7,'Y'),(6,9,'Y'),(14,1,'Y'),(14,2,'N'),(14,5,'N'),(14,6,'N'),(14,7,'N'),(14,8,'N'),(14,9,'N'),(14,10,'N'),(14,11,'N'),(14,12,'N'),(14,13,'N'),(14,14,'N'),(14,15,'N'),(14,18,'N'),(14,19,'N'),(15,1,'N'),(15,2,'Y'),(15,5,'N'),(15,6,'N'),(15,7,'N'),(15,8,'N'),(15,9,'N'),(15,10,'N'),(15,11,'N'),(15,12,'N'),(15,13,'N'),(15,14,'N'),(15,15,'N'),(15,18,'N'),(15,19,'N'),(16,2,'N'),(16,37,'Y'),(17,6,'N'),(18,6,'N'),(21,6,'N'),(22,6,'N'),(23,6,'N'),(30,10,'Y'),(30,11,'N'),(31,10,'N'),(31,11,'Y'),(32,1,'N'),(32,2,'N'),(32,20,'Y'),(33,21,'Y'),(33,27,'N'),(34,6,'N'),(34,22,'Y'),(35,1,'N'),(35,2,'N'),(35,23,'Y'),(36,9,'N'),(36,12,'Y'),(36,13,'N'),(36,15,'N'),(37,13,'Y'),(37,15,'N'),(38,15,'Y'),(39,5,'N'),(39,14,'Y'),(39,15,'N'),(40,16,'Y'),(41,17,'Y'),(42,18,'Y'),(42,19,'N'),(42,27,'N'),(43,24,'Y'),(44,25,'Y'),(44,27,'N'),(45,26,'Y'),(45,27,'N'),(46,27,'Y'),(47,28,'Y'),(48,29,'Y'),(50,1,'N'),(50,2,'N'),(50,30,'Y'),(51,25,'N'),(51,31,'Y'),(52,36,'Y'),(54,35,'Y'),(56,19,'Y'),(56,27,'N'),(79,34,'Y');

--
-- Table structure for table `searchtable`
--

DROP TABLE IF EXISTS `searchtable`;
CREATE TABLE `searchtable` (
  `SearchTableID` int(11) unsigned NOT NULL auto_increment,
  `DisplayName` varchar(255) default NULL,
  `TableName` varchar(255) default NULL,
  `TablePrimaryKey` varchar(255) default NULL,
  `RecordPermissionQuery` varchar(255) default NULL,
  `IsOnGobalReplaceTable` enum('N','Y') NOT NULL default 'N',
  PRIMARY KEY  (`SearchTableID`),
  UNIQUE KEY `SearchTableID` (`SearchTableID`)
) TYPE=InnoDB;

--
-- Dumping data for table `searchtable`
--

INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (1,'Entity','entity','EntityID',NULL,'Y'),(2,'Individual','individual','IndividualID',NULL,'Y'),(3,'Method Of Contact','methodofcontact','MOCID',NULL,'N'),(4,'Address','address','AddressID',NULL,'N'),(5,'Custom Fields','customfield','CustomFieldID',NULL,'N'),(6,'Activities','activity','ActivityID',NULL,'N'),(7,'Notes','note','NoteID',NULL,'N'),(8,'Email','emailmessage','MessageID',NULL,'N'),(9,'Files','cvfile','FileID',NULL,'N'),(10,'Opportunities','opportunity','OpportunityID',NULL,'N'),(11,'Proposals','proposal','ProposalID',NULL,'N'),(12,'Projects','project','ProjectID',NULL,'N'),(13,'Tasks','task','ActivityID',NULL,'N'),(14,'Tickets','ticket','ticketid',NULL,'N'),(15,'Timeslips','timeslip','TimeSlipID',NULL,'N'),(16,'FAQs','faq','faqid',NULL,'N'),(17,'Knowledgebase','knowledgebase','kbid',NULL,'N'),(18,'Order History','cvorder','orderid',NULL,'N'),(19,'Invoice History','invoice','InvoiceID',NULL,'N'),(20,'Marketing List','marketinglist','ListId','','N'),(21,'Promotions','promotion','PromotionID','','N'),(22,'Literature Request','literaturerequest','ActivityID','','N'),(23,'Event','event','EventId','','N'),(24,'Payment','payment','paymentid','','N'),(25,'Expense','expense','expenseid','','N'),(26,'Purchase Order','purchaseorder','purchaseorderid','','N'),(27,'Item','item','itemid','','N'),(28,'GLAccount','glaccount','glaccountsid','','N'),(29,'Inventory','inventory','inventoryid','','N'),(30,'Vendor','vendor','entityid','','N'),(31,'Expense Form','expenseform','expenseformid','','N'),(32,'Attendee','attendee','ActivityID','','N'),(33,'RuleCriteria','','',NULL,'N'),(34,'Rule','emailrule','ruleID',NULL,'N'),(35,'Employee','individual','IndividualID',NULL,'N'),(36,'TimeSheet','timesheet','TimeSheetID',NULL,'N'),(37,'Group','grouptbl','GroupID',NULL,'N');

--
-- Table structure for table `searchtablerelate`
--

DROP TABLE IF EXISTS `searchtablerelate`;
CREATE TABLE `searchtablerelate` (
  `LeftSearchTableID` int(11) unsigned NOT NULL default '0',
  `RightSearchTableID` int(11) unsigned NOT NULL default '0',
  `RelationshipQuery` varchar(255) default NULL,
  UNIQUE KEY `SearchRelationshipID` (`LeftSearchTableID`,`RightSearchTableID`),
  KEY `LeftSearchTableID` (`LeftSearchTableID`),
  KEY `RightSearchTableID` (`RightSearchTableID`),
  CONSTRAINT `0_21600` FOREIGN KEY (`LeftSearchTableID`) REFERENCES `searchtable` (`SearchTableID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `0_21601` FOREIGN KEY (`RightSearchTableID`) REFERENCES `searchtable` (`SearchTableID`) ON DELETE CASCADE ON UPDATE CASCADE
) TYPE=InnoDB;

--
-- Dumping data for table `searchtablerelate`
--

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (2,1,'individual.Entity = entity.entityID'),(6,1,'activity.ActivityID = activitylink.ActivityID AND activitylink.RecordTypeID = 1 AND activitylink.RecordID = entity.EntityID'),(6,2,'activity.ActivityID = activitylink.ActivityID AND activitylink.RecordTypeID = 2 AND activitylink.RecordID = individual.IndividualID'),(7,1,'note.RelateEntity = entity.EntityID'),(7,2,'note.RelateIndividual = individual.IndividualID'),(8,1,'emailmessage.AccountID = emailaccount.AccountID AND emailaccount.Owner = individual.IndividualID AND individual.Entity = entity.EntityID'),(8,2,'emailmessage.AccountID = emailaccount.AccountID AND emailaccount.Owner = individual.IndividualID'),(9,1,'cvfile.RelateEntity = entity.EntityID'),(9,2,'cvfile.RelateIndividual = individual.IndividualID'),(10,1,'opportunity.EntityID = entity.EntityID'),(10,2,'opportunity.IndividualID = individual.IndividualID'),(11,1,'proposal.OpportunityID = opportunity.OpportunityID AND opportunity.EntityID = entity.EntityID'),(11,2,'proposal.OpportunityID = opportunity.OpportunityID AND opportunity.IndividualID = individual.IndividualID'),(11,10,'proposal.OpportunityID = opportunity.OpportunityID'),(12,1,'project.ProjectID = projectlink.ProjectID AND projectlink.RecordTypeID = 14 AND projectlink.RecordID = entity.EntityID'),(12,2,'project.Owner = individual.IndividualID'),(12,9,'cvfile.FileID = cvfilelink.FileID and cvfilelink.RecordTypeID = 36 AND project.ProjectID = cvfilelink.RecordID'),(13,1,'task.ProjectID = project.ProjectID AND project.ProjectID = projectlink.ProjectID AND projectlink.RecordTypeID = 14 AND projectlink.RecordID = entity.EntityID'),(13,2,'task.ProjectID = project.ProjectID AND project.Owner = individual.IndividualID'),(13,12,'task.ProjectID = project.ProjectID'),(14,1,'ticket.entityid = entity.EntityID'),(14,2,'ticket.individualid = individual.IndividualID'),(15,1,'timeslip.ProjectID = project.ProjectID AND project.ProjectID = projectlink.ProjectID AND projectlink.RecordTypeID = 14 AND projectlink.RecordID = entity.EntityID'),(15,2,'timeslip.ProjectID = project.ProjectID AND project.Owner = individual.IndividualID'),(15,12,'timeslip.ProjectID = project.ProjectID'),(15,13,'timeslip.ActivityID = task.ActivityID'),(15,14,'timeslip.TicketID = ticket.ticketid'),(18,1,'cvorder.entityid = entity.EntityID'),(18,2,'cvorder.entityid = entity.EntityID AND individual.Entity = entity.EntityID'),(18,27,'item.itemid=orderitem.itemid AND orderitem.orderid = cvorder.orderid'),(19,1,'invoice.CustomerID = entity.EntityID'),(19,2,'invoice.CustomerID = entity.EntityID AND individual.Entity = entity.EntityID'),(19,18,'invoice.OrderID = cvorder.orderid'),(19,27,'item.itemid=invoiceitems.itemid AND invoiceitems.InvoiceID = invoice.InvoiceID'),(20,1,'marketinglist.ListID = entity.list'),(20,2,'marketinglist.ListID = individual.list'),(21,27,'item.itemid=promoitem.itemid AND promoitem.PromotionID = promotion.PromotionID'),(22,6,'activity.ActivityID = literaturerequest.ActivityID'),(23,1,'event.eventID = eventregister.EventID AND individual.IndividualID=eventregister.IndividualID AND entity.EntityID=individual.entity'),(23,2,'event.eventID = eventregister.EventID AND individual.IndividualID=eventregister.IndividualID'),(25,27,'item.itemid=expenseitem.ExpenseItemID AND expenseitem.ExpenseID = invoice.ExpenseID'),(26,27,'item.itemid=purchaseorderitem.itemid AND purchaseorderitem.purchaseorderid = purchaseorder.purchaseorderid'),(30,1,'entity.entityid=vendor.entityid'),(30,2,'individual.entity=vendor.entityid'),(31,25,'expenseform.ExpenseFormID=expense.ExpenseFormID'),(35,2,'individual.IndividualID=employee.IndividualID'),(37,2,'grouptbl.GroupID = member.GroupID and individual.IndividualID=member.ChildID');

--
-- Table structure for table `securityprofile`
--

DROP TABLE IF EXISTS `securityprofile`;
CREATE TABLE `securityprofile` (
  `profileid` int(11) unsigned NOT NULL auto_increment,
  `profilename` varchar(100) NOT NULL default '',
  PRIMARY KEY  (`profileid`)
) TYPE=InnoDB;

--
-- Dumping data for table `securityprofile`
--

INSERT INTO `securityprofile` (`profileid`, `profilename`) VALUES (1,'Full Access');

--
-- Table structure for table `serversettings`
--

DROP TABLE IF EXISTS `serversettings`;
CREATE TABLE `serversettings` (
  `serversettingid` int(10) unsigned NOT NULL auto_increment,
  `hostname` varchar(255) NOT NULL default '',
  `sessiontimeout` int(22) default '30',
  `workinghoursfrom` time default NULL,
  `workinghoursto` time default NULL,
  `emailcheckinterval` int(22) default '10',
  `filesystemstoragepath` varchar(50) NOT NULL default '',
  `defaulttimezone` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`serversettingid`)
) TYPE=InnoDB;

--
-- Dumping data for table `serversettings`
--

INSERT INTO `serversettings` (`serversettingid`, `hostname`, `sessiontimeout`, `workinghoursfrom`, `workinghoursto`, `emailcheckinterval`, `filesystemstoragepath`, `defaulttimezone`) VALUES (1,'chewy',120,'09:00:00','17:00:00',10,'','EST');

--
-- Table structure for table `simplesearch`
--

DROP TABLE IF EXISTS `simplesearch`;
CREATE TABLE `simplesearch` (
  `SearchID` int(11) unsigned NOT NULL auto_increment,
  `ModuleID` int(10) unsigned NOT NULL default '0',
  `OwnerID` int(10) unsigned default NULL,
  `CreatedBy` int(10) unsigned default NULL,
  `CreationDate` datetime default NULL,
  `ModifiedBy` int(10) unsigned default NULL,
  `ModifiedDate` timestamp(14) NOT NULL,
  `SearchName` varchar(255) default NULL,
  PRIMARY KEY  (`SearchID`),
  UNIQUE KEY `SearchID` (`SearchID`),
  KEY `ModuleID` (`ModuleID`),
  KEY `OwnerID` (`OwnerID`),
  KEY `CreatedBy` (`CreatedBy`),
  KEY `ModifiedBy` (`ModifiedBy`),
  CONSTRAINT `0_21668` FOREIGN KEY (`ModuleID`) REFERENCES `module` (`moduleid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `0_21669` FOREIGN KEY (`OwnerID`) REFERENCES `individual` (`IndividualID`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `0_21670` FOREIGN KEY (`CreatedBy`) REFERENCES `individual` (`IndividualID`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `0_21671` FOREIGN KEY (`ModifiedBy`) REFERENCES `individual` (`IndividualID`) ON DELETE SET NULL ON UPDATE CASCADE
) TYPE=InnoDB;

--
-- Dumping data for table `simplesearch`
--

INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (1,14,1,1,'2004-05-04 00:00:00',1,20040504120257,'entity'),(2,15,1,1,'2004-05-04 00:00:00',1,20040504120316,'individual'),(3,2,1,1,'2004-05-04 00:00:00',1,20040504120342,'Email'),(4,3,1,1,'2004-05-04 00:00:00',1,20040504120238,'Activities'),(5,5,1,1,'2004-05-04 00:00:00',1,20040504120550,'Notes'),(6,6,1,1,'2004-05-04 00:00:00',1,20040504122032,'Files'),(7,30,1,1,'2004-05-04 00:00:00',1,20040504135421,'Opportunity'),(8,31,1,1,'2004-05-04 00:00:00',1,20040504135729,'Proposal'),(9,32,1,1,'2004-05-04 00:00:00',1,20040504135924,'ListManager'),(10,33,1,1,'2004-05-04 00:00:00',1,20040504140104,'Promotion'),(11,34,1,1,'2004-05-04 00:00:00',1,20040504140315,'LiteratureRequest'),(12,35,1,1,'2004-05-04 00:00:00',1,20040504140514,'Event'),(13,36,1,1,'2004-05-04 00:00:00',1,20040504140844,'Project'),(15,38,1,1,'2004-05-04 00:00:00',1,20040504141419,'TimeSlip'),(16,39,1,1,'2004-05-04 00:00:00',1,20040504141625,'Ticket'),(17,40,1,1,'2004-05-04 00:00:00',1,20040504141719,'FAQ'),(18,41,1,1,'2004-05-04 00:00:00',1,20040504141826,'KnowledgeBase'),(19,42,1,1,'2004-05-04 00:00:00',1,20040504142103,'Order'),(20,56,1,1,'2004-05-04 00:00:00',1,20040504142336,'Invoice'),(21,43,1,1,'2004-05-04 00:00:00',1,20040504142510,'Payment'),(22,44,1,1,'2004-05-04 00:00:00',1,20040504142707,'Expense'),(23,45,1,1,'2004-05-04 00:00:00',1,20040504142920,'PurchaseOrder'),(24,46,1,1,'2004-05-04 00:00:00',1,20040504143047,'item'),(25,48,1,1,'2004-05-04 00:00:00',1,20040504143225,'inventory'),(26,50,1,1,'2004-05-04 00:00:00',1,20040504143256,'vendor'),(27,51,1,1,'2004-05-04 00:00:00',1,20040504143409,'ExpenseForm'),(28,52,1,1,'2004-05-04 00:00:00',1,20040504143537,'Timesheet'),(29,54,1,1,'2004-05-04 00:00:00',1,20040504143623,'Employee'),(33,47,1,1,'2004-06-07 00:00:00',1,20040607103647,'glaccount'),(34,16,1,1,'2004-06-07 00:00:00',1,20040607103852,'group');

--
-- Table structure for table `simplesearchcriteria`
--

DROP TABLE IF EXISTS `simplesearchcriteria`;
CREATE TABLE `simplesearchcriteria` (
  `SearchCriteriaID` int(11) unsigned NOT NULL auto_increment,
  `SearchID` int(11) unsigned NOT NULL default '0',
  `SearchTableID` int(11) unsigned NOT NULL default '0',
  `SearchFieldID` int(11) unsigned NOT NULL default '0',
  `ConditionID` int(11) unsigned NOT NULL default '0',
  `ExpressionType` enum('AND','OR') NOT NULL default 'AND',
  `Value` varchar(255) default NULL,
  `CriteriaGroup` int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (`SearchCriteriaID`),
  UNIQUE KEY `SearchCriteriaID` (`SearchCriteriaID`),
  KEY `SearchID` (`SearchID`),
  KEY `SearchTableID` (`SearchTableID`),
  KEY `SearchFieldID` (`SearchFieldID`),
  CONSTRAINT `0_21673` FOREIGN KEY (`SearchID`) REFERENCES `simplesearch` (`SearchID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `0_21674` FOREIGN KEY (`SearchTableID`) REFERENCES `searchtable` (`SearchTableID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `0_21675` FOREIGN KEY (`SearchFieldID`) REFERENCES `searchfield` (`SearchFieldID`) ON DELETE CASCADE ON UPDATE CASCADE
) TYPE=InnoDB;

--
-- Dumping data for table `simplesearchcriteria`
--

INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (1,1,1,3,2,'OR','',1),(2,1,2,14,2,'OR','',1),(3,1,2,15,2,'OR','',1),(4,1,1,217,2,'OR','',1),(10,2,2,13,2,'OR','',1),(11,2,2,14,2,'OR','',1),(12,2,2,15,2,'OR','',1),(15,3,8,63,2,'OR','',1),(16,3,8,66,2,'OR','',1),(17,3,8,67,2,'OR','',1),(18,3,8,68,2,'OR','',1),(19,3,8,427,2,'OR','',1),(20,4,6,38,2,'OR','',1),(21,4,6,39,2,'OR','',1),(22,4,6,40,2,'OR','',1),(23,4,6,44,2,'OR','',1),(24,4,6,48,2,'OR','',1),(25,4,6,49,2,'OR','',1),(26,4,6,42,2,'OR','',1),(27,4,6,46,2,'OR','',1),(28,5,7,53,2,'OR','',1),(29,5,7,54,2,'OR','',1),(30,5,7,58,2,'OR','',1),(31,5,7,60,2,'OR','',1),(32,5,7,61,2,'OR','',1),(33,5,7,55,2,'OR','',1),(34,6,9,70,2,'OR','',1),(35,6,9,71,2,'OR','',1),(36,6,9,77,2,'OR','',1),(37,6,9,78,2,'OR','',1),(38,6,9,74,2,'OR','',1),(39,6,9,73,2,'OR','',1),(40,7,10,86,2,'OR','',1),(41,7,10,87,2,'OR','',1),(42,7,10,97,2,'OR','',1),(43,7,10,88,2,'OR','',1),(44,7,10,90,2,'OR','',1),(45,7,10,91,2,'OR','',1),(46,7,10,100,2,'OR','',1),(47,7,10,93,2,'OR','',1),(48,7,10,94,2,'OR','',1),(49,7,10,96,2,'OR','',1),(50,7,10,95,2,'OR','',1),(51,8,11,103,2,'OR','',1),(52,8,11,104,2,'OR','',1),(53,8,11,105,2,'OR','',1),(54,8,11,107,2,'OR','',1),(55,8,11,108,2,'OR','',1),(56,8,11,106,2,'OR','',1),(57,8,11,110,2,'OR','',1),(58,8,11,115,2,'OR','',1),(59,8,11,113,2,'OR','',1),(60,8,11,229,2,'OR','',1),(61,9,20,271,2,'OR','',1),(62,9,20,272,2,'OR','',1),(63,9,20,278,2,'OR','',1),(64,9,1,3,2,'OR','',1),(65,9,2,14,2,'OR','',1),(66,9,2,15,2,'OR','',1),(67,10,21,281,2,'OR','',1),(68,10,21,282,2,'OR','',1),(69,10,21,283,2,'OR','',1),(70,10,21,284,2,'OR','',1),(71,10,21,434,2,'OR','',1),(72,10,21,435,2,'OR','',1),(73,10,27,365,2,'OR','',1),(74,10,27,366,2,'OR','',1),(75,11,22,287,2,'OR','',1),(76,11,22,290,2,'OR','',1),(77,11,22,291,2,'OR','',1),(78,11,22,293,2,'OR','',1),(79,11,22,430,2,'OR','',1),(80,11,22,429,2,'OR','',1),(81,11,22,431,2,'OR','',1),(82,11,22,432,2,'OR','',1),(83,11,6,41,2,'OR','',1),(84,12,23,298,2,'OR','',1),(85,12,23,299,2,'OR','',1),(86,12,23,301,2,'OR','',1),(87,12,23,302,2,'OR','',1),(88,12,23,437,2,'OR','',1),(89,12,23,438,2,'OR','',1),(90,12,23,439,2,'OR','',1),(91,12,23,440,2,'OR','',1),(92,12,23,441,2,'OR','',1),(93,12,1,3,2,'OR','',1),(94,13,12,118,2,'OR','',1),(95,13,12,119,2,'OR','',1),(96,13,12,120,2,'OR','',1),(97,13,12,121,2,'OR','',1),(98,13,12,122,2,'OR','',1),(99,13,12,124,2,'OR','',1),(100,13,12,125,2,'OR','',1),(101,13,12,127,2,'OR','',1),(102,13,12,126,2,'OR','',1),(103,13,12,231,2,'OR','',1),(104,13,12,232,2,'OR','',1),(105,13,12,233,2,'OR','',1),(107,13,9,72,2,'OR','',1),(120,15,15,153,2,'OR','',1),(121,15,15,154,2,'OR','',1),(122,15,15,155,2,'OR','',1),(123,15,15,156,2,'OR','',1),(124,15,15,157,2,'OR','',1),(125,15,15,158,2,'OR','',1),(126,15,15,159,2,'OR','',1),(127,15,15,243,2,'OR','',1),(128,16,14,140,2,'OR','',1),(129,16,14,141,2,'OR','',1),(130,16,14,142,2,'OR','',1),(131,16,14,143,2,'OR','',1),(132,16,14,144,2,'OR','',1),(133,16,14,147,2,'OR','',1),(134,16,14,146,2,'OR','',1),(135,16,14,148,2,'OR','',1),(136,16,14,149,2,'OR','',1),(137,16,14,151,2,'OR','',1),(138,16,14,244,2,'OR','',1),(139,16,14,245,2,'OR','',1),(140,17,16,161,2,'OR','',1),(141,17,16,162,2,'OR','',1),(142,17,16,163,2,'OR','',1),(143,17,16,164,2,'OR','',1),(144,17,16,166,2,'OR','',1),(145,18,17,170,2,'OR','',1),(146,18,17,171,2,'OR','',1),(147,18,17,172,2,'OR','',1),(148,18,17,173,2,'OR','',1),(149,18,17,174,2,'OR','',1),(150,18,17,176,2,'OR','',1),(151,19,18,181,2,'OR','',1),(152,19,18,183,2,'OR','',1),(153,19,18,184,2,'OR','',1),(154,19,18,185,2,'OR','',1),(155,19,18,188,2,'OR','',1),(156,19,18,189,2,'OR','',1),(157,19,18,192,2,'OR','',1),(158,19,18,195,2,'OR','',1),(159,19,18,196,2,'OR','',1),(160,19,18,250,2,'OR','',1),(161,19,18,255,2,'OR','',1),(162,20,19,200,2,'OR','',1),(163,20,19,201,2,'OR','',1),(164,20,19,202,2,'OR','',1),(165,20,19,203,2,'OR','',1),(166,20,19,207,2,'OR','',1),(167,20,19,208,2,'OR','',1),(168,20,19,210,2,'OR','',1),(169,20,19,209,2,'OR','',1),(170,20,19,213,2,'OR','',1),(171,20,19,263,2,'OR','',1),(172,20,19,268,2,'OR','',1),(173,20,19,269,2,'OR','',1),(174,21,24,306,2,'OR','',1),(175,21,24,307,2,'OR','',1),(176,21,24,308,2,'OR','',1),(177,21,24,309,2,'OR','',1),(178,21,24,310,2,'OR','',1),(179,21,24,311,2,'OR','',1),(180,21,24,312,2,'OR','',1),(181,21,24,315,2,'OR','',1),(182,21,24,316,2,'OR','',1),(183,22,25,322,2,'OR','',1),(184,22,25,323,2,'OR','',1),(185,22,25,324,2,'OR','',1),(186,22,25,325,2,'OR','',1),(187,22,25,326,2,'OR','',1),(188,22,25,329,2,'OR','',1),(189,22,25,330,2,'OR','',1),(190,22,25,331,2,'OR','',1),(191,22,25,332,2,'OR','',1),(192,22,25,336,2,'OR','',1),(193,23,26,341,2,'OR','',1),(194,23,26,343,2,'OR','',1),(195,23,26,344,2,'OR','',1),(196,23,26,346,2,'OR','',1),(197,23,26,357,2,'OR','',1),(198,23,26,353,2,'OR','',1),(199,23,26,358,2,'OR','',1),(200,23,26,359,2,'OR','',1),(201,23,26,360,2,'OR','',1),(202,24,27,365,2,'OR','',1),(203,24,27,366,2,'OR','',1),(204,24,27,367,2,'OR','',1),(205,24,27,368,2,'OR','',1),(206,24,27,370,2,'OR','',1),(207,24,27,369,2,'OR','',1),(208,24,27,371,2,'OR','',1),(209,24,27,374,2,'OR','',1),(210,24,27,373,2,'OR','',1),(211,24,27,381,2,'OR','',1),(212,25,29,387,2,'OR','',1),(213,25,29,388,2,'OR','',1),(214,25,29,389,2,'OR','',1),(215,25,29,391,2,'OR','',1),(216,25,29,390,2,'OR','',1),(217,25,29,392,2,'OR','',1),(218,25,29,393,2,'OR','',1),(219,25,29,396,2,'OR','',1),(220,26,1,3,2,'OR','',1),(221,27,31,401,2,'OR','',1),(222,27,31,402,2,'OR','',1),(223,27,31,403,2,'OR','',1),(224,27,31,404,2,'OR','',1),(225,27,31,405,2,'OR','',1),(226,27,31,406,2,'OR','',1),(227,27,31,409,2,'OR','',1),(228,27,31,411,2,'OR','',1),(229,28,36,444,2,'OR','',1),(230,28,36,445,2,'OR','',1),(231,28,36,446,2,'OR','',1),(232,28,36,451,2,'OR','',1),(233,28,36,452,2,'OR','',1),(234,28,36,453,2,'OR','',1),(252,33,28,382,2,'OR','',1),(253,33,28,383,2,'OR','',1),(254,33,28,384,2,'OR','',1),(255,33,28,385,2,'OR','',1),(256,33,28,463,2,'OR','',1),(257,33,28,464,2,'OR','',1),(258,34,37,455,2,'OR','',1),(259,34,37,458,2,'OR','',1),(260,34,37,459,2,'OR','',1),(261,34,37,460,2,'OR','',1),(262,34,2,14,2,'OR','',1),(263,34,2,14,2,'OR','',1),(264,29,35,465,2,'OR','',1),(265,1,1,466,2,'OR','',1),(266,1,1,467,2,'OR','',1),(267,1,1,468,2,'OR','',1),(268,1,1,469,2,'OR','',1),(269,1,1,470,2,'OR','',1),(270,1,1,471,2,'OR','',1),(271,1,1,472,2,'OR','',1),(272,1,1,473,2,'OR','',1),(273,2,2,474,2,'OR','',1),(274,2,2,475,2,'OR','',1),(275,2,2,476,2,'OR','',1),(276,2,2,477,2,'OR','',1),(277,2,2,478,2,'OR','',1),(278,2,2,479,2,'OR','',1),(279,2,2,480,2,'OR','',1),(280,2,2,481,2,'OR','',1),(281,1,1,482,2,'OR','',1),(282,1,1,483,2,'OR','',1),(283,1,1,484,2,'OR','',1),(284,1,1,485,2,'OR','',1),(285,1,1,486,2,'OR','',1),(286,1,1,487,2,'OR','',1),(287,2,2,488,2,'OR','',1),(288,2,2,489,2,'OR','',1),(289,2,2,490,2,'OR','',1),(290,2,2,491,2,'OR','',1),(291,2,2,492,2,'OR','',1),(292,2,2,493,2,'OR','',1),(293,19,18,494,2,'OR','',1),(294,19,18,495,2,'OR','',1),(295,19,18,496,2,'OR','',1),(296,19,18,497,2,'OR','',1),(297,19,18,498,2,'OR','',1),(298,19,18,499,2,'OR','',1),(299,20,19,500,2,'OR','',1),(300,20,19,501,2,'OR','',1),(301,20,19,502,2,'OR','',1),(302,20,19,503,2,'OR','',1),(303,20,19,504,2,'OR','',1),(304,20,19,505,2,'OR','',1),(305,23,26,506,2,'OR','',1),(306,23,26,507,2,'OR','',1),(307,23,26,508,2,'OR','',1),(308,23,26,509,2,'OR','',1),(309,23,26,510,2,'OR','',1),(310,23,26,511,2,'OR','',1),(311,8,11,512,2,'OR','',1),(312,8,11,513,2,'OR','',1),(313,8,11,514,2,'OR','',1),(314,8,11,515,2,'OR','',1),(315,8,11,516,2,'OR','',1),(316,8,11,517,2,'OR','',1);

--
-- Table structure for table `source`
--

DROP TABLE IF EXISTS `source`;
CREATE TABLE `source` (
  `SourceID` int(10) NOT NULL auto_increment,
  `Name` varchar(25) default NULL,
  PRIMARY KEY  (`SourceID`)
) TYPE=InnoDB;

--
-- Dumping data for table `source`
--

INSERT INTO `source` (`SourceID`, `Name`) VALUES (1,'Trade Show'),(2,'Phone Call'),(3,'Press Conference'),(4,'Website'),(5,'Cold Call'),(6,'Walk-in');

--
-- Table structure for table `state`
--

DROP TABLE IF EXISTS `state`;
CREATE TABLE `state` (
  `StateID` int(11) NOT NULL auto_increment,
  `Name` varchar(50) default NULL,
  PRIMARY KEY  (`StateID`),
  UNIQUE KEY `stateID` (`StateID`)
) TYPE=InnoDB;

--
-- Dumping data for table `state`
--

INSERT INTO `state` (`StateID`, `Name`) VALUES (1,'AL'),(2,'AK'),(3,'AZ'),(4,'AR'),(5,'CA'),(6,'CO'),(7,'CT'),(8,'DE'),(9,'DC'),(10,'FL'),(11,'GA'),(12,'HI'),(13,'ID'),(14,'IL'),(15,'IN'),(16,'IA'),(17,'KS'),(18,'KY'),(19,'LA'),(20,'ME'),(21,'MD'),(22,'MA'),(23,'MI'),(24,'MN'),(25,'MS'),(26,'MO'),(27,'MT'),(28,'NE'),(29,'NV'),(30,'NH'),(31,'NJ'),(32,'NM'),(33,'NY'),(34,'NC'),(35,'ND'),(36,'OH'),(37,'OK'),(38,'OR'),(39,'PA'),(40,'RI'),(41,'SC'),(42,'SD'),(43,'TN'),(44,'TX'),(45,'UT'),(46,'VT'),(47,'VA'),(48,'WA'),(49,'WV'),(50,'WI'),(51,'WY');

--
-- Table structure for table `supportemailaccount`
--

DROP TABLE IF EXISTS `supportemailaccount`;
CREATE TABLE `supportemailaccount` (
  `SupportEmailAccountID` int(11) unsigned NOT NULL auto_increment,
  `EmailAccountID` int(11) unsigned default NULL,
  PRIMARY KEY  (`SupportEmailAccountID`),
  UNIQUE KEY `SupportEmailAccountID` (`SupportEmailAccountID`),
  KEY `EmailAccountID` (`EmailAccountID`),
  CONSTRAINT `0_21615` FOREIGN KEY (`EmailAccountID`) REFERENCES `emailaccount` (`AccountID`) ON DELETE CASCADE ON UPDATE CASCADE
) TYPE=InnoDB;

--
-- Dumping data for table `supportemailaccount`
--


--
-- Table structure for table `supportportlet`
--

DROP TABLE IF EXISTS `supportportlet`;
CREATE TABLE `supportportlet` (
  `individualid` int(11) unsigned default NULL,
  `visible` enum('YES','NO') default NULL
) TYPE=InnoDB;

--
-- Dumping data for table `supportportlet`
--


--
-- Table structure for table `supportpriority`
--

DROP TABLE IF EXISTS `supportpriority`;
CREATE TABLE `supportpriority` (
  `priorityid` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`priorityid`)
) TYPE=InnoDB;

--
-- Dumping data for table `supportpriority`
--

INSERT INTO `supportpriority` (`priorityid`, `name`) VALUES (1,'Low'),(2,'Medium'),(3,'High');

--
-- Table structure for table `supportstatus`
--

DROP TABLE IF EXISTS `supportstatus`;
CREATE TABLE `supportstatus` (
  `statusid` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`statusid`)
) TYPE=InnoDB;

--
-- Dumping data for table `supportstatus`
--

INSERT INTO `supportstatus` (`statusid`, `name`) VALUES (1,'Open'),(2,'Closed'),(3,'Resolved');

--
-- Table structure for table `syncconfig`
--

DROP TABLE IF EXISTS `syncconfig`;
CREATE TABLE `syncconfig` (
  `lastsynceddate` datetime NOT NULL default '0000-00-00 00:00:00',
  `syncedid` int(11) NOT NULL default '0',
  KEY `syncedid` (`syncedid`)
) TYPE=InnoDB;

--
-- Dumping data for table `syncconfig`
--


--
-- Table structure for table `syncmaster`
--

DROP TABLE IF EXISTS `syncmaster`;
CREATE TABLE `syncmaster` (
  `syncas` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`syncas`)
) TYPE=InnoDB;

--
-- Dumping data for table `syncmaster`
--

INSERT INTO `syncmaster` (`syncas`) VALUES ('Fax'),('Home'),('Main'),('Mobile'),('Other'),('Pager'),('Work');

--
-- Table structure for table `syncnote`
--

DROP TABLE IF EXISTS `syncnote`;
CREATE TABLE `syncnote` (
  `recordID` int(11) unsigned NOT NULL default '0',
  `recordTypeID` int(11) unsigned NOT NULL default '0',
  `content` text,
  UNIQUE KEY `noteID` (`recordID`,`recordTypeID`),
  KEY `recordID` (`recordID`)
) TYPE=InnoDB;

--
-- Dumping data for table `syncnote`
--


--
-- Table structure for table `syncproperties`
--

DROP TABLE IF EXISTS `syncproperties`;
CREATE TABLE `syncproperties` (
  `syncID` int(11) unsigned NOT NULL auto_increment,
  `recordType` int(11) unsigned NOT NULL default '0',
  `recordID` int(11) unsigned NOT NULL default '0',
  `syncFlag` int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (`syncID`),
  KEY `recordType` (`recordType`),
  KEY `recordID` (`recordID`)
) TYPE=InnoDB;

--
-- Dumping data for table `syncproperties`
--


--
-- Table structure for table `systememailsettings`
--

DROP TABLE IF EXISTS `systememailsettings`;
CREATE TABLE `systememailsettings` (
  `systemEmailID` int(11) NOT NULL auto_increment,
  `toAddress` varchar(255) NOT NULL default '',
  `fromAddress` varchar(255) NOT NULL default '',
  `subject` varchar(255) NOT NULL default '',
  `smtpServer` varchar(255) NOT NULL default '',
  `description` varchar(255) default NULL,
  PRIMARY KEY  (`systemEmailID`)
) TYPE=InnoDB;

--
-- Dumping data for table `systememailsettings`
--

INSERT INTO `systememailsettings` (`systemEmailID`, `toAddress`, `fromAddress`, `subject`, `smtpServer`, `description`) VALUES (1,'','','CentraView Web Request: Customer Profile Update','','Email which is sent to a CentraView employee user when a Customer requests that their Customer Profile data gets changed.'),(2,'','','CentraView Web Request: Forgot Password','','Email which is sent to a CentraView Customer user when the Customer make a \'Forgot Password\' request.');

--
-- Table structure for table `systemsettings`
--

DROP TABLE IF EXISTS `systemsettings`;
CREATE TABLE `systemsettings` (
  `settingID` int(11) unsigned NOT NULL auto_increment,
  `settingName` varchar(255) NOT NULL default '',
  `settingValue` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`settingID`)
) TYPE=InnoDB;

--
-- Dumping data for table `systemsettings`
--

INSERT INTO `systemsettings` (`settingID`, `settingName`, `settingValue`) VALUES (1,'supportEmailCheckInterval','10');

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
  `ActivityID` int(11) unsigned NOT NULL default '0',
  `ProjectID` int(11) unsigned default NULL,
  `Parent` int(11) unsigned default NULL,
  `Milestone` enum('YES','NO') default 'NO',
  `PercentComplete` int(2) unsigned NOT NULL default '0',
  `ProjectTaskCount` int(11) default NULL,
  PRIMARY KEY  (`ActivityID`),
  KEY `ActivityID` (`ActivityID`),
  KEY `ProjectID` (`ProjectID`),
  KEY `Parent` (`Parent`)
) TYPE=InnoDB;

--
-- Dumping data for table `task`
--


--
-- Table structure for table `taskassigned`
--

DROP TABLE IF EXISTS `taskassigned`;
CREATE TABLE `taskassigned` (
  `TaskID` int(11) unsigned default NULL,
  `AssignedTo` int(11) unsigned default NULL,
  KEY `TaskID` (`TaskID`),
  KEY `AssignedTo` (`AssignedTo`)
) TYPE=InnoDB;

--
-- Dumping data for table `taskassigned`
--


--
-- Table structure for table `taskportlet`
--

DROP TABLE IF EXISTS `taskportlet`;
CREATE TABLE `taskportlet` (
  `individualid` int(11) unsigned default NULL,
  `visibility` enum('YES','NO') default NULL,
  KEY `individualid` (`individualid`)
) TYPE=InnoDB;

--
-- Dumping data for table `taskportlet`
--


--
-- Table structure for table `taxclass`
--

DROP TABLE IF EXISTS `taxclass`;
CREATE TABLE `taxclass` (
  `taxclassid` int(10) unsigned NOT NULL auto_increment,
  `title` varchar(25) NOT NULL default '',
  `description` text NOT NULL,
  PRIMARY KEY  (`taxclassid`)
) TYPE=InnoDB;

--
-- Dumping data for table `taxclass`
--

INSERT INTO `taxclass` (`taxclassid`, `title`, `description`) VALUES (1,'Goods','Goods Tax Class'),(2,'Services','Services');

--
-- Table structure for table `taxjurisdiction`
--

DROP TABLE IF EXISTS `taxjurisdiction`;
CREATE TABLE `taxjurisdiction` (
  `taxjurisdictionid` int(10) unsigned NOT NULL auto_increment,
  `taxjurisdictionname` varchar(25) NOT NULL default '',
  `taxjurisdictioncode` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`taxjurisdictionid`)
) TYPE=InnoDB;

--
-- Dumping data for table `taxjurisdiction`
--

INSERT INTO `taxjurisdiction` (`taxjurisdictionid`, `taxjurisdictionname`, `taxjurisdictioncode`) VALUES (1,'Philadelphia','');

--
-- Table structure for table `taxmatrix`
--

DROP TABLE IF EXISTS `taxmatrix`;
CREATE TABLE `taxmatrix` (
  `taxclassid` int(10) unsigned NOT NULL default '0',
  `taxjurisdictionid` int(10) unsigned NOT NULL default '0',
  `taxrate` float NOT NULL default '0',
  KEY `taxclassid` (`taxclassid`),
  KEY `taxjurisdictionid` (`taxjurisdictionid`)
) TYPE=InnoDB;

--
-- Dumping data for table `taxmatrix`
--

INSERT INTO `taxmatrix` (`taxclassid`, `taxjurisdictionid`, `taxrate`) VALUES (1,1,0.06),(2,1,0);

--
-- Table structure for table `terms`
--

DROP TABLE IF EXISTS `terms`;
CREATE TABLE `terms` (
  `TermID` int(11) NOT NULL auto_increment,
  `TermName` varchar(20) NOT NULL default '',
  PRIMARY KEY  (`TermID`)
) TYPE=InnoDB;

--
-- Dumping data for table `terms`
--

INSERT INTO `terms` (`TermID`, `TermName`) VALUES (1,'Dynamic Type 1'),(2,'Dynamic Type 2'),(3,'Dynamic Type 3');

--
-- Table structure for table `thread`
--

DROP TABLE IF EXISTS `thread`;
CREATE TABLE `thread` (
  `threadid` int(11) unsigned NOT NULL auto_increment,
  `ticketid` int(10) unsigned NOT NULL default '0',
  `title` varchar(255) default NULL,
  `detail` text,
  `created` datetime NOT NULL default '0000-00-00 00:00:00',
  `creator` int(10) unsigned NOT NULL default '0',
  `priority` int(11) unsigned NOT NULL default '0',
  `type` enum('INTERNAL','EXTERNAL','BOTH') NOT NULL default 'INTERNAL',
  PRIMARY KEY  (`threadid`),
  KEY `ticketid` (`ticketid`),
  KEY `creator` (`creator`),
  KEY `priority` (`priority`)
) TYPE=InnoDB;

--
-- Dumping data for table `thread`
--


--
-- Table structure for table `ticket`
--

DROP TABLE IF EXISTS `ticket`;
CREATE TABLE `ticket` (
  `ticketid` int(10) unsigned NOT NULL auto_increment,
  `subject` varchar(100) default NULL,
  `description` text,
  `entityid` int(10) unsigned NOT NULL default '0',
  `individualid` int(10) unsigned NOT NULL default '0',
  `assignedto` int(10) unsigned NOT NULL default '0',
  `status` int(10) unsigned NOT NULL default '0',
  `priority` int(10) unsigned NOT NULL default '0',
  `owner` int(10) unsigned NOT NULL default '0',
  `created` datetime NOT NULL default '0000-00-00 00:00:00',
  `createdby` int(10) unsigned NOT NULL default '0',
  `modifiedby` int(10) unsigned NOT NULL default '0',
  `manager` int(11) unsigned NOT NULL default '0',
  `modified` timestamp(14) NOT NULL,
  `ocstatus` enum('OPEN','CLOSE') NOT NULL default 'OPEN',
  `dateclosed` datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (`ticketid`),
  KEY `entityid` (`entityid`),
  KEY `individualid` (`individualid`),
  KEY `assignedto` (`assignedto`),
  KEY `status` (`status`),
  KEY `priority` (`priority`),
  KEY `owner` (`owner`),
  KEY `createdby` (`createdby`),
  KEY `modifiedby` (`modifiedby`),
  KEY `manager` (`manager`)
) TYPE=InnoDB;

--
-- Dumping data for table `ticket`
--


--
-- Table structure for table `ticketlink`
--

DROP TABLE IF EXISTS `ticketlink`;
CREATE TABLE `ticketlink` (
  `ticketid` int(10) unsigned NOT NULL default '0',
  `recordtypeid` int(10) unsigned NOT NULL default '0',
  `recordid` int(10) unsigned NOT NULL default '0',
  KEY `ticketid` (`ticketid`),
  KEY `recordtypeid` (`recordtypeid`),
  KEY `recordid` (`recordid`)
) TYPE=InnoDB;

--
-- Dumping data for table `ticketlink`
--


--
-- Table structure for table `timeentry`
--

DROP TABLE IF EXISTS `timeentry`;
CREATE TABLE `timeentry` (
  `TimeEntryID` int(11) NOT NULL default '0',
  `TimeSlipID` int(11) NOT NULL default '0',
  `TicketID` int(11) NOT NULL default '0',
  `ProjectID` int(11) NOT NULL default '0',
  `Title` varchar(50) NOT NULL default '',
  `Description` text NOT NULL,
  `owner` int(11) NOT NULL default '0',
  `Creator` int(11) NOT NULL default '0',
  `Created` datetime NOT NULL default '0000-00-00 00:00:00',
  `Modified` timestamp(14) NOT NULL,
  `Start` datetime NOT NULL default '0000-00-00 00:00:00',
  `End` datetime NOT NULL default '0000-00-00 00:00:00',
  `Status` int(11) NOT NULL default '0',
  PRIMARY KEY  (`TimeEntryID`),
  KEY `TimeSlipID` (`TimeSlipID`),
  KEY `TicketID` (`TicketID`),
  KEY `ProjectID` (`ProjectID`),
  KEY `owner` (`owner`),
  KEY `Creator` (`Creator`),
  KEY `Status` (`Status`)
) TYPE=InnoDB;

--
-- Dumping data for table `timeentry`
--


--
-- Table structure for table `timesheet`
--

DROP TABLE IF EXISTS `timesheet`;
CREATE TABLE `timesheet` (
  `TimeSheetID` int(10) unsigned NOT NULL auto_increment,
  `Description` text,
  `Owner` int(10) unsigned NOT NULL default '0',
  `Creator` int(10) unsigned default '0',
  `ModifiedBy` int(10) unsigned default '0',
  `Created` datetime NOT NULL default '0000-00-00 00:00:00',
  `Modified` timestamp(14) NOT NULL,
  `Start` date NOT NULL default '0000-00-00',
  `End` date NOT NULL default '0000-00-00',
  `Status` int(10) unsigned default '0',
  `Notes` text,
  `ReportingTo` int(10) unsigned default NULL,
  PRIMARY KEY  (`TimeSheetID`),
  KEY `Owner` (`Owner`),
  KEY `Creator` (`Creator`),
  KEY `ModifiedBy` (`ModifiedBy`),
  KEY `Status` (`Status`),
  KEY `ReportingTo` (`ReportingTo`)
) TYPE=InnoDB;

--
-- Dumping data for table `timesheet`
--


--
-- Table structure for table `timeslip`
--

DROP TABLE IF EXISTS `timeslip`;
CREATE TABLE `timeslip` (
  `TimeSlipID` int(11) unsigned NOT NULL auto_increment,
  `TimeEntryID` int(11) NOT NULL default '0',
  `ProjectID` int(11) unsigned default NULL,
  `ActivityID` int(11) NOT NULL default '0',
  `TicketID` int(11) unsigned default NULL,
  `Title` varchar(25) default NULL,
  `Description` text,
  `Hours` float unsigned default NULL,
  `CreatedBy` int(11) NOT NULL default '0',
  `Date` date NOT NULL default '0000-00-00',
  `Start` time default NULL,
  `End` time default NULL,
  `BreakTime` float NOT NULL default '0',
  `TimeSheetID` int(11) NOT NULL default '0',
  PRIMARY KEY  (`TimeSlipID`),
  KEY `ProjectID` (`ProjectID`),
  KEY `TaskID` (`ActivityID`),
  KEY `TimeEntryID` (`TimeEntryID`),
  KEY `ActivityID` (`ActivityID`),
  KEY `TicketID` (`TicketID`),
  KEY `CreatedBy` (`CreatedBy`),
  KEY `TimeSheetID` (`TimeSheetID`)
) TYPE=InnoDB;

--
-- Dumping data for table `timeslip`
--


--
-- Table structure for table `uidlist`
--

DROP TABLE IF EXISTS `uidlist`;
CREATE TABLE `uidlist` (
  `accountID` int(11) unsigned NOT NULL default '0',
  `uid` varchar(100) NOT NULL default '',
  UNIQUE KEY `messageUID` (`accountID`,`uid`),
  KEY `accountID` (`accountID`)
) TYPE=InnoDB;

--
-- Dumping data for table `uidlist`
--


--
-- Table structure for table `uifieldmapping`
--

DROP TABLE IF EXISTS `uifieldmapping`;
CREATE TABLE `uifieldmapping` (
  `ID` int(11) NOT NULL auto_increment,
  `uifieldname` varchar(50) NOT NULL default '',
  `moduleid` int(11) NOT NULL default '0',
  `fieldid` int(11) NOT NULL default '0',
  PRIMARY KEY  (`ID`)
) TYPE=InnoDB;

--
-- Dumping data for table `uifieldmapping`
--

INSERT INTO `uifieldmapping` (`ID`, `uifieldname`, `moduleid`, `fieldid`) VALUES (1,'Entity Name',14,3),(2,'Street1',14,29),(3,'Street2',14,30),(4,'city',14,31),(5,'Title',14,17),(6,'First Name',14,14),(7,'Last Name',14,16),(8,'Acct Mgr',14,649),(9,'Acct Team',14,62),(10,'MI',14,15),(13,'First Name',15,14),(14,'Last Name',15,16),(15,'Entity',15,3),(16,'Title',15,17),(17,'Street',15,29),(18,'City',15,31),(19,'State',15,32),(20,'Zip',15,33),(21,'Country',15,34);

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `UserID` int(4) unsigned NOT NULL auto_increment,
  `Name` varchar(25) default NULL,
  `IndividualID` int(11) unsigned default NULL,
  `Password` varchar(50) default NULL,
  `userstatus` enum('DISABLED','ENABLED') NOT NULL default 'DISABLED',
  `usertype` enum('EMPLOYEE','CUSTOMER','ADMINISTRATOR') NOT NULL default 'EMPLOYEE',
  PRIMARY KEY  (`UserID`),
  KEY `IndividualID` (`IndividualID`),
  CONSTRAINT `0_21584` FOREIGN KEY (`IndividualID`) REFERENCES `individual` (`IndividualID`)
) TYPE=InnoDB;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`UserID`, `Name`, `IndividualID`, `Password`, `userstatus`, `usertype`) VALUES (1,'admin',1,'0DPiKuNIrrVmD8IUCuw1hQxNqZc=','ENABLED','ADMINISTRATOR');

--
-- Table structure for table `userpreference`
--

DROP TABLE IF EXISTS `userpreference`;
CREATE TABLE `userpreference` (
  `preferenceid` int(11) NOT NULL auto_increment,
  `individualid` int(11) default '0',
  `moduleid` int(11) NOT NULL default '1',
  `preference_name` varchar(250) NOT NULL default 'HOMEREFRESHMINUTE',
  `preference_value` varchar(250) NOT NULL default '10',
  `tag` varchar(100) NOT NULL default '',
  PRIMARY KEY  (`preferenceid`),
  KEY `individualid` (`individualid`),
  KEY `moduleid` (`moduleid`)
) TYPE=InnoDB;

--
-- Dumping data for table `userpreference`
--

INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (1,1,4,'calendarrefreshmin','5',''),(2,1,4,'calendarrefreshsec','0',''),(3,1,4,'caldefaultview','DAILY',''),(4,1,0,'TIMEZONE','EST',''),(5,1,6,'DEFAULTFOLDERID','8','0'),(6,1,2,'acknowledgeddays','23',''),(7,1,1,'emailaccountid','1',''),(8,1,4,'DATEFORMAT','MMM d, yyyy h:mm a',''),(9,1,4,'email','YES',''),(10,1,4,'todayscalendar','YES',''),(11,1,4,'unscheduledactivities','YES',''),(12,1,4,'scheduledopportunities','YES',''),(13,1,4,'projecttasks','YES',''),(14,1,4,'supporttickets','YES',''),(15,1,4,'companynews','YES',''),(16,1,4,'homesettingrefreshmin','5',''),(17,1,4,'homesettingrefreshsec','0',''),(18,1,4,'contenttype','HTML',''),(19,1,1,'allRecordsPublic','Yes',''),(20,1,61,'syncAsPrivate','YES',''),(21,1,2,'emailCheckInterval','10',''),(22,1,1,'showticketcharts','Yes',''),(23,1,1,'showsalescharts','Yes','');

--
-- Table structure for table `userpreferencedefault`
--

DROP TABLE IF EXISTS `userpreferencedefault`;
CREATE TABLE `userpreferencedefault` (
  `preferenceid` int(11) NOT NULL auto_increment,
  `moduleid` int(11) NOT NULL default '1',
  `preference_name` varchar(100) NOT NULL default 'HOMEREFRESHMINUTE',
  `preference_value` varchar(100) NOT NULL default '10',
  `tag` varchar(100) NOT NULL default '',
  PRIMARY KEY  (`preferenceid`),
  KEY `moduleid` (`moduleid`)
) TYPE=InnoDB;

--
-- Dumping data for table `userpreferencedefault`
--

INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (1,2,'TIMEZONE','EST',''),(2,3,'TIMESTAMP','',''),(3,4,'DATEFORMAT','MMM d, yyyy h:mm a',''),(4,5,'DEFAULTFOLDERID','',''),(5,6,'caldefaultview','DAILY',''),(6,1,'calendarrefreshmin','10',''),(7,1,'calendarrefreshsec','0',''),(8,1,'homesettingrefreshmin','5',''),(9,1,'homesettingrefreshsec','0',''),(10,1,'unscheduledactivities','YES',''),(11,1,'todayscalendar','YES',''),(12,1,'email','YES',''),(13,1,'scheduledopportunities','YES',''),(14,1,'projecttasks','YES',''),(15,1,'supporttickets','YES',''),(16,1,'companynews','YES',''),(17,1,'contenttype','HTML',''),(18,1,'emailaccountid','',''),(19,1,'syncAsPrivate','YES',''),(20,1,'allRecordsPublic','Yes',''),(21,2,'emailCheckInterval','10','');

--
-- Table structure for table `usersecurityprofile`
--

DROP TABLE IF EXISTS `usersecurityprofile`;
CREATE TABLE `usersecurityprofile` (
  `profileid` int(11) unsigned NOT NULL default '0',
  `individualid` int(11) unsigned NOT NULL default '0',
  KEY `profileid` (`profileid`),
  KEY `individualid` (`individualid`)
) TYPE=InnoDB;

--
-- Dumping data for table `usersecurityprofile`
--

INSERT INTO `usersecurityprofile` (`profileid`, `individualid`) VALUES (1,1);

--
-- Table structure for table `valuelist`
--

DROP TABLE IF EXISTS `valuelist`;
CREATE TABLE `valuelist` (
  `valueListId` int(11) unsigned NOT NULL auto_increment,
  `valueListName` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`valueListId`)
) TYPE=InnoDB;

--
-- Dumping data for table `valuelist`
--

INSERT INTO `valuelist` (`valueListId`, `valueListName`) VALUES (1,'Entity'),(2,'Individual');

--
-- Table structure for table `valuelistfield`
--

DROP TABLE IF EXISTS `valuelistfield`;
CREATE TABLE `valuelistfield` (
  `valueListFieldId` int(11) unsigned NOT NULL auto_increment,
  `valueListId` int(11) unsigned NOT NULL default '0',
  `fieldName` varchar(255) NOT NULL default '',
  `queryIndex` int(11) unsigned NOT NULL default '0',
  `customField` enum('N','Y') NOT NULL default 'N',
  `externalKey` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`valueListFieldId`),
  KEY `valueListId` (`valueListId`),
  CONSTRAINT `0_21727` FOREIGN KEY (`valueListId`) REFERENCES `valuelist` (`valueListId`) ON DELETE CASCADE ON UPDATE CASCADE
) TYPE=InnoDB;

--
-- Dumping data for table `valuelistfield`
--

INSERT INTO `valuelistfield` (`valueListFieldId`, `valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`) VALUES (1,1,'Entity ID',1,'N','entity.entityId'),(2,1,'Entity Name',2,'N','entity.entityName'),(3,1,'Marketing List',3,'N','entity.marketingList'),(4,1,'Account Manager ID',4,'N','entity.accountManagerId'),(5,1,'Account Manager',5,'N','entity.accountManagerName'),(6,1,'Primary Contact ID',6,'N','entity.primaryContactId'),(7,1,'Primary Contact',7,'N','entity.primaryContact'),(8,1,'Phone',8,'N','entity.phone'),(9,1,'Email',9,'N','entity.email'),(10,1,'Website',10,'N','entity.website'),(11,1,'Street 1',11,'N','entity.street1'),(12,1,'Street 2',12,'N','entity.street2'),(13,1,'City',13,'N','entity.city'),(14,1,'State',14,'N','entity.state'),(15,1,'Zipcode',15,'N','entity.zipcode'),(16,1,'Country',16,'N','entity.country'),(17,1,'Address',17,'N','entity.address');

--
-- Table structure for table `vendor`
--

DROP TABLE IF EXISTS `vendor`;
CREATE TABLE `vendor` (
  `entityid` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`entityid`)
) TYPE=InnoDB;

--
-- Dumping data for table `vendor`
--


--
-- Table structure for table `version`
--

DROP TABLE IF EXISTS `version`;
CREATE TABLE `version` (
  `dbVersion` int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (`dbVersion`)
) TYPE=InnoDB;

--
-- Dumping data for table `version`
--

INSERT INTO `version` (`dbVersion`) VALUES (100);

--
-- Table structure for table `viewcolumns`
--

DROP TABLE IF EXISTS `viewcolumns`;
CREATE TABLE `viewcolumns` (
  `viewid` int(11) unsigned default NULL,
  `columnname` varchar(100) default NULL,
  `columnorder` int(3) unsigned default NULL,
  KEY `viewid` (`viewid`)
) TYPE=InnoDB;

--
-- Dumping data for table `viewcolumns`
--

INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (5,'Name',1),(6,'Type',1),(7,'Street1',1),(9,'Name',1),(11,'Field',1),(12,'Subject',1),(13,'Name',1),(14,'Title',1),(16,'Title',2),(17,'ID',1),(19,'Type',1),(26,'Fax',3),(26,'Phone',2),(26,'Name',1),(29,'Title',1),(29,'Start',3),(29,'End',4),(29,'Duration',5),(29,'Priority',6),(29,'Createdby',7),(13,'Description',2),(13,'Created',3),(13,'Updated',4),(26,'Email',4),(31,'Name',1),(31,'Description',2),(33,'Name',1),(33,'Description',2),(33,'Created',3),(13,'CreatedBy',5),(23,'Name',1),(23,'Description',2),(23,'Enabled',3),(7,'Street2',2),(7,'City',3),(7,'State',4),(7,'ZipCode',5),(7,'Country',6),(6,'Title',2),(6,'Created',3),(6,'Priority',5),(6,'CreatedBy',6),(6,'Status',7),(9,'Title',2),(9,'Entity',3),(9,'Phone',4),(9,'Fax',5),(9,'Email',6),(11,'Value',2),(12,'From',2),(12,'Received',3),(12,'Size',4),(5,'Title',2),(5,'Email',3),(5,'Phone',4),(19,'Content',2),(19,'Notes',3),(17,'Entity',2),(17,'WhoRequested',3),(17,'DateRequested',4),(17,'LiteratureRequested',5),(17,'DeliveryMethod',6),(17,'Forecast Amnt.',7),(17,'Sales Rep.',8),(16,'Name',1),(16,'Email',3),(16,'Phone',4),(36,'Title',2),(37,'Name',1),(38,'Name',1),(39,'Type',1),(40,'Street1',1),(41,'Title',1),(42,'Name',1),(43,'Title',1),(44,'Field',1),(45,'Subject',1),(46,'Name',1),(47,'Title',1),(49,'ID',1),(50,'Title',1),(53,'Title',1),(53,'Created',2),(58,'Fax',3),(58,'Phone',2),(58,'Name',1),(59,'Title',1),(59,'Start',3),(59,'End',4),(59,'Duration',5),(59,'Priority',6),(59,'Createdby',7),(46,'Description',2),(46,'Created',3),(46,'Updated',4),(58,'Email',4),(35,'Name',1),(35,'Address',2),(35,'PrimaryContact',3),(62,'Name',1),(62,'Description',2),(62,'NOOfMembers',3),(61,'Name',1),(61,'Description',2),(34,'Name',1),(34,'PrimaryContact',2),(34,'Phone',3),(34,'Email',4),(34,'Address',5),(63,'Name',1),(63,'Description',2),(63,'Created',3),(46,'CreatedBy',5),(52,'Type',2),(52,'Created',3),(52,'Priority',5),(52,'CreatedBy',6),(52,'Status',7),(55,'RuleName',1),(55,'Description',2),(55,'EnabledStatus',3),(57,'Title',1),(57,'Created',2),(57,'Priority',4),(57,'CreatedBy',5),(57,'Status',6),(40,'Street2',2),(40,'City',3),(40,'State',4),(40,'ZipCode',5),(40,'Country',6),(39,'Title',2),(39,'Created',3),(39,'Priority',5),(39,'CreatedBy',6),(39,'Status',7),(41,'Start',3),(41,'End',4),(41,'Duration',5),(41,'Priority',6),(41,'Createdby',7),(41,'Status',8),(42,'Title',2),(42,'Entity',3),(42,'Phone',4),(42,'Fax',5),(42,'Email',6),(43,'CallType',2),(43,'Duration',3),(43,'CreatedBy',4),(43,'Priority',6),(43,'Created',7),(43,'Status',8),(44,'Value',2),(45,'From',2),(45,'To',3),(45,'Received',3),(45,'Size',4),(53,'Title',1),(53,'Priority',4),(53,'CreatedBy',5),(53,'Status',6),(37,'Description',2),(37,'NOOfMembers',3),(38,'Title',2),(38,'Email',3),(38,'Phone',4),(51,'Content',2),(36,'Name',1),(51,'Notes',3),(36,'Entity',3),(50,'Start',3),(36,'Phone',4),(50,'End',4),(36,'Fax',5),(50,'Duration',5),(50,'Priority',6),(36,'Email',6),(50,'CreatedBy',7),(50,'Status',8),(49,'Entity',2),(49,'WhoRequested',3),(49,'DateRequested',4),(49,'LitratureRequested',5),(49,'DiliveryMethod',6),(54,'Title',1),(54,'Date',2),(54,'CreatedBy',3),(48,'Title',2),(48,'Name',1),(48,'Email',3),(48,'Phone',4),(52,'Title',1),(3,'Name',1),(3,'Entity',2),(3,'Phone',3),(3,'Fax',4),(3,'Email',5),(4,'Name',1),(4,'Description',2),(4,'NOOfMembers',3),(32,'Name',1),(32,'Description',2),(1,'Name',1),(1,'PrimaryContact',2),(1,'Phone',3),(1,'Email',4),(1,'Street1',5),(1,'City',6),(1,'State',7),(51,'Type',1),(60,'Name',1),(60,'Title',2),(60,'Entity',3),(60,'Phone',4),(60,'Fax',5),(66,'Subject',2),(66,'Entity',3),(66,'DateOpened',4),(66,'Status',5),(66,'DateClosed',6),(66,'AssignedTo',7),(67,'Title',1),(67,'Created',2),(67,'Updated',3),(68,'Name',1),(68,'DateCreated',2),(68,'DateUpdated',3),(69,'Subject',1),(69,'Entity',2),(69,'DateOpened',3),(69,'Status',4),(69,'DateClosed',5),(69,'AssignedTo',6),(70,'Title',1),(70,'Created',2),(70,'Updated',3),(71,'Name',1),(71,'DateCreated',2),(71,'DateUpdated',3),(33,'Updated',4),(33,'CreatedBy',5),(33,'FolderName',6),(63,'Updated',4),(63,'CreatedBy',5),(63,'FolderName',6),(78,'individualname',1),(78,'email',2),(78,'accepted',3),(79,'InventoryID',1),(79,'ItemName',2),(79,'Identifier',3),(79,'Manufacturer',4),(79,'Vendor',5),(81,'OrderNO',1),(81,'Date',3),(81,'Total',4),(81,'Status',5),(81,'SalesRep',6),(82,'PaymentID',1),(82,'PaymentMethod',7),(82,'Entity',2),(82,'PaymentDate',6),(82,'Reference',8),(82,'CreatedBy',9),(83,'ExpenseID',1),(83,'Amount',3),(83,'Created',3),(83,'EntityName',4),(83,'Status',5),(83,'Creator',6),(84,'PurchaseOrderID',1),(84,'Created',2),(84,'Creator',3),(84,'Entity',4),(84,'SubTotal',5),(84,'Tax',6),(84,'Total',7),(84,'Status',8),(74,'Name',1),(74,'Balance',3),(74,'Type',2),(74,'ParentAccount',4),(85,'DiscountID',1),(85,'Description',2),(85,'From',3),(85,'To',4),(85,'Discount',5),(85,'Status',6),(86,'Name',1),(86,'PrimaryContact',2),(86,'Phone',3),(86,'Email',5),(86,'Website',6),(86,'Address',7),(87,'InvoiceID',1),(87,'OrderID',2),(87,'Customer',3),(87,'InvoiceDate',4),(87,'Total',5),(87,'Paid',6),(87,'Creator',7),(90,'Title',1),(90,'Project',2),(90,'Milestone',3),(90,'Manager',4),(90,'StartDate',5),(90,'DueDate',6),(90,'Complete',7),(94,'Title',1),(94,'Description',2),(94,'Type',3),(94,'Stage',4),(94,'Probability',5),(94,'Status',6),(94,'EstimatedCloseDate',7),(94,'ForecastAmmount',8),(88,'Task',1),(88,'Milestone',2),(88,'Owner',3),(88,'StartDate',4),(88,'DueDate',5),(88,'Complete',6),(89,'Task',1),(89,'Description',2),(89,'Duration',3),(89,'CreatedBy',4),(89,'Date',5),(89,'StartTime',6),(89,'EndTime',7),(95,'Task',1),(95,'Milestone',2),(95,'Owner',3),(95,'StartDate',4),(95,'DueDate',5),(95,'Complete',6),(96,'Task',1),(96,'Description',2),(96,'Duration',3),(96,'CreatedBy',4),(96,'Date',5),(96,'StartTime',6),(96,'EndTime',7),(97,'Title',1),(97,'Project',2),(97,'Milestone',3),(97,'Owner',4),(97,'StartDate',5),(97,'DueDate',6),(97,'Complete',7),(99,'ID',1),(99,'Description',2),(99,'Project',3),(99,'Task',4),(99,'Duration',5),(99,'CreatedBy',6),(99,'Date',7),(99,'StartTime',8),(99,'EndTime',9),(94,'SalePersonName',9),(100,'Title',1),(100,'CreatedBy',2),(100,'Created',3),(100,'DueDate',4),(100,'Priority',5),(100,'Status',6),(101,'Type',1),(101,'Title',2),(101,'Created',3),(101,'Priority',5),(101,'CreatedBy',6),(101,'Status',7),(104,'Name',1),(104,'Title',2),(104,'Entity',3),(104,'Phone',4),(104,'Fax',5),(104,'Email',6),(105,'Name',1),(105,'Description',2),(105,'Created',3),(105,'Updated',4),(105,'CreatedBy',5),(106,'Title',1),(106,'Date',2),(106,'CreatedBy',3),(93,'Title',1),(93,'Description',2),(93,'Entity',3),(93,'Type',4),(93,'Stage',5),(93,'Probability',6),(93,'Status',7),(93,'EstimatedCloseDate',8),(93,'ForecastAmmount',9),(93,'ActualAmount',10),(93,'SalePersonName',11),(19,'SyncAs',4),(107,'Subject',1),(107,'DateOpened',2),(107,'DateClosed',3),(107,'Status',4),(107,'AssignedTo',5),(108,'Subject',1),(108,'DateOpened',2),(108,'DateClosed',3),(108,'Status',4),(108,'AssignedTo',5),(82,'AppliedAmount',4),(82,'UnAppliedAmount',5),(82,'AmountPaid',3),(110,'Name',1),(110,'Type',2),(110,'Module',3),(110,'Record',4),(114,'EntityName',1),(114,'Strengths',2),(114,'Weaknesses',3),(114,'Notes',4),(111,'Record',1),(111,'Name',2),(111,'Type',3),(111,'Module',4),(115,'Type',1),(115,'Title',2),(115,'Created',3),(115,'Priority',5),(160,'OpportunityID',1),(160,'Title',2),(160,'Description',3),(160,'EntityID',4),(160,'Entity',5),(160,'Name',6),(160,'PrimaryContact',7),(160,'Phone',8),(160,'Email',9),(160,'Address',10),(160,'Street1',11),(160,'Street2',12),(164,'Name',1),(164,'Entity',2),(91,'Name',1),(91,'Entity',2),(91,'Status',3),(91,'DueDate',4),(166,'ListID',1),(166,'title',2),(166,'description',3),(167,'Name',1),(167,'Status',2),(163,'Name',1),(163,'Entity',2),(163,'Status',3),(2,'Name',1),(2,'Address',2),(2,'PrimaryContact',3),(151,'Date',1),(151,'Title',2),(169,'Title',1),(169,'Created',2),(116,'Name',1),(116,'PrimaryContact',2),(116,'Phone',3),(116,'Email',4),(170,'Name',1),(170,'Title',2),(170,'Entity',3),(170,'Phone',4),(170,'Fax',5),(170,'Email',6),(171,'ID',1),(171,'Employee',2),(171,'StartDate',3),(171,'EndDate',4),(171,'Duration',5),(171,'CreatedBy',6),(172,'ID',1),(172,'Employee',2),(172,'StartDate',3),(172,'EndDate',4),(172,'Amount',5),(172,'Status',6),(172,'CreatedBy',7),(173,'Name',1),(173,'Description',2),(173,'Created',3),(173,'Updated',4),(173,'CreatedBy',5),(183,'individualname',1),(183,'email',2),(183,'accepted',3),(175,'SearchName',1),(175,'Module',2),(175,'Record',3),(176,'Name',1),(176,'Phone',2),(176,'Fax',3),(177,'ViewName',1),(177,'Module',2),(177,'Record',3),(178,'OrderNO',1),(178,'Entity',2),(178,'Total',3),(179,'ProfileName',1),(179,'NoOfUsers',2),(180,'Title',1),(180,'Module',2),(180,'Record',3),(180,'Owner',4),(180,'Deleted',5),(181,'Title',1),(181,'Module',2),(181,'Record',3),(181,'Owner',4),(181,'Deleted',5),(182,'Date',1),(182,'User',2),(182,'Action',3),(182,'Type',4),(184,'Entity',1),(184,'Individual',2),(184,'Email',3),(184,'PhoneNumber',4),(185,'ReportID',1),(185,'Name',2),(185,'Description',3),(186,'ReportID',1),(186,'Name',2),(186,'Description',3),(186,'View',4),(187,'ReportID',1),(187,'Name',2),(187,'Description',3),(187,'View',4),(189,'Entity',1),(189,'Individual',2),(189,'Email',3),(189,'PhoneNumber',4),(190,'Name',1),(190,'File',2),(191,'Title',1),(191,'Owner',2),(1,'AccountManager',8),(66,'Number',1),(106,'Detail',4),(90,'Status',9),(192,'ProposalID',1),(192,'Title',2),(192,'Description',3),(192,'Type',4),(192,'Stage',5),(192,'Probability',6),(192,'Status',7),(192,'EstimatedCloseDate',8),(192,'ForecastAmmount',9),(174,'UserName',1),(174,'Name',2),(174,'Entity',3),(174,'Enabled',4),(182,'RecordName',5),(193,'TemplateID',1),(193,'Name',2),(193,'Category',3),(14,'Description',2),(14,'Entity',3),(14,'Type',4),(14,'Stage',5),(14,'Probability',6),(14,'Status',7),(14,'EstimatedCloseDate',8),(14,'ForecastAmount',9),(14,'ActualAmount',10),(14,'SalesPersonName',11),(72,'LiteratureRequested',1),(72,'WhoRequested',2),(72,'DateRequested',3),(72,'DeliveryMethod',4),(72,'Entity',5),(20,'Title',1),(20,'Details',2),(20,'Entity',3),(20,'Start',5),(20,'End',6),(20,'Type',7),(20,'Status',8),(20,'Owner',10),(8,'Title',1),(8,'Details',2),(8,'Entity',3),(8,'Start',5),(8,'End',6),(8,'Status',7),(8,'Owner',9),(10,'Title',1),(10,'Details',2),(10,'Entity',3),(10,'Start',5),(10,'End',6),(10,'CallType',7),(10,'Status',8),(10,'Owner',10),(18,'Title',1),(18,'Details',2),(18,'Entity',3),(18,'Start',5),(18,'End',6),(18,'Status',7),(18,'Owner',9),(21,'Title',1),(21,'Details',2),(21,'Entity',3),(21,'Individual',4),(21,'Start',5),(21,'End',6),(21,'Status',7),(21,'Priority',8),(21,'Owner',9),(21,'Created',10),(21,'CreatedBy',11),(21,'Notes',12),(25,'Title',1),(25,'Details',2),(25,'Entity',3),(25,'Start',5),(25,'End',6),(25,'Status',7),(25,'Owner',9),(194,'Title',1),(194,'entityName',2),(22,'Title',1),(22,'Date',2),(22,'CreatedBy',3),(22,'Detail',4),(75,'Title',1),(75,'Description',2),(75,'OrderValue',3),(75,'NumberOfRecords',4),(75,'OpportunityCount',5),(75,'OpportunityValue',6),(75,'OrderCount',7),(76,'Title',1),(76,'Description',2),(76,'StartDate',3),(76,'EndDate',4),(76,'NoOfOrders',5),(76,'Status',6),(73,'Title',1),(73,'Description',2),(73,'Start',3),(73,'End',4),(73,'RegisteredAttendees',5),(73,'OwnerName',6),(92,'TimeSlipID',1),(92,'Description',2),(92,'Project',3),(92,'Task',4),(92,'Duration',5),(92,'CreatedBy',6),(92,'Date',7),(92,'StartTime',8),(92,'EndTime',9),(77,'SKU',1),(77,'Name',2),(77,'Type',3),(77,'Price',4),(77,'OnHand',5),(77,'Tax',6),(77,'Cost',7),(77,'Vendor',8),(77,'Manufacturer',9),(30,'To',1),(30,'Cc',2),(30,'Bcc',3),(30,'Name',4),(30,'Address',5),(1,'Fax',9);

COMMIT;
