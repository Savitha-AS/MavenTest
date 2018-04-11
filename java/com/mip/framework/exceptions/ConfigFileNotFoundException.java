package com.mip.framework.exceptions;

/**
 * <p>
 * <h3>Description of ConfigFileNotFoundException.java : </h3>
 * <code>ConfigFileNotFoundException</code> is derived from <code>MISPException
 * </code>.
 * The main functionality of this class is to handle the absence of a 
 * configuration file required for setting up of the application or during
 * the referencing of a file during any operation.
 * </p>
 * <p>
 * <b>Author : THBS</b>
 * <b>Date   : Apr 05, 2011</b>
 * </p>
 * <p>
 * <h3><u>Change Log</u></h3>
 * <TABLE border="1">
 * 	<TR>
 * 		<TD>Version</TD><TD>Date</TD><TD>Author</TD><TD>Description</TD>
 * 	</TR>
 * 	<TR> 
 * 		<TD>1.0</TD><TD>Apr 05, 2011</TD><TD>THBS</TD><TD>Initial Version</TD>
 * 	</TR>
 * </TABLE>
 * </p>
 */
public class ConfigFileNotFoundException extends MISPException
{
    /**
     * serialVersionUID an instance of long.
     * <BR>
     * <b>Description : </b>Useful in serialization of the object instance.
     */
    private static final long serialVersionUID = 2649126575201379746L;

    /**
     * <b>Description : </b>Default constructor.
     */
    public ConfigFileNotFoundException()
    {
	   super();
    }

    /**
     * <b>Description : </b>Constructor with <code>String</code> parameter.
     * 
     * @param message an instance of <tt>String</tt> which represents
     * the cause of the exception. 
     */
    public ConfigFileNotFoundException(String message)
    {
	   super(message);
    }

    /**
     * <b>Description : </b>Constructor with <code>Throwable</code> parameter.
     * 
     * @param cause an instance of <tt>Throwable</tt> 
     * which represents the cause of the exception. 
     */
    public ConfigFileNotFoundException(Throwable cause)
    {
	   super(cause);
    }

    /**
     * <b>Description : </b>Constructor with <code>String</code>,<code>Throwable
     * </code> as parameters.
     * 
     * @param message an instance of <tt>String</tt> which has a meaningful 
     * message for the exception.
     *  
     * @param cause an instance of <tt>Throwable</tt> 
     * which represents the cause of the exception. 
     */
    public ConfigFileNotFoundException(String message,Throwable cause)
    {
	   super(message, cause);
    }
}