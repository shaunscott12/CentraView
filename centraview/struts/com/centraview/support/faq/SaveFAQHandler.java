/*
 * $RCSfile: SaveFAQHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/01 15:31:03 $ - $Author: mcallist $
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

package com.centraview.support.faq;

import java.io.IOException;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.support.common.SupportConstantKeys;
import com.centraview.support.supportfacade.SupportFacade;
import com.centraview.support.supportfacade.SupportFacadeHome;

public class SaveFAQHandler extends Action
{
  /** Global Forwards for exception handling. */
  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_faq = "displayfaq";
  private static final String FORWARD_newfaq = "newfaq";

  /** Redirect constant.*/
  private static String FORWARD_final = GLOBAL_FORWARD_failure;
  static int counter = 0;

  private static Logger logger = Logger.getLogger(SaveFAQHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		if (request.getParameter("closeornew").equalsIgnoreCase("cancel"))
		{
			FORWARD_final = FORWARD_faq;
			return mapping.findForward(FORWARD_final);
		}
		HttpSession session = request.getSession();
		UserObject userObject = (UserObject) session.getAttribute("userobject");
		int individualID = userObject.getIndividualID();
		DynaActionForm faqForm = (DynaActionForm) form;
		FaqVO faqVO = new FaqVO();
		faqVO.setTitle((String) faqForm.get("title"));
		faqVO.setStatus((String) faqForm.get("status"));
		if (faqForm.get("publishToCustomerView") != null)
		{
			faqVO.setPublishToCustomerView((String) faqForm.get("publishToCustomerView"));
		}
		String faqIdStr = ((String) faqForm.get("faqid")).trim();

		if (!faqIdStr.equals(""))
		{
			faqVO.setFaqId(Integer.parseInt(faqIdStr));
		}

		SupportFacadeHome sfh = (SupportFacadeHome) CVUtility.getHomeObject("com.centraview.support.supportfacade.SupportFacadeHome",
				"SupportFacade");
		try{
			SupportFacade remote = (SupportFacade) sfh.create();
			remote.setDataSource(dataSource);

			if (request.getParameter(Constants.TYPEOFOPERATION).equals(SupportConstantKeys.ADD))
			{
				remote.addFaq(individualID, faqVO);
			}
			else if (request.getParameter(Constants.TYPEOFOPERATION).equals(SupportConstantKeys.DUPLICATE))
			{
				remote.duplicateFaq(individualID, faqVO);
			}
			else
			{
				remote.updateFaq(individualID, faqVO);
			}
		}
		catch (Exception e)
		{
			logger.error("[Exception] [SaveFAQHandler.execute Calling SurrotFacade]  ", e);
			FORWARD_final = GLOBAL_FORWARD_failure;
		}


		if (request.getParameter("closeornew").equalsIgnoreCase("new"))
		{
			FORWARD_final = FORWARD_newfaq;
		}
		else if (request.getParameter("closeornew").equalsIgnoreCase("close"))
		{
			FORWARD_final = FORWARD_faq;
		}

		request.setAttribute("refreshWindow", "true");
    return mapping.findForward(FORWARD_final);
  }
}
