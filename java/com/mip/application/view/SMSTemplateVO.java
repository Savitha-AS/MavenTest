package com.mip.application.view;

import java.io.Serializable;

/*
 * HISTORY
 * =====================================================================
 * Version | Date        | Who              | Comments
 * ---------------------------------------------------------------------
 * 1.0     | 10/05/2011  | T H B S          |Initial Version.
 * =====================================================================
 */

/**
 * <p>
 * <code>SMSTemplateVO.java</code> contains all the setter and getter methods pertaining to  
 * SMS Template screen fields. This is a mapping class for all fields in create/edit SMS template
 * screen.
 * </p>
 * 
 * @author T H B S
 *
 */

public class SMSTemplateVO implements Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 3284034534439193050L;
	
	private String smsTemplateId;
	private String smsTemplate;
	private String smsPlaceHolder;
	private String smsValidity;

	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SMSTemplateVO [smsTemplateId=");
		builder.append(smsTemplateId);
		builder.append(", smsTemplate=");
		builder.append(smsTemplate);
		builder.append(", smsPlaceHolder=");
		builder.append(smsPlaceHolder);
		builder.append(", smsValidity=");
		builder.append(smsValidity);
		builder.append("]");
		return builder.toString();
	}


	/**
	 * @return the smsTemplateId
	 */
	public String getSmsTemplateId() {
		return smsTemplateId;
	}


	/**
	 * @return the smsTemplate
	 */
	public String getSmsTemplate() {
		return smsTemplate;
	}


	/**
	 * @return the smsPlaceHolder
	 */
	public String getSmsPlaceHolder() {
		return smsPlaceHolder;
	}


	/**
	 * @return the smsValidity
	 */
	public String getSmsValidity() {
		return smsValidity;
	}


	/**
	 * @param smsTemplateId the smsTemplateId to set
	 */
	public void setSmsTemplateId(String smsTemplateId) {
		this.smsTemplateId = smsTemplateId;
	}


	/**
	 * @param smsTemplate the smsTemplate to set
	 */
	public void setSmsTemplate(String smsTemplate) {
		this.smsTemplate = smsTemplate;
	}


	/**
	 * @param smsPlaceHolder the smsPlaceHolder to set
	 */
	public void setSmsPlaceHolder(String smsPlaceHolder) {
		this.smsPlaceHolder = smsPlaceHolder;
	}


	/**
	 * @param smsValidity the smsValidity to set
	 */
	public void setSmsValidity(String smsValidity) {
		this.smsValidity = smsValidity;
	}

	
}
