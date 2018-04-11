package com.mip.application.services;

import java.util.List;

import com.mip.application.dal.managers.BranchManager;
import com.mip.application.dal.managers.UserManager;
import com.mip.application.model.BranchDetails;
import com.mip.application.model.UserDetails;
import com.mip.application.model.mappings.BranchMappings;
import com.mip.application.view.BranchVO;
import com.mip.application.view.mappings.BranchDetailsM2VMappings;
import com.mip.framework.constants.PlatformConstants;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.TypeUtil;

/**
 * <p>
 * <code>BranchService.java</code> contains all the service layer methods 
 * pertaining to Branch Management use case model.
 * </p>
 * 
 * @author T H B S
 */
public class BranchService {
	
	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			BranchService.class);
	
	/**
	 * Set inversion of Control for <code>BranchManager</code> and 
	 * <code>UserManager</code>.
	 */
	private BranchManager branchManager;
	
	private UserManager userManager;
			
	public void setBranchManager(BranchManager branchManager) {
		this.branchManager = branchManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}



	/**
	 * Registers the branch details into database.
	 * 
	 * @param branchVO 
	 * 				BranchVO containing the data to be added into database.
	 * @param loginUserId 
	 * 				contains user id of the logged in user.
	 * @throws MISPException	 
	 */
	
	public String registerBranch(BranchVO branchVO, UserDetails loginUserDetails,
			boolean deleteFlag) throws MISPException{	
			
		Object[] params = {branchVO, ""+loginUserDetails};
		logger.entering("registerBranch", params);	
		
		String branchCode = null;
		
		try{
			/**
			 * TODO : check for synchronizing the actual definition of 
			 * below method. 
			 */
			// Get branchId and auto-increment it
			int branchId = branchManager.getMaxBranchIdFromDB();
			branchId++;		// Set next sequence for Branch Code.
			
			branchCode = (new StringBuilder(
							PlatformConstants.BRANCH_CODE_PREFIX)
							.append(branchId)).toString();
			branchVO.setBranchCode(branchCode);
			
			if(branchVO.getBranchId() != null){
				branchVO.setBranchId(null);
				// This assignment indicates that the operation is INSERT,
				// 	in the method BranchMappings.mapBranchVOToBranchDetailsModel().
			}
			BranchDetails branchDetails = BranchMappings.
					mapBranchVOToBranchDetailsModel(branchVO, loginUserDetails, 
							new BranchDetails(), deleteFlag);	
			
			branchManager.addBranchDetails(branchDetails);
		}
		catch(DBException exception){
			logger.error("An exception occured while registering Branch Details.",
					exception);
			throw new MISPException(exception);
		}
		catch(MISPException exception){
			logger.error("An exception occured while mapping VO to Model.",
					exception);
			throw new MISPException(exception);
		}
		logger.exiting("registerBranch",branchCode);
		return branchCode;
	}
		
	/**
	 * Gets a list of Branch Details
	 * 
	 * @return List of Branch Details
	 * @throws MISPException
	 */
	public List<BranchDetails> listBranchDetails()
				throws MISPException{
	
		logger.entering("listBranchDetails");
		
		List<BranchDetails> branchList = null;
		try{
			branchList = branchManager.getBranchDetailsList();
			
		}catch(DBException exception){
			logger.error("An exception occured while fetching list of " +
					"Branch Details.", exception);
			throw new MISPException(exception);
		}
		
		logger.exiting("listBranchDetails");
		return branchList;
	}
	
	/**
	 * Gets a particular branch's Branch Details.
	 * Maps the table data to an appropriate View Object, and 
	 * sends it back to the controller.
	 * 
	 * @param branchId
	 * 				Branch Id of the user.
	 * @return
	 * 			BranchVO, the view object used for representing branch data in
	 * 			all the use cases of Branch Management use case model. 
	 * @throws MISPException
	 */
	public BranchVO getBranchDetails(String branchId)
			throws MISPException{
			
		Object[] params = {branchId};
		logger.entering("getBranchDetails", params);
		
		BranchVO branchVO = new BranchVO();
						
		try{
			BranchDetails branchDetails = branchManager.getBranchDetails(
					new Integer(branchId));
			
			branchVO = BranchDetailsM2VMappings.
					mapBranchModelToBranchVO(branchDetails);
				
		}catch(DBException exception){
			logger.error("An exception occured while fetching " +
					"Branch Details based on a branch id.", exception);
			throw new MISPException(exception);
		}
		catch(MISPException exception){
			logger.error("An exception occured while mapping Model to VO.",
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("getBranchDetails", branchVO);
		return branchVO; 
	}
		
	/**
	 * Modifies the branch details in the database.
	 * 
	 * @param branchVO 
	 * 			BranchVO containing the data to be modified.
	 * @param loginUserDetails
	 * 			User Details of the logged in user.
	 * @throws MISPException
	 */
	public void modifyBranchDetails(BranchVO branchVO, 
			UserDetails loginUserDetails, boolean deleteFlag) 
			throws MISPException{
			
		Object[] params = {branchVO,loginUserDetails};
		logger.entering("modifyBranchDetails", params);
						
		try{
			BranchDetails branchDetails = branchManager.getBranchDetails(
					TypeUtil.toInt(branchVO.getBranchId()));
			
			//logger.debug("branchDetails : ",branchDetails);
			
			branchDetails = BranchMappings.mapBranchVOToBranchDetailsModel(
					branchVO, loginUserDetails, branchDetails, deleteFlag);
						
			branchManager.updateBranchDetails(branchDetails);
		}catch(DBException exception){
			logger.error("An exception occured while modifying branch details.", 
						exception);
			throw new MISPException(exception);
		}
		catch(MISPException exception){
			logger.error("An exception occured while mapping VO to Model.",
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("modifyBranchDetails");
	}
	
	/**
	 * Invoked as a DWR call, this method checks if the input Branch Name 
	 * already exists in the database.
	 * 
	 * @param branchName input Branch Name.
	 * 
	 * @return true if exists and false otherwise.
	 */
	public boolean checkBranchName(String branchName, String branchID){
		
		Object[] params = {branchName, branchID};
		logger.entering("checkBranchName", params);
		
		boolean doesBranchExists = false;
		try {
			doesBranchExists = branchManager.checkIfBranchNameExists(
						branchName, Integer.valueOf(branchID));
		} catch (DBException exception) {
			logger.error("An error occured while " +
					"validating branch name", exception);
		}
		
		logger.exiting("checkBranchName",doesBranchExists);
		return doesBranchExists;
	}
	
	/**
	 * Invoked as a DWR call, this method checks if the input Branch Name 
	 * already exists in the database.
	 * 
	 * @param branchName input Branch Name.
	 * 
	 * @return the row count.
	 */
	public int checkBeforeBranchDelete(String branchId){
		
		Object[] params = {branchId};
		logger.entering("checkBranchName", params);
		
		int rowCount = 0;
		try {
			BranchDetails branchDetails = new BranchDetails();
			branchDetails.setBranchId(new Integer(branchId));
			
			rowCount = userManager.checkBeforeBranchDelete(branchDetails);
			
		} catch (DBException exception) {
			logger.error("An error occured while " +
					"validating branch name", exception);
		}
		
		logger.exiting("checkBranchName",rowCount);
		return rowCount;
	}
}
