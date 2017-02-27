UPDATE listcolumns set listtype='EntityMerge' where listtype='Merge';
UPDATE listviews set listtype='EntityMerge' where listtype='Merge';
UPDATE defaultviews set listtype='EntityMerge' where listtype='Merge';

-- Add List for Merge Search Results
INSERT INTO listviews (listtype, viewid, viewname, sortmember, sorttype, searchid, noofrecords, searchtype) VALUES ('IndividualMerge', NULL, 'Merge Default', 'Title', 'A', 0, 101, 'A');
select @ID:=LAST_INSERT_ID();
INSERT INTO viewcolumns (viewid, columnname, columnorder) VALUES (@ID, 'Title', 1);
INSERT INTO viewcolumns (viewid, columnname, columnorder) VALUES (@ID, 'entityName', 2);
INSERT INTO listcolumns (listtype, columnname, columnorder) VALUES ('IndividualMerge', 'Title', 1);
INSERT INTO listcolumns (listtype, columnname, columnorder) VALUES ('IndividualMerge', 'entityName', 2);
INSERT INTO defaultviews (listtype, viewid) VALUES ('IndividualMerge', @ID);

-- Moving the Individual from Wrong List to the correct List..
UPDATE individual i ,entity e SET i.List=e.List WHERE e.EntityID=i.Entity;

-- For new IMAP stuff
ALTER TABLE emailaccount ADD beenChecked enum('YES', 'NO') DEFAULT 'NO' NOT NULL;
ALTER TABLE emailfolder ADD FullName varchar(255);
ALTER TABLE emailaccount DROP beenChecked;
--
-- Table structure for table 'literaturerequestlink'
--

DROP TABLE IF EXISTS `literaturerequestlink`;
CREATE TABLE `literaturerequestlink` (
  `ActivityID` int(11) unsigned default NULL,
  `LiteratureID` int(11) unsigned default NULL,
  KEY `ActivityID` (`ActivityID`),
  KEY `LiteratureID` (`LiteratureID`)
) TYPE=InnoDB;


INSERT INTO `literaturerequestlink` SELECT ActivityID,LiteratureID FROM literaturerequest;
CREATE TEMPORARY TABLE `literaturerequesttemp` SELECT * FROM `literaturerequest` group by Activityid;
DELETE FROM `literaturerequest`;
INSERT INTO `literaturerequest` SELECT * FROM `literaturerequesttemp`;
ALTER TABLE `literaturerequest` DROP LiteratureID;
ALTER TABLE `literaturerequest` DROP Title;
ALTER TABLE `literaturerequest` DROP Description;
DROP TABLE `literaturerequesttemp`;


INSERT INTO `salesprobability` VALUES (0,13,'0%','');

-- Note valuelist
DELETE FROM viewcolumns WHERE viewid = 22;
INSERT INTO viewcolumns VALUES(22, 'Title', 1);
INSERT INTO viewcolumns VALUES(22, 'Date', 2);
INSERT INTO viewcolumns VALUES(22, 'CreatedBy', 3);
INSERT INTO viewcolumns VALUES(22, 'Detail', 4);
UPDATE listviews SET sortmember = 'Date' WHERE listtype = 'Note';

