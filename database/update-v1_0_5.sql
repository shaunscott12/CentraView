-- query to provide a relationship on the internal advanced search table, between
-- entity and individual.
SET @entityTableID = 1;
SET @individualTableID = 2;
SET @customFieldTableID = 5;

INSERT INTO `searchtablerelate` (`LeftSearchTableID`, `RightSearchTableID`, `RelationshipQuery`) VALUES(@individualTableID, @entityTableID, 'individual.Entity = entity.entityID');
DELETE FROM `searchtablerelate` WHERE `leftsearchtableid` = @customFieldTableID OR `rightsearchtableid` = @customFieldTableID;

-- Saving Terms on a proposal.
ALTER TABLE `proposal` ADD `termid` int(11) unsigned default NULL;

-- proposal individual and owner decoupling
ALTER TABLE `proposal` ADD `individualid` int(11) unsigned default NULL;
UPDATE `proposal` SET `individualid`=`owner`;