<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/frameWorkFunctions.tld" prefix="frameWork" %>

<c:set var="menuMap" value="${sessionScope['menuTree']}"/>
<c:choose>
	<c:when test="${frameWork:isNotNull(menuMap)}">
		<div class="menu-wrapper">
		<div id="menuDiv" style="width: 205px; float:left">
			<ul id="menu" class="menu collapsible">					
			
			<c:forEach var="parentMenu" items="${menuMap[0]}">
			
				<c:set var="parentUrl" value="${parentMenu.menuUrl}"/>				
				<c:if test="${parentMenu.menuUrl ne '#'}">
					<c:set var="parentUrl" value="${pageContext.request.contextPath}/${parentUrl}"/>		
				</c:if>
				
				<li id="menu${parentMenu.menuId}">	
					<a href="${parentUrl}">${parentMenu.menuName}</a>
							
					<c:if test="${parentMenu.menuUrl eq '#'}">
						<ul>
							<c:forEach var="childMenu" items="${menuMap[parentMenu.menuId]}">
								<li id="submenu${childMenu.menuId}"><a href="${pageContext.request.contextPath}/${childMenu.menuUrl}">${childMenu.menuName}</a></li>								
							</c:forEach>
						</ul>
					</c:if>
				</li>
				
			</c:forEach>
			
			</ul>
		</div>
		</div>	
	</c:when>
	<c:otherwise>
		<c:redirect url="${pageContext.request.contextPath}/pages/login.jsp"></c:redirect>
	</c:otherwise>
</c:choose>