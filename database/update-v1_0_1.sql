-- Naresh's fix for Market List Listing view
delete from listcolumns where listtype='Marketing';

INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Marketing','title',1);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Marketing','description',2);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Marketing','OrderValue',3);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Marketing','numberofrecords',4);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Marketing','Opportunities',5);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Marketing','OpportunityValue',6);
INSERT INTO `listcolumns` (`listtype`, `columnname`, `columnorder`) VALUES ('Marketing','Order',7);

delete from viewcolumns where viewid=75;
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (75,'title',1);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (75,'description',2);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (75,'OrderValue',3);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (75,'numberofrecords',4);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (75,'Opportunities',5);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (75,'OpportunityValue',6);
INSERT INTO `viewcolumns` (`viewid`, `columnname`, `columnorder`) VALUES (75,'Order',7);

-- Ryan's fix for the Zero Records
DELETE FROM state WHERE StateId = 0;
DELETE FROM country WHERE CountryId = 0; 
DELETE FROM salesstatus WHERE SalesStatusID = 0;
DELETE FROM source WHERE sourceID = 0; 

-- Mike's fix for Activity Title max length
ALTER TABLE activity MODIFY Title varchar(255);
