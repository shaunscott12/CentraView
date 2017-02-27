/*
 * $RCSfile: ContactAdd.java,v $    $Revision: 1.2 $  $Date: 2005/09/01 15:31:03 $ - $Author: mcallist $
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

package com.centraview.sync;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

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

import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.common.ListGenerator;
import com.centraview.common.UserObject;
import com.centraview.common.UserPrefererences;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.contact.entity.EntityVO;
import com.centraview.contact.helper.AddressVO;
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.contact.individual.IndividualVO;
import com.centraview.note.Note;
import com.centraview.note.NoteException;
import com.centraview.note.NoteHome;
import com.centraview.note.NoteVO;
import com.centraview.settings.Settings;
import com.centraview.syncfacade.SyncFacadeHome;

/**
 * Adds a new contact (Individual) record to the database from
 * the CompanionLink Sync API. Also creates optional Entity record,
 * Contact Method records, and primary Address record.
 */
public class ContactAdd extends Action
{
  
  private static Logger logger = Logger.getLogger(ContactAdd.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    final String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    response.setContentType("text/plain");
    PrintWriter writer = response.getWriter();

    StringBuffer output = new StringBuffer("");

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");
    
    // get the user's preference for sync'ing as private or default privileges
    UserPrefererences userPrefs = userObject.getUserPref();
    String prefValue = (String)userPrefs.getSyncAsPrivate();
    boolean syncAsPrivate = (prefValue != null && prefValue.equals("YES")) ? true : false;

    int individualID = userObject.getIndividualID();    // logged in user

    // "sycnContactForm", defined in struts-config-sync.xml
    DynaActionForm contactForm = (DynaActionForm)form;
    SyncUtils syncUtils = new SyncUtils();

    try
    {
      // check to see if CompanionLink Agent has signed in
      if (syncUtils.checkSession(userObject, contactForm) == false)
      {
        writer.print("FAIL: You are not logged in.");
        return(null);
      }
      
      // decode all characters that are encoded by CompanionLink Agent
      contactForm = syncUtils.parseSpecialChars(contactForm);

      String firstName = (String)contactForm.get("firstName");
      String lastName = (String)contactForm.get("lastName");
      // now validate the form data
      if (firstName == null || firstName.equals(""))
      {
        if (lastName == null || lastName.equals(""))
        {
          return(null);
        }
      }

      String primaryContact = (String)contactForm.get("primaryContact");
      if (! primaryContact.equals("Yes") && ! primaryContact.equals("No"))
      {
        return(null);
      }

      // if CompanionLink Agent did not send us an Entity name,
      // then set the entity name equal to the individual's name
      String companyName = (String)contactForm.get("companyName");
      if (companyName == null || companyName.equals(""))
      {
        companyName = firstName + " " + lastName;
      }

      ContactFacadeHome cfh = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome","ContactFacade");
      ContactFacade remote = (ContactFacade)cfh.create();
      remote.setDataSource(dataSource);
      
      SyncFacadeHome syncHome = (SyncFacadeHome)CVUtility.getHomeObject("com.centraview.syncfacade.SyncFacadeHome", "SyncFacade");
      com.centraview.syncfacade.SyncFacade sfremote = (com.centraview.syncfacade.SyncFacade)syncHome.create();
      sfremote.setDataSource(dataSource);

      // this will represent the new Individual we are creating
      IndividualVO individualVO = new IndividualVO();

      individualVO.setFirstName(firstName);
      individualVO.setMiddleName((String)contactForm.get("MI"));
      individualVO.setLastName(lastName);
      individualVO.setTitle((String)contactForm.get("title"));
      individualVO.setIsPrimaryContact(primaryContact);
      individualVO.setContactType(2);   // contactType 2 is "Individual"

      AddressVO primaryAddress = new AddressVO();

      primaryAddress.setIsPrimary("YES");
      primaryAddress.setStreet1((String)contactForm.get("street1"));
      primaryAddress.setStreet2((String)contactForm.get("street2"));
      primaryAddress.setCity((String)contactForm.get("city"));
      primaryAddress.setStateName((String)contactForm.get("state"));
      primaryAddress.setZip((String)contactForm.get("zipCode"));
      primaryAddress.setCountryName((String)contactForm.get("country"));
      
      individualVO.setPrimaryAddress(primaryAddress);

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
      if (workPhone != null && (! workPhone.equals("")))
      {
        // create new MocVO object
        MethodOfContactVO workPhoneMocVO = new MethodOfContactVO();
        
        // set properties
        String workPhoneExt = (String)contactForm.get("workPhoneExt");
        if (workPhoneExt != null && ! workPhoneExt.equals(""))
        {
          workPhone = workPhone + "EXT" + workPhoneExt;
        }
        workPhoneMocVO.setContent(workPhone);
        workPhoneMocVO.setSyncAs("Work");
        workPhoneMocVO.setMocType(Constants.MOC_WORK);   // hardcoded to "Phone" type

        individualVO.setMOC(workPhoneMocVO);
      }

      // set homePhone
      String homePhone = (String)contactForm.get("homePhone");
      if (homePhone != null && (! homePhone.equals("")))
      {
        // create new MocVO object
        MethodOfContactVO homePhoneMocVO = new MethodOfContactVO();
        
        // set properties
        String homePhoneExt = (String)contactForm.get("homePhoneExt");
        if (homePhoneExt != null && ! homePhoneExt.equals(""))
        {
          homePhone = homePhone + "EXT" + homePhoneExt;
        }
        homePhoneMocVO.setContent(homePhone);
        homePhoneMocVO.setSyncAs("Home");
        homePhoneMocVO.setMocType(Constants.MOC_HOME);   // hardcoded to "Phone" type

        individualVO.setMOC(homePhoneMocVO);
      }

      // set faxPhone
      String faxPhone = (String)contactForm.get("faxPhone");
      if (faxPhone != null && (! faxPhone.equals("")))
      {
        // create new MocVO object
        MethodOfContactVO faxPhoneMocVO = new MethodOfContactVO();
        
        // set properties
        String faxPhoneExt = (String)contactForm.get("faxPhoneExt");
        if (faxPhoneExt != null && ! faxPhoneExt.equals(""))
        {
          faxPhone = faxPhone + "EXT" + faxPhoneExt;
        }
        faxPhoneMocVO.setContent(faxPhone);
        faxPhoneMocVO.setSyncAs("Fax");
        faxPhoneMocVO.setMocType(Constants.MOC_FAX);   // hardcoded to "Fax" type

        individualVO.setMOC(faxPhoneMocVO);
      }

      // set otherPhone
      String otherPhone = (String)contactForm.get("otherPhone");
      if (otherPhone != null && (! otherPhone.equals("")))
      {
        // create new MocVO object
        MethodOfContactVO otherPhoneMocVO = new MethodOfContactVO();
        
        // set properties
        String otherPhoneExt = (String)contactForm.get("otherPhoneExt");
        if (otherPhoneExt != null && ! otherPhoneExt.equals(""))
        {
          otherPhone = otherPhone + "EXT" + otherPhoneExt;
        }
        otherPhoneMocVO.setContent(otherPhone);
        otherPhoneMocVO.setSyncAs("Other");
        otherPhoneMocVO.setMocType(Constants.MOC_OTHER);   // hardcoded to "Phone" type

        individualVO.setMOC(otherPhoneMocVO);
      }

      // set mainPhone
      String mainPhone = (String)contactForm.get("mainPhone");
      if (mainPhone != null && (! mainPhone.equals("")))
      {
        // create new MocVO object
        MethodOfContactVO mainPhoneMocVO = new MethodOfContactVO();
        
        // set properties
        String mainPhoneExt = (String)contactForm.get("mainPhoneExt");
        if (mainPhoneExt != null && ! mainPhoneExt.equals(""))
        {
          mainPhone = mainPhone + "EXT" + mainPhoneExt;
        }
        mainPhoneMocVO.setContent(mainPhone);
        mainPhoneMocVO.setSyncAs("Main");
        mainPhoneMocVO.setMocType(Constants.MOC_MAIN);   // hardcoded to "Phone" type

        individualVO.setMOC(mainPhoneMocVO);
      }

      // set pagerPhone
      String pagerPhone = (String)contactForm.get("pagerPhone");
      if (pagerPhone != null && (! pagerPhone.equals("")))
      {
        // create new MocVO object
        MethodOfContactVO pagerPhoneMocVO = new MethodOfContactVO();
        
        // set properties
        String pagerPhoneExt = (String)contactForm.get("pagerPhoneExt");
        if (pagerPhoneExt != null && ! pagerPhoneExt.equals(""))
        {
          pagerPhone = pagerPhone + "EXT" + pagerPhoneExt;
        }
        pagerPhoneMocVO.setContent(pagerPhone);
        pagerPhoneMocVO.setSyncAs("Pager");
        pagerPhoneMocVO.setMocType(Constants.MOC_PAGER);   // hardcoded to "Phone" type

        individualVO.setMOC(pagerPhoneMocVO);
      }

      // set mobilePhone
      String mobilePhone = (String)contactForm.get("mobilePhone");
      if (mobilePhone != null && (! mobilePhone.equals("")))
      {
        // create new MocVO object
        MethodOfContactVO mobilePhoneMocVO = new MethodOfContactVO();
        
        // set properties
        String mobilePhoneExt = (String)contactForm.get("mobilePhoneExt");
        if (mobilePhoneExt != null && ! mobilePhoneExt.equals(""))
        {
          mobilePhone = mobilePhone + "EXT" + mobilePhoneExt;
        }
        mobilePhoneMocVO.setContent(mobilePhone);
        mobilePhoneMocVO.setSyncAs("Mobile");
        mobilePhoneMocVO.setMocType(Constants.MOC_MOBILE);   // hardcoded to "Mobile" type

        individualVO.setMOC(mobilePhoneMocVO);
      }

      // get notes field
      String notes = (String)contactForm.get("notes");
      boolean addNote = (notes != null && notes.length() > 0) ? true : false;

      // first, we need to get the entityID based on the CompanyName
      // passed into the form. If the entity name doesn't match something
      // in the database, then set a flag to create a new entity.
      int companyID = 0;
      int finalEntityID = 0;
      boolean createNewEntity = false;
      if (companyName != null && (! companyName.equals("")))
      {
        companyID = sfremote.findCompanyNameMatch(companyName, individualID);

        if (companyID == 0)
        {
          createNewEntity = true;
        }else{
          finalEntityID = companyID;
        }
      }   // end  if (companyName != null && (! companyName.equals("")))

      // now that we've got all the Individual's info set
      // up properly in the appropriate VO objects, let's
      // create an Entity if necessary
      if (createNewEntity)
      {
        // we must have set the createNewEntity flag to true
        // above, when we checked for a matching entity name
        // in the database, and didn't find one.
        EntityVO newEntity = new EntityVO();
        newEntity.setContactType(1);    // contact type 1 = Entity
        newEntity.setName(companyName);

        // set the new Entity's primary Address equal to the
        // same data for the new Individual
        newEntity.setPrimaryAddress(primaryAddress);
        
        Vector indivMOCs = individualVO.getMOC();
        Iterator iter = indivMOCs.iterator();
        while (iter.hasNext())
        {
          // set the new Entity's methods of contacts equal
          // to the same data for the new Individual
          MethodOfContactVO tmpVO = (MethodOfContactVO)iter.next();
          newEntity.setMOC(tmpVO);
        }

        int newEntityID = remote.createEntity(newEntity, individualID);

        // Check to see if the user's preference is to create sync'ed
        // records as private. If so, delete all records from recordauthorisation
        // and publicrecords tables that link to the newly created records.
        if (syncAsPrivate)
        {
          ArrayList entityIDs = new ArrayList();
          try
          {
            entityIDs.add(new Integer(newEntityID));
          }catch(NumberFormatException nfe){
            // don't need to do anything, because we obviously didn't add an entity successfully.
          }
          sfremote.markRecordsPrivate(14, entityIDs);
        }

        finalEntityID = newEntityID;
        individualVO.setIsPrimaryContact("Yes");
      }
      
      // create the individual record via the ContactFacade
      individualVO.setEntityID(finalEntityID);
      int newIndividualID = remote.createIndividual(individualVO, individualID);
      
      if (newIndividualID != 0)
      {
        if (addNote)
        {
          NoteHome noteHome = (NoteHome)CVUtility.getHomeObject("com.centraview.note.NoteHome", "Note");
          Note noteRemote = (Note)noteHome.create();
          noteRemote.setDataSource(dataSource);

          NoteVO noteVO = new NoteVO();

          // the "title" of the note will be the first 22 characters of the content,
          // plus "...", unless the content is less than 22 characters, in which case
          // it will be the same as the content...
          String title = "";
          if (notes.length() > 22)
          {
            title = notes.substring(0, 22) + "...";
          }else{
            title = notes;
          }
          
          noteVO.setTitle(title);
          noteVO.setDetail(notes);
          noteVO.setPriority(NoteVO.NP_MEDIUM);
          noteVO.setCreatedBy(individualID);
          noteVO.setOwner(individualID);
          noteVO.setRelateEntity(finalEntityID);
          noteVO.setRelateIndividual(newIndividualID);
          
          try
          {
            noteRemote.addNote(individualID, noteVO);
          }catch(NoteException ne){
            // TODO: clean up this NoteException handling
            System.out.println("[Exception][ContactAdd] Note Exception caught!: " + ne);
            ne.printStackTrace();
          }
        }   // end if (addNote)

        
        // TODO: check to see if the user's preference is to create sync'ed
        // records as private. If so, delete all records from recordauthorisation
        // and publicrecords tables that link to the newly created records.
        if (syncAsPrivate)
        {
          ArrayList individualIDs = new ArrayList();
          try
          {
            individualIDs.add(new Integer(newIndividualID));
          }catch(NumberFormatException nfe){
            // don't need to do anything, because we obviously didn't add an entity successfully.
          }
          sfremote.markRecordsPrivate(15, individualIDs);
        }

        // we need to make the IndividualList dirty, so that the next time
        // it is viewed, it is refreshed and contains the record we just added
        ListGenerator lg = ListGenerator.getListGenerator(dataSource);
        lg.makeListDirty("Individual");
        
        // now print the new record's ID to the browser
        writer.print(newIndividualID);
      }else{
        writer.print("FAIL: Could not create Contact record.");
        return(null);
      }
    }catch(Exception e){
      System.out.println("[SyncContactAdd] Exception thrown in execute(): " + e);
      //e.printStackTrace();
    }

    // encode the output according to the CompanionLink specs
    String formattedOutput = output.toString();

    // print the output to STDOUT (http response)
    writer.print(formattedOutput);
    
    // we're not forwarding to a JSP, so return null
    return(null);
  }   // end execute()

}   // end class definition

