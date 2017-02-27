/*
 * $RCSfile: SyncFacadeEJB.java,v $    $Revision: 1.3 $  $Date: 2005/06/10 17:52:30 $ - $Author: mking_cv $
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


package com.centraview.syncfacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.note.NoteVO;

/**
 * Used by the CompanionLink Sync API, this is EJB acts
 * as a conduit/facade EJB between the Struts Sync layer
 * code and the CentraView EJB layer.
 */

public class SyncFacadeEJB implements SessionBean
{
  private SessionContext sessionCtx;
  private String dataSource = "MySqlDS";

  public void ejbCreate() { }
  public void ejbLoad() { }
  public void ejbRemove() { }
  public void ejbStore() { }
  public void ejbActivate() { }
  public void ejbPassivate() { }

  public void setSessionContext(SessionContext context)
  {
    sessionCtx = context;
  }

  public void unsetSessionContext()
  {
    sessionCtx = null;
  }


  /**
   * Queries database for Notes for a given user, and returns a
   * <i>Collection</i> of <i>HashMaps</i> representing each Note.
   * Be sure to set <i>individualID</i> to the ID of an INDIVIDUAL
   * record, NOT a userID. This is very important.
   *
   * @param individualID    The individualID of the user whose notes we
   * are returning.
   * @return  <i>Collection</i> of <i>HashMaps</i>, each representing
   * one Note. The <i>Collection</i> returned will be <i>null</i> when no
   * records are found.
   * @deprecated We no longer sync note records, instead we sync the "notes" field
   *             of contact records in Outlook. This method should no longer be
   *             used by any calling code.
   */
  public Collection getNotesList(int individualID)
  {
    CVDal cvdl = new CVDal(dataSource);

    // IMPORTANT: record permissions are integrated into this query
    // for the sake of efficiency. Make sure you do not check record
    // permissions while you are looping through the results this
    // method/query returns, as that would defeat the purpose of this
    // efficiency here.
    String sql = "SELECT DISTINCT n.NoteID, n.Title, n.Detail, n.DateUpdated " +
                 "FROM note n LEFT JOIN recordauthorisation ra ON (n.NoteID=ra.recordid) " +
                 "LEFT JOIN module m ON (ra.recordtypeid=m.moduleid) " +
                 "WHERE ((n.Owner=?) OR (m.name='Notes' and ra.individualid=?))";

    cvdl.setSqlQuery(sql);
    cvdl.setInt(1, individualID);
    cvdl.setInt(2, individualID);

    Collection sqlResults = cvdl.executeQuery();
    Collection notesList = null;

    if (sqlResults != null)
    {
      notesList = sqlResults;
    }

    cvdl.clearParameters();
    cvdl.destroy();

    return(notesList);
  }   // end getNotesList() method

  /**
   * Gets information from the Sync API and creates a new note record
   * in the database. Be sure to set <i>individualID</i> to the ID of an
   * INDIVIDUAL record, NOT a userID. This is very important.
   *
   * @param individualID    The individualID of the user who will own the created note.
   * @param noteVO    A <i>NoteVO</i> object representing the note to be created.
   * @return  int - the new noteID for success, 0 (zero) for failure
   * @deprecated We no longer sync note records, instead we sync the "notes" field
   *             of contact records in Outlook. This method should no longer be
   *             used by any calling code.
   */
  public int addNote(int individualID, NoteVO noteVO)throws AuthorizationFailedException
  {
    if (! CVUtility.isModuleVisible("Notes", individualID, this.dataSource))
    {
      throw new AuthorizationFailedException("[SyncFacadeEJB]: User cannot create new Notes.");
    }

    int newNoteID = 0;
    try
    {
      CVDal cvdl = new CVDal(dataSource);

      cvdl.setSqlQuery("INSERT INTO note (Title, Detail, Owner, Creator, UpdatedBy, Priority, DateCreated) VALUES (?, ?, ?, ?, ?, 'MEDIUM', NOW())");
      cvdl.setString(1, noteVO.getTitle());
      cvdl.setString(2, noteVO.getDetail());
      cvdl.setInt(3, individualID);
      cvdl.setInt(4, individualID);
      cvdl.setInt(5, individualID);

      cvdl.executeUpdate();

      newNoteID = cvdl.getAutoGeneratedKey();

    }catch(Exception e){
      System.out.println("[Exception][SyncFacadeEJB] Exception thrown in addNote(): " + e);
      //e.printStackTrace();
      return(0);
    }
    return(newNoteID);
  }   // end addNote() method

