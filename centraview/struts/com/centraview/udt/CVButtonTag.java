/*
 * $RCSfile: CVButtonTag.java,v $    $Revision: 1.2 $  $Date: 2005/07/25 13:44:47 $ - $Author: mcallist $
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

// package declaration
package com.centraview.udt;

// import declaration
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;
import org.apache.struts.taglib.html.ButtonTag;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.ResponseUtils;

import com.centraview.administration.authorization.Authorization;
import com.centraview.administration.authorization.AuthorizationHome;
import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

/**
*	@version	1.0
*	Date		4 June 2003
*	@author		Ashwin Nagar
*	Provides customize buttons to display status window and control of tooltip.
*	@param String statuswindow containing string to display status window information and tooltip
*	@param String statuswindowarg containing additional information passed as argument.
*	@param boolean tooltip controls display of tooltip
*/
public class CVButtonTag extends ButtonTag {

  private static Logger logger = Logger.getLogger(CVButtonTag.class);


	/**
	*	status window and tooltip string
	*/
	protected String statuswindow;

	/**
	*	status window and tooltip argument string
	*/
	protected String statuswindowarg;

	/**
	*	controller for displaying tootip
	*/
	protected boolean tooltip;

	// message property file
	protected static MessageResources messages =	MessageResources.getMessageResources("ApplicationResources");

	/**
	*	recordID int
	*/
	protected int recordID;

	/**
	*	status module name string
	*/
	protected String modulename;

	/**
	*	status module name string
	*/
	protected int fieldRight = 0;

	/**
	 *	status field name string
	 */
	protected String fieldname;

	protected int buttonoperationtype;

	/**
	*	Constructor, with no parameter, to initialize instance variables.
	*/
	public CVButtonTag(){
		statuswindow=null;
		statuswindowarg=null;
		tooltip=false;
		buttonoperationtype = 0;
		recordID = 0;
		modulename = null;
	}

	public int getButtonoperationtype()
	{
		return this.buttonoperationtype;
	}

	public void setButtonoperationtype(int buttonOperationType)
	{
		this.buttonoperationtype = buttonOperationType;
	}

	public String getFieldname()     {
			return fieldname;
	}

	public void setFieldname(String fieldname)     {
			this.fieldname = fieldname;
	}

	/**
	*	Getter method for statuswindow
	*	returns String
	*/
	public String getStatuswindow()     {
		return statuswindow;
	}

	/**
	*	Setter method for statuswindow
	*	@param String
	*	Accepts string to display
	*/
	public void setStatuswindow(String statuswindow)     {
		this.statuswindow = statuswindow;
	}

	/**
	*	Getter method for statuswindowarg
	*	returns String
	*/
	public String getStatuswindowarg()     {
		return statuswindowarg;
	}

	/**
	*	Setter method for statuswindowarg
	*	@param String
	*	Accepts string to display
	*/
	public void setStatuswindowarg(String statuswindowarg)     {
		this.statuswindowarg = statuswindowarg;
	}

	/**
	*	Getter method for tooltip
	*	returns boolean
	*/
	public boolean getTooltip()
	{
		return tooltip;
	}

	/**
	*	Setter method for tooltip
	*	@param boolean
	*	Accepts boolean to control display of status window and tooltip
	*/
	public void setTooltip(boolean tooltip)
	{
		this.tooltip = tooltip;
	}

	/**
	*	Getter method for recordID
	*	returns integer
	*/
	public int getRecordID()     {
		return this.recordID;
	}

	/**
	*	Setter method for recordID
	*	@param interger
	*	Accepts integer to check wheather we must disable the button or not. On basis of information such as modulename, recordID, buttonOperationType.
	*/
	public void setRecordID(int recordID){
		this.recordID = recordID;
	}

	/**
	*	Getter method for modulename
	*	returns String
	*/
	public String getModulename()     {
		return this.modulename;
	}

	/**
	*	Setter method for modulename
	*	@param String
	*	Accepts String to check wheather we must disable the button or not. On basis of information such as modulename, recordID, buttonOperationType.
	*/
	public void setModulename(String modulename)     {
		this.modulename = modulename;
	}

	/**
	*	Executes at start of tag
	*	returns int
	*/
	public int doStartTag() throws JspException    {
		return 2;
	}

	/**
	* Executes after body of tag but before end of tag delimeter
	*	returns int
	*/
	public int doAfterBody() throws JspException {
		if(bodyContent != null) {
			String value = bodyContent.getString().trim();
			if(value.length() > 0)
				super.text = value;
		}
		return 0;
	}

