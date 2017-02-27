/*
 * $RCSfile: PTViewHandler.java,v $    $Revision: 1.2 $  $Date: 2005/08/01 21:05:13 $ - $Author: mcallist $
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

package com.centraview.printtemplate;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.contact.entity.EntityVO;
import com.centraview.contact.helper.AddressVO;
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.contact.individual.IndividualVO;
import com.centraview.settings.Settings;

/*
 * This class is used for viewing print template data.
 */
public class PTViewHandler extends org.apache.struts.action.Action {
  private static Logger logger = Logger.getLogger(PTViewHandler.class);
  // Global Forwards
  public static final String GLOBAL_FORWARD_failure = "failure";
  // Local Forwards
  private static final String FORWARD_ptpreview = ".view.marketing.mailmerge";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    try {
      final String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
      // Collect the Template information from the Form
      DynaActionForm dynaform = (DynaActionForm) form;
      String ptData = (String) dynaform.get("templateData");
      String subject = (String) dynaform.get("templatesubject");
      String templatefrom = (String) dynaform.get("templatefrom");
      String templatereplyto = (String) dynaform.get("templatereplyto");
      String mergeType = (String) dynaform.get("mergetype");
      // list of Individual's ID
      HashMap individualIds = (HashMap) dynaform.get("toIndividuals");
      ArrayList templateList = new ArrayList();
      ArrayList emailList = new ArrayList();
      Vector fieldList = new Vector();
      fieldList.add("IndividualID");
      fieldList.add("EntityID");
      fieldList.add("Company");
      fieldList.add("FirstName");
      fieldList.add("MiddleInitial");
      fieldList.add("LastName");
      fieldList.add("Title");
      fieldList.add("PrimaryAddress");
      fieldList.add("Street1");
      fieldList.add("Street2");
      fieldList.add("City");
      fieldList.add("State");
      fieldList.add("Zip");
      fieldList.add("Country");
      fieldList.add("Website");
      fieldList.add("ExternalID");
      fieldList.add("Source");
      fieldList.add("Fax");
      fieldList.add("Home");
      fieldList.add("Main");
      fieldList.add("Mobile");
      fieldList.add("Other");
      fieldList.add("Pager");
      fieldList.add("Work");
      fieldList.add("Email");

      if (mergeType.equals("EMAIL")) {
        // Populating the ArrayList with the DDNameValue.
        ArrayList attachments = new ArrayList();
        String[] tempAttachmentMap = (String[]) dynaform.get("attachments");
        if (tempAttachmentMap != null && tempAttachmentMap.length > 0) {
          for (int i = 0; i < tempAttachmentMap.length; i++) {
            String fileKeyName = tempAttachmentMap[i];
            if (fileKeyName != null) {
              int indexOfHash = fileKeyName.indexOf("#");
              if (indexOfHash != -1) {
                int lenString = fileKeyName.length();
                String fileID = fileKeyName.substring(0, indexOfHash);
                String fileName = fileKeyName.substring(indexOfHash + 1, lenString);
                attachments.add(new DDNameValue(fileID + "#" + fileName, fileName));
              }
            }
          }// end of for(int i = 0; i < tempAttachmentMap.length; i++)
        }// end of if (tempAttachmentMap != null && tempAttachmentMap.size() >
          // 0)
        dynaform.set("attachmentList", attachments);
      }
      if (individualIds != null && individualIds.size() != 0) {
        Set key = individualIds.keySet();
        Iterator iterator = key.iterator();
        while (iterator.hasNext()) {
          String keyValue = (String) iterator.next();
          String tempData = ptData;
          int individualId = 0;
          try {
            individualId = Integer.parseInt(keyValue);
          } catch (NumberFormatException e) {
            logger.error("[execute] There was no valid individual Id.", e);
          }
          if (individualId != 0) {
            // calling the IndividualValues with passing the individual it will
            // populate the bean file PrintTemplateField and return the
            // information.
            PrintTemplateField printTemplate = IndividualValues(individualId, dataSource);
            // Before doing any text manipulation, lets check if we are type
            // email, and this
            // Individual has no email address then we can just bail on this
            // individual now.
            if (mergeType.equals("EMAIL") && printTemplate.getEmail().equals("")) {
              // punt on this one and try the next one.
              continue;
            }
            // call the method from PrintTemplateField and replace all the
            // occurance of fieldList from Template Body.
            Class printTemplateClass = printTemplate.getClass();
            for (int j = 0; j < fieldList.size(); j++) {
              String methodName = fieldList.elementAt(j).toString();
              String getmethod = "get" + methodName;
              Method method = printTemplateClass.getMethod(getmethod, null);
              Object methodValue = method.invoke(printTemplate, null);
              String valueString = "";
              if (valueString != null) {
                valueString = (String) methodValue;
              }
              if (valueString != null && valueString.equals("null")) {
                valueString = "";
              }
              String findString = "&lt;" + methodName + "/&gt;";
              tempData = tempData.replaceAll(findString, valueString);
              tempData = tempData.replaceAll("<" + methodName + "/>", valueString);
              tempData = tempData.replaceAll("&lt;" + methodName + "/&gt;", valueString);
            }// end of for (int j = 0; j < fieldList.size(); j++)
            // get the Email Information for the Individual and Store it into
            // emailList if necessary.
            String name = "";
            String address = "";
            try {
              if (mergeType.equals("EMAIL")) {
                address = printTemplate.getEmail();
                name = printTemplate.getFirstName() + " " + printTemplate.getLastName();
                InternetAddress internetAddress = new InternetAddress(address, name);
                emailList.add(internetAddress.toString());
              }
              // templateList is sample template with update information of the
              // Individual.
              templateList.add(tempData);
            } catch (UnsupportedEncodingException e) {
              // If the creation of an InternetAddress fails then skip adding
              // this record in.
              logger.error("[execute] invalid email address, skipping: " + name + ", " + address, e);
            }
          }// end of if (individualId != 0)
        } // end of while (iterator.hasNext())
      } // end of if (individualIds != null && individualIds.size() != 0)
      dynaform.set("toPrint", templateList);
      dynaform.set("emailList", emailList);
      dynaform.set("templatesubject", subject);
      dynaform.set("templatefrom", templatefrom);
      dynaform.set("templatereplyto", templatereplyto);
      FORWARD_final = FORWARD_ptpreview;
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }

