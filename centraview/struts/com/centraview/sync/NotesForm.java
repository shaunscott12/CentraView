/*
 * $RCSfile: NotesForm.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:29:01 $ - $Author: mking_cv $
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

package com.centraview.sync;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class NotesForm extends ActionForm
{

  private String sessionID = null;
  private int    noteID    = 0;
  private String title     = null;
  private String content   = null;
  private String isPrivate = null;
  private String date      = null;

  /* sessionID */
  public String getSessionID() {
    return this.sessionID;
  }   // end getSessionID()

  public void setSessionID(String sessionID) {
    this.sessionID = sessionID;
  }   // end setSessionID()

  /* noteID */
  public int getNoteID() {
    return this.noteID;
  }   // end getNoteID()

  public void setNoteID(int noteID) {
    this.noteID = noteID;
  }   // end setNoteID()

  /* title */
  public String getTitle() {
    return this.title;
  }   // end getTitle()

  public void setTitle(String title) {
    this.title = title;
  }   // end setTitle()

  /* content */
  public String getContent() {
    return this.content;
  }   // end getContent()

  public void setContent(String content) {
    this.content = content;
  }   // end setContent()

  /* isPrivate */
  public String getIsPrivate() {
    return this.isPrivate;
  }   // end getIsPrivate()

  public void setIsPrivate(String isPrivate) {
    this.isPrivate = isPrivate;
  }   // end setIsPrivate()

  /* date */
  public String getDate() {
    return this.date;
  }   // end getDate()

  public void setDate(String date) {
    this.date = date;
  }   // end setDate()

  public void reset(ActionMapping mapping, HttpServletRequest request) {
    this.sessionID = null;
    this.noteID = 0;
    this.title = null;
    this.content = null;
    this.isPrivate = null;
    this.date = null;
  }   // end reset()
  
}   // end class LookupForm definition
