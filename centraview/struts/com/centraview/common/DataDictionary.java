/*
 * $RCSfile: DataDictionary.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:56 $ - $Author: mking_cv $
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import javax.naming.CommunicationException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.centraview.search.Search;
import com.centraview.search.SearchHome;

public class DataDictionary
{
  private static Logger logger = Logger.getLogger(DataDictionary.class);
  MasterDictionary md = new MasterDictionary();
  HashMap finalMapping; // Mapping of the table to Different Table.
  private String dataSource = null;

  public DataDictionary(String dataSource) throws CommunicationException, NamingException
  {
    this.dataSource = dataSource;
    this.finalMapping = new HashMap();
    SearchHome searchHome = (SearchHome)CVUtility.getHomeObject("com.centraview.search.SearchHome", "Search");
    try
    {
      Search searchRemote = (Search)searchHome.create();
      searchRemote.setDataSource(this.dataSource);
      this.finalMapping = searchRemote.getFinalMapping();
    } catch (Exception e) {
      logger.error("[DataDictionary] Exception thrown.", e);    
    }
  }
  
  public HashMap getFinalMapping()
  {
    return this.finalMapping;
  }

  public String getAdvanceQuery(String powerEditString, String primaryType, String target) throws CommunicationException, NamingException
  {
    //		String powerEditString = "Entity.Name <begins with> 'ABC Litho' AND
    // Address.Street1 <begins with> 'diwaakr purighalla' OR Address.City
    // <begins with> 'pune' ";
    //		String primaryType= " EntityID ";
    //		String target = "Entity";

    MasterDictionary mdir = null;
    SearchHome yy = (SearchHome)CVUtility.getHomeObject("com.centraview.search.SearchHome", "Search");
    try
    {
      Search remote = (Search)yy.create();
      remote.setDataSource(this.dataSource);
      mdir = remote.getMasterDictionary();
    } catch (Exception e) {
      logger.error("[getAdvanceQuery] Exception thrown.", e);
    }

    SearchCollection scol = new SearchCollection();
    ArrayList list = scol.getSearchCollection(powerEditString);

    Set globalSet = new TreeSet();

    StringBuffer newsb = new StringBuffer();
    newsb.append(" And ( 1 =1 ");
    String searchClause = "";

    for (int i = 0; i < list.size(); i++)
    {

      String srecord = (String)list.get(i);
      SearchRecord aa = new SearchRecord(this.dataSource);
      aa.setPowerString(srecord);
      String tableName = aa.getTable();
      if (tableName != null)
        tableName = tableName.trim();
      String columnName = aa.getColumn();
      if (columnName != null)
        columnName = columnName.trim();

      String condition = aa.getCondition();
      if (condition != null)
        condition = condition.trim();

      String criteria = aa.getCriteria();
      if (criteria != null)
        criteria = criteria.trim();

      String join = aa.getJoin();
      if (join == null)
        join = "AND";

      //			String target = "Entity";
      //			String source = "Address";

      String source = tableName;
      HashMap map = mdir.getQuryData(target, source);
      searchClause = searchClause + (String)map.get("Search");
      Set tablesName = (Set)map.get("Table");

      Object[] objArray = tablesName.toArray();
      for (int j = 0; j < objArray.length; j++)
      {
        globalSet.add((String)objArray[j]);
      }

      String cl = " " + join + " " + tableName + "." + columnName;
      if (condition.equalsIgnoreCase("begins with")) 
      {
        cl = cl + "  like  '" + criteria + "%'";
      } else if (condition.equalsIgnoreCase("ends with")) {
        cl = cl + "  like  '%" + criteria + "'";
      } else if (condition.equalsIgnoreCase("contains")) {
        cl = cl + "  like  '%" + criteria + "%'";
      } else if (condition.equalsIgnoreCase("equals")) {
        cl = cl + "  =  '" + criteria + "'";
      } else if (condition.equalsIgnoreCase("less than")) {
        cl = cl + "  <  '" + criteria + "'";
      } else if (condition.equalsIgnoreCase("greater than")) {
        cl = cl + "  <  '" + criteria + "'";
      }
      newsb.append(cl);
    }// end of for
    newsb.append(" ) ");
    String finalSql = " Select " + primaryType + " from ";
    Object[] objArray = globalSet.toArray();
    for (int j = 0; j < objArray.length; j++)
    {
      finalSql = finalSql + " " + (String)objArray[j];
      if (j < objArray.length - 1)
      {
        finalSql = finalSql + " , ";
      }
    }
    finalSql = finalSql + "   where 1 = 1 ";
    finalSql = finalSql + " " + searchClause + " " + newsb.toString();

    return finalSql;
  }// end of getAdvanceQuery
}