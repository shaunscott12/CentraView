
function secNavRollOn(element){
	element.style.borderColor = "#FFFFFF";
}

function secNavRollOff(element){
	element.style.borderColor = "#FFE994";
}

// START COPY HERE

var menuClicked = false;
var prevText;
var arrMenu = [];
arrMenu[0] = "menuAddl";
arrMenu[1] = "menuView";
arrMenu[2] = "menuPageTop";
arrMenu[3] = "menuPageBottom";
arrMenu[4] = "MoveToTop";
arrMenu[5] = "MoveToBottom";

document.onclick = function(e) {
	if (document.all) {
		var el = window.event.srcElement;
		if (el) {
			var isMenu = el.getAttribute("menu");
			if (isMenu!="true") showDropdownMenu();
		}
	} else {
		if (e) {
			var el = e.target;
			if (el) {
				var isMenu = el.getAttribute("menu");
				if (isMenu!="true") showDropdownMenu();
			}
		}
	}
}

function pgRollOn(element){
	element.style.color = "#334a60";
}

function pgRollOff(element){
	element.style.color = "#937810";
}

function dvRollOn(element){
	element.style.color = "#937810";
}

function dvRollOff(element){
	element.style.color = "#4a6e9f";
}

function tabsRollOn(element){
	element.style.color = "#E1D39A";
}

function tabsRollOff(element){
	element.style.color = "#A2B3BF";
}

function iconRollOn(text){
	document.getElementById("iconAltText").innerHTML=text;
}

function iconRollOut(){
	document.getElementById("iconAltText").innerHTML="";
}
function secNavRollOn(element){
	element.style.borderColor = "#FFFFFF";
}
function secNavRollOff(element){
	element.style.borderColor = "#FFE994";
}

function ddRollOn(element){
	element.style.backgroundColor = "#244961";
}
function ddRollOff(element){
	element.style.backgroundColor = "#17283c";
}

function dd2RollOn(element){
	element.style.color = "#937810";
}
function dd2RollOff(element){
	element.style.color = "#334a60";
}

function dd3RollOn(element){
	element.style.color = "#334a60";
}
function dd3RollOff(element){
	element.style.color = "#937810";
}
function dd4RollOn(element){
	element.style.backgroundColor = "#244961";
}
function dd4RollOff(element){
	element.style.backgroundColor = "#345788";
}
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}
	
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}
	
function MM_findObj(n, d) { //v3.0
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document); return x;
}
	
function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

function selectRow(element, isOdd){
	if (element.id!="headCheck") {
		var isSelected=element.checked;
		var cssName="contactTableRowSelected";
		var cssNameCheck="contactTableCheckSelected";
		if(element.id=="bottomCheck") cssNameCheck="contactTableCheckBottomSelected";
		if(!isSelected){
			if (isOdd) cssName = "contactTableRowOdd";
			else cssName = "contactTableRowEven";
			cssNameCheck="contactTableCheck";
			if(element.id=="bottomCheck") cssNameCheck="contactTableCheckBottom";
		}
		var objTR=element.parentNode.parentNode;
		if(objTR) objTR.className=cssName;
		var objTD=element.parentNode;
		if(objTD) objTD.className=cssNameCheck;
	}
}

function gotoSearch(index){
	if (index == 6) location.href = "/search/index.html";
}

function gotoActivity(index){
	if (index == 2) openPopup("schedule.html");
}

function openPopup(page, popW, popH){
//	window.open(page, "", "width=" + popW + ", height=" + popH)
	window.open(page, "", "width=780, height=580")
}

function openWindow(page, name, width, height, windowOptions){
  if (windowOptions == ''){
    windowOptions = 'scrollbars=yes, resizable=yes';
  }
  window.open(page, name, "width="+width+", height="+height+", "+windowOptions);
}

function goTo(page){
	location.href=page;
}

function resizeWindow(){
	window.resizeTo("792", "580")
}

function showDropdownMenu(menuID) {
	for (i=0;i<arrMenu.length;i++) {
		if (arrMenu[i].indexOf(menuID) < 0) {
			var menu = document.getElementById(arrMenu[i]);
			if (menu) menu.style.display = "none";
		}
	}
	var el = document.getElementById(menuID);
	if (el) {
		if (!document.all) el.className = "dropdownMenuNS";
		el.style.display = (el.style.display == "none" || el.style.display == "") ? "inline" : "none";
		menuClicked = (el.style.display == "inline") ? true : false;
	}
}

