<%--
 * $RCSfile: search.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:46 $ - $Author: mcallist $
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
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.centraview.common.DDNameValue" %>
<%@ page import="com.centraview.common.SearchRecord" %>
<%@ page import="com.centraview.common.SearchCollection" %>
<%@ page import="com.centraview.common.SearchObject" %>
<%@ page import="com.centraview.common.DataDictionary" %>
<%@ page import="com.centraview.common.ListGenerator" %>
<%@ page import="com.centraview.common.DisplayList" %>
<%@ page import="com.centraview.common.GlobalMasterLists" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator"%>
<%@ page import="com.centraview.common.CVUtility" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.settings.SiteInfo" %>


<html:form action="/Search">

<%
 	SearchCollection sColl =null;
	SearchObject searchObj = null;
	ListGenerator lg = null;
	GlobalMasterLists gl= null;
	DisplayList dl = null;

	Vector userVec1 = null;
	Vector gpVec1 = null;

	Vector authUsersVec = new Vector();
	Vector authGroupsVec = new Vector();

	String ps = "";
	String isValidRecord = "true";
	String isDisabled = "disabled";
	String strTableId = null;
	String strUserId = null;
	String lastChanged = "none";
	String listid = null;
	String searchCriteria = "valid";
	String tbl = null;

	boolean isSavedSearch = false;

	//By default there will be 1 row always.
	int noOfRecords = 1;

	int noOfTables = 0;
	int noOfColumns = 0;
	int noOfConditions = 0;
	int noOfJoins = 0;
	//No  of SearchRecord objects in sColl
	int intRecords = 0;

	//if click on update
	if(request.getAttribute("lastChanged") != null)
	{
		lastChanged = (String)request.getAttribute("lastChanged");
	}

	//Always set
	if(request.getAttribute("tableid") != null)
	{
		strTableId = (String)request.getAttribute("tableid");
		request.setAttribute("tableid",strTableId);
	}

	if(request.getAttribute("individualid") != null)
	{
		strUserId = (String)request.getAttribute("individualid");
		request.setAttribute("individualid",strUserId);
	}

	if(request.getAttribute("listid") != null)
	{
		listid = (String)request.getAttribute("listid");
		request.setAttribute("listid",listid);
	}

	if(request.getAttribute("table") != null)
	{
		tbl = (String)request.getAttribute("table");
		request.setAttribute("table",tbl);
	}

	//always set ends here.

	if(request.getAttribute("isValidRecord") != null )
	{
		isValidRecord = (String)request.getAttribute("isValidRecord");
	}

	if(request.getAttribute("errormsg") != null)
	{
		searchCriteria = "invalid";
	}
	if(request.getAttribute("sColl") != null )
	{
		isDisabled = "";
		sColl = (SearchCollection)request.getAttribute("sColl");

		noOfRecords = sColl.size();
		intRecords = noOfRecords;

		//This is needed if ps is changed and handler
		//throws error to jsp. sColl will help to paint the
		//page again with selected values
		request.setAttribute("sColl",sColl);
	}

	String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(application)).getDataSource();
	gl = GlobalMasterLists.getGlobalMasterLists(dataSource);

	userVec1 = (Vector)gl.get("Users");
	Vector userVec = new Vector();
	userVec.addAll(userVec1);

	gpVec1 = (Vector)gl.get("Groups");
	Vector gpVec   = new Vector();
	gpVec.addAll(gpVec1);

	int p = userVec.size();
	for(int h=0;h<p;h++)
	{
		DDNameValue dd = (DDNameValue)(userVec.get(h));

		String sName = dd.getName();

		int id = dd.getId();
		String sId = sName+"u:"+id;
		dd.setStrid(sId);

		dd = new DDNameValue(sId,sName);
		userVec.setElementAt(dd,h);
	}

	p = gpVec.size();

	for(int h=0;h<p;h++)
	{
		DDNameValue dd = (DDNameValue)(gpVec.get(h));

		String sName = dd.getName();
		int id = dd.getId();
		String sId = sName+"g:"+id;
		dd.setStrid(sId);

		dd = new DDNameValue(sId,sName);
		gpVec.setElementAt(dd,h);
	}

	userVec.addAll(gpVec);

	pageContext.setAttribute("userVec", userVec, PageContext.PAGE_SCOPE);

	SearchRecord sr[]= new SearchRecord[intRecords];

	for (int i=0 ; i<intRecords;i++){

	 	 sr[i] = (SearchRecord)sColl.elementAt(i);
		 ps = ps + sr[i].getPowerString();
	}

	ArrayList joinAl = new ArrayList();

	joinAl.add("AND");
	joinAl.add( "OR");

	noOfJoins = joinAl.size();

	ArrayList tableAl = new ArrayList();

	//Get all search criteria from DataDictionary
	//which is in session

	if (request.getAttribute("SearchObject") != null)
	{
		isSavedSearch = true;
		searchObj = (SearchObject)request.getAttribute("SearchObject");
		request.setAttribute("SearchObject",searchObj);
		//ps = searchObj.getStrSearchString();
	}

	if(request.getAttribute("AuthUsers")!=null && ((Vector)request.getAttribute("AuthUsers")).size() != 0)
	{
		Vector aUsers  = (Vector)request.getAttribute("AuthUsers");

		p = aUsers.size();

		for(int h=0;h<p;h++)
		{
			DDNameValue dd = (DDNameValue)(aUsers.get(h));

			String sName = dd.getName();
			int id = dd.getId();
			String sId = sName+"u:"+id;
			dd.setStrid(sId);

			dd = new DDNameValue(sId,sName);
			aUsers.setElementAt(dd,h);
		}

		if ( authUsersVec != null)
		authUsersVec.clear();

		authUsersVec.addAll(aUsers);

	}

	if(request.getAttribute("AuthGroups") != null && ((Vector)request.getAttribute("AuthGroups")).size() != 0)
	{
		Vector aGroups = (Vector)request.getAttribute("AuthGroups");
		p = aGroups.size();

		for(int h=0;h<p;h++)
		{
			DDNameValue dd = (DDNameValue)(aGroups.get(h));

			String sName = dd.getName();
			int id = dd.getId();
			String sId = sName+"g:"+id;
			dd.setStrid(sId);

			dd = new DDNameValue(sId,sName);
			aGroups.setElementAt(dd,h);
		}
		authUsersVec.addAll(aGroups);
	}


    pageContext.setAttribute("authUsersVec", authUsersVec, PageContext.PAGE_SCOPE);

	DisplayList displaylist1=null;
	String keyTable = null;

	if(request.getAttribute("table") != null)
	{
		keyTable = (String)request.getAttribute("table");
		request.setAttribute("table",keyTable);

	}

	DataDictionary dd = new DataDictionary(dataSource);
	HashMap fhm = dd.getFinalMapping();
	HashMap hm  = (HashMap)fhm.get(keyTable);
	noOfTables = hm.size();

	Vector colVec = null;
	Iterator iterator = (hm.keySet()).iterator();

	String table = null;

	int j = 0;

	while (iterator.hasNext())
	{
		table = (String)iterator.next();
		tableAl.add(table);
%>
	<input type="hidden" name="tbl<%=j%>" value="<%=tableAl.get(j)%>"/>
<%
		colVec = (Vector)hm.get(table);
		noOfColumns = colVec.size();

		for(int i=0;i<noOfColumns;i++)
		{
%>
	<input type="hidden" name="<%=tableAl.get(j)%>col<%=i%>" value="<%=colVec.elementAt(i)%>"/>
<%
		}
%>
	<input type="hidden" name="colCntFor<%=tableAl.get(j)%>" value="<%=noOfColumns%>"/>
<%
		j++;
		colVec.clear();

	}
