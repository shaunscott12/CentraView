/*
 * $RCSfile: TextLink.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:19 $ - $Author: mking_cv $
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

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;

/**
 * @author <a href="mailto:bilyush@belsoft.by">Boris A Ilyushonak</a>
 * Date: Jan 28, 2004
 * Time: 2:39:22 PM
 * @version $Revision: 1.1.1.1 $
 */
public class TextLink {

   private String urlLink = null;
   private String text;
   private String toolTip;
   private Point blockStart = null;
   private Point blockEnd = null;

   private String rowId = "";

   public TextLink(String text, String toolTip) {

      this.text = text;
      this.toolTip = toolTip;
   }

   public TextLink(String url,
                   String text,
                   String toolTip,
                   FontMetrics fm,
                   int x,
                   int y,
                   Graphics g) {

      this.text = text;
      this.toolTip = toolTip;
      this.setPosition(fm,
                   x,
                   y,
                   g);
      urlLink = url;
   }

   public void setPosition(FontMetrics fm,
                   int x,
                   int y,
                   Graphics g) {

      blockStart = new Point(x, y  - fm.getHeight());
      blockEnd = new Point(fm.stringWidth(text) + x
                           , y);

   }

   public boolean mouseWithin(Point p) {

      if ((p.x >= blockStart.x )
            && (p.y >= blockStart.y)
            && (p.x <= blockEnd.x)
            && (p.y <= blockEnd.y)) {

         return true;
      }
      else {

         return false;
      }
   }

   public String getUrl() {

      return urlLink;
   }

   public void setUrl(String url) {

      this.urlLink = url;
   }

   public String getText() {

      return text;
   }

   public Point getPointStart () {

      return this.blockStart;
   }

   public Point getPointEnd() {

      return this.blockEnd;
   }

   public String getToolTip() {

      return toolTip;
   }

   public String getRowId() {

      return rowId;
   }

   public void setRowId(String rowId) {

      this.rowId = rowId;
   }
}
