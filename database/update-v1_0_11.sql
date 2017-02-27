-- Fix the opportunity title field to not truncate at 25 chars.
ALTER TABLE `opportunity` CHANGE title title varchar(255) NOT NULL;

-- Change the Address table state and country fields
-- to be text fields.  While preserving the current values
CREATE TEMPORARY TABLE addressChange SELECT a.addressid, b.name AS stateName, c.name AS countryName FROM address AS a LEFT OUTER JOIN state AS b ON a.state = b.stateid LEFT OUTER JOIN country AS c ON a.country = c.countryid;

ALTER TABLE `address` CHANGE `state` `state` varchar(255) NOT NULL DEFAULT "";
ALTER TABLE `address` CHANGE `country` `country` varchar(255) NOT NULL DEFAULT "";
ALTER TABLE `address` DROP INDEX `state`;
ALTER TABLE `address` DROP INDEX `country`;

UPDATE `address` AS a, `addressChange` AS b SET a.`state`=b.`stateName`, a.`country`=b.`countryName` WHERE a.`addressid`=b.`addressid`;

-- Update advanced search to compensate for the address field chagnes
UPDATE `searchfield` SET `fieldname`='state', `isontable` = 'Y', `realtablename` = NULL, `relationshipquery` = NULL WHERE `searchfieldid` = 28;
UPDATE `searchfield` SET `fieldname`='country', `isontable` = 'Y', `realtablename` = NULL, `relationshipquery` = NULL WHERE `searchfieldid` = 31;

-- This is data for fixing bug # 1509
-- Adds a ticket Number column
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Ticket','Number',8);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES ('66','Number',7);
UPDATE viewcolumns SET columnorder = 1 WHERE (viewid=66) AND (columnname='Number');
UPDATE viewcolumns SET columnorder = 2 WHERE (viewid=66) AND (columnname='Subject');
UPDATE viewcolumns SET columnorder = 3 WHERE (viewid=66) AND (columnname='Entity');
UPDATE viewcolumns SET columnorder = 4 WHERE (viewid=66) AND (columnname='DateOpened');
UPDATE viewcolumns SET columnorder = 5 WHERE (viewid=66) AND (columnname='Status');
UPDATE viewcolumns SET columnorder = 6 WHERE (viewid=66) AND (columnname='DateClosed');
UPDATE viewcolumns SET columnorder = 7 WHERE (viewid=66) AND (columnname='AssignedTo');

-- password update
ALTER TABLE `user` MODIFY `Password` varchar(50) DEFAULT NULL;

-- thread limit
ALTER TABLE `thread` MODIFY `title` varchar(255);

-- email stuff.
DROP TABLE IF EXISTS `supportemaildetails`;
DROP TABLE IF EXISTS `supportemailmatter`;
DROP TABLE IF EXISTS `supportemailmessage`;
CREATE TABLE `supportemailmessage` (
  `Name` varchar(100) NOT NULL default '',
  `MessageBody` text NULL default NULL
) TYPE=InnoDB;

insert into `supportemailmessage` (Name) values('Ticket');
insert into `supportemailmessage` (Name) values('Thread');
insert into `supportemailmessage` (Name) values('Error');

-- Reports Search Integration:
UPDATE `report` SET `moduleid`=14 WHERE `moduleid`=1;
UPDATE `report` SET `moduleid`=30 WHERE `moduleid`=7;
UPDATE `report` SET `moduleid`=33 WHERE `moduleid`=8;
UPDATE `report` SET `moduleid`=36 WHERE `moduleid`=9;
UPDATE `report` SET `moduleid`=39 WHERE `moduleid`=10;
UPDATE `report` SET `moduleid`=42 WHERE `moduleid`=12;
UPDATE `report` SET `moduleid`=52 WHERE `moduleid`=13;

DROP TABLE IF EXISTS `ReportSearch`;
DROP TABLE IF EXISTS `reportsearch`;
CREATE TABLE `reportsearch` (
  `SearchID` int(11) unsigned NOT NULL,
  `ReportID` int(11) unsigned NOT NULL,
  INDEX `SearchID`(`SearchID`),
  UNIQUE KEY `ReportID` (`ReportID`),
  CONSTRAINT `0_9382` FOREIGN KEY (`SearchID`) REFERENCES `search` (`SearchId`)
) TYPE=InnoDB;

DROP TABLE IF EXISTS `inventorystatus`;
CREATE TABLE `inventorystatus` (
  `StatusID` int(11) unsigned NOT NULL,
  `StatusName` character(50) NOT NULL,
  INDEX `StatusID`(`StatusID`)
) TYPE=InnoDB;

insert into `inventorystatus`(`StatusID`,`StatusName`) values (1,'Avalible');
insert into `inventorystatus`(`StatusID`,`StatusName`) values (2,'Back Ordered');
insert into `inventorystatus`(`StatusID`,`StatusName`) values (1,'Not Avalible');