  public PrintTemplateField IndividualValues(int individualId, String dataSource)
  {
    PrintTemplateField printTemplateField = new PrintTemplateField();
    try {
      ContactFacadeHome aa = (ContactFacadeHome) CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      ContactFacade remote = aa.create();
      remote.setDataSource(dataSource);
      IndividualVO individualVOReference = remote.getIndividual(individualId);
      printTemplateField.setIndividualID(String.valueOf(individualId));
      printTemplateField.setEntityID(String.valueOf(individualVOReference.getEntityID()));
      int entityId = individualVOReference.getEntityID();
      if (entityId != 0) {
        EntityVO evo = remote.getEntity(entityId);
        printTemplateField.setCompany(evo.getName());
      }
      // set the Primary information of the individual
      printTemplateField.setFirstName(individualVOReference.getFirstName());
      printTemplateField.setMiddleInitial(individualVOReference.getMiddleName());
      printTemplateField.setLastName(individualVOReference.getLastName());
      printTemplateField.setTitle(individualVOReference.getTitle());
      printTemplateField.setExternalID(individualVOReference.getExternalID());
      printTemplateField.setSource(individualVOReference.getSourceName());

      AddressVO primaryAdd = individualVOReference.getPrimaryAddress();
      // Set the Address information
      if (primaryAdd != null) {
        printTemplateField.setStreet1(primaryAdd.getStreet1());
        printTemplateField.setStreet2(primaryAdd.getStreet2());
        printTemplateField.setCity(primaryAdd.getCity());
        String state = primaryAdd.getStateName();
        printTemplateField.setState(state);
        printTemplateField.setZip(primaryAdd.getZip());
        String country = primaryAdd.getCountryName();
        printTemplateField.setCountry(country);
        printTemplateField.setWebsite(primaryAdd.getWebsite());
      } // end if (primaryAdd != null)
      // set the Method of Contacts
      Collection mocList = individualVOReference.getMOC();
      Iterator iterator = mocList.iterator();
      while (iterator.hasNext()) {
        MethodOfContactVO moc = (MethodOfContactVO) iterator.next();
        // TODO Should we make an effort to see if the email address is the
        // primary???
        if (moc.getMocType() == 1) // this is for email
        {
          printTemplateField.setEmail(moc.getContent());
        } else if (moc.getMocType() != 1) {
          String syncAs = moc.getSyncAs();
          if (syncAs != null) {
            if (syncAs.trim().equals("Fax")) {
              printTemplateField.setFax(moc.getContent());
            }
            if (syncAs.trim().equals("Home")) {
              printTemplateField.setHome(moc.getContent());
            }
            if (syncAs.trim().equals("Main")) {
              printTemplateField.setMain(moc.getContent());
            }
            if (syncAs.trim().equals("Mobile")) {
              printTemplateField.setMobile(moc.getContent());
            }
            if (syncAs.trim().equals("Other")) {
              printTemplateField.setOther(moc.getContent());
            }
            if (syncAs.trim().equals("Pager")) {
              printTemplateField.setPager(moc.getContent());
            }
            if (syncAs.trim().equals("Work")) {
              printTemplateField.setWork(moc.getContent());
            }
          } // end of if (syncAs != null)
        } // end of else statement (moc.getMocType() == 1)
      } // end of while
    } catch (Exception e) {
      logger.error("[IndividualValues] Exception thrown.", e);
    }
    return printTemplateField;
  } // end of PrintTemplateField IndividualValues(int individualId)
}
