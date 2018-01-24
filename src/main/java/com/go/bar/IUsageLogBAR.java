package com.go.bar;

import java.util.List;

import com.go.model.UsageLog;
import com.go.model.SortDirection;

public interface IUsageLogBAR {
	void log(UsageLog log);

	List<UsageLog> fetchPagedLogsSortedAscending(Integer pageNumber, Integer pageSize);

	List<UsageLog> fetchPagedLogsSortedDescending(Integer pageNumber, Integer pageSize);

	List<UsageLog> fetchUsageLogsByInterval(Long startTimeInMillis, Long endTimeInMillis, SortDirection sortDirection);

	void deleteLog(UsageLog log);
}
