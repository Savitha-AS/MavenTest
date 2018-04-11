var g_timerId;
var g_timerValue = 20;
function showTimeoutWarning() {
	g_timerId = setTimeout("processSession(false)", g_timerValue * 1000);
	centerSessionPopup();
	loadSessionPopup();
	createTimer();
}

$(function() {
	$("#okBtn").click(function() {
		// Retain the current session.
		clearTimeout(g_timerId);
		disableSessionPopup();
		processSession(true);
	});

	$("#cancelBtn").click(function() {
		// Invalidate the session.
		disableSessionPopup();
		processSession(false);
	});
});

/* DWR Call */
function processSession(val) {
	validateSessionTimeout.processSession(val, loadSessionCheck);
}

/* Callback */
function loadSessionCheck(redirectURL) {
	if (redirectURL == null || redirectURL == "") {
		setTimeout("showTimeoutWarning()", (g_maxInactiveTime - 20) * 1000);
	} else {
		window.location = ctx + redirectURL;
	}
}

/* Timer */
// var timer;
var g_totalSeconds;
function createTimer() {
	// timer = document.getElementById(TimerId);
	g_totalSeconds = g_timerValue;
	updateTimer();
	window.setTimeout("tick()", 1000);
}

function tick() {
	if (g_totalSeconds > 0) {
		g_totalSeconds -= 1;
		updateTimer();
		window.setTimeout("tick()", 1000);
	}
}

function updateTimer() {
	$("#sessionArea")
			.html(
				"Your online session will be timed out in "
					+ g_totalSeconds + " seconds due to inactivity."
					+ " Click Retain to continue the session or End to end the session now.");
}