package com.mip.application.dal.managers;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.mip.application.model.HashHistory;
import com.mip.framework.dao.impls.DataAccessManager;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * <p>
 * <code>HashHistoryManager.java</code> contains all the data access layer  
 * methods pertaining to HashHistory model.
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
public class HashHistoryManager 
	extends DataAccessManager<HashHistory, Integer> {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			HashHistoryManager.class);

	public HashHistoryManager() {
		super(HashHistory.class);		
	}
	
	/**
	 * Get hash history for a particular user.
	 * 
	 * @param hashHistory
	 * 				HashHistory having a reference of UserDetails.
	 * @return List of HashHistory records.
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public List<HashHistory> getHashHistory(HashHistory hashHistory) 
		throws DBException{
		
		Object[] params = {hashHistory};
		logger.entering("getHashHistory", params);
		
		List<HashHistory> historyList = null;
		
		try{
			historyList = getHibernateTemplate().find(
					"FROM HashHistory hashHistory " +
					"WHERE hashHistory.userDetails = ? ",
					hashHistory.getUserDetails());
			
		}catch(DataAccessException dae){
			logger.error("Exception occured while fetching " +
					"Hash History details.",dae);
			throw new DBException(dae);
		}
		
		logger.exiting("getHashHistory",historyList);
		return historyList;
	}
	
	/**
	 * Deletes a HashHistory record.
	 * 
	 * @param hashHistory HashHistory record to be deleted.
	 * @throws DBException
	 */
	public void deleteHistory(HashHistory hashHistory)
		throws DBException {
			
		Object[] params = {hashHistory};
		logger.entering("deleteHistory", params);
		
		try{
			getHibernateTemplate().delete(hashHistory);
			
		}catch(DataAccessException e){
			logger.error("Exception occured while deleting Hash History " +
					"details.", e);
			throw new DBException(e);
		}

		logger.exiting("deleteHistory");
	}
	
	/**
	 * Updates a HashHistory record.
	 * 
	 * @param hashHistory HashHistory record to be updated.
	 * @throws DBException
	 */
	public void updateHistory(HashHistory hashHistory)
		throws DBException {
			
		Object[] params = {hashHistory};
		logger.entering("updateHistory", params);
		
		try{
			getHibernateTemplate().update(hashHistory);
			
		}catch(DataAccessException e){
			logger.error("Exception occured while updating Hash History " +
					"details.", e);
			throw new DBException(e);
		}

		logger.exiting("updateHistory");
	}

	/**
	 * Adds a new HashHistory record.
	 * 
	 * @param hashHistory HashHistory record to be inserted.
	 * @throws DBException 
	 */
	public void addHistory(HashHistory hashHistory)
		throws DBException {
			
		Object[] params = {hashHistory};
		logger.entering("addHistory", params);
		
		try{
			getHibernateTemplate().save(hashHistory);
			
		}catch(DataAccessException e){
			logger.error("Exception occured while adding Hash History " +
					"details.", e);
			throw new DBException(e);
		}

		logger.exiting("addHistory");
	}
}
