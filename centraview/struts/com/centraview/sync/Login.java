/*
 * $RCSfile: Login.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:29:01 $ - $Author: mking_cv $
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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;


public class Login extends Action
{

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    final String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    // we're going to be printing directly to the client,
    // so let's set the content type and get a PrintWriter
    response.setContentType("text/plain"); 
    PrintWriter writer = response.getWriter();

    // create an instance of our SyncFacade EJB
    SyncFacade syncFacade = new SyncFacade();
    syncFacade.setDataSource(dataSource);

    try
    {
      // check the user name and password provided
      boolean loginResult = syncFacade.doLogin(form, request, response);

      if (loginResult != false) {
        // SyncFacade printed the output, don't do anything here.
      }else{
        writer.print("FAIL");
      }   // end if (doLogin() !== false)
    }catch(Exception e){
      writer.print("FAIL");
    }
    
    // we're not forwarding to a jsp, so return null
    return(null);
  }   // end execute()
  
}   // end class Login definition

