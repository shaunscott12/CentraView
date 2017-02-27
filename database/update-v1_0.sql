-- Ryan's fix for Title length of a Project
ALTER TABLE project MODIFY ProjectTitle varchar(255); 

-- Add List for Merge Search Results
INSERT INTO listviews (listtype, viewid, viewname, sortmember, sorttype, searchid, noofrecords, searchtype) VALUES ('Merge', NULL, 'Merge Default', 'Owner', 'A', 0, 101, 'A');
select @ID:=LAST_INSERT_ID();
INSERT INTO viewcolumns (viewid, columnname, columnorder) VALUES (@ID, 'Title', 1);
INSERT INTO viewcolumns (viewid, columnname, columnorder) VALUES (@ID, 'Owner', 2);
INSERT INTO listcolumns (listtype, columnname, columnorder) VALUES ('Merge', 'Title', 1);
INSERT INTO listcolumns (listtype, columnname, columnorder) VALUES ('Merge', 'Owner', 2);
INSERT INTO defaultviews (listtype, viewid) VALUES ('Merge', @ID);
INSERT INTO listtypes (moduleid, typename) VALUES (64, 'Merge');