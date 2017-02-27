/*
 * $RCSfile: RuleDetails.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:51 $ - $Author: mking_cv $
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
package com.centraview.email;

import java.util.TreeMap;
public class RuleDetails extends TreeMap 
{
  
  String  RuleId;
  String  AccountID;
  String  name;
  String  description;
  String  enabled;
  String  colA[];
  String  colB[];
  String  colC[];
  String  colD[];		   
  String  movemessageto;		   
  String  movemessagetofolder;
  String  markasread;
  String  deletemessage;

  
  public String getRuleID()
  {
    return this.RuleId;
  }
  
   public void setRuleID(String RuleId)
  {
  	this.RuleId = RuleId;
  }

  public String getAccountID()
  {
    return this.AccountID;
  }
  
   public void setAccountID(String AccountID)
  {
  	this.AccountID = AccountID;
  }

  public String getName()
  {
    return this.name;
  }
  
   public void setName(String name)
  {
  	this.name = name;
  }

  public String getDescription()
  {
    return this.description;
  }
  
   public void setDescription(String description)
  {
  	this.description = description;
  }

  public String getEnabled()
  {
    return this.enabled;
  }
  
   public void setEnabled(String enabled)
  {
  	this.enabled = enabled;
  }
  
  public String[] getcolA()
  {
    return this.colA;
  }
  
  public void setcolA(String colA[])
  {
     this.colA = colA;
  }

  public String[] getcolB()
  {
    return this.colB;
  }
  
  public void setcolB(String colB[])
  {
     this.colB = colB;
  }

  public String[] getcolC()
  {
    return this.colC;
  }
  
  public void setcolC(String colC[])
  {
     this.colC = colC;
  }
    public String[] getcolD()
  {
     return this.colD;
  }
  
  public void setcolD(String colD[])
  {
     this.colD = colD;
  }

  
  public String getMovemessageto()
  {
    return this.movemessageto;
  }
  
   public void setMovemessageto(String movemessageto)
  {
  	this.movemessageto = movemessageto;
  }
  
  public String getMovemessagetofolder()
  {
    return this.movemessagetofolder;
  }
  
   public void setMovemessagetofolder(String movemessagetofolder)
  {
  	this.movemessagetofolder = movemessagetofolder;
  }
  
  public String getMarkasread()
  {
    return this.markasread;
  }
  
   public void setMarkasread(String markasread)
  {
  	this.markasread = markasread;
  }

  public String getDeleteMessage()
  {
    return this.deletemessage;
  }
  
   public void setDeleteMessage(String deletemessage)
  {
  	this.deletemessage = deletemessage;
  }
}