function hideDetail(btnEl){
	var el = document.getElementById("detail");
	if (el) {
		el.style.display = (el.style.display=="none") ? "block" : "none";
		btnEl.value = (btnEl.value=="Minimize -") ? "Maximize +" : "Minimize -";
	}
}

function selectAll(el, single) {
	var cbElements = document.getElementsByTagName("input");
	if (single) {
		for (i=0;i<cbElements.length;i++) {
			if (cbElements[i].getAttribute("type")=="checkbox") {
				cbElements[i].checked = el.checked;
				selectRow(cbElements[i], i%2);
			}
		}
	} else {
		var cbCount = 0;
		var checkedCount = 0;
		var cbElements = document.getElementsByTagName("input");
		for (i=0;i<cbElements.length;i++) {
			if (cbElements[i].getAttribute("type")=="checkbox") {
				if (cbElements[i].id == "headCheck" && !el.checked)	cbElements[i].checked = false;
				else if (cbElements[i].checked) {
					checkedCount++;
				}
				cbCount++;
			}
		}
		if (checkedCount == cbCount - 1) document.getElementById("headCheck").checked = true;
	}
}

function collapse(node) {
	if (node) {
		var el = node.parentNode;
		var nodes = el.childNodes;
		for (i=0; i<nodes.length; i++) {
			if (nodes[i].tagName) {
				if (nodes[i].tagName == "DIV" && nodes[i].id == "m") 
					nodes[i].style.display = (nodes[i].style.display == "block") ? "none" : "block";
			}
		}
		if (prevText) {
			var cls = prevText.className;
			if (cls.indexOf("emailsec") > -1) {
				prevText.className = (prevText.className == "emailsecNavOn") ? "emailsecNavOffRight" : "emailsecNavOn";
			} else {
				prevText.style.fontWeight = (prevText.style.fontWeight=="bold") ? "normal" : "bold";
			}
		}
		if ((el.className).indexOf("level") < 0) {
			var last = document.getElementById("container");
			if (last) last.childNodes[last.childNodes.length-1].childNodes[0].className = "emailsecNavOffRight";
			node.className = (node.className == "emailsecNavOn") ? "emailsecNavOffRight" : "emailsecNavOn";
			prevText = node;
		} else {
			var text = node.childNodes[2];
			if (text) {
				text.style.fontWeight = (text.style.fontWeight=="bold") ? "normal" : "bold";
				prevText = text;
			}
			var last = document.getElementById("container");
			if (last) last.childNodes[last.childNodes.length-1].childNodes[0].className = "emailsecNavOn";
		}
		var img = node.childNodes[0].childNodes[0];
		if (img) img.src = ((img.src).indexOf("_minus.gif") > -1) ? (img.src).replace("_minus.gif", "_plus.gif") : (img.src).replace("_plus.gif", "_minus.gif");
	}
}

//  pulldown jump-to-URL script (contacts)
function relatedInfoChange(form){
  if(form.options[form.selectedIndex].value != ""){
    document.location.href  = form.options[form.selectedIndex].value;
  }
}

function refreshBottomFrame(page){
  related_info.location.href = page;
}

function openWindowDropdown(form, windowName, width, height, windowOptions){
  if(form.options[form.selectedIndex].value != ""){
    //document.location.href  = form.options[form.selectedIndex].value;
	openWindow(form.options[form.selectedIndex].value, windowName, width, height, windowOptions); 
  }
}

function minimizeHyperlink(btnEl, goToURL){
	var el = parent.document.getElementById("detail");
	var minButton = parent.document.getElementById("minMaxButton");
	var winButton = parent.document.getElementById("newWindowButton");
	
	minButton.style.display = "none";
	winButton.style.display = "none";
	
	if (el) {
		el.style.display = (el.style.display=="none") ? "block" : "none";
		document.location.href = goToURL;
		//btnEl.value = (btnEl.value=="Minimize -") ? "Maximize +" : "Minimize -";
	}
}

function minimizeFromTop(minMaxButton, goToURL){
	var el = document.getElementById("detail");
	var btnEl = document.getElementById(minMaxButton);
	if (el) {
		el.style.display = (el.style.display=="none") ? "block" : "none";
		btnEl.value = (btnEl.value=="Minimize -") ? "Maximize +" : "Minimize -";
	}
	related_info.document.location.href = goToURL;
}














