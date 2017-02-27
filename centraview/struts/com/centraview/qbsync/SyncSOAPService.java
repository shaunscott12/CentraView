/*
 * $RCSfile: SyncSOAPService.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:34 $ - $Author: mking_cv $
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

package com.centraview.qbsync;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServlet;

import org.apache.axis.MessageContext;
import org.apache.axis.session.SimpleSession;
import org.apache.axis.transport.http.HTTPConstants;

import com.centraview.account.expense.ExpenseVO;
import com.centraview.account.helper.GLAccountVO;
import com.centraview.account.helper.PaymentMethodVO;
import com.centraview.account.invoice.InvoiceVO;
import com.centraview.account.item.ItemVO;
import com.centraview.account.payment.PaymentVO;
import com.centraview.account.purchaseorder.PurchaseOrderVO;
import com.centraview.common.CVUtility;
import com.centraview.contact.entity.EntityVO;
import com.centraview.contact.individual.IndividualVO;
import com.centraview.login.Login;
import com.centraview.login.LoginHome;
import com.centraview.settings.Settings;

/**
 * <p>web-service class </p>
 * @author Valery Kasinski
 * @version 1.0
 */

public class SyncSOAPService
{
  private int AUTHENTICATION_SUCCESS_STATUS = 0;
  private int AUTHENTICATION_FAILED_STATUS = 1;
  private SimpleSession session = new SimpleSession();
  // wow, thats a mouthful.

  
  /**
   *  this method save path to the config files in session
   */
  private void getPATH()
  {
    String PATH;
    MessageContext mc = MessageContext.getCurrentContext();
    PATH = (String)((HttpServlet)mc.getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext().getRealPath("/") + "WEB-INF/";
    session.set("PATH", PATH);
  }

  /**
   *
   * @param moduleName String
   * @return boolean
   */
  public boolean isItemModule(String moduleName)
  {
    return (moduleName.equals("ItemInventory")
            || moduleName.equals("ItemNonInventory")
            || moduleName.equals("ItemService"));
  }
  /**
   *
   * @param moduleName String
   * @return boolean
   */
  public boolean isEntityModule(String moduleName)
  {
    return (moduleName.equals("Customer")
            || moduleName.equals("Vendor"));
  }


  /**
   * This method is used for authenticate user from QB
   * @param userName String
   * @param passwd String
   * @return String
   */
  public int authenticateUser(String userName, String passwd)
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(((HttpServlet)MessageContext.getCurrentContext().getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext())).getDataSource();
    int authenticationString = AUTHENTICATION_FAILED_STATUS;
    HashMap hm = null;
    getPATH();
    try {
      InitialContext ic = CVUtility.getInitialContext();
      LoginHome lh = (LoginHome) ic.lookup("Login");
      Login remote = (Login) lh.create();
      remote.setDataSource(dataSource);
      hm = (HashMap) remote.authenticateUser(userName, passwd);
      //checking hashmap
      if (hm != null && hm.get("userid")!= null && hm.get("individualid")!= null && hm.get("lastName")!= null && hm.get("firstName")!= null ) {
        session.set("authenticateUserHashMap", hm);
        authenticationString = AUTHENTICATION_SUCCESS_STATUS;
        //QBSyncHome qbsh = (QBSyncHome) CVUtility.getHomeObject("com.centraview.qbsync.QBSyncHome", "QBSync");
        //QBSync remoteQB = (QBSync) qbsh.create();
        //remoteQB.initializeLog4j(logPATH + userName + "\\");
      }
      else
        authenticationString = AUTHENTICATION_FAILED_STATUS;
    }
    catch (Exception e) {
      System.out.println(AUTHENTICATION_FAILED_STATUS);
      e.printStackTrace();
      return authenticationString;
    }
    return authenticationString;
  }

  /**
   * This method is used for checking is hashMap with user details in session
   * @return boolean
   */
  private boolean checkForSession()
  {
    boolean authenticateUserFlag = false;
    HashMap hm = (HashMap) session.get("authenticateUserHashMap");
    if (hm != null)
      authenticateUserFlag = true;

    return authenticateUserFlag;
  }

