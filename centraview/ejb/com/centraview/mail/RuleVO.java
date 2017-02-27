/*
 * $RCSfile: RuleVO.java,v $    $Revision: 1.2 $  $Date: 2005/06/10 17:52:30 $ - $Author: mking_cv $
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
import java.util.ArrayList;

/**
 * Represents a single Email Rule as a Value Object.
 * @since v1.0.12
 */
public class RuleVO implements Serializable
{
  /** The ID of the Rule. */
  private int ruleID = -1;

  /** The accountID of the email account this Rule is associated with. */
  private int accountID = -1;

  /** The Name of the Rule. */
  private String ruleName = "";

  /** Short description of this Rule. */
  private String description = "";

  /** Tells whether this Rule is enabled or disabled. */
  private boolean enabled = false;

  /** Tells whether the message is to be moved when the Rule matches. */
  private boolean moveMessage = false;

  /** The folderID of the email folder to which matching messages are to be moved. */
  private int folderID = -1;

  /** Tells whether the message is to be marked read when the Rule matches. */
  private boolean markMessageRead = false;

  /** Tells whether the message is to be deleted when this Rule matches. */
  private boolean deleteMessage = false;

  /** An ArrayList of RuleCriteriaVO object representing the criteria for this Rule. */
  private ArrayList ruleCriteria = new ArrayList();


  // Getter and Setter for RuleID
  public int getRuleID()
  {
    return(this.ruleID);
  }
  public void setRuleID(int newRuleID)
  {
    this.ruleID = newRuleID;
  }

  // Getter and Setter for AccountID
  public int getAccountID()
  {
    return(this.accountID);
  }
  public void setAccountID(int newAccountID)
  {
    this.accountID = newAccountID;
  }

  // Getter and Setter for RuleName
  public String getRuleName()
  {
    return(this.ruleName);
  }
  public void setRuleName(String newRuleName)
  {
    this.ruleName = newRuleName;
  }

  // Getter and Setter for Description
  public String getDescription()
  {
    return(this.description);
  }
  public void setDescription(String newDescription)
  {
    this.description = newDescription;
  }

  // Getter and Setter for Enabled
  public boolean isEnabled()
  {
    return(this.enabled);
  }
  public void setEnabled(boolean newEnabled)
  {
    this.enabled = newEnabled;
  }

  // Getter and Setter for MoveMessage
  public boolean isMoveMessage()
  {
    return(this.moveMessage);
  }
  public void setMoveMessage(boolean newMoveMessage)
  {
    this.moveMessage = newMoveMessage;
  }

  // Getter and Setter for folderID
  public int getFolderID()
  {
    return(this.folderID);
  }
  public void setFolderID(int newFolderID)
  {
    this.folderID = newFolderID;
  }

  // Getter and Setter for markMessageRead
  public boolean isMarkMessageRead()
  {
    return(this.markMessageRead);
  }
  public void setMarkMessageRead(boolean newMarkMessageRead)
  {
    this.markMessageRead = newMarkMessageRead;
  }
  
  // Getter and Setter for DeleteMessage
  public boolean isDeleteMessage()
  {
    return(this.deleteMessage);
  }
  public void setDeleteMessage(boolean newDeleteMessage)
  {
    this.deleteMessage = newDeleteMessage;
  }

  // Getter and Setter for RuleCriteria
  public ArrayList getRuleCriteria()
  {
    return(this.ruleCriteria);
  }
  public void setRuleCriteria(ArrayList newRuleCriteria)
  {
    if (newRuleCriteria == null){ newRuleCriteria = new ArrayList(); }
    this.ruleCriteria = newRuleCriteria;
  }

  /**
   * Adds a single RuleCriteriaVO object to the ruleCriteria
   * ArrayList of this VO.
   * @param criteriaVO The single RuleCriteriaVO object to be added
   *        to the ArrayList of RuleCriteria on this object.
   * @return void
   */
  public void addRuleCriteria(RuleCriteriaVO criteriaVO)
  {
    if (this.ruleCriteria == null)
    {
      this.ruleCriteria = new ArrayList();
    }
    this.ruleCriteria.add(criteriaVO);
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer("RuleVO = [\n");
    sb.append("  ruleID = [" + ruleID + "]\n");
    sb.append("  accountID = [" + accountID + "]\n");
    sb.append("  ruleName = [" + ruleName + "]\n");
    sb.append("  description = [" + description + "]\n");
    sb.append("  enabled = [" + enabled + "]\n");
    sb.append("  moveMessage = [" + moveMessage + "]\n");
    sb.append("  folderID = [" + folderID + "]\n");
    sb.append("  markMessageRead = [" + markMessageRead + "]\n");
    sb.append("  deleteMessage = [" + deleteMessage + "]\n");
    sb.append("  ruleCriteria = [" + ruleCriteria + "]\n");
    sb.append("]\n");
    return(sb.toString());
  }   // end toString() method



}   // end class definition

