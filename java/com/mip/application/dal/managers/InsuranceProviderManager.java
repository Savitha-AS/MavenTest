package com.mip.application.dal.managers;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.mip.application.model.InsuranceCompany;
import com.mip.application.view.InsuranceCompanyVO;
import com.mip.framework.dao.impls.DataAccessManager;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.TypeUtil;

/**
 * <p>
 * <code>InsuranceProviderManager.java</code> contains all the 
 * data access layer methods pertaining to InsuranceCompany model.
 * </p>
 * 
 * @author T H B S
 * 
 */
public class InsuranceProviderManager extends
		DataAccessManager<InsuranceCompany, Integer> {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			InsuranceProviderManager.class);

	/**
	 * Default constructor of the InsuranceProviderManager class
	 */
	public InsuranceProviderManager() {
		super(InsuranceCompany.class);
	}

	/**
	 * This method inserts the details of Insurance company in the DB.
	 * 
	 * @param insModel
	 * @return boolean: TRUE - If the company details got inserted properly in
	 *         the DB. FALSE - If the company details fails to get inserted in
	 *         the DB.
	 * @throws <code>DBException</code>, if any error occurs.    
	 */
	public boolean addInsuranceProvider(InsuranceCompany insModel)
			throws DBException {
		
		logger.entering("addInsuranceProvider");
		
		try {			
			getHibernateTemplate().saveOrUpdate(insModel);			
		} 
		catch (DataAccessException e) {			
			logger.error("Error occured while adding a new Insurance " +
					"Company details : ", e);
			throw new DBException(e);
		}
		
		logger.exiting("addInsuranceProvider");		
		return true;
	}

	/**
	 * This method updates the details of the insurance company in the DB.
	 * 
	 * @param insModel
	 * @return boolean: TRUE - If the company details got updated properly in
	 *         the DB. FALSE - If the company details fails to get updated in
	 *         the DB.
	 * @throws  <code>DBException</code>, if any error occurs.       
	 */
	public boolean updateInsuranceProviderDetails(
			InsuranceCompany insuranceCompany) throws DBException {
		
		logger.entering("updateInsuranceProviderDetails",insuranceCompany);
		
		boolean isUpdated = false;		
		try {
			getHibernateTemplate().update(insuranceCompany);			
			isUpdated = true;			
		} 
		catch (DataAccessException e) {
			logger.error("Error occured while updating Insurance Provider " +
					"details : ", e);
			throw new DBException(e);
		}
		
		logger.exiting("updateInsuranceProviderDetails");
		return isUpdated;
	}

	/**
	 * This method retrieves the details of the Insurance Company.
	 * 
	 * @param companyId
	 * @return <code>List<InsuranceCompany></code> Insurance
	 *                 company details
	 * @throws <code>DBException</code>, if any error occurs.                 
	 */
	@SuppressWarnings("unchecked")
	public List<InsuranceCompany> getInsuranceProviderDetail(
			String companyId) throws DBException {
		
		logger.entering("getInsuranceProviderDetail",companyId);
		
		List<InsuranceCompany> insuranceCompanyDetails = null;
		try {			
			insuranceCompanyDetails = getHibernateTemplate().find(
					"from InsuranceCompany insMO where insMO.insCompId = ? ",
					TypeUtil.toInt(companyId));			
		} 
		catch (DataAccessException ex) {			
			logger.error("Error occured while retreiving Insurance company " +
					"details : ", ex);
			throw new DBException(ex);
		}
		
		logger.exiting("getInsuranceProviderDetail",insuranceCompanyDetails);		
		return insuranceCompanyDetails;
	}

	/**
	 * This method retrieves the list of the Insurance companies present in the
	 * DB.
	 * 
	 * @return <code>List<InsuranceCompany></code>, Insurance Company list.
	 * @throws <code>DBException</code>, if any error occurs
	 */
	@SuppressWarnings("unchecked")
	public List<InsuranceCompany> listInsuranceProviders()
			throws DBException {		
		logger.entering("listInsuranceProviders");
		
		List<InsuranceCompany> insuranceCompanyList = null;		
		try {					
			insuranceCompanyList = getHibernateTemplate().find(
					"FROM InsuranceCompany insCompany");			
		} 
		catch (DataAccessException ex) {			
			logger.error("Error occured while listing the Insurance " +
					"companies : ", ex);		
			throw new DBException(ex);
		}
		
		logger.exiting("listInsuranceProviders",insuranceCompanyList);		
		return insuranceCompanyList;
	}
	
	/**
	 * This methods queries the database to check is the input Company Name 
	 * is already registered.
	 * 
	 * @param companyName input Insurance Company Name.
	 * 
	 * @return true if exists and false otherwise.
	 * 
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public boolean checkIfInsCompExists(String companyName) 
		throws DBException{
		
		logger.entering("checkIfInsCompExists",companyName);	
		
		boolean isCompExists = false;
		List<InsuranceCompanyVO> insCompList = null;
		try{
			insCompList = getHibernateTemplate().find(
					"FROM InsuranceCompany ic where ic.insCompName = ?",
					companyName);
			
			int rowCount = insCompList.size();
			if (rowCount > 0){
				isCompExists = true;
			}
			else{
				isCompExists = false;
			}
		}
		catch (DataAccessException exception) {	
			logger.error("Exception occured while validating insurance " +
					"company name", exception);
			throw new DBException(exception);
		}
		
		logger.exiting("checkIfInsCompExists",isCompExists);		
		return isCompExists;
	}
}
