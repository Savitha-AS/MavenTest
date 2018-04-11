package com.mip.application.services;

import java.util.Iterator;
import java.util.List;

import com.mip.application.dal.managers.InsuranceProviderManager;
import com.mip.application.model.InsuranceCompany;
import com.mip.application.model.UserDetails;
import com.mip.application.model.mappings.InsuranceCompanyMappings;
import com.mip.application.view.InsuranceCompanyVO;
import com.mip.application.view.mappings.InsuranceCompanyM2VMappings;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * <p>
 * <code>InsuranceProviderService.java</code> contains all the service layer
 * methods pertaining to Insurance Company Management use case model.
 * </p>
 * 
 * @author T H B S
 * 
 */
public class InsuranceProviderService {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			InsuranceProviderService.class);
	
	/**
	 * Set inversion of Control for <code>InsuranceProviderManager</code>.
	 */
	private InsuranceProviderManager insuranceManager;

	public void setInsuranceManager(InsuranceProviderManager insuranceManager) {
		this.insuranceManager = insuranceManager;
	}
	
	/**
	 * This method registers the Company details in the DB.
	 * 
	 * @param icObj, Insurance Company VO
	 * @param userDetails
	 * @return boolean: TRUE - If the company details got inserted properly in
	 *         the DB. FALSE - If the company details fails to get inserted in
	 *         the DB.
	 * @throws <code>MISPException</code>, if any error occurs.        
	 */
	public boolean registerInsuranceProvider(
			InsuranceCompanyVO insuranceCompanyVO,
			UserDetails userDetails) throws MISPException{

		logger.entering("registerInsuranceProvider", insuranceCompanyVO);

		InsuranceCompany insuranceCompany = null;		
		boolean isDataAdded = false;		
		try{
			// Map the VO objects to the MO objects
			insuranceCompany = InsuranceCompanyMappings.
				mapInsCompanyVOToInsCompanyModel(insuranceCompanyVO,
						userDetails);

			isDataAdded = insuranceManager.addInsuranceProvider(
					insuranceCompany);
			
		} catch (DBException exception) {
			logger.error(
				"An exception occured while registering the Insurance Company.",
				exception);
			throw new MISPException(exception);
		}
		
		logger.exiting("registerInsuranceProvider", isDataAdded);		
		return isDataAdded;
	}

	/**
	 * This method updates the Company details saved in the DB.
	 * 
	 * @param icObj, Insurance Company VO
	 * @param userDetails
	 * @param companyId, company id
	 * @return boolean: TRUE - If the company details got updated properly in
	 *         the DB. FALSE - If the company details fails to get updated in
	 *         the DB.
	 * @throws <code>MISPException</code>, if any error occurs.        
	 */
	public boolean modifyInsuranceProviderDetails(
			InsuranceCompanyVO insuranceCompanyVO, UserDetails userDetails) 
			throws MISPException{

		Object[] params = {insuranceCompanyVO, userDetails};
		logger.entering("modifyInsuranceProviderDetails",params);
		
		boolean isDataModified = false;		
		try {
			InsuranceCompany insuranceCompany = this.insuranceManager.fetch(
					new Integer(insuranceCompanyVO.getCompanyId()));
			
			insuranceCompany = InsuranceCompanyMappings.
				mapToInsCompanyModelToModify(insuranceCompany, 
						insuranceCompanyVO, userDetails);
			
			isDataModified = insuranceManager.updateInsuranceProviderDetails(
					insuranceCompany);			
		} 
		catch (DBException dbException){			
			logger.error("DBException occured while modifying the Insurance " +
					"Company details : ", dbException);			
			throw new MISPException(dbException);			
		}
		
		logger.exiting("modifyInsuranceProviderDetails", isDataModified);		
		return isDataModified;
	}

	/**
	 * This method retrieves the list of the Insurance companies present in the
	 * DB.
	 * 
	 * @return <code>ArrayList<InsuranceCompany></code> ArrayList object
	 * @throws <code>MISPException</code>, if any error occurs
	 */
	public List<InsuranceCompany> listInsuranceProviders() throws MISPException{
		
		logger.entering("listInsuranceProviders");
		
		List<InsuranceCompany> insuranceCompanyList = null;		
		try{
			insuranceCompanyList = insuranceManager.listInsuranceProviders();		
	    } 
		catch (DBException e) {			
			logger.error("An exception occured while listing the " +
					"Insurance Companies : ", e);
			throw new MISPException(e);
	    }
		logger.exiting("listInsuranceProviders");		
		return insuranceCompanyList;
	}

	/**
	 * This method retrieves the details of the Insurance Company.
	 * 
	 * @param companyId, company id
	 * @return InsuranceCompanyVO, view object
	 * @throws <code>MISPException</code>, if any error occurs
	 */
	public InsuranceCompanyVO getInsuranceProviderDetail(String companyId) 
		throws MISPException{
		
		logger.entering("getInsuranceProviderDetail",companyId);
		
		InsuranceCompanyVO insuranceCompanyVO = null;
		try {
			List<InsuranceCompany> insuranceCompanyDetails = 
				insuranceManager.getInsuranceProviderDetail(companyId);
			
			Iterator<InsuranceCompany> itr = insuranceCompanyDetails.iterator();
			
			while (itr.hasNext()){				
				InsuranceCompany insuranceCompany = itr.next();				
				insuranceCompanyVO = InsuranceCompanyM2VMappings.
					mapInsCompanyModelToInsCompanyVO(insuranceCompany);
			}
		} 
		catch (DBException e){
			logger.error("An exception occured while retrieving the Insurance "+
					"company details : ", e);			
			throw new MISPException(e);
		}
		
		logger.exiting("getInsuranceProviderDetail",insuranceCompanyVO);		
		return insuranceCompanyVO;
	}
	
	/**
	 * Invoked as a DWR call, this method checks if the input Company 
	 * name already exists in the database.
	 * 
	 * @param companyName input Insurance Company Name.
	 * 
	 * @return true if exists and false otherwise.
	 */
	public boolean checkIfInsCompExists(String companyName){
		
		logger.entering("checkIfInsCompExists",companyName);
		
		boolean isInsCompExisting = false;		
		try{
			isInsCompExisting = insuranceManager.checkIfInsCompExists(
					companyName);
		}		
		catch (DBException exception) {
			logger.error("An error occured while validating insurance " +
					"company name", exception);
		}

		logger.exiting("checkIfInsCompExists",isInsCompExisting);	
		return isInsCompExisting;
	}

}