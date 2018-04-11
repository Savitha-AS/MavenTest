<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags"  prefix="spring"%>
<%
	response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setHeader("Cache-Control", "no-store");
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<jsp:include page="../../includes/global_javascript.jsp"></jsp:include>
	<jsp:include page="../../includes/global_css.jsp"></jsp:include>
	<link href="${pageContext.request.contextPath}/theme/css/displaytag.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript">
		$(document).ready(function() 
		{
			$("#div_searchResults").show('slow');
		});
	</script>
</head>
<body onload="keepSubMenuSelected()">
	<form method="post" id="listCompanyFrm" >
	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>
	
	<!-- View Content -->	
	<div class="content-container">
	<div id="box" style="width: 100%;">	
		<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
		<input type="hidden" id="pageId" value="19" />		
		<div class="pagetitle">
			<h3><spring:message code="listinsprovider.header.text"></spring:message>
			</h3>			
		</div>	
		<div style="overflow-x: auto; display: none;" id="div_searchResults">
			<display:table
				id="insuranceCompanyList" name="insuranceCompanyList"
			requestURI="/insuranceCompany.controller.listInsuranceProviders.task" excludedParams="*"
			pagesize="10" style="width:690px;"
			cellspacing="0" cellpadding="0" >
					
				<display:setProperty name="basic.msg.empty_list" 
					value="No insurance company records found.">
				</display:setProperty>
					
				<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
					titleKey="insprovider.text.compname" property="insCompName" 
					paramId="companyId" paramProperty="insCompId"
					url="/insuranceCompany.controller.getInsuranceProviderDetail.task"
					maxLength="15">			
				</display:column>
				
				<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
					titleKey="insprovider.text.branchname" property="insCompBranchName"
					maxLength="15">			
				</display:column>
				
				<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
					titleKey="insprovider.text.primcntname" property="insCompPocName" 
					maxLength="25">			
				</display:column>
				
				<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
					titleKey="insprovider.text.primcntmsisdn" property="insCompPocMsisdn" 
					maxLength="25">			
				</display:column>
				
				<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
					titleKey="platform.text.regby" property="createdBy.userName"
					maxLength="32">					
				</display:column>
				
				<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
					titleKey="platform.text.regdate" property="createdDateStr"
					maxLength="20">
				</display:column>
			</display:table>
		</div>
		<br/>
	</div>
	</div>
<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
</form>
</body>
</html>