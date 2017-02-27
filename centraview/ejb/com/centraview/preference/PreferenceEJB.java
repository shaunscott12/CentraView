/*
 * $RCSfile: PreferenceEJB.java,v $    $Revision: 1.5 $  $Date: 2005/09/01 15:31:05 $ - $Author: mcallist $
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

package com.centraview.preference;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

import com.centraview.common.CVDal;
import com.centraview.common.Constants;
import com.centraview.common.DDNameValue;
import com.centraview.common.ListPreference;
import com.centraview.common.ListView;
import com.centraview.common.UserPrefererences;

/**
 * This is the EJB class for Preference module -- Preferences The Logic for
 * methods defined in Remote interface is defined in this class
 */
public class PreferenceEJB implements SessionBean {
  protected SessionContext ctx;

  private String dataSource = "";

  private static Logger logger = Logger.getLogger(PreferenceEJB.class);

  public void setSessionContext(SessionContext ctx) {
    this.ctx = ctx;
  }

  public void ejbCreate() {}

  public void ejbRemove() {}

  public void ejbActivate() {}

  public void ejbPassivate() {}

  /**
   * This method gets the details of the user preferences Preferences will have
   * only get and update methods. No new entity can be added.
   * 
   * @param individualId
   * @return
   * @exception Exception
   */
  public UserPrefererences getUserPreferences(int individualId) {
    UserPrefererences up = new UserPrefererences();
    CVDal cvdl = new CVDal(dataSource);
    try {
      cvdl.setSql("preference.getpreference");
      cvdl.setInt(1, individualId);
      Collection col = cvdl.executeQuery();
      if (col == null) {
        throw new Exception("User preference not found");
      }
      Iterator it = col.iterator();
      while (it.hasNext()) {
        HashMap hm = (HashMap) it.next();
        String preferencename = (String) hm.get("preference_name");
        if (preferencename != null) {
          if (preferencename.equals("acknowledgeddays")) {
            String strlng = (String) hm.get("preference_value");
            up.setAcknowledgedDays(Integer.parseInt(strlng));
          }
          if (preferencename.equals("acknowledgedvisability")) {
            up.setAcknowledgedVisibility((String) hm.get("preference_value"));
          }
          if (preferencename.equals("activitytype")) {
            String stracttype = (String) hm.get("preference_value");
            up.setActivityPortletAccountType(Integer.parseInt(stracttype));
          }
          if (preferencename.equals("emailvisability")) {
            up.setEmailVisibility((String) hm.get("preference_value"));
          }
          if (preferencename.equals("opportunityvisible")) {
            boolean isVisible = false;
            if (((String) hm.get("preference_value")).equalsIgnoreCase("Yes")) {
              isVisible = true;
            }
            up.setOpportunityVisible(isVisible);
          }
          if (preferencename.equals("taskvisible")) {
            boolean isVisible = false;
            if (((String) hm.get("preference_value")).equalsIgnoreCase("Yes")) {
              isVisible = true;
            }
            up.setTaskVisible(isVisible);
          }
          if (preferencename.equals("supportvisible")) {
            boolean isVisible = false;
            if (((String) hm.get("preference_value")).equalsIgnoreCase("Yes")) {
              isVisible = true;
            }
            up.setSupportVisible(isVisible);
          }

          if (preferencename.equals("emailaccountid")) {
            String strepaid = (String) hm.get("preference_value");
            try {
              up.setEmailPortletAccountID(Integer.parseInt(strepaid));
            } catch (NumberFormatException nfe) {
              // "I get paid, to do the wild thing." -Tone Loc
            }
          }
          if (preferencename.equals("calactivitytype")) {
            String strlngObj = (String) hm.get("preference_value");
            up.setCalenderPortletActivityType(Integer.parseInt(strlngObj));
          }
          if (preferencename.equals("newsdays")) {
            String strnd = (String) hm.get("preference_value");
            up.setNewPortletDays(Integer.parseInt(strnd));
          }

          if (individualId != 0) {
            up.setIndividualID(individualId);
          }

          if (preferencename.equals("caldefaultview")) {
            up.setCalendarDefaultView((String) hm.get("preference_value"));
          }

          if (preferencename.equals("calendarrefreshmin")) {
            up.setCalendarRefreshMin((String) hm.get("preference_value"));
          }

          if (preferencename.equals("calendarrefreshsec")) {
            up.setCalendarRefreshSec((String) hm.get("preference_value"));
          }

          if (preferencename.equals("email")) {
            up.setEmail((String) hm.get("preference_value"));
          }

          if (preferencename.equals("todayscalendar")) {
            up.setTodaysCalendar((String) hm.get("preference_value"));
          }

          if (preferencename.equals("unscheduledactivities")) {
            up.setUnscheduledActivities((String) hm.get("preference_value"));
          }

          if (preferencename.equals("scheduledopportunities")) {
            up.setScheduledOpportunities((String) hm.get("preference_value"));
          }

          if (preferencename.equals("projecttasks")) {
            up.setProjectTasks((String) hm.get("preference_value"));
          }

          if (preferencename.equals("supporttickets")) {
            up.setSupportTickets((String) hm.get("preference_value"));
          }

          if (preferencename.equals("companynews")) {
            up.setCompanyNews((String) hm.get("preference_value"));
          }

          if (preferencename.equals("DEFAULTFOLDERID")) {
            up.setDefaultFolderID(Integer.parseInt((String) hm.get("preference_value")));
          }

          if (preferencename.equals("TIMEZONE")) {
            up.setTimeZone((String) hm.get("preference_value"));
          }

          if (preferencename.equals("DATEFORMAT")) {
            up.setDateFormat((String) hm.get("preference_value"));
          }

          if (preferencename.equals("contenttype")) {
            up.setContentType((String) hm.get("preference_value"));
          }

          if (preferencename.equals("allRecordsPublic")) {
            up.setAllRecordsPublic((String) hm.get("preference_value"));
          }

          if (preferencename.equals("syncAsPrivate")) {
            up.setSyncAsPrivate((String) hm.get("preference_value"));
          }

          if (preferencename.equals("emailCheckInterval")) {
            // default to 10 if no valid value is found
            int interval = 10;
            try {
              interval = Integer.parseInt((String) hm.get("preference_value"));
            } catch (NumberFormatException nfe) { /* default already set */}
            up.setEmailCheckInterval(interval);
          }

          if (preferencename.equals("homesettingrefreshmin")) {
            up.setHomeRefreshMin((String) hm.get("preference_value"));
          }

          if (preferencename.equals("homesettingrefreshsec")) {
            up.setHomeRefreshSec((String) hm.get("preference_value"));
          }
          if (preferencename.equals("showticketcharts")) {
            boolean isVisible = false;
            if (((String) hm.get("preference_value")).equalsIgnoreCase("Yes")) {
              isVisible = true;
            }
            up.setShowTicketCharts(isVisible);
          }
          if (preferencename.equals("showsalescharts")) {
            boolean isVisible = false;
            if (((String) hm.get("preference_value")).equalsIgnoreCase("Yes")) {
              isVisible = true;
            }
            up.setShowSalesCharts(isVisible);
          }

        } // end if (preferencename != null)
      } // end while (it.hasNext())

      // Get all the views.
      ListPreference listPreference = null;
      ListView listView = null;
      // Step One: we will get all the Default Views for the Individual, who is
      // logged in.
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("SELECT listviews.viewid, listviews.listtype, listviews.sorttype, listviews.sortmember AS sortelement, listviews.noofrecords AS recordsperpage, listviews.viewname, listviews.searchid FROM listviews, defaultviews WHERE listviews.listtype = defaultviews.listtype AND listviews.viewid = defaultviews.viewid");
      Collection ownerViewCollection = cvdl.executeQuery();
      Iterator ownerViewIterator = ownerViewCollection.iterator();
      while (ownerViewIterator.hasNext()) {
        HashMap ownerViewHM = (HashMap) ownerViewIterator.next();
        String listType = (String) ownerViewHM.get("listtype");
        int viewId = ((Number) ownerViewHM.get("viewid")).intValue();
        listPreference = new ListPreference(listType);
        // Step Two:
        // We must get all the Views which exist for each ListType from the
        // query for default views.
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("SELECT viewid, viewname, searchid FROM listviews WHERE listtype = ? and ownerid IS NULL UNION SELECT viewid, viewname, searchid FROM listviews WHERE listtype = ? and ownerid = ?");
        cvdl.setString(1, listType);
        cvdl.setString(2, listType);
        cvdl.setInt(3, individualId);
        Collection allViewCollection = cvdl.executeQuery();
        Iterator allViewIterator = allViewCollection.iterator();
        while (allViewIterator.hasNext()) {
          HashMap allViewHM = (HashMap) allViewIterator.next();
          int allViewId = ((Number) allViewHM.get("viewid")).intValue();
          listView = new ListView(allViewId);
          listView.setListType(listType);
          listView.setViewName((String) allViewHM.get("viewname"));
          listView.setViewID(allViewId);
          listView.setSearchID(((Number) allViewHM.get("searchid")).intValue());
          // Get all columns for the View. Which we are processing.
          cvdl.setSqlQueryToNull();
          cvdl.setSql("view.getownerallcolumn");
          cvdl.setInt(1, allViewId);
          cvdl.setString(2, listType);
          Collection viewColumnCollection = cvdl.executeQuery();
          Iterator viewColumnIterator = viewColumnCollection.iterator();
          while (viewColumnIterator.hasNext()) {
            HashMap viewColumnHM = (HashMap) viewColumnIterator.next();
            listView.addColumnName((String) viewColumnHM.get("columnname"));
          }// end of while (viewColumnIterator.hasNext())
          listPreference.addListView(listView);
        }// end of while (allViewIterator.hasNext())
        listPreference.setDefaultView(viewId);
        listPreference.setRecordsPerPage(((Long) ownerViewHM.get("recordsperpage")).intValue());
        listPreference.setSortElement((String) ownerViewHM.get("sortelement"));
        String sortType = (String) ownerViewHM.get("sorttype");
        boolean sortTypeBoolean = true;
        if (sortType.equals("A")) {
          sortTypeBoolean = true;
        } else {
          sortTypeBoolean = false;
        }
        listPreference.setSortOrder(sortTypeBoolean);
        up.addListPreferences(listPreference);
      } // end of while (ownerViewIterator.hasNext())
    } catch (Exception e) {
      logger.error("[getUserPreferences] Exception thrown.", e);
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
    return up;
  } // end getUserPreferences(int) method

  /**
   * update Preference using UserPreference Preferences will have only get and
   * update methods. No new entity can be added.
   * 
   * @param userId
   * @param preferenceVO
   */
  public void updateUserPreference(int individualId, Vector vecPref) {
    CVDal cvdl = new CVDal(this.dataSource);
    try {
      cvdl.setSql("preference.updatepreference");

      for (int i = 0; i < vecPref.size(); i++) {
        PreferenceVO prefVO = (PreferenceVO) vecPref.elementAt(i);
        cvdl.setString(1, prefVO.getPreferenceValue());
        cvdl.setString(2, prefVO.getTag());
        cvdl.setInt(3, individualId);
        cvdl.setString(4, prefVO.getPreferenceName());
        cvdl.executeUpdate();
      }
    } catch (Exception e) {
      System.out.println("[Exception][PreferenceEJB.updateUserPreference] Exception Thrown: " + e);
      // e.printStackTrace();
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
  } // end updateUserPreference(int,Vector) method

  public Vector getDelegatorIDs(int userID, String moduleName, String typeofoperation) {
    Collection col = null;
    Vector vec = new Vector();

    CVDal dl = new CVDal(this.dataSource);

    try {
      dl.setSql("delegation.getdelegators");
      dl.setInt(1, userID);
      dl.setString(2, moduleName);

      col = dl.executeQuery();

      int indID = 0;
      String rights = "";

      if (col != null) {
        Iterator it = col.iterator();
        while (it.hasNext()) {
          HashMap hm = (HashMap) it.next();
          indID = ((Number) hm.get("fromuser")).intValue();
          rights = (String) hm.get("rights");

          if (moduleName.equals(Constants.ACTIVITYMODULE)) {
            if (rights.equals(typeofoperation)) {
              vec.add(new Long(indID));
            }
          } else if (moduleName.equals(Constants.EMAILMODULE)) {
            if (rights.equals(typeofoperation)) {
              vec.add(String.valueOf(indID));
            }
          }
        } // end while (it.hasNext())
      } // end if (col != null)
    } catch (Exception e) {
      System.out.println("Error while getting delegator  ids");
      e.printStackTrace();
    } finally {
      dl.destroy();
      dl = null;
    }
    return vec;
  } // end getDelegatorIDs(int,String,String) method

  public Vector getCalendarDelegatorIds(int userID, String moduleName, String typeofoperation) {
    Collection col = null;
    Vector vec = new Vector();

    CVDal dl = new CVDal(this.dataSource);

    try {
      dl.setSql("delegation.getdelegators");
      dl.setInt(1, userID);
      dl.setString(2, moduleName);

      col = dl.executeQuery();

      int indID = 0;
      String rights = "";

      if (col != null) {
        Iterator it = col.iterator();
        while (it.hasNext()) {
          HashMap hm = (HashMap) it.next();
          indID = ((Number) hm.get("fromuser")).intValue();
          rights = (String) hm.get("rights");
          String name = (String) hm.get("Name");

          if (moduleName.equals(Constants.ACTIVITYMODULE)) {
            if (rights.equals(typeofoperation)) {
              DDNameValue userInformation = new DDNameValue(String.valueOf(indID), name);
              vec.add(userInformation);
            }
          } else if (moduleName.equals(Constants.EMAILMODULE)) {
            if (rights.equals(typeofoperation)) {
              vec.add(String.valueOf(indID));
            }
          }
        } // end while (it.hasNext())
      } // end if (col != null)
    } catch (Exception e) {
      System.out.println("Error while getting delegator  ids");
      e.printStackTrace();
    } finally {
      dl.destroy();
      dl = null;
    }
    return vec;
  } // end getCalendarDelegatorIds(int,String,String) method

  public HashMap getUserDelegation(int userID, String moduleName) {
    HashMap hmIndividual = new HashMap();
    Vector vecView = new Vector();
    Vector vecAll = new Vector();
    Vector vecSchedule = new Vector();

    CVDal dl = new CVDal(dataSource);

    try {
      dl.setSql("delegation.userdelegators");
      dl.setInt(1, userID);
      dl.setString(2, moduleName);

      Collection col = dl.executeQuery();

      Long indID = null;
      String rights = "";

      if (col != null) {
        Iterator it = col.iterator();
        while (it.hasNext()) {
          HashMap hm = (HashMap) it.next();
          indID = (Long) hm.get("touser");
          rights = (String) hm.get("rights");
          if (moduleName.equals(Constants.ACTIVITYMODULE)) {
            if (rights.equals(Constants.VIEW)) {
              vecView.addElement(new Integer(indID.intValue()));
            }
            if (rights.equals(Constants.SCHEDULEACTIVITY)) {
              vecSchedule.addElement(new Integer(indID.intValue()));
            }
          }
          if (vecView.contains(new Integer(indID.intValue())) && vecSchedule.contains(new Integer(indID.intValue()))) {
            vecAll.addElement(new Integer(indID.intValue()));
            vecView.removeElement(new Integer(indID.intValue()));
            vecSchedule.removeElement(new Integer(indID.intValue()));
          }
        } // end while (it.hasNext())
      } // end if (col != null)

      if (moduleName.equals(Constants.ACTIVITYMODULE)) {
        hmIndividual.put(Constants.VIEW, vecView);
        hmIndividual.put(Constants.SCHEDULEACTIVITY, vecSchedule);
        hmIndividual.put(Constants.VIEWSCHEDULEACTIVITY, vecAll);
      }
    } catch (Exception e) {
      System.out.println("[Exception][PreferenceEJB.getUserDelegation] Exception Thrown: " + e);
      e.printStackTrace();
    } finally {
      dl.destroy();
      dl = null;
    }
    return hmIndividual;
  } // end getUserDelegation(int,String) method

  /**
   * This method saves the information about the delegation
   */
  public void updateUserDelegation(int userID, String moduleName, HashMap hm) {
    Vector vecView = (Vector) hm.get(Constants.VIEW);
    Vector vecAll = (Vector) hm.get(Constants.VIEWSCHEDULEACTIVITY);
    Vector vecSchedule = (Vector) hm.get(Constants.SCHEDULEACTIVITY);

    CVDal dl = new CVDal(this.dataSource);

    try {
      dl.setSql("delegation.deleteuserdelegators");
      dl.setInt(1, userID);
      dl.setString(2, moduleName);
      dl.executeUpdate();

      dl.clearParameters();
      dl.setSqlQueryToNull();

      dl.setSql("delegation.getModule");
      dl.setString(1, moduleName);

      Collection col = dl.executeQuery();

      dl.clearParameters();
      dl.setSqlQueryToNull();

      int moduleID = 0;

      if (col != null && col.size() > 0) {
        Iterator iter = col.iterator();
        HashMap hmModule = (HashMap) iter.next();
        moduleID = ((Number) hmModule.get("moduleid")).intValue();
      }

      if (vecAll.size() > 0) {
        for (int i = 0; i < vecAll.size(); i++) {
          dl.setSql("delegation.insertuserdelegators");
          dl.setInt(1, userID);
          dl.setInt(2, ((Integer) vecAll.elementAt(i)).intValue());
          dl.setInt(3, moduleID);
          dl.setString(4, Constants.VIEW);
          dl.executeUpdate();
          dl.clearParameters();
          dl.setSqlQueryToNull();

          dl.setSql("delegation.insertuserdelegators");
          dl.setInt(1, userID);
          dl.setInt(2, ((Integer) vecAll.elementAt(i)).intValue());
          dl.setInt(3, moduleID);
          dl.setString(4, Constants.SCHEDULEACTIVITY);
          dl.executeUpdate();

          dl.clearParameters();
          dl.setSqlQueryToNull();
        }
      }
      if (vecView.size() > 0) {
        for (int i = 0; i < vecView.size(); i++) {
          dl.setSql("delegation.insertuserdelegators");
          dl.setInt(1, userID);
          dl.setInt(2, ((Integer) vecView.elementAt(i)).intValue());
          dl.setInt(3, moduleID);
          dl.setString(4, Constants.VIEW);
          dl.executeUpdate();

          dl.clearParameters();
          dl.setSqlQueryToNull();
        }
      }
      if (vecSchedule.size() > 0) {
        for (int i = 0; i < vecSchedule.size(); i++) {
          dl.setSql("delegation.insertuserdelegators");
          dl.setInt(1, userID);
          dl.setInt(2, ((Integer) vecSchedule.elementAt(i)).intValue());
          dl.setInt(3, moduleID);
          dl.setString(4, Constants.SCHEDULEACTIVITY);
          dl.executeUpdate();

          dl.clearParameters();
          dl.setSqlQueryToNull();
        }
      }
    } catch (Exception e) {
      System.out.println("[Exception][PreferenceEJB] updateUserDelegation(): " + e);
      e.printStackTrace();
    } finally {
      dl.destroy();
      dl = null;
    }
  } // end updateUserDelegation(int,String,HashMap) method

  /**
   * Updates the <code>preference_value</code> field in the
   * <code>userpreference</code> table for the record that matches the given
   * <code>individualID</code> and where <code>preference_name</code> is
   * "syncAsPrivate". The updated value is the given
   * <code>preferenceValue</code>.
   * 
   * @param individualID
   *          The individualID of the user whose preference we are updating.
   * @param preferenceValue
   *          The String that will be set as the value of preference_value.
   * @return int The number of records updated (should always be 1 or 0)
   */
  public int updateSyncAsPrivatePref(int individualID, String preferenceValue) {
    int recordsChanged = 0;
    CVDal cvdal = new CVDal(this.dataSource);
    try {
      // check preferenceValue - if null or not "YES" and not "NO",
      // then set to the default of "YES". Otherwise, use preferenceValue
      preferenceValue = (preferenceValue == null || (!preferenceValue.equals("YES") && !preferenceValue.equals("NO"))) ? "YES"
          : preferenceValue;

      cvdal
          .setSqlQuery("UPDATE userpreference SET preference_value=? WHERE preference_name='syncAsPrivate' AND individualID=?");
      cvdal.setString(1, preferenceValue);
      cvdal.setInt(2, individualID);
      recordsChanged = cvdal.executeUpdate();
    } finally {
      cvdal.destroy();
      cvdal = null;
    }
    return recordsChanged;
  } // end updateSyncAsPrivatePref(int,String) method

  /**
   * @author Kevin McAllister <kevin@centraview.com> This simply sets the target
   *         datasource to be used for DB interaction
   * @param ds
   *          A string that contains the cannonical JNDI name of the datasource
   */
  public void setDataSource(String ds) {
    this.dataSource = ds;
  }

  public Vector getEmailDelegation(int individualID) {
    Vector delegatorIDs = new Vector();
    CVDal dl = new CVDal(dataSource);

    try {
      dl.setSql("delegation.emaildelegators");
      dl.setInt(1, individualID);

      Collection col = dl.executeQuery();

      if (col != null) {
        Iterator it = col.iterator();
        while (it.hasNext()) {
          HashMap hm = (HashMap) it.next();
          Number indID = (Number) hm.get("delegatorID");
          delegatorIDs.addElement(new Integer(indID.intValue()));
        }
      }
    } catch (Exception e) {
      System.out.println("[Exception][PreferenceEJB.getEmailDelegation] Exception Thrown: " + e);
      // e.printStackTrace();
    } finally {
      dl.destroy();
      dl = null;
    }
    return delegatorIDs;
  } // end getEmailDelegation(int) method

  public void updateEmailDelegation(int individualID, Vector delegatorIDs) {
    CVDal dl = new CVDal(this.dataSource);

    try {
      dl.setSql("delegation.deleteemaildelegators");
      dl.setInt(1, individualID);
      dl.executeUpdate();
      dl.clearParameters();
      dl.setSqlQueryToNull();

      for (int i = 0; i < delegatorIDs.size(); i++) {
        dl.setSql("delegation.insertemaildelegators");
        dl.setInt(1, individualID);
        dl.setInt(2, ((Integer) delegatorIDs.elementAt(i)).intValue());
        dl.executeUpdate();
        dl.clearParameters();
        dl.setSqlQueryToNull();
      }
    } catch (Exception e) {
      System.out.println("[Exception][PreferenceEJB] updateUserDelegation(): " + e);
      e.printStackTrace();
    } finally {
      dl.destroy();
      dl = null;
    }
  } // end updateEmailDelegation(int,Vector) method
} // end class definition

