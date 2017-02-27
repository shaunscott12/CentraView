/*
 * $RCSfile: PrintTemplateEJB.java,v $    $Revision: 1.4 $  $Date: 2005/10/24 13:38:45 $ - $Author: mcallist $
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

package com.centraview.printtemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.mail.SendFailedException;
import javax.mail.internet.InternetAddress;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;

import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.common.EJBUtil;
import com.centraview.mail.MailAccountVO;
import com.centraview.mail.MailLocal;
import com.centraview.mail.MailLocalHome;
import com.centraview.mail.MailMessageVO;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

/**
 * PrintTemplateEJB.java
 *
 * This is the EJB class for Print Templates
 * The Logic for methods defined in Remote interface
 * is defined in this class
 *
 * This Ejb is Staleless SessionBean
 * Used for Create, Update Print Template.
 */
public class PrintTemplateEJB implements SessionBean
{
  private static Logger logger = Logger.getLogger(PrintTemplateEJB.class);
  private String dataSource = "";
  protected javax.ejb.SessionContext ctx;
  protected Context environment;

  public void ejbCreate() {}
  public void ejbRemove() {}
  public void ejbActivate() {}
  public void ejbPassivate() {}
  public void setSessionContext(SessionContext ctx)
  {
    this.ctx = ctx;
  }

  /**
   * This method is for add category and it's member are inserted into the DataBase.
   *
   * @param   catName
   * @param   artifactId
   * @param  parentId
   * @return  none
   * @exception   None
   */
  public void addCategory(String catName, int artifactId, int parentId,
    String catType)
  {
    try
    {
      CVDal cvdl = new CVDal(this.dataSource);
      cvdl.setSql("pt.addcategory");

      if (catName.length() != 0)
      {
        cvdl.setString(1, catName);
      }
      else
      {
        cvdl.setString(1, "");
      }

      if (artifactId != 0)
      {
        cvdl.setInt(2, artifactId);
      }
      else
      {
        cvdl.setInt(2, 0);
      }

      if (parentId != 0)
      {
        cvdl.setInt(3, parentId);
      }
      else
      {
        cvdl.setInt(3, 0);
      }

      if (catType.length() != 0)
      {
        cvdl.setString(4, catType);
      }
      else
      {
        cvdl.setString(4, "");
      }

      cvdl.executeUpdate();
      cvdl.clearParameters();
      cvdl.destroy();
      cvdl = null;
    }
    catch (Exception e)
    {
      System.out.println("[Exception] PrintTemplateEJB.addCategory: " + e.toString());
    }
  } // end of addCategory

  /**
   * This method is for add printtemplate and it's member are inserted into the DataBase.
   *
   * @param   ptCategoryId
   * @param   ptData
   * @param  isDefault
   * @param   nvo
   * @return  none
   * @exception   None
   */
  public int addPrintTemplate(int ptCategoryId, String ptData,
    String isDefault, String ptname, int userid, int artifactid, String pttype,
    String ptsubject)
  {
	int templateID = -1;
    CVDal cvdl = new CVDal(this.dataSource);
    try
    {
      cvdl.setSql("pt.addprinttemplate");

      if (ptCategoryId != 0)
      {
        cvdl.setInt(1, ptCategoryId);
      }
      else
      {
        cvdl.setInt(1, 0);
      }

      if (isDefault.length() != 0)
      {
        cvdl.setString(2, isDefault);
      }
      else
      {
        cvdl.setString(2, "");
      }

      if (ptData.length() != 0)
      {
        cvdl.setString(3, ptData);
      }
      else
      {
        cvdl.setString(3, "");
      }

      if (ptname.length() != 0)
      {
        cvdl.setString(4, ptname);
      }
      else
      {
        cvdl.setString(4, "");
      }

      if (userid != 0)
      {
        cvdl.setInt(5, userid);
      }
      else
      {
        cvdl.setInt(5, 0);
      }

      if (artifactid != 0)
      {
        cvdl.setInt(6, artifactid);
      }
      else
      {
        cvdl.setInt(6, 0);
      }

      if (pttype.length() != 0)
      {
        cvdl.setString(7, pttype);
      }
      else
      {
        cvdl.setString(7, "");
      }

      if (ptsubject.length() != 0)
      {
        cvdl.setString(8, ptsubject);
      }
      else
      {
        cvdl.setString(8, "");
      }

      cvdl.executeUpdate();
      templateID = cvdl.getAutoGeneratedKey();
      cvdl.clearParameters();
    }
    catch (Exception e)
    {
      System.out.println("[Exception] PrintTemplateEJB.addPrintTemplate: " + e.toString());
      //e.printStackTrace();
    }
    finally
    {
      cvdl.destroy();
      cvdl = null;
    }
    return templateID;
  } // end of addPrintTemplate

