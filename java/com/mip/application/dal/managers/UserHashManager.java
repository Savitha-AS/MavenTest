package com.mip.application.dal.managers;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.mip.application.model.UserHash;
import com.mip.framework.dao.impls.DataAccessManager;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * <p>
 * <code>UserHashManager.java</code> contains all the data access layer methods 
 * pertaining to UserHash model.
 * </p>
 * 	
 * <br/>
 * This class extends the <code>DataAccessManager</code> class of 
 * our MISP framework.
 * </p>
 * 
 * @see com.mip.framework.dao.impls.DataAccessManager
 * 
 * @author T H B S
 * 
 */
public class UserHashManager extends DataAccessManager<UserHash, Integer> {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			UserHashManager.class);

	public UserHashManager() {
		super(UserHash.class);		
	}
	
	/**
	 * Gets a particular UserHash record based on a UserDetails record.
	 * 
	 * @param userHash 
	 * 			UserHash having a reference of UserDetails.
	 * @return UserHash record of a particular user.
	 * 
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public UserHash getUserHash(UserHash userHash) 
		throws DBException {
		
		Object[] params = {userHash};
		logger.entering("getUserHash", params);
		
		List<UserHash> userList = null;
		
		try{
			userList = getHibernateTemplate().find(
					"FROM UserHash userHash WHERE userHash.userDetails = ?",
					userHash.getUserDetails());
			if(userList != null && userList.size() > 0){
				userHash = userList.get(0);
			}
		}catch(DataAccessException e){
			logger.error("Exception occured while retrieving Hash Details.", e);
			throw new DBException(e);
		}

		logger.exiting("getUserHash", userHash);
		return userHash;
	}
	
	/**
	 * Adds a new UserHash record.
	 * 
	 * @param userHash 
	 * 				UserHash record to be added
	 * @throws DBException
	 */
	public void addUserHash(UserHash userHash)
		throws DBException {
			
		Object[] params = {userHash};
		logger.entering("addUserHash", params);
		
		try{
			getHibernateTemplate().save(userHash);
			
		}catch(DataAccessException e){
			logger.error("Exception occured while saving Hash Details.", e);
			throw new DBException(e);
		}

		logger.exiting("addUserHash");
	}
	
	/**
	 * Updates a UserHash record.
	 * 
	 * @param userHash
	 * 			UserHash record to be updated.
	 * @throws DBException
	 */
	public void updateUserHash(UserHash userHash)
		throws DBException {
			
		Object[] params = {userHash};
		logger.entering("updateUserHash", params);
		
		try{
			getHibernateTemplate().update(userHash);
			
		}catch(DataAccessException e){
			logger.error("Exception occured while updating Hash Details.", e);
			throw new DBException(e);
		}

		logger.exiting("updateUserHash");
	}
}
