<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
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
	<jsp:include page="../../includes/calendar.jsp"></jsp:include>
	<jsp:include page="../../includes/global_javascript.jsp"></jsp:include>
	<jsp:include page="../../includes/global_css.jsp"></jsp:include>
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/engine.js"></script>
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/util.js"></script>
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/interface/validateLeaveDates.js"></script>
	<script src="${pageContext.request.contextPath}/appScripts/leave.js" type="text/javascript"></script>
	<script>

	$(function() {
		$( "button, input:button, input:submit").button();
	});
	$(function() {
			$("#applyBtn").click(function() { 
				if(!validateApplyLeave()){
					checkForValidLeaveDates();
				}
			});
		});

	function loadLeaveDatesCheck(invalidDates) {
		if(invalidDates.length > 0) {
			setError ( "userId" , 37 , "Leave details", invalidDates  );
			isDateReady = false;
		}
		if(showValidationErrors("validationMessages_parent"))
			return true;			
		else {
			confirmSave("Are you sure you want to continue?", 
					function(result) {
				if(result){
                    document.getElementById("applyLeaveForm").action="${pageContext.request.contextPath}/leave.controller.applyLeaveForUsers.task";
					document.getElementById("applyLeaveForm").submit();
				}
			});
		}
	}
	
	$(function() {
		$("#clearBtn").click(function() {
			clearField("userId");
			clearField("fromDate");
			clearField("toDate");
			clearField("reason");
		});
	});
	
	</script>
</head>

<body onload="keepSubMenuSelected()">
<form method="post" id="applyLeaveForm">
	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>

	<!-- View Content -->
	<div class="content-container">
		<div id="box" style="width: 100%;">
			<!-- The value attribute of the following 
				"pageId" field should hold the value of 
				"menuId" coming from database for this page. -->
			<input type="hidden" id="pageId" value="35" />
			<div class="pagetitle">
				<h3>
					<spring:message code="applyLeave.header.text"></spring:message>
				</h3>
			</div>
	
			<jsp:include page="../../includes/global_validations.jsp"></jsp:include>

			<div class="data_container_color1">
				<div class="data_label" id="label_userId"><spring:message code="platform.text.username"/>&nbsp;:</div>
				<div class="data_text">
					<div id="div_id_15" >
						<select id="userId" name="userId" style="width:170px;">
							<option value=""></option>
							<c:forEach var="userDetails" items="${userList}">  
						    	<option value="${userDetails.userId}">${userDetails.userName}</option>
						 	</c:forEach>					
						</select>
					</div>
				</div>
			</div>
			<div id="dateFromRange">
				<div class="data_container_color2">
					<div class="data_label" id="label_fromDate"><spring:message code="applyLeave.text.fromdate"></spring:message>&nbsp;:</div>
					<div class="data_text">
						<div style="float:left">
							<input type="text" id="fromDate" name="fromDate" readonly="readonly"  style="width:70px"
								value="${userLeaveVO.fromDate}"/>
							<div class="calendar-icon" onclick="javascript:clearField('fromDate')" id="calBut1" title="<spring:message code='tooltip.platform.calendar'/>"></div>
							<div class="clear-icon" onclick="javascript:clearField('fromDate')" title="<spring:message code='tooltip.platform.clear'/>"></div>				
							<script>
							   Calendar.setup({
							        inputField : "fromDate",
							        trigger    : "calBut1",
							        onSelect   : function() { 
							        				this.hide();
							        			 },
							        showTime   : false,
							        dateFormat : "%d/%m/%Y",
							        disabled   : function(date) {
							        	if (date.getDay() == 0 || date.getDay() == 6) {
							                return true;
							            } else {
							                return false;
							            }
							        }
							    });
							</script>
						</div>
					</div>
				</div>
			</div>
	
			<div id="dateToRange">
				<div class="data_container_color1">
					<div class="data_label" id="label_toDate"><spring:message code="applyLeave.text.todate"></spring:message>&nbsp;:</div>
					<div class="data_text">
						<div style="float:left">
							<input type="text" id="toDate" name="toDate" readonly="readonly"  style="width:70px"
								value="${userLeaveVO.toDate}"/>
							<div class="calendar-icon" onclick="javascript:clearField('toDate')" id="calBut2" title="<spring:message code='tooltip.platform.calendar'/>"></div>
							<div class="clear-icon" onclick="javascript:clearField('toDate')" title="<spring:message code='tooltip.platform.clear'/>"></div>				
							<script>
							   Calendar.setup({
							        inputField : "toDate",
							        trigger    : "calBut2",
							        onSelect   : function() { 
							        				this.hide();
							        			 },
							        showTime   : false,
							        dateFormat : "%d/%m/%Y",
							        disabled   : function(date) {
							            if (date.getDay() == 0 || date.getDay() == 6) {
							                return true;
							            } else {
							                return false;
							            }
							        }	
							    });
							</script>
						</div>
					</div>
				</div>
			</div>	
			<div class="data_container_color2">
				<div class="data_label" id="label_reason"><spring:message code="applyLeave.text.reason"/>&nbsp;:</div>
				<div class="data_text">
					<div id="div_id_15" >
						<select id="reason" name="reason" style="width:170px;">
							<option value=""></option>
							<c:forEach var="reasons" items="reasonList"> 
						    	<option value="<spring:message code="leave.type.1"/>"><spring:message code="leave.type.1"/></option>
						    	<option value="<spring:message code="leave.type.2"/>"><spring:message code="leave.type.2"/></option>
						    	<option value="<spring:message code="leave.type.3"/>"><spring:message code="leave.type.3"/></option>
						 	</c:forEach>					
						</select>
					</div>
				</div>
			</div>
			<br/>
			
			<div style="text-align:center;">
				<ul class="btn-wrapper">
					<li class="btn-inner" id="applyBtn">
						<span>
							<spring:message code="applyLeave.button.apply" />
						</span>
					</li>
				</ul>
				<ul class="btn-wrapper">
					<li class="btn-inner" id="clearBtn">
						<span>
							<spring:message code="platform.button.clear" />
						</span>
					</li>
				</ul>
			</div>
			<br/>
		</div>
	</div>
	
	<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
</form>
</body>
</html>