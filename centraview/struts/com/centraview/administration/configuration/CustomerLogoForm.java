/*
 * $RCSfile: CustomerLogoForm.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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
package com.centraview.administration.configuration;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;

import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.common.Validation;

public class CustomerLogoForm extends org.apache.struts.action.ActionForm
{

  private static Logger logger = Logger.getLogger(CustomerLogoForm.class);
  /**
   * Stores Current file
   */
  private String currentfile;
  /**
   * Stores file object
   */
  private FormFile file;

  private String customerlogo;

  public void reset(ActionMapping actionMapping, HttpServletRequest request)
  {}

  public String getCurrentfile()
  {
    return this.currentfile;
  }

  public void setCurrentfile(String currentfile)
  {
    this.currentfile = currentfile;
  }

  public FormFile getFile()
  {
    return this.file;
  }

  public void setFile(FormFile file)
  {
    this.file = file;
  }

  protected static MessageResources messages = MessageResources.getMessageResources("ApplicationResources");

  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
  {

    // initialize new actionerror object
    ActionErrors errors = new ActionErrors();

    if (request.getParameter("buttonpress") != null && "delete".equals(request.getParameter("buttonpress")))
    {
      return errors;
    }

    try
    {
      // initialize validation
      Validation validation = new Validation();
      request.getParameter("file");
      FormFile fileUpload = this.getFile();
      String filename = fileUpload.getFileName();
      String customerlogo = this.getCustomerlogo();
      int index = filename.indexOf(".");
      String strfile = filename.substring(index + 1, filename.length());
      if (strfile.length() != 0 && (!(strfile.equalsIgnoreCase("jpg") || strfile.equalsIgnoreCase("gif") || strfile.equalsIgnoreCase("png"))))
      {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "Please select a file to upload"));
      } else if (strfile.length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "Please select a file to upload"));
      }
      if (errors != null)
      {
        request.setAttribute(AdministrationConstantKeys.TYPEOFMODULE, AdministrationConstantKeys.CUSTOMERLOGO);
        request.setAttribute("Cust", customerlogo);
      }
    } catch (Exception e) {
      logger.error("[validate] Exception thrown.", e);
    }
    return errors;
  }

  public String getCustomerlogo()
  {
    return this.customerlogo;
  }

  public void setCustomerlogo(String customerlogo)
  {
    this.customerlogo = customerlogo;
  }
}
