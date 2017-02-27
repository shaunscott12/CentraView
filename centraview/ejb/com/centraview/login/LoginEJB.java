/*
 * $RCSfile: LoginEJB.java,v $    $Revision: 1.2 $  $Date: 2005/07/25 13:26:08 $ - $Author: mcallist $
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

package com.centraview.login;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.common.Password;
import com.centraview.common.UserObject;
import com.centraview.common.UserPrefererences;
import com.centraview.contact.contactfacade.ContactFacadeLocal;
import com.centraview.contact.contactfacade.ContactFacadeLocalHome;
import com.centraview.contact.entity.EntityVO;
import com.centraview.contact.helper.AddressVO;
import com.centraview.preference.PreferenceLocal;
import com.centraview.preference.PreferenceLocalHome;

/**
 * LoginEJB
 */
public class LoginEJB implements SessionBean {
  protected javax.ejb.SessionContext ctx;
  protected Context environment;
  private String dataSource = "";
  private static Logger logger = Logger.getLogger(LoginEJB.class);

  /**
   * @param userName
   * @param password
   * @return
   */
  public HashMap authenticateUser(String userName, String password)
  {
    HashMap loginResults = authenticateUserLogin(userName, password, "EMPLOYEE");

    String error = (String) loginResults.get("error");

    if (error != null && error.equals("logon.invalid")) {
      // for now if we got an error try again as Administrator
      loginResults = authenticateUserLogin(userName, password, "ADMINISTRATOR");

      String errortwo = (String) loginResults.get("error");
      if (errortwo != null && errortwo.equals("logon.invalid")) {
        loginResults = authenticateUserLogin(userName, password, "CUSTOMER");
        loginResults.put("type", "CUSTOMER");
      } else {
        loginResults.put("type", "ADMINISTRATOR");
      }
    } else {
      loginResults.put("type", "EMPLOYEE");
    }
    return loginResults;
  } // end authenticateUser() method

  public HashMap authenticateUserCustomer(String userName, String password)
  {
    return authenticateUserLogin(userName, password, "CUSTOMER");
  }

  public HashMap getForgottenPassword(String userName, String email)
  {
    HashMap returnFPHm = null;
    String strPassword = "";
    String firstName = "";
    String lastName = "";
    int individualID;
    int userid;

    CVDal fPdl = new CVDal(dataSource);
    try {

      // for individualID and password
      fPdl.setSql("user.getindividual");
      fPdl.setString(1, userName);
      Vector vec = (Vector) fPdl.executeQuery();
      fPdl.clearParameters();
      returnFPHm = new HashMap();
      if (vec.size() == 0)
        returnFPHm.put("status", "forgottenpassword.invalid");

      HashMap hm = (HashMap) vec.elementAt(0);
      strPassword = (String) hm.get("password");
      if ((strPassword != null)) {
        Number lngObj = (Number) hm.get("UserID");
        userid = lngObj.intValue();
        returnFPHm.put("userid", new Integer(userid));

        Number individualObj = (Number) hm.get("IndividualID");
        individualID = individualObj.intValue();
        returnFPHm.put("individualID", new Integer(individualID));

        // for firstname and lastname
        fPdl.setSql("user.getinfo");
        fPdl.setInt(1, individualID);
        Vector vecUserInfo = (Vector) fPdl.executeQuery();
        fPdl.clearParameters();
        if (vecUserInfo.size() != 0) {
          HashMap hmUserInfo = (HashMap) vecUserInfo.elementAt(0);
          firstName = (String) hmUserInfo.get("firstName");
          lastName = (String) hmUserInfo.get("lastName");
        } else
          returnFPHm.put("status", "forgottenpassword.invalid");

        // for user email
        fPdl.setSql("user.forgottenpassword");
        fPdl.setInt(1, individualID);
        Collection col = fPdl.executeQuery();
        if (col.size() < 1) {
          returnFPHm.put("noemail", "1");
        }
        Iterator it = col.iterator();
        HashMap fphm = new HashMap();
        while (it.hasNext()) {
          fphm = (HashMap) it.next();
          if (((String) fphm.get("content")).equals(email)) {
            returnFPHm.put("status", "forgottenpassword.success");
            returnFPHm.put("firstName", firstName);
            returnFPHm.put("lastName", lastName);
            break;
          }
        }
        fPdl.clearParameters();
      } else {
        returnFPHm.put("status", "forgottenpassword.invalid");
      }
    } catch (Exception e) {
      logger.error("[getForgottenPassword]: Exception", e);
    } finally {
      fPdl.clearParameters();
      fPdl.destroy();
      fPdl = null;
    }
    return returnFPHm;
  }

