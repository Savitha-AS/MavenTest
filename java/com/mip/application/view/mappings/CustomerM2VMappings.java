package com.mip.application.view.mappings;

import java.util.ArrayList;
import java.util.List;

import sun.nio.cs.HistoricallyNamedCharset;

import com.mip.application.model.CoverHistory;
import com.mip.application.model.CustomerDetails;
import com.mip.application.model.InsuredRelativeDetails;
import com.mip.application.model.UserDetails;
import com.mip.application.view.CustomerVO;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.DateUtil;

/*
 * HISTORY
 * =====================================================================
 * Version | Date        | Who              | Comments
 * ---------------------------------------------------------------------
 * 1.0     | 29/04/2011  | T H B S          |Initial Version.
 * =====================================================================
 */

/**
 * <p>
 * <code>CustomerM2VMappings.java</code> contains all the methods pertaining to
 * Customer Management use case model. This is a mapping class for all customer
 * management modules. The methods is used to convert a DAL Object to a VO
 * Object.
 * </p>
 * 
 * @author T H B S
 * 
 */
public class CustomerM2VMappings {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			CustomerM2VMappings.class);

	/**
	 * This method is used to map a Model/ DAL Object to VO Object for Modify
	 * operation
	 * 
	 * @param customerDetails
	 *            An instance of CusotmerDetails containing customer details
	 * @param insuredRelativeDetails
	 *            An instance of InsuredRelativeDetails containing Insured
	 *            relative details
	 * @param userDetails
	 *            An instance of UserDetails containing user details
	 * @return <code>CustomerVO</code> Object of the type CustomerVO
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public static CustomerVO mapCustModelToCustVOForModify(
			CustomerDetails customerDetails,
			List<InsuredRelativeDetails> insRelativeDetailsList,
			UserDetails userDetails) throws MISPException {
		logger.entering("mapCustModelToCustVOForModify");
		CustomerVO customerVO = new CustomerVO();

		try {
			if (customerDetails != null) {
				customerVO.setFname(customerDetails.getFname());
				customerVO.setSname(customerDetails.getSname());
				customerVO.setMsisdn(customerDetails.getMsisdn());
				customerVO.setAge(customerDetails.getAge() + "");
				customerVO.setImpliedAge(customerDetails.getImpliedAge() + "");
				customerVO
						.setCustId(String.valueOf(customerDetails.getCustId()));
				customerVO.setDob(DateUtil.toDateString(customerDetails
						.getDob()));
				customerVO.setGender(customerDetails.getGender());
				customerVO.setDeductionMode(customerDetails.getDeductionMode()+"");
				if (null != insRelativeDetailsList) {
					for(int i=0; i<insRelativeDetailsList.size();i++)
					{
					if(insRelativeDetailsList.get(i).getOfferId()==4)
					{
						customerVO.setIpNomFirstName(insRelativeDetailsList.get(i).getFname());
						customerVO.setIpNomSurName(insRelativeDetailsList.get(i).getSname());
						customerVO.setIpNomAge(insRelativeDetailsList.get(i).getAge()+ "");
						customerVO.setIpInsMsisdn(insRelativeDetailsList.get(i).getInsMsisdn());
					}
					else if(insRelativeDetailsList.get(i).getOfferId()!=4)
					{
					customerVO.setInsRelAge(insRelativeDetailsList.get(i).getAge()
							+ "");
					customerVO.setInsRelation(insRelativeDetailsList.get(i)
							.getCustRelationship());
					customerVO.setInsRelIrDoB(DateUtil
							.toDateString(insRelativeDetailsList.get(i).getDob()));
					customerVO
							.setInsRelFname(insRelativeDetailsList.get(i).getFname());
					customerVO.setInsRelSurname(insRelativeDetailsList.get(i)
							.getSname());
					customerVO.setInsId(insRelativeDetailsList.get(i).getInsId() + "");
					customerVO.setInsMsisdn(insRelativeDetailsList.get(i).getInsMsisdn());
					
					}
					}
					/**/
				}
				
			}
		} catch (Exception e) {
			logger.error("An exception has occured while populating a "
					+ "CustomerVO from a customerDetails.", e);
			throw new MISPException(e);
		}

		// logger.exiting("mapCustModelToCustVOForModify", customerVO);
		return customerVO;
	}
	
	
	
	

	/**
	 * This method is used to map a DAL Object to a VO Object for search
	 * operation
	 * 
	 * @param customerDetails
	 *            An instance of CusotmerDetails containing customer details
	 * @param userDetails
	 *            An instance of UserDetails containing user details
	 * @return <code>CustomerVO</code> Object of type CustomerVO
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public static CustomerVO mapCustModelToCustVOForSearch(
			CustomerDetails customerDetails, UserDetails userDetails)
			throws MISPException {

		// Object[] params = { customerDetails, userDetails };
		// logger.entering("mapCustModelToCustVOForSearch", params);

		CustomerVO customerVO = new CustomerVO();

		try {
			if (customerDetails != null) {
				customerVO.setFname(customerDetails.getFname());
				customerVO.setSname(customerDetails.getSname());
				customerVO.setMsisdn(customerDetails.getMsisdn());
				customerVO.setModifiedDate(DateUtil
						.toDateString(customerDetails.getModifiedDate()));
				customerVO.setName(customerDetails.getFname() + " "
						+ customerDetails.getSname());
				customerVO.setAge(customerDetails.getAge() + "");
				// customerVO.setStatus(customerDetails.getConfirmed()+ "");
				// customerVO.setCustId(customerDetails.getCustId());
				customerVO.setDob(DateUtil.toDateString(customerDetails
						.getDob()));
				customerVO.setGender(customerDetails.getGender());

				String createdBy = String.valueOf(customerDetails
						.getModifiedBy().getFname()
						+ " "
						+ customerDetails.getModifiedBy().getSname());

				customerVO.setModifiedBy(createdBy);

				customerVO.setModifiedDateStr(customerDetails
						.getModifiedDateStr());
				/*
				 * customerVO.setConfirmed(
				 * String.valueOf(customerDetails.getConfirmed()));
				 */
			}
		} catch (Exception e) {
			logger.error("An exception has occured while populating a "
					+ "CustomerVO from a customerDetails.", e);
			throw new MISPException(e);
		}
		// logger.exiting("mapCustModelToCustVOForSearch", customerVO);
		return customerVO;
	}

	public List<CustomerVO> mapCoverModelToCustomerVO(
			List<CoverHistory> coverHistory) {

		logger.entering("mapCoverModelToCustomerVO", coverHistory);

		List<CustomerVO> customerDetails = new ArrayList<CustomerVO>();
		CustomerVO customerVO = null;

		for (CoverHistory coverHistory1 : coverHistory) {

			customerVO = new CustomerVO();
			customerVO.setCustId(String.valueOf(coverHistory1.getCustId()));
			customerVO.setMsisdn(coverHistory1.getMonth());

			if (coverHistory1.getPrevMonthUsage() != null
					&& coverHistory1.getCoverFree() != null) {
				customerVO.setPrevMonthUsage(String.valueOf(coverHistory1
						.getPrevMonthUsage()));
				customerVO.setCoverFree(String.valueOf(coverHistory1
						.getCoverFree()));
			}
			
			if(coverHistory1.getCoverXL()!=null && coverHistory1.getChargesXL()!=null)
			{
			customerVO.setCoverXL(String.valueOf(coverHistory1.getCoverXL()));
			customerVO
					.setChargesXL(String.valueOf(coverHistory1.getChargesXL()));

			}
			
			if(coverHistory1.getCoverHP()!=null && coverHistory1.getChargesHP()!=null)
			{
			customerVO.setCoverHP(String.valueOf(coverHistory1.getCoverHP()));
			customerVO
					.setChargesHP(String.valueOf(coverHistory1.getChargesHP()));
			}
			customerVO.setMonth(coverHistory1.getMonth() + "-"
					+ String.valueOf(coverHistory1.getYear()));
			customerVO.setYear(String.valueOf(coverHistory1.getYear()));

			customerVO.setCreatedDate(String.valueOf(coverHistory1
					.getCreatedDate()));

			customerDetails.add(customerVO);

		}

		logger.exiting("mapCoverModelToCustomerVO", customerDetails);
		return customerDetails;
	}

}
