package com.mip.application.services.transaction;

import java.util.List;

import com.mip.application.dal.managers.HashHistoryManager;
import com.mip.application.dal.managers.UserHashManager;
import com.mip.application.dal.managers.UserManager;
import com.mip.application.model.HashHistory;
import com.mip.application.model.UserDetails;
import com.mip.application.model.UserHash;
import com.mip.application.services.LoginService;
import com.mip.application.view.AdminConfigVO;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * This class injects Transaction into the Service layer pertaining to 
 * User use-case model. 
 * 
 * @author T H B S
 */
public class UserTXService {

	/**
	 * An instance of logger.
	 */	
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			UserTXService.class);
	
	/**
	 * Set inversion of Control for <code>UserManager</code>, 
	 * <code>UserHashManager</code>, <code>HashHistoryManager</code> 
	 * and <code>LoginService</code>
	 */
	private UserManager userManager;
	private UserHashManager userHashManager;
	private HashHistoryManager hashHistoryManager; 	
	private LoginService loginService;
			
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setUserHashManager(UserHashManager userHashManager) {
		this.userHashManager = userHashManager;
	}

	public void setHashHistoryManager(HashHistoryManager hashHistoryManager) {
		this.hashHistoryManager = hashHistoryManager;
	}
	
	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}

	/**
	 * Sets the new password into database, and also sets the old password 
	 * in the History table.
	 *  
	 * @param currentHash Current Hash to be set in the History table. 
	 * @param historyList List of Hash History.
	 * @param hashHistory
	 * @param userHash
	 * @return
	 * @throws MISPException
	 */
	public void setPassword(String currentHash, List<HashHistory> historyList, 
			UserDetails userDetails, UserHash userHash) 
		throws MISPException{
	
		Object[] params = {currentHash};
		logger.entering("setPassword", params);
		
		try{
			
			/**
			 * Get Admin Config details.
			 */
			AdminConfigVO adminConfigVO = loginService.getConfigDetails();
			
			/**
			 * Delete hash with priority set to 
			 * <code>adminConfigVO.getPwdHistoryLimit()</code> from
			 * HashHistory table
			 */
			if(historyList != null){
				for(HashHistory hashHist : historyList){
					if(hashHist.getHashPriority() == adminConfigVO.
							getPwdHistoryLimit()){
						
						hashHistoryManager.deleteHistory(hashHist);
					}
				}
			}
			/**
			 * Update the priorities of other hashes by increasing the count, 
			 * in HashHistory table.
			 */
			if(historyList != null){
				for(HashHistory hashHist : historyList){
					if(hashHist.getHashPriority() != adminConfigVO.
							getPwdHistoryLimit()){
						
						int hashPriority = hashHist.getHashPriority() + 1;			
						hashHist.setHashPriority(hashPriority);
						
						hashHistoryManager.updateHistory(hashHist);
					}
				}
			}
			
			/**
			 * Update the user's hash with <code>newHash</code>, 
			 * in UserHash table.
			 */
			userHashManager.updateUserHash(userHash);
			
			/**
			 * Insert <code>currentHash</code> into HashHistory with priority 1.
			 */
			HashHistory hashHistory = new HashHistory();
			hashHistory.setUserDetails(userDetails);
			hashHistory.setUserHash(currentHash);
			hashHistory.setHashPriority(1);
			hashHistoryManager.addHistory(hashHistory);
		}
		catch(DBException exception){
			logger.error("An exception occured while changing the password " +
					"in a transaction.", exception);
			throw new MISPException(exception);
		}

		logger.exiting("setPassword");
	}
	

	
	/**
	 * Registers the user details into database, in a Transaction.
	 * 
	 * @param userDetails 
	 * 				UserDetails to be saved.
	 * @param userHash 
	 * 				UserHash to be saved.
	 * @throws DBException
	 */
	public void registerUser(UserDetails userDetails, UserHash userHash) 
		throws DBException{
		logger.entering("registerUser");	
		
		try{
			userManager.addUserDetails(userDetails);
			userHashManager.addUserHash(userHash);
		}
		catch(DBException exception){
			logger.error("An exception occured while registering User Details " +
					"in a transaction.", exception);
			throw exception;
		}
		
		logger.exiting("registerUser");
	}
}
