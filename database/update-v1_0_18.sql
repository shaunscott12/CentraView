-- Fix for restoring Projects from Garbage or Attic.
UPDATE `field` SET `name`='Creator' WHERE `fieldid`=461 AND `tableid`=48;
INSERT INTO `restoresequence` VALUEs (9, 48, 1, 48);

-- Fix for Expense forms status
ALTER TABLE `expenseform` ADD `Statust` enum('Paid','Unpaid') NOT NULL DEFAULT "Unpaid";
UPDATE `expenseform` set Statust='Unpaid' WHERE status='Deleted';
UPDATE `expenseform` set Statust='Paid' WHERE status='Active';
ALTER TABLE `expenseform` DROP `Status`;
ALTER TABLE `expenseform` CHANGE `Statust` `Status` enum('Paid','Unpaid') NOT NULL DEFAULT "Unpaid";

-- Added the Field Jurisdiction for the Address
Alter Table `address` ADD `jurisdictionID` int(11) unsigned default 0;


ALTER TABLE `invoiceitems` DROP `Tax Rate`;
ALTER TABLE `invoiceitems` DROP `Tax`;
ALTER TABLE `invoiceitems` ADD `taxAmount` float NOT NULL default '0';
ALTER TABLE `orderitem` ADD `taxAmount` float NOT NULL default '0';


ALTER TABLE `proposalitem` DROP `Tax Rate`;
ALTER TABLE `proposalitem` DROP `Tax`;
ALTER TABLE `proposalitem` ADD `taxAmount` float NOT NULL default '0';

ALTER TABLE `proposalitem` MODIFY `Quantity` int(11) unsigned default 0;

-- tables needed for webform import
DROP TABLE IF EXISTS `mailimporttypes`;
CREATE TABLE `mailimporttypes`
(
  typeID int(11) unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name varchar(255) NOT NULL
) TYPE=InnoDB;
INSERT INTO `mailimporttypes` VALUES (1, 'Contact');

