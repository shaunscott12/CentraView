/*
 * $RCSfile: AlertUtil.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:19 $ - $Author: mking_cv $
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

import java.awt.Font;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

/**
 * @author <a href="mailto:bilyush@belsoft.by">Boris A Ilyushonak</a>
 * Date: Jan 28, 2004
 * Time: 2:51:44 PM
 * @version $Revision: 1.1.1.1 $
 */
public class AlertUtil {

   public static int getFontStyle(String s) {

      if (s.equalsIgnoreCase("bold")) return Font.BOLD;
      else if (s.equalsIgnoreCase("italic")) return Font.ITALIC;
      else if (s.equalsIgnoreCase("bolditalic") || s.equalsIgnoreCase("bold italic")) return (Font.BOLD | Font.ITALIC);
      else return Font.PLAIN;
   }

   public static Vector getAlertList(String requestUrl) throws IOException {

      Vector al = null;
      ObjectInputStream inFromServlet = null;
      try {

         URL studentDBservlet = new URL( requestUrl );
         URLConnection servletConnection = studentDBservlet.openConnection();
         servletConnection.setDoInput(true);
         servletConnection.setDoOutput(true);
         servletConnection.setUseCaches (false);
         servletConnection.setRequestProperty
             ("Content-Type", "application/octet-stream");
         inFromServlet = new ObjectInputStream(servletConnection.getInputStream());
         al = (Vector) inFromServlet.readObject();
      }
      catch (ClassNotFoundException e) {

         e.printStackTrace();
      }
      finally {

         if (inFromServlet != null) {

            inFromServlet.close();
         }
      }
      return al;
   }
}
