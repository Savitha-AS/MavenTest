package com.mip.framework.exceptions;


/**
 * <p>
 * <h3>Description of XMLException.java : </h3>
 * <code>XMLException</code> is derived from <code>MISPException
 * </code>.
 * The main functionality of this class is to handle the XML exceptions
 * while performing any XML related operations in the platform.
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
public class XMLException extends MISPException
{

	/**
	 * serialVersionUID an instance of long.
     * <BR>
     * <b>Description : </b>Useful in serialization of the object instance.
	 */
	private static final long serialVersionUID = -4450474397968633150L;
	

	/**
     * Constructs a new exception with the specified detail message.
     * 
     * @param message
     *            the detail message. The detail message is saved for later
     *            retrieval by the {@link #getMessage()} method.
     */
    public XMLException(String message)
    {
        super(message);
    }

    /**
     * Constructs a new exception with the specified cause.
     * 
     * @param cause
     *            the cause (which is saved for later retrieval by the
     *            {@link #getCause()} method).
     * 
     */
    public XMLException(Throwable cause)
    {
        super(cause);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * 
     * @param message
     *            the detail message (which is saved for later retrieval by the
     *            {@link #getMessage()} method).
     * @param cause
     *            the cause (which is saved for later retrieval by the
     *            {@link #getCause()} method).
     * 
     */
    public XMLException(String message, Throwable cause)
    {
        super(message, cause);
    }
}

