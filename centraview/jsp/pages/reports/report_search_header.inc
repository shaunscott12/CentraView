<%--
 * $RCSfile$    $Revision$  $Date$ - $Author$
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
<script language="javascript">
<!--
Table = function() {
	this.setID("Table"+(Table.Count++));
	this.tableid = 0;
	this.name = "";
	this.fullname = "";
        this.fieldid = [];
        this.fieldname = [];
};
Table.prototype.setID = function(id) {
	if (this.id) delete Table.all[this.id];
	this.id = id;
	Table.all[this.id] = this;
};
Table.prototype.addFieldId = function(c) {
	this.fieldid[this.fieldid.length] = c;
	return c;
};
Table.prototype.addFieldName = function(c) {
	this.fieldname[this.fieldname.length] = c;
	return c;
};
Table.Count = 0;
Table.all = [];
  <%
     int s = topTables.size();
  %>
  tables = new Array(<%=s%>);
  <%
     for (i = 0; i < s; i++)
     {
          TheTable table = (TheTable)topTables.get(i);
          ArrayList fields = (ArrayList)table.getFields();
  %>
          tables[<%=i%>] = new Table();
          tables[<%=i%>].tableid = <%=table.getId()%>;
          tables[<%=i%>].fullname = "<%=table.getFullName()%>";
  <%
        int s1 = fields.size();
        for (j = 0; j < s1; j++)
        { TheItem field = (TheItem)fields.get(j); %> tables[<%=i%>].addFieldId('<%=table.getId()%>:<%=field.getId()%>') ;  tables[<%=i%>].addFieldName("<%=field.getFullName()%>") ;  <% } } %>


function getTable(fieldid) {

   for (var i = 0; i < tables.length; i++)
   {
      len = tables[i].fieldid.length;
      for (var j = 0; j < len; j++)
      {
           if ( tables[i].fieldid[j] == fieldid )
              return tables[i];
      }
   }
   return null;
}

function getTableName(fieldid) {

   str = "";
   table = getTable(fieldid);
   if ( table != null)
       return table.fullname;
   else
       return str;
}

function getFieldName(fieldid) {

   for (i = 0; i < tables.length; i++)
   {
      len = tables[i].fieldid.length;
      for (j = 0; j < len; j++)
      {
           if ( tables[i].fieldid[j] == fieldid )
              return tables[i].fieldname[j];
      }
   }
   return null;
}

function setFields() 
{
	var tablesObj  = eval("document.forms[1].topTables");
	var fieldsObj  = eval("document.forms[1].topFields");
    var tableid;
    if (tablesObj != null)
	{
        for (i=0; i < tablesObj.options.length; i++)
        {
	        if (tablesObj.options[i].selected && i > 0 )
            {
	             tableid = tablesObj.options[i].value;
                 break;
	        }
        }
    }
    len = fieldsObj.options.length;
    for (i = len; i >= 0; --i)
    {
         fieldsObj.options[i] = null;
    }
    for (i = 0; i < tables.length; i++)
    {
        if (tableid == tables[i].tableid)
        {
            len = tables[i].fieldid.length;
            for (j = 0; j < len; j++)
            {
                text = tables[i].fieldname[j];
                value = tables[i].fieldid[j];
                newOption = new Option(text, value, false, false);
                fieldsObj.options[fieldsObj.length] = newOption;
            }
            break;
        }
    }
}

