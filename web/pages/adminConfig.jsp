<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
	<script src="${pageContext.request.contextPath}/appScripts/adminConfig.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/appScripts/utils.js" type="text/javascript"></script>
	<script>
		/* $(function() {
			$( "button, input:button, input:submit").button();
		}); */
		
		$(function() {
			$("#saveBtn").click(function() {				
				if(!validateAdminConfig()){
					document.getElementById("adminConfigFrm").action="${pageContext.request.contextPath}/adminConfig.controller.saveConfigDetails.task";
					document.getElementById("adminConfigFrm").submit();
				}
			});
		});	
		
		$(function() {
			$("body").keypress(function(e) {
				 if (e.keyCode == '13') {
				 	$("#saveBtn").click(); 
				}
		 	});
		});
	</script>
</head>

<body onload="keepMenuSelected()">
	<form method="post" id="adminConfigFrm">
	<jsp:include page="../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../includes/menus.jsp"></jsp:include>
	
	<!-- View Content -->	
	<div class="content-container">
	<div id="box" style="width: 100%;">	
		<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
		<input type="hidden" id="pageId" value="9" />	
		<div class="pagetitle">
			<h3><spring:message code="adminConfig.header.text"/>
			</h3>			
		</div>	
		<div style="text-align: right; margin-right: 5px;"><spring:message code="platform.text.mandatory"/></div>
		<br/>
		<jsp:include page="../includes/global_validations.jsp"></jsp:include>
		
		<div class="data_container_color1">
			<div class="data_label" id="label_defaultPwd" style="width: 250px;"><spring:message code="adminConfig.text.defaultPwd"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width: 268px;">
					<input type="text" id="defaultPwd" name="defaultPwd" style="width:110px" maxlength="100"  value="${adminConfigVO.defaultPwd}"/>
			</div>
		</div>
		
		<div class="data_container_color2">
			<div class="data_label" id="label_userLoginPrefix" style="width: 250px;"><spring:message code="adminConfig.text.userLoginPrefix"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width: 268px;">
				<div id="div_id_2">
					<input type="text" id="userLoginPrefix" name="userLoginPrefix" style="width:110px" maxlength="2"  value="${adminConfigVO.userLoginPrefix}"/>
				</div>
			</div>
		</div>
		
		<div class="data_container_color1">
			<div class="data_label" id="label_pwdHistoryLimit" style="width: 250px;"><spring:message code="adminConfig.text.pwdHistoryLimit"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width: 268px;">
				<div id="div_id_3">
					<input type="text" id="pwdHistoryLimit" name="pwdHistoryLimit" maxlength="1" size="3"  value="${adminConfigVO.pwdHistoryLimit}"/>
				</div>
			</div>
		</div>
		<div class="data_container_color2">
			<div class="data_label" id="label_maxLoginAttempts" style="width: 250px;"><spring:message code="adminConfig.text.maxLoginAttempts"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width: 268px;">
				<input type="text" id="maxLoginAttempts" name="maxLoginAttempts" maxlength="1" size="3"  value="${adminConfigVO.maxLoginAttempts}"/>
			</div>
		</div>
		<div class="data_container_color1" style="height:25px; padding-bottom:5px; padding-top:5px;">
			<div class="data_label" id="label_maxIdleCount" style="width: 420px;"><spring:message code="adminConfig.text.maxIdleCount"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width: 48px;">
				<input type="text" id="maxIdleCount" name="maxIdleCount" maxlength="1" size="3"  value="${adminConfigVO.maxIdleCount}"/>
			</div>
		</div>
		<div class="data_container_color2">
			<div class="data_label" id="label_offerSubscriptionLastDay" style="width: 250px;"><spring:message code="adminConfig.text.offerSubLastDay"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width: 268px;">
				<div id="div_id_3">
					<input type="text" id="offerSubscriptionLastDay" name="offerSubscriptionLastDay" maxlength="2" size="3"  value="${adminConfigVO.offerSubscriptionLastDay}"/>
				</div>
			</div>
		</div>
		<div class="data_container_color1">
			<div class="data_label" id="label_commissionPercent" style="width: 250px;"><spring:message code="adminConfig.text.commPercent"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width: 268px;">
				<div id="div_id_3">
					<input type="text" id="commissionPercent" name="commissionPercent" maxlength="5" size="6"  value="${adminConfigVO.commissionPercent}"/>
				</div>
			</div>
		</div>
		<div class="data_container_color2" style="height:52px; padding-bottom:7px; padding-top:3px;">
			<div class="data_label" id="label_registerCustomerWSURL" style="width: 250px;"><spring:message code="adminConfig.text.registerCustomerWSURL"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width: 268px;">
				<textarea cols="37" id="registerCustomerWSURL" name="registerCustomerWSURL">${adminConfigVO.registerCustomerWSURL}</textarea>
			</div>
		</div>
		<%-- <div class="data_container_color1" style="height:52px; padding-bottom:7px; padding-top:3px;">
			<div class="data_label" id="label_offerUnsubscribeWSURL" style="width: 250px;"><spring:message code="adminConfig.text.offerUnSubWSURL"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width: 268px;">
				<textarea cols="37" id="offerUnsubscribeWSURL" name="offerUnsubscribeWSURL">${adminConfigVO.offerUnsubscribeWSURL}</textarea>
			</div>
		</div>
		<div class="data_container_color2" style="height:52px; padding-bottom:7px; padding-top:3px;">
			<div class="data_label" id="label_removeCustomerRegistrationWSURL" style="width: 250px;"><spring:message code="adminConfig.text.removeCustomerRegWSURL"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width: 268px;">
				<textarea cols="37" id="removeCustomerRegistrationWSURL" name="removeCustomerRegistrationWSURL">${adminConfigVO.removeCustomerRegistrationWSURL}</textarea>
			</div>
		</div> --%>
		<div class="data_container_color1" style="height:52px; padding-bottom:7px; padding-top:3px;">
			<div class="data_label" id="label_announcementMessage" style="width: 250px;"><spring:message code="adminConfig.text.announcementMessage"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width: 268px;">
				<textarea cols="37" id="announcementMessage" name="announcementMessage">${adminConfigVO.announcementMessage}</textarea>
			</div>
		</div>
		<div class="data_container_color2" style="height:52px; padding-bottom:7px; padding-top:3px;">
			<div class="data_label" id="label_msisdnCodes" style="width: 250px;"><spring:message code="adminConfig.text.msisdnCodes"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width: 268px;">
				<textarea cols="37" id="msisdnCodes" name="msisdnCodes">${adminConfigVO.msisdnCodes}</textarea>
			</div>
		</div>
		<%-- <div class="data_container_color1">
			<div class="data_label" id="label_defaultOfferAssigned" style="width: 250px;"><spring:message code="adminConfig.text.defaultOfferAssigned"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width: 268px;">
				<select id="defaultOfferAssigned" name="defaultOfferAssigned" style="width: 150px;">
					<option value=""></option>
					<c:forEach var="offers" items="${productsList}">
						<option value="${offers.productId}">${offers.productName}</option>
					</c:forEach>
				</select>
			</div>
		</div> --%>
		<br/>
		<div style="text-align:center;">
			<ul class="btn-wrapper"><li class="btn-inner" id="saveBtn"><span><spring:message code="platform.button.save"/></span></li></ul>
		</div>
		<br/><br/>
	</div>
	</div>
	<jsp:include page="../includes/global_footer.jsp"></jsp:include>
</form>
	<script type="text/javascript"> 
		//loadDropDownList('defaultOfferAssigned', '${adminConfigVO.defaultOfferAssigned}');
	</script>
</body>
</html>