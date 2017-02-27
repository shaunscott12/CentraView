/*
 * $RCSfile: SaveContactHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/01 15:31:06 $ - $Author: mcallist $
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

package com.centraview.mail.webimport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.common.ListGenerator;
import com.centraview.common.UserObject;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.contact.entity.EntityVO;
import com.centraview.contact.helper.AddressVO;
import com.centraview.contact.helper.ContactVO;
import com.centraview.contact.helper.CustomFieldVO;
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.contact.individual.IndividualVO;
import com.centraview.mail.MailImport;
import com.centraview.mail.MailImportHome;
import com.centraview.settings.Settings;
import com.centraview.sync.SyncUtils;
import com.centraview.syncfacade.SyncFacadeHome;

public class SaveContactHandler extends org.apache.struts.action.Action
{
  private static Logger logger = Logger.getLogger(SaveContactHandler.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, Exception
  {
    final String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user

    ActionErrors allErrors = new ActionErrors();
    String forward = "showContactDetails";

    // "webformContactForm" defined in struts-config-email.xml
    DynaActionForm emailForm = (DynaActionForm)form;

    int newIndividualID = -1;

    try
    {
      MailImportHome importHome = (MailImportHome)CVUtility.getHomeObject("com.centraview.mail.MailImportHome", "MailImport");
      MailImport mailRemote = (MailImport)importHome.create();
      mailRemote.setDataSource(dataSource);

      // first, get the list of valid fields from the database.
      // loop through that list, and get the form parameter value
      // for each valid field. Stick that value in a hashmap for
      // use in the rest of this handler...
      ArrayList validFields = mailRemote.getValidFields("Contact");
      HashMap contactForm = new HashMap();
      if (validFields != null && validFields.size() > 0)
      {
        Iterator iter = validFields.iterator();
        while (iter.hasNext())
        {
          HashMap validFieldMap = (HashMap)iter.next();
          String name = (String)validFieldMap.get("name");
          String fieldName = (String)validFieldMap.get("fieldName");
          String value = request.getParameter(fieldName);
          if (value == null){ value = ""; }
          contactForm.put(fieldName, value);
        }
      }

      // now, formMap is a hashmap representing the entire individual/entity
      // just convert it into an IndividualVO/EntityVO, and save it :-) (as if!)

      String firstName = (String)contactForm.get("firstName");
      String lastName = (String)contactForm.get("lastName");
      if (firstName == null || firstName.equals(""))
      {
        if (lastName == null || lastName.equals(""))
        {
		  // TODO: Add ActionError message...
          forward = "errorOccurred";
        }
      }

      // if there is no entity name, then set the entity name equal to the individual's name
      String companyName = (String)contactForm.get("entityName");
      if (companyName == null || companyName.equals(""))
      {
        companyName = firstName + " " + lastName;
      }

      SyncUtils syncUtils = new SyncUtils();

      // this will represent the new Individual we are creating
      IndividualVO individualVO = new IndividualVO();
      
      // general basic information
      individualVO.setFirstName(firstName);
      individualVO.setMiddleName((String)contactForm.get("middleInitial"));
      individualVO.setLastName(lastName);
      individualVO.setTitle((String)contactForm.get("title"));
      individualVO.setContactType(2);   // contactType 2 is "Individual"
      individualVO.setSourceName((String)contactForm.get("source"));
      individualVO.setExternalID((String)contactForm.get("ID2"));
      
      // Address information
      String street1 = (String)contactForm.get("street1");
      String street2 = (String)contactForm.get("street2");
      String city = (String)contactForm.get("city");
      String state = (String)contactForm.get("state");
      String zipCode = (String)contactForm.get("zip");
      String country = (String)contactForm.get("country");
      AddressVO primaryAddress = new AddressVO();
      // only create an Address VO if ONE or MORE fields are filled in
      if ((street1 != null && street1.length() > 0) || (street2 != null && street2.length() > 0) ||
          (city != null && city.length() > 0) || (state != null && state.length() > 0) ||
          (zipCode != null && zipCode.length() > 0) || (country != null && country.length() > 0))
      {
        primaryAddress.setIsPrimary("YES");
        primaryAddress.setStreet1(street1);
        primaryAddress.setStreet2(street2);
        primaryAddress.setCity(city);
        primaryAddress.setStateName(state);
        primaryAddress.setZip(zipCode);
        primaryAddress.setCountryName(country);
        individualVO.setPrimaryAddress(primaryAddress);
      }

      // save email address
      String email = (String)contactForm.get("email");
      if (email != null && ! email.equals(""))
      {
        MethodOfContactVO emailVO = new MethodOfContactVO();
        emailVO.setContent(email);
        emailVO.setMocType(Constants.MOC_EMAIL);   // hardcoded to "Email" type
        emailVO.setIsPrimary("YES");  // always set as the primary email address
        individualVO.setMOC(emailVO);
      }

      // set workPhone
      String workPhone = (String)contactForm.get("workPhone");
      String workPhoneExt = (String)contactForm.get("workPhoneExt");    // will be needed later for entity, maybe
      if (workPhone != null && ! workPhone.equals(""))
      {
        individualVO.setMOC(syncUtils.createNewPhoneMoc(workPhone, workPhoneExt, "Work",Constants.MOC_WORK));    // (content, ext, syncAs, mocType)
      }

      // set homePhone
      String homePhone = (String)contactForm.get("homePhone");
      String homePhoneExt = (String)contactForm.get("homePhoneExt");
      if (homePhone != null && ! homePhone.equals(""))
      {
        individualVO.setMOC(syncUtils.createNewPhoneMoc(homePhone, homePhoneExt, "Home" , Constants.MOC_HOME));
      }

      // set faxPhone
      String faxPhone = (String)contactForm.get("faxPhone");
      String faxPhoneExt = (String)contactForm.get("faxPhoneExt");
      if (faxPhone != null && (! faxPhone.equals("")))
      {
        individualVO.setMOC(syncUtils.createNewPhoneMoc(faxPhone, faxPhoneExt, "Fax" , Constants.MOC_FAX));
      }

      // set otherPhone
      String otherPhone = (String)contactForm.get("otherPhone");
      String otherPhoneExt = (String)contactForm.get("otherPhoneExt");
      if (otherPhone != null && (! otherPhone.equals("")))
      {
        individualVO.setMOC(syncUtils.createNewPhoneMoc(otherPhone, otherPhoneExt, "Other" , Constants.MOC_OTHER));
      }

      // set mainPhone
      String mainPhone = (String)contactForm.get("mainPhone");
      String mainPhoneExt = (String)contactForm.get("mainPhoneExt");
      if (mainPhone != null && (! mainPhone.equals("")))
      {
        individualVO.setMOC(syncUtils.createNewPhoneMoc(mainPhone, mainPhoneExt, "Main" , Constants.MOC_MAIN));
      }

      // set pagerPhone
      String pagerPhone = (String)contactForm.get("pagerPhone");
      String pagerPhoneExt = (String)contactForm.get("pagerPhoneExt");
      if (pagerPhone != null && (! pagerPhone.equals("")))
      {
        individualVO.setMOC(syncUtils.createNewPhoneMoc(pagerPhone, pagerPhoneExt, "Pager" , Constants.MOC_PAGER));
      }

      // set mobilePhone
      String mobilePhone = (String)contactForm.get("mobilePhone");
      String mobilePhoneExt = (String)contactForm.get("mobilePhoneExt");
      if (mobilePhone != null && (! mobilePhone.equals("")))
      {
        individualVO.setMOC(syncUtils.createNewPhoneMoc(mobilePhone, mobilePhoneExt, "Mobile" , Constants.MOC_MOBILE));
      }

      // might as well create the EJB connections now...
      ContactFacadeHome cfh = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome","ContactFacade");
      ContactFacade cfremote = (ContactFacade)cfh.create();
      cfremote.setDataSource(dataSource);
      
      SyncFacadeHome syncHome = (SyncFacadeHome)CVUtility.getHomeObject("com.centraview.syncfacade.SyncFacadeHome", "SyncFacade");
      com.centraview.syncfacade.SyncFacade sfremote = (com.centraview.syncfacade.SyncFacade)syncHome.create();
      sfremote.setDataSource(dataSource);

      // first, we need to get the entityID based on the CompanyName
      // passed into the form. If the entity name doesn't match something
      // in the database, then set a flag to create a new entity.
      int finalEntityID = 0;
      boolean createNewEntity = false;
      if (companyName != null && (! companyName.equals("")))
      {
        finalEntityID = sfremote.findCompanyNameMatch(companyName, individualID);

        if (finalEntityID == 0)
        {
          createNewEntity = true;
        }
      }   // end if (companyName != null && (! companyName.equals("")))

      // now that we've got all the Individual's info set up properly in
      // the appropriate VO objects, let's create an Entity if necessary
      String primaryContact = "Yes";    // gets set to no if we're updating an existing entity
      if (createNewEntity)
      {
        // in this case, we didn't find a match to our entity name in the
        // database, so we'll create a new entity with any form information
        EntityVO newEntity = new EntityVO();
        newEntity.setContactType(1);    // contact type 1 = Entity
        newEntity.setName(companyName);
        newEntity.setSourceName((String)contactForm.get("entitySource"));
        newEntity.setExternalID((String)contactForm.get("entityID2"));

        // Entity Address information
        String eStreet1 = (String)contactForm.get("entityStreet1");
        String eStreet2 = (String)contactForm.get("entityStreet2");
        String eCity = (String)contactForm.get("entityCity");
        String eState = (String)contactForm.get("entityState");
        String eZipCode = (String)contactForm.get("entityZip");
        String eCountry = (String)contactForm.get("entityCountry");
        // only create an Address VO if ONE or MORE fields are filled in
        if ((eStreet1 != null && eStreet1.length() > 0) || (eStreet2 != null && eStreet2.length() > 0) ||
            (eCity != null && eCity.length() > 0) || (eState != null && eState.length() > 0) ||
            (eZipCode != null && eZipCode.length() > 0) || (eCountry != null && eCountry.length() > 0))
        {
          AddressVO ePrimaryAddress = new AddressVO();
          ePrimaryAddress.setIsPrimary("YES");
          ePrimaryAddress.setStreet1(eStreet1);
          ePrimaryAddress.setStreet2(eStreet2);
          ePrimaryAddress.setCity(eCity);
          ePrimaryAddress.setStateName(eState);
          ePrimaryAddress.setZip(eZipCode);
          ePrimaryAddress.setCountryName(eCountry);
          newEntity.setPrimaryAddress(ePrimaryAddress);
        }else{
          // if no entity address information was given, use individual address
          newEntity.setPrimaryAddress(primaryAddress);
        }

        // save email address
        String eEmail = (String)contactForm.get("entityEmail");
        if (eEmail != null && ! eEmail.equals(""))
        {
          MethodOfContactVO eEmailVO = new MethodOfContactVO();
          eEmailVO.setContent(eEmail);
          eEmailVO.setMocType(1);   // hardcoded to "Email" type
          eEmailVO.setIsPrimary("YES");  // always set as the primary email address
          newEntity.setMOC(eEmailVO);
        }
        
        // set workPhone
        String eWorkPhone = (String)contactForm.get("entityWorkPhone");
        if (eWorkPhone != null && ! eWorkPhone.equals(""))
        {
          String eWorkPhoneExt = (String)contactForm.get("entityWorkPhoneExt");
          newEntity.setMOC(syncUtils.createNewPhoneMoc(eWorkPhone, eWorkPhoneExt, "Work", Constants.MOC_WORK));    // (content, ext, syncAs, mocType)
        }

        // set homePhone
        String eHomePhone = (String)contactForm.get("entityHomePhone");
        if (eHomePhone != null && ! eHomePhone.equals(""))
        {
          String eHomePhoneExt = (String)contactForm.get("entityHomePhoneExt");
          newEntity.setMOC(syncUtils.createNewPhoneMoc(eHomePhone, eHomePhoneExt, "Home", Constants.MOC_HOME));    // (content, ext, syncAs, mocType)
        }

        // set FaxPhone
        String eFaxPhone = (String)contactForm.get("entityFaxPhone");
        if (eFaxPhone != null && ! eFaxPhone.equals(""))
        {
          String eFaxPhoneExt = (String)contactForm.get("entityFaxPhoneExt");
          newEntity.setMOC(syncUtils.createNewPhoneMoc(eFaxPhone, eFaxPhoneExt, "Fax", Constants.MOC_FAX));    // (content, ext, syncAs, mocType)
        }

        // set OtherPhone
        String eOtherPhone = (String)contactForm.get("entityOtherPhone");
        if (eOtherPhone != null && ! eOtherPhone.equals(""))
        {
          String eOtherPhoneExt = (String)contactForm.get("entityOtherPhoneExt");
          newEntity.setMOC(syncUtils.createNewPhoneMoc(eOtherPhone, eOtherPhoneExt, "Other", Constants.MOC_OTHER));    // (content, ext, syncAs, mocType)
        }

        // set MainPhone
        String eMainPhone = (String)contactForm.get("entityMainPhone");
        if (eMainPhone != null && ! eMainPhone.equals(""))
        {
          String eMainPhoneExt = (String)contactForm.get("entityMainPhoneExt");
          newEntity.setMOC(syncUtils.createNewPhoneMoc(eMainPhone, eMainPhoneExt, "Main", Constants.MOC_MAIN));    // (content, ext, syncAs, mocType)
        }

        // set PagerPhone
        String ePagerPhone = (String)contactForm.get("entityPagerPhone");
        if (ePagerPhone != null && ! ePagerPhone.equals(""))
        {
          String ePagerPhoneExt = (String)contactForm.get("entityPagerPhoneExt");
          newEntity.setMOC(syncUtils.createNewPhoneMoc(ePagerPhone, ePagerPhoneExt, "Pager", Constants.MOC_PAGER));    // (content, ext, syncAs, mocType)
        }

        // set MobilePhone
        String eMobilePhone = (String)contactForm.get("entityMobilePhone");
        if (eMobilePhone != null && ! eMobilePhone.equals(""))
        {
          String eMobilePhoneExt = (String)contactForm.get("entityMobilePhoneExt");
          newEntity.setMOC(syncUtils.createNewPhoneMoc(eMobilePhone, eMobilePhoneExt, "Mobile", Constants.MOC_MOBILE));    // (content, ext, syncAs, mocType)
        }
        
        // custom fields
        this.addCustomFields(newEntity, mailRemote.getValidFields("Entity"), contactForm);

        int newEntityID = cfremote.createEntity(newEntity, individualID);
        finalEntityID = newEntityID;
      }else{
        primaryContact = "No";
        // in this block, we're updating an existing entity
        // USE finalEntityID to update the existing entity
		// TODO: write code for updating existing entity        
      }

      // custom fields...
      this.addCustomFields(individualVO, mailRemote.getValidFields("Individual"), contactForm);

      // create the individual record via the ContactFacade
      individualVO.setEntityID(finalEntityID);
      individualVO.setIsPrimaryContact(primaryContact);
      
      newIndividualID = cfremote.createIndividual(individualVO, individualID);
      
      if (newIndividualID > 0)
      {
        // we need to make the IndividualList dirty, so that the next time
        // it is viewed, it is refreshed and contains the record we just added
        ListGenerator lg = ListGenerator.getListGenerator(dataSource);
        lg.makeListDirty("Individual");
        lg.makeListDirty("Entity");
      }else{
		// TODO: create an ActionError message..
        forward = "errorOccurred";
      }
    }catch(Exception e){
		logger.error("[Exception] SaveContactHandler.Execute Handler ", e);
      	e.printStackTrace();
    }
    
   return(new ActionForward(mapping.findForward(forward).getPath() + newIndividualID, true));
  }   // end execute() method

  private void addCustomFields(ContactVO contactVO, ArrayList validCustomFields, HashMap contactForm)
  {
    if (validCustomFields != null && validCustomFields.size() > 0)
    {
      Iterator cfIter = validCustomFields.iterator();
      // loop through valid CF's...
      while (cfIter.hasNext())
      {
        HashMap fieldInfo = (HashMap)cfIter.next();
        Number fieldID = (Number)fieldInfo.get("fieldID");
        String fieldName = (String)fieldInfo.get("fieldName");
        
        // now that we have the field name, we can get the form
        // value for each valid custom field...
        String formValue = (String)contactForm.get(fieldName);
        if (formValue != null && formValue.length() > 0)
        {
          // ... and create a CustomFieldVO if there is a valid form value
          CustomFieldVO customFieldVO = new CustomFieldVO();
          customFieldVO.setFieldID(fieldID.intValue());
          customFieldVO.setValue(formValue);
          contactVO.setCustomField(customFieldVO);
        }
      }   // end while (cfIter.hasNext())
    }   // end if (validCustomFields != null && validCustomFields.size() > 0)
  }   // end addCustomFields(ArrayList,String) method

}   // end class definition

