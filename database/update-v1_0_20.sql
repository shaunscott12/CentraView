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

INSERT INTO `history` SELECT date,'',recordtypeid,recordid,operation,individualid,referenceactivityid,historyid FROM contacthistory;

UPDATE history, project SET history.recordName = project.ProjectTitle where history.recordtypeid=36 and project.ProjectID = history.recordid;

UPDATE history, entity SET history.recordName = entity.Name where history.recordtypeid=14 and entity.EntityID = history.recordid;

UPDATE history, individual SET history.recordName = concat(individual.FirstName ,' ', individual.MiddleInitial,' ',individual.LastName) where history.recordtypeid=15 and individual.IndividualID = history.recordid;

UPDATE history, activity SET history.recordName = activity.Title where history.recordtypeid=15 and activity.ActivityID = history.referenceactivityid and history.referenceactivityid != 0;
UPDATE history, activity SET history.recordName = activity.Title where history.recordtypeid=14 and activity.ActivityID = history.referenceactivityid and history.referenceactivityid != 0;


-- Dropping the table contacthistory

DROP TABLE IF EXISTS `contacthistory`;

-- Insert the listing columns.
INSERT INTO listcolumns (listtype, columnname, columnorder) VALUES ('History', 'RecordName', 5);

-- Insert the viewable columns.
INSERT INTO viewcolumns (viewid, columnname, columnorder) VALUES (182, 'RecordName', 5);

ALTER TABLE `emailmessage` ADD `private` enum('YES','NO') default 'NO';

-- Kevin's update to add a list view for Template administration
SET @templateModuleID = 59;
SET @newListType = 'Template';
INSERT INTO listtypes (typename, moduleid) VALUES (@newListType, @templateModuleID);
INSERT INTO listviews (listtype, viewname, ownerid, creatorid, sortmember, sorttype, searchid, noofrecords, searchtype) VALUES (@newListType, 'Default View', NULL, NULL, 'Name', 'A', 0, 100, 'A');
SET @templateViewID = LAST_INSERT_ID();
INSERT INTO defaultviews (viewid, listtype) VALUES (@templateViewID, @newListType);
INSERT INTO listcolumns (listtype, columnname, columnorder) VALUES (@newListType, 'TemplateID', 1);
INSERT INTO listcolumns (listtype, columnname, columnorder) VALUES (@newListType, 'Name', 2);
INSERT INTO listcolumns (listtype, columnname, columnorder) VALUES (@newListType, 'Category', 3);
INSERT INTO viewcolumns (viewid, columnname, columnorder) VALUES (@templateViewID, 'TemplateID', 1);
INSERT INTO viewcolumns (viewid, columnname, columnorder) VALUES (@templateViewID, 'Name', 2); 
INSERT INTO viewcolumns (viewid, columnname, columnorder) VALUES (@templateViewID, 'Category', 3);

