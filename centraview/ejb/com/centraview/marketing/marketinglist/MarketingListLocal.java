/*
 * $RCSfile: MarketingListLocal.java,v $    $Revision: 1.2 $  $Date: 2005/09/08 12:22:24 $ - $Author: mcallist $
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

package com.centraview.marketing.marketinglist;

import java.sql.SQLException;
import java.util.HashMap;

import javax.ejb.EJBLocalObject;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.marketing.EventAtendeesList;
import com.centraview.marketing.EventList;
import com.centraview.marketing.MarketingList;
import com.centraview.marketing.MarketingListMemberList;
import com.centraview.marketing.PromotionList;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

public interface MarketingListLocal extends EJBLocalObject {

  public EventList getEventList(int userId, HashMap info) throws AuthorizationFailedException;

  public PromotionList getPromotionList(int userId, HashMap preference)
      throws AuthorizationFailedException;

  public EventAtendeesList getEventAtendeesList(int userId, HashMap preference)
      throws AuthorizationFailedException;

  public MarketingList getMarketingList(int userId, HashMap info)
      throws AuthorizationFailedException;

  public MarketingListMemberList getMarketingListMemberList(int userID, int marketingListID,
      HashMap preferences) throws AuthorizationFailedException;

  /**
   * @author Kevin McAllister <kevin@centraview.com> Allows the client to set
   *         the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   */
  public void setDataSource(String ds);

  public ValueListVO getLitValueList(int individualID, ValueListParameters parameters);

  public ValueListVO getListManagerValueList(int individualID, ValueListParameters parameters);

  public ValueListVO getPromotionValueList(int individualID, ValueListParameters parameters);

  public ValueListVO getEventValueList(int individualID, ValueListParameters parameters);

  /**
   * Returns a ValueListVO representing a list of Event records, based on the
   * <code>parameters</code> argument which limits results.
   * @throws SQLException
   */
  public ValueListVO getEventAttendeeValueList(int individualID, ValueListParameters parameters);
}
