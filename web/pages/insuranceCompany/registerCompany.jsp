<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags"  prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
	<script src="${pageContext.request.contextPath}/appScripts/insuranceProvider.js" type="text/javascript"></script>
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/engine.js"></script>
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/util.js"></script>
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/interface/validateInsCompName.js"></script>
	<script>	
	
	/*	$(function() {
			$( "button, input:button, input:submit").button();
		}); */
		
		$(function() {
			$("#saveBtn").click(function() { 
				if(!validateRegisterInsuranceProvider()){
					checkForExistingInsCmp();
				}
			});
			
			$("body").keypress(function(e) {
				if (e.keyCode == '13') {
					$("#saveBtn").click();
					return false;
				}
			});
		});

		$(function() {
			$("#clearBtn").click(function() {
				clearField("companyName");
				clearField("companyNumber");
				clearField("branchName");
				clearField("addressLine1");
				clearField("addressLine2");
				clearField("city");
				clearField("state");
				clearField("country");
				clearField("zipCode");
				clearField("primaryContact");
				clearField("primaryContactMobile");
			});
		});
		
		/**
		 * Call-back method for <code>checkForExistingInsCmp</code> DWR method.
		 * 
		 * @param companyExists boolean result of the DWR method invoked.
		 * 
		 * @see checkForExistingInsCmp()
		 */
		function loadInsCompCheck(companyExists){
			if(companyExists){
				setError("companyName", 20, "Company Name");
			}
			if(showValidationErrors("validationMessages_parent"))
				return true;
			else{
				confirmSave("", function(result) {
					if(result){
						document.getElementById("registerCompanyFrm").action="${pageContext.request.contextPath}/insuranceCompany.controller.registerInsuranceProvider.task";
						document.getElementById("registerCompanyFrm").submit();
					}
				});
			}
		}
	</script>
</head>
<body onload="keepSubMenuSelected()">
	<form method="post" id="registerCompanyFrm" >
	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>
	
	<!-- View Content -->	
	<div class="content-container">
	<div id="box" style="width: 100%;">	
		<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
		<input type="hidden" id="pageId" value="18" />		
		<div class="pagetitle">
			<h3><spring:message code="reginsprovider.header.text"></spring:message>
			</h3>			
		</div>	
		
		<div style="text-align: right; margin-right: 5px;"><spring:message code="platform.text.mandatory"/></div>
		<br/>
		<jsp:include page="../../includes/global_validations.jsp"></jsp:include>
							
		<div class="data_container_color1">
			<div class="data_label" style="width:260px;" id="label_companyName"><spring:message code="insprovider.text.compname"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width:328px;">
					<input type="text" id="companyName" name="companyName" style="width:110px;" maxlength="50" tabindex="1"/>
			</div>
		</div>
		
		<div class="data_container_color2">
			<div class="data_label" id="label_companyNumber" style="width:260px;"><spring:message code="insprovider.text.compcontactnum"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width:328px;">
				<div id="div_id_2">
					<input type="text" id="companyNumber" name="companyNumber" style="width:110px" maxlength="10" tabindex="2"/>
				</div>
			</div>
		</div>
		
		<div class="data_container_color1">
			<div class="data_label" id="label_branchName" style="width:260px;"><spring:message code="insprovider.text.branchname"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text"  style="width:328px;">
				<div id="div_id_3">
					<input type="text" id="branchName" name="branchName" maxlength="50" style="width:110px" tabindex="3"/>
				</div>
			</div>
		</div>
		
		<div class="data_container_color2">
			<div class="data_label" id="label_addressLine1" style="width:260px;"><spring:message code="insprovider.text.address1"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width:328px;text-align: left;">
				<div id="div_id_4">
					<input type="text" id="addressLine1" name="addressLine1" maxlength="90" style="width:110px" tabindex="4"/>
				</div>
			</div>
		</div>
		
		<div class="data_container_color1">
			<div class="data_label" style="width:260px;"><spring:message code="insprovider.text.address2"></spring:message>&nbsp;:</div>
			<div class="data_text" style="width:328px;">
				<input type="text" id="addressLine2" name="addressLine2" maxlength="90" style="width:110px" tabindex="5"/>
			</div>
		</div>
		
		<div class="data_container_color2">
			<div class="data_label" id="label_city" style="width:260px;"><spring:message code="insprovider.text.city"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width:328px;">
				<input type="text" id="city" name="city" style="width:110px" maxlength="50" tabindex="6"/>
			</div>
		</div>
		
		<div class="data_container_color1">
			<div class="data_label" id="label_state" style="width:260px;"><spring:message code="insprovider.text.state"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width:328px;">
				<div id="div_id_7">
					<input type="text" id="state" name="state" style="width:110px" maxlength="50" tabindex="7"/>
				</div>
			</div>
		</div>
		
		<div class="data_container_color2">
			<div class="data_label" id="label_country" style="width:260px;"><spring:message code="insprovider.text.country"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width:328px;">
				<div id="div_id_8">
					<input type="text" id="country" name="country" style="width:110px" maxlength="50" tabindex="8"/>
				</div>
			</div>
		</div>
		
		<div class="data_container_color1">
			<div class="data_label" id="label_zipCode" style="width:260px;"><spring:message code="insprovider.text.zip"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width:328px;text-align: left;">
				<input type="text" id="zipCode" name="zipCode" style="width:110px" maxlength="9" tabindex="9"/>
			</div>
		</div>
		
		<div class="data_container_color2">
			<div class="data_label" id="label_primaryContact" style="width:260px;"><spring:message code="insprovider.text.primcntname"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width:328px;text-align: left;">
				<input type="text" id="primaryContact" name="primaryContact" maxlength="80" style="width:110px" tabindex="10"/>
			</div>
		</div>
		
		<div class="data_container_color1">
			<div class="data_label" id="label_primaryContactMobile" style="width:260px;"><spring:message code="insprovider.text.primcntmsisdn"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width:328px;">
				<div id="div_id_11" >
					<input type="text" id="primaryContactMobile" maxlength="10" name="primaryContactMobile" style="width:110px" maxlength="10" tabindex="11"/>
				</div>
			</div>
		</div>
		
		<br/>
		<div style="text-align: center;">
			<ul class="btn-wrapper"><li class="btn-inner" id="saveBtn"><span><spring:message code="platform.button.save"/></span></li></ul>
			<ul class="btn-wrapper"><li class="btn-inner" id="clearBtn"><span><spring:message code="platform.button.clear"/></span></li></ul>
		</div>
		<br/>
	</div>
	</div>
<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
</form>
</body>
</html>

