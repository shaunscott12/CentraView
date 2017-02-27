/*
 * $RCSfile: AttendeeList.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 21:57:04 $ - $Author: mcallist $
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
package com.centraview.activity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.naming.CommunicationException;
import javax.naming.NamingException;

import com.centraview.common.DDNameValue;
import com.centraview.common.IndividualList;
import com.centraview.common.IntMember;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.StringMember;

/**
 * class AttendeeList
 */

public class AttendeeList extends HashMap {

  /**
   * this method create an object of this class
   * @return AttendeeList
   */
  public static AttendeeList getAttendeeList(int individualID, String listType, int dbID,
      String dataSource) throws CommunicationException, NamingException
  {
    return new AttendeeList(individualID, listType, dbID, dataSource);
  }

  /**
   * constructor of this class with no arguments creates a vector and add
   * attendee object to this vector and put this vector in the hashmap
   * AttendeeList
   */
  private AttendeeList(int individualID, String listType, int dbID, String dataSource)
      throws CommunicationException, NamingException {
    Vector attendeeList = new Vector();
    ListGenerator lg = ListGenerator.getListGenerator(dataSource);

    if (listType.equals("individual")) {

      IndividualList DL = lg.getIndividualList(individualID, 1, 0, "", "Name", dbID);

      DL.setTotalNoOfRecords(DL.size());
      Set s = DL.keySet();
      Iterator it = s.iterator();
      while (it.hasNext()) {
        String lim = (String)it.next();
        ListElement ile = (ListElement)DL.get(lim);
        StringMember smName = (StringMember)ile.get("Name");
        String name = (String)smName.getMemberValue();
        IntMember smid = (IntMember)ile.get("IndividualID");
        int id = new Integer(smid.getMemberValue().toString()).intValue();
        // attendeeList.addElement(new DDNameValue(id,name));
        attendeeList.addElement(new DDNameValue("" + id + "#" + name, name));
      }
      put("attendee", attendeeList);
    } else if (listType.equals("allindividual")) {
      IndividualList DL = lg.getIndividualList(individualID, 0, 0, "", "Name");
      DL.setTotalNoOfRecords(DL.size());
      Set s = DL.keySet();
      Iterator it = s.iterator();
      while (it.hasNext()) {
        String lim = (String)it.next();
        ListElement ile = (ListElement)DL.get(lim);
        StringMember smName = (StringMember)ile.get("Name");
        String name = (String)smName.getMemberValue();
        IntMember smid = (IntMember)ile.get("IndividualID");
        int id = new Integer(smid.getMemberValue().toString()).intValue();
        // attendeeList.addElement(new DDNameValue(id,name));
        attendeeList.addElement(new DDNameValue("" + id + "#" + name, name));
      }
      put("allattendee", attendeeList);
    }
  }

}