%>
	<input type="hidden" name="tblCount" value="<%=noOfTables%>"/>
<%

	ArrayList condAl = new ArrayList();

	condAl.add("Begins With");
	condAl.add("Equals");
	condAl.add("Greater Than");
	condAl.add("Ends With");
	condAl.add("Contains");

	noOfConditions = condAl.size();
	for (j = 0;j < noOfConditions ; j++){

%>
	<input type="hidden" name="cond<%=j%>" value="<%=condAl.get(j)%>"/>
<%
	}
%>
<input type="hidden" name="condCount" value="<%=noOfConditions%>"/>

<!-- BEGIN main content area -->
<input type=hidden name="lastChanged" value="<%=lastChanged%>">
<input type =hidden name="noOfRows" value="<%=noOfRecords%>" name="noOfRows"/>
<input type =hidden name="rowsAddedTill" value="<%=noOfRecords+2%>" />

<table border="0" cellspacing="0" cellpadding="0" height="100%" >
<tr height="10" valign="top">
	<td colspan="3"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="1"></td>
	<td rowspan="26">
		<table border="0" cellspacing="0" cellpadding="0" height="100%" width="125">
			<tr height="20">
				<td class="popupDarkTD"><img src="<bean:message key='label.url.images' />/spacer.gif" width="3" height="1"></td>
				<td class="popupDarkTD"><span class="popupTableTextBold"><bean:message key="label.search.instructions"/></td>
				<td class="popupDarkTD"><img src="<bean:message key='label.url.images' />/spacer.gif" width="3" height="1"></td>
			</tr>
			<tr><td colspan="3" class="popupMediumTD"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="12"></td></tr>
			<tr>
				<td class="popupMediumTD"><img src="<bean:message key='label.url.images' />/spacer.gif" width="3" height="1"></td>
				<td class="popupMediumTD"><span class="popupTableText"><bean:message key="label.search.savechanges"/>.</td>
				<td class="popupMediumTD"><img src="<bean:message key='label.url.images' />/spacer.gif" width="3" height="1"></td>
			</tr>
			<tr><td colspan="3" class="popupMediumTD"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1"></td></tr>
			<tr>
				<td class="popupMediumTD"><img src="<bean:message key='label.url.images' />/spacer.gif" width="3" height="1"></td>
				<td class="popupMediumTD"><span class="popupTableText"><bean:message key="label.search.buildasearch"/>.</td>
				<td class="popupMediumTD"><img src="<bean:message key='label.url.images' />/spacer.gif" width="3" height="1"></td>
			</tr>
			<tr><td colspan="3" class="popupMediumTD"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="6"></td></tr>
			<tr>
				<td class="popupMediumTD"><img src="<bean:message key='label.url.images' />/spacer.gif" width="3" height="1"></td>
				<td class="popupMediumTD"><%-- <input class="svdSrchInsBtn" type="button" value="<bean:message key='label.administration.searchtips'/>"> --%></td>
				<td class="popupMediumTD"><img src="<bean:message key='label.url.images' />/spacer.gif" width="3" height="1"></td>
			</tr>
			<tr><td colspan="3" class="popupMediumTD"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="2"></td></tr>
			<tr>
				<td class="popupMediumTD"><img src="<bean:message key='label.url.images' />/spacer.gif" width="3" height="1"></td>
				<td class="popupMediumTD"><%-- <input class="svdSrchInsBtn" type="button" value="<bean:message key='label.administration.searchhelp'/>"> --%></td>
				<td class="popupMediumTD"><img src="<bean:message key='label.url.images' />/spacer.gif" width="3" height="1"></td>
			</tr>
			<tr><td colspan="3" class="popupMediumTD"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="2"></td></tr>
			<tr>
				<td class="popupMediumTD"><img src="<bean:message key='label.url.images' />/spacer.gif" width="3" height="1"></td>
				<td class="popupMediumTD"><%-- <input class="svdSrchInsBtn" type="button" value="<bean:message key='label.administration.showmeexamples'/>"> --%></td>
				<td class="popupMediumTD"><img src="<bean:message key='label.url.images' />/spacer.gif" width="3" height="1"></td>
			</tr>
			<tr><td colspan="3" class="popupMediumTD"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="18"></td></tr>
			<tr>
				<td class="popupMediumTD"><img src="<bean:message key='label.url.images' />/spacer.gif" width="3" height="1"></td>
				<td class="popupMediumTD"><%-- <span class="popupTableTextBold"><bean:message key="label.search.poweredit"/>--%></td>
				<td class="popupMediumTD"><img src="<bean:message key='label.url.images' />/spacer.gif" width="3" height="1"></td>
			</tr>
			<tr>
				<td class="popupMediumTD"><img src="<bean:message key='label.url.images' />/spacer.gif" width="3" height="1"></td>
				<td class="popupMediumTD"><span class="popupTableText">&nbsp;</td>
				<td class="popupMediumTD"><img src="<bean:message key='label.url.images' />/spacer.gif" width="3" height="1"></td>
			</tr>
			<tr height="100%"><td class="popupMediumTD" colspan="3" height="100%"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="1"></td></tr>
		</table>
	</td>
