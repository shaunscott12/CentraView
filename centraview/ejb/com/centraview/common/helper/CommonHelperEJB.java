/*
 * $RCSfile: CommonHelperEJB.java,v $    $Revision: 1.3 $  $Date: 2005/09/07 19:37:04 $ - $Author: mcallist $
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


package com.centraview.common.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;

import org.apache.log4j.Logger;

import com.centraview.common.CVDal;
import com.centraview.common.DDNameValue;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

public class CommonHelperEJB implements SessionBean
{
  private static Logger logger = Logger.getLogger(CommonHelperEJB.class);
  protected javax.ejb.SessionContext ctx;
  protected Context environment;
  private String dataSource = "";
  public void setSessionContext(SessionContext ctx)
  {
    this.ctx = ctx;
  }

  public void ejbCreate()
  {}

  public void ejbRemove()
  {}

  public void ejbActivate()
  {}

  public void ejbPassivate()
  {}

  /**
   * Returns the SourceName based on the sourceID. If the sourceID is 0 or not
   * found in the database, an empty string will be returned.
   * 
   * @param sourceID The ID of the Source to be found.
   * 
   * @return An empty String if the sourceID is 0 or not in the database.
   *         Otherwise, it will return the name of the source.
   */
  public String getSourceName(int sourceID)
  {
    CVDal dl = new CVDal(dataSource);
    String sourceName = "";
    try {
      if (sourceID != 0) {
        dl.setSql("common.getsource");
        dl.setInt(1, sourceID);

        Collection col = dl.executeQuery();

        Iterator it = col.iterator();

        if (it.hasNext()) {
          HashMap rec = (HashMap)it.next();
          sourceName = (String)rec.get("name");
        } //end of if statement (it.hasNext())
      } //end of if statement (sourceID != 0)\
    } finally {
      dl.destroy();
    }
    return sourceName;
  } //end of getSourceName method

  public HashMap getSourceList()
  {
    HashMap sourceList = new HashMap();
    CVDal dl = new CVDal(dataSource);
    try {
      dl.setSqlQuery("SELECT sourceid, name FROM source");
      Collection col = dl.executeQuery();
      Iterator it = col.iterator();
      int sourceID = 0;

      if (it.hasNext()) {
        HashMap hm = (HashMap)it.next();
        sourceID = ((Number)hm.get("sourceid")).intValue();

        String sourceName = (String)hm.get("name");
        sourceList.put(sourceName, Integer.toString(sourceID));
      } //end of if statement (it.hasNext())
    } finally {
      dl.destroy();
    }
    return sourceList;
  } //end of getSourceList method

  public Vector getAllSource()
  {
    return getMasterListVector("common.getallsource");
  }

  /**
   * Description - This method get the result in the vector as key/value pair.
   * It will never return null.
   * 
   * @param queryKey The name of the query to ba called from QueryCollection.
   * 
   * @return A Vector of DDNameValues with the list key/value pairs. It will
   *         never return null.
   * 
   * @see com.centraview.common.DDNameValue
   * @see com.centraview.common.QueryCollection
   */
  private Vector getMasterListVector(String queryKey)
  {
    Vector listVector = new Vector();
    CVDal dl = new CVDal(dataSource);
    try {
      dl.setSql(queryKey);
      Collection col = dl.executeQuery();

      DDNameValue emptyValue = new DDNameValue(0, "");
      listVector.add(emptyValue);

      if (col != null) {
        Iterator it = col.iterator();

        while (it.hasNext()) {
          HashMap hm = (HashMap)it.next();
          int id = Integer.parseInt(hm.get("id").toString());
          String name = (String)hm.get("name");

          DDNameValue dd = new DDNameValue(id, name);
          listVector.add(dd);
        } //end of while loop (it.hasNext())
      } //end of if (col != null)
    } //end of try block
    catch (Exception e) {
      System.out.println("[Exception] CommonHelperEJB.getMasterListVector: " + e.toString());
      //e.printStackTrace();
    } finally {
      dl.destroy();
    }
    return listVector;
  } //end of getMasterListVector method

  /**
   * Returns an ArrayList which contains HashMap objects representing the email
   * folders which are a sub-folder of the given <code>parentID</code>
   * parameter.
   * @param individualID The individualID of the user who is asking for the
   *          list.
   * @param parentID The folderID of the folder for which we want the list of
   *          sub-folders.
   * @param tableName An String that is containg the information of the Table.
   * @return ArrayList of HashMaps representing the sub-folders.
   */
  public ArrayList getSubFolderList(int individualID, int parentID, String tableName)
  {
    ArrayList folderList = new ArrayList();

    if (parentID <= 0) {
      return folderList;
    }

    CVDal cvdal = new CVDal(this.dataSource);

    try {
      // we have to display the current folder in the list too,
      // so that the user can navigate *UP* the folder list in
      // order to get to a higher-level folder.
      String query = "SELECT FolderID as folderID, Name AS folderName, Parent AS parentID FROM " + tableName + " WHERE Parent=? UNION SELECT FolderID as folderID, Name AS folderName, Parent AS parentID FROM " + tableName + " WHERE FolderID=? ORDER BY folderName";
      cvdal.setSqlQuery(query);
      cvdal.setInt(1, parentID);
      cvdal.setInt(2, parentID);
      Collection results = cvdal.executeQuery();

      if (results != null) {
        Iterator iter = results.iterator();
        while (iter.hasNext()) {
          HashMap row = (HashMap)iter.next();
          folderList.add(row);
        }
      }
    } catch (Exception e) {
      System.out.println("[Exception][MailEJB] Exception thrown in getSubFolderList(int,int): " + e);
      // e.printStackTrace();
    } finally {
      cvdal.destroy();
      cvdal = null;
    }
    return (folderList);
  } // end getSubFolderList(int,int,String) method

  /**
   * Returns an ArrayList that represents the full folder path of the given
   * folderID. The list will be keyed on folderID with the value being the
   * folder name. This list can be used in the view layer to display a linked
   * list of the full path.
   * @param folderID The ID of the folder whose path we're asking for.
   * @param tableName An String that is containg the information of the Table.
   * @return ArrayList representing the full folder path.
   */
  public ArrayList getFolderFullPath(int folderID, String tableName)
  {
    ArrayList folderPathList = new ArrayList();
    CVDal cvdal = new CVDal(this.dataSource);
    try {
      folderPathList = this.getFolderFullPath(folderID, cvdal, tableName);
    } catch (Exception e) {
      logger.error("[getFolderFullPath] Exception thrown.", e);
    } finally {
      cvdal.destroy();
      cvdal = null;
    }
    return (folderPathList);
  } // end getFolderFullPath(int,String) method

  /**
   * Returns an ArrayList that represents the full folder path of the given
   * folderID. The list will be keyed on folderID with the value being the
   * folder name. This list can be used in the view layer to display a linked
   * list of the full path.
   * @param folderID The ID of the folder whose path we're asking for.
   * @param cvdal An <strong>open </strong> database connection.
   * @param tableName An String that is containg the information of the Table.
   * @return ArrayList representing the full folder path.
   */
  private ArrayList getFolderFullPath(int folderID, CVDal cvdal, String tableName) throws Exception
  {
    ArrayList folderPathList = new ArrayList();
    try {
      this.getParentFolder(folderID, folderPathList, cvdal, tableName);
    } catch (Exception e) {
      logger.error("[getFolderFullPath] Exception thrown.", e);
    } finally {
      cvdal.setSqlQueryToNull();
    }
    // this reverses the order of the array list, since we
    // looped recursively from node to root, we were backwards
    java.util.Collections.reverse(folderPathList);
    return folderPathList;
  } // end getFolderFullPath(int,CVDal,String) method

  /**
   * This method calls itself recursively in order to generate the full folder
   * path list of a given folder ID. During each successive iteration, a new
   * HashMap is added to the folderPathList ArrayList method parameter.
   * <strong>**NOTE** </strong>- the ArrayList that will be returned will be in
   * <strong>*REVERSE* </strong> order, so the calling method (which should
   * always be MailEJB.getFullFolderPath()) will need to reverse the sort of the
   * ArrayList.
   * @param folderID The ID of the folder whose details we are going to get from
   *          the database, and add to the list as a HashMap.
   * @param folderPathList An ArrayList that is populated and passed by
   *          reference, to which this method adds one HashMap entry during each
   *          iteration.
   * @param cvdal An <strong>open </strong> database connection.
   * @param tableName An String that is containg the information of the Table.
   * @return void
   */
  private void getParentFolder(int folderID, ArrayList folderPathList, CVDal cvdal, String tableName) throws Exception
  {
    try {
      // get the folder info here...
      String query = "SELECT FolderID AS folderID, Parent AS parentID, Name as name FROM " + tableName + " WHERE FolderID=?";
      cvdal.setSqlQuery(query);
      cvdal.setInt(1, folderID);
      Collection results = cvdal.executeQuery();
      if (results != null) {
        Iterator iter = results.iterator();
        if (iter.hasNext()) {
          HashMap folderInfo = (HashMap)iter.next();
          Number parentID = (Number)folderInfo.get("parentID");

          // ...then add to the array list...
          folderPathList.add(folderInfo);

          // ...then call this method recursively
          if (parentID.intValue() > 0) {
            this.getParentFolder(parentID.intValue(), folderPathList, cvdal, tableName);
          }
        }
      }
    } catch (Exception e) {
      System.out.println("[Exception][MailEJB] Exception thrown in getParentFolder(int,ArrayList,CVDal): " + e);
      // e.printStackTrace();
    } finally {
      cvdal.setSqlQueryToNull();
    }
    return;
  } // end getParentFolder(int,ArrayList,CVDal,String) method

  /**
   * @author Kevin McAllister <kevin@centraview.com>This simply sets the target
   *         datasource to be used for DB interaction
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  }

  /**
   * Returns a sourceID for the Passed Source Name. If suppose the SourceId is
   * -1 then we Insert the Source and get the SourceID
   * 
   * @param sourceName sourceName contains the information of the Source Name
   * @return sourceID The ID of the Source we are looking for.
   */
  public int getSourceID(String sourceName)
  {
    CVDal cvdal = new CVDal(this.dataSource);
    int sourceID = -1;
    // We will try to do and case Insensitive and Remove all the white spaces
    // and then match the String.
    // try to get the sourceID
    try {
      String selectQuery = "SELECT SourceID FROM source where Upper(Trim(Replace(Name,' ','')))= ?";
      cvdal.setSqlQuery(selectQuery);
      String tempSourceName = (sourceName.replaceAll(" ", "")).toUpperCase();
      cvdal.setString(1, tempSourceName.trim());
      Collection col = cvdal.executeQuery();
      cvdal.setSqlQueryToNull();
      if (col != null) {
        Iterator it = col.iterator();
        if (it.hasNext()) {
          HashMap resultsHashMap = (HashMap)it.next();
          Number sourceNumberID = (Number)resultsHashMap.get("SourceID");
          sourceID = sourceNumberID.intValue();
        }// end of if (it.hasNext())
      }// end of if(col != null){
    }//end of try block
    catch (Exception e) {
      e.printStackTrace();
    }// end of catch Block

    // Still If we get the Same Result the insert a new Entry in the source
    // table and return the SourceID
    if (sourceID == -1) {
      String insertQuery = "INSERT INTO source (Name) Values(?);";
      cvdal.setSqlQuery(insertQuery);
      cvdal.setString(1, sourceName);
      cvdal.executeUpdate();
      sourceID = cvdal.getAutoGeneratedKey();
      cvdal.setSqlQueryToNull();
    }// end of if(sourceID == -1)
    cvdal.destroy();
    return sourceID;
  }// end of public int getSourceID(String sourceName)

  /**
   * This method will frame Collection moduleName and moduleID
   * 
   * @return An Collection of the ModuleName as the Key Value and moduleID as
   *         value.
   */
  public HashMap getModuleList()
  {
    CVDal dl = new CVDal(dataSource);
    HashMap moduleNameList = new HashMap();
    try {
      dl.setSqlQuery("select Distinct name, moduleid from module");
      Collection col = dl.executeQuery();
      Iterator it = col.iterator();
      while (it.hasNext()) {
        HashMap moduleRec = (HashMap)it.next();
        String moduleName = (String)moduleRec.get("name");
        String moduleID = ((Number)moduleRec.get("moduleid")).toString();
        moduleNameList.put(moduleName, moduleID);
      } //end of while statement (it.hasNext())
    } finally {
      dl.destroy();
    }
    return moduleNameList;
  } //end of getModuleList method

  /**
   * Gets the Source Value List for the source lookup.
   * @param individualId
   * @param parameters
   * @return
   */
  public ValueListVO getSourceValueList(int individualId, ValueListParameters parameters)
  {
    ArrayList list = new ArrayList();
    CVDal cvdl = new CVDal(this.dataSource);
    try {
      cvdl.setSql("common.getsourcelist");
      list = cvdl.executeQueryList(1);
      parameters.setTotalRecords(list.size());
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
    return new ValueListVO(list, parameters);
  }
  
  /**
   * Gets the Location Value List for the location lookup.
   * @param individualId
   * @param parameters
   * @return
   */
  public ValueListVO getLocationValueList(int individualId, ValueListParameters parameters)
  {
    ArrayList list = new ArrayList();
    CVDal cvdl = new CVDal(this.dataSource);
    try {
      cvdl.setSqlQuery("SELECT locationid, title FROM location");
      list = cvdl.executeQueryList(1);
      parameters.setTotalRecords(list.size());
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
    return new ValueListVO(list, parameters);
  }
} //end of CommonHelperEJB class
