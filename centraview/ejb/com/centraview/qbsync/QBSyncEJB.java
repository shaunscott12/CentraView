/*
 * $RCSfile: QBSyncEJB.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:54 $ - $Author: mking_cv $
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


/*
 * This class implements compare function of synchronization module
 * and also works as getter, setter and updater of CV database data
 * @author   Ilia Kirillov
 * @version  1.0
 *
 */

package com.centraview.qbsync;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.centraview.account.accountfacade.AccountFacadeLocal;
import com.centraview.account.accountfacade.AccountFacadeLocalHome;
import com.centraview.account.expense.ExpenseVO;
import com.centraview.account.helper.AccountHelperLocal;
import com.centraview.account.helper.AccountHelperLocalHome;
import com.centraview.account.helper.GLAccountVO;
import com.centraview.account.helper.PaymentMethodVO;
import com.centraview.account.invoice.InvoiceVO;
import com.centraview.account.item.ItemLocal;
import com.centraview.account.item.ItemLocalHome;
import com.centraview.account.item.ItemVO;
import com.centraview.account.order.OrderForm;
import com.centraview.account.payment.PaymentVO;
import com.centraview.account.purchaseorder.PurchaseOrderVO;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.contact.contactfacade.ContactFacadeLocal;
import com.centraview.contact.contactfacade.ContactFacadeLocalHome;
import com.centraview.contact.entity.EntityVO;
import com.centraview.contact.helper.AddressVO;
import com.centraview.contact.helper.ContactHelperLocal;
import com.centraview.contact.helper.ContactHelperLocalHome;
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.contact.individual.IndividualVO;
import com.centraview.hr.hrfacade.HrFacadeLocal;
import com.centraview.hr.hrfacade.HrFacadeLocalHome;
/*
This is the EJB class for compare function of synchronization module
The Logic for methods defined in Remote interface
is defined in this class
*/

public class QBSyncEJB implements SessionBean
{
        protected SessionContext ctx;
        private String dataSource;
        private Logger logger = Logger.getLogger(QBSyncEJB.class);

        public void setSessionContext(SessionContext ctx)
        {
                this.ctx=ctx;
        }

        public void ejbCreate() {}
        public void ejbRemove() {}
        public void ejbActivate() {}
        public void ejbPassivate() {}

        /**
         * Method for log4j initialization
         * @param path String path for lof file
         */
        public void initializeLog4j(String path)
        {
          Properties prop = new Properties();
          prop.setProperty("log4j.appender.R","org.apache.log4j.RollingFileAppender");
          prop.setProperty("log4j.appender.R.MaxBackupIndex","5");
          prop.setProperty("log4j.appender.R.File",path);
          prop.setProperty("log4j.appender.R.layout","org.apache.log4j.PatternLayout");
          prop.setProperty("log4j.appender.R.layout.ConversionPattern","%p %t %c - %m%n");
          prop.setProperty("log4j.appender.stdout.layout.ConversionPattern","%5p [%t] (%F:%L) - %m%n");
          prop.setProperty("log4j.appender.stdout.layout","org.apache.log4j.PatternLayout");
          prop.setProperty("log4j.appender.stdout","org.apache.log4j.ConsoleAppender");
          prop.setProperty("log4j.rootLogger","info, stdout, R");
          PropertyConfigurator.configure(prop);
        }

        /**
         * Method for getting objects from CV side
         * @param objName String module name
         * @param params HashMap hash with all necessary params
         * @return ArrayList with objects requeried
         */
        public ArrayList getObjects(String objName,HashMap params,int indId)
        {
          ArrayList retVector = null;
          try
          {
            if ( isEntityModule(objName) )
            {
              params.put("entitymodule",objName);
              retVector = this.getEntities(params);
            }
            else if ( objName.equals("Individual") )
            {
              retVector = this.getIndividuals(params);
            }
            else if ( objName.equals("Invoice") )
            {
              retVector = this.getInvoices(params,indId);
            }
            else if ( objName.equals("PurchaseOrder") )
            {
              retVector = this.getPurchaseOrders(params,indId);
            }
            else if ( objName.equals("Expense") )
            {
              retVector = this.getExpenses(params,indId);
            }
            else if (isItemModule(objName))
            {
              params.put("itemname",objName);
              retVector = this.getItems(params,indId);
            }
            else if ( objName.equals("Payment") )
            {
              retVector = this.getPayments(params,indId);
            }
            else if ( objName.equals("PaymentMethod") )
            {
              retVector = this.getPaymentMethods(params);
            }
            else if ( objName.equals("GLAccount") )
            {
              retVector = this.getGLAccounts(params);
            }
          }catch(Exception e)
          {
            logger.debug("Error in getting Objects : "+e);
            e.printStackTrace();
          }
          return retVector;
        }

        /**
         * Method for adding objects to CV side
         * @param objName String module name
         * @param VOs ArrayList value objects to add
         * @param indId int individual who adds objects
         */
        public void addObjects(String objName,ArrayList VOs,int indId)
        {
          try
          {
            if (isEntityModule(objName) )
            {
              this.addEntities(VOs,indId);
            }
            else if ( objName.equals("Individual") )
            {
              this.addIndividuals(VOs,indId);
            }
            else if ( objName.equals("Invoice") )
            {
              this.addInvoices(VOs,indId);
            }
            else if ( objName.equals("PurchaseOrder") )
            {
              this.addPurchaseOrders(VOs,indId);
            }
            else if ( objName.equals("Expense") )
            {
              this.addExpenses(VOs,indId);
            }
            else if (isItemModule(objName))
            {
              this.addItems(VOs,indId);
            }
            else if ( objName.equals("Payment") )
            {
              this.addPayments(VOs,indId);
            }
            else if ( objName.equals("PaymentMethod") )
            {
              this.addPaymentMethods(VOs,indId);
            }
            else if ( objName.equals("GLAccount") )
            {
              this.addGLAccounts(VOs,indId);
            }
          }catch(Exception e)
          {
            logger.debug("Error in adding Objects : "+e);
            e.printStackTrace();
          }
        }

        /**
         * Method for deleting objects from CV side
         * @param objName String mosule name
         * @param VOs ArrayList value objects to delete
         * @param indId int individual who deletes objects
         */
        public void deleteObjects(String objName,ArrayList VOs,int indId)
        {
          try
          {
            if (isEntityModule(objName) )
            {
              this.deleteEntities(objName,VOs,indId);
            }
            else if ( objName.equals("Individual") )
            {
              this.deleteIndividuals(objName,VOs,indId);
            }
            else if ( objName.equals("Invoice") )
            {
              this.deleteInvoices(objName,VOs,indId);
            }
            else if ( objName.equals("PurchaseOrder") )
            {
              this.deletePurchaseOrders(objName,VOs,indId);
            }
            else if ( objName.equals("Expense") )
            {
              this.deleteExpenses(objName,VOs,indId);
            }
            else if (isItemModule(objName))
            {
              this.deleteItems(objName,VOs,indId);
            }
            else if ( objName.equals("Payment") )
            {
              this.deletePayments(objName,VOs,indId);
            }
            else if ( objName.equals("PaymentMethod") )
            {
              this.deletePaymentMethods(objName,VOs);
            }
            else if ( objName.equals("GLAccount") )
            {
              this.deleteGLAccounts(objName,VOs);
            }
          }catch(Exception e)
          {
            logger.debug("Error in deleting Objects : "+e);
            e.printStackTrace();
          }
        }

        /**
         *
         * @param objName String module name
         * @param VOs ArrayList value objects to update
         * @param indId int individual who updates objects
         */
        public void updateObjects(String objName, ArrayList VOs,int indId)
        {
          try
          {
            if (isEntityModule(objName) )
            {
              this.updateEntities(VOs, indId);
            }
            else if ( objName.equals("Individual") )
            {
              this.updateIndividuals(VOs, indId);
            }
            else if ( isItemModule(objName))
            {
              this.updateItems(VOs, indId);
            }
          }catch(Exception e)
          {
            logger.debug("Error in updating Objects : "+e);
            e.printStackTrace();
          }
        }

