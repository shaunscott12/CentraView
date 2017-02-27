-- Update to the Method of Contact architecture
UPDATE `moctype` SET `Name`='Main' WHERE `MOCTypeID`=4;
INSERT INTO `moctype` VALUES (5, 'Home');
INSERT INTO `moctype` VALUES (6, 'Other');
INSERT INTO `moctype` VALUES (7, 'Pager');
INSERT INTO `moctype` VALUES (8, 'Work');


-- Adding the Fields for the GlobalReplace

ALTER TABLE `searchtable` ADD `IsOnGobalReplaceTable` ENUM('N','Y') NOT NULL DEFAULT 'N';
UPDATE `searchtable` SET `IsOnGobalReplaceTable`='Y' where SearchTableID in (1,2);

-- Adding the column for Global Replace to identify it is in which table.

ALTER TABLE `searchfield` ADD `RealFieldName` VARCHAR(255) DEFAULT NULL;
ALTER TABLE `searchfield` ADD `FieldType` INT(11) UNSIGNED NOT NULL DEFAULT '0';
UPDATE `searchfield` SET RealFieldName = FieldName;
UPDATE `searchfield` set RealFieldName='Creator',FieldType='1' where SearchFieldID=5;
UPDATE `searchfield` set RealFieldName='ModifiedBy',FieldType='1' where SearchFieldID=6;
UPDATE `searchfield` set RealFieldName='Owner',FieldType='1' where SearchFieldID=7;
UPDATE `searchfield` set RealFieldName='AccountManagerID',FieldType='4' where SearchFieldID=217;
UPDATE `searchfield` set RealFieldName='AccountTeamID',FieldType='2' where SearchFieldID=218;
UPDATE `searchfield` set RealFieldName='Source',FieldType='3' where SearchFieldID=10;
UPDATE `searchfield` set RealFieldName='CreatedBy',FieldType='1' where SearchFieldID=219;
UPDATE `searchfield` set RealFieldName='ModifiedBy',FieldType='1' where SearchFieldID=220;
UPDATE `searchfield` set RealFieldName='Source',FieldType='3' where SearchFieldID=21;
UPDATE `searchfield` set RealFieldName='Entity',FieldType='7' where SearchFieldID=13;

ALTER TABLE `searchfield` ADD `IsOnGobalReplaceTable` ENUM('N','Y') NOT NULL DEFAULT 'N';
UPDATE `searchfield` SET IsOnGobalReplaceTable = IsOnTable;
UPDATE `searchfield` SET IsOnGobalReplaceTable='Y' where searchFieldID in (7,10,217,218,13,21);

ALTER TABLE `searchfield` ADD `SubRelationshipQuery` varchar(255) DEFAULT NULL;

ALTER TABLE `searchfield` ADD `IsGobalReplaceField` ENUM('N','Y') NOT NULL DEFAULT 'Y';
UPDATE `searchfield` SET IsGobalReplaceField='N' where SearchFieldID in(1,5,6,8,9,11,19,20,219,220,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36);

-- Building the relation on globalreplacerelate to the corresponding table.

DROP TABLE IF EXISTS `globalreplacerelate`;
CREATE TABLE `globalreplacerelate` (
  `RelateTableID` INT(11) UNSIGNED NOT NULL,
  `SearchTableID` INT(11) UNSIGNED NOT NULL,
  `IsRelateTable` ENUM('N','Y') NOT NULL DEFAULT 'Y',
  UNIQUE KEY `SearchRelationshipID` (`RelateTableID`, `SearchTableID`),
  INDEX (`RelateTableID`),
  INDEX (`SearchTableID`),
  FOREIGN KEY (`RelateTableID`) REFERENCES `searchtable` (`SearchTableID`) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (`SearchTableID`) REFERENCES `searchtable` (`SearchTableID`) ON UPDATE CASCADE ON DELETE CASCADE
) TYPE=InnoDB;

INSERT INTO `globalreplacerelate` values(5,1,'N'); 
INSERT INTO `globalreplacerelate` values(5,2,'N'); 

DELETE from `simplesearchcriteria` where  SearchCriteriaID in (5,6,7,8,14,13);

-- Building the relation on gobalreplacevalue to the corresponding table.

DROP TABLE IF EXISTS `gobalreplacevalue`;				
CREATE TABLE `gobalreplacevalue` (
  `tableID` INT(11) UNSIGNED NOT NULL,
  `fieldID` INT(11) UNSIGNED NOT NULL,
  `fieldName` varchar(255) DEFAULT NULL,
  `tableName` varchar(255) DEFAULT NULL,
  UNIQUE KEY `replaceValueID` (`tableID`, `fieldID`),
) TYPE=InnoDB;