function moveField()
{
   {
      var topFields  = eval("document.forms[1].topFields");
      var selectedTopFields  = eval("document.forms[1].selectedTopFields");

      var tmp="";
      var fieldValue = "";
      var fieldText  = "";
      var selected=false;
      var k=-1;
      for ( var i = 0; i < topFields.options.length; i++ )
      {
         if (topFields.options[i].selected)
         {
            selected = true;
            fieldid = topFields.options[i].value;
            tableName = getTableName(fieldid);
            fieldName = getFieldName(fieldid);
            fieldValue = topFields.options[i].value;
            fieldText  = tableName+"."+fieldName;

             // check if entered  field already exists
             k = -1;
             for ( var j = 0; j < selectedTopFields.options.length; ++j )
             {
                tmp = selectedTopFields.options[j].text;
                if (fieldText == tmp)
                {
                   k = j;
                   alert("<bean:message key='label.alert.field'/> "+tmp+" <bean:message key='label.alert.alreadyexists'/>");
                   break;
                }
             }
             if (k == -1)
             {


            newOption = new Option(fieldText, fieldValue, false, false);
            selectedTopFields.options[selectedTopFields.length] = newOption;
            topFields.options[i].selected = false;
            }
         }
      }
      if (!selected)
         alert("<bean:message key='label.alert.selectfieldstoadd'/>");
   }
}

function removeField()
{
   {
       var topFields  = eval("document.forms[1].topFields");
       var selectedTopFields  = eval("document.forms[1].selectedTopFields");
       var tmp="";
       var fieldValue = "";
       var fieldText  = "";
       var selected=false;
       for( i = 0; i < selectedTopFields.options.length; i++)
       {
    	  if (selectedTopFields.options[i].selected)
    	  {
             selected = true;
             selectedTopFields.options[i].selected = false;
             selectedTopFields.options[i] = null;
             --i;
          }
       }
       if (!selected)
          alert("<bean:message key='label.alert.selectfieldstoremove'/>");
   }
}

function moveUp(Select) {
       var Selections  = eval("document.forms[1]."+Select);
       var obj;
       for( i = 0; i < Selections.options.length; i++ )
       {

       	  if (Selections.options[i].selected && i > 0 )
       	  {
             value = Selections.options[i - 1].value;
             text = Selections.options[i - 1].text;
             selected = Selections.options[i -1].selected;
             Selections.options[i - 1].value = Selections.options[i].value;
             Selections.options[i - 1].text = Selections.options[i].text;
             Selections.options[i - 1].selected=true;
             Selections.options[i].value = value;
             Selections.options[i].text = text;
             Selections.options[i].selected=selected;
          }
       }
}

function moveDown(Select) {
    var Selections  = eval("document.forms[1]."+Select);
    for( i = Selections.options.length-1; i >=0; --i )
    {
        if (Selections.options[i].selected && i < Selections.options.length - 1)
        {
             value = Selections.options[i + 1].value;
             text = Selections.options[i + 1].text;
             selected = Selections.options[i + 1].selected;
             Selections.options[i + 1].value = Selections.options[i].value;
             Selections.options[i + 1].text = Selections.options[i].text;
             Selections.options[i + 1].selected = true;
             Selections.options[i].value = value;
             Selections.options[i].text = text;
             Selections.options[i].selected = selected;
        }
    }
}

function moveToOrder()
{
      var selectedTopFields  = eval("document.forms[1].selectedTopFields");
      var sortOrder = eval("document.forms[1].sortOrder");
      var tmp="";
      var fieldValue = "";
      var fieldText  = "";
      var selected=false;
      var k=-1;
      for(i=0;i<selectedTopFields.options.length;i++)
      {
         if (selectedTopFields.options[i].selected)
         {
            selected=true;
            fieldValue = "A"+selectedTopFields.options[i].value;
            fieldText  = selectedTopFields.options[i].text;

             // check if entered field already exists
             k = -1;
             for(j=0;j<sortOrder.options.length;++j)
             {
                tmp = sortOrder.options[j].text;
                if (fieldText == tmp)
                {
                   k=j;
                   alert("<bean:message key='label.alert.field'/> "+tmp+" <bean:message key='label.alert.alreadyexists'/>");
                   break;
                }
             }
             if (k==-1)
             {


            newOption = new Option(fieldText, fieldValue, false, false);
            sortOrder.options[sortOrder.length] = newOption;

            selectedTopFields.options[i].selected = false;
            }
         }
      }
      if (!selected)
         alert("<bean:message key='label.alert.selectfieldstoadd'/>");
}

