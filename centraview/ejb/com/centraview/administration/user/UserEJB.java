/*
 * $RCSfile: UserEJB.java,v $    $Revision: 1.8 $  $Date: 2005/10/13 13:04:20 $ - $Author: mcallist $
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

package com.centraview.administration.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.IntMember;
import com.centraview.common.Password;
import com.centraview.common.StringMember;
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.contact.individual.IndividualLocal;
import com.centraview.contact.individual.IndividualLocalHome;
import com.centraview.contact.individual.IndividualPK;
import com.centraview.contact.individual.IndividualVO;
import com.centraview.file.CvFileFacade;
import com.centraview.preference.PreferenceLocal;
import com.centraview.preference.PreferenceLocalHome;
import com.centraview.preference.PreferenceVO;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

/**
 * This is the EJB class for User The Logic for methods defined in Remote
 * interface is defined in this class
 */
public class UserEJB implements SessionBean {
  private static Logger logger = Logger.getLogger(UserEJB.class);
  protected SessionContext ctx;
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
   * Adds a new User
   * @param individualId
   * @param UserVO
   * @exception UserException
   */
  public void addUser(int individualId, UserVO uvo) throws UserException
  {
    // First find out if individualId is an Administrator, if not, blow up.
    if (!this.isAdministrator(individualId)) {
      throw new EJBException("Not Authorized");
    }
    CVDal cvdl = new CVDal(dataSource);
    try {
      if (uvo == null) {
        throw new UserException(UserException.INVALID_DATA, "Cannot add User. User is empty.");
      }
      if ((uvo.getLoginName() == null) || (uvo.getLoginName().length() == 0)) {
        throw new UserException(UserException.INVALID_DATA, "User Name is Empty");
      }
      if (!(uvo.getContactID() > 0)) {
        throw new UserException(UserException.INVALID_DATA, "Individual ID is 0");
      }
      if (uvo.getUserType() == null) {
        uvo.setUserType(UserVO.UT_EMPLOYEE);
      }
      if (uvo.getUserStatus() == null) {
        uvo.setUserStatus(UserVO.US_DISABLED);
      }
      // Check to see if the individual exists.
      int userTargetIndividualId = uvo.getContactID();
      cvdl.setSql("contact.getindividual");
      cvdl.setInt(1, userTargetIndividualId);
      Collection coli = cvdl.executeQuery();
      Iterator iti = coli.iterator();
      if (!iti.hasNext()) {
        throw new UserException(UserException.INSERT_FAILED, "Individual Does not exists");
      }
      // Check to see if there is already a user for this
      // Individual
      cvdl.setSqlQueryToNull();
      cvdl.setSql("user.getuser");
      cvdl.setInt(1, uvo.getContactID());
      Collection col = cvdl.executeQuery();
      Iterator itu = col.iterator();
      if (itu.hasNext()) {
        throw new UserException(UserException.INSERT_FAILED, "A user for this individual already exists");
      }
      // first try to create the users home folder, if this fails in any way, bail out.
      CvFileFacade cvf = new CvFileFacade();
      int folderID = 0;
      try {
        folderID = cvf.addFolder(individualId, uvo.getLoginName(), this.dataSource);
        // change the owner of the folder:
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("UPDATE cvfolder SET owner = ? WHERE folderId = ?");
        cvdl.setInt(1, userTargetIndividualId);
        cvdl.setInt(2, folderID);
        cvdl.executeUpdate();
      } catch (Exception e) {
        throw new UserException(UserException.IO_FAILURE, "Could not create home directory.");
      }
      if (folderID < 1) {
        throw new UserException(UserException.IO_FAILURE, "Could not create home directory.");
      }
      cvdl.setSqlQueryToNull();
      cvdl.setSql("user.insertuser");
      cvdl.setString(1, uvo.getLoginName());
      cvdl.setInt(2, uvo.getContactID());
      String userPassword = uvo.getPassword();
      Password passwordService = Password.getInstance();
      String encryptedPassword = passwordService.encrypt(userPassword);
      cvdl.setString(3, encryptedPassword);
      cvdl.setString(4, uvo.getUserStatus());
      cvdl.setString(5, uvo.getUserType());
      cvdl.executeUpdate();
      // insert into userrole
      Vector urVec = uvo.getUserSecurityProfileId();
      if (urVec != null) {
        Iterator it = urVec.iterator();
        while (it.hasNext()) {
          int rid = ((Integer) it.next()).intValue();
          cvdl.setSqlQueryToNull();
          cvdl.setSql("authorization.insertusersecurityprofile");
          cvdl.setInt(1, rid);
          cvdl.setInt(2, userTargetIndividualId);
          cvdl.executeUpdate();
        }
      }
      cvdl.setSqlQueryToNull();
      cvdl.setSql("user.insertdefaultpreference");
      cvdl.setInt(1, userTargetIndividualId);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      cvdl.setSql("authorization.insertdefaultrecordpermission");
      cvdl.setInt(1, userTargetIndividualId);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      cvdl.setSql("authorization.insertdefaultlistpermission");
      cvdl.setInt(1, userTargetIndividualId);
      cvdl.executeUpdate();
      // Add the folder preference
      PreferenceVO preferenceVO = new PreferenceVO();
      preferenceVO.setPreferenceName("DEFAULTFOLDERID");
      preferenceVO.setPreferenceValue(String.valueOf(folderID));
      Vector preferenceVector = new Vector();
      preferenceVector.addElement(preferenceVO);
      InitialContext ic = CVUtility.getInitialContext();
      PreferenceLocalHome home = (PreferenceLocalHome) ic.lookup("local/Preference");
      PreferenceLocal localPreference = home.create();
      localPreference.setDataSource(this.dataSource);
      localPreference.updateUserPreference(userTargetIndividualId, preferenceVector);
    } catch (Exception e) {
      logger.error("[addUser] Exception thrown.", e);
      throw new UserException(UserException.INSERT_FAILED, "Failed in User ejb while adding User");
    } finally {
      cvdl.destroy();
    }
  } // end addUser(int,UserVO) method

