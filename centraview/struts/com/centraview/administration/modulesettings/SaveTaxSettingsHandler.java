/*
 * $RCSfile: SaveTaxSettingsHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:08 $ - $Author: mcallist $
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

package com.centraview.administration.modulesettings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import com.centraview.account.common.TaxMartixForm;
import com.centraview.account.helper.AccountHelper;
import com.centraview.account.helper.AccountHelperHome;
import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

/**
 * This EJB handles all Database related
 * issues with the EmailSettings client in CentraView.
 * @author Naresh Patel <npatel@centraview.com>
 * @version $Revision: 1.2 $
 */

public class SaveTaxSettingsHandler extends Action
{
  public static final String GLOBAL_FORWARD_failure = ".view.administration.tax_settings";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;
  private static final String FORWARD_save = ".view.administration.tax_settings";
  private static final String FORWARD_saveandclose = ".view.administration.module_settings";

  private static Logger logger = Logger.getLogger(SaveTaxSettingsHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form,  HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    ActionErrors allErrors = new ActionErrors();
    String returnStatus = "";
    String actionType = request.getParameter("actionType").toString();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject) session.getAttribute("userobject");
    int IndividualId = userObject.getIndividualID();
    DynaActionForm dynaform = (DynaActionForm) form;

    String taxClassValue = (String) dynaform.get("taxClassValue");
    String taxJurisdictionsValue = (String) dynaform.get("taxJurisdictionsValue");
    String tempTaxClassID = (String) dynaform.get("taxClassID");
    String tempTaxJurisdictionsID = (String) dynaform.get("taxJurisdictionsID");

    FORWARD_final = FORWARD_save;

    if (actionType != null && actionType.equals("ADD")){
      AccountHelperHome home = (AccountHelperHome)CVUtility.getHomeObject("com.centraview.account.helper.AccountHelperHome", "AccountHelper");
      try {
        AccountHelper remote = home.create();
        remote.setDataSource(dataSource);
        if (taxClassValue != null && ! taxClassValue.equals("")) {
          remote.insertTaxClassOrJurisdiction(taxClassValue,"taxClass");
        }
        if (taxJurisdictionsValue != null && !taxJurisdictionsValue.equals("")) {
          remote.insertTaxClassOrJurisdiction(taxJurisdictionsValue,"taxJurisdiction");
        }
      } catch (Exception e) {
        logger.error("[Exception] ViewSourceSettingHandler.Execute Handler ", e);
      }
    }
    
    if (actionType != null && actionType.equals("REMOVE")) {
      AccountHelperHome home = (AccountHelperHome)CVUtility.getHomeObject("com.centraview.account.helper.AccountHelperHome", "AccountHelper");
      try {
        AccountHelper remote = home.create();
        remote.setDataSource(dataSource);
        if (tempTaxClassID != null && !tempTaxClassID.equals("")) {
          int taxClassID = Integer.parseInt(tempTaxClassID);
          remote.removeTaxClassOrJurisdiction(taxClassID, "taxClass");
        }
        if (tempTaxJurisdictionsID != null && !tempTaxJurisdictionsID.equals("")) {
          int taxJurisdictionsID = Integer.parseInt(tempTaxJurisdictionsID);
          remote.removeTaxClassOrJurisdiction(taxJurisdictionsID,"taxJurisdiction");
        }
      } catch (Exception e) {
        logger.error("[Exception] ViewSourceSettingHandler.Execute Handler ", e);
      }
    }
    if (actionType != null && actionType.startsWith("SAVE")) {
      AccountHelperHome home = (AccountHelperHome)CVUtility.getHomeObject("com.centraview.account.helper.AccountHelperHome", "AccountHelper");
      try {
        AccountHelper remote = home.create();
        remote.setDataSource(dataSource);
        Vector taxClass = remote.getTaxClasses();
        Vector taxJurisdiction = remote.getTaxJurisdiction();
        ArrayList taxMatrixValue = new ArrayList();
        
        if (taxJurisdiction != null && taxJurisdiction.size() != 0) {
          for (int i = 0; i < taxJurisdiction.size(); i++) {
            DDNameValue taxJurisdictionNameValue = (DDNameValue)taxJurisdiction.get(i);
            if (taxJurisdictionNameValue != null) {
              int taxJurisdictionID = taxJurisdictionNameValue.getId();
              String taxJurisdictionName = taxJurisdictionNameValue.getName();
              
              if (taxClass != null && taxClass.size() != 0) {
                for (int j = 0; j < taxClass.size(); j++) {
                  DDNameValue taxClassNameValue = (DDNameValue)taxClass.get(j);
                  if (taxClassNameValue != null) {
                    int taxClassID = taxClassNameValue.getId();
                    String taxClassName = taxClassNameValue.getName();
                    
                    String textBoxName = "J" + taxJurisdictionID + "C" + taxClassID;
                    String taxRate = request.getParameter(textBoxName);
                    if (taxRate == null || taxRate.length() <= 0) {
                      allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", taxJurisdictionName+"/"+taxClassName));
                    } else {
                      float floatTaxRate = Float.parseFloat(taxRate);
                      if (floatTaxRate < 0 || floatTaxRate > 100) {
                        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.rangeField", taxJurisdictionName+"/"+taxClassName));
                      }
                      taxMatrixValue.add(new TaxMartixForm(taxClassID,taxJurisdictionID,floatTaxRate));
                    }
                  }
                }
              }
            }
          }
        }
        
        if (! allErrors.isEmpty()) {
          saveErrors(request, allErrors);
          return(mapping.findForward(FORWARD_save));
        }
        
        if (actionType != null && actionType.equals("SAVEANDCLOSE")){
          FORWARD_final = FORWARD_saveandclose;
        }
        remote.setTaxMatrix(taxMatrixValue);
      } catch (Exception e) {
        logger.error("[Exception] ViewSourceSettingHandler.Execute Handler ", e);
        e.printStackTrace();
      }
    }

    dynaform.set("taxClassValue","");
    dynaform.set("taxJurisdictionsValue","");
    dynaform.set("taxClassID","");
    dynaform.set("taxJurisdictionsID","");
    request.setAttribute("masterdataform",dynaform);
    return mapping.findForward(FORWARD_final);
  }

}

