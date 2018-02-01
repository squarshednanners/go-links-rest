package com.go.bac;

import java.util.List;

import com.go.model.UsageSummary;

public interface IUsageSummaryBAC {

	void calculateHourlySummary();

	void calculateDailySummary();

	List<UsageSummary> fetchAllHourlySummaries();

	List<UsageSummary> fetchAllDailySummaries();

	UsageSummary fetchTotalSummary();

	void deleteHourlyUsageSummary(Integer usageSummaryDaysToKeep);

	void deleteDailyUsageSummary(Integer usageSummaryDaysToKeep);
}
