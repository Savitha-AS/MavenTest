/* Session timeout popup */
var g_sessionPopupStatus = 0;
// loading popup with jQuery magic!
function loadSessionPopup() {
	// loads popup only if it is disabled
	if (g_sessionPopupStatus == 0) {
		// The below style has been overridden in  
		// sessionBackgroundPopup, sessionPopup classes in popup.css
		$("#sessionBackgroundPopup").css({
			"opacity" : "0.7"
		});
		$("#sessionBackgroundPopup").fadeIn("fast");
		$("#sessionPopup").fadeIn("fast");
		g_sessionPopupStatus = 1;
	}
}

// disabling popup with jQuery magic!
function disableSessionPopup() {
	// disables popup only if it is enabled
	if (g_sessionPopupStatus == 1) {
		$("#sessionBackgroundPopup").fadeOut("fast");
		$("#sessionPopup").fadeOut("fast");
		g_sessionPopupStatus = 0;
	}
}

// centering popup
function centerSessionPopup() {
	// request data for centering
	var windowWidth = document.documentElement.clientWidth;
	var windowHeight = document.documentElement.clientHeight;
	var popupHeight = $("#sessionPopup").height();
	var popupWidth = $("#sessionPopup").width();
	// centering
	$("#sessionPopup").css({
		"position" : "absolute",
		"top" : windowHeight / 2 - popupHeight / 2,
		"left" : windowWidth / 2 - popupWidth / 2
	});
	// only need force for IE6

	$("#sessionBackgroundPopup").css({
		"height" : windowHeight
	});
}