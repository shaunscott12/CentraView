--
-- $RCSfile: cvdb_1_1_final.sql,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:30:13 $ - $Author: mking_cv $
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

SET FOREIGN_KEY_CHECKS=0;
SET UNIQUE_CHECKS=0;
SET AUTOCOMMIT=0;

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

INSERT INTO `activitystatus` (`StatusID`, `Name`) VALUES (3,'Assigned');
INSERT INTO `activitystatus` (`StatusID`, `Name`) VALUES (2,'Completed');
INSERT INTO `activitystatus` (`StatusID`, `Name`) VALUES (1,'Pending');

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
  `state` varchar(255) default '',
  `Zip` varchar(25) default NULL,
  `country` varchar(255) default '',
  `Website` varchar(100) default NULL,
  `jurisdictionID` int(11) unsigned default '0',
  PRIMARY KEY  (`AddressID`),
  KEY `AddressType` (`AddressType`)
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
  `status` enum('DRAFT','PUBLISH') NOT NULL default 'DRAFT',
  `publishToCustomerView` enum('YES','NO') NOT NULL default 'NO',
  PRIMARY KEY  (`catid`),
  KEY `parent` (`parent`),
  KEY `createdby` (`createdby`),
  KEY `modifiedby` (`modifiedby`),
  KEY `owner` (`owner`)
) TYPE=InnoDB;

--
-- Dumping data for table 'category'
--

INSERT INTO `category` (`catid`, `title`, `description`, `parent`, `createdby`, `modifiedby`, `created`, `modified`, `owner`, `status`, `publishToCustomerView`) VALUES (1,'KnowledgeBase','Root Category',0,1,1,'2003-12-14 23:50:16',20040927155044,1,'DRAFT','YES');

--
-- Table structure for table 'companynews'
--

DROP TABLE IF EXISTS `companynews`;
CREATE TABLE `companynews` (
  `FileID` int(11) NOT NULL auto_increment,
  `DateFrom` datetime NOT NULL default '0000-00-00 00:00:00',
  `DateTo` datetime NOT NULL default '0000-00-00 00:00:00',
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

INSERT INTO `customfieldmultiple` (`CustomFieldID`, `ValueID`, `RecordID`) VALUES (1,0,1);
INSERT INTO `customfieldmultiple` (`CustomFieldID`, `ValueID`, `RecordID`) VALUES (3,0,1);
INSERT INTO `customfieldmultiple` (`CustomFieldID`, `ValueID`, `RecordID`) VALUES (5,0,1);

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

INSERT INTO `customfieldscalar` (`CustomFieldID`, `RecordID`, `Value`) VALUES (2,1,'');
INSERT INTO `customfieldscalar` (`CustomFieldID`, `RecordID`, `Value`) VALUES (4,1,'');

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
-- Table structure for table 'cvfilelink'
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
-- Dumping data for table 'cvfilelink'
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
INSERT INTO `cvfolder` (`FolderID`, `Name`, `Parent`, `CreatedBy`, `ModifiedBy`, `CreatedOn`, `ModifiedOn`, `Description`, `FullPath`, `LocationID`, `owner`, `visibility`, `IsSystem`) VALUES (9,'CV_FILE_DEFAULT_FOLDER',3,2,2,'2003-12-14 23:51:26',20031214235126,'',NULL,1,1,'PUBLIC','TRUE');

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
INSERT INTO `cvtable` (`tableid`, `moduleid`, `name`) VALUES (52,39,'ticket');
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
  CONSTRAINT `0_21439` FOREIGN KEY (`TableId`) REFERENCES `cvtable` (`tableid`)
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
-- Dumping data for table 'defaultprivilege'
--


--
-- Table structure for table 'defaultrecordauthorisation'
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
-- Dumping data for table 'defaultrecordauthorisation'
--

INSERT INTO `defaultrecordauthorisation` (`recordid`, `recordtypeid`, `privilegelevel`) VALUES (1,76,30);
INSERT INTO `defaultrecordauthorisation` (`recordid`, `recordtypeid`, `privilegelevel`) VALUES (2,76,30);
INSERT INTO `defaultrecordauthorisation` (`recordid`, `recordtypeid`, `privilegelevel`) VALUES (3,76,30);
INSERT INTO `defaultrecordauthorisation` (`recordid`, `recordtypeid`, `privilegelevel`) VALUES (4,76,30);
INSERT INTO `defaultrecordauthorisation` (`recordid`, `recordtypeid`, `privilegelevel`) VALUES (5,76,10);
INSERT INTO `defaultrecordauthorisation` (`recordid`, `recordtypeid`, `privilegelevel`) VALUES (6,76,10);
INSERT INTO `defaultrecordauthorisation` (`recordid`, `recordtypeid`, `privilegelevel`) VALUES (7,76,30);
INSERT INTO `defaultrecordauthorisation` (`recordid`, `recordtypeid`, `privilegelevel`) VALUES (1,32,30);
INSERT INTO `defaultrecordauthorisation` (`recordid`, `recordtypeid`, `privilegelevel`) VALUES (9,76,20);

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
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('ForecastSales',14);
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
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('EventAtendees',183);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('StandardReport',185);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('AdHocReport',186);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('CustomReport',187);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('ReportResult',188);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('MarketingListMembers',189);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Literature',190);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Merge',191);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('BottomProposal',192);
INSERT INTO `defaultviews` (`listtype`, `viewid`) VALUES ('Template',193);

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
  `SecureConnectionIfPossible` enum('NO','YES') NOT NULL default 'NO',
  `ForceSecureConnection` enum('NO','YES') NOT NULL default 'NO',
  `SMTPRequiresAuthentication` enum('NO','YES') NOT NULL default 'NO',
  `POPBeforeSMTP` enum('NO','YES') NOT NULL default 'NO',
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
-- Table structure for table 'emaildelegation'
--

