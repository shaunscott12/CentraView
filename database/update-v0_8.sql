-- Changes between version 0.7 and 0.8

-- Naresh's fix for setting existing user's default email accounts in preference
UPDATE `userpreference` up, `emailaccount` ea SET up.`preference_value`=ea.`accountid`
WHERE up.`individualid`=ea.`owner` AND up.`moduleid`=1 AND up.`preference_name`='emailaccountid';


-- Naresh's fix for promotions
UPDATE `module` SET `primarytable`='promotion', `ownerfield`='owner', `primarykeyfield`='PromotionID' WHERE `moduleid`=33;
DELETE FROM `module` WHERE `moduleid`=74;