//COPPIED CODE FROM [script.js] file STARTS 

<!--
var menuClicked = false;
var prevText;
var arrMenu = [];
arrMenu[0] = "menuAddl";
arrMenu[1] = "menuView";
arrMenu[2] = "menuPageTop";
arrMenu[3] = "menuPageBottom";
arrMenu[4] = "MoveToTop";
arrMenu[5] = "MoveToBottom";

document.onclick = function(e) {
	if (document.all) {
		var el = window.event.srcElement;
		if (el) {
			var isMenu = el.getAttribute("menu");
			if (isMenu!="true") showDropdownMenu();
		}
	} else {
		if (e) {
			var el = e.target;
			if (el) {
				var isMenu = el.getAttribute("menu");
				if (isMenu!="true") showDropdownMenu();
			}
		}
	}
}

function pgRollOn(element){
	element.style.color = "#334a60";
}

function pgRollOff(element){
	element.style.color = "#937810";
}

function dvRollOn(element){
	element.style.color = "#937810";
}

function dvRollOff(element){
	element.style.color = "#4a6e9f";
}

function tabsRollOn(element){
	element.style.color = "#E1D39A";
}

function tabsRollOff(element){
	element.style.color = "#A2B3BF";
}

function iconRollOn(text){
	document.getElementById("iconAltText").innerHTML=text;
}

function iconRollOut(){
	document.getElementById("iconAltText").innerHTML="";
}
function secNavRollOn(element){
	element.style.borderColor = "#FFFFFF";
}
function secNavRollOff(element){
	element.style.borderColor = "#FFE994";
}

function ddRollOn(element){
	element.style.backgroundColor = "#244961";
}
function ddRollOff(element){
	element.style.backgroundColor = "#17283c";
}

function dd2RollOn(element){
	element.style.color = "#937810";
}
function dd2RollOff(element){
	element.style.color = "#334a60";
}

function dd3RollOn(element){
	element.style.color = "#334a60";
}
function dd3RollOff(element){
	element.style.color = "#937810";
}
function dd4RollOn(element){
	element.style.backgroundColor = "#244961";
}
function dd4RollOff(element){
	element.style.backgroundColor = "#345788";
}
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}
	
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}
	
function MM_findObj(n, d) { //v3.0
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document); return x;
}
	
function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

function selectRow(element, isOdd){
	if (element.id!="headCheck") {
		var isSelected=element.checked;
		var cssName="contactTableRowSelected";
		var cssNameCheck="contactTableCheckSelected";
		if(element.id=="bottomCheck") cssNameCheck="contactTableCheckBottomSelected";
		if(!isSelected){
			if (isOdd) cssName = "contactTableRowOdd";
			else cssName = "contactTableRowEven";
			cssNameCheck="contactTableCheck";
			if(element.id=="bottomCheck") cssNameCheck="contactTableCheckBottom";
		}
		var objTR=element.parentNode.parentNode;
		if(objTR) objTR.className=cssName;
		var objTD=element.parentNode;
		if(objTD) objTD.className=cssNameCheck;
	}
}

function gotoSearch(index){
	if (index == 6) location.href = "/search/index.html";
}

function gotoActivity(index){
	if (index == 2) openPopup("schedule.html");
}

function openPopup(page, popW, popH){
//	window.open(page, "", "width=" + popW + ", height=" + popH)
	window.open(page, "", "width=780, height=580")
}

function openSamePopup(page, windowname, popW, popH){
	window.open(page, windowname, "width=780, height=580")
}

function openWindow(page, name, width, height, windowOptions){
  if (windowOptions == ''){
    windowOptions = 'scrollbars=yes, resizable=yes';
  }

  window.open(page, name, "width="+width+", height="+height+", "+windowOptions);
}

function goTo(page){
	location.href=page;
}

function goToTopPage(page){
//	alert(" alert");
	//alert(" document.entityForm "+document.forms[0]);
//	location.href.target="_parent";
	document.forms[0].target="_parent";
	document.forms[0].action = page;
	document.forms[0].submit();
//	location.href=page;
}

function goToParent(form, page){
	form.target="_parent";
	form.action=page;
	form.submit();
	return false;
}

function resizeWindow(){
	window.resizeTo("792", "580")
}

