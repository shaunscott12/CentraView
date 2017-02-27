/*
 * $RCSfile: PrintTemplate.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:46 $ - $Author: mking_cv $
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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.ejb.EJBObject;

import com.centraview.mail.MailMessageVO;

/**
 * This is  remote interface which is called from client
 */
public interface PrintTemplate extends EJBObject
{
  public void addCategory(String catName,int artifactId,int parentId,String catType) throws RemoteException;
  public int addPrintTemplate(int ptCategoryId,String ptData,String isDefault,String ptname,int userid,int artifactid,String pttype,String ptsubject) throws RemoteException;
  public PrintTemplateVO getPrintTemplate(int ptDetailId) throws RemoteException, PrintTemplateException;
  public void updatePrintTemplate(int ptDetailId,String ptData,int userid,String ptname,String ptsubject) throws RemoteException;
  public Collection getAllCategories(String categoryType,String artifactName) throws RemoteException;
  public PrintTemplateVO getDefaultPrintTemplate(int individualId, int categoryId) throws PrintTemplateException, RemoteException;
  public Collection getallPrintTemplate(int individualId, int categoryId) throws PrintTemplateException, RemoteException;
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
  public Collection getContactsForEntity(Collection entityIds, boolean primary) throws RemoteException;
  public String sendPTEmail(int from, int accountID, ArrayList to, String subject,ArrayList content, MailMessageVO messageVO)throws RemoteException;
  public void deletePT(int ptDetailId)throws RemoteException;
  public void setDataSource(String ds) throws RemoteException;
  public HashMap getSearchResult(String advanceSearchQuery, String moduleName) throws RemoteException;
  /**
   * Gets all the category ids and names (from the ptcategory table) stuffs them
   * in a labelvaluebean and returns them in a collection
   * @return Collection of LabelValueBean objects containing the template
   *         category information.
   */
  public Collection getCategories() throws RemoteException;
  public boolean updateTemplate(PrintTemplateVO templateVO) throws RemoteException;
  public int newTemplate(PrintTemplateVO templateVO) throws RemoteException;
}