-- Activity lists and a LOT of clean up
DELETE FROM listcolumns WHERE listtype = 'MultiActivity' and columnname = 'Due';
INSERT INTO listcolumns VALUES('MultiActivity', 'Details', '4');
INSERT INTO listcolumns VALUES('MultiActivity', 'Entity', '10');
INSERT INTO listcolumns VALUES('MultiActivity', 'Individual', '11');
INSERT INTO listcolumns VALUES('MultiActivity', 'Owner', '12');
INSERT INTO listcolumns VALUES('MultiActivity', 'Notes', '13');
DELETE FROM viewcolumns WHERE viewid = 20 and columnname = 'Due';
INSERT INTO viewcolumns VALUES('20', 'Details', '4');
INSERT INTO viewcolumns VALUES('20', 'Entity', '10');
INSERT INTO viewcolumns VALUES('20', 'Individual', '11');
INSERT INTO viewcolumns VALUES('20', 'Owner', '12');
INSERT INTO viewcolumns VALUES('20', 'Notes', '13');
UPDATE listviews SET sorttype='A' WHERE listtype = 'MultiActivity';
DELETE FROM listcolumns WHERE listtype = "Appointment" and columnorder = '8';
DELETE FROM viewcolumns WHERE columnname = 'Location';
DELETE FROM listcolumns WHERE listtype = "Call" and columnname = "Due";
DELETE FROM viewcolumns WHERE viewid = 10 and columnname = 'Due';
UPDATE listviews SET sortmember = 'Title' WHERE viewid = '14';
INSERT INTO listcolumns VALUES('ForecastSales', 'Title', '1');
INSERT INTO listcolumns VALUES('ForecastSales', 'Description', '2');
INSERT INTO listcolumns VALUES('ForecastSales', 'Entity', '3');
INSERT INTO listcolumns VALUES('ForecastSales', 'Type', '4');
INSERT INTO listcolumns VALUES('ForecastSales', 'Stage', '5');
INSERT INTO listcolumns VALUES('ForecastSales', 'Probability', '6');
INSERT INTO listcolumns VALUES('ForecastSales', 'Status', '7');
INSERT INTO listcolumns VALUES('ForecastSales', 'EstimatedCloseDate', '8');
INSERT INTO listcolumns VALUES('ForecastSales', 'ForecastAmount', '9');
INSERT INTO listcolumns VALUES('ForecastSales', 'ActualAmount', '10');
INSERT INTO listcolumns VALUES('ForecastSales', 'SalesPersonName', '11');
UPDATE listviews SET listtype = 'ForecastSales' WHERE viewid = '14';
INSERT INTO viewcolumns VALUES('14', 'Description', '2');
INSERT INTO viewcolumns VALUES('14', 'Entity', '3');
INSERT INTO viewcolumns VALUES('14', 'Type', '4');
INSERT INTO viewcolumns VALUES('14', 'Stage', '5');
INSERT INTO viewcolumns VALUES('14', 'Probability', '6');
INSERT INTO viewcolumns VALUES('14', 'Status', '7');
INSERT INTO viewcolumns VALUES('14', 'EstimatedCloseDate', '8');
INSERT INTO viewcolumns VALUES('14', 'ForecastAmount', '9');
INSERT INTO viewcolumns VALUES('14', 'ActualAmount', '10');
INSERT INTO viewcolumns VALUES('14', 'SalesPersonName', '11');
UPDATE defaultviews SET listtype = 'ForecastSales' WHERE viewid = '14';
UPDATE listtypes SET typename = 'ForecastSales' WHERE moduleid = '19';
UPDATE listviews SET sortmember = 'LiteratureRequested' WHERE listtype = 'LiteratureFulfillment'; 
DELETE FROM viewcolumns WHERE viewid = 72;
INSERT INTO viewcolumns VALUES('72', 'LiteratureRequested', '1');
INSERT INTO viewcolumns VALUES('72', 'WhoRequested', '2');
INSERT INTO viewcolumns VALUES('72', 'DateRequested', '3');
INSERT INTO viewcolumns VALUES('72', 'DeliveryMethod', '4');
INSERT INTO viewcolumns VALUES('72', 'Entity', '5');
DELETE FROM listcolumns WHERE listtype = 'LiteratureFulfillment';
INSERT INTO listcolumns VALUES('LiteratureFulfillment', 'LiteratureRequested', '1');
INSERT INTO listcolumns VALUES('LiteratureFulfillment', 'WhoRequested', '2');
INSERT INTO listcolumns VALUES('LiteratureFulfillment', 'DateRequested', '3');
INSERT INTO listcolumns VALUES('LiteratureFulfillment', 'DeliveryMethod', '4');
INSERT INTO listcolumns VALUES('LiteratureFulfillment', 'Entity', '5');
DELETE FROM viewcolumns WHERE viewid = 21;
INSERT INTO viewcolumns VALUES('21', 'Title', '1');
INSERT INTO viewcolumns VALUES('21', 'Created', '2');
INSERT INTO viewcolumns VALUES('21', 'Priority', '3');
INSERT INTO viewcolumns VALUES('21', 'CreatedBy', '4');
INSERT INTO viewcolumns VALUES('21', 'Status', '5');
INSERT INTO viewcolumns VALUES('21', 'Start', '6');
INSERT INTO viewcolumns VALUES('21', 'End', '7');
DELETE FROM listcolumns WHERE listtype = 'NextAction';
INSERT INTO listcolumns VALUES('NextAction', 'Title', '1');
INSERT INTO listcolumns VALUES('NextAction', 'Created', '2');
INSERT INTO listcolumns VALUES('NextAction', 'Priority', '3');
INSERT INTO listcolumns VALUES('NextAction', 'CreatedBy', '4');
INSERT INTO listcolumns VALUES('NextAction', 'Status', '5');
INSERT INTO listcolumns VALUES('NextAction', 'Start', '6');
INSERT INTO listcolumns VALUES('NextAction', 'End', '7');
DELETE FROM viewcolumns WHERE viewid = 25;
INSERT INTO viewcolumns VALUES('25', 'Title', '1');
INSERT INTO viewcolumns VALUES('25', 'Created', '2');
INSERT INTO viewcolumns VALUES('25', 'Priority', '3');
INSERT INTO viewcolumns VALUES('25', 'CreatedBy', '4');
INSERT INTO viewcolumns VALUES('25', 'Status', '5');
INSERT INTO viewcolumns VALUES('25', 'Start', '6');
INSERT INTO viewcolumns VALUES('25', 'End', '7');
DELETE FROM listcolumns WHERE listtype = 'ToDo';
INSERT INTO listcolumns VALUES('ToDo', 'Title', '1');
INSERT INTO listcolumns VALUES('ToDo', 'Created', '2');
INSERT INTO listcolumns VALUES('ToDo', 'Priority', '3');
INSERT INTO listcolumns VALUES('ToDo', 'CreatedBy', '4');
INSERT INTO listcolumns VALUES('ToDo', 'Status', '5');
INSERT INTO listcolumns VALUES('ToDo', 'Start', '6');
INSERT INTO listcolumns VALUES('ToDo', 'End', '7');
DELETE FROM viewcolumns WHERE columnname = 'Due';

