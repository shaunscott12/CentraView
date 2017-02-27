/*
 * $RCSfile: NewKnowledgebaseHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:50 $ - $Author: mking_cv $
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
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.UserObject;
import com.centraview.support.supportfacade.SupportFacade;
import com.centraview.support.supportfacade.SupportFacadeHome;

public class NewKnowledgebaseHandler extends org.apache.struts.action.Action {
  private static String FORWARD_final = "failure";
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    try {
      HttpSession session = request.getSession();
      int userId = ((UserObject)session.getAttribute("userobject")).getIndividualID();
      request.setAttribute("actionPath","AddKnowledgebase");
      
      // Get all categories and recurively process them for there parent/child relationships.
      SupportFacadeHome supportFacade = (SupportFacadeHome)CVUtility.getHomeObject("com.centraview.support.supportfacade.SupportFacadeHome","SupportFacade");
      SupportFacade remote =(SupportFacade)supportFacade.create();
      ArrayList flatList = (ArrayList)remote.getAllCategory(userId);
      ArrayList categoryList = new ArrayList();
      
      categoryList.add(new DDNameValue(1, "Knowledgebase"));
      Iterator iter = flatList.iterator();
      while (iter.hasNext()) {
        CategoryVO catVO = (CategoryVO)iter.next();
        if (catVO.getParent() == 1)
          KBUtil.processCategory(flatList, catVO, categoryList, 0);
      }
      
      request.setAttribute("CATEGORYVOARRAY", categoryList);
      request.setAttribute("DETAILPRESS", "YES");

      FORWARD_final = ".view.support.knowledgebase.new";
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    return (mapping.findForward(FORWARD_final));
  }
}
