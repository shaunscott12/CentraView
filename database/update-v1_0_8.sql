-- Update some search stuff.
-- So the user can search activities.

set @activityTableID = 6;

SELECT @activityModuleId := moduleid FROM module WHERE
UPPER(name) = UPPER('activities');
                                                                                                                                                              
UPDATE `searchmodule` SET `IsPrimaryTable` = 'Y' WHERE `ModuleID` = @activityModuleId
AND `SearchTableID` =  @activityTableID;

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@activityTableID, 'Attendee Name', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.IndividualID = attendee.individualid AND attendee.activityId = activity.activityId');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@activityTableID, 'Entity Name', 'entity.name', 'N', 'entity',
'entity.entityId = activitylink.recordId AND activitylink.recordTypeId = 1 AND activitylink.activityId = activity.activityId');

INSERT INTO `searchfield` (`SearchTableID`, `DisplayName`, `FieldName`,
`IsOnTable`, `RealTableName`, `RelationshipQuery`)
VALUES (@activityTableID, 'Individual Name', 'CONCAT(individual.FirstName, " ", individual.LastName)', 'N', 'individual',
'individual.individualId = activitylink.recordId AND activitylink.recordTypeId = 2 AND activitylink.activityId = activity.activityId');