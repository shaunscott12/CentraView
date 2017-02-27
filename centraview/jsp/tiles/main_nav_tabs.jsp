<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="com.centraview.common.*" %>
<%@ page import="com.centraview.common.menu.*" %>
<%@ page import="com.centraview.common.CVUtility" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.settings.SiteInfo" %>
<%@ page import="com.centraview.common.GlobalMasterLists"%>
<%@ page import="com.centraview.common.Globals"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="java.util.*"%>
<%@ page import="com.centraview.additionalmenu.AdditionalMenuVO"%>
<%@ page import="com.centraview.common.UserObject"%>
<%@ page import="com.centraview.common.UserPrefererences"%>
<%@ page import="com.centraview.administration.authorization.*"%>
<table border="0" cellspacing="0" cellpadding="0" class="tabsContainer">
  <tr>
    <td class="tabsContainerTopRow">
      <table border="0" cellspacing="0" cellpadding="0" class="tabsTable">
        <tr>
          <%-- NOTE: do not move this form tag, or the page layout will break --%>
          <form action="#" name="additionalMenuForm" id="additionanMenuForm" method="get">
          <td class="firstTabOff"><html:img page="/images/spacer.gif" width="15" height="11" alt="" /></td>
          <%
            // This file used to be extremely clean, using only JSP taglibs. *sigh*
            Locale locale = pageContext.getRequest().getLocale();
            UserObject uObj = (UserObject)session.getAttribute("userobject");
            String userType = uObj.getUserType();
            UserPrefererences up = uObj.getUserPref();
            ModuleFieldRightMatrix mfrm = new ModuleFieldRightMatrix();
            if (up != null){
              mfrm = up.getModuleAuthorizationMatrix();
            }
            TreeMap moduleMap = (TreeMap)mfrm.getModuleRights();
            Collection mapSet = (Collection)moduleMap.values();
            ArrayList validModules = new ArrayList();
            validModules.add(new Integer(Globals.HOME_TAB));  // home always shows
            Iterator iter = mapSet.iterator();
            while (iter.hasNext()) {
              HashMap element = (HashMap)iter.next();
              Integer rightsValue = (Integer)element.get("rights");
              String moduleName = (String)element.get("name");
              if (rightsValue.compareTo(new Integer(30)) <= 0) {
                // add this to the valid modules. Make sure we add the
                // corresponding Globals.tabsMap Integer value, not the
                // moduleName String
                Integer moduleIndex = (Integer)Globals.tabsMap.get(moduleName);
                if (moduleIndex != null && moduleIndex.intValue() >= 0) {
                  validModules.add(moduleIndex);
                }
              }
            }
            pageContext.setAttribute("validModules", validModules, PageContext.PAGE_SCOPE);

            UIAttributes uiAttributes = (UIAttributes)request.getAttribute("uiAttributes");
            int mainSelected = uiAttributes.getMainSelected();
            Iterator iter2 = uiAttributes.getMainNav().getElements().iterator();
            while (iter2.hasNext()) {
              MenuItem menuItem = (MenuItem)iter2.next();
              String cssClass = "tabsOff";
              if (mainSelected == menuItem.getId()) {
                cssClass = "tabsOn";
              }

              if (validModules.contains(new Integer(menuItem.getId()))) {
                menuItem.setLocale(locale);
              %>
              <td class="<%=cssClass%>" nowrap><a href="<html:rewrite page=""/><%=menuItem.getURL()%>" class="<%=cssClass%>"><%=menuItem.getLabel()%></a></td>
              <%
              }
            }
          %>
          <c:if test="${requestScope.uiAttributes.mainSelected == 14}">
            <c:set var="additionalCss" value="On"/>
          </c:if>
          <c:if test="${requestScope.uiAttributes.mainSelected != 14}">
            <c:set var="additionalCss" value="Off"/>
          </c:if>
          <td class="tabs<c:out value="${additionalCss}" default="Off"/>">
            <%-- use "tabsDropNavOn" when the additional tab is selected --%>
            <%

              String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(application)).getDataSource();
              GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
              Vector additionalMenus = gml.getAdditionalMenu();
              Iterator iterator = additionalMenus.iterator();
              AdditionalMenuVO amVO = null;

              String menuLabel = (String)session.getAttribute("additionalMenuItem");
              if (menuLabel == null) {
                menuLabel = "Additional +";
              }
            %>
            <select class="tabsDropNav<c:out value="${additionalCss}" default="Off"/>" onchange="c_additionalMenu(this.value);">
              <option value="--"><%=menuLabel%></option>
              <%
                while (iterator.hasNext()) {
                  amVO = (AdditionalMenuVO)iterator.next();
                  if (amVO.getModuleName().equals("Administrator") && ! userType.equals("ADMINISTRATOR")) {
                    continue;
                  }
                  if (mfrm.isModuleVisible(amVO.getModuleName())) {
                    int isNewWin = 0;
                    if (amVO.getIsNewWin()) {
                      isNewWin = 1;
                    }
                    if (! menuLabel.equals(amVO.getMenuItemName())) {
                      %><option value="<%=amVO.getForwardResource()%>"><%=amVO.getMenuItemName()%></option><%
                    }
                  }
                }
              %>
            </select>
          </td>
          <td class="lastTabOff"><html:img page="/images/spacer.gif" width="1" height="1" alt="" /></td>
          <%-- NOTE: do not move this form tag, or the page layout will break --%>
          </form>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td class="tabsBottomBar"><html:img page="/images/clear.gif" width="1" height="6" alt="" /></td>
  </tr>
</table>