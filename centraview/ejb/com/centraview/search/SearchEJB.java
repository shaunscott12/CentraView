/*
 * $RCSfile: SearchEJB.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:09 $ - $Author: mking_cv $
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


package com.centraview.search;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

import com.centraview.common.CVDal;
import com.centraview.common.MasterDictionary;
import com.centraview.common.TableRelate;

public class SearchEJB implements SessionBean
{
  protected javax.ejb.SessionContext ctx;
  private String dataSource = "";
  private static Logger logger = Logger.getLogger(SearchEJB.class);

  /**
   * This method returns the MasterDictionary Object having relationship between
   * tables and their Fields.
   */
  public  MasterDictionary getMasterDictionary() 
  {
    MasterDictionary mdObj = null;
    CVDal dl = new CVDal(dataSource);
    try
    {
      mdObj = new MasterDictionary();

      dl.setSql("common.getmasterdictionary");
      Collection  col  = (Collection) dl.executeQuery();
      dl.clearParameters();

      Iterator it = col.iterator();
      while (it.hasNext()) 
      {
        HashMap hm	= (HashMap)it.next();

        String table1	= (String) hm.get("table1");
        String table2	= (String) hm.get("table2");
        String field1	= (String) hm.get("field1");
        String field2	= (String) hm.get("field1");
        String clause	= (String) hm.get("clause");
        String otherTable= (String) hm.get("othertablename");

        // Check whether allready Present or Not 
        // if Present then get and add  to it.
        // else put New Object.
        if (mdObj.containsKey(table2))
        {
          HashMap innHMap = (HashMap)mdObj.get(table2);
          Vector vec = (Vector)innHMap.get(table1);
          if (vec == null)
          {
            vec = new Vector();
          } //end of if statement (vec == null)
          vec.add(new TableRelate(table1,field1,field2,clause,otherTable));
          innHMap.put(table1,vec);
          mdObj.put(table2,innHMap);
        } //end of if statement (mdObj.containsKey(table2))
        else
        {
          HashMap  add = new HashMap();
          Vector vec = new Vector();
          vec.add(new TableRelate(table1,field1,field2,clause,otherTable));
          add.put(table1,vec);
          mdObj.put(table2,add);
        } //end of else statement (mdObj.containsKey(table2))
      }// end of while loop (it.hasNext())
    } //end of try block
    catch(Exception e)
    {
      logger.error("[getMasterDictionary] Exception thrown.", e);
    } //end of catch block (Exception)
    finally
    {
      dl.destroy();
      dl = null;
    } //end of finally block
    return mdObj;
  }// end of getMasterDictionary
	
  /**
   * To get all table ids and name from DB
   */	
  public HashMap getTableIdsAndNames()
  {
    HashMap hmp = new HashMap();
    CVDal dl = new CVDal(dataSource);
    try
    {
      dl.setSql("common.gettables");
      Collection  col  = (Collection)dl.executeQuery();
      Iterator it = col.iterator();
      while (it.hasNext()) 
      {
        HashMap hm	= (HashMap)it.next();

        Long lng = (Long)hm.get("tableid");
        String moduleName	= (String)hm.get("name");								

        hmp.put(moduleName,lng);							
      } //end of while loop (it.hasNext())
    } //end of try block
    catch(Exception e)
    {
      logger.error("[getTableIdsAndNames] Exception thrown.", e);
    } //end of catch block (Exception)
    finally
    {
      dl.destroy();
      dl = null;
    } //end of finally block
    return hmp;
  } //end of getTableIdsAndNames method
	
	//added by shilpa ends here.	
	
  public HashMap getFinalMapping()
  {
    HashMap map = new HashMap();
    CVDal dl = new CVDal(dataSource);
    try
    {
      dl.setSql("common.getfinalmapping");

      Collection  col  = (Collection)dl.executeQuery();
      Iterator it = col.iterator();
      while (it.hasNext()) 
      {
        HashMap hm	= (HashMap)it.next();

        String module	= (String)hm.get("module");
        String table	= (String)hm.get("table");								
        String column	= (String)hm.get("column");								

        if (map.containsKey(module)) // if allready exist
        {
          HashMap presentMap = (HashMap)map.get(module);
          if (presentMap.containsKey(table))
          {
            Vector colvec = (Vector)presentMap.get(table);
            colvec.add(column);
            presentMap.put(table,colvec);
            map.put(module,presentMap);
          } //end of if statement (presentMap.containsKey(table))
          else
          {
            Vector colvec = new Vector();
            colvec.add(column);
            presentMap.put(table,colvec);
            map.put(module,presentMap);						
          } //end of else statement (presentMap.containsKey(table))
        } //end of if statement (map.containsKey(module))
        else
        {				
          HashMap tabMap = new HashMap();
          Vector colVec = new Vector();
          colVec.add(column);
          tabMap.put(table,colVec);
          map.put(module,tabMap);
        } //end of else statement (map.containsKey(module))
      }// end of while
    } //end of try block
    catch(Exception e)
    {
      logger.error("[getFinalMapping] Exception thrown.", e);
    } //end of catch block (Exception)
    finally
    {
      dl.destroy();
      dl = null;
    } //end of finally block
    return map;
  } // end of getFinalMapping method
	

  /*
   * Set the associated session context. The container calls this method after the instance
   * creation. The enterprise Bean instance should store the reference to the context
   * object in an instance variable. This method is called with no transaction context.
   */
  public void setSessionContext(SessionContext ctx)
  {
    this.ctx = ctx;
  }


  public SearchEJB()
  {

  }

  /**
   * Called by the container to create a session bean instance. Its parameters typically
   * contain the information the client uses to customize the bean instance for its use.
   * It requires a matching pair in the bean class and its home interface.
   */
  public void ejbCreate()
  {

  }
  /**
   * A container invokes this method before it ends the life of the session object. This
   * happens as a result of a client's invoking a remove operation, or when a container
   * decides to terminate the session object after a timeout. This method is called with
   * no transaction context.
   */
  public void ejbRemove()
  {

  }

  /**
   * The activate method is called when the instance is activated from its 'passive' state.
   * The instance should acquire any resource that it has released earlier in the ejbPassivate()
   * method. This method is called with no transaction context.
   */
  public void ejbActivate()
  {

  }

  /**
   * The passivate method is called before the instance enters the 'passive' state. The
   * instance should release any resources that it can re-acquire later in the ejbActivate()
   * method. After the passivate method completes, the instance must be in a state that
   * allows the container to use the Java Serialization protocol to externalize and store
   * away the instance's state. This method is called with no transaction context.
   */
  public void ejbPassivate()
  {

  }
  /**
 * @author Kevin McAllister <kevin@centraview.com>
 * This simply sets the target datasource to be used for DB interaction
 * @param ds A string that contains the cannonical JNDI name of the datasource
 */
 public void setDataSource(String ds) {
 	this.dataSource = ds;
 }
} //end of SearchEJB class
