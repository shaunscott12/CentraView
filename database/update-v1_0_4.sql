-- Database Structure for Advanced Search

---
-- Table contains the 'searchable' tables.
---

DROP TABLE IF EXISTS `searchtable`;
CREATE TABLE `searchtable` (
  `SearchTableID` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `DisplayName` VARCHAR(255) DEFAULT NULL,
  `TableName` VARCHAR(255) DEFAULT NULL,
  `TablePrimaryKey` VARCHAR(255) DEFAULT NULL,
  `RecordPermissionQuery` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY  (`SearchTableID`),
  UNIQUE KEY `SearchTableID` (`SearchTableID`)
) TYPE=InnoDB;

---
-- Table contains the tables that are linked to modules
---

DROP TABLE IF EXISTS `searchmodule`;
CREATE TABLE `searchmodule` (
  `ModuleID` INT(10) UNSIGNED NOT NULL,
  `SearchTableID` INT(11) UNSIGNED NOT NULL,
  `IsPrimaryTable` ENUM('N','Y') NOT NULL DEFAULT 'N',
  UNIQUE KEY `SearchModuleID` (`ModuleID`, `SearchTableID`),
  INDEX (`ModuleID`),
  INDEX (`SearchTableID`),
  FOREIGN KEY (`ModuleID`) REFERENCES `module` (`moduleid`) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (`SearchTableID`) REFERENCES `searchtable` (`SearchTableID`) ON UPDATE CASCADE ON DELETE CASCADE
) TYPE=InnoDB;

---
-- Table contains the 'searchable' fields.
---

DROP TABLE IF EXISTS `searchfield`;
CREATE TABLE `searchfield` (
  `SearchFieldID` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `SearchTableID` INT(11) UNSIGNED NOT NULL,
  `DisplayName` VARCHAR(255) DEFAULT NULL,
  `FieldName` VARCHAR(255) DEFAULT NULL,
  `FieldPermissionQuery` VARCHAR(255) DEFAULT NULL,
  `IsOnTable` ENUM('N','Y') NOT NULL DEFAULT 'Y',
  `RealTableName` VARCHAR(255) DEFAULT NULL,
  `RelationshipQuery` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY  (`SearchFieldID`),
  UNIQUE KEY `SearchFieldID` (`SearchFieldID`), 
  INDEX (`SearchTableID`),
  FOREIGN KEY (`SearchTableID`) REFERENCES `searchtable` (`SearchTableID`) ON UPDATE CASCADE ON DELETE CASCADE
) TYPE=InnoDB;

---
-- Table contains the relationships between tables
---

DROP TABLE IF EXISTS `searchtablerelate`;
CREATE TABLE `searchtablerelate` (
  `LeftSearchTableID` INT(11) UNSIGNED NOT NULL,
  `RightSearchTableID` INT(11) UNSIGNED NOT NULL,
  `RelationshipQuery` VARCHAR(255) DEFAULT NULL,
  UNIQUE KEY `SearchRelationshipID` (`LeftSearchTableID`, `RightSearchTableID`),
  INDEX (`LeftSearchTableID`),
  INDEX (`RightSearchTableID`),
  FOREIGN KEY (`LeftSearchTableID`) REFERENCES `searchtable` (`SearchTableID`) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (`RightSearchTableID`) REFERENCES `searchtable` (`SearchTableID`) ON UPDATE CASCADE ON DELETE CASCADE
) TYPE=InnoDB;

---
-- Table contains the saved searches
---

DROP TABLE IF EXISTS `search`; 
CREATE TABLE `search` (
  `SearchID` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `ModuleID` INT(10) UNSIGNED NOT NULL,
  `OwnerID` INT(10) UNSIGNED DEFAULT NULL,
  `CreatedBy` INT(10) UNSIGNED DEFAULT NULL,
  `CreationDate` DATETIME DEFAULT NULL,
  `ModifiedBy` INT(10) UNSIGNED DEFAULT NULL,
  `ModifiedDate` TIMESTAMP(14) NOT NULL,
  `SearchName` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY  (`SearchID`),
  UNIQUE KEY `SearchID` (`SearchID`), 
  INDEX (`ModuleID`),
  INDEX (`OwnerID`),
  INDEX (`CreatedBy`),
  INDEX (`ModifiedBy`),
  FOREIGN KEY (`ModuleID`) REFERENCES `module` (`moduleid`) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (`OwnerID`) REFERENCES `individual` (`IndividualID`) ON UPDATE CASCADE ON DELETE SET NULL,
  FOREIGN KEY (`CreatedBy`) REFERENCES `individual` (`IndividualID`) ON UPDATE CASCADE ON DELETE SET NULL,
  FOREIGN KEY (`ModifiedBy`) REFERENCES `individual` (`IndividualID`) ON UPDATE CASCADE ON DELETE SET NULL
) TYPE=InnoDB;

