/*
 * $RCSfile: EntityMerge.java,v $    $Revision: 1.2 $  $Date: 2005/07/26 20:19:18 $ - $Author: mcallist $
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
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class EntityMerge extends Action
{
  private static Logger logger = Logger.getLogger(EntityMerge.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException,CommunicationException, NamingException 
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    // use the search result to forward either to next grouping or
    // to the final "congratulations, you just FUBARed all your data" page.
    HttpSession session = request.getSession(true);
    MergeSearchResultVO searchResult = (MergeSearchResultVO)session.getAttribute("mergeSearchResult");
    MergeResultBean mergeResult = (MergeResultBean)session.getAttribute("mergeResult");
    if (mergeResult == null) {
      mergeResult = new MergeResultBean();
    }
    int contactType = searchResult.getMergeType();

    UserObject userObject = (UserObject)session.getAttribute("userobject");
    int individualId = userObject.getIndividualID();

    DynaActionForm mergeEntityDetails = (DynaActionForm)form;

    // Anyway, we get the users selections from the formbean
    String idSelect = (String)mergeEntityDetails.get("idSelect");
    String primaryContactSelect = (String)mergeEntityDetails.get("primaryContactSelect");
    String [] addressCheckBox = (String[])mergeEntityDetails.get("addressCheckBox");
    String [] methodOfContactCheckBox = (String[])mergeEntityDetails.get("methodOfContactCheckBox");

    // Grab the VO collections too, we will be needing some of them.
    ArrayList entityVOs = (ArrayList)mergeEntityDetails.get("entityVOs");
    ArrayList individualVOs = (ArrayList)mergeEntityDetails.get("individualVOs");
    ArrayList addressVOs = (ArrayList)mergeEntityDetails.get("addressVOs");
    ArrayList methodOfContactVOs = (ArrayList)mergeEntityDetails.get("methodOfContactVOs");

    // get the EntityVO for the matching id we picked.  To serve as a starting point.
    EntityVO survivor = null;
    int entityId = Integer.parseInt(idSelect);
    for (int i = 0; (survivor == null) && (i < entityVOs.size()); i++) {
      EntityVO current = (EntityVO)entityVOs.get(i);
      if (current.getContactID() == entityId) {
        survivor = current;
      }
    }

    // Now we have our survivor, lets update the fields.

    IndividualVO primaryContact = null;
    int primaryContactId = 0;
    for (int i = 0; (primaryContact == null) && (i < individualVOs.size()); i++) {
      IndividualVO current = (IndividualVO)individualVOs.get(i);
      if (current.getContactID() == Integer.parseInt(primaryContactSelect)) {
        primaryContact = current;
        primaryContactId = current.getContactID();
        primaryContact.setEntityID(entityId);
      }
    }

    // Time to start using the EJB layer
    ContactFacade remoteContactFacade = null;
    ContactFacadeHome contactFacadeHome = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
    try {
      remoteContactFacade = contactFacadeHome.create();
      remoteContactFacade.setDataSource(dataSource);
    } catch (Exception e) {
      logger.error("[Exception] EntityMerge.Execute Handler ", e);
      throw new ServletException(e);
    }

    ArrayList addressCheckList = new ArrayList();
    for (int i = 0; i < addressCheckBox.length; i++) {
      int addressId = Integer.parseInt(addressCheckBox[i]);
      addressCheckList.add(addressId+"");
      remoteContactFacade.changeAddressRelate(addressId, entityId, contactType);
    }

    // delete un-used addresses
    // Of course only delete the ones that weren't selected
    // thats what all the following convoluted code does
    for (int i = 0; i < addressVOs.size(); i++) {
      int currentId = ((AddressVO)addressVOs.get(i)).getAddressID();
      if (!addressCheckList.contains(currentId+"")) {
        try {
          remoteContactFacade.deleteAddress(currentId, entityId, individualId);
        } catch (RemoteException re) {
          logger.error("[Exception] EntityMerge.Execute Handler ", re);
        } catch (AuthorizationFailedException afe) {
          logger.error("[Exception] EntityMerge.Execute Handler ", afe);
        }
      }
    }

    ArrayList methodOfContactCheckList = new ArrayList();
    for (int i = 0; i < methodOfContactCheckBox.length; i++) {
      int mocId = Integer.parseInt(methodOfContactCheckBox[i]);
      methodOfContactCheckList.add(mocId+"");
      remoteContactFacade.changeMOCRelate(mocId, entityId, contactType);
    }

    // delete un-used mocs.
    // Of course only delete the ones that weren't selected
    // thats what all the following convoluted code does
    for (int i = 0; i < methodOfContactVOs.size(); i++) {
      int currentId = ((MethodOfContactVO)methodOfContactVOs.get(i)).getMocID();

      if (!methodOfContactCheckList.contains(currentId+"")) {
        try {
          remoteContactFacade.deleteMOC(currentId, entityId, individualId);
        } catch (RemoteException re) {
          logger.error("[Exception] EntityMerge.Execute Handler ", re);
        } catch (AuthorizationFailedException afe) {
          logger.error("[Exception] EntityMerge.Execute Handler ", afe);
        }
      }
    }
    
    MergedEntityConfirmationBean mergedEntityBean = (MergedEntityConfirmationBean)session.getAttribute("mergedEntity");
    
    int listID = mergedEntityBean.getListID();

    // populate the surviving VO
    survivor.setName(mergedEntityBean.getEntityName());
    survivor.setExternalID(mergedEntityBean.getId2());
    survivor.setAccManager(mergedEntityBean.getAccountManagerID());
    survivor.setAccTeam(mergedEntityBean.getAccountTeamID());
    survivor.setSource(mergedEntityBean.getSourceID());
    survivor.setSourceName(mergedEntityBean.getSourceName());
    survivor.setIndividualID(primaryContactId);
    survivor.setIndividualVO(primaryContact);
    survivor.setModifiedBy(individualId);
    survivor.setList(listID);
    survivor.clearMOC();  // Don't make millions of MOCs


    // Move all Individuals to the surviving record.
    // I can either update the IndividualVOs and call update
    // Or do a DB query.  For now I am going to do the former, but we
    // maybe should update to do the latter, to save precious Compute
    // resources.  But for now this is cleaner.
    for (int i = 0; i < individualVOs.size(); i++) {
      IndividualVO current = (IndividualVO)individualVOs.get(i);
      current.setEntityID(entityId);
      current.setList(listID);
      current.setIsPrimaryContact("NO");
      
      try {
        remoteContactFacade.updateIndividual(current, individualId);
      } catch (RemoteException re) {
        logger.error("[Exception] EntityMerge.Execute Handler ", re);
      } catch (AuthorizationFailedException afe) {
        logger.error("[Exception] EntityMerge.Execute Handler ", afe);
      }
    }

     // update the surviving record information.
    try {
      remoteContactFacade.updateEntity(survivor, individualId);
      String entityIDs[] = new String[1];
      entityIDs[0] = entityId + "";
      // Reason for adminIndividualID to set as -1. We know that admin is a super user
      // he can move any thing. thats why its hard coded to -1
      int adminIndividualID = -1;
      remoteContactFacade.entityMove(adminIndividualID, listID, entityIDs);
    } catch (RemoteException re) {
      logger.error("[Exception] EntityMerge.Execute Handler ", re);
    } catch (AuthorizationFailedException afe) {
      logger.error("[Exception] EntityMerge.Execute Handler ", afe);
    }


    // PURGE!!!!
    // As Nobushige drew his sword, Hakuin remarked, "Here open the gates of hell!"
    ArrayList deletedEntities = new ArrayList();
    for (int i = 0; i < entityVOs.size(); i++) {
      int currentId = ((EntityVO)entityVOs.get(i)).getContactID();
      if (currentId != entityId) {
        try {
          remoteContactFacade.deleteEntity(currentId, individualId);
        } catch (RemoteException re) {
          logger.error("[Exception] EntityMerge.Execute Handler ", re);
        } catch (AuthorizationFailedException afe) {
          logger.error("[Exception] EntityMerge.Execute Handler ", afe);
        }
        deletedEntities.add(new Integer(currentId));
      }
    }

    int purged = mergeResult.getRecordsPurged();
    purged += deletedEntities.size();
    mergeResult.setRecordsPurged(purged);
    int merged = mergeResult.getMergesCompleted();
    merged++;
    mergeResult.setMergesCompleted(merged);
    session.setAttribute("mergeResult",mergeResult);

    // Now rolling up the orphaned records.
    Merge remoteMerge = null;
    MergeHome mergeHome = (MergeHome)CVUtility.getHomeObject("com.centraview.administration.merge.MergeHome", "Merge");
    try {
      remoteMerge = mergeHome.create();
      remoteMerge.setDataSource(dataSource);
    } catch (Exception e) {
      logger.error("[Exception] EntityMerge.Execute Handler ", e);
      throw new ServletException(e);
    }
    remoteMerge.rollUpEntityOrphans(deletedEntities, entityId);

    // Well that was exciting.  Now are there more merges to do?
    if (searchResult.hasNextGrouping()) {
      return (mapping.findForward("displaynextsearchresults"));
    }
    // If we don't have more to merge we should do some housekeeping on the session.
    session.removeAttribute("mergeSearchResult");
    session.removeAttribute("mergeSearchForm");
    session.removeAttribute("mergedEntity");
    session.removeAttribute("mergeEntityDetials");

    return mapping.findForward(".view.administration.merge_complete");
  }
}
