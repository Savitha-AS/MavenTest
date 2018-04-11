package com.mip.application.view;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mip.application.model.UserDetails;

public class DeductionReportVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -58022437644254972L;

	private String fromDate;
	private String toDate;
	private String role;
	private String filterOption;
	private String monthSel;
	private List<UserDetails> userDetailsList;
	private Map<Integer, CustDeductionReportVO> deductionReport;
	private List<String> dateRange;

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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getFilterOption() {
		return filterOption;
	}

	public void setFilterOption(String filterOption) {
		this.filterOption = filterOption;
	}

	public List<UserDetails> getUserDetailsList() {
		return userDetailsList;
	}

	public void setUserDetailsList(List<UserDetails> userDetailsList) {
		this.userDetailsList = userDetailsList;
	}

	public List<String> getDateRange() {
		return dateRange;
	}

	public void setDateRange(List<String> dateRange) {
		this.dateRange = dateRange;
	}

	public String getMonthSel() {
		return monthSel;
	}

	public void setMonthSel(String monthSel) {
		this.monthSel = monthSel;
	}

	public Map<Integer, CustDeductionReportVO> getDeductionReport() {
		return deductionReport;
	}

	public void setDeductionReport(Map<Integer, CustDeductionReportVO> deductionReport) {
		this.deductionReport = deductionReport;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DeductionReportVO [fromDate=");
		builder.append(fromDate);
		builder.append(", toDate=");
		builder.append(toDate);
		builder.append(", role=");
		builder.append(role);
		builder.append(", filterOption=");
		builder.append(filterOption);
		builder.append(", monthSel=");
		builder.append(monthSel);
		builder.append(", userDetailsList=");
		builder.append(userDetailsList);
		builder.append(", deductionReport=");
		builder.append(deductionReport);
		builder.append(", dateRange=");
		builder.append(dateRange);
		builder.append("]");
		return builder.toString();
	}

}
