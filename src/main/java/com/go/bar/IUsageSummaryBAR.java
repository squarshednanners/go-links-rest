package com.go.bar;

import java.util.List;

import com.go.model.UsageSummary;

public interface IUsageSummaryBAR {

	List<UsageSummary> fetchAllHourlySummaries();

	List<UsageSummary> fetchPagedHourlySummary(Integer pageNumber, Integer pageSize);

	void addHourlySummary(UsageSummary summary);

	void deleteHourlySummary(UsageSummary summary);

	List<UsageSummary> fetchAllDailySummaries();

	List<UsageSummary> fetchPagedDailySummary(Integer pageNumber, Integer pageSize);

	void addDailySummary(UsageSummary summary);

	void deleteDailySummary(UsageSummary summary);

	UsageSummary fetchTotalSummary();

	void setTotalSummary(UsageSummary summary);
}