function showDropdownMenu(menuID) {
	for (i=0;i<arrMenu.length;i++) {
		if (arrMenu[i].indexOf(menuID) < 0) {
			var menu = document.getElementById(arrMenu[i]);
			if (menu) menu.style.display = "none";
		}
	}
	var el = document.getElementById(menuID);
	if (el) {
		if (!document.all) el.className = "dropdownMenuNS";
		el.style.display = (el.style.display == "none" || el.style.display == "") ? "inline" : "none";
		menuClicked = (el.style.display == "inline") ? true : false;
	}
}

function hideDetail(btnEl){
	var el = document.getElementById("detail");
	if (el) {
		el.style.display = (el.style.display=="none") ? "block" : "none";
		btnEl.value = (btnEl.value=="Minimize -") ? "Maximize +" : "Minimize -";
	}
}

/* function selectAll(el, single) {
	var cbElements = document.getElementsByTagName("input");
	if (single) {
		for (i=0;i<cbElements.length;i++) {
			if (cbElements[i].getAttribute("type")=="checkbox") {
				cbElements[i].checked = el.checked;
				selectRow(cbElements[i], i%2);
			}
		}
	} else {
		var cbCount = 0;
		var checkedCount = 0;
		var cbElements = document.getElementsByTagName("input");
		for (i=0;i<cbElements.length;i++) {
			if (cbElements[i].getAttribute("type")=="checkbox") {
				if (cbElements[i].id == "headCheck" && !el.checked)	cbElements[i].checked = false;
				else if (cbElements[i].checked) {
					checkedCount++;
				}
				cbCount++;
			}
		}
		if (checkedCount == cbCount - 1) document.getElementById("headCheck").checked = true;
	}
} */
var selectToggle = true;
function selectAll(depricated, single) {
  var cbElements = document.getElementsByTagName("input");
  if (single) {
    for (i=0; i<cbElements.length; i++) {
      if (cbElements[i].getAttribute("type") == "checkbox") {
        cbElements[i].checked = selectToggle; //el.checked;
        selectRow(cbElements[i], i%2);
      }
    }
				if (selectToggle == true) {
				  selectToggle = false;
				} else {
				  selectToggle = true;
				}
  } else {
    var cbCount = 0;
    var checkedCount = 0;
    var cbElements = document.getElementsByTagName("input");
    for (i=0;i<cbElements.length;i++) {
      if (cbElements[i].getAttribute("type")=="checkbox") {
        if (cbElements[i].id == "headCheck" && !el.checked)	cbElements[i].checked = false;
        else if (cbElements[i].checked) {
          checkedCount++;
        }
        cbCount++;
      }
    }
    if (checkedCount == cbCount - 1) document.getElementById("headCheck").checked = true;
  }
}

function collapse(node) {
	if (node) {
		var el = node.parentNode;
		var nodes = el.childNodes;
		for (i=0; i<nodes.length; i++) {
			if (nodes[i].tagName) {
				if (nodes[i].tagName == "DIV" && nodes[i].id == "m") 
					nodes[i].style.display = (nodes[i].style.display == "block") ? "none" : "block";
			}
		}
		if (prevText) {
			var cls = prevText.className;
			if (cls.indexOf("emailsec") > -1) {
				prevText.className = (prevText.className == "emailsecNavOn") ? "emailsecNavOffRight" : "emailsecNavOn";
			} else {
				prevText.style.fontWeight = (prevText.style.fontWeight=="bold") ? "normal" : "bold";
			}
		}
		if ((el.className).indexOf("level") < 0) {
			var last = document.getElementById("container");
			if (last) last.childNodes[last.childNodes.length-1].childNodes[0].className = "emailsecNavOffRight";
			node.className = (node.className == "emailsecNavOn") ? "emailsecNavOffRight" : "emailsecNavOn";
			prevText = node;
		} else {
			var text = node.childNodes[2];
			if (text) {
				text.style.fontWeight = (text.style.fontWeight=="bold") ? "normal" : "bold";
				prevText = text;
			}
			var last = document.getElementById("container");
			if (last) last.childNodes[last.childNodes.length-1].childNodes[0].className = "emailsecNavOn";
		}
		var img = node.childNodes[0].childNodes[0];
		if (img) img.src = ((img.src).indexOf("_minus.gif") > -1) ? (img.src).replace("_minus.gif", "_plus.gif") : (img.src).replace("_plus.gif", "_minus.gif");
	}
}

