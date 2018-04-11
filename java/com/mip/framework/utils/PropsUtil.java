package com.mip.framework.utils;

import static com.mip.framework.constants.PlatformConstants.PROPERTY_FILE_NAME;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

public class PropsUtil {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			PropsUtil.class);
	
	
	private static Properties props = null;	

	public static Properties loadProperties() throws IOException{
		logger.entering("loadProperties");
		try {
			props = new Properties();
			props.load(PropsUtil.class
					.getResourceAsStream(PROPERTY_FILE_NAME));
		} catch (IOException e) {
			logger.error("Exception has occured while loading property file.",e);
			throw new IOException(e);
		}
		logger.exiting("loadProperties");
		return props;
	}

	/**
	 * This method is used to read a property from configuration. It also throws
	 * a runtime Exception if the key is null.
	 * 
	 * @param key
	 *            instance of String containing the key.
	 * 
	 * @return an String containing the value for key being passed.
	 * 
	 */
	public static String getProperty(String key) throws IOException {
		String value = "";

		loadProperties();
		if (props != null) {
			value = props.getProperty(key);
		}
		return value;

	}

	/**
	 * This method is used to read a property set of properties from
	 * configuration having the baseKey passed as prefix.
	 * 
	 * It also throws a runtime Exception if the baseKey is null.
	 * 
	 * @param key
	 *            instance of String containing the baseKkey, which forms the
	 *            initial prefix of the entire key.
	 * 
	 * @return a List containing the value for property starting
	 *         with the baseKey passed as the key prefix.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getPropertyList(String baseKey)
			throws IOException {

		loadProperties();
		List<String> valuesList = new ArrayList<String>();
		if (props != null) {
			
			Map<String, String> sortedMap = TypeUtil.convertToTreeMap(props);
			
			String key;
			Iterator<String> itr = sortedMap.keySet().iterator();

			while (itr.hasNext()) {
				key = itr.next().toString();
				if (key.startsWith(baseKey)) {
					valuesList.add(sortedMap.get(key));
				}
			}
		}

		return valuesList;
	}
	
}
