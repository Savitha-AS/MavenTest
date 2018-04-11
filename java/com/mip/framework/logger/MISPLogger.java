package com.mip.framework.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The is an implementation class for the interface <code>Logger</code>.
 * <code>MISP</code> uses Logback to implement the logging methods defined in
 * the interface.
 * <p>
 * It also implements a custom level called STAT to implement the
 * <code>STAT</code> method by the interface.
 * 
 * @author THBS
 * @version 1.0, 11/07/2011
 * 
 */
public class MISPLogger implements com.mip.framework.logger.Logger {
	/**
	 * Enter method prefix.
	 */
	private static final String ENTER_PREFIX = " ENTERING METHOD : ";

	/**
	 * Exit method prefix.
	 */
	private static final String EXIT_PREFIX = " EXITING METHOD : ";

	/**
	 * Parameter prefix.
	 */
	private static final String PARAM_PREFIX = " PARAM";

	/**
	 * Return value prefix.
	 */
	private static final String RETURN_PREFIX = " RETURN VALUE = ";

	/**
	 * Logback logger for logging.
	 */
	private final Logger logger;

	/**
	 * Creates a logger for the given class.
	 * 
	 * @param cls
	 *            the <code>Class</code> object interested in logging.
	 */
	@SuppressWarnings("unchecked")
	public MISPLogger(Class cls) {
		logger = LoggerFactory.getLogger(cls);

		/**
		 * Print internal state.
		 */
		//LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		//StatusPrinter.print(lc);
	}

	/**
	 * Creates a logger for the given class name.
	 * 
	 * @param name
	 *            the name of the class interested in logging.
	 */
	public MISPLogger(String name) {
		logger = LoggerFactory.getLogger(name);
	}

	/**
	 * Creates a logger with a given logger. This is used to load all the
	 * predefined system loggers.
	 * 
	 * @param logger
	 *            a Logback logger instance
	 */
	public MISPLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * {@inheritDoc}
	 */
	public void trace(String message, Object... appendThese) {
		if(this.isTraceEnabled()){
			logger.trace(message, appendThese);			
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void debug(String message, Object... appendThese) {
		if (!this.isDebugEnabled()) {
			return;
		}

		if (appendThese != null) {
			StringBuilder msg = new StringBuilder(message);

			for (Object appendThis : appendThese) {
				msg.append(appendThis);
			}

			message = msg.toString();
		}
		logger.debug(message);
	}

	/**
	 * {@inheritDoc}
	 */
	public void info(String message, Object... appendThese) {
		if (!this.isInfoEnabled()) {
			return;
		}

		if (appendThese != null) {
			StringBuilder msg = new StringBuilder(message);

			for (Object appendThis : appendThese) {
				msg.append(appendThis);
			}

			message = msg.toString();
		}
		logger.info(message);
	}

	/**
	 * {@inheritDoc}
	 */
	public void warn(String message, Object... appendThese) {
		if (!this.isWarnEnabled()) {
			return;
		}

		if (appendThese != null) {
			StringBuilder msg = new StringBuilder(message);

			for (Object appendThis : appendThese) {
				msg.append(appendThis);
			}

			message = msg.toString();
		}
		logger.warn(message);
	}

	/**
	 * {@inheritDoc}
	 */
	public void warn(String message, Throwable throwable) {
		if (this.isWarnEnabled()) {
			logger.warn(message, throwable);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void error(String message) {
		if(this.isErrorEnabled()){
			logger.error(message);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void error(String message, Throwable throwable) {
		if(this.isErrorEnabled()){
			logger.error(message, throwable);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void entering(String methodName, Object... params) {
		logger.debug(this.getEnterMethodLogStr(methodName, params));
	}

	/**
	 * {@inheritDoc}
	 */
	public void exiting(String methodName) {

		StringBuffer buf = new StringBuffer();
		buf.append(EXIT_PREFIX);
		buf.append(methodName + "(..)");

		logger.debug(buf.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	public void exiting(String methodName, Object retVal) {
		
		StringBuffer buf = new StringBuffer();
		buf.append(EXIT_PREFIX);
		buf.append(methodName + "(..)");
		buf.append(RETURN_PREFIX);
		buf.append("[ ");
		buf.append(retVal);
		buf.append(" ]");

		logger.debug(buf.toString());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	/**
	 * Returns the log string for entry of a method.
	 * 
	 * @param methodName
	 *            a string containing the method name as the parameter.
	 * 
	 * @param params
	 *            an array of objects containing the parameters to the method.
	 * 
	 * @return a string to be logged indicating the entry of this method.
	 */
	private String getEnterMethodLogStr(String methodName, Object[] params) {
		StringBuffer buf = new StringBuffer(ENTER_PREFIX);
		buf.append(methodName + "(..)");

		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				if (i > 0 && i != params.length) {
					buf.append(",");
				}

				buf.append(PARAM_PREFIX);
				buf.append(i + 1);
				buf.append(" = [");
				buf.append(params[i]);
				buf.append("]");
			} 
		} 

		return buf.toString();
	}
}
