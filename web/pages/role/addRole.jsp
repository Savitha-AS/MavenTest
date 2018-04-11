<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="/WEB-INF/frameWorkFunctions.tld" prefix="frameWork" %>
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
	<script src="${pageContext.request.contextPath}/appScripts/role.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/appScripts/utils.js" type="text/javascript"></script>
	
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/engine.js"></script>
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/util.js"></script>
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/interface/validateAddRole.js"></script>
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/interface/displayChildMenus.js"></script>
	<script>
	
	var parentId = 0;
	
	/*	$(function() {
			$( "button, input:button, input:submit").button();
		}); */
		
		
		$(function() {
			
			$("#saveBtn").bind("click",(function() {
				if(!validateAddRole()){
					checkForExistingRole("");
				}
			}));

			$("body").keypress(function(e) {
				 if (e.keyCode == '13') {
				 	$("#saveBtn").click(); 
					return false;
				 }
		 	});

			$("#clearBtn").click(function() {
				clearField("roleName");
				//clearField("roleDescription");
				$('input:checkbox').removeAttr('checked');
				checkDefault();
			});
		});	
		
		/**
		 * Call-back method for <code>checkForExistingMSISDN</code> DWR method.
		 * 
		 * @param msisdnExists boolean result of the DWR method invoked.
		 * 
		 * @see checkForExistingRole()
		 */
		function loadRoleCheck(roleExists){
			if(roleExists){
				setError("roleName", 20, getFieldText('roleName'));
			}
			if(showValidationErrors("validationMessages_parent"))
				return true;
			else{
				confirmSave("", function(result) {
					if(result){
						
						var parentList = "";
						$('input[name=parentMenuCheckBox]').each(function () {
							if(this.checked){
								parentList += this.id + ",";
							}
						});
						
						var childList = "";
						$('input[name=childMenuCheckBox]').each(function () {
							if(this.checked){
								childList += this.id + ",";
							}
						});
						
						document.getElementById("parentMenuString").value = parentList;
						document.getElementById("childMenuString").value = childList;
						//document.getElementById("parentMenuString").value = inputsWithParentMenuCheckBoxString;
						//document.getElementById("childMenuString").value = inputsWithChildMenuCheckBoxString;
						document.getElementById("addRoleFrm").action = "${pageContext.request.contextPath}/role.controller.addRole.task";
						document.getElementById("addRoleFrm").submit();
							}

						});
			}
		}
		 
		 function checkParent(childMenuId, parentMenuId) {
			 
			 parentId = parentMenuId;
			 
			 	if(document.getElementById(childMenuId).checked){   
			 		$("#"+parentMenuId+"").attr('checked', 'checked');
				}
			 	else if(!document.getElementById(childMenuId).checked){   
			 		$("#"+parentMenuId+"").attr('checked', false);
				}
			 	
			 	checkIfAllChildUnchecked(parentMenuId);
		}
		 
		function checkOrUncheckChild(parentMenuId){
			 if(document.getElementById(parentMenuId).checked){
				 displayChildMenus.getChildMenusForParentMenuId(parentMenuId, checkAllChild);
			 }
			 else if(!document.getElementById(parentMenuId).checked){   
				 displayChildMenus.getChildMenusForParentMenuId(parentMenuId, uncheckAllChild);
				}
		}
		
		function checkAllChild(childRoleVO) {
			checkChilds(childRoleVO);
		}
		
		function uncheckAllChild(childRoleVO) {
			uncheckChilds(childRoleVO);
		}
		
		function checkIfAllChildUnchecked(parentMenuId){
			displayChildMenus.getChildMenusForParentMenuId(parentMenuId, checkForChildWithSameParent);
		}
		
		function checkForChildWithSameParent(childRoleVO){
			checkOrUncheckParent(parentId, childRoleVO);
		}
		
		function checkDefault() {
			$("#"+1+"").attr('checked', 'checked');
			$("#"+10+"").attr('checked', 'checked');
			$("#"+11+"").attr('checked', 'checked');
			document.getElementById(1).disabled = true;
			document.getElementById(10).disabled = true;
			document.getElementById(11).disabled = true;
		}
		
	</script>
</head>

