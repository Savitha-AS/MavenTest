package com.mip.framework.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.joda.time.DateTime;

import com.mip.application.view.ReportDailyNewVO;


public class DateUtil {

    private static final String DEF_DATE_FORMAT = "dd/MM/yyyy";
    
    private static final String DEF_DATE_TIME_FORMAT = "dd/MM/yyyy hh:mm:ss";
    
    private static final String DEF_DATE_TIME_FORMAT_MERIDIAN = "dd/MM/yyyy hh:mm a";
    
    private static final String DATE_DAY_FORMAT = "dd/MM-EEE";

	/**
	 * Considers default format as dd/MM/yyyy
	 * @param strDate
	 * @return Date
	 * 
	 */
	public static Date toDate(String strDate) {
	    
	    if(StringUtil.isEmpty(strDate)) return null;
	    
	    DateFormat formatter = new SimpleDateFormat(DEF_DATE_FORMAT);
        try {
            return formatter.parse(strDate);
        } catch (ParseException e) {
            return null;
        }
	}
	
	/**
	 * Considers default format as dd/MM/yyyy
	 * @param strDate
	 * @return Date
	 * 
	 */
	public static Date toDateTime(String strDate) {
	    
	    if(StringUtil.isEmpty(strDate)) return null;
	    
	    DateFormat formatter = new SimpleDateFormat(DEF_DATE_TIME_FORMAT);
        try {
            return formatter.parse(strDate);
        } catch (ParseException e) {
            return toDate(strDate);
        }
	}
	
	/**
	 * @param strDate
	 * @param format
	 * @return Date
	 */
	public static Date toDate(String strDate, String format) {
	    
	    if(StringUtil.isEmpty(strDate) || StringUtil.isEmpty(format)) return null;
	    
	    DateFormat formatter = new SimpleDateFormat(format);
        try {
            return formatter.parse(strDate);
        } catch (ParseException e) {
            return null;
        }
	}
	
	/**
	 * @param strDate
	 * @param format
	 * @return Date
	 */
	public static String toDateString(Date date, String format) {
	    
	    if(date ==null || StringUtil.isEmpty(format)) return null;
	    
	    DateFormat formatter = new SimpleDateFormat(format);
       
        return formatter.format(date);
       
	}
	
	/**
	 * @param strDate
	 * @param format
	 * @return Date
	 */
	public static String toDateString(Date date) {
	    
	    if(date ==null ) return null;
	    
	    DateFormat formatter = new SimpleDateFormat(DEF_DATE_FORMAT);
        return formatter.format(date);
	}
	
	public static String toDateTimeString(Date date) {
	    
	    if(date ==null ) return null;
	    
	    DateFormat formatter = new SimpleDateFormat(DEF_DATE_TIME_FORMAT);
        return formatter.format(date);
	}
	
	public static Date toDateTimeSQL(String date) {
	    
	    if(date == null ) return null;
	    
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date newDate = null;
		try {
			newDate = formatter.parse(date);
		} catch (ParseException e) {
            return null;
        }
        return newDate;
	}
	
	public static String toDateTimeMeridianString(Date date) {
	    
	    if(date ==null ) return null;
	    
	    DateFormat formatter = new SimpleDateFormat(DEF_DATE_TIME_FORMAT_MERIDIAN);
        return formatter.format(date);
	}
	

