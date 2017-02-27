/*
 * $RCSfile: EntityMergeConfirm.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:11 $ - $Author: mcallist $
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

/**
 * This class simply constructs a result form bean for the end user
 * to confirm against, this is based on the selections on the mergeEntityDetails form
 * that is on the session.
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class EntityMergeConfirm extends Action
{
  private static Logger logger = Logger.getLogger(EntityMergeConfirm.class);
  
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    HttpSession session = request.getSession(true);
  
    // Get the searchResult from the session.  We will need to know what stuff to delete.
    // I think entity deletion takes the MOCs and Addresses with it, so before doing the Delete
    // We need to re-associate the MOCs and Addresses.
    // However this page will just build a formbean to confirm the values.
    DynaActionForm mergeEntityDetails = (DynaActionForm)form;

    // Get what was picked:
    // each of the following must be populated by the user.
    // well the checboxen can be blank, but the MOC and Addresses will be
    // tossed out with the garbage.
    String nameRadio = (String)mergeEntityDetails.get("nameRadio");
    String marketingListSelect = (String)mergeEntityDetails.get("marketingListSelect");
    String idSelect = (String)mergeEntityDetails.get("idSelect");
    String id2Radio = (String)mergeEntityDetails.get("id2Radio");
    String primaryContactSelect = (String)mergeEntityDetails.get("primaryContactSelect");
    String accountManagerRadio = (String)mergeEntityDetails.get("accountManagerRadio");
    String accountTeamRadio = (String)mergeEntityDetails.get("accountTeamRadio");
    String sourceRadio = (String)mergeEntityDetails.get("sourceRadio");
    String [] addressCheckBox = (String[])mergeEntityDetails.get("addressCheckBox");
    String [] methodOfContactCheckBox = (String[])mergeEntityDetails.get("methodOfContactCheckBox");

    ArrayList nameCollection = (ArrayList)mergeEntityDetails.get("nameCollection");
    ArrayList id2Collection = (ArrayList)mergeEntityDetails.get("id2Collection");
    ArrayList individualNameCollection = (ArrayList)mergeEntityDetails.get("individualNameCollection");
    ArrayList accountManagerCollection = (ArrayList)mergeEntityDetails.get("accountManagerCollection");
    ArrayList accountTeamCollection = (ArrayList)mergeEntityDetails.get("accountTeamCollection");
    ArrayList sourceCollection = (ArrayList)mergeEntityDetails.get("sourceCollection");
    ArrayList addressCollection = (ArrayList)mergeEntityDetails.get("addressCollection");
    ArrayList methodOfContactCollection = (ArrayList)mergeEntityDetails.get("methodOfContactCollection");
    ArrayList marketingListCollection = (ArrayList)mergeEntityDetails.get("marketingListCollection");

    String entityName = null;
    if (nameRadio.equals("custom")) {
      entityName = (String)mergeEntityDetails.get("customName");
    } else {
      entityName =  MergeUtil.getCollectionLabel(nameRadio, nameCollection);
    }

    int id = 0;
    if (idSelect != null) {
      id = Integer.parseInt(idSelect);
    }

    String id2 = null;
    if (id2Radio.equals("custom")) {
      id2 = (String)mergeEntityDetails.get("customId2");
    } else {
      id2 = MergeUtil.getCollectionLabel(id2Radio, id2Collection);
    }


    // make sure the accountTeam, accountManager and source are
    // all valid numbers.
    // default to 0 which means there isn't one assigned.
    int accountManagerID = 0;
    int accountTeamID = 0;
    int sourceID = 0;
    try {
      if (accountManagerRadio!= null && !accountManagerRadio.equals("")) {
        accountManagerID = Integer.parseInt(accountManagerRadio);
      }
    } catch (NumberFormatException e){ }
    
    try {
      if (accountTeamRadio!= null && !accountTeamRadio.equals("")) {
        accountTeamID = Integer.parseInt(accountTeamRadio);
      }
    } catch (NumberFormatException e) { }
    
    try {
      if (sourceRadio!= null && !sourceRadio.equals("")) {
        sourceID = Integer.parseInt(sourceRadio);
      }
    } catch (NumberFormatException e) { }


    String primaryContactName = MergeUtil.getCollectionLabel(primaryContactSelect, individualNameCollection);
    String accountManager = MergeUtil.getCollectionLabel(accountManagerRadio, accountManagerCollection);
    if (accountManager == null) {
      // must be custom
      accountManager = (String)mergeEntityDetails.get("customAccountManagerName");
    }

    String accountTeam = MergeUtil.getCollectionLabel(accountTeamRadio, accountTeamCollection);
    if (accountTeam == null) {
      // must be custom
      accountTeam = (String)mergeEntityDetails.get("customAccountTeamName");
    }

    String source = MergeUtil.getCollectionLabel(sourceRadio, sourceCollection);
    if (source == null) {
      // must be custom
      source = (String)mergeEntityDetails.get("customSourceName");
    }

    // now iterate the checboxen and build a collection to disply for validation
    ArrayList selectedAddressCollection = new ArrayList();
    for (int i = 0; i < addressCheckBox.length; i++) {
      String address = MergeUtil.getCollectionLabel(addressCheckBox[i], addressCollection);
      if (address != null) {
        selectedAddressCollection.add(address);
      }
    }

    ArrayList selectedMethodOfContactCollection = new ArrayList();
    for (int i = 0; i < methodOfContactCheckBox.length; i++) {
      String moc = MergeUtil.getCollectionLabel(methodOfContactCheckBox[i], methodOfContactCollection);
      if (moc != null) {
        selectedMethodOfContactCollection.add(moc);
      }
    }

    int listID = 0;
    if (marketingListSelect != null && !marketingListSelect.equals("")) {
      listID = Integer.parseInt(marketingListSelect);
    }
    String marketingListName = MergeUtil.getCollectionLabel(marketingListSelect, marketingListCollection);

    // populate the bean for display.
    MergedEntityConfirmationBean mergedEntity = new MergedEntityConfirmationBean();
    mergedEntity.setEntityName(entityName);
    mergedEntity.setId(id);
    mergedEntity.setId2(id2);
    mergedEntity.setPrimaryContactName(primaryContactName);
    mergedEntity.setAccountManagerName(accountManager);
    mergedEntity.setAccountTeamName(accountTeam);
    mergedEntity.setSourceName(source);
    mergedEntity.setAddressCollection(selectedAddressCollection);
    mergedEntity.setMethodOfContactCollection(selectedMethodOfContactCollection);
    mergedEntity.setMarketingList(marketingListName);
    mergedEntity.setListID(listID);
    mergedEntity.setAccountManagerID(accountManagerID);
    mergedEntity.setAccountTeamID(accountTeamID);
    mergedEntity.setSourceID(listID);

    // stick the bean on the request
    request.setAttribute("mergedEntity", mergedEntity);
    session.setAttribute("mergedEntity", mergedEntity);

    return mapping.findForward(".view.administration.entity_merge_confirm");
  }
}