</tr>

<tr>
	<td width="11"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="11"></td>
	<td><span class="popupTableText"><bean:message key="label.search.savechanges"/></span></td>
	<td width="11"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="11"></td>
</tr>

<tr><td colspan="3"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="7"></td></tr>

<tr>
	<td width="11"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="11"></td>
	<td>
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td><span class="popupTableTextBold"><bean:message key="label.search.renamesearch"/>:</span></td>
				<td width="3"><img src="<bean:message key='label.url.images' />/spacer.gif" width="3" height="1"></td>
				<%
					String val = "";
					int id = -1;
					if(isSavedSearch)
					{
						val = searchObj.getStrSearchName();
						id =  searchObj.getIntSearchId();
					}
				%>
				<td>
					<input name="searchName" class="popupTextBox" type="text" value="<%=val%>" onChange="setLastChanged('search',this,-1)">
					<input name="searchid" type="hidden" value="<%=id%>">
				</td>
			</tr>
		</table>
	</td>
	<td width="11"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="11"></td>
</tr>

<tr height="7"><td colspan="3"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="7"></td></tr>
<tr>
	<td width="11"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="11"></td>
	<td class="activityDivider"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="1"></td>
	<td width="11"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="11"></td>
</tr>
<tr height="7"><td colspan="3"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="7"></td></tr>

<!-- user/group selection -->
<tr>
	<td width="11"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="11"></td>
	<td>
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td><span class="popupTableTextBold"><bean:message key="label.search.selectuser"/>:</span></td>
				<td width="5"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="5"></td>
				<td>&nbsp;</td>
				<td width="5"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="5"></td>
				<td><span class="popupTableTextBold"><bean:message key="label.search.accessibleto"/>:</span></td>
			</tr>
			<tr>
				<td>
				<!--style="width:200px;"-->
				<html:select property="selectuser" multiple="true" styleClass="svdSrchTextAreaNew">
			    <html:options collection="userVec" property="strid" labelProperty="name" />
				</html:select>
				</td>
				<td width="5"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="5"></td>
				<td>
					<input class="addRemoveBtn" type="button" value="<bean:message key='label.administration.add'/> &raquo;" onClick="addToRight()"><br>
					<input class="addRemoveBtn" type="button" value="&laquo; <bean:message key='label.administration.remove'/>" onClick="removeFromRight()">
				</td>
				<td width="5"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="5"></td>
				<td>
				<!--style="width:200px;"-->
				<html:select property="access" multiple="true" styleClass="svdSrchTextAreaNew">
			     <html:options collection="authUsersVec" property="strid" labelProperty="name" />
				</html:select>
				</td>
			</tr>
		</table>
	</td>
	<td width="11"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="11"></td>
</tr>
<tr height="3"><td colspan="3"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="3"></td></tr>
<tr>
	<td width="11"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="11"></td>
	<td><span class="popupTableText"><bean:message key="label.search.deselectinstructions"/>.</span></td>
	<td width="11"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="11"></td>
</tr>
<tr><td colspan="3"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="7"></td></tr>
<tr>
	<td width="11"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="11"></td>
	<td><input type="button" class="addNewBtn" value="<bean:message key='label.administration.addnew'/>"></td>
	<td width="11"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="11"></td>
</tr>

<tr height="7"><td colspan="3"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="7"></td></tr>
<tr>
	<td width="11"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="11"></td>
	<td class="activityDivider"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="1"></td>
	<td width="11"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="11"></td>
</tr>
<tr height="7"><td colspan="3"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="7"></td></tr>

<!-- search builder table  -->
<tr height="20">
	<td width="11"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="11"></td>
	<td class="searchBuilder"><bean:message key="label.search.searchbuilder"/>
	<%
		if(searchCriteria.equals("invalid"))
		{
		}
	%>
	</td>
	<td width="11"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="11"></td>
</tr>
<tr>
	<td width="11"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="11"></td>
	<td width="100%">
		<table border="0" cellspacing="0" cellpadding="0" width="100%" id="search">
			<tr height="3"><td class="popupTableHeadShadow" colspan="6"><img src="<bean:message key='label.url.images' />/spacer.gif" height="3"></td></tr>
			<tr height="17">
				<td class="contactTableHeadText"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="1"><bean:message key="label.search.andor"/></td>
				<td class="contactTableHeadText"><bean:message key="label.search.tahle"/></td>
				<td class="contactTableHeadText"><bean:message key="label.search.field"/></td>
				<td class="contactTableHeadText"><bean:message key="label.search.condition"/></td>
				<td class="contactTableHeadText" colspan="2"><bean:message key="label.search.criteria"/></td>
			</tr>
			<tr height="1">
				<td class="contactTableHeadBottom" colspan="6"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1"></td>
			</tr>
