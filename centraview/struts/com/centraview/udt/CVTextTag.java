/*
 * $RCSfile: CVTextTag.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:29:05 $ - $Author: mking_cv $
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
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.html.BaseFieldTag;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.common.UserObject;

public class CVTextTag extends BaseFieldTag {

	
    // for cv field authorization	
    /**
     *	status field name string
     */
    protected String fieldname;

    /**
     *	status module name string
     */
    protected String modulename;
    
    /**
     *	status module name string
     */
    protected int fieldRight = 0;
    

    
    /**
     */
    public String getFieldname()     {
        return fieldname;
    }

    /**
     */
    public void setFieldname(String fieldname)     {
        this.fieldname = fieldname;
    }

    /**
     */
    public String getModulename()     {
        return modulename;
    }

    /**
     */
    public void setModulename(String modulename)     {
        this.modulename = modulename;
    }
	// end cv field authorization	


    // message property file
    protected static MessageResources messages =
    MessageResources.getMessageResources
    ("ApplicationResources");

    /**
     *	Constructor, with no parameter, to initialize instance variables.
     */
    public CVTextTag()     {
        super();
        //this.type = "text";
    }

    /**
     * Generate the required input tag.
     * <p>
     * Support for indexed property since Struts 1.1
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException 
	{
		HttpSession session = ((HttpServletRequest)(pageContext.getRequest())).getSession();
		
        ModuleFieldRightMatrix mfrmx = ((UserObject)session.getAttribute("userobject")).getUserPref().getModuleAuthorizationMatrix();
		HashMap hm = mfrmx.getFieldRights(getModulename());
		
		if (hm != null && hm.containsKey(getFieldname())) 
		{
			fieldRight =  ((Integer)hm.get(getFieldname())).intValue();
		} 
		
        // Create an appropriate "input" element based on our parameters
		
        StringBuffer results = new StringBuffer("<input type=\"");
		//super.type = "text";
		
        results.append(type);
		
        results.append("\" name=\"");
        // * @since Struts 1.1
        if (indexed)
            prepareIndex(results, name);
        results.append(property);
        results.append("\"");
        if (accesskey != null) {
            results.append(" accesskey=\"");
            results.append(accesskey);
            results.append("\"");
        }
        if (accept != null) {
            results.append(" accept=\"");
            results.append(accept);
            results.append("\"");
        }
        if (maxlength != null) {
            results.append(" maxlength=\"");
            results.append(maxlength);
            results.append("\"");
        }
        if (cols != null) {
            results.append(" size=\"");
            results.append(cols);
            results.append("\"");
        }
		
        if (fieldRight > 0 && (fieldRight == ModuleFieldRightMatrix.VIEW_RIGHT || fieldRight == ModuleFieldRightMatrix.NONE_RIGHT)) 
            results.append(" readonly=\"readonly\"");
		
        if (tabindex != null) {
            results.append(" tabindex=\"");
            results.append(tabindex);
            results.append("\"");
        }
		
		
        results.append(" value=\"");
        if (value != null) {
            results.append(ResponseUtils.filter(value));
        } else if ((redisplay || !"password".equals(type)) && (fieldRight != ModuleFieldRightMatrix.NONE_RIGHT || fieldRight == 0)) {
            Object value = RequestUtils.lookup(pageContext, name, property, null);
            if (value == null)
                value = "";
            results.append(ResponseUtils.filter(value.toString()));
        }
		
        results.append("\"");
        results.append(prepareEventHandlers());
        results.append(prepareStyles());
        results.append(getElementClose());

        // Print this field to our output writer
        ResponseUtils.write(pageContext, results.toString());
        return (EVAL_BODY_TAG);

    }


	/**
	 *	Initialize all variables to null or boolean
	 */
    public void release()     {
        super.release();
        //statuswindow = null;
		//tooltip=false;
    }
}
