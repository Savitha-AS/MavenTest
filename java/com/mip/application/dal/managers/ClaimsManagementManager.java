package com.mip.application.dal.managers;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.mip.application.constants.FaultMessages;
import com.mip.application.model.ClaimDetails;
import com.mip.framework.dao.impls.DataAccessManager;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * <p>
 * <code>ClaimsManagementManager.java</code> contains all the data access layer methods 
 * pertaining to ClaimDetails model.
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
public class ClaimsManagementManager  extends DataAccessManager<ClaimDetails, Integer>{

	public ClaimsManagementManager() {
		super(ClaimDetails.class);
	}
	
	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			ClaimsManagementManager.class);
	
	/**
	 * This method persists the ClaimDetails in DB
	 * 
	 * @param claimModel
	 * 			- an instance of ClaimDetails
	 * @return
	 * 			- True if successful, false otherwise
	 * 
	 * @throws DBException
	 */
	public boolean claimInsurance(ClaimDetails claimModel) throws DBException {
		logger.entering("claimInsurance");
		
		boolean isClaimed = false;
		try{
			getHibernateTemplate().save(claimModel);
			
			isClaimed = true;
		}
		catch(DataAccessException dae){
			logger.error(FaultMessages.CLAIM_INSURANCE_EXCEPTION, dae);
			throw new DBException(dae);
		}
		logger.exiting("claimInsurance", isClaimed);
		return isClaimed;
	}
	
	/**
	 * This method returns all the claim details.
	 * 
	 * @return
	 * 			- <code>List</code> of ClaimDetails
	 * 
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public List<ClaimDetails> getAllClaimDetails() throws DBException {
		logger.entering("getAllClaimDetails");
		
		List<ClaimDetails>  claimList = null;
		try {
			
			claimList = getHibernateTemplate().find("FROM ClaimDetails");
			
		} catch (DataAccessException dae) {
			logger.error(FaultMessages.CLAIM_LIST_EXCEPTION, dae);
			throw new DBException(dae);
		}
		
		logger.exiting("getAllClaimDetails");
		return claimList;
	}
}
