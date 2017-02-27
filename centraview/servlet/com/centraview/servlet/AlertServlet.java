/*
 * $RCSfile: AlertServlet.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:11 $ - $Author: mking_cv $
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
package com.centraview.servlet;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.centraview.alert.Alert;
import com.centraview.alert.AlertHome;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class AlertServlet extends HttpServlet
{

  private static Logger logger = Logger.getLogger(AlertServlet.class);
  
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(this.getServletContext())).getDataSource();
    
    HttpSession session = request.getSession(true);
    UserObject userObject = (UserObject)session.getAttribute("userobject");
    int individualID = userObject.getIndividualID();
    
    String alertAppletRequest = request.getParameter("alertapplet");
    
    try 
    {
      Vector alertsList = this.getAlertList(individualID, dataSource);
      ObjectOutputStream outputToApplet = new ObjectOutputStream(response.getOutputStream());
      outputToApplet.writeObject(alertsList);
      outputToApplet.flush();
      outputToApplet.close();
    }catch(Exception e){
      logger.error("[AlertServlet] Exception thrown in service(): ", e);
    }
    
    if (alertAppletRequest.equals("dismissalert"))
    {
      try 
      {
        int alertID = Integer.parseInt(request.getParameter("rowid"));
        this.deleteAlert(individualID, alertID, dataSource);
      }catch (Exception e){
        logger.error("[AlertServlet] Exception thrown in service(): ", e);
      }
    }
  }

  private void deleteAlert(int individualID, int alertID, String dataSource)
  {
    try 
    {
      AlertHome ah = (AlertHome)CVUtility.getHomeObject("com.centraview.alert.AlertHome", "Alert");
      Alert remote = ah.create();
      remote.setDataSource(dataSource);
      remote.deleteAlert(alertID, individualID);
    }catch(Exception e){
      logger.error("[AlertServlet] Exception thrown in deleteAlert(): ", e);
    }
  }

  private Vector getAlertList(int individualID, String dataSource) throws CommunicationException, NamingException
  {
    Vector alertsList = new Vector();

    HashMap hmConf = new HashMap();
    hmConf.put("UserID", new Integer(individualID));
    
    try 
    {
      AlertHome home = (AlertHome)CVUtility.getHomeObject("com.centraview.alert.AlertHome", "Alert");
      Alert remote = (Alert)home.create();
      remote.setDataSource(dataSource);
      
      Collection results = remote.getAlertList(hmConf);
      if (results != null && results.size() > 0)
      {
        Iterator it = results.iterator();
        while (it.hasNext()) 
        {
          HashMap row = (HashMap)it.next();
          String AlertType = (String)row.get("alerttype");
          if (AlertType.equals("ALERT")) 
          {
            Hashtable ht = convertHashMapToHashtable(row);
            alertsList.add(ht);
          }
        }
      }
    }catch (Exception e){
      logger.error("[AlertServlet] Exception thrown in getAlertList(): ", e);
    }
    return alertsList;
  }   // end getAlertList() method

  private Hashtable convertHashMapToHashtable(HashMap hm)
  {
    Hashtable ht = new Hashtable();
    Iterator it = hm.keySet().iterator();
    while (it.hasNext())
    {
      Object key = it.next();
      Object value = hm.get(key);
      if (value != null) 
      {
        ht.put(key, value);
      }
    }
    return ht;
  }   // end convertHashMapToHashtable() method
  
}