function removeFromOrder()
{
       var selectedTopFields  = eval("document.forms[1].selectedTopFields");
       var sortOrder = eval("document.forms[1].sortOrder");
       var selected=false;
       for(i=0;i<sortOrder.options.length;i++)
       {
    	  if (sortOrder.options[i].selected)
    	  {
             selected=true;
             sortOrder.options[i].selected = false;
             sortOrder.options[i] = null;
             --i;
          }
       }
       if (!selected)
          alert("<bean:message key='label.alert.selectsectionstoremove'/>");
}

function setRadio()
{
       var radio  = eval("document.forms[1].radiobutton");
       var sortOrder = eval("document.forms[1].sortOrder");
       var selected=false;
       for(i = 0; i < sortOrder.options.length; i++)
       {
    	  if (sortOrder.options[i].selected)
    	  {
             selected=true;
             tmpStr = sortOrder.options[i].value.substring(0, 1);
             tmpVal = sortOrder.options[i].value.substring(1, sortOrder.options[i].value.length)
             if (radio[0].checked == true) {
                sortOrder.options[i].value = "A"+tmpVal;
             } else {
                sortOrder.options[i].value = "D"+tmpVal;
             }
             break
          }
       }
       if (!selected)
          alert("<bean:message key='label.alert.selectfieldsinsortorders'/>");


}


function showRadio()
{
       var radio  = eval("document.forms[1].radiobutton");
       var sortOrder = eval("document.forms[1].sortOrder");
       for(i = 0; i < sortOrder.options.length; i++)
       {
    	  if (sortOrder.options[i].selected)
    	  {
             tmpStr = sortOrder.options[i].value.substring(0, 1);
             if (tmpStr == "D") {
                radio[0].checked = false;
                radio[1].checked = true;
             } else {
                radio[1].checked = false;
                radio[0].checked = true;
             }
             break
          }
       }

}

// end scripts for top

// scripts for bottom

var isIE = false;
if ((navigator.appName.indexOf("Microsoft")>-1) ||
    (navigator.appName.indexOf("MSIE")>-1)) {
   isIE = true;
}


// parse name from name+id
function getName(rowId) {
  var name="";

  i=0;
  while(i<rowId.length && isNaN(rowId.charAt(i))) {
    name+=rowId.charAt(i++);
  }
  return name;
}

// parse id from name+id
function getId(rowId) {
  var name="";

  var i=rowId.length-1;
  while(i>=0 && !isNaN(rowId.charAt(i))) {
    name=rowId.charAt(i--)+name;
  }
  return name;
}

// copy attributes
function copyAttribute(from,to) {
  str = "";
  for (var i in from){
    str += i + ": " + from.getAttribute(i)+";";
    try {
    to.setAttribute(i,from.getAttribute(i));
    }
    catch (err) {
       str +=err.description;
   }
  }
}

// copy select options
function copySelectOptions(from,to) {
   for (i=0; i < from.options.length; i++)
   {
       currentOption=document.createElement("option");
       currentOption.setAttribute("value",from.options[i].value);
       if (i==0) {
          currentOption.setAttribute("selected",true);
       }

       currenttext=document.createTextNode(from.options[i].text);
       currentOption.appendChild(currenttext);
       to.appendChild(currentOption);
   }
}

// copy input options
function copyInputAttributes(from,to) {
    to.setAttribute("type",from.getAttribute("type"));
    to.setAttribute("value",from.getAttribute("value"));
    to.setAttribute("style",from.getAttribute("style"));
}