-- Marketing valuelist
UPDATE listviews SET sortmember = 'Title' WHERE viewid = 75;
DELETE FROM viewcolumns WHERE viewid = 75;
INSERT INTO viewcolumns VALUES(75, 'Title', 1);
INSERT INTO viewcolumns VALUES(75, 'Description', 2);
INSERT INTO viewcolumns VALUES(75, 'OrderValue', 3);
INSERT INTO viewcolumns VALUES(75, 'NumberOfRecords', 4);
INSERT INTO viewcolumns VALUES(75, 'OpportunityCount', 5);
INSERT INTO viewcolumns VALUES(75, 'OpportunityValue', 6);
INSERT INTO viewcolumns VALUES(75, 'OrderCount', 7);
DELETE FROM listcolumns WHERE listtype = 'Marketing';
INSERT INTO listcolumns VALUES('Marketing', 'Title', 1);
INSERT INTO listcolumns VALUES('Marketing', 'Description', 2);
INSERT INTO listcolumns VALUES('Marketing', 'OrderValue', 3);
INSERT INTO listcolumns VALUES('Marketing', 'NumberOfRecords', 4);
INSERT INTO listcolumns VALUES('Marketing', 'OpportunityCount', 5);
INSERT INTO listcolumns VALUES('Marketing', 'OpportunityValue', 6);
INSERT INTO listcolumns VALUES('Marketing', 'OrderCount', 7);
UPDATE listviews SET sortmember = 'Title' WHERE viewid = 76;
DELETE FROM listcolumns WHERE listtype = 'Promotion';
INSERT INTO listcolumns VALUES('Promotion', 'Title', 1);
INSERT INTO listcolumns VALUES('Promotion', 'Description', 2);
INSERT INTO listcolumns VALUES('Promotion', 'StartDate', 3);
INSERT INTO listcolumns VALUES('Promotion', 'EndDate', 4);
INSERT INTO listcolumns VALUES('Promotion', 'NoOfOrders', 5);
INSERT INTO listcolumns VALUES('Promotion', 'Status', 6);
DELETE FROM viewcolumns WHERE viewid = 76;
INSERT INTO viewcolumns VALUES(76, 'Title', 1);
INSERT INTO viewcolumns VALUES(76, 'Description', 2);
INSERT INTO viewcolumns VALUES(76, 'StartDate', 3);
INSERT INTO viewcolumns VALUES(76, 'EndDate', 4);
INSERT INTO viewcolumns VALUES(76, 'NoOfOrders', 5);
INSERT INTO viewcolumns VALUES(76, 'Status', 6);
UPDATE listviews SET sortmember = 'Title' WHERE viewid = 73;
DELETE FROM listcolumns WHERE listtype = 'Event';
INSERT INTO listcolumns VALUES('Event', 'EventID', 1);
INSERT INTO listcolumns VALUES('Event', 'Title', 2);
INSERT INTO listcolumns VALUES('Event', 'Description', 3);
INSERT INTO listcolumns VALUES('Event', 'Start', 4);
INSERT INTO listcolumns VALUES('Event', 'End', 5);
INSERT INTO listcolumns VALUES('Event', 'RegisteredAttendees', 6);
INSERT INTO listcolumns VALUES('Event', 'OwnerName', 7);
DELETE FROM viewcolumns WHERE viewid = 73;
INSERT INTO viewcolumns VALUES(73, 'Title', 1);
INSERT INTO viewcolumns VALUES(73, 'Description', 2);
INSERT INTO viewcolumns VALUES(73, 'Start', 3);
INSERT INTO viewcolumns VALUES(73, 'End', 4);
INSERT INTO viewcolumns VALUES(73, 'RegisteredAttendees', 5);
INSERT INTO viewcolumns VALUES(73, 'OwnerName', 6);

UPDATE listviews SET sortmember = 'TimeSlipID' WHERE viewid = 92;
DELETE FROM listcolumns WHERE listtype = 'Timeslip';
INSERT INTO listcolumns VALUES('Timeslip', 'TimeSlipID', 1);
INSERT INTO listcolumns VALUES('Timeslip', 'Description', 2);
INSERT INTO listcolumns VALUES('Timeslip', 'Project', 3);
INSERT INTO listcolumns VALUES('Timeslip', 'Task', 4);
INSERT INTO listcolumns VALUES('Timeslip', 'Duration', 5);
INSERT INTO listcolumns VALUES('Timeslip', 'CreatedBy', 6);
INSERT INTO listcolumns VALUES('Timeslip', 'Date', 7);
INSERT INTO listcolumns VALUES('Timeslip', 'StartTime', 8);
INSERT INTO listcolumns VALUES('Timeslip', 'EndTime', 9);

DELETE FROM viewcolumns WHERE viewid = 92;
INSERT INTO viewcolumns VALUES(92, 'TimeSlipID', 1);
INSERT INTO viewcolumns VALUES(92, 'Description', 2);
INSERT INTO viewcolumns VALUES(92, 'Project', 3);
INSERT INTO viewcolumns VALUES(92, 'Task', 4);
INSERT INTO viewcolumns VALUES(92, 'Duration', 5);
INSERT INTO viewcolumns VALUES(92, 'CreatedBy', 6);
INSERT INTO viewcolumns VALUES(92, 'Date', 7);
INSERT INTO viewcolumns VALUES(92, 'StartTime', 8);
INSERT INTO viewcolumns VALUES(92, 'EndTime', 9);

delete from listcolumns where listtype='Order' and columnname='Sales';
update listcolumns set columnorder=5  where listtype='Order' and columnorder=6;

