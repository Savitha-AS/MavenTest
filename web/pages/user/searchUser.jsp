<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
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
	<script src="${pageContext.request.contextPath}/appScripts/user.js" type="text/javascript"></script>
	
	<script>	
			$(function() {
				$("#searchBtn").click(function() {			
					if(!validateSearchUser()){
						document.getElementById("searchUserFrm").action="${pageContext.request.contextPath}/user.controller.findUserByUserDetails.task";
						document.getElementById("searchUserFrm").submit();
					}
				});
				
				$("body").keypress(function(e) {
					if (e.keyCode == '13') {
					 	$("#searchBtn").click(); 
						 return false;
				 	}
			 	});

				$("#clearBtn").click(function() {
					clearField("fname");
					clearField("sname");
					clearField("msisdn");
					clearField("userUid");
				});
				
				$('#userUid').keydown(function() {
					  var prefix = 'TA';
					  if (this.value.substring(0, prefix.length) != prefix){
					    $(this).val(prefix);
					  }
				});	
			});	
			
			$(document).ready(function() 
			{
				$("#div_searchResults").show('slow');
			});
			
	</script>
	
	<link href="${pageContext.request.contextPath}/theme/css/displaytag.css" rel="stylesheet" type="text/css"/>
</head>

<body onload="keepSubMenuSelected()">
	<form method="post" id="searchUserFrm">
	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>
	
	<!-- View Content -->
	<div class="content-container">
	<div id="box" style="width: 100%;">	
		<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
		<input type="hidden" id="pageId" value="14" />	
		<div class="pagetitle">
			<h3><spring:message code="searchuser.header.text"/>
			</h3>			
		</div>	
		
		<br/>
		<jsp:include page="../../includes/global_validations.jsp"></jsp:include>
		
		<div class="data_container_color1">
			<div class="data_label" id="label_fname"><spring:message code="platform.text.firstname"/>&nbsp;:</div>
			<div class="data_text">
					<input type="text" id="fname" name="fname" style="width:110px" maxlength="100"/>
			</div>
		</div>
		
		<div class="data_container_color2">
			<div class="data_label" id="label_sname"><spring:message code="platform.text.surname"/>&nbsp;:</div>
			<div class="data_text">
				<div class="" id="div_id_2">
					<input type="text" id="sname" name="sname" style="width:110px"  maxlength="50"/>
				</div>
			</div>
		</div>
		
		<div class="data_container_color1">
			<div class="data_label" id="label_msisdn"><spring:message code="platform.text.msisdn"/>&nbsp;:</div>
			<div class="data_text" >
				<div class="" id="div_id_3" style="float:left;">
					<input type="text" id="msisdn" name="msisdn" style="width:110px" maxlength="10" size="8"/>
				</div>
			</div>
		</div>
		<div class="data_container_color2">
			<div class="data_label" id="label_userUid">
			<spring:message code="platform.text.userid"/>&nbsp;:</div>
			<div class="data_text" >
				<div class="" id="div_id_4" style="float:left;">
					<input type="text" id="userUid" name="userUid" style="width:110px" maxlength="6" size="6"/>
				</div>
			</div>
		</div>				
		<div class="data_container_color1">
			<div class="data_label" id="label_role"><spring:message code="platform.text.role"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<div id="div_id_15" >
					<select id="role" name="role" style="width:170px;">
						<option value="-1"></option>
						<c:forEach var="roleMap" items="${roleMap}">  
					    	<option value="${roleMap.key}">${roleMap.value}</option>
					 	</c:forEach>					
					</select>
				</div>
			</div>
		</div>
		<br/>
		<div style="text-align:center;">
			<ul class="btn-wrapper"><li class="btn-inner" id="searchBtn"><span><spring:message code="platform.button.search"/></span></li></ul>
    		<ul class="btn-wrapper"><li class="btn-inner" id="clearBtn"><span><spring:message code="platform.button.clear"/></span></li></ul>
		</div>
		<br/>
		<div style="overflow-x: auto; display: none;" id="div_searchResults">		
			<c:if test="${frameWork:isNotNull(userList)}">
				<display:table id="userList" name="userList"
					requestURI="/user.controller.findUserByUserDetails.task" excludedParams=""
					pagesize="10" style="width:690px;" cellspacing="0"
					cellpadding="0">
				
					<display:setProperty name="basic.msg.empty_list" 
						value="No user records found matching the specified criteria.">
					</display:setProperty>
	
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="platform.text.userid" property="userUid" maxLength="5">
					</display:column> 
		
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="platform.text.username" property="userName"
						url="/user.controller.showModifyUserDetails.task"
						paramId="userId" paramProperty="userId" maxLength="25"> 	
					</display:column>
					
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="platform.text.msisdn" property="msisdn"
						maxLength="10">
					</display:column>
					
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="platform.text.age" property="age"
						maxLength="2">
					</display:column>
							
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="platform.text.role" property="roleMaster.roleName"
						maxLength="25">
					</display:column>
					
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="platform.text.regdate" property="createdDateStr"
						maxLength="25">
					</display:column>
					
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="platform.text.currentMonthLeave" maxLength="20"
						style="align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
						<c:out value="${monthYearLeave.currentMonthLeaveMap[userList.userId] }">
						</c:out>
					</display:column>
					
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="platform.text.currentYearLeave" maxLength="20"
						style="align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
						<c:out value="${monthYearLeave.currentYearLeaveMap[userList.userId] }">
						</c:out>
					</display:column>
				</display:table>
			</c:if>
		</div>
		<br/>
	</div>
	</div>
	<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
</form>
</body>
</html>