  /**
   * This method is for get printtemplate and it's member are inserted into the DataBase.
   *
   * @param   ptDetailId
   * @param   ptData
   * @param  isDefault
   * @return  ptvo
   * @exception PrintTemplateException
   */
  public PrintTemplateVO getPrintTemplate(int ptDetailId) throws PrintTemplateException
  {
    PrintTemplateVO ptvo = new PrintTemplateVO();
    CVDal cvdl = new CVDal(this.dataSource);
    try
    {
      cvdl.setSql("pt.getprinttemplate");
      cvdl.setInt(1, ptDetailId);
      Collection col = cvdl.executeQuery();
      if (col == null)
      {
        throw new PrintTemplateException(PrintTemplateException.GET_FAILED, 
            "Could not find PrintTemplate : " + ptDetailId);
      }
      Iterator it = col.iterator();
      if (!it.hasNext())
      {
        throw new PrintTemplateException(PrintTemplateException.GET_FAILED,
          "Could not find PrintTemplate : " + ptDetailId);
      }
      HashMap hm = (HashMap) it.next();
      ptvo.setPtcategoryId(((Number)hm.get("ptcategoryid")).intValue());
      ptvo.setIsDefault((String)hm.get("isdefault"));
      ptvo.setPtData((String)hm.get("ptdata"));
      ptvo.setPtname((String)hm.get("ptname"));
      ptvo.setUserid(((Number)hm.get("userid")).intValue());
      ptvo.setArtifactId(((Number)hm.get("artifactid")).intValue());
      ptvo.setPtsubject((String)hm.get("ptsubject"));
      ptvo.setPtdetailId(ptDetailId);
    }catch (Exception e){
      logger.error("[getPrintTemplate] Exception thrown.", e);
    }finally{
      cvdl.destroy();
      cvdl = null;
    }
    return ptvo;
  }

  /**
   * This method is for update printtemplate and it's member are inserted into the DataBase.
   *
   * @param   ptDetailId
   * @param   ptData
   * @return  none
   * @exception none
   */
  public void updatePrintTemplate(int ptDetailId, String ptData, int userid,
    String ptname, String ptsubject)
  {
    CVDal cvdl = new CVDal(this.dataSource);
    try
    {
      cvdl.setSql("pt.updateprinttemplate");
      cvdl.setString(1, ptData);
      cvdl.setInt(2, userid);
      cvdl.setString(3, ptname);
      cvdl.setString(4, ptsubject);
      cvdl.setInt(5, ptDetailId);
      cvdl.executeUpdate();
      cvdl.clearParameters();

    }
    catch (Exception e)
    {
      System.out.println("[Exception] PrintTemplateEJB.updatePrintTemplate: " + e.toString());
      //e.printStackTrace();
    }
    finally
    {
      cvdl.destroy();
      cvdl = null;
    }
  }

  /**
   * This method is for getting all categories.
   * @param   categoryType
   * @param   artifactName
   * @return  vec
   * @exception none
   */
  public Collection getAllCategories(String categoryType, String artifactName)
  {
    CVDal dl = new CVDal(this.dataSource);
    ArrayList arrlistCategory = new ArrayList();
    try
    {
      dl.setSql("pt.getAllCategories");
      dl.setString(1, artifactName);
      Collection colID = dl.executeQuery();
      Iterator it = colID.iterator();
      int ID = 0;
      String name = "";
      int parent = 0;
      while (it.hasNext())
      {
        PrintTemplateVO ptVO = new PrintTemplateVO();
        HashMap hm = (HashMap) it.next();
        ID = ((Long) hm.get("ptcategoryid")).intValue();
        name = (String) hm.get("catname");
        parent = ((Long) hm.get("parentid")).intValue();
        ptVO.setCatName(name);
        ptVO.setPtcategoryId(ID);
        ptVO.setParentId(parent);
        arrlistCategory.add(ptVO);
      }
    } catch (Exception e) {
      logger.error("[getAllCategories] Exception thrown.", e);
    } finally {
      dl.destroy();
      dl = null;
    }
    return arrlistCategory;
  }

