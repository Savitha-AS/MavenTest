package com.mip.application.view;

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
 * This is a VO class to hold the Business Rule details.
 * @author THBS
 *
 */
public class BusinessRuleVO {
	
	/**
	 * <code>int</code> represents the insurance provider.
	 */
	private int insurer;
	
	/**
	 * <code>float</code> represents the premium amount.
	 */
	private float premiumAmtPerc;
	
	/**
	 * <code>String[]</code> represents the business rule start values.
	 */
	private String[] from;	
	
	/**
	 * <code>String[]</code> represents the business rule end values.
	 */
	private String[] to;	
	
	/**
	 * <code>String[]</code> represents the free offered cover.
	 */
	private String[] assuredCover;
	
	/**
     * Returns the insurance provider.
     * @return a <code>int</code> containing the insurance provider.
     */
	public int getInsurer() {
		return insurer;
	}
	
	/**
     * Sets the insurance provider.
     * @param insurer,
     *            a <code>int</code> containing the insurance provider id.
     */
	public void setInsurer(int insurer) {
		this.insurer = insurer;
	}
	
	/**
     * Returns the business rule slab start values.
     * @return a <code>int</code> containing the business rule slab start values.
     */
	public String[] getFrom() {
		return from;
	}
	
	/**
     * Sets the business rule slab start values.
     * @param from,
     *            a <code>String[]</code> business rule slab start values.
     */
	public void setFrom(String[] from) {
		this.from = from;
	}
	
	/**
     * Returns the business rule slab end values.
     * @return a <code>int</code> containing the business rule slab end values.
     */
	public String[] getTo() {
		return to;
	}
	
	/**
     * Sets the business rule slab end values.
     * @param to,
     *            a <code>String[]</code> business rule slab end values.
     */
	public void setTo(String[] to) {
		this.to = to;
	}
	
	/**
     * Returns the business rule freecover values.
     * @return a <code>int</code> containing the business rule freecover values.
     */
	public String[] getAssuredCover() {
		return assuredCover;
	}
	
	/**
     * Sets the business rule free cover values.
     * @param assuredCover,
     *            a <code>String[]</code> business rule free cover values.
     */
	public void setAssuredCover(String[] assuredCover) {
		this.assuredCover = assuredCover;
	}
	
	/**
     * Returns the business rule freecover values.
     * @return a <code>int</code> containing the business rule freecover values.
     */
	public float getPremiumAmtPerc() {
		return premiumAmtPerc;
	}
	
	/**
     * Sets the insurance premium amount.
     * @param premiumAmtPerc,
     *            a <code>float</code> insurance premium amount.
     */
	public void setPremiumAmtPerc(float premiumAmtPerc) {
		this.premiumAmtPerc = premiumAmtPerc;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BusinessRuleVO [insurer=");
		builder.append(insurer);
		builder.append(", premiumAmtPerc=");
		builder.append(premiumAmtPerc);
		builder.append(", from=");
		builder.append(Arrays.toString(from));
		builder.append(", to=");
		builder.append(Arrays.toString(to));
		builder.append(", AssuredCover=");
		builder.append(Arrays.toString(assuredCover));
		builder.append("]");
		return builder.toString();
	}

}
