package com.mip.application.view;

import java.io.Serializable;

public class ReportWeeklyVO implements Serializable {

	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = 5269959025474500643L;

	private String fromDate;
	private String toDate;
	private String refDate;
	private String registrationType;
	private String targetEndDateReg;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReportWeeklyVO [fromDate=");
		builder.append(fromDate);
		builder.append(", toDate=");
		builder.append(toDate);
		builder.append(", refDate=");
		builder.append(refDate);
		builder.append(", registrationType=");
		builder.append(registrationType);
		builder.append(", targetEndDateReg=");
		builder.append(targetEndDateReg);
		builder.append("]");
		return builder.toString();
	}
	
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getRefDate() {
		return refDate;
	}
	public void setRefDate(String refDate) {
		this.refDate = refDate;
	}
	public String getRegistrationType() {
		return registrationType;
	}
	public void setRegistrationType(String registrationType) {
		this.registrationType = registrationType;
	}
	public String getTargetEndDateReg() {
		return targetEndDateReg;
	}
	public void setTargetEndDateReg(String targetEndDateReg) {
		this.targetEndDateReg = targetEndDateReg;
	}
}
