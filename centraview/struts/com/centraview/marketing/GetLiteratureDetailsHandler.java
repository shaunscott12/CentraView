/*
 * $RCSfile: GetLiteratureDetailsHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:13 $ - $Author: mking_cv $
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

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.UserObject;
import com.centraview.marketing.marketingfacade.MarketingFacade;
import com.centraview.marketing.marketingfacade.MarketingFacadeHome;
import com.centraview.settings.Settings;

public class GetLiteratureDetailsHandler extends org.apache.struts.action.Action
{

  // Global Forwards
  public static final String GLOBAL_FORWARD_failure = "failure";

  // Local Forwards
  private static final String FORWARD_newliterature = ".view.marketing.literaturefulfillment.detail";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public GetLiteratureDetailsHandler()
  {}

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try
    {
      // get session
      HttpSession session = request.getSession();
      // hash map for add literature
      HashMap literatureHashMap = null;
      // to identify multiple opened window
      String newWindowId = "1";
      int activityid = Integer.parseInt(request.getParameter("activityid").toString());

      int individualId = ((UserObject)session.getAttribute("userobject")).getIndividualID();

      LiteratureForm literatureform = new LiteratureForm();

      MarketingFacadeHome home = (MarketingFacadeHome)CVUtility.getHomeObject("com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
      MarketingFacade remote = (MarketingFacade)home.create();
      remote.setDataSource(dataSource);
      LiteratureDetails literatureDetails = remote.getLiteratureDetails(activityid,individualId);
      literatureform.setAssignedtoid("" + literatureDetails.getAssignedtoid());
      literatureform.setDeliverymethodid("" + literatureDetails.getDeliveryid());
      literatureform.setEntityid("" + literatureDetails.getEntityid());
      literatureform.setIndividualid("" + literatureDetails.getIndividualid());
      literatureform.setStatusid("" + literatureDetails.getStatusid());
      literatureform.setDetail(literatureDetails.getDetail());
      literatureform.setLiteraturename(literatureDetails.getLiteratureName());
      literatureform.setLiteratureid(literatureDetails.getLiteratureId());
      literatureform.setTitle(literatureDetails.getTitle());
      literatureform.setAssignedtoname(literatureDetails.getAssignedtoname());
      literatureform.setIndividualname(literatureDetails.getIndividualname());
      literatureform.setEntityname(literatureDetails.getEntityname());
      Vector literaturenamevec = new Vector();
      String strLiteratureid = (String)literatureDetails.getLiteratureId();
      String strLiteraturename = (String)literatureDetails.getLiteratureName();
      String strnames = "";

      if (strLiteratureid != null && !strLiteratureid.equals(""))
      {
		StringTokenizer stid = new StringTokenizer(strLiteratureid,",");
		StringTokenizer stName = new StringTokenizer(strLiteraturename,",");
		String strId = "";
		String strName = "";
		while(stid.hasMoreTokens())
		{
			strId = stid.nextToken();
			strName = stName.nextToken();
			if (!strId.equals("")){
				int id = Integer.parseInt(strId);
				literaturenamevec.add(new DDNameValue(id,strName));
			}
		}
      }

      literatureform.setLiteraturenamevec(literaturenamevec);
      literatureform.setNames(strnames);

      if (literatureDetails.getDuebydate() != null)
      {
        Timestamp date = (Timestamp)literatureDetails.getDuebydate();

        String strMonth = "" + (date.getMonth() + 1);
        String strDay = "" + date.getDate();
        String strYear = "" + (date.getYear() + 1900);
        String strHours = "" + date.getHours();
        String strMins = "" + date.getMinutes();
        int hhmm[] = new int[2];
        hhmm[0] = Integer.parseInt(strHours.trim());
        hhmm[1] = Integer.parseInt(strMins.trim());
        String strTime = CVUtility.convertTime24HrsFormatToStr(hhmm);
        literatureform.setDuebymonth(strMonth);
        literatureform.setDuebyday(strDay);
        literatureform.setDuebyyear(strYear);
        literatureform.setDuebytime(strTime);
        literatureform.setSelduebytime(strTime);
      }

      Vector deliverymethodlist = remote.getAllDeliveryMethod();
      request.setAttribute("deliverymethodlist", deliverymethodlist);

      Vector activitystatuslist = remote.getAllActivityStatus();
      request.setAttribute("activitystatuslist", activitystatuslist);

      form = (ActionForm)literatureform;

      // set request
      request.setAttribute(MarketingConstantKeys.CURRENTTAB, MarketingConstantKeys.DETAIL);

      if ((request.getParameter("TYPEOFOPERATION") != null) && (request.getParameter("TYPEOFOPERATION").equals("ADD")))
      {
        request.setAttribute(MarketingConstantKeys.TYPEOFOPERATION, MarketingConstantKeys.ADD);
      }
      else
      {
        request.setAttribute(MarketingConstantKeys.TYPEOFOPERATION, MarketingConstantKeys.EDIT);
      }
      request.setAttribute(MarketingConstantKeys.WINDOWID, newWindowId);
      request.setAttribute("literatureform", form);

      FORWARD_final = FORWARD_newliterature;
    }
    catch (Exception e)
    {
      System.out.println("[Exception][GetLiteratureDetailsHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }
}
