/*
 * $RCSfile: DisplayUserProfileHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:10 $ - $Author: mcallist $
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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.contact.helper.AddressVO;
import com.centraview.contact.individual.IndividualVO;
import com.centraview.preference.common.AdminConstantKeys;
import com.centraview.settings.Settings;

/**
* This class gets the currently logged in user's profile from
* the database and displays it in a form. The user can change
* change the details of their Individual record and also set
* a new password through this screen.OB
*/
public class DisplayUserProfileHandler extends Action
{


  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try
    {
      DynaActionForm dynaForm = (DynaActionForm)form;

      HttpSession session = request.getSession();
      UserObject userObject = (UserObject)session.getAttribute("userobject");
      int individualID = userObject.getIndividualID();

      IndividualVO individualVO;
      AddressVO addressVO = null;

      ContactFacadeHome cfh = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      ContactFacade remote = (ContactFacade)cfh.create();
      remote.setDataSource(dataSource);

      session.setAttribute("highlightmodule", "preferences");
      individualVO = remote.getIndividual(individualID);
      individualVO.populateFormBean(dynaForm);      

      if (individualVO.getUserName() != null)
      {
        dynaForm.set("userName", individualVO.getUserName());
      }
      request.setAttribute(AdminConstantKeys.PREFERENCEPAGE, "USERPROFILE");
    }catch(Exception e){
      System.out.println("[Exception][DisplayUserProfileHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return (mapping.findForward(".view.preference.user_profile"));
  }
}
