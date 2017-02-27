SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `supportemailaccount`;
CREATE TABLE `supportemailaccount` (
  `SupportEmailAccountID` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `EmailAccountID` INT(11) UNSIGNED DEFAULT NULL,
  PRIMARY KEY  (`SupportEmailAccountID`),
  UNIQUE KEY `SupportEmailAccountID` (`SupportEmailAccountID`),
  INDEX (`EmailAccountID`),
  FOREIGN KEY (`EmailAccountID`) REFERENCES `emailaccount` (`AccountID`) ON UPDATE CASCADE ON DELETE CASCADE
) TYPE=InnoDB;

DROP TABLE IF EXISTS `emailmessagefolder`;
CREATE TABLE `emailmessagefolder` (
  `MessageID` INT(11) UNSIGNED DEFAULT NULL,
  `FolderID` INT(11) UNSIGNED DEFAULT NULL,
  UNIQUE KEY `SearchModuleID` (`MessageID`, `FolderID`),
  INDEX (`MessageID`),
  INDEX (`FolderID`),
  FOREIGN KEY (`MessageID`) REFERENCES `emailmessage` (`MessageID`) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (`FolderID`) REFERENCES `emailfolder` (`FolderID`) ON UPDATE CASCADE ON DELETE CASCADE
) TYPE=InnoDB;

DROP TABLE IF EXISTS `emailrulecriteria`;
CREATE TABLE `emailrulecriteria` (
  `CriteriaID` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `RuleID` INT(11) UNSIGNED DEFAULT NULL,
  `CriteriaValue` VARCHAR(255) DEFAULT NULL,
  `ExpressionType` ENUM('AND','OR') NOT NULL DEFAULT 'AND',
  `FieldID` INT(11) UNSIGNED NOT NULL,
  `ConditionID` INT(11) UNSIGNED NOT NULL,
  PRIMARY KEY  (`CriteriaID`),
  UNIQUE KEY `CriteriaID` (`CriteriaID`),
  INDEX (`RuleID`),
  FOREIGN KEY (`RuleID`) REFERENCES `emailrule` (`ruleid`) ON UPDATE CASCADE ON DELETE CASCADE
) TYPE=InnoDB;

ALTER TABLE `emailrules` ADD `MoveMessageTo` int(11) DEFAULT NULL;
ALTER TABLE `emailrules` ADD `MarkMessageRead` ENUM('NO','YES') NOT NULL DEFAULT 'NO';
ALTER TABLE `emailrules` ADD `DeleteMessage` ENUM('NO','YES') NOT NULL DEFAULT 'NO';

-- Magic happens where old expressions are converted to 
-- new rule architecture.

-- Then get rid of the old field.
-- ALTER TABLE `emailrule` DROP emailrule.rulestatement;

-- End of Magic happens where old expressions are converted to 
-- new rule architecture.

ALTER TABLE `emailmessage` ADD `UID` varchar(22) DEFAULT NULL;
ALTER TABLE `emailmessage` ADD `LocallyDeleted` ENUM('NO','YES') NOT NULL DEFAULT 'NO';
ALTER TABLE `emailmessage` ADD `LocallyModified` ENUM('NO','YES') NOT NULL DEFAULT 'NO';
ALTER TABLE `emailmessage` ADD `MessageRead` ENUM('NO','YES') NOT NULL DEFAULT 'NO';
ALTER TABLE `emailmessage` ADD `MovedToFolder` INT(11) UNSIGNED DEFAULT 0;
ALTER TABLE `emailmessage` ADD `CopiedToFolder` INT(11) UNSIGNED DEFAULT 0;
ALTER TABLE `emailmessage` MODIFY `ReplyTo` varchar(255) DEFAULT NULL;
ALTER TABLE `emailmessage` MODIFY `MailFrom` varchar(255) DEFAULT NULL;

ALTER TABLE `emailaccount` ADD `SecureConnectionIfPossible` ENUM('NO','YES') NOT NULL DEFAULT 'NO';
ALTER TABLE `emailaccount` ADD `ForceSecureConnection` ENUM('NO','YES') NOT NULL DEFAULT 'NO';
ALTER TABLE `emailaccount` ADD `SMTPRequiresAuthentication` ENUM('NO','YES') NOT NULL DEFAULT 'NO';
ALTER TABLE `emailaccount` ADD `POPBeforeSMTP` ENUM('NO','YES') NOT NULL DEFAULT 'NO';

UPDATE `emailmessage` m, `emailstore` s SET m.MessageRead = s.ReadStatus 
WHERE m.MessageID = s.MessageID;

INSERT INTO `emailmessagefolder` (MessageID, FolderID)
SELECT MessageID, EmailFolder FROM `emailstore`;

DROP TABLE IF EXISTS `emailstore`;
DROP TABLE IF EXISTS `emailportlet`;

SET FOREIGN_KEY_CHECKS=1;
