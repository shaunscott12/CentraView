<%--
* $RCSfile: folder_tree.jsp,v $    $Revision: 1.3 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
* Original Code created by CentraView are Copyright (c) 2003 - 2005 CentraView,
* LLC; All Rights Reserved.  The terms "CentraView" and the CentraView
* logos are trademarks and service marks of CentraView, LLC.
--%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="java.util.HashMap"%>
<%@ page import = "com.centraview.common.*"%>
<%@ page import = "com.centraview.file.*,java.util.*,java.io.*;"%>
<%
  String folderId = (String)request.getAttribute("folderId");
  String fileTypeRequest = (String)request.getAttribute("fileTypeRequest");
  Integer publicFolderId = (Integer)request.getAttribute("publicFolderId");
  String ctx = request.getContextPath();
  String search = "";
  if (fileTypeRequest.equals("MY")) {
    search = "mMy Files";
  } else if (fileTypeRequest.equals("PUBLIC")) {
    search = "pPublic Folders";
  } else {
    search = "aAll Files";
  }
  
  if (folderId.equals("null")) {
    search = "mMy Files";
  }
%>
<%!
 /**
  * This method gets a list of parents for an item to show in the tree
  * Everything is available to it in the folderlist.
  */
  private String[] getTreeViewList(ArrayList folderlist, String param, String rootName, String rowId, String ctx, int publicFolderId)
  {		
    CvFolderVO folder;
    int folderId = 0;
    int parentId = 0;
	/** temporary string */
    String str = "";
    
    String returnString[] = new String[2];
    folder = (CvFolderVO)folderlist.get(0);
    
    int tempParentID = folder.getParent();
    
    String mapping = "";
    String name  = "";			
    String search = "";
    String parentName = "";
    int count = 0;
    
    String paramValue = "MY";
    if (param.equals("m")) { 
      paramValue = "MY";
    }
    
    if (param.equals("a")) {
      paramValue = "ALL";
    }

    if (param.equals("p")) {
      paramValue = "PUBLIC";
    }
    
    for (int i = 0; i < folderlist.size(); i++) {
      folder = (CvFolderVO)folderlist.get(i);
      folderId = folder.getFolderId();
      parentId = folder.getParent();
      parentName = folder.getParentName();
      name = folder.getName();
      
      if (param.equals("a") && ((folderId == publicFolderId) || (parentId == publicFolderId))) {
        // skip the public folder under "All"
        continue;
      }
      
      if (rowId != null && ! rowId.equals("null")) {
        int row = Integer.parseInt(rowId);
        if (row == folderId) {
          search = name;
        }
      }
      
      if (tempParentID == parentId) {
        parentName = rootName;
        str = str + param + folderId + "= insFldX(" + parentName + ", gFld(\"" + param + name + "\", \"" + ctx + "/files/file_list.do?folderId=" + folderId + "&TYPEOFFILELIST=" + paramValue + "\"));\n";
      } else {
        str = str + param + folderId + "= insFldX(" + param + parentId + ", gFld(\"" + param + name + "\", \"" + ctx + "/files/file_list.do?folderId=" + folderId + "&TYPEOFFILELIST=" + paramValue + "\"));\n";
      }
    } // end for
    returnString[0] = str;
    returnString[1] = search;
    return returnString;
  }
%>
<!--
  This frameset document includes the Treeview script.
  Script found at: http://www.treeview.net
  Author: Marcelino Alves Martins
-->
<html:html>
<head>
  <link rel="stylesheet" type="text/css" href="<html:rewrite page="/stylesheet/main.css" />">
  <script src="<html:rewrite page="/stylesheet/ua.js" />"></script>
  <script src="<html:rewrite page="/stylesheet/ftiens4.js" />"></script>
  <script>
    USETEXTLINKS = 1
    STARTALLOPEN = 0
    HIGHLIGHT = 1
    PERSERVESTATE = 1
    ICONPATH = '<html:rewrite page="/images/"/>';
    var counterI = 0
    function insFldX(parentOb, childOb)
    {
      childOb.xID = 'X' + counterI;
      counterI--;
      return insFld(parentOb, childOb)
    }
    
    function insDocX(parentOb, childOb)
    {
      childOb.xID = 'Y' + counterI;
      counterI--;
      return insDoc(parentOb, childOb)
    }
    
    foldersTree = gFld("aFiles")
    foldersTree.treeID = "big"
    foldersTree.xID = 'X0';
    counterI--;
    
    my = insFldX(foldersTree, gFld("mMy Files", "<%=ctx%>/files/file_list.do?TYPEOFFILELIST=MY"));
    all = insFldX(foldersTree, gFld("aAll Files", "<%=ctx%>/files/file_list.do?folderId=2&TYPEOFFILELIST=ALL"));
    publicFolder = insFldX(foldersTree, gFld("pPublic Folders", "<%=ctx%>/files/file_list.do?folderId=<%=publicFolderId%>&TYPEOFFILELIST=PUBLIC"));
    
    <%
      // Set up sub folders
      ArrayList myfolderList = (ArrayList)request.getAttribute("userfolderlist");
      ArrayList allFolderList = (ArrayList)request.getAttribute("allfolderlist");
      ArrayList publicFolderList = (ArrayList)request.getAttribute("publicFolderList");
      
      if (myfolderList != null && !(myfolderList.size() == 0)) {
        String myfolderTemp[] = getTreeViewList(myfolderList, "m", "my", folderId, ctx, publicFolderId.intValue());
        out.println(myfolderTemp[0]);
        if (myfolderTemp[1] != null && ! myfolderTemp[1].equals("")) {
          if (fileTypeRequest != null && fileTypeRequest.equals("MY")) {
            search = "m" + myfolderTemp[1];
          }
        }
      }
      
      if (allFolderList != null && ! (allFolderList.size() == 0)) {
        String folderTemp[] = getTreeViewList(allFolderList, "a", "all", folderId, ctx, publicFolderId.intValue());
        out.println(folderTemp[0]);
        if (folderTemp[1] != null && ! folderTemp[1].equals("")) {
          if (fileTypeRequest != null && fileTypeRequest.equals("ALL")) {
            search = "a" + folderTemp[1];
          }
        }
      }
      
      if (publicFolderList != null && ! (publicFolderList.size() == 0)) {
        String folderTemp[] = getTreeViewList(publicFolderList, "p", "publicFolder", folderId, ctx, publicFolderId.intValue());
        out.println(folderTemp[0]);
        if (folderTemp[1] != null && ! folderTemp[1].equals("")) {
          if (fileTypeRequest != null && fileTypeRequest.equals("PUBLIC")) {
            search = "p" + folderTemp[1];
          }
        }
      }

      if (folderId != null && ! folderId.equals("null")) {
        int row = Integer.parseInt(folderId);
        if (row == 2) {
          search= "aAll Files";
        }
      }
    %>
  </script>
</head>
<body class="emailIframe">
<script>initializeDocument('<%=search%>');</script>
<noscript>
  <bean:message key="label.files.treejavascript"/>.
</noscript>
</body>
</html:html>
