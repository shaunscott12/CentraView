-- Drop the OLD rules table
DROP TABLE IF EXISTS `emailrules`;

-- Create the NEW rules table (different name)
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


-- Drop the OLD rules criteria table
DROP TABLE IF EXISTS `emailrulecriteria`;
-- Create the NEW rules criteria table
CREATE TABLE `emailrulecriteria` (
  `ruleID` int(11) unsigned NOT NULL,
  `orderID` int(11) unsigned NOT NULL,
  `expressionType` enum('AND', 'OR') NOT NULL default 'OR',
  `fieldID` int(11) unsigned NOT NULL,
  `conditionID` int(11) unsigned NOT NULL,
  `value` varchar(255) NOT NULL,
  KEY `ruleID` (`ruleID`),
  KEY `fieldID` (`fieldID`),
  KEY `conditionID` (`conditionID`),
  FOREIGN KEY (`ruleID`) REFERENCES `emailrule` (`ruleID`) ON UPDATE CASCADE ON DELETE CASCADE
) TYPE=InnoDB;


-- Drop the OLD rules actions table if it exists (IT SHOULDN'T!)
DROP TABLE IF EXISTS `emailruleaction`;
-- Create the NEW rules actions table
CREATE TABLE `emailruleaction` (
  `ruleID` int(11) unsigned NOT NULL,
  `actionType` enum('MOVE', 'MARK', 'DEL') NOT NULL,
  `folderID` int(11) unsigned NOT NULL,
  KEY `ruleID` (`ruleID`),
  KEY `folderID` (`folderID`),
  FOREIGN KEY (`ruleID`) REFERENCES `emailrule` (`ruleID`) ON UPDATE CASCADE ON DELETE CASCADE
) TYPE=InnoDB;


DELETE FROM searchfield WHERE SearchFieldID=62;
UPDATE searchfield SET DisplayName='Subject', fieldname='Subject', RealTableName='emailmessage', RelationshipQuery='' WHERE SearchFieldID=63;
UPDATE searchfield SET DisplayName='Body', fieldname='Body', RealTableName='emailmessage', RelationshipQuery='' WHERE SearchFieldID=64;
DELETE FROM searchfield WHERE SearchFieldID=65;
UPDATE searchfield SET DisplayName='Sender', fieldname='Sender', RealTableName='emailmessage', RelationshipQuery='' WHERE SearchFieldID=66;
UPDATE searchfield SET DisplayName='Recipient', fieldname='Recipient', RealTableName='emailmessage', RelationshipQuery='' WHERE SearchFieldID=67;

-- Report custom field stuff
ALTER TABLE `reportcontent` ADD `tableId` int(11) unsigned NOT NULL DEFAULT 0;

-- Email Message table needs a contentType field
ALTER TABLE emailmessage ADD contentType ENUM('text/plain', 'text/html') NOT NULL DEFAULT 'text/plain';

-- Update to make customfields appear on the support reports.
UPDATE `cvtable` SET `moduleid` = 39 WHERE `tableid` = 52;
INSERT INTO `searchmodule` VALUES (39,5,'N');
