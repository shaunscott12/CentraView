-- Changes between version 0.6.5 and 0.7

-- Naresh's changes to the default proposal template
update ptdetail set ptdata=
'<html>
<head>
  <title>Untitled Document</title>
</head>
<body>
<table width="100%%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td width="50%" align="left" valign="top">
      <table width="100%%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="50%" align="left" valign="top">&lt;OwningEntity/&gt;</td>
          <td width="50%" valign="top"><div align="right"><font size="+2" face="Arial, Helvetica, sans-serif"><strong>PROPOSAL</strong></font></div></td>
        </tr>
      </table>
      <br>
      <hr>
      <table width="100%%" border="0" cellpadding="0" cellspacing="0">
        <tr align="left" valign="top">
          <td width="50%"><p><font face="Arial, Helvetica, sans-serif"><strong>Bill To:</strong></font></p>&lt;BillTo/&gt;</td>
          <td width="50%"><div align="left"><p><font face="Arial, Helvetica, sans-serif"><strong>Ship To:</strong></font></p>&lt;ShipTo/&gt;</div></td>
        </tr>
      </table>
      <hr>
      <br>
      <table width="100%%" border="1" cellspacing="0" cellpadding="0">
        <tr align="left" valign="top" bgcolor="#CCCCCC">
          <td width="25%"><font size="2" face="Arial, Helvetica, sans-serif">Account Manager</font></td>
          <td width="25%"><font size="2" face="Arial, Helvetica, sans-serif">Purchase Order</font></td>
          <td width="25%"><font size="2" face="Arial, Helvetica, sans-serif">Payment Terms</font></td>
          <td width="25%"><font size="2" face="Arial, Helvetica, sans-serif">Date</font></td>
        </tr>
        <tr align="left" valign="top">
          <td width="25%">&nbsp;&lt;AccountManager/&gt;</td>
          <td width="25%">&nbsp;&lt;PurchaseOrder/&gt;</td>
          <td width="25%">&nbsp;&lt;PaymentTerms/&gt;</td>
          <td width="25%">&nbsp;&lt;Date/&gt;</td>
        </tr>
      </table>
      <br>
      <table width="100%%" border="1" cellspacing="0" cellpadding="0">
        <tr>
          <td width="15%"><font size="2" face="Arial, Helvetica, sans-serif">ITEM</font></td>
          <td width="40%"><font size="2" face="Arial, Helvetica, sans-serif">Description</font></td>
          <td width="15%"><font size="2" face="Arial, Helvetica, sans-serif">Quantity</font></td>
          <td width="15%"><font size="2" face="Arial, Helvetica, sans-serif" align="right">Price</font></td>
          <td width="15%"><font size="2" face="Arial, Helvetica, sans-serif" align="right">Extended Price</font></td>
        </tr>
      </table>
      <table width="100%%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td>&lt;ProposalDetails/&gt;</td>
        </tr>
      </table>
      <table width="100%%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="15%" bordercolor="#FFFFFF">&nbsp;</td>
          <td width="40%" bordercolor="#FFFFFF">&nbsp;</td>
          <td width="15%" bordercolor="#FFFFFF">&nbsp;</td>
          <td>
            <table width="100%%" border="1" cellspacing="0" cellpadding="0">
              <tr>
                <td width="50%" bgcolor="#CCCCCC"><div align="right"><font size="2" face="Arial, Helvetica, sans-serif"><strong>Sub Total</strong></font></div></td>
                <td width="50%"><div align="right"><font size="2" face="Arial, Helvetica, sans-serif">&lt;SubTotal/&gt;</font></div></td>
              </tr>
              <tr>
                <td width="50%" bgcolor="#CCCCCC"><div align="right"><font size="2" face="Arial, Helvetica, sans-serif"><strong>Tax</strong></font></div></td>
                <td width="50%"><div align="right"><font size="2" face="Arial, Helvetica, sans-serif">&lt;Tax/&gt;</font></div></td>
              </tr>
              <tr>
                <td width="50%" bgcolor="#CCCCCC"><div align="right"><font size="2" face="Arial, Helvetica, sans-serif"><strong>Total</strong></font></div></td>
                <td width="50%"><div align="right"><font size="2" face="Arial, Helvetica, sans-serif">&lt;Total/&gt;</font></div></td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      <p>Special Instructions:</p>
      <table width="400" border="1" cellspacing="0" cellpadding="0">
        <tr>
          <td bgcolor="#CCCCCC"><p>&lt;Notes/&gt;</p><p>&nbsp;</p></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<p>&nbsp;</p>