DROP TABLE IF EXISTS `mailimportfields`;
CREATE TABLE `mailimportfields`
(
  fieldID int(11) unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
  typeID int(11) unsigned NOT NULL,
  name varchar(255) NOT NULL,
  fieldName varchar(255) NOT NULL,
  KEY `typeID` (`typeID`)
) TYPE=InnoDB;
INSERT INTO `mailimportfields` VALUES (1, 1, 'First Name', 'firstName');
INSERT INTO `mailimportfields` VALUES (2, 1, 'MI', 'middleInitial');
INSERT INTO `mailimportfields` VALUES (3, 1, 'Last Name', 'lastName');
INSERT INTO `mailimportfields` VALUES (4, 1, 'Street1', 'street1');
INSERT INTO `mailimportfields` VALUES (5, 1, 'Street2', 'street2');
INSERT INTO `mailimportfields` VALUES (6, 1, 'City', 'city');
INSERT INTO `mailimportfields` VALUES (7, 1, 'State', 'state');
INSERT INTO `mailimportfields` VALUES (8, 1, 'Zip', 'zip');
INSERT INTO `mailimportfields` VALUES (9, 1, 'Country', 'country');
INSERT INTO `mailimportfields` VALUES (10, 1, 'Main Phone', 'mainPhone');
INSERT INTO `mailimportfields` VALUES (11, 1, 'Fax Phone', 'faxPhone');
INSERT INTO `mailimportfields` VALUES (12, 1, 'Mobile Phone', 'mobilePhone');
INSERT INTO `mailimportfields` VALUES (13, 1, 'Home Phone', 'homePhone');
INSERT INTO `mailimportfields` VALUES (14, 1, 'Other Phone', 'otherPhone');
INSERT INTO `mailimportfields` VALUES (15, 1, 'Pager Phone', 'pagerPhone');
INSERT INTO `mailimportfields` VALUES (16, 1, 'Work Phone', 'workPhone');
INSERT INTO `mailimportfields` VALUES (17, 1, 'Email', 'email');
INSERT INTO `mailimportfields` VALUES (18, 1, 'Source', 'source');
INSERT INTO `mailimportfields` VALUES (19, 1, 'Title', 'title');
INSERT INTO `mailimportfields` VALUES (20, 1, 'ID2', 'ID2');
INSERT INTO `mailimportfields` VALUES (21, 1, 'Entity Name', 'entityName');
INSERT INTO `mailimportfields` VALUES (22, 1, 'Entity Street1', 'entityStreet1');
INSERT INTO `mailimportfields` VALUES (23, 1, 'Entity Street2', 'entityStreet2');
INSERT INTO `mailimportfields` VALUES (24, 1, 'Entity City', 'entityCity');
INSERT INTO `mailimportfields` VALUES (25, 1, 'Entity State', 'entityState');
INSERT INTO `mailimportfields` VALUES (26, 1, 'Entity Zip', 'entityZip');
INSERT INTO `mailimportfields` VALUES (27, 1, 'Entity Country', 'entityCountry');
INSERT INTO `mailimportfields` VALUES (28, 1, 'Entity Main Phone', 'entityMainPhone');
INSERT INTO `mailimportfields` VALUES (29, 1, 'Entity Fax Phone', 'entityFaxPhone');
INSERT INTO `mailimportfields` VALUES (30, 1, 'Entity Mobile Phone', 'entityMobilePhone');
INSERT INTO `mailimportfields` VALUES (31, 1, 'Entity Home Phone', 'entityHomePhone');
INSERT INTO `mailimportfields` VALUES (32, 1, 'Entity Other Phone', 'entityOtherPhone');
INSERT INTO `mailimportfields` VALUES (33, 1, 'Entity Pager Phone', 'entityPagerPhone');
INSERT INTO `mailimportfields` VALUES (34, 1, 'Entity Work Phone', 'entityWorkPhone');
INSERT INTO `mailimportfields` VALUES (35, 1, 'Entity Email', 'entityEmail');
INSERT INTO `mailimportfields` VALUES (36, 1, 'Entity Source', 'entitySource');
INSERT INTO `mailimportfields` VALUES (37, 1, 'Entity ID2', 'entityID2');
INSERT INTO `mailimportfields` VALUES (38, 1, 'Main Phone Ext', 'mainPhoneExt');
INSERT INTO `mailimportfields` VALUES (39, 1, 'Fax Phone Ext', 'faxPhoneExt');
INSERT INTO `mailimportfields` VALUES (40, 1, 'Mobile Phone Ext', 'mobilePhoneExt');
INSERT INTO `mailimportfields` VALUES (41, 1, 'Home Phone Ext', 'homePhoneExt');
INSERT INTO `mailimportfields` VALUES (42, 1, 'Other Phone Ext', 'otherPhoneExt');
INSERT INTO `mailimportfields` VALUES (43, 1, 'Pager Phone Ext', 'pagerPhoneExt');
INSERT INTO `mailimportfields` VALUES (44, 1, 'Work Phone Ext', 'workPhoneExt');
INSERT INTO `mailimportfields` VALUES (45, 1, 'Entity Main Phone Ext', 'entityMainPhoneExt');
INSERT INTO `mailimportfields` VALUES (46, 1, 'Entity Fax Phone Ext', 'entityFaxPhoneExt');
INSERT INTO `mailimportfields` VALUES (47, 1, 'Entity Mobile Phone Ext', 'entityMobilePhoneExt');
INSERT INTO `mailimportfields` VALUES (48, 1, 'Entity Home Phone Ext', 'entityHomePhoneExt');
INSERT INTO `mailimportfields` VALUES (49, 1, 'Entity Other Phone Ext', 'entityOtherPhoneExt');
INSERT INTO `mailimportfields` VALUES (50, 1, 'Entity Pager Phone Ext', 'entityPagerPhoneExt');
INSERT INTO `mailimportfields` VALUES (51, 1, 'Entity Work Phone Ext', 'entityWorkPhoneExt');



ALTER TABLE `proposal` ADD `orderID` int(11) unsigned default 0;

-- script for the bug 1312
delete from `listcolumns` where listtype='user' and columnname='UserName' and columnorder='5';
delete from `listcolumns` where listtype='user' and columnname='Name' and columnorder='3';
delete from `listcolumns` where listtype='user' and columnname='Entity' and columnorder='4';
insert into listcolumns values('user','UserName',3);
insert into listcolumns values('user','Name',4);
insert into listcolumns values('user','Entity',5);
delete from `viewcolumns` where viewid=174 AND columnname='UserName' AND columnorder='3';
delete from `viewcolumns` where viewid=174 AND columnname='Name' AND columnorder='1';
delete from `viewcolumns` where viewid=174 AND columnname='Entity' AND columnorder='2';
delete from `viewcolumns` where viewid=174 AND columnname='Email' AND columnorder='4';
delete from `viewcolumns` where viewid=174 AND columnname='Enabled' AND columnorder='5';
insert into viewcolumns values(174,'UserName',1);
insert into viewcolumns values(174,'Name',2);
insert into viewcolumns values(174,'Entity',3);
insert into viewcolumns values(174,'Enabled',4);

-- fix for bug 1866
delete from viewcolumns where viewid=12 and columnname='To' and columnorder=3;


ALTER TABLE `activity` MODIFY `Title` varchar(255) default NULL;
ALTER TABLE `expenseform` ADD `employeeID` int(10) unsigned NOT NULL default '0';
update `expenseform` set employeeID=owner;

Alter Table `expenseform` ADD `lineStatus` enum('Deleted','Active') NOT NULL default 'Active';

update `emailtemplate` set requiredFromAddress='NO',requiredReplyTo='NO' where templateID in(4,5) ;