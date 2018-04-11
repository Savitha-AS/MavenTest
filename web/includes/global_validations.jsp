<%@taglib uri="http://www.springframework.org/tags"  prefix="spring"%>
<div id="validationMessages_parent" style="display:none;">
	<div class="error-div-header" style="height: 20px; line-height:22px;">
		<span class="spi-gtitest-mid1-cdt1-abstract-text" style="vertical-align:sub; margin-left: 12px;" >
			<strong>
				<font color="red"><spring:message code="platform.text.errors"></spring:message>
				</font>
			</strong>
		</span>
	</div>
	<div class="error-div-body" id="validationMessages"></div>
	<div class="error-div-footer">&nbsp;</div>
	<br/>
</div>