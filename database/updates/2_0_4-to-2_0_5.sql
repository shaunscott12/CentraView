-- Add a column to the cvfolder table to determine what shows in UI left nav
ALTER TABLE `cvfolder` ADD `leftNav` TINYINT(1) NOT NULL DEFAULT 0;
-- Add a public folder to cvfolder table
-- corresponding real folder must be created as well
INSERT INTO `cvfolder` (`FolderID`, `Name`, `Parent`, `CreatedBy`, `ModifiedBy`, `LocationID`, `owner`, `visibility`, `IsSystem`, `leftNav`) VALUES (NULL, 'Public Folders', 2, 1, 1, 1, 1, 'PUBLIC', 'FALSE', 1);
-- Get the last inserted ID
SET @publicFolderId = LAST_INSERT_ID();
INSERT INTO `publicrecords` (`recordid`,`moduleid`) VALUES (@publicFolderId, 76);