/*
 * $RCSfile: LiteratureLookupHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:15 $ - $Author: mking_cv $
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
import java.util.StringTokenizer;
import java.util.Vector;

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
import com.centraview.common.DDNameValue;
import com.centraview.marketing.marketingfacade.MarketingFacade;
import com.centraview.marketing.marketingfacade.MarketingFacadeHome;
import com.centraview.settings.Settings;

public class LiteratureLookupHandler extends Action
{

  private static final String FORWARD_filelookup = ".view.marketing.literature.lookup";
  private static Logger logger = Logger.getLogger(LiteratureLookupHandler.class);

  /**
   * Fetches the list of literature and
   *  forwards the request to the jsp to display
   *
   * @param   mapping
   * @param   form
   * @param   request
   * @param   response
   * @return  ActionForward
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
  	throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

	MarketingFacadeHome home = (MarketingFacadeHome)CVUtility.getHomeObject("com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
    try
    {
		HttpSession session = request.getSession(true);
		com.centraview.common.UserObject userobjectd = (com.centraview.common.UserObject)session.getAttribute("userobject");
		int individualID = userobjectd.getIndividualID();
		MarketingFacade remote = home.create();
		remote.setDataSource(dataSource);
		Vector literaturelist = remote.getAllLiterature();
		String searchStr = request.getParameter("searchTextBox");
		if (searchStr != null && !searchStr.equals("")){
			if (literaturelist != null){
				int size = literaturelist.size();
				int i = 0;
				while (i<size){

					DDNameValue tempListInfo = (DDNameValue) literaturelist.elementAt(i);
					String value = tempListInfo.getName();
					String tempValue = value.toUpperCase();
					String tempSearch = searchStr.toUpperCase();
					int occuranceVALUESearch = (tempValue).indexOf(tempSearch);
					if(occuranceVALUESearch == -1){
						literaturelist.remove(i);
						size --;
					}
					else
					{
						i++;
					}
				}
			}
		}
		request.setAttribute("literaturelist", literaturelist);
		ArrayList literatureIDList = new ArrayList();
		if (request.getParameter("literatureid") != null)
		{
			if (!request.getParameter("literatureid").equals(""))
			{
				String strValues = request.getParameter("literatureid").toString();
				StringTokenizer st = new StringTokenizer(strValues,",");
				String strId = "";
				while(st.hasMoreTokens())
				{
					strId = st.nextToken();
					literatureIDList.add(strId);
				}
			 }
		 }
		request.setAttribute("literatureIDList", literatureIDList);
		
    }
    catch (Exception e)
    {
			logger.error("[Exception] LiteratureLookupHandler.Execute Handler ", e);
      return (mapping.findForward("failure"));
    }
    return (mapping.findForward(FORWARD_filelookup));
  }
}
