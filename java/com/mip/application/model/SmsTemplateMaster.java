package com.mip.application.model;

import java.util.Date;

/**
 * @author thiyaneshwaran_r
 *
 */
public class SmsTemplateMaster extends BaseModel implements
		java.io.Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -7940397128715681482L;

	private int smsTemplateId;
	private String smsTemplateName;
	private String smsTemplateDescription;
	private String smsTemplate;
	private int smsPlaceHoldersCount;
	private UserDetails createdBy;
	private Date createdDate;
	private UserDetails modifiedBy;
	private Date modifiedDate;
	private int smsValidity;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SmsTemplateMaster [smsTemplateId=");
		builder.append(smsTemplateId);
		builder.append(", smsTemplateName=");
		builder.append(smsTemplateName);
		builder.append(", smsTemplateDescription=");
		builder.append(smsTemplateDescription);
		builder.append(", smsTemplate=");
		builder.append(smsTemplate);
		builder.append(", smsPlaceHoldersCount=");
		builder.append(smsPlaceHoldersCount);
		//builder.append(", createdBy=");
		//builder.append(createdBy);
		builder.append(", createdDate=");
		builder.append(createdDate);
		//builder.append(", modifiedBy=");
		//builder.append(modifiedBy);
		builder.append(", modifiedDate=");
		builder.append(modifiedDate);
		builder.append(", smsValidty=");
		builder.append(smsValidity);
		builder.append("]");
		return builder.toString();
	}

	public int getSmsTemplateId() {
		return smsTemplateId;
	}

	public void setSmsTemplateId(int smsTemplateId) {
		this.smsTemplateId = smsTemplateId;
	}

	public String getSmsTemplateName() {
		return smsTemplateName;
	}

	public void setSmsTemplateName(String smsTemplateName) {
		this.smsTemplateName = smsTemplateName;
	}

	public String getSmsTemplateDescription() {
		return smsTemplateDescription;
	}

	public void setSmsTemplateDescription(String smsTemplateDescription) {
		this.smsTemplateDescription = smsTemplateDescription;
	}

	public String getSmsTemplate() {
		return smsTemplate;
	}

	public void setSmsTemplate(String smsTemplate) {
		this.smsTemplate = smsTemplate;
	}

	public int getSmsPlaceHoldersCount() {
		return smsPlaceHoldersCount;
	}

	public void setSmsPlaceHoldersCount(int smsPlaceHoldersCount) {
		this.smsPlaceHoldersCount = smsPlaceHoldersCount;
	}

	public UserDetails getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserDetails createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public UserDetails getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(UserDetails modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}


	/**
	 * @return the smsValidity
	 */
	public int getSmsValidity() {
		return smsValidity;
	}

	/**
	 * @param smsValidity the smsValidity to set
	 */
	public void setSmsValidity(int smsValidity) {
		this.smsValidity = smsValidity;
	}
}
