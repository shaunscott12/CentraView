/*
 * $RCSfile: CVUtility.java,v $    $Revision: 1.8 $  $Date: 2005/10/24 14:51:52 $ - $Author: mcallist $
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
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.naming.CommunicationException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.authorization.Authorization;
import com.centraview.administration.authorization.AuthorizationHome;
import com.centraview.administration.authorization.AuthorizationLocal;
import com.centraview.administration.authorization.AuthorizationLocalHome;
import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.administration.history.HistoryLocal;
import com.centraview.administration.history.HistoryLocalHome;
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.ejb.EJBHomeCache;
import com.centraview.file.CvFile;
import com.centraview.file.CvFileHome;
import com.centraview.file.CvFolderVO;
import com.centraview.settings.Settings;

/**
 * This class provides common and reusable functions.
 */
public class CVUtility {
  public static final int PRIMARY_ADDRESS_TYPE = 1; // Temporary constant for
                                                    // address Type
  private static final String NAMING_FACTORY_KEY = "java.naming.factory.initial";
  private static final String NAMING_PROVIDER_KEY = "java.naming.provider.url";
  private static Logger logger = Logger.getLogger(CVUtility.class);
  private static InitialContext initialContext = null;

  /**
   * Checks the EJBHomeCache singleton for Object returns cached object if it
   * exists. Otherwise does the JNDI lookup caches and returns the results. Does
   * not work with local. eg: EntityHome aa =
   * (EntityHome)CVUtility.getHomeObject("com.centraview.contact.entity.EntityHome","Entity");
   * It would be nice in the future to use reflection to call create() and
   * setDataSource() on the resultant EJBHome. And then return it. That could
   * cut down on errors caused by not setting the dataSource name on the EJB.
   * And it would also simplify other code.
   * @param String a fully qualified home class name
   *          (com.centraview.contact.entity.EntityHome)
   * @param String Bean JNDI name
   * @return Object, the EJBHome object.
   * @throws CommunicationException
   * @throws NamingException, if a ClassNotFoundException occurs a
   *           NamingException will be thrown also.
   * @author Kevin McAllister <kevin@centraview.com>
   */
  public static Object getHomeObject(String homeClassName, String beanJndiName) throws CommunicationException, NamingException
  {
    EJBHomeCache homeCache = EJBHomeCache.getInstance();
    Class homeClass = null;
    Object homeObject = null;
    try {
      homeClass = Class.forName(homeClassName);
    } catch (ClassNotFoundException e) {
      logger.error("[getHomeObject] There are classpath/classloader issues.  I can't do Class.forName(" + homeClassName + ");", e);
      // This kinda sucks, because you are losing information.
      // but for now it wasn't worthwhile for me to go through and make sure
      // ClassNotFoundException was also handled in some way by all the calling
      // classes.
      throw new NamingException("ClassNotFoundException!");
    }
    homeObject = homeCache.getHome(homeClass);
    if (homeObject == null) {
      Object tmpObj = Settings.getInstance().getInitCtx().lookup(beanJndiName);
      homeObject = PortableRemoteObject.narrow(tmpObj, homeClass);
      homeCache.putHome(homeClass, homeObject);
    }
    return homeObject;
  }

  /**
   * returns a class field initialContext. Will build it using hardcoded naming
   * factory and naming provider properties: currently
   * org.jnp.interfaces.NamingContextFactory and jnp://localhost:1099
   * <strong>Should only be called from code that will run under the JBoss VM</strong>
   * If this method fails it returns NULL! Thats right. We suck.
   * @return InitialContext
   * @return null
   */
  public static InitialContext getInitialContext()
  {
    try {
      if (initialContext == null) {
        // so one or both aren't set. Either the LoadProperties filter failed,
        // or
        // we could be calling from JBoss and for somereason it isn't setup.
        // So we are going to try and default.
        Properties props = new Properties();
        props.setProperty(NAMING_FACTORY_KEY, "org.jnp.interfaces.NamingContextFactory");
        props.setProperty(NAMING_PROVIDER_KEY, "jnp://localhost:1099");
        initialContext = new InitialContext(props);
      }
      return initialContext;
    } catch (Exception e) {
      logger.error("[getInitialContext] Exception thrown.", e);
    }
    // Sad, lonely and scared, we now return null.
    return null;
  }