function add(rowId) {

  var table = document.getElementById("search");

  var tableRows = table.getElementsByTagName("tr");
  var firstRowIndex = -1;
  var lastRowIndex = -1;
  var buttonAdd=null;
  var newRowId = 0;
  var currRowId = 0;
  for (i=0;i<tableRows.length;++i) {
      var tr = tableRows.item(i);
      var trId = tr.getAttribute("id");

      if ( trId == null ) continue;

      if (trId.match("searchRow")) {

         // first row of table search has found
         if (firstRowIndex == -1) {
            firstRowIndex=i;
         }
         else {
            lastRowIndex=i;
         }
         // find max rowId
         currRowId = getId(trId);
         if (currRowId > newRowId) {
            newRowId = currRowId;
         }
      }
      else {

        // find last row with button "Add Row"
        if (trId=="addRowButton") {
           buttonAdd = tr;
        }
      }
  }

  // get new row id
  if (lastRowIndex==-1) {
     lastRowIndex=firstRowIndex;
  }

  ++newRowId;

  mycurrent_row=document.createElement("TR");

  mycurrent_row.setAttribute("id","searchRow"+newRowId);

  // get last searchRow
  var lastRow = tableRows.item(lastRowIndex);

  // find selects
  var select = lastRow.getElementsByTagName("select");

  // create TD with selects
  for (k=0;k<select.length;++k) {
      currentItem =  select.item(k);

      // create new TD
      mycurrent_cell=document.createElement("TD");

      // create new SELECT
      currentSelect=document.createElement("select");

      // copy classname
      currentSelect.className = currentItem.className;

      // copy OPTIONS
      copySelectOptions(currentItem,currentSelect);

      // set SELECT to TD
      mycurrent_cell.appendChild(currentSelect);

      name = getName(currentItem.getAttribute("name"));

      // set new name
      currentSelect.setAttribute("name",name+newRowId);

      // set new id
      currentSelect.setAttribute("id",name+newRowId);

      if (name.match("join")) {

         // set TD class
         mycurrent_cell.className = 'svdSrchTableEvenBegin';
         // set SELECT class
      }else {
         // set TD class
         mycurrent_cell.className = 'contactTableOdd';
         // set SELECT class
         if (name.match("bottomTables")) {
            // set onCLick handler
            if (isIE) {
	        currentSelect.setAttribute("onchange",new Function("setBottomFields('"+newRowId+"');"));
            }
            else {
               currentSelect.setAttribute("onchange","setBottomFields('"+newRowId+"');");
            }
         }
      }
      // set width

      currentSelect.style.width=currentItem.style.width;

      // set TD to TR
      mycurrent_row.appendChild(mycurrent_cell);

  }

  // find inputs
  select = lastRow.getElementsByTagName("input");

  // create TD with inputs
  for (k=0;k<select.length;++k) {
      currentItem =  select.item(k);

      // create new TD
      mycurrent_cell=document.createElement("TD");

      // create new INPUT
      currentSelect=document.createElement("INPUT");

      // copy classname
      currentSelect.className = currentItem.className;

      // copy OPTIONS
      copyInputAttributes(currentItem,currentSelect);

      // set INPUT to TD
      mycurrent_cell.appendChild(currentSelect);

      // set TD class for input value
      name = getName(currentItem.getAttribute("name"));

      type = currentItem.getAttribute("type");

      if (type=="button") {
         // set onClick property

         if (name.match("remove")) {

            if (isIE) {
	        currentSelect.setAttribute("onclick",new Function("deleteRowF('"+newRowId+"');"));
            }
            else {
               currentSelect.setAttribute("onclick","deleteRowF('"+newRowId+"');");
            }
         }
         else
         if (name.match("moveRowDown")) {

            if (isIE) {
               currentSelect.setAttribute("onclick",new Function("moveRowDownF('"+newRowId+"');"));
            }
            else {
                currentSelect.setAttribute("onclick","moveRowDownF('"+newRowId+"');");
            }
            // last row cant move down
            currentSelect.style.visibility='hidden';
         }
         else
         if (name.match("moveRowUp")) {
            if (isIE) {
               currentSelect.setAttribute("onclick",new Function("moveRowUpF('"+newRowId+"');"));
            }
            else {
                currentSelect.setAttribute("onclick","moveRowUpF('"+newRowId+"');");
            }
         }

         // set TD class
           mycurrent_cell.className = 'contactTableOdd';

         // set BUTTON class
      } else {
         // set TD class
         mycurrent_cell.className = 'contactTableOdd';

         // set TEXT class
         currentSelect.className = 'svdSrchCriteriaTextBox';

      }
      // set width
      currentSelect.style.width=currentItem.style.width;

      // set new name
      currentSelect.setAttribute("name",name+newRowId);

      // set new id
      currentSelect.setAttribute("id",name+newRowId);

      // set TD to TR
      mycurrent_row.appendChild(mycurrent_cell);

  }

  if (buttonAdd==null) {

     table.tBodies[0].appendChild(mycurrent_row);
  }
  else {

     table.tBodies[0].insertBefore(mycurrent_row,buttonAdd);

  }

  setVisibility(tableRows);
  return;
}

