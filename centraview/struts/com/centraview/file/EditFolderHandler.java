/*
 * $RCSfile: EditFolderHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:53 $ - $Author: mking_cv $
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class EditFolderHandler extends org.apache.struts.action.Action {
  //gloal forwards    
  public static final String GLOBAL_FORWARD_failure = "failure";
  // Local Forwards
  private static final String FORWARD_editfolder = ".view.files.editfolder";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;
  
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    
    System.out.println("NewFolderHandler::execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)::entry");
    
    // set request
    request.setAttribute(FileConstantKeys.TYPEOFFILE, FileConstantKeys.FOLDER);
    request.setAttribute(FileConstantKeys.CURRENTTAB, FileConstantKeys.DETAIL);
    request.setAttribute(FileConstantKeys.TYPEOFOPERATION, FileConstantKeys.EDIT);
    request.setAttribute(FileConstantKeys.WINDOWID, "1");
    
    FORWARD_final = FORWARD_editfolder;
    return mapping.findForward(FORWARD_final);
    
  }
}
