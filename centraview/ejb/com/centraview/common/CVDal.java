/*
 * $RCSfile: CVDal.java,v $    $Revision: 1.4 $  $Date: 2005/10/07 19:18:09 $ - $Author: mcallist $
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

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.centraview.valuelist.ValueListRow;

/**
 * This class wraps the full database interaction.
 * Uses prepared Statements for executing Queries and
 * Datasource to get connection from a  connection pool
 *
 * eg.
 * CVDal dl = new CCVDal();
 * dl.setsql("contact.getentity");
 * dl.setString(1,Iqbal);
 * dl.setInt(2,30);
 * HashMap hm = dl.executeQuery();
 * dl.destroy();
 *
 * <b>NOTE:</b>
 * STATIC SQL Queries are NO longer stored in this file, they should be set in
 *  the static initializer in com.centraview.common.QueryCollection
 * DynamicQueries are still built in this file.
 *
 */
public class CVDal
{
  private DataSource ds;
  private Connection conn;
  private PreparedStatement stmt = null;
  private String sqlString; //the actual SQL string

  private static Logger logger = Logger.getLogger(CVDal.class);

  /**
   * Constructor.
   * Just throws an exception as this should never be used.
   * @deprecated use the CVDal(String dataSource) constructor.
   */
  public CVDal() throws Exception
  {
    logger.error("[ERROR][CVDal.CVDal] Call made to Deprecated CVDal Constructor, use the CVDal(String dataSource) constructor");
    throw new Exception("This CVDal constructor is Deprecated, use the CVDal(String dataSource) constructor");
  }

  public CVDal(String dataSource)
  {
    String dsName = "java:/" + dataSource;
    try {
      ds = (DataSource)CVUtility.getInitialContext().lookup(dsName);
      conn = ds.getConnection();
    } catch (Exception e) {
      logger.error("[CVDal]: Failed while getting datasource connection: "+dsName, e);
    }
  }

  public void setAutoCommit(boolean autoCommitFlag) throws SQLException
  {
    this.conn.setAutoCommit(autoCommitFlag);
  }

  public boolean getAutoCommit() throws SQLException
  {
    return this.conn.getAutoCommit();
  }

  public void commit() throws SQLException
  {
    this.conn.commit();
  }

  public void rollback() throws SQLException
  {
    this.conn.rollback();
  }

 /**
  * Sets the prepared SQL statement, by getting it from the SQL store properties file.
  *
  * @param  String  Valid SQL ID from the SQL Store properties file
  */
  public void setSql(String sqlId)
  {
    try {
      this.sqlString = getSql(sqlId);
      stmt = conn.prepareStatement(this.sqlString);
    } catch (Exception e) {
      System.out.println("[Exception][CVDal] Failed while preparing Statement in setSQL():" + e);
      e.printStackTrace();
    }
  }   // end setSQL()


  /**
   * Sets the prepared SQL statement, by using the String passed to the method.
   * @param  String  A Valid SQL Query.
   */
  public void setSqlQuery(String sql)
  {
    try {
      this.sqlString = sql;
      stmt = conn.prepareStatement(this.sqlString);
    } catch (Exception e) {
      logger.error("[setSqlQuery]: Exception in setSqlQuery", e);
    }   // end try
  }   // end setSqlQuery() method

  public void setSqlQueryToNull()
  {
    try {
      if (stmt != null) {
        stmt.close();
        stmt = null;
      }
    } catch (Exception e) {
      logger.error("[setSqlQueryToNull]: Exception", e);
    }
  }   // end setSqlQueryToNull() method