<%

	String selJoin = null;
	String selTable = null;
	String selColumn = null;
	String selCondition = null;
	String strRmvBtnDisabled = "";

	for (int k = 0; k<noOfRecords; k++) {


	if(k == 0)
	strRmvBtnDisabled = "disabled";
	else
	strRmvBtnDisabled =" ";
%>
			<tr class="contactTableRowOdd" height="28">
<%
	if ( k >0)
	{
%>
			<td class="svdSrchTableEvenBegin">
				<select name="join<%=k+3%>" class="svdSrchAndDrp" onChange="setLastChanged('search',this,<%=k+3%>)" >
				<%

					for(int i=0;i<noOfJoins;i++)
					{
						if (sColl != null && sr[k] != null)
						{
						 	selJoin = sr[k].getJoin();
						}

						if(selJoin != null && joinAl.get(i)!= null && selJoin.equalsIgnoreCase((String)joinAl.get(i)))
						{
				%>
				<option value="<%=joinAl.get(i)%>" selected><%=joinAl.get(i)%></option>
				<%

						}
						else
						{
				%>
				<option value="<%=joinAl.get(i)%>"><%=joinAl.get(i)%></option>
				<%
						}
					}//for loop ends here.
				%>
				</select>
			</td>
<%
	} else {
%>
			<td class="svdSrchTableOddBegin">
			<img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="1">
			</td>
<%
	}
%>
				<td class="contactTableOdd">
				<select name="table<%=k+3%>" class="svdSrchColsDrp" onChange="setColumns('search',<%=k+3%>,this.value)">
				<%
					if (k == 0 && noOfRecords == 1)
					{
				%>
				<option selected value="none"><bean:message key="label.search.selecttable"/></option>
				<%
					}

					for(int i=0;i<noOfTables;i++)
					{

						if (sColl != null && sr[k] != null)
						{
						 	selTable = sr[k].getTable();
						}

						if(selTable != null && tableAl.get(i)!= null && selTable.equalsIgnoreCase((String)tableAl.get(i)))
						{
				%>

				<option selected value="<%=tableAl.get(i)%>"><%=tableAl.get(i)%></option>

				<%
						}
						else
						{
				%>

				<option value="<%=tableAl.get(i)%>"><%=tableAl.get(i)%></option>
				<%
						}
					}
				%>
				</select>
				</td>

				<td class="contactTableOdd">
				<select name="column<%=k+3%>" class="svdSrchColsDrp" onchange="setLastChanged('search',this,<%=k+3%>)" <%=isDisabled%>>
				<%

					if(selTable != null)
					{
						DataDictionary dd1 = new DataDictionary(dataSource);

						HashMap fhm1 = dd1.getFinalMapping();

						if(fhm1!= null)
						{
							HashMap hm1  = (HashMap)fhm1.get(keyTable);

							if(hm1 != null)
							{
								colVec = (Vector)hm1.get(selTable.trim());
								noOfColumns = colVec.size();
							} else
							{
								colVec = null;
								noOfColumns = 0;
							}
						} else
						{
							colVec = null;
							noOfColumns = 0;
						}

					}
					else
					{
						//Set some value to column select box
						colVec = null;
						noOfColumns = 0;
					}

					for(int i=0;i<noOfColumns;i++)
					{
						if (sColl != null && sr[k] != null)
						{
						 	selColumn = sr[k].getColumn();
						}

						if(selColumn != null && colVec != null && colVec.elementAt(i)!= null && selColumn.equalsIgnoreCase((String)colVec.elementAt(i)))
						{
				%>
				<option value="<%=colVec.elementAt(i)%>" selected><%=colVec.elementAt(i)%></option>
				<%
						}
						else if(selTable == null || colVec == null)
						{
				%>

				<option value=""></option>
				<%
						}
						else
						{
				%>
				<option value="<%=colVec.elementAt(i)%>" ><%=colVec.elementAt(i)%></option>
				<%
						}
					}
				%>
				</select>
				</td>
				<td class="contactTableOdd">
				<select name="condition<%=k+3%>" class="svdSrchCondDrp" onchange="setLastChanged('search',this,<%=k+3%>)" >
				<%

					for(int i=0;i<noOfConditions;i++)
					{
						if (sColl != null && sr[k] != null)
						{
						 	selCondition = sr[k].getCondition();
						}

						if(selCondition != null && condAl.get(i)!= null && selCondition.equalsIgnoreCase((String)condAl.get(i)))
						{
				%>
				<option value="<%=condAl.get(i)%>" selected><%=condAl.get(i)%></option>
				<%
						}
						else
						{
				%>
				<option value="<%=condAl.get(i)%>"><%=condAl.get(i)%></option>
				<%
						}
					}
				%>
				</select>
				</td>

				<td class="contactTableOdd">
				<%
					String criteria = "";

					if(sColl != null && sr[k] != null)
					{
						criteria = sr[k].getCriteria();
					}

					boolean flag = false;
					boolean flagForMv = false;

					if(k == 0)
					{
						flag = true;
					    if(noOfRecords == 1)
						flagForMv = true;
					}

				%>
				<input type=text name="criteria<%=k+3%>" class="svdSrchCriteriaTextBox" value="<%=criteria%>" onChange="setLastChanged('search',this,<%=k+3%>)"/ >

				</td>
				<td class="contactTableOdd" nowrap>
					<img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="2">
					<input type ="button" class="removeRowBtn" name="removeRow<%=k%>" value="<bean:message key='label.administration.removerow'/>" onclick="deleteRow(<%=k+3%>)" <%=strRmvBtnDisabled%>/>
					<img src='<bean:message key='label.url.images' />/spacer.gif' height='1' width='2'>
			 	 	<input class='moveUpBtn' type='button' value='<bean:message key="label.value.moveup"/>' onClick="moveDataUp(<%=k+3%>)" <%=strRmvBtnDisabled%>>
					<img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="2">
					<input class="moveDownBtn" type="button" value="<bean:message key='label.administration.movedown'/>" onClick="moveDataDown(<%=k+3%>)">
				</td>
			</tr>
<%
	} // end of for loop.
%>
			<tr class="contactTableRowOdd" height="28">
			  <td class="svdSrchTableOddBegin">&nbsp;</td>
			  <td class="contactTableOdd">
			    <html:button property="addRow" styleClass="addRowBtn" value="<bean:message key='label.administration.addrow'/>" onclick="add(2)" >
  				</html:button>
			  </td>
			  <td class="contactTableOdd">&nbsp;</td>
			  <td class="contactTableOdd">&nbsp;</td>
			  <td class="contactTableOdd">&nbsp;</td>
			  <td class="contactTableOdd" nowrap>&nbsp;</td>
		  </tr>
			<tr height="1">
				<td class="contactTableHeadBottom" colspan="6"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1"></td>
			</tr>
		</table>
	</td>
	<td width="11"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="11"></td>
</tr>
<tr height="11"><td colspan="3"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="11"></td></tr>

<!-- power edit -->
<tr>
	<td width="11"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="11"></td>
		<td>
			<span class="popupTableTextBold"><bean:message key="label.search.poweredit"/>:
			</span><br>
			<html:textarea property="ps" styleClass="powerEditTextArea" value="<%=ps%>"  onchange="setLastChanged('ps',this,-1)">
			</html:textarea>
			<br>
			<img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="3"><br>
			<input type=button name="update" class="addNewBtn" value="<bean:message key='label.administration.update'/>" onclick="updateTextBox(this,<%=strTableId%>,<%=strUserId%>,<%=listid%>,'<%=tbl%>')">
		</td>
	<td width="11"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="11"></td>
</tr>

<tr class="popupTableRow" height="7"><td colspan="3"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="7"></td></tr>
<tr>
	<td width="11"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="11"></td>
	<td class="activityDivider"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="1"></td>
	<td width="11"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="11"></td>