	/**
	 * @param date 
	 * @return String
	 */
	public static String toStringMonth(String date) {
		
		if (StringUtil.isEmpty(date)) return null;
		
		try {
			String new_date = null;
			String mon = date.substring(3,5);
			if(mon.equals("01")) {
				new_date = date.replaceAll(mon,"Jan");
				if(new_date.startsWith("Jan")) {
					new_date = new_date.replaceFirst("Jan","01");
				}
			}
			if(mon.equals("02")) {
				new_date = date.replaceAll(mon,"Feb");
				if(new_date.startsWith("Feb")) {
					new_date = new_date.replaceFirst("Feb","02");
				}
			}
			if(mon.equals("03")) {
				new_date = date.replaceAll(mon,"Mar");
				if(new_date.startsWith("Mar")) {
					new_date = new_date.replaceFirst("Mar","03");
				}
			}
			if(mon.equals("04")) {
				new_date = date.replaceAll(mon,"Apr");
				if(new_date.startsWith("Apr")) {
					new_date = new_date.replaceFirst("Apr","04");
				}
			}
			if(mon.equals("05")) {
				new_date = date.replaceAll(mon,"May");
				if(new_date.startsWith("May")) {
					new_date = new_date.replaceFirst("May","05");
				}
			}
			if(mon.equals("06")) {
				new_date = date.replaceAll(mon,"June");
				if(new_date.startsWith("June")) {
					new_date = new_date.replaceFirst("June","06");
				}
			}
			if(mon.equals("07")) {
				new_date = date.replaceAll(mon,"July");
				if(new_date.startsWith("July")) {
					new_date = new_date.replaceFirst("July","07");
				}
			}
			if(mon.equals("08")) {
				new_date = date.replaceAll(mon,"Aug");
				if(new_date.startsWith("Aug")) {
					new_date = new_date.replaceFirst("Aug","08");
				}
			}
			if(mon.equals("09")) {
				new_date = date.replaceAll(mon,"Sept");
				if(new_date.startsWith("Sept")) {
					new_date = new_date.replaceFirst("Sept","09");
				}
			}
			if(mon.equals("10")) {
				new_date = date.replaceAll(mon,"Oct");
				if(new_date.startsWith("Oct")) {
					new_date = new_date.replaceFirst("Oct","10");
				}
			}
			if(mon.equals("11")) {
				new_date = date.replaceAll(mon,"Nov");
				if(new_date.startsWith("Nov")) {
					new_date = new_date.replaceFirst("Nov","11");
				}
			}
			if(mon.equals("12")) {
				new_date = date.replaceAll(mon,"Dec");
				if(new_date.startsWith("Dec")) {
					new_date = new_date.replaceFirst("Dec","12");
				}
			}
			return new_date;
		} catch (RuntimeException e) {
			return null;
		}
	}
	
	public static java.sql.Date toSQLDate(String dateStr){
		java.util.Date utilDate = toDate(dateStr);
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

		return sqlDate;
	}
	
