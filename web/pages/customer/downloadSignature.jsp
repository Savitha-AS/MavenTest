<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
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
	<script src="${pageContext.request.contextPath}/appScripts/customer.js" type="text/javascript"></script>
	
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/engine.js"></script>
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/util.js"></script>
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/interface/validateCustomerMSISDN.js"></script>
	<script>
	/*	$(function() {
			$( "button, input:button, input:submit").button();
		}); */
		$(function() {	
			$("#downloadSignatureLink").click(function() {
				if(!validateDlSignature())
				{
					checkForExistingMSISDN("");
				}
			});	
		});
		$(function() {
			$("body").keypress(function(e) {
				if (e.keyCode == '13') {
					$("#downloadSignatureLink").click();
					return false;
				}
			});
		});
		/**
		 * Call-back method for <code>checkForExistingMSISDN</code> DWR method.
		 * 
		 * @param msisdnExists boolean result of the DWR method invoked.
		 * 
		 * @see checkForExistingMSISDN()
		 */
		function loadMSISDNCheck(msisdnExists){
			var isMsisdnExists = msisdnExists.split(",")[0];
			if(isMsisdnExists == "false"){
				setError("msisdn", 36, "Mobile Number");
			}
			
			if(showValidationErrors("validationMessages_parent"))
				return true;
			else {
				
				document.getElementById("downloadSignatureForm").action="${pageContext.request.contextPath}/customer.controller.downloadSignature.task";
				document.getElementById("downloadSignatureForm").submit();
				
				$("#msisdn").val('');
			}
		}
	</script>
</head>

<body onload="keepSubMenuSelected()">
	<form method="post" id="downloadSignatureForm">
	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>

	<!-- View Content -->
	<div class="content-container">
	<div id="box" style="width: 100%;">	
		<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
		<input type="hidden" id="pageId" value="40" />	
		<div class="pagetitle">
			<h3><spring:message code="dlSignature.header.text"></spring:message> 
			</h3>			
		</div>	

		<div style="text-align: right; margin-right: 5px;"><spring:message code="platform.text.mandatory"/></div>
		<br/>
		<jsp:include page="../../includes/global_validations.jsp"></jsp:include>
		
		<div class="data_container_color1">
			<div class="data_label" id="label_msisdn">
				<spring:message code="platform.text.msisdn"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
			</div>
			<div class="data_text">
				<input type="text" id="msisdn" name="msisdn" maxlength="10" style="width:110px;" />
			</div>
		</div>
		<br/>
		<div style="text-align: center;"> 
			<ul class="btn-wrapper"><li class="btn-inner" id="downloadSignatureLink"><span><spring:message code="dlSignature.button.downloadreport"/></span></li></ul>
		</div>			
		<br/>

	</div>
	</div>

	<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
	</form>
</body>
</html>