//  pulldown jump-to-URL script (contacts)
function relatedInfoChange(form){
  if(form.options[form.selectedIndex].value != ""){
    document.location.href  = form.options[form.selectedIndex].value;
  }
}

function refreshBottomFrame(page){
  related_info.location.href = page;
}

function openWindowDropdown(form, windowName, width, height, windowOptions){
  if(form.options[form.selectedIndex].value != ""){
    //document.location.href  = form.options[form.selectedIndex].value;
	openWindow(form.options[form.selectedIndex].value, windowName, width, height, windowOptions); 
  }
}

function minimizeHyperlink(btnEl, goToURL){
	var el = parent.document.getElementById("detail");
	var minButton = parent.document.getElementById("minMaxButton");
	var winButton = parent.document.getElementById("newWindowButton");
	
	minButton.style.display = "none";
	winButton.style.display = "none";
	
	if (el) {
		el.style.display = (el.style.display=="none") ? "block" : "none";
		document.location.href = goToURL;
		//btnEl.value = (btnEl.value=="Minimize -") ? "Maximize +" : "Minimize -";
	}
}

function minimizeFromTop(minMaxButton, goToURL){
	var el = document.getElementById("detail");
	var btnEl = document.getElementById(minMaxButton);
	if (el) {
		el.style.display = (el.style.display=="none") ? "block" : "none";
		btnEl.value = (btnEl.value=="Minimize -") ? "Maximize +" : "Minimize -";
	}
	related_info.document.location.href = goToURL;
}


function openWindowWithTool(page)
{
window.open(page,"Centraview",
    "height=200,width=400,status=yes,toolbar=yes,menubar=yes,location=yes");
}

function checkForCheckBox()
{
  var k = 0;
  for (i=0; i<document.listrenderer.elements.length; i++)
  {
    if (document.listrenderer.elements[i].type=="checkbox" )
    {
      k++;
    }
  } 
  
  if (k >= 1)
  {
    for (i=0 ;i<document.listrenderer.elements.length;i++)
    {
      if (document.listrenderer.elements[i].type=="checkbox")
      {
        if ((document.listrenderer.elements[i].checked) )
        {
          return true;
        }
      }
    }
    //    alert("Please atleast select one record");
    alert("Please select at least one record.");
    return false;
  }else{
    alert("No Record to View/Delete/Duplicate");
    return false;
  }
}
// Funtion used by the Addtional Menu to forward the the control to appropriate 
// forward based on selection in the additional Menu. 

function callResource(forward,params,isNewWin,winProperty)
{
	var url = "/centraview/AdditionalMenu.do?UIParam=0&forward="+forward+params;
	//alert(url);
	if(isNewWin == 1)
	{
		
		openWindow(url, 'popupWin' ,winProperty);
	}
	else
	{
		window.location.href = url;
	}


}
// Generic function used by the Addtional Menu to tokenize the parameters passed to 
// callResource.
function tokenize(str, delimiter)
{
	var tokens = new Array(4);
	
	tokens[0] = str;
	tokens[1] = '';
	tokens[2] = '';
	tokens[3] = '';
	tokens[4] = '';
	var len = 0;
	for(var indx=0; str.indexOf(delimiter) > -1; indx++)
	{
		len = str.indexOf(delimiter);
		tokens[indx] = str.substring(0,len);
		str = str.substring(len+1,str.length);			
	}
	return tokens;
}
//-->

// generalize function used for Item Listing

function fnCheckBox()
{
	var flag = true;
	// function called for remove operation
	var hidden="";
	var str="";
	var k=0;
	flag2 = 1;
	formName="document.forms[0]";
	
	len = eval(formName+".length");
	for(i =0 ; i<len ; i++)
	{
	//	alert(eval(formName+".elements["+i+"].name"));
		if (eval(formName+".elements["+i+"].name")  == "checkbox"){
			flag2 = 0;
		}

	}
	if(flag2==0){  
		for (i=0 ;i<eval(formName+".elements.length");i++)
		{
		     if (eval(formName+".elements["+i+"].type")=="checkbox")
		     {
		       k++;
		     }
		}
		if(k>1)
		{
			for(i =0 ;i<eval(formName+".checkbox.length");i++)
			{
				if ( eval(formName+".checkbox["+i+"].checked"))
				{
					 hidden=eval(formName+".checkbox[i].value") + ",";
					 str=str+hidden;
				}
			}
			str=(str.substring(0,str.length-1));
		}
		else if (k==1) 
		{
			hidden=eval(formName+".checkbox.value") + ",";
			str=str+hidden;
			str=(str.substring(0,str.length-1));
		}

		if(!checkCheckbox(formName))
		{
			alert("Please select a record");
			return false;
		}
	}
	else {
		alert(" No item available ");
		return false;
	}
	flag = false;
	submitForm("", "", "", str,flag);
}

