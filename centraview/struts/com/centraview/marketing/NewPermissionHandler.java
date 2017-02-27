/*
 * $RCSfile: NewPermissionHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:17 $ - $Author: mking_cv $
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
/*
 * 	NewPermissionHandler
 *	Used to display new permission page
 *	Date 19 Aug 2003
 * 	@author  Sunita
 * 	@version 1.0
*/

package com.centraview.marketing;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class NewPermissionHandler extends org.apache.struts.action.Action {

    // Global Forwards
    public static final String GLOBAL_FORWARD_failure = "failure";
    
    // Local Forwards
    private static final String FORWARD_newliterature = "displayLiterature";
    private static String FORWARD_final = GLOBAL_FORWARD_failure;
    
    public NewPermissionHandler() {
        // TODO: Write constructor body
    }
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
	    try 
	    {			
	    	System.out.println("NewPermissionHandler::execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)::entry");

	    	// get session
	    	HttpSession session = request.getSession();
	    	// hash map for add literature
	    	HashMap literatureHashMap = null;
	    	// to identify multiple opened window
	    	String newWindowId = "1";

			request.setAttribute("Menu","Permission");

	    	FORWARD_final = FORWARD_newliterature;
	    } catch (Exception e)	
	    {
	    	e.printStackTrace();
	    	FORWARD_final = GLOBAL_FORWARD_failure;		
	    }
	        return mapping.findForward(FORWARD_final);
    }
}
