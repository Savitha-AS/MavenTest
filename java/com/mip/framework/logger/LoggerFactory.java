package com.mip.framework.logger;

import org.slf4j.Logger;

/**
 * This class <code>LoggerFactory</code> provides the point of access to the
 * logging layer.
 * 
 * The typical usage in getting a <code>MISPLogger</code> instance to start
 * logging is as shown below :
 * 
 * <p>
 * <blockquote>
 * 
 * <pre>
 * private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
 * 		MyClass.class);
 * </pre>
 * 
 * or
 * 
 * <pre>
 * private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
 * 		&quot;MyJSP&quot;);
 * </pre>
 * 
 * <p>
 * It can then use all the methods defined under <code>Logger</code> interface
 * appropriately.
 * 
 * <pre>
 *   For Ex :
 *     if (logger.isDebugEnabled())
 *        logger.debug(&quot;My debug message&quot;);
 * 
 * </pre>
 * 
 * </blockquote>
 * 
 * @author THBS
 * @version 1.0, 11/07/2011
 * 
 * @see Logger
 * @see MISPLogger
 * 
 */

public final class LoggerFactory {

	private static LoggerFactory theInstance = new LoggerFactory();

	/**
	 * Returns the instance of this class
	 * 
	 * @return an instance of <code>LoggerFactory</code>
	 */
	public static LoggerFactory getInstance() {
		return theInstance;
	}

	/**
	 * Gets the instance of logger which is associated with the given class.
	 * 
	 * @param clss
	 *            a <code>Class</code> object interested in using the logger.
	 * 
	 * @return an implementation of <code>Logger</code>
	 * 
	 */
	@SuppressWarnings("unchecked")
	public MISPLogger getLogger(Class clss) {
		MISPLogger log = new MISPLogger(clss);
		return log;
	}

	/**
	 * Gets the instance of logger which is associated with the given class
	 * name.
	 * 
	 * @param name
	 *            name of a class interested in using the logger.
	 * 
	 * @return an implementation of <code>Logger</code>
	 * 
	 */
	public MISPLogger getLogger(String name) {
		MISPLogger log = new MISPLogger(name);
		return log;
	}

}