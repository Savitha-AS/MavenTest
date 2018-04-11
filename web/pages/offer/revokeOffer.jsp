<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags"%>
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
	<jsp:include page="../../includes/session.jsp"></jsp:include>
	<script src="${pageContext.request.contextPath}/appScripts/offer.js" type="text/javascript"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/dwr/interface/validateOfferMSISDN.js"></script>
	
	<script>
	/* $(function() {
		$( "button, input:button, input:submit").button();
	}); */
	
	 $(function() {
		$("#revokeBtn").click(function(){
			if(get_radio_value("selectedRadio") == "basedOnMsisdn"){
				confirmSave("Are you sure you want to revoke the selected offer from the customers?", function(result) {		
					if(result){						
						document.getElementById("revokeOffer").action="${pageContext.request.contextPath}/offers.controller.revokeOfferForMSISDN.task";
						document.getElementById("revokeOffer").submit();
					}
				});						
			}
			if(get_radio_value("selectedRadio") == "basedOnOffer"){						
				confirmSave("Are you sure you want to revoke the selected offer from the customers?", function(result) {	
					if(result){								
			        	document.getElementById("revokeOffer").action="${pageContext.request.contextPath}/offers.controller.revokeOffer.task";
						document.getElementById("revokeOffer").submit(); 						
					} 
				});						
			}
		});		
	}); 

	$(function(){
		$("#offers").change(function(){
			$("#customersDiv").hide('slow');
		});
		$("#msisdnCSV").click(function(){
			$("#customersDiv").hide('slow');
		});
	});
	
	$(function(){
		$("#searchBtn").click(function()
		{
			if(get_radio_value("selectedRadio") == "basedOnMsisdn"){
				if(!validateRevokeOfferBasedOnMSISDN()) {
					checkForValidMSISDN();
				}				
			}
			if(get_radio_value("selectedRadio") == "basedOnOffer"){
				$("#msisdnCSV").val('');
				if(!validateRevokeOffer()) {
		            document.getElementById("revokeOffer").action="${pageContext.request.contextPath}/offers.controller.fetchCustomersBasedOnOffer.task";
					document.getElementById("revokeOffer").submit();
					$("#customersDiv").show('slow');
				}				
			}
		});
	});
	
	
	/**
	 * Call-back method for <code>checkForValidMSISDN</code> DWR method.
	 * 
	 * @param <code>validMSISDNs</code> MSISDN which are existed/assigned.
	 * 
	 * @see checkForValidMSISDN()
	 */
	function loadMSISDNCheck(validMSISDNs) {
		$("#customersDiv").hide('slow');
		formMSISDNErrorIfExists(validMSISDNs);
		
		if(showValidationErrors("validationMessages_parent"))
			return true;
		else {
			document.getElementById("revokeOffer").action="${pageContext.request.contextPath}/offers.controller.fetchCustomersBasedOnMSISDN.task";
			document.getElementById("revokeOffer").submit();
			$("#customersDiv").show('slow');
		}		
	}
	
	function showMSISDNText() {
		$("#offer_select_drop").hide('slow');
		$("#msisdn_text_div").show('slow');
		$("#searchBtnDiv").show('slow');
		$("#customersDiv").hide('slow');
	}

	function showOfferDropDown(){
		$("#msisdn_text_div").hide('slow');
		$("#offer_select_drop").show('slow');
		$("#searchBtnDiv").show('slow');
		$("#customersDiv").hide('slow');
	}
	
	$(document).ready(function(){
		if('${assignOfferVO.selectedRadio}' != '' && '${assignOfferVO.selectedRadio}' == "basedOnMsisdn") {
			showMSISDNText();
			$("#msisdnCSV").val('${assignOfferVO.msisdnCSV}');
			$("#customersDiv").show('slow'); // Customer Count
		}
		if('${assignOfferVO.selectedRadio}' != '' && '${assignOfferVO.selectedRadio}' == "basedOnOffer") {
			showOfferDropDown();
			$("#customersDiv").show('slow'); // Customer Count
		}
	});			
	</script> 