---
-- Table contains the criteria for saved searches
---

DROP TABLE IF EXISTS `searchcriteria`;
CREATE TABLE `searchcriteria` (
  `SearchCriteriaID` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT, 
  `SearchID` INT(11) UNSIGNED NOT NULL,
  `SearchTableID` INT(11) UNSIGNED NOT NULL,
  `SearchFieldID` INT(11) UNSIGNED NOT NULL,
  `ConditionID` INT(11) UNSIGNED NOT NULL,
  `ExpressionType` ENUM('AND','OR') NOT NULL DEFAULT 'AND',
  `Value` VARCHAR(255) DEFAULT NULL,
  `CriteriaGroup` INT(11) UNSIGNED NOT NULL,
  PRIMARY KEY  (`SearchCriteriaID`),
  UNIQUE KEY `SearchCriteriaID` (`SearchCriteriaID`), 
  INDEX (`SearchID`),
  INDEX (`SearchTableID`),
  INDEX (`SearchFieldID`),
  FOREIGN KEY (`SearchID`) REFERENCES `search` (`SearchID`) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (`SearchTableID`) REFERENCES `searchtable` (`SearchTableID`) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (`SearchFieldID`) REFERENCES `searchfield` (`SearchFieldID`) ON UPDATE CASCADE ON DELETE CASCADE
) TYPE=InnoDB;

SELECT @entityModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('entity');

SELECT @individualModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('individual');

SELECT @noteModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('note');

SELECT @fileModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('cvfile');

SELECT @opportunityModuleId := moduleid FROM module WHERE 
UPPER(primarytable) like UPPER('opportunity%');

SELECT @proposalModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('proposal');

SELECT @projectModuleId := moduleid FROM module WHERE
UPPER(primarytable) = UPPER('project');

SELECT @taskModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('task');

SELECT @timeslipModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('timeslip');

SELECT @ticketModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('ticket');

SELECT @faqModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('faq');

SELECT @knowledgebaseModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('knowledgebase');

SELECT @orderhistoryModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('cvorder');

SELECT @invoiceModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('invoice');



-- Entity
INSERT INTO `searchtable` (`DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Entity', 'entity', 'EntityID', NULL);

set @entityTableID = LAST_INSERT_ID();

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@entityModuleId, @entityTableID, 'Y');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@individualModuleId, @entityTableID, 'N');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@entityTableID, 'Entity ID', 'EntityID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@entityTableID, 'ID 2', 'ExternalID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@entityTableID, 'Entity Name', 'Name');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@entityTableID, 'Marketing List', 'List');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@entityTableID, 'Created By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = entity.Creator');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@entityTableID, 'Modified By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = entity.Modified');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@entityTableID, 'Owned By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = entity.Owner');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@entityTableID, 'Creation Date', 'Created');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@entityTableID, 'Modified Date', 'Modified');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@entityTableID, 'Source', 'source.Name', 'N', 'source',
'source.SourceID = entity.Source');




-- Individual
INSERT INTO `searchtable` (`DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Individual', 'individual', 'IndividualID', NULL);

set @individualTableID = LAST_INSERT_ID();

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@entityModuleId, @individualTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@individualModuleId, @individualTableID, 'Y');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@individualTableID, 'Individual ID', 'IndividualID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@individualTableID, 'ID 2', 'ExternalID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@individualTableID, 'Entity Name', 'entity.Name', 'N', 'entity',
'individual.Entity = entity.EntityID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@individualTableID, 'First Name', 'FirstName');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@individualTableID, 'Last Name', 'LastName');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@individualTableID, 'Middle Initial', 'MiddleInitial');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@individualTableID, 'Title', 'Title');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@individualTableID, 'Marketing List', 'list');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@individualTableID, 'Creation Date', 'Created');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@individualTableID, 'Modified Date', 'Modified');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@individualTableID, 'Source', 'source.Name', 'N', 'source',
'source.SourceID = individual.Source');




