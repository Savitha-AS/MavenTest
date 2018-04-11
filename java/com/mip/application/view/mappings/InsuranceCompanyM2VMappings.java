package com.mip.application.view.mappings;

import com.mip.application.model.InsuranceCompany;
import com.mip.application.model.mappings.InsuranceCompanyMappings;
import com.mip.application.view.InsuranceCompanyVO;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.DateUtil;

/*
 * HISTORY
 * =====================================================================
 * Version | Date        | Who              | Comments
 * ---------------------------------------------------------------------
 * 1.0     | 01/05/2011  | T H B S          |Initial Version.
 * =====================================================================
 */

/**
 * <p>
 * <code>InsuranceCompanyM2VMappings.java</code> contains the mapping of the MO
 * objects to the VO objects.
 * </p>
 * 
 * @author T H B S
 * 
 */

public class InsuranceCompanyM2VMappings {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			InsuranceCompanyMappings.class);

	/**
	 * This method maps a insurance company Model to a insurance company VO.
	 * @param insuranceCompany - instance of InsuranceCompany class
	 * @return insuranceCompanyVO - instance of InsuranceCompanyVO class
	 * @throws MISPException
	 */
	public static InsuranceCompanyVO mapInsCompanyModelToInsCompanyVO(InsuranceCompany insuranceCompany) throws MISPException 
	{
		Object[] params = { insuranceCompany };
		
		logger.entering("mapInsCompanyModelToInsCompanyVO", params);

		InsuranceCompanyVO insuranceCompanyVO = null;

		try 
		{
			insuranceCompanyVO =  new InsuranceCompanyVO();
			
			if (insuranceCompany != null) 
			{
				insuranceCompanyVO.setCompanyId(insuranceCompany.getInsCompId()+"");
				
				insuranceCompanyVO.setCompanyName(insuranceCompany.getInsCompName());
				
				insuranceCompanyVO.setCompanyNumber(insuranceCompany.getInsCompPhone());
				
				insuranceCompanyVO.setBranchName(insuranceCompany.getInsCompBranchName());
				
				insuranceCompanyVO.setAddressLine1(insuranceCompany.getInsCompAddrs1());
				
				insuranceCompanyVO.setAddressLine2(insuranceCompany.getInsCompAddrs2());
				
				insuranceCompanyVO.setCity(insuranceCompany.getInsCompCity());
				
				insuranceCompanyVO.setState(insuranceCompany.getInsCompState());
				
				insuranceCompanyVO.setCountry(insuranceCompany.getInsCompCountry());
				
				insuranceCompanyVO.setZipCode(""+ insuranceCompany.getInsCompZip());
				
				insuranceCompanyVO.setPrimaryContact(insuranceCompany.getInsCompPocName());
				
				insuranceCompanyVO.setPrimaryContactMobile(insuranceCompany.getInsCompPocMsisdn());
				
				insuranceCompanyVO.setCreatedBy(insuranceCompany.getCreatedBy().getFname()+ " " + insuranceCompany.getCreatedBy().getSname());
				
				insuranceCompanyVO.setCreatedDate(DateUtil.toDateString(insuranceCompany.getCreatedDate()));
				
				insuranceCompanyVO.setModifiedBy(insuranceCompany.getModifiedBy().getFname()+ " " + insuranceCompany.getModifiedBy().getSname());
				
				insuranceCompanyVO.setModifiedDate(DateUtil.toDateString(insuranceCompany.getModifiedDate()));
			}
		} 
		catch (Exception e) 
		{
			logger.error("An exception has occured while populating a " + "InsuranceCompanyVO  from a insuranceCompany.", e);
			
			throw new MISPException(e);
		}
		
		logger.exiting("mapInsCompanyModelToInsCompanyVO", insuranceCompanyVO);

		return insuranceCompanyVO;
	}

}
