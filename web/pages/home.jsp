<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/frameWorkFunctions.tld" prefix="frameWork"%>
<%
	response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setHeader("Cache-Control", "no-store");
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<jsp:include page="../includes/global_javascript.jsp"></jsp:include>
<jsp:include page="../includes/global_css.jsp"></jsp:include>
<script
	src="${pageContext.request.contextPath}/appScripts/adminConfig.js"
	type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/appScripts/utils.js"
	type="text/javascript"></script>
<script type='text/javascript'
	src='${pageContext.request.contextPath}/dwr/interface/statsService.js'></script>
<script type='text/javascript'
	src='${pageContext.request.contextPath}/dwr/engine.js'></script>

<style type="text/css">
#cust_stats,#user_stats {
	left: 0;
	margin: 30px auto;
	margin-left: 10px;
	position: relative;
	top: 0;
	width: 100%;
}

#stats {
	width: 60%;
	float: left;
}

#announcements {
	width: 35%;
	/*float: right;*/
	padding-left: 440px;
	padding-top: 30px;
}

.stat_header {
	width: 500px;
	height: 20px;
	text-align: left;
	margin-top: 0px;
	margin-left: 5px;
	float: left;
	text-decoration: underline;
	font-weight: bold;
}

.stat_text {
	float: left;
	padding-top: 6px;
	width: 50px;
	text-align: right;
}

.stat_text_nextline {
	float: left;
	padding-top: 19px;
	width: 50px;
	text-align: right;
}
</style>
<script>
	// reloads statistics for every 5 minutes
	window.setTimeout("reloadCustomerStats()", 5 * 60 * 1000);
	function reloadCustomerStats() {
		statsService.getCustomerStatsForUser(
				"${sessionScope['userDetails'].userId}", updateCustomerStats);
	}

	function updateCustomerStats(customerStats) {
		updateCustomerStatistics(customerStats);
		window.setTimeout("reloadCustomerStats()", 5 * 60 * 1000);
	}
