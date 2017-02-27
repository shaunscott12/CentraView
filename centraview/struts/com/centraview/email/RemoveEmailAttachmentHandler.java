/*
 * $RCSfile: RemoveEmailAttachmentHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:51 $ - $Author: mking_cv $
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
 
/**
 * RemoveAttachmentHandler.java
 */

package com.centraview.email;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.file.CvFileFacade;
import com.centraview.settings.Settings;


/**
This class does RemoveEmailAttachmentHandler file
*/
public class RemoveEmailAttachmentHandler extends org.apache.struts.action.Action {



/**
This is a overridden method from action class
*/
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
      String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		try
		{
			HttpSession session = request.getSession(true);
			UserObject  userobjectd = (UserObject)session.getAttribute( "userobject" );
			int individualID = userobjectd.getIndividualID();
			
			String filename = request.getParameter( "removefile" );

			HashMap hm = ( HashMap )session.getAttribute( "AttachfileList" );
			Set v = hm.keySet();
			Iterator it = v.iterator();
			Object key = null;
			while( it.hasNext() )
			{
				key = it.next();
				if ( ( (String) hm.get(key) ).equals(filename) )
				{
					break;
				}
			}
			if ( hm!= null )
			{
				CvFileFacade cvfile = new CvFileFacade();
				cvfile.deleteEmailAttachment(individualID,Integer.parseInt(key.toString()), dataSource);
				hm.remove( key );
				session.setAttribute( "AttachfileList" , hm );
			}

		} catch (Exception e)
		{
			System.out.println("[Exception][RemoveEmailAttachmentHandler.execute] Exception Thrown: "+e);
			e.printStackTrace();
		}
		return (mapping.findForward( "attach" ));
    }
}