</tr>
<tr class="popupTableRow" height="7"><td colspan="3"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="7"></td></tr>

<!-- button row -->
<tr>
	<td width="11"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="11"></td>
	<td align="right">
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
				<input type=button class="saveCloseBtn" name = "saveApply" value="<bean:message key='label.administration.saveandapply'/>" onclick="updateTextBox(this,<%=strTableId%>,<%=strUserId%>,<%=listid%>,'<%=tbl%>')" >
				</td>
				<td><img src="<bean:message key='label.url.images' />/spacer.gif" width="4" height="1"></td>
				<td>
				<input type=button class="saveCreateNewBtn" name="saveCreateNew" value="<bean:message key='label.administration.saveandcreatenew'/>"  onclick="updateTextBox(this,<%=strTableId%>,<%=strUserId%>,<%=listid%>,'<%=tbl%>')">
				</td>
				<td width="13"><img src="<bean:message key='label.url.images' />/spacer.gif" width="13" height="1"></td>
				<td width="1"><img src="<bean:message key='label.url.images' />/popup_vert_divider.gif" height="17" width="1"></td>
				<td width="13"><img src="<bean:message key='label.url.images' />/spacer.gif" width="13" height="1"></td>
				<td>
				<html:reset property="Reset Fields" styleClass="resetFieldsBtn" value="<bean:message key='label.administration.resetfields'/>" onclick="resetFields()">
				</html:reset>
				</td>
				<td><img src="<bean:message key='label.url.images' />/spacer.gif" width="4" height="1"></td>
				<td><input class="popupButton" type="button" value="<bean:message key='label.administration.cancel'/>" onClick="cancelSearch(<%=strTableId%>,<%=strUserId%>,<%=listid%>,'<%=tbl%>')"></td>
			</tr>
		</table>
	</td>
	<td width="11"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="11"></td>
</tr>
<tr height="100%">
	<td colspan="3" height="100%"><img src="<bean:message key='label.url.images' />/spacer.gif" height="1" width="11"></td></tr>
</table>
<!-- END main content area -->
</html:form>

<script>
function updateTextBox(obj,tableid,uid,listid,tbl)
{
	var changed  = "yes";
	var len = document.searchform.access.options.length;
	for(var k=0;k<len;k++)
	{
		if(document.searchform.access.options[k] != null)
		{
			document.searchform.access.options[k].selected = true;
		}
	}

	var url = null;
	var actionType = obj.name;
	var lastChanged = document.searchform.lastChanged.value;
	var searchName  = document.searchform.searchName.value;

	var noOfRows = document.searchform.noOfRows.value;
	var flag = "true";

	for( var k=3 ;k<=rowsAddedTill;k++)
	{
		var tblObj  = eval("document.searchform.table"+k);

		if(tblObj != null &&  tblObj != "undefined")
		{
			if(tblObj.value == "none")
			{
				flag = "false";
			}
		}
	}

	if(lastChanged == "none")
	{
		alert("<bean:message key='label.alert.nochangestosave'/>!!");
		changed = "no";
	}

	if(flag == "false")
	{
		alert("<bean:message key='label.alert.selecttableorremoverow'/> !!");
	}
	else
	{
		if(searchName.length == 0 )
		alert("<bean:message key='label.alert.entersearchname'/>");
		else
		{
			url = "<bean:message key='label.url.root' />/Search.do?&actionType="+actionType+"&tableid="+tableid+"&individualid="+uid+"&listid="+listid+"&table="+tbl+"&changed="+changed;
			document.searchform.action = url;
			document.searchform.submit();
		}
	}
}

function setLastChanged(lastChanged,obj,rowid)
{
//	alert("rowid = "+rowid)
/*
	var fieldName = "x";
	var crtName = "y";

	if( obj != null && obj != "undefined")
	fieldName = obj.name;

	if(rowid != null && rowid != "undefined")
	crtName = "criteria"+rowid;

	if( fieldName == crtName )
	{
		rowid = rowid + 1;
		var joinObj = eval("document.searchform.join"+rowid);
		if(joinObj != null && joinObj != "undefined")
		joinObj.disabled = false;

		var tblObj  = eval("document.searchform.table"+rowid);
		if(tblObj  != null && tblObj  != "undefined")
		tblObj.disabled = false;

		var condObj = eval("document.searchform.condition"+rowid);
		if(condObj != null && condObj != "undefined")
		condObj.disabled = false;

		var crtObj  = eval("document.searchform.criteria"+rowid);
		if(crtObj  != null && crtObj != "undefined")
		crtObj.disabled = false;
	}
*/
	document.searchform.lastChanged.value = lastChanged;
}

function setColumns(lastChanged,rowid,val)
{
	document.searchform.lastChanged.value = lastChanged;

	var colObj  = eval("document.searchform.column"+rowid);
	var condObj = eval("document.searchform.condition"+rowid);
	var crtObj  = eval("document.searchform.criteria"+rowid);

	var colCnt  = eval("document.searchform.colCntFor"+val);
	var colTbl;

	colObj.disabled  = false;
	condObj.disabled = false;
	crtObj.disabled  = false;

	var len = 0;

	if(colObj!= null)
	{
		var len = colObj.length;
	}

	for(var i= 0 ;i<len;i++)
	{
		if(colObj.options[i] != null)
		{
			colObj.options[i] = null;
		}
		else
		{
			if(colObj.options[0] != null)
			{
				//alert("0th value"+colObj.options[0].value);
				colObj.options[0] = null;
			}
		}
	}

	if ( colCnt != null && colCnt != "undefined")
	{
		for(var i=0;i<colCnt.value;i++)
		{
			colTbl = eval("document.searchform."+val+"col"+i);
			colObj.options[i] = new Option(colTbl.value,colTbl.value,false,false);
		}
	}
	else
	{

	}
}

var indexForRow = 0;
var rowsAddedTill = parseInt(document.searchform.rowsAddedTill.value);

