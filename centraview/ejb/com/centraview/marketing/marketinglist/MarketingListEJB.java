/*
 * $RCSfile: MarketingListEJB.java,v $    $Revision: 1.3 $  $Date: 2005/09/08 12:22:24 $ - $Author: mcallist $
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

package com.centraview.marketing.marketinglist;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimeZone;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;

import org.apache.log4j.Logger;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.common.DateMember;
import com.centraview.common.EJBUtil;
import com.centraview.common.IntMember;
import com.centraview.common.StringMember;
import com.centraview.marketing.EventAtendeesList;
import com.centraview.marketing.EventAtendeesListElement;
import com.centraview.marketing.EventList;
import com.centraview.marketing.EventListElement;
import com.centraview.marketing.MarketingList;
import com.centraview.marketing.MarketingListElement;
import com.centraview.marketing.MarketingListMemberList;
import com.centraview.marketing.MarketingListMemberListElement;
import com.centraview.marketing.PromotionList;
import com.centraview.marketing.PromotionListElement;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

/**
 * This class is a Statefull session Bean which acts as a Interface for
 * Marketing Module
 */

public class MarketingListEJB implements SessionBean {
  private static Logger logger = Logger.getLogger(MarketingListEJB.class);
  protected javax.ejb.SessionContext ctx;
  protected Context environment;
  private String dataSource = "MySqlDS";

  /**
   * This method sets the context for this Bean
   */
  public void setSessionContext(SessionContext ctx) throws RemoteException
  {
    this.ctx = ctx;
  }

  /**
   * These are life cycle methods from EJB
   */
  public void ejbActivate()
  {}

  public void ejbPassivate()
  {}

  public void ejbRemove()
  {}

  public void ejbCreate()
  {}