function deleteRowF(rowid)
{
  var table = document.getElementById("search");
  var tableRows = table.getElementsByTagName("tr");
  var rowToRemove=null;
  var noOfRows = 0;
  for ( i = 0; i < tableRows.length; ++i) {
      var tr = tableRows.item(i);
      var trId = tr.getAttribute("id");
      if ( trId == null ) continue;
      if (trId.match("searchRow")) {
         ++noOfRows;
      }
  }
  if (noOfRows == 1)
  {
     alert("<bean:message key='label.alert.recordcannotberemoved'/>");
     return;
  }

  rowToRemove = document.getElementById("searchRow"+rowid);
  if (rowToRemove!=null) {
     table.tBodies[0].removeChild(rowToRemove);
     // set visibility for row controls
     setVisibility(tableRows);
  }
  return;
}

function moveRowDownF(rowid)
{
  var table = document.getElementById("search");

  var tableRows = table.getElementsByTagName("tr");
  var rowToMoveDown=null;
  var rowToMoveUp=null;
  var noOfRows = 0;
  var wantNext = false;
  for (i=0;i<tableRows.length;++i) {
      var tr = tableRows.item(i);
      var trId = tr.getAttribute("id");

      if ( trId == null ) continue;

      if (trId.match("searchRow")) {
         ++noOfRows;
         if (trId == "searchRow"+rowid)  {
            rowToMoveDown = tr;
            wantNext = true;
            continue;
         }
         if (wantNext) {
            rowToMoveUp = tr;
            wantNext = false;
         }
      }
  }

  if (noOfRows == 1)
  {
     alert("<bean:message key='label.alert.recordcannotmovedown'/>");
     return;
  }

  if (rowToMoveDown!=null && rowToMoveUp!=null) {
     table.tBodies[0].replaceChild(rowToMoveDown,rowToMoveUp);
     table.tBodies[0].insertBefore(rowToMoveUp,rowToMoveDown);
     // set visibility for row controls
     setVisibility(tableRows);
  }
  return;

}

function moveRowUpF(rowid)
{
  var table = document.getElementById("search");

  var tableRows = table.getElementsByTagName("tr");
  var rowToMoveDown=null;
  var rowToMoveUp=null;
  var noOfRows = 0;
  var wantPrevious = true;
  for (i=0;i<tableRows.length;++i) {
      var tr = tableRows.item(i);
      var trId = tr.getAttribute("id");

      if ( trId == null ) continue;

      if (trId.match("searchRow")) {
         ++noOfRows;
         if (trId == "searchRow"+rowid)  {
            rowToMoveUp = tr;
            wantPrevious = false;
            continue;
         }
         if (wantPrevious) {
            rowToMoveDown = tr;
         }

      }
  }

  if (noOfRows == 1)
  {
     alert("<bean:message key='label.alert.recordcannotmoveup'/>");
     return;
  }

  if (rowToMoveDown!=null && rowToMoveUp!=null) {
     table.tBodies[0].replaceChild(rowToMoveDown,rowToMoveUp);
     table.tBodies[0].insertBefore(rowToMoveUp,rowToMoveDown);
     // set visibility for row controls
     setVisibility(tableRows);
  }
  return;
}

