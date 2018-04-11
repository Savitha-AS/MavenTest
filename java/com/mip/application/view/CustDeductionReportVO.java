package com.mip.application.view;

import java.io.Serializable;
import java.math.BigDecimal;

public class CustDeductionReportVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8023255159029562168L;

	private BigDecimal partialDedCountXL;
	private BigDecimal partialDedCountHP;
	private BigDecimal partialDedCountIP;
	private BigDecimal fullyDedCountXL;
	private BigDecimal fullyDedCountHP;
	private BigDecimal fullyDedCountIP;
	private BigDecimal partialDedCountXLPrev;
	private BigDecimal partialDedCountHPPrev;
	private BigDecimal partialDedCountIPPrev;
	private BigDecimal fullyDedCountXLPrev;
	private BigDecimal fullyDedCountHPPrev;
	private BigDecimal fullyDedCountIPPrev;
	

	public BigDecimal getPartialDedCountXL() {
		return partialDedCountXL;
	}

	public void setPartialDedCountXL(BigDecimal partialDedCountXL) {
		this.partialDedCountXL = partialDedCountXL;
	}

	public BigDecimal getPartialDedCountHP() {
		return partialDedCountHP;
	}

	public void setPartialDedCountHP(BigDecimal partialDedCountHP) {
		this.partialDedCountHP = partialDedCountHP;
	}

	public BigDecimal getPartialDedCountIP() {
		return partialDedCountIP;
	}

	public void setPartialDedCountIP(BigDecimal partialDedCountIP) {
		this.partialDedCountIP = partialDedCountIP;
	}

	public BigDecimal getFullyDedCountXL() {
		return fullyDedCountXL;
	}

	public void setFullyDedCountXL(BigDecimal fullyDedCountXL) {
		this.fullyDedCountXL = fullyDedCountXL;
	}

	public BigDecimal getFullyDedCountHP() {
		return fullyDedCountHP;
	}

	public void setFullyDedCountHP(BigDecimal fullyDedCountHP) {
		this.fullyDedCountHP = fullyDedCountHP;
	}

	public BigDecimal getFullyDedCountIP() {
		return fullyDedCountIP;
	}

	public void setFullyDedCountIP(BigDecimal fullyDedCountIP) {
		this.fullyDedCountIP = fullyDedCountIP;
	}
	
	

	public BigDecimal getPartialDedCountXLPrev() {
		return partialDedCountXLPrev;
	}

	public void setPartialDedCountXLPrev(BigDecimal partialDedCountXLPrev) {
		this.partialDedCountXLPrev = partialDedCountXLPrev;
	}

	public BigDecimal getPartialDedCountHPPrev() {
		return partialDedCountHPPrev;
	}

	public void setPartialDedCountHPPrev(BigDecimal partialDedCountHPPrev) {
		this.partialDedCountHPPrev = partialDedCountHPPrev;
	}

	public BigDecimal getPartialDedCountIPPrev() {
		return partialDedCountIPPrev;
	}

	public void setPartialDedCountIPPrev(BigDecimal partialDedCountIPPrev) {
		this.partialDedCountIPPrev = partialDedCountIPPrev;
	}

	public BigDecimal getFullyDedCountXLPrev() {
		return fullyDedCountXLPrev;
	}

	public void setFullyDedCountXLPrev(BigDecimal fullyDedCountXLPrev) {
		this.fullyDedCountXLPrev = fullyDedCountXLPrev;
	}

	public BigDecimal getFullyDedCountHPPrev() {
		return fullyDedCountHPPrev;
	}

	public void setFullyDedCountHPPrev(BigDecimal fullyDedCountHPPrev) {
		this.fullyDedCountHPPrev = fullyDedCountHPPrev;
	}

	public BigDecimal getFullyDedCountIPPrev() {
		return fullyDedCountIPPrev;
	}

	public void setFullyDedCountIPPrev(BigDecimal fullyDedCountIPPrev) {
		this.fullyDedCountIPPrev = fullyDedCountIPPrev;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustDeductionReportVO [partialDedCountXL=");
		builder.append(partialDedCountXL);
		builder.append(", partialDedCountHP=");
		builder.append(partialDedCountHP);
		builder.append(", partialDedCountIP=");
		builder.append(partialDedCountIP);
		builder.append(", fullyDedCountXL=");
		builder.append(fullyDedCountXL);
		builder.append(", fullyDedCountHP=");
		builder.append(fullyDedCountHP);
		builder.append(", fullyDedCountIP=");
		builder.append(fullyDedCountIP);
		builder.append(", partialDedCountXLPrev=");
		builder.append(partialDedCountXLPrev);
		builder.append(", partialDedCountHPPrev=");
		builder.append(partialDedCountHPPrev);
		builder.append(", partialDedCountIPPrev=");
		builder.append(partialDedCountIPPrev);
		builder.append(", fullyDedCountXLPrev=");
		builder.append(fullyDedCountXLPrev);
		builder.append(", fullyDedCountHPPrev=");
		builder.append(fullyDedCountHPPrev);
		builder.append(", fullyDedCountIPPrev=");
		builder.append(fullyDedCountIPPrev);
		builder.append("]");
		return builder.toString();
	}

	

}
