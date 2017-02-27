/*
 * $RCSfile: QueryCollection.java,v $    $Revision: 1.11 $  $Date: 2005/10/24 14:52:32 $ - $Author: mcallist $
 * 
 * The contents of this file are subject to the Open Software License
 * Version 2.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.centraview.com/opensource/license.html
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * 
 * The Original Code is: CentraView Open Source. 
 * 
 * The developer of the Original Code is CentraView.  Portions of the
 * Original Code created by CentraView are Copyright (c) 2004 CentraView,
 * LLC; All Rights Reserved.  The terms "CentraView" and the CentraView
 * logos are trademarks and service marks of CentraView, LLC.
 */

package com.centraview.common;

import java.util.HashMap;

/**
 * A static class that contains the Massive collection of SQL queries
 * @author Kevin McAllister <kevin@centraview.com>
 */
public final class QueryCollection
{
  protected static HashMap allSql = null;

  static
  {
    allSql = new HashMap();

    allSql.put("contact.addaddress","INSERT INTO address (AddressType,Street1,Street2,City,State,Zip,Country,Website,jurisdictionID) VALUES (?,?,?,?,?,?,?,?,?)");
    allSql.put("contact.addaddressrelate","INSERT INTO addressrelate(Address,ContactType,Contact,AddressType,IsPrimary) VALUES (?,?,?,?,?)");
    allSql.put("contact.addmoc","insert into methodofcontact(MOCType,Content,Note,syncas,MOCOrder) values(?,?,?,?,?)");
    allSql.put("contact.addmocrelate","insert into mocrelate(MOCID,ContactType,ContactID,isPrimary) values(?,?,?,?)");
    allSql.put("contact.addentity","insert into entity(ExternalID,Name,Source,DBase,List,Creator,Owner,Created,AccountManagerID,AccountTeamID) values(?,?,?,?,?,?,?,NOW(),?,?)");
    allSql.put("contact.getentity", "select EntityID, ExternalID, Name, Source, Modified, Created, Owner, ModifiedBy,DBase, List, Creator,AccountManagerID ,AccountTeamID from entity where EntityID = ?");
    allSql.put("contact.getentitymarketinglist","SELECT list from entity WHERE entityid = ?");
    allSql.put("contact.getprimaryaddress"," select a.AddressID,a.AddressType,a.Street1,a.Street2,a.City,a.State,a.Zip,a.Country,a.Website,b.IsPrimary,c.name as countryname ,d.name as statename from address a, addressrelate b ,country c,state d where a.AddressID = b.Address  and b.isPrimary = 'YES' and c.countryid = a.country and d.stateid = a.state and b.Contact=? and b.contactType= ? ");
    allSql.put("contact.getprimarymoc","select a.mocid, a.MOCType, a.Content, a.Note, a.MOCOrder, isPrimary,a.syncas from methodofcontact a, `mocrelate` b where a.MOCID = b.MOCID and b.isPrimary='YES' and ContactID = ? and ContactType= ?");
    allSql.put("contact.updateentity","update entity set ExternalID = ?, Name = ?, Source = ?, DBase = ?,  ModifiedBy=? , AccountManagerID = ?,AccountTeamID = ?, Modified = NOW() where EntityID = ?");
    allSql.put("contact.getgroup","select GroupID,Description,Name from grouptbl where GroupID=?");

   allSql.put("contact.individuallistinsert","INSERT INTO individuallist(IndividualID, EntityID, FirstName, LastName, MiddleInitial, Title,dbid ) "  +
             "SELECT ind1.IndividualID, ind1.Entity, ind1.FirstName, ind1.LastName, ind1.MiddleInitial, ind1.Title, ind1.list FROM individual ind1 WHERE ind1.owner=? " +
               "UNION SELECT ind2.IndividualID, ind2.Entity, ind2.FirstName, ind2.LastName, ind2.MiddleInitial, ind2.Title, ind2.list FROM publicrecords pub LEFT JOIN individual ind2 ON pub.recordid=ind2.Individualid WHERE pub.moduleid=15 " +
               "UNION SELECT ind3.IndividualID, ind3.Entity, ind3.FirstName, ind3.LastName, ind3.MiddleInitial, ind3.Title, ind3.list FROM recordauthorisation auth  LEFT JOIN individual ind3 ON auth.recordid=ind3.Individualid WHERE auth.individualid=? AND auth.recordtypeid=15 AND auth.privilegelevel<40");


    allSql.put("contact.individuallistcreatetable", "CREATE TEMPORARY TABLE individuallist SELECT i.IndividualID AS individualID, i.List AS dbid, i.Entity AS EntityID, CONCAT(i.FirstName, ' ', i.LastName) AS Name, i.FirstName, i.MiddleInitial, i.LastName, i.Title, e.Name AS Entity, a.Street1, a.Street2, a.City, a.State, a.Zip, a.Country, moc.Content AS Phone, moc.Content AS Email, moc.Content AS Fax FROM individual i LEFT OUTER JOIN entity e ON (i.Entity=e.EntityID) LEFT OUTER JOIN addressrelate ar ON (i.IndividualID=ar.Contact) LEFT OUTER JOIN address a ON (ar.Address=a.AddressID) LEFT OUTER JOIN methodofcontact moc ON (moc.MOCID=0) WHERE i.List=? AND ar.contacttype = 2");
    allSql.put("contact.individualcreatetable", "CREATE TEMPORARY TABLE individuallist SELECT i.IndividualID AS individualID, i.List AS dbid, i.Entity AS EntityID, CONCAT(i.FirstName, ' ', i.LastName) AS Name, i.FirstName, i.MiddleInitial,i.LastName, i.Title, e.Name AS Entity, a.Street1, a.Street2, a.City, a.State, a.Zip, a.Country, moc.Content AS Phone, moc.Content  AS Email, moc.Content AS Fax FROM individual i LEFT OUTER JOIN entity e ON (i.Entity=e.EntityID) LEFT OUTER JOIN addressrelate ar ON (i.IndividualID=ar.Contact) LEFT OUTER JOIN address a ON (ar.Address=a.AddressID) LEFT OUTER JOIN methodofcontact moc ON (moc.MOCID=0) WHERE ar.contacttype = 2 and i.owner=? UNION SELECT i.IndividualID AS individualID, i.List AS dbid, i.Entity AS EntityID, CONCAT(i.FirstName, ' ', i.LastName) AS Name, i.FirstName, i.MiddleInitial, i.LastName, i.Title, e.Name AS Entity, a.Street1, a.Street2, a.City, a.State, a.Zip, a.Country, moc.Content AS Phone, moc.Content  AS Email, moc.Content AS Fax FROM publicrecords pub LEFT JOIN individual i ON  pub.recordid=i.individualid  LEFT OUTER JOIN entity e ON  (i.Entity=e.EntityID) LEFT OUTER JOIN addressrelate ar ON (i.IndividualID=ar.Contact) LEFT OUTER JOIN address a ON (ar.Address=a.AddressID) LEFT OUTER JOIN methodofcontact moc ON (moc.MOCID=0) WHERE ar.contacttype = 2 and pub.moduleid=15 UNION  SELECT i.IndividualID AS individualID, i.List AS dbid, i.Entity AS EntityID,  CONCAT(i.FirstName, ' ', i.LastName) AS Name, i.FirstName, i.MiddleInitial, i.LastName, i.Title, e.Name AS Entity, a.Street1, a.Street2, a.City, a.State, a.Zip, a.Country, moc.Content AS Phone, moc.Content  AS Email, moc.Content AS Fax FROM recordauthorisation auth  LEFT JOIN individual i ON auth.recordid=i.individualid LEFT OUTER JOIN entity e ON (i.Entity=e.EntityID) LEFT OUTER JOIN addressrelate ar ON (i.IndividualID=ar.Contact) LEFT OUTER JOIN address a ON (ar.Address=a.AddressID) LEFT OUTER JOIN methodofcontact moc ON (moc.MOCID=0) WHERE ar.contacttype = 2 AND auth.individualid=? AND auth.recordtypeid=15 AND auth.privilegelevel<40");

    allSql.put("contact.individuallistupdate1", "UPDATE individuallist il, methodofcontact moc, mocrelate mr SET il.Phone=moc.Content WHERE il.IndividualID=mr.ContactID AND moc.MOCID=mr.MOCID AND moc.MOCType=4 AND mr.contacttype = 2");
    allSql.put("contact.individuallistupdate2", "UPDATE individuallist il, methodofcontact moc, mocrelate mr SET il.Email=moc.Content WHERE il.IndividualID=mr.ContactID AND moc.MOCID=mr.MOCID AND moc.MOCType=1 AND mr.contacttype = 2");
    allSql.put("contact.individuallistupdate3", "UPDATE individuallist il, methodofcontact moc, mocrelate mr SET il.Fax=moc.Content WHERE il.IndividualID=mr.ContactID AND moc.MOCID=mr.MOCID AND moc.MOCType=2 AND mr.contacttype = 2");

    // If some field change then change also ContactListEjb
    allSql.put("contact.individuallistselect","Select individualID ,dbid,EntityID,concat(FirstName ,'  ', LastName) Name, FirstName, MiddleInitial, LastName, Title, Entity, Phone ,Email, Fax,concat(Street1,'  ',Street2,' ',City ,' ', State ,' ' , Zip , ' ',Country ) Address from individuallist");
    allSql.put("contact.individuallistdroptable","DROP TABLE individuallist");


    allSql.put("contact.grouplistcreatetable","create TEMPORARY TABLE grouplist(NoOfMembers INT) select GroupID GroupID, Name Name, Description Description FROM grouptbl WHERE 1=0");
    allSql.put("contact.grouplistinsert","INSERT INTO grouplist(GroupID, Name, Description, NoOfMembers )" +
           "SELECT grouptbl.GroupID, grouptbl.Name, grouptbl.Description, COUNT(member.ChildID) " +
           "FROM  grouptbl LEFT JOIN member ON grouptbl.GroupID = member.GroupID WHERE grouptbl.owner=? GROUP BY grouptbl.GroupID " +
           "UNION SELECT gr2.GroupID, gr2.Name, gr2.Description, COUNT(member.ChildID) FROM publicrecords pub " +
           "LEFT JOIN grouptbl gr2 ON pub.recordid=gr2.GroupID LEFT JOIN member ON gr2.GroupID = member.GroupID " +
           "WHERE pub.moduleid=16 GROUP BY gr2.GroupID " +
           "UNION SELECT gr3.GroupID, gr3.Name, gr3.Description, COUNT(member.ChildID) FROM recordauthorisation auth " +
           "LEFT JOIN grouptbl gr3 ON auth.recordid=gr3.GroupID LEFT JOIN member ON gr3.GroupID = member.GroupID " +
           "WHERE auth.individualid=? AND auth.recordtypeid=16 AND auth.privilegelevel<40 GROUP BY gr3.GroupID ");


    // If some field change then change also ContactListEjb
    allSql.put("contact.grouplistselect","Select GroupID, Name, Description, NoOfMembers from grouplist");
    allSql.put("contact.grouplistdroptable","DROP TABLE grouplist");

    allSql.put("contact.addcustomfield","insert into customField(Name,FieldType) values(?,?)");
    allSql.put("contact.addcustomfieldvalue","insert into customFieldValue(CustomFieldID,Value) values(?,?)");
    // Changed addcustomfiledscalar and multiple to updates, because the blank values will be (or should be) inserted first.
    // See EntityEJB.ejbCreate for an example.
    allSql.put("contact.addcustomfieldscalar","update customfieldscalar set customfieldid=?, value=?");
    allSql.put("contact.addcustomfieldmultiple","update customfieldmultiple set customfieldid=?, value=?");
    allSql.put("contact.creategrouptbl","insert into grouptbl(Name,description) values(?,?)");
    allSql.put("contact.createmember","insert into member(groupID,childID) values(?,?)");
    allSql.put("contact.createindividual","insert into individual(entity,FirstName,LastName,Title,CreatedBy,Created,PrimaryContact,MiddleInitial,Source,ExternalID,list,Owner) values(?,?,?,?,?,NOW(),?,?,?,?,?,?)");

    allSql.put("contact.getindividual", "SELECT i.individualID, i.Entity,i.FirstName, i.LastName, i.Title, i.Created, i.Owner, i.Modified, "+
								" i.CreatedBy, i.ModifiedBy, i.PrimaryContact, i.MiddleInitial, i.ExternalID, i.source, i.list, "+ 
								" e.Name As EntityName from individual i LEFT OUTER JOIN entity e ON (i.Entity = e.EntityID) "+
								" where IndividualID = ? ");
    
    allSql.put("contact.updateindividual","update individual set FirstName = ? ,LastName = ?,ExternalID = ? , Title= ?,  ModifiedBy =  ? , PrimaryContact = ?, MiddleInitial = ?, Source = ?, Modified = NOW() where individualId = ? ");
    allSql.put("contact.individual.linktoentity", "UPDATE individual SET Entity = ? , PrimaryContact = ? WHERE IndividualID = ?");
    allSql.put("contact.updateaddress","UPDATE address SET street1 = ?, street2 = ?, city = ?, state = ?, zip = ?, country = ?, website = ?, AddressType = ?, jurisdictionID = ? WHERE AddressID = ?");
    allSql.put("contact.updaterelateaddress","UPDATE addressrelate SET AddressType = ?, isPrimary = ? WHERE Address = ? and ContactType = ? and Contact = ?");
    allSql.put("contact.updaterelateaddresstonotprim","UPDATE addressrelate SET IsPrimary='NO' WHERE ContactType=? and Contact=?");
    allSql.put("contact.updateMOC","update methodofcontact set MOCType = ?,Content = ? ,Note = ?,syncas = ?, MOCOrder=? where MOCID = ?  ");
    allSql.put("contact.updateMOCRelate","update mocrelate set isPrimary = ? where MOCID = ? ");
    allSql.put("contact.updateMOCSyncAs","update mocrelate mr1,mocrelate mr2,methodofcontact mc1,methodofcontact mc2 set mc2.syncas=''where mc1.mocid= ? and mr1.mocid=mc1.mocid and mr1.contactid=mr2.contactid and mr1.contacttype=mr2.contacttype and mr2.mocid=mc2.mocid and mc2.syncas= ?   ");
    allSql.put("contact.AddMOCSyncAs","update methodofcontact ,mocrelate set syncas = '' where methodofcontact.mocid = mocrelate.mocid  and  mocrelate.contactid = ? and mocrelate.contacttype = ? and mocrelate.mocid  !=  ? and syncas = ? ");

    allSql.put("contact.updategroup","update grouptbl set name = ?  ,description = ?  where groupid = ?  ");


    allSql.put("contact.removeindividualsforentity", "update individual set entity = null , primarycontact = 'NO' where entity = ? ");
    allSql.put("contact.deletegroupmemberindividual", "delete from member where ChildID = ? ");

    allSql.put("contact.getindividualforprimarycontact", "select individualID, Entity  from individual where  Entity = ?  and PrimaryContact = 'YES' ");
    allSql.put("contact.deleteaddress","delete from address where addressID = ? ");
    allSql.put("contact.deleteaddressrelate","delete from addressrelate where address = ?");
    allSql.put("contact.deletemoc","delete from methodofcontact where MOCID = ? ");
    allSql.put("contact.deletemocrelate","delete from mocrelate where MOCID = ?");
    allSql.put("contact.deleteindividual", "delete from individual where individualID = ? ");
    allSql.put("contact.deleteentity", "delete from entity  where entityID = ? ");
    allSql.put("contact.getalladdress","select a.AddressID,a.AddressType,a.Street1,a.Street2,a.City,a.Zip,c.name Country ,a.Website,b.IsPrimary,d.name StateName from address a, addressrelate b,country c,state d where a.AddressID = b.Address and a.state = d.stateID and a.country = c.countryID and b.Contact= ? and b.contactType= ? ");
    allSql.put("contacts.getAllAddressVO","SELECT a.addressId, a.addressType, a.street1, a.street2, a.city, a.state, a.zip, a.country, a.website, b.isPrimary FROM address a, addressrelate b WHERE a.AddressID = b.Address AND b.Contact= ? AND b.contactType= ? ");
    allSql.put("contact.getallmoc","select a.mocid, a.MOCType, a.Content, a.Note, a.MOCOrder, IFNULL(a.MOCOrder, '9999') as noNullMOCOrder, isPrimary, c.Name ,a.syncas from methodofcontact a, mocrelate b, moctype c where a.MOCID = b.MOCID  and a.MOCType = c.MOCTypeID and ContactID = ? and ContactType= ? order by noNullMOCOrder asc, a.MOCID asc");
    allSql.put("contact.getindividualusername", "SELECT name FROM user where individualid =  ?");
    allSql.put("contact.deletegroupmember","delete from member  where GroupID = ? ");
    allSql.put("contact.deletegroup","delete from grouptbl where GroupID = ? ");
    allSql.put("contact.getindividualrelatedentity","SELECT entity.entityid, entity.name FROM entity, individual where entity.entityid = individual.entity AND individual.individualid = ?");

    // custom field list
    allSql.put("customfield.createTempListTable", "CREATE TEMPORARY TABLE customfieldlist (customfieldid int(11), name varchar(255), value varchar(255))");
    allSql.put("customfield.insertIntoTempListTable", "INSERT INTO customfieldlist SELECT cf.customfieldid, cf.name, NULL FROM customfield cf LEFT JOIN cvtable t ON (cf.recordtype=t.tableid) WHERE t.name=?");
    allSql.put("customfield.updateTempListScalar", "UPDATE customfieldlist cfl LEFT JOIN customfieldscalar cfs ON (cfl.customfieldid=cfs.customfieldID) SET cfl.value=cfs.value WHERE cfs.recordid=?");
    allSql.put("customfield.updateTempListMultiple", "UPDATE customfieldlist cfl, customfieldmultiple cfm, customfieldvalue cfv SET cfl.value=cfv.value WHERE cfl.customfieldid=cfm.customfieldid AND cfv.valueid=cfm.valueid and cfm.recordid=?");
    allSql.put("customfield.getCustomFieldList", "SELECT customfieldid, name, value FROM customfieldlist");
    allSql.put("customfield.deleteTempTable", "DROP TABLE customfieldlist");

    // Get Custom Fields
    allSql.put("common.getCustomFields"," SELECT cf.customfieldid, cf.name, cf.fieldtype FROM customfield cf LEFT OUTER JOIN cvtable cv ON (cf.recordtype=cv.tableid) WHERE cv.name= ?  order by  1 ");
    allSql.put("common.getCustomField"," SELECT customfieldid ,customfield.name,fieldtype,recordtype FROM customfield ,cvtable where recordtype  = tableid and cvtable.name  = ?  order by  1  ");
    allSql.put("common.getCustomFieldOnly"," SELECT customfieldid ,customfield.name,fieldtype,recordtype FROM customfield where  customfieldid = ?   ");
    allSql.put("common.getCustomFieldOption"," SELECT cf.customfieldid, cf.name, cf.fieldtype,cfv.valueid,cfv.value from customfield cf,customfieldvalue cfv ,cvtable where cf.customfieldid = cfv.customfieldid and cf.recordtype = cvtable.tableid and  cvtable.name  =  ?");
    allSql.put("common.addCustomFieldScalar"," insert into customfieldscalar (customfieldid,recordid,value) values (?,?,? )");
    allSql.put("common.addCustomFieldMultiple"," insert into customfieldmultiple (customfieldid,recordid,valueid) values (?,?,? )");
    allSql.put("common.updateCustomFieldScalar"," update  customfieldscalar set value = ? where customfieldid = ? and recordid = ? ");
    allSql.put("common.updateCustomFieldMultiple"," update  customfieldmultiple set valueid = ? where customfieldid = ? and recordid = ? ");
    allSql.put("common.getCustomFieldList", "SELECT cf.customfieldid, cf.name, cfs.value FROM customfield cf, cvtable, customfieldscalar cfs WHERE cf.recordtype=cvtable.tableid AND cf.customfieldid=cfs.customfieldid AND cfs.recordid=? AND cvtable.name=? UNION SELECT cf.CustomFieldID, cf.Name, cfv.Value FROM customfield cf LEFT JOIN customfieldmultiple cfm ON (cf.CustomFieldID=cfm.CustomFieldID) LEFT JOIN customfieldvalue cfv ON (cfm.ValueID=cfv.ValueID) LEFT JOIN cvtable cvt ON (cf.RecordType=cvt.tableid) WHERE cfm.RecordID=? AND cvt.name=?");

    allSql.put("contact.deletecustomfieldscalar", "delete customfieldscalar  from customfieldscalar inner join customfield  inner join cvtable on(customfield.recordtype =   cvtable.tableid and cvtable.name = ? and customfield.fieldtype = 'SCALAR' ) on (customfieldscalar .customfieldid = customfield.customfieldid) where customfieldscalar.recordid = ?   ");
    allSql.put("contact.deletecustomfieldmultiple", "delete customfieldmultiple  from customfieldmultiple inner join customfield  inner join cvtable on(customfield.recordtype =   cvtable.tableid and cvtable.name = ? and customfield.fieldtype = 'MULTIPLE') on (customfieldmultiple.customfieldid = customfield.customfieldid) where customfieldmultiple.recordid = ?   ");
    allSql.put("contact.checkindividualcustomfield"," select recordid from  customfieldmultiple where recordid = ? union select recordid from  customfieldscalar where recordid = ? ");
    // 
    allSql.put("contact.getentites","select * from entity " );
    allSql.put("contact.listgroupmembers" , "select childid from member where GroupID = ? ");
    allSql.put("contact.deleteselectedgroupmember","delete from member  where GroupID = ? and ChildId= ?");

    allSql.put("contact.getAllDb", "SELECT mls.ListID AS dbaseid, mls.title AS name FROM marketinglist mls WHERE mls.owner=? UNION SELECT m.ListID AS dbaseid, m.title AS name FROM marketinglist m LEFT JOIN publicrecords p ON (m.ListID=p.recordid) WHERE p.moduleid=32 UNION SELECT ml.ListID AS dbaseid, ml.title AS name FROM marketinglist ml LEFT JOIN recordauthorisation ra ON (ml.ListID=ra.recordid) WHERE ra.recordtypeid=32 AND ra.individualid=?");
    allSql.put("contact.getAllMarketingList","SELECT listid, title FROM marketinglist");

    // These Statements are common
    allSql.put("activity.appointmentlistcreatetable","create TEMPORARY TABLE appointmentlist SELECT en1.ActivityID ActivityID , en1.Title Title , en1.Location Location , en1.Start Start, en1.End  End,priv.Name Priority ,  indv.FirstName FirstN, indv.LastName LastN ,en1.Created Createddate  , stat.Name Status,us.individualid ,us.userid userid FROM `activity` en1, `activitypriority` priv ,`activitystatus` stat , `individual` indv,`user` us where 1=0;");
    //These Statements are for All Appointments

    allSql.put("activity.appointmentlistinsert","INSERT INTO appointmentlist( ActivityID, Title , Location ,Start, End , Createddate ) SELECT DISTINCT a.ActivityID, a.Title , a.Location ,a.Start, a.End , a.Created FROM `activity`a,`activitytype` at,`attendee` att where a.type =at.typeid and at.name='Appointment' and att.ActivityID=a.ActivityID and att.individualid=? UNION SELECT DISTINCT a.ActivityID, a.Title , a.Location ,a.Start, a.End , a.Created FROM `activity`a,`activitytype` at where a.type =at.typeid and at.name='Appointment'and a.owner=?;" );
    allSql.put("activity.appointmentlistupdate3","UPDATE appointmentlist,`activity`,`individual` SET appointmentlist.individualid=`individual`.individualid,appointmentlist.FirstN = `individual`.FirstName, appointmentlist.LastN = `individual`.LastName WHERE `individual`.IndividualID = `activity`.creator and `activity`.ActivityID=appointmentlist.ActivityID;" );

    //These Statements are for My Appointments Lists

    allSql.put("activity.myappointmentlistinsert","insert into appointmentlist(activityId,individualId,userid) select act.activityId,act.Creator,us.userid from user us,activity act,activitytype atp where act.type =atp.typeid and atp.name='Appointment' and us.individualid=act.Creator and act.owner=?");
    allSql.put("activity.myappointmentlistupdate1","update appointmentlist my ,`activity` set my.Title =`activity`.title, my.createddate=`activity`.created,my.start=`activity`.start, my.End=`activity`.end,my.location=`activity`.location where `activity`.activityId =my.activityId;");
    allSql.put("activity.myappointmentlistupdate2","UPDATE appointmentlist ,`individual` SET appointmentlist .FirstN = `individual`.FirstName, appointmentlist.LastN = `individual`.LastName WHERE `individual`.individualid=appointmentlist.individualid;");

    //These  Statements are for common for All Appointments and My Appointments
    allSql.put("activity.appointmentlistupdate1","UPDATE appointmentlist ,`activity` , `activitypriority` SET appointmentlist.Priority = `activitypriority`.Name WHERE `activity`.`Priority` = `activitypriority`.PriorityID AND  `activity`.`ActivityID`=appointmentlist.ActivityID ");
    allSql.put("activity.appointmentlistupdate2","UPDATE appointmentlist ,`activity` , `activitystatus` SET appointmentlist.Status = `activitystatus`.Name WHERE `activity`.`Status` = `activitystatus`.`StatusID` AND  `activity`.`ActivityID`=appointmentlist.ActivityID ;" );
    //allSql.put("activity.appointmentlistselect","Select ActivityID , Title , Location, Start, End, Priority , Createddate , concat( FirstN, LastN ) CreatedBy , Status from appointmentlist ORDER BY ? LIMIT ? , ?;" );
    allSql.put("activity.appointmentlistdroptable","DROP TABLE appointmentlist;");
    // End Of Appointments
    allSql.put("contact.getallstate","select  StateID,  Name from state order by StateID");
    allSql.put("contact.getallcountry","Select CountryID ,  Name  from country order by CountryID");
    allSql.put("contact.getallmoctype","Select  MOCTypeID,  Name from moctype");
    allSql.put("contact.getallusers","select usr.individualid,ind.FirstName Name from individual ind , user usr where ind.individualid = usr.individualid");
    allSql.put("contact.getallgroups","SELECT groupid,name Name FROM grouptbl");
    allSql.put("contact.checkindividualasUser"," SELECT individualid FROM user where individualid = ? ");
    allSql.put("contact.getaddress","SELECT a.AddressID, a.AddressType, a.Street1, a.Street2, a.City, a.State, a.Zip, a.Country, a.Website, ar.isPrimary, a.jurisdictionID FROM address a, addressrelate ar WHERE a.AddressID = ? AND ar.Address = ?");
    allSql.put("contact.getrelateaddress","select a.AddressID,a.AddressType,a.Street1,a.Street2,a.City,a.State,a.Zip,a.Country,a.Website,b.isPrimary from address a, addressrelate b where b.Address=a.AddressID and a.AddressID = ? and b.ContactType=? and b.Contact=?");
    allSql.put("contact.getmoc","select a.mocid, a.MOCType, a.Content, a.Note, a.MOCOrder, b.Name,a.syncas from methodofcontact a, moctype b where a.MOCType = b.MOCTypeID and MOCID = ?");
    allSql.put("contact.getallsyncas","SELECT syncas FROM syncmaster");
    // Activity DML
    allSql.put("activity.addactivity","insert into activity(Type, Priority, Status, Title, DueDate, Details, Creator, Start, End, Location, AllDay, Owner, visibility, AttachmentType, Notes, CompletedDate, Created) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW())");
    allSql.put("activity.addattendee","insert into attendee(activityid,individualid,type,status) values(?,?,?,?)");
    allSql.put("activity.addrecurrence","insert into recurrence(activityID,every,timeperiod,RecurrOn,startdate,until) values(?,?,?,?,?,?)");
    allSql.put("activity.addrecurrenceexception","insert into recurexcept(RecurrenceID, Exception) values(?,?)");
    allSql.put("activity.addaction","insert into action (Type,Message,ActionTime,Repeat,ActionInterval) values(?,?,?,?,?)");
    allSql.put("activity.addactivityaction","insert into activityaction (ActivityID,ActionId,recipient) values(?,?,?)");
    allSql.put("activity.addresource","insert into resourcerelate (ActivityID,ResourceID) values(?,?)");
    allSql.put("activity.addactivitylink","insert into activitylink (ActivityID,RecordTypeID,RecordID) values(?,?,?)");

    allSql.put("activity.addcall","insert into call (ActivityID,CallType) values(?,?)");


    // Activity fetch
    allSql.put("activity.getbasicactivity", "SELECT a.ActivityID, a.Type, a.AllDay, a.Priority, a.Status, a.Title, a.DueDate, a.CompletedDate, a.Details, a.Creator, a.Owner, a.ModifiedBy, a.Modified, a.Created, a.Start, a.End, a.Location, a.visibility, b.Name, a.AttachmentType, a.Notes FROM activity a, activitytype b WHERE a.Type=b.TypeID AND ActivityID=?");
    allSql.put("activity.getrecurrence","SELECT RecurrenceID, ActivityID, Every, TimePeriod, Until, startdate, RecurrOn FROM recurrence where ActivityID = ?")  ;
    allSql.put("activity.getrecurrexcept","SELECT RecurrenceID, Exception FROM recurexcept where RecurrenceID = ?") ;
    allSql.put("activity.getattendee","SELECT a.Type, a.Status, a.IndividualID , a.ActivityID, b.FirstName, b.MiddleInitial,b.LastName FROM attendee a, individual b where a.IndividualID = b.IndividualID and a.ActivityID = ?");
    allSql.put("activity.getresource","SELECT ActivityResourceID, a.Name, a.Detail FROM activityresources a, resourcerelate b where a.ActivityResourceID = b.ResourceID and b.ActivityID = ?");
    allSql.put("activity.getaction","SELECT   a.ActionID, a.Type, a.Message, a.ActionTime, a.Repeat, a.ActionInterval, b.recipient FROM action a, activityaction b where a.ActionID = b.ActionID and b.ActivityID = ?");
    allSql.put("activity.getactivitylink","SELECT ActivityID,RecordTypeID, RecordID FROM activitylink where ActivityID = ?");
    allSql.put("activity.getcall","SELECT ActivityID,CallType FROM call where ActivityID = ?");

    allSql.put("activity.deleteattendee","delete attendee from attendee where activityID=?");
    allSql.put("activity.deleterecurexcept","delete recurexcept from recurexcept, recurrence AS b where recurexcept.recurrenceID=b.recurrenceID and b.activityId=?");
    allSql.put("activity.deleterecurrence","delete recurrence from recurrence where activityId=?");
    allSql.put("activity.deleteresourcerelate","delete resourcerelate from resourcerelate where activityid=?");
    allSql.put("activity.deleteaction","delete action from action,activityaction b where action.actionid=b.actionid and b.activityId=?");
    allSql.put("activity.deleteactivityaction","delete activityaction from activityaction where activityId=?");
    allSql.put("activity.deleteactivitylink","delete activitylink from activitylink where ActivityID=?");
    allSql.put("activity.deleteactivity","delete activity from activity where activityId=?");
    allSql.put("activity.deletecall","delete from call where activityId=?");

    allSql.put("activity.updateactivity","update activity set Type = ?, Priority = ?, Status = ?, Title = ?, DueDate = ?, Details = ?, Start = ?, End = ?, Location = ?, AllDay = ?,  ModifiedBy = ?, CompletedDate = ?, visibility = ?, AttachmentType = ? ,Notes = ? where ActivityID = ? ");
    allSql.put("activity.updatecall","update call set CallType = ? where ActivityID = ? ");

    allSql.put("activity.getallresources","select ActivityResourceID, Name, Detail from activityresources");
    allSql.put("activity.updateattendeestatus","update attendee,attendeestatus set status=statusid where name=? and individualid=? and activityid=? ");

    // These Statements are common

    allSql.put("activity.meetinglistcreatetable", "create TEMPORARY TABLE Meetinglist SELECT en1.ActivityID ActivityID , en1.Title Title ,en1.Start StartTime, en1.End  EndTime, indv.FirstName FirstN, indv.LastName LastN ,ap.Name Priority,as1.Name Status,ac.ActivityResourceID ResourceID, ac.NAME Location,en1.created Created,en1.duedate Due,us.individualid ,us.userid userid FROM `activity` en1,`activityresources` ac,`activitypriority` ap, `activitystatus` as1,`individual` indv,`user` us where 1=0;" );

    // These Statements are for All meeting
    allSql.put("activity.meetinglistinsert", "INSERT INTO Meetinglist(ActivityID, Title ,created,Due ,StartTime,EndTime,Location ) SELECT DISTINCT a.ActivityID, a.Title ,a.created,a.dueDate,a.Start,a.End,a.Location FROM `activity` a,`activitytype` at, attendee att where a.type =at.typeid and at.name='Meeting' and att.activityId=a.activityId and att.individualid=? UNION SELECT DISTINCT a.ActivityID, a.Title ,a.created,a.dueDate,a.Start,a.End,a.Location FROM `activity` a,`activitytype` at where a.type =at.typeid and at.name='Meeting' and a.owner=?;" );
    //allSql.put("activity.meetinglistupdate1", "UPDATE Meetinglist,`meeting`,`activityresources` SET Meetinglist.Location = `activityresources`.Name WHERE `activityresources`.ActivityResourceID=`meeting`.ActivityID and `meeting`.ActivityID=Meetinglist.ActivityID;" );
    allSql.put("activity.meetinglistupdate4", "UPDATE Meetinglist,`activity`,`individual` SET Meetinglist.individualid=`individual`.individualid,Meetinglist.FirstN = `individual`.FirstName, Meetinglist.LastN = `individual`.LastName WHERE `individual`.IndividualID = `activity`.creator and `activity`.ActivityID=Meetinglist.ActivityID;");

    //These Statements are for My meeting Lists

    allSql.put("activity.mymeetinglistinsert1","insert into Meetinglist (activityId,individualId,userid) select act.activityId,act.Creator,us.userid from user us,activity act,activitytype atp where act.type =atp.typeid and atp.name='Meeting' and us.individualid=act.Creator and act.owner=?");
    allSql.put("activity.mymeetinglistupdate1","update Meetinglist my ,`activity` set my.Title =`activity`.title, my.created=`activity`.created, my.due=`activity`.duedate, my.starttime=`activity`.start, my.endtime=`activity`.end, my.location=`activity`.location where `activity`.activityId =my.activityId;");
    allSql.put("activity.mymeetinglistupdate2","UPDATE Meetinglist,`individual` SET Meetinglist.FirstN = `individual`.FirstName,Meetinglist.LastN = `individual`.LastName WHERE `individual`.individualid=Meetinglist.individualid;");


    // These Statements Are common for both My meeting lists and All meeting Lists
    allSql.put("activity.meetinglistupdate2", "UPDATE Meetinglist,`activity`,`activitystatus` SET Meetinglist.Status = `activitystatus`.Name WHERE `activitystatus`.StatusID = `activity`.Status and `activity`.ActivityID=Meetinglist.ActivityID;" );
    allSql.put("activity.meetinglistupdate3","UPDATE Meetinglist,`activity`,`activitypriority` SET Meetinglist.Priority = `activitypriority`.Name WHERE `activitypriority`.PriorityID = `activity`.Priority and `activity`.ActivityID=Meetinglist.ActivityID;");
    allSql.put("activity.meetinglistdroptable","DROP TABLE Meetinglist;");
    // These Statements are common
    allSql.put("activity.calllistcreatetable","create TEMPORARY TABLE Callslist SELECT en1.ActivityID ActivityID , en1.Title Title ,en1.Start StartTime, en1.End  EndTime,indv.FirstName FirstN, indv.LastName LastN,en1.created Created,en1.Duedate Due,ap.Name Priority,as1.Name Status,cl.Name CallType,us.individualid ,us.userid userid FROM `activity` en1,`calltype` cl,`activitystatus` as1,`activitypriority` ap,`individual` indv,`user` us where 1=0;");

    // These Statements are for All call

    allSql.put("activity.calllistinsert","INSERT INTO Callslist(ActivityID, Title ,Created,Due ,StartTime,EndTime ) SELECT DISTINCT a.ActivityID, a.Title ,a.created,a.dueDate,a.Start,a.End FROM `activity` a,`activitytype` at,attendee att where a.type =at.typeid and at.name='Call' and att.activityid=a.activityid and att.individualid=? UNION SELECT DISTINCT a.ActivityID, a.Title ,a.created,a.dueDate,a.Start,a.End FROM `activity` a,`activitytype` at where a.type =at.typeid and at.name='Call' and a.owner=?;");
    allSql.put("activity.calllistupdate4","UPDATE Callslist,`activity`,`individual` SET Callslist.individualid=`individual`.individualid,Callslist.FirstN = `individual`.FirstName, Callslist.LastN = `individual`.LastName WHERE `individual`.IndividualID = `activity`.creator and  `activity`.ActivityID=Callslist.ActivityID;");

    //These Statements are for My Call Lists

    allSql.put("activity.mycalllistinsert","insert into Callslist(activityId,individualId,userid) select act.activityId,act.Creator,us.userid from user us,activity act,activitytype atp where act.type =atp.typeid and atp.name='Call' and us.individualid=act.Creator and act.owner=? ");
    allSql.put("activity.mycalllistupdate1","update Callslist my ,`activity` set my.Title =`activity`.title, my.created=`activity`.created, my.due=`activity`.duedate,my.startTime=`activity`.start, my.EndTime=`activity`.end where `activity`.activityId =my.activityId;");
    allSql.put("activity.mycalllistupdate2","UPDATE Callslist,`individual` SET Callslist.FirstN = `individual`.FirstName, Callslist.LastN = `individual`.LastName WHERE `individual`.individualid=Callslist.individualid;");


    // These Statements Are common for both My Call lists and All Call Lists
    allSql.put("activity.calllistupdate1","UPDATE Callslist,`calltype`,`call` SET Callslist.CallType = `calltype`.Name WHERE `call`.CallType = `calltype`.CallTypeID and `call`.ActivityID=Callslist.ActivityID;");
    allSql.put("activity.calllistupdate2","UPDATE Callslist,`activity`,`activitystatus` SET Callslist.Status = `activitystatus`.Name WHERE `activitystatus`.StatusID = `activity`.Status and `activity`.ActivityID=Callslist.ActivityID;");
    allSql.put("activity.calllistupdate3","UPDATE Callslist,`activity`,`activitypriority` SET Callslist.Priority = `activitypriority`.Name WHERE `activitypriority`.PriorityID = `activity`.Priority and `activity`.ActivityID=Callslist.ActivityID;");
    allSql.put("activity.calllistdroptable","DROP TABLE Callslist;");
    // These Statements Are common
    allSql.put("activity.todolistcreatetable","create TEMPORARY TABLE ToDoList SELECT en1.ActivityID ActivityID , en1.Title Title,en1.created Created,indv.FirstName FirstN, indv.LastName LastN,en1.duedate Due, en1.start Start,en1.end End,ap.Name Priority,as1.Name Status , us.individualid ,us.userid userid FROM `activity` en1,`activitystatus` as1,`activitypriority` ap,`individual` indv ,`user` us   where 1=0;");
    // These Statements Are for All ToDo Lists
    allSql.put("activity.todolistinsert1","INSERT INTO ToDoList (ActivityID, Title ,created,due,start,end) SELECT DISTINCT a.ActivityID, a.Title ,a.created,a.dueDate, a.start, a.end FROM `activity` a,`activitytype` at, attendee att where a.type =at.typeid and at.name='To Do'  and att.activityid=a.activityid and att.individualid=? UNION SELECT DISTINCT a.ActivityID, a.Title ,a.created,a.dueDate FROM `activity` a,`activitytype` at where a.type =at.typeid and at.name='To Do' and a.owner=?;");
    allSql.put("activity.todolistupdatename","UPDATE ToDoList,`activity`,`individual` SET ToDoList.individualid=`individual`.individualid,ToDoList.FirstN = `individual`.FirstName,ToDoList.LastN = `individual`.LastName WHERE `individual`.IndividualID = `activity`.creator and `activity`.ActivityID=ToDoList.ActivityID;");


    //These Statements are for My ToDo Lists

    allSql.put("activity.mytodolistinsert1" ,"insert into ToDoList(activityId,individualId,userid) select act.activityId,act.Creator,us.userid from user us,activity act,activitytype atp where act.type =atp.typeid and atp.name='To Do' and us.individualid=act.Creator and act.owner=?");
    allSql.put("activity.mytodolistupdate1", "update ToDoList my ,`activity` set my.Title =`activity`.title, my.created=`activity`.created, my.due=`activity`.duedate, my.start=`activity`.start, my.end=`activity`.end where `activity`.activityId =my.activityId;");
    allSql.put("activity.mytodolistupdate2","UPDATE ToDoList,`individual` SET ToDoList.FirstN = `individual`.FirstName,ToDoList.LastN = `individual`.LastName WHERE `individual`.individualid=ToDoList.individualid;");

    // These Statements Are common for both My ToDo lists and All ToDo Lists
    allSql.put("activity.todolistupdatestatus","UPDATE ToDoList,`activity`,`activitystatus`SET ToDoList.Status = `activitystatus`.Name WHERE `activitystatus`.StatusID = `activity`.Status and `activity`.ActivityID=ToDoList.ActivityID;");
    allSql.put("activity.todolistupdatepriority","UPDATE ToDoList,`activity`,`activitypriority` SET ToDoList.Priority = `activitypriority`.Name WHERE `activitypriority`.PriorityID = `activity`.Priority and `activity`.ActivityID=ToDoList.ActivityID;");
    allSql.put("activity.todolisttabledrop","DROP TABLE ToDoList;");
    // These Statements are common
    allSql.put("activity.nextactioncreatetable","create TEMPORARY TABLE NextList SELECT en1.ActivityID ActivityID , en1.Title Title ,en1.created Created,indv.FirstName FirstN, indv.LastName LastN,en1.duedate Due,en1.start Start,en1.end End,ap.Name Priority,as1.Name Status,us.individualid ,us.userid userid FROM `activity` en1,`activitystatus` as1,`activitypriority` ap,`individual` indv,`user` us  where 1=0;");
    // These Statements are for All Next Action
    allSql.put("activity.nextactioninsert1","INSERT INTO NextList (ActivityID, Title ,created, Due ,Start,End) SELECT DISTINCT a.ActivityID, a.Title ,a.created,a.dueDate, a.start, a.end FROM `activity` a,`activitytype` at,attendee att where a.type =at.typeid and at.name='Next Action' and att.activityid=a.activityid and att.individualid=?  union SELECT DISTINCT a.ActivityID, a.Title ,a.created,a.dueDate, a.start, a.end  FROM `activity` a,`activitytype` at where a.type =at.typeid and at.name='Next Action' and a.owner=?;");
    allSql.put("activity.nextactionupdatename","UPDATE NextList,`activity`,`individual` SET NextList.individualid=`individual`.IndividualID,NextList.FirstN = `individual`.FirstName,NextList.LastN = `individual`.LastName WHERE `individual`.IndividualID = `activity`.creator and `activity`.ActivityID=NextList.ActivityID;");

    // These Statements are for My Next Action

    allSql.put("activity.mynextactionlistinsert1","insert into NextList(activityId,individualId,userid) select act.activityId,act.Creator,us.userid from user us,activity act,activitytype atp where act.type =atp.typeid and atp.name='Next Action' and us.individualid=act.Creator and act.owner=?");
    allSql.put("activity.mynextactionlistupdate1", "update NextList my ,`activity` set my.Title =`activity`.title, my.created=`activity`.created, my.due=`activity`.duedate, my.start=`activity`.start, my.end=`activity`.end where `activity`.activityId =my.activityId; ");
    allSql.put("activity.mynextactionlistupdate2","UPDATE NextList,`individual` SET NextList.FirstN = `individual`.FirstName, NextList.LastN = `individual`.LastName WHERE `individual`.IndividualID=NextList.individualid;");

    // These Statements are common for both My Next Action and All Next Action
    allSql.put("activity.nextactionstatus","UPDATE NextList,`activity`,`activitystatus` SET NextList.Status = `activitystatus`.Name WHERE `activitystatus`.StatusID = `activity`.Status and `activity`.ActivityID=NextList.ActivityID;");
    allSql.put("activity.nextactionpriority","UPDATE NextList,`activity`,`activitypriority` SET NextList.Priority = `activitypriority`.Name WHERE `activitypriority`.PriorityID = `activity`.Priority and `activity`.ActivityID=NextList.ActivityID;");
    allSql.put("activity.nextactiontabledrop","DROP TABLE NextList;");
    allSql.put("calendar.getallscheduledactivity", "SELECT a.Start, a.Title, a.End, a.AllDay, a.ActivityID,a.Type, a.Status, a.Priority, a.DueDate, a.CompletedDate, a.Details, a.Creator, a.Owner, a.ModifiedBy, a.Modified, a.Created, a.Location, b.Name AS ActivityName, c.RecurrenceID, c.TimePeriod, c.Every, c.RecurrOn, c.Until, c.startdate, a.visibility AS visibility FROM activity a LEFT JOIN recurrence c ON (a.ActivityID=c.ActivityID), activitytype b WHERE a.Type=b.TypeID AND a.Start IS NOT NULL AND a.Type!=3 AND a.Type!=6 AND a.Type!=8 AND a.Owner=? UNION SELECT a.Start, a.Title, a.End, a.AllDay, a.ActivityID, a.Type, a.Status, a.Priority, a.DueDate, a.CompletedDate, a.Details, a.Creator, a.Owner, a.ModifiedBy, a.Modified, a.Created, a.Location, b.Name AS ActivityName, c.RecurrenceID, c.TimePeriod, c.Every, c.RecurrOn, c.Until, c.startdate, a.visibility AS visibility FROM activity a LEFT JOIN recurrence c ON (a.ActivityID=c.ActivityID), activitytype b, attendee d WHERE a.Type=b.TypeID AND a.Start IS NOT NULL AND a.Type!=3 AND a.Type!=6 AND a.Type!=8 AND a.ActivityID=d.ActivityID AND d.IndividualID=? ORDER BY 1, 2 ");

    //allSql.put("calendar.getscheduledactivitydaily","Select a.Start, a.End, a.AllDay, a.ActivityID,a.Type, a.Status,a.Priority,a.Title, a.DueDate, a.CompletedDate, a.Details, a.Creator, a.Owner Owner,a.ModifiedBy, a.Modified, a.Created, a.Location, b.Name ActivityName,c.RecurrenceID, c.TimePeriod, c.Every, c.RecurrOn, c.Until, c.startdate, a.visibility visibility from activity a LEFT JOIN recurrence c ON a.ActivityID = c.ActivityID, activitytype b where  a.Type = b.TypeID and a.Type in (3,6,8)  and a.Owner = ? and a.status != 2 union Select a.Start, a.End, a.AllDay, a.ActivityID, a.Type, a.Status,a.Priority, a.Title, a.DueDate, a.CompletedDate, a.Details, a.Creator, a.Owner, a.ModifiedBy, a.Modified, a.Created, a.Location, b.Name ActivityName, c.RecurrenceID, c.TimePeriod, c.Every, c.RecurrOn, c.Until, c.startdate, a.visibility visibility from activity a LEFT JOIN recurrence c ON a.ActivityID = c.ActivityID, activitytype b, attendee d where  a.Type = b.TypeID and  a.Type in (3,6,8) and a.ActivityID = d.ActivityID and d.IndividualID = ? and a.status != 2 ");

    // Added the query for the opportunity to show up in the calendar
    // account manager can also see the opportunity in the calendar
    allSql.put("calendar.getscheduledactivitydaily"," Select a.Start, a.End, a.AllDay, a.ActivityID,a.Type, a.Status,a.Priority,a.Title, a.DueDate, "+
				" a.CompletedDate, a.Details, a.Creator, a.Owner Owner,a.ModifiedBy, a.Modified, a.Created, "+
				" a.Location, b.Name ActivityName,c.RecurrenceID, c.TimePeriod, c.Every, c.RecurrOn, c.Until, "+
				" c.startdate, a.visibility visibility from activity a LEFT JOIN recurrence c ON "+
				" a.ActivityID = c.ActivityID, activitytype b where  a.Type = b.TypeID and a.Type in (6,8)  and "+
				" a.Owner = ? and a.status != 2 union Select a.Start, a.End, a.AllDay, a.ActivityID, a.Type, "+
				" a.Status,a.Priority, a.Title, a.DueDate, a.CompletedDate, a.Details, a.Creator, a.Owner, "+
				" a.ModifiedBy, a.Modified, a.Created, a.Location, b.Name ActivityName, c.RecurrenceID, c.TimePeriod, "+
				" c.Every, c.RecurrOn, c.Until, c.startdate, a.visibility visibility from activity a "+
				" LEFT JOIN recurrence c ON a.ActivityID = c.ActivityID, activitytype b, attendee d where "+
				" a.Type = b.TypeID and  a.Type in (6,8) and a.ActivityID = d.ActivityID and d.IndividualID = ? "+
				" and a.status != 2 UNION "+
				" Select a.Start, a.End, a.AllDay, a.ActivityID, a.Type, a.Status,a.Priority, a.Title, a.DueDate,  "+
				" a.CompletedDate, a.Details, a.Creator, a.Owner, a.ModifiedBy, a.Modified, a.Created, a.Location, "+
				" b.Name ActivityName, c.RecurrenceID, c.TimePeriod, c.Every, c.RecurrOn, c.Until, c.startdate, "+
				" a.visibility visibility from activity a LEFT JOIN recurrence c ON a.ActivityID = c.ActivityID,  "+
				" activitytype b, opportunity o where  a.Type = b.TypeID and  a.Type=3 and a.ActivityID = o.ActivityID  "+
				" and a.Owner = ? and a.status != 2 and a.Start IS NOT NULL UNION  "+
				" Select a.Start, a.End, a.AllDay, a.ActivityID, a.Type, a.Status,a.Priority, a.Title, a.DueDate, "+
				" a.CompletedDate, a.Details, a.Creator, a.Owner, a.ModifiedBy, a.Modified, a.Created, a.Location, "+
				" b.Name ActivityName, c.RecurrenceID, c.TimePeriod, c.Every, c.RecurrOn, c.Until, c.startdate,  "+
				" a.visibility visibility from activity a LEFT JOIN recurrence c ON a.ActivityID = c.ActivityID, "+
				" activitytype b,  opportunity o where  a.Type = b.TypeID and a.Type=3 and "+
				" a.ActivityID = o.ActivityID and o.AccountManager = ? and a.status != 2 and a.Start IS NOT NULL;");



    //and ((a.start < ? and a.end > ?) or (c.startdate < ? and c.until > ?)) union Select a.Start, a.End, a.AllDay, a.ActivityID, a.Type, a.Status,a.Priority, a.Title, a.DueDate, a.CompletedDate, a.Details, a.Creator, a.Owner, a.ModifiedBy, a.Modified, a.Created, a.Location, b.Name ActivityName, c.RecurrenceID, c.TimePeriod, c.Every, c.RecurrOn, c.Until, c.startdate from activity a LEFT JOIN recurrence c ON a.ActivityID = c.ActivityID, activitytype b, attendee d where a.Type = b.TypeID and a.ActivityID = d.ActivityID and d.IndividualID = ? and ((a.start < ? and a.end > ?) or (c.startdate < ? and c.until > ?))
    allSql.put("calendar.getallunscheduledactivity","Select a.Start, a.End, a.AllDay, a.ActivityID, a.Type, a.Status, a.Priority, a.Title, a.DueDate, a.CompletedDate, a.Details, a.Creator, a.Owner Owner, a.ModifiedBy, a.Modified, a.Created, a.Location, b.Name ActivityName, a.visibility visibility from activity a, activitytype b, activitystatus c where a.Type = b.TypeID and a.Owner = ? and a.status=c.statusid and c.name='Pending' and (Start IS NULL and End IS NULL)");
    //or (a.DueDate between ? AND ?))");
    //Start All Activities
    allSql.put("activity.allactivitiescreate","create TEMPORARY TABLE AllActivitiesList SELECT en1.ActivityID ActivityID , en1.Title Title ,at.Name Type,indv.individualid individualid,indv.FirstName FirstN, indv.LastName LastN,en1.created Created,en1.dueDate Due,en1.Start Start,en1.End End,ap.Name Priority,as1.Name Status,en1.visibility visibility FROM `activity` en1,  `activitystatus`  as1,`activitypriority` ap,`activitytype`  at,`individual` indv where 1=0;");

    allSql.put("activity.allactivitiesinsert1",
               "INSERT INTO AllActivitiesList(ActivityID, Title ,Type,created, Due, Start,End, visibility) " +
               "SELECT act.ActivityID, act.Title, act.Type, act.created, act.dueDate, act.Start ,act.End, act.visibility " +
               "FROM activity act WHERE act.visibility='PUBLIC' UNION " +
               "SELECT act.ActivityID, act.Title, act.Type, act.created, act.dueDate, act.Start, act.End, " +
               "act.visibility FROM activity act, attendee at WHERE at.activityId=act.activityId AND at.individualid=? " +
               "UNION SELECT ActivityID, Title, Type, created, dueDate, Start, End, visibility FROM activity " +
               "WHERE activity.owner=? UNION SELECT activity.ActivityID, Title, Type, created, dueDate, Start, End, " +
               "visibility FROM activity, activitylink  WHERE activity.ActivityID=activitylink.ActivityID AND " +
               "activitylink.RecordTypeID=2 AND activitylink.RecordID=? UNION SELECT activity.ActivityID, " +
               "activity.Title, Type, created, dueDate, Start, End, visibility FROM activity, taskassigned, task " +
               "WHERE taskassigned.taskid=task.activityid AND task.ActivityID=activity.ActivityID AND " +
               "taskassigned.AssignedTo=? ");

    allSql.put("activity.allactivitiesupdate1","UPDATE AllActivitiesList,`activity`,`activitystatus` SET AllActivitiesList.Status = `activitystatus`.Name WHERE `activitystatus`.StatusID = `activity`.Status and `activity`.ActivityID=AllActivitiesList.ActivityID;");
    allSql.put("activity.allactivitiesupdate2","UPDATE AllActivitiesList,`activity`,`activitypriority` SET AllActivitiesList.Priority = `activitypriority`.Name WHERE `activitypriority`.PriorityID = `activity`.Priority and `activity`.ActivityID=AllActivitiesList.ActivityID;");
    allSql.put("activity.allactivitiesupdate3","UPDATE AllActivitiesList,`activity`,`activitytype` SET AllActivitiesList.Type = `activitytype`.Name WHERE `activitytype`.TypeID = `activity`.Type and `activity`.ActivityID=AllActivitiesList.ActivityID;");
    allSql.put("activity.allactivitiesupdate4","UPDATE AllActivitiesList,`activity`,`individual` SET AllActivitiesList.individualid=`individual`.individualid,AllActivitiesList.FirstN = `individual`.FirstName, AllActivitiesList.LastN = `individual`.LastName WHERE `individual`.IndividualID = `activity`.creator and `activity`.ActivityID=AllActivitiesList.ActivityID;");
    allSql.put("activity.allactivitiesupdate5","UPDATE AllActivitiesList,`opportunity` SET AllActivitiesList.activityid = `opportunity`.opportunityid WHERE `opportunity`.activityid=AllActivitiesList.activityid; ");
    allSql.put("activity.allactivitiesdrop","drop table AllActivitiesList;");
    //End All Activities
    //Start My Activities
    allSql.put("activity.myactivitiescreate","CREATE TEMPORARY TABLE  MyActivitiesList SELECT en1.ActivityID ActivityID, en1.Title Title,  at.Name Type, indv.FirstName FirstN, indv.LastName LastN, en1.created  Created, en1.dueDate Due, en1.Start Start, en1.End End,ap.Name Priority, as1.Name Status, us.individualid, us.userid userid, NOW() AS Modified FROM `activity` en1, `activitystatus` as1, `activitypriority` ap, `user` us, `activitytype` at, `individual` indv WHERE 1=0;");
    allSql.put("activity.myactivitiesinsert","insert into  MyActivitiesList(activityId,individualId,userid)  select act.activityId,act.Creator, "+
																						"us.userid from user us,activity act where us.individualid=act.Creator and act.owner=? UNION  "+
																						"SELECT `activity`.ActivityID, `activity`.Creator ,`activity`.Owner FROM `activity`,`taskassigned`,`task` "+
																						"where `taskassigned`.taskid = `task`.activityid AND `task`.ActivityID = `activity`.ActivityID AND `taskassigned`.AssignedTo = ? ");

    allSql.put("activity.myactivitiesupdate1", "UPDATE MyActivitiesList my,`activity` SET my.Title=`activity`.title, my.created=`activity`.created, my.due=`activity`.duedate, my.start=`activity`.start, my.end=`activity`.end, my.Modified=`activity`.Modified WHERE `activity`.activityId=my.activityId;");
    allSql.put("activity.myactivitiesupdate2","UPDATE MyActivitiesList,`activity`,`activitystatus` SET MyActivitiesList.Status = `activitystatus`.Name WHERE `activitystatus`.StatusID = `activity`.Status and `activity`.ActivityID=MyActivitiesList.ActivityID; ");
    allSql.put("activity.myactivitiesupdate3","UPDATE MyActivitiesList,`activity`,`activitypriority` SET MyActivitiesList.Priority = `activitypriority`.Name WHERE `activitypriority`.PriorityID = `activity`.Priority and `activity`.ActivityID=MyActivitiesList.ActivityID; ");
    allSql.put("activity.myactivitiesupdate4","UPDATE MyActivitiesList,`activity`,`activitytype` SET MyActivitiesList.Type = `activitytype`.Name WHERE `activitytype`.TypeID = `activity`.Type and `activity`.ActivityID=MyActivitiesList.ActivityID; ");
    allSql.put("activity.myactivitiesupdate5","UPDATE MyActivitiesList,`individual` SET MyActivitiesList.FirstN = `individual`.FirstName, MyActivitiesList.LastN = `individual`.LastName WHERE `individual`.individualid=MyActivitiesList.individualid; ");
    allSql.put("activity.myactivitiesupdate6","UPDATE MyActivitiesList,`opportunity` SET MyActivitiesList.activityid = `opportunity`.opportunityid WHERE `opportunity`.activityid=MyActivitiesList.activityid; ");
    allSql.put("activity.myactivitiesdrop","drop table `MyActivitiesList`;");


    //End of My Activities
    allSql.put("activity.allappointmentlistcount","select count(*) as appointmentlistcount from appointmentlist");
    allSql.put("activity.allcalllistcount","select count(*) as calllistcount from Callslist");
    allSql.put("activity.allmeetinglistcount","select count(*) as meetinglistcount from Meetinglist");
    allSql.put("activity.alltodolistcount","select count(*) as todolistcount from ToDoList");
    allSql.put("activity.allnextactionlistcount","select count(*) as nextactionlistcount from NextList");

    allSql.put("activity.allactivitieslistcount","select count(*) as allactivitieslistcount from AllActivitiesList");
    allSql.put("activity.myactivitieslistcount","select count(*) as myactivitieslistcount from MyActivitiesList");

    // Parameter 1 : int  RecordTypeID ( 1 = Entity, 2 = Individual )
    // Parameter 2 : int  RecordID ( <EntityID>, <IndividualID> )
    allSql.put("activity.getrelatedactivity", "SELECT activity.ActivityID as id, activitytype.Name as type, activity.Title as title, activity.Created as created, activity.DueDate as duedate, CONCAT(individual.FirstName,' ',individual.LastName) as createdby, activitystatus.Name as status FROM activity LEFT OUTER JOIN activitylink ON activity.ActivityID = activitylink.ActivityID LEFT OUTER JOIN individual ON activity.Creator = individual.IndividualID LEFT OUTER JOIN activitystatus ON activity.Status = activitystatus.StatusID LEFT OUTER JOIN activitytype ON activity.Type = activitytype.TypeID WHERE (activitylink.RecordTypeID = ?) AND (activitylink.RecordID = ?)");


    allSql.put("email.emaillistcreatetable" ,"CREATE  TEMPORARY  TABLE `allemaillist` (`MessageId` int(11) default NULL,`MessageDate` datetime default NULL,`MailFrom` varchar(50) default NULL,`ReplyTo` varchar(25) default NULL,`Subject` text,`size` int(11) default NULL, `AttIndication` enum('YES','NO') default 'NO', `ReadIndication` enum('YES','NO') default 'NO')");
    allSql.put("email.insertintoemaillist","Insert Into `allemaillist`( MessageId,MessageDate,MailFrom,ReplyTo,Subject, size,  ReadIndication)SELECT em.MessageID, em.messagedate, em.mailfrom,em.replyto,em.subject, em.size , est.readstatus FROM `emailmessage` em,`emailstore`  est WHERE em.MessageID = est.MessageID");
    allSql.put("email.updateinsertintoemaillist"," Update `allemaillist`,attachment set AttIndication = 'YES' WHERE   `allemaillist`.MessageID = attachment.`MessageID`");
    allSql.put("email.emaillist","Select MessageId ,MessageDate,MailFrom,ReplyTo,Subject,size, AttIndication,  ReadIndication From   allemaillist");
    allSql.put("email.emaillistdroptable","drop table allemaillist ;");
  allSql.put( "email.getcreatetable", "create TEMPORARY TABLE emailmsg  SELECT em.MessageID  MessageID ,em.MailFrom ,  em.AccountID AccountID , em.MessageDate MessageDate ,em.Body Body, em.FromIndividual FromIndividual , em.Importance Importance, em.Priority Priority , em.Subject Subject ,em.Headers  Headers , at.AttachmentID AttachmentID FROM  `emailmessage` em, `attachment` at where 1=0;" );
  allSql.put( "email.getmailinsert", "insert into emailmsg ( MessageID ,MailFrom , AccountID,MessageDate,Body, FromIndividual, Importance,Priority ,Subject ,Headers ) select em.MessageID , em.MailFrom ,  em.AccountID , em.MessageDate,em.Body , em.FromIndividual, em.Importance , em.Priority , em.Subject,em.Headers From `emailmessage` em  where MessageID = ?;" );
  allSql.put( "email.getmailupdate1", "update emailmsg emsg ,`attachment` set emsg.AttachmentID =`attachment`.AttachmentID where `attachment`.MessageID = emsg.MessageID ;");
  allSql.put( "email.getmailselect","select * from emailmsg ;" );
  allSql.put( "email.getmaildrop","drop table emailmsg;" );
  allSql.put( "email.getmailaddressCC" , "select * from `emailrecipient` where MessageID = ? and RecipientType = 'CC' ;" );
  allSql.put( "email.getmailaddressTO" , "select * from `emailrecipient` where MessageID = ? and RecipientType = 'TO' ;" );
  allSql.put( "email.getmailaddressBCC" , "select * from `emailrecipient` where MessageID = ? and RecipientType = 'BCC' ;" );
   allSql.put( "email.selectAllemailrule", "select ActionName,TargetID ,emailrules.ruleid,rulename,description,enabledstatus,accountid,rulestatement from emailrules , emailaction where accountid =?  and enabledstatus='YES' and emailrules.ruleid = emailaction.ruleid  group by emailrules.ruleid;");
   allSql.put( "email.updateheader", "update emailmessage set Headers = ? where MessageID = ?");
   allSql.put( "email.getheader", "select MessageID,Headers from emailmessage where MessageID = ?");
  //For source
  allSql.put("common.addsource"," insert into source (sourceid,name) values (?,?)");
  allSql.put("common.updatesource","update source set name = ?  where sourceid = ?  ");
  allSql.put("common.deletesource", "delete from source  where sourceid = ? ");
  allSql.put("common.getsource"," SELECT name from source where sourceid=? ");
  allSql.put("common.getsourcelist", "SELECT sourceId, name FROM source");
  allSql.put("common.getallsource","select sourceid id, name from source");
  // SearchEJB
  allSql.put("common.getmasterdictionary"," SELECT cvtable1.name table1,cvtable2.name table2, field1.name field1, field2.name field2,cvjoin.cvjoin cvjoin, cvjoin.clause clause,cvjoin.othertablename FROM cvtable cvtable1, cvtable cvtable2, cvjoin cvjoin, field field1, field field2 WHERE   (cvjoin.tableid1 = cvtable1.tableid)   and (cvjoin.tableid2 = cvtable2.tableid)   and (field1.fieldid = cvjoin.fieldid1)   and (field2.fieldid = cvjoin.fieldid2) ");
  allSql.put("common.getfinalmapping"," SELECT finalmappings.module, finalmappings.table ,finalmappings.column FROM finalmappings  ");
  allSql.put("common.gettables","SELECT tableid, name FROM cvtable");
  //For login
  String sqlLogin = "SELECT ap.acknowledgedvisability ," + "ap.emailvisability," + "ap.acknowledgeddays," + "em.composestyle," + "ep.accountid," + "cp.activitytype AS cp_activitytype," + "act.activitytype AS act_activitytype," + "tp.visibility AS taskvisibility," + "op.visible AS opvisible," + "sp.visible AS spvisible,"
  + "np.visible AS newsvisible," + "np.days AS newsdays " + "FROM " + "alertpreference ap," + "activityportlet act," + "supportportlet sp," + "newsportlet np," + "opportunityportlet op," + "taskportlet tp," + "calendarportlet cp," + "emailportlet ep," + "emailpreference em " + "WHERE " + "ap.individualid = ep.individualid " + "and "
  + "ap.individualid = em.individualid " + "and " + "ap.individualid = cp.individualid " + "and " + "ap.individualid = tp.individualid " + "and " + "ap.individualid = op.individualid " + "and " + "ap.individualid = np.individualid " + "and " + "ap.individualid = sp.individualid " + "and " + "ap.individualid = act.individualid " + "and "
  + "ap.individualid = ? ";


    // Users Queries
    allSql.put("common.login"," select Name,IndividualID,Password,userstatus,usertype from user where Name = ? and usertype=? and userstatus='ENABLED'");
    allSql.put("common.login.passwd"," select password from user where Password = ? ");

    //For forgotten password
    allSql.put("user.getindividual","select IndividualID, UserID, password from user where name = ?");
    allSql.put("user.getinfo","select firstName, lastName from individual where individualID = ?");
    allSql.put("user.forgottenpassword"," select  moc.content from methodofcontact moc, mocrelate mr where moc.MOCID=mr.MOCID and moc.MOCType=1 and mr.ContactID=? ");

    //For indivdual details
    allSql.put("common.user.details"," select * from individual where  IndividualID = ? ");

    //For User Preferences.
    allSql.put("common.userpref.allpref",sqlLogin);
    allSql.put("common.userpref.userpreference","Select preference_name, preference_value, IndividualID from userpreference where IndividualID = ? and moduleid = 0");
    allSql.put("common.userpref.moduleids","select moduleid from modulepreference where individualid = ?");
    allSql.put("common.userpref.otherpreferences","Select PreferenceName, PreferenceValue, IndividualID from otherpreferences where IndividualID = ?");

    // User Queries
    allSql.put("user.insertuser","INSERT INTO user (Name,IndividualID,Password,userstatus,usertype) values (?,?,?,?,?)");
    allSql.put("user.getpwd","select UserId,Name,IndividualID,Password from user where IndividualID=?");
    allSql.put("user.changepwd","update user set Password=? where IndividualID=?");
    allSql.put("user.insertdefaultpreference", "INSERT INTO userpreference (individualid, moduleid, preference_name, preference_value,tag) SELECT ?, moduleid, preference_name, preference_value, tag from userpreferencedefault");
    allSql.put("user.listcount","select count(*) as allcountuser from user");
    allSql.put("user.getuser","select us.UserId,us.Name,us.IndividualID,us.Password,us.userstatus,us.usertype,indv.firstname,indv.lastname from user us,individual indv where us.individualid=indv.individualid and us.IndividualID=?");
    allSql.put("user.getUserType", "SELECT usertype FROM user WHERE IndividualID=?");
    allSql.put("user.deleteuser","DELETE user, usersecurityprofile FROM user,usersecurityprofile WHERE user.individualid = usersecurityprofile.individualid AND user.individualid = ?");
    allSql.put("user.deleteuserpreference","DELETE FROM userpreference WHERE individualid = ?");
    allSql.put("user.updateuser","update user set Name=?, Password=?,userstatus=?,usertype=? where IndividualID=?");

    allSql.put("authorization.getmoduletableowner","select moduleid,name,primarytable,ownerfield,primarykeyfield from module where name=? and primarytable !=''");
    allSql.put("authorization.getallmodules","SELECT moduleid, parentid, name FROM module;");
    allSql.put("authorization.insertsecurityprofile","INSERT INTO securityprofile (profilename) values (?)");
    allSql.put("authorization.updatesecurityprofile","update securityprofile set profilename = ? where profileid = ?");
    allSql.put("authorization.deleteallsecurityprofile","delete a, b from securityprofile a, usersecurityprofile  b where a.profileid=b.profileid and  a.profileid = ?");
    allSql.put("authorization.getsecurityprofilemodule","select c.moduleid, a.profileid, a.profilename,c.name, b.privilegelevel from securityprofile a, moduleauthorisation b,module c where a.profileid = b.profileid and b.moduleid = c.moduleid and a.profileid=?");
    allSql.put("authorization.getsecurityprofilefield","select  a.profileid, a.profilename,c.name, c.mapid as fieldid, b.privilegelevel,d.name as modulename from securityprofile a, fieldauthorisation b, modulefieldmapping c, module d where a.profileid = b.profileid and b.fieldid = c.mapid and c.moduleid = d.moduleid and a.profileid=? order by modulename");

    allSql.put("authorization.setallrecordspublicfordefaultprivileges","UPDATE userpreference SET preference_value = ? WHERE preference_name = 'allRecordsPublic' AND individualid = ?");
    allSql.put("authorization.getallrecordspublicfordefaultprivileges","SELECT preference_value FROM userpreference WHERE preference_name = 'allRecordsPublic' AND individualid = ?");
    allSql.put("authorization.deleteuserdefaultprivileges","DELETE FROM defaultprivilege WHERE OwnerId = ?");
    allSql.put("authorization.insertintopublicrecords","INSERT INTO publicrecords (moduleid,recordid) VALUES (?,?) ");
    allSql.put("authorization.deletefrompublicrecords","DELETE FROM publicrecords WHERE moduleid = ? AND recordid = ? ");
    allSql.put("authorization.getmoduleidbymodulename","SELECT moduleid FROM module WHERE name=?");
    allSql.put("authorization.getrecordfrompublicrecords","SELECT * FROM publicrecords WHERE moduleid = ? AND recordid = ?");
    allSql.put("authorization.deletefromrecordauthorization","DELETE FROM recordauthorisation where recordtypeid = ? AND recordid = ?");

    allSql.put("authorization.getuserallsecurityprofilemodule","select DISTINCT c.moduleid, c.name, b.privilegelevel from usersecurityprofile a,moduleauthorisation b, module c  where a.profileid = b.profileid and b.moduleid = c.moduleid and a.individualid=?");
    allSql.put("authorization.getuserallsecurityprofilefield","select distinct d.name modulename, c.name fieldname,min(b.privilegelevel) as privilegelevel from usersecurityprofile a,fieldauthorisation b, modulefieldmapping c,module d where a.profileid = b.profileid and b.fieldid = c.mapid and c.moduleid = d.moduleid and a.individualid=? group by modulename,fieldname");

    allSql.put("authorization.getusersecurityprofilemodule","select DISTINCT c.name, b.privilegelevel from usersecurityprofile a,moduleauthorisation b, module c  where a.profileid = b.profileid and b.moduleid = c.moduleid and a.individualid=? and c.name=?");
    allSql.put("authorization.getusersecurityprofilefield","select distinct d.name modulename, c.name fieldname,min(b.privilegelevel) as privilegelevel, c.listcolname from usersecurityprofile a,fieldauthorisation b, modulefieldmapping c,module d where a.profileid = b.profileid and b.fieldid = c.mapid and c.moduleid = d.moduleid and a.individualid=? and d.name=? group by modulename,fieldname");
    allSql.put("authorization.getusersecurityprofilefieldselective","select distinct d.name modulename, c.name fieldname,min(b.privilegelevel) as privilegelevel, c.listcolname,c.methodname  from usersecurityprofile a,fieldauthorisation b, modulefieldmapping c,module d where a.profileid = b.profileid and b.fieldid = c.mapid and c.moduleid = d.moduleid and a.individualid=? and d.name=? group by modulename,fieldname having privilegelevel=?");

    allSql.put("authorization.insertdefaultrecordpermission","insert into recordauthorisation(individualid,recordid,recordtypeid,privilegelevel) select ?,recordid,recordtypeid,privilegelevel from defaultrecordauthorisation");
    allSql.put("authorization.insertdefaultlistpermission", "INSERT INTO recordauthorisation (individualid, recordid, recordtypeid, privilegelevel) VALUES (?, 1, 32, 30)");
    allSql.put("authorization.insertdefaultuserpreferences", "INSERT INTO userpreference (individualid,preference_name,preference_value) VALUES (?, 'allRecordsPublic', 'Yes')");

    allSql.put("authorization.individualmarketinglistrecordpermission","insert into recordauthorisation(individualid,recordid,recordtypeid,privilegelevel) select ?,individualid,?,?  from individual where list=?");
    allSql.put("authorization.entitymarketinglistrecordpermission","insert into recordauthorisation(individualid,recordid,recordtypeid,privilegelevel) select ?,entityid,?,?  from entity where list=?");

    allSql.put("authorization.insertrecordpermission","insert into recordauthorisation(individualid,recordid,privilegelevel,recordtypeid)  select ?,?,?,a.moduleid from module a where a.name=?");

    allSql.put("authorization.updaterecordpermission","update recordauthorisation a, module b set a.privilegelevel = ?  where a.recordtypeid=b.moduleid and b.name=? and a.individualid=?");

    allSql.put("authorization.getrecordpermission","select a.individualid,a.recordtypeid,b.name,a.recordid,a.privilegelevel from recordauthorisation a, module b where a.recordtypeid=b.moduleid and b.name=? and recordid=?");
    allSql.put("authorization.getdefaultrecordpermission","select IndividualId,PrivilegeLevel from defaultprivilege where OwnerId=?");
    allSql.put("authorization.getuserrecordpermission","select a.individualid,a.recordtypeid,b.name,a.recordid,a.privilegelevel from recordauthorisation a, module b where a.recordtypeid=b.moduleid and b.name=? and recordid=? and a.individualid=?");
    allSql.put("authorization.deleterecordpermission","DELETE recordauthorisation FROM recordauthorisation,module AS b WHERE b.moduleid = recordauthorisation.recordtypeid AND b.name=? AND recordauthorisation.recordid=?");

	allSql.put("authorization.deleteindividualrecordpermission","delete recordauthorisation from recordauthorisation,module b,individual i where b.moduleid = recordauthorisation.recordtypeid and i.individualid= recordauthorisation.recordid and b.name=? and i.List=?");
	allSql.put("authorization.deleteentityrecordpermission","delete recordauthorisation from recordauthorisation,module b, entity i where b.moduleid = recordauthorisation.recordtypeid and i.entityid= recordauthorisation.recordid and b.name=? and i.List=?");

    allSql.put("authorization.deletedefaultpermissions","delete from defaultprivilege where OwnerId = ?");
    allSql.put("authorization.insertdefaultpermissions","insert into defaultprivilege(OwnerId,IndividualId,PrivilegeLevel) values( ?, ?, ?)");
    allSql.put("authorization.getdefaultpermissions","select IndividualId, PrivilegeLevel from defaultprivilege where OwnerId = ?");
    allSql.put("authorization.getuserownerbyrecord","select  ?  from  ?  where  ? = ? ");
    allSql.put("authorization.getuserdefaultrecordpermission","select PrivilegeLevel from defaultprivilege where OwnerId=? and IndividualId=?");

    allSql.put("authorization.insertmoduleauthorization","INSERT INTO moduleauthorisation (profileid,privilegelevel,moduleid) values (?,?,?)");
    allSql.put("authorization.deletemoduleauthorization","delete from moduleauthorisation where profileid=?");

    allSql.put("authorization.insertfieldauthorization","INSERT INTO fieldauthorisation (profileid,privilegelevel,fieldid) select ?,?,b.mapid as fieldid from module a, modulefieldmapping b where a.moduleid=b.moduleid and a.name=? and b.name=?");
    allSql.put("authorization.deletefieldauthorization","delete from fieldauthorisation where profileid=?");

    allSql.put("authorization.insertusersecurityprofile","INSERT INTO usersecurityprofile (profileid,individualid) values (?,?)");
    allSql.put("authorization.getusersecurityprofile","select a.profileid,a.profilename from securityprofile a, usersecurityprofile b where a.profileid = b.profileid and b.individualid=?");

    allSql.put("authorization.deletesecurityprofile","delete from securityprofile where profileid=?");
    allSql.put("authorization.deleteusersecurityprofile","delete from usersecurityprofile where profileid=?");
    allSql.put("securityprofile.allsecurityprofilecount","select count(profileid) allsecurityprofilecount from securityprofile");
    //Start Contact History Queries
    allSql.put("history.historylistcreatetable","create temporary table historylist select hst.historyid historyid, md.name recordtype, hst.recordid recordid, hst.recordName, hst.referenceactivityid referenceactivityid, hst.date date, concat(indv.FirstName,' ',indv.lastName) user, hsttyp.historytype action, acttyp.name type, act.Title reference, act.Notes notes from history hst , individual indv, historytype hsttyp, activity act, activitytype acttyp , module md where 1=0");
    allSql.put("history.advhistorylistinsert","insert into historylist (historyid, recordtype, recordid, recordName, referenceactivityid, date, user, action) select hst.historyid ,md.name, hst.recordid , hst.recordName, hst.referenceactivityid, hst.date , concat(indv.FirstName,' ',indv.lastName) , hsttyp.historytype from history hst , individual indv, historytype hsttyp, module md, historyListSearch hls where hst.individualid = indv.IndividualID and hst.operation = hsttyp.historytypeid and hst.recordtypeid = md.moduleid and hls.historyid = hst.historyid");
    allSql.put("history.historylistinsert","insert into historylist (historyid,recordtype, recordid, recordName, referenceactivityid, date,user, action) select hst.historyid, md.name, hst.recordid, hst.recordName, hst.referenceactivityid, hst.date , concat(indv.FirstName,' ',indv.lastName) , hsttyp.historytype from history hst LEFT JOIN individual indv ON hst.individualid = indv.IndividualID,  historytype hsttyp, module md where hst.operation = hsttyp.historytypeid and hst.recordtypeid = md.moduleid");
    allSql.put("history.historylistupdate","update historylist hst, activity act, activitytype acttyp set hst.type = acttyp.Name, hst.reference = act.Title, hst.notes = act.Notes, hst.date = act.CompletedDate  where hst.referenceactivityid = act.ActivityID and act.Type = acttyp.TypeID and hst.action=?");
    allSql.put("history.historylistgetcount","select count(historyid) allhistorycount from historylist");
    allSql.put("history.historylistdroptable","drop table historylist;");
    allSql.put("history.deleterecord","delete from history where operation=? and recordtypeid=? and recordid=?");
    allSql.put("history.seleterecord","select ht.historytype, max(a.date), date historydate from history a,  module m ,historytype ht where m.moduleid = a.recordtypeid and ht.historytypeid = a.operation and m.name=? and a.recordid=?  group by ht.historytype having (ht.historytype=? or ht.historytype=?) order by a.operation");
    allSql.put("history.insertrecord","insert into history (recordid, recordtypeid, recordName, operation,individualid,referenceactivityid) values (?,?,?,?,?,?)");
    //End Contact History Queries

    //Start Literature Query -- PK

    allSql.put("literature.getliterature","select lt.LiteratureID LiteratureID, lt.Title Title, cvf.Title File from cvfile cvf, literature lt where lt.FileID = cvf.FileID");
    allSql.put("literature.getliteraturecount","select count(LiteratureID) literaturecount from literature");
    allSql.put("literature.addliterature","insert into literature(Title,FileID,Description) values(?,?,?)");
    allSql.put("literature.deleteliterature","delete from literature where LiteratureID=?");
    allSql.put("literature.selectliterature","select lt.Title Title, cvf.Title FileName, lt.FileID FileID from cvfile cvf, literature lt where lt.FileID = cvf.FileID and lt.LiteratureID=?");
    allSql.put("literature.updateliteraturetitle","update literature set Title=? where LiteratureID=?");
    allSql.put("literature.updateliterature","update literature set Title=?, FileID=? where LiteratureID=?");
    //END of Literature query
    //Start Company News Query
    allSql.put("companynews.insertnews","insert into companynews(FileID,DateFrom,DateTo) values(?,?,?)");
    allSql.put("companynews.updatenews","update companynews set DateFrom = ? , DateTo = ? where FileId = ?");
    allSql.put("companynews.deletenews","delete from companynews where FileId = ?");
    allSql.put("companynews.getnew","select DateFrom, DateTo from companynews where FileId = ?");
    allSql.put("companynews.getnews","select distinct a.FileID,Description,Title from cvfile a, companynews b where (b.DateFrom <= concat(CURDATE())) and (b.DateTo >= concat(CURDATE()) and (a.FileID = b.FileID)) ");
    //End Company News Query
    // File Queries
    allSql.put("file.insertfolder","insert into cvfolder(Name,Description,Parent,LocationID, owner,IsSystem,visibility,CreatedBy,CreatedOn, ModifiedBy) values(?,?,?,?,?,?,?,?,NOW(),?)");
    allSql.put("file.updatefolder","update cvfolder set Name = ?, description = ?, parent = ?, ModifiedBy = ?, visibility = ?, owner = ? where FolderID = ?");
    allSql.put("file.getfolder","select FolderID,Name,Description,Parent,FullPath, CreatedBy, ModifiedBy,CreatedOn,ModifiedOn,b.LocationID,b.Detail, owner, IsSystem, visibility from cvfolder a, cvfolderlocation b where a.LocationID = b.LocationID and FolderID = ?");
    allSql.put("file.getfolderbyname","select FolderID,Name,Description,Parent,FullPath, CreatedBy, ModifiedBy,CreatedOn,ModifiedOn,b.LocationID,b.Detail,owner,IsSystem, visibility from cvfolder a, cvfolderlocation b where a.LocationID = b.LocationID and trim(Name) = ? and Parent = ?");
    allSql.put("file.gethomefolder","select up.Preference_Value as HomeFolderID from userpreference up where up.individualid=? and up.Preference_Name='DEFAULTFOLDERID'");
    allSql.put("file.insertfile","insert into cvfile (Title, Description, Name, Owner, FileSize, Version, Status, visibility, Author,RelateEntity,RelateIndividual, IsTemporary, CustomerView, Creator, Created ,UpdatedBy) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?)");
    allSql.put("file.updatefile","update cvfile set Title = ?, Description = ?, Name = ?, FileSize = ?,Version = ?,Status = ?,visibility = ?,Author = ?,UpdatedBy = ?,RelateEntity=?,RelateIndividual=?, CustomerView = ? where FileID = ?");
    allSql.put("file.insertfilefolder","insert into cvfilefolder (fileid,folderid,referencetype) values(?,?,?)");
    allSql.put("file.getfile","SELECT a.FileID, a.Title, a.Name, a.Description,a.Owner,a.Creator,a.UpdatedBy, a.Created, a.Updated, a.FileSize, a.Version, a.Status, a.visibility, a.Author, a.CustomerView, c.FolderID, c.Name FolderName, c.Description FolderDescription, c.Parent,a.RelateEntity,a.RelateIndividual FROM cvfile a,cvfilefolder b, cvfolder c WHERE a.FileID = b.fileid and c.FolderID = b.folderid and b.referencetype='PHYSICAL' and a.FileID = ?");
    allSql.put("file.getvirtualfilefolder","SELECT a.FileID,c.FolderID,c.Name,c.Parent,c.Description,b.referencetype FROM cvfile a,cvfilefolder b,cvfolder c where a.FileID = b.fileid and b.folderid = c.FolderID and b.referencetype = 'VIRTUAL' and b.fileid = ?");
    allSql.put("file.deleteallfilefolder","delete from cvfilefolder where fileid = ?");
    allSql.put("file.commitemailattachment","update cvfile set name = ?, IsTemporary='NO' where FileID = ?");
    allSql.put("file.deletefilefolder","delete from cvfilefolder  where folderid=?");
    allSql.put("file.deletefile","delete cvfile from cvfile,cvfilefolder where cvfile.fileid=cvfilefolder.fileid and cvfilefolder.folderid=?;");
    allSql.put("file.deletefolder","delete cvfolder from cvfolder where folderid=? ;");
    allSql.put("file.selectfoldlerfiles","select fileid as fileid,referencetype as referenceType from cvfilefolder ffd where ffd.folderid=? ;");
    allSql.put("file.selectfiletype","select referencetype as referenceType from cvfilefolder ffd where ffd.fileid=? and ffd.folderid=? ;");
    allSql.put("file.deletephysicalfile","delete cvfile from cvfile where fileid=? ;");
    allSql.put("file.deletevirtualfile","delete cvfilefolder from cvfilefolder where fileid=? and folderid=? ;");
    allSql.put("file.deleteallvirtualfiles","delete cvfilefolder from cvfilefolder where fileid=? ;");
    allSql.put("file.getparentinfo","select b.FolderID as parentid,b.name as parentname,b.IsSystem as system from cvfolder a, cvfolder b where a.parent = b.FolderID and a.folderid=?;");
    allSql.put("file.getcurfoldername","select name as name,IsSystem as system from cvfolder where folderid=?;");
    allSql.put("file.getuserfolders","select cv.name as name,cv.folderid as folderid,cv.parent as parent, cf.name as parentname  from cvfolder cv, cvfolder cf  where cv.owner= ? and  cv.parent=cf.folderid and cv.parent !=4 and cv.parent !=2 and cv.parent != ? and cv.IsSystem='FALSE' order by cv.parent,cv.name");
    allSql.put("file.getpublicfolders","SELECT c.name as name,c.folderid as folderid,c.parent as parent, p.name as parentname  from cvfolder c, cvfolder p WHERE c.parent = p.folderid and c.parent = ? and c.IsSystem='FALSE' order by c.parent,c.name");
    allSql.put("file.getsubfolders","select folderid as folderid from cvfolder where parent=?;");
    allSql.put("file.getallfolders","select cv.name as name,cv.folderid as folderid,cv.parent as parent,cf.name as parentname from cvfolder cv, cvfolder cf, folderaccess fa  where cv.IsSystem='FALSE' and  cv.parent=cf.folderid and cf.folderid=fa.folderid order by cv.parent,cv.name");
    allSql.put("file.getallfoldersnonsystem","select  cv.name as name,cv.folderid as folderid,cv.parent as parent,cv.visibility,cf.name as parentname from cvfolder cv, cvfolder cf, folderaccess fa  where cv.IsSystem='FALSE' and cv.visibility='PUBLIC' and cv.parent=cf.folderid and cf.folderid=fa.folderid  group by 1 order by cv.parent,cv.folderid");
    //duplicate folder
    allSql.put("file.getsubfolderdetails","select folderid as folderid ,name as name,description as description,parent as parent,createdby as createdby, createdOn as createdon from cvfolder where parent=?;");
    allSql.put("file.fileentries","insert into cvfilefolder(fileid,folderid,referencetype) values(?,?,?);");
    allSql.put("file.selectallfiles","select fileid as fileid,folderid as folderid,referencetype as referencetype from cvfilefolder where folderid=?;");
    allSql.put("file.getuserfiles","select 'FOLDER' as FileFolder,indv.individualid as individualid,fld.folderid ID,fld.name Name,fld.description Description,fld.createdOn Created,fld.ModifiedOn Updated,concat(indv.FirstName,'',indv.LastName) 'Created BY' from individual indv,cvfolder fld where indv.individualid=fld.owner and fld.owner=?  union select 'FILE' as FileFolder,indv.individualid as individualid,fil.fileid ID,fil.name Name,fil.description Description,fil.created Created,fil.updated Updated,concat(indv.FirstName,'',indv.LastName) 'Created BY' from cvfile fil ,individual indv,cvfilefolder fld  where indv.individualid=fil.owner and fld.fileid=fil.fileid and fil.owner=? ");
    allSql.put("file.getindividualfiles","select 'FILE' as FileFolder,indv.individualid as individualid,fil.fileid ID,fil.name Name,fil.description Description,fil.created Created,fil.updated Updated,concat(indv.FirstName,'',indv.LastName) 'Created BY' from cvfile fil ,individual indv,cvfilefolder fld  where indv.individualid=fil.owner and fld.fileid=fil.fileid and (fil.owner=? or fil.RelateIndividual=?) ");
    // end files
    allSql.put( "email.getcreatetable", "create TEMPORARY TABLE emailmsg  SELECT em.MessageID  MessageID ,em.MailFrom ,  em.AccountID AccountID , em.MessageDate MessageDate ,em.Body Body, em.FromIndividual FromIndividual , em.Importance Importance, em.Priority Priority , em.Subject Subject ,em.Headers  Headers , at.AttachmentID AttachmentID FROM  `emailmessage` em, `attachment` at where 1=0;" );
      allSql.put( "email.getmailinsert", "insert into emailmsg ( MessageID ,MailFrom , AccountID,MessageDate,Body, FromIndividual, Importance,Priority ,Subject ,Headers ) select em.MessageID , em.MailFrom ,  em.AccountID , em.MessageDate,em.Body , em.FromIndividual, em.Importance , em.Priority , em.Subject,em.Headers From `emailmessage` em  where MessageID = ?;" );
      allSql.put( "email.getmailupdate1", "update emailmsg emsg ,`attachment` set emsg.AttachmentID =`attachment`.AttachmentID where `attachment`.MessageID = emsg.MessageID ;");
      allSql.put( "email.getmailselect","select * from emailmsg ;" );
      allSql.put( "email.getmaildrop","drop table emailmsg;" );
      allSql.put( "email.getmailaddressCC" , "select * from `emailrecipient` where MessageID = ? and RecipientType = 'CC' ;" );
      allSql.put( "email.getmailaddressTO" , "select * from `emailrecipient` where MessageID = ? and RecipientType = 'TO' ;" );
      allSql.put( "email.getmailaddressBCC" , "select * from `emailrecipient` where MessageID = ? and RecipientType = 'BCC' ;" );

      // save draft
      allSql.put( "email.savedraft1","insert into emailmessage( MessageDate, MailFrom, Subject, ReplyTo, Body, Headers, AccountID ,owner) values( NOW() ,?,?,?,?,?,?,? )");
      allSql.put("email.savedraft2","insert into emailstore( EmailFolder , MessageID , ReadStatus ) values ( ?,?,?) " );
      allSql.put("email.savedraft3","insert into emailrecipient(  MessageID , Address , RecipientType ,RecipientIsGroup ) values( ?,?,?,? )" );
      allSql.put("email.savedraft4","insert into emailstore( EmailFolder , MessageID , ReadStatus ) select FolderID ,? ,? from emailfolder where AccountID = ? and Name='Sent'");
      allSql.put("email.savedraft5","insert into emailstore( EmailFolder , MessageID , ReadStatus ) select FolderID ,? ,? from emailfolder where AccountID = ? and Name='Inbox'");
      allSql.put("email.savedraft6","insert into emailstore( EmailFolder , MessageID , ReadStatus ) select FolderID ,? ,? from emailfolder where AccountID = ? and Name='Delete'");
      allSql.put( "email.savedraftattchment","insert into attachment (  MessageID , FileName , FileID  ) values ( ?,?,?) " );

      allSql.put( "email.updatedraft1","update emailmessage set MessageDate = NOW(), MailFrom=? , Subject=?, Body=? , AccountID =? where MessageID = ? " );
      allSql.put( "email.deletedraft1","delete from emailstore   where  MessageID = ? " );
      allSql.put( "email.deletedraft2","delete from emailrecipient  where   MessageID = ?  " );
      allSql.put( "email.deletedraftattchment","delete from attachment where  MessageID = ?  " );
      // create folder
      allSql.put( "email.getcreatefolder","insert into emailfolder(  Parent , AccountID , Name , Ftype  ) values( ?,?,?,? )" );
      allSql.put( "email.geteditfolder", "update `emailfolder` set parent = ?, name = ? where folderid = ? and AccountID = ?");
      allSql.put( "email.checkfolderpresence", "select count(*) as foldercount from `emailfolder` where parent = ? and Name = ? and AccountID= ?" );
      allSql.put("email.emaillist","select em.messageID, em.messagedate, em.mailfrom, em.replyto, em.subject, em.size, es.readstatus, count(at.attachmentID) noofAttachments from `emailstore` es, `emailmessage` em left outer join `attachment` at on em.messageID = at.messageID where es.messageid = em.messageID and es.emailfolder = ? group by em.messageid ");
      allSql.put("email.emaillistTo","select er.RecipientID, er.Address, er.RecipientIsGroup from `emailrecipient` er, `emailmessage` em, `emailstore` es where es.emailfolder = ? and er.RecipientType = 'TO' and em.messageid = er.messageid and em.messageid = es.messageid ");
	allSql.put("email.emaillistfolder","select ea.accountID, ea.name accountName, ea.smtpserver smtpserver , "+
				"ea.address, ef.folderid, ef.parent, ef.name FolderName, ef.ftype, "+
				"ea.Default, -1 allMesages, count(es.messageid) unreadmessages , "+
				"ea.Signature signature, tef.name parentname from `emailaccount` ea, `emailfolder` ef , `emailfolder`  tef "+
				"left outer join `emailstore` es on es.emailfolder = ef.folderid "+
				"where es.readstatus='NO'  and es.MessageID != 0  and ea.accountID = ef.accountID  and ef.parent = tef.folderid "+
				"and ea.owner = ? group by ef.folderid  union  "+
				"select ea.accountID, ea.name accountName, ea.smtpserver smtpserver, "+
				"ea.address, ef.folderid, ef.parent, ef.name FolderName, ef.ftype, "+
				"ea.Default, count(es.messageid) allMesages, 0 unreadmessages , ea.Signature "+
				"signature, tef.name parentname from `emailaccount` ea, `emailfolder` ef , `emailfolder`  tef left outer join `emailstore` "+
				"es on es.emailfolder = ef.folderid and es.MessageID != 0 where ea.accountID = ef.accountID and "+
				"ea.owner = ?   and  ef.parent = tef.folderid group by ef.folderid order by ea.accountID ,ef.folderid ,allMesages");

      allSql.put("email.enablerule", "update `emailrules` set enabledstatus = ?  where ruleid = ? ");
      allSql.put("email.deleterule" ,"delete from `emailrules` where ruleid = ? ");
      allSql.put("email.updatelastuid" ,"update emailaccount set lastuid = ? where accountid = ? ");
	  allSql.put( "email.createnewemailaccount", "insert into emailaccount(  address , leaveonserver , login , name , owner , password, replyto ,servertype , signature, smtpserver ,mailserver,Port) values( ?,?,?,?,?,?,?,?,?,?,?,?  )" );
	  allSql.put( "email.editemailaccount","update emailaccount set  address = ? , leaveonserver= ? , login= ?  , name= ? , owner= ? , password= ? , replyto= ? ,servertype= ? , signature= ?, smtpserver= ?, mailserver= ? , Port= ? where AccountID = ?" );
	  allSql.put( "email.checkemailaccount","select accountid, Address from emailaccount where owner=? ");
	  allSql.put( "email.updatedefaultemailfolder","update userpreference set preference_value= ? where individualid = ? and moduleid = 1 and preference_name='emailaccountid'");
	  allSql.put( "email.getdefaultemailaccount","select preference_value from userpreference where individualid = ? and moduleid = 1 and preference_name='emailaccountid'");
      allSql.put( "email.movemailupdate",   "update emailstore set emailfolder = ? where emailfolder = ? and messageid = ? ;");
      allSql.put( "email.Markreadupdate",   "update emailstore set ReadStatus = ?  where emailfolder = ? and messageid = ? ;");
      allSql.put( "email.deletemailstore",    "delete from emailstore where EmailFolder = ? and Messageid = ? ;");
      allSql.put( "email.selectemailstore", "select * from emailstore where EmailFolder = ? and Messageid = ? ;");
      allSql.put( "email.deleteattachment", "delete from attachment where Messageid = ? ;");
      allSql.put( "email.deleteemailrecipient", "delete from emailrecipient where Messageid = ? ;");
      allSql.put( "email.deleteemailmessage", "delete from emailmessage where Messageid = ? ;");
      allSql.put( "email.movemailupdate1",   "update emailstore set emailfolder = ? where emailfolder = ? ;");
      allSql.put( "email.deleteemailfolder", "delete from emailfolder where folderID = ? ;");
      allSql.put( "email.insertrule", "insert into emailrules (rulename,description,enabledstatus,accountid,rulestatement,flag) values(?, ?, ?, ?, ?, ?);");
      allSql.put( "email.insertemailaction", "insert into emailaction (ruleid,actionname,targetiD) values(?, ?, ?);");
      allSql.put( "email.updaterule", "update emailrules set rulename = ?,description = ?,enabledstatus = ?,accountid = ?,rulestatement = ? where ruleid = ?;");
      allSql.put( "email.deleteemailaction", "delete from emailaction where ruleid = ?;");
      allSql.put( "email.selectemailrule", "select ruleid,rulename,description,enabledstatus,accountid,rulestatement from emailrules where ruleid = ?;");
      allSql.put( "email.selectemailaction", "select ruleid,ActionName,TargetID from emailaction where ruleid = ?;");
      allSql.put( "email.getdefaultemailaccountid","select ea.AccountID from EmailAccount ea, Individual ind where ind.individualid=ea.owner and ind.individualid= ?;");
      allSql.put( "email.getallaccounts", "select accountid, owner, name, login, password, smtpserver,mailserver ,  servertype, leaveonserver, address, replyto, signature from emailaccount order by servertype;");
      //attachment
      allSql.put( "email.getallattachments","SELECT * FROM `attachment` where messageid=?" );
      allSql.put( "email.getemailaccountforedit", "select * from emailaccount where AccountID = ?" );
      allSql.put( "email.deleteemailaccount" , "delete from emailaccount where AccountID = ?" );
      //Note DML
      allSql.put("note.insertnote","insert into note(Title,Detail,priority,Owner,RelateEntity, RelateIndividual, Creator,DateCreated) values(?,?,?,?,?,?,?,NOW())");
      allSql.put("note.updatenote","update note set Title = ?, Detail = ?, priority = ?, RelateEntity = ?, RelateIndividual = ?, UpdatedBy = ? where NoteID = ?");
      allSql.put("note.getnote","select NoteID,Title,Detail,priority,Owner, Creator, UpdatedBy,DateCreated,DateUpdated,RelateEntity,RelateIndividual from note where NoteID = ?");
      allSql.put("note.allnotecount","select  count(*) as allnotecount from note nt left outer join individual indv on indv.individualid=nt.owner ");
      allSql.put("note.mynotecount","select count(*) as mynotecount from note nt left outer join  individual indv on indv.individualid=nt.owner where nt.owner=? ");
      //added by Chuck Durham  10/8/2003 - used for Entity Detail bottom listing
      allSql.put("note.entitynotecount","select count(*) as mynotecount from note nt left outer join  individual indv on indv.individualid=nt.owner where nt.RelateEntity=? ");

      // Note end

      allSql.put("note.deletenote","delete from note where noteid=?");
      //added by himansu
      String moduleauth = "SELECT a.moduleid ,"+
                "b.name ,a.privilegelevel "+
                "FROM "+
                "moduleauthorisation a,"+
                "module b "+
                "WHERE "+
                "a.moduleid = b.moduleid and a.individualid = ?";
      String allauth = "select b.moduleid AS modid ,b.privilegelevel AS modulePrev,"+
      "d.recordid AS recid,d.privilegelevel AS  recordPrev,e.fieldid AS fid,e.privilegelevel AS fieldPrev "+
      "from "+
      "moduleauthorisation b,recordauthorisation d,fieldauthorisation e "+
      "where "+
      "b.individualid = d.individualid and  e.individualid = b.individualid "+
      "and  b.individualid = ?";

      String moduleids = "select tableid from cvtable where name = ?";
      allSql.put( "common.moduleauthorisation",moduleauth);
      allSql.put( "common.getmoduleid",moduleids);
      String recordauth = "select c.recordid,a.fieldid,a.name,b.privilegelevel "+
                "from field a,fieldauthorisation b,record c "+
                "where b.fieldid = a.fieldid and a.tableid = c.parentid "+
                "and a.tableid = ? ";
    //  allSql.put( "common.fieldauthorisation", "select accountid, owner, name, login, password, smtpserver, servertype, leaveonserver, address, replyto, signature from emailaccount order by servertype;");
      allSql.put( "common.recordauth",recordauth);
      allSql.put( "common.allauthorisation",allauth);


      allSql.put( "email.newemailaccountfolder" , "insert into emailfolder ( Parent , AccountID , Name , Ftype ) values ( ?,?,?,? ) " );

      // view - added by ashwin
      // new view
      allSql.put("view.insertview", "insert into listviews (listtype, viewname, sortmember, sorttype, searchid, searchtype, noofrecords, creatorid, ownerid) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
      // new viewcolumn
      allSql.put("view.insertviewcolumn","insert into viewcolumns (viewid, columnname, columnorder) values (?, ?, ?)");
      // get view
      allSql.put("view.getview", "select viewid, listtype, viewname, sortmember, sorttype, listviews.searchid, searchtype, noofrecords, ownerid from listviews where viewid = ? order by viewid");
      // get viewcolumn
      allSql.put("view.getcolumnview", "select columnname, columnorder from viewcolumns where viewid = ? order by columnorder");
      // get all search
      allSql.put("view.getownersearch", "select searchid, searchname from search where owner = ?");
      // update view
      allSql.put("view.updateview", "update listviews set listtype = ?, viewname = ?, sortmember = ?, sorttype = ?, searchid = ?, searchtype = ?, noofrecords = ? where viewid = ?");
      allSql.put("view.deleteviewcolumn", "delete from viewcolumns where viewid = ?");
      // delete view
      allSql.put("view.deleteview", "delete from listviews where viewid = ?");
      // get all columns
      allSql.put("view.getallcolumn", "select columnname,columnorder from listcolumns where listtype = ? order by columnorder");

      // get all owner view combo data
      // quick fix of 2 sql
      // allSql.put("view.getownertableview", "select listviews.viewid, listviews.viewname from listviews where ownerid = ? and listviews.listtype = ?");
      allSql.put("view.getowneruniquedefaultview", "select distinct listpreferences.defaultviewid, listviews.viewname from listviews, listpreferences where ownerid = ? and listpreferences.listtype = ? and listpreferences.defaultviewid = listviews.viewid");
      // get owner all views for that listtype
      allSql.put("view.getownernondefaultview", "select listviews.viewid, listviews.viewname from listviews where ownerid = ? and listviews.listtype = ?");

      // get all list types
      allSql.put("view.getalllisttypes", "select typename from listtypes order by typename");
      // get owner default view from list preference to populate user object - login ejb
      allSql.put("view.getownerallview", "select listviews.viewid, listviews.listtype, 0 as individualid, listviews.sorttype, listviews.sortmember as sortelement, listviews.noofrecords as recordsperpage, listviews.viewname, listviews.searchid from listviews, defaultviews where defaultviews.listtype = ? and ownerid is null and listviews.viewid = defaultviews.viewid union select viewid,listtype,ownerid,sorttype,sortmember,noofrecords, listviews.viewname, listviews.searchid from listviews where listtype = ? and ownerid = ?");
      // get owner default columns for required listtype - login ejb
      // allSql.put("view.getownerallcolumn", "select columnorder, columnname from viewcolumns, listviews where viewcolumns.viewid = ? and listviews.listtype = ? and ownerid = ? and listviews.viewid = viewcolumns.viewid union select columnorder, columnname from viewcolumns, listviews where viewcolumns.viewid = ? and listviews.listtype = ? and listviews.viewid = viewcolumns.viewid order by columnorder");
      allSql.put("view.getownerallcolumn", "select columnorder, columnname from viewcolumns, listviews where viewcolumns.viewid = ? and listviews.listtype = ? and listviews.viewid = viewcolumns.viewid order by columnorder");
      // get user default and then system default view - login ejb
      allSql.put("view.getownerdefaultview", "select viewid, listviews.listtype, ownerid as individualid, listviews.sorttype, sortmember as sortelement, noofrecords as recordsperpage, listviews.viewname from listviews, listpreferences where listviews.listtype = ? and ownerid = ? and listviews.viewid = listpreferences.defaultviewid union select listviews.viewid, listviews.listtype, 0 as individualid, listviews.sorttype, listviews.sortmember as sortelement, listviews.noofrecords as recordsperpage, listviews.viewname from listviews, defaultviews where defaultviews.listtype = ? and ownerid is null and listviews.viewid = defaultviews.viewid");
      // get user view
      allSql.put("view.getviewinfo", "select sorttype, sortmember, noofrecords,searchid from listviews where viewid = ?");
      // update user default view
      allSql.put("view.updateuserdefaultview", "update listpreferences set sorttype = ?, sortelement = ?, recordsperpage = ?, defaultviewid = ? where individualid = ? and listtype = ?");
      // get system default view
      allSql.put("view.getsystemdefaultview", "select viewid from defaultviews where listtype = ?");
      // get user default view
      allSql.put("view.getuserdefaultview", "select defaultviewid from listpreferences where individualid = ? and listType = ?");
      // update list preference sort info
      allSql.put("view.updatelistpreferencesort", "update listpreferences set sorttype = ?, sortelement = ? where defaultviewid = ?");
      // update list view sort info
      allSql.put("view.updatelistviewsort", "update listviews set sorttype = ?, sortmember = ? where viewid = ?");
      // update list preference record perpage info
      allSql.put("view.updatelistpreferencerpp", "update listpreferences set recordsperpage = ? where defaultviewid = ?");
      // update list view record perpage info
      allSql.put("view.updatelistviewrpp", "update listviews set noofrecords = ? where viewid = ?");
      // end of view

      // added by Sunita
      // Literature
      allSql.put( "marketing.insertliteraturerequest", "insert into literaturerequest (activityid , literatureid , title ,description , requestedby , deliveryMethod) values (?,?,?,?,?,?)");
      allSql.put( "marketing.insertactivity", "insert into activity(status, title,duedate, details, creator,owner,created,type) values (?,?,?,?,?,?,NOW(),?)");
      allSql.put( "marketing.getallliterature", "select literatureid , title from literature");
      allSql.put( "marketing.getalldeliverymethod", "select deliveryid , name from deliverymethod");
      allSql.put( "marketing.getallactivitystatus", "select statusid , name from activitystatus");
      allSql.put( "marketing.getliteraturedetails", "select lr.literatureid literatureid, lr.title title ,lr.description description,   "+
													"dm.name deliveryname, l.title literaturetitle,lr.deliverymethod deliverymethod,   "+
													"lr.requestedby requestedby, concat(req.firstname, ' ' , req.lastname) requestedbyname   "+
													"from literaturerequest lr,literature l, individual req, deliverymethod dm where    "+
													"lr.requestedby = req.individualid and lr.literatureid = l.literatureid and  "+
													"lr.activityid = ? and dm.deliveryid = lr.deliverymethod ");

      allSql.put( "marketing.deleteliteraturerequest", "delete from literaturerequest where activityid = ?");
      allSql.put( "marketing.deleteactivity", "delete from activity where activityid = ?");
      allSql.put( "marketing.updateactivity", "update activity set status = ? , title = ? ,duedate = ? , details = ? , creator = ? , owner = ? where activityid = ?");

      // Event
      allSql.put( "marketing.insertevent", "insert into event (title , detail , start , end , formember , owner , creator , maxattendees , created) values(?,?,?,?,?,?,?,?,NOW())");
      allSql.put( "marketing.inserteventregister", "insert into eventregister (eventid , individualid , accepted) values(?,?,?)");
      //allSql.put( "marketing.geteventdetails", "select eve.title title , eve.detail detail , eve.start startdate, eve.end enddate, eve.formember formember , eve.maxattendees maxattendees, eve.owner ownerid, concat(indeve.firstname,' ' , indeve.lastname) ownername , evereg.individualid individualid , concat(ind.firstname,' ' , ind.lastname) individualname from event eve , eventregister evereg , individual ind , individual indeve where eve.eventid = evereg.eventid and eve.eventid= ? and evereg.individualid = ind.individualid and eve.owner = indeve.individualid ");
      allSql.put( "marketing.geteventdetails", "select eve.eventid, eve.title title , eve.detail detail , eve.start startdate, eve.end enddate, eve.formember formember ,eve.maxattendees maxattendees, eve.owner ownerid, concat(ind.firstname,' ' , ind.lastname) ownername , eve.created createddate , eve.modified modifieddate from event eve , individual ind where eve.owner = ind.individualid and eve.eventid = ?");
      allSql.put( "marketing.updateevent", "update event set title = ? , detail = ? , start = ? , end = ? , formember = ? , owner = ? , creator = ?  ,  maxattendees  = ? , modified = NOW() where eventid = ?");
      allSql.put( "marketing.deleteevent", "delete from event where eventid = ?");
      allSql.put( "marketing.deleteeventregister", "delete from eventregister where eventid = ?");
      allSql.put( "marketing.deleteeventindregister", "delete from eventregister where eventid = ? and individualid = ?");

      allSql.put("EventAtendees.individuallistcreatetable","create TEMPORARY TABLE individuallist select indv.IndividualID IndividualID, indv.FirstName FirstName ,indv.LastName LastName,  moc.Content Email, e.accepted accepted  FROM eventregister e, individual indv, methodofcontact moc  WHERE 1=0;");
      allSql.put("EventAtendees.individuallistinsert","INSERT INTO individuallist(IndividualID, FirstName, LastName, accepted) SELECT DISTINCT ind.IndividualID, ind.FirstName, ind.LastName, e.accepted FROM individual ind, eventregister e where e.eventid = ? and e.individualid = ind.individualid ;");
      allSql.put("EventAtendees.individuallistupdate","UPDATE individuallist, mocrelate, methodofcontact, moctype, contacttype SET individuallist.Email = methodofcontact.Content where individuallist.individualID = mocrelate.contactID AND   mocrelate.MOCID = methodofcontact.MOCID AND   methodofcontact.MOCType = moctype.MOCTypeID AND   moctype.Name = 'Email' AND   contacttype.contacttypeid = mocrelate.contactType AND  contacttype.Name = 'Individual';");
      allSql.put("EventAtendees.individuallistselect","Select individualID ,concat(FirstName ,'  ', LastName) Name,Email from individuallist;");
      allSql.put("EventAtendees.individuallistdroptable","DROP TABLE individuallist;");


      allSql.put("marketing.registerattendee","update eventregister set accepted = ? where eventid = ? and IndividualID = ?;");

      //SUPPORT
      //TICKET
      allSql.put( "support.ticket.insertticket", "INSERT INTO ticket (Subject,Description,status,priority,entityid,individualid,manager,assignedto,owner,createdby,created) VALUES(?,?,?,?,?,?,?,?,?,?,NOW())");
      allSql.put( "support.ticket.updateticket", "update ticket set Subject = ?,Description = ?,status = ?,priority = ?,entityid = ?,individualid = ?,manager = ?,assignedto = ?,modifiedby = ? where ticketid = ?");
      allSql.put( "support.ticket.deleteticket", "delete ticket from ticket where ticketid = ?");
      allSql.put( "support.ticket.getticket", "select a.ticketid, a.subject, a.Description, a.status, a.priority, b.name statusname, c.name priorityname, a.assignedto, a.createdby, a.created, a.dateclosed, a.entityid, a.individualid, a.manager, a.modifiedby, a.modified, a.owner, a.ocstatus  from ticket a, supportstatus b, supportpriority c where a.status = b.statusid and a.priority = c.priorityid and a.ticketid = ?");
      allSql.put( "support.ticket.closeticket", "update ticket set ocstatus = ?, dateclosed = ?, status=? where ticketid = ?");
      allSql.put("support.ticket.getallticketids","select ticketid from ticket");
      allSql.put( "support.ticket.insertthread", "INSERT INTO thread(ticketid,title,detail,type,priority,creator,created) VALUES(?,?,?,?,?,?,NOW())");
      allSql.put( "support.ticket.updatethread", "update thread set title = ?,detail = ?,priority = ?, type = ? where threadid = ?");
      allSql.put( "support.ticket.deletethread", "delete thread from thread where ticketid = ?");
      allSql.put( "support.ticket.getthread", "select a.threadid,a.ticketid,a.title,a.detail,a.priority,b.name priorityname,a.type,a.creator,a.created from thread a, supportpriority b where a.priority = b.priorityid and a.threadid = ?");
      allSql.put( "support.ticket.getthreadforticket", "select a.threadid as threadid,a.ticketid as ticketid,a.title as title,a.detail as detail,a.priority as priority,b.name as priorityname ,"+
                               "a.type as type,a.creator as creator,a.created as created,en.name as reference,en.entityid as entityid,concat(indv.FirstName,' ',indv.LastName) as createdby from thread a, supportpriority b,"+
                               "ticket t,entity en,individual indv where a.priority = b.priorityid and t.ticketid =a.ticketid and "+
                               "  indv.individualid=a.creator and a.ticketid = ? group by a.title ORDER BY created DESC");
      allSql.put("support.ticket.addlink","insert into ticketlink (ticketid,recordtypeid,recordid) values(?,?,?)");
      //TICKET END

      allSql.put( "kb.getparentcat","select parent from category where catid=?;");
      allSql.put("support.insertcategory","insert into category(title,parent,createdby, owner, created,status,publishToCustomerView) values(?,?,?,?,NOW(),?,?)");
      allSql.put("support.updatecategory","update category set title = ?,parent = ?,modifiedby = ?, status =?, publishToCustomerView= ?  where catid = ?");
      allSql.put("support.deletecategory","delete from category where catid=?");
      allSql.put("support.getcategory","select catid,title, parent,createdby,modifiedby,created,modified,owner,status,publishToCustomerView from category where catid  = ?");
      allSql.put("support.getallcategory","select catid,title, parent,createdby,modifiedby,created,modified,owner from category");
      allSql.put("support.getsubcategory","select catid from category where parent = ?");

      allSql.put("support.insertkb","insert into knowledgebase (title,detail,createdby, created,owner,category, status, publishToCustomerView) values(?,?,?,NOW(),?,?,?,?)");
      allSql.put("support.updatekb","update knowledgebase set title = ?, detail = ?,updatedby = ?,updated = NOW(), category =? , status = ? , publishToCustomerView =? where kbid = ?");
      allSql.put("support.deletekb","delete from  knowledgebase where kbid = ?");
      allSql.put("support.deleteallkb","delete knowledgebase");
      allSql.put("support.getkb",   "select kbid,title,detail,createdby,created,updatedby,updated,owner,category, status, publishToCustomerView from knowledgebase where kbid  = ?");
      allSql.put("support.deleteallkbforcategory","delete from  knowledgebase where category = ?");
      allSql.put("support.getallkbforcategory","select kbid,title,detail,createdby,created,updatedby,updated,owner,category from knowledgebase where category  = ?");
      allSql.put( "kb.getparentinfo","select b.catID as parentid,b.title as parenttitle  from category a, category b where a.parent = b.catID and a.catid=?;");
      allSql.put( "kb.getcurcategoryname","select title as title from category where catid=?;");
      // KB END

      allSql.put("support.ticket.getallstatus","select statusid , name  from supportstatus");
      allSql.put("support.ticket.getallpriorities","select priorityid , name from supportpriority");

      allSql.put("support.ticket.filelist","select indv.individualid as individualid,"+
                         "fil.fileid fileid,fil.name name,fil.description description,"+
                         "fil.created created,fil.updated updated,"+
                         "concat(indv.FirstName,'',indv.LastName) 'createdby'"+
                         "from cvfile fil ,individual indv,ticketlink tlk,cvtable cvt "+
                         "where indv.individualid=fil.owner and tlk.recordid=fil.fileid "+
                         "and cvt.tableid=tlk.recordtypeid and cvt.name='cvfile'"+
                         "and tlk.ticketid=?;");




      allSql.put("support.ticket.allticketcount","Select count(*) as allticketcount from ticket tick left outer join entity en on en.entityid=tick.entityid left outer join individual indv on indv.individualid=tick.assignedto left outer join supportstatus supstat on  tick.status=supstat.statusid ");
      allSql.put("support.faq.allfaqcount","Select count(*) as allcountfaq from faq fq ");
      allSql.put("support.faq.getquestion","select questionid, faqid, question, answer from question where questionid = ?");
      allSql.put("support.faq.insertquestion","insert into question (faqid, question, answer) values(?,?,?)");
      allSql.put("support.faq.updatequestion","update question set question = ?, answer = ? where questionid =?");
      allSql.put("support.faq.deletequestion","delete from question where questionid = ?");
      allSql.put("support.faq.insertfaq","insert into faq (title,detail,createdby, created,status,owner,publishToCustomerView) values(?,?,?,NOW(),?,?,?)");
      allSql.put("support.faq.getfaq","select faqid,title,detail,createdby,created,status,owner,publishToCustomerView from faq where faqid  = ?");
      allSql.put("support.faq.updatefaq","update faq set title = ?, detail = ?,updatedby = ?,updated = NOW(), status =?, publishToCustomerView= ? where faqid = ?");
      allSql.put( "support.faq.deletefaq", "delete from faq where faqid = ?");
      allSql.put( "support.faq.getquestionforfaq", "select questionid,question,answer  from question where faqid = ?");

            //SUPORT END


//************************/
//Accounts


      //Common
      allSql.put("project.getrecordtype", "SELECT tableid FROM cvtable WHERE name=?");
      allSql.put("project.addcustomfieldscalar", "INSERT INTO customfieldscalar(customfieldid, recordid, value) VALUES (?, ?, ?)" );
      allSql.put("project.deletecustomfieldscalar", "DELETE FROM customfieldscalar WHERE recordid = ?" );
      allSql.put("project.selectprojectcount", "SELECT count(pr.projectid) FROM project pr,projectaccess pra where pra.ProjectID=pr.ProjectID;" );
      allSql.put("project.selecttaskcount", "SELECT count(activityid) FROM task" );
      allSql.put("project.selecttimeslipcount", "SELECT count(timeslipid) FROM timeslip" );

      //Project
      allSql.put("project.createprojectlist", "CREATE TEMPORARY TABLE projectlist SELECT pr.ProjectID ProjectID, pr.projecttitle ProjectTitle, pr.Description Description, projectstatus.title Status, pr.StatusID StatusID, pr.Start Start, pr.End End, entity.EntityID EntityID, entity.Name EntityName from project as pr, entity, projectstatus where 1=0;");
      allSql.put("project.insertprojectlist", "INSERT INTO projectlist (ProjectID, ProjectTitle, Description, Status, StatusID, Start, End) SELECT project.ProjectID, ProjectTitle, project.Description, projectstatus.Title, project.StatusID, Start , End from projectaccess pra, project left join projectstatus on project.statusid = projectstatus.statusid where pra.projectid=project.projectid;");
      allSql.put("project.updateprojectlist", "UPDATE projectlist, projectlink, entity set projectlist.entityid =  projectlink.recordid , projectlist.entityname = entity.name WHERE entity.entityid = projectlink.recordid and projectlink.projectid = projectlist.projectid and projectlink.recordtypeid = 14;");
      allSql.put("project.deleteprojectlist", "DROP TABLE projectlist;");

      allSql.put("project.deleteproject", "DELETE FROM project WHERE projectid = ?");
      allSql.put("project.deleteprojectlink", "DELETE FROM projectlink WHERE projectid = ?");
      allSql.put("project.getprojectstatuslist", "select * from projectstatus");
      allSql.put("project.addproject", "INSERT INTO project(ProjectTitle,Description,StatusID,Start,End,BudgetedHours,Manager,Owner,Creator,Created) values(?,?,?,?,?,?,?,?,?,NOW())");
      allSql.put("project.addprojectlink", "insert into projectlink(projectid, recordtypeid, recordid) values(?,?,?)");
      allSql.put("project.deleteprojecttimeslip", "UPDATE timeslip SET ActivityID = 0 WHERE projectid =?");

      allSql.put("project.getproject", "SELECT * FROM project WHERE projectid=?");
      allSql.put("project.getprojectentitylink", "SELECT entity.entityid, entity.name FROM entity,projectlink,module WHERE module.Name='Entity' and module.moduleid=projectlink.recordtypeid and projectlink.recordid = entity.entityid AND projectlink.projectid=?");
      allSql.put("project.getprojectindividuallink", "SELECT individualid, CONCAT(firstname , ' ' , lastname) FROM individual, projectlink, module where module.Name='Individual'and module.moduleid=projectlink.recordtypeid and projectlink.recordid = individual.individualid AND projectlink.projectid=?");
      allSql.put("project.getprojectgrouplink", "SELECT grouptbl.groupid, grouptbl.name FROM grouptbl,projectlink, module where module.Name='Group' and module.moduleid=projectlink.recordtypeid and grouptbl.groupid = projectlink.recordid  AND projectlink.projectid=?");

      allSql.put("project.updateproject", "UPDATE project SET ProjectTitle =?, Description =?, StatusID =?, Start=?, End=?, BudgetedHours=?, Manager=?, ModifiedBy =?,Modified= NOW() WHERE projectid=?");
      allSql.put("project.getindname", "SELECT CONCAT(firstname,' ',lastname) FROM individual WHERE individualid=?");
      allSql.put("project.projects.tempselect", "CREATE TEMPORARY TABLE projectaccess SELECT proj.projectid, proj.owner FROM project proj WHERE proj.owner=? or proj.manager=? UNION SELECT proj.projectid, proj.owner FROM project proj, recordauthorisation b WHERE proj.projectid=b.recordid AND b.recordtypeid=9 AND b.privilegelevel<40 AND b.individualID=? UNION SELECT proj.projectid, proj.owner FROM publicrecords pub LEFT JOIN project proj ON pub.recordid=proj.projectid WHERE pub.moduleid=9");


      allSql.put("projecttask.createtasklist", "CREATE TEMPORARY TABLE tasklist SELECT  at.activityid activityid, at.title title, tk.milestone milestone, at.start start, at.duedate duedate, tk.percentcomplete percentcomplete,project.projectid projectid, project.projecttitle projecttitle,at.owner owner,concat(individual.firstname, ' ', individual.lastname)  name,activitypriority.name priority, activitystatus.name status, at.creator creator, concat(individual.firstname, ' ', individual.lastname) createdby, at.created created , concat(individual.firstname, ' ', individual.lastname) managername , 0 managerid from activity at, task tk, project,individual, activitypriority,activitystatus where 1=0;");

			allSql.put("projecttask.inserttasklist","INSERT INTO tasklist(activityid, title, milestone, Start, DueDate, percentcomplete, "+
															"projectid,projecttitle, owner, priority,status,creator,created) "+
															"SELECT tk.activityid, at.title, tk.milestone, at.start, at.duedate,  "+
															"tk.percentcomplete, tk.projectid, project.projecttitle, at.owner,  "+
															"activitypriority.name, activitystatus.name, at.creator, at.created  "+
															"from activity at, task tk,project left join activitypriority on  "+
															"at.priority = activitypriority.priorityid left join activitystatus on  "+
															"at.status = activitystatus.statusid where tk.projectid = project.projectid  "+
															"and tk.activityid = at.activityid and at.owner= ? "+
															"UNION "+
															"SELECT tk.activityid, at.title, tk.milestone, at.start, at.duedate,  "+
															"tk.percentcomplete, tk.projectid, project.projecttitle, at.owner,  "+
															"activitypriority.name, activitystatus.name, at.creator, at.created  "+
															"from activity at left join activitylink al on at.activityid=al.activityid , task tk,project left join activitypriority on  "+
															"at.priority = activitypriority.priorityid left join activitystatus on  "+
															"at.status = activitystatus.statusid where tk.projectid = project.projectid  "+
															"and tk.activityid = at.activityid and al.RecordTypeID =2 and al.RecordID =? "+
															"UNION "+
															"SELECT tk.activityid, at.title, tk.milestone, at.start, at.duedate,  "+
															"tk.percentcomplete, tk.projectid, project.projecttitle, at.owner,  "+
															"activitypriority.name, activitystatus.name, at.creator, at.created  "+
															"from activity at,taskassigned ta, task tk,project left join activitypriority on  "+
															"at.priority = activitypriority.priorityid left join activitystatus on  "+
															"at.status = activitystatus.statusid where tk.projectid = project.projectid  "+
															"and tk.activityid = at.activityid and ta.taskid = tk.activityid AND ta.AssignedTo = ? ");

			allSql.put("projecttask.updatetasklist", "UPDATE tasklist, individual set tasklist.name =  concat(individual.firstname, ' ', individual.lastname) WHERE tasklist.owner = individual.individualid;");
      allSql.put("projecttask.updatetasklist1", "UPDATE tasklist, individual set tasklist.createdby =  concat(individual.firstname, ' ', individual.lastname) WHERE tasklist.creator = individual.individualid;");
			allSql.put("projecttask.updatetasklist2", "UPDATE tasklist, individual i ,activitylink set tasklist.managername =  concat(i.firstname, ' ', i.lastname) , tasklist.managerid = i.IndividualID WHERE tasklist.activityid = activitylink.ActivityID and activitylink.RecordTypeID = 2 and activitylink.RecordID = i.IndividualID;");

      allSql.put("projecttask.droplist", "DROP TABLE tasklist;");

      allSql.put("projecttask.addtaskactivity", "INSERT INTO activity(type, title, details, creator, created, owner,start,duedate,priority) values(?,?,?,?,NOW(),?, ?, ?,2)");
      allSql.put("projecttask.selecttaskcount", "SELECT count(projecttaskcount) FROM task WHERE projectid = ?");
      allSql.put("projecttask.addtask", "INSERT INTO task(activityid, projectid, parent, milestone, projecttaskcount) values(?,?,?,?,?)");
      allSql.put("projecttask.addtaskassigned", "INSERT INTO taskassigned(taskid, assignedto) values(?,?)");
      allSql.put("projecttask.addtaskalertaction", "INSERT INTO action(type, message) values(?,?)");
      allSql.put("projecttask.addtaskalert", "INSERT INTO activityaction(activityid, actionid, recipient) values(?,?,?)");

      allSql.put("projecttask.gettaskactivity", "SELECT type, title, details, creator, created, modified, modifiedby, owner, status,  activitystatus.name, start, duedate FROM activity left outer join activitystatus on activity.status = activitystatus.statusid WHERE activityid = ?");
      allSql.put("projecttask.gettask", "SELECT task.projectid, parent, milestone, projecttaskcount, percentcomplete, projecttitle FROM task, project WHERE task.projectid= project.projectid and task.activityid = ?");

      allSql.put("projecttask.gettaskassigned", "SELECT individualid, CONCAT(firstname, ' ' , lastname) FROM individual, taskassigned WHERE taskassigned.assignedto = individual.individualid AND taskassigned.taskid=?");
      allSql.put("projecttask.gettaskalert", "SELECT activityaction.actionid, activityaction.recipient, concat(individual.firstname,' ', individual.lastname) FROM individual INNER JOIN activityaction ON (individual.individualid = activityaction.recipient) where activityaction.activityid=?");
      allSql.put("projecttask.gettaskalertaction", "SELECT type FROM action WHERE actionid = ?");
      allSql.put("projecttask.getsubtask", "SELECT task.ActivityID, task.Milestone, activity.Owner, activity.Start, activity.End, task.PercentComplete, activity.Title, individual.FirstName, individual.LastName FROM task, activity left outer join individual ON (individual.IndividualID = activity.Owner) WHERE activity.ActivityID = task.ActivityID AND task.Parent= ?");
      allSql.put("projecttask.gettaskparent", "select activity.activityid, title, parent from activity, task where  activity.activityid = task.activityid and task.activityid = ?");
      allSql.put("projecttask.getparenttaskname", "select title from activity where  activity.activityid = ?");
      allSql.put("projecttask.gettaskstatus", "select statusid, name from activitystatus order by statusid");

      allSql.put("projecttask.updatetaskactivity", "UPDATE activity SET type =? , title = ?, details =?, modifiedby = ?, status = ? , start = ?, duedate = ? ,owner = ? WHERE activityid = ?");
      allSql.put("projecttask.updatetask", "UPDATE task SET projectid =? , parent = ?, milestone = ?, percentcomplete =? WHERE activityid = ?");

      allSql.put("projecttask.deletetaskassigned", "DELETE FROM taskassigned WHERE taskid = ?");

      allSql.put("projecttask.getactivitycreator", "select activity.created, creator, concat(individual.firstname, ' ', individual.lastname) from activity, individual where activity.creator = individual.individualid AND activity.activityid = ?");

      allSql.put("projecttask.deletetaskactivity", "DELETE FROM activity WHERE activityid=?");
      allSql.put("projecttask.selecttaskaction", "SELECT DISTINCT(actionid) FROM activityaction WHERE activityid=?");
      allSql.put("projecttask.deletetaskalert", "DELETE FROM activityaction WHERE activityid=?");
      allSql.put("projecttask.deletetaskalertaction", "DELETE FROM action WHERE actionid=?");
      allSql.put("projecttask.setidnull", "UPDATE timeslip SET activityid = NULL WHERE activityid = ?");

      //Timeslip
      allSql.put("projecttimeslip.addtimeslip", "INSERT INTO timeslip(projectid, activityid, description, date, start, end, breaktime, CreatedBy, hours,ticketid,timesheetid) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
      allSql.put("projecttimeslip.deletetimeSlip", "DELETE FROM timeslip WHERE timeslipid= ?");
      //allSql.put("projecttimeslip.gettimeSlip", "SELECT timeslipid, ts.projectid, pr.projecttitle, ts.activityid, at.title, ts.description, ts.date, ts.start, ts.end, ts.breaktime, ts.hours FROM timeslip ts, activity at, project pr WHERE ts.projectid = pr.projectid and ts.activityid = at.activityid and timeslipid = ?");
      allSql.put("projecttimeslip.updatetimeslip", "UPDATE timeslip SET projectid = ?, activityid =?, ticketid =?, description =?, date=?, start=?, end=?, breaktime=?, hours=? WHERE timeslipid = ?");

      allSql.put("projecttimeslip.gettimeSlip", "SELECT timeslipid, ts.ticketid, ts.projectid, ts.description, ts.date, ts.activityid, ts.start, ts.end, ts.breaktime, ts.hours FROM timeslip ts WHERE timeslipid = ?");
      allSql.put("projecttimeslip.getprojecttimeSlip", "select projecttitle from project where projectid =?");
      allSql.put("projecttimeslip.getactivitytimeSlip", "select title from activity where activityid =?");




      //Relates
      String strPr = "select at.activityid, at.title, tk.milestone, at.start, at.duedate, tk.percentcomplete, project.projectid, project.projecttitle, at.owner, individual.firstname, individual.lastname from activity at, task tk left join project "
      +"on tk.projectid = project.projectid left join individual on at.owner = individual.individualid where tk.activityid = at.activityid and tk.projectid = ? and tk.Parent <> ? and tk.activityid <> ? order by projecttitle";
      //For lookup and relates
      allSql.put("project.tasklookup", strPr);

      String strTs = "select ts.TimeSlipID, ts.ProjectID, project.ProjectTitle, ts.Description, ts.CreatedBy, individual.FirstName, individual.LastName, ts.activityid, ts.BreakTime, ts.Date, ts.Start, ts.End, activity.title from timeslip  ts left outer join project on  ts.ProjectID = project.ProjectID "
      +"left outer join individual on ts.CreatedBy = individual.IndividualID left outer join task on ts.activityID = task.activityID left outer join activity on task.activityid = activity.activityid where ts.projectid = ?  order by ts.timeslipid";
      allSql.put("project.projecttimeslip", strTs);
      allSql.put("project.projecthoursused", "select sum(hours) from timeslip where projectid = ?");

      // account::item
      allSql.put("item.insertitem", "insert into item (type, description, taxclass, parent, listprice, cost, createdby, createddate, sku, glaccountid, manufacturerid, vendorid, title,externalid,LinkToInventory, qtyonbackorder, qtyonorder) values (?, ?, ?, ?, ?, ?, ?, NOW(), ?, ?, ?, ?, ?,?,?, ?, ?)");
      allSql.put("item.updateitem", "update item set description = ?, type = ?, taxclass = ?, parent = ?, listprice = ?, cost = ?, modifiedby = ?, createddate = createddate, modifieddate = NOW(), glaccountid = ?, manufacturerid = ?, vendorid = ?, title = ?, sku = ?, LinkToInventory = ?, qtyonbackorder = ?, qtyonorder = ? where itemid = ?");
      allSql.put("item.getitem", "SELECT i.itemid, i.type, i.description, i.taxclass, i.parent, i.listprice, i.cost, i.sku, i.glaccountid, i.manufacturerid, m.name AS manufacturername, i.vendorid, v.name AS vendorname, i.title, i.createddate, i.modifieddate, i.externalid, i.LinkToInventory, i.qtyonbackorder, i.qtyonorder, COUNT(inv.item) AS qtyonhand FROM item i   LEFT JOIN entity m ON (i.manufacturerid=m.entityID) LEFT JOIN entity v ON (i.vendorid=v.entityID) LEFT JOIN inventory inv ON (i.itemid=inv.item) WHERE i.itemid=? GROUP BY i.itemid");
      allSql.put("item.deleteitem", "update item set deletestatus='Deleted' where itemid = ?");   // "delete from item where itemid = ?");
      allSql.put("item.getitemtype", "select itemtypeid, title from itemtype order by title");
      allSql.put("item.getglaccounttype", "select GLAccountsID, Title from glaccount order by Title");
      allSql.put("item.gettaxclasstype", "select taxclassid, title from taxclass order by title");
      allSql.put("item.getiteminfo", "select itemid from item where sku like ?");

      //order

      allSql.put("order.addorder", " insert into cvorder (entityid,billaddress,shipaddress,status,terms,accountmgr,project,ponumber,creator,orderdate,created,subtotal,tax,total,description,invoiceIsGenerated,owner) values (?,?,?,?,?,?,?,?,?,?,NOW(),?,?,?,?,?,?) ");
      allSql.put("order.updateorder", " update cvorder  set entityid = ? ,billaddress = ?, shipaddress = ?,status = ? , ponumber = ?,terms  = ? ,accountmgr = ? , project = ?, subtotal= ?, tax = ?,total = ?,description = ?, invoiceIsGenerated = ? ,modifiedby = ? where orderid = ? ");

      allSql.put("order.addorderitem", " insert into orderitem  (orderid,itemid,quantity,price,sku,description,taxAmount) values(?,?,?,?,?,?,?) ");
      allSql.put("order.updateorderitem", " update orderitem set itemid = ? , quantity = ? ,price = ? ,sku = ?, description = ?, taxAmount = ? where lineid = ? and orderid = ?  ");
      allSql.put("order.markdeletedorderitem", " update orderitem set status  = ? where lineid = ? and orderid = ?  ");


//      allSql.put("order.getorder", " SELECT  cvorder.orderid,cvorder.entityid,entity.name as entityname,cvorder.billaddress ,concat(add1.street1,' ',add1.street2) as billaddressdesc ,cvorder.shipaddress,concat(add2.street1,'  ',add2.street2) as shipaddressdesc ,cvorder.status ,accountingstatus.title as statusdesc,cvorder.orderdate,cvorder.ponumber,cvorder.terms,accountingterms.title as termsdesc ,cvorder.accountmgr,concat(individual.firstname,' ',individual.lastname) as accountmgrdesc ,cvorder.description,cvorder.invoiceisgenerated  FROM cvorder left outer join  entity on cvorder.entityid = entity.entityid left outer join  address add1  on cvorder.billaddress = add1.addressid left outer join  address add2  on cvorder.shipaddress = add2.addressid left outer join  accountingstatus on cvorder.status = accountingstatus.statusid left outer join  accountingterms on cvorder.terms  = accountingterms.termsid left outer join  individual on cvorder.accountmgr= individual.individualid where cvorder.orderid = ? ");


      allSql.put("order.getorder", " SELECT  cvorder.orderid,cvorder.entityid,entity.name as entityname, "+
 								   " cvorder.billaddress ,concat(add1.street1,' ',add1.street2)  "+
  								   " as billaddressdesc ,add1.jurisdictionID ,cvorder.shipaddress,concat(add2.street1,'  ',add2.street2)  "+
  								   " as shipaddressdesc ,cvorder.status ,accountingstatus.title as statusdesc, "+
  								   " cvorder.orderdate,cvorder.ponumber,cvorder.terms,accountingterms.title as  "+
  								   " termsdesc ,cvorder.accountmgr,concat(individual.firstname,' ',individual.lastname)  "+
  								   " as accountmgrdesc , "+
  								   " cvorder.description,cvorder.invoiceisgenerated  , cvorder.project , project.ProjectTitle "+
  								   " FROM cvorder  "+
  								   " left outer join  entity on cvorder.entityid = entity.entityid  "+
  								   " left outer join  address add1  on cvorder.billaddress = add1.addressid  "+
  								   " left outer join  address add2  on cvorder.shipaddress = add2.addressid  "+
  								   " left outer join  accountingstatus on cvorder.status = accountingstatus.statusid  "+
  								   " left outer join  accountingterms on cvorder.terms  = accountingterms.termsid  "+
  								   " left outer join  individual on cvorder.accountmgr= individual.individualid  "+
  								   " left outer join  project on cvorder.Project = project.ProjectId  "+
  								   " where cvorder.orderid = ? ");

      allSql.put("order,createorderitemlist", " create temporary table orderitemlist as select orderitem.lineid lineid, orderitem.itemid itemid,orderitem.orderid orderid, orderitem.quantity quantity,orderitem.price priceeach, orderitem.sku sku, orderitem.status linestatus, orderitem.description description,orderitem.price*orderitem.quantity priceextended, taxmatrix.taxrate unittaxrate,orderitem.price*orderitem.quantity* taxmatrix.taxrate unittax,orderitem.quantity orderquantity, invoiceitems.quantity invoicequantity, invoiceitems.quantity pendingquantity from orderitem, taxmatrix, invoiceitems where 1=0 ");
      allSql.put("order,insertorderitemlist", " insert into orderitemlist (lineid, itemid, orderid, quantity, priceeach, sku, linestatus, description, priceextended, orderquantity)  select  orderitem.lineid, orderitem.itemid, orderitem.orderid, orderitem.quantity,orderitem.price, orderitem.sku ,orderitem.status, orderitem.description, orderitem.price*orderitem.quantity,orderitem.quantity from orderitem where orderid = ? ");
      allSql.put("order,updateorderitemlist", " update orderitemlist, taxclass, item, taxjurisdiction, taxmatrix set orderitemlist.unittaxrate = taxmatrix.taxrate where orderitemlist.itemid = item.itemid and item.taxclass = taxclass.taxclassid and taxjurisdiction.taxjurisdictionid = ? and taxmatrix.taxjurisdictionid = taxjurisdiction.taxjurisdictionid and taxmatrix.taxclassid = taxclass.taxclassid ");
      allSql.put("order,update1orderitemlist", "update orderitemlist set unittaxrate = 0 where unittaxrate is null");
      allSql.put("order,update2orderitemlist", " update orderitemlist set unittax = priceextended*unittaxrate ");
      allSql.put("order.getorderitem", " SELECT lineid, itemid,quantity,priceeach,sku,linestatus,description ,priceextended ,unittaxrate,unittax,orderquantity,invoicequantity,pendingquantity FROM orderitemlist  ");
      allSql.put("order.droporderitemlist", " DROP TABLE orderitemlist ");


      allSql.put("order.deleteorderitem", " update  orderitem  set status = 'Deleted' where orderid = ?  ");
      allSql.put("order.deleteorder", " update  cvorder set orderstatus = 'Deleted' where orderid = ? ");

      //Invoice
      allSql.put("invoice.addinvoice", " insert into invoice (orderid,billaddress,shipaddress,status,terms,accountmgr,project,ponumber,creator,invoicedate,created,subtotal,tax,total,customerid,description,externalid,owner) values (?,?,?,?,?,?,?,?,?,?,NOW(),?,?,?,?,?,?,?)      ");
      allSql.put("invoice.addinvoiceitem", " insert into invoiceitems  (invoiceid,itemid,quantity,price,sku,description,taxAmount)  values(?,?,?,?,?,?,?); ");
      allSql.put("invoice.updateinvoiceitem", "   update invoiceitems set itemid = ? , quantity = ? ,price = ? ,sku = ?, description = ? ,taxAmount = ?  where invoicelineid = ? and invoiceid = ? ");
      allSql.put("invoice.markdeletedinvoiceitem", "  update invoiceitems set  status  = ?  where invoicelineid = ? and invoiceid = ?  ");
      allSql.put("invoice.getinvoice", "   SELECT  invoice.invoiceid,invoice.customerid,entity.name as entityname,invoice.billaddress ,concat(add1.street1,' ',add1.street2) as billaddressdesc,add1.jurisdictionID ,invoice.shipaddress,concat(add2.street1,'  ',add2.street2) as shipaddressdesc ,invoice.status ,accountingstatus.title as statusdesc,invoice.invoicedate,invoice.ponumber,invoice.terms,accountingterms.title as termsdesc ,invoice.accountmgr,concat(individual.firstname,' ',individual.lastname) as accountmgrdesc,invoice.description  ,invoice.project ,project.projecttitle,invoice.orderid,invoice.externalid,invoice.Created,invoice.Modified FROM invoice left outer join  entity on invoice.customerid = entity.entityid left outer join  address add1  on invoice.billaddress = add1.addressid left outer join  address add2  on invoice.shipaddress = add2.addressid left outer join  accountingstatus on invoice.status = accountingstatus.statusid left outer join  accountingterms on invoice.terms  = accountingterms.termsid left outer join  individual on invoice.accountmgr= individual.individualid  left outer join  project on invoice.project = project.projectid where invoice.invoiceid =  ? ");

      allSql.put("invoice.createinvoiceitemlist", "  create temporary table invoiceitemlist as select invoiceitems.invoicelineid lineid, invoiceitems.itemid itemid,invoiceitems.invoiceid invoiceid, invoiceitems.quantity quantity,invoiceitems.price priceeach, invoiceitems.sku sku, invoiceitems.status linestatus, invoiceitems.description description,invoiceitems.price*invoiceitems.quantity priceextended,taxclassid, taxmatrix.taxrate unittaxrate,invoiceitems.price*invoiceitems.quantity* taxmatrix.taxrate unittax,orderitem.quantity orderquantity, invoiceitems.quantity invoicequantity, invoiceitems.quantity pendingquantity from orderitem, taxmatrix, invoiceitems where 1=0  ");

      allSql.put("invoice.insertinvoiceitemlist", " insert into invoiceitemlist (lineid, itemid, invoiceid, quantity, priceeach, sku, linestatus, description, priceextended) select  invoiceitems.invoicelineid, invoiceitems.itemid, invoiceitems.invoiceid, invoiceitems.quantity,invoiceitems.price, invoiceitems.sku ,invoiceitems.status, invoiceitems.description, invoiceitems.price*invoiceitems.quantity from invoiceitems where invoiceitems.status  != 'Deleted'  and invoiceid =  ?    ");
      allSql.put("invoice.updateinvoiceitemlist", " update invoiceitemlist, taxclass, item, taxjurisdiction, taxmatrix set invoiceitemlist.unittaxrate = taxmatrix.taxrate, invoiceitemlist.taxclassid = item.taxclass where invoiceitemlist.itemid = item.itemid and item.taxclass = taxclass.taxclassid and taxjurisdiction.taxjurisdictionid = ? and taxmatrix.taxjurisdictionid = taxjurisdiction.taxjurisdictionid and taxmatrix.taxclassid = taxclass.taxclassid  ");
      allSql.put("invoice.update1invoiceitemlist", "update invoiceitemlist set unittaxrate = 0 where unittaxrate is null ");
      allSql.put("invoice.update2invoiceritemlist", " update invoiceitemlist,invoice,orderitem set invoiceitemlist.orderquantity = orderitem.quantity where invoiceitemlist.invoiceid = invoice.invoiceid and invoice.orderid=orderitem.orderid and invoiceitemlist.itemid = orderitem.itemid; ");
      allSql.put("invoice.getinvoiceitem", " SELECT lineid, itemid,taxclassid,quantity,priceeach,sku,linestatus,description ,priceextended ,unittaxrate,unittax,orderquantity,invoicequantity,pendingquantity  from invoiceitemlist ");
      allSql.put("invoice.dropinvoiceritemlist", " drop table invoiceitemlist;");
      allSql.put("invoice.deleteinvoiceitem", " update invoiceitems  set status = 'Deleted' where invoiceid = ?");
      allSql.put("invoice.deleteinvoice", " update invoice set invoicestatus = 'Deleted' where invoiceid = ? ");
      allSql.put("invoice.updateinvoice", " update invoice  set billaddress = ?, modified = NOW(), shipaddress = ?,status = ? , ponumber = ?,terms  = ? ,accountmgr = ? , project = ?, subtotal= ?, tax = ?,total = ?,description = ?,externalid = ?,`modified by`=? where invoiceid = ? ");

      //Purchase order
      allSql.put("purchaseorder.addpurchaseorder"," insert into purchaseorder (billaddress,shipaddress,status,terms,accountmgr,creator,purchaseorderdate,created,subtotal,tax,total,entity,description,externalid,owner) values  (?,?,?,?,?,?,?,NOW(),?,?,?,?,?,?,?)  ");
      allSql.put("purchaseorder.addpurchaseorderitem","  insert into purchaseorderitem (purchaseorderid,itemid,quantity,price,sku,description)  values(?,?,?,?,?,?); ");
      allSql.put("purchaseorder.updatepurchaseorderitem","  update purchaseorderitem set  itemid = ? , quantity = ? ,price = ? ,sku = ?, description = ? where purchaseorderlineid = ? and purchaseorderid = ? ");
      allSql.put("purchaseorder.markdeletedpurchaseorderitem","  update purchaseorderitem set  status  = ?  where purchaseorderlineid = ? and purchaseorderid = ?  ");
      allSql.put("purchaseorder.getpurchaseorder"," SELECT  purchaseorder.purchaseorderid,purchaseorder.entity,entity.name as entityname,purchaseorder.billaddress ,concat(add1.street1,' ',add1.street2) as billaddressdesc ,purchaseorder.shipaddress,concat(add2.street1,'  ',add2.street2) as shipaddressdesc ,purchaseorder.status ,accountingstatus.title as statusdesc,purchaseorder.purchaseorderdate,purchaseorder.terms,accountingterms.title as termsdesc ,purchaseorder.accountmgr ,concat(individual.firstname,' ',individual.lastname) as accountmgrdesc,purchaseorder.description ,purchaseorder.externalid,purchaseorder.Created,purchaseorder.Modified FROM purchaseorder left outer join  entity on purchaseorder.entity= entity.entityid left outer join  address add1  on purchaseorder.billaddress = add1.addressid left outer join  address add2  on purchaseorder.shipaddress = add2.addressid left outer join  accountingstatus on purchaseorder.status = accountingstatus.statusid left outer join  accountingterms on purchaseorder.terms  = accountingterms.termsid left outer join  individual on purchaseorder.accountmgr= individual.individualid   where  purchaseorderid = ? ");
      allSql.put("purchaseorder.getpurchaseorderitem","  SELECT purchaseorderlineid, itemid,quantity,price,sku,status,description ,price* quantity as priceextended ,0.0 as unittaxrate,0.0 as unittax, 0 as orderquantity, 0 as purchaseorderquantity, 0 as pendingquantity,status linestatus   from purchaseorderitem where purchaseorderid = ?  ");
      allSql.put("purchaseorder.deletepurchaseorderitem","  update purchaseorderitem  set status = 'Deleted' where purchaseorderid = ?  ");
      allSql.put("purchaseorder.deletepurchaseorder","  update purchaseorder set purchaseorderstatus = 'Deleted' where purchaseorderid = ? ");
      allSql.put("purchaseorder.updatepurchaseorder","  update purchaseorder  set billaddress = ?, shipaddress = ?,status = ? ,terms  = ? ,accountmgr = ? , subtotal= ?, tax = ?,total = ? ,description = ?,modifiedby=? where purchaseorderid = ? ");

      // add vendor
      allSql.put("vendor.addvendor","  insert into vendor (entityid) values (?)");
      allSql.put("vendor.deletevendor"," delete from  vendor where entityid  = ?  ");

      //For GML

      //deletings of synchronized objects
      allSql.put("account.helper.deleteGLAccount","delete from glaccount where externalID in (?)");
      allSql.put("account.helper.deletePaymentMethod","delete from paymentmethod where externalID in (?)");
      allSql.put("account.helper.deletePayment","delete from payment where ExternalID in (?)");
      allSql.put("account.helper.deleteItem","delete from item where externalID in (?)");
      allSql.put("account.helper.deleteExpense","delete from expense where ExternalID in (?)");
      allSql.put("account.helper.deletePurchaseOrder","delete from purchaseorder where ExternalID in (?)");
      allSql.put("account.helper.deleteInvoice","delete from invoice where ExternalID in (?)");
      allSql.put("account.helper.deleteEntity","delete from entity where externalID in (?)");
      allSql.put("account.helper.deleteIndividual","delete from individual where ExternalID in (?)");
      allSql.put("account.helper.deleteEmployee","delete from employee where IndividualID in (?)");

      allSql.put("account.helper.getEmployeesByExternalIds","select IndividualID from individual where ExternalID in (?)");

      allSql.put("account.helper.getitemtypes", "select itemtypeid as ID ,title as ItemType from itemtype");
      allSql.put("account.helper.getglaccounts","select glaccountsid as ID ,title as GLAccount from glaccount");
      allSql.put("account.helper.getnewglaccounts","select glaccountsid as ID ,title as GLAccount, Description, Balance, GLAccountType as Type, Parent, ExternalID from glaccount where externalID = ''");
      allSql.put("account.helper.gettaxclasses","select taxclassid as ID,title as TaxClass from taxclass");
      allSql.put("account.helper.getaccountingstatus","select statusid as ID,title as AccountingStatus from accountingstatus");
      allSql.put("account.helper.getaccountingterms","select termsid as ID,title as AccountingTerms from accountingterms");
      allSql.put("account.helper.getpaymentmethods","select methodid as ID,title as PaymentMethod from paymentmethod");
      allSql.put("account.helper.getnewpaymentmethods","select methodid as ID,title as PaymentMethod , externalID from paymentmethod where externalID='[NULL]'");
      // added by SandeepJ for GLAccount and PaymentMethod
      allSql.put("account.helper.addglaccount","insert into glaccount(title,description,balance,glaccounttype,parent,externalid) values(?,?,?,?,?,?)");
      allSql.put("account.helper.updateglaccount","update glaccount set title = ?,description = ?,balance = ?,glaccounttype = ?,parent = ?,externalid = ? where glaccountid = ?");
      allSql.put("account.helper.addPaymentMethod","insert into paymentmethod(title,externalid) values(?,?)");
      allSql.put("account.helper.updatePaymentMethod","update paymentmethod set title = ?, externalid = ? where methodid = ?");
      //account.inventory starts here

      String query = " select item.title as itemname,inventory.item as itemid ,inventory.title as identifier," +
               "location.locationid as locationid,location.title location,man.name as manufacturer," +
               "man.entityid as manid,ven.entityid as vendorid,ven.name as vendor ,inventory.description,inventory.qty , "+
               " inventory.created,inventory.modified,inventorystatus.statusid as statusid," +
               "inventorystatus.statusname as status,soldto.entityid as soldtoid ,soldto.name as soldto " +
               "from inventory left outer join item on "+
               " inventory.item=item.itemid left outer join location on inventory.locationid=location.locationid "+
               " left outer join entity man on item.manufacturerid = man.entityid left outer join "+
               " entity ven on item.vendorid = ven.entityid " +
               "left outer join inventorystatus on "+
               " inventory.statusid = inventorystatus.statusid left outer join entity soldto "+
               " on inventory.customerid = soldto.entityid where inventoryid = ? group by "+
               " itemname,identifier,location,manufacturer,vendor,description,created,modified,status,soldto ";

      allSql.put("account.inventory.getinventory",query);

      allSql.put("account.inventory.insertinventory","insert into inventory (title,description,qty,locationid,item,statusid,customerid,created,vendorid,owner) values(?,?,?,?,?,?,?,NOW(),?,?)");
      allSql.put("account.inventory.updateinventory","update inventory set title = ? ,description = ?,qty = ?,locationid = ?,item = ?,statusid = ?,customerid = ?,created = NOW() ,vendorid = ? where inventoryid = ?");
      allSql.put("account.inventory.deleteinventory","update  inventory set linestatus='deleted' where inventoryid = ?");
      //account.inventory ends here

      //account.expense starts here
      allSql.put("account.expense.addexpense","insert into expense (glaccountsid,amount,title,description,entityid,status,created,owner,project,opportunity,ticket,notes,externalid) values(?,?,?,?,?,?,NOW(),?,?,?,?,?,?)");
      allSql.put("account.expense.addexpenseitem","insert into expenseitem (expenseid,expenseitemid,sku,description,price,quantity) values(?,?,?,?,?,?)");
      allSql.put("account.expense.updateexpense","update expense set glaccountsid = ?,amount = ?,title = ?,description = ?,entityid = ?,status = ?,modified = NOW(),owner = ?,project = ?,opportunity = ?,ticket = ? ,notes = ? where expenseid = ?");
      allSql.put("account.expense.updateexpenseitem","update expenseitem set expenseitemid = ?,sku = ?,description = ?,price = ?,quantity = ? ,linestatus = ? where expenseid = ? and lineid = ?");
      allSql.put("account.expense.deleteexpense","update expense set linestatus = 'deleted' where expenseid = ?");
      allSql.put("account.expense.deleteexpenseitem","update expenseitem set linestatus = 'deleted' where expenseid = ? ");


      String getExpense = " select expense.expenseid ,expense.GLAccountsID,expense.Amount, "+
                " expense.Title, expense.Description,entity.EntityID,entity.name as EntityName "+
                " ,expense.Created ,expense.Modified , expense.Status,employee.IndividualID,concat(employee.firstname ,' ',employee.lastname) as EmployeeName "+
                " ,project.ProjectID,project.ProjectTitle,opportunity.OpportunityID,opportunity.title as OpportunityTitle "+
                " ,ticket.TicketID,ticket.subject as SupportTicket ,expense.notes,expense.externalid from expense "+
                " left outer join entity on expense.entityid = entity.entityid " +
                " left outer join individual employee on expense.owner = employee.individualid "+
                " left outer join project on expense.project = project.projectid " +
                " left outer join opportunity on expense.opportunity = opportunity.opportunityid "+
                " left outer join ticket on expense.ticket = ticketid  where  ExpenseID = ?";

      allSql.put("account.expense.getexpense",getExpense);
      allSql.put("account.expense.getexpenseitem","select ExpenseItemID,SKU,Description,Price,Quantity,lineid,linestatus,0.0 as priceextended ,0.0 as unittax ,0.0 as unittaxrate ,0 as orderquantity, 0 as pendingquantity from expenseitem where expenseid = ?");
      allSql.put("account.expense.markdeletedexpenseitem","update expenseitem set linestatus = ? where  expenseid = ? and expenseitemid = ? and  lineid = ?");
      //account.expense ends here


      //********** Sale entry starts
      allSql.put("sale.opportunity.getopportunityid","select OpportunityID from opportunitylink where RecordID=? and recordtypeid=21;");
      allSql.put("sale.getallstatus","select salesstatusid id, name from salesstatus");
      allSql.put("sale.getallstage","select salesstageid id, name from salesstage");
      allSql.put("sale.getalltype","select salestypeid id, name from salestype");
      allSql.put("sale.getallprobability","select probabilityid id, title name, Probability from salesprobability order by 3");
      allSql.put("sale.getallterm","select TermID id,TermName name from terms");
      allSql.put("sale.opportunity.addopportunity","insert  into opportunity (activityid,title ,description,entityid ,individualid,typeid,status ,stage,forecastammount,actualamount,probability ,source ,accountmanager ,accountteam ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
      allSql.put("sale.opportunity.updateopportunity","update opportunity set title = ?,description = ?,entityid = ?,individualid = ?,typeid = ?,status = ? ,stage = ?,forecastammount = ?,actualamount = ?,probability = ? ,source = ? ,accountmanager = ? ,accountteam = ? where opportunityid = ?");
      allSql.put("sale.opportunity.getopportunity",
      " select o.opportunityid,o.activityid ,o.title ,o.description ,o.entityid, e.name entityname,o.individualid, act.modified, act.created, act.modifiedby modifiedbyid, act.creator createdbyid,"
      +" concat(i.firstname,  '  ' , i.lastname) individualname,"
      +" o.typeid,stype.name typename,o.status, ss.name statusname,o.stage,sstage.name stagename,o.forecastammount,o.actualamount,o.probability ,o.source, src.name sourcename,o.accountmanager,"
      +" concat(m.firstname,  '  ' , m.lastname) managername,"
      +" o.accountteam,g.name accteamname ,act.start ,act.completeddate,"
      +" concat(cb.firstname,  '  ' , cb.lastname) createdbyname,"
      +" concat(mb.firstname,  '  ' , mb.lastname) modifiedbyname"
      +" from entity e,individual cb, individual mb, activity act, opportunity o left join individual i on o.individualid = i.individualid left join individual m on o.accountmanager = m.individualid left join  grouptbl g on  o.accountteam = g.groupid"
      +" left join  source src on  o.source = src.sourceid"
      +" left join  salesstatus ss on  o.status = ss.salesstatusid"
      +" left join  salesstage sstage on  o.stage = sstage.salesstageid"
      +" left join  salestype stype on  o.typeid = stype.salestypeid"
      +" where o.opportunityid = ?"
      +" and act.activityid = o.activityid"
      +" and o.entityid = e.entityid"
      +" and cb.individualid = act.creator"
      +" and mb.individualid = act.modifiedby");


      allSql.put("sale.opportunity.relatedopportunityinfo"," select o.Title, o.EntityId, e.Name EntityName, o.IndividualId,"
      		+" concat(i.firstname,  '  ' , i.lastname) individualname "
			+" from entity e, opportunity o left join individual i on o.individualid = i.individualid "
			+" where o.entityid = e.entityid  and OpportunityID= ? ");

      allSql.put("sale.opportunity.getopportunityactivity",
      " select o.opportunityid,o.activityid ,o.title ,o.description ,o.entityid, e.name entityname,o.individualid, act.modified, act.created, act.modifiedby modifiedbyid, act.creator createdbyid,"
      +" concat(i.firstname,  '  ' , i.lastname) individualname,"
      +" o.typeid,stype.name typename,o.status, ss.name statusname,o.stage,sstage.name stagename,o.forecastammount,o.actualamount,o.probability ,o.source, src.name sourcename,o.accountmanager,"
      +" concat(m.firstname,  '  ' , m.lastname) managername,"
      +" o.accountteam,g.name accteamname ,act.start ,act.completeddate,"
      +" concat(cb.firstname,  '  ' , cb.lastname) createdbyname,"
      +" concat(mb.firstname,  '  ' , mb.lastname) modifiedbyname"
      +" from entity e,individual cb, individual mb, activity act, opportunity o left join individual i on o.individualid = i.individualid left join individual m on o.accountmanager = m.individualid left join  grouptbl g on  o.accountteam = g.groupid"
      +" left join  source src on  o.source = src.sourceid"
      +" left join  salesstatus ss on  o.status = ss.salesstatusid"
      +" left join  salesstage sstage on  o.stage = sstage.salesstageid"
      +" left join  salestype stype on  o.typeid = stype.salestypeid"
      +" where o.activityid = ?"
      +" and act.activityid = o.activityid"
      +" and o.entityid = e.entityid"
      +" and cb.individualid = act.creator"
      +" and mb.individualid = act.modifiedby");


      allSql.put("sale.opportunity.addactivity","insert  into activity( status, modified , created, type, title, start, details, creator, owner, modifiedby ) select StatusID, NOW(), NOW(), ?, ?, ?, ?, ?, ?, ? from activitystatus where upper(name) = upper('Pending')");
      allSql.put("sale.getactivitystatus","select activitystatusid from salesstatus where salesstatusid = ?");
      allSql.put("sale.opportunity.updateactivity","update activity set type = ?, status = ? ,title = ?,start = ? , completeddate = ? ,details = ? , modifiedby = ? , modified = NOW() where activityid = ?");
      allSql.put("sale.opportunity.linkopportunity","insert into opportunitylink(opportunityid, recordtypeid, recordid) values(?,?,?)");
      allSql.put("sale.opportunity.hasproposal","select proposalid from proposal where opportunityid = ?");
      allSql.put("sale.opportunity.hasproposalInForcast","select proposalid from proposal where IncludeForcastSale = 'YES' and opportunityid = ?");
      allSql.put("sale.opportunity.getForcastFromProposalInForcast","select ForecastAmmount from proposal where IncludeForcastSale = 'YES' and opportunityid = ?");
      allSql.put("sale.opportunity.getProbabilityPercentage","select salesprobability.Probability from salesprobability inner join opportunity on opportunity.probability = salesprobability.probabilityid and opportunity.opportunityid  = ?");
      allSql.put("sale.opportunity.updateActualAmount","update opportunity set ActualAmount = ? where opportunityid = ?");
      allSql.put("sale.opportunity.updateForcastAmount","update opportunity set ForecastAmmount = ? where opportunityid = ?");
      allSql.put( "sale.getactivityidfromopportunityid", "select activityid from opportunity where opportunityid = ?");
      allSql.put( "sale.deleteassociatedopportunitylinks", "delete from opportunitylink where opportunityid = ?");
      allSql.put( "sale.deleteopportunity", "delete from opportunity where opportunityid = ?");
      allSql.put( "sale.deleteactivity", "delete from activity where activityid = ?");
      allSql.put( "sale.deleteassociatedproposallinks", "delete from proposallink where proposalid = ?");
      allSql.put( "sale.deleteproposalitems", "delete from proposalitem where proposalid = ?");
      allSql.put( "sale.deleteproposal", "delete from proposal where proposalid = ?");



      allSql.put("attic.selectrecordtypeid","select tableid from cvtable where name=?");
      allSql.put("attic.insertattic","insert into attic(deletedby,dumptype,recordtitle,owner,moduleid,record) values(?,?,?,?,?,?)");
      allSql.put("attic.insertatticdata","insert into atticdata(atticid,recordtypeid,fieldid,value) values(?,?,?,?)");
      allSql.put("attic.selectfields","select fieldid,name from field where  tableid=?");



      allSql.put("account.payment.getpayment","select payment.PaymentID,payment.EntityID, entity.name as Entity ,amount,reference,description,PaymentMethod,CardType,CardNumber,Expiration,CheckNumber,payment.ExternalID,payment.Created,payment.Modified from payment inner join entity on payment.entityid =  entity.entityid where paymentid = ?");

      allSql.put("account.payment.addpayment","insert into payment (entityid,reference,description,paymentmethod,checknumber,cardnumber,cardtype,expiration,amount ,created,externalid,owner) values (?,?,?,?,?,?,?,?,?,NOW(),?,?)");
      allSql.put("account.payment.applypaymentinvoices","insert into applypayment (paymentid,invoiceid,amount) values(?,?,?)");

      allSql.put("account.payment.updatepaymentinvoices","update applypayment set amount = amount + ? where paymentid = ? and invoiceid =?");

      allSql.put("account.payment.updatepaymentamount","update payment set amount = ? where paymentid = ? ");

      allSql.put("account.payment.selectApplypaymentamount","select sum(amount) Amount from  applypayment where paymentid = ? ");
      
      allSql.put("account.payment.updatepayment","update payment set modified = NOW() ,entityid = ? ,reference = ? ,description = ?,paymentmethod = ? ,checknumber = ?,cardnumber = ?,cardtype = ? ,expiration = ?, externalid = ?, modifiedby = ? where paymentid  = ?");

      allSql.put("account.payment.deletepayment","update payment set linestatus = 'Deleted'  where paymentid = ?");
      allSql.put("account.payment.deletepaymentinvoices","update applypayment set linestatus = 'Deleted'  where paymentid = ?");
      //End of Payment Queries

      // N for Promotion
      // Promotion queries here
      //add promotion
      allSql.put( "promotion.addpromotion1","insert into promotion( Title , Description , Status , Startdate , Enddate ,Notes, Owner  ) values( ?,?,?,?,?,?,? )");
      allSql.put( "promotion.addpromotion2","insert into promoitem( PromotionID , ItemID , Quantity , Rule , Price  ) values( ?,?,?,?,? )");

      //get promotion
      allSql.put( "promotion.getpromotion1","select * from promotion where PromotionID = ? " );
      allSql.put( "promotion.getpromotion2","select * from promoItem where PromotionID = ? " );

	  allSql.put( "promotion.getpromotionitem","select * from promotion , promoitem where promoitem.itemid=? and promotion.status='YES'");
      //update promotion
      allSql.put( "promotion.updatepromotion1","update promotion set Title = ? , Description = ? , Status = ? , Startdate = ? , Enddate=? , Notes =? where PromotionID = ? " );
      //allSql.put( "promotion.updatepromotion2","update promoitem set PromotionID = ? , ItemID = ? , Quantity = ? , Rule = ?, Price = ?  " );

      //delete promotion
      allSql.put( "promotion.deletepromotion1","delete  from promotion where PromotionID = ? " );
      allSql.put( "promotion.deletepromotion2","delete  from promoitem where PromotionID = ? " );



      // Custom Fields

      allSql.put( "admin.newcustomfield","insert into customfield(name,fieldtype,recordtype) values(?,?,?) ");
      allSql.put( "admin.customfieldvalues","insert into customfieldvalue(customfieldid,value) values(?,?) ");
      allSql.put( "admin.updatecustomfieldvalues","update customfieldvalue set customfieldid=? ,value=? where valueid=? ");

      allSql.put( "admin.updatecustomfield","update customfield set name=?,fieldtype=?,recordtype=? where customfieldid=? ");
      allSql.put( "admin.deletecustomfieldvalues","delete from customfieldvalue where customfieldid=? ");

      allSql.put( "admin.deletecustomfield","delete from customfield where customfieldid=? ");

      allSql.put( "admin.getCustomField","select name as name,fieldtype as fieldtype,recordtype as recordtype from customfield where customfieldid=? ");
      allSql.put( "admin.getCustomFieldvalues","select value as value, ValueID as ValueID from customfieldvalue where customfieldid=? ");
      allSql.put( "admin.getCustomFieldValues","select cfv.ValueID, cfv.CustomFieldID, cfv.value as value, cf.name from customfieldvalue cfv, customfield cf where cfv.CustomFieldID=cf.CustomFieldID");
      //End Custom Fields
      //strat changepassword
      allSql.put("common.user.passworddetails","select * from user where userid=? ");
      allSql.put("common.user.updatepassword","update user set password=? where userid = ? ");
      //end changepassword

      //FOR EMPLOYEE LIST SHILPA PATIL
      allSql.put("contact.getemployees","select IndividualID from employee");
      // changes by SHILPA PATIL ends here

      //For Getting & Setting Tax for TaxClass and Jurisdiction By Sameer
      allSql.put("account.tax.gettax","select TaxRate from taxmatrix where taxclassid = ? and taxjurisdictionid = ?");
      allSql.put("account.tax.settax","update taxmatrix set TaxRate = ? where taxclassid = ? and taxjurisdictionid = ?");
      allSql.put("account.tax.inserttax","insert into taxmatrix(taxclassid,taxjurisdictionid, taxrate) values (?,?,?)");
      allSql.put("additionalmenu.getallmenu", "select a.menuitem_id id, a.menuitem_name name,a.moduleid moduleid,m.name modulename,a.forward_res forward,a.new_win new_win,a.win_property win_property, a.params params from additionalmenu a, module m where a.moduleid = m.moduleid order by a.menuitem_order");
    //********** ADMINISTRATION ***********
      allSql.put("preference.getpreference","select individualid, moduleid, preference_name, preference_value, tag from userpreference where individualid = ?");

      allSql.put("preference.updatepreference","update userpreference set preference_value = ?, tag = ? where individualid = ? and preference_name=?");

    // queries for administration.configuration -----> authorization settings
      allSql.put("administration.configuration.setauthorizationsettings","INSERT INTO `authorizationsettings`  (userAuthType, server, port, username, password, usernameField, passwordField, authField) values(?, ?, ?, ?, ?, ?, ?, ?)");
      allSql.put("administration.configuration.getauthorizationsettings", "SELECT * FROM `authorizationsettings`");

    // queries for serversettings page Added
      allSql.put("administration.configuration.getserversettings","SELECT hostname, sessiontimeout, workinghoursfrom, workinghoursto, emailcheckinterval, filesystemstoragepath, defaulttimezone from serversettings");
      allSql.put("administration.configuration.setHostName","UPDATE serversettings SET hostname = ?");
      allSql.put("administration.configuration.updateserversettings","UPDATE serversettings set sessiontimeout = ?, workinghoursfrom = ? , workinghoursto = ? , emailcheckinterval = ? , filesystemstoragepath = ? , defaulttimezone = ?");

      //queries for hr module start here
      allSql.put("hr.employeelistinsert","INSERT INTO individuallist(IndividualID, EntityID, FirstName, LastName, MiddleInitial, Title ) SELECT DISTINCT employee.IndividualID, Entity, FirstName, LastName, MiddleInitial,Title FROM individual,employee WHERE individual.IndividualID = employee.IndividualID");
      //timesheet
      allSql.put("hr.createtimesheetlist","create TEMPORARY TABLE timesheetlist select timesheet.TimeSheetID ID,employee.IndividualID EmpIndvidualID ,concat(FirstName ,' ', LastName) Name,timesheet.Start StartDate,timesheet.End EndDate,sum(Hours) Duration,concat(FirstName ,' ', LastName) CreatedBy,timesheet.Creator from individual ,  employee, timeslip,timesheet where employee.IndividualID=individual.IndividualID AND timesheet.TimeSheetID = timeslip.TimeSheetID AND employee.IndividualID=546 AND 1=0  GROUP BY timesheet.TimeSheetID");
      allSql.put("hr.inserttimesheetlist","INSERT INTO timesheetlist (ID,EmpIndvidualID,Name,StartDate,EndDate,Duration,CreatedBy,Creator)  SELECT TimeSheetID,Owner,'',Start,End,'','',Creator FROM timesheet WHERE (Owner=? or ReportingTo=? or creator=?)");
      allSql.put("hr.updatetimesheetlist1","UPDATE timesheetlist,individual set Name=concat(FirstName ,' ', LastName) where EmpIndvidualID= individual.IndividualID");
      allSql.put("hr.updatetimesheetlist2","UPDATE timesheetlist,individual set timesheetlist.CreatedBy=concat(FirstName ,' ', LastName)  where Creator = individual.IndividualID");

      allSql.put("hr.timesheetlistselect","select timesheet.TimeSheetID ID,concat(FirstName ,' ', LastName) Name,timesheet.Start StartDate,timesheet.End EndDate,sum(Hours) Duration,concat(FirstName ,' ', LastName) CreatedBy from individual ,  employee, timeslip,timesheet where timesheet.Owner=individual.IndividualID AND timesheet.TimeSheetID = timeslip.TimeSheetID AND timesheet.Owner= 546 GROUP BY timesheet.TimeSheetID");
      allSql.put("hr.selecttimesheetcount","SELECT count(TimeSheetID) FROM timesheet");


      //expenseformlist
      allSql.put("hr.deleteexpenseformlist","DROP TABLE expenseFormList");
      allSql.put("hr.createexpenseformlist","CREATE  TEMPORARY TABLE expenseFormList SELECT expenseform.ExpenseFormID ID , employee.IndividualID EmpIndvidualID,concat(FirstName, ' ', LastName)  Employee,expenseform.FromDate StartDate,expenseform.ToDate EndDate,sum(Amount) Amount, expense.Status Status , concat(FirstName, ' ', LastName) CreatedBy , expenseform.Creator FROM expenseform,expense,individual, employee  WHERE  1=0 GROUP BY  expense.ExpenseFormID");
      allSql.put("hr.insertexpenseformlist","INSERT INTO expenseFormList (ID,EmpIndvidualID,Employee,StartDate,EndDate,Amount,Status,CreatedBy,Creator) select ExpenseFormID,Owner,'',FromDate,ToDate,0,Status,'',Creator from expenseform where (Creator=? or Owner=? or reportingto=?) and lineStatus <> 'Deleted'");
      allSql.put("hr.updateexpenseformlist1","UPDATE expenseFormList,individual set Employee = concat(FirstName, ' ', LastName)  where EmpIndvidualID= individual.IndividualID");
      allSql.put("hr.updateexpenseformlist2","UPDATE expenseFormList,individual set expenseFormList.CreatedBy = concat(FirstName, ' ', LastName)  where expenseFormList.Creator= individual.IndividualID;");
      allSql.put("hr.createexpenseamount","CREATE TEMPORARY TABLE expenseamount select ExpenseFormId ID, sum(Amount) Total from expense where Linestatus not in ('Deleted') group by ExpenseFormId;");
      allSql.put("hr.updateexpenseformlist3","UPDATE expenseFormList, expenseamount set Amount = Total where expenseFormList.ID = expenseamount.ID");
      allSql.put("hr.updateexpenseformlist4", "UPDATE expenseformlist set Status=? where ID=?;");
      allSql.put("hr.deleteexpenseamount","DROP TABLE expenseamount");

      //expenseformadd
      allSql.put("hr.expense.addexpenseform","insert into expenseform(FromDate,ToDate,Note,ReportingTo,employeeID,Owner,Creator,ModifiedBy,Description,Created,Modified)Values(?,?,?,?,?,?,?,?,?,NOW(),NOW())");
      allSql.put("hr.expense.addexpense","insert into expense (Description,Project,Ticket,Opportunity,Owner,Creator,ModifiedBy,LineID,Amount,ExpenseFormID,Created,Modified) Values(?,?,?,?,?,?,?,?,?,?,NOW(),NOW())");
      allSql.put("hr.expense.addexpenseitem","insert into expenseitem (expenseid,expenseitemid,sku,description,price,quantity) values(?,?,?,?,?,?)");
      allSql.put("hr.expense.getexpenseform","select * from expenseform where ExpenseFormID = ?;");
      allSql.put("hr.expense.getexpenseitem","select expenseitem.ExpenseID ExpenseID,ExpenseItemID,SKU,Note,expense.Description reference ,Price,Quantity,expenseitem.LineID LineID,Amount,Ticket,Project,Opportunity,expense.linestatus,expenseitem.Description Description,expense.ExpenseFormID ExpenseFormID,Quantity*Price priceextended  from expenseitem,expense,expenseform where (expenseform.ExpenseFormID = expense.ExpenseFormID AND expense.LineStatus !='Deleted' AND expense.ExpenseID= expenseitem.ExpenseID AND expenseform.ExpenseFormID = ?)");
      allSql.put("hr.expense.updateexpenseform","update expenseform set FromDate = ?,ToDate = ?,Note = ?,Description = ?,ReportingTo = ?,employeeID = ? ,ModifiedBy = ?,Status = ?,created = NOW(), Modified = NOW() where expenseformid = ?");
      allSql.put("hr.expense.updateexpense","update expense set Description=? , Project=?,Ticket=?,Opportunity=?, ModifiedBy = ?, Modified=NOW() where ExpenseFormID = ? and lineid = ?");
      //check this allSql.put("hr.expense.updateexpenseitem","update expenseitem set expenseitemid = ?,sku = ?,description = ?,price = ?,quantity = ? linestatus=? where expenseid = ? ");
      allSql.put("hr.expense.updateexpenseitem","update expenseitem set expenseitemid = ?,sku = ?,description = ?,price = ?,quantity = ?  where expenseid = ? ");
      allSql.put("hr.expense.updateexpenselineid","update expense set LineID = ? where ExpenseID = ? ");//cw
      allSql.put("hr.expense.markdeletedexpenseitem","update expenseform set Status ='Deleted' where  expenseformid = ?");
      //Added by Rohit needs to be modified
      //allSql.put("hr.expense.deleteexpenses"," delete from expenseform where ExpenseFormID =?");


      //19/10/03allSql.put("hr.createexpenseformlist","CREATE TEMPORARY TABLE expenseFormList SELECT expenseform.ExpenseFormID ExpenseFormId , concat(FirstName, ' ', LastName)  Employee,expenseform.FromDate StartDate,expenseform.ToDate EndDate,sum(Amount) Amount,concat(FirstName, ' ', LastName) CreatedBy FROM expenseform,expense,individual WHERE expense.ExpenseFormID = expenseform.ExpenseFormID AND IndividualID = 546 AND 1=0 GROUP BY  expense.ExpenseFormID");
      //19/10/03allSql.put("hr.insertexpenseformlist","INSERT INTO expenseFormList(ExpenseFormID, Employee, StartDate, EndDate, Amount,CreatedBy) SELECT expenseform.ExpenseFormID , concat(FirstName, ' ', LastName) ,expenseform.FromDate ,expenseform.ToDate ,sum(Amount),concat(FirstName, ' ', LastName) FROM expenseform,expense,individual WHERE expense.ExpenseFormID = expenseform.ExpenseFormID AND individual.IndividualID = 546 GROUP BY  expense.ExpenseFormID");


      allSql.put("hr.addemployee","insert into employee(IndividualID) values(?)");
      allSql.put("hr.deleteemployee","delete from employee where IndividualID = ?");
      allSql.put("hr.getemployeeids", "SELECT IndividualID FROM employee");


      //timesheet start
      allSql.put("hr.addtimesheet","insert into timesheet (Description,Owner,Creator,ModifiedBy,Start,End,Status,Notes,ReportingTo,Created,Modified) values (?,?,?,?,?,?,?,?,?,NOW(),NOW())");
      allSql.put("hr.deletetimesheet","delete from timesheet where TimeSheetID = ?");
      allSql.put("hr.gettimeSheet","select * from timesheet where TimeSheetID = ?");
      allSql.put("hr.gettimeSheetID","select count(*) from timesheet");
      allSql.put("hr.addtimeslip", "INSERT INTO timeslip(timesheetid,projectid, activityid, description, date, start, end, breaktime, CreatedBy, hours,ticketid) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
      allSql.put("hr.deletetimeSlip","update timeslip set timesheetid = 0 where timeslipid in (?)");
      allSql.put("hr.updatetimesheet","update timesheet set Description = ?, Owner = ?, ModifiedBy = ?, Start = ?, End =? , Status = ?, Notes = ? , ReportingTo = ? , Modified = NOW() where TimeSheetId = ?");
      //timesheet end

      //expense form
      allSql.put("hr.addexpenseform","insert into expenseform (FromDate, ToDate, Note, ReportingTo, Owner, Creator, ModifiedBy, Created, Modified) values (?,?,?,?,?,?,?,?,?)");
      //expense form

      //suggestion starts
      allSql.put("hr.getemailaddress","select mailaddressfrom,mailaddressto,smtpserver from suggestion,serversettings");
      //suggestion ends here
    //queries for hr module ends here

      //Delegation
      allSql.put("delegation.getModule","Select moduleid from module where name=?");
      allSql.put("delegation.getdelegators","SELECT del.fromuser AS fromuser, del.righttype AS rights, concat(i.FirstName,' ',i.LastName) AS Name FROM module AS m, delegation AS del LEFT OUTER JOIN individual AS i ON del.fromuser = i.individualid WHERE del.touser=? AND del.moduleid=m.moduleid AND m.name=?");
      allSql.put("delegation.userdelegators","select del.touser AS touser,del.righttype rights from delegation del,module m where del.fromuser=? and del.moduleid=m.moduleid and m.name=?");

      allSql.put("delegation.deleteuserdelegators","delete del from delegation del, module m where del.fromuser=? and del.moduleid=m.moduleid and m.name=?");
      allSql.put("delegation.insertuserdelegators","insert into delegation(fromuser,touser,moduleid,righttype) values(?,?,?,?)");

      // Email Delegation
      allSql.put("delegation.deleteemaildelegators", "DELETE FROM emaildelegation WHERE individualID=?");
      allSql.put("delegation.insertemaildelegators", "INSERT INTO emaildelegation (individualID, delegatorID) VALUES (?,?)");
      allSql.put("delegation.emaildelegators", "SELECT delegatorID FROM emaildelegation WHERE individualID=?");

      // Start Query for  print Template
      allSql.put("pt.addcategory","insert into ptcategory(catname,artifactid,parentid,cattype) values(?,?,?,?)");
      allSql.put("pt.addprinttemplate","INSERT INTO ptdetail(ptcategoryid, isdefault, ptdata, ptname, userid, artifactid, pttype, ptsubject) VALUES (?,?,?,?,?,?,?,?)");
      allSql.put("pt.getprinttemplate","select ptcategoryid,isdefault,ptdata,ptname,userid,artifactid,ptsubject from ptdetail where ptdetailid = ?");
      allSql.put("pt.updateprinttemplate","update ptdetail set ptdata = ?,userid=?,ptname=?,ptsubject=? where ptdetailid = ? ");
      allSql.put("pt.getAllCategories","select ptc.catname,ptc.ptcategoryid,ptc.parentid,ptc.cattype from ptcategory ptc,ptartifact pta where pta.artifactid=ptc.artifactid and artifactname=?");
      allSql.put("pt.getdefaultprinttemplate","SELECT a.ptdetailid,a.ptcategoryid,a.isdefault,a.ptdata,a.ptname,a.ptsubject FROM ptdetail AS a WHERE isdefault='YES' AND a.ptCategoryId=? LIMIT 1");
      allSql.put("pt.getAllPrintTemplate","SELECT ptd.ptname, ptd.ptdetailid FROM ptdetail AS ptd WHERE ptd.ptcategoryId=?");
      allSql.put("pt.getprimarycontact","select individualid from individual where PrimaryContact='YES' and entity=?");
      allSql.put("pt.getallcontactforentity","select IndividualID individualID from individual where entity=?");
      allSql.put("pt.deletePt","delete from ptdetail where ptdetailid=?");
      allSql.put("template.getCategories", "SELECT ptCategoryId AS id, catName AS name FROM ptcategory");

      // Quickbooks Sync
      allSql.put("synch.getCVidForEntity","Select EntityID cvid from entity where ExternalID = ?");
      allSql.put("synch.getCVidForIndividual","Select IndividualID cvid from individual where ExternalID = ?");
      allSql.put("synch.getCVidForInvoice","Select InvoiceID cvid from invoice where ExternalID = ?");
      allSql.put("synch.getCVidForItem","Select itemid cvid from item where externalid = ?");
      allSql.put("synch.getCVidForPurchaseOrder","Select PurchaseOrderID cvid from purchaseorder where ExternalID = ?");
      allSql.put("synch.getCVidForExpense","Select ExpenseID cvid from expense where ExternalID = ?");
      allSql.put("synch.getCVidForPayment","Select PaymentID cvid from payment where externalID = ?");
      allSql.put("synch.getCVidForAccountingTerms","Select AccountingTermsid cvid from accountingterms where extid = ?");
      allSql.put("synch.getCVidForPaymentMethod","Select MethodID cvid from paymentmethod where externalID = ?");
      allSql.put("synch.getCVidForGLAccount","Select GLAccountsid cvid from glaccount where externalID = ?");
      allSql.put("synch.getExtidForEntity","Select ExternalID extid from entity where EntityID = ?");
      allSql.put("synch.getExtidForIndividual","Select ExternalID extid from individual where IndividualID = ?");
      allSql.put("synch.getExtidForInvoice","Select ExternalID extid from invoice where InvoiceID = ?");
      allSql.put("synch.getExtidForItem","Select externalid extid from item where itemid = ?");
      allSql.put("synch.getExtidForPurchaseOrder","Select extid ExternalID from purchaseorder where PurchaseOrderID = ?");
      allSql.put("synch.getExtidForExpense","Select ExternalID extid from expense where ExpenseID = ?");
      allSql.put("synch.getExtidForPayment","Select ExternalID extid from payment where PaymentID = ?");
      allSql.put("synch.getExtidForAccountingTerms","Select extid extid from accountingterms where AccountingTermsid = ?");
      allSql.put("synch.getExtidForPaymentMethod","Select externalID extid from paymentmethod where MethodID = ?");
      allSql.put("synch.getExtidForGLAccount","Select externalID extid from glaccount where GLAccountsid = ?");

      allSql.put("savedsearch.allsavedsearchcount","select count(searchid) as allcountss from search s,module m where s.moduleid=m.moduleid and m.name=?");
      allSql.put("customview.allcustomviewcount","select count(*) as allcountcustomview from listviews lv,defaultviews df,listtypes lt,module m1,module m2 where df.viewid!=lv.viewid and lv.listtype=df.listtype and lt.typename=lv.listtype and lt.moduleid=m1.moduleid and m1.parentid=m2.moduleid and m2.name=?");

      //attic
      allSql.put("attic.deletegarbage" ,"delete attic,atticdata from attic,atticdata where attic.atticid=atticdata.atticid and attic.atticid=? and dumptype=?");
      allSql.put("attic.updateattic","update attic set dumptype=? where atticid=? and dumptype=?");
      allSql.put("attic.getatticmodule","select moduleid,record from attic where atticid=? and dumptype=?");
      allSql.put("attic.getsequence","select tableid,sequence from restoresequence where moduleid=? and primarytable=? order by sequence");
      allSql.put("attic.gettable","select name from cvtable where tableid=?");
      allSql.put("attic.columnnames","select name,value from atticdata at,field f where f.fieldid=at.fieldid and atticid=? and tableid=?");

      //add by sandipw for module settings
      allSql.put("view.getviewidforviewname", "select viewid from listviews where viewname= ? and listtype = ? ");
      allSql.put("view.getdefaultview", "select viewid,viewname from listviews where listtype = ?");
      allSql.put("view.setdefaultview", "update defaultviews set viewid =? where listtype =?");

      allSql.put("view.setdefaultviewowner", "update listviews set ownerid =? where viewid =?");

      allSql.put("view.admin.getdefaultviewid","select viewid from defaultviews where listtype = ?;");

      allSql.put("appsettings.getrecordtypes","select m1.name name, m1.primarytable primarytable from module m1, module m2 where m1.parentid = m2.moduleid and m2.name = ?");

      //end add by sandipw

      //Added by sandipw for attachment in proposal
      allSql.put("proposal.deleteproposallink","delete from proposallink where proposalid=?");
      allSql.put("proposal.addproposallink","insert into proposallink (proposalid,recordtypeid,recordid) values(?,?,?)");
      allSql.put("proposal.checkproposallink","select proposalid from proposallink where proposalid=?");
      // End - Added by sandipw for attachment in proposal

      allSql.put("alert.deletealert","delete from activityaction where activityid=? and recipient=?");

      // Only for CustomerView
      allSql.put("customerview.allcustomviewcount","select count(*) as allcountcustomview from listviews lv,defaultviews df,listtypes lt,module m1,module m2 where df.viewid!=lv.viewid and lv.listtype=df.listtype and lt.typename=lv.listtype and lt.moduleid=m1.moduleid and m1.parentid=m2.moduleid and m2.name=?");
      allSql.put("customerview.file.getuserfiles","select 'FOLDER' as FileFolder,indv.individualid as individualid,fld.folderid ID,fld.name Name,fld.description Description,fld.createdOn Created,fld.ModifiedOn Updated,concat(indv.FirstName,'',indv.LastName) 'Created BY' from individual indv,cvfolder fld where indv.individualid=fld.owner and fld.owner=?  union select 'FILE' as FileFolder,indv.individualid as individualid,fil.fileid ID,fil.name Name,fil.description Description,fil.created Created,fil.updated Updated,concat(indv.FirstName,'',indv.LastName) 'Created BY' from cvfile fil ,individual indv,cvfilefolder fld  where indv.individualid=fil.owner and fld.fileid=fil.fileid and fil.owner=? ");
      allSql.put("customerview.profile.getcustomerAddresses","select address.* from address, addressrelate where addressrelate.ContactType=?  AND addressrelate.Contact=? and address.AddressID=addressrelate.address");
      allSql.put("customerview.events.geteventdetails", "SELECT e.eventID, e.title, e.detail, e.start AS startDate, e.end AS endDate, e.formember as whoAttends, e.maxattendees AS maxAttendees, CONCAT(i.firstname, ' ', i.lastname) AS moderator FROM event e LEFT JOIN individual i ON (e.owner=i.IndividualID) WHERE e.eventid=?");

      allSql.put("customer.events.getEventAttachments", "SELECT f.FileID AS fileid , f.Title AS title, f.Description AS description, f.Created AS dateCreated, f.Updated AS dateUpdated, CONCAT(i.FirstName, ' ', i.LastName) AS CreatedBy FROM eventlink el , cvfile f LEFT JOIN individual i ON (f.Creator=i.IndividualID) WHERE  "+
																												" f.FileID=el.RecordID and el.EventID=? and el.RecordTypeID=6;");

      allSql.put("customerview.support.faq.allfaqcount","Select count(*) as allcountfaq from faq fq where fq.status='PUBLISH'");
      allSql.put("customer.email.getEmailList", "SELECT em.MessageID, em.Subject, em.MessageDate, em.MailFrom, em.ReplyTo, em.Size, em.Priority, em.MessageRead AS readstatus, es.FolderID AS EmailFolder FROM emailmessage em LEFT JOIN emailrecipient er ON (em.MessageID=er.MessageID) LEFT JOIN emailmessagefolder es ON (em.MessageID = es.MessageID) LEFT JOIN methodofcontact moc ON (er.Address=moc.Content) LEFT JOIN mocrelate mr ON (moc.MOCID=mr.MOCID) LEFT JOIN individual i ON (mr.ContactID=i.IndividualID) WHERE mr.ContactType=2 AND moc.MOCType=1 AND es.FolderID IS NOT NULL AND i.Entity=? UNION SELECT em.MessageID, em.Subject, em.MessageDate, em.MailFrom, em.ReplyTo, em.Size, em.Priority, em.MessageRead AS readstatus, es.FolderID AS EmailFolder FROM emailmessage em LEFT JOIN emailrecipient er ON (em.MessageID=er.MessageID) LEFT JOIN emailmessagefolder es ON (em.MessageID = es.MessageID) LEFT JOIN methodofcontact moc ON (er.Address=moc.Content) LEFT JOIN mocrelate mr ON (moc.MOCID=mr.MOCID) WHERE mr.ContactType=1 AND moc.MOCType=1 AND es.FolderID IS NOT NULL AND mr.ContactID=?");
      allSql.put("reports.selectclause","select searchtable.tablename, searchfield.fieldname, reportcontent.sequencenumber, searchtable.searchtableid, searchfield.isontable, searchfield.realtablename, searchfield.relationshipquery, reportcontent.fieldid, searchfield.displayname, searchfield.SubRelationshipQuery from searchtable, searchfield, reportcontent where reportcontent.reportid = ? and searchfield.searchfieldid=reportcontent.fieldid and searchtable.searchtableid = reportcontent.tableId order by reportcontent.sequencenumber");
      allSql.put("reports.fromclause","select distinct searchtable.tablename from searchtable, reportcontent where reportcontent.reportid = ? and searchtable.searchtableid = reportcontent.tableid");
      allSql.put("reports.orderbyclause", "select reportcontent.tableid, reportcontent.fieldid, reportcontent.sortorder, reportcontent.sortordersequence from reportcontent where reportcontent.reportid = ? and reportcontent.sortorder is not null order by reportcontent.sortordersequence;");
      allSql.put("reports.getfieldname", "select searchtable.tablename, searchfield.fieldname, searchfield.displayname from searchtable, searchfield where searchfield.searchfieldid=? and searchfield.searchtableid = searchtable.searchtableid");
      allSql.put("reports.gettitles","select searchfield.displayname, reportcontent.tableid, reportcontent.fieldid from reportcontent, searchfield where reportcontent.reportid = ? and reportcontent.fieldid=searchfield.searchfieldid ");
      allSql.put("reports.gettableid","select searchtableid from searchfield where searchfieldid=?");
      allSql.put("reports.gettables","select searchtable.searchtableid, searchtable.tablename, searchtable.displayname from searchmodule, searchtable where  searchtable.searchtableid=searchmodule.searchtableid and searchmodule.moduleid=? and searchmodule.IsPrimaryTable='Y'");
      allSql.put("reports.getfields", "select searchfieldid, displayname from searchfield where searchtableid = ?");
      allSql.put("reports.createreportcontent","INSERT INTO reportcontent (reportid, fieldid, tableid, sequencenumber, sortorder, sortordersequence) VALUES (?, ?, ?, ?, ?, ?)");
      allSql.put("reports.deletereportcontent","DELETE FROM reportcontent WHERE reportcontentid = ?");
      allSql.put("reports.findreportcontentbypk","SELECT reportcontentid FROM reportcontent WHERE reportcontentid = ?");
      allSql.put("reports.loadreportcontent","SELECT reportid, fieldid, tableid, sequencenumber, sortorder, sortordersequence FROM reportcontent WHERE reportcontentid = ?");
      allSql.put("reports.storereportcontent","UPDATE reportcontent SET reportid = ?, fieldid = ?, tableid = ?, sequencenumber = ?, sortorder = ?, sortordersequence = ? WHERE reportcontentid = ?");
      allSql.put("reports.findreportcontentbyreport","SELECT reportcontentid FROM reportcontent WHERE reportid = ? ORDER BY sequenceNumber, sortordersequence");
      allSql.put("reports.createreport","INSERT INTO report (moduleid, name, description, createdby, createdon, modifiedby, modifiedon, reportURL, reporttypeid, datefrom, dateto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
      allSql.put("reports.deletereport","DELETE FROM report WHERE reportid = ?");
      allSql.put("reports.deletereportsearch","DELETE FROM reportsearch WHERE reportid = ?");
      allSql.put("reports.addsearchlink","INSERT INTO reportsearch (SearchID, ReportID) VALUES (?,?)");
      allSql.put("reports.findreportbypk","SELECT reportid FROM report WHERE reportid = ?");
      allSql.put("reports.loadreport","SELECT moduleid, name, description, createdby, createdon, modifiedby, modifiedon, reportURL, reporttypeid, datefrom, dateto FROM report WHERE reportid = ?");
      allSql.put("reports.storereport","UPDATE report SET moduleid = ?, name = ?, description = ?, createdby = ?, createdon = ?, modifiedby = ?, modifiedon = ?, reportURL = ?, reporttypeid = ?, datefrom = ?, dateto = ? WHERE reportid = ?");
      allSql.put("reports.getsearchcriteria","select searchfieldid, conditionid, value, expressiontype from searchcriteria where searchid = ?");
      // END reports queries
    // Begin Global Replace queries
    allSql.put("administration.globalreplace.getmodulesname", "select  name  from module where name like 'Entity'  OR name  like 'Individual'");
    // End Global Replace queries
    allSql.put("system.email.getSystemEmailInfo", "SELECT toAddress, fromAddress, subject, smtpServer FROM systememailsettings WHERE systemEmailID=?");


		allSql.put("mergeActivityRoll","UPDATE activitylink SET recordId = ? WHERE recordId = ? AND recordTypeId = ?");
		allSql.put("mergeProjectsRoll","UPDATE projectlink SET recordId = ? WHERE recordId = ? AND recordTypeId = ?");

    // Queries for Merge rollUpEntityOrphans
    allSql.put("mergeNoteEntityRoll","UPDATE note SET relateEntity = ? WHERE relateEntity = ?");
    allSql.put("mergeFileEntityRoll","UPDATE cvfile SET relateEntity = ? WHERE relateEntity = ?");
    allSql.put("mergeOpportunityEntityRoll","UPDATE opportunity SET entityId = ? WHERE entityId = ?");
    allSql.put("mergeProjectsRoll","UPDATE projectlink SET recordId = ? WHERE recordId = ? AND recordTypeId = ?");
    allSql.put("mergeTicketsEntityRoll","UPDATE ticket SET entityId = ? WHERE entityId = ?");
    allSql.put("mergeOrdersEntityRoll", "UPDATE cvorder SET entityId = ? WHERE entityId = ?");
    allSql.put("mergePaymentsEntityRoll", "UPDATE payment SET entityId = ? WHERE entityId = ?");
    allSql.put("mergeExpenseEntityRoll","UPDATE expense SET entityId = ? WHERE entityId = ?");
    allSql.put("mergePurchaseOrderEntityRoll","UPDATE purchaseorder SET entity = ? WHERE entity = ?");
    allSql.put("mergeItemVendorEntityRoll","UPDATE item SET vendorId = ? WHERE vendorId = ?");
    allSql.put("mergeItemManufacturerEntityRoll","UPDATE item SET manufacturerId = ? WHERE manufacturerId = ?");
    allSql.put("mergeVendorRoll","UPDATE vendor SET entityId = ? WHERE entityId = ?");
    allSql.put("mergeInventoryVendorRoll","UPDATE inventory SET vendorId = ? WHERE vendorId = ?");
    allSql.put("mergeInventoryCustomerRoll","UPDATE inventory SET customerId = ? WHERE customerId = ?");
    allSql.put("mergeInvoiceEntityRoll","UPDATE invoice SET customerId = ? WHERE customerId = ?");
    // end Queries for Merge rollUpEntityOrphans


    // Queries for Merge rollUpEntityOrphans
    allSql.put("mergeNoteIndividualRoll","UPDATE note SET relateIndividual = ? WHERE relateIndividual = ?");
    allSql.put("mergeFileIndividualRoll","UPDATE cvfile SET relateIndividual = ? WHERE relateIndividual = ?");
    allSql.put("mergeOpportunityIndividualRoll","UPDATE opportunity SET IndividualID = ? WHERE IndividualID = ?");
    allSql.put("mergeTicketsIndividualRoll","UPDATE ticket SET individualid = ? WHERE individualid = ?");
    allSql.put("mergeProposalIndividualRoll","UPDATE proposal SET individualid = ? WHERE individualid = ?");
    // end Queries for Merge rollUpEntityOrphans

  }
}