-- Method of Contact
INSERT INTO `searchtable` (`DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Method Of Contact', 'methodofcontact', 'MOCID', NULL);

set @methodOfContactTableID = LAST_INSERT_ID();

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@entityModuleId, @methodOfContactTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@individualModuleId, @methodOfContactTableID, 'N');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@methodOfContactTableID, 'Content', 'Content');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@methodOfContactTableID, 'Sync As', 'syncas');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@methodOfContactTableID, 'Note', 'Note');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@methodOfContactTableID, 'Type', 'moctype.Name', 'N', 'moctype',
'moctype.MOCTypeID = methodofcontact.MOCType');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@methodOfContactTableID, @entityTableID, 'entity.entityID = mocrelate.ContactId AND mocrelate.MOCID = methodofcontact.MOCID AND mocrelate.ContactType = 1');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@methodOfContactTableID, @individualTableID, 'individual.IndividualID = mocrelate.ContactId AND mocrelate.MOCID = methodofcontact.MOCID AND mocrelate.ContactType = 2');


-- Address
INSERT INTO `searchtable` (`DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Address', 'address', 'AddressID', NULL);

set @addressTableID = LAST_INSERT_ID();

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@entityModuleId, @addressTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@individualModuleId, @addressTableID, 'N');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@addressTableID, 'Street 1', 'Street1');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@addressTableID, 'Street 2', 'Street2');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@addressTableID, 'State', 'state.Name', 'N', 'state',
'state.StateID = address.State');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@addressTableID, 'City', 'City');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@addressTableID, 'Zip Code', 'Zip');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@addressTableID, 'Country', 'country.Name', 'N', 'country',
'country.CountryID = address.Country');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@addressTableID, 'Website', 'Website');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@addressTableID, @entityTableID, 'entity.entityID = addressrelate.Contact AND addressrelate.Address = address.AddressID AND addressrelate.ContactType = 1');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@addressTableID, @individualTableID, 'individual.IndividualID = addressrelate.Contact AND addressrelate.Address = address.AddressID AND addressrelate.ContactType = 2');


-- Custom Fields
INSERT INTO `searchtable` (`DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Custom Fields', 'customfield', 'CustomFieldID', NULL);

set @customFieldTableID = LAST_INSERT_ID();

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@entityModuleId, @customFieldTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@individualModuleId, @customFieldTableID, 'N');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@customFieldTableID, 'Name', 'Name');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@customFieldTableID, 'Custom Field Type', 'FieldType');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@customFieldTableID, 'Scalar Value', 'customfieldscalar.Value', 'N', 'customfieldscalar',
'customfieldscalar.CustomFieldID = customfield.CustomFieldID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@customFieldTableID, 'Multiple Value', 'customfieldvalue.Value', 'N', 'customfieldvalue',
'customfieldvalue.ValueID = customfieldmultiple.ValueID = AND customfieldmultiple.CustomFieldID = customfield.CustomFieldID');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@customFieldTableID, @entityTableID, 'customfield.RecordType = 1');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@customFieldTableID, @individualTableID, 'customfield.RecordType = 2');




-- Activities
INSERT INTO `searchtable` (`DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Activities', 'activity', 'ActivityID', NULL);

set @activityTableID = LAST_INSERT_ID();

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@entityModuleId, @activityTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@individualModuleId, @activityTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
SELECT moduleid, @activityTableID AS SearchTableID, 'N' AS IsPrimaryTable
FROM module WHERE UPPER(primarytable) = UPPER('activity');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@activityTableID, 'Activity ID', 'ActivityID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@activityTableID, 'Title', 'Title');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@activityTableID, 'Details', 'Details');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@activityTableID, 'Type', 'activitytype.Name', 'N', 'activitytype',
'activitytype.TypeID = activity.Type');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@activityTableID, 'Status', 'activitystatus.Name', 'N', 'activitystatus',
'activitystatus.StatusID = activity.Status');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@activityTableID, 'Priority', 'activitypriority.Name', 'N', 'activitypriority',
'activitypriority.PriorityID = activity.Priority');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@activityTableID, 'Owned By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = activity.Owner');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@activityTableID, 'Created By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = activity.Creator');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@activityTableID, 'Modified By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = activity.ModifiedBy');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@activityTableID, 'Creation Date', 'Created');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@activityTableID, 'Modified Date', 'Modified');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@activityTableID, 'Start Date', 'Start');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@activityTableID, 'End Date', 'End');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@activityTableID, 'Notes', 'Notes');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@activityTableID, 'Visibility', 'visibility');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@activityTableID, @entityTableID, 'activity.ActivityID = activitylink.ActivityID AND activitylink.RecordTypeID = 1 AND activitylink.RecordID = entity.EntityID');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@activityTableID, @individualTableID, 'activity.ActivityID = activitylink.ActivityID AND activitylink.RecordTypeID = 2 AND activitylink.RecordID = individual.IndividualID');




