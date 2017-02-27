/*
 * $RCSfile: EditCategoryHandler.java,v $    $Revision: 1.3 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.support.common.SupportConstantKeys;
import com.centraview.support.supportfacade.SupportFacade;
import com.centraview.support.supportfacade.SupportFacadeHome;

/**
 *
 * @author CentraView, LLC.
 */
public class EditCategoryHandler extends Action
{


  /** Global Forwards for exception handling. */
  final String GLOBAL_FORWARD_failure = "failure";

  /** Local Forwards for redirecting to jsp list_common_c. */
  final String FORWARD_editsavecategory = "savecategory";

  /** Local Forwards for redirecting to jsp list_common_c. */
  final String FORWARD_editsaveclosecategory = "saveclosecategory";

  /** Redirect constant.  */
  String FORWARD_final = GLOBAL_FORWARD_failure;

	private static Logger logger = Logger.getLogger(EditCategoryHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		SupportFacadeHome supportFacade = (SupportFacadeHome) CVUtility
			.getHomeObject("com.centraview.support.supportfacade.SupportFacadeHome",
				"SupportFacade");
    try
    {
      HttpSession session = request.getSession();
      UserObject userObject = (UserObject) session.getAttribute("userobject");
      int individualID = userObject.getIndividualID();
      CategoryVO categoryVO = new CategoryVO();
      CategoryForm catForm = (CategoryForm) form;
      int catid;
      String typeOfOperation = (String) request.getParameter(Constants.TYPEOFOPERATION);

      catid = Integer.parseInt(request.getParameter("rowId"));

      if ((typeOfOperation != null)
          && typeOfOperation.equalsIgnoreCase(SupportConstantKeys.DUPLICATE)) {
        categoryVO.setOwner(individualID);
      }

      categoryVO.setCatid(catid);


      SupportFacade remote = (SupportFacade) supportFacade.create();
      remote.setDataSource(dataSource);

      if ((String) catForm.getCategoryname() != null)
      {
        categoryVO.setTitle((String) catForm.getCategoryname());
      }

      if ((String) catForm.getParentcategory() != null)
      {
        categoryVO.setParent(Integer.parseInt(
            (catForm.getParentcategory()).toString()));
      }

      if (catForm.getStatus() != null)
      {
        categoryVO.setStatus((String) catForm.getStatus());
      }

      if (catForm.getPublishToCustomerView() != null)
      {
        categoryVO.setPublishToCustomerView((String) catForm.getPublishToCustomerView());
      }

      FORWARD_final = FORWARD_editsavecategory;
      String closeornew = (String) request.getParameter("closeornew");
      String saveandclose = null;
      String saveandnew = null;

      if (closeornew.equals("close"))
      {
        saveandclose = "saveandclose";
      }
      else if (closeornew.equals("new"))
      {
        saveandnew = "saveandnew";
        catForm.setCategoryname("");
        FORWARD_final = "viewcategory";
      }

      // set refresh and closewindow flag
      if (saveandclose != null)
      {
        request.setAttribute("closeWindow", "true");
      }
      request.setAttribute("refreshWindow", "true");

      if ((typeOfOperation != null)
          && typeOfOperation.equalsIgnoreCase(SupportConstantKeys.DUPLICATE))
      {
        remote.duplicateCategory(individualID, categoryVO);
      }
      else
      {
        try
        {
          categoryVO.setModifiedBy(individualID);
          remote.updateCategory(individualID, categoryVO);
        }
        catch (KBException kbex)
        {

					ActionErrors allErrors = new ActionErrors();
					String errorMessage = kbex.getExceptionDescription();
					if (errorMessage != null && !errorMessage.equals("")){
						allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", errorMessage));
						saveErrors(request, allErrors);
					}// end of if (startOfError >= 0 && endOfError >= 0)

          saveandnew = "saveandnew";
          request.setAttribute("closeWindow", "false");
          FORWARD_final = "viewcategory";
          logger.error("[Exception] [EditCategoryHandler.execute Calling SupportFacade]  ", kbex);
        }
      }
    }
    catch (Exception e)
    {
			logger.error("[Exception] [EditCategoryHandler.execute Calling SupportFacade]  ", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }
}
