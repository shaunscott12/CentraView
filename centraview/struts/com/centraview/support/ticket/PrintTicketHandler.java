/*
 * $RCSfile: PrintTicketHandler.java,v $    $Revision: 1.2 $  $Date: 2005/07/12 20:49:27 $ - $Author: mcallist $
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
package com.centraview.support.ticket;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.CustomFieldData;
import com.centraview.common.DDNameValue;
import com.centraview.common.DateMember;
import com.centraview.common.GlobalMasterLists;
import com.centraview.common.ListElement;
import com.centraview.common.StringMember;
import com.centraview.common.UserObject;
import com.centraview.contact.entity.EntityVO;
import com.centraview.contact.helper.AddressVO;
import com.centraview.contact.helper.CustomFieldVO;
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.printtemplate.PrintTemplate;
import com.centraview.printtemplate.PrintTemplateHome;
import com.centraview.printtemplate.PrintTemplateVO;
import com.centraview.settings.Settings;
import com.centraview.support.supportfacade.SupportFacade;
import com.centraview.support.supportfacade.SupportFacadeHome;
import com.centraview.support.thread.ThreadList;

/**
 * @author Naresh Patel <npatel@centraview.com>.
 */
public class PrintTicketHandler extends Action {
  private static Logger logger = Logger.getLogger(PrintTicketHandler.class);

  // Global Forwards
  public static final String GLOBAL_FORWARD_failure = "failure";

