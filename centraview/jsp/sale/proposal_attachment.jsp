<%--
 * $RCSfile: proposal_attachment.jsp,v $    $Revision: 1.3 $  $Date: 2005/09/02 20:14:50 $ - $Author: mcallist $
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
 <%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
 <%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
 <%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
 <%@ page import="java.util.*"%>
 <%! HashMap hm = null ; %>
 <%! int size = 0 ; %>
<%try
{
   hm = ( HashMap )session.getAttribute( "AttachfileList" );
   if ( hm != null)
      size = hm.size();
}
catch(Exception e )
{

   e.printStackTrace();
}%>
<html>
<head>
  <title>Centraview: <bean:message key="label.sale.attachment"/></title>
  <link rel="stylesheet" type="text/css" href="<bean:message key='label.url.css' />/centraview.css">
  <script language="JavaScript1.2" src="<bean:message key='label.url.css' />/scripts.js"></script>

</head>
<body marginheight="0" marginwidth="0" topmargin="0" bottommargin="0" leftmargin="0" rightmargin="0" onLoad="window.resizeTo(605, 260);" bgcolor="#345788">
<!-- BEGIN outside table -->

<html:form action="/sales/proposal_attach_file"  enctype="multipart/form-data">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr height="2"><td class="headerDivider"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="1"><td></tr>
  <tr valign="top" height="31">
    <td>
      <!-- BEGIN tabs table -->
      <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr height="6">
          <td class="tabsOff"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="1"></td>
        </tr>
        <tr height="11">
          <td class="mainTableBG"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="11"></td>
        </tr>
      </table>
      <!-- END tabs table -->
    </td>
  </tr>
  <tr valign="top" height="100%">
    <td>
      <!-- BEGIN main table -->
      <table border="0" cellspacing="0" cellpadding="0" width="100%" height="100%">
        <tr valign="top">
          <td class="mainTableBG" width="10"><img src="<bean:message key='label.url.images' />/spacer.gif" width="10" height="1"></td>
          <td>
            <!-- BEGIN main content area -->
            <table border="0" cellspacing="0" cellpadding="0" width="100%">
              <tr>
                <td width="100%">
                  <table border="0" cellspacing="0" cellpadding="0" width="100%">
                    <tr height="3"><td class="popupTableHeadShadow"><img src="<bean:message key='label.url.images' />/spacer.gif" height="3" width="1"></td>
                    </tr>
                    <tr class="popupTableHead" height="17">
                    <td class="popupTableHeadText"><bean:message key="label.sale.attachfile"/></td>
                    </tr>
                    <tr height="1"><td class="popupTableHeadBottom"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1"></td></tr>
                    <tr class="popupTableRow" valign="top">
                      <!-- Left content area -->
                      <td class="popupTableTD">
                 <table border="0" cellspacing="0" cellpadding="3" width="100%">
                        <tr height="5">
                          <td class="popupDarkTD"></td>
                        </tr>
                      </table>
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <td width="50%"><table border="0" cellspacing="0" cellpadding="3" width="100%">
                              <tr>
                                <td width="33%" height="67%" align="left" class="popupTableText"><bean:message key="label.sale.attachfilefromlocalsys"/>:</td>
                              </tr>
                              <tr>
                                <td height="67%">
                        <!--input name="file" type="file" class="fileBrowseButton"-->
                        <!--html:file property="file" styleClass="fileBrowseButton" /-->
                        <html:file property="file"/>
                                </td>
                              </tr>

                              <tr>
                                <td align="left" class="popupTableText"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="3"></td>
                              </tr>
                              <tr>
                                <td align="left" class="popupTableText"><bean:message key="label.sale.attachfilefromserver"/>:</td>
                              </tr>
                              <tr>
                                <td>
                        <input name="fileNameFromCent" type="text" class="textBoxWhiteBlueBorder" style="font-size:65%;width:15em;" />
                                <input name="button42" type="button" class="popupButton" value="<bean:message key='label.value.lookup'/>" onClick="filelookup()">
                                </td>
                              </tr>
                            </table></td>
                            <td width="50%"><table width="100%" border="0" cellspacing="0" cellpadding="2">
                              <tr>
                                <td width="25%" align="center">

                        <app:cvbutton property="attach" styleClass="popupButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" style="width:7em;" onclick="attachFile()">
                          <bean:message key="label.sale.attach"/>
                        </app:cvbutton>
                        <br>
                        <app:cvbutton property="remove" styleClass="popupButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" style="width:7em;" onclick="removeFile()">
                          <bean:message key="label.sale.remove"/>
                        </app:cvbutton>

                                </td>
                                <td width="75%">
                        <html:select property="filelist" multiple="true" style="width:180px;"  styleClass="textBoxWhiteBlueBorder" value="">
                        <%if( hm != null )
                        {
                           Set col = hm.keySet();
                           Iterator itt = col.iterator();
                           int i=0;
                           Object ref = null;
                           while( itt.hasNext() )
                           {
                              ref = itt.next();%>
                              <option id="<%=ref%>" ><%= hm.get(ref)%></option>
                        <%	}
                        }%>
                        </html:select>
                                </td>
                              </tr>
                            </table></td>
                          </tr>
                          <tr>
                            <td colspan="2"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="5"></td>
                          </tr>
                          <tr>
                            <td colspan="2" class="activityDivider"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="1"></td>
                          </tr>
                          <tr>
                             <td colspan="2" align="center" style="padding:4px;">
                                 <app:cvbutton property="done" styleClass="popupButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" style="width:7em;" onclick="done12233()">
                                  <bean:message key="label.sale.done"/>
                                 </app:cvbutton>
                              </td>
                           </tr>
                        </table>
                      </td>
                      <!-- Middle content area -->
                      <!-- Right content area -->
                    </tr>
                    <tr height="1">
                                <td class="popupTableDivider"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="1"></td>
                             </tr>
                    <!-- Bottom content area -->
                    <tr height="1">
                                <td class="popupTableDivider"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="1"></td>
                             </tr>
                  </table>
                </td>
              </tr>
            </table>
            <!-- END main content area -->
          </td>
          <td class="mainTableBG" width="10"><img src="<bean:message key='label.url.images' />/spacer.gif" width="10" height="1"></td>
        </tr>
        <tr height="11">
          <td class="mainTableBG" colspan="3"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="11"></td>
        </tr>
      </table>
      <!-- END main table -->
    </td>
  </tr>
