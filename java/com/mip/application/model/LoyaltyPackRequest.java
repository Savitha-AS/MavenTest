
package com.mip.application.model;

import java.io.Serializable;

public class LoyaltyPackRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7788799346843703028L;

	private String msisdn;
	private int custId;
	private int snId;
	private int offerId;
	private int offerCoverId;
	private int oldLoyaltyPack;
	private int newLoyaltyPack;
	private int userId;
	private int lcId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}


	public int getCustId() {
		return custId;
	}

	public void setCustId(int custId) {
		this.custId = custId;
	}

	public int getSnId() {
		return snId;
	}

	public void setSnId(int snId) {
		this.snId = snId;
	}

	public int getOfferId() {
		return offerId;
	}

	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}

	public int getOfferCoverId() {
		return offerCoverId;
	}

	public void setOfferCoverId(int offerCoverId) {
		this.offerCoverId = offerCoverId;
	}

	
	public int getOldLoyaltyPack() {
		return oldLoyaltyPack;
	}

	public void setOldLoyaltyPack(int oldLoyaltyPack) {
		this.oldLoyaltyPack = oldLoyaltyPack;
	}

	public int getNewLoyaltyPack() {
		return newLoyaltyPack;
	}

	public void setNewLoyaltyPack(int newLoyaltyPack) {
		this.newLoyaltyPack = newLoyaltyPack;
	}

	public int getLcId() {
		return lcId;
	}

	public void setLcId(int lcId) {
		this.lcId = lcId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LoyaltyPackRequest [msisdn=");
		builder.append(msisdn);
		builder.append(", custId=");
		builder.append(custId);
		builder.append(", snId=");
		builder.append(snId);
		builder.append(", offerId=");
		builder.append(offerId);
		builder.append(", offerCoverId=");
		builder.append(offerCoverId);
		builder.append(", oldLoyaltyPack=");
		builder.append(oldLoyaltyPack);
		builder.append(", newLoyaltyPack=");
		builder.append(newLoyaltyPack);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", lcId=");
		builder.append(lcId);
		builder.append("]");
		return builder.toString();
	}


	

}
