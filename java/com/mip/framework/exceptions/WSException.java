package com.mip.framework.exceptions;

import java.rmi.RemoteException;

import com.tigo.www.faultdata.FaultType;

/**
 * <p>
 * <h3>Description of WSException.java : </h3>
 * <code>WSException</code> is derived from <code>MISPException</code>
 * The main functionality of this class is to handle the Exceptions while
 * doing any WS related operations. The application must 
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
public class WSException extends MISPException 
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
	public WSException()
	{
		super();
	}
	
	/**
     * <b>Description : </b>Constructor with <code>String</code> parameter.
     * 
     * @param message an instance of <tt>String</tt> which represents
     * the cause of the exception. 
     */
    public WSException(String message)
    {
        super(message);
    }

    /**
     * <b>Description : </b>Constructor with <code>RemoteException</code> parameter
     * .
     * @param remoteException an instance of <tt>RemoteException</tt> 
     * which represents the cause of the exception. 
     */
    public WSException(RemoteException remoteException) 
    {
        super(remoteException);
    }
    
    /**
     * <b>Description : </b>Constructor with <code>Throwable</code> parameter.
     * 
     * @param cause an instance of <tt>Throwable</tt> 
     * which represents the cause of the exception. 
     */
    public WSException(Throwable cause)
    {
	   super(cause);
    }

    /**
     * <b>Description : </b>Constructor with <code>String</code>,<code>
     * RemoteException</code> as parameters.
     * 
     * @param message an instance of <tt>String</tt> which has a meaningful 
     * message for the exception.
     *  
     * @param cause an instance of <tt>RemoteException</tt> 
     * which represents the cause of the exception. 
     */
    public WSException(String message, RemoteException cause)
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
    public WSException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    /**
     * <b>Description : </b>Constructor with <code>FaultType</code> parameter
     * .
     * @param faultType an instance of <tt>FaultType</tt> 
     * which represents the cause of the exception. 
     */
    public WSException(FaultType faultType) 
    {
        super(faultType);
    }
    
    /**
     * <b>Description : </b>Constructor with <code>String</code>,<code>
     * FaultType</code> as parameters.
     * 
     * @param message an instance of <tt>String</tt> which has a meaningful 
     * message for the exception.
     *  
     * @param cause an instance of <tt>FaultType</tt> 
     * which represents the cause of the exception. 
     */
    public WSException(String message, FaultType cause)
    {
        super(message, cause);
    }
}