  /**
   * This Function sets the  sql string  with sortmember,sortType
   */
  public void setDynamicQuery( String sql,String sortType,String sortMember)
  {
    String strFFSortType = "";
    String sortFFMember = sortMember;

    if (sortType.equals("ASC")) {
      strFFSortType = "DESC";
    } else {
      strFFSortType = "ASC";
    }

    if (sortMember.equals("CreatedBy")) {
      sortFFMember="Created By";
    }

    QueryCollection.allSql.put("kb.allkb","select 'KBELEMENT' as Catkb,kb.kbid ID,kb.title Name," +
                              "kb.created DateCreated,kb.updated DateUpdated,kb.category " +
                              "from knowledgebase kb where category=? and kb.owner=? and status='DRAFT' union " +
                              "select 'CATEGORY' as Catkb,cat.catid ID,cat.title Name," +
                              "cat.created DateCreated,cat.Modified DateUpdated,cat.parent "+
                              "from individual indv,category cat where cat.parent=? " +
                              "union select 'KBELEMENT' as Catkb,kb.kbid ID,kb.title Name," +
                              "kb.created DateCreated,kb.updated DateUpdated,kb.category " +
                              "from publicrecords pub left join knowledgebase kb on " +
                              "pub.recordid=kb.kbid where pub.moduleid=41 and category=? and status='PUBLISH' " +
                              "union select 'KBELEMENT' as Catkb,kb.kbid ID,kb.title Name," +
                              "kb.created DateCreated,kb.updated DateUpdated,kb.category " +
                              "from recordauthorisation auth left join knowledgebase kb on " +
                              "auth.recordid=kb.kbid where auth.individualid=? AND auth.recordtypeid=41 AND auth.privilegelevel<40 " +
                              "and category=?  and status='PUBLISH' order by '"+ sortMember +"' "+sortType);


    QueryCollection.allSql.put("file.allfilefolderssystem",   "select 'FOLDER' as FileFolder,indv.individualid as individualid,fld.folderid ID,fld.name Name,fld.description Description,fld.createdOn Created,fld.ModifiedOn Updated,concat(indv.FirstName,'',indv.LastName) 'Created BY', fld.parent as Parent,fld.name NameTitle from individual indv,cvfolder fld,folderaccess flda where indv.individualid=fld.owner and fld.parent=? and fld.IsSystem='FALSE'  and flda.folderid=fld.folderid union select 'FILE' as FileFolder,indv.individualid as individualid,fil.fileid ID,fil.name Name,fil.description Description,fil.created Created,fil.updated Updated,concat(indv.FirstName,'',indv.LastName) 'Created BY', fld.folderid AS Parent, fil.Title AS Title from cvfile fil ,individual indv,cvfilefolder fld, fileaccess fila where indv.individualid=fil.owner and fld.fileid=fil.fileid and fld.folderid=? and fila.fileid=fil.fileid order by FileFolder "+strFFSortType+",'"+sortFFMember +"' "+sortType);
    QueryCollection.allSql.put("file.myfilefolderssystem",    "select 'FOLDER' as FileFolder,indv.individualid as individualid,fld.folderid ID,fld.name Name,fld.description Description,fld.createdOn Created,fld.ModifiedOn Updated,concat(indv.FirstName,'',indv.LastName) 'Created BY', fld.parent AS Parent,fld.name NameTitle from individual indv,cvfolder fld where indv.individualid=fld.owner and fld.parent=? and IsSystem='FALSE' and fld.owner=?                                       union select 'FILE' as FileFolder,indv.individualid as individualid,fil.fileid ID,fil.name Name,fil.description Description,fil.created Created,fil.updated Updated,concat(indv.FirstName,'',indv.LastName) 'Created BY', fld.folderid AS Parent, fil.Title AS Title from cvfile fil ,individual indv,cvfilefolder fld  where indv.individualid=fil.owner and fld.fileid=fil.fileid and fld.folderid=? and fil.owner=? order by FileFolder "+strFFSortType+",'"+sortFFMember +"' "+sortType);
    QueryCollection.allSql.put("file.allfilefoldersnonsystem","select 'FOLDER' as FileFolder,indv.individualid as individualid,fld.folderid ID,fld.name Name,fld.description Description,fld.createdOn Created,fld.ModifiedOn Updated,concat(indv.FirstName,'',indv.LastName) 'Created BY', fld.parent AS Parent,fld.name NameTitle from individual indv,cvfolder fld where indv.individualid=fld.owner  and IsSystem='FALSE' and visibility='PUBLIC' and fld.parent=?                              union select 'FILE' as FileFolder,indv.individualid as individualid,fil.fileid ID,fil.name Name,fil.description Description,fil.created Created,fil.updated Updated,concat(indv.FirstName,'',indv.LastName) 'Created BY', fld.folderid AS Parent, fil.Title AS Title from cvfile fil ,individual indv,cvfilefolder fld where indv.individualid=fil.owner and fld.fileid=fil.fileid and fld.folderid=? and visibility='PUBLIC' order by FileFolder "+strFFSortType+",'"+sortFFMember +"' "+sortType);
    QueryCollection.allSql.put("file.myfilefoldersnonsystem", "select 'FOLDER' as FileFolder,indv.individualid as individualid,fld.folderid ID,fld.name Name,fld.description Description,fld.createdOn Created,fld.ModifiedOn Updated,concat(indv.FirstName,'',indv.LastName) 'Created BY', fld.parent AS Parent,fld.name NameTitle from individual indv,cvfolder fld where indv.individualid=fld.owner and IsSystem='FALSE' and fld.parent=? and fld.owner=?                                       union select 'FILE' as FileFolder,indv.individualid as individualid,fil.fileid ID,fil.name Name,fil.description Description,fil.created Created,fil.updated Updated,concat(indv.FirstName,'',indv.LastName) 'Created BY', fld.folderid AS Parent, fil.Title AS Title from cvfile fil ,individual indv,cvfilefolder fld  where indv.individualid=fil.owner and fld.fileid=fil.fileid and fld.folderid=? and fil.owner=? order by FileFolder "+strFFSortType+",'"+sortFFMember +"' "+sortType);

    String itemQuery = " select item.itemid,item.sku,item.title as Name ,itemtype.title as type ,item.listprice as Price ,count(inventory.qty) as OnHand ,taxclass.title as taxclass,item.cost ,"+
                       " ven.entityid as vendorid,ven.name as vendor,man.entityid as manid, man.name as manufacturer  from  item left outer join itemtype on item.type = itemtype.itemtypeid "+
                       " left outer join inventory on item.itemid = inventory.item left outer join taxclass on item.taxclass = taxclass.taxclassid "+
                       " left outer join entity man on item.manufacturerid = man.entityid   left outer join entity ven on item.vendorid = ven.entityid "+
                       " where item.deletestatus != 'Deleted' Group by item.itemid,item.sku,item.title,itemtype.title,item.listprice, taxclass.title,item.cost order by '"+ sortMember +"' "+sortType;

    QueryCollection.allSql.put("account.getitemlist",itemQuery);

    //order

    QueryCollection.allSql.put("account.getorderlist","  select cvorder.orderid as OrderNO ,cvorder.entityid,entity.name as Entity,cvorder.created as date ,cvorder.total,cvorder.accountmgr,concat(indv1.firstname,' ',indv1.lastname) as salesrep ,accountingstatus.title as status,cvorder.discount,cvorder.creator,concat(indv2.firstname,' ',indv2.lastname) as creator,cvorder.ponumber from cvorder left outer join entity on  cvorder.entityid = entity.entityid       left outer join individual indv1 on  cvorder.accountmgr = indv1.individualid  left outer join individual indv2 on  cvorder.creator = indv2.individualid  left outer join accountingstatus on  cvorder.status = accountingstatus.statusid  where orderstatus !=  'Deleted' order by '"+ sortMember +"' "+sortType);

    //Inventory

    QueryCollection.allSql.put("account.getinventorylist", "select ven.entityid as EntityID,inventory.inventoryid as InventoryID,item.title as ItemName,inventory.title as Identifier  ,man.name as Manufacturer,ven.name as Vendor from inventory  left outer join item on inventory.item = item.itemid left outer join entity man on item.manufacturerid = man.entityid left outer join entity ven on item.vendorid  = ven.entityid order by '" + sortMember +"' "+sortType);

    //Location
    QueryCollection.allSql.put("account.getlocationlist", "select locationid, title, parent from location order by '" + sortMember +"' "+sortType);

    //GLAccount
    QueryCollection.allSql.put("account.getglaccountlist", "SELECT gla.glaccountsid GLAccountsID,gla.title Name ,gla.glaccounttype Type ,gla.Balance Balance ,gla1.title ParentAccount FROM glaccount gla left outer join glaccount gla1 on gla.parent = gla1.glaccountsid order by '"+ sortMember +"' "+sortType);

    QueryCollection.allSql.put("customview.getcustomviewlist", "select lv.viewid viewid,lv.viewname viewname,m2.name module,lv.listtype record from listviews lv,defaultviews df,listtypes lt,module m1,module m2 where df.viewid!=lv.viewid and lv.listtype=df.listtype and lt.typename=lv.listtype and lt.moduleid=m1.moduleid and m1.parentid=m2.moduleid and m2.name=? "+
    " union select lv.viewid viewid,lv.viewname viewname,m2.name module,lv.listtype record from listviews lv,defaultviews df,listtypes lt,module m2 where df.viewid!=lv.viewid and lv.listtype=df.listtype and lt.typename=lv.listtype and lt.moduleid=m2.moduleid and m2.name=? order by '"+ sortMember +"' "+sortType);


    try {
      if (stmt != null) {
        stmt = null;
      }
      this.sqlString = getSql(sql);
      stmt = conn.prepareStatement(this.sqlString);
    } catch (Exception e) {
      logger.error("[setDynamicQuery] Exception thrown.", e);
    }
  }

