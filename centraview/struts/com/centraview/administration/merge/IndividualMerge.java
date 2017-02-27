/*
 * $RCSfile: IndividualMerge.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:44 $ - $Author: mking_cv $
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

package com.centraview.administration.merge;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.ejb.RemoveException;
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
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.contact.entity.EntityVO;
import com.centraview.contact.helper.AddressVO;
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.contact.individual.IndividualVO;
import com.centraview.settings.Settings;

/**
 * @author Naresh Patel <npatel@centraview.com>
 */
public class IndividualMerge extends Action
{
  private static Logger logger = Logger.getLogger(IndividualMerge.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, NamingException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    HttpSession session = request.getSession(true);
    
    MergeSearchResultVO searchResult = (MergeSearchResultVO)session.getAttribute("mergeSearchResult");
    MergeResultBean mergeResult = (MergeResultBean)session.getAttribute("mergeResult");
    if (mergeResult == null) {
      mergeResult = new MergeResultBean();
    }
    int contactType = searchResult.getMergeType();
    
    UserObject userObject = (UserObject)session.getAttribute("userobject");
    int individualId = userObject.getIndividualID();
    
    DynaActionForm mergeIndvidualDetails = (DynaActionForm)form;
    
    String idSelect = (String)mergeIndvidualDetails.get("idSelect");
    String [] addressCheckBox = (String[])mergeIndvidualDetails.get("addressCheckBox");
    String [] methodOfContactCheckBox = (String[])mergeIndvidualDetails.get("methodOfContactCheckBox");
    
    // Grab the VO collections too, we will be needing some of them.
    ArrayList entityVOs = (ArrayList)mergeIndvidualDetails.get("entityVOs");
    ArrayList individualVOs = (ArrayList)mergeIndvidualDetails.get("individualVOs");
    ArrayList addressVOs = (ArrayList)mergeIndvidualDetails.get("addressVOs");
    ArrayList methodOfContactVOs = (ArrayList)mergeIndvidualDetails.get("methodOfContactVOs");
    
    // get the IndividualVO for the matching id we picked.  To serve as a starting point.
    IndividualVO individualVO = null;
    int contactID = Integer.parseInt(idSelect);
    ArrayList deletedIndividuals = new ArrayList();
    
    for (int i = 0; i < individualVOs.size(); i++) {
      IndividualVO current = (IndividualVO)individualVOs.get(i);
      
      if (current.getContactID() == contactID) {
        individualVO = current;
      } else {
        deletedIndividuals.add(new Integer(current.getContactID()));
      }
    }
    
    // Time to start using the EJB layer
    ContactFacade remoteContactFacade = null;
    try {
      ContactFacadeHome contactFacadeHome = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      remoteContactFacade = contactFacadeHome.create();
      remoteContactFacade.setDataSource(dataSource);
    } catch (Exception e) {
      logger.error("[Exception] IndividualMerge.Execute Handler ", e);
      throw new ServletException(e);
    }
    
    ArrayList addressCheckList = new ArrayList();
    for (int i = 0; i < addressCheckBox.length; i++) {
      int addressId = Integer.parseInt(addressCheckBox[i]);
      addressCheckList.add(addressId+"");
      remoteContactFacade.changeAddressRelate(addressId, contactID, contactType);
    }
    
    // delete un-used addresses
    // Of course only delete the ones that weren't selected
    // thats what all the following convoluted code does
    
    for (int i = 0; i < addressVOs.size(); i++) {
      int currentId = ((AddressVO)addressVOs.get(i)).getAddressID();
      if (! addressCheckList.contains(currentId+"")) {
        try {
          remoteContactFacade.deleteAddress(currentId, contactID, individualId);
        } catch (RemoteException re) {
          logger.error("[Exception] IndividualMerge.Execute Handler ", re);
        } catch (AuthorizationFailedException afe) {
          logger.error("[Exception] IndividualMerge.Execute Handler ", afe);
        }
      }
    }
    
    ArrayList methodOfContactCheckList = new ArrayList();
    for (int i = 0; i < methodOfContactCheckBox.length; i++) {
      int mocId = Integer.parseInt(methodOfContactCheckBox[i]);
      methodOfContactCheckList.add(mocId+"");
      remoteContactFacade.changeMOCRelate(mocId, contactID, contactType);
    }
    
    
    // delete un-used mocs.
    // Of course only delete the ones that weren't selected
    // thats what all the following convoluted code does
    for (int i = 0; i < methodOfContactVOs.size(); i++) {
      int currentId = ((MethodOfContactVO)methodOfContactVOs.get(i)).getMocID();
      
      if (! methodOfContactCheckList.contains(currentId+"")) {
        try {
          remoteContactFacade.deleteMOC(currentId, contactID, individualId);
        } catch (RemoteException re) {
          logger.error("[Exception] IndividualMerge.Execute Handler ", re);
        } catch (AuthorizationFailedException afe) {
          logger.error("[Exception] IndividualMerge.Execute Handler ", afe);
        }
      }
    }
    
    MergedIndividualConfirmationBean mergedIndividualBean = (MergedIndividualConfirmationBean)session.getAttribute("mergedIndividual");
    
    // populate the individualVO
    int entityID = mergedIndividualBean.getEntityID();
    
    EntityVO entityVO = null;
    for (int i = 0; (entityVO == null) && (i < entityVOs.size()); i++) {
      EntityVO current = (EntityVO)entityVOs.get(i);
      if (current.getContactID() == entityID) {
        entityVO = current;
      }
    }
    
    int listID = mergedIndividualBean.getListID();
    if (entityID == 1) {
      listID = entityVO.getList();
    }
    
    individualVO.setFirstName(mergedIndividualBean.getFirstName());
    individualVO.setMiddleName(mergedIndividualBean.getMiddleName());
    individualVO.setLastName(mergedIndividualBean.getLastName());
    individualVO.setExternalID(mergedIndividualBean.getId2());
    individualVO.setSource(mergedIndividualBean.getSourceID());
    individualVO.setSourceName(mergedIndividualBean.getSourceName());
    individualVO.setEntityID(entityID);
    individualVO.setModifiedBy(individualId);
    individualVO.setList(listID);
    individualVO.clearMOC();  // Don't make millions of MOCs
    
    // update the individualVO record information.
    try {
      remoteContactFacade.updateIndividual(individualVO, individualId);
      String entityIDs[] = new String[1];
      entityIDs[0] = entityID+"";
      // Reason for adminIndividualID to set as -1. We know that admin is a super user
      // he can move any thing. thats why its hard coded to -1
      int adminIndividualID = -1;
      remoteContactFacade.entityMove(adminIndividualID, listID, entityIDs);
    } catch (RemoteException re) {
      logger.error("[Exception] IndividualMerge.Execute Handler ", re);
    } catch (AuthorizationFailedException afe) {
      logger.error("[Exception] IndividualMerge.Execute Handler ", afe);
    }
    
    for (int i = 0; i < deletedIndividuals.size(); i++) {
      int currentId = ((Integer)deletedIndividuals.get(i)).intValue();
      try {
        remoteContactFacade.deleteIndividual(currentId, individualId);
      } catch (RemoteException re) {
        logger.error("[Exception] IndividualMerge.Execute Handler ", re);
      } catch (AuthorizationFailedException afe) {
        logger.error("[Exception] IndividualMerge.Execute Handler ", afe);
      } catch (RemoveException rme) {
        logger.error("[Exception] IndividualMerge.Execute Handler ", rme);
      }
    }
    
    int purged = mergeResult.getRecordsPurged();
    purged += deletedIndividuals.size();
    mergeResult.setRecordsPurged(purged);
    int merged = mergeResult.getMergesCompleted();
    merged++;
    mergeResult.setMergesCompleted(merged);
    session.setAttribute("mergeResult",mergeResult);
    
    // Now rolling up the orphaned records.
    Merge remoteMerge = null;
    try {
      MergeHome mergeHome = (MergeHome)CVUtility.getHomeObject("com.centraview.administration.merge.MergeHome", "Merge");
      remoteMerge = mergeHome.create();
      remoteMerge.setDataSource(dataSource);
    } catch (Exception e) {
      logger.error("[Exception] IndividualMerge.Execute Handler ", e);
      throw new ServletException(e);
    }
    remoteMerge.rollUpIndividualOrphans(deletedIndividuals, contactID);
    
    mergeIndvidualDetails.set("customFirst","");
    mergeIndvidualDetails.set("customMiddle","");
    mergeIndvidualDetails.set("customLast","");
    mergeIndvidualDetails.set("customId2","");
    mergeIndvidualDetails.set("customSourceName","");
    
    mergeIndvidualDetails.set("nameRadio","value");
    mergeIndvidualDetails.set("id2Radio","value");
    mergeIndvidualDetails.set("sourceRadio","value");
    
    // Well that was exciting.  Now are there more merges to do?
    if (searchResult.hasNextGrouping()) {
      return (mapping.findForward("displaynextsearchresults"));
    }
  
    // If we don't have more to merge we should do some housekeeping on the session.
    session.removeAttribute("mergeSearchResult");
    session.removeAttribute("mergedIndividual");
    session.removeAttribute("mergeSearchForm");
    session.removeAttribute("mergeEntityDetials");
    session.removeAttribute("mergeIdArray");

    return mapping.findForward(".view.administration.merge_complete");
  }
}
