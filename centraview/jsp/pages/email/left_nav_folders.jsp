<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<iframe src="<c:out value="${pageContext.request.contextPath}"/>/email/folder_list.do" height="100%" width="100%" frameborder="0" class="emailIframe" scrolling="auto"><bean:message key="label.email.iframenotsupported"/></iframe>