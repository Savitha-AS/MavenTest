package com.mip.application.model;

import java.math.BigDecimal;
import java.util.Date;

public class CoverHistory extends BaseModel implements java.io.Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -4635380455587647304L;
	
	private int coverHistoryId;
	private int custId;
	private String msisdn;
	private String prevMonthUsage;
	private String coverFree;
	private String chargesXL;
	private String coverXL;
	private String chargesHP;
	private String coverHP;
	
	private String month;
	private int year;
	private Date createdDate;
	
	public String toString() {
		return new StringBuilder()
			.append("CoverHistory [coverHistoryId=")
			.append(coverHistoryId)
			.append(", custId=")
			.append(custId)
			.append(", msisdn=")
			.append(msisdn)
			.append(", prevMonthUsage=")
			.append(prevMonthUsage)
			.append(", coverFree=")
			.append(coverFree)
			.append(", chargesXL=")
			.append(chargesXL)
			.append(", coverXL=")
			.append(coverXL)
			.append(", chargesHP=")
			.append(chargesHP)
			.append(", coverHP=")
			.append(coverHP)
			.append(", chargesXL=")
			.append(chargesXL)
			.append(", month=")
			.append(month)
			.append(", year=")
			.append(year)
			.append(", createdDate=")
			.append(createdDate)
			.append("]").toString();
	}

	public int getCoverHistoryId() {
		return coverHistoryId;
	}

	public void setCoverHistoryId(int coverHistoryId) {
		this.coverHistoryId = coverHistoryId;
	}

	public int getCustId() {
		return custId;
	}

	public void setCustId(int custId) {
		this.custId = custId;
	}

	public String getPrevMonthUsage() {
		return prevMonthUsage;
	}

	public void setPrevMonthUsage(String prevMonthUsage) {
		this.prevMonthUsage = prevMonthUsage;
	}

	public String getCoverFree() {
		return coverFree;
	}

	public void setCoverFree(String coverFree) {
		this.coverFree = coverFree;
	}

	public String getChargesXL() {
		return chargesXL;
	}

	public void setChargesXL(String chargesXL) {
		this.chargesXL = chargesXL;
	}

	public String getCoverXL() {
		return coverXL;
	}

	public void setCoverXL(String coverXL) {
		this.coverXL = coverXL;
	}

	public String getChargesHP() {
		return chargesHP;
	}

	public void setChargesHP(String chargesHP) {
		this.chargesHP = chargesHP;
	}

	public String getCoverHP() {
		return coverHP;
	}

	public void setCoverHP(String coverHP) {
		this.coverHP = coverHP;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}



	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}



	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	
}
