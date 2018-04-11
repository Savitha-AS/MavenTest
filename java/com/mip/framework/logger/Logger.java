package com.mip.framework.logger;

/**
 * This is an interface for application logger.It contains methods which
 * facilitate the logging.Implementing <code>Logger</code> interface will
 * unify most of the logging requirements and result in consistent logging
 * system.
 * <p>
 * Application loggers which implement this must map the methods to appropriate
 * logging levels.  
 * 
 * @author THBS
 * @version 1.0, 11/07/2011
 */
public interface Logger {

    /**
     * Logs the given string as a trace message, provided logging at 
     * <tt>TRACE</tt> level is enabled for the current logger.
     * <p>
     * Any <tt>appendThese</tt> arguments passed will be appended to the 
     * message.
     * 
     * @param message
     *            the message that needs to be logged.
     * 
     * @param appendThese
     *            fragments which will be appended to the message.
     */
    void trace(String message, Object... appendThese);

    /**
     * Logs the given string as a debug message, provided logging at 
     * <tt>DEBUG</tt> level is enabled for the current logger.
     * <p>
     * Any <tt>appendThese</tt> arguments passed will be appended to the 
     * message.
     * 
     * @param message
     *            the message that needs to be logged.
     * 
     * @param appendThese
     *            fragments which will be appended to the message.
     */
    void debug(String message, Object... appendThese);

    /**
     * Logs the given string as an information, provided logging at 
     * <tt>INFO</tt> level is enabled for the current logger.
     * <p>
     * Any <tt>appendThese</tt> arguments passed will be appended to the 
     * message. 
     * 
     * @param message
     *            the message that needs to be logged.
     * 
     * @param appendThese
     *            fragments which will be appended to the message.            
     */
    void info(String message, Object... appendThese);

    /**
     * Logs the given string as a warning message, provided logging at 
     * <tt>WARN</tt> level is enabled for the current logger.
     * <p>
     * Any <tt>appendThese</tt> arguments passed will be appended to the 
     * message.
     * 
     * @param message
     *            the message that needs to be logged.
     * 
     * @param appendThese
     *            fragments which will be appended to the message.            
     */
    void warn(String message, Object... appendThese);

    /**
     * Logs the given string at the warn level with the exception message.
     * 
     * @param message
     *            the message that needs to be logged.
     * 
     * @param throwable
     *            the <code>Throwable</code> to be logged.
     */
    void warn(String message, Throwable throwable);

    /**
     * Logs the given string as an error message.
     * 
     * @param message
     *            the message that needs to be logged.
     */
    void error(String message);

    /**
     * Logs the given string at the error level with the exception message.
     * 
     * @param message
     *            the message that needs to be logged.
     * 
     * @param throwable
     *            the <code>Throwable</code> to be logged.
     */
    void error(String message, Throwable throwable);

    /**
     * Logs a method entry with the list of its parameters at debug level.
     * 
     * @param methodName
     *            the string containing method name.
     * 
     * @param params
     *            an array of the parameters of the method.
     */
    void entering(String methodName, Object... params);

    /**
     * Logs the exit from a method at debug level.
     * 
     * @param methodName
     *            the string containing method name.
     */
    void exiting(String methodName);

    /**
     * Logs the exit from a method which returns a value at debug level.
     * 
     * @param methodName
     *            the string containing method name.
     * 
     * @param retVal
     *            the return value of the method.
     */
    void exiting(String methodName, Object retVal);

    /**
     * Returns the <code>TRACE</code> status of the logger in context.
     * 
     * @return boolean indicating whether trace is turned on or off
     */
    boolean isTraceEnabled();

    /**
     * Returns the <code>DEBUG</code> status of the logger in context.
     * 
     * @return boolean indicating whether debug is turned on or off
     */
    boolean isDebugEnabled();

    /**
     * Returns the <code>WARN</code> status of the logger in context.
     * 
     * @return boolean indicating whether warn is turned on or off
     */
    boolean isWarnEnabled();

    /**
     * Returns the <code>INFO</code> status of the logger in context.
     * 
     * @return boolean indicating whether info is turned on or off
     */
    boolean isInfoEnabled();

    /**
     * Returns the <code>ERROR</code> status of the logger in context.
     * 
     * @return boolean indicating whether error is turned on or off
     */
    boolean isErrorEnabled();
}
