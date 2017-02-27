/*
 * $RCSfile: ContactDelete.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:29:00 $ - $Author: mking_cv $
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


package com.centraview.sync;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.authorization.Authorization;
import com.centraview.administration.authorization.AuthorizationHome;
import com.centraview.common.CVUtility;
import com.centraview.common.ListGenerator;
import com.centraview.common.UserObject;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.settings.Settings;

public class ContactDelete extends Action
{

  
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    response.setContentType("text/plain");
    PrintWriter writer = response.getWriter();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user

    // "sycnContactForm", defined in struts-config-sync.xml
    DynaActionForm contactForm = (DynaActionForm)form;
    SyncUtils syncUtils = new SyncUtils();

    try
    {
      // check to see if CompanionLink Agent has signed in
      if (syncUtils.checkSession(userObject, contactForm) == false)
      {
        writer.print("FAIL: You are not logged in.");
        return(null);
      }
      
      // decode all characters that are encoded by CompanionLink Agent
      contactForm = syncUtils.parseSpecialChars(contactForm);

      int contactID = 0;
      String formContactID = (String)contactForm.get("contactID");
      if (formContactID != null && ! formContactID.equals(""))
      {
        try
        {
          contactID = Integer.parseInt(formContactID);
        }catch(NumberFormatException nfe){
          writer.print("FAIL: Invalid contact ID specified.");
          return(null);
        }
      }else{
        writer.print("FAIL: Invalid contact ID specified.");
        return(null);
      }

      try
      {
        // check to see if the user has the right to update this record
        AuthorizationHome authHome = (AuthorizationHome)CVUtility.getHomeObject("com.centraview.administration.authorization.AuthorizationHome", "Authorization");
        Authorization authRemote = (Authorization)authHome.create();
        
        if (! authRemote.canPerformRecordOperation(individualID, "Individual", contactID, 10))
        {
          writer.print("FAIL: You do not have permission to delete this record.");
          return(null);
        }
      }catch(Exception e){
        System.out.println("[Exception][SyncFacade] Exception thrown in deleteContact(): " + e);
        writer.print("FAIL: Unknown error occurred.");
        return(null);
      }

      ContactFacadeHome cfh = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome","ContactFacade");
      ContactFacade remote = (ContactFacade)cfh.create();
      remote.setDataSource(dataSource);

      // deleteIndividual() returns void
      // I guess, if no exception is thrown, then everything is good
      remote.deleteIndividual(contactID, individualID);
      
      ListGenerator lg = ListGenerator.getListGenerator(dataSource);
      lg.makeListDirty("Individual");

      writer.print(contactID);
    }catch(Exception e){
      writer.print("FAIL");
      return(null);
    }

    // we're not forwarding to a jsp, so return null
    return(null);
  }   // end execute()
  
}   // end class ContactDelete definition