function add(rowId)
{

	rowsAddedTill = parseInt(document.searchform.rowsAddedTill.value);

	//alert("rowsAddedTill in add"+rowsAddedTill);
	rowsAddedTill = parseInt(rowsAddedTill) + 1;
	document.searchform.rowsAddedTill.value = rowsAddedTill;

	//Assume that on click of Add lastChanged is search
	document.searchform.lastChanged.value = 'search';

	var noOfRows = document.searchform.noOfRows.value;
	var noOfTables  = document.searchform.tblCount.value;
 	//var noOfColumns = document.searchform.colCount.value;
	var noOfColumns = 0;
 	var noOfConditions = document.searchform.condCount.value;

	var tableId = document.getElementById("search");

	indexForRow = parseInt(noOfRows) + 2;

	/*if( parseInt(noOfRows) == 1)
	{
		indexForRow = 3;
		indexForRow = indexForRow + 1;

	} else
	{*/
		indexForRow = indexForRow + 1;
	//}

	var n =  parseInt(rowId) + parseInt(noOfRows);

	tableId.insertRow(n+1);

	//Adding row
	tableId.rows[n+1].className = 'contactTableRowOdd';
	tableId.rows[n+1].height = 28;
	tableId.rows[n+1].insertCell();

	//Adding 1st cell in above row ie n+1
	tableId.rows[n+1].cells[0].className = 'svdSrchTableEvenBegin';
	tableId.rows[n+1].cells[0].innerHTML =
	"<select name='join"+parseInt(rowsAddedTill)+"' class='svdSrchAndDrp' onchange= \"setLastChanged(\'search\',this,"+noOfRows+") \" >"+
	"<option selected value='AND'>AND </option> <option value='OR'>OR </option></select>";

	tableId.rows[n+1].insertCell();

	//Adding 2nd cell in above row ie n+1
	tableId.rows[n+1].cells[1].className = 'contactTableOdd';

	var innHtml = " <select name='table"+parseInt(rowsAddedTill)+"' class='svdSrchColsDrp' onChange=\"setColumns(\'search\',"+parseInt(rowsAddedTill)+",this.value) \" >";

	//By default add this
	innHtml = innHtml + "<option value='none' selected>Select Table</option>";

	for (var i=0;i<noOfTables;i++)
	{
		var tbl = (eval("document.searchform.tbl"+i)).value;

		innHtml = innHtml + "<option value='"+tbl+"'>"+tbl+"</option>";
	}

	innHtml = innHtml +"</select>" ;

	tableId.rows[n+1].cells[1].innerHTML = innHtml;
	tableId.rows[n+1].insertCell();

	//Adding 3rd cell in above row ie n+1
	tableId.rows[n+1].cells[2].className = 'contactTableEven';

	innHtml = " <select name='column"+parseInt(rowsAddedTill)+"' class='svdSrchColsDrp' onchange=\"setLastChanged(\'search\',this,"+noOfRows+") \" >";
	for (var i=0;i<noOfColumns;i++)
	{
		//var column = (eval("document.searchform.col"+i)).value;
		var column = "";
		innHtml = innHtml + "<option value='"+column+"'>"+column+"</option>";
	}

	innHtml = innHtml +"</select>";

	tableId.rows[n+1].cells[2].innerHTML = innHtml;
	tableId.rows[n+1].insertCell();

	//Adding 4th cell in above row ie n+1
	tableId.rows[n+1].cells[3].className = 'contactTableEven';

	innHtml = " <select name='condition"+parseInt(rowsAddedTill)+"' class='svdSrchCondDrp' onchange=\"setLastChanged(\'search\',this,"+noOfRows+")\" >";
	for (var i=0;i<noOfConditions;i++)
	{
		var cond = (eval("document.searchform.cond"+i)).value;
		innHtml = innHtml + "<option value='"+cond+"'>"+cond+"</option>";
	}

	innHtml = innHtml +"</select>";

	tableId.rows[n+1].cells[3].innerHTML = innHtml;
	tableId.rows[n+1].insertCell();

	//Adding 5th cell in above row ie n+1
	tableId.rows[n+1].cells[4].className = 'contactTableEven';
	innHtml = "<input type=text class='svdSrchCriteriaTextBox' name='criteria"+parseInt(rowsAddedTill)+"' value='' onchange=\"setLastChanged(\'search\',this,"+noOfRows+") \" >";
	tableId.rows[n+1].cells[4].innerHTML = innHtml;
	tableId.rows[n+1].insertCell();

	//Adding 6th cell in above row ie n+1

	//alert("indexForRow is "+indexForRow);
	tableId.rows[n+1].cells[5].className = 'contactTableOdd';
	tableId.rows[n+1].cells[5].wrap = "off";
	innHtml = "&nbsp;<img src='<bean:message key='label.url.images' />/spacer.gif' height='1' width='2'>"+
			  "<input class='removeRowBtn' type='button' name='removeRow"+noOfRows+"' value='<bean:message key='label.administration.removerow'/>' onClick='deleteRow("+indexForRow+")'> "+
			  "<input type='hidden' name='indexForRow"+indexForRow+"' value='"+indexForRow+"'>"+
			  "<img src='<bean:message key='label.url.images' />/spacer.gif' height='1' width='2'> "+
			  "<input class='moveUpBtn' type='button' value='<bean:message key='label.administration.moveup'/>' onClick='moveDataUp("+rowsAddedTill+")' >"+
			  "<img src='<bean:message key='label.url.images' />/spacer.gif' height='1' width='2'> &nbsp;"+
			  "<input class='moveDownBtn' type='button' value='<bean:message key='label.administration.movedown'/>' onClick='moveDataDown("+rowsAddedTill+")'>";
	tableId.rows[n+1].cells[5].innerHTML = innHtml;
	//alert("inner html for 5th cell "+innHtml);
	tableId.rows[n+1].insertCell();


	//This is to set rowId ie after how many rows of table u want
	//to add a new row.
	//rowId = parseInt(rowId) + parseInt(noOfRows);

	noOfRows = parseInt(noOfRows)+1;
	document.searchform.noOfRows.value = noOfRows;

}


function deleteRow(rowid)
{
	//Assume that on click of Add lastChanged is search
	document.searchform.lastChanged.value = 'search';
	var tableId = document.getElementById("search");

	var noOfRows = document.searchform.noOfRows.value
	var n = parseInt(rowid);

	//alert("rowid = "+rowid);
	if(parseInt(noOfRows) == 1)
	{
		alert("<bean:message key='label.alert.recordcannotberemoved'/>");
	}
	else
	{
		var tableId=document.getElementById("search");
		var obj = eval("document.searchform.indexForRow"+rowid);

		if( obj != null && obj != "undefined")
		n = obj.value;

		//alert("n = "+n);
		tableId.deleteRow(n);

		for ( var k = rowid +1 ;k<=rowsAddedTill;k++)
		{
			obj = eval("document.searchform.indexForRow"+k);
			if( obj != null && obj != "undefined")
			{
				var index = obj.value;
				index = index - 1;
				obj.value = index;
			}
		}
		//This is to set rowId ie after u delete a row.
		noOfRows = parseInt(noOfRows) - 1;
		document.searchform.noOfRows.value = noOfRows;
		indexForRow = indexForRow -1;

	}

}

