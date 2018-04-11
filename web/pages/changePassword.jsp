<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setHeader("Cache-Control", "no-store");
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<jsp:include page="../includes/global_javascript.jsp"></jsp:include>
	<jsp:include page="../includes/global_css.jsp"></jsp:include>
	<script src="${pageContext.request.contextPath}/appScripts/user.js" type="text/javascript"></script>
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/engine.js"></script>
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/util.js"></script>
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/interface/validatePassword.js"></script>
	<script>
		/* $(function() {
			$( "button, input:button, input:submit").button();
		}); */
		
		$(function() {
			$("#saveBtn").click(function() {

				if(!validateChangePassword()){
					checkPassword();
				}
				
			});

			$("body").keypress(function(e) {
				if (e.keyCode == '13') {
					$("#saveBtn").click();
					return false;
				}
			});

			$("#clearBtn").click(function() {
				clearField("currentHash");
				clearField("newHash");
				clearField("confirmHash");
			});	
		});

		function checkPassword(){

			var currentHash = document.getElementById("currentHash").value;
			var newHash = document.getElementById("newHash").value;
			
			validatePassword.checkPassword(currentHash,newHash, {
				callback:function(resultCode) { 

			    	switch(resultCode){
			    		case 1: setError("currentHash", 22, "Old Password");
			    				if(showValidationErrors("validationMessages_parent"))
			    					return true;
			    	  			break;
			    	  			
			    		case 2: setError("newHash", 23, "New Password");
			    				if(showValidationErrors("validationMessages_parent"))
			    					return true;
						    	break;
						    	
			    		default: // FALLS THROUGH	
			    		case 0: confirmSave("Are you sure you want to change your password?", function(result) {
									if(result){
										document.getElementById("changePwdFrm").action="${pageContext.request.contextPath}/login.controller.changePassword.task";
										document.getElementById("changePwdFrm").submit();
									}
								});
			    	}
			  	}
			});
		}
	</script>
</head>

<body onload="keepMenuSelected()">
	<form method="post" id="changePwdFrm">
	<jsp:include page="../includes/global_header.jsp"></jsp:include>
	
	<c:choose>
		<c:when test="${isFirstLogin}">
			<input type="hidden" name="firstLogin" id="firstLogin" value="1"/>
		</c:when>
		<c:otherwise>
			<jsp:include page="../includes/menus.jsp"></jsp:include>
		</c:otherwise>
	</c:choose>
	
	<!-- View Content -->	
	<div class="content-container">
	<div id="box" style="width: 100%;">	
	
		<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
		<input type="hidden" id="pageId" value="10" />
			
		<div class="pagetitle">
			<h3><spring:message code="chngpwd.header.text"></spring:message></h3>			
		</div>	
		
		<div style="text-align: right; margin-right: 5px;"><spring:message code="platform.text.mandatory"></spring:message></div>
		<br/>
		
		<jsp:include page="../includes/global_validations.jsp"></jsp:include>
		
		<div class="data_container_color1">
			<div class="data_label" id="label_currentHash"><spring:message code="chngpwd.text.oldpwd"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<input type="password" id="currentHash" name="currentHash" style="width:110px;"/>
			</div>
		</div>
		
		<div class="data_container_color2">
			<div class="data_label" id="label_newHash"><spring:message code="chngpwd.text.newpwd"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<input type="password" id="newHash" name="newHash" style="width:110px;"/>
			</div>
		</div>
		
		<div class="data_container_color1">
			<div class="data_label" id="label_confirmHash"><spring:message code="chngpwd.text.repwd"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" >
				<input type="password" id="confirmHash" name="confirmHash" style="width:110px;"/>
			</div>
		</div>
		<br/>
		<div style="text-align:center;">
			<ul class="btn-wrapper"><li class="btn-inner" id="saveBtn"><span><spring:message code="platform.button.save"/></span></li></ul>
			<ul class="btn-wrapper"><li class="btn-inner" id="clearBtn"><span><spring:message code="platform.button.clear"/></span></li></ul>
		</div>
		<br/>
	</div>
	</div>
	<jsp:include page="../includes/global_footer.jsp"></jsp:include>
</form>
</body>
</html>