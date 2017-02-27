/*
 * $RCSfile: SearchRecord.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:16 $ - $Author: mking_cv $
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
import java.util.Iterator;
import java.util.Vector;

import javax.naming.CommunicationException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class SearchRecord
{
  private static Logger logger = Logger.getLogger(SearchRecord.class);
  private String strJoin;
  private String strTable;
  private String strColumn;
  private String strCriteria;
  private String strCondition;
  private String dataSource;

  public SearchRecord(String table, String column, String criteria, String condition, String join)
  {
    strTable = table;
    strColumn = column;
    strCondition = condition;
    strCriteria = criteria;
    strJoin = join;
  }

  public SearchRecord(String dataSource)
  {
    this.dataSource = dataSource;
  }

  //Assuming PS will be in this format , join is optional
  // AND Table.column condition criteria
  public void setPowerString(String ps)
  {
    try
    {
      int indexForJoin = ps.indexOf("AND");
      if (indexForJoin == -1)
      {
        indexForJoin = ps.indexOf("OR");

        if (indexForJoin == -1)
        {
          strJoin = null;
        } else {
          strJoin = "OR";
        }
      } else {
        strJoin = "AND";
      }
      int indexForDot = ps.indexOf(".");
      int lastIndexForJoin = 0;
      int indexForPrevWS = 0;
      int indexForNextWS = 0;

      if (strJoin != null)
      {
        lastIndexForJoin = ps.lastIndexOf(strJoin);
        indexForPrevWS = ps.indexOf(" ", lastIndexForJoin);
      }

      if (indexForDot != -1)
      {
        if (indexForPrevWS != -1)
        {
          strTable = ps.substring(indexForPrevWS, indexForDot);
        } else {
          strTable = null;
        }
        indexForNextWS = ps.indexOf(" ", indexForDot);
        if (indexForNextWS != -1)
        {
          strColumn = ps.substring(indexForDot + 1, indexForNextWS);
        } else {
          strColumn = null;
        }
      }

      int indexForG = ps.indexOf("<");
      int indexForL = ps.indexOf(">");

      if (indexForG != -1 && indexForL != -1)
      {
        strCondition = ps.substring(indexForG + 1, indexForL);
      } else {
        strCondition = null;
      }

      int indexForSQ = 0;
      int indexForEQ = 0;

      indexForSQ = ps.indexOf("'");
      if (indexForSQ != -1)
      {
        indexForEQ = ps.indexOf("'", indexForSQ + 1);
        if (indexForEQ != -1)
        {
          strCriteria = ps.substring(indexForSQ + 1, indexForEQ);
        } else {
          strCriteria = null;
        }
      }
    } catch (Exception e) {
      logger.error("[setPowerString] Exception thrown.", e);
    }
  }

  public String getPowerString()
  {
    if (strJoin == null)
    {
      strJoin = "";
    }

    String strPowerString = " " + strJoin + " " + strTable + "." + strColumn + "  <" + strCondition + ">  " + "'" + strCriteria + "' ";

    return strPowerString;
  }

  public boolean isValidRecord(String tbl) throws CommunicationException, NamingException
  {
    boolean isValidRecord = false;

    ArrayList tblArl = new ArrayList();
    ArrayList colArl = new ArrayList();
    Vector condVec = new Vector();
    Vector joinVec = new Vector();

    condVec.add("Begins With");
    condVec.add("Equals");
    condVec.add("Greater Than");
    condVec.add("Less Than");
    condVec.add("Contains");
    condVec.add("Ends With");

    joinVec.add("AND");
    joinVec.add("OR");

    //Check all mem variables with DB
    DataDictionary dd = new DataDictionary(dataSource);
    HashMap fhm = dd.getFinalMapping();
    HashMap hm = (HashMap)fhm.get(tbl.trim());

    Vector colVec = null;
    Iterator iterator = (hm.keySet()).iterator();
    String table = null;

    while (iterator.hasNext())
    {
      table = (String)iterator.next();

      //If table name is valid then only check for columns
      if (strTable != null && table.equals(strTable.trim()))
      {
        colVec = (Vector)hm.get(table);

        if (strColumn != null && colVec.contains(strColumn.trim()) == true)
        {
          if (strCondition != null && condVec.contains(strCondition.trim()) == true)
          {
            if (strJoin != null)
            {
              if (joinVec.contains(strJoin.trim()) == true)
              {
                isValidRecord = true;
                break;
              }
            } else
            {
              isValidRecord = true;
              break;
            }
          }
        }
      }
    }
    return isValidRecord;
  }

  public String getTable()
  {
    if (strTable != null)
      strTable = strTable.trim();

    return strTable;
  }

  public String getColumn()
  {
    if (strColumn != null)
      strColumn = strColumn.trim();

    return strColumn;
  }

  public String getJoin()
  {
    if (strJoin != null)
      strJoin = strJoin.trim();

    return strJoin;
  }

  public String getCondition()
  {
    if (strCondition != null)
      strCondition = strCondition.trim();

    return strCondition;
  }

  public String getCriteria()
  {

    if (strCriteria != null)
      strCriteria = strCriteria.trim();

    return strCriteria;
  }

  public void setTable(String table)
  {
    if (table != null)
      table = table.trim();

    strTable = table;
  }

  public void setColumn(String column)
  {
    if (column != null)
      column = column.trim();

    strColumn = column;
  }

  public void setCondition(String condition)
  {
    if (condition != null)
      condition = condition.trim();

    strCondition = condition;
  }

  public void setCriteria(String criteria)
  {
    if (criteria != null)
      criteria = criteria.trim();

    strCriteria = criteria;
  }

  public void setJoin(String join)
  {
    if (join != null)
      join = join.trim();

    strJoin = join;
  }
}