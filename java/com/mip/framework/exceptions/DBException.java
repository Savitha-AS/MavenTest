package com.mip.framework.exceptions;

import java.sql.SQLException;

/**
 * <p>
 * <h3>Description of DBException.java : </h3>
 * <code>DBException</code> is derived from <code>MISPException</code>
 * The main functionality of this class is to handle the Exceptions while
 * doing any data base related operations. The application must 
 * throw an instance of this class to handle such exceptions.
 * </p>
 * <p>
 * <b>Author : THBS</b>
 * <b>Date   : Apr 05, 2011</b>
 * </p>
 * <p>
 * <h3><u>Change Log</u></h3>
 * <TABLE border="1">
 * 	<TR>
 * 	   <TD>Version</TD><TD>Date</TD><TD>Author</TD><TD>Description</TD>
 * 	</TR>
 * 	<TR> 
 * 	   <TD>1.0</TD><TD>Apr 05, 2011</TD><TD>THBS</TD><TD>Initial Version</TD>
 * 	</TR>
 * </TABLE>
 * </p>
 */
public class DBException extends MISPException 
{
	/**
     * <code>serialVersionUID</code> is an instance of <tt>long</tt>.
     * <BR>
     * <b>Description:</b>Useful in serialization of the object instance.
     */
    private static final long serialVersionUID = 1682778434462068659L;

	/**
     * <b>Description : </b>Default constructor.
     */
	public DBException()
	{
		super();
	}
	
	/**
     * <b>Description : </b>Constructor with <code>String</code> parameter.
     * 
     * @param message an instance of <tt>String</tt> which represents
     * the cause of the exception. 
     */
    public DBException(String message)
    {
        super(message);
    }

    /**
     * <b>Description : </b>Constructor with <code>SQLException</code> parameter
     * .
     * @param sqlException an instance of <tt>SQLException</tt> 
     * which represents the cause of the exception. 
     */
    public DBException(SQLException sqlException) 
    {
        super(sqlException);
    }
    
    /**
     * <b>Description : </b>Constructor with <code>Throwable</code> parameter.
     * 
     * @param cause an instance of <tt>Throwable</tt> 
     * which represents the cause of the exception. 
     */
    public DBException(Throwable cause)
    {
	   super(cause);
    }

    /**
     * <b>Description : </b>Constructor with <code>String</code>,<code>
     * SQLException</code> as parameters.
     * 
     * @param message an instance of <tt>String</tt> which has a meaningful 
     * message for the exception.
     *  
     * @param cause an instance of <tt>SQLException</tt> 
     * which represents the cause of the exception. 
     */
    public DBException(String message, SQLException cause)
    {
        super(message, cause);
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
    public DBException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
