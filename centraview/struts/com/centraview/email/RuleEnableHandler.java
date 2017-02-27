/*
 * $RCSfile: RuleEnableHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:51 $ - $Author: mking_cv $
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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.DisplayList;
import com.centraview.common.ListGenerator;
import com.centraview.settings.Settings;

public class RuleEnableHandler extends org.apache.struts.action.Action {


/**
 * RuleEnableHandler constructor comment.
 */

public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
{
  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

  
  System.out.println( "*********** RuleEnableHandler ********** " );  
    
  String rowId[]=null;
  String listType=null;
  
  HttpSession session = request.getSession(true);  

  rowId =request.getParameterValues("rowId");
  listType=request.getParameter("listType");

  String enable =request.getParameter("enable");
  boolean status = false ;
  
  if ( enable.equals( "0" ) ) 
  {
    status = false ;
  }
  
  if ( enable.equals( "1" ) ) 
  {
    status = true ;
  }
    

  System.out.println( "*********** RuleEnableHandler ********** " +enable );  

  System.out.println("Row ID"+ rowId[0] +" ListType  "+listType);

  // id in global list
  String listId = request.getParameter("listId");

  long idd = 0;
  if(listId != null)
    idd = Long.parseLong( listId  );

  ListGenerator lg = ListGenerator.getListGenerator(dataSource);
  RuleList DL = ( RuleList )lg.getDisplayList(idd );
  
  //added by sameer
  DisplayList displaylistSession = ( DisplayList )session.getAttribute( "displaylist") ;
  if((displaylistSession!=null)&&(displaylistSession.getListID()==idd))
      displaylistSession.setDirtyFlag(true);
      
  for( int l=0 ; l < rowId.length ; l++ )
  {
    DL.enableElement( rowId[l] , status   );
    DL.setDirtyFlag(true);    
    System.out.println( "Enabled " );  
  }

  request.setAttribute( "displaylist" , DL  );

  
  return ( mapping.findForward( "enable"+listType  ) );

  }

}