</body>
</html>' where ptdetailid=2;

-- Ryan's fix for the cvfolder table
INSERT INTO `cvfolder` (Name, Parent, CreatedBy, ModifiedBy, CreatedOn, ModifiedOn, Description, FullPath, LocationID, owner, visibility, IsSystem) VALUES ('CV_FILE_DEFAULT_FOLDER', 3, 2, 2, '2003-12-14 23:51:26', '20031214235126', '', NULL, 1, 1, 'PUBLIC', 'TRUE');
set @newFolderID = LAST_INSERT_ID();
INSERT INTO `defaultrecordauthorisation` (recordid, recordtypeid, privilegelevel) VALUES (@newFolderID, 76, 20);
INSERT INTO `recordauthorisation` (individualid, recordid, recordtypeid, privilegelevel)
SELECT individualid, @newFolderID AS recordid, 76 AS recordtypeid, 20 AS privilegelevel FROM `user`; 

-- Ryan's fix for the literature table
ALTER TABLE `literature` MODIFY `LiteratureID` int(10) unsigned auto_increment;


-- Ryan's update for the Literature configuration in admin
SET @literatureAdminModuleID = 73;
SET @newListType = 'Literature';
INSERT INTO listtypes (typename, moduleid) VALUES ('Literature', @literatureAdminModuleID);
INSERT INTO listviews (listtype, viewname, ownerid, creatorid, sortmember, sorttype, searchid, noofrecords, searchtype) VALUES (@newListType, 'Default View', NULL, NULL, 'Name', 'A', 0, 10, 'A');
SET @literatureAdminViewID = LAST_INSERT_ID();
UPDATE defaultviews SET viewid = @literatureAdminViewID WHERE listtype = @newListType;
INSERT INTO listcolumns (listtype, columnname, columnorder) VALUES (@newListType, 'Name', 1);
INSERT INTO listcolumns (listtype, columnname, columnorder) VALUES (@newListType, 'File', 2);
INSERT INTO viewcolumns (viewid, columnname, columnorder) VALUES (@literatureAdminViewID, 'Name', 1);
INSERT INTO viewcolumns (viewid, columnname, columnorder) VALUES (@literatureAdminViewID, 'File', 2); 

-- Mike's addition of the public records framework
DROP TABLE IF EXISTS `publicrecords`;
CREATE TABLE `publicrecords` (
  `recordid` int(11) NOT NULL,
  `moduleid` int(11) NOT NULL,
  KEY `recordid` (`recordid`),
  KEY `moduleid` (`moduleid`)
) TYPE=InnoDB;

-- Update all existing users to have a preference for Public Records - by default set to 'Yes'
INSERT INTO `userpreference` (`individualid`, `preference_name`, `preference_value`) SELECT `individualid`, 'allRecordsPublic', 'Yes' FROM `user`;


-- Mike's update for allowing the system to have a default smtp server
ALTER TABLE `serversettings` ADD COLUMN `smtpserver` varchar(255) AFTER `adminemailaddress`;


-- Naresh's fix for Files module
UPDATE `cvfile` SET `updatedby`=`creator`;
UPDATE `cvfolder` SET `ModifiedBy`=`createdBy`;

-- Naresh's fix for Print Templates
UPDATE `ptdetail` SET `artifactid`=2 WHERE `ptdetailid`=2;

