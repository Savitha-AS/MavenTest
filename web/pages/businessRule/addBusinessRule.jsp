<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
	<script src="${pageContext.request.contextPath}/appScripts/businessRule.js" type="text/javascript"></script>
	<script>
	/*	$(function(){
			$( "button, input:button, input:submit").button();
		}); */
		
		$(function(){
			$("#saveBtn").click(function(){ 
				if(!validateCreateBR()){
					confirmSave("", function(result) {
						if(result){
							//var frm = document.forms[0];
			                //frm.action="${pageContext.request.contextPath}/businessRule.controller.saveBusinessRule.task";
		                    //frm.submit();
		                    document.getElementById("businessRule").action="${pageContext.request.contextPath}/businessRule.controller.saveBusinessRule.task";
							document.getElementById("businessRule").submit();
						}
					});				
				}
			});
		});
		
		$(function(){
			$("body").keypress(function(e){
				if (e.keyCode == '13') {
			 		$("#saveBtn").click(); 
				}
		 	});
		});

		$(function() {
			$("#clearBtn").click(function() {
				clearField("selectInsCmpny");
				clearField("premiumAmt");

				//clearField("txt_13");
				clearField("txt_14");
				clearField("txt_15");
				
				if($("#txt_23").is(':visible'))
					hideCurrentCoverRange('coverage',2);
				if($("#txt_33").is(':visible'))
					hideCurrentCoverRange('coverage',3);
				if($("#txt_43").is(':visible'))
					hideCurrentCoverRange('coverage',4);
				if($("#txt_53").is(':visible'))
					hideCurrentCoverRange('coverage',5);
				if($("#txt_63").is(':visible'))
					hideCurrentCoverRange('coverage',6);
				if($("#txt_73").is(':visible'))
					hideCurrentCoverRange('coverage',7);
				if($("#txt_83").is(':visible'))
					hideCurrentCoverRange('coverage',8);
			});
		});
	</script>
</head>

