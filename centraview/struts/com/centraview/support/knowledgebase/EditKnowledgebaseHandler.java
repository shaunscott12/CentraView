/*
 * $RCSfile: EditKnowledgebaseHandler.java,v $    $Revision: 1.3 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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

/**
 * EditKnowledgebaseHandler.java
 *
 * @author CentraView, LLC.
 */
public class EditKnowledgebaseHandler extends Action
{
  // Global Forwards
  public static final String GLOBAL_FORWARD_failure = "failure";

  // Local Forwards
  private static String FORWARD_final = GLOBAL_FORWARD_failure;
  private static Logger logger = Logger.getLogger(EditKnowledgebaseHandler.class);


  public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		KnowledgeBaseHome kbHome = (KnowledgeBaseHome)
				CVUtility.getHomeObject("com.centraview.support.knowledgebase.KnowledgeBaseHome", "KnowledgeBase");
    try
    {
      HttpSession session = request.getSession();
      UserObject userObject = (UserObject) session.getAttribute("userobject");
      int individualID = userObject.getIndividualID();
      int kbId = Integer.parseInt((String) request.getParameter("rowId"));

      KnowledgeVO knowledgeVO = new KnowledgeVO();
      KnowledgeBaseForm kbForm = (KnowledgeBaseForm) form;
      knowledgeVO.setKbid(kbId);

      if (kbForm.getCategoryname() != null)
      {
        knowledgeVO.setTitle((String) kbForm.getCategoryname());
      }

      if (kbForm.getDetail() != null)
      {
        knowledgeVO.setDetail((String) kbForm.getDetail());
      }

      if (kbForm.getTitle() != null)
      {
        knowledgeVO.setTitle((String) kbForm.getTitle());
      }

      if (kbForm.getCategory() != null)
      {
        knowledgeVO.setCatid(Integer.parseInt(kbForm.getCategory()));
      }

      if (kbForm.getStatus() != null)
      {
        knowledgeVO.setStatus((String) kbForm.getStatus());
      }

      if (kbForm.getPublishToCustomerView() != null)
      {
        knowledgeVO.setPublishToCustomerView((String) kbForm.getPublishToCustomerView());
      }

      KnowledgeBase remote = kbHome.create();
      remote.setDataSource(dataSource);
      String typeOfOperation = (String) request.getParameter(Constants.TYPEOFOPERATION);

      if (typeOfOperation != null)
      {
        if (typeOfOperation.equalsIgnoreCase(SupportConstantKeys.DUPLICATE))
        {
          remote.insertKB(individualID, knowledgeVO);
        }
      }
      else
      {
				try{
        	remote.updateKB(individualID, knowledgeVO);
				}
				catch(KBException e){
					ActionErrors allErrors = new ActionErrors();
					String errorMessage = e.getExceptionDescription();
					if (errorMessage != null && !errorMessage.equals("")){
						allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", errorMessage));
						saveErrors(request, allErrors);
						FORWARD_final = "newkb";
						return (mapping.findForward(FORWARD_final));
					}// end of if (startOfError >= 0 && endOfError >= 0)

				}
      }

      FORWARD_final = "editfolder";
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
        request.setAttribute("typeofoperation", "editkb");
        request.setAttribute("show", "new");
        FORWARD_final = "newkb";
      }

      if (saveandclose != null)
      {
        request.setAttribute("closeWindow", "true");
      }
      request.setAttribute("refreshWindow", "true");
    }
    catch (Exception e)
    {
			logger.error("[Exception] [EditKnowledgebaseHandler.execute]  ", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return (mapping.findForward(FORWARD_final));
  }
}
