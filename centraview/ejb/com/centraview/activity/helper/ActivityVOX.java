/*
 * $RCSfile: ActivityVOX.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:37 $ - $Author: mking_cv $
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


package com.centraview.activity.helper;

import org.apache.struts.action.ActionForm;

import com.centraview.activity.ActivityForm;

public class ActivityVOX extends ActivityVO
{
  /**
   * Constructor.
   * @param form    Struts ActionForm object holding the form values to be populated in this VOX.
   * @return  ActivityVOX
   */
  public ActivityVOX(ActionForm form)
  {
    ActivityForm activityForm = (ActivityForm)form;
    ActivityGenericFillVOX agf = new ActivityGenericFillVOX();
    agf.fillBasic(this, activityForm);
  }
  /**
   * Returns an ActivityVO representation of this VOX object.
   * @return ActivityVO ojbect representation of this VOX
   */
  public ActivityVO getVO()
  {
    return super.getVO();
  } // end getVO() method
}