update `searchfield` set FieldType = 8 , DisplayName='Marketing List' where RealFieldName='list';

SELECT @marketingListEntityFieldID := SearchFieldID FROM `searchfield` WHERE UPPER(DisplayName) = UPPER('Marketing List') and SearchTableID=1;
INSERT INTO `gobalreplacevalue` VALUES(1 , @marketingListEntityFieldID ,' ListID as ValueID , Title as Value ','marketinglist');

SELECT @marketingListIndividualFieldID := SearchFieldID FROM `searchfield` WHERE UPPER(DisplayName) = UPPER('Marketing List') and SearchTableID=2;
INSERT INTO `gobalreplacevalue` VALUES(2 , @marketingListIndividualFieldID ,' ListID as ValueID , Title as Value ','marketinglist');

-- Deleting the associatiion of the address and method of contacts from the searchmodule table

DELETE FROM `searchmodule` WHERE SearchTableID in (3,4);
delete from searchtablerelate where leftsearchtableid in(3,4) or rightsearchtableid in (3,4);

-- Adding the Entries for address, Method Of contact in the Entity and Individual tables.

SELECT @entityTableID := SearchTableID FROM `searchtable` WHERE
UPPER(TableName) = UPPER('entity');

SELECT @individualTableID := SearchTableID FROM `searchtable` WHERE
UPPER(DisplayName) = UPPER('Individual');

SELECT @orderTableID := SearchTableID FROM `searchtable` WHERE
UPPER(TableName) = UPPER('cvorder');

SELECT @invoiceTableID := SearchTableID FROM `searchtable` WHERE
UPPER(TableName) = UPPER('invoice');

SELECT @purchaseOrderTableID := SearchTableID FROM `searchtable` WHERE
UPPER(TableName) = UPPER('purchaseorder');

SELECT @proposalTableID := SearchTableID FROM `searchtable` WHERE
UPPER(TableName) = UPPER('proposal');

-- Adding the METHOD of contact fields in entity table

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@entityTableID, 'Email', 'mocEmail.Content', 'N', 'mocrelate mocRelateEmail,methodofcontact mocEmail',
'entity.entityID = mocRelateEmail.ContactId AND mocRelateEmail.MOCID = mocEmail.MOCID AND mocRelateEmail.ContactType = 1 AND mocEmail.MOCType = 1','mocEmail.Content'," AND mocRelateEmail.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();
INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(1,@entityTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`,`FieldType`)
VALUES (@entityTableID, 'Fax', 'mocFax.Content', 'N', 'mocrelate mocRelateFax,methodofcontact mocFax',
'entity.entityID = mocRelateFax.ContactId AND mocRelateFax.MOCID = mocFax.MOCID AND mocRelateFax.ContactType = 1 AND mocFax.MOCType = 2','mocFax.Content'," AND mocRelateFax.IsPrimary = 'YES'",6);

set @fieldID = LAST_INSERT_ID();
INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(1,@entityTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`,`FieldType`) VALUES (@entityTableID, 'Mobile',
'mocMobile.Content', 'N', 'mocrelate mocRelateMobile,methodofcontact mocMobile',
'entity.entityID = mocRelateMobile.ContactId AND mocRelateMobile.MOCID = mocMobile.MOCID AND mocRelateMobile.ContactType = 1 AND mocMobile.MOCType = 3','mocMobile.Content'," AND mocRelateMobile.IsPrimary = 'YES'",6);

set @fieldID = LAST_INSERT_ID();
INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(1,@entityTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`,`FieldType`)
VALUES (@entityTableID, 'Main', 'mocMain.Content', 'N', 'mocrelate mocRelateMain,methodofcontact mocMain',
'entity.entityID = mocRelateMain.ContactId AND mocRelateMain.MOCID = mocMain.MOCID AND mocRelateMain.ContactType = 1 AND mocMain.MOCType = 4','mocMain.Content'," AND mocRelateMain.IsPrimary = 'YES'",6);

set @fieldID = LAST_INSERT_ID();
INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(1,@entityTableID,@fieldID,2,'OR','',1);


INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`,`FieldType`)
VALUES (@entityTableID, 'Home', 'mocHome.Content', 'N', 'mocrelate mocRelateHome,methodofcontact mocHome',
'entity.entityID = mocRelateHome.ContactId AND mocRelateHome.MOCID = mocHome.MOCID AND mocRelateHome.ContactType = 1 AND mocHome.MOCType = 5','mocHome.Content'," AND mocRelateHome.IsPrimary = 'YES'",6);

set @fieldID = LAST_INSERT_ID();
INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(1,@entityTableID,@fieldID,2,'OR','',1);


INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`,`FieldType`)
VALUES (@entityTableID, 'Other', 'mocOther.Content', 'N', 'mocrelate mocRelateOther,methodofcontact mocOther',
'entity.entityID = mocRelateOther.ContactId AND mocRelateOther.MOCID = mocOther.MOCID AND mocRelateOther.ContactType = 1 AND mocOther.MOCType = 6','mocOther.Content'," AND mocRelateOther.IsPrimary = 'YES'",6);

set @fieldID = LAST_INSERT_ID();
INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(1,@entityTableID,@fieldID,2,'OR','',1);


INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`,`FieldType`)
VALUES (@entityTableID, 'Pager', 'mocPager.Content', 'N', 'mocrelate mocRelatePager,methodofcontact mocPager',
'entity.entityID = mocRelatePager.ContactId AND mocRelatePager.MOCID = mocPager.MOCID AND mocRelatePager.ContactType = 1 AND mocPager.MOCType = 7','mocPager.Content'," AND mocRelatePager.IsPrimary = 'YES'",6);

set @fieldID = LAST_INSERT_ID();
INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(1,@entityTableID,@fieldID,2,'OR','',1);


INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`,`FieldType`)
VALUES (@entityTableID, 'Work', 'mocWork.Content', 'N', 'mocrelate mocRelateWork,methodofcontact mocWork',
'entity.entityID = mocRelateWork.ContactId AND mocRelateWork.MOCID = mocWork.MOCID AND mocRelateWork.ContactType = 1 AND mocWork.MOCType = 8','mocWork.Content'," AND mocRelateWork.IsPrimary = 'YES'",6);

set @fieldID = LAST_INSERT_ID();
INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(1,@entityTableID,@fieldID,2,'OR','',1);


-- Adding the METHOD of contact fields in individual table

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@individualTableID, 'Email', 'mocEmail.Content', 'N', 'mocrelate mocRelateEmail,methodofcontact mocEmail',
'individual.IndividualID = mocRelateEmail.ContactId AND mocRelateEmail.MOCID = mocEmail.MOCID AND mocRelateEmail.ContactType = 2 AND mocEmail.MOCType = 1','mocEmail.Content'," AND mocRelateEmail.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(2,@individualTableID,@fieldID,2,'OR','',1);



INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`,`FieldType`)
VALUES (@individualTableID, 'Fax', 'mocFax.Content', 'N', 'mocrelate mocRelateFax,methodofcontact mocFax',
'individual.IndividualID = mocRelateFax.ContactId AND mocRelateFax.MOCID = mocFax.MOCID AND mocRelateFax.ContactType = 2 AND mocFax.MOCType = 2','mocFax.Content'," AND mocRelateFax.IsPrimary = 'YES'",6);

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(2,@individualTableID,@fieldID,2,'OR','',1);


INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`,`FieldType`) VALUES (@individualTableID, 'Mobile',
'mocMobile.Content', 'N', 'mocrelate mocRelateMobile,methodofcontact mocMobile',
'individual.IndividualID = mocRelateMobile.ContactId AND mocRelateMobile.MOCID = mocMobile.MOCID AND mocRelateMobile.ContactType = 2 AND mocMobile.MOCType = 3','mocMobile.Content'," AND mocRelateMobile.IsPrimary = 'YES'",6);

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(2,@individualTableID,@fieldID,2,'OR','',1);


INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`,`FieldType`)
VALUES (@individualTableID, 'Main', 'mocMain.Content', 'N', 'mocrelate mocRelateMain,methodofcontact mocMain',
'individual.IndividualID = mocRelateMain.ContactId AND mocRelateMain.MOCID = mocMain.MOCID AND mocRelateMain.ContactType = 2 AND mocMain.MOCType = 4','mocMain.Content'," AND mocRelateMain.IsPrimary = 'YES'",6);

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(2,@individualTableID,@fieldID,2,'OR','',1);


INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`,`FieldType`)
VALUES (@individualTableID, 'Home', 'mocHome.Content', 'N', 'mocrelate mocRelateHome,methodofcontact mocHome',
'individual.IndividualID = mocRelateHome.ContactId AND mocRelateHome.MOCID = mocHome.MOCID AND mocRelateHome.ContactType = 2 AND mocHome.MOCType = 5','mocHome.Content'," AND mocRelateHome.IsPrimary = 'YES'",6);

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(2,@individualTableID,@fieldID,2,'OR','',1);


INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`,`FieldType`)
VALUES (@individualTableID, 'Other', 'mocOther.Content', 'N', 'mocrelate mocRelateOther,methodofcontact mocOther',
'individual.IndividualID = mocRelateOther.ContactId AND mocRelateOther.MOCID = mocOther.MOCID AND mocRelateOther.ContactType = 2 AND mocOther.MOCType = 6','mocOther.Content'," AND mocRelateOther.IsPrimary = 'YES'",6);

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(2,@individualTableID,@fieldID,2,'OR','',1);


INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`,`FieldType`)
VALUES (@individualTableID, 'Pager', 'mocPager.Content', 'N', 'mocrelate mocRelatePager,methodofcontact mocPager',
'individual.IndividualID = mocRelatePager.ContactId AND mocRelatePager.MOCID = mocPager.MOCID AND mocRelatePager.ContactType = 2 AND mocPager.MOCType = 7','mocPager.Content'," AND mocRelatePager.IsPrimary = 'YES'",6);

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(2,@individualTableID,@fieldID,2,'OR','',1);


INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`,`FieldType`)
VALUES (@individualTableID, 'Work', 'mocWork.Content', 'N', 'mocrelate mocRelateWork,methodofcontact mocWork',
'individual.IndividualID = mocRelateWork.ContactId AND mocRelateWork.MOCID = mocWork.MOCID AND mocRelateWork.ContactType = 2 AND mocWork.MOCType = 8','mocWork.Content'," AND mocRelateWork.IsPrimary = 'YES'",6);

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(2,@individualTableID,@fieldID,2,'OR','',1);

-- Adding the address fields in entity table

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@entityTableID, 'Street 1', 'addressStreet1.Street1', 'N', 'addressrelate addressRelateStreet1,address addressStreet1',
'entity.entityID = addressRelateStreet1.Contact AND addressRelateStreet1.Address = addressStreet1.AddressID AND addressRelateStreet1.ContactType = 1'
,'addressStreet1.Street1'," AND addressRelateStreet1.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(1,@entityTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@entityTableID, 'Street 2', 'addressStreet2.Street2', 'N', 'addressrelate addressRelateStreet2,address addressStreet2',
'entity.entityID = addressRelateStreet2.Contact AND addressRelateStreet2.Address = addressStreet2.AddressID AND addressRelateStreet2.ContactType = 1'
,'addressStreet2.Street2'," AND addressRelateStreet2.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(1,@entityTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`) 
VALUES (@entityTableID, 'State', 'addressState.state', 'N', 'addressrelate addressRelateState,address addressState',
'entity.entityID = addressRelateState.Contact AND addressRelateState.Address = addressState.AddressID AND addressRelateState.ContactType = 1'
,'addressState.state'," AND addressRelateState.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(1,@entityTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@entityTableID, 'Zip Code', 'addressZip.Zip', 'N', 'addressrelate addressRelateZip,address addressZip',
'entity.entityID = addressRelateZip.Contact AND addressRelateZip.Address = addressZip.AddressID AND addressRelateZip.ContactType = 1'
,'addressZip.Zip'," AND addressRelateZip.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(1,@entityTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@entityTableID, 'Country', 'addressCountry.country', 'N', 'addressrelate addressRelateCountry,address addressCountry',
'entity.entityID = addressRelateCountry.Contact AND addressRelateCountry.Address = addressCountry.AddressID AND addressRelateCountry.ContactType = 1'
,'addressCountry.country'," AND addressRelateCountry.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(1,@entityTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@entityTableID, 'Website', 'addressWebsite.Website', 'N', 'addressrelate addressRelateWebsite,address addressWebsite',
'entity.entityID = addressRelateWebsite.Contact AND addressRelateWebsite.Address = addressWebsite.AddressID AND addressRelateWebsite.ContactType = 1'
,'addressWebsite.Website'," AND addressRelateWebsite.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(1,@entityTableID,@fieldID,2,'OR','',1);

-- Adding the address fields in individual table

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@individualTableID, 'Street 1', 'addressStreet1.Street1', 'N', 'addressrelate addressRelateStreet1,address addressStreet1',
'individual.IndividualID  = addressRelateStreet1.Contact AND addressRelateStreet1.Address = addressStreet1.AddressID AND addressRelateStreet1.ContactType = 2'
,'addressStreet1.Street1'," AND addressRelateStreet1.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(2,@individualTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@individualTableID, 'Street 2', 'addressStreet2.Street2', 'N', 'addressrelate addressRelateStreet2,address addressStreet2',
'individual.IndividualID  = addressRelateStreet2.Contact AND addressRelateStreet2.Address = addressStreet2.AddressID AND addressRelateStreet2.ContactType = 2'
,'addressStreet2.Street2'," AND addressRelateStreet2.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(2,@individualTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`) 
VALUES (@individualTableID, 'State', 'addressState.state', 'N', 'addressrelate addressRelateState,address addressState',
'individual.IndividualID  = addressRelateState.Contact AND addressRelateState.Address = addressState.AddressID AND addressRelateState.ContactType = 2'
,'addressState.state'," AND addressRelateState.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(2,@individualTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@individualTableID, 'Zip Code', 'addressZip.Zip', 'N', 'addressrelate addressRelateZip,address addressZip',
'individual.IndividualID  = addressRelateZip.Contact AND addressRelateZip.Address = addressZip.AddressID AND addressRelateZip.ContactType = 2'
,'addressZip.Zip'," AND addressRelateZip.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(2,@individualTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@individualTableID, 'Country', 'addressCountry.country', 'N', 'addressrelate addressRelateCountry,address addressCountry',
'individual.IndividualID  = addressRelateCountry.Contact AND addressRelateCountry.Address = addressCountry.AddressID AND addressRelateCountry.ContactType = 2'
,'addressCountry.country'," AND addressRelateCountry.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(2,@individualTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@individualTableID, 'Website', 'addressWebsite.Website', 'N', 'addressrelate addressRelateWebsite,address addressWebsite',
'individual.IndividualID  = addressRelateWebsite.Contact AND addressRelateWebsite.Address = addressWebsite.AddressID AND addressRelateWebsite.ContactType = 2'
,'addressWebsite.Website'," AND addressRelateWebsite.IsPrimary = 'YES'");


set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(2,@individualTableID,@fieldID,2,'OR','',1);

-- Adding the address fields in Order table

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@orderTableID, 'Street 1', 'addressStreet1.Street1', 'N', 'address addressStreet1',
'(addressStreet1.AddressID = cvorder.BillAddress OR addressStreet1.AddressID = cvorder.ShipAddress)'
,'addressStreet1.Street1'," AND addressRelateStreet1.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(19,@orderTableID,@fieldID,2,'OR','',1);


INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@orderTableID, 'Street 2', 'addressStreet2.Street2', 'N', 'address addressStreet2',
'(addressStreet2.AddressID = cvorder.BillAddress OR addressStreet2.AddressID = cvorder.ShipAddress)'
,'addressStreet2.Street2'," AND addressRelateStreet2.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(19,@orderTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`) 
VALUES (@orderTableID, 'State', 'addressState.state', 'N', 'address addressState',
'(addressState.AddressID = cvorder.BillAddress OR addressState.AddressID = cvorder.ShipAddress)'
,'addressState.state'," AND addressRelateState.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(19,@orderTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@orderTableID, 'Zip Code', 'addressZip.Zip', 'N', 'address addressZip',
'(addressZip.AddressID = cvorder.BillAddress OR addressZip.AddressID = cvorder.ShipAddress)'
,'addressZip.Zip'," AND addressRelateZip.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(19,@orderTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@orderTableID, 'Country', 'addressCountry.country', 'N', 'address addressCountry',
'(addressCountry.AddressID = cvorder.BillAddress OR addressCountry.AddressID = cvorder.ShipAddress)'
,'addressCountry.country'," AND addressRelateCountry.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(19,@orderTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@orderTableID, 'Website', 'addressWebsite.Website', 'N', 'address addressWebsite',
'(addressWebsite.AddressID = cvorder.BillAddress OR addressWebsite.AddressID = cvorder.ShipAddress)'
,'addressWebsite.Website'," AND addressRelateWebsite.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(19,@orderTableID,@fieldID,2,'OR','',1);

-- Adding the address fields in Invoice table

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@invoiceTableID, 'Street 1', 'addressStreet1.Street1', 'N', 'address addressStreet1',
'(addressStreet1.AddressID = invoice.billaddress OR addressStreet1.AddressID = invoice.shipaddress)'
,'addressStreet1.Street1'," AND addressRelateStreet1.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(20,@invoiceTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@invoiceTableID, 'Street 2', 'addressStreet2.Street2', 'N', 'address addressStreet2',
'(addressStreet2.AddressID = invoice.billaddress OR addressStreet2.AddressID = invoice.shipaddress)'
,'addressStreet2.Street2'," AND addressRelateStreet2.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(20,@invoiceTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`) 
VALUES (@invoiceTableID, 'State', 'addressState.state', 'N', 'address addressState',
'(addressState.AddressID = invoice.billaddress OR addressState.AddressID = invoice.shipaddress)'
,'addressState.state'," AND addressRelateState.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(20,@invoiceTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@invoiceTableID, 'Zip Code', 'addressZip.Zip', 'N', 'address addressZip',
'(addressZip.AddressID = invoice.billaddress OR addressZip.AddressID = invoice.shipaddress)'
,'addressZip.Zip'," AND addressRelateZip.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(20,@invoiceTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@invoiceTableID, 'Country', 'addressCountry.country', 'N', 'address addressCountry',
'(addressCountry.AddressID = invoice.billaddress OR addressCountry.AddressID = invoice.shipaddress)'
,'addressCountry.country'," AND addressRelateCountry.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(20,@invoiceTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@invoiceTableID, 'Website', 'addressWebsite.Website', 'N', 'address addressWebsite',
'(addressWebsite.AddressID = invoice.billaddress OR addressWebsite.AddressID = invoice.shipaddress)'
,'addressWebsite.Website'," AND addressRelateWebsite.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(20,@invoiceTableID,@fieldID,2,'OR','',1);

-- Adding the address fields in Purchase Order table

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@purchaseOrderTableID, 'Street 1', 'addressStreet1.Street1', 'N', 'address addressStreet1',
'(addressStreet1.AddressID = purchaseorder.BillAddress OR addressStreet1.AddressID = purchaseorder.ShipAddress)'
,'addressStreet1.Street1'," AND addressRelateStreet1.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(23,@purchaseOrderTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@purchaseOrderTableID, 'Street 2', 'addressStreet2.Street2', 'N', 'address addressStreet2',
'(addressStreet2.AddressID = purchaseorder.BillAddress OR addressStreet2.AddressID = purchaseorder.ShipAddress)'
,'addressStreet2.Street2'," AND addressRelateStreet2.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(23,@purchaseOrderTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`) 
VALUES (@purchaseOrderTableID, 'State', 'addressState.state', 'N', 'address addressState',
'(addressState.AddressID = purchaseorder.BillAddress OR addressState.AddressID = purchaseorder.ShipAddress)'
,'addressState.state'," AND addressRelateState.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(23,@purchaseOrderTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@purchaseOrderTableID, 'Zip Code', 'addressZip.Zip', 'N', 'address addressZip',
'(addressZip.AddressID = purchaseorder.BillAddress OR addressZip.AddressID = purchaseorder.ShipAddress)'
,'addressZip.Zip'," AND addressRelateZip.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(23,@purchaseOrderTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@purchaseOrderTableID, 'Country', 'addressCountry.country', 'N', 'address addressCountry',
'(addressCountry.AddressID = purchaseorder.BillAddress OR addressCountry.AddressID = purchaseorder.ShipAddress)'
,'addressCountry.country'," AND addressRelateCountry.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(23,@purchaseOrderTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@purchaseOrderTableID, 'Website', 'addressWebsite.Website', 'N', 'address addressWebsite',
'(addressWebsite.AddressID = purchaseorder.BillAddress OR addressWebsite.AddressID = purchaseorder.ShipAddress)'
,'addressWebsite.Website'," AND addressRelateWebsite.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(23,@purchaseOrderTableID,@fieldID,2,'OR','',1);

-- Adding the address fields in Proposal table

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@proposalTableID, 'Street 1', 'addressStreet1.Street1', 'N', 'address addressStreet1',
'(addressStreet1.AddressID = proposal.Billingid OR addressStreet1.AddressID = proposal.Shippingid)'
,'addressStreet1.Street1'," AND addressRelateStreet1.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(8,@proposalTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@proposalTableID, 'Street 2', 'addressStreet2.Street2', 'N', 'address addressStreet2',
'(addressStreet2.AddressID = proposal.Billingid OR addressStreet2.AddressID = proposal.Shippingid)'
,'addressStreet2.Street2'," AND addressRelateStreet2.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(8,@proposalTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`) 
VALUES (@proposalTableID, 'State', 'addressState.state', 'N', 'address addressState',
'(addressState.AddressID = proposal.Billingid OR addressState.AddressID = proposal.Shippingid)'
,'addressState.state'," AND addressRelateState.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(8,@proposalTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@proposalTableID, 'Zip Code', 'addressZip.Zip', 'N', 'address addressZip',
'(addressZip.AddressID = proposal.Billingid OR addressZip.AddressID = proposal.Shippingid)'
,'addressZip.Zip'," AND addressRelateZip.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(8,@proposalTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@proposalTableID, 'Country', 'addressCountry.country', 'N', 'address addressCountry',
'(addressCountry.AddressID = proposal.Billingid OR addressCountry.AddressID = proposal.Shippingid)'
,'addressCountry.country'," AND addressRelateCountry.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(8,@proposalTableID,@fieldID,2,'OR','',1);

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`)
VALUES (@proposalTableID, 'Website', 'addressWebsite.Website', 'N', 'address addressWebsite',
'(addressWebsite.AddressID = proposal.Billingid OR addressWebsite.AddressID = proposal.Shippingid)'
,'addressWebsite.Website'," AND addressRelateWebsite.IsPrimary = 'YES'");

set @fieldID = LAST_INSERT_ID();

INSERT INTO `simplesearchcriteria` (SearchID,SearchTableID,SearchFieldID,ConditionID,ExpressionType,Value,CriteriaGroup) 
values(8,@proposalTableID,@fieldID,2,'OR','',1);

-- Removing the Column Reference, Notes from the History List

delete from viewcolumns  where viewid=182 and columnname='Reference';
delete from viewcolumns  where viewid=182 and columnname='Notes';
delete from listcolumns  where listtype='History' and columnname='Notes';
delete from listcolumns  where listtype='History' and columnname='Reference';

-- Change knowledgebase title column from 25 to 255 characters

ALTER TABLE knowledgebase MODIFY title varchar(255) default NULL;

-- Stuff for the new ValueList framework
-- The following list contains a fieldDescriptor for a List.
DROP TABLE IF EXISTS `valuelist`;
CREATE TABLE `valuelist` (
  `valueListId` INT(11) UNSIGNED NOT NULL auto_increment,
  `valueListName` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`valueListId`)
) TYPE=InnoDB;

INSERT INTO `valuelist` (`valueListName`) VALUES ('Entity');
INSERT INTO `valuelist` (`valueListName`) VALUES ('Individual');

DROP TABLE IF EXISTS `valuelistfield`;
CREATE TABLE `valuelistfield` (
  `valueListFieldId` INT(11) UNSIGNED NOT NULL auto_increment,
  `valueListId` INT(11) UNSIGNED NOT NULL,
  `fieldName` VARCHAR(255) NOT NULL,
  `queryIndex` INT(11) UNSIGNED NOT NULL,
  `customField` ENUM('N', 'Y') NOT NULL DEFAULT 'N',
  `externalKey` VARCHAR(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`valueListFieldId`),
  INDEX (`valueListId`),
  FOREIGN KEY (`valueListId`) REFERENCES `valuelist` (`valueListId`) ON UPDATE CASCADE ON DELETE CASCADE
) TYPE=InnoDB;

INSERT INTO `valuelistfield` (`valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`)
VALUES (1, 'Entity ID', 1, 'N', 'entity.entityId');
INSERT INTO `valuelistfield` (`valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`)
VALUES (1, 'Entity Name', 2, 'N', 'entity.entityName');
INSERT INTO `valuelistfield` (`valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`)
VALUES (1, 'Marketing List', 3, 'N', 'entity.marketingList');
INSERT INTO `valuelistfield` (`valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`)
VALUES (1, 'Account Manager ID', 4, 'N', 'entity.accountManagerId');
INSERT INTO `valuelistfield` (`valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`)
VALUES (1, 'Account Manager', 5, 'N', 'entity.accountManagerName');
INSERT INTO `valuelistfield` (`valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`)
VALUES (1, 'Primary Contact ID', 6, 'N', 'entity.primaryContactId');
INSERT INTO `valuelistfield` (`valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`)
VALUES (1, 'Primary Contact', 7, 'N', 'entity.primaryContact');
INSERT INTO `valuelistfield` (`valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`)
VALUES (1, 'Phone', 8, 'N', 'entity.phone');
INSERT INTO `valuelistfield` (`valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`)
VALUES (1, 'Email', 9, 'N', 'entity.email');
INSERT INTO `valuelistfield` (`valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`)
VALUES (1, 'Website', 10, 'N', 'entity.website');
INSERT INTO `valuelistfield` (`valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`)
VALUES (1, 'Street 1', 11, 'N', 'entity.street1');
INSERT INTO `valuelistfield` (`valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`)
VALUES (1, 'Street 2', 12, 'N', 'entity.street2');
INSERT INTO `valuelistfield` (`valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`)
VALUES (1, 'City', 13, 'N', 'entity.city');
INSERT INTO `valuelistfield` (`valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`)
VALUES (1, 'State', 14, 'N', 'entity.state');
INSERT INTO `valuelistfield` (`valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`)
VALUES (1, 'Zipcode', 15, 'N', 'entity.zipcode');
INSERT INTO `valuelistfield` (`valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`)
VALUES (1, 'Country', 16, 'N', 'entity.country');
INSERT INTO `valuelistfield` (`valueListId`, `fieldName`, `queryIndex`, `customField`, `externalKey`)
VALUES (1, 'Address', 17, 'N', 'entity.address');

DROP TABLE IF EXISTS `listview`;
CREATE TABLE `listview` (
  `listViewId` INT(11) UNSIGNED NOT NULL auto_increment,
  `valueListId` INT(11) UNSIGNED NOT NULL,
  `recordsPerPage` SMALLINT(3) UNSIGNED NOT NULL DEFAULT 100,
  `sortColumn` INT(11) UNSIGNED NOT NULL,
  `sortDirection` ENUM('ASC', 'DESC') NOT NULL DEFAULT 'ASC',
  `owner` INT(11) UNSIGNED NOT NULL,
  `modified` TIMESTAMP(14) NOT NULL,
  PRIMARY KEY (`listViewId`),
  INDEX (`valueListId`),
  INDEX (`owner`),
  INDEX (`sortColumn`),
  FOREIGN KEY (`valueListId`) REFERENCES `valuelist` (`valueListId`) ON DELETE CASCADE,
  FOREIGN KEY (`sortColumn`) REFERENCES `valuelistfield` (`valueListFieldId`) ON DELETE CASCADE,
  FOREIGN KEY (`owner`) REFERENCES `individual` (`individualId`) ON DELETE CASCADE
) TYPE=InnoDB;
-- show the old email attachment in the UI 

UPDATE `cvfile` set Owner = Author where Description like 'Email Attachment:%';

-- Move primary address functionality to the addressrelate table
alter table address drop isPrimary;

--
-- Table structure for table 'reportcontenttemp'
--

CREATE TEMPORARY TABLE `reportcontenttemp` select * from reportcontent;

--
-- Table structure for table 'reportcontent'
--

DROP TABLE IF EXISTS `reportcontent`;
CREATE TABLE `reportcontent` (
  `ReportContentId` int(10) unsigned NOT NULL auto_increment,
  `ReportId` int(10) unsigned NOT NULL default '0',
  `FieldId` int(11) unsigned NOT NULL default '0',
  `SequenceNumber` tinyint(4) NOT NULL default '0',
  `SortOrder` enum('ASC','DESC') default NULL,
  `SortOrderSequence` tinyint(4) default NULL,
  `tableId` int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (`ReportContentId`),
  KEY `ReportId` (`ReportId`),
  KEY `FieldId` (`FieldId`),
  CONSTRAINT `reportcontentFieldId` FOREIGN KEY (`FieldId`) REFERENCES `searchfield` (`SearchFieldID`),
  CONSTRAINT `reportcontentReportId` FOREIGN KEY (`ReportId`) REFERENCES `report` (`ReportId`) ON DELETE CASCADE
) TYPE=InnoDB;

INSERT INTO `reportcontent` SELECT * FROM reportcontenttemp;

--
-- Remove Table structure for table 'reportcontenttemp'
--

DROP TABLE IF EXISTS `reportcontenttemp`;

DROP TABLE IF EXISTS `reportmodule`;
CREATE TABLE `reportmodule` (
	`ModuleID` int(10) unsigned NOT NULL default '0',
	`SearchTableID` int(11) unsigned NOT NULL default '0',
	UNIQUE KEY `SearchModuleID` (`ModuleID`,`SearchTableID`),
	KEY `ModuleID` (`ModuleID`),
	KEY `SearchTableID` (`SearchTableID`),
	CONSTRAINT `reportModuleID` FOREIGN KEY (`ModuleID`) REFERENCES `module` (`moduleid`) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `reportSearchTableID` FOREIGN KEY (`SearchTableID`) REFERENCES `searchtable` (`SearchTableID`) ON DELETE CASCADE ON UPDATE CASCADE
)TYPE=InnoDB;

-- Entity
INSERT INTO `reportmodule` values(14,1);
INSERT INTO `reportmodule` values(14,5);

-- Individual
INSERT INTO `reportmodule` values(15,2);
INSERT INTO `reportmodule` values(15,5);

-- Activities
INSERT INTO `reportmodule` values(3,6);

-- Sales 
-- Opportunity
INSERT INTO `reportmodule` values(30,10);
-- Proposal
INSERT INTO `reportmodule` values(31,11);
-- Marketing
--Promotion
INSERT INTO `reportmodule` values(33,21);


-- Projects
--Project
INSERT INTO `reportmodule` values(36,12);
--Task
INSERT INTO `reportmodule` values(37,13);
--TimeSlip
INSERT INTO `reportmodule` values(38,15);

-- Support
--Ticket
INSERT INTO `reportmodule` values(39,14);
INSERT INTO `reportmodule` values(39,5);	

-- Accounting
--Order
INSERT INTO `reportmodule` values(42,18);
--Inventory
INSERT INTO `reportmodule` values(48,29);
-- HR
-- TimeSheet
INSERT INTO `reportmodule` values(52,36);

-- Set the individual list to default sort ascending.
UPDATE `listviews` SET `searchType`='A' WHERE `viewId`=3;