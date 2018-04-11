package com.mip.application.view;

import java.util.List;

import com.mip.application.model.BusinessRuleDefinition;

/*
 * HISTORY
 * =====================================================================
 * Version | Date        | Who              | Comments
 * ---------------------------------------------------------------------
 * 1.0     | 19/04/2011  | T H B S          |Initial Version.
 * =====================================================================
 */

/**
 * This is a VO class to hold the Business Rule and Offer details.
 * @author THBS
 *
 */
public class BusinessRuleOfferVO {
	
	/**
	 * <code>int</code> represents the offerid.
	 */
	private int offerId;
	
	/**
	 * <code>String</code> represents the offer name.
	 */
	private String offerName;	
	
	/**
	 * <code>String</code> represents the offer type.
	 */
	private String offerType;	
	
	/**
	 * <code>String</code> represents the multiple value.
	 */
	private String multiValue;	
	
	/**
	 * <code>List</code> represents the business rule defintions.
	 */
	List<BusinessRuleDefinition> busRuleDefList;

	/**
     * Returns the offer name.
     * @return a <code>String</code> containing the offer name.
     */
	public String getOfferName() {
		return offerName;
	}

	/**
     * Sets the offer name.
     * @param offerName,
     *            a <code>String</code> containing the offer name.
     */
	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}

	/**
     * Returns the offer type.
     * @return a <code>String</code> containing the offer type.
     */
	public String getOfferType() {
		return offerType;
	}

	/**
     * Sets the offer type.
     * @param offerType,
     *            a <code>String</code> containing the offer type.
     */
	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}

	/**
     * Returns the multiple value.
     * @return a <code>String</code> containing the multiple value.
     */
	public String getMultiValue() {
		return multiValue;
	}

	/**
     * Sets the multiple value.
     * @param multiValue,
     *            a <code>String</code> containing the multiple value.
     */
	public void setMultiValue(String multiValue) {
		this.multiValue = multiValue;
	}

	/**
     * Returns the business rule defintions.
     * @return a <code>List</code> containing the business rule definitons.
     */
	public List<BusinessRuleDefinition> getBusRuleDefList() {
		return busRuleDefList;
	}

	/**
     * Sets the business rule defintions.
     * @param busRuleDefList,
     *            a <code>List</code> containing the business rule definitons.
     */
	public void setBusRuleDefList(List<BusinessRuleDefinition> busRuleDefList) {
		this.busRuleDefList = busRuleDefList;
	}
	
	/**
     * Returns the offer id.
     * @return a <code>int</code> containing the offer id.
     */
	public int getOfferId() {
		return offerId;
	}

	/**
     * Sets the offer id.
     * @param offerId,
     *            a <code>int</code> containing the offer id.
     */
	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BusinessRuleOfferVO [offerId = ");
		builder.append(offerId).append(",offerName=");
		builder.append(offerName);
		builder.append(", offerType=");
		builder.append(offerType);
		builder.append(", multiValue=");
		builder.append(multiValue);
		builder.append(", busRuleDefList");
		builder.append(busRuleDefList);
		builder.append("]");
		return builder.toString();
	}
}
