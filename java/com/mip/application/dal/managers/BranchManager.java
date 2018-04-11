package com.mip.application.dal.managers;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.mip.application.model.BranchDetails;
import com.mip.framework.dao.impls.DataAccessManager;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * <p>
 * <code>BranchManager.java</code> contains all the data access layer methods
 * pertaining to BranchDetails model.
 * </p>
 * 
 * <br/>
 * This class extends the <code>DataAccessManager</code> class of our MISP
 * framework. </p>
 * 
 * @see com.mip.framework.dao.impls.DataAccessManager
 * 
 * @author T H B S
 * 
 */
public class BranchManager extends DataAccessManager<BranchDetails, Integer> {

	public BranchManager() {
		super(BranchDetails.class);
	}

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			BranchManager.class);

	/**
	 * Saves a record of type BranchDetails in database.
	 * 
	 * @param branchDetails
	 *            BranchDetails, record to be saved.
	 * @throws DBException
	 */
	public void addBranchDetails(BranchDetails branchDetails)
			throws DBException {

		Object[] params = { branchDetails };
		logger.entering("addBranchDetails", params);

		try {
			getHibernateTemplate().save(branchDetails);

		} catch (DataAccessException e) {
			logger.error("Exception occured while saving Branch Details.", e);
			throw new DBException(e);
		}

		logger.exiting("addBranchDetails");
	}

	/**
	 * Gets a list of active Branches.
	 * 
	 * @return List of Branches
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public List<BranchDetails> getBranchDetailsList() throws DBException {

		logger.entering("getBranchDetailsList");

		List<BranchDetails> branchList = null;
		try {
			branchList = getHibernateTemplate().find(
					"FROM BranchDetails branchDetails "
							+ "WHERE branchDetails.active = 1 ");

		} catch (DataAccessException dae) {
			logger.error("Exception occured while fetching list of all active "
					+ "branches.", dae);
			throw new DBException(dae);
		}

		logger.exiting("getBranchDetailsList");
		return branchList;
	}

	/**
	 * Gets Branch details, based on PK.
	 * 
	 * @param branchId
	 *            Branch Id, Primary Key of the table.
	 * @return BranchDetails, branch record.
	 * @throws DBException
	 */
	public BranchDetails getBranchDetails(Integer branchId) throws DBException {

		Object[] params = { branchId };
		logger.entering("getBranchDetails", params);

		BranchDetails branchDetails = null;
		try {
			branchDetails = super.fetch(branchId);

		} catch (DataAccessException dae) {
			logger.error("Exception occured while fetching Branch details "
					+ "based on Branch Id.", dae);
			throw new DBException(dae);
		}

		logger.exiting("getBranchDetails", branchDetails);
		return branchDetails;
	}

	/**
	 * Update a branch record.
	 * 
	 * @param branchDetails
	 *            BranchDetails, details to be updated.
	 * @throws DBException
	 */
	public void updateBranchDetails(BranchDetails branchDetails)
			throws DBException {

		Object[] params = { branchDetails };
		logger.entering("updateBranchDetails", params);

		try {
			getHibernateTemplate().update(branchDetails);

		} catch (DataAccessException e) {
			logger.error("Exception occured while updating Branch Details.", e);
			throw new DBException(e);
		}

		logger.exiting("updateBranchDetails");
	}

	/**
	 * This method queries the database to check if the input Branch name
	 * already exists.
	 * 
	 * @param branchName
	 *            input Branch Name.
	 * 
	 * @return true if exists and false otherwise.
	 * 
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public boolean checkIfBranchNameExists(String branchName, int branchId)
			throws DBException {

		logger.entering("checkIfBranchNameExists", branchName, branchId);
		int rowCount;
		List<BranchDetails> branchList = null;
		try {
			if (branchId != 0) {
				Object[] params = { branchName, branchId };
				branchList = getHibernateTemplate().find(
					"FROM BranchDetails branchDetails "
							+ " WHERE branchDetails.branchName = ? and "
							+ " branchDetails.branchId <> ? ", params);
			} else{
				branchList = getHibernateTemplate().find(
						"FROM BranchDetails branchDetails "
						+ " WHERE branchDetails.branchName = ? ", branchName);
			}

			rowCount = branchList.size();
		} catch (DataAccessException exception) {
			logger.error("Exception occured while validating Branch name",
					exception);
			throw new DBException(exception);
		}

		boolean returnValue = false;
		if (rowCount > 0){
			returnValue = true;
		}
		logger.exiting("checkIfBranchNameExists",returnValue);
		return returnValue;
	}

	/**
	 * Gets the MAX(BranchId), for generation of Branch Code.
	 * 
	 * @return MAX(BranchId)
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public Integer getMaxBranchIdFromDB() throws DBException {

		logger.entering("getMaxBranchIdFromDB");

		List<Integer> branchIdList = null;
		Integer branchId = 0;
		try {
			branchIdList = getHibernateTemplate().find(
					"SELECT MAX(branchDetails.branchId) "
							+ "FROM BranchDetails branchDetails");

			if (branchIdList != null && branchIdList.size() > 0) {
				branchId = branchIdList.get(0);
			}
		} catch (DataAccessException dae) {
			logger.error("Exception occured while fetching MAX(Branch Id).",
					dae);
			throw new DBException(dae);
		}
		if(branchId == null){
			branchId = 0;
		}
		logger.exiting("getMaxBranchIdFromDB", branchId);
		return branchId;
	}
}