<body onload="keepSubMenuSelected()">
	<form method="post" id="businessRule">
	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>
	
	<!-- View Content -->	
	<div class="content-container">
	<div id="box" style="width: 100%;">
		<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->	
		<input type="hidden" id="pageId" value="20" />	
		<div class="pagetitle">
			<h3><spring:message code="addBR.header.text"></spring:message>
			</h3>			
		</div>	
		<div style="text-align: right; margin-right: 5px;"><spring:message code="platform.text.mandatory"></spring:message></div>
		<br/>
		<jsp:include page="../../includes/global_validations.jsp"></jsp:include>
		
		<div class="data_container_color1">
			<div class="data_label" id="label_selectInsCmpny" style="width: 200px;"><spring:message code="BR.text.inscmpnyname"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<select id="selectInsCmpny" name="insurer" style="width:180px;">
				  <option></option>
					<c:forEach var="insuranceCompany" items="${insuranceCompList}">  
                       <option value="${insuranceCompany.insCompId}">${insuranceCompany.insCompName}</option>
                     </c:forEach>      
				</select>
			</div>
		</div>
		
		<div class="data_container_color2">
			<div class="data_label" id="label_premiumAmt" style="width: 200px;"><spring:message code="BR.text.premiumpercent"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<div id="div_id_2">
					<input type="text" id="premiumAmt" name="premiumAmtPerc" size="7" maxlength="7" />&nbsp;&nbsp;&nbsp;%
				</div>
			</div>
		</div>
		<br/>		
		
		<fieldset>
			<legend id="label_busRuleRange">											
				<spring:message code="BR.text.coveragelevel"></spring:message> (<spring:message code="platform.text.currency"></spring:message>)&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
			</legend>
			<div id="mainDiv">
				<div id="coverageHead" class="mini_table_color1">
					<div class="mini_table_td mini_table_th"><spring:message code="platform.text.from"></spring:message> (&gt;=)</div>
					<div class="mini_table_td mini_table_th"><spring:message code="platform.text.to"></spring:message> (&lt;)</div>
					<div class="mini_table_td mini_table_th" style="width:120px;"><spring:message code="BR.text.assuredcover"></spring:message></div>
				</div>
				<div id="coverage1" class="mini_table_color2">
					<div class="mini_table_td">
						<input type="text" id="txt_13" size="5" name="from" maxlength="5" value="0" readonly="readonly"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
					</div>
					<div class="mini_table_td">
						<input type="text" id="txt_14" size="5" name="to" maxlength="5"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
					</div>
					<div class="mini_table_td">
						<input type="text" id="txt_15" size="5" name="assuredCover" maxlength="5"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
					</div>
					<div class="mini_table_td" id="divAddRemBtn1" >
						<div class="add-icon" onclick="javascript:showNextCoverRange('coverage',1)" title="<spring:message code='tooltip.platform.add.new.row'/>"></div>
					</div>
				</div>
				<div id="coverage2" class="mini_table_color1" style="display:none;">
					<div class="mini_table_td">
						<input type="text" id="txt_23" readonly="readonly" size="5" name="from" maxlength="5"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
					</div>                                                                            
					<div class="mini_table_td">                                                       
						<input type="text" id="txt_24" size="5" name="to" maxlength="5"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
					</div>
					<div class="mini_table_td">
						<input type="text" id="txt_25" size="5" name="assuredCover" maxlength="5"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
					</div>
					<div class="mini_table_td" id="divAddRemBtn2">
						<div class="add-icon" onclick="javascript:showNextCoverRange('coverage',2)" title="<spring:message code='tooltip.platform.add.new.row'/>"></div>
						<div class="delete-icon" onclick="javascript:hideCurrentCoverRange('coverage',2)" title="<spring:message code='tooltip.platform.delete.current.row'/>"></div>
					</div>
				</div>
				<div id="coverage3" class="mini_table_color2" style="display:none;">
					<div class="mini_table_td">
						<input type="text" id="txt_33" readonly="readonly" name="from" size="5" maxlength="5"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
					</div>                                                                             
					<div class="mini_table_td">                                                        
						<input type="text" id="txt_34" size="5" name="to" maxlength="5"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
					</div>
					<div class="mini_table_td">
						<input type="text" id="txt_35" size="5" name="assuredCover" maxlength="5"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
					</div>
					<div class="mini_table_td" id="divAddRemBtn3">
						<div class="add-icon" onclick="javascript:showNextCoverRange('coverage',3)" title="<spring:message code='tooltip.platform.add.new.row'/>"></div>
						<div class="delete-icon" onclick="javascript:hideCurrentCoverRange('coverage',3)" title="<spring:message code='tooltip.platform.delete.current.row'/>"></div>
					</div>
				</div>
				<div id="coverage4" class="mini_table_color1" style="display:none;">
					<div class="mini_table_td">
						<input type="text" id="txt_43" readonly="readonly" size="5" name="from" maxlength="5"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
					</div>                                                                             
					<div class="mini_table_td">                                                        
						<input type="text" id="txt_44" size="5" name="to" maxlength="5"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
					</div>
					<div class="mini_table_td">
						<input type="text" id="txt_45" size="5" name="assuredCover" maxlength="5"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
					</div>
					<div class="mini_table_td" id="divAddRemBtn4">
						<div class="add-icon" onclick="javascript:showNextCoverRange('coverage',4)" title="<spring:message code='tooltip.platform.add.new.row'/>"></div>
						<div class="delete-icon" onclick="javascript:hideCurrentCoverRange('coverage',4)" title="<spring:message code='tooltip.platform.delete.current.row'/>"></div>
					</div>
				</div>
				<div id="coverage5" class="mini_table_color2" style="display:none;">
					<div class="mini_table_td">
						<input type="text" id="txt_53" readonly="readonly" size="5" name="from" maxlength="5"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
					</div>                                                                             
					<div class="mini_table_td">                                                        
						<input type="text" id="txt_54" size="5" name="to" maxlength="5"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
					</div>
					<div class="mini_table_td">
						<input type="text" id="txt_55" size="5" name="assuredCover" maxlength="5"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
					</div>
					<div class="mini_table_td" id="divAddRemBtn5">
						<div class="add-icon" onclick="javascript:showNextCoverRange('coverage',5)" title="<spring:message code='tooltip.platform.add.new.row'/>"></div>
						<div class="delete-icon" onclick="javascript:hideCurrentCoverRange('coverage',5)" title="<spring:message code='tooltip.platform.delete.current.row'/>"></div>
					</div>
				</div>
				<div id="coverage6" class="mini_table_color1" style="display:none;">
					<div class="mini_table_td">
						<input type="text" id="txt_63" readonly="readonly" size="5" name="from" maxlength="5"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
					</div>                                                                             
					<div class="mini_table_td">                                                        
						<input type="text" id="txt_64" size="5" name="to" maxlength="5"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
					</div>
					<div class="mini_table_td">
						<input type="text" id="txt_65" size="5" name="assuredCover" maxlength="5"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
					</div>
					<div class="mini_table_td" id="divAddRemBtn6">
						<div class="add-icon" onclick="javascript:showNextCoverRange('coverage',6)" title="<spring:message code='tooltip.platform.add.new.row'/>"></div>
						<div class="delete-icon" onclick="javascript:hideCurrentCoverRange('coverage',6)" title="<spring:message code='tooltip.platform.delete.current.row'/>"></div>
					</div>
				</div>
				<div id="coverage7" class="mini_table_color2" style="display:none;">
					<div class="mini_table_td">
						<input type="text" id="txt_73" readonly="readonly" size="5" name="from" maxlength="5"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
					</div>                                                                             
					<div class="mini_table_td">                                                        
						<input type="text" id="txt_74" size="5" name="to" maxlength="5" />&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
					</div>
					<div class="mini_table_td">
						<input type="text" id="txt_75" size="5" name="assuredCover" maxlength="5"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
					</div>
					<div class="mini_table_td" id="divAddRemBtn7">
						<div class="add-icon" onclick="javascript:showNextCoverRange('coverage',7)" title="<spring:message code='tooltip.platform.add.new.row'/>"></div>
						<div class="delete-icon" onclick="javascript:hideCurrentCoverRange('coverage',7)" title="<spring:message code='tooltip.platform.delete.current.row'/>"></div>
					</div>
				</div>
				<div id="coverage8" class="mini_table_color1" style="display:none;">
					<div class="mini_table_td">
						<input type="text" id="txt_83" readonly="readonly" size="5" name="from" maxlength="5"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
					</div>
					<div class="mini_table_td">
						<input type="text" id="txt_84" size="5" name="to" maxlength="5"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
					</div>
					<div class="mini_table_td">
						<input type="text" id="txt_85" size="5"  name="assuredCover" maxlength="5"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
					</div>
					<div class="mini_table_td" id="divAddRemBtn8">
						<div class="delete-icon" onclick="javascript:hideCurrentCoverRange('coverage',8)" title="<spring:message code='tooltip.platform.delete.current.row'/>"></div>
					</div>
				</div>
			</div>
			<br/>
		</fieldset>
		<br/>
		
		<div style="text-align: center;">		
			<ul class="btn-wrapper"><li class="btn-inner" id="saveBtn"><span><spring:message code="platform.button.save"/></span></li></ul>
			<ul class="btn-wrapper"><li class="btn-inner" id="clearBtn"><span><spring:message code="platform.button.clear"/></span></li></ul>
		</div>
		<br/>
		
		<c:set var="messageStr"><spring:message code="notification.addBusinessRule"/></c:set>
		<jsp:include page="../../includes/notifications.jsp">
			<jsp:param name="message" value="${messageStr}"/>
		</jsp:include>
	</div>	
	</div>
	<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
</form>
</body>
</html>