/*
 * $RCSfile: CVTextAreaTag.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:29:05 $ - $Author: mking_cv $
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

package com.centraview.udt;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.taglib.html.BaseInputTag;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.common.UserObject;



/**
 * Custom tag for input fields of type "textarea".
 *
 * @author Iqbal Khan
 */
public class CVTextAreaTag extends BaseInputTag {



    /**
     * The name of the bean containing our underlying property.
     */
    /**
     * The name of the bean containing our underlying property.
     */
    protected String name = Constants.BEAN_KEY;
    //protected String name ;
    protected static MessageResources messages = MessageResources.getMessageResources("ApplicationResources");
    protected String fieldname;
    protected String modulename;
    protected int fieldRight;
 
	

    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        this.name = name;
    }


    public CVTextAreaTag()
    {
        fieldRight = 0;
    }

    public String getFieldname()
    {
        return fieldname;
    }

    public void setFieldname(String s)
    {
        fieldname = s;
    }

    public String getModulename()
    {
        return modulename;
    }

    public void setModulename(String s)
    {
        modulename = s;
    }


 
    /**
     * Generate the required input tag.
     * Support for indexed since Struts 1.1
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
        
        ResponseUtils.write(pageContext, this.renderTextareaElement());

        return (EVAL_BODY_TAG);
    }

    /**
     * Generate an HTML &lt;textarea&gt; tag.
     * @throws JspException
     * @since Struts 1.1
     */
    protected String renderTextareaElement() throws JspException {

        HttpSession httpsession = ((HttpServletRequest)pageContext.getRequest()).getSession();
        ModuleFieldRightMatrix modulefieldrightmatrix = ((UserObject)httpsession.getAttribute("userobject")).getUserPref().getModuleAuthorizationMatrix();
        HashMap hashmap = modulefieldrightmatrix.getFieldRights(getModulename());
        if(hashmap != null && hashmap.containsKey(getFieldname()))
            fieldRight = ((Integer)hashmap.get(getFieldname())).intValue();
			
        StringBuffer results = new StringBuffer("<textarea");
        
        results.append(" name=\"");
        // @since Struts 1.1
        if (indexed) {
            prepareIndex(results, name);
        }
        results.append(property);
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
        if (cols != null) {
            results.append(" cols=\"");
            results.append(cols);
            results.append("\"");
        }
        if (rows != null) {
            results.append(" rows=\"");
            results.append(rows);
            results.append("\"");
        }

        if(fieldRight > 0 && (fieldRight == ModuleFieldRightMatrix.VIEW_RIGHT || fieldRight == ModuleFieldRightMatrix.NONE_RIGHT))
            results.append(" readonly=\"readonly\"");
		
        results.append(prepareEventHandlers());
        results.append(prepareStyles());
        results.append(">");
		
		results.append(this.renderData());
        
        results.append("</textarea>");
        return results.toString();
    }

    /**
     * Renders the value displayed in the &lt;textarea&gt; tag.
     * @throws JspException
     * @since Struts 1.1
     */
    protected String renderData() throws JspException {
	    String s = value;
	    if(fieldRight == ModuleFieldRightMatrix.NONE_RIGHT)
	        return "";
	    if (s == null) 
		{
		
	        s = lookupProperty(name, property);
		}
		
		
	    return s != null ? ResponseUtils.filter(s) : "";
    }

    /**
     * Release any acquired resources.
     */
    public void release() {

        super.release();
        //name = Constants.BEAN_KEY;

    }


    protected String lookupProperty(String s, String s1)
        throws JspException
    {
        Object obj = RequestUtils.lookup(pageContext, s, null);
		
        if(obj == null)
            throw new JspException(messages.getMessage("getter.bean", s));
        try
        {
            return BeanUtils.getProperty(obj, s1);
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            throw new JspException(messages.getMessage("getter.access", s1, s));
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            Throwable throwable = invocationtargetexception.getTargetException();
            throw new JspException(messages.getMessage("getter.result", s1, throwable.toString()));
        }
        catch(NoSuchMethodException nosuchmethodexception)
        {
            throw new JspException(messages.getMessage("getter.method", s1, s));
        }
    }


}