  // Local Forwards
  private static final String FORWARD_printticket = "printticket";

  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public PrintTicketHandler() {}

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try {
      HttpSession session = request.getSession();
      UserObject userObject = (UserObject) session.getAttribute("userobject");
      int individualID = userObject.getIndividualID();
      int ticketID = 0;

      if (request.getAttribute("ticketID") != null) {
        ticketID = ((Integer) (request.getAttribute("ticketID"))).intValue();
      } else {
        ticketID = Integer.parseInt(request.getParameter("ticketID"));
      }

      // Populating the TicketVO Objects
      TicketHome th = (TicketHome) CVUtility.getHomeObject("com.centraview.support.ticket.TicketHome", "Ticket");
      Ticket remoteTicket = th.create();
      remoteTicket.setDataSource(dataSource);
      TicketVO tVO = remoteTicket.getTicketBasicRelations(individualID, ticketID);

      // Setting the Custom Fields..
      String customFields = "";
      TreeMap map = new TreeMap();
      map = CustomFieldData.getCustomField("Ticket", ticketID, dataSource);

      Set set = map.keySet();
      Iterator it = set.iterator();
      while (it.hasNext()) {
        String str = (String) it.next();
        CustomFieldVO field = (CustomFieldVO) map.get(str);
        if (field == null) {
          continue;
        }// end if (field == null)

        String fieldType = field.getFieldType();
        String fieldValue = field.getValue();
        String fieldLabel = field.getLabel();

        if (fieldValue == null) {
          fieldValue = "";
        }// end of if (fieldValue == null)
        if (fieldType == null) {
          fieldType = "SCALAR";
        }// end of if (fieldType == null)

        if (fieldType.equals("SCALAR")) {
          customFields += "<TR>\n";
          customFields += "<TD class=\"popupTableText\">" + fieldLabel + "</TD>";
          customFields += "<TD class=\"popupTableText\">" + fieldValue + "</TD>";
          customFields += "</TR>";
        }// end of if (fieldType.equals("SCALAR"))
        else {
          fieldValue = fieldValue.trim();
          if (fieldValue.equals("")) {
            customFields += "<TR>\n";
            customFields += "<TD class=\"popupTableText\">" + fieldLabel + "</TD>";
            customFields += "<TD class=\"popupTableText\">" + fieldValue + "</TD>";
            customFields += "</TR>";
            fieldValue = "0";
          }// end of if(fieldValue.equals(""))

          Vector vec = field.getOptionValues();
          if (vec != null) {
            for (int j = 0; j < vec.size(); j++) {
              DDNameValue ddName = (DDNameValue) vec.get(j);
              int compId = Integer.parseInt(fieldValue);
              if (ddName.getId() == compId) {
                customFields += "<TR>\n";
                customFields += "<TD class=\"popupTableText\">" + fieldLabel + "</TD>";
                customFields += "<TD class=\"popupTableText\">" + ddName.getName() + "</TD>";
                customFields += "</TR>";
              }
            }
          } // end if (vec != null)
        } // end if (fieldType.equals("SCALAR"))
      } // end while (it.hasNext())

      // Setting the Values in the Values Map
      TreeMap Values = new TreeMap();
      Values.put("--TicketNumber--", new Integer(tVO.getId()).toString());

      GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
      Vector statusVec = gml.getAllStatus();
      if (statusVec != null) {
        for (int j = 0; j < statusVec.size(); j++) {
          DDNameValue ddName = (DDNameValue) statusVec.get(j);
          int compId = tVO.getStatusId();
          if (ddName.getId() == compId) {
            Values.put("--Status--", ddName.getName());
          }
        }
      } // end if (vec != null)

      Vector priorityVec = gml.getAllPriorities();
      if (priorityVec != null) {
        for (int j = 0; j < priorityVec.size(); j++) {
          DDNameValue ddName = (DDNameValue) priorityVec.get(j);
          int compId = tVO.getPriorityId();
          if (ddName.getId() == compId) {

            Values.put("--Priority--", ddName.getName());
          }
        }
      } // end if (priorityVec != null)

      Values.put("--Subject--", tVO.getTitle());
      String detail = tVO.getDetail();
      detail = detail.replaceAll("\n", "<br>");
      Values.put("--Details--", detail);
      Values.put("--Contact--", tVO.getRefIndividualName());
      Values.put("--Manager--", tVO.getManagerName());
      Values.put("--AssignedTo--", tVO.getAssignedToName());

      SupportFacadeHome supFacade = (SupportFacadeHome) CVUtility.getHomeObject(
          "com.centraview.support.supportfacade.SupportFacadeHome", "SupportFacade");
      SupportFacade remote = supFacade.create();
      remote.setDataSource(dataSource);

      ThreadList threadList = remote.getThreadList(individualID, tVO.getId());
      String threadValues = this.setLinksfunction(threadList);

      EntityVO entityVO = tVO.getEntityVO();
      if (entityVO != null) {
        String entityName = entityVO.getName();
        Values.put("--Name--", entityName);
        AddressVO primaryAdd = entityVO.getPrimaryAddress();
        String address = "";
        if (primaryAdd != null) {
          if (primaryAdd.getStreet1() != null && !primaryAdd.getStreet1().equals("")) {
            address += primaryAdd.getStreet1();
          }
          if (primaryAdd.getStreet2() != null && !primaryAdd.getStreet2().equals("")) {
            address += ", " + primaryAdd.getStreet2() + "\n";
          } else {
            address += "\n";
          }
          if (primaryAdd.getCity() != null && !primaryAdd.getCity().equals("")) {
            address += primaryAdd.getCity();
          }
          if (primaryAdd.getStateName() != null && !primaryAdd.getStateName().equals("")) {
            address += ", " + primaryAdd.getStateName();
          }
          if (primaryAdd.getZip() != null && !primaryAdd.getZip().equals("")) {
            address += " " + primaryAdd.getZip();
          }
          if (primaryAdd.getCountryName() != null && !primaryAdd.getCountryName().equals("")) {
            address += ", " + primaryAdd.getCountryName();
          }
          address = address.replaceAll("\n", "<br>");
        } // end if (primaryAdd != null)

        Values.put("--Address--", address);
        Collection mocList = entityVO.getMOC();
        Iterator iterator = mocList.iterator();
        String emailAddress = "";
        String phoneNumber = "";
        while (iterator.hasNext()) {
          MethodOfContactVO moc = (MethodOfContactVO) iterator.next();
          if (moc.getMocType() == 1 && moc.getIsPrimary().equalsIgnoreCase("YES")) // this
                                                                                    // is
                                                                                    // for
                                                                                    // email
          {
            emailAddress = moc.getContent();
          } // end of if statement (moc.getMocType() == 1)
          else if (moc.getMocType() == 4) {
            phoneNumber = moc.getContent();
          } // end of else statement (moc.getMocType() == 1)
        }// end of while
        Values.put("--Email--", emailAddress);
        Values.put("--Phone--", phoneNumber);

      }

      PrintTemplateHome PTHome = (PrintTemplateHome) CVUtility.getHomeObject(
          "com.centraview.printtemplate.PrintTemplateHome", "Printtemplate");
      PrintTemplate PTRemote = PTHome.create();
      PTRemote.setDataSource(dataSource);

      PrintTemplateVO ptVO = PTRemote.getPrintTemplate(3);

      String ticketTemplate = ptVO.getPtData();

      Set listkey = Values.keySet();
      Iterator it1 = listkey.iterator();
      while (it1.hasNext()) {
        String str = (String) it1.next();
        String valueString = (String) Values.get(str);
        try {
          ticketTemplate = ticketTemplate.replaceAll(str, valueString);
        } catch (Exception e) {
          logger.error("[execute]: Exception", e);
          StringBuffer tempTicketTemplateErr = new StringBuffer();
          int fieldLengthKey = str.length();
          int fieldIndexKey = ticketTemplate.indexOf(str);
          if (fieldIndexKey != 0) {
            tempTicketTemplateErr.append(ticketTemplate.substring(0, fieldIndexKey));
            tempTicketTemplateErr.append(valueString);
            tempTicketTemplateErr.append(ticketTemplate.substring(fieldIndexKey + fieldLengthKey, ticketTemplate
                .length()));
          }
          ticketTemplate = tempTicketTemplateErr.toString();
        }
      }
      String fieldName = "--Values--";

      StringBuffer tempTicketTemplate = new StringBuffer();
      int fieldLength = fieldName.length();
      int fieldIndex = ticketTemplate.indexOf(fieldName);

      if (fieldIndex != 0) {
        tempTicketTemplate.append(ticketTemplate.substring(0, fieldIndex));
        tempTicketTemplate.append(threadValues);
        tempTicketTemplate.append(ticketTemplate.substring(fieldIndex + fieldLength, ticketTemplate.length()));
      }
      ticketTemplate = tempTicketTemplate.toString();
      tempTicketTemplate = null;
      tempTicketTemplate = new StringBuffer();
      fieldName = "--CustomFields--";
      fieldLength = fieldName.length();
      fieldIndex = ticketTemplate.indexOf(fieldName);

      if (fieldIndex != 0) {
        tempTicketTemplate.append(ticketTemplate.substring(0, fieldIndex));
        tempTicketTemplate.append(customFields);
        tempTicketTemplate.append(ticketTemplate.substring(fieldIndex + fieldLength, ticketTemplate.length()));
      }
      ticketTemplate = tempTicketTemplate.toString();

      request.setAttribute("ticketTemplate", ticketTemplate);

      FORWARD_final = FORWARD_printticket;
    } catch (Exception e) {
      logger.debug("[Exception] PrintTicketHandler.execute: " + e.toString());
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }

  public String setLinksfunction(ThreadList DL) {
    String ThreadListValues = "";
    Set listkey = DL.keySet();
    Iterator it = listkey.iterator();
    int i = 0;
    if (!it.hasNext()) {
      ThreadListValues += "<tr class=\"rowOdd\"  height=\"20\">\n";
      ThreadListValues += "<td class=\"rowOdd\" colspan=\"5\" align=\"center\">No Selected Threads.</td>\n";
      ThreadListValues += "</tr>\n";
    }// end of if (!it.hasNext())
    while (it.hasNext()) {
      i++;
      try {
        String oddOrEven = "";
        if (i % 2 == 0) {
          oddOrEven = "rowEven"; // CSS class should be "even"
        } else {
          oddOrEven = "rowOdd"; // CSS class should be "odd"
        }
        String str = (String) it.next();
        ListElement ele = (ListElement) DL.get(str);

        StringMember Title = (StringMember) ele.get("Title");
        StringMember description = (StringMember) ele.get("Description");
        DateMember date = (DateMember) ele.get("Created");
        StringMember Priority = (StringMember) ele.get("Priority");
        StringMember Created = (StringMember) ele.get("CreatedBy");
        ThreadListValues += "<tr class=\"" + oddOrEven + "\"  height=\"20\">\n";
        ThreadListValues += "<td class=\"" + oddOrEven + "\" >" + Title.getMemberValue().toString() + "</td>\n";

        String dataValue = description.getMemberValue().toString();
        dataValue = dataValue.replaceAll("\n", "<br>");

        ThreadListValues += "<td class=\"" + oddOrEven + "\" >" + dataValue + "</td>\n";
        ThreadListValues += "<td class=\"" + oddOrEven + "\" >" + date.getMemberValue().toString() + "</td>\n";
        ThreadListValues += "<td class=\"" + oddOrEven + "\" >" + Priority.getMemberValue().toString() + "</td>\n";
        ThreadListValues += "<td class=\"" + oddOrEven + "\" >" + Created.getMemberValue().toString() + "</td>\n";
        ThreadListValues += "</tr>\n";

      }// end of try block
      catch (Exception e) {
        logger.debug("[Exception] PrintTicketHandler.setLinksfunction: " + e.toString());
      }// end of catch
    }// end of while (it.hasNext())

    return ThreadListValues;
  }
}
