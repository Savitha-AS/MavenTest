package com.mip.application.view;

public class SMSPlaceHolderVO {
	
	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 1964182354143696152L;

	private String placeHolderCode;		
	private String placeHolderName;	
	private String placeHolderValue;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SMSPlaceHolderVO [placeHolderCode=");
		builder.append(placeHolderCode);
		builder.append(", placeHolderName=");
		builder.append(placeHolderName);
		builder.append(", placeHolderValue=");
		builder.append(placeHolderValue);
		builder.append("]");
		return builder.toString();
	}
	
	/**
	 * @return the placeHolderCode
	 */
	public String getPlaceHolderCode() {
		return placeHolderCode;
	}
	/**
	 * @return the placeHolderName
	 */
	public String getPlaceHolderName() {
		return placeHolderName;
	}
	/**
	 * @return the placeHolderValue
	 */
	public String getPlaceHolderValue() {
		return placeHolderValue;
	}
	/**
	 * @param placeHolderCode the placeHolderCode to set
	 */
	public void setPlaceHolderCode(String placeHolderCode) {
		this.placeHolderCode = placeHolderCode;
	}
	/**
	 * @param placeHolderName the placeHolderName to set
	 */
	public void setPlaceHolderName(String placeHolderName) {
		this.placeHolderName = placeHolderName;
	}
	/**
	 * @param placeHolderValue the placeHolderValue to set
	 */
	public void setPlaceHolderValue(String placeHolderValue) {
		this.placeHolderValue = placeHolderValue;
	}
}
