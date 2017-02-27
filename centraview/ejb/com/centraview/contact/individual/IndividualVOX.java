/*
 * $RCSfile: IndividualVOX.java,v $    $Revision: 1.2 $  $Date: 2005/07/26 20:18:19 $ - $Author: mcallist $
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


package com.centraview.contact.individual;

import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.contact.helper.AddressVO;
import com.centraview.contact.helper.CustomFieldVO;
import com.centraview.contact.helper.MethodOfContactVO;

public class IndividualVOX extends IndividualVO
{
  private static Logger logger = Logger.getLogger(IndividualVOX.class);

  /**
   * In this constructor Populate IndividualVO Object.
   * This one seems only to be called from the SaveUserProfileHandler
   * We will assume that the marketinglist stuff works right.
   * @param   form  ActionForm
   */
  public IndividualVOX(ActionForm form)
  {
    // TODO this constructor probably needs a fixin'
    DynaActionForm dynaForm = (DynaActionForm)form;

    String id = (String)dynaForm.get("IndividualID");

    if (id == null) {
      id = "-1";
    }

    int contactID = Integer.parseInt(id);
    setContactID(contactID);

    setFirstName((String)dynaForm.get("firstname"));
    setLastName((String)dynaForm.get("lastname"));
    setMiddleName((String)dynaForm.get("middlename"));

    int entId;
    String eId = (String)dynaForm.get("entity");
    if (eId != null && eId.length() == 0) {
      entId = 0;
    } else {
      entId = Integer.parseInt(eId);
    }

    setEntityID(entId);

    setTitle((String)dynaForm.get("title"));
    setExternalID((String)dynaForm.get("id2"));
    String sid = (String)dynaForm.get("sourceid");
    setSource(Integer.parseInt(sid));
    setSourceName((String)dynaForm.get("source"));

    //This is for setting Address
    AddressVO primaryAdd = new AddressVO();
    primaryAdd.setStreet1((String)dynaForm.get("street1"));
    primaryAdd.setStreet2((String)dynaForm.get("street2"));
    primaryAdd.setCity((String)dynaForm.get("city"));

    primaryAdd.setStateName((String)dynaForm.get("state"));

    primaryAdd.setZip((String)dynaForm.get("zip"));

    primaryAdd.setCountryName((String)dynaForm.get("country"));

    primaryAdd.setWebsite(((String)dynaForm.get("paWebsite")));

    String addId = (String)dynaForm.get("AddressID");
    int addressID;
    if (addId != null && addId.length() == 0) {
      addressID = 0;
    } else {
      addressID = Integer.parseInt(addId);
    }
    primaryAdd.setAddressID(addressID);

    setPrimaryAddress(primaryAdd);
  }// end of constructor

  /**
   * In this constructor Populate IndividualVO Object.
   * @param   form  ActionForm ,HttpServletRequest
   */
  public IndividualVOX(ActionForm form, HttpServletRequest request, String dataSource)
  {
    try {
      DynaActionForm dynaForm = (DynaActionForm)form;
      HttpSession session = request.getSession(true);
      ModuleFieldRightMatrix mfrmx = ((UserObject)session.getAttribute("userobject")).getUserPref().getModuleAuthorizationMatrix();
      HashMap individualFieldRights = mfrmx.getFieldRights("Individual");

      ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      ContactFacade remote = (ContactFacade)aa.create();
      // here is hoping that the individualVOX is used only from the struts layer.
      remote.setDataSource(dataSource);

      Integer individualIdForm = (null == dynaForm.get("individualId")) ? new Integer(-1) : (Integer)dynaForm.get("individualId");
      int individualId = individualIdForm.intValue();
      IndividualVO individualVOReference = null;
      if (individualId > 0) {
        individualVOReference = remote.getIndividual(individualIdForm.intValue());
      } else {
        individualVOReference = new IndividualVO();
        individualVOReference.setContactID(individualId);
      }
      // seems Add new individual calls this'un too, nice deviation from what they do in Entity.
      // This little check should correct the problems though.
      if (null != individualVOReference) {
        // completely prepopulate from the existing VO
        this.setVO(individualVOReference);
      }
      // now we can change fields based on the form submission and be sure the
      // rest of the stuff maintains original hidden values.

      if (CVUtility.updateFieldPermitted(individualFieldRights, "firstname")) {
        this.setFirstName((String)dynaForm.get("firstName"));
      }
      if (CVUtility.updateFieldPermitted(individualFieldRights, "middlename")) {
        this.setMiddleName((String)dynaForm.get("middleInitial"));
      }
      if (CVUtility.updateFieldPermitted(individualFieldRights, "lastname")) {
        this.setLastName((String)dynaForm.get("lastName"));
      }
      if (CVUtility.updateFieldPermitted(individualFieldRights, "entity")) {
        Integer entityId = (null == dynaForm.get("entityId")) ? new Integer(0) : (Integer)dynaForm.get("entityId");
        this.setEntityID(entityId.intValue());
      }
      if (CVUtility.updateFieldPermitted(individualFieldRights, "title")) {
        this.setTitle((String)dynaForm.get("title"));
      }
      if (CVUtility.updateFieldPermitted(individualFieldRights, "id2")) {
        this.setExternalID((String)dynaForm.get("id2"));
      }
      if (CVUtility.updateFieldPermitted(individualFieldRights, "source")) {
        Integer sourceId = (null == dynaForm.get("sourceId")) ? new Integer(0) : (Integer)dynaForm.get("sourceId");
        this.setSourceName((String)dynaForm.get("sourceName"));
        this.setSource(sourceId.intValue());
      }
      if (CVUtility.updateFieldPermitted(individualFieldRights, "address")) {
        AddressVO primaryAddress = this.getPrimaryAddress();
        primaryAddress.setStreet1((String)dynaForm.get("street1"));
        primaryAddress.setStreet2((String)dynaForm.get("street2"));
        primaryAddress.setCity((String)dynaForm.get("city"));
        primaryAddress.setStateName((String)dynaForm.get("state"));
        primaryAddress.setZip((String)dynaForm.get("zip"));
        primaryAddress.setCountryName((String)dynaForm.get("country"));
        String webSiteForm = (null == dynaForm.get("website")) ? "" : (String)dynaForm.get("website");
        String webSite = "";
        if (webSiteForm.matches("^https?://.+")) {
          webSite = webSiteForm;
        } else if (webSiteForm.trim().length() > 0) {
          webSite = "http://" + webSiteForm;
        }
        primaryAddress.setWebsite(webSite);
        Integer addressId = (dynaForm.get("addressId") == null) ? new Integer(-1) : (Integer)dynaForm.get("addressId");
        primaryAddress.setAddressID(addressId.intValue());
        this.setPrimaryAddress(primaryAddress);
      }
      if (CVUtility.updateFieldPermitted(individualFieldRights, "contactmethod")) {
        MethodOfContactVO workingVO = null;
        HashMap currentMocVOs = new HashMap();
        Vector mocVector = this.getMOC();
        for (int i = 0; i < mocVector.size(); i++) {
          MethodOfContactVO current = (MethodOfContactVO)mocVector.get(i);
          currentMocVOs.put(new Integer(current.getMocID()), current);
        }
        int formMocId1 = (dynaForm.get("mocId1") == null || dynaForm.get("mocId1").equals("")) ? -1 : Integer.parseInt((String)dynaForm.get("mocId1"));
        int formMocId2 = (dynaForm.get("mocId2") == null || dynaForm.get("mocId2").equals("")) ? -1 : Integer.parseInt((String)dynaForm.get("mocId2"));
        int formMocId3 = (dynaForm.get("mocId3") == null || dynaForm.get("mocId3").equals("")) ? -1 : Integer.parseInt((String)dynaForm.get("mocId3"));
        int formMocId4 = (dynaForm.get("emailId") == null || dynaForm.get("emailId").equals("")) ? -1 : Integer.parseInt((String)dynaForm.get("emailId"));

        mocVector.clear();
        // The following will return a properly updated MethodOFContactVO
        // We can then fill up the vector and set it back on the EntityVO
        workingVO = CVUtility.updateMoc(dynaForm, "1", currentMocVOs, formMocId1, false);
        if (workingVO.getContent().trim().length() != 0 || workingVO.isDelete()) {
          mocVector.add(workingVO);
        }
        workingVO = CVUtility.updateMoc(dynaForm, "2", currentMocVOs, formMocId2, false);
        if (workingVO.getContent().trim().length() != 0 || workingVO.isDelete()) {
          mocVector.add(workingVO);
        }
        workingVO = CVUtility.updateMoc(dynaForm, "3", currentMocVOs, formMocId3, false);
        if (workingVO.getContent().trim().length() != 0 || workingVO.isDelete()) {
          mocVector.add(workingVO);
        }
        // moc4 is always email, so <sarcasm>of course it is handled differently.</sarcasm>
        // the special case with the last boolean flag on the updateMoc method
        // was created, and must be invoked here by setting it to true.
        workingVO = CVUtility.updateMoc(dynaForm, "4", currentMocVOs, formMocId4, true);
        if (workingVO.getContent().trim().length() != 0 || workingVO.isDelete()) {
          mocVector.add(workingVO);
        }
        this.setMoc(mocVector);
      } // end if (CVUtility.updateFieldPermitted(individualFieldRights, "contactmethod"))
      // Just get the Custom field VOs right off the form
      if (CVUtility.updateFieldPermitted(individualFieldRights, "customfield")) {
        Vector customFieldVector = new Vector();
        CustomFieldVO[] customFields = (CustomFieldVO[])dynaForm.get("customFields");
        for (int i = 0; i < customFields.length; i++) {
          CustomFieldVO field = (CustomFieldVO)customFields[i];
          if (field.getFieldID() > 0) {
            customFieldVector.add(customFields[i]);
          }
        }
        this.setCustomFieldVOs(customFieldVector);
      } // end if (CVUtility.updateFieldPermitted(individualFieldRights, "customfield"))
    } catch (Exception e) {
      logger.error("[IndividualVOX] Exception thrown.", e);
    }
  }// end of constructor IndividualVOX(ActionForm form ,HttpServletRequest request)

  /**
   *  This method returns IndividualVO Object
   *
   * @return     IndividualVO
   */
  public IndividualVO getVO()
  {
    return super.getVO();
  }
}