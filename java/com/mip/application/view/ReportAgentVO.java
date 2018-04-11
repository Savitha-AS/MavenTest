package com.mip.application.view;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mip.application.model.UserDetails;

public class ReportAgentVO implements Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 6958819285332767055L;

	private String fromDate;
	private String toDate;
	private String role;
	private String filterOption;
	private List<UserDetails> userDetailsList;
	private Map<Integer, List<Map<String, List<ReportDailyNewVO>>>> allWeeksReport;
	
	private Map<String, ReportDailyNewVO> durationReport;
	
	private Map<Integer, List<Map<String, String>>> agentLeaves;
	
	private Map<Integer, List<UserOfferRegistrationVO>> offerRegDetails;
	
	private Map<Integer, List<DeregisteredCustomersVO>> deregFMCustomersDetails;
	private Map<Integer, List<DeregisteredCustomersVO>> deregXLCustomersDetails;
	private Map<Integer, List<DeregisteredCustomersVO>> deregHPCustomersDetails;
	private Map<Integer, List<ReportDailyNewVO>> confirmedXLandHPDetails;
	private Map<Integer, ReportDailyNewVO> registeredXLandHPDetailsMap;
	private Map<Integer, ReportDailyNewVO> registeredXLandIPDetailsMap;
	private Map<Integer, ReportDailyNewVO> confirmedXLandHPDetailsMap;
	
	private Map<Integer,Map<String, List<ReportDailyNewVO>>> agentDaywiseReportMap;
	private List<String> dateRange;
	private List<Integer> distinctBranchIdList;
	
	private Map<Integer, List<ReportDailyNewVO>> confirmedXLandIPDetails;
	private Map<Integer, ReportDailyNewVO> confirmedXLandIPDetailsMap;
	private Map<Integer, List<DeregisteredCustomersVO>> deregIPCustomersDetails;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReportAgentVO [fromDate=");
		builder.append(fromDate);
		builder.append(", toDate=");
		builder.append(toDate);
		builder.append(", role=");
		builder.append(role);
		builder.append(", filterOption=");
		builder.append(filterOption);
		/*builder.append(", userDetailsList=");
		builder.append(userDetailsList);*/
		builder.append(", durationReport=");
		builder.append(durationReport);
		builder.append(", allWeeksReport=");
		builder.append(allWeeksReport);
		builder.append(", agentLeaves=");
		builder.append(agentLeaves);
		builder.append(", offerRegDetails=");
		builder.append(offerRegDetails);
		builder.append(", deregFMCustomersDetails=");
		builder.append(deregFMCustomersDetails);
		builder.append(", deregXLCustomersDetails=");
		builder.append(deregXLCustomersDetails);
		builder.append(", deregHPCustomersDetails=");
		builder.append(deregHPCustomersDetails);
		builder.append(", confirmedXLandHPDetails=");
		builder.append(confirmedXLandHPDetails);	
		builder.append(", registeredXLandHPDetailsMap=");
		builder.append(registeredXLandHPDetailsMap);
		builder.append(", registeredXLandIPDetailsMap=");
		builder.append(registeredXLandIPDetailsMap);
		builder.append(", confirmedXLandHPDetailsMap=");
		builder.append(confirmedXLandHPDetailsMap);
		builder.append(", confirmedXLandIPDetails=");
		builder.append(confirmedXLandIPDetails);
		builder.append(", confirmedXLandIPDetailsMap=");
		builder.append(confirmedXLandIPDetailsMap);
		builder.append(", deregIPCustomersDetails=");
		builder.append(deregIPCustomersDetails);
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

	public Map<String, ReportDailyNewVO> getDurationReport() {
		return durationReport;
	}
	
	public void setDurationReport(Map<String, ReportDailyNewVO> durationReport) {
		this.durationReport = durationReport;
	}
	
	public Map<Integer, List<Map<String, List<ReportDailyNewVO>>>> getAllWeeksReport() {
		return allWeeksReport;
	}
	
	public void setAllWeeksReport(
			Map<Integer, List<Map<String, List<ReportDailyNewVO>>>> allWeeksReport) {
		this.allWeeksReport = allWeeksReport;
	}
	
	public List<UserDetails> getUserDetailsList() {
		return userDetailsList;
	}

	public void setUserDetailsList(List<UserDetails> userDetailsList) {
		this.userDetailsList = userDetailsList;
	}

	public Map<Integer, List<Map<String, String>>> getAgentLeaves() {
		return agentLeaves;
	}
	
	public void setAgentLeaves(
			Map<Integer, List<Map<String, String>>> agentLeaves) {
		this.agentLeaves = agentLeaves;
	}
	
	public Map<Integer, List<UserOfferRegistrationVO>> getOfferRegDetails() {
		return offerRegDetails;
	}
	
	public void setOfferRegDetails(Map<Integer, List<UserOfferRegistrationVO>> offerRegDetails) {
		this.offerRegDetails = offerRegDetails;
	}

	public Map<Integer, List<DeregisteredCustomersVO>> getDeregFMCustomersDetails() {
		return deregFMCustomersDetails;
	}

	public void setDeregFMCustomersDetails(
			Map<Integer, List<DeregisteredCustomersVO>> deregFMCustomersDetails) {
		this.deregFMCustomersDetails = deregFMCustomersDetails;
	}

	public Map<Integer, List<DeregisteredCustomersVO>> getDeregXLCustomersDetails() {
		return deregXLCustomersDetails;
	}

	public void setDeregXLCustomersDetails(
			Map<Integer, List<DeregisteredCustomersVO>> deregXLCustomersDetails) {
		this.deregXLCustomersDetails = deregXLCustomersDetails;
	}

	public Map<Integer, List<DeregisteredCustomersVO>> getDeregHPCustomersDetails() {
		return deregHPCustomersDetails;
	}
	
	public void setDeregHPCustomersDetails(
			Map<Integer, List<DeregisteredCustomersVO>> deregHPCustomersDetails) {
		this.deregHPCustomersDetails = deregHPCustomersDetails;
	}
	
	public Map<Integer, List<ReportDailyNewVO>> getConfirmedXLandHPDetails() {
		return confirmedXLandHPDetails;
	}
	
	public void setConfirmedXLandHPDetails(
			Map<Integer, List<ReportDailyNewVO>> confirmedXLandHPDetails) {
		this.confirmedXLandHPDetails = confirmedXLandHPDetails;
	}
	
	public Map<Integer, ReportDailyNewVO> getRegisteredXLandHPDetailsMap() {
		return registeredXLandHPDetailsMap;
	}
	
	public void setRegisteredXLandHPDetailsMap(
			Map<Integer, ReportDailyNewVO> registeredXLandHPDetailsMap) {
		this.registeredXLandHPDetailsMap = registeredXLandHPDetailsMap;
	}
	
	
	
	public Map<Integer, ReportDailyNewVO> getRegisteredXLandIPDetailsMap() {
		return registeredXLandIPDetailsMap;
	}

	public void setRegisteredXLandIPDetailsMap(
			Map<Integer, ReportDailyNewVO> registeredXLandIPDetailsMap) {
		this.registeredXLandIPDetailsMap = registeredXLandIPDetailsMap;
	}

	public Map<Integer, ReportDailyNewVO> getConfirmedXLandHPDetailsMap() {
		return confirmedXLandHPDetailsMap;
	}
	
	public void setConfirmedXLandHPDetailsMap(
			Map<Integer, ReportDailyNewVO> confirmedXLandHPDetailsMap) {
		this.confirmedXLandHPDetailsMap = confirmedXLandHPDetailsMap;
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

	public Map<Integer, Map<String, List<ReportDailyNewVO>>> getAgentDaywiseReportMap() {
		return agentDaywiseReportMap;
	}

	public void setAgentDaywiseReportMap(
			Map<Integer, Map<String, List<ReportDailyNewVO>>> agentDaywiseReportMap) {
		this.agentDaywiseReportMap = agentDaywiseReportMap;
	}

	public List<String> getDateRange() {
		return dateRange;
	}

	public void setDateRange(List<String> dateRange) {
		this.dateRange = dateRange;
	}

	public List<Integer> getDistinctBranchIdList() {
		return distinctBranchIdList;
	}

	public void setDistinctBranchIdList(List<Integer> distinctBranchIdList) {
		this.distinctBranchIdList = distinctBranchIdList;
	}

	public Map<Integer, List<ReportDailyNewVO>> getConfirmedXLandIPDetails() {
		return confirmedXLandIPDetails;
	}

	public void setConfirmedXLandIPDetails(
			Map<Integer, List<ReportDailyNewVO>> confirmedXLandIPDetails) {
		this.confirmedXLandIPDetails = confirmedXLandIPDetails;
	}

	public Map<Integer, ReportDailyNewVO> getConfirmedXLandIPDetailsMap() {
		return confirmedXLandIPDetailsMap;
	}

	public void setConfirmedXLandIPDetailsMap(
			Map<Integer, ReportDailyNewVO> confirmedXLandIPDetailsMap) {
		this.confirmedXLandIPDetailsMap = confirmedXLandIPDetailsMap;
	}

	public Map<Integer, List<DeregisteredCustomersVO>> getDeregIPCustomersDetails() {
		return deregIPCustomersDetails;
	}

	public void setDeregIPCustomersDetails(
			Map<Integer, List<DeregisteredCustomersVO>> deregIPCustomersDetails) {
		this.deregIPCustomersDetails = deregIPCustomersDetails;
	}

	
	
	
	
}
