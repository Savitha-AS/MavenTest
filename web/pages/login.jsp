<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>

<un:useConstants className="org.acegisecurity.ui.AbstractProcessingFilter" 
			var="AbstractProcessingFilter" />
 
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en"> 
<head>    
	<jsp:include page="../includes/global_javascript.jsp"></jsp:include>	
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/theme_gh/css/style.css" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/css/jquery.alerts.css" />
	<script>	  
			$(function() {
				$( "button, input:button, input:submit").button();
			});
			
			$(function() {
				$("#loginBtn").click(function() {
					var loginId = document.getElementById("loginId").value;
					document.getElementById("loginId").value = loginId.toUpperCase();
					
					//if(checkForUpperCase("loginId"))
					//{
						if((validateTextBox("loginId")) && (validateTextBox("userHash")))						
							document.getElementById("frmLogin").submit();						
						else
							alert("Please enter both user id and password.");	
					/*}
					else
						alert("User Id is case-sensitive. Please enter correct User Id.");	*/							
				});				 
			});

			$(function() {
				$("#clearBtn").click(function() {
					clearField("loginId");
					clearField("userHash");
				});
			});
			
			$(function() {
				$("body").keypress(function(e) {
					 if (e.keyCode == '13') {
					 	$("#loginBtn").click();
					}
			 	});
			});
	</script>
	<style>
		#mylink:link {color: blue; }
		#mylink:active {color: orange; }
		#mylink:visited {color: purple; }
		#mylink:hover {color: red; } 
	</style>
</head>
<body>
	<form method="post" action="<c:url value='/j_security_check'/>" id="frmLogin">
	
	<div class="login-page-wrapper">
		<div class="header-login"></div>
		
		<div class="login-content">
			<div class="left"></div>
			<div class="right">
				<div class="login-field-wrapper">
							
							<div class="error-msg-login">
								<c:if test="${not empty param.login_error}">								
									<c:set var="errorReason" 
										value="${sessionScope[AbstractProcessingFilter.ACEGI_SECURITY_LAST_EXCEPTION_KEY].message}"/>	
										
									<!-- This logic is to avoid Stack Trace being printed on screen. -->			
									<c:if test="${fn:length(errorReason) gt 50}">
										<c:set var="errorReason" value="The network is down. Please contact administrator."/>		
									</c:if>
		                            <label style="color:red">Your login attempt was not successful, try
		                                   again.<br/>Reason: ${errorReason}</label>
                                 </c:if>
                             </div>
                             
							<div class="username-field">
								<div class="login-username">
									<strong>User Id</strong>&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
								</div>
								<div class="login-pswd">
									<input type="text" id="loginId" name="j_username" class="userUid" />
								</div>	
							</div>
							<div class="password-field">
								<div class="login-username">
									<strong>Password</strong>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
								<div class="login-pswd">
									<input type="password" id="userHash" name="j_password" />
								</div>
							</div>
							<div class="buttons">
								<ul class="btn-wrapper"><li class="btn-inner" id="loginBtn"><span>Login</span></li></ul>
								<ul class="btn-wrapper"><li class="btn-inner" id="clearBtn"><span>Clear</span></li></ul>
							</div>
							<div class="buttons">
								<a id="mylink" href="${pageContext.request.contextPath}/pages/forgotPassword.jsp">Forgot Password?</a>
							</div>
							</div>
			
			
			
		</div></div>
		<div class="footer-copyright">
		<!-- CR 28-01-2013: javascript added to automatically write the current year in footer -->
		© Copyright 2010-<script>document.write(new Date().getFullYear())</script> Milvik. All Rights Reserved</div>
		<div class="footer-line"></div>
	</div>
		<script type="text/javascript">
		window.onload = $("#loginId").focus();
	</script>
	</form>
</body>
</html>