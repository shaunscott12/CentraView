-- Update the item.title field to have maxlength of 255
ALTER TABLE `item` MODIFY `title` varchar(255);
ALTER TABLE `item` MODIFY `sku` varchar(255);

UPDATE `listviews` SET `sortmember`='Received' WHERE `listtype`='Email';

UPDATE userpreference up, cvfolder cvf, user u
   SET up.preference_value=cvf.folderid
 WHERE u.individualid=cvf.owner AND
       u.individualid=up.individualid AND
       cvf.name=u.name AND
       up.moduleid=5 AND
       up.preference_name='DEFAULTFOLDERID';