  /**
   * This Overloaded Function sets the sql string with sortmember,beginindex and
   * endindex
   * KDM: I came across the fact that this was limiting list for no reason that
   * I could decipher, so I have removed that  "feature" now, it does the same
   * thing as the other setDynamicQuery method, but it takes Limit values that are
   * ignored.  The real long term fix is to wack this method and fix all the places
   * that call it.
   */
  public void setDynamicQuery(String sql, String sortType, String sortMember, int beginIndex, int endIndex)
  {
    String sortFFMember = sortMember;

    if (sortMember.equals("CreatedBy")) {
      sortFFMember = "Created By";
    }

    QueryCollection.allSql.put("note.allnotes", "select nt.noteid as noteid,nt.title as title,nt.Detail as Detail,nt.datecreated as date,concat(indv.FirstName,' ',indv.LastName) 'Created By',nt.priority as priority,indv.individualid as individualid from note nt left outer join individual indv on indv.individualid=nt.owner,noteaccess nta where nta.NoteID = nt.NoteID order by '" + sortFFMember + "' " + sortType);
    QueryCollection.allSql.put("note.mynotes", "select nt.noteid as noteid,nt.title as title,nt.Detail as Detail,nt.datecreated as date ,concat(indv.FirstName,' ',indv.LastName) 'Created By',nt.priority as priority,indv.individualid as individualid from note nt left outer join  individual indv on indv.individualid=nt.owner where nt.owner=? order by '" + sortFFMember + "' " + sortType);
    QueryCollection.allSql.put("note.entitynotes", "select nt.noteid as noteid,nt.title as title,nt.Detail as Detail,nt.datecreated as date ,concat(indv.FirstName,' ',indv.LastName) 'Created By',nt.priority as priority,indv.individualid as individualid from note nt left outer join  individual indv on indv.individualid=nt.owner where nt.RelateEntity=? order by '" + sortFFMember + "' " + sortType);
    QueryCollection.allSql.put("faq.allfaq", "select fq.faqid as faqid,fq.title as title,fq.created as created," + "fq.updated as updated from faq fq where fq.owner=? and fq.status='DRAFT' "
        + "union select fq.faqid as faqid,fq.title as title,fq.created as created," + "fq.updated as updated from publicrecords pub "
        + "left join faq fq on pub.recordid=fq.faqid " + "where pub.moduleid=40  and fq.status='PUBLISH' " + "union select fq.faqid as faqid,fq.title as title ,fq.created as created,"
        + "fq.updated as updated from recordauthorisation auth " + "left join faq fq on auth.recordid=fq.faqid "
        + "where auth.individualid=? and auth.recordtypeid=40 and auth.privilegelevel<40  and fq.status='PUBLISH'  " + "order by '" + sortMember + "' " + sortType);

    QueryCollection.allSql.put("user.getsavedsearchlist", "select s.searchid,s.searchname,t.name recordtype,m.name module from search s,cvtable t,module m where s.tableid=t.tableid and m.moduleid=s.moduleid and m.name=? order by '" + sortMember + "' " + sortType);
    QueryCollection.allSql.put("securityprofile.getsecurityprofilelist", "select sp.profileid ProfileID,sp.profilename ProfileName,count(usp.individualid) NoOfUsers from securityprofile sp left join usersecurityprofile usp on sp.profileid = usp.profileid group by sp.profileid order by '" + sortFFMember + "' " + sortType + " LIMIT " + (beginIndex - 1) + ", " + (endIndex + 1));
    QueryCollection.allSql.put("history.gethistorylist", "select historyid, date , recordid , recordtype , recordName, user, action, type, reference, notes,referenceactivityid from historylist order by '" + sortFFMember + "' " + sortType);
    QueryCollection.allSql.put("user.getuserlist", "SELECT a.UserID, a.IndividualID, a.Name UserName, concat(b.FirstName , ' ', b.LastName) Name, c.Name Entity, c.EntityID, a.userstatus Enabled, a.usertype, 'foo@example.com' Email from user a, individual b, entity c where a.IndividualID = b.IndividualID and b.Entity = c.EntityID order by '" + sortMember + "' " + sortType);
    QueryCollection.allSql.put("customerview.faq.allfaq", "Select fq.faqid as faqid,fq.title as title ,fq.created as created ,fq.updated as updated from faq fq where fq.status='PUBLISH' order by '" + sortMember + "' " + sortType);
    
    try {
      if (stmt != null) {
        stmt.close();
        stmt = null;
      }
      this.sqlString = getSql(sql);
      stmt = conn.prepareStatement(this.sqlString);
    } catch (Exception e) {
      System.out.println("[Exception][CVDal.setDynamicQuery] Exception Thrown: " + e);
      e.printStackTrace();
    }
  }


