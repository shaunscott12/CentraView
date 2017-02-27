/*
 * $RCSfile: ImportHandler.java,v $    $Revision: 1.2 $  $Date: 2005/08/04 21:14:49 $ - $Author: mcallist $
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

package com.centraview.marketing;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.UserObject;
import com.centraview.customfield.CustomField;
import com.centraview.customfield.CustomFieldHome;
import com.centraview.file.CvFileFacade;
import com.centraview.file.CvFileVO;
import com.centraview.settings.Settings;

/**
 * @author: Naresh Patel
 */
public class ImportHandler extends org.apache.struts.action.Action {
  private static Logger logger = Logger.getLogger(ImportHandler.class);

  /**
   * @return org.apache.struts.action.ActionForward
   * @param param org.apache.struts.action.ActionMapping
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException,
      ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    CustomFieldHome cfh = (CustomFieldHome) CVUtility.getHomeObject("com.centraview.customfield.CustomFieldHome", "CustomField");
    try {
      HttpSession session = request.getSession(true);
      // We must have to remove the attribute from the session.
      session.removeAttribute("backgroundImportMembers");

      ListMemberForm dyn = (ListMemberForm) session.getAttribute("importListForm");
      UserObject userobject = (UserObject) session.getAttribute("userobject");
      int indvID = userobject.getIndividualID();

      FormFile ff = dyn.getTheFile();
      String strf = ff.getFileName();
      InputStream im = ff.getInputStream();

      CvFileFacade cvfile = new CvFileFacade();
      CvFileVO flvo = new CvFileVO();
      flvo.setTitle("EmailAttachment");
      flvo.setName(strf);
      flvo.setCreatedBy(indvID);
      flvo.setOwner(indvID);
      int emailAttachmentResult = cvfile.addEmailAttachment(indvID, flvo, im, dataSource);

      flvo = cvfile.getEmailAttachment(indvID, emailAttachmentResult, dataSource);
      String path = flvo.getPhysicalFolderVO().getFullPath(null, true) + flvo.getName();

      session.setAttribute("path", path);

      // fill vector with column name
      Vector v = new Vector();
      String hasHeader = dyn.getHeaderrow();
      // int vectorColCounter = 0;

      RandomAccessFile ral = new RandomAccessFile(new File(path), "r");
      String readline = "";

      String tabDelimiter = dyn.getFieldseprator();
      String lineDelimiter = dyn.getLineseprator();
      if (tabDelimiter.equals("")) {
        tabDelimiter = dyn.getTab();
      }
      if (lineDelimiter.equals("")) {
        lineDelimiter = dyn.getLine();
      }

      if (hasHeader.equals("Yes") || (hasHeader.equals("No"))) {
        readline = ral.readLine();
        String[] sFileFieldName = readline.split(tabDelimiter);
        for (int k = 0; k < sFileFieldName.length; k++) {
          v.add(new DDNameValue(sFileFieldName[k], sFileFieldName[k]));
        }
      }

      dyn.setHeadervector(v);
      dyn.setFilePath(path);

      Collection importEntityList = new ArrayList();
      Collection importIndividualList = new ArrayList();

      Collection mapImport = new ArrayList();
      mapImport.add(new DDNameValue("1", "--- Select ---"));
      mapImport.add(new DDNameValue("2", "First Name"));
      mapImport.add(new DDNameValue("3", "Middle Name"));
      mapImport.add(new DDNameValue("4", "Last Name"));
      mapImport.add(new DDNameValue("5", "Entity Name"));
      mapImport.add(new DDNameValue("6", "ExternalID (Entity)"));
      mapImport.add(new DDNameValue("6", "ExternalID (Individual)"));
      mapImport.add(new DDNameValue("7", "Street1"));
      mapImport.add(new DDNameValue("8", "Street2"));
      mapImport.add(new DDNameValue("9", "City"));
      mapImport.add(new DDNameValue("10", "State"));
      mapImport.add(new DDNameValue("11", "Zipcode"));
      mapImport.add(new DDNameValue("12", "Country"));
      mapImport.add(new DDNameValue("13", "Title"));
      mapImport.add(new DDNameValue("14", "Source (Individual)"));
      mapImport.add(new DDNameValue("14", "Source (Entity)"));
      mapImport.add(new DDNameValue("15", "Home Phone"));
      mapImport.add(new DDNameValue("16", "Fax Phone"));
      mapImport.add(new DDNameValue("17", "Main Phone"));
      mapImport.add(new DDNameValue("18", "Mobile Phone"));
      mapImport.add(new DDNameValue("19", "Other Phone"));
      mapImport.add(new DDNameValue("20", "Pager Phone"));
      mapImport.add(new DDNameValue("21", "Work Phone"));
      mapImport.add(new DDNameValue("22", "Email"));
      mapImport.add(new DDNameValue("23", "Website"));
      mapImport.add(new DDNameValue("24", "Account Manager"));
      mapImport.add(new DDNameValue("25", "Account Team"));

      CustomField remote = cfh.create();
      remote.setDataSource(dataSource);

      Collection entityCustomFieldList = remote.getCustomFieldImportData("entity");
      Collection individualCustomFieldList = remote.getCustomFieldImportData("individual");

      if (entityCustomFieldList != null) {
        Vector entityCustomField = new Vector(entityCustomFieldList);

        if (entityCustomField != null) {
          for (Enumeration e = entityCustomField.elements(); e.hasMoreElements();) {
            HashMap hm = (HashMap) e.nextElement();

            String title = hm.get("name").toString() + "(Entity)";
            String listID = hm.get("customfieldid").toString();
            String type = hm.get("fieldtype").toString();
            mapImport.add(new DDNameValue(listID, title));
            importEntityList.add(new DDNameValue(title, listID + "*" + type));
          }
          dyn.setCustomEntityList(importEntityList);

        }
      }

      if (individualCustomFieldList != null) {
        Vector indivdualCustomField = new Vector(individualCustomFieldList);

        if (indivdualCustomField != null) {
          for (Enumeration e = indivdualCustomField.elements(); e.hasMoreElements();) {
            HashMap hm = (HashMap) e.nextElement();

            String title = hm.get("name").toString() + "(Individual)";
            String listID = hm.get("customfieldid").toString();
            String type = hm.get("fieldtype").toString();
            mapImport.add(new DDNameValue(listID, title));
            importIndividualList.add(new DDNameValue(title, listID + "*" + type));
          }
          dyn.setCustomIndividualList(importIndividualList);
        }
      }
      request.setAttribute("mapImport", mapImport);
    } catch (Exception e) {
      logger.error("[Exception] ImportHandler.Execute Handler ", e);
    }
    return (mapping.findForward(".view.marketing.listmanager.import"));
  }
}
