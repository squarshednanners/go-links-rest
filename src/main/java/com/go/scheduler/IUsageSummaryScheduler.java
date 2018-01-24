package com.go.scheduler;

public interface IUsageSummaryScheduler {

	void deleteUsageSummary();

	void calculateHourlySummary();

	void calculateDailySummary();
}
