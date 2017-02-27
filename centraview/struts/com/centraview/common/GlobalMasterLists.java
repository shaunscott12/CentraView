/*
 * $RCSfile: GlobalMasterLists.java,v $    $Revision: 1.4 $  $Date: 2005/10/24 21:11:21 $ - $Author: mcallist $
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TimeZone;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.centraview.account.helper.AccountHelper;
import com.centraview.account.helper.AccountHelperHome;
import com.centraview.activity.activityfacade.ActivityFacade;
import com.centraview.activity.activityfacade.ActivityFacadeHome;
import com.centraview.additionalmenu.AdditionalMenu;
import com.centraview.additionalmenu.AdditionalMenuHome;
import com.centraview.administration.applicationsettings.AppSettings;
import com.centraview.administration.applicationsettings.AppSettingsHome;
import com.centraview.common.helper.CommonHelper;
import com.centraview.common.helper.CommonHelperHome;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.sale.salefacade.SaleFacade;
import com.centraview.sale.salefacade.SaleFacadeHome;
import com.centraview.search.Search;
import com.centraview.search.SearchHome;
import com.centraview.support.supportfacade.SupportFacade;
import com.centraview.support.supportfacade.SupportFacadeHome;

/**
 * A singleton to cache some infrequently changed lists from the database. And
 * some stuff that isn't even in the database but is only hardcoded here. It
 * currently stores a separate instance of GlobalMasterLists per dataSource in a
 * hashmap.
 */
public class GlobalMasterLists extends HashMap {
  private static Logger logger = Logger.getLogger(GlobalMasterLists.class);
  private static HashMap listsMap = new HashMap();
  private GlobalMasterLists gml;
  private Vector supportStatusList;
  private Vector supportPriorityList;
  private Vector allSource;
  private Vector allSaleStatus;
  private Vector allSaleStage;
  private Vector allSaleType;
  private Vector allSaleProbability;
  private Vector allSaleTerm;
  private Vector additionalMenu;
  private String dataSource = null;

