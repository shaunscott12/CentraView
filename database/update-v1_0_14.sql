-- Added the entries for Note's Advance Search

 insert into searchmodule values(5,1,'N');
 insert into searchmodule values(5,2,'N');
-- insert into searchmodule values(5,7,'N');

-- Added the entries for File's Advance Search
-- insert into searchmodule values(6,9,'Y');

-- updating the information of the Email module 
 update module set primarytable='emailmessage' where moduleid=2;
 update module set ownerfield='Owner' where moduleid=2;
 
 -- Added the entries for Email's Advance Search
 insert into searchmodule values(2,8,'Y');
 
 -- RuleCriteria Entries for Rule Module
 
 INSERT INTO `module` (`parentid`, `applyrights`, `hasfields`, `primarytable`, `ownerfield`, `primarykeyfield`, `otherownerfield`, `otherprimarytable`, `name`) VALUES (NULL,0,0,'emailrule','','ruleID','','','Email Rule');
 
 SELECT @emailRuleModuleID := moduleid FROM module WHERE
 UPPER(primarytable) = UPPER('emailrule');
 
 INSERT INTO `searchtable` (`DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`)
 VALUES ('RuleCriteria', '', '', NULL);
 
 set @ruleCriteriaTableID = LAST_INSERT_ID();
 
 INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
 VALUES (@ruleCriteriaTableID, 'Body', 'Body');
 
 INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
 VALUES (@ruleCriteriaTableID, 'Subject', 'Subject');
 
 INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
 VALUES (@ruleCriteriaTableID, 'Sender', 'Sender');
 
 INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
 VALUES (@ruleCriteriaTableID, 'Recipient', 'Recipient');
 
 INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
 VALUES (@ruleCriteriaTableID, 'Message Date', 'MessageDate');

-- update the information of the email Entries in the Advance Search

update searchfield set FieldName='MailFrom',IsOnTable='Y' where SearchFieldID = 66;

update searchfield set FieldName='Address', IsOnTable='N',RealTableName='emailrecipient',RelationshipQuery='emailrecipient.MessageID=emailmessage.MessageID' where SearchFieldID = 67;

-- Rule Entries for the Advance Search

