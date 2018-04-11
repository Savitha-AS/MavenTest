<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
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
		
		$(function() {	
			$("#exportToExcelBtn").click(function() {
				document.getElementById("listUserFrm").action="${pageContext.request.contextPath}/user.controller.downloadUserReport.task";
				document.getElementById("listUserFrm").submit();
			});
		});	
	</script>
	<style>
		.header-right-push{
			 padding-right: 3px;
		}
	</style>
</head>

<body onload="keepSubMenuSelected()">	
	<form method="get" id="listUserFrm">
	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>
	
	<!-- View Content -->	
	<div class="content-container">
	<div id="box" style="width: 100%;">	
		<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
		<input type="hidden" id="pageId" value="13" />	
		<div class="pagetitle">
			<h3><spring:message code="listuser.header.text"/>
			</h3>			
		</div>	
		
		<div style="overflow-x: auto; display: none;" id="div_searchResults">
			<display:table id="userList" name="userList"
				requestURI="/user.controller.listUsers.task" excludedParams="*"
				pagesize="10" style="width:690px;" cellspacing="0"
				cellpadding="0">
				
				<display:setProperty name="basic.msg.empty_list" 
					value="Currently no users are registered in the platform.">
				</display:setProperty>

				<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
					titleKey="platform.text.userid" property="userUid" maxLength="5">
				</display:column> 
	
				<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
					titleKey="platform.text.username" property="userName"
					paramId="userId" paramProperty="userId"
					url="/user.controller.showUserDetails.task" maxLength="25"> 
				</display:column>
				
				<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
					titleKey="platform.text.msisdn" maxLength="10" property="msisdn">
				</display:column>
				
				<display:column class="TD_STYLE TD_RIGHT_STYLE" headerClass="TR_PUSH_HEADER header-right-push"
					titleKey="platform.text.age" property="age" maxLength="2">
				</display:column>
				
				<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
					titleKey="platform.text.role" property="roleMaster.roleName" maxLength="25">
				</display:column>
				
				<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
					titleKey="platform.text.branch" property="branchDetails.branchName" maxLength="25">
				</display:column>
				
				<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
					titleKey="platform.text.regdate" property="createdDateStr" maxLength="25">
				</display:column>
				
				<display:column class="TD_STYLE TD_RIGHT_STYLE" headerClass="TR_PUSH_HEADER header-right-push"
					titleKey="platform.text.currentMonthLeave" >
					<c:out value="${currentMonthLeaveMap[userList.userId] }"></c:out>
				</display:column>
				
				<display:column class="TD_STYLE TD_RIGHT_STYLE" headerClass="TR_PUSH_HEADER header-right-push"
					titleKey="platform.text.currentYearLeave" >
					<c:out value="${currentYearLeaveMap[userList.userId] }"></c:out>
				</display:column>
				
			</display:table>
		</div>
		<br/>
		<br/>
		<div style="text-align:center;">
			<ul class="btn-wrapper">
				<li class="btn-inner" id="exportToExcelBtn">
					<span><spring:message code="platform.text.exportToExcel"/></span>
				</li>
			</ul>
		</div>
		<br/>
	</div>
	</div>
	
	<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
	</form>
</body>
</html>