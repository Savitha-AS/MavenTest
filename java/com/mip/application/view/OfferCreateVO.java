package com.mip.application.view;

import java.io.Serializable;
import java.util.Arrays;

/*
 * HISTORY
 * =====================================================================
 * Version | Date        | Who              | Comments
 * ---------------------------------------------------------------------
 * 1.0     | 19/04/2011  | T H B S          |Initial Version.
 * =====================================================================
 */

/**
 * This is a VO class to hold the Offer details.
 * @author THBS
 *
 */
public class OfferCreateVO implements Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 944666058259722983L;

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
	 * <code>String</code> represents the business rule id.
	 */
	private String brId;	
	
	/**
	 * <code>String[]</code> represents the free cover.
	 */
	private String[] freeCover;	
	
	/**
	 * <code>String[]</code> represents the offered cover.
	 */
	private String[] offeredCover;	
	
	/**
	 * <code>String[]</code> represents the amount to be paid.
	 */
	private String[] paidAmount;
	
	/**
	 * <code>String</code> represents the per day deduction.
	 */
	private String perDayDeduction;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OfferCreateVO [offerName=");
		builder.append(offerName);
		builder.append(", offerType=");
		builder.append(offerType);
		builder.append(", multiValue=");
		builder.append(multiValue);
		builder.append(", brId=");
		builder.append(brId);
		builder.append(", freeCover=");
		builder.append(Arrays.toString(freeCover));
		builder.append(", offeredCover=");
		builder.append(Arrays.toString(offeredCover));
		builder.append(", paidAmount=");
		builder.append(Arrays.toString(paidAmount));
		builder.append(", perDayDeduction=");
		builder.append(perDayDeduction);
		builder.append("]");
		return builder.toString();
	}

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
     * Returns the business rule id.
     * @return a <code>String</code> containing the business rule id.
     */
	public String getBrId() {
		return brId;
	}

	/**
     * Sets the business rule id.
     * @param brId,
     *            a <code>String</code> containing the business rule id.
     */
	public void setBrId(String brId) {
		this.brId = brId;
	}

	/**
     * Returns the freecover values.
     * @return a <code>int</code> containing the freecover values.
     */
	public String[] getFreeCover() {
		return freeCover;
	}

	/**
     * Sets the free cover values.
     * @param freeCover,
     *            a <code>String[]</code>  free cover details.
     */
	public void setFreeCover(String[] freeCover) {
		this.freeCover = freeCover;
	}

	/**
     * Returns the offered cover values.
     * @return a <code>String[]</code> containing the offered cover details.
     */
	public String[] getOfferedCover() {
		return offeredCover;
	}

	/**
     * Sets the offered cover values.
     * @param offeredCover,
     *            a <code>String[]</code>  offered cover details.
     */
	public void setOfferedCover(String[] offeredCover) {
		this.offeredCover = offeredCover;
	}

	/**
     * Returns the amount to be paid values.
     * @return a <code>String[]</code> containing the amount to be paid details.
     */
	public String[] getPaidAmount() {
		return paidAmount;
	}

	/**
     * Sets the amount to be paid values.
     * @param paidAmount,
     *            a <code>String[]</code>  amount to be paid details.
     */
	public void setPaidAmount(String[] paidAmount) {
		this.paidAmount = paidAmount;
	}
	
	/**
     * Returns the per day deduction value.
     * @return a <code>String</code> containing the per day deduction value.
     */
	public String getPerDayDeduction() {
		return perDayDeduction;
	}
	
	/**
     * Sets the per day deduction value.
     * @param perDayDeduction,
     *            a <code>String</code> containing the per day deduction value.
     */
	public void setPerDayDeduction(String perDayDeduction) {
		this.perDayDeduction = perDayDeduction;
	}
}
