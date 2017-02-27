<%--
 * $RCSfile: folder_list.jsp,v $    $Revision: 1.3 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
 *
 * The contents of this file are subject to the Open Software License
 * Version 2.1 ("the License"); you may not use this file except in
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
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="java.util.HashMap"%>
<%@ page import = "com.centraview.common.*"%>
<%@ page import = "com.centraview.mail.MailFolderVO,java.util.*;"%>
<%
try{
String search="";
%>
<html:html>
<head>
  <link rel="stylesheet" type="text/css" href="<html:rewrite page="/stylesheet/main.css" />">
  <script src="<html:rewrite page="/stylesheet/ua.js" />"></script>
  <script src="<html:rewrite page="/stylesheet/ftiens4.js" />"></script>
  <script >
  USETEXTLINKS = 1
  STARTALLOPEN = 0
  HIGHLIGHT = 1
  PERSERVESTATE = 1
  ICONPATH = '<html:rewrite page="/images/"/>';

  <%--
  // If you don't have server-side coding, make your nodes.js file similar to the others
  // in the ZIP, not this one.
  // Without a database, the auxiliary functions defined and used here are the only way
  // I am able to test the "PERSERVESTATE through .xID keys" functionality.
  // Your server side code will do something similar, but without the need to these functions.
  // It will simply output nodes with two lines of statements, one for insDoc or insFld call,
  // the other with the assignment of xID to an actual database ID
  // See online instructions for limitations on the use of xID
  --%>
  var counterI = 0
  function insFldX(parentOb, childOb)
  {
    childOb.xID = 'X' + counterI;
    counterI--;
    return insFld(parentOb, childOb);
  }
  function insDocX(parentOb, childOb)
  {
    childOb.xID = 'Y' + counterI;
    counterI--;
    return insDoc(parentOb, childOb);
  }


  foldersTree = gFld("eEmail");
  foldersTree.treeID = "big";
  foldersTree.xID = 'X0';
  counterI--;

  <%
    TreeMap folderList = (TreeMap)request.getAttribute("folderList");
    Integer selectedFolderID = (Integer) session.getAttribute("currentMailFolder");
    int iFolderID = selectedFolderID.intValue();
    Set listkey = folderList.keySet();
    Iterator it = listkey.iterator();
    while (it.hasNext())
    {
      String accountKeyValue = (it.next()).toString();
      String emailAddress = "";

      StringTokenizer st = new StringTokenizer(accountKeyValue, "*");
      while (st.hasMoreTokens())
      {
        emailAddress = (String)st.nextToken();
      }

      ArrayList accountFolderList = (ArrayList)folderList.get(accountKeyValue);

      if (accountFolderList != null)
      {
        int indexEmail = emailAddress.indexOf("@");
        String tempEmailAddress = emailAddress;
        if(indexEmail > 0)
        {
          tempEmailAddress = emailAddress.substring(0,indexEmail)+"root";
        }
        tempEmailAddress = tempEmailAddress.replace('.','e');
        tempEmailAddress = tempEmailAddress.replaceAll(" ","");
        tempEmailAddress = tempEmailAddress.replaceAll("[ ~`!#$%^&*()_+|{}:\"?><,./;\'=-]","");
        %><%=tempEmailAddress%> = insFldX(foldersTree, gFld("&<%=emailAddress%>", "root"));

<%

        HashMap folderCollection = new HashMap();
        for (int j=0; j<accountFolderList.size(); j++)
        {
          MailFolderVO f = (MailFolderVO)accountFolderList.get(j);
          int folderID = f.getFolderID();
          String folderName= f.getFolderName();
          folderCollection.put(new Integer(folderID), folderName);
        }

        ArrayList foldStrs = new ArrayList();
        TreeMap sysfoldStrs = new TreeMap();
        String str = "";
        for (int i=0; i<accountFolderList.size(); i++)
        {
          MailFolderVO f = (MailFolderVO)accountFolderList.get(i);
          int folderID = f.getFolderID();
          String folderName= f.getFolderName();
          int parentID= f.getParentID();
          String parentName= (String)folderCollection.get(new Integer(parentID));

          if (parentName != null && parentName.equals("root"))
          {
            parentName = tempEmailAddress;
          }else{
            parentName = tempEmailAddress + parentName;
          }

          if (folderName != null && (!folderName.equals("root")))
          {
            // Strip spaces and hyphens out of the folder name
            String tempFolderName = folderName;
  	        parentName=parentName.replaceAll("[ ~`!@#$%^&*()_+|{}:\"?><,./;\'=-]","");
  	        parentName=parentName.replace(']','e');
   	        parentName=parentName.replace('[','e');

  	        tempFolderName=tempFolderName.replaceAll("[ ~`!@#$%^&*()_+|{}:\"?><,./;\'=-]","");
 	        tempFolderName=tempFolderName.replace(']','e');
 	        tempFolderName=tempFolderName.replace('[','e');

            str = tempEmailAddress + tempFolderName + " = insFldX(" + parentName + ", gFld(\"" + folderID + "±" + folderName + "\", \"" + request.getContextPath() + "/email/email_list.do?clicked=true&folderID=" + folderID + "\"));";
            if (folderName.equals("Inbox") || folderName.equals("Drafts") || folderName.equals("Sent") || folderName.equals("Trash")){
                sysfoldStrs.put(folderName, str);
            } else {
                foldStrs.add(str);
            }

            if(iFolderID == folderID)
            {
              search=folderID+"±"+folderName;
            }
          }   // end if (folderName != null && (!folderName.equals("root")))
        }   // end for (int i=0; i<accountFolderList.size(); i++)
        %>
        <%=(String)sysfoldStrs.get("Inbox")%>
        <%=(String)sysfoldStrs.get("Drafts")%>
        <%=(String)sysfoldStrs.get("Sent")%>
        <%=(String)sysfoldStrs.get("Trash")%>
        <%
   			for (int j=0; j<foldStrs.size(); j++)
        {
        %>
            <%=(String)foldStrs.get(j)%>
        <%}
      }   // end if (accountFolderList != null)
    }   // end while (it.hasNext())
  %>

  foldersTree.treeID = "L1";
  </script>
</head>
<body class="emailIframe">
<script>initializeDocument('<%=search%>');</script>
<noscript>
  <bean:message key="label.email.treejavascript"/>.
</noscript>
</body>
</html:html>
<%
}catch(Exception e){
  System.out.println("[Exception][/mail/folder_list.jsp]: " + e);
	e.printStackTrace();
}
%>