  /**
   * Sets a String type Parameter value in prepared statement.
   * delegates to PreparedStatement's setString
   * @param  int  Parameter Sequence
   * @param  String  Value to be set
   */
  public void setString(int sequence, String data)
  {
    try {
      stmt.setString(sequence,data);
    } catch (Exception e) {
      System.out.println("[Exception][CVDal.setString] Exception Thrown: "+e);
      e.printStackTrace();
    }
  }


  /**
   * Sets a int type Parameter value in prepared statement.
   * delegates to PreparedStatement's setInt
   * @param  int  Parameter Sequence
   * @param  int  Value to be set
   */
  public void setInt(int sequence, int data)
  {
    try {
      stmt.setInt(sequence,data);
    } catch(Exception e) {
      System.out.println("[Exception][CVDal.setInt] Exception Thrown: "+e);
      e.printStackTrace();
    }
  }

  /**
   * Sets a short type Parameter value in prepared statement.
   * delegates to PreparedStatement's setShort
   * @param  int Parameter Sequence
   * @param  short Value to be set
   */
  public void setShort(int sequence, short data)
  {
    try {
      stmt.setShort(sequence,data);
    } catch(Exception e) {
      System.out.println("[Exception][CVDal.setInt] Exception Thrown: "+e);
      e.printStackTrace();
    }
  }

