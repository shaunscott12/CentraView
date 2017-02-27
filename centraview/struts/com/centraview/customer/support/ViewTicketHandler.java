/*
 * $RCSfile: ViewTicketHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:31 $ - $Author: mking_cv $
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

package com.centraview.customer.support;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.account.expense.ExpenseList;
import com.centraview.common.CVUtility;
import com.centraview.common.IntMember;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.StringMember;
import com.centraview.common.TimeSlipList;
import com.centraview.common.UserObject;
import com.centraview.contact.entity.EntityVO;
import com.centraview.contact.helper.AddressVO;
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.file.CvFile;
import com.centraview.file.CvFileHome;
import com.centraview.file.FileList;
import com.centraview.settings.Settings;
import com.centraview.support.common.SupportConstantKeys;
import com.centraview.support.supportfacade.SupportFacade;
import com.centraview.support.supportfacade.SupportFacadeHome;
import com.centraview.support.thread.ThreadList;
import com.centraview.support.ticket.Ticket;
import com.centraview.support.ticket.TicketForm;
import com.centraview.support.ticket.TicketHome;
import com.centraview.support.ticket.TicketVO;

public class ViewTicketHandler extends org.apache.struts.action.Action
{

  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_newticket = ".view.customer.view_ticket";
  private static final String FORWARD_showticketlist = ".view.customer.ticket_list";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
System.out.println("### View Ticket = [" + request.getAttribute("ticketId") + "] [" + request.getParameter("ticketId") + "]");    
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try {
      // get session
      HttpSession session = request.getSession();
      com.centraview.common.UserObject userobjectd = (com.centraview.common.UserObject)session.getAttribute("userobject"); //get the user object

      int individualId = ((UserObject)session.getAttribute("userobject")).getIndividualID();
      int ticketId = 0;

      if (request.getAttribute("ticketId") != null) {
        ticketId = ((Integer) (request.getAttribute("ticketId"))).intValue();
      } else {
        try {
          ticketId = Integer.parseInt(request.getParameter("ticketId"));
        } catch (NumberFormatException e) {
          FORWARD_final = FORWARD_showticketlist;
          return mapping.findForward(FORWARD_final);
        }
      }
      
      TicketVO ticketVO = new TicketVO();
      TicketForm dynaForm = (TicketForm)form;

      TicketHome th = (TicketHome)CVUtility.getHomeObject("com.centraview.support.ticket.TicketHome", "Ticket");
      Ticket t = th.create();
      t.setDataSource(dataSource);

      TicketVO tVO = t.getTicketBasicRelations(individualId, ticketId);

      SupportFacadeHome supFacade = (SupportFacadeHome)CVUtility.getHomeObject("com.centraview.support.supportfacade.SupportFacadeHome", "SupportFacade");
      SupportFacade remote = supFacade.create();
      remote.setDataSource(dataSource);

      CvFileHome fileHome = (CvFileHome)CVUtility.getHomeObject("com.centraview.file.CvFileHome", "CvFile");
      CvFile cvFile = fileHome.create();
      cvFile.setDataSource(dataSource);
      FileList fileList = null;
      
      if (tVO.getId() != 0) {
        fileList = cvFile.getAllTicketFiles(individualId, tVO.getId());
        fileList = setLinksFunction(fileList);
      }

      ThreadList threadList = remote.getThreadList(individualId, tVO.getId());
      threadList = setLinksfunction(threadList);

      ListGenerator lg1 = ListGenerator.getListGenerator(dataSource);
      HashMap hmDispLists = lg1.getDisplayLists();

      if (hmDispLists != null) {
        hmDispLists.put(new Long(threadList.getListID()), threadList);
      }

      Vector timeSlipColumns = new Vector();

      timeSlipColumns.addElement("Description");
      timeSlipColumns.addElement("Duration");
      timeSlipColumns.addElement("CreatedBy");
      timeSlipColumns.addElement("Date");
      timeSlipColumns.addElement("StartTime");
      timeSlipColumns.addElement("EndTime");

      TimeSlipList timesliplist = new TimeSlipList();

      ListGenerator lg = ListGenerator.getListGenerator(dataSource); //get the List Generator object for Listing

      String searchTimeSlipString =
        "ADVANCE:select ts.TimeSlipID, ts.Description, ts.CreatedBy, individual.FirstName,"
          + "individual.LastName, ts.BreakTime, ts.Date, ts.Start, ts.End "
          + "from timeslip  ts left outer join individual on "
          + "ts.CreatedBy = individual.IndividualID where ts.ticketid="
          + ticketId;

      timesliplist = (TimeSlipList)lg.getTimeSlipList(individualId, 1, 200, searchTimeSlipString, "Description");
      timesliplist = setTimeSlipLinks(timesliplist, ticketId);

      Vector expenseColumns = new Vector();

      expenseColumns.addElement("Reference");
      expenseColumns.addElement("Amount");
      expenseColumns.addElement("Created");
      expenseColumns.addElement("Creator");

      ExpenseList expenselist = new ExpenseList();

      String searchExpenseString =
        "ADVANCE:select expense.ExpenseID,expense.Amount,expense.Created,entity.name as Reference ,expense.Status, concat(individual.firstname,' ',individual.lastname) as Creator,individual.IndividualID "
          + "from expense left outer join individual on expense.creator = individual.individualid left outer join entity on expense.entityid = entity.entityid "
          + " where expense.linestatus != 'deleted'  and expense.ticket="
          + ticketId
          + " group by expense.ExpenseID,expense.Amount,expense.Created,Reference ,expense.Status, Creator";

      expenselist = (ExpenseList)lg.getExpenseList(individualId, 1, 200, searchExpenseString, "Reference"); //called when the request for the list is for first time
      expenselist = setExpenseLinks(expenselist, ticketId);

      request.setAttribute("closeDate", tVO.getCloseDate());
      request.setAttribute("openDate", tVO.getCreatedOn());
      request.setAttribute("modifyDate", tVO.getModifiedOn());
      request.setAttribute("threadlist", threadList);
      request.setAttribute("filelist", fileList);
      request.setAttribute("OCStatus", tVO.getOCStatus());
      request.setAttribute("timesliplist", timesliplist);
      request.setAttribute("timeSlipColumns", timeSlipColumns);
      request.setAttribute("expenselist", expenselist);
      request.setAttribute("expenseColumns", expenseColumns);

      dynaForm.setId(new Integer(tVO.getId()).toString());
      dynaForm.setSubject(tVO.getTitle());
      dynaForm.setDetail(tVO.getDetail());
      dynaForm.setStatus(new Integer(tVO.getStatusId()).toString());
      dynaForm.setPriority(new Integer(tVO.getPriorityId()).toString());
      dynaForm.setEntityid(new Integer(tVO.getRefEntityId()).toString());
      dynaForm.setAssignedto(tVO.getAssignedToName());
      dynaForm.setAssignedtoid(new Integer(tVO.getAssignedToId()).toString());
      dynaForm.setContact(tVO.getRefIndividualName());
      
      EntityVO entityVO = tVO.getEntityVO();
      if (entityVO != null) {
        String entityName = entityVO.getName();
        dynaForm.setEntityname(entityName);
        AddressVO primaryAdd = entityVO.getPrimaryAddress();
        
        if (primaryAdd != null) {
          String address = "";
          if (primaryAdd.getStreet1() != null && !primaryAdd.getStreet1().equals("")) {
            address += primaryAdd.getStreet1();
          }
          if (primaryAdd.getStreet2() != null && !primaryAdd.getStreet2().equals("")) {
            address +=", "+ primaryAdd.getStreet2()+"\n";
          } else {
            address += "\n";
          }
          if (primaryAdd.getCity() != null && !primaryAdd.getCity().equals("")) {
            address += primaryAdd.getCity();
          }
          if (primaryAdd.getStateName() != null && !primaryAdd.getStateName().equals("")) {
            address += ", "+ primaryAdd.getStateName();
          }
          if (primaryAdd.getZip() != null && !primaryAdd.getZip().equals("")) {
            address += " "+ primaryAdd.getZip();
          }
          if (primaryAdd.getCountryName() != null && !primaryAdd.getCountryName().equals("")) {
            address += ", "+ primaryAdd.getCountryName();
          }
          dynaForm.setAddress(address);
          if (primaryAdd.getWebsite() != null) {
            dynaForm.setWebsite(primaryAdd.getWebsite());
          }
        }
        
        Collection mocList = entityVO.getMOC();
        Iterator iterator = mocList.iterator();
        while (iterator.hasNext()) {
          MethodOfContactVO moc  = (MethodOfContactVO) iterator.next();
          if (moc.getMocType() == 1 && moc.getIsPrimary().equalsIgnoreCase("YES")) // this is for email
          {
            dynaForm.setEmail(moc.getContent());
          } else if (moc.getMocType() == 4) {
            dynaForm.setPhone(moc.getContent());
          }
        }
      }

      dynaForm.setContactid(new Integer(tVO.getRefIndividualId()).toString());
      dynaForm.setManagername(tVO.getManagerName() );
      dynaForm.setManagerid(new Integer(tVO.getManagerId()).toString());

System.out.println("dynaForm = [" + dynaForm + "]");

      FORWARD_final = FORWARD_newticket;
      request.setAttribute(SupportConstantKeys.TYPEOFSUBMODULE, "Ticket");
    } catch (Exception e) {
      System.out.println("[Exception][ViewTicketHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }

  public ThreadList setLinksfunction(ThreadList DL)
  {

    Set listkey = DL.keySet();
    Iterator it = listkey.iterator();
    while (it.hasNext()) {
      try {
        String str = (String)it.next();
        StringMember sm = null;
        ListElement ele = (ListElement)DL.get(str);
        sm = (StringMember)ele.get("Title");
        sm.setRequestURL("/customer/view_thread.do?rowId=" + ele.getElementID() + "&listId=" + DL.getListID()); //sets the URL for this member
        sm = (StringMember)ele.get("CreatedBy");

        IntMember im = (IntMember)ele.get("IndividualID");
        Integer value = (Integer)im.getMemberValue();
        int IndividualID = value.intValue();
        sm.setLinkEnabled(false);
      } catch (Exception e) {
        System.out.println("[Exception][ViewTicketHandler.setLinksfunction] Exception Thrown: " + e);
        e.printStackTrace();
      }
    }
    return DL;
  }

  public TimeSlipList setTimeSlipLinks(TimeSlipList DL, int ticketId)
  {
    Set listkey = DL.keySet();
    Iterator it = listkey.iterator();
    while (it.hasNext()) {
      String str = (String)it.next();
      StringMember sm = null;
      ListElement ele = (ListElement)DL.get(str);

      IntMember im = (IntMember)ele.get("ID");
      im.setRequestURL("openWindow('/centraview/customer/ViewTimeSlipDetail.do?rowId=" + ((Integer)im.getMemberValue()).intValue()
          + "&listId="
          + DL.getListID()
          + "'"
          + ",'', 740,  335,'scrollbars=yes')");

      sm = (StringMember)ele.get("CreatedBy");
      if (sm != null) {
        im = (IntMember)ele.get("Creator");
        sm.setRequestURL("/customer/ViewIndividualDetail.do?rowId=" + ((Integer)im.getMemberValue()).intValue() + "&listId=" + DL.getListID());
      }
      sm = (StringMember)ele.get("Description");
      sm.setLinkEnabled(true);
      if (sm != null) {
        im = (IntMember)ele.get("ID");
        sm.setRequestURL(
          "openPopup('/centraview/customer/ViewTimeslip.do?rowId=" + ((Integer)im.getMemberValue()).intValue() + "&listId=" + DL.getListID() + "&ticketId=" + ticketId + "')");
      }
    }
    return DL;
  }

  public FileList setLinksFunction(FileList list)
  {
    Set listkey = list.keySet();
    Iterator iter = listkey.iterator();
    while (iter.hasNext()) {
      String key = (String)iter.next();
      ListElement row = (ListElement)list.get(key);
      StringMember created = (StringMember)row.get("CreatedBy");
      created.setLinkEnabled(false);
    }
    return(list);
  }

  public ExpenseList setExpenseLinks(ExpenseList DL, int ticketId)
  {
    String url = null;
    Set listkey = DL.keySet();
    Iterator it = listkey.iterator();
    while (it.hasNext()) {
      try {
        String str = (String)it.next();
        StringMember sm = null;
        ListElement ele = (ListElement)DL.get(str);
        IntMember intExpenseID = (IntMember)ele.get("ExpenseID");
        int expID = 0;

        if (intExpenseID != null) {
          expID = ((Integer)intExpenseID.getMemberValue()).intValue();
        }

        Integer intIndividualID = (Integer) ((IntMember)ele.get("IndividualID")).getMemberValue();
        sm = (StringMember)ele.get("Creator");

        if (intIndividualID != null) {
          url = "/customer/ViewIndividualDetail.do?rowId=" + intIndividualID.intValue();
          sm.setRequestURL(url);
        }

        sm = (StringMember)ele.get("Reference");

        if (intIndividualID != null) {
          url = "showExpense(" + expID + ")";
          sm.setRequestURL(url);
        }
        sm.setLinkEnabled(true);
      } catch (Exception e) {
        System.out.println("[Exception][ViewTicketHandler.setExpenseLinks] Exception Thrown: " + e);
      }
    }
    return DL;
  }
}