  private HashMap authenticateUserLogin(String userName, String password, String userType)
  {
    HashMap returnHm = null;
    int individualid;

    String firstName;
    String lastName;

    CVDal dl = new CVDal(dataSource);
    try {
      dl.setSql("common.login");
      dl.setString(1, userName);
      dl.setString(2, userType);

      Vector vec = (Vector) dl.executeQuery();
      dl.clearParameters();
      returnHm = new HashMap();

      if (vec.size() == 0) {
        returnHm.put("error", "logon.invalid");
        return returnHm;
      }
      HashMap hm = (HashMap) vec.elementAt(0);
      String strPassword = (String) hm.get("Password");

      Password passwordService = Password.getInstance();
      String encryptedPassword = passwordService.encrypt(password);

      if ((strPassword != null) && strPassword.equals(encryptedPassword)) {
        dl.setSql("common.user.details");

        Long lngObj = (Long) hm.get("IndividualID");
        individualid = lngObj.intValue();
        dl.setInt(1, lngObj.intValue());

        Vector vec1 = (Vector) dl.executeQuery();
        HashMap hm1 = (HashMap) vec1.elementAt(0);

        firstName = (String) hm1.get("FirstName");
        lastName = (String) hm1.get("LastName");
        returnHm.put("individualid", new Integer(individualid).toString());
        returnHm.put("firstName", firstName);
        returnHm.put("lastName", lastName);
      } else {
        returnHm.put("error", "logon.invalid");
      }
    } catch (Exception e) {
      logger.error("[authenticateUserLogin]: Exception", e);
    } finally {
      dl.clearParameters();
      dl.destroy();
      dl = null;
    }
    return returnHm;
  }

  /**
   * Set the associated session context. The container calls this method after
   * the instance test creation. The enterprise Bean instance should store the
   * reference to the context object in an instance variable. This method is
   * called with no transaction context. @param ctx
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

  public UserObject getUserObject(int individualId, String firstName, String lastName, String userType)
  {
    UserPrefererences up = new UserPrefererences();
    try {
      InitialContext ic = CVUtility.getInitialContext();
      PreferenceLocalHome home = (PreferenceLocalHome) ic.lookup("local/Preference");
      PreferenceLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      up = remote.getUserPreferences(individualId);
    } catch (Exception e) {
      logger.error("[getUserObject]: Exception", e);
    }
    UserObject userObj = new UserObject(individualId, firstName, lastName, up, userType);

    if (individualId != 0) {
      userObj.setIndividualID(individualId);
      try {
        InitialContext ic = CVUtility.getInitialContext();
        ContactFacadeLocalHome home = (ContactFacadeLocalHome) ic.lookup("local/ContactFacade");
        ContactFacadeLocal remote = home.create();
        remote.setDataSource(this.dataSource);
        int entityID = remote.getEntityIDForIndividual(individualId);
        EntityVO entityVO = remote.getEntity(entityID);
        AddressVO primaryAdd = entityVO.getPrimaryAddress();
        userObj.setEntityName(entityVO.getName());
        userObj.setEntityId(entityID);
        userObj.setAddressVO(primaryAdd);
      } catch (Exception e) {
        logger.error("[getUserObject]: Exception", e);
      }
    }
    return userObj;
  }
}