  /**
   * Sets a Parameter to Null value in prepared statement.
   * delegates to PreparedStatement's setNull
   * @param  int Parameter Sequence
   * @param  int type from java.sql.Types
   */
  public void setNull(int sequence, int sqlType)
  {
    try {
      stmt.setNull(sequence,sqlType);
    } catch(Exception e) {
      System.out.println("[Exception][CVDal.setInt] Exception Thrown: "+e);
      e.printStackTrace();
    }
  }

  /**
   * Sets a Parameter to the byte value in prepared statement.
   * delegates to PreparedStatement's setByte
   * @param  int Parameter Sequence
   * @param  byte, the value to be set.
   */
  public void setByte(int sequence, byte value)
  {
    try {
      stmt.setByte(sequence,value);
    } catch(Exception e) {
      System.out.println("[Exception][CVDal.setInt] Exception Thrown: "+e);
      e.printStackTrace();
    }
  }

  /**
   * Sets a Parameter to the Object value in prepared statement.
   * delegates to PreparedStatement's setObject
   * @param  int Parameter Sequence
   * @param  Object, the value to be set.
   */
  public void setObject(int sequence, Object value)
  {
    try {
      stmt.setObject(sequence,value);
    } catch(Exception e) {
      System.out.println("[Exception][CVDal.setInt] Exception Thrown: "+e);
      e.printStackTrace();
    }
  }

  /**
   * Sets a Date type Parameter value in prepared statement.
   * delegates to PreparedStatement's setString
   * @param  int  Parameter Sequence
   * @param  java.sql.Date  Value to be set
   */
  public void setDate(int sequence, java.sql.Date data)
  {
    try {
      stmt.setDate(sequence, data);
    } catch(Exception e) {
      logger.error("[setDate]: Exception", e);
    }
  }

  public void setRealDate(int sequence, java.sql.Date data)
  {
    try {
      stmt.setDate(sequence, data);
    } catch (Exception e) {
      System.out.println("[Exception][CVDal.setDate] Exception Thrown: "+e);
      e.printStackTrace();
    }
  }