</head>

<body>
 <form  id = "revokeOffer" method="post">
	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>
	
	<!-- View Content -->	
	<div class="content-container">
	<div id="box" style="width: 100%;">		
		<div class="pagetitle">
			<h3><spring:message code="revokeoffer.header.text"></spring:message>
			</h3>			
		</div>	
		
		<div style="text-align: right; margin-right: 5px;"><spring:message code="platform.text.mandatory"></spring:message></div>
		<br/>
		<jsp:include page="../../includes/global_validations.jsp"></jsp:include>

		<div class="data_container_color1" id="customers_revokeoffer_div">
			<div class="data_label" id="label_radio_type"><spring:message code="assignoffer.text.customers"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<input type="radio" id="selectedRadio" name="selectedRadio" value="basedOnMsisdn" onclick="javascript:showMSISDNText();" <c:if test="${'basedOnMsisdn' == assignOfferVO.selectedRadio}">checked</c:if>/><spring:message code="revokeoffer.choose.bymsisdn"></spring:message>&nbsp;&nbsp;&nbsp;
				<input type="radio" id="selectedRadio" name="selectedRadio" value="basedOnOffer" onclick="javascript:showOfferDropDown();" <c:if test="${'basedOnOffer' == assignOfferVO.selectedRadio}">checked</c:if>/><spring:message code="revokeoffer.choose.byoffername" ></spring:message>&nbsp;&nbsp;&nbsp;
			</div>
		</div>

			
		<div class="data_container_color2" id="msisdn_text_div" style="display:none;height:52px; padding-bottom:7px; padding-top:3px;">
			<div class="data_label" id="label_textarea_msisdn"><spring:message code="revokeoffer.textarea.heading"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
      		<div class="data_text" style="width: 268px;">
       		 	<textarea name="msisdnCSV" id="msisdnCSV" rows="2" cols="40"></textarea>
       		</div>
		</div>
    	  	
		<div class="data_container_color2" id="offer_select_drop"  style="display: none;">
			<div class="data_label" id = "label_offers"><spring:message code="offer.text.offername"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<select id="offers" name="offers" style="width:120px">
					<option value="-2"></option>
					<c:forEach var="offer" items="${productsList}">  
                      <option value="${offer.offerId}"
                       <c:if test="${offer.offerId==assignOfferVO.offers}">selected</c:if>>${offer.offerName}</option>
                    </c:forEach>
                    <option value="-1" <c:if test="${-1 == assignOfferVO.offers}">selected</c:if>>All</option>	  
				</select>
			</div>
		</div>

		<br />
		<div id="searchBtnDiv" style="display: none;text-align:center;">
			<ul class="btn-wrapper"><li class="btn-inner" id="searchBtn"><span><spring:message code="platform.button.search"/></span></li></ul>
		</div>
		
		<c:if test="${assignOfferVO != null}">
		<div id="customersDiv" style="display: none;">
			<br/>
			<div class="data_label" style="width:500px" class="data_container_color2">
				<div style="float:left">&nbsp;&nbsp; <spring:message code="revokeoffer.text.assignedcount"></spring:message>&nbsp;:&nbsp; <strong>${customerCount}</strong></div>
				<div id="CustomerCount"></div>
			</div>	
			<br/><br/>
			<div style="text-align:center;">
				<c:if test="${customerCount != null && customerCount > 0}">
					<ul class="btn-wrapper"><li class="btn-inner" id="revokeBtn"><span><spring:message code="revokeoffer.button.revoke"/></span></li></ul>
				</c:if>
			</div>
			<input id="custCount" type="hidden" value="${customerCount}" />
		</div>
		</c:if>
		<br/>
	</div>
	</div>
	<jsp:include page="../../includes/global_footer.jsp"></jsp:include>

</form>
</body>
</html>