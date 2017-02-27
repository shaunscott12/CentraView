-- Remove the sync item from the additonal menu.
DELETE FROM `additionalmenu` WHERE menuitem_id = 2;

-- Add city field to the search fields for reports, and advanced search
INSERT INTO `searchfield` (searchFieldID, searchTableID, displayName, fieldName, fieldPermissionQuery, isOnTable, RealTableName, RelationshipQuery, RealFieldName, fieldType, IsOnGobalReplaceTable, subRelationshipQuery, isGobalReplaceField) VALUES (NULL, 2, 'City', 'addressCity.city', NULL, 'N', 'addressrelate addressRelateCity, address addressCity', 'individual.individualID = addressRelateCity.Contact AND addressRelateCity.Address = addressCity.AddressID AND addressRelateCity.contactType = 2', 'addressCity.city', 0, 'N', "AND addressRelateCity.IsPrimary = 'YES'", 'Y');
INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`, `IsOnTable`, `RealTableName`, `RelationshipQuery`,`RealFieldName`,`SubRelationshipQuery`) VALUES (1, 'City', 'addressCity.City', 'N', 'addressrelate addressRelateCity, address addressCity', 'entity.entityID = addressRelateCity.Contact AND addressRelateCity.Address = addressCity.AddressID AND addressRelateCity.ContactType = 1','addressCity.City'," AND addressRelateCity.IsPrimary = 'YES'");