  /**
   * This method is for getting  Default Print Template by Artifact Name.
   * @param   individualId
   * @param   artifactName
   * @return  none
   * @exception none
   */
  public PrintTemplateVO getDefaultPrintTemplate(int individualId, int categoryId) throws PrintTemplateException
  {
    PrintTemplateVO ptvo = new PrintTemplateVO();
    CVDal cvdl = new CVDal(this.dataSource);
    try
    {
      // Gets the 1st one returned by the DB that isDefault = 'YES' for the selected
      // CategoryId.
      // select
      // a.ptdetailid,a.ptcategoryid,a.isdefault,a.ptdata,a.ptname,b.artifactid
      // from ptdetail where isdefault='YES' and a.ptCategoryId=? LIMIT 1
      cvdl.setSql("pt.getdefaultprinttemplate");
      cvdl.setInt(1, categoryId);
      Collection col = cvdl.executeQuery();
      if (col == null)
      {
        throw new PrintTemplateException(PrintTemplateException.GET_FAILED, "Could not find PrintTemplate : " + individualId);
      }
      Iterator it = col.iterator();
      if (!it.hasNext())
      {
		 return ptvo;
      }
      HashMap hm = (HashMap)it.next();
      ptvo.setPtdetailId(((Number)hm.get("ptdetailid")).intValue());
      ptvo.setPtcategoryId(categoryId);
      ptvo.setIsDefault((String) hm.get("isdefault"));
      ptvo.setPtData((String) hm.get("ptdata"));
      ptvo.setPtname((String) hm.get("ptname"));
      ptvo.setUserid(individualId);
      ptvo.setPtsubject((String)hm.get("ptsubject"));
      ptvo.setPtcategoryId(((Number)hm.get("ptcategoryid")).intValue());
    }catch(Exception e){
      logger.error("[getDefaultPrintTemplate] Exception thrown.", e);
      throw new EJBException(e);
    }finally{
      cvdl.destroy();
      cvdl = null;
    }
    return ptvo;
  }

  /**
   * This method is for getting all print template.
   * @param   individualId
   * @param   artifactName
   * @return  arraylist
   * @exception none
   */
  public Collection getallPrintTemplate(int individualId, int categoryId)
  {
    CVDal dl = new CVDal(this.dataSource);
    Collection list = new ArrayList();
    try
    {
      dl.setSql("pt.getAllPrintTemplate");
      dl.setInt(1, categoryId);
      Collection colID = dl.executeQuery();
      Iterator it = colID.iterator();
      int ID = 0;
      String name = "";
      while (it.hasNext())
      {
        PrintTemplateVO ptVO = new PrintTemplateVO();
        HashMap hm = (HashMap)it.next();
        name = (String)hm.get("ptname");
        Number detailId = (Number)hm.get("ptdetailid");
        list.add(new LabelValueBean(name, detailId.toString()));
      }
    } catch (Exception e) {
      logger.error("[getallPrintTemplate] Exception thrown.", e);
    } finally {
      dl.destroy();
      dl = null;
    }
    return list;
  }