        /**
         * Methods for determinig new and modified objects from QB side
         * @param qbObjectList ArrayList list with objects from QB side
         * @param modulename String module name
         * @param lastSDate Date date of last synchronization
         * @return HashMap with new and modify lists
         */
        public HashMap chooseNewAndModify(ArrayList qbObjectList, String modulename, Date lastSDate)
        {
          // TODO does this need to be in the EJB layer?
          HashMap newAndModifyMap = new HashMap();
          ArrayList newObjs = new ArrayList();
          ArrayList modObjs = new ArrayList();
          Class voidType[] = null;
          Object qbObject = null;
          Date temp = new Date();
          Timestamp qbCreatedDateTimeStamp = new Timestamp(0);
          Timestamp lastSyncTimeStamp = this.dateConverter(lastSDate);

          try
          {
            String moduleName = this.convertModuleName(modulename);
            Class qbClass = Class.forName(moduleName);
            Method qbMethod = qbClass.getMethod("getCreatedOn",voidType);

            Iterator it = qbObjectList.iterator();
            while (it.hasNext()) 
            {
              qbObject = it.next();
              temp = (Date)qbMethod.invoke(qbObject,voidType);
              qbCreatedDateTimeStamp = this.dateConverter(temp);
              if (qbCreatedDateTimeStamp.after(lastSyncTimeStamp))
              {
                newObjs.add(qbObject);
              }else{
                modObjs.add(qbObject);
              }
            }
            newAndModifyMap.put("ADD",newObjs);
            newAndModifyMap.put("MODIFY",modObjs);
          } catch (Exception e) {
            System.out.println("[Exception][QBSyncEJB.chooseNewAndModify] Exception Thrown: " + e);
            logger.debug("Error detecting new and modified objects: "+ e);
            e.printStackTrace();
            throw new EJBException(e);
          }
          return newAndModifyMap;
        }

        /**
         * Method to update external ids on CV side
         * @param hm HashMap hash with CV ids and corresponding QB ids
         * @param modulename String module name
         * @return boolean status of operation
         */
        public boolean updateExternalIDs(HashMap hm, String modulename)
        {
          logger.info("Updating external IDs on CV side in " + modulename + " module");
          boolean res = false;
          CVDal dl = new CVDal(dataSource);

          String mName;
          if ( isItemModule(modulename) )
              mName = "Item";
          else if (isEntityModule(modulename))
              mName = "Entity";
          else
              mName = modulename;

          dl.setSqlQuery("SELECT primarytable, primarykeyfield FROM module m WHERE name ='"+mName+"'");
          Collection rows = dl.executeQuery();
          Object data[] = (rows == null || rows.size() == 0)? null : rows.toArray();

          if (data == null) {
             System.out.println("QBSync.updateExternalIDs[data]: Primary table is not exist for module '"+modulename+"'");
          }
          else {
          HashMap hmq = (HashMap)data[0];
          String primarytable = (String)hmq.get("primarytable");
          String prKeyField = (String)hmq.get("primarykeyfield");

          String idcv = "";
          String idqb = "";

          if (hm != null)
          {
            Set cvids = hm.keySet();
            Iterator it = cvids.iterator();
            while (it.hasNext())
            {
              idcv = (String)it.next();
              idqb = (String)hm.get(idcv);
              dl.setSqlQueryToNull();
              dl.setSqlQuery("UPDATE " + primarytable + " SET ExternalID = '" + idqb + "' WHERE " + prKeyField + "=" + idcv);
              dl.executeUpdate();
            }
            res = true;
          }
          }
          dl.clearParameters();
          dl.destroy();
          dl=null;
          logger.info("External IDs "+((res)?"":"un")+"successfully updated in " + modulename + " module");
          return res;
        }

        /**
         * Method for getting full module name
         * @param modulename String module name
         * @return String full module name
         */
        public String convertModuleName (String modulename)
        {
          String retValue = "";
          if ( isEntityModule(modulename) )
          {
            retValue = "com.centraview.contact.entity.EntityVO";
          }
          else if ( modulename.equals("Individual") )
          {
            retValue = "com.centraview.contact.individual.IndividualVO";
          }
          else if ( modulename.equals("Invoice") )
          {
            retValue = "com.centraview.account.invoice.InvoiceVO";
          }
          else if ( modulename.equals("PurchaseOrder") )
          {
            retValue = "com.centraview.account.purchaseorder.PurchaseOrderVO";
          }
          else if ( modulename.equals("Expense") )
          {
            retValue = "com.centraview.account.expense.ExpenseVO";
          }
          else if ( isItemModule(modulename) )
          {
            retValue = "com.centraview.account.item.ItemVO";
          }
          else if ( modulename.equals("Payment") )
          {
            retValue = "com.centraview.account.payment.PaymentVO";
          }
          else if ( modulename.equals("PaymentMethod") )
          {
            retValue = "com.centraview.account.helper.PaymentMethodVO";
          }
          else if ( modulename.equals("GLAccount") )
          {
            retValue = "com.centraview.account.helper.GLAccountVO";
          }

          return retValue;
        }

        /**
         * Method for date convertation
         * @param date Date last synchronization date
         * @return Timestamp last synchronization date
         */
        public Timestamp dateConverter(Date date)
        {
          Timestamp newD = new Timestamp(date.getTime());
          return newD;
        }

