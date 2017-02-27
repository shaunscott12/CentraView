/*
 * $RCSfile: CvAtticEJB.java,v $    $Revision: 1.2 $  $Date: 2005/09/01 15:31:06 $ - $Author: mcallist $
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

package com.centraview.cvattic;

import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.centraview.common.CVDal;
import com.centraview.common.Constants;
import com.centraview.common.DDNameValue;

/**
 *
 * @author CentraView, LLC.
 */
public class CvAtticEJB implements SessionBean
{
  protected SessionContext ctx;
  private String dataSource = "MySqlDS";

  public void ejbCreate()
  {
  }

  public void ejbRemove()
  {
  }

  public void setSessionContext(SessionContext ctx)
  {
    this.ctx = ctx;
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  public void dumpData(int userID, int transactionID, String recordType, HashMap primaryMembers)
  {
    try
    {
      insertAtticData(userID, transactionID, recordType, primaryMembers);
    }
    catch (Exception e)
    {
      System.out.println("[Exception][CvAtticEJB.dumpData] Exception Thrown: " + e);
      //e.printStackTrace();
    }
  }

  public int getAtticTransactionID(int userID, String dumpType, HashMap hm)
  {
    int transactionID = 0;
    CVDal cvdl = new CVDal(dataSource);

    String title = (String) hm.get("title");
    int owner = ((Integer) hm.get("owner")).intValue();
    int module = ((Integer) hm.get("module")).intValue();
    int recordtype = ((Integer) hm.get("recordtype")).intValue();

    try
    {
      cvdl.setSql("attic.insertattic");
      cvdl.setInt(1, userID);
      cvdl.setString(2, dumpType);
      cvdl.setString(3, title);
      cvdl.setInt(4, owner);
      cvdl.setInt(5, module);
      cvdl.setInt(6, recordtype);
      cvdl.executeUpdate();

      transactionID = cvdl.getAutoGeneratedKey();
    }
    catch (Exception e)
    {
      System.out.println("[Exception][CvAtticEJB.getAtticTransactionID] Exception Thrown: " + e);
      //e.printStackTrace();
    }
    finally
    {
      cvdl.destroy();
    }
    return transactionID;
  }

  private void insertAtticData(int userID, int transactionID,
    String recordType, HashMap primaryMembers)
  {
    CVDal cvdl = new CVDal(dataSource);
    try
    {
      //System.out.println("[DEBUG] [CvAtticEJB]: userID: " + userID);
      //System.out.println("[DEBUG] [CvAtticEJB]: transactionID: " + transactionID);
      //System.out.println("[DEBUG] [CvAtticEJB]: recordType: " + recordType);
      //System.out.println("[DEBUG] [CvAtticEJB]: primaryMembers: " + primaryMembers);
      cvdl.setSql("attic.selectrecordtypeid");
      cvdl.setString(1, recordType);

      Collection colTableID = cvdl.executeQuery();
      Iterator iterTableID = colTableID.iterator();

      int tableID = 0;

      while (iterTableID.hasNext())
      {
        HashMap hmTableID = (HashMap) iterTableID.next();
        tableID = ((Long) hmTableID.get("tableid")).intValue();
        //System.out.println("[DEBUG] [CvAtticEJB]: tableID: " + tableID);
      }

      if (tableID > 0)
      {
        Iterator iterPrimaryMembers = primaryMembers.keySet().iterator();

        String strWhereClause = "";
        int counter = 0;

        while (iterPrimaryMembers.hasNext())
        {
          String strPrimaryMember = (String) iterPrimaryMembers.next();
          String strPrimaryValue = (String) primaryMembers.get(strPrimaryMember);
          
          //System.out.println("[DEBUG] [CvAtticEJB]: strPrimaryMember: " + strPrimaryMember);
          //System.out.println("[DEBUG] [CvAtticEJB]: strPrimaryValue: " + strPrimaryValue);

          if (counter > 0)
          {
            strWhereClause = strWhereClause + Constants.WHERE_CLAUSE_SEPARATOR;
          }

          strWhereClause = strWhereClause + strPrimaryMember
            + Constants.EH_KEYVALUE_DELEMETER + strPrimaryValue;
          counter++;
        }
        cvdl.setSqlQueryToNull();
        cvdl.setSql("attic.selectfields");
        cvdl.setInt(1, tableID);

        Collection colFields = cvdl.executeQuery();

        Iterator iterFields = colFields.iterator();

        Vector vecDDName = new Vector();
        String selectField = "";

        counter = 0;

        while (iterFields.hasNext())
        {
          if (counter > 0)
          {
            selectField = selectField + Constants.FIELD_SEPARATOR;
          }

          HashMap hmFields = (HashMap) iterFields.next();

          int fieldID = ((Long) hmFields.get("fieldid")).intValue();
          String fieldName = (String) hmFields.get("name");
          
          //System.out.println("[DEBUG] [CvAtticEJB]: fieldID: " + fieldID);
          //System.out.println("[DEBUG] [CvAtticEJB]: fieldName: " + fieldName);

          DDNameValue ddname = new DDNameValue(fieldID, fieldName);
          vecDDName.addElement(ddname);

          selectField = selectField + fieldName;

          counter++;
        }

        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("Select " + selectField + " from " + recordType + " where " + strWhereClause);

        Collection colFieldValues = cvdl.executeQuery();
        Iterator iterFieldValues = colFieldValues.iterator();

        while (iterFieldValues.hasNext())
        {
          HashMap hmFieldValue = (HashMap) iterFieldValues.next();

          for (int i = 0; i < vecDDName.size(); i++)
          {
            DDNameValue ddname = (DDNameValue) vecDDName.elementAt(i);

            String fieldValue = "";

            if (hmFieldValue.get(ddname.getName()) != null)
            {
              fieldValue = (hmFieldValue.get(ddname.getName())).toString();
            }
            else
            {
              fieldValue = "";
            }

            //System.out.println("[DEBUG] [CvAtticEJB]: transactionID: " + transactionID);
            //System.out.println("[DEBUG] [CvAtticEJB]: tableID: " + tableID);
            //System.out.println("[DEBUG] [CvAtticEJB]: ddname.getId(): " + ddname.getId());
            //System.out.println("[DEBUG] [CvAtticEJB]: fieldValue: " + fieldValue);
            cvdl.setSqlQueryToNull();
            cvdl.setSql("attic.insertatticdata");
            cvdl.setInt(1, transactionID);
            cvdl.setInt(2, tableID);
            cvdl.setInt(3, ddname.getId());
            cvdl.setString(4, fieldValue);

            cvdl.executeUpdate();
          }
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("[Exception][CvAtticEJB.insertAtticData] Exception Thrown: " + e);
      //e.printStackTrace();
    }
    finally
    {
      cvdl.destroy();
      cvdl = null;
    }
  }

  public void delete(int userID, int ID, String type)
  {
    CVDal cvdl = new CVDal(dataSource);
    try {
      if (type.equals(Constants.CV_GARBAGE)) {
        cvdl.setSql("attic.deletegarbage");
        cvdl.setInt(1, ID);
        cvdl.setString(2, type);
      } else if (type.equals(Constants.CV_ATTIC)) {
        cvdl.setSql("attic.updateattic");
        cvdl.setString(1, Constants.CV_GARBAGE);
        cvdl.setInt(2, ID);
        cvdl.setString(3, Constants.CV_ATTIC);
      }
      cvdl.executeUpdate();
    } catch (Exception e) {
      System.out.println("[Exception] CvAtticEJB.delete: " + e.toString());
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
  }

  public void moveToAttic(int userID, int gbID)
  {
    CVDal cvdl = new CVDal(dataSource);
    
    try {
      cvdl.setSql("attic.updateattic");
      cvdl.setString(1, Constants.CV_ATTIC);
      cvdl.setInt(2, gbID);
      cvdl.setString(3, Constants.CV_GARBAGE);
      cvdl.executeUpdate();
    } catch (Exception e) {
      System.out.println("[Exception] CvAtticEJB.moveToAttic: " + e.toString());
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
  }

  public void restore(int userID, int ID, String type)
  {
    //System.out.println("[DEBUG] [CvAtticEJB]: userID: " + userID);
    //System.out.println("[DEBUG] [CvAtticEJB]: ID: " + ID);
    //System.out.println("[DEBUG] [CvAtticEJB]: type: " + type);

    CVDal cvdl = new CVDal(dataSource);

    cvdl.setSql("attic.getatticmodule");
    cvdl.setInt(1, ID);
    cvdl.setString(2, type);

    Collection colAt = cvdl.executeQuery();
    cvdl.clearParameters();

    int moduleID = 0;
    int record = 0;

    if (colAt.size() > 0)
    {
      Iterator iter = colAt.iterator();

      while (iter.hasNext())
      {
        HashMap hm = (HashMap) iter.next();
        moduleID = ((Long) hm.get("moduleid")).intValue();
        record = ((Long) hm.get("record")).intValue();

        //System.out.println("[DEBUG] [CvAtticEJB]: hm: " + hm);
        //System.out.println("[DEBUG] [CvAtticEJB]: moduleID: " + moduleID);
        //System.out.println("[DEBUG] [CvAtticEJB]: record: " + record);

        cvdl.setSql("attic.getsequence");
        cvdl.setInt(1, moduleID);
        cvdl.setInt(2, record);

        Collection colTables = cvdl.executeQuery();
        cvdl.clearParameters();

        if (colTables.size() > 0)
        {
          Iterator iterTabs = colTables.iterator();

          while (iterTabs.hasNext())
          {
            HashMap hmTab = (HashMap) iterTabs.next();
            int tableID = ((Long) hmTab.get("tableid")).intValue();

            //System.out.println("[DEBUG] [CvAtticEJB]: hmTab: " + hmTab);
            //System.out.println("[DEBUG] [CvAtticEJB]: tableID: " + tableID);

            cvdl.setSql("attic.gettable");
            cvdl.setInt(1, tableID);

            Collection colTabName = cvdl.executeQuery();
            cvdl.clearParameters();

            String tableName = "";

            if (colTabName.size() > 0)
            {
              Iterator iter1 = colTabName.iterator();

              while (iter1.hasNext())
              {
                HashMap hm1 = (HashMap) iter1.next();
                tableName = (String) hm1.get("name");
                //System.out.println("[DEBUG] [CvAtticEJB]: tableName: " + tableName);
              }
            }
            cvdl.setSqlQuery("Select * from " + tableName);
            HashMap hmMetaData = cvdl.getTableMetaData(tableName);
            cvdl.clearParameters();

            String columnNames = "";
            String columnValues = "";

            cvdl.setSql("attic.columnnames");
            cvdl.setInt(1, ID);
            cvdl.setInt(2, tableID);

            Collection colFieldNames = cvdl.executeQuery();
            cvdl.clearParameters();

            if (colFieldNames.size() > 0)
            {
              Iterator iterField = colFieldNames.iterator();

              int i = 0;

              while (iterField.hasNext())
              {
                String fieldName = "";
                String fieldValue = "";

                if (i > 0)
                {
                  columnValues = columnValues + ",";
                  columnNames = columnNames + ",";
                }

                HashMap hmField = (HashMap) iterField.next();
                fieldName = (String) hmField.get("name");
                columnNames = columnNames + fieldName;

                //System.out.println("[DEBUG] [CvAtticEJB]: hmField: " + hmField);
                //System.out.println("[DEBUG] [CvAtticEJB]: fieldName: " + fieldName);
                //System.out.println("[DEBUG] [CvAtticEJB]: columnNames: " + columnNames);

                if (hmMetaData.containsKey(fieldName.toLowerCase()))
                {
                  fieldValue = (String) hmField.get("value");
                  //System.out.println("[DEBUG] [CvAtticEJB]: fieldValue: " + fieldValue);

                  switch (((Integer) hmMetaData.get(fieldName.toLowerCase())).intValue())
                  {
                  case Types.LONGVARCHAR:
                    if (fieldValue.equals(""))
                    {
                      fieldValue = "";
                    }
                    fieldValue = "'" + fieldValue + "'";
                    break;
                  case Types.TIMESTAMP:
                    if (fieldValue.equals(""))
                    {
                      fieldValue = "";
                    }
                    fieldValue = "'" + fieldValue + "'";
                    break;
                  case Types.DATE:
                    if (fieldValue.equals(""))
                    {
                      fieldValue = "";
                    }
                    fieldValue = "'" + fieldValue + "'";
                    break;
                  case Types.TIME:
                    if (fieldValue.equals(""))
                    {
                      fieldValue = "";
                    }
                    fieldValue = "'" + fieldValue + "'";
                    break;
                  case Types.VARCHAR:
                    if (fieldValue.equals(""))
                    {
                      fieldValue = "";
                    }
                    fieldValue = "'" + fieldValue + "'";
                    break;
                  case Types.INTEGER:
                    if (fieldValue.equals(""))
                    {
                      fieldValue = null;
                    }
                    break;
                  case Types.FLOAT:
                    if (fieldValue.equals(""))
                    {
                      fieldValue = null;
                    }
                    break;
                  case Types.DOUBLE:
                    if (fieldValue.equals(""))
                    {
                      fieldValue = null;
                    }
                    break;
                  case Types.CHAR:
                    if (fieldValue.equals(""))
                    {
                      fieldValue = "";
                    }
                    fieldValue = "'" + fieldValue + "'";
                    break;
                  }

                  columnValues = columnValues + fieldValue;
                }

                i++;
              }
            }

            String insertQuery = "insert into " + tableName + "(" + columnNames 
              + ")values(" + columnValues + ")";

            cvdl.setSqlQuery(insertQuery);
            cvdl.executeUpdate();
            cvdl.clearParameters();
          }
        }
      }
    }

    cvdl.setSql("attic.deletegarbage");
    cvdl.setInt(1, ID);
    cvdl.setString(2, type);
    cvdl.executeUpdate();
    cvdl.clearParameters();

    cvdl.destroy();
  }

  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * This simply sets the target datasource to be used for DB interaction
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  }
}