/*
 * $RCSfile: CVEntityBean.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:59 $ - $Author: mking_cv $
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


package com.centraview.report.ejb.entity;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;

/**********************************************************************************
*
*	Entity Bean home interface factory.
*
*	@version 1.0
*	@date 12/26/03
*
**********************************************************************************/

public abstract class CVEntityBean implements EntityBean
{
  /**************************************************************************
  *
  *	Constants - Report types values.
  *
  **************************************************************************/

  /** Constant - Standard report type code. */
  public static final int STANDARD_REPORT_CODE = 1;

  /** Constant - Ad Hoc report type code. */
  public static final int ADHOC_REPORT_CODE = 2;

  /** Constant - Custom report type code. */
  public static final int CUSTOM_REPORT_CODE = 3;

  /*************************************************************************************
  *
  *	Helper methods
  *
  *************************************************************************************/

  public String dataSource = "MySqlDS";
  public EntityContext entityContext;

  public void unsetEntityContext()
  {
    this.entityContext = null;
  }

  public void setEntityContext(EntityContext entityContext)
  {
    this.entityContext = entityContext;
  }
}
