package com.mip.application.model.mappings;

import java.util.HashSet;
import java.util.Set;

import com.mip.application.model.BusinessRuleMaster;
import com.mip.application.model.ProductCoverDetails;
import com.mip.application.model.ProductDetails;
import com.mip.application.view.OfferCreateVO;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.TypeUtil;

/*
 * HISTORY
 * =====================================================================
 * Version | Date        | Who              | Comments
 * ---------------------------------------------------------------------
 * 1.0     | 19/04/2011  | T H B S          |Initial Version.
 * =====================================================================
 */


/**
 * This class contains the methods for mapping OfferVo to OfferDetails Model.
 * 
 * @author THBS
 *
 */
public class OfferMappings {
	
	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			OfferMappings.class);

	public static ProductDetails mapOfferCreateVOToOfferDetailsModel(
			OfferCreateVO offerCreateVO) throws MISPException{
		
		logger.entering("mapOfferCreateVOToOfferDetailsModel", offerCreateVO);

		ProductDetails productDetails = new ProductDetails();
	
		try{
			
			if (offerCreateVO != null) {
				productDetails.setProductName(offerCreateVO.getOfferName());

				/*OfferType offerType = new OfferType();
				offerType.setOfferTypeId(TypeUtil.toInt(offerCreateVO
						.getOfferType()));*/
				/*productDetails.setOfferType(offerType);

				productDetails.setCoverMultiplier(TypeUtil.toInt(offerCreateVO
						.getMultiValue()));*/

				BusinessRuleMaster businessRuleMaster = new BusinessRuleMaster();
				businessRuleMaster.setBrId(TypeUtil.toInt(offerCreateVO
						.getBrId()));
				/*productDetails.setBusinessRuleMaster(businessRuleMaster);*/
				
				Set<ProductCoverDetails> offerCoverList = OfferMappings
						.mapOfferCreateVOToOfferCoverDetailsModel(offerCreateVO,
								productDetails);
				
				productDetails.setProductCoverDetails(offerCoverList);
			}
			
		}catch (Exception exception) {
			logger.error(
					"Exception occurred while Mapping OfferCreateVO to OfferDetails Model.",
					exception);
			
			throw new MISPException("Exception occurred while Mapping OfferCreateVO to OfferDetails Model", 
					exception);
			
		}

		logger.exiting("mapOfferCreateVOToOfferDetailsModel",productDetails);

		return productDetails;
	}

	public static Set<ProductCoverDetails> mapOfferCreateVOToOfferCoverDetailsModel(
			OfferCreateVO offerCreateVO, ProductDetails productDetails) throws MISPException{
		
		logger.entering("mapOfferCreateVOToOfferCoverDetailsModel", offerCreateVO);

		Set<ProductCoverDetails> offerCoverDetailsList = new HashSet<ProductCoverDetails>();
		
		try {
			if (offerCreateVO != null) {

				String[] offeredCover = offerCreateVO.getOfferedCover();

				for (int counter = 0; offeredCover != null
						&& counter < offeredCover.length; counter++) {

					if (offeredCover[counter] != null
							&& offeredCover[counter].trim().length() != 0) {

						ProductCoverDetails productCoverDetails = new ProductCoverDetails();

						productCoverDetails.setProductCover(TypeUtil
								.toFloat(offeredCover[counter]));

						productCoverDetails
								.setCoverCharges(TypeUtil
										.toFloat(offerCreateVO.getPaidAmount()[counter]));

						productCoverDetails.setProductDetails(productDetails);

						offerCoverDetailsList.add(productCoverDetails);

					}

				}
			}

		} catch (Exception exception) {
			logger.error(
				"Exception occurred while Mapping OfferCreateVO to OfferDetails Model.",
				exception);

			throw new MISPException(
					"Exception occurred while Mapping OfferCreateVO to OfferDetails Model",
					exception);
		}

		logger.exiting("mapOfferCreateVOToOfferCoverDetailsModel",offerCoverDetailsList);
		return offerCoverDetailsList;
	}
}