  public static String convertTime24HrsFormatToStr(int hhmm[])
  {
    String hhmmpmOram;
    String strmm = "";
    if (new Integer(hhmm[1]).toString().length() == 1) {
      strmm = "0" + hhmm[1];
    } else
      strmm = "" + hhmm[1];

    if (hhmm[0] >= 12) {
      if (hhmm[0] >= 13)
        hhmm[0] = hhmm[0] - 12;
      hhmmpmOram = hhmm[0] + ":" + strmm + " " + "PM";
    } else {
      if (hhmm[0] == 0) {
        hhmm[0] = 12;
      }
      hhmmpmOram = hhmm[0] + ":" + strmm + " " + "AM";
    }
    return hhmmpmOram;
  }

  /**
   * This method takes in a String in the format of: HH:MM PM/AM and converts it
   * into a 24 hour time. This new time is returned in an int array[2].
   * <strong>Note: The AM/PM is case insensitive.</strong>
   * @param timestr A String with a time format of HH:MM PM/AM
   * @return An int array with the 24 hour time.
   */
  public static int[] convertTimeTo24HrsFormat(String timestr)
  {
    timestr = timestr.toUpperCase();
    int colonPostition = timestr.indexOf(':');
    int hours = Integer.parseInt((timestr.substring(0, colonPostition)).trim());
    int minutes;
    int hourMinuteArray[] = new int[2];
    int amPmMarkerPosition = timestr.indexOf('A');
    if (amPmMarkerPosition == -1) {
      amPmMarkerPosition = timestr.indexOf('P');
      if ((hours >= 1) && (hours <= 11)) {
        hours = hours + 12;
      } // end of if statement ((hh >= 1) && (hh <= 11))
    } // end of if statement (posPorA == -1)
    else {
      if (hours == 12) {
        hours = 0;
      }
    }
    minutes = Integer.parseInt((timestr.substring(colonPostition + 1, amPmMarkerPosition)).trim());
    hourMinuteArray[0] = hours;
    hourMinuteArray[1] = minutes;
    return hourMinuteArray;
  } // end of convertTimeTo24HrsFormat method

  /**
   * changes the date & time of a Timestamp object by applying a new timezone
   */
  public static Timestamp convertTimeZone(Timestamp dt, TimeZone currentTZ, TimeZone targetTZ)
  {
    GregorianCalendar gc = new GregorianCalendar(currentTZ);
    gc.setTime(dt);
    gc.get(Calendar.YEAR);
    gc.setTimeZone(targetTZ);
    Timestamp nts = new Timestamp(gc.getTimeInMillis());
    return nts;
  }

  /**
   * changes the date of a Date object by applying a new timezone
   */
  public static Date convertTimeZone(Date dt, TimeZone currentTZ, TimeZone targetTZ)
  {
    GregorianCalendar gc = new GregorianCalendar(currentTZ);
    gc.setTime(dt);
    gc.setTimeZone(targetTZ);
    Date nts = new Date(gc.getTimeInMillis());
    return nts;
  }

  /**
   * Static method to add new record in history Table
   * @param recordID
   * @param recordTypeID
   * @param operation
   * @param individualID
   * @param referenceActivityID
   */
  public static void addHistoryRecord(HashMap historyInfo, String dataSource)
  {
    try {

      InitialContext ic = getInitialContext();

      // Call Local EJB to add the record in DB
      HistoryLocalHome historyLocal = (HistoryLocalHome) ic.lookup("local/History");
      HistoryLocal history = historyLocal.create();
      history.setDataSource(dataSource);
      history.addHistoryRecord(historyInfo);

    } catch (Exception e) {
      logger.error("[addHistoryRecord]: Exception", e);
    }

  }

  // To run this our CVUtility Class shoul present at Server Side

  /**
   * Static method to remove record in history Table
   * @param recordID
   * @param recordTypeID
   * @param operation
   */
  public static void deleteHistoryRecord(int recordID, int recordTypeID, int operation, String ds)
  {
    try {

      InitialContext ic = getInitialContext();

      // Call Local EJB to add the record in DB
      HistoryLocalHome historyLocal = (HistoryLocalHome) ic.lookup("local/History");
      HistoryLocal history = historyLocal.create();
      history.setDataSource(ds);
      history.deleteHistoryRecord(operation, recordTypeID, recordID);
    } catch (Exception e) {
      logger.error("[deleteHistoryRecord]: Exception", e);
    }

  }

