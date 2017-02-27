/*
 * $RCSfile: ViewCategoryHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/01 15:31:05 $ - $Author: mcallist $
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
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

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

import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.common.DDNameValue;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.support.common.SupportConstantKeys;
import com.centraview.support.supportfacade.SupportFacade;
import com.centraview.support.supportfacade.SupportFacadeHome;

/**
 * @author CentraView, LLC.
 */
public class ViewCategoryHandler extends Action
{
  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_duplicatecategory = "duplicatecategory";
  private static final String FORWARD_newcategory = ".view.support.knowledgebase.editcat";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  private int catid = 0;

	private static Logger logger = Logger.getLogger(ViewCategoryHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    SupportFacadeHome supportFacade = (SupportFacadeHome)
        CVUtility.getHomeObject("com.centraview.support.supportfacade.SupportFacadeHome", "SupportFacade");
    try {
      HttpSession session = request.getSession();
      UserObject userObject = (UserObject) session.getAttribute("userobject");
      int individualID = userObject.getIndividualID();
      CategoryForm catForm = (CategoryForm) form;

      int catid = 1;
      String rowId = request.getParameter("rowId");
      if (rowId != null && !rowId.equals("")) {
		    if (rowId.indexOf("*") > -1) {
		      String elements[] = rowId.split("\\*");
		      if (elements.length != 2)
		        throw new Exception("No row selected.");
		      catid = Integer.parseInt(elements[0]);
		    } else {
			    catid = Integer.parseInt(rowId);
		    }
		  }

      String categoryId = Integer.toString(catid);
      request.setAttribute("rowId", categoryId);

      SupportFacade remote = (SupportFacade) supportFacade.create();
      remote.setDataSource(dataSource);
      CategoryVO catVO = remote.getCategory(individualID, catid);

      if (catVO == null)
        throw new Exception("Bad category");
      catForm.setCategoryname(catVO.getTitle());
      catForm.setParentcatid(catVO.getParent());
      
      // Format created time
      Timestamp timeStamp = catVO.getCreatedOn();
      if (timeStamp != null){
        Date d = new Date(timeStamp.getTime());
        DateFormat df = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        catForm.setCreated(df.format(d));
      }
      
      // Format modified time
      timeStamp = catVO.getModifiedOn();
      if (timeStamp != null){
        Date d = new Date(timeStamp.getTime());
        DateFormat df = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        catForm.setModified(df.format(d));
      }
      
      catForm.setOwner(catVO.getOwnerVO().getFirstName() + " "
        + catVO.getOwnerVO().getLastName());
      catForm.setOwnerid(new Integer(catVO.getCreatedBy()).toString());
			catForm.setStatus(catVO.getStatus());
			catForm.setPublishToCustomerView(catVO.getPublishToCustomerView());
      request.setAttribute("catform", catForm);

      String closeornew = (String) request.getParameter("closeornew");

      if (closeornew != null)
      {
        if (closeornew.equals("new"))
        {
          catForm.setCategoryname("");
        }
      }

      ArrayList flatList = (ArrayList)remote.getAllCategory(individualID);
      ArrayList categoryList = new ArrayList(); 
      categoryList.add(new DDNameValue(1, "Knowledgebase"));
      Iterator iter = flatList.iterator();
      while (iter.hasNext()) {
        CategoryVO cVO = (CategoryVO)iter.next();
        if (cVO.getParent() == 1)
          KBUtil.processCategory(flatList, cVO, categoryList, 0);
      }
      
      request.setAttribute("CATEGORYVOARRAY", categoryList);

      if (((String) request.getAttribute(Constants.TYPEOFOPERATION)) != null)
      {
        if (((String) request.getAttribute(Constants.TYPEOFOPERATION))
            .equalsIgnoreCase(SupportConstantKeys.DUPLICATE))
        {
          FORWARD_final = FORWARD_duplicatecategory;
        }
      }
      else
      {
        FORWARD_final = FORWARD_newcategory;
      }
    }
    catch (Exception e)
    {
			logger.error("[Exception] [ViewCategoryHandler.execute Calling SupportFacade]  ", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }
}