  /**
   * Updates a note record in the database with the values set in the
   * <i>NoteVO</i> which is passed in. Only fields which are set on the
   * <i>NoteVO</i> are actually updated. All other fields are left as-is.
   * The <b>NoteID</b> field on the <i>NoteVO</i> <b>MUST</b> be set.
   *
   * @param noteVO    A <i>NoteVO</i> object representing the fields which
   * are to be changed. <b>Only</b> fields set on this VO are updated.
   * @return  boolean - <b>true</b> for success, <b>false</b> for failure
   * @deprecated We no longer sync note records, instead we sync the "notes" field
   *             of contact records in Outlook. This method should no longer be
   *             used by any calling code.
   */
  public boolean editNote(NoteVO noteVO)
  {
    boolean result = false;

    if (noteVO != null)
    {
      CVDal cvdl = new CVDal(dataSource);
      String updateQuery = this.generateUpdateQuery(noteVO);

      if (updateQuery == null || updateQuery.equals(""))
      {
        return(false);
      }

      cvdl.setSqlQuery(updateQuery);

      try
      {
        cvdl.executeUpdate();
        result = true;
      }catch(Exception e){
        System.out.println("[Exception][] Exception thrown in editNote(): " + e);
        //e.printStackTrace();
        result = false;
      }
    }
    return(result);
  }   // end editNote() method

  /**
   * Generates a SQL query to update a note record based on the
   * values passed in with the <i>NoteVO</i>; only fields that are set
   * on the <i>NoteVO</i> are included in the query. All other fields
   * are left as-is and <b>not included</b> in the query.
   *
   * @param noteVO    A <i>NoteVO</i> object representing the fields which
   * are to be changed. <b>Only</b> fields set on this VO are updated.
   * @return  A <i>String</i> representation of the SQL query.
   */
  private String generateUpdateQuery(NoteVO noteVO)
  {
    StringBuffer query = new StringBuffer("");
    if (noteVO != null)
    {
      query.append("UPDATE note SET ");

      String title = noteVO.getTitle();
      if (title != null && ! title.equals(""))
      {
        query.append("Title='" + title + "', ");
      }

      String detail = noteVO.getDetail();
      if (detail != null && ! detail.equals(""))
      {
        query.append("Detail='" + detail + "', ");
      }

      // IMPORTANT: I'm setting NoteID=NoteID in the next part of this
      // query because it's easier than doing String manipulation to make
      // sure there is no trailing comma (,) at the end of the previous
      // parts of the query :-)
      query.append("NoteID=NoteID WHERE NoteID=" + noteVO.getNoteId());
    }
    return(query.toString());
  }   // end generateUpdateQuery() method

  /**
   * Deletes a note from the database that matches the given <b>noteID</b>
   * and where the given <b>individualID</b> equals the note's Owner
   * field. individualID must be an ID of an Individual record,
   * <b>NOT</b> a userID.
   *
   * @param noteID The NoteID of the note to be delete from the database.
   * @param individualID The individualID that matches the Owner field in the database.
   * on the note record to be deleted. If it does not match, the delete is not made.
   * @return  boolean - <b>true</b> for success, <b>false</b> for failure
   * @deprecated We no longer sync note records, instead we sync the "notes" field
   *             of contact records in Outlook. This method should no longer be
   *             used by any calling code.
   */
  public boolean deleteNote(int noteID, int individualID) throws AuthorizationFailedException
  {
    boolean result = false;
    if (noteID != 0 && individualID != 0)
    {
      // check to see if the user has the privilege to delete this record
      if (! CVUtility.canPerformRecordOperation(individualID, "Notes", noteID, 10, this.dataSource))
      {
        throw new AuthorizationFailedException("Fail-Permision: User cannot delete this Note.");
      }

      CVDal cvdl = new CVDal(dataSource);
      cvdl.setSqlQuery("DELETE FROM note WHERE NoteID=? AND Owner=?");
      cvdl.setInt(1, noteID);
      cvdl.setInt(2, individualID);

      try
      {
        cvdl.executeUpdate();
        result = true;
      }catch(Exception e){
        System.out.println("[Exception][SyncFacadeEJB] Exception thrown in deleteNote(): " + e);
        //e.printStackTrace();
        return(false);
      }finally{
        cvdl.destroy();
        cvdl = null;
      }
    }
    return(result);
  }   // end deleteNote() method