-- update the Primary column of the old data to set only one entity information to yes
CREATE TEMPORARY TABLE `tempAddress` SELECT  Contact, Address, IsPrimary FROM addressrelate WHERE ContactType = 1 AND IsPrimary='YES' Group by 1 ;

UPDATE `addressrelate` SET IsPrimary='NO' WHERE ContactType = 1;

UPDATE `addressrelate`, `tempAddress`, `entity` SET addressrelate.IsPrimary = 'YES' WHERE 
 addressrelate.Address = tempAddress.Address and tempAddress.Contact = entity.EntityID;

DROP TABLE `tempAddress`;

-- update the Primary column of the old data to set only one individual information to yes
CREATE TEMPORARY TABLE `tempAddress` SELECT  Contact, Address, IsPrimary FROM addressrelate WHERE ContactType = 2 AND IsPrimary='YES' Group by 1 ;

UPDATE `addressrelate` SET IsPrimary='NO' WHERE ContactType = 2;

UPDATE `addressrelate`, `tempAddress`, `individual` SET addressrelate.IsPrimary = 'YES' WHERE 
 addressrelate.Address = tempAddress.Address and tempAddress.Contact = individual.IndividualID;

DROP TABLE `tempAddress`; 

UPDATE `listviews` SET sortmember='date' WHERE viewid=22;

-- KDM: Change Category name from File to Print
UPDATE ptcategory SET catname = "Print" WHERE ptcategoryid = 1;

-- Removing the Duplicate Contacts from the Application. Only for the Individual
CREATE TEMPORARY TABLE `tempGoodMOC` SELECT mc.Content, mr.MocID,mc.SyncAs,i.IndividualID FROM methodofcontact mc,mocrelate mr,individual i 
WHERE mc.MocID = mr.MocID AND mr.ContactID = i.IndividualID AND mr.contactType=2 group by 1,4;

CREATE TEMPORARY TABLE `tempAllMOC` SELECT MocID FROM mocrelate where ContactType=2;

DELETE FROM tempAllMOC USING tempAllMOC,tempGoodMOC WHERE tempAllMOC.MocID = tempGoodMOC.MocID;

DELETE FROM mocrelate USING mocrelate,tempAllMOC WHERE mocrelate.MocID = tempAllMOC.MocID and ContactType = 2;

DELETE FROM methodofcontact USING methodofcontact,tempAllMOC WHERE methodofcontact.MocID = tempAllMOC.MocID;

