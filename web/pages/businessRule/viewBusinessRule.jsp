<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
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
	
	<script>		
	/*	$(function() {
			$( "button, input:button, input:submit").button();
		}); */
		
		$(function() {
			$("#backBtn").click(function() {  
				document.getElementById("offerDetailsFrm").action="${pageContext.request.contextPath}/businessRule.controller.listBusinessRules.task";
				document.getElementById("offerDetailsFrm").submit();
			});
		});	
	</script>
</head>
    <body onload="keepSubMenuSelected();">
    <form id="offerDetailsFrm" method="post">
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
			<h3><spring:message code="viewBR.header.text"></spring:message>
			</h3>			
		</div>	
			
			<br/>				
			<div class="data_container_color1">
				<div class="data_label" style="width: 200px;"><spring:message code="viewBR.text.BRVersion"></spring:message>&nbsp;:</div>
				<div class="data_text">${businessRule.brVersion}</div>
			</div>
			
			<div class="data_container_color2">
				<div class="data_label" style="width: 200px;"><spring:message code="BR.text.inscmpnyname"></spring:message>&nbsp;:</div>
				<div class="data_text">${businessRule.insuranceCompany.insCompName}</div>
			</div>
			
			<div class="data_container_color1">
				<div class="data_label" style="width: 200px;"><spring:message code="BR.text.premiumpercent"></spring:message>&nbsp;:</div>
				<div class="data_text">${businessRule.premiumAmtPerc}</div>
			</div>	
			<br/>
											
			<fieldset>
				<legend id="label_busRuleRange">											
					<spring:message code="BR.text.coveragelevel"></spring:message> (<spring:message code="platform.text.currency"></spring:message>)&nbsp;:&nbsp;
				</legend>
							
				<div>
					<div id="mainDiv">
						<div id="coverageHead" class="mini_table_color2">
					       <div class="mini_table_td mini_table_th"><spring:message code="platform.text.from"></spring:message> (&gt;=)</div>
					       <div class="mini_table_td mini_table_th"><spring:message code="platform.text.to"></spring:message> (&lt;)</div>
					       <div class="mini_table_td mini_table_th" style="width:120px;"><spring:message code="BR.text.assuredcover"></spring:message></div>
				        </div>
				        
				        
						<c:forEach var="busRuleDef" items="${businessRule.busRuleDef}" varStatus="loopCounter"> 							
							<c:choose>
								<c:when test="${loopCounter.count%2 eq 1}">
									<c:set var="cssClass" value="mini_table_color1"></c:set>
								</c:when>
								<c:otherwise>
									<c:set var="cssClass" value="mini_table_color2"></c:set>								
								</c:otherwise>
							</c:choose>
							  <div id="coverage${loopCounter.count}" class="${cssClass}">
								<div class="mini_table_td">
									<input type="text" id="txt_${loopCounter.count}1" 
									name="brRangeFrom" size="5" value="${busRuleDef.brRangeFrom}"  
									class="${cssClass}" style="border:0px; width:50px; text-align: right; margin-top: -2px;" 
									readonly="readonly" />									
								</div>
								<div class="mini_table_td">
									<input type="text" id="txt_${loopCounter.count}2" 
									name="brRangeTo" size="5" value="${busRuleDef.brRangeTo}"  
									class="${cssClass}" style="border:0px; width:50px; text-align: right; margin-top: -2px;" 
									readonly="readonly" />									
								</div>
								<div class="mini_table_td">
									<input type="text" id="txt_${loopCounter.count}3" 
									name="brRangeVal" size="5" value="${busRuleDef.brRangeVal}"  
									class="${cssClass}" style="border:0px; width:50px; text-align: right; margin-top: -2px;" 
									readonly="readonly" />									
								</div>
							  </div> 
						</c:forEach>
					</div>
				</div>
				<br/>
			</fieldset>
			
			<br/>
			<div style="text-align:center;">
			<ul class="btn-wrapper"><li class="btn-inner" id="backBtn"><span><spring:message code="platform.button.back"/></span></li></ul>			
		    </div>
		    <br/>		   
		</div>	
		</div>	
	  <jsp:include page="../../includes/global_footer.jsp"></jsp:include>
     </form>
    </body>
</html>