  public HashMap getSearchResult(String advanceSearchQuery, String moduleName)
  {
    HashMap contactSearch = new HashMap();
    CVDal cvdl = new CVDal(this.dataSource);
    try
    {
      cvdl.setSqlQuery(advanceSearchQuery);
      Collection col = cvdl.executeQuery();
      Iterator it = col.iterator();
      if (col != null)
      {
        while (it.hasNext())
        {
          HashMap hm = (HashMap) it.next();
          if (moduleName.equals("entity"))
          {
            int entityId = ((Number) hm.get("EntityID")).intValue();
            contactSearch.put(entityId+"",entityId+"");
          } else {
            int individualId = ((Number) hm.get("IndividualID")).intValue();
            contactSearch.put(individualId+"",individualId+"");
          }
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("[Exception] PrintTemplateEJB.getSearchResult: " + e.toString());
      e.printStackTrace();
    }
    finally
    {
      cvdl.destroy();
      cvdl = null;
    }
    return contactSearch;
  }

  /**
   * Takes a collection of EntityIds and returns a collection of
   * IndividualIds related to those Entities.   Also takes a boolean
   * if primary = true only returns the individuals that are the primary
   * Contact for the Entity otherwise returns all related individuals.
   * @param entityIds A collection of entity ids.
   * @param primary boolean
   * @return Collection of individualIds related to the entities identified
   * by the Ids passed in.
   */
  public Collection getContactsForEntity(Collection entityIds, boolean primary)
  {
    ArrayList primaryContacts = new ArrayList();
    CVDal cvdl = new CVDal(this.dataSource);
    try
    {
      StringBuffer selectQuery = new StringBuffer();
      selectQuery.append("SELECT individualId FROM individual, entity "
          + "WHERE individual.entity = entity.entityid ");
      if (primary)
      {
        selectQuery.append("AND individual.primaryContact = 'YES' ");
      }
      selectQuery.append("AND entity.entityid in (");
      Iterator entityIterator = entityIds.iterator();
      while (entityIterator.hasNext())
      {
        selectQuery.append(entityIterator.next());
        if (entityIterator.hasNext())
        {
          selectQuery.append(", ");
        }
      }
      selectQuery.append(")");

      cvdl.setSqlQuery(selectQuery.toString());
      Collection results = cvdl.executeQuery();
      Iterator resultsIterator = results.iterator();
      while(resultsIterator.hasNext())
      {
        HashMap resultMap = (HashMap)resultsIterator.next();
        primaryContacts.add(resultMap.get("individualId"));
      }
    }
    catch (Exception e)
    {
      System.out.println("[Exception] PrintTemplateEJB.getPrimaryContactForEntity: " + e.toString());
    }
    finally
    {
      cvdl.destroy();
      cvdl = null;
    }
    return primaryContacts;
  } // end getContactsForEntity()

  public String sendPTEmail(int individualId, int accountID, ArrayList to, String subject, ArrayList content, MailMessageVO messageVO)
  {
    String errorMessage = "";
    try {
      InitialContext ic = CVUtility.getInitialContext();
      MailLocalHome home = (MailLocalHome)ic.lookup("local/Mail");
      MailLocal remote = home.create();
      remote.setDataSource(dataSource);

      messageVO.setEmailAccountID(accountID);

      MailAccountVO accountVO = remote.getMailAccountVO(accountID);
      if (accountVO == null) {
        accountVO = new MailAccountVO();
      }
      if (accountVO.getAccountID() != -1) {
        InternetAddress fromAddress = new InternetAddress(accountVO.getEmailAddress(), accountVO.getAccountName());
        messageVO.setFromAddress(fromAddress.toString());
        messageVO.setReplyTo(fromAddress.toString());
        messageVO.setSubject(subject);
        messageVO.setHeaders("");
        messageVO.setContentType(MailMessageVO.HTML_TEXT_TYPE);
        messageVO.setReceivedDate(new java.util.Date());
        if (content != null && content.size() != 0) { 
          // content is an arrayList of strings which are the customized messages
          for (int i = 0; i < content.size(); i++) {
            String bodyContent = (String)content.get(i);
            messageVO.setBody(bodyContent);
            // to is an arraylist of corresponding addresses
            String toAddress = (String)to.get(i);
            if (CVUtility.isEmailAddressValid(toAddress)) {
              ArrayList toList = new ArrayList();
              toList.add(toAddress);
              messageVO.setToList(toList);
              try {
                boolean messageSent = remote.sendMessage(accountVO, messageVO);
                if (messageSent) {
                  logger.debug("["+dataSource+"]Successfully Sent Mail Merge to: "+toAddress);
                } else {
                  logger.error("["+dataSource+"]Mail Merge Failed sending message to: "+messageVO.getToList()+", from account: "+accountVO.getAccountID());
                }
              } catch (SendFailedException sfe) {
                logger.debug("["+dataSource+"]Failed sending Mail Merge to: "+toAddress);
                logger.error("[sendPTEmail] Exception thrown.", sfe);
              }
            } else {
              logger.error("["+dataSource+"]Mail Merge skipping invalid address: "+toAddress);
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("[sendPTEmail]["+dataSource+"] Exception thrown.", e);
      throw new EJBException(e);
    }
    return errorMessage;
  }

  public void deletePT(int ptDetailId)
  {
    // Don't allow the defaults to get whacked.
    switch(ptDetailId)
    {
      case 1:
      case 2:
      case 3:
        return;
    }

    CVDal cvdl = new CVDal(this.dataSource);
    try
    {
      cvdl.setSql("pt.deletePt");
      cvdl.setInt(1, ptDetailId);
      cvdl.executeUpdate();
    } catch (Exception e) {
      logger.error("[deletePT] Exception thrown trying to delete template: "+ptDetailId, e);
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
  }

  /**
   * This simply sets the target datasource to be used for DB interaction.
   *
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
   public void setDataSource(String ds)
   {
     this.dataSource = ds;
   }
   
 /**
   * This method will get the list of Print Templates according to the sort, limit and
   * filter in the parameters passed in. 
   * @param individualID
   * @param parameters
   * @return a ValueListVO with the parameters and the list.
   */
  public ValueListVO getTemplateValueList(int individualID, ValueListParameters parameters)
   {
     ArrayList list = new ArrayList();
     CVDal cvdl = new CVDal(this.dataSource);
     try 
     {
       int numberOfRecords = 0;
       // create a temp table for filter if necessary.
       boolean applyFilter = false;
       String filter = parameters.getFilter();
       if (filter != null && filter.length() > 0)
       {
         String str = "CREATE TABLE templatelistfilter " + filter;
         cvdl.setSqlQuery(str);
         numberOfRecords = cvdl.executeUpdate();
         cvdl.setSqlQueryToNull();
         applyFilter = true;
       } else {
         numberOfRecords = EJBUtil.getRowCount(cvdl, "ptDetailId", "ptdetail");
       }
       parameters.setTotalRecords(numberOfRecords);
       // get the list.
       String select = "SELECT ptd.ptDetailId AS ID, ptd.ptName AS Name, ptc.catname AS Category ";
       StringBuffer from = new StringBuffer("FROM ptdetail AS ptd, ptcategory AS ptc ");
       StringBuffer where =  new StringBuffer("WHERE ptc.ptCategoryId = ptd.ptCategoryId ");
       String orderBy = "ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
       String limit = parameters.getLimitParam();

       StringBuffer query = new StringBuffer();
       if (applyFilter) 
       {
         from.append(", templatelistfilter AS tlf");
         where.append("AND ptd.ptDetailId = tlf.ptdetailid ");
       }
       query.append(select);
       query.append(from);
       query.append(where);
       query.append(orderBy);
       query.append(limit);
       cvdl.setSqlQuery(query.toString());
       list = cvdl.executeQueryList(1);
       if (applyFilter)
       {
         cvdl.setSqlQueryToNull();
         cvdl.setSqlQuery("DROP TABLE templatelistfilter");
         cvdl.executeUpdate();
       }
     } finally {
       cvdl.destroy();
       cvdl = null;
     }
     return new ValueListVO(list, parameters);
   } // end getTemplateValueList()
  
  /**
   * Gets all the category ids and names (from the ptcategory table) stuffs them
   * in a labelvaluebean and returns them in a collection
   * @return Collection of LabelValueBean objects containing the template
   *         category information.
   */
  public Collection getCategories()
  {
    ArrayList categories = new ArrayList();
    CVDal cvdl = new CVDal(this.dataSource);
    try 
    {
      cvdl.setSql("template.getCategories");
      Collection results = cvdl.executeQuery();
      Iterator it = results.iterator();
      while (it.hasNext())
      {
        HashMap category = (HashMap)it.next();
        String name = (String)category.get("name");
        Number id = (Number)category.get("id");
        categories.add(new LabelValueBean(name, String.valueOf(id)));
      }
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
    return categories;
  }
  
  public int newTemplate(PrintTemplateVO templateVO)
  {
    int createdId = 0;
    CVDal cvdl = new CVDal(this.dataSource);
    try
    {
      cvdl.setSql("pt.addprinttemplate");
      // INSERT INTO ptdetail
      // (ptcategoryid,isdefault,ptdata,ptname,userid,artifactid,pttype,ptsubject) 
      // VALUES (?,?,?,?,?,?,?,?)")
      cvdl.setInt(1, templateVO.getPtcategoryId());
      cvdl.setString(3, templateVO.getPtData());
      cvdl.setString(4, templateVO.getPtname());
      cvdl.setInt(5, templateVO.getUserid());
      // I think the following fields are not used.
      cvdl.setString(2, "YES");
      cvdl.setInt(6, 2); // default to artifact 2, entity.
      // boy that wasn't thought through at all, good job.
      cvdl.setString(7, "UNUSED");
      cvdl.setString(8, templateVO.getPtsubject());
      cvdl.executeUpdate();
      createdId = cvdl.getAutoGeneratedKey();
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
    return createdId;
  } // end newTemplate(...)
  
  public boolean updateTemplate(PrintTemplateVO templateVO)
  {
    boolean successful = false;
    CVDal cvdl = new CVDal(this.dataSource);
    try 
    {
      cvdl.setSql("pt.updateprinttemplate");
      // update ptdetail set ptdata = ?,userid=?,ptname=?,ptsubject=? where ptdetailid = ? 
      cvdl.setString(1, templateVO.getPtData());
      cvdl.setInt(2, templateVO.getUserid());
      cvdl.setString(3, templateVO.getPtname());
      cvdl.setString(4, templateVO.getPtsubject());
      cvdl.setInt(5, templateVO.getPtdetailId());
      int result = cvdl.executeUpdate();
      successful = (result > 0);
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
    return successful;
  } // end updateTemplate(...)
}