  /**
   * This method call EJB for synchronization
   * @param individualID int
   * @param LastSyncDate Timestamp
   * @param moduleName String
   * @param VO ArrayList
   * @param isNewFlag String
   * @return ArrayList
   */
  private ArrayList SynchronizeMod(int individualID, Date LastSyncDate, String moduleName, ArrayList VO, String isNewFlag)
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(((HttpServlet)MessageContext.getCurrentContext().getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext())).getDataSource();
    ArrayList arrlfromCV = null;
    try {
      InitialContext it = CVUtility.getInitialContext();
      QBSyncHome qbsh = (QBSyncHome) it.lookup("QBSync");
      QBSync remote = (QBSync) qbsh.create();
      remote.setDataSource(dataSource);
      arrlfromCV = (ArrayList) remote.beginSynchronisation(individualID, LastSyncDate, moduleName, VO, isNewFlag);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return arrlfromCV;
  }

  /**
   * This method call EJB for update ExternalID in CV DB
   * @param hm HashMap
   * @param moduleName String
   * @throws RemoteException
   * @return boolean
   */
  private boolean updateExternalIDs(HashMap hm, String moduleName) throws java.rmi.RemoteException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(((HttpServlet)MessageContext.getCurrentContext().getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext())).getDataSource();
    boolean resultFlag = false;
    try
    {
      InitialContext it = CVUtility.getInitialContext();
      QBSyncHome qbsh = (QBSyncHome) it.lookup("QBSync");
      QBSync remote = (QBSync) qbsh.create();
      remote.setDataSource(dataSource);
      resultFlag = (boolean)remote.updateExternalIDs(hm, moduleName);
    }
    catch (Exception e)
    {
      System.out.println("Exception in web-service.updateExternalIDs: " + e);
    }
    return resultFlag;
  }

  /**
   * This method call EJB for deleting objects
   * @param ModuleName String
   * @param VO ArrayList
   * @param individualID int
   */
  private void deleteObjects(String ModuleName, ArrayList VO, int individualID)
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(((HttpServlet)MessageContext.getCurrentContext().getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext())).getDataSource();
    try
    {
      InitialContext it = CVUtility.getInitialContext();
      QBSyncHome qbsh = (QBSyncHome) it.lookup("QBSync");
      QBSync remote = (QBSync) qbsh.create();
      remote.setDataSource(dataSource);
      remote.deleteObjects(ModuleName, VO, individualID);
    }
    catch (Exception e)
    {
      System.out.println("Exception in deleteObjects:" + e);
      e.printStackTrace();
    }
  }

  /**
   * This method save CV ID in session
   * @param arrl ArrayList
   * @param modulename String
   */
  private void saveCVID (ArrayList arrl, String modulename)
  {
    ArrayList arrlCVID = new ArrayList();
    try{
      if (isEntityModule(modulename)) {
        Iterator it = arrl.iterator();
        EntityVO VO = new EntityVO();
        while (it.hasNext()) {
          VO = (EntityVO) it.next();
          int ID = VO.getContactID();
          arrlCVID.add(new Integer(ID));
        }
      }
      else if (modulename.equals("Individual")) {
        Iterator it = arrl.iterator();
        IndividualVO VO = new IndividualVO();
        while (it.hasNext()) {
          VO = (IndividualVO) it.next();
          int ID = VO.getContactID();
          arrlCVID.add(new Integer(ID));
        }
      }
      else if (modulename.equals("Invoice")) {
        Iterator it = arrl.iterator();
        InvoiceVO VO = new InvoiceVO();
        while (it.hasNext()) {
          VO = (InvoiceVO) it.next();
          int ID = VO.getInvoiceId();
          arrlCVID.add(new Integer(ID));
        }
      }
      else if (modulename.equals("PurchaseOrder")) {
        Iterator it = arrl.iterator();
        PurchaseOrderVO VO = new PurchaseOrderVO();
        while (it.hasNext()) {
          VO = (PurchaseOrderVO) it.next();
          int ID = VO.getPurchaseOrderId();
          arrlCVID.add(new Integer(ID));
        }
      }
      else if (modulename.equals("Expense")) {
        Iterator it = arrl.iterator();
        ExpenseVO VO = new ExpenseVO();
        while (it.hasNext()) {
          VO = (ExpenseVO) it.next();
          int ID = VO.getExpenseID();
          arrlCVID.add(new Integer(ID));
        }
      }
      else if (isItemModule(modulename)) {
        Iterator it = arrl.iterator();
        ItemVO VO = new ItemVO();
        while (it.hasNext()) {
          VO = (ItemVO) it.next();
          int ID = VO.getItemId();
          arrlCVID.add(new Integer(ID));
        }
      }
      else if (modulename.equals("Payment")) {
        Iterator it = arrl.iterator();
        PaymentVO VO = new PaymentVO();
        while (it.hasNext()) {
          VO = (PaymentVO) it.next();
          int ID = VO.getPaymentID();
          arrlCVID.add(new Integer(ID));
        }
      }
      else if (modulename.equals("PaymentMethod")) {
        Iterator it = arrl.iterator();
        PaymentMethodVO VO = new PaymentMethodVO();
        while (it.hasNext()) {
          VO = (PaymentMethodVO) it.next();
          int ID = VO.getMethodID();
          arrlCVID.add(new Integer(ID));
        }
      }
      else if (modulename.equals("GLAccount")) {
        Iterator it = arrl.iterator();
        GLAccountVO VO = new GLAccountVO();
        while (it.hasNext()) {
          VO = (GLAccountVO) it.next();
          int ID = VO.getGlaccountID();
          arrlCVID.add(new Integer(ID));
        }
      }
    }catch(Exception e)
    {
      System.out.println("Exception in SaveCVID: " + e);
      e.printStackTrace();
    }
    session.set("cvID", arrlCVID);
  }

  /**
   * This method is for using by QB agent for synchronization
   * @param qbXML String
   * @param LastSyncDate Date
   * @return String
   */
  public String callSync(String qbXML, Date LastSyncDate)
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(((HttpServlet)MessageContext.getCurrentContext().getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext())).getDataSource();
    System.out.println("BEGIN SYNCHRONIZATION  "  + new Date() + " *****\n");
    String xmlVo = "";
    if (checkForSession() == true) {
      try 
      {
        //EJB call
        InitialContext it = CVUtility.getInitialContext();
        QBSyncHome qbsh = (QBSyncHome) it.lookup("QBSync");
        QBSync remote = (QBSync) qbsh.create();
        remote.setDataSource(dataSource);
        //convertion module calls here
        QBXMLConverter converter = new QBXMLConverter();
        converter.setDataSource(dataSource);
        String PATH = (String)session.get("PATH");
        // Parse the QBXML and get the list of Objects we have to create or modify
        HashMap voMap = (HashMap) converter.getVOListFromQBXML(qbXML, PATH);
        String qbXMLname = (String)voMap.get("qbxmlname");
        // An array List of the VOs we parsed from the QBXML.
        ArrayList voList = (ArrayList)voMap.get("listvo");
        String moduleName = (String)voMap.get("modulename");

        System.out.println("*******************************MODULENAME************************************");
        System.out.println("***************************" + moduleName + "********************************");
        session.set("qbXMLName", qbXMLname);
        ArrayList newVOarrl = new ArrayList();
        ArrayList modVOarrl = new ArrayList();
        //sort objects for new and modify
        // Only Entity, Individual and Item have modifications?
        if (isEntityModule(moduleName) || moduleName.equals("Individual") || isItemModule(moduleName))
        {
          HashMap syncVOs = (HashMap)remote.chooseNewAndModify(voList, moduleName, LastSyncDate);
          newVOarrl = (ArrayList)syncVOs.get("ADD");
          modVOarrl = (ArrayList)syncVOs.get("MODIFY");
        } else {
            newVOarrl = voList;
        }
        //save in session modify VO arraylist, lastSyncDate, moduleName
        if (modVOarrl == null)
        {
          modVOarrl = new ArrayList();
          session.set("modArrl", modVOarrl);
        } else {
          session.set("modArrl", modVOarrl);
        }
        session.set("syncDate", LastSyncDate);
        session.set("moduleName", moduleName);
        //get from session individualID
        HashMap userhm = (HashMap)session.get("authenticateUserHashMap");
        int individualID = Integer.parseInt(userhm.get("individualid").toString());
        //call sync
        ArrayList VO = SynchronizeMod(individualID, LastSyncDate, moduleName, newVOarrl, "ADD");
        saveCVID(VO, moduleName);
        if (VO.size() > 0) 
        {
          //convert VO to qbXML
          xmlVo = (String) converter.getQBXMLFromVOList(VO, qbXMLname, PATH, new HashMap(), "ADD");
        } else {
          xmlVo = "";
        }
      }
      catch (Exception e)
      {
        System.out.println("Exception in callSync " + e);
        e.printStackTrace();
      }
    }
    else
    {
      return "";
    }
    return xmlVo;
  }

  /**
   * This method fill ArrayList with exteranlIDs
   * @param arrl ArrayList
   * @param modulename String
   * @return ArrayList
   */
  private ArrayList getExtID(ArrayList arrl, String modulename)
  {
    ArrayList arrlExtID = new ArrayList();
    try
    {
      if (isEntityModule(modulename))
      {
        Iterator it = arrl.iterator();
        EntityVO VO = new EntityVO();
        while (it.hasNext())
        {
          VO = (EntityVO) it.next();
          String extID = VO.getExternalID();
          arrlExtID.add(extID);
        }
      }
      else if (modulename.equals("Individual"))
      {
        Iterator it = arrl.iterator();
        IndividualVO VO = new IndividualVO();
        while (it.hasNext())
        {
          VO = (IndividualVO) it.next();
          String extID = VO.getExternalID();
          arrlExtID.add(extID);
        }
      }
      else if (modulename.equals("Invoice"))
      {
        Iterator it = arrl.iterator();
        InvoiceVO VO = new InvoiceVO();
        while (it.hasNext())
        {
          VO = (InvoiceVO) it.next();
          String extID = VO.getExternalId();
          arrlExtID.add(extID);
        }
      }
      else if (modulename.equals("PurchaseOrder"))
      {
        Iterator it = arrl.iterator();
        PurchaseOrderVO VO = new PurchaseOrderVO();
        while (it.hasNext())
        {
          VO = (PurchaseOrderVO) it.next();
          String extID = VO.getExternalID();
          arrlExtID.add(extID);
        }
      }
      else if (modulename.equals("Expense"))
      {
        Iterator it = arrl.iterator();
        ExpenseVO VO = new ExpenseVO();
        while (it.hasNext())
        {
          VO = (ExpenseVO) it.next();
          String extID = VO.getExternalID();
          arrlExtID.add(extID);
        }
      }
      else if( isItemModule(modulename))
      {
        Iterator it = arrl.iterator();
        ItemVO VO = new ItemVO();
        while (it.hasNext())
        {
          VO = (ItemVO) it.next();
          String extID = VO.getExternalID();
          arrlExtID.add(extID);
        }
      }
      else if (modulename.equals("Payment"))
      {
        Iterator it = arrl.iterator();
        PaymentVO VO = new PaymentVO();
        while (it.hasNext())
        {
          VO = (PaymentVO) it.next();
          String extID = VO.getExternalID();
          arrlExtID.add(extID);
        }
      }
      else if (modulename.equals("PaymentMethod"))
      {
        Iterator it = arrl.iterator();
        PaymentMethodVO VO = new PaymentMethodVO();
        while (it.hasNext())
        {
          VO = (PaymentMethodVO) it.next();
          String extID = VO.getExternalID();
          arrlExtID.add(extID);
        }
      }
      else if (modulename.equals("GLAccount"))
      {
        Iterator it = arrl.iterator();
        GLAccountVO VO = new GLAccountVO();
        while (it.hasNext())
        {
          VO = (GLAccountVO) it.next();
          String extID = VO.getExternalID();
          arrlExtID.add(extID);
        }
      }
    }
    catch(Exception e)
    {
      System.out.println("Exception in getExtID: " + e);
      e.printStackTrace();
    }
     return arrlExtID;
  }

  /**
   * This method is for using by QB agent for udating ID
   * It provides the QB unique identifier for each object that 
   * it added from CV
   * 
   * @param qbIDXML String
   * @return String
   */
  public boolean callUpdateID(String qbIDXML)
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(((HttpServlet)MessageContext.getCurrentContext().getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext())).getDataSource();
    boolean statusUpdateID = false;
    if (checkForSession() == true)
    {
      try
      {
        if (! (qbIDXML.equals("")))
        {
          QBXMLConverter converter = new QBXMLConverter();
          converter.setDataSource(dataSource);
          String PATH = (String) session.get("PATH");
          ArrayList arrlExternalID = (ArrayList) converter.getVOListfromResponse(qbIDXML, PATH);
          //get from session cvID arrayList and moduleName
          ArrayList arrlCVID = (ArrayList) session.get("cvID");
          String moduleName = (String) session.get("moduleName");
          HashMap hmID = new HashMap();
          if (arrlExternalID==null) {
            System.out.println("com.centraview.qbsync.callUpdateID arrlExternalID IS  NULL;\n");
            arrlExternalID = new ArrayList();
          }
          if (arrlCVID==null) {
            System.out.println("com.centraview.qbsync.callUpdateID arrlCVID IS  NULL;\n");
            arrlCVID = new ArrayList();
          }
          Iterator itExtID = arrlExternalID.iterator();
          Iterator itCVID = arrlCVID.iterator();
          Object obj1=null;
          Object obj2=null;
          while (itExtID.hasNext() && itCVID.hasNext()) {
            obj1=itCVID.next();
            obj2=itExtID.next();
            if (obj1 != null && obj2 != null) {
               hmID.put(obj1.toString(), obj2.toString());
            }
          }
          statusUpdateID = updateExternalIDs(hmID, moduleName);
        }
        else
        {
            return true;
        }
      }
      catch (Exception ex)
      {
        System.out.println("Exception in callUpdateID:" + ex);
        ex.printStackTrace();
      }
    }
    else
    {
        return false;
    }
    return statusUpdateID;
  }

  /**
   * This method return to agent XML with ListID for modify objects
   * @return String
   */
  public String getModifyListID()
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(((HttpServlet)MessageContext.getCurrentContext().getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext())).getDataSource();
    String listIDXML = "";
    try
    {
      String moduleName = (String) session.get("moduleName");
      if (isEntityModule(moduleName) || moduleName.equals("Individual") || isItemModule(moduleName))
      {
      HashMap userhm = (HashMap) session.get("authenticateUserHashMap");
      int individualID = Integer.parseInt(userhm.get("individualid").toString());
      Date syncDate = (Date) session.get("syncDate");
      ArrayList modVOarrl = (ArrayList) session.get("modArrl");
      ArrayList VO = SynchronizeMod(individualID, syncDate, moduleName, modVOarrl, "MODIFY");
      if (VO.size() >0)
      {
        session.set("modifyArrlFromCV", VO);
        ArrayList extIDArrl = (ArrayList) getExtID(VO, moduleName);

        if (extIDArrl.size() > 0)
        {
          QBXMLConverter converter = new QBXMLConverter();
          converter.setDataSource(dataSource);
          listIDXML = converter.createQBXMLForEditSequence(extIDArrl, (String) session.get("qbXMLName"));
        }
        else
          listIDXML = "";
      }
      else
      {
          session.set("modifyArrlFromCV", new ArrayList());
          listIDXML = "";
      }
    }
      else
        listIDXML = "";
    }
    catch (Exception e)
    {
      System.out.println("Exception in getModifyListID " + e);
      e.printStackTrace();
    }
    return listIDXML;
  }

  /**
   * This method returns modifying objects at the CV Side
   * to the Agent.
   * @param qbXML String
   * @return String
   */
  public String getModifyObjects(String qbXML)
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(((HttpServlet)MessageContext.getCurrentContext().getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext())).getDataSource();
    String modXML = "";
    try
    {
      if (!qbXML.equals(""))
      {
        QBXMLConverter converter = new QBXMLConverter();
        converter.setDataSource(dataSource);
        HashMap hm = converter.getEditSequenceList(qbXML,(String) session.get("qbXMLName"));
        ArrayList arrl = (ArrayList) session.get("modifyArrlFromCV");
        if (arrl.size() > 0)
        {
          String PATH = (String) session.get("PATH");
          modXML = converter.getQBXMLFromVOList(arrl,(String) session.get("qbXMLName"),PATH, hm, "MODIFY");
        }
        else
          modXML = "";
      }
      else
      {
        modXML = "";
      }
    }
    catch (Exception e)
    {
      System.out.println("Exception in getModifyObjects " + e);
      e.printStackTrace();
    }
    return modXML;
  }

  /**
   * The QBAgent 
   * @param qbXML String
   */
  public void callDelete(String qbXML)
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(((HttpServlet)MessageContext.getCurrentContext().getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext())).getDataSource();
    if (!qbXML.equals(""))
    {
      try {
        QBXMLConverter converter = new QBXMLConverter();
        converter.setDataSource(dataSource);
        String PATH = (String) session.get("PATH");
        HashMap hm = (HashMap) converter.getVOListFromQBXML(qbXML, PATH);
        ArrayList arrlDeleteVO = (ArrayList) hm.get("listvo");
        String moduleName = (String) session.get("moduleName");
        HashMap userhm = (HashMap) session.get("authenticateUserHashMap");
        int individualID = Integer.parseInt(userhm.get("individualid").toString());
        deleteObjects(moduleName, arrlDeleteVO, individualID);
      }
      catch (Exception e) {
        System.out.println("Exception in callUpdateID:" + e);
        e.printStackTrace();
      }
    }
  }
}