-- Adding new searchtables
INSERT INTO `searchtable` (`DisplayName`, `TableName`,
`TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Marketing List', 'marketinglist', 'ListId', '');

INSERT INTO `searchtable` (`DisplayName`, `TableName`,
`TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Promotions', 'promotion', 'PromotionID', '');

INSERT INTO `searchtable` (`DisplayName`, `TableName`,
`TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Literature Request', 'literaturerequest', 'ActivityID', '');

INSERT INTO `searchtable` (`DisplayName`, `TableName`,
`TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Event', 'event', 'EventId', '');

INSERT INTO `searchtable` (`DisplayName`, `TableName`,
`TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Payment', 'payment', 'paymentid', '');

INSERT INTO `searchtable` (`DisplayName`, `TableName`,
`TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Expense', 'expense', 'expenseid', '');

INSERT INTO `searchtable` (`DisplayName`, `TableName`,
`TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Purchase Order', 'purchaseporder', 'purchaseorderid', '');

INSERT INTO `searchtable` (`DisplayName`, `TableName`,
`TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Item', 'item', 'itemid', '');

INSERT INTO `searchtable` (`DisplayName`, `TableName`,
`TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('GLAccount', 'glaccount', 'glaccountsid', '');

INSERT INTO `searchtable` (`DisplayName`, `TableName`,
`TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Inventory', 'inventory', 'inventoryid', '');

INSERT INTO `searchtable` (`DisplayName`, `TableName`,
`TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Vendor', 'vendor', 'entityid', '');

INSERT INTO `searchtable` (`DisplayName`, `TableName`,
`TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Expense Form', 'expenseform', 'expenseformid', '');

INSERT INTO `searchtable` (`DisplayName`, `TableName`,
`TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Attendee', 'attendee', 'ActivityID', '');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (33, 21, 'Y');


-- adding new search fields

update searchfield set displayname = 'List' where displayname = 'Marketing List' and searchtableid = 1;

update searchfield set displayname = 'Entity Name' where displayname = 'Entity' and searchtableid = 10;

update searchfield set displayname = 'Individual Name' where displayname = 'Individual' and searchtableid = 10;

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (1, 'Account Manager', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.individualid = entity.accountmanagerid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (1, 'Account Team', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.individualid = entity.accountteamid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (2, 'Created By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.individualid = individual.createdby');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (2, 'Modified By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.individualid = individual.modifiedby');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (10, 'Created By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.individualid = activity.creator AND activity.activityid = opportunity.activityid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (10, 'Modified By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.individualid = activity.modifiedby AND activity.activityid = opportunity.activityid');

update searchfield set displayname = 'Owned By' where displayname = 'Owner' and searchtableid = 10;

update searchfield set displayname = 'Owned By' where displayname = 'Owner' and searchtableid = 11;

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (11, 'Opportunity', 'opportunity.name', 'N', 'opportunity',
'opportunity.opportunityid = proposal.opportunityid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (11, 'Individual', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.individualid = proposal.individualid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (11, 'Billing Address', 'billing', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (11, 'Shipping Address', 'shipping', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (11, 'Terms', 'terms.termname', 'N', 'terms',
'terms.termid = proposal.termid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (11, 'Item', 'proposalitem.name', 'N', 'proposalitem',
'propsalitem.proposalid = proposal.proposalid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (11, 'Created By', 'CONCAT(individual.FirstName, " ", individual.LastName) ', 'N', 'individual',
'individual.individualid = proposal.createdby');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (11, 'Modified By', 'CONCAT(individual.FirstName, " ", individual.LastName) ', 'N', 'individual',
'individual.individualid = proposal.createdby');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (12, 'Entity', 'entuty.name', 'N', 'entuty',
'entity.entityid = projectlink.recordid AND projectlink.recordtypeid = 14 AND projectlink.projectid = project.projectid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (12, 'Individual', 'CONCAT(individual.FirstName, " ", individual.LastName) ', 'N', 'individual',
'individual.individualid = projectlink.recordid AND projectlink.recordtypeid = 15 AND projectlink.projectid = project.projectid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (12, 'Team', 'grouptbl.name', 'N', 'grouptbl',
'grouptbl.groupid = projectlink.recordid AND projectlink.recordtypeid=16 AND projectlink.projectid = project.projectid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (12, 'Available', 'project.budgetedhours - project.hoursused', 'N', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (13, 'Created', 'activity.created', 'N', 'activity',
'activity.activityid = task.activityid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (13, 'Modified', 'activity.modified', 'N', 'activity',
'activity.activityid = task.activityid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (13, 'Title', 'activity.title', 'N', 'activity',
'activity.activityid = task.activityid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (13, 'Description', 'activity.description', 'N', 'activity',
'activity.activityid = task.activityid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (13, 'Project', 'project.title', 'N', 'project',
'project.projectid = task.projectid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (13, 'Parent Task', 'activity.title', 'N', 'activity',
'activity.activityid = task.parent');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (13, 'Manager', 'CONCAT(individual.FirstName, " ", individual.LastName) ', 'N', 'individual',
'individual.individualid = activity.owner AND activity.activityid = task.activityid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (13, 'Satus', 'activitystatus.name', 'N', 'activitystatus',
'activitystatus.statusid = activity.status AND activity.activityid = task.activityid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (15, 'Task', 'activity.title', 'N', 'activity',
'activity.activityid = timeslip.activityid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (14, 'Entity', 'entity.name', 'N', 'entity',
'entity.entityid = ticket.entityid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (14, 'Individual', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.individualid = ticket.individualid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (14, 'Created', 'created', 'Y', '',
'individual.individualid = ticket.individualid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (18, 'Created', 'created', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (18, 'modified', 'modified', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (18, 'Description', '', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (18, 'Entity', '', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (18, 'Billindividual', '', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (18, 'Shipindividual', '', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (18, 'Billaddress', '', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (18, 'Shipaddress', '', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (18, 'Project', '', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (18, 'Proposal', '', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (18, 'Creator', '', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (18, 'Owner', '', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (18, 'Created', '', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (18, 'Modified', '', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (18, 'Ownerstatus', '', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (19, 'OrderID', '', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (19, 'Creator', '', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (19, 'Modified By', '', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (19, 'Billaddress', '', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (19, 'Shipaddress', '', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (19, 'InvoiceStatus', '', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (19, 'Project', '', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (19, 'InvoiceDate', '', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (20, 'ListID', 'listid', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (20, 'Title', 'title', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (20, 'Description', 'description', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (20, 'Number Of Records', 'numberofrecords', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (20, 'Proposals', 'proposals', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (20, 'Orders', 'orders', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (20, 'OrderValue', 'ordervalue', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (20, 'Status', 'status', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (20, 'Owner', 'CONCAT(owner20.FirstName, " ", owner20.LastName)', 'N', 'individual owner20',
'owner20.individualid = marketinglist.owner');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (20, 'Creator', 'CONCAT(creator20.FirstName, " ", creator20.LastName)', 'N', 'individual creator20',
'creator20.individualid = marketinglist.creator');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (20, 'Modifier', 'CONCAT(modifier20.FirstName, " ", modifier20.LastName)', 'N', 'individual modifier20',
'modifier20.individualid = marketinglist.modifiedby');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (21, 'Title', 'title', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (21, 'Description', 'description', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (21, 'Start Date', 'satrtdate', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (21, 'End Date', 'enddate', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (21, 'Status', 'status', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (21, 'Item', 'item21.sku', 'N', 'item item21',
'item21.itemid = promoitem.itemid AND promoitem.promotionid = prmotion.promotionid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (22, 'Title', 'title', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (22, 'Detail', 'Detail22.detail', 'N', 'activity Detail22',
'Detail22.activityid = litereaturerequest.activityid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (22, 'Literature', 'title', 'Y', '',
'literature.literatureid = litereaturerequest.litereatureid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (22, 'Entity', 'entity22.name', 'N', 'entity entity22',
'entity22.entityid = activitylink.recordid AND activitylink.recordtypeid = 1 AND activitylink.activityid = litereaturerequest.activityid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (22, 'Individual', 'CONCAT(ind22.FirstName, " ", ind22.LastName) ', 'N', 'individual ind22',
'ind22.individualid = literaturerequest.requestedby');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (22, 'Assigned To', 'CONCAT(assto22.FirstName, " ", assto22.LastName) ', 'N', 'individual assto22',
'assto22.individualid = activity.owner AND activity.activityid = literaturerequest.activityid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (22, 'Delivery Method', 'deliverymethod22.name', 'N', 'deliverymethod deliverymethod22',
'deliverymethod22.deliverid = litereaturerequest.delivermethod');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (22, 'Status', 'activitystatus22.name', 'N', 'activitystatus activitystatus22',
'activitystatus22.statusid = activity.status AND activity.activityid = literaturerequest.activityid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (22, 'Due By', 'dueby22.duedate', 'N', 'activity dueby22',
'dueby22.activityid = literaturerequest.activityid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (23, 'Event', 'title', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (23, 'Description', 'description', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (23, 'Start', 'start', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (23, 'End', 'End', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (23, 'Who should attend', 'whoshouldattend', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (23, 'Max attendees', 'maxattendees', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (23, 'Moderator', 'CONCAT(moderator23.FirstName, " ", moderator23.LastName) ', 'N', 'individual moderator23',
'moderator23.individualid = event.moderator');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (24, 'PaymentID', 'paymentid', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (24, 'ExternalID', 'externalid', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (24, 'Title', 'title', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (24, 'Description', 'description', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (24, 'Entity', 'entity24.name', 'N', 'entity entity24',
'payment.entity = entity24.entityid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (24, 'PaymentMethod', 'paymentmethod24.title', 'N', 'paymentmethod paymentmethod24',
'Paymentmethod24.MethodID = Payment.PaymentMethod');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (24, 'Reference', 'reference', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (24, 'Amount', 'amount', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (24, 'Card Type', 'cardtype', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (24, 'Card Number', 'cardnumber', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (24, 'Expiration', 'expiration', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (24, 'Chaeck Number', 'checknumber', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (24, 'Owner', 'CONCAT(owner24.FirstName, " ", owner24.LastName) ', 'N', 'individual owner24',
'owner24.individualid = payment.owner');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (24, 'Modified By', 'modifiedby', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (24, 'Line Status', 'linestatus', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (24, 'Line Id', 'lineid', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (24, 'Modified', 'modified', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (25, 'ExpenseID', 'expenseid', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (25, 'ExternalID', 'externalid', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (25, 'Title', 'title', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (25, 'Description', 'description', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (25, 'Entity', 'entity25.name', 'N', 'entity entity25',
'expense.entity = entity25.entityid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (25, 'Amount', 'amount', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (25, 'Owner', 'CONCAT(owner25.FirstName, " ", owner25.LastName) ', 'N', 'individual owner25',
'owner25.individualid = expense.owner');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (25, 'Created', 'created', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (25, 'Modified', 'modified', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (25, 'Status', 'status', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (25, 'Project', 'project25.projecttitle', 'N', 'project project25',
'expense.project = project25.projected');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (25, 'Ticket', 'ticket25.subject', 'N', 'ticket ticket25',
'expense.ticket = ticket25.ticketid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (25, 'Opportunity', 'title', 'Y', 'opportunity',
'expense.opportunity = opportunity.opportunityid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (25, 'Line ID', 'lineid', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (25, 'Line Status', 'linestatus', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (25, 'GLAccountsID', 'glaccount25.title', 'N', 'glaccount glaccount25',
'glaccount25.glaccountid=Expense.glaccountid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (25, 'Notes', 'notes', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (25, 'Expense Form ID', 'expenseformid', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (26, 'Purchase Order ID', 'purchaseorderid', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (26, 'External ID', 'externalid', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (26, 'Title', 'title', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (26, 'Description', 'description', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (26, 'PONumber', 'ponumber', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (26, 'Entity', 'entity26.name', 'N', 'entity entity26',
'purchaseorder.entity = entity26.entityid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (26, 'SubTotal', 'subtotal', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (26, 'Shipping', 'shipping', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (26, 'Tax', 'tax', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (26, 'Discount', 'discount', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (26, 'Total', 'total', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (26, 'ShipIndividual', 'shipindividual', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (26, 'ShipAddress', 'concat(saddress26.street1,' ', saddress26.street2) ', 'N', 'address saddress26',
'purchaseorder.shipaddress = saddress26.addressid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (26, 'BillIndividual', 'concat(baddress26.street1,' ', baddress26.street2) ', 'N', 'address baddress26',
'purchaseorder.BillAddress= baddress26.addressid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (26, 'BillAddress', 'billaddress', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (26, 'Owner', 'CONCAT(owner26.FirstName, " ", owner26.LastName) ', 'N', 'individual owner26',
'owner26.individualid = purchaseorder.owner');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (26, 'Creator', 'CONCAT(creator26.FirstName, " ", creator26.LastName) ', 'N', 'individual creator26',
'creator26.individualid = purchaseorder.creator');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (26, 'Modified By', 'modifiedby', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (26, 'Modified', 'modified', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (26, 'Status', 'accountingstatus26.title', 'N', 'accountingstatus accountingstatus26',
'purchaseorder.status = accountingstatus26.statusid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (26, 'Terms', 'accountongterms26.title', 'N', 'accountongterms accountongterms26',
'purchaseorder.terms  = accountingterms26.termsid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (26, 'Account Manager', 'CONCAT(acctmgr26.FirstName, " ", acctmgr26.LastName) ', 'N', 'individual acctmgr26',
'acctmgr26.individualid = purchaseorder.accountmgr');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (26, 'Purchase Order Date', 'purchaseorderdate', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (26, 'Purchase Order Status', 'purchaseprderstatus', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (26, 'Created', 'created', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (27, 'Item ID', 'itemid', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (27, 'External ID', 'externalid', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (27, 'Title', 'title', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (27, 'Description', 'description', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (27, 'List Price', 'listprice', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (27, 'Cost', 'cost', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (27, 'Tax Class', 'title', 'Y', 'taxclass',
'taxclass. taxclass id=Item.taxclass');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (27, 'Type', 'title', 'Y', 'itemtipe',
'itemtype.Itemtypeid = Item.type');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (27, 'Sku', 'sku', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (27, 'Parent', 'parent', 'Y', '',
'item.itemid = item.parent');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (27, 'Manufacturer ID', 'manentity27.name', 'N', 'entity manentity27',
'manitem27.manufacturerid = entity.entityid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (27, 'Vendor ID', 'venentity27.name', 'N', 'entity venentity27',
'item.vendorid = venentity27.entityid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (27, 'Created By', 'CONCAT(createdby27.FirstName, " ", createdby27.LastName) ', 'N', 'individual createdby27',
'createdby27.individualid = item.createdby');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (27, 'Modified By', 'CONCAT(modifiedby27.FirstName, " ", modifiedby27.LastName) ', 'N', 'individual modifiedby27',
'modifiedby27.individualid = item.modifiedby');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (27, 'Created Date', 'createddate', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (27, 'Modified Date', 'modifieddate', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (27, 'GLAccount ID', 'glaccountid27.title', 'N', 'glaccount glaccountid27',
'glaccountid27.glaccountid=item.glaccountid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (27, 'Delete Status', 'deletestatus', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (27, 'Link To Inventory', 'linktoinventory', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (28, 'Title', 'title', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (28, 'Type', 'type', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (28, 'Balance', 'balance', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (28, 'Parent Account', 'glaccount28.title', 'N', 'glaccount glaccount28',
'glaccount28.glaccountid=glaccount28.parent');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (29, 'Inventory ID', 'inventoryid', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (29, 'Title', 'title', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (29, 'Description', 'description', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (29, 'Quantity', 'qty', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (29, 'Location ID', 'location29.title', 'N', 'location location29',
'inventory.locationid=location29.locationid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (29, 'Item', 'title', 'Y', '',
'inventory.item=item.itemid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (29, 'Status ID', 'inventorystatus29.statusname', 'N', 'inventorystatus inventorystatus29',
'inventorystatus292.statusid=inventory.statusid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (29, 'Customer ID', 'entity29.name', 'N', 'entity entity29',
'inventory.customer = entity29.entityid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (29, 'Created', 'CONCAT(created29.FirstName, " ", created29.LastName) ', 'N', 'individual created29',
'created29.individualid = inventory.created');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (29, 'Modified', 'CONCAT(modified29.FirstName, " ", modified29.LastName) ', 'N', 'individual modified29',
'modified29.individualid = inventory.modified');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (29, 'Vendor', 'entityv29.name', 'N', 'entity entityv29',
'inventory.vendor = entityv29.entityid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (29, 'Line ID', 'lineid', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (29, 'Line Status', 'linestatus', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (30, 'Entity', 'entityven30.name', 'N', 'entity entityven30',
'vendor.entityid = entityven30.entityid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (31, 'Expense Form ID', 'expenseformid', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (31, 'From Date', 'fromdate', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (31, 'To Date', 'todate', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (31, 'Description', 'description', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (31, 'Note', 'note', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (31, 'Reporting To', 'CONCAT(reportingto31.FirstName, " ", reportingto31.LastName) ', 'N', 'individual reportingto31',
'reportingto31.individualid = Expenseform.ReportingTo');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (31, 'Owner', 'CONCAT(owner31.FirstName, " ", owner31.LastName) ', 'N', 'individual owner31',
'owner31.individualid = Expenseform.owner');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (31, 'Creator', 'CONCAT(creator31.FirstName, " ", creator31.LastName) ', 'N', 'individual creator31',
'creator31.individualid = Expenseform.Creator');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (31, 'Modified By', 'CONCAT(modifiedby31.FirstName, " ", modifiedby31.LastName) ', 'N', 'individual modifiedby31',
'modifiedby31.individualid = Expenseform.modifiedby');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (31, 'Created', 'created', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (31, 'Modified', 'modified', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (31, 'Status', 'status', 'Y', '',
'');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (32, 'Name', 'CONCAT(name32.FirstName, " ", name32.LastName) ', 'N', 'individual name32',
'name32.individualid = attendee.individualid AND attendee.activityid = activity.activityid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (32, 'Status', 'attendeestatus32.name', 'N', 'attendeestatus attendeestatus32',
'attendeestatus32.statusid = attendee.status AND attendee.activityid = activity.activityid');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (32, 'Type', 'type', 'Y', '',
'attendee.activityid = activity.activityid');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (52, 31, 'Y');

-- Put it here Yo

update `searchfield` set FieldName = 'CONCAT(createdby1.FirstName, " ", createdby1.LastName)' ,
RealTableName = 'individual createdby1' , RelationshipQuery = 'createdby1.IndividualID = entity.Creator' where
SearchTableID = 1 and DisplayName = 'Created By';

update `searchfield` set FieldName = 'CONCAT(modifiedby1.FirstName, " ", modifiedby1.LastName)' ,
RealTableName = 'individual modifiedby1' , RelationshipQuery = 'modifiedby1.IndividualID = entity.Modified' where
SearchTableID = 1 and DisplayName = 'Modified By';

update `searchfield` set FieldName = 'CONCAT(ownedby1.FirstName, " ", ownedby1.LastName)' ,
RealTableName = 'individual ownedby1' , RelationshipQuery = 'ownedby1.IndividualID = entity.Owner' where
SearchTableID = 1 and DisplayName = 'Owned By';

update `searchfield` set FieldName = 'source1.Name' ,
RealTableName = 'source source1' , RelationshipQuery = 'source1.SourceID = entity.Source' where
SearchTableID = 1 and DisplayName = 'Source';


-- Individual
update `searchfield` set FieldName = 'entity2.Name' ,
RealTableName = 'entity entity2' , RelationshipQuery = 'individual.Entity = entity2.EntityID' where
SearchTableID = 2 and DisplayName = 'Entity Name';

update `searchfield` set FieldName = 'source2.Name' ,
RealTableName = 'source source2' , RelationshipQuery = 'source2.SourceID = individual.Source' where
SearchTableID = 2 and DisplayName = 'Source';




-- Method of Contact
update `searchfield` set FieldName = 'moctype3.Name' ,
RealTableName = 'moctype moctype3' , RelationshipQuery = 'moctype3.MOCTypeID = methodofcontact.MOCType' where
SearchTableID = 3 and DisplayName = 'Type';


-- Address
update `searchfield` set FieldName = 'state4.Name' ,
RealTableName = 'state state4' , RelationshipQuery = 'state4.StateID = address.State' where
SearchTableID = 4 and DisplayName = 'State';

update `searchfield` set FieldName = 'country4.Name' ,
RealTableName = 'country country4' , RelationshipQuery = 'country4.CountryID = address.Country', IsOnTable = 'N' where
SearchTableID = 4 and DisplayName = 'Country';


-- Custom Fields
update `searchfield` set FieldName = 'customfieldscalar5.Value' ,
RealTableName = 'customfieldscalar customfieldscalar5' , RelationshipQuery = 'customfieldscalar5.CustomFieldID = customfield.CustomFieldID' where
SearchTableID = 5 and DisplayName = 'Scalar Value';

update `searchfield` set FieldName = 'customfieldvalue5.Value' ,
RealTableName = 'customfieldvalue customfieldvalue5' , RelationshipQuery = 'customfieldvalue5.ValueID = customfieldmultiple.ValueID = AND customfieldmultiple.CustomFieldID = customfield.CustomFieldID' where
SearchTableID = 5 and DisplayName = 'Multiple Value';


-- Activities
update `searchfield` set FieldName = 'activitytype6.Name' ,
RealTableName = 'activitytype activitytype6' , RelationshipQuery = 'activitytype6.TypeID = activity.Type' where
SearchTableID = 6 and DisplayName = 'Type';

update `searchfield` set FieldName = 'activitystatus6.Name' ,
RealTableName = 'activitystatus activitystatus6' , RelationshipQuery = 'activitystatus6.StatusID = activity.Status' where
SearchTableID = 6 and DisplayName = 'Status';

update `searchfield` set FieldName = 'activitypriority6.Name' ,
RealTableName = 'activitypriority activitypriority6' , RelationshipQuery = 'activitypriority6.PriorityID = activity.Priority' where
SearchTableID = 6 and DisplayName = 'Priority';

update `searchfield` set FieldName = 'CONCAT(ownedby6.FirstName, " ", ownedby6.LastName)' ,
RealTableName = 'individual ownedby6' , RelationshipQuery = 'ownedby6.IndividualID = activity.Owner' where
SearchTableID = 6 and DisplayName = 'Owned By';

update `searchfield` set FieldName = 'CONCAT(createdby6.FirstName, " ", createdby6.LastName)' ,
RealTableName = 'individual createdby6' , RelationshipQuery = 'createdby6.IndividualID = activity.Creator' where
SearchTableID = 6 and DisplayName = 'Created By';

update `searchfield` set FieldName = 'CONCAT(modifiedby6.FirstName, " ", modifiedby6.LastName)' ,
RealTableName = 'individual modifiedby6' , RelationshipQuery = 'modifiedby6.IndividualID = activity.ModifiedBy' where
SearchTableID = 6 and DisplayName = 'Modified By';



-- Notes
update `searchfield` set FieldName = 'CONCAT(createdby7.FirstName, " ", createdby7.LastName)' ,
RealTableName = 'individual createdby7' , RelationshipQuery = 'createdby7.IndividualID = note.Creator' where
SearchTableID = 7 and DisplayName = 'Created By';

update `searchfield` set FieldName = 'CONCAT(modifiedby7.FirstName, " ", modifiedby7.LastName)' ,
RealTableName = 'individual modifiedby7' , RelationshipQuery = 'modifiedby7.IndividualID = note.UpdatedBy' where
SearchTableID = 7 and DisplayName = 'Modified By';

update `searchfield` set FieldName = 'CONCAT(ownedby7.FirstName, " ", ownedby7.LastName)' ,
RealTableName = 'individual ownedby7' , RelationshipQuery = 'ownedby7.IndividualID = note.Owner' where
SearchTableID = 7 and DisplayName = 'Owned By';

update `searchfield` set FieldName = 'entity7.name' ,
RealTableName = 'entity entity7' , RelationshipQuery = 'entity7.EntityID = note.RelateEntity' where
SearchTableID = 7 and DisplayName = 'Related Entity';

update `searchfield` set FieldName = 'CONCAT(relatedind7.FirstName, " ", relatedind7.LastName)' ,
RealTableName = 'individual relatedind7' , RelationshipQuery = 'relatedind7.IndividualID = note.RelateIndividual' where
SearchTableID = 7 and DisplayName = 'Related Individual';




-- Email
update `searchfield` set FieldName = 'CONCAT(createdby8.FirstName, " ", createdby8.LastName)' ,
RealTableName = 'individual createdby8' , RelationshipQuery = 'createdby8.IndividualID = emailmessage.CreatedBy' where
SearchTableID = 8 and DisplayName = 'Created By';

update `searchfield` set FieldName = 'CONCAT(sentfrom8.FirstName, " ", sentfrom8.LastName)' ,
RealTableName = 'individual sentfrom8' , RelationshipQuery = 'sentfrom8.IndividualID = emailmessage.UpdatedBy' where
SearchTableID = 8 and DisplayName = 'Sent From';

update `searchfield` set FieldName = 'CONCAT(ownedby8.FirstName, " ", ownedby8.LastName)' ,
RealTableName = 'individual ownedby8' , RelationshipQuery = 'ownedby8.IndividualID = emailmessage.Owner' where
SearchTableID = 8 and DisplayName = 'Owned By';




-- Files
update `searchfield` set FieldName = 'CONCAT(ownedby9.FirstName, " ", ownedby9.LastName)' ,
RealTableName = 'individual ownedby9' , RelationshipQuery = 'ownedby9.IndividualID = cvfile.Owner' where
SearchTableID = 9 and DisplayName = 'Owned By';

update `searchfield` set FieldName = 'CONCAT(createdby9.FirstName, " ", createdby9.LastName)' ,
RealTableName = 'individual createdby9' , RelationshipQuery = 'createdby9.IndividualID = cvfile.Creator' where
SearchTableID = 9 and DisplayName = 'Created By';

update `searchfield` set FieldName = 'CONCAT(modifiedby9.FirstName, " ", modifiedby9.LastName)' ,
RealTableName = 'individual modifiedby9' , RelationshipQuery = 'modifiedby9.IndividualID = cvfile.UpdatedBy' where
SearchTableID = 9 and DisplayName = 'Modified By';

update `searchfield` set FieldName = 'CONCAT(author9.FirstName, " ", author9.LastName)' ,
RealTableName = 'individual author9' , RelationshipQuery = 'author9.IndividualID = cvfile.Author' where
SearchTableID = 9 and DisplayName = 'Author';

update `searchfield` set FieldName = 'entity9.name' ,
RealTableName = 'entity entity9' , RelationshipQuery = 'entity9.EntityID = cvfile.RelateEntity' where
SearchTableID = 9 and DisplayName = 'Related Entity';

update `searchfield` set FieldName = 'CONCAT(relatedind9.FirstName, " ", relatedind9.LastName)' and
RealTableName = 'individual relatedind9' and RelationshipQuery = 'relatedind9.IndividualID = cvfile.RelateIndividual' where
SearchTableID = 9 and DisplayName = 'Related Individual';


-- Opportunity
update `searchfield` set FieldName = 'salestype10.Name' ,
RealTableName = 'salestype salestype10' , RelationshipQuery = 'salestype10.SalesTypeID = opportunity.TypeID' where
SearchTableID = 10 and DisplayName = 'Type';

update `searchfield` set FieldName = 'salesstatus10.Name' ,
RealTableName = 'salesstatus salesstatus10' , RelationshipQuery = 'salesstatus10.SalesStatusID = opportunity.Status' where
SearchTableID = 10 and DisplayName = 'Status';

update `searchfield` set FieldName = 'salesstage10.Name' ,
RealTableName = 'salesstage salesstage10' , RelationshipQuery = 'salesstage10.SalesStageID = opportunity.Stage' where
SearchTableID = 10 and DisplayName = 'Stage';

update `searchfield` set FieldName = 'salesprobability10.Title' ,
RealTableName = 'salesprobability salesprobability10' , RelationshipQuery = 'salesprobability10.ProbabilityID = opportunity.Probability' where
SearchTableID = 10 and DisplayName = 'Probability';

update `searchfield` set FieldName = 'source10.Name' ,
RealTableName = 'source source10' , RelationshipQuery = 'source10.SourceID = opportunity.Source' where
SearchTableID = 10 and DisplayName = 'Source';

update `searchfield` set FieldName = 'CONCAT(owner10.FirstName, " ", owner10.LastName)' ,
RealTableName = 'individual owner10' , RelationshipQuery = 'owner10.IndividualID = activity.Owner AND activity.ActivityID = opportunity.ActivityID' where
SearchTableID = 10 and DisplayName = 'Owner';

update `searchfield` set FieldName = 'CONCAT(accountmgr10.FirstName, " ", accountmgr10.LastName)' ,
RealTableName = 'individual accountmgr10' , RelationshipQuery = 'accountmgr10.IndividualID = opportunity.AccountManager' where
SearchTableID = 10 and DisplayName = 'Account Manager';

update `searchfield` set FieldName = 'entity10.name' ,
RealTableName = 'entity entity10' , RelationshipQuery = 'entity10.EntityID = opportunity.EntityID' where
SearchTableID = 10 and DisplayName = 'Entity';

update `searchfield` set FieldName = 'CONCAT(ind10.FirstName, " ", ind10.LastName)' ,
RealTableName = 'individual ind10' , RelationshipQuery = 'ind10.IndividualID = opportunity.IndividualID' where
SearchTableID = 10 and DisplayName = 'Individual';

update `searchfield` set FieldName = 'grouptbl10.Name' ,
RealTableName = 'grouptbl grouptbl10' , RelationshipQuery = 'grouptbl10.GroupID = opportunity.AccountTeam' where
SearchTableID = 10 and DisplayName = 'Account Team';

update `searchfield` set FieldName = 'estcldat10.Start' ,
RealTableName = 'activity estcldat10' , RelationshipQuery = 'estcldat10.ActivityID = opportunity.ActivityID' where
SearchTableID = 10 and DisplayName = 'Estimated Close Date';

update `searchfield` set FieldName = 'actcldat10.CompletedDate' ,
RealTableName = 'activity actcldat10' , RelationshipQuery = 'actcldat10.ActivityID = opportunity.ActivityID' where
SearchTableID = 10 and DisplayName = 'Actual Close Date';




-- Proposal
update `searchfield` set FieldName = 'salestype11.Name' ,
RealTableName = 'salestype salestype11' , RelationshipQuery = 'salestype11.SalesTypeID = proposal.TypeID' where
SearchTableID = 11 and DisplayName = 'Type';

update `searchfield` set FieldName = 'salesstatus11.Name' ,
RealTableName = 'salesstatus salesstatus11' , RelationshipQuery = 'salesstatus11.SalesStatusID = proposal.Status' where
SearchTableID = 11 and DisplayName = 'Status';

update `searchfield` set FieldName = 'salesstage11.Name' ,
RealTableName = 'salesstage salesstage11' , RelationshipQuery = 'salesstage11.SalesStageID = proposal.Stage' where
SearchTableID = 11 and DisplayName = 'Stage';

update `searchfield` set FieldName = 'salesprobability11.Title' ,
RealTableName = 'salesprobability salesprobability11' , RelationshipQuery = 'salesprobability11.ProbabilityID = proposal.Probability' where
SearchTableID = 11 and DisplayName = 'Probability';

update `searchfield` set FieldName = 'source11.Name' ,
RealTableName = 'source source11' , RelationshipQuery = 'source11.SourceID = proposal.Source' where
SearchTableID = 11 and DisplayName = 'Source';

update `searchfield` set FieldName = 'CONCAT(owner11.FirstName, " ", owner11.LastName)' ,
RealTableName = 'individual owner11' , RelationshipQuery = 'owner11.IndividualID = proposal.Owner' where
SearchTableID = 11 and DisplayName = 'Owner';

update `searchfield` set FieldName = 'CONCAT(acctmgr11.FirstName, " ", acctmgr11.LastName)' ,
RealTableName = 'individual acctmgr11' , RelationshipQuery = 'acctmgr11.IndividualID = proposal.AccountManager' where
SearchTableID = 11 and DisplayName = 'Account Manager';

update `searchfield` set FieldName = 'grouptbl11.Name' ,
RealTableName = 'grouptbl grouptbl11' , RelationshipQuery = 'grouptbl11.GroupID = proposal.AccountTeam' where
SearchTableID = 11 and DisplayName = 'Account Team';



-- Project
update `searchfield` set FieldName = 'projectstatus12.Title' ,
RealTableName = 'projectstatus projectstatus12' , RelationshipQuery = 'projectstatus12.StatusID = project.StatusID' where
SearchTableID = 12 and DisplayName = 'Status';

update `searchfield` set FieldName = 'CONCAT(manager12.FirstName, " ", manager12.LastName)' ,
RealTableName = 'individual manager12' , RelationshipQuery = 'manager12.IndividualID = project.Manager' where
SearchTableID = 12 and DisplayName = 'Manager';

update `searchfield` set FieldName = 'CONCAT(owner12.FirstName, " ", owner12.LastName)' ,
RealTableName = 'individual owner12' , RelationshipQuery = 'owner12.IndividualID = project.Owner' where
SearchTableID = 12 and DisplayName = 'Owner';

update `searchfield` set FieldName = 'CONCAT(createdby12.FirstName, " ", createdby12.LastName)' ,
RealTableName = 'individual createdby12' , RelationshipQuery = 'createdby12.IndividualID = project.Creator' where
SearchTableID = 12 and DisplayName = 'Created By';

update `searchfield` set FieldName = 'CONCAT(modifiedby12.FirstName, " ", modifiedby12.LastName)' ,
RealTableName = 'individual modifiedby12' , RelationshipQuery = 'modifiedby12.IndividualID = project.ModifiedBy' where
SearchTableID = 12 and DisplayName = 'Modified By';



-- Tasks
update `searchfield` set FieldName = 'std13.Start' ,
RealTableName = 'activity std13' , RelationshipQuery = 'std13.ActivityID = task.ActivityID' where
SearchTableID = 13 and DisplayName = 'Start Date';

update `searchfield` set FieldName = 'dud13.DueDate' ,
RealTableName = 'activity dud13' , RelationshipQuery = 'dud13.ActivityID = task.ActivityID' where
SearchTableID = 13 and DisplayName = 'Due Date';

update `searchfield` set FieldName = 'activitystatus13.Name' ,
RealTableName = 'activitystatus activitystatus13' , RelationshipQuery = 'activity.ActivityID = task.ActivityID AND activitystatus13.StatusID = activity.Status' where
SearchTableID = 13 and DisplayName = 'Status';

update `searchfield` set FieldName = 'CONCAT(assto13.FirstName, " ", assto13.LastName)' ,
RealTableName = 'individual assto13' , RelationshipQuery = 'assto13.IndividualID = taskassigned.AssignedTo AND taskassigned.TaskID = task.ActivityID' where
SearchTableID = 13 and DisplayName = 'Assigned To';



-- Tickets
update `searchfield` set FieldName = 'CONCAT(assto14.FirstName, " ", assto14.LastName)' ,
RealTableName = 'individual assto14' , RelationshipQuery = 'assto14.IndividualID = ticket.assignedto' where
SearchTableID = 14 and DisplayName = 'Assigned To';

update `searchfield` set FieldName = 'CONCAT(mgr14.FirstName, " ", mgr14.LastName)' ,
RealTableName = 'individual mgr14' , RelationshipQuery = 'mgr14.IndividualID = ticket.manager' where
SearchTableID = 14 and DisplayName = 'Manager';

update `searchfield` set FieldName = 'CONCAT(createdby14.FirstName, " ", createdby14.LastName)' ,
RealTableName = 'individual createdby14' , RelationshipQuery = 'createdby14.IndividualID = ticket.createdby' where
SearchTableID = 14 and DisplayName = 'Created By';

update `searchfield` set FieldName = 'CONCAT(modifiedby14.FirstName, " ", modifiedby14.LastName)' ,
RealTableName = 'individual modifiedby14' , RelationshipQuery = 'modifiedby14.IndividualID = ticket.modifiedby' where
SearchTableID = 14 and DisplayName = 'Modified By';

update `searchfield` set FieldName = 'CONCAT(ownedby14.FirstName, " ", ownedby14.LastName)' ,
RealTableName = 'individual ownedby14' , RelationshipQuery = 'ownedby14.IndividualID = ticket.owner' where
SearchTableID = 14 and DisplayName = 'Owned By';

update `searchfield` set FieldName = 'supportstatus14.name' ,
RealTableName = 'supportstatus supportstatus14' , RelationshipQuery = 'supportstatus14.statusid = ticket.status' where
SearchTableID = 14 and DisplayName = 'Status';

update `searchfield` set FieldName = 'supportpriority14.name' ,
RealTableName = 'supportpriority supportpriority14' , RelationshipQuery = 'supportpriority14.priorityid = ticket.priority' where
SearchTableID = 14 and DisplayName = 'Priority';



-- Timeslips
update `searchfield` set FieldName = 'CONCAT(createdby15.FirstName, " ", createdby15.LastName)' ,
RealTableName = 'individual createdby15' , RelationshipQuery = 'createdby15.IndividualID = timeslip.CreatedBy' where
SearchTableID = 15 and DisplayName = 'Created By';



-- FAQs
update `searchfield` set FieldName = 'CONCAT(createdby16.FirstName, " ", createdby16.LastName)' ,
RealTableName = 'individual createdby16' , RelationshipQuery = 'createdby16.IndividualID = faq.createdby' where
SearchTableID = 16 and DisplayName = 'Created By';

update `searchfield` set FieldName = 'CONCAT(modifiedby16.FirstName, " ", modifiedby16.LastName)' ,
RealTableName = 'individual modifiedby16' , RelationshipQuery = 'modifiedby16.IndividualID = faq.updatedby' where
SearchTableID = 16 and DisplayName = 'Modified By';

update `searchfield` set FieldName = 'CONCAT(ownedby16.FirstName, " ", ownedby16.LastName)' ,
RealTableName = 'individual ownedby16' , RelationshipQuery = 'ownedby16.IndividualID = faq.owner' where
SearchTableID = 16 and DisplayName = 'Owned By';




-- Knowledgebase
update `searchfield` set FieldName = 'category17.title' ,
RealTableName = 'category category17' , RelationshipQuery = 'category17.catid = knowledgebase.category' where
SearchTableID = 17 and DisplayName = 'Category';

update `searchfield` set FieldName = 'CONCAT(createdby17.FirstName, " ", createdby17.LastName)' ,
RealTableName = 'individual createdby17' , RelationshipQuery = 'createdby17.IndividualID = knowledgebase.createdby' where
SearchTableID = 17 and DisplayName = 'Created By';

update `searchfield` set FieldName = 'CONCAT(modifiedby17.FirstName, " ", modifiedby17.LastName)' ,
RealTableName = 'individual createdby17' , RelationshipQuery = 'modifiedby17.IndividualID = knowledgebase.updatedby' where
SearchTableID = 17 and DisplayName = 'Modified By';

update `searchfield` set FieldName = 'CONCAT(ownedby17.FirstName, " ", ownedby17.LastName)' ,
RealTableName = 'individual ownedby17' , RelationshipQuery = 'ownedby17.IndividualID = knowledgebase.owner' where
SearchTableID = 17 and DisplayName = 'Owned By';




-- Order History
update `searchfield` set FieldName = 'accountingstatus18.title' ,
RealTableName = 'accountingstatus accountingstatus18' , RelationshipQuery = 'accountingstatus18.statusid = cvorder.status' where
SearchTableID = 18 and DisplayName = 'Status';

update `searchfield` set FieldName = 'accountingterms18.title' ,
RealTableName = 'accountingterms accountingterms18' , RelationshipQuery = 'accountingterms18.termsid = cvorder.terms' where
SearchTableID = 18 and DisplayName = 'Terms';

update `searchfield` set FieldName = 'CONCAT(owner18.FirstName, " ", owner18.LastName)' ,
RealTableName = 'individual owner18' , RelationshipQuery = 'owner18.IndividualID = cvorder.owner' where
SearchTableID = 18 and DisplayName = 'Owner';

update `searchfield` set FieldName = 'CONCAT(cretedby18.FirstName, " ", cretedby18.LastName)' ,
RealTableName = 'individual cretedby18' , RelationshipQuery = 'cretedby18.IndividualID = cvorder.creator' where
SearchTableID = 18 and DisplayName = 'Created By';

update `searchfield` set FieldName = 'CONCAT(modifiedby18.FirstName, " ", modifiedby18.LastName)' ,
RealTableName = 'individual modifiedby18' , RelationshipQuery = 'modifiedby18.IndividualID = cvorder.modifiedby' where
SearchTableID = 18 and DisplayName = 'Modified By';

update `searchfield` set FieldName = 'CONCAT(acctmgr18.FirstName, " ", acctmgr18.LastName)' ,
RealTableName = 'individual acctmgr18' , RelationshipQuery = 'acctmgr18.IndividualID = cvorder.accountmgr' where
SearchTableID = 18 and DisplayName = 'Account Manager';



-- invoiceModuleId
-- Invoice History
update `searchfield` set FieldName = 'accountingstatus19.title' ,
RealTableName = 'accountingstatus accountingstatus19' , RelationshipQuery = 'accountingstatus19.statusid = invoice.Status' where
SearchTableID = 19 and DisplayName = 'Status';

update `searchfield` set FieldName = 'accountingterms19.title' ,
RealTableName = 'accountingterms accountingterms19' , RelationshipQuery = 'accountingterms19.termsid = invoice.Terms' where
SearchTableID = 19 and DisplayName = 'Terms';

update `searchfield` set FieldName = 'CONCAT(owner19.FirstName, " ", owner19.LastName)' ,
RealTableName = 'individual owner19' , RelationshipQuery = 'owner19.IndividualID = invoice.Owner' where
SearchTableID = 19 and DisplayName = 'Owner';

update `searchfield` set FieldName = 'CONCAT(createdby19.FirstName, " ", createdby19.LastName)' ,
RealTableName = 'individual createdby19' , RelationshipQuery = 'createdby19.IndividualID = invoice.Creator' where
SearchTableID = 19 and DisplayName = 'Created By';

update `searchfield` set FieldName = 'CONCAT(acctmgr19.FirstName, " ", acctmgr19.LastName)' ,
RealTableName = 'individual acctmgr19' , RelationshipQuery = 'acctmgr19.IndividualID = invoice.accountmgr' where
SearchTableID = 19 and DisplayName = 'Account Manager';

-- Ticket Template
INSERT INTO ptcategory VALUES(4,'Ticket',2,4,'PRINT');
INSERT INTO ptdetail VALUES(3,4,'YES','<HTML>
<HEAD>
<TITLE> Ticket Template </TITLE>
</HEAD>
<STYLE>
.popupTableText
{
    FONT-SIZE: 65%;
    COLOR: #334a60;
    height: 3%;
    font-family: verdana, arial, sans-serif
}

.popupTableHead
{
    PADDING-RIGHT: 2px;
    PADDING-LEFT: 2px;
    FONT-WEIGHT: bold;
    FONT-SIZE: 65%;
    height: 3%;
    COLOR: #334a60;
    font-family: verdana, arial, sans-serif;
    BACKGROUND-COLOR: #9ebed1
}

.popupTableHeadText
{
    PADDING-RIGHT: 2px;
    PADDING-LEFT: 2px;
    FONT-WEIGHT: bold;
    FONT-SIZE: 65%;
    height: 3%;
    COLOR: #334a60;
    font-family: verdana, arial, sans-serif
}

.searchBuilder
{
    COLOR: #FFFFFF;
    font-family: verdana, arial, sans-serif;
    FONT-WEIGHT: bold;
    FONT-SIZE: 65%;
    PADDING-LEFT: 3px;
    BACKGROUND-COLOR: #7c91b2;
}

.rowOdd
{
    FONT-WEIGHT: bold;
    FONT-SIZE: 80%;
    COLOR: #334a60;
    font-family: verdana, arial, sans-serif;
}

.rowEven
{
    FONT-WEIGHT: bold;
    FONT-SIZE: 80%;
    COLOR: #334a60;
    font-family: verdana, arial, sans-serif;
}

.contactTableHeadShadow
{
    BACKGROUND-COLOR: #7d93b2
}
.contactTableHeadShadowLt
{
    BACKGROUND-COLOR: #b1d0dc
}
.contactTableHeadBottom
{
    BACKGROUND-COLOR: #7d93b2
}
</STYLE>
<BODY  marginheight="0" marginwidth="0" topmargin="0" bottommargin="0" leftmargin="0" rightmargin="0" bgcolor="#d8dfea">
<TABLE width="100%" cellspacing="0" cellpadding="0">
<TR>
                                    <TD colspan="2" >&nbsp;</TD>
</TR>
<TR>
                                    <TD class="popupTableText" >
                                    <FONT SIZE="5" COLOR="#000066">--Name--</FONT><br>
                                    --Contact--<br>
                                    --Address--<br>
                                    --Email--<br>
                                    --Phone--<br><br>
                                    </TD>
                                    <TD valign="top" align="Right" ><font Color="#000066"><H1>Ticket</H1></font></TD>
</TR>
<TR>
                                    <TD colspan="2" >&nbsp;</TD>
</TR>
<TR>
            <TD valign="top" >
                        <TABLE width="100%"  cellspacing="0" cellpadding="0"  >
                        <TR>
                                    <TD class="popupTableHead"  colspan="2" >Ticket Details</TD>
                        </TR>
                        <TR>
                                    <TD class="popupTableText">Ticket Number:</TD>
                                    <TD class="popupTableText">--TicketNumber--</TD>
                        </TR>
                        <TR>
                                    <TD class="popupTableText">Status:</TD>
                                    <TD class="popupTableText">--Status--</TD>
                        </TR>
                        <TR>
                                    <TD class="popupTableText">Priority:</TD>
                                    <TD class="popupTableText">--Priority--</TD>
                        </TR>
                        <TR>
                                    <TD class="popupTableText">Subject:</TD>
                                    <TD class="popupTableText">--Subject--</TD>
                        </TR>
                        <TR>
                                    <TD class="popupTableText">Details:</TD>
                                    <TD class="popupTableText">--Details--</TD>
                        </TR>
                        <TR>
                                    <TD class="popupTableHead" colspan="2">Responsibility</TD>
                        </TR>
                        <TR>
                                    <TD class="popupTableText">Manager:</TD>
                                    <TD class="popupTableText">--Manager--</TD>
                        </TR>
                        <TR>
                                    <TD class="popupTableText">Assigned To:</TD>
                                    <TD class="popupTableText">--AssignedTo--</TD>
                        </TR>
                        </TABLE>
            </TD>
            <TD valign="top">
                        <TABLE width="100%" cellspacing="0" cellpadding="0" >
                        <TR>
                                    <TD class="popupTableHead" colspan="2">Custom Fields</TD>
                        </TR>
                        --CustomFields--
                        </TABLE>
            </TD>
</TR>
<tr><td colspan="2">&nbsp;</td>
</tr>
<TR>
            <TD colspan="2">
            <table>
                        <tr height="20">
                                    <td width="11">&nbsp;</td>
                                    <td class="searchBuilder">
                                      <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                                <tr>
                                                  <td class="searchBuilder">Threads</td>
                                                  <td class="searchBuilder" style="padding:2px;padding-right:10px;" align="right">
                                                  </td>
                                                </tr>
                                      </table>
                                    </td>
                                    <td width="11"></td>
                        </tr>
                        <tr>
                                    <td width="11"></td>
                                    <td width="100%">
                                                <table border="0" cellspacing="0" cellpadding="0" width="100%">
                                                            <tr height="1">
                                                                        <td class="contactTableHeadShadow" colspan="5"></td>
                                                            </tr>
                                                            <tr height="1">
                                                                        <td class="contactTableHeadShadowLt" colspan="5"></td>
                                                            </tr>
                                                            <tr>
                                                                        <td class="popupTableHead">Title</td>
                                                                        <td class="popupTableHead">Data</td>
                                                                        <td class="popupTableHead">Date</td>
                                                                        <td class="popupTableHead">Priority</td>
                                                                        <td class="popupTableHead">Created By</td>
                                                            </tr>
                                                            <tr height="1">
                                                                        <td class="contactTableHeadBottom" colspan="5"></td>
                                                            </tr>
                                                            --Values--
                                    </tr>
                                    <tr height="1">
                                      <td class="contactTableHeadBottom" colspan="5"></td>
                                    </tr>
                          </table>
                        </td>
                        <td width="11"></td>
              </tr>
            </table>
            </TD>
</TR>
</TABLE>
</BODY>
</HTML>
','TicketTemplate',2,2,'TICKET','Ticket');

-- The emailrecipient table would only hold addresses 50 characters or less!!
ALTER TABLE emailrecipient MODIFY `Address` varchar(255);

UPDATE searchfield SET fieldName = 'state', isOnTable = 'Y', realTableName = null, relationshipQuery = null WHERE searchfieldID = 28;

UPDATE searchfield SET fieldName = 'country', isOnTable = 'Y', realTableName = null, relationshipQuery = null WHERE searchfieldID = 31;

-- Make all user's email composition preference be HTML by default
UPDATE `userpreferencedefault` SET `preference_value`='HTML' WHERE `preference_name`='contenttype';

INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES
(22,'Detail',4);

INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES
(106,'Detail',5);