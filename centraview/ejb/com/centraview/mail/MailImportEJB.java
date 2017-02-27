/*
 * $RCSfile: MailImportEJB.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:36 $ - $Author: mking_cv $
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

package com.centraview.mail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;

import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.customfield.CustomFieldLocal;
import com.centraview.customfield.CustomFieldLocalHome;

public class MailImportEJB implements SessionBean
{
  /** The SessionContext of this SessionBean. */
  protected SessionContext ctx;

  /** The JNDI/DataSource name this EJB will be using. */
  private String dataSource = "";

  public void ejbActivate() throws EJBException { } 

  public void ejbRemove() throws EJBException { } 

  public void ejbPassivate() throws EJBException { }

  public void ejbCreate() throws CreateException { }

  public void setSessionContext(SessionContext ctx) throws EJBException
  {
    this.ctx = ctx;
  }

  /**
   * Takes the body of the message, and parses all the import
   * information from it, returning an ArrayList containing
   * HashMaps representing the fields and values to be imported.
   * Only valid fields are returned in the ArrayList (ie: if
   * "Height:" is in the body, but is not a valid field, it will
   * not be returned in the ArrayList.
   * @param body String representing the message body to be parsed.
   * @param type String representing the type of import of this
   *        message (ie: "Contact", "Ticket, etc).
   * @return ArrayList containing HashMaps representing the
   *         fields and values to be imported.
   */
  public ArrayList parseMessageBody(String body, String type)
  {
    ArrayList fields = new ArrayList();
    body = body.replaceAll("\r", "");
    body = body.replaceAll("\f", "");
    StringTokenizer tokenizer = new StringTokenizer(body, "\n");
    
    while (tokenizer.hasMoreTokens())
    {
      String currentLine = (String)tokenizer.nextToken();
      if (currentLine.matches("\\s*[^:]*:\\s*.*"))
      {
        String[] parts = currentLine.split(":", 2);
        if (parts.length > 0)
        {
          HashMap fieldMap = new HashMap();
          fieldMap.put("key", parts[0].trim());
          fieldMap.put("value", parts[1].trim());
          
          HashMap validField = this.isValidField((String)fieldMap.get("key"), type);
          if (validField != null && validField.containsKey("name"))
          {
            fieldMap.put("fieldName", validField.get("fieldName"));
            fields.add(fieldMap);
          }
        }
      }
    }
    return fields;
  }   // end parseMessageBody() method

  /**
   * Returns an ArrayList containing String objects, each representing
   * a valid field name for the given <code>importType</code>. The
   * ArrayList is used to determine which fields from the message body
   * are valid to be imported.
   * @param importType String representing the type of import for
   *        which to return valid field names (ie: "Contact").
   * @return ArrayList of Strings with valid field names.
   */
  public ArrayList getValidFields(String importType)
  {
    ArrayList validFields = new ArrayList();

    // these are the defined static fields
    CVDal cvdal = new CVDal(this.dataSource);
    try
    {
      String query = "SELECT f.name, f.fieldName FROM mailimportfields f LEFT JOIN mailimporttypes t ON (f.typeID=t.typeID) WHERE t.name=?";
      cvdal.setSqlQuery(query);
      cvdal.setString(1, importType);
      Collection results = cvdal.executeQuery();
      if (results != null && results.size() > 0)
      {
        Iterator iter = results.iterator();
        while (iter.hasNext())
        {
          HashMap row = (HashMap)iter.next();
          validFields.add(row);
        }
      }
      // add the list of custom fields to the valid fields arraylist
      validFields.addAll(this.getValidCustomFields("Both"));
    }catch(Exception e){
      System.out.println("[Exception][ImportUtils] Exception thrown in getValidFields(): " + e);
      e.printStackTrace();
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return validFields;
  }   // end getValidFields() method

  /**
   * Returns an ArrayList containing String objects, each representing
   * a valid field name for a custom field. The ArrayList is used to
   * determine which fields from the message body are valid custom fields
   * to be imported.
   * @param fieldType The type of custom fields to return ("Entity", "Individual", "Both")
   * @return ArrayList of Strings with valid field names.
   */
  public ArrayList getValidCustomFields(String fieldType)
  {
    ArrayList validFields = new ArrayList();
    try
    {
      InitialContext ic = CVUtility.getInitialContext();
      CustomFieldLocalHome home = (CustomFieldLocalHome)ic.lookup("local/CustomField");
      CustomFieldLocal remote = home.create();
      remote.setDataSource(this.dataSource);

      if (fieldType.equals("Both") || fieldType.equals("Individual"))
      {
        // Collections of hashmaps
        Collection individualCustomFields = remote.getCustomFieldImportData("individual");
        if (individualCustomFields != null && individualCustomFields.size() > 0)
        {
          Iterator indivIter = individualCustomFields.iterator();
          while (indivIter.hasNext())
          {
            HashMap field = (HashMap)indivIter.next();
            // hashmap to be added to the arraylist returned from this method
            HashMap fieldMap = new HashMap();
            String fieldName = (String)field.get("name");
            Number fieldID = (Number)field.get("customfieldid");
            fieldMap.put("fieldID", fieldID);
            
            fieldMap.put("name", fieldName);
            // strip all non-alphanumeric chars from fieldName
            fieldName = fieldName.replaceAll("[^\\w]", "");
            // prepend fieldname with "cf_" so we know this is a custom field
            fieldMap.put("fieldName", "cf_" + fieldName);
            validFields.add(fieldMap);
          }
        }
      }
      
      if (fieldType.equals("Both") || fieldType.equals("Entity"))
      {
        Collection entityCustomFields = remote.getCustomFieldImportData("entity");
        if (entityCustomFields != null && entityCustomFields.size() > 0)
        {
          Iterator entityIter = entityCustomFields.iterator();
          while (entityIter.hasNext())
          {
            HashMap field = (HashMap)entityIter.next();
            // hashmap to be added to the arraylist returned from this method
            HashMap fieldMap = new HashMap();
            String fieldName = (String)field.get("name");
            Number fieldID = (Number)field.get("customfieldid");
            fieldMap.put("fieldID", fieldID);

            // pre-pend human readable name with "Entity " in order to match entity fields
            fieldMap.put("name", "Entity " + fieldName);
            // strip all non-alphanumeric chars from fieldName
            fieldName = fieldName.replaceAll("[^\\w]", "");
            // prepend fieldname with "cf_Entity" so we know this is an entity custom field
            fieldMap.put("fieldName", "cf_Entity" + fieldName);
            validFields.add(fieldMap);
          }
        }
      }
    }catch(Exception e){
      System.out.println("[Exception][MailImportEJB] Exception thrown in getValidCustomFields(): " + e);
    }
    return validFields;
  }   // end getValidCustomFields() method

  private HashMap isValidField(String fieldName, String type)
  {
    ArrayList validFields = this.getValidFields(type);
    if (validFields != null && validFields.size() > 0)
    {
      Iterator iter = validFields.iterator();
      while (iter.hasNext())
      {
        HashMap fieldMap = (HashMap)iter.next();
        String validFieldName = (String)fieldMap.get("name");
        if (validFieldName.equals(fieldName))
        {
          return(fieldMap);
        }
      }
    }
    return(new HashMap());
  }   // end isValidField() method


  

  /**
   * Sets the target datasource to be used for DB interaction.
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  }

}   // end class definition

