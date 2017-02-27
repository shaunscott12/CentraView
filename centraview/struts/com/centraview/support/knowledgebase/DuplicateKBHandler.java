/*
 * $RCSfile: DuplicateKBHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/01 15:31:05 $ - $Author: mcallist $
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

package com.centraview.support.knowledgebase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.Constants;
import com.centraview.common.UserObject;
import com.centraview.support.common.SupportConstantKeys;

public class DuplicateKBHandler extends org.apache.struts.action.Action {
  
  /*	
   *	Global Forwards for exception handling
   */
  final String GLOBAL_FORWARD_failure = "failure";
  
  /*	
   *	Local Forwards for redirecting to jsp list_common_c
   */
  final String FORWARD_editcategory = "editcategory";
  /*	
   *	Local Forwards for redirecting to jsp list_common_c
   */
  final String FORWARD_editkb = "editkb";	
  /*
   *	Redirect constant
   */
  String FORWARD_final = GLOBAL_FORWARD_failure;
  /*
   *	Constructor
   */	
  
  /*
   *	Executes initialization of required parameters and open window for entry of note
   *	returns ActionForward
   */	
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      HttpSession session = request.getSession();
      int userId = ((UserObject)session.getAttribute("userobject")).getIndividualID();
      request.setAttribute(Constants.TYPEOFOPERATION,SupportConstantKeys.DUPLICATE);
      String rowId = request.getParameter("rowId");
      if (rowId != null) {
        String elements[] = rowId.split("\\*");
        if (elements.length != 2)
          throw new Exception("No row selected.");
        if (elements[1].equals("CATEGORY")) {
          FORWARD_final = FORWARD_editcategory;
        } else if (elements[1].equals("KBELEMENT")) {
          FORWARD_final = FORWARD_editkb;
          request.setAttribute(Constants.TYPEOFOPERATION, "editkb");
        } else {
          throw new Exception("No row selected.");
        }
      } else {
        throw new Exception("No row selected.");
      }
    } 
    catch (Exception e) {
      e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;		
    }
    return mapping.findForward(FORWARD_final);
  }
  
}