</table>
<!-- END outside table -->

<input type="hidden" name="fileId">
<script language="javascript">

function attachFile()
{

   var filename = document.forms[0].file.value;
   var fileNameFromCent = document.forms[0].fileNameFromCent.value;

   if( filename == "" && fileNameFromCent == "")
   {
      alert("<bean:message key='label.alert.selectfile'/>!");
      return;
   }
   var index ;
   var file ;
   if (filename != "")
      file = filename.substring( filename.lastIndexOf("\\")+1  );
   else
      file = fileNameFromCent;
   var m = document.forms[0].filelist.options.length;
   //document.forms[0].filelist.options[m] = new Option(file,file,"false","false");
   if (filename != "")
      document.forms[0].action="<bean:message key='label.url.root' />/ProposalAttachFileHandler.do?From=Local";
   else if (fileNameFromCent != null)
   {
   document.forms[0].action="<bean:message key='label.url.root' />/ProposalAttachFileHandler.do?From=Server&FileID="+document.forms[0].fileId.value+"&fileName="+file;
   }
   document.forms[0].submit();
   m = m+1;
}

function removeFile()
{

   var length = document.forms[0].elements['filelist'].options.length - 1;
   for (var i=length; i >= 0 ; i--) {

      if (document.forms[0].elements['filelist'].options[i].selected) {

         document.forms[0].elements['filelist'].options[i] = null;
      }
   }
}

function done12233()
{
   opener.updateAttachmentBlank();
   var i=0;
   while( i < document.forms[0].filelist.length )
   {
         var strAttach = document.forms[0].filelist[i].text
         var strAttachID = document.forms[0].filelist[i].id
         opener.updateAttachment(strAttach, strAttachID);
         i++;
   }
   window.close();
}

function filelookup()
{
   openWindow('<bean:message key='label.url.root' />/files/file_lookup.do', '', 400, 400,'');
}
function setData(lookupValues) {
  document.forms[0].fileId.value = lookupValues.idValue;
  document.forms[0].fileNameFromCent.value = lookupValues.Name;
}

</script>
</html:form>
</body>
</html>