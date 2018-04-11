package com.mip.application.view;

import java.io.Serializable;

public class AdminConfigVO implements Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 4166256903602160832L;

	private String defaultPwd;

	private String userLoginPrefix;

	private int pwdHistoryLimit;

	private int maxLoginAttempts;

	private int maxIdleCount;

	private String registerCustomerWSURL;

	private String announcementMessage;

	private int offerSubscriptionLastDay;

	private String msisdnCodes;

	private String offerUnsubscribeWSURL;

	private String removeCustomerRegistrationWSURL;

	private String deregisterCustomerWSURL;

	private String defaultOfferAssigned;

	private String commissionPercent;

	private String changeDeductionModeWSURL;

	private String reactivationCustomerWSURL;

	private String assignOfferWSURL;
	
	private String summaryCustomerDetailsChangesRecordLimit;
	
	private String loyaltyPackWSURL;
	
	private String insMsisdnCode;
	
	

	public String getInsMsisdnCode() {
		return insMsisdnCode;
	}

	public void setInsMsisdnCode(String insMsisdnCode) {
		this.insMsisdnCode = insMsisdnCode;
	}

	public String getAssignOfferWSURL() {
		return assignOfferWSURL;
	}

	public void setAssignOfferWSURL(String assignOfferWSURL) {
		this.assignOfferWSURL = assignOfferWSURL;
	}

	public String getDefaultPwd() {
		return defaultPwd;
	}

	public String getReactivationCustomerWSURL() {
		return reactivationCustomerWSURL;
	}

	public void setReactivationCustomerWSURL(String reactivationCustomerWSURL) {
		this.reactivationCustomerWSURL = reactivationCustomerWSURL;
	}

	public void setDefaultPwd(String defaultPwd) {
		this.defaultPwd = defaultPwd;
	}

	public String getUserLoginPrefix() {
		return userLoginPrefix;
	}

	public void setUserLoginPrefix(String userLoginPrefix) {
		this.userLoginPrefix = userLoginPrefix;
	}

	public int getPwdHistoryLimit() {
		return pwdHistoryLimit;
	}

	public void setPwdHistoryLimit(int pwdHistoryLimit) {
		this.pwdHistoryLimit = pwdHistoryLimit;
	}

	public int getMaxLoginAttempts() {
		return maxLoginAttempts;
	}

	public void setMaxLoginAttempts(int maxLoginAttempts) {
		this.maxLoginAttempts = maxLoginAttempts;
	}

	public int getMaxIdleCount() {
		return maxIdleCount;
	}

	public void setMaxIdleCount(int maxIdleCount) {
		this.maxIdleCount = maxIdleCount;
	}

	public String getRegisterCustomerWSURL() {
		return registerCustomerWSURL;
	}

	public void setRegisterCustomerWSURL(String registerCustomerWSURL) {
		this.registerCustomerWSURL = registerCustomerWSURL;
	}

	public String getAnnouncementMessage() {
		return announcementMessage;
	}

	public void setAnnouncementMessage(String announcementMessage) {
		this.announcementMessage = announcementMessage;
	}

	public int getOfferSubscriptionLastDay() {
		return offerSubscriptionLastDay;
	}

	public void setOfferSubscriptionLastDay(int offerSubscriptionLastDay) {
		this.offerSubscriptionLastDay = offerSubscriptionLastDay;
	}

	public String getMsisdnCodes() {
		return msisdnCodes;
	}

	public void setMsisdnCodes(String msisdnCodes) {
		this.msisdnCodes = msisdnCodes;
	}

	/**
	 * @return the offerUnsubscribeWSURL
	 */
	public String getOfferUnsubscribeWSURL() {
		return offerUnsubscribeWSURL;
	}

	/**
	 * @param offerUnsubscribeWSURL
	 *            the offerUnsubscribeWSURL to set
	 */
	public void setOfferUnsubscribeWSURL(String offerUnsubscribeWSURL) {
		this.offerUnsubscribeWSURL = offerUnsubscribeWSURL;
	}

	/**
	 * @return the removeCustomerRegistrationWSURL
	 */
	public String getRemoveCustomerRegistrationWSURL() {
		return removeCustomerRegistrationWSURL;
	}

	/**
	 * @param removeCustomerRegistrationWSURL
	 *            the removeCustomerRegistrationWSURL to set
	 */
	public void setRemoveCustomerRegistrationWSURL(
			String removeCustomerRegistrationWSURL) {
		this.removeCustomerRegistrationWSURL = removeCustomerRegistrationWSURL;
	}

	public String getDefaultOfferAssigned() {
		return defaultOfferAssigned;
	}

	public void setDefaultOfferAssigned(String defaultOfferAssigned) {
		this.defaultOfferAssigned = defaultOfferAssigned;
	}

	public String getCommissionPercent() {
		return commissionPercent;
	}

	public void setCommissionPercent(String commissionPercent) {
		this.commissionPercent = commissionPercent;
	}

	/**
	 * @return the deregisterCustomerWSURL
	 */
	public String getDeregisterCustomerWSURL() {
		return deregisterCustomerWSURL;
	}

	/**
	 * @param deregisterCustomerWSURL
	 *            the deregisterCustomerWSURL to set
	 */
	public void setDeregisterCustomerWSURL(String deregisterCustomerWSURL) {
		this.deregisterCustomerWSURL = deregisterCustomerWSURL;
	}

	public String getChangeDeductionModeWSURL() {
		return changeDeductionModeWSURL;
	}

	public void setChangeDeductionModeWSURL(String changeDeductionModeWSURL) {
		this.changeDeductionModeWSURL = changeDeductionModeWSURL;
	}

	public String getSummaryCustomerDetailsChangesRecordLimit() {
		return summaryCustomerDetailsChangesRecordLimit;
	}

	public void setSummaryCustomerDetailsChangesRecordLimit(
			String summaryCustomerDetailsChangesRecordLimit) {
		this.summaryCustomerDetailsChangesRecordLimit = summaryCustomerDetailsChangesRecordLimit;
	}

	public String getLoyaltyPackWSURL() {
		return loyaltyPackWSURL;
	}

	public void setLoyaltyPackWSURL(String loyaltyPackWSURL) {
		this.loyaltyPackWSURL = loyaltyPackWSURL;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AdminConfigVO [defaultPwd=");
		builder.append(defaultPwd);
		builder.append(", userLoginPrefix=");
		builder.append(userLoginPrefix);
		builder.append(", pwdHistoryLimit=");
		builder.append(pwdHistoryLimit);
		builder.append(", maxLoginAttempts=");
		builder.append(maxLoginAttempts);
		builder.append(", maxIdleCount=");
		builder.append(maxIdleCount);
		builder.append(", registerCustomerWSURL=");
		builder.append(registerCustomerWSURL);
		builder.append(", announcementMessage=");
		builder.append(announcementMessage);
		builder.append(", offerSubscriptionLastDay=");
		builder.append(offerSubscriptionLastDay);
		builder.append(", msisdnCodes=");
		builder.append(msisdnCodes);
		builder.append(", offerUnsubscribeWSURL=");
		builder.append(offerUnsubscribeWSURL);
		builder.append(", removeCustomerRegistrationWSURL=");
		builder.append(removeCustomerRegistrationWSURL);
		builder.append(", deregisterCustomerWSURL=");
		builder.append(deregisterCustomerWSURL);
		builder.append(", defaultOfferAssigned=");
		builder.append(defaultOfferAssigned);
		builder.append(", commissionPercent=");
		builder.append(commissionPercent);
		builder.append(", changeDeductionModeWSURL=");
		builder.append(changeDeductionModeWSURL);
		builder.append(", reactivationCustomerWSURL=");
		builder.append(reactivationCustomerWSURL);
		builder.append(", assignOfferWSURL=");
		builder.append(assignOfferWSURL);
		builder.append(", summaryCustomerDetailsChangesRecordLimit=");
		builder.append(summaryCustomerDetailsChangesRecordLimit);
		builder.append(", loyaltyPackWSURL=");
		builder.append(loyaltyPackWSURL);
		builder.append(", insMsisdnCode=");
		builder.append(insMsisdnCode);
		builder.append("]");
		return builder.toString();
	}

	

}