</script>
</head>
<body onload="keepMenuSelected()">
	<jsp:include page="../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../includes/menus.jsp"></jsp:include>


	<!-- View Content -->
	<div class="content-container" style="min-height: 0px;">
		<div id="box" style="width: 100%;">
			<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
			<input type="hidden" id="pageId" value="1" />
			<div class="pagetitle">
				<h3>
					<spring:message code="platform.text.welcome"></spring:message>
				</h3>
			</div>
			<c:choose>
				<c:when
					test="${sessionScope['userDetails'].roleMaster.roleDescription eq 'ROLE_INSURER'}">
					<div style="height: 100px; float: left;">&nbsp;</div>
					<div style="height: 100px;">&nbsp;</div>
					<div class="user-stat">
						<c:if test="${frameWork:isNotNull(customerStatsVO)}">
							<b><spring:message code="home.usr.stats.header.text"></spring:message></b>
							<br />
							<spring:message code="home.usr.stats.text.loggedInUser" />
							<b>${sessionScope['userDetails'].fname}
								${sessionScope['userDetails'].sname}</b>,
						<c:if test="${frameWork:isNotNull(customerStatsVO.lastLoginDate)}">
								<spring:message code="home.usr.stats.text.lastLoginDate" />&nbsp;
							<b>${customerStatsVO.lastLoginDate}</b>
							</c:if>
						</c:if>
					</div>
				</c:when>
				<c:otherwise>

					<c:if test="${frameWork:isNotNull(customerStatsVO)}">

						<div class="stat-left" style="height: 260px;">
							<h3>
								<spring:message code="home.cust.stats.header.text"></spring:message>
							</h3>
							<br />
							
	<div style="height: 210px; overflow:scroll;overflow:auto">
							<c:if
								test="${frameWork:isNotNull(customerStatsVO.xlRegByUserConfirmed)}">
								<div>
									<div class="stat_label">
										<spring:message
											code="home.cust.stats.text.xlRegByUserConfirmed" />
									</div>
									<div id="xlRegByUserConfirmed" class="stat_text">
										<fmt:formatNumber
											value="${customerStatsVO.xlRegByUserConfirmed}"
											groupingUsed="true" type="number" />
									</div>
								</div>
							</c:if>
							<c:if
								test="${frameWork:isNotNull(customerStatsVO.xlRegByUserPending)}">
								<div>
									<div class="stat_label">
										<spring:message code="home.cust.stats.text.xlRegByUserPending" />
									</div>
									<div id="xlRegByUserPending" class="stat_text">
										<fmt:formatNumber
											value="${customerStatsVO.xlRegByUserPending}"
											groupingUsed="true" type="number" />
									</div>
								</div>
							</c:if>
							
							<c:if
								test="${frameWork:isNotNull(customerStatsVO.xlConfirmedByFullyDeducted)}">
								<div>
									<div class="stat_label">
										<spring:message
											code="home.cust.stats.text.xlConfirmedByFullyDeducted" />
									</div>
									<div id="xlConfirmedByFullyDeducted" class="stat_text_nextline">
										<fmt:formatNumber
											value="${customerStatsVO.xlConfirmedByFullyDeducted}"
											groupingUsed="true" type="number" />
									</div>
								</div>
							</c:if>	
							
							<c:if
								test="${frameWork:isNotNull(customerStatsVO.xlConfirmedByPartiallyDeducted)}">
								<div>
									<div class="stat_label">
										<spring:message
											code="home.cust.stats.text.xlConfirmedByPartiallyDeducted" />
									</div>
									<div id="xlConfirmedByPartiallyDeducted" class="stat_text_nextline">
										<fmt:formatNumber
											value="${customerStatsVO.xlConfirmedByPartiallyDeducted}"
											groupingUsed="true" type="number" />
									</div>
								</div>
							</c:if>	
							<c:if
								test="${frameWork:isNotNull(customerStatsVO.xlConfirmedInFully_PartiallyDeducted)}">
								<div>
									<div class="stat_label">
										<spring:message
											code="home.cust.stats.text.xlConfirmedInFully_PartiallyDeducted" />
									</div>
									<div id="xlConfirmedInFully_PartiallyDeducted" class="stat_text_nextline">
										<fmt:formatNumber
											value="${customerStatsVO.xlConfirmedInFully_PartiallyDeducted}"
											groupingUsed="true" type="number" />
									</div>
								</div>
							</c:if>	
							
						<%-- 	<c:if
								test="${frameWork:isNotNull(customerStatsVO.xlRegByUserDeducted)}">
								<div>
									<div class="stat_label">
										<spring:message
											code="home.cust.stats.text.xlCnfByUserDeducted" />
									</div>
									<div id="xlRegByUserDeducted" class="stat_text">
										<fmt:formatNumber
											value="${customerStatsVO.xlRegByUserDeducted}"
											groupingUsed="true" type="number" />
									</div>
								</div>
							</c:if>		 --%>					
							<c:if
								test="${frameWork:isNotNull(customerStatsVO.hospRegByUserConfirmed)}">
								<div>
									<div class="stat_label">
										<spring:message
											code="home.cust.stats.text.hospRegByUserConfirmed" />
									</div>
									<div id="hospRegByUserConfirmed" class="stat_text_nextline">
										<fmt:formatNumber
											value="${customerStatsVO.hospRegByUserConfirmed}"
											groupingUsed="true" type="number" />
									</div>
								</div>
							</c:if>
							<c:if
								test="${frameWork:isNotNull(customerStatsVO.hospRegByUserPending)}">
								<div>
									<div class="stat_label">
										<spring:message
											code="home.cust.stats.text.hospRegByUserPending" />
									</div>
									<div id="hospRegByUserPending" class="stat_text_nextline">
										<fmt:formatNumber
											value="${customerStatsVO.hospRegByUserPending}"
											groupingUsed="true" type="number" />
									</div>
								</div>
							</c:if>		
							
							<c:if
								test="${frameWork:isNotNull(customerStatsVO.hospConfirmedByFullyDeducted)}">
								<div>
									<div class="stat_label">
										<spring:message
											code="home.cust.stats.text.hospConfirmedByFullyDeducted" />
									</div>
									<div id="hospConfirmedByFullyDeducted" class="stat_text_nextline">
										<fmt:formatNumber
											value="${customerStatsVO.hospConfirmedByFullyDeducted}"
											groupingUsed="true" type="number" />
									</div>
								</div>
							</c:if>		
							
							<c:if
								test="${frameWork:isNotNull(customerStatsVO.hospConfirmedByPartiallyDeducted)}">
								<div>
									<div class="stat_label">
										<spring:message
											code="home.cust.stats.text.hospConfirmedByPartiallyDeducted" />
									</div>
									<div id="hospConfirmedByPartiallyDeducted" class="stat_text_nextline">
										<fmt:formatNumber
											value="${customerStatsVO.hospConfirmedByPartiallyDeducted}"
											groupingUsed="true" type="number" />
									</div>
								</div>
							</c:if>		
							
							<c:if
								test="${frameWork:isNotNull(customerStatsVO.hospConfirmedInFully_PartiallyDeducted)}">
								<div>
									<div class="stat_label">
										<spring:message
											code="home.cust.stats.text.hospConfirmedInFully_PartiallyDeducted" />
									</div>
									<div id="hospConfirmedInFully_PartiallyDeducted" class="stat_text_nextline">
										<fmt:formatNumber
											value="${customerStatsVO.hospConfirmedInFully_PartiallyDeducted}"
											groupingUsed="true" type="number" />
									</div>
								</div>
							</c:if>							
						<%-- 	<c:if
								test="${frameWork:isNotNull(customerStatsVO.hospRegByUserDeducted)}">
								<div>
									<div class="stat_label">
										<spring:message
											code="home.cust.stats.text.hospCnfByUserDeducted" />
									</div>
									<div id="hospRegByUserDeducted" class="stat_text">
										<fmt:formatNumber
											value="${customerStatsVO.hospRegByUserDeducted}"
											groupingUsed="true" type="number" />
									</div>
								</div>
							</c:if>
							 --%>
							
							<c:if
								test="${frameWork:isNotNull(customerStatsVO.ipRegByUserConfirmed)}">
								<div>
									<div class="stat_label">
										<spring:message
											code="home.cust.stats.text.ipRegByUserConfirmed" />
									</div>
									<div id="ipRegByUserConfirmed" class="stat_text_nextline">
										<fmt:formatNumber
											value="${customerStatsVO.ipRegByUserConfirmed}"
											groupingUsed="true" type="number" />
									</div>
								</div>
							</c:if>
							<c:if
								test="${frameWork:isNotNull(customerStatsVO.ipRegByUserPending)}">
								<div>
									<div class="stat_label">
										<spring:message
											code="home.cust.stats.text.ipRegByUserPending" />
									</div>
									<div id="ipRegByUserPending" class="stat_text_nextline">
										<fmt:formatNumber
											value="${customerStatsVO.ipRegByUserPending}"
											groupingUsed="true" type="number" />
									</div>
								</div>
							</c:if>			
							
														<c:if
								test="${frameWork:isNotNull(customerStatsVO.ipConfirmedByFullyDeducted)}">
								<div>
									<div class="stat_label">
										<spring:message
											code="home.cust.stats.text.ipConfirmedByFullyDeducted" />
									</div>
									<div id="ipConfirmedByFullyDeducted" class="stat_text_nextline">
										<fmt:formatNumber
											value="${customerStatsVO.ipConfirmedByFullyDeducted}"
											groupingUsed="true" type="number" />
									</div>
								</div>
							</c:if>			
							<c:if
								test="${frameWork:isNotNull(customerStatsVO.ipConfirmedByPartiallyDeducted)}">
								<div>
									<div class="stat_label">
										<spring:message
											code="home.cust.stats.text.ipConfirmedByPartiallyDeducted" />
									</div>
									<div id="ipConfirmedByPartiallyDeducted" class="stat_text_nextline">
										<fmt:formatNumber
											value="${customerStatsVO.ipConfirmedByPartiallyDeducted}"
											groupingUsed="true" type="number" />
									</div>
								</div>
							</c:if>		
							
							<c:if
								test="${frameWork:isNotNull(customerStatsVO.ipConfirmedInFully_PartiallyDeducted)}">
								<div>
									<div class="stat_label">
										<spring:message
											code="home.cust.stats.text.ipConfirmedInFully_PartiallyDeducted" />
									</div>
									<div id="ipConfirmedInFully_PartiallyDeducted" class="stat_text_nextline">
										<fmt:formatNumber
											value="${customerStatsVO.ipConfirmedInFully_PartiallyDeducted}"
											groupingUsed="true" type="number" />
									</div>
								</div>
							</c:if>								
							<%-- <c:if
								test="${frameWork:isNotNull(customerStatsVO.ipRegByUserDeducted)}">
								<div>
									<div class="stat_label">
										<spring:message
											code="home.cust.stats.text.ipCnfByUserDeducted" />
									</div>
									<div id="ipRegByUserDeducted" class="stat_text">
										<fmt:formatNumber
											value="${customerStatsVO.ipRegByUserDeducted}"
											groupingUsed="true" type="number" />
									</div>
								</div>
							</c:if> --%>
							