	/**
	 * This method returns the YEAR for the provided Date
	 * @param date, an instance of Date
	 * @return YEAR
	 */
	public static int getYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}
	
	/**
	 * This method returns the MONTH for the provided Date
	 * @param date, an instance of Date
	 * @return MONTH
	 */
	public static int getMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH);
	}
	
	/**
	 * This method returns the DAY for the provided Date
	 * @param date, an instance of Date
	 * @return DAY
	 */
	public static int getDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DATE);
	}
	
	/**
	 * This method returns the date of first day of the current week of year
	 * 
	 * @param date, an instance of Date
	 * @return Date
	 */
	public static Date getFirstDayOfTheWeek(Date date) {
		/*int weekOfYear = getWeekOfYear(date);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.WEEK_OF_YEAR, weekOfYear);
		cal.set(Calendar.DAY_OF_WEEK,
		   cal.getActualMinimum(Calendar.DAY_OF_WEEK));		
		return cal.getTime();*/
		Calendar cal = Calendar.getInstance();		
		cal.setTime(date);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return cal.getTime();
	}
	
	/**
	 * This method returns the date of last day of the current week of year
	 * 
	 * @param date, an instance of Date
	 * @return Date
	 */
	public static Date getLastDayOfTheWeek(Date date) {
		/*int weekOfYear = getWeekOfYear(date);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.WEEK_OF_YEAR, weekOfYear);
		cal.set(Calendar.DAY_OF_WEEK,
		   cal.getActualMaximum(Calendar.DAY_OF_WEEK));
		return cal.getTime();*/
		Calendar cal = Calendar.getInstance();		
		cal.setTime(date);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.add(Calendar.DATE, 6);
		return cal.getTime();
	}
	
	/**
	 * This method returns the date of first day of the previous week of year
	 * 
	 * @param date, an instance of Date
	 * @return Date
	 */
	public static Date getFirstDayOfPrevWeek(Date date) {
		Calendar cal = Calendar.getInstance();		
		cal.setTime(date);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.add(Calendar.DATE, -7);
		return cal.getTime();
	}
	
	/**
	 * This method returns the difference between date in minutes
	 * 
	 * @param date, an instance of Date
	 */
	public static long getDateDiff(Calendar todayDate, Calendar createdDate) {
		long millis1 = todayDate.getTimeInMillis();
		long millis2 = createdDate.getTimeInMillis();
		
		long diff=millis1-millis2;
		long diffInMinutes = diff / (60 * 1000);		
		return diffInMinutes;
	}
	/**
	 * This method returns the week of year for the provided date
	 * 
	 * @param date, an instance of Date
	 * @return Week of Year
	 */
	public static int getWeekOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);		
	}
	
	/**
	 * This method returns the date of first day of the provided date
	 * 
	 * @param date, an instance of Date
	 * @return Date
	 */
	public static Date getFirstDayOfTheMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}
	
	/**
	 * This method returns the date of last day of the provided date
	 * 
	 * @param date, an instance of Date
	 * @return Date
	 */
	public static Date getLastDayOfTheMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}
	
	/**
	 * This method returns the no of days between two dates.
	 * 
	 * @param start Start Date
	 * @param end End Date
	 * @return No of days between Start Date and End Date
	 */
	public static long getNoOfDays(String start, String end) {
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(toDate(start));
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(toDate(end));
		long noOfDays = (calEnd.getTimeInMillis() - calStart.getTimeInMillis())
				/ (24 * 60 * 60 * 1000);
		return noOfDays;
	}

	/**
	 * This method checks for the weekends.
	 * @param date
	 * @return true if weekend, false otherwise.
	 */
	public static boolean checkForWeekEnd(Date date) {
		boolean isWeekEnd = false;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
				|| cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			isWeekEnd = true;
		return isWeekEnd;
	}
	
	/**
	 * This method returns the week of year as specified in ISO-8601 standard
	 * for dates and times.
	 * 
	 * @param date Instance of <code>Date</code>
	 * @return Week of Year
	 */
	public static int getISOWeekNumber(Date date) {
		DateTime dt = new DateTime(date);
		return dt.getWeekOfWeekyear();
	}
	
	/**
	 * This method returns the date of first day of week for the given date.
	 * Based on ISO-8601 standard for dates and times.
	 * 
	 * @param date Instance of <code>Date</code>
	 * @return Date
	 */
	public static Date getISOFirstDayOfWeek(Date date) {
		DateTime dt = new DateTime(date).dayOfWeek().withMinimumValue();
		return (Date)dt.toDate();
	}
	
	/**
	 * This method returns the date of last day of week for the given date.
	 * Based on ISO-8601 standard for dates and times.
	 * 
	 * @param date Instance of <code>Date</code>
	 * @return Date
	 */
	public static Date getISOLastDayOfWeek(Date date) {
		DateTime dt = new DateTime(date).dayOfWeek().withMaximumValue();
		return (Date)dt.toDate();
	}
	
	/**
	 * This method returns the date of first day of previous week for the given date.
	 * Based on ISO-8601 standard for dates and times.
	 * 
	 * @param date Instance of <code>Date</code>
	 * @return Date
	 */
	public static Date getISOFirstDayOfPrevWeek(Date date) {
		DateTime dt = new DateTime(date);
		dt = dt.minusWeeks(1);
		dt = dt.dayOfWeek().withMinimumValue();
		return (Date)dt.toDate();
	}
	
	/**
	 * This method returns the date of first day of the previous week of year
	 * 
	 * @param date, an instance of Date
	 * @return Date
	 */
	public static Date getLastDayOfPrevWeek(Date date) {
		Calendar cal = Calendar.getInstance();		
		cal.setTime(date);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.add(Calendar.DATE, 6);
		return cal.getTime();
	}
	
	/**
	 * This method returns the date of first day of previous month of the provided date
	 * 
	 * @param date, an instance of Date
	 * @return Date
	 */
	public static Date getFirstDayOfThePreviousMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DATE, 1);
		
		return cal.getTime();
	}
	
	/**
	 * This method returns the date of 17th day of month of the provided date
	 * 
	 * @param date, an instance of Date
	 * @return Date
	 */
	public static List<String> getDateRangeForDeductedReport(int quickSelectionType) {
		
		String fromDate="" , toDate="";
		List<String> dateRange = new ArrayList<String>();
		
		DateFormat dateFormat = new SimpleDateFormat(DEF_DATE_FORMAT);
		
		Calendar calFrom = Calendar.getInstance();
		calFrom.setTime(new Date());
		
		Calendar calTo = Calendar.getInstance();
		calTo.setTime(new Date());
		
		int currentDay = calFrom.get(Calendar.DAY_OF_MONTH);

		if (quickSelectionType== 0 ) {		// current month
			
			 if ( currentDay >= 16 ) {
				 
				 calFrom.set(Calendar.DATE, 16);
				 fromDate  = dateFormat.format(calFrom.getTime());
				 
				 calTo.add(Calendar.MONTH,1);
				 calTo.set(Calendar.DATE, 15);
				 toDate = dateFormat.format(calTo.getTime());
				
			 } else { 
				 
				 calFrom.add(Calendar.MONTH,-1);
				 calFrom.set(Calendar.DATE, 16);
				 fromDate  = dateFormat.format(calFrom.getTime());
				 
				 calTo.set(Calendar.DATE, 15);
				 toDate = dateFormat.format(calTo.getTime());
				 
			 }
			
		} else { // prev month
			
			if ( currentDay >= 16 ) {
				 
				 calFrom.add(Calendar.MONTH,-1);
				 calFrom.set(Calendar.DATE, 16);
				 fromDate  = dateFormat.format(calFrom.getTime());
				 
				 calTo.set(Calendar.DATE, 15);
				 toDate = dateFormat.format(calTo.getTime());
				
			 } else { 
				 
				 calFrom.add(Calendar.MONTH,-2);
				 calFrom.set(Calendar.DATE, 16);
				 fromDate  = dateFormat.format(calFrom.getTime());
				 
				 calTo.add(Calendar.MONTH,-1);
				 calTo.set(Calendar.DATE, 15);
				 toDate = dateFormat.format(calTo.getTime());
				 
			 }
			
			
		}
		
		 dateRange.add(fromDate);
		 dateRange.add(toDate);	
		 
		 return dateRange;
	}
	
	/**
	 * This method returns the date of 16th day of month of the provided date
	 * 
	 * @param date, an instance of Date
	 * @return Date
	 */
	public static Date getSixteenthDayOfTheMonth(Date date,int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		if (month==1 ) {			
			cal.add(Calendar.MONTH, 1);		
		}
		
		cal.set(Calendar.DATE, 16);
		
		return cal.getTime();
	}
	
	/**
	 * This method returns the date of last day of the previous month of 
	 * provided date
	 * 
	 * @param date, an instance of Date
	 * @return Date
	 */
	public static Date getLastDayOfThePreviousMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}
	
	/**
	 * This method returns the string date of day of the current date
	 * 
	 * @return String an instance of String representing string form of date.
	 */
	public static String getCurrentDateString(){
		String year = "";
		String month = "";
		String day = "";
		
		Date date = new Date();
		
		year = year + DateUtil.getYear(date);
		
		if(DateUtil.getMonth(date) + 1 < 10)
			month = month + "0" + (DateUtil.getMonth(date)+1);
		else
			month = month + (DateUtil.getMonth(date)+1);
		if(DateUtil.getDay(date) < 10)
			day = day + "0" + DateUtil.getDay(date);
		else
			day = day + DateUtil.getDay(date);
		
		String currentDate = year+month+day;
		
		return currentDate;
	}

	/**
	 * This method will return the custom current week.
	 * Since for CSC weekly typical week start from Thursday.
	 * @return
	 */
	public static List<String> getCurrentWeekRangeForCSCWeekly() {
		
		String fromDate="" , toDate="";
		List<String> dateRange = new ArrayList<String>();
		
		DateFormat dateFormat = new SimpleDateFormat(DEF_DATE_FORMAT);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		 while(cal.get(Calendar.DAY_OF_WEEK) != Calendar.THURSDAY)
			{
				cal.add(Calendar.DATE, -1);
			}
			
		 fromDate = dateFormat.format(cal.getTime());
		 cal.add(Calendar.DATE, 6);
		 toDate = dateFormat.format(cal.getTime());
		 
		 dateRange.add(fromDate);
		 dateRange.add(toDate);
			
			
		return dateRange;
	}

	/**
	 * This method will return the custom previous week.
	 * Since for CSC weekly typical week start from Thursday.
	 * @return
	 */
	public static List<String> getPreviousWeekRangeForCSCWeekly() {
		String fromDate="" , toDate="";
		List<String> dateRange = new ArrayList<String>();
		
		DateFormat dateFormat = new SimpleDateFormat(DEF_DATE_FORMAT);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		int dayDiff = Calendar.DAY_OF_WEEK - Calendar.THURSDAY;
		
		 if(dayDiff >= 0)
		 {
			 cal.add(Calendar.WEEK_OF_YEAR, -1);
			 while(cal.get(Calendar.DAY_OF_WEEK) != Calendar.THURSDAY)
				{
					cal.add(Calendar.DATE, -1);
				}
				
				 fromDate = dateFormat.format(cal.getTime());
				 cal.add(Calendar.DATE, 6);
				 toDate = dateFormat.format(cal.getTime());
		 }
		 else 
		 {
			 cal.add(Calendar.WEEK_OF_YEAR, -2);
			 while(cal.get(Calendar.DAY_OF_WEEK) != Calendar.THURSDAY)
				{
					cal.add(Calendar.DATE, -1);
				}
				
				 fromDate = dateFormat.format(cal.getTime());
				 cal.add(Calendar.DATE, 6);
				 toDate = dateFormat.format(cal.getTime());
		 }
		 
		
		 dateRange.add(fromDate);
		 dateRange.add(toDate);
			
		return dateRange;
	}

	/**
	 * This method will return the custom current month.
	 * Since for CSC weekly typical month start from 16th of the month 
	 * & ends on 15th of the next month.
	 * @return
	 */
	public static List<String> getCurrentMonthRangeForCSCWeekly() {
		
		String fromDate="" , toDate="";
		List<String> dateRange = new ArrayList<String>();
		
		DateFormat dateFormat = new SimpleDateFormat(DEF_DATE_FORMAT);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		int currentDay = cal.get(Calendar.DAY_OF_MONTH);
		
		 if(currentDay >= 16)
		 {
			 cal.set(Calendar.DATE, 16);
			 fromDate = dateFormat.format(cal.getTime());
			 cal.add(Calendar.MONTH,1);
			 cal.set(Calendar.DATE, 15);
			 toDate = dateFormat.format(cal.getTime());
		 }
		 else 
		 {
			 
			 cal.set(Calendar.DATE, 15);
			 toDate = dateFormat.format(cal.getTime());
			 cal.add(Calendar.MONTH,-1);
			 cal.set(Calendar.DATE, 16);
			 fromDate = dateFormat.format(cal.getTime());
			 
		 }
		
		 dateRange.add(fromDate);
		 dateRange.add(toDate);
			
			
		return dateRange;
	}

	/**
	 * This method will return the custom previous month.
	 * Since for CSC weekly typical month start from 16th of the month 
	 * & ends on 15th of the next month.
	 * @return
	 */
	public static List<String> getPreviousMonthRangeForCSCWeekly() {

		String fromDate="" , toDate="";
		List<String> dateRange = new ArrayList<String>();
		
		DateFormat dateFormat = new SimpleDateFormat(DEF_DATE_FORMAT);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		int currentDay = cal.get(Calendar.DAY_OF_MONTH);
		

		 if(currentDay >= 16)
		 {
			 cal.set(Calendar.DATE, 15);
			 toDate = dateFormat.format(cal.getTime());
			 cal.add(Calendar.MONTH,-1);
			 cal.set(Calendar.DATE, 16);
			 fromDate = dateFormat.format(cal.getTime());
			
		 }
		 else 
		 { 
			 cal.add(Calendar.MONTH,-1);
			 cal.set(Calendar.DATE, 15);
			 toDate = dateFormat.format(cal.getTime());
			 cal.add(Calendar.MONTH,-1);
			 cal.set(Calendar.DATE, 16);
			 fromDate = dateFormat.format(cal.getTime());
			 
		 }
		
		 dateRange.add(fromDate);
		 dateRange.add(toDate);
		
		return dateRange;
	}
	/**
	 * This method will format the date by appending the day.
	 * @param date
	 * @return
	 */
	public static String toDateDayString(Date date) {
	    
	    if(date ==null ) return null;
	    
	    DateFormat formatter = new SimpleDateFormat(DATE_DAY_FORMAT);
        return formatter.format(date);
	}

	/**
	 * This method will return the date range based on the fromDate and todate.
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static List<String> getBeginEndDateRange(String beginDate, String endDate) {
		
		List<String> dateRange = new ArrayList<String>();
		DateFormat formatter = new SimpleDateFormat(DATE_DAY_FORMAT);
		Date now = null;
		
		Calendar start = Calendar.getInstance();
		start.setTime(DateUtil.toDate(beginDate));
		Calendar end = Calendar.getInstance();
		end.setTime(DateUtil.toDate(endDate));
		

		for (; !start.after(end); start.add(Calendar.DATE, 1)) {
			now = start.getTime();
			dateRange.add(formatter.format(now));
		}
	
		return dateRange;
	}

	
	/**
	 * This method returns the date of 16th day of month of the provided date
	 * 
	 * @param date, an instance of Date
	 * @return Date
	 */
