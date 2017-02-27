/*
 * $RCSfile: SaveSettingsHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:36 $ - $Author: mking_cv $
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
 * SaveSettingsHandler is used to update authorization settings
 * 
 */

package com.centraview.administration.authorization;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;

public class SaveSettingsHandler extends org.apache.struts.action.Action {

    /*	
	 *	Global Forwards for exception handling
	 */
    public static final String GLOBAL_FORWARD_failure = "failure";
    
    /*	
	 *	To forward to jsp add_note_c.jsp
	 */
    private static final String FORWARD_SaveSecurityProfileSetting = "SaveSecurityProfileSetting";
	/*
	 *	Redirect constant
	 */
	private static String FORWARD_final = GLOBAL_FORWARD_failure;

    
	/*
	 *	Executes initialization of required parameters and open window for entry of note
	 *	returns ActionForward
	 */    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
	  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		// initialization of required parameter
	    try 
    	{			
			String name="";
			Vector temp_mod_id = new Vector();
			String ModuleName="";
			String profileName="";
			profileName = request.getParameter("profilename");
			
			DynaActionForm dyna = (DynaActionForm)form;
						
			Hashtable ht = new Hashtable();			
			Map test_map = request.getParameterMap();
			Set test_ks = test_map.keySet();
			
			ModuleFieldRightMatrix mfrm = new ModuleFieldRightMatrix();			
					
			Iterator keys = test_ks.iterator();
			Vector vkey = new Vector();					
			String [] arr = (String []) dyna.get("moduleright");
			for(int i = 0;i<arr.length;i++)
			{
				String temp = arr[i];
				int index = temp.indexOf("_M"); 
				if (index != -1 )
				{
					temp = temp.substring(0,index);
					vkey.addElement(temp);				
				}else 
					vkey.addElement(temp);								   	
			}			
			
			String tempmodule ="";
			String temprecord ="";
			for(int i=0;i<vkey.size();i++)
			{
				name = (String) vkey.elementAt(i);	
				if (name.indexOf("_") != -1)
				{
					tempmodule = name.substring(0,name.indexOf("_"));
					if(!(temp_mod_id.contains(tempmodule) ))
						temp_mod_id.addElement(tempmodule);										
						
					temprecord = name.substring((name.indexOf("_")+1),name.length());
					if(!(temp_mod_id.contains(temprecord) ))
						temp_mod_id.addElement(temprecord);										
					
				}else
				{
					tempmodule = name;
					if(!(temp_mod_id.contains(name) ))
						temp_mod_id.addElement(name);										
				}														
			} 

			AuthorizationHome ah = (AuthorizationHome) CVUtility.getHomeObject("com.centraview.administration.authorization.AuthorizationHome","Authorization");
			Authorization auth =(Authorization)ah.create();
			auth.setDataSource(dataSource);
			
			// Saving SubmoduleId and Field and it's rights
			String smodule = "";	
			String srecord = "";		
			String recordType = "";
			String fieldName = "";
			for(int i=0;i<vkey.size();i++)
			{
				String tmodule = (String)vkey.elementAt(i);
				//System.out.println("tmodule = "+tmodule);
				if (tmodule.indexOf("_") != -1)
				{
					smodule = tmodule.substring(0,tmodule.indexOf("_"));
					srecord = tmodule.substring((tmodule.indexOf("_")+1),tmodule.length());
				}else
				{
					smodule = tmodule;
					srecord = tmodule;
				}	
				Set htkeyset = test_map.keySet();
				Iterator fieldset = htkeyset.iterator();
				while (fieldset.hasNext())
				{
					String temp = fieldset.next().toString();
					if (temp.indexOf("_") != -1)
					{
						recordType = temp.substring(0,temp.indexOf("_"));
						if (srecord.equals(recordType))
						{
							fieldName = temp.substring((temp.indexOf("_")+1),temp.length());
							String [] arr3 = (String []) test_map.get(temp);
							System.out.println("smodule = "+smodule+"fieldName = "+fieldName+"right = "+Integer.parseInt(arr3[0]));
							mfrm.setFieldRight(smodule, fieldName, Integer.parseInt(arr3[0]));
						}						
					}
				}								
			}
			
			if (vkey.size() > 0)
			{
				for(int i=0;i<temp_mod_id.size();i++)
				{		
				
					ModuleName = (String)temp_mod_id.elementAt(i);
					mfrm.setVisibleModule(ModuleName);
				}
				int pid = auth.addSecurityProfile(profileName,mfrm);
				request.setAttribute("propfileid",""+pid);
			}
			
			String action = (String)request.getParameter("action");		
			if(action.equals("save"))
				FORWARD_final = "save";
			if(action.equals("saveandnew"))
				FORWARD_final = "saveandnew";
			if(action.equals("saveandclose"))
				FORWARD_final = "saveandclose";
    	}
		catch (Exception e) 
		{
			System.out.println("[Exception][SaveSettingsHandler.execute] Exception Thrown: "+e);
	    	e.printStackTrace();
	    	FORWARD_final = GLOBAL_FORWARD_failure;			
		}
        return mapping.findForward(FORWARD_final);
    }
}