-- Notes
INSERT INTO `searchtable` (`DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Notes', 'note', 'NoteID', NULL);

set @noteTableID = LAST_INSERT_ID();

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@entityModuleId, @noteTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@individualModuleId, @noteTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@noteModuleId, @noteTableID, 'Y');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@noteTableID, 'Note ID', 'NoteID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@noteTableID, 'Title', 'Title');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@noteTableID, 'Details', 'Detail');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@noteTableID, 'Created By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = note.Creator');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@noteTableID, 'Modified By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = note.UpdatedBy');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@noteTableID, 'Owned By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = note.Owner');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@noteTableID, 'Creation Date', 'DateCreated');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@noteTableID, 'Modified Date', 'DateUpdated');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@noteTableID, 'Related Entity', 'entity.name', 'N', 'entity',
'entity.EntityID = note.RelateEntity');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@noteTableID, 'Related Individual', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = note.RelateIndividual');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@noteTableID, @entityTableID, 'note.RelateEntity = entity.EntityID');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@noteTableID, @individualTableID, 'note.RelateIndividual = individual.IndividualID');




-- Email
INSERT INTO `searchtable` (`DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Email', 'emailmessage', 'MessageID', NULL);

set @emailTableID = LAST_INSERT_ID();

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@entityModuleId, @emailTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@individualModuleId, @emailTableID, 'N');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@emailTableID, 'Message ID', 'MessageID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@emailTableID, 'Subject', 'Subject');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@emailTableID, 'Message Body', 'Body');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@emailTableID, 'Created By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = emailmessage.CreatedBy');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@emailTableID, 'Sent From', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = emailmessage.UpdatedBy');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@emailTableID, 'Owned By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = emailmessage.Owner');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@emailTableID, 'Message Date', 'MessageDate');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@emailTableID, @entityTableID, 'emailmessage.AccountID = emailaccount.AccountID AND emailaccount.Owner = individual.IndividualID AND individual.Entity = entity.EntityID');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@emailTableID, @individualTableID, 'emailmessage.AccountID = emailaccount.AccountID AND emailaccount.Owner = individual.IndividualID');




