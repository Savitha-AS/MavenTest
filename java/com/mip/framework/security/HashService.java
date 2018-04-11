package com.mip.framework.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.mip.framework.exceptions.EncryptException;

import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;


/*
 * HISTORY
 * =====================================================================
 * Version | Date        | Who              | Comments
 * ---------------------------------------------------------------------
 * 1.0     | 19/04/2011  | T H B S          |Initial Version.
 * =====================================================================
 */

/**
 * <p><code>HashService.java</code> class provides the functionality
 * to encrypt the given plain text. This returns the encrypted hash for the
 * input plain text.
 * </p>
 *
 * @see java.security.MessageDigest
 * @author T H B S
 *
 */
public final class HashService
{
    private static HashService singleton;

    private HashService() {}
    
    /**
     * This method implements the functionality to encrypt the provided
     * plain text.
     * 
     * @param plaintext
     * 
     * @return Encrypted Hash for the input plain text.
     * @throws IOException 
     * 
     * @throws Exception
     */
    public synchronized static String encrypt(String plaintext)
    		throws EncryptException, IOException
    {	
        MessageDigest md = null;
        try{
            md = MessageDigest.getInstance("SHA");
        }
        catch (NoSuchAlgorithmException e){
            throw new EncryptException(e.getMessage());
        }
        try{
        	if(null != plaintext)
        		md.update(plaintext.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException exception){
            throw new EncryptException(exception.getMessage());
        }
        byte raw[] = md.digest();
        String hash = (new BASE64Encoder()).encode(raw);
        System.out.println((new BASE64Encoder()).encode("Tigo123!".getBytes()));
        return hash;
    }
    
    public static void main(String[] args) throws IOException, EncryptException {
    	
    	/*  MessageDigest md = null;
          try{
              md = MessageDigest.getInstance("SHA");
          }
          catch (NoSuchAlgorithmException e){
              throw new EncryptException(e.getMessage());
          }
          try{
          	
          		md.update("KE6mPBtavW7vDuLvuZ0F+U2dvAs=".getBytes("UTF-8"));
          }
          catch (UnsupportedEncodingException exception){
              throw new EncryptException(exception.getMessage());
          }
          byte raw[] = md.digest();
          System.out.println(raw);
    	
    	byte[] decodedValue = (new BASE64Decoder()).decodeBuffer(raw.toString());
    	System.out.println(decodedValue.toString());
    	System.out.println(new String(decodedValue));*/
    	
    	 System.out.println((new BASE64Encoder()).encode("Tigo123!".getBytes()));
	}

    /**
     * Implementation of Singleton design pattern.
     * 
     * @return A singleton instance of the class.
     */
    public static synchronized HashService getInstance()
    {
        if (singleton == null)
        {
            singleton = new HashService();
        }
        return singleton;
    }
}