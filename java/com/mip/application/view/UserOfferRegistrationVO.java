package com.mip.application.view;

import java.io.Serializable;

public class UserOfferRegistrationVO implements Serializable {

	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = -6106636805393992782L;
	private int regXLLaptop;
	private int regXLHandset;
	private int regXLTotal;
	private int confirmXL;
	private int offerRegBy;
	private String offerRegDate;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserOfferRegistrationVO [offerRegBy=");
		builder.append(offerRegBy);
		builder.append(", regXLLaptop=");
		builder.append(regXLLaptop);
		builder.append(", regXLHandset=");
		builder.append(regXLHandset);
		builder.append(", regXLTotal=");
		builder.append(regXLTotal);
		builder.append(", confirmXL=");
		builder.append(confirmXL);
		builder.append(", offerRegDate=");
		builder.append(offerRegDate);
		builder.append("]");
		return builder.toString();
	}
	
	public int getRegXLLaptop() {
		return regXLLaptop;
	}
	public void setRegXLLaptop(int regXLLaptop) {
		this.regXLLaptop = regXLLaptop;
	}
	public int getRegXLHandset() {
		return regXLHandset;
	}
	public void setRegXLHandset(int regXLHandset) {
		this.regXLHandset = regXLHandset;
	}
	public int getRegXLTotal() {
		return regXLTotal;
	}
	public void setRegXLTotal(int regXLTotal) {
		this.regXLTotal = regXLTotal;
	}
	public int getConfirmXL() {
		return confirmXL;
	}
	public void setConfirmXL(int confirmXL) {
		this.confirmXL = confirmXL;
	}
	public int getOfferRegBy() {
		return offerRegBy;
	}
	public void setOfferRegBy(int offerRegBy) {
		this.offerRegBy = offerRegBy;
	}
	public String getOfferRegDate() {
		return offerRegDate;
	}
	public void setOfferRegDate(String offerRegDate) {
		this.offerRegDate = offerRegDate;
	}

	@Override
	public boolean equals(Object obj) {
		UserOfferRegistrationVO offerRegVO = (UserOfferRegistrationVO)obj; 
		if (this.offerRegBy == offerRegVO.getOfferRegBy()
				&& this.offerRegDate.equals(offerRegVO.getOfferRegDate()))
				return true;
		return false;
	}
}
