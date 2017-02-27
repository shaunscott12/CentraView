/*
 * $RCSfile: MergeUtil.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:44 $ - $Author: mking_cv $
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

package com.centraview.administration.merge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;

import com.centraview.common.CVUtility;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.contact.entity.EntityVO;
import com.centraview.contact.individual.IndividualVO;

/**
 * This has static methods that can be used multiple places in
 * the Merge functionallity.
 *
 * @author Kevin McAllister <kevin@centraview.com>
 *
 */
public class MergeUtil
{
  private static Logger logger = Logger.getLogger(MergeUtil.class);
  /**
   * Iterates a Collection of LabelValueBeans compares the value of each
   * with the passed in value.  Returns the matching label.
   *
   * @author Kevin McAllister <kevin@centraview.com>
   * @param value the value you want to match
   * @param collection the Collection of LabelValueBeans which should contain the matching value
   * @return the label or null if no match was found.
   */
  static public String getCollectionLabel(String value, Collection collection)
  {
    String label = null;
    Iterator iter = collection.iterator();
    while(iter.hasNext())
    {
      LabelValueBean item = (LabelValueBean)iter.next();
      if (item.getValue().equals(value))
      {
        return item.getLabel();
      }
    } // end for (int i = 0; (label != null) && (i < collection.size()); i++)
    return label;
  } // end getCollectionLabel

  static public ArrayList getAddressVOs(String [] mergeIds, int contactType, String dataSource)
  {
    ArrayList addressVOs = new ArrayList();
    try
    {
      ContactFacadeHome contactFacadeHome = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      ContactFacade remoteContactFacade = contactFacadeHome.create();
      remoteContactFacade.setDataSource(dataSource);
      for (int i = 0; i < mergeIds.length; i++)
      {
        // Get a collection of all addresses for each
        // add that collection to the addressVOs
        ArrayList contactAddressVOs = (ArrayList)remoteContactFacade.getAllAddressVOs(Integer.parseInt(mergeIds[i]), contactType);
        addressVOs.addAll(contactAddressVOs);
      } // end for(int i = 0; i < mergeIds.length; i++)
    }
    catch (Exception e)
    {
      logger.error("[getAddressVOs] Exception thrown.", e);
    }
    return addressVOs;
  } // end getAddressVOs

  static public ArrayList getEntityVOs(String [] mergeIds, String dataSource)
  {
    ArrayList entityVOs = new ArrayList();
    try
    {
      ContactFacadeHome contactFacadeHome = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      ContactFacade remoteContactFacade = contactFacadeHome.create();
      remoteContactFacade.setDataSource(dataSource);
      for (int i = 0; i < mergeIds.length; i++)
      {
        try
        {
          EntityVO entityVO = remoteContactFacade.getEntity(Integer.parseInt(mergeIds[i]));
          entityVOs.add(entityVO);
        } catch (NumberFormatException nfe) {} // only increment if there is actually an entityId
      }
    }
    catch (Exception e)
    {
      logger.error("[getEntityVOs] Exception thrown.", e);
    }
    return entityVOs;
  } // end getEntityVOs

  static public ArrayList getIndividualVOs(String [] mergeIds, String dataSource)
  {
    ArrayList individualVOs = new ArrayList();
    try
    {
      ContactFacadeHome contactFacadeHome = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      ContactFacade remoteContactFacade = contactFacadeHome.create();
      remoteContactFacade.setDataSource(dataSource);
      for (int i = 0; i < mergeIds.length; i++)
      {
        IndividualVO individualVO = remoteContactFacade.getIndividual(Integer.parseInt(mergeIds[i]));
        individualVOs.add(individualVO);
      }
    }
    catch (Exception e)
    {
      logger.error("[getIndividualVOs] Exception thrown.", e);
    }
    return individualVOs;
  } // end getIndividualVOs


  // Get all Individuals for the entities
  static public ArrayList getIndividualVOsForEntityIds(String [] mergeIds, String dataSource)
  {
    ArrayList individualVOs = new ArrayList();
    try
    {
      ContactFacadeHome contactFacadeHome = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      ContactFacade remoteContactFacade = contactFacadeHome.create();
      remoteContactFacade.setDataSource(dataSource);
      for (int i = 0; i < mergeIds.length; i++)
      {
        ArrayList individuals = (ArrayList)remoteContactFacade.getAllIndividualVOs(Integer.parseInt(mergeIds[i]));
        individualVOs.addAll(individuals);
      }
    }
    catch (Exception e)
    {
      logger.error("[getIndividualVOsForEntityIds] Exception thrown.", e);
    }
    return individualVOs;
  } // end getIndividualVOs
} // end MergeUtil