function moveDataDown(rowid)
{
	var index = 1;
	//Assume that on click of Add lastChanged is search
	document.searchform.lastChanged.value = 'search';

	var noOfRows = document.searchform.noOfRows.value

	//alert("rowsAddedTill in moveDown"+rowsAddedTill);

	if (parseInt(rowid) == parseInt(rowsAddedTill))
	alert("<bean:message key='label.alert.datacannotmovedown'/>!!");
	else
	{
		//alert("rowid ="+ rowid);

		for(var k = rowid+1;k<=rowsAddedTill;k++)
		{
			var obj = eval("document.searchform.table"+k);
			if(obj != null && obj!= "undefined")
			{
				index = k;
				break;
			}
		}

		//alert("index = "+index);

		var botTblObj = eval("document.searchform.table"+index);
		var curTblObj = eval("document.searchform.table"+parseInt(rowid));

		if( curTblObj != null && curTblObj != "undefined")
		{
			if(botTblObj != null && botTblObj != "undefined")
			{
				var temp = botTblObj.value;
				botTblObj.value = curTblObj.value;
				curTblObj.value = temp;
			}
		}

		//For column
		var botColObj = eval("document.searchform.column"+index);
		var curColObj = eval("document.searchform.column"+parseInt(rowid));

		var botLen = 0;
		var curLen = 0;

		if (botColObj != null && botColObj != "undefined")
		botLen = botColObj.length;

		if (curColObj != null && curColObj != "undefined")
		curLen = curColObj.length;

		var botValues = new Array(botLen);
		for(var t=0;t<botLen;t++)
		{
			if(botColObj.options[t] != null && botColObj.options[t] != "undefined")
			{
				var tempB = botColObj.options[t].value;
				botValues[t] = tempB;

				botColObj.options[t].text  = "";
				botColObj.options[t].value = "";
			}
		}

		//if (botColObj != null && botColObj != "undefined")
		//botLen = botColObj.length;

		for(var t=0;t<botLen;t++)
		{
			botColObj.options[t] = null;
		}

		var curValues = new Array(curLen);
		for(var t=0;t<curLen;t++)
		{
			if(curColObj.options[t] != null && curColObj.options[t] != "undefined")
			{
				var tempC = curColObj.options[t].value;
				curValues[t] = tempC;
				curColObj.options[t].text = "";
				curColObj.options[t].value = "";
			}
		}

		//if (curColObj != null && curColObj != "undefined")
		//curLen = curColObj.length;

		for(var t=0;t<curLen;t++)
		{
			curColObj.options[t] = null;
		}

		for(var k=0;k<curLen;k++)
		{
			if(botColObj[k] != null)
			{
				botColObj[k].value = curValues[k];
				botColObj[k].text = curValues[k];
			}
			else
			{
				botColObj.options[k] = new Option(curValues[k],curValues[k],false,false);
			}
		}
		for(var k=0;k<botLen;k++)
		{
			if(curColObj[k] != null)
			{
				curColObj[k].text = botValues[k];
				curColObj[k].value = botValues[k];
			}
			else
			{
			    curColObj.options[k] = new Option(botValues[k],botValues[k],false,false);
			}
		}
		//For column ends here

		//For condition
		var botCondObj = eval("document.searchform.condition"+index);
		var curCondObj = eval("document.searchform.condition"+parseInt(rowid));

		if( curCondObj != null && curCondObj != "undefined")
		{
			if(botCondObj != null && botCondObj != "undefined")
			{
				var temp = botCondObj.value;
				botCondObj.value = curCondObj.value;
				curCondObj.value = temp;
			}
		}
		//For condition ends here

		//For criteria
		var botCrtObj = eval("document.searchform.criteria"+index);
		var curCrtObj = eval("document.searchform.criteria"+parseInt(rowid));

		//alert("botCrtObj is = "+botCrtObj);
		//alert("curCrtObj is = "+curCrtObj);

		if( curCrtObj != null && curCrtObj != "undefined")
		{
			//alert("cur value = "+curCrtObj.value );
			if(botCrtObj != null && botCrtObj != "undefined")
			{
				//alert("bot value = "+botCrtObj.value);
				var temp = botCrtObj.value;
				botCrtObj.value = curCrtObj.value;
				curCrtObj.value = temp;
			}
		}
		//For criteria ends here

		//For join
		var botJoinObj = eval("document.searchform.join"+index);
		var curJoinObj = eval("document.searchform.join"+parseInt(rowid));

		if( curJoinObj != null && curJoinObj != "undefined")
		{
			if(botJoinObj != null && botJoinObj != "undefined")
			{
				var temp = botJoinObj.value;
				botJoinObj.value = curJoinObj.value;
				curJoinObj.value = temp;
			}
		}
		//For Join ends here
	}

}