public static List<String> getDateRangeForFirstMonthDeductedReport(int quickSelectOption) {
		
		String fromDate="" , toDate="";
		List<String> dateRange = new ArrayList<String>();
		
		DateFormat dateFormat = new SimpleDateFormat(DEF_DATE_FORMAT);
		
		Calendar calFrom = Calendar.getInstance();
		calFrom.setTime(new Date());
		
		Calendar calTo = Calendar.getInstance();
		calTo.setTime(new Date());
		int currentDay = calFrom.get(Calendar.DAY_OF_MONTH);

		if (quickSelectOption == 0 ) {		// current month
			
			 if ( currentDay >= 16 ) {
				 
				 calFrom.set(Calendar.DATE, 16);
				 fromDate  = dateFormat.format(calFrom.getTime());
				 
				 calTo.add(Calendar.MONTH,1);
				 calTo.set(Calendar.DATE, 15);
				 toDate = dateFormat.format(calTo.getTime());
				
			 } else { 
				 
				 calFrom.add(Calendar.MONTH,-1);
				 calFrom.set(Calendar.DATE, 16);
				 fromDate  = dateFormat.format(calFrom.getTime());
				 
				 calTo.set(Calendar.DATE, 15);
				 toDate = dateFormat.format(calTo.getTime());
				 
			 }
		/*} else { // previous month
			
			if ( currentDay >= 16 ) {
				 
				 calFrom.add(Calendar.MONTH,-1);
				 calFrom.set(Calendar.DATE, 16);
				 fromDate  = dateFormat.format(calFrom.getTime());
				 
				 calTo.set(Calendar.DATE, 15);
				 toDate = dateFormat.format(calTo.getTime());
				
			 } else { 
				 
				 calFrom.add(Calendar.MONTH,-2);
				 calFrom.set(Calendar.DATE, 16);
				 fromDate  = dateFormat.format(calFrom.getTime());
				 
				 calTo.add(Calendar.MONTH,-1);
				 calTo.set(Calendar.DATE, 15);
				 toDate = dateFormat.format(calTo.getTime());
				 
			 }*/
			
		}
		
		 dateRange.add(fromDate);
		 dateRange.add(toDate);	
		 
		 return dateRange;
	}

	
	/**
	 * This method returns the date of 16th day of month of the provided date
	 * (selected by the user from dropdown)
	 * @param date, an instance of Date
	 * @return Date
	 *//*
	public static List<String> getDropDownDateRangeForFirstMonthDeductedReport(
			int monthSel) {
		String fromDate="" , toDate="";
		List<String> dateRange = new ArrayList<String>();
		
		
		
		dateRange = getDateRange(monthSel);
		 return dateRange;
	}*/

	/**
	 * This method returns the date of 16th day of month of the provided date
	 * (based on the value of dropdown)
	 * @param date, an instance of Date
	 * @return Date
	 */
	public static List<String> getDateRange(int monthSel) {
		DateFormat dateFormat = new SimpleDateFormat(DEF_DATE_FORMAT);
		String fromDate="" , toDate="";
		List<String> dateRange = new ArrayList<String>();
		
		Calendar calFrom = Calendar.getInstance();
		calFrom.setTime(new Date());
		Calendar calTo = Calendar.getInstance();
		calTo.setTime(new Date());

		int currentDay = calFrom.get(Calendar.DAY_OF_MONTH);
		
		 if ( currentDay >= 16 ) {
			 calFrom.set(Calendar.DATE, 16);
			 calFrom.add(Calendar.MONTH, 0-monthSel);
			
			 calTo.add(Calendar.MONTH,1-monthSel);
			 calTo.set(Calendar.DATE, 15);
			 
		 } else { 
			 calFrom.add(Calendar.MONTH,0-(monthSel+1));
			 calFrom.set(Calendar.DATE, 16);
			 
			 calTo.set(Calendar.DATE, 15);
			 calTo.add(Calendar.MONTH, 0-monthSel);
			
		 }
		 fromDate  = dateFormat.format(calFrom.getTime());
		 toDate = dateFormat.format(calTo.getTime());
		 
		 dateRange.add(fromDate);
		 dateRange.add(toDate);
		 
		return dateRange;
	}

	public static List<String> getDateRangeForCalenderMonth(int quickOptionSelect) {
		DateFormat dateFormat = new SimpleDateFormat(DEF_DATE_FORMAT);
		String fromDate="" , toDate="";
		
		List<String> dateRange = new ArrayList<String>();
		
		Calendar calFrom = Calendar.getInstance();
		calFrom.setTime(new Date());
		
		Calendar calTo = Calendar.getInstance();
		calTo.setTime(new Date());
		
		
		if(quickOptionSelect == 1){
			calFrom.add(Calendar.MONTH,-1);
			calTo.add(Calendar.MONTH,-1);
		}
		
		calFrom.set(Calendar.DAY_OF_MONTH, calFrom.getActualMinimum(Calendar.DAY_OF_MONTH));
		calTo.set(Calendar.DAY_OF_MONTH, calTo.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		
		fromDate  = dateFormat.format(calFrom.getTime());
		toDate = dateFormat.format(calTo.getTime());
		dateRange.add(fromDate);
		dateRange.add(toDate);
		
		return dateRange;
	}
	
	public static List<String> getDateRangeForQuarterReport(int quickSelectOption){
		DateFormat dateFormat = new SimpleDateFormat(DEF_DATE_FORMAT);
		String fromDate="" , toDate="";
		List<String> dateRange = new ArrayList<String>();
		
		Calendar calFrom = Calendar.getInstance();
		calFrom.setTime(new Date());
		Calendar calTo = Calendar.getInstance();
		calTo.setTime(new Date());
		
		if(quickSelectOption == 0){
			calFrom.add(Calendar.MONTH,-2);
			calTo.add(Calendar.MONTH,-1);
		}else{
			calFrom.add(Calendar.MONTH,-3);
			calTo.add(Calendar.MONTH,-2);
		}
		
		calFrom.set(Calendar.DAY_OF_MONTH, calFrom.getActualMinimum(Calendar.DAY_OF_MONTH));
		calTo.set(Calendar.DAY_OF_MONTH, calTo.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		
		fromDate  = dateFormat.format(calFrom.getTime());
		toDate = dateFormat.format(calTo.getTime());
		
		dateRange.add(fromDate);
		dateRange.add(toDate);
		
		return dateRange;
	}
	
}
