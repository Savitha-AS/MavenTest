package com.mip.framework.utils;

import static com.mip.framework.constants.PlatformConstants.TICKET_NUMBER_FORMAT;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtil {

	/*
	 * This represents the incident number count.
	 */
	private static long incidentNumberCount = 1;

	/**
	 * This method is used to get the incident number.
	 * 
	 * @return String containing the incident number.
	 */

	public static synchronized String getIncidentNumber() {
		return (new SimpleDateFormat(TICKET_NUMBER_FORMAT).format(new Date())
				+ "-" + (incidentNumberCount++));
	}

}
