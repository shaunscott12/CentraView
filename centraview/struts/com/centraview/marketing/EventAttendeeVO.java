/*
 * $RCSfile: EventAttendeeVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:12 $ - $Author: mking_cv $
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
package com.centraview.marketing;

import java.io.Serializable;
/**
 * Value Object for the Event Attendee Objects.
 * 
 * @author Ryan Grier <ryan@centraview.com>
 */
public class EventAttendeeVO  implements Serializable
{  
  /** Whether or not the user has accepted the event. */
  private String acceptedString;
  
  /** The event ID of the event. */
  private int eventID;
  
  /** The individual ID of the person invited. */
  private int individualID;
  
  /**
   * Returns whether or not the invited has
   * accepted the event.
   * 
   * @return True if the user has accepted the invitation.
   * False, if not.
   */
  public boolean hasAccepted()
  {
    return this.getAcceptedString().equalsIgnoreCase("YES");
  } //end of hasAccepted method
  
  /**
   * Sets whether or not the invited has
   * accepted the event.
   * 
   * @param accepted True if the user has accepted the invitation.
   * False, if not.
   */
  public void setAccepted(boolean accepted)
  {
    if (accepted)
    {
      this.setAcceptedString("YES");
    } //end of if statement (accepted)
    else
    {
      this.setAcceptedString("NO");
    } //end of else statement (accepted)
  } //end of hasAccepted method
  
  /**
     * Returns whether or not the invited has
     * accepted the event.
     * 
     * @return YES if the user has accepted the invitation.
     * NO, if not.
     */
    public String getAcceptedString()
    {
      return this.acceptedString;
    } //end of getAcceptedString method
  
    /**
     * Sets whether or not the invited has
     * accepted the event.
     * 
     * @param accepted YES if the user has accepted the invitation.
     * NO, if not.
     */
    public void setAcceptedString(String acceptedString)
    {
      this.acceptedString = acceptedString;
    } //end of setAcceptedString method
  
  /**
    * Returns the event ID associated with this object.
    * 
    * @return The event ID associated with this object.
    */
   public int getEventID()
   {
     return this.eventID;
   } //end of hasAccepted method
  
   /**
    * Sets the event ID associated with this object.
    * 
    * @param eventID The event ID associated with this object.
    */
   public void setEventID(int eventID)
   {
     this.eventID = eventID;
   } //end of hasAccepted method
   
  /**
    * Returns the Individual ID associated with this object.
    * 
    * @return The Individual ID associated with this object.
    */
   public int getIndividualID()
   {
     return this.individualID;
   } //end of getIndividualID method
  
   /**
    * Sets the Individual ID associated with this object.
    * 
    * @param individualID The Individual ID associated with this object.
    */
   public void setIndividualID(int individualID)
   {
     this.individualID = individualID;
   } //end of setIndividualID method
   
  /**
   * Overrides the Object.toString() method.
   * 
   * @return A String version of the object.
   */
   public String toString()
   {
     StringBuffer stringBuffer = new StringBuffer();
     stringBuffer.append("Individual ID: " + this.getIndividualID());
     stringBuffer.append("Event ID: " + this.getEventID());
     stringBuffer.append("Accepted Stirng: " + this.getAcceptedString());
     stringBuffer.append("Accepted Boolean: " + this.hasAccepted());
     return stringBuffer.toString();
   } //end of toString method
} //end of EventAttendeeVO Class
