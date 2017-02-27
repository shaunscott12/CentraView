/*
 * $RCSfile: ImportMembersHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:13 $ - $Author: mking_cv $
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
import java.io.RandomAccessFile;
import java.util.Collection;
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

import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.GlobalMasterLists;
import com.centraview.common.ListGenerator;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

/**
 * Insert the type's description here.
 * Creation date: (6/4/2003 11:46:36 AM)
 * @author: shirish d
 */
public class ImportMembersHandler extends org.apache.struts.action.Action
{

  private static Logger logger = Logger.getLogger(ImportMembersHandler.class);

  /**
   * Insert the method's description here.
   * Creation date: (6/4/2003 11:59:03 AM)
   * @return org.apache.struts.action.ActionForward
   * @param param org.apache.struts.action.ActionMapping
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)   throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

      HttpSession session = request.getSession(true);

      GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
      Vector vecMOC = (Vector)gml.get("MOC");

      int listid = 0;

      if (session.getAttribute("listid") != null)
      {
        listid = Integer.parseInt((String)session.getAttribute("listid"));
      }

      ListMemberForm dyn = (ListMemberForm)session.getAttribute("importListForm");
      UserObject userobject = (UserObject)session.getAttribute("userobject");

      int indvID = 0;

      if (userobject != null)
      {
        indvID = userobject.getIndividualID();
      }

      ListGenerator lg = ListGenerator.getListGenerator(dataSource);
      lg.makeListDirty("Entity");
      lg.makeListDirty("Individual");

      RandomAccessFile ral = new RandomAccessFile(new File(dyn.getFilePath()), "r");

      String readline = "";
      int i = 0;
      //field seperator
      String seperator = dyn.getFieldseprator();

      Vector importList = new Vector();
      Vector headerVector = new Vector();

      String s = "";
      String hasHeader = dyn.getHeaderrow();

      String sFileFieldName = "";

      int headerRow = 0;

      String tabDelimiter = dyn.getFieldseprator();
      String lineDelimiter = dyn.getLineseprator();
      if (tabDelimiter.equals(""))
      {
        tabDelimiter = dyn.getTab();
      }
      if (lineDelimiter.equals(""))
      {
        lineDelimiter = dyn.getLine();
      }

      String headLine = "";
      if (hasHeader.equals("Yes"))
      {
        headLine = ral.readLine();
        headerRow++;
      }

      int iTemp = 0;

      Vector listVector = dyn.getHeadervector();
      int vectorColCounter = 0;

      while ((readline = ral.readLine()) != null)
      {

        String[] lineSeprator = readline.split(lineDelimiter);
        for (int j = 0; j < lineSeprator.length; j++)
        {
          HashMap hm = new HashMap();
          int headerLen = listVector.size();
          String[] sFileFieldList = lineSeprator[j].split(tabDelimiter, (listVector.size() + 1));
          vectorColCounter = 0;
          int fieldLen = sFileFieldList.length;
          if (fieldLen == headerLen)
          {
            for (int k = 0; k < sFileFieldList.length; k++)
            {
              DDNameValue ddnamevalue = (DDNameValue)listVector.get(vectorColCounter);
              String name = ddnamevalue.getName() + vectorColCounter;
              String defValue = ddnamevalue.getName() + "DefaultValue" + vectorColCounter;
              String sDBFieldName = (String)request.getParameter(name);
              String defaultValue = (String)request.getParameter(defValue);
              if (sDBFieldName != null && !(sDBFieldName.equals("--- Select ---")))
              {

                String defaultNameList = sFileFieldList[k];
                if (defaultNameList == null && defaultValue != null)
                {
                  defaultNameList = defaultValue;
                } // end of if

                if (defaultNameList != null && defaultNameList.equals("") && defaultValue != null)
                {
                  defaultNameList = defaultValue;
                } // end of if

                defaultNameList = defaultNameList.replaceAll("\"", "");
                hm.put(sDBFieldName, defaultNameList.trim());
              } // end if block  to not allow (sDBFieldName.equals("--- Select ---")) and null
              vectorColCounter++;
            } // end of for block  used to parse every tabSeprator
          } //end of if block (fieldLen==headerLen)
          hm.put("Line", readline);
          importList.add(hm);
        } // end of for block  used to parse every lineSeprator
      } // end of while

      Collection CustomEntList = dyn.getCustomEntityList();
      Collection CustomIndList = dyn.getCustomIndividualList();

      if (tabDelimiter != null && tabDelimiter.equals("\\t"))
      {
        tabDelimiter = "\t";
      }

      if (lineDelimiter != null && lineDelimiter.equals("\\n"))
      {
        lineDelimiter = "\n";
      }

      //We will check for the instance of the BackgroundImportMembers. and start the import process in background
      BackgroundImportMembers backgroundImportMembers = (BackgroundImportMembers)session.getAttribute("backgroundImportMembers");
	  if (backgroundImportMembers == null || !backgroundImportMembers.isAlive())
	  {
		backgroundImportMembers = new BackgroundImportMembers(importList, headerRow, listid,
								indvID, CustomEntList, CustomIndList, tabDelimiter,
								lineDelimiter, headLine, dataSource,
								"backgroundImportMembers"+indvID);
		backgroundImportMembers.start();
		HashMap importMessage = backgroundImportMembers.getImportedMessages();
		request.setAttribute("Message", importMessage);
		session.setAttribute("backgroundImportMembers", backgroundImportMembers);
	  }
		
	  StringBuffer returnPath = new StringBuffer();
	  returnPath.append(mapping.findForward(".view.marketing.listmanager.importmember").getPath());
		
	  // give the import a few hundred milliseconds in case it doesn't take to long.
	  try
	  {
		// 350 ms is probably a good threshhold.
		Thread.sleep(350);
	  } catch (InterruptedException e) {}
  	  return(new ActionForward(returnPath.toString(), true));
  }// end execute() method
}

