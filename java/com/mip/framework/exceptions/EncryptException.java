package com.mip.framework.exceptions;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 * <h3>Description of EncryptException.java : </h3>
 * <code>EncryptException</code> is derived from <code>MISPException</code>
 * The main functionality of this class is to handle the Exceptions while
 * trying to encode any plain text using some algorithm. The application must 
 * throw an instance of this class to handle such exceptions.
 * </p>
 * <p>
 * <b>Author : T H B S</b><br/>
 * <b>Date   : Apr 29, 2011</b>
 * </p>
 * <p>
 * <h3><u>Change Log</u></h3>
 * <TABLE border="1">
 * 	<TR>
 * 	   <TD>Version</TD><TD>Date</TD><TD>Author</TD><TD>Description</TD>
 * 	</TR>
 * 	<TR> 
 * 	   <TD>1.0</TD><TD>Apr 29, 2011</TD><TD>T H B S</TD><TD>Initial Version</TD>
 * 	</TR>
 * </TABLE>
 * </p>
 */
public class EncryptException extends MISPException implements Serializable
{
	/**
     * <code>serialVersionUID</code> is an instance of <tt>long</tt>.
     * <BR>
     * <b>Description:</b>Useful in serialization of the object instance.
     */
	private static final long serialVersionUID = -6950097910146381071L;	

	/**
     * <b>Description : </b>Default constructor.
     */
	public EncryptException()
	{
		super();
	}
	
	/**
     * <b>Description : </b>Constructor with <code>String</code> parameter.
     * 
     * @param message an instance of <tt>String</tt> which represents
     * the cause of the exception. 
     */
    public EncryptException(String message)
    {
        super(message);
    }

    /**
     * <b>Description : </b>Constructor with 
     * <code>NoSuchAlgorithmException</code> parameter
     * .
     * @param noSuchAlgorithmException, 
     * 			an instance of <tt>NoSuchAlgorithmException</tt> 
     * which represents the cause of the exception. 
     */
    public EncryptException(
    		NoSuchAlgorithmException noSuchAlgorithmException) 
    {
        super(noSuchAlgorithmException);
    }

    /**
     * <b>Description : </b>Constructor with 
     * <code>UnsupportedEncodingException</code> parameter
     * .
     * @param unsupportedEncodingException, 
     * 			an instance of <tt>UnsupportedEncodingException</tt> 
     * which represents the cause of the exception. 
     */
    public EncryptException(
    		UnsupportedEncodingException unsupportedEncodingException) 
    {
        super(unsupportedEncodingException);
    }
    
    /**
     * <b>Description : </b>Constructor with <code>Throwable</code> parameter.
     * 
     * @param cause an instance of <tt>Throwable</tt> 
     * which represents the cause of the exception. 
     */
    public EncryptException(Throwable cause)
    {
	   super(cause);
    }

    /**
     * <b>Description : </b>Constructor with <code>String</code>,<code>
     * NoSuchAlgorithmException</code> as parameters.
     * 
     * @param message an instance of <tt>String</tt> which has a meaningful 
     * message for the exception.
     *  
     * @param cause an instance of <tt>NoSuchAlgorithmException</tt> 
     * which represents the cause of the exception. 
     */
    public EncryptException(String message, NoSuchAlgorithmException cause)
    {
        super(message, cause);
    }

    /**
     * <b>Description : </b>Constructor with <code>String</code>,<code>
     * UnsupportedEncodingException</code> as parameters.
     * 
     * @param message an instance of <tt>String</tt> which has a meaningful 
     * message for the exception.
     *  
     * @param cause an instance of <tt>UnsupportedEncodingException</tt> 
     * which represents the cause of the exception. 
     */
    public EncryptException(String message, UnsupportedEncodingException cause)
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
    public EncryptException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
