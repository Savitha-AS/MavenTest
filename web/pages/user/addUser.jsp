<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
	<script src="${pageContext.request.contextPath}/appScripts/user.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/appScripts/utils.js" type="text/javascript"></script>
	
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/engine.js"></script>
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/util.js"></script>
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/interface/validateMSISDN.js"></script>
	<script>
	/*	$(function() {
			$( "button, input:button, input:submit").button();
		}); */
		
		$(function() {
			$("#saveBtn").bind("click",(function() {				
				if(!validateAddUser()){
					checkForExistingMSISDN("");
				}
			}));

			$("body").keypress(function(e) {
				 if (e.keyCode == '13') {
				 	$("#saveBtn").click(); 
					return false;
				 }
		 	});

			$("#clearBtn").click(function() {
				clearField("fname");
				clearField("sname");
				clearField("msisdn");
				clearField("dob");
				clearField("age");
				resetDropDownList("role");
				resetDropDownList("branch");
				resetRadioButtons("gender");
			});
		});	
		
		/**
		 * Call-back method for <code>checkForExistingMSISDN</code> DWR method.
		 * 
		 * @param msisdnExists boolean result of the DWR method invoked.
		 * 
		 * @see checkForExistingMSISDN()
		 */
		function loadMSISDNCheck(msisdnExists){
			if(msisdnExists){
				setError("msisdn", 20, "Mobile Number");
			}
			if(showValidationErrors("validationMessages_parent"))
				return true;
			else{
				confirmSave("", function(result) {
					if(result){
						document.getElementById("addUserFrm").action="${pageContext.request.contextPath}/user.controller.addUser.task";
						document.getElementById("addUserFrm").submit();
					}
				});
			}
		}
	</script>
</head>

<body onload="keepSubMenuSelected()">
	<form method="post" id="addUserFrm">
	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>
	<!-- View Content -->
	<div class="content-container">
	<div id="box" style="width: 100%;">	
		<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
		<input type="hidden" id="pageId" value="12" />	
		<div class="pagetitle">
			<h3><spring:message code="adduser.header.text"/>
			</h3>			
		</div>	
		<div style="text-align: right; margin-right: 5px;"><spring:message code="platform.text.mandatory"/></div>
		<br/>
		<jsp:include page="../../includes/global_validations.jsp"></jsp:include>
		
		<div class="data_container_color1">
			<div class="data_label" id="label_fname"><spring:message code="platform.text.firstname"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<input type="text" id="fname" name="fname" style="width:110px" maxlength="100"/>
			</div>
		</div>
		
		<div class="data_container_color2">
			<div class="data_label" id="label_sname"><spring:message code="platform.text.surname"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<div id="div_id_2">
					<input type="text" id="sname" name="sname" style="width:110px" maxlength="50"/>
				</div>
			</div>
		</div>
		
		<div class="data_container_color1">
			<div class="data_label" id="label_msisdn"><spring:message code="platform.text.msisdn"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" >
				<div id="div_id_3" style="float:left;">
					<input type="text" id="msisdn" name="msisdn" maxlength="10" size="10"/>
				</div>
			</div>
		</div>
		
		<div class="data_container_color2">
			<div class="data_label" id="label_email"><spring:message code="platform.text.email"/>&nbsp;:</div>
			<div class="data_text" >
				<div id="div_id_3" style="float:left;">
					<input type="text" id="email" name="email" maxlength="150" size="30"/>
				</div>
			</div>
		</div>
		
		<div class="data_container_color1" id="calendar">
			<div class="data_label" id="label_dob"><spring:message code="platform.text.dob"/>&nbsp;:</div>
			<div class="data_text" style="text-align: left;">
				<input type="text" id="dob" name="dob" readonly="readonly" style="width:110px"/>
				<div class="calendar-icon" onclick="javascript:clearDate('dob','age')" id="calBut1" title="<spring:message code='tooltip.platform.calendar'/>"></div>
				<div class="clear-icon" onclick="javascript: clearDate('dob','age')" title="<spring:message code='tooltip.platform.clear'/>"></div>				
				<script>
				   Calendar.setup({
				        inputField : "dob",
				        trigger    : "calBut1",
				        onSelect   : function() { 
				        				this.hide();
				        				calculateAge('dob','age');
				        			 },
				        showTime   : false,
				        dateFormat : "%d/%m/%Y"	// %Y-%m-%d %I:%M %p
				    });
				</script>								
			</div>
		</div>
		<div class="data_container_color2">
			<div class="data_label" id="label_age"><spring:message code="platform.text.age"/>&nbsp;:</div>
			<div class="data_text">
				<input type="text" id="age" name="age" style="width:20px" maxlength="2"/>
			</div>
		</div>
		<div class="data_container_color1">
			<div class="data_label" id="label_gender"><spring:message code="platform.text.gender"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<input type="radio" id="gender" name="gender" value="Male" /><spring:message code="platform.text.male"/>&nbsp;&nbsp;&nbsp;
				<input type="radio" id="gender" name="gender" value="Female" /><spring:message code="platform.text.female"/>
			</div>
		</div>
		<div class="data_container_color2">
			<div class="data_label" id="label_role"><spring:message code="platform.text.role"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<div id="div_id_15" >
					<select id="role" name="role" style="width:170px;">
						<option value=""></option>
						<c:forEach var="roleMap" items="${roleMap}">  
					    	<option value="${roleMap.key}">${roleMap.value}</option>
					 	</c:forEach>					
					</select>
				</div>
			</div>
		</div>
		<div class="data_container_color1">
			<div class="data_label" id="label_branch"><spring:message code="platform.text.branch"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<div id="div_id_6" >
					<select id="branch" name="branch" style="width:170px;">
						<option value=""></option>
						<c:forEach var="branchDetails" items="${branchList}">  
					    	<option value="${branchDetails.branchId}">${branchDetails.branchName}</option>
					 	</c:forEach>					
					</select>
				</div>
			</div>
		</div>
		<br/>
		<div style="text-align:center;">
		  	<ul class="btn-wrapper"><li class="btn-inner" id="saveBtn"><span><spring:message code="platform.button.save"/></span></li></ul>
			<ul class="btn-wrapper"><li class="btn-inner" id="clearBtn"><span><spring:message code="platform.button.clear"/></span></li></ul>
		</div>		
	</div>
	</div>
	<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
</form>
</body>
</html>