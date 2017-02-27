/*
 * $RCSfile: CVRadioTag.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:29:04 $ - $Author: mking_cv $
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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.taglib.html.BaseHandlerTag;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.common.UserObject;

/**
 * Tag for input fields of type "radio".
 *
 * @author Craig R. McClanahan
 * @author Ted Husted
 * @author David Graham
 * @version $Revision: 1.1.1.1 $ $Date: 2005/04/28 20:29:04 $
 */
public class CVRadioTag extends BaseHandlerTag {


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

    // ----------------------------------------------------- Instance Variables
	
	/**
     * The message resources for this package.
     */
    protected static MessageResources messages =
    MessageResources.getMessageResources
    ("ApplicationResources");

/*    
    protected static MessageResources messages =
    MessageResources.getMessageResources(Constants.Package + ".LocalStrings");
*/

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


    /**
     * Name of the bean (in some scope) that will return the
     * value of the radio tag.
     * <p>
     * If an iterator is used to render a series of radio tags,
     * this field may be used to specify the name of the bean
     * exposed by the iterator. In this case, the value attribute is
     * used as the name of a property on the <code>idName</code> bean
     * that returns the value of the radio tag in this iteration.
     */
    protected String idName = null;


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

    /**
     * Return the idName.
     * @since Struts 1.1
     */
    public String getIdName() {

        return (this.idName);

    }

    /**
     * Set the idName.
     * @since Struts 1.1
     *
     * @param idName The new idName
     */
    public void setIdName(String idName) {

        this.idName=idName;

    }


    // --------------------------------------------------------- Public Methods


    /**
     * Generate the required input tag.
     * [Indexed property since Struts 1.1]
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        String radioTag = renderRadioElement(serverValue(), currentValue());

        ResponseUtils.write(pageContext, radioTag);

        this.text = null;
        return (EVAL_BODY_TAG);
    }

    /**
     * Return the String to be used in the radio tag's <code>value</code> attribute 
     * that gets sent to the server on form submission.
     * @throws JspException
     */
    private String serverValue() throws JspException {

        // Not using indexed radio buttons
        if (this.idName == null) {
            return this.value;
        }
        
        String serverValue = this.lookupProperty(this.idName, this.value);
        
        return (serverValue == null) ? "" : serverValue;
    }

    /**
     * Acquire the current value of the bean specified by the <code>name</code> 
     * attribute and the property specified by the <code>property</code> attribute.
     * This radio button with this value will be checked.
     * @throws JspException
     */
    private String currentValue() throws JspException {
        String current = this.lookupProperty(this.name, this.property);
        
        return (current == null) ? "" : current;
    }
    
    /**
     * Renders an HTML &lt;input type="radio"&gt; element.
     * @param serverValue The data to be used in the tag's <code>value</code> 
     * attribute and sent to the server when the form is submitted.
     * @param checkedValue If the serverValue equals this value the radio button 
     * will be checked.
     * @return A radio input element.
     * @throws JspException
     * @since Struts 1.1
     */
    protected String renderRadioElement(String serverValue, String checkedValue)
        throws JspException {
		
		//Added by shilpa.
        HttpSession session = ((HttpServletRequest)(pageContext.getRequest())).getSession();
        
        ModuleFieldRightMatrix mfrmx = ((UserObject)session.getAttribute("userobject")).getUserPref().getModuleAuthorizationMatrix();
        HashMap hm = mfrmx.getFieldRights(getModulename());
        
        if (hm != null && hm.containsKey(getFieldname())) 
        {
        	fieldRight =  ((Integer)hm.get(getFieldname())).intValue();
        } 
		
            
        StringBuffer results = new StringBuffer("<input type=\"radio\"");
        results.append(" name=\"");
        // @since Struts 1.1
        if (indexed) {
            prepareIndex(results, name);
        }
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
		
        results.append(serverValue);
        results.append("\"");
        if (serverValue.equals(checkedValue) && (fieldRight != ModuleFieldRightMatrix.NONE_RIGHT || fieldRight == 0)) {
            results.append(" checked=\"checked\"");
        }
		
        if (fieldRight > 0 && (fieldRight == ModuleFieldRightMatrix.VIEW_RIGHT || fieldRight == ModuleFieldRightMatrix.NONE_RIGHT)) 
            results.append(" readonly=\"readonly\"");
		
        results.append(prepareEventHandlers());
        results.append(prepareStyles());
        results.append(getElementClose());
        return results.toString();
    }

    /**
     * Save the associated label from the body content.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doAfterBody() throws JspException {

        if (this.bodyContent != null) {
            String value = this.bodyContent.getString().trim();
            if (value.length() > 0) {
                this.text = value;
            }
        }
        
        return (SKIP_BODY);
    }

    /**
     * Optionally render the associated label from the body content.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {

        // Render any description for this radio button
        if (this.text != null) {
            ResponseUtils.write(pageContext, text);
        }
        
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
		
	/**
	 * Searches all scopes for the bean and calls BeanUtils.getProperty() with the 
	 * given arguments and converts any exceptions into JspException.
	 * 
	 * @param beanName The name of the object to get the property from.
	 * @param property The name of the property to get.
	 * @return The value of the property.
	 * @throws JspException
	 * @since Struts 1.1
	 */
	protected String lookupProperty(String beanName, String property)
	    throws JspException {
	        
	    Object bean = RequestUtils.lookup(this.pageContext, beanName, null);
	    if (bean == null) {
	        throw new JspException(messages.getMessage("getter.bean", beanName));
	    }

	    try {
	        return BeanUtils.getProperty(bean, property);

	    } catch (IllegalAccessException e) {
	        throw new JspException(
	            messages.getMessage("getter.access", property, beanName));

	    } catch (InvocationTargetException e) {
	        Throwable t = e.getTargetException();
	        throw new JspException(
	            messages.getMessage("getter.result", property, t.toString()));

	    } catch (NoSuchMethodException e) {
	        throw new JspException(
	            messages.getMessage("getter.method", property, beanName));
	    }
	}
	

}
