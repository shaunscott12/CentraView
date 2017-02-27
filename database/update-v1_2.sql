-- KDM adding preferences for every user relating to charts.
INSERT INTO userpreference (individualid, preference_name, preference_value) SELECT distinct individualid, "showticketcharts", "Yes" FROM userpreference;
INSERT INTO userpreference (individualid, preference_name, preference_value) SELECT distinct individualid, "showsalescharts", "Yes" FROM userpreference;

-- Default ticket sort
UPDATE listviews SET sortmember = 'DateOpened', sorttype = 'A' WHERE listtype = 'Ticket';

-- Add to list to email
ALTER TABLE emailmessage ADD ToList VARCHAR(255) DEFAULT NULL;

-- Clean up Activity list columns
DELETE FROM viewcolumns WHERE viewid = 20 AND columnname = 'Individual';
DELETE FROM viewcolumns WHERE viewid = 20 AND columnname = 'Priority';
DELETE FROM viewcolumns WHERE viewid = 20 AND columnname = 'Created';
DELETE FROM viewcolumns WHERE viewid = 20 AND columnname = 'CreatedBy';
DELETE FROM viewcolumns WHERE viewid = 20 AND columnname = 'Notes';
DELETE FROM viewcolumns WHERE viewid = 8 AND columnname = 'Individual';
DELETE FROM viewcolumns WHERE viewid = 8 AND columnname = 'Priority';
DELETE FROM viewcolumns WHERE viewid = 8 AND columnname = 'Created';
DELETE FROM viewcolumns WHERE viewid = 8 AND columnname = 'CreatedBy';
DELETE FROM viewcolumns WHERE viewid = 8 AND columnname = 'Notes';
DELETE FROM viewcolumns WHERE viewid = 10 AND columnname = 'Individual';
DELETE FROM viewcolumns WHERE viewid = 10 AND columnname = 'Priority';
DELETE FROM viewcolumns WHERE viewid = 10 AND columnname = 'Created';
DELETE FROM viewcolumns WHERE viewid = 10 AND columnname = 'CreatedBy';
DELETE FROM viewcolumns WHERE viewid = 10 AND columnname = 'Notes';
DELETE FROM viewcolumns WHERE viewid = 18 AND columnname = 'Individual';
DELETE FROM viewcolumns WHERE viewid = 18 AND columnname = 'Priority';
DELETE FROM viewcolumns WHERE viewid = 18 AND columnname = 'Created';
DELETE FROM viewcolumns WHERE viewid = 18 AND columnname = 'CreatedBy';
DELETE FROM viewcolumns WHERE viewid = 18 AND columnname = 'Notes';
DELETE FROM viewcolumns WHERE viewid = 22 AND columnname = 'Individual';
DELETE FROM viewcolumns WHERE viewid = 22 AND columnname = 'Priority';
DELETE FROM viewcolumns WHERE viewid = 22 AND columnname = 'Created';
DELETE FROM viewcolumns WHERE viewid = 22 AND columnname = 'CreatedBy';
DELETE FROM viewcolumns WHERE viewid = 22 AND columnname = 'Notes';
DELETE FROM viewcolumns WHERE viewid = 25 AND columnname = 'Individual';
DELETE FROM viewcolumns WHERE viewid = 25 AND columnname = 'Priority';
DELETE FROM viewcolumns WHERE viewid = 25 AND columnname = 'Created';
DELETE FROM viewcolumns WHERE viewid = 25 AND columnname = 'CreatedBy';
DELETE FROM viewcolumns WHERE viewid = 25 AND columnname = 'Notes';

-- Update on the Address Column
ALTER TABLE `address` MODIFY `Street1` varchar(225) default NULL;
ALTER TABLE `address` MODIFY `Street2` varchar(255) default NULL;
ALTER TABLE `address` MODIFY `City` varchar(255) default NULL;
ALTER TABLE `address` MODIFY `Website` varchar(255) default NULL;

-- Removing the Print Template from Additional Menu
DELETE FROM `additionalmenu` WHERE menuitem_id = 5;
UPDATE `additionalmenu` SET menuitem_id=5 WHERE menuitem_id=6;

-- Sorting the Individual List in Ascending Order
UPDATE `listviews` SET sorttype='A' WHERE viewid=3;

-- KDM updating the default data to use the category field correctly.
UPDATE ptdetail SET ptcategoryid = 2 WHERE ptdetailid = 101;
UPDATE ptdetail SET ptcategoryid = 2 WHERE ptdetailid = 102;