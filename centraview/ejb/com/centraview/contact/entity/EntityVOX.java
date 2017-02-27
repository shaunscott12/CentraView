/*
 * $RCSfile: EntityVOX.java,v $    $Revision: 1.2 $  $Date: 2005/07/26 20:18:20 $ - $Author: mcallist $
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


package com.centraview.contact.entity;

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
import com.centraview.contact.individual.IndividualVO;

public class EntityVOX extends EntityVO
{
  private static Logger logger = Logger.getLogger(EntityVOX.class);
  /**
   * This method is called to update the VO.  It used to be highly complex and
   * always replace everything, now it has been scaled down and simply loads up the EntityVO
   * with the existingVO from the database, and updates fields that need to and can be updated
   * based on field rights.
   * @param   form  Action Form,HttpServletRequest request
   */
  public EntityVOX(ActionForm form, HttpServletRequest request, String dataSource)
  {
    try {
      DynaActionForm dynaForm = (DynaActionForm)form;
      HttpSession session = request.getSession(true);
      Integer entityIdForm = (dynaForm.get("entityId") == null) ? new Integer(-1) : (Integer)dynaForm.get("entityId");

      UserObject userobject = (UserObject)session.getAttribute("userobject");
      ModuleFieldRightMatrix mfrmx = ((UserObject)session.getAttribute("userobject")).getUserPref().getModuleAuthorizationMatrix();
      HashMap entityRights = mfrmx.getFieldRights("Entity");

      ContactFacadeHome contactFacadeHome = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      ContactFacade contactFacadeRemote = (ContactFacade)contactFacadeHome.create();
      // Here's hoping the EntityVOX is only used from the struts layer.
      contactFacadeRemote.setDataSource(dataSource);
      int entityId = entityIdForm.intValue();
      EntityVO entityVOReference = null;
      if (entityId > 0) {
        entityVOReference = contactFacadeRemote.getEntity(entityIdForm.intValue());
      } else {
        entityVOReference = new EntityVO();
        entityVOReference.setContactID(entityId);
      }
      
      // populate this VO completely from the Database.
      entityVOReference.setContacyType(1);
      this.setVO(entityVOReference);

      if (CVUtility.updateFieldPermitted(entityRights, "name")) {
        this.setName((String)dynaForm.get("entityName"));
      }

      if (CVUtility.updateFieldPermitted(entityRights, "id2")) {
        this.setExternalID((String)dynaForm.get("id2"));
      }

      AddressVO primaryAddress = this.getPrimaryAddress();
      if (CVUtility.updateFieldPermitted(entityRights, "address")) {
        primaryAddress.setStreet1((String)dynaForm.get("street1"));
        primaryAddress.setStreet2((String)dynaForm.get("street2"));
        primaryAddress.setCity((String)dynaForm.get("city"));
        primaryAddress.setStateName((String)dynaForm.get("state"));
        primaryAddress.setZip((String)dynaForm.get("zip"));
        primaryAddress.setCountryName((String)dynaForm.get("country"));
        String webSiteForm = (dynaForm.get("website") == null || dynaForm.get("website") == null) ? "" : (String)dynaForm.get("website");
        String webSite = "";
        if (webSiteForm.matches("^https?://.+")) {
          webSite = webSiteForm;
        } else if (webSiteForm.trim().length() > 0) {
          webSite = "http://" + webSiteForm;
        }
        primaryAddress.setWebsite(webSite);
        Integer addressId = dynaForm.get("addressId") == null ? new Integer(-1) : (Integer)dynaForm.get("addressId");
        primaryAddress.setAddressID(addressId.intValue());
        primaryAddress.setIsPrimary("YES");
        if (!primaryAddress.isEmpty()) {
        this.setPrimaryAddress(primaryAddress);
      }
      }

      if (CVUtility.updateFieldPermitted(entityRights, "source")) {
        Integer sourceIdInteger = (Integer)dynaForm.get("sourceId");
        int sourceId = sourceIdInteger == null ? 0 : sourceIdInteger.intValue();
        this.setSource(sourceId);
        this.setSourceName((String)dynaForm.get("sourceName"));
      }

      if (CVUtility.updateFieldPermitted(entityRights, "acctmanager")) {
        this.setAccManager(((Integer)dynaForm.get("accountManagerId")).intValue());
        this.setAcctMgrName((String)dynaForm.get("accountManagerName"));
      }

      if (CVUtility.updateFieldPermitted(entityRights, "acctteam")) {
        this.setAccTeam(((Integer)dynaForm.get("accountTeamId")).intValue());
        this.setAcctTeamName((String)dynaForm.get("accountTeamName"));
      }

      String pcFName = dynaForm.get("pcFirstName") == null ? "" : (String)dynaForm.get("pcFirstName");
      String pcMName = dynaForm.get("pcMiddleInitial") == null ? "" : (String)dynaForm.get("pcMiddleInitial");
      String pcLName = dynaForm.get("pcLastName") == null ? "" : (String)dynaForm.get("pcLastName");
      String pcTitle = dynaForm.get("pcTitle") == null ? "" : (String)dynaForm.get("pcTitle");

      IndividualVO individualVO = null;
      int individualId = dynaForm.get("pcIndividualId") == null ? 0 : ((Integer)dynaForm.get("pcIndividualId")).intValue();
      if (individualId != 0) {
        individualVO = contactFacadeRemote.getIndividual(individualId);
        // In certain cases (when the individual was imported via marketing list)
        // The individual and the Entity share the same (exact) Address  Therefore
        // If that is the case, I must make sure the IndividualVO address is updated also
        // otherwise the crapiness of the Entity and Individual EJB interaction. will
        // cause a condition, which always reverts the common address to the old one
        // if it were changed.
        // The real solution is probably to delete this code, and make a method on the
        // Individual EJB that simply updates the potentially changed fields on the Individual.
        AddressVO individualPrimaryAddress = individualVO.getPrimaryAddress();
        if (individualPrimaryAddress.getAddressID() == primaryAddress.getAddressID()) {
          individualVO.setPrimaryAddress(primaryAddress);
        }

        // We must clear the mocs on this Individual otherwise we will create new mocs
        // associated with the Individual 
        individualVO.clearMOC();
        if ((pcFName != null && !((pcFName.trim()).equals(""))) || (pcLName != null && !((pcLName.trim()).equals(""))) || (pcMName != null && !((pcMName.trim()).equals(""))) || (pcTitle != null && !((pcTitle.trim()).equals("")))) {
          if (CVUtility.updateFieldPermitted(entityRights, "firstname")) {
            individualVO.setFirstName(pcFName);
          }
          if (CVUtility.updateFieldPermitted(entityRights, "middlename")) {
            individualVO.setMiddleName(pcMName);
          }
          if (CVUtility.updateFieldPermitted(entityRights, "lastname")) {
            individualVO.setLastName(pcLName);
          }
          if (CVUtility.updateFieldPermitted(entityRights, "title")) {
            individualVO.setTitle(pcTitle);
          }
          this.setIndividualID(individualId);
        }
        String first = individualVO.getFirstName();
        String last = individualVO.getLastName();
        if ((first == null || first.equals("")) && (last == null || last.equals(""))) {
          individualVO = null;
          this.setIndividualID(0);
        }
        this.setIndividualVO(individualVO);
      } else {
        // we may be creating a new one.
        if ((pcFName != null && !((pcFName.trim()).equals(""))) || (pcLName != null && !((pcLName.trim()).equals(""))) || (pcMName != null && !((pcMName.trim()).equals(""))) || (pcTitle != null && !((pcTitle.trim()).equals("")))) {
          individualVO = new IndividualVO();
          if (CVUtility.updateFieldPermitted(entityRights, "firstname")) {
            individualVO.setFirstName(pcFName);
          }
          if (CVUtility.updateFieldPermitted(entityRights, "middlename")) {
            individualVO.setMiddleName(pcMName);
          }
          if (CVUtility.updateFieldPermitted(entityRights, "lastname")) {
            individualVO.setLastName(pcLName);
          }
          if (CVUtility.updateFieldPermitted(entityRights, "title")) {
            individualVO.setTitle(pcTitle);
          }
          String first = individualVO.getFirstName();
          String last = individualVO.getLastName();
          if ((first == null || first.equals("")) && (last == null || last.equals(""))) {
            individualVO = null;
          } else {
            this.setIndividualID(0);
            individualVO.setContactID(0);
            individualVO.setIsPrimaryContact("YES");
          }
        }
        this.setIndividualVO(individualVO);
      }

      // Methods Of Contact
      if (CVUtility.updateFieldPermitted(entityRights, "contactmethod")) {
        MethodOfContactVO workingVO = null;
        HashMap currentMocVOs = new HashMap();
        Vector mocVector = this.getMOC();
        for (int i = 0; i < mocVector.size(); i++) {
          MethodOfContactVO current = (MethodOfContactVO) mocVector.get(i);
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
      }   // end if (CVUtility.updateFieldPermitted(entityRights, "contactmethod"))

      // Just get the Custom field VOs right off the form
      if (CVUtility.updateFieldPermitted(entityRights, "customfield"))
      {
        Vector customFieldVector = new Vector();
        CustomFieldVO[] customFields = (CustomFieldVO[])dynaForm.get("customFields");
        for (int i = 0; i < customFields.length; i++)
        {
          CustomFieldVO field = (CustomFieldVO)customFields[i];
          if (field.getFieldID() > 0)
          {
            customFieldVector.add(customFields[i]);
          }
        }
        this.setCustomFieldVOs(customFieldVector);
      } // end if (CVUtility.updateFieldPermitted(individualFieldRights, "customfield"))
    } catch (Exception e) {
      logger.error("[EntityVOX] Exception thrown.", e);
    }
  } // end of construtor  EntityVOX(ActionForm form,HttpServletRequest request)

  /**
   * @return The EntityVO
   */
  public EntityVO getVO()
  {
    return super.getVO();
  }
}
