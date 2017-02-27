/*
 * $RCSfile: MergeHome.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:46 $ - $Author: mking_cv $
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

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * EJB spec Mandated Home Interface for MergeEJB SessionBean.
 * The MergeEJB SessionBean will do all the heavy lifting for
 * the Merge / Purge functionallity.  The methods in here will
 * implement the business logic that populates the SearchCriteriaVO
 * with the right fieldlists.  That in turn, parses and does a search
 * based on the contents of a SearchCriteriaVO.  And returns a MergeSearchResultVO.
 * 
 * Finally based on further selections by the user, Entity or Individual Records
 * will be updated, many many related records will be re-associated with the newly updated
 * (merged) record.  And purged records will be deleted through their
 * standard delete use methods.
 * 
 * The phonetic algortihm searches (soundex, metaphone) will rely on the Jakarta commons
 * codec library. 
 * <http://jakarta.apache.org/commons/codec/>
 * <http://jakarta.apache.org/commons/codec/apidocs/>

 * @author Kevin McAllister <kevin@centraview.com>
 * 
 */
public interface MergeHome extends EJBHome
{
  public Merge create() throws RemoteException, CreateException;
}
