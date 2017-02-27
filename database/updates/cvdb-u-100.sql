-- This is brings the CentraView database to version 100.
INSERT INTO `report` (`ReportId`, `Name`, `Description`, `moduleId`, `reportTypeId`, `CreatedBy`) 
VALUES (117, "Notes By Individual", "Show all the notes for each individual", 15, 1, 1);

-- Add a version table to help track the schema
-- arbitrarily start at 100
DROP TABLE IF EXISTS `version`;
CREATE TABLE `version` (
  `dbVersion` int(11) unsigned NOT NULL,
  PRIMARY KEY  (`dbVersion`)
) TYPE=InnoDB;
INSERT INTO `version` (`dbVersion`) VALUES (100);