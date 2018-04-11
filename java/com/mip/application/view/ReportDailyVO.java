package com.mip.application.view;

import java.io.Serializable;

public class ReportDailyVO implements Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -8558760299296949289L;

	private String userUid;
	
	private int regFreeHandset;
	private int regXLHandset;
	private int regFreeLaptop;
	private int regXLLaptop;
	private int regFreeTotal;
	private int regXLTotal;
	private int confirmFree;
	private int confirmXL;
	
	private float avgFreeConfirm;
	private String leaveReason;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReportDailyVO [userUid=");
		builder.append(userUid);
		builder.append(", regFreeHandset=");
		builder.append(regFreeHandset);
		builder.append(", regXLHandset=");
		builder.append(regXLHandset);
		builder.append(", regFreeLaptop=");
		builder.append(regFreeLaptop);
		builder.append(", regXLLaptop=");
		builder.append(regXLLaptop);
		builder.append(", regFreeTotal=");
		builder.append(regFreeTotal);
		builder.append(", regXLTotal=");
		builder.append(regXLTotal);
		builder.append(", confirmFree=");
		builder.append(confirmFree);
		builder.append(", confirmXL=");
		builder.append(confirmXL);
		builder.append(", avgFreeConfirm=");
		builder.append(avgFreeConfirm);
		builder.append(", leaveReason=");
		builder.append(leaveReason);
		builder.append("]");
		return builder.toString();
	}

	public String getUserUid() {
		return userUid;
	}

	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	public int getRegFreeHandset() {
		return regFreeHandset;
	}

	public void setRegFreeHandset(int regFreeHandset) {
		this.regFreeHandset = regFreeHandset;
	}

	public int getRegXLHandset() {
		return regXLHandset;
	}

	public void setRegXLHandset(int regXLHandset) {
		this.regXLHandset = regXLHandset;
	}

	public int getRegFreeLaptop() {
		return regFreeLaptop;
	}

	public void setRegFreeLaptop(int regFreeLaptop) {
		this.regFreeLaptop = regFreeLaptop;
	}

	public int getRegXLLaptop() {
		return regXLLaptop;
	}

	public void setRegXLLaptop(int regXLLaptop) {
		this.regXLLaptop = regXLLaptop;
	}

	public int getRegFreeTotal() {
		return regFreeTotal;
	}

	public void setRegFreeTotal(int regFreeTotal) {
		this.regFreeTotal = regFreeTotal;
	}

	public int getRegXLTotal() {
		return regXLTotal;
	}

	public void setRegXLTotal(int regXLTotal) {
		this.regXLTotal = regXLTotal;
	}

	public int getConfirmFree() {
		return confirmFree;
	}

	public void setConfirmFree(int confirmFree) {
		this.confirmFree = confirmFree;
	}

	public int getConfirmXL() {
		return confirmXL;
	}

	public void setConfirmXL(int confirmXL) {
		this.confirmXL = confirmXL;
	}

	public float getAvgFreeConfirm() {
		return avgFreeConfirm;
	}

	public void setAvgFreeConfirm(float avgFreeConfirm) {
		this.avgFreeConfirm = avgFreeConfirm;
	}

	public String getLeaveReason() {
		return leaveReason;
	}

	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}
	
}
