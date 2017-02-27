-- added the Status & customerview field to the KnowledgeBase Table
ALTER TABLE `knowledgebase` ADD `status` enum('DRAFT','PUBLISH') NOT NULL default 'DRAFT';
ALTER TABLE `knowledgebase` ADD `publishToCustomerView` enum('YES','NO') NOT NULL default 'NO';

-- added the customerview field to the faq Table
ALTER TABLE `faq` ADD `publishToCustomerView` enum('YES','NO') NOT NULL default 'NO';

-- added the Status & customerview field to the Category Table
ALTER TABLE `category` ADD `status` enum('DRAFT','PUBLISH') NOT NULL default 'DRAFT';
ALTER TABLE `category` ADD `publishToCustomerView` enum('YES','NO') NOT NULL default 'NO';

update `category` set publishToCustomerView='YES' where catid=1;

-- Outland Fix to bug 881

insert into `viewcolumns`(viewid,columnname,columnorder) values(90,'Status',9);
insert into `listcolumns`(listtype,columnname,columnorder) values ('Task','Status',9);


-- Fix for the Bug 881 
update `listcolumns` set columnname = 'Manager' where listtype = 'Task' and columnname = 'Owner';
update `viewcolumns` set columnname = 'Manager' where viewid = 90 and columnname = 'Owner';

-- Setting the general System Email Setting



DROP TABLE IF EXISTS `emailsettings`;
CREATE TABLE `emailsettings` (
  `username` varchar(50) default NULL,
	`password` varchar(25) default NULL,
	`authentication` enum('YES','NO') default 'NO',
	`smtpport` int(4) unsigned NOT NULL default '25',
	`smtpserver` varchar(255) default NULL
) TYPE=InnoDB;

SELECT @username := username FROM serversettings ;
SELECT @password := password FROM serversettings ;
SELECT @smtpport := smtpport FROM serversettings ;
SELECT @authentication := authentication FROM serversettings ;
SELECT @smtpserver := smtpserver FROM serversettings ;
 
-- inserting a blank entry for the emailsettings
insert into emailsettings values (@username,@password,@authentication,@smtpport,@smtpserver);

-- Droping the Columns from the ServerSetting and added to the emailSettings table
Alter table `serversettings` Drop `username`;
Alter table `serversettings` Drop `password`;
Alter table `serversettings` Drop `smtpport`;
Alter table `serversettings` Drop `authentication`;
Alter table `serversettings` Drop `adminemailaddress`;
Alter table `serversettings` Drop `smtpserver`;

-- Creating the System's Email Template, which will be used for responding on the Emails
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
  PRIMARY KEY  (`templateID`),
) TYPE=InnoDB;

SELECT @supportinbox := MessageBody FROM supportemailmessage where Name='Ticket';
SELECT @supportticket := MessageBody FROM supportemailmessage where Name='Thread';
SELECT @supporterror := MessageBody FROM supportemailmessage where Name='Error' ;

-- Created predefined Email Templates for associated Action
insert into emailtemplate values(1,'Support Ticket','Auto-response Template for New Tickets.','','','','',@supportinbox,'NO','NO','NO','YES','YES');
insert into emailtemplate values(2,'Support Thread','Auto-response Template for Open Tickets (New Threads).','','','','', @supportticket,'NO','NO','NO','NO','YES');
insert into emailtemplate values(3,'Support Error','Auto-response Template for Errors (Invalid Ticket ID).','','','','', @supporterror,'NO','NO','NO','NO','YES');
insert into emailtemplate values(4,'Activities',"Auto-response Template for reminding email to Attendee's of Activity. ",'','','','', '','NO','YES','YES','YES','YES');
insert into emailtemplate values(5,'Task','Auto-response Template to notify that Task is completed.','','','','', '','NO','YES','YES','YES','YES');
insert into emailtemplate values(6,'Forgot Password','Auto-response Template for Forgot Password.','','','','', '','NO','YES','YES','YES','NO');
insert into emailtemplate values(7,'Suggestion Box','Auto-response Template for Suggestion Box.','','','','', '','YES','YES','YES','YES','NO');
insert into emailtemplate values(8,'Change Request for Profile','Auto-response Template for Profile change request.','','','','', '','YES','YES','YES','YES','NO');

-- Droping the table which will not be used from now on.. coz we implemented the Email Templates for it.
DROP TABLE IF EXISTS `suggestion`;
DROP TABLE IF EXISTS `suggestionbox`;
DROP TABLE IF EXISTS `supportemailmessage`;

 -- unused table
DROP TABLE IF EXISTS `dbase`;

-- For handling tickets in Garbage/Attic
INSERT INTO `restoresequence` (`moduleid`, `tableid`, `sequence`, `primarytable`) VALUES (10, 52, 2, 52);

-- new table for handling email delegation functionality
DROP TABLE IF EXISTS `emaildelegation`;
CREATE TABLE `emaildelegation` (
  `individualID` int(11) unsigned NOT NULL,
  `delegatorID` int(11) unsigned NOT NULL,
  UNIQUE KEY `delegationID` (`individualID`, `delegatorID`)
) TYPE=InnoDB;

-- Search Functionality for the EmployeeList

update `searchtable` set TableName = 'individual' where  SearchTableID=35;

delete from `searchfield` where SearchTableID=35;
delete from `searchmodule` where ModuleId=54 and SearchTableID=2;

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (35, 'Individual', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.entity = 1');

SELECT @searchFieldId := SearchFieldID FROM `searchfield` WHERE SearchTableID=35;

delete from `simplesearchcriteria` where SearchID=29;

SELECT @searchId := SearchID FROM `simplesearch` WHERE ModuleID=54;

INSERT INTO `simplesearchcriteria` (`SearchCriteriaID`, `SearchID`, `SearchTableID`, `SearchFieldID`, `ConditionID`, `ExpressionType`, `Value`, `CriteriaGroup`) VALUES (264,@searchId,35,@searchFieldId,2,'OR','',1);

-- Activities table was not set as a primary table corrected it
update searchmodule set IsPrimaryTable='Y' where ModuleID=3 and SearchTableID=6;