        /**
         * Method for synchronization new or modified objects
         * @param indId int individual id
         * @param lastSDate Date last synchronization date
         * @param moduleName String module name
         * @param qbVOs ArrayList value objects from QB side
         * @param flag String type of operation
         * @return ArrayList with value objects from CV side
         */
        public ArrayList beginSynchronisation(int indId, Date lastSDate, String moduleName,ArrayList qbVOs, String flag)
        {
          ArrayList cvArray = new ArrayList();
          HashMap params = new HashMap();

          String ADD = "ADD";
          String MODIFY = "MODIFY";

          String mName = this.convertModuleName(moduleName);
          Timestamp lastSyncDate = dateConverter(lastSDate);

          Class voidType[] = null;
          Class qbClass = null;
          Object qbObj = null;
          Object cvObj = null;
          Method mth = null;
          Method md = null;
          String extIdQB = "";
          String extIdCV = "";
          Object mdateQB = null;
          Object mdateCV = null;


          try{

            if (flag.equals(ADD)) {
              logger.info("Getting objects from CV in " + moduleName + " module");
              params.put("Operation",flag);
              params.put("lastSyncDate", lastSyncDate);
              cvArray = this.getObjects(moduleName, params, indId);
              logger.info("Got objects from CV in " + moduleName + " module");

              logger.info("Adding object to CV ");
              this.addObjects(moduleName, qbVOs, indId);
              logger.info("Added objects to CV ");

            }else if (flag.equals(MODIFY)){

              qbClass = Class.forName(mName);
              mth = qbClass.getMethod("getExternalID",voidType);
              md = qbClass.getMethod("getModifiedOn",voidType);

              logger.info("Getting objects from CV in " + moduleName + " module");
              params.put("Operation",flag);
              params.put("lastSyncDate", lastSyncDate);
              cvArray = this.getObjects(moduleName, params,indId);
              logger.info("Got objects from CV in " + moduleName + " module");

              Iterator itcv = cvArray.iterator();
              Iterator itqb = qbVOs.iterator();

              logger.info("Synchronizing of modified objects in " + moduleName + " module");
              while (itcv.hasNext())
              {
                cvObj = itcv.next();
                extIdCV = mth.invoke(cvObj,voidType).toString();
                mdateCV = md.invoke(cvObj,voidType);
                while(itqb.hasNext())
                {
                  qbObj = itqb.next();
                  extIdQB = mth.invoke(qbObj,voidType).toString();
                  mdateQB = md.invoke(qbObj,voidType);
                  if(extIdQB.equals(extIdCV))
                  {
                    if (((Timestamp)mdateCV).before((Timestamp)mdateQB))
                    {
                      cvArray.remove(cvObj);
                    }else{
                      qbVOs.remove(qbObj);
                    }
                    break;
                  }
                }
                itqb = qbVOs.iterator();
               }
               logger.info("Modified objects synchronized in " + moduleName + " module");

/*               int i = 0;
               int j = 0;
               int cvsize = cvArray.size();
               int qbsize = qbVOs.size();
               boolean cont = true;
               int compare = 0;

               while(cont)
               {
                 cvObj = cvArray.get(i);
                 qbObj = qbVOs.get(j);
                 extIdCV = mth.invoke(cvObj,voidType).toString();
                 extIdQB = mth.invoke(qbObj,voidType).toString();
                 compare = extIdCV.compareTo(extIdQB);

                 if(compare < 0)
                 {
                   i++;
                 }else if(compare > 0)
                 {
                   j++;
                 }else
                 {
                     if (((Timestamp)mdateCV).before((Timestamp)mdateQB))
                     {
                       cvArray.remove(cvObj);
                       j++;
                     }else{
                       qbVOs.remove(qbObj);
                       i++;
                     }
                  }

                  if((i == cvsize) || (j == qbsize))
                  {
                    cont = false;
                  }
               }*/


              logger.info("Updating objects on CV side in " + moduleName + " module");
              this.updateObjects(moduleName, qbVOs, indId);
              logger.info("Objects on CV side updated in " + moduleName + " module");

            }
          }catch(Exception e){
            logger.debug("Error while synchronizing objects : "+ e);
            e.printStackTrace();
          }

          return cvArray;

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
                  ||moduleName.equals("ItemService"));
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
         * Method for obtaining ids collection
         * @param moduleName String module name
         * @param lastSyncDate Timestamp last synchronization date
         * @param flag String type of operation
         * @param syncDatePrKeyField String[] primary key field for given modulename
         * @return Collection of ids in given modulename
         */
        public Collection getList(String moduleName, Timestamp lastSyncDate, String flag, String syncDatePrKeyField[])
        {
          CVDal dl = new CVDal(dataSource);
          if (isItemModule(moduleName))
          {
            dl.setSqlQuery("SELECT primarytable, primarykeyfield FROM module m WHERE name ='Item'");
          }
          else if (isEntityModule(moduleName))
          {
            dl.setSqlQuery("SELECT primarytable, primarykeyfield FROM module m WHERE name ='Entity'");
          }
          else
          {
            dl.setSqlQuery("SELECT primarytable, primarykeyfield FROM module m WHERE name ='" + moduleName + "'");
          }

          Collection rows = dl.executeQuery();
          dl.setSqlQueryToNull();
          Object data[] = rows.toArray();
          HashMap hm = (HashMap)data[0];
          String primarytable = (String)hm.get("primarytable");
          syncDatePrKeyField[0] = (String)hm.get("primarykeyfield");

          String typeID = "0";

          String cdate = "Created";
          String mdate = "Modified";
          String query = "";

          if (isItemModule(moduleName))
          {
            cdate = "createddate";
            mdate = "modifieddate";
          }

          if (moduleName.equals("ItemInventory") || moduleName.equals("Customer"))
          {
              typeID = "1";
          }
          else if (moduleName.equals("ItemNonInventory") || moduleName.equals("Vendor"))
          {
              typeID = "2";
          }
          else if (moduleName.equals("ItemService"))
          {
              typeID = "3";
          }

          if(flag.equals("ADD"))
          {
            if (moduleName.equals("Individual"))
            {
              query = "SELECT employee.IndividualID FROM employee LEFT JOIN individual ON employee.IndividualID = individual.IndividualID "
                  + "WHERE " + cdate + ">'" + lastSyncDate + "'";
            }else if (isItemModule(moduleName)){
              query = "SELECT " + syncDatePrKeyField[0] + " FROM " + primarytable +
                  " WHERE " + cdate + ">'" + lastSyncDate + "' and type = " + typeID;
            }
            else if (isEntityModule(moduleName)){
              query = "SELECT " + syncDatePrKeyField[0] + " FROM " + primarytable + ", customfieldmultiple" +
                  " WHERE " + primarytable +"." + cdate + ">'" + lastSyncDate + "'" + " and " +
                  primarytable + "." + "EntityId = " + "customfieldmultiple.RecordID and customfieldmultiple.ValueID = " + typeID;
            }
            else {
              query = "SELECT " + syncDatePrKeyField[0] + " FROM " + primarytable +
                  " WHERE " + cdate + ">'" + lastSyncDate + "'";
            }

            System.out.println("Query is " + query);
            dl.setSqlQuery(query);
          }else if(flag.equals("MODIFY")){
            if (moduleName.equals("Individual"))
            {
              query = "SELECT employee.IndividualID FROM employee LEFT JOIN individual ON employee.IndividualID = individual.IndividualID "
                  + "WHERE " + mdate + ">'" + lastSyncDate + "' AND " + cdate + "<'" + lastSyncDate + "'";
            }else if(isItemModule(moduleName)){

              query = "SELECT " + syncDatePrKeyField[0] + " FROM " + primarytable +
                  " WHERE " + mdate + ">'" + lastSyncDate + "' AND " + cdate + "<'" + lastSyncDate + "' and type = " + typeID;
            }
            else if(isEntityModule(moduleName)){
              query = "SELECT " + syncDatePrKeyField[0] + " FROM " + primarytable + ", customfieldmultiple" +
                  " WHERE " + primarytable +"." + mdate + ">'" + lastSyncDate + "'" + " and " + primarytable +"." + cdate + "<'" + lastSyncDate + "'"
                  +" and " + primarytable + "." + "EntityId = " + "customfieldmultiple.RecordID and customfieldmultiple.ValueID = " + typeID;
            }
            else{
              query = "SELECT " + syncDatePrKeyField[0] + " FROM " + primarytable +
                  " WHERE " + mdate + ">'" + lastSyncDate + "' AND " + cdate + "<'" + lastSyncDate + "'";
            }
            dl.setSqlQuery(query);
          }
          rows = dl.executeQuery();
          dl.destroy();

          System.out.println("rows are " + rows);
          return rows;
        }

        /**
         * Method for getting CV id by external id
         * @param objName String module name
         * @param ExtId String external id
         * @return int CV id
         */
        public int getCVidForExtid(String objName, String ExtId )
        {
          int cvid = 0;
          CVDal dl = new CVDal(this.dataSource);
          try
          {

            dl.setSql("synch.getCVidFor"+objName);
            dl.setString(1,ExtId);
            Collection rows = dl.executeQuery();
            dl.destroy();
            Iterator it = rows.iterator();
            HashMap hm = new HashMap();
            if (it.hasNext())
            {
              hm = (HashMap) it.next();
            }
            if (hm != null)
            {
              if(hm.containsKey("cvid"))
              {
                cvid = ( (Long) hm.get("cvid")).intValue();
              }
            }
          }catch(Exception e)
          {
            logger.debug("Error in getting CVidForExtid"+objName+" : "+e);
            e.printStackTrace();
          }
          finally {
             dl.clearParameters();
             dl.destroy();
             dl=null;
          }
          return cvid;
       }

