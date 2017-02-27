/*
 * $RCSfile: CustomerProfileHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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

package com.centraview.customer.profile;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.GlobalMasterLists;
import com.centraview.common.UserObject;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.contact.entity.EntityVO;
import com.centraview.contact.helper.AddressVO;
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.contact.individual.IndividualVO;
import com.centraview.settings.Settings;



public class CustomerProfileHandler extends Action
{

  private static Logger logger = Logger.getLogger(CustomerProfileHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user
    int entityID = userObject.getEntityId();    // logged in user

    ActionErrors allErrors = new ActionErrors();

    // "customerProfileForm", defined in cv-struts-config.xml
    DynaActionForm profileForm = (DynaActionForm)form;

    // get the customer profile from the database
    ContactFacadeHome cfh = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome","ContactFacade");
    
    try {
      ContactFacade remote = (ContactFacade)cfh.create();
      remote.setDataSource(dataSource);
      
      // get the Entity record of the logged-in user's Entity
      EntityVO entityVO = (EntityVO)remote.getEntity(entityID);
      
      if (entityVO == null) {
        // let the user know something bad happened
        allErrors.add("error.general.databaseError", new ActionMessage("error.general.databaseError"));
        entityVO = new EntityVO();
      }
      
      IndividualVO primaryContactVO = entityVO.getIndividualVO();
      if (primaryContactVO == null) {
        // avoid null pointers
        primaryContactVO = new IndividualVO();
      }
      
      AddressVO primaryAddress = entityVO.getPrimaryAddress();
      if (primaryAddress == null) {
        // avoid null pointers
        primaryAddress = new AddressVO();
      }
      
      // now, populate the form bean with the data
      profileForm.set("entityName", entityVO.getName());
      profileForm.set("firstName", primaryContactVO.getFirstName());
      profileForm.set("middleName", primaryContactVO.getMiddleName());
      profileForm.set("lastName", primaryContactVO.getLastName());
      profileForm.set("title", primaryContactVO.getTitle());
      profileForm.set("street1", primaryAddress.getStreet1());
      profileForm.set("street2", primaryAddress.getStreet2());
      profileForm.set("city", primaryAddress.getCity());
      profileForm.set("zipCode", primaryAddress.getZip());
      profileForm.set("website", primaryAddress.getWebsite());
      
      GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
      Vector stateList = (Vector)gml.get("States");
      profileForm.set("state", primaryAddress.getStateName());
      Vector countryList = (Vector)gml.get("Country");
      profileForm.set("country", primaryAddress.getCountryName());
      
      Collection mocList = entityVO.getMOC();
      Iterator iterator = mocList.iterator();
      int count = 1;
      while (iterator.hasNext()) {
        MethodOfContactVO moc = (MethodOfContactVO)iterator.next();
        if (moc.getMocType() == 1 && moc.getIsPrimary().equalsIgnoreCase("YES")) {
          // this is for email
          profileForm.set("email", moc.getContent());
        } else if (count < 4 && moc.getMocType() != 1) {
          profileForm.set("mocType" + count, new Integer(moc.getMocType()).toString());
          String mocContent = moc.getContent();
          String mocContentValue = "";
          String mocContentExt = "";
          if (mocContent.indexOf("EXT") != -1) {
            String tempContent = mocContent;
            mocContentValue = tempContent.substring(0,tempContent.indexOf("EXT"));
            mocContentExt = tempContent.substring(tempContent.indexOf("EXT")+3,tempContent.length())  ;
          }else{
            mocContentValue = mocContent;
          }
          profileForm.set("mocContent" + count, mocContentValue );
          profileForm.set("mocExt" + count, mocContentExt );
          count++;
        }
      }
      
      if (! allErrors.isEmpty()) {
        saveErrors(request, allErrors);
      }
    }catch(Exception e){
      logger.error("[Exception] CustomerProfileHandler.Execute Handler ", e);
    }
    return(mapping.findForward(".view.customer.profile"));
  }   // end execute() method

}   // end class definition

