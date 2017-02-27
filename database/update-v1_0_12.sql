-- Fixes for Item/Inventory hookup
ALTER TABLE `item` ADD `qtyonbackorder` INT(10)  UNSIGNED DEFAULT "0";
ALTER TABLE `item` ADD `qtyonorder` INT(10)  UNSIGNED DEFAULT "0";
ALTER TABLE `item` DROP `alertlevel`;

update module set name = 'Accounting' where moduleid = 12;
update module set name = 'Opportunities' where moduleid = 30;