<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/frameWorkFunctions.tld" prefix="frameWork"%>
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
	<script src="${pageContext.request.contextPath}/appScripts/user.js" type="text/javascript"></script>
	<script>
	/*	$(function() {
			$( "button, input:button, input:submit").button();
		}); */
		
		$(function() {
			$("#search-icon").click(function() { 	
				var userUid = document.getElementById("userUid").value;
				document.getElementById("userUid").value = userUid.toUpperCase();		
			
				if(!validateResetUserPassword()){
					
					document.getElementById("resetPwdFrm").action="${pageContext.request.contextPath}/user.controller.findUserByUserId.task";
					document.getElementById("resetPwdFrm").submit();
				}				
			});	

			$("#resetBtn").click(function() { 
				confirmSave("Are you sure you want to reset password and/or unlock the account for this user?", function(result) {
					if(result){
						document.getElementById("resetPwdFrm").action="${pageContext.request.contextPath}/user.controller.resetPassword.task";
						document.getElementById("resetPwdFrm").submit();
					}
				});
			});
			
			$("body").keypress(function(e) {
				 if (e.keyCode == '13') {
					 $("#search-icon").click();
					 return false;
				 }				
		 	});				
		});	

		$(document).ready(function(){
			$("#div_searchResults").show('slow');
		});
	</script>	
</head>

<body onload="keepSubMenuSelected()">
	<form method="post" id="resetPwdFrm">

	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>
	
	<!-- View Content -->	
	<div class="content-container">
	<div id="box" style="width: 100%;">	
		<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
		<input type="hidden" id="pageId" value="15" />	
		<div class="pagetitle">
			<h3><spring:message code="resetpwd.header.text"/>
			</h3>			
		</div>	
		
		<div style="text-align: right; margin-right: 5px;"><spring:message code="platform.text.mandatory"/></div>
		<br/>
		<jsp:include page="../../includes/global_validations.jsp" flush="true"></jsp:include>
		
		<div class="data_container_color1">
			<div class="data_label" id="label_userId" style="width:120px;"><spring:message code="platform.text.userid"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width:358px;">												
				<div id="div_id_1" style="float:left;">										
					<input type="text" id="userUid" name="userUid" class="userUid" style="width:110px;"/>
				</div>
				<div id="search-icon"></div>
			</div>
		</div>
		<br/>
		<div style="display: none;" id="div_searchResults">
			<c:if test="${frameWork:isNotNull(userList)}">	
				<display:table
					id="userList" name="userList"
					excludedParams="*" pagesize="10" style="width:690px;"
					cellspacing="0" cellpadding="0" >	
					
					<display:setProperty name="basic.msg.empty_list" 
						value="No user record found for the entered User Id.">
					</display:setProperty>
					
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="platform.text.userid" property="userUid" 
						maxLength="5">
					</display:column>
					
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="platform.text.username" property="userName"
						maxLength="25">
					</display:column>
					
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="platform.text.msisdn" property="msisdn"
						maxLength="10">		
					</display:column>
					
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="platform.text.age" property="age" maxLength="2">			
					</display:column>
					
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="platform.text.role" property="roleMaster.roleName"
						maxLength="25">			
					</display:column>
					
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="platform.text.regdate" property="createdDateStr"
						maxLength="25">			
					</display:column>
										
				</display:table>
				
				<br/>	
			</c:if>
		</div>
		<c:if test="${not empty userList}"> 
			<div style="text-align: center;">
				<ul class="btn-wrapper"><li class="btn-inner" id="resetBtn"><span><spring:message code="resetpwd.button.reset"/></span></li></ul>
			</div> 
			<input type="hidden" id="userId" name="userId" value="${userList.userId}"/>
		</c:if>
		<br/>	
		
		<c:set var="messageStr"><spring:message code="notification.resetPassword"/></c:set>
		<jsp:include page="../../includes/notifications.jsp">
			<jsp:param name="message" value="${messageStr}"/>
		</jsp:include>
		
	</div>
	</div>
	
	<jsp:include page="../../includes/global_footer.jsp"></jsp:include>

	</form>
</body>
</html>