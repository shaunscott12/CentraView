/*
 * $RCSfile: MethodOfContactVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:08 $ - $Author: mking_cv $
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

 
package com.centraview.contact.helper;

import java.io.Serializable;

/*
 *  This is MethodOfContact Object which represent the MethodOfContact Data. 
 */
public class MethodOfContactVO implements Serializable
{
  private int mocID;
  //	private String name;
  private String content; //  Contact  Type Content
  private int mocType; // type of moc ie Email,Phone etc.
  private String mocOrder; //The order of the MOC.
  private String isPrimary = "NO";
  private String note; //Note of this Moc		
  private String syncAs; //syncAs of this Moc
  private String mocTypeName = "";		

  private boolean isAdded = true;
  private boolean isUpdated = false;
  private boolean isDelete = false;

  public MethodOfContactVO()
  {
    super();
  }

  public void setIsPrimary(String isPrim)
  {
    this.isPrimary = isPrim;
  }

  public String getIsPrimary()
  {
    return this.isPrimary;
  }

  /**
   *
   *
   * @return    The Content
   */
  public String getContent()
  {
    return this.content;
  }

  /**
   *	Set the Content Data
   *
   * @param   content  
   */
  public void setContent(String content)
  {
    this.content = content;
  }

  /**
   *
   *
   * @return   The Method of Contact ID.  
   */
  public int getMocID()
  {
    return this.mocID;
  }

  /**
   *	Set the Method of Contact ID.
   *
   * @param   mocID  Method of Contact ID.
   */
  public void setMocID(int mocID)
  {
    this.mocID = mocID;
  }

  /**
   *
   *
   * @return   The Method of Contact Order.  
   */
  public String getMocOrder()
  {
    return this.mocOrder;
  } //end of getMocOrder method

  /**
   *	Set the Method of Contact Order.
   *
   * @param   mocOrder  Method of Contact Order.
   */
  public void setMocOrder(String mocOrder)
  {
    this.mocOrder = mocOrder;
  } //end of setMocOrder method

  /**
   *
   *
   * @return The Method of Contact TypeID    
   */
  public int getMocType()
  {
    return this.mocType;
  }

  /**
   *	Set the  Method of Contact TypeID
   *
   * @param   mocType  Method of Contact TypeID
   */
  public void setMocType(int mocType)
  {
    this.mocType = mocType;
  }

  /**
   *
   *
   * @return The note of Method of Contact.    
   */
  public String getNote()
  {
    return this.note;
  }

  /**
   *	Set the Note
   *
   * @param   note  Note
   */
  public void setNote(String note)
  {
    this.note = note;
  }

  /**
   *
   *
   * @return  The MethodOfContact  value Object.   
   */
  public MethodOfContactVO getVO()
  {
    return this;
  }

  public boolean isAdded()
  {
    return this.isAdded;
  }

  public void added(boolean isAdded)
  {
    this.isAdded = isAdded;
  }

  public boolean isUpdated()
  {
    return this.isUpdated;
  }

  public void updated(boolean isUpdated)
  {
    this.isUpdated = isUpdated;
  }

  public boolean isDelete()
  {
    return this.isDelete;
  }

  public void delete(boolean isDelete)
  {
    this.isDelete = isDelete;
  }

  public String getSyncAs()
  {
    return this.syncAs;
  }

  public void setSyncAs(String syncAs)
  {
    this.syncAs = syncAs;
  }

  /**
   * Returns a string representing the <b>structure</b> of this
   * object. Over-rides the toString() method of Object so that
   * printing this object into the log file shows useful
   * information. This is especially useful for debugging.
   * @return String representation of this object.
   */
  public String toString()
  {
    StringBuffer toReturn = new StringBuffer("");

    toReturn.append("\n    MethodOfContactVO:\n");
    toReturn.append("     - mocID = " + this.mocID + "\n");
    toReturn.append("     - mocType = " + this.mocType + "\n");
    toReturn.append("     - this.content = " + this.content + "\n");
    toReturn.append("     - mocOrder = " + this.mocOrder + "\n");
    toReturn.append("     - isPrimary = " + this.isPrimary + "\n");
    toReturn.append("     - note = " + this.note + "\n");
    toReturn.append("     - syncAs = " + this.syncAs + "\n");
    toReturn.append("     - isAdded = " + this.isAdded + "\n");
    toReturn.append("     - isUpdated = " + this.isUpdated + "\n");
    toReturn.append("     - isDelete = " + this.isDelete + "\n");

    return (toReturn.toString());
  } // end toString() method

  /**
   * Returns the text label value of the MocType which is stored
   * in the moctype table in the database.
   * The only method known to populate this is ContactHelperEJB.getAllMOCForContact()
   * @author Kevin McAllister <kevin@centraview.com>
   * @return string, the moctype.  it may be "" if it wasn't populated
   */
  public String getMocTypeName()
  {
    return mocTypeName;
  }

  /**
   * Sets the moctype text label, should be based on the value from the
   * database table moctype.  Only
   * ContactHelperEJB.getAllMOCForContact seems to call this at the time
   * of this writing.
   * @author Kevin McAllister <kevin@centraview.com>
   * @param string the moctype label.
   */
  public void setMocTypeName(String string)
  {
    mocTypeName = string;
  }

} // end class definition