-- Literature request fixes
UPDATE searchfield SET RelationshipQuery = 'entity22.entityid = activitylink.recordid AND activitylink.recordtypeid = 1 AND activitylink.activityid = literaturerequest.activityid' WHERE SearchTableID = 22 AND DisplayName = 'Entity';
UPDATE searchfield SET RelationshipQuery = 'deliverymethod22.deliveryid = literaturerequest.deliverymethod' WHERE SearchTableID = 22 AND DisplayName = 'Delivery Method';

-- Activity order
DELETE FROM viewcolumns WHERE viewid = 20;
INSERT INTO viewcolumns VALUES(20, 'Title', 1);
INSERT INTO viewcolumns VALUES(20, 'Details', 2);
INSERT INTO viewcolumns VALUES(20, 'Entity', 3);
INSERT INTO viewcolumns VALUES(20, 'Individual', 4);
INSERT INTO viewcolumns VALUES(20, 'Start', 5);
INSERT INTO viewcolumns VALUES(20, 'End', 6);
INSERT INTO viewcolumns VALUES(20, 'Type', 7);
INSERT INTO viewcolumns VALUES(20, 'Status', 8);
INSERT INTO viewcolumns VALUES(20, 'Priority', 9);
INSERT INTO viewcolumns VALUES(20, 'Owner', 10);
INSERT INTO viewcolumns VALUES(20, 'Created', 11);
INSERT INTO viewcolumns VALUES(20, 'CreatedBy', 12);
INSERT INTO viewcolumns VALUES(20, 'Notes', 13);
DELETE FROM listcolumns WHERE listtype = 'MultiActivity';
INSERT INTO listcolumns VALUES('MultiActivity', 'Title', 1);
INSERT INTO listcolumns VALUES('MultiActivity', 'Details', 2);
INSERT INTO listcolumns VALUES('MultiActivity', 'Entity', 3);
INSERT INTO listcolumns VALUES('MultiActivity', 'Individual', 4);
INSERT INTO listcolumns VALUES('MultiActivity', 'Start', 5);
INSERT INTO listcolumns VALUES('MultiActivity', 'End', 6);
INSERT INTO listcolumns VALUES('MultiActivity', 'Type', 7);
INSERT INTO listcolumns VALUES('MultiActivity', 'Status', 8);
INSERT INTO listcolumns VALUES('MultiActivity', 'Priority', 9);
INSERT INTO listcolumns VALUES('MultiActivity', 'Owner', 10);
INSERT INTO listcolumns VALUES('MultiActivity', 'Created', 11);
INSERT INTO listcolumns VALUES('MultiActivity', 'CreatedBy', 12);
INSERT INTO listcolumns VALUES('MultiActivity', 'Notes', 13);
DELETE FROM viewcolumns WHERE viewid = 8;
INSERT INTO viewcolumns VALUES(8, 'Title', 1);
INSERT INTO viewcolumns VALUES(8, 'Details', 2);
INSERT INTO viewcolumns VALUES(8, 'Entity', 3);
INSERT INTO viewcolumns VALUES(8, 'Individual', 4);
INSERT INTO viewcolumns VALUES(8, 'Start', 5);
INSERT INTO viewcolumns VALUES(8, 'End', 6);
INSERT INTO viewcolumns VALUES(8, 'Status', 7);
INSERT INTO viewcolumns VALUES(8, 'Priority', 8);
INSERT INTO viewcolumns VALUES(8, 'Owner', 9);
INSERT INTO viewcolumns VALUES(8, 'Created', 10);
INSERT INTO viewcolumns VALUES(8, 'CreatedBy', 11);
INSERT INTO viewcolumns VALUES(8, 'Notes', 12);
DELETE FROM listcolumns WHERE listtype = 'Appointment';
INSERT INTO listcolumns VALUES('Appointment', 'Title', 1);
INSERT INTO listcolumns VALUES('Appointment', 'Details', 2);
INSERT INTO listcolumns VALUES('Appointment', 'Entity', 3);
INSERT INTO listcolumns VALUES('Appointment', 'Individual', 4);
INSERT INTO listcolumns VALUES('Appointment', 'Start', 5);
INSERT INTO listcolumns VALUES('Appointment', 'End', 6);
INSERT INTO listcolumns VALUES('Appointment', 'Status', 7);
INSERT INTO listcolumns VALUES('Appointment', 'Priority', 8);
INSERT INTO listcolumns VALUES('Appointment', 'Owner', 9);
INSERT INTO listcolumns VALUES('Appointment', 'Created', 10);
INSERT INTO listcolumns VALUES('Appointment', 'CreatedBy', 11);
INSERT INTO listcolumns VALUES('Appointment', 'Notes', 12);
DELETE FROM viewcolumns WHERE viewid = 10;
INSERT INTO viewcolumns VALUES(10, 'Title', 1);
INSERT INTO viewcolumns VALUES(10, 'Details', 2);
INSERT INTO viewcolumns VALUES(10, 'Entity', 3);
INSERT INTO viewcolumns VALUES(10, 'Individual', 4);
INSERT INTO viewcolumns VALUES(10, 'Start', 5);
INSERT INTO viewcolumns VALUES(10, 'End', 6);
INSERT INTO viewcolumns VALUES(10, 'CallType', 7);
INSERT INTO viewcolumns VALUES(10, 'Status', 8);
INSERT INTO viewcolumns VALUES(10, 'Priority', 9);
INSERT INTO viewcolumns VALUES(10, 'Owner', 10);
INSERT INTO viewcolumns VALUES(10, 'Created', 11);
INSERT INTO viewcolumns VALUES(10, 'CreatedBy', 12);
INSERT INTO viewcolumns VALUES(10, 'Notes', 13);
DELETE FROM listcolumns WHERE listtype = 'Call';
INSERT INTO listcolumns VALUES('Call', 'Title', 1);
INSERT INTO listcolumns VALUES('Call', 'Details', 2);
INSERT INTO listcolumns VALUES('Call', 'Entity', 3);
INSERT INTO listcolumns VALUES('Call', 'Individual', 4);
INSERT INTO listcolumns VALUES('Call', 'Start', 5);
INSERT INTO listcolumns VALUES('Call', 'End', 6);
INSERT INTO listcolumns VALUES('Call', 'CallType', 7);
INSERT INTO listcolumns VALUES('Call', 'Status', 8);
INSERT INTO listcolumns VALUES('Call', 'Priority', 9);
INSERT INTO listcolumns VALUES('Call', 'Owner', 10);
INSERT INTO listcolumns VALUES('Call', 'Created', 11);
INSERT INTO listcolumns VALUES('Call', 'CreatedBy', 12);
INSERT INTO listcolumns VALUES('Call', 'Notes', 13);
DELETE FROM viewcolumns WHERE viewid = 18;
INSERT INTO viewcolumns VALUES(18, 'Title', 1);
INSERT INTO viewcolumns VALUES(18, 'Details', 2);
INSERT INTO viewcolumns VALUES(18, 'Entity', 3);
INSERT INTO viewcolumns VALUES(18, 'Individual', 4);
INSERT INTO viewcolumns VALUES(18, 'Start', 5);
INSERT INTO viewcolumns VALUES(18, 'End', 6);
INSERT INTO viewcolumns VALUES(18, 'Status', 7);
INSERT INTO viewcolumns VALUES(18, 'Priority', 8);
INSERT INTO viewcolumns VALUES(18, 'Owner', 9);
INSERT INTO viewcolumns VALUES(18, 'Created', 10);
INSERT INTO viewcolumns VALUES(18, 'CreatedBy', 11);
INSERT INTO viewcolumns VALUES(18, 'Notes', 12);
DELETE FROM listcolumns WHERE listtype = 'Meeting';
INSERT INTO listcolumns VALUES('Meeting', 'Title', 1);
INSERT INTO listcolumns VALUES('Meeting', 'Details', 2);
INSERT INTO listcolumns VALUES('Meeting', 'Entity', 3);
INSERT INTO listcolumns VALUES('Meeting', 'Individual', 4);
INSERT INTO listcolumns VALUES('Meeting', 'Start', 5);
INSERT INTO listcolumns VALUES('Meeting', 'End', 6);
INSERT INTO listcolumns VALUES('Meeting', 'Status', 7);
INSERT INTO listcolumns VALUES('Meeting', 'Priority', 8);
INSERT INTO listcolumns VALUES('Meeting', 'Owner', 9);
INSERT INTO listcolumns VALUES('Meeting', 'Created', 10);
INSERT INTO listcolumns VALUES('Meeting', 'CreatedBy', 11);
INSERT INTO listcolumns VALUES('Meeting', 'Notes', 12);
DELETE FROM viewcolumns WHERE viewid = 21;
INSERT INTO viewcolumns VALUES(21, 'Title', 1);
INSERT INTO viewcolumns VALUES(21, 'Details', 2);
INSERT INTO viewcolumns VALUES(21, 'Entity', 3);
INSERT INTO viewcolumns VALUES(21, 'Individual', 4);
INSERT INTO viewcolumns VALUES(21, 'Start', 5);
INSERT INTO viewcolumns VALUES(21, 'End', 6);
INSERT INTO viewcolumns VALUES(21, 'Status', 7);
INSERT INTO viewcolumns VALUES(21, 'Priority', 8);
INSERT INTO viewcolumns VALUES(21, 'Owner', 9);
INSERT INTO viewcolumns VALUES(21, 'Created', 10);
INSERT INTO viewcolumns VALUES(21, 'CreatedBy', 11);
INSERT INTO viewcolumns VALUES(21, 'Notes', 12);
DELETE FROM listcolumns WHERE listtype = 'NextAction';
INSERT INTO listcolumns VALUES('NextAction', 'Title', 1);
INSERT INTO listcolumns VALUES('NextAction', 'Details', 2);
INSERT INTO listcolumns VALUES('NextAction', 'Entity', 3);
INSERT INTO listcolumns VALUES('NextAction', 'Individual', 4);
INSERT INTO listcolumns VALUES('NextAction', 'Start', 5);
INSERT INTO listcolumns VALUES('NextAction', 'End', 6);
INSERT INTO listcolumns VALUES('NextAction', 'Status', 7);
INSERT INTO listcolumns VALUES('NextAction', 'Priority', 8);
INSERT INTO listcolumns VALUES('NextAction', 'Owner', 9);
INSERT INTO listcolumns VALUES('NextAction', 'Created', 10);
INSERT INTO listcolumns VALUES('NextAction', 'CreatedBy', 11);
INSERT INTO listcolumns VALUES('NextAction', 'Notes', 12);
DELETE FROM viewcolumns WHERE viewid = 25;
INSERT INTO viewcolumns VALUES(25, 'Title', 1);
INSERT INTO viewcolumns VALUES(25, 'Details', 2);
INSERT INTO viewcolumns VALUES(25, 'Entity', 3);
INSERT INTO viewcolumns VALUES(25, 'Individual', 4);
INSERT INTO viewcolumns VALUES(25, 'Start', 5);
INSERT INTO viewcolumns VALUES(25, 'End', 6);
INSERT INTO viewcolumns VALUES(25, 'Status', 7);
INSERT INTO viewcolumns VALUES(25, 'Priority', 8);
INSERT INTO viewcolumns VALUES(25, 'Owner', 9);
INSERT INTO viewcolumns VALUES(25, 'Created', 10);
INSERT INTO viewcolumns VALUES(25, 'CreatedBy', 11);
INSERT INTO viewcolumns VALUES(25, 'Notes', 12);
DELETE FROM listcolumns WHERE listtype = 'ToDo';
INSERT INTO listcolumns VALUES('ToDo', 'Title', 1);
INSERT INTO listcolumns VALUES('ToDo', 'Details', 2);
INSERT INTO listcolumns VALUES('ToDo', 'Entity', 3);
INSERT INTO listcolumns VALUES('ToDo', 'Individual', 4);
INSERT INTO listcolumns VALUES('ToDo', 'Start', 5);
INSERT INTO listcolumns VALUES('ToDo', 'End', 6);
INSERT INTO listcolumns VALUES('ToDo', 'Status', 7);
INSERT INTO listcolumns VALUES('ToDo', 'Priority', 8);
INSERT INTO listcolumns VALUES('ToDo', 'Owner', 9);
INSERT INTO listcolumns VALUES('ToDo', 'Created', 10);
INSERT INTO listcolumns VALUES('ToDo', 'CreatedBy', 11);
INSERT INTO listcolumns VALUES('ToDo', 'Notes', 12);

INSERT INTO `emailfolder` (Parent, AccountID, Name, Ftype) SELECT Parent, AccountID, 'Templates' ,'SYSTEM' FROM emailfolder WHERE Name='Inbox';