update viewcolumns set columnname='OrderID' where columnname='Order' and viewid=87;
update viewcolumns set columnname='Customer' where columnname='CustomerID' and viewid=87;
update listcolumns set columnname='Customer' where columnname='CustomerID' and listtype='InvoiceHistory';
update listcolumns set columnname='OrderID' where columnname='Order' and listtype='InvoiceHistory';


update viewcolumns set columnname='CreatedBy' where columnname='EnteredBy' and viewid=82;
update listcolumns set columnname='CreatedBy' where columnname='EnteredBy' and listtype='Payment';

update viewcolumns set columnname='EntityName' where columnname='Reference' and viewid=83;

UPDATE listviews SET sortmember = 'SKU' WHERE viewid = 77;
DELETE FROM listcolumns WHERE listtype = 'Item';
INSERT INTO listcolumns VALUES('Item', 'SKU', 1);
INSERT INTO listcolumns VALUES('Item', 'Name', 2);
INSERT INTO listcolumns VALUES('Item', 'Type', 3);
INSERT INTO listcolumns VALUES('Item', 'Price', 4);
INSERT INTO listcolumns VALUES('Item', 'OnHand', 5);
INSERT INTO listcolumns VALUES('Item', 'Tax', 6);
INSERT INTO listcolumns VALUES('Item', 'Cost', 7);
INSERT INTO listcolumns VALUES('Item', 'Vendor', 8);
INSERT INTO listcolumns VALUES('Item', 'Manufacturer', 9);

DELETE FROM viewcolumns WHERE viewid = 77;
INSERT INTO viewcolumns VALUES(77, 'SKU', 1);
INSERT INTO viewcolumns VALUES(77, 'Name', 2);
INSERT INTO viewcolumns VALUES(77, 'Type', 3);
INSERT INTO viewcolumns VALUES(77, 'Price', 4);
INSERT INTO viewcolumns VALUES(77, 'OnHand', 5);
INSERT INTO viewcolumns VALUES(77, 'Tax', 6);
INSERT INTO viewcolumns VALUES(77, 'Cost', 7);
INSERT INTO viewcolumns VALUES(77, 'Vendor', 8);
INSERT INTO viewcolumns VALUES(77, 'Manufacturer', 9);

UPDATE `recurrence` SET Every = Month(startdate) WHERE TimePeriod='YEAR' AND Every > 12;

-- Related info notes list.
DELETE FROM `viewcolumns` WHERE `viewid` = 106 AND `columnorder` = 3;
UPDATE `viewcolumns` SET `columnorder` = 3 WHERE `viewid` = 106 AND `columnname` = 'createdby';
UPDATE `viewcolumns` SET `columnorder` = 4 WHERE `viewid` = 106 AND `columnname` = 'detail';
UPDATE `listviews` SET `sortmember` = 'Date' WHERE `listtype` = 'BottomNotes' AND `viewid` = 106;
UPDATE `listviews` SET `sorttype` = 'D' WHERE `listtype` = 'BottomNotes' AND `viewid` = 106;

UPDATE listcolumns SET columnname = 'CreatedBy' WHERE listtype = 'TimeSheet' AND columnorder = 6;

-- Employee lists columns.
UPDATE listcolumns SET columnname = 'Entity' WHERE listtype = 'Employee' AND columnorder = 3;
UPDATE viewcolumns SET columnname = 'Entity' WHERE viewid = 170 AND columnorder = 3;

-- New email lookup list type.
INSERT INTO listtypes VALUES('EmailLookup', 74);
INSERT INTO listcolumns VALUES('EmailLookup', 'To', 1);
INSERT INTO listcolumns VALUES('EmailLookup', 'Cc', 2);
INSERT INTO listcolumns VALUES('EmailLookup', 'Bcc', 3);
INSERT INTO listcolumns VALUES('EmailLookup', 'Name', 4);
INSERT INTO listcolumns VALUES('EmailLookup', 'Address', 5);
INSERT INTO listviews VALUES('EmailLookup', 30, 'Default View', NULL, NULL, 'Name', 'A', 0, 100, 'A');
DELETE FROM viewcolumns WHERE viewid = 30;
INSERT INTO viewcolumns VALUES(30, 'To', 1);
INSERT INTO viewcolumns VALUES(30, 'Cc', 2);
INSERT INTO viewcolumns VALUES(30, 'Bcc', 3);
INSERT INTO viewcolumns VALUES(30, 'Name', 4);
INSERT INTO viewcolumns VALUES(30, 'Address', 5);
INSERT INTO defaultviews VALUES('EmailLookup', 30);

UPDATE `activity` SET details = concat(details ,'\n',notes);