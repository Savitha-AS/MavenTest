package com.mip.framework.exceptions;

/**
 * <p>
 * <h3>Description of KeyNotFoundException.java : </h3>
 * <code>KeyNotFoundException</code> is derived from <code>MISPException</code>
 * The main functionality of this class is to handle the absence of a key in
 * the application's configuration files. When referencing a key which is
 * not present, this class has to be invoked to handle such a case.
 * </p>
 * <p>
 * <b>Author : THBS</b>
 * <b>Date   : Jul 17, 2009</b>
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
public class KeyNotFoundException extends MISPException
{
    /**
     * serialVersionUID an instance of long.
     * <BR>
     * <b>Description : </b>Useful in serialization of the object instance.
     */
    private static final long serialVersionUID = -3601164427299844313L;

    /**
     * <b>Description : </b>Default constructor.
     */
    public KeyNotFoundException()
    {
	   super();
    }

    /**
     * <b>Description : </b>Constructor with <code>String</code> parameter.
     * 
     * @param message an instance of <tt>String</tt> which represents
     * the cause of the exception. 
     */
    public KeyNotFoundException(String message)
    {
	   super(message);
    }

    /**
     * <b>Description : </b>Constructor with <code>Throwable</code> parameter.
     * 
     * @param cause an instance of <tt>Throwable</tt> 
     * which represents the cause of the exception. 
     */
    public KeyNotFoundException(Throwable cause)
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
    public KeyNotFoundException(String message,Throwable cause)
    {
	   super(message, cause);
    }
}