<body onload="keepSubMenuSelected(); checkDefault();">
	<form method="post" id="addRoleFrm">
	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>
	
	<!-- View Content -->
	<div class="content-container">
	<div id="box" style="width: 100%;">	
		<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
		<input type="hidden" id="pageId" value="46" />	
		<div class="pagetitle">
			<h3><spring:message code="addrole.header.text"/>
			</h3>			
		</div>	
		<div style="text-align: right; margin-right: 5px;"><spring:message code="platform.text.mandatory"/></div>
		<br/>
		<jsp:include page="../../includes/global_validations.jsp"></jsp:include>
		
		<div class="data_container_color1">
			<div class="data_label" id="label_roleName"><spring:message code="platform.text.roleName"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<input type="text" id="roleName" name="roleName" style="width:150px" maxlength="100"/>
			</div>
		</div>
		
		<div class="data_container_color2" style="display: none">
			<div class="data_label" id="label_roleDescription"><spring:message code="platform.text.roleDescription"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<input type="text" id="roleDescription" name="roleDescription" style="width:150px" maxlength="100"/>
			</div>
		</div>
		
		<div class="data_container_color2" style="height: 650px">
		
			<div class="data_label" id="label_roleMenu"><spring:message code="platform.text.menu"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<br />
		
			<c:set var="menuMapAdmin" value="${sessionScope['menuTreeAdmin']}"/>
			<c:choose>
			<c:when test="${frameWork:isNotNull(menuMapAdmin)}">
	
				<div id="menuDiv" style="float:left">
					<c:forEach var="parentMenu" items="${menuMapAdmin[0]}">
			
						<c:set var="parentUrl" value="${parentMenu.menuUrl}"/>
						<c:if test="${parentMenu.menuUrl ne '#'}">
							<c:set var="parentUrl" value="${pageContext.request.contextPath}/${parentUrl}"/>		
						</c:if>
						<br/>
						
						<div style="" class="data_label" id="label_menu">
							<input id="${parentMenu.menuId}" name="parentMenuCheckBox" onchange="checkOrUncheckChild(this.id)" value="${parentUrl}" type="checkbox" />
								<%-- <spring:message code="${parentMenu.menuName}"></spring:message> --%>
								<c:out value="${parentMenu.menuName}"></c:out>
						</div>
											
						<c:if test="${parentMenu.menuUrl eq '#'}">
							<br/>
							<c:forEach var="childMenu" items="${menuMapAdmin[parentMenu.menuId]}">
									<div style="margin-left: 50px;" class="data_label" id="label_subMenu">
										<input id="${childMenu.menuId}" name="childMenuCheckBox" onchange="checkParent(this.id, ${parentMenu.menuId})" value="${pageContext.request.contextPath}/${childMenu.menuUrl}" type="checkbox" />
										<%-- <spring:message code="${childMenu.menuName}"> --%>
										<c:out value="${childMenu.menuName}"></c:out>
										<c:if test="${childMenu.menuId eq 16}">
									            <div id="isIPReg_div" style="display:none; float: right">
													<input type="checkbox" id="isIPReg" name="isIPReg" value="1"/>
													<spring:message code="platform.text.access"></spring:message>
												</div>
											</c:if>	
											<%-- </spring:message> --%>
									</div>
									<br/>
							</c:forEach>
						</c:if>
					</c:forEach>
				</div>
			</c:when>
			<c:otherwise>
				<c:redirect url="${pageContext.request.contextPath}/pages/login.jsp"></c:redirect>
			</c:otherwise>
			</c:choose>
			<br /><br />
		</div>
		
 		<div class="data_container_color2" style="display: none;">
			<div class="data_label" id="label_parentMenuString"><spring:message code="platform.text.parentMenu"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<input type="text" id="parentMenuString" name="parentMenuString" style="width:110px" maxlength="100"/>
			</div>
		</div>
		
		
		<div class="data_container_color1" style="display: none;">
			<div class="data_label" id="label_childMenuString"><spring:message code="platform.text.childMenu"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<input type="text" id="childMenuString" name="childMenuString" style="width:110px" maxlength="100"/>
			</div>
		</div> 
		<br/>
		<br/>
		<div style="text-align: center;">
					<ul class="btn-wrapper">
						<li class="btn-inner" id="saveBtn"><span><spring:message
									code="platform.button.save" /></span></li>
					</ul>
					<ul class="btn-wrapper">
						<li class="btn-inner" id="clearBtn"><span><spring:message
									code="platform.button.clear" /></span></li>
					</ul>
				</div>
	</div>
	</div>
	<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
</form>
<script>
//on Customer Registeration Check

$("input[type=checkbox][id=16]").click(function(){
	if($('input:checkbox[id=16]').is(':checked')){		
		$("#isIPReg_div").show('slow');
	}
	else{
		$("input[type=checkbox][id=isIPReg]").attr( "checked",false);		
		$("#isIPReg_div").hide('slow');
	}
});

$("input[type=checkbox][id=3]").click(function(){
	$("input[type=checkbox][id=16]").attr( "checked",true);
	if($('input:checkbox[id=3]').is(':checked') && $('input:checkbox[id=16]').is(':checked')){
		$("#isIPReg_div").show('slow');		
		$("input[type=checkbox][id=isIPReg]").attr( "checked",true);
		
	}
	else{
		$("input[type=checkbox][id=isIPReg]").val('0');		
		$("input[type=checkbox][id=isIPReg]").attr( "checked",false);
		$("#isIPReg_div").hide('slow');
		
	}

});

</script>
</body>
</html>