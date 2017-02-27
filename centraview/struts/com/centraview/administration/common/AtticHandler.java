/*
 * $RCSfile: AtticHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:37 $ - $Author: mking_cv $
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
 
package com.centraview.administration.common;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.centraview.administration.cvattic.AtticList;
import com.centraview.administration.cvgarbage.GarbageList;
import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;


public class AtticHandler extends DispatchAction 
{
	public ActionForward restore(ActionMapping mapping ,ActionForm form, HttpServletRequest request,HttpServletResponse response)
	{
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    
		String FORWARD_NAME = "failure";
		
		String rowId[] = null;
		String listType = null;
		
		HttpSession session = request.getSession(true);	
		rowId = request.getParameterValues("rowId");
		listType = request.getParameter("listType");
		
		if (listType.equals("CVAttic")) {
			AtticList attList = new AtticList();
			for (int i = 0; i < rowId.length; i++) {
        attList.restoreElement(rowId[i]);
      }
      FORWARD_NAME = "restoreattic";
    } else if (listType.equals("CVGarbage")) {
      GarbageList gbList = new GarbageList(dataSource);
      for (int i = 0; i < rowId.length; i++) {
        gbList.restoreElement(rowId[i]);
      }
      FORWARD_NAME = "restoregarbage";
    }
		return mapping.findForward(FORWARD_NAME);					
	}
	
	public ActionForward moveToAttic(ActionMapping mapping ,ActionForm form, HttpServletRequest request,HttpServletResponse response)
	{
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		HttpSession session = request.getSession(true);	
		String rowId[] = null;
    rowId = request.getParameterValues("rowId");
    
    GarbageList gbList = new GarbageList(dataSource);
    
    for (int i = 0; i < rowId.length; i++) {
      gbList.moveToAttic(rowId[i]);
    }
    
    request.setAttribute("typeofmodule",AdministrationConstantKeys.DATAADMINISTRATION);
    request.setAttribute("typeoflist",AdministrationConstantKeys.GARBAGE);
		request.setAttribute("typeofsubmodule",AdministrationConstantKeys.GARBAGE);
		
		return mapping.findForward("movetoattic");					
	}

}

