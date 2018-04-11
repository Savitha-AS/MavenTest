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
	<script src="${pageContext.request.contextPath}/appScripts/claims.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/appScripts/utils.js" type="text/javascript"></script>
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/engine.js"></script>
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/util.js"></script>
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/interface/validateCustomerMSISDN.js"></script>
	<script>
	/*	$(function() {
			$( "button, input:button, input:submit").button();
		}); */
		
		$(function() {
			$("#search-icon").click(function() { 	
				
				if(!validateSearchBeneficiary()){
					checkForExistingMSISDN("");
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
				if (msisdnExists == "false,true") {
					setError("msisdn", 36, "Mobile Number");
				}
				else if (msisdnExists == "true,true"){
					setError("msisdn", 33, "Mobile Number");
				}
				else if (msisdnExists == "false,false"){
					setError("msisdn", 36, "Mobile Number");
				}
				
				if(showValidationErrors("validationMessages_parent"))
					return true;
				else {
					
					document.getElementById("claimBeneficiaryForm").action="${pageContext.request.contextPath}/claims.controller.findCustomerByMSISDN.task?snm=0";
					document.getElementById("claimBeneficiaryForm").submit();
					
				}
			}
			
			$(function() {
				$("#claimBtn").click(function() { 
					if(!validateClaimBeneficiary()) {
						document.getElementById("claimBeneficiaryForm").action="${pageContext.request.contextPath}/claims.controller.loadModifyClaimDetails.task?snm=0";
						document.getElementById("claimBeneficiaryForm").submit();
					}
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
	<form method="post" id="claimBeneficiaryForm">

	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>
	
	<!-- View Content -->	
	<div class="content-container">
	<div id="box" style="width: 100%;">	
		<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
		<input type="hidden" id="pageId" value="42" />	
		<div class="pagetitle">
			<h3><spring:message code="claimbenficiary.header.text"/>
			</h3>			
		</div>	
		
		<div style="text-align: right; margin-right: 5px;"><spring:message code="platform.text.mandatory"/></div>
		<br/>
		<jsp:include page="../../includes/global_validations.jsp" flush="true"></jsp:include>
		
		<div class="data_container_color1">
			<div class="data_label" id="label_msisdn" style="width:120px;"><spring:message code="platform.text.msisdn"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width:358px;">												
				<div id="div_id_1" style="float:left;">										
					<input type="text" id="msisdn" name="msisdn" maxlength="10" style="width:110px;"/>
				</div>
				<div id="search-icon"></div>
			</div>
		</div>
		<br/>
		<div style="display: none;" id="div_searchResults">
			<c:if test="${frameWork:isNotNull(custList)}">
				<display:table id="custList" name="custList"
					excludedParams="*" pagesize="10"	
					style="width:690px; height:100%;" cellspacing="0"
					cellpadding="0">
					
					<display:setProperty name="basic.msg.empty_list"
						value="No customer record found for the entered mobile no.">
					</display:setProperty>
					
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="searchcustomer.text.cname" property="name"
						style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">		
					</display:column>
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="platform.text.msisdn" property="msisdn"
						style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
					</display:column>
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="platform.text.age" property="age"
						style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
					</display:column>
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="searchcustomer.text.confirmation.status"
						style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
							<c:choose>
								<c:when test="${custList.confirmed == 1}">
									<spring:message code="searchcustomer.text.confirmed"/>
								</c:when>
								<c:otherwise>
									<spring:message code="searchcustomer.text.unconfirmed"/>
								</c:otherwise>
							</c:choose>
					</display:column>
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="platform.text.regby" property="createdBy"
						style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
					</display:column>
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="platform.text.regdate" property="createdDateStr"
						style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
					</display:column>
				</display:table>
				<br/>	
			</c:if>
		</div>
		
		<c:if test="${not empty custList}"> 
		
			<div class="data_container_color2">
				<div class="data_label" id="label_claimedPerson">
					<spring:message code="claimbenficiary.claim.person"></spring:message>&nbsp;:&nbsp;
					<span class="mandatoryStar">*</span>
				</div>
				<div class="data_text">
					<input type="radio" id="claimedPerson" name="claimedPerson" value="C" />
					<spring:message code="claimbenficiary.text.customer"></spring:message>&nbsp;&nbsp;&nbsp; 
					<input type="radio" id="claimedPerson" name="claimedPerson" value="I" />
					<spring:message code="claimbenficiary.text.insrelative"></spring:message>
				</div>
			</div>
			<br />
			<div style="text-align: center;">
				<ul class="btn-wrapper"><li class="btn-inner" id="claimBtn"><span><spring:message code="claimbenficiary.button.claim"/></span></li></ul>
			</div>
			<input type="hidden" id="custId" name="custId" value="${custList.custId}"/>
		</c:if>
		<br/>	
		
	</div>
	</div>
	
	<jsp:include page="../../includes/global_footer.jsp"></jsp:include>

	</form>
</body>
</html>