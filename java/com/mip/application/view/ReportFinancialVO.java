package com.mip.application.view;

import java.io.Serializable;

public class ReportFinancialVO implements Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 1964182354143696154L;

	private String totalActiveCustomers;	
	private String freeSumAssured;	
	private String paidSumAssured;	
	private String totalSumAssured;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReportFinancialVO [totalActiveCustomers=");
		builder.append(totalActiveCustomers);
		builder.append(", freeSumAssured=");
		builder.append(freeSumAssured);
		builder.append(", paidSumAssured=");
		builder.append(paidSumAssured);
		builder.append(", totalSumAssured=");
		builder.append(totalSumAssured);
		builder.append("]");
		return builder.toString();
	}

	public String getTotalActiveCustomers() {
		return totalActiveCustomers;
	}

	public void setTotalActiveCustomers(String totalActiveCustomers) {
		this.totalActiveCustomers = totalActiveCustomers;
	}

	public String getFreeSumAssured() {
		return freeSumAssured;
	}

	public void setFreeSumAssured(String freeSumAssured) {
		this.freeSumAssured = freeSumAssured;
	}

	public String getPaidSumAssured() {
		return paidSumAssured;
	}

	public void setPaidSumAssured(String paidSumAssured) {
		this.paidSumAssured = paidSumAssured;
	}

	public String getTotalSumAssured() {
		return totalSumAssured;
	}

	public void setTotalSumAssured(String totalSumAssured) {
		this.totalSumAssured = totalSumAssured;
	}
}
