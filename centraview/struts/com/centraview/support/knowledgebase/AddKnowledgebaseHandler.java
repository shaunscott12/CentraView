/*
 * $RCSfile: AddKnowledgebaseHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:48 $ - $Author: mking_cv $
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

public class AddKnowledgebaseHandler extends org.apache.struts.action.Action
{
	private static String FORWARD_final = "failure";

	private static Logger logger = Logger.getLogger(AddKnowledgebaseHandler.class);

	int rowID=0;

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException, CommunicationException, NamingException
	{
		KnowledgeBaseHome kbHome = (KnowledgeBaseHome)CVUtility.getHomeObject("com.centraview.support.knowledgebase.KnowledgeBaseHome","KnowledgeBase");
		try {
			HttpSession session = request.getSession();
			int userId = ((UserObject)session.getAttribute("userobject")).getIndividualID();

			KnowledgeVO knowledgeVO = new KnowledgeVO();
			KnowledgeBaseForm kbForm = (KnowledgeBaseForm)form;

			if((String)kbForm.getCategoryname() != null)
				knowledgeVO.setTitle((String)kbForm.getCategoryname());

			if((String)kbForm.getDetail() != null)
				knowledgeVO.setDetail((String)kbForm.getDetail());

			if((String)kbForm.getTitle() != null)
				knowledgeVO.setTitle((String)kbForm.getTitle());

			if((String)kbForm.getCategory() != null)
				knowledgeVO.setCatid(Integer.parseInt(kbForm.getCategory()));

			if((String)kbForm.getParentcategory() != null)
				knowledgeVO.setParent(Integer.parseInt((kbForm.getParentcategory()).toString()));

			if (kbForm.getStatus() != null)
			{
				knowledgeVO.setStatus((String) kbForm.getStatus());
			}

			if (kbForm.getPublishToCustomerView() != null)
			{
				knowledgeVO.setPublishToCustomerView((String) kbForm.getPublishToCustomerView());
			}

			KnowledgeBase remote = kbHome.create();
			rowID=remote.insertKB(userId,knowledgeVO);

			String closeornew = (String)request.getParameter("closeornew");

			String saveandclose = null;
			String saveandnew = null;

			if (request.getParameter("closeornew").equals("close"))	{
				request.setAttribute("closeWindow", "true");
			}
			request.setAttribute("refreshWindow", "true");

			request.setAttribute("actionPath","AddKnowledgebase");
			FORWARD_final = "savekb";

			if (request.getParameter("Button") != null && !request.getParameter("Button").equals("yes")) {
				if ((request.getParameter("Flag").equals("Knowledgebase"))) {
					session.setAttribute("modulename", request.getParameter("Flag"));
					session.setAttribute("rowID",rowID+"");
					request.setAttribute("DETAILPRESS","NO");
					session.setAttribute("Type","ategory");
					FORWARD_final="permission";
				}
			} else {
				request.setAttribute("DETAILPRESS","YES");
			}	
		} catch(Exception e) {
			logger.error("[Exception] [AddKnowledgebaseHandler.execute Calling SupportFacade] ", e);
		}
		return (mapping.findForward(FORWARD_final));
	}
}