       /**
        * Method for getting external id by CV id
        * @param objName Stringmodule name
        * @param CVid int cv id
        * @return String external id
        */
       public String getExtidforCVid(String objName,int CVid)
       {
         String extid = "";
         CVDal dl = new CVDal(this.dataSource);
         try
         {

           dl.setSql("synch.getExtidFor"+objName);
           dl.setInt(1,CVid);
           Collection rows = dl.executeQuery();
           dl.destroy();
           Iterator it = rows.iterator();
           HashMap hm = new HashMap();
           if (it.hasNext())
           {
             hm = (HashMap) it.next();
           }
           if (hm != null)
           {
             if (hm.containsKey("extid"))
             {
               extid = (String) hm.get("extid");
             }
           }
         }catch(Exception e)
         {
           logger.debug("Error in getting ExtidForCVid"+objName+" : "+e);
           e.printStackTrace();
         }
         finally {
             dl.clearParameters();
             dl.destroy();
             dl=null;
         } 
         return extid;
       }

       /**
        * Method for getting CV entities
        * @param params HashMap operation type + last synchronization date
        * @return ArrayList with entities
        */
       public ArrayList getEntities(HashMap params)
       {
         EntityVO entityVO = null;
         ArrayList entities = new ArrayList();
         ContactFacadeLocal entL = null;

         try
         {
           String syncDatePrKeyField[] = {""};
           Timestamp lastSyncDate = (Timestamp)params.get("lastSyncDate");
           String operation = (String)params.get("Operation");
           InitialContext ic = CVUtility.getInitialContext();
           ContactFacadeLocalHome home = (ContactFacadeLocalHome)ic.lookup("local/ContactFacade");
           entL = home.create();
           entL.setDataSource(this.dataSource);

           Collection col = this.getList((String)params.get("entitymodule"),lastSyncDate,operation,syncDatePrKeyField);
           Object ids [] = col.toArray();
           HashMap hm = new HashMap();
           int size = ids.length;

           for(int i=0;i<size;i++)
           {
             hm = (HashMap)ids[i];
             entityVO = entL.getEntity(((Long)hm.get(syncDatePrKeyField[0])).intValue());
             entities.add(entityVO);
           }
         }catch(Exception e)
         {
           logger.debug("Error in getting EntityVos : "+e);
           e.printStackTrace();
         }

         return entities;
       }

       /**
        * Method for adding entities on CV side
        * @param EVOs ArrayList with entities to add on CV side
        * @param indId int individual id
        * @return boolean stetus of operation
        */
       public boolean addEntities(ArrayList EVOs,int indId)
       {
         EntityVO evo = null;
         ContactFacadeLocal entL = null;
         int newId = 0;

         try
         {
           InitialContext ic = CVUtility.getInitialContext();
           ContactFacadeLocalHome home = (ContactFacadeLocalHome)ic.lookup("local/ContactFacade");
           entL = home.create();
           entL.setDataSource(this.dataSource);

           int size = (EVOs == null)? 0 : EVOs.size();
           for ( int i=0;i<size;i++)
           {
             evo = (EntityVO) EVOs.get(i);
             newId = entL.createEntity(evo,indId);
             logger.info("Added " + newId + " entity record");
           }
         }catch(Exception e)
         {
           logger.debug("Error in adding Entities : "+e);
           e.printStackTrace();
           return false;
         }
         return true;
       }

       /**
        * Method for updating entities on CV side
        * @param EVOs ArrayList with entities
        * @param indID int individual id
        * @return boolean status of operation
        */
       public boolean updateEntities(ArrayList EVOs,int indID)
       {
         EntityVO evo = null;
         ContactFacadeLocal entL = null;
         ContactHelperLocal contacthelperL = null;

         try
         {
           InitialContext ic = CVUtility.getInitialContext();
           ContactFacadeLocalHome home = (ContactFacadeLocalHome)ic.lookup("local/ContactFacade");
           entL = home.create();
           entL.setDataSource(this.dataSource);
           int size = (EVOs == null)? 0 : EVOs.size();

           ContactHelperLocalHome homeHelper = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
           contacthelperL = homeHelper.create();
           contacthelperL.setDataSource(this.dataSource);


           for ( int i=0;i<size;i++)
           {
             evo = (EntityVO) EVOs.get(i);
             AddressVO adrVO = contacthelperL.getPrimaryAddressForContact(indID, evo.getContactID(), evo.getContactType());
             evo.getPrimaryAddress().setAddressID(adrVO.getAddressID());

             Collection col = contacthelperL.getPrimaryMOCForContact(indID, evo.getContactID(), evo.getContactType());
             Iterator it = col.iterator();
             Vector vec = evo.getMOC();
             int vecsize = vec.size();
             while (it.hasNext()){
               MethodOfContactVO moc = (MethodOfContactVO)it.next();
               for (int j = 0; j < vecsize; j++){
                 if(moc.getMocType() == ((MethodOfContactVO) vec.elementAt(j)).getMocType()){
                   ((MethodOfContactVO) vec.elementAt(j)).setMocID(moc.getMocID());
                   ((MethodOfContactVO) vec.elementAt(j)).added(false);
                   ((MethodOfContactVO) vec.elementAt(j)).updated(true);
                   break;
                 }
               }
             }
             entL.updateEntity(evo,indID);
             logger.info("Updated " + evo.getContactID() + " entity record");
           }
         }catch(Exception e)
         {
           logger.debug("Error in updating Entity : "+e);
           e.printStackTrace();
           return false;
         }
         return true;
       }

       /**
        * Method for deleting entities from CV side
        * @param modulename String module name
        * @param arl ArrayList list with entities
        * @param indId int individual id
        */
       public void deleteEntities(String modulename, ArrayList arl, int indId)
       {
         if (arl==null) {
            return;
         }
         EntityVO pvo = new EntityVO();
         String extId = "";
         int intId = 0;

         Iterator it = arl.iterator();

         ContactFacadeLocal helperL = null;
         try
         {
           InitialContext ic = CVUtility.getInitialContext();
           ContactFacadeLocalHome home = (ContactFacadeLocalHome ) ic.lookup("local/ContactFacade");
           helperL = home.create();

           while(it.hasNext())
           {
             pvo = (EntityVO)it.next();
             extId = pvo.getExternalID();
             intId = this.getCVidForExtid(modulename,extId);
             helperL.deleteEntity(intId, indId);

             logger.info("Entity " + intId +" object deleted");
           }

         }catch(Exception e)
         {
           logger.debug("Error in deleteing Entity : "+e);
           e.printStackTrace();
         }
       }

       /**
        * Method for getting invividuals from CV side
        * @param params HashMap type of operation + last synchronization date
        * @return ArrayList with individuals
        */
       public ArrayList getIndividuals(HashMap params)
       {
         IndividualVO indVO = null;
         ArrayList individuals = new ArrayList();
         ContactFacadeLocal indL = null;

         try
         {
           String syncDatePrKeyField[] = {""};
           Timestamp lastSyncDate = (Timestamp)params.get("lastSyncDate");
           String operation = (String)params.get("Operation");
           InitialContext ic = CVUtility.getInitialContext();
           ContactFacadeLocalHome home = (ContactFacadeLocalHome)ic.lookup("local/ContactFacade");
           indL = home.create();
           indL.setDataSource(this.dataSource);

           Collection col = getList("Individual",lastSyncDate,operation,syncDatePrKeyField);
           Object ids[] = col.toArray();
           HashMap hm = new HashMap();
           int size = ids.length;

           for(int i=0;i<size;i++)
           {
             hm = (HashMap)ids[i];
             indVO = indL.getIndividual(((Long)hm.get(syncDatePrKeyField[0])).intValue());
             individuals.add(indVO);
           }
         }catch(Exception e)
         {
           logger.debug("Error in getting IndividualVos : "+e);
           e.printStackTrace();
         }
         return individuals;
       }

