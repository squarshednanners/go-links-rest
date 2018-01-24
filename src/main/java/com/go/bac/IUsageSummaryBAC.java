package com.go.bac;

import java.util.List;

import com.go.model.UsageSummary;

public interface IUsageSummaryBAC {

	void deleteUsageSummary(Integer usageSummaryDaysToKeep);

	List<UsageSummary> fetchAllHourlySummaries();

	void calculateHourlySummary();

	List<UsageSummary> fetchAllDailySummaries();

	void calculateDailySummary();

	UsageSummary fetchTotalSummary();
}
