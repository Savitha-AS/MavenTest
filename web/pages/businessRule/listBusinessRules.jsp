<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
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
	<script src="${pageContext.request.contextPath}/appScripts/businessRule.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/appScripts/utils.js" type="text/javascript"></script>
	
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/engine.js"></script>
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/util.js"></script>
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/interface/validateSelectedBR.js"></script>
	
	<link href="${pageContext.request.contextPath}/theme/css/displaytag.css" rel="stylesheet" type="text/css"/>
	
	<script>
			
	/*	$(function() {
			$( "button, input:button, input:submit").button();
		}); */
		
		$(function() {
			$("#actBtn").click(function() {
				// Reset the error list and unmark the errors if any.
				resetErrors();
				checkForSelectedBR();
			});
		});

		$(document).ready(function() {
			$("#div_searchResults").show('slow');
		});
		
		/**
		 * Call-back method for <code>checkForSelectedBR</code> DWR method.
		 * 
		 * @param msisdnExists boolean result of the DWR method invoked.
		 * 
		 * @see checkForExistingMSISDN()
		 */
		function loadSelectedBRCheck(isSelectedBRActive){
				
			if(isSelectedBRActive){
				var elementId = "checkboxBR" + getRadioButtonValue('selectBR');	
				setError(elementId, 26, "Business Rule");
			}
			if(showValidationErrors("validationMessages_parent"))
				return true;
			else{
				document.getElementById("listBusinessRules").action="${pageContext.request.contextPath}/businessRule.controller.listOffers.task";
				document.getElementById("listBusinessRules").submit();
			}
		}

	</script>
</head>
<body onload="keepSubMenuSelected()">
   <form id="listBusinessRules" method="post">
   <jsp:include page="../../includes/global_header.jsp"></jsp:include>
   <jsp:include page="../../includes/menus.jsp"></jsp:include>   
	
	<!-- View Content -->	
	<div class="content-container">
	<div id="box" style="width: 100%;">
		<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->		
		<input type="hidden" id="pageId" value="21" />
		<div class="pagetitle">
			<h3><spring:message code="listBR.header.text"></spring:message>
			</h3>			
	</div>	
		<br/>
		<jsp:include page="../../includes/global_validations.jsp"></jsp:include>
								
		<div style="display: none;" id="div_searchResults">					
			<display:table id="listBusRules" name="requestScope.businessRulesList"
				requestURI="" excludedParams="*"  htmlId="businessRulesTable"
				pagesize="10" style="width:690px;" cellspacing="0"
				cellpadding="0" sort="list">
		
				<display:setProperty name="basic.msg.empty_list" 
					value="No business rule definitions found.">
				</display:setProperty>
				
				<display:column title="Select" class="TD_STYLE" headerClass="TR_PUSH_HEADER">
					<input type="radio" name="selectBR" 
					id ="checkboxBR${listBusRules_rowNum}" 
					value="${listBusRules.brId}"
					<c:if test="${listBusRules.active == 1}">checked</c:if> />
				</display:column>
												
				<display:column titleKey='listBR.text.busruleversion' class="TD_STYLE" headerClass="TR_PUSH_HEADER" maxLength="35">
				   <a href="businessRule.controller.viewBusinessRule.task?busRuleId=${listBusRules.brId}&insComp=${listBusRules.insuranceCompany.insCompName}">${listBusRules.brVersion}</a>
				</display:column>
				                  
				<display:column property="insuranceCompany.insCompName" titleKey='BR.text.inscmpnyname' class="TD_STYLE" headerClass="TR_PUSH_HEADER" maxLength="15"/>
				
				<display:column property="premiumAmtPerc" titleKey='BR.text.premiumpercent' class="TD_STYLE" headerClass="TR_PUSH_HEADER" maxLength="6"/>
				
				<display:column titleKey='platform.text.status' class="TD_STYLE" headerClass="TR_PUSH_HEADER">
					<c:choose>
						<c:when test="${listBusRules.active == '1'}">
							<img style="text-align: center;" src="${pageContext.request.contextPath}/theme/images/tick.gif" alt="Active"/>
							<input type="hidden" id="activeBRId" value="${listBusRules.brId}"/>
						</c:when>
						<c:otherwise>
							<img style="text-align: center;" src="${pageContext.request.contextPath}/theme/images/cross.gif" alt="Inactive"/>
						</c:otherwise>
					</c:choose>
				</display:column>
			</display:table>
		</div>
        <br/>
                  
        <div style="text-align:center;">
        	<ul class="btn-wrapper"><li class="btn-inner" id="actBtn"><span><spring:message code="BR.button.activate"/></span></li></ul>	
         </div>
         <br/>
    </div>
    </div>
    <jsp:include page="../../includes/global_footer.jsp"></jsp:include>
  </form>
</body>
</html>

