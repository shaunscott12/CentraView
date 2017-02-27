<%--
 * $RCSfile: Download.jsp,v $    $Revision: 1.2 $  $Date: 2005/09/01 14:48:21 $ - $Author: mcallist $
 * 
 * The contents of this file are subject to the Open Software License
 * Version 2.1 ("the "License"); you may not use this file except in
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
--%>
<%@ page import="java.io.*"%>
<% 
  try
  { 
    String strPath = (String)request.getAttribute("Path");

    // In email, we're sending along the filename that we
    // would like this file to be saved as over the HTTP
    // request. So if that attribute is not null, we're
    // going to ignore the actual file name, and give this one.
    String requestFileName = (String)request.getAttribute("requestFileName");
    boolean useRequestFileName = false;
    if (requestFileName != null && ! requestFileName.equals(""))
    {
      useRequestFileName = true;
    }

    String sendFileName = "";
    
    if (request.getParameter("RealName") == null)
    {
      if (request.getAttribute("RandFileName") != null)
      {
        sendFileName = (String)request.getAttribute("RandFileName");
      }else{
        sendFileName = (String)request.getAttribute("RealName");
      }
    }else{
      sendFileName = (String)request.getParameter("RealName");
    }

    if (useRequestFileName)
    {
      sendFileName = requestFileName;
    }
    
    response.setHeader("Content-Disposition", "attachment;filename=" + sendFileName);
    response.setContentType ("application/octet-stream");
    
    File f = new File(strPath);
    
    InputStream in = new FileInputStream(f);
    byte[] buf = new byte[1024];
    
    OutputStream op = response.getOutputStream(); 
    
    try
    {
      if ((int)f.length() != 0)
      {
        int i=0;
        while ((in != null) && ((i=in.read()) != -1)) 
        {
          op.write(i);
        }
      }
    }finally{
      if (op != null)
      {
        op.flush();
        op.close();
      }
      
      if (in != null) 
      {
        in.close();
      }
    }
  }catch(Exception e){
    System.out.println(e);
  }
%>