  public static boolean isModuleVisible(String moduleName, int individualId, String dataSource)
  {
    boolean retVal = false;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      AuthorizationLocalHome home = (AuthorizationLocalHome) ic.lookup("local/Authorization");
      AuthorizationLocal local = home.create();
      local.setDataSource(dataSource);
      retVal = local.isModuleVisible(moduleName, individualId);
    } catch (Exception e) {
      logger.error("[isModuleVisible]: Exception", e);
    }
    return retVal;
  }

  /**
   * This method looks up the authorized fields for the individual for the object type
   * and then any that are not authorized to be changed are filled in from the database
   * VO that is passed in.  The DBVO.  This method uses reflection to do all the dirty
   * work. 
   */
  public static Object replaceVO(Object objDBVO, Object objUIVO, String moduleName, int individualId, String dataSource)
  {
    HashMap hm = new HashMap();
    try {
      InitialContext ic = CVUtility.getInitialContext();
      AuthorizationLocalHome home = (AuthorizationLocalHome) ic.lookup("local/Authorization");
      AuthorizationLocal local = home.create();
      local.setDataSource(dataSource);

      hm = local.getNoneRightFieldMethod(moduleName, individualId);
      if (hm != null) {
        Collection col = hm.values();
        Iterator it = col.iterator();

        Class clDB = objDBVO.getClass();
        Class clUI = objUIVO.getClass();

        String get = "get";
        String set = "set";
        Method[] theMethods = clUI.getMethods();
        while (it.hasNext()) {
          String mName = (String) it.next();
          String getmethod = get + mName;
          Method mth = clDB.getMethod(getmethod, null);
          Object mthObj = mth.invoke(objDBVO, null);

          Object argList[] = new Object[1];
          argList[0] = mthObj;

          for (int i = 0; i < theMethods.length; i++) {
            String methodString = theMethods[i].getName();

            if (methodString.equals((set + mName))) {
              Method mth1 = clUI.getMethod((set + mName), theMethods[i].getParameterTypes());
              mth1.invoke(objUIVO, argList);
            }
          }

        }
      } // end of if statement (hm != null)
    } catch (Exception e) {
      logger.error("[replaceVO]: Exception", e);
    }
    return objUIVO;
  }

  public static ModuleFieldRightMatrix getUserModuleRight(String moduleName, int individualId, boolean byListCol, String dataSource)
  {
    ModuleFieldRightMatrix retVal = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      AuthorizationLocalHome home = (AuthorizationLocalHome) ic.lookup("local/Authorization");
      AuthorizationLocal local = home.create();
      local.setDataSource(dataSource);
      retVal = local.getUserSecurityProfileMatrix(moduleName, individualId, byListCol);
    } catch (Exception e) {
      logger.error("[getUserModuleRight]: Exception", e);
    }
    return retVal;
  }

  public static boolean canPerformRecordOperation(int indId, String moduleName, int recordId, int privilegeLevel, String dataSource)
  {
    boolean retVal = false;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      AuthorizationLocalHome home = (AuthorizationLocalHome) ic.lookup("local/Authorization");
      AuthorizationLocal local = home.create();
      local.setDataSource(dataSource);
      retVal = local.canPerformRecordOperation(indId, moduleName, recordId, privilegeLevel);
    } catch (Exception e) {
      logger.error("[canPerformRecordOperation]: Exception", e);
    }
    return retVal;
  }

  public static int getRecordPermission(int indId, String moduleName, int recordId, String dataSource)
  {
    int retVal = 0;
    try {
      AuthorizationHome home = (AuthorizationHome) CVUtility.getHomeObject("com.centraview.administration.authorization.AuthorizationHome",
          "Authorization");
      Authorization local = home.create();
      local.setDataSource(dataSource);
      retVal = local.getRecordPermission(indId, moduleName, recordId);
    } catch (Exception e) {
      logger.error("[getRecordPermission]: Exception", e);
    }
    return retVal;
  }

  // if otherWherClause is provided then the otherWherClause clause will be used
  // else a dynamic where caluse will be created
  public static void getAllAccessibleRecords(String moduleName, String tempTableName, String primaryTableName, String primaryFieldName,
      String ownerFieldName, String otherWherClause, int individualId, CVDal cvdl)
  {
    cvdl.setSqlQueryToNull();
    String fptn = primaryTableName + ".";
    String sqlQ = "";

    int moduleID = 0;

    if (moduleName != null) {
      String lowercase = moduleName.toLowerCase();
      if (lowercase.equals("entity")) {
        moduleID = 14;
      } else if (lowercase.equals("individual")) {
        moduleID = 15;
      } else if (lowercase.equals("proposals")) {
        moduleID = 31;
      } else if (lowercase.equals("projects")) {
        moduleID = 9;
      } else if (lowercase.equals("notes")) {
        moduleID = 5;
      }
    }

    // This is a special case to build the Opportunities List.
    if (moduleName.equalsIgnoreCase("Opportunities")) {
      sqlQ = "CREATE TEMPORARY TABLE opportunityaccess " + "SELECT opportunity.opportunityID, activity.owner FROM "
          + "opportunity, activity WHERE activity.activityid = opportunity.activityid " + "AND activity.owner = " + individualId
          + " UNION SELECT opportunity.opportunityID, activity.owner FROM " + "opportunity, activity, recordauthorisation b "
          + "WHERE opportunity.opportunityID = b.recordid AND b.recordtypeid = 30 "
          + "AND b.privilegelevel != 40 AND b.privilegelevel != 0 AND activity.activityid = opportunity.activityid " + "AND b.individualid = "
          + individualId + " UNION SELECT opportunity.opportunityID, activity.owner FROM publicrecords pub, activity, opportunity "
          + "WHERE pub.recordid = opportunity.opportunityID AND " + "pub.moduleid=30 AND activity.activityid = opportunity.activityid";
    } else {
      sqlQ = "CREATE TEMPORARY TABLE " + tempTableName + " " + "SELECT " + fptn + primaryFieldName + ", " + fptn + ownerFieldName + " " + "FROM "
          + primaryTableName + " " + primaryTableName;

      if (otherWherClause != null) {
        sqlQ = sqlQ + " WHERE " + otherWherClause;
      } else {
        sqlQ = sqlQ + " WHERE " + fptn + ownerFieldName + " = " + individualId;
      }

      if (moduleID != 0) {
        sqlQ = sqlQ + " UNION SELECT " + primaryTableName + "." + primaryFieldName + ", " + primaryTableName + "." + ownerFieldName
            + " FROM publicrecords pub " + "LEFT JOIN " + primaryTableName + " ON " + "(pub.recordid=" + primaryTableName + "." + primaryFieldName
            + ") " + "WHERE pub.moduleid=" + moduleID + " ";
      }

      sqlQ = sqlQ + " UNION" + " SELECT " + fptn + primaryFieldName + "," + fptn + ownerFieldName + " FROM " + primaryTableName + " "
          + primaryTableName + ", recordauthorisation b, module c" + " WHERE " + fptn + primaryFieldName + " = b.recordid "
          + "AND b.recordtypeid = c.moduleid " + "AND c.name = '" + moduleName + "'" + " AND b.privilegelevel != 40 " + "AND b.individualid = "
          + individualId;
      if (moduleName.equalsIgnoreCase("Projects")) {
        sqlQ = sqlQ + " UNION " + "SELECT p.ProjectID, p.Owner " + "FROM project p, task t, taskassigned ta WHERE "
            + "p.ProjectID=t.ProjectID AND t.ActivityID = ta.TaskID AND ta.AssignedTo = " + individualId;
      } // end of if statement (moduleName.equalsIgnoreCase("Projects"))
    } // end of else statement (moduleName.equalsIgnoreCase("Opportunities"))
    cvdl.setSqlQuery(sqlQ);
    cvdl.executeUpdate();
  }

  public static boolean isEmailAddressValid(String address)
  {
    boolean result = true;
    try {
      new InternetAddress(address);
    } catch (AddressException ae) {
      result = false;
    } catch (NullPointerException npe) {
      result = false;
    }
    return result;
  } // end isEmailAddressValid(String)

  /**
   * This method takes a hashmap of field rights and a String that is the field
   * name, and returns true if the field can be updated If anything goes wrong
   * returns false.
   * @param fieldRights a HashMap that has field name strings for keys and
   *          Integers for values. The integer determines the field privilege.
   * @param fieldName the key, we are currently checking.
   * @return true if the field can be updated or false if something ain't right.
   * @author Kevin McAllister <kevin@centraview.com>
   */
  static public boolean updateFieldPermitted(HashMap fieldRights, String fieldName)
  {
    int currentPrivilege = 0;
    if (fieldRights == null) {
      return false;
    }
    if (fieldRights.containsKey(fieldName)) {
      currentPrivilege = ((Integer) fieldRights.get(fieldName)).intValue();
      if (currentPrivilege == ModuleFieldRightMatrix.NONE_RIGHT || currentPrivilege == ModuleFieldRightMatrix.VIEW_RIGHT) {
        return false;
      }
      return true;
    }
    return false;
  }

  /**
   * This method spits out a MethodOfContactVO based on the input from the form
   * and a HashMap of the MOCs that were on the Entity prior to this update.
   * @param dynaForm Of course the form bean is still needed
   * @param number there are 4 mocs, the formbean names and parameter keys were
   *          generalized enough to allow them to have a number concatenated to
   *          the end and get the right info.
   * @param currentMocVOs A HashMap of the MocVOs that were associated with the
   *          Entity prior to this edit.
   * @param formMocId This is the MOCId as passed in by the Form, or -1 if there
   *          was none.
   * @param email a boolean flag, because the 4th moc is always an email field,
   *          for some reason it is handled differently, even though it always
   *          uses the same backend database.
   * @return the Method of contact that is now representative for this ID
   * @author Kevin McAllister <kevin@centraview.com>
   */
  static public MethodOfContactVO updateMoc(DynaActionForm dynaForm, String number, HashMap currentMocVOs, int formMocId, boolean email)
  {
    MethodOfContactVO workingVO = null;
    String mocContent = "";
    int mocType = 0;
    if (email) {
      mocContent = (dynaForm.get("email") == null) ? "" : (String) dynaForm.get("email");
      mocType = 1;
    } else {
      mocContent = CVUtility.getMOCContent(dynaForm, number);
      mocType = Integer.parseInt((String) dynaForm.get("mocType" + number));
    }
    // the old code used form fields MocAllReadyPresent# to determine how to set
    // the updated and added flags. That stuff should eventually be removed
    // from the formbean and the JSP as it is worthless. given this more
    // accurate
    // way to determine that info, which follows:
    if (currentMocVOs.containsKey(new Integer(formMocId))) {
      workingVO = (MethodOfContactVO) currentMocVOs.get(new Integer(formMocId));
      if (mocContent.trim().length() == 0) {
        workingVO.delete(true);
        workingVO.updated(false);
        workingVO.added(false);
      } else {
        workingVO.delete(false);
        workingVO.updated(true);
        workingVO.added(false);
      }
    } else {
      workingVO = new MethodOfContactVO();
      workingVO.updated(false);
      workingVO.added(true);
      workingVO.delete(false);
      workingVO.setMocID(formMocId);
      workingVO.setIsPrimary("YES");
    }
    workingVO.setContent(mocContent);
    workingVO.setMocType(mocType);
    workingVO.setSyncAs(getSyncAs(mocType));

    return workingVO;
  }

  /**
   * Returns a String representation of the <code>syncAs</code> type for the
   * given int value. I know this sucks, but we're probably going to get rid of
   * sync as soon, so I didn't want to over-engineer this simple thing.
   */
  public static String getSyncAs(int mocType)
  {
    switch (mocType) {
      case 2:
        return ("Fax");
      case 3:
        return ("Mobile");
      case 4:
        return ("Main");
      case 5:
        return ("Home");
      case 6:
        return ("Other");
      case 7:
        return ("Pager");
      case 8:
        return ("Work");
      default:
        return ("Main");
    }
  }// end getSyncAs(int) method

  /**
   * Simple method which takes information from the parameter, and just formats
   * in an expected way and returns it. This is highly specific to MOCContent
   * @author Kevin McAllister <kevin@centraview.com>
   */
  public static String getMOCContent(DynaActionForm dynaForm, String number)
  {
    String content = dynaForm.get("mocContent" + number) == null ? "" : (String) dynaForm.get("mocContent" + number);
    String extension = dynaForm.get("mocExt" + number) == null ? "" : (String) dynaForm.get("mocExt" + number);
    if (!(extension.equals(""))) {
      content = content + "EXT" + extension;
    }
    return content;
  }

  /**
   * This method takes a javax.servlet.ServletContext and gets the
   * org.apache.catalina.resources attribute from the Context. The Object held
   * at that key implements a method getHostName which provides us the
   * configured hostname from the server.xml. If that fails we try and get the
   * hostname another way.
   * @param context a javax.servlet.ServletContext. which hopefully contains the
   *          class we need.
   * @return the hostname as a String
   */
  static public final String getHostName(ServletContext context)
  {
    String host = null;
    // Get the catalina resources from the servlet Attribute.
    // use Reflection to invoke the getHostName method.
    Object dirContext = context.getAttribute("org.apache.catalina.resources");
    Class dirContextClass = dirContext.getClass();

    try {
      Method hostMethod = dirContextClass.getMethod("getHostName", null);
      host = (String) hostMethod.invoke(dirContext, null);
    } catch (Exception e) {
      logger.error("[getHostName] Cannot retrieve hostname.", e);
    }

    // if it is null the above method call failed for some reason.
    // if it is localhost, we will assume there is only one
    // instance on this machine and then we will try and get the hostname from
    // the server.
    try {
      if (host == null || host.equals("localhost")) {
        InetAddress local = InetAddress.getLocalHost();
        host = local.getHostName();
      }
    } catch (UnknownHostException e) {
      logger.error("[getHostName] Cannot get hostname for localhost.", e);
      host = "UNKNOWN";
    }
    return host;
  }

  /**
   * Returns the Directory where CentraView files are written to. If this
   * directory cannot be found (for whatever reason) a null object is returned.
   * Must only be called from Tomcat world.
   * @return The CentraView File Directory (or null if it cannot be found).
   */
  public static File getCentraViewFileSystemRoot(String dataSource)
  {
    File returnFile = null;
    try {
      CvFileHome fileHome = (CvFileHome) getHomeObject("com.centraview.file.CvFileHome", "CvFile");
      CvFile fileRemote = fileHome.create();
      fileRemote.setDataSource(dataSource);
      CvFolderVO rootFolder = fileRemote.getFolder(-13, 1);
      returnFile = new File(rootFolder.getLocationName());
    } catch (Exception e) {
      logger.error("[getCentraViewFileSystemRoot] Exception thrown.", e);
    }
    return returnFile;
  }

  /**
   * Converts a long to a left-zero padded string, len characters long. len
   * should be less than 27. Taken from
   * http://mindprod.com/jgloss/conversion.html
   * @param i the long value to be converted.
   * @param len a value less than 27
   * @return String
   */
  public static String leftZeroPad(long i, int len)
  {
    if (len > 27) {
      len = 27;
    }
    // converts integer to left-zero padded string, len chars long.
    String s = Long.toString(i);
    if (s.length() > len) {
      return s.substring(0, len);
    } else if (s.length() < len) {// pad on left with zeros
      return "000000000000000000000000000".substring(0, len - s.length()) + s;
    } else {
      return s;
    }
  } // end toLZ

  /**
   * invokeEJBMethod will use reflection to invoke a method on an EJB. It will
   * get the HomeObject invoke create and setDataSource and then find the
   * requested method and invoke it with the given parameters. There are
   * performance considerations when using this, specifically it should only be
   * used when your Handler will only call one EJB method on the EJB, if you are
   * going to make several calls to the same EJB it is best to get the object
   * and call create yourself in order to save on the RMI overhead, reflection
   * overhead and the overhead of repeat calls to create on the same EJB object.
   * @param jndiName The JNDI name of the Object.
   * @param remoteClassName the ClassName of the object.
   * @param remoteMethodName the name of the method you wish to invoke.
   * @param parameters an Object array of the parameters, there is currently a
   *          problem using primatives, they do not automatically unbox like I
   *          thought, so something will change in this method shortly to
   *          accomodate that.
   * @param dataSource the dataSource to set before invoking the remoteMethod
   *          indicated by remoteMethodName
   * @return an Object, cast it as you will :) appropriately of course unless
   *         you like looking at ClassCastException
   * @throws Exception yeah, sucky generic exception, but when this stuff
   *           doesn't work, it can be really complicated to untangle. Either
   *           make it better, or enclose it in a servlet exception and rethrow,
   *           so that tomcat will display a nice "Application Error" page.
   */
  public static Object invokeEJBMethod(String jndiName, String remoteClassName, String remoteMethodName, Object[] parameters, String dataSource)
      throws Exception
  {
    Object result = null;
    try {
      Object ejb = CVUtility.setupEJB(jndiName, remoteClassName, dataSource);
      Class ejbClass = ejb.getClass();
      // iterate the parameters array and get the types to build a Class[] to
      // find the method.
      Class[] methodArgType = CVUtility.getClassArray(parameters);
      Method remoteMethod = ejbClass.getMethod(remoteMethodName, methodArgType);
      // it'd be really cool if this actually works consistently.
      result = remoteMethod.invoke(ejb, parameters);
    } catch (Exception e) {
      // Catch all possible exceptions log and rethrow as a generic exception
      // the struts world should handle this somehow or wrap up in a servlet
      // exception
      // and bubble it up.
      logger.error("[callRemoteMethod] Exception trying to call " + remoteClassName + "." + remoteMethodName + " on EJB with JNDI: " + jndiName, e);
      logger.debug("[callRemoteMethod] parameters: " + parameters + " dataSource: " + dataSource);
      throw e;
    }
    return result;
  }

  /**
   * Takes an array of Object[] iterates and creates an array of Class[] with
   * the types in parameters in the same order. This is useful for finding a
   * method using reflection, having an array of the parameters to be passed in.
   * @param parameters
   * @return
   */
  public static Class[] getClassArray(Object[] parameters)
  {
    Class[] result = null;
    if (parameters != null) {
      ArrayList typeList = new ArrayList();
      for (int i = 0; i < parameters.length; i++) {
        Object param = parameters[i];
        typeList.add(param.getClass());
      }
      if (typeList.size() > 0) {
        result = (Class[]) typeList.toArray(new Class[typeList.size()]);
      }
    }
    return result;
  }

  /**
   * setupEJB does the JNDI lookup and gets the home object. Then uses
   * reflection to invoke the create and setDataSource methods. The remote ejb
   * object ready to use is returned to you.
   * @param jndiName The JNDI name of the Object.
   * @param remoteClassName the ClassName of the object.
   * @param dataSource the dataSource to set before invoking the remoteMethod
   *          indicated by remoteMethodName
   * @return the remote ejb object ready for casting and use.
   * @throws Exception yeah, sucky generic exception, but when this stuff
   *           doesn't work, it can be really complicated to untangle. Either
   *           make it better, or enclose it in a servlet exception and rethrow,
   *           so that tomcat will display a nice "Application Error" page.
   */
  public static Object setupEJB(String jndiName, String remoteClassName, String dataSource) throws Exception
  {
    Object ejb = null;
    try {
      // Get the home object
      Object ejbHome = CVUtility.getHomeObject(remoteClassName, jndiName);
      Class ejbHomeClass = ejbHome.getClass();
      // Call create
      Method homeCreateMethod = ejbHomeClass.getMethod("create", null);
      ejb = homeCreateMethod.invoke(ejbHome, null);
      Class ejbClass = ejb.getClass();
      // set the dataSource
      Class[] setDataSourceArgType = { String.class };
      Method setDataSourceMethod = ejbClass.getMethod("setDataSource", setDataSourceArgType);
      Object[] setDataSourceArg = { dataSource };
      setDataSourceMethod.invoke(ejb, setDataSourceArg);
    } catch (Exception e) {
      throw e;
    }
    return ejb;
  }
  
  /**
   * Simple utility class to remove carriage returns, newlines and apostrophes
   * to prevent javascript errors.
   * @param dirty
   * @return the string after removing the offending characters.
   */
  public static final String cleanStringJS(String dirty) 
  {
    String work = dirty;
    work = work.replaceAll("\r", "");
    work = work.replaceAll("\n", "");
    work = work.replaceAll("\'", "\\\\\'");
    return work;
  }
  
  /**
   * checks an object to see if it is null and if it contains
   * someting interesting.  In the case of a String, it means non
   * whitespace data, in the case of a List or Collection it means
   * there are elements.  In the case of anything else, there
   * is no interesting test, it just means not null.
   * @param o
   * @return true if object is interesting, false otherwise
   */
  public static final boolean notEmpty(Object o)
  {
    if (null == o) {
      return false;
    }
    if (o instanceof String) {
      if (((String)o).trim().equals("")) {
        return false;
      }
    } else if (o instanceof Collection) {
      if (((Collection)o).isEmpty()) {
        return false;
      }
    } else if (o instanceof List) {
      if (((List)o).isEmpty()) {
        return false;
      }
    }
    return true;
  }
  
  /**
   * Checks an object to see if it is empty.  Meaning either null
   * or a string with no non-whitespace chars, or a Collection or List
   * that is empty.
   * @param o
   * @return true if object is null or not interesting false otherwise.
   */
  public static final boolean empty(Object o) {
    return (!CVUtility.notEmpty(o));
  }
}
