package com.mip.application.view;

import java.io.Serializable;
import java.math.BigDecimal;

public class CustomerDeregistrationVO implements Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 1L;

	private Integer custId;
	private String msisdn;
	private String deRegisterOption;
	
	private Integer confirmed;
	private Integer offerId;
	private Integer isOfferSubscribed;
	private BigDecimal deductedOfferAmount;
	
	private Integer networkChurned;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustomerDeregistrationVO [custId=");
		builder.append(custId);
		builder.append(", msisdn=");
		builder.append(msisdn);
		builder.append(", deRegisterOption=");
		builder.append(deRegisterOption);
		builder.append(", confirmed=");
		builder.append(confirmed);
		builder.append(", offerId=");
		builder.append(offerId);
		builder.append(", isOfferSubscribed=");
		builder.append(isOfferSubscribed);
		builder.append(", deductedOfferAmount=");
		builder.append(deductedOfferAmount);
		builder.append(", networkChurned=");
		builder.append(networkChurned);
		builder.append("]");
		return builder.toString();
	}
	
	public Integer getCustId() {
		return custId;
	}
	public void setCustId(Integer custId) {
		this.custId = custId;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getDeRegisterOption() {
		return deRegisterOption;
	}
	public void setDeRegisterOption(String deRegisterOption) {
		this.deRegisterOption = deRegisterOption;
	}
	public Integer getConfirmed() {
		return confirmed;
	}
	public void setConfirmed(Integer confirmed) {
		this.confirmed = confirmed;
	}
	public Integer getOfferId() {
		return offerId;
	}
	public void setOfferId(Integer offerId) {
		this.offerId = offerId;
	}
	public Integer getIsOfferSubscribed() {
		return isOfferSubscribed;
	}
	public void setIsOfferSubscribed(Integer isOfferSubscribed) {
		this.isOfferSubscribed = isOfferSubscribed;
	}
	public BigDecimal getDeductedOfferAmount() {
		return deductedOfferAmount;
	}
	public void setDeductedOfferAmount(BigDecimal deductedOfferAmount) {
		this.deductedOfferAmount = deductedOfferAmount;
	}
	public Integer getNetworkChurned() {
		return networkChurned;
	}
	public void setNetworkChurned(Integer networkChurned) {
		this.networkChurned = networkChurned;
	}
}