  public HashMap getRelatedActivityInfo(int activityID)
  {
    HashMap relatedInfo = new HashMap();

    if (activityID != 0)
    {
      CVDal cvdl = new CVDal(dataSource);
      cvdl.setSqlQuery("SELECT 'Individual' AS Type, CONCAT(i.FirstName, ' ', i.LastName) AS Name FROM activity a RIGHT JOIN activitylink ali ON (a.ActivityID=ali.ActivityID) LEFT JOIN individual i ON (ali.RecordID=i.IndividualID) WHERE ali.RecordTypeID=2 AND a.ActivityID=? UNION SELECT 'Entity' AS Type, e.name AS Name FROM activity a RIGHT JOIN activitylink ale ON (a.ActivityID=ale.ActivityID) LEFT JOIN entity e ON (ale.RecordID=e.EntityID) WHERE ale.RecordTypeID=1 AND a.ActivityID=?");
      cvdl.setInt(1, activityID);
      cvdl.setInt(2, activityID);

      try
      {
        Collection sqlResults = cvdl.executeQuery();

        if (sqlResults != null)
        {
          Iterator iter = sqlResults.iterator();
          while (iter.hasNext())
          {
            HashMap sqlMap = (HashMap)iter.next();

            if (sqlMap.get("Type").equals("Entity"))
            {
              relatedInfo.put("Entity", sqlMap.get("Name"));
            }else if (sqlMap.get("Type").equals("Individual")){
              relatedInfo.put("Individual", sqlMap.get("Name"));
            }
          }
        }
      }catch(Exception e){
        System.out.println("[Exception][SyncFacadeEJB] Exception thrown in deleteNote(): " + e);
        //e.printStackTrace();
      }finally{
        cvdl.destroy();
        cvdl = null;
      }
    }

    return(relatedInfo);
  }   // end getActivityRelatedInfo() method

  /**
   * Finds an EntityID in the database that matches a given entity
   * Name using a little advanced string matching. If no match is
   * found, then this returns 0.
   * @param companyName The entity Name which we are looking for the ID of (does not need to be an exact string match)
   * @param individualID The individualID of the user who is logged in. Helps us limit the records to search against.
   * @return int - EntityID that matches, 0 for no match
   */
  public int findCompanyNameMatch(String companyName, int individualID)
  {
    int newEntityID = 0;
    if ((companyName != null && ! companyName.equals("")) && individualID != 0)
    {
      CVDal cvdl = new CVDal(dataSource);
      cvdl.setSqlQuery("SELECT e.EntityID, e.Name FROM entity e LEFT JOIN publicrecords p ON (e.EntityID=p.recordid) WHERE p.moduleid=14 UNION SELECT e.EntityID, e.Name FROM entity e LEFT JOIN recordauthorisation r ON (e.EntityID=r.recordID) WHERE r.individualID=? AND r.recordtypeid=14 and r.privilegelevel < 40 UNION SELECT e.EntityID, e.Name FROM entity e WHERE e.Owner=?");
      cvdl.setInt(1, individualID);
      cvdl.setInt(2, individualID);

      try
      {
        String formattedCompanyName = companyName.replaceAll("[^a-zA-Z_0-9]", "");
        Collection sqlResults = cvdl.executeQuery();

        if (sqlResults != null)
        {
          Iterator iter = sqlResults.iterator();
          while (iter.hasNext())
          {
            HashMap sqlMap = (HashMap)iter.next();
            String tmpName = (String)sqlMap.get("Name");

            String formattedEntityName = null;
            if (tmpName != null)
            {
              formattedEntityName = tmpName.replaceAll("[^a-zA-Z_0-9]", "");
            }

            if (formattedCompanyName.toLowerCase().equals(formattedEntityName.toLowerCase()))
            {
              newEntityID = ((Number)sqlMap.get("EntityID")).intValue();
              //Integer.parseInt((String)sqlMap.get("EntityID"));
              break;
            }
          }   // end while() loop
        }   // end if (sqlResults != null)
      }catch(Exception e){
        System.out.println("[Exception][SyncFacadeEJB] Exception thrown in findCompanyNameMatch(): " + e);
        //e.printStackTrace();
      }finally{
        cvdl.destroy();
        cvdl = null;
      }
    }
    return(newEntityID);
  }

  /**
   * Returns a HashMap containing the <b>raw</b> recurring
   * information for a given activityID. This "raw" data is
   * the database values for "Every" and "RecurrOn" from the
   * "recurrnece" table.
   * @param activityID (int) activityID whose data we are looking for
   * @return (HashMap) containing two values "Every" and "RecurrOn"
   */
  public HashMap getRecurringInfo(int activityID)
  {
    HashMap recurringInfo = null;
    if (activityID != 0)
    {
      CVDal cvdl = new CVDal(dataSource);
      cvdl.setSqlQuery("SELECT Every, RecurrOn FROM recurrence WHERE ActivityID=?");
      cvdl.setInt(1, activityID);

      try
      {
        Collection sqlResults = cvdl.executeQuery();

        if (sqlResults != null)
        {
          Iterator iter = sqlResults.iterator();
          while (iter.hasNext())
          {
            recurringInfo = (HashMap)iter.next();
            // there should only be one record. If there is more than one
            // record, then something went wrong while creating this activity
            break;
          }   // end while() loop
        }   // end if (sqlResults != null)
      }catch(Exception e){
        System.out.println("[Exception][SyncFacadeEJB] Exception thrown in findCompanyNameMatch(): " + e);
      }finally{
        cvdl.destroy();
        cvdl = null;
      }
    }   // end if (activityID != 0)
    return(recurringInfo);
  }   // end getRecurringInfo() method

