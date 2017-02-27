/*
 * $RCSfile: CustomReportForm.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:11 $ - $Author: mcallist $
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
package com.centraview.report.web;

import java.sql.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.Validation;

/**
 * An ActionForm bean serves as an adapter to make data entered
 * into an HTML available to the rest of a Web application,
 * usually by transferring data to an internal object that uses
 * native types and implements a business logic interface.
 * <p>
 * This ActionForm is used by the <code>NewCustomReportHandler</code> and
 * <code>EditCustomReportHandler</code>. If fields are empty when validate is
 * called by the <code>ActionServlet</code> , error messages are created.
 * <p>
 * @author Kalmychkov Alexi, Serdioukov Eduard
 * @version 1.0
 * @date 01/05/04
 */


public class CustomReportForm extends ActionForm
{
    private int reportId;
    private int moduleId;
    private String name;
    private String description;
    private String reportURL;

    public CustomReportForm()
    {
    }

    /**
     * getReportId
     *
     * @return int
     */
    public int getReportId() {
      return reportId;
    }

    /**
     * setReportId
     *
     * @return
     */
    public void setReportId(int reportId) {
      this.reportId=reportId;
    }


    /**
     * getDateFrom
     *
     * @return Date
     */
    public Date getDateFrom() {
      return null;
    }

    /**
     * getDescription
     *
     * @return String
     */
    public String getDescription() {
    return description;
  }

    /**
     * setDescription
     *
     * @param description String
     */
    public void setDescription(String description) {
    this.description = description;
  }

    /**
     * getModuleId
     *
     * @return int
     */
    public int getModuleId() {
    return moduleId;
  }

    /**
     * setModuleId
     *
     * @param moduleId int
     */
    public void setModuleId(int moduleId) {
    this.moduleId = moduleId;
  }

    /**
     * getName
     *
     * @return String
     */
    public String getName() {
    return name;
  }

    /**
     * setName
     *
     * @param name String
     */
    public void setName(String name) {
    this.name = name;
  }

    /**
     * getReportURL
     *
     * @return String
     */
    public String getReportURL() {
    return reportURL;
  }

    /**
     * setReportURL
     *
     * @param reportURL String
     */
    public void setReportURL(String reportURL) {
        this.reportURL = reportURL;
    }

    /**
     * Clears forn data
     */
    public void clear() {
        reportId = 0;
        moduleId = 0;
        name = "";
        description = "";
        reportURL = "";
    }

    /**
     * Validates user input data
     *
     * @param mapping ActionMapping
     * @param request HttpServletRequest
     * @return ActionErrors
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();
        Validation validation = new Validation();
        validation.checkForRequired("label.reports.name", getName(), "error.application.required", errors);
        return errors;
    }

}