-- Files
INSERT INTO `searchtable` (`DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Files', 'cvfile', 'FileID', NULL);

set @fileTableID = LAST_INSERT_ID();

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@entityModuleId, @fileTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@individualModuleId, @fileTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@fileModuleId, @fileTableID, 'Y');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@fileTableID, 'File ID', 'FileID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@fileTableID, 'Title', 'Title');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@fileTableID, 'Description', 'Description');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@fileTableID, 'Name', 'Name');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@fileTableID, 'Owned By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = cvfile.Owner');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@fileTableID, 'Created By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = cvfile.Creator');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@fileTableID, 'Modified By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = cvfile.UpdatedBy');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@fileTableID, 'Author', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = cvfile.Author');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@fileTableID, 'Creation Date', 'Created');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@fileTableID, 'Modified Date', 'Updated');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@fileTableID, 'Version', 'Version');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@fileTableID, 'Status', 'Status');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@fileTableID, 'Visibility', 'visibility');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@fileTableID, 'Temporary File', 'IsTemporary');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@fileTableID, 'Related Entity', 'entity.name', 'N', 'entity',
'entity.EntityID = cvfile.RelateEntity');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@fileTableID, 'Related Individual', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = cvfile.RelateIndividual');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@fileTableID, @entityTableID, 'cvfile.RelateEntity = entity.EntityID');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@fileTableID, @individualTableID, 'cvfile.RelateIndividual = individual.IndividualID');



-- Opportunity
INSERT INTO `searchtable` (`DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Opportunities', 'opportunity', 'OpportunityID', NULL);

set @opportunityTableID = LAST_INSERT_ID();

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@entityModuleId, @opportunityTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@individualModuleId, @opportunityTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@opportunityModuleId, @opportunityTableID, 'Y');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@proposalModuleId, @opportunityTableID, 'N');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@opportunityTableID, 'Opportunity ID', 'OpportunityID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@opportunityTableID, 'Title', 'Title');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@opportunityTableID, 'Description', 'Description');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@opportunityTableID, 'Type', 'salestype.Name', 'N', 'salestype',
'salestype.SalesTypeID = opportunity.TypeID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@opportunityTableID, 'Status', 'salesstatus.Name', 'N', 'salesstatus',
'salesstatus.SalesStatusID = opportunity.Status');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@opportunityTableID, 'Stage', 'salesstage.Name', 'N', 'salesstage',
'salesstage.SalesStageID = opportunity.Stage');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@opportunityTableID, 'Probability', 'salesprobability.Title', 'N', 'salesprobability',
'salesprobability.ProbabilityID = opportunity.Probability');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@opportunityTableID, 'Source', 'source.Name', 'N', 'source',
'source.SourceID = opportunity.Source');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@opportunityTableID, 'Forecast Amount', 'ForecastAmmount');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@opportunityTableID, 'Actual Amount', 'ActualAmount');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@opportunityTableID, 'Owner', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = activity.Owner AND activity.ActivityID = opportunity.ActivityID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@opportunityTableID, 'Account Manager', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = opportunity.AccountManager');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@opportunityTableID, 'Entity', 'entity.name', 'N', 'entity',
'entity.EntityID = opportunity.EntityID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@opportunityTableID, 'Individual', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = opportunity.IndividualID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@opportunityTableID, 'Account Team', 'grouptbl.Name', 'N', 'grouptbl',
'grouptbl.GroupID = opportunity.AccountTeam');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@opportunityTableID, 'Estimated Close Date', 'activity.Start', 'N', 'activity',
'activity.ActivityID = opportunity.ActivityID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@opportunityTableID, 'Actual Close Date', 'activity.CompletedDate', 'N', 'activity',
'activity.ActivityID = opportunity.ActivityID');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@opportunityTableID, @entityTableID, 'opportunity.EntityID = entity.EntityID');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@opportunityTableID, @individualTableID, 'opportunity.IndividualID = individual.IndividualID');




