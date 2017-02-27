/*
 * $RCSfile: LiteratureForm.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:36:00 $ - $Author: mcallist $
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

import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.Validation;
import com.centraview.marketing.marketingfacade.MarketingFacade;
import com.centraview.marketing.marketingfacade.MarketingFacadeHome;
import com.centraview.settings.Settings;


public class LiteratureForm extends org.apache.struts.action.ActionForm {

	/*
	 *	Stores activity id
	 */
	private String activityid;

	/*
	 *	Stores title
	 */
	private String title;

	/*
	 *	Stores detail
	 */
	private String detail;

	/*
	 *	Stores other literature names in vector to display in list box on jsp
	 */
	private Vector literaturenamevec;

	/*
	 *	Stores literature name
	 */
	private String literaturename;

	/*
	* Stores literatureid
	*/
	private String literatureid;


	/*
	 *	Stores entity name
	 */
	private String entityname;

	/*
	 *	Stores entity id
	 */
	private String entityid;

	/*
	 *	Stores individual name	for the selected entity
	 */
	private String individualname;

	/*
	 *	Stores individual id for the selected entity
	 */
	private String individualid;

	/*
	 *	Stores assignedto name for the selected entity
	 */
	private String assignedtoname;

	/*
	 *	Stores assignedto id for the selected entity
	 */
	private String assignedtoid;

	/*
	 *	Stores deliverymethodid
	 */
	private String deliverymethodid = "1";

	/*
	 *	Stores deliverymethodname
	 */
	private String deliverymethodname ;

	/*
	 *	Stores statusid
	 */
	private String statusid = "1";

	/*
	 *	Stores statusname
	 */
	private String statusname;

	/*
	 *	Stores duebymonth
	 */
	private String duebymonth;

	/*
	 *	Stores duebyday
	 */
	private String duebyday;

	/*
	 *	Stores duebyyear
	 */
	private String duebyyear;

	/*
	 *	Stores duebytime
	 */
	private String duebytime;
	/*
	 * Stores duebytime
	 */
	private String	selduebytime;

	/*
	 * Stores names of literature
	 */
	private String	names;

	private String	currentday;
	private String	currentmonth;
	private String	currentyear;

	// message property file
	protected static MessageResources messages = MessageResources.getMessageResources("ApplicationResources");



	/**
	 *	Constructor
	 */
    public LiteratureForm() {
        // TODO: Write constructor body
    }

	/**
	 *	Reset
	 */
    public void reset(ActionMapping actionMapping, HttpServletRequest request) {
        // TODO: Write method body
    }


	public String getActivityid()
	{
		return this.activityid;
	}

	public void setActivityid(String activityid)
	{
		this.activityid = activityid;
	}


	public String getAssignedtoid()
	{
		return this.assignedtoid;
	}

	public void setAssignedtoid(String assignedtoid)
	{
		this.assignedtoid = assignedtoid;
	}


	public String getAssignedtoname()
	{
		return this.assignedtoname;
	}

	public void setAssignedtoname(String assignedtoname)
	{
		this.assignedtoname = assignedtoname;
	}


	public String getDetail()
	{
		return this.detail;
	}

	public void setDetail(String detail)
	{
		this.detail = detail;
	}


	public String getEntityid()
	{
		return this.entityid;
	}

	public void setEntityid(String entityid)
	{
		this.entityid = entityid;
	}


	public String getEntityname()
	{
		return this.entityname;
	}

	public void setEntityname(String entityname)
	{
		this.entityname = entityname;
	}


	public String getIndividualid()
	{
		return this.individualid;
	}

	public void setIndividualid(String individualid)
	{
		this.individualid = individualid;
	}


	public String getIndividualname()
	{
		return this.individualname;
	}

	public void setIndividualname(String individualname)
	{
		this.individualname = individualname;
	}


	public String getLiteratureid()
	{
		return this.literatureid;
	}


	public void setLiteratureid(String literatureid)
	{
		this.literatureid = literatureid;
	}


	public String getLiteraturename()
	{
		return this.literaturename;
	}

	public void setLiteraturename(String literaturename)
	{
		this.literaturename = literaturename;
	}


	public Vector getLiteraturenamevec()
	{
		return this.literaturenamevec;
	}

	public void setLiteraturenamevec(Vector literaturenamevec)
	{
		this.literaturenamevec = literaturenamevec;
	}


	public String getTitle()
	{
		return this.title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}


	public String getStatusname()
	{
		return this.statusname;
	}

	public void setStatusname(String statusname)
	{
		this.statusname = statusname;
	}


	public String getStatusid()
	{
		return this.statusid;
	}

	public void setStatusid(String statusid)
	{
		this.statusid = statusid;
	}


	public String getDeliverymethodid()
	{
		return this.deliverymethodid;
	}

	public void setDeliverymethodid(String deliverymethodid)
	{
		this.deliverymethodid = deliverymethodid;
	}


	public String getDeliverymethodname()
	{
		return this.deliverymethodname;
	}

	public void setDeliverymethodname(String deliverymethodname)
	{
		this.deliverymethodname = deliverymethodname;
	}


	public String getDuebyday()
	{
		return this.duebyday;
	}

	public void setDuebyday(String duebyday)
	{
		this.duebyday = duebyday;
	}


	public String getDuebymonth()
	{
		return this.duebymonth;
	}

	public void setDuebymonth(String duebymonth)
	{
		this.duebymonth = duebymonth;
	}


	public String getDuebytime()
	{
		return this.duebytime;
	}

	public void setDuebytime(String duebytime)
	{
		this.duebytime = duebytime;
	}


	public String getDuebyyear()
	{
		return this.duebyyear;
	}

	public void setDuebyyear(String duebyyear)
	{
		this.duebyyear = duebyyear;
	}

	public String getSelduebytime()
	{
		return this.selduebytime;
	}

	public void setSelduebytime(String selduebytime)
	{
		this.selduebytime = selduebytime;
	}

	public String getNames()
	{
		return this.names;
	}

	public void setNames(String names)
	{
		this.names = names;
	}

		public String getCurrentday()
	{
		return this.currentday;
	}

	public void setCurrentday(String currentday)
	{
		this.currentday = currentday;
	}


	public String getCurrentmonth()
	{
		return this.currentmonth;
	}

	public void setCurrentmonth(String currentmonth)
	{
		this.currentmonth = currentmonth;
	}


	public String getCurrentyear()
	{
		return this.currentyear;
	}

	public void setCurrentyear(String currentyear)
	{
		this.currentyear = currentyear;
	}

	/*
	 *	Validates user input data
	 *	@param mapping ActionMapping
	 *	@param request HttpServletRequest
	 *	@return ActionErrors
	 */
	public ActionErrors validate (ActionMapping mapping, HttpServletRequest request)
	{
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		// initialize new actionerror object
		ActionErrors errors = new ActionErrors();

		try
		{
			Validation validation = new Validation();

      if (this.getTitle() == null || this.getTitle().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Title"));
      }
      
      if (this.getEntityname() == null || this.getEntityname().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Entity"));
      }
      
      if (this.getIndividualname() == null || this.getIndividualname().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Individual"));
      }

      if (this.getAssignedtoname() == null || this.getAssignedtoname().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Assigned To"));
      }

			// Literature
			String ids = this.getLiteratureid();

			// check if user have entered any data
			if ( (this.getDuebyyear() != null && this.getDuebyyear().length() != 0) ||
           (this.getDuebymonth() != null && this.getDuebymonth().length() != 0) ||
           (this.getDuebyday() != null && this.getDuebyday().length() != 0) ||
           (this.getDuebytime() != null && this.getDuebytime().length() != 0)) {

				// validation.checkForDate("error.literature.duedate", this.getDuebyyear(), this.getDuebymonth(), this.getDuebyday(), "error.application.date", "", errors);
				// validation.checkForDateComparison("error.literature.currentdate", this.getCurrentyear(), this.getCurrentmonth(), this.getCurrentday(), "00:00 AM", "error.literature.duedate", this.getDuebyyear(), this.getDuebymonth(), this.getDuebyday(), "00:00 AM", "error.application.datecomparison", "", errors, "error.literature.currentdate", "error.literature.duedate");
      }


			if ((ids == null) || (ids.equals(""))) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Literature"));
			}

			// redirect to jsp if errors present
			if (errors != null)
			{
		    	request.setAttribute(MarketingConstantKeys.CURRENTTAB, MarketingConstantKeys.DETAIL);
		    	request.setAttribute(MarketingConstantKeys.TYPEOFOPERATION, request.getParameter(MarketingConstantKeys.TYPEOFOPERATION));
				request.setAttribute(MarketingConstantKeys.WINDOWID, request.getParameter(MarketingConstantKeys.WINDOWID));
				// set request parameter as set in viewhandler and newhandler
				//System.out.println("fasdsfsfsfgsf sfsdf sf sfs sf s");
			}

			MarketingFacadeHome home = (MarketingFacadeHome)CVUtility.getHomeObject("com.centraview.marketing.marketingfacade.MarketingFacadeHome","MarketingFacade");
			MarketingFacade remote = home.create();
      		remote.setDataSource(dataSource);
			Vector deliverymethodlist = remote.getAllDeliveryMethod();
			request.setAttribute("deliverymethodlist" , deliverymethodlist );
			//System.out.println("deliverymethodlist ::" + deliverymethodlist);

			Vector activitystatuslist = remote.getAllActivityStatus();
			request.setAttribute("activitystatuslist" , activitystatuslist );
			//System.out.println("activitystatuslist ::" + activitystatuslist);

			String names = this.getNames();
			//System.out.println("names :: "+names);

			String strLiteratureId = this.getLiteratureid();

			//System.out.println("strLiteratureId :: "+strLiteratureId);
			if ( strLiteratureId != null )
			{
				Vector literaturenamevec = new Vector();
				/*for (int i=0;i<strLiteratureId.length;i++)
				{
					System.out.println("strLiteratureId[i] :: "+strLiteratureId[i]);
					int id = Integer.parseInt(strLiteratureId[i]);
					System.out.println("id :: "+id);
					literaturenamevec.add(new DDNameValue(id,strLiteratureName[i]));
				}*/
				//System.out.println("strLiteratureId[0] :: "+strLiteratureId[0]);

				if (!strLiteratureId.equals(""))
				{
					StringTokenizer stid = new StringTokenizer(strLiteratureId,",");
					StringTokenizer stName = new StringTokenizer(names,",");
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
				this.setLiteraturenamevec(literaturenamevec);
				//System.out.println("getLiteraturenamevec "+this.getLiteraturenamevec());
				request.setAttribute("literatureform",this);
			}

		}
		catch (Exception e)
		{
      System.out.println("[Exception] LiteratureForm.validate: " + e.toString());
			//e.printStackTrace();
		}
		return errors;
	}
}
