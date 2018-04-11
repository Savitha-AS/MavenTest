<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%
	response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setHeader("Cache-Control", "no-store");
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<head>	
	<jsp:include page="../../includes/global_javascript.jsp"></jsp:include>
	<jsp:include page="../../includes/global_css.jsp"></jsp:include>
	<link href="${pageContext.request.contextPath}/theme/css/displaytag.css" rel="stylesheet" type="text/css"/>
   		
	<script type="text/javascript">
	/*	$(function() {
			$("button, input:button, input:submit").button();
		}); */
		
		$(function() {
			$("#actBtn").click(function() { 
				confirmSave("Are you sure you want to activate this Business Rule?", 
					function(result) {
						if(result){
							//var frm = document.forms[0];
					        //frm.action="${pageContext.request.contextPath}/businessRule.controller.activateBusinessRule.task";
					        //frm.submit();
					        document.getElementById("listOffersFrm").action="${pageContext.request.contextPath}/businessRule.controller.activateBusinessRule.task";
							document.getElementById("listOffersFrm").submit();
						}
					}
				);		
			});
		});

		$(document).ready(function() {
			$("#div_searchResults").show('slow');
		});

		$(function() {
			$("#backBtn").click(function() {				
				//var frm = document.forms[0];
                //frm.action="${pageContext.request.contextPath}/businessRule.controller.listBusinessRules.task";
                //frm.submit();
                
                document.getElementById("listOffersFrm").action="${pageContext.request.contextPath}/businessRule.controller.listBusinessRules.task";
				document.getElementById("listOffersFrm").submit();

			});
		});
	</script>
</head>
<body onload="keepSubMenuSelected()">
	<form id="listOffersFrm" method="post">
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
				<h3><spring:message code="listoffer.header.text"></spring:message>
				</h3>			
			</div>	
			<br/>				
			
			<jsp:include page="../../includes/global_validations.jsp"></jsp:include>											
			
			<div class="data_container_color1">
				<div class="data_label" style="width:130px;">
					<spring:message code="BR.text.activerule"></spring:message>&nbsp;:
				</div>
				<div class="data_text">
					<input type="text" id="txt_1" value="${activeBusinessRule.brVersion}" size="18"  
						class="labelTxtBox_color1" style="border:0px; text-align:left" readonly="readonly" />
				</div>
			</div>
			<div class="data_container_color2">
				<div class="data_label" style="width:130px;">			
						<spring:message code="BR.text.selectedrule"></spring:message>&nbsp;:
				</div>
				<div class="data_text">
					<input type="text" id="txt_1" value="${futureActiveBusinessRule.brVersion}" size="18" 
		 				class="labelTxtBox_color2" style="border:0px; text-align:left" readonly="readonly"  />		
				</div>
			</div>
			<br/>
			<div style="width:520px; padding-left: 15px; display: none;" id="div_searchResults">								
				<display:table id="listOffers" name="requestScope.productsList" pagesize="10" 
	    					requestURI="" cellspacing="0" cellpadding="0" class="table"
	                				style="width: 520px;">
					
					<display:setProperty name="basic.msg.empty_list" 
						value="No offer records found.">
					</display:setProperty>
	
					<c:set var="key" value="${listOffers.offerId}"></c:set>
					<c:choose>
						<c:when test="${sessionScope.businessRuleActivationOffers == null || sessionScope.businessRuleActivationOffers[key] == null}">
							<display:column  titleKey='offer.text.offername' paramId="offerId" paramProperty="offerId" class="TD_STYLE" headerClass="TR_PUSH_HEADER" maxLength="35">
								<a href="businessRule.controller.viewOffer.task?offerId=${listOffers.offerId}&offerName=${listOffers.offerName}&offerType=${listOffers.offerType.offerTypeName}&multiValue=${listOffers.coverMultiplier}">${listOffers.offerName}</a>
							</display:column>
							<display:column  titleKey='platform.text.status' value="Pending" class="TD_STYLE" headerClass="TR_PUSH_HEADER" maxLength="10"/>
						</c:when>
						<c:otherwise>
							<display:column  titleKey='offer.text.offername' value="${listOffers.offerName}" class="TD_STYLE" headerClass="TR_PUSH_HEADER" maxLength="35"/>
							<display:column  titleKey='platform.text.status' value="Updated" class="TD_STYLE" headerClass="TR_PUSH_HEADER" maxLength="10"/>
						</c:otherwise>
					</c:choose>
				</display:table>  									
			</div>
			<br/>

			<div style="text-align: center;">
				<c:if test="${fn:length(requestScope.productsList) == fn:length(sessionScope.businessRuleActivationOffers)}">
					<ul class="btn-wrapper"><li class="btn-inner" id="actBtn"><span><spring:message code="BR.button.activateBR"/></span></li></ul>
				</c:if>				
				<ul class="btn-wrapper"><li class="btn-inner" id="backBtn"><span><spring:message code="platform.button.back"/></span></li></ul>
			</div>
			<br/>
		
			<c:set var="messageStr"><spring:message code="notification.listBR.listOffers" arguments="${activeBusinessRule.brVersion},${futureActiveBusinessRule.brVersion}"/></c:set>
			<jsp:include page="../../includes/notifications.jsp">
				<jsp:param name="message" value="${messageStr}"/>
			</jsp:include>
			
	   </div>
	</div>      
    <jsp:include page="../../includes/global_footer.jsp"></jsp:include>

	</form>
</body>
</html>