-- Proposal
INSERT INTO `searchtable` (`DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Proposals', 'proposal', 'ProposalID', NULL);

set @proposalTableID = LAST_INSERT_ID();

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@entityModuleId, @proposalTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@individualModuleId, @proposalTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@opportunityModuleId, @proposalTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@proposalModuleId, @proposalTableID, 'Y');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@proposalTableID, 'Proposal ID', 'ProposalID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@proposalTableID, 'Title', 'Title');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@proposalTableID, 'Description', 'Description');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@proposalTableID, 'Type', 'salestype.Name', 'N', 'salestype',
'salestype.SalesTypeID = proposal.TypeID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@proposalTableID, 'Status', 'salesstatus.Name', 'N', 'salesstatus',
'salesstatus.SalesStatusID = proposal.Status');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@proposalTableID, 'Stage', 'salesstage.Name', 'N', 'salesstage',
'salesstage.SalesStageID = proposal.Stage');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@proposalTableID, 'Probability', 'salesprobability.Title', 'N', 'salesprobability',
'salesprobability.ProbabilityID = proposal.Probability');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@proposalTableID, 'Source', 'source.Name', 'N', 'source',
'source.SourceID = proposal.Source');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@proposalTableID, 'Forecast Amount', 'ForecastAmmount');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@proposalTableID, 'Actual Amount', 'ActualAmount');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@proposalTableID, 'Owner', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = proposal.Owner');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@proposalTableID, 'Account Manager', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = proposal.AccountManager');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@proposalTableID, 'Account Team', 'grouptbl.Name', 'N', 'grouptbl',
'grouptbl.GroupID = proposal.AccountTeam');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@proposalTableID, 'Estimated Close Date', 'EstimatedCloseDate');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@proposalTableID, 'Actual Close Date', 'ActualCloseDate');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@proposalTableID, @entityTableID, 'proposal.OpportunityID = opportunity.OpportunityID AND opportunity.EntityID = entity.EntityID');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@proposalTableID, @individualTableID, 'proposal.OpportunityID = opportunity.OpportunityID AND opportunity.IndividualID = individual.IndividualID');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@proposalTableID, @opportunityTableID, 'proposal.OpportunityID = opportunity.OpportunityID');


-- Project
INSERT INTO `searchtable` (`DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Projects', 'project', 'ProjectID', NULL);

set @projectTableID = LAST_INSERT_ID();

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@entityModuleId, @projectTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@individualModuleId, @projectTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@projectModuleId, @projectTableID, 'Y');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@projectTableID, 'Project ID', 'ProjectID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@projectTableID, 'Title', 'ProjectTitle');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@projectTableID, 'Description', 'Description');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@projectTableID, 'Status', 'projectstatus.Title', 'N', 'projectstatus',
'projectstatus.StatusID = project.StatusID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@projectTableID, 'Start Date', 'Start');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@projectTableID, 'End Date', 'End');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@projectTableID, 'End Date', 'End');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@projectTableID, 'Budgeted Hours', 'BudgetedHours');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@projectTableID, 'Hours Used', 'HoursUsed');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@projectTableID, 'Manager', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = project.Manager');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@projectTableID, 'Owner', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = project.Owner');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@projectTableID, 'Created By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = project.Creator');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@projectTableID, 'Modified By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = project.ModifiedBy');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@projectTableID, 'Creation Date', 'Created');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@projectTableID, 'Modified Date', 'Modified');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@projectTableID, @entityTableID, 'project.ProjectID = projectlink.ProjectID AND projectlink.RecordTypeID = 14 AND projectlink.RecordID = entity.EntityID');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@projectTableID, @individualTableID, 'project.Owner = individual.IndividualID');




-- Tasks
INSERT INTO `searchtable` (`DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Tasks', 'task', 'ActivityID', NULL);

set @taskTableID = LAST_INSERT_ID();

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@entityModuleId, @taskTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@individualModuleId, @taskTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@projectModuleId, @taskTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@taskModuleId, @taskTableID, 'Y');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@taskTableID, 'Task ID', 'ActivityID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@taskTableID, 'Milestone', 'Milestone');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@taskTableID, 'Percent Complete', 'PercentComplete');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@taskTableID, 'Start Date', 'activity.Start', 'N', 'activity',
'activity.ActivityID = task.ActivityID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@taskTableID, 'Due Date', 'activity.DueDate', 'N', 'activity',
'activity.ActivityID = task.ActivityID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@activityTableID, 'Status', 'activitystatus.Name', 'N', 'activitystatus',
'activity.ActivityID = task.ActivityID AND activitystatus.StatusID = activity.Status');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@taskTableID, 'Assigned To', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = taskassigned.AssignedTo AND taskassigned.TaskID = task.ActivityID');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@taskTableID, @entityTableID, 'task.ProjectID = project.ProjectID AND project.ProjectID = projectlink.ProjectID AND projectlink.RecordTypeID = 14 AND projectlink.RecordID = entity.EntityID');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@taskTableID, @individualTableID, 'task.ProjectID = project.ProjectID AND project.Owner = individual.IndividualID');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@taskTableID, @projectTableID, 'task.ProjectID = project.ProjectID');


-- Tickets
INSERT INTO `searchtable` (`DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Tickets', 'ticket', 'ticketid', NULL);

set @ticketTableID = LAST_INSERT_ID();

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@entityModuleId, @ticketTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@individualModuleId, @ticketTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@ticketModuleId, @ticketTableID, 'Y');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@ticketTableID, 'Ticket ID', 'ticketid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@ticketTableID, 'Subject', 'subject');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@ticketTableID, 'Description', 'description');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@ticketTableID, 'Assigned To', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = ticket.assignedto');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@ticketTableID, 'Manager', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = ticket.manager');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@ticketTableID, 'Created By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = ticket.createdby');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@ticketTableID, 'Modified By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = ticket.modifiedby');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@ticketTableID, 'Owned By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = ticket.owner');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@ticketTableID, 'Status', 'supportstatus.name', 'N', 'supportstatus',
'supportstatus.statusid = ticket.status');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@ticketTableID, 'Priority', 'supportpriority.name', 'N', 'supportpriority',
'supportpriority.priorityid = ticket.priority');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@ticketTableID, 'Creation Date', 'created');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@ticketTableID, 'Modification Date', 'modified');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@ticketTableID, 'Open/Closed Status', 'ocstatus');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@ticketTableID, @entityTableID, 'ticket.entityid = entity.EntityID');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@ticketTableID, @individualTableID, 'ticket.individualid = individual.IndividualID');


