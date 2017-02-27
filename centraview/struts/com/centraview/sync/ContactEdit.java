/*
 * $RCSfile: ContactEdit.java,v $    $Revision: 1.3 $  $Date: 2005/09/01 15:31:03 $ - $Author: mcallist $
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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.authorization.Authorization;
import com.centraview.administration.authorization.AuthorizationHome;
import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.common.ListGenerator;
import com.centraview.common.UserObject;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.contact.helper.AddressVO;
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.contact.individual.IndividualVO;
import com.centraview.note.Note;
import com.centraview.note.NoteException;
import com.centraview.note.NoteHome;
import com.centraview.note.NoteVO;
import com.centraview.settings.Settings;
import com.centraview.syncfacade.SyncFacadeHome;

public class ContactEdit extends Action
{


  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    response.setContentType("text/plain");
    PrintWriter writer = response.getWriter();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user

    // "sycnContactForm", defined in struts-config-sync.xml
    DynaActionForm contactForm = (DynaActionForm)form;
    SyncUtils syncUtils = new SyncUtils();

    try {
      // check to see if CompanionLink Agent has signed in
      if (syncUtils.checkSession(userObject, contactForm) == false) {
        writer.print("FAIL: You are not logged in.");
        return(null);
      }
      
      // decode all characters that are encoded by CompanionLink Agent
      contactForm = syncUtils.parseSpecialChars(contactForm);

      int contactID = 0;
      String formContactID = (String)contactForm.get("contactID");
      if (formContactID != null && ! formContactID.equals("")) {
        try {
          contactID = Integer.parseInt(formContactID);
        }catch(NumberFormatException nfe){
          writer.print("FAIL: Invalid contact ID specified.");
          return(null);
        }
      }else{
        writer.print("FAIL: Invalid contact ID specified.");
        return(null);
      }
      
      ContactFacadeHome cfh = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome","ContactFacade");
      ContactFacade remote = (ContactFacade)cfh.create();
      remote.setDataSource(dataSource);

      try {
        // check to see if the user has the right to update this record
        AuthorizationHome authHome = (AuthorizationHome)CVUtility.getHomeObject("com.centraview.administration.authorization.AuthorizationHome", "Authorization");
        Authorization authRemote = (Authorization)authHome.create();
        
        if (! authRemote.canPerformRecordOperation(individualID, "Individual", contactID, 20)) {
          return(null);
        }
      }catch(Exception e){
        System.out.println("[Exception][SyncContactEdit] Exception thrown in editContact(2): " + e);
        //e.printStackTrace();
        return(null);
      }

      ModuleFieldRightMatrix rightsMatrix = userObject.getUserPref().getModuleAuthorizationMatrix();
      HashMap indivFieldRights = rightsMatrix.getFieldRights("Individual");
      HashMap entityFieldRights = rightsMatrix.getFieldRights("Entity");

      IndividualVO individualVO = new IndividualVO();
      IndividualVO individualCurrent = remote.getIndividual(contactID);

      individualVO.setContactID(contactID);
      
      String companyName = (String)contactForm.get("companyName");
      if (companyName != null) {
        if (! companyName.equals("")) {
          // first, check to see if a entity with a matching name exists
          // if yes, then associate this invidivual with that entity
          // if no, then create a new entity, and associate this individual with that entity
          SyncFacadeHome syncHome = (SyncFacadeHome)CVUtility.getHomeObject("com.centraview.syncfacade.SyncFacadeHome", "SyncFacade");
          com.centraview.syncfacade.SyncFacade sfremote = (com.centraview.syncfacade.SyncFacade)syncHome.create();
          sfremote.setDataSource(dataSource);
          
          int newEntityID = sfremote.findCompanyNameMatch(companyName, individualID);

          individualVO.setEntityID(newEntityID);
        }else{
          individualVO.setEntityID(individualCurrent.getEntityID());
        }
      }

      String firstName = (String)contactForm.get("firstName");
      if (firstName != null && ! firstName.equals("") && ((Integer)indivFieldRights.get("firstname")).intValue() < ModuleFieldRightMatrix.VIEW_RIGHT) {
        individualVO.setFirstName(firstName);
      }else{
        individualVO.setFirstName(individualCurrent.getFirstName());
      }


      String MI = (String)contactForm.get("MI");
      if (MI != null && ! MI.equals("") && ((Integer)indivFieldRights.get("middlename")).intValue() < ModuleFieldRightMatrix.VIEW_RIGHT) {
        individualVO.setMiddleName(MI);
      }else{
        individualVO.setMiddleName(individualCurrent.getMiddleName());
      }

      String lastName = (String)contactForm.get("lastName");
      if (lastName != null && ! lastName.equals("") && ((Integer)indivFieldRights.get("lastname")).intValue() < ModuleFieldRightMatrix.VIEW_RIGHT) {
        individualVO.setLastName(lastName);
      }else{
        individualVO.setLastName(individualCurrent.getLastName());
      }

      String title = (String)contactForm.get("title");
      if (title != null && ! title.equals("") && ((Integer)indivFieldRights.get("title")).intValue() < ModuleFieldRightMatrix.VIEW_RIGHT) {
        individualVO.setTitle(title);
      }else{
        individualVO.setTitle(individualCurrent.getTitle());
      }

      String primaryContact = (String)contactForm.get("primaryContact");
      if (primaryContact != null) { // && ((Integer)indivFieldRights.get("primarycontact")).intValue() < ModuleFieldRightMatrix.VIEW_RIGHT)
        if (primaryContact.equals("YES") || primaryContact.equals("NO")) {
          individualVO.setIsPrimaryContact(primaryContact);
        }
      }else{
        individualVO.setIsPrimaryContact(individualCurrent.getIsPrimaryContact());
      }
      
      AddressVO primaryAddress = individualCurrent.getPrimaryAddress();
      
      if (primaryAddress == null) {
        primaryAddress = new AddressVO();
      }
      primaryAddress.setIsPrimary("YES");

      if (((Integer)indivFieldRights.get("address")).intValue() < ModuleFieldRightMatrix.VIEW_RIGHT) {
        String street1 = (String)contactForm.get("street1");
        if ((street1 != null) && (! street1.equals(""))) {
          primaryAddress.setStreet1(street1);
        }
        
        String street2 = (String)contactForm.get("street2");
        if ((street2 != null) && (! street2.equals(""))) {
          primaryAddress.setStreet2(street2);
        }
        
        String city = (String)contactForm.get("city");
        if ((city != null) && (! city.equals(""))) {
          primaryAddress.setCity(city);
        }
        
        String state = (String)contactForm.get("state");
        if ((state != null) && (!state.equals(""))) {
          primaryAddress.setStateName(state);
        }

        String zipCode = (String)contactForm.get("zipCode");
        if ((zipCode != null) && (! zipCode.equals(""))) {
          primaryAddress.setZip(zipCode);
        }

        String country = (String)contactForm.get("country");
        if ((country != null) && (!country.equals(""))) {
          primaryAddress.setCountryName(country);
        }
        
        individualVO.setPrimaryAddress(primaryAddress);

      }   // end if (((Integer)indivFieldRights.get("address")).intValue() < ModuleFieldRightMatrix.VIEW_RIGHT)

      if (((Integer)indivFieldRights.get("contactmethod")).intValue() < ModuleFieldRightMatrix.VIEW_RIGHT) {
        // get the current MOC values from the individualCurrent MOC
        // save them in String variables for use below
        String currentWorkPhone   = new String("");
        String currentHomePhone   = new String("");
        String currentFaxPhone    = new String("");
        String currentOtherPhone  = new String("");
        String currentMainPhone   = new String("");
        String currentPagerPhone  = new String("");
        String currentMobilePhone = new String("");
        String currentEmail       = new String("");

        MethodOfContactVO currentWorkVO = null;
        MethodOfContactVO currentHomeVO = null;
        MethodOfContactVO currentFaxVO = null;
        MethodOfContactVO currentOtherVO = null;
        MethodOfContactVO currentMainVO = null;
        MethodOfContactVO currentPagerVO = null;
        MethodOfContactVO currentMobileVO = null;
        MethodOfContactVO currentEmailVO = null;

        Vector currentMOCs = individualCurrent.getMOC();
        if (currentMOCs != null) {
          Enumeration e = currentMOCs.elements();
          while (e.hasMoreElements()) {
            MethodOfContactVO mocVO = (MethodOfContactVO)e.nextElement();
            String syncAs = mocVO.getSyncAs();
            if (syncAs != null && (! syncAs.equals(""))) {
              if (syncAs.equals("Work")) {
                currentWorkPhone = mocVO.getContent();
                currentWorkVO = mocVO;
              }else if (syncAs.equals("Home")){
                currentHomePhone = mocVO.getContent();
                currentHomeVO = mocVO;
              }else if (syncAs.equals("Fax")){
                currentFaxPhone = mocVO.getContent();
                currentFaxVO = mocVO;
              }else if (syncAs.equals("Other")){
                currentOtherPhone = mocVO.getContent();
                currentOtherVO = mocVO;
              }else if (syncAs.equals("Main")){
                currentMainPhone = mocVO.getContent();
                currentMainVO = mocVO;
              }else if (syncAs.equals("Pager")){
                currentPagerPhone = mocVO.getContent();
                currentPagerVO = mocVO;
              }else if (syncAs.equals("Mobile")){
                currentMobilePhone = mocVO.getContent();
                currentMobileVO = mocVO;
              }   // end if (syncAs.equals("Work"))
            }else if (mocVO.getMocType() == 1 && mocVO.getIsPrimary().equals("YES")){
              currentEmail = mocVO.getContent();
              currentEmailVO = mocVO;
            }   // end if (syncAs != null && (! syncAs.equals("")))
          }   // end while (e.hasMoreElements())
        }   // end if (currentMOCS != null)


        Vector newMOCs = new Vector();

        // now, check to see what SyncAs values we were passed,
        // if a given value is not null, then check to see if
        // there is an existing value for that field in the db:
        // (check "currentXxxVO" where "Xxx" equals one of the
        // syncAs types [Work, Home, Fax, etc]). If currentXxxVO
        // is null, create a new MethodOfContactVO object, and set
        // the appropriate values, then add that object to the
        // individualVO using setMOC(). Else, update the "content"
        // field and updated field on the currentXxxVO, and pass
        // that object to individualVO.setMOC();
        String workPhone = (String)contactForm.get("workPhone");
        if (workPhone != null && (! workPhone.equals(""))) {
          if (currentWorkVO == null) {
            MethodOfContactVO workPhoneVO = new MethodOfContactVO();
            workPhoneVO.setContent(workPhone);
            workPhoneVO.setSyncAs("Work");
            workPhoneVO.setMocType(Constants.MOC_WORK);
            newMOCs.add(workPhoneVO);
          }else{
            currentWorkVO.setContent(workPhone);
            currentWorkVO.updated(true);
            currentWorkVO.added(false);
            currentWorkVO.delete(false);
            newMOCs.add(currentWorkVO);
          }
        }

        String homePhone = (String)contactForm.get("homePhone");
        if (homePhone != null && (! homePhone.equals(""))) {
          if (currentHomeVO == null) {
            MethodOfContactVO homePhoneVO = new MethodOfContactVO();
            homePhoneVO.setContent(homePhone);
            homePhoneVO.setSyncAs("Home");
            homePhoneVO.setMocType(Constants.MOC_HOME);
            newMOCs.add(homePhoneVO);
          }else{
            currentHomeVO.setContent(homePhone);
            currentHomeVO.updated(true);
            currentHomeVO.added(false);
            currentHomeVO.delete(false);
            newMOCs.add(currentHomeVO);
          }
        }

        String faxPhone = (String)contactForm.get("faxPhone");
        if (faxPhone != null && (! faxPhone.equals(""))) {
          if (currentFaxVO == null) {
            MethodOfContactVO faxPhoneVO = new MethodOfContactVO();
            faxPhoneVO.setContent(faxPhone);
            faxPhoneVO.setSyncAs("fax");
            faxPhoneVO.setMocType(Constants.MOC_FAX);
            newMOCs.add(faxPhoneVO);
          }else{
            currentFaxVO.setContent(faxPhone);
            currentFaxVO.updated(true);
            currentFaxVO.added(false);
            currentFaxVO.delete(false);
            newMOCs.add(currentFaxVO);
          }
        }
        
        String otherPhone = (String)contactForm.get("otherPhone");
        if (otherPhone != null && (! otherPhone.equals(""))) {
          if (currentOtherVO == null) {
            MethodOfContactVO otherPhoneVO = new MethodOfContactVO();
            otherPhoneVO.setContent(otherPhone);
            otherPhoneVO.setSyncAs("other");
            otherPhoneVO.setMocType(Constants.MOC_OTHER);
            newMOCs.add(otherPhoneVO);
          }else{
            currentOtherVO.setContent(otherPhone);
            currentOtherVO.updated(true);
            currentOtherVO.added(false);
            currentOtherVO.delete(false);
            newMOCs.add(currentOtherVO);
          }
        }

        String mainPhone = (String)contactForm.get("mainPhone");
        if (mainPhone != null && (! mainPhone.equals(""))) {
          if (currentMainVO == null) {
            MethodOfContactVO mainPhoneVO = new MethodOfContactVO();
            mainPhoneVO.setContent(mainPhone);
            mainPhoneVO.setSyncAs("main");
            mainPhoneVO.setMocType(Constants.MOC_MAIN);
            newMOCs.add(mainPhoneVO);
          }else{
            currentMainVO.setContent(mainPhone);
            currentMainVO.updated(true);
            currentMainVO.added(false);
            currentMainVO.delete(false);
            newMOCs.add(currentMainVO);
          }
        }

        String pagerPhone = (String)contactForm.get("pagerPhone");
        if (pagerPhone != null && (! pagerPhone.equals(""))) {
          if (currentPagerVO == null) {
            MethodOfContactVO pagerPhoneVO = new MethodOfContactVO();
            pagerPhoneVO.setContent(pagerPhone);
            pagerPhoneVO.setSyncAs("pager");
            pagerPhoneVO.setMocType(Constants.MOC_PAGER);
            newMOCs.add(pagerPhoneVO);
          }else{
            currentPagerVO.setContent(pagerPhone);
            currentPagerVO.updated(true);
            currentPagerVO.added(false);
            currentPagerVO.delete(false);
            newMOCs.add(currentPagerVO);
          }
        }

        String mobilePhone = (String)contactForm.get("mobilePhone");
        if (mobilePhone != null && (! mobilePhone.equals(""))) {
          if (currentMobileVO == null) {
            MethodOfContactVO mobilePhoneVO = new MethodOfContactVO();
            mobilePhoneVO.setContent(mobilePhone);
            mobilePhoneVO.setSyncAs("mobile");
            mobilePhoneVO.setMocType(Constants.MOC_MOBILE);
            newMOCs.add(mobilePhoneVO);
          }else{
            currentMobileVO.setContent(mobilePhone);
            currentMobileVO.updated(true);
            currentMobileVO.added(false);
            currentMobileVO.delete(false);
            newMOCs.add(currentMobileVO);
          }
        }

        String email = (String)contactForm.get("email");
        if (email != null && (! email.equals(""))) {
          if (currentEmailVO == null) {
            MethodOfContactVO emailVO = new MethodOfContactVO();
            emailVO.setContent(email);
            emailVO.setMocType(Constants.MOC_EMAIL);    // 1 == "email"
            emailVO.setIsPrimary("YES");  // always set as the primary email address
            newMOCs.add(emailVO);
          }else{
            currentEmailVO.setContent(email);
            currentEmailVO.updated(true);
            currentEmailVO.added(false);
            currentEmailVO.delete(false);
            newMOCs.add(currentEmailVO);
          }
        }
        individualVO.setMoc(newMOCs);
      }   // end if (((Integer)indivFieldRights.get("contactmethod")).intValue() < ModuleFieldRightMatrix.VIEW_RIGHT)

      // now check notes
      String noteContent = (String)contactForm.get("notes");
      if (noteContent != null)
      {
        // ok, here's the tricky part. We need to take this content,
        // parse it into individual notes, and figure out which to
        // change/delete/create. This could prove troublesome...
        NoteHome noteHome = (NoteHome)CVUtility.getHomeObject("com.centraview.note.NoteHome", "Note");
        Note noteRemote = (Note)noteHome.create();
        noteRemote.setDataSource(dataSource);

        NoteVO noteVO = new NoteVO();

        // the "title" of the note will be the first 22 characters of the content,
        // plus "...", unless the content is less than 22 characters, in which case
        // it will be the same as the content...
        String noteTitle = "";
        if (noteContent.length() > 22)
        {
          noteTitle = noteContent.substring(0, 22) + "...";
        }else{
          noteTitle = noteContent;
        }
        
        noteVO.setTitle(noteTitle);
        noteVO.setDetail(noteContent);
        noteVO.setPriority(NoteVO.NP_MEDIUM);
        noteVO.setCreatedBy(individualID);
        noteVO.setOwner(individualID);
        noteVO.setRelateEntity(individualVO.getEntityID());
        noteVO.setRelateIndividual(contactID);
        
        try
        {
          noteRemote.addNote(individualID, noteVO);
        }catch(NoteException ne){
          // TODO: clean up this NoteException handling
          System.out.println("[Exception][ContactAdd] Note Exception caught!: " + ne);
          //ne.printStackTrace();
        }
        // ..whew! That was interesting! At least we're finished now ;-)
      }
      
      individualVO.setContactType(2);
      remote.updateIndividual(individualVO, individualID);//Integer.parseInt(contactID));
      writer.print(contactID);
      
      // we need to make the IndividualList dirty, so that the next time
      // it is viewed, it is refreshed and contains the record we just added
      ListGenerator lg = ListGenerator.getListGenerator(dataSource);
      lg.makeListDirty("Individual");

    }catch(Exception e){
      System.out.println("[Exception][ContactEdit] Exception thrown in execute(): " + e);
      //e.printStackTrace();
    }

    // we're not forwarding to a jsp, so return null
    return(null);
  }   // end execute()
  
}   // end class ContactEdit definition

