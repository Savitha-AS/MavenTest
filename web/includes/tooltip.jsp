<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/css/tooltip.css" />

<script>
$(document).ready(function($) {
	var id = '${param.tooltipId}';
	var type =  '${param.tooltipType}';
	var body = "";
	if(type == "csv") {
		var values = '${param.tooltipValue}'.split(",");
		for(var i=0; i<values.length; i++){
			body += '<div><li>'+values[i]+'</li></div>';
		}
	}
	if(type == "message") {
		body = '${param.tooltipValue}';
	}
	
	//Tooltip
	$('a[id='+id+']').mouseover(function(e) {
		//Grab the title attribute's value and assign it to a variable
		var title = $(this).attr('title');
	
		//Remove the title attribute's to avoid the native tooltip from the browser
		$(this).attr('title', '');
	
		//Append the tooltip template and its value
		$(this).append('<div id="tooltip">'
			+ '<div class="tipHeader"></div>'
			+ '<div class="tipBody">'
			+ '<strong>'
			+ title
			+ '</strong>'
			+ body
			+ '</div>'
			+ '<div class="tipFooter">'
			+ '</div></div>');
	
		//Set the X and Y axis of the tooltip
		$('#tooltip').css('top',e.pageY + 10);
		$('#tooltip').css('left',e.pageX + 20);
	
		//Show the tooltip with faceIn effect
		$('#tooltip').fadeIn('500');
		$('#tooltip').fadeTo('10', 1);
	
	})
	.mousemove(function(e) {
	
		//Keep changing the X and Y axis for the tooltip, 
		// thus, the tooltip move along with the mouse
		$('#tooltip').css('top', e.pageY + 10);
		$('#tooltip').css('left', e.pageX + 20);
	
	})
	.mouseout(function() {
		//Put back the title attribute's value
		$(this).attr('title', $('.tipBody strong').html());
	
		//Remove the appended tooltip template
		$(this).children('div#tooltip').remove();
	});
});	
</script>

<a id="${param.tooltipId}" 
	title="${param.tooltipTitle}"><img
	src="${pageContext.request.contextPath}/theme/images/confirm.png"
	style="height: 18px; width: 18px;" />
</a>
