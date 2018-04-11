package com.mip.framework.utils;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


public class TypeUtil {
	
	public static boolean isNull(Object data){
		return (data == null);
	}
	
	public static boolean isNotNull(Object data){
		return (data != null && !data.toString().equalsIgnoreCase("null"));
	}
    
	public static boolean toBoolean(String data) {

		if (StringUtil.isEmpty(data))
			return false;

		if (data.equalsIgnoreCase("true"))
			return true;

		if (data.equalsIgnoreCase("yes"))
			return true;

		if (data.equalsIgnoreCase("enabled"))
			return true;

		if (data.equalsIgnoreCase("Y"))
			return true;

		try {
			int value= Integer.parseInt(data);
			return (value > 0);
		} catch (NumberFormatException ex) { }

		return false;
	}

	public static int toInt( String data ) {
		return toInt( data, -1 );
	}

	public static int toInt( String data, int onerror ) {
		if ( StringUtil.isEmpty( data ) )
			return onerror;

		int ret = onerror;

		try {
			ret= Integer.parseInt(data.trim());
		} catch (NumberFormatException ex) { }

		return ret;
	}

	public static long toLong( String data ) {
		return toLong( data, -1 );
	}
	
	public static long toLong( String data, long onerror ) {
		if ( StringUtil.isEmpty( data ) )
			return onerror;

		long ret = onerror;

		try {
			ret= Long.parseLong(data);
		} catch (NumberFormatException ex) { }

		return ret;
	}

	public static float toFloat( String data ) {
		return toFloat( data, -1 );
	}

	public static float toFloat( String data, float def ) {
		if ( StringUtil.isEmpty( data ) )
			return def;

		float ret = def;
		try {
			ret = Float.parseFloat(data);
		} catch (NumberFormatException ex) { }
		return ret;
	}

	public static double toDouble( String data ) {
		return toDouble( data, -1 );
	}

	public static double toDouble( String data, double def ) {
		if ( StringUtil.isEmpty( data ) )
			return def;

		double ret = def;
		try {
			ret = Double.parseDouble(data);
		} catch (NumberFormatException ex) { }
		return ret;
	}
	
	public static Integer toInteger( String data, Integer onerror ) {
	    if ( data == null || data.trim().length() == 0 )
			return onerror;

		Integer ret = onerror;

		try {
			ret = new Integer(data);
		} catch (NumberFormatException ex) { }

		return ret;
	}
	
	public static Integer[] toInteger(String data[], Integer onerror[]) {
	    Integer ret[] = onerror;
	    if(data== null || data.length <=0) return onerror;
	     
	    ret = new Integer[data.length];
	    for(int i=0; i<data.length;i++) {
	        ret[i] = toInteger(data[i], null);
	    }
	    
	    return ret;
	}
	
	public static Integer handleInteger(String data, Integer onerror ) {
	    if ( data == null || data.trim().length() == 0 )
			return onerror;

		Integer ret = onerror;

		try {
		    ret = new Integer(data);
			if(ret.intValue() <= 0 ) {
			    ret = onerror;
			}
		} catch (NumberFormatException ex) { }

		return ret;
	}
	
	/**
	 * @param requestParameterValues
	 * @param object
	 * @return
	 */
	public static Long[] toLong(String[] values, long onerror) {
		
		Long []retValues = new Long[values.length];
		for (int i = 0; i < values.length; i++) {
			try {
				retValues[i] = Long.valueOf(values[i].trim());
			} catch (NumberFormatException e) {
				retValues[i] = new Long(onerror);
				e.printStackTrace();
			}
		}
		return retValues;
	}
	
	public static BigDecimal toBigDecimal(String val) {
		
		if (StringUtil.isEmpty(val)) return null;
		
		try {
			return new BigDecimal(val);
		} catch (RuntimeException e) {
			return null;
		}
		
	}
	
	public  static String arrayToCsv(Object obj[]) {
		
	    if(obj == null || obj.length <=0) {
	        return null;
	    }
	    StringBuffer arr = new StringBuffer("");
	    for(int i=0; i<obj.length; i++) {
	        arr = arr.append(String.valueOf(obj[i]));
	        if(i+1 <obj.length) {
	        	arr.append(',');
	        }
	    }
	    return arr.toString();
	}

	
	@SuppressWarnings("unchecked")
	public static Map convertToTreeMap (Map map) {
	    
	    if (map == null || map.isEmpty()) {
	        return map;
	    }
	    
	    TreeMap tMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
	    
	    Iterator it = map.keySet().iterator();
	    
	    while (it.hasNext()) {
	        Object key = it.next();
	        Object val = map.get(key);
	        
	        tMap.put(key,val);
	    }
	    
	    return tMap;	    
	}	
	
	public static String handleNull(Float value, String def) {
		return isNull(value) ? def : String.valueOf(value);
	}
	
	public static Float handleNull(Float value, Float def) {
		return isNull(value) ? def : value;
	}
	
	public static BigDecimal handleNull(BigDecimal value, BigDecimal def) {
		return isNull(value) ? def : value;
	}
	
	public static String handleNull(BigDecimal value, String def) {
		return isNull(value) ? def : String.valueOf(value);
	}
	
	public static String concat(Object str1, Object str2) {
		return (String)str1 + " " + (String)str2;
	}
}
