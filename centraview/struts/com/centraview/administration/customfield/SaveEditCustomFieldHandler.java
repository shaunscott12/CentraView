/*
 * $RCSfile: SaveEditCustomFieldHandler.java,v $    $Revision: 1.3 $  $Date: 2005/09/07 19:38:07 $ - $Author: mcallist $
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
 * SaveEditCustomFieldHandler.java
 *
 */

package com.centraview.administration.customfield;

import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.contact.helper.CustomFieldVO;
import com.centraview.customfield.CustomField;
import com.centraview.customfield.CustomFieldHome;
import com.centraview.settings.Settings;

public class SaveEditCustomFieldHandler  extends org.apache.struts.action.Action
{

   // Global Forwards
   public static final String GLOBAL_FORWARD_failure = "failure";

   // Local Forwards
   private static final String FORWARD_savenew = "savenew";
   private static final String FORWARD_saveclose = "saveclose";

   private static String FORWARD_final = GLOBAL_FORWARD_failure;


   public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
   {
     String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

      try
      {
         HttpSession session = request.getSession();
         int IndividualId = ((UserObject)session.getAttribute("userobject")).getIndividualID();
         String typeOfSave = "saveclose";
         int customfieldid =Integer.parseInt((String)request.getParameter("fieldid"));
         if (request.getParameter("buttonpress") != null)
            typeOfSave = request.getParameter("buttonpress");


         DynaActionForm cf= (DynaActionForm) form;
         // initialize customfield vo

         CustomFieldVO customfieldVO = new CustomFieldVO();

         String module = (String)request.getParameter("module");
         String fieldtype = (String)cf.get("layerSwitch");

         int tableId = 0;

         customfieldVO.setFieldID(customfieldid);
         customfieldVO.setLabel(cf.get("fieldname").toString());
         // customfieldVO.setRecordType(tableName);
         customfieldVO.setFieldType(fieldtype);

          Integer recordTypeId = (Integer)cf.get("recordTypeId");
         customfieldVO.setRecordTypeID(recordTypeId.intValue());

         if (fieldtype.equals(CustomFieldVO.MULTIPLE))
         {
            String[] optionValues=(String[])cf.get("valuelist");
            Vector vecOption = new Vector();
            HashMap vecOptionIds = new HashMap();
            for(int i=0;i<optionValues.length;i++) {

               vecOption.addElement(optionValues[i]);

               if (request.getParameter("optionName" + optionValues[i]) != null) {

                  vecOptionIds.put(optionValues[i]
                     , request.getParameter("optionName" + optionValues[i]));

               }
            }
            customfieldVO.setOptionValues(vecOption);
            customfieldVO.setOptionValuesIds(vecOptionIds);
         }

         CustomFieldHome customfieldHome = (CustomFieldHome)CVUtility.getHomeObject("com.centraview.customfield.CustomFieldHome","CustomField");
         CustomField customfieldRemote = (CustomField)customfieldHome.create();
         customfieldRemote.setDataSource(dataSource);
         
         customfieldRemote.updateNewCustomField(customfieldVO);

         // TODO updateNewCustomField EJB needs user-rights?
         if (typeOfSave.equals("savenew")) {

            request.setAttribute("typeofmodule",AdministrationConstantKeys.MODULESETTINGS);
            request.setAttribute("typeofsubmodule",module);
            request.setAttribute("module",module);
            FORWARD_final = FORWARD_savenew;
         }
         else if(typeOfSave.equals("saveclose")) {

            request.setAttribute("typeofmodule",AdministrationConstantKeys.MODULESETTINGS);
            request.setAttribute("typeofsubmodule",module);
            request.setAttribute("module",module);
            FORWARD_final = FORWARD_saveclose;
         }
      }
      catch (Exception e) {
         System.out.println("[Exception][SaveEditCustomFieldHandler.execute] Exception Thrown: "+e);
         e.printStackTrace();
         FORWARD_final = GLOBAL_FORWARD_failure;
      }
      return (mapping.findForward(FORWARD_final));
   }
}
