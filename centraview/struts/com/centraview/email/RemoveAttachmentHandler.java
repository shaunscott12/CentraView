/*
 * $RCSfile: RemoveAttachmentHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:51 $ - $Author: mking_cv $
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


/**
This class does RemoveAttachmentHandler file
*/


public class RemoveAttachmentHandler extends org.apache.struts.action.Action {


/**
This is a overridden method from action class
*/


    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
		try
		{

			System.out.println(" In Remove Attachment Handler " );
			HttpSession session = request.getSession(true);
			String filename = request.getParameter( "removefile" );
			
			System.out.println(" In Remove Attachment Handler "+filename  );

			HashMap hm = ( HashMap )session.getAttribute( "AttachfileList" );


			Set v = hm.keySet();
			Iterator it = v.iterator();
			String key = null;

			while( it.hasNext() )
			{
				key = ( String  )it.next();
				if ( hm.get( key ).equals( filename ) )
				{
					break;
				}
			}

			if ( hm!= null )
			{
				System.out.println( "Going to Remote key " +key ); 
				System.out.println( "Going to Remote file " +filename ); 
				hm.remove( key );
				session.setAttribute( "AttachfileList" , hm );
			}

		} catch (Exception e)
		{
			e.printStackTrace();

		}
		
		return (mapping.findForward( "attach" ));
    }
}
