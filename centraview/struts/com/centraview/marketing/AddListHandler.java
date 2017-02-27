/*
 * $RCSfile: AddListHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:12 $ - $Author: mking_cv $
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
import com.centraview.common.ListGenerator;
import com.centraview.common.UserObject;
import com.centraview.marketing.List.ListVO;
import com.centraview.marketing.marketingfacade.MarketingFacade;
import com.centraview.marketing.marketingfacade.MarketingFacadeHome;
import com.centraview.settings.Settings;

public class AddListHandler extends Action
{
  private static Logger logger = Logger.getLogger(AddListHandler.class);

  /**
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
  	throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession(false);
    UserObject userobjectd = (UserObject) session.getAttribute("userobject");
    int individualID = userobjectd.getIndividualID();

    request.setAttribute("TypeOfOperation", "List Manager");

    ListMemberForm dynaForm = (ListMemberForm) form;

    String masterListID = dynaForm.getMasterlistid();
    int listID = 0;

    if (masterListID != null)
    {
      listID = Integer.parseInt(masterListID);
    }else if (request.getParameter("masterlistid") != null){
      listID = Integer.parseInt(request.getParameter("masterlistid"));
    }

    if (listID == -1)
    {
      listID = this.saveList(form, individualID);
    }

    session.setAttribute("listid", "" + listID);

    // populate form bean for previous sub-activity
    PopulateMarketingForm populateMarketingForm = new PopulateMarketingForm();

    // set the form elements
    populateMarketingForm.resetForm(request, response, form);

    ListGenerator lg = ListGenerator.getListGenerator(dataSource);
    lg.makeListDirty("Marketing");

    return (mapping.findForward(".view.marketing.newlistmanager"));
  }

  public int saveList(ActionForm form, int userid) throws CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    int listid = 0;
		MarketingFacadeHome aa = (MarketingFacadeHome)CVUtility.getHomeObject("com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
    try
    {
      MarketingFacade remote = (MarketingFacade)aa.create();
      remote.setDataSource(dataSource);
	ListMemberForm listMemberForm = (ListMemberForm) form;
	ListVO listVO = new ListVO();
	listVO.setTitle(listMemberForm.getListname());
	listVO.setDescription(listMemberForm.getListdescription());
	if(listMemberForm.getOwner() != null){
		int ownerID = Integer.parseInt(listMemberForm.getOwner());
		listVO.setOwnerID(ownerID);
	}
      listid = remote.addList(userid, listVO);
    }catch (Exception e){
      logger.error("[Exception] AddListHandler.Execute Handler ", e);
    }
    return listid;
  }   // end saveList() method
  
}   // end class definition