  /**
   * Creates a record in the "recurrence" table for a recurring activity
   * that was created via the CompanionLink Sync API. This method was
   * created because the existing Activity code did not allow for setting
   * the "raw" values for this table.
   * @param activityID The activityID that will be recurring.
   * @param recurrenceType The type of recurring activity ("DAY", "WEEK", "MONTH", "YEAR").
   * @param every The value for the recurrence.every field in the database.
   * @param recurrOn The value for the recurrence.recurrOn fied in the database.
   * @param startDate The starting date of the recurrence.
   * @param endDate The ending date of the recurrence.
   * @return boolean - true for success, false for failure
   */
  public boolean setRecurringFields(int activityID, String recurrenceType, int every, int recurrOn, Date startDate, Date endDate)
  {
    boolean result = false;
    // first, validate data
    boolean allDataValid = true;
    if (activityID == 0)
    {
      allDataValid = false;
    }

    if (recurrenceType == null || recurrenceType.equals("") || (! recurrenceType.equals("DAY") && ! recurrenceType.equals("WEEK") && ! recurrenceType.equals("MONTH") && ! recurrenceType.equals("YEAR")))
    {
      allDataValid = false;
    }

    if (allDataValid)
    {
      CVDal cvdl = new CVDal(dataSource);

      try
      {
        // change the java.util.Date objects into java.sql.Date objects
        java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
        java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());

        cvdl.setSqlQuery("INSERT INTO recurrence (ActivityID, Every, TimePeriod, Until, startDate, RecurrOn) VALUES (?, ?, ?, ?, ?, ?)");
        cvdl.setInt(1, activityID);
        cvdl.setInt(2, every);
        cvdl.setString(3, recurrenceType);
        cvdl.setRealDate(4, sqlEndDate);
        cvdl.setRealDate(5, sqlStartDate);
        cvdl.setInt(6, recurrOn);

        int sqlResult = cvdl.executeUpdate();

        if (sqlResult != 0)
        {
          result = true;
        }
      }catch(Exception e){
        System.out.println("[Exception][SyncFacadeEJB] Exception thrown in setRecurringFields(): " + e);
        //e.printStackTrace();
      }finally{
        cvdl.destroy();
        cvdl = null;
      }
    }
    return(result);
  }   // end setRecurringFields() method

  /**
   * Update the record in the "recurrence" table for the given activity
   * that was created via the CompanionLink Sync API. This method will
   * create the recurrence record if the activity is not currently recurring.
   * @param activityID The activityID to be updated.
   * @param recurrenceType The type of recurring activity ("DAY", "WEEK", "MONTH", "YEAR").
   * @param every The value for the recurrence.every field in the database.
   * @param recurrOn The value for the recurrence.recurrOn fied in the database.
   * @param startDate The starting date of the recurrence.
   * @param endDate The ending date of the recurrence.
   * @return boolean - true for success, false for failure
   */
  public boolean updateRecurringFields(int activityID, String recurrenceType, int every, int recurrOn, Date startDate, Date endDate)
  {
    boolean result = false;

    if (activityID > 0) {
      CVDal cvdl = new CVDal(dataSource);

      try {
        // check to see if the recurring record exists already
        boolean recurringExists = false;
        Number recurrenceID = new Integer(0);
        cvdl.setSqlQuery("SELECT RecurrenceID FROM recurrence WHERE ActivityID=?");
        cvdl.setInt(1, activityID);

        Collection sqlResults = cvdl.executeQuery();

        if (sqlResults != null) {
          Iterator iterator = sqlResults.iterator();
          while (iterator.hasNext()) {
            HashMap sqlRow = (HashMap)iterator.next();
            recurrenceID = (Number)sqlRow.get("RecurrenceID");
            break;
          }

          if (recurrenceID.intValue() != 0) {
            recurringExists = true;
          }
        }   // end if (sqlResults != null)

        cvdl.clearParameters();

        if (recurringExists) {
          // the recurring record exists, so update the existing
          // record with the values that are not null
          StringBuffer updateSql = new StringBuffer("UPDATE recurrence SET recurrenceID=recurrenceID");

          if (recurrenceType != null && ! recurrenceType.equals("")) {
            if (recurrenceType.equals("DAY") || recurrenceType.equals("WEEK") || recurrenceType.equals("MONTH") || recurrenceType.equals("YEAR")) {
              // update the recurrenceType field
              updateSql.append(", TimePeriod='" + recurrenceType + "'");
            }
          }

          if (every > 0) {
            updateSql.append(", Every=" + every);
          }

          if (recurrOn > 0) {
            updateSql.append(", RecurrOn=" + recurrOn);
          }

          if (startDate != null) {
            updateSql.append(", startdate=" + startDate.toString());
          }

          if (endDate != null) {
            updateSql.append(", Until=" + endDate.toString());
          }

          updateSql.append(" WHERE activityID=" + activityID);

          cvdl.setSqlQuery(updateSql.toString());

          int updateResult = cvdl.executeUpdate();

          if (updateResult == 0) {
            result = false;
          }
        }else{
          // the recurring record does not exists, so create
          // a new record in the recurring table. Make sure all
          // fields have been supplied first.
          result = true;    // next operation might not happen...so assume success at this point
          if (recurrenceType != null || every != 0 || recurrOn != 0 || startDate != null || endDate != null) {
            result = false;   // now set flag back to false, and get result from operation
            // if any *one* recurrence field is not null, then the Sync
            // client is trying to add a new recurring record, thus we
            // must validate the data
            boolean insertResult = this.setRecurringFields(activityID, recurrenceType, every, recurrOn, startDate, endDate);

            if (insertResult != false) {
              result = true;
            }
          }   // end if (recurrenceType != null || every != null || recurrOn != null || startDate != null || endDate != null)
        }   // end if (recurringExists)
      }catch(Exception e){
        System.out.println("[Exception][SyncFacadeEJB] Exception thrown in updateRecurringFields(): " + e);
        //e.printStackTrace();
      }finally{
        cvdl.destroy();
        cvdl = null;
      }
    }
    return(result);
  }   // end setRecurringFields() method

  /**
   * Takes a list of record IDs and a module ID, and deletes
   * all records in "recordauthorisation" and "publicrecords"
   * tables that match both the moduleID and recordID. This
   * is used when the user's preference is to sync all records
   * as private, because our EJB layer will originally create
   * new records with the user's default privileges, so we'll
   * have to modify those privileges in order to make the records
   * private.
   * @param moduleID The moduleID of the list of records being deleted.
   * @param recordIDs An ArrayList of Integer objects representing
   *        the records to be deleted.
   * @return void
   */
  public void markRecordsPrivate(int moduleID, ArrayList recordIDs)
  {
    CVDal cvdal = new CVDal(this.dataSource);
    try
    {
      if (recordIDs != null && recordIDs.size() > 0)
      {
        StringBuffer inClause = new StringBuffer("(");
        Iterator iter = recordIDs.iterator();
        while (iter.hasNext())
        {
          Integer recordID = (Integer)iter.next();
          inClause.append(recordID.toString());
          if (iter.hasNext())
          {
            inClause.append(", ");
          }
        }
        inClause.append(")");

        String recordAuthSql = "DELETE FROM recordauthorisation WHERE recordtypeid=? AND recordid IN " + inClause.toString();
        cvdal.setSqlQuery(recordAuthSql);
        cvdal.setInt(1, moduleID);
        cvdal.executeUpdate();

        cvdal.clearParameters();
        cvdal.setSqlQueryToNull();

        String publicSql = "DELETE FROM publicrecords WHERE moduleid=? AND recordID IN " + inClause.toString();
        cvdal.setSqlQuery(publicSql);
        cvdal.setInt(1, moduleID);
        cvdal.executeUpdate();
      }
    }catch(Exception e){
      System.out.println("[Exception][SyncFacadeEJB] markRecordsPrivate():" + e);
      // TODO: remove stack trace after version 1.0.15
      e.printStackTrace();
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
  }   // end markRecordsPrivate(int,ArrayList);

  /**
   * Returns a Collection of HashMaps, each representing a fully-populated
   * Individual record for use by the Sync API.
   * @param individualID The individualID of the user requesting the list.
   * @return Collection of HashMaps representing the individual list.
   */
  public Collection getIndividualList(int individualID)
  {
    Collection individualList = null;

    if (individualID < 0)
    {
      return individualList;
    }

    CVDal cvdal = new CVDal(this.dataSource);

    try
    {
      // Ok, first create the temporary table since the more complex
      // joins for contact methods are actually impossible with our
      // data structure. When we create the table, we'll populate it
      // with all the info from the "individual" table, as well as
      // the primary address information.

      // NOTE that I've created a temp variable to hold the SELECT part
      // of the query, because we'll be UNION-ing three queries, all of
      // which will have identical SELECT clauses, and this makes it
      // easier to maintain.
      String select = "SELECT i.individualID AS contactID, i.Modified AS lastModified, e.Name AS companyName, " +
                      "i.FirstName AS firstName, i.MiddleInitial AS middleName, i.LastName AS lastName, i.Title AS title, " +
                      "a.Street1 AS street1, a.Street2 AS street2, a.City AS city, a.State AS state, a.Zip AS zipCode, " +
                      "a.Country AS country, moc.Content AS email, moc.Content AS workPhone, " +
                      "moc.Content AS homePhone, moc.Content AS faxPhone, moc.Content AS otherPhone, " +
                      "moc.Content AS mainPhone, moc.Content AS pagerPhone, moc.Content AS mobilePhone, sn.content AS notes ";

      String query = "CREATE TEMPORARY TABLE `syncIndividualList` " + select +
        "FROM individual i LEFT OUTER JOIN entity e ON (i.Entity=e.EntityID) " +
        "LEFT OUTER JOIN addressrelate ar ON (i.IndividualID=ar.Contact) " +
        "LEFT OUTER JOIN address a ON (ar.Address=a.AddressID) " +
        "LEFT OUTER JOIN methodofcontact moc ON (moc.MOCID=0) " +
        "LEFT OUTER JOIN syncnote sn ON (i.individualID=sn.recordID AND sn.recordTypeID=2) " +
        "WHERE ar.contacttype=2 AND i.owner=? " +

        "UNION " + select +
        "FROM publicrecords pub LEFT JOIN individual i ON (pub.recordid=i.individualid) " +
        "LEFT OUTER JOIN entity e ON  (i.Entity=e.EntityID) " +
        "LEFT OUTER JOIN addressrelate ar ON (i.IndividualID=ar.Contact) " +
        "LEFT OUTER JOIN address a ON (ar.Address=a.AddressID) " +
        "LEFT OUTER JOIN methodofcontact moc ON (moc.MOCID=0) " +
        "LEFT OUTER JOIN syncnote sn ON (i.individualID=sn.recordID AND sn.recordID=2) " +
        "WHERE ar.contacttype=2 AND pub.moduleid=15 " +

        "UNION " + select +
        "FROM recordauthorisation auth LEFT JOIN individual i ON (auth.recordid=i.individualid) " +
        "LEFT OUTER JOIN entity e ON (i.Entity=e.EntityID) " +
        "LEFT OUTER JOIN addressrelate ar ON (i.IndividualID=ar.Contact) " +
        "LEFT OUTER JOIN address a ON (ar.Address=a.AddressID) " +
        "LEFT OUTER JOIN methodofcontact moc ON (moc.MOCID=0) " +
        "LEFT OUTER JOIN syncnote sn ON (i.individualID=sn.recordID AND sn.recordID=2) " +
        "WHERE ar.contacttype=2 AND auth.individualid=? AND auth.recordtypeid=15 AND auth.privilegelevel<40 ";

      cvdal.setSqlQuery(query);
      cvdal.setInt(1, individualID);
      cvdal.setInt(2, individualID);
      cvdal.executeUpdate();

      // make sure you clean up cvdal after each query...
      cvdal.clearParameters();
      cvdal.setSqlQueryToNull();

      // Now that we've got our temp table, populated with all the records
      // we need, we'll have to update the email and phone number fields.

      // Let's start with email...
      cvdal.setSqlQuery("UPDATE syncIndividualList sil, methodofcontact moc, mocrelate mr SET sil.email=moc.Content, sil.lastModified=sil.lastModified WHERE sil.contactID=mr.ContactID AND moc.MOCID=mr.MOCID AND moc.MOCType=1 AND mr.contacttype=2 AND mr.isPrimary='YES'");
      cvdal.executeUpdate();
      cvdal.clearParameters();
      cvdal.setSqlQueryToNull();

      // ...workPhone...
      cvdal.setSqlQuery("UPDATE syncIndividualList sil, methodofcontact moc, mocrelate mr SET sil.workPhone=moc.Content, sil.lastModified=sil.lastModified WHERE sil.contactID=mr.ContactID AND moc.MOCID=mr.MOCID AND mr.contacttype=2 AND moc.syncAs='Work'");
      cvdal.executeUpdate();
      cvdal.clearParameters();
      cvdal.setSqlQueryToNull();

      // ...homePhone...
      cvdal.setSqlQuery("UPDATE syncIndividualList sil, methodofcontact moc, mocrelate mr SET sil.homePhone=moc.Content, sil.lastModified=sil.lastModified WHERE sil.contactID=mr.ContactID AND moc.MOCID=mr.MOCID AND mr.contacttype=2 AND moc.syncAs='Home'");
      cvdal.executeUpdate();
      cvdal.clearParameters();
      cvdal.setSqlQueryToNull();

      // ...faxPhone...
      cvdal.setSqlQuery("UPDATE syncIndividualList sil, methodofcontact moc, mocrelate mr SET sil.faxPhone=moc.Content, sil.lastModified=sil.lastModified WHERE sil.contactID=mr.ContactID AND moc.MOCID=mr.MOCID AND mr.contacttype=2 AND moc.syncAs='Fax'");
      cvdal.executeUpdate();
      cvdal.clearParameters();
      cvdal.setSqlQueryToNull();

      // ...otherPhone...
      cvdal.setSqlQuery("UPDATE syncIndividualList sil, methodofcontact moc, mocrelate mr SET sil.otherPhone=moc.Content, sil.lastModified=sil.lastModified WHERE sil.contactID=mr.ContactID AND moc.MOCID=mr.MOCID AND mr.contacttype=2 AND moc.syncAs='Other'");
      cvdal.executeUpdate();
      cvdal.clearParameters();
      cvdal.setSqlQueryToNull();

      // ...mainPhone...
      cvdal.setSqlQuery("UPDATE syncIndividualList sil, methodofcontact moc, mocrelate mr SET sil.mainPhone=moc.Content, sil.lastModified=sil.lastModified WHERE sil.contactID=mr.ContactID AND moc.MOCID=mr.MOCID AND mr.contacttype=2 AND moc.syncAs='Main'");
      cvdal.executeUpdate();
      cvdal.clearParameters();
      cvdal.setSqlQueryToNull();

      // ...pagerPhone...
      cvdal.setSqlQuery("UPDATE syncIndividualList sil, methodofcontact moc, mocrelate mr SET sil.pagerPhone=moc.Content, sil.lastModified=sil.lastModified WHERE sil.contactID=mr.ContactID AND moc.MOCID=mr.MOCID AND mr.contacttype=2 AND moc.syncAs='Pager'");
      cvdal.executeUpdate();
      cvdal.clearParameters();
      cvdal.setSqlQueryToNull();

      // ...mobilePhone...
      cvdal.setSqlQuery("UPDATE syncIndividualList sil, methodofcontact moc, mocrelate mr SET sil.mobilePhone=moc.Content, sil.lastModified=sil.lastModified WHERE sil.contactID=mr.ContactID AND moc.MOCID=mr.MOCID AND mr.contacttype=2 AND moc.syncAs='Mobile'");
      cvdal.executeUpdate();
      cvdal.clearParameters();
      cvdal.setSqlQueryToNull();

      // Now that all the data in our temp table is correct, let's get
      // the data and give it to the Struts layer...
      cvdal.setSqlQuery("SELECT * FROM syncIndividualList");
      Collection sqlResults = cvdal.executeQuery();
      if (sqlResults != null)
      {
        individualList = sqlResults;
      }

      cvdal.clearParameters();
      cvdal.setSqlQueryToNull();

      // Don't forget to drop the temp table...
      cvdal.setSqlQuery("DROP TABLE IF EXISTS syncIndividualList");
      cvdal.executeUpdate();
    }catch(Exception e){
      System.out.println("[Exception][SyncFacadeEJB] getIndividualList():" + e);
      // TODO: remove stack trace after version 1.0.16
      e.printStackTrace();
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return individualList;
  }   // end getIndividualList(int)

  /**
   * Returns a Collection of HashMaps, each representing a fully-populated
   * Activity record for use by the Sync API.
   * @param individualID The individualID of the user requesting the list.
   * @return Collection of HashMaps representing the activity list.
   */
  public Collection getActivityList(int individualID)
  {
    Collection activityList = null;

    if (individualID < 0)
    {
      return activityList;
    }

    CVDal cvdal = new CVDal(this.dataSource);

    try
    {
      // Ok, first create the temporary table since the more complex
      // joins for related contacts are actually impossible with our
      // data structure. When we create the table, we'll populate it
      // with all the info from the "activity" and "recurrence" tables,
      // as well as the reminder information.
      String query = "CREATE TEMPORARY TABLE `syncActivityList` " +
                     "SELECT a.ActivityID AS activityID, at.Name AS activityType, a.Title AS title, " +
                     "a.Modified AS lastModified, a.dueDate AS dueDate, ap.Name AS priority, " +
                     "stat.Name AS status, CONCAT(cb.FirstName, ' ', cb.lastName) AS createdBy, " +
                     "a.Start AS startDateTime, a.End AS endDateTime, a.Details AS description, " +
                     "a.visibility AS private, r.startdate AS recurrenceStartDate, " +
                     "r.Until AS recurrenceEndDate, r.TimePeriod AS recurrenceType, r.Every AS every, " +
                     "r.RecurrOn AS recurrOn, remind.ActionTime AS alarmDateTime, " +
                     "e.Name AS linkCompany, CONCAT(i.FirstName, ' ', i.LastName) AS linkContact " +
                     "FROM activity a LEFT JOIN activitystatus stat ON (a.Status=stat.StatusID) " +
                     "LEFT JOIN activitypriority ap ON (a.Priority=ap.PriorityID) " +
                     "LEFT JOIN activitytype at ON (a.`Type`=at.TypeID) " +
                     "LEFT JOIN individual cb ON (a.Creator=cb.IndividualID) " +
                     "LEFT JOIN recurrence r ON (a.ActivityID=r.ActivityID) " +
                     "LEFT JOIN activityaction aa ON (a.ActivityID=aa.ActivityID) " +
                     "LEFT JOIN action remind ON (aa.ActionID=remind.ActionID) " +
                     "LEFT JOIN entity e ON (e.EntityID=0) " +
                     "LEFT JOIN individual i ON (i.IndividualID=0) " +
                     "WHERE a.owner=? "+
                     " UNION  " +
                     "SELECT a.ActivityID AS activityID, at.Name AS activityType, a.Title AS title,   " +
                     "a.Modified AS lastModified, a.dueDate AS dueDate, ap.Name AS priority,   " +
                     "stat.Name AS status, CONCAT(cb.FirstName, ' ', cb.lastName) AS createdBy,   " +
                     "a.Start AS startDateTime, a.End AS endDateTime, a.Details AS description,   " +
                     "a.visibility AS private, r.startdate AS recurrenceStartDate,   " +
                     "r.Until AS recurrenceEndDate, r.TimePeriod AS recurrenceType, r.Every AS every, " +
                     "r.RecurrOn AS recurrOn, remind.ActionTime AS alarmDateTime,   " +
                     "e.Name AS linkCompany, CONCAT(i.FirstName, ' ', i.LastName) AS linkContact   " +
                     "FROM attendee atten, activity a LEFT JOIN activitystatus stat ON (a.Status=stat.StatusID)   " +
                     "LEFT JOIN activitypriority ap ON (a.Priority=ap.PriorityID)   " +
                     "LEFT JOIN activitytype at ON (a.`Type`=at.TypeID)   " +
                     "LEFT JOIN individual cb ON (a.Creator=cb.IndividualID)   " +
                     "LEFT JOIN recurrence r ON (a.ActivityID=r.ActivityID)   " +
                     "LEFT JOIN activityaction aa ON (a.ActivityID=aa.ActivityID)   " +
                     "LEFT JOIN action remind ON (aa.ActionID=remind.ActionID)   " +
                     "LEFT JOIN entity e ON (e.EntityID=0)   " +
                     "LEFT JOIN individual i ON (i.IndividualID=0)   " +
                     "where a.ActivityID = atten.ActivityID and atten.IndividualID=? " ;

      cvdal.setSqlQuery(query);
      cvdal.setInt(1, individualID);
			cvdal.setInt(2, individualID);
      cvdal.executeUpdate();

      // make sure you clean up cvdal after each query...
      cvdal.clearParameters();
      cvdal.setSqlQueryToNull();

      // Now that we've got our temp table, populated with all the records
      // we need, we'll have to update the related contacts fields.

      // Let's start with entity...
      cvdal.setSqlQuery("UPDATE syncActivityList sal, activitylink al, entity e SET sal.linkCompany=e.Name WHERE sal.activityID=al.ActivityID AND al.RecordID=e.EntityID AND al.RecordTypeID=1");
      cvdal.executeUpdate();
      cvdal.clearParameters();
      cvdal.setSqlQueryToNull();

      // ...now individual...
      cvdal.setSqlQuery("UPDATE syncActivityList sal, activitylink al, individual i SET sal.linkContact=CONCAT(i.FirstName, ' ', i.LastName) WHERE sal.activityID=al.ActivityID AND al.RecordID=i.IndividualID AND al.RecordTypeID=2");
      cvdal.executeUpdate();
      cvdal.clearParameters();
      cvdal.setSqlQueryToNull();

      // Now that all the data in our temp table is correct, let's get
      // the data and give it to the Struts layer...
      cvdal.setSqlQuery("SELECT * FROM syncActivityList");
      Collection sqlResults = cvdal.executeQuery();
      if (sqlResults != null)
      {
        activityList = sqlResults;
      }

      cvdal.clearParameters();
      cvdal.setSqlQueryToNull();

      // ...Done. Don't forget to drop the temp table...
      cvdal.setSqlQuery("DROP TABLE IF EXISTS syncActivityList");
      cvdal.executeUpdate();
    }catch(Exception e){
      System.out.println("[Exception][SyncFacadeEJB] getActivityList():" + e);
      // TODO: remove stack trace after version 1.0.16
      e.printStackTrace();
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return activityList;
  }   // end getActivityList(int) method

	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * This simply sets the target datasource to be used for DB interaction
	 * @param ds A string that contains the cannonical JNDI name of the datasource
	 */
	 public void setDataSource(String ds) {
	 	this.dataSource = ds;
	 }

}   // end class definition

