/*
 * $RCSfile: IndividualMergeDetail.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:44 $ - $Author: mking_cv $
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
 * This Action Class will get the Individual detail from the
 * EJB layer and populate a mergeIndividualDetails form bean
 * so things can be merged.
 *
 * Yeah, I crappily copied and pasted a lot of code from EntityMergeDetail
 * I should be hung up by my thumbs and flayed.
 *
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class IndividualMergeDetail extends Action
{
  private static Logger logger = Logger.getLogger(IndividualMergeDetail.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    HttpSession session = request.getSession(true);
    MergeSearchResultVO searchResult = (MergeSearchResultVO)session.getAttribute("mergeSearchResult");

    UserObject userObject = (UserObject)session.getAttribute("userobject");
    int individualId = userObject.getIndividualID();

    String[] mergeIdArray = (String[])session.getAttribute("mergeIdArray");
    session.removeAttribute("mergeIdArray");

    // Get the VOs for each of these Ids.
    ArrayList individualVOs = MergeUtil.getIndividualVOs(mergeIdArray, dataSource);

    // The IndividualVOs each have a collection of their associated MOC VOs
    // in a field.  (ContactVO.getMOC())
    ArrayList methodOfContactVOs = new ArrayList();
    ArrayList entityIds = new ArrayList();
    for (int i = 0; i < individualVOs.size(); i++)
    {
      IndividualVO current = (IndividualVO)individualVOs.get(i);
      methodOfContactVOs.addAll(current.getMOC());
      // collect a list of unique entity Ids among the Individuals
      int entityId = current.getEntityID();
      if (entityId > 0 && !entityIds.contains(String.valueOf(entityId)))
      {
        entityIds.add(String.valueOf(entityId));
      }
    }
    // Get the EntityVOs
    ArrayList entityVOs = new ArrayList();
    if (entityIds.size() > 0)
    {
      String[] entityIdArray = (String[])entityIds.toArray(new String[entityIds.size()]);
      entityVOs = MergeUtil.getEntityVOs(entityIdArray, dataSource);
    }

    // only the primary address is in the ContactVO so we have to get them all.
    int mergeType = searchResult.getMergeType();
    ArrayList addressVOs = MergeUtil.getAddressVOs(mergeIdArray, mergeType, dataSource);
    // The customfields are in the ContactVO too.
    // TODO merge custom fields

    // Populate the form with the information.
    DynaActionForm mergeIndividualDetails = (DynaActionForm)form;
    ArrayList nameCollection = new ArrayList();
    ArrayList idCollection = new ArrayList();
    ArrayList id2Collection = new ArrayList();
    ArrayList entityNameCollection = new ArrayList();
    ArrayList sourceCollection = new ArrayList();
    ArrayList methodOfContactCollection = new ArrayList();
    ArrayList addressCollection = new ArrayList();
    ArrayList marketingListCollection = new ArrayList();

	ArrayList rawMarketingList = new ArrayList();
	ContactFacadeHome contactFacadeHome = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
	try
	{
	  ContactFacade remoteContactFacade = contactFacadeHome.create();
	  remoteContactFacade.setDataSource(dataSource);
	  rawMarketingList = (ArrayList)remoteContactFacade.getAllMarketingList();
	} catch (Exception e) {
	  logger.error("[Exception] IndividualMergeDetail.Execute Handler ", e);
	  throw new ServletException(e);
	}
	for(int i = 0; i < rawMarketingList.size(); i++)
	{
	  HashMap currentList = (HashMap)rawMarketingList.get(i);
	  marketingListCollection.add(new LabelValueBean((String)currentList.get("title"), ((Number)currentList.get("listid")).toString()));
	}

    for (int i = 0; i < individualVOs.size(); i++)
    {
      // build up the collections with the unique values from each VO.
      IndividualVO individual = (IndividualVO)individualVOs.get(i);
      String middleName = individual.getMiddleName();
      if(middleName != null && !middleName.equals("")){
      	middleName += middleName + ". ";
      }
      else{
      	middleName = "";
      }
      String individualName = individual.getFirstName() + " " + middleName + individual.getLastName();
      LabelValueBean currentName = new LabelValueBean(individualName, String.valueOf(i));
      boolean flag = false;
      for (int j = 0; !flag && (j < nameCollection.size()); j++)
      {
        if(((LabelValueBean)nameCollection.get(j)).getLabel().equals(currentName.getLabel()))
        {
          flag = true;
        }
      }
      if (!flag)
      {
        nameCollection.add(currentName);
      }

      LabelValueBean currentId = new LabelValueBean(String.valueOf(individual.getContactID()), String.valueOf(individual.getContactID()));
      idCollection.add(currentId);

      String currentId2String = individual.getExternalID();
      LabelValueBean currentId2 = new LabelValueBean(currentId2String, String.valueOf(i));
      boolean flag2 = false;
      for (int j = 0; !flag2 && (j < id2Collection.size()); j++)
      {
        String id2CollectionLabel = ((LabelValueBean)id2Collection.get(j)).getLabel();
        if((currentId2String == null) || // if string is null for some reason
           (currentId2String.length() == 0) || // if the length == 0
           (id2CollectionLabel.equals(currentId2String))) // if it is equal to an existing one
        {
          flag2 = true; // Don't add it.
        } // end if
      } // end for (int j = 0; j < id2Collection.size(); j++)
      if (!flag2)
      {
        id2Collection.add(currentId2);
      }

      LabelValueBean currentSource = new LabelValueBean(individual.getSourceName(), String.valueOf(individual.getSource()));
      if(!sourceCollection.contains(currentSource) &&
          !(currentSource.getValue().equals("") || currentSource.getLabel().equals("")))
      {
        sourceCollection.add(currentSource);
      }
    } // end for(int i = 0; i < entityVOs.size(); i++)

    // populate bean from AddressVO
    for (int i=0; i < addressVOs.size(); i++)
    {
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
    for (int i = 0; i < methodOfContactVOs.size(); i++)
    {
      MethodOfContactVO moc = (MethodOfContactVO)methodOfContactVOs.get(i);
      StringBuffer mocLabel = new StringBuffer();
      mocLabel.append(moc.getMocTypeName());
      mocLabel.append(":&nbsp;");
      mocLabel.append(moc.getContent());
      LabelValueBean currentMOC = new LabelValueBean(mocLabel.toString(), String.valueOf(moc.getMocID()));
      methodOfContactCollection.add(currentMOC);
    } // end for (int i = 0; i < methodOfContactVOs.size(); i++)

    for (int i = 0; i < entityVOs.size(); i++)
    {
      // Add the Entity Id onto the end of the Label so users can have that
      // information available to guide their choices.
      EntityVO current = (EntityVO)entityVOs.get(i);
      String entityName = current.getName() + " ID: " +current.getContactID();
      LabelValueBean entityLabel = new LabelValueBean(entityName, String.valueOf(current.getContactID()));
      entityNameCollection.add(entityLabel);
    }

    mergeIndividualDetails.set("nameCollection", nameCollection);
    mergeIndividualDetails.set("idCollection", idCollection);
    mergeIndividualDetails.set("id2Collection", id2Collection);
    mergeIndividualDetails.set("entityNameCollection", entityNameCollection);
    mergeIndividualDetails.set("sourceCollection", sourceCollection);
    mergeIndividualDetails.set("methodOfContactCollection", methodOfContactCollection);
    mergeIndividualDetails.set("addressCollection", addressCollection);
    mergeIndividualDetails.set("marketingListCollection", marketingListCollection);
    // Stick the VO collections on here, it will make life easier when building
    // the final merge VO.
    // Yes it is wasteful of resources, since this information is also in the above
    // collections, but this is more detail, and that is scrubbed for display
    // It will make the actual merge code much simpler and easier to understand.
    mergeIndividualDetails.set("individualVOs", individualVOs);
    mergeIndividualDetails.set("addressVOs", addressVOs);
    mergeIndividualDetails.set("methodOfContactVOs", methodOfContactVOs);
    mergeIndividualDetails.set("entityVOs", entityVOs);

    return mapping.findForward(".view.administration.individual_merge_detail");
  }
}
