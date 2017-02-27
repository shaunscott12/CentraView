/*
 * $RCSfile: RuleCriteriaVO.java,v $    $Revision: 1.2 $  $Date: 2005/06/10 17:52:30 $ - $Author: mking_cv $
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

package com.centraview.mail;

import java.io.Serializable;

/**
 * Represents a single Email Rule Criteria as a Value Object.
 * @since v1.0.12
 */
public class RuleCriteriaVO implements Serializable
{
  /** The ruleID of the email rule this RuleCriteriaVO is associated with. */
  private int ruleID = -1;

  /** The order in which this Criteria must be applied when matchin this rule to a message. */
  private int orderID = -1;

  /** The Expression Type of this Search Criteria. And/Or. */
  private String expressionType = "";

  /** Tells whether this Rule is enabled or disabled. */
  private int fieldID = -1;

  /** Tells whether the message is to be moved when the Rule matches. */
  private int conditionID = -1;

  /** The folderID of the email folder to which matching messages are to be moved. */
  private String value = "";

  // Getter and Setter for RuleID
  public int getRuleID()
  {
    return(this.ruleID);
  }
  public void setRuleID(int newRuleID)
  {
    this.ruleID = newRuleID;
  }

  // Getter and Setter for OrderID
  public int getOrderID()
  {
    return(this.orderID);
  }
  public void setOrderID(int newOrderID)
  {
    this.orderID = newOrderID;
  }

  // Getter and Setter for ExpressionType
  public String getExpressionType()
  {
    return(this.expressionType);
  }
  public void setExpressionType(String newExpressionType)
  {
    if (newExpressionType != null && (newExpressionType.equals("AND") || newExpressionType.equals("OR")))
    {
      this.expressionType = newExpressionType;
    }else{
      this.expressionType = "OR";
    }
  }

  // Getter and Setter for FieldID
  public int getFieldID()
  {
    return(this.fieldID);
  }
  public void setFieldID(int newFieldID)
  {
    this.fieldID = newFieldID;
  }

  // Getter and Setter for ConditionID
  public int getConditionID()
  {
    return(this.conditionID);
  }
  public void setConditionID(int newConditionID)
  {
    this.conditionID = newConditionID;
  }

  // Getter and Setter for Value
  public String getValue()
  {
    return(this.value);
  }
  public void setValue(String newValue)
  {
    this.value = newValue;
  }


  public String toString()
  {
    StringBuffer sb = new StringBuffer("RuleCriteriaVO = [\n");
    sb.append("  ruleID = [" + ruleID + "]\n");
    sb.append("  orderID = [" + orderID + "]\n");
    sb.append("  expressionType = [" + expressionType + "]\n");
    sb.append("  fieldID = [" + fieldID + "]\n");
    sb.append("  conditionID = [" + conditionID + "]\n");
    sb.append("  value = [" + value + "]\n");
    sb.append("]\n");
    return(sb.toString());
  }

}   // end class definition

