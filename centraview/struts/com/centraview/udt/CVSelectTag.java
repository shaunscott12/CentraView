/*
 * $RCSfile: CVSelectTag.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:29:05 $ - $Author: mking_cv $
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
import org.apache.struts.taglib.html.BaseHandlerTag;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.common.UserObject;
/**
 * Custom tag that represents an HTML select element, associated with a
 * bean property specified by our attributes.  This tag must be nested
 * inside a form tag.
 *
 * @author Craig R. McClanahan
 * @author David Graham
 * @version $Revision: 1.1.1.1 $ $Date: 2005/04/28 20:29:05 $
 */
public class CVSelectTag extends BaseHandlerTag {


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
     * The actual values we will match against, calculated in doStartTag().
     */
    protected String match[] = null;


    /**
     * Should multiple selections be allowed.  Any non-null value will
     * trigger rendering this.
     */
    protected String multiple = null;

    public String getMultiple() {
        return (this.multiple);
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }


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
     * The property name we are associated with.
     */
    protected String property = null;


    /**
     * The saved body content of this tag.
     */
    protected String saveBody = null;


    /**
     * How many available options should be displayed when this element
     * is rendered?
     */
    protected String size = null;

    public String getSize() {
        return (this.size);
    }

    public void setSize(String size) {
        this.size = size;
    }


    /**
     * The value to compare with for marking an option selected.
     */
    protected String value = null;


    // ------------------------------------------------------------- Properties


    /**
     * Does the specified value match one of those we are looking for?
     *
     * @param value Value to be compared.
     */
    public boolean isMatched(String value) {
        if ((this.match == null) || (value == null)) {
            return false;
        }

        for (int i = 0; i < this.match.length; i++) {
            if (value.equals(this.match[i]))
                return true;
        }

        return false;
    }


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
     * Return the comparison value.
     */
    public String getValue() {

        return (this.value);

    }


    /**
     * Set the comparison value.
     *
     * @param value The new comparison value
     */
    public void setValue(String value) {

        this.value = value;

    }


    // --------------------------------------------------------- Public Methods


    /**
     * Render the beginning of this select tag.
     * <p>
     * Support for indexed property since Struts 1.1
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        ResponseUtils.write(pageContext, renderSelectStartElement());

        // Store this tag itself as a page attribute
        pageContext.setAttribute(Constants.SELECT_KEY, this);

        this.calculateMatchValues();

        return (EVAL_BODY_TAG);
    }

    /**
     * Create an appropriate select start element based on our parameters.
     * @throws JspException
     * @since Struts 1.1
     */
    protected String renderSelectStartElement() throws JspException {

	    HttpSession session = ((HttpServletRequest)(pageContext.getRequest())).getSession();

	    ModuleFieldRightMatrix mfrmx = ((UserObject)session.getAttribute("userobject")).getUserPref().getModuleAuthorizationMatrix();
	    HashMap hm = mfrmx.getFieldRights(getModulename());
	    //System.out.println("[DEBUG] [CVSelectTag]: SandeepJ- matrix in cvselect : "+hm);
	    if (hm != null && hm.containsKey(getFieldname()))
	    {
	    	fieldRight =  ((Integer)hm.get(getFieldname())).intValue();
	    }

        StringBuffer results = new StringBuffer("<select");

        results.append(" name=\"");
        // * @since Struts 1.1
        if (this.indexed) {
            prepareIndex(results, name);
        }
        results.append(property);
        results.append("\"");
        if (accesskey != null) {
            results.append(" accesskey=\"");
            results.append(accesskey);
            results.append("\"");
        }
        if (multiple != null) {
            results.append(" multiple=\"multiple\"");
        }
        if (size != null) {
            results.append(" size=\"");
            results.append(size);
            results.append("\"");
        }

        if (fieldRight == ModuleFieldRightMatrix.NONE_RIGHT || fieldRight == 0)
        {
          //System.out.println("[DEBUG] [CVSelectTag]: SandeepJ - setting to dontselect(this),resetdata(this)... in CVSelect");
          //results.append(" onclick=\"dontSelect(this)\" onchange=\"resetData(this)\" ");    // TODO: uncomment this field to get this to work properly
         }

        if (tabindex != null) {
            results.append(" tabindex=\"");
            results.append(tabindex);
            results.append("\"");
        }
        results.append(prepareEventHandlers());
        results.append(prepareStyles());
        results.append(">");

        return results.toString();
    }

    /**
     * Calculate the match values we will actually be using.
     * @throws JspException
     */
    private void calculateMatchValues() throws JspException {
        if (this.value != null) {
            this.match = new String[1];
            this.match[0] = this.value;

        } else {
            Object bean = RequestUtils.lookup(pageContext, name, null);
            if (bean == null) {
                JspException e =
                    new JspException(messages.getMessage("getter.bean", name));

                RequestUtils.saveException(pageContext, e);
                throw e;
            }

            try {
                this.match = BeanUtils.getArrayProperty(bean, property);
                if (this.match == null) {
                    this.match = new String[0];
                }

            } catch (IllegalAccessException e) {
                RequestUtils.saveException(pageContext, e);
                throw new JspException(
                    messages.getMessage("getter.access", property, name));

            } catch (InvocationTargetException e) {
                Throwable t = e.getTargetException();
                RequestUtils.saveException(pageContext, t);
                throw new JspException(
                    messages.getMessage("getter.result", property, t.toString()));

            } catch (NoSuchMethodException e) {
                RequestUtils.saveException(pageContext, e);
                throw new JspException(
                    messages.getMessage("getter.method", property, name));
            }
        }
    }


    /**
     * Save any body content of this tag, which will generally be the
     * option(s) representing the values displayed to the user.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doAfterBody() throws JspException {

        if (bodyContent != null) {
            String value = bodyContent.getString();
            if (value == null) {
                value = "";
            }

            this.saveBody = value.trim();
        }

        return (SKIP_BODY);
    }


    /**
     * Render the end of this form.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {

        // Remove the page scope attributes we created
        pageContext.removeAttribute(Constants.SELECT_KEY);

        // Render a tag representing the end of our current form
        StringBuffer results = new StringBuffer();
        if (saveBody != null) {
            results.append(saveBody);
        }
        results.append("</select>");

        ResponseUtils.write(pageContext, results.toString());

        return (EVAL_PAGE);
    }

    /**
     * Release any acquired resources.
     */
    public void release() {

        super.release();
        match = null;
        multiple = null;
        name = Constants.BEAN_KEY;
        property = null;
        saveBody = null;
        size = null;
        value = null;

    }

}
