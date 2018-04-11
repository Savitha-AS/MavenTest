package com.mip.application.model;

import java.util.Date;

public class SmsQueue extends BaseModel implements java.io.Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 6089371396195982126L;

	private int smsQueueId;
	private String smsText;
	private int smsMsisdn;
	private int smsValidity;
	private Date createdDate;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SmsQueue [smsQueueId=");
		builder.append(smsQueueId);
		builder.append(", smsText=");
		builder.append(smsText);
		builder.append(", smsMsisdn=");
		builder.append(smsMsisdn);
		builder.append(", smsValidity=");
		builder.append(smsValidity);
		builder.append(", createdDate=");
		builder.append(createdDate);
		builder.append("]");
		return builder.toString();
	}

	public int getSmsQueueId() {
		return smsQueueId;
	}

	public void setSmsQueueId(int smsQueueId) {
		this.smsQueueId = smsQueueId;
	}

	public String getSmsText() {
		return smsText;
	}

	public void setSmsText(String smsText) {
		this.smsText = smsText;
	}

	public int getSmsMsisdn() {
		return smsMsisdn;
	}

	public void setSmsMsisdn(int smsMsisdn) {
		this.smsMsisdn = smsMsisdn;
	}

	public int getSmsValidity() {
		return smsValidity;
	}

	public void setSmsValidity(int smsValidity) {
		this.smsValidity = smsValidity;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}
