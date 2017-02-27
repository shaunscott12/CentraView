/*
 * $RCSfile: BlockEmailHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:49 $ - $Author: mking_cv $
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

package com.centraview.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.email.emailfacade.EmailFacade;
import com.centraview.email.emailfacade.EmailFacadeHome;
import com.centraview.settings.Settings;


public class BlockEmailHandler extends org.apache.struts.action.Action
{


  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

	String blockForward = "displaylistruleemail";
    try
    {
        DynaActionForm dynaForm = (DynaActionForm)form;

		HttpSession session = request.getSession(true);
		UserObject userobjectd = (UserObject)session.getAttribute("userobject");
        int individualId = userobjectd.getIndividualID();

		EmailFacadeHome cfh = (EmailFacadeHome)CVUtility.getHomeObject("com.centraview.email.emailfacade.EmailFacadeHome", "EmailFacade");
		EmailFacade remote = (EmailFacade)cfh.create();
		remote.setDataSource(dataSource);

		int accountid        = 0;
		String accountID           = (String) request.getParameter("accountid");

		if (accountID != null && !accountID.equals(""))
		{
		  accountid = Integer.parseInt(accountID);
		} //end of if statement (aid != null && !aid.equals(""))

		//System.out.println("accountID"+accountID);




		//System.out.println("accountid"+accountid);

		FolderList fl        = (FolderList) session.getAttribute("folderlist");


		int folderid = fl.getDefaultFolder();

		if(request.getParameter("folderid")!= null)
			folderid =Integer.parseInt(request.getParameter("folderid"));

		//System.out.println("folderid"+folderid);


		Set listkey = fl.keySet();
		Iterator it = listkey.iterator();
		while(it.hasNext())
		{
			AccountDetail ad1 =(AccountDetail)fl.get(it.next());

			String accountAddress = ad1.getAccountaddress();
			ArrayList folderList = ad1.getFolderList() ;

			int parentID = 0;
			String path = "";
			Folder f = null;
			int id = 0;
			for( int i=0 ; i < folderList.size(); i++ )
			{
					f = (Folder)folderList.get( i );
					id = f.getFolderid();
					accountid = ad1.getAccountid();
					if(id==folderid)
					{
						break;
					}
			}
			if (accountid != 0){
				break;
			}
		}

	    //System.out.println("accountid Before Accounting Detail"+accountid);
		AccountDetail ad     = (AccountDetail) fl.get(new Integer(accountid));
		ArrayList al         = ad.getFolderList();
		Folder f;

		boolean createJunkMailFolder = true;

		int parentId = 0;
		int junkMailId = 0;
		for (int i = 0; i < al.size(); i++)
		{
		f = (Folder) al.get(i);

			if (f.getFoldername().equalsIgnoreCase("trash")
			   && f.getFtype().equalsIgnoreCase("system"))
			{
			  parentId = f.getParentfolderid();
			  //break;
			}
			if (f.getFoldername().equalsIgnoreCase("junk mail")
			   && f.getFtype().equalsIgnoreCase("user"))
			{
			   junkMailId = f.getFolderid();
			   createJunkMailFolder = false;
			  //break;
			}
			//end of if statement (f.getFoldername().equalsIgnoreCase("trash")
			  // && f.getFtype().equalsIgnoreCase("user"))
		} //end of for loop (int i = 0; i < al.size(); i++)

		if (parentId != 0){
			createJunkMailFolder = true;
		}


//System.out.println("parentId"+parentId);
//System.out.println("createJunkMailFolder"+createJunkMailFolder);

		if(createJunkMailFolder){

			HashMap hmFolder = new HashMap();
			hmFolder.put("AccountID", new Integer(accountid));
			hmFolder.put("SubfolderID", new Integer(parentId));
			hmFolder.put("foldername", "Junk Mail");
			int i = remote.checkFoldersPresence(individualId, hmFolder);
			if (i != 0) // 0 means folder is present already
			{
			  junkMailId = remote.addFolder(individualId, hmFolder);

  			  FolderList folderList = (FolderList)remote.getFolderList(individualId);
			  session.setAttribute("folderlist", folderList);
			}
		}

//System.out.println("parentId After"+parentId);

        String messageID[] = request.getParameterValues("rowId");
        ArrayList emailFrom = remote.getEmailsFrom(messageID);

//System.out.println("emailFrom"+emailFrom);

		int ruleId = remote.getRuleId(accountid);

//System.out.println("ruleId Before"+ruleId);

		boolean rulecreated = false;
		if (ruleId == 0){

			HashMap hm = new HashMap();
			hm.put("RuleName", "JunkMail");
			hm.put("Description", "JunkMail");
			hm.put("EnabledStatus", "YES");
			hm.put("AccountID", accountid+"");

			int size = emailFrom.size();

			//System.out.println("size"+size);
			String join[] = new String[size];
			String field[] = new String[size];
			String condition[] = new String[size];
			String criteria[] = new String[size];

			for (int i=0; i<size; i++){
				join[i]="1";
				field[i]="1";
				condition[i]="3";
				String fromEmail = (emailFrom.get(i)).toString();
				int startIndex = fromEmail.indexOf("<");
				int endIndex = fromEmail.indexOf(">");
				if (startIndex >0){
					fromEmail = fromEmail.substring(startIndex,(endIndex-1));
				}
				criteria[i]=fromEmail;
				//System.out.println("emailFrom"+fromEmail);
			}
			hm.put("Join", join);
			hm.put("Field", field);
			hm.put("Condition", condition);
			hm.put("Criteria", criteria);

			hm.put("ActionMoveMessage", "1");
			hm.put("MoveFolderId", junkMailId+"");
			hm.put("ActionDeleteMessage", "0");
			hm.put("ActionMarkasRead", "0");
			hm.put("JunkMail", "TRUE");
			ruleId = remote.addRule(hm);
			rulecreated = true;
		}

		//System.out.println("Rule Id Final :"+ruleId);

		RuleDetails ruledetails = remote.getRuleDetails(ruleId);
		dynaForm.set("RuleID", ruledetails.getRuleID());
		dynaForm.set("AccountID", ruledetails.getAccountID());
		dynaForm.set("name", ruledetails.getName());
		dynaForm.set("description", ruledetails.getDescription());
		dynaForm.set("enabled", ruledetails.getEnabled());

		if (!rulecreated){

			int size = emailFrom.size();
			//System.out.println("size Before"+size);

			String colA[] = ruledetails.getcolA();
			String colB[] = ruledetails.getcolB();
			String colC[] = ruledetails.getcolC();
			String colD[] = ruledetails.getcolD();

			int colSize = colA.length;
			//System.out.println("col Before"+colSize);

			int finalSize = size + colSize - 1 ;

			//System.out.println("size"+size);

			String join[] = new String[finalSize];
			String field[] = new String[finalSize];
			String condition[] = new String[finalSize];
			String criteria[] = new String[finalSize];


			for (int i=0; i < colSize; i++){
				join[i]=colA[i];
				field[i]=colB[i];
				condition[i]=colC[i];
				criteria[i]=colD[i];
				//System.out.println("criteria[i]"+criteria[i]);
			}

			int j = 0;
			for (int i=colSize-1; i < finalSize; i++){
				join[i]="1";
				field[i]="1";
				condition[i]="3";

				String fromEmail = (emailFrom.get(j)).toString();
				int startIndex = fromEmail.indexOf("<");
				int endIndex = fromEmail.indexOf(">");
				if (startIndex >0){
					fromEmail = fromEmail.substring(startIndex+1,endIndex);
				}

				criteria[i]=fromEmail;
				//System.out.println("emailFrom"+fromEmail);
				j++;
			}

			dynaForm.set("colA", join);
			dynaForm.set("colB", field);
			dynaForm.set("colC", condition);
			dynaForm.set("colD", criteria);
		}
		else{
			dynaForm.set("colA", ruledetails.getcolA());
			dynaForm.set("colB", ruledetails.getcolB());
			dynaForm.set("colC", ruledetails.getcolC());
			dynaForm.set("colD", ruledetails.getcolD());
		}

		dynaForm.set("movemessageto", ruledetails.getMovemessageto());
		dynaForm.set("movemessagetofolder", ruledetails.getMovemessagetofolder());
		dynaForm.set("markasread", ruledetails.getMarkasread());
		dynaForm.set("deletemessage", ruledetails.getDeleteMessage());
		request.setAttribute("foldername", "Junk Mail");




		blockForward = "viewrule";

    }catch(Exception e){
      e.printStackTrace();
    }
    return (mapping.findForward(blockForward));
  }
}

