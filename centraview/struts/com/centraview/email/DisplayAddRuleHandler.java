/*
 * $RCSfile: DisplayAddRuleHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:49 $ - $Author: mking_cv $
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public final class DisplayAddRuleHandler extends Action
{

	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)	throws IOException, ServletException
	{
		try
		{
			System.out.println( "DisplayAddRuleHandler" );
			
			HttpSession session = request.getSession(true);
			FolderList  fl = (FolderList)session.getAttribute("folderlist");
			AccountDetail ad = ( AccountDetail ) fl.get(new Integer(fl.getDefaultaccount()));
			
			
			System.out.println( "DisplayAddRuleHandler" +ad.getAccountid()  );
			
			
			
			
			
			DynaActionForm dynaform = (DynaActionForm)form;
			System.out.println("block::"+request.getParameter("Block"));
			System.out.println("dynaform "+dynaform );		
			
			if ( (request.getParameter("Block") != null) && (request.getParameter("Block").equals("true") ) )
			{
//				String emailid[] = (String[])request.getAttribute("emailid");
//				System.out.println("emailid "+emailid );				
				System.out.println(((org.apache.struts.action.DynaActionForm)request.getAttribute("rulesEmailForm")));
				((org.apache.struts.action.DynaActionForm)request.getAttribute("rulesEmailForm")).set("deletemessage","1");				
				
//				((org.apache.struts.action.DynaActionForm)request.getAttribute("rulesEmailForm")).set("colD",emailid);
			}
			
			request.setAttribute( "AccountID" , new Integer( ad.getAccountid() ).toString()  );	
			
		}
		catch(Exception exe)
		{
			exe.printStackTrace();
		}
		
		return (mapping.findForward("displayAddRule"));
	}

		public void clearForm(DynaActionForm form , ActionMapping mapping)
		{
			form.initialize(mapping);
		}

	}

