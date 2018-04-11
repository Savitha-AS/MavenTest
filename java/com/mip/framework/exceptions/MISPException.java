package com.mip.framework.exceptions;

/**
 * <p>
 * <h3>Description of MISPException.java :</h3>
 * <code>MISPException</code> is derived from <code>Exception</code>
 * The main functionality of this class is to handle unforeseen circumstances
 * that might occur in the application.This class serves as a wrapper class for 
 * handling customized exceptions.
 * </p>
 * <p>
 * <b>Author : THBS</b> <b>Date : Apr 05, 2011</b>
 * </p>
 * <p>
 * <h3><u>Change Log</u></h3>
 * <TABLE border="1">
 * <TR>
 * <TD>Version</TD>
 * <TD>Date</TD>
 * <TD>Author</TD>
 * <TD>Description</TD>
 * </TR>
 * <TR>
 * <TD>1.0</TD>
 * <TD>Apr 05, 2011</TD>
 * <TD>THBS</TD>
 * <TD>Initial Version</TD>
 * </TR>
 * </TABLE>
 * </p>
 */
public class MISPException extends Exception
{
    /**
     * serialVersionUID an instance of long.
     * <BR>
     * <b>Description : </b>Useful in serialization of the object instance.
     */
    private static final long serialVersionUID = -5131907938107099281L;
    
    /**
     * <tt>throwableMispException</tt> an instance of <tt>Throwable</tt>.
     * <BR>
     * <b>Description : </b>This is used to get the root cause of the exception
     * being thrown.
     */
    private Throwable throwableMispException;

    /**
     * <b>Description : </b>Default constructor.
     */
    public MISPException()
    {
	   super();
    }

    /**
     * <b>Description : </b>Constructor with <code>String</code> parameter.
     * 
     * @param mispException an instance of <tt>String</tt> which represents
     * the cause of the exception. 
     */
    public MISPException(String mispException)
    {
	   super(mispException);
    }

    /**
     * <b>Description : </b>Constructor with <code>Throwable</code> parameter.
     * 
     * @param throwableMispException an instance of <tt>Throwable</tt> 
     * which represents the cause of the exception. 
     */
    public MISPException(Throwable throwableMispException)
    {
	   super(throwableMispException);
	   this.throwableMispException = throwableMispException;
    }

    /**
     * <b>Description : </b>Constructor with <code>String</code>,<code>Throwable
     * </code> as parameters.
     * 
     * @param mispException an instance of <tt>String</tt> which contains a 
     * custom message for better understanding of the exception.
     *  
     * @param throwableMispException an instance of <tt>Throwable</tt> 
     * which represents the cause of the exception. 
     */
    public MISPException(String mispException,Throwable throwableMispException)
    {
	   super(mispException, throwableMispException);
	   this.throwableMispException = throwableMispException;
    }

    /**
     * <p>
     * <h3>Description of getCause() :</h3>
     *  This method returns you the root cause of the exception thrown.
     * </p>
     * <b>
     * @param none.
     * 
     * @return an instance of <code>Throwable</code> containing the root cause 
     * of the exception being thrown if the object instance is not null.
     * </b>         
     * </p> 
     */
    public Throwable getCause()
    {
	   return this.throwableMispException.getCause();
    }
}