INSERT INTO `searchtable` (`DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Rule', 'emailrule', 'ruleID', NULL);

set @emailRuleTableID = LAST_INSERT_ID();

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@emailRuleModuleID, @emailRuleTableID, 'Y');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@emailRuleTableID, 'Name', 'name');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@emailRuleTableID, 'Description', 'description');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@emailRuleTableID, 'Enabled', 'enabled');


-- Email Entries for the Advance Search

SELECT @emailTableID := SearchTableID from searchtable where DisplayName='Email';

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@emailTableID, 'Owned By', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = emailmessage.Owner');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@emailTableID, 'From Individual', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = emailmessage.FromIndividual');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@emailTableID, 'Headers', 'Headers');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@emailTableID, 'Priority', 'Priority');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@emailTableID, 'Size', 'Size');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@emailTableID, 'Importance', 'Importance');

-- Update the Rule Column name from RuleName to Name

update listcolumns set columnname='Name' where columnname='RuleName' and listtype='Rule';
update listcolumns set columnname='Enabled' where columnname='EnabledStatus' and listtype='Rule';
update viewcolumns set columnname='Enabled' where columnname='EnabledStatus' and viewid=23;
update viewcolumns set columnname='Name' where columnname='RuleName' and viewid=23;
update listviews set sortmember='Name' where viewid=23;

-- Data changes for bugs 1515 and 1377
-- Adding Start and End Times to the activities.
insert into `viewcolumns`(`viewid`,`columnname`,`columnorder`) values (20,'Start',8);
insert into `viewcolumns`(`viewid`,`columnname`,`columnorder`) values (20,'End',9);
insert into `viewcolumns`(`viewid`,`columnname`,`columnorder`) values (20,'Due',4);
insert into `listcolumns`(`listtype`,`columnname`,`columnorder`) values ('MultiActivity','Start',8);
insert into `listcolumns`(`listtype`,`columnname`,`columnorder`) values ('MultiActivity','End',9);
insert into `listcolumns`(`listtype`,`columnname`,`columnorder`) values ('Appointment','End',8);
insert into `listcolumns`(`listtype`,`columnname`,`columnorder`) values ('Call','Start',9);
insert into `listcolumns`(`listtype`,`columnname`,`columnorder`) values ('Call','End',10);
insert into `viewcolumns`(`viewid`,`columnname`,`columnorder`) values (10,'Start',9);
insert into `viewcolumns`(`viewid`,`columnname`,`columnorder`) values (10,'End',10);
insert into `listcolumns`(`listtype`,`columnname`,`columnorder`) values ('NextAction','Due',7);
insert into `listcolumns`(`listtype`,`columnname`,`columnorder`) values ('NextAction','End',8);
insert into `viewcolumns`(`viewid`,`columnname`,`columnorder`) values (21,'Start',7);
insert into `viewcolumns`(`viewid`,`columnname`,`columnorder`) values (21,'End',8);
insert into `listcolumns`(`listtype`,`columnname`,`columnorder`) values ('ToDo','Due',7);
insert into `listcolumns`(`listtype`,`columnname`,`columnorder`) values ('ToDo','End',8);
insert into `viewcolumns`(`viewid`,`columnname`,`columnorder`) values (25,'Start',7);
insert into `viewcolumns`(`viewid`,`columnname`,`columnorder`) values (25,'End',8);

-- fix for bug 1678
-- Related Note list order.
UPDATE viewcolumns SET `columnorder` = 1 WHERE `viewid` = 22 AND `columnname` = 'Date';
UPDATE viewcolumns SET `columnorder` = 2 WHERE `viewid` = 22 AND `columnname` = 'Title';
UPDATE viewcolumns SET `columnorder` = 3 WHERE `viewid` = 22 AND `columnname` = 'Detail';
UPDATE viewcolumns SET `columnorder` = 4 WHERE `viewid` = 22 AND `columnname` = 'CreatedBy';


-- Literature FulFillment
delete from searchfield where searchfieldid in (288,292,294,295,289);

update module set primarytable='literaturerequest', ownerfield='' where moduleid=34;

SELECT @literatureModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('literaturerequest');

SELECT @entityModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('entity');

SELECT @individualModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('individual');

SELECT @activityModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('activity');

SELECT @entityTableId := SearchTableID FROM searchtable WHERE 
UPPER(DisplayName) = UPPER('Entity');

SELECT @individualTableId := SearchTableID FROM searchtable WHERE 
UPPER(DisplayName) = UPPER('Individual');

SELECT @activityTableId := SearchTableID FROM searchtable WHERE 
UPPER(DisplayName) = UPPER('Activities');

SELECT @literatureTableID := SearchTableID FROM searchtable WHERE 
UPPER(DisplayName) = UPPER('Literature Request');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@literatureModuleId, @activityTableId, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@literatureModuleId, @literatureTableID, 'Y');


INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@literatureTableID, 'Description', 'Description');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@literatureTableID, 'Literature', 'literature.Title', 'N', 'literature',
'literature.LiteratureID = literaturerequest.LiteratureID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@literatureTableID, 'Owner', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = activity.Owner AND activity.ActivityID = literaturerequest.ActivityID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@literatureTableID, 'Assigned To', 'CONCAT(ass22.FirstName, " ", ass22.LastName)', 'N', 'individual ass22',
'ass22.IndividualID = literaturerequest.RequestedBy');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@literatureTableID, @activityTableId, 'activity.ActivityID = literaturerequest.ActivityID ');

-- Marketing List
delete from searchfield where searchfieldid in (273,274,275,276);

SELECT @marketingModuleId := moduleid FROM module WHERE 
UPPER(name) = UPPER('ListManager');

SELECT @marketingTableID := SearchTableID FROM searchtable WHERE 
UPPER(DisplayName) = UPPER('Marketing List');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@marketingModuleId, @entityTableId, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@marketingModuleId, @individualTableId, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@marketingModuleId, @marketingTableID, 'Y');


INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@marketingModuleId, @entityTableId, 'marketinglist.ListID = entity.list');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@marketingModuleId, @individualTableId, 'marketinglist.ListID = entity.list');

-- Promotion
delete from searchfield where searchfieldid in (285,286);

SELECT @promotionModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('promotion');

SELECT @promotionTableID := SearchTableID FROM searchtable WHERE 
UPPER(DisplayName) = UPPER('Promotions');

SELECT @itemTableID := SearchTableID FROM searchtable WHERE 
UPPER(DisplayName) = UPPER('Item');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@promotionModuleId, @itemTableID, 'N');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@promotionTableID, 'PromotionID', 'PromotionID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@promotionTableID, 'Notes', 'Notes');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@promotionTableID, 'Owner', 'CONCAT(ind21.FirstName, " ", ind21.LastName)', 'N', 'individual ind21',
'ind21.IndividualID = promotion.Owner');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@promotionTableID, @itemTableID, 'item.itemid=promoitem.itemid AND promoitem.PromotionID = promotion.PromotionID');


-- Event
delete from searchfield where searchfieldid in (296,297,300);

SELECT @eventModuleId := moduleid FROM module WHERE 
UPPER(name) = UPPER('Events');

SELECT @eventTableID := SearchTableID FROM searchtable WHERE 
UPPER(DisplayName) = UPPER('Event');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@eventModuleId, @entityTableId, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@eventModuleId, @individualTableId, 'N');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@eventModuleId, @eventTableID, 'Y');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@eventTableID, 'EventID', 'EventID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@eventTableID, 'Title', 'Title');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@eventTableID, 'Description', 'Detail');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@eventTableID, 'Who Should Attend', 'ForMember');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@eventTableID, 'Owner', 'CONCAT(own23.FirstName, " ", own23.LastName)', 'N', 'individual own23',
'own23.IndividualID = event.Owner');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@eventTableID, 'Modified By', 'CONCAT(mod23.FirstName, " ", mod23.LastName)', 'N', 'individual mod23',
'mod23.IndividualID = event.ModifiedBy');


INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@eventTableID, @entityTableId, 'event.eventID = eventregister.EventID AND individual.IndividualID=eventregister.IndividualID AND entity.EntityID=individual.entity');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@eventTableID, @individualTableId, 'event.eventID = eventregister.EventID AND individual.IndividualID=eventregister.IndividualID');


-- Give every user a default "syncAsPrivate" preference
INSERT INTO userpreference (individualid, moduleid, preference_name, preference_value) SELECT individualid, 61, 'syncAsPrivate', 'YES' FROM user;

-- Set the default value for "syncAsPrivate" for all new users
INSERT INTO userpreferencedefault (moduleid, preference_name, preference_value) VALUES (1, 'syncAsPrivate', 'YES');

-- This was missing from "userpreferencedefault"
INSERT INTO userpreferencedefault (moduleid, preference_name, preference_value) VALUES (1, 'allRecordsPublic', 'Yes');

-- New table for tracking POP UIDL strings of downloaded messages
DROP TABLE IF EXISTS `uidlist`;
CREATE TABLE `uidlist` (
  `accountID` int(11) unsigned NOT NULL,
  `uid` varchar(100) NOT NULL,
  UNIQUE KEY `messageUID` (`accountID`, `uid`),
  INDEX `accountID` (`accountID`)
) TYPE=InnoDB;

DROP TABLE IF EXISTS `cvfilelink`;
CREATE TABLE `cvfilelink` (
  `FileID` int(11) unsigned NOT NULL default '0',
  `RecordTypeID` int(11) unsigned NOT NULL default '0',
  `RecordID` int(11) unsigned NOT NULL default '0',
  KEY `FileID` (`FileID`),
  KEY `RecordTypeID` (`RecordTypeID`),
  KEY `RecordID` (`RecordID`)
) TYPE=InnoDB;


-- Project 


SELECT @projectModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('project');

SELECT @projectTableId := SearchTableID FROM searchtable WHERE 
UPPER(DisplayName) = UPPER('Projects');

SELECT @fileModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('cvfile');

SELECT @fileTableId := SearchTableID FROM searchtable WHERE 
UPPER(DisplayName) = UPPER('Files');


INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@projectModuleId, @fileTableId, 'Y');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@projectTableId, @fileTableId, 'cvfilelink.RecordTypeID = 36 AND project.ProjectID = cvfilelink.RecordID ');

update searchfield set FieldName='entity.name' , RealTableName='entity' where SearchFieldID=231;

update searchfield set FieldName='CONCAT(ind12.FirstName, " ", ind12.LastName)' , RealTableName='individual ind12' , RelationshipQuery='ind12.individualid = projectlink.recordid AND projectlink.recordtypeid = 15 AND projectlink.projectid = project.projectid' where SearchFieldID=232;



update searchtable set TableName='purchaseorder' where searchtableID=26 ;

alter table `inventory` ADD `Owner` int(11) unsigned NOT NULL default '0';

update module set ownerfield='Owner' where moduleid=48;

update module set primarytable='vendor' where moduleid=50;

update module set primarytable='employee' where moduleid=54;


update module set primarytable='timesheet',ownerfield='owner',primarykeyfield='TimeSheetID' where moduleid=52;

update module set primarytable='expenseform',ownerfield='Owner',primarykeyfield='ExpenseFormID' where moduleid=51;

-- Item
delete from searchfield where SearchFieldID in (380);

update searchfield set FieldName='item27.Title',RealTableName='item item27', RelationshipQuery='item27.itemid = item.parent', IsOnTable='N' where SearchFieldID = 372;

SELECT @ItemOrderModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('item');

SELECT @ItemOrderTableId := SearchTableID FROM searchtable WHERE 
UPPER(DisplayName) = UPPER('Item');


INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@ItemOrderModuleId, @ItemOrderTableId, 'Y');

-- Order 

delete from searchfield where SearchFieldID in (180,187,251,252,256,257,258,259,260,261,254,253);

update searchfield set DisplayName='Notes',FieldName='Description' where SearchFieldID = 249;

update searchfield set FieldName='en18.Name',RealTableName='entity en18', RelationshipQuery='en18.EntityID = cvorder.en18', IsOnTable='N' where SearchFieldID = 250;

update searchfield set FieldName='proj18.ProjectTitle',RealTableName='project proj18', RelationshipQuery='proj18.ProjectID = cvorder.project', IsOnTable='N' where SearchFieldID = 255;

SELECT @orderModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('cvorder');

SELECT @orderTableID := SearchTableID FROM searchtable WHERE 
UPPER(DisplayName) = UPPER('Order History');

SELECT @addressTableId := SearchTableID FROM searchtable WHERE 
UPPER(DisplayName) = UPPER('Address');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@orderModuleId, @addressTableId, 'N');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@orderTableID, @addressTableId, '( address.AddressID = cvorder.BillAddress OR address.AddressID = cvorder.ShipAddress )');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@orderModuleId, @ItemOrderTableId, 'N');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@orderTableID, @ItemOrderTableId, 'item.itemid=orderitem.itemid AND orderitem.orderid = cvorder.orderid');

-- Invoice 

delete from searchfield where SearchFieldID in (199,205,206,265,266,267);

update searchfield set DisplayName='Notes',FieldName='Description' where SearchFieldID = 200;

update searchfield set FieldName='OrderID' where SearchFieldID = 262;

update searchfield set FieldName='CONCAT(indv19.FirstName, " ", indv19.LastName)',RealTableName='individual indv19', RelationshipQuery='indv19.IndividualID = invoice.Creator', IsOnTable='N' where SearchFieldID = 263;

update searchfield set FieldName='CONCAT(indiv19.FirstName, " ", indiv19.LastName)',RealTableName='individual indiv19', RelationshipQuery='indiv19.IndividualID = invoice.`Modified By`' , IsOnTable='N' where SearchFieldID = 264;

update searchfield set FieldName='proj19.ProjectTitle',RealTableName='project proj19', RelationshipQuery='proj19.ProjectID = invoice.project', IsOnTable='N' where SearchFieldID = 268;

update searchfield set FieldName='InvoiceDate' where SearchFieldID = 269;

SELECT @invoiceModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('invoice');

SELECT @invoiceTableID := SearchTableID FROM searchtable WHERE 
UPPER(DisplayName) = UPPER('Invoice History');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@invoiceModuleId, @addressTableId, 'N');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@invoiceTableID, @addressTableId, '( address.AddressID = invoice.billaddress OR address.AddressID = invoice.shipaddress )');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@invoiceModuleId, @ItemOrderTableId, 'N');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@invoiceTableID, @ItemOrderTableId, 'item.itemid=invoiceitems.itemid AND invoiceitems.InvoiceID = invoice.InvoiceID');

-- Payment

delete from searchfield where SearchFieldID in (305,317,318);

update searchfield set RelationshipQuery='entity24.EntityID = payment.EntityID', IsOnTable='N' where SearchFieldID = 307;

update searchfield set DisplayName='Check Number' where SearchFieldID = 314;

update searchfield set FieldName='CONCAT(individual24.FirstName, " ", individual24.LastName)',RealTableName='individual individual24', RelationshipQuery='individual24.IndividualID = payment.modifiedby' , IsOnTable='N' where SearchFieldID = 316;

SELECT @paymentModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('payment');

SELECT @paymentTableId := SearchTableID FROM searchtable WHERE 
UPPER(DisplayName) = UPPER('Payment');


INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@paymentModuleId, @paymentTableId, 'Y');

-- Expense

delete from searchfield where SearchFieldID in (333,334);

update searchfield set RelationshipQuery='entity25.EntityID = expense.EntityID' where SearchFieldID = 324;

update searchfield set FieldName='opportunity26.Title',RealTableName='opportunity opportunity26', RelationshipQuery='expense.opportunity = opportunity26.opportunityid', IsOnTable='N' where SearchFieldID = 332;


SELECT @expenseModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('expense');

SELECT @expenseTableId := SearchTableID FROM searchtable WHERE 
UPPER(DisplayName) = UPPER('Expense');


INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@expenseModuleId, @expenseTableId, 'Y');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@expenseModuleId, @ItemOrderTableId, 'N');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@expenseTableId, @ItemOrderTableId, 'item.itemid=expenseitem.ExpenseItemID AND expenseitem.ExpenseID = invoice.ExpenseID');


-- Purchase Order

delete from searchfield where SearchFieldID in (340,342,345,347,349,351,361);


update searchfield set DisplayName='Notes' where SearchFieldID = 341;

update searchfield set FieldName='CONCAT(individual26.FirstName, " ", individual26.LastName)',RealTableName='individual individual26', RelationshipQuery='individual26.IndividualID = purchaseorder.modifiedby' , IsOnTable='N' where SearchFieldID = 355;


SELECT @PurchaseOrderModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('purchaseorder');

SELECT @PurchaseOrderTableId := SearchTableID FROM searchtable WHERE 
UPPER(DisplayName) = UPPER('Purchase Order');


INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@PurchaseOrderModuleId, @PurchaseOrderTableId, 'Y');


INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@PurchaseOrderModuleId, @addressTableId, 'N');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@PurchaseOrderTableId, @addressTableId, '( address.AddressID = purchaseorder.BillAddress OR address.AddressID = purchaseorder.ShipAddress )');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@PurchaseOrderModuleId, @ItemOrderTableId, 'N');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@PurchaseOrderTableId, @ItemOrderTableId, 'item.itemid=purchaseorderitem.itemid AND purchaseorderitem.purchaseorderid = purchaseorder.purchaseorderid');


-- Inventory

delete from searchfield where SearchFieldID in (397,398);

update searchfield set RelationshipQuery='entity29.EntityID = inventory.customerid' where SearchFieldID = 393;

update searchfield set RelationshipQuery='',FieldName='created', IsOnTable='Y',RealTableName='' where SearchFieldID = 394;

update searchfield set RelationshipQuery='',FieldName='modified', IsOnTable='Y',RealTableName='' where SearchFieldID = 395;

SELECT @InventoryOrderModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('inventory');

SELECT @InventoryOrderTableId := SearchTableID FROM searchtable WHERE 
UPPER(DisplayName) = UPPER('Inventory');


INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@InventoryOrderModuleId, @InventoryOrderTableId, 'Y');

-- Vendor

SELECT @vendorModuleId := moduleid FROM module WHERE 
UPPER(name) = UPPER('vendor');

SELECT @vendorTableId := SearchTableID FROM searchtable WHERE 
UPPER(DisplayName) = UPPER('Vendor');


INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@vendorModuleId, @vendorTableId, 'Y');

SELECT @entityModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('entity');

SELECT @individualModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('individual');

SELECT @entityTableId := SearchTableID FROM searchtable WHERE 
UPPER(DisplayName) = UPPER('Entity');

SELECT @individualTableId := SearchTableID FROM searchtable WHERE 
UPPER(DisplayName) = UPPER('Individual');


INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@vendorModuleId, @entityTableId, 'N');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@vendorTableId, @entityTableId, 'entity.entityid=vendor.entityid');


INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@vendorModuleId, @individualTableId, 'N');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@vendorTableId, @individualTableId, 'individual.entity=vendor.entityid');


-- Employee



SELECT @employeeModuleId := moduleid FROM module WHERE 
UPPER(name) = UPPER('EmployeeList');


INSERT INTO `searchtable` (`DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('Employee', 'employee', 'IndividualID', NULL);

set @employeeTableID = LAST_INSERT_ID();

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@employeeModuleId, @employeeTableID, 'Y');

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@employeeModuleId, @individualTableId, 'N');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@employeeTableID, @individualTableId, 'individual.IndividualID=employee.IndividualID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@employeeTableID, 'Individual', 'CONCAT(individual35.FirstName, " ", individual35.LastName)', 'N', 'individual individual35',
'individual35.IndividualID = employee.IndividualID');