-- Timeslips
INSERT INTO `searchtable` (`DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Timeslips', 'timeslip', 'TimeSlipID', NULL);

set @timeslipTableID = LAST_INSERT_ID();

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@entityModuleId, @timeslipTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@individualModuleId, @timeslipTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@projectModuleId, @timeslipTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@taskModuleId, @timeslipTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@ticketModuleId, @timeslipTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@timeslipModuleId, @timeslipTableID, 'Y');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@timeslipTableID, 'Timeslip ID', 'TimeSlipID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@timeslipTableID, 'Description', 'Description');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@timeslipTableID, 'Duration (in hours)', 'Hours');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@timeslipTableID, 'Break Time (in hours)', 'BreakTime');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@timeslipTableID, 'Date', 'Date');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@timeslipTableID, 'Start Time', 'Start');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@timeslipTableID, 'End Time', 'End');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@timeslipTableID, 'Created By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = timeslip.CreatedBy');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@timeslipTableID, @entityTableID, 'timeslip.ProjectID = project.ProjectID AND project.ProjectID = projectlink.ProjectID AND projectlink.RecordTypeID = 14 AND projectlink.RecordID = entity.EntityID');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@timeslipTableID, @individualTableID, 'timeslip.ProjectID = project.ProjectID AND project.Owner = individual.IndividualID');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@timeslipTableID, @projectTableID, 'timeslip.ProjectID = project.ProjectID');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@timeslipTableID, @taskTableID, 'timeslip.ActivityID = task.ActivityID');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@timeslipTableID, @ticketTableID, 'timeslip.TicketID = ticket.ticketid');



-- FAQs
INSERT INTO `searchtable` (`DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('FAQs', 'faq', 'faqid', NULL);

set @faqTableID = LAST_INSERT_ID();

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@faqModuleId, @faqTableID, 'Y');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@faqTableID, 'FAQ ID', 'faqid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@faqTableID, 'Title', 'title');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@faqTableID, 'Detail', 'detail');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@faqTableID, 'Status', 'status');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@faqTableID, 'Created By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = faq.createdby');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@faqTableID, 'Modified By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = faq.updatedby');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@faqTableID, 'Owned By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = faq.owner');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@faqTableID, 'Creation Date', 'created');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@faqTableID, 'Modification Date', 'updated');




