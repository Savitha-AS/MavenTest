<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%
	response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setHeader("Cache-Control", "no-store");
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<jsp:include page="../../includes/calendar.jsp"></jsp:include>
	<jsp:include page="../../includes/global_javascript.jsp"></jsp:include>
	<jsp:include page="../../includes/global_css.jsp"></jsp:include>
	<script src="${pageContext.request.contextPath}/appScripts/offer.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/appScripts/utils.js" type="text/javascript"></script>
	
	<script>	
		/* $(function() {
			$( "button, input:button, input:submit").button();
		}); */
		
		$(function() {
			$("#assignBtn").click(function() { 
			
			if($('input[@name=customers]:checked').val() == "allCustomers")
			{
				if(!validateAssignAll())
				{
					confirmSave("Are you sure you want to assign this offer to all the customers?", 
							function(result) {
						if(result){

							//var frm = document.forms[0];
			                //frm.action="${pageContext.request.contextPath}/offers.controller.assignOffer.task";
		                    //frm.submit();
		                    document.getElementById("assignOffer").action="${pageContext.request.contextPath}/offers.controller.assignOffer.task";
							document.getElementById("assignOffer").submit();
						}
					});					
				}					
			}
			if($('input[@name=customers]:checked').val() == "selectedCustomers")
			{
				if(!validateAssignOffer())
				{
					confirmSave("Are you sure you want to assign this offer to the selected customers?", 
							function(result) {
						if(result){
							//var frm = document.forms[0];
			                //frm.action="${pageContext.request.contextPath}/offers.controller.assignOffer.task";
		                    //frm.submit();
		                    document.getElementById("assignOffer").action="${pageContext.request.contextPath}/offers.controller.assignOffer.task";
							document.getElementById("assignOffer").submit();
						}
					});					
				}
				else
				{
					$("#selectResultDiv").hide('slow');
					$("#assign").hide('slow');
				}	
			}
			});
		});
		
		$(function() 
		{
			$("#searchBtn").click(function()
			{ 
				if(!validateAssignOffer())
				{					
					//var frm = document.forms[0];
	                //frm.action="${pageContext.request.contextPath}/offers.controller.fetchRegCustomersBwnDates.task";
                    //frm.submit();
                    document.getElementById("assignOffer").action="${pageContext.request.contextPath}/offers.controller.fetchRegCustomersBwnDates.task";
					document.getElementById("assignOffer").submit();
				}
				else
				{
					//$("#selectResultDiv").hide('slow');
					$("#assign").hide('slow');
				}
			});
		});

		$(function() {
			$("#offerSelected").change(function() { 
				$("#customers_div").show('slow');
			});
		});

		function hideSelectTable()
		{
			$("#selectedCustomersDiv").hide('slow');
			$("#selectResultDiv").hide('slow');
			$("#allCustomersDiv").show('slow');
			$("#assign").show('slow');
		}
		function showSelectTable()
		{
			$("#selectedCustomersDiv").show('slow');
			$("#allCustomersDiv").hide('slow');	
			$("#assign").hide('slow');
			document.getElementById("fromDate").value="";
			document.getElementById("toDate").value="";
		}

		function onLoad(){

			<c:if test="${assignOfferVO != null}">
			 $("#selectedCustomersDiv").show('slow');
			 $("#allCustomersDiv").hide('slow');	
			 $("#assign").hide('slow');
			 $("#customers_div").show('slow');
			 $("#assign").show('slow');
			 loadRadioButton('customers', '${assignOfferVO.customers}');
			</c:if>
							
		}
	</script> 
</head>

<body onload="javascript:onLoad();">

