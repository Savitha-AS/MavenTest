package com.mip.application.view;

import java.io.Serializable;

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
public class OfferMgmtVO implements Serializable{

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 807360179701655579L;
	
	/**
	 * <code>String</code> represents the selected radio option.
	 */
	private String selectedRadio;
	
	/**
	 * <code>int</code> represents the offer id.
	 */
	private int products;	
	
	/**
	 * <code>String</code> represents the customers count.
	 */
	private String customers;	
	
	/**
	 * <code>String</code> represents the start date.
	 */
	private String fromDate;	

	/**
	 * <code>String</code> represents the end date.
	 */
	private String toDate;
	
	/**
	 * <code>String</code> represents the comma separated MSISDN.
	 */
	private String msisdnCSV;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OfferMgmtVO [products=");
		builder.append(products);
		builder.append(", customers=");
		builder.append(customers);
		builder.append(", fromDate=");
		builder.append(fromDate);
		builder.append(", toDate=");
		builder.append(toDate);
		builder.append(", msisdnCSV=");
		builder.append(msisdnCSV);
		builder.append(", selectedRadio=");
		builder.append(selectedRadio);		
		builder.append("]");
		return builder.toString();
	}

	/**
	 * Returns the selected value of radio button.
	 * @return a <code>String</code> containing the selected value of radio button.
	 */
	public String getSelectedRadio() {
		return selectedRadio;
	}

	/**
	 * Sets the selectedRadio.
	 * @param selectedRadio 
	 * 				, a <code>String</code> containing the selected value of radio button.
	 */
	public void setSelectedRadio(String selectedRadio) {
		this.selectedRadio = selectedRadio;
	}
	
	/**
     * Returns the offer id.
     * @return a <code>int</code> containing the offer id.
     */
	public int getProducts() {
		return products;
	}

	/**
     * Sets the offer id.
     * @param offers,
     *            a <code>String</code> containing the offer id.
     */
	public void setProductss(int products) {
		this.products = products;
	}

	/**
     * Returns the customers count.
     * @return a <code>String</code> containing the customers count.
     */
	public String getCustomers() {
		return customers;
	}
	/**
     * Sets the customers count.
     * @param customers,
     *            a <code>String</code> containing the customers count.
     */
	public void setCustomers(String customers) {
		this.customers = customers;
	}

	/**
     * Returns the start date.
     * @return a <code>String</code> containing the start date.
     */
	public String getFromDate() {
		return fromDate;
	}

	/**
     * Sets the start date.
     * @param fromDate,
     *            a <code>String</code> containing the start date.
     */
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	/**
     * Returns the end date.
     * @return a <code>String</code> containing the end date.
     */
	public String getToDate() {
		return toDate;
	}

	/**
     * Sets the end date.
     * @param toDate,
     *            a <code>String</code> containing the end date.
     */
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
	/**
     * Returns the MSISDN comma separated values.
     * @return a <code>String</code> containing the comma separated MSISDN.
     */
	public String getMsisdnCSV() {
		return msisdnCSV;
	}
	
	/**
     * Sets the MSISDN comma separated values.
     * @param msisdnCSV ,
     *            a <code>String</code> containing the comma separated MSISDN.
     */
	public void setMsisdnCSV(String msisdnCSV) {
		this.msisdnCSV = msisdnCSV;
	}
}
