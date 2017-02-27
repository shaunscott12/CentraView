--
-- $RCSfile: default-data.sql,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:31:02 $ - $Author: mking_cv $
-- 
-- The contents of this file are subject to the Open Software License
-- Version 2.1 ("the "License"); you may not use this file except in
-- compliance with the License. You may obtain a copy of the License at
-- http://www.centraview.com/opensource/license.html
-- 
-- Software distributed under the License is distributed on an "AS IS"
-- basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
-- License for the specific language governing rights and limitations
-- under the License.
-- 
-- The Original Code is: CentraView Open Source. 
-- 
-- The developer of the Original Code is CentraView.  Portions of the
-- Original Code created by CentraView are Copyright (c) 2004 CentraView,
-- LLC; All Rights Reserved.  The terms "CentraView" and the CentraView
-- logos are trademarks and service marks of CentraView, LLC.
--


-- Turn off Foreign Key Constraint checking:
-- Refer to: http://www.mysql.com/doc/en/InnoDB_foreign_key_constraints.html
-- MySQL dump has an option that accounts for this.
SET FOREIGN_KEY_CHECKS = 0;

--
-- Table structure for table 'accountingstatus'
--

DROP TABLE IF EXISTS `accountingstatus`;
CREATE TABLE `accountingstatus` (
  `statusid` int(10) unsigned NOT NULL auto_increment,
  `title` varchar(25) NOT NULL default '',
  `description` text NOT NULL,
  PRIMARY KEY  (`statusid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'accountingstatus'
--

INSERT INTO `accountingstatus` (`statusid`, `title`, `description`) VALUES (1,'Pending','We are in process of deciding whether to accept or reject this order');
INSERT INTO `accountingstatus` (`statusid`, `title`, `description`) VALUES (2,'Accepted','We are going to supply material according to this order');
INSERT INTO `accountingstatus` (`statusid`, `title`, `description`) VALUES (3,'Rejected','We are not going to fulfill this order');

--
-- Table structure for table 'accountingterms'
--

DROP TABLE IF EXISTS `accountingterms`;
CREATE TABLE `accountingterms` (
  `termsid` int(11) unsigned NOT NULL auto_increment,
  `title` varchar(25) default NULL,
  `description` text,
  PRIMARY KEY  (`termsid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'accountingterms'
--

INSERT INTO `accountingterms` (`termsid`, `title`, `description`) VALUES (1,'AccountTerms1',NULL);
INSERT INTO `accountingterms` (`termsid`, `title`, `description`) VALUES (2,'AccountTerms2',NULL);
INSERT INTO `accountingterms` (`termsid`, `title`, `description`) VALUES (3,'AccountTerms3',NULL);
INSERT INTO `accountingterms` (`termsid`, `title`, `description`) VALUES (4,'AccountTerms4',NULL);

--
-- Table structure for table 'action'
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
-- Dumping data for table 'action'
--


--
-- Table structure for table 'activity'
--

DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
  `AllDay` enum('YES','NO') default 'NO',
  `ActivityID` int(11) unsigned NOT NULL auto_increment,
  `Type` int(11) NOT NULL default '0',
  `Priority` int(11) NOT NULL default '0',
  `Status` int(11) NOT NULL default '0',
  `Title` varchar(50) default NULL,
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
-- Dumping data for table 'activity'
--


--
-- Table structure for table 'activityaction'
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
-- Dumping data for table 'activityaction'
--


--
-- Table structure for table 'activitylink'
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
-- Dumping data for table 'activitylink'
--


--
-- Table structure for table 'activityportlet'
--

DROP TABLE IF EXISTS `activityportlet`;
CREATE TABLE `activityportlet` (
  `individualid` int(11) unsigned default NULL,
  `activitytype` int(11) unsigned default NULL,
  KEY `individualid` (`individualid`),
  KEY `activitytype` (`activitytype`)
) TYPE=InnoDB;

--
-- Dumping data for table 'activityportlet'
--


--
-- Table structure for table 'activitypriority'
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
-- Dumping data for table 'activitypriority'
--

INSERT INTO `activitypriority` (`PriorityID`, `Name`, `PriorityOrder`) VALUES (1,'High',1);
INSERT INTO `activitypriority` (`PriorityID`, `Name`, `PriorityOrder`) VALUES (2,'Medium',2);
INSERT INTO `activitypriority` (`PriorityID`, `Name`, `PriorityOrder`) VALUES (3,'Low',3);

--
-- Table structure for table 'activityresources'
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
-- Dumping data for table 'activityresources'
--


--
-- Table structure for table 'activitystatus'
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
-- Dumping data for table 'activitystatus'
--

INSERT INTO `activitystatus` (`StatusID`, `Name`) VALUES (1,'Pending');
INSERT INTO `activitystatus` (`StatusID`, `Name`) VALUES (2,'Completed');

--
-- Table structure for table 'activitytype'
--

DROP TABLE IF EXISTS `activitytype`;
CREATE TABLE `activitytype` (
  `TypeID` int(11) NOT NULL auto_increment,
  `Name` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`TypeID`),
  UNIQUE KEY `TypeID` (`TypeID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'activitytype'
--

INSERT INTO `activitytype` (`TypeID`, `Name`) VALUES (1,'Appointment');
INSERT INTO `activitytype` (`TypeID`, `Name`) VALUES (2,'Call');
INSERT INTO `activitytype` (`TypeID`, `Name`) VALUES (3,'Forecast Sale');
INSERT INTO `activitytype` (`TypeID`, `Name`) VALUES (4,'Literature Request');
INSERT INTO `activitytype` (`TypeID`, `Name`) VALUES (5,'Meeting');
INSERT INTO `activitytype` (`TypeID`, `Name`) VALUES (6,'To Do');
INSERT INTO `activitytype` (`TypeID`, `Name`) VALUES (7,'Next Action');
INSERT INTO `activitytype` (`TypeID`, `Name`) VALUES (8,'Task');

--
-- Table structure for table 'additionalmenu'
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
-- Dumping data for table 'additionalmenu'
--

INSERT INTO `additionalmenu` (`menuitem_id`, `menuitem_name`, `moduleid`, `forward_res`, `new_win`, `win_property`, `params`, `menuitem_order`) VALUES (1,'Help',60,'help',1,' width=500,height=500,status=no','',1);
INSERT INTO `additionalmenu` (`menuitem_id`, `menuitem_name`, `moduleid`, `forward_res`, `new_win`, `win_property`, `params`, `menuitem_order`) VALUES (2,'Synchronize',61,'synchronize',0,'','',2);
INSERT INTO `additionalmenu` (`menuitem_id`, `menuitem_name`, `moduleid`, `forward_res`, `new_win`, `win_property`, `params`, `menuitem_order`) VALUES (3,'Preferences',62,'preferences',0,'','',3);
INSERT INTO `additionalmenu` (`menuitem_id`, `menuitem_name`, `moduleid`, `forward_res`, `new_win`, `win_property`, `params`, `menuitem_order`) VALUES (4,'Reports',63,'reports',0,'','',4);
INSERT INTO `additionalmenu` (`menuitem_id`, `menuitem_name`, `moduleid`, `forward_res`, `new_win`, `win_property`, `params`, `menuitem_order`) VALUES (5,'Print Templates',59,'printtemplates',0,'','',5);
INSERT INTO `additionalmenu` (`menuitem_id`, `menuitem_name`, `moduleid`, `forward_res`, `new_win`, `win_property`, `params`, `menuitem_order`) VALUES (6,'Administrator',64,'administrator',0,'','',6);

--
-- Table structure for table 'address'
--

DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
  `AddressID` int(10) unsigned NOT NULL auto_increment,
  `AddressType` int(11) unsigned default NULL,
  `Street1` varchar(25) default NULL,
  `Street2` varchar(25) default NULL,
  `City` varchar(50) default NULL,
  `State` int(11) unsigned default NULL,
  `Zip` varchar(25) default NULL,
  `Country` int(11) unsigned default NULL,
  `Website` varchar(100) default NULL,
  `IsPrimary` enum('YES','NO') NOT NULL default 'YES',
  PRIMARY KEY  (`AddressID`),
  KEY `AddressType` (`AddressType`),
  KEY `State` (`State`),
  KEY `Country` (`Country`)
) TYPE=InnoDB;

--
-- Dumping data for table 'address'
--


--
-- Table structure for table 'addressrelate'
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
-- Dumping data for table 'addressrelate'
--


--
-- Table structure for table 'addresstype'
--

DROP TABLE IF EXISTS `addresstype`;
CREATE TABLE `addresstype` (
  `TypeID` int(11) unsigned NOT NULL auto_increment,
  `Name` varchar(25) default NULL,
  PRIMARY KEY  (`TypeID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'addresstype'
--

INSERT INTO `addresstype` (`TypeID`, `Name`) VALUES (1,'Billing');
INSERT INTO `addresstype` (`TypeID`, `Name`) VALUES (2,'Shipping');

--
-- Table structure for table 'alert'
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
-- Dumping data for table 'alert'
--


--
-- Table structure for table 'alertpreference'
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
-- Dumping data for table 'alertpreference'
--


--
-- Table structure for table 'applicationsetting'
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
-- Dumping data for table 'applicationsetting'
--

INSERT INTO `applicationsetting` (`modulesettingid`, `msname`, `msvalue`, `starttime`, `endtime`, `workingdays`) VALUES (1,'CUSTOMERLOGO','logo_customer.gif',NULL,NULL,'');
INSERT INTO `applicationsetting` (`modulesettingid`, `msname`, `msvalue`, `starttime`, `endtime`, `workingdays`) VALUES (2,'DEFAULTOWNER','646',NULL,NULL,'');
INSERT INTO `applicationsetting` (`modulesettingid`, `msname`, `msvalue`, `starttime`, `endtime`, `workingdays`) VALUES (3,'','','10:00:00','07:00:00','mon,tues,wed,thurs,fri,sat');

--
-- Table structure for table 'applypayment'
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
-- Dumping data for table 'applypayment'
--


--
-- Table structure for table 'attachment'
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
-- Dumping data for table 'attachment'
--


--
-- Table structure for table 'attendee'
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
-- Dumping data for table 'attendee'
--


--
-- Table structure for table 'attendeestatus'
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
-- Dumping data for table 'attendeestatus'
--

INSERT INTO `attendeestatus` (`StatusID`, `Name`) VALUES (1,'Accepted');
INSERT INTO `attendeestatus` (`StatusID`, `Name`) VALUES (2,'Declined');
INSERT INTO `attendeestatus` (`StatusID`, `Name`) VALUES (3,'Tentatively Accepted');

--
-- Table structure for table 'attic'
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
-- Dumping data for table 'attic'
--


--
-- Table structure for table 'atticdata'
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
-- Dumping data for table 'atticdata'
--


--
-- Table structure for table 'authorizationsettings'
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
-- Dumping data for table 'authorizationsettings'
--


--
-- Table structure for table 'calendarportlet'
--

DROP TABLE IF EXISTS `calendarportlet`;
CREATE TABLE `calendarportlet` (
  `individualid` int(11) unsigned default NULL,
  `activitytype` int(11) unsigned default NULL,
  KEY `individualid` (`individualid`),
  KEY `activitytype` (`activitytype`)
) TYPE=InnoDB;

--
-- Dumping data for table 'calendarportlet'
--


--
-- Table structure for table 'call'
--

DROP TABLE IF EXISTS `call`;
CREATE TABLE `call` (
  `ActivityID` int(11) unsigned NOT NULL default '0',
  `CallType` int(11) unsigned default NULL,
  KEY `ActivityID` (`ActivityID`),
  KEY `CallType` (`CallType`)
) TYPE=InnoDB;

--
-- Dumping data for table 'call'
--


--
-- Table structure for table 'calltype'
--

DROP TABLE IF EXISTS `calltype`;
CREATE TABLE `calltype` (
  `CallTypeID` int(10) unsigned NOT NULL auto_increment,
  `Name` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`CallTypeID`),
  UNIQUE KEY `CallTypeID` (`CallTypeID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'calltype'
--

INSERT INTO `calltype` (`CallTypeID`, `Name`) VALUES (1,'Incoming');
INSERT INTO `calltype` (`CallTypeID`, `Name`) VALUES (2,'Outgoing');

--
-- Table structure for table 'category'
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
  PRIMARY KEY  (`catid`),
  KEY `parent` (`parent`),
  KEY `createdby` (`createdby`),
  KEY `modifiedby` (`modifiedby`),
  KEY `owner` (`owner`)
) TYPE=InnoDB;

--
-- Dumping data for table 'category'
--

INSERT INTO `category` (`catid`, `title`, `description`, `parent`, `createdby`, `modifiedby`, `created`, `modified`, `owner`) VALUES (1,'KnowledgeBase','Root Category',0,1,1,'2003-12-14 23:50:16',20031214235016,1);


--
-- Table structure for table 'companynews'
--

DROP TABLE IF EXISTS `companynews`;
CREATE TABLE `companynews` (
  `FileID` int(11) auto_increment,
  `DateFrom` DATETIME NOT NULL default '0000-00-00 00:00:00',
  `DateTo` DATETIME NOT NULL default '0000-00-00 00:00:00',
  KEY `FileID` (`FileID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'companynews'
--


--
-- Table structure for table 'competition'
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
-- Dumping data for table 'competition'
--


--
-- Table structure for table 'contacthistory'
--

DROP TABLE IF EXISTS `contacthistory`;
CREATE TABLE `contacthistory` (
  `date` timestamp(14) NOT NULL,
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
-- Dumping data for table 'contacthistory'
--


--
-- Table structure for table 'contacttype'
--

DROP TABLE IF EXISTS `contacttype`;
CREATE TABLE `contacttype` (
  `ContactTypeID` int(11) unsigned NOT NULL auto_increment,
  `Name` varchar(25) default NULL,
  PRIMARY KEY  (`ContactTypeID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'contacttype'
--

INSERT INTO `contacttype` (`ContactTypeID`, `Name`) VALUES (1,'Entity');
INSERT INTO `contacttype` (`ContactTypeID`, `Name`) VALUES (2,'Individual');

--
-- Table structure for table 'contentstatus'
--

DROP TABLE IF EXISTS `contentstatus`;
CREATE TABLE `contentstatus` (
  `statusid` int(10) unsigned NOT NULL default '0',
  `name` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`statusid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'contentstatus'
--


--
-- Table structure for table 'country'
--

DROP TABLE IF EXISTS `country`;
CREATE TABLE `country` (
  `CountryID` int(11) unsigned NOT NULL auto_increment,
  `Name` varchar(50) default NULL,
  PRIMARY KEY  (`CountryID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'country'
--

INSERT INTO `country` (`CountryID`, `Name`) VALUES (221,'');
UPDATE country SET CountryID=0 WHERE CountryID=221;
INSERT INTO `country` (`CountryID`, `Name`) VALUES (1,'United States');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (2,'Canada');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (3,'United Kingdom');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (4,'Afghanistan');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (5,'Albania');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (6,'Algeria');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (7,'American Samoa');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (8,'Andorra');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (9,'Angola');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (10,'Anguilla');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (11,'Antigua and Barbuda');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (12,'Argentina');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (13,'Armenia');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (14,'Aruba');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (15,'Australia');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (16,'Austria');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (17,'Azerbaijan Republic');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (18,'Bahamas');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (19,'Bahrain');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (20,'Bangladesh');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (21,'Barbados');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (22,'Belarus');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (23,'Belgium');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (24,'Belize');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (25,'Benin');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (26,'Bermuda');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (27,'Bhutan');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (28,'Bolivia');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (29,'Bosnia and Herzegovina');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (30,'Botswana');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (31,'Brazil');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (32,'British Virgin Islands');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (33,'Brunei Darussalam');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (34,'Bulgaria');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (35,'Burkina Faso');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (36,'Burma');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (37,'Burundi');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (38,'Cambodia');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (39,'Cameroon');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (40,'Canada');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (41,'Cape Verde Islands');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (42,'Cayman Islands');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (43,'Central African Republic');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (44,'Chad');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (45,'Chile');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (46,'China');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (47,'Colombia');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (48,'Comoros');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (49,'Congo, Democratic Republic of the');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (50,'Congo, Republic of the');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (51,'Cook Islands');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (52,'Costa Rica');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (53,'Cote d Ivoire (Ivory Coast)');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (54,'Croatia, Republic of');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (55,'Cyprus');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (56,'Czech Republic');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (57,'Denmark');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (58,'Djibouti');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (59,'Dominica');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (60,'Dominican Republic');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (61,'Ecuador');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (62,'Egypt');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (63,'El Salvador');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (64,'Equatorial Guinea');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (65,'Eritrea');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (66,'Estonia');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (67,'Ethiopia');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (68,'Falkland Islands (Islas Malvinas)');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (69,'Fiji');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (70,'Finland');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (71,'France');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (72,'French Guiana');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (73,'French Polynesia');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (74,'Gabon Republic');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (75,'Gambia');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (76,'Georgia');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (77,'Germany');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (78,'Ghana');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (79,'Gibraltar');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (80,'Greece');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (81,'Greenland');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (82,'Grenada');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (83,'Guadeloupe');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (84,'Guam');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (85,'Guatemala');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (86,'Guernsey');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (87,'Guinea');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (88,'Guinea-Bissau');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (89,'Guyana');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (90,'Haiti');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (91,'Honduras');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (92,'Hong Kong');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (93,'Hungary');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (94,'Iceland');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (95,'India');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (96,'Indonesia');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (97,'Ireland');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (98,'Israel');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (99,'Italy');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (100,'Jamaica');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (101,'Jan Mayen');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (102,'Japan');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (103,'Jersey');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (104,'Jordan');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (105,'Kazakhstan');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (106,'Kenya Coast Republic');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (107,'Kiribati');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (108,'Korea, South');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (109,'Kuwait');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (110,'Kyrgyzstan');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (111,'Laos');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (112,'Latvia');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (113,'Lebanon');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (114,'Liechtenstein');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (115,'Lithuania');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (116,'Luxembourg');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (117,'Macau');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (118,'Macedonia');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (119,'Madagascar');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (120,'Malawi');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (121,'Malaysia');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (122,'Maldives');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (123,'Mali');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (124,'Malta');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (125,'Marshall Islands');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (126,'Martinique');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (127,'Mauritania');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (128,'Mauritius');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (129,'Mayotte');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (130,'Mexico');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (131,'Micronesia');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (132,'Moldova');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (133,'Monaco');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (134,'Mongolia');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (135,'Montserrat');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (136,'Morocco');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (137,'Mozambique');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (138,'Namibia');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (139,'Nauru');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (140,'Nepal');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (141,'Netherlands');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (142,'Netherlands Antilles');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (143,'New Caledonia');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (144,'New Zealand');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (145,'Nicaragua');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (146,'Niger');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (147,'Nigeria');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (148,'Niue');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (149,'Norway');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (150,'Oman');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (151,'Pakistan');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (152,'Palau');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (153,'Panama');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (154,'Papua New Guinea');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (155,'Paraguay');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (156,'Peru');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (157,'Philippines');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (158,'Poland');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (159,'Portugal');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (160,'Puerto Rico');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (161,'Qatar');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (162,'Romania');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (163,'Russian Federation');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (164,'Rwanda');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (165,'Saint Helena');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (166,'Saint Kitts-Nevis');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (167,'Saint Lucia');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (168,'Saint Pierre and Miquelon');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (169,'Saint Vincent and the Grenadines');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (170,'San Marino');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (171,'Saudi Arabia');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (172,'Senegal');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (173,'Seychelles');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (174,'Sierra Leone');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (175,'Singapore');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (176,'Slovakia');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (177,'Slovenia');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (178,'Solomon Islands');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (179,'Somalia');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (180,'South Africa');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (181,'Spain');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (182,'Sri Lanka');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (183,'Suriname');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (184,'Svalbard');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (185,'Swaziland');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (186,'Sweden');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (187,'Switzerland');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (188,'Syria');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (189,'Tahiti');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (190,'Taiwan');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (191,'Tajikistan');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (192,'Tanzania');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (193,'Thailand');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (194,'Togo');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (195,'Tonga');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (196,'Trinidad and Tobago');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (197,'Tunisia');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (198,'Turkey');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (199,'Turkmenistan');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (200,'Turks and Caicos Islands');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (201,'Tuvalu');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (202,'Uganda');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (203,'Ukraine');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (204,'United Arab Emirates');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (205,'United Kingdom');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (206,'United States');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (207,'Uruguay');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (208,'Uzbekistan');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (209,'Vanuatu');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (210,'Vatican City State');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (211,'Venezuela');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (212,'Vietnam');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (213,'Virgin Islands (U.S.)');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (214,'Wallis and Futuna');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (215,'Western Sahara');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (216,'Western Samoa');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (217,'Yemen');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (218,'Yugoslavia');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (219,'Zambia');
INSERT INTO `country` (`CountryID`, `Name`) VALUES (220,'Zimbabwe');

--
-- Table structure for table 'createfieldauthorisation'
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
-- Dumping data for table 'createfieldauthorisation'
--


--
-- Table structure for table 'createrecordauthorisation'
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
-- Dumping data for table 'createrecordauthorisation'
--


--
-- Table structure for table 'customfield'
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
-- Dumping data for table 'customfield'
--

INSERT INTO `customfield` (`CustomFieldID`, `Name`, `CustomFieldTypeID`, `FieldType`, `RecordType`) VALUES (1,'Type',NULL,'MULTIPLE',1);
INSERT INTO `customfield` (`CustomFieldID`, `Name`, `CustomFieldTypeID`, `FieldType`, `RecordType`) VALUES (2,'Industry',NULL,'SCALAR',1);
INSERT INTO `customfield` (`CustomFieldID`, `Name`, `CustomFieldTypeID`, `FieldType`, `RecordType`) VALUES (3,'Status',NULL,'MULTIPLE',1);
INSERT INTO `customfield` (`CustomFieldID`, `Name`, `CustomFieldTypeID`, `FieldType`, `RecordType`) VALUES (4,'Assistant\'s Name',NULL,'SCALAR',2);
INSERT INTO `customfield` (`CustomFieldID`, `Name`, `CustomFieldTypeID`, `FieldType`, `RecordType`) VALUES (5,'Department',NULL,'MULTIPLE',2);


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
-- Recordtype 76 is cvfolder (give Read privileges to default parent folders)
-- Recordtype 32 is the marketinglist (give read privileges to default marketing list)

INSERT INTO `defaultrecordauthorisation` (`recordid`, `recordtypeid`, `privilegelevel`) VALUES (1,76,30); 
INSERT INTO `defaultrecordauthorisation` (`recordid`, `recordtypeid`, `privilegelevel`) VALUES (2,76,30);
INSERT INTO `defaultrecordauthorisation` (`recordid`, `recordtypeid`, `privilegelevel`) VALUES (3,76,30);
INSERT INTO `defaultrecordauthorisation` (`recordid`, `recordtypeid`, `privilegelevel`) VALUES (4,76,30);
INSERT INTO `defaultrecordauthorisation` (`recordid`, `recordtypeid`, `privilegelevel`) VALUES (5,76,10);
INSERT INTO `defaultrecordauthorisation` (`recordid`, `recordtypeid`, `privilegelevel`) VALUES (6,76,10);
INSERT INTO `defaultrecordauthorisation` (`recordid`, `recordtypeid`, `privilegelevel`) VALUES (7,76,30);
INSERT INTO `defaultrecordauthorisation` (`recordid`, `recordtypeid`, `privilegelevel`) VALUES (1,32,30);


--
-- Table structure for table 'customfieldmultiple'
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
-- Dumping data for table 'customfieldmultiple'
--

INSERT INTO `customfieldmultiple` (`CustomFieldID`, `ValueID`, `RecordID`) VALUES (1,0,1); -- Default Entity Type
INSERT INTO `customfieldmultiple` (`CustomFieldID`, `ValueID`, `RecordID`) VALUES (3,0,1); -- Default Entity Status
INSERT INTO `customfieldmultiple` (`CustomFieldID`, `ValueID`, `RecordID`) VALUES (5,0,1); -- Default Individual Department

--
-- Table structure for table 'customfieldscalar'
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
-- Dumping data for table 'customfieldscalar'
--

INSERT INTO `customfieldscalar` (`CustomFieldID`, `RecordID`, `Value`) VALUES (2,1,''); -- Default Entity Industry
INSERT INTO `customfieldscalar` (`CustomFieldID`, `RecordID`, `Value`) VALUES (4,1,''); -- Default Individual Assistant's Name

--
-- Table structure for table 'customfieldvalue'
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
-- Dumping data for table 'customfieldvalue'
--

INSERT INTO `customfieldvalue` (`ValueID`, `CustomFieldID`, `Value`) VALUES (1,1,'Customer');
INSERT INTO `customfieldvalue` (`ValueID`, `CustomFieldID`, `Value`) VALUES (2,1,'Vendor');
INSERT INTO `customfieldvalue` (`ValueID`, `CustomFieldID`, `Value`) VALUES (3,1,'Reseller');
INSERT INTO `customfieldvalue` (`ValueID`, `CustomFieldID`, `Value`) VALUES (4,3,'Active');
INSERT INTO `customfieldvalue` (`ValueID`, `CustomFieldID`, `Value`) VALUES (5,3,'Inactive');
INSERT INTO `customfieldvalue` (`ValueID`, `CustomFieldID`, `Value`) VALUES (6,5,'Information Technology');
INSERT INTO `customfieldvalue` (`ValueID`, `CustomFieldID`, `Value`) VALUES (7,5,'Project Management');
INSERT INTO `customfieldvalue` (`ValueID`, `CustomFieldID`, `Value`) VALUES (8,5,'Support');
INSERT INTO `customfieldvalue` (`ValueID`, `CustomFieldID`, `Value`) VALUES (9,5,'Sales');
INSERT INTO `customfieldvalue` (`ValueID`, `CustomFieldID`, `Value`) VALUES (10,5,'Accounting');

--
-- Table structure for table 'cvfile'
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
-- Dumping data for table 'cvfile'
--


--
-- Table structure for table 'cvfilefolder'
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
-- Dumping data for table 'cvfilefolder'
--


--
-- Table structure for table 'cvfolder'
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
  PRIMARY KEY  (`FolderID`),
  KEY `Parent` (`Parent`),
  KEY `CreatedBy` (`CreatedBy`),
  KEY `ModifiedBy` (`ModifiedBy`),
  KEY `LocationID` (`LocationID`),
  KEY `owner` (`owner`)
) TYPE=InnoDB;

--
-- Dumping data for table 'cvfolder'
--

INSERT INTO `cvfolder` (`FolderID`, `Name`, `Parent`, `CreatedBy`, `ModifiedBy`, `CreatedOn`, `ModifiedOn`, `Description`, `FullPath`, `LocationID`, `owner`, `visibility`, `IsSystem`) VALUES (1,'CV_ROOT',0,2,2,'2003-12-14 23:51:25',20031214235125,'',NULL,1,1,'PUBLIC','TRUE');
INSERT INTO `cvfolder` (`FolderID`, `Name`, `Parent`, `CreatedBy`, `ModifiedBy`, `CreatedOn`, `ModifiedOn`, `Description`, `FullPath`, `LocationID`, `owner`, `visibility`, `IsSystem`) VALUES (2,'CVFS_ROOT',1,2,2,'2003-12-14 23:51:25',20031214235125,'',NULL,1,1,'PUBLIC','TRUE');
INSERT INTO `cvfolder` (`FolderID`, `Name`, `Parent`, `CreatedBy`, `ModifiedBy`, `CreatedOn`, `ModifiedOn`, `Description`, `FullPath`, `LocationID`, `owner`, `visibility`, `IsSystem`) VALUES (3,'CVFS_SYSTEM',1,2,2,'2003-12-14 23:51:25',20031214235125,'',NULL,1,1,'PUBLIC','TRUE');
INSERT INTO `cvfolder` (`FolderID`, `Name`, `Parent`, `CreatedBy`, `ModifiedBy`, `CreatedOn`, `ModifiedOn`, `Description`, `FullPath`, `LocationID`, `owner`, `visibility`, `IsSystem`) VALUES (4,'CVFS_USER',2,2,2,'2003-12-14 23:51:26',20031214235126,'',NULL,1,1,'PUBLIC','FALSE');
INSERT INTO `cvfolder` (`FolderID`, `Name`, `Parent`, `CreatedBy`, `ModifiedBy`, `CreatedOn`, `ModifiedOn`, `Description`, `FullPath`, `LocationID`, `owner`, `visibility`, `IsSystem`) VALUES (5,'ATTIC',3,2,2,'2003-12-14 23:51:26',20031214235126,'',NULL,1,1,'PUBLIC','TRUE');
INSERT INTO `cvfolder` (`FolderID`, `Name`, `Parent`, `CreatedBy`, `ModifiedBy`, `CreatedOn`, `ModifiedOn`, `Description`, `FullPath`, `LocationID`, `owner`, `visibility`, `IsSystem`) VALUES (6,'CV_EMAIL_DEFAULT_FOLDER',2,2,2,'2003-12-14 23:51:26',20031214235126,'',NULL,1,1,'PUBLIC','TRUE');
INSERT INTO `cvfolder` (`FolderID`, `Name`, `Parent`, `CreatedBy`, `ModifiedBy`, `CreatedOn`, `ModifiedOn`, `Description`, `FullPath`, `LocationID`, `owner`, `visibility`, `IsSystem`) VALUES (7,'CV_EMPLOYEEHANDBOOK_DEFAULT_FOLDER',2,2,2,'2003-12-14 23:51:26',20031214235126,'',NULL,1,1,'PUBLIC','TRUE');
INSERT INTO `cvfolder` (`FolderID`, `Name`, `Parent`, `CreatedBy`, `ModifiedBy`, `CreatedOn`, `ModifiedOn`, `Description`, `FullPath`, `LocationID`, `owner`, `visibility`, `IsSystem`) VALUES (8,'admin',4,2,2,'2003-12-14 23:51:27',20031214235127,'',NULL,1,1,'PUBLIC','FALSE');

--
-- Table structure for table 'cvfolderlocation'
--

DROP TABLE IF EXISTS `cvfolderlocation`;
CREATE TABLE `cvfolderlocation` (
  `LocationID` int(11) unsigned NOT NULL auto_increment,
  `Detail` varchar(250) default NULL,
  PRIMARY KEY  (`LocationID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'cvfolderlocation'
--

INSERT INTO `cvfolderlocation` (`LocationID`, `Detail`) VALUES (1,'/var/centraview/fs');

--
-- Table structure for table 'cvjoin'
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
-- Dumping data for table 'cvjoin'
--

INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (1,2,1,13,'LEFT','','AND entity.EntityID = individual.Entity');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (1,2,61,12,'INNER','','AND entity.AccountManagerID = individual.IndividualID');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (1,3,62,3,'INNER','','AND entity.AccountTeamID = GroupTBL.GroupID');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (1,11,1,51,'LEFT','contacttype','AND entity.EntityID = MOCRelate.ContactID AND MOCRelate.ContactType = ContactType.ContactTypeID AND ContactType.Name = \'Entity\'');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (1,12,1,55,'LEFT','contacttype','AND entity.EntityID = addressrelate.Contact AND addressrelate.ContactType = contacttype.ContactTypeID AND contacttype.Name = \'Entity\'');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (1,14,9,59,'INNER','','AND entity.DBase = dbase.DBaseID');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (2,1,13,1,'RIGHT','','AND entity.EntityID = individual.Entity');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (2,11,12,51,'LEFT','contacttype','AND individual.IndividualID = mocrelate.ContactID AND mocrelate.ContactType = contacttype.ContactTypeID AND contacttype.Name = \'Individual\'');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (2,12,12,55,'LEFT','contacttype','AND individual.IndividualID = addressrelate.Contact AND addressrelate.ContactType = contacttype.ContactTypeID AND contacttype.Name = \'Individual\'');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (2,13,12,58,'INNER','','AND individual.IndividualID = member.ChildID');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (4,5,32,35,'INNER','','AND address.State = state.StateID');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (4,6,34,37,'INNER','','And address.Country = country.CountryID');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (12,4,12,27,'INNER','','AND addressrelate.Address = address.AddressID');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (13,3,57,24,'INNER','','AND member.GroupID = groupTbl.GroupID');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (15,20,71,106,'LEFT','','AND emailmessage.messageid = attachment.messageid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (15,18,63,85,'INNER','','AND emailmessage.accountid = emailaccount.accountid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (15,17,71,82,'INNER','','AND emailmessage.messageid = emailstore.messageid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (15,19,71,99,'INNER','','AND emailmessage.messageid = emailrecipient.messageid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (15,2,66,12,'INNER','','AND emailmessage.fromindividual = emailrecipient.individualid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (15,2,72,19,'LEFT','','AND emailmessage.owner = individual.owner');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (15,2,65,20,'INNER','','AND emailmessage.createdby = individual.createdby');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (15,16,63,79,'INNER','','AND emailmessage.accountid = emailfolder.accountid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (17,16,83,78,'INNER','','AND emailstore.folderid = emailfolder.folderid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (2,19,12,100,'INNER','','AND individual.individualid = emailrecipient.recipientid AND emailrecipient.recipientlsgroup = \'NO\'');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (19,3,100,24,'LEFT','','AND emailrecipient.recipientid = group.groupid AND emailrecipient.recipientlsgroup = \'YES\'');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (20,33,104,162,'INNER','','AND attachment.fileid = file.fileid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (2,18,19,92,'LEFT','','AND emailaccount.owner = individual.owner');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (23,107,129,480,'INNER','','AND calltype.calltypeid = call.calltype');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (2,22,12,126,'LEFT','','AND individual.individualid = attendee.individualid AND attendee.type=\'REQUIRED\'');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (2,21,12,118,'LEFT','','AND individual.individualid = activity.owner');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (40,21,523,107,'LEFT','','AND literaturerequest.activityid = activity.activityid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (96,21,426,107,'LEFT','','AND opportunity.activityid = activity.activityid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (49,21,465,107,'LEFT','','AND task.activityid = activity.activityid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (22,21,125,107,'LEFT','','AND attendee.activityid = activity.activityid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (69,28,244,137,'LEFT','','AND record.recordid = activitylink.recordid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (29,21,139,125,'LEFT','','AND recurrence.activityid = activity.activityid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (31,21,149,139,'INNER','','AND activityaction.activityid = activity.activityid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (106,21,477,123,'INNER','','AND activitytype.typeid = activity.type');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (110,21,486,121,'INNER','','AND activitystatus.statusid = status.status');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (109,21,483,119,'INNER','','AND activityprority.priorityid = activity.priority');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (108,22,481,127,'INNER','','AND attendeestatus.statusid = attendee.status');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (107,21,479,107,'LEFT','','AND call.activityid = activity.activityid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (24,21,577,107,'LEFT','','AND appointment.activityid = activity.activityid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (25,21,579,107,'LEFT','','AND meeting.activityid = activity.activityid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (25,26,581,131,'LEFT','','AND meeting.resourceid = activityresources.activityresourceid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (24,26,578,131,'LEFT','','AND appointment.resourceid = activityresources.activityresourceid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (31,32,148,151,'INNER','','AND activityaction.actionid = action.actionid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (29,30,141,147,'LEFT','','AND recurrence.recurrenceid = recurexcept.recurrenceid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (111,2,492,19,'LEFT','','AND note.owner= individual.individualid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (111,2,491,12,'LEFT','','AND note.creator= individual.individualid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (111,2,495,12,'LEFT','','AND note.updatedby = individual.individualid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (111,112,488,496,'LEFT','','AND note.noteid = notelink.noteid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (112,69,498,244,'LEFT','','AND notelink.recordid = record.recordid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (33,2,166,19,'LEFT','','AND file.owner = individual.owner');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (33,2,172,12,'LEFT','','AND file.updatedby = individual.individualid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (33,2,159,12,'LEFT','','AND file.creator = individual.individualid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (33,34,162,175,'LEFT','','AND file.fileid = filefolder.fileid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (34,42,176,181,'INNER','','AND filefolder.folderid = folder.folderid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (96,1,429,1,'LEFT','','AND opportunity.entityid = entity.entityid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (96,2,430,12,'LEFT','','AND opportunity.individualid = individual.individualid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (96,21,426,107,'LEFT','','AND opportunity.activityid = activity.activityid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (96,100,425,446,'LEFT','','AND opportunity.opportunityid = opportunitylink.opportunityid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (96,100,436,449,'INNER','','AND opportunity.probablity = salesprobablity.probablityid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (96,97,431,440,'INNER','','AND opportunity.typeid = salestype.salestypeid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (96,98,433,442,'INNER','','AND opportunity.stage = salesstage.salesstageid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (96,99,432,444,'INNER','','AND opportunity.status = salesstatus.salesstatusid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (69,100,242,448,'INNER','','AND opportunitylink.recordid = record.recordid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (48,49,453,466,'LEFT','','AND project.projectid = task.projectid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (48,105,453,474,'LEFT','','AND project.projectid = projectlink.projectid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (48,2,460,19,'LEFT','','AND project.owner = individual.individualid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (48,2,461,12,'LEFT','','AND project.createdby = individual.individualid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (48,2,462,12,'LEFT','','AND project.modifiedby = individual.individualid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (49,50,465,472,'INNER','','AND task.taskid = taskassigned.taskid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (2,50,12,473,'INNER','','AND individual.individualid = taskassigned.individualid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (69,105,244,476,'LEFT','','AND record.recordid = projectlink.recordid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (37,1,564,10,'LEFT','','AND marketinglist.listid = entity.list');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (37,2,572,19,'LEFT','','AND marketinglist.owner = individual.owner');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (37,2,573,12,'LEFT','','AND marketinglist.creator = individual.individualid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (37,2,574,12,'LEFT','','AND marketinglist.modifiedby = individual.individualid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (2,43,19,541,'LEFT','','AND individual.owner = event.owner');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (2,43,12,542,'LEFT','','AND individual.individualid = event.creator');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (2,43,12,544,'LEFT','','AND individual.individualid = event.modifiedby');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (2,40,12,527,'LEFT','','AND individual.individualid = literaturerequest.requestedby');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (40,41,524,529,'LEFT','','AND literaturerequest.literatureid = literature.literatureid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (40,42,528,533,'LEFT','','AND literaturerequest.deliverymethod = deliverymethod.deliveryid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (43,44,535,546,'INNER','','AND event.eventid = eventregister.eventid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (44,2,547,12,'INNER','','AND eventregister.individualid = individual.individualid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (2,45,554,19,'LEFT','','AND individual.owner = promotion.owner');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (2,45,12,555,'LEFT','','AND individual.individualid = promotion.creator');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (2,45,12,556,'LEFT','','AND individual.individualid = promotion.modifiedby');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (45,46,548,559,'LEFT','','AND promotion.promotionid = promoitem.promotionid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (46,78,560,297,'INNER','','AND promoitem.itemid = item.itemid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (52,1,585,1,'LEFT','','AND ticket.entityid = entity.entityid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (52,54,588,595,'LEFT','','AND ticket.status = supportstatus.statusid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (52,56,589,607,'LEFT','','AND ticket.priority = supportpriority.priorityid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (52,53,582,595,'LEFT','','AND ticket.ticketid = thread.ticketid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (52,2,590,19,'LEFT','','AND ticket.owner = individual.owner');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (52,2,592,12,'LEFT','','AND ticket.createdby = individual.individualid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (52,2,593,12,'LEFT','','AND ticket.modifiedby = individual.individualid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (52,55,582,604,'LEFT','','AND ticket.ticketid = ticketlink.ticketid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (53,2,602,12,'LEFT','','AND thread.creator = individual.individualid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (55,69,606,244,'LEFT','','AND ticketlink.recordid = record.recordid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (69,58,244,620,'LEFT','','AND record.recordid = knowledgebaselink.recordid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (57,2,617,19,'LEFT','','AND FAQ.owner = individual.owner');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (57,2,612,12,'LEFT','','AND FAQ.createdby = individual.individualid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (57,2,614,12,'LEFT','','AND FAQ.updatedby = individual.individualid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (57,59,616,612,'LEFT','','AND FAQ.status = contentstatus.statusid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (60,2,631,19,'LEFT','','AND knowledgebase.owner = individual.owner');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (60,2,626,12,'LEFT','','AND knowledgebase.createdby = individual.individualid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (60,2,628,12,'LEFT','','AND knowledgebase.updatedby = individual.individualid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (60,59,630,612,'LEFT','','AND knowledgebase.status = contentstatus.statusid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (60,61,632,633,'LEFT','','AND knowledgebase.category = category.catid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (93,94,403,415,'LEFT','','AND timeslip.timeslipid = timeentry.timeslipid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (93,2,406,19,'LEFT','','AND timeslip.owner = individual.owner');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (93,2,407,12,'LEFT','','AND timeslip.creator = individual.individualid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (93,2,408,12,'LEFT','','AND timeslip.modifiedby = individual.individualid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (93,95,413,423,'LEFT','','AND timeslip.timestatus = status.statusid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (94,52,417,582,'LEFT','','AND timeentry.ticketid = ticket.ticketid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (94,48,416,453,'LEFT','','AND timeentry.projectid = project.projectid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (73,74,258,280,'LEFT','','AND order.orderid = orderitem.orderid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (73,77,264,294,'LEFT','','AND order.status = accountingstatus.statusid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (73,79,265,309,'LEFT','','AND order.terms = accountingterms.termsid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (73,84,258,324,'LEFT','','AND order.orderid = invoice.order');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (73,113,268,499,'LEFT','','AND order.proposal = proposal.proposalid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (74,78,281,297,'LEFT','','AND orderitem.itemid = item.itemid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (77,88,294,369,'LEFT','','AND accountingstatus.statusid = purchaseorder.status');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (77,84,294,327,'LEFT','','AND accountingstatus.statusid = invoice.status');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (79,84,309,328,'LEFT','','AND accountingterms.termsid = invoice.terms');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (75,76,290,291,'LEFT','','AND inventory.locationid = location.locationid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (75,78,289,297,'LEFT','','AND inventory.item = item.itemid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (78,81,302,318,'LEFT','','AND item.taxclass = taxclass.taxclassid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (78,80,303,312,'LEFT','','AND item.type = itemtype.itemtypeid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (78,90,297,388,'LEFT','','AND item.itemid = purchaseorderitem.itemid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (81,82,315,318,'LEFT','','AND taxclass.taxclassid = taxmatrix.taxclassid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (82,83,319,321,'LEFT','','AND taxmatrix.taxjurisdictionid = taxjurisdiction.taxjurisdictionid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (88,90,354,387,'LEFT','','AND purchaseorder.purchaseorderid = purchaseorderitem.orderid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (89,92,374,398,'LEFT','','AND expense.expenseid = expenseitem.expenseid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (84,85,324,336,'LEFT','','AND invoice.invoiceid = applypayment.invoiceid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (86,85,338,335,'LEFT','','AND payment.paymentid = applypayment.paymentid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (86,87,342,352,'LEFT','','AND payment.paymentmethod = paymentmethod.methodid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (48,116,645,643,'LEFT','','AND project.statusid=projectstatus.statusid');
INSERT INTO `cvjoin` (`tableid1`, `tableid2`, `fieldid1`, `fieldid2`, `cvjoin`, `othertablename`, `clause`) VALUES (48,1,453,1,'LEFT','projectlink','AND project.projectid=projectlink.projectid and projectlink.recordid=entity.entityid');

--
-- Table structure for table 'cvorder'
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
-- Dumping data for table 'cvorder'
--


--
-- Table structure for table 'cvtable'
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
-- Dumping data for table 'cvtable'
--

INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (1,14,'entity');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (2,15,'individual');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (3,16,'grouptbl');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (4,1,'address');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (5,1,'state');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (6,1,'country');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (7,1,'contacttype');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (8,1,'addresstype');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (9,1,'moctype');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (10,1,'methodofcontact');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (11,1,'mocrelate');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (12,1,'addressrelate');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (13,1,'member');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (14,1,'dbase');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (15,2,'emailmessage');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (16,2,'emailfolder');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (17,2,'emailstore');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (18,2,'emailaccount');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (19,2,'emailrecipient');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (20,2,'attachment');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (21,3,'activity');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (22,3,'attendee');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (23,3,'calltype');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (24,3,'appointment');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (25,3,'meeting');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (26,3,'activityresources');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (27,3,'resourcerelate');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (28,3,'activitylink');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (29,3,'recurrence');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (30,3,'recurexcept');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (31,3,'activityaction');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (32,3,'action');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (33,6,'cvfile');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (34,6,'cvfilefolder');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (35,6,'cvfolderlocation');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (36,6,'cvfolder');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (37,8,'marketinglist');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (40,8,'literaturerequest');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (41,8,'literature');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (42,8,'deliverymethod');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (43,8,'event');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (44,8,'eventregister');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (45,8,'promotion');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (46,8,'promoitem');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (48,9,'project');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (49,9,'task');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (50,9,'taskassigned');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (51,9,'projectlink');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (52,10,'ticket');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (53,10,'thread');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (54,10,'supportstatus');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (55,10,'ticketlink');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (56,10,'supportpriority');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (57,10,'faq');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (58,10,'knoledgebaselink');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (59,10,'contentstatus');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (60,10,'knowledgebase');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (61,10,'category');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (62,11,'role');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (63,11,'user');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (64,11,'alert');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (65,11,'moduleauthorization');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (66,11,'recordauthorization');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (67,11,'fieldauthorization');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (68,11,'module');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (69,11,'record');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (70,11,'field');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (71,11,'createrecordauthorization');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (72,11,'createfieldauthorization');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (73,12,'order');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (74,12,'orderitem');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (75,12,'p');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (76,12,'location');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (77,12,'accountingstatus');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (78,12,'item');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (79,12,'accountingterms');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (80,12,'itemtype');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (81,12,'taxclass');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (82,12,'taxmatrix');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (83,12,'taxjurisdiction');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (84,12,'invoice');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (85,12,'applypayment');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (86,12,'payment');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (87,12,'paymentmethod');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (88,12,'purchaseorder');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (89,12,'expense');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (90,12,'purchaseorderitem');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (91,12,'glaccount');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (92,12,'expenseitem');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (93,13,'timeslip');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (94,13,'timeentry');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (95,13,'timestatus');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (96,7,'opportunity');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (97,7,'salestype');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (98,7,'salesstage');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (99,7,'salesstatus');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (100,7,'opportunitylink');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (101,7,'salesprobablity');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (106,3,'activitytype');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (107,3,'call');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (108,3,'attendeestatus');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (109,3,'activitypriority');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (110,3,'activitystatus');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (111,5,'note');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (112,5,'notelink');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (113,7,'proposal');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (114,7,'proposallink');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (115,7,'proposalitem');
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (116,9,'projectstatus');

--
-- Table structure for table 'cvtableextra'
--

DROP TABLE IF EXISTS `cvtableextra`;
CREATE TABLE `cvtableextra` (
  `TableId` int(10) unsigned default NULL,
  `FullName` varchar(55) default NULL,
  `Appearance` enum('TOP','BOTTOM','BOTH') default 'BOTH',
  UNIQUE KEY `TableId` (`TableId`),
  CONSTRAINT `0_5010` FOREIGN KEY (`TableId`) REFERENCES `cvtable` (`tableid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'cvtableextra'
--

INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (1,'entity','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (2,'individual','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (3,'grouptbl','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (4,'address','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (5,'state','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (6,'country','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (7,'contacttype','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (8,'addresstype','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (9,'moctype','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (10,'methodofcontact','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (11,'mocrelate','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (12,'addressrelate','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (13,'member','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (14,'dbase','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (15,'emailmessage','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (16,'emailfolder','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (17,'emailstore','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (18,'emailaccount','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (19,'emailrecipient','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (20,'attachment','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (21,'activity','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (22,'attendee','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (23,'calltype','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (24,'appointment','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (25,'meeting','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (26,'activityresources','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (27,'resourcerelate','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (28,'activitylink','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (29,'recurrence','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (30,'recurexcept','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (31,'activityaction','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (32,'action','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (33,'cvfile','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (34,'cvfilefolder','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (35,'cvfolderlocation','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (36,'cvfolder','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (37,'marketinglist','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (40,'literaturerequest','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (41,'literature','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (42,'deliverymethod','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (43,'event','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (44,'eventregister','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (45,'promotion','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (46,'promoitem','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (48,'project','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (49,'task','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (50,'taskassigned','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (51,'projectlink','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (52,'ticket','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (53,'thread','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (54,'supportstatus','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (55,'ticketlink','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (56,'supportpriority','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (57,'faq','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (58,'knoledgebaselink','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (59,'contentstatus','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (60,'knowledgebase','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (61,'category','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (62,'role','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (63,'user','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (64,'alert','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (65,'moduleauthorization','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (66,'recordauthorization','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (67,'fieldauthorization','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (68,'module','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (69,'record','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (70,'field','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (71,'createrecordauthorization','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (72,'createfieldauthorization','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (73,'order','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (74,'orderitem','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (75,'p','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (76,'location','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (77,'accountingstatus','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (78,'item','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (79,'accountingterms','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (80,'itemtype','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (81,'taxclass','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (82,'taxmatrix','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (83,'taxjurisdiction','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (84,'invoice','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (85,'applypayment','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (86,'payment','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (87,'paymentmethod','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (88,'purchaseorder','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (89,'expense','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (90,'purchaseorderitem','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (91,'glaccount','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (92,'expenseitem','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (93,'timeslip','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (94,'timeentry','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (95,'timestatus','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (96,'opportunity','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (97,'salestype','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (98,'salesstage','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (99,'salesstatus','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (100,'opportunitylink','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (101,'salesprobablity','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (106,'activitytype','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (107,'call','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (108,'attendeestatus','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (109,'activitypriority','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (110,'activitystatus','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (111,'note','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (112,'notelink','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (113,'proposal','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (114,'proposallink','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (115,'proposalitem','BOTH');
INSERT INTO `cvtableextra` (`TableId`, `FullName`, `Appearance`) VALUES (116,'projectstatus','BOTH');

--
-- Table structure for table 'defaultprivilege'
--

DROP TABLE IF EXISTS `defaultprivilege`;
CREATE TABLE `defaultprivilege` (
  `DefaultPrivilegeId`   int(10) unsigned NOT NULL auto_increment,
  `OwnerId`              int(10) unsigned NOT NULL,
  `IndividualId`         int(10) unsigned NOT NULL,
  `PrivilegeLevel`       tinyint(4) NOT NULL default '0',
  PRIMARY KEY(`DefaultPrivilegeId`),
  KEY `OwnerId` (`OwnerId`),
  KEY `IndividualId` (`IndividualId`),
  CONSTRAINT `0_5013` FOREIGN KEY (`OwnerId`) REFERENCES `individual` (`IndividualId`) ON DELETE CASCADE,
  CONSTRAINT `0_5014` FOREIGN KEY (`IndividualId`) REFERENCES `individual` (`IndividualId`) ON DELETE CASCADE
)  TYPE=InnoDB;


--
-- Table structure for table 'defaultviews'
--

DROP TABLE IF EXISTS `defaultviews`;
CREATE TABLE `defaultviews` (
  `listtype` varchar(30) NOT NULL default '',
  `viewid` int(10) unsigned default NULL,
  PRIMARY KEY  (`listtype`),
  KEY `viewid` (`viewid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'defaultviews'
--

INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Entity',1);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Individual',3);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Group',4);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('GroupMember',5);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('AllActivity',6);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Address',7);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Appointment',8);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('BottomIndividual',9);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Call',10);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('CustomField',11);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Email',12);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('File',13);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('ForcastedSales',14);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('LiteratureRequests',17);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Meeting',18);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('MOC',19);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('MultiActivity',20);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('NextAction',21);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Note',22);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Rule',23);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Tasks',24);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('ToDo',25);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Ticket',66);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('FAQ',70);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('KnowledgeBase',71);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('LiteratureFulfillment',72);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Event',73);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('GLAccount',74);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Marketing',75);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Promotion',76);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Item',77);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Inventory',79);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Order',81);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Payment',82);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Expense',83);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('PurchaseOrder',84);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('VolumeDiscount',85);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Vendor',86);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('InvoiceHistory',87);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('BottomTask',88);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('BottomTimeslip',89);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Task',90);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Project',91);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Timeslip',92);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Opportunity',93);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Proposal',94);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('ActivityTask',100);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('BottomMultiActivity',101);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('BottomContacts',104);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('BottomFiles',105);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('BottomNotes',106);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('CustomFields',110);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Employee',170);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('TimeSheet',171);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Expenses',172);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('EmployeeHandbook',173);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('USER',174);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('SavedSearch',175);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('CustomView',177);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('SecurityProfile',179);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('CVAttic',180);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('CVGarbage',181);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('History',182);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('StandardReport',185);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('AdHocReport',186);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('CustomReport',187);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('ReportResult',188);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('MarketingListMembers',189);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Literature',193);

--
-- Table structure for table 'delegation'
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
-- Dumping data for table 'delegation'
--


--
-- Table structure for table 'deliverymethod'
--

DROP TABLE IF EXISTS `deliverymethod`;
CREATE TABLE `deliverymethod` (
  `DeliveryID` int(10) unsigned NOT NULL auto_increment,
  `Name` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`DeliveryID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'deliverymethod'
--

INSERT INTO `deliverymethod` (`DeliveryID`, `Name`) VALUES (1,'Email');
INSERT INTO `deliverymethod` (`DeliveryID`, `Name`) VALUES (2,'Fax');
INSERT INTO `deliverymethod` (`DeliveryID`, `Name`) VALUES (3,'Mail');
INSERT INTO `deliverymethod` (`DeliveryID`, `Name`) VALUES (4,'Postal');

--
-- Table structure for table 'emailaccount'
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
  PRIMARY KEY  (`AccountID`),
  KEY `Owner` (`Owner`)
) TYPE=InnoDB;

--
-- Dumping data for table 'emailaccount'
--


--
-- Table structure for table 'emailaction'
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
-- Dumping data for table 'emailaction'
--


--
-- Table structure for table 'emailcomposition'
--

DROP TABLE IF EXISTS `emailcomposition`;
CREATE TABLE `emailcomposition` (
  `individualid` int(10) unsigned default NULL,
  `composestyle` enum('TEXT','HTML') default NULL,
  KEY `individualid` (`individualid`)
) TYPE=InnoDB ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table 'emailcomposition'
--


--
-- Table structure for table 'emailfolder'
--

DROP TABLE IF EXISTS `emailfolder`;
CREATE TABLE `emailfolder` (
  `Parent` int(11) unsigned default NULL,
  `FolderID` int(11) unsigned NOT NULL auto_increment,
  `AccountID` int(11) unsigned default NULL,
  `Name` varchar(25) default NULL,
  `Ftype` enum('SYSTEM','USER') default NULL,
  PRIMARY KEY  (`FolderID`),
  KEY `Parent` (`Parent`),
  KEY `AccountID` (`AccountID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'emailfolder'
--

INSERT INTO `emailfolder` (`Parent`, `FolderID`, `AccountID`, `Name`, `Ftype`) VALUES (0,1,1,'root','SYSTEM');

--
-- Table structure for table 'emailmessage'
--

DROP TABLE IF EXISTS `emailmessage`;
CREATE TABLE `emailmessage` (
  `MessageID` int(11) unsigned NOT NULL auto_increment,
  `MessageDate` datetime default NULL,
  `MailFrom` varchar(50) default NULL,
  `FromIndividual` int(11) unsigned default NULL,
  `ReplyTo` varchar(25) default NULL,
  `Subject` text,
  `Headers` text,
  `Owner` int(11) unsigned default NULL,
  `CreatedBy` int(11) unsigned default NULL,
  `size` int(11) default NULL,
  `Priority` enum('HIGH','MEDIUM','LOW') default NULL,
  `Importance` enum('YES','NO') default NULL,
  `Body` text,
  `AccountID` int(11) unsigned default NULL,
  PRIMARY KEY  (`MessageID`),
  KEY `FromIndividual` (`FromIndividual`),
  KEY `Owner` (`Owner`),
  KEY `CreatedBy` (`CreatedBy`),
  KEY `AccountID` (`AccountID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'emailmessage'
--


--
-- Table structure for table 'emailportlet'
--

DROP TABLE IF EXISTS `emailportlet`;
CREATE TABLE `emailportlet` (
  `individualid` int(11) unsigned default NULL,
  `accountid` int(11) unsigned default NULL,
  KEY `individualid` (`individualid`),
  KEY `accountid` (`accountid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'emailportlet'
--


--
-- Table structure for table 'emailpreference'
--

DROP TABLE IF EXISTS `emailpreference`;
CREATE TABLE `emailpreference` (
  `individualid` int(11) default NULL,
  `composestyle` enum('TEXT','HTML') default NULL,
  KEY `individualid` (`individualid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'emailpreference'
--


--
-- Table structure for table 'emailrecipient'
--

DROP TABLE IF EXISTS `emailrecipient`;
CREATE TABLE `emailrecipient` (
  `MessageID` int(11) unsigned default NULL,
  `RecipientID` int(11) unsigned default NULL,
  `Address` varchar(50) default NULL,
  `RecipientType` enum('TO','CC','BCC','FROM') default NULL,
  `RecipientIsGroup` enum('YES','NO') default NULL,
  KEY `MessageID` (`MessageID`),
  KEY `RecipientID` (`RecipientID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'emailrecipient'
--


--
-- Table structure for table 'emailrules'
--

DROP TABLE IF EXISTS `emailrules`;
CREATE TABLE `emailrules` (
  `ruleid` int(11) unsigned NOT NULL auto_increment,
  `rulename` varchar(50) default NULL,
  `description` varchar(200) default NULL,
  `enabledstatus` enum('YES','NO') default NULL,
  `accountid` int(11) default NULL,
  `rulestatement` varchar(255) default NULL,
  PRIMARY KEY  (`ruleid`),
  KEY `accountid` (`accountid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'emailrules'
--


--
-- Table structure for table 'emailstore'
--

DROP TABLE IF EXISTS `emailstore`;
CREATE TABLE `emailstore` (
  `MessageID` int(11) unsigned default NULL,
  `EmailFolder` int(11) unsigned default NULL,
  `ReadStatus` enum('YES','NO') default NULL,
  KEY `MessageID` (`MessageID`),
  KEY `EmailFolder` (`EmailFolder`)
) TYPE=InnoDB;

--
-- Dumping data for table 'emailstore'
--


--
-- Table structure for table 'employee'
--

DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `EmployeeID` int(10) unsigned NOT NULL auto_increment,
  `IndividualID` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`EmployeeID`),
  KEY `IndividualID` (`IndividualID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'employee'
--

INSERT INTO `employee` (`EmployeeID`, `IndividualID`) VALUES (1,1);

--
-- Table structure for table 'entity'
--

DROP TABLE IF EXISTS `entity`;
CREATE TABLE `entity` (
  `EntityID` int(10) unsigned NOT NULL auto_increment,
  `ExternalID` varchar(25) default NULL,
  `Name` varchar(40) default NULL,
  `Source` int(10) unsigned NOT NULL default '0',
  `Modified` timestamp(14) NOT NULL,
  `Created` timestamp(14) NOT NULL,
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
-- Dumping data for table 'entity'
--

INSERT INTO `entity` (`EntityID`, `ExternalID`, `Name`, `Source`, `Modified`, `Created`, `Owner`, `ModifiedBy`, `DBase`, `List`, `Creator`, `AccountManagerID`, `AccountTeamID`) VALUES (1,'','Default Entity',0,NOW(),NOW(),1,1,0,1,1,NULL,NULL);

--
-- Table structure for table 'event'
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
-- Dumping data for table 'event'
--


--
-- Table structure for table 'eventregister'
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
-- Dumping data for table 'eventregister'
--


--
-- Table structure for table 'expense'
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
  `Modified` timestamp(14) NOT NULL,
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
-- Dumping data for table 'expense'
--


--
-- Table structure for table 'expenseform'
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
  `Status` enum('Deleted','Active') NOT NULL default 'Active',
  PRIMARY KEY  (`ExpenseFormID`),
  KEY `ReportingTo` (`ReportingTo`),
  KEY `Owner` (`Owner`),
  KEY `Creator` (`Creator`),
  KEY `ModifiedBy` (`ModifiedBy`)
) TYPE=InnoDB;

--
-- Dumping data for table 'expenseform'
--


--
-- Table structure for table 'expenseitem'
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
-- Dumping data for table 'expenseitem'
--


--
-- Table structure for table 'faq'
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
  PRIMARY KEY  (`faqid`),
  KEY `createdby` (`createdby`),
  KEY `updatedby` (`updatedby`),
  KEY `owner` (`owner`)
) TYPE=InnoDB;

--
-- Dumping data for table 'faq'
--


--
-- Table structure for table 'field'
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
-- Dumping data for table 'field'
--

INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (1,1,'EntityID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (2,1,'ExternalID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (3,1,'Name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (4,1,'Source');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (5,1,'Modified');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (6,1,'Created');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (7,1,'Owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (8,1,'ModifiedBy');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (9,1,'Dbase');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (10,1,'List');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (11,1,'Creator');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (12,2,'IndividualID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (13,2,'Entity');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (14,2,'FirstName');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (15,2,'MiddleInitial');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (16,2,'LastName');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (17,2,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (18,2,'PrimaryContact');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (19,2,'Owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (20,2,'CreatedBy');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (21,2,'ModifiedBy');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (22,2,'Created');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (23,2,'Modified');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (24,3,'GroupID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (25,3,'Name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (26,3,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (27,4,'AddressID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (28,4,'AddressType');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (29,4,'Street1');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (30,4,'Street2');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (31,4,'City');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (32,4,'State');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (33,4,'Zip');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (34,4,'Country');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (35,5,'StateID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (36,5,'Name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (37,6,'CountryID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (38,6,'Name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (39,7,'ContactTypeID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (40,7,'Name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (41,8,'TypeID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (42,8,'Name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (43,9,'MOCTypeID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (44,9,'Name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (45,10,'MOCID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (46,10,'MOCType');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (47,10,'Content');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (48,10,'Note');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (49,11,'MOCID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (50,11,'ContactType');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (51,11,'ContactID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (52,11,'IsPrimary');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (53,12,'Address');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (54,12,'ContactType');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (55,12,'Contact');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (56,12,'AddressType');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (57,13,'GroupID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (58,13,'ChildID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (59,14,'DBaseID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (60,14,'Name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (61,1,'AccountManagerID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (62,1,'AccountTeamID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (63,15,'AccountID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (64,15,'Body');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (65,15,'CreatedBy');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (66,15,'FromIndividual');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (67,15,'Headers');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (68,15,'Importance');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (69,15,'MailFrom');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (70,15,'MessageDate');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (71,15,'MessageID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (72,15,'Owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (73,15,'Prority');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (74,15,'Reply To');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (75,15,'size');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (76,15,'subject');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (77,16,'Parent');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (78,16,'FolderID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (79,16,'AccountID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (80,16,'Name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (81,16,'Ftype');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (82,17,'MessageID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (83,17,'EmailFolder');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (84,17,'ReadStatus');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (85,18,'AccountID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (86,18,'Address');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (87,18,'Default');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (88,18,'LeaveOnServer');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (89,18,'Login');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (90,18,'mailserver');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (91,18,'Name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (92,18,'Owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (93,18,'Password');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (94,18,'Reply To');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (95,18,'ServerType');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (96,18,'Signature');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (97,18,'SMTPServer');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (98,19,'Address');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (99,19,'MessageID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (100,19,'RecipientID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (101,19,'RecipientlsGroup');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (102,19,'RecipientType');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (103,20,'AttachmentID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (104,20,'FileID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (105,20,'FileName');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (106,20,'MessageID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (107,21,'ActivityID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (108,21,'AllDay');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (109,21,'CompletedDate');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (110,21,'Created');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (111,21,'Creator');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (112,21,'Details');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (113,21,'DueDate');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (114,21,'End');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (115,21,'Location');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (116,21,'Modified');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (117,21,'ModifiedBy');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (118,21,'Owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (119,21,'Priority');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (120,21,'Start');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (121,21,'Status');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (122,21,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (123,21,'Type');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (124,21,'visibility');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (125,22,'ActivityID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (126,22,'IndividualID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (127,22,'Status');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (128,22,'Type');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (129,23,'CallTypeID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (130,23,'Name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (131,26,'ActivityResourceID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (132,26,'Detail');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (133,26,'Name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (134,27,'ActivityID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (135,27,'ResourceID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (136,28,'ActivityID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (137,28,'RecordID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (138,28,'RecordTypeID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (139,29,'ActivityID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (140,29,'Every');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (141,29,'RecurrenceID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (142,29,'RecurrOn');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (143,29,'startdate');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (144,29,'TimePeriod');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (145,29,'Until');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (146,30,'Exception');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (147,30,'RecurrenceID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (148,31,'ActionID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (149,31,'ActivityID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (150,31,'recipient');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (151,32,'ActionID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (152,32,'ActionInterval');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (153,32,'Action Time');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (154,32,'Message');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (155,32,'Type');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (156,32,'Repeat');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (157,33,'Author');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (158,33,'Created');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (159,33,'Creator');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (160,33,'CustomerView');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (161,33,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (162,33,'FileID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (163,33,'FileSize');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (164,33,'IsTemporary');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (165,33,'Name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (166,33,'Owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (167,33,'RelateEntity');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (168,33,'RelateIndividual');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (169,33,'Status');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (170,33,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (171,33,'Updated');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (172,33,'UpdatedBy');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (173,33,'Version');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (174,33,'visibility');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (175,34,'fileid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (176,34,'folderid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (177,34,'referencetype');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (178,36,'CreatedBy');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (179,36,'CreatedOn');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (180,36,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (181,36,'FolderID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (182,36,'FullPath');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (183,36,'isSystem');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (184,36,'LocationID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (185,36,'ModifiedBy');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (186,36,'ModifiedOn');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (187,36,'Owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (188,36,'Name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (189,36,'Parent');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (190,38,'AccountManagerID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (191,38,'AccountTeamID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (192,38,'Created');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (193,38,'Creator');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (194,38,'Dbase');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (195,38,'EntityID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (196,38,'ExternalID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (197,38,'List');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (198,38,'Modified');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (199,38,'ModifiedBy');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (200,38,'Name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (201,38,'Owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (202,38,'Source');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (203,39,'Created');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (204,39,'CreatedBy');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (205,39,'Entity');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (206,39,'ExternalID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (207,39,'FirstName');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (208,39,'IndividualID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (209,39,'LastName');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (210,39,'MiddleInitial');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (211,39,'Modified');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (212,39,'ModifiedBy');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (213,39,'Owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (214,39,'PrimaryContact');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (215,39,'Source');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (216,39,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (217,62,'roleid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (218,62,'rolename');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (219,63,'IndividualID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (220,63,'Name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (221,63,'Password');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (222,63,'UserID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (223,64,'ack');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (224,64,'alertid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (225,64,'displayedtime');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (226,64,'message');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (227,64,'Owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (228,65,'IndividualID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (229,65,'moduleid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (230,65,'privilegelevel');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (231,66,'IndividualID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (232,66,'privilegelevel');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (233,66,'RecordID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (234,66,'referenceid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (235,67,'fieldid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (236,67,'IndividualID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (237,67,'privilegelevel');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (238,67,'referenceid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (239,68,'moduleid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (240,68,'name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (241,68,'parentid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (242,69,'name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (243,69,'parentid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (244,69,'recordid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (245,70,'fieldid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (246,70,'name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (247,70,'tableid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (248,71,'groupid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (249,71,'IndividualID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (250,71,'Owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (251,71,'privilegelevel');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (252,71,'RecordID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (253,72,'fieldid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (254,72,'groupid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (255,72,'IndividualID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (256,72,'Owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (257,72,'privilegelevel');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (258,73,'OrderID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (259,73,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (260,73,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (261,73,'Entity');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (262,73,'BillIndividual');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (263,73,'ShipIndividual');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (264,73,'Status');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (265,73,'Terms');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (266,73,'AccountMgr');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (267,73,'Project');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (268,73,'Proposal');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (269,73,'SubTotal');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (270,73,'Tax');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (271,73,'Shipping');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (272,73,'Discount');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (273,73,'Total');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (274,73,'Creator');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (275,73,'Owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (276,73,'ModifiedBy');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (277,73,'Created');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (278,73,'Modified');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (279,73,'PONumber');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (280,74,'OrderID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (281,74,'ItemID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (282,74,'Quantity');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (283,74,'Price');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (284,74,'SKU');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (285,74,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (286,75,'InventoryID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (287,75,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (288,75,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (289,75,'Item');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (290,75,'LocationID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (291,76,'LocationID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (292,76,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (293,76,'Parent');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (294,77,'StatusID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (295,77,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (296,77,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (297,78,'ItemID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (298,78,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (299,78,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (300,78,'ListPrice');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (301,78,'Cost');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (302,78,'TaxClass');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (303,78,'Type');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (304,78,'SKU');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (305,78,'Parent');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (306,78,'Manufacturer');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (307,78,'Vendor');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (308,78,'AlertLevel');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (309,79,'TermsID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (310,79,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (311,79,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (312,80,'ItemTypeID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (313,80,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (314,80,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (315,81,'TaxClassID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (316,81,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (317,81,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (318,82,'TaxClassID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (319,82,'TaxJurisdictionID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (320,82,'TaxRate');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (321,83,'TaxJurisdictionID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (322,83,'TaxJurisdictionName');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (323,83,'TaxJurisdictionCode');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (324,84,'InvoiceID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (325,84,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (326,84,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (327,84,'Status');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (328,84,'Terms');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (329,84,'Order');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (330,84,'Owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (331,84,'Creator');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (332,84,'ModifiedBy');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (333,84,'Created');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (334,84,'Modified');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (335,85,'PaymentID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (336,85,'InvoiceID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (337,85,'Amount');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (338,86,'PaymentID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (339,86,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (340,86,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (341,86,'EntityID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (342,86,'PaymentMethod');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (343,86,'Amount');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (344,86,'CardType');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (345,86,'CardNumber');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (346,86,'Expiration');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (347,86,'Owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (348,86,'Creator');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (349,86,'ModifiedBy');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (350,86,'Created');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (351,86,'Modified');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (352,87,'MethodID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (353,87,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (354,88,'PurchaseOrderID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (355,88,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (356,88,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (357,88,'PONumber');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (358,88,'Entity');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (359,88,'SubTotal');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (360,88,'Shipping');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (361,88,'Tax');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (362,88,'Discount');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (363,88,'Total');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (364,88,'ShipIndividual');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (365,88,'ShipAddress');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (366,88,'BillIndividual');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (367,88,'BillAddress');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (368,88,'Modified');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (369,88,'Status');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (370,88,'Owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (371,88,'Creator');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (372,88,'ModifiedBy');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (373,88,'Created');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (374,89,'ExpenseID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (375,89,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (376,89,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (377,89,'EntityID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (378,89,'Status');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (379,89,'Project');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (380,89,'Ticket');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (381,89,'Opportunity');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (382,89,'Owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (383,89,'Creator');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (384,89,'ModifiedBy');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (385,89,'Created');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (386,89,'Modified');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (387,90,'OrderID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (388,90,'ItemID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (389,90,'Quantity');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (390,90,'price');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (391,90,'SKU');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (392,90,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (393,91,'GLAAccountsID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (394,91,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (395,91,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (396,91,'Balance');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (397,91,'Parent');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (398,92,'ExpenseID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (399,92,'ExpenseItemID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (400,92,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (401,92,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (402,92,'Cost');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (403,93,'TimeSlipID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (404,93,'TimeEntryID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (405,93,'ProjectID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (406,93,'ActivityID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (407,93,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (408,93,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (409,93,'Hours');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (410,93,'CreatedBy');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (411,93,'Date');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (412,93,'Start');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (413,93,'End');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (414,94,'TimeEntryID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (415,94,'TimeSlipID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (416,94,'ProjectID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (417,94,'TicketID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (418,94,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (419,94,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (420,94,'Hours');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (421,94,'start');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (422,94,'End');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (423,95,'StatusID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (424,95,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (425,96,'OpportunityID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (426,96,'ActivityID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (427,96,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (428,96,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (429,96,'EntityID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (430,96,'IndividualID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (431,96,'TypeID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (432,96,'Status');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (433,96,'Stage');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (434,96,'ForecastAmmount');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (435,96,'ActualAmmount');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (436,96,'Probablity');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (437,96,'Source');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (438,96,'AccountManager');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (439,96,'AccountTeam');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (440,97,'SalesTypeID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (441,97,'Name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (442,98,'SalesStageID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (443,98,'Name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (444,99,'SalesStatusID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (445,99,'Name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (446,100,'OpportunityID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (447,100,'RecordTypeID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (448,100,'RecordID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (449,101,'ProbablityID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (450,101,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (451,101,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (452,101,'Probablity');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (453,48,'ProjectID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (454,48,'ProjectTitle');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (455,48,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (456,48,'Start');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (457,48,'End');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (458,48,'BudgetedHours');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (459,48,'HoursUsed');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (460,48,'Owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (461,48,'CreatedBy');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (462,48,'ModifiedBy');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (463,48,'Created');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (464,48,'Modified');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (465,49,'activityid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (466,49,'ProjectID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (467,49,'MileStone');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (470,49,'PercentComplete');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (471,49,'Parent');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (472,50,'TaskID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (473,50,'IndividualID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (474,51,'ProjectID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (475,51,'RecordTypeID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (476,51,'RecordID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (477,106,'typeid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (478,106,'name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (479,107,'activityid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (480,107,'calltype');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (481,108,'statusid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (482,108,'name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (483,109,'priorityid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (484,109,'name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (485,109,'priorityorder');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (486,110,'statusid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (487,110,'name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (488,111,'noteid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (489,111,'title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (490,111,'detail');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (491,111,'creator');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (492,111,'owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (493,111,'dateupdated');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (494,111,'datecreated');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (495,111,'updatedby');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (496,112,'noteid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (497,112,'recordtypeid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (498,112,'recordid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (499,113,'proposalid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (500,113,'title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (501,113,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (502,113,'opportunityid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (503,113,'status');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (504,113,'type');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (505,113,'stage');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (506,113,'estimatedclose');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (507,113,'probability');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (508,113,'amount');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (509,113,'owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (510,113,'creator');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (511,113,'modifiedby');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (512,113,'created');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (513,113,'modified');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (514,114,'proposalid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (515,114,'recordtypeid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (516,114,'recordid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (517,115,'proposalid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (518,115,'itemid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (519,115,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (520,115,'Quantity');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (521,115,'price');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (522,115,'name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (523,40,'ActivityID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (524,40,'literatureid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (525,40,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (526,40,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (527,40,'requestedby');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (528,40,'deliverymethod');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (529,41,'literatureid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (530,41,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (531,41,'fileid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (532,41,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (533,42,'deliveryid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (534,42,'name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (535,43,'eventid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (536,43,'title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (537,43,'detail');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (538,43,'start');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (539,43,'end');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (540,43,'for');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (541,43,'Owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (542,43,'creator');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (543,43,'created');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (544,43,'modifiedby');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (545,43,'modified');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (546,44,'eventid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (547,44,'IndividualID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (548,45,'promotionid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (549,45,'title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (550,45,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (551,45,'status');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (552,45,'startdate');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (553,45,'enddate');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (554,45,'Owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (555,45,'creator');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (556,45,'modifiedby');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (557,45,'created');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (558,45,'modified');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (559,46,'promotionid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (560,46,'itemid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (561,46,'Quantity');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (562,46,'rule');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (563,46,'price');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (564,37,'listid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (565,37,'title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (566,37,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (567,37,'numberofrecords');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (568,37,'proposals');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (569,37,'orders');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (570,37,'ordervalue');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (571,37,'status');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (572,37,'owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (573,37,'creator');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (574,37,'modifiedby');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (575,37,'created');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (576,37,'modified');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (577,24,'activityid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (578,24,'resourceid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (579,25,'activityid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (580,25,'moderator');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (581,25,'resourceid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (582,52,'ticketid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (583,52,'subject');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (584,52,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (585,52,'entityid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (586,52,'IndividualID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (587,52,'assignedto');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (588,52,'status');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (589,52,'priority');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (590,52,'owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (591,52,'created');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (592,52,'createdby');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (593,52,'modifiedby');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (594,52,'modified');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (595,54,'statusid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (596,54,'name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (597,53,'threadid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (598,53,'ticketid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (599,53,'title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (600,53,'detail');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (601,53,'created');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (602,53,'creator');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (603,53,'type');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (604,55,'ticketid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (605,55,'recordtypeid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (606,55,'recordid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (607,56,'priorityid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (608,56,'name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (609,57,'faqid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (610,57,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (611,57,'detail');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (612,57,'createdby');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (613,57,'created');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (614,57,'updatedby');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (615,57,'updated');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (616,57,'status');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (617,57,'Owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (618,58,'ticketid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (619,58,'recordtypeid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (620,58,'recordid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (621,59,'statusid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (622,59,'name');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (623,60,'kbid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (624,60,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (625,60,'detail');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (626,60,'createdby');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (627,60,'created');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (628,60,'updatedby');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (629,60,'updated');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (630,60,'status');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (631,60,'Owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (632,60,'category');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (633,61,'catid');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (634,61,'title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (635,61,'Description');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (636,61,'Parent');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (637,61,'Owner');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (638,61,'createdby');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (639,61,'modifiedby');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (640,61,'created');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (641,61,'modified');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (642,93,'BreakTime');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (643,116,'StatusID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (644,116,'Title');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (645,48,'StatusID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (646,35,'LocationID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (647,35,'Detail');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (648,48,'Manager');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (649,2,'ExternalID');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (650,2,'Source');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (651,2,'list');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (652,12,'IsPrimary');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (653,10,'syncas');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (654,10,'MOCOrder');


--
-- Table structure for table 'fieldauthorisation'
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
-- Dumping data for table 'fieldauthorisation'
--

INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,3,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,1,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,5,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,6,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,29,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,30,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,31,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,32,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,33,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,34,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,35,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,36,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,37,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,38,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,39,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,42,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,43,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,44,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,45,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,46,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,47,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,48,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,49,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,50,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,51,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,52,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,53,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,54,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,55,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,56,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,57,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,58,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,59,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,60,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,61,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,62,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,63,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,95,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,99,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,100,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,101,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,22,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,106,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,107,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,108,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,109,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,110,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,111,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,113,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,114,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,115,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,116,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,117,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,118,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,119,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,7,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,8,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,9,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,10,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,11,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,12,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,13,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,14,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,15,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,120,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,121,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,122,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,123,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,124,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,127,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,128,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,129,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,133,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,139,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,140,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,141,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,142,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,143,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,144,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,145,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,146,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,147,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,148,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,162,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,163,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,164,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,165,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,167,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,168,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,169,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,170,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,171,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,172,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,173,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,174,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,4,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,16,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,17,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,18,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,19,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,20,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,21,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,187,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,188,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,193,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,192,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,191,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,203,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,204,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,205,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,206,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,207,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,208,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,209,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,210,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,211,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,212,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,217,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,218,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,219,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,220,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,221,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,222,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,223,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,224,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,225,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,226,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,227,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,228,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,229,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,230,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,231,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,232,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,233,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,234,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,235,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,236,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,237,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,238,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,239,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,240,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,241,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,242,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,243,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,244,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,245,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,246,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,247,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,248,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,249,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,250,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,254,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,255,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,256,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,257,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,258,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,259,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,260,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,261,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,262,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,263,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,264,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,265,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,266,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,267,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,268,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,269,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,270,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,271,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,272,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,273,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,274,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,275,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,276,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,277,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,278,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,279,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,280,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,281,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,282,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,285,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,286,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,287,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,288,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,289,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,290,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,291,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,292,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,293,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,294,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,295,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,296,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,297,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,298,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,299,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,307,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,308,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,309,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,310,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,311,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,312,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,313,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,314,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,315,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,316,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,317,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,318,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,319,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,320,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,321,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,126,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,137,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,138,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,322,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,323,NULL,1,10);
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,105,NULL,1,10); 
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,149,NULL,1,10); 
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,150,NULL,1,10); 
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,151,NULL,1,10); 
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,152,NULL,1,10); 
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,153,NULL,1,10); 
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,154,NULL,1,10); 
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,156,NULL,1,10); 
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,157,NULL,1,10); 
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,159,NULL,1,10); 
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,160,NULL,1,10); 
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,161,NULL,1,10); 


--
-- Table structure for table 'fieldextra'
--

DROP TABLE IF EXISTS `fieldextra`;
CREATE TABLE `fieldextra` (
  `FieldId` int(11) unsigned default NULL,
  `FullName` varchar(55) default NULL,
  `Appearance` enum('TOP','BOTTOM','BOTH') default 'BOTH',
  UNIQUE KEY `FieldId` (`FieldId`),
  CONSTRAINT `0_5012` FOREIGN KEY (`FieldId`) REFERENCES `field` (`fieldid`),
  CONSTRAINT `0_4886` FOREIGN KEY (`FieldId`) REFERENCES `field` (`fieldid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'fieldextra'
--

INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (1,'EntityID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (2,'ExternalID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (3,'Name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (4,'Source','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (5,'Modified','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (6,'Created','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (7,'Owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (8,'ModifiedBy','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (9,'Dbase','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (10,'List','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (11,'Creator','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (12,'IndividualID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (13,'EntityID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (14,'First','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (15,'MiddleInitial','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (16,'Last','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (17,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (18,'PrimaryContact','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (19,'Owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (20,'CreatedBy','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (21,'ModifiedBy','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (22,'Created','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (23,'Modified','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (24,'GroupID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (25,'Name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (26,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (27,'AddressID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (28,'AddressType','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (29,'Stret1','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (30,'Street2','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (31,'City','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (32,'State','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (33,'Zip','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (34,'Country','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (35,'StateID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (36,'Name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (37,'CountryID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (38,'Name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (39,'ContactTypeID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (40,'Name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (41,'TypeID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (42,'Name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (43,'MOCTypeID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (44,'Name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (45,'MOCID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (46,'MOCType','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (47,'Content','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (48,'Note','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (49,'MOCID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (50,'ContactType','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (51,'ContactID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (52,'Primary','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (53,'Address','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (54,'ContactType','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (55,'Contact','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (56,'AddressType','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (57,'GroupID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (58,'ChildID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (59,'DBaseID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (60,'Name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (61,'AccountManager','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (62,'AccountTeam','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (63,'AccountID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (64,'Body','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (65,'CreatedBy','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (66,'FromIndividual','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (67,'Headers','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (68,'Importance','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (69,'MailFrom','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (70,'MessageDate','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (71,'MessageID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (72,'Owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (73,'Prority','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (74,'Reply To','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (75,'size','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (76,'subject','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (77,'Parent','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (78,'FolderID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (79,'AccountID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (80,'Name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (81,'Ftype','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (82,'MessageID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (83,'EmailFolder','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (84,'ReadStatus','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (85,'AccountID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (86,'Address','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (87,'Default','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (88,'LeaveOnServer','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (89,'Login','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (90,'mailserver','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (91,'Name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (92,'Owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (93,'Password','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (94,'Reply To','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (95,'ServerType','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (96,'Signature','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (97,'SMTPServer','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (98,'Address','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (99,'MessageID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (100,'RecipientID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (101,'RecipientlsGroup','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (102,'RecipientType','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (103,'AttachmentID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (104,'FileID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (105,'FileName','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (106,'MessageID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (107,'ActivityID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (108,'AllDay','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (109,'CompletedDate','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (110,'Created','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (111,'Creator','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (112,'Details','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (113,'DueDate','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (114,'End','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (115,'Location','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (116,'Modified','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (117,'ModifiedBy','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (118,'Owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (119,'Priority','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (120,'Start','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (121,'Status','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (122,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (123,'Type','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (124,'visibility','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (125,'ActivityID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (126,'IndividualID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (127,'Status','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (128,'Type','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (129,'CallTypeID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (130,'Name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (131,'ActivityResourceID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (132,'Detail','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (133,'Name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (134,'ActivityID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (135,'ResourceID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (136,'ActivityID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (137,'RecordID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (138,'RecordTypeID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (139,'ActivityID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (140,'Every','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (141,'RecurrenceID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (142,'RecurrOn','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (143,'startdate','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (144,'TimePeriod','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (145,'Until','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (146,'Exception','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (147,'RecurrenceID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (148,'ActionID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (149,'ActivityID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (150,'recipient','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (151,'ActionID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (152,'ActionInterval','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (153,'Action Time','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (154,'Message','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (155,'Type','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (156,'Repeat','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (157,'Author','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (158,'Created','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (159,'Creator','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (160,'CustomerView','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (161,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (162,'FileID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (163,'FileSize','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (164,'IsTemporary','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (165,'Name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (166,'Owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (167,'RelateEntity','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (168,'RelateIndividual','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (169,'Status','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (170,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (171,'Updated','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (172,'UpdatedBy','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (173,'Version','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (174,'visibility','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (175,'fileid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (176,'folderid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (177,'referencetype','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (178,'CreatedBy','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (179,'CreatedOn','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (180,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (181,'FolderID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (182,'FullPath','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (183,'isSystem','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (184,'LocationID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (185,'ModifiedBy','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (186,'ModifiedOn','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (187,'Owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (188,'Name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (189,'Parent','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (190,'AccountManagerID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (191,'AccountTeamID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (192,'Created','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (193,'Creator','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (194,'Dbase','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (195,'EntityID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (196,'ExternalID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (197,'List','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (198,'Modified','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (199,'ModifiedBy','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (200,'Name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (201,'Owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (202,'Source','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (203,'Created','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (204,'CreatedBy','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (205,'Entity','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (206,'ExternalID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (207,'FirstName','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (208,'IndividualID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (209,'LastName','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (210,'MiddleInitial','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (211,'Modified','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (212,'ModifiedBy','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (213,'Owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (214,'PrimaryContact','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (215,'Source','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (216,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (217,'roleid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (218,'rolename','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (219,'IndividualID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (220,'Name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (221,'Password','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (222,'UserID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (223,'ack','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (224,'alertid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (225,'displayedtime','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (226,'message','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (227,'Owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (228,'IndividualID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (229,'moduleid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (230,'privilegelevel','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (231,'IndividualID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (232,'privilegelevel','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (233,'RecordID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (234,'referenceid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (235,'fieldid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (236,'IndividualID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (237,'privilegelevel','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (238,'referenceid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (239,'moduleid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (240,'name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (241,'parentid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (242,'name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (243,'parentid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (244,'recordid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (245,'fieldid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (246,'name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (247,'tableid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (248,'groupid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (249,'IndividualID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (250,'Owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (251,'privilegelevel','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (252,'RecordID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (253,'fieldid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (254,'groupid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (255,'IndividualID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (256,'Owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (257,'privilegelevel','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (258,'OrderID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (259,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (260,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (261,'Entity','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (262,'BillIndividual','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (263,'ShipIndividual','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (264,'Status','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (265,'Terms','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (266,'AccountMgr','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (267,'Project','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (268,'Proposal','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (269,'SubTotal','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (270,'Tax','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (271,'Shipping','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (272,'Discount','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (273,'Total','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (274,'Creator','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (275,'Owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (276,'ModifiedBy','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (277,'Created','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (278,'Modified','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (279,'PONumber','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (280,'OrderID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (281,'ItemID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (282,'Quantity','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (283,'Price','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (284,'SKU','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (285,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (286,'InventoryID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (287,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (288,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (289,'Item','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (290,'LocationID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (291,'LocationID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (292,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (293,'Parent','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (294,'StatusID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (295,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (296,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (297,'ItemID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (298,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (299,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (300,'ListPrice','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (301,'Cost','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (302,'TaxClass','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (303,'Type','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (304,'SKU','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (305,'Parent','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (306,'Manufacturer','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (307,'Vendor','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (308,'AlertLevel','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (309,'TermsID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (310,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (311,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (312,'ItemTypeID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (313,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (314,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (315,'TaxClassID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (316,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (317,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (318,'TaxClassID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (319,'TaxJurisdictionID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (320,'TaxRate','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (321,'TaxJurisdictionID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (322,'TaxJurisdictionName','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (323,'TaxJurisdictionCode','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (324,'InvoiceID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (325,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (326,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (327,'Status','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (328,'Terms','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (329,'Order','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (330,'Owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (331,'Creator','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (332,'ModifiedBy','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (333,'Created','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (334,'Modified','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (335,'PaymentID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (336,'InvoiceID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (337,'Amount','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (338,'PaymentID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (339,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (340,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (341,'EntityID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (342,'PaymentMethod','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (343,'Amount','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (344,'CardType','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (345,'CardNumber','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (346,'Expiration','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (347,'Owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (348,'Creator','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (349,'ModifiedBy','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (350,'Created','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (351,'Modified','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (352,'MethodID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (353,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (354,'PurchaseOrderID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (355,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (356,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (357,'PONumber','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (358,'Entity','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (359,'SubTotal','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (360,'Shipping','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (361,'Tax','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (362,'Discount','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (363,'Total','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (364,'ShipIndividual','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (365,'ShipAddress','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (366,'BillIndividual','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (367,'BillAddress','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (368,'Modified','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (369,'Status','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (370,'Owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (371,'Creator','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (372,'ModifiedBy','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (373,'Created','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (374,'ExpenseID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (375,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (376,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (377,'EntityID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (378,'Status','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (379,'Project','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (380,'Ticket','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (381,'Opportunity','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (382,'Owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (383,'Creator','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (384,'ModifiedBy','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (385,'Created','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (386,'Modified','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (387,'OrderID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (388,'ItemID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (389,'Quantity','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (390,'price','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (391,'SKU','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (392,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (393,'GLAAccountsID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (394,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (395,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (396,'Balance','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (397,'Parent','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (398,'ExpenseID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (399,'ExpenseItemID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (400,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (401,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (402,'Cost','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (403,'TimeSlipID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (404,'TimeEntryID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (405,'ProjectID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (406,'ActivityID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (407,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (408,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (409,'Hours','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (410,'CreatedBy','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (411,'Date','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (412,'Start','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (413,'End','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (414,'TimeEntryID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (415,'TimeSlipID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (416,'ProjectID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (417,'TicketID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (418,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (419,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (420,'Hours','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (421,'start','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (422,'End','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (423,'StatusID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (424,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (425,'OpportunityID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (426,'ActivityID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (427,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (428,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (429,'EntityID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (430,'IndividualID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (431,'TypeID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (432,'Status','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (433,'Stage','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (434,'ForecastAmmount','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (435,'ActualAmmount','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (436,'Probablity','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (437,'Source','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (438,'AccountManager','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (439,'AccountTeam','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (440,'SalesTypeID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (441,'Name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (442,'SalesStageID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (443,'Name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (444,'SalesStatusID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (445,'Name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (446,'OpportunityID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (447,'RecordTypeID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (448,'RecordID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (449,'ProbablityID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (450,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (451,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (452,'Probablity','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (453,'ProjectID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (454,'ProjectTitle','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (455,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (456,'Start','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (457,'End','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (458,'BudgetedHours','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (459,'HoursUsed','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (460,'Owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (461,'CreatedBy','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (462,'ModifiedBy','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (463,'Created','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (464,'Modified','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (465,'activityid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (466,'ProjectID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (467,'MileStone','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (470,'PercentComplete','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (471,'Parent','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (472,'TaskID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (473,'IndividualID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (474,'ProjectID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (475,'RecordTypeID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (476,'RecordID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (477,'typeid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (478,'name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (479,'activityid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (480,'calltype','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (481,'statusid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (482,'name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (483,'priorityid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (484,'name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (485,'priorityorder','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (486,'statusid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (487,'name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (488,'noteid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (489,'title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (490,'detail','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (491,'creator','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (492,'owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (493,'dateupdated','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (494,'datecreated','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (495,'updatedby','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (496,'noteid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (497,'recordtypeid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (498,'recordid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (499,'proposalid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (500,'title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (501,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (502,'opportunityid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (503,'status','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (504,'type','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (505,'stage','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (506,'estimatedclose','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (507,'probability','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (508,'amount','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (509,'owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (510,'creator','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (511,'modifiedby','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (512,'created','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (513,'modified','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (514,'proposalid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (515,'recordtypeid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (516,'recordid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (517,'proposalid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (518,'itemid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (519,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (520,'Quantity','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (521,'price','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (522,'name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (523,'ActivityID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (524,'literatureid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (525,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (526,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (527,'requestedby','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (528,'deliverymethod','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (529,'literatureid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (530,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (531,'fileid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (532,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (533,'deliveryid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (534,'name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (535,'eventid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (536,'title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (537,'detail','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (538,'start','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (539,'end','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (540,'for','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (541,'Owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (542,'creator','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (543,'created','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (544,'modifiedby','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (545,'modified','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (546,'eventid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (547,'IndividualID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (548,'promotionid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (549,'title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (550,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (551,'status','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (552,'startdate','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (553,'enddate','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (554,'Owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (555,'creator','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (556,'modifiedby','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (557,'created','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (558,'modified','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (559,'promotionid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (560,'itemid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (561,'Quantity','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (562,'rule','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (563,'price','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (564,'listid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (565,'title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (566,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (567,'numberofrecords','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (568,'proposals','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (569,'orders','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (570,'ordervalue','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (571,'status','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (572,'owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (573,'creator','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (574,'modifiedby','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (575,'created','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (576,'modified','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (577,'activityid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (578,'resourceid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (579,'activityid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (580,'moderator','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (581,'resourceid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (582,'ticketid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (583,'subject','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (584,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (585,'entityid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (586,'IndividualID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (587,'assignedto','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (588,'status','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (589,'priority','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (590,'owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (591,'created','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (592,'createdby','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (593,'modifiedby','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (594,'modified','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (595,'statusid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (596,'name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (597,'threadid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (598,'ticketid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (599,'title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (600,'detail','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (601,'created','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (602,'creator','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (603,'type','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (604,'ticketid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (605,'recordtypeid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (606,'recordid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (607,'priorityid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (608,'name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (609,'faqid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (610,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (611,'detail','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (612,'createdby','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (613,'created','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (614,'updatedby','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (615,'updated','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (616,'status','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (617,'Owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (618,'ticketid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (619,'recordtypeid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (620,'recordid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (621,'statusid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (622,'name','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (623,'kbid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (624,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (625,'detail','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (626,'createdby','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (627,'created','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (628,'updatedby','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (629,'updated','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (630,'status','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (631,'Owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (632,'category','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (633,'catid','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (634,'title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (635,'Description','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (636,'Parent','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (637,'Owner','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (638,'createdby','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (639,'modifiedby','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (640,'created','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (641,'modified','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (642,'BreakTime','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (643,'StatusID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (644,'Title','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (645,'StatusID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (646,'LocationID','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (647,'Detail','BOTH');
INSERT INTO `fieldextra` (`FieldId`, `FullName`, `Appearance`) VALUES (648,'Manager','BOTH');

--
-- Table structure for table 'finalmappings'
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
-- Dumping data for table 'finalmappings'
--

INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (1,'entity','individual','firstname');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (2,'entity','individual','middlename');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (3,'entity','individual','lastname');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (4,'entity','individual','title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (5,'entity','individual','source');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (6,'entity','state','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (7,'entity','country','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (8,'entity','emailmessage','body');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (9,'entity','grouptbl','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (10,'entity','address','street1');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (11,'entity','address','street2');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (12,'entity','address','city');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (13,'entity','address','zip');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (14,'individual','entity','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (15,'individual','dbase','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (16,'individual','address','street1');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (17,'individual','address','street2');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (18,'individual','address','city');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (19,'individual','address','zip');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (20,'individual','state','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (21,'individual','country','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (22,'individual','emailmessage','body');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (23,'individual','grouptbl','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (24,'grouptbl','entity','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (25,'grouptbl','individual','firstname');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (26,'grouptbl','individual','middlename');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (27,'grouptbl','individual','lastname');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (28,'grouptbl','individual','title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (29,'grouptbl','address','street1');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (30,'grouptbl','address','street2');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (31,'grouptbl','address','city');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (32,'grouptbl','address','zip');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (33,'grouptbl','state','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (34,'grouptbl','country','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (35,'emailmessage','emailmessage','From');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (36,'emailmessage','emailmessage','ReplyTo');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (37,'emailmessage','emailmessage','Subject');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (38,'emailmessage','emailmessage','Body');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (39,'emailmessage','emailmessage','Headres');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (40,'emailmessage','emailfolder','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (41,'emailmessage','emailaccount','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (42,'emailmessage','emailaccount','Login');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (43,'emailmessage','emailaccount','SMTPServer');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (44,'emailmessage','emailaccount','MailServer');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (45,'emailmessage','emailaccount','Address');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (46,'emailmessage','emailaccount','ReplyTo');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (47,'emailmessage','emailaccount','Signature');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (48,'emailmessage','emailrecipient','Address');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (49,'emailmessage','individual','firstname');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (50,'emailmessage','individual','middlename');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (51,'emailmessage','individual','lastname');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (52,'emailmessage','individual','title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (53,'emailmessage','address','street1');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (54,'emailmessage','address','street2');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (55,'emailmessage','address','city');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (56,'emailmessage','address','zip');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (57,'emailmessage','state','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (58,'emailmessage','country','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (59,'emailmessage','grouptbl','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (60,'emailmessage','file','Title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (61,'emailmessage','file','Description');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (62,'emailmessage','file','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (63,'emailmessage','filelocation','Detail');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (64,'emailmessage','folder','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (65,'activity','activity','title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (66,'activity','calltype','name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (67,'activity','activityresources','name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (68,'activity','action','message');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (69,'activity','activitytype','name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (70,'activity','attendeestatus','name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (71,'activity','activitypriority','name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (72,'activity','activitystatus','name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (73,'activity','individual','firstname');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (74,'activity','individual','middlename');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (75,'activity','individual','lastname');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (76,'activity','individual','title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (77,'activity','individual','source');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (78,'activity','literaturerequest','title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (79,'activity','literaturerequest','description');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (80,'activity','opportunity','title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (81,'activity','opportunity','description');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (82,'activity','opportunity','source');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (83,'activity','entity','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (84,'activity','salestype','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (85,'activity','salesstage','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (86,'activity','salesstatus','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (87,'activity','salesprobablity','description');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (88,'activity','salesprobablity','title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (89,'activity','record','name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (90,'activity','project','projecttitle');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (91,'activity','project','description');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (92,'activity','address','street1');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (93,'activity','address','street2');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (94,'activity','address','city');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (95,'activity','address','zip');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (96,'activity','address','website');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (97,'activity','country','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (98,'activity','state','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (99,'note','note','title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (100,'note','note','detail');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (101,'note','entity','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (102,'note','individual','firstname');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (103,'note','individual','middlename');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (104,'note','individual','lastname');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (105,'note','individual','title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (106,'note','address','street1');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (107,'note','address','street2');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (108,'note','address','city');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (109,'note','address','zip');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (110,'note','state','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (111,'note','country','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (112,'note','record','name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (113,'grouptbl','grouptbl','description');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (115,'cvfile','cvfile','Title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (116,'cvfile','cvfile','Description');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (117,'cvfile','cvfile','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (133,'ticket','ticket','Subject');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (134,'faq','faq','Title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (135,'knowledgebase','knowledgebase','kbid');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (136,'inventory','inventory','Title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (137,'glaccount','glaccount','Title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (138,'project','project','projecttitle');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (139,'project','entity','Name');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (140,'project','individual','firstname');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (141,'project','individual','lastname');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (142,'project','task','title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (143,'project','timeslip','description');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (145,'project','activity','Title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (146,'task','activity','Title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (147,'task','activity','Status');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (148,'task','activity','Priority');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (149,'task','project','ProjectTitle');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (150,'timeslip','project','ProjectTitle');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (151,'timeslip','task','Title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (152,'knowledgebase','knowledgebase','Title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (153,'project','projectstatus','Title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (154,'opportunity','opportunity','Title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (155,'proposal','proposal','Title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (156,'proposal','proposal','Description');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (157,'opportunity','opportunity','Description');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (158,'marketinglist','marketinglist','Title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (159,'marketinglist','marketinglist','Description');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (160,'promotion','promotion','Title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (161,'promotion','promotion','Description');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (162,'event','event','Title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (163,'event','event','Detail');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (164,'cvorder','cvorder','Title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (165,'cvorder','cvorder','Description');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (166,'invoice','invoice','Title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (167,'invoice','invoice','Description');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (168,'payment','payment','Title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (169,'payment','payment','Description');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (170,'expense','expense','Title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (171,'expense','expense','Description');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (172,'purchaseorder','purchaseorder','Title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (173,'purchaseorder','purchaseorder','Description');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (174,'item','item','Title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (175,'item','item','Description');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (176,'glaccount','glaccount','Title');
INSERT INTO `finalmappings` (`mapid`, `module`, `table`, `column`) VALUES (177,'glaccount','glaccount','Description');



--
-- Table structure for table 'folderlist'
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
-- Dumping data for table 'folderlist'
--


--
-- Table structure for table 'glaccount'
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
-- Dumping data for table 'glaccount'
--

INSERT INTO `glaccount` (`GLAccountsID`, `externalID`, `Title`, `Description`, `Balance`, `GLAccountType`, `Parent`) VALUES (1,'','Default Account','Default account',0,'A',0);

--
-- Table structure for table 'glaccounttype'
--

DROP TABLE IF EXISTS `glaccounttype`;
CREATE TABLE `glaccounttype` (
  `accounttype` varchar(30) NOT NULL default '',
  PRIMARY KEY  (`accounttype`)
) TYPE=InnoDB;

--
-- Dumping data for table 'glaccounttype'
--


--
-- Table structure for table 'grouptbl'
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
-- Dumping data for table 'grouptbl'
--


--
-- Table structure for table 'historytype'
--

DROP TABLE IF EXISTS `historytype`;
CREATE TABLE `historytype` (
  `historytypeid` int(10) unsigned NOT NULL auto_increment,
  `historytype` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`historytypeid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'historytype'
--
INSERT INTO `historytype` (`historytypeid`, `historytype`) VALUES (1,'Deleted');
INSERT INTO `historytype` (`historytypeid`, `historytype`) VALUES (2,'Created');
INSERT INTO `historytype` (`historytypeid`, `historytype`) VALUES (3,'Restored');
INSERT INTO `historytype` (`historytypeid`, `historytype`) VALUES (4,'Updated');
INSERT INTO `historytype` (`historytypeid`, `historytype`) VALUES (5,'CompleteActivity');


--
-- Table structure for table 'individual'
--

DROP TABLE IF EXISTS `individual`;
CREATE TABLE `individual` (
  `IndividualID` int(10) unsigned NOT NULL auto_increment,
  `Entity` int(10) unsigned NOT NULL default '0',
  `FirstName` varchar(25) default NULL,
  `MiddleInitial` char(1) default NULL,
  `LastName` varchar(25) default NULL,
  `Title` varchar(25) default NULL,
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
-- Dumping data for table 'individual'
--

INSERT INTO `individual` (`IndividualID`, `Entity`, `FirstName`, `MiddleInitial`, `LastName`, `Title`, `PrimaryContact`, `Owner`, `CreatedBy`, `ModifiedBy`, `Created`, `Modified`, `ExternalID`, `Source`, `list`) VALUES (1,1,'Administrative','','User','','YES',1,1,1,NOW(),NOW(),'',0,1);

--
-- Table structure for table 'inventory'
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
  PRIMARY KEY  (`inventoryid`),
  KEY `locationid` (`locationid`),
  KEY `item` (`item`),
  KEY `statusid` (`statusid`),
  KEY `customerid` (`customerid`),
  KEY `vendorid` (`vendorid`),
  KEY `LineID` (`LineID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'inventory'
--


--
-- Table structure for table 'inventorystatus'
--

DROP TABLE IF EXISTS `inventorystatus`;
CREATE TABLE `inventorystatus` (
  `InventoryStatusID` int(11) unsigned NOT NULL auto_increment,
  `Name` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`InventoryStatusID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'inventorystatus'
--

INSERT INTO `inventorystatus` (`InventoryStatusID`, `Name`) VALUES (1,'Not Available');
INSERT INTO `inventorystatus` (`InventoryStatusID`, `Name`) VALUES (2,'Available');
INSERT INTO `inventorystatus` (`InventoryStatusID`, `Name`) VALUES (3,'On Hold');
INSERT INTO `inventorystatus` (`InventoryStatusID`, `Name`) VALUES (4,'Backordered');
INSERT INTO `inventorystatus` (`InventoryStatusID`, `Name`) VALUES (5,'On Order');

--
-- Table structure for table 'invoice'
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
  `Modified` timestamp(14) NOT NULL,
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
-- Dumping data for table 'invoice'
--


--
-- Table structure for table 'invoiceitems'
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
  `Tax Rate` float NOT NULL default '0',
  `Tax` int(11) NOT NULL default '0',
  `status` enum('Active','Deleted') NOT NULL default 'Active',
  `TransactionID` int(11) NOT NULL default '0',
  PRIMARY KEY  (`InvoiceLineID`),
  KEY `InvoiceID` (`InvoiceID`),
  KEY `ItemID` (`ItemID`),
  KEY `Tax` (`Tax`),
  KEY `TransactionID` (`TransactionID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'invoiceitems'
--


--
-- Table structure for table 'item'
--

DROP TABLE IF EXISTS `item`;
CREATE TABLE `item` (
  `itemid` int(10) unsigned NOT NULL auto_increment,
  `externalid` varchar(25) default '',
  `title` varchar(25) default NULL,
  `description` text,
  `listprice` float unsigned default '0',
  `cost` float unsigned default '0',
  `taxclass` int(10) unsigned default '0',
  `type` int(10) unsigned default '0',
  `sku` varchar(25) default NULL,
  `parent` int(10) unsigned default '0',
  `manufacturerid` int(10) unsigned default '0',
  `vendorid` int(10) unsigned default '0',
  `alertlevel` int(11) unsigned default '0',
  `itemcategory` int(11) unsigned default '0',
  `createdby` int(10) unsigned default NULL,
  `modifiedby` int(10) unsigned default NULL,
  `createddate` timestamp(14) NOT NULL,
  `modifieddate` timestamp(14) NOT NULL,
  `glaccountid` int(10) unsigned default NULL,
  `deletestatus` enum('Active','Deleted') NOT NULL default 'Active',
  `LinkToInventory` enum('YES','NO') default 'NO',
  PRIMARY KEY  (`itemid`),
  KEY `taxclass` (`taxclass`),
  KEY `type` (`type`),
  KEY `parent` (`parent`),
  KEY `manufacturerid` (`manufacturerid`),
  KEY `vendorid` (`vendorid`),
  KEY `alertlevel` (`alertlevel`),
  KEY `itemcategory` (`itemcategory`),
  KEY `createdby` (`createdby`),
  KEY `modifiedby` (`modifiedby`),
  KEY `glaccountid` (`glaccountid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'item'
--


--
-- Table structure for table 'itemcategory'
--

DROP TABLE IF EXISTS `itemcategory`;
CREATE TABLE `itemcategory` (
  `categoryid` int(10) unsigned NOT NULL auto_increment,
  `categoryname` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`categoryid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'itemcategory'
--


--
-- Table structure for table 'itemtype'
--

DROP TABLE IF EXISTS `itemtype`;
CREATE TABLE `itemtype` (
  `itemtypeid` int(10) unsigned NOT NULL auto_increment,
  `title` varchar(25) NOT NULL default '',
  `description` text NOT NULL,
  PRIMARY KEY  (`itemtypeid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'itemtype'
--

INSERT INTO `itemtype` (`itemtypeid`, `title`, `description`) VALUES (1,'Inventory Item','');
INSERT INTO `itemtype` (`itemtypeid`, `title`, `description`) VALUES (2,'Non-inventory Item','');
INSERT INTO `itemtype` (`itemtypeid`, `title`, `description`) VALUES (3,'Service','');
INSERT INTO `itemtype` (`itemtypeid`, `title`, `description`) VALUES (4,'Other Charge','');
INSERT INTO `itemtype` (`itemtypeid`, `title`, `description`) VALUES (5,'Discount','');
INSERT INTO `itemtype` (`itemtypeid`, `title`, `description`) VALUES (6,'Expense','');
INSERT INTO `itemtype` (`itemtypeid`, `title`, `description`) VALUES (7,'Support Instance','');
INSERT INTO `itemtype` (`itemtypeid`, `title`, `description`) VALUES (8,'Support Contract','');

--
-- Table structure for table 'knowledgebase'
--

DROP TABLE IF EXISTS `knowledgebase`;
CREATE TABLE `knowledgebase` (
  `kbid` int(10) unsigned NOT NULL auto_increment,
  `title` varchar(25) default NULL,
  `detail` text,
  `createdby` int(10) unsigned NOT NULL default '0',
  `created` datetime NOT NULL default '0000-00-00 00:00:00',
  `updatedby` int(10) unsigned NOT NULL default '0',
  `updated` timestamp(14) NOT NULL,
  `owner` int(10) unsigned NOT NULL default '0',
  `category` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`kbid`),
  KEY `createdby` (`createdby`),
  KEY `updatedby` (`updatedby`),
  KEY `owner` (`owner`),
  KEY `category` (`category`)
) TYPE=InnoDB;

--
-- Dumping data for table 'knowledgebase'
--


--
-- Table structure for table 'knowledgebaselink'
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
-- Dumping data for table 'knowledgebaselink'
--


--
-- Table structure for table 'license'
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
-- Dumping data for table 'license'
--


--
-- Table structure for table 'listcolumns'
--

DROP TABLE IF EXISTS `listcolumns`;
CREATE TABLE `listcolumns` (
  `listtype` varchar(30) NOT NULL default '',
  `columnname` varchar(100) NOT NULL default '',
  `columnorder` int(11) NOT NULL default '0',
  PRIMARY KEY  (`listtype`,`columnorder`)
) TYPE=InnoDB;

--
-- Dumping data for table 'listcolumns'
--

INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ActivityTask','Title',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ActivityTask','CreatedBy',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ActivityTask','Created',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ActivityTask','DueDate',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ActivityTask','Priority',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ActivityTask','Status',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Address','Street1',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Address','Street2',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Address','City',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Address','State',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Address','ZipCode',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Address','Country',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('AdHocReport','ReportID',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('AdHocReport','Name',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('AdHocReport','Description',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('AdHocReport','View',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('AllActivity','Type',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('AllActivity','Title',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('AllActivity','Created',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('AllActivity','Due',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('AllActivity','Priority',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('AllActivity','CreatedBy',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('AllActivity','Status',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Appointment','Title',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Appointment','Start',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Appointment','End',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Appointment','Duration',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Appointment','Priority',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Appointment','Createdby',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Appointment','Status',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomContacts','Name',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomContacts','Title',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomContacts','Entity',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomContacts','Phone',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomContacts','Fax',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomContacts','Email',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomEntityTicket','Subject',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomEntityTicket','DateOpened',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomEntityTicket','DateClosed',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomEntityTicket','Status',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomEntityTicket','AssignedTo',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomFiles','Name',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomFiles','Description',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomFiles','Created',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomFiles','Updated',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomFiles','CreatedBy',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomIndividual','Name',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomIndividual','Title',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomIndividual','Entity',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomIndividual','Phone',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomIndividual','Fax',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomIndividual','Email',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomIndividualTicket','Subject',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomIndividualTicket','DateOpened',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomIndividualTicket','DateClosed',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomIndividualTicket','Status',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomIndividualTicket','AssignedTo',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomMultiActivity','Type',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomMultiActivity','Title',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomMultiActivity','Created',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomMultiActivity','Due',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomMultiActivity','Priority',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomMultiActivity','CreatedBy',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomMultiActivity','Status',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomNotes','Title',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomNotes','Date',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomNotes','Priority',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomNotes','CreatedBy',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomTask','Task',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomTask','Milestone',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomTask','Owner',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomTask','StartDate',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomTask','DueDate',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomTask','Complete',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomTimeslip','Task',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomTimeslip','Description',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomTimeslip','Duration',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomTimeslip','CreatedBy',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomTimeslip','Date',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomTimeslip','StartTime',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomTimeslip','EndTime',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Call','Title',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Call','CallType',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Call','Duration',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Call','CreatedBy',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Call','Due',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Call','Priority',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Call','Created',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Call','Status',8);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Competition','EntityID',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Competition','EntityName',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Competition','Strengths',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Competition','Weaknesses',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Competition','Notes',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('CustomField','Field',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('CustomField','Value',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('CustomFields','Name',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('CustomFields','Type',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('CustomFields','Module',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('CustomFields','Record',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('CustomReport','ReportID',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('CustomReport','Name',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('CustomReport','Description',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('CustomReport','View',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('CustomView','ViewID',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('CustomView','ViewName',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('CustomView','Module',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('CustomView','Record',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('CVAttic','Title',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('CVAttic','Module',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('CVAttic','Record',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('CVAttic','Owner',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('CVAttic','Deleted',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('CVGarbage','Title',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('CVGarbage','Module',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('CVGarbage','Record',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('CVGarbage','Owner',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('CVGarbage','Deleted',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Email','Subject',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Email','From',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Email','To',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Email','Received',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Email','Size',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Employee','Name',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Employee','Title',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Employee','Compony',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Employee','Phone',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Employee','Fax',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Employee','Email',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('EmployeeHandbook','Name',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('EmployeeHandbook','Description',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('EmployeeHandbook','Created',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('EmployeeHandbook','Updated',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('EmployeeHandbook','CreatedBy',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Entity','EntityID',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Entity','Name',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Entity','PrimaryContact',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Entity','Phone',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Entity','Email',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Entity','Address',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Entity','Street1',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Entity','Street2',8);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Entity','City',9);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Entity','State',10);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Entity','Zip',11);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Entity','Country',12);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Entity','ListID',13);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Entity','Website',14);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Event','eventid',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Event','title',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Event','detail',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Event','start',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Event','end',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Event','registeredattendees',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Event','ownername',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('EventAtendees','individualname',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('EventAtendees','email',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('EventAtendees','accepted',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Expense','ExpenseID',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Expense','Amount',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Expense','Created',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Expense','EntityName',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Expense','Status',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Expense','Creator',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Expenses','ID',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Expenses','Employee',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Expenses','StartDate',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Expenses','EndDate',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Expenses','Amount',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Expenses','Status',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Expenses','CreatedBy',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('FAQ','Title',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('FAQ','Created',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('FAQ','Updated',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('File','Name',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('File','Description',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('File','Created',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('File','Updated',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('File','CreatedBy',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('File','FolderName',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ForcastedSales','Title',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ForcastedSales','Entity',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ForcastedSales','Type',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ForcastedSales','Probability',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ForcastedSales','Status',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ForcastedSales','CloseDate',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ForcastedSales','Forecast Amnt.',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ForcastedSales','Sales Rep.',8);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('GLAccount','Name',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('GLAccount','Type',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('GLAccount','Balance',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('GLAccount','ParentAccount',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Group','Name',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Group','Description',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Group','NOOfMembers',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('GroupMember','Name',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('GroupMember','Title',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('GroupMember','Email',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('GroupMember','Phone',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('History','Date',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('History','User',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('History','Action',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('History','Type',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('History','Reference',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('History','Notes',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Individual','Name',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Individual','Title',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Individual','Entity',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Individual','Phone',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Individual','Fax',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Individual','Email',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Inventory','InventoryID',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Inventory','ItemName',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Inventory','Identifier',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Inventory','Manufacturer',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Inventory','Vendor',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('InvoiceHistory','InvoiceID',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('InvoiceHistory','Order',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('InvoiceHistory','CustomerID',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('InvoiceHistory','Total',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('InvoiceHistory','Paid',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('InvoiceHistory','Creator',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Item','SKU',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Item','Name',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Item','Category',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Item','Type',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Item','Price',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Item','OnHand',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Item','TaxClass',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Item','Cost',8);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Knowledgebase','Name',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Knowledgebase','DateCreated',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Knowledgebase','DateUpdated',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('LiteratureFulfillment','Entity',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('LiteratureFulfillment','WhoRequested',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('LiteratureFulfillment','DateRequested',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('LiteratureFulfillment','LiteratureRequested',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('LiteratureFulfillment','DeliveryMethod',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Marketing','ListID',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Marketing','title',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Marketing','description',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Marketing','ordervalue',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Marketing','numberofrecords',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Marketing','proposals',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Marketing','orders',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MarketingListMembers','Entity',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MarketingListMembers','Individual',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MarketingListMembers','Email',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MarketingListMembers','PhoneNumber',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Meeting','Title',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Meeting','Location',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Meeting','Start',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Meeting','End',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Meeting','Duration',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Meeting','Priority',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Meeting','CreatedBy',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Meeting','Status',8);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MOC','Type',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MOC','Content',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MOC','Notes',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MultiActivity','Type',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MultiActivity','Title',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MultiActivity','Created',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MultiActivity','Due',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MultiActivity','Priority',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MultiActivity','CreatedBy',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MultiActivity','Status',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('NextAction','Title',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('NextAction','Created',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('NextAction','Due',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('NextAction','Priority',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('NextAction','CreatedBy',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('NextAction','Status',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Note','Title',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Note','Date',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Note','CreatedBy',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Note', 'Detail', 4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Opportunity','OpportunityID',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Opportunity','Title',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Opportunity','Description',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Opportunity','EntityID',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Opportunity','Entity',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Opportunity','Type',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Opportunity','Stage',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Opportunity','Probability',8);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Opportunity','Status',9);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Opportunity','EstimatedCloseDate',10);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Opportunity','ForecastAmmount',11);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Opportunity','ActualAmount',12);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Opportunity','SalePersonName',13);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Order','OrderNO',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Order','Entity',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Order','Date',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Order','Total',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Order','Sales',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Order','SalesRep',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Payment','PaymentID',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Payment','Entity',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Payment','AmountPaid',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Payment','AppliedAmount',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Payment','UnAppliedAmount',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Payment','PaymentDate',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Payment','PaymentMethod',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Payment','Reference',8);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Payment','EnteredBy',9);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Project','Name',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Project','Entity',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Project','Status',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Project','DueDate',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Promotion','title',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Promotion','description',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Promotion','startdate',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Promotion','enddate',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Promotion','nooforders',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Promotion','status',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Proposal','ProposalID',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Proposal','Title',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Proposal','Description',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Proposal','Type',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Proposal','Stage',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Proposal','Probability',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Proposal','Status',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Proposal','EstimatedCloseDate',8);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Proposal','ForecastAmmount',9);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Proposal','SalePersonID',10);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Proposal','SalePersonName',11);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('PurchaseOrder','PurchaseOrderID',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('PurchaseOrder','Created',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('PurchaseOrder','Creator',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('PurchaseOrder','Entity',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('PurchaseOrder','SubTotal',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('PurchaseOrder','Tax',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('PurchaseOrder','Total',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('PurchaseOrder','Status',8);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Rule','RuleName',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Rule','Description',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Rule','EnabledStatus',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('SavedSearch','SearchID',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('SavedSearch','SearchName',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('SavedSearch','Module',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('SavedSearch','Record',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('StandardReport','ReportID',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('StandardReport','Name',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('StandardReport','Description',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Task','Title',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Task','Project',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Task','Milestone',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Task','Owner',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Task','StartDate',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Task','DueDate',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Task','Complete',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Thread','Title',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Thread','Date',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Thread','Reference',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Thread','Priority',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Thread','CreatedBy',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Ticket','Subject',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Ticket','Entity',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Ticket','DateOpened',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Ticket','Status',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Ticket','DateClosed',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Ticket','AssignedTo',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Ticket','Category',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('TimeSheet','ID',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('TimeSheet','Employee',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('TimeSheet','StartDate',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('TimeSheet','EndDate',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('TimeSheet','Duration',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('TimeSheet','CreateBy',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Timeslip','ID',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Timeslip','Description',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Timeslip','Project',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Timeslip','Task',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Timeslip','Duration',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Timeslip','CreatedBy',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Timeslip','Date',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Timeslip','StartTime',8);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Timeslip','EndTime',9);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ToDo','Title',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ToDo','Created',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ToDo','Due',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ToDo','Priority',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ToDo','CreatedBy',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ToDo','Status',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('USER','UserID',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('USER','IndividualID',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('USER','Name',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('USER','Entity',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('USER','UserName',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('USER','Enabled',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('USER','Email',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Vendor','Name',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Vendor','PrimaryContact',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Vendor','Phone',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Vendor','Email',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Vendor','Website',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Vendor','Address',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('VolumeDiscount','DiscountID',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('VolumeDiscount','Description',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('VolumeDiscount','From',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('VolumeDiscount','To',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('VolumeDiscount','Discount',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('VolumeDiscount','Status',6);

--
-- Table structure for table 'listpreferences'
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
-- Dumping data for table 'listpreferences'
--


--
-- Table structure for table 'listtypes'
--

DROP TABLE IF EXISTS `listtypes`;
CREATE TABLE `listtypes` (
  `typename` varchar(30) NOT NULL default '',
  `moduleid` int(11) default NULL,
  PRIMARY KEY  (`typename`),
  KEY `moduleid` (`moduleid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'listtypes'
--

INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Address',1);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('BottomEntityTicket',1);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('BottomIndividual',1);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('BottomIndividualTicket',1);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('CustomField',1);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('MOC',1);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Email',2);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Rule',2);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('AllActivity',3);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('MultiActivity',3);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Note',5);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('File',6);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Competition',7);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Opportunity',7);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Proposal',7);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Event',8);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('EventAtendees',8);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('LiteratureFulfillment',8);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Marketing',8);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('MarketingListMembers',8);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Promotion',8);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Entity',14);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Individual',15);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Group',16);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('GroupMember',16);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Appointment',17);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Call',18);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('ForcastedSales',19);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Meeting',21);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('NextAction',22);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('ActivityTask',23);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('ToDo',23);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Tasks',24);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('BottomContacts',36);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('BottomFiles',36);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('BottomMultiActivity',36);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('BottomNotes',36);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('BottomTask',36);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('BottomTimeslip',36);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Project',36);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Ticket',36);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('FAQ',37);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Task',37);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Knowledgebase',38);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Timeslip',38);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Order',42);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Payment',43);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Expense',44);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('PurchaseOrder',45);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Item',46);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('GLAccount',47);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Inventory',48);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('VolumeDiscount',49);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Vendor',50);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Employee',54);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('InvoiceHistory',56);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('CustomFields',58);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('TimeSheet',60);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Expenses',61);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('EmployeeHandbook',62);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('AdHocReport',63);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('CustomReport',63);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('ReportResult',63);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('StandardReport',63);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('USER',65);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('SavedSearch',66);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('CustomView',67);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('SecurityProfile',68);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('CVAttic',69);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('CVGarbage',70);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('History',71);

--
-- Table structure for table 'listviews'
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
-- Dumping data for table 'listviews'
--

INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Entity',1,'Default View',NULL,NULL,'Name','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Individual',3,'Default View',NULL,NULL,'Name','D',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Group',4,'Default View',NULL,NULL,'Name','D',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('GroupMember',5,'Default View',NULL,NULL,'Name','D',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('AllActivity',6,'Default View',NULL,NULL,'Title','D',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Address',7,'Default View',NULL,NULL,'Street1','D',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Appointment',8,'Default View',NULL,NULL,'Title','D',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('BottomIndividual',9,'Default View',NULL,NULL,'Name','D',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Call',10,'Default View',NULL,NULL,'Title','D',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('CustomField',11,'Default View',NULL,NULL,'Field','D',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Email',12,'Default View',NULL,NULL,'Received','D',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('File',13,'Default View',NULL,NULL,'Name','D',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('ForcastedSales',14,'Default View',NULL,NULL,'Name','D',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Meeting',18,'Default View',NULL,NULL,'Title','D',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('MOC',19,'Default View',NULL,NULL,'Type','D',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('MultiActivity',20,'Default View',NULL,NULL,'Type','D',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('NextAction',21,'Default View',NULL,NULL,'Title','D',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Note',22,'Default View',NULL,NULL,'Title','D',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Rule',23,'Default View',NULL,NULL,'RuleName','D',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Tasks',24,'Default View',NULL,NULL,'Name','D',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('ToDo',25,'Default View',NULL,NULL,'Title','D',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Ticket',66,'Default View',NULL,NULL,'Subject','D',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('FAQ',70,'Default View',NULL,NULL,'Title','D',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Knowledgebase',71,'Default View',NULL,NULL,'Name','D',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('LiteratureFulfillment',72,'Default View',NULL,NULL,'title','A',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Event',73,'Default View',NULL,NULL,'eventid','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('GLAccount',74,'Default View',NULL,NULL,'Name','A',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Marketing',75,'Default View',NULL,NULL,'Description','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Promotion',76,'Default View',NULL,NULL,'title','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Item',77,'Default View',NULL,NULL,'SKU','A',29,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Inventory',79,'Default View',NULL,NULL,'InventoryID','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Order',81,'Default View',NULL,NULL,'OrderNO','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Payment',82,'Default View',NULL,NULL,'PaymentID','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Expense',83,'Default View',NULL,NULL,'ExpenseID','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('PurchaseOrder',84,'Default View',NULL,NULL,'PurchaseOrderID','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('VolumeDiscount',85,'Default View',NULL,NULL,'DiscountID','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Vendor',86,'Default View',NULL,NULL,'Name','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('InvoiceHistory',87,'Default View',NULL,NULL,'InvoiceID','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('BottomTask',88,'Default View',NULL,NULL,'Task','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('BottomTimeslip',89,'Default View',NULL,NULL,'Task','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Task',90,'Default View',NULL,NULL,'Title','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Project',91,'Default View',NULL,NULL,'Name','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Timeslip',92,'Default View',NULL,NULL,'ID','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Opportunity',93,'Default View',NULL,NULL,'Title','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Proposal',94,'Default View',NULL,NULL,'Title','A',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('ActivityTask',100,'Default View',NULL,NULL,'Title','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('BottomMultiActivity',101,'Default View',NULL,NULL,'Type','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('BottomContacts',104,'Default View',NULL,NULL,'Name','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('BottomFiles',105,'Default View',NULL,NULL,'Name','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('BottomNotes',106,'Default View',NULL,NULL,'Title','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('CustomFields',110,'Default View',NULL,NULL,'Name','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Employee',170,'Default View',NULL,NULL,'Name','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('TimeSheet',171,'Default View',NULL,NULL,'ID','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Expenses',172,'Default View',NULL,NULL,'ID','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('EmployeeHandbook',173,'Default View',NULL,NULL,'Name','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('USER',174,'Default User',NULL,NULL,'Name','A',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('SavedSearch',175,'Default Saved Search',NULL,NULL,'SearchName','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Employee',176,'uma_testv1',546,546,'Name','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('CustomView',177,'Default CustomView',NULL,NULL,'ViewName','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Order',178,'uma_v1',546,546,'OrderNO','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('SecurityProfile',179,'Default Security Profile',NULL,NULL,'ProfileName','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('CVAttic',180,'Default Attic',NULL,NULL,'Title','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('CVGarbage',181,'Default Garbage',NULL,NULL,'Title','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('History',182,'Default History',NULL,NULL,'Date','D',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('EventAtendees',183,'Default View',NULL,NULL,'individualname','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('MarketingListMembers',184,'Default View',NULL,NULL,'Entity','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('StandardReport',185,'Default View',NULL,NULL,'ReportID','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('AdHocReport',186,'Default View',NULL,NULL,'ReportID','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('CustomReport',187,'Default View',NULL,NULL,'ReportID','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('ReportResult',188,'Default View',NULL,NULL,'ReportID','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('MarketingListMembers',189,'Default View',NULL,NULL,'Entity','A',0,100,'A');

--
-- Table structure for table 'literature'
--

DROP TABLE IF EXISTS `literature`;
CREATE TABLE `literature` (
  `LiteratureID` int(10) unsigned NOT NULL default '0',
  `Title` varchar(25) NOT NULL default '',
  `FileID` int(10) unsigned NOT NULL default '0',
  `Description` text NOT NULL,
  KEY `LiteratureID` (`LiteratureID`),
  KEY `FileID` (`FileID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'literature'
--


--
-- Table structure for table 'literaturerequest'
--

DROP TABLE IF EXISTS `literaturerequest`;
CREATE TABLE `literaturerequest` (
  `ActivityID` int(10) unsigned NOT NULL default '0',
  `LiteratureID` int(10) unsigned NOT NULL default '0',
  `Title` varchar(25) NOT NULL default '',
  `Description` varchar(100) NOT NULL default '',
  `RequestedBy` int(10) unsigned NOT NULL default '0',
  `DeliveryMethod` int(10) unsigned NOT NULL default '0',
  KEY `ActivityID` (`ActivityID`),
  KEY `LiteratureID` (`LiteratureID`),
  KEY `RequestedBy` (`RequestedBy`)
) TYPE=InnoDB;

--
-- Dumping data for table 'literaturerequest'
--


--
-- Table structure for table 'location'
--

DROP TABLE IF EXISTS `location`;
CREATE TABLE `location` (
  `locationid` int(10) unsigned NOT NULL default '0',
  `title` varchar(25) default NULL,
  `parent` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`locationid`),
  KEY `parent` (`parent`)
) TYPE=InnoDB;

--
-- Dumping data for table 'location'
--


--
-- Table structure for table 'marketinglist'
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
-- Dumping data for table 'marketinglist'
--

INSERT INTO `marketinglist` (`ListID`, `Title`, `Description`, `NumberofRecords`, `Proposals`, `Orders`, `Ordervalue`, `Status`, `Owner`, `Creator`, `ModifiedBy`, `Created`, `Modified`) VALUES (1,'Default List','Do not delete this list.',0,0,0,0,'YES',1,1,1,NOW(),NOW());

--
-- Table structure for table 'mastertable'
--

DROP TABLE IF EXISTS `mastertable`;
CREATE TABLE `mastertable` (
  `Name` varchar(50) default NULL,
  `TableType` varchar(50) NOT NULL default ''
) TYPE=InnoDB;

--
-- Dumping data for table 'mastertable'
--

INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('accountingstatus','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('accountingterms','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('action','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('activity','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('activityaction','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('activitylink','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('activityportlet','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('activitypriority','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('activityresources','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('activitystatus','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('activitytype','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('address','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('addressrelate','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('addresstype','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('alert','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('alertpreference','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('applypayment','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('attachment','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('attendee','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('attendeestatus','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('attic','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('atticdata','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('calendarportlet','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('call','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('calltype','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('category','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('competition','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('contacttype','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('contentstatus','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('country','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('createfieldauthorisation','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('createrecordauthorisation','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('customfield','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('customfieldmultiple','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('customfieldscalar','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('customfieldvalue','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('cvfile','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('cvfilefolder','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('cvfolder','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('cvfolderlocation','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('cvjoin','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('cvorder','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('cvtable','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('dbase','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('defaultviews','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('deliverymethod','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('emailaccount','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('emailaction','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('emailcomposition','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('emailfolder','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('emailmessage','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('emailportlet','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('emailpreference','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('emailrecipient','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('emailrules','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('emailstore','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('employee','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('entity','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('event','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('eventregister','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('expense','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('expenseitem','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('faq','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('field','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('fieldauthorisation','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('finalmappings','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('folderlist','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('glaccount','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('glaccounttype','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('grouptbl','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('individual','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('inventory','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('inventorystatus','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('invoice','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('invoiceitems','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('item','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('itemcategory','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('itemtype','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('knowledgebase','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('knowledgebaselink','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('listcolumns','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('listpreferences','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('listtypes','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('listviews','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('literature','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('literaturerequest','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('location','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('marketinglist','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('member','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('methodofcontact','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('mocrelate','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('moctype','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('module','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('moduleauthorisation','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('modulepreference','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('newsportlet','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('note','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('opportunity','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('opportunitylink','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('opportunityportlet','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('orderitem','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('otherpreferences','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('userpreferencedefault','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('payment','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('paymentmethod','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('project','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('projectlink','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('projectstatus','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('promoitem','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('promotion','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('proposal','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('proposalitem','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('proposallink','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('purchaseorder','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('purchaseorderitem','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('question','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('record','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('recordauthorisation','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('recurexcept','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('recurrence','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('resourcerelate','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('role','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('salesprobability','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('salesstage','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('salesstatus','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('salestype','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('search','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('searchcriteria','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('searchexpression','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('source','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('state','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('supportportlet','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('supportpriority','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('supportstatus','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('syncmaster','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('task','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('taskassigned','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('taskportlet','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('taxclass','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('taxjurisdiction','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('license','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('taxmatrix','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('thread','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('ticket','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('ticketlink','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('timeentry','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('timeslip','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('user','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('vendor','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('viewcolumns','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('terms','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('userpreference','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('ptartifact','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('ptcategory','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('ptdetail','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('additionalmenu','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('timesheet','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('expenseform','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('suggestion','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('delegation','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('userrole','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('securityprofile','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('syncconfig','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('syncproperties','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('applicationsetting','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('individuallist','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('restoresequence','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('contacthistory','');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('historytype','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('authorizationsettings','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('mastertable','M');
INSERT INTO `mastertable` (`Name`, `TableType`) VALUES ('usersecurityprofile','');

--
-- Table structure for table 'member'
--

DROP TABLE IF EXISTS `member`;
CREATE TABLE `member` (
  `GroupID` int(10) unsigned NOT NULL default '0',
  `ChildID` int(10) unsigned default NULL,
  KEY `GroupID` (`GroupID`),
  KEY `ChildID` (`ChildID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'member'
--


--
-- Table structure for table 'methodofcontact'
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
-- Dumping data for table 'methodofcontact'
--


--
-- Table structure for table 'mocrelate'
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
-- Dumping data for table 'mocrelate'
--


--
-- Table structure for table 'moctype'
--

DROP TABLE IF EXISTS `moctype`;
CREATE TABLE `moctype` (
  `MOCTypeID` int(10) unsigned NOT NULL auto_increment,
  `Name` varchar(25) default NULL,
  PRIMARY KEY  (`MOCTypeID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'moctype'
--

INSERT INTO `moctype` (`MOCTypeID`, `Name`) VALUES (1,'Email');
INSERT INTO `moctype` (`MOCTypeID`, `Name`) VALUES (2,'Fax');
INSERT INTO `moctype` (`MOCTypeID`, `Name`) VALUES (3,'Mobile');
INSERT INTO `moctype` (`MOCTypeID`, `Name`) VALUES (4,'Phone');

--
-- Table structure for table 'module'
--

DROP TABLE IF EXISTS `module`;
CREATE TABLE `module` (
  `moduleid` int(10) unsigned NOT NULL auto_increment,
  `parentid` int(10) unsigned default NULL,
  `applyrights` tinyint(1) NOT NULL default '0',
  `hasfields` tinyint(1) NOT NULL default '0',
  `primarytable` varchar(50) NOT NULL default '',
  `ownerfield` varchar(50) NOT NULL default '',
  `primarykeyfield` varchar(50) NOT NULL default '',
  `otherownerfield` varchar(250) NOT NULL default '',
  `otherprimarytable` varchar(250) NOT NULL default '',
  `name` varchar(25) default NULL,
  PRIMARY KEY  (`moduleid`),
  KEY `parentid` (`parentid`)
) TYPE=InnoDB COMMENT='InnoDB free: 68608 kB; InnoDB free: 66560 kB';

--
-- Dumping data for table 'module'
--

INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (1,NULL,0,0,'','','','','','Contacts');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (2,NULL,0,0,'','','','','','Email');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (3,NULL,0,0,'activity','owner','activityid','','','Activities');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (4,NULL,0,0,'','','','','','Calendar');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (5,NULL,0,0,'note','Owner','NoteID','','','Notes');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (6,NULL,0,0,'cvfile','Owner','FileID','','','File');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (7,NULL,0,0,'','','','','','Sales');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (8,NULL,0,0,'','','','','','Marketing');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (9,NULL,0,0,'','','','','','Projects');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (10,NULL,0,0,'','','','','','Support');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (11,NULL,0,0,'','','','','','Authentication');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (12,NULL,0,0,'','','','','','Acounting');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (13,NULL,0,0,'','','','','','HumanResources');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (14,1,0,0,'entity','Owner','EntityID','','','Entity');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (15,1,0,0,'individual','Owner','IndividualID','','','Individual');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (16,1,0,0,'grouptbl','owner','GroupID','','','Group');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (17,3,0,0,'activity','owner','activityid','','','Appointments');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (18,3,0,0,'activity','owner','activityid','','','Calls');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (19,3,0,0,'','','','','','ForecastSales');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (20,3,0,0,'','','','','','LiteratureRequests');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (21,3,0,0,'activity','owner','activityid','','','Meetings');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (22,3,0,0,'activity','owner','activityid','','','NextActions');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (23,3,0,0,'activity','owner','activityid','','','ToDos');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (24,3,0,0,'','','','','','Tasks');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (25,4,0,0,'','','','','','DailyView');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (26,4,0,0,'','','','','','WeeklyView');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (27,4,0,0,'','','','','','WeeklyColumnar');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (28,4,0,0,'','','','','','MonthlyView');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (29,4,0,0,'','','','','','YearlyView');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (30,7,0,0,'opportunity o, activity a','owner','o.activityid = a.activityid and o.opportunityid','','','Opportunioties');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (31,7,0,0,'proposal','Owner','ProposalID','','','Proposals');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (32,8,0,0,'marketinglist','owner','ListID','','','ListManager');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (33,8,0,0,'','','','','','Promotion');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (34,8,0,0,'','','','','','LiteratureFulfilment');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (35,8,0,0,'','','','','','Events');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (36,9,0,0,'project','Owner','ProjectID','','','Projects');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (37,9,0,0,'task','','ActivityID','','','Tasks');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (38,9,0,0,'timeslip','CreatedBy','TimeSlipID','','','Time Slips');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (39,10,0,0,'ticket','owner','ticketid','','','Ticket');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (40,10,0,0,'faq','owner','faqid','','','FAQ');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (41,10,0,0,'knowledgebase','owner','kbid','','','Knowledgebase');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (42,12,0,0,'cvorder','owner','orderid','','','OrderHistory');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (43,12,0,0,'payment','owner','paymentid','','','Payment');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (44,12,0,0,'Expense','owner','expenseid','','','Expense');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (45,12,0,0,'purchaseorder','owner','purchaseorderid','','','PurchaseOrder');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (46,12,0,0,'item','createdby','itemid','','','Item');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (47,12,0,0,'glaccount','','glaccountsid','','','GLAccount');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (48,12,0,0,'inventory','','inventoryid','','','Inventory');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (49,12,0,0,'','','','','','VolumeDiscount');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (50,12,0,0,'','','','','','Vendor');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (51,13,0,0,'','','','','','ExpenseForms');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (52,13,0,0,'','','','','','Time Sheets');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (53,13,0,0,'','','','','','EmployeeHandbook');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (54,13,0,0,'','','','','','EmployeeList');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (55,13,0,0,'','','','','','SuggestionBox');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (56,12,0,0,'Invoice','owner','InvoiceId','','','InvoiceHistory');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (57,39,0,0,'','','','','','Thread');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (58,NULL,0,0,'','','','','','CustomFields');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (59,NULL,0,0,'','','','','','Print Templates');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (60,NULL,0,0,'','','','','','Help');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (61,NULL,0,0,'','','','','','Synchronize');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (62,NULL,0,0,'','','','','','Preferences');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (63,NULL,0,0,'','','','','','Reports');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (64,NULL,0,0,'','','','','','Administrator');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (65,64,0,0,'','','','','','User');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (66,64,0,0,'','','','','','SavedSearch');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (67,64,0,0,'','','','','','CustomView');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (68,64,0,0,'','','','','','SecurityProfile');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (69,64,0,0,'','','','','','Attic');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (70,64,0,0,'','','','','','Search');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (71,64,0,0,'','','','','','History');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (72,NULL,0,0,'additionalmenu','','menuitem_id','','','Additional');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (73,64,0,0,'literature','','LiteratureID','','','Literature');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (74,NULL,0,0,'promotion','','PromotionID','','','Promotion');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (76,6,0,0,'cvfolder','owner','FolderID','','','CVFolder');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (77,NULL,0,0,'event','','EventID','','','Event');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (78,NULL,0,0,'marketinglist','Owner','ListId','','','MarketingList');

--
-- Table structure for table 'moduleauthorisation'
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
-- Dumping data for table 'moduleauthorisation'
--

INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,1,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,2,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,3,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,4,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,5,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,6,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,7,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,8,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,9,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,10,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,11,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,12,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,13,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,14,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,15,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,16,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,17,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,18,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,19,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,20,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,21,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,22,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,23,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,24,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,25,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,26,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,27,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,28,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,29,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,30,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,31,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,32,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,33,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,34,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,35,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,37,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,36,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,38,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,39,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,40,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,41,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,42,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,43,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,44,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,45,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,46,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,47,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,48,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,49,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,50,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,51,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,52,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,53,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,54,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,55,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,56,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,59,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,60,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,61,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,62,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,63,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,64,1,10);
INSERT INTO `moduleauthorisation` (`individualid`, `moduleid`, `profileid`, `privilegelevel`) VALUES (NULL,72,1,10);

--
-- Table structure for table 'modulefieldmapping'
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
-- Dumping data for table 'modulefieldmapping'
--

INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (490,5,'detail',1,'','Detail',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,14,'id1',8,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (3,14,'name',3,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (14,15,'title',4,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,5,'entity',5,'','RelateEntity',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,5,'individual',6,'','RelateIndividual',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,14,'address',7,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,14,'id2',9,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,14,'website',10,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,14,'source',11,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,14,'acctmanager',12,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,14,'acctteam',13,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,14,'contactmethod',14,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,14,'customfield',15,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,15,'id1',16,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,15,'id2',17,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,15,'address',18,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,15,'source',19,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,15,'contactmethod',20,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,15,'customfield',21,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,6,'description',22,'','Description',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,6,'authorId',23,'','AuthorId',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,6,'version',24,'','Version',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,6,'entity',25,'','RelateEntity',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,6,'individual',26,'','RelateIndividual',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,36,'description',29,'0','Description',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,36,'entity',30,'0','Entity',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,36,'contact',31,'0','Contact',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,36,'manager',32,'0','Manager',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,36,'team',33,'0','Team',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,36,'start',34,'0','Start',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,36,'end',35,'0','End',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,36,'status',36,'0','Status',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,36,'budhr',37,'0','Budhr',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,36,'usedHours',38,'0','UsedHours',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,36,'available',39,'','Available',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,37,'description',42,'0','ActivityDetails',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,37,'milestone',43,'0','Milestone',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,37,'sendAlert',44,'0','SendAlert',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,37,'alertTypeAlert',45,'0','AlertTypeAlert',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,37,'alertTypeEmail',46,'0','AlertTypeEmail',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,37,'sendTo',47,'0','SendTo',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,37,'manager',48,'0','Manager',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,37,'status',49,'0','Status',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,37,'percentComplete',50,'0','PercentComplete',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,37,'assignedTo',51,'0','AssignedTo',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,38,'project',52,'0','Reference',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,38,'task',53,'0','TaskID',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,38,'ticket',54,'0','lookupList',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,39,'priority',55,'','Priority',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,39,'detail',56,'','Detail',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,39,'entityname',57,'','Entityname',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,39,'contact',58,'','Contact',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,39,'phone',59,'','Phone',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,39,'email',60,'','Email',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,39,'managername',61,'','Managername',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,39,'assignedto',62,'','Assignedto',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,39,'contact',63,'','Contact',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,35,'attachfile',64,'','Attachfile',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,18,'activityDetails',217,'','ActivityDetails',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,51,'notes',195,'','Notes',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,53,'description',201,'','Description',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,52,'description',198,'','Description',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,52,'notes',199,'','Notes',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,52,'status',200,'','Status',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,51,'linestatus',196,'','Linestatus',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,35,'description',76,'','Description',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,17,'activityDetails',203,'','ActivityDetails',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,32,'listdescription',79,'','Listdescription',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,51,'expenseId',197,'','ExpenseId',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,35,'masterlistid',81,'','Masterlistid',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,35,'moderatorname',82,'','Moderatorname',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,35,'maxattendees',83,'','Maxattendees',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,16,'description',191,'','Description',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,16,'owner',192,'','Owner',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,16,'memberlist',193,'','',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,51,'formDescription',194,'','FormDescription',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,31,'estimatedclosedate',109,'','Estimatedclose',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,31,'proposaltype',108,'','Typeid',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,42,'statusIdValue',95,'','StatusIdValue',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,56,'po',120,'','Po',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,31,'stage',107,'','Stage',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,31,'status',106,'','Statusid',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,42,'termsIdValue',99,'','TermsIdValue',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,42,'acctMgrIdValue',100,'','AcctMgrIdValue',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,42,'projectIdValue',101,'','ProjectIdValue',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,56,'statusId',121,'','StatusId',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,31,'description',105,'','Description',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,31,'probability',110,'','Probability',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'description',111,'','Description',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'source',112,'','SourceID',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'stage',113,'','StageID',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'status',114,'','StatusID',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'opportunitytype',115,'','OpportunityTypeID',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'probability',116,'','Probability',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'forcastedamount',117,'','ForcastedAmount',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'estimatedclosedate',118,'','EstimatedClose',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,34,'individualname',119,'','Individualname',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,56,'termId',122,'','TermId',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,56,'accountManagerId',123,'','AccountManagerId',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,56,'projectId',124,'','ProjectId',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'description',125,'','Description',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'sourcename',126,'','Sourcename',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,44,'expenseDescription',127,'','ExpenseDescription',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,44,'notes',128,'','Notes',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,44,'statusIDValue',129,'','StatusIDValue',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'statusname',130,'','Statusname',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'stagename',131,'','Stagename',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'opportunitytypename',132,'','Opportunitytypename',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'forecastedamount',133,'','Forecastedamount',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'probabilityname',134,'','Probabilityname',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'estimatedclose',135,'','Estimatedclose',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'actualclose',136,'','Actualclose',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'acctmgrname',137,'','Acctmgrname',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'acctteamname',138,'','Acctteamname',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,46,'itemDesc',139,'','ItemDesc',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,46,'glAccountId',140,'','GlAccountId',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,46,'taxClassId',141,'','TaxClassId',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,46,'subItemOfId',142,'','SubItemOfId',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,46,'price',143,'','price',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,46,'cost',144,'','Cost',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,46,'linkToInventory',145,'','LinkToInventory',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,46,'qtyOnHand',146,'','QtyOnHand',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,46,'qtyOnOrder',147,'','QtyOnOrder',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,46,'qtyOnBackOrder',148,'','QtyOnBackOrder',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,31,'prodescription',149,'','Prodescription',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,31,'billingaddress',150,'','Billingaddress',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,31,'shippingaddress',151,'','Shippingaddress',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,31,'statuslist',152,'','Statuslist',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,31,'stage',153,'','Stage',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,31,'proposaltype',154,'','Proposaltype',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,31,'probability',155,'','Probability',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,31,'estimatedclose',156,'','Estimatedclose',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,31,'actualclose',157,'','Actualclose',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,31,'terms',158,'','Terms',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,31,'item',159,'','Item',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,31,'specialinstructions',160,'','Specialinstructions',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,31,'attachFileValues',161,'','AttachFileValues',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,45,'statusId',162,'','StatusId',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,45,'purchaseorderDate',163,'','PurchaseorderDate',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,45,'termId',164,'','TermId',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,45,'accountManagerId',165,'','AccountManagerId',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,48,'strIdentifier',167,'','StrIdentifier',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,48,'intLocationID',168,'','IntLocationID',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,48,'manufacturerVO',169,'','ManufacturerVO',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,48,'vendorVO',170,'','VendorVO',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,48,'strDescription',171,'','StrDescription',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,48,'intStatusID',172,'','IntStatusID',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,48,'soldToVo',173,'','soldToVo',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,48,'customFieldsVec',174,'','CustomFieldsVec',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,33,'description',175,'','Description',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,33,'startdate',176,'','Startdate',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,33,'status',177,'','Status',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,33,'customfield',178,'','Customfield',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,33,'notes',179,'','Notes',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,33,'notes',180,'','Notes',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,33,'itemlines',181,'','ItemLines',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,34,'entityname',182,'','Entityname',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,34,'assignedtoname',183,'','Assignedtoname',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,34,'detail',184,'','Detail',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,34,'deliverymethodname',185,'','Deliverymethodname',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,34,'statusname',186,'','Statusname',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,43,'reference',187,'','Reference',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,43,'description',188,'','Description',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,34,'duebyday',189,'','Duebyday',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,34,'literaturename',190,'','Literaturename',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,17,'entityVO',204,'','EntityVO',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,17,'individualVO',205,'','IndividualVO',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,17,'opportunityID',206,'','OportunityID',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,17,'projectID',207,'','ProjectID',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,17,'status',208,'','Status',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,17,'notes',209,'','Notes',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,17,'activityStartDate',210,'','ActivityStartDate',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,17,'activityEndDate',211,'','ActivityEndDate',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,17,'owner',212,'','Owner',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,18,'entityVO',218,'','EntityVO',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,53,'authorname',215,'','Authorname',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,18,'individualVO',219,'','IndividualVO',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,18,'opportunityID',220,'','OpportunityID',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,18,'projectID',221,'','ProjectID',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,18,'owner',222,'','Owner',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,18,'status',223,'','Status',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,18,'notes',224,'','Notes',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,18,'activityStartDate',225,'','ActivityStartDate',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,18,'activityEndDate',226,'','ActivityEndDate',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,21,'activityDetails',227,'','ActivityDetails',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,21,'entityVO',228,'','EntityVO',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,21,'individualVO',229,'','IndividualVO',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,21,'opportunityID',230,'','OpportunityID',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,21,'projectID',231,'','ProjectID',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,21,'owner',232,'','Owner',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,21,'status',233,'','Status',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,21,'notes',234,'','Notes',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,22,'activityDetails',235,'','ActivityDetails',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,22,'entityVO',236,'','EntityVO',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,22,'individualVO',237,'','IndividualVO',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,22,'opportunityID',238,'','OpportunityID',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,22,'projectID',239,'','ProjectID',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,22,'owner',240,'','Owner',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,22,'status',241,'','Status',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,22,'notes',242,'','Notes',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,23,'activityDetails',243,'','ActivityDetails',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,23,'entityVO',244,'','EntityVO',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,23,'individualVO',245,'','IndividualVO',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,23,'opportunityID',246,'','OpportunityID',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,23,'projectID',247,'','ProjectID',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,23,'owner',248,'','Owner',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,23,'status',249,'','Status',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,23,'notes',250,'','Notes',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,53,'fileversion',251,'','Fileversion',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,53,'individualname',252,'','Individualname',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,53,'entityname',253,'','Entityname',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,14,'firstname',254,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,14,'middlename',255,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,14,'lastname',256,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,15,'entity',257,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,16,'groupname',258,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,16,'groupdescription',259,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,16,'owner',260,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,5,'title',261,'','Title',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,15,'firstname',262,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,15,'lastname',263,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,3,'type',264,'','Type',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,3,'priority',265,'','Priority',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,3,'status',266,'','Status',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,3,'title',267,'','Title',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,3,'duedate',268,'','DueDate',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,3,'completeddate',269,'','CompletedDate',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,3,'details',270,'','Details',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,3,'creator',271,'','Creator',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,3,'owner',272,'','Owner',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,3,'modifiedby',273,'','ModifiedBy',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,3,'modified',274,'','Modified',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,3,'created',275,'','Created',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,3,'start',276,'','Start',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,3,'end',277,'','End',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,3,'location',278,'','Location',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,3,'visibility',279,'','Visibility',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,3,'notes',280,'','Notes',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,3,'calltype',281,'','CallType',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,15,'middlename',282,'','MiddleName',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,46,'item',299,'','Item',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,42,'acctMgr',307,'','AcctMgr',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,42,'project',308,'','Project',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'entityid',309,'','Entityid',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'entityname',310,'','Entityname',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'opportunityid',311,'','Opportunityid',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'title',312,'','Title',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'activityid',313,'','Activityid',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'individualid',314,'','Individualid',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'individualname',315,'','Individualname',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'sourceid',316,'','Sourceid',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'statusid',317,'','Statusid',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'stageid',318,'','Stageid',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'opportunitytypeid',319,'','Opportunitytypeid',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'totalamount',320,'','Totalamount',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'probabilityid',321,'','Probabilityid',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'acctmgrid',322,'','Acctmgrid',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,30,'acctteamid',323,'','Acctteamid',1);

--
-- Table structure for table 'modulepreference'
--

DROP TABLE IF EXISTS `modulepreference`;
CREATE TABLE `modulepreference` (
  `individualid` int(11) unsigned default NULL,
  `moduleid` int(11) unsigned default NULL,
  KEY `moduleid` (`moduleid`),
  KEY `individualid` (`individualid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'modulepreference'
--


--
-- Table structure for table 'newsportlet'
--

DROP TABLE IF EXISTS `newsportlet`;
CREATE TABLE `newsportlet` (
  `individualid` int(11) unsigned default NULL,
  `visible` enum('YES','NO') default NULL,
  `days` int(11) default NULL,
  KEY `individualid` (`individualid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'newsportlet'
--


--
-- Table structure for table 'note'
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
-- Dumping data for table 'note'
--


--
-- Table structure for table 'opportunity'
--

DROP TABLE IF EXISTS `opportunity`;
CREATE TABLE `opportunity` (
  `OpportunityID` int(11) unsigned NOT NULL auto_increment,
  `ActivityID` int(11) unsigned NOT NULL default '0',
  `Title` varchar(25) NOT NULL default '',
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
-- Dumping data for table 'opportunity'
--


--
-- Table structure for table 'opportunitylink'
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
-- Dumping data for table 'opportunitylink'
--


--
-- Table structure for table 'opportunityportlet'
--

DROP TABLE IF EXISTS `opportunityportlet`;
CREATE TABLE `opportunityportlet` (
  `individualid` int(11) unsigned default NULL,
  `visible` enum('YES','NO') default NULL,
  KEY `individualid` (`individualid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'opportunityportlet'
--


--
-- Table structure for table 'orderitem'
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
  PRIMARY KEY  (`lineid`),
  KEY `orderid` (`orderid`),
  KEY `itemid` (`itemid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'orderitem'
--


--
-- Table structure for table 'otherpreferences'
--

DROP TABLE IF EXISTS `otherpreferences`;
CREATE TABLE `otherpreferences` (
  `PreferenceName` varchar(100) NOT NULL default '',
  `PreferenceValue` varchar(100) NOT NULL default '',
  `IndividualID` int(11) unsigned NOT NULL default '0',
  KEY `IndividualID` (`IndividualID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'otherpreferences'
--

INSERT INTO `otherpreferences` (`PreferenceName`, `PreferenceValue`, `IndividualID`) VALUES ('DEFAULTFOLDERID','8',1);

--
-- Table structure for table 'payment'
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
  `Modified` timestamp(14) NOT NULL,
  PRIMARY KEY  (`PaymentID`),
  KEY `EntityID` (`EntityID`),
  KEY `PaymentMethod` (`PaymentMethod`),
  KEY `Owner` (`Owner`),
  KEY `Creator` (`Creator`),
  KEY `ModifiedBy` (`ModifiedBy`),
  KEY `LineID` (`LineID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'payment'
--


--
-- Table structure for table 'paymentmethod'
--

DROP TABLE IF EXISTS `paymentmethod`;
CREATE TABLE `paymentmethod` (
  `MethodID` int(10) unsigned NOT NULL auto_increment,
  `externalID` varchar(25) default '[NULL]',
  `Title` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`MethodID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'paymentmethod'
--

INSERT INTO `paymentmethod` (`MethodID`, `externalID`, `Title`) VALUES (1,'[NULL]','Credit Card');
INSERT INTO `paymentmethod` (`MethodID`, `externalID`, `Title`) VALUES (2,'[NULL]','Cash');
INSERT INTO `paymentmethod` (`MethodID`, `externalID`, `Title`) VALUES (3,'[NULL]','Check');

--
-- Table structure for table 'project'
--

DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `ProjectID` int(11) unsigned NOT NULL auto_increment,
  `ProjectTitle` varchar(25) default NULL,
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
-- Dumping data for table 'project'
--


--
-- Table structure for table 'projectlink'
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
-- Dumping data for table 'projectlink'
--


--
-- Table structure for table 'projectstatus'
--

DROP TABLE IF EXISTS `projectstatus`;
CREATE TABLE `projectstatus` (
  `StatusID` int(11) unsigned NOT NULL auto_increment,
  `Title` varchar(25) default NULL,
  `Description` text,
  PRIMARY KEY  (`StatusID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'projectstatus'
--

INSERT INTO `projectstatus` (`StatusID`, `Title`, `Description`) VALUES (1,'Open',NULL);
INSERT INTO `projectstatus` (`StatusID`, `Title`, `Description`) VALUES (2,'Closed',NULL);

--
-- Table structure for table 'promoitem'
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
-- Dumping data for table 'promoitem'
--


--
-- Table structure for table 'promotion'
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
-- Dumping data for table 'promotion'
--


--
-- Table structure for table 'proposal'
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
-- Dumping data for table 'proposal'
--


--
-- Table structure for table 'proposalitem'
--

DROP TABLE IF EXISTS `proposalitem`;
CREATE TABLE `proposalitem` (
  `Name` varchar(25) default NULL,
  `ProposalID` int(11) default NULL,
  `ProposalLineID` int(11) unsigned NOT NULL auto_increment,
  `ItemID` int(11) unsigned default NULL,
  `Description` text,
  `Quantity` float(11,0) unsigned default NULL,
  `Price` float(9,3) default NULL,
  `SKU` varchar(50) NOT NULL default '',
  `Tax Rate` float NOT NULL default '0',
  `Tax` int(11) NOT NULL default '0',
  `status` enum('Active','Deleted') NOT NULL default 'Active',
  `TransactionID` int(11) NOT NULL default '0',
  PRIMARY KEY  (`ProposalLineID`),
  KEY `ProposalID` (`ProposalID`),
  KEY `ItemID` (`ItemID`),
  KEY `Tax` (`Tax`)
) TYPE=InnoDB;

--
-- Dumping data for table 'proposalitem'
--


--
-- Table structure for table 'proposallink'
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
-- Dumping data for table 'proposallink'
--


--
-- Table structure for table 'ptartifact'
--

DROP TABLE IF EXISTS `ptartifact`;
CREATE TABLE `ptartifact` (
  `artifactid` int(10) unsigned NOT NULL auto_increment,
  `artifactname` char(25) NOT NULL default '',
  PRIMARY KEY  (`artifactid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'ptartifact'
--

INSERT INTO `ptartifact` (`artifactid`, `artifactname`) VALUES (1,'file');
INSERT INTO `ptartifact` (`artifactid`, `artifactname`) VALUES (2,'entity');
INSERT INTO `ptartifact` (`artifactid`, `artifactname`) VALUES (3,'Individual');

--
-- Table structure for table 'ptcategory'
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
-- Dumping data for table 'ptcategory'
--

INSERT INTO `ptcategory` (`ptcategoryid`, `catname`, `artifactid`, `parentid`, `cattype`) VALUES (1,'File',1,4,'PRINT');
INSERT INTO `ptcategory` (`ptcategoryid`, `catname`, `artifactid`, `parentid`, `cattype`) VALUES (2,'Email',2,4,'EMAIL');
INSERT INTO `ptcategory` (`ptcategoryid`, `catname`, `artifactid`, `parentid`, `cattype`) VALUES (3,'Proposal',2,4,'PRINT');

--
-- Table structure for table 'ptdetail'
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
-- Dumping data for table 'ptdetail'
--

--
-- Table structure for table 'purchaseorder'
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
  `Created` timestamp(14) NOT NULL,
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
-- Dumping data for table 'purchaseorder'
--


--
-- Table structure for table 'purchaseorderitem'
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
-- Dumping data for table 'purchaseorderitem'
--


--
-- Table structure for table 'question'
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
-- Dumping data for table 'question'
--


--
-- Table structure for table 'recordauthorisation'
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
-- Dumping data for table 'recordauthorisation'
--


--
-- Table structure for table 'recurexcept'
--

DROP TABLE IF EXISTS `recurexcept`;
CREATE TABLE `recurexcept` (
  `RecurrenceID` int(11) unsigned NOT NULL default '0',
  `Exception` date NOT NULL default '0000-00-00',
  KEY `RecurrenceID` (`RecurrenceID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'recurexcept'
--


--
-- Table structure for table 'recurrence'
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
-- Dumping data for table 'recurrence'
--


--
-- Table structure for table 'report'
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
  CONSTRAINT `0_4998` FOREIGN KEY (`ReportTypeId`) REFERENCES `reporttype` (`ReportTypeId`),
  CONSTRAINT `0_4996` FOREIGN KEY (`ModuleId`) REFERENCES `module` (`moduleid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'report'
--
INSERT INTO `report` (`ReportId`, `ModuleId`,`Name`,`Description`,`CreatedBy`,`ReportTypeId`) VALUES (100,1,'Contacts by state','Contacts by state, order by State, Zip Code, City',1,1);
INSERT INTO `report` (`ReportId`, `ModuleId`,`Name`,`Description`,`CreatedBy`,`ReportTypeId`) VALUES (101,1,'Contacts with custom fields list','Contacts with custom fields list by account manager, order by Account Manager, Entity',1,1);
INSERT INTO `report` (`ReportId`, `ModuleId`,`Name`,`Description`,`CreatedBy`,`ReportTypeId`) VALUES (102,13,'Expenses by user','Expenses by user, order by User, Expense Type',1,1);
INSERT INTO `report` (`ReportId`, `ModuleId`,`Name`,`Description`,`CreatedBy`,`ReportTypeId`) VALUES (103,13,'Expenses by entity','Expenses by user, order by User, Expense Type',1,1);
INSERT INTO `report` (`ReportId`, `ModuleId`,`Name`,`Description`,`CreatedBy`,`ReportTypeId`) VALUES (104,13,'Timeslips by user','Timeslips by user, order by User, Entity',1,1);
INSERT INTO `report` (`ReportId`, `ModuleId`,`Name`,`Description`,`CreatedBy`,`ReportTypeId`) VALUES (105,13,'Timeslips by Entity','Timeslips by user, order by Entity, User',1,1);
INSERT INTO `report` (`ReportId`, `ModuleId`,`Name`,`Description`,`CreatedBy`,`ReportTypeId`) VALUES (106,3,'Activities by user','Activities by user, order by User',1,1);
INSERT INTO `report` (`ReportId`, `ModuleId`,`Name`,`Description`,`CreatedBy`,`ReportTypeId`) VALUES (107,3,'Activities by entity','Activities by entity, order by Entity',1,1);
INSERT INTO `report` (`ReportId`, `ModuleId`,`Name`,`Description`,`CreatedBy`,`ReportTypeId`) VALUES (108,7,'Opportunitiess by user','Opportunities by user, order by Entity, Account Manager',1,1);
INSERT INTO `report` (`ReportId`, `ModuleId`,`Name`,`Description`,`CreatedBy`,`ReportTypeId`) VALUES (109,7,'Proposal by user','Proposal by user, order by Entity, Account Manager',1,1);
INSERT INTO `report` (`ReportId`, `ModuleId`,`Name`,`Description`,`CreatedBy`,`ReportTypeId`) VALUES (110,7,'Proposal detail by user','Proposal detail by user, order by Entity, Account Manager',1,1);
INSERT INTO `report` (`ReportId`, `ModuleId`,`Name`,`Description`,`CreatedBy`,`ReportTypeId`) VALUES (111,7,'Sales order report','Sales order report, order by Entity, Account Manager',1,1);
INSERT INTO `report` (`ReportId`, `ModuleId`,`Name`,`Description`,`CreatedBy`,`ReportTypeId`) VALUES (112,9,'Project summary','Project summary',1,1);
INSERT INTO `report` (`ReportId`, `ModuleId`,`Name`,`Description`,`CreatedBy`,`ReportTypeId`) VALUES (114,10,'Support tickets per user','Support tickets per user',1,1);
INSERT INTO `report` (`ReportId`, `ModuleId`,`Name`,`Description`,`CreatedBy`,`ReportTypeId`) VALUES (115,10,'Support tickets by entity','Support tickets by entity',1,1);
INSERT INTO `report` (`ReportId`, `ModuleId`,`Name`,`Description`,`CreatedBy`,`ReportTypeId`) VALUES (116,10,'Support tickets list','Support tickets list',1,1);

--
-- Table structure for table 'reportcontent'
--

DROP TABLE IF EXISTS `reportcontent`;
CREATE TABLE `reportcontent` (
  `ReportContentId` int(10) unsigned NOT NULL auto_increment,
  `ReportId` int(10) unsigned NOT NULL default '0',
  `FieldId` int(11) unsigned NOT NULL default '0',
  `SequenceNumber` tinyint(4) NOT NULL default '0',
  `SortOrder` enum('ASC','DESC') default NULL,
  `SortOrderSequence` tinyint(4) default NULL,
  PRIMARY KEY  (`ReportContentId`),
  KEY `ReportId` (`ReportId`),
  KEY `FieldId` (`FieldId`),
  CONSTRAINT `0_5002` FOREIGN KEY (`FieldId`) REFERENCES `field` (`fieldid`),
  CONSTRAINT `0_5000` FOREIGN KEY (`ReportId`) REFERENCES `report` (`ReportId`) ON DELETE CASCADE
) TYPE=InnoDB;

--
-- Dumping data for table 'reportcontent'
--


--
-- Table structure for table 'reportsearchcriteria'
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
  CONSTRAINT `0_5008` FOREIGN KEY (`ExpressionId`) REFERENCES `reportsearchexpression` (`ExpressionId`) ON DELETE CASCADE,
  CONSTRAINT `0_5004` FOREIGN KEY (`ReportId`) REFERENCES `report` (`ReportId`) ON DELETE CASCADE,
  CONSTRAINT `0_5006` FOREIGN KEY (`FieldId`) REFERENCES `field` (`fieldid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'reportsearchcriteria'
--


--
-- Table structure for table 'reportsearchexpression'
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
-- Dumping data for table 'reportsearchexpression'
--

INSERT INTO `reportsearchexpression` (`ExpressionId`, `Label`, `Clause`, `PreValue`, `PostValue`) VALUES (1,'begins with','like','\'','%\'');
INSERT INTO `reportsearchexpression` (`ExpressionId`, `Label`, `Clause`, `PreValue`, `PostValue`) VALUES (2,'ends with','like','\'%','\'');
INSERT INTO `reportsearchexpression` (`ExpressionId`, `Label`, `Clause`, `PreValue`, `PostValue`) VALUES (3,'contains','like','\'%','%\'');
INSERT INTO `reportsearchexpression` (`ExpressionId`, `Label`, `Clause`, `PreValue`, `PostValue`) VALUES (4,'equals','=','','');
INSERT INTO `reportsearchexpression` (`ExpressionId`, `Label`, `Clause`, `PreValue`, `PostValue`) VALUES (5,'less than','<','','');
INSERT INTO `reportsearchexpression` (`ExpressionId`, `Label`, `Clause`, `PreValue`, `PostValue`) VALUES (6,'greater than','>','','');
INSERT INTO `reportsearchexpression` (`ExpressionId`, `Label`, `Clause`, `PreValue`, `PostValue`) VALUES (7,'less than equals','<=','','');
INSERT INTO `reportsearchexpression` (`ExpressionId`, `Label`, `Clause`, `PreValue`, `PostValue`) VALUES (8,'greater than equals','>=','','');

--
-- Table structure for table 'reporttype'
--

DROP TABLE IF EXISTS `reporttype`;
CREATE TABLE `reporttype` (
  `ReportTypeId` int(4) NOT NULL auto_increment,
  `ReportTypeName` varchar(20) NOT NULL default '',
  PRIMARY KEY  (`ReportTypeId`),
  UNIQUE KEY `ReportTypeName` (`ReportTypeName`)
) TYPE=InnoDB;

--
-- Dumping data for table 'reporttype'
--

INSERT INTO `reporttype` (`ReportTypeId`, `ReportTypeName`) VALUES (2,'Ad Hoc');
INSERT INTO `reporttype` (`ReportTypeId`, `ReportTypeName`) VALUES (3,'Custom');
INSERT INTO `reporttype` (`ReportTypeId`, `ReportTypeName`) VALUES (1,'Standard');

--
-- Table structure for table 'resourcerelate'
--

DROP TABLE IF EXISTS `resourcerelate`;
CREATE TABLE `resourcerelate` (
  `ActivityID` int(11) unsigned default NULL,
  `ResourceID` int(11) unsigned default NULL,
  KEY `ActivityID` (`ActivityID`),
  KEY `ResourceID` (`ResourceID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'resourcerelate'
--


--
-- Table structure for table 'restoresequence'
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
-- Dumping data for table 'restoresequence'
--

INSERT INTO restoresequence (moduleid, tableid, sequence, primaryTable) VALUES (5,111,1,111);
INSERT INTO restoresequence (moduleid, tableid, sequence, primaryTable) VALUES (6,34,2,33);
INSERT INTO restoresequence (moduleid, tableid, sequence, primaryTable) VALUES (6,33,1,33);
INSERT INTO restoresequence (moduleid, tableid, sequence, primaryTable) VALUES (6,36,1,36);
INSERT INTO restoresequence (moduleid, tableid, sequence, primaryTable) VALUES (6,34,2,36);
INSERT INTO restoresequence (moduleid, tableid, sequence, primaryTable) VALUES (6,33,3,36);
INSERT INTO restoresequence (moduleid, tableid, sequence, primaryTable) VALUES (1,10,3,1);
INSERT INTO restoresequence (moduleid, tableid, sequence, primaryTable) VALUES (1,1,1,1);
INSERT INTO restoresequence (moduleid, tableid, sequence, primaryTable) VALUES (1,4,4,1);
INSERT INTO restoresequence (moduleid, tableid, sequence, primaryTable) VALUES (1,2,1,2);
INSERT INTO restoresequence (moduleid, tableid, sequence, primaryTable) VALUES (1,11,2,2);
INSERT INTO restoresequence (moduleid, tableid, sequence, primaryTable) VALUES (1,10,3,2);
INSERT INTO restoresequence (moduleid, tableid, sequence, primaryTable) VALUES (1,4,4,2);
INSERT INTO restoresequence (moduleid, tableid, sequence, primaryTable) VALUES (1,11,2,1);
INSERT INTO restoresequence (moduleid, tableid, sequence, primaryTable) VALUES (1, 12, 5, 2);
INSERT INTO restoresequence (moduleid, tableid, sequence, primaryTable) VALUES (1, 12, 5, 1); 


--
-- Table structure for table 'role'
--

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `roleid` int(10) NOT NULL auto_increment,
  `rolename` varchar(20) NOT NULL default '',
  PRIMARY KEY  (`roleid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'role'
--


--
-- Table structure for table 'salesprobability'
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
-- Dumping data for table 'salesprobability'
--

INSERT INTO `salesprobability` (`Probability`, `ProbabilityID`, `Title`, `Description`) VALUES (10,1,' 10%',NULL);
INSERT INTO `salesprobability` (`Probability`, `ProbabilityID`, `Title`, `Description`) VALUES (20,2,' 20%',NULL);
INSERT INTO `salesprobability` (`Probability`, `ProbabilityID`, `Title`, `Description`) VALUES (25,3,' 25%',NULL);
INSERT INTO `salesprobability` (`Probability`, `ProbabilityID`, `Title`, `Description`) VALUES (30,4,' 30%',NULL);
INSERT INTO `salesprobability` (`Probability`, `ProbabilityID`, `Title`, `Description`) VALUES (40,5,' 40%','');
INSERT INTO `salesprobability` (`Probability`, `ProbabilityID`, `Title`, `Description`) VALUES (50,6,' 50%','');
INSERT INTO `salesprobability` (`Probability`, `ProbabilityID`, `Title`, `Description`) VALUES (60,7,' 60%','');
INSERT INTO `salesprobability` (`Probability`, `ProbabilityID`, `Title`, `Description`) VALUES (70,8,' 70%','');
INSERT INTO `salesprobability` (`Probability`, `ProbabilityID`, `Title`, `Description`) VALUES (75,9,' 75%','');
INSERT INTO `salesprobability` (`Probability`, `ProbabilityID`, `Title`, `Description`) VALUES (80,10,' 80%','');
INSERT INTO `salesprobability` (`Probability`, `ProbabilityID`, `Title`, `Description`) VALUES (90,11,' 90%','');
INSERT INTO `salesprobability` (`Probability`, `ProbabilityID`, `Title`, `Description`) VALUES (100,12,'100%','');

--
-- Table structure for table 'salesstage'
--

DROP TABLE IF EXISTS `salesstage`;
CREATE TABLE `salesstage` (
  `SalesStageID` int(11) NOT NULL auto_increment,
  `Name` varchar(25) default NULL,
  PRIMARY KEY  (`SalesStageID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'salesstage'
--

INSERT INTO `salesstage` (`SalesStageID`, `Name`) VALUES (1,'Approach');
INSERT INTO `salesstage` (`SalesStageID`, `Name`) VALUES (2,'Needs Analysis');
INSERT INTO `salesstage` (`SalesStageID`, `Name`) VALUES (3,'Demo');
INSERT INTO `salesstage` (`SalesStageID`, `Name`) VALUES (4,'Proposal');
INSERT INTO `salesstage` (`SalesStageID`, `Name`) VALUES (5,'Trial Close');
INSERT INTO `salesstage` (`SalesStageID`, `Name`) VALUES (6,'Verbal Agreement');
INSERT INTO `salesstage` (`SalesStageID`, `Name`) VALUES (7,'Closed');

--
-- Table structure for table 'salesstatus'
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
-- Dumping data for table 'salesstatus'
--

-- 0 value will eventually go.
INSERT INTO `salesstatus` (`SalesStatusID`, `Name`, `ActivityStatusID`) VALUES (6,'',0);
UPDATE salesstatus SET SalesStatusID=0 WHERE SalesStatusID=6;
INSERT INTO `salesstatus` (`SalesStatusID`, `Name`, `ActivityStatusID`) VALUES (1,'Pending',1);
INSERT INTO `salesstatus` (`SalesStatusID`, `Name`, `ActivityStatusID`) VALUES (2,'Won',2);
INSERT INTO `salesstatus` (`SalesStatusID`, `Name`, `ActivityStatusID`) VALUES (3,'Lost',3);
INSERT INTO `salesstatus` (`SalesStatusID`, `Name`, `ActivityStatusID`) VALUES (4,'On Hold',4);
INSERT INTO `salesstatus` (`SalesStatusID`, `Name`, `ActivityStatusID`) VALUES (5,'Cancelled',5);

--
-- Table structure for table 'salestype'
--

DROP TABLE IF EXISTS `salestype`;
CREATE TABLE `salestype` (
  `SalesTypeID` int(11) NOT NULL auto_increment,
  `Name` varchar(25) default NULL,
  PRIMARY KEY  (`SalesTypeID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'salestype'
--

INSERT INTO `salestype` (`SalesTypeID`, `Name`) VALUES (1,'Software');
INSERT INTO `salestype` (`SalesTypeID`, `Name`) VALUES (2,'Hardware');
INSERT INTO `salestype` (`SalesTypeID`, `Name`) VALUES (3,'Service');

--
-- Table structure for table 'search'
--

DROP TABLE IF EXISTS `search`;
CREATE TABLE `search` (
  `searchname` varchar(100) NOT NULL default '',
  `searchstring` varchar(255) NOT NULL default '',
  `tableid` int(10) unsigned NOT NULL default '0',
  `searchid` int(10) unsigned NOT NULL auto_increment,
  `moduleid` int(10) unsigned default NULL,
  `owner` int(10) unsigned default NULL,
  `createdby` int(10) unsigned default NULL,
  `created` datetime default NULL,
  `modified` timestamp(14) NOT NULL,
  `modifiedby` int(10) unsigned default NULL,
  PRIMARY KEY  (`searchid`),
  KEY `tableid` (`tableid`),
  KEY `moduleid` (`moduleid`),
  KEY `owner` (`owner`),
  KEY `createdby` (`createdby`),
  KEY `modifiedby` (`modifiedby`)
) TYPE=InnoDB;

--
-- Dumping data for table 'search'
--


--
-- Table structure for table 'searchcriteria'
--

DROP TABLE IF EXISTS `searchcriteria`;
CREATE TABLE `searchcriteria` (
  `searchcriteriaid` int(10) unsigned NOT NULL auto_increment,
  `recordid` int(10) unsigned default NULL,
  `fieldid` int(10) unsigned default NULL,
  `expressionid` int(10) unsigned default NULL,
  `value` varchar(25) default NULL,
  PRIMARY KEY  (`searchcriteriaid`),
  KEY `recordid` (`recordid`),
  KEY `fieldid` (`fieldid`),
  KEY `expressionid` (`expressionid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'searchcriteria'
--


--
-- Table structure for table 'searchexpression'
--

DROP TABLE IF EXISTS `searchexpression`;
CREATE TABLE `searchexpression` (
  `expressionid` int(10) unsigned NOT NULL auto_increment,
  `label` varchar(25) default NULL,
  PRIMARY KEY  (`expressionid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'searchexpression'
--


--
-- Table structure for table 'securityprofile'
--

DROP TABLE IF EXISTS `securityprofile`;
CREATE TABLE `securityprofile` (
  `profileid` int(11) unsigned NOT NULL auto_increment,
  `profilename` varchar(100) NOT NULL default '',
  PRIMARY KEY  (`profileid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'securityprofile'
--

INSERT INTO `securityprofile` (`profileid`, `profilename`) VALUES (1,'Full Access');
INSERT INTO `securityprofile` (`profileid`, `profilename`) VALUES (2,'Empty Profile');

--
-- Table structure for table 'serversettings'
--

DROP TABLE IF EXISTS `serversettings`;
CREATE TABLE `serversettings` (
  `serversettingid` int(10) unsigned NOT NULL auto_increment,
  `hostname` varchar(50) NOT NULL default '',
  `sessiontimeout` int(22) default 30,
  `workinghoursfrom` time default NULL,
  `workinghoursto` time default NULL,
  `emailcheckinterval` int(22) default 10,
  `adminemailaddress` varchar(50) NOT NULL default '',
  `filesystemstoragepath` varchar(50) NOT NULL default '',
  `defaulttimezone` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`serversettingid`)
) TYPE=InnoDB;

INSERT INTO `serversettings` (`hostname`, `sessiontimeout`, `workinghoursfrom`, `workinghoursto`, 
  `emailcheckinterval`, `adminemailaddress`, `filesystemstoragepath`, `defaulttimezone`)
  VALUES ('', 120, '09:00:00', '17:00:00', 10, '', '', 'EST');

--
-- Table structure for table 'source'
--

DROP TABLE IF EXISTS `source`;
CREATE TABLE `source` (
  `SourceID` int(10) NOT NULL auto_increment,
  `Name` varchar(25) default NULL,
  PRIMARY KEY  (`SourceID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'source'
--

INSERT INTO `source` (`SourceID`, `Name`) VALUES (7,'');
UPDATE source SET SourceID=0 WHERE SourceID=7;
INSERT INTO `source` (`SourceID`, `Name`) VALUES (1,'Trade Show');
INSERT INTO `source` (`SourceID`, `Name`) VALUES (2,'Phone Call');
INSERT INTO `source` (`SourceID`, `Name`) VALUES (3,'Press Conference');
INSERT INTO `source` (`SourceID`, `Name`) VALUES (4,'Website');
INSERT INTO `source` (`SourceID`, `Name`) VALUES (5,'Cold Call');
INSERT INTO `source` (`SourceID`, `Name`) VALUES (6,'Walk-in');

--
-- Table structure for table 'state'
--

DROP TABLE IF EXISTS `state`;
CREATE TABLE `state` (
  `StateID` int(11) NOT NULL auto_increment,
  `Name` varchar(50) default NULL,
  PRIMARY KEY  (`StateID`),
  UNIQUE KEY `stateID` (`StateID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'state'
--

INSERT INTO `state` (`StateID`, `Name`) VALUES (52,'');
UPDATE state SET StateID=0 WHERE StateID=52;
INSERT INTO `state` (`StateID`, `Name`) VALUES (1,'AL');
INSERT INTO `state` (`StateID`, `Name`) VALUES (2,'AK');
INSERT INTO `state` (`StateID`, `Name`) VALUES (3,'AZ');
INSERT INTO `state` (`StateID`, `Name`) VALUES (4,'AR');
INSERT INTO `state` (`StateID`, `Name`) VALUES (5,'CA');
INSERT INTO `state` (`StateID`, `Name`) VALUES (6,'CO');
INSERT INTO `state` (`StateID`, `Name`) VALUES (7,'CT');
INSERT INTO `state` (`StateID`, `Name`) VALUES (8,'DE');
INSERT INTO `state` (`StateID`, `Name`) VALUES (9,'DC');
INSERT INTO `state` (`StateID`, `Name`) VALUES (10,'FL');
INSERT INTO `state` (`StateID`, `Name`) VALUES (11,'GA');
INSERT INTO `state` (`StateID`, `Name`) VALUES (12,'HI');
INSERT INTO `state` (`StateID`, `Name`) VALUES (13,'ID');
INSERT INTO `state` (`StateID`, `Name`) VALUES (14,'IL');
INSERT INTO `state` (`StateID`, `Name`) VALUES (15,'IN');
INSERT INTO `state` (`StateID`, `Name`) VALUES (16,'IA');
INSERT INTO `state` (`StateID`, `Name`) VALUES (17,'KS');
INSERT INTO `state` (`StateID`, `Name`) VALUES (18,'KY');
INSERT INTO `state` (`StateID`, `Name`) VALUES (19,'LA');
INSERT INTO `state` (`StateID`, `Name`) VALUES (20,'ME');
INSERT INTO `state` (`StateID`, `Name`) VALUES (21,'MD');
INSERT INTO `state` (`StateID`, `Name`) VALUES (22,'MA');
INSERT INTO `state` (`StateID`, `Name`) VALUES (23,'MI');
INSERT INTO `state` (`StateID`, `Name`) VALUES (24,'MN');
INSERT INTO `state` (`StateID`, `Name`) VALUES (25,'MS');
INSERT INTO `state` (`StateID`, `Name`) VALUES (26,'MO');
INSERT INTO `state` (`StateID`, `Name`) VALUES (27,'MT');
INSERT INTO `state` (`StateID`, `Name`) VALUES (28,'NE');
INSERT INTO `state` (`StateID`, `Name`) VALUES (29,'NV');
INSERT INTO `state` (`StateID`, `Name`) VALUES (30,'NH');
INSERT INTO `state` (`StateID`, `Name`) VALUES (31,'NJ');
INSERT INTO `state` (`StateID`, `Name`) VALUES (32,'NM');
INSERT INTO `state` (`StateID`, `Name`) VALUES (33,'NY');
INSERT INTO `state` (`StateID`, `Name`) VALUES (34,'NC');
INSERT INTO `state` (`StateID`, `Name`) VALUES (35,'ND');
INSERT INTO `state` (`StateID`, `Name`) VALUES (36,'OH');
INSERT INTO `state` (`StateID`, `Name`) VALUES (37,'OK');
INSERT INTO `state` (`StateID`, `Name`) VALUES (38,'OR');
INSERT INTO `state` (`StateID`, `Name`) VALUES (39,'PA');
INSERT INTO `state` (`StateID`, `Name`) VALUES (40,'RI');
INSERT INTO `state` (`StateID`, `Name`) VALUES (41,'SC');
INSERT INTO `state` (`StateID`, `Name`) VALUES (42,'SD');
INSERT INTO `state` (`StateID`, `Name`) VALUES (43,'TN');
INSERT INTO `state` (`StateID`, `Name`) VALUES (44,'TX');
INSERT INTO `state` (`StateID`, `Name`) VALUES (45,'UT');
INSERT INTO `state` (`StateID`, `Name`) VALUES (46,'VT');
INSERT INTO `state` (`StateID`, `Name`) VALUES (47,'VA');
INSERT INTO `state` (`StateID`, `Name`) VALUES (48,'WA');
INSERT INTO `state` (`StateID`, `Name`) VALUES (49,'WV');
INSERT INTO `state` (`StateID`, `Name`) VALUES (50,'WI');
INSERT INTO `state` (`StateID`, `Name`) VALUES (51,'WY');

--
-- Table structure for table 'suggestion'
--

DROP TABLE IF EXISTS `suggestion`;
CREATE TABLE `suggestion` (
  `mailaddress` varchar(250) default NULL,
  `mailaddressto` varchar(250) default NULL,
  `mailaddressfrom` varchar(250) default NULL
) TYPE=InnoDB;

--
-- Dumping data for table 'suggestion'
--


--
-- Table structure for table 'suggestionbox'
--

DROP TABLE IF EXISTS `suggestionbox`;
CREATE TABLE `suggestionbox` (
  `emailid` varchar(100) NOT NULL default ''
) TYPE=InnoDB;

--
-- Dumping data for table 'suggestionbox'
--


--
-- Table structure for table 'supportemaildetails'
--

DROP TABLE IF EXISTS `supportemaildetails`;
CREATE TABLE `supportemaildetails` (
  `EmailSubject` varchar(100) NOT NULL default '',
  `EmailMatter` text NOT NULL
) TYPE=InnoDB;

--
-- Dumping data for table 'supportemaildetails'
--


--
-- Table structure for table 'supportemailmatter'
--

DROP TABLE IF EXISTS `supportemailmatter`;
CREATE TABLE `supportemailmatter` (
  `EmailMatter` text NOT NULL
) TYPE=InnoDB;

--
-- Dumping data for table 'supportemailmatter'
--


--
-- Table structure for table 'supportportlet'
--

DROP TABLE IF EXISTS `supportportlet`;
CREATE TABLE `supportportlet` (
  `individualid` int(11) unsigned default NULL,
  `visible` enum('YES','NO') default NULL
) TYPE=InnoDB;

--
-- Dumping data for table 'supportportlet'
--


--
-- Table structure for table 'supportpriority'
--

DROP TABLE IF EXISTS `supportpriority`;
CREATE TABLE `supportpriority` (
  `priorityid` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`priorityid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'supportpriority'
--

INSERT INTO `supportpriority` (`priorityid`, `name`) VALUES (1,'Low');
INSERT INTO `supportpriority` (`priorityid`, `name`) VALUES (2,'Medium');
INSERT INTO `supportpriority` (`priorityid`, `name`) VALUES (3,'High');

--
-- Table structure for table 'supportstatus'
--

DROP TABLE IF EXISTS `supportstatus`;
CREATE TABLE `supportstatus` (
  `statusid` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`statusid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'supportstatus'
--

INSERT INTO `supportstatus` (`statusid`, `name`) VALUES (1,'Open');
INSERT INTO `supportstatus` (`statusid`, `name`) VALUES (2,'Closed');
INSERT INTO `supportstatus` (`statusid`, `name`) VALUES (3,'Resolved');

--
-- Table structure for table 'syncconfig'
--

DROP TABLE IF EXISTS `syncconfig`;
CREATE TABLE `syncconfig` (
  `lastsynceddate` datetime NOT NULL default '0000-00-00 00:00:00',
  `syncedid` int(11) NOT NULL default '0',
  KEY `syncedid` (`syncedid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'syncconfig'
--


--
-- Table structure for table 'syncmaster'
--

DROP TABLE IF EXISTS `syncmaster`;
CREATE TABLE `syncmaster` (
  `syncas` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`syncas`)
) TYPE=InnoDB;

--
-- Dumping data for table 'syncmaster'
--

INSERT INTO `syncmaster` (`syncas`) VALUES ('Fax');
INSERT INTO `syncmaster` (`syncas`) VALUES ('Home');
INSERT INTO `syncmaster` (`syncas`) VALUES ('Main');
INSERT INTO `syncmaster` (`syncas`) VALUES ('Mobile');
INSERT INTO `syncmaster` (`syncas`) VALUES ('Other');
INSERT INTO `syncmaster` (`syncas`) VALUES ('Pager');
INSERT INTO `syncmaster` (`syncas`) VALUES ('Work');

--
-- Table structure for table 'syncproperties'
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
-- Dumping data for table 'syncproperties'
--


--
-- Table structure for table 'task'
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
-- Dumping data for table 'task'
--


--
-- Table structure for table 'taskassigned'
--

DROP TABLE IF EXISTS `taskassigned`;
CREATE TABLE `taskassigned` (
  `TaskID` int(11) unsigned default NULL,
  `AssignedTo` int(11) unsigned default NULL,
  KEY `TaskID` (`TaskID`),
  KEY `AssignedTo` (`AssignedTo`)
) TYPE=InnoDB;

--
-- Dumping data for table 'taskassigned'
--


--
-- Table structure for table 'taskportlet'
--

DROP TABLE IF EXISTS `taskportlet`;
CREATE TABLE `taskportlet` (
  `individualid` int(11) unsigned default NULL,
  `visibility` enum('YES','NO') default NULL,
  KEY `individualid` (`individualid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'taskportlet'
--


--
-- Table structure for table 'taxclass'
--

DROP TABLE IF EXISTS `taxclass`;
CREATE TABLE `taxclass` (
  `taxclassid` int(10) unsigned NOT NULL auto_increment,
  `title` varchar(25) NOT NULL default '',
  `description` text NOT NULL,
  PRIMARY KEY  (`taxclassid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'taxclass'
--

INSERT INTO `taxclass` (`taxclassid`, `title`, `description`) VALUES (1,'Goods','Goods Tax Class');
INSERT INTO `taxclass` (`taxclassid`, `title`, `description`) VALUES (2,'Services','Services');

--
-- Table structure for table 'taxjurisdiction'
--

DROP TABLE IF EXISTS `taxjurisdiction`;
CREATE TABLE `taxjurisdiction` (
  `taxjurisdictionid` int(10) unsigned NOT NULL auto_increment,
  `taxjurisdictionname` varchar(25) NOT NULL default '',
  `taxjurisdictioncode` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`taxjurisdictionid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'taxjurisdiction'
--

INSERT INTO `taxjurisdiction` (`taxjurisdictionid`, `taxjurisdictionname`, `taxjurisdictioncode`) VALUES (1,'Philadelphia','');

--
-- Table structure for table 'taxmatrix'
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
-- Dumping data for table 'taxmatrix'
--

INSERT INTO `taxmatrix` (`taxclassid`, `taxjurisdictionid`, `taxrate`) VALUES (1,1,0.06);
INSERT INTO `taxmatrix` (`taxclassid`, `taxjurisdictionid`, `taxrate`) VALUES (2,1,0);

--
-- Table structure for table 'terms'
--

DROP TABLE IF EXISTS `terms`;
CREATE TABLE `terms` (
  `TermID` int(11) NOT NULL auto_increment,
  `TermName` varchar(20) NOT NULL default '',
  PRIMARY KEY  (`TermID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'terms'
--

INSERT INTO `terms` (`TermID`, `TermName`) VALUES (1,'Dynamic Type 1');
INSERT INTO `terms` (`TermID`, `TermName`) VALUES (2,'Dynamic Type 2');
INSERT INTO `terms` (`TermID`, `TermName`) VALUES (3,'Dynamic Type 3');

--
-- Table structure for table 'thread'
--

DROP TABLE IF EXISTS `thread`;
CREATE TABLE `thread` (
  `threadid` int(11) unsigned NOT NULL auto_increment,
  `ticketid` int(10) unsigned NOT NULL default '0',
  `title` varchar(25) default NULL,
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
-- Dumping data for table 'thread'
--


--
-- Table structure for table 'ticket'
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
-- Dumping data for table 'ticket'
--


--
-- Table structure for table 'ticketlink'
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
-- Dumping data for table 'ticketlink'
--


--
-- Table structure for table 'timeentry'
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
-- Dumping data for table 'timeentry'
--


--
-- Table structure for table 'timesheet'
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
-- Dumping data for table 'timesheet'
--


--
-- Table structure for table 'timeslip'
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
-- Dumping data for table 'timeslip'
--


--
-- Table structure for table 'user'
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `UserID` int(4) unsigned NOT NULL auto_increment,
  `Name` varchar(25) default NULL,
  `IndividualID` int(11) unsigned default NULL,
  `Password` varchar(25) default NULL,
  `userstatus` enum('DISABLED','ENABLED') NOT NULL default 'DISABLED',
  `usertype` enum('EMPLOYEE','CUSTOMER','ADMINISTRATOR') NOT NULL default 'EMPLOYEE',
  PRIMARY KEY  (`UserID`),
  KEY `IndividualID` (`IndividualID`),
  CONSTRAINT `0_4989` FOREIGN KEY (`IndividualID`) REFERENCES `individual` (`IndividualID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'user'
--

INSERT INTO `user` (`UserID`, `Name`, `IndividualID`, `Password`, `userstatus`, `usertype`) VALUES (1,'admin',1,'admin','ENABLED','ADMINISTRATOR');

--
-- Table structure for table 'userpreference'
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
-- Dumping data for table 'userpreference'
--

INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (1,1,4,'calendarrefreshmin','5','');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (2,1,4,'calendarrefreshsec','0','');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (3,1,4,'caldefaultview','DAILY','');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (4,1,0,'TIMEZONE','EST','');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (5,1,6,'DEFAULTFOLDERID','8','0');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (6, 1, 2, 'acknowledgeddays', '23', '');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (7, 1, 1, 'emailaccountid', '1', '');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (8, 1, 4, 'DATEFORMAT', 'MMM d, yyyy h:mm a', '');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (9, 1, 4, 'email', 'YES', '');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (10, 1, 4, 'todayscalendar', 'YES', '');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (11, 1, 4, 'unscheduledactivities', 'YES', '');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (12, 1, 4, 'scheduledopportunities', 'YES', '');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (13, 1, 4, 'projecttasks', 'YES', '');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (14, 1, 4, 'supporttickets', 'YES', '');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (15, 1, 4, 'companynews', 'YES', '');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (16, 1, 4, 'homesettingrefreshmin', '5', '');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (17, 1, 4, 'homesettingrefreshsec', '0', '');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (18, 1, 4, 'contenttype', 'HTML', '');

--
-- Table structure for table 'userpreferencedefault'
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
-- Dumping data for table 'userpreferencedefault'
--
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (1, 2, 'TIMEZONE', 'EST', '');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (2, 3, 'TIMESTAMP', '', '');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (3, 4, 'DATEFORMAT', 'MMM d, yyyy h:mm a', '');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (4, 5, 'DEFAULTFOLDERID', '', '');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (5, 6, 'caldefaultview', 'DAILY', '');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (6, 1, 'calendarrefreshmin', '10', '');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (7, 1, 'calendarrefreshsec', '0', '');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (8, 1, 'homesettingrefreshmin', '5', '');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (9, 1, 'homesettingrefreshsec', '0', '');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (10, 1, 'unscheduledactivities', 'YES', '');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (11, 1, 'todayscalendar', 'YES', '');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (12, 1, 'email', 'YES', '');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (13, 1, 'scheduledopportunities', 'YES', '');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (14, 1, 'projecttasks', 'YES', '');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (15, 1, 'supporttickets', 'YES', '');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (16, 1, 'companynews', 'YES', '');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (17, 1, 'contenttype', 'PLAIN', '');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (18, 1, 'emailaccountid', '', '');



--
-- Table structure for table 'usersecurityprofile'
--

DROP TABLE IF EXISTS `usersecurityprofile`;
CREATE TABLE `usersecurityprofile` (
  `profileid` int(11) unsigned NOT NULL default '0',
  `individualid` int(11) unsigned NOT NULL default '0',
  KEY `profileid` (`profileid`),
  KEY `individualid` (`individualid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'usersecurityprofile'
--

INSERT INTO `usersecurityprofile` (`profileid`, `individualid`) VALUES (1,1);

--
-- Table structure for table 'vendor'
--

DROP TABLE IF EXISTS `vendor`;
CREATE TABLE `vendor` (
  `entityid` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`entityid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'vendor'
--


--
-- Table structure for table 'viewcolumns'
--

DROP TABLE IF EXISTS `viewcolumns`;
CREATE TABLE `viewcolumns` (
  `viewid` int(11) unsigned default NULL,
  `columnname` varchar(100) default NULL,
  `columnorder` int(3) unsigned default NULL,
  KEY `viewid` (`viewid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'viewcolumns'
--

INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (5,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (6,'Type',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (7,'Street1',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (8,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (9,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (10,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (11,'Field',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (12,'Subject',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (13,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (14,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (16,'Title',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (17,'ID',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (18,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (19,'Type',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (21,'Created',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (26,'Fax',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (26,'Phone',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (26,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (29,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (29,'Location',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (29,'Start',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (29,'End',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (29,'Duration',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (29,'Priority',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (29,'Createdby',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (13,'Description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (13,'Created',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (13,'Updated',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (26,'Email',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (31,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (31,'Description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (33,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (33,'Description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (33,'Created',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (13,'CreatedBy',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (23,'RuleName',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (23,'Description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (23,'EnabledStatus',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (25,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (25,'Created',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (25,'Due',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (25,'Priority',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (25,'CreatedBy',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (25,'Status',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (7,'Street2',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (7,'City',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (7,'State',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (7,'ZipCode',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (7,'Country',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (6,'Title',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (6,'Created',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (6,'Due',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (6,'Priority',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (6,'CreatedBy',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (6,'Status',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (8,'Location',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (8,'Start',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (8,'End',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (8,'Duration',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (8,'Priority',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (8,'Createdby',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (8,'Status',8);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (9,'Title',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (9,'Entity',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (9,'Phone',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (9,'Fax',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (9,'Email',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (10,'CallType',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (10,'Duration',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (10,'CreatedBy',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (10,'Due',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (10,'Priority',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (10,'Created',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (10,'Status',8);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (11,'Value',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (12,'From',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (12,'To',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (12,'Received',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (12,'Size',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (21,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (21,'Due',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (21,'Priority',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (21,'CreatedBy',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (21,'Status',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (5,'Title',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (5,'Email',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (5,'Phone',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (19,'Content',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (19,'Notes',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (18,'Location',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (18,'Start',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (18,'End',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (18,'Duration',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (18,'Priority',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (18,'CreatedBy',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (18,'Status',8);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (17,'Entity',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (17,'WhoRequested',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (17,'DateRequested',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (17,'LiteratureRequested',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (17,'DeliveryMethod',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (17,'Forecast Amnt.',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (17,'Sales Rep.',8);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (16,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (16,'Email',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (16,'Phone',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (36,'Title',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (37,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (38,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (39,'Type',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (40,'Street1',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (41,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (42,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (43,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (44,'Field',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (45,'Subject',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (46,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (47,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (49,'ID',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (50,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (53,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (53,'Created',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (58,'Fax',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (58,'Phone',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (58,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (59,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (59,'Location',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (59,'Start',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (59,'End',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (59,'Duration',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (59,'Priority',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (59,'Createdby',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (46,'Description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (46,'Created',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (46,'Updated',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (58,'Email',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (35,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (35,'Address',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (35,'PrimaryContact',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (62,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (62,'Description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (62,'NOOfMembers',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (61,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (61,'Description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (34,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (34,'PrimaryContact',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (34,'Phone',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (34,'Email',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (34,'Address',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (63,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (63,'Description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (63,'Created',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (46,'CreatedBy',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (52,'Type',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (52,'Created',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (52,'Due',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (52,'Priority',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (52,'CreatedBy',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (52,'Status',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (55,'RuleName',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (55,'Description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (55,'EnabledStatus',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (57,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (57,'Created',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (57,'Due',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (57,'Priority',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (57,'CreatedBy',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (57,'Status',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (40,'Street2',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (40,'City',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (40,'State',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (40,'ZipCode',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (40,'Country',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (39,'Title',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (39,'Created',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (39,'Due',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (39,'Priority',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (39,'CreatedBy',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (39,'Status',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (41,'Location',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (41,'Start',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (41,'End',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (41,'Duration',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (41,'Priority',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (41,'Createdby',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (41,'Status',8);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (42,'Title',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (42,'Entity',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (42,'Phone',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (42,'Fax',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (42,'Email',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (43,'CallType',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (43,'Duration',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (43,'CreatedBy',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (43,'Due',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (43,'Priority',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (43,'Created',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (43,'Status',8);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (44,'Value',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (45,'From',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (45,'To',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (45,'Received',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (45,'Size',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (53,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (53,'Due',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (53,'Priority',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (53,'CreatedBy',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (53,'Status',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (37,'Description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (37,'NOOfMembers',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (38,'Title',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (38,'Email',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (38,'Phone',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (51,'Content',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (36,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (51,'Notes',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (50,'Location',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (36,'Entity',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (50,'Start',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (36,'Phone',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (50,'End',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (36,'Fax',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (50,'Duration',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (50,'Priority',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (36,'Email',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (50,'CreatedBy',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (50,'Status',8);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (49,'Entity',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (49,'WhoRequested',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (49,'DateRequested',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (49,'LitratureRequested',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (49,'DiliveryMethod',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (54,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (54,'Date',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (54,'CreatedBy',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (48,'Title',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (48,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (48,'Email',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (48,'Phone',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (52,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (30,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (30,'Title',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (30,'Entity',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (30,'Phone',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (3,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (3,'Entity',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (3,'Phone',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (3,'Fax',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (3,'Email',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (4,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (4,'Description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (4,'NOOfMembers',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (32,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (32,'Description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (1,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (1,'PrimaryContact',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (1,'Phone',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (1,'Email',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (1,'Street1',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (1,'City',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (1,'State',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (51,'Type',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (60,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (60,'Title',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (60,'Entity',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (60,'Phone',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (60,'Fax',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (66,'Subject',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (66,'Entity',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (66,'DateOpened',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (66,'Status',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (66,'DateClosed',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (66,'AssignedTo',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (67,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (67,'Created',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (67,'Updated',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (68,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (68,'DateCreated',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (68,'DateUpdated',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (69,'Subject',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (69,'Entity',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (69,'DateOpened',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (69,'Status',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (69,'DateClosed',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (69,'AssignedTo',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (70,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (70,'Created',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (70,'Updated',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (71,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (71,'DateCreated',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (71,'DateUpdated',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (20,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (20,'Type',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (20,'Created',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (20,'Due',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (20,'Priority',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (20,'CreatedBy',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (33,'Updated',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (33,'CreatedBy',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (33,'FolderName',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (63,'Updated',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (63,'CreatedBy',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (63,'FolderName',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (72,'Entity',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (72,'WhoRequested',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (73,'title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (73,'detail',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (73,'start',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (75,'title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (75,'description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (73,'end',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (73,'registeredattendees',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (73,'ownername',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (77,'SKU',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (77,'Name',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (77,'Type',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (77,'OnHand',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (77,'Price',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (77,'TaxClass',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (77,'Cost',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (76,'title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (76,'description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (76,'startdate',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (76,'enddate',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (76,'nooforders',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (76,'status',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (78,'individualname',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (78,'email',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (78,'accepted',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (79,'InventoryID',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (79,'ItemName',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (79,'Identifier',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (79,'Manufacturer',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (79,'Vendor',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (81,'OrderNO',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (81,'Date',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (81,'Total',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (81,'Status',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (81,'SalesRep',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (82,'PaymentID',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (82,'PaymentMethod',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (82,'Entity',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (82,'PaymentDate',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (82,'Reference',8);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (82,'EnteredBy',9);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (83,'ExpenseID',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (83,'Amount',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (83,'Created',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (83,'Reference',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (83,'Status',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (83,'Creator',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (84,'PurchaseOrderID',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (84,'Created',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (84,'Creator',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (84,'Entity',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (84,'SubTotal',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (84,'Tax',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (84,'Total',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (84,'Status',8);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (74,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (74,'Balance',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (74,'Type',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (74,'ParentAccount',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (85,'DiscountID',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (85,'Description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (85,'From',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (85,'To',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (85,'Discount',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (85,'Status',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (86,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (86,'PrimaryContact',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (86,'Phone',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (86,'Email',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (86,'Website',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (86,'Address',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (87,'InvoiceID',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (87,'Order',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (87,'CustomerID',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (87,'InvoiceDate',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (87,'Total',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (87,'Paid',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (87,'Creator',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (72,'DateRequested',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (72,'LitratureRequested',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (72,'DiliveryMethod',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (72,'literatureid',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (90,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (90,'Project',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (90,'Milestone',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (90,'Owner',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (90,'StartDate',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (90,'DueDate',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (90,'Complete',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (75,'ordervalue',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (75,'numberofrecords',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (75,'proposals',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (75,'ListID',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (75,'orders',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (92,'ID',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (92,'Description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (92,'Project',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (92,'Task',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (92,'Duration',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (92,'CreatedBy',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (92,'Date',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (92,'StartTime',8);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (92,'EndTime',9);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (94,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (94,'Description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (94,'Type',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (94,'Stage',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (94,'Probability',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (94,'Status',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (94,'EstimatedCloseDate',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (94,'ForecastAmmount',8);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (88,'Task',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (88,'Milestone',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (88,'Owner',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (88,'StartDate',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (88,'DueDate',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (88,'Complete',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (89,'Task',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (89,'Description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (89,'Duration',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (89,'CreatedBy',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (89,'Date',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (89,'StartTime',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (89,'EndTime',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (95,'Task',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (95,'Milestone',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (95,'Owner',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (95,'StartDate',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (95,'DueDate',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (95,'Complete',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (96,'Task',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (96,'Description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (96,'Duration',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (96,'CreatedBy',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (96,'Date',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (96,'StartTime',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (96,'EndTime',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (97,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (97,'Project',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (97,'Milestone',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (97,'Owner',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (97,'StartDate',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (97,'DueDate',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (97,'Complete',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (99,'ID',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (99,'Description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (99,'Project',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (99,'Task',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (99,'Duration',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (99,'CreatedBy',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (99,'Date',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (99,'StartTime',8);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (99,'EndTime',9);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (94,'SalePersonName',9);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (100,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (100,'CreatedBy',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (100,'Created',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (100,'DueDate',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (100,'Priority',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (100,'Status',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (101,'Type',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (101,'Title',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (101,'Created',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (101,'Due',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (101,'Priority',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (101,'CreatedBy',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (101,'Status',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (104,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (104,'Title',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (104,'Entity',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (104,'Phone',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (104,'Fax',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (104,'Email',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (105,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (105,'Description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (105,'Created',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (105,'Updated',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (105,'CreatedBy',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (106,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (106,'Date',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (106,'Priority',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (106,'CreatedBy',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (93,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (93,'Description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (93,'Entity',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (93,'Type',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (93,'Stage',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (93,'Probability',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (93,'Status',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (93,'EstimatedCloseDate',8);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (93,'ForecastAmmount',9);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (93,'ActualAmount',10);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (93,'SalePersonName',11);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (19,'SyncAs',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (107,'Subject',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (107,'DateOpened',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (107,'DateClosed',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (107,'Status',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (107,'AssignedTo',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (108,'Subject',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (108,'DateOpened',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (108,'DateClosed',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (108,'Status',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (108,'AssignedTo',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (82,'AppliedAmount',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (82,'UnAppliedAmount',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (82,'AmountPaid',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (110,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (110,'Type',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (110,'Module',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (110,'Record',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (114,'EntityName',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (114,'Strengths',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (114,'Weaknesses',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (114,'Notes',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (111,'Record',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (111,'Name',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (111,'Type',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (111,'Module',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (115,'Type',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (115,'Title',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (115,'Created',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (115,'Due',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (115,'Priority',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (160,'OpportunityID',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (160,'Title',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (160,'Description',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (160,'EntityID',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (160,'Entity',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (160,'Name',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (160,'PrimaryContact',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (160,'Phone',8);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (160,'Email',9);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (160,'Address',10);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (160,'Street1',11);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (160,'Street2',12);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (164,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (164,'Entity',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (91,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (91,'Entity',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (91,'Status',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (91,'DueDate',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (22,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (22,'Date',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (22,'CreatedBy',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (166,'ListID',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (166,'title',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (166,'description',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (167,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (167,'Status',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (163,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (163,'Entity',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (163,'Status',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (2,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (2,'Address',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (2,'PrimaryContact',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (151,'Date',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (151,'Title',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (169,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (169,'Created',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (116,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (116,'PrimaryContact',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (116,'Phone',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (116,'Email',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (170,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (170,'Title',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (170,'Company',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (170,'Phone',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (170,'Fax',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (170,'Email',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (171,'ID',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (171,'Employee',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (171,'StartDate',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (171,'EndDate',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (171,'Duration',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (171,'CreatedBy',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (172,'ID',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (172,'Employee',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (172,'StartDate',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (172,'EndDate',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (172,'Amount',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (172,'Status',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (172,'CreatedBy',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (173,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (173,'Description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (173,'Created',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (173,'Updated',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (173,'CreatedBy',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (183,'individualname',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (183,'email',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (183,'accepted',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (174,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (174,'Entity',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (174,'UserName',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (174,'Email',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (174,'Enabled',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (175,'SearchName',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (175,'Module',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (175,'Record',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (176,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (176,'Phone',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (176,'Fax',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (177,'ViewName',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (177,'Module',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (177,'Record',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (178,'OrderNO',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (178,'Entity',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (178,'Total',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (179,'ProfileName',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (179,'NoOfUsers',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (180,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (180,'Module',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (180,'Record',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (180,'Owner',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (180,'Deleted',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (181,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (181,'Module',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (181,'Record',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (181,'Owner',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (181,'Deleted',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (182,'Date',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (182,'User',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (182,'Action',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (182,'Type',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (182,'Reference',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (182,'Notes',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (184,'Entity',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (184,'Individual',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (184,'Email',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (184,'PhoneNumber',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (185,'ReportID',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (185,'Name',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (185,'Description',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (186,'ReportID',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (186,'Name',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (186,'Description',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (186,'View',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (187,'ReportID',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (187,'Name',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (187,'Description',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (187,'View',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (189,'Entity',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (189,'Individual',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (189,'Email',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (189,'PhoneNumber',4);

-- Turn foreign key checks back on
SET FOREIGN_KEY_CHECKS = 1;

