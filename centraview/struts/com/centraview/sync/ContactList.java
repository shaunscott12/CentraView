/*
 * $RCSfile: ContactList.java,v $    $Revision: 1.2 $  $Date: 2005/06/02 15:14:35 $ - $Author: mking_cv $
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
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.common.CVUtility;
import com.centraview.common.ListGenerator;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.syncfacade.SyncFacadeHome;

/**
 * Handles the request for "/SyncContactList.do", which sends
 * a list of individuals to the CompanionLink Sync client. This
 * class gets the data from SyncFacadeEJB.getIndividualList()
 * and prints it directly to the http response stream.
 */
public class ContactList extends Action 
{


  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    response.setContentType("text/plain");
    PrintWriter writer = response.getWriter();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualIDsession = userObject.getIndividualID();    // logged in user

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

      SyncFacadeHome syncHome = (SyncFacadeHome)CVUtility.getHomeObject("com.centraview.syncfacade.SyncFacadeHome", "SyncFacade");
      com.centraview.syncfacade.SyncFacade sfremote = (com.centraview.syncfacade.SyncFacade)syncHome.create();
      sfremote.setDataSource(dataSource);

      // get the list of individuals from the ejb layer
      Collection individualList = sfremote.getIndividualList(individualIDsession);

      ModuleFieldRightMatrix rightsMatrix = userObject.getUserPref().getModuleAuthorizationMatrix();
      HashMap indivFieldRights = rightsMatrix.getFieldRights("Individual");
      HashMap entityFieldRights = rightsMatrix.getFieldRights("Entity");

