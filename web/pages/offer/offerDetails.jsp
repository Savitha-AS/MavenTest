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
		
			/* $(function() {
				$( "button, input:button, input:submit").button();
			}); */
			
			$(function() {
				$("#backBtn").click(function() {  
					document.getElementById("offerDetailsFrm").action="${pageContext.request.contextPath}/offers.controller.listOffers.task";
					document.getElementById("offerDetailsFrm").submit();
				});
			});	
	</script>
		
    </head>
    <body onload="keepSubMenuSelected()">
    <form id="offerDetailsFrm" method="post">
     <jsp:include page="../../includes/global_header.jsp"></jsp:include>
	 <jsp:include page="../../includes/menus.jsp"></jsp:include>
	 
	<!-- View Content -->	
	<div class="content-container">
	<div id="box" style="width: 100%;">	
		<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
		<input type="hidden" id="pageId" value="24" />	
		<div class="pagetitle">
			<h3><spring:message code="viewoffer.header.text"></spring:message>
			</h3>			
		</div>	
			
			<br/>				
			<div class="data_container_color1">
				<div class="data_label"><spring:message code="offer.text.offername"></spring:message>&nbsp;:</div>
				<div class="data_text">${createOfferVO.offerName}</div>
			</div>
			
			<div class="data_container_color2">
				<div class="data_label"><spring:message code="offer.text.offertype"></spring:message>&nbsp;:</div>
				<div class="data_text">${createOfferVO.offerType}</div>
			</div>		
			<br/>	
			
			<fieldset>
				<legend>											
					<spring:message code="offer.text.offerrange"></spring:message> (<spring:message code="platform.text.currency"></spring:message>)&nbsp;:
				</legend>	
				<div id="mainDiv">
					<div id="coverageHead" class="mini_table_color2">
						<div class="mini_table_td mini_table_th"><spring:message code="offer.text.offeredcover"></spring:message></div>
						<div class="mini_table_td mini_table_th"><spring:message code="offer.text.amttobepaid"></spring:message></div>
					</div>
					<c:forEach var="offerCover" items="${offerCoverDetailsList}" varStatus="loopCounter">  							
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
								name="offerCover" size="5" value="${offerCover.offerCover}"  
								class="${cssClass}" style="border:0px; width:50px; text-align: right; margin-top: -2px;" 
								readonly="readonly" />								
							</div>
							<div class="mini_table_td">
								<input type="text" id="txt_${loopCounter.count}2" 
								name="offerCoverCharges" size="5" value="${offerCover.offerCoverCharges}"  
								class="${cssClass}" style="border:0px; width:50px; text-align: right; margin-top: -2px;" 
								readonly="readonly" />									
							</div>
						</div> 
					</c:forEach>
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

