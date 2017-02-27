/*
 * $RCSfile: CustomFieldEJB.java,v $    $Revision: 1.9 $  $Date: 2005/08/25 15:56:40 $ - $Author: mcallist $
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

package com.centraview.customfield;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;

import org.apache.log4j.Logger;

import com.centraview.common.CVDal;
import com.centraview.common.CustomFieldList;
import com.centraview.common.CustomFieldListElement;
import com.centraview.common.DDNameValue;
import com.centraview.common.EJBUtil;
import com.centraview.common.IntMember;
import com.centraview.common.StringMember;
import com.centraview.contact.helper.CustomFieldVO;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

public class CustomFieldEJB implements SessionBean {
  private static Logger logger = Logger.getLogger(CustomFieldEJB.class);
  protected javax.ejb.SessionContext ctx;
  protected Context environment;
  private String dataSource = "";

  /**
   * Returns a lsit of custom fields and their values for a particular record.
   */
  public CustomFieldList getCustomFieldList(String recordType, int recordID)
  {
    // This method returns a customfields and their values for a particular
    // record.
    // It is used in relatedInfo primarily.
    // If a method is needed to get a list of customfields for a particular
    // recordtype without values,
    // write another one.
    CustomFieldList cfList = new CustomFieldList();
    CVDal cvdl = new CVDal(dataSource);

    try {
      cvdl.clearParameters();
      cvdl.setSql("customfield.createTempListTable");
      cvdl.executeUpdate();

      cvdl.clearParameters();
      cvdl.setSql("customfield.insertIntoTempListTable");
      cvdl.setString(1, recordType);
      cvdl.executeUpdate();

      cvdl.clearParameters();
      cvdl.setSql("customfield.updateTempListScalar");
      cvdl.setInt(1, recordID);
      cvdl.executeUpdate();

      cvdl.clearParameters();
      cvdl.setSql("customfield.updateTempListMultiple");
      cvdl.setInt(1, recordID);
      cvdl.executeUpdate();

      cvdl.clearParameters();
      cvdl.setSql("customfield.getCustomFieldList");
      Collection col = cvdl.executeQuery();

      cvdl.clearParameters();
      cvdl.setSql("customfield.deleteTempTable");
      cvdl.executeUpdate();

      if (col != null) {
        Iterator it = col.iterator();

        while (it.hasNext()) {
          HashMap hm = (HashMap) it.next();

          int customFieldID = ((Number) hm.get("customfieldid")).intValue();

          IntMember addId = new IntMember("CustomFieldID", customFieldID, 10, "/centraview/ViewHandler.do?customFieldID=" + customFieldID, 'T',
              false, 10);
          StringMember field = new StringMember("Field", (String) hm.get("name"), 10, "requestURL", 'T', true);
          StringMember value = new StringMember("Value", (String) hm.get("value"), 10, "requestURL", 'T', false);

          CustomFieldListElement listElement = new CustomFieldListElement(customFieldID);

          listElement.put("CustomFieldID", addId);
          listElement.put("Field", field);
          listElement.put("Value", value);

          cfList.put((String) hm.get("Field") + customFieldID, listElement);
        }
      }
    } catch (Exception e) {
      logger.error("[getCustomFieldList]: Exception", e);
    } finally {
      cvdl.destroy();
    }
    return (cfList);
  }

  public ValueListVO getCustomFieldValueList(int individualId, ValueListParameters parameters)
  {
    ArrayList list = new ArrayList();
    String filter = parameters.getFilter();
    CVDal cvdl = new CVDal(this.dataSource);
    try {
      if (filter != null && filter.length() > 0) {
        String str = "CREATE TABLE customfieldlistfilter " + filter;
        cvdl.setSqlQuery(str);
        cvdl.executeUpdate();
        cvdl.setSqlQueryToNull();
      }
      int numberOfRecords = 0;
      numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "customfieldlistfilter", individualId, 0, "customfield", "customFieldId", "owner", null,
          false);
      parameters.setTotalRecords(numberOfRecords);
      String query = this.buildCustomFieldListQuery(parameters);
      cvdl.setSqlQuery(query);
      list = cvdl.executeQueryList(1);
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE customfieldlistfilter");
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE listfilter");
      cvdl.executeUpdate();
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
    return new ValueListVO(list, parameters);
  }

  private String buildCustomFieldListQuery(ValueListParameters parameters)
  {
    StringBuffer query = new StringBuffer();
    query.append("SELECT cf.customFieldId, cf.name, cf.fieldType, cfs.value ");
    query.append("FROM customfield AS cf, listfilter AS lf " + "LEFT OUTER JOIN customfieldscalar AS cfs ON cfs.recordid = ");
    query.append(parameters.getExtraId());
    query.append(" AND cf.customfieldId = cfs.customfieldId WHERE cf.customfieldid = lf.customfieldid AND cf.fieldType = 'SCALAR' ");
    query.append("UNION SELECT cf.customFieldId, cf.name, cf.fieldType, cfv.value ");
    query.append("FROM customfield AS cf, listfilter AS lf LEFT OUTER JOIN customfieldmultiple AS cfm ON cfm.recordid = ");
    query.append(parameters.getExtraId());
    query
        .append(" AND cf.customfieldId = cfm.customfieldId LEFT OUTER JOIN customfieldvalue AS cfv ON cfm.valueId = cfv.valueId AND cfv.customfieldId = cf.customFieldId WHERE cf.customfieldid = lf.customfieldid AND cf.fieldType = 'MULTIPLE' ");
    String orderBy = "ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
    String limit = parameters.getLimitParam();
    query.append(orderBy);
    query.append(limit);
    return query.toString();
  }

  // end of getCustomFieldList

  /**
   * This method returns the CustomFields for Particular type of recordType .
   * For Example: recordType ='entity'
   */
  public Collection getCustomFieldImportData(String recordType)
  {
    Collection col = null;
    CVDal dl = new CVDal(dataSource);
    try {
      dl.setSql("common.getCustomFields");
      dl.setString(1, recordType);
      col = dl.executeQuery();
    } catch (Exception e) {
      logger.error("[getCustomFieldImportData]: Exception", e);
    } finally {
      dl.destroy();
    }

    return col;
  }

  // end of getCustomFieldImportData

  /**
   * This method returns the CustomFieldData .
   */
  public TreeMap getCustomFieldData(String recordType)
  {
    TreeMap cusData = null;
    CVDal dl = new CVDal(dataSource);
    try {
      cusData = new TreeMap();
      dl.setSql("common.getCustomField");
      dl.setString(1, recordType);
      Collection col = dl.executeQuery();
      Iterator it = col.iterator();
      while (it.hasNext()) {
        CustomFieldVO field = new CustomFieldVO();
        HashMap hm = (HashMap) it.next();
        int fieldID = ((Long) hm.get("customfieldid")).intValue();
        String label = (String) hm.get("name");
        String fieldType = (String) hm.get("fieldtype");
        int recordTypeID = ((Long) hm.get("recordtype")).intValue();
        field.setFieldID(fieldID);
        field.setLabel(label);
        field.setFieldType(fieldType);
        field.setRecordTypeID(recordTypeID);
        cusData.put("" + fieldID, field);
      } // end of while
      dl.clearParameters();
      dl.setSql("common.getCustomFieldOption");
      dl.setString(1, recordType);
      col = dl.executeQuery();
      it = col.iterator();
      while (it.hasNext()) {
        HashMap hm = (HashMap) it.next();
        int fieldID = ((Long) hm.get("customfieldid")).intValue();
        int valueID = ((Long) hm.get("valueid")).intValue();
        String value = (String) hm.get("value");
        CustomFieldVO cf = (CustomFieldVO) cusData.get(String.valueOf(fieldID));
        Vector vec = cf.getOptionValues();
        if (vec == null) {
          vec = new Vector();
          DDNameValue dd = new DDNameValue(valueID, value);
          vec.add(dd);
        } else {
          DDNameValue dd = new DDNameValue(valueID, value);
          vec.add(dd);
        }
        cf.setOptionValues(vec);
        cusData.put(String.valueOf(fieldID), cf);
      }
    } catch (Exception e) {
      logger.error("[getCustomFieldData]: Exception", e);
    } finally {
      dl.destroy();
      dl = null;
    }
    return cusData;
  } // end of getCustomFieldData

  /**
   * This method gets and returns a TreeMap with the CustomField values. These
   * values are sorted in alpahbetical order.
   * @param recordType The type of record.
   * @param recordID The record ID.
   * @return The values for the custom field.
   */
  public TreeMap getCustomFieldData(String recordType, int recordID)
  {
    TreeMap cusData = new TreeMap();
    CVDal dl = new CVDal(dataSource);
    try {
      // get the first three custom fields for a particular record type.
      dl.setSql("common.getCustomField");
      dl.setString(1, recordType);
      Collection col = dl.executeQuery();
      Iterator it = col.iterator();
      while (it.hasNext()) {
        CustomFieldVO field = new CustomFieldVO();
        HashMap hm = (HashMap) it.next();
        int fieldID = ((Long) hm.get("customfieldid")).intValue();
        String label = (String) hm.get("name");
        String fieldType = (String) hm.get("fieldtype");
        int recordTypeID = ((Long) hm.get("recordtype")).intValue();
        field.setFieldID(fieldID);
        field.setLabel(label);
        field.setFieldType(fieldType);
        field.setRecordTypeID(recordTypeID);
        cusData.put(String.valueOf(fieldID), field);
      } // end of while
      dl.setSqlQueryToNull();
      // now get the all the values of custom fields for this record
      String str = " SELECT cf.customfieldid, cf.name, cf.fieldtype,cf.recordType,cfv.valueid,cfv.value ,cfm.valueID as selected from customfield cf,customfieldvalue cfv left outer join customfieldmultiple cfm on ( cfv.customfieldid = cfm.customfieldid and cfv.valueid = cfm.valueid and cfm.recordid = "
          + recordID
          + " ),cvtable where cf.customfieldid = cfv.customfieldid and cf.recordtype = cvtable.tableid and  cvtable.name  =  '"
          + recordType + "' ";
      str = str
          + " union SELECT cf.customfieldid, cf.name, cf.fieldtype,cf.recordType,null ,cfs.value ,null as seleted from customfield cf,customfieldscalar cfs ,cvtable where cf.customfieldid = cfs.customfieldid and cf.recordtype = cvtable.tableid and  cvtable.name  =  '"
          + recordType + "'  and cfs.recordid = " + recordID + " order by value";
      dl.setSqlQuery(str);
      col = dl.executeQuery();
      it = col.iterator();
      while (it.hasNext()) {
        HashMap hm = (HashMap) it.next();
        int fieldID = ((Number) hm.get("customfieldid")).intValue();
        String fieldType = (String) hm.get("fieldtype");
        // The query returns scalars with the ID as NULL
        // Also some non-selected multiples give NULL
        // So we have to do something about that
        Object valueIdObject = hm.get("valueid");
        int valueId = 0;
        if (valueIdObject != null) {
          try { 
            valueId = Integer.parseInt(valueIdObject.toString());
          } catch (NumberFormatException nfe) {}
        }
        String value = (String) hm.get("value");
        Object selectedObject = hm.get("selected");
        String selected;
        if (selectedObject != null) {
          selected = selectedObject.toString();
        } else {
          selected = "";
        }
        // Dig the custom field back out and set the options and values on it.
        CustomFieldVO cf = (CustomFieldVO) cusData.get(String.valueOf(fieldID));
        if (fieldType.equals("MULTIPLE")) {
          Vector optionsVector = cf.getOptionValues();
          if (optionsVector == null) {
            optionsVector = new Vector();
            DDNameValue dd = new DDNameValue(valueId, value);
            optionsVector.add(dd);
          } else {
            DDNameValue dd = new DDNameValue(valueId, value);
            optionsVector.add(dd);
          }
          cf.setOptionValues(optionsVector);
          if (!selected.equals("")) {
            cf.setValue(selected);
          }
        } else if (fieldType.equals("SCALAR")) {
          cf.setValue(value);
        }
      } // end of while loop on values query
    } catch (Exception e) {
      logger.error("[getCustomFieldData] Exception thrown.", e);
      throw new EJBException(e);
    } finally {
      dl.destroy();
      dl = null;
    }
    return cusData;
  } // end of getCustomFieldData(String recordType ,int recordID)

  public CustomFieldVO getCustomField(int customFieldID, int recordID)
  {
    CustomFieldVO field = null;
    CVDal dl = new CVDal(dataSource);
    try {
      dl.setSql("common.getCustomFieldOnly");
      dl.setInt(1, customFieldID);
      Collection col = dl.executeQuery();
      Iterator it = col.iterator();
      while (it.hasNext()) {
        field = new CustomFieldVO();
        HashMap hm = (HashMap) it.next();
        int fieldID = ((Long) hm.get("customfieldid")).intValue();
        String label = (String) hm.get("name");
        String fieldType = (String) hm.get("fieldtype");
        int recordTypeID = ((Long) hm.get("recordtype")).intValue();
        field.setFieldID(fieldID);
        field.setLabel(label);
        field.setFieldType(fieldType);
        field.setRecordTypeID(recordTypeID);
      }
      String str = " SELECT cf.customfieldid, cf.name, cf.fieldtype,cf.recordType,cfv.valueid,cfv.value ,cfm.valueID as selected from customfield cf,customfieldvalue cfv left outer join customfieldmultiple cfm on ( cfv.customfieldid = cfm.customfieldid) and cfm.recordid= "
          + recordID + " where cf.customfieldid = cfv.customfieldid  and cf.customfieldid  = " + customFieldID;
      str = str
          + " union SELECT cf.customfieldid, cf.name, cf.fieldtype,cf.recordType,null ,cfs.value ,null as selected  from customfield cf left outer join customfieldscalar cfs on (cf.customfieldid = cfs.customfieldid) and cfs.recordid="
          + recordID + " where cf.customfieldid  = " + customFieldID;
      dl.setSqlQueryToNull();
      dl.setSqlQuery(str);
      col = dl.executeQuery();
      it = col.iterator();

      while (it.hasNext()) {
        HashMap hm = (HashMap) it.next();
        String fieldType = (String) hm.get("fieldtype");
        int valueID = ((Long) hm.get("valueid")).intValue();
        String value = (String) hm.get("value");
        Long selectedLong = (Long)hm.get("seleted");
        String selected = "";
        if (selectedLong != null) {
          selected = "" + selectedLong.intValue();
        }
        CustomFieldVO cf = field;
        if (fieldType.equals("MULTIPLE")) {
          Vector vec = cf.getOptionValues();
          if (value != null) {
            if (vec == null) {
              vec = new Vector();
              DDNameValue dd = new DDNameValue(valueID, value);
              vec.add(dd);
            } else {
              DDNameValue dd = new DDNameValue(valueID, value);
              vec.add(dd);
            }

            cf.setOptionValues(vec);
            if (selected == null) {
              selected = "";
            }
            selected = selected.trim();

            if (!selected.equals("")) {
              cf.setValue(selected);
            }
          }
        } else if (fieldType.equals("SCALAR")) {
          cf.setValue(value);
        }
        field = cf;
      }
    } catch (Exception e) {
      logger.error("[getCustomField]: Exception", e);
    } finally {
      dl.destroy();
    }
    return field;
  } // end of getCustomField(int customFieldID)

  /**
   * Adds a custom field to the database.
   * @param customFieldVO The Custom Field VOObject
   */
  public void addCustomField(CustomFieldVO customFieldVO)
  {
    String value = customFieldVO.getValue();
    String fieldType = customFieldVO.getFieldType();
    int fieldID = customFieldVO.getFieldID();
    int recordID = customFieldVO.getRecordID();
    CVDal dl = new CVDal(dataSource);

    try {
      if (fieldType.equals("SCALAR")) {
        // ALLSQL.put("common.addCustomFieldScalar"," insert into
        // customfieldscalar (customfieldid,recordid,value) values (?,?,? )");
        dl.setSql("common.addCustomFieldScalar");
        dl.setInt(1, fieldID);
        dl.setInt(2, recordID);
        dl.setString(3, value);
        dl.executeUpdate();
      } else if (fieldType.equals("MULTIPLE")) {
        // ALLSQL.put("common.addCustomFieldMultiple"," insert into
        // customfieldmultiple (customfieldid,recordid,valueid) values (?,?,?
        // )");
        dl.setSql("common.addCustomFieldMultiple");
        dl.setInt(1, fieldID);
        dl.setInt(2, recordID);
        dl.setString(3, value);
        dl.executeUpdate();
      }
    } // end of try block
    catch (Exception e) {
      logger.error("[addCustomField]: Exception", e);
    } // end of catch block (Exception)
    finally {
      dl.destroy();
      dl = null;
    } // end of finally block
  } // end of addCustomField

  public void updateCustomField(CustomFieldVO cfdata)
  {
    String value = cfdata.getValue(); // Value of the field
    String fieldType = cfdata.getFieldType(); // this is either SCALAR or
                                              // MULTIPLE

    int fieldID = cfdata.getFieldID(); // filedID
    int recordID = cfdata.getRecordID(); // recordID

    CVDal dl = new CVDal(dataSource);
    try {
      int recordsUpdated = 0;
      if (fieldType.equals("SCALAR")) {
        // update customfieldscalar set value = ? where customfieldid = ? and
        // recordid = ?
        dl.setSql("common.updateCustomFieldScalar");
        dl.setString(1, value);
        dl.setInt(2, fieldID);
        dl.setInt(3, recordID);
        recordsUpdated = dl.executeUpdate();
        // if we have a result of < 1 for executeUpdate() it means we
        // must not have a record for this custom field alread and
        // we need to insert and not update.
        if (recordsUpdated < 1) {
          this.addCustomField(cfdata);
        }
      } else if (fieldType.equals("MULTIPLE")) {
        // update customfieldmultiple set valueid = ? where customfieldid = ?
        // and recordid = ?
        dl.setSql("common.updateCustomFieldMultiple");
        dl.setString(1, value);
        dl.setInt(2, fieldID);
        dl.setInt(3, recordID);
        recordsUpdated = dl.executeUpdate();
        // if we have a result of < 1 for executeUpdate() it means we
        // must not have a record for this custom field alread and
        // we need to insert and not update.
        if (recordsUpdated < 1) {
          this.addCustomField(cfdata);
        }
      }
    } catch (Exception e) {
      logger.error("[updateCustomField]: Exception", e);
    } finally {
      dl.destroy();
      dl = null;
    }
  }

  // end of updateCustomField

  public CustomFieldList getCustomFieldList(int userID, HashMap hashmap)
  {
    Integer intStart = (Integer) hashmap.get("startATparam");
    Integer intEnd = (Integer) hashmap.get("EndAtparam");
    String strSearch = (String) hashmap.get("searchString");
    String strSortMem = (String) hashmap.get("sortmem");
    Character chrSortType = (Character) hashmap.get("sortType");
    String moduleName = (String) hashmap.get("module");

    char charSort = chrSortType.charValue();

    int intStartParam = intStart.intValue();
    int intEndParam = intEnd.intValue();

    int beginindex = Math.max(intStartParam - 100, 1);
    int endindex = intEndParam + 100;

    CustomFieldList cfList = new CustomFieldList();
    cfList.setPrimaryMember("Name");
    cfList.setSortMember(strSortMem);

    CVDal cvdl = new CVDal(dataSource);

    Collection colList = null;
    try {
      if ((strSearch != null) && strSearch.startsWith("ADVANCE:")) {
        strSearch = strSearch.substring(8);
        String str = "create TEMPORARY TABLE customfieldlistSearch " + strSearch;
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery(str);
        cvdl.executeUpdate();
        cvdl.clearParameters();

        String sortType = "ASC";

        if (charSort == 'A') {
          sortType = "ASC";
        } else {
          sortType = "DESC";
        }

        String strQuery = "select c.customfieldid as customfieldid,c.name as name,c.fieldtype as type,m.name as module,r.name as record from customfield c,module m,cvtable r,customfieldlistSearch cfsearch where r.moduleid=m.moduleid and c.recordtype=r.tableid  and cfsearch.customfieldid=c.customfieldid order by "
            + strSortMem + " " + sortType;
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery(strQuery);
        colList = cvdl.executeQuery();
        cvdl.clearParameters();

        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("DROP TABLE customfieldlistSearch");
        cvdl.executeUpdate();
      } else {
        String sortType = "ASC";

        if (charSort == 'A') {
          sortType = "ASC";
        } else {
          sortType = "DESC";
        }

        String strQuery = "select a.CustomFieldID as customfieldid, a.Name as name,a.FieldType as type,d.name as module,b.name as record from customfield a,cvtable b, module c, module d where a.RecordType = b.tableid and b.moduleid = c.moduleid and c.parentid = d.moduleid and d.name = ? union select a.CustomFieldID as customfieldid, a.Name as name,a.FieldType as type,b.name as module,b.name as record from customfield a,cvtable b, module c, module d where a.RecordType = b.tableid and b.moduleid = c.moduleid and  ISNULL(c.parentid) and c.name =?  order by "
            + strSortMem + " " + sortType;
        cvdl.setSqlQuery(strQuery);
        cvdl.setString(1, moduleName);
        cvdl.setString(2, moduleName);
        colList = cvdl.executeQuery();
      }

      if (colList.size() > 0) {
        Iterator it = colList.iterator();
        int i = 0;
        while (it.hasNext()) {
          i++;
          HashMap hm = (HashMap) it.next();
          int custfieldID = ((Long) hm.get("customfieldid")).intValue();
          try {
            IntMember intCustFieldID = new IntMember("CustomFieldID", custfieldID, 10, "", 'T', false, 10);
            StringMember strName = null;
            if ((hm.get("name") != null)) {
              strName = new StringMember("Name", (String) hm.get("name"), 10, "", 'T', true);
            } else {
              strName = new StringMember("Name", null, 10, "", 'T', true);
            }
            String typeValue = (hm.get("type") == null) ? "SCALAR" : (String) hm.get("type");
            if (typeValue.equals("SCALAR")) {
              typeValue = "Single";
            } else {
              typeValue = "Multiple";
            }
            StringMember strType = new StringMember("Type", typeValue, 10, "", 'T', false);
            StringMember strModule = null;
            if ((hm.get("module") != null)) {
              strModule = new StringMember("Module", (String) hm.get("module"), 10, "", 'T', false);
            } else {
              strModule = new StringMember("Module", "", 10, "", 'T', false);
            }
            StringMember strRecord = null;
            if ((hm.get("record") != null)) {
              strRecord = new StringMember("Record", (String) hm.get("record"), 10, "", 'T', false);
            } else {
              strRecord = new StringMember("Record", "", 10, "", 'T', false);
            }
            CustomFieldListElement cflistelement = new CustomFieldListElement(custfieldID);
            cflistelement.put("CustomFieldID", intCustFieldID);
            cflistelement.put("Name", strName);
            cflistelement.put("Type", strType);
            cflistelement.put("Module", strModule);
            cflistelement.put("Record", strRecord);
            StringBuffer stringbuffer = new StringBuffer("00000000000");
            stringbuffer.setLength(11);
            String s3 = (new Integer(i)).toString();
            stringbuffer.replace(stringbuffer.length() - s3.length(), stringbuffer.length(), s3);
            String s4 = stringbuffer.toString();
            cfList.put(s4, cflistelement);
          } catch (Exception e) {
            logger.error("[getCustomFieldList]: Exception", e);
          }
        }
      }
      cfList.setTotalNoOfRecords(cfList.size());
      cfList.setListType("CustomFields");
      cfList.setBeginIndex(beginindex);
      cfList.setEndIndex(endindex);
    } finally {
      cvdl.destroy();
    }
    return cfList;
  }

  public void addNewCustomField(CustomFieldVO customFieldVO)
  {
    String fieldType = customFieldVO.getFieldType();
    String label = customFieldVO.getLabel();
    int recordTypeID = customFieldVO.getRecordTypeID();
    String recordType = customFieldVO.getRecordType();

    CVDal cvdl = new CVDal(dataSource);
    int customFieldID = 0;

    try {
      if (fieldType.equals(CustomFieldVO.SCALAR)) {
        cvdl.clearParameters();
        cvdl.setSql("admin.newcustomfield");

        cvdl.setString(1, label);
        cvdl.setString(2, fieldType);
        cvdl.setInt(3, recordTypeID);
        cvdl.executeUpdate();

        customFieldID = cvdl.getAutoGeneratedKey();
        cvdl.clearParameters();
      } // end of if statement (fieldType.equals(CustomFieldVO.SCALAR))
      else if (fieldType.equals(CustomFieldVO.MULTIPLE)) {
        cvdl.clearParameters();
        cvdl.setSql("admin.newcustomfield");

        cvdl.setString(1, label);
        cvdl.setString(2, fieldType);
        cvdl.setInt(3, recordTypeID);
        cvdl.executeUpdate();

        customFieldID = cvdl.getAutoGeneratedKey();
        cvdl.clearParameters();

        cvdl.setSql("admin.customfieldvalues");

        Vector vecFieldValues = customFieldVO.getOptionValues();

        for (int i = 0; i < vecFieldValues.size(); i++) {
          String fieldValue = (String) vecFieldValues.elementAt(i);
          cvdl.setInt(1, customFieldID);
          cvdl.setString(2, fieldValue);

          cvdl.executeUpdate();
        } // end of for loop (int i = 0; i < vecFieldValues.size(); i++)
      } // end of else if statement (fieldType.equals(CustomFieldVO.MULTIPLE))

      this.insertBlankRecordsForNewCustomField(customFieldID, recordTypeID, recordType, fieldType);
    } // end of try block
    catch (Exception e) {
      logger.error("[addNewCustomField]: Exception", e);
    } finally {
      cvdl.destroy();
    }
  } // end of addNewCustomField method

  private void insertBlankRecordsForNewCustomField(int customFieldID, int tableID, String primaryTable, String customFieldType)
  {
    // This is one messy method.
    CVDal cvdl = new CVDal(this.dataSource);
    try {
      // Get the module information for the record type.
      // select * from cvtable where tableid = 2 AND name = 'individual';
      cvdl.setSqlQuery("SELECT moduleid FROM cvtable WHERE tableid = " + tableID + " AND name = '" + primaryTable + "'");
      Collection cvTableResults = cvdl.executeQuery();
      cvdl.clearParameters();
      if (cvTableResults != null) {
        Iterator cvTableIterator = cvTableResults.iterator();
        if (cvTableIterator.hasNext()) {
          HashMap thisResult = (HashMap) cvTableIterator.next();
          Number moduleID = (Number) thisResult.get("moduleid");

          cvdl.setSqlQuery("SELECT primarykeyfield FROM `module` WHERE (moduleid = " + moduleID.intValue() + " OR parentid = " + moduleID.intValue()
              + ") AND primarytable = '" + primaryTable + "'");

          Collection moduleResults = cvdl.executeQuery();
          cvdl.clearParameters();
          if (moduleResults != null) {
            Iterator moduleIterator = moduleResults.iterator();
            if (moduleIterator.hasNext()) {
              HashMap moduleResultHM = (HashMap) moduleIterator.next();
              String primaryKeyField = (String) moduleResultHM.get("primarykeyfield");

              cvdl.setSqlQuery("SELECT " + primaryKeyField + " FROM " + primaryTable);
              Collection recordResults = cvdl.executeQuery();
              cvdl.clearParameters();
              if (recordResults != null) {
                Iterator recordIterator = recordResults.iterator();
                while (recordIterator.hasNext()) {
                  HashMap recordHashMap = (HashMap) recordIterator.next();
                  Number primaryKeyValue = (Number) recordHashMap.get(primaryKeyField);

                  // Now insert the blank record.
                  if (customFieldType.equals(CustomFieldVO.SCALAR)) {
                    cvdl.setSqlQuery("INSERT INTO customfieldscalar (CustomFieldID, RecordID, Value) " + " VALUES (" + customFieldID + ", "
                        + primaryKeyValue.intValue() + ", '')");
                    cvdl.executeUpdate();
                    cvdl.clearParameters();
                  } // end of if statement
                    // (customFieldType.equals(CustomFieldVO.SCALAR))
                  else if (customFieldType.equals(CustomFieldVO.MULTIPLE)) {
                    cvdl.setSqlQuery("INSERT INTO customfieldmultiple (CustomFieldID, RecordID, ValueID) " + " VALUES (" + customFieldID + ", "
                        + primaryKeyValue.intValue() + ", 0)");
                    cvdl.executeUpdate();
                    cvdl.clearParameters();
                  } // end of else if statement
                    // (customFieldType.equals(CustomFieldVO.MULTIPLE))
                } // end of while loop (recordIterator.hasNext())
              } // end of if statement (recordResults != null)
            } // end of if statement (moduleIterator.hasNext())
          } // end of if statement (moduleResults != null)
        } // end of if statement (cvTableIterator.hasNext())
      } // end of if statement (cvTableResults != null)
    } // end of try block
    catch (Exception e) {
      logger.error("[insertBlankRecordsForNewCustomField]: Exception", e);
    } // end of catch block (Exception)
    finally {
      cvdl.destroy();
      cvdl = null;
    } // end of finally block
  } // end of insertBlankRecordsForNewCustomField

  public HashMap getCustomFieldValue()
  {
    CVDal cvdl = new CVDal(dataSource);
    HashMap vecFieldValues = new HashMap();
    try {
      cvdl.setSql("admin.getCustomFieldValues");
      Collection valueList = cvdl.executeQuery();
      if (valueList.size() > 0) {
        Iterator valueIter = valueList.iterator();
        while (valueIter.hasNext()) {
          HashMap hmValue = (HashMap) valueIter.next();
          String value = (String) hmValue.get("value");
          int valueid = ((Long) hmValue.get("ValueID")).intValue();
          int fieldid = ((Long) hmValue.get("CustomFieldID")).intValue();
          String name = (String) hmValue.get("name");
          vecFieldValues.put(value + "*" + name, valueid + "*" + fieldid);
        }
      }
    } catch (Exception e) {
      logger.error("[getCustomFieldValue]: Exception", e);
    } finally {
      cvdl.destroy();
    }
    return vecFieldValues;
  }

  public void addCustomFieldValue(int customFieldID, String fieldValue)
  {
    CVDal cvdl = new CVDal(dataSource);

    try {
      cvdl.setSql("admin.customfieldvalues");
      cvdl.setInt(1, customFieldID);
      cvdl.setString(2, fieldValue);
      cvdl.executeUpdate();
    } catch (Exception e) {
      logger.error("[addCustomFieldValue]: Exception", e);
    } finally {
      cvdl.destroy();
    }
  }

  // update custom field
  public void updateNewCustomField(CustomFieldVO cfData)
  {

    CVDal cvdl = new CVDal(dataSource);
    try {

      if ((cfData.getFieldType()).equals(CustomFieldVO.SCALAR)) {

        cvdl.clearParameters();
        cvdl.setSql("admin.updatecustomfield");

        cvdl.setString(1, cfData.getLabel());
        cvdl.setString(2, cfData.getFieldType());
        cvdl.setInt(3, cfData.getRecordTypeID());

        cvdl.setInt(4, cfData.getFieldID());

        cvdl.executeUpdate();
        cvdl.clearParameters();
      } else if ((cfData.getFieldType()).equals(CustomFieldVO.MULTIPLE)) {

        cvdl.clearParameters();
        cvdl.setSql("admin.updatecustomfield");

        cvdl.setString(1, cfData.getLabel());
        cvdl.setString(2, cfData.getFieldType());
        cvdl.setInt(3, cfData.getRecordTypeID());
        cvdl.setInt(4, cfData.getFieldID());

        cvdl.executeUpdate();
        cvdl.clearParameters();

        cvdl.setSql("admin.getCustomFieldvalues");
        cvdl.setInt(1, cfData.getFieldID());
        Collection customFieldValues = cvdl.executeQuery();
        Vector customFieldValueIds = new Vector();
        if (customFieldValues != null) {

          Iterator customVieldValuesIt = customFieldValues.iterator();
          while (customVieldValuesIt.hasNext()) {
            HashMap hm = (HashMap) customVieldValuesIt.next();
            customFieldValueIds.addElement(hm.get("ValueID"));
          }
        }
        cvdl.clearParameters();

        Vector vecFieldValues = cfData.getOptionValues();
        HashMap opValIds = cfData.getOptionValuesIds();

        for (int i = 0; i < vecFieldValues.size(); i++) {

          String fieldValue = (String) vecFieldValues.elementAt(i);
          if (opValIds.get(fieldValue) != null) {

            cvdl.setSql("admin.updatecustomfieldvalues");
            cvdl.setInt(1, cfData.getFieldID());
            cvdl.setString(2, fieldValue);
            cvdl.setInt(3, (new Integer((String) opValIds.get(fieldValue))).intValue());
            cvdl.executeUpdate();
            cvdl.clearParameters();

            customFieldValueIds.remove(new Long((String) opValIds.get(fieldValue)));
          } else {

            cvdl.setSql("admin.customfieldvalues");
            cvdl.setInt(1, cfData.getFieldID());
            cvdl.setString(2, fieldValue);
            cvdl.executeUpdate();
            cvdl.clearParameters();
          }
        }
        cvdl.clearParameters();

        if (customFieldValueIds.size() > 0) {

          Iterator customFieldValueIdsIterator = customFieldValueIds.iterator();
          String deleteQuery = "delete from customfieldvalue where ";
          int g = 0;
          while (customFieldValueIdsIterator.hasNext()) {

            if (g > 0) {

              deleteQuery += " or ";
            }
            deleteQuery += " valueid=" + customFieldValueIdsIterator.next();
            g++;
          }

          cvdl.setSqlQuery(deleteQuery);
          cvdl.executeUpdate();
          cvdl.clearParameters();
        }
      }
    } catch (Exception e) {
      logger.error("[updateNewCustomField]: Exception", e);
    } finally {
      cvdl.destroy();
    }
  }

  public void deleteCustomField(int userID, int customFieldID)
  {
    CVDal cvdl = new CVDal(dataSource);

    try {
      CustomFieldVO cfData = getCustomField(customFieldID);
      if ((cfData.getFieldType()).equals(CustomFieldVO.SCALAR)) {
        cvdl.setSql("admin.deletecustomfield");
        cvdl.setInt(1, cfData.getFieldID());
        cvdl.executeUpdate();
        cvdl.clearParameters();
      } else if ((cfData.getFieldType()).equals(CustomFieldVO.MULTIPLE)) {
        cvdl.setSql("admin.deletecustomfieldvalues");
        cvdl.setInt(1, cfData.getFieldID());
        cvdl.executeUpdate();
        cvdl.clearParameters();
        cvdl.setSql("admin.deletecustomfield");
        cvdl.setInt(1, cfData.getFieldID());
        cvdl.executeUpdate();
        cvdl.clearParameters();
      }
    } catch (Exception e) {
      logger.error("[deleteCustomField]: Exception", e);
    } finally {
      cvdl.destroy();
    }
  }

  public CustomFieldVO getCustomField(int customFieldID)
  {
    // ALLSQL.put( "admin.getCustomField","select name as name,fieldtype as
    // fieldtype,recordtype as recordtype from customfield where customfieldid=?
    // ");
    // ALLSQL.put( "admin.getCustomFieldvalues","select value as value from
    // customfieldvalue where customfieldid=? ");
    CVDal cvdl = new CVDal(dataSource);
    CustomFieldVO cfVO = new CustomFieldVO();

    try {

      cvdl.setSql("admin.getCustomField");
      cvdl.setInt(1, customFieldID);

      Collection colList = cvdl.executeQuery();

      cvdl.clearParameters();
      if ((colList != null) && (colList.size() > 0)) {

        Iterator iter = colList.iterator();

        while (iter.hasNext()) {

          HashMap hm = (HashMap) iter.next();

          cfVO.setFieldID(customFieldID);
          if (hm.get("fieldtype") != null) {

            cfVO.setFieldType((String) hm.get("fieldtype"));

            Vector vecFieldValues = new Vector();
            HashMap vecFieldValueIds = new HashMap();
            if (((String) hm.get("fieldtype")).equals(CustomFieldVO.MULTIPLE)) {

              cvdl.setSql("admin.getCustomFieldvalues");
              cvdl.setInt(1, customFieldID);

              Collection valueList = cvdl.executeQuery();
              if ((valueList != null) && (valueList.size() > 0)) {

                Iterator valueIter = valueList.iterator();
                while (valueIter.hasNext()) {

                  HashMap hmValue = (HashMap) valueIter.next();

                  vecFieldValues.addElement(hmValue.get("value"));
                  vecFieldValueIds.put(hmValue.get("value"), hmValue.get("ValueID"));
                }
              }
            }
            cfVO.setOptionValues(vecFieldValues);
            cfVO.setOptionValuesIds(vecFieldValueIds);
          }
          if (hm.get("name") != null) {

            cfVO.setLabel((String) hm.get("name"));
          }
          if (hm.get("recordtype") != null) {

            cfVO.setRecordTypeID(((Long) hm.get("recordtype")).intValue());
          }

        }
      }
    } catch (Exception e) {
      logger.error("[getCustomField]: Exception", e);
    } finally {
      cvdl.destroy();
    }

    return cfVO;
  }

  /**
   * Set the associated session context. The container calls this method after
   * the instance creation. The enterprise Bean instance should store the
   * reference to the context object in an instance variable. This method is
   * called with no transaction context.
   */
  public void setSessionContext(SessionContext ctx)
  {
    this.ctx = ctx;
  }

  /**
   * Called by the container to create a session bean instance. Its parameters
   * typically contain the information the client uses to customize the bean
   * instance for its use. It requires a matching pair in the bean class and its
   * home interface.
   */
  public void ejbCreate()
  {}

  /**
   * A container invokes this method before it ends the life of the session
   * object. This happens as a result of a client's invoking a remove operation,
   * or when a container decides to terminate the session object after a
   * timeout. This method is called with no transaction context.
   */
  public void ejbRemove()
  {}

  /**
   * The activate method is called when the instance is activated from its
   * 'passive' state. The instance should acquire any resource that it has
   * released earlier in the ejbPassivate() method. This method is called with
   * no transaction context.
   */
  public void ejbActivate()
  {}

  /**
   * The passivate method is called before the instance enters the 'passive'
   * state. The instance should release any resources that it can re-acquire
   * later in the ejbActivate() method. After the passivate method completes,
   * the instance must be in a state that allows the container to use the Java
   * Serialization protocol to externalize and store away the instance's state.
   * This method is called with no transaction context.
   */
  public void ejbPassivate()
  {}

  /**
   * @author Kevin McAllister <kevin@centraview.com> This simply sets the target
   *         datasource to be used for DB interaction
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  }

  public ValueListVO getCustomFieldsValueList(int individualId, ValueListParameters parameters)
  {
    ArrayList list = new ArrayList();
    String filter = parameters.getFilter();
    CVDal cvdl = new CVDal(this.dataSource);

    String query = this.buildCustomFieldsListQuery(parameters);
    String str = "CREATE TABLE customfieldslistfilter " + query;
    cvdl.setSqlQuery(str);
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();

    StringBuffer selectQuery = new StringBuffer();
    if (filter != null && filter.length() > 0) {
      selectQuery.append(" WHERE CustomFieldID Like '" + filter + "' OR Name ");
      selectQuery.append(" Like '%" + filter + "%' OR Type Like '%" + filter + "%' ");
      selectQuery.append(" OR Record Like '%" + filter + "%' OR Module Like '%" + filter + "%'");
    }
    cvdl.setSqlQuery("SELECT * FROM customfieldslistfilter " + selectQuery.toString());
    list = cvdl.executeQueryList(1);
    cvdl.setSqlQueryToNull();

    // We know that customfieldslistfilter table is having all custom field.
    cvdl.setSqlQuery("SELECT count(CustomFieldID) AS numberOfRecords FROM customfieldslistfilter " + selectQuery.toString());
    Collection results = cvdl.executeQuery();
    cvdl.clearParameters();
    cvdl.setSqlQueryToNull();
    int numberOfRecords = 0;
    if (results != null && results.size() > 0) {
      Iterator iter = results.iterator();
      while (iter.hasNext()) {
        HashMap row = (HashMap) iter.next();
        numberOfRecords = ((Number) row.get("numberOfRecords")).intValue();
      }
    }
    parameters.setTotalRecords(numberOfRecords);

    cvdl.setSqlQuery("DROP TABLE customfieldslistfilter");
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();
    cvdl.destroy();
    cvdl = null;
    return new ValueListVO(list, parameters);
  }

  private String buildCustomFieldsListQuery(ValueListParameters parameters)
  {
    StringBuffer query = new StringBuffer();
    query.append(" SELECT cf.CustomFieldID, cf.Name, cf.FieldType as Type, dmd.name as Module, ");
    query.append(" cvt.name as Record ");
    query.append(" FROM customfield cf, cvtable cvt, module cmd, module dmd ");
    query.append(" WHERE cf.RecordType = cvt.tableid and cvt.moduleid = cmd.moduleid");
    query.append(" and cmd.parentid = dmd.moduleid and dmd.name = '" + parameters.getExtraString() + "' ");
    query.append(" UNION SELECT cf.CustomFieldID, cf.Name, cf.FieldType as Type, cmd.name as Module, ");
    query.append(" cvt.name as Record FROM customfield cf, cvtable cvt, module cmd");
    query.append(" WHERE cf.RecordType = cvt.tableid and cvt.moduleid = cmd.moduleid ");
    query.append(" and ISNULL(cmd.parentid) and cmd.name ='" + parameters.getExtraString() + "' ");
    String orderBy = "ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
    String limit = parameters.getLimitParam();
    query.append(orderBy);
    query.append(limit);
    return query.toString();
  }

  public HashMap getFieldModuleInfo(int individualId, int customFieldId)
  {
    CVDal cvdal = new CVDal(dataSource);
    HashMap fieldInfo = new HashMap();

    try {
      cvdal
          .setSqlQuery("SELECT m.name AS recordType, m2.name AS moduleName FROM customfield cf LEFT OUTER JOIN cvtable t ON (cf.RecordType = t.tableid) LEFT OUTER JOIN module m ON (t.moduleid = m.moduleid) LEFT OUTER JOIN module m2 ON (m.parentid = m2.moduleid) WHERE cf.CustomFieldID=?");
      cvdal.setInt(1, customFieldId);
      Collection results = cvdal.executeQuery();
      if (results != null && results.size() > 0) {
        Iterator iter = results.iterator();
        while (iter.hasNext()) {
          HashMap row = (HashMap) iter.next();
          fieldInfo = row;
        }
      }
    } finally {
      cvdal.destroy();
      cvdal = null;
    }
    return fieldInfo;
  } // end getFieldModuleInfo() method

}
