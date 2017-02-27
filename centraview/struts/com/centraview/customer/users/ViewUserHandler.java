/*
 * $RCSfile: ViewUserHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:08 $ - $Author: mcallist $
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

package com.centraview.customer.users;

import java.util.Collection;
import java.util.Iterator;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.user.User;
import com.centraview.administration.user.UserHome;
import com.centraview.administration.user.UserVO;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.contact.helper.AddressVO;
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.settings.Settings;

/**
 * Handles the request for the Customer View User Details
 * screen. Displays User only, no modification allowed.
 */
public class ViewUserHandler extends Action
{

  private static Logger logger = Logger.getLogger(ViewUserHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String forward = ".view.customer.view_user";

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user
    int entityID = userObject.getEntityId();    // entityID of the logged-in user's entity

    ActionErrors allErrors = new ActionErrors();

    // "customerUserForm", defined in cv-struts-config.xml
    DynaActionForm userForm = (DynaActionForm)form;

    try {
      // get the user ID from the form bean
      Integer formUserID = (Integer)userForm.get("userID");
      // create an int to hold the user ID value
      int userID = 0;

      // now, check the user ID on the form...
      if (formUserID == null) {
        // if user ID is not set on the form, then there is
        // no point in continuing forward. Show the user an
        // error and quit.
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "User ID"));
        return(mapping.findForward(forward));
      }else{
        // if user ID is set on the form properly, then set
        // the int representation for use in the code below
        userID = formUserID.intValue();
      }

      UserHome home = (UserHome)CVUtility.getHomeObject("com.centraview.administration.user.UserHome", "User");
      User remote = (User)home.create();
      remote.setDataSource(dataSource);

      UserVO userVO = remote.getCustomerUserFull(userID);

      String fullName = userVO.getFirstName() + " " + userVO.getLastName();
      userForm.set("individualName", fullName);
      userForm.set("username", userVO.getLoginName());
      userForm.set("title", userVO.getTitle());
      userForm.set("userID", new Integer(userVO.getUserId()));

      AddressVO primaryAddress = userVO.getPrimaryAddress();
      if (primaryAddress != null) {
        userForm.set("street1", primaryAddress.getStreet1());
        userForm.set("street2", primaryAddress.getStreet2());
        userForm.set("city", primaryAddress.getCity());
        userForm.set("state", primaryAddress.getStateName());
        userForm.set("zipCode", primaryAddress.getZip());
        userForm.set("country", primaryAddress.getCountryName());
      }
      
      Collection mocList = userVO.getMOC();
      Iterator iterator = mocList.iterator();
      int count = 1;
      while (iterator.hasNext()) {
        MethodOfContactVO moc = (MethodOfContactVO)iterator.next();
        if (moc.getMocType() == 1 && moc.getIsPrimary().equalsIgnoreCase("YES")) {
          userForm.set("email", moc.getContent());
        } else if (count < 4 && moc.getMocType() != 1) {
          userForm.set("mocType" + count, new Integer(moc.getMocType()).toString());
          String mocContent = moc.getContent();
          String mocContentValue = "";
          String mocContentExt = "";
          
          if (mocContent.indexOf("EXT")  != -1)  {
            String tempContent = mocContent;
            mocContentValue = tempContent.substring(0,tempContent.indexOf("EXT"));
            mocContentExt = tempContent.substring(tempContent.indexOf("EXT")+3,tempContent.length())  ;
          } else{
            mocContentValue = mocContent;
          }
          userForm.set("mocContent" + count, mocContentValue );
          userForm.set("mocExt" + count, mocContentExt );
          count++;
        }
      }
    }catch(Exception e){
      logger.error("[Exception] ViewUserHandler.Execute Handler ", e);
    }
    return(mapping.findForward(forward));
  }   // end execute() method

}   // end class definition


