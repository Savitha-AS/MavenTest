<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
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
<link rel="stylesheet" href="${pageContext.request.contextPath}/theme/css/popup.css" type="text/css" media="screen" />  
<script src="${pageContext.request.contextPath}/appScripts/smsTemplate.js"
	type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/appScripts/popup.js"
	type="text/javascript"></script>
<script>
	/* $(function() {
		$("button, input:button, input:submit").button();
	}); */

	$(function() {
		
		$("#previewBtn").click(function() {
		 	resetErrors();	
			var textFrom = stringTrim($("#smsTemplate").val());
			if(textFrom.length == 0){
		 		unMarkError("smsTemplate");
		 		setError("smsTemplate",0,"Content");
		 		if(showValidationErrors("validationMessages_parent"))
		 			return true;
			 }else{
				 document.getElementById("contactArea").innerHTML=substituteValues();
				 centerPopup();  
				 loadPopup();
			 }
		});
		
		$("#saveBtn").click(function() {
			if (!submitTemplate()) {
				confirmSave("", function(result) {
					if(result){
						//var frm = document.forms[0];
						//frm.action="${pageContext.request.contextPath}/sms.controller.saveTemplate.task";
						//frm.submit();
						document.getElementById("editSMSTemplate").action="${pageContext.request.contextPath}/sms.controller.saveTemplate.task";
						document.getElementById("editSMSTemplate").submit();
					}
				});
			}
		});
	});
	$(function() {
		$("#smsTemplateId").change(function() {	
			if (!validateDropDown(document.getElementById("smsTemplateId"))) {
				$("#div_content").hide('slow');
				$("#div_placeHolder").hide('slow');
				$("#div_smsValidity").hide('slow');
				$("#span_buttons").hide('slow');
			} else {
				//var frm = document.forms[0];
				//frm.action = "${pageContext.request.contextPath}/sms.controller.getTemplateContent.task";
				//frm.submit();
				document.getElementById("editSMSTemplate").action="${pageContext.request.contextPath}/sms.controller.getTemplateContent.task";
				document.getElementById("editSMSTemplate").submit();
			}
		});
	});
	
	$(function() {
		$("body").keypress(function(e) {
			if(e.keyCode=='27' && popupStatus==1){  
				disablePopup();  
			 }  
	 	});
		
		$("#confirmBtn").click(function() {
			disablePopup();  
		});
	});
	var placeHolderExists = false;