	/**
	*	Executes at end of tag delimeter
	*	returns int
	*/
	public int doEndTag() throws JspException{

		setDisabled(false);
		HttpSession session = ((HttpServletRequest)pageContext.getRequest()).getSession();
		UserObject userObject = (UserObject)session.getAttribute("userobject");

		ModuleFieldRightMatrix modulefieldrightmatrix = userObject.getUserPref().getModuleAuthorizationMatrix();

		HashMap hashmap = modulefieldrightmatrix.getFieldRights(getModulename());
		if(hashmap != null && hashmap.containsKey(getFieldname()))
			fieldRight = ((Integer)hashmap.get(getFieldname())).intValue();

		int individualID = userObject.getIndividualID();

		StringBuffer results = new StringBuffer();

		String label = super.value;
		if(label == null && super.text != null)
			label = super.text;

		if(label == null || label.trim().length() < 1)
			label = "Click";

		results.append("<input type=\"button\"");

		if(super.property != null){
			results.append(" name=\"");
			results.append(super.property);
			if(super.indexed)
				super.prepareIndex(results, null);
			results.append("\"");
		}// end of if(super.property != null)

		if(super.accesskey != null){
			results.append(" accesskey=\"");
			results.append(super.accesskey);
			results.append("\"");
		}// end of if(super.accesskey != null)

		if(super.tabindex != null){
			results.append(" tabindex=\"");
			results.append(super.tabindex);
			results.append("\"");
		}// end of if(super.tabindex != null)

		results.append(" value=\"");
		results.append(label);
		results.append("\"");

		if (statuswindow != null && statuswindow.length() > 0){
			String statusWindow=messages.getMessage(statuswindow);
			String statusWindowArg=messages.getMessage(statuswindowarg);
			results.append(" onmouseover=\"window.status='");
			results.append(statusWindow + " " + statusWindowArg);
			results.append("'; return true\"");
			results.append(" onmouseout=\"window.status='");
			results.append("");
			results.append("'; return true\" ");
		}// end of if (statuswindow != null && statuswindow.length() > 0)

		if (fieldRight == ModuleFieldRightMatrix.NONE_RIGHT){
			setDisabled(true);
		}// end of if (fieldRight == ModuleFieldRightMatrix.NONE_RIGHT)
		if (fieldRight == ModuleFieldRightMatrix.VIEW_RIGHT){
			if (label.equals("View All")){
				setDisabled(false);
			}
			else{
				setDisabled(true);
			}
		}// end of if (fieldRight == ModuleFieldRightMatrix.VIEW_RIGHT)

		if (fieldRight == ModuleFieldRightMatrix.DELETE_RIGHT || fieldRight == ModuleFieldRightMatrix.UPDATE_RIGHT ){
			setDisabled(false);
		}// end of if (fieldRight == ModuleFieldRightMatrix.DELETE_RIGHT || fieldRight == ModuleFieldRightMatrix.UPDATE_RIGHT )

		// We will Perform the check wheather, we must disable the button or not.
		// On basis of parameter value such as individualID, ModuleName, RecordID, ButtonOperationType
		// We will query the database and return true (if we can perform the action) or false.
		if (this.recordID != 0 && this.modulename != null && this.buttonoperationtype != 0){
		    boolean buttonOperationFlag = false;
		    //Setting the DataSource
		    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(this.pageContext.getServletContext())).getDataSource();
		    try
		    {
		      InitialContext ic = CVUtility.getInitialContext();
			  AuthorizationHome authorizationHome = (AuthorizationHome)CVUtility.getHomeObject("com.centraview.administration.authorization.AuthorizationHome", "Authorization");
		      Authorization authorization = authorizationHome.create();
			  authorization.setDataSource(dataSource);
			  buttonOperationFlag = authorization.canPerformRecordOperation(individualID, this.modulename, this.recordID, this.buttonoperationtype);
		    }// end of try block
		    catch (Exception e)
		    {
				logger.error("[EXCEPTION] CVButtonTag.CLASS doEndTag() Method "+e);
		    }// end of catch block

			if (!buttonOperationFlag)
			{
				setDisabled(true);
			}// end of if (!buttonOperationFlag)
			else{
				setDisabled(false);
			}// end of else for if (!buttonOperationFlag)
		}// end of if (this.recordID != 0 && this.modulename != null && this.buttonoperationtype != 0)

		results.append(prepareStyles());
		results.append(prepareEventHandlers());
		results.append(getElementClose());
		ResponseUtils.write(pageContext, results.toString());
		return 6;
	}

	/**
	*	Initialize all variables to null or boolean
	*/
	public void release()     {
		super.release();
		statuswindow = null;
		modulename = null;
		recordID = 0;
		buttonoperationtype = 0;
		tooltip=false;
	}
}