  /**
   * Updates a user in the database.
   * @param individualId The individualID of the user executing the update.
   * @param uvo A UserVO object representing the up-to-date user to be
   *          stored/updated.
   * @return void
   * @throws UserException
   */
  public void updateUser(int individualId, UserVO uvo) throws UserException
  {
    if (!this.isAdministrator(individualId)) {
      throw new EJBException("Not Authorized");
    }
    CVDal cvdl = new CVDal(dataSource);
    try {
      if (uvo == null) {
        throw new UserException(UserException.INVALID_DATA, "Cannot update User. UserVO is empty.");
      }

      if ((uvo.getLoginName() == null) || (uvo.getLoginName().length() == 0)) {
        throw new UserException(UserException.INVALID_DATA, "User Name is Empty");
      }

      if (!(uvo.getContactID() > 0)) {
        throw new UserException(UserException.INVALID_DATA, "Individual ID is 0");
      }

      if (uvo.getUserType() == null) {
        uvo.setUserType(UserVO.UT_EMPLOYEE);
      }

      if (uvo.getUserStatus() == null) {
        uvo.setUserStatus(UserVO.US_DISABLED);
      }

      String updateQuery = "UPDATE user SET Name=?, Password=?, userstatus=?, usertype=? WHERE IndividualID=?";

      if (uvo.getPassword() == null || uvo.getPassword().length() <= 0) {
        updateQuery = "UPDATE user SET Name=?, userstatus=?, usertype=? WHERE IndividualID=?";
      }

      int indId = uvo.getContactID();

      // Need to have this because we have a different number
      // of parameters being set on the prepared statement.
      int queryCount = 1;

      cvdl.setSqlQuery(updateQuery);
      cvdl.setString(queryCount++, uvo.getLoginName());

      if (uvo.getPassword() != null && uvo.getPassword().length() > 0) {
        String updatedPassword = uvo.getPassword();
        Password passwordService = Password.getInstance();
        String encryptedPassword = passwordService.encrypt(updatedPassword);
        cvdl.setString(queryCount++, encryptedPassword);
      }

      cvdl.setString(queryCount++, uvo.getUserStatus());
      cvdl.setString(queryCount++, uvo.getUserType());
      cvdl.setInt(queryCount++, uvo.getContactID());
      cvdl.executeUpdate();

      // to Update UserRole delete all records and add newer ones
      cvdl.clearParameters();
      cvdl.setSqlQuery("DELETE FROM usersecurityprofile WHERE individualid = ?");
      cvdl.setInt(1, indId);
      cvdl.executeUpdate();

      Vector urVec = uvo.getUserSecurityProfileId();

      if (urVec != null) {
        Iterator it = urVec.iterator();

        while (it.hasNext()) {
          int rid = ((Integer) it.next()).intValue();
          cvdl.clearParameters();
          cvdl.setSql("authorization.insertusersecurityprofile");
          cvdl.setInt(1, rid);
          cvdl.setInt(2, indId);
          cvdl.executeUpdate();
        }
      }
    } catch (Exception e) {
      System.out.println("[Exception][UserEJB.updateUser] Exception Thrown: " + e);
      // e.printStackTrace();
      throw new UserException(UserException.INSERT_FAILED, "Failed in User ejb while adding User");
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
  } // end updateUser(int,UserVO) method

  public void changePassword(int userId, String oldPwd, String newPwd) throws UserException
  {
    CVDal cvdl = new CVDal(dataSource);

    try {
      cvdl.setSql("user.getpwd");
      cvdl.setInt(1, userId);

      Collection col = cvdl.executeQuery();

      HashMap hm = (HashMap) col.iterator().next();

      if (hm != null) {
        Password passwordService = Password.getInstance();

        String dbOldPwd = (String) hm.get("Password");
        String oldEncryptedPassword = passwordService.encrypt(oldPwd);

        if (!dbOldPwd.equals(oldEncryptedPassword)) {
          throw new UserException(UserException.COULDNOT_CHANGE_PASSWORD, "Old password is invalid");
        }

        cvdl.setSql("user.changepwd");

        String newEncryptedPassword = passwordService.encrypt(newPwd);
        cvdl.setString(1, newEncryptedPassword);
        cvdl.setInt(2, userId);
        cvdl.executeUpdate();
      }
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
  }

  public void setNewPassword(int individualId, String newPwd)
  {
    CVDal cvdl = new CVDal(dataSource);
    try {
      Password passwordService = Password.getInstance();
      cvdl.setSql("user.changepwd");
      String newEncryptedPassword = passwordService.encrypt(newPwd);
      cvdl.setString(1, newEncryptedPassword);
      cvdl.setInt(2, individualId);
      cvdl.executeUpdate();
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
  }

  /**
   * Get a UserVO object without individual data.
   * @param individualId The <strong>individualID</strong> of the user whose
   *          information we are retreiving.
   * @return A UserVO object without invidual data.
   * @throws UserException
   */
  public UserVO getUserPlain(int individualId) throws UserException
  {
    CVDal cvdl = new CVDal(dataSource);
    UserVO userVO = new UserVO();

    try {
      // first, get the user's basic info
      cvdl.setSql("user.getuser");
      cvdl.setInt(1, individualId);

      Collection results = cvdl.executeQuery();

      if (results != null && results.size() > 0) {
        Iterator iter = results.iterator();
        while (iter.hasNext()) {
          HashMap row = (HashMap) iter.next();

          if (row != null) {
            userVO.setUserId(((Number) row.get("UserId")).intValue());
            userVO.setContactID(((Number) row.get("IndividualID")).intValue());
            userVO.setLoginName((String) row.get("Name"));
            userVO.setUserStatus((String) row.get("userstatus"));
            userVO.setUserType((String) row.get("usertype"));
            userVO.setPassword((String) row.get("Password"));
            userVO.setFirstName((String) row.get("firstname"));
            userVO.setLastName((String) row.get("lastname"));
          } else {
            logger.debug("Could not obtain user info from database for userID = [" + individualId + "]");
            logger.debug("Throwing UserException...");
            throw new UserException(UserException.COULDNOT_CHANGE_PASSWORD, "Could not find user");
          }
        } // end while (iter.hasNext())
      } // end if (results != null ...)

      // next, get user's security profile info
      cvdl.clearParameters();
      cvdl.setSql("authorization.getusersecurityprofile");
      cvdl.setInt(1, individualId);

      Collection profileResults = cvdl.executeQuery();
      if (profileResults != null && profileResults.size() > 0) {
        Iterator profileIter = profileResults.iterator();

        Vector securityProfile = null;

        while (profileIter.hasNext()) {
          HashMap profileRow = (HashMap) profileIter.next();
          if (profileRow != null) {
            if (securityProfile == null) {
              securityProfile = new Vector();
            }

            DDNameValue ddnv = new DDNameValue(((Number) profileRow.get("profileid")).intValue(), (String) profileRow.get("profilename"));
            securityProfile.add(ddnv);
          }
        }
        userVO.setUserSecurityProfile(securityProfile);
      } // end if (profileResults != null ...)
    } finally {
      cvdl.destroy();
      cvdl = null;
    }

    return userVO;
  } // end getUserPlain(int) method

  /**
   * This method will delete a user account and anything associated with that
   * user account(preferences, email account).
   * @param userID The individualID of the user to be deleted.
   * @return void
   * @throws UserException
   */
  public void deleteUser(int userID)
  {
    CVDal cvdl = new CVDal(dataSource);

    try {
      cvdl.setSql("user.deleteuser");
      cvdl.setInt(1, userID);
      cvdl.executeUpdate();

      cvdl.clearParameters();
      cvdl.setSql("user.deleteuserpreference");
      cvdl.setInt(1, userID);
      cvdl.executeUpdate();

      cvdl.clearParameters();
      cvdl.setSqlQuery("DELETE FROM usersecurityprofile WHERE individualid = ?");
      cvdl.setInt(1, userID);
      cvdl.executeUpdate();

      cvdl.clearParameters();
      cvdl.setSqlQuery("DELETE FROM recordauthorisation WHERE individualid = ?");
      cvdl.setInt(1, userID);
      cvdl.executeUpdate();

      cvdl.clearParameters();
      cvdl.setSqlQuery("DELETE FROM emailaccount WHERE Owner = ?");
      cvdl.setInt(1, userID);
      cvdl.executeUpdate();
    } catch (Exception e) {
      logger.error("[deleteUser]: Exception", e);
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
  } // end deleteUser(int) method

  /**
   * Returns the individualId associated with the user for the given userId.
   * @param individualID The <strong>individualId</strong> of the user
   *          requesting the information.
   * @param userId The <strong>userId</strong> of the user whose individualID
   *          we're asking for.
   * @return int individualId of the user or -1 for error condition.
   */
  public int getIndividualIdForUser(int userId)
  {
    int returnIndividualId = -1;
    if (userId <= 0) {
      return returnIndividualId;
    }

    CVDal cvdal = new CVDal(dataSource);
    try {
      cvdal.setSqlQuery("SELECT individualId FROM user WHERE userId = ?");
      cvdal.setInt(1, userId);
      Collection results = cvdal.executeQuery();

      if (results != null && results.size() > 0) {
        Iterator iter = results.iterator();
        while (iter.hasNext()) {
          HashMap row = (HashMap) iter.next();
          Number temp = (Number) row.get("individualId");
          try {
            returnIndividualId = temp.intValue();
          } catch (Exception e) {
            // already set default error condition value
          }
        }
      }
    } catch (Exception e) {
      return -1;
    } finally {
      cvdal.destroy();
      cvdal = null;
    }
    return returnIndividualId;
  } // end getIndividualIdForUser() method

  /**
   * Gets the Source Value List for the source lookup.
   * @param individualId
   * @param parameters
   * @return
   */
  public ValueListVO getUserValueList(int individualId, ValueListParameters parameters)
  {
    if (!(this.isAdministrator(individualId) || this.isCustomer(individualId))) {
      throw new EJBException("Not Authorized");
    }
    ArrayList list = new ArrayList();
    boolean applyFilter = false;
    CVDal cvdl = new CVDal(this.dataSource);
    try {
      String filter = parameters.getFilter();
      if (filter != null && filter.length() > 0) {
        String str = "CREATE TABLE userlistfilter " + filter;
        cvdl.setSqlQuery(str);
        cvdl.executeUpdate();
        cvdl.setSqlQueryToNull();
        applyFilter = true;
      }
      int numberOfRecords = 0;
      String str = "SELECT COUNT(*) AS count FROM user";
      cvdl.setSqlQuery(str);
      Collection countCollection = cvdl.executeQuery();
      cvdl.setSqlQueryToNull();
      Iterator i = countCollection.iterator();
      if (i.hasNext()) {
        HashMap row = (HashMap) i.next();
        Number count = (Number) row.get("count");
        numberOfRecords = count.intValue();
      }
      parameters.setTotalRecords(numberOfRecords);
      String select = "SELECT u.userId, u.name AS userName, concat(i.firstName , ' ', i.lastName) AS name, i.individualId, e.name AS entityName, e.entityId, u.userStatus AS enabled, u.userType ";
      StringBuffer from = new StringBuffer("FROM user AS u, individual AS i, entity AS e ");
      StringBuffer where = new StringBuffer("WHERE u.individualId = i.individualId AND i.entity = e.entityId ");
      String orderBy = "ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
      String limit = parameters.getLimitParam();
      StringBuffer query = new StringBuffer();
      query.append(select);
      query.append(from);
      if (applyFilter) {
        query.append(", userlistfilter AS lf ");
      }
      query.append(where);
      if (applyFilter) {
        query.append("AND u.userId = lf.userId ");
      }
      query.append(orderBy);
      query.append(limit);
      cvdl.setSqlQuery(query.toString());
      list = cvdl.executeQueryList(1);
      if (numberOfRecords < 1) {
        parameters.setTotalRecords(list.size());
      }
      if (applyFilter) {
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("DROP TABLE userlistfilter");
        cvdl.executeUpdate();
      }
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
    return new ValueListVO(list, parameters);
  }

  public UserList getUserList(int individualId, HashMap hashmap)
  {
    if (!this.isAdministrator(individualId)) {
      throw new EJBException("Not Authorized");
    }
    Integer intStart = (Integer) hashmap.get("startATparam");
    Integer intEnd = (Integer) hashmap.get("EndAtparam");
    String strSearch = (String) hashmap.get("searchString");
    String strSortMem = (String) hashmap.get("sortmem");
    Character chrSortType = (Character) hashmap.get("sortType");

    char charSort = chrSortType.charValue();

    int intStartParam = intStart.intValue();
    int intEndParam = intEnd.intValue();

    int beginIndex = Math.max(intStartParam - 100, 1);
    int endindex = intEndParam + 100;

    UserList userList = new UserList();

    userList.setSortMember(strSortMem);

    CVDal cvdl = new CVDal(dataSource);

    try {
      Collection colList = null;

      if ((strSearch != null) && strSearch.startsWith("ADVANCE:")) {

      } else {
        String sortType = "ASC";

        if (charSort == 'A') {
          sortType = "ASC";
        } else {
          sortType = "DESC";
        }

        cvdl.setDynamicQuery("user.getuserlist", sortType, strSortMem, beginIndex, endindex);
        colList = cvdl.executeQuery();
        cvdl.clearParameters();

        cvdl.setSql("user.listcount");
        cvdl.executeQuery();

        Collection count = cvdl.executeQuery();
        Iterator itCount = count.iterator();
        HashMap hmx = (HashMap) itCount.next();
        Integer endCount = (Integer) hmx.get("allcountuser");
        cvdl.clearParameters();

        int totalCount = endCount.intValue();

        userList.setTotalNoOfRecords(totalCount);
      }

      if (colList != null) {
        Iterator it = colList.iterator();

        int i = 0;

        while (it.hasNext()) {
          i++;

          HashMap hm = (HashMap) it.next();
          int userID = ((Long) hm.get("UserID")).intValue();
          int IndividualID = ((Long) hm.get("IndividualID")).intValue();
          int EntityID = ((Long) hm.get("EntityID")).intValue();

          try {
            IntMember intUserID = new IntMember("UserID", userID, 10, "", 'T', false, 10);
            IntMember intIndividualID = new IntMember("IndividualID", IndividualID, 10, "", 'T', false, 10);
            IntMember intEntityID = new IntMember("EntityID", EntityID, 10, "", 'T', false, 10);

            StringMember strName = null;
            StringMember strEntity = null;
            StringMember strUserName = null;
            StringMember strEnabled = null;

            if ((hm.get("Name") != null)) {
              strName = new StringMember("Name", (String) hm.get("Name"), 10, "", 'T', true);
            } else {
              strName = new StringMember("Name", null, 10, "", 'T', true);
            }

            if ((hm.get("Entity") != null)) {
              strEntity = new StringMember("Entity", (String) hm.get("Entity"), 10, "", 'T', true);
            } else {
              strEntity = new StringMember("Entity", null, 10, "", 'T', true);
            }

            if ((hm.get("UserName") != null)) {
              strUserName = new StringMember("UserName", (String) hm.get("UserName"), 10, "", 'T', true);
            } else {
              strUserName = new StringMember("UserName", null, 10, "", 'T', true);
            }

            if ((hm.get("Enabled") != null)) {
              strEnabled = new StringMember("Enabled", (String) hm.get("Enabled"), 10, "", 'T', false);
            } else {
              strEnabled = new StringMember("Enabled", null, 10, "", 'T', false);
            }

            UserListElement userlistelement = new UserListElement(IndividualID);

            userlistelement.put("UserID", intUserID);
            userlistelement.put("IndividualID", intIndividualID);
            userlistelement.put("EntityID", intEntityID);
            userlistelement.put("Name", strName);
            userlistelement.put("UserName", strUserName);
            userlistelement.put("Entity", strEntity);
            if (String.valueOf(strEnabled.getMemberValue()).equalsIgnoreCase("Enabled")) {
              userlistelement.put("Enabled", new StringMember("Enabled", "Yes", 10, "", 'T', false));
            } else {
              userlistelement.put("Enabled", new StringMember("Enabled", "No", 10, "", 'T', false));
            }

            StringBuffer stringbuffer = new StringBuffer("00000000000");
            stringbuffer.setLength(11);

            String s3 = (new Integer(i)).toString();
            stringbuffer.replace(stringbuffer.length() - s3.length(), stringbuffer.length(), s3);

            String s4 = stringbuffer.toString();

            userList.put(s4, userlistelement);
          } catch (Exception e) {
            System.out.println("[Exception][UserEJB.getUserList] Exception Thrown: " + e);
            // e.printStackTrace();
          }
        }
      }

      userList.setListType("USER");
      userList.setBeginIndex(beginIndex);
      userList.setEndIndex(userList.size());
    } finally {
      cvdl.destroy();
      cvdl = null;
    }

    return userList;
  } // end getUserList(int,HashMap) method

  public UserVO getCustomerUserFull(int userId) throws UserException
  {
    CVDal cvdl = new CVDal(dataSource);
    UserVO uv = null;
    IndividualVO iv = null;

    try {
      cvdl.setSql("user.getuser");
      cvdl.setInt(1, userId);

      Collection col = cvdl.executeQuery();
      HashMap hm = new HashMap();

      if (col != null) {
        Iterator iter = col.iterator();
        while (iter.hasNext()) {
          hm = (HashMap) iter.next();
          break;
        }
      }

      if (hm != null && hm.size() > 0) {
        uv = new UserVO();
        uv.setUserId(((Long) hm.get("UserId")).intValue());
        uv.setContactID(((Long) hm.get("IndividualID")).intValue());
        uv.setLoginName((String) hm.get("Name"));
        uv.setUserStatus((String) hm.get("userstatus"));
        uv.setUserType((String) hm.get("usertype"));

        String strPassword = (String) hm.get("Password");

        uv.setPassword(strPassword);
        uv.setFirstName((String) hm.get("firstname"));
        uv.setLastName((String) hm.get("lastname"));

        if (hm.get("IndividualID") != null) {
          int intIdTemp = ((Long) hm.get("IndividualID")).intValue();
          try {
            InitialContext ic = CVUtility.getInitialContext();
            IndividualLocalHome home = (IndividualLocalHome) ic.lookup("local/Individual");
            IndividualLocal remote = home.findByPrimaryKey(new IndividualPK(intIdTemp, this.dataSource));
            iv = remote.getIndividualVOWithBasicReferences();
          } catch (Exception e) {
            System.out.println("[Exception][UserEJB.getCustomerUserFull] Exception Thrown: " + e);
            e.printStackTrace();
          }
        }

        if (iv != null) {
          uv.setPrimaryAddress(iv.getPrimaryAddress());
          uv.setEntityID(iv.getEntityID());
          uv.setContactID(iv.getContactID());
          uv.setFirstName(iv.getFirstName());
          uv.setLastName(iv.getLastName());
          uv.setTitle(iv.getTitle());

          Collection mocColl = iv.getMOC();
          if (mocColl != null) {
            Iterator it = mocColl.iterator();
            uv.clearMOC();
            while (it.hasNext()) {
              uv.setMOC((MethodOfContactVO) it.next());
            }
          }

          uv.setCreatedBy(iv.getCreatedBy());
          uv.setModifiedBy(iv.getModifiedBy());
        }

        return uv;
      }
      throw new UserException(UserException.COULDNOT_CHANGE_PASSWORD, "Could not find user");
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
  } // end getCustomerUserFull(int) method

  /**
   * @author Kevin McAllister <kevin@centraview.com> This simply sets the target
   *         datasource to be used for DB interaction
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  }

  /**
   * Given an individualId tell us if this individual has an associated user
   * which is an administrator.
   * @param individualId
   * @return
   */
  private boolean isAdministrator(int individualId)
  {
    boolean test = false;
    try {
      UserVO user = this.getUserPlain(individualId);
      if (logger.isDebugEnabled()) {
        logger.debug("[isAdministrator]: user: "+user.getUserName()+", type: "+user.getUserType()+".");
      }
      if (user.getUserType().equals(UserVO.UT_ADMINISTRATOR)) {
        test = true;
      }
    } catch (UserException ue) {
      throw new EJBException(ue);
    }
    return test;
  }
  /**
   * Given an individualId tell us if this individual has an associated user
   * which is a customer.
   * @param individualId
   * @return
   */
  private boolean isCustomer(int individualId)
  {
    boolean test = false;
    try {
      UserVO user = this.getUserPlain(individualId);
      if (logger.isDebugEnabled()) {
        logger.debug("[isAdministrator]: user: "+user.getUserName()+", type: "+user.getUserType()+".");
      }
      if (user.getUserType().equals(UserVO.UT_CUSTOMER)) {
        test = true;
      }
    } catch (UserException ue) {
      throw new EJBException(ue);
    }
    return test;
  }

} // end class definition

