<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
	<script src="${pageContext.request.contextPath}/appScripts/utils.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/appScripts/leave.js" type="text/javascript"></script>
	<script>

	/* $(function() {
		$( "button, input:button, input:submit").button();
	}); */
	
	$(function() {
			
		$("#viewBtn").click(function() { 
			if(!validateViewLeave()){
				document.getElementById("viewLeaveForm").action="${pageContext.request.contextPath}/leave.controller.viewLeaves.task";
				document.getElementById("viewLeaveForm").submit();
			}
		});
		
		
		$("#leaveRange").change(function(){
			if($("#leaveRange").val()==4) {
				$("#dateRangeDiv").show('slow');
				$("#fromDate").val("");
				$("#toDate").val("");	
			}
			else
				$("#dateRangeDiv").hide('slow');
		});
		
	});
	
	$(document).ready(function() 
	{
		$("#div_searchResults").show('slow');
	});
	
	</script>
	<link href="${pageContext.request.contextPath}/theme/css/displaytag.css" rel="stylesheet" type="text/css"/>
</head>

<body onload="keepSubMenuSelected()">
<form method="post" id="viewLeaveForm">
	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>

	<!-- View Content -->
	<div class="content-container">
		<div id="box" style="width: 100%;">
			<!-- The value attribute of the following 
				"pageId" field should hold the value of 
				"menuId" coming from database for this page. -->
			<input type="hidden" id="pageId" value="38" />
			<div class="pagetitle">
				<h3>
					<spring:message code="leave.view.header.text"></spring:message>
				</h3>
			</div>
	
			<jsp:include page="../../includes/global_validations.jsp"></jsp:include>

			<div class="data_container_color1">
				<div class="data_label" id="label_leaveRange" style="width: 200px;"><spring:message code="leave.text.select"/>&nbsp;:</div>
				<div class="data_text">
					<div id="div_id_15" >
						<select id="leaveRange" name="leaveRange" style="width:125px;">
							<option value="1"><spring:message code="leave.text.today"/></option>
							<option value="2"><spring:message code="leave.text.current.week"/></option>
							<option value="3"><spring:message code="leave.text.current.month"/></option>
							<option value="4"><spring:message code="leave.text.custom"/></option>					
						</select>
					</div>
				</div>
			</div>
			
			<div class="data_container_color2" id="dateRangeDiv" style="display: none;">
				<div class="data_label" id="label_dateRange" style="width: 200px;"><spring:message code="leave.text.date.range"/>&nbsp;:</div>
				<div class="data_text">
					<div style="float: left; margin-top: 3px;" id="label_fromDate">
						<spring:message code="platform.text.from" />&nbsp;&nbsp;
					</div>
					<div style="float: left">
						<input type="text" id="fromDate" name="fromDate" readonly="readonly" 
							style="width: 70px" value="" />
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
						        dateFormat : "%d/%m/%Y"	
						    });
						</script>
					</div>
					<div style="float: left; margin-top: 3px;" id="label_toDate">
						&nbsp;&nbsp;<spring:message code="platform.text.to" />&nbsp;&nbsp;
					</div>
					<div>
						<input type="text" id="toDate" name="toDate" readonly="readonly" 
							style="width: 70px" value="" />
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
						        dateFormat : "%d/%m/%Y"
						    });
						</script>
					</div>
				</div>
			</div>
			<br />
			<div style="text-align:center;">
				<ul class="btn-wrapper"><li class="btn-inner" id="viewBtn"><span><spring:message code="platform.button.view" /></span></li></ul>
			</div>
			<br/>
			<div style="overflow-x: auto; display: none;" id="div_searchResults">		
				<c:if test="${frameWork:isNotNull(userLeaveList)}">
				<c:choose>
					<c:when test="${leaveRangeType == 4}">
						<c:set var="requestParams" value="leaveRange=${leaveRangeType}&fromDate=${VO_USERLEAVE.fromDate}&toDate=${VO_USERLEAVE.toDate}" />
					</c:when>
					<c:otherwise>
						<c:set var="requestParams" value="leaveRange=${leaveRangeType}" />
					</c:otherwise>
				</c:choose>
					<display:table id="userLeaveList" name="userLeaveList" 
						requestURI="/leave.controller.viewLeaves.task?${requestParams}" excludedParams="*"
						pagesize="10" style="width:690px;" cellspacing="0"
						cellpadding="0">
					
						<display:setProperty name="basic.msg.empty_list" 
							value="No user records found matching the specified criteria.">
						</display:setProperty>
						
						<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
							titleKey="platform.text.username" property="user.userName"
							maxLength="40"> 	
						</display:column>
						
						<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
							titleKey="leave.text.date" property="leaveDate"
							maxLength="12" format="{0,date,dd-MM-yyyy}">
						</display:column>
						
						<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
							titleKey="leave.text.reason" property="reason"
							maxLength="5">
						</display:column>
					</display:table>
				</c:if>
			</div>
			<br/>
		</div>
	</div>
	
	<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
</form>
<script>
	loadDropDownList('leaveRange', '${leaveRangeType}');
	if('${leaveRangeType}' == 4) {
		$("#fromDate").val('${VO_USERLEAVE.fromDate}');
		$("#toDate").val('${VO_USERLEAVE.toDate}');
		$("#dateRangeDiv").show();
	}
		
</script>
</body>
</html>