function checkCheckbox(formName)
{
	if ( eval(formName+".checkbox.length") )
	{
		for(i =0 ;i<eval(formName+".checkbox.length");i++)
		{
			if ( eval(formName+".checkbox["+i+"].checked"))
			{
				return true;
			}
		}
	}
	else
	{
		 return eval(formName+".checkbox.checked");
	}
		
}

function closeWindow(){
	window.close();
}

function checkRadio(formName)
{

//	formName="document."+formName

 
	if ( eval(formName+".radio.length") )
	{
		for(i =0 ;i<eval(formName+".radio.length");i++)
		{
			if ( eval(formName+".radio["+i+"].checked"))
			{
				return true;
			}
		}
	}
	else
	{
		return eval(formName+".radio.checked");
	}
	
}

function fnSet(lookupName)
{
//	formName="document."+formName

	formName="document.forms[0]";


	if (eval(formName+".dataPresent.value") <= 0)
	{
	    if (lookupName == "address"){
	  	    addressLookupValues = {Name: "", ID: "0", jurisdictionID: "0" }
			window.opener.setAddress(addressLookupValues);
		}
	        if (lookupName == "individual"){
			window.opener.setInd("","0");	
		}
		if (lookupName == "entity"){
			entityLookupValues = {entName: "", entID: "0", acctManager: "" , acctManagerID: ""}
			window.opener.setEntity(entityLookupValues);
		}
		if (lookupName == "group"){
		
			window.opener.setGrp("","0");
		}
		if (lookupName == "location"){
			window.opener.setLocation('','0');
		}
		if (lookupName == "project"){
			window.opener.setProject("","0");
		}
		if (lookupName == "projecttask"){
			window.opener.setParentTask("","0");	
		}
		if (lookupName == "opportunity"){
			opportunityLookupValues = {entityName: "", entityID: "0", Name: "" , ID: ""}
			window.opener.setOpp(opportunityLookupValues);		
		}
		if (lookupName == "primary"){
			window.opener.setPrimary("","0","","","");	
		}
		
		window.close();
	}

	flag2 = 1;
	len = eval(formName+".length");
	for(i =0 ; i<len ; i++)
	{
	//	alert (eval(formName+".elements["+i+"].name"));
		if (eval(formName+".elements["+i+"].name")  == "radio"){
			flag2 = 0;
		}

	}
	//alert (flag2);
	if(flag2==0){ 
		if(!checkRadio(formName))
		{
			alert("Please select a record");
			return false;
		}
	}
	else {
		alert(" No Records available ");
		return false;
	}

		
    if (lookupName == "address"){
	   	addressLookupValues = {Name: name, ID: idValue, jurisdictionID: jurisdictionIDValue }
		window.opener.setAddress(addressLookupValues);    
	}
	if (lookupName == "individual"){
		window.opener.setInd(name,idValue);	
	}
	if (lookupName == "entity"){
		entityLookupValues = {entName: name, entID: idValue, acctManager: accountManager , acctManagerID: accountManagerID}
		window.opener.setEntity(entityLookupValues);
	}
	if (lookupName == "group"){
		window.opener.setGrp(name,idValue);
	}
	if (lookupName == "location"){
   		window.opener.setLocation(name,idValue);
	}
	if (lookupName == "project"){
		window.opener.setProject(name,idValue);
	}
	if (lookupName == "projecttask"){
		window.opener.setParentTask(name,idValue);	
	}
	if (lookupName == "opportunity"){
		opportunityLookupValues = {entityName: entity, entityID: entityId, Name: name , ID: idValue}
		window.opener.setOpp(opportunityLookupValues);
	}
	if (lookupName == "primary"){
		window.opener.setPrimary(name,idValue,midleName ,lastName,title);
	}

	window.close();
}

function logout(){
	location.href = "<bean:message key='label.url.root' />/logout.do";
	return false;
}


// COPY ENDS 
