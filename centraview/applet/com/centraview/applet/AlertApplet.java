/*
 * $RCSfile: AlertApplet.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:19 $ - $Author: mking_cv $
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

package com.centraview.applet;

import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class AlertApplet extends Applet implements Runnable
{
  private Thread theThread = null;
  private Thread alertListUpdater = null;
  
  TextLink textlinkArray[] = null;
  private int currentLink = 0;
  
  private int alertX = 0;
  private int alertY = 0;
  
  private int linkX = 0;
  private int linkY = 0;
  
  private int bgX = 0;
  private int bgY = 0;
  
  private int dismissPadding = 0;
  
  private int timeSleep = 0;
  private int timeUpdate = 0;
  
  private Color textColor = null;
  
  private Image theBackGround = null;
  private Image theAlertIcon = null;
  
  private Font fontAlert = null;
  private Font fontDismiss = null;
  
  private String webServerStr;
  private String servletPath;
  private TextLink dismissText;
  
  boolean isAlertUpdating = false;
  boolean isAlertListEmpty = true;
  
  private String noAlerts = "";
  private String alertUrl = "";
  
  private int alertLength = 0;
  MediaTracker mt;
  
  private class AlertListUpdater implements Runnable
  {
    public AlertListUpdater()
    {
      try
      {
        setTextLinkArray(AlertUtil.getAlertList(servletPath + "?alertapplet=getlist"));
      }catch(IOException e){
        e.printStackTrace();
      }
    }   // end constructor
    
    public void run()
    {
      Thread thisThread = Thread.currentThread();
      while (alertListUpdater == thisThread)
      {
        try
        {
          setTextLinkArray(AlertUtil.getAlertList(servletPath + "?alertapplet=getlist"));
          Thread.sleep(timeUpdate);
        }catch(IOException e){
          e.printStackTrace();
        }catch(InterruptedException ie){
        }catch(Exception e){
          e.printStackTrace();
          Thread.currentThread().interrupt();
        }
      }
    }   // end run() method
  }   // end class AlertListUpdater definition
  
  public void init()
  {
    String backgroundImage = this.getParameter("background-image");
    mt = new MediaTracker(this);
    theBackGround = this.getImage(this.getCodeBase(), backgroundImage);
    String alertIcon = this.getParameter("alert-icon");
    theAlertIcon = this.getImage(this.getCodeBase(), alertIcon);
    mt.addImage(theBackGround, 0);
    mt.addImage(theAlertIcon, 0);
    try
    {
      mt.waitForAll();
    }catch(InterruptedException ex){
      ex.printStackTrace();
    }
    
    alertX = Integer.parseInt(getParameter("padding-left"));
    int theHeight = theAlertIcon.getHeight(this);
    alertY = ((this.getBounds().height - theHeight) / 2);
    
    linkX = alertX + theAlertIcon.getWidth(this) + Integer.parseInt(getParameter("padding-text"));
    
    fontAlert = new Font(getParameter("font-face"), AlertUtil.getFontStyle(getParameter("font-style")), Integer.parseInt(getParameter("font-size")));
    this.setFont(fontAlert);
    linkY = (this.getBounds().height + this.getFontMetrics(this.getFont()).getHeight() / 2) / 2;
    textColor = Color.decode(this.getParameter("text-color"));
    timeSleep = Integer.parseInt(getParameter("time-sleep"));
    
    timeUpdate= Integer.parseInt(getParameter("time-update"));
    noAlerts = getParameter("no-alerts");
    alertLength = Integer.parseInt(getParameter("alert-length"));
    
    dismissPadding = Integer.parseInt(getParameter("dismiss-padding"));
    dismissText = new TextLink(getParameter("dismiss-text"), getParameter("dismiss-tooltip"));
    fontDismiss = new Font(getParameter("dismiss-font-face"), AlertUtil.getFontStyle(getParameter("dismiss-font-style")), Integer.parseInt(getParameter("dismiss-font-size")));
    
    URL hostURL = getCodeBase();
    String hostName = hostURL.getHost();
    int port = hostURL.getPort();
    
    if (port == -1)
    {
      port = 80;
    }
    
    if (port == 80)
    {
      webServerStr = "http://" + hostName;
    }else{
      webServerStr = "http://" + hostName + ":" + port;
    }
    servletPath = webServerStr + getParameter("servlet-path");
    alertUrl = webServerStr + getParameter("alert-url");
  }   // end init() method
  
  public void run()
  {
    Thread thisThread = Thread.currentThread();
    while (theThread == thisThread)
    {
      cycleLinks();
      repaint();
      try
      {
        Thread.sleep(timeSleep);
      }catch(InterruptedException ie){
      }catch(Exception e){
        e.printStackTrace();
        Thread.currentThread().interrupt();
      }
    }
  }    // end run() method
  
  private void cycleLinks()
  {
    if (! isAlertListEmpty && ! this.isAlertUpdating)
    {
      currentLink = (currentLink >= textlinkArray.length - 1) ? 0 : currentLink + 1;
      dismissText.setUrl(servletPath + "?alertapplet=dismissalert&" + "rowid=" + textlinkArray[currentLink].getRowId());
    }
  }    // end cycleLinks() method
  
  public void paint(Graphics g)
  {
    try
    {
      mt.waitForAll();
    }catch(InterruptedException ex){
    }

    g.drawImage(theBackGround, bgX, bgY, this);
    
    if (!isAlertListEmpty)
    {
      g.drawImage(theAlertIcon, alertX, alertY, this);
      g.setColor(textColor);
      g.setFont(this.fontAlert);
      g.drawString(textlinkArray[currentLink].getText(), linkX, linkY);
      dismissText.setPosition(this.getFontMetrics(this.getFont()), textlinkArray[currentLink].getPointEnd().x + dismissPadding, linkY, this.getGraphics());
      g.setFont(this.fontDismiss);
      g.drawString(dismissText.getText(), dismissText.getPointStart().x, linkY);
    }else{
      g.setColor(textColor);
      g.setFont(this.fontAlert);
      g.drawString(noAlerts, linkX, linkY);
    }
  }   // end paint(Graphics) method
  
  public void start()
  {
    if (alertListUpdater == null)
    {
      alertListUpdater = new Thread(new AlertListUpdater());
      alertListUpdater.start();
    }
    
    if (theThread == null)
    {
      theThread = new Thread(this);
      theThread.start();
    }
    enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
  }    // end start() method
  
  public void stop()
  {
    theThread = null;
    alertListUpdater = null;
  }    // end stop() method
  
  public void destroy()
  {
    theThread = null;
    alertListUpdater = null;
  }   // end destroy() method
  
  public void update(Graphics g)
  {
    paint(g);
  }   // end update(Graphics) method
  
  public void processMouseEvent(MouseEvent me)
  {
    if (! isAlertListEmpty)
    {
      if (textlinkArray[currentLink].mouseWithin(me.getPoint()))
      {
        if (me.getID() == MouseEvent.MOUSE_CLICKED)
        {
          try
          {
            getAppletContext().showDocument(new URL("javascript:doAlert(\"" + textlinkArray[currentLink].getUrl() +"\")"));
          }catch(MalformedURLException e){
            e.printStackTrace();
          }
        }
      }
      
      if (dismissText.mouseWithin(me.getPoint()))
      {
        if (me.getID() == MouseEvent.MOUSE_CLICKED)
        {
          String aUrl = dismissText.getUrl();
          cycleLinks();
          repaint();
          
          try
          {
            setTextLinkArray(AlertUtil.getAlertList(aUrl));
          }catch(IOException e){
            e.printStackTrace();
          }
        }
      }
    }
  }    // end processMouseEvent(MouseEvent) method
  
  public void processMouseMotionEvent(MouseEvent me)
  {
    if (!isAlertListEmpty && textlinkArray[currentLink].mouseWithin(me.getPoint()))
    {
      this.setCursor(new Cursor(Cursor.HAND_CURSOR));
      this.showStatus(textlinkArray[currentLink].getToolTip());
    }else if (!isAlertListEmpty && dismissText.mouseWithin(me.getPoint())){
      this.setCursor(new Cursor(Cursor.HAND_CURSOR));
      this.showStatus(dismissText.getToolTip());
    }else{
      this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      this.showStatus("");
    }
  }    // end processMouseMotionEvent(MouseEvent) method
  
  private void setTextLinkArray(Vector al)
  {
    isAlertUpdating = true;
    TextLink aTextlinkArray[] = null;
    if ((al != null) && (al.size() != 0))
    {
      isAlertListEmpty = false;
      String title = "";
      String moduleName	= "";
      String alertType = "";
      String rowid = "";
      
      aTextlinkArray = new TextLink[al.size()];
      
      int i = 0;
      Enumeration enum = al.elements();
      while (enum.hasMoreElements())
      {
        Hashtable hm = (Hashtable)enum.nextElement();
        alertType = (String)hm.get("alerttype");
        
        if (alertType.equals("ALERT"))
        {
          title = (String)hm.get("title");
          moduleName = (String)hm.get("modulename");
          rowid = "" + hm.get("id");
          
          String aUrl = alertUrl + "?rowId=" + rowid + "&TYPEOFACTIVITY=" + moduleName;
          
          String cuttedTitle = title;
          if (title.length() > alertLength)
          {
            cuttedTitle = title.substring(0, (alertLength - 1)) + "...";
          }
          
          String project = (hm.get("project") == null) ? "" : (String)hm.get("project");
          
          String startTime = "";
          if (hm.get("starttime") != null)
          {
            Timestamp ts = (Timestamp) hm.get("starttime");
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            startTime = sdf.format(ts);
          }
          String titleToShow = moduleName + ": " + cuttedTitle + " " + project + " " + startTime;
          
          aTextlinkArray[i] = new TextLink(aUrl, titleToShow, title, this.getFontMetrics(this.getFont()), linkX, linkY, this.getGraphics());
          aTextlinkArray[i].setRowId(rowid);
          i++;
        }
      }
      textlinkArray = null;
      textlinkArray = aTextlinkArray;
      aTextlinkArray = null;
    }else{
      isAlertListEmpty = true;
    }
    currentLink = 0;
    isAlertUpdating = false;
  }    // end setTextLinkArray(Vector) method
  
}   // end class definition

