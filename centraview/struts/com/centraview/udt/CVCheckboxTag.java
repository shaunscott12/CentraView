/*
 * $RCSfile: CVCheckboxTag.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:29:02 $ - $Author: mking_cv $
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


import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.html.BaseHandlerTag;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.common.UserObject;
/**
 * Tag for input fields of type "checkbox".
 *
 * @author Shilpa Patil
 * @version $Revision: 1.1.1.1 $ $Date: 2005/04/28 20:29:02 $
 */

public class CVCheckboxTag extends BaseHandlerTag {


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

	
	/**
     * The message resources for this package.
     */
    protected static MessageResources messages =
    MessageResources.getMessageResources
    ("ApplicationResources");
	
    // ----------------------------------------------------- Instance Variables


    /**
     * The name of the bean containing our underlying property.
     */
    protected String name = Constants.BEAN_KEY;

    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * The property name for this field.
     */
    protected String property = null;


    /**
     * The body content of this tag (if any).
     */
    protected String text = null;


    /**
     * The server value for this option.
     */
    protected String value = null;


    // ------------------------------------------------------------- Properties


    /**
     * Return the property name.
     */
    public String getProperty() {

        return (this.property);

    }


    /**
     * Set the property name.
     *
     * @param property The new property name
     */
    public void setProperty(String property) {

        this.property = property;

    }


    /**
     * Return the server value.
     */
    public String getValue() {

        return (this.value);

    }


    /**
     * Set the server value.
     *
     * @param value The new server value
     */
    public void setValue(String value) {

        this.value = value;

    }


    // --------------------------------------------------------- Public Methods


    /**
     * Generate the required input tag.
     * <p>
     * Support for indexed property since Struts 1.1
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

	    HttpSession session = ((HttpServletRequest)(pageContext.getRequest())).getSession();
	    
	    ModuleFieldRightMatrix mfrmx = ((UserObject)session.getAttribute("userobject")).getUserPref().getModuleAuthorizationMatrix();
	    HashMap hm = mfrmx.getFieldRights(getModulename());
	    
	    if (hm != null && hm.containsKey(getFieldname())) 
	    {
	    	fieldRight =  ((Integer)hm.get(getFieldname())).intValue();
	    } 
    

        // Create an appropriate "input" element based on our parameters
        StringBuffer results = new StringBuffer("<input type=\"checkbox\"");
        results.append(" name=\"");
        // * @since Struts 1.1
        if( indexed )
                prepareIndex( results, name );
        results.append(this.property);
        results.append("\"");
        if (accesskey != null) {
            results.append(" accesskey=\"");
            results.append(accesskey);
            results.append("\"");
        }
        if (tabindex != null) {
            results.append(" tabindex=\"");
            results.append(tabindex);
            results.append("\"");
        }
        results.append(" value=\"");
        if (value == null)
            results.append("on");
        else
            results.append(value);
			
        results.append("\"");
        Object result = RequestUtils.lookup(pageContext, name,
                                            property, null);
        if (result == null)
            result = "";
        if (!(result instanceof String))
            result = result.toString();
        String checked = (String) result;
        if (checked.equalsIgnoreCase(value)
            || checked.equalsIgnoreCase("true")
            || checked.equalsIgnoreCase("yes")
            || checked.equalsIgnoreCase("on")
			&& 
			(fieldRight != ModuleFieldRightMatrix.NONE_RIGHT || fieldRight == 0)
			)
            results.append(" checked=\"checked\"");
		
        if (fieldRight > 0 && (fieldRight == ModuleFieldRightMatrix.VIEW_RIGHT || fieldRight == ModuleFieldRightMatrix.NONE_RIGHT)) 
            results.append(" readonly=\"readonly\"");
			
			
        results.append(prepareEventHandlers());
        results.append(prepareStyles());
        results.append(getElementClose());

        // Print this field to our output writer
        ResponseUtils.write(pageContext, results.toString());

        // Continue processing this page
        this.text = null;
        return (EVAL_BODY_TAG);

    }



    /**
     * Save the associated label from the body content.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doAfterBody() throws JspException {

        if (bodyContent != null) {
            String value = bodyContent.getString().trim();
            if (value.length() > 0)
                text = value;
        }
        return (SKIP_BODY);

    }


    /**
     * Process the remainder of this page normally.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {

        // Render any description for this checkbox
        if (text != null)
            ResponseUtils.write(pageContext, text);

        // Evaluate the remainder of this page
        return (EVAL_PAGE);

    }


    /**
     * Release any acquired resources.
     */
    public void release() {

        super.release();
        name = Constants.BEAN_KEY;
        property = null;
        text = null;
        value = null;

    }


}
