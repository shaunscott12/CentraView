/*
 * $RCSfile: EntityMergeDetail.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:44 $ - $Author: mking_cv $
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

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

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
 * This method will take the details of the selected entities
 * and stick them in a form bean so they can be displayed to the end
 * user.  Basically there is nothing clever about the way it is done.
 * Just plow through the fields from beginning to end.  And set them
 * on the form-bean.
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class EntityMergeDetail extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    HttpSession session = request.getSession(true);
    MergeSearchResultVO searchResult = (MergeSearchResultVO)session.getAttribute("mergeSearchResult");
    
    UserObject userObject = (UserObject)session.getAttribute("userobject");
    int individualId = userObject.getIndividualID();

    String[] mergeIdArray = (String[])session.getAttribute("mergeIdArray");
    session.removeAttribute("mergeIdArray");
    // Get the VOs for each of these Ids.
    ArrayList entityVOs = MergeUtil.getEntityVOs(mergeIdArray, dataSource);
    // The entityVO only carries the IndividualVO that is the primary contact
    // So we should just get them all.
    ArrayList individualVOs = MergeUtil.getIndividualVOsForEntityIds(mergeIdArray, dataSource);
    // The entityVOs each have a collection of their associated MOC VOs
    // in a field.  (ContactVO.getMOC())
    ArrayList methodOfContactVOs = new ArrayList();
    for (int i = 0; i < entityVOs.size(); i++) {
      methodOfContactVOs.addAll(((EntityVO)entityVOs.get(i)).getMOC());
    }
    // only the primary address is in the ContactVO so we have to get them all. 
    int mergeType = searchResult.getMergeType();
    ArrayList addressVOs = MergeUtil.getAddressVOs(mergeIdArray, mergeType, dataSource);
    // The customfields are in the ContactVO too.
    // TODO merge custom fields

    // Populate the form with the information.
    DynaActionForm mergeEntityDetails = (DynaActionForm)form;
    ArrayList nameCollection = new ArrayList();
    ArrayList idCollection = new ArrayList();
    ArrayList id2Collection = new ArrayList();
    ArrayList individualNameCollection = new ArrayList();
    ArrayList accountManagerCollection = new ArrayList();
    ArrayList accountTeamCollection = new ArrayList();
    ArrayList sourceCollection = new ArrayList();
    ArrayList methodOfContactCollection = new ArrayList();
    ArrayList addressCollection = new ArrayList();
    ArrayList marketingListCollection = new ArrayList();

    // Get the MarketingLists from the database, so you can set the result.
    ArrayList rawMarketingList = new ArrayList();
    try {
      ContactFacadeHome contactFacadeHome = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      ContactFacade remoteContactFacade = contactFacadeHome.create();
      remoteContactFacade.setDataSource(dataSource);
      rawMarketingList = (ArrayList)remoteContactFacade.getAllMarketingList();
    } catch (Exception e) {
      System.out.println("[Exception][MergeSearch.execute] Exception Thrown: Getting Marketing Lists: "+e);
      throw new ServletException(e);
    }

    for (int i = 0; i < rawMarketingList.size(); i++) {
      HashMap currentList = (HashMap)rawMarketingList.get(i);
      marketingListCollection.add(new LabelValueBean((String)currentList.get("title"), ((Number)currentList.get("listid")).toString()));
    }
    
    for (int i = 0; i < entityVOs.size(); i++) {
      // build up the collections with the unique values from each VO.
      EntityVO entity = (EntityVO)entityVOs.get(i);
      LabelValueBean currentName = new LabelValueBean(entity.getName(), String.valueOf(i));
      boolean flag = false;
      for (int j = 0; !flag && (j < nameCollection.size()); j++) {
        if (((LabelValueBean)nameCollection.get(j)).getLabel().equals(currentName.getLabel())) {
          flag = true;
        }
      }

      if (!flag) {
        nameCollection.add(currentName);
      }

      LabelValueBean currentId = new LabelValueBean(String.valueOf(entity.getContactID()), String.valueOf(entity.getContactID()));
      idCollection.add(currentId);

      String currentId2String = entity.getExternalID();
      LabelValueBean currentId2 = new LabelValueBean(currentId2String, String.valueOf(i));
      boolean flag2 = false;
      for (int j = 0; !flag2 && (j < id2Collection.size()); j++) {
        String id2CollectionLabel = ((LabelValueBean)id2Collection.get(j)).getLabel();
        if ((currentId2String == null) || // if string is null for some reason
           (currentId2String.length() == 0) || // if the length == 0
           (id2CollectionLabel.equals(currentId2String))) // if it is equal to an existing one
        {
          flag2 = true; // Don't add it.
        }
      }

      if (!flag2) {
        id2Collection.add(currentId2);
      }
      
      LabelValueBean currentAccountManager = new LabelValueBean(entity.getAcctMgrName(), String.valueOf(entity.getAccManager()));
      if (!accountManagerCollection.contains(currentAccountManager) &&
          !(currentAccountManager.getValue().equals("0") || currentAccountManager.getLabel().equals(""))) {
        accountManagerCollection.add(currentAccountManager);
      }
      
      LabelValueBean currentAccountTeam = new LabelValueBean(entity.getAcctTeamName(), String.valueOf(entity.getAccTeam()));
      if (!accountTeamCollection.contains(currentAccountTeam) &&
          !(currentAccountTeam.getValue().equals("0") || currentAccountTeam.getLabel().equals(""))) {
        accountTeamCollection.add(currentAccountTeam);
      }
      
      LabelValueBean currentSource = new LabelValueBean(entity.getSourceName(), String.valueOf(entity.getSource()));
      if (!sourceCollection.contains(currentSource) && 
          !(currentSource.getValue().equals("") || currentSource.getLabel().equals(""))) {
        sourceCollection.add(currentSource);
      }
    } // end for(int i = 0; i < entityVOs.size(); i++)

    // populate bean from AddressVO
    for (int i=0; i < addressVOs.size(); i++) {
      AddressVO address = (AddressVO)addressVOs.get(i);
      StringBuffer addressLabel = new StringBuffer();
      // I know, bad programmer, HTML stuff shouldn't be here.
      // I blame the guy who designed the prototype.
      // Anyway its only two little <br> tags, it won't hurt anyone.
      // Plus it will make the other screen look "nice."
      addressLabel.append(address.getStreet1()+"<br>");
      addressLabel.append(address.getStreet2()+"<br>");
      addressLabel.append(address.getCity()+", ");
      addressLabel.append(address.getStateName()+", ");
      addressLabel.append(address.getZip()+", ");
      addressLabel.append(address.getCountryName());
      LabelValueBean currentAddress = new LabelValueBean(addressLabel.toString(), String.valueOf(address.getAddressID()));
      addressCollection.add(currentAddress);
    } // end for (int i=0; i < addressVOs.size(); i++)
    
    // populate bean with all MOC vos
    for (int i = 0; i < methodOfContactVOs.size(); i++) {
      MethodOfContactVO moc = (MethodOfContactVO)methodOfContactVOs.get(i);
      StringBuffer mocLabel = new StringBuffer();
      mocLabel.append(moc.getMocTypeName());
      mocLabel.append(":&nbsp;");
      mocLabel.append(moc.getContent());
      LabelValueBean currentMOC = new LabelValueBean(mocLabel.toString(), String.valueOf(moc.getMocID()));
      methodOfContactCollection.add(currentMOC);
    } // end for (int i = 0; i < methodOfContactVOs.size(); i++)

    // populate bean with all related Individual vos
    for (int i = 0; i < individualVOs.size(); i++) {
      IndividualVO individual = (IndividualVO)individualVOs.get(i);
      StringBuffer individualLabel = new StringBuffer();
      individualLabel.append(individual.getFirstName());
      individualLabel.append(" ");
      individualLabel.append(individual.getLastName());
      LabelValueBean currentIndividual = new LabelValueBean(individualLabel.toString(), String.valueOf(individual.getContactID()));
      individualNameCollection.add(currentIndividual);
    } // end for (int i = 0; i < individualVOs.size(); i++)

    mergeEntityDetails.set("nameCollection", nameCollection);
    mergeEntityDetails.set("idCollection", idCollection);
    mergeEntityDetails.set("id2Collection", id2Collection);
    mergeEntityDetails.set("individualNameCollection", individualNameCollection);
    mergeEntityDetails.set("accountManagerCollection", accountManagerCollection);
    mergeEntityDetails.set("accountTeamCollection", accountTeamCollection);
    mergeEntityDetails.set("sourceCollection", sourceCollection);
    mergeEntityDetails.set("methodOfContactCollection", methodOfContactCollection);
    mergeEntityDetails.set("addressCollection", addressCollection);
    mergeEntityDetails.set("marketingListCollection", marketingListCollection);
    // Stick the VO collections on here, it will make life easier when building
    // the final merge VO.
    // Yes it is wasteful of resources, since this information is also in the above
    // collections, but this is more detail, and that is scrubbed for display
    // It will make the actual merge code much simpler and easier to understand.
    mergeEntityDetails.set("entityVOs", entityVOs);
    mergeEntityDetails.set("individualVOs", individualVOs);
    mergeEntityDetails.set("addressVOs", addressVOs);
    mergeEntityDetails.set("methodOfContactVOs", methodOfContactVOs);


    return (mapping.findForward(".view.administration.entity_merge_detail"));
  }
  
}
