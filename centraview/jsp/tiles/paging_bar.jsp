<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<!-- start paging_bar.jsp -->
<table width="600" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="left">
      <table border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td class="pagingButtonTD"><input type="button" value="<bean:message key='label.value.addnew'/>" class="normalButton" onclick="showNew();" /></td>
          <td class="pagingButtonTD"><input type="button" name="duplicate" class="normalButton" value="<bean:message key='label.value.duplicate'/>" onclick="duplicateList();" /></td>
          <td class="pagingButtonTD"><input type="button" name="delete" class="normalButton" value="<bean:message key='label.value.delete'/>" onclick="deleteList();" /></td>
        </tr>
      </table>
    </td>
    <td align="right">
      <table border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td class="pagingButtonTD"><input type="button" name="firstlist" class="normalButton" value="|&laquo;"></td>
          <td class="pagingButtonTD"><input type="button" name="previous" class="normalButton" value="&laquo;"></td>
          <td class="pagingButtonTD"><span class="pagingText"><bean:message key="label.tiles.page"/>: 0/0</span></td>
          <td class="pagingButtonTD"><input type="button" name="next" class="normalButton" value="&raquo;"></td>
          <td class="pagingButtonTD"><input type="button" name="last" class="normalButton" value="&raquo;|"></td>
          <td  class="pagingButtonTD"><span class="pagingText"><bean:message key="label.tiles.recordspg"/>:</span></td>
          <td class="pagingButtonTD">
            <select class="pagingDropdown">
              <option>100</option>
              <option>50</option>
              <option>25</option>
              <option>10</option>
            </select>
          </td>
          <td  class="pagingButtonTD"><span class="pagingText">3<bean:message key="label.tiles.records"/> </span></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<!-- end paging_bar.jsp -->