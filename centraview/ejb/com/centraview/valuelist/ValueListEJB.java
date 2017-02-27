/*
 * $RCSfile: ValueListEJB.java,v $    $Revision: 1.3 $  $Date: 2005/09/07 19:37:18 $ - $Author: mcallist $
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


package com.centraview.valuelist;

import java.lang.reflect.Method;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.centraview.common.CVUtility;

/**
 * The ValueList EJB acts as a dispatcher to parse a simple set of request
 * parameters and route the request to the correct List provider EJB, the list
 * provider does a query which provides all possible fields for that list type,
 * paged appropriately (according to parameters), meaning it only returns the
 * rows requested based on paging, filters and permission.
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class ValueListEJB implements SessionBean
{
  /** The SessionContext of this SessionBean. */
  protected SessionContext ctx;

  /** The JNDI/DataSource name this EJB will be using. */
  private String dataSource;
  private static Logger logger = Logger.getLogger(ValueListEJB.class);

  public void setSessionContext(SessionContext ctx) throws EJBException
  {
    this.ctx = ctx;
  }
  
  public void ejbActivate() throws EJBException{}
  public void ejbRemove() throws EJBException{}
  public void ejbPassivate() throws EJBException{}
  public void ejbCreate() {}

  /**
   * This simply sets the target datasource to be used for DB interaction.
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  }   // end setDataSource() method
  
  /**
   * This method dispatches the work of building the list
   * to the appropriate EJB which will return a list. 
   * @param individualId
   * @param parameters
   * @return
   */
  public ValueListVO getValueList(int individualId, ValueListParameters parameters)
  {
    // TODO: somehow check if listType is a valid list type value
    return this.getEJBValueList(individualId, parameters, parameters.getValueListType());
  }

  /**
   * This method uses reflection to instantiate a localEJB then calls
   * setDataSource and a method to get the value list.  It relies on the 
   * ValueListConstants having the right information for each list type.
   * @param individualId
   * @param parameters
   * @return A ValueListVO representing // TODO: finish javadoc
   */
  private ValueListVO getEJBValueList(int individualId, ValueListParameters parameters, int valueListType)
  {
    ValueListClassMapping mapping = ValueListConstants.listTypeMapping[valueListType];
    String jndiName = mapping.getJndiName();
    String getValueListMethodName = mapping.getMethodName();
    Object list = null;
    InitialContext ic = CVUtility.getInitialContext();
    try 
    {
      Object ejbHome = ic.lookup(jndiName);
      Class ejbHomeClass = ejbHome.getClass();
      Method homeCreateMethod = ejbHomeClass.getMethod("create", null);
      Object ejb = homeCreateMethod.invoke(ejbHome, null);
      Class ejbClass = ejb.getClass();
      Class[] setDataSourceArgType = {String.class};
      Class[] getValueListArgType = {int.class, ValueListParameters.class};
      Method setDataSourceMethod = ejbClass.getMethod("setDataSource", setDataSourceArgType);
      Method getValueListMethod = ejbClass.getMethod(getValueListMethodName, getValueListArgType);
      Object[] setDataSourceArg = {this.dataSource};
      setDataSourceMethod.invoke(ejb,setDataSourceArg);
      Object[] getValueListArg = {new Integer(individualId), parameters};
      list = getValueListMethod.invoke(ejb, getValueListArg);
    }catch(Exception e){
      // Catch all these and just rethrow something, since there is
      // no way to recover from any of these things gracefully.
      logger.error("[getEJBValueList] Exception trying to call: "+getValueListMethodName+" from EJB with JNDI name: "+jndiName, e);
      throw new EJBException(e);
    }
    return (ValueListVO)list;
  }
}   // end ValueListEJB class definition

