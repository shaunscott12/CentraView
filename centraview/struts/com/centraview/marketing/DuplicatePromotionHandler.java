/*
 * $RCSfile: DuplicatePromotionHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:12 $ - $Author: mking_cv $
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
import java.sql.Timestamp;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.marketing.marketingfacade.MarketingFacade;
import com.centraview.marketing.marketingfacade.MarketingFacadeHome;
import com.centraview.marketing.promotion.PromotionVO;
import com.centraview.settings.Settings;

public class DuplicatePromotionHandler extends org.apache.struts.action.Action
{

  public static final String GLOBAL_FORWARD_failure = "failure";

  // Local Forwards
  private static final String FORWARD_Promotion = ".view.marketing.new.promotion";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;
  static int counter = 0;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try
    {
      String promotionid = request.getParameter("duplicateId");
      if (promotionid != null)
      {
        // code for load form
        HttpSession session = request.getSession(true);
        int individualId = ((UserObject)session.getAttribute("userobject")).getIndividualID();
        MarketingFacadeHome home = (MarketingFacadeHome)CVUtility.getHomeObject("com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
        MarketingFacade remote = home.create();
        remote.setDataSource(dataSource);
        HashMap mapPromotion = new HashMap();
        mapPromotion.put("PromotionID", "" + promotionid);
        PromotionVO promotionVO = remote.getPromotion(individualId, mapPromotion);
        PromotionDetailListForm promotionDetailListForm = (PromotionDetailListForm)form;
        //promotionDetailListForm.convertItemLines();
        // wrong to call this method here as there will be no itemlines in existsing form
        // we have to take itemLines from VO and put it in form
        // that is done at bottom ...
        promotionDetailListForm.setPromotionid("" + promotionid);
        promotionDetailListForm.setPname(promotionVO.getName());
        promotionDetailListForm.setPdescription(promotionVO.getDescription());
        promotionDetailListForm.setPstatus(promotionVO.getStatus());
        promotionDetailListForm.setNotes(promotionVO.getNotes());

        if (promotionVO.getStartdate() != null)
        {
          Timestamp date = (Timestamp)promotionVO.getStartdate();
          String strMonth = "" + (date.getMonth() + 1);
          String strDay = "" + date.getDate();
          String strYear = "" + (date.getYear() + 1900);
          promotionDetailListForm.setStartmonth(strMonth);
          promotionDetailListForm.setStartday(strDay);
          promotionDetailListForm.setStartyear(strYear);
        }

        if (promotionVO.getEnddate() != null)
        {
          Timestamp date = (Timestamp)promotionVO.getEnddate();
          String strMonth = "" + (date.getMonth() + 1);
          String strDay = "" + date.getDate();
          String strYear = "" + (date.getYear() + 1900);
          promotionDetailListForm.setEndmonth(strMonth);
          promotionDetailListForm.setEndday(strDay);
          promotionDetailListForm.setEndyear(strYear);
        }

        ItemLines itemlines = promotionVO.getItemlines();
        promotionDetailListForm.setItemLines(itemlines);
        FORWARD_final = FORWARD_Promotion;
        request.setAttribute("TypeOfOperation", "Promotions");
      }
    }
    catch (Exception e)
    {
      System.out.println("[Exception][DuplicatePromotionHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    finally
    {
      request.setAttribute("TypeOfOperation", "Promotions");
      request.setAttribute("Operation", "New");
    }
    return (mapping.findForward(FORWARD_final));
  }
}
