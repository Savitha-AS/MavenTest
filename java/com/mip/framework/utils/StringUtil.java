package com.mip.framework.utils;

/**
 * Base Util class for Strings
 * 
 * @author kranthi
 *  
 */
public class StringUtil {

	public static boolean isEmpty(String data) {
		return (data == null || data.trim().length() == 0);
	}

	/**
	 * @param str
	 * @return boolean
	 */
	public static boolean getBoolean(String str) {

		return (str == null || "yes".equalsIgnoreCase(str)
				|| "y".equalsIgnoreCase(str) || "true".equalsIgnoreCase(str));
	}

	/**
	 * @param str
	 * @param delimiter
	 * @return String[]
	 */
	public static String[] getArray(String str, String delimiter) {

		if (str != null && delimiter != null) {
			return str.split(delimiter.trim());
		}

		return null;
	}

	/**
	 * @param value
	 * @param def
	 * @return String
	 */
	public static String handleDefault(String value, String def) {
		return isEmpty(value) ? def : value;
	}

	/**
	 * returns true if data is either null length=0 or nothing has been selected
	 * 
	 * @param data
	 * @return true
	 */
	public static boolean isDefault(String data) {
		return (data == null || (data = data.trim()).length() == 0 || "-1"
				.equals(data));
	}
	

	public static String handleNull(Object data) {
		return (data == null) ? "" : String.valueOf(data);
	}

	/**
	 * returns true if data is either null length=0 or nothing has been selected
	 * 
	 * @param data
	 * @return true
	 */
	public static String isDefault(String data, String def) {
		return isDefault(data) ? def : data;
	}

	public static String trim(String data) {

		if (isEmpty(data))
			return null;

		return data.trim();
	}

	public static String remove(String data, int pos) {
		if (isEmpty(data))
			return null;

		String str = new StringBuffer(data.trim())
							.deleteCharAt(pos)
							.toString();
		return str;
	}

	public static boolean isBoolean(String str) {
		return new Boolean(str).booleanValue();
	}

	public static String getCSV(Object[] obj) {

		if (obj == null || obj.length == 0)
			return "";

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < obj.length; i++) {
			sb.append(obj[i] + ",");
		}
		return sb == null ? null : sb.toString().substring(0,
				sb.toString().length() - 1);
	}
}