       /**
        * Method for adding individuals to CV side
        * @param IVOs ArrayList with individuals from QB
        * @param indId int individual id
        * @return boolean status of operation
        */
       public boolean addIndividuals(ArrayList IVOs, int indId)
       {
         IndividualVO ivo = null;
         int newId = 0;
         ContactFacadeLocal indL = null;
         HrFacadeLocal empL = null;

         try
         {
           InitialContext ic = CVUtility.getInitialContext();
           ContactFacadeLocalHome home = (ContactFacadeLocalHome)ic.lookup("local/ContactFacade");
           indL = home.create();
           indL.setDataSource(this.dataSource);

           HrFacadeLocalHome emplhome = (HrFacadeLocalHome)ic.lookup("local/HrFacade");
           empL = (HrFacadeLocal)emplhome.create();
           empL.setDataSource(this.dataSource);

           int size = IVOs.size();

           for ( int i=0;i<size;i++)
           {
             ivo = (IndividualVO) IVOs.get(i);
             newId = indL.createIndividual(ivo,indId);
             empL.addEmployee(indId,newId);
             logger.info("Added " + newId + " individual record");
           }
         }catch(Exception e)
         {
           logger.debug("Error in adding Individuals : "+e);
           e.printStackTrace();
           return false;
         }
         return true;
       }

       /**
        * Method for updating individuals on CV side
        * @param IVOs ArrayList list of individuas
        * @param indID int individual id
        * @return boolean status of operation
        */
       public boolean updateIndividuals(ArrayList IVOs,int indID)
       {
         IndividualVO ivo = null;
         ContactFacadeLocal indL = null;
         ContactHelperLocal contacthelperL = null;

         try
         {
           InitialContext ic = CVUtility.getInitialContext();
           ContactFacadeLocalHome home = (ContactFacadeLocalHome)ic.lookup("local/ContactFacade");
           indL = home.create();
           indL.setDataSource(this.dataSource);

           int size = IVOs.size();

           ContactHelperLocalHome homeHelper = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
           contacthelperL = homeHelper.create();
           contacthelperL.setDataSource(this.dataSource);

           for ( int i=0;i<size;i++)
           {
             ivo = (IndividualVO) IVOs.get(i);
             ivo.setContactID(ivo.getIndividualID());

             AddressVO adrVO = contacthelperL.getPrimaryAddressForContact(indID, ivo.getIndividualID(), ivo.getContactType());
             ivo.getPrimaryAddress().setAddressID(adrVO.getAddressID());

             Collection col = contacthelperL.getPrimaryMOCForContact(indID, ivo.getContactID(), ivo.getContactType());
             Iterator it = col.iterator();
             Vector vec = ivo.getMOC();
             int vecsize = vec.size();
             while (it.hasNext()){
               MethodOfContactVO moc = (MethodOfContactVO)it.next();
               for (int j = 0; j < vecsize; j++){
                 if(moc.getMocType() == ((MethodOfContactVO) vec.elementAt(j)).getMocType()){
                   ((MethodOfContactVO) vec.elementAt(j)).setMocID(moc.getMocID());
                   ((MethodOfContactVO) vec.elementAt(j)).added(false);
                   ((MethodOfContactVO) vec.elementAt(j)).updated(true);
                   break;
                 }
               }
             }
             indL.updateIndividual(ivo,indID);
             logger.info("Updated " + ivo.getIndividualID() + " Individual record");
           }
         }catch(Exception e)
         {
           logger.debug("Error in updating Individual : "+e);
           e.printStackTrace();
           return false;
         }
         return true;
       }

       /**
        * Method for deleting objects from CV side
        * @param modulename String module name
        * @param arl ArrayList with individuals
        * @param indId int individual id
        */
       public void deleteIndividuals(String modulename, ArrayList arl, int indId)
       {
         IndividualVO ivo = new IndividualVO();
         String extId = "";
         int intId = 0;

         Iterator it = arl.iterator();

         ContactFacadeLocal helperL = null;
         try
         {
           InitialContext ic = CVUtility.getInitialContext();
           ContactFacadeLocalHome home = (ContactFacadeLocalHome ) ic.lookup("local/ContactFacade");
           helperL = home.create();

           while(it.hasNext())
           {
             ivo = (IndividualVO)it.next();
             extId = ivo.getExternalID();
             intId = this.getCVidForExtid(modulename,extId);
             helperL.deleteIndividual(indId, intId);
             logger.info("Individual " + intId + " object deleted");
           }

         }catch(Exception e)
         {
           logger.debug("Error in deleting Individual : "+e);
           e.printStackTrace();
         }
       }

       /**
        * Method for getting invoices from CV side
        * @param params HashMap operation type + last synchronization date
        * @return ArrayList of invoices from CV side
        */
       public ArrayList getInvoices(HashMap params,int indId)
       {
         InvoiceVO invoiceVO = null;
         ArrayList invoices = new ArrayList();
         AccountFacadeLocal invoiceL = null;

         try
         {
           String syncDatePrKeyField[] = {""};
           Timestamp lastSyncDate = (Timestamp)params.get("lastSyncDate");
           String operation = (String)params.get("Operation");
           InitialContext ic = CVUtility.getInitialContext();
           AccountFacadeLocalHome home = (AccountFacadeLocalHome)ic.lookup("local/AccountFacade");
           invoiceL = home.create();
           invoiceL.setDataSource(this.dataSource);

           Collection col = getList("InvoiceHistory",lastSyncDate,operation,syncDatePrKeyField);
           Object ids[] = col.toArray();
           HashMap hm = new HashMap();
           int size = ids.length;

           for(int i=0;i<size;i++)
           {
             hm = (HashMap)ids[i];
             invoiceVO = invoiceL.getInvoiceVO(((Long)hm.get(syncDatePrKeyField[0])).intValue(),indId);
             invoices.add(invoiceVO);
           }
         }catch(Exception e)
         {
           logger.debug("Error in getting InvoiceVos : "+e);
           e.printStackTrace();
         }
         return invoices;
       }

       /**
        * Method for adding invoices on CV side
        * @param IVOs ArrayList list of invoices
        * @param indId int individual id
        * @return boolean status of operation
        */
       public boolean addInvoices(ArrayList IVOs, int indId)
       {
         InvoiceVO ivo = null;
         AccountFacadeLocal invoiceL = null;
         ContactHelperLocal chl = null;

         try
         {
           InitialContext ic = CVUtility.getInitialContext();
           AccountFacadeLocalHome home = (AccountFacadeLocalHome)ic.lookup("local/AccountFacade");
           invoiceL = home.create();
           invoiceL.setDataSource(this.dataSource);

           ContactHelperLocalHome chlh = (ContactHelperLocalHome) ic.lookup("local/ContactHelper");
           chl = chlh.create();

           OrderForm of = new OrderForm();
           int primAddId = 0;

           for ( int i=0;i<IVOs.size();i++)
           {
             ivo = (InvoiceVO) IVOs.get(i);
             if (ivo.getDescription()== null)
               ivo.setDescription("");
             of.setItemLines(ivo.getItemLines());
             of.setCustomerIdValue(ivo.getCustomerId());
             primAddId = ((AddressVO)chl.getPrimaryAddressForContact(indId,ivo.getCustomerId(),1)).getAddressID();

             of.setBillToAddIdValue(primAddId);
             of.setShipToAddIdValue(1);
             of.setOrderDate(ivo.getInvoiceDate());
             of.setInvoiceIsGenerated(true);

             of = invoiceL.createOrder(of,indId);
             ivo.setOrderId(of.getOrderIdValue());
             ivo.setBillToId(primAddId);
             invoiceL.createInvoice(ivo,indId);
             logger.info("Added " +/* ivo.getInvoiceId()*/ 1+ " invoice record");
           }
         }catch(Exception e)
         {
           logger.debug("Error in adding Invoices : "+e);
           e.printStackTrace();
           return false;
         }
         return true;
       }

