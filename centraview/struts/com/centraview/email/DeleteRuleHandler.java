/*
 * $RCSfile: DeleteRuleHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:49 $ - $Author: mking_cv $
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
 package com.centraview.email;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.email.emailfacade.EmailFacade;
import com.centraview.email.emailfacade.EmailFacadeHome;
import com.centraview.settings.Settings;
//	/**
//	 * @version 	1.0 Date  7/23/2003
//	 * @author Vivek T
/*	This handler is used when deleting Rule */

public final class DeleteRuleHandler extends Action
{

	String resultPage = "displayruleemail";
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)	throws IOException, ServletException
	{
	  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		try
		{
			EmailFacadeHome cfh = (EmailFacadeHome)CVUtility.getHomeObject("com.centraview.email.emailfacade.EmailFacadeHome","EmailFacade");
			EmailFacade remote =( EmailFacade )cfh.create();
      remote.setDataSource(dataSource);
			String ruleid = request.getParameter("rowId");
			HashMap hm = new HashMap();
			hm.put( "RuleID" , ruleid);
			remote.deleteRule(hm);
		}
		catch(Exception e)
		{
			resultPage = "failure";
			System.out.println("[Exception][DeleteRuleHandler.execute] Exception Thrown: "+e);
      e.printStackTrace();			
		}
		return (mapping.findForward(resultPage));
	}
}