  /**
   * Sets a Time type Parameter value in prepared statement.
   * delegates to PreparedStatement's setTime
   * @param  int  Parameter Sequence
   * @param  java.sql.Time  Value to be set
   */
  public void setTime(int sequence, java.sql.Time data)
  {
    try {
      stmt.setTime(sequence,data);
    } catch(Exception e) {
      System.out.println("[Exception][CVDal.setTime] Exception Thrown: "+e);
      e.printStackTrace();
    }
  }

  /**
   * Sets a boolean type Parameter value in prepared statement.
   * delegates to PreparedStatement's setBoolean
   * @param  int  Parameter Sequence
   * @param  boolean  Value to be set
   */
  public void setBoolean(int sequence, boolean data)
  {
    try {
      stmt.setBoolean(sequence,data);
    } catch (Exception e) {
      System.out.println("[Exception][CVDal.setBoolean] Exception Thrown: "+e);
      e.printStackTrace();
    }
  }

  /**
   * Sets a java.sql.Timestamp type Parameter value in prepared statement.
   * delegates to PreparedStatement's setTimestamp
   * @param  int  Parameter Sequence
   * @param  Timestamp Value to be set
   */
  public void setTimestamp(int sequence, java.sql.Timestamp data)
  {
    try {
      stmt.setTimestamp(sequence, data);
    } catch(Exception e) {
      logger.error("[setTimestamp]: Exception", e);
    }
  }

  /**
   * Sets a java.sql.Timestamp type Parameter value in prepared statement.
   * delegates to PreparedStatement's setTimestamp. This does not build
   * a string (like setTimestamp, but rather sets a REAL java.sql.Timestamp object
   * in the statement).
   * @param  sequence The sequence in the statement to be set.
   * @param  data The timestamp value to be set in the statement.
   * @see java.sql.Timestamp
   * @see CVDal#setTimestamp(int,java.sql.Timestamp)
   */
  public void setRealTimestamp(int sequence, java.sql.Timestamp data)
  {
    try {
      if (data != null) {
        stmt.setTimestamp(sequence, data);
      } else {
        stmt.setString(sequence, null);
      }
    } catch (Exception exception) {
      System.out.println("[Exception] Exception in CVDal.setRealTimestamp: " + exception.toString());
      exception.printStackTrace();
    }
  }

  /**
   * Executes a Batch Query and returns the int[]
   * @param ArrayList Collection of SQL Query
   */
  public int[] batchProcess(ArrayList querys) throws Exception
  {
    int batchResult[] = null;
    try {
      Statement batchStmt = conn.createStatement();
      for (int i = 0; i < querys.size(); i++) {
        String query = (String) querys.get(i);
        if (query != null && ! query.equals("")) {
          batchStmt.addBatch(query);
        }
      }
      batchResult = batchStmt.executeBatch();
    } catch (Exception e) {
      System.out.println("[Exception][CVDal.batchProcess] Exception Thrown: "+e);
      e.printStackTrace();
    }
    return batchResult;
  }