       /**
        * Method for deleting invoices from CV side
        * @param modulename String module name
        * @param arl ArrayList list with invoices to delete
        * @param indId int individual id
        */
       public void deleteInvoices(String modulename, ArrayList arl, int indId)
       {
         InvoiceVO ivo = new InvoiceVO();
         String extId = "";
         int intId = 0;

         Iterator it = arl.iterator();

         AccountFacadeLocal helperL = null;
         try
         {
           InitialContext ic = CVUtility.getInitialContext();
           AccountFacadeLocalHome home = (AccountFacadeLocalHome ) ic.lookup("local/AccountFacade");
           helperL = home.create();

           while(it.hasNext())
           {
             ivo = (InvoiceVO)it.next();
             extId = ivo.getExternalId();
             intId = this.getCVidForExtid(modulename,extId);
             helperL.deleteInvoice(indId, intId);
             logger.info("Invoice " + intId +" object deleted");
           }

         }catch(Exception e)
         {
           logger.debug("Error in deleting Invoices : "+e);
           e.printStackTrace();
         }
       }

       /**
        * Method for getting objects from CV side
        * @param params HashMap operation type + last synchronization date
        * @return ArrayList with items from CV side
        */
       public ArrayList getItems(HashMap params,int indId)
       {
         ItemVO itemVO = null;
         ArrayList items = new ArrayList();

         try
         {
           String syncDatePrKeyField[] = {""};
           Timestamp lastSyncDate = (Timestamp)params.get("lastSyncDate");
           String operation = (String)params.get("Operation");
           InitialContext ic = CVUtility.getInitialContext();
           ItemLocalHome home = (ItemLocalHome)ic.lookup("local/Item");
           ItemLocal itemL = home.create();

           Collection col = getList((String)params.get("itemname"),lastSyncDate,operation,syncDatePrKeyField);
           Object ids[] = col.toArray();
           HashMap hm = new HashMap();
           int size = ids.length;

           for(int i=0;i<size;i++)
           {
             hm = (HashMap)ids[i];
             itemVO = itemL.getItem(indId,((Long)hm.get(syncDatePrKeyField[0])).intValue());
             items.add(itemVO);
           }
         }catch(Exception e)
         {
           logger.debug("Error in getting ItemVos : "+e);
           e.printStackTrace();
         }
         return items;
       }

       /**
        * Method for adding items to CV side
        * @param IVOs ArrayList list with items
        * @param indId int individual id
        * @return boolean status of operation
        */
       public boolean addItems(ArrayList IVOs,int indId)
       {
         ItemVO ivo = null;
         int newId = 0;

         try
         {
           InitialContext ic = CVUtility.getInitialContext();
           ItemLocalHome home = (ItemLocalHome)ic.lookup("local/Item");
           ItemLocal itemL = home.create();

           int size = IVOs.size();

            for ( int i=0;i<size;i++) {

             ivo = (ItemVO) IVOs.get(i);
                if (null != ivo.getParentExternalID())
                    ivo.setSubItemOfId(getItemId(ivo.getParentExternalID()));
             newId = itemL.addItem(indId,ivo);
             logger.info("Added " + newId + " item record");
           }
         }catch(Exception e)
         {
           logger.debug("Error in adding Items : "+e);
           e.printStackTrace();
           return false;
         }
         return true;
       }
    /**
     * Method to get items id from external id
     * @param externalId String external id
     * @return int item ID
     */
    public int getItemId(String externalId) {
        int itemId = 0;
        CVDal dl = new CVDal(dataSource);
        dl.setSqlQuery("SELECT itemid FROM item WHERE externalID ='"+externalId+"'");
        Collection rows = dl.executeQuery();
        Iterator it = rows.iterator();

        if (rows != null) {

            while (it.hasNext()) {

                HashMap hm  = (HashMap)it.next();
                itemId = ((Number) hm.get("itemid")).intValue();
                System.out.println("SEV: ItemId = "+itemId+" externalID = "+externalId);
            }
        }
        return itemId;
    }

       /**
        * Method for updating items on CV side
        * @param IVOs ArrayList list with items
        * @param userID int individual id
        * @return boolean status of operation
        */
       public boolean updateItems(ArrayList IVOs,int userID)
       {
         ItemVO ivo = null;
         try
         {
           InitialContext ic = CVUtility.getInitialContext();
           ItemLocalHome home = (ItemLocalHome)ic.lookup("local/Item");
           ItemLocal itemL = home.create();

           int size = IVOs.size();

           for ( int i=0;i<size;i++)
           {
             ivo = (ItemVO) IVOs.get(i);
             itemL.updateItem(userID,ivo);
             logger.info("Updated " + ivo.getItemId() + " item record");
           }
         }catch(Exception e)
         {
           logger.debug("Error in updating Item : "+e);
           e.printStackTrace();
           return false;
         }
         return true;
       }// end of updateItem

       /**
        * Method for deleting items from CV side
        * @param modulename String module name
        * @param arl ArrayList list with items
        * @param indId int individual id
        */
       public void deleteItems(String modulename, ArrayList arl, int indId)
       {
         ItemVO ivo = new ItemVO();
         String extId = "";
         int intId = 0;

         Iterator it = arl.iterator();

         AccountFacadeLocal helperL = null;
         try
         {
           InitialContext ic = CVUtility.getInitialContext();
           AccountFacadeLocalHome home = (AccountFacadeLocalHome ) ic.lookup("local/AccountFacade");
           helperL = home.create();

           while(it.hasNext())
           {
             ivo = (ItemVO)it.next();
             extId = ivo.getExternalID();
             intId = this.getCVidForExtid(modulename,extId);
             helperL.deleteInvoice(indId, intId);
             logger.info("Item " + intId +" object deleted");
           }

         }catch(Exception e)
         {
           logger.debug("Error in deleting Item : "+e);
           e.printStackTrace();
         }
       }

       /**
        * Method for getting purchase orders from CV side
        * @param params HashMap operation type + last synchronization date
        * @return ArrayList with purchaseorders
        */
       public ArrayList getPurchaseOrders(HashMap params, int indId)
       {
         PurchaseOrderVO porderVO= null;
         ArrayList porders = new ArrayList();
         AccountFacadeLocal porderL = null;

         try
         {
           String syncDatePrKeyField[] = {""};
           Timestamp lastSyncDate = (Timestamp)params.get("lastSyncDate");
           String operation = (String)params.get("Operation");
           InitialContext ic = CVUtility.getInitialContext();
           AccountFacadeLocalHome home = (AccountFacadeLocalHome)ic.lookup("local/AccountFacade");
           porderL = home.create();
           porderL.setDataSource(this.dataSource);

           Collection col = getList("PurchaseOrder",lastSyncDate,operation,syncDatePrKeyField);
           Object ids[] = col.toArray();
           HashMap hm = new HashMap();
           int size = ids.length;

           for(int i=0;i<size;i++)
           {
             hm = (HashMap)ids[i];
             porderVO= porderL.getPurchaseOrderVO(((Long)hm.get(syncDatePrKeyField[0])).intValue(),indId);
             porders.add(porderVO);
           }
         }catch(Exception e)
         {
           logger.debug("Error in getting porderVOs : "+e);
           e.printStackTrace();
         }
         return porders;
       }// end of getPayments