-- Expense Form

SELECT @expenseFormModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('expenseform');

SELECT @expenseFormTableId := SearchTableID FROM searchtable WHERE 
UPPER(DisplayName) = UPPER('Expense Form');


INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@expenseFormModuleId, @expenseFormTableId, 'Y');


INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@expenseFormModuleId, @expenseTableId, 'N');

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`)
VALUES(@expenseFormTableId, @expenseTableId, 'expenseform.ExpenseFormID=expense.ExpenseFormID');



-- Time Sheet

delete from searchmodule where searchtableid=31 and moduleid=52;

SELECT @timesheetModuleId := moduleid FROM module WHERE 
UPPER(primarytable) = UPPER('timesheet');

INSERT INTO `searchtable` (`DisplayName`, `TableName`, `TablePrimaryKey`, `RecordPermissionQuery`)
VALUES ('TimeSheet', 'timesheet', 'TimeSheetID', NULL);

set @timesheetTableId = LAST_INSERT_ID();

INSERT INTO `searchmodule` (`ModuleID`, `SearchTableID`, `IsPrimaryTable`)
VALUES (@timesheetModuleId, @timesheetTableId, 'Y');


SELECT @timesheetTableId := SearchTableID FROM searchtable WHERE 
UPPER(DisplayName) = UPPER('TimeSheet');


INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@timesheetTableId, 'Time Sheet ID', 'TimeSheetID');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@timesheetTableId, 'Description', 'Description');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@timesheetTableId, 'Owner', 'CONCAT(individual33.FirstName, " ", individual33.LastName)', 'N', 'individual individual33',
'individual33.IndividualID = timesheet.Owner');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@timesheetTableId, 'Creator', 'CONCAT(Creator33.FirstName, " ", Creator33.LastName)', 'N', 'individual Creator33',
'Creator33.IndividualID = timesheet.Creator');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@timesheetTableId, 'Modified By', 'CONCAT(ModifiedBy33.FirstName, " ", ModifiedBy33.LastName)', 'N', 'individual ModifiedBy33',
'ModifiedBy33.IndividualID = timesheet.ModifiedBy');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@timesheetTableId, 'Modified By', 'CONCAT(ReportingTo33.FirstName, " ", ReportingTo33.LastName)', 'N', 'individual ReportingTo33',
'ReportingTo33.IndividualID = timesheet.ReportingTo');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@timesheetTableId, 'Created', 'Created');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@timesheetTableId, 'Modified', 'Modified');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@timesheetTableId, 'Start', 'Start');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@timesheetTableId, 'End', 'End');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@timesheetTableId, 'Notes', 'Notes');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`)
VALUES (@timesheetTableId, 'Notes', 'Notes');

