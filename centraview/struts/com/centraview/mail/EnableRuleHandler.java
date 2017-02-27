/*
 * $RCSfile: EnableRuleHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:07 $ - $Author: mking_cv $
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

package com.centraview.mail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class EnableRuleHandler extends org.apache.struts.action.Action
{


  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user
    
    ActionErrors allErrors = new ActionErrors();
    String forward = "showRulesList";
    
    // "deleteRuleForm", defined in struts-config-email.xml
    DynaActionForm ruleForm = (DynaActionForm)form;

    Integer accountID = (Integer)ruleForm.get("accountID");

    try
    {
      MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
      Mail remote = (Mail)home.create();
      remote.setDataSource(dataSource);

      String ruleIDs = (String)ruleForm.get("ruleIDs");
      String status = (String)ruleForm.get("status");
      
      String[] rulesList = ruleIDs.split(",");
      
      for (int i = 0; i<rulesList.length; i++)
      {
        Integer ruleID = Integer.valueOf(rulesList[i]);
        boolean statusValue = false;
        if (status != null && status.equals("enable"))
        {
          statusValue = true;
        }
        remote.enableRule(individualID, ruleID.intValue(), statusValue);
      }
      
    }catch(Exception e){
      System.out.println("[Exception][EnableRuleHandler] Exception thrown in execute(): " + e);
      //e.printStackTrace();
    }

    StringBuffer returnPath = new StringBuffer(mapping.findForward(forward).getPath());
    if (accountID != null && accountID.intValue() > 0)
    {
      returnPath.append("?accountID="+accountID);
    }
    return(new ActionForward(returnPath.toString(), true));

  }   // end execute() method
  
}   // end class definition