  /**
   * Thismethod returns EventList from local Bean
   */
  public EventList getEventList(int userId, HashMap preference) throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("ListManager", userId, this.dataSource))
      throw new AuthorizationFailedException("ListManager - getEventList");

    Integer startATparam = (Integer)preference.get("startATparam");
    Integer EndAtparam = (Integer)preference.get("EndAtparam");
    String searchString = (String)preference.get("searchString");
    String sortmem = (String)preference.get("sortmem");
    Character chr = (Character)preference.get("sortType");

    char sorttype = chr.charValue();
    int startat = startATparam.intValue();
    int endat = EndAtparam.intValue();
    int begainindex = Math.max(startat - 100, 1);
    int endindex = endat + 100;

    EventList DL = new EventList();
    DL.setSortMember(sortmem);
    CVDal cvdl = new CVDal(dataSource);

    if (searchString == null)
      searchString = "";

    String appendStr = "";
    String strSQL = "";
    String sortType = "";
    if (sorttype == 'D') {
      sortType = " DESC ";
    }
    strSQL = "select e.eventid eventid, e.title, detail,start,end, concat(ind.firstname, ' ', ind.lastname) ownername, e.owner, count(er.eventid) registeredattendees from individual ind , `event` e left outer join `eventregister` er on e.eventid = er.eventid where e.owner = ind.individualid group by e.eventid order by "
        + sortmem + sortType + " LIMIT " + (begainindex - 1) + " , " + (endindex + 1) + ";";

    if (searchString.startsWith("SIMPLE :")) {
      searchString = searchString.substring(8);
      appendStr = " where  (e.eventid like '%" + searchString + "%'   OR e.title like '%"
          + searchString + "%' OR ind.firstname like  '%" + searchString
          + "%' OR ind.lastname like  '%" + searchString + "%' OR detail like  '%" + searchString
          + "%' OR start like  '%" + searchString + "%' OR end like  '%" + searchString
          + "%' ) and ";

      strSQL = strSQL.replaceFirst("where ", appendStr);
    }

    if ((null != searchString) && (searchString.startsWith("ADVANCE:"))) {
      int searchIndex = (searchString.toUpperCase()).indexOf("WHERE");
      strSQL = "select event.eventid eventid, event.title, detail,start,end, concat(ind.firstname, ' ', ind.lastname) ownername"
          + ", event.owner, count(er.eventid) registeredattendees from individual ind , "
          + "`event` left outer join `eventregister` er on event.eventid = er.eventid "
          + "where event.owner = ind.individualid and "
          + searchString.substring((searchIndex + 5), searchString.length())
          + " group by event.eventid order by "
          + sortmem
          + sortType
          + " LIMIT "
          + (begainindex - 1) + " , " + (endindex + 1) + ";";

    }

    cvdl.setSqlQuery(strSQL);
    Collection v = cvdl.executeQuery();
    cvdl.clearParameters();
    cvdl.destroy();

    Iterator it = v.iterator();
    TimeZone tz = TimeZone.getTimeZone("EST");
    int i = 0;

    while (it.hasNext()) {
      i++;
      HashMap hm = (HashMap)it.next();

      int ID = ((Long)hm.get("eventid")).intValue();
      IntMember eventid = new IntMember("eventid", ID, 10, "", 'T', false, 10);
      StringMember title = new StringMember("title", (String)hm.get("title"), 10, "", 'T', true);
      StringMember detail = new StringMember("detail", (String)hm.get("detail"), 10, "", 'T', false);
      DateMember start = new DateMember("start", (java.util.Date)hm.get("start"), 10, "URL", 'T',
          false, 100, "EST");
      DateMember end = new DateMember("end", (java.util.Date)hm.get("end"), 10, "URL", 'T', false,
          100, "EST");
      StringMember ownername = new StringMember("ownername", (String)hm.get("ownername"), 10, "",
          'T', true);
      // Object test = hm.get("registeredattendees");
      int regAtt = ((Number)hm.get("registeredattendees")).intValue();
      IntMember registeredattendees = new IntMember("registeredattendees", regAtt, 10, "", 'T',
          false, 10);
      int ownrid = ((Long)hm.get("owner")).intValue();
      IntMember ownerid = new IntMember("ownerid", ownrid, 10, "", 'T', false, 10);

      EventListElement ele = new EventListElement(ID);

      ele.put("eventid", eventid);
      ele.put("title", title);
      ele.put("detail", detail);
      ele.put("start", start);
      ele.put("end", end);
      ele.put("ownerid", ownerid);
      ele.put("ownername", ownername);
      ele.put("registeredattendees", registeredattendees);

      // added by Sameer for generating fixed length sort key from i
      StringBuffer sb = new StringBuffer("00000000000");
      sb.setLength(11);
      String str = (new Integer(i)).toString();
      sb.replace((sb.length() - str.length()), (sb.length()), str);
      String newOrd = sb.toString();

      DL.put(newOrd, ele);
      DL.setTotalNoOfRecords(DL.size());
      DL.setListType("Event");
      DL.setBeginIndex(begainindex);
      DL.setEndIndex(endindex);

    }

    return DL;

  }

  /**
   * Thismethod returns PromotionList from local Bean
   */
  public PromotionList getPromotionList(int userId, HashMap preference)
      throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Promotion", userId, this.dataSource))
      throw new AuthorizationFailedException("Promotion - getPromotionList");

    Integer startATparam = (Integer)preference.get("startATparam");
    Integer EndAtparam = (Integer)preference.get("EndAtparam");
    String searchString = (String)preference.get("searchString");
    String sortmem = (String)preference.get("sortmem");
    Character chr = (Character)preference.get("sortType");

    char sorttype = chr.charValue();
    int startat = startATparam.intValue();
    int endat = EndAtparam.intValue();
    int begainindex = Math.max(startat - 100, 1);
    int endindex = endat + 100;

    PromotionList DL = new PromotionList();
    DL.setSortMember(sortmem);
    CVDal cvdl = new CVDal(dataSource);

    if (searchString == null)
      searchString = "";

    String appendStr = "";
    String strSQL = "";
    String sortType = "";
    if (sorttype == 'D') {
      sortType = " DESC ";
    }

    strSQL = "select e.Promotionid promotionid, e.title, description,startdate ,enddate, status, 0 as ordercount from `promotion` e order by "
        + sortmem + sortType + " LIMIT " + (begainindex - 1) + " , " + (endindex + 1) + ";";

    if (searchString.startsWith("SIMPLE :")) {
      searchString = searchString.substring(8);
      appendStr = " where  (e.Promotionid like '%" + searchString + "%' OR e.title like '%"
          + searchString + "%' OR description like  '%" + searchString
          + "%' 	OR startdate like  '%" + searchString + "%' OR enddate like  '%" + searchString
          + "%' OR status like  '%" + searchString + "%' ) order by  ";
      strSQL = strSQL.replaceFirst("order by", appendStr);
    }

    if ((null != searchString) && (searchString.startsWith("ADVANCE:"))) {
      int searchIndex = (searchString.toUpperCase()).indexOf("WHERE");
      strSQL = "select promotion.Promotionid promotionid, promotion.title, description,startdate ,enddate,"
          + "status, 0 as ordercount from `promotion`  where "
          + searchString.substring((searchIndex + 5), searchString.length())
          + " order by "
          + sortmem + sortType + " LIMIT " + (begainindex - 1) + " , " + (endindex + 1) + ";";
    }

    cvdl.setSqlQuery(strSQL);
    Collection v = cvdl.executeQuery();
    cvdl.clearParameters();
    cvdl.destroy();

    Iterator it = v.iterator();
    TimeZone tz = TimeZone.getTimeZone("EST");
    int i = 0;

    while (it.hasNext()) {
      i++;
      HashMap hm = (HashMap)it.next();

      int ID = ((Long)hm.get("promotionid")).intValue();
      IntMember promotionid = new IntMember("promotionid", ID, 10, "", 'T', false, 10);
      StringMember title = new StringMember("title", (String)hm.get("title"), 10, "", 'T', true);
      StringMember description = new StringMember("description", (String)hm.get("description"), 10,
          "", 'T', false);
      DateMember startdate = new DateMember("startdate", (java.util.Date)hm.get("startdate"), 10,
          "URL", 'T', false, 100, "EST");
      DateMember enddate = new DateMember("enddate", (java.util.Date)hm.get("enddate"), 10, "URL",
          'T', false, 100, "EST");
      StringMember status = new StringMember("status", (String)hm.get("status"), 10, "", 'T', false);
      // int nooforder = ((Long)hm.get("nooforder")).intValue();
      int nooforder = Integer.parseInt(hm.get("ordercount").toString());
      IntMember nooforders = new IntMember("nooforders", nooforder, 10, "", 'T', false, 10);

      PromotionListElement ele = new PromotionListElement(ID);

      ele.put("promotionid", promotionid);
      ele.put("title", title);
      ele.put("description", description);
      ele.put("startdate", startdate);
      ele.put("enddate", enddate);
      ele.put("status", status);
      ele.put("nooforders", nooforders);

      // added by Sameer for generating fixed length sort key from i
      StringBuffer sb = new StringBuffer("00000000000");
      sb.setLength(11);
      String str = (new Integer(i)).toString();

      sb.replace((sb.length() - str.length()), (sb.length()), str);
      String newOrd = sb.toString();

      DL.put(newOrd, ele);
      DL.setTotalNoOfRecords(DL.size());
      DL.setListType("Promotion");
      DL.setBeginIndex(begainindex);
      DL.setEndIndex(endindex);

    }

    return DL;

  }

  /**
   * Returns a MarketingListMemberList based on the List ID and a set of
   * parameters.
   * <p>
   * The preferences HashMap must contain the following elements:
   * <ul>
   * <li>startATparam</li>
   * <li>EndAtparam</li>
   * <li>searchString</li>
   * <li>sortmem</li>
   * <li>sortType</li>
   * </ul>
   * @param userID The Individual ID of the user accessing this list.
   * @param marketingListID The parent Marketing List ID.
   * @param preferences The list Preferences.
   * @return A MarketingListMemberList for the appropriate import list.
   */
  public MarketingListMemberList getMarketingListMemberList(int userID, int marketingListID,
      HashMap preferences) throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("ListManager", userID, this.dataSource)) {
      throw new AuthorizationFailedException("ListManager - getMarketingListMemberList");
    }

    MarketingListMemberList returnList = new MarketingListMemberList();

    Integer startAtParam = (Integer)preferences.get("startATparam");
    Integer EndAtParam = (Integer)preferences.get("EndAtparam");
    String searchString = (String)preferences.get("searchString");
    String sortMember = (String)preferences.get("sortmem");
    Character sortTypeCharacter = (Character)preferences.get("sortType");

    char sortType = sortTypeCharacter.charValue();
    int startAt = startAtParam.intValue();
    int endAt = EndAtParam.intValue();
    int beginIndex = Math.max(startAt - 100, 1);
    int endIndex = endAt + 100;

    CVDal dataConnection = new CVDal(dataSource);

    try {
      // Create the table
      dataConnection
          .setSqlQuery("CREATE TEMPORARY TABLE marketinglistmembers (EntityID int(10) unsigned default null, Entity varchar(40) default null, IndividualID int(10) unsigned default null, Individual varchar (51) default null, PhoneNumber varchar(50) default null, Email varchar(50) default null, PRIMARY KEY (IndividualID)) TYPE=HEAP");
      dataConnection.executeUpdate();
      dataConnection.clearParameters();

      // Insert the Entities
      // dataConnection.setSqlQuery("INSERT INTO marketinglistmembers (EntityID,
      // Entity) SELECT DISTINCT ent.EntityID, ent.name from entity ent where
      // ent.list = ?");
      // dataConnection.setInt(1, marketingListID);
      // dataConnection.executeUpdate();
      // dataConnection.clearParameters();
      //
      // //Insert the entity Phone Number
      // dataConnection.setSqlQuery("UPDATE marketinglistmembers, mocrelate,
      // methodofcontact, moctype, contacttype SET
      // marketinglistmembers.PhoneNumber = methodofcontact.Content WHERE
      // marketinglistmembers.EntityID = mocrelate.ContactID AND mocrelate.MOCID
      // = methodofcontact.MOCID AND methodofcontact.MOCType = moctype.MOCTypeID
      // AND moctype.Name = 'Phone' AND contacttype.contacttypeid =
      // mocrelate.ContactType AND contacttype.Name = 'Entity'");
      // dataConnection.executeUpdate();
      // dataConnection.clearParameters();
      //
      // //Insert the entity Email Address
      // dataConnection.setSqlQuery("UPDATE marketinglistmembers, mocrelate,
      // methodofcontact, moctype, contacttype SET marketinglistmembers.Email =
      // methodofcontact.Content WHERE marketinglistmembers.EntityID =
      // mocrelate.ContactID AND mocrelate.MOCID = methodofcontact.MOCID AND
      // methodofcontact.MOCType = moctype.MOCTypeID AND moctype.Name = 'Email'
      // AND contacttype.contacttypeid = mocrelate.ContactType AND
      // contacttype.Name = 'Entity'");
      // dataConnection.executeUpdate();
      // dataConnection.clearParameters();

      // Insert the individuals
      dataConnection
          .setSqlQuery("INSERT INTO marketinglistmembers (EntityID, Entity, IndividualID, Individual) SELECT DISTINCT ind.Entity, ent.name, ind.individualID, concat(ind.FirstName, ' ', ind.LastName) from individual ind left join entity ent on ind.entity = ent.entityid where ind.list = ?");
      dataConnection.setInt(1, marketingListID);
      dataConnection.executeUpdate();
      dataConnection.clearParameters();

      // Insert the individual Phone Number
      dataConnection
          .setSqlQuery("UPDATE marketinglistmembers, mocrelate, methodofcontact, moctype, contacttype SET marketinglistmembers.PhoneNumber = methodofcontact.Content WHERE marketinglistmembers.individualID = mocrelate.ContactID AND mocrelate.MOCID = methodofcontact.MOCID AND methodofcontact.MOCType = moctype.MOCTypeID AND moctype.Name = 'Phone' AND contacttype.contacttypeid = mocrelate.ContactType AND contacttype.Name = 'Individual'");
      dataConnection.executeUpdate();
      dataConnection.clearParameters();

      // Insert the individual Email Address
      dataConnection
          .setSqlQuery("UPDATE marketinglistmembers, mocrelate, methodofcontact, moctype, contacttype SET marketinglistmembers.Email = methodofcontact.Content WHERE marketinglistmembers.individualID = mocrelate.ContactID AND mocrelate.MOCID = methodofcontact.MOCID AND methodofcontact.MOCType = moctype.MOCTypeID AND moctype.Name = 'Email' AND contacttype.contacttypeid = mocrelate.ContactType AND contacttype.Name = 'Individual'");
      dataConnection.executeUpdate();
      dataConnection.clearParameters();

      // do the search
      returnList.setSortMember(sortMember);

      if (searchString == null) {
        searchString = "";
      } // end of if statement (searchString == null)

      StringBuffer query = new StringBuffer();
      query
          .append("SELECT EntityID, Entity, IndividualID, Individual, PhoneNumber, Email FROM marketinglistmembers ");
      query.append("order by " + sortMember + " ");

      if (sortType == 'A') {
        query.append("ASC ");
      } // end of if statement (sortType == 'A')
      else {
        query.append(" DESC ");
      } // end of else statement (sortType == 'A')

      // add the limit statement
      // query.append(" LIMIT " + (beginIndex - 1) + " , " + (endIndex + 1));

      dataConnection.setSqlQuery(query.toString());
      Collection results = dataConnection.executeQuery();

      // drop the table
      dataConnection.setSqlQuery("DROP TABLE marketinglistmembers");
      dataConnection.executeUpdate();
      dataConnection.clearParameters();

      if (results != null) {
        Iterator iterator = results.iterator();
        int i = 1;

        while (iterator.hasNext()) {
          HashMap hashMap = (HashMap)iterator.next();

          // int entityIntID = ((Long) hashMap.get("EntityID")).intValue();
          // int individualIntID = ((Long)
          // hashMap.get("IndividualID")).intValue();

          StringMember entityID;
          StringMember individualID;

          if (hashMap.get("EntityID") != null) {
            entityID = new StringMember("EntityID", ((Long)hashMap.get("EntityID")).toString(), 10,
                "", 'T', true);
          } else {
            entityID = new StringMember("EntityID", null, 10, "", 'T', true);
          }

          if (hashMap.get("IndividualID") != null) {
            individualID = new StringMember("IndividualID", ((Long)hashMap.get("IndividualID"))
                .toString(), 10, "", 'T', true);
          } else {
            individualID = new StringMember("IndividualID", null, 10, "", 'T', true);
          }

          StringMember entityName = new StringMember("Entity", (String)hashMap.get("Entity"), 10,
              "", 'T', true);
          StringMember individualName = new StringMember("Individual", (String)hashMap
              .get("Individual"), 10, "", 'T', true);
          StringMember emailAddress = new StringMember("Email", (String)hashMap.get("Email"), 10,
              "", 'T', true);
          StringMember phoneNumber = new StringMember("PhoneNumber", (String)hashMap
              .get("PhoneNumber"), 10, "", 'T', false);

          MarketingListMemberListElement currentElement = new MarketingListMemberListElement(i);

          currentElement.put("EntityID", entityID);
          currentElement.put("IndividualID", individualID);
          currentElement.put("Entity", entityName);
          currentElement.put("Individual", individualName);
          currentElement.put("Email", emailAddress);
          currentElement.put("PhoneNumber", phoneNumber);

          // TODO Move code to Delete Handler to make sure
          // we get the correct Element.
          StringBuffer sb = new StringBuffer("00000000000");
          sb.setLength(11);
          String countString = Integer.toString(i);
          sb.replace((sb.length() - countString.length()), (sb.length()), countString);
          String newElementID = sb.toString();

          returnList.put(newElementID, currentElement);
          i++;
        } // end of while loop (iterator.hasNext())
      } // end of if statement (results != null)

      returnList.setTotalNoOfRecords(returnList.size());
      returnList.setListType("MarketingListMembers");
      returnList.setBeginIndex(beginIndex);
      returnList.setEndIndex(endIndex);

      // do stuff
    } // end of try block
    catch (Exception e) {
      System.out
          .println("[Exception] MarketingListEJB.getMarketingListMemberList: " + e.toString());
      // e.printStackTrace();
    } // end of catch block (Exception)
    finally {
      dataConnection.destroy();
      dataConnection = null;
    } // end of finally block
    return returnList;
  } // end of getMarketingListMemberList method

  /**
   * Thismethod returns EventAtendeesList from local Bean
   */
  public EventAtendeesList getEventAtendeesList(int userId, HashMap preference)
      throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("ListManager", userId, this.dataSource))
      throw new AuthorizationFailedException("MarketingList - getEventAtendeesList");

    Integer startATparam = (Integer)preference.get("startATparam");
    Integer EndAtparam = (Integer)preference.get("EndAtparam");
    String searchString = (String)preference.get("searchString");
    String sortmem = (String)preference.get("sortmem");
    Character chr = (Character)preference.get("sortType");
    int eventID = Integer.parseInt(preference.get("eventId").toString());

    char sorttype = chr.charValue();
    int startat = startATparam.intValue();
    int endat = EndAtparam.intValue();
    int begainindex = Math.max(startat - 100, 1);
    int endindex = endat + 100;

    EventAtendeesList DL = new EventAtendeesList();
    DL.setSortMember(sortmem);
    CVDal cvdl = new CVDal(dataSource);

    cvdl.setSql("EventAtendees.individuallistcreatetable");
    cvdl.executeUpdate();
    cvdl.clearParameters();

    cvdl.setSql("EventAtendees.individuallistinsert");
    cvdl.setInt(1, eventID);
    cvdl.executeUpdate();
    cvdl.clearParameters();

    cvdl.setSql("EventAtendees.individuallistupdate");
    cvdl.executeUpdate();
    cvdl.clearParameters();

    if (searchString == null)
      searchString = "";

    String appendStr = "";
    String strSQL = "";

    if (sortmem == null) {
      sortmem = "individualname";
    }

    if (sortmem.equals("name"))
      sortmem = "individualname";

    if (sorttype == 'A') {
      strSQL = "Select individualID individualid,concat(FirstName ,'  ', LastName) individualname,Email email, accepted from individuallist order by "
          + sortmem + " LIMIT " + (begainindex - 1) + " , " + (endindex + 1) + ";";
    } else {
      strSQL = "Select individualID individualid,concat(FirstName ,'  ', LastName) individualname,Email email, accepted from individuallist order by "
          + sortmem + " DESC LIMIT " + (begainindex - 1) + " , " + (endindex + 1) + ";";
    }

    if (searchString.startsWith("SIMPLE :")) {
      searchString = searchString.substring(8);
      appendStr = " where  lr.literatureid like '%" + searchString + "%'   OR lr.title like '%"
          + searchString + "%' OR ind.firstname like  '%" + searchString
          + "%' OR ind.lastname like  '%" + searchString + "%' OR ac.created like  '%"
          + searchString + "%' OR en.name like  '%" + searchString + "%' and ";
      // strSQL = strSQL.replaceFirst("where ", appendStr);
    }

    cvdl.setSqlQuery(strSQL);
    Collection v = cvdl.executeQuery();

    cvdl.setSql("EventAtendees.individuallistdroptable");
    cvdl.executeUpdate();

    cvdl.clearParameters();
    cvdl.destroy();

    Iterator it = v.iterator();
    TimeZone tz = TimeZone.getTimeZone("EST");
    int i = 0;

    while (it.hasNext()) {
      i++;
      HashMap hm = (HashMap)it.next();

      int ID = ((Long)hm.get("individualid")).intValue();
      IntMember individualid = new IntMember("individualid", ID, 10, "", 'T', false, 10);
      StringMember individualname = new StringMember("individualname", (String)hm
          .get("individualname"), 10, "", 'T', true);
      StringMember email = new StringMember("email", (String)hm.get("email"), 10, "", 'T', true);
      StringMember accepted = new StringMember("accepted", (String)hm.get("accepted"), 10, "", 'T',
          false);
      EventAtendeesListElement ele = new EventAtendeesListElement(ID);

      ele.put("individualid", individualid);
      ele.put("individualname", individualname);
      ele.put("email", email);
      ele.put("accepted", accepted);

      // added by Sameer for generating fixed length sort key from i
      StringBuffer sb = new StringBuffer("00000000000");
      sb.setLength(11);
      String str = (new Integer(i)).toString();

      sb.replace((sb.length() - str.length()), (sb.length()), str);
      String newOrd = sb.toString();

      DL.put(newOrd, ele);
      DL.setTotalNoOfRecords(DL.size());
      DL.setListType("EventAtendees");
      DL.setBeginIndex(begainindex);
      DL.setEndIndex(endindex);

    }

    return DL;

  }

  public MarketingList getMarketingList(int userId, HashMap preference)
      throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("ListManager", userId, this.dataSource)) {
      throw new AuthorizationFailedException("ListManager - getMarketingList");
    }

    MarketingList DL = new MarketingList();

    try {
      Integer startATparam = (Integer)preference.get("startATparam");
      Integer EndAtparam = (Integer)preference.get("EndAtparam");
      String searchString = (String)preference.get("searchString");
      String sortmem = (String)preference.get("sortmem");
      Character chr = (Character)preference.get("sortType");
      sortmem = "description";
      char sorttype = chr.charValue();
      int startat = startATparam.intValue();
      int endat = EndAtparam.intValue();
      int begainindex = Math.max(startat - 100, 1);
      int endindex = endat + 100;

      DL.setSortMember(sortmem);

      CVDal cvdl = new CVDal(dataSource);

      if (searchString == null) {
        searchString = "";
      }
      String appendStr = "";
      String strSQL = "";

      String sqlSortType = new String("ASC");
      if (sorttype != 'A') {
        sqlSortType = "ORDER BY " + sortmem + " DESC";
      }

      strSQL = " select ml.listID , count(o.OpportunityID) Opportunity ,sum(o.ForecastAmmount) OpportunityValue,count(cvo.Orderid) ordercount ,sum(cvo.total) ordervalue "
          + " from  entity e , marketinglist ml LEFT OUTER JOIN cvorder cvo ON (e.entityid=cvo.entityid and cvo.orderstatus='Active')  "
          + " LEFT OUTER JOIN opportunity o ON (e.entityid=o.entityid) "
          + " where e.list=ml.listID group by ml.ListID ";

      cvdl.setSqlQuery(strSQL);
      Collection OrderOpportunity = cvdl.executeQuery();
      cvdl.clearParameters();

      HashMap OrderOpportunityList = new HashMap();

      Iterator itOrderOpportunity = OrderOpportunity.iterator();

      if (OrderOpportunity != null && OrderOpportunity.size() != 0) {

        while (itOrderOpportunity.hasNext()) {
          HashMap hm = (HashMap)itOrderOpportunity.next();
          HashMap OrderOpportunityValue = new HashMap();
          int ListID = ((Long)hm.get("listID")).intValue();

          int Opportunity = 0;
          double OpportunityValue = 0.0;
          int order = 0;
          double ordervalue = 0.0;

          if (hm.get("Opportunity") != null) {
            Opportunity = ((Number)hm.get("Opportunity")).intValue();
          }

          if (hm.get("OpportunityValue") != null) {
            OpportunityValue = ((Number)hm.get("OpportunityValue")).doubleValue();
          }

          if (hm.get("ordercount") != null) {
            order = ((Number)hm.get("ordercount")).intValue();
          }

          if (hm.get("ordervalue") != null) {
            ordervalue = ((Number)hm.get("ordervalue")).doubleValue();
          }

          OrderOpportunityValue.put("Opportunity", Opportunity + "");
          OrderOpportunityValue.put("OpportunityValue", OpportunityValue + "");
          OrderOpportunityValue.put("order", order + "");
          OrderOpportunityValue.put("ordervalue", ordervalue + "");
          OrderOpportunityList.put(ListID + "", OrderOpportunityValue);
        }
      }

      strSQL = "";

      // first, get all records that the logged-in user owns
      // then, get all records that are marked as "Public"
      // then, get all records that the logged-in user has
      // been given permissions to view (or greater)
      strSQL = "SELECT m.ListID, m.title, m.description, COUNT(i.IndividualID) "
          + "AS numberofrecords, m.proposals, m.orders, m.ordervalue "
          + "FROM marketinglist m LEFT JOIN individual i ON (m.ListId=i.List) "
          + "WHERE m.status='YES' AND m.owner="
          + userId
          + " "
          + "GROUP BY m.ListID "
          + " UNION "
          + "SELECT m.ListID, m.title, m.description, COUNT(i.IndividualID) "
          + "AS numberofrecords, m.proposals, m.orders, m.ordervalue "
          + "FROM marketinglist m LEFT JOIN individual i ON (m.ListId=i.List) "
          + "LEFT JOIN publicrecords p ON (m.ListID=p.recordid) "
          + "WHERE m.status='YES' AND p.moduleid=32 "
          + "GROUP BY m.ListID "
          + " UNION "
          + "SELECT m.ListID, m.title, m.description, COUNT(i.IndividualID) "
          + "AS numberofrecords, m.proposals, m.orders, m.ordervalue "
          + "FROM marketinglist m, recordauthorisation ra LEFT JOIN individual i ON (m.ListId=i.List) "
          + "WHERE m.listid=ra.recordid AND ra.individualid=" + userId
          + " AND ra.recordtypeid=32 AND " + "ra.privilegelevel <= 30 AND m.status='YES' "
          + "GROUP BY m.ListID " + sqlSortType + " LIMIT " + (begainindex - 1) + ", "
          + (endindex + 1);

      if (searchString.startsWith("SIMPLE :")) {
        searchString = searchString.substring(8);
        appendStr = "WHERE (ListID LIKE '%" + searchString + "%' OR m.title LIKE '%" + searchString
            + "%' OR " + "description LIKE  '%" + searchString + "%' OR numberofrecords LIKE  '%"
            + searchString + "%' OR " + "proposals LIKE  '%" + searchString
            + "%'  OR orders LIKE  '%" + searchString + "%' OR " + "ordervalue LIKE '%"
            + searchString + "%') AND ";
        strSQL = strSQL.replaceFirst("where", appendStr);
      }

      if ((null != searchString) && (searchString.startsWith("ADVANCE:"))) {
        int searchIndex = (searchString.toUpperCase()).indexOf("WHERE");

        strSQL = " SELECT marketinglist.ListID, marketinglist.title, marketinglist.description, COUNT(i.IndividualID) "
            + "AS numberofrecords, marketinglist.proposals, marketinglist.orders, marketinglist.ordervalue "
            + "FROM marketinglist LEFT JOIN individual i ON (ListId=i.List) "
            + "WHERE status='YES' AND "
            + searchString.substring((searchIndex + 5), searchString.length())
            + " GROUP BY ListID " + sqlSortType;
      }

      cvdl.setSqlQuery(strSQL);
      Collection v = cvdl.executeQuery();

      cvdl.clearParameters();
      cvdl.destroy();

      Iterator it = v.iterator();
      int i = 0;

      while (it.hasNext()) {
        i++;
        HashMap hm = (HashMap)it.next();

        int ID = ((Long)hm.get("ListID")).intValue();
        IntMember intmem = new IntMember("ListID", ID, 10, "", 'T', true, 10);
        StringMember entity = new StringMember("title", (String)hm.get("title"), 10, "", 'T', true);

        /*
         * PLEASE NOTE: this code *MUST* be cleaned up!!!! the variable names
         * here are EXTREMELY confusing because someone copied and pasted code
         * from another location, and never changed the variable names to make
         * any sense within this scope. ie: "whorequested" makes no sense, as
         * there is no such field in the Marketing Lists module.
         */
        StringMember whorequested = new StringMember("description", (String)hm.get("description")
            .toString(), 10, "", 'T', false);
        StringMember daterequested = new StringMember("numberofrecords", (String)hm.get(
            "numberofrecords").toString(), 10, "URL", 'T', false);
        StringMember literaturename = new StringMember("proposals", (String)hm.get("proposals")
            .toString(), 10, "", 'T', true);

        StringMember orders = new StringMember("opportunities", "0", 10, "", 'T', false);
        StringMember ordervalue = new StringMember("opportunityvalue", "0.00", 10, "", 'T', false);

        StringMember opportunity = new StringMember("orders", "0", 10, "", 'T', false);
        StringMember opportunityvalue = new StringMember("ordervalue", "0.00", 10, "", 'T', false);

        boolean listFlag = OrderOpportunityList.containsKey(ID + "");

        if (listFlag) {
          HashMap orderOpportunityValues = (HashMap)OrderOpportunityList.get(ID + "");
          opportunity = new StringMember("opportunities", (String)orderOpportunityValues
              .get("Opportunity"), 10, "", 'T', false);
          opportunityvalue = new StringMember("opportunityvalue", (String)orderOpportunityValues
              .get("OpportunityValue"), 10, "", 'T', false);
          orders = new StringMember("orders", (String)orderOpportunityValues.get("order"), 10, "",
              'T', false);
          ordervalue = new StringMember("ordervalue", (String)orderOpportunityValues
              .get("ordervalue"), 10, "", 'T', false);
        }

        MarketingListElement ele = new MarketingListElement(ID);

        ele.put("ListID", intmem);
        ele.put("title", entity);
        ele.put("description", whorequested);
        ele.put("numberofrecords", daterequested);
        ele.put("proposals", literaturename);
        ele.put("Order", orders);
        ele.put("OrderValue", ordervalue);
        ele.put("Opportunities", opportunity);
        ele.put("OpportunityValue", opportunityvalue);

        StringBuffer sb = new StringBuffer("00000000000");
        sb.setLength(11);
        String str = (new Integer(i)).toString();

        sb.replace((sb.length() - str.length()), (sb.length()), str);
        String newOrd = sb.toString();

        DL.put(newOrd, ele);
        DL.setTotalNoOfRecords(DL.size());
        DL.setListType("Marketing");
        DL.setBeginIndex(begainindex);
        DL.setEndIndex(endindex);
      } // end while(it.hasNext())
    } catch (Exception e) {
      e.printStackTrace();
    }
    return (DL);
  } // end getMarketingList() method

  /**
   * @author Kevin McAllister <kevin@centraview.com> This simply sets the target
   *         datasource to be used for DB interaction
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  }

  /**
   * Returns a ValueListVO representing a list of Literature records, based on
   * the <code>parameters</code> argument which limits results.
   */
  public ValueListVO getLitValueList(int individualId, ValueListParameters parameters)
  {
    ArrayList list = new ArrayList();

    boolean applyFilter = false;
    String filter = parameters.getFilter();

    CVDal cvdl = new CVDal(this.dataSource);
    if (filter != null && filter.length() > 0) {
      String str = "CREATE TABLE litlistfilter " + filter;
      cvdl.setSqlQuery(str);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      applyFilter = true;
    }

    String query = this.buildLitListQuery(applyFilter, individualId, cvdl, parameters);
    cvdl.setSqlQuery(query);
    list = cvdl.executeQueryList(1);

    if (applyFilter) {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE litlistfilter");
      cvdl.executeUpdate();
    }

    cvdl.destroy();
    cvdl = null;
    return new ValueListVO(list, parameters);
  }

  private String buildLitListQuery(boolean applyFilter, int individualId, CVDal cvdl,
      ValueListParameters parameters)
  {
    // Create table column definitions
    String create = "CREATE TEMPORARY TABLE litlist ";
    String select = "SELECT lr.ActivityId, a.Title, a.title placeholder, lr.requestedBy, "
        + "concat(i.FirstName, ' ', i.LastName) AS Who, a.created, dm.name AS "
        + "DelMethod, e.name AS Entity, e.entityId ";
    String from = "FROM literaturerequest lr ";

    String joinConditions = " LEFT OUTER JOIN individual AS i ON (lr.requestedBy = i.individualId) "
        + " LEFT OUTER JOIN deliverymethod AS dm ON (dm.deliveryid = lr.deliverymethod) "
        + " LEFT OUTER JOIN entity AS e ON (i.entity = e.entityId) "
        + " LEFT OUTER JOIN activity AS a ON (a.activityId = lr.activityId) ";
    String where = "";
    if (applyFilter) {
      joinConditions += " LEFT OUTER JOIN litlistfilter llf ";
      joinConditions += " ON (llf.ActivityId = lr.ActivityId) ";
    }

    // So horrible...
    cvdl.setSqlQuery(create + select + from + joinConditions + where);
    int count = cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();
    parameters.setTotalRecords(count);
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("DROP TABLE litlist");
    cvdl.executeUpdate();

    String orderBy = "ORDER BY "
        + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
    String limit = parameters.getLimitParam();

    return (select + from + joinConditions + where + orderBy + limit);
  }

  /**
   * Returns a ValueListVO representing a list of ListManager records, based on
   * the <code>parameters</code> argument which limits results.
   */
  public ValueListVO getListManagerValueList(int individualID, ValueListParameters parameters)
  {
    ArrayList list = new ArrayList();

    boolean permissionSwitch = individualID < 1 ? false : true;
    boolean applyFilter = false;
    String filter = parameters.getFilter();

    CVDal cvdl = new CVDal(this.dataSource);
    if (filter != null && filter.length() > 0) {
      String str = "CREATE TABLE listmanagerlistfilter " + filter;
      cvdl.setSqlQuery(str);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      applyFilter = true;
    }
    int numberOfRecords = 0;
    if (applyFilter)
      numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "listmanagerlistfilter", individualID,
          32, "marketinglist", "ListID", "Owner", null, permissionSwitch);
    else if (permissionSwitch)
      numberOfRecords = EJBUtil.buildListFilterTable(cvdl, null, individualID, 32, "marketinglist",
          "ListID", "Owner", null, permissionSwitch);
    parameters.setTotalRecords(numberOfRecords);

    String str = "CREATE TEMPORARY TABLE opportunityorders SELECT ml.ListID, COUNT(o.OpportunityID) "
        + "AS OpportunityCount, SUM(o.ForecastAmmount) AS OpportunityValue, COUNT(cvo.OrderID) AS OrderCount, "
        + "SUM(cvo.Total) OrderValue FROM  entity e, marketinglist ml LEFT OUTER JOIN cvorder cvo ON "
        + "(e.EntityID = cvo.EntityID AND cvo.OrderStatus = 'Active') LEFT OUTER JOIN opportunity o ON "
        + "(e.EntityID = o.EntityID) WHERE e.List = ml.ListID GROUP BY ml.ListID";
    cvdl.setSqlQuery(str);
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();

    String query = this.buildListManagerListQuery(applyFilter, permissionSwitch, parameters);
    cvdl.setSqlQuery(query);
    list = cvdl.executeQueryList(1);
    cvdl.setSqlQueryToNull();

    if (applyFilter) {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE listmanagerlistfilter");
      cvdl.executeUpdate();
    }
    if (applyFilter || permissionSwitch) {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE listfilter");
      cvdl.executeUpdate();
    }
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("DROP TABLE opportunityorders");
    cvdl.executeUpdate();

    cvdl.destroy();
    cvdl = null;
    return new ValueListVO(list, parameters);
  }

  private String buildListManagerListQuery(boolean applyFilter, boolean permissionSwitch,
      ValueListParameters parameters)
  {
    String select = "SELECT ml.ListID, ml.Title, ml.Description, COUNT(i.IndividualID) AS NumberOfRecords, ml.Proposals, "
        + "oo.OrderCount, oo.OrderValue, oo.OpportunityCount, oo.OpportunityValue ";

    String joinConditions = "LEFT OUTER JOIN individual i ON ml.ListId = i.List "
        + "LEFT OUTER JOIN opportunityorders oo ON ml.ListID = oo.ListID ";

    StringBuffer from = new StringBuffer(" FROM marketinglist ml ");
    StringBuffer where = new StringBuffer("WHERE ml.status = 'YES' ");
    String groupBy = "GROUP BY ml.ListID ";
    String orderBy = "ORDER BY "
        + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
    String limit = parameters.getLimitParam();

    // If a filter is applied we need to do an additional join against the
    // temporary entity list filter table.
    if (applyFilter || permissionSwitch) {
      from.append(", listfilter AS lf ");
      where.append("AND ml.ListID = lf.ListID ");
    }
    // Build up the actual query using all the different permissions.
    // Where owner = passed individualId
    StringBuffer query = new StringBuffer();
    query.append(select);
    query.append(from);
    query.append(joinConditions);
    query.append(where);
    query.append(groupBy);
    query.append(orderBy);
    query.append(limit);
    return query.toString();
  }

  /**
   * Returns a ValueListVO representing a list of Promotion records, based on
   * the <code>parameters</code> argument which limits results.
   */
  public ValueListVO getPromotionValueList(int individualID, ValueListParameters parameters)
  {
    ArrayList list = new ArrayList();

    boolean permissionSwitch = individualID < 1 ? false : true;
    boolean applyFilter = false;
    String filter = parameters.getFilter();

    CVDal cvdl = new CVDal(this.dataSource);
    if (filter != null && filter.length() > 0) {
      String str = "CREATE TABLE promotionlistfilter " + filter;
      cvdl.setSqlQuery(str);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      applyFilter = true;
    }
    int numberOfRecords = 0;
    if (applyFilter)
      numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "promotionlistfilter", individualID, 33,
          "promotion", "PromotionID", "Owner", null, permissionSwitch);
    else if (permissionSwitch)
      numberOfRecords = EJBUtil.buildListFilterTable(cvdl, null, individualID, 33, "promotion",
          "PromotionID", "Owner", null, permissionSwitch);
    parameters.setTotalRecords(numberOfRecords);

    String query = this.buildPromotionListQuery(applyFilter, permissionSwitch, parameters);
    cvdl.setSqlQuery(query);
    list = cvdl.executeQueryList(1);
    cvdl.setSqlQueryToNull();

    if (applyFilter) {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE promotionlistfilter");
      cvdl.executeUpdate();
    }
    if (applyFilter || permissionSwitch) {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE listfilter");
      cvdl.executeUpdate();
    }

    cvdl.destroy();
    cvdl = null;
    return new ValueListVO(list, parameters);
  }

  private String buildPromotionListQuery(boolean applyFilter, boolean permissionSwitch,
      ValueListParameters parameters)
  { // FIXME
    String select = "SELECT p.PromotionID, p.Title, p.Description, p.StartDate, p.EndDate, p.Status, 0 "
        + "AS OrderCount ";

    StringBuffer from = new StringBuffer("FROM promotion p ");
    StringBuffer where = new StringBuffer("WHERE 1 = 1 ");
    String orderBy = "ORDER BY "
        + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
    String limit = parameters.getLimitParam();

    // If a filter is applied we need to do an additional join against the
    // temporary entity list filter table.
    if (applyFilter || permissionSwitch) {
      from.append(", listfilter AS lf ");
      where.append("AND p.PromotionID = lf.PromotionID ");
    }
    // Build up the actual query using all the different permissions.
    // Where owner = passed individualId
    StringBuffer query = new StringBuffer();
    query.append(select);
    query.append(from);
    query.append(where);
    query.append(orderBy);
    query.append(limit);
    return query.toString();
  }

  /**
   * Returns a ValueListVO representing a list of Event records, based on the
   * <code>parameters</code> argument which limits results.
   */
  public ValueListVO getEventValueList(int individualID, ValueListParameters parameters)
  {
    ArrayList list = new ArrayList();

    boolean permissionSwitch = individualID < 1 ? false : true;
    boolean applyFilter = false;
    String filter = parameters.getFilter();

    CVDal cvdl = new CVDal(this.dataSource);
    if (filter != null && filter.length() > 0) {
      String str = "CREATE TABLE eventlistfilter " + filter;
      cvdl.setSqlQuery(str);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      applyFilter = true;
    }
    int numberOfRecords = 0;
    if (applyFilter)
      numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "eventlistfilter", individualID, 35,
          "event", "EventID", "Owner", null, permissionSwitch);
    else if (permissionSwitch)
      numberOfRecords = EJBUtil.buildListFilterTable(cvdl, null, individualID, 35, "event",
          "EventID", "Owner", null, permissionSwitch);
    parameters.setTotalRecords(numberOfRecords);

    String query = this.buildEventListQuery(applyFilter, permissionSwitch, parameters);
    cvdl.setSqlQuery(query);
    list = cvdl.executeQueryList(1);
    cvdl.setSqlQueryToNull();

    if (applyFilter) {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE eventlistfilter");
      cvdl.executeUpdate();
    }
    if (applyFilter || permissionSwitch) {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE listfilter");
      cvdl.executeUpdate();
    }

    cvdl.destroy();
    cvdl = null;
    return new ValueListVO(list, parameters);
  }

  private String buildEventListQuery(boolean applyFilter, boolean permissionSwitch,
      ValueListParameters parameters)
  {
    String select = "SELECT e.EventID, e.Title, e.Detail, e.Start, e.End, CONCAT(i.FirstName, ' ', "
        + "i.LastName) AS OwnerName, e.Owner, COUNT(er.EventID) RegisteredAttendees ";

    String joinConditions = "LEFT OUTER JOIN eventregister er ON e.EventID = er.EventID ";

    StringBuffer from = new StringBuffer("FROM event e, individual i ");
    StringBuffer where = new StringBuffer("WHERE e.Owner = i.IndividualID ");
    String groupBy = "GROUP BY e.EventID ";
    String orderBy = "ORDER BY "
        + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
    String limit = parameters.getLimitParam();

    // If a filter is applied we need to do an additional join against the
    // temporary entity list filter table.
    if (applyFilter || permissionSwitch) {
      from.append(", listfilter AS lf ");
      where.append("AND e.EventID = lf.EventID ");
    }
    // Build up the actual query using all the different permissions.
    // Where owner = passed individualId
    StringBuffer query = new StringBuffer();
    query.append(select);
    query.append(from);
    query.append(joinConditions);
    query.append(where);
    query.append(groupBy);
    query.append(orderBy);
    query.append(limit);
    return query.toString();
  }

  /**
   * Returns a ValueListVO representing a list of Event records, based on the
   * <code>parameters</code> argument which limits results.
   * @throws SQLException
   */
  public ValueListVO getEventAttendeeValueList(int individualID, ValueListParameters parameters)
  {
    ArrayList list = new ArrayList();
    CVDal cvdl = new CVDal(this.dataSource);
    try {

      boolean permissionSwitch = individualID < 1 ? false : true;
      boolean applyFilter = false;
      String filter = parameters.getFilter();

      if (filter != null && filter.length() > 0) {
        String str = "CREATE TABLE eventattendeelistfilter " + filter;
        cvdl.setSqlQuery(str);
        cvdl.executeUpdate();
        cvdl.setSqlQueryToNull();
        applyFilter = true;
      }

      int numberOfRecords = 0;

      if (applyFilter) {
        numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "eventattendeelistfilter",
            individualID, 15, "individual", "individualId", "owner", null, permissionSwitch);
      } else if (permissionSwitch) {
        numberOfRecords = EJBUtil.buildListFilterTable(cvdl, null, individualID, 15, "individual",
            "individualId", "owner", null, permissionSwitch);
      }
      parameters.setTotalRecords(numberOfRecords);

      String query = this.buildEventAttendeeListQuery(applyFilter, permissionSwitch, parameters);
      cvdl.setSqlQuery(query);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();

      // obtain a Lock on the tables here to prevent deadlocks
      boolean autoCommitFlag = cvdl.getAutoCommit();
      cvdl.setAutoCommit(false);
      cvdl
          .setSqlQuery("LOCK TABLES eventattendeelist AS il WRITE, methodofcontact AS moc WRITE, mocrelate AS mr WRITE");
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();

      cvdl
          .setSqlQuery("UPDATE eventattendeelist AS il, methodofcontact AS moc, mocrelate AS mr SET il.email=moc.content WHERE il.individualID=mr.contactID AND moc.mocID=mr.mocID AND moc.mocType=1 AND mr.contactType=2 AND mr.isPrimary='YES'");
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();

      // unlock tables
      cvdl.setSqlQuery("UNLOCK TABLES");
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      cvdl.setAutoCommit(autoCommitFlag);

      // Now, Finally, we can just select the individual list and populate value
      // List
      cvdl.setSqlQuery("SELECT * FROM eventattendeelist");
      list = cvdl.executeQueryList(1);
      cvdl.setSqlQueryToNull();

      // drop individuallist table
      cvdl.setSqlQuery("DROP TABLE eventattendeelist");
      cvdl.executeUpdate();

      if (applyFilter) {
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("DROP TABLE eventattendeelistfilter");
        cvdl.executeUpdate();
      }
      if (applyFilter || permissionSwitch) {
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("DROP TABLE listfilter");
        cvdl.executeUpdate();
      }
    } catch (SQLException e) {
      logger.error("[getIndividualValueList] Exception thrown.", e);
      throw new EJBException(e);
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
    return new ValueListVO(list, parameters);
  }

  private String buildEventAttendeeListQuery(boolean applyFilter, boolean permissionSwitch,
      ValueListParameters parameters)
  {
    String select = "SELECT i.IndividualID, CONCAT(i.FirstName, ' ', i.LastName) AS individualname,"
        + " moc.Content AS email, er.accepted ";

    String joinConditions = "LEFT OUTER JOIN individual AS i ON (er.IndividualID = i.IndividualID) "
        + "LEFT OUTER JOIN methodofcontact moc ON (moc.MOCID=0) ";

    StringBuffer from = new StringBuffer("FROM eventregister er ");
    StringBuffer where = new StringBuffer("WHERE EventID=" + parameters.getExtraId() + " ");
    String orderBy = " ORDER BY "
        + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
    String limit = parameters.getLimitParam();

    // If a filter is applied we need to do an additional join against the
    // temporary entity list filter table.
    if (applyFilter || permissionSwitch) {
      from.append(", listfilter AS lf ");
      where.append("AND er.IndividualID = lf.IndividualID");
    }
    // Build up the actual query using all the different permissions.
    // Where owner = passed individualId
    StringBuffer query = new StringBuffer("CREATE TEMPORARY TABLE eventattendeelist ");
    query.append(select);
    query.append(from);
    query.append(joinConditions);
    query.append(where);
    query.append(orderBy);
    query.append(limit);
    return query.toString();
  }

}