-- Task
update searchfield set FieldName='CONCAT(ind13.FirstName, " ", ind13.LastName)' , RealTableName='individual ind13' , RelationshipQuery='ind13.individualid = activitylink.recordid AND activitylink.recordtypeid = 2 AND activitylink.activityid = task.activityid' where SearchFieldID=241;
update searchfield set FieldName='created13.created' , RealTableName='activity created13' , RelationshipQuery='created13.activityid = task.activityid' where SearchFieldID=235;
update searchfield set FieldName='modified13.modified' , RealTableName='activity modified13' , RelationshipQuery='modified13.activityid = task.activityid' where SearchFieldID=236;
update searchfield set FieldName='title13.title' , RealTableName='activity title13' , RelationshipQuery='title13.activityid = task.activityid' where SearchFieldID=237;
update searchfield set FieldName='description13.description' , RealTableName='activity description13' , RelationshipQuery='description13.activityid = task.activityid' where SearchFieldID=238;

insert into activitylink select activity.ActivityID,2,owner from activity, task where task.ActivityID = activity.ActivityID and activity.type=8;
insert into activitylink select activity.ActivityID,2,owner from activity, literaturerequest where literaturerequest.ActivityID = activity.ActivityID and activity.type=4;

update activity set owner=1 where type=8;
update activity set owner=1 where type=4;