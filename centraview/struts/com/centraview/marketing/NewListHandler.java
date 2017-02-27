/*
 * $RCSfile: NewListHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:17 $ - $Author: mking_cv $
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

package com.centraview.marketing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.UserObject;
import com.centraview.marketing.marketingfacade.MarketingFacade;
import com.centraview.marketing.marketingfacade.MarketingFacadeHome;
import com.centraview.settings.Settings;

/**
 * Handles the request that opens the New List screen (step 1).
 * @author Naresh Patel
 */
public class NewListHandler extends Action
{

  
  /**
   * Handles the request that opens the New List screen (step 1).
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    String FORWARD_final = ".view.marketing.newlistmanager";
    HttpSession session = request.getSession(true);

    try
    {
      MarketingFacadeHome aa = (MarketingFacadeHome)CVUtility.getHomeObject("com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
      MarketingFacade remote = (MarketingFacade)aa.create();
      remote.setDataSource(dataSource);
      String rowStringId = null;
      String existingListName = null;
      boolean addToList = false;

      if (request.getParameter("listId") != null)
      {
        rowStringId = request.getParameter("listId");
      }else if (request.getAttribute("listId") != null){
        rowStringId = (String)request.getAttribute("listId");
      }   // end if (request.getAttribute("listId") !=null)

      try
      {
        Integer.parseInt(rowStringId);
        addToList = true;
      }catch(Exception ignored){
        //ignored
      }

      Collection marketList = remote.getAllList();
      Collection marketingList = new ArrayList();
      marketingList.add(new DDNameValue("-1", "New Marketing List"));

      Iterator iterator = marketList.iterator();

      Vector marketingListColumn = new Vector(marketList);

      if (marketingListColumn != null)
      {
        for (Enumeration e = marketingListColumn.elements(); e.hasMoreElements();)
        {
          HashMap hm = (HashMap)e.nextElement();

          String title = (String)hm.get("title").toString();
          String listID = (String)hm.get("ListID").toString();
          marketingList.add(new DDNameValue(listID, title));
          if (addToList && rowStringId.equals(listID))
          {
            existingListName = title;
          }
        } //end for (Enumeration e = col2.elements() ; e.hasMoreElements() ;)
      } //end if (marketingListColumn != null)

      request.setAttribute("marketingList", marketingList);
      PopulateMarketingForm populateMarketingForm = new PopulateMarketingForm();

      // set the form elements
      populateMarketingForm.resetForm(request, response, form);
      ListMemberForm listMemberForm = (ListMemberForm)form;
      UserObject userObject = (UserObject)session.getAttribute("userobject");
      String firstname ="";
      String lastname = "";
      String loginname ="";
      if (userObject != null)
      {
	      firstname = userObject.getfirstName();
	      lastname = userObject.getlastName();
	      loginname = userObject.getLoginName();
	      listMemberForm.setOwnername(firstname+" "+lastname );
	      int userid = userObject.getIndividualID();
	      listMemberForm.setOwner(userid+"");
      }
      if (addToList)
      {
        listMemberForm.setListname(existingListName);
        listMemberForm.setMasterlistid(rowStringId);
      }
      session.setAttribute("importListForm", listMemberForm);
    }catch(Exception e){
      System.out.println("[Exception] ViewListHandler.execute: " + e.toString());
      //e.printStackTrace();
    } //end of catch block (Exception)
    return mapping.findForward(FORWARD_final);
  } //end of execute method
  
} //end of NewListHandler class

