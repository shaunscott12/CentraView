/*
 * $RCSfile: SaveNewCategoryHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:50 $ - $Author: mking_cv $
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


package com.centraview.support.knowledgebase;

import java.io.IOException;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.support.supportfacade.SupportFacade;
import com.centraview.support.supportfacade.SupportFacadeHome;

public class SaveNewCategoryHandler extends org.apache.struts.action.Action {
	final String GLOBAL_FORWARD_failure = "failure";
	final String FORWARD_editsavecategory = "savecategory";
	final String FORWARD_editsaveclosecategory = "saveclosecategory";
	private static Logger logger = Logger.getLogger(SaveNewCategoryHandler.class);
	String FORWARD_final = GLOBAL_FORWARD_failure;

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException, CommunicationException, NamingException
	{
		int rowID=0;
		SupportFacadeHome supportFacade = (SupportFacadeHome)CVUtility.getHomeObject("com.centraview.support.supportfacade.SupportFacadeHome","SupportFacade");
		try {

			HttpSession session = request.getSession();
			int userId = ((UserObject)session.getAttribute("userobject")).getIndividualID();

			// call ejb to insert record
			// initialize file vo
			CategoryVO categoryVO = new CategoryVO();
			CategoryForm catForm = (CategoryForm)form;


			SupportFacade remote =(SupportFacade)supportFacade.create();

			// set the VO from form bean data
			// set the CategoryVO
			if((String)catForm.getCategoryname() != null)
				categoryVO.setTitle((String)catForm.getCategoryname());

			if((String)catForm.getParentcategory() != null)
				categoryVO.setParent(Integer.parseInt((catForm.getParentcategory()).toString()));

			categoryVO.setCreatedBy(userId);
			categoryVO.setOwner(userId);

			if (catForm.getStatus() != null)
			{
				categoryVO.setStatus((String) catForm.getStatus());
			}

			if (catForm.getPublishToCustomerView() != null)
			{
				categoryVO.setPublishToCustomerView((String) catForm.getPublishToCustomerView());
			}

			rowID=remote.insertCategory(userId,categoryVO);

			FORWARD_final=	FORWARD_editsavecategory;

			String closeornew = (String)request.getParameter("closeornew");
			String saveandclose = null;
			String saveandnew = null;
			if (closeornew.equals("close"))
				saveandclose = "saveandclose";
			else if (closeornew.equals("new")) {
				saveandnew = "saveandnew";
				catForm.setCategoryname("");
				FORWARD_final =	"newcategory";
			}

			// set refresh and closewindow flag
			if (saveandclose != null)
			request.setAttribute("closeWindow", "true");

			request.setAttribute("refreshWindow", "true");


			if (!request.getParameter("Button").equals("yes"))
			{
				if ((request.getParameter("Flag").equals("Knowledgebase")))
				{
					session.setAttribute("modulename",request.getParameter("Flag"));
					session.setAttribute("rowID",rowID+"");
					request.setAttribute("DETAILPRESS","NO");
					session.setAttribute("Type","Category");
					FORWARD_final="permission";
				}
			}
			else
			{
				request.setAttribute("DETAILPRESS","YES");
			}
		}
		catch (Exception e) {
			logger.error("[Exception] [SaveNewCategoryHandler.execute Calling SupportFacade]  ", e);
			FORWARD_final = GLOBAL_FORWARD_failure;
		}
		return mapping.findForward(FORWARD_final);
	}
}
