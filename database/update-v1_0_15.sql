-- updating the module table for the Task
alter table module modify `primarykeyfield` varchar(255) NOT NULL default '';
update module set primarytable=' task, activity',ownerfield='owner', primarykeyfield='task.ActivityID=activity.ActivityID and task.ActivityID' where moduleid=37;

-- Server Setting
alter table serversettings add `username` varchar(50) default NULL;
alter table serversettings add `password` varchar(25) default NULL;
alter table serversettings add `authentication` enum('YES','NO') default 'NO';
alter table serversettings add `smtpport` int(4) unsigned NOT NULL default '25';

-- Suggestion

alter table suggestion drop `mailaddress` ;

-- Group

SELECT @groupModuleID := moduleid FROM module WHERE
UPPER(primarytable) = UPPER('grouptbl');

INSERT INTO `searchtable` (`DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Group', 'grouptbl', 'GroupID', NULL);

set @groupTableID = LAST_INSERT_ID();

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@groupModuleID, @groupTableID, 'Y');

SELECT @individualTableId := SearchTableID FROM searchtable WHERE 
UPPER(DisplayName) = UPPER('Individual');


INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@groupModuleID, @individualTableId, 'N');


INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@groupTableID, @individualTableId, 'grouptbl.GroupID = member.GroupID and individual.IndividualID=member.ChildID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@groupTableID, 'Creation Date', 'createDate');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@groupTableID, 'Modified Date', 'modifyDate');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@groupTableID, 'Group ID', 'GroupID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@groupTableID, 'Name', 'Name');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@groupTableID, 'Description', 'Description');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@groupTableID, 'Owned By', 'CONCAT(Owned16.FirstName, " ", Owned16.LastName)', 'N', 'individual Owned16',
'Owned16.IndividualID = grouptbl.owner');

-- GLAccount

SELECT @glAccountModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('glaccount');

SELECT @glAccountTableId := SearchTableID FROM searchtable WHERE 
UPPER(DisplayName) = UPPER('glaccount');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@glAccountModuleId, @glAccountTableId, 'Y');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@glAccountTableId, 'GLAccounts ID', 'GLAccountsID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@glAccountTableId, 'External ID', 'externalID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@glAccountTableId, 'Name', 'Name');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@glAccountTableId, 'Description', 'Description');


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
  CONSTRAINT `0_13666` FOREIGN KEY (`ModuleID`) REFERENCES `module` (`moduleid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `0_13667` FOREIGN KEY (`OwnerID`) REFERENCES `individual` (`IndividualID`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `0_13668` FOREIGN KEY (`CreatedBy`) REFERENCES `individual` (`IndividualID`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `0_13669` FOREIGN KEY (`ModifiedBy`) REFERENCES `individual` (`IndividualID`) ON DELETE SET NULL ON UPDATE CASCADE
) TYPE=InnoDB;

--
-- Dumping data for table `simplesearch`
--

INSERT INTO `simplesearch` (`SearchID`, `ModuleID`, `OwnerID`, `CreatedBy`, `CreationDate`, `ModifiedBy`, `ModifiedDate`, `SearchName`) VALUES (1,14,1,1,'2004-05-04 00:00:00',1,20040504120257,'entity'),(2,15,1,1,'2004-05-04 00:00:00',1,20040504120316,'individual'),(3,2,1,1,'2004-05-04 00:00:00',1,20040504120342,'Email'),(4,3,1,1,'2004-05-04 00:00:00',1,20040504120238,'Activities'),(5,5,1,1,'2004-05-04 00:00:00',1,20040504120550,'Notes'),(6,6,1,1,'2004-05-04 00:00:00',1,20040504122032,'Files'),(7,30,1,1,'2004-05-04 00:00:00',1,20040504135421,'Opportunity'),(8,31,1,1,'2004-05-04 00:00:00',1,20040504135729,'Proposal'),(9,32,1,1,'2004-05-04 00:00:00',1,20040504135924,'ListManager'),(10,33,1,1,'2004-05-04 00:00:00',1,20040504140104,'Promotion'),(11,34,1,1,'2004-05-04 00:00:00',1,20040504140315,'LiteratureRequest'),(12,35,1,1,'2004-05-04 00:00:00',1,20040504140514,'Event'),(13,36,1,1,'2004-05-04 00:00:00',1,20040504140844,'Project'),(14,24,1,1,'2004-05-04 00:00:00',1,20040504141059,'Task'),(15,38,1,1,'2004-05-04 00:00:00',1,20040504141419,'TimeSlip'),(16,39,1,1,'2004-05-04 00:00:00',1,20040504141625,'Ticket'),(17,40,1,1,'2004-05-04 00:00:00',1,20040504141719,'FAQ'),(18,41,1,1,'2004-05-04 00:00:00',1,20040504141826,'KnowledgeBase'),(19,42,1,1,'2004-05-04 00:00:00',1,20040504142103,'Order'),(20,56,1,1,'2004-05-04 00:00:00',1,20040504142336,'Invoice'),(21,43,1,1,'2004-05-04 00:00:00',1,20040504142510,'Payment'),(22,44,1,1,'2004-05-04 00:00:00',1,20040504142707,'Expense'),(23,45,1,1,'2004-05-04 00:00:00',1,20040504142920,'PurchaseOrder'),(24,46,1,1,'2004-05-04 00:00:00',1,20040504143047,'item'),(25,48,1,1,'2004-05-04 00:00:00',1,20040504143225,'inventory'),(26,50,1,1,'2004-05-04 00:00:00',1,20040504143256,'vendor'),(27,51,1,1,'2004-05-04 00:00:00',1,20040504143409,'ExpenseForm'),(28,52,1,1,'2004-05-04 00:00:00',1,20040504143537,'Timesheet'),(29,54,1,1,'2004-05-04 00:00:00',1,20040504143623,'Employee'),(33,47,1,1,'2004-06-07 00:00:00',1,20040607103647,'glaccount'),(34,16,1,1,'2004-06-07 00:00:00',1,20040607103852,'group');

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
  CONSTRAINT `0_13671` FOREIGN KEY (`SearchID`) REFERENCES `simplesearch` (`SearchID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `0_13672` FOREIGN KEY (`SearchTableID`) REFERENCES `searchtable` (`SearchTableID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `0_13673` FOREIGN KEY (`SearchFieldID`) REFERENCES `searchfield` (`SearchFieldID`) ON DELETE CASCADE ON UPDATE CASCADE
) TYPE=InnoDB;

--
-- Dumping data for table `simplesearchcriteria`
--
SET FOREIGN_KEY_CHECKS=0;
INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (1,1,1,3,2,'OR','',1),(2,1,2,14,2,'OR','',1),(3,1,2,15,2,'OR','',1),(4,1,1,217,2,'OR','',1),(5,1,4,26,2,'OR','',1),(6,1,4,27,2,'OR','',1),(7,1,4,29,2,'OR','',1),(8,1,4,28,2,'OR','',1),(9,1,1,217,2,'OR','',1),(10,2,2,13,2,'OR','',1),(11,2,2,14,2,'OR','',1),(12,2,2,15,2,'OR','',1),(13,2,3,22,2,'OR','',1),(14,1,3,22,2,'OR','',1),(15,3,8,63,2,'OR','',1),(16,3,8,66,2,'OR','',1),(17,3,8,67,2,'OR','',1),(18,3,8,68,2,'OR','',1),(19,3,8,427,2,'OR','',1),(20,4,6,38,2,'OR','',1),(21,4,6,39,2,'OR','',1),(22,4,6,40,2,'OR','',1),(23,4,6,44,2,'OR','',1),(24,4,6,48,2,'OR','',1),(25,4,6,49,2,'OR','',1),(26,4,6,42,2,'OR','',1),(27,4,6,46,2,'OR','',1),(28,5,7,53,2,'OR','',1),(29,5,7,54,2,'OR','',1),(30,5,7,58,2,'OR','',1),(31,5,7,60,2,'OR','',1),(32,5,7,61,2,'OR','',1),(33,5,7,55,2,'OR','',1),(34,6,9,70,2,'OR','',1),(35,6,9,71,2,'OR','',1),(36,6,9,77,2,'OR','',1),(37,6,9,78,2,'OR','',1),(38,6,9,74,2,'OR','',1),(39,6,9,73,2,'OR','',1),(40,7,10,86,2,'OR','',1),(41,7,10,87,2,'OR','',1),(42,7,10,97,2,'OR','',1),(43,7,10,88,2,'OR','',1),(44,7,10,90,2,'OR','',1),(45,7,10,91,2,'OR','',1),(46,7,10,100,2,'OR','',1),(47,7,10,93,2,'OR','',1),(48,7,10,94,2,'OR','',1),(49,7,10,96,2,'OR','',1),(50,7,10,95,2,'OR','',1),(51,8,11,103,2,'OR','',1),(52,8,11,104,2,'OR','',1),(53,8,11,105,2,'OR','',1),(54,8,11,107,2,'OR','',1),(55,8,11,108,2,'OR','',1),(56,8,11,106,2,'OR','',1),(57,8,11,110,2,'OR','',1),(58,8,11,115,2,'OR','',1),(59,8,11,113,2,'OR','',1),(60,8,11,229,2,'OR','',1),(61,9,20,271,2,'OR','',1),(62,9,20,272,2,'OR','',1),(63,9,20,278,2,'OR','',1),(64,9,1,3,2,'OR','',1),(65,9,2,14,2,'OR','',1),(66,9,2,15,2,'OR','',1),(67,10,21,281,2,'OR','',1),(68,10,21,282,2,'OR','',1),(69,10,21,283,2,'OR','',1),(70,10,21,284,2,'OR','',1),(71,10,21,434,2,'OR','',1),(72,10,21,435,2,'OR','',1),(73,10,27,365,2,'OR','',1),(74,10,27,366,2,'OR','',1),(75,11,22,287,2,'OR','',1),(76,11,22,290,2,'OR','',1),(77,11,22,291,2,'OR','',1),(78,11,22,293,2,'OR','',1),(79,11,22,430,2,'OR','',1),(80,11,22,429,2,'OR','',1),(81,11,22,431,2,'OR','',1),(82,11,22,432,2,'OR','',1),(83,11,6,41,2,'OR','',1),(84,12,23,298,2,'OR','',1),(85,12,23,299,2,'OR','',1),(86,12,23,301,2,'OR','',1),(87,12,23,302,2,'OR','',1),(88,12,23,437,2,'OR','',1),(89,12,23,438,2,'OR','',1),(90,12,23,439,2,'OR','',1),(91,12,23,440,2,'OR','',1),(92,12,23,441,2,'OR','',1),(93,12,1,3,2,'OR','',1),(94,13,12,118,2,'OR','',1),(95,13,12,119,2,'OR','',1),(96,13,12,120,2,'OR','',1),(97,13,12,121,2,'OR','',1),(98,13,12,122,2,'OR','',1),(99,13,12,124,2,'OR','',1),(100,13,12,125,2,'OR','',1),(101,13,12,127,2,'OR','',1),(102,13,12,126,2,'OR','',1),(103,13,12,231,2,'OR','',1),(104,13,12,232,2,'OR','',1),(105,13,12,233,2,'OR','',1),(106,13,12,234,2,'OR','',1),(107,13,9,72,2,'OR','',1),(108,14,13,133,2,'OR','',1),(109,14,13,134,2,'OR','',1),(110,14,13,135,2,'OR','',1),(111,14,13,136,2,'OR','',1),(112,14,13,138,2,'OR','',1),(113,14,13,235,2,'OR','',1),(114,14,13,237,2,'OR','',1),(115,14,13,238,2,'OR','',1),(116,14,13,239,2,'OR','',1),(117,14,13,240,2,'OR','',1),(118,14,13,241,2,'OR','',1),(119,14,13,242,2,'OR','',1),(120,15,15,153,2,'OR','',1),(121,15,15,154,2,'OR','',1),(122,15,15,155,2,'OR','',1),(123,15,15,156,2,'OR','',1),(124,15,15,157,2,'OR','',1),(125,15,15,158,2,'OR','',1),(126,15,15,159,2,'OR','',1),(127,15,15,243,2,'OR','',1),(128,16,14,140,2,'OR','',1),(129,16,14,141,2,'OR','',1),(130,16,14,142,2,'OR','',1),(131,16,14,143,2,'OR','',1),(132,16,14,144,2,'OR','',1),(133,16,14,147,2,'OR','',1),(134,16,14,146,2,'OR','',1),(135,16,14,148,2,'OR','',1),(136,16,14,149,2,'OR','',1),(137,16,14,151,2,'OR','',1),(138,16,14,244,2,'OR','',1),(139,16,14,245,2,'OR','',1),(140,17,16,161,2,'OR','',1),(141,17,16,162,2,'OR','',1),(142,17,16,163,2,'OR','',1),(143,17,16,164,2,'OR','',1),(144,17,16,166,2,'OR','',1),(145,18,17,170,2,'OR','',1),(146,18,17,171,2,'OR','',1),(147,18,17,172,2,'OR','',1),(148,18,17,173,2,'OR','',1),(149,18,17,174,2,'OR','',1),(150,18,17,176,2,'OR','',1),(151,19,18,181,2,'OR','',1),(152,19,18,183,2,'OR','',1),(153,19,18,184,2,'OR','',1),(154,19,18,185,2,'OR','',1),(155,19,18,188,2,'OR','',1),(156,19,18,189,2,'OR','',1),(157,19,18,192,2,'OR','',1),(158,19,18,195,2,'OR','',1),(159,19,18,196,2,'OR','',1),(160,19,18,250,2,'OR','',1),(161,19,18,255,2,'OR','',1),(162,20,19,200,2,'OR','',1),(163,20,19,201,2,'OR','',1),(164,20,19,202,2,'OR','',1),(165,20,19,203,2,'OR','',1),(166,20,19,207,2,'OR','',1),(167,20,19,208,2,'OR','',1),(168,20,19,210,2,'OR','',1),(169,20,19,209,2,'OR','',1),(170,20,19,213,2,'OR','',1),(171,20,19,263,2,'OR','',1),(172,20,19,268,2,'OR','',1),(173,20,19,269,2,'OR','',1),(174,21,24,306,2,'OR','',1),(175,21,24,307,2,'OR','',1),(176,21,24,308,2,'OR','',1),(177,21,24,309,2,'OR','',1),(178,21,24,310,2,'OR','',1),(179,21,24,311,2,'OR','',1),(180,21,24,312,2,'OR','',1),(181,21,24,315,2,'OR','',1),(182,21,24,316,2,'OR','',1),(183,22,25,322,2,'OR','',1),(184,22,25,323,2,'OR','',1),(185,22,25,324,2,'OR','',1),(186,22,25,325,2,'OR','',1),(187,22,25,326,2,'OR','',1),(188,22,25,329,2,'OR','',1),(189,22,25,330,2,'OR','',1),(190,22,25,331,2,'OR','',1),(191,22,25,332,2,'OR','',1),(192,22,25,336,2,'OR','',1),(193,23,26,341,2,'OR','',1),(194,23,26,343,2,'OR','',1),(195,23,26,344,2,'OR','',1),(196,23,26,346,2,'OR','',1),(197,23,26,357,2,'OR','',1),(198,23,26,353,2,'OR','',1),(199,23,26,358,2,'OR','',1),(200,23,26,359,2,'OR','',1),(201,23,26,360,2,'OR','',1),(202,24,27,365,2,'OR','',1),(203,24,27,366,2,'OR','',1),(204,24,27,367,2,'OR','',1),(205,24,27,368,2,'OR','',1),(206,24,27,370,2,'OR','',1),(207,24,27,369,2,'OR','',1),(208,24,27,371,2,'OR','',1),(209,24,27,374,2,'OR','',1),(210,24,27,373,2,'OR','',1),(211,24,27,381,2,'OR','',1),(212,25,29,387,2,'OR','',1),(213,25,29,388,2,'OR','',1),(214,25,29,389,2,'OR','',1),(215,25,29,391,2,'OR','',1),(216,25,29,390,2,'OR','',1),(217,25,29,392,2,'OR','',1),(218,25,29,393,2,'OR','',1),(219,25,29,396,2,'OR','',1),(220,26,1,3,2,'OR','',1),(221,27,31,401,2,'OR','',1),(222,27,31,402,2,'OR','',1),(223,27,31,403,2,'OR','',1),(224,27,31,404,2,'OR','',1),(225,27,31,405,2,'OR','',1),(226,27,31,406,2,'OR','',1),(227,27,31,409,2,'OR','',1),(228,27,31,411,2,'OR','',1),(229,28,36,444,2,'OR','',1),(230,28,36,445,2,'OR','',1),(231,28,36,446,2,'OR','',1),(232,28,36,451,2,'OR','',1),(233,28,36,452,2,'OR','',1),(234,28,36,453,2,'OR','',1),(235,29,2,13,2,'OR','',1),(236,29,2,14,2,'OR','',1),(237,29,2,15,2,'OR','',1),(252,33,28,382,2,'OR','',1),(253,33,28,383,2,'OR','',1),(254,33,28,384,2,'OR','',1),(255,33,28,385,2,'OR','',1),(256,33,28,463,2,'OR','',1),(257,33,28,464,2,'OR','',1),(258,34,37,455,2,'OR','',1),(259,34,37,458,2,'OR','',1),(260,34,37,459,2,'OR','',1),(261,34,37,460,2,'OR','',1),(262,34,2,14,2,'OR','',1),(263,34,2,14,2,'OR','',1);
SET FOREIGN_KEY_CHECKS=1;

-- Updating and deleting the duplicate entries from the Advance Search

delete from searchfield where SearchFieldId in (123,454,350,352,186,249);
update searchfield set DisplayName='Status' where SearchFieldID=242;


-- Allow null in those fields.
ALTER TABLE `address` MODIFY `state` varchar(255) NULL default '';
ALTER TABLE `address` MODIFY `country` varchar(255) NULL default '';
-- Fix the case for the primary table name:
UPDATE `module` SET `primarytable`='invoice' WHERE `primarytable`='Invoice';
UPDATE `module` SET `primarytable`='expense' WHERE `primarytable`='Expense';
-- increase the size of the hostname field
ALTER TABLE `serversettings` MODIFY `hostname` varchar(255) NOT NULL default '';


-- Event Link table
DROP TABLE IF EXISTS `eventlink`;
CREATE TABLE `eventlink` (
  `EventID` int(11) unsigned NOT NULL default '0',
  `RecordTypeID` int(11) unsigned NOT NULL default '0',
  `RecordID` int(11) unsigned NOT NULL default '0',
  KEY `EventID` (`EventID`),
  KEY `RecordTypeID` (`RecordTypeID`),
  KEY `RecordID` (`RecordID`)
) TYPE=InnoDB;

-- Added the Missing field in the Fields table for Note's Module

insert into field (tableid,name) values(111,'priority');
insert into field (tableid,name) values(111,'RelateEntity');
insert into field (tableid,name) values(111,'RelateIndividual');

-- Remove the unnecessary Column in Ticket View
delete from listcolumns where listtype='Ticket' and columnname='Category';

-- Fix for bug 780
insert into `activitystatus` values (3,'Assigned');


-- For handling aggregate notes data for CompanionLink Sync API
DROP TABLE IF EXISTS `syncnote`;
CREATE TABLE `syncnote` (
  `recordID` int(11) unsigned NOT NULL default '0',
  `recordTypeID` int(11) unsigned NOT NULL default '0',
  `content` text,
  UNIQUE KEY `noteID` (`recordID`,`recordTypeID`),
  KEY `recordID` (`recordID`)
) TYPE=InnoDB;

-- Delete the Duplicate module Called Task

delete from module where moduleid=24;

-- listtype table was having the wrong module id for the Ticket 

update listtypes set moduleid=37 where typename='Tasks';
update listtypes set moduleid=39 where typename='Ticket';

-- corrected the Mistake in searchtablerelte for marketing module
update searchtablerelate set LeftSearchTableID=20 where LeftSearchTableID='32';
 
-- Wrong information in the searchfield table for the status

update searchfield set RelationshipQuery='inventorystatus29.statusid=inventory.statusid' where SearchFieldID=392;

delete from simplesearchcriteria where searchcriteriaID= 106;

update searchtablerelate set  RelationshipQuery='marketinglist.ListID = individual.list' where LeftSearchTableID=20 and RightSearchTableID=2;

-- New user preference for recurring email checking
INSERT INTO userpreference (individualid, moduleid, preference_name, preference_value) SELECT individualid, 2, 'emailCheckInterval', '10' FROM user;
INSERT INTO userpreferencedefault (moduleid, preference_name, preference_value) VALUES (2, 'emailCheckInterval', '10');

-- Table to hold general system settings values
DROP TABLE IF EXISTS `systemsettings`;
CREATE TABLE `systemsettings` (
  `settingID` int(11) unsigned AUTO_INCREMENT PRIMARY KEY,
  `settingName` varchar(255) NOT NULL,
  `settingValue` varchar(255) NOT NULL
) TYPE=InnoDB;
INSERT INTO `systemsettings` (settingName, settingValue) VALUES ('supportEmailCheckInterval', '10');

update searchmodule set isPrimaryTable='N' where searchtableid=9 and moduleid=36;
update searchmodule set isPrimaryTable='N' where searchtableid=6 and moduleid=3;

update searchtablerelate set RelationshipQuery='cvfile.FileID = cvfilelink.FileID and cvfilelink.RecordTypeID = 36 AND project.ProjectID = cvfilelink.RecordID' where LeftSearchTableID=12 and RightSearchTableID=9;
delete from simplesearchcriteria where searchcriteriaid=9;