DROP TABLE IF EXISTS `emaildelegation`;
CREATE TABLE `emaildelegation` (
  `individualID` int(11) unsigned NOT NULL default '0',
  `delegatorID` int(11) unsigned NOT NULL default '0',
  UNIQUE KEY `delegationID` (`individualID`,`delegatorID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'emaildelegation'
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
-- Table structure for table 'emailmessagefolder'
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
-- Dumping data for table 'emailmessagefolder'
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
  `Address` varchar(255) default NULL,
  `RecipientType` enum('TO','CC','BCC','FROM') default NULL,
  `RecipientIsGroup` enum('YES','NO') default NULL,
  KEY `MessageID` (`MessageID`),
  KEY `RecipientID` (`RecipientID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'emailrecipient'
--


--
-- Table structure for table 'emailrule'
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
-- Dumping data for table 'emailrule'
--


--
-- Table structure for table 'emailruleaction'
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
-- Dumping data for table 'emailruleaction'
--


--
-- Table structure for table 'emailrulecriteria'
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
-- Dumping data for table 'emailrulecriteria'
--


--
-- Table structure for table 'emailsettings'
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
-- Dumping data for table 'emailsettings'
--

INSERT INTO `emailsettings` (`username`, `password`, `authentication`, `smtpport`, `smtpserver`) VALUES (NULL,NULL,'NO',25,NULL);

--
-- Table structure for table 'emailtemplate'
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
-- Dumping data for table 'emailtemplate'
--

INSERT INTO `emailtemplate` (`templateID`, `name`, `description`, `toAddress`, `fromAddress`, `replyTo`, `subject`, `body`, `requiredToAddress`, `requiredFromAddress`, `requiredReplyTo`, `requiredSubject`, `requiredBody`) VALUES (1,'Support Ticket','Auto-response Template for New Tickets.','','','','',NULL,'NO','NO','NO','YES','YES');
INSERT INTO `emailtemplate` (`templateID`, `name`, `description`, `toAddress`, `fromAddress`, `replyTo`, `subject`, `body`, `requiredToAddress`, `requiredFromAddress`, `requiredReplyTo`, `requiredSubject`, `requiredBody`) VALUES (2,'Support Thread','Auto-response Template for Open Tickets (New Threads).','','','','',NULL,'NO','NO','NO','NO','YES');
INSERT INTO `emailtemplate` (`templateID`, `name`, `description`, `toAddress`, `fromAddress`, `replyTo`, `subject`, `body`, `requiredToAddress`, `requiredFromAddress`, `requiredReplyTo`, `requiredSubject`, `requiredBody`) VALUES (3,'Support Error','Auto-response Template for Errors (Invalid Ticket ID).','','','','',NULL,'NO','NO','NO','NO','YES');
INSERT INTO `emailtemplate` (`templateID`, `name`, `description`, `toAddress`, `fromAddress`, `replyTo`, `subject`, `body`, `requiredToAddress`, `requiredFromAddress`, `requiredReplyTo`, `requiredSubject`, `requiredBody`) VALUES (4,'Activities','Auto-response Template for reminding email to Attendee\'s of Activity.','','','','','','NO','NO','NO','YES','YES');
INSERT INTO `emailtemplate` (`templateID`, `name`, `description`, `toAddress`, `fromAddress`, `replyTo`, `subject`, `body`, `requiredToAddress`, `requiredFromAddress`, `requiredReplyTo`, `requiredSubject`, `requiredBody`) VALUES (5,'Task','Auto-response Template to notify that Task is completed.','','','','','','NO','NO','NO','YES','YES');
INSERT INTO `emailtemplate` (`templateID`, `name`, `description`, `toAddress`, `fromAddress`, `replyTo`, `subject`, `body`, `requiredToAddress`, `requiredFromAddress`, `requiredReplyTo`, `requiredSubject`, `requiredBody`) VALUES (6,'Forgot Password','Auto-response Template for Forgot Password.','','','','','','NO','YES','YES','YES','NO');
INSERT INTO `emailtemplate` (`templateID`, `name`, `description`, `toAddress`, `fromAddress`, `replyTo`, `subject`, `body`, `requiredToAddress`, `requiredFromAddress`, `requiredReplyTo`, `requiredSubject`, `requiredBody`) VALUES (7,'Suggestion Box','Auto-response Template for Suggestion Box.','','','','','','YES','YES','YES','YES','NO');
INSERT INTO `emailtemplate` (`templateID`, `name`, `description`, `toAddress`, `fromAddress`, `replyTo`, `subject`, `body`, `requiredToAddress`, `requiredFromAddress`, `requiredReplyTo`, `requiredSubject`, `requiredBody`) VALUES (8,'Change Request for Profile','Auto-response Template for Profile change request.','','','','','','YES','YES','YES','YES','YES');
INSERT INTO `emailtemplate` (`templateID`, `name`, `description`, `toAddress`, `fromAddress`, `replyTo`, `subject`, `body`, `requiredToAddress`, `requiredFromAddress`, `requiredReplyTo`, `requiredSubject`, `requiredBody`) VALUES (9,'Change Request for User','Auto-response Template for User Information change request.','','','','','','YES','YES','YES','YES','YES');

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

INSERT INTO `entity` (`EntityID`, `ExternalID`, `Name`, `Source`, `Modified`, `Created`, `Owner`, `ModifiedBy`, `DBase`, `List`, `Creator`, `AccountManagerID`, `AccountTeamID`) VALUES (1,'','Default Entity',0,20040308092111,20040308092111,1,1,0,1,1,NULL,NULL);

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
-- Table structure for table 'eventlink'
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
-- Dumping data for table 'eventlink'
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
  `publishToCustomerView` enum('YES','NO') NOT NULL default 'NO',
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
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (461,48,'Creator');
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
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (655,111,'priority');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (656,111,'RelateEntity');
INSERT INTO `field` (`fieldid`, `tableid`, `name`) VALUES (657,111,'RelateIndividual');

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
INSERT INTO `fieldauthorisation` (`individualid`, `fieldid`, `referenceid`, `profileid`, `privilegelevel`) VALUES (NULL,324,NULL,1,10);

--
-- Table structure for table 'fieldextra'
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
-- Table structure for table 'globalreplacerelate'
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
-- Dumping data for table 'globalreplacerelate'
--

INSERT INTO `globalreplacerelate` (`RelateTableID`, `SearchTableID`, `IsRelateTable`) VALUES (5,1,'N');
INSERT INTO `globalreplacerelate` (`RelateTableID`, `SearchTableID`, `IsRelateTable`) VALUES (5,2,'N');

--
-- Table structure for table 'gobalreplacevalue'
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
-- Dumping data for table 'gobalreplacevalue'
--

INSERT INTO `gobalreplacevalue` (`tableID`, `fieldID`, `fieldName`, `tableName`) VALUES (1,4,' ListID as ValueID , Title as Value','marketinglist');
INSERT INTO `gobalreplacevalue` (`tableID`, `fieldID`, `fieldName`, `tableName`) VALUES (2,18,' ListID as ValueID , Title as Value','marketinglist');

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
-- Table structure for table 'history'
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
-- Dumping data for table 'history'
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

INSERT INTO `individual` (`IndividualID`, `Entity`, `FirstName`, `MiddleInitial`, `LastName`, `Title`, `PrimaryContact`, `Owner`, `CreatedBy`, `ModifiedBy`, `Created`, `Modified`, `ExternalID`, `Source`, `list`) VALUES (1,1,'Administrative','','User','','YES',1,1,1,'2004-03-08 09:21:19',20040308092119,'',0,1);

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
-- Dumping data for table 'inventory'
--


--
-- Table structure for table 'inventorystatus'
--

DROP TABLE IF EXISTS `inventorystatus`;
CREATE TABLE `inventorystatus` (
  `StatusID` int(11) unsigned NOT NULL default '0',
  `StatusName` char(50) NOT NULL default '',
  KEY `StatusID` (`StatusID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'inventorystatus'
--

INSERT INTO `inventorystatus` (`StatusID`, `StatusName`) VALUES (1,'Avalible');
INSERT INTO `inventorystatus` (`StatusID`, `StatusName`) VALUES (2,'Back Ordered');
INSERT INTO `inventorystatus` (`StatusID`, `StatusName`) VALUES (1,'Not Avalible');

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
  `status` enum('Active','Deleted') NOT NULL default 'Active',
  `TransactionID` int(11) NOT NULL default '0',
  `taxAmount` float NOT NULL default '0',
  PRIMARY KEY  (`InvoiceLineID`),
  KEY `InvoiceID` (`InvoiceID`),
  KEY `ItemID` (`ItemID`),
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
  `modifieddate` timestamp(14) NOT NULL,
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
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Appointment','Details',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Appointment','Entity',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Appointment','Individual',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Appointment','Start',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Appointment','End',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Appointment','Status',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Appointment','Priority',8);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Appointment','Owner',9);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Appointment','Created',10);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Appointment','CreatedBy',11);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Appointment','Notes',12);
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
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomProposal','ProposalID',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomProposal','Title',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomProposal','Description',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomProposal','Type',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomProposal','Stage',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomProposal','Probability',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomProposal','Status',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomProposal','EstimatedCloseDate',8);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('BottomProposal','ForecastAmmount',9);
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
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Call','Details',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Call','Entity',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Call','Individual',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Call','Start',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Call','End',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Call','CallType',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Call','Status',8);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Call','Priority',9);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Call','Owner',10);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Call','Created',11);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Call','CreatedBy',12);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Call','Notes',13);
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
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Entity','AccountManager',15);
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
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ForecastSales','Title',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ForecastSales','Description',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ForecastSales','Entity',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ForecastSales','Type',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ForecastSales','Stage',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ForecastSales','Probability',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ForecastSales','Status',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ForecastSales','EstimatedCloseDate',8);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ForecastSales','ForecastAmount',9);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ForecastSales','ActualAmount',10);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ForecastSales','SalesPersonName',11);
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
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('History','RecordName',5);
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
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Literature','Name',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Literature','File',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('LiteratureFulfillment','LiteratureRequested',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('LiteratureFulfillment','WhoRequested',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('LiteratureFulfillment','DateRequested',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('LiteratureFulfillment','DeliveryMethod',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('LiteratureFulfillment','Entity',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Marketing','title',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Marketing','description',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Marketing','OrderValue',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Marketing','numberofrecords',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Marketing','Opportunities',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Marketing','OpportunityValue',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Marketing','Order',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MarketingListMembers','Entity',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MarketingListMembers','Individual',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MarketingListMembers','Email',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MarketingListMembers','PhoneNumber',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Meeting','Title',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Meeting','Details',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Meeting','Entity',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Meeting','Individual',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Meeting','Start',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Meeting','End',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Meeting','Status',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Meeting','Priority',8);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Meeting','Owner',9);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Meeting','Created',10);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Meeting','CreatedBy',11);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Meeting','Notes',12);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Merge','Title',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Merge','Owner',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MOC','Type',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MOC','Content',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MOC','Notes',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MultiActivity','Title',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MultiActivity','Details',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MultiActivity','Entity',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MultiActivity','Individual',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MultiActivity','Start',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MultiActivity','End',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MultiActivity','Type',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MultiActivity','Status',8);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MultiActivity','Priority',9);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MultiActivity','Owner',10);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MultiActivity','Created',11);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MultiActivity','CreatedBy',12);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('MultiActivity','Notes',13);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('NextAction','Title',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('NextAction','Details',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('NextAction','Entity',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('NextAction','Individual',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('NextAction','Start',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('NextAction','End',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('NextAction','Status',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('NextAction','Priority',8);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('NextAction','Owner',9);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('NextAction','Created',10);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('NextAction','CreatedBy',11);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('NextAction','Notes',12);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Note','Title',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Note','Date',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Note','CreatedBy',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Note','Detail',4);
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
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Rule','Name',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Rule','Description',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Rule','Enabled',3);
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
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Task','Manager',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Task','StartDate',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Task','DueDate',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Task','Complete',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Task','Status',9);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Template','TemplateID',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Template','Name',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Template','Category',3);
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
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Ticket','Number',8);
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
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ToDo','Details',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ToDo','Entity',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ToDo','Individual',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ToDo','Start',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ToDo','End',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ToDo','Status',7);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ToDo','Priority',8);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ToDo','Owner',9);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ToDo','Created',10);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ToDo','CreatedBy',11);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('ToDo','Notes',12);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('USER','UserID',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('USER','IndividualID',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('user','UserName',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('user','Name',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('user','Entity',5);
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
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('ForecastSales',19);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Meeting',21);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('NextAction',22);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('ActivityTask',23);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('ToDo',23);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('BottomProposal',31);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('BottomContacts',36);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('BottomFiles',36);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('BottomMultiActivity',36);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('BottomNotes',36);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('BottomTask',36);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('BottomTimeslip',36);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Project',36);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('FAQ',37);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Task',37);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Tasks',37);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Knowledgebase',38);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Timeslip',38);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Ticket',39);
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
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Template',59);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('TimeSheet',60);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Expenses',61);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('EmployeeHandbook',62);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('AdHocReport',63);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('CustomReport',63);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('ReportResult',63);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('StandardReport',63);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Merge',64);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('USER',65);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('SavedSearch',66);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('CustomView',67);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('SecurityProfile',68);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('CVAttic',69);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('CVGarbage',70);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('History',71);
INSERT INTO `listtypes` (`typename`, `moduleid`) VALUES ('Literature',73);

--
-- Table structure for table 'listview'
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
-- Dumping data for table 'listview'
--


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

INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Entity',1,'Default View',NULL,NULL,'Name','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Individual',3,'Default View',NULL,NULL,'Name','D',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Group',4,'Default View',NULL,NULL,'Name','D',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('GroupMember',5,'Default View',NULL,NULL,'Name','D',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('AllActivity',6,'Default View',NULL,NULL,'Title','D',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Address',7,'Default View',NULL,NULL,'Street1','D',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Appointment',8,'Default View',NULL,NULL,'Title','D',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('BottomIndividual',9,'Default View',NULL,NULL,'Name','D',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Call',10,'Default View',NULL,NULL,'Title','D',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('CustomField',11,'Default View',NULL,NULL,'Field','D',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Email',12,'Default View',NULL,NULL,'Received','D',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('File',13,'Default View',NULL,NULL,'Name','D',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('ForecastSales',14,'Default View',NULL,NULL,'Title','D',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Meeting',18,'Default View',NULL,NULL,'Title','D',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('MOC',19,'Default View',NULL,NULL,'Type','D',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('MultiActivity',20,'Default View',NULL,NULL,'Type','A',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('NextAction',21,'Default View',NULL,NULL,'Title','D',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Note',22,'Default View',NULL,NULL,'date','D',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Rule',23,'Default View',NULL,NULL,'Name','D',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Tasks',24,'Default View',NULL,NULL,'Name','D',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('ToDo',25,'Default View',NULL,NULL,'Title','D',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Ticket',66,'Default View',NULL,NULL,'Subject','D',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('FAQ',70,'Default View',NULL,NULL,'Title','D',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Knowledgebase',71,'Default View',NULL,NULL,'Name','D',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('LiteratureFulfillment',72,'Default View',NULL,NULL,'LiteratureRequested','A',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Event',73,'Default View',NULL,NULL,'eventid','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('GLAccount',74,'Default View',NULL,NULL,'Name','A',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Marketing',75,'Default View',NULL,NULL,'Description','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Promotion',76,'Default View',NULL,NULL,'title','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Item',77,'Default View',NULL,NULL,'SKU','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Inventory',79,'Default View',NULL,NULL,'InventoryID','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Order',81,'Default View',NULL,NULL,'OrderNO','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Payment',82,'Default View',NULL,NULL,'PaymentID','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Expense',83,'Default View',NULL,NULL,'ExpenseID','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('PurchaseOrder',84,'Default View',NULL,NULL,'PurchaseOrderID','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('VolumeDiscount',85,'Default View',NULL,NULL,'DiscountID','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Vendor',86,'Default View',NULL,NULL,'Name','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('InvoiceHistory',87,'Default View',NULL,NULL,'InvoiceID','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('BottomTask',88,'Default View',NULL,NULL,'Task','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('BottomTimeslip',89,'Default View',NULL,NULL,'Task','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Task',90,'Default View',NULL,NULL,'Title','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Project',91,'Default View',NULL,NULL,'Name','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Timeslip',92,'Default View',NULL,NULL,'ID','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Opportunity',93,'Default View',NULL,NULL,'EstimatedCloseDate','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Proposal',94,'Default View',NULL,NULL,'EstimatedCloseDate','A',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('ActivityTask',100,'Default View',NULL,NULL,'Title','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('BottomMultiActivity',101,'Default View',NULL,NULL,'Type','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('BottomContacts',104,'Default View',NULL,NULL,'Name','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('BottomFiles',105,'Default View',NULL,NULL,'Name','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('BottomNotes',106,'Default View',NULL,NULL,'Title','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('CustomFields',110,'Default View',NULL,NULL,'Name','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Employee',170,'Default View',NULL,NULL,'Name','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('TimeSheet',171,'Default View',NULL,NULL,'ID','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Expenses',172,'Default View',NULL,NULL,'ID','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('EmployeeHandbook',173,'Default View',NULL,NULL,'Name','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('USER',174,'Default User',NULL,NULL,'Name','A',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('SavedSearch',175,'Default Saved Search',NULL,NULL,'SearchName','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Employee',176,'uma_testv1',546,546,'Name','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('CustomView',177,'Default CustomView',NULL,NULL,'ViewName','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Order',178,'uma_v1',546,546,'OrderNO','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('SecurityProfile',179,'Default Security Profile',NULL,NULL,'ProfileName','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('CVAttic',180,'Default Attic',NULL,NULL,'Title','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('CVGarbage',181,'Default Garbage',NULL,NULL,'Title','A',17,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('History',182,'Default History',NULL,NULL,'Date','D',17,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('EventAtendees',183,'Default View',NULL,NULL,'individualname','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('MarketingListMembers',184,'Default View',NULL,NULL,'Entity','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('StandardReport',185,'Default View',NULL,NULL,'ReportID','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('AdHocReport',186,'Default View',NULL,NULL,'ReportID','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('CustomReport',187,'Default View',NULL,NULL,'ReportID','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('ReportResult',188,'Default View',NULL,NULL,'ReportID','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('MarketingListMembers',189,'Default View',NULL,NULL,'Entity','A',0,100,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Literature',190,'Default View',NULL,NULL,'Name','A',0,10,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Merge',191,'Merge Default',NULL,NULL,'Owner','A',0,101,'A');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('BottomProposal',192,'Default View',NULL,NULL,'Title','A',0,100,'D');
INSERT INTO `listviews` (`listtype`, `viewid`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES ('Template',193,'Default View',NULL,NULL,'Name','A',0,100,'A');

--
-- Table structure for table 'literature'
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
  `locationid` int(10) unsigned NOT NULL auto_increment,
  `title` varchar(25) default NULL,
  `parent` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`locationid`),
  KEY `parent` (`parent`)
) TYPE=InnoDB;

--
-- Dumping data for table 'location'
--


--
-- Table structure for table 'mailimportfields'
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
-- Dumping data for table 'mailimportfields'
--

INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (1,1,'First Name','firstName');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (2,1,'MI','middleInitial');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (3,1,'Last Name','lastName');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (4,1,'Street1','street1');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (5,1,'Street2','street2');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (6,1,'City','city');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (7,1,'State','state');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (8,1,'Zip','zip');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (9,1,'Country','country');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (10,1,'Main Phone','mainPhone');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (11,1,'Fax Phone','faxPhone');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (12,1,'Mobile Phone','mobilePhone');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (13,1,'Home Phone','homePhone');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (14,1,'Other Phone','otherPhone');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (15,1,'Pager Phone','pagerPhone');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (16,1,'Work Phone','workPhone');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (17,1,'Email','email');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (18,1,'Source','source');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (19,1,'Title','title');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (20,1,'ID2','ID2');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (21,1,'Entity Name','entityName');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (22,1,'Entity Street1','entityStreet1');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (23,1,'Entity Street2','entityStreet2');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (24,1,'Entity City','entityCity');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (25,1,'Entity State','entityState');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (26,1,'Entity Zip','entityZip');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (27,1,'Entity Country','entityCountry');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (28,1,'Entity Main Phone','entityMainPhone');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (29,1,'Entity Fax Phone','entityFaxPhone');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (30,1,'Entity Mobile Phone','entityMobilePhone');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (31,1,'Entity Home Phone','entityHomePhone');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (32,1,'Entity Other Phone','entityOtherPhone');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (33,1,'Entity Pager Phone','entityPagerPhone');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (34,1,'Entity Work Phone','entityWorkPhone');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (35,1,'Entity Email','entityEmail');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (36,1,'Entity Source','entitySource');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (37,1,'Entity ID2','entityID2');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (38,1,'Main Phone Ext','mainPhoneExt');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (39,1,'Fax Phone Ext','faxPhoneExt');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (40,1,'Mobile Phone Ext','mobilePhoneExt');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (41,1,'Home Phone Ext','homePhoneExt');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (42,1,'Other Phone Ext','otherPhoneExt');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (43,1,'Pager Phone Ext','pagerPhoneExt');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (44,1,'Work Phone Ext','workPhoneExt');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (45,1,'Entity Main Phone Ext','entityMainPhoneExt');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (46,1,'Entity Fax Phone Ext','entityFaxPhoneExt');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (47,1,'Entity Mobile Phone Ext','entityMobilePhoneExt');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (48,1,'Entity Home Phone Ext','entityHomePhoneExt');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (49,1,'Entity Other Phone Ext','entityOtherPhoneExt');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (50,1,'Entity Pager Phone Ext','entityPagerPhoneExt');
INSERT INTO `mailimportfields` (`fieldID`, `typeID`, `name`, `fieldName`) VALUES (51,1,'Entity Work Phone Ext','entityWorkPhoneExt');

--
-- Table structure for table 'mailimporttypes'
--

DROP TABLE IF EXISTS `mailimporttypes`;
CREATE TABLE `mailimporttypes` (
  `typeID` int(11) unsigned NOT NULL auto_increment,
  `name` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`typeID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'mailimporttypes'
--

INSERT INTO `mailimporttypes` (`typeID`, `name`) VALUES (1,'Contact');

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

INSERT INTO `marketinglist` (`ListID`, `Title`, `Description`, `NumberofRecords`, `Proposals`, `Orders`, `Ordervalue`, `Status`, `Owner`, `Creator`, `ModifiedBy`, `Created`, `Modified`) VALUES (1,'Default List','Do not delete this list.',0,0,0,0,'YES',1,1,1,'2004-03-08 09:21:31',20040308092131);

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
INSERT INTO `moctype` (`MOCTypeID`, `Name`) VALUES (4,'Main');
INSERT INTO `moctype` (`MOCTypeID`, `Name`) VALUES (5,'Home');
INSERT INTO `moctype` (`MOCTypeID`, `Name`) VALUES (6,'Other');
INSERT INTO `moctype` (`MOCTypeID`, `Name`) VALUES (7,'Pager');
INSERT INTO `moctype` (`MOCTypeID`, `Name`) VALUES (8,'Work');

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
  `primarykeyfield` varchar(255) NOT NULL default '',
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
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (2,NULL,0,0,'emailmessage','Owner','','','','Email');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (3,NULL,0,0,'activity','owner','activityid','','','Activities');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (4,NULL,0,0,'','','','','','Calendar');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (5,NULL,0,0,'note','Owner','NoteID','','','Notes');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (6,NULL,0,0,'cvfile','Owner','FileID','','','File');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (7,NULL,0,0,'','','','','','Sales');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (8,NULL,0,0,'','','','','','Marketing');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (9,NULL,0,0,'','','','','','Projects');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (10,NULL,0,0,'','','','','','Support');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (11,NULL,0,0,'','','','','','Authentication');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (12,NULL,0,0,'','','','','','Accounting');
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
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (25,4,0,0,'','','','','','DailyView');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (26,4,0,0,'','','','','','WeeklyView');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (27,4,0,0,'','','','','','WeeklyColumnar');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (28,4,0,0,'','','','','','MonthlyView');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (29,4,0,0,'','','','','','YearlyView');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (30,7,0,0,'opportunity o, activity a','owner','o.activityid = a.activityid and o.opportunityid','','','Opportunities');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (31,7,0,0,'proposal','Owner','ProposalID','','','Proposals');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (32,8,0,0,'marketinglist','owner','ListID','','','ListManager');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (33,8,0,0,'promotion','owner','PromotionID','','','Promotion');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (34,8,0,0,'literaturerequest','','','','','LiteratureFulfilment');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (35,8,0,0,'event','owner','EventId','','','Events');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (36,9,0,0,'project','Owner','ProjectID','','','Projects');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (37,9,0,0,' task, activity','owner','task.ActivityID=activity.ActivityID and task.ActivityID','','','Tasks');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (38,9,0,0,'timeslip','CreatedBy','TimeSlipID','','','Time Slips');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (39,10,0,0,'ticket','owner','ticketid','','','Ticket');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (40,10,0,0,'faq','owner','faqid','','','FAQ');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (41,10,0,0,'knowledgebase','owner','kbid','','','Knowledgebase');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (42,12,0,0,'cvorder','owner','orderid','','','OrderHistory');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (43,12,0,0,'payment','owner','paymentid','','','Payment');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (44,12,0,0,'expense','owner','expenseid','','','Expense');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (45,12,0,0,'purchaseorder','owner','purchaseorderid','','','PurchaseOrder');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (46,12,0,0,'item','createdby','itemid','','','Item');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (47,12,0,0,'glaccount','','glaccountsid','','','GLAccount');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (48,12,0,0,'inventory','Owner','inventoryid','','','Inventory');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (49,12,0,0,'','','','','','VolumeDiscount');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (50,12,0,0,'vendor','','','','','Vendor');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (51,13,0,0,'expenseform','Owner','ExpenseFormID','','','ExpenseForms');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (52,13,0,0,'timesheet','owner','TimeSheetID','','','Time Sheets');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (53,13,0,0,'','','','','','EmployeeHandbook');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (54,13,0,0,'employee','','','','','EmployeeList');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (55,13,0,0,'','','','','','SuggestionBox');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (56,12,0,0,'invoice','owner','InvoiceId','','','InvoiceHistory');
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
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (76,6,0,0,'cvfolder','owner','FolderID','','','CVFolder');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (77,NULL,0,0,'event','','EventID','','','Event');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (78,NULL,0,0,'marketinglist','Owner','ListId','','','MarketingList');
INSERT INTO `module` (`moduleid`, `parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (79,NULL,0,0,'emailrule','','ruleID','','','Email Rule');

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
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (3,14,'name',3,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (14,15,'title',4,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,5,'entity',5,'','RelateEntity',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,5,'individual',6,'','RelateIndividual',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,14,'address',7,'','',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,14,'id1',8,'','',1);
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
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,35,'description',76,'','Description',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,32,'listdescription',79,'','Listdescription',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,35,'masterlistid',81,'','Masterlistid',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,35,'moderatorname',82,'','Moderatorname',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,35,'maxattendees',83,'','Maxattendees',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,42,'statusIdValue',95,'','StatusIdValue',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,42,'termsIdValue',99,'','TermsIdValue',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,42,'acctMgrIdValue',100,'','AcctMgrIdValue',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,42,'projectIdValue',101,'','ProjectIdValue',1);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,31,'description',105,'','Description',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,31,'status',106,'','Statusid',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,31,'stage',107,'','Stage',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,31,'proposaltype',108,'','Typeid',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,31,'estimatedclosedate',109,'','Estimatedclose',0);
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
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,56,'po',120,'','Po',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,56,'statusId',121,'','StatusId',0);
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
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,16,'description',191,'','Description',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,16,'owner',192,'','Owner',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,16,'memberlist',193,'','',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,51,'formDescription',194,'','FormDescription',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,51,'notes',195,'','Notes',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,51,'linestatus',196,'','Linestatus',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,51,'expenseId',197,'','ExpenseId',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,52,'description',198,'','Description',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,52,'notes',199,'','Notes',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,52,'status',200,'','Status',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,53,'description',201,'','Description',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,17,'activityDetails',203,'','ActivityDetails',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,17,'entityVO',204,'','EntityVO',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,17,'individualVO',205,'','IndividualVO',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,17,'opportunityID',206,'','OportunityID',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,17,'projectID',207,'','ProjectID',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,17,'status',208,'','Status',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,17,'notes',209,'','Notes',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,17,'activityStartDate',210,'','ActivityStartDate',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,17,'activityEndDate',211,'','ActivityEndDate',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,17,'owner',212,'','Owner',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,53,'authorname',215,'','Authorname',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,18,'activityDetails',217,'','ActivityDetails',0);
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,18,'entityVO',218,'','EntityVO',0);
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
INSERT INTO `modulefieldmapping` (`fieldid`, `moduleid`, `name`, `mapid`, `listcolname`, `methodname`, `accesscontrollevel`) VALUES (0,14,'title',324,'','',1);

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
  `taxAmount` float NOT NULL default '0',
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

INSERT INTO `ptcategory` (`ptcategoryid`, `catname`, `artifactid`, `parentid`, `cattype`) VALUES (1,'Print',1,4,'PRINT');
INSERT INTO `ptcategory` (`ptcategoryid`, `catname`, `artifactid`, `parentid`, `cattype`) VALUES (2,'Email',2,4,'EMAIL');
INSERT INTO `ptcategory` (`ptcategoryid`, `catname`, `artifactid`, `parentid`, `cattype`) VALUES (3,'Proposal',2,4,'PRINT');
INSERT INTO `ptcategory` (`ptcategoryid`, `catname`, `artifactid`, `parentid`, `cattype`) VALUES (4,'Ticket',2,4,'PRINT');

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

INSERT INTO `ptdetail` (`ptdetailid`, `ptcategoryid`, `isdefault`, `ptdata`, `ptname`, `userid`, `artifactid`, `pttype`, `ptsubject`) VALUES (1,1,'YES','<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&lt;EntityName/&gt;<?xml:namespace prefix = o ns = \"urn:schemas-microsoft-com:office:office\" /><o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&lt;Address/&gt;<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&nbsp;<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>date <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&nbsp;<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>Dear &lt;FirstName/&gt; <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><FONT face=\"Times New Roman\"><FONT size=3>&nbsp;<o:p></o:p></FONT></FONT></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt; TEXT-ALIGN: center\" align=center><FONT size=3><B style=\"mso-bidi-font-weight: normal\"><SPAN style=\"FONT-FAMILY: Tahoma\">attention grabbing heading</SPAN></B><SPAN style=\"FONT-FAMILY: Tahoma\"> (up to 10 words)<o:p></o:p></SPAN></FONT></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&nbsp;<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>(the heading must grab attention - something your prospect will relate to that your proposition will produce - for example, <B style=\"mso-bidi-font-weight: normal\">Cost-Effective Sales Enquiry Generation</B>, or <B style=\"mso-bidi-font-weight: normal\">Reduce Your Staffing Overheads</B>, or <B style=\"mso-bidi-font-weight: normal\">Fast-Track Management Training</B>) <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><FONT face=\"Times New Roman\"><FONT size=3>&nbsp;<o:p></o:p></FONT></FONT></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>It has come to my attention that you may be in the market for (product/service). I welcome the opportunity to gain a better understanding of your requirements, and to work together with you to determine if our product/service will help you achieve your goals.<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><FONT face=\"Times New Roman\"><FONT size=3>&nbsp;<o:p></o:p></FONT></FONT></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>(Optional) Our customers include (two or three examples, relevant and known to the prospect), who have found (state key benefit, % savings, strategic advantage) from working with us. <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&nbsp;<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>I will telephone you soon to agree a future contact time that suits you/your own review timescale. <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&nbsp;<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>Yours sincerely <o:p></o:p></FONT></SPAN></P>\r\n<P><SPAN style=\"FONT-SIZE: 12pt; FONT-FAMILY: Tahoma; mso-fareast-font-family: \'Arial Unicode MS\'; mso-ansi-language: EN-US; mso-bidi-language: AR-SA\">(Name)</SPAN></P>','Sample Template',1,2,'PRINT','hello');
INSERT INTO `ptdetail` (`ptdetailid`, `ptcategoryid`, `isdefault`, `ptdata`, `ptname`, `userid`, `artifactid`, `pttype`, `ptsubject`) VALUES (2,3,'YES','<html>\n<head>\n  <title>Untitled Document</title>\n</head>\n<body>\n<table width=\"100%%\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\">\n  <tr>\n    <td width=\"50%\" align=\"left\" valign=\"top\">\n      <table width=\"100%%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n        <tr>\n          <td width=\"50%\" align=\"left\" valign=\"top\">&lt;OwningEntity/&gt;</td>\n          <td width=\"50%\" valign=\"top\"><div align=\"right\"><font size=\"+2\" face=\"Arial, Helvetica, sans-serif\"><strong>PROPOSAL</strong></font></div></td>\n        </tr>\n      </table>\n      <br>\n      <hr>\n      <table width=\"100%%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n        <tr align=\"left\" valign=\"top\">\n          <td width=\"50%\"><p><font face=\"Arial, Helvetica, sans-serif\"><strong>Bill To:</strong></font></p>&lt;BillTo/&gt;</td>\n          <td width=\"50%\"><div align=\"left\"><p><font face=\"Arial, Helvetica, sans-serif\"><strong>Ship To:</strong></font></p>&lt;ShipTo/&gt;</div></td>\n        </tr>\n      </table>\n      <hr>\n      <br>\n      <table width=\"100%%\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\">\n        <tr align=\"left\" valign=\"top\" bgcolor=\"#CCCCCC\">\n          <td width=\"25%\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">Account Manager</font></td>\n          <td width=\"25%\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">Purchase Order</font></td>\n          <td width=\"25%\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">Payment Terms</font></td>\n          <td width=\"25%\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">Date</font></td>\n        </tr>\n        <tr align=\"left\" valign=\"top\">\n          <td width=\"25%\">&nbsp;&lt;AccountManager/&gt;</td>\n          <td width=\"25%\">&nbsp;&lt;PurchaseOrder/&gt;</td>\n          <td width=\"25%\">&nbsp;&lt;PaymentTerms/&gt;</td>\n          <td width=\"25%\">&nbsp;&lt;Date/&gt;</td>\n        </tr>\n      </table>\n      <br>\n      <table width=\"100%%\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\">\n        <tr>\n          <td width=\"15%\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">ITEM</font></td>\n          <td width=\"40%\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">Description</font></td>\n          <td width=\"15%\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">Quantity</font></td>\n          <td width=\"15%\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\" align=\"right\">Price</font></td>\n          <td width=\"15%\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\" align=\"right\">Extended Price</font></td>\n        </tr>\n      </table>\n      <table width=\"100%%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n        <tr>\n          <td>&lt;ProposalDetails/&gt;</td>\n        </tr>\n      </table>\n      <table width=\"100%%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n        <tr>\n          <td width=\"15%\" bordercolor=\"#FFFFFF\">&nbsp;</td>\n          <td width=\"40%\" bordercolor=\"#FFFFFF\">&nbsp;</td>\n          <td width=\"15%\" bordercolor=\"#FFFFFF\">&nbsp;</td>\n          <td>\n            <table width=\"100%%\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\">\n              <tr>\n                <td width=\"50%\" bgcolor=\"#CCCCCC\"><div align=\"right\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\"><strong>Sub Total</strong></font></div></td>\n                <td width=\"50%\"><div align=\"right\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">&lt;SubTotal/&gt;</font></div></td>\n              </tr>\n              <tr>\n                <td width=\"50%\" bgcolor=\"#CCCCCC\"><div align=\"right\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\"><strong>Tax</strong></font></div></td>\n                <td width=\"50%\"><div align=\"right\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">&lt;Tax/&gt;</font></div></td>\n              </tr>\n              <tr>\n                <td width=\"50%\" bgcolor=\"#CCCCCC\"><div align=\"right\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\"><strong>Total</strong></font></div></td>\n                <td width=\"50%\"><div align=\"right\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">&lt;Total/&gt;</font></div></td>\n              </tr>\n            </table>\n          </td>\n        </tr>\n      </table>\n      <p>Special Instructions:</p>\n      <table width=\"400\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\">\n        <tr>\n          <td bgcolor=\"#CCCCCC\"><p>&lt;Notes/&gt;</p><p>&nbsp;</p></td>\n        </tr>\n      </table>\n    </td>\n  </tr>\n</table>\n<p>&nbsp;</p>\n</body>\n</html>','ProposalTemplate',2,2,'PROPOSAL','Proposal');
INSERT INTO `ptdetail` (`ptdetailid`, `ptcategoryid`, `isdefault`, `ptdata`, `ptname`, `userid`, `artifactid`, `pttype`, `ptsubject`) VALUES (3,4,'YES','<HTML>\n<HEAD>\n<TITLE> Ticket Template </TITLE>\n</HEAD>\n<STYLE>\n.popupTableText\n{\n    FONT-SIZE: 65%;\n    COLOR: #334a60;\n    height: 3%;\n    font-family: verdana, arial, sans-serif\n}\n\n.popupTableHead\n{\n    PADDING-RIGHT: 2px;\n    PADDING-LEFT: 2px;\n    FONT-WEIGHT: bold;\n    FONT-SIZE: 65%;\n    height: 3%;\n    COLOR: #334a60;\n    font-family: verdana, arial, sans-serif;\n    BACKGROUND-COLOR: #9ebed1\n}\n\n.popupTableHeadText\n{\n    PADDING-RIGHT: 2px;\n    PADDING-LEFT: 2px;\n    FONT-WEIGHT: bold;\n    FONT-SIZE: 65%;\n    height: 3%;\n    COLOR: #334a60;\n    font-family: verdana, arial, sans-serif\n}\n\n.searchBuilder\n{\n    COLOR: #FFFFFF;\n    font-family: verdana, arial, sans-serif;\n    FONT-WEIGHT: bold;\n    FONT-SIZE: 65%;\n    PADDING-LEFT: 3px;\n    BACKGROUND-COLOR: #7c91b2;\n}\n\n.rowOdd\n{\n    FONT-WEIGHT: bold;\n    FONT-SIZE: 80%;\n    COLOR: #334a60;\n    font-family: verdana, arial, sans-serif;\n}\n\n.rowEven\n{\n    FONT-WEIGHT: bold;\n    FONT-SIZE: 80%;\n    COLOR: #334a60;\n    font-family: verdana, arial, sans-serif;\n}\n\n.contactTableHeadShadow\n{\n    BACKGROUND-COLOR: #7d93b2\n}\n.contactTableHeadShadowLt\n{\n    BACKGROUND-COLOR: #b1d0dc\n}\n.contactTableHeadBottom\n{\n    BACKGROUND-COLOR: #7d93b2\n}\n</STYLE>\n<BODY  marginheight=\"0\" marginwidth=\"0\" topmargin=\"0\" bottommargin=\"0\" leftmargin=\"0\" rightmargin=\"0\" bgcolor=\"#d8dfea\">\n<TABLE width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n<TR>\n                                    <TD colspan=\"2\" >&nbsp;</TD>\n</TR>\n<TR>\n                                    <TD class=\"popupTableText\" >\n                                    <FONT SIZE=\"5\" COLOR=\"#000066\">--Name--</FONT><br>\n                                    --Contact--<br>\n                                    --Address--<br>\n                                    --Email--<br>\n                                    --Phone--<br><br>\n                                    </TD>\n                                    <TD valign=\"top\" align=\"Right\" ><font Color=\"#000066\"><H1>Ticket</H1></font></TD>\n</TR>\n<TR>\n                                    <TD colspan=\"2\" >&nbsp;</TD>\n</TR>\n<TR>\n            <TD valign=\"top\" >\n                        <TABLE width=\"100%\"  cellspacing=\"0\" cellpadding=\"0\"  >\n                        <TR>\n                                    <TD class=\"popupTableHead\"  colspan=\"2\" >Ticket Details</TD>\n                        </TR>\n                        <TR>\n                                    <TD class=\"popupTableText\">Ticket Number:</TD>\n                                    <TD class=\"popupTableText\">--TicketNumber--</TD>\n                        </TR>\n                        <TR>\n                                    <TD class=\"popupTableText\">Status:</TD>\n                                    <TD class=\"popupTableText\">--Status--</TD>\n                        </TR>\n                        <TR>\n                                    <TD class=\"popupTableText\">Priority:</TD>\n                                    <TD class=\"popupTableText\">--Priority--</TD>\n                        </TR>\n                        <TR>\n                                    <TD class=\"popupTableText\">Subject:</TD>\n                                    <TD class=\"popupTableText\">--Subject--</TD>\n                        </TR>\n                        <TR>\n                                    <TD class=\"popupTableText\">Details:</TD>\n                                    <TD class=\"popupTableText\">--Details--</TD>\n                        </TR>\n                        <TR>\n                                    <TD class=\"popupTableHead\" colspan=\"2\">Responsibility</TD>\n                        </TR>\n                        <TR>\n                                    <TD class=\"popupTableText\">Manager:</TD>\n                                    <TD class=\"popupTableText\">--Manager--</TD>\n                        </TR>\n                        <TR>\n                                    <TD class=\"popupTableText\">Assigned To:</TD>\n                                    <TD class=\"popupTableText\">--AssignedTo--</TD>\n                        </TR>\n                        </TABLE>\n            </TD>\n            <TD valign=\"top\">\n                        <TABLE width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" >\n                        <TR>\n                                    <TD class=\"popupTableHead\" colspan=\"2\">Custom Fields</TD>\n                        </TR>\n                        --CustomFields--\n                        </TABLE>\n            </TD>\n</TR>\n<tr><td colspan=\"2\">&nbsp;</td>\n</tr>\n<TR>\n            <TD colspan=\"2\">\n            <table>\n                        <tr height=\"20\">\n                                    <td width=\"11\">&nbsp;</td>\n                                    <td class=\"searchBuilder\">\n                                      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n                                                <tr>\n                                                  <td class=\"searchBuilder\">Threads</td>\n                                                  <td class=\"searchBuilder\" style=\"padding:2px;padding-right:10px;\" align=\"right\">\n                                                  </td>\n                                                </tr>\n                                      </table>\n                                    </td>\n                                    <td width=\"11\"></td>\n                        </tr>\n                        <tr>\n                                    <td width=\"11\"></td>\n                                    <td width=\"100%\">\n                                                <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\">\n                                                            <tr height=\"1\">\n                                                                        <td class=\"contactTableHeadShadow\" colspan=\"5\"></td>\n                                                            </tr>\n                                                            <tr height=\"1\">\n                                                                        <td class=\"contactTableHeadShadowLt\" colspan=\"5\"></td>\n                                                            </tr>\n                                                            <tr>\n                                                                        <td class=\"popupTableHead\">Title</td>\n                                                                        <td class=\"popupTableHead\">Data</td>\n                                                                        <td class=\"popupTableHead\">Date</td>\n                                                                        <td class=\"popupTableHead\">Priority</td>\n                                                                        <td class=\"popupTableHead\">Created By</td>\n                                                            </tr>\n                                                            <tr height=\"1\">\n                                                                        <td class=\"contactTableHeadBottom\" colspan=\"5\"></td>\n                                                            </tr>\n                                                            --Values--\n                                    </tr>\n                                    <tr height=\"1\">\n                                      <td class=\"contactTableHeadBottom\" colspan=\"5\"></td>\n                                    </tr>\n                          </table>\n                        </td>\n                        <td width=\"11\"></td>\n              </tr>\n            </table>\n            </TD>\n</TR>\n</TABLE>\n</BODY>\n</HTML>\n','TicketTemplate',2,2,'TICKET','Ticket');
INSERT INTO `ptdetail` (`ptdetailid`, `ptcategoryid`, `isdefault`, `ptdata`, `ptname`, `userid`, `artifactid`, `pttype`, `ptsubject`) VALUES (98,2,'YES','<P>Dear &lt;FirstName/&gt; &lt;LastName/&gt;,</P>\r\n<P>Thank you for your interest in our products and services. As we discussed, I will be sending you our company brochure and some sample products for your review. Please note that I will be sending this information to the following address:</P>\r\n<P>&lt;Company/&gt;<BR>Attn: &lt;FirstName/&gt; &lt;MiddleInitial/&gt; &lt;LastName/&gt;<BR>&lt;Title/&gt;<BR>&lt;PrimaryAddress/&gt;<BR>&lt;Street1/&gt;<BR>&lt;Street2/&gt;<BR>&lt;City/&gt;, &lt;State/&gt; &lt;Zip/&gt;<BR>&lt;Country/&gt;<BR>Please also help us to make certain we have the correction information on file for your account. Our database currently reflects the following information for your account:</P>\r\n<P>&lt;IndividualID/&gt;<BR>&lt;EntityID/&gt;<BR>&lt;Website/&gt;<BR>&lt;ExternalID/&gt;<BR>&lt;Source/&gt;<BR>&lt;Fax/&gt;<BR>&lt;Home/&gt;<BR>&lt;Main/&gt;<BR>&lt;Mobile/&gt;<BR>&lt;Other/&gt;<BR>&lt;Pager/&gt;<BR>&lt;Work/&gt;<BR>&lt;Email/&gt;</P>\r\n<P>Thank you again for your interest, and I look forward to our follow-up meeting next week. If you have any questions in the mean time, please don\'t hesistate to call me at (215) 555-1212 or send me an email.</P>\r\n<P><BR>Regards,</P>\r\n<P>My Name<BR>Company, Inc<BR><A href=\"mailto:email@example.com\">email@example.com</A></P>\r\n<P>- - - - -</P>\r\n<P>The information transmitted is intended only for the person or entity to which it is addressed and may contain confidential and/or privileged material. Any review, retransmission,&nbsp; dissemination or other use of, or taking of any action in reliance upon, this information by persons or entities other than the intended recipient is prohibited. If you received this in error, please contact the sender and delete the material from any computer.</P>\r\n<P>&nbsp;</P>','Sample Email Template',1,2,'EMAIL','');
INSERT INTO `ptdetail` (`ptdetailid`, `ptcategoryid`, `isdefault`, `ptdata`, `ptname`, `userid`, `artifactid`, `pttype`, `ptsubject`) VALUES (99,1,'YES','<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>Dear Mr Smith <?xml:namespace prefix = o ns = \"urn:schemas-microsoft-com:office:office\" /><o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><FONT size=3><B style=\"mso-bidi-font-weight: normal\"><SPAN style=\"FONT-FAMILY: Tahoma\">Sales Development</SPAN></B><SPAN style=\"FONT-FAMILY: Tahoma\"> <o:p></o:p></SPAN></FONT></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>Companies like yours in the (industry type) sector have achieved significant business growth following the introduction of certain new specialised software technology, by increasing customer visibility, and sales personnel accountability and productivity. <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>Typically, for an investment of less than $7,500 it is possible to achieve sales growth of 20-30% in year one, and 25-50% subsequent years. All directly attributable to this new technology, which focuses on and integrates: <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 0pt 35.35pt; TEXT-INDENT: -14.15pt; mso-list: l0 level1 lfo1; tab-stops: list 35.35pt\"><SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: Symbol; mso-bidi-font-family: StarSymbol\">·<SPAN style=\"FONT: 7pt \'Times New Roman\'\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </SPAN></SPAN><FONT size=3><SPAN style=\"FONT-FAMILY: Tahoma\">New advanced contact management and salesforce automation</SPAN><FONT face=\"Times New Roman\"> </FONT></FONT></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 0pt 35.35pt; TEXT-INDENT: -14.15pt; mso-list: l0 level1 lfo1; tab-stops: list 35.35pt\"><SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: Symbol; mso-bidi-font-family: StarSymbol\">·<SPAN style=\"FONT: 7pt \'Times New Roman\'\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </SPAN></SPAN><FONT size=3><SPAN style=\"FONT-FAMILY: Tahoma\">Provides centralized management of all customer information, transactions and communications</SPAN><FONT face=\"Times New Roman\"> </FONT></FONT></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 0pt 35.35pt; TEXT-INDENT: -14.15pt; mso-list: l0 level1 lfo1; tab-stops: list 35.35pt\"><SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: Symbol; mso-bidi-font-family: StarSymbol\">·<SPAN style=\"FONT: 7pt \'Times New Roman\'\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </SPAN></SPAN><FONT size=3><SPAN style=\"FONT-FAMILY: Tahoma\">Refining and developing your propositions and market sector targeting</SPAN><FONT face=\"Times New Roman\"> </FONT></FONT></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt 35.35pt; TEXT-INDENT: -14.15pt; mso-list: l0 level1 lfo1; tab-stops: list 35.35pt\"><SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: Symbol; mso-bidi-font-family: StarSymbol\">·<SPAN style=\"FONT: 7pt \'Times New Roman\'\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </SPAN></SPAN><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>Time management improvements, especially selling-time optimization <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>If you are interested in assessing the potential and relevance of these ideas for your own business I\'m happy to talk on the phone first and will call you in the next few days. <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>Yours sincerely,<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&nbsp;<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>Your Name, Title<BR style=\"mso-special-character: line-break\"><BR style=\"mso-special-character: line-break\"><o:p></o:p></FONT></SPAN></P><SPAN style=\"FONT-SIZE: 12pt; FONT-FAMILY: Tahoma; mso-fareast-font-family: \'Arial Unicode MS\'; mso-ansi-language: EN-US; mso-bidi-language: AR-SA\">Company Name<BR>(215)555-1212<BR>Email@example.com</SPAN>','Sample Template 2',1,2,'PRINT','');
INSERT INTO `ptdetail` (`ptdetailid`, `ptcategoryid`, `isdefault`, `ptdata`, `ptname`, `userid`, `artifactid`, `pttype`, `ptsubject`) VALUES (100,1,'YES','<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&lt;EntityName/&gt;<?xml:namespace prefix = o ns = \"urn:schemas-microsoft-com:office:office\" /><o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&lt;Address/&gt;<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&nbsp;<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>date <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&nbsp;<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>Dear &lt;FirstName/&gt; <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><FONT face=\"Times New Roman\"><FONT size=3>&nbsp;<o:p></o:p></FONT></FONT></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt; TEXT-ALIGN: center\" align=center><FONT size=3><B style=\"mso-bidi-font-weight: normal\"><SPAN style=\"FONT-FAMILY: Tahoma\">attention grabbing heading</SPAN></B><SPAN style=\"FONT-FAMILY: Tahoma\"> (up to 10 words)<o:p></o:p></SPAN></FONT></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&nbsp;<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>(the heading must grab attention - something your prospect will relate to that your proposition will produce - for example, <B style=\"mso-bidi-font-weight: normal\">Cost-Effective Sales Enquiry Generation</B>, or <B style=\"mso-bidi-font-weight: normal\">Reduce Your Staffing Overheads</B>, or <B style=\"mso-bidi-font-weight: normal\">Fast-Track Management Training</B>) <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><FONT face=\"Times New Roman\"><FONT size=3>&nbsp;<o:p></o:p></FONT></FONT></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>It has come to my attention that you may be in the market for (product/service). I welcome the opportunity to gain a better understanding of your requirements, and to work together with you to determine if our product/service will help you achieve your goals.<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><FONT face=\"Times New Roman\"><FONT size=3>&nbsp;<o:p></o:p></FONT></FONT></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>(Optional) Our customers include (two or three examples, relevant and known to the prospect), who have found (state key benefit, % savings, strategic advantage) from working with us. <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&nbsp;<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>I will telephone you soon to agree a future contact time that suits you/your own review timescale. <o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>&nbsp;<o:p></o:p></FONT></SPAN></P>\r\n<P class=MsoBodyText style=\"MARGIN: 0in 0in 6pt\"><SPAN style=\"FONT-FAMILY: Tahoma\"><FONT size=3>Yours sincerely <o:p></o:p></FONT></SPAN></P><SPAN style=\"FONT-SIZE: 12pt; FONT-FAMILY: Tahoma; mso-fareast-font-family: \'Arial Unicode MS\'; mso-ansi-language: EN-US; mso-bidi-language: AR-SA\">(Name)</SPAN>','Sample Template 3',1,2,'PRINT','');
INSERT INTO `ptdetail` (`ptdetailid`, `ptcategoryid`, `isdefault`, `ptdata`, `ptname`, `userid`, `artifactid`, `pttype`, `ptsubject`) VALUES (101,1,'YES','\r\n<META content=\"OpenOffice.org 1.1.0  (Win32)\" name=GENERATOR>\r\n<META content=\"Alan Rihm\" name=AUTHOR>\r\n<META content=20040210;21092896 name=CREATED>\r\n<META content=\"Alan Rihm\" name=CHANGEDBY>\r\n<META content=20040210;21340454 name=CHANGED>\r\n<STYLE>\r\n	<!--\r\n		@page { size: 8.5in 11in; margin-left: 1.25in; margin-right: 1.25in; margin-top: 1in; margin-bottom: 1in }\r\n		P { margin-bottom: 0.08in }\r\n		TD P { margin-bottom: 0.08in }\r\n	-->\r\n	</STYLE>\r\n\r\n<P style=\"MARGIN-BOTTOM: 0in\"><BR></P>\r\n<CENTER>\r\n<TABLE borderColor=#336699 cellSpacing=2 cellPadding=15 width=600 border=2>\r\n<TBODY>\r\n<TR>\r\n<TD bgColor=#336699 colSpan=2>\r\n<P align=center><FONT color=#ffffff><FONT face=\"verdana, arial\"><B><FONT size=4>Newsletter Name Here </FONT></B><BR>Subtitle Here | Volume # Here | Date Here </FONT></FONT></P></TD></TR>\r\n<TR>\r\n<TD bgColor=#ffffff colSpan=2>\r\n<P><FONT face=\"verdana, arial\">Descriptive text here. Enter your descriptive text here. Descriptive text here. Enter your descriptive text here. Descriptive text here. Enter your descriptive text here. Descriptive text here. Enter your descriptive text here. Descriptive text here. Enter your descriptive text here. </FONT></P></TD></TR>\r\n<TR>\r\n<TD width=200>\r\n<P align=center><IMG height=134 src=\"http://www.lyris.com/img/template_sample_135x135.gif\" width=135 align=bottom border=0 name=Graphic1> </P></TD>\r\n<TD width=400>\r\n<P><FONT color=#336699><FONT face=\"verdana, arial\"><FONT size=4>Announcement! </FONT></FONT></FONT></P>\r\n<P><FONT face=\"verdana, arial\">Announcement text here. Enter your announcement text here. Announcement text here. Enter your announcement text here. Announcement text here. Enter your announcement text here. </FONT></P>\r\n<P><FONT face=\"verdana, arial\"><A href=\"http://www.example.com/\">Name Your Link Here</A> </FONT></P></TD></TR>\r\n<TR>\r\n<TD colSpan=2>\r\n<P><FONT face=\"verdana, arial\"><B><FONT color=#336699>Bold Text #1 </FONT></B><BR>Descriptive text #1 here. Enter your descriptive text #1 here. Descriptive text #1 here. Enter your descriptive text #1 here. Descriptive text #1 here. Enter your descriptive text #1 here. Descriptive text #1 here. Enter your descriptive text #1 here. </FONT></P>\r\n<P><FONT face=\"verdana, arial\"><B><FONT color=#336699>Bold Text #2 </FONT></B><BR>Descriptive text #2 here. Enter your descriptive text #2 here. Descriptive text #2 here. Enter your descriptive text #2 here. Descriptive text #2 here. Enter your descriptive text #2 here. Descriptive text #2 here. Enter your descriptive text #2 here. </FONT></P>\r\n<P><FONT face=\"verdana, arial\"><B><FONT color=#336699>Bold Text #3 </FONT></B><BR>Descriptive text #3 here. Enter your descriptive text #3 here. Descriptive text #3 here. Enter your descriptive text #3 here. Descriptive text #3 here. Enter your descriptive text #3 here. Descriptive text #3 here. Enter your descriptive text #3 here. </FONT></P></TD></TR>\r\n<TR>\r\n<TD colSpan=2>\r\n<P align=center><FONT color=#336699><FONT face=\"verdana, arial\"><FONT size=2>Small footer text here. Enter your small footer text here. Small footer text here. Enter your small footer text here. Small footer text here. </FONT></FONT></FONT></P></TD></TR></TBODY></TABLE></CENTER>\r\n<P style=\"MARGIN-BOTTOM: 0in\"><BR></P>','Sample HTML Email',1,2,'EMAIL','');
INSERT INTO `ptdetail` (`ptdetailid`, `ptcategoryid`, `isdefault`, `ptdata`, `ptname`, `userid`, `artifactid`, `pttype`, `ptsubject`) VALUES (102,1,'YES','\r\n<META content=\"OpenOffice.org 1.1.0  (Win32)\" name=GENERATOR>\r\n<META content=\"Alan Rihm\" name=AUTHOR>\r\n<META content=20040210;21385198 name=CREATED>\r\n<META content=\"Alan Rihm\" name=CHANGEDBY>\r\n<META content=20040210;21392016 name=CHANGED>\r\n<P><BR><BR></P>\r\n<CENTER>\r\n<TABLE borderColor=#336633 cellSpacing=0 cellPadding=10 width=600 bgColor=#99cc99 border=2>\r\n<TBODY>\r\n<TR vAlign=top>\r\n<TD width=\"25%\">\r\n<P><FONT face=arial><FONT size=2><B>Sidebar Title Here </B></FONT></FONT></P>\r\n<P><FONT size=2><FONT face=arial><A href=\"http://www.example.com/\">Name Your Link Here</A><BR><A href=\"http://www.example.com/\">Name Your Link Here</A><BR><A href=\"http://www.example.com/\">Name Your Link Here</A><BR><A href=\"http://www.example.com/\">Name Your Link Here</A><BR><A href=\"http://www.example.com/\">Name Your Link Here</A><BR><A href=\"http://www.example.com/\">Name Your Link Here</A><BR><A href=\"http://www.example.com/\">Name Your Link Here</A><BR><A href=\"http://www.example.com/\">Name Your Link Here</A><BR><A href=\"http://www.example.com/\">Name Your Link Here</A></FONT></FONT></P></TD>\r\n<TD width=\"75%\" bgColor=#ffffff>\r\n<P><FONT color=#336633><FONT face=arial><FONT size=4><B>Announcement Title Here! </B></FONT></FONT></FONT></P>\r\n<P><A href=\"http://www.example.com/\"><FONT color=#000080><IMG height=112 src=\"http://www.lyris.com/img/template_sample_97x112.gif\" width=97 align=right border=1 name=Graphic1></FONT></A><FONT face=arial>Descriptive text #1 here. Enter your descriptive text #1 here. Descriptive text #1 here. Enter your descriptive text #1 here. Descriptive text #1 here. Descriptive text #1 here. Enter your descriptive text #1 here. Descriptive text #1 here. Enter your descriptive text #1 here. Descriptive text #1 here. </FONT></P>\r\n<P><FONT color=#336633><FONT face=arial><B>Bold Text #1 Here </B></FONT></FONT></P>\r\n<UL>\r\n<LI>\r\n<P style=\"MARGIN-BOTTOM: 0in\"><FONT face=arial>Bullet text here, bullet text here </FONT></P>\r\n<LI>\r\n<P style=\"MARGIN-BOTTOM: 0in\"><FONT face=arial>Bullet text here, bullet text here </FONT></P>\r\n<LI>\r\n<P style=\"MARGIN-BOTTOM: 0in\"><FONT face=arial>Bullet text here, bullet text here </FONT></P>\r\n<LI>\r\n<P style=\"MARGIN-BOTTOM: 0in\"><FONT face=arial>Bullet text here, bullet text here </FONT></P>\r\n<LI>\r\n<P style=\"MARGIN-BOTTOM: 0in\"><FONT face=arial>Bullet text here, bullet text here </FONT></P>\r\n<LI>\r\n<P><FONT face=arial>Bullet text here, bullet text here </FONT></P></LI></UL>\r\n<P><FONT color=#336633><FONT face=arial><B>Bold Text #2 Here </B></FONT></FONT></P>\r\n<P><FONT face=arial>Descriptive text #2 here. Enter your descriptive text #2 here. Descriptive text #2 here. Enter your descriptive text #2 here. Descriptive text #2 here. Descriptive text #2 here. Enter your descriptive text #2 here. Descriptive text #2 here. Enter your descriptive text #2 here. Descriptive text #2 here. </FONT></P>\r\n<P align=center><FONT face=arial><A href=\"http://www.example.com/\"><B>Name Your Link Here</B></A> </FONT></P></TD></TR>\r\n<TR>\r\n<TD bgColor=#ffffff colSpan=2></TD></TR></TBODY></TABLE></CENTER>\r\n<P><BR><BR></P>','Sample HTML Email 2',1,2,'EMAIL','');

--
-- Table structure for table 'publicrecords'
--

DROP TABLE IF EXISTS `publicrecords`;
CREATE TABLE `publicrecords` (
  `recordid` int(11) NOT NULL default '0',
  `moduleid` int(11) NOT NULL default '0',
  KEY `recordid` (`recordid`),
  KEY `moduleid` (`moduleid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'publicrecords'
--

INSERT INTO `publicrecords` (`recordid`, `moduleid`) VALUES (1,14);
INSERT INTO `publicrecords` (`recordid`, `moduleid`) VALUES (1,15);
INSERT INTO `publicrecords` (`recordid`, `moduleid`) VALUES (1,32);
INSERT INTO `publicrecords` (`recordid`, `moduleid`) VALUES (1,78);

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
-- Table structure for table 'record'
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
-- Dumping data for table 'record'
--

INSERT INTO `record` (`RecordID`, `ParentID`, `Name`) VALUES (289,1,'fff');
INSERT INTO `record` (`RecordID`, `ParentID`, `Name`) VALUES (298,1,'nnn');
INSERT INTO `record` (`RecordID`, `ParentID`, `Name`) VALUES (299,1,'hhh');
INSERT INTO `record` (`RecordID`, `ParentID`, `Name`) VALUES (440,2,'bbb');
INSERT INTO `record` (`RecordID`, `ParentID`, `Name`) VALUES (597,2,'kkk');

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

INSERT INTO `recordauthorisation` (`individualid`, `recordid`, `recordtypeid`, `privilegelevel`) VALUES (1,9,76,20);

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
  CONSTRAINT `0_21533` FOREIGN KEY (`ReportTypeId`) REFERENCES `reporttype` (`ReportTypeId`),
  CONSTRAINT `0_21534` FOREIGN KEY (`ModuleId`) REFERENCES `module` (`moduleid`)
) TYPE=InnoDB;

--
-- Dumping data for table 'report'
--

INSERT INTO `report` (`ReportId`, `ModuleId`, `Name`, `Description`, `CreatedBy`, `CreatedOn`, `ModifiedBy`, `ModifiedOn`, `ReportURL`, `ReportTypeId`, `DateFrom`, `DateTo`) VALUES (100,14,'Contacts by state','Contacts by state, order by State, Zip Code, City',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL);
INSERT INTO `report` (`ReportId`, `ModuleId`, `Name`, `Description`, `CreatedBy`, `CreatedOn`, `ModifiedBy`, `ModifiedOn`, `ReportURL`, `ReportTypeId`, `DateFrom`, `DateTo`) VALUES (101,14,'Contacts with custom fields list','Contacts with custom fields list by account manager, order by Account Manager, Entity',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL);
INSERT INTO `report` (`ReportId`, `ModuleId`, `Name`, `Description`, `CreatedBy`, `CreatedOn`, `ModifiedBy`, `ModifiedOn`, `ReportURL`, `ReportTypeId`, `DateFrom`, `DateTo`) VALUES (102,52,'Expenses by user','Expenses by user, order by User, Expense Type',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL);
INSERT INTO `report` (`ReportId`, `ModuleId`, `Name`, `Description`, `CreatedBy`, `CreatedOn`, `ModifiedBy`, `ModifiedOn`, `ReportURL`, `ReportTypeId`, `DateFrom`, `DateTo`) VALUES (103,52,'Expenses by entity','Expenses by user, order by User, Expense Type',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL);
INSERT INTO `report` (`ReportId`, `ModuleId`, `Name`, `Description`, `CreatedBy`, `CreatedOn`, `ModifiedBy`, `ModifiedOn`, `ReportURL`, `ReportTypeId`, `DateFrom`, `DateTo`) VALUES (104,52,'Timeslips by user','Timeslips by user, order by User, Entity',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL);
INSERT INTO `report` (`ReportId`, `ModuleId`, `Name`, `Description`, `CreatedBy`, `CreatedOn`, `ModifiedBy`, `ModifiedOn`, `ReportURL`, `ReportTypeId`, `DateFrom`, `DateTo`) VALUES (105,52,'Timeslips by Entity','Timeslips by user, order by Entity, User',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL);
INSERT INTO `report` (`ReportId`, `ModuleId`, `Name`, `Description`, `CreatedBy`, `CreatedOn`, `ModifiedBy`, `ModifiedOn`, `ReportURL`, `ReportTypeId`, `DateFrom`, `DateTo`) VALUES (106,3,'Activities by user','Activities by user, order by User',1,'0000-00-00 00:00:00',NULL,20040308092154,NULL,1,NULL,NULL);
INSERT INTO `report` (`ReportId`, `ModuleId`, `Name`, `Description`, `CreatedBy`, `CreatedOn`, `ModifiedBy`, `ModifiedOn`, `ReportURL`, `ReportTypeId`, `DateFrom`, `DateTo`) VALUES (107,3,'Activities by entity','Activities by entity, order by Entity',1,'0000-00-00 00:00:00',NULL,20040308092154,NULL,1,NULL,NULL);
INSERT INTO `report` (`ReportId`, `ModuleId`, `Name`, `Description`, `CreatedBy`, `CreatedOn`, `ModifiedBy`, `ModifiedOn`, `ReportURL`, `ReportTypeId`, `DateFrom`, `DateTo`) VALUES (108,30,'Opportunitiess by user','Opportunities by user, order by Entity, Account Manager',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL);
INSERT INTO `report` (`ReportId`, `ModuleId`, `Name`, `Description`, `CreatedBy`, `CreatedOn`, `ModifiedBy`, `ModifiedOn`, `ReportURL`, `ReportTypeId`, `DateFrom`, `DateTo`) VALUES (109,30,'Proposal by user','Proposal by user, order by Entity, Account Manager',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL);
INSERT INTO `report` (`ReportId`, `ModuleId`, `Name`, `Description`, `CreatedBy`, `CreatedOn`, `ModifiedBy`, `ModifiedOn`, `ReportURL`, `ReportTypeId`, `DateFrom`, `DateTo`) VALUES (110,30,'Proposal detail by user','Proposal detail by user, order by Entity, Account Manager',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL);
INSERT INTO `report` (`ReportId`, `ModuleId`, `Name`, `Description`, `CreatedBy`, `CreatedOn`, `ModifiedBy`, `ModifiedOn`, `ReportURL`, `ReportTypeId`, `DateFrom`, `DateTo`) VALUES (111,30,'Sales order report','Sales order report, order by Entity, Account Manager',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL);
INSERT INTO `report` (`ReportId`, `ModuleId`, `Name`, `Description`, `CreatedBy`, `CreatedOn`, `ModifiedBy`, `ModifiedOn`, `ReportURL`, `ReportTypeId`, `DateFrom`, `DateTo`) VALUES (112,36,'Project summary','Project summary',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL);
INSERT INTO `report` (`ReportId`, `ModuleId`, `Name`, `Description`, `CreatedBy`, `CreatedOn`, `ModifiedBy`, `ModifiedOn`, `ReportURL`, `ReportTypeId`, `DateFrom`, `DateTo`) VALUES (114,39,'Support tickets per user','Support tickets per user',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL);
INSERT INTO `report` (`ReportId`, `ModuleId`, `Name`, `Description`, `CreatedBy`, `CreatedOn`, `ModifiedBy`, `ModifiedOn`, `ReportURL`, `ReportTypeId`, `DateFrom`, `DateTo`) VALUES (115,39,'Support tickets by entity','Support tickets by entity',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL);
INSERT INTO `report` (`ReportId`, `ModuleId`, `Name`, `Description`, `CreatedBy`, `CreatedOn`, `ModifiedBy`, `ModifiedOn`, `ReportURL`, `ReportTypeId`, `DateFrom`, `DateTo`) VALUES (116,39,'Support tickets list','Support tickets list',1,'0000-00-00 00:00:00',NULL,20040927155029,NULL,1,NULL,NULL);

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
  `tableId` int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (`ReportContentId`),
  KEY `ReportId` (`ReportId`),
  KEY `FieldId` (`FieldId`),
  CONSTRAINT `0_21734` FOREIGN KEY (`FieldId`) REFERENCES `searchfield` (`SearchFieldID`),
  CONSTRAINT `0_21735` FOREIGN KEY (`ReportId`) REFERENCES `report` (`ReportId`) ON DELETE CASCADE
) TYPE=InnoDB;

--
-- Dumping data for table 'reportcontent'
--


--
-- Table structure for table 'reportmodule'
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
-- Dumping data for table 'reportmodule'
--

INSERT INTO `reportmodule` (`ModuleID`, `SearchTableID`) VALUES (3,6);
INSERT INTO `reportmodule` (`ModuleID`, `SearchTableID`) VALUES (14,1);
INSERT INTO `reportmodule` (`ModuleID`, `SearchTableID`) VALUES (14,5);
INSERT INTO `reportmodule` (`ModuleID`, `SearchTableID`) VALUES (15,2);
INSERT INTO `reportmodule` (`ModuleID`, `SearchTableID`) VALUES (15,5);
INSERT INTO `reportmodule` (`ModuleID`, `SearchTableID`) VALUES (30,10);
INSERT INTO `reportmodule` (`ModuleID`, `SearchTableID`) VALUES (31,11);
INSERT INTO `reportmodule` (`ModuleID`, `SearchTableID`) VALUES (33,21);
INSERT INTO `reportmodule` (`ModuleID`, `SearchTableID`) VALUES (36,12);
INSERT INTO `reportmodule` (`ModuleID`, `SearchTableID`) VALUES (37,13);
INSERT INTO `reportmodule` (`ModuleID`, `SearchTableID`) VALUES (38,15);
INSERT INTO `reportmodule` (`ModuleID`, `SearchTableID`) VALUES (39,5);
INSERT INTO `reportmodule` (`ModuleID`, `SearchTableID`) VALUES (39,14);
INSERT INTO `reportmodule` (`ModuleID`, `SearchTableID`) VALUES (42,18);
INSERT INTO `reportmodule` (`ModuleID`, `SearchTableID`) VALUES (48,29);
INSERT INTO `reportmodule` (`ModuleID`, `SearchTableID`) VALUES (52,36);

--
-- Table structure for table 'reportsearch'
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
-- Dumping data for table 'reportsearch'
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
  CONSTRAINT `0_21539` FOREIGN KEY (`ExpressionId`) REFERENCES `reportsearchexpression` (`ExpressionId`) ON DELETE CASCADE,
  CONSTRAINT `0_21540` FOREIGN KEY (`ReportId`) REFERENCES `report` (`ReportId`) ON DELETE CASCADE,
  CONSTRAINT `0_21541` FOREIGN KEY (`FieldId`) REFERENCES `field` (`fieldid`)
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

INSERT INTO `restoresequence` (`moduleid`, `tableid`, `sequence`, `primaryTable`) VALUES (5,111,1,111);
INSERT INTO `restoresequence` (`moduleid`, `tableid`, `sequence`, `primaryTable`) VALUES (6,34,2,33);
INSERT INTO `restoresequence` (`moduleid`, `tableid`, `sequence`, `primaryTable`) VALUES (6,33,1,33);
INSERT INTO `restoresequence` (`moduleid`, `tableid`, `sequence`, `primaryTable`) VALUES (6,36,1,36);
INSERT INTO `restoresequence` (`moduleid`, `tableid`, `sequence`, `primaryTable`) VALUES (6,34,2,36);
INSERT INTO `restoresequence` (`moduleid`, `tableid`, `sequence`, `primaryTable`) VALUES (6,33,3,36);
INSERT INTO `restoresequence` (`moduleid`, `tableid`, `sequence`, `primaryTable`) VALUES (1,10,3,1);
INSERT INTO `restoresequence` (`moduleid`, `tableid`, `sequence`, `primaryTable`) VALUES (1,1,1,1);
INSERT INTO `restoresequence` (`moduleid`, `tableid`, `sequence`, `primaryTable`) VALUES (1,4,4,1);
INSERT INTO `restoresequence` (`moduleid`, `tableid`, `sequence`, `primaryTable`) VALUES (1,2,1,2);
INSERT INTO `restoresequence` (`moduleid`, `tableid`, `sequence`, `primaryTable`) VALUES (1,11,2,2);
INSERT INTO `restoresequence` (`moduleid`, `tableid`, `sequence`, `primaryTable`) VALUES (1,10,3,2);
INSERT INTO `restoresequence` (`moduleid`, `tableid`, `sequence`, `primaryTable`) VALUES (1,4,4,2);
INSERT INTO `restoresequence` (`moduleid`, `tableid`, `sequence`, `primaryTable`) VALUES (1,11,2,1);
INSERT INTO `restoresequence` (`moduleid`, `tableid`, `sequence`, `primaryTable`) VALUES (1,12,5,2);
INSERT INTO `restoresequence` (`moduleid`, `tableid`, `sequence`, `primaryTable`) VALUES (1,12,5,1);
INSERT INTO `restoresequence` (`moduleid`, `tableid`, `sequence`, `primaryTable`) VALUES (10,52,2,52);
INSERT INTO `restoresequence` (`moduleid`, `tableid`, `sequence`, `primaryTable`) VALUES (9,48,1,48);

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
-- Dumping data for table 'search'
--


--
-- Table structure for table 'searchcriteria'
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
-- Table structure for table 'searchfield'
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
-- Dumping data for table 'searchfield'
--

INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (1,1,'Entity ID','EntityID',NULL,'Y',NULL,NULL,'EntityID',0,'Y',NULL,'N');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (2,1,'ID 2','ExternalID',NULL,'Y',NULL,NULL,'ExternalID',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (3,1,'Entity Name','Name',NULL,'Y',NULL,NULL,'Name',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (4,1,'Marketing List','List',NULL,'Y',NULL,NULL,'List',8,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (5,1,'Created By','CONCAT(createdby1.FirstName, \" \", createdby1.LastName)',NULL,'N','individual createdby1','createdby1.IndividualID = entity.Creator','Creator',1,'N',NULL,'N');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (6,1,'Modified By','CONCAT(modifiedby1.FirstName, \" \", modifiedby1.LastName)',NULL,'N','individual modifiedby1','modifiedby1.IndividualID = entity.Modified','ModifiedBy',1,'N',NULL,'N');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (7,1,'Owned By','CONCAT(ownedby1.FirstName, \" \", ownedby1.LastName)',NULL,'N','individual ownedby1','ownedby1.IndividualID = entity.Owner','Owner',1,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (8,1,'Creation Date','Created',NULL,'Y',NULL,NULL,'Created',0,'Y',NULL,'N');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (9,1,'Modified Date','Modified',NULL,'Y',NULL,NULL,'Modified',0,'Y',NULL,'N');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (10,1,'Source','source1.Name',NULL,'N','source source1','source1.SourceID = entity.Source','Source',3,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (11,2,'Individual ID','IndividualID',NULL,'Y',NULL,NULL,'IndividualID',0,'Y',NULL,'N');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (12,2,'ID 2','ExternalID',NULL,'Y',NULL,NULL,'ExternalID',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (13,2,'Entity Name','entity2.Name',NULL,'N','entity entity2','individual.Entity = entity2.EntityID','Entity',7,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (14,2,'First Name','FirstName',NULL,'Y',NULL,NULL,'FirstName',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (15,2,'Last Name','LastName',NULL,'Y',NULL,NULL,'LastName',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (16,2,'Middle Initial','MiddleInitial',NULL,'Y',NULL,NULL,'MiddleInitial',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (17,2,'Title','Title',NULL,'Y',NULL,NULL,'Title',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (18,2,'Marketing List','list',NULL,'Y',NULL,NULL,'list',8,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (19,2,'Creation Date','Created',NULL,'Y',NULL,NULL,'Created',0,'Y',NULL,'N');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (20,2,'Modified Date','Modified',NULL,'Y',NULL,NULL,'Modified',0,'Y',NULL,'N');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (21,2,'Source','source2.Name',NULL,'N','source source2','source2.SourceID = individual.Source','Source',3,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (22,3,'Content','Content',NULL,'Y',NULL,NULL,'Content',0,'Y',NULL,'N');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (23,3,'Sync As','syncas',NULL,'Y',NULL,NULL,'syncas',0,'Y',NULL,'N');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (24,3,'Note','Note',NULL,'Y',NULL,NULL,'Note',0,'Y',NULL,'N');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (25,3,'Type','moctype3.Name',NULL,'N','moctype moctype3','moctype3.MOCTypeID = methodofcontact.MOCType','moctype3.Name',0,'N',NULL,'N');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (26,4,'Street 1','Street1',NULL,'Y',NULL,NULL,'Street1',0,'Y',NULL,'N');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (27,4,'Street 2','Street2',NULL,'Y',NULL,NULL,'Street2',0,'Y',NULL,'N');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (28,4,'State','state',NULL,'Y',NULL,NULL,'state',0,'Y',NULL,'N');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (29,4,'City','City',NULL,'Y',NULL,NULL,'City',0,'Y',NULL,'N');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (30,4,'Zip Code','Zip',NULL,'Y',NULL,NULL,'Zip',0,'Y',NULL,'N');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (31,4,'Country','country',NULL,'Y',NULL,NULL,'country',0,'Y',NULL,'N');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (32,4,'Website','Website',NULL,'Y',NULL,NULL,'Website',0,'Y',NULL,'N');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (33,5,'Name','Name',NULL,'Y',NULL,NULL,'Name',0,'Y',NULL,'N');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (34,5,'Custom Field Type','FieldType',NULL,'Y',NULL,NULL,'FieldType',0,'Y',NULL,'N');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (35,5,'Scalar Value','customfieldscalar5.Value',NULL,'N','customfieldscalar customfieldscalar5','customfieldscalar5.CustomFieldID = customfield.CustomFieldID','customfieldscalar5.Value',0,'N',NULL,'N');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (36,5,'Multiple Value','customfieldvalue5.Value',NULL,'N','customfieldvalue customfieldvalue5','customfieldvalue5.ValueID = customfieldmultiple.ValueID = AND customfieldmultiple.CustomFieldID = customfield.CustomFieldID','customfieldvalue5.Value',0,'N',NULL,'N');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (37,6,'Activity ID','ActivityID',NULL,'Y',NULL,NULL,'ActivityID',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (38,6,'Title','Title',NULL,'Y',NULL,NULL,'Title',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (39,6,'Details','Details',NULL,'Y',NULL,NULL,'Details',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (40,6,'Type','activitytype6.Name',NULL,'N','activitytype activitytype6','activitytype6.TypeID = activity.Type','activitytype6.Name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (41,6,'Status','activitystatus6.Name',NULL,'N','activitystatus activitystatus6','activitystatus6.StatusID = activity.Status','activitystatus6.Name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (42,6,'Priority','activitypriority6.Name',NULL,'N','activitypriority activitypriority6','activitypriority6.PriorityID = activity.Priority','activitypriority6.Name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (43,6,'Owned By','CONCAT(ownedby6.FirstName, \" \", ownedby6.LastName)',NULL,'N','individual ownedby6','ownedby6.IndividualID = activity.Owner','CONCAT(ownedby6.FirstName, \" \", ownedby6.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (44,6,'Created By','CONCAT(createdby6.FirstName, \" \", createdby6.LastName)',NULL,'N','individual createdby6','createdby6.IndividualID = activity.Creator','CONCAT(createdby6.FirstName, \" \", createdby6.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (45,6,'Modified By','CONCAT(modifiedby6.FirstName, \" \", modifiedby6.LastName)',NULL,'N','individual modifiedby6','modifiedby6.IndividualID = activity.ModifiedBy','CONCAT(modifiedby6.FirstName, \" \", modifiedby6.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (46,6,'Creation Date','Created',NULL,'Y',NULL,NULL,'Created',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (47,6,'Modified Date','Modified',NULL,'Y',NULL,NULL,'Modified',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (48,6,'Start Date','Start',NULL,'Y',NULL,NULL,'Start',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (49,6,'End Date','End',NULL,'Y',NULL,NULL,'End',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (50,6,'Notes','Notes',NULL,'Y',NULL,NULL,'Notes',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (51,6,'Visibility','visibility',NULL,'Y',NULL,NULL,'visibility',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (52,7,'Note ID','NoteID',NULL,'Y',NULL,NULL,'NoteID',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (53,7,'Title','Title',NULL,'Y',NULL,NULL,'Title',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (54,7,'Details','Detail',NULL,'Y',NULL,NULL,'Detail',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (55,7,'Created By','CONCAT(createdby7.FirstName, \" \", createdby7.LastName)',NULL,'N','individual createdby7','createdby7.IndividualID = note.Creator','CONCAT(createdby7.FirstName, \" \", createdby7.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (56,7,'Modified By','CONCAT(modifiedby7.FirstName, \" \", modifiedby7.LastName)',NULL,'N','individual modifiedby7','modifiedby7.IndividualID = note.UpdatedBy','CONCAT(modifiedby7.FirstName, \" \", modifiedby7.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (57,7,'Owned By','CONCAT(ownedby7.FirstName, \" \", ownedby7.LastName)',NULL,'N','individual ownedby7','ownedby7.IndividualID = note.Owner','CONCAT(ownedby7.FirstName, \" \", ownedby7.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (58,7,'Creation Date','DateCreated',NULL,'Y',NULL,NULL,'DateCreated',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (59,7,'Modified Date','DateUpdated',NULL,'Y',NULL,NULL,'DateUpdated',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (60,7,'Related Entity','entity7.name',NULL,'N','entity entity7','entity7.EntityID = note.RelateEntity','entity7.name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (61,7,'Related Individual','CONCAT(relatedind7.FirstName, \" \", relatedind7.LastName)',NULL,'N','individual relatedind7','relatedind7.IndividualID = note.RelateIndividual','CONCAT(relatedind7.FirstName, \" \", relatedind7.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (63,8,'Subject','Subject',NULL,'Y','emailmessage','','Subject',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (64,8,'Body','Body',NULL,'Y','emailmessage','','Body',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (66,8,'Sender','MailFrom',NULL,'Y','emailmessage','','MailFrom',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (67,8,'Recipient','Address',NULL,'N','emailrecipient','emailrecipient.MessageID=emailmessage.MessageID','Address',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (68,8,'Message Date','MessageDate',NULL,'Y',NULL,NULL,'MessageDate',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (69,9,'File ID','FileID',NULL,'Y',NULL,NULL,'FileID',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (70,9,'Title','Title',NULL,'Y',NULL,NULL,'Title',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (71,9,'Description','Description',NULL,'Y',NULL,NULL,'Description',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (72,9,'Name','Name',NULL,'Y',NULL,NULL,'Name',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (73,9,'Owned By','CONCAT(ownedby9.FirstName, \" \", ownedby9.LastName)',NULL,'N','individual ownedby9','ownedby9.IndividualID = cvfile.Owner','CONCAT(ownedby9.FirstName, \" \", ownedby9.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (74,9,'Created By','CONCAT(createdby9.FirstName, \" \", createdby9.LastName)',NULL,'N','individual createdby9','createdby9.IndividualID = cvfile.Creator','CONCAT(createdby9.FirstName, \" \", createdby9.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (75,9,'Modified By','CONCAT(modifiedby9.FirstName, \" \", modifiedby9.LastName)',NULL,'N','individual modifiedby9','modifiedby9.IndividualID = cvfile.UpdatedBy','CONCAT(modifiedby9.FirstName, \" \", modifiedby9.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (76,9,'Author','CONCAT(author9.FirstName, \" \", author9.LastName)',NULL,'N','individual author9','author9.IndividualID = cvfile.Author','CONCAT(author9.FirstName, \" \", author9.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (77,9,'Creation Date','Created',NULL,'Y',NULL,NULL,'Created',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (78,9,'Modified Date','Updated',NULL,'Y',NULL,NULL,'Updated',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (79,9,'Version','Version',NULL,'Y',NULL,NULL,'Version',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (80,9,'Status','Status',NULL,'Y',NULL,NULL,'Status',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (81,9,'Visibility','visibility',NULL,'Y',NULL,NULL,'visibility',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (82,9,'Temporary File','IsTemporary',NULL,'Y',NULL,NULL,'IsTemporary',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (83,9,'Related Entity','entity9.name',NULL,'N','entity entity9','entity9.EntityID = cvfile.RelateEntity','entity9.name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (84,9,'Related Individual','0',NULL,'N','individual','individual.IndividualID = cvfile.RelateIndividual','0',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (85,10,'Opportunity ID','OpportunityID',NULL,'Y',NULL,NULL,'OpportunityID',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (86,10,'Title','Title',NULL,'Y',NULL,NULL,'Title',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (87,10,'Description','Description',NULL,'Y',NULL,NULL,'Description',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (88,10,'Type','salestype10.Name',NULL,'N','salestype salestype10','salestype10.SalesTypeID = opportunity.TypeID','salestype10.Name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (89,10,'Status','salesstatus10.Name',NULL,'N','salesstatus salesstatus10','salesstatus10.SalesStatusID = opportunity.Status','salesstatus10.Name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (90,10,'Stage','salesstage10.Name',NULL,'N','salesstage salesstage10','salesstage10.SalesStageID = opportunity.Stage','salesstage10.Name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (91,10,'Probability','salesprobability10.Title',NULL,'N','salesprobability salesprobability10','salesprobability10.ProbabilityID = opportunity.Probability','salesprobability10.Title',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (92,10,'Source','source10.Name',NULL,'N','source source10','source10.SourceID = opportunity.Source','source10.Name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (93,10,'Forecast Amount','ForecastAmmount',NULL,'Y',NULL,NULL,'ForecastAmmount',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (94,10,'Actual Amount','ActualAmount',NULL,'Y',NULL,NULL,'ActualAmount',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (95,10,'Owned By','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.IndividualID = activity.Owner AND activity.ActivityID = opportunity.ActivityID','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (96,10,'Account Manager','CONCAT(accountmgr10.FirstName, \" \", accountmgr10.LastName)',NULL,'N','individual accountmgr10','accountmgr10.IndividualID = opportunity.AccountManager','CONCAT(accountmgr10.FirstName, \" \", accountmgr10.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (97,10,'Entity Name','entity.name',NULL,'N','entity','entity.EntityID = opportunity.EntityID','entity.name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (98,10,'Individual Name','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.IndividualID = opportunity.IndividualID','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (99,10,'Account Team','grouptbl10.Name',NULL,'N','grouptbl grouptbl10','grouptbl10.GroupID = opportunity.AccountTeam','grouptbl10.Name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (100,10,'Estimated Close Date','estcldat10.Start',NULL,'N','activity estcldat10','estcldat10.ActivityID = opportunity.ActivityID','estcldat10.Start',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (101,10,'Actual Close Date','actcldat10.CompletedDate',NULL,'N','activity actcldat10','actcldat10.ActivityID = opportunity.ActivityID','actcldat10.CompletedDate',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (102,11,'Proposal ID','ProposalID',NULL,'Y',NULL,NULL,'ProposalID',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (103,11,'Title','Title',NULL,'Y',NULL,NULL,'Title',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (104,11,'Description','Description',NULL,'Y',NULL,NULL,'Description',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (105,11,'Type','salestype11.Name',NULL,'N','salestype salestype11','salestype11.SalesTypeID = proposal.TypeID','salestype11.Name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (106,11,'Status','salesstatus11.Name',NULL,'N','salesstatus salesstatus11','salesstatus11.SalesStatusID = proposal.Status','salesstatus11.Name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (107,11,'Stage','salesstage11.Name',NULL,'N','salesstage salesstage11','salesstage11.SalesStageID = proposal.Stage','salesstage11.Name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (108,11,'Probability','salesprobability11.Title',NULL,'N','salesprobability salesprobability11','salesprobability11.ProbabilityID = proposal.Probability','salesprobability11.Title',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (109,11,'Source','source11.Name',NULL,'N','source source11','source11.SourceID = proposal.Source','source11.Name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (110,11,'Forecast Amount','ForecastAmmount',NULL,'Y',NULL,NULL,'ForecastAmmount',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (111,11,'Actual Amount','ActualAmount',NULL,'Y',NULL,NULL,'ActualAmount',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (112,11,'Owned By','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.IndividualID = proposal.Owner','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (113,11,'Account Manager','CONCAT(acctmgr11.FirstName, \" \", acctmgr11.LastName)',NULL,'N','individual acctmgr11','acctmgr11.IndividualID = proposal.AccountManager','CONCAT(acctmgr11.FirstName, \" \", acctmgr11.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (114,11,'Account Team','grouptbl11.Name',NULL,'N','grouptbl grouptbl11','grouptbl11.GroupID = proposal.AccountTeam','grouptbl11.Name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (115,11,'Estimated Close Date','EstimatedCloseDate',NULL,'Y',NULL,NULL,'EstimatedCloseDate',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (116,11,'Actual Close Date','ActualCloseDate',NULL,'Y',NULL,NULL,'ActualCloseDate',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (117,12,'Project ID','ProjectID',NULL,'Y',NULL,NULL,'ProjectID',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (118,12,'Title','ProjectTitle',NULL,'Y',NULL,NULL,'ProjectTitle',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (119,12,'Description','Description',NULL,'Y',NULL,NULL,'Description',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (120,12,'Status','projectstatus12.Title',NULL,'N','projectstatus projectstatus12','projectstatus12.StatusID = project.StatusID','projectstatus12.Title',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (121,12,'Start Date','Start',NULL,'Y',NULL,NULL,'Start',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (122,12,'End Date','End',NULL,'Y',NULL,NULL,'End',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (124,12,'Budgeted Hours','BudgetedHours',NULL,'Y',NULL,NULL,'BudgetedHours',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (125,12,'Hours Used','HoursUsed',NULL,'Y',NULL,NULL,'HoursUsed',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (126,12,'Manager','CONCAT(manager12.FirstName, \" \", manager12.LastName)',NULL,'N','individual manager12','manager12.IndividualID = project.Manager','CONCAT(manager12.FirstName, \" \", manager12.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (127,12,'Owner','CONCAT(owner12.FirstName, \" \", owner12.LastName)',NULL,'N','individual owner12','owner12.IndividualID = project.Owner','CONCAT(owner12.FirstName, \" \", owner12.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (128,12,'Created By','CONCAT(createdby12.FirstName, \" \", createdby12.LastName)',NULL,'N','individual createdby12','createdby12.IndividualID = project.Creator','CONCAT(createdby12.FirstName, \" \", createdby12.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (129,12,'Modified By','CONCAT(modifiedby12.FirstName, \" \", modifiedby12.LastName)',NULL,'N','individual modifiedby12','modifiedby12.IndividualID = project.ModifiedBy','CONCAT(modifiedby12.FirstName, \" \", modifiedby12.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (130,12,'Creation Date','Created',NULL,'Y',NULL,NULL,'Created',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (131,12,'Modified Date','Modified',NULL,'Y',NULL,NULL,'Modified',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (132,13,'Task ID','ActivityID',NULL,'Y',NULL,NULL,'ActivityID',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (133,13,'Milestone','Milestone',NULL,'Y',NULL,NULL,'Milestone',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (134,13,'Percent Complete','PercentComplete',NULL,'Y',NULL,NULL,'PercentComplete',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (135,13,'Start Date','std13.Start',NULL,'N','activity std13','std13.ActivityID = task.ActivityID','std13.Start',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (136,13,'Due Date','dud13.DueDate',NULL,'N','activity dud13','dud13.ActivityID = task.ActivityID','dud13.DueDate',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (137,6,'Status','activitystatus6.Name',NULL,'N','activitystatus activitystatus6','activitystatus6.StatusID = activity.Status','activitystatus6.Name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (138,13,'Assigned To','CONCAT(assto13.FirstName, \" \", assto13.LastName)',NULL,'N','individual assto13','assto13.IndividualID = taskassigned.AssignedTo AND taskassigned.TaskID = task.ActivityID','CONCAT(assto13.FirstName, \" \", assto13.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (139,14,'Ticket ID','ticketid',NULL,'Y',NULL,NULL,'ticketid',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (140,14,'Subject','subject',NULL,'Y',NULL,NULL,'subject',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (141,14,'Description','description',NULL,'Y',NULL,NULL,'description',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (142,14,'Assigned To','CONCAT(assto14.FirstName, \" \", assto14.LastName)',NULL,'N','individual assto14','assto14.IndividualID = ticket.assignedto','CONCAT(assto14.FirstName, \" \", assto14.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (143,14,'Manager','CONCAT(mgr14.FirstName, \" \", mgr14.LastName)',NULL,'N','individual mgr14','mgr14.IndividualID = ticket.manager','CONCAT(mgr14.FirstName, \" \", mgr14.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (144,14,'Created By','CONCAT(createdby14.FirstName, \" \", createdby14.LastName)',NULL,'N','individual createdby14','createdby14.IndividualID = ticket.createdby','CONCAT(createdby14.FirstName, \" \", createdby14.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (145,14,'Modified By','CONCAT(modifiedby14.FirstName, \" \", modifiedby14.LastName)',NULL,'N','individual modifiedby14','modifiedby14.IndividualID = ticket.modifiedby','CONCAT(modifiedby14.FirstName, \" \", modifiedby14.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (146,14,'Owned By','CONCAT(ownedby14.FirstName, \" \", ownedby14.LastName)',NULL,'N','individual ownedby14','ownedby14.IndividualID = ticket.owner','CONCAT(ownedby14.FirstName, \" \", ownedby14.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (147,14,'Status','supportstatus14.name',NULL,'N','supportstatus supportstatus14','supportstatus14.statusid = ticket.status','supportstatus14.name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (148,14,'Priority','supportpriority14.name',NULL,'N','supportpriority supportpriority14','supportpriority14.priorityid = ticket.priority','supportpriority14.name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (149,14,'Creation Date','created',NULL,'Y',NULL,NULL,'created',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (150,14,'Modification Date','modified',NULL,'Y',NULL,NULL,'modified',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (151,14,'Open/Closed Status','ocstatus',NULL,'Y',NULL,NULL,'ocstatus',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (152,15,'Timeslip ID','TimeSlipID',NULL,'Y',NULL,NULL,'TimeSlipID',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (153,15,'Description','Description',NULL,'Y',NULL,NULL,'Description',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (154,15,'Duration (in hours)','Hours',NULL,'Y',NULL,NULL,'Hours',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (155,15,'Break Time (in hours)','BreakTime',NULL,'Y',NULL,NULL,'BreakTime',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (156,15,'Date','Date',NULL,'Y',NULL,NULL,'Date',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (157,15,'Start Time','Start',NULL,'Y',NULL,NULL,'Start',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (158,15,'End Time','End',NULL,'Y',NULL,NULL,'End',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (159,15,'Created By','CONCAT(createdby15.FirstName, \" \", createdby15.LastName)',NULL,'N','individual createdby15','createdby15.IndividualID = timeslip.CreatedBy','CONCAT(createdby15.FirstName, \" \", createdby15.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (160,16,'FAQ ID','faqid',NULL,'Y',NULL,NULL,'faqid',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (161,16,'Title','title',NULL,'Y',NULL,NULL,'title',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (162,16,'Detail','detail',NULL,'Y',NULL,NULL,'detail',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (163,16,'Status','status',NULL,'Y',NULL,NULL,'status',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (164,16,'Created By','CONCAT(createdby16.FirstName, \" \", createdby16.LastName)',NULL,'N','individual createdby16','createdby16.IndividualID = faq.createdby','CONCAT(createdby16.FirstName, \" \", createdby16.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (165,16,'Modified By','CONCAT(modifiedby16.FirstName, \" \", modifiedby16.LastName)',NULL,'N','individual modifiedby16','modifiedby16.IndividualID = faq.updatedby','CONCAT(modifiedby16.FirstName, \" \", modifiedby16.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (166,16,'Owned By','CONCAT(ownedby16.FirstName, \" \", ownedby16.LastName)',NULL,'N','individual ownedby16','ownedby16.IndividualID = faq.owner','CONCAT(ownedby16.FirstName, \" \", ownedby16.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (167,16,'Creation Date','created',NULL,'Y',NULL,NULL,'created',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (168,16,'Modification Date','updated',NULL,'Y',NULL,NULL,'updated',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (169,17,'Knowledgebase ID','kbid',NULL,'Y',NULL,NULL,'kbid',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (170,17,'Title','title',NULL,'Y',NULL,NULL,'title',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (171,17,'Detail','detail',NULL,'Y',NULL,NULL,'detail',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (172,17,'Status','status',NULL,'Y',NULL,NULL,'status',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (173,17,'Category','category17.title',NULL,'N','category category17','category17.catid = knowledgebase.category','category17.title',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (174,17,'Created By','CONCAT(createdby17.FirstName, \" \", createdby17.LastName)',NULL,'N','individual createdby17','createdby17.IndividualID = knowledgebase.createdby','CONCAT(createdby17.FirstName, \" \", createdby17.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (175,17,'Modified By','CONCAT(modifiedby17.FirstName, \" \", modifiedby17.LastName)',NULL,'N','individual createdby17','modifiedby17.IndividualID = knowledgebase.updatedby','CONCAT(modifiedby17.FirstName, \" \", modifiedby17.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (176,17,'Owned By','CONCAT(ownedby17.FirstName, \" \", ownedby17.LastName)',NULL,'N','individual ownedby17','ownedby17.IndividualID = knowledgebase.owner','CONCAT(ownedby17.FirstName, \" \", ownedby17.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (177,17,'Creation Date','created',NULL,'Y',NULL,NULL,'created',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (178,17,'Modification Date','updated',NULL,'Y',NULL,NULL,'updated',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (179,18,'Order ID','orderid',NULL,'Y',NULL,NULL,'orderid',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (181,18,'Notes','description',NULL,'Y',NULL,NULL,'description',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (182,18,'Status','accountingstatus18.title',NULL,'N','accountingstatus accountingstatus18','accountingstatus18.statusid = cvorder.status','accountingstatus18.title',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (183,18,'Terms','accountingterms18.title',NULL,'N','accountingterms accountingterms18','accountingterms18.termsid = cvorder.terms','accountingterms18.title',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (184,18,'Subtotal','subtotal',NULL,'Y',NULL,NULL,'subtotal',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (185,18,'Tax','tax',NULL,'Y',NULL,NULL,'tax',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (188,18,'Total','total',NULL,'Y',NULL,NULL,'total',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (189,18,'Owner','CONCAT(owner18.FirstName, \" \", owner18.LastName)',NULL,'N','individual owner18','owner18.IndividualID = cvorder.owner','CONCAT(owner18.FirstName, \" \", owner18.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (190,18,'Created By','CONCAT(cretedby18.FirstName, \" \", cretedby18.LastName)',NULL,'N','individual cretedby18','cretedby18.IndividualID = cvorder.creator','CONCAT(cretedby18.FirstName, \" \", cretedby18.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (191,18,'Modified By','CONCAT(modifiedby18.FirstName, \" \", modifiedby18.LastName)',NULL,'N','individual modifiedby18','modifiedby18.IndividualID = cvorder.modifiedby','CONCAT(modifiedby18.FirstName, \" \", modifiedby18.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (192,18,'Account Manager','CONCAT(acctmgr18.FirstName, \" \", acctmgr18.LastName)',NULL,'N','individual acctmgr18','acctmgr18.IndividualID = cvorder.accountmgr','CONCAT(acctmgr18.FirstName, \" \", acctmgr18.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (193,18,'Creation Date','created',NULL,'Y',NULL,NULL,'created',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (194,18,'Modified Date','modified',NULL,'Y',NULL,NULL,'modified',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (195,18,'Order Date','orderdate',NULL,'Y',NULL,NULL,'orderdate',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (196,18,'P. O. Number','ponumber',NULL,'Y',NULL,NULL,'ponumber',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (197,19,'Invoice ID','InvoiceID',NULL,'Y',NULL,NULL,'InvoiceID',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (198,19,'External ID','ExternalID',NULL,'Y',NULL,NULL,'ExternalID',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (200,19,'Notes','Description',NULL,'Y',NULL,NULL,'Description',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (201,19,'Status','accountingstatus19.title',NULL,'N','accountingstatus accountingstatus19','accountingstatus19.statusid = invoice.Status','accountingstatus19.title',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (202,19,'Terms','accountingterms19.title',NULL,'N','accountingterms accountingterms19','accountingterms19.termsid = invoice.Terms','accountingterms19.title',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (203,19,'Subtotal','SubTotal',NULL,'Y',NULL,NULL,'SubTotal',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (204,19,'Tax','Tax',NULL,'Y',NULL,NULL,'Tax',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (207,19,'Total','Total',NULL,'Y',NULL,NULL,'Total',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (208,19,'Owner','CONCAT(owner19.FirstName, \" \", owner19.LastName)',NULL,'N','individual owner19','owner19.IndividualID = invoice.Owner','CONCAT(owner19.FirstName, \" \", owner19.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (209,19,'Created By','CONCAT(createdby19.FirstName, \" \", createdby19.LastName)',NULL,'N','individual createdby19','createdby19.IndividualID = invoice.Creator','CONCAT(createdby19.FirstName, \" \", createdby19.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (210,19,'Account Manager','CONCAT(acctmgr19.FirstName, \" \", acctmgr19.LastName)',NULL,'N','individual acctmgr19','acctmgr19.IndividualID = invoice.accountmgr','CONCAT(acctmgr19.FirstName, \" \", acctmgr19.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (211,19,'Creation Date','created',NULL,'Y',NULL,NULL,'created',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (212,19,'Modified Date','modified',NULL,'Y',NULL,NULL,'modified',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (213,19,'P. O. Number','ponumber',NULL,'Y',NULL,NULL,'ponumber',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (214,6,'Attendee Name','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.IndividualID = attendee.individualid AND attendee.activityId = activity.activityId','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (215,6,'Entity Name','entity.name',NULL,'N','entity','entity.entityId = activitylink.recordId AND activitylink.recordTypeId = 1 AND activitylink.activityId = activity.activityId','entity.name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (216,6,'Individual Name','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.individualId = activitylink.recordId AND activitylink.recordTypeId = 2 AND activitylink.activityId = activity.activityId','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (217,1,'Account Manager','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.individualid = entity.accountmanagerid','AccountManagerID',4,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (218,1,'Account Team','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.individualid = entity.accountteamid','AccountTeamID',2,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (219,2,'Created By','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.individualid = individual.createdby','CreatedBy',1,'N',NULL,'N');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (220,2,'Modified By','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.individualid = individual.modifiedby','ModifiedBy',1,'N',NULL,'N');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (221,10,'Created By','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.individualid = activity.creator AND activity.activityid = opportunity.activityid','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (222,10,'Modified By','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.individualid = activity.modifiedby AND activity.activityid = opportunity.activityid','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (223,11,'Opportunity','opportunity.name',NULL,'N','opportunity','opportunity.opportunityid = proposal.opportunityid','opportunity.name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (224,11,'Individual','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.individualid = proposal.individualid','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (225,11,'Billing Address','billing',NULL,'Y','','','billing',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (226,11,'Shipping Address','shipping',NULL,'Y','','','shipping',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (227,11,'Terms','terms.termname',NULL,'N','terms','terms.termid = proposal.termid','terms.termname',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (228,11,'Item','proposalitem.name',NULL,'N','proposalitem','propsalitem.proposalid = proposal.proposalid','proposalitem.name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (229,11,'Created By','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.individualid = proposal.createdby','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (230,11,'Modified By','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.individualid = proposal.createdby','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (231,12,'Entity','entity.name',NULL,'N','entity','entity.entityid = projectlink.recordid AND projectlink.recordtypeid = 14 AND projectlink.projectid = project.projectid','entity.name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (232,12,'Individual','CONCAT(ind12.FirstName, \" \", ind12.LastName)',NULL,'N','individual ind12','ind12.individualid = projectlink.recordid AND projectlink.recordtypeid = 15 AND projectlink.projectid = project.projectid','CONCAT(ind12.FirstName, \" \", ind12.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (233,12,'Team','grouptbl.name',NULL,'N','grouptbl','grouptbl.groupid = projectlink.recordid AND projectlink.recordtypeid=16 AND projectlink.projectid = project.projectid','grouptbl.name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (234,12,'Available','project.budgetedhours - project.hoursused',NULL,'N','','','project.budgetedhours - project.hoursused',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (235,13,'Created','created13.created',NULL,'N','activity created13','created13.activityid = task.activityid','created13.created',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (236,13,'Modified','modified13.modified',NULL,'N','activity modified13','modified13.activityid = task.activityid','modified13.modified',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (237,13,'Title','title13.title',NULL,'N','activity title13','title13.activityid = task.activityid','title13.title',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (238,13,'Description','description13.description',NULL,'N','activity description13','description13.activityid = task.activityid','description13.description',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (239,13,'Project','project.title',NULL,'N','project','project.projectid = task.projectid','project.title',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (240,13,'Parent Task','activity.title',NULL,'N','activity','activity.activityid = task.parent','activity.title',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (241,13,'Manager','CONCAT(ind13.FirstName, \" \", ind13.LastName)',NULL,'N','individual ind13','ind13.individualid = activitylink.recordid AND activitylink.recordtypeid = 2 AND activitylink.activityid = task.activityid','CONCAT(ind13.FirstName, \" \", ind13.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (242,13,'Status','activitystatus.name',NULL,'N','activitystatus','activitystatus.statusid = activity.status AND activity.activityid = task.activityid','activitystatus.name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (243,15,'Task','activity.title',NULL,'N','activity','activity.activityid = timeslip.activityid','activity.title',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (244,14,'Entity','entity.name',NULL,'N','entity','entity.entityid = ticket.entityid','entity.name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (245,14,'Individual','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.individualid = ticket.individualid','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (246,14,'Created','created',NULL,'Y','','individual.individualid = ticket.individualid','created',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (247,18,'Created','created',NULL,'Y','','','created',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (248,18,'modified','modified',NULL,'Y','','','modified',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (250,18,'Entity','en18.Name',NULL,'N','entity en18','en18.EntityID = cvorder.en18','en18.Name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (255,18,'Project','proj18.ProjectTitle',NULL,'N','project proj18','proj18.ProjectID = cvorder.project','proj18.ProjectTitle',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (262,19,'OrderID','OrderID',NULL,'Y','','','OrderID',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (263,19,'Creator','CONCAT(indv19.FirstName, \" \", indv19.LastName)',NULL,'N','individual indv19','indv19.IndividualID = invoice.Creator','CONCAT(indv19.FirstName, \" \", indv19.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (264,19,'Modified By','CONCAT(indiv19.FirstName, \" \", indiv19.LastName)',NULL,'N','individual indiv19','indiv19.IndividualID = invoice.`Modified By`','CONCAT(indiv19.FirstName, \" \", indiv19.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (268,19,'Project','proj19.ProjectTitle',NULL,'N','project proj19','proj19.ProjectID = invoice.project','proj19.ProjectTitle',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (269,19,'InvoiceDate','InvoiceDate',NULL,'Y','','','InvoiceDate',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (270,20,'ListID','listid',NULL,'Y','','','listid',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (271,20,'Title','title',NULL,'Y','','','title',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (272,20,'Description','description',NULL,'Y','','','description',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (277,20,'Status','status',NULL,'Y','','','status',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (278,20,'Owner','CONCAT(owner20.FirstName, \" \", owner20.LastName)',NULL,'N','individual owner20','owner20.individualid = marketinglist.owner','CONCAT(owner20.FirstName, \" \", owner20.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (279,20,'Creator','CONCAT(creator20.FirstName, \" \", creator20.LastName)',NULL,'N','individual creator20','creator20.individualid = marketinglist.creator','CONCAT(creator20.FirstName, \" \", creator20.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (280,20,'Modifier','CONCAT(modifier20.FirstName, \" \", modifier20.LastName)',NULL,'N','individual modifier20','modifier20.individualid = marketinglist.modifiedby','CONCAT(modifier20.FirstName, \" \", modifier20.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (281,21,'Title','title',NULL,'Y','','','title',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (282,21,'Description','description',NULL,'Y','','','description',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (283,21,'Start Date','satrtdate',NULL,'Y','','','satrtdate',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (284,21,'End Date','enddate',NULL,'Y','','','enddate',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (287,22,'Title','title',NULL,'Y','','','title',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (290,22,'Entity','entity22.name',NULL,'N','entity entity22','entity22.entityid = activitylink.recordid AND activitylink.recordtypeid = 1 AND activitylink.activityid = literaturerequest.activityid','entity22.name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (291,22,'Individual','CONCAT(ind22.FirstName, \" \", ind22.LastName)',NULL,'N','individual ind22','ind22.individualid = literaturerequest.requestedby','CONCAT(ind22.FirstName, \" \", ind22.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (293,22,'Delivery Method','deliverymethod22.name',NULL,'N','deliverymethod deliverymethod22','deliverymethod22.deliveryid = literaturerequest.deliverymethod','deliverymethod22.name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (298,23,'Start','start',NULL,'Y','','','start',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (299,23,'End','End',NULL,'Y','','','End',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (301,23,'Max attendees','maxattendees',NULL,'Y','','','maxattendees',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (302,23,'Moderator','CONCAT(moderator23.FirstName, \" \", moderator23.LastName)',NULL,'N','individual moderator23','moderator23.individualid = event.moderator','CONCAT(moderator23.FirstName, \" \", moderator23.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (303,24,'PaymentID','paymentid',NULL,'Y','','','paymentid',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (304,24,'ExternalID','externalid',NULL,'Y','','','externalid',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (306,24,'Description','description',NULL,'Y','','','description',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (307,24,'Entity','entity24.name',NULL,'N','entity entity24','entity24.EntityID = payment.EntityID','entity24.name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (308,24,'PaymentMethod','paymentmethod24.title',NULL,'N','paymentmethod paymentmethod24','Paymentmethod24.MethodID = Payment.PaymentMethod','paymentmethod24.title',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (309,24,'Reference','reference',NULL,'Y','','','reference',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (310,24,'Amount','amount',NULL,'Y','','','amount',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (311,24,'Card Type','cardtype',NULL,'Y','','','cardtype',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (312,24,'Card Number','cardnumber',NULL,'Y','','','cardnumber',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (313,24,'Expiration','expiration',NULL,'Y','','','expiration',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (314,24,'Check Number','checknumber',NULL,'Y','','','checknumber',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (315,24,'Owner','CONCAT(owner24.FirstName, \" \", owner24.LastName)',NULL,'N','individual owner24','owner24.individualid = payment.owner','CONCAT(owner24.FirstName, \" \", owner24.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (316,24,'Modified By','CONCAT(individual24.FirstName, \" \", individual24.LastName)',NULL,'N','individual individual24','individual24.IndividualID = payment.modifiedby','CONCAT(individual24.FirstName, \" \", individual24.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (319,24,'Modified','modified',NULL,'Y','','','modified',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (320,25,'ExpenseID','expenseid',NULL,'Y','','','expenseid',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (321,25,'ExternalID','externalid',NULL,'Y','','','externalid',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (322,25,'Title','title',NULL,'Y','','','title',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (323,25,'Description','description',NULL,'Y','','','description',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (324,25,'Entity','entity25.name',NULL,'N','entity entity25','entity25.EntityID = expense.EntityID','entity25.name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (325,25,'Amount','amount',NULL,'Y','','','amount',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (326,25,'Owner','CONCAT(owner25.FirstName, \" \", owner25.LastName)',NULL,'N','individual owner25','owner25.individualid = expense.owner','CONCAT(owner25.FirstName, \" \", owner25.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (327,25,'Created','created',NULL,'Y','','','created',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (328,25,'Modified','modified',NULL,'Y','','','modified',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (329,25,'Status','status',NULL,'Y','','','status',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (330,25,'Project','project25.projecttitle',NULL,'N','project project25','expense.project = project25.projected','project25.projecttitle',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (331,25,'Ticket','ticket25.subject',NULL,'N','ticket ticket25','expense.ticket = ticket25.ticketid','ticket25.subject',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (332,25,'Opportunity','opportunity26.Title',NULL,'N','opportunity opportunity26','expense.opportunity = opportunity26.opportunityid','opportunity26.Title',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (335,25,'GLAccountsID','glaccount25.title',NULL,'N','glaccount glaccount25','glaccount25.glaccountid=Expense.glaccountid','glaccount25.title',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (336,25,'Notes','notes',NULL,'Y','','','notes',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (337,25,'Expense Form ID','expenseformid',NULL,'Y','','','expenseformid',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (338,26,'Purchase Order ID','purchaseorderid',NULL,'Y','','','purchaseorderid',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (339,26,'External ID','externalid',NULL,'Y','','','externalid',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (341,26,'Notes','description',NULL,'Y','','','description',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (343,26,'Entity','entity26.name',NULL,'N','entity entity26','purchaseorder.entity = entity26.entityid','entity26.name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (344,26,'SubTotal','subtotal',NULL,'Y','','','subtotal',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (346,26,'Tax','tax',NULL,'Y','','','tax',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (348,26,'Total','total',NULL,'Y','','','total',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (353,26,'Owner','CONCAT(owner26.FirstName, \" \", owner26.LastName)',NULL,'N','individual owner26','owner26.individualid = purchaseorder.owner','CONCAT(owner26.FirstName, \" \", owner26.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (354,26,'Creator','CONCAT(creator26.FirstName, \" \", creator26.LastName)',NULL,'N','individual creator26','creator26.individualid = purchaseorder.creator','CONCAT(creator26.FirstName, \" \", creator26.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (355,26,'Modified By','CONCAT(individual26.FirstName, \" \", individual26.LastName)',NULL,'N','individual individual26','individual26.IndividualID = purchaseorder.modifiedby','CONCAT(individual26.FirstName, \" \", individual26.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (356,26,'Modified','modified',NULL,'Y','','','modified',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (357,26,'Status','accountingstatus26.title',NULL,'N','accountingstatus accountingstatus26','purchaseorder.status = accountingstatus26.statusid','accountingstatus26.title',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (358,26,'Terms','accountongterms26.title',NULL,'N','accountongterms accountongterms26','purchaseorder.terms  = accountingterms26.termsid','accountongterms26.title',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (359,26,'Account Manager','CONCAT(acctmgr26.FirstName, \" \", acctmgr26.LastName)',NULL,'N','individual acctmgr26','acctmgr26.individualid = purchaseorder.accountmgr','CONCAT(acctmgr26.FirstName, \" \", acctmgr26.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (360,26,'Purchase Order Date','purchaseorderdate',NULL,'Y','','','purchaseorderdate',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (362,26,'Created','created',NULL,'Y','','','created',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (363,27,'Item ID','itemid',NULL,'Y','','','itemid',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (364,27,'External ID','externalid',NULL,'Y','','','externalid',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (365,27,'Title','title',NULL,'Y','','','title',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (366,27,'Description','description',NULL,'Y','','','description',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (367,27,'List Price','listprice',NULL,'Y','','','listprice',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (368,27,'Cost','cost',NULL,'Y','','','cost',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (369,27,'Tax Class','title',NULL,'Y','taxclass','taxclass. taxclass id=Item.taxclass','title',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (370,27,'Type','title',NULL,'Y','itemtipe','itemtype.Itemtypeid = Item.type','title',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (371,27,'Sku','sku',NULL,'Y','','','sku',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (372,27,'Parent','item27.Title',NULL,'N','item item27','item27.itemid = item.parent','item27.Title',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (373,27,'Manufacturer ID','manentity27.name',NULL,'N','entity manentity27','manitem27.manufacturerid = entity.entityid','manentity27.name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (374,27,'Vendor ID','venentity27.name',NULL,'N','entity venentity27','item.vendorid = venentity27.entityid','venentity27.name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (375,27,'Created By','CONCAT(createdby27.FirstName, \" \", createdby27.LastName)',NULL,'N','individual createdby27','createdby27.individualid = item.createdby','CONCAT(createdby27.FirstName, \" \", createdby27.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (376,27,'Modified By','CONCAT(modifiedby27.FirstName, \" \", modifiedby27.LastName)',NULL,'N','individual modifiedby27','modifiedby27.individualid = item.modifiedby','CONCAT(modifiedby27.FirstName, \" \", modifiedby27.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (377,27,'Created Date','createddate',NULL,'Y','','','createddate',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (378,27,'Modified Date','modifieddate',NULL,'Y','','','modifieddate',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (379,27,'GLAccount ID','glaccountid27.title',NULL,'N','glaccount glaccountid27','glaccountid27.glaccountid=item.glaccountid','glaccountid27.title',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (381,27,'Link To Inventory','linktoinventory',NULL,'Y','','','linktoinventory',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (382,28,'Title','title',NULL,'Y','','','title',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (383,28,'Type','type',NULL,'Y','','','type',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (384,28,'Balance','balance',NULL,'Y','','','balance',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (385,28,'Parent Account','glaccount28.title',NULL,'N','glaccount glaccount28','glaccount28.glaccountid=glaccount28.parent','glaccount28.title',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (386,29,'Inventory ID','inventoryid',NULL,'Y','','','inventoryid',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (387,29,'Title','title',NULL,'Y','','','title',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (388,29,'Description','description',NULL,'Y','','','description',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (389,29,'Quantity','qty',NULL,'Y','','','qty',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (390,29,'Location ID','location29.title',NULL,'N','location location29','inventory.locationid=location29.locationid','location29.title',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (391,29,'Item','title',NULL,'Y','','inventory.item=item.itemid','title',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (392,29,'Status ID','inventorystatus29.statusname',NULL,'N','inventorystatus inventorystatus29','inventorystatus29.statusid=inventory.statusid','inventorystatus29.statusname',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (393,29,'Customer ID','entity29.name',NULL,'N','entity entity29','entity29.EntityID = inventory.customerid','entity29.name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (394,29,'Created','created',NULL,'Y','','','created',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (395,29,'Modified','modified',NULL,'Y','','','modified',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (396,29,'Vendor','entityv29.name',NULL,'N','entity entityv29','inventory.vendor = entityv29.entityid','entityv29.name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (399,30,'Entity','entityven30.name',NULL,'N','entity entityven30','vendor.entityid = entityven30.entityid','entityven30.name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (400,31,'Expense Form ID','expenseformid',NULL,'Y','','','expenseformid',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (401,31,'From Date','fromdate',NULL,'Y','','','fromdate',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (402,31,'To Date','todate',NULL,'Y','','','todate',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (403,31,'Description','description',NULL,'Y','','','description',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (404,31,'Note','note',NULL,'Y','','','note',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (405,31,'Reporting To','CONCAT(reportingto31.FirstName, \" \", reportingto31.LastName)',NULL,'N','individual reportingto31','reportingto31.individualid = Expenseform.ReportingTo','CONCAT(reportingto31.FirstName, \" \", reportingto31.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (406,31,'Owner','CONCAT(owner31.FirstName, \" \", owner31.LastName)',NULL,'N','individual owner31','owner31.individualid = Expenseform.owner','CONCAT(owner31.FirstName, \" \", owner31.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (407,31,'Creator','CONCAT(creator31.FirstName, \" \", creator31.LastName)',NULL,'N','individual creator31','creator31.individualid = Expenseform.Creator','CONCAT(creator31.FirstName, \" \", creator31.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (408,31,'Modified By','CONCAT(modifiedby31.FirstName, \" \", modifiedby31.LastName)',NULL,'N','individual modifiedby31','modifiedby31.individualid = Expenseform.modifiedby','CONCAT(modifiedby31.FirstName, \" \", modifiedby31.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (409,31,'Created','created',NULL,'Y','','','created',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (410,31,'Modified','modified',NULL,'Y','','','modified',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (411,31,'Status','status',NULL,'Y','','','status',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (412,32,'Name','CONCAT(name32.FirstName, \" \", name32.LastName)',NULL,'N','individual name32','name32.individualid = attendee.individualid AND attendee.activityid = activity.activityid','CONCAT(name32.FirstName, \" \", name32.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (413,32,'Status','attendeestatus32.name',NULL,'N','attendeestatus attendeestatus32','attendeestatus32.statusid = attendee.status AND attendee.activityid = activity.activityid','attendeestatus32.name',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (414,32,'Type','type',NULL,'Y','','attendee.activityid = activity.activityid','type',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (415,33,'Body','Body',NULL,'Y',NULL,NULL,'Body',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (416,33,'Subject','Subject',NULL,'Y',NULL,NULL,'Subject',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (417,33,'Sender','Sender',NULL,'Y',NULL,NULL,'Sender',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (418,33,'Recipient','Recipient',NULL,'Y',NULL,NULL,'Recipient',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (419,33,'Message Date','MessageDate',NULL,'Y',NULL,NULL,'MessageDate',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (420,34,'Name','name',NULL,'Y',NULL,NULL,'name',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (421,34,'Description','description',NULL,'Y',NULL,NULL,'description',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (422,34,'Enabled','enabled',NULL,'Y',NULL,NULL,'enabled',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (423,8,'Owned By','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.IndividualID = emailmessage.Owner','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (424,8,'From Individual','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.IndividualID = emailmessage.FromIndividual','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (425,8,'Headers','Headers',NULL,'Y',NULL,NULL,'Headers',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (426,8,'Priority','Priority',NULL,'Y',NULL,NULL,'Priority',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (427,8,'Size','Size',NULL,'Y',NULL,NULL,'Size',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (428,8,'Importance','Importance',NULL,'Y',NULL,NULL,'Importance',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (429,22,'Description','Description',NULL,'Y',NULL,NULL,'Description',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (430,22,'Literature','literature.Title',NULL,'N','literature','literature.LiteratureID = literaturerequest.LiteratureID','literature.Title',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (431,22,'Owner','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.IndividualID = activity.Owner AND activity.ActivityID = literaturerequest.ActivityID','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (432,22,'Assigned To','CONCAT(ass22.FirstName, \" \", ass22.LastName)',NULL,'N','individual ass22','ass22.IndividualID = literaturerequest.RequestedBy','CONCAT(ass22.FirstName, \" \", ass22.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (433,21,'PromotionID','PromotionID',NULL,'Y',NULL,NULL,'PromotionID',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (434,21,'Notes','Notes',NULL,'Y',NULL,NULL,'Notes',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (435,21,'Owner','CONCAT(ind21.FirstName, \" \", ind21.LastName)',NULL,'N','individual ind21','ind21.IndividualID = promotion.Owner','CONCAT(ind21.FirstName, \" \", ind21.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (436,23,'EventID','EventID',NULL,'Y',NULL,NULL,'EventID',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (437,23,'Title','Title',NULL,'Y',NULL,NULL,'Title',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (438,23,'Description','Detail',NULL,'Y',NULL,NULL,'Detail',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (439,23,'Who Should Attend','ForMember',NULL,'Y',NULL,NULL,'ForMember',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (440,23,'Owner','CONCAT(own23.FirstName, \" \", own23.LastName)',NULL,'N','individual own23','own23.IndividualID = event.Owner','CONCAT(own23.FirstName, \" \", own23.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (441,23,'Modified By','CONCAT(mod23.FirstName, \" \", mod23.LastName)',NULL,'N','individual mod23','mod23.IndividualID = event.ModifiedBy','CONCAT(mod23.FirstName, \" \", mod23.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (443,36,'Time Sheet ID','TimeSheetID',NULL,'Y',NULL,NULL,'TimeSheetID',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (444,36,'Description','Description',NULL,'Y',NULL,NULL,'Description',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (445,36,'Owner','CONCAT(individual33.FirstName, \" \", individual33.LastName)',NULL,'N','individual individual33','individual33.IndividualID = timesheet.Owner','CONCAT(individual33.FirstName, \" \", individual33.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (446,36,'Creator','CONCAT(Creator33.FirstName, \" \", Creator33.LastName)',NULL,'N','individual Creator33','Creator33.IndividualID = timesheet.Creator','CONCAT(Creator33.FirstName, \" \", Creator33.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (447,36,'Modified By','CONCAT(ModifiedBy33.FirstName, \" \", ModifiedBy33.LastName)',NULL,'N','individual ModifiedBy33','ModifiedBy33.IndividualID = timesheet.ModifiedBy','CONCAT(ModifiedBy33.FirstName, \" \", ModifiedBy33.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (448,36,'Modified By','CONCAT(ReportingTo33.FirstName, \" \", ReportingTo33.LastName)',NULL,'N','individual ReportingTo33','ReportingTo33.IndividualID = timesheet.ReportingTo','CONCAT(ReportingTo33.FirstName, \" \", ReportingTo33.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (449,36,'Created','Created',NULL,'Y',NULL,NULL,'Created',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (450,36,'Modified','Modified',NULL,'Y',NULL,NULL,'Modified',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (451,36,'Start','Start',NULL,'Y',NULL,NULL,'Start',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (452,36,'End','End',NULL,'Y',NULL,NULL,'End',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (453,36,'Notes','Notes',NULL,'Y',NULL,NULL,'Notes',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (455,37,'Creation Date','createDate',NULL,'Y',NULL,NULL,'createDate',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (456,37,'Modified Date','modifyDate',NULL,'Y',NULL,NULL,'modifyDate',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (457,37,'Group ID','GroupID',NULL,'Y',NULL,NULL,'GroupID',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (458,37,'Name','Name',NULL,'Y',NULL,NULL,'Name',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (459,37,'Description','Description',NULL,'Y',NULL,NULL,'Description',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (460,37,'Owned By','CONCAT(Owned16.FirstName, \" \", Owned16.LastName)',NULL,'N','individual Owned16','Owned16.IndividualID = grouptbl.owner','CONCAT(Owned16.FirstName, \" \", Owned16.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (461,28,'GLAccounts ID','GLAccountsID',NULL,'Y',NULL,NULL,'GLAccountsID',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (462,28,'External ID','externalID',NULL,'Y',NULL,NULL,'externalID',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (463,28,'Name','Name',NULL,'Y',NULL,NULL,'Name',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (464,28,'Description','Description',NULL,'Y',NULL,NULL,'Description',0,'Y',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (465,35,'Individual','CONCAT(individual.FirstName, \" \", individual.LastName)',NULL,'N','individual','individual.entity = 1','CONCAT(individual.FirstName, \" \", individual.LastName)',0,'N',NULL,'Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (466,1,'Email','mocEmail.Content',NULL,'N','mocrelate mocRelateEmail,methodofcontact mocEmail','entity.entityID = mocRelateEmail.ContactId AND mocRelateEmail.MOCID = mocEmail.MOCID AND mocRelateEmail.ContactType = 1 AND mocEmail.MOCType = 1','mocEmail.Content',0,'N',' AND mocRelateEmail.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (467,1,'Fax','mocFax.Content',NULL,'N','mocrelate mocRelateFax,methodofcontact mocFax','entity.entityID = mocRelateFax.ContactId AND mocRelateFax.MOCID = mocFax.MOCID AND mocRelateFax.ContactType = 1 AND mocFax.MOCType = 2','mocFax.Content',6,'N',' AND mocRelateFax.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (468,1,'Mobile','mocMobile.Content',NULL,'N','mocrelate mocRelateMobile,methodofcontact mocMobile','entity.entityID = mocRelateMobile.ContactId AND mocRelateMobile.MOCID = mocMobile.MOCID AND mocRelateMobile.ContactType = 1 AND mocMobile.MOCType = 3','mocMobile.Content',6,'N',' AND mocRelateMobile.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (469,1,'Main','mocMain.Content',NULL,'N','mocrelate mocRelateMain,methodofcontact mocMain','entity.entityID = mocRelateMain.ContactId AND mocRelateMain.MOCID = mocMain.MOCID AND mocRelateMain.ContactType = 1 AND mocMain.MOCType = 4','mocMain.Content',6,'N',' AND mocRelateMain.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (470,1,'Home','mocHome.Content',NULL,'N','mocrelate mocRelateHome,methodofcontact mocHome','entity.entityID = mocRelateHome.ContactId AND mocRelateHome.MOCID = mocHome.MOCID AND mocRelateHome.ContactType = 1 AND mocHome.MOCType = 5','mocHome.Content',6,'N',' AND mocRelateHome.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (471,1,'Other','mocOther.Content',NULL,'N','mocrelate mocRelateOther,methodofcontact mocOther','entity.entityID = mocRelateOther.ContactId AND mocRelateOther.MOCID = mocOther.MOCID AND mocRelateOther.ContactType = 1 AND mocOther.MOCType = 6','mocOther.Content',6,'N',' AND mocRelateOther.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (472,1,'Pager','mocPager.Content',NULL,'N','mocrelate mocRelatePager,methodofcontact mocPager','entity.entityID = mocRelatePager.ContactId AND mocRelatePager.MOCID = mocPager.MOCID AND mocRelatePager.ContactType = 1 AND mocPager.MOCType = 7','mocPager.Content',6,'N',' AND mocRelatePager.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (473,1,'Work','mocWork.Content',NULL,'N','mocrelate mocRelateWork,methodofcontact mocWork','entity.entityID = mocRelateWork.ContactId AND mocRelateWork.MOCID = mocWork.MOCID AND mocRelateWork.ContactType = 1 AND mocWork.MOCType = 8','mocWork.Content',6,'N',' AND mocRelateWork.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (474,2,'Email','mocEmail.Content',NULL,'N','mocrelate mocRelateEmail,methodofcontact mocEmail','individual.IndividualID = mocRelateEmail.ContactId AND mocRelateEmail.MOCID = mocEmail.MOCID AND mocRelateEmail.ContactType = 2 AND mocEmail.MOCType = 1','mocEmail.Content',0,'N',' AND mocRelateEmail.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (475,2,'Fax','mocFax.Content',NULL,'N','mocrelate mocRelateFax,methodofcontact mocFax','individual.IndividualID = mocRelateFax.ContactId AND mocRelateFax.MOCID = mocFax.MOCID AND mocRelateFax.ContactType = 2 AND mocFax.MOCType = 2','mocFax.Content',6,'N',' AND mocRelateFax.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (476,2,'Mobile','mocMobile.Content',NULL,'N','mocrelate mocRelateMobile,methodofcontact mocMobile','individual.IndividualID = mocRelateMobile.ContactId AND mocRelateMobile.MOCID = mocMobile.MOCID AND mocRelateMobile.ContactType = 2 AND mocMobile.MOCType = 3','mocMobile.Content',6,'N',' AND mocRelateMobile.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (477,2,'Main','mocMain.Content',NULL,'N','mocrelate mocRelateMain,methodofcontact mocMain','individual.IndividualID = mocRelateMain.ContactId AND mocRelateMain.MOCID = mocMain.MOCID AND mocRelateMain.ContactType = 2 AND mocMain.MOCType = 4','mocMain.Content',6,'N',' AND mocRelateMain.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (478,2,'Home','mocHome.Content',NULL,'N','mocrelate mocRelateHome,methodofcontact mocHome','individual.IndividualID = mocRelateHome.ContactId AND mocRelateHome.MOCID = mocHome.MOCID AND mocRelateHome.ContactType = 2 AND mocHome.MOCType = 5','mocHome.Content',6,'N',' AND mocRelateHome.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (479,2,'Other','mocOther.Content',NULL,'N','mocrelate mocRelateOther,methodofcontact mocOther','individual.IndividualID = mocRelateOther.ContactId AND mocRelateOther.MOCID = mocOther.MOCID AND mocRelateOther.ContactType = 2 AND mocOther.MOCType = 6','mocOther.Content',6,'N',' AND mocRelateOther.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (480,2,'Pager','mocPager.Content',NULL,'N','mocrelate mocRelatePager,methodofcontact mocPager','individual.IndividualID = mocRelatePager.ContactId AND mocRelatePager.MOCID = mocPager.MOCID AND mocRelatePager.ContactType = 2 AND mocPager.MOCType = 7','mocPager.Content',6,'N',' AND mocRelatePager.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (481,2,'Work','mocWork.Content',NULL,'N','mocrelate mocRelateWork,methodofcontact mocWork','individual.IndividualID = mocRelateWork.ContactId AND mocRelateWork.MOCID = mocWork.MOCID AND mocRelateWork.ContactType = 2 AND mocWork.MOCType = 8','mocWork.Content',6,'N',' AND mocRelateWork.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (482,1,'Street 1','addressStreet1.Street1',NULL,'N','addressrelate addressRelateStreet1,address addressStreet1','entity.entityID = addressRelateStreet1.Contact AND addressRelateStreet1.Address = addressStreet1.AddressID AND addressRelateStreet1.ContactType = 1','addressStreet1.Street1',0,'N',' AND addressRelateStreet1.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (483,1,'Street 2','addressStreet2.Street2',NULL,'N','addressrelate addressRelateStreet2,address addressStreet2','entity.entityID = addressRelateStreet2.Contact AND addressRelateStreet2.Address = addressStreet2.AddressID AND addressRelateStreet2.ContactType = 1','addressStreet2.Street2',0,'N',' AND addressRelateStreet2.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (484,1,'State','addressState.state',NULL,'N','addressrelate addressRelateState,address addressState','entity.entityID = addressRelateState.Contact AND addressRelateState.Address = addressState.AddressID AND addressRelateState.ContactType = 1','addressState.state',0,'N',' AND addressRelateState.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (485,1,'Zip Code','addressZip.Zip',NULL,'N','addressrelate addressRelateZip,address addressZip','entity.entityID = addressRelateZip.Contact AND addressRelateZip.Address = addressZip.AddressID AND addressRelateZip.ContactType = 1','addressZip.Zip',0,'N',' AND addressRelateZip.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (486,1,'Country','addressCountry.country',NULL,'N','addressrelate addressRelateCountry,address addressCountry','entity.entityID = addressRelateCountry.Contact AND addressRelateCountry.Address = addressCountry.AddressID AND addressRelateCountry.ContactType = 1','addressCountry.country',0,'N',' AND addressRelateCountry.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (487,1,'Website','addressWebsite.Website',NULL,'N','addressrelate addressRelateWebsite,address addressWebsite','entity.entityID = addressRelateWebsite.Contact AND addressRelateWebsite.Address = addressWebsite.AddressID AND addressRelateWebsite.ContactType = 1','addressWebsite.Website',0,'N',' AND addressRelateWebsite.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (488,2,'Street 1','addressStreet1.Street1',NULL,'N','addressrelate addressRelateStreet1,address addressStreet1','individual.IndividualID  = addressRelateStreet1.Contact AND addressRelateStreet1.Address = addressStreet1.AddressID AND addressRelateStreet1.ContactType = 2','addressStreet1.Street1',0,'N',' AND addressRelateStreet1.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (489,2,'Street 2','addressStreet2.Street2',NULL,'N','addressrelate addressRelateStreet2,address addressStreet2','individual.IndividualID  = addressRelateStreet2.Contact AND addressRelateStreet2.Address = addressStreet2.AddressID AND addressRelateStreet2.ContactType = 2','addressStreet2.Street2',0,'N',' AND addressRelateStreet2.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (490,2,'State','addressState.state',NULL,'N','addressrelate addressRelateState,address addressState','individual.IndividualID  = addressRelateState.Contact AND addressRelateState.Address = addressState.AddressID AND addressRelateState.ContactType = 2','addressState.state',0,'N',' AND addressRelateState.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (491,2,'Zip Code','addressZip.Zip',NULL,'N','addressrelate addressRelateZip,address addressZip','individual.IndividualID  = addressRelateZip.Contact AND addressRelateZip.Address = addressZip.AddressID AND addressRelateZip.ContactType = 2','addressZip.Zip',0,'N',' AND addressRelateZip.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (492,2,'Country','addressCountry.country',NULL,'N','addressrelate addressRelateCountry,address addressCountry','individual.IndividualID  = addressRelateCountry.Contact AND addressRelateCountry.Address = addressCountry.AddressID AND addressRelateCountry.ContactType = 2','addressCountry.country',0,'N',' AND addressRelateCountry.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (493,2,'Website','addressWebsite.Website',NULL,'N','addressrelate addressRelateWebsite,address addressWebsite','individual.IndividualID  = addressRelateWebsite.Contact AND addressRelateWebsite.Address = addressWebsite.AddressID AND addressRelateWebsite.ContactType = 2','addressWebsite.Website',0,'N',' AND addressRelateWebsite.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (494,18,'Street 1','addressStreet1.Street1',NULL,'N','address addressStreet1','(addressStreet1.AddressID = cvorder.BillAddress OR addressStreet1.AddressID = cvorder.ShipAddress)','addressStreet1.Street1',0,'N',' AND addressRelateStreet1.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (495,18,'Street 2','addressStreet2.Street2',NULL,'N','address addressStreet2','(addressStreet2.AddressID = cvorder.BillAddress OR addressStreet2.AddressID = cvorder.ShipAddress)','addressStreet2.Street2',0,'N',' AND addressRelateStreet2.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (496,18,'State','addressState.state',NULL,'N','address addressState','(addressState.AddressID = cvorder.BillAddress OR addressState.AddressID = cvorder.ShipAddress)','addressState.state',0,'N',' AND addressRelateState.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (497,18,'Zip Code','addressZip.Zip',NULL,'N','address addressZip','(addressZip.AddressID = cvorder.BillAddress OR addressZip.AddressID = cvorder.ShipAddress)','addressZip.Zip',0,'N',' AND addressRelateZip.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (498,18,'Country','addressCountry.country',NULL,'N','address addressCountry','(addressCountry.AddressID = cvorder.BillAddress OR addressCountry.AddressID = cvorder.ShipAddress)','addressCountry.country',0,'N',' AND addressRelateCountry.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (499,18,'Website','addressWebsite.Website',NULL,'N','address addressWebsite','(addressWebsite.AddressID = cvorder.BillAddress OR addressWebsite.AddressID = cvorder.ShipAddress)','addressWebsite.Website',0,'N',' AND addressRelateWebsite.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (500,19,'Street 1','addressStreet1.Street1',NULL,'N','address addressStreet1','(addressStreet1.AddressID = invoice.billaddress OR addressStreet1.AddressID = invoice.shipaddress)','addressStreet1.Street1',0,'N',' AND addressRelateStreet1.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (501,19,'Street 2','addressStreet2.Street2',NULL,'N','address addressStreet2','(addressStreet2.AddressID = invoice.billaddress OR addressStreet2.AddressID = invoice.shipaddress)','addressStreet2.Street2',0,'N',' AND addressRelateStreet2.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (502,19,'State','addressState.state',NULL,'N','address addressState','(addressState.AddressID = invoice.billaddress OR addressState.AddressID = invoice.shipaddress)','addressState.state',0,'N',' AND addressRelateState.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (503,19,'Zip Code','addressZip.Zip',NULL,'N','address addressZip','(addressZip.AddressID = invoice.billaddress OR addressZip.AddressID = invoice.shipaddress)','addressZip.Zip',0,'N',' AND addressRelateZip.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (504,19,'Country','addressCountry.country',NULL,'N','address addressCountry','(addressCountry.AddressID = invoice.billaddress OR addressCountry.AddressID = invoice.shipaddress)','addressCountry.country',0,'N',' AND addressRelateCountry.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (505,19,'Website','addressWebsite.Website',NULL,'N','address addressWebsite','(addressWebsite.AddressID = invoice.billaddress OR addressWebsite.AddressID = invoice.shipaddress)','addressWebsite.Website',0,'N',' AND addressRelateWebsite.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (506,26,'Street 1','addressStreet1.Street1',NULL,'N','address addressStreet1','(addressStreet1.AddressID = purchaseorder.BillAddress OR addressStreet1.AddressID = purchaseorder.ShipAddress)','addressStreet1.Street1',0,'N',' AND addressRelateStreet1.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (507,26,'Street 2','addressStreet2.Street2',NULL,'N','address addressStreet2','(addressStreet2.AddressID = purchaseorder.BillAddress OR addressStreet2.AddressID = purchaseorder.ShipAddress)','addressStreet2.Street2',0,'N',' AND addressRelateStreet2.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (508,26,'State','addressState.state',NULL,'N','address addressState','(addressState.AddressID = purchaseorder.BillAddress OR addressState.AddressID = purchaseorder.ShipAddress)','addressState.state',0,'N',' AND addressRelateState.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (509,26,'Zip Code','addressZip.Zip',NULL,'N','address addressZip','(addressZip.AddressID = purchaseorder.BillAddress OR addressZip.AddressID = purchaseorder.ShipAddress)','addressZip.Zip',0,'N',' AND addressRelateZip.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (510,26,'Country','addressCountry.country',NULL,'N','address addressCountry','(addressCountry.AddressID = purchaseorder.BillAddress OR addressCountry.AddressID = purchaseorder.ShipAddress)','addressCountry.country',0,'N',' AND addressRelateCountry.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (511,26,'Website','addressWebsite.Website',NULL,'N','address addressWebsite','(addressWebsite.AddressID = purchaseorder.BillAddress OR addressWebsite.AddressID = purchaseorder.ShipAddress)','addressWebsite.Website',0,'N',' AND addressRelateWebsite.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (512,11,'Street 1','addressStreet1.Street1',NULL,'N','address addressStreet1','(addressStreet1.AddressID = proposal.Billingid OR addressStreet1.AddressID = proposal.Shippingid)','addressStreet1.Street1',0,'N',' AND addressRelateStreet1.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (513,11,'Street 2','addressStreet2.Street2',NULL,'N','address addressStreet2','(addressStreet2.AddressID = proposal.Billingid OR addressStreet2.AddressID = proposal.Shippingid)','addressStreet2.Street2',0,'N',' AND addressRelateStreet2.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (514,11,'State','addressState.state',NULL,'N','address addressState','(addressState.AddressID = proposal.Billingid OR addressState.AddressID = proposal.Shippingid)','addressState.state',0,'N',' AND addressRelateState.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (515,11,'Zip Code','addressZip.Zip',NULL,'N','address addressZip','(addressZip.AddressID = proposal.Billingid OR addressZip.AddressID = proposal.Shippingid)','addressZip.Zip',0,'N',' AND addressRelateZip.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (516,11,'Country','addressCountry.country',NULL,'N','address addressCountry','(addressCountry.AddressID = proposal.Billingid OR addressCountry.AddressID = proposal.Shippingid)','addressCountry.country',0,'N',' AND addressRelateCountry.IsPrimary = \'YES\'','Y');
INSERT INTO `searchfield` (`SearchFieldID`, `SearchTableID`, `DisplayName`, `FieldName`, `FieldPermissionQuery`, `IsOnTable`, `RealTableName`, `RelationshipQuery`, `RealFieldName`, `FieldType`, `IsOnGobalReplaceTable`, `SubRelationshipQuery`, `IsGobalReplaceField`) VALUES (517,11,'Website','addressWebsite.Website',NULL,'N','address addressWebsite','(addressWebsite.AddressID = proposal.Billingid OR addressWebsite.AddressID = proposal.Shippingid)','addressWebsite.Website',0,'N',' AND addressRelateWebsite.IsPrimary = \'YES\'','Y');

--
-- Table structure for table 'searchmodule'
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
-- Dumping data for table 'searchmodule'
--

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (2,8,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (3,6,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (5,1,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (5,2,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (5,7,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (6,9,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (14,1,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (14,2,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (14,5,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (14,6,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (14,7,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (14,8,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (14,9,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (14,10,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (14,11,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (14,12,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (14,13,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (14,14,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (14,15,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (14,18,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (14,19,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (15,1,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (15,2,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (15,5,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (15,6,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (15,7,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (15,8,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (15,9,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (15,10,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (15,11,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (15,12,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (15,13,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (15,14,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (15,15,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (15,18,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (15,19,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (16,2,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (16,37,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (17,6,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (18,6,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (21,6,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (22,6,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (23,6,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (30,10,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (30,11,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (31,10,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (31,11,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (32,1,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (32,2,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (32,20,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (33,21,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (33,27,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (34,6,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (34,22,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (35,1,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (35,2,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (35,23,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (36,9,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (36,12,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (36,13,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (36,15,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (37,13,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (37,15,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (38,15,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (39,5,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (39,14,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (39,15,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (40,16,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (41,17,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (42,18,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (42,19,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (42,27,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (43,24,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (44,25,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (44,27,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (45,26,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (45,27,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (46,27,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (47,28,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (48,29,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (50,1,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (50,2,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (50,30,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (51,25,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (51,31,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (52,36,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (54,35,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (56,19,'Y');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (56,27,'N');
INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`) VALUES (79,34,'Y');

--
-- Table structure for table 'searchtable'
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
-- Dumping data for table 'searchtable'
--

INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (1,'Entity','entity','EntityID',NULL,'Y');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (2,'Individual','individual','IndividualID',NULL,'Y');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (3,'Method Of Contact','methodofcontact','MOCID',NULL,'N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (4,'Address','address','AddressID',NULL,'N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (5,'Custom Fields','customfield','CustomFieldID',NULL,'N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (6,'Activities','activity','ActivityID',NULL,'N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (7,'Notes','note','NoteID',NULL,'N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (8,'Email','emailmessage','MessageID',NULL,'N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (9,'Files','cvfile','FileID',NULL,'N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (10,'Opportunities','opportunity','OpportunityID',NULL,'N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (11,'Proposals','proposal','ProposalID',NULL,'N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (12,'Projects','project','ProjectID',NULL,'N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (13,'Tasks','task','ActivityID',NULL,'N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (14,'Tickets','ticket','ticketid',NULL,'N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (15,'Timeslips','timeslip','TimeSlipID',NULL,'N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (16,'FAQs','faq','faqid',NULL,'N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (17,'Knowledgebase','knowledgebase','kbid',NULL,'N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (18,'Order History','cvorder','orderid',NULL,'N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (19,'Invoice History','invoice','InvoiceID',NULL,'N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (20,'Marketing List','marketinglist','ListId','','N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (21,'Promotions','promotion','PromotionID','','N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (22,'Literature Request','literaturerequest','ActivityID','','N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (23,'Event','event','EventId','','N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (24,'Payment','payment','paymentid','','N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (25,'Expense','expense','expenseid','','N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (26,'Purchase Order','purchaseorder','purchaseorderid','','N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (27,'Item','item','itemid','','N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (28,'GLAccount','glaccount','glaccountsid','','N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (29,'Inventory','inventory','inventoryid','','N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (30,'Vendor','vendor','entityid','','N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (31,'Expense Form','expenseform','expenseformid','','N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (32,'Attendee','attendee','ActivityID','','N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (33,'RuleCriteria','','',NULL,'N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (34,'Rule','emailrule','ruleID',NULL,'N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (35,'Employee','individual','IndividualID',NULL,'N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (36,'TimeSheet','timesheet','TimeSheetID',NULL,'N');
INSERT INTO `searchtable` (`SearchTableID`, `DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`, `IsOnGobalReplaceTable`) VALUES (37,'Group','grouptbl','GroupID',NULL,'N');

--
-- Table structure for table 'searchtablerelate'
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
-- Dumping data for table 'searchtablerelate'
--

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (2,1,'individual.Entity = entity.entityID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (6,1,'activity.ActivityID = activitylink.ActivityID AND activitylink.RecordTypeID = 1 AND activitylink.RecordID = entity.EntityID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (6,2,'activity.ActivityID = activitylink.ActivityID AND activitylink.RecordTypeID = 2 AND activitylink.RecordID = individual.IndividualID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (7,1,'note.RelateEntity = entity.EntityID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (7,2,'note.RelateIndividual = individual.IndividualID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (8,1,'emailmessage.AccountID = emailaccount.AccountID AND emailaccount.Owner = individual.IndividualID AND individual.Entity = entity.EntityID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (8,2,'emailmessage.AccountID = emailaccount.AccountID AND emailaccount.Owner = individual.IndividualID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (9,1,'cvfile.RelateEntity = entity.EntityID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (9,2,'cvfile.RelateIndividual = individual.IndividualID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (10,1,'opportunity.EntityID = entity.EntityID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (10,2,'opportunity.IndividualID = individual.IndividualID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (11,1,'proposal.OpportunityID = opportunity.OpportunityID AND opportunity.EntityID = entity.EntityID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (11,2,'proposal.OpportunityID = opportunity.OpportunityID AND opportunity.IndividualID = individual.IndividualID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (11,10,'proposal.OpportunityID = opportunity.OpportunityID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (12,1,'project.ProjectID = projectlink.ProjectID AND projectlink.RecordTypeID = 14 AND projectlink.RecordID = entity.EntityID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (12,2,'project.Owner = individual.IndividualID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (12,9,'cvfile.FileID = cvfilelink.FileID and cvfilelink.RecordTypeID = 36 AND project.ProjectID = cvfilelink.RecordID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (13,1,'task.ProjectID = project.ProjectID AND project.ProjectID = projectlink.ProjectID AND projectlink.RecordTypeID = 14 AND projectlink.RecordID = entity.EntityID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (13,2,'task.ProjectID = project.ProjectID AND project.Owner = individual.IndividualID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (13,12,'task.ProjectID = project.ProjectID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (14,1,'ticket.entityid = entity.EntityID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (14,2,'ticket.individualid = individual.IndividualID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (15,1,'timeslip.ProjectID = project.ProjectID AND project.ProjectID = projectlink.ProjectID AND projectlink.RecordTypeID = 14 AND projectlink.RecordID = entity.EntityID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (15,2,'timeslip.ProjectID = project.ProjectID AND project.Owner = individual.IndividualID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (15,12,'timeslip.ProjectID = project.ProjectID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (15,13,'timeslip.ActivityID = task.ActivityID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (15,14,'timeslip.TicketID = ticket.ticketid');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (18,1,'cvorder.entityid = entity.EntityID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (18,2,'cvorder.entityid = entity.EntityID AND individual.Entity = entity.EntityID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (18,27,'item.itemid=orderitem.itemid AND orderitem.orderid = cvorder.orderid');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (19,1,'invoice.CustomerID = entity.EntityID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (19,2,'invoice.CustomerID = entity.EntityID AND individual.Entity = entity.EntityID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (19,18,'invoice.OrderID = cvorder.orderid');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (19,27,'item.itemid=invoiceitems.itemid AND invoiceitems.InvoiceID = invoice.InvoiceID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (20,1,'marketinglist.ListID = entity.list');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (20,2,'marketinglist.ListID = individual.list');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (21,27,'item.itemid=promoitem.itemid AND promoitem.PromotionID = promotion.PromotionID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (22,6,'activity.ActivityID = literaturerequest.ActivityID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (23,1,'event.eventID = eventregister.EventID AND individual.IndividualID=eventregister.IndividualID AND entity.EntityID=individual.entity');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (23,2,'event.eventID = eventregister.EventID AND individual.IndividualID=eventregister.IndividualID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (25,27,'item.itemid=expenseitem.ExpenseItemID AND expenseitem.ExpenseID = invoice.ExpenseID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (26,27,'item.itemid=purchaseorderitem.itemid AND purchaseorderitem.purchaseorderid = purchaseorder.purchaseorderid');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (30,1,'entity.entityid=vendor.entityid');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (30,2,'individual.entity=vendor.entityid');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (31,25,'expenseform.ExpenseFormID=expense.ExpenseFormID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (35,2,'individual.IndividualID=employee.IndividualID');
INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES (37,2,'grouptbl.GroupID = member.GroupID and individual.IndividualID=member.ChildID');

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

--
-- Table structure for table 'serversettings'
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
-- Dumping data for table 'serversettings'
--

INSERT INTO `serversettings` (`serversettingid`, `hostname`, `sessiontimeout`, `workinghoursfrom`, `workinghoursto`, `emailcheckinterval`, `filesystemstoragepath`, `defaulttimezone`) VALUES (1,'localhost.localdomain',120,'09:00:00','17:00:00',10,'','EST');

--
-- Table structure for table 'simplesearch'
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
-- Dumping data for table 'simplesearch'
--

INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (1,14,1,1,'2004-05-04 00:00:00',1,20040504120257,'entity');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (2,15,1,1,'2004-05-04 00:00:00',1,20040504120316,'individual');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (3,2,1,1,'2004-05-04 00:00:00',1,20040504120342,'Email');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (4,3,1,1,'2004-05-04 00:00:00',1,20040504120238,'Activities');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (5,5,1,1,'2004-05-04 00:00:00',1,20040504120550,'Notes');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (6,6,1,1,'2004-05-04 00:00:00',1,20040504122032,'Files');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (7,30,1,1,'2004-05-04 00:00:00',1,20040504135421,'Opportunity');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (8,31,1,1,'2004-05-04 00:00:00',1,20040504135729,'Proposal');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (9,32,1,1,'2004-05-04 00:00:00',1,20040504135924,'ListManager');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (10,33,1,1,'2004-05-04 00:00:00',1,20040504140104,'Promotion');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (11,34,1,1,'2004-05-04 00:00:00',1,20040504140315,'LiteratureRequest');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (12,35,1,1,'2004-05-04 00:00:00',1,20040504140514,'Event');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (13,36,1,1,'2004-05-04 00:00:00',1,20040504140844,'Project');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (15,38,1,1,'2004-05-04 00:00:00',1,20040504141419,'TimeSlip');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (16,39,1,1,'2004-05-04 00:00:00',1,20040504141625,'Ticket');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (17,40,1,1,'2004-05-04 00:00:00',1,20040504141719,'FAQ');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (18,41,1,1,'2004-05-04 00:00:00',1,20040504141826,'KnowledgeBase');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (19,42,1,1,'2004-05-04 00:00:00',1,20040504142103,'Order');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (20,56,1,1,'2004-05-04 00:00:00',1,20040504142336,'Invoice');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (21,43,1,1,'2004-05-04 00:00:00',1,20040504142510,'Payment');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (22,44,1,1,'2004-05-04 00:00:00',1,20040504142707,'Expense');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (23,45,1,1,'2004-05-04 00:00:00',1,20040504142920,'PurchaseOrder');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (24,46,1,1,'2004-05-04 00:00:00',1,20040504143047,'item');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (25,48,1,1,'2004-05-04 00:00:00',1,20040504143225,'inventory');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (26,50,1,1,'2004-05-04 00:00:00',1,20040504143256,'vendor');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (27,51,1,1,'2004-05-04 00:00:00',1,20040504143409,'ExpenseForm');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (28,52,1,1,'2004-05-04 00:00:00',1,20040504143537,'Timesheet');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (29,54,1,1,'2004-05-04 00:00:00',1,20040504143623,'Employee');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (33,47,1,1,'2004-06-07 00:00:00',1,20040607103647,'glaccount');
INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (34,16,1,1,'2004-06-07 00:00:00',1,20040607103852,'group');

--
-- Table structure for table 'simplesearchcriteria'
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
-- Dumping data for table 'simplesearchcriteria'
--

INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (1,1,1,3,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (2,1,2,14,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (3,1,2,15,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (4,1,1,217,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (10,2,2,13,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (11,2,2,14,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (12,2,2,15,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (15,3,8,63,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (16,3,8,66,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (17,3,8,67,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (18,3,8,68,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (19,3,8,427,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (20,4,6,38,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (21,4,6,39,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (22,4,6,40,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (23,4,6,44,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (24,4,6,48,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (25,4,6,49,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (26,4,6,42,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (27,4,6,46,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (28,5,7,53,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (29,5,7,54,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (30,5,7,58,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (31,5,7,60,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (32,5,7,61,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (33,5,7,55,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (34,6,9,70,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (35,6,9,71,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (36,6,9,77,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (37,6,9,78,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (38,6,9,74,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (39,6,9,73,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (40,7,10,86,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (41,7,10,87,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (42,7,10,97,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (43,7,10,88,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (44,7,10,90,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (45,7,10,91,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (46,7,10,100,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (47,7,10,93,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (48,7,10,94,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (49,7,10,96,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (50,7,10,95,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (51,8,11,103,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (52,8,11,104,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (53,8,11,105,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (54,8,11,107,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (55,8,11,108,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (56,8,11,106,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (57,8,11,110,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (58,8,11,115,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (59,8,11,113,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (60,8,11,229,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (61,9,20,271,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (62,9,20,272,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (63,9,20,278,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (64,9,1,3,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (65,9,2,14,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (66,9,2,15,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (67,10,21,281,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (68,10,21,282,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (69,10,21,283,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (70,10,21,284,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (71,10,21,434,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (72,10,21,435,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (73,10,27,365,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (74,10,27,366,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (75,11,22,287,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (76,11,22,290,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (77,11,22,291,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (78,11,22,293,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (79,11,22,430,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (80,11,22,429,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (81,11,22,431,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (82,11,22,432,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (83,11,6,41,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (84,12,23,298,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (85,12,23,299,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (86,12,23,301,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (87,12,23,302,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (88,12,23,437,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (89,12,23,438,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (90,12,23,439,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (91,12,23,440,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (92,12,23,441,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (93,12,1,3,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (94,13,12,118,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (95,13,12,119,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (96,13,12,120,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (97,13,12,121,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (98,13,12,122,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (99,13,12,124,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (100,13,12,125,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (101,13,12,127,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (102,13,12,126,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (103,13,12,231,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (104,13,12,232,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (105,13,12,233,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (107,13,9,72,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (120,15,15,153,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (121,15,15,154,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (122,15,15,155,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (123,15,15,156,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (124,15,15,157,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (125,15,15,158,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (126,15,15,159,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (127,15,15,243,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (128,16,14,140,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (129,16,14,141,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (130,16,14,142,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (131,16,14,143,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (132,16,14,144,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (133,16,14,147,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (134,16,14,146,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (135,16,14,148,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (136,16,14,149,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (137,16,14,151,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (138,16,14,244,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (139,16,14,245,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (140,17,16,161,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (141,17,16,162,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (142,17,16,163,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (143,17,16,164,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (144,17,16,166,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (145,18,17,170,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (146,18,17,171,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (147,18,17,172,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (148,18,17,173,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (149,18,17,174,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (150,18,17,176,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (151,19,18,181,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (152,19,18,183,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (153,19,18,184,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (154,19,18,185,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (155,19,18,188,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (156,19,18,189,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (157,19,18,192,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (158,19,18,195,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (159,19,18,196,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (160,19,18,250,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (161,19,18,255,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (162,20,19,200,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (163,20,19,201,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (164,20,19,202,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (165,20,19,203,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (166,20,19,207,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (167,20,19,208,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (168,20,19,210,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (169,20,19,209,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (170,20,19,213,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (171,20,19,263,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (172,20,19,268,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (173,20,19,269,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (174,21,24,306,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (175,21,24,307,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (176,21,24,308,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (177,21,24,309,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (178,21,24,310,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (179,21,24,311,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (180,21,24,312,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (181,21,24,315,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (182,21,24,316,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (183,22,25,322,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (184,22,25,323,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (185,22,25,324,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (186,22,25,325,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (187,22,25,326,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (188,22,25,329,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (189,22,25,330,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (190,22,25,331,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (191,22,25,332,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (192,22,25,336,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (193,23,26,341,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (194,23,26,343,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (195,23,26,344,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (196,23,26,346,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (197,23,26,357,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (198,23,26,353,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (199,23,26,358,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (200,23,26,359,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (201,23,26,360,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (202,24,27,365,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (203,24,27,366,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (204,24,27,367,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (205,24,27,368,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (206,24,27,370,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (207,24,27,369,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (208,24,27,371,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (209,24,27,374,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (210,24,27,373,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (211,24,27,381,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (212,25,29,387,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (213,25,29,388,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (214,25,29,389,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (215,25,29,391,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (216,25,29,390,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (217,25,29,392,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (218,25,29,393,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (219,25,29,396,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (220,26,1,3,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (221,27,31,401,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (222,27,31,402,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (223,27,31,403,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (224,27,31,404,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (225,27,31,405,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (226,27,31,406,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (227,27,31,409,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (228,27,31,411,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (229,28,36,444,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (230,28,36,445,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (231,28,36,446,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (232,28,36,451,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (233,28,36,452,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (234,28,36,453,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (252,33,28,382,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (253,33,28,383,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (254,33,28,384,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (255,33,28,385,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (256,33,28,463,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (257,33,28,464,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (258,34,37,455,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (259,34,37,458,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (260,34,37,459,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (261,34,37,460,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (262,34,2,14,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (263,34,2,14,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (264,29,35,465,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (265,1,1,466,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (266,1,1,467,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (267,1,1,468,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (268,1,1,469,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (269,1,1,470,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (270,1,1,471,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (271,1,1,472,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (272,1,1,473,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (273,2,2,474,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (274,2,2,475,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (275,2,2,476,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (276,2,2,477,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (277,2,2,478,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (278,2,2,479,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (279,2,2,480,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (280,2,2,481,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (281,1,1,482,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (282,1,1,483,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (283,1,1,484,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (284,1,1,485,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (285,1,1,486,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (286,1,1,487,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (287,2,2,488,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (288,2,2,489,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (289,2,2,490,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (290,2,2,491,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (291,2,2,492,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (292,2,2,493,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (293,19,18,494,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (294,19,18,495,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (295,19,18,496,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (296,19,18,497,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (297,19,18,498,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (298,19,18,499,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (299,20,19,500,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (300,20,19,501,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (301,20,19,502,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (302,20,19,503,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (303,20,19,504,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (304,20,19,505,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (305,23,26,506,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (306,23,26,507,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (307,23,26,508,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (308,23,26,509,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (309,23,26,510,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (310,23,26,511,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (311,8,11,512,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (312,8,11,513,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (313,8,11,514,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (314,8,11,515,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (315,8,11,516,2,'OR','',1);
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (316,8,11,517,2,'OR','',1);

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
-- Table structure for table 'supportemailaccount'
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
-- Dumping data for table 'supportemailaccount'
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
-- Table structure for table 'syncnote'
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
-- Dumping data for table 'syncnote'
--


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
-- Table structure for table 'systememailsettings'
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
-- Dumping data for table 'systememailsettings'
--

INSERT INTO `systememailsettings` (`systemEmailID`, `toAddress`, `fromAddress`, `subject`, `smtpServer`, `description`) VALUES (1,'','','CentraView Web Request: Customer Profile Update','','Email which is sent to a CentraView employee user when a Customer requests that their Customer Profile data gets changed.');
INSERT INTO `systememailsettings` (`systemEmailID`, `toAddress`, `fromAddress`, `subject`, `smtpServer`, `description`) VALUES (2,'','','CentraView Web Request: Forgot Password','','Email which is sent to a CentraView Customer user when the Customer make a \'Forgot Password\' request.');

--
-- Table structure for table 'systemsettings'
--

DROP TABLE IF EXISTS `systemsettings`;
CREATE TABLE `systemsettings` (
  `settingID` int(11) unsigned NOT NULL auto_increment,
  `settingName` varchar(255) NOT NULL default '',
  `settingValue` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`settingID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'systemsettings'
--

INSERT INTO `systemsettings` (`settingID`, `settingName`, `settingValue`) VALUES (1,'supportEmailCheckInterval','10');

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
-- Table structure for table 'uidlist'
--

DROP TABLE IF EXISTS `uidlist`;
CREATE TABLE `uidlist` (
  `accountID` int(11) unsigned NOT NULL default '0',
  `uid` varchar(100) NOT NULL default '',
  UNIQUE KEY `messageUID` (`accountID`,`uid`),
  KEY `accountID` (`accountID`)
) TYPE=InnoDB;

--
-- Dumping data for table 'uidlist'
--


--
-- Table structure for table 'uifieldmapping'
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
-- Dumping data for table 'uifieldmapping'
--

INSERT INTO `uifieldmapping` (`ID`, `uifieldname`, `moduleid`, `fieldid`) VALUES (1,'Entity Name',14,3);
INSERT INTO `uifieldmapping` (`ID`, `uifieldname`, `moduleid`, `fieldid`) VALUES (2,'Street1',14,29);
INSERT INTO `uifieldmapping` (`ID`, `uifieldname`, `moduleid`, `fieldid`) VALUES (3,'Street2',14,30);
INSERT INTO `uifieldmapping` (`ID`, `uifieldname`, `moduleid`, `fieldid`) VALUES (4,'city',14,31);
INSERT INTO `uifieldmapping` (`ID`, `uifieldname`, `moduleid`, `fieldid`) VALUES (5,'Title',14,17);
INSERT INTO `uifieldmapping` (`ID`, `uifieldname`, `moduleid`, `fieldid`) VALUES (6,'First Name',14,14);
INSERT INTO `uifieldmapping` (`ID`, `uifieldname`, `moduleid`, `fieldid`) VALUES (7,'Last Name',14,16);
INSERT INTO `uifieldmapping` (`ID`, `uifieldname`, `moduleid`, `fieldid`) VALUES (8,'Acct Mgr',14,649);
INSERT INTO `uifieldmapping` (`ID`, `uifieldname`, `moduleid`, `fieldid`) VALUES (9,'Acct Team',14,62);
INSERT INTO `uifieldmapping` (`ID`, `uifieldname`, `moduleid`, `fieldid`) VALUES (10,'MI',14,15);
INSERT INTO `uifieldmapping` (`ID`, `uifieldname`, `moduleid`, `fieldid`) VALUES (13,'First Name',15,14);
INSERT INTO `uifieldmapping` (`ID`, `uifieldname`, `moduleid`, `fieldid`) VALUES (14,'Last Name',15,16);
INSERT INTO `uifieldmapping` (`ID`, `uifieldname`, `moduleid`, `fieldid`) VALUES (15,'Entity',15,3);
INSERT INTO `uifieldmapping` (`ID`, `uifieldname`, `moduleid`, `fieldid`) VALUES (16,'Title',15,17);
INSERT INTO `uifieldmapping` (`ID`, `uifieldname`, `moduleid`, `fieldid`) VALUES (17,'Street',15,29);
INSERT INTO `uifieldmapping` (`ID`, `uifieldname`, `moduleid`, `fieldid`) VALUES (18,'City',15,31);
INSERT INTO `uifieldmapping` (`ID`, `uifieldname`, `moduleid`, `fieldid`) VALUES (19,'State',15,32);
INSERT INTO `uifieldmapping` (`ID`, `uifieldname`, `moduleid`, `fieldid`) VALUES (20,'Zip',15,33);
INSERT INTO `uifieldmapping` (`ID`, `uifieldname`, `moduleid`, `fieldid`) VALUES (21,'Country',15,34);

--
-- Table structure for table 'user'
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
-- Dumping data for table 'user'
--

INSERT INTO `user` (`UserID`, `Name`, `IndividualID`, `Password`, `userstatus`, `usertype`) VALUES (1,'admin',1,'0DPiKuNIrrVmD8IUCuw1hQxNqZc=','ENABLED','ADMINISTRATOR');

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
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (6,1,2,'acknowledgeddays','23','');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (7,1,1,'emailaccountid','1','');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (8,1,4,'DATEFORMAT','MMM d, yyyy h:mm a','');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (9,1,4,'email','YES','');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (10,1,4,'todayscalendar','YES','');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (11,1,4,'unscheduledactivities','YES','');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (12,1,4,'scheduledopportunities','YES','');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (13,1,4,'projecttasks','YES','');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (14,1,4,'supporttickets','YES','');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (15,1,4,'companynews','YES','');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (16,1,4,'homesettingrefreshmin','5','');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (17,1,4,'homesettingrefreshsec','0','');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (18,1,4,'contenttype','HTML','');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (19,1,1,'allRecordsPublic','Yes','');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (20,1,61,'syncAsPrivate','YES','');
INSERT INTO `userpreference` (`preferenceid`, `individualid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (21,1,2,'emailCheckInterval','10','');

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

INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (1,2,'TIMEZONE','EST','');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (2,3,'TIMESTAMP','','');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (3,4,'DATEFORMAT','MMM d, yyyy h:mm a','');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (4,5,'DEFAULTFOLDERID','','');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (5,6,'caldefaultview','DAILY','');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (6,1,'calendarrefreshmin','10','');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (7,1,'calendarrefreshsec','0','');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (8,1,'homesettingrefreshmin','5','');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (9,1,'homesettingrefreshsec','0','');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (10,1,'unscheduledactivities','YES','');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (11,1,'todayscalendar','YES','');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (12,1,'email','YES','');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (13,1,'scheduledopportunities','YES','');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (14,1,'projecttasks','YES','');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (15,1,'supporttickets','YES','');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (16,1,'companynews','YES','');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (17,1,'contenttype','HTML','');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (18,1,'emailaccountid','','');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (19,1,'syncAsPrivate','YES','');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (20,1,'allRecordsPublic','Yes','');
INSERT INTO `userpreferencedefault` (`preferenceid`, `moduleid`, `preference_name`, `preference_value`, `tag`) VALUES (21,2,'emailCheckInterval','10','');

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
-- Table structure for table 'valuelist'
--

DROP TABLE IF EXISTS `valuelist`;
CREATE TABLE `valuelist` (
  `valueListId` int(11) unsigned NOT NULL auto_increment,
  `valueListName` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`valueListId`)
) TYPE=InnoDB;

--
-- Dumping data for table 'valuelist'
--

INSERT INTO `valuelist` (`valueListId`, `valueListName`) VALUES (1,'Entity');
INSERT INTO `valuelist` (`valueListId`, `valueListName`) VALUES (2,'Individual');

--
-- Table structure for table 'valuelistfield'
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
-- Dumping data for table 'valuelistfield'
--

INSERT INTO `valuelistfield` (`valueListFieldId`, `valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`) VALUES (1,1,'Entity ID',1,'N','entity.entityId');
INSERT INTO `valuelistfield` (`valueListFieldId`, `valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`) VALUES (2,1,'Entity Name',2,'N','entity.entityName');
INSERT INTO `valuelistfield` (`valueListFieldId`, `valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`) VALUES (3,1,'Marketing List',3,'N','entity.marketingList');
INSERT INTO `valuelistfield` (`valueListFieldId`, `valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`) VALUES (4,1,'Account Manager ID',4,'N','entity.accountManagerId');
INSERT INTO `valuelistfield` (`valueListFieldId`, `valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`) VALUES (5,1,'Account Manager',5,'N','entity.accountManagerName');
INSERT INTO `valuelistfield` (`valueListFieldId`, `valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`) VALUES (6,1,'Primary Contact ID',6,'N','entity.primaryContactId');
INSERT INTO `valuelistfield` (`valueListFieldId`, `valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`) VALUES (7,1,'Primary Contact',7,'N','entity.primaryContact');
INSERT INTO `valuelistfield` (`valueListFieldId`, `valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`) VALUES (8,1,'Phone',8,'N','entity.phone');
INSERT INTO `valuelistfield` (`valueListFieldId`, `valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`) VALUES (9,1,'Email',9,'N','entity.email');
INSERT INTO `valuelistfield` (`valueListFieldId`, `valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`) VALUES (10,1,'Website',10,'N','entity.website');
INSERT INTO `valuelistfield` (`valueListFieldId`, `valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`) VALUES (11,1,'Street 1',11,'N','entity.street1');
INSERT INTO `valuelistfield` (`valueListFieldId`, `valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`) VALUES (12,1,'Street 2',12,'N','entity.street2');
INSERT INTO `valuelistfield` (`valueListFieldId`, `valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`) VALUES (13,1,'City',13,'N','entity.city');
INSERT INTO `valuelistfield` (`valueListFieldId`, `valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`) VALUES (14,1,'State',14,'N','entity.state');
INSERT INTO `valuelistfield` (`valueListFieldId`, `valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`) VALUES (15,1,'Zipcode',15,'N','entity.zipcode');
INSERT INTO `valuelistfield` (`valueListFieldId`, `valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`) VALUES (16,1,'Country',16,'N','entity.country');
INSERT INTO `valuelistfield` (`valueListFieldId`, `valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`) VALUES (17,1,'Address',17,'N','entity.address');

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
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (9,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (11,'Field',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (12,'Subject',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (13,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (14,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (16,'Title',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (17,'ID',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (19,'Type',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (26,'Fax',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (26,'Phone',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (26,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (29,'Title',1);
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
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (23,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (23,'Description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (23,'Enabled',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (7,'Street2',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (7,'City',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (7,'State',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (7,'ZipCode',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (7,'Country',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (6,'Title',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (6,'Created',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (6,'Priority',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (6,'CreatedBy',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (6,'Status',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (9,'Title',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (9,'Entity',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (9,'Phone',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (9,'Fax',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (9,'Email',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (11,'Value',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (12,'From',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (12,'Received',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (12,'Size',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (5,'Title',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (5,'Email',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (5,'Phone',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (19,'Content',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (19,'Notes',3);
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
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (52,'Priority',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (52,'CreatedBy',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (52,'Status',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (55,'RuleName',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (55,'Description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (55,'EnabledStatus',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (57,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (57,'Created',2);
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
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (39,'Priority',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (39,'CreatedBy',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (39,'Status',7);
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
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (43,'Priority',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (43,'Created',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (43,'Status',8);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (44,'Value',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (45,'From',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (45,'To',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (45,'Received',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (45,'Size',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (53,'Title',1);
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
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (66,'Subject',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (66,'Entity',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (66,'DateOpened',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (66,'Status',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (66,'DateClosed',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (66,'AssignedTo',7);
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
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (33,'Updated',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (33,'CreatedBy',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (33,'FolderName',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (63,'Updated',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (63,'CreatedBy',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (63,'FolderName',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (73,'title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (73,'detail',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (73,'start',3);
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
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (90,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (90,'Project',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (90,'Milestone',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (90,'Manager',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (90,'StartDate',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (90,'DueDate',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (90,'Complete',7);
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
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (22,'Title',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (22,'Date',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (22,'CreatedBy',4);
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
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (190,'Name',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (190,'File',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (191,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (191,'Owner',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (75,'title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (75,'description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (75,'OrderValue',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (75,'numberofrecords',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (75,'Opportunities',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (75,'OpportunityValue',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (75,'Order',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (1,'AccountManager',8);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (66,'Number',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (22,'Detail',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (106,'Detail',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (90,'Status',9);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (192,'ProposalID',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (192,'Title',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (192,'Description',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (192,'Type',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (192,'Stage',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (192,'Probability',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (192,'Status',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (192,'EstimatedCloseDate',8);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (192,'ForecastAmmount',9);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (174,'UserName',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (174,'Name',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (174,'Entity',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (174,'Enabled',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (182,'RecordName',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (193,'TemplateID',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (193,'Name',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (193,'Category',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (14,'Description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (14,'Entity',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (14,'Type',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (14,'Stage',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (14,'Probability',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (14,'Status',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (14,'EstimatedCloseDate',8);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (14,'ForecastAmount',9);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (14,'ActualAmount',10);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (14,'SalesPersonName',11);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (72,'LiteratureRequested',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (72,'WhoRequested',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (72,'DateRequested',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (72,'DeliveryMethod',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (72,'Entity',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (20,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (20,'Details',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (20,'Entity',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (20,'Individual',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (20,'Start',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (20,'End',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (20,'Type',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (20,'Status',8);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (20,'Priority',9);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (20,'Owner',10);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (20,'Created',11);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (20,'CreatedBy',12);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (20,'Notes',13);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (8,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (8,'Details',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (8,'Entity',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (8,'Individual',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (8,'Start',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (8,'End',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (8,'Status',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (8,'Priority',8);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (8,'Owner',9);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (8,'Created',10);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (8,'CreatedBy',11);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (8,'Notes',12);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (10,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (10,'Details',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (10,'Entity',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (10,'Individual',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (10,'Start',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (10,'End',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (10,'CallType',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (10,'Status',8);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (10,'Priority',9);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (10,'Owner',10);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (10,'Created',11);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (10,'CreatedBy',12);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (10,'Notes',13);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (18,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (18,'Details',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (18,'Entity',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (18,'Individual',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (18,'Start',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (18,'End',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (18,'Status',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (18,'Priority',8);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (18,'Owner',9);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (18,'Created',10);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (18,'CreatedBy',11);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (18,'Notes',12);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (21,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (21,'Details',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (21,'Entity',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (21,'Individual',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (21,'Start',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (21,'End',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (21,'Status',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (21,'Priority',8);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (21,'Owner',9);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (21,'Created',10);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (21,'CreatedBy',11);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (21,'Notes',12);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (25,'Title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (25,'Details',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (25,'Entity',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (25,'Individual',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (25,'Start',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (25,'End',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (25,'Status',7);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (25,'Priority',8);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (25,'Owner',9);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (25,'Created',10);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (25,'CreatedBy',11);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (25,'Notes',12);


COMMIT;