<form  id ="assignOffer" method="post">

	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>
	
	<!-- View Content -->	
	<div class="content-container">
	<div id="box" style="width: 100%;">		
		<div class="pagetitle">
			<h3><spring:message code="assignoffer.header.text"></spring:message>
			</h3>			
		</div>	
		
		<div style="text-align: right; margin-right: 5px;"><spring:message code="platform.text.mandatory"/></div>
		<br/>
		<jsp:include page="../../includes/global_validations.jsp"></jsp:include>
		
		<div class="data_container_color1">
			<div class="data_label" id="label_offerSelected"><spring:message code="offer.text.offername"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<select id="offerSelected" name="offers" style="width:150px;">
				 <option></option>
				  <c:forEach var="offer" items="${productsList}">  
				    <option value="${offer.offerId}"
				      <c:if test="${offer.offerId==assignOfferVO.offers}">selected</c:if>>${offer.offerName}</option>
				  </c:forEach>
				</select>
			</div>
		</div>
		
		<div class="data_container_color2" id="customers_div" style="display:none;">
			<div class="data_label" id="label_customers"><spring:message code="assignoffer.text.customers"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<input type="radio" id="customers" name="customers" value="allCustomers" onclick="javascript:hideSelectTable();"/><spring:message code="assignoffer.text.all"></spring:message>&nbsp;&nbsp;&nbsp;
				<input type="radio" id="customers" name="customers" value="selectedCustomers" onclick="javascript:showSelectTable();"/><spring:message code="assignoffer.text.selected"></spring:message>
			</div>
		</div>	
		
		<div id="allCustomersDiv" style="display:none">
			<br/>
			<div class="data_label" style="width:500px;padding-left:10px; color: red;">
				<spring:message code="assignoffer.text.selectednote"></spring:message>
			</div>
		</div>	
		<br/>
		
		<div id="selectedCustomersDiv" style="display:none">
			<fieldset>
				<legend><spring:message code="platform.button.search"></spring:message></legend>
				<br/>
				<div class="data_container_color1">
					<div class="data_label"><spring:message code="platform.text.regdate"></spring:message>&nbsp;:</div>
					<div class="data_text">
						<div style="float:left; margin-top: 3px;" id="label_fromDate"><spring:message code="platform.text.from"></spring:message>&nbsp;&nbsp;</div>
						<div style="float:left">
							<input type="text" id="fromDate" name ="fromDate" style="width:70px"
								value="${assignOfferVO.fromDate}"/>
							<div class="calendar-icon" onclick="javascript: clearField('fromDate')" id="calBut1" title="<spring:message code='tooltip.platform.calendar'/>"></div>
							<div class="clear-icon" onclick="javascript: clearField('fromDate')" title="<spring:message code='tooltip.platform.clear'/>"></div>								
							<script>
							   Calendar.setup({
							        inputField : "fromDate",
							        trigger    : "calBut1",
							        onSelect   : function() { 
							        				this.hide();
							        			 },
							        dateFormat : "%d/%m/%Y"	
							    });
							</script>
						</div>
						<div style="float:left; margin-top: 3px;" id="label_toDate"><spring:message code="platform.text.to"></spring:message>&nbsp;&nbsp;</div>
						<div>
							<input type="text" id="toDate" name ="toDate" style="width:70px"
								value="${assignOfferVO.toDate}"/>
							<div class="calendar-icon" onclick="javascript: clearField('toDate')" id="calBut2" title="<spring:message code='tooltip.platform.calendar'/>"></div>
							<div class="clear-icon" onclick="javascript: clearField('toDate')" title="<spring:message code='tooltip.platform.clear'/>"></div>								
							<script>
							   Calendar.setup({
							        inputField : "toDate",
							        trigger    : "calBut2",
							        onSelect   : function() { 
							        				this.hide();
							        			 },
							        dateFormat : "%d/%m/%Y"	
							    });
							</script>
						</div>
					</div>
				</div>	
				<br/>
				<div style="text-align:center;">
					<ul class="btn-wrapper"><li class="btn-inner" id="searchBtn"><span><spring:message code="platform.button.search"/></span></li></ul>
				</div>
				<br/>				
			</fieldset>
			<br/>
		</div>		
		<c:if test="${assignOfferVO != null && 'selectedCustomers'==assignOfferVO.customers}">
		<div id="selectResultDiv">
			&nbsp;&nbsp;&nbsp; <spring:message code="assignoffer.text.offernotassigned"></spring:message> : <strong>${customerCount}</strong>
		</div>
		</c:if>
		
		<br/>
	
		<div style="text-align:center; display:none;" id="assign">
			<c:if test="${customerCount != null && customerCount > 0}">
				<ul class="btn-wrapper"><li class="btn-inner" id="assignBtn"><span><spring:message code="assignoffer.button.assign"/></span></li></ul>
			</c:if>
		</div>
		<br/>
	</div>
	</div>
	<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
</form>
</body>
</html>