// set visibility of join select box, button moveDown, button moveUp(first row)
//                   moveDown(last row)
function setVisibility(tableRows)
{
  var firstRowIndex = -1;
  var lastRowIndex = -1;
  var lastId = 0;
  var id =0;
  var element = null;
  for (i=0;i<tableRows.length;++i) {
      var tr = tableRows.item(i);
      var trId = tr.getAttribute("id");

      if ( trId == null ) continue;

      if (trId.match("searchRow")) {

         id = getId(trId);

         lastId = id;

         // first row of table search has found
         if (firstRowIndex == -1) {
            firstRowIndex=i;
            // set visibility of first row controls
            element = document.getElementById("join"+id);
            if (element != null) {
               element.style.visibility='hidden';
            }
            element = document.getElementById("remove"+id);
            if (element != null) {
               element.style.visibility='visible';
            }
            element = document.getElementById("moveRowDown"+id);
            if (element != null) {
               element.style.visibility='visible';
            }
            element = document.getElementById("moveRowUp"+id);
            if (element != null) {
               element.style.visibility='hidden';
            }
         }
         else {
            lastRowIndex=i;
            // set visibility of non first row controls
            element = document.getElementById("join"+id);
            if (element != null) {
               element.style.visibility='visible';
            }
            element = document.getElementById("remove"+id);
            if (element != null) {
               element.style.visibility='visible';
            }
            element = document.getElementById("moveRowDown"+id);
            if (element != null) {
               element.style.visibility='visible';
            }
            element = document.getElementById("moveRowUp"+id);
            if (element != null) {
               element.style.visibility='visible';
            }
         }
      }
  }

  // change visibility for last row
  element = document.getElementById("moveRowDown"+lastId);
  if (element != null) {
     element.style.visibility='hidden';
  }
  if (lastRowIndex==-1) {
     // we  have only one row, change visibility of remove button
     element = document.getElementById("remove"+lastId);
     if (element != null) {
        element.style.visibility='hidden';
     }
  }
}

function openSelectDateTime()
{
  var startDate = document.getElementById('startmonth').value + "/" + document.getElementById('startday').value + "/" + document.getElementById('startyear').value;
  if (startDate == "//"){ startDate = ""; }
  var endDate = document.getElementById('endmonth').value + "/" + document.getElementById('endday').value + "/" + document.getElementById('endyear').value;
  if (endDate == "//"){ endDate = ""; }
  c_openWindow('/calendar/select_date_time.do?dateTimeType=3&startDate='+startDate+'&endDate='+endDate+'&startTime=&endTime=&endDateTitle=To&startDateTitle=From', 'selectDateTime', 350, 500, '');
}
/**
 * Called by the /calendar/select_date_time.do popup, this function takes
 * input from that screen via the function parameters, and populates the
 * form fields on this JSP for the Estimated Close Date field.
 * DO NOT CHANGE THE NAME OR SIGNATURE OF THIS FUNCTION!!!!
 */
function setDateTimeFromPopup(startDate, endDate, startTime, endTime)
{
  var startDateArray = startDate.split("/");
  if (startDateArray == null || startDateArray.length < 3) {
    document.getElementById('startmonth').value = "";
    document.getElementById('startday').value = "";
    document.getElementById('startyear').value = "";
  }else{
    document.getElementById('startmonth').value = startDateArray[0];
    document.getElementById('startday').value = startDateArray[1];
    document.getElementById('startyear').value = startDateArray[2];
  }
  var endDateArray = endDate.split("/");
  if (endDateArray == null || endDateArray.length < 3) {
    document.getElementById('endmonth').value = "";
    document.getElementById('endday').value = "";
    document.getElementById('endyear').value = "";
  }else{
    document.getElementById('endmonth').value = endDateArray[0];
    document.getElementById('endday').value = endDateArray[1];
    document.getElementById('endyear').value = endDateArray[2];
  }
  return(true);
}

//-->
</script>

