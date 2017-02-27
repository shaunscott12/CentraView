/*
 * $RCSfile: PaymentMethodVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:26 $ - $Author: mking_cv $
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


package com.centraview.account.helper;

import java.io.Serializable;

public class PaymentMethodVO implements Serializable
{
  private int methodID;
  private String title;
  private String externalID;


  /**
   *
   *
   */
  public PaymentMethodVO(){}


  /**
   *
   *
   */
  public PaymentMethodVO(int methodID, String title, String externalID)
  {
            this.methodID=methodID;
            this.title=title;
            this.externalID=externalID;
  }


  /**
   *
   *
   * @return
   */
  public String getExternalID()
  {
    return this.externalID;
  }


  /**
   *
   *
   * @param   externalID
   */
  public void setExternalID(String externalID)
  {
    this.externalID = externalID;
  }



  /**
   *
   *
   * @return
   */
  public int getMethodID()
  {
    return this.methodID;
  }


  /**
   *
   *
   * @param   methodID
   */
  public void setMethodID(int methodID)
  {
    this.methodID = methodID;
  }



  /**
   *
   *
   * @return
   */
  public String getTitle()
  {
    return this.title;
  }


  /**
   *
   *
   * @param   title
   */
  public void setTitle(String title)
  {
    this.title = title;
  }

  /**
   *
   *
   * 
   */
  public String toString()
  {
     String str="PaymentMethodVO:\n";
           str+="methodID="+methodID+";\n"+
                "title="+((title==null)?"null":title)+";\n"+
          "externalID="+((externalID==null)?"null":externalID)+";\n";
           return str;
        }

}