  /**
   * Inserts a list of recipients into the emailrecipient table
   * for a given email message. You must supply an array of Address
   * objects, the message these recipients are attached to, the
   * type of recipients they are (To, Cc, Bcc).
   * @param recipients ArrayList objects, each being a
   *        recipient of the given email message.
   * @param messageID The ID of the message which these recipients
   *        are to be associated with in the database.
   * @param type String representation of the type of recipient
   *        list. Can be one of "TO", "CC", or "BCC".
   * @return int []
   */
  public int[] addRecipients(ArrayList recipients, int messageID, String type) throws Exception
  {
    int batchResult[] = null;
    try {
      PreparedStatement batchStmt = conn.prepareStatement("INSERT INTO emailrecipient (MessageID, Address, RecipientType, RecipientIsGroup) VALUES (?, ? ,?, 'NO')");
      
      for (int i = 0; i < recipients.size(); i++) {
        batchStmt.setInt(1, messageID);
        batchStmt.setString(2, (String)recipients.get(i));
        batchStmt.setString(3, type);
        batchStmt.addBatch();
      }
      
      batchResult = batchStmt.executeBatch();
    } catch (Exception e) {
      System.out.println("[Exception][CVDal.batchProcess] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return batchResult;
  }

  /**
   * Executes a Query (Select), and returns the data as a Collection of HashMaps
   * HashMap contains field names as key and field data as value
   * delegates to PreparedStatement's executeQuery
   */
  public Collection executeQuery()
  {
    ResultSet resultSet = null;
    try {
      // Maybe convert to an ArrayList for speed in the future.
      Vector resultVector = new Vector();
      resultSet = stmt.executeQuery();
      ResultSetMetaData metaData = resultSet.getMetaData();
      int columnCount = metaData.getColumnCount();
      while (resultSet.next()) {
        // convert recordset to hashmap
        HashMap hm = new HashMap();
        for (int i = 1; i <= columnCount; i++) {
          hm.put(metaData.getColumnName(i), resultSet.getObject(i));
        }
        resultVector.add(hm);
      }
      return resultVector;
    } catch (Exception e) {
      logger.error("[executeQuery][Failed Query]: " + this.sqlString);
      logger.error("[executeQuery] Exception thrown.", e);
    } finally {
      // clean up the resultset resources, it's your heap too!
      if (resultSet != null) {
        try { resultSet.close(); } catch (SQLException e) { /* Not much we can do at this point. */ }
      }
    }
    return null;
  }
  
  /**
   * Executes a Query (Select), and returns the data as an ArrayList of ValueListRows
   * This was added during the List rewrite overhaul.
   * delegates to PreparedStatement's executeQuery.
   * @param int idColumn, the column from the select statement that will contain
   * the row's primaryKey.
   */
  public ArrayList executeQueryList(int idColumn)
  {
    ResultSet resultSet = null;
    ArrayList results = new ArrayList();
    try {
      resultSet = this.stmt.executeQuery();
      ResultSetMetaData metaData = resultSet.getMetaData();

      int columnCount = metaData.getColumnCount();
      while (resultSet.next()) {
        // convert recordset to hashmap
        int rowId = resultSet.getInt(idColumn);
        ArrayList rowList = new ArrayList();
        for (int i = 1; i <= columnCount; i++) {
          Object valueString = resultSet.getObject(i);
          rowList.add(valueString);
        }
        results.add(new ValueListRow(rowId, rowList));
      }
    } catch(Exception e) {
      logger.error("[executeQueryList] The Query: "+this.sqlString);
      logger.error("[executeQueryList] Exception thrown.", e);
    } finally { // clean up the resultset resources, it's your heap too!
      if (resultSet != null) {
        try { resultSet.close(); } catch (SQLException e) { }
      }
    }
    return results;
  }

  /**
   * Executes a Query (Select), and returns the
   * java.sql.ResultSet delegates to PreparedStatement's executeQuery
   * Make sure to clean up your own ResultSets when you are done.
   * @author Kevin McAllister <kevin@centraview.com>
   * @return java.sql.ResultSet or null if a SQLexception is thrown by executeQuery
   */
  public ResultSet executeQueryNonParsed()
  {
    try {
      return stmt.executeQuery();
    } catch(java.sql.SQLException e) {
      logger.error("[executeQueryNonParsed][Failed Query]: "+this.sqlString);
      logger.error("[executeQueryNonParsed] Exception thrown.", e);
    }
    return null;
  }

  /**
   * Executes the SQL statement in this PreparedStatement object,
   * which must be an SQL INSERT, UPDATE or DELETE statement; or
   * an SQL statement that returns nothing, such as a DDL statement.
   * @return int: Either (a) the row count for INSERT, UPDATE, or
   * DELETE statements or (b) 0 for SQL statements that return nothing
   */
  public int executeUpdate()
  {
    int rowsAffected = 0;
    try {
      rowsAffected = stmt.executeUpdate();
    } catch (Exception e) {
      logger.error("[executeUpdate][Failed Query]: "+this.sqlString);
      logger.error("[executeUpdate] Exception thrown.", e);
    }
    return (rowsAffected);
  } // end executeUpdate() method


  /**
   * Returns the AtogeneratedID's value for the recent insert.
   * Delegates to PreparedStatement's getAutoGeneratedKey
   */
  public int getAutoGeneratedKey()
  {
    int i = 0;
    ResultSet resultSet = null;
    try {
      resultSet = stmt.getGeneratedKeys();
      resultSet.first();
      i = resultSet.getInt(1);
    } catch (Exception e) {
      System.out.println("[Exception][CVDal.getAutoGeneratedKey] Exception Thrown: "+e);
      e.printStackTrace();
    } finally {
      // clean up the resultset resources, it's your heap too!
      if (resultSet != null) {
        try {
          resultSet.close();
        } catch (SQLException e) {
          // Not much we can do at this point.
        }
      }
    }
    return i;
  }


  /**
   * Clears the value of all the parametrs set on the prepared statement.
   * This should be used before firing more statemets on a single statement
   * Delegates to PreparedStatement's clearParameters
   */
  public void clearParameters()
  {
    try {
      if (stmt != null) {
        stmt.clearParameters();
      }
    } catch (Exception e) {
      System.out.println("[Exception][CVDal.clearParameters] Exception Thrown: "+e);
      e.printStackTrace();
    }
  }

  /**
   * Closes Connection and destroys the PreparedStatement.
   */
  public void destroy()
  {
    try {
      if (stmt != null) {
        stmt.close();
        stmt = null;
      }
    } catch (SQLException e) {
      // Nothing we can do.
    }
    
    try {
      if (conn != null) {
        conn.close();
        conn = null;
      }
    } catch(SQLException e) {
      // Nothing we can do.
    }
    ds = null;
  }

  /**
   * Returns the SQL attached with the sqlId
   * Currently picking from placeholder HashMap.
   * Should actually pick up from Properties File
   */
  private String getSql(String sqlId)
  {
    return (String)QueryCollection.allSql.get(sqlId);
  }

  public void setFloat(int sequence, float data)
  {
    try {
      stmt.setFloat(sequence,data);
    } catch(Exception e) {
      System.out.println("[Exception][CVDal.setFloat] Exception Thrown: " + e);
      e.printStackTrace();
    }
  }

  public void setDouble(int sequence, double data)
  {
    try {
      stmt.setDouble(sequence,data);
    } catch (Exception e) {
      System.out.println("[Exception][CVDal.setDouble] Exception Thrown: "+e);
      e.printStackTrace();
    }
  }

  public void writeToFile(String filePath, String tabName)
  {
    String LINE_SEPARATOR = "\n";

    FileWriter out = null;
    try {
      File outputFile = new File(filePath);

      if (outputFile.exists()) {
        outputFile.delete();
      }

      out = new FileWriter(outputFile);

      ResultSet rs = stmt.executeQuery();
      ResultSetMetaData md = rs.getMetaData();
      int cc = md.getColumnCount();

      String columnNames = "";
      String columnValues = "";

      for (int i = 1; i <= cc; i++) {
        columnNames = columnNames + "\t" + md.getColumnName(i);
      }

      columnNames = columnNames+LINE_SEPARATOR;
      while (rs.next()) {
        for (int i = 1; i <= cc; i++) {
          columnValues = columnValues + "\t" + rs.getObject(i);
        }

        columnValues = columnValues + LINE_SEPARATOR;
      }
      out.write(columnNames + columnValues);
      out.close();
    } catch(Exception e) {
      System.out.println("[Exception][CVDal.writeToFile] Exception Thrown: "+e);
      e.printStackTrace();
    }
  }

  /**
   * Returns a HashMap with the MetaData for the table
   * passed into the method. It returns an empty HashMap
   * if something goes wrong.
   * @param tableName The name of the table to get the metadata from.
   * @return A HashMap with the metadata for the table
   * passed into the method. It returns an empty HashMap
   * if something goes wrong.
   */
  public HashMap getTableMetaData(String tableName)
  {
    HashMap hm = new HashMap();
    ResultSet resultSet = null;
    try {
      resultSet = stmt.executeQuery();
      ResultSetMetaData md = resultSet.getMetaData();
      int cc = md.getColumnCount();

      for (int i = 0; i < cc; i++) {
        int ii = i + 1;
        hm.put((md.getColumnName(ii)).toLowerCase(), new Integer(md.getColumnType(ii)));
      }
    } catch (Exception e) {
      System.out.println("[Exception][CVDal.getTableMetaData] Exception Thrown: " + e);
      e.printStackTrace();
    } finally {
      if (resultSet != null) {
        try {
          resultSet.close();
        } catch (SQLException e) {
          // Not much we can do at this point.
        }
      }
    }
    return hm;
  }

  /**
   * convenience Method for use in debugging.  The 
   * currently set query is written to the debug logger.
   */
  public void logQuery() {
    logger.debug("Loaded Query: "+this.sqlString);
  }
}