       /**
        * Method for adding purchaseorders to CV side
        * @param PVOs ArrayList of purchaseorders
        * @param indId int individual id
        * @return boolean status of operation
        */
       public boolean addPurchaseOrders(ArrayList PVOs,int indId)
       {
         PurchaseOrderVO pvo = null;
         AccountFacadeLocal porderL = null;
         ContactHelperLocal chl = null;

         try
         {
           InitialContext ic = CVUtility.getInitialContext();
           AccountFacadeLocalHome home = (AccountFacadeLocalHome)ic.lookup("local/AccountFacade");
           porderL = home.create();
           porderL.setDataSource(this.dataSource);
           ContactHelperLocalHome chlh = (ContactHelperLocalHome) ic.lookup("local/ContactHelper");
           chl = chlh.create();

           int size = PVOs.size();
           int primAddId = 0;

           for ( int i=0;i<size;i++)
           {
             pvo = (PurchaseOrderVO) PVOs.get(i);
             primAddId = ((AddressVO)chl.getPrimaryAddressForContact(indId,pvo.getVendorId() ,1)).getAddressID();
             pvo.setBillToId(primAddId);
             pvo.setShipToId(primAddId);
             pvo = porderL.createPurchaseOrder(pvo,indId);
             logger.info("Added " + pvo.getPurchaseOrderId() + " purchaseorder record");
           }
         }catch(Exception e)
         {
           logger.debug("Error in adding PurchaseOrders : "+e);
           e.printStackTrace();
           return false;
         }
         return true;
       }

       /**
        * Method for deleting purchaseorders from CV side
        * @param modulename String module name
        * @param arl ArrayList list with purchaseorders
        * @param indId int individual id
        */
       public void deletePurchaseOrders(String modulename, ArrayList arl, int indId)
       {
         PurchaseOrderVO ivo = new PurchaseOrderVO();
         String extId = "";
         int intId = 0;

         Iterator it = arl.iterator();

         AccountFacadeLocal helperL = null;
         try
         {
           InitialContext ic = CVUtility.getInitialContext();
           AccountFacadeLocalHome home = (AccountFacadeLocalHome ) ic.lookup("local/AccountFacade");
           helperL = home.create();

           while(it.hasNext())
           {
             ivo = (PurchaseOrderVO)it.next();
             extId = ivo.getExternalID();
             intId = this.getCVidForExtid(modulename,extId);
             helperL.deleteInvoice(indId, intId);
             logger.info("PurchaseOrder " + intId +" object deleted");
           }

         }catch(Exception e)
         {
           logger.debug("Error in deleting PurchaseOrder : "+e);
           e.printStackTrace();
         }
       }

       /**
        * Method for getting expenses from CV side
        * @param params HashMap operation type + last synchronization date
        * @return ArrayList list of expenses
        */
       public ArrayList getExpenses(HashMap params, int indId)
       {
         ExpenseVO EVO = null;
         ArrayList expenses = new ArrayList();
         AccountFacadeLocal expenseL = null;

         try{
           String syncDatePrKeyField[] = {""};
           System.out.println("Getting last sync date");
           Timestamp lastSyncDate = (Timestamp)params.get("lastSyncDate");
           System.out.println("Getting operation");
           String operation = (String)params.get("Operation");
           InitialContext ic = CVUtility.getInitialContext();
           AccountFacadeLocalHome home = (AccountFacadeLocalHome)ic.lookup("local/AccountFacade");
           expenseL = home.create();
           expenseL.setDataSource(this.dataSource);
           System.out.println("Data source setted up");

           Collection col = getList("Expense",lastSyncDate,operation,syncDatePrKeyField);
           System.out.println("Got collection of ids " + col);
           Object ids[] = col.toArray();
           HashMap hm = new HashMap();
           int size = ids.length;
           System.out.println("All transformations done");

           for(int i=0;i<size;i++)
           {
             System.out.println("Taking " + i + " id");
             hm = (HashMap)ids[i];
             System.out.println("HashMap is " + hm);
             EVO = expenseL.getExpenseVO(((Long)hm.get(syncDatePrKeyField[0])).intValue(),indId);
             System.out.println("Successfully got EVO!!");
             expenses.add(EVO);
           }
         }catch(Exception e)
         {
           logger.debug("Error in getting ExpenseVos : "+e);
           e.printStackTrace();
         }
         return expenses;
       }

       /**
        * Method for adding expenses on CV side
        * @param EVOs ArrayList list with expenses
        * @param indId int individual id
        * @return boolean status of operation
        */
       public boolean addExpenses(ArrayList EVOs,int indId)
       {
         ExpenseVO evo = null;
         AccountFacadeLocal expenseL = null;

         try
         {
           InitialContext ic = CVUtility.getInitialContext();
           AccountFacadeLocalHome home = (AccountFacadeLocalHome)ic.lookup("local/AccountFacade");
           expenseL = home.create();
           expenseL.setDataSource(this.dataSource);

           int size = EVOs.size();

           for ( int i=0;i<size;i++)
           {
             evo = (ExpenseVO) EVOs.get(i);
             if (evo.getExpenseDescription() == null){
               evo.setExpenseDescription("");
             }
             evo = expenseL.createExpense(evo,indId);
             logger.info("Added " + evo.getExpenseID() + " expense record");
           }
         }catch(Exception e)
         {
           logger.debug("Error in adding Expenses : "+e);
           e.printStackTrace();
           return false;
         }
         return true;
       }

       /**
        * Method for deleting expenses from CV side
        * @param modulename String module name
        * @param arl ArrayList list with expenses
        * @param indId int individual id
        */
       public void deleteExpenses(String modulename, ArrayList arl, int indId)
       {
         ExpenseVO ivo = new ExpenseVO();
         String extId = "";
         int intId = 0;

         Iterator it = arl.iterator();

         AccountFacadeLocal helperL = null;
         try
         {
           InitialContext ic = CVUtility.getInitialContext();
           AccountFacadeLocalHome home = (AccountFacadeLocalHome ) ic.lookup("local/AccountFacade");
           helperL = home.create();

           while(it.hasNext())
           {
             ivo = (ExpenseVO)it.next();
             extId = ivo.getExternalID();
             intId = this.getCVidForExtid(modulename,extId);
             helperL.deleteInvoice(indId, intId);
             logger.info("Expense " + intId +" object deleted");
           }

         }catch(Exception e)
         {
           logger.debug("Error in deleting Expense : "+e);
           e.printStackTrace();
         }
       }

       /**
        * Method for getting payments from CV side
        * @param params HashMap operation type + last syncronization date
        * @return ArrayList list with payments
        */
       public ArrayList getPayments(HashMap params, int indId)
       {
         PaymentVO paymentVO = null;
         ArrayList payments = new ArrayList();
         AccountFacadeLocal paymentL = null;

         try
         {
           String syncDatePrKeyField[] = {""};
           Timestamp lastSyncDate = (Timestamp)params.get("lastSyncDate");
           String operation = (String)params.get("Operation");
           InitialContext ic = CVUtility.getInitialContext();
           AccountFacadeLocalHome home = (AccountFacadeLocalHome)ic.lookup("local/AccountFacade");
           paymentL = home.create();
           paymentL.setDataSource(this.dataSource);

           Collection col = getList("Payment",lastSyncDate,operation,syncDatePrKeyField);
           Object ids[] = col.toArray();
           HashMap hm = new HashMap();
           int size = ids.length;

           for(int i=0;i<size;i++)
           {
             hm = (HashMap)ids[i];
             paymentVO = paymentL.getPaymentVO(((Long)hm.get(syncDatePrKeyField[0])).intValue(),indId);
             payments.add(paymentVO);
           }
         }catch(Exception e)
         {
           logger.debug("Error in getting PaymentVos : "+e);
           e.printStackTrace();
         }
         return payments;
       }// end of getPaymentForm

