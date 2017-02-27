-- Update the entity table to allow names longer than 40 characters.
ALTER TABLE `entity` MODIFY `name` varchar(255);