      // individualList shouldn't be null. If it is, there's a problem.
      // SyncFacadeEJB should always return a valid collection (an
      // empty Collection is still a valid Collection, but null is not).
      if (individualList != null)
      {
        // print the header row. Do it even if there are no individuals in the list
        writer.print("ContactID\tlastModified\tcompanyName\tfirstName\tMI\tlastName\ttitle\t");
        writer.print("street1\tstreet2\tcity\tstate\tzipCode\tcountry\t");
        writer.print("email\tworkPhone\thomePhone\tfaxPhone\totherPhone\tmainPhone\t");
        writer.print("pagerPhone\tmobilePhone\tworkPhoneExt\thomePhoneExt\tfaxPhoneExt\t");
        writer.print("otherPhoneExt\tmainPhoneExt\tpagerPhoneExt\tmobilePhoneExt\tnotes\n"); 

        // we successfully got the IndividualList, let's process it :-)
        if (individualList.size() > 0)
        {
          Iterator it = individualList.iterator();
          
          while (it.hasNext())
          {
            HashMap individualDetails = (HashMap)it.next();

            int individualID = ((Number)individualDetails.get("contactID")).intValue();

            // This hashmap will temporarily hold all field values until
            // we are ready to print them out. It will be helpful to us
            // when we need to encode special characters.
            HashMap record = new HashMap();

            // IndividualID
            record.put("ContactID", String.valueOf(individualID));

            //lastModifiedDate
            record.put("lastModified", syncUtils.formatDate((Timestamp)individualDetails.get("lastModified")));

            // First check field rights privileges (of certain fields),
            // if user has sufficient privilege, print field, if user
            // does not have sufficient privilege, print a "-".

            // ***IMPORTANT!!*** If the user does not have privilege to
            // a given field, CompanionLink expects a "-". Sending a null
            // or blank string will cause CompanionLink to believe this
            // is the CONTENT of the field, and will update the record
            // with that information. It is extremely important to make
            // sure this is done properly.

            // ***IMPORTANT!!*** It is also extremely important to note
            // that we can never set any field to the java NULL value. This
            // will cause "null" to be printed out to the CompanionLink
            // client, in which case CompanionLink will evaluate the String
            // "null" as the content of the field. Therefore, ALWAYS CHECK
            // FOR NULL VALUES BEFORE PRINTING!!! (or adding to our temp
            // HashMap)...

            // companyName
            if (((Integer)entityFieldRights.get("name")).intValue() < ModuleFieldRightMatrix.NONE_RIGHT)
            {
              String companyName = (String)individualDetails.get("companyName");
              record.put("companyName", (companyName != null) ? companyName : "");
            }else{
              record.put("companyName", "-");
            }
            
            // firstName
            if (((Integer)indivFieldRights.get("firstname")).intValue() < ModuleFieldRightMatrix.NONE_RIGHT)
            {
              String firstName = (String)individualDetails.get("firstName");
              record.put("firstName", (firstName != null) ? firstName : "");
            }else{
              record.put("firstName", "-");
            }
            
            // MI
            if (((Integer)indivFieldRights.get("middlename")).intValue() < ModuleFieldRightMatrix.NONE_RIGHT)
            {
              String middleName = (String)individualDetails.get("middleName");
              record.put("MI", (middleName != null) ? middleName : "");
            }else{
              record.put("MI", "-");
            }

            // lastName
            if (((Integer)indivFieldRights.get("lastname")).intValue() < ModuleFieldRightMatrix.NONE_RIGHT)
            {
              String lastName = (String)individualDetails.get("lastName");
              record.put("lastName", (lastName != null) ? lastName : "");
            }else{
              record.put("lastName", "-");
            }

            // title
            if (((Integer)indivFieldRights.get("title")).intValue() < ModuleFieldRightMatrix.NONE_RIGHT)
            {
              String title = (String)individualDetails.get("title");
              record.put("title", (title != null) ? title : "");
            }else{
              record.put("title", "-");
            }

            // Check field rights for address
            if (((Integer)indivFieldRights.get("address")).intValue() < ModuleFieldRightMatrix.NONE_RIGHT)
            {
              // if user has view rights to address, show address data
              String street1 = (String)individualDetails.get("street1");
              record.put("street1", (street1 != null) ? street1 : "");
              
              String street2 = (String)individualDetails.get("street2");
              record.put("street2", (street2 != null) ? street2 : "");
              
              String city = (String)individualDetails.get("city");
              record.put("city", (city != null) ? city : "");
              
              String state = (String)individualDetails.get("state");
              record.put("state", (state != null) ? state : "");
              
              String zipCode = (String)individualDetails.get("zipCode");
              record.put("zipCode", (zipCode != null) ? zipCode : "");
              
              String country = (String)individualDetails.get("country");
              record.put("country", (country != null) ? country : "");
            }else{
              // user does not have field rights for address, so send dashes to CompanionLink
              record.put("street1", "-");
              record.put("street2", "-");
              record.put("city", "-");
              record.put("state", "-");
              record.put("zipCode", "-");
              record.put("country", "-");
            }

            String emailContent = new String("");
            String workPhone    = new String("");
            String workPhoneExt   = new String("");
            String homePhone    = new String("");
            String homePhoneExt   = new String("");
            String faxPhone     = new String("");
            String faxPhoneExt    = new String("");
            String otherPhone   = new String("");
            String otherPhoneExt  = new String("");
            String mainPhone    = new String("");
            String mainPhoneExt   = new String("");
            String pagerPhone   = new String("");
            String pagerPhoneExt  = new String("");
            String mobilePhone  = new String("");
            String mobilePhoneExt = new String("");
            
            if (((Integer)indivFieldRights.get("contactmethod")).intValue() < ModuleFieldRightMatrix.NONE_RIGHT)
            {
              // If the user has privilege to view contact methods (they are not limited
              // individually; ie: "email" cannot be different than "homePhome"), then
              // get the data from the database and stick it in temp variables
              emailContent = (String)individualDetails.get("email");

              // NOTE that for all phone fields, we need to extract the extension
              // and store it in a separate variable because it's a separate field
              workPhone = (String)individualDetails.get("workPhone");
              if (workPhone != null && workPhone.indexOf("EXT") > 0)
              {
                String temp = workPhone;
                workPhone = temp.substring(0, temp.indexOf("EXT"));
                workPhoneExt = temp.substring(temp.indexOf("EXT") + 3, temp.length());
              }
              
              homePhone = (String)individualDetails.get("homePhone");
              if (homePhone != null && homePhone.indexOf("EXT") > 0)
              {
                String temp = homePhone;
                homePhone = temp.substring(0, temp.indexOf("EXT"));
                homePhoneExt = temp.substring(temp.indexOf("EXT") + 3, temp.length());
              }

              faxPhone = (String)individualDetails.get("faxPhone");
              if (faxPhone != null && faxPhone.indexOf("EXT") > 0)
              {
                String temp = faxPhone;
                faxPhone = temp.substring(0, temp.indexOf("EXT"));
                faxPhoneExt = temp.substring(temp.indexOf("EXT") + 3, temp.length());
              }

              otherPhone = (String)individualDetails.get("otherPhone");
              if (otherPhone != null && otherPhone.indexOf("EXT") > 0)
              {
                String temp = otherPhone;
                otherPhone = temp.substring(0, temp.indexOf("EXT"));
                otherPhoneExt = temp.substring(temp.indexOf("EXT") + 3, temp.length());
              }

              mainPhone = (String)individualDetails.get("mainPhone");
              if (mainPhone != null && mainPhone.indexOf("EXT") > 0)
              {
                String temp = mainPhone;
                mainPhone = temp.substring(0, temp.indexOf("EXT"));
                mainPhoneExt = temp.substring(temp.indexOf("EXT") + 3, temp.length());
              }

              pagerPhone = (String)individualDetails.get("pagerPhone");
              if (pagerPhone != null && pagerPhone.indexOf("EXT") > 0)
              {
                String temp = pagerPhone;
                pagerPhone = temp.substring(0, temp.indexOf("EXT"));
                pagerPhoneExt = temp.substring(temp.indexOf("EXT") + 3, temp.length());
              }

              mobilePhone = (String)individualDetails.get("mobilePhone");
              if (mobilePhone != null && mobilePhone.indexOf("EXT") > 0)
              {
                String temp = mobilePhone;
                mobilePhone = temp.substring(0, temp.indexOf("EXT"));
                mobilePhoneExt = temp.substring(temp.indexOf("EXT") + 3, temp.length());
              }
            }else{
              // If the user does NOT have privilege to view contact methods, then
              // set the content of all contact method variables to "-", which tells
              // CompanionLink that this is a restricted field.
              emailContent   = "-";
              workPhone      = "-";
              workPhoneExt   = "-";
              homePhone      = "-";
              homePhoneExt   = "-";
              faxPhone       = "-";
              faxPhoneExt    = "-";
              otherPhone     = "-";
              otherPhoneExt  = "-";
              mainPhone      = "-";
              mainPhoneExt   = "-";
              pagerPhone     = "-";
              pagerPhoneExt  = "-";
              mobilePhone    = "-";
              mobilePhoneExt = "-";
            }

            record.put("email", (emailContent != null) ? emailContent : "");
            record.put("workPhone", (workPhone != null) ? workPhone : "");
            record.put("workPhoneExt", (workPhoneExt != null) ? workPhoneExt : "");
            record.put("homePhone", (homePhone != null) ? homePhone : "");
            record.put("homePhoneExt", (homePhoneExt != null) ? homePhoneExt : "");
            record.put("faxPhone", (faxPhone != null) ? faxPhone : "");
            record.put("faxPhoneExt", (faxPhoneExt != null) ? faxPhoneExt : "");
            record.put("otherPhone", (otherPhone != null) ? otherPhone : "");
            record.put("otherPhoneExt", (otherPhoneExt != null) ? otherPhoneExt : "");
            record.put("mainPhone", (mainPhone != null) ? mainPhone : "");
            record.put("mainPhoneExt", (mainPhoneExt != null) ? mainPhoneExt : "");
            record.put("pagerPhone", (pagerPhone != null) ? pagerPhone : "");
            record.put("pagerPhoneExt", (pagerPhoneExt != null) ? pagerPhoneExt : "");
            record.put("mobilePhone", (mobilePhone != null) ? mobilePhone : "");
            record.put("mobilePhoneExt", (mobilePhoneExt != null) ? mobilePhoneExt : "");

            // notes
            String notes = (String)individualDetails.get("notes");
            record.put("notes", (notes != null) ? notes : "");

            // now encode all strings in the record properly
            record = syncUtils.encodeRecord(record);

            // time to print out the record and move on
            // DO NOT CHANGE THE ORDER OF THESE FIELDS!
            // NOTE that we did not add the "\t" delimiter
            // to each field, but instead we're adding it here
            // as we print out each field. That is because we
            // encode the "record" HashMap as a whole, and part
            // of that encoding encodes tabs into a non-printable
            // character. If we had added the tab to our fields,
            // then we would lose our field delimiter...
            writer.print(record.get("ContactID") + "\t");
            writer.print(record.get("lastModified") + "\t");
            writer.print(record.get("companyName") + "\t");
            writer.print(record.get("firstName") + "\t");
            writer.print(record.get("MI") + "\t");
            writer.print(record.get("lastName") + "\t");
            writer.print(record.get("title") + "\t");
            writer.print(record.get("street1") + "\t");
            writer.print(record.get("street2") + "\t");
            writer.print(record.get("city") + "\t");
            writer.print(record.get("state") + "\t");
            writer.print(record.get("zipCode") + "\t");
            writer.print(record.get("country") + "\t");
            writer.print(record.get("email") + "\t");
            writer.print(record.get("workPhone") + "\t");
            writer.print(record.get("homePhone") + "\t");
            writer.print(record.get("faxPhone") + "\t");
            writer.print(record.get("otherPhone") + "\t");
            writer.print(record.get("mainPhone") + "\t");
            writer.print(record.get("pagerPhone") + "\t");
            writer.print(record.get("mobilePhone") + "\t");
            writer.print(record.get("workPhoneExt") + "\t");
            writer.print(record.get("homePhoneExt") + "\t");
            writer.print(record.get("faxPhoneExt") + "\t");
            writer.print(record.get("otherPhoneExt") + "\t");
            writer.print(record.get("mainPhoneExt") + "\t");
            writer.print(record.get("pagerPhoneExt") + "\t");
            writer.print(record.get("mobilePhoneExt") + "\t");
            writer.print(record.get("notes") + "\n");  // NOTE THE NEWLINE HERE!!!
          }   // end while (it.hasNext())  (individualList)

          ListGenerator lg = ListGenerator.getListGenerator(dataSource);
          lg.makeListDirty("Individual");
        }
      }else{
        // individual list was null. Something must have gone wrong.
        writer.print("FAIL: An unknown error occurred.");
        return(null);
      }
    }catch(Exception e){
      System.out.println("[Exception][ContactList] Exception thrown in execute(): " + e);
      // TODO: remove stack trace
      e.printStackTrace();
    }

    // we're not forwarding to a jsp, so return null
    return(null);
  }   // end execute()
  
}   // end class definition