       /**
        * Method for adding payments to CV side
        * @param PVOs ArrayList list with payments
        * @param indId int individual id
        * @return boolean status of operation
        */
       public boolean addPayments(ArrayList PVOs, int indId)
       {
         PaymentVO pvo = null;
         AccountFacadeLocal paymentL = null;

         try
         {
           InitialContext ic = CVUtility.getInitialContext();
           AccountFacadeLocalHome home = (AccountFacadeLocalHome)ic.lookup("local/AccountFacade");
           paymentL = home.create();
           paymentL.setDataSource(this.dataSource);

           int size = PVOs.size();

           for ( int i=0;i<size;i++)
           {
             pvo = (PaymentVO) PVOs.get(i);
             pvo = paymentL.createPayment(pvo,indId);
             logger.info("Added " + pvo.getPaymentID() + " payment record");
           }
         }catch(Exception e)
         {
           logger.debug("Error in adding Payments : "+e);
           e.printStackTrace();
           return false;
         }
         return true;
       }

       /**
        * Method for deleting payments from CV side
        * @param modulename String module name
        * @param arl ArrayList list with payments
        * @param indId int individual id
        */
       public void deletePayments(String modulename, ArrayList arl, int indId)
       {
         PaymentVO ivo = new PaymentVO();
         String extId = "";
         int intId = 0;

         Iterator it = arl.iterator();

         AccountFacadeLocal helperL = null;
         try
         {
           InitialContext ic = CVUtility.getInitialContext();
           AccountFacadeLocalHome home = (AccountFacadeLocalHome ) ic.lookup("local/AccountFacade");
           helperL = home.create();

           while(it.hasNext())
           {
             ivo = (PaymentVO)it.next();
             extId = ivo.getExternalID();
             intId = this.getCVidForExtid(modulename,extId);
             helperL.deleteInvoice(indId, intId);
             logger.info("Payment " + intId +" object deleted");
           }

         }catch(Exception e)
         {
           System.out.println("Error in deleting Payment : "+e);
           e.printStackTrace();
         }
       }

       /**
        * Method for getting payment methods from CV side
        * @param params HashMap some params
        * @return ArrayList with payment methods
        */
       public ArrayList getPaymentMethods(HashMap params)
       {
         Vector pmethodsArl = new Vector();
         ArrayList arl = new ArrayList();
         AccountHelperLocal helperL = null;
         try
         {
           InitialContext ic = CVUtility.getInitialContext();
           AccountHelperLocalHome home = (AccountHelperLocalHome ) ic.lookup("local/AccountHelper");
           helperL = home.create();
           pmethodsArl = helperL.getNewPaymentMethods();

           Iterator it = pmethodsArl.iterator();
           while(it.hasNext())
           {
             arl.add(it.next());
           }

         }catch(Exception e)
         {
           logger.debug("Error in getting Accounting terms : "+e);
           e.printStackTrace();
         }
         return arl;
       }

       /**
        * Method for adding payment methods to CV side
        * @param PVOs ArrayList with payment methods
        * @param indId int individual id
        * @return boolean status of operation
        */
       public boolean addPaymentMethods(ArrayList PVOs, int indId)
       {
         PaymentMethodVO pvo = null;
         AccountHelperLocal helperL  = null;
         try
         {
           InitialContext ic = CVUtility.getInitialContext();
           AccountHelperLocalHome home = (AccountHelperLocalHome ) ic.lookup("local/AccountHelper");
           helperL = home.create();

           int size = PVOs.size();
           int cvid = 0;

           for ( int i=0;i<size;i++)
           {
             pvo = (PaymentMethodVO)PVOs.get(i);
             cvid = this.getCVidForExtid("PaymentMethod",pvo.getExternalID());
             if (cvid == 0){
               helperL.savePaymentMethod(pvo);
             }
             logger.info("Added " + pvo.getTitle() + " paymentmethod record");
           }
         }catch(Exception e)
         {
           logger.debug("Error in adding PaymentMethodss : "+e);
           e.printStackTrace();
           return false;
         }
         return true;
       }

       /**
        * Mrthod for deleting payment methods from CV side
        * @param modulename String module name
        * @param arl ArrayList list of payment methods
        */
       public void deletePaymentMethods(String modulename, ArrayList arl)
       {
         PaymentMethodVO pvo = new PaymentMethodVO();
         ArrayList ids = new ArrayList();
         String delIds = "";

         Iterator it = arl.iterator();
         while(it.hasNext())
         {
           pvo = (PaymentMethodVO)it.next();
           ids.add(new Integer(pvo.getExternalID()));
         }

         AccountHelperLocal helperL = null;
         try
         {
           InitialContext ic = CVUtility.getInitialContext();
           AccountHelperLocalHome home = (AccountHelperLocalHome ) ic.lookup("local/AccountHelper");
           helperL = home.create();
           delIds = helperL.deleteObjects(modulename, ids);
           logger.info("PaymentMethods " + delIds +" objects deleted");
         }catch(Exception e)
         {
           logger.debug("Error in deleting PaymentMethod : "+e);
           e.printStackTrace();
         }
       }

       /**
        * Method for adding glaccounts to CV side
        * @param PVOs ArrayList list with glaccounts
        * @param indId int individual id
        * @return boolean status of operation
        */
       public boolean addGLAccounts(ArrayList PVOs, int indId)
       {
         GLAccountVO avo = null;
         AccountHelperLocal helperL  = null;
         try
         {
           InitialContext ic = CVUtility.getInitialContext();
           AccountHelperLocalHome home = (AccountHelperLocalHome ) ic.lookup("local/AccountHelper");
           helperL = home.create();

           int size = PVOs.size();
           int cvid = 0;

           for ( int i=0;i<size;i++)
           {
             avo = (GLAccountVO)PVOs.get(i);
             cvid = this.getCVidForExtid("GLAccount",avo.getExternalID());
             if (cvid == 0){
               helperL.addGLAccount(avo);
             }
             logger.info("Added " + avo.getTitle() + " glaccount record");
           }
         }catch(Exception e)
         {
           logger.debug("Error in adding GLAccounts : "+e);
           e.printStackTrace();
           return false;
         }
         return true;
       }

       /**
        * Method for getting glaccounts from CV side
        * @param params HashMap some params
        * @return ArrayList list with glaccounts
        */
       public ArrayList getGLAccounts(HashMap params)
       {
         Vector glaArl = new Vector();
         ArrayList arl = new ArrayList();
         AccountHelperLocal helperL = null;
         try
         {
           InitialContext ic = CVUtility.getInitialContext();
           AccountHelperLocalHome home = (AccountHelperLocalHome ) ic.lookup("local/AccountHelper");
           helperL = home.create();
           glaArl = helperL.getNewGLAccounts();

           Iterator it = glaArl.iterator();
           while(it.hasNext())
           {
             arl.add(it.next());
           }


         }catch(Exception e)
         {
           logger.debug("Error in getting GL Accounts : "+e);
           e.printStackTrace();
         }
         return arl;
       }

       /**
        * Method to set data source
        * @param ds String with data source
        */
       public void setDataSource(String ds) {
         this.dataSource = ds;
       }

       /**
        * Method for deleting glaccounts from CV side
        * @param modulename String
        * @param arl ArrayList
        */
       public void deleteGLAccounts(String modulename, ArrayList arl)
       {
         GLAccountVO pvo = new GLAccountVO();
         ArrayList ids = new ArrayList();
         String delIds = "";

         Iterator it = arl.iterator();
         while(it.hasNext())
         {
           pvo = (GLAccountVO)it.next();
           ids.add(new Integer(pvo.getExternalID()));
         }

         AccountHelperLocal helperL = null;
         try
         {
           InitialContext ic = CVUtility.getInitialContext();
           AccountHelperLocalHome home = (AccountHelperLocalHome ) ic.lookup("local/AccountHelper");
           helperL = home.create();
           delIds = helperL.deleteObjects(modulename, ids);
           logger.info("GLAccount " + delIds +" objects deleted");
         }catch(Exception e)
         {
           logger.debug("Error in deleting GL Accounts : "+e);
           e.printStackTrace();
         }
       }
}// end of class
