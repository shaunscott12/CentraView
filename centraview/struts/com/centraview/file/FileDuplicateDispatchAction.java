/*
 * $RCSfile: FileDuplicateDispatchAction.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:53 $ - $Author: mking_cv $
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

package com.centraview.file;

import java.io.IOException;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.UserObject;

/**
 * Duplicates files or folders.
 * Created: Oct 28, 2004
 * 
 * @author CentraView, LLC. 
 */
public class FileDuplicateDispatchAction extends Action {
  private static Logger logger = Logger.getLogger(FileDuplicateDispatchAction.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    HttpSession session = request.getSession(true);
    UserObject userObject = (UserObject)session.getAttribute("userobject");
    int individualId = userObject.getIndividualID();
    
    String rowId = request.getParameter("rowId");
    String type = null;
    
    if(rowId != null && !rowId.equals("")) {
      String elements[] = rowId.split("\\*");
      rowId = elements[0];
      type = elements[1];
    }
        
    if (type.equals("FILE"))
      return new ActionForward("/files/file_dup.do?rowId=" + rowId, true);
    else if (type.equals("FOLDER"))
      return new ActionForward("/files/folder_dup.do?rowId=" + rowId, true);
    
    return null;
  }
}