  private GlobalMasterLists(String dataSource) {
    this.dataSource = dataSource;
    try {
      ContactFacadeHome cfh = (ContactFacadeHome)CVUtility.getHomeObject(
          "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      ContactFacade remote = cfh.create();
      remote.setDataSource(this.dataSource);

      Vector stateColl = remote.getStates();
      Vector countColl = remote.getCountry();
      Vector mocColl = remote.getMOCType();
      Vector syncAsColl = remote.getSyncAs();

      this.put("States", stateColl);
      this.put("Country", countColl);
      this.put("MOC", mocColl);
      this.put("SyncAs", syncAsColl);

      Vector userColl = remote.getUsers();
      this.put("Users", userColl);

      Vector groupColl = remote.getGroups();
      this.put("Groups", groupColl);

      Vector activityColl = new Vector();
      activityColl.add(new DDNameValue(1, "All"));
      activityColl.add(new DDNameValue(2, "Appointments"));
      activityColl.add(new DDNameValue(3, "Calls"));
      activityColl.add(new DDNameValue(4, "Next Actions"));
      activityColl.add(new DDNameValue(5, "Opportunity Info"));
      activityColl.add(new DDNameValue(6, "Project Info"));
      activityColl.add(new DDNameValue(7, "Events"));
      activityColl.add(new DDNameValue(8, "Multiple Actitivy Types"));

      this.put("Activities", activityColl);

      Vector timeZoneColl = new Vector();
      Date today = new Date();
      // this is used to prevent duplicates in the list
      ArrayList descriptionList = new ArrayList();
      String[] zoneIds = TimeZone.getAvailableIDs();
      for (int i=0; i<zoneIds.length; i++) {
        TimeZone tz = TimeZone.getTimeZone(zoneIds[i]);
        String shortName = tz.getDisplayName(tz.inDaylightTime(today), TimeZone.SHORT);
        String longName = tz.getDisplayName(tz.inDaylightTime(today), TimeZone.LONG);
        int offset = tz.getOffset(today.getTime());
        int hour = offset / (60*60*1000);
        int min = Math.abs(offset / (60*1000)) % 60;
        StringBuffer description = new StringBuffer();
        description.append(shortName+": ");
        description.append(longName);
        description.append(" (GMT");
        if(offset >= 0) {
          description.append("+");
        }
        description.append(hour);
        description.append(":");
        DecimalFormat df = new DecimalFormat("00");
        description.append(df.format(min));
        description.append(")");
        if (!descriptionList.contains(description)) {
          timeZoneColl.add(new DDNameValue(zoneIds[i], description.toString()));
          descriptionList.add(description);
        }
      }
      this.put("TimeZone", timeZoneColl);

      Vector timeSpanColl = new Vector();
      timeSpanColl.add(new DDNameValue(1, "1 Hour"));
      timeSpanColl.add(new DDNameValue(2, "30 Minutes"));
      timeSpanColl.add(new DDNameValue(3, "15 Minutes"));

      this.put("TimeSpan", timeSpanColl);

      SearchHome sh = (SearchHome)CVUtility.getHomeObject("com.centraview.search.SearchHome",
          "Search");
      Search rmt = sh.create();
      rmt.setDataSource(this.dataSource);
      HashMap hmp = rmt.getTableIdsAndNames();
      this.put("Tables", hmp);

      setSupportLists();

      AccountHelperHome hm = (AccountHelperHome)CVUtility.getHomeObject(
          "com.centraview.account.helper.AccountHelperHome", "AccountHelper");
      AccountHelper accHelper = hm.create();
      accHelper.setDataSource(this.dataSource);

      Vector itemTypesVec = accHelper.getItemTypes();
      this.put("ItemTypes", itemTypesVec);

      Vector glAccountsVec = accHelper.getGLAccounts();
      this.put("GLAccounts", glAccountsVec);

      Vector accountingStatusVec = accHelper.getAccountingStatus();
      this.put("AccountingStatus", accountingStatusVec);

      Vector accountingTermsVec = accHelper.getAccountingTerms();
      this.put("AccountingTerms", accountingTermsVec);

      Vector paymentMethodsVec = accHelper.getPaymentMethods();
      this.put("PaymentMethods", paymentMethodsVec);

      Vector locationsVector = accHelper.getLocations();
      this.put("AccountingLocations", locationsVector);

      CommonHelperHome commonHelperHome = (CommonHelperHome)CVUtility.getHomeObject(
          "com.centraview.common.helper.CommonHelperHome", "CommonHelper");
      CommonHelper commonRemote = commonHelperHome.create();
      commonRemote.setDataSource(this.dataSource);

      allSource = commonRemote.getAllSource();
      this.put("AllSource", allSource);

      HashMap moduleList = commonRemote.getModuleList();
      this.put("moduleList", moduleList);

      setSupportLists();
      setSaleLists();

      Vector mocTypes = remote.getMOCType();
      if (mocTypes != null && mocTypes.size() > 0) {
        Iterator iter = mocTypes.iterator();
        while (iter.hasNext()) {
          DDNameValue row = (DDNameValue)iter.next();
          String name = row.getName();
          if (name != null && name.equals("Email")) {
            iter.remove();
          }
        }
      }
      this.put("mocTypeList", mocTypes);

      this.put("AllSaleStatus", allSaleStatus);
      this.put("AllSaleStage", allSaleStage);
      this.put("AllSaleType", allSaleType);
      this.put("AllSaleProbability", allSaleProbability);
      this.put("AllSaleTerm", allSaleTerm);

      additionalMenu = getAllAdditionalMenu(true);
    } catch (Exception e) {
      logger.error("[GlobalMasterLists] Exception thrown.", e);
    }
  }

  /**
   * This method removes the GML, referred to by dataSource, from the HashMap
   * then forces a refresh.
   */
  public static void refreshGlobalMasterList(String dataSource)
  {
    GlobalMasterLists.listsMap.remove(dataSource);
    GlobalMasterLists.getGlobalMasterLists(dataSource);
  } // end of refreshGlobalMasterList method

  /**
   * Retruns only one instance of this class mapped by the dataSource. There
   * actually can be many GlobalMasterLists one per each configured dataSource.
   */
  public synchronized static GlobalMasterLists getGlobalMasterLists(String dataSource)
  {
    GlobalMasterLists gml = (GlobalMasterLists)GlobalMasterLists.listsMap.get(dataSource);
    if (gml == null) {
      gml = new GlobalMasterLists(dataSource);
      GlobalMasterLists.listsMap.put(dataSource, gml);
    } // end of if statement (gml == null)
    return gml;
  } // end of getGlobalMasterLists method

  public Vector getAllResources()
  {
    Vector vector = null;

    try {
      ActivityFacadeHome activityfacadehome = (ActivityFacadeHome)CVUtility.getHomeObject(
          "com.centraview.activity.activityfacade.ActivityFacadeHome", "ActivityFacade");
      ActivityFacade activityfacade = activityfacadehome.create();
      activityfacade.setDataSource(this.dataSource);

      vector = activityfacade.getAllResources();
    } catch (Exception exception) {
      logger.error("[getAllResources] Exception thrown.", exception);
    }
    return vector;
  }

  /**
   * Get views info in combo box on list page
   */
  public Vector getViewComboData(ListPreference listPreference)
  {
    Vector viewVec = new Vector();
    try {
      HashMap listViewHM = listPreference.getViewHashMap();
      Set listViewSet = listViewHM.keySet();
      Iterator iterator = listViewSet.iterator();
      String viewName = "";
      DDNameValue ddNameValue = null;
      ListView listView = null;
      String viewIdStr = "0";
      while (iterator.hasNext()) {
        viewIdStr = iterator.next().toString();
        listView = (ListView)listViewHM.get(viewIdStr);
        viewName = listView.getViewName();
        ddNameValue = new DDNameValue(Integer.parseInt(viewIdStr), viewName);
        viewVec.add(ddNameValue);
      }
    } catch (Exception e) {
      logger.error("[getViewComboData] Exception thrown.", e);
    }
    return viewVec;
  }

  /**
   * returns Tax Classes in vector
   * @return Vector
   */
  public Vector getTaxClasses()
  {
    Vector taxClassesVec = new Vector();

    try {
      AccountHelperHome hm = (AccountHelperHome)CVUtility.getHomeObject(
          "com.centraview.account.helper.AccountHelperHome", "AccountHelper");
      AccountHelper remote = hm.create();
      remote.setDataSource(this.dataSource);
      taxClassesVec = remote.getTaxClasses();
    } catch (Exception e) {
      logger.error("[getTaxClasses] Exception thrown.", e);
    }
    return taxClassesVec;
  }

  public Vector getAllStatus()
  {
    return supportStatusList;
  }

  public Vector getAllPriorities()
  {
    return supportPriorityList;
  }

  private void setSupportLists()
  {
    try {
      SupportFacadeHome cfh = (SupportFacadeHome)CVUtility.getHomeObject(
          "com.centraview.support.supportfacade.SupportFacadeHome", "SupportFacade");
      SupportFacade remote = cfh.create();
      remote.setDataSource(this.dataSource);
      supportStatusList = remote.getStatusList();
      supportPriorityList = remote.getPriorityList();
    } catch (Exception e) {
      logger.error("[setSupportLists] Exception thrown.", e);
    }
  }

  private void setSaleLists()
  {
    try {
      SaleFacadeHome cfh = (SaleFacadeHome)CVUtility.getHomeObject(
          "com.centraview.sale.salefacade.SaleFacadeHome", "SaleFacade");
      SaleFacade remote = cfh.create();
      remote.setDataSource(this.dataSource);

      allSaleStatus = remote.getAllStatus();
      allSaleStage = remote.getAllStage();
      allSaleType = remote.getAllType();
      allSaleProbability = remote.getAllProbability();
      allSaleTerm = remote.getAllTerm();
    } catch (Exception e) {
      logger.error("[setSaleLists] Exception thrown.", e);
    }
  }

  public Vector getRecordType(String module)
  {
    Vector vec = new Vector();

    try {
      AppSettingsHome appHome = (AppSettingsHome)CVUtility.getHomeObject(
          "com.centraview.administration.applicationsettings.AppSettingsHome", "AppSettings");
      AppSettings appRemote = appHome.create();
      appRemote.setDataSource(this.dataSource);

      vec = appRemote.getRecordType(module);
    } catch (Exception e) {
      logger.error("[getRecordType] Exception thrown.", e);
    }
    return vec;
  }

  public Vector getAdditionalMenu()
  {
    return this.additionalMenu;
  }

  public void setAdditionalMenu(Vector additionalMenu)
  {
    this.additionalMenu = additionalMenu;
  }

  private Vector getAllAdditionalMenu(boolean isOrdered)
  {
    Vector vec = new Vector();

    try {
      AdditionalMenuHome home = (AdditionalMenuHome)CVUtility.getHomeObject(
          "com.centraview.additionalmenu.AdditionalMenuHome", "AdditionalMenu");
      AdditionalMenu remote = home.create();
      remote.setDataSource(this.dataSource);

      vec = remote.getAllAdditionalMenuItems(isOrdered);
    } catch (Exception e) {
      logger.error("[getAllAdditionalMenu] Exception thrown.", e);
    }
    return vec;
  }
} // end of GlobalMasterLists class
