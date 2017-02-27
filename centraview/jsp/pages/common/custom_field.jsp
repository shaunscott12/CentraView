<%
/*
 * $RCSfile: custom_field.jsp,v $    $Revision: 1.2 $  $Date: 2005/06/16 13:41:08 $ - $Author: mking_cv $
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
 */
%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="com.centraview.common.*" %>
<%@ page import="com.centraview.contact.helper.*" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="java.util.*" %>
<%
String operation = (String)request.getParameter("Operation");
String recordType = (String)request.getParameter("RecordType");
String recordIdStr = (String)request.getParameter("RecordId");

if(recordType == null)
  recordType = "";
if(operation == null)
  operation = "";
if(recordIdStr  == null)
  recordIdStr = "0";
  
int recordID = Integer.parseInt(recordIdStr);

TreeMap cfmap = new TreeMap();
TreeMap cfrmap = new TreeMap();
String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(application)).getDataSource();
cfmap = CustomFieldData.getCustomField(recordType, dataSource);
cfrmap =  CustomFieldData.getCustomField(recordType,recordID, dataSource);
TreeMap map = new TreeMap();
if(operation.equals("Add")){
  map = cfmap;
}
else if(operation.equals("Edit")){
  map.putAll(cfmap);
  map.putAll(cfrmap);
}
%>
<table border="0" cellspacing="0" cellpadding="3">
<%
int count = 1; // counter for control Name
if(map != null){
  Set  set = map.keySet() ;
  Iterator  it = set.iterator();
  
  // initial is 1 so Total NoOfFields = count
  while( it.hasNext() && count < 4 )
  {
    String str = ( String )it.next();
    Vector cvector = (Vector) request.getAttribute("CustomFieldVector");
    CustomFieldVO field =null;
    CustomFieldVO datafield = null;
    field = (CustomFieldVO)map.get(str);
    datafield = field;
    if ( cvector != null)
    {
      if (cvector.size() != 0)
      {
        datafield = (CustomFieldVO) cvector.get(count-1);
      }
    }
    // note: getting Values only from datafield which is from VO
    String fieldType = field.getFieldType();
    String fieldValue = datafield.getValue() ;

    if(fieldValue  == null)
      fieldValue = "";

    if(fieldType  == null)
      fieldType  = "SCALAR";

    int colspan = 5;

    if(!fieldType.equals("SCALAR"))
      colspan = 4;
    %>

    <input type="hidden" value="<%=datafield.getFieldID()%>" name="<%="fieldid"+count%>"/>
    <input type="hidden" value="<%=fieldType%>" name="<%="fieldType"+count%>"/>
    <tr>
        <td class="labelCell">
           <%=field.getLabel()%>:
        </td>
        <%
        if(fieldType.equals("SCALAR"))
        {
        %>
        <td align="left" class="contentCell">
          <input name="text<%=count%>" type="text" class="inputBox" value="<%=fieldValue%>" size="20">
        </td>
        <%
        }
        else
        {
          fieldValue = fieldValue.trim();
          if(fieldValue.equals(""))
            fieldValue = "0";

          Vector vec = field.getOptionValues();
          %>
          <td align="left" class="contentCell">
            <select name="text<%=count%>" class="inputBox">
              <%
              if(vec != null)
              {
                for(int j = 0;j<vec.size();j++)
                {
                  DDNameValue ddName = (DDNameValue)vec.get(j);
                  int fieldId = Integer.parseInt(fieldValue);
                  int id = ddName.getId();
                  String fieldName = ddName.getName();

                  if(fieldId == id)
                  {
                  %>
                    <option value="<%=id%>" selected ><%=fieldName%></option>
                  <%
                  }//end of if
                  else
                  {
                  %>
                    <option value="<%=id%>"><%=fieldName%></option>
                  <%
                  }
                }
              }// end of if
              %>
            </select>
          </td>
          <%
        }// end of else multiple
        %>
      </tr>

    <%
    count ++;
  }// end of while
}
%>
</table>
<!-- count is ++ so 1 to be less from it, note that count initialized to 1 -->
<input type="hidden" value="<%=count-1%>" name="TotalCustomFields"/>
