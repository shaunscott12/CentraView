/*
 * $RCSfile: IndividualMergeConfirm.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:11 $ - $Author: mcallist $
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * This class simply constructs a result form bean for the end
 * user to confirm against, this is based on the selections on
 * the mergeIndividualDetails form that is on the session.
 * @author Naresh Patel <npatel@centraview.com>
 */
public class IndividualMergeConfirm extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    HttpSession session = request.getSession(true);

    // Get the searchResult from the session.  We will need to know what stuff to delete.
    // I think entity deletion takes the MOCs and Addresses with it, so before doing the Delete
    // We need to re-associate the MOCs and Addresses.
    // However this page will just build a formbean to confirm the values.
    DynaActionForm mergeIndividualDetails = (DynaActionForm)form;

    // Get what was picked:
    // each of the following must be populated by the user.
    // well the checboxen can be blank, but the MOC and Addresses will be
    // tossed out with the garbage.
    String nameRadio = (String)mergeIndividualDetails.get("nameRadio");
    String marketingListSelect = (String)mergeIndividualDetails.get("marketingListSelect");
    String idSelect = (String)mergeIndividualDetails.get("idSelect");
    String id2Radio = (String)mergeIndividualDetails.get("id2Radio");
    String entitySelect = (String)mergeIndividualDetails.get("entitySelect");
    String sourceRadio = (String)mergeIndividualDetails.get("sourceRadio");
    String [] addressCheckBox = (String[])mergeIndividualDetails.get("addressCheckBox");
    String [] methodOfContactCheckBox = (String[])mergeIndividualDetails.get("methodOfContactCheckBox");
    String customFirst = (String)mergeIndividualDetails.get("customFirst");
    String customMiddle = (String)mergeIndividualDetails.get("customMiddle");
    String customLast = (String)mergeIndividualDetails.get("customLast");
    String customId2 = (String)mergeIndividualDetails.get("customId2");

    ArrayList nameCollection = (ArrayList)mergeIndividualDetails.get("nameCollection");
    ArrayList idCollection = (ArrayList)mergeIndividualDetails.get("idCollection");
    ArrayList id2Collection = (ArrayList)mergeIndividualDetails.get("id2Collection");
    ArrayList entityNameCollection = (ArrayList)mergeIndividualDetails.get("entityNameCollection");
    ArrayList sourceCollection = (ArrayList)mergeIndividualDetails.get("sourceCollection");
    ArrayList methodOfContactCollection = (ArrayList)mergeIndividualDetails.get("methodOfContactCollection");
    ArrayList addressCollection = (ArrayList)mergeIndividualDetails.get("addressCollection");
    ArrayList marketingListCollection = (ArrayList)mergeIndividualDetails.get("marketingListCollection");

    String firstName = null;
    String middleName = null;
    String lastName = null;
    String individualName = "";

    if (nameRadio.equals("custom")) {
      firstName = (String)mergeIndividualDetails.get("customFirst");
      middleName = (String)mergeIndividualDetails.get("customMiddle");
      lastName = (String)mergeIndividualDetails.get("customLast");
      if (middleName != null && !middleName.equals("")) {
        middleName += middleName + ". ";
      } else {
        middleName = "";
      }
      individualName = firstName + " " + middleName + lastName;
    } else {
      individualName = MergeUtil.getCollectionLabel(nameRadio, nameCollection);

      int tempIndexOfSpace = individualName.indexOf(" ");
      int tempIndexOfDotSpace = individualName.indexOf(". ");
      int tempNameLen = individualName.length();
      if (tempIndexOfSpace > -1) {
        firstName = individualName.substring(0,tempIndexOfSpace);
        if (tempIndexOfDotSpace > -1) {
          middleName = individualName.substring((tempIndexOfSpace+1),tempIndexOfDotSpace);
          lastName = individualName.substring((tempIndexOfDotSpace+2),tempNameLen);
        } else {
          if ((tempIndexOfSpace+1) != tempNameLen) {
            lastName = individualName.substring((tempIndexOfSpace+1),tempNameLen);
          }
        }
      }
    }
    
    int id = 0;
    if (idSelect != null && !idSelect.equals("")) {
      id = Integer.parseInt(idSelect);
    }
    
    String id2 = null;
    if (id2Radio.equals("custom")) {
      id2 = (String)mergeIndividualDetails.get("customId2");
    } else {
      id2 = MergeUtil.getCollectionLabel(id2Radio, id2Collection);
    }
    
    int entityID = 0;
    if (entitySelect != null && !entitySelect.equals("")) {
      entityID = Integer.parseInt(entitySelect);
    }
    String entityName = MergeUtil.getCollectionLabel(entitySelect, entityNameCollection);
    
    int sourceID = 0;
    if (sourceRadio != null && !sourceRadio.equals("")) {
      try {
        sourceID = Integer.parseInt(sourceRadio);
      } catch (NumberFormatException e) { }
    }
    String source = MergeUtil.getCollectionLabel(sourceRadio, sourceCollection);
    if (source == null) {
      // must be custom
      source = (String)mergeIndividualDetails.get("customSourceName");
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
    MergedIndividualConfirmationBean mergedIndividual = new MergedIndividualConfirmationBean();
    mergedIndividual.setIndividualName(individualName);
    mergedIndividual.setId(id);
    mergedIndividual.setId2(id2);
    mergedIndividual.setEntityName(entityName);
    mergedIndividual.setFirstName(firstName);
    mergedIndividual.setMiddleName(middleName);
    mergedIndividual.setLastName(lastName);
    mergedIndividual.setEntityID(entityID);
    mergedIndividual.setListID(listID);
    mergedIndividual.setSourceID(sourceID);
    mergedIndividual.setSourceName(source);
    mergedIndividual.setAddressCollection(selectedAddressCollection);
    mergedIndividual.setMethodOfContactCollection(selectedMethodOfContactCollection);
    mergedIndividual.setMarketingList(marketingListName);

    // stick the bean on the request
    request.setAttribute("mergedIndividual", mergedIndividual);
    session.setAttribute("mergedIndividual", mergedIndividual);

    return mapping.findForward(".view.administration.individual_merge_confirm");
  }
}