</script>
</head>
<body onload="keepSubMenuSelected()">
	<form id="editSMSTemplate" method="post">
	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>
	
	<!-- View Content -->	
	<div class="content-container">
	<div id="box" style="width: 100%;">	
		<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
		<input type="hidden" id="pageId" value="22" />	
		<div class="pagetitle">
			<h3><spring:message code="editSMSTemplate.header.text"/>
			</h3>			
		</div>	
		
		<div style="text-align: right; margin-right: 5px;"><spring:message code="platform.text.mandatory"/></div>
		<br />
	
		<jsp:include page="../../includes/global_validations.jsp"></jsp:include>
		
		<div class="data_container_color1">
			<div class="data_label"><spring:message code="editSMSTemplate.text.templatename"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<select id="smsTemplateId" name="smsTemplateId" style="width: 270px">
					<option>&nbsp;</option>
					<c:forEach var="templateNames" items="${TEMPLATE_NAME_LIST}">
						<c:choose>
							<c:when test="${templateNames.smsTemplateId eq SMS_TEMPLATE_VO.smsTemplateId}">
								<option value="${templateNames.smsTemplateId}" selected="selected">${templateNames.smsTemplateName}</option>
							</c:when>
							<c:otherwise>
								<option value="${templateNames.smsTemplateId}">${templateNames.smsTemplateName}</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>
			</div>
		</div>
	
		<div class="data_container_color2" style="height: 100px; display: none;" id="div_content">
			<div class="data_label" id="label_smsTemplate"><spring:message code="editSMSTemplate.text.content"/>&nbsp;:&nbsp;
				<span class="mandatoryStar">*</span>
			</div>
			<div class="data_text">
				<div id="div_id_1">
					<textarea id="smsTemplate" name="smsTemplate" rows="3" cols="45" style="height:65px; overflow:auto;" >${SMS_TEMPLATE_VO.smsTemplate}</textarea>
				</div>
				<div id="div_id_2" style="display:inline-block;">
					<div style="float:left; margin-top:5px;">
						<spring:message code="editSMSTemplate.text.smsChar"/>&nbsp;:&nbsp;
					</div>
					<div style="float:left; margin-left:15px; margin-top:3px;">
						<input type="text" id="txtLen1" value="160" size="3" readonly="readonly"/>
					</div>
					<div style="float:left; padding-top: 5px; font-size: 0.9em;">
						&nbsp;<spring:message code="editSMSTemplate.text.smsChar.limit"/>
					</div>
				</div>
			</div>
		</div>
		
		<c:choose>
			<c:when test="${fn:length(PLACE_HOLDER_LIST) gt 0}">
				<div class="data_container_color1" id="div_placeHolder" style="display: none;">
					<div class="data_label" id="label_smsPlaceHolder"><spring:message code="editSMSTemplate.text.placeholder"/>&nbsp;:</div>
					<div class="data_text">
						<div id="div_id_3" style="float:left;">
							<select id="smsPlaceHolder" name="smsPlaceHolder" style="width: 160px">
								<option>&nbsp;</option>
								<c:forEach var="placeHolderNames" items="${PLACE_HOLDER_LIST}">
									<option
										value="${placeHolderNames.value.placeHolderCode}#${placeHolderNames.value.placeHolderValue}">${placeHolderNames.value.placeHolderName}</option>
								</c:forEach>
							</select>
						</div>
						<div style="margin-top:3px;">&nbsp;&nbsp;<a id="addLink" href="#"><spring:message code="editSMSTemplate.link.add"/></a></div>
					</div>
				</div>
				<div class="data_container_color2" style="display: none;" id="div_smsValidity">
					<div class="data_label" id="label_smsValidity"><spring:message code="editSMSTemplate.text.smsvalidity"/>:</div>
					<div class="data_text">
						<div id="div_id_4" style="padding-top:1px; float:left;">
							<input type="text" id="smsValidity" name="smsValidity" maxlength="3" style="width:30px;" value="${SMS_TEMPLATE_VO.smsValidity}"/>	
						</div>									
						<div style="align: left; padding-top: 4px;">
							&nbsp;<spring:message code="editSMSTemplate.text.days"/>
						</div>
					</div>
				</div>
				<script>
					placeHolderExists = true;
				</script>
			</c:when>
			<c:otherwise>
				<div class="data_container_color1" style="display: none;" id="div_smsValidity">
					<div class="data_label" id="label_smsValidity"><spring:message code="editSMSTemplate.text.smsvalidity"/>:</div>
					<div class="data_text">
						<div id="div_id_4" style="padding-top:1px; float:left;">
							<input type="text" id="smsValidity" name="smsValidity" maxlength="3" style="width:30px;" value="${SMS_TEMPLATE_VO.smsValidity}"/>	
						</div>									
						<div style="align: left; padding-top: 4px;">
							&nbsp;<spring:message code="editSMSTemplate.text.days"/>
						</div>
					</div>
				</div>
				<script>
					placeHolderExists = false;
				</script>
			</c:otherwise>
		</c:choose>
			<br/>
			<div style="text-align: center;display: none;" id="span_buttons">
				<ul class="btn-wrapper"><li class="btn-inner" id="previewBtn"><span><spring:message code="editSMSTemplate.button.preview"/></span></li></ul>
				<ul class="btn-wrapper"><li class="btn-inner" id="saveBtn"><span><spring:message code="platform.button.save"/></span></li></ul>
			</div>
			<br/>
			<!-- For Pop Up -->		
			 <div id="popupContact">
	        		<h1><spring:message code="previewSMS.header.text"/></h1>  
	        		<hr />
			        <div id="contactArea"></div>
			        <hr />	
			        <div style="text-align: center;">
			        <ul class="btn-wrapper"><li class="btn-inner" id="confirmBtn"><span><spring:message code="platform.button.ok"/></span></li></ul>
					</div>								          
	    	</div>
	    	<div id="backgroundPopup"></div>		    	  
			<!-- End of Pop Up code -->
	</div>
	</div>
	
	
	<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
	
		<script type="text/javascript">
			document.getElementById("txtLen1").value = allowedLength
					- document.getElementById("smsTemplate").value.length;
			if(placeHolderExists){
				document.getElementById("addLink").onclick = setPlaceHolder;
			}
			document.getElementById("smsTemplate").onkeydown = processKeyDown;
			document.getElementById("smsTemplate").onkeyup = processKeyUp;
			document.getElementById("smsTemplate").onblur = processOnBlur;
			document.getElementById("smsTemplate").onmousedown = processMouseDown;
			document.getElementById("smsTemplate").onpaste = processOnPaste;
			document.getElementById("smsTemplate").onfocus = countChars;
			if (validateDropDown(document.getElementById("smsTemplateId"))) {
				$("#div_content").show('slow');
				if(placeHolderExists){
					$("#div_placeHolder").show('slow');
				}
				$("#div_smsValidity").show('slow');
				$("#span_buttons").show('slow');
			}
		</script>
	</form>
</body>
</html>