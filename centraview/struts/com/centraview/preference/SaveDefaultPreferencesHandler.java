/*
 * $RCSfile: SaveDefaultPreferencesHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:24 $ - $Author: mking_cv $
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
package com.centraview.preference;

import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.administration.authorization.Authorization;
import com.centraview.administration.authorization.AuthorizationHome;
import com.centraview.common.CVUtility;


public class SaveDefaultPreferencesHandler extends Action
{
  
  public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
  {
    
    HttpSession session=request.getSession(false);
    
    try {
      int uid = Integer.parseInt(request.getParameter("userID").toString());
      
      String viewSelect[] = new String[100];  // BUG HERE!!! What happens when there are more than 100 users?!?!?
      String modifySelect[] = new String[100];
      String deleteSelect[] = new String[100];
      
      Vector view = new Vector();
      Vector modify = new Vector();
      Vector delete = new Vector();
      
      int arrView[] = null;
      int arrModify[] = null;
      int arrDelete[] = null;
      
      if ((String[])request.getParameterValues("view") != null) {
        viewSelect = (String[])request.getParameterValues("view");
        
        if (viewSelect != null) {
          for (int i = 0; i < viewSelect.length; i++) {
            view.addElement(new Integer(viewSelect[i]));
          }
          arrView = new int[view.size()];
          Iterator it = view.iterator();
          int j = 0;
          while (it.hasNext()) {
            arrView[j] = ((Integer)it.next()).intValue();
            j = j + 1;
          }
        }
      }
      
      if ((String[])request.getParameterValues("modify") != null) {
        modifySelect = (String[])request.getParameterValues("modify");
        
        if (modifySelect != null) {
          for (int i = 0; i < modifySelect.length; i++) {
            modify.addElement(new Integer(modifySelect[i]));
          }
          Iterator it = modify.iterator();
          int j = 0;
          arrModify = new int[modify.size()];
          while (it.hasNext()) {
            arrModify[j] = ((Integer)it.next()).intValue();
            j = j + 1;
          }
        }
      }
      
      if ((String[])request.getParameterValues("deleten") != null) {
        deleteSelect = (String[])request.getParameterValues("deleten");
        
        if (deleteSelect != null) {
          for (int i = 0; i < deleteSelect.length; i++) {
            delete.addElement(new Integer(deleteSelect[i]));
          }
          Iterator it = delete.iterator();
          int  j = 0;
          arrDelete = new int[delete.size()];
          while (it.hasNext()) {
            arrDelete[j] = ((Integer)it.next()).intValue();
            j = j + 1;
          }
        }
      }
      
      AuthorizationHome authHome = (AuthorizationHome)CVUtility.getHomeObject("com.centraview.administration.authorization.AuthorizationHome","Authorization");
      Authorization authRemote = (Authorization)authHome.create();
      
      int flag = 0;
      
      if (request.getParameter("isPublic") != null) {
        flag = -1;
      }
      
      authRemote.saveDefaultPermissions(flag,uid,arrView,arrModify,arrDelete);
    } catch(Exception e) {
      System.out.println("Error in SaveDefaultPreferencesHandler: " + e);
      e.printStackTrace();
    }
    return mapping.findForward(".forward.administration.user_list");
  }

}

