package com.mip.application.view;

import java.io.Serializable;

public class ReportRevenueVO implements Serializable {

	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = -8002570257081989939L;

	private String freemiumRevenue;
	private String totalRevenue;
	private String commissionCost;
	private String freePremiumCost;
	private String freemiumPremiumCost;
	private String totalPremiumCost;
	private String totalCost;
	private String profit;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReportRevenueVO [freemiumRevenue=");
		builder.append(freemiumRevenue);
		builder.append(", totalRevenue=");
		builder.append(totalRevenue);
		builder.append(", commissionCost=");
		builder.append(commissionCost);
		builder.append(", freePremiumCost=");
		builder.append(freePremiumCost);
		builder.append(", freemiumPremiumCost=");
		builder.append(freemiumPremiumCost);
		builder.append(", totalPremiumCost=");
		builder.append(totalPremiumCost);
		builder.append(", totalCost=");
		builder.append(totalCost);
		builder.append(", profit=");
		builder.append(profit);
		builder.append("]");
		
		return builder.toString();
	}
	
	public String getFreemiumRevenue() {
		return freemiumRevenue;
	}
	public void setFreemiumRevenue(String freemiumRevenue) {
		this.freemiumRevenue = freemiumRevenue;
	}
	public String getTotalRevenue() {
		return totalRevenue;
	}
	public void setTotalRevenue(String totalRevenue) {
		this.totalRevenue = totalRevenue;
	}
	public String getCommissionCost() {
		return commissionCost;
	}
	public void setCommissionCost(String commissionCost) {
		this.commissionCost = commissionCost;
	}
	public String getFreePremiumCost() {
		return freePremiumCost;
	}
	public void setFreePremiumCost(String freePremiumCost) {
		this.freePremiumCost = freePremiumCost;
	}
	public String getFreemiumPremiumCost() {
		return freemiumPremiumCost;
	}
	public void setFreemiumPremiumCost(String freemiumPremiumCost) {
		this.freemiumPremiumCost = freemiumPremiumCost;
	}
	public String getTotalPremiumCost() {
		return totalPremiumCost;
	}
	public void setTotalPremiumCost(String totalPremiumCost) {
		this.totalPremiumCost = totalPremiumCost;
	}
	public String getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}
	public String getProfit() {
		return profit;
	}
	public void setProfit(String profit) {
		this.profit = profit;
	}

}
