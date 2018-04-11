package com.mip.framework.security;

import org.acegisecurity.providers.encoding.ShaPasswordEncoder;
import org.springframework.dao.DataAccessException;

import com.mip.framework.exceptions.EncryptException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/*
 * HISTORY
 * =====================================================================
 * Version | Date        | Who              | Comments
 * ---------------------------------------------------------------------
 * 1.0     | 19/04/2011  | T H B S          |Initial Version.
 * =====================================================================
 */

/**
 * <p>
 * <code>CustomPasswordEncoder.java</code> extends the 
 * <code>ShaPasswordEncoder</code> class. This class overrides the <code>
 * encodePassword</code> method of super class to provide custom encryption
 * mechanism implemented by MISP platform.
 * </p>
 * 
 * @see org.acegisecurity.providers.encoding.ShaPasswordEncoder
 * @author T H B S
 *
 */
public class CustomPasswordEncoder extends ShaPasswordEncoder 
{

	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			CustomPasswordEncoder.class);
	
	/**
	 * <p>This method is overridden by MISP platform to inject custom password
	 * encryption mechanism.</p>
	 *  
	 * {@inheritDoc}
	 */
	public String encodePassword(String rawPass, Object salt)
		throws DataAccessException {
		
		String encodedPassWord = null;
		try {
			encodedPassWord = HashService.encrypt(rawPass);			
		}
		catch (EncryptException exception) {
			logger.error("An exception occured while changing the password.",
					exception);
		}
		
		return encodedPassWord;
	}
	
	/**
	 * @override
	 */
	public boolean isPasswordValid(String encPass, String rawPass, Object salt)
    {		
		String pass1 = "" + encPass;
        String pass2 = encodePassword(rawPass, salt);
                             
        return pass1.equals(pass2);
    }
}
