package com.mip.application.view;

import java.io.Serializable;
import java.util.Map;

public class ReportCustomerVO implements Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 2252097351562418095L;
	
	private String regMode;	
	private String regType;
	private String filterOption;
	private String fromDate;
	private String toDate;
	private String confStat;
	private int totalCustCount;
	private String repStyle;
	private String reportStartRow;	
	private String reportEndRow;	
	private Map<String,String> reportRangeMap;	
	private String reportRange;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReportCustomerVO [repStyle=");
		builder.append(repStyle);
		builder.append(", regMode=");
		builder.append(regMode);
		builder.append(", regType=");
		builder.append(regType);
		builder.append(", filterOption=");
		builder.append(filterOption);
		builder.append(", fromDate=");
		builder.append(fromDate);
		builder.append(", toDate=");
		builder.append(toDate);
		builder.append(", confStat=");
		builder.append(confStat);
		builder.append(", reportStartRow=");
		builder.append(reportStartRow);
		builder.append(", totalCustCount=");
		builder.append(totalCustCount);
		builder.append(", reportEndRow=");
		builder.append(reportEndRow);
		builder.append(", reportRangeMap=");
		builder.append(reportRangeMap);
		builder.append(", reportRange=");
		builder.append(reportRange);
		builder.append("]");
		return builder.toString();
	}

	public String getRegMode() {
		return regMode;
	}
	public void setRegMode(String regMode) {
		this.regMode = regMode;
	}
	public String getRegType() {
		return regType;
	}
	public void setRegType(String regType) {
		this.regType = regType;
	}
	public String getFilterOption() {
		return filterOption;
	}
	public void setFilterOption(String filterOption) {
		this.filterOption = filterOption;
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
	public String getConfStat() {
		return confStat;
	}
	public void setConfStat(String confStat) {
		this.confStat = confStat;
	}
	public int getTotalCustCount() {
		return totalCustCount;
	}
	public void setTotalCustCount(int totalCustCount) {
		this.totalCustCount = totalCustCount;
	}
	public String getRepStyle() {
		return repStyle;
	}
	public void setRepStyle(String repStyle) {
		this.repStyle = repStyle;
	}
	public String getReportStartRow() {
		return reportStartRow;
	}
	public void setReportStartRow(String reportStartRow) {
		this.reportStartRow = reportStartRow;
	}
	public String getReportEndRow() {
		return reportEndRow;
	}
	public void setReportEndRow(String reportEndRow) {
		this.reportEndRow = reportEndRow;
	}
	public Map<String, String> getReportRangeMap() {
		return reportRangeMap;
	}
	public void setReportRangeMap(Map<String, String> reportRangeMap) {
		this.reportRangeMap = reportRangeMap;
	}
	public String getReportRange() {
		return reportRange;
	}
	public void setReportRange(String reportRange) {
		this.reportRange = reportRange;
	}
}