</div>
						</div>
						<div class="stat-right" style="height: 260px;">
							<h3>
								<spring:message code="home.announce.stats.header.text"></spring:message>
							</h3>
							<br />
							<c:if
								test="${frameWork:isNotNull(customerStatsVO.marqueeMessage)}">

								<!-- To escape commas in the announcement message -->
								<c:set var="marqueeMessage"
									value="${customerStatsVO.marqueeMessage}"></c:set>
								<c:choose>
									<c:when test="${fn:contains(marqueeMessage, ',')}">
										<marquee behavior="scroll" direction="up" scrolldelay="280"
											width="200" height="140"> <spring:message
											code="home.stats.marquee.message"
											arguments="${fn:replace(marqueeMessage, ',', '&#44;')}" /> </marquee>
									</c:when>
									<c:otherwise>
										<marquee behavior="scroll" direction="up" scrolldelay="280"
											width="200" height="140"> <spring:message
											code="home.stats.marquee.message"
											arguments="${marqueeMessage}" /> </marquee>
									</c:otherwise>
								</c:choose>
							</c:if>
						</div>

						<div class="user-stat">
							<b><spring:message code="home.usr.stats.header.text"></spring:message></b><br />
							<spring:message code="home.usr.stats.text.loggedInUser" />
							<b>${sessionScope['userDetails'].fname}
								${sessionScope['userDetails'].sname}</b>,
							<c:if
								test="${frameWork:isNotNull(customerStatsVO.lastLoginDate)}">
								<spring:message code="home.usr.stats.text.lastUpdateTimeStamp" />&nbsp;<b
									id="lastUpdateTimeStamp">${customerStatsVO.lastUpdateTimeStamp}</b>
							</c:if>

						</div>
					</c:if>
				</c:otherwise>
			</c:choose>

		</div>
	</div>

	<jsp:include page="../includes/global_footer.jsp"></jsp:include>
</body>
</html>

