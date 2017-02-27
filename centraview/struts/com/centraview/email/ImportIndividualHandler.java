/*
 * $RCSfile: ImportIndividualHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/01 15:31:05 $ - $Author: mcallist $
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

package com.centraview.email;

import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.common.DDNameValue;
import com.centraview.common.GlobalMasterLists;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class ImportIndividualHandler extends org.apache.struts.action.Action
{
  private static Logger logger = Logger.getLogger(ImportIndividualHandler.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    try
    {
      String entityNo = (String)request.getParameter("entityNo");
      String entityName = (String)request.getParameter("entityName");
      if (entityNo == null)
        entityNo = "0";

      if (entityName == null)
        entityName = "";

      request.setAttribute("entityNo", entityNo);
      request.setAttribute("entityName", entityName);

      HttpSession session = request.getSession(true);
      UserObject userobjectd = (UserObject)session.getAttribute("userobject");
      int individualID = userobjectd.getIndividualID();

      String messageBody = (String)session.getAttribute("emailmessage");
      DynaActionForm dynaForm = (DynaActionForm)form;

      GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);

      StringTokenizer st = new StringTokenizer(messageBody, Constants.EH_HEADER_DELIMETER);

      while (st.hasMoreTokens())
      {
        String fullHeader = st.nextToken();

        String strhead = (fullHeader.substring(0, fullHeader.indexOf(Constants.EH_KEYVALUE_DELEMETER))).trim();
        String strValue = (fullHeader.substring(fullHeader.indexOf(Constants.EH_KEYVALUE_DELEMETER) + 1)).trim();

        if (strhead.equals("street1"))
          dynaForm.set("street1", strValue);
        else if (strhead.equals("street2"))
          dynaForm.set("street2", strValue);
        else if (strhead.equals("city"))
          dynaForm.set("city", strValue);
        else if (strhead.equals("state"))
        {
          Collection stateCol = (Collection)gml.get("States");

          Iterator stateIter = stateCol.iterator();
          while (stateIter.hasNext())
          {
            DDNameValue ddname = (DDNameValue)stateIter.next();

            if ((ddname.getName()).equals(strValue))
            {
              dynaForm.set("state", (new Integer(ddname.getId())).toString());
              break;
            }
          }
        } else if (strhead.equals("zip"))
          dynaForm.set("zip", strValue);
        else if (strhead.equals("country"))
        {
          Collection countryCol = (Collection)gml.get("Country");

          Iterator stateIter = countryCol.iterator();
          while (stateIter.hasNext())
          {
            DDNameValue ddname = (DDNameValue)stateIter.next();

            if ((ddname.getName()).equals(strValue))
              dynaForm.set("country", (new Integer(ddname.getId())).toString());

          }
        } else if (strhead.equals("title"))
          dynaForm.set("title", strValue);
        else if (strhead.equals("source"))
          dynaForm.set("source", strValue);
        else if (strhead.equals("firstname"))
          dynaForm.set("firstname", strValue);
        else if (strhead.equals("lastname"))
          dynaForm.set("lastname", strValue);
        else if (strhead.equals("middlename"))
          dynaForm.set("middlename", strValue);
        else if (strhead.equals("entity"))
          dynaForm.set("entity", strValue);
        else if (strhead.equals("entity1"))
          dynaForm.set("entity1", strValue);
        else if (strhead.equals("Fax"))
        {
          Collection mocFax = (Collection)gml.get("MOC");

          Iterator mocFaxIter = mocFax.iterator();
          while (mocFaxIter.hasNext())
          {
            DDNameValue ddnameFax = (DDNameValue)mocFaxIter.next();

            if ((ddnameFax.getName()).equals(strhead))
            {
              dynaForm.set("mocType1", (new Integer(ddnameFax.getId())).toString());
              dynaForm.set("moc1", strValue);
            }
          }

        } else if (strhead.equals("Phone")) {
          Collection mocPhone = (Collection)gml.get("MOC");

          Iterator mocPhoneIter = mocPhone.iterator();
          while (mocPhoneIter.hasNext())
          {
            DDNameValue ddnamePhone = (DDNameValue)mocPhoneIter.next();

            if ((ddnamePhone.getName()).equals(strhead))
            {
              dynaForm.set("mocType2", (new Integer(ddnamePhone.getId())).toString());
              dynaForm.set("moc2", strValue);
            }
          }
        } else if (strhead.equals("Mobile"))
        {
          Collection mocMobile = (Collection)gml.get("MOC");

          Iterator mocMobileIter = mocMobile.iterator();
          while (mocMobileIter.hasNext())
          {
            DDNameValue ddnameMobile = (DDNameValue)mocMobileIter.next();

            if ((ddnameMobile.getName()).equals(strhead))
            {
              dynaForm.set("mocType3", (new Integer(ddnameMobile.getId())).toString());
              dynaForm.set("moc3", strValue);
            }
          }
        }
      }

      /*
       * EmailFacadeHome home =
       * (EmailFacadeHome)CVUtility.getHomeObject("com.centraview.email.emailfacade.EmailFacadeHome","EmailFacade");
       * EmailFacade remote =( EmailFacade )home.create();
       * 
       * remote.updateHeader(individualID,Integer.parseInt(messageID),Constants.EH_SERVICE_KEY,Constants.EH_SERVICE_VALUE_INDIVIDUALIMPORTED);
       */

    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      return mapping.findForward("failure");
    }
    return mapping.findForward("addindiviudal");
  }
}