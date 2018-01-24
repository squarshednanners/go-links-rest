package com.go.bac;

public interface IUsageLogBAC {

	void logUsage(String linkName);

	void deleteUsageLogs(Integer daysToKeep);

}
