/*
 * $RCSfile: CVResetTag.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:29:04 $ - $Author: mking_cv $
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
/**
 *	@version	1.0 
 *	Date		4 June 2003
 *	@author		Ashwin Nagar
 *	Provides customize buttons to display status window and control of tooltip.
 *	@param String statuswindow containing string to display status window information and tooltip
 *	@param String statuswindowarg containing additional information passed as argument.
 *	@param boolean tooltip controls display of tooltip
 */

// package declaration
package com.centraview.udt;

// import declaration
import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.html.ResetTag;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.ResponseUtils;

public class CVResetTag extends ResetTag {

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
    protected static MessageResources messages =
	MessageResources.getMessageResources
	("ApplicationResources");

	/**
	 *	Constructor, with no parameter, to initialize instance variables.
	 */
    public CVResetTag()     {
		statuswindow=null;
		statuswindowarg=null;
		tooltip=false;
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
	 *	Executes at start of tag
	 *	returns int
	 */
    public int doStartTag()      throws JspException    {
        return 2;
    }

	/**
	 * Executes after body of tag but before end of tag delimeter
	 *	returns int
	 */
    public int doAfterBody()    throws JspException     {
        if(bodyContent != null)       {
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
    public int doEndTag() throws JspException   {

        StringBuffer results = new StringBuffer();

        String label = super.value;
        if(label == null && super.text != null)
            label = super.text;
        if(label == null || label.trim().length() < 1)
            label = "Reset";
        results.append("<input type=\"reset\"");
        if(super.property != null)        {
            results.append(" name=\"");
            results.append(super.property);
            if(super.indexed)
                super.prepareIndex(results, null);
            results.append("\"");
        }
        if(super.accesskey != null)         {
            results.append(" accesskey=\"");
            results.append(super.accesskey);
            results.append("\"");
        }
        if(super.tabindex != null)         {
            results.append(" tabindex=\"");
            results.append(super.tabindex);
            results.append("\"");
        }
        results.append(" value=\"");
        results.append(label);
        results.append("\"");

		if (statuswindow != null && statuswindow.length() > 0) 	{
			String statusWindow=messages.getMessage(statuswindow);
			String statusWindowArg=messages.getMessage(statuswindowarg);
			results.append(" onmouseover=\"window.status='");
			results.append(statusWindow + " " + statusWindowArg);
			results.append("'; return true\"");
			results.append(" onmouseout=\"window.status='");
			results.append("");
			results.append("'; return true\" ");
			/*
			if (tooltip) 		{
				results.append(" title=\"");
				results.append(statusWindow + " " + statusWindowArg);
				results.append("\"");
			}
			*/
		}

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
		tooltip=false;
    }
}
