/*
 * $RCSfile: MoneyMember.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:13 $ - $Author: mking_cv $
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
package com.centraview.common;

/**
 * The MoneyMember holds Float values.
 *
 * @author
 */
public class MoneyMember extends FloatMember
{

  public MoneyMember (  String  memberType , Float  memberValue, int auth , String requestURL , char displayType ,
            boolean  linkEnabled , int  displayWidth )
  {
             super(memberType, memberValue, auth, requestURL,  displayType,
       linkEnabled, displayWidth);

  }

  /**
   * Returns A String representation of the
   * FloatMember in the following format:
   * ###,###,##0.00 The #'s will not
   * be shown unless there is a value there. If
   * A value is present, the number will be shown.
   *
   * @see java.text.DecimalFormat
   *
   * @return A String representation of the MoneyMember.
   */
  public String getDisplayString()
  {
    return "$ "+super.getDisplayString();
  } //end of getDisplayString method

}