function moveDataUp(rowid)
{
	//Assume that on click of Add lastChanged is search
	document.searchform.lastChanged.value = 'search';
	var index = 0;

	if (parseInt(rowid) == 3 )
	alert("<bean:message key='label.alert.datacannotmoveup'/> !!");
	else
	{
		//alert("rowid ="+ rowid);

		for(var k = (parseInt(rowid) - 1);k >= 3;k--)
		{
			var obj = eval("document.searchform.table"+k);
			if(obj != null && obj!= "undefined")
			{
				index = k;
				break;
			}
		}

		//alert("index = "+index);

		//For table
		var upTblObj = eval("document.searchform.table"+index);
		var curTblObj = eval("document.searchform.table"+parseInt(rowid));

		if( curTblObj != null && curTblObj != "undefined")
		{
            //alert(" curTblObj value is " +curTblObj.value);
			if(upTblObj != null && upTblObj != "undefined")
			{
	            //alert(" upTblObj.value is " +upTblObj.value);

				var temp = upTblObj.value;
				upTblObj.value = curTblObj.value;
				curTblObj.value = temp;
			}
		}
		//For table ends here.

		//For column
		var upColObj = eval("document.searchform.column"+index);
		var curColObj = eval("document.searchform.column"+parseInt(rowid));

		var upLen = 0;
		var curLen = 0;

		if ( upColObj != null && upColObj != "undefined")
		upLen = upColObj.length;

		if ( curColObj != null && curColObj != "undefined")
		curLen = curColObj.length;

		var upValues = new Array(upLen);
		for(var t=0;t<upLen;t++)
		{
			if(upColObj.options[t] != null && upColObj.options[t] != "undefined")
			{
				var tempB = upColObj.options[t].value;
				upValues[t] = tempB;

			    upColObj.options[t].text  = "";
				upColObj.options[t].value = "";
			}
		}

		for(var t=0;t<upLen;t++)
		{
			//alert("<bean:message key='label.alert.setuptonull'/>");
			upColObj.options[t] = null;
		}

		var curValues = new Array(curLen);
		for(var t=0;t<curLen;t++)
		{
			if(curColObj.options[t] != null && curColObj.options[t] != "undefined")
			{
				var tempC = curColObj.options[t].value;
				curValues[t] = tempC;

				curColObj.options[t].text = "";
				curColObj.options[t].value = "";
			}
		}

		for(var t=0;t<curLen;t++)
		{
			//alert("<bean:message key='label.alert.setcurrenttonull'/>");
			curColObj.options[t] = null;
		}

		for(var k=0;k<curLen;k++)
		{
			if(upColObj[k] != null && upColObj[k] != "undefined")
			{
				upColObj[k].value = curValues[k];
				upColObj[k].text = curValues[k];
			}
			else
			{
				upColObj.options[k] = new Option(curValues[k],curValues[k],false,false);
			}
		}
		for(var k=0;k<upLen;k++)
		{
			if(curColObj[k] != null && curColObj[k] != "undefined")
			{
				curColObj[k].text = upValues[k];
				curColObj[k].value = upValues[k];
			}
			else
			{
			    curColObj.options[k] = new Option(upValues[k],upValues[k],false,false);
			}
		}

		//For column ends here

		//For condition
		var upCondObj = eval("document.searchform.condition"+index);
		var curCondObj = eval("document.searchform.condition"+parseInt(rowid));

		if( curCondObj != null && curCondObj != "undefined")
		{
			if(upCondObj != null && upCondObj != "undefined")
			{
				var temp = upCondObj.value;
				upCondObj.value = curCondObj.value;
				curCondObj.value = temp;
			}
		}
		//For condition ends here

		//For criteria
		var upCrtObj = eval("document.searchform.criteria"+index);
		var curCrtObj = eval("document.searchform.criteria"+parseInt(rowid));

		if( curCrtObj != null && curCrtObj != "undefined")
		{
			if(upCrtObj != null && upCrtObj != "undefined")
			{
				var temp = upCrtObj.value;
				upCrtObj.value = curCrtObj.value;
				curCrtObj.value = temp;
			}
		}
		//For criteria ends here

		//For join
		var upJoinObj = eval("document.searchform.join"+index);
		var curJoinObj = eval("document.searchform.join"+parseInt(rowid));

		if( curJoinObj != null && curJoinObj != "undefined")
		{
			if(upJoinObj != null && upJoinObj != "undefined")
			{
				var temp = upJoinObj.value;
				upJoinObj.value = curJoinObj.value;
				curJoinObj.value = temp;
			}
		}
		//For Join ends here
	}

}

function addToRight()
{
	document.searchform.lastChanged.value = 'ps';

	var len = document.searchform.access.options.length;
	var isRepeated = false;

	if (window.document.searchform.selectuser.selectedIndex == -1)
	{
		alert("<bean:message key='label.alert.selectaddtag'/>");
	}
	if (window.document.searchform.selectuser.selectedIndex >= 0)
	{
		var noOfUsers = window.document.searchform.selectuser.options.length;

		for (var ii =0;ii<noOfUsers;ii++)
		{
			if(window.document.searchform.selectuser.options[ii].selected == true)
			{
				var selValue = window.document.searchform.selectuser.options[ii].text;
				for(var  k=0;k<len;k++)
				{
					var prevValue = window.document.searchform.access.options[k].text;
					if(selValue == prevValue)
					{
						isRepeated = true;
						break;
					}
				}
			}
		}

		if(isRepeated == true)
		{
			alert("<bean:message key='label.alert.dataalreadyadded'/>!!");
		}
		else
		{
			for (var ii =0;ii<noOfUsers;ii++)
			{
				if(window.document.searchform.selectuser.options[ii].selected == true)
				{
					var index = ii;
					//alert("arr[kk] is "+arr[kk]);
					len = document.searchform.access.options.length;
					window.document.searchform.access.options[len] = new Option(window.document.searchform.selectuser.options[index].text,
					window.document.searchform.selectuser.options[index].value,
					"false","false");
				}
			}//if ends here.
		}//for loop ends here.
	}//outer if ends here.
}

function removeFromRight()
{
	document.searchform.lastChanged.value = 'ps';

	var len = document.searchform.selectuser.options.length;

	if (window.document.searchform.access.selectedIndex == -1)
	{
		alert("<bean:message key='label.alert.selectremovetag'/>");
	}
	if (window.document.searchform.access.selectedIndex >= 0)
	{
		var noOfUsers = window.document.searchform.access.options.length;
		var arr = new Array();
		var noOfSelUsers = 0;

		for (var ii =0;ii<noOfUsers;ii++)
		{
			if(window.document.searchform.access.options[ii].selected == true)
			{
				window.document.searchform.access.options[ii]=null;
				ii --;
				noOfUsers = window.document.searchform.access.options.length;
			}
		}
	}
}

function resetFields()
{
	var len = document.searchform.access.options.length;
	for(var k=0;k<len;k++)
	{
		if(document.searchform.access.options[k] != null)
		{
			if(document.searchform.access.options[k].selected == true)
			{
				//alert(k);
				document.searchform.access.options[k] = null;
				k--;
			}
		}
	}
}

function cancelSearch(tableid,uid,listid,tbl)
{
	var	url = "<bean:message key='label.url.root' />/Search.do?&actionType=new&tableid="+tableid+"&individualid="+uid+"&listid="+listid+"&table="+tbl;
	document.searchform.action = url;
	document.searchform.submit();
}

</script>