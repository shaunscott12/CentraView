/*
 * $RCSfile: InsertCategoryHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:50 $ - $Author: mking_cv $
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.support.supportfacade.SupportFacade;
import com.centraview.support.supportfacade.SupportFacadeHome;

/**
 *  EditFileHandler is used to update file information.
 *
 *  @author Ashwin Nagar
 */
public class InsertCategoryHandler extends Action
{

  
  /** Global Forwards for exception handling. */
  final String GLOBAL_FORWARD_failure = "failure";

  /** Local Forwards for redirecting to jsp list_common_c. */
  final String FORWARD_editsavefile = "savekb";

  /** Local Forwards for redirecting to jsp list_common_c.*/
  final String FORWARD_editsaveclosefile = "saveclosekb";

  /** Redirect constant. */
  String FORWARD_final = GLOBAL_FORWARD_failure;

  /** Default Constructor. */
  public InsertCategoryHandler()
  {
    //empty constructor body
  }

  /**
   *  Executes initialization of required parameters and open window for entry of note
   *  returns ActionForward
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try
    {
      HttpSession session = request.getSession();
      UserObject userObject = (UserObject) session.getAttribute("userobject");
      int individualID = userObject.getIndividualID();
      CategoryVO categoryVO = new CategoryVO();
      CategoryForm catForm = (CategoryForm) form;

      SupportFacadeHome supportFacade = (SupportFacadeHome) CVUtility
        .getHomeObject("com.centraview.support.supportfacade.SupportFacadeHome",
          "SupportFacade");
      SupportFacade remote = (SupportFacade) supportFacade.create();
      remote.setDataSource(dataSource);

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
      }

      if (saveandclose != null)
      {
        request.setAttribute("closeWindow", "true");
      }
      request.setAttribute("refreshWindow", "true");

      if (catForm.getCategoryname() != null)
      {
        categoryVO.setTitle(catForm.getCategoryname());
      }

      if (catForm.getParentcategory() != null)
      {
        categoryVO.setParent(Integer.parseInt(catForm.getParentcategory()));
      }
      categoryVO.setCreatedBy(individualID);
      categoryVO.setOwner(individualID);
      remote.insertCategory(individualID, categoryVO);

      FORWARD_final = FORWARD_editsavefile;
    }
    catch (Exception e)
    {
      System.out.println("[Exception] InsertCategoryHandler.execute: " + e.toString());
      //e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }
}
