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
	<jsp:include page="../../includes/global_javascript.jsp"></jsp:include>
	<jsp:include page="../../includes/global_css.jsp"></jsp:include>
	<script src="${pageContext.request.contextPath}/appScripts/branch.js" type="text/javascript"></script>
	
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/engine.js"></script>
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/util.js"></script>
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/interface/validateBranchName.js"></script>
	<script>
		/*$(function() {
			$( "button, input:button, input:submit").button();
		});*/
		
		$(function() {
			$("#saveBtn").bind("click",(function() {				
				if(!validateBranchDetails()){
					checkForExistingBranchName("");
				}
			}));

			$("body").keypress(function(e) {
				if (e.keyCode == '13') {
					$("#saveBtn").click(); 
					return false;
				}
		 	});

			$("#clearBtn").click(function() {
				clearField("branchName");
				clearField("branchStreet");
				clearField("branchRegion");
				clearField("branchCity");
			});
		});	
		
		/**
		 * Call-back method for <code>checkForExistingBranchName</code> DWR method.
		 * 
		 * @param branchNameExists boolean result of the DWR method invoked.
		 * 
		 * @see checkForExistingBranchName()
		 */
		function loadBranchNameCheck(branchNameExists){

			//resetErrors();
			//unMarkError("branchName");
			
			if(branchNameExists){
				setError("branchName", 20, "Branch Name");
			}
			if(showValidationErrors("validationMessages_parent"))
				return true;
			else{
				confirmSave("", function(result) {
					if(result){
						document.getElementById("addBranchFrm").action="${pageContext.request.contextPath}/branch.controller.addBranch.task";
						document.getElementById("addBranchFrm").submit();
					}
				});
			}
		}
	</script>
</head>

<body onload="keepSubMenuSelected()">
	<form method="post" id="addBranchFrm">
	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>
	<!-- View Content -->
	<div class="content-container">	
	<div id="box" style="width: 100%;">
		<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
		<input type="hidden" id="pageId" value="30" />	
		<div class="pagetitle">
			<h3><spring:message code="addBranch.header.text"/></h3>				
		</div>
		<div style="text-align: right; margin-right: 10px;" ><spring:message code="platform.text.mandatory"/></div>
		<br/>
		<jsp:include page="../../includes/global_validations.jsp"></jsp:include>
		
		<div class="data_container_color1">
			<div class="data_label" id="label_branchName"><spring:message code="branch.text.name"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
					<input type="text" id="branchName" name="branchName" style="width:110px" maxlength="100"/>
			</div>
		</div>
		
		<div class="data_container_color2">
			<div class="data_label" id="label_branchStreet"><spring:message code="branch.text.street"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<div id="div_id_2">
					<input type="text" id="branchStreet" name="branchStreet" style="width:110px" maxlength="100"/>
				</div>
			</div>
		</div>
		
		<div class="data_container_color1">
			<div class="data_label" id="label_branchRegion"><spring:message code="branch.text.region"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" >
				<div id="div_id_3" style="float:left;">
					<input type="text" id="branchRegion" name="branchRegion" style="width:110px" maxlength="100"/>
				</div>
			</div>
		</div>
		<div class="data_container_color2">
			<div class="data_label" id="label_branchCity"><spring:message code="branch.text.city"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<div id="div_id_4" style="float:left;">
					<input type="text" id="branchCity" name="branchCity" style="width:110px" maxlength="100"/>
				</div>
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
	<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
</form>
</body>
</html>