-- Knowledgebase
INSERT INTO `searchtable` (`DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Knowledgebase', 'knowledgebase', 'kbid', NULL);

set @knowledgebaseTableID = LAST_INSERT_ID();

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@knowledgebaseModuleId, @knowledgebaseTableID, 'Y');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@knowledgebaseTableID, 'Knowledgebase ID', 'kbid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@knowledgebaseTableID, 'Title', 'title');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@knowledgebaseTableID, 'Detail', 'detail');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@knowledgebaseTableID, 'Status', 'status');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@knowledgebaseTableID, 'Category', 'category.title', 'N', 'category',
'category.catid = knowledgebase.category');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@knowledgebaseTableID, 'Created By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = knowledgebase.createdby');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@knowledgebaseTableID, 'Modified By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = knowledgebase.updatedby');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@knowledgebaseTableID, 'Owned By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = knowledgebase.owner');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@knowledgebaseTableID, 'Creation Date', 'created');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@knowledgebaseTableID, 'Modification Date', 'updated');



-- Order History
INSERT INTO `searchtable` (`DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Order History', 'cvorder', 'orderid', NULL);

set @orderHistoryTableID = LAST_INSERT_ID();

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@entityModuleId, @orderHistoryTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@individualModuleId, @orderHistoryTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@orderhistoryModuleId, @orderHistoryTableID, 'Y');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@orderHistoryTableID, 'Order ID', 'orderid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@orderHistoryTableID, 'Title', 'title');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@orderHistoryTableID, 'Notes', 'description');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@orderHistoryTableID, 'Status', 'accountingstatus.title', 'N', 'accountingstatus',
'accountingstatus.statusid = cvorder.status');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@orderHistoryTableID, 'Terms', 'accountingterms.title', 'N', 'accountingterms',
'accountingterms.termsid = cvorder.terms');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@orderHistoryTableID, 'Subtotal', 'subtotal');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@orderHistoryTableID, 'Tax', 'tax');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@orderHistoryTableID, 'Shipping', 'shipping');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@orderHistoryTableID, 'Discount', 'discount');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@orderHistoryTableID, 'Total', 'total');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@orderHistoryTableID, 'Owner', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = cvorder.owner');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@orderHistoryTableID, 'Created By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = cvorder.creator');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@orderHistoryTableID, 'Modified By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = cvorder.modifiedby');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@orderHistoryTableID, 'Account Manager', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = cvorder.accountmgr');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@orderHistoryTableID, 'Creation Date', 'created');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@orderHistoryTableID, 'Modified Date', 'modified');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@orderHistoryTableID, 'Order Date', 'orderdate');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@orderHistoryTableID, 'P. O. Number', 'ponumber');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@orderHistoryTableID, @entityTableID, 'cvorder.entityid = entity.EntityID');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@orderHistoryTableID, @individualTableID, 'cvorder.entityid = entity.EntityID AND individual.Entity = entity.EntityID');


-- invoiceModuleId
-- Invoice History
INSERT INTO `searchtable` (`DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Invoice History', 'invoice', 'InvoiceID', NULL);

set @invoiceHistoryTableID = LAST_INSERT_ID();

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@entityModuleId, @invoiceHistoryTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@individualModuleId, @invoiceHistoryTableID, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@invoiceModuleId, @invoiceHistoryTableID, 'Y');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@orderhistoryModuleId, @invoiceHistoryTableID, 'N');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@invoiceHistoryTableID, 'Invoice ID', 'InvoiceID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@invoiceHistoryTableID, 'External ID', 'ExternalID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@invoiceHistoryTableID, 'Title', 'title');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@invoiceHistoryTableID, 'Description', 'description');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@invoiceHistoryTableID, 'Status', 'accountingstatus.title', 'N', 'accountingstatus',
'accountingstatus.statusid = invoice.Status');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@invoiceHistoryTableID, 'Terms', 'accountingterms.title', 'N', 'accountingterms',
'accountingterms.termsid = invoice.Terms');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@invoiceHistoryTableID, 'Subtotal', 'SubTotal');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@invoiceHistoryTableID, 'Tax', 'Tax');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@invoiceHistoryTableID, 'Shipping', 'Shipping');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@invoiceHistoryTableID, 'Discount', 'Discount');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@invoiceHistoryTableID, 'Total', 'Total');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@invoiceHistoryTableID, 'Owner', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = invoice.Owner');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@invoiceHistoryTableID, 'Created By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = invoice.Creator');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@invoiceHistoryTableID, 'Account Manager', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = invoice.accountmgr');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@invoiceHistoryTableID, 'Creation Date', 'created');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@invoiceHistoryTableID, 'Modified Date', 'modified');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@invoiceHistoryTableID, 'P. O. Number', 'ponumber');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@invoiceHistoryTableID, @entityTableID, 'invoice.CustomerID = entity.EntityID');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@invoiceHistoryTableID, @individualTableID, 'invoice.CustomerID = entity.EntityID AND individual.Entity = entity.EntityID');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@invoiceHistoryTableID, @orderHistoryTableID, 'invoice.OrderID = cvorder.orderid');

