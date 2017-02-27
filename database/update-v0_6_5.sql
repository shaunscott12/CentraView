-- SQL update script to update installations from version 0.6 to 0.6.5

-- Naresh's Events module update
UPDATE module SET primarytable='event', ownerfield='owner', primarykeyfield='EventId' WHERE name='Events';


