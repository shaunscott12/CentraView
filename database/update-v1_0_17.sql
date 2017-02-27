-- Adding the List View For the Related Proposal

SET @listType = 'BottomProposal';
SET @newModuleID = 31;

INSERT INTO `listviews` (`listtype`, `viewname`, `ownerid`, `creatorid`, `sortmember`, `sorttype`, `searchid`, `noofrecords`, `searchtype`) VALUES (@listType,'Default View',NULL,NULL,'Title','A',0,100,'D');

-- Get the last inserted ID
SET @newViewID = LAST_INSERT_ID();

INSERT INTO listtypes (typename, moduleid) VALUES (@listType, @newModuleID);

INSERT INTO defaultviews (listtype, viewid) VALUES (@listType, @newViewID);

-- Insert the viewable columns.
INSERT INTO listcolumns (listtype, columnname, columnorder) VALUES (@listType, 'ProposalID', 1);
INSERT INTO listcolumns (listtype, columnname, columnorder) VALUES (@listType, 'Title', 2);
INSERT INTO listcolumns (listtype, columnname, columnorder) VALUES (@listType, 'Description', 3);
INSERT INTO listcolumns (listtype, columnname, columnorder) VALUES (@listType, 'Type', 4);
INSERT INTO listcolumns (listtype, columnname, columnorder) VALUES (@listType, 'Stage', 5);
INSERT INTO listcolumns (listtype, columnname, columnorder) VALUES (@listType, 'Probability', 6);
INSERT INTO listcolumns (listtype, columnname, columnorder) VALUES (@listType, 'Status', 7);
INSERT INTO listcolumns (listtype, columnname, columnorder) VALUES (@listType, 'EstimatedCloseDate', 8);
INSERT INTO listcolumns (listtype, columnname, columnorder) VALUES (@listType, 'ForecastAmmount', 9);

-- Insert the viewable columns.
INSERT INTO viewcolumns (viewid, columnname, columnorder) VALUES (@newViewID, 'ProposalID', 1);
INSERT INTO viewcolumns (viewid, columnname, columnorder) VALUES (@newViewID, 'Title', 2);
INSERT INTO viewcolumns (viewid, columnname, columnorder) VALUES (@newViewID, 'Description', 3);
INSERT INTO viewcolumns (viewid, columnname, columnorder) VALUES (@newViewID, 'Type', 4);
INSERT INTO viewcolumns (viewid, columnname, columnorder) VALUES (@newViewID, 'Stage', 5);
INSERT INTO viewcolumns (viewid, columnname, columnorder) VALUES (@newViewID, 'Probability', 6);
INSERT INTO viewcolumns (viewid, columnname, columnorder) VALUES (@newViewID, 'Status', 7);
INSERT INTO viewcolumns (viewid, columnname, columnorder) VALUES (@newViewID, 'EstimatedCloseDate', 8);
INSERT INTO viewcolumns (viewid, columnname, columnorder) VALUES (@newViewID, 'ForecastAmmount', 9);

-- User Information Change Template
insert into emailtemplate values(9,'Change Request for User','Auto-response Template for User Information change request.','','','','', '','YES','YES','YES','YES','YES');
update emailtemplate set requiredBody='YES',